package com.yoga.ewedding.recharge.service;


import com.yoga.core.data.PageList;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.property.PropertiesService;
import com.yoga.core.redis.lock.AquiredLockWorker;
import com.yoga.core.redis.lock.RedisLocker;
import com.yoga.core.service.BaseService;
import com.yoga.core.utils.DateUtil;
import com.yoga.core.utils.StrUtil;
import com.yoga.ewedding.counselor.model.Counselor;
import com.yoga.ewedding.counselor.repo.CounselorRepository;
import com.yoga.ewedding.recharge.enums.RechargeStatus;
import com.yoga.ewedding.recharge.enums.RechargeType;
import com.yoga.ewedding.recharge.mapper.EwRechargeMapper;
import com.yoga.ewedding.recharge.model.Charge;
import com.yoga.ewedding.recharge.model.Order;
import com.yoga.ewedding.recharge.model.Recharge;
import com.yoga.ewedding.recharge.repo.RechargeRepository;
import com.yoga.ewedding.sequence.SequenceNameEnum;
import com.yoga.pay.alipay.service.AlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("eweddingRechargeService")
public class RechargeService extends BaseService {

    private final static String RedisLock_Prefix = "ewRechargeService:";

    @Autowired
    private RechargeRepository rechargeRepository;
    @Autowired
    private AlipayService alipayService;
    @Autowired
    private ChargeService chargeService;
    @Autowired
    private PropertiesService propertiesService;
    @Autowired
    private EwRechargeMapper rechargeMapper;
    @Autowired
    private CounselorRepository counselorRepository;
    @Autowired
    private RedisLocker redisLocker;

    public PageList<Order> find(long tenantId, Long userId, RechargeType type, RechargeStatus status, String user, Date begin, Date end, int pageIndex, int pageSize) {
        int startIndex = pageIndex * pageSize;
        int totalCount = rechargeMapper.countBy(tenantId, userId, type, status, user, begin, end);
        if (startIndex >= totalCount) return new PageList<>(null, pageIndex, pageSize, totalCount);
        List<Order> orders = rechargeMapper.findBy(tenantId, userId, type, status, user, begin, end, startIndex, pageSize);
        return new PageList<>(orders, pageIndex, pageSize, totalCount);
    }

    public String alipay(long tenantId, long typeId, long userId, RechargeType type, Date from, Date to, Double amount) {
        Charge charge = chargeService.get(tenantId, typeId);
        if (charge == null) throw new BusinessException("暂不支持充值！");
        autoClose(tenantId, userId);

        double should = 0;
        String content = "";
        switch (type) {
            case monthly:
                should = charge.getMonthlyFee();
                content = "月交";
                break;
            case quarterly:
                should = charge.getQuarterlyFee();
                content = "季交";
                break;
            case halfyear:
                should = charge.getQuarterlyFee();
                content = "半年交";
                break;
            case yearly:
                should = charge.getYearlyFee();
                content = "年交";
                break;
        }
        content += "：" + DateUtil.formatDateShort(from) + "-" + DateUtil.formatDateShort(to);
        if (amount != null && Math.abs(amount - should) > 0.1) throw new BusinessException("充值价格发生变化，请重新选择！");
        if (should < 0.01) return null;
        long id = sequenceService.getNextValue(SequenceNameEnum.SEQ_EW_RECHARGE_ID);
        String orderNo = "1" + DateUtil.formatDate(new Date(), "yyyyMMddHHmmss") + String.format("%08d", userId) + String.format("%08d", id);
        Recharge recharge = new Recharge(tenantId, userId, should, orderNo, to, type);
        recharge.setId(id);
        rechargeRepository.save(recharge);
        String notifyUrl = propertiesService.getSysBaseurl() + "/api/ewedding/charge/alipay/paied/" + tenantId;
        String result = alipayService.sign(tenantId, orderNo, should, "1d", notifyUrl, String.valueOf(id), "服务费充值", content);
        return result;
    }
    private void autoClose(long tenantId, long userId) {
        String notifyUrl = propertiesService.getSysBaseurl() + "/api/ewedding/charge/alipay/closed/" + tenantId;
        List<Recharge> recharges = rechargeRepository.findByTenantIdAndUserIdAndStatus(tenantId, userId, RechargeStatus.pay);
        recharges.stream().forEach(recharge -> {
            try {
                alipayService.close(tenantId, "SYSTEM", recharge.getOrderNo(), notifyUrl);
            } catch (Exception ex) {
            }
            recharge.setStatus(RechargeStatus.closed);
        });
        rechargeRepository.save(recharges);
    }

    @Transactional
    public void paied(long tenantId, Map<String, String> message) {
        String bizResult = null;
        String orderNo = message.get("out_trade_no");
        String tradeNo = message.get("trade_no");
        boolean rsaChecked = alipayService.checkCallback(tenantId, message);
        if (!rsaChecked) {
            bizResult = "验签失败！";
        } else if (StrUtil.isBlank(orderNo)) {
            bizResult = "无效的订单号！";
        } else {
            try {
                bizResult = redisLocker.lock(RedisLock_Prefix + orderNo, new AquiredLockWorker<String>() {
                    @Override
                    public String invokeAfterLockAquire() throws Exception {
                        Recharge recharge = rechargeRepository.findByOrderNo(orderNo);
                        if (recharge == null) return "无效的订单号！";
                        if (recharge.getStatus() == RechargeStatus.paied && !StrUtil.isEqual(recharge.getTradeNo(), tradeNo))
                            return "订单已支付，但订单交易号不符，可能重复支付！";
                        Counselor counselor = counselorRepository.findOne(recharge.getUserId());
                        if (counselor == null) return "未找到该顾问！";
                        if (recharge.getStatus() != RechargeStatus.paied) {
                            setExpire(counselor, recharge.getRechargeType());
                            recharge.setTradeNo(tradeNo);
                            recharge.setStatus(RechargeStatus.paied);
                            rechargeRepository.save(recharge);
                        }
                        return null;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                bizResult = e.getMessage();
            }
        }
        alipayService.saveWater(tenantId, message, rsaChecked, bizResult);
    }

    private void setExpire(Counselor counselor, RechargeType type) {
        Calendar expire = Calendar.getInstance();
        if (counselor.getExpire() != null) expire.setTime(counselor.getExpire());
        switch (type) {
            case monthly:
                expire.add(Calendar.MONTH, 1);
                break;
            case quarterly:
                expire.add(Calendar.MONTH, 3);
                break;
            case halfyear:
                expire.add(Calendar.MONTH, 6);
                break;
            case yearly:
                expire.add(Calendar.MONTH, 12);
                break;
        }
        counselor.setExpire(expire.getTime());
        counselorRepository.save(counselor);
    }
}
