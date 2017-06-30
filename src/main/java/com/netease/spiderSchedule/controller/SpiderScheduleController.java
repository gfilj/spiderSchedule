package com.netease.spiderSchedule.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.netease.spiderSchedule.boot.PredictionBootStrap;
import com.netease.spiderSchedule.model.PredictionRecordStaticInfoKey;
import com.netease.spiderSchedule.model.PredictionRecordStaticInfoValue;
import com.netease.spiderSchedule.model.SpiderScheduleDto;
import com.netease.spiderSchedule.model.prediction.PredictionSpiderRecordStaticInfo;
import com.netease.spiderSchedule.service.spiderRateInfo.SpiderRateInfoService;
import com.netease.spiderSchedule.service.spiderRateInfo.impl.SpiderRateInfoServiceImpl;
import com.netease.spiderSchedule.service.spiderRecordInfo.SpiderRecodeInfoService;
import com.netease.spiderSchedule.service.spiderSort.impl.SpiderSortServiceImpl;
import com.netease.spiderSchedule.timer.model.Request;
import com.netease.spiderSchedule.util.CalAbility;
import com.netease.spiderSchedule.util.RateLevel;
import com.netease.spiderSchedule.util.Runner;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class SpiderScheduleController extends AbstractVerticle {

	private static ClassPathXmlApplicationContext context;

	private static SpiderRateInfoService spiderRateInfoService;
	private static SpiderSortServiceImpl spiderSortService;
	private static SpiderRecodeInfoService spiderRecordInfoService;
	public static CalAbility calAbility = new CalAbility();
	private static Map<PredictionRecordStaticInfoKey, PredictionRecordStaticInfoValue> predirctSpiderRecordInfoMap = new HashMap<PredictionRecordStaticInfoKey, PredictionRecordStaticInfoValue>();
	public static Map<String, Integer> errorHandleMap = Collections.synchronizedMap(new HashMap<String, Integer>());//errorHandleMap
//	private static Map<String,JSONObject> ipJsonMap = Collections.synchronizedMap(new HashMap<String,JSONObject>());
	private static List<Request> weixinListRequest = Collections.synchronizedList(new ArrayList<Request>());//微信列表页list
	
	/**
	 * 获取任务进行执行
	 * @return
	 */
	public static List<Request> getWeixinListRequest() {
		return weixinListRequest;
	}


	protected static Logger logger = Logger.getLogger(SpiderScheduleController.class);

	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext("classpath*:config/spring-application.xml");
		context.start();
		spiderRateInfoService = (SpiderRateInfoServiceImpl) context.getBean("spiderRateInfoService");
		spiderSortService = (SpiderSortServiceImpl) context.getBean("smoothingAlgorithmSpiderSortService");
		spiderRecordInfoService = (SpiderRecodeInfoService) context.getBean("spiderRecordInfoServie");
		Runner.runExample(SpiderScheduleController.class);
		System.out.println("init down!");
	}

	@Override
	public void start() {
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		router.get("/getTask/:taskNum").handler(SpiderScheduleController::handleGetTask);
		router.get("/addTask/:sourceId").handler(this::handleAddTask);
		router.get("/getRateMap/:sourceId").handler(this::handleGetRateMap);
		router.get("/getRateMap/:sourceId").handler(this::handleGetRateMap);
		router.get("/handleTaskError/:sourceId").handler(this::handleTaskError);
		router.get("/daychart").handler(routingContext -> {
			routingContext.response().putHeader("content-type", "text/html")
					.end("<form action=\"/form\" method=\"post\">\n" + "<div>\n"
							+ "<label for=\"name\">Enter your name:</label>\n"
							+ "<input type=\"text\" id=\"name\" name=\"name\" />\n" + "</div>\n"
							+ "<div class=\"button\">\n" + "<button type=\"submit\">Send</button>\n" + "</div>"
							+ "</form>");
		});
		router.route("/*").handler(StaticHandler.create());

		// handle the form
		router.post("/form").handler(ctx -> {
			ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
			// note the form attribute matches the html form element name.
			ctx.response().end("Hello " + ctx.request().getParam("name") + "!");
		});

		router.post("/statistics/:dayBeforeCurrent").handler(ctx -> {
			String paramval = "";
			int dayBeforeCurrent = 1;
			try {
				paramval = ctx.request().getParam("dayBeforeCurrent");
				dayBeforeCurrent = Integer.parseInt(paramval);
			} catch (Exception e) {
				e.printStackTrace();
				sendError(400, ctx.response());

			}

			ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
			// note the form attribute matches the html form element name.
			PredictionSpiderRecordStaticInfo spiderRecordStaticInfo = new PredictionSpiderRecordStaticInfo();
			spiderRecordInfoService.selectIntervalDataBase("spider_record_info", dayBeforeCurrent, dayBeforeCurrent + 1)
					.forEach((v) -> {
						long timeDelay = v.getUpdate_time().getTime() - v.getCreate_time().getTime();
						spiderRecordStaticInfo.statistics(timeDelay, v);
					});

			PredictionSpiderRecordStaticInfo spiderRecordStaticInfo1 = new PredictionSpiderRecordStaticInfo();
			spiderRecordInfoService.selectIntervalDataBase("spider_record_info", dayBeforeCurrent, dayBeforeCurrent + 1)
					.forEach((v) -> {
						long timeDelay = v.getUpdate_time().getTime() - v.getCreate_time().getTime();
						spiderRecordStaticInfo1.statistics(timeDelay, v);
					});
			JsonArray arr = new JsonArray();
			arr.add(JsonObject.mapFrom(spiderRecordStaticInfo));
			arr.add(JsonObject.mapFrom(spiderRecordStaticInfo1));
			ctx.response().end(arr.encodePrettily());
		});
		router.post("/detailSourceId").handler(ctx -> {
			ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
			JsonArray arr = new JsonArray();
			spiderRateInfoService.getRateMap().forEach((k, v) -> {

				arr.add(JsonObject.mapFrom(v));
			});
			ctx.response().end(arr.encodePrettily());
		});
		router.post("/getWheelScore").handler(ctx -> {
			ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
			ctx.response().end(String.valueOf(RateLevel.TEN.getRateVal()));
		});
		router.post("/predictRecord/:dayInterval/:combineTimeSlice/:taskNum/:wheelScore").handler(ctx -> {

			ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
			JsonArray arr = new JsonArray();
			int dayInterval = 7;
			int combineTimeSlice = 5;
			int taskNum = 15;
			int wheelScore = 2000;
			try {
				dayInterval = Integer.parseInt(ctx.request().getParam("dayInterval"));
				combineTimeSlice = Integer.parseInt(ctx.request().getParam("combineTimeSlice"));
				taskNum = Integer.parseInt(ctx.request().getParam("taskNum"));
				wheelScore = Integer.parseInt(ctx.request().getParam("wheelScore"));
			} catch (Exception e) {
				e.printStackTrace();
				sendError(400, ctx.response());
			}
			RateLevel.TEN.setRateVal(wheelScore);
			PredictionRecordStaticInfoKey predictionRecordStaticInfoKey = PredictionRecordStaticInfoKey
					.getInstance(dayInterval, combineTimeSlice, taskNum, wheelScore);
			PredictionRecordStaticInfoValue predictionRecordStaticInfoValue = predirctSpiderRecordInfoMap
					.get(predictionRecordStaticInfoKey);
			if (predictionRecordStaticInfoValue == null) {
				predirctSpiderRecordInfoMap.put(predictionRecordStaticInfoKey,
						PredictionBootStrap.predirctSpiderRecordInfo(context, dayInterval, combineTimeSlice, taskNum));
			}
			PredictionRecordStaticInfoValue predictionRecordStaticInfoValueNotNull = predirctSpiderRecordInfoMap
					.get(predictionRecordStaticInfoKey);
			arr.add(JsonObject.mapFrom(predictionRecordStaticInfoValueNotNull.getPredictRecord()));
			arr.add(JsonObject.mapFrom(predictionRecordStaticInfoValueNotNull.getTrueRecord()));
			arr.add(JsonObject.mapFrom(predictionRecordStaticInfoValueNotNull.getTimeSliceAddNumMap()));

			ctx.response().end(arr.encodePrettily());
		});
		
		router.post("/getSpecialPredictRecord/:dayInterval/:combineTimeSlice/:taskNum/:wheelScore/:sourceId").handler(ctx -> {

			ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
			JsonArray arr = new JsonArray();
			int dayInterval = 7;
			int combineTimeSlice = 5;
			int taskNum = 15;
			int wheelScore = 2000;
			String sourceId = "";
			try {
				dayInterval = Integer.parseInt(ctx.request().getParam("dayInterval"));
				combineTimeSlice = Integer.parseInt(ctx.request().getParam("combineTimeSlice"));
				taskNum = Integer.parseInt(ctx.request().getParam("taskNum"));
				wheelScore = Integer.parseInt(ctx.request().getParam("wheelScore"));
				sourceId = ctx.request().getParam("sourceId");
			} catch (Exception e) {
				e.printStackTrace();
				sendError(400, ctx.response());
			}
			RateLevel.TEN.setRateVal(wheelScore);
			PredictionRecordStaticInfoKey predictionRecordStaticInfoKey = PredictionRecordStaticInfoKey
					.getInstance(dayInterval, combineTimeSlice, taskNum, wheelScore);
			PredictionRecordStaticInfoValue predictionRecordStaticInfoValue = predirctSpiderRecordInfoMap
					.get(predictionRecordStaticInfoKey);
			if (predictionRecordStaticInfoValue == null) {
				predirctSpiderRecordInfoMap.put(predictionRecordStaticInfoKey,
						PredictionBootStrap.predirctSpiderRecordInfo(context, dayInterval, combineTimeSlice, taskNum));
			}
			PredictionRecordStaticInfoValue predictionRecordStaticInfoValueNotNull = predirctSpiderRecordInfoMap
					.get(predictionRecordStaticInfoKey);
			HashSet<Date> hashSet = predictionRecordStaticInfoValueNotNull.getPredictMap().get(sourceId);
			ctx.response().end(hashSet.toString());
		});
		vertx.createHttpServer().requestHandler(router::accept).listen(8079);
	}

	private static void handleGetTask(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		int taskNum = 1;
		try {
			taskNum = Integer.parseInt(routingContext.request().getParam("taskNum"));
		} catch (Exception e) {
			sendError(400, response);
			return;
		}
		JsonArray arr = new JsonArray();
		if (calAbility.getSpiderScheduleAbility().get() > 0) {
			if (calAbility.getSpiderScheduleAbility().addAndGet(0 - taskNum) < 0) {
				taskNum = calAbility.getSpiderScheduleAbility().addAndGet(taskNum);
			} else {
				calAbility.getSpiderScheduleAbility().addAndGet(taskNum);
			}
			List<SpiderScheduleDto> list = spiderSortService.getTask(taskNum, spiderRateInfoService);
			list.forEach((v) -> {
				arr.add(JsonObject.mapFrom(v));
			});
			int size = list.size();
			calAbility.getSpiderScheduleAbility().addAndGet(0 - size);
			logger.info("当前要的公众号数目：" + taskNum + ",5s内的剩余抓取量：" + calAbility.getSpiderScheduleAbility() + ",获取的队列大小:"
					+ size + " 要抓的数据为：" + list);
			response.putHeader("content-type", "application/json").end(arr.encodePrettily());
		} else {
			int getSize = 3;
			if(weixinListRequest.size()<3){
				getSize=weixinListRequest.size();
			}
			for(int i=0; i< getSize; i++){
				arr.add(JsonObject.mapFrom(weixinListRequest.remove(0)));
			}
			response.putHeader("content-type", "application/json").end(arr.encodePrettily());
		}

	}

	private void handleTaskError(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		String sourceId = null;
		try {
			sourceId = String.valueOf(routingContext.request().getParam("sourceId"));
		} catch (Exception e) {
			sendError(400, response);
			return;
		}
		// 整理次数
//		if (errorHandleMap.containsKey(sourceId)) {
//			errorHandleMap.put(sourceId, errorHandleMap.get(sourceId) + 1);
//		} else {
//			errorHandleMap.put(sourceId, 1);
//		}
//		if (errorHandleMap.get(sourceId) <= 15) {
			if (spiderRateInfoService.getRateMap().containsKey(sourceId)) {
				spiderSortService.addErrorTask(sourceId, spiderRateInfoService);
				logger.info("spiderSchedule handleTaskError:" + sourceId);
			}
//		}
		sendOK(200, response);
	}

	private void handleAddTask(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		String sourceId = routingContext.request().getParam("sourceId");
		if (sourceId == null) {
			sendError(400, response);
		} else {
			spiderSortService.addTask(sourceId, spiderRateInfoService);
			logger.info("spiderSchedule handleAddTask:" + sourceId);
			response.end();
		}
	}

	private void handleGetRateMap(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		String sourceId = null;
		try {
			sourceId = routingContext.request().getParam("sourceId");
			response.putHeader("content-type", "application/json")
					.end(JsonObject.mapFrom(spiderRateInfoService.getRateMap().get(sourceId)).encodePrettily());
		} catch (Exception e) {
			JsonArray arr = new JsonArray();
			spiderRateInfoService.getRateMap().forEach((k, v) -> arr.add(JsonObject.mapFrom(v)));
			response.putHeader("content-type", "application/json").end(arr.encodePrettily());
		}
	}

	private static void sendError(int statusCode, HttpServerResponse response) {
		response.setStatusCode(statusCode).end("400 error");
	}

	private void sendOK(int statusCode, HttpServerResponse response) {
		response.setStatusCode(statusCode).end("200 ok");
	}
}
