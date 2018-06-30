package com.yoga.wechat.weixin

import com.yoga.core.service.BaseService
import com.yoga.wechat.weixin.model.ImageEventMessage
import com.yoga.wechat.weixin.model.LocationEventMessage
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage
import org.springframework.stereotype.Service

@Service
open class MessageCache() : BaseService() {

    private val cacheKeyPrefix = "wxmc_"
    private val cacheExpireSecond = 60L     //缓存失效时间为1分钟
    open fun savePicEventMessage(eventMessage: WxMpXmlMessage): Boolean {
        if (eventMessage.msgType != "event") return false
        if (eventMessage.event != "pic_sysphoto" && eventMessage.event != "pic_photo_or_album" && eventMessage.event != "pic_weixin") return false
        val cache = ImageEventMessage(eventMessage.eventKey, 0, eventMessage.sendPicsInfo.count.toInt())
        redisOperator.set(cacheKeyPrefix + eventMessage.fromUser, cache, cacheExpireSecond)
        return true
    }

    open fun updatePicImageMessage(imageMessage: WxMpXmlMessage) {
        if (imageMessage.msgType == "image") {
            runInLock("com.yoga.wechat.weixin.MessageCache.${imageMessage.fromUser}", {
                val cache = redisOperator.get<ImageEventMessage>(cacheKeyPrefix + imageMessage.fromUser, ImageEventMessage::class.java)
                if (cache == null) return@runInLock
                cache.imageCount++
                if (cache.imageCount > cache.totalCount) return@runInLock redisOperator.remove(cacheKeyPrefix + imageMessage.fromUser)
                redisOperator.set(cacheKeyPrefix + imageMessage.fromUser, cache, cacheExpireSecond)
                imageMessage.eventKey = cache.eventKey
            })
        }
    }

    private val locationCacheKeyPrefix = "wxlmc_"
    private val locationCacheExpireSecond = 3600L   //缓存失效时间为1小时
    open fun saveUserLocation(eventMessage: WxMpXmlMessage): Boolean {
        if (eventMessage.msgType != "event") return false
        if (eventMessage.event != "LOCATION") return false
        val cache = LocationEventMessage(eventMessage.longitude, eventMessage.latitude)
        redisOperator.set(locationCacheKeyPrefix + eventMessage.fromUser, cache, locationCacheExpireSecond)
        return true
    }
    open fun getUserLocation(openId: String?): LocationEventMessage? {
        if (openId == null) return null
        return redisOperator.get<LocationEventMessage>(locationCacheKeyPrefix + openId, LocationEventMessage::class.java)
    }
}