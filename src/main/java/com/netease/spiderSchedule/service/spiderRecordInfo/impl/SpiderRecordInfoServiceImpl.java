package com.netease.spiderSchedule.service.spiderRecordInfo.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

}
