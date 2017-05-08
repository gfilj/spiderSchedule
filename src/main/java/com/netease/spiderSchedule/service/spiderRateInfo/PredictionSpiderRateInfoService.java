package com.netease.spiderSchedule.service.spiderRateInfo;

import com.netease.spiderSchedule.util.TimeSimulator;

public interface PredictionSpiderRateInfoService extends SpiderRateInfoService{

	void setTimeSimulator(TimeSimulator timeSimulator);
}
