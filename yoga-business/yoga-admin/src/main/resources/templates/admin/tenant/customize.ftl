<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<@html>
    <@head>
    </@head>
    <@body>
        <form id="iframeDiv">
            <div class="form-horizontal modalForm">
                <div class="modal-body">
                    <div class="form-group">
                        <div style="text-align:center">后台网站设置</div>
                    </div>
                    <@inputHidden name="tenantId" value="${tenantId?c}" />
                    <@formText name="adminLogin" label="自定义登录页：" value="${(customize.adminLogin)!}"/>
                    <@formText name="adminIndex" label="自定义框架页：" value="${(customize.adminIndex)!}"/>
                    <@formText name="adminLeft" label="自定义导航页：" value="${(customize.adminLeft)!}"/>
                    <@formText name="adminTop" label="自定义状态页：" value="${(customize.adminTop)!}"/>
                    <@formText name="adminWelcome" label="自定义欢迎页：" value="${(customize.adminWelcome)!}"/>
                    <hr>
                    <div class="form-group">
                        <div style="text-align:center">前台网站设置</div>
                    </div>
                    <@formText name="frontIndex" label="自定义主页：" value="${(customize.frontIndex)!}"/>
                    <@formText name="frontLogin" label="自定义登录页：" value="${(customize.frontLogin)!}"/>
                </div>
                <hr>
                <div class="box-footer">
                    <a href="#" class="btn btn-default col-sm-1 col-sm-offset-5" style="margin-right: 10px" onclick="save()">确认
                    </a>
                    <button class="btn btn-default col-sm-1" style="margin-left: 10px" onclick="window.parent.closeModal()">取消
                    </button>
                </div>
            </div>
        </form>
    </@body>

<script>
    function save() {
        var json = $("#iframeDiv").serialize();
        $.post(
                "/admin/tenant/tenant/customize/save.json",
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