package com.netease.spiderSchedule.boot;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.netease.spiderSchedule.model.prediction.PredictionSpiderRecordStaticInfo;
import com.netease.spiderSchedule.service.spiderRecordInfo.impl.SpiderRecordInfoServiceImpl;

public class StaticBootStrap {

	private static ClassPathXmlApplicationContext context;

	public static void main(String[] args) {
		start();
	}
	
	public static void start() {
		context = new ClassPathXmlApplicationContext("classpath*:config/spring-application.xml");
		context.start();
		SpiderRecordInfoServiceImpl spiderRecordInfoServie = context.getBean(SpiderRecordInfoServiceImpl.class);
		PredictionSpiderRecordStaticInfo spiderRecordStaticInfo = new PredictionSpiderRecordStaticInfo();
		spiderRecordInfoServie.selectIntervalDataBase("spider_record_info_test",0, 1).forEach((v)->{
			long timeDelay = v.getUpdate_time().getTime() - v.getCreate_time().getTime();
			spiderRecordStaticInfo.statistics(timeDelay, v);
		});
		System.out.println(spiderRecordStaticInfo);
		PredictionSpiderRecordStaticInfo spiderRecordStaticInfo1 = new PredictionSpiderRecordStaticInfo();
		spiderRecordInfoServie.selectIntervalDataBase("spider_record_info_test_1", 0, 1).forEach((v)->{
			long timeDelay = v.getUpdate_time().getTime() - v.getCreate_time().getTime();
			spiderRecordStaticInfo1.statistics(timeDelay, v);
		});
		System.out.println(spiderRecordStaticInfo1);
//		PredictionSpiderRecordStaticInfo spiderRecordStaticInfo = new PredictionSpiderRecordStaticInfo();
//		spiderRecordInfoServie.selectTest1().forEach((v)->{
//			long timeDelay = v.getUpdate_time().getTime() - v.getCreate_time().getTime();
//			spiderRecordStaticInfo.statistics(timeDelay, v);
//		});
//		System.out.println(spiderRecordStaticInfo);
//		PredictionSpiderRecordStaticInfo spiderRecordStaticInfo1 = new PredictionSpiderRecordStaticInfo();
//		spiderRecordInfoServie.selectTest2().forEach((v)->{
//			long timeDelay = v.getUpdate_time().getTime() - v.getCreate_time().getTime();
//			spiderRecordStaticInfo1.statistics(timeDelay, v);
//		});
//		System.out.println(spiderRecordStaticInfo1);
		System.exit(0);
	}

}
