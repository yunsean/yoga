package com.yoga.utility.push.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.utility.push.model.PushUser;
import com.yoga.utility.sms.model.SmsResult;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PushUserMapper extends MyMapper<PushUser> {
}
