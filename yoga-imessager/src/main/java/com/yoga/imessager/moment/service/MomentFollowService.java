package com.yoga.imessager.moment.service;

import com.yoga.core.service.BaseService;
import com.yoga.core.utils.StrUtil;
import com.yoga.imessager.moment.mapper.MomentMapper;
import com.yoga.imessager.moment.model.Moment;
import com.yoga.imessager.moment.model.MomentFollow;
import com.yoga.imessager.moment.model.MomentUpvote;
import com.yoga.imessager.moment.repo.MomentFollowRepository;
import com.yoga.imessager.moment.repo.MomentRepository;
import com.yoga.imessager.moment.repo.MomentUpvoteRepository;
import com.yoga.imessager.sequence.SequenceNameEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MomentFollowService extends BaseService {

    @Autowired
    private MomentService momentService;
    @Autowired
    private MomentFollowRepository momentFollowRepository;
    @Autowired
    private MomentMapper momentMapper;

    public void reply(long tenantId, long momentId, long replierId, String content, Long receiverId) {
        Moment moment = momentService.get(tenantId, momentId);
        MomentFollow follow = new MomentFollow(momentId, moment.getCreatorId(), replierId, content, receiverId);
        follow.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_IM_MOMENT_FOLLOW_ID));
        momentFollowRepository.save(follow);
    }
    public List<MomentFollow> list(long momentId) {
        return momentMapper.findFollowByMementId(momentId);
    }
}
