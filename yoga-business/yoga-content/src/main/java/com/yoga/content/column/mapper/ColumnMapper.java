package com.yoga.content.column.mapper;

import com.yoga.content.column.model.Column;
import com.yoga.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ColumnMapper extends MyMapper<Column> {
    List<Column> list(@Param("tenantId") long tenantId,
                      @Param("keyword") String keyword,
                      @Param("enabled") Boolean enabled,
                      @Param("parentId") Long parentId,
                      @Param("hidden") Boolean hidden);
    Column get(@Param("id") Long id,
               @Param("code") String code);
    List<Column> childrenOf(@Param("tenantId") long tenantId,
                            @Param("parentId") long parentId,
                            @Param("includeHidden") boolean includeHidden,
                            @Param("containSelf") boolean containSelf);
}
