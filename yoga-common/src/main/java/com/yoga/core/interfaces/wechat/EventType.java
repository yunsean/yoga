package com.yoga.core.interfaces.wechat;

import com.yoga.core.data.BaseEnum;

public enum EventType implements BaseEnum<String> {

    menu("菜单事件"),
    text("文字消息"),
    image("图片消息"),
    voice("语音消息"),
    video("视频消息"),
    shortvideo("小视频消息"),
    location("地理位置消息"),
    link("链接消息"),
    subscribe("用户关注"),
    unsubscribe("取消关注"),
    click("点击菜单项");
    private final String name;
    EventType(String name){
        this.name = name;
    }

    @Override
    public String getCode() {
        return null;
    }
    @Override
    public String getName() {
        return null;
    }
}
