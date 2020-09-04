package com.yoga.admin.shiro;

import com.yoga.core.data.CommonResult;
import com.yoga.core.data.ResultConstants;
import com.yoga.core.spring.SpringContext;
import com.yoga.core.utils.StringUtil;
import com.yoga.logging.model.LoginUser;
import com.yoga.tenant.tenant.service.TenantService;
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
public class OperatorShiroFilter extends AbstractShiroFilter {
    private static Logger logger = LoggerFactory.getLogger(OperatorShiroFilter.class);

    @Autowired
    private SpringContext springContext;
    @Autowired
    private TenantService tenantService;
    @Value("${yoga.tenant.placeholder:}")
    private String placeholder = "";

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
        if ("/v2/api-docs".equals(url) || "doc.html".equals(url)) {
            final ServletRequest msg = this.prepareServletRequest(servletRequest, servletResponse, chain);
            final ServletResponse response = this.prepareServletResponse(msg, servletResponse, chain);
            WebSubject subject = this.createSubject(msg, response);
            if (subject != null && subject.isPermitted("sys_apidoc")) {
                chain.doFilter(servletRequest, servletResponse);
            } else {
                responseError(url, (HttpServletResponse) servletResponse);
            }
        } else if (!ignore) {
            request.setCharacterEncoding("UTF-8");
            Long tid = getTid(servletRequest, servletResponse, chain);
            if (tid == null && StringUtil.isNotBlank(placeholder) && (servletResponse instanceof HttpServletResponse)) {
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.setContentType("text/html;charset=UTF-8");
                response.sendRedirect(placeholder);
                return;
            } else if (tid == null) {
                tid = 0L;
            }
            Map<String, String[]> m = new HashMap<>(servletRequest.getParameterMap());
            m.put("tid", new String[]{String.valueOf(tid)});
            servletRequest = new RequestWrapper((HttpServletRequest) servletRequest, m);
            WebSecurityManager securityManager = (WebSecurityManager) springContext.getApplicationContext().getBean("securityManager");
            setSecurityManager(securityManager);
            super.doFilterInternal(servletRequest, servletResponse, chain);
        } else {
            chain.doFilter(servletRequest, servletResponse);
        }
    }

    private void responseError(String requestUrl, HttpServletResponse response) {
        try {
            if (requestUrl != null && (requestUrl.startsWith("/api") || requestUrl.endsWith(".json"))) {
                CommonResult result = new CommonResult(ResultConstants.ERROR_UNAUTHORIZED, "未授权");
                response.getWriter().print(result.toString());
            } else {
                response.getWriter().println("未授权！");
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
                    LoginUser user = (LoginUser) session.getAttribute("user");
                    if (user != null) tid = user.getTenantId();
                }
            }
        } catch (Exception ex) {
        }
        if (tid == null) {
            if (servletRequest instanceof HttpServletRequest) {
                String tids = servletRequest.getParameter("tid");
                if (StringUtil.isNotBlank(tids)) {
                    try {
                        tid = Long.valueOf(tids);
                    } catch (Exception ex) {
                        tid = null;
                    }
                }
                if (tid == null) {
                    tids = ((HttpServletRequest)servletRequest).getHeader("tid");
                    if (StringUtil.isNotBlank(tids)) {
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
            if (StringUtil.isNotBlank(host) && host.indexOf(".") > 0) {
                code = host.substring(0, host.indexOf("."));
            }
            if (StringUtil.isNotBlank(code) && codeToId != null && codeToId.size() > 0) {
                tid = codeToId.get(code);
            }
        }
        return tid;
    }
}
