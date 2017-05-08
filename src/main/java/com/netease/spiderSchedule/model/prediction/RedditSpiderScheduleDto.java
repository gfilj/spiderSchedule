package com.netease.spiderSchedule.model.prediction;

import com.netease.spiderSchedule.model.SpiderRateInfo;
import com.netease.spiderSchedule.model.SpiderScheduleDto;
import com.netease.spiderSchedule.util.DelayLevel;
import com.netease.spiderSchedule.util.TimeSimulator;

public class RedditSpiderScheduleDto extends SpiderScheduleDto{

	public RedditSpiderScheduleDto(SpiderRateInfo spiderRateInfo, TimeSimulator timeSimulator) {
		super(spiderRateInfo);
		Integer timeSliceCountValue = spiderRateInfo.getTimeSliceCount().get(timeSimulator.getTimeSliceKey());
		if(timeSliceCountValue == null){
			timeSliceCountValue = 0;
		}
		setRate(timeSliceCountValue);
		DelayLevel delayLevel = DelayLevel.getDelayLevel(getLastUpdateTime(),timeSimulator);
		setDelayVal(delayLevel.getDelayVal());
		countScore(spiderRateInfo);
	}

}
