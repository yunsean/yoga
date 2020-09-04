package com.yoga.content.comment.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yoga.content.comment.ao.CommentCount;
import com.yoga.content.comment.ao.CommentUpvoteCount;
import com.yoga.content.comment.mapper.CommentMapper;
import com.yoga.content.comment.mapper.CommentUpvoterMapper;
import com.yoga.content.comment.model.Comment;
import com.yoga.content.comment.model.CommentUpvote;
import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.setting.service.SettingService;
import com.yoga.utility.quartz.QuartzService;
import com.yoga.utility.quartz.QuartzTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service("cmsCommentService")
public class CommentService extends BaseService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SettingService settingService;
    @Autowired
    private QuartzService quartzService;
    @Autowired
    private CommentUpvoterMapper commentUpvoterMapper;

    public final static String ModuleName = "cms_comment";
    public final static String KeyName_NeedAudit = "needAudit";

    @PostConstruct
    public void addQuartz() {
        quartzService.add(new QuartzTask(UpdateCountJob.class, ModuleName, "更新文章关注数量", "0 */5 * * * ?"));
    }

    public long add(long tenantId, String articleId, Long commentId, Long replierId, String replierName, Long receiverId, String content, String articleTitle) {
        if (new MapperQuery<>(Comment.class)
            .andEqualTo("replierId", replierId)
            .andEqualTo("articleId", articleId)
            .andGreaterThan("issueTime", LocalDateTime.now().minusSeconds(10))
            .count(commentMapper) > 0) throw new BusinessException("发表评论不能太频繁，10S后再试！");
        Comment comment = new Comment(tenantId, articleId, articleTitle, commentId, replierId, replierName, receiverId, content, !isNeedAudit(tenantId));
        commentMapper.insertSelective(comment);
        return comment.getId();
    }
    public void delete(long tenantId, long id) {
        Comment comment = commentMapper.selectByPrimaryKey(id);
        if (comment == null || comment.getTenantId() != tenantId) throw new BusinessException("评论不存在！");
        commentMapper.deleteByPrimaryKey(id);
    }
    public void issue(long tenantId, long id, boolean issued) {
        Comment comment = commentMapper.selectByPrimaryKey(id);
        if (comment == null || comment.getTenantId() != tenantId) throw new BusinessException("评论不存在！");
        comment.setIssued(issued);
        comment = new Comment(id, issued);
        commentMapper.updateByPrimaryKeySelective(comment);
    }
    public void issues(long tenantId, List<Long> ids, boolean issued) {
        for (Long id : ids) {
            Comment comment = new Comment(id, issued);
            commentMapper.updateByPrimaryKeySelective(comment);
        }
    }
    //@Cacheable(value = CommentService.ModuleName, keyGenerator = "wiselyKeyGenerator")
    public PageInfo<Comment> list(long tenantId, String articleId, Boolean issued, Long replierId, int replyLimit, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex + 1, pageSize);
        List<Comment> comments = commentMapper.list(tenantId, articleId, issued, replierId, replyLimit);
        PageInfo<Comment> result = new PageInfo<>(comments);
        if (replyLimit > 0) {
            List<Long> ids = comments.stream().map(Comment::getReplyIds).filter(it-> it != null && !it.isEmpty()).flatMap(it-> it.stream()).distinct().collect(Collectors.toList());
            List<Comment> replys = new MapperQuery<>(Comment.class)
                    .andIn("id", ids)
                    .query(commentMapper);
            Map<Long, Comment> replyMap = replys.stream().collect(Collectors.toMap(Comment::getId, it-> it));
            comments.stream().filter(it-> it.getReplyIds() != null && !it.getReplyIds().isEmpty()).forEach(it->
                    it.setReplies(it.getReplyIds().stream().map(replyMap::get).filter(it2-> it2 != null).collect(Collectors.toList()))
            );
        }
        return result;
    }
    public PageInfo<Comment> listForAudit(long tenantId, String filter, Boolean issued, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex + 1, pageSize);
        List<Comment> comments = commentMapper.listForAduit(tenantId, filter, issued);
        return new PageInfo<>(comments);
    }
    @Cacheable(value = CommentService.ModuleName, keyGenerator = "wiselyKeyGenerator")
    public List<CommentCount> commentCountTop(long tenantId, int count) {
        return commentMapper.commentCountTop(tenantId, count);
    }
    @Cacheable(value = CommentService.ModuleName, keyGenerator = "wiselyKeyGenerator")
    public Comment get(long tenantId, long id, int replyLimit) {
        Comment comment = commentMapper.get(id, replyLimit);
        if (comment == null || comment.getTenantId() != tenantId) throw new BusinessException("评论不存在！");
        return comment;
    }
    @Cacheable(value = CommentService.ModuleName, keyGenerator = "wiselyKeyGenerator")
    public CommentUpvoteCount getCommentCount(long tenantId, String articleId) {
        return commentMapper.getCount(articleId);
    }
    public PageInfo<Comment> listReply(long tenantId, long id, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex + 1, pageSize);
        List<Comment> comments = commentMapper.listReply(id);
        return new PageInfo<>(comments);
    }

    @Transactional
    public boolean upvote(long tenantId, Long commentId, Long upvoterId) {
        if (new MapperQuery<>(CommentUpvote.class)
                .andEqualTo("commentId", commentId)
                .andEqualTo("upvoterId", upvoterId)
                .count(commentUpvoterMapper) > 0) return false;
        CommentUpvote upvote = new CommentUpvote(commentId, upvoterId);
        commentUpvoterMapper.insertSelective(upvote);
        commentMapper.increaceUpvote(commentId);
        return true;
    }

    public boolean isNeedAudit(long tenantId) {
        return settingService.get(tenantId, ModuleName, KeyName_NeedAudit, false);
    }
}
