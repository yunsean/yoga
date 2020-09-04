package com.yoga.operator.user.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.operator.user.model.User;
import com.yoga.operator.user.model.UserData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDataMapper extends MyMapper<UserData> {
}
