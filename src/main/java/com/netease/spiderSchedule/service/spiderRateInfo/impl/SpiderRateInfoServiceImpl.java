package com.netease.spiderSchedule.service.spiderRateInfo.impl;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.spiderSchedule.model.SpiderRateInfo;
import com.netease.spiderSchedule.model.SpiderRateInfoDto;
import com.netease.spiderSchedule.model.SpiderRecordInfo;
import com.netease.spiderSchedule.model.SpiderScheduleDto;
import com.netease.spiderSchedule.model.SpiderSourceInfo;
import com.netease.spiderSchedule.service.spiderRateInfo.SpiderRateInfoService;
import com.netease.spiderSchedule.service.spiderRecordInfo.SpiderRecodeInfoService;
import com.netease.spiderSchedule.service.spiderSourceInfo.SpiderSourceInfoService;
import com.netease.spiderSchedule.timer.SpiderScheduleTask;

@Service("spiderRateInfoService")
public class SpiderRateInfoServiceImpl implements SpiderRateInfoService, InitializingBean {

	protected Map<String, SpiderRateInfo> rateMap = Collections.synchronizedMap(new HashMap<String, SpiderRateInfo>());

	@Autowired
	protected SpiderSourceInfoService spiderSourceInfoServie;
	@Autowired
	private SpiderRecodeInfoService spiderRecordInfoServie;

	private static final int TIMESLICE = 5;
	private static final int TIMESLICECOUNT = 24 * 60 / TIMESLICE;

	// public static final int
	@Override
	public void generateRateMap(int start, int end) {
		rateMap.clear();
		List<SpiderRecordInfo> spiderRecordInfoList = spiderRecordInfoServie.selectInterval(start, end);
		Calendar calendar = Calendar.getInstance();
		for (SpiderRecordInfo spiderRecordInfo : spiderRecordInfoList) {
			String rateMapKey = spiderRecordInfo.getSourceId();
			SpiderRateInfo spiderRateInfo;
			if (!rateMap.containsKey(rateMapKey)) {
				spiderRateInfo = new SpiderRateInfo(spiderRecordInfo);
				rateMap.put(rateMapKey, spiderRateInfo);
			} else {
				spiderRateInfo = rateMap.get(rateMapKey);
				spiderRateInfo.increateTotalCount();

			}
			calendar.setTime(spiderRecordInfo.getCreate_time());
			int key = (calendar.get(Calendar.HOUR) * 60 + calendar.get(Calendar.MINUTE)) / TIMESLICE;
			Map<Integer, Integer> timeSliceCount = spiderRateInfo.getTimeSliceCount();
			putInfoSliceCount(key, timeSliceCount);
			/*
			 * if (key + 1 < 288) { putInfoSliceCount(key + 1, timeSliceCount);
			 * spiderRateInfo.increateTotalCount(); } if (key + 12 < 288) {
			 * putInfoSliceCount(key + 2, timeSliceCount);
			 * spiderRateInfo.increateTotalCount(); } if (key + 3 < 288) {
			 * putInfoSliceCount(key + 3, timeSliceCount);
			 * spiderRateInfo.increateTotalCount(); } if (key - 3 > 0) {
			 * putInfoSliceCount(key - 3, timeSliceCount);
			 * spiderRateInfo.increateTotalCount(); }
			 */
		}

		for (SpiderRateInfo spiderRateInfo : rateMap.values()) {
			try {
				// make up priority
				SpiderSourceInfo spiderSourceInfo = spiderSourceInfoServie
						.selectBySourceid(spiderRateInfo.getSourceId());
				if (spiderSourceInfo == null || spiderSourceInfo.getPriority() == null) {
					spiderRateInfo.setPriority(0);
				} else {
					spiderRateInfo.setPriority(spiderSourceInfo.getPriority());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// for (SpiderRateInfo spiderRateInfo : rateMap.values()) {
		// // make up statistics map , timeSliceList
		// for(Entry<Integer, Integer>
		// timeSlice:spiderRateInfo.getTimeSliceCount().entrySet()){
		// spiderRateInfo.getTimeSliceList().add(timeSlice.getKey());
		// for(int i=1; i <= 96 ; i++){
		// if(timeSlice.getKey()<=i*3 && timeSlice.getKey()>(i-1)*3){
		// if(!spiderRateInfo.getSliceStisticsMap().containsKey(i)){
		// spiderRateInfo.getSliceStisticsMap().put(i, new
		// SliceStatistics(1,timeSlice.getValue()));
		// }else{
		// spiderRateInfo.getSliceStisticsMap().get(i).addCount(timeSlice.getValue()).addShowTime();
		// }
		// }
		// }
		// }
		// }
		// //合并时间片
		// for (SpiderRateInfo spiderRateInfo : rateMap.values()) {
		// for(Entry<Integer, SliceStatistics>
		// sliceStisticsEntry:spiderRateInfo.getSliceStisticsMap().entrySet()){
		// SliceStatistics sliceStatistics = sliceStisticsEntry.getValue();
		// if(sliceStatistics.getShowTime()>1){
		// int sliceStaticsKey = sliceStisticsEntry.getKey();
		// for(int timeSlice :spiderRateInfo.getTimeSliceList()){
		// if(timeSlice<=sliceStaticsKey*3 && timeSlice>(sliceStaticsKey-1)*3){
		// spiderRateInfo.getTimeSliceCount().remove(timeSlice);
		// }
		// }
		// spiderRateInfo.getTimeSliceCount().put(sliceStaticsKey*3 ,
		// sliceStatistics.getCount());
		// }
		// }
		// System.out.println(spiderRateInfo);
		// }
		// System.out.println("spiderRateInfoService do call zeroSchedule size "
		// + rateMap.values().size());

		// 置信区间
		final double z = 1.96;
		for (SpiderRateInfo spiderRateInfo : rateMap.values()) {
			for (Entry<Integer, Integer> timeSlice : spiderRateInfo.getTimeSliceCount().entrySet()) {
				int n = spiderRateInfo.getTotalCount();
				double phat = Double.valueOf(timeSlice.getValue()) / n;
				double redditP = (phat + z * z / (2 * n) - z * Math.sqrt((phat * (1 - phat) + z * z / (4 * n)) / n))
						/ (1 + z * z / n);
				timeSlice.setValue((int) (redditP * 1000000));
			}
		}

		// 三次平滑
		final double a = 0.4, b = 0.05, y = 0.9;
		final int k = 2;
		for (SpiderRateInfo spiderRateInfo : rateMap.values()) {
			LinkedList<Double> spiderRateInfoT = new LinkedList<Double>();
			LinkedList<Double> spiderRateInfoS = new LinkedList<Double>();
			LinkedList<Double> spiderRateInfoP = new LinkedList<Double>();

			for (int i = 0; i < TIMESLICECOUNT; i++) {
				Integer timeSliceCountValue = getTimeSliceCountValue(spiderRateInfo, i);

				double s = 0d;
				double t = 0d;
				double p = 1d;
				if (i == 0) {
					s = timeSliceCountValue;
					t = 0;
					p = 1;
				} else {
					s = a * (timeSliceCountValue - getValueP(spiderRateInfoP, i - k))
							+ (1 - a) * (getValueST(spiderRateInfoS, i - 1) + getValueST(spiderRateInfoT, i - 1));
					t = b * (s - getValueST(spiderRateInfoS, i - 1)) + (1 - b) * getValueST(spiderRateInfoT, i - 1);
					p = y * (timeSliceCountValue - s) + (1 - y) * getValueP(spiderRateInfoP, i - k);
				}
				spiderRateInfoT.add(t);
				spiderRateInfoS.add(s);
				spiderRateInfoP.add(p);
				spiderRateInfo.getTimeSlicePredict().put(i, s);
			}
		}
		//
		// rateMap.forEach((r,v)->System.out.println(v.getTimeSliceCount() +
		// "-----" + v.getTimeSlicePredict()));
		// makeup all source
		/*
		 * for(SpiderSourceInfo
		 * spiderSourceInfo:spiderSourceInfoServie.selectAll()){
		 * if(spiderSourceInfo.getSourceid() == null){ continue; }
		 * SpiderRateInfo spiderRateInfo = new SpiderRateInfo(spiderSourceInfo);
		 * Calendar cal = Calendar.getInstance();
		 * cal.setTime(spiderSourceInfo.getUpdate_time()); int dayUpdate =
		 * cal.get(Calendar.DAY_OF_YEAR);
		 * cal.setTimeInMillis(System.currentTimeMillis()); int dayCur=
		 * cal.get(Calendar.DAY_OF_YEAR); if((dayCur - dayUpdate) > end){
		 * spiderRateInfo.setTooOld(true); }
		 * rateMap.put(spiderRateInfo.getSourceId(), spiderRateInfo);
		 * 
		 * }
		 */
		int countTooOld = 0;
		System.out.println("before add sourceId ---------------->" + rateMap.size());
		for (SpiderSourceInfo spiderSourceInfo : spiderSourceInfoServie.selectAll()) {
			String sourceid = spiderSourceInfo.getSourceid();
			if (sourceid == null) {
				continue;
			}
			if (!rateMap.containsKey(sourceid)) {
				SpiderRateInfo spiderRateInfo = new SpiderRateInfo(spiderSourceInfo);
				rateMap.put(sourceid, spiderRateInfo);

				Calendar cal = Calendar.getInstance();
				cal.setTime(spiderSourceInfo.getCreate_time());
				int dayUpdate = cal.get(Calendar.DAY_OF_YEAR);
				cal.setTimeInMillis(System.currentTimeMillis());
				int dayCur = cal.get(Calendar.DAY_OF_YEAR);
				if ((dayCur - dayUpdate) >= end) {
					spiderRateInfo.setTooOld(true);
					countTooOld++;
				}
			}
		}
		System.out.println("end add sourceId ----------->" + rateMap.size() + "--------->countTooOld" + countTooOld);
		
		// update time
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, -1);
		rateMap.forEach((r, v) -> {
			if (v.isTooOld()) {
				v.setUpdate_time(cal.getTime());
			}else{
				v.setUpdate_time(new Date());
			}
		});
	}

	public Double getValueST(LinkedList<Double> spiderRateInfoS, int i) {
		if (i < 0) {
			return 0d;
		}
		Double valueS = spiderRateInfoS.get(i);
		if (valueS == null) {
			valueS = 0d;
		}
		return valueS;
	}

	public Double getValueP(LinkedList<Double> spiderRateInfoP, int i) {
		if (i < 0) {
			return 1d;
		}
		Double valueP = spiderRateInfoP.get(i);
		if (valueP == null) {
			valueP = 1d;
		}
		return valueP;
	}

	public Integer getTimeSliceCountValue(SpiderRateInfo spiderRateInfo, int i) {
		if (i < 0) {
			return 0;
		}
		Integer timeSliceCountValue = spiderRateInfo.getTimeSliceCount().get(i);
		if (timeSliceCountValue == null) {
			timeSliceCountValue = 0;
		}
		return timeSliceCountValue;
	}

	public void putInfoSliceCount(int key, Map<Integer, Integer> timeSliceCount) {
		if (timeSliceCount.containsKey(key)) {
			timeSliceCount.put(key, timeSliceCount.get(key) + 1);
		} else {
			timeSliceCount.put(key, 1);
		}
	}

	@Override
	public void updateRateMap(SpiderScheduleDto spiderScheduleDto) {
		SpiderRateInfo spiderRateInfo = rateMap.get(spiderScheduleDto.getSourceId());
		if (spiderRateInfo != null) {
			spiderRateInfo.setUpdate_time(new Date());
			//
			// spiderRateInfo.setTooOld(true);
		}
	}

	@Override
	public void addRateMap(SpiderRateInfoDto spiderRateInfoDte) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(new Date());
		instance.add(Calendar.HOUR, -1);
		SpiderRateInfo spiderRateInfo = new SpiderRateInfo(spiderRateInfoDte);
		spiderRateInfo.setUpdate_time(instance.getTime());
		this.rateMap.put(spiderRateInfo.getSourceId(), spiderRateInfo);

	}

	// @Override
	// public void addRateMap(SpiderSourceInfo spiderSourceInfo) {
	// Calendar instance = Calendar.getInstance();
	// instance.setTime(new Date());
	// instance.add(Calendar.HOUR, -1);
	// SpiderRateInfo spiderRateInfo = new SpiderRateInfo(spiderRateInfoDte);
	// spiderRateInfo.setUpdate_time(instance.getTime());
	// this.rateMap.put(spiderRateInfo.getSourceid(), spiderRateInfo);
	//
	// }

	@Override
	public Map<String, SpiderRateInfo> getRateMap() {
		return rateMap;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		generateRateMap(0, 20);
	}

}
