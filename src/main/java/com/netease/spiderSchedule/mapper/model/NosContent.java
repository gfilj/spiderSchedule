package com.netease.spiderSchedule.mapper.model;

import java.io.Serializable;
import java.util.HashMap;

import com.netease.spiderSchedule.timer.model.ContentInfo;

/**
 * nos上传对象
 * 
 * @author handongming
 *
 */
public class NosContent implements Serializable{

	private static final long serialVersionUID = 1L;
	private ContentInfo info;
	private HashMap<String, String> tagURL=new HashMap<String, String>();
	private int appId;
	private String sourceId;
	private String time;
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public ContentInfo getInfo() {
		return info;
	}
	public void setInfo(ContentInfo info) {
		this.info = info;
	}
	public HashMap<String, String> getTagURL() {
		return tagURL;
	}
	public void setTagURL(HashMap<String, String> tagURL) {
		this.tagURL = tagURL;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public void putTagURL(String URL,String tag){
		getTagURL().put(URL, tag);
	}
}
