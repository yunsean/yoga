package com.yoga.wechat.weiweb

import com.yoga.core.exception.BusinessException
import com.yoga.core.service.BaseService
import com.yoga.wechat.weixin.WeixinServiceFactory
import org.apache.commons.collections.map.HashedMap
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/** Created on 2017/8/22 **/
@Service
open class WeiXinJsSdkService : BaseService() {
    @Autowired
    private val weiXingServiceFactory: WeixinServiceFactory? = null

    open fun getConfig(tid: Long, accountId: Long, url: String): HashedMap {
        val result = weiXingServiceFactory?.getService(accountId)?.createJsapiSignature(url) ?: throw BusinessException("获取config错误")
        val temp = weiXingServiceFactory?.getService(accountId)?.getJsapiTicket();
        val map = HashedMap()
        map.put("appId", result.appId)
        map.put("nonceStr", result.nonceStr)
        map.put("timestamp", result.timestamp)
        map.put("signature", result.signature)
        map.put("jsapiTicket", temp)
        return map
    }

}