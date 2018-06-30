package com.yoga.wechat.base

import com.yoga.core.controller.BaseApiController
import com.yoga.core.data.CommonResult
import com.yoga.core.data.ResultConstants
import me.chanjar.weixin.common.exception.WxErrorException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import java.io.IOException
import java.lang.reflect.UndeclaredThrowableException
import javax.servlet.http.HttpServletResponse

abstract class WxBaseApiController(): BaseApiController() {

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(UndeclaredThrowableException::class)
    @Throws(IOException::class)
    fun handleAllException(ex: UndeclaredThrowableException): CommonResult {
        ex.printStackTrace()
        if (ex.undeclaredThrowable is WxErrorException) {
            val ex1 = ex.undeclaredThrowable as WxErrorException
            return CommonResult(ResultConstants.ERROR_BUSINESSERROR, ex1.error.errorMsg)
        } else {
            return CommonResult(ResultConstants.ERROR_UNKNOWN, getMessage(ex))
        }
    }
}