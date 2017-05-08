package com.netease.spiderSchedule.mapper;

import java.io.Serializable;
import java.util.List;

import org.springframework.dao.DataAccessException;
import com.netease.spiderSchedule.model.SpiderRecordInfo;


public interface SpiderRecordInfoMapper extends BaseMapper<SpiderRecordInfo,Serializable> {
	public List<SpiderRecordInfo> selectInterval(int start, int end) throws DataAccessException;
}

