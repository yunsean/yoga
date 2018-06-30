package com.yoga.ewedding.recharge.service;


import com.yoga.core.exception.BusinessException;
import com.yoga.core.service.BaseService;
import com.yoga.ewedding.recharge.mapper.ChargeMapper;
import com.yoga.ewedding.recharge.model.Charge;
import com.yoga.ewedding.recharge.repo.ChargeRepository;
import com.yoga.ewedding.sequence.SequenceNameEnum;
import com.yoga.utility.journal.service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("eweddingServiceFeeService")
public class ChargeService extends BaseService {

    @Autowired
    private ChargeRepository chargeRepository;
    @Autowired
    private ChargeMapper chargeMapper;

    public void set(long tenantId, long typeId, double originalFee, double monthlyFee, double quarterlyFee, double halfyearFee, double yearlyFee) {
        Charge charge = chargeRepository.findOneByTenantIdAndTypeId(tenantId, typeId);
        if (charge == null) {
            charge = new Charge(tenantId, typeId, originalFee, monthlyFee, quarterlyFee, halfyearFee, yearlyFee);
            charge.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_EW_CHARGE_ID));
        } else {
            charge.setOriginalFee(originalFee);
            charge.setMonthlyFee(monthlyFee);
            charge.setQuarterlyFee(quarterlyFee);
            charge.setHalfyearFee(halfyearFee);
            charge.setYearlyFee(yearlyFee);
        }
        JournalService.add(tenantId, "ew_charge", "setCharge", "调整费率为：", typeId, originalFee, monthlyFee, quarterlyFee, halfyearFee, yearlyFee);
        chargeRepository.save(charge);
    }
    public Charge get(long tenantId, long typeId) {
        Charge charge = chargeRepository.findOneByTenantIdAndTypeId(tenantId, typeId);
        if (charge == null) throw new BusinessException("不支持此种收费方式！");
        return charge;
    }

    public List<Charge> list(long tenantId, long parentId) {
        List<Charge> charges = chargeMapper.findByTenantIdAndParentId(tenantId, parentId);
        return charges;
    }
}
