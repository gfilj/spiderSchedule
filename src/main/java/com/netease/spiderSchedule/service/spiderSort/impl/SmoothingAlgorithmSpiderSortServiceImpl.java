package com.netease.spiderSchedule.service.spiderSort.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.netease.spiderSchedule.model.SpiderRateInfo;
import com.netease.spiderSchedule.model.SpiderScheduleDto;
import com.netease.spiderSchedule.model.prediction.SmoothingAlgorithmSpiderScheduleDto;
import com.netease.spiderSchedule.service.spiderRateInfo.SpiderRateInfoService;
import com.netease.spiderSchedule.service.spiderSort.SpiderSortService;
import com.netease.spiderSchedule.util.RateLevel;
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
			if (timeSimulator == null) {
				timeSimulator = TimeSimulator.getNow();
			}

			int addCount = 0;//static的数目
			int timeSliceKey = timeSimulator.getTimeSliceKey();
			int countOld = 0;//static tooold的数目
			int countWheel = 0;
			for (SpiderRateInfo spiderRateInfo : spiderRateInfoService.getRateMap().values()) {
				SmoothingAlgorithmSpiderScheduleDto smoothingAlgorithmSpiderScheduleDto;
				smoothingAlgorithmSpiderScheduleDto = new SmoothingAlgorithmSpiderScheduleDto(spiderRateInfo,
						timeSimulator);
				if (smoothingAlgorithmSpiderScheduleDto.getScore() > 0) {
					boolean canPut = true;
					if (spiderRateInfo.isTooOld()) {
						canPut = false;
						countOld++;
						//在9点以后才进行抓取
						if (timeSliceKey >= 9 * 12 && countOld < (spiderRateInfoService.getCountTooOld() / 144)) {
							canPut = true;
						}
					}
					//每5分钟限制放入轮刷的号
//					if(smoothingAlgorithmSpiderScheduleDto.isWheel()){
//						canPut = false;
//						countWheel++;
//						if(countWheel <=(spiderRateInfoService.getRateMap().size()/6) ){
//							canPut = true;
//						}
//					}
					
					//
//					if(timeSliceKey == 85){
//						System.out.println(smoothingAlgorithmSpiderScheduleDto.getScore());
//					}
					if (canPut) {
						addCount++;
						heapSort.add(smoothingAlgorithmSpiderScheduleDto);
					}
				}
			}

			logger.info("SmoothingAlgorithmSpiderSortServiceImpl do call addTask add " + addCount + " currentSliceKey "
					+ timeSliceKey);
			return addCount;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
}
