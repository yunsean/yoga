package com.yoga.wechat.reply

import com.yoga.core.annotation.Explain
import com.yoga.core.data.CommonResult
import com.yoga.core.exception.BusinessException
import com.yoga.core.exception.IllegalArgumentException
import com.yoga.core.interfaces.wechat.EventType
import com.yoga.core.utils.MapConverter
import com.yoga.user.model.LoginUser
import com.yoga.wechat.actions.ActionsService
import com.yoga.wechat.base.WxBaseApiController
import org.apache.shiro.authz.annotation.RequiresAuthentication
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Explain("WX回复管理", module = "wx_reply")
@RestController(value = "wechatReplyApiController")
@RequestMapping("/api/wechat/reply")
internal open class ReplyApiController @Autowired constructor(
        val actionsService: ActionsService,
        val replyService: ReplyService) : WxBaseApiController() {

    @Explain("从公众号刷新")
    @RequiresPermissions("wx_reply.update")
    @RequestMapping("/refresh")
    open fun refresh(user: LoginUser, @Valid dto: RefreshDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        replyService.sync(dto.tid, dto.accountId!!, user.nickname, user.id)
        return CommonResult()
    }

    @Explain("添加回复")
    @RequiresPermissions("wx_reply.update")
    @RequestMapping("/add")
    open fun add(@Valid dto: AddDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        if (dto.event == Event.keyword && dto.getKeyword().size < 1) throw BusinessException("请输入关键字")
        if (dto.type in listOf(MessageType.image, MessageType.video, MessageType.video, MessageType.voice) && dto.mediaId.isNullOrBlank()) throw BusinessException("请选择回复素材")
        if (dto.type == MessageType.music && dto.musicUrl.isNullOrBlank()) throw BusinessException("请输入音乐URL")
        if (dto.type == MessageType.text && dto.text.isNullOrBlank()) throw BusinessException("请输入回复内容")
        replyService.add(dto.tid, dto.accountId!!, dto.event!!, dto.tag, dto.gender, dto.name, dto.keyword, dto.type!!, dto.text, dto.mediaId, dto.mediaName, dto.title, dto.description, dto.musicUrl, dto.hqMusicUrl, dto.pluginCode, dto.pluginConfig)
        return CommonResult()
    }
    @Explain("更新回复")
    @RequiresPermissions("wx_reply.update")
    @RequestMapping("/update")
    open fun update(@Valid dto: UpdateDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        if (dto.event == Event.keyword && dto.getKeyword().size < 1) throw BusinessException("请输入关键字")
        if (dto.type in listOf(MessageType.image, MessageType.video, MessageType.video, MessageType.voice) && dto.mediaId.isNullOrBlank()) throw BusinessException("请选择回复素材")
        if (dto.type == MessageType.music && dto.musicUrl.isNullOrBlank()) throw BusinessException("请输入音乐URL")
        if (dto.type == MessageType.text && dto.text.isNullOrBlank()) throw BusinessException("请输入回复内容")
        replyService.update(dto.tid, dto.accountId!!, dto.id!!, dto.event, dto.tag, dto.gender, dto.name, dto.keyword, dto.type!!, dto.text, dto.mediaId, dto.mediaName, dto.title, dto.description, dto.musicUrl, dto.hqMusicUrl, dto.pluginCode, dto.pluginConfig)
        return CommonResult()
    }
    @Explain("回复详情")
    @RequiresAuthentication
    @RequestMapping("/get")
    open fun get(user: LoginUser, @Valid dto: GetDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        val reply = replyService.get(dto.tid, dto.accountId!!, dto.id!!)
        return CommonResult(MapConverter(MapConverter.Converter<Reply> { item, map ->
            with(item) {
                map.set("id", id)
                        .set("event", event)
                        .set("tag", tag)
                        .set("gender", gender)
                        .set("name", name)
                        .set("title", title)
                        .set("type", messageType)
                        .set("text", text)
                        .set("mediaId", mediaId)
                        .set("mediaName", mediaName)
                        .set("title", title)
                        .set("description", description)
                        .set("musicUrl", musicUrl)
                        .set("hqMusicUrl", hqMusicUrl)
                        .set("pluginCode", pluginCode)
                        .set("pluginConfig", pluginConfig)
            }
        }).build(reply))
    }
    @Explain("删除回复")
    @RequiresPermissions("wx_reply.update")
    @RequestMapping("/delete")
    open fun delete(user: LoginUser, @Valid dto: DeleteDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        replyService.delete(dto.tid, dto.accountId!!, dto.id!!)
        return CommonResult()
    }

    @Explain(exclude = true)
    @RequiresPermissions("wx_reply.update")
    @RequestMapping("/action/items")
    open fun actionItems(user: LoginUser, @Valid dto: ActionListDto, bindingResult: BindingResult): CommonResult {
        if (bindingResult.hasErrors()) throw IllegalArgumentException(bindingResult)
        if (dto.event == Event.common) return CommonResult(actionsService.actions())

        val eventType = when (dto.event!!) {
            Event.subscribe-> EventType.subscribe
            Event.unsubscribe-> EventType.unsubscribe
            Event.text-> EventType.text
            Event.keyword-> EventType.text
            Event.image-> EventType.image
            Event.voice-> EventType.voice
            Event.video-> EventType.video
            Event.shortvideo-> EventType.shortvideo
            Event.location-> EventType.location
            else-> throw IllegalArgumentException("不支持的事件类型")
        }
        val actions = actionsService.actions(eventType)
        return CommonResult(actions)
    }
}
