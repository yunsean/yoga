package com.yoga.wechat.actions


import com.yoga.core.interfaces.wechat.EventType
import com.yoga.core.interfaces.wechat.WechatAction
import com.yoga.core.service.BaseService
import com.yoga.core.spring.SpringContext
import com.yoga.wechat.weixin.WeixinService
import org.apache.commons.lang3.ClassUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AssignableTypeFilter
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.PostConstruct

@Service
open class ActionsService : BaseService() {

    @Autowired
    private val springContext: SpringContext? = null
    private var replyActions: MutableList<ActionItem> = ArrayList<ActionItem>()

    @PostConstruct
    open fun loadActions() {
        logger.info("查找微信应答服务")
        val scan = ClassPathScanningCandidateComponentProvider(false)
        scan.addIncludeFilter(AssignableTypeFilter(WechatAction::class.java))
        val beanDefinitionSet = scan.findCandidateComponents("com.yoga.**")
        for (beanDefinition in beanDefinitionSet) {
            try {
                val service = ClassUtils.getClass(beanDefinition.beanClassName)
                val action = springContext?.getApplicationContext()?.getBean(service) as WechatAction?
                if (action == null) continue
                val name = springContext?.getApplicationContext()?.getBeanNamesForType(service)
                if (name == null || name.size < 1) continue
                val events = action.supportEvent().toSet()
                val needConfig = action.needConfig()
                if (replyActions.find { it.code.equals(action.code) } != null) {
                    logger.info("存在重复CODE：${action.code}，已经丢弃$name")
                    continue
                }
                replyActions.add(ActionItem(action.name, action.code, events, needConfig, action))
                logger.info("找到微信应答服务：" + action.name)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        logger.info("共找到" + replyActions.size + "个微信应答服务")
    }

    open fun actions() = replyActions
    open fun actions(event: EventType) = replyActions.filter { it.events.contains(event) }
    open fun action(code: String) = replyActions.find { it.code.equals(code) }

    companion object {
        protected var logger = LoggerFactory.getLogger(WeixinService::class.java)
    }
}
