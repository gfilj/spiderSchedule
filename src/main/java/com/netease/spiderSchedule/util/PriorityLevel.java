package com.netease.spiderSchedule.util;

public enum PriorityLevel {
	
	TEN(10);
	
	private int priorityVal;
	
	
	public int getPriorityVal() {
		return priorityVal;
	}


	/**
	 * @param priorityVal
	 */
	private PriorityLevel(int priorityVal) {
		this.priorityVal = priorityVal;
	}
	
}
