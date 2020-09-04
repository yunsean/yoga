package com.yoga.core.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@ApiModel("API结果")
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResults<T> {
    private static final long serialVersionUID = 1L;

    @ApiParam(value = "返回值，<0代表失败，=0代表成功，>0代表成功但有警告", required = true)
    private Integer code = 0;
    @ApiParam(value = "错误信息或者警告信息")
    private String message = null;
    @ApiParam(value = "调用结果")
    private Collection<T> result = null;
    @ApiParam(value = "分页信息")
    private CommonPage page = null;

    public ApiResults(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiResults(Collection<T> result) {
        this.result = result;
    }
    public ApiResults(PageInfo<T> result) {
        this.result = result.getList();
        this.page = new CommonPage(result);
    }
    public ApiResults(Collection<T> result, CommonPage page) {
        this.result = result;
        this.page = page;
    }

    public ApiResults(int code, String message, Collection<T> result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }
    public ApiResults(int code, String message, PageInfo<T> result) {
        this.code = code;
        this.message = message;
        this.result = result.getList();
        this.page = new CommonPage(result);
    }

    public <PO> ApiResults(Collection<PO> from, Class<T> clazz) {
        this.result = BaseVo.copys(from, clazz);
    }
    public <PO> ApiResults(PageInfo<PO> from, Class<T> clazz) {
        this.result = from == null ? null : BaseVo.copys(from.getList(), clazz);
        this.page = new CommonPage(from);
    }

    public <PO> ApiResults(int code, String message, Collection<PO> from, Class<T> clazz) {
        this.code = code;
        this.message = message;
        this.result = BaseVo.copys(from, clazz);
    }
    public <PO> ApiResults(int code, String message, PageInfo<PO> from, Class<T> clazz) {
        this.code = code;
        this.message = message;
        this.result = from == null ? null : BaseVo.copys(from.getList(), clazz);
        this.page = new CommonPage(from);
    }

    public <PO> ApiResults(Collection<PO> from, Class<T> clazz, BaseVo.Converter<PO, T> converter) {
        this.result = BaseVo.copys(from, clazz, converter);
    }
    public <PO> ApiResults(PageInfo<PO> from, Class<T> clazz, BaseVo.Converter<PO, T> converter) {
        this.result = from == null ? null : BaseVo.copys(from.getList(), clazz, converter);
        this.page = new CommonPage(from);
    }
    public <PO> ApiResults(PageInfo<PO> from, Class<T> clazz, BaseVo.Converter2<PO, T> converter) {
        this.result = from == null ? null : BaseVo.copys(from.getList(), clazz, converter);
        this.page = new CommonPage(from);
    }

    public <PO> ApiResults(int code, String message, Collection<PO> from, Class<T> clazz, BaseVo.Converter<PO, T> converter) {
        this.code = code;
        this.message = message;
        this.result = BaseVo.copys(from, clazz, converter);
    }
    public <PO> ApiResults(int code, String message, Collection<PO> from, Class<T> clazz, BaseVo.Converter2<PO, T> converter) {
        this.code = code;
        this.message = message;
        this.result = BaseVo.copys(from, clazz, converter);
    }
    public <PO> ApiResults(int code, String message, PageInfo<PO> from, Class<T> clazz, BaseVo.Converter<PO, T> converter) {
        this.code = code;
        this.message = message;
        this.result = from == null ? null : BaseVo.copys(from.getList(), clazz);
        this.page = new CommonPage(from);
    }
    public <PO> ApiResults(int code, String message, PageInfo<PO> from, Class<T> clazz, BaseVo.Converter2<PO, T> converter) {
        this.code = code;
        this.message = message;
        this.result = from == null ? null : BaseVo.copys(from.getList(), clazz);
        this.page = new CommonPage(from);
    }
}
