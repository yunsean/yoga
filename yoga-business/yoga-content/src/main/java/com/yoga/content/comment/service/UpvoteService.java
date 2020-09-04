package com.yoga.content.comment.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yoga.content.comment.ao.FavoriteCount;
import com.yoga.content.comment.ao.UpvoteCount;
import com.yoga.content.comment.mapper.UpvoteMapper;
import com.yoga.content.comment.model.Comment;
import com.yoga.content.comment.model.Upvote;
import com.yoga.core.base.BaseService;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.core.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("cmsUpvoteService")
public class UpvoteService extends BaseService {

    @Autowired
    private UpvoteMapper upvoteMapper;

    public long add(long tenantId, String articleId, Long replierId) {
        Upvote upvote = new MapperQuery<>(Upvote.class)
            .andEqualTo("upvoterId", replierId)
            .andEqualTo("articleId", articleId)
            .queryFirst(upvoteMapper);
        if (upvote != null) return upvote.getId();
        upvote = new Upvote(tenantId, articleId, replierId);
        upvoteMapper.insert(upvote);
        return upvote.getId();
    }
    public boolean is(long tenantId, String articleId, Long replierId) {
        return new MapperQuery<>(Upvote.class)
                .andEqualTo("upvoterId", replierId)
                .andEqualTo("articleId", articleId)
                .count(upvoteMapper) > 0;
    }
    public long count(long tenantId, String articleId) {
        return new MapperQuery<>(Upvote.class)
                .andEqualTo("articleId", articleId)
                .count(upvoteMapper);
    }
    public PageInfo<Upvote> list(long tenantId, String articleId, Long replierId, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex + 1, pageSize);
        List<Upvote> upvotes = new MapperQuery<>(Upvote.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("articleId", articleId, StringUtil.isNotBlank(articleId))
                .andEqualTo("upvoterId", replierId, replierId != null)
                .query(upvoteMapper);
        return new PageInfo<>(upvotes);
    }
}
