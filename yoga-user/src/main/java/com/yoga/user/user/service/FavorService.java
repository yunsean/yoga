package com.yoga.user.user.service;

import com.yoga.core.data.BaseEnum;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.service.BaseService;
import com.yoga.user.sequence.SequenceNameEnum;
import com.yoga.user.user.model.Favor;
import com.yoga.user.user.repo.FavorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;

@Service
public class FavorService extends BaseService {

	@Autowired
	private FavorRepository favorRepository = null;

	public void addFavor(long tenantId, long userId, BaseEnum<Integer> type, String objectId, String title, String param1, Long param2, String param3, String param4, String param5) {
		Date now = new Date();
		Favor favor = new Favor(tenantId, userId, type.getCode(), objectId, now, title, param1, param2, param3, param4, param5);
		favor.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_S_FAVOR_ID));
		favorRepository.save(favor);
	}
	public void addFavor2(long tenantId, long userId, int type, long objectId, String title, String param1, Long param2, String param3, String param4, String param5) {
		Date now = new Date();
		Favor favor = new Favor(tenantId, userId, type, objectId, now, title, param1, param2, param3, param4, param5);
		favor.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_S_FAVOR_ID));
		favorRepository.save(favor);
	}
	public void delFavor(long favorId) {
		long count = favorRepository.countById(favorId);
		if (count < 1) throw new BusinessException("未找到该收藏！");
		favorRepository.delete(favorId);
	}
	public void delFavorByUser(long userId) {
		favorRepository.deleteByUserId(userId);
	}
	public void delFavorByObject(BaseEnum<Integer> type, String objectId) {
		favorRepository.deleteByTypeAndObjectId(type.getCode(), objectId);
	}
	public void delFavorByObject(BaseEnum<Integer> type, long objectId) {
		String sid = String.format("%d", objectId);
		delFavorByObject(type, sid);
	}
	public Page<Favor> findFavor(long tenantId, Long userId, BaseEnum<Integer> type, String objectId, String title, String filter, Date begin, Date end, int pageIndex, int pageSize) {
		Sort sort = new Sort(Sort.Direction.DESC, "favorDate");
		Pageable request = new PageRequest(pageIndex, pageSize, sort);
		return favorRepository.findAll(new Specification<Favor>() {			
			@Override
			public Predicate toPredicate(Root<Favor> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();
				expressions.add(cb.equal(root.get("tenantId"), tenantId));
				if (userId != null) expressions.add(cb.equal(root.get("userId"), userId));
				if (type != null) expressions.add(cb.equal(root.get("type"), type.getCode()));
				if (objectId != null) expressions.add(cb.equal(root.get("objectId"), objectId));
				if (begin != null) expressions.add(cb.greaterThanOrEqualTo(root.get("favorDate"), begin));
				if (end != null) expressions.add(cb.lessThanOrEqualTo(root.get("favorDate"), end));
				if (title != null) expressions.add(cb.like(root.get("title"), "%" + title + "%"));
				if (filter != null) expressions.add(cb.or(cb.like(root.get("title"), "%" + filter + "%"),
						cb.like(root.get("param1"), "%" + filter + "%"),
						cb.like(root.get("param3"), "%" + filter + "%"),
						cb.like(root.get("param4"), "%" + filter + "%"),
						cb.like(root.get("param5"), "%" + filter + "%")));
				return predicate;
			}
		}, request);
	}
	public Page<Favor> findFavor(long tenantId, Long userId, BaseEnum<Integer> type, Long objectId, Date begin, Date end, int pageIndex, int pageSize) {
		String sid = null;
		if (objectId != null) sid = String.format("%d", objectId);
		return findFavor(tenantId, userId, type, sid, null, null, begin, end, pageIndex, pageSize);
	}
	public Page<Favor> findFavor(long tenantId, Long userId, BaseEnum<Integer> type, int pageIndex, int pageSize) {
		return findFavor(tenantId, userId, type, null, null, null, null, null, pageIndex, pageSize);
	}
	public boolean isFavor(Long userId, BaseEnum<Integer> type, String objectId) {
		long count = favorRepository.countByUserIdAndTypeAndObjectId(userId, type.getCode(), objectId);
		return count > 0;
	}
}
