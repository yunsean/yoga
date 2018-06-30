package com.yoga.user.user.service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.service.BaseService;
import com.yoga.user.sequence.SequenceNameEnum;
import com.yoga.user.user.model.UserData;
import com.yoga.user.user.repo.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

@Service
public class UserDataService extends BaseService {

	@Autowired
	private UserDataRepository userDataRepository;

	private Gson gson = null;
	private Gson getGson() {
		if (gson != null) return gson;
		synchronized (this) {
			if (gson == null) {
				gson = new GsonBuilder()
						.setDateFormat("yyyy-MM-dd HH:mm:ss")
						.create();
			}
		}
		return gson;
	}

	public void setUserData(long tenantId, long userId, String key, String value) {
		UserData userData = userDataRepository.findFirstByUserIdAndName(userId, key);
		if (userData == null) {
			userData = new UserData();
			userData.setUserId(userId);
			userData.setTenantId(tenantId);
			userData.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_S_USER_DATA_ID));
		}
		userData.setName(key);
		userData.setValue(value);
		userDataRepository.save(userData);
	}
	
	public String getUserData(long tenantId, long userId, String key, boolean optional) {
		UserData userData = userDataRepository.findFirstByUserIdAndName(userId, key);
		if (userData == null) {
			if (optional) return null;
			else throw new BusinessException("未找到用户数据项！");
		}
		return userData.getValue();
	}

	public <T> void setUserData(long tenantId, long userId, String key, T value) {
		UserData userData = userDataRepository.findFirstByUserIdAndName(userId, key);
		if (userData == null) {
			userData = new UserData();
			userData.setUserId(userId);
			userData.setTenantId(tenantId);
			userData.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_S_USER_DATA_ID));
		}
		userData.setName(key);
		userData.setValue(getGson().toJson(value));
		userDataRepository.save(userData);
	}
	public <T> T getUserData(long tenantId, long userId, String key, Type type, boolean optional) {
		UserData userData = userDataRepository.findFirstByUserIdAndName(userId, key);
		if (userData == null) {
			if (optional) return null;
			else throw new BusinessException("未找到用户数据项！");
		}
		return (T) getGson().fromJson(userData.getValue(), type);
	}
	public <T> T getUserData(long tenantId, long userId, String key, T defaultValue) {
		UserData userData = userDataRepository.findFirstByUserIdAndName(userId, key);
		if (userData == null) return defaultValue;
		String value = userData.getValue();
		if (value == null) return defaultValue;
		try {
			if (defaultValue.getClass().isPrimitive()) {
				PropertyEditor editor = PropertyEditorManager.findEditor(defaultValue.getClass());
				editor.setAsText(value);
				return (T) editor.getValue();
			}
			Method valueOf = defaultValue.getClass().getMethod("valueOf", new Class[] { String.class });
			if (valueOf != null) {
				return (T)valueOf.invoke(null, new Object[]{value});
			}
			return (T) JSONObject.parseObject(value, defaultValue.getClass());
		} catch (Exception ex) {
			return defaultValue;
		}
	}
}
