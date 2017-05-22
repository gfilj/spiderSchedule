package com.netease.spiderSchedule.service.spiderRecordInfo.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.netease.spiderSchedule.mapper.BaseMapper;
import com.netease.spiderSchedule.mapper.SpiderRecordInfoMapper;
import com.netease.spiderSchedule.model.SpiderRecordInfo;
import com.netease.spiderSchedule.service.base.impl.AbsBaseServiceImpl;
import com.netease.spiderSchedule.service.spiderRecordInfo.SpiderRecodeInfoService;

@Service("spiderRecordInfoServie")
public class SpiderRecordInfoServiceImpl extends AbsBaseServiceImpl<SpiderRecordInfo, Serializable> implements SpiderRecodeInfoService{
	
	private SpiderRecordInfoMapper spiderRecordMapper;
	@Override
	@Autowired
	public void setBaseMapper(BaseMapper<SpiderRecordInfo, Serializable> spiderRecordInfoMapper) {
		setAbsBaseMapper(spiderRecordInfoMapper);
		this.spiderRecordMapper = (SpiderRecordInfoMapper)spiderRecordInfoMapper;
		
	}

	@Override
	public List<SpiderRecordInfo> selectInterval(int start, int end) {
		try {
			return spiderRecordMapper.selectInterval(start, end);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.<SpiderRecordInfo>emptyList();
	}

	@Override
	public List<SpiderRecordInfo> selectIntervalDataBase(String database, int start, int end)
			throws DataAccessException {
		try {
			return spiderRecordMapper.selectIntervalDataBase(database, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.<SpiderRecordInfo>emptyList();
	}

	@Override
	public List<SpiderRecordInfo> selectTest1() throws DataAccessException {
		try {
			return spiderRecordMapper.selectTest1();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.<SpiderRecordInfo>emptyList();
	}

	@Override
	public List<SpiderRecordInfo> selectTest2() throws DataAccessException {
		try {
			return spiderRecordMapper.selectTest2();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.<SpiderRecordInfo>emptyList();
	}

}
