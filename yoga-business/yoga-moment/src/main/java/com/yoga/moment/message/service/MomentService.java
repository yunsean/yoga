package com.yoga.moment.message.service;

import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.core.utils.StringUtil;
import com.yoga.moment.message.mapper.MomentFollowMapper;
import com.yoga.moment.message.mapper.MomentMessageMapper;
import com.yoga.moment.message.mapper.MomentUpvoteMapper;
import com.yoga.moment.message.model.MomentFollow;
import com.yoga.moment.message.model.MomentMessage;
import com.yoga.moment.message.model.MomentUpvote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class MomentService extends BaseService {

    @Autowired
    private MomentMessageMapper messageMapper;
    @Autowired
    private MomentFollowMapper followMapper;
    @Autowired
    private MomentUpvoteMapper upvoteMapper;

    public Long add(long tenantId, long creatorId, Long groupId, String content, String[] imageIds, String[] imageUrls, String[] fileIds, String[] fileNames, String linkUrl, String linkTitle, String linkPoster, String address, Double longitude, Double latitude, String poiId, String poiTitle) {
        String ids = StringUtil.array2String(imageIds);
        String urls = StringUtil.array2String(imageUrls);
        String fids = StringUtil.array2String(fileIds);
        String fnames = StringUtil.array2String(fileNames);
        MomentMessage moment = new MomentMessage(tenantId, creatorId, groupId, content, ids, urls, fids, fnames, linkUrl, linkTitle, linkPoster, address, longitude, latitude, poiId, poiTitle);
        messageMapper.insert(moment);
        return moment.getId();
    }
    public MomentMessage get(long tenantId, long messageId) {
        MomentMessage message = messageMapper.selectByPrimaryKey(messageId);
        if (message == null || message.getTenantId() != tenantId) throw new BusinessException("不存在该消息！");
        return message;
    }
    public List<MomentMessage> list(long tenantId, Long branchId, Long bigThan, Long smallThan, Integer limitCount, boolean wantFollow) {
        if (limitCount != null && limitCount < 1) limitCount = null;
        List<MomentMessage> moments = messageMapper.findMomentByTenantId(tenantId, branchId, bigThan, smallThan, limitCount);
        if (wantFollow) {
            moments.forEach(moment -> {
                moment.setFollows(listFollow(moment.getId()));
                moment.setUpvotes(listUpvote(moment.getId()));
            });
        }
        return moments;
    }
    public Long reply(long tenantId, long messageId, long replierId, String content, Long receiverId) {
        MomentMessage moment = messageMapper.selectByPrimaryKey(messageId);
        if (moment == null || moment.getTenantId() != tenantId) throw new BusinessException("信息流不存在！");
        moment.setUpdatedTime(new Date());
        messageMapper.updateByPrimaryKey(moment);
        MomentFollow follow = new MomentFollow(messageId, moment.getCreatorId(), replierId, content, receiverId);
        followMapper.insert(follow);
        return follow.getId();
    }
    public void upvote(long tenantId, long messageId, long upvoterId) {
        MomentMessage moment = messageMapper.selectByPrimaryKey(messageId);
        if (moment == null || moment.getTenantId() != tenantId) throw new BusinessException("信息流不存在！");
        if (moment.getCreatorId() == upvoterId) throw new BusinessException("不能给自己点赞");
        moment.setUpdatedTime(new Date());
        messageMapper.updateByPrimaryKey(moment);
        MomentUpvote upvote = new MomentUpvote(messageId, upvoterId);
        upvoteMapper.insert(upvote);
    }
    public long getReplyCount(long tenantId, long userId, LocalDateTime after) {
        return new MapperQuery<>(MomentFollow.class)
                .andEqualTo("receiverId", userId)
                .andGreaterThan("replyTime", after, after != null)
                .count(followMapper);
    }
    public long getUpvoteCount(long tenantId, long userId, LocalDateTime after) {
        return upvoteMapper.countUpvoteForUser(userId, after);
    }
    public long getMessageCount(long tenantId, long userId, Long bigThan) {
        return messageMapper.countNewestMessages(tenantId, userId, bigThan);
    }

    public List<MomentFollow> listFollow(long messageId) {
        return followMapper.findFollowByMementId(messageId);
    }
    public List<MomentUpvote> listUpvote(long messageId) {
        return upvoteMapper.findUpvoteByMementId(messageId);
    }
}
