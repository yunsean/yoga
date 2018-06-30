package com.yoga.pay.wxpay.service;

import com.alibaba.fastjson.JSON;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.sequence.SequenceService;
import com.yoga.core.utils.StrUtil;
import com.yoga.pay.alipay.enums.WaterType;
import com.yoga.pay.sequence.SequenceNameEnum;
import com.yoga.pay.wxpay.model.WxpayParam;
import com.yoga.pay.wxpay.model.WxpayWater;
import com.yoga.pay.wxpay.repo.WxpayWaterRepository;
import com.yoga.pay.wxpay.sdk.WXPay;
import com.yoga.pay.wxpay.sdk.WXPayConstants;
import com.yoga.pay.wxpay.sdk.WXPayUtil;
import com.yoga.pay.wxpay.sdk.impl.WXPayConfigImpl;
import com.yoga.tenant.setting.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Transactional
public class WxpayService {

    @Autowired
    private SettingService settingService;

    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private WxpayWaterRepository wxpayWaterRepository;

    public Map<String, String> signApp(long tenantId, String orderNo, double totalFee, String notifyUrl, String body, String clientIp) {
        try {
            BigDecimal total_fee = new BigDecimal(String.valueOf(totalFee)).multiply(new BigDecimal(String.valueOf(100)));
            WxpayParam param = settingService.get(tenantId, Module_Name, Key_wxpay, WxpayParam.class);
            if (StrUtil.hasBlank(param.getAppId(), param.getApiKey(), param.getMchId()))
                throw new BusinessException("尚未开通微信支付功能！");
            Map<String, String> oparams = new TreeMap();
            oparams.put("appid", param.getAppId());
            oparams.put("mch_id", param.getMchId());
            oparams.put("nonce_str", WXPayUtil.generateNonceStr());
            oparams.put("body", body);
            oparams.put("out_trade_no", orderNo);
            oparams.put("total_fee", String.valueOf(total_fee.longValue()));
            oparams.put("spbill_create_ip", clientIp);
            oparams.put("notify_url", notifyUrl);
            oparams.put("trade_type", "APP");
//            oparams.put("sign", WXPayUtil.generateSignature(oparams, param.getApiKey()));
            WXPayConfigImpl config = new WXPayConfigImpl(param.getAppId(), param.getMchId(), param.getApiKey(), param.getCert());
            WXPay wxPay = new WXPay(config);
            Map<String, String> map = wxPay.unifiedOrder(oparams);
            if ("SUCCESS".equals(map.get("result_code"))) {
                String timeStamp = String.valueOf(WXPayUtil.getCurrentTimestamp());
                SortedMap<String, String> sortMap = new TreeMap();
                sortMap.put("appid", param.getAppId());
                sortMap.put("noncestr", WXPayUtil.generateNonceStr());
                sortMap.put("package", "Sign=WXPay");
                sortMap.put("partnerid", param.getMchId());
                sortMap.put("prepayid", map.get("prepay_id"));
                sortMap.put("timestamp", timeStamp);
                String paySign = WXPayUtil.generateSignature(sortMap, param.getApiKey(), WXPayConstants.SignType.HMACSHA256);
                sortMap.put("sign", paySign);
                return sortMap;
            }
            throw new BusinessException(map.get("err_code_des"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("调用微信支付错误：" + e.getMessage());
        }
    }

    public Map<String, String> refund(long tenantId, String orderNo, String refundNo, double totalFee, double refundFee) {
        try {
            BigDecimal total_fee = new BigDecimal(String.valueOf(totalFee)).multiply(new BigDecimal(String.valueOf(100)));
            BigDecimal refund_fee = new BigDecimal(String.valueOf(refundFee)).multiply(new BigDecimal(String.valueOf(100)));

            WxpayParam param = settingService.get(tenantId, Module_Name, Key_wxpay, WxpayParam.class);
            Map<String, String> oparams = new TreeMap();
            oparams.put("appid", param.getAppId());
            oparams.put("mch_id", param.getMchId());
            oparams.put("nonce_str", WXPayUtil.generateNonceStr());
            oparams.put("out_trade_no", orderNo);
            oparams.put("out_refund_no", refundNo);
            oparams.put("refund_fee", String.valueOf(refund_fee.longValue()));
            oparams.put("total_fee", String.valueOf(total_fee.longValue()));
            oparams.put("sign", WXPayUtil.generateSignature(oparams, param.getApiKey(), WXPayConstants.SignType.HMACSHA256));
            WXPayConfigImpl config = new WXPayConfigImpl(param.getAppId(), param.getMchId(), param.getApiKey(), param.getCert());
            WXPay wxPay = new WXPay(config);
            Map<String, String> map = wxPay.refund(oparams);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("调用微信支付错误：" + e.getMessage());
        }
    }

    public boolean checkCallback(Long tenantId, Map<String, String> data) {
        try {
            WxpayParam param = settingService.get(tenantId, Module_Name, Key_wxpay, WxpayParam.class);
            if (StrUtil.hasBlank(param.getAppId(), param.getApiKey(), param.getMchId())) {
                throw new BusinessException("尚未开通微信支付功能！");
            }
            return WXPayUtil.isSignatureValid(data, param.getApiKey(), WXPayConstants.SignType.HMACSHA256);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    public void saveWater(long tenantId, Map<String, String> result, WaterType type) {
        try {
            String orderNo = result.get("out_trade_no");
            String tradeNo = result.get("transaction_id");
            String bankType = result.get("bank_type");
            String cashFee = result.get("cash_fee");
            String feeType = result.get("fee_type");
            String totalFee = result.get("total_fee");
            String isSubscribe = result.get("is_subscribe");
            String openId = result.get("openid");
            String timeEnd = result.get("time_end");
            String tradeType = result.get("trade_type");
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
            Date date = format.parse(timeEnd);
            WxpayWater saved;
            WxpayWater water = new WxpayWater(tenantId, type, orderNo, tradeNo, bankType, Integer.valueOf(cashFee), feeType, Integer.valueOf(totalFee), isSubscribe, openId, date, tradeType);
            water.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_PAY_WATER_WXPAY_ID));
            wxpayWaterRepository.save(water);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private static SortedMap<String, String> sortMap(Map<String, String> map) {
        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });
        SortedMap<String, String> sortmap = new TreeMap<String, String>();
        for (int i = 0; i < infoIds.size(); i++) {
            String[] split = infoIds.get(i).toString().split("=");
            sortmap.put(split[0], split[1]);
        }
        return sortmap;
    }

    public static final String Module_Name = "gcf_wxpay";
    public static final String Key_wxpay = "wxpay.setting";

    public WxpayParam getSetting(long tenantId) {
        WxpayParam param = settingService.get(tenantId, Module_Name, Key_wxpay, WxpayParam.class);
        if (param == null) return new WxpayParam();
        else return param;
    }

    public void saveSetting(long tenantId, WxpayParam param) {
        WxpayParam saved = settingService.get(tenantId, Module_Name, Key_wxpay, WxpayParam.class);
        if (saved == null) saved = new WxpayParam();
        if (StrUtil.isNotBlank(param.getAppId())) saved.setAppId(param.getAppId());
        if (StrUtil.isNotBlank(param.getMchId())) saved.setMchId(param.getMchId());
        if (StrUtil.isNotBlank(param.getApiKey())) saved.setApiKey(param.getApiKey());
        if (StrUtil.isNotBlank(param.getCert())) saved.setCert(param.getCert());
        if (StrUtil.isBlank(saved.getAppId())) throw new BusinessException("用户AppID不能为空");
        if (StrUtil.isBlank(saved.getMchId())) throw new BusinessException("商户号不能为空");
        if (StrUtil.isBlank(saved.getApiKey())) throw new BusinessException("API密钥不能为空");
        if (StrUtil.isBlank(saved.getCert())) throw new BusinessException("商户证书不能为空");

        settingService.save(tenantId, Module_Name, Key_wxpay, JSON.toJSONString(saved), saved.getAppId());


    }
}
