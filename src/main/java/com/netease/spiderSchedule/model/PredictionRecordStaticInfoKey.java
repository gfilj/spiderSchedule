package com.netease.spiderSchedule.model;

public class PredictionRecordStaticInfoKey {
	
	private int dayInterval;
	private int combineTimeSlice;
	private int tashNum;
	
	public int getDayInterval() {
		return dayInterval;
	}
	public void setDayInterval(int dayInterval) {
		this.dayInterval = dayInterval;
	}
	public int getCombineTimeSlice() {
		return combineTimeSlice;
	}
	public void setCombineTimeSlice(int combineTimeSlice) {
		this.combineTimeSlice = combineTimeSlice;
	}
	
	public int getTashNum() {
		return tashNum;
	}
	
	public void setTashNum(int tashNum) {
		this.tashNum = tashNum;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PredictionRecordStaticInfoKey)) {
			return false;
		}
		PredictionRecordStaticInfoKey predictionRecordStaticInfoKey = (PredictionRecordStaticInfoKey) obj;
		if (predictionRecordStaticInfoKey.getDayInterval() != this.getDayInterval() || predictionRecordStaticInfoKey.getCombineTimeSlice() != this.getCombineTimeSlice()||predictionRecordStaticInfoKey.getTashNum() != this.getTashNum()) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return (getDayInterval() + "/" + getCombineTimeSlice() + "/" + getTashNum() ).hashCode();
	}
	
	public static PredictionRecordStaticInfoKey getInstance(int dayInterval, int combineTimeSlice, int taskNum){
		PredictionRecordStaticInfoKey predictionRecordStaticInfoKey = new PredictionRecordStaticInfoKey();
		predictionRecordStaticInfoKey.setCombineTimeSlice(combineTimeSlice);
		predictionRecordStaticInfoKey.setDayInterval(dayInterval);
		predictionRecordStaticInfoKey.setTashNum(taskNum);
		return predictionRecordStaticInfoKey;
	}
}
