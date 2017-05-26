package com.netease.spiderSchedule.util;

import java.util.concurrent.atomic.AtomicInteger;

public class CalAbility {

	private int ipNum=10;
	private int ipAbility=50;
	private int sourceidConsume=18;
	private AtomicInteger spiderScheduleAbility = new AtomicInteger(ipNum*ipAbility/sourceidConsume);
	private boolean reset = false;
	private static CalAbility instance = new CalAbility();
	
	public static CalAbility getInstance(){
		return instance;
	}

	public int getIpNum() {
		return ipNum;
	}

	public void setIpNum(int ipNum) {
		this.ipNum = ipNum;
	}

	public int getIpAbility() {
		return ipAbility; 
	}

	public void setIpAbility(int ipAbility) {
		this.ipAbility = ipAbility;
	}

	public int getSourceidConsume() {
		return sourceidConsume;
	}

	public AtomicInteger getSpiderScheduleAbility() {
		if(reset){
			 spiderScheduleAbility = new AtomicInteger(ipNum*ipAbility/sourceidConsume);
		}
		return spiderScheduleAbility;
	}

	public void setSpiderScheduleAbility(AtomicInteger spiderScheduleAbility) {
		this.spiderScheduleAbility = spiderScheduleAbility;
	}

	public boolean isReset() {
		return reset;
	}

	public void setReset(boolean reset) {
		this.reset = reset;
	}

	public void setSourceidConsume(int sourceidConsume) {
		this.sourceidConsume = sourceidConsume;
	}
	
	
	public static void main(String[] args) {
	}
}
