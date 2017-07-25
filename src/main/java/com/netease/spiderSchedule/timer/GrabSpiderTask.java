package com.netease.spiderSchedule.timer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.spiderSchedule.controller.SpiderScheduleController;
import com.netease.spiderSchedule.ip.VPSHttp;
import com.netease.spiderSchedule.service.spiderRateInfo.SpiderRateInfoService;
import com.netease.spiderSchedule.service.spiderSort.SpiderSortService;
import com.netease.spiderSchedule.service.spiderSourceInfo.SpiderSourceInfoService;
import com.netease.spiderSchedule.timer.model.Request;
import com.netease.spiderSchedule.util.DateUtil;

public class GrabSpiderTask implements Runnable {
	private static final String SEARCH_REGEX_PRE = "<a target=\"_blank\" uigs=\"account_name_0\" href=\"([\\s\\S]*?)\">[\\s\\S]*?";
	private static final String SEARCH_REGEX_END = "</label>";

	private static final String CONTENTLIST = "var\\s+msgList\\s+=\\s+\\{(.*?)\\};";
	private static final Pattern CONTENT_LIST_PATTERN = Pattern.compile(CONTENTLIST);

	private static final String SEARCH_REFER = "http://weixin.sogou.com/";
	private static final String ESSAY_HOST = "http://mp.weixin.qq.com";
	private static int searchSuccess = 0;
	private static int listSuccess = 0;
	protected static Logger logger = Logger.getLogger(GrabSpiderTask.class);
	private String sourceid;
	private JSONObject ipJson;
	private int priority;
	private int appid;
	private SpiderRateInfoService spiderRateInfoService;

	private SpiderSortService spiderSortService;
	
	private SpiderSourceInfoService spiderSourceInfoService;
	/**
	 * @param sourceid
	 * @param ipJson
	 */
	public GrabSpiderTask(String sourceid, JSONObject ipJson, int priority, int appid,
			SpiderRateInfoService spiderRateInfoService, SpiderSortService spiderSortService, SpiderSourceInfoService spiderSourceInfoService) {
		this.sourceid = sourceid;
		this.ipJson = ipJson;
		this.priority = priority;
		this.appid = appid;
		this.spiderRateInfoService = spiderRateInfoService;
		this.spiderSortService = spiderSortService;
		this.spiderSourceInfoService = spiderSourceInfoService;
	}

	public void run() {
		Map<String, String> maps = new HashMap<String, String>();
		String ip = ipJson.getString("ip");
		String port = ipJson.getString("port");
		String machine = ipJson.getString("machine");
		maps.put("machine", machine);
		String listurl = "";
		ipJson.put("status", true);
		try {
			// search
			String result = null;
			String searchURl = "http://weixin.sogou.com/weixin?type=1&query=" + sourceid
					+ "&ie=utf8&_sug_=n&_sug_type_=";
			try {
				result = VPSHttp.getInstance().sendHttpGet(searchURl, ip, port, SEARCH_REFER);
			} catch (Exception e) {
				e.printStackTrace();
				ipError(maps, machine, "ip is error：" + machine, false);
				return;
			}

			if (result.equals("error") || result.indexOf("您的访问出错了") != -1) {
				ipError(maps, machine, "search error:" + machine, false);
				return;

			} else {
				Pattern pattern = Pattern.compile(SEARCH_REGEX_PRE + sourceid + SEARCH_REGEX_END);
				Matcher m = pattern.matcher(result);
				if (m.find()) {
					listurl = m.group(1).replace("amp;", "");
					logger.info("search success：" + searchSuccess++ + ", listurl:" + listurl + ", ip:" + ip);
				} else {
					if (result.contains("相关的官方认证订阅号")) {
						logger.info("搜索失败：" + sourceid + ", ip:" + ip);
						ipError(maps, machine, "search false：" + result + ", ip:" + ip, false);
						spiderRateInfoService.getRateMap().remove(sourceid);
						// intodb
						spiderSourceInfoService.updateBySourceid(sourceid);
					}
					logger.info("search false：" + result + ", ip:" + ip);
					ipError(maps, machine, "search false：" + result + ", ip:" + ip, true);
				}
			}
			Random ran = new Random();
//			int sleepTime = ran.nextInt(500) + 500;
//			logger.info("sleep time" + sleepTime);
			Thread.sleep(500);

			// list
			String contentlist = null;
			try {
				contentlist = VPSHttp.getInstance().sendHttpGet(listurl, ip, port, searchURl);
			} catch (Exception e) {
				e.printStackTrace();
				ipError(maps, machine, "ip is error：：" + machine, true);
				// 可以考虑将此消息封装到
				return;
			}
			if (contentlist.equals("error") || contentlist.indexOf("请输入验证码") != -1) {
				listError(maps, machine, "list is error" + machine);
				return;
			} else {
				success(maps);
			}


			Matcher contentListMatcher = CONTENT_LIST_PATTERN.matcher(contentlist);
			if (contentListMatcher.find()) {
				String contList = contentListMatcher.group(1);
				if (contList != null && !"".equals(contList)) {
					contList = "{" + contList + "}";
					JSONObject jsonObject = JSON.parseObject(contList);
					JSONArray list = jsonObject.getJSONArray("list");
					if (list != null && !list.isEmpty()) {
						for (int j = 0; j < list.size(); j++) {
							JSONObject obj = list.getJSONObject(j);
							JSONObject cmi = obj.getJSONObject("comm_msg_info");
							String time = cmi.getString("datetime");
							String today = DateUtil.formatTime(cmi.getString("datetime"), "yyyy-MM-dd");
							String nowDay = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
							if (nowDay.equals(today)) {
								JSONObject amei = obj.getJSONObject("app_msg_ext_info");
								String contentUrl = amei.get("content_url").toString().replaceAll("amp;", "");
								String title = amei.get("title").toString();
								SpiderScheduleController.getWeixinListRequest()
										.add(getRequest(contentUrl, title, time, listurl));
								JSONArray jsonArray = amei.getJSONArray("multi_app_msg_item_list");
								if (jsonArray != null && !jsonArray.isEmpty()) {
									for (int z = 0; z < jsonArray.size(); z++) {
										JSONObject subJson = jsonArray.getJSONObject(z);
										String subUrl = subJson.getString("content_url").replaceAll("amp;", "");
										String subTitle = subJson.get("title").toString();
										// 添加文章
										SpiderScheduleController.getWeixinListRequest()
												.add(getRequest(subUrl, subTitle, time, listurl));
									}
								}
							}
						}
					}
				}
			}
			Thread.sleep(500);
		} catch (Exception e) {
			logger.info(this.toString() + "is error", e);
			if ((boolean) ipJson.get("status")) {
				ipError(maps, machine, "" + machine, true);
			}
		}
	}

	@Override
	public String toString() {
		return "GrabSpiderTask [sourceid=" + sourceid + ", ipJson=" + ipJson + ", priority=" + priority + ", appid="
				+ appid + "]";
	}

	public void success(Map<String, String> maps) {
		logger.info("list success ：" + listSuccess++);
		ipJson.put("status", false);
		VPSHttp.getInstance().sendHttpPost("http://vps.ws.netease.com/incproxyip.action", maps);// 自增
		VPSHttp.getInstance().sendHttpPost("http://vps.ws.netease.com/updatefree.action", maps);// 置为空闲，其他项目可以使用
	}

	public void ipError(Map<String, String> maps, String machine, String message, boolean regrab) {
		logger.info(message);
		// VPSHttp.getInstance().sendHttpPost("http://vps.ws.netease.com/updatestatus.action",
		// maps);
		ipJson.put("status", false);
		VPSHttp.getInstance().sendHttpPost("http://vps.ws.netease.com/restartip.action", maps);
		if (regrab) {
			spiderSortService.addErrorTask(this.sourceid, spiderRateInfoService);
		}
	}

	public void listError(Map<String, String> maps, String machine, String message) {
		logger.info(message);
		ipJson.put("status", false);
		VPSHttp.getInstance().sendHttpPost("http://vps.ws.netease.com/incproxyip.action", maps);// 自增
		VPSHttp.getInstance().sendHttpPost("http://vps.ws.netease.com/updatefree.action", maps);// 置为空闲，其他项目可以使用
		spiderSortService.addErrorTask(this.sourceid, spiderRateInfoService);
	}

	public static final String WEIXIN_CONTENT = "weixin_content";
	public static final String REQUEST_HEADER_REFERER = "requestHeaderReferer";

	public Request getRequest(String url, String title, String time, String listUrl) {
		Request request = new Request();
		if (url != null && !"".equals(url)) {
			String contentUrl = url.replace("amp;", "").replace("\\", "");
			if (!contentUrl.startsWith("http")) {
				contentUrl = ESSAY_HOST + contentUrl;
			}
			ConcurrentHashMap<String, Object> extras = new ConcurrentHashMap<String, Object>();
			extras.put("pageName", WEIXIN_CONTENT);
			extras.put("taskName", WEIXIN_CONTENT);
			extras.put(REQUEST_HEADER_REFERER, listUrl);
			extras.put("time", time);
			request.setExtras(extras);
			request.setWhether_proxy(false);
			request.setUrl(contentUrl.replace("&amp;", "&"));
			title = Matcher.quoteReplacement(title);
			request.setTitle(title.replace("&nbsp;", " "));
			request.setSourceid(sourceid);
			request.setAppid(appid);
			request.setPriority(priority + 1);

		}
		logger.info("get list request: " + request);
		return request;
	}

}
