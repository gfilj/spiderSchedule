package com.netease.spiderSchedule.timer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.netease.spiderSchedule.controller.SpiderScheduleController;
import com.netease.spiderSchedule.ip.VPSHttp;
import com.netease.spiderSchedule.model.SpiderScheduleDto;
import com.netease.spiderSchedule.service.spiderRateInfo.SpiderRateInfoService;
import com.netease.spiderSchedule.service.spiderSort.SpiderSortService;
import com.netease.spiderSchedule.service.spiderSourceInfo.SpiderSourceInfoService;

public class SpiderScheduleTask {

	@Autowired
	@Qualifier("spiderRateInfoService")
	private SpiderRateInfoService spiderRateInfoService;

	@Autowired
	@Qualifier("smoothingAlgorithmSpiderSortService")
	private SpiderSortService spiderSortService;
	
	@Autowired
	private SpiderSourceInfoService spiderSourceInfoService;
	
	private static ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(30);
	
	protected static Logger logger = Logger.getLogger(SpiderScheduleTask.class);
	/**
	 * 凌晨统计
	 */
	public void zeroSchedule() {
		spiderRateInfoService.generateRateMap(0, 9);
	}

	/**
	 * 相隔5分钟计算任务数
	 */

	public void perFiveMinutesSchedule() {

		spiderSortService.addTask(spiderRateInfoService);
		SpiderScheduleController.errorHandleMap.clear();
	}

	/**
	 * 相隔20s
	 */
	public void per20sSchedule() {
		SpiderScheduleController.calAbility.setReset(true);
	}

	/**
	 * per5sScheduleCron
	 */
	public void per5sSchedule() {
		// 获取ip
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("size", "13");// 需要ip个数
		maps.put("type", "schedule");// 类型
		String proxyjson = VPSHttp.getInstance().sendHttpPost("http://vps.ws.netease.com/getProxyUsable.action",
				maps);// 获取接口
		JSONArray json = JSON.parseArray(proxyjson);
		if (json != null) {
			for (int i = 0; i < json.size(); i++) {
				List<SpiderScheduleDto> task = spiderSortService.getTask(1, spiderRateInfoService);
				if(task.size()>0){
					SpiderScheduleDto spiderScheduleDto = task.get(0);
					logger.info("per5sSchedule go to crab " + spiderScheduleDto);
					executor.execute(new GrabSpiderTask(spiderScheduleDto.getSourceId().trim(),json.getJSONObject(i),spiderScheduleDto.getPriority(), spiderScheduleDto.getAppId(),spiderRateInfoService,spiderSortService,spiderSourceInfoService));
				}
			}
		}
		logger.info("executor free size " + executor.getActiveCount());
	}

	/**
	 * 半个小时清除队列任务
	 */

	public void cleanTaskQueue() {
		spiderRateInfoService.cleanTaskQueue();
	}
}
