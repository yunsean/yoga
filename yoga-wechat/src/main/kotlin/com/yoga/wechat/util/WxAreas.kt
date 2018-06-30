package com.yoga.wechat.util

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.yoga.core.service.BaseService
import com.yoga.core.utils.ResourceLoader
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.annotation.PostConstruct


@Service
class WxAreas(): BaseService() {

    lateinit var areaMaps: Map<Long, WxArea>

    @PostConstruct
    open fun loadArea() {
        val resources = ResourceLoader.getResources("classpath*:/address.json")
        if (null != resources && resources.size > 0) {
            try {
                val inputStream = resources[0].url.openConnection().inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val type = object : TypeToken<List<WxArea>>() {}.type
                val areas: List<WxArea> = Gson().fromJson(reader, type)
                areaMaps = areas.map { converter(it) }.toMap()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    open fun converter(area: WxArea): Pair<Long, WxArea> {
        area.map = area.children?.map { converter(it) }?.toMap()
        area.children = null
        return area.id to area
    }
}