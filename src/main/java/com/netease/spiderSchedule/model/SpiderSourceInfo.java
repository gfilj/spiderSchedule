package com.netease.spiderSchedule.model;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @author bjluzhangqing
 *
 */
public class SpiderSourceInfo implements Serializable{

	private static final long serialVersionUID = -5676830573760184841L;
	
	private Long id;
	private String sourceId;
	private Integer appid;
	private String name;
	private Date create_time;
	private Integer status;
	private Integer priority;
	private Date update_time;
	private String openid;
	private String customid;


	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSourceid() {
		return sourceId;
	}
	public void setSourceid(String sourceId) {
		this.sourceId = sourceId;
	}
	public Integer getAppid() {
		return appid;
	}
	public void setAppid(Integer appid) {
		this.appid = appid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getCustomid() {
		return customid;
	}
	public void setCustomid(String customid) {
		this.customid = customid;
	}
	@Override
	public String toString() {
		return "SpiderSourceInfo [id=" + id + ", sourceid=" + sourceId + ", appid=" + appid + ", name=" + name
				+ ", create_time=" + create_time + ", status=" + status + ", priority=" + priority + ", update_time="
				+ update_time + ", openid=" + openid + ", customid=" + customid + "]";
	}
	
	

}
