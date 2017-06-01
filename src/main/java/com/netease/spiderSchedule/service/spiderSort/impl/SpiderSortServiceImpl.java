package com.netease.spiderSchedule.service.spiderSort.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.netease.spiderSchedule.model.SpiderRateInfo;
import com.netease.spiderSchedule.model.SpiderScheduleDto;
import com.netease.spiderSchedule.service.spiderRateInfo.SpiderRateInfoService;
import com.netease.spiderSchedule.service.spiderSort.SpiderSortService;
import com.netease.spiderSchedule.sort.MaxHeap;
@Service("spiderSortService")
public class SpiderSortServiceImpl implements SpiderSortService{
	
//	@Autowired
//	private SpiderRateInfoService spiderRateInfoService;
	
	protected MaxHeap<SpiderScheduleDto> heapSort = new MaxHeap<SpiderScheduleDto>();
	
	@Override
	public int addTask(SpiderRateInfoService spiderRateInfoService) {
		int addCount = 0;
		for(SpiderRateInfo spiderRateInfo:spiderRateInfoService.getRateMap().values()){
			SpiderScheduleDto spiderScheduleDto = new SpiderScheduleDto(spiderRateInfo);
			if(spiderScheduleDto.getScore()>0){
				addCount ++;
				heapSort.add(spiderScheduleDto);
			}
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int timeSliceKey = (calendar.get(Calendar.HOUR)*60 + calendar.get(Calendar.MINUTE))/5;
		
		System.out.println("do call perFiveMinutesSchedule add " + addCount + " currentSliceKey " + timeSliceKey);
		return addCount;
	}

	@Override
	public List<SpiderScheduleDto> getTask(int taskNum, SpiderRateInfoService spiderRateInfoService) {
		List<SpiderScheduleDto> list = new ArrayList<SpiderScheduleDto>();
		if(taskNum==-1){
			taskNum = heapSort.size();
		}
		for (int i = 0; i < taskNum; i++) {
			SpiderScheduleDto schedule = heapSort.removeTop();
			if (schedule == null) {
				break;
			}
			//update
			spiderRateInfoService.updateRateMap(schedule);
			list.add(schedule);
		}
		return list;
	}


}
