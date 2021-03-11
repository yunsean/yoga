# YOGA简易多租户平台框架



### yoga-core

#### yoga-common	

一些基础类库，常用的工具类

* base：	BaseCache, BaseController, BaseDto, BaseEnum, BaseService, BaseVo
* data：    ApiResult, ApiResults, ChainMap, CommonMessage, CommonPage, MapConverter, PropertiesLoader, ResourceLoader, ResultConstants
* json：    DateJsonDeserializer, DateJsonSerializer, DateTimeJsonDeserializer, DateTimeJsonSerializer, DateYMDHMJsonDeserializer, DateYMDHMJsonSerializer, TimeJsonDeserializer, TimeJsonSerializer
* mybatis： IntListTypeHandler, LocalDateTypeHandler, LongArrayTypeHandler, MapperQuery, MyMapper, StringListTypeHandler
* utils：   AesUtil, ArrayUtil, AssertUtil, ClassUtil, CryptUtil, DateUtil, DateUtils, DefaultUtil, EnumUtil, FreemarkerUtil, HttpUtils, JsonUtil, MapUtil, NumberUtil, ObjectUtil, PinyinUtil, SqlBuilder, StringUtil, TypeCastUtil

#### yoga-logging   

基于AOP的日志库，通过注解方式自动记录业务日志

​	日志注解：

```java
@Logging(module = ModuleName, description = "添加租户", primaryKeyIndex = -1, excludeArgs = 5, argNames = "租户名称，租户编码，租户描述，模板ID，管理员账号，，管理员昵称，管理员手机号")
public long add(String name, String code, String remark, Long templateId, String username, String password, String nickname, String mobile) throws Exception {
...
```

​	日志显示主键查询：

```java
@LoggingPrimary(module = TenantService.ModuleName, name = "租户管理")
public class TenantService extends BaseService implements LoggingPrimaryHandler {

	public final static String ModuleName = "gbl_tenant";
		@Override
	public String getPrimaryInfo(Object primaryId) {
		Tenant tenant = tenantMapper.selectByPrimaryKey(primaryId);
		if (tenant == null) return null;
		return tenant.getName();
	}
	...
```

#### yoga-setting    

基于Spring扫描的系统配置项管理，通过注解方式自动发现注解

​	单行（无配置界面）增加设置项：

```java
@Settable(module = WxmpService.ModuleName, key = WxmpService.Key_AppState, name = "微信小程序-小程序发布状态", placeHolder = "developer为开发版；trial为体验版；formal为正式版")
public class WeixinappController extends BaseController {
```

​	有配置页面的设置项：

```java
@Settable
public class CaptchaController extends BaseController {

    @ApiIgnore
    @Settable(module = CaptchaService.ModuleName, key = CaptchaService.Key_Config, name = "通知设置-短信验证码设置")
    @GetMapping("")
    public String showSetting(ModelMap model, @Valid BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        CaptchaSetting setting = captchaService.getSetting(dto.getTid());
        if (setting == null) setting = new CaptchaSetting();
        if (StringUtil.isBlank(setting.getFormat())) setting.setFormat("您的验证码是：#code#，#time#内有效。如非您本人操作，请忽略本消息。");
        model.put("setting", setting);
        return "/admin/utility/captcha/setting";
    }
    
    ...
```

​	保存读取配置项：

```java
    @Autowired
    private SettingService settingService;
    public final static String ModuleName = "gcf_weixinapp";
    public final static String Key_Setting = "weixinapp.setting";
    public final static String Key_AppState = "weixinapp.state";
    public SettingConfig getSetting(long tenantId) {
        return settingService.get(tenantId, ModuleName, Key_Setting, SettingConfig.class);
    }
    public void saveSetting(long tenantId, SettingConfig config) {
        settingService.save(tenantId, ModuleName, Key_Setting, JSONObject.toJSONString(config), config.getAppId());
    }
    public String getAppState(long tenantId) {
        return settingService.get(tenantId, ModuleName, Key_AppState, "formal");
    }
```

#### yoga-utility  

 工具模块库

* aliyun  阿里云身份证识别模块

* captcha  短信验证码模块，可通过系统配置项设置发送短信模板，以及是否模拟发送（不真实发送，直接返回验证码）

* district  中国省市区三级地理信息模块

* feie  飞鹅云打印机对接模块

* image   基于image magick的图片压缩模块

* mail   邮件发送模块，通过系统配置项配置发送邮箱信息

* push  推送框架模块，插件模型，可对接极光等推送

* qr  二维码模块，生成二维码图形，事件关联二维码等，扫码执行任务

* quartz   定时器模块，有独立的配置页面

  ​	注册定时任务方法：

  ```java
  @PostConstruct
  public void addQuartz() {
      quartzService.add(new QuartzTask(SendSubscribeService.class, ModuleName, "发送微信通知", "*/10 * * * * ?"));
  }
  ```

* sms  短信发送模块，插件模型，可以对接阿里云短信、云片短信平台等

* uploader  文件上传模块，文件上传建议全部通过该接口，在业务部分只需要记录文件的ID即可

#### yoga-excelkit  

 Excel导出模块，基于https://github.com/wenzewoo/ExcelKit，致谢

#### yoga-ueditor  

 UEditor网页编辑工具，基于百度UEditor并做了相应适配，致谢

#### yoga-resource   

静态资源模块，后端管理系统中的各种静态资源，含：echarts、jquery、pdf.js、zui、zTree以及一些自定义样式定义，致谢各开源机构和个人

​	在实际部署中，可以将该模块中的静态资源通过独立nginx服务器进行部署，并做好跨域配置，实现动静态分离

#### yoga-tenant  

 基于域名映射的多租户框架模块

* menu   菜单管理

  应用启动时，将自动扫描包内所有的menu.xml文件，然后对所有的菜单进行合并，形成管理后台左侧的菜单输，然后根据每个租户开通的模块，自动筛选具有的菜单进行展示

* TemplateService  租户模板

  通过租户模块可以快速建立租户，租户模板将保存租户具有的模块、自定义菜单，基本配置信息等

* TenantService  租户管理

  可以全手动建立租户或者通过租户模板建立租户，租户建立将确定租户具有的模块、自定菜单、通过租户全局配置可以配置租户的管理网站登录页面样式（更换背景图、标题图、登录框左侧图、网站名称等）。

  当导入数据库后第一次启动时，将自动建立0租户（系统租户），只有系统租户中可以增减配置其他租户。

#### yoga-operator  

 管理账户配置模块

* role   角色管理模块
  * 系统基本基于RBAC权限框架，创建租户后，首先建立角色，角色中将配置角色可以访问的模块（管理页面是否显示对应的菜单）以及模块中的各个权限（操作时鉴权，基于Shiro权限框架）
  * 可以将角色绑定到部门、职级、用户三个对象上，最终登录用户将拥有上述三者的权限并集，本系统暂不支持权限黑名单机制
* branch  部门管理
  * 部门按照树形结构进行管理，无层级限制
* duty  职级管理
  * 从设计意图上说，职级和部门属于两个维度的管理体制（横向、纵向）
  * 职级存在级别（level），添加职级时按照level进行排序添加，业务系统可使用level进行某些场景的排序（领导排在前面啦）
* user  用户管理
  * 用户可以数据部门和职级，也可以不属于，而直接赋予角色。

### freemark封装

在yoga-core/yoga-common中对freemarker进行了一系列封装，以减少编写freemarker时的代码量，同时规格化页面中各个页面的显示风格。

#### page.component.ftl

管理页面基础框架，包含页面头定义，脚注定义，框架定义等，典型页面使用：

``` xml
<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<#setting number_format="##0.##">
<@html>
    <@head includeDate=true includeUploader=true>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="权限管理" icon="icon-user">
            <@crumbItem href="#" name="用户列表" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "用户列表" />
                <@panelBody>
                    <@inlineForm class="margin-b-15">
                        <@formLabelGroup class="margin-r-15" label="所属部门">
                        </@formLabelGroup>
                        <@inputSubmit text="搜索" icon="icon icon-search" class="btn btn-success"/>
                        <@rightAction>
                            <@shiro.hasPermission name="admin_user.add" >
                                <@inputButton text="添加" icon="icon-plus" class="btn btn-primary" onclick="doAdd();" />
                            </@shiro.hasPermission>
                        </@rightAction>
                    </@inlineForm>
                    <@table>
                        <@thead>
                            <@tr>
                                <@th 7>用户名</@th>
                                ...
                                <@th 15 true>操作</@th>
                            </@tr>
                        </@thead>
                        <@tbody>
                          <#list users as operator>
                              <@tr>
                                  <@td>${operator.username!}</@td>
                                  ...
                                  <@td true>
                                      <@shiro.hasPermission name="admin_user.update" >
                                          <a href="javascript:void(0)" onclick="doEdit(${(operator.id?c)!})" class="btn btn-sm btn-info">
                                              <i class="icon icon-edit"></i>
                                              编辑
                                          </a>
                                      </@shiro.hasPermission>
                                  </@td>
                              </@tr>
                          </#list>
                        </@tbody>
                    </@table>
                </@panelBody>
                <@panelPageFooter action="/admin/operator/user/list" />
            </@panel>
        </@bodyContent>
    </@bodyFrame>

<script>
    ...
</script>
</@html>
```

#### form.component.ftl

表单封装，比如inputForm

#### input.component.ftl

常用表单控件，包含input和form两种前缀，比如inputDate代表一个单纯的日期输入空间，而formDate则代表Label和控件的单行组合，通过栅格布局实现了各个控件的显示统一

#### modal.component.ftl

弹出框（比如信息编辑）控件，典型使用：

``` xml
    <@modal title="用户编辑" showId="userAddButton" onOk="saveOperator" width=75>
        <@inputHidden name="id" id="edit_id"/>
        <div class="form-group">
            <label class="col-sm-offset-1 col-sm-1 control-label">用户名：</label>
            <div class="col-sm-4">
                <@inputText name="username"/>
            </div>
            <label class="col-sm-1 control-label">真实姓名：</label>
            <div class="col-sm-4">
                <@inputText name="nickname"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-offset-1 col-sm-1 control-label">手机号：</label>
            <div class="col-sm-4">
                <@inputText name="mobile"/>
            </div>
            <label class="col-sm-1 control-label">Email：</label>
            <div class="col-sm-4">
                <@inputText name="email"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-offset-1 col-sm-1 control-label">设置密码：</label>
            <div class="col-sm-4">
                <@inputPassword name="password" id="password" />
            </div>
            <#if branches?? && (((branches?size)!0) gt 0)>
                <label class="col-sm-1 control-label">所属部门：</label>
                <div class="col-sm-4">
                    <select class="form-control" name="branchId" id="branchId">
                        <option value="0">未指定</option>
                        <#list branches! as root>
                            <@m1_columns root 0 root_index/>
                        </#list>
                    </select>
                </div>
            </#if>
        </div>
        <div class="form-group">
            <label class="col-sm-offset-1 col-sm-1 control-label">确认密码：</label>
            <div class="col-sm-4">
                <@inputPassword class="col-sm-4" id="repwd"  />
            </div>
            <#if roles?? && (((branches?size)!0) gt 0)>
                <label class="col-sm-1 control-label">所属职级：</label>
                <div class="col-sm-4">
                    <@inputList options=duties! name="dutyId" blank="未指定" blankValue="0"/>
                </div>
            </#if>
        </div>
        <div class="form-group">
            <label class="col-sm-offset-1 col-sm-1 control-label">赋予角色：</label>
            <input type="hidden" name="roleIds" value="0">
            <div class="col-sm-8">
                <@inputCheckboxGroup options=roles! name="roleIds"/>
            </div>
        </div>
    </@modal>

<script>
    function doAdd() {
        $("#add_form")[0].reset();
        $("#add_form input[name='id']").val(0);
        $("#add_modal").modal("show");
    }
    function doEdit(id) {
        $("#add_form")[0].reset();
        $.get(
                "/admin/operator/user/get.json?id=" + id,
                function (data) {
                    if (parseInt(data.code) < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#add_form input[name='id']").val(id);
                        $("#add_modal").modal("show");
                    }
                }
        );
    }
</script>
```

#### paging.component.ftl

分页控件，列表分页，固定格式：

```xml
<@panelPageFooter action="/admin/operator/user/list" />
```

只需要替换action中的路径为controller的列表RequstMpping，controller中的ModelMap中需要增加page，形如：

```java
model.put("users", users.getList());
model.put("page", new CommonPage(users));
```

#### table.component.ftl

表格控件，定义了tr th td宏，典型的如：

``` xml
<@table>
  <@thead>
    <@tr>
      <@th 7>用户名</@th>
      ...
      <@th 15 true>操作</@th>
      </@tr>
    </@thead>
  <@tbody>
    <#list users as operator>
      <@tr>
        <@td>${operator.username!}</@td>
        ...
        <@td true>
          <@shiro.hasPermission name="admin_user.update" >
            <a href="javascript:void(0)" onclick="doEdit(${(operator.id?c)!})" class="btn btn-sm btn-info">
              <i class="icon icon-edit"></i>
              编辑
            </a>
            </@shiro.hasPermission>
          </@td>
        </@tr>
      </#list>
    </@tbody>
```

### yoga-business

#### yoga-admin  

后端管理网站模块

其实就是yoga-core各个模块的管理网站controller

* aggregate：信息聚合，通过类接口注册每个模块中哪些信息需要聚合，聚合信息将用于后台管理网站首屏显示以及API中通过接口获取（比如平台订单总量，订单总额等等信息）

* branch：部门管理，配合yoga-operator

* duty：职级管理，配合yoga-operator

* role：角色管理，配合yoga-operator

* user：用户管理，配合yoga-operator

* frame：管理网站框架，包含登录页以及iframe模式的主页（左侧菜单，菜单树显示）

* freemarker：Freemarker标签页组件，实现管理网站脚注正确显示租户的名称，提供以下标签支撑：

  ```xml
  <center><strong><@tenantTags tag="footer"/></strong></center>
  <center><strong><@tenantTags tag="footer"/></strong></center>
  <@tenantTags tag="resource"/>
  ```

  特别的，resource前缀用于实现静态资源分离，配合yoga-resource的移除，实现动静分离

* logging：系统业务日志管理页面，配合yoga-logging

* quartz：定时任务管理页面，配合yoga-utility

* setting：系统配置管理页面，配合yoga-setting

* template：租户模版管理页面，配合yoga-tenant

* tenant：租户管理管理页面，配合yoga-tenant

* config：基于Swagger API管理的配置，配合shiro模块实现API页面的鉴权

* shiro：基于Shiro的后台管理网站权限框架

  * freemarker：针对freemarker使用的shiro扩展，在freemarker页面中使用形如：

    ```xml
        <@shiro.hasPermission name="admin_branch.add" >
            <a href="javascript:void(0)" onclick="doAdd(${(branch.id?c)!})" class="btn btn-sm btn-info">
                <i class="icon icon-plus"></i>添加
            </a>
        </@shiro.hasPermission>
    ```

    页面将自动根据当前账号是否具有admin_branch.add而显示隐藏添加按钮

  * OperatorRealm、OperatorPrincipal、OperatorToken、ShiroXMLReader、ShiroConfiguration：Shiro基础框架

  * OperatorShiroFilter：Shiro全局过滤器，实现通过域名映射到租户（也可以通过http header中tid明确指定租户，但当用户登录后，将优先使用当前登录用户信息中的租户ID），租户ID将在BaseDto中统一传递到controller，在controller中可以通过如下代码获取当前登录用户：

    ```java
    User user = User.getLoginUser();
    ```

    如果系统中存在多个shiro框架（比如前台一个，后台一个），则可以通过如下代码获取统一的LoginUser：

    ```java
    LoginUser loginUser = (LoginUser) subject.getSession().getAttribute("user");
    ```

    需要注意，自定义shiro（比如前台）中需要在Realm中配置user属性，形如：

    ```java
    Session session = SecurityUtils.getSubject().getSession();
    session.setAttribute("user", user);
    session.setAttribute("permissions", info.getStringPermissions());
    ```

  * MultiRealmAuthenticator：为框架支持多realm shiro提供支撑

#### yoga-content 

内容管理模块

​	基于mongodb进行文档管理的内容管理模块，主要提供栏目结构化（字段设置）和在线文档编辑以及发布模板（不怎么好用）。

* article：基于Mongodb以及mysql字段设置的文档管理
* column：栏目管理，定义栏目树
* comment：评论管理
* property：选项字段选项值管理，比如地区列表
* template：模板管理，每个栏目中的文章结构（包含哪些字段，字段类型）

#### yoga-moment

朋友圈模块

#### yoga-weixinapp

微信小程序接口

#### yoga-points

积分管理，但是这个积分是针对后台用户的，通过类接口方式注册系统的积分项目

### yoga-push

#### yoga-push-jiguang

极光推送，实现了yoga-utility中的push，可以按照此例子支持更多的推送方案

### yoga-sms

#### yoga-sms-aliyun

阿里云短信发送平台

#### yoga-sms-eucpsms

亿美短信平台

#### yoga-sms-massms

中国移动短信网关平台

#### yoga-sms-yunpian

云片短信平台

### maven仓库

​	可通过如下maven以来快速使用上边的模块（未列举部分自行替换即可）

```
implementation 'com.github.yunsean.yoga:yoga-common:1.4.0.1'
implementation 'com.github.yunsean.yoga:yoga-logging:1.4.0.1'
implementation 'com.github.yunsean.yoga:yoga-operator:1.4.0.1'
implementation 'com.github.yunsean.yoga:yoga-resource:1.4.0.1'
implementation 'com.github.yunsean.yoga:yoga-tenant:1.4.0.1'
implementation 'com.github.yunsean.yoga:yoga-utility:1.4.0.1'
implementation 'com.github.yunsean.yoga:yoga-push-jiguang:1.4.0.1'
implementation 'com.github.yunsean.yoga:yoga-sms-aliyun:1.4.0.1'
implementation 'com.github.yunsean.yoga:yoga-sms-yunpian:1.4.0.1'
implementation 'com.github.yunsean.yoga:yoga-admin:1.4.0.1'
```

### 其他

​		master分支为新拆分出的v1.4版本，老版本请切换到v1.0分支。

​		若有问题，欢迎联系：yunsean@163.com
