package com.yoga.logging.mapper;

import com.yoga.logging.model.Logging;
import com.yoga.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoggingMapper extends MyMapper<Logging> {
}
