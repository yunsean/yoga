package com.yoga.content.template.mapper;

import com.yoga.content.template.model.Template;
import com.yoga.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TemplateMapper extends MyMapper<Template> {

    List<Template> list(@Param("tenantId") long tenantId,
                        @Param("keyword") String keyword,
                        @Param("enabled") Boolean enabled);
}
