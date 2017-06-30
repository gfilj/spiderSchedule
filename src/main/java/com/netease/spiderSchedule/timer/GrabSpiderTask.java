package com.netease.spiderSchedule.timer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.spiderSchedule.controller.SpiderScheduleController;
import com.netease.spiderSchedule.ip.VPSHttp;
import com.netease.spiderSchedule.timer.model.Request;
import com.netease.spiderSchedule.timer.model.SpiderRequestInfo;
import com.netease.spiderSchedule.util.DateUtil;

public class GrabSpiderTask implements Runnable {
	private static final String SEARCH_REGEX_PRE = "<a target=\"_blank\" uigs=\"account_name_0\" href=\"([\\s\\S]*?)\">[\\s\\S]*?";
	private static final String SEARCH_REGEX_END = "</label>";

	private static final String CONTENTLIST = "var\\s+msgList\\s+=\\s+\\{(.*?)\\};";
	private static final Pattern CONTENT_LIST_PATTERN = Pattern.compile(CONTENTLIST);

	private static final String SEARCH_REFER = "http://weixin.sogou.com/";
	private static final String ESSAY_HOST = "https://mp.weixin.qq.com";
	private static final String UUID = "content_weixin_spider";

	private Pattern varctPattern = Pattern.compile("var ct = \"([\\s\\S]*?)\"");
	private Pattern coverPicturePattern = Pattern.compile("var msg_cdn_url = \"([\\s\\S]*?)\"");
	private Pattern errPattern = Pattern
			.compile("(<div[^>]*\\s+style=['\"]global_error_msg warn['\"]>*\\s+操作过于频繁，请稍后再试。*\\s+</div>)");

	private static Pattern datasrcPattern = Pattern.compile("<img[^>]*\\s+data-src\\s*=\\s*['\"]([^>]*?)['\"][^>]*/?>");
	private static Pattern srcPattern = Pattern.compile("<img[^>]*\\s+src\\s*=\\s*['\"]([^>]*?)['\"][^>]*/?>");
	private static int searchSuccess = 0;
	private static int listSuccess = 0;
	protected static Logger logger = Logger.getLogger(GrabSpiderTask.class);
	private String sourceid;
	private JSONObject ipJson;
	private int priority;
	private int appid;

	/**
	 * @param sourceid
	 * @param ipJson
	 */
	public GrabSpiderTask(String sourceid, JSONObject ipJson, int priority, int appid) {
		this.sourceid = sourceid;
		this.ipJson = ipJson;
		this.priority = priority;
		this.appid = appid;
	}

	// Map<String,String> maps=new HashMap<String, String>();
	// maps.put("size", "5");//需要ip个数
	// String
	// proxyjson=VPSHttp.getInstance().sendHttpPost("http://test.nbot.netease.com/getProxyUsable.action",
	// maps);//获取接口
	// JSONArray json=JSON.parseArray(proxyjson);
	// if(json!=null){
	// for(int i=0;i<json.size();i++){
	// }
	// }
	public void run() {
		Map<String, String> maps = new HashMap<String, String>();
		String ip = ipJson.getString("ip");
		String port = ipJson.getString("port");
		String machine = ipJson.getString("machine");
		maps.put("machine", machine);
		String listurl = "";
		try {
			// search
			String result = null;
			String searchURl = "http://weixin.sogou.com/weixin?type=1&query=" + sourceid
					+ "&ie=utf8&_sug_=n&_sug_type_=";
			try {
				result = VPSHttp.getInstance().sendHttpGet(searchURl, ip, port, SEARCH_REFER);
			} catch (Exception e) {
				error(maps, machine, "代理软件出现问题：" + machine);
				return;
			}

			if (result.equals("error") || result.indexOf("您的访问出错了") != -1) {
				error(maps, machine, "失败的机器" + machine);
				return;

			} else {
				Pattern pattern = Pattern.compile(SEARCH_REGEX_PRE + sourceid + SEARCH_REGEX_END);
				Matcher m = pattern.matcher(result);
				if (m.find()) {
					listurl = m.group(1).replace("amp;", "");
					logger.info("搜索成功：" + searchSuccess++ + ", listurl:" + listurl + ", ip:" + ip);
				}else{
					logger.info("搜索失败：" + result  + ", ip:" + ip);
				}
			}

			Thread.sleep(500);

			// list
			String contentlist = null;
			try {
				contentlist = VPSHttp.getInstance().sendHttpGet(listurl, ip, port, searchURl);
			} catch (Exception e) {
				error(maps, machine, "代理软件出现问题：" + machine);
				return;
			}
			if (contentlist.equals("error") || contentlist.indexOf("请输入验证码") != -1) {
				error(maps, machine, "失败的机器" + machine);
				return;
			} else {
				success(maps);
			}

			// list request info
			List<SpiderRequestInfo> listSpiderRequestInfo = new ArrayList<SpiderRequestInfo>();

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
//								listSpiderRequestInfo.add(new SpiderRequestInfo(time, sourceid, title, contentUrl));
								SpiderScheduleController.getWeixinListRequest().add(getRequest(contentUrl, title, time, listurl));
								JSONArray jsonArray = amei.getJSONArray("multi_app_msg_item_list");
								if (jsonArray != null && !jsonArray.isEmpty()) {
									for (int z = 0; z < jsonArray.size(); z++) {
										JSONObject subJson = jsonArray.getJSONObject(z);
										String subUrl = subJson.getString("content_url").replaceAll("amp;", "");
										String subTitle = subJson.get("title").toString();
//										listSpiderRequestInfo
//												.add(new SpiderRequestInfo(time, sourceid, subTitle, subUrl));
										//添加文章
										SpiderScheduleController.getWeixinListRequest().add(getRequest(subUrl, subTitle, time, listurl));
									}
								}
							}
						}
					}
				}
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void success(Map<String, String> maps) {
		logger.info("列表成功：" + listSuccess++);
		VPSHttp.getInstance().sendHttpPost("http://test.nbot.netease.com/incproxyip.action", maps);// 自增
		VPSHttp.getInstance().sendHttpPost("http://test.nbot.netease.com/updatefree.action", maps);// 置为空闲，其他项目可以使用
	}

	public void error(Map<String, String> maps, String machine, String message) {
		logger.info(message);
//		VPSHttp.getInstance().sendHttpPost("http://test.nbot.netease.com/updatestatus.action", maps);
		VPSHttp.getInstance().sendHttpPost("http://test.nbot.netease.com/restartip.action", maps);
	}
	
	public static final String WEIXIN_CONTENT = "weixin_content";
	public static final String REQUEST_HEADER_REFERER = "requestHeaderReferer";
	public Request getRequest(String url,String title, String time, String listUrl) {
		Request request = new Request();
		if (url != null && !"".equals(url)) {
			String contentUrl = url.replace("amp;", "").replace("\\", "");
			if (!contentUrl.startsWith(ESSAY_HOST)) {
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
		return request;
	}

}
