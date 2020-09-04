package com.yoga.utility.qr.service;

import com.alibaba.fastjson.TypeReference;
import com.yoga.utility.qr.dto.UploadedFileBean;
import com.yoga.utility.qr.model.QRBindInfo;
import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.utils.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class QRCodeService extends BaseService {
	private final static String RedisKey_Qr_Common = "QR.Common";
	private final static String RedisKey_Qr_file = "Qr.File";

	public interface ActionQueryEntry {
		String actionQuery(String code);
	}

	private List<ActionQueryEntry> actionQueryEntries = new ArrayList<>();

	public void registerQueryEntry(ActionQueryEntry entry) {
		actionQueryEntries.add(entry);
	}
	public void unregisterQueryEntry(ActionQueryEntry entry) {
		actionQueryEntries.remove(entry);
	}

	@PostConstruct
	public void registerEntry() {
		registerQueryEntry(code-> {
			QRBindInfo info = redisOperator.get(RedisKey_Qr_Common + code, QRBindInfo.class);
			if (info == null) return null;
			return info.getUrl() + "?code=" + code;
		});
	}

	public String actionQuery(String code) {
		for (ActionQueryEntry entry : actionQueryEntries) {
			String url = entry.actionQuery(code);
			if (StringUtil.isNotBlank(url)) return url;
		}
		return null;
	}

	public void bindToQr(String code, HttpServletRequest request, String url, Map<String, Object> params) {
		org.apache.shiro.subject.Subject subject = SecurityUtils.getSubject();
		Session session = subject == null ? null : subject.getSession();
		String token = session == null ? null : session.getId().toString();
		QRBindInfo bindInfo = new QRBindInfo(request, url, params, token);
		redisOperator.set(RedisKey_Qr_Common + code, bindInfo, 600);
	}
	public void bindToQr(String code, HttpServletRequest request, String url, Map<String, Object> params, String token) {
		QRBindInfo bindInfo = new QRBindInfo(request, url, params, token);
		redisOperator.set(RedisKey_Qr_Common + code, bindInfo, 600);
	}
	public QRBindInfo unbindFromQr(String code) {
		String key = RedisKey_Qr_Common + code;
		QRBindInfo info = redisOperator.get(key, QRBindInfo.class);
		if (info == null) throw new BusinessException("未找到二维码信息");
		redisOperator.remove(key);
		return info;
	}
	public QRBindInfo pickFromQr(String code) {
		String key = RedisKey_Qr_Common + code;
		QRBindInfo info = redisOperator.get(key, QRBindInfo.class);
		if (info == null) throw new BusinessException("未找到二维码信息");
		return info;
	}
	public void bindFilesToQr(String code, List<UploadedFileBean> files) {
		redisOperator.set(RedisKey_Qr_file + code, files, 60);
	}
    public List<UploadedFileBean> unbindFilesFromQr(String code) {
        String key = RedisKey_Qr_file + code;
        return redisOperator.get(key, new TypeReference<List<UploadedFileBean>>(){});
    }
}