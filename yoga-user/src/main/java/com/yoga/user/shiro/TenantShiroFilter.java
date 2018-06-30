package com.yoga.user.shiro;

import com.yoga.core.data.CommonResult;
import com.yoga.core.data.ResultConstants;
import com.yoga.core.spring.SpringContext;
import com.yoga.core.utils.ParameterRequestWrapper;
import com.yoga.core.utils.StrUtil;
import com.yoga.tenant.tenant.model.Tenant;
import com.yoga.tenant.tenant.service.TenantService;
import com.yoga.tenant.tenant.util.TenantContext;
import com.yoga.user.user.model.User;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.apache.shiro.web.subject.WebSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class TenantShiroFilter extends AbstractShiroFilter {
    private static Logger logger = LoggerFactory.getLogger(TenantShiroFilter.class);

    @Autowired
    private SpringContext springContext;
    @Autowired
    private TenantService tenantService;
    @Value("${yoga.tenant.expire-nerver:true}")
    private boolean nerverExpire = true;

    private static final Set<String> ignoreExt = new HashSet<String>() {{
        add(".jpg");
        add(".png");
        add(".gif");
        add(".bmp");
        add(".js");
        add(".css");
    }};

    @Override
    protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        request.setCharacterEncoding("UTF-8");  //解决页面表单提交中文乱码问题
        String url = request.getRequestURI().toLowerCase();
        logger.info(url);
        String str = url;
        int idx = str.indexOf(".");
        boolean ignore = false;
        if (idx > 0) {
            str = str.substring(idx);
            if (ignoreExt.contains(str.toLowerCase())) {
                ignore = true;
            }
        }
        if (!ignore) {
            request.setCharacterEncoding("UTF-8");
            Long tid = getTid(servletRequest, servletResponse, chain);
            if (tid != null) {
                Map<String, String[]> m = new HashMap<>(servletRequest.getParameterMap());
                m.put("tid", new String[]{String.valueOf(tid)});
                servletRequest = new ParameterRequestWrapper((HttpServletRequest) servletRequest, m);
            }
            if (tid == null) tid = 0L;
            if (!nerverExpire && tid != 0L) {
                Tenant tenant = tenantService.get(tid);
                if (tenant == null || tenant.hasExpired()) {
                    responseError(url, (HttpServletResponse) servletResponse);
                    return;
                }
            }
            TenantContext.setId(tid);
            WebSecurityManager securityManager = (WebSecurityManager) springContext.getApplicationContext().getBean("securityManager");
            setSecurityManager(securityManager);
            super.doFilterInternal(servletRequest, servletResponse, chain);
        } else {
            chain.doFilter(servletRequest, servletResponse);
        }
    }

    private void responseError(String requestUrl, HttpServletResponse response) {
        try {
            if (requestUrl != null && requestUrl.startsWith("/api")) {
                CommonResult result = new CommonResult(ResultConstants.ERROR_UNAUTHORIZED, "未授权");
                response.getWriter().print(result.toString());
            } else {
                response.getWriter().println("租户已过期！");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Long getTid(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) {
        Long tid = null;
        try {
            final ServletRequest msg = this.prepareServletRequest(servletRequest, servletResponse, chain);
            final ServletResponse response = this.prepareServletResponse(msg, servletResponse, chain);
            WebSubject subject = this.createSubject(msg, response);
            if (subject != null) {
                Session session = subject.getSession();
                if (session != null) {
                    User user = (User) session.getAttribute("user");
                    if (user != null) {
                        tid = user.getTenantId();
                    }
                }
            }
        } catch (Exception ex) {
        }
        if (tid == null) {
            if (servletRequest instanceof HttpServletRequest) {
                String tids = servletRequest.getParameter("tid");
                if (StrUtil.isNotBlank(tids)) {
                    try {
                        tid = Long.valueOf(tids);
                    } catch (Exception ex) {
                        tid = null;
                    }
                }
                if (tid == null) {
                    tids = ((HttpServletRequest)servletRequest).getHeader("tid");
                    if (StrUtil.isNotBlank(tids)) {
                        try {
                            tid = Long.valueOf(tids);
                        } catch (Exception ex) {
                            tid = null;
                        }
                    }
                }
            }
        }
        if (tid == null) {
            String host = servletRequest.getServerName();
            String code = "";
            Map<String, Long> codeToId = tenantService.allCode2Id();
            if (StrUtil.isNotBlank(host) && host.indexOf(".") > 0) {
                code = host.substring(0, host.indexOf("."));
            }
            if (StrUtil.isNotBlank(code) && codeToId != null && codeToId.size() > 0) {
                tid = codeToId.get(code);
            }
        }
        return tid;
    }
}
