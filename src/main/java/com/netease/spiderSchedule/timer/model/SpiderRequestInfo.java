package com.netease.spiderSchedule.timer.model;

import com.netease.spiderSchedule.util.MD5Util;

public class SpiderRequestInfo {

	private String time;
	private String sourceid;
	private String title;
	private String contentUrl;


	/**
	 * @param time
	 * @param sourceid
	 * @param title
	 * @param contentUrl
	 */
	public SpiderRequestInfo(String time, String sourceid, String title, String contentUrl) {
		this.time = time;
		this.sourceid = sourceid;
		this.title = title;
		this.contentUrl = contentUrl;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSourceid() {
		return sourceid;
	}

	public void setSourceid(String sourceid) {
		this.sourceid = sourceid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
	public String getModifyKey() {
		return MD5Util.calcMD5(time + "_" + sourceid + "_" + title);
	}

	@Override
	public String toString() {
		return "SpiderRequestInfo [time=" + time + ", sourceid=" + sourceid + ", title=" + title + ", contentUrl="
				+ contentUrl + "]";
	}
	
}
