package com.netease.spiderSchedule.controller;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.netease.spiderSchedule.model.SpiderRateInfoDto;
import com.netease.spiderSchedule.service.spiderRateInfo.SpiderRateInfoService;
import com.netease.spiderSchedule.service.spiderRateInfo.impl.SpiderRateInfoServiceImpl;
import com.netease.spiderSchedule.service.spiderSort.SpiderSortService;
import com.netease.spiderSchedule.service.spiderSort.impl.SpiderSortServiceImpl;
import com.netease.spiderSchedule.util.Runner;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class SpiderScheduleController extends AbstractVerticle {

	private static ClassPathXmlApplicationContext context;

	private static SpiderRateInfoService spiderRateInfoService;
	private static SpiderSortService spiderSortService;
	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext("classpath*:config/spring-application.xml");
		context.start();
		spiderRateInfoService = (SpiderRateInfoServiceImpl)context.getBean("spiderRateInfoService");
		spiderSortService = (SpiderSortServiceImpl)context.getBean("smoothingAlgorithmSpiderSortService");
		Runner.runExample(SpiderScheduleController.class);
	}
	
	

	@Override
	public void start() {
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		router.get("/getTask/:taskNum").handler(this::handleGetTask);
		router.put("/addTask").handler(this::handleAddTask);
		router.get("/getRateMap").handler(this::handleGetRateMap);
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
		spiderSortService.getTask(taskNum, spiderRateInfoService).forEach((v)->arr.add(JsonObject.mapFrom(v)));
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
		JsonArray arr = new JsonArray();
		spiderRateInfoService.getRateMap().forEach((k, v) -> arr.add(JsonObject.mapFrom(v)));
		response.putHeader("content-type", "application/json").end(arr.encodePrettily());
	}

	private void sendError(int statusCode, HttpServerResponse response) {
		response.setStatusCode(statusCode).end("400 error");
	}

}
