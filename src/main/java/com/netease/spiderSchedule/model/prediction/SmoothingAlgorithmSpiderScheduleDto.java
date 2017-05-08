package com.netease.spiderSchedule.model.prediction;

import com.netease.spiderSchedule.model.SpiderRateInfo;
import com.netease.spiderSchedule.model.SpiderScheduleDto;
import com.netease.spiderSchedule.util.DelayLevel;
import com.netease.spiderSchedule.util.RateLevel;
import com.netease.spiderSchedule.util.TimeSimulator;

public class SmoothingAlgorithmSpiderScheduleDto extends SpiderScheduleDto{

	public SmoothingAlgorithmSpiderScheduleDto(SpiderRateInfo spiderRateInfo, TimeSimulator timeSimulator) {
		super(spiderRateInfo);
		Double timeSliceCountValue = spiderRateInfo.getTimeSlicePredict().get(timeSimulator.getTimeSliceKey());
		if(timeSliceCountValue == null){
			timeSliceCountValue = 0d;
		}
		setRate(timeSliceCountValue);
		DelayLevel delayLevel = DelayLevel.getDelayLevel(getLastUpdateTime(),timeSimulator);
		setDelayVal(delayLevel.getDelayVal());
		countScore(spiderRateInfo);
	}

}
