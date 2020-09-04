package com.yoga.content.property.mapper;

import com.yoga.content.column.model.Column;
import com.yoga.content.property.model.Property;
import com.yoga.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PropertyMapper extends MyMapper<Property> {
    List<Property> childrenOf(@Param("tenantId") long tenantId,
                              @Param("parentId") long parentId,
                              @Param("containSelf") boolean containSelf);
    List<Property> childrenOfCode(@Param("tenantId") long tenantId,
                                  @Param("parentCode") String parentCode,
                                  @Param("containSelf") boolean containSelf);
    List<Property> listByIds(@Param("tenantId") long tenantId,
                             @Param("ids") List<Long> ids);
}
