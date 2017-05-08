package com.netease.spiderSchedule.service.spiderSort.impl;

import org.springframework.stereotype.Service;

import com.netease.spiderSchedule.model.SpiderRateInfo;
import com.netease.spiderSchedule.model.prediction.RedditSpiderScheduleDto;
import com.netease.spiderSchedule.model.prediction.SmoothingAlgorithmSpiderScheduleDto;
import com.netease.spiderSchedule.service.spiderRateInfo.SpiderRateInfoService;
import com.netease.spiderSchedule.util.TimeSimulator;
@Service("smoothingAlgorithmSpiderSortService")
public class SmoothingAlgorithmSpiderSortServiceImpl extends SpiderSortServiceImpl {
	private TimeSimulator timeSimulator;
	
	
	public void setTimeSimulator(TimeSimulator timeSimulator) {
		this.timeSimulator = timeSimulator;
	}


	@Override
	public void addTask(SpiderRateInfoService spiderRateInfoService) {
		int addCount = 0;
		int timeSliceKey = timeSimulator.getTimeSliceKey();
		int countOld = 0;
		for (SpiderRateInfo spiderRateInfo : spiderRateInfoService.getRateMap().values()) {
			SmoothingAlgorithmSpiderScheduleDto smoothingAlgorithmSpiderScheduleDto = new SmoothingAlgorithmSpiderScheduleDto(spiderRateInfo,
					timeSimulator);
			if (smoothingAlgorithmSpiderScheduleDto.getScore() > 0) {
				boolean canPut = true;
				if(spiderRateInfo.isTooOld()){
					countOld ++;
					if(countOld >= 10){
						canPut = false;
					}else{
//						System.out.println(spiderRateInfo + "------" + smoothingAlgorithmSpiderScheduleDto + "------" + timeSliceKey);
					}
				}
				if(canPut){
					addCount++;
					heapSort.add(smoothingAlgorithmSpiderScheduleDto);
				}
			}
		}

		System.out.println("SmoothingAlgorithmSpiderSortServiceImpl do call addTask add " + addCount + " currentSliceKey " + timeSliceKey);
	}
}
