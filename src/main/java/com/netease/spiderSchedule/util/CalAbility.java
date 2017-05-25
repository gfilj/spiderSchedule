package com.netease.spiderSchedule.util;

public class CalAbility {

	private int ipNum=10;
	private int ipAbility=50;
	private int sourceidConsume=18;
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

	public void setSourceidConsume(int sourceidConsume) {
		this.sourceidConsume = sourceidConsume;
	}
	
	public int getAbility(){
		return ipNum*ipAbility/sourceidConsume;
	}
	
	public static void main(String[] args) {
		System.out.println(getInstance().getAbility());
	}
}
