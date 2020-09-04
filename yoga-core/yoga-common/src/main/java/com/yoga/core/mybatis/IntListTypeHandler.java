package com.yoga.core.mybatis;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class IntListTypeHandler extends BaseTypeHandler<List<Integer>> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<Integer> integers, JdbcType jdbcType) throws SQLException {
        String str = Joiner.on(",").skipNulls().join(integers);
        preparedStatement.setString(i, str);
    }

    @Override
    public List<Integer> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return this.stringToList(rs.getString(columnName));
    }

    @Override
    public List<Integer> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return this.stringToList(rs.getString(columnIndex));
    }

    @Override
    public List<Integer> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return this.stringToList(cs.getString(columnIndex));
    }

    private List<Integer> stringToList(String str) {
        return Strings.isNullOrEmpty(str) ? new ArrayList<>() : Arrays.stream(str.split(",")).map(Integer::valueOf).collect(Collectors.toList());
    }
}
