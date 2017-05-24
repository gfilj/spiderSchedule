package com.netease.spiderSchedule.controller;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.netease.spiderSchedule.model.SpiderRateInfoDto;
import com.netease.spiderSchedule.model.prediction.PredictionSpiderRecordStaticInfo;
import com.netease.spiderSchedule.service.spiderRateInfo.SpiderRateInfoService;
import com.netease.spiderSchedule.service.spiderRateInfo.impl.SpiderRateInfoServiceImpl;
import com.netease.spiderSchedule.service.spiderRecordInfo.SpiderRecodeInfoService;
import com.netease.spiderSchedule.service.spiderSort.SpiderSortService;
import com.netease.spiderSchedule.service.spiderSort.impl.SpiderSortServiceImpl;
import com.netease.spiderSchedule.util.Runner;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class SpiderScheduleController extends AbstractVerticle {

	private static ClassPathXmlApplicationContext context;

	private static SpiderRateInfoService spiderRateInfoService;
	private static SpiderSortService spiderSortService;
	private static SpiderRecodeInfoService spiderRecordInfoService;

	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext("classpath*:config/spring-application.xml");
		context.start();
		spiderRateInfoService = (SpiderRateInfoServiceImpl) context.getBean("spiderRateInfoService");
		spiderSortService = (SpiderSortServiceImpl) context.getBean("smoothingAlgorithmSpiderSortService");
		spiderRecordInfoService = (SpiderRecodeInfoService) context.getBean("spiderRecordInfoServie");
		Runner.runExample(SpiderScheduleController.class);
	}

	@Override
	public void start() {
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		router.get("/getTask/:taskNum").handler(this::handleGetTask);
		router.put("/addTask").handler(this::handleAddTask);
		router.get("/getRateMap/:sourceId").handler(this::handleGetRateMap);
		router.get("/daychart").handler(routingContext -> {
			routingContext.response().putHeader("content-type", "text/html")
					.end("<form action=\"/form\" method=\"post\">\n" + "    <div>\n"
							+ "        <label for=\"name\">Enter your name:</label>\n"
							+ "        <input type=\"text\" id=\"name\" name=\"name\" />\n" + "    </div>\n"
							+ "    <div class=\"button\">\n" + "        <button type=\"submit\">Send</button>\n"
							+ "    </div>" + "</form>");
		});
		router.route("/*").handler(StaticHandler.create());

		// handle the form
		router.post("/form").handler(ctx -> {
			ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
			// note the form attribute matches the html form element name.
			ctx.response().end("Hello " + ctx.request().getParam("name") + "!");
		});

		router.post("/statistics").handler(ctx -> {
			ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
			// note the form attribute matches the html form element name.
			PredictionSpiderRecordStaticInfo spiderRecordStaticInfo = new PredictionSpiderRecordStaticInfo();
			spiderRecordInfoService.selectIntervalDataBase("spider_record_info_test", 0, 1).forEach((v) -> {
				long timeDelay = v.getUpdate_time().getTime() - v.getCreate_time().getTime();
				spiderRecordStaticInfo.statistics(timeDelay, v);
			});
			
			PredictionSpiderRecordStaticInfo spiderRecordStaticInfo1 = new PredictionSpiderRecordStaticInfo();
			spiderRecordInfoService.selectIntervalDataBase("spider_record_info_test_1", 0, 1).forEach((v) -> {
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
			spiderRateInfoService.getRateMap().forEach((k,v)->{
				
				arr.add(JsonObject.mapFrom(v));
			});
			ctx.response().end(arr.encodePrettily());
		});

		vertx.createHttpServer().requestHandler(router::accept).listen(8079);
	}

	private void handleGetTask(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		int taskNum = 1;
		try {
			taskNum = Integer.parseInt(routingContext.request().getParam("taskNum"));
		} catch (Exception e) {
			sendError(400, response);
			return;
		}
		JsonArray arr = new JsonArray();
		spiderSortService.getTask(taskNum, spiderRateInfoService).forEach((v) -> arr.add(JsonObject.mapFrom(v)));
		response.putHeader("content-type", "application/json").end(arr.encodePrettily());

	}

	private void handleAddTask(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		String postValue = routingContext.getBodyAsString();
		if (postValue == null) {
			sendError(400, response);
		} else {
			SpiderRateInfoDto spiderRateInfoDto = Json.decodeValue(postValue, SpiderRateInfoDto.class);
			spiderRateInfoService.addRateMap(spiderRateInfoDto);
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

	private void sendError(int statusCode, HttpServerResponse response) {
		response.setStatusCode(statusCode).end("400 error");
	}

}
