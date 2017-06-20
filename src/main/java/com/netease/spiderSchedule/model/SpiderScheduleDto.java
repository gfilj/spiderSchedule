package com.netease.spiderSchedule.model;

import java.util.Calendar;
import java.util.Date;

import com.netease.spiderSchedule.util.DelayLevel;
import com.netease.spiderSchedule.util.RateLevel;

public class SpiderScheduleDto implements Comparable<SpiderScheduleDto> {

	private double score;
	private String sourceId;
	private int priority;
	private long lastUpdateTime;
	private double rate;
	private int delayVal;
	private int appId;
	private boolean wheel = false;// 甄别轮刷策略
	private boolean highQuality = false;// 甄别高质量公众号
	
	public static boolean wheelSwitch = false;
	public static int wheelScore = 10000;
	

	public boolean isHighQuality() {
		return highQuality;
	}

	public void setHighQuality(boolean highQuality) {
		this.highQuality = highQuality;
	}

	public boolean isWheel() {
		return wheel;
	}

	public void setWheel(boolean wheel) {
		this.wheel = wheel;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public int getDelayVal() {
		return delayVal;
	}

	public void setDelayVal(int delayVal) {
		this.delayVal = delayVal;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
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

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	/**
	 * 
	 */
	public SpiderScheduleDto() {
	}

	/**
	 * @param score
	 * @param sourceId
	 * @param priority
	 * @param lastUpdateTime
	 */
	public SpiderScheduleDto(SpiderRateInfo spiderRateInfo) {
		this.sourceId = spiderRateInfo.getSourceId();
		this.priority = spiderRateInfo.getPriority();
		this.appId = spiderRateInfo.getAppId();
		this.lastUpdateTime = spiderRateInfo.getUpdate_time().getTime();
		int size = spiderRateInfo.getTimeSliceCount().size();
		if (size == 1 || size == 2 || size == 3 || size == 4 || size == 5 || size == 6) {
			this.highQuality = true;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int timeSliceKey = (calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)) / 5;
		Integer timeSliceCountValue = spiderRateInfo.getTimeSliceCount().get(timeSliceKey);
		if (timeSliceCountValue != null) {
			rate = Double.valueOf(timeSliceCountValue) / spiderRateInfo.getTotalCount();
		} else {
			rate = 0d;
		}
		DelayLevel delayLevel = DelayLevel.getDelayLevel(lastUpdateTime);
		this.delayVal = delayLevel.getDelayVal();
		countScore(spiderRateInfo);
	}

	public void countScore(SpiderRateInfo spiderRateInfo) {
		if (spiderRateInfo.isTooOld()) {
			if (delayVal >= DelayLevel.HALFDAY.getDelayVal()) {
				this.score = delayVal * RateLevel.TEN.getRateVal();
			} else {
				this.score = -1;
			}
		} else {
			if (delayVal >= DelayLevel.ONE.getDelayVal() && rate <= 0d) {
				setWheel(true);
				this.score = delayVal * RateLevel.TEN.getRateVal();
			} else {
				this.score = delayVal * rate;
			}
		}
	}

	@Override
	public int compareTo(SpiderScheduleDto task) {
		if (this.score > task.score) {
			return 1;
		} else if (this.score < task.score) {
			return -1;
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return "SpiderScheduleDto [score=" + score + ", sourceId=" + sourceId + ", priority=" + priority
				+ ", lastUpdateTime=" + lastUpdateTime + ", rate=" + rate + ", delayVal=" + delayVal + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SpiderScheduleDto)) {
			return false;
		}
		SpiderScheduleDto spiderScheduleDtoTemp = (SpiderScheduleDto) obj;
		if (spiderScheduleDtoTemp.getSourceId() == null) {
			return false;
		}
		return spiderScheduleDtoTemp.getSourceId().equals(getSourceId());
	}

	@Override
	public int hashCode() {
		return getSourceId().hashCode();
	}

}
