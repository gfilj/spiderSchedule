package com.netease.spiderSchedule.ip;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.spiderSchedule.util.DecaptchaDemo;

public class TestVps {
	private static final String SEARCH_REGEX_PRE = "<a target=\"_blank\" uigs=\"account_name_0\" href=\"([\\s\\S]*?)\">[\\s\\S]*?";
	private static final String SEARCH_REGEX_END = "</label>";
	private static final Pattern pattern = Pattern.compile(SEARCH_REGEX_PRE + "people_rmw" + SEARCH_REGEX_END);
	// private static final String searchR = "<input type=\"hidden\" name=\"r\"
	// id=\"from\" value=\"[\\s\\S]*?\" >";
	private static final Pattern searchRPattern = Pattern.compile("<img id=\"verify_img\" src=\"([\\s\\S]*?)\">");
	private static final Pattern searchUrlPattern = Pattern.compile(
			"<img id=\"seccodeImage\" onload=\"setImgCode\\(1\\)\" onerror=\"setImgCode\\(0\\)\" src=\"([\\s\\S]*?)\" width=\"100\" height=\"40\" alt=\"请输入图中的验证码\" title=\"请输入图中的验证码\">");

	private static final String identify_code_prefix = "http://weixin.sogou.com/antispider/";
	private static final String submit_code_url = "http://weixin.sogou.com/antispider/thank.php";

	private static final String CONTENTLIST = "var\\s+msgList\\s+=\\s+\\{(.*?)\\};";
	private static final Pattern contentListPattern = Pattern.compile(CONTENTLIST);

	public static void main(String[] args) {
		new TestVps().run();
	}

	// 当前ip是否有效
	public void run() {
		Map<String, Object> identifyCodeMap = new HashMap<String, Object>();
		int zzz = 0;
		int sss = 0;
		while (true) {
			String listurl = "";
			try {
				// 访问微信搜索页
				Map<String, Object> result = null;
				// try {
				// result =
				// VPSHttp.getInstance().sendHttpGet("http://weixin.sogou.com/weixin?type=1&query="
				// + "people_rmw" + "&ie=utf8&_sug_=n&_sug_type_=",
				// "http://weixin.sogou.com/");
				// } catch (Exception e) {
				// e.printStackTrace();
				// System.out.println(result);
				// continue;
				// }
				// if (result.size() > 0) {
				// int status = (int) result.get("status");
				// if (status == 302) {
				// System.err.println(result.get("location"));
				// System.out.println("==========================");
				// } else {
				// String page = String.valueOf(result.get("result"));
				//// System.err.println(page);
				// if(page.contains("请输入图中的验证码")){
				// System.out.println("==========================");
				// String identifyUrl = identify_code_prefix +
				// getPatternStr(searchUrlPattern,page);
				// System.out.println(identifyUrl);
				// String rStr = getPatternStr(searchRPattern,page);
				// System.out.println(rStr);
				//
				// String identifyCodeStr =
				// DecaptchaDemo.getIdentifyCode(identifyUrl);
				// JSONObject identifyCodeJson =
				// JSON.parseObject(identifyCodeStr);
				// Map<String, String> submitMaps = new HashMap<String,
				// String>();
				// //{"msg":"OK","code":0,"data":"geyzzq"}
				// if("OK".equals(identifyCodeJson.getString("msg"))){
				// submitMaps.put("c", identifyCodeJson.getString("data"));
				// }
				// submitMaps.put("r", rStr);
				// submitMaps.put("v", "5");
				// System.out.println(submitMaps);
				// String identifyCodeResult =
				// VPSHttp.getInstance().sendHttpPost(submit_code_url,
				// submitMaps);
				// System.out.println(identifyCodeResult);
				// }else{
				// String listUrl =
				// getPatternStr(pattern,page).replaceAll("amp;", "");
				// System.out.println(listUrl);
				Map<String, Object> contentlist = VPSHttp.getInstance().sendHttpGet(
						"https://mp.weixin.qq.com/profile?src=3&timestamp=1501472818&ver=1&signature=pQHYYLowxMdZkMY1QWWeunarmp*ARU99KFqEgEktTtSSh8uhL-GtqKAXos4Guy9X9R8xkvqP*TwafR4ySyU9Og==",
						"http://weixin.sogou.com/weixin?type=1&query=people_rmw&ie=utf8&_sug_=n&_sug_type_=");
				if (contentlist.size() > 0) {
					int status = (int) contentlist.get("status");
					String page = String.valueOf(contentlist.get("result"));
//					System.out.println(contentlist);
					if (status == 200) {
						if (page.contains("请输入验证码")) {
							System.out.println("=====================");
							Random ran = new Random();
							String cert = +new Date().getTime() + "." + ran.nextInt(10000);
							String identifyUrl = "https://mp.weixin.qq.com/mp/verifycode?cert=" + cert;
							Map<String, Object> identifyCodeOrgin = DecaptchaDemo.getIdentifyCode(identifyUrl);
							String identifyCodeStr = (String)identifyCodeOrgin.get("resp");
							JSONObject identifyCodeJson = JSON.parseObject(identifyCodeStr);
							Map<String, String> submitMaps = new HashMap<String, String>();
							if ("OK".equals(identifyCodeJson.getString("msg"))) {
								submitMaps.put("input", identifyCodeJson.getString("data"));
							}
							submitMaps.put("cert", cert);
							System.out.println(submitMaps);
							String identifyCodeResult = VPSHttp.getInstance().sendHttpPost("https://mp.weixin.qq.com/mp/verifycode", submitMaps, (String)identifyCodeOrgin.get("cookie"));
							System.out.println(identifyCodeResult);
						}else{
							System.out.println("列表页成功");
						}
					}
				}
				Thread.sleep(5000);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String getPatternStr(Pattern pattern, String page) {
		Matcher matcher = pattern.matcher(page);
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return "";
		}
	}
}
