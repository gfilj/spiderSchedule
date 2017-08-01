package com.netease.spiderSchedule.ip;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.spiderSchedule.util.DateUtil;
import com.netease.spiderSchedule.util.DecaptchaDemo;

public class TestVpsYanZhengMa {
	private static final String SEARCH_REGEX_PRE = "<a target=\"_blank\" uigs=\"account_name_0\" href=\"([\\s\\S]*?)\">[\\s\\S]*?";
	private static final String SEARCH_REGEX_END = "</label>";
	private static final Pattern pattern = Pattern.compile(SEARCH_REGEX_PRE + "tennisWorldMagazine" + SEARCH_REGEX_END, Pattern.CASE_INSENSITIVE);

	private static final String CONTENTLIST = "var\\s+msgList\\s+=\\s+\\{(.*?)\\};";
	private static final Pattern contentListPattern = Pattern.compile(CONTENTLIST);

	public static void main(String[] args) {
//		new TestVpsYanZhengMa().run();
		String str = "<a target=\"_blank\" uigs"
				+ "=\"account_name_0\" href=\"http://mp.weixin.qq.com/profile?src=3&amp;timestamp=1501567697&amp;ver=1&amp;signature=NZi6bQWApxXdQgqvKXftNpcAS2T4ldtw0o9W1ZdHggJWipZ6tuC-o5S7jW0zBLemYDLlVJ*1yxb7Bxb5hgkoAQ==\">《网球天地》杂志</a>\r\n</p>\r\n<p class=\"info\">微信号：<label name=\"em_weixinhao\">TennisWorldMagazine</label>";
		System.out.println(str);
		Matcher m = pattern.matcher(str);
		if (m.find()) {
			System.out.println(m.group(1).replaceAll("amp;", ""));
		}
	}

	// 当前ip是否有效
	public void run() {
		int zzz = 0;
		int sss = 0;
		
		while (true) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("size", "5");// 需要ip个数
			maps.put("type", "schedule");// 需要ip个数
			String proxyjson = VPSHttp.getInstance().sendHttpPost("http://vps.ws.netease.com/getProxyUsable.action",
					maps);// 获取接口
			JSONArray json = JSON.parseArray(proxyjson);
			if (json != null) {
				for (int i = 0; i < json.size(); i++) {
					JSONObject jsonobject = json.getJSONObject(i);
					String ip = jsonobject.getString("ip");
					String port = jsonobject.getString("port");
					String machine = jsonobject.getString("machine");
					maps.put("machine", machine);
					String listurl = "";
					try {
						// 访问微信搜索页
						String result = null;
						try {
							result = VPSHttp.getInstance()
									.sendHttpGet(
											"http://weixin.sogou.com/weixin?type=1&query=" + "tennisWorldMagazine"
													+ "&ie=utf8&_sug_=n&_sug_type_=",
											ip, port, "http://weixin.sogou.com/");
						} catch (Exception e) {
							// e.printStackTrace();
							System.out.println("代理软件出现问题：" + machine);
							VPSHttp.getInstance().sendHttpPost("http://vps.ws.netease.com/updatestatus.action",
									maps);
							// 重新拨号
							VPSHttp.getInstance().sendHttpPost("http://vps.ws.netease.com/restartip.action", maps);
							continue;
						}
						if (result.equals("error") || result.indexOf("您的访问出错了") != -1) {
							System.out.println("失败的机器" + machine);
							// 更新ip状态为不可用，避免拨号失败，死机等原因造成的不可用ip使抓取延迟
							VPSHttp.getInstance().sendHttpPost("http://vps.ws.netease.com/updatestatus.action",
									maps);
							// 重新拨号
							VPSHttp.getInstance().sendHttpPost("http://vps.ws.netease.com/restartip.action", maps);

						} else {
							// System.out.println(result);
							Matcher m = pattern.matcher(result);
							if (m.find()) {
								listurl = m.group(1).replaceAll("amp;", "");
							}
							System.out.println("搜索成功：" + zzz++ + ", listurl" + listurl);
						}
						Thread.sleep(500);

						String contentlist = null;
						try {
							contentlist = VPSHttp.getInstance().sendHttpGet(listurl, ip, port,
									"http://weixin.sogou.com/weixin?type=1&query=" + "gh_7aa898d08b05"
											+ "&ie=utf8&_sug_=n&_sug_type_=");
						} catch (Exception e) {
							// e.printStackTrace();
							System.out.println("代理软件出现问题：" + machine);
							VPSHttp.getInstance().sendHttpPost("http://vps.ws.netease.com/updatestatus.action",
									maps);
							// 重新拨号
							VPSHttp.getInstance().sendHttpPost("http://vps.ws.netease.com/restartip.action", maps);
							continue;
						}
						int retryTime = 0;
						while(contentlist.equals("error") || contentlist.indexOf("请输入验证码") != -1) {
							if(retryTime>=3){
								break;
							}
							System.out.println("-----begin input identify code");
							Random ran = new Random();
							String cert = +new Date().getTime() + "." + ran.nextInt(10000);
							String identifyUrl = "https://mp.weixin.qq.com/mp/verifycode?cert=" + cert;
							Map<String, Object> identifyCodeOrgin = DecaptchaDemo.getIdentifyCode(identifyUrl,ip,port);
							String identifyCodeStr = (String)identifyCodeOrgin.get("resp");
							JSONObject identifyCodeJson = JSON.parseObject(identifyCodeStr);
							Map<String, String> submitMaps = new HashMap<String, String>();
							if ("OK".equals(identifyCodeJson.getString("msg"))) {
								submitMaps.put("input", identifyCodeJson.getString("data"));
							}
							submitMaps.put("cert", cert);
							System.out.println(submitMaps);
							String identifyCodeResult = VPSHttp.getInstance().sendHttpPost("https://mp.weixin.qq.com/mp/verifycode", submitMaps, (String)identifyCodeOrgin.get("cookie"),ip,port);
							Thread.sleep(2000 + new Random().nextInt(1000));
							System.out.println(identifyCodeResult);
							
							contentlist = VPSHttp.getInstance().sendHttpGet(listurl, ip, port,
									"http://weixin.sogou.com/weixin?type=1&query=" + "people_rmw"
											+ "&ie=utf8&_sug_=n&_sug_type_=");
							retryTime++;
							
						}
						if(contentlist.equals("error") || contentlist.indexOf("请输入验证码") != -1){
							System.out.println("失败的机器" + machine);
							// 更新ip状态为不可用，避免拨号失败，死机等原因造成的不可用ip使抓取延迟
							VPSHttp.getInstance().sendHttpPost("http://vps.ws.netease.com/updatestatus.action",
									maps);
							// 重新拨号
							VPSHttp.getInstance().sendHttpPost("http://vps.ws.netease.com/restartip.action", maps);
							continue;
						}else{
							
							// System.out.println(result);
							System.out.println("列表成功：" + sss++);
							VPSHttp.getInstance().sendHttpPost("http://vps.ws.netease.com/incproxyip.action", maps);// 自增
							VPSHttp.getInstance().sendHttpPost("http://vps.ws.netease.com/updatefree.action", maps);// 置为空闲，其他项目可以使用
						}
						

						Matcher contentListMatcher = contentListPattern.matcher(contentlist);
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
											String contentUrl = amei.get("content_url").toString().replaceAll("amp;",
													"");
											String title = amei.get("title").toString();
											System.out.println(title + ":" + contentUrl);
											// page.addTargetRequest(getRequest(contentUrl,title,
											// time,page));
											JSONArray jsonArray = amei.getJSONArray("multi_app_msg_item_list");
											if (jsonArray != null && !jsonArray.isEmpty()) {
												for (int z = 0; z < jsonArray.size(); z++) {
													JSONObject subJson = jsonArray.getJSONObject(z);
													String subUrl = subJson.getString("content_url").replaceAll("amp;",
															"");
													String subTitle = subJson.get("title").toString();
													System.out.println(subTitle + ":" + subUrl);
													// page.addTargetRequest(getRequest(subUrl,
													// subTitle, time, page));
												}
											}
										}
									}
								}
							}
						}

						Thread.sleep(500);

					} catch (Exception e) {
					}
				}
			}
			// try {
			// Thread.sleep(5000);//休眠5秒钟
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
		}
	}
}
