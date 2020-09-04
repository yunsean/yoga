package com.yoga.core.mybatis;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import tk.mybatis.mapper.entity.Example;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Stack;

public class MapperQuery<T> {
    private Example example;
    private Example.Criteria criteria;
    private Stack<Example.Criteria> subCriterias = new Stack<>();
    public MapperQuery(Class<T> clazz) {
        this.example = new Example(clazz);
        this.criteria = example.createCriteria();
    }

    public static <T> MapperQuery<T> create(Class<T> clazz) {
        return new MapperQuery<>(clazz);
    }

    //AND OR 嵌套
    public MapperQuery<T> and() {
        subCriterias.push(this.criteria);
        this.criteria = example.and();
        return this;
    }
    public MapperQuery<T> or() {
        subCriterias.push(this.criteria);
        this.criteria = example.or();
        return this;
    }
    public MapperQuery<T> pop() {
        if (subCriterias.size() < 1) throw new RuntimeException("Invalid criteria stack.");
        this.criteria = subCriterias.pop();
        return this;
    }

    public MapperQuery<T> exclude(String... properties) {
        example.excludeProperties(properties);
        return this;
    }
    public MapperQuery<T> include(String... properties) {
        example.selectProperties(properties);
        return this;
    }
    public MapperQuery<T> distinct() {
        example.setDistinct(true);
        return this;
    }
    public MapperQuery<T> forUpdate() {
        example.setForUpdate(true);
        return this;
    }
    public MapperQuery<T> tableName(String tableName) {
        example.setTableName(tableName);
        return this;
    }

    //AND
    public MapperQuery<T> andIsNull(String property) {
        this.criteria.andIsNull(property);
        return this;
    }
    public MapperQuery<T> andIsNotNull(String property) {
        this.criteria.andIsNotNull(property);
        return this;
    }
    public MapperQuery<T> andEqualTo(String property, Object value) {
        this.criteria.andEqualTo(property, value);
        return this;
    }
    public MapperQuery<T> andNotEqualTo(String property, Object value) {
        this.criteria.andNotEqualTo(property, value);
        return this;
    }
    public MapperQuery<T> andGreaterThan(String property, Object value) {
        this.criteria.andGreaterThan(property, value);
        return this;
    }
    public MapperQuery<T> andGreaterThanOrEqualTo(String property, Object value) {
        this.criteria.andGreaterThanOrEqualTo(property, value);
        return this;
    }
    public MapperQuery<T> andLessThan(String property, Object value) {
        this.criteria.andLessThan(property, value);
        return this;
    }
    public MapperQuery<T> andLessThanOrEqualTo(String property, Object value) {
        this.criteria.andLessThanOrEqualTo(property, value);
        return this;
    }
    public MapperQuery<T> andIn(String property, Iterable values) {
        this.criteria.andIn(property, values);
        return this;
    }
    public MapperQuery<T> andNotIn(String property, Iterable values) {
        this.criteria.andNotIn(property, values);
        return this;
    }
    public MapperQuery<T> andBetween(String property, Object value1, Object value2) {
        this.criteria.andNotBetween(property, value1, value2);
        return this;
    }
    public MapperQuery<T> andNotBetween(String property, Object value1, Object value2) {
        this.criteria.andNotBetween(property, value1, value2);
        return this;
    }
    public MapperQuery<T> andLike(String property, String value) {
        this.criteria.andLike(property, value);
        return this;
    }
    public MapperQuery<T> andNotLike(String property, String value) {
        this.criteria.andNotLike(property, value);
        return this;
    }
    public MapperQuery<T> andCondition(String condition) {
        this.criteria.andCondition(condition);
        return this;
    }
    public MapperQuery<T> andCondition(String condition, Object value) {
        this.criteria.andCondition(condition, value);
        return this;
    }
    public MapperQuery<T> andEqualTo(Object param) {
        this.criteria.andEqualTo(param);
        return this;
    }
    public MapperQuery<T> andAllEqualTo(Object param) {
        this.criteria.andAllEqualTo(param);
        return this;
    }

    //OR
    public MapperQuery<T> orIsNull(String property) {
        this.criteria.orIsNull(property);
        return this;
    }
    public MapperQuery<T> orIsNotNull(String property) {
        this.criteria.orIsNotNull(property);
        return this;
    }
    public MapperQuery<T> orEqualTo(String property, Object value) {
        this.criteria.orEqualTo(property, value);
        return this;
    }
    public MapperQuery<T> orNotEqualTo(String property, Object value) {
        this.criteria.orNotEqualTo(property, value);
        return this;
    }
    public MapperQuery<T> orGreaterThan(String property, Object value) {
        this.criteria.orGreaterThan(property, value);
        return this;
    }
    public MapperQuery<T> orGreaterThanOrEqualTo(String property, Object value) {
        this.criteria.orGreaterThanOrEqualTo(property, value);
        return this;
    }
    public MapperQuery<T> orLessThan(String property, Object value) {
        this.criteria.orLessThan(property, value);
        return this;
    }
    public MapperQuery<T> orLessThanOrEqualTo(String property, Object value) {
        this.criteria.orLessThanOrEqualTo(property, value);
        return this;
    }
    public MapperQuery<T> orIn(String property, Iterable values) {
        this.criteria.orIn(property, values);
        return this;
    }
    public MapperQuery<T> orNotIn(String property, Iterable values) {
        this.criteria.orNotIn(property, values);
        return this;
    }
    public MapperQuery<T> orBetween(String property, Object value1, Object value2) {
        this.criteria.orBetween(property, value1, value2);
        return this;
    }
    public MapperQuery<T> orNotBetween(String property, Object value1, Object value2) {
        this.criteria.orNotBetween(property, value1, value2);
        return this;
    }
    public MapperQuery<T> orLike(String property, String value) {
        this.criteria.orLike(property, value);
        return this;
    }
    public MapperQuery<T> orNotLike(String property, String value) {
        this.criteria.orNotLike(property, value);
        return this;
    }
    public MapperQuery<T> orCondition(String condition) {
        this.criteria.orCondition(condition);
        return this;
    }
    public MapperQuery<T> orCondition(String condition, Object value) {
        this.criteria.orCondition(condition, value);
        return this;
    }
    public MapperQuery<T> orEqualTo(Object param) {
        this.criteria.orEqualTo(param);
        return this;
    }
    public MapperQuery<T> orAllEqualTo(Object param) {
        this.criteria.orAllEqualTo(param);
        return this;
    }

    //AND valid
    public MapperQuery<T> andIsNull(String property, boolean valid) {
        if (valid) this.criteria.andIsNull(property);
        return this;
    }
    public MapperQuery<T> andIsNotNull(String property, boolean valid) {
        if (valid) this.criteria.andIsNotNull(property);
        return this;
    }
    public MapperQuery<T> andEqualTo(String property, Object value, boolean valid) {
        if (valid) this.criteria.andEqualTo(property, value);
        return this;
    }
    public MapperQuery<T> andNotEqualTo(String property, Object value, boolean valid) {
        if (valid) this.criteria.andNotEqualTo(property, value);
        return this;
    }
    public MapperQuery<T> andGreaterThan(String property, Object value, boolean valid) {
        if (valid) this.criteria.andGreaterThan(property, value);
        return this;
    }
    public MapperQuery<T> andGreaterThanOrEqualTo(String property, Object value, boolean valid) {
        if (valid) this.criteria.andGreaterThanOrEqualTo(property, value);
        return this;
    }
    public MapperQuery<T> andLessThan(String property, Object value, boolean valid) {
        if (valid) this.criteria.andLessThan(property, value);
        return this;
    }
    public MapperQuery<T> andLessThanOrEqualTo(String property, Object value, boolean valid) {
        if (valid) this.criteria.andLessThanOrEqualTo(property, value);
        return this;
    }
    public MapperQuery<T> andIn(String property, Iterable values, boolean valid) {
        if (valid) this.criteria.andIn(property, values);
        return this;
    }
    public MapperQuery<T> andNotIn(String property, Iterable values, boolean valid) {
        if (valid) this.criteria.andNotIn(property, values);
        return this;
    }
    public MapperQuery<T> andBetween(String property, Object value1, Object value2, boolean valid) {
        if (valid) this.criteria.andNotBetween(property, value1, value2);
        return this;
    }
    public MapperQuery<T> andNotBetween(String property, Object value1, Object value2, boolean valid) {
        if (valid) this.criteria.andNotBetween(property, value1, value2);
        return this;
    }
    public MapperQuery<T> andLike(String property, String value, boolean valid) {
        if (valid) this.criteria.andLike(property, value);
        return this;
    }
    public MapperQuery<T> andNotLike(String property, String value, boolean valid) {
        if (valid) this.criteria.andNotLike(property, value);
        return this;
    }
    public MapperQuery<T> andCondition(String condition, boolean valid) {
        if (valid) this.criteria.andCondition(condition);
        return this;
    }
    public MapperQuery<T> andCondition(String condition, Object value, boolean valid) {
        if (valid) this.criteria.andCondition(condition, value);
        return this;
    }
    public MapperQuery<T> andAllEqualTo(Object param, boolean valid) {
        if (valid) this.criteria.andAllEqualTo(param);
        return this;
    }

    //OR valid
    public MapperQuery<T> orIsNull(String property, boolean valid) {
        if (valid) this.criteria.orIsNull(property);
        return this;
    }
    public MapperQuery<T> orIsNotNull(String property, boolean valid) {
        if (valid) this.criteria.orIsNotNull(property);
        return this;
    }
    public MapperQuery<T> orEqualTo(String property, Object value, boolean valid) {
        if (valid) this.criteria.orEqualTo(property, value);
        return this;
    }
    public MapperQuery<T> orNotEqualTo(String property, Object value, boolean valid) {
        if (valid) this.criteria.orNotEqualTo(property, value);
        return this;
    }
    public MapperQuery<T> orGreaterThan(String property, Object value, boolean valid) {
        if (valid) this.criteria.orGreaterThan(property, value);
        return this;
    }
    public MapperQuery<T> orGreaterThanOrEqualTo(String property, Object value, boolean valid) {
        if (valid) this.criteria.orGreaterThanOrEqualTo(property, value);
        return this;
    }
    public MapperQuery<T> orLessThan(String property, Object value, boolean valid) {
        if (valid) this.criteria.orLessThan(property, value);
        return this;
    }
    public MapperQuery<T> orLessThanOrEqualTo(String property, Object value, boolean valid) {
        if (valid) this.criteria.orLessThanOrEqualTo(property, value);
        return this;
    }
    public MapperQuery<T> orIn(String property, Iterable values, boolean valid) {
        if (valid) this.criteria.orIn(property, values);
        return this;
    }
    public MapperQuery<T> orNotIn(String property, Iterable values, boolean valid) {
        if (valid) this.criteria.orNotIn(property, values);
        return this;
    }
    public MapperQuery<T> orBetween(String property, Object value1, Object value2, boolean valid) {
        if (valid) this.criteria.orBetween(property, value1, value2);
        return this;
    }
    public MapperQuery<T> orNotBetween(String property, Object value1, Object value2, boolean valid) {
        if (valid) this.criteria.orNotBetween(property, value1, value2);
        return this;
    }
    public MapperQuery<T> orLike(String property, String value, boolean valid) {
        if (valid) this.criteria.orLike(property, value);
        return this;
    }
    public MapperQuery<T> orNotLike(String property, String value, boolean valid) {
        if (valid) this.criteria.orNotLike(property, value);
        return this;
    }
    public MapperQuery<T> orCondition(String condition, boolean valid) {
        if (valid) this.criteria.orCondition(condition);
        return this;
    }
    public MapperQuery<T> orCondition(String condition, Object value, boolean valid) {
        if (valid) this.criteria.orCondition(condition, value);
        return this;
    }
    public MapperQuery<T> orEqualTo(Object param, boolean valid) {
        if (valid) this.criteria.orEqualTo(param);
        return this;
    }
    public MapperQuery<T> orAllEqualTo(Object param, boolean valid) {
        if (valid) this.criteria.orAllEqualTo(param);
        return this;
    }

    public MapperQuery<T> setOrderByClause(String orderByClause) {
        this.example.setOrderByClause(orderByClause);
        return this;
    }
    public MapperQuery<T> orderBy(String property) {
        this.example.orderBy(property);
        return this;
    }
    public MapperQuery<T> orderBy(String property, boolean desc) {
        if (desc) this.example.orderBy(property).desc();
        else this.example.orderBy(property).asc();
        return this;
    }

    public long count(MyMapper<T> mapper) {
        return mapper.selectCountByExample(this.example);
    }
    public boolean exist(MyMapper<T> mapper) {
        return mapper.selectCountByExample(this.example) > 0;
    }
    public int delete(MyMapper<T> mapper) {
        return mapper.deleteByExample(this.example);
    }
    public int updateSelective(MyMapper<T> mapper, T record) {
        return mapper.updateByExampleSelective(record, this.example);
    }
    public int update(MyMapper<T> mapper, T record) {
        return mapper.updateByExample(record, this.example);
    }
    public List<T> query(MyMapper<T> mapper) {
        return mapper.selectByExample(this.example);
    }
    public PageInfo<T> queryPage(MyMapper<T> mapper, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex + 1, pageSize);
        List<T> records = mapper.selectByExample(this.example);
        return new PageInfo<>(records);
    }
    public T queryOne(MyMapper<T> mapper) {
        List<T> records = mapper.selectByExample(this.example);
        if (records == null) return null;
        if (records.size() < 1) return null;
        if (records.size() > 1) throw new RuntimeException("More than one record returned.");
        return records.get(0);
    }
    public T queryFirst(MyMapper<T> mapper) {
        List<T> records = mapper.selectByExample(this.example);
        if (records == null) return null;
        if (records.size() < 1) return null;
        return records.get(0);
    }
}
