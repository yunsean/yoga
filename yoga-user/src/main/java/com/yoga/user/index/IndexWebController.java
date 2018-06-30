package com.yoga.user.index;

import com.yoga.core.controller.BaseWebController;
import com.yoga.tenant.setting.Settable;
import com.yoga.tenant.tenant.enums.IndexModeType;
import com.yoga.tenant.tenant.service.TenantService;
import com.yoga.user.basic.TenantDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Settable
@Controller
@RequestMapping("/")
@Settable(module = TenantService.Setting_Module, key = TenantService.IndexMode_Key, name = "网站默认入口", type = IndexModeType.class, defaultValue = "Admin", systemOnly = true)
public class IndexWebController extends BaseWebController {

    @Autowired
    private TenantService tenantService;

    @RequestMapping(value = "")
    @ResponseStatus(HttpStatus.FOUND)
    public String index(HttpServletRequest request, TenantDto dto) {
        IndexModeType type = tenantService.indexModeType(dto.getTid());
        if (type == IndexModeType.Auto) {
            String userAgent = request.getHeader("user-agent").toLowerCase();
            if (check(userAgent)) return IndexModeType.Web.getCode();
            else return IndexModeType.Admin.getCode();
        }
        return "redirect:" + type.getCode();
    }

    static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i"
            +"|windows (phone|ce)|blackberry"
            +"|s(ymbian|eries60|amsung)|p(laybook|micromessenger|alm|rofile/midp"
            +"|laystation portable)|nokia|fennec|htc[-_]"
            +"|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
    static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser"
            +"|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
    static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
    static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);
    public static boolean check(String userAgent){
        if (null == userAgent) return false;
        Matcher matcherPhone = phonePat.matcher(userAgent);
        Matcher matcherTable = tablePat.matcher(userAgent);
        if(matcherPhone.find() || matcherTable.find()) return true;
        else return false;
    }
}
