package com.netease.spiderSchedule.timer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.netease.spiderSchedule.controller.SpiderScheduleController;
import com.netease.spiderSchedule.ip.VPSHttp;
import com.netease.spiderSchedule.model.SpiderScheduleDto;
import com.netease.spiderSchedule.service.spiderRateInfo.SpiderRateInfoService;
import com.netease.spiderSchedule.service.spiderSort.SpiderSortService;

public class SpiderScheduleTask {

	@Autowired
	@Qualifier("spiderRateInfoService")
	private SpiderRateInfoService spiderRateInfoService;

	@Autowired
	@Qualifier("smoothingAlgorithmSpiderSortService")
	private SpiderSortService spiderSortService;
	
//	
//	@Autowired
//	private AppRecordInfoMapper appRecordInfoMapper;
//	
//	@Autowired
//	private AppImageInfoMapper appImageInfoMapper;
//
//	@Autowired
//	private NosServiceImpl nosServiceImpl;
//	
//	@Autowired
//	private SignatureUtil signatureUtil;
//	
//	@Autowired
//	@Qualifier("jedisPool")
//	private JedisPool pool;	
	
	private static ExecutorService executor = Executors.newFixedThreadPool(5);

	/**
	 * 凌晨统计
	 */
	public void zeroSchedule() {
		spiderRateInfoService.generateRateMap(0, 9);
	}

	/**
	 * 相隔5分钟计算任务数
	 */

	public void perFiveMinutesSchedule() {

		spiderSortService.addTask(spiderRateInfoService);
		SpiderScheduleController.errorHandleMap.clear();
	}

	/**
	 * 相隔20s
	 */
	public void per20sSchedule() {
		SpiderScheduleController.calAbility.setReset(true);
	}

	/**
	 * per5sScheduleCron
	 */
	public void per5sSchedule() {
		// 获取ip
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("size", "5");// 需要ip个数
		String proxyjson = VPSHttp.getInstance().sendHttpPost("http://test.nbot.netease.com/getProxyUsable.action",
				maps);// 获取接口
		JSONArray json = JSON.parseArray(proxyjson);
		if (json != null) {
			for (int i = 0; i < json.size(); i++) {
				List<SpiderScheduleDto> task = spiderSortService.getTask(1, spiderRateInfoService);
				SpiderScheduleDto spiderScheduleDto = task.get(0);
				
				executor.submit(new GrabSpiderTask(spiderScheduleDto.getSourceId(),json.getJSONObject(i),spiderScheduleDto.getPriority(), spiderScheduleDto.getAppId()));
			}
		}
	}

	/**
	 * 半个小时清除队列任务
	 */

	public void cleanTaskQueue() {
		spiderRateInfoService.cleanTaskQueue();
	}
}
