package com.yoga.imessager.moment.controller;

import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.MapConverter;
import com.yoga.core.utils.StrUtil;
import com.yoga.imessager.moment.dto.IssueDto;
import com.yoga.imessager.moment.dto.ListDto;
import com.yoga.imessager.moment.dto.ReplyDto;
import com.yoga.imessager.moment.dto.UpvoteDto;
import com.yoga.imessager.moment.model.Moment;
import com.yoga.imessager.moment.model.MomentFollow;
import com.yoga.imessager.moment.model.MomentUpvote;
import com.yoga.imessager.moment.service.MomentFollowService;
import com.yoga.imessager.moment.service.MomentService;
import com.yoga.imessager.moment.service.MomentUpvoteService;
import com.yoga.user.model.LoginUser;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Explain(value = "IM朋友圈", module = "gcf_imessager")
@RestController
@RequestMapping("/api/im/moment")
public class MomentApiController extends BaseApiController {

    @Autowired
    private MomentService momentService;
    @Autowired
    private MomentFollowService followService;
    @Autowired
    private MomentUpvoteService upvoteService;

    @Explain("发表朋友圈")
    @RequiresAuthentication
    @RequestMapping("/issue")
    public CommonResult issue(LoginUser login, @Valid IssueDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (StrUtil.allBlank(dto.getContent(), dto.getLinkUrl())) throw new BusinessException("请输入发送内容！");
        momentService.add(dto.getTid(), login.getId(), dto.getContent(), dto.getImageIds(), dto.getImageUrls(), dto.getLinkUrl(), dto.getLinkTitle(), dto.getLinkPoster(), dto.getAddress(), dto.getLongitude(), dto.getLatitude(), dto.getPoiId(), dto.getPoiTitle());
        return new CommonResult();
    }

    @Explain("朋友圈列表")
    @RequiresAuthentication
    @RequestMapping("/list")
    public CommonResult list(LoginUser login, ListDto dto) {
        List<Moment> moments = momentService.list(dto.getTid(), dto.getDeptId(), dto.getBigThan(), dto.getSmallThan(), dto.getLimitCount(), dto.isWantFollow());
        return new CommonResult(new MapConverter<Moment>(new MapConverter.Converter<Moment>() {
            @Override
            public void convert(Moment item, MapConverter.MapItem<String, Object> map) {
                map.set("id", item.getId())
                        .set("creatorId", item.getCreatorId())
                        .set("creator", item.getCreator())
                        .set("avatar", item.getAvatar())
                        .set("issueTime", item.getIssueTime())
                        .set("content", item.getContent())
                        .set("linkUrl", item.getLinkUrl())
                        .set("linkTitle", item.getLinkTitle())
                        .set("linkPoster", item.getLinkPoster())
                        .set("address", item.getAddress())
                        .set("latitude", item.getLatitude())
                        .set("longitude", item.getLongitude())
                        .set("poiId", item.getPoiId())
                        .set("poiTitle", item.getPoiTitle())
                        .set("images", item.getImageUrlList())
                        .set("follows", new MapConverter<MomentFollow>(new MapConverter.Converter<MomentFollow>() {
                            @Override
                            public void convert(MomentFollow item, MapConverter.MapItem<String, Object> map) {
                                map.set("id", item.getId())
                                        .set("repliery", new MapConverter.MapItem<String, Object>()
                                                .set("id", item.getReplierId())
                                                .set("name", item.getReplier())
                                                .set("avatar", item.getReceiverAvatar()))
                                        .set("receiver", (item.getReceiverId() == null || item.getReceiverId() == 0L) ? null : new MapConverter.MapItem<String, Object>()
                                                .set("id", item.getReceiverId())
                                                .set("name", item.getReceiver())
                                                .set("avatar", item.getReceiverAvatar()))
                                        .set("content", item.getContent());
                            }
                        }).build(item.getFollows()))
                        .set("upvotes", new MapConverter<MomentUpvote>(new MapConverter.Converter<MomentUpvote>() {
                            @Override
                            public void convert(MomentUpvote item, MapConverter.MapItem<String, Object> map) {
                                map.set("id", item.getUpvoterId())
                                        .set("name", item.getUpvoter())
                                        .set("avatar", item.getUpvoterAvater());
                            }
                        }).build(item.getUpvotes()));
            }
        }).build(moments));
    }

    @Explain("回复朋友圈")
    @RequiresAuthentication
    @RequestMapping("/reply")
    public CommonResult reply(LoginUser login, @Valid ReplyDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        followService.reply(dto.getTid(), dto.getMomentId(), login.getId(), dto.getContent(), dto.getReceiverId());
        return new CommonResult();
    }

    @Explain("点赞朋友圈")
    @RequiresAuthentication
    @RequestMapping("/upvote")
    public CommonResult upvote(LoginUser login, @Valid UpvoteDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        upvoteService.upvote(dto.getTid(), dto.getMomentId(), login.getId());
        return new CommonResult();
    }
}
