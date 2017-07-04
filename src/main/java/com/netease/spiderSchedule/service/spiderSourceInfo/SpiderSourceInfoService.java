package com.netease.spiderSchedule.service.spiderSourceInfo;

import java.io.Serializable;

import org.springframework.dao.DataAccessException;

import com.netease.spiderSchedule.model.SpiderSourceInfo;
import com.netease.spiderSchedule.service.base.BaseService;

public interface SpiderSourceInfoService extends BaseService<SpiderSourceInfo, Serializable>{
	SpiderSourceInfo selectBySourceid(String sourceid) throws DataAccessException;
	void updateBySourceid(String sourceid) throws DataAccessException;
	
}
