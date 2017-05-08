package com.netease.spiderSchedule.service.spiderSourceInfo.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.netease.spiderSchedule.mapper.BaseMapper;
import com.netease.spiderSchedule.mapper.SpiderSourceInfoMapper;
import com.netease.spiderSchedule.model.SpiderSourceInfo;
import com.netease.spiderSchedule.service.base.impl.AbsBaseServiceImpl;
import com.netease.spiderSchedule.service.spiderSourceInfo.SpiderSourceInfoService;

@Service("spiderSourceInfoService")
public class SpiderSourceInfoServiceImpl extends AbsBaseServiceImpl<SpiderSourceInfo, Serializable> implements SpiderSourceInfoService{

	private SpiderSourceInfoMapper spiderSourceMapper;
	@Override
	@Autowired
	public void setBaseMapper(BaseMapper<SpiderSourceInfo, Serializable> spiderSourceInfoMapper) {
		setAbsBaseMapper(spiderSourceInfoMapper);
		spiderSourceMapper = (SpiderSourceInfoMapper)spiderSourceInfoMapper;
	}

	@Override
	public SpiderSourceInfo selectBySourceid(String sourceid) throws DataAccessException {
		try {
			return spiderSourceMapper.selectBySourceid(sourceid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
