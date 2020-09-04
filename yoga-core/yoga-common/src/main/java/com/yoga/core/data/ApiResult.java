package com.yoga.core.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yoga.core.base.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ApiModel("API结果")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> {
    private static final long serialVersionUID = 1L;

    @ApiParam(value = "返回值，<0代表失败，=0代表成功，>0代表成功但有警告", required = true)
    private Integer code = 0;
    @ApiParam(value = "错误信息或者警告信息")
    private String message = null;
    @ApiParam(value = "调用结果")
    private T result = null;
    @ApiParam(value = "分页信息")
    private CommonPage page = null;

    public ApiResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiResult(T result) {
        this.result = result;
    }
    public ApiResult(int code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public <PO> ApiResult(PO from, Class<T> clazz) {
        this.result = BaseVo.copy(from, clazz);
    }
    public <PO> ApiResult(int code, String message, PO from, Class<T> clazz) {
        this.code = code;
        this.message = message;
        this.result = BaseVo.copy(from, clazz);
    }

    public <PO> ApiResult(PO from, Class<T> clazz, BaseVo.Converter<PO, T> converter) {
        this.result = BaseVo.copy(from, clazz, converter);
    }
    public <PO> ApiResult(PO from, Class<T> clazz, BaseVo.Converter2<PO, T> converter) {
        this.result = BaseVo.copy(from, clazz, converter);
    }
    public <PO> ApiResult(int code, String message, PO from, Class<T> clazz, BaseVo.Converter<PO, T> converter) {
        this.code = code;
        this.message = message;
        this.result = BaseVo.copy(from, clazz, converter);
    }
    public <PO> ApiResult(int code, String message, PO from, Class<T> clazz, BaseVo.Converter2<PO, T> converter) {
        this.code = code;
        this.message = message;
        this.result = BaseVo.copy(from, clazz, converter);
    }
}
