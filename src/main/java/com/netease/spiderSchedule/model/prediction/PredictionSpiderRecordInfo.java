package com.netease.spiderSchedule.model.prediction;

import java.util.Date;
import java.util.Set;

import com.netease.spiderSchedule.model.SpiderRecordInfo;

public class PredictionSpiderRecordInfo extends SpiderRecordInfo{
	
	private static final long serialVersionUID = -4035868389625646483L;
	
	private Date recordUnpateDate;
	
	private Set<Date> list;
	
	/**
	 * @param recordUnpateDate
	 */
	public PredictionSpiderRecordInfo (SpiderRecordInfo spiderRecordInfo ,Date recordUnpateDate, Set<Date> list) {
		setId(spiderRecordInfo.getId());
		setAppid(spiderRecordInfo.getAppid());
		setSourceId(spiderRecordInfo.getSourceId());
		setCreate_time(spiderRecordInfo.getCreate_time());
		setUpdate_time(spiderRecordInfo.getUpdate_time());
		this.recordUnpateDate = recordUnpateDate;
		this.list = list;
	}

	@Override
	public String toString() {
		return "PredictionSpiderRecordInfo [recordUnpateDate=" + recordUnpateDate + ", list=" + list + ", getId()="
				+ getId() + ", getAppid()=" + getAppid() + ", getSourceid()=" + getSourceId() + ", getCreate_time()="
				+ getCreate_time() + ", getUpdate_time()=" + getUpdate_time() + "]";
	}

	public Date getRecordUnpateDate() {
		return recordUnpateDate;
	}

	public void setRecordUnpateDate(Date recordUnpateDate) {
		this.recordUnpateDate = recordUnpateDate;
	}
	
	
	
	
}
