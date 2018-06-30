package com.yoga.wechat

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
open class ApplicationStartup @Autowired constructor(val jdbcTemplate: JdbcTemplate) :
        ApplicationListener<ContextRefreshedEvent> {

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        jdbcTemplate.update("set names utf8mb4;")
    }
}