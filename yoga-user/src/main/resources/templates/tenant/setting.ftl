<#import "/macro_common.ftl" as macroCommon>
<@macroCommon.html>
<div id="iframeDiv">
    <div class="form-horizontal modalForm">
        <div class="modal-body">
            <div class="form-group">
                <label class="col-sm-3 control-label">平台名称:</label>
                <div class="col-sm-8">
                    <input type="text" name="platform" id="platform" class="form-control" value="${setting.platformName?if_exists}">
                    <span class="help-block"><span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">页脚文字:</label>
                <div class="col-sm-8">
                    <input type="text" name="footer" id="footer" class="form-control" value="${setting.footerRemark?if_exists}">
                    <span class="help-block"><span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">登录页背景图:</label>
                <div class="col-sm-8">
                    <input type="text" name="loginbg" id="loginbg" class="form-control" value="${setting.loginBackUrl?if_exists}">
                    <span class="help-block"><span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">登录页主题图:</label>
                <div class="col-sm-8">
                    <input type="text" name="loginlogo" id="loginlogo" class="form-control" value="${setting.loginLogoUrl?if_exists}">
                    <span class="help-block"><span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">页面顶部图:</label>
                <div class="col-sm-8">
                    <input type="text" name="topimage" id="topimage" class="form-control" value="${setting.topImageUrl?if_exists}">
                    <span class="help-block"><span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">页面顶部图:</label>
                <div class="col-sm-8">
                    <input type="text" name="topimage" id="topimage" class="form-control" value="${setting.topImageUrl?if_exists}">
                    <span class="help-block"><span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">静态资源前缀:</label>
                <div class="col-sm-8">
                    <input type="text" name="resource" id="resource" class="form-control" value="${setting.resourcePrefix?if_exists}">
                    <span class="help-block"><span>
                </div>
            </div>
            <hr>
            <div class="form-group">
                <label class="col-sm-3 control-label">角色别名:</label>
                <div class="col-sm-8">
                    <input type="text" name="role" id="role" class="form-control" value="${setting.roleAlias?if_exists}">
                    <span class="help-block"><span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">部门别名:</label>
                <div class="col-sm-8">
                    <input type="text" name="dept" id="dept" class="form-control" value="${setting.deptAlias?if_exists}">
                    <span class="help-block"><span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">职级别名:</label>
                <div class="col-sm-8">
                    <input type="text" name="duty" id="duty" class="form-control" value="${setting.dutyAlias?if_exists}">
                    <span class="help-block"><span>
                </div>
            </div>
        </div>
        <hr>
        <div class="box-footer" style="margin-top: 10px">
            <button class="btn btn-default col-sm-1 col-sm-offset-5" style="margin-right: 10px" onclick="save()">确认</button>
            <button class="btn btn-default col-sm-1" style="margin-left: 10px" onclick="window.parent.closeModal()">取消</button>
        </div>
    </div>
</div>

<script>
    function save() {
        var platform = $("#platform").val();
        var footer = $("#footer").val();
        var loginbg = $("#loginbg").val();
        var loginlogo = $("#loginlogo").val();
        var topimage = $("#topimage").val();
        var resource = $("#resource").val();
        var role = $("#role").val();
        var dept = $("#dept").val();
        var duty = $("#duty").val();
        var pageSize = $("#pageSize").val();
        $.post(
                "/api/tenant/setting/save",
                {tenantId: ${tenantId?c}, platform: platform, footer: footer, loginbg: loginbg, loginlogo: loginlogo, topimage: topimage, resource: resource,
                    role: role, dept: dept, duty: duty, pageSize: pageSize},
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
</@macroCommon.html>