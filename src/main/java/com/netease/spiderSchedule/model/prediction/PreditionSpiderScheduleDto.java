package com.netease.spiderSchedule.model.prediction;

import com.netease.spiderSchedule.model.SpiderRateInfo;
import com.netease.spiderSchedule.model.SpiderScheduleDto;
import com.netease.spiderSchedule.util.DelayLevel;
import com.netease.spiderSchedule.util.PriorityLevel;
import com.netease.spiderSchedule.util.RateLevel;
import com.netease.spiderSchedule.util.TimeSimulator;

public class PreditionSpiderScheduleDto extends SpiderScheduleDto{

	public PreditionSpiderScheduleDto(SpiderRateInfo spiderRateInfo, TimeSimulator timeSimulator) {
		super(spiderRateInfo);
		Integer timeSliceCountValue = spiderRateInfo.getTimeSliceCount().get(timeSimulator.getTimeSliceKey());
		if(timeSliceCountValue != null){
			setRate(Double.valueOf(timeSliceCountValue)/spiderRateInfo.getTotalCount());
		}else{
			setRate(0d);
		}
		DelayLevel delayLevel = DelayLevel.getDelayLevel(getLastUpdateTime(),timeSimulator);
		setDelayVal(delayLevel.getDelayVal());
		countScore(spiderRateInfo);
		
	}

}
