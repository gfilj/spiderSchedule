package com.netease.spiderSchedule.service.spiderSort;

import java.util.List;

import com.netease.spiderSchedule.model.SpiderScheduleDto;
import com.netease.spiderSchedule.service.spiderRateInfo.SpiderRateInfoService;

public interface SpiderSortService {
	
	List<SpiderScheduleDto> getTask(int taskNum, SpiderRateInfoService spiderRateInfoService);
	void addTask(SpiderRateInfoService spiderRateInfoService);
}
