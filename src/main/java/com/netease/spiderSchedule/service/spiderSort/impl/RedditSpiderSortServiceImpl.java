package com.netease.spiderSchedule.service.spiderSort.impl;

import org.springframework.stereotype.Service;

import com.netease.spiderSchedule.model.SpiderRateInfo;
import com.netease.spiderSchedule.model.prediction.RedditSpiderScheduleDto;
import com.netease.spiderSchedule.service.spiderRateInfo.SpiderRateInfoService;
import com.netease.spiderSchedule.util.TimeSimulator;
@Service("redditSpiderSortService")
public class RedditSpiderSortServiceImpl extends SpiderSortServiceImpl {
	private TimeSimulator timeSimulator;
	
	
	public void setTimeSimulator(TimeSimulator timeSimulator) {
		this.timeSimulator = timeSimulator;
	}


	@Override
	public int addTask(SpiderRateInfoService spiderRateInfoService) {
		int addCount = 0;
		for (SpiderRateInfo spiderRateInfo : spiderRateInfoService.getRateMap().values()) {
			RedditSpiderScheduleDto redditSpiderScheduleDto = new RedditSpiderScheduleDto(spiderRateInfo,
					timeSimulator);
			if (redditSpiderScheduleDto.getScore() > 0) {
				addCount++;
				heapSort.add(redditSpiderScheduleDto);
			}
		}
		int timeSliceKey = timeSimulator.getTimeSliceKey();

		System.out.println("PredictionSpiderSortServiceImpl do call addTask add " + addCount + " currentSliceKey " + timeSliceKey);
		return addCount;
	}
}
