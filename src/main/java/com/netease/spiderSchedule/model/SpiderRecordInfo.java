package com.netease.spiderSchedule.model;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @author bjluzhangqing
 *
 */
public class SpiderRecordInfo implements Serializable{

	
	
	private static final long serialVersionUID = 8204160602090647578L;
	
	private int id;			//自增id
	private int appid;    //抓取类型
	private String sourceId;	//源id
	private Date create_time;//创建时间
	private Date update_time;//最后更新时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAppid() {
		return appid;
	}
	public void setAppid(int appid) {
		this.appid = appid;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceid) {
		this.sourceId = sourceid;
	}
	public Date getCreate_time() {
		if(create_time == null){
			setCreate_time(new Date());
		}
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	@Override
	public String toString() {
		return "SpiderRecordInfo [id=" + id + ", appid=" + appid + ", sourceid=" + sourceId + ", create_time="
				+ create_time + ", update_time=" + update_time + "]";
	}
	
}
