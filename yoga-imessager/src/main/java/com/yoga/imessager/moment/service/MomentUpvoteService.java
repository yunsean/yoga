package com.yoga.imessager.moment.service;

import com.yoga.core.exception.BusinessException;
import com.yoga.core.service.BaseService;
import com.yoga.imessager.moment.mapper.MomentMapper;
import com.yoga.imessager.moment.model.Moment;
import com.yoga.imessager.moment.model.MomentFollow;
import com.yoga.imessager.moment.model.MomentUpvote;
import com.yoga.imessager.moment.repo.MomentFollowRepository;
import com.yoga.imessager.moment.repo.MomentUpvoteRepository;
import com.yoga.imessager.sequence.SequenceNameEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MomentUpvoteService extends BaseService {

    @Autowired
    private MomentService momentService;
    @Autowired
    private MomentUpvoteRepository momentUpvoteRepository;
    @Autowired
    private MomentMapper momentMapper;

    public void upvote(long tenantId, long momentId, long upvoterId) {
        Moment moment = momentService.get(tenantId, momentId);
        if (moment.getCreatorId() == upvoterId) throw new BusinessException("不能给自己点赞");
        MomentUpvote upvote = new MomentUpvote(momentId, upvoterId);
        momentUpvoteRepository.save(upvote);
    }
    public List<MomentUpvote> list(long momentId) {
        return momentMapper.findUpvoteByMementId(momentId);
    }
}
