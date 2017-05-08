package com.netease.spiderSchedule.service.spiderRecordInfo;

import java.io.Serializable;
import java.util.List;

import com.netease.spiderSchedule.model.SpiderRecordInfo;
import com.netease.spiderSchedule.service.base.BaseService;

public interface SpiderRecodeInfoService extends BaseService<SpiderRecordInfo, Serializable>{
	
	List<SpiderRecordInfo> selectInterval(int start, int end);
	
}
