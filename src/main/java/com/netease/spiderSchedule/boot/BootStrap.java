package com.netease.spiderSchedule.boot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.netease.spiderSchedule.model.SpiderRateInfo;
import com.netease.spiderSchedule.model.SpiderRecordInfo;
import com.netease.spiderSchedule.model.SpiderScheduleDto;
import com.netease.spiderSchedule.service.spiderRecordInfo.impl.SpiderRecordInfoServiceImpl;
import com.netease.spiderSchedule.service.spiderSourceInfo.impl.SpiderSourceInfoServiceImpl;
import com.netease.spiderSchedule.sort.MaxHeap;

public class BootStrap {

	private static ClassPathXmlApplicationContext context;
	private static SpiderSourceInfoServiceImpl spiderSourceInfoServie;
	private static SpiderRecordInfoServiceImpl spiderRecordInfoServie;
	private static HashMap<String, SpiderRateInfo> rateMap = new HashMap<String, SpiderRateInfo>();

	public static void start(){
		context = new ClassPathXmlApplicationContext("classpath*:config/spring-application.xml");
		context.start();
//		spiderSourceInfoServie = context.getBean(SpiderSourceInfoServieImpl.class);
//		System.out.println(spiderSourceInfoServie.selectBySourceid("luquanluntan"));
//		List<SpiderSourceInfo> spiderSourceInfoList = spiderSourceInfoServie.selectAll();
//		for(SpiderSourceInfo spiderSourceInfo: spiderSourceInfoList){
//			System.out.println(spiderSourceInfo);
//		}
//		ZeroSchedule();
		try {
			System.in.read();
		} catch (Exception e) {
		}
	}

	public static void ZeroSchedule() {
		Calendar calendar = Calendar.getInstance();
		spiderRecordInfoServie = context.getBean(SpiderRecordInfoServiceImpl.class);
		List<SpiderRecordInfo> spiderRecordInfoList = spiderRecordInfoServie.selectAll();
		for(SpiderRecordInfo spiderRecordInfo: spiderRecordInfoList){
			String rateMapKey = spiderRecordInfo.getSourceId();
			if(!rateMap.containsKey(rateMapKey)){
				rateMap.put(rateMapKey, new SpiderRateInfo(spiderRecordInfo));
			}
			SpiderRateInfo spiderRateInfo = rateMap.get(rateMapKey);
			spiderRateInfo.increateTotalCount();
			calendar.setTime(spiderRecordInfo.getCreate_time());
			int key = (calendar.get(Calendar.HOUR)*60 + calendar.get(Calendar.MINUTE))/5;
			Map<Integer, Integer> timeSliceCount = spiderRateInfo.getTimeSliceCount();
			if(timeSliceCount.containsKey(key)){
				timeSliceCount.put(key, timeSliceCount.get(key)+1);
			}else{
				timeSliceCount.put(key, 1);
			}
		}
		int totalCount = 0;
		MaxHeap<SpiderScheduleDto> heapSort = new MaxHeap<SpiderScheduleDto>();
		for(SpiderRateInfo spiderRateInfo:rateMap.values()){
//			System.out.println(spiderRateInfo);
			totalCount += spiderRateInfo.getTotalCount(); 
			heapSort.add(new SpiderScheduleDto(spiderRateInfo));
			
		}
		System.out.println("is stat ture ?" + (totalCount==spiderRecordInfoList.size()));
		
		for(int i = 0 ; i < rateMap.size();i++){
			System.out.println(heapSort.removeTop());
		}
	}
	
	public static void main(String[] args) {
		start();
	}
	
	
}
