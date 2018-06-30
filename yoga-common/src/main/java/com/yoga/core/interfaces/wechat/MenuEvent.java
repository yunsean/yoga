package com.yoga.core.interfaces.wechat;

import com.yoga.core.data.BaseEnum;

public enum MenuEvent implements BaseEnum<String> {

    view("跳转URL"),
    click("点击推事件"),
    scancode_push("扫码推事件"),
    scancode_waitmsg("扫码带提示"),
    pic_sysphoto("弹出系统拍照发图"),
    pic_photo_or_album("弹出拍照或者相册发图"),
    pic_weixin("弹出微信相册发图"),
    location_select("地理位置选择");

    private String remark;
    MenuEvent(String remark) {
        this.remark = remark;
    }

    @Override
    public String getName() {
        return remark;
    }
    @Override
    public String getCode() {
        return toString();
    }
}
