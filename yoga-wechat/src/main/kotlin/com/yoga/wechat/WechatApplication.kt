package com.yoga.wechat

import com.yoga.core.CoreApplication
import com.yoga.resource.ResourceApplication
import com.yoga.UserApplication
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.Charset

@SpringBootApplication
open class WechatApplication {
    companion object {
        private val logger = LoggerFactory.getLogger(WechatApplication::class.java)
        private val defaultCharSet: String
            get() {
                val writer = OutputStreamWriter(ByteArrayOutputStream())
                val enc = writer.encoding
                return enc
            }

        @JvmStatic fun main(args: Array<String>) {
            logger.warn("Default Charset=" + Charset.defaultCharset())
            logger.warn("file.encoding=" + System.getProperty("file.encoding"))
            logger.warn("Default Charset=" + Charset.defaultCharset())
            logger.warn("Default Charset in Use=" + defaultCharSet)
            val objects = arrayOf<Any>(
                    CoreApplication::class.java,
                    ResourceApplication::class.java,
                    UserApplication::class.java,
                    WechatApplication::class.java)
            SpringApplicationBuilder(*objects)
                    .web(true)
                    .run(*args)
        }
    }
}
