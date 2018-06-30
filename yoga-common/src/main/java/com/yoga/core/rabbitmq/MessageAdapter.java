package com.yoga.core.rabbitmq;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;

import com.alibaba.fastjson.JSON;

/**
 * 消息适配器
 * @author Skysea
 *
 */
public class MessageAdapter {
	
	public static Map<String, Object> getBody(Message message){
		
		String body = null;
		try {
			body = new String(message.getBody(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(null == body) throw new AmqpRejectAndDontRequeueException("消息字节流转换失败"+message);
		
		Map<String, Object> map = null;
		try{
			map = JSON.parseObject(body);
		}catch(Exception e){
			throw new AmqpRejectAndDontRequeueException("消息转对象异常"+message);
		}
		
		if(null == map) new AmqpRejectAndDontRequeueException("消息转对象失败"+message);
		return map;
	}
}
