package com.netease.spiderSchedule.model;

import java.io.Serializable;

public class SpiderRateInfoDto implements Serializable{


	private static final long serialVersionUID = 8221531461586040607L;
	
	private int appId;
	private String sourceId;
	private int priority;
	
	
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appid) {
		this.appId = appid;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
}
