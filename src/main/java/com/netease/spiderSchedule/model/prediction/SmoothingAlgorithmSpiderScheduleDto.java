package com.netease.spiderSchedule.model.prediction;

import com.netease.spiderSchedule.model.SpiderRateInfo;
import com.netease.spiderSchedule.model.SpiderScheduleDto;
import com.netease.spiderSchedule.util.DelayLevel;
import com.netease.spiderSchedule.util.TimeSimulator;

public class SmoothingAlgorithmSpiderScheduleDto extends SpiderScheduleDto{

	public SmoothingAlgorithmSpiderScheduleDto(SpiderRateInfo spiderRateInfo, TimeSimulator timeSimulator) {
		super(spiderRateInfo);
		Double timeSliceCountValue = getTimeSliceCountValue(spiderRateInfo,timeSimulator.getTimeSliceKey());
		if(timeSliceCountValue == null){
			timeSliceCountValue = 0d;
		}
		setRate(timeSliceCountValue);
		DelayLevel delayLevel = DelayLevel.getDelayLevel(getLastUpdateTime(),timeSimulator);
		setDelayVal(delayLevel.getDelayVal());
		countScore(spiderRateInfo);
	}
	
	
	/*public SmoothingAlgorithmSpiderScheduleDto(SpiderRateInfo spiderRateInfo) {
		super(spiderRateInfo);
		Double timeSliceCountValue = getTimeSliceCountValue(spiderRateInfo, TimeSimulator.getTimeSliceKey(new Date()));
		if(timeSliceCountValue == null){
			timeSliceCountValue = 0d;
		}
		setRate(timeSliceCountValue);
		DelayLevel delayLevel = DelayLevel.getDelayLevel(getLastUpdateTime());
		setDelayVal(delayLevel.getDelayVal());
		countScore(spiderRateInfo);
		
	}*/


	public Double getTimeSliceCountValue(SpiderRateInfo spiderRateInfo, int timeSliceKey) {
		return spiderRateInfo.getTimeSlicePredict().get((timeSliceKey)%288);
	}

}
