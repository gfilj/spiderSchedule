package com.netease.spiderSchedule.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author bjluzhangqing
 *
 */
public class SpiderRateInfo implements Serializable{

	
	
	private static final long serialVersionUID = 8204160602090647578L;
	
	private int appId;    //抓取类型
	private String sourceId;	//源id
	private Date update_time;//最后更新时间
	private String rateJson;
	private Map<Integer,Integer> timeSliceCount;//真实预测时间表
	private int totalCount;//总数
	private int priority;//优先级
	private Map<Integer, SliceStatistics> sliceStisticsMap;
	private List<Integer> timeSliceList;//预测时间片列表
	private Map<Integer,Double> timeSlicePredict;//预测数据
	private boolean tooOld;//是否太老了
	private boolean moreOnceTime=false;//一天超过一次更新
	

	public boolean isMoreOnceTime() {
		return moreOnceTime;
	}

	public void setMoreOnceTime(boolean moreOnceTime) {
		this.moreOnceTime = moreOnceTime;
	}

	public boolean isTooOld() {
		return tooOld;
	}

	public void setTooOld(boolean tooOld) {
		this.tooOld = tooOld;
	}

	public String getSourceId() {
		return sourceId;
	}
	
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	
	public Map<Integer, SliceStatistics> getSliceStisticsMap() {
		return sliceStisticsMap;
	}
	
	public void setSliceStisticsMap(Map<Integer, SliceStatistics> sliceStisticsMap) {
		this.sliceStisticsMap = sliceStisticsMap;
	}

	public Map<Integer, Double> getTimeSlicePredict() {
		return timeSlicePredict;
	}

	public void setTimeSlicePredict(Map<Integer, Double> timeSlicePredict) {
		this.timeSlicePredict = timeSlicePredict;
	}

	public List<Integer> getTimeSliceList() {
		return timeSliceList;
	}

	public void setTimeSliceList(List<Integer> timeSliceList) {
		this.timeSliceList = timeSliceList;
	}
	public static class SliceStatistics{
		int showTime;//出现次数
		int count;//这段时间内的总和
		/**
		 * @param slice
		 * @param showTime
		 * @param count
		 */
		public SliceStatistics(int showTime, int count) {
			this.showTime = showTime;
			this.count = count;
		}
		public int getShowTime() {
			return showTime;
		}
		public SliceStatistics addShowTime() {
			this.showTime ++;
			return this;
		}
		public int getCount() {
			return count;
		}
		public SliceStatistics addCount(int count) {
			this.count += count;
			return this;
		}
		@Override
		public String toString() {
			return "SliceStatistics [showTime=" + showTime + ", count=" + count + "]";
		}
		
	}
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public String getRateJson() {
		return rateJson;
	}

	public void setRateJson(String rateJson) {
		this.rateJson = rateJson;
	}

	public Map<Integer, Integer> getTimeSliceCount() {
		return timeSliceCount;
	}

	public void setTimeSliceCount(Map<Integer, Integer> timeSliceCount) {
		this.timeSliceCount = timeSliceCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}



	@Override
	public String toString() {
		return "SpiderRateInfo [appId=" + appId + ", sourceId=" + sourceId + ", update_time=" + update_time
				+ ", rateJson=" + rateJson + ", timeSliceCount=" + timeSliceCount + ", totalCount=" + totalCount
				+ ", priority=" + priority + ", sliceStisticsMap=" + sliceStisticsMap + ", timeSliceList="
				+ timeSliceList + ", timeSlicePredict=" + timeSlicePredict + ", tooOld=" + tooOld + "]";
	}

	public SpiderRateInfo() {
	}
	
	public SpiderRateInfo(SpiderRecordInfo spiderRecordInfo) {
		this.appId = spiderRecordInfo.getAppid();
		this.sourceId = spiderRecordInfo.getSourceId();
		this.update_time = spiderRecordInfo.getUpdate_time();
		this.timeSliceCount = new HashMap<Integer,Integer>();
		this.rateJson = "";
		this.tooOld = false;
		//avoid 0 as dividend
		this.totalCount = 1;
		sliceStisticsMap = new HashMap<Integer, SliceStatistics>();
		timeSliceList = new ArrayList<Integer>();
		timeSlicePredict = new HashMap<Integer, Double>();
	}
	
	public SpiderRateInfo(SpiderRateInfoDto spiderRateInfoDto){
		this.appId = spiderRateInfoDto.getAppId();
		this.sourceId = spiderRateInfoDto.getSourceId();
		this.priority = spiderRateInfoDto.getPriority();
		this.update_time = new Date();
		this.timeSliceCount = new HashMap<Integer,Integer>();
		this.rateJson = "";
		this.tooOld = false;
		//avoid 0 as dividend
		this.totalCount = 1;
		sliceStisticsMap = new HashMap<Integer, SliceStatistics>();
		timeSliceList = new ArrayList<Integer>();	
		timeSlicePredict = new HashMap<Integer, Double>();
	}
	public SpiderRateInfo(SpiderSourceInfo spiderSourceInfo) {
		this.appId = spiderSourceInfo.getAppid();
		this.sourceId = spiderSourceInfo.getSourceid();
		if(spiderSourceInfo.getPriority() == null){
			this.priority = 0;	
		}else{
			this.priority = spiderSourceInfo.getPriority();
		}
		this.tooOld = false;
		this.update_time = spiderSourceInfo.getUpdate_time();
		this.timeSliceCount = new HashMap<Integer,Integer>();
		this.rateJson = "";
		//avoid 0 as dividend
		this.totalCount = 1;
		sliceStisticsMap = new HashMap<Integer, SliceStatistics>();
		timeSliceList = new ArrayList<Integer>();	
		timeSlicePredict = new HashMap<Integer, Double>();
	}

	public void increateTotalCount(){
		this.totalCount++;
	}
	
	
}
