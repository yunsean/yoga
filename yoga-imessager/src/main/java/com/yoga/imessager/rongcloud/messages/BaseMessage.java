package com.yoga.imessager.rongcloud.messages;

//消息基类，如实现自定义消息，可继承此类
public abstract class BaseMessage {

	public abstract String getType();

	public abstract String toString();
}
