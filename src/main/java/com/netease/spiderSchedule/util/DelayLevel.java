package com.netease.spiderSchedule.util;

import com.netease.spiderSchedule.model.SpiderScheduleDto;

public enum DelayLevel {

	HALFDAY(12 * SpiderScheduleDto.HOURVAL), 
	SIX(6 * SpiderScheduleDto.HOURVAL),
	FIVE(5 * SpiderScheduleDto.HOURVAL), 
	FOUR(4 * SpiderScheduleDto.HOURVAL), 
	THREE(3 * SpiderScheduleDto.HOURVAL), 
	TWO(2 * SpiderScheduleDto.HOURVAL), 
	ONE(SpiderScheduleDto.HOURVAL), 
	ZERO(1), 
	WHEELLEVEL(1),
	CURRLEVEL(1);
	
	private long levelVal;

	private DelayLevel(long levelVal) {
		this.levelVal = levelVal;
	}

	public long getDelayVal() {
		return levelVal;
	}
	
	public void setDelayVal(long interVal){
		this.levelVal = interVal;
	}

	public static DelayLevel getDelayLevel(long lastUpdateTime) {
		long interVal = System.currentTimeMillis() - lastUpdateTime;
		DelayLevel.CURRLEVEL.setDelayVal(interVal);
		return CURRLEVEL;
	}

	public static DelayLevel getDelayLevel(long lastUpdateTime, TimeSimulator timeSimulator) {
		long interVal = timeSimulator.getTime() - lastUpdateTime;
		DelayLevel.CURRLEVEL.setDelayVal(interVal);
		return CURRLEVEL;
	}
}
