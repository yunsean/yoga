package com.yoga.wechat.base

import com.yoga.core.controller.BaseEndController
import com.yoga.wechat.exception.NotBindException
import com.yoga.wechat.exception.NotInWechatException
import com.yoga.wechat.oauth2.OAuth2Service
import org.apache.shiro.authz.UnauthenticatedException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.ModelAndView
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

abstract class WxBaseEndController(): BaseEndController() {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NotInWechatException::class)
    @Throws(IOException::class)
    internal fun handleAllException(ex: NotInWechatException, response: HttpServletResponse): ModelAndView {
        val mav = ModelAndView(ex.redirect())
        mav.addObject("error", ex.message)
        return mav
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NotBindException::class)
    @Throws(IOException::class)
    internal fun handleAllException(ex: NotBindException, response: HttpServletResponse): ModelAndView {
        val mav = ModelAndView(ex.redirect())
        mav.addObject("error", ex.message)
        return mav
    }
}