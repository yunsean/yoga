package com.yoga.tenant.recharge.service;

import com.yoga.core.data.PageList;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.service.BaseService;
import com.yoga.core.utils.StrUtil;
import com.yoga.tenant.recharge.model.Recharge;
import com.yoga.tenant.recharge.repo.RechargeRepository;
import com.yoga.tenant.tenant.cache.TenantCache;
import com.yoga.tenant.tenant.model.Tenant;
import com.yoga.tenant.tenant.repo.TenantRepository;
import com.yoga.user.sequence.SequenceNameEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service("tenantRechargeService")
public class RechargeService extends BaseService {

	@Autowired
	private RechargeRepository rechargeRepository;
	@Autowired
	private TenantRepository tenantRepository;
	@Autowired
	private TenantCache tenantCache;

	@Transactional
	public void add(long tenantId, long operatorId, String refereeA, String refereeB, BigDecimal amount, Date expireDate, boolean invoiced, String invoiceNo, String orderNo, String tradeNo, String remark) {
		Tenant tenant = tenantRepository.findOne(tenantId);
		if (tenant == null) throw new BusinessException("租户不存在！");
		boolean isExpired = tenant.getExpireDate() == null || tenant.getExpireDate().before(new Date());
		tenant.setExpireDate(expireDate);
		tenantRepository.save(tenant);

		Recharge recharge = new Recharge(tenantId, tenant.getName(), operatorId, refereeA, refereeB, amount, expireDate, invoiced, invoiceNo, orderNo, tradeNo, remark);
		recharge.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_G_RECHARGE_ID));
		rechargeRepository.save(recharge);

		tenantCache.clearTenant(tenantId);
		if (isExpired) tenantCache.clearAll();
	}

	public void invoiced(long id, String invoiceNo, String orderNo, String tradeNo, String remark) {
		Recharge recharge = rechargeRepository.findOne(id);
		if (recharge == null) throw new BusinessException("续费记录不存在！");
		recharge.setInvoiced(true);
		recharge.setInvoiceNo(invoiceNo);
		recharge.setOrderNo(orderNo);
		recharge.setTradeNo(tradeNo);
		recharge.setRemark(remark);
		rechargeRepository.save(recharge);
	}

	public PageList<Recharge> list(Long tenantId, String tenantName, String referee, Date beginTime, Date endTime, Boolean invoiced, String number, int pageIndex, int pageSize) {
		Sort sort = new Sort(Sort.Direction.DESC, "id");
		PageRequest request = new PageRequest(pageIndex, pageSize, sort);
		return new PageList<Recharge>(rechargeRepository.findAll(new Specification<Recharge>() {
			@Override
			public Predicate toPredicate(Root<Recharge> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();
				if (tenantId != null) expressions.add(cb.equal(root.get("tenantId"), tenantId));
				if (StrUtil.isNotBlank(tenantName)) expressions.add(cb.like(root.get("tenantName"), "%" + tenantName + "%"));
				if (StrUtil.isNotBlank(referee)) expressions.add(cb.or(
						cb.like(root.get("refereeA"), "%" + referee + "%"),
						cb.like(root.get("refereeB"), "%" + referee + "%")));
				if (beginTime != null) expressions.add(cb.greaterThanOrEqualTo(root.get("time"), beginTime));
				if (endTime != null) expressions.add(cb.lessThanOrEqualTo(root.get("time"), endTime));
				if (invoiced != null) expressions.add(cb.equal(root.get("invoiced"), invoiced));
				if (StrUtil.isNotBlank(number)) expressions.add(cb.or(
						cb.equal(root.get("invoiceNo"), number),
						cb.equal(root.get("orderNo"), number),
						cb.equal(root.get("tradeNo"), number)));
				return predicate;
			}
		}, request));
	}

	public final static String ModuleName = "gbl_tenant_recharge";
}
