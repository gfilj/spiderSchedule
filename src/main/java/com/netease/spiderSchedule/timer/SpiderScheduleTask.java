package com.netease.spiderSchedule.timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.netease.spiderSchedule.controller.SpiderScheduleController;
import com.netease.spiderSchedule.service.spiderRateInfo.SpiderRateInfoService;
import com.netease.spiderSchedule.service.spiderSort.SpiderSortService;

public class SpiderScheduleTask {

	@Autowired
	private SpiderRateInfoService spiderRateInfoService;
	
	@Autowired
	@Qualifier("smoothingAlgorithmSpiderSortService")
	private SpiderSortService spiderSortService;
	
	/**
	 * 凌晨统计
	 */
	public void zeroSchedule(){
		spiderRateInfoService.generateRateMap(0,9);
		SpiderScheduleController.errorHandleMap.clear();
		perFiveMinutesSchedule();
	}
	
	/**
	 * 相隔5分钟计算任务数
	 */
	
	public void perFiveMinutesSchedule(){
		SpiderScheduleController.calAbility.setReset(true);
		spiderSortService.addTask(spiderRateInfoService);
	}

}
