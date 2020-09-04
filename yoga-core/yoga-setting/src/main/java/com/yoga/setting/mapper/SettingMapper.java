package com.yoga.setting.mapper;

import com.yoga.setting.model.Setting;
import com.yoga.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SettingMapper extends MyMapper<Setting> {
}
