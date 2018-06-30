package com.yoga.core.interfaces.wechat;

import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface WechatAction {

    String getName();
    String getCode();

    /**
     * 返回本插件支持的事件模型
     * @return
     */
    EventType[] supportEvent();

    /**
     * 返回本插件是否需要配置
     * @return
     */
    default boolean needConfig() {
        return false;
    }

    /**
     * 该事件希望的用户信息提供级别
     * @return 用户信息获取级别
     */
    default IdentityType expectIdentity() {
        return IdentityType.none;
    }

    /**
     * 当为菜单事件时，点击菜单后的行为
     * @return 菜单行为
     */
    default MenuEvent menuEvent() {
        return null;
    }

    /**
     * 档位菜单事件，并且MenuEvent为view时，可直接返回点击跳转的链接（也可以通过后续的showConfig拼接返回跳转地址）
     * 如果EventType=menu，MenuEvent=view时，并且未通过showConfig返回参数，则将使用该值
     * @return 点击跳转的链接
     */
    default String clickedUrl(long tenantId, long accountId) {
        return null;
    }

    /**
     * 调用插件的配置页面
     * @param tenantId 租户ID
     * @param request
     * @param response
     * @param model
     * @param config 修改模式时，传递原始的配置参数
     * @return 返回页面模板
     * 约定：在返回的页面中，需要包含js函数：getConfig
     * 该函数返回一个字符串：
     *  *当EventType=menu，MenuEvent=view时有效，点击菜单项跳转URL
     *  *当为其他情况时，为其自定义参数，将传递给onMessage作为config
     */
    default String showConfig(long tenantId, HttpServletRequest request, HttpServletResponse response, ModelMap model, String config) {
        return null;
    }

    /**
     * 当微信触发后，调用插件
     * @param inMessage
     * @return 返回应答内容（如果返回null，框架将自动发送succeed回复）
     */
    default OutMessage onMessage(long tenantId, long accountId, InMessage inMessage, String config) {
        return null;
    }
}
