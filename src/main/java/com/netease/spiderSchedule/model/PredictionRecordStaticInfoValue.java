package com.netease.spiderSchedule.model;

import java.util.Map;

import com.netease.spiderSchedule.model.prediction.PredictionSpiderRecordStaticInfo;

public class PredictionRecordStaticInfoValue {

	private Map<String, Integer> timeSliceAddNumMap;
	private PredictionSpiderRecordStaticInfo predictRecord;
	private PredictionSpiderRecordStaticInfo trueRecord;

	public Map<String, Integer> getTimeSliceAddNumMap() {
		return timeSliceAddNumMap;
	}

	public void setTimeSliceAddNumMap(Map<String, Integer> timeSliceAddNumMap) {
		this.timeSliceAddNumMap = timeSliceAddNumMap;
	}

	public PredictionSpiderRecordStaticInfo getPredictRecord() {
		return predictRecord;
	}

	public void setPredictRecord(PredictionSpiderRecordStaticInfo predictRecord) {
		this.predictRecord = predictRecord;
	}

	public PredictionSpiderRecordStaticInfo getTrueRecord() {
		return trueRecord;
	}

	public void setTrueRecord(PredictionSpiderRecordStaticInfo trueRecord) {
		this.trueRecord = trueRecord;
	}

		
}
