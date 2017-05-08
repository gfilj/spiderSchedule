package com.netease.spiderSchedule.boot;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.netease.spiderSchedule.model.SpiderRateInfo;
import com.netease.spiderSchedule.model.SpiderRecordInfo;
import com.netease.spiderSchedule.model.SpiderScheduleDto;
import com.netease.spiderSchedule.model.prediction.PredictionSpiderRecordInfo;
import com.netease.spiderSchedule.model.prediction.PredictionSpiderRecordStaticInfo;
import com.netease.spiderSchedule.service.spiderRateInfo.PredictionSpiderRateInfoService;
import com.netease.spiderSchedule.service.spiderRecordInfo.impl.SpiderRecordInfoServiceImpl;
import com.netease.spiderSchedule.service.spiderSort.impl.SmoothingAlgorithmSpiderSortServiceImpl;
import com.netease.spiderSchedule.sort.MaxHeap;
import com.netease.spiderSchedule.util.TimeSimulator;

public class PredictionBootStrap {

	private static ClassPathXmlApplicationContext context;

	public static void main(String[] args) {
		start();
	}

	public static void start() {
		Map<SpiderScheduleDto, HashSet<Date>> predictMap = new HashMap<SpiderScheduleDto, HashSet<Date>>();
		context = new ClassPathXmlApplicationContext("classpath*:config/spring-application.xml");
		context.start();
		// 查询过去1到8天的
		PredictionSpiderRateInfoService predictionSpiderRateInfoServiceImpl = (PredictionSpiderRateInfoService) context
				.getBean("predictionSpiderRateInfoService");
		SmoothingAlgorithmSpiderSortServiceImpl smoothingAlgorithmSpiderSortServiceImpl = (SmoothingAlgorithmSpiderSortServiceImpl) context
				.getBean("smoothingAlgorithmSpiderSortService");
		SpiderRecordInfoServiceImpl spiderRecordInfoServie = context.getBean(SpiderRecordInfoServiceImpl.class);

		TimeSimulator timeSimulator = new TimeSimulator(-1, 0, 0);
		timeSimulator.setDayBegin();
		predictionSpiderRateInfoServiceImpl.setTimeSimulator(timeSimulator);
		smoothingAlgorithmSpiderSortServiceImpl.setTimeSimulator(timeSimulator);
		predictionSpiderRateInfoServiceImpl.generateRateMap(2, 11);
		// 计算明天要统计的公众号
//		int count = 0;
		List<SpiderRecordInfo> todaySpiderRecordList = spiderRecordInfoServie.selectInterval(1, 2);
//		for (SpiderRecordInfo spiderRecordInfo : todaySpiderRecordList) {
//			SpiderRateInfo spiderRateInfo = predictionSpiderRateInfoServiceImpl.getRateMap().get(spiderRecordInfo.getSourceId());
//			if(spiderRateInfo!=null&spiderRateInfo.isTooOld()){
//				count++;
//				System.out.println(spiderRateInfo +"---" + spiderRecordInfo);
//			};
//			if (!predictionSpiderRateInfoServiceImpl.getRateMap().containsKey(spiderRecordInfo.getSourceId())) {
//				SpiderRateInfo spiderRateInfo = new SpiderRateInfo(spiderRecordInfo);
//				predictionSpiderRateInfoServiceImpl.getRateMap().put(spiderRecordInfo.getSourceId(), spiderRateInfo);
//			if()
//				Calendar cal = Calendar.getInstance();
//				cal.setTime(spiderRecordInfo.getCreate_time());
//				if (cal.get(Calendar.MONTH)== 5-1 && cal.get(Calendar.DAY_OF_MONTH)==2) {
//					
//				}else{
//					
//					System.out.println(spiderRecordInfo + "----" + cal.get(Calendar.MONTH) + "----" + cal.get(Calendar.DAY_OF_MONTH));
//					count++;
//				}
//			}
//		}

//		 System.out.println("太久未更新的---》" + count);
		// sort 
		int i = 0;
		while (!timeSimulator.isNextDay()) {
			smoothingAlgorithmSpiderSortServiceImpl.addTask(predictionSpiderRateInfoServiceImpl);
			for (SpiderScheduleDto spiderScheduleDto : smoothingAlgorithmSpiderSortServiceImpl.getTask(200,
					predictionSpiderRateInfoServiceImpl)) {
				i++;
				if (predictMap.containsKey(spiderScheduleDto)) {
					predictMap.get(spiderScheduleDto).add(timeSimulator.getDate());
				} else {
					HashSet<Date> dateSet = new HashSet<Date>();
					dateSet.add(timeSimulator.getDate());
					predictMap.put(spiderScheduleDto, dateSet);
				}
			}
			timeSimulator.getFiveMinuteAfter();
		}
		System.out.println("total count :" + i);
		// check
		// predictMap.forEach((k,v)->System.out.println(k));
		// System.out.println("this is the count :" + predictMap.size());
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
		PredictionSpiderRecordStaticInfo predictionSpiderRecordStaticInfo = new PredictionSpiderRecordStaticInfo();
		// statistics
		for (Entry<SpiderRecordInfo, PredictionSpiderRecordInfo> entry : todayPredictionSpiderRecordMap.entrySet()) {
			long timeDelay = entry.getValue().getRecordUnpateDate().getTime()
					- entry.getValue().getCreate_time().getTime();
			predictionSpiderRecordStaticInfo.statistics(entry, timeDelay, predictionSpiderRateInfoServiceImpl);
		}
		System.out.println(predictionSpiderRecordStaticInfo);

		PredictionSpiderRecordStaticInfo spiderRecordStaticInfo = new PredictionSpiderRecordStaticInfo();
		for (Entry<SpiderRecordInfo, PredictionSpiderRecordInfo> entry : todayPredictionSpiderRecordMap.entrySet()) {
			long timeDelay = entry.getValue().getUpdate_time().getTime() - entry.getValue().getCreate_time().getTime();
			spiderRecordStaticInfo.statistics(entry, timeDelay, predictionSpiderRateInfoServiceImpl);
		}
		System.out.println(spiderRecordStaticInfo);
		try {
			System.in.read();
		} catch (Exception e) {
		}
	}

}
