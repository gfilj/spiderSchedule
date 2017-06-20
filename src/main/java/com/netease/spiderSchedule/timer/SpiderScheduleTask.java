package com.netease.spiderSchedule.timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.netease.spiderSchedule.controller.SpiderScheduleController;
import com.netease.spiderSchedule.service.spiderRateInfo.SpiderRateInfoService;
import com.netease.spiderSchedule.service.spiderSort.SpiderSortService;

public class SpiderScheduleTask {

	@Autowired
	@Qualifier("spiderRateInfoService")
	private SpiderRateInfoService spiderRateInfoService;
	
	@Autowired
	@Qualifier("smoothingAlgorithmSpiderSortService")
	private SpiderSortService spiderSortService;
	
	/**
	 * 凌晨统计
	 */
	public void zeroSchedule(){
		spiderRateInfoService.generateRateMap(0,9);
	}
	
	/**
	 * 相隔5分钟计算任务数
	 */
	
	public void perFiveMinutesSchedule(){
		
		spiderSortService.addTask(spiderRateInfoService);
		SpiderScheduleController.errorHandleMap.clear();
	}
	
	/**
	 * 相隔20s
	 */
	public void per20sSchedule(){
		SpiderScheduleController.calAbility.setReset(true);
	}
	
	/**
	 * 半个小时清除队列任务
	 */
	
	public void cleanTaskQueue(){
		spiderRateInfoService.cleanTaskQueue();
	}
}
