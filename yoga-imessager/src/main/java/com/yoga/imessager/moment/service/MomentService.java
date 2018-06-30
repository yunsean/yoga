package com.yoga.imessager.moment.service;

import com.yoga.core.exception.BusinessException;
import com.yoga.core.service.BaseService;
import com.yoga.core.utils.StrUtil;
import com.yoga.imessager.moment.mapper.MomentMapper;
import com.yoga.imessager.moment.model.Moment;
import com.yoga.imessager.moment.repo.MomentRepository;
import com.yoga.imessager.sequence.SequenceNameEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MomentService extends BaseService {

    @Autowired
    private MomentRepository momentRepository;
    @Autowired
    private MomentMapper momentMapper;
    @Autowired
    private MomentFollowService followService;
    @Autowired
    private MomentUpvoteService upvoteService;

    public void add(long tenantId, long creatorId, String content, String[] imageIds, String[] imageUrls, String linkUrl, String linkTitle, String linkPoster, String address, Double longitude, Double latitude, String poiId, String poiTitle) {
        String ids = StrUtil.array2String(imageIds);
        String urls = StrUtil.array2String(imageUrls);
        Moment moment = new Moment(tenantId, creatorId, content, ids, urls, linkUrl, linkTitle, linkPoster, address, longitude, latitude, poiId, poiTitle);
        moment.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_IM_MOMENT_ID));
        momentRepository.save(moment);
    }

    public Moment get(long tenantId, long momentId) {
        Moment moment = momentRepository.findOne(momentId);
        if (moment == null || moment.getTenantId() != tenantId) throw new BusinessException("不存在该消息！");
        return moment;
    }

    public List<Moment> list(long tenantId, Long deptId, Long bigThan, Long smallThan, Integer limitCount, boolean wantFollow) {
        if (limitCount != null && limitCount < 1) limitCount = null;
        List<Moment> moments = momentMapper.findMomentByTenantId(tenantId, deptId, bigThan, smallThan, limitCount);
        if (wantFollow) {
            moments.forEach(moment -> {
                moment.setFollows(followService.list(moment.getId()));
                moment.setUpvotes(upvoteService.list(moment.getId()));
            });
        }
        return moments;
    }
}
