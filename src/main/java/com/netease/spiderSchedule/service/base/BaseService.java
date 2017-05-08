package com.netease.spiderSchedule.service.base;

import java.io.Serializable;
import java.util.List;

import com.netease.spiderSchedule.mapper.BaseMapper;

public interface BaseService <T,ID extends Serializable> {
	void setBaseMapper(BaseMapper<T, ID> baseMapper);
	List<T> selectAll();
}
