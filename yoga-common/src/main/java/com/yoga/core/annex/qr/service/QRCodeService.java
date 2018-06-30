package com.yoga.core.annex.qr.service;

import com.yoga.core.annex.qr.model.QRBindInfo;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.service.BaseService;
import com.yoga.core.utils.StrUtil;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class QRCodeService extends BaseService {
	private final static String RedisKey_Show_QR = "RedisKey.QR.Show";

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
		registerQueryEntry(new QRCodeService.ActionQueryEntry() {
			@Override
			public String actionQuery(String code) {
				QRBindInfo info = redisOperator.get(RedisKey_Show_QR + code, QRBindInfo.class);
				if (info == null) return null;
				if (info.isTransfer()) return "/qr/transfer?code=" + code;
				unbindFromQr(code);
				return info.getUrl();
			}
		});
	}

	public String actionQuery(String code) {
		for (ActionQueryEntry entry : actionQueryEntries) {
			String url = entry.actionQuery(code);
			if (StrUtil.isNotBlank(url)) return url;
		}
		return null;
	}

	public void bindQrToShow(String code, String url, Map<String, String> headers) {
		redisOperator.set(RedisKey_Show_QR + code, new QRBindInfo(url, headers), 300);
	}
	public QRBindInfo unbindFromQr(String code) {
		String key = RedisKey_Show_QR + code;
		QRBindInfo info = redisOperator.get(key, QRBindInfo.class);
		if (info == null) throw new BusinessException("未找到二维码信息");
		redisOperator.remove(key);
		return info;
	}
}