package com.yoga.content.comment.controller;

import com.github.pagehelper.PageInfo;
import com.yoga.content.comment.dto.IssueDto;
import com.yoga.content.comment.dto.IssuesDto;
import com.yoga.content.comment.dto.ListDto;
import com.yoga.content.comment.model.Comment;
import com.yoga.content.comment.service.CommentService;
import com.yoga.content.template.dto.DeleteDto;
import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.CommonPage;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.setting.annotation.Settable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@ApiIgnore
@Controller("cmsCommentController")
@RequestMapping("/admin/cms/comment")
@Api(tags = "CMS评论管理")
@Settable(module = CommentService.ModuleName, key = CommentService.KeyName_NeedAudit, name = "评论管理-评论需要审核", type = boolean.class, defaultValue = "false")
public class CommentController extends BaseController {

    @Autowired
    private CommentService commentService;

    @ApiIgnore
    @RequiresPermissions("cms_comment")
    @RequestMapping("")
    public String list(ModelMap model, CommonPage page, @Valid ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<Comment> comments = commentService.listForAudit(dto.getTid(), dto.getFilter(), dto.getIssued(), page.getPageIndex(), page.getPageSize());
        model.put("comments", comments.getList());
        model.put("page", new CommonPage(comments));
        model.put("param", dto.wrapAsMap());
        return "/admin/cms/comment/list";
    }

    @ResponseBody
    @ApiOperation("删除评论")
    @DeleteMapping("/delete.json")
    @RequiresPermissions("cms_comment.audit")
    public ApiResult delete(@Valid DeleteDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        commentService.delete(dto.getTid(), dto.getId());
        return new ApiResult();
    }
    @ResponseBody
    @ApiOperation("发布评论")
    @PostMapping("/issue.json")
    @RequiresPermissions("cms_comment.audit")
    public ApiResult issue(@Valid IssueDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        commentService.issue(dto.getTid(), dto.getId(), dto.getIssued());
        return new ApiResult();
    }
    @ResponseBody
    @ApiOperation("批量发布评论")
    @PostMapping("/issues.json")
    @RequiresPermissions("cms_comment.audit")
    public ApiResult issues(@RequestBody @Valid IssuesDto body, BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        commentService.issues(dto.getTid(), body.getIds(), body.getIssued());
        return new ApiResult();
    }
}
