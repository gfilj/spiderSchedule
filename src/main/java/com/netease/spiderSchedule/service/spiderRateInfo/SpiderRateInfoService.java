package com.netease.spiderSchedule.service.spiderRateInfo;

import java.util.List;
import java.util.Map;

import com.netease.spiderSchedule.model.SpiderRateInfo;
import com.netease.spiderSchedule.model.SpiderRateInfoDto;
import com.netease.spiderSchedule.model.SpiderScheduleDto;

public interface SpiderRateInfoService {

	void updateRateMap(SpiderScheduleDto spiderScheduleDto);

	void addRateMap(SpiderRateInfoDto spiderRateInfoDto);

	void generateRateMap(int start, int end);

	Map<String, SpiderRateInfo> getRateMap();

	List<SpiderRateInfo> getDelaySpiderRateInfoList();

	int getCountTooOld();

	void cleanTaskQueue();

	int getEfficeTiveSourceIdNum();
}
