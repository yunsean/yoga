package com.yoga.core.mybatis;

import com.yoga.core.utils.NumberUtil;
import com.yoga.core.utils.StringUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class LongArrayTypeHandler extends BaseTypeHandler<List<Long>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Long> parameter, JdbcType jdbcType) throws SQLException {
        String value = null;
        if (parameter != null) value = parameter.stream().map(String::valueOf).collect(Collectors.joining(","));
        ps.setString(i, value);
    }
    @Override
    public List<Long> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String code = rs.getString(columnName);
        return (rs.wasNull() || StringUtil.isBlank(code)) ? null : Arrays.stream(code.split(",")).map(NumberUtil::optLong).collect(Collectors.toList());
    }
    @Override
    public List<Long> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String code = rs.getString(columnIndex);
        return (rs.wasNull() || StringUtil.isBlank(code)) ? null : Arrays.stream(code.split(",")).map(NumberUtil::optLong).collect(Collectors.toList());
    }
    @Override
    public List<Long> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String code = cs.getString(columnIndex);
        return (cs.wasNull() || StringUtil.isBlank(code)) ? null : Arrays.stream(code.split(",")).map(NumberUtil::optLong).collect(Collectors.toList());
    }
}