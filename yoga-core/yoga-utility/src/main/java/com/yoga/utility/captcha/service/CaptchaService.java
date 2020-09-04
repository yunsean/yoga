package com.yoga.utility.captcha.service;

import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.utils.StringUtil;
import com.yoga.utility.captcha.model.CaptchaSetting;
import com.yoga.setting.service.SettingService;
import com.yoga.utility.sms.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaptchaService extends BaseService {

	@Autowired
	private SettingService settingService;
	@Autowired
	private SmsService smsService;

	public final static String ModuleName = "gcf_sms_captcha";
	public final static String Key_Config = "captcha.config";
	public CaptchaSetting getSetting(long tenantId) {
		return settingService.get(tenantId, ModuleName, Key_Config, CaptchaSetting.class);
	}
	public void setSetting(long tenantId, String value, String showValue) {
		settingService.save(tenantId, ModuleName, Key_Config, value, showValue);
	}

	public boolean verifyCaptcha(long tenantId, String mobile, String uuid, String captcha) {
		if(StringUtil.isBlank(captcha)) return false;
		String key = "captcha." + tenantId + "." + mobile + "_" + uuid;
		if (!captcha.equals(redisOperator.get(key))) return false;
		redisOperator.remove(key);
		return true;
	}

	/**
	 * 自动生成短信信息并且返回验证码
	 * @param tenantId 租户ID
	 * @param mobile 手机号码
	 * @param uuid 短信唯一标识
	 * @return 根据配置决定是否返回验证码
	 */
	public String sendCaptcha(long tenantId, String mobile, String uuid) {
		if (redisOperator.getExpire("captcha.interval." + tenantId + "." + mobile) > 0) throw new BusinessException("短信发送过于频繁");
		CaptchaSetting setting = getSetting(tenantId);
		if (setting == null) setting = new CaptchaSetting();
		String captcha = randomCode(setting.getLength());
		String duration = "";
		if (setting.getExpire() >= 120 && setting.getExpire() == 60) duration = String.valueOf(setting.getExpire() / 60) + "分钟";
		else duration = String.valueOf(setting.getExpire()) + "秒";
		String content = "";
		if (StringUtil.isBlank(setting.getFormat())) {
			content = "您的验证码是：" + captcha + "，" + duration + "内有效。如非您本人操作，可忽略本消息。";
		} else {
			content = setting.getFormat().replace("#code#", captcha).replace("#time#", String.valueOf(duration));
		}
		if (!setting.isAutofill()) smsService.sendSmsSync(tenantId, mobile, content, "验证码");
		String key = "captcha." + tenantId + "." + mobile + "_" + uuid;
		redisOperator.set(key, captcha, setting.getExpire());
		if (setting.getInterval() > 0) {
			redisOperator.set("captcha.interval." + tenantId + "." + mobile, captcha, setting.getInterval());
		}
		if (setting.isAutofill()) return captcha;
		else return null;
	}
	private String randomCode(int length){
		return  ""+((int)((Math.random() * 9 + 1) * Math.pow(10, length - 1)));
	}
}
