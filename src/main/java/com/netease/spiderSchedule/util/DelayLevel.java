package com.netease.spiderSchedule.util;

public enum DelayLevel {

	HALFDAY(200), FOUR(100), THREE(70), TWO(50), ONE(10), ZERO(1);

	private int levelVal;

	private DelayLevel(int levelVal) {
		this.levelVal = levelVal;
	}
	
	public int getDelayVal(){
		return levelVal;
	}

	public static DelayLevel getDelayLevel(long lastUpdateTime) {
		long interVal = System.currentTimeMillis() - lastUpdateTime;
		final int HOURVAL = 1000 * 60 * 60 ;
		if (0 <= interVal && interVal < HOURVAL) {
			return DelayLevel.ZERO;
		} else if (HOURVAL <= interVal && interVal < 2 * HOURVAL) {
			return DelayLevel.ONE;
		} else if (2 * HOURVAL <= interVal && interVal < 4 * HOURVAL) {
			return DelayLevel.TWO;
		} else if (4 * HOURVAL <= interVal) {
			return DelayLevel.FOUR;
		}
		return DelayLevel.ZERO;
	}
	public static DelayLevel getDelayLevel(long lastUpdateTime, TimeSimulator timeSimulator) {
		long interVal = timeSimulator.getTime() - lastUpdateTime;
		final int HOURVAL = 1000 * 60 * 60 ;
		if (0 <= interVal && interVal < HOURVAL) {
			return DelayLevel.ZERO;
		} else if (HOURVAL <= interVal && interVal < 2 * HOURVAL) {
			return DelayLevel.ONE;
		} else if (2 * HOURVAL <= interVal && interVal < 3 * HOURVAL) {
			return DelayLevel.TWO;
		} else if (3 * HOURVAL <= interVal && interVal < 4 * HOURVAL) {
			return DelayLevel.THREE;
		} else if (4 * HOURVAL <= interVal && interVal < 24 * HOURVAL) {
			return DelayLevel.FOUR;
		} else if (24 * HOURVAL <=interVal ){
			return DelayLevel.HALFDAY;
		}
		return DelayLevel.ZERO;
	}
}
