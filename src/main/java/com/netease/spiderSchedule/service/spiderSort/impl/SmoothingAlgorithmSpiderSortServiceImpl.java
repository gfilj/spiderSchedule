package com.netease.spiderSchedule.service.spiderSort.impl;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.netease.spiderSchedule.model.SpiderRateInfo;
import com.netease.spiderSchedule.model.prediction.SmoothingAlgorithmSpiderScheduleDto;
import com.netease.spiderSchedule.service.spiderRateInfo.SpiderRateInfoService;
import com.netease.spiderSchedule.service.spiderSort.SpiderSortService;
import com.netease.spiderSchedule.util.TimeSimulator;

@Service("smoothingAlgorithmSpiderSortService")
public class SmoothingAlgorithmSpiderSortServiceImpl extends SpiderSortServiceImpl implements SpiderSortService {
	private TimeSimulator timeSimulator;
	protected Logger logger = Logger.getLogger(getClass());
	public void setTimeSimulator(TimeSimulator timeSimulator) {
		this.timeSimulator = timeSimulator;
	}

	@Override
	public int addTask(SpiderRateInfoService spiderRateInfoService) {
		try {
			int addCount = 0;
			int timeSliceKey = 0;
			if (timeSimulator == null) {
				timeSliceKey = TimeSimulator.getTimeSliceKey(new Date());
			} else {
				timeSliceKey = timeSimulator.getTimeSliceKey();
			}
			int countOld = 0;
			for (SpiderRateInfo spiderRateInfo : spiderRateInfoService.getRateMap().values()) {
				SmoothingAlgorithmSpiderScheduleDto smoothingAlgorithmSpiderScheduleDto;
				if (timeSimulator == null) {

					smoothingAlgorithmSpiderScheduleDto = new SmoothingAlgorithmSpiderScheduleDto(spiderRateInfo);
				} else {
					smoothingAlgorithmSpiderScheduleDto = new SmoothingAlgorithmSpiderScheduleDto(spiderRateInfo,
							timeSimulator);
				}
				if (smoothingAlgorithmSpiderScheduleDto.getScore() > 0) {
					boolean canPut = true;
					if (spiderRateInfo.isTooOld()) {
						canPut = false;
						countOld++;
						if(timeSliceKey >=9*12&&countOld < (spiderRateInfoService.getCountTooOld()/144)){
							canPut = true;
						}
					}
					if (canPut) {
						addCount++;
						heapSort.add(smoothingAlgorithmSpiderScheduleDto);
					}
				}
			}

			logger.info("SmoothingAlgorithmSpiderSortServiceImpl do call addTask add " + addCount
					+ " currentSliceKey " + timeSliceKey);
			return addCount;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
