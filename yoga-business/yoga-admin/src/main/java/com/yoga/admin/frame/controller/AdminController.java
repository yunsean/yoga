package com.yoga.admin.frame.controller;

import com.yoga.admin.aggregate.ao.Schedule;
import com.yoga.admin.aggregate.ao.Statement;
import com.yoga.admin.aggregate.service.AggregateService;
import com.yoga.tenant.menu.MenuItem;
import com.yoga.admin.frame.dto.LoginDto;
import com.yoga.admin.frame.dto.ToLoginDto;
import com.yoga.admin.shiro.OperatorPrincipal;
import com.yoga.admin.shiro.OperatorToken;
import com.yoga.admin.shiro.RedisSessionDAO;
import com.yoga.admin.shiro.SuperAdminUser;
import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.property.PropertiesService;
import com.yoga.core.redis.RedisOperator;
import com.yoga.core.utils.StringUtil;
import com.yoga.operator.user.model.User;
import com.yoga.operator.user.service.UserService;
import com.yoga.tenant.menu.MenuLoader;
import com.yoga.tenant.tenant.model.TenantCustomize;
import com.yoga.tenant.tenant.model.TenantSetting;
import com.yoga.tenant.tenant.service.TenantService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.patchca.background.SingleColorBackgroundFactory;
import org.patchca.color.RandomColorFactory;
import org.patchca.filter.FilterFactory;
import org.patchca.font.RandomFontFactory;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.utils.encoder.EncoderHelper;
import org.patchca.word.RandomWordFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;

@ApiIgnore
@Controller
@EnableAutoConfiguration
@RequestMapping("/admin")
public class AdminController extends BaseController {

    @Autowired
    SuperAdminUser superAdminUser;
    @Autowired
    private PropertiesService propertiesService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private RedisSessionDAO sessionDAO;
    @Autowired
    private AggregateService aggregateService;

    @Value("${app.login.choice-tenant:false}")
    private boolean choiceTenant = false;
    @Value("${app.login.patcha-font:}")
    private String patchaFont = null;

    @Value("${app.login.default-login:/admin/frame/login}")
    private String defaultLogin = "/admin/frame/login";
    @Value("${app.login.default-back:/admin/login/back.jpg}")
    private String defaultBack = "/admin/login/back.jpg";
    @Value("${app.login.default-pick:/admin/login/pick.png}")
    private String defaultPick = "/admin/login/pick.png";
    @Value("${app.login.default-top:/admin/login/top.png}")
    private String defaultTop = "/admin/login/top.png";

    @RequiresAuthentication
    @RequestMapping(value = "")
    public String index(ModelMap model, @Valid BaseDto dto) {
        TenantCustomize customize = tenantService.readCustomize(dto.getTid());
        if (StringUtil.isBlank(customize.getAdminIndex()) || customize.getAdminIndex().equals("/admin/frame/index")) return "/admin/frame/index";
        else return "redirect:" + customize.getAdminIndex();
    }
    @RequiresAuthentication
    @RequestMapping(value = "/top")
    public String top(HttpServletRequest request, ModelMap model, @Valid BaseDto dto, BindingResult bindingResult) {
        TenantCustomize customize = tenantService.readCustomize(dto.getTid());
        if (StringUtil.isBlank(customize.getAdminTop()) || customize.getAdminTop().equals("/admin/frame/top")) {
            if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
            model.put("user", User.getLoginUser());
            TenantSetting setting = tenantService.readSetting(dto.getTid());
            model.put("topImg", setting.getTopImageUrl());
            model.put("defaultTop", defaultTop);
            model.put("menuColor", setting.getMenuColor());
            return "/admin/frame/top";
        } else {
            String query = request.getQueryString();
            String url = customize.getAdminTop();
            if (StringUtil.isNotBlank(query)) {
                if (url.contains("?")) url += "&" + query;
                else url += "?" + query;
            }
            return "redirect:" + url;
        }
    }
    private static String intToHex(int value) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < 8; i++) {
            byte b = (byte)((value >> (28 - i * 4)) & 0xf);
            if (b == 0 && sb.length() < 1) continue;
            sb.append(chars[b]);
        }
        return sb.toString().trim();
    }
    private static int hexToInt(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        int result = 0;
        for (int i = 0; i < hexs.length; i++) {
            result *= 16;
            result += str.indexOf(hexs[i]);
        }
        return result;
    }
    private String colorToWebColor(Integer color, int defaultColor) {
        if (color == null) color = defaultColor;
        try {
            return intToHex(color);
        } catch (Exception ex) {
            return "";
        }
    }
    private Integer bitOffsetOf(Integer color, float offset) {
        if (color == null) return null;
        try {
            int result = 0;
            result += ((int)(((color >> 24) & 0xff) * offset) << 24) & 0xff000000;
            result += ((int)(((color >> 16) & 0xff) * offset) << 16) & 0x00ff0000;
            result += ((int)(((color >>  8) & 0xff) * offset) <<  8) & 0x0000ff00;
            result += ((int)(((color >>  0) & 0xff) * offset) <<  0) & 0x000000ff;
            return result;
        } catch (Exception ex) {
            return null;
        }
    }
    @RequiresAuthentication
    @RequestMapping(value = "/left")
    public String left(ModelMap model, @Valid BaseDto dto) {
        TenantCustomize customize = tenantService.readCustomize(dto.getTid());
        TenantSetting setting = tenantService.readSetting(dto.getTid());
        model.put("menuColor", setting.getMenuColor());
        model.put("subMenuColor", colorToWebColor(bitOffsetOf(hexToInt(setting.getMenuColor()), 0.8f), 0x384552));
        if (StringUtil.isBlank(customize.getAdminLeft()) || customize.getAdminLeft().equals("/admin/frame/left")) return "/admin/frame/left";
        else return "redirect:" + customize.getAdminLeft();
    }
    @RequiresAuthentication
    @RequestMapping(value = "/welcome")
    public String welcome(HttpServletRequest request, ModelMap model, @Valid BaseDto dto) {
        TenantCustomize customize = tenantService.readCustomize(dto.getTid());
        if (StringUtil.isBlank(customize.getAdminWelcome()) || customize.getAdminWelcome().equals("/admin/frame/welcome")) {
            User user = User.getLoginUser();
            List<Schedule> schedules = aggregateService.getSchedules(dto.getTid(), user.getId());
            List<Statement> statements = aggregateService.getStatements(dto.getTid(), user.getId());
            model.put("user", User.getLoginUser());
            model.put("schedules", schedules);
            model.put("statements", statements);
            return "/admin/frame/welcome";
        } else {
            String query = request.getQueryString();
            String url = customize.getAdminWelcome();
            if (StringUtil.isNotBlank(query)) {
                if (url.contains("?")) url += "&" + query;
                else url += "?" + query;
            }
            return "redirect:" + url;
        }
    }
    @RequiresAuthentication
    @RequestMapping(value = "/passwd")
    public String toResetPwd(ModelMap model) {
        User user = User.getLoginUser();
        if (user == null) {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
            return "redirect:/admin/toLogin";
        } else {
            model.put("user", user);
            return "/admin/frame/passwd";
        }
    }
    @RequestMapping(value = "/toLogin")
    public void loginJump(HttpServletResponse response) throws IOException, ServletException {
        response.getWriter().println("<script>top.location='/admin/loginJump'</script>");
        response.getWriter().close();
    }
    @RequestMapping(value = "/loginJump")
    public String toLogin(HttpServletRequest request, ModelMap model, @Valid ToLoginDto dto, BindingResult bindingResult) {
        TenantCustomize customize = tenantService.readCustomize(dto.getTid());
        if (StringUtil.isBlank(customize.getAdminLogin()) || customize.getAdminLogin().equals("/admin/loginJump") || customize.getAdminLogin().equals("/admin/toLogin")) {
            Map<String, String> img = new HashMap<>();
            TenantSetting setting = tenantService.readSetting(dto.getTid());
            img.put("pickLogin", setting.getLoginLogoUrl());
            img.put("topImage", setting.getTopImageUrl());
            img.put("loginBg", setting.getLoginBackUrl());
            img.put("defaultBack", defaultBack);
            img.put("defaultTop", defaultTop);
            img.put("defaultPick", defaultPick);
            model.put("img", img);
            model.put("error", dto.getReason());
            if (choiceTenant) model.put("tenants", tenantService.getAll());
            return defaultLogin;
        } else {
            String query = request.getQueryString();
            String url = customize.getAdminLogin();
            if (StringUtil.isNotBlank(query)) {
                if (url.contains("?")) url += "&" + query;
                else url += "?" + query;
            }
            return "redirect:" + url;
        }
    }

    private class FontFactory extends RandomFontFactory {
        FontFactory() {
            super();
            if (StringUtil.isNotBlank(patchaFont)) {
                this.families = new ArrayList<>();
                this.families.add(patchaFont);
            }
        }
    }
    private Random random = new Random();
    private FilterFactory[] filterFactories = {new org.patchca.filter.predefined.DoubleRippleFilterFactory(),
            new org.patchca.filter.predefined.WobbleRippleFilterFactory(),
            new org.patchca.filter.predefined.RippleFilterFactory(),
            new org.patchca.filter.predefined.CurvesRippleFilterFactory()};
//    private FilterFactory[] filterFactories = {new org.patchca.filter.predefined.MarbleRippleFilterFactory(),
//            new org.patchca.filter.predefined.DoubleRippleFilterFactory(),
//            new org.patchca.filter.predefined.WobbleRippleFilterFactory(),
//            new org.patchca.filter.predefined.RippleFilterFactory(),
//            new org.patchca.filter.predefined.DiffuseRippleFilterFactory(),
//            new org.patchca.filter.predefined.CurvesRippleFilterFactory()};
    @RequestMapping(value = "/patchca")
    public void creatCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
        RandomFontFactory fontFactory = new FontFactory();
        fontFactory.setMinSize(20);
        fontFactory.setMaxSize(30);
        RandomWordFactory wordFactory = new RandomWordFactory();
        wordFactory.setMinLength(4);
        wordFactory.setMaxLength(4);
        wordFactory.setCharacters("23456789abcdefghigkmnpqrstuvwxyz");
        cs.setHeight(30);
        cs.setWidth(140);
        cs.setWordFactory(wordFactory);
        cs.setFontFactory(fontFactory);
        cs.setColorFactory(new RandomColorFactory());
        cs.setBackgroundFactory(new SingleColorBackgroundFactory(Color.white));
        cs.setFilterFactory(filterFactories[random.nextInt(filterFactories.length)]);
        HttpSession session = request.getSession();
        if (session == null) {
            session = request.getSession();
        }
        try {
            ServletOutputStream stream = response.getOutputStream();
            String patchcaToken = EncoderHelper.getChallangeAndWriteImage(cs, "png", stream);
            session.setAttribute("patchcaToken", patchcaToken);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping(value = "/login")
    public String webLogin(HttpServletRequest request, ModelMap model, @Valid LoginDto dto, BindingResult bindingResult) throws Exception {
        String patchcaToken = "";
        String reason = null;
        if (bindingResult.hasErrors()) {
            reason = bindingResult.getFieldError().getDefaultMessage();
        } else {
            try {
                patchcaToken = (String) request.getSession().getAttribute("patchcaToken");
            } catch (Exception ex) {
                patchcaToken = "";
            }
            if (propertiesService.isAdminCheckPatchca() && (patchcaToken == null || !patchcaToken.equals(dto.getPatchca()))) {
                reason = "验证码错误";
            } else {
                UsernamePasswordToken token = new OperatorToken(dto.getTid(), dto.getUsername(), dto.getPassword());
                Subject subject = SecurityUtils.getSubject();
                try {
                    subject.login(token);
                    if (dto.isRememberMe()) subject.getSession().setTimeout(15 * 24 * 3600 * 1000);
                } catch (AuthenticationException e) {
                    reason = "用户名无效或者密码错误";
                }
                updateUserCache("manager-web");
                if (reason == null && !superAdminUser.isAdmin(dto.getUsername()) && !subject.isPermitted("admin.login")) {
                    reason = "您没有后台登录的权限！";
                }
            }
        }
        if (reason == null) {
            if (StringUtil.isNotBlank(dto.getUri())) return "redirect:" + dto.getUri();
            else return "redirect:/admin";
        } else {
            return "redirect:/admin/loginJump?reason=" + URLEncoder.encode(reason, "UTF-8");
        }
    }
    @RequestMapping(value = "/retrieve")
    public String retrieve(ModelMap model, BaseDto dto) {
        Map<String, String> img = new HashMap<>();
        TenantSetting setting = tenantService.readSetting(dto.getTid());
        img.put("pickLogin", setting.getLoginLogoUrl());
        img.put("topImage", setting.getTopImageUrl());
        img.put("loginBg", setting.getLoginBackUrl());
        model.put("img", img);
        return "/admin/frame/retrieve";
    }
    @RequiresAuthentication
    @RequestMapping(value = "/logout")
    public String webLogout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/admin/toLogin";
    }

    @ResponseBody
    @RequiresAuthentication
    @GetMapping("/menus.json")
    public Map<String, Object> getMenus(BaseDto dto) {
        Map<String, Object> result = new HashMap<>();
        User user = User.getLoginUser();
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        Collection<OperatorPrincipal> operatorPrincipals = principals.byType(OperatorPrincipal.class);
        Set<String> permissions;
        if (superAdminUser.isAdmin(user.getUsername())) {
            permissions = superAdminUser.getPermissions();
        } else if (operatorPrincipals != null && operatorPrincipals.size() > 0) {
            permissions = new HashSet<>();
            for (OperatorPrincipal principal : operatorPrincipals) {
                permissions.addAll(principal.getAuthorizationInfo().getStringPermissions());
            }
        } else {
            permissions = userService.getPrivileges(user.getTenantId(), user.getId());
        }
        Set<String> modules = new HashSet<>();
        Set<String> menus = new HashSet<>();
        for (String permission : permissions) {
            int index = permission.indexOf('.');
            if (index > 0) {
                modules.add(permission.substring(0, index));
            } else {
                menus.add(permission);
                modules.add(permission);
            }
        }
        java.util.List<MenuItem> menuItems = MenuLoader.getInstance().getMenuByModule(modules, true, true);
        java.util.List<MenuItem> validItems = new ArrayList<>();
        for (MenuItem menuItem : menuItems) {
            if (menuItem.getChildren() == null) continue;
            java.util.List<MenuItem> children = new ArrayList<>();
            for (MenuItem child : menuItem.getChildren()) {
                if (menus.contains(child.getCode())) children.add(child);
            }
            if (children.size() > 0) {
                menuItem.setChildren(children);
                validItems.add(menuItem);
            }
        }
        menuItems = validItems;
        if (!superAdminUser.isAdmin(user.getUsername())) {
            java.util.List<MenuItem> menuItems1 = tenantService.getMenus(user.getTenantId(), user.getId());
            for (MenuItem menuItem1 : menuItems1) {
                MenuItem to = null;
                for (MenuItem menuItem : menuItems) {
                    if (menuItem1.getName().equals(menuItem.getName())) {
                        to = menuItem;
                        break;
                    }
                }
                if (to == null) menuItems.add(menuItem1);
                else to.addChild(menuItem1.getChildren());
            }
        }
        //特殊处理了角色 部门和职务三个字段
        menuItems.sort(Comparator.comparing(MenuItem::getSort));
        for (MenuItem menuItem : menuItems) {
            if (menuItem.getChildren() != null) {
                menuItem.getChildren().sort(Comparator.comparing(MenuItem::getSort));
            }
        }
        result.put("modules", menuItems);
        return result;
    }
    private void updateUserCache(String deviceId) {
        Session session = SecurityUtils.getSubject().getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) return;
        String token = (String) session.getId();
        long userId = user.getId();
        String redisKey = "user-cache." + userId + "@" + deviceId;
        String old = redisOperator.get(redisKey);
        if (old != null && !old.equals(token)) sessionDAO.close(old);
        redisOperator.set(redisKey, token);
    }
}
