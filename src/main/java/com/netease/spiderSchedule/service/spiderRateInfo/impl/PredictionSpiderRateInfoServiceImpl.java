package com.netease.spiderSchedule.service.spiderRateInfo.impl;

import java.util.Calendar;

import org.springframework.stereotype.Service;

import com.netease.spiderSchedule.model.SpiderRateInfo;
import com.netease.spiderSchedule.model.SpiderScheduleDto;
import com.netease.spiderSchedule.service.spiderRateInfo.PredictionSpiderRateInfoService;
import com.netease.spiderSchedule.util.TimeSimulator;

@Service("predictionSpiderRateInfoService")
public class PredictionSpiderRateInfoServiceImpl extends SpiderRateInfoServiceImpl
		implements PredictionSpiderRateInfoService {

	private TimeSimulator timeSimulator;

	@Override
	public void generateRateMap(int start, int end) {
		super.generateRateMap(start, end);
		// updateTime
		Calendar cal = Calendar.getInstance();
		cal.setTime(timeSimulator.getDate());
		cal.add(Calendar.DAY_OF_MONTH, -1);
		rateMap.forEach((k, v) -> {
			if (v.isTooOld()) {
				v.setUpdate_time(cal.getTime());
			}else{
				v.setUpdate_time(timeSimulator.getDate());
			}
		});
	}

	@Override
	public void updateRateMap(SpiderScheduleDto spiderScheduleDto) {
		SpiderRateInfo spiderRateInfo = rateMap.get(spiderScheduleDto.getSourceId());
		if (spiderRateInfo != null) {
			spiderRateInfo.setUpdate_time(timeSimulator.getDate());
		}
	}

	@Override
	public void setTimeSimulator(TimeSimulator timeSimulator) {
		this.timeSimulator = timeSimulator;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}
}
