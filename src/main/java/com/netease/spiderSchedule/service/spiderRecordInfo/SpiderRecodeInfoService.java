package com.netease.spiderSchedule.service.spiderRecordInfo;

import java.io.Serializable;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.netease.spiderSchedule.model.SpiderRecordInfo;
import com.netease.spiderSchedule.service.base.BaseService;

public interface SpiderRecodeInfoService extends BaseService<SpiderRecordInfo, Serializable>{
	
	List<SpiderRecordInfo> selectInterval(int start, int end);
	List<SpiderRecordInfo> selectIntervalDataBase(String database,int start, int end) throws DataAccessException;
	List<SpiderRecordInfo> selectTest1() throws DataAccessException;
	List<SpiderRecordInfo> selectTest2() throws DataAccessException;
}
