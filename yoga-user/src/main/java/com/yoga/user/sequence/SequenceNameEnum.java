package com.yoga.user.sequence;

import com.yoga.core.data.BaseEnum;

public enum SequenceNameEnum implements BaseEnum<String> {

	TDD_NO_SEQ("",""),

	SEQ_U_USER_ID("seq_u_user_id", "用户Id"),
	SEQ_U_DUTY_ID("seq_s_duty_id", "职级Id"),
	SEQ_DC_DEPT_ID("seq_s_dept_id", "部门Id"),
	SEQ_S_ROLE_ID("seq_s_role_id", "角色Id"),
	SEQ_S_PRIVILEGE_ID("seq_s_privilege_id", "权限Id"),
	SEQ_S_MODULE_ID("seq_s_module_id", "模块Id"),
	SEQ_S_FAVOR_ID("seq_s_favor_id", "收藏Id"),	
	SEQ_S_USER_DATA_ID("seq_s_user_data_id", "用户数据Id"),
	SEQ_G_SETTING_ID("seq_s_setting_id", "配置Id"),

    SEQ_G_TENANT_ID("seq_g_tenant_id", "租户Id"),
    SEQ_G_RECHARGE_ID("seq_g_recharge_id", "充值记录Id"),
    SEQ_G_TENANT_MENU_ID("seq_g_tenant_menu_id", "租户菜单Id"),
    SEQ_G_TENANT_SETTING_ID("seq_g_tenant_setting_id", "租户设置Id"),

    SEQ_DC_LEAVEOFF_ID("seq_dc_leave_off_id", "请假Id"),
    SEQ_DC_LEAVEOFF_LOGS_ID("seq_dc_leave_off_logs_id", "请假日志Id"),

    SEQ_SMS_HISTORY_ID("seq_sms_history_id", "短信历史记录Id"),

    SEQ_G_FILES_ID("seq_g_files_id", "上传文件列表Id");


    private final String code;
    private final String desc;
    private SequenceNameEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public static SequenceNameEnum getEnum(String code) {
        for (SequenceNameEnum status : SequenceNameEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }
    public String getName() {
        return desc;
    }
}
