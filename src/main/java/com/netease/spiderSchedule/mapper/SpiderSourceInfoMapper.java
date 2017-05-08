package com.netease.spiderSchedule.mapper;

import java.io.Serializable;

import org.springframework.dao.DataAccessException;

import com.netease.spiderSchedule.model.SpiderSourceInfo;

public interface SpiderSourceInfoMapper extends BaseMapper<SpiderSourceInfo,Serializable> {
	/**
	 * 根据sourceID 查找
	 * @param sourceId
	 * @throws DataAccessException
	 */
	SpiderSourceInfo selectBySourceid(String sourceid) throws DataAccessException;
}
