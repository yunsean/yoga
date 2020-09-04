package com.yoga.content.template.mapper;

import com.yoga.content.template.model.TemplateField;
import com.yoga.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TemplateFieldMapper extends MyMapper<TemplateField> {

    long countOfProperty(@Param("tenantId") long tenantId,
                         @Param("type") int type,
                         @Param("code") String code);
}
