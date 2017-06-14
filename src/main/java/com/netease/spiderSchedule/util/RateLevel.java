package com.netease.spiderSchedule.util;

public enum RateLevel {

	ZERO(0), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(1000);

	private int rate;

	private RateLevel(int fate) {
		this.rate = fate;
	}

	public int getRateVal() {
		return rate;
	}

	public static RateLevel getRate(double inputFate) {
		if (0.0d < inputFate && inputFate <= 0.1d) {
			return RateLevel.ONE;
		} else if (0.1d < inputFate && inputFate <= 0.2d) {
			return RateLevel.TWO;
		} else if (0.2d < inputFate && inputFate <= 0.3d) {
			return RateLevel.THREE;
		} else if (0.3d < inputFate && inputFate <= 0.4d) {
			return RateLevel.FOUR;
		} else if (0.4d < inputFate && inputFate <= 0.5d) {
			return RateLevel.FIVE;
		} else if (0.5d < inputFate && inputFate <= 0.6d) {
			return RateLevel.SIX;
		} else if (0.6d < inputFate && inputFate <= 0.7d) {
			return RateLevel.SEVEN;
		} else if (0.7d < inputFate && inputFate <= 0.8d) {
			return RateLevel.EIGHT;
		} else if (0.8d < inputFate && inputFate <= 0.9d) {
			return RateLevel.NINE;
		} else if (0.9d < inputFate && inputFate <= 1.0d) {
			return RateLevel.TEN;
		}
		return RateLevel.ZERO;
	}

}
