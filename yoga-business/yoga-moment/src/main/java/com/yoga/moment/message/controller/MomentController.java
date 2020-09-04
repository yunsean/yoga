package com.yoga.moment.message.controller;

import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseVo;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.ApiResults;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.NumberUtil;
import com.yoga.core.utils.StringUtil;
import com.yoga.moment.message.dto.MomentIssueDto;
import com.yoga.moment.message.dto.MomentListDto;
import com.yoga.moment.message.dto.MomentReplyDto;
import com.yoga.moment.message.dto.MomentUpvoteDto;
import com.yoga.moment.message.model.MomentMessage;
import com.yoga.moment.message.service.MomentService;
import com.yoga.moment.message.vo.MomentFileVo;
import com.yoga.moment.message.vo.MomentFollowVo;
import com.yoga.moment.message.vo.MomentMessageVo;
import com.yoga.moment.message.vo.MomentUserVo;
import com.yoga.operator.user.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "信息流")
@Controller
@RequestMapping("/admin/moment/message")
public class MomentController extends BaseController {

    @Autowired
    private MomentService momentService;

    @ResponseBody
    @ApiOperation("发表信息流")
    @RequiresAuthentication
    @PostMapping("/issue.json")
    public ApiResult issue(@Valid @ModelAttribute MomentIssueDto dto, BindingResult bindingResult) {
        User user = User.getLoginUser();
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (StringUtil.allBlank(dto.getContent(), dto.getLinkUrl())) throw new BusinessException("请输入发送内容！");
        momentService.add(dto.getTid(), user.getId(), dto.getGroupId(), dto.getContent(), dto.getImageIds(), dto.getImageUrls(), dto.getFileIds(), dto.getFileNames(), dto.getLinkUrl(), dto.getLinkTitle(), dto.getLinkPoster(), dto.getAddress(), dto.getLongitude(), dto.getLatitude(), dto.getPoiId(), dto.getPoiTitle());
        return new ApiResult();
    }
    @ResponseBody
    @ApiOperation("信息流列表")
    @RequiresAuthentication
    @GetMapping("/list.json")
    public ApiResults<MomentMessageVo> list(@ModelAttribute MomentListDto dto) {
        List<MomentMessage> moments = momentService.list(dto.getTid(), dto.getGroupId(), dto.getBigThan(), dto.getSmallThan(), dto.getLimitCount(), dto.isWantFollow());
        return new ApiResults<>(moments, MomentMessageVo.class, (po, vo)-> {
            vo.setFollows(BaseVo.copys(po.getFollows(), MomentFollowVo.class, (po1, vo1)-> {
                vo1.setReceiver(new MomentUserVo(po1.getReceiverId(), po1.getReceiver(), po1.getReceiverAvatar()));
                vo1.setReplier(new MomentUserVo(po1.getReplierId(), po1.getReplier(), po1.getReplierAvatar()));
            }));
            vo.setUpvoters(BaseVo.copys(po.getUpvotes(), MomentUserVo.class, (po2)-> new MomentUserVo(po2.getUpvoterId(), po2.getUpvoter(), po2.getAvater())));
            if (StringUtil.isNotBlank(po.getImageUrls())) vo.setImages(Arrays.stream(StringUtil.string2Array(po.getImageUrls())).collect(Collectors.toList()));
            if (StringUtil.isNotBlank(po.getFileIds())) {
                Long[] fileIds = Arrays.stream(StringUtil.string2Array(po.getFileIds())).map(Long::valueOf).toArray(Long[]::new);
                String[] fileNames = StringUtil.string2Array(po.getFileNames());
                List<MomentFileVo> files = new ArrayList<>();
                for (int i = 0; i < fileIds.length && i < fileNames.length; i++) {
                    files.add(new MomentFileVo(fileIds[i], fileNames[i]));
                }
                vo.setFiles(files);
            }
        });
    }
    @ResponseBody
    @ApiOperation("回复信息流")
    @RequiresAuthentication
    @PostMapping("/reply.json")
    public ApiResult reply(@Valid @ModelAttribute MomentReplyDto dto, BindingResult bindingResult) {
        User user = User.getLoginUser();
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        momentService.reply(dto.getTid(), dto.getMessageId(), user.getId(), dto.getContent(), dto.getReceiverId());
        return new ApiResult();
    }
    @ResponseBody
    @ApiOperation("点赞信息流.json")
    @RequiresAuthentication
    @PostMapping("/upvote.json")
    public ApiResult upvote(@Valid @ModelAttribute MomentUpvoteDto dto, BindingResult bindingResult) {
        User user = User.getLoginUser();
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        momentService.upvote(dto.getTid(), dto.getMessageId(), user.getId());
        return new ApiResult();
    }
}
