package com.yoga.user.basic;

import com.yoga.core.data.CommonPage;
import com.yoga.core.utils.NumberUtil;
import com.yoga.tenant.setting.service.SettingService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class TenantPage extends CommonPage {

    public TenantPage() {
        super();
    }
    public TenantPage(int pageIndex, int pageSize){
        super(pageIndex, pageSize);
    }
    public TenantPage(int pageIndex, int pageSize, long totalCount){
        super(pageIndex, pageSize, totalCount);
    }
    public TenantPage(CommonPage page, long totalCount){
        super(page, totalCount);
    }

    @Override
    protected int defaultPageSize() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        Long tid = NumberUtil.longValue(request.getParameter("tid"));
        if (tid == null) tid = 0L;
        if (settingService != null) {
            Integer pageSize = (Integer) settingService.getPageSize(tid);
            if (pageSize != null) return pageSize;
        }
        return super.defaultPageSize();
    }

    private static SettingService settingService = null;
    public static void setSettingService(SettingService settingService) {
        TenantPage.settingService = settingService;
    }
}
