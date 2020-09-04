package com.yoga.core.mybatis;

import com.yoga.core.utils.StringUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@MappedTypes(LocalDate.class)
public class LocalDateTypeHandler extends BaseTypeHandler<LocalDate> {
    private BaseTypeHandler typeHandler = null;
    private final static DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, LocalDate localDate, JdbcType jdbcType) throws SQLException {
        if (localDate != null) preparedStatement.setString(i, localDate.format(dft));
    }

    @Override
    public LocalDate getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String value = resultSet.getString(s);
        if (StringUtil.isBlank(value)) return null;
        return LocalDate.parse(value, dft);
    }

    @Override
    public LocalDate getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String value = resultSet.getString(i);
        if (StringUtil.isBlank(value)) return null;
        return LocalDate.parse(value, dft);
    }

    @Override
    public LocalDate getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String value = callableStatement.getString(i);
        if (StringUtil.isBlank(value)) return null;
        return LocalDate.parse(value, dft);
    }
}
