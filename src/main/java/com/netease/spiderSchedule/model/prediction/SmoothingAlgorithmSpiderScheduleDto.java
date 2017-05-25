package com.netease.spiderSchedule.model.prediction;

import java.util.Calendar;
import java.util.Date;

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
	
	
	public SmoothingAlgorithmSpiderScheduleDto(SpiderRateInfo spiderRateInfo) {
		super(spiderRateInfo);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int timeSliceKey = (calendar.get(Calendar.HOUR) * 60 + calendar.get(Calendar.MINUTE)) / 5 ;
		Double timeSliceCountValue = spiderRateInfo.getTimeSlicePredict().get(timeSliceKey);
		if(timeSliceCountValue == null){
			timeSliceCountValue = 0d;
		}
		setRate(timeSliceCountValue);
		DelayLevel delayLevel = DelayLevel.getDelayLevel(getLastUpdateTime());
		setDelayVal(delayLevel.getDelayVal());
		countScore(spiderRateInfo);
		
	}

}
