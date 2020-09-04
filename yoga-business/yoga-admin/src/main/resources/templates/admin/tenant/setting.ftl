<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<@html>
    <@head includeUploader=true>
        <script src="<@resource/>/js/jscolor.js" charset="UTF-8"></script>
    </@head>
    <@body>
        <form id="iframeDiv">
            <div class="form-horizontal modalForm">
                <div class="modal-body">
                    <@inputHidden name="tenantId" value="${tenantId?c}" />
                    <@formText name="platform" label="平台名称：" value="${(setting.platformName)!'YOGA管理平台'}"/>
                    <@formText name="footer" label="页脚文字：" value="${(setting.footerRemark)!'Copyright @ 2017 YOGA管理平台'}"/>
                    <@formText name="adminIcon" label="网站图标：" value="${(setting.adminIcon)!'/admin/login/favicon.ico'}"/>
                    <hr>
                    <@formText name="resource" label="静态资源前缀：" value="${(setting.resourcePrefix)!}"/>
                    <@formImage name="loginbg" label="登录页背景图：" editable=true value="${(setting.loginBackUrl)!'/admin/login/back.jpg'}"/>
                    <@formImage name="loginlogo" label="登录页主题图：" editable=true value="${(setting.loginLogoUrl)!'/admin/login/pick.png'}"/>
                    <@formImage name="topimage" label="页面顶部图：" editable=true value="${(setting.topImageUrl)!'/admin/login/top.png'}"/>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">菜单背景颜色：</label>
                        <div class="col-sm-8" style="display:flex; ">
                            <input onchange="onColorChanged(this)" style="background-color: white; flex:1; display: inline-block; text-align: center; " type="text" class="form-control" name="menuColor" id="menu-color" value="${(setting.menuColor)!'364150'}" />
                            <div style="display: inline-block; margin-left: 15px; width: 32px; height: 32px; background-color: #${(setting.menuColor)!'364150'}" id="menu-color-demo"></div>
                            <button style="width: 98px; margin-left: 15px; display: inline-block" type="button"
                                    class="btn btn-primary jscolor {valueElement:'chosen-value', onFineChange:'setTextColor(this)'}">
                                <i class="icon icon-tasks"></i>选择
                            </button>
                        </div>
                    </div>
                </div>
                <hr>
                <div class="box-footer">
                    <a href="#" class="btn btn-default col-sm-1 col-sm-offset-5" style="margin-right: 10px" onclick="save()">确认</a>
                    <a class="btn btn-default col-sm-1" style="margin-left: 10px" onclick="window.parent.closeModal()">取消</a>
                </div>
            </div>
        </form>
    </@body>

<script>
    function onColorChanged(elem) {
        var color = $("#menu-color").val();
        $("#menu-color-demo").css("background-color", "#" + color);
    }
    function setTextColor(picker) {
        $("#menu-color").val(picker.toString());
        $("#menu-color-demo").css("background-color", '#' + picker.toString());
    }
    function save() {
        var json = $("#iframeDiv").serialize();
        $.post(
                "/admin/tenant/tenant/setting/save.json",
                json,
                function (data) {
                    if (data.code < 0) {
                        alertShow("warning", data.message, 3000);
                    } else {
                        window.parent.closeModal();
                    }
                },
                "json"
        );
    }
</script>
</@html>