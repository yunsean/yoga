package com.yoga.utility.uploader.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.utility.district.model.District;
import com.yoga.utility.uploader.model.UploadFile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UploadFileMapper extends MyMapper<UploadFile> {
}
