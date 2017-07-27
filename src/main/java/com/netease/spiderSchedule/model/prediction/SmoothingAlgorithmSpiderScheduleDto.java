package com.netease.spiderSchedule.model.prediction;

import com.netease.spiderSchedule.model.SpiderRateInfo;
import com.netease.spiderSchedule.model.SpiderScheduleDto;
import com.netease.spiderSchedule.util.DelayLevel;
import com.netease.spiderSchedule.util.TimeSimulator;

public class SmoothingAlgorithmSpiderScheduleDto extends SpiderScheduleDto{

	public SmoothingAlgorithmSpiderScheduleDto(SpiderRateInfo spiderRateInfo, TimeSimulator timeSimulator) {
		super(spiderRateInfo);
		int lastTimeUpdateKey = TimeSimulator.getTimeSliceKey(spiderRateInfo.getUpdate_time());
		Double timeSliceCountValue = getTimeSliceCountValue(spiderRateInfo,timeSimulator.getTimeSliceKey(),lastTimeUpdateKey);
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


	public Double getTimeSliceCountValue(SpiderRateInfo spiderRateInfo, int timeSliceKey, int lastTimeUpdateKey) {
//		if(this.isHighQuality()){
//			int highTimeSliceKey = (timeSliceKey-2)%288;
//			Integer d = spiderRateInfo.getTimeSliceCount().get(highTimeSliceKey<0?288+timeSliceKey-2:highTimeSliceKey);
//			if(d==null){
//				d=0;
//			}
//			return Double.valueOf(d);
//		}else{
//			
//			return spiderRateInfo.getTimeSlicePredict().get((timeSliceKey)%288);
//		}
		if(lastTimeUpdateKey>timeSliceKey){
			//说明还没下发过
			lastTimeUpdateKey = -1;
		}
		
		Double max = 0d;
		for(int i = lastTimeUpdateKey + 1; i<=timeSliceKey; i++){
			Double predictValue = spiderRateInfo.getTimeSlicePredict().get((i)%288);
			if(predictValue == null)continue;
			if(max < predictValue){
				max = predictValue;
			}
		}
		return max;
	}

}
