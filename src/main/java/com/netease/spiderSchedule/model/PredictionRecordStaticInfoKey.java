package com.netease.spiderSchedule.model;

public class PredictionRecordStaticInfoKey {
	
	private int dayInterval;
	private int combineTimeSlice;
	private int tashNum;
	private int wheelScore;
	
	public int getWheelScore() {
		return wheelScore;
	}
	public void setWheelScore(int wheelScore) {
		this.wheelScore = wheelScore;
	}
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
		if (predictionRecordStaticInfoKey.getDayInterval() != this.getDayInterval() 
				|| predictionRecordStaticInfoKey.getCombineTimeSlice() != this.getCombineTimeSlice()
				||predictionRecordStaticInfoKey.getTashNum() != this.getTashNum()
				||predictionRecordStaticInfoKey.getWheelScore() != this.getWheelScore()) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return (getDayInterval() + "/" + getCombineTimeSlice() + "/" + getTashNum() + "/" + getWheelScore() ).hashCode();
	}
	
	public static PredictionRecordStaticInfoKey getInstance(int dayInterval, int combineTimeSlice, int taskNum, int wheelScore){
		PredictionRecordStaticInfoKey predictionRecordStaticInfoKey = new PredictionRecordStaticInfoKey();
		predictionRecordStaticInfoKey.setCombineTimeSlice(combineTimeSlice);
		predictionRecordStaticInfoKey.setDayInterval(dayInterval);
		predictionRecordStaticInfoKey.setTashNum(taskNum);
		predictionRecordStaticInfoKey.setWheelScore(wheelScore);
		return predictionRecordStaticInfoKey;
	}
}
