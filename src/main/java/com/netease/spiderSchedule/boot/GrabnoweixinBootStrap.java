package com.netease.spiderSchedule.boot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.netease.spiderSchedule.ip.VPSHttp;
import com.netease.spiderSchedule.model.SpiderSourceInfo;
import com.netease.spiderSchedule.service.spiderSourceInfo.impl.SpiderSourceInfoServiceImpl;
import com.netease.spiderSchedule.timer.GrabSpiderNotExitsTask;

public class GrabnoweixinBootStrap {
	private static ClassPathXmlApplicationContext context;
	private static ExecutorService executor = Executors.newFixedThreadPool(5);

	public static void main(String[] args) throws InterruptedException {
		start();
	}

	public static void start() throws InterruptedException {
		context = new ClassPathXmlApplicationContext("classpath*:config/spring-application.xml");
		context.start();
		// 便利
		SpiderSourceInfoServiceImpl bean = context.getBean(SpiderSourceInfoServiceImpl.class);

		int offset = 0;
		List<SpiderSourceInfo> selectAll = bean.selectAll();
		System.out.println(selectAll.size());
		while (offset < selectAll.size()) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("size", "5");// 需要ip个数
			maps.put("type", "schedule");// 类型
			String proxyjson = VPSHttp.getInstance().sendHttpPost("http://vps.ws.netease.com/getProxyUsable.action",
					maps);// 获取接口
			JSONArray json = JSON.parseArray(proxyjson);
			for (int i = 0; i < json.size(); i++) {
				if (json.getJSONObject(i) == null) {
					continue;
				}
				SpiderSourceInfo spiderSourceInfo = selectAll.get(offset);
				if (spiderSourceInfo != null) {
					executor.submit(new GrabSpiderNotExitsTask(spiderSourceInfo.getSourceid(), json.getJSONObject(i), 0, 0));
				} else {
					System.out.println(offset);
				}
				offset++;
			}
			Thread.sleep(5000);
		}

		while (GrabSpiderNotExitsTask.grabSpiderTaskList.size() > 0) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("size", "5");// 需要ip个数
			maps.put("type", "schedule");//类型
			String proxyjson = VPSHttp.getInstance().sendHttpPost("http://vps.ws.netease.com/getProxyUsable.action",
					maps);// 获取接口
			JSONArray json = JSON.parseArray(proxyjson);
			for (int i = 0; i < json.size(); i++) {
				if (json.getJSONObject(i) == null) {
					continue;
				}
				GrabSpiderNotExitsTask spiderSourceInfo = GrabSpiderNotExitsTask.grabSpiderTaskList.remove(0);
				spiderSourceInfo.setIpJson(json.getJSONObject(i));
				executor.submit(spiderSourceInfo);
			}
			Thread.sleep(5000);
		}

	}

}
