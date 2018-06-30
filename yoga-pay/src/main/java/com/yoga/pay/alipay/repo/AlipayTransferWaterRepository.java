package com.yoga.pay.alipay.repo;

import com.yoga.core.repository.BaseRepository;
import com.yoga.pay.alipay.model.AlipayTransferWater;
import com.yoga.pay.alipay.model.AlipayWater;
import org.springframework.stereotype.Repository;

@Repository
public interface AlipayTransferWaterRepository extends BaseRepository<AlipayTransferWater, Long> {

}
