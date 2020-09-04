package com.yoga.setting.customize;

import com.yoga.core.utils.NumberUtil;
import com.yoga.setting.service.SettingService;
import com.yoga.core.data.CommonPage;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class CustomPage extends CommonPage {

    public CustomPage() {
        super();
    }
    public CustomPage(int pageIndex, int pageSize){
        super(pageIndex, pageSize);
    }
    public CustomPage(int pageIndex, int pageSize, long totalCount){
        super(pageIndex, pageSize, totalCount);
    }
    public CustomPage(CommonPage page, long totalCount){
        super(page, totalCount);
    }

    @Override
    protected int defaultPageSize() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        Long tid = NumberUtil.longValue(request.getParameter("tid"));
        if (settingService != null) return settingService.getPageSize(tid);
        else return super.defaultPageSize();
    }

    private static SettingService settingService = null;
    public static void setSettingService(SettingService settingService) {
        CustomPage.settingService = settingService;
    }
}
