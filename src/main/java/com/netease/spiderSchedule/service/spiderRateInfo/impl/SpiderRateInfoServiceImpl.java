package com.netease.spiderSchedule.service.spiderRateInfo.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.netease.spiderSchedule.controller.SpiderScheduleController;
import com.netease.spiderSchedule.model.SpiderRateInfo;
import com.netease.spiderSchedule.model.SpiderRateInfoDto;
import com.netease.spiderSchedule.model.SpiderRecordInfo;
import com.netease.spiderSchedule.model.SpiderScheduleDto;
import com.netease.spiderSchedule.model.SpiderSourceInfo;
import com.netease.spiderSchedule.service.spiderRateInfo.SpiderRateInfoService;
import com.netease.spiderSchedule.service.spiderRecordInfo.SpiderRecodeInfoService;
import com.netease.spiderSchedule.service.spiderSort.SpiderSortService;
import com.netease.spiderSchedule.service.spiderSourceInfo.SpiderSourceInfoService;
import com.netease.spiderSchedule.sort.MaxHeap;
import com.netease.spiderSchedule.util.RateLevel;
import com.netease.spiderSchedule.util.TimeSimulator;

@Service("spiderRateInfoService")
public class SpiderRateInfoServiceImpl implements SpiderRateInfoService, InitializingBean {

	protected Map<String, SpiderRateInfo> rateMap = Collections.synchronizedMap(new HashMap<String, SpiderRateInfo>());
	protected Map<String, SpiderRateInfo> rateMap2 = Collections.synchronizedMap(new HashMap<String, SpiderRateInfo>());

	@Autowired
	protected SpiderSourceInfoService spiderSourceInfoServie;
	@Autowired
	private SpiderRecodeInfoService spiderRecordInfoServie;
	@Autowired
	@Qualifier("smoothingAlgorithmSpiderSortService")
	private SpiderSortService spiderSortService;
	
	protected static Logger logger = Logger.getLogger(SpiderRateInfoServiceImpl.class);
	private static final int TIMESLICE = 5;
	private static final int TIMESLICECOUNT = 24 * 60 / TIMESLICE;

	private int combineInterval = 5;

	public int getCombineInterval() {
		return combineInterval;
	}

	public void setCombineInterval(int combineInterval) {
		this.combineInterval = combineInterval;
	}

	private int countTooOld;

	public int getCountTooOld() {
		return countTooOld;
	}

	public void setCountTooOld(int countTooOld) {
		this.countTooOld = countTooOld;
	}

	@Override
	public void generateRateMap(int start, int end) {
		generateRateMapDetail(start, end, 2, 7);

	}
	
	@Override
	public void cleanTaskQueue(){
		//清除
		List<SpiderRecordInfo> yesterDayList = spiderRecordInfoServie.selectInterval(0, 1);
		yesterDayList.forEach((v)->{
			if(rateMap.containsKey(v.getSourceId())){
				//时间片存在多个时间段当中的直接干掉
				SpiderRateInfo spiderRateInfo = rateMap2.get(v.getSourceId());
				
				if(spiderRateInfo.isMoreOnceTime()){
					if(TimeSimulator.getTimeSliceKey(v.getCreate_time())>=spiderRateInfo.getMaxTimeSlice()){
						removeTaskFromRateMap(v);
					}
				}else{
					removeTaskFromRateMap(v);
				}
			}
			
		});
		//更新概率
		//统计最近几天的概率，当前时间段没抓到的我现在下发，针对利当前时间片比较近的下发，最多不会超过500个
		int nowTimeSliceKey = TimeSimulator.getNow().getTimeSliceKey();
		MaxHeap<SpiderRateInfo> spiderRateInfoSortedList = new MaxHeap<SpiderRateInfo>();
		rateMap2.forEach((k,v)->{
			//计算离当前时间片最近的
			if(!v.isMoreOnceTime()){
				if(v.getMaxTimeSlice() < nowTimeSliceKey){
					v.setNearestInterval(nowTimeSliceKey-v.getMaxTimeSlice());
				}
			}else{
				//如果当前存在多个发文时间，比较比當前時間段小的发文是时间段，然后找打最小的进行设置
				int nearest = 288;
				for(int timeSlice : v.getTimeSliceCount().keySet()){
					if(nowTimeSliceKey > timeSlice){
						int currentSliceInterval = nowTimeSliceKey-timeSlice;
						if(nearest < currentSliceInterval){
							nearest = currentSliceInterval;
						}
					}
				}
				v.setNearestInterval(nearest);
			}
			//排序
			spiderRateInfoSortedList.add(v);
		});
		int delayNum = 0;
		for(int i = 0; i < 500; i++){
			SpiderRateInfo delayRateInfo = spiderRateInfoSortedList.removeTop();
			if(delayRateInfo.getNearestInterval()<5||delayRateInfo.getNearestInterval()>96){
				continue;
			}
			spiderSortService.addTask(delayRateInfo.getSourceId(),this);
			delayNum++;
			logger.info(nowTimeSliceKey +"----" +delayRateInfo .getNearestInterval() +"----" +delayRateInfo .getTimeSliceCount()+ "----" + delayRateInfo.getSourceId());
		}
		//计算完后所有归位
		rateMap2.forEach((k,v)->{
			v.setNearestInterval(288);
		});
		logger.info("SpiderRateInfoServiceImpl  after clean go to crab " + rateMap.size() + " delay num  " + delayNum);
	}
	/**
	 * 
	 * @param rateMap2
	 * @param v
	 */
	public void removeTaskFromRateMap(SpiderRecordInfo v) {
		rateMap.remove(v.getSourceId());
		if(rateMap2.containsKey(v.getSourceId())){
			rateMap2.remove(v.getSourceId());
		}
	}
	public void generateRateMapDetail(int start, int end, int freeStart, int freeEnd) {
		rateMap.clear();
		rateMap2.clear();
		generateOriginalRateMap(start, end, rateMap);
		generateOriginalRateMap(0, 1, rateMap2);
		
		int moreOnceTimeCount14 = 0;
		for(SpiderRateInfo spiderRateInfo : this.rateMap.values()){
			if(spiderRateInfo.isMoreOnceTime()){
				moreOnceTimeCount14++;
			}
		}
		int moreOnceTimeCount2 = 0;
		for(SpiderRateInfo spiderRateInfo : this.rateMap2.values()){
			if(spiderRateInfo.isMoreOnceTime()){
				moreOnceTimeCount2++;
			}
		}
		logger.info("moreOnceTimeCount14:" + moreOnceTimeCount14 + ",moreOnceTimeCount2:" + moreOnceTimeCount2);
		
		// make up priority
		for (SpiderRateInfo spiderRateInfo : rateMap.values()) {
			try {
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

		// 置信区间
		final double z = 1.96;
		for (SpiderRateInfo spiderRateInfo : rateMap.values()) {
			boolean canCount = false;
			for (Entry<Integer, Integer> timeSlice : spiderRateInfo.getTimeSliceCount().entrySet()) {
				int n = spiderRateInfo.getTotalCount();
				double phat = Double.valueOf(timeSlice.getValue()) / n;
				double redditP = (phat + z * z / (2 * n) - z * Math.sqrt((phat * (1 - phat) + z * z / (4 * n)) / n))
						/ (1 + z * z / n);
				timeSlice.setValue((int) (redditP * 1000000));
			}

		}
		setWheelRate();
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

		// 合并时间片
		// 针对2,3,4,5,6,7期间的时间片按照最大的分数合并到6点对应的时间片为
		// 针对半个小时有连续概率大于0的全部合并到其后
		int spiderRateInfoTimes = 0;
		for (SpiderRateInfo v : rateMap.values()) {
			spiderRateInfoTimes++;
			Map<Integer, Double> timeSlicePredict = v.getTimeSlicePredict();
			int offset = spiderRateInfoTimes % combineInterval;
			int offset2To7 = spiderRateInfoTimes % (combineInterval * 3);
			double freeMax = 0.0d;
			for (int i = freeStart * 12 + 1; i < freeEnd * 12 + offset2To7; i++) {
				timeSlicePredict.put(i, -1d);
				if (freeMax < timeSlicePredict.get(i)) {
					freeMax = timeSlicePredict.get(i);
				}
			}
			int putIntoKey = freeEnd * 12 + offset2To7;
			int j = 1;
			for (int i = timeSlicePredict.size() - 1 - offset; i >= 0; i -= j) {
				double combineMax = timeSlicePredict.get(i);
				for (j = 1; j < combineInterval; j++) {
					int combinekey = i - j;
					if (combinekey >= 0) {
						Double combineValue = timeSlicePredict.get(combinekey);
						// System.out.println(v.getSourceId() + " " + i + "/" +
						// combinekey + " " + combineValue);
						timeSlicePredict.put(combinekey, -1d);
						if (combineValue < 0) {
							if (combineMax > 0) {
								timeSlicePredict.put(i, combineMax);
							}
							// i = combinekey;
							break;
						} else {
							if (combineMax < combineValue) {
								combineMax = combineValue;
							}
						}
					} else {
						if (combineMax > 0) {
							timeSlicePredict.put(i, combineMax);
						}
						break;
					}
				}
			}
//			for (int i = timeSlicePredict.size() - 1; i > timeSlicePredict.size() - 1 - offset; i--) {
//				timeSlicePredict.put(i, -1d);
//			}

		}
		// makeup all source
//		List<SpiderSourceInfo> sourceList = spiderSourceInfoServie.selectAll();
//
//		System.out.println("before add sourceId rate map size ---------------->" + rateMap.size()
//				+ " adding list size :" + sourceList.size());
//		for (SpiderSourceInfo spiderSourceInfo : sourceList) {
//			String sourceid = spiderSourceInfo.getSourceid();
//			if (sourceid == null) {
//				continue;
//			}
//			if (!rateMap.containsKey(sourceid)) {
//				SpiderRateInfo spiderRateInfo = new SpiderRateInfo(spiderSourceInfo);
//				rateMap.put(sourceid, spiderRateInfo);
//				// 过滤刚创建的公众号
//				Calendar cal = Calendar.getInstance();
//				Date create_time = spiderSourceInfo.getCreate_time();
//				if (create_time == null) {
//					spiderRateInfo.setTooOld(true);
//					countTooOld++;
//				} else {
//					cal.setTime(create_time);
//					int dayUpdate = cal.get(Calendar.DAY_OF_YEAR);
//					cal.setTimeInMillis(System.currentTimeMillis());
//					int dayCur = cal.get(Calendar.DAY_OF_YEAR);
//					if ((dayCur - dayUpdate) >= end) {
//						spiderRateInfo.setTooOld(true);
//						countTooOld++;
//					}
//				}
//			}
//		}
//		System.out.println("end add sourceId ----------->" + rateMap.size() + "--------->countTooOld" + countTooOld);

		// update time
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, -1);
		rateMap.forEach((r, v) -> {
			if (v.isTooOld()) {
				v.setUpdate_time(cal.getTime());
			} else {
				// 重新启动默认轮刷
				v.setUpdate_time(TimeSimulator.getNow().getHoursBefore(2));
			}
		});
	}

	public void nakeUpOnceTime(Map<String, SpiderRateInfo> rateMapDay, Map<String, SpiderRateInfo> rateMapMutiDay) {
		for(Entry<String, SpiderRateInfo> entry :rateMapDay.entrySet()){
			if(entry.getValue().getTimeSliceCount().size()>1){
				rateMapMutiDay.get(entry.getKey()).setMoreOnceTime(true);;
			}
		}
	} 

	public void generateOriginalRateMap(int start, int end, Map<String, SpiderRateInfo> rateMapMutiDay ) {
		Map<String, SpiderRateInfo> rateMapDay = new HashMap<String, SpiderRateInfo>();
		//按照天来统计发文重复次数
		for(int i=start; i<=end; i++){
			rateMapDay.clear();
			List<SpiderRecordInfo> spiderRecordInfoList = spiderRecordInfoServie.selectInterval(i, i+1);
			for (SpiderRecordInfo spiderRecordInfo : spiderRecordInfoList) {
				//确保是在当天多次发问的
				String rateMapKey = spiderRecordInfo.getSourceId();
				SpiderRateInfo spiderRateInfo;
				if (!rateMapMutiDay.containsKey(rateMapKey)) {
					spiderRateInfo = new SpiderRateInfo(spiderRecordInfo);
					rateMapMutiDay.put(rateMapKey, spiderRateInfo);
				} else {
					spiderRateInfo = rateMapMutiDay.get(rateMapKey);
					spiderRateInfo.increateTotalCount();
					
				}
				int key = TimeSimulator.getTimeSliceKey(spiderRecordInfo.getCreate_time());
				Map<Integer, Integer> timeSliceCount = spiderRateInfo.getTimeSliceCount();
				putInfoSliceCount(key, timeSliceCount);
				//设置最大key
				if(spiderRateInfo.getMaxTimeSlice()<key){
					spiderRateInfo.setMaxTimeSlice(key);
				}
				rateMapDay.put(rateMapKey, spiderRateInfo);
			}
			nakeUpOnceTime(rateMapDay,rateMapMutiDay);
		}
	}
	/**
	 * 统计时间片比例
	 */
	public void staticSliceRate(){
		int count = 0;
		int count2 = 0;
		int count3 = 0;
		int count4 = 0;
		int count5 = 0;
		int count6 = 0;
		int count7 = 0;
		int count8 = 0;
		int count9 = 0;
		for (SpiderRateInfo spiderRateInfo : rateMap.values()) {
			if (spiderRateInfo.getTimeSliceCount().size() == 1) {
				count++;
			}
			if (spiderRateInfo.getTimeSliceCount().size() == 2) {
				count2++;
			}
			if (spiderRateInfo.getTimeSliceCount().size() == 3) {
				count3++;
			}
			if (spiderRateInfo.getTimeSliceCount().size() == 4) {
				count4++;
			}
			if (spiderRateInfo.getTimeSliceCount().size() == 5) {
				count5++;
			}
			if (spiderRateInfo.getTimeSliceCount().size() == 6) {
				count6++;
			}
			if (spiderRateInfo.getTimeSliceCount().size() == 7) {
				count7++;
			}
			if (spiderRateInfo.getTimeSliceCount().size() == 8) {
				count8++;
			}
			if (spiderRateInfo.getTimeSliceCount().size() == 9) {
				count9++;
			}
		}
		System.out.println(count + "--" + count2 + "--" + count3 + "--" + count4 + "--" + count5 + "--" + count6 + "--"
				+ count7 + "--" + count8 + "--" + count9 );
	}
	/**
	 * 设置轮刷分数
	 */
	public void setWheelRate() {
		ArrayList<Integer> arr = new ArrayList<Integer>();
		for (SpiderRateInfo spiderRateInfo : rateMap.values()) {
			for (Entry<Integer, Integer> timeSlice : spiderRateInfo.getTimeSliceCount().entrySet()) {
				arr.add(timeSlice.getValue());
			}
		}
		Collections.sort(arr);
		RateLevel.TEN.setRateVal(arr.get(arr.size()*1/40+1));
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
		generateRateMap(0, 14);
		cleanTaskQueue();
		spiderSortService.addTask(this);
	}

}
