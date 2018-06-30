package com.yoga.pay.alipay.service;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.service.BaseService;
import com.yoga.core.utils.StrUtil;
import com.yoga.pay.alipay.enums.WaterType;
import com.yoga.pay.alipay.model.AlipayTransferWater;
import com.yoga.pay.alipay.model.AlipayParam;
import com.yoga.pay.alipay.model.AlipayWater;
import com.yoga.pay.alipay.repo.AlipayTransferWaterRepository;
import com.yoga.pay.alipay.repo.AlipayWaterRepository;
import com.yoga.pay.sequence.SequenceNameEnum;
import com.yoga.tenant.setting.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.Map;

@Service
public class AlipayService extends BaseService {

    @Autowired
    private SettingService settingService;
    @Autowired
    private AlipayWaterRepository alipayWaterRepository;
    @Autowired
    private AlipayTransferWaterRepository alipayTransferWaterRepository;

    public final static int GoodsType_Entity = 1;   //实物商品
    public final static int GoodsType_Virtual = 0;  //虚拟商品，不支持花呗


    public String sign(long tenantId, String orderNo, double amount, String timeout, String notifyUrl, String notifyParam, String subject, String describe) {
        return sign(tenantId, orderNo, amount, timeout, GoodsType_Entity, false, notifyUrl, notifyParam, subject, describe);
    }

    /* 调用支付宝签名
     * timeout：订单超时时间：取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。该参数数值不接受小数点， 如 1.5h，可转换为 90m。
     */
    public String sign(long tenantId, String orderNo, double amount, String timeout, int goodsType, boolean creditEnable, String notifyUrl, String notifyParam, String subject, String describe) {
        try {
            AlipayParam param = settingService.get(tenantId, Module_Name, Key_Alipay, AlipayParam.class);
            if (StrUtil.hasBlank(param.getAppId(), param.getAppPrivateKey(), param.getAlipayPublicKey()))
                throw new BusinessException("尚未开通支付宝支付功能！");
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                    param.getAppId(), param.getAppPrivateKey(), "json", "utf-8", param.getAlipayPublicKey(), "RSA2");
            AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setBody(describe);
            model.setSubject(subject);
            model.setOutTradeNo(orderNo);
            model.setTimeoutExpress(timeout);
            model.setTotalAmount(String.format("%.2f", amount));
            model.setProductCode("QUICK_MSECURITY_PAY");
            model.setGoodsType(String.valueOf(goodsType));
            if (StrUtil.isNotBlank(notifyParam)) model.setPassbackParams(URLEncoder.encode(notifyParam, "utf-8"));
            if (!creditEnable) model.setDisablePayChannels("creditCard");
            request.setBizModel(model);
            request.setNotifyUrl(notifyUrl);
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            if (!response.isSuccess()) throw new Exception(response.getSubMsg());
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("调用支付宝接口错误：" + e.getMessage());
        }
    }

    public AlipayFundTransToaccountTransferResponse transfer(long tenantId, String bizNo, double amount, String account, String payerShowName, String remark) {
        AlipayFundTransToaccountTransferResponse response = null;
        try {
            AlipayParam param = settingService.get(tenantId, Module_Name, Key_Alipay, AlipayParam.class);
            if (StrUtil.hasBlank(param.getAppId(), param.getAppPrivateKey(), param.getAlipayPublicKey()))
                throw new BusinessException("尚未开通支付宝支付功能！");
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", param.getAppId(), param.getAppPrivateKey(), "json", "utf-8", param.getAlipayPublicKey(), "RSA2");
            AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
            AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
            model.setOutBizNo(bizNo);
            model.setPayeeType("ALIPAY_LOGONID");
            model.setPayeeAccount(account);
            model.setAmount(String.format("%.2f", amount));
            model.setPayerShowName(payerShowName);
            model.setRemark(remark);
            request.setBizModel(model);
            response = alipayClient.execute(request);
            saveTransferWater(tenantId, response);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return response;
        }

    }

    public boolean refund(long tenantId, String orderNo, String refundNo, double refundFee) {
        try {
            AlipayParam param = settingService.get(tenantId, Module_Name, Key_Alipay, AlipayParam.class);
            if (StrUtil.hasBlank(param.getAppId(), param.getAppPrivateKey(), param.getAlipayPublicKey()))
                throw new BusinessException("尚未开通支付宝支付功能！");
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", param.getAppId(), param.getAppPrivateKey(), "json", "utf-8", param.getAlipayPublicKey(), "RSA2");
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
            model.setOutTradeNo(orderNo);
            model.setRefundAmount(String.format("%.2f", refundFee));
            model.setRefundReason("正常退款");
            model.setOutRequestNo(refundNo);
            request.setBizModel(model);
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if (!response.isSuccess()) throw new Exception(response.getSubMsg());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("调用支付宝接口错误：" + e.getMessage());
        }
    }

    public boolean close(long tenantId, String user, String orderNo, String notifyUrl) {
        try {
            AlipayParam param = settingService.get(tenantId, Module_Name, Key_Alipay, AlipayParam.class);
            if (StrUtil.hasBlank(param.getAppId(), param.getAppPrivateKey(), param.getAlipayPublicKey()))
                throw new BusinessException("尚未开通支付宝支付功能！");
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                    param.getAppId(), param.getAppPrivateKey(), "json", "utf-8", param.getAlipayPublicKey(), "RSA2");
            AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
            AlipayTradeCloseModel model = new AlipayTradeCloseModel();
            model.setOutTradeNo(orderNo);
            model.setOperatorId(user);
            request.setBizModel(model);
            request.setNotifyUrl(notifyUrl);
            AlipayTradeCloseResponse response = alipayClient.execute(request);
            if (!response.isSuccess()) throw new Exception(response.getSubMsg());
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BusinessException("调用支付宝接口错误：" + ex.getMessage());
        }
    }

    public boolean cancel(long tenantId, String orderNo, String notifyUrl) {
        try {
            AlipayParam param = settingService.get(tenantId, Module_Name, Key_Alipay, AlipayParam.class);
            if (StrUtil.hasBlank(param.getAppId(), param.getAppPrivateKey(), param.getAlipayPublicKey()))
                throw new BusinessException("尚未开通支付宝支付功能！");
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                    param.getAppId(), param.getAppPrivateKey(), "json", "utf-8", param.getAlipayPublicKey(), "RSA2");
            AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
            request.setBizContent("{" +
                    "\"out_trade_no\":\"" + orderNo + "\"" +
                    "  }");
            request.setNotifyUrl(notifyUrl);
            AlipayTradeCancelResponse response = alipayClient.execute(request);
            if (!response.isSuccess()) throw new Exception(response.getSubMsg());
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BusinessException("调用支付宝接口错误：" + ex.getMessage());
        }
    }

    public boolean checkCallback(long tenantId, Map<String, String> result) {
        try {
            result.remove("tid");
            AlipayParam param = settingService.get(tenantId, Module_Name, Key_Alipay, AlipayParam.class);
            if (StrUtil.hasBlank(param.getAppId(), param.getAppPrivateKey(), param.getAlipayPublicKey()))
                throw new BusinessException("尚未开通支付宝支付功能！");
            return AlipaySignature.rsaCheckV1(result, param.getAlipayPublicKey(), "utf-8", "RSA2");
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void saveTransferWater(long tenantId, AlipayFundTransToaccountTransferResponse response) {

        AlipayTransferWater transferWater = new AlipayTransferWater(tenantId, response.getOrderId(), response.getOutBizNo(), response.getPayDate());
        transferWater.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_TRANSFER_WATER_ALIPAY_ID));
        alipayTransferWaterRepository.save(transferWater);
    }

    public void saveWater(long tenantId, Map<String, String> result, boolean rsaChecked, String bizResult) {
        String orderNo = result.get("out_trade_no");
        String tradeNo = result.get("trade_no");
        String buyer = result.get("buyer_logon_id");
        String status = result.get("trade_status");
        String response = JSON.toJSONString(result);
        AlipayWater water = new AlipayWater(tenantId, WaterType.pay, orderNo, tradeNo, buyer, status, response, rsaChecked, bizResult);
        water.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_PAY_WATER_ALIPAY_ID));
        alipayWaterRepository.save(water);
    }

    public static final String Module_Name = "gcf_pay";
    public static final String Key_Alipay = "alipay.setting";

    public AlipayParam getSetting(long tenantId) {
        AlipayParam param = settingService.get(tenantId, Module_Name, Key_Alipay, AlipayParam.class);
        if (param == null) return new AlipayParam();
        else return param;
    }

    public void saveSetting(long tenantId, AlipayParam param) {
        AlipayParam saved = settingService.get(tenantId, Module_Name, Key_Alipay, AlipayParam.class);
        if (saved == null) saved = new AlipayParam();
        if (StrUtil.isNotBlank(param.getAppId())) saved.setAppId(param.getAppId());
        if (StrUtil.isNotBlank(param.getAppPrivateKey())) saved.setAppPrivateKey(param.getAppPrivateKey());
        if (StrUtil.isNotBlank(param.getAlipayPublicKey())) saved.setAlipayPublicKey(param.getAlipayPublicKey());
        if (StrUtil.isBlank(saved.getAppId())) throw new BusinessException("用户AppID不能为空");
        if (StrUtil.isBlank(saved.getAppPrivateKey())) throw new BusinessException("用户私匙不能为空");
        if (StrUtil.isBlank(saved.getAlipayPublicKey())) throw new BusinessException("支付宝公匙不能为空");
        settingService.save(tenantId, Module_Name, Key_Alipay, JSON.toJSONString(saved), saved.getAppId());
    }
}
