package com.netease.spiderSchedule.boot;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.support.ClassPathXmlApplicationContext;

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

	private static ClassPathXmlApplicationContext context = null;

	public static void main(String[] args) {
		start();
	}

	public static void start() {
		context = new ClassPathXmlApplicationContext("classpath*:config/spring-application.xml");
		context.start();
		predirctSpiderRecordInfo(context, 1);
	}

	public static List<PredictionSpiderRecordStaticInfo> predirctSpiderRecordInfo(
			ClassPathXmlApplicationContext context, int start) {
		Map<SpiderScheduleDto, HashSet<Date>> predictMap = new HashMap<SpiderScheduleDto, HashSet<Date>>();
		// 查询过去1到8天的
		PredictionSpiderRateInfoService predictionSpiderRateInfoServiceImpl = (PredictionSpiderRateInfoService) context
				.getBean("predictionSpiderRateInfoService");
		SmoothingAlgorithmSpiderSortServiceImpl smoothingAlgorithmSpiderSortServiceImpl = (SmoothingAlgorithmSpiderSortServiceImpl) context
				.getBean("smoothingAlgorithmSpiderSortService");
		SpiderRecordInfoServiceImpl spiderRecordInfoServie = context.getBean(SpiderRecordInfoServiceImpl.class);

		TimeSimulator timeSimulator = new TimeSimulator(0 - start, 0, 0);
		timeSimulator.setDayBegin();
		predictionSpiderRateInfoServiceImpl.setTimeSimulator(timeSimulator);
		smoothingAlgorithmSpiderSortServiceImpl.setTimeSimulator(timeSimulator);
		predictionSpiderRateInfoServiceImpl.generateRateMap(start + 1, start + 9);

		// 计算明天要统计的公众号
		// int count = 0;
		List<SpiderRecordInfo> todaySpiderRecordList = spiderRecordInfoServie.selectInterval(start, start + 1);
		System.out.println(todaySpiderRecordList.size());
		// sort
		int i = 0,addCount=0;
		while (!timeSimulator.isNextDay()) {
			if (!timeSimulator.inStopGrapSegment()) {
				addCount += smoothingAlgorithmSpiderSortServiceImpl.addTask(predictionSpiderRateInfoServiceImpl);
				for (SpiderScheduleDto spiderScheduleDto : smoothingAlgorithmSpiderSortServiceImpl.getTask(15,
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
			}
			timeSimulator.getFiveMinuteAfter();
		}
		System.out.println("total count :" + i + "; add count:" + addCount);
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
		returnStaticInfo.add(predictionSpiderRecordStaticInfo);
		PredictionSpiderRecordStaticInfo spiderRecordStaticInfo = new PredictionSpiderRecordStaticInfo();
		for (Entry<SpiderRecordInfo, PredictionSpiderRecordInfo> entry : todayPredictionSpiderRecordMap.entrySet()) {
			long timeDelay = entry.getValue().getUpdate_time().getTime() - entry.getValue().getCreate_time().getTime();
			spiderRecordStaticInfo.statistics(entry, timeDelay, predictionSpiderRateInfoServiceImpl);
		}
		System.out.println(spiderRecordStaticInfo);
		returnStaticInfo.add(spiderRecordStaticInfo);
		return returnStaticInfo;
	}

}
