package com.yoga.core.behavior.service;


import com.yoga.core.behavior.model.Behavior;
import com.yoga.core.behavior.repo.BehaviorRepository;
import com.yoga.core.data.PageList;
import com.yoga.core.sequence.SequenceNameEnum;
import com.yoga.core.sequence.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.List;

@Service
public class BehaviorService {

    @Autowired
    private BehaviorRepository behaviorRepository;
    @Autowired
    protected SequenceService sequenceService;

    public void add(long tenantId, String actor, String action, long operatorId, String operator, String remark) {
        Behavior behavior = new Behavior(tenantId, actor, action, operatorId, operator, remark);
        behavior.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_LOG_BEHAVIOR_ID));
        behaviorRepository.save(behavior);
    }

    public PageList<Behavior> find(long tenantId, Long operatorId, String operator, String actor, String action, Date begin, Date end, int pageIndex, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "date");
        Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
        return new PageList<>(behaviorRepository.findAll((root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            List<Expression<Boolean>> expressions = predicate.getExpressions();
            expressions.add(cb.equal(root.get("tenantId"), tenantId));
            if (operatorId != null && operatorId != 0) expressions.add(cb.equal(root.get("operatorId"), operatorId));
            if (operator != null && operator.length() > 0) expressions.add(cb.equal(root.get("operator"), operator));
            if (actor != null && actor.length() > 0) expressions.add(cb.like(root.get("actor"), "%" + actor + "%"));
            if (action != null && action.length() > 0) expressions.add(cb.equal(root.get("action"), action));
            if (begin != null) expressions.add(cb.greaterThanOrEqualTo(root.get("date"), begin));
            if (end != null) expressions.add(cb.lessThanOrEqualTo(root.get("date"), end));
            return predicate;
        }, pageable));
    }
}
