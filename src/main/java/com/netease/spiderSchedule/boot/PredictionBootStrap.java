package com.netease.spiderSchedule.boot;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.netease.spiderSchedule.model.PredictionRecordStaticInfoKey;
import com.netease.spiderSchedule.model.PredictionRecordStaticInfoValue;
import com.netease.spiderSchedule.model.SpiderRecordInfo;
import com.netease.spiderSchedule.model.SpiderScheduleDto;
import com.netease.spiderSchedule.model.prediction.PredictionSpiderRecordInfo;
import com.netease.spiderSchedule.model.prediction.PredictionSpiderRecordStaticInfo;
import com.netease.spiderSchedule.service.spiderRateInfo.PredictionSpiderRateInfoService;
import com.netease.spiderSchedule.service.spiderRateInfo.impl.PredictionSpiderRateInfoServiceImpl;
import com.netease.spiderSchedule.service.spiderRecordInfo.impl.SpiderRecordInfoServiceImpl;
import com.netease.spiderSchedule.service.spiderSort.impl.SmoothingAlgorithmSpiderSortServiceImpl;
import com.netease.spiderSchedule.sort.MaxHeap;
import com.netease.spiderSchedule.util.TimeSimulator;

public class PredictionBootStrap {

	private static ClassPathXmlApplicationContext context = null;

	public static void main(String[] args) {
		start();
	}

	public static void start() {
		context = new ClassPathXmlApplicationContext("classpath*:config/spring-application.xml");
		context.start();
		predirctSpiderRecordInfo(context, 1, 5, 30);
	}

	public static PredictionRecordStaticInfoValue predirctSpiderRecordInfo(
			ClassPathXmlApplicationContext context, int start, int combineInterval, int taskNum) {
		PredictionRecordStaticInfoValue predictionRecordStaticInfoValue = new PredictionRecordStaticInfoValue();
		Map<SpiderScheduleDto, HashSet<Date>> predictMap = new HashMap<SpiderScheduleDto, HashSet<Date>>();
		// 查询过去1到8天的
		PredictionSpiderRateInfoServiceImpl predictionSpiderRateInfoServiceImpl = (PredictionSpiderRateInfoServiceImpl) context
				.getBean("predictionSpiderRateInfoService");
		SmoothingAlgorithmSpiderSortServiceImpl smoothingAlgorithmSpiderSortServiceImpl = (SmoothingAlgorithmSpiderSortServiceImpl) context
				.getBean("smoothingAlgorithmSpiderSortService");
		SpiderRecordInfoServiceImpl spiderRecordInfoServie = context.getBean(SpiderRecordInfoServiceImpl.class);

		TimeSimulator timeSimulator = new TimeSimulator(0 - start, 0, 0);
		timeSimulator.setDayBegin();
		predictionSpiderRateInfoServiceImpl.setTimeSimulator(timeSimulator);
		smoothingAlgorithmSpiderSortServiceImpl.setTimeSimulator(timeSimulator);
		predictionSpiderRateInfoServiceImpl.setCombineInterval(combineInterval);
		predictionSpiderRateInfoServiceImpl.generateRateMap(start + 1, start + 10);

		List<SpiderRecordInfo> todaySpiderRecordList = spiderRecordInfoServie.selectInterval(start, start + 1);
		System.out.println(todaySpiderRecordList.size());
		// sort
		Map<String, Integer> timeSliceAddNumMap = new HashMap<String, Integer>();
		int taskNumTemp = taskNum;
		while (!timeSimulator.isNextDay()) {
			if(timeSimulator.inStopAddSegment()){
				timeSliceAddNumMap.put( "key"+timeSimulator.getTimeSliceKey(),0);
			}else{
				timeSliceAddNumMap.put( "key"+timeSimulator.getTimeSliceKey(),smoothingAlgorithmSpiderSortServiceImpl.addTask(predictionSpiderRateInfoServiceImpl));
			}
			if (!timeSimulator.inStopGrapSegment()) {
				for (SpiderScheduleDto spiderScheduleDto : smoothingAlgorithmSpiderSortServiceImpl.getTask(taskNum,
						predictionSpiderRateInfoServiceImpl)) {
					if (predictMap.containsKey(spiderScheduleDto)) {
						predictMap.get(spiderScheduleDto).add(timeSimulator.getDate());
					} else {
						HashSet<Date> dateSet = new HashSet<Date>();
						dateSet.add(timeSimulator.getDate());
						predictMap.put(spiderScheduleDto, dateSet);
					}
				}
			}
			timeSimulator.getFiveMinuteAfter();
		}
		predictionRecordStaticInfoValue.setTimeSliceAddNumMap(timeSliceAddNumMap);
		// prediction
		Map<SpiderRecordInfo, PredictionSpiderRecordInfo> todayPredictionSpiderRecordMap = new HashMap<SpiderRecordInfo, PredictionSpiderRecordInfo>();

		for (SpiderRecordInfo spiderRecordInfo : todaySpiderRecordList) {
			for (Entry<SpiderScheduleDto, HashSet<Date>> entry : predictMap.entrySet()) {
				if (spiderRecordInfo.getSourceId().equals(entry.getKey().getSourceId())) {
					// sort get period time
					MaxHeap<Date> dateMap = new MaxHeap<Date>();
					dateMap.add(entry.getValue());
					dateMap.add(spiderRecordInfo.getCreate_time());
					Date selectDate = new Date();
					Date staticDate = selectDate;
					while ((selectDate = dateMap.removeTop()) != spiderRecordInfo.getCreate_time()) {
						staticDate = selectDate;
					}
					todayPredictionSpiderRecordMap.put(spiderRecordInfo,
							new PredictionSpiderRecordInfo(spiderRecordInfo, staticDate, entry.getValue()));
				}
			}
		}
		System.out.println(todayPredictionSpiderRecordMap.size() + "-------" + todaySpiderRecordList.size());
		List<PredictionSpiderRecordStaticInfo> returnStaticInfo = new LinkedList<PredictionSpiderRecordStaticInfo>();
		PredictionSpiderRecordStaticInfo predictionSpiderRecordStaticInfo = new PredictionSpiderRecordStaticInfo();
		// statistics
		for (Entry<SpiderRecordInfo, PredictionSpiderRecordInfo> entry : todayPredictionSpiderRecordMap.entrySet()) {
			long timeDelay = entry.getValue().getRecordUnpateDate().getTime()
					- entry.getValue().getCreate_time().getTime();
			predictionSpiderRecordStaticInfo.statistics(entry, timeDelay, predictionSpiderRateInfoServiceImpl);
		}
		System.out.println(predictionSpiderRecordStaticInfo);
		predictionRecordStaticInfoValue.setPredictRecord(predictionSpiderRecordStaticInfo);
		PredictionSpiderRecordStaticInfo spiderRecordStaticInfo = new PredictionSpiderRecordStaticInfo();
		for (Entry<SpiderRecordInfo, PredictionSpiderRecordInfo> entry : todayPredictionSpiderRecordMap.entrySet()) {
			long timeDelay = entry.getValue().getUpdate_time().getTime() - entry.getValue().getCreate_time().getTime();
			spiderRecordStaticInfo.statistics(entry, timeDelay, predictionSpiderRateInfoServiceImpl);
		}
		System.out.println(spiderRecordStaticInfo);
		predictionRecordStaticInfoValue.setTrueRecord(spiderRecordStaticInfo);
		return predictionRecordStaticInfoValue;
	}

}
