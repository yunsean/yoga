package com.yoga.utility.district.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.utility.district.model.District;
import com.yoga.utility.sms.model.SmsResult;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DistrictMapper extends MyMapper<District> {
}
