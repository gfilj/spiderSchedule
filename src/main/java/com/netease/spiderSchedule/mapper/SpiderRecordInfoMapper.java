package com.netease.spiderSchedule.mapper;

import java.io.Serializable;
import java.util.List;

import org.springframework.dao.DataAccessException;
import com.netease.spiderSchedule.model.SpiderRecordInfo;


public interface SpiderRecordInfoMapper extends BaseMapper<SpiderRecordInfo,Serializable> {
	List<SpiderRecordInfo> selectInterval(int start, int end) throws DataAccessException;
	List<SpiderRecordInfo> selectJointInterval(int start, int end) throws DataAccessException;
	List<SpiderRecordInfo> selectIntervalDataBase(String database,int start, int end) throws DataAccessException;
	List<SpiderRecordInfo> selectTest1() throws DataAccessException;
	List<SpiderRecordInfo> selectTest2() throws DataAccessException;
}

