<#import "/macro_common.ftl" as macroCommon>
<@macroCommon.html>
<div id="iframeDiv">
    <div class="form-horizontal modalForm">
        <div class="modal-body">

            <div class="form-group">
                <div style="text-align:center">后端管理网站设置</div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">自定义登录页:</label>
                <div class="col-sm-8">
                    <input type="text" id="adminLogin" class="form-control" value="${customize.adminLogin?if_exists}">
                    <span class="help-block"><span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">自定义框架页:</label>
                <div class="col-sm-8">
                    <input type="text" id="adminIndex" class="form-control" value="${customize.adminIndex?if_exists}">
                    <span class="help-block"><span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">自定义导航页:</label>
                <div class="col-sm-8">
                    <input type="text" id="adminLeft" class="form-control" value="${customize.adminLeft?if_exists}">
                    <span class="help-block"><span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">自定义状态页:</label>
                <div class="col-sm-8">
                    <input type="text" id="adminTop" class="form-control" value="${customize.adminTop?if_exists}">
                    <span class="help-block"><span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">自定义欢迎页:</label>
                <div class="col-sm-8">
                    <input type="text" id="adminWelcome" class="form-control" value="${customize.adminWelcome?if_exists}">
                    <span class="help-block"><span>
                </div>
            </div>
            <hr>
            <div class="form-group">
                <div style="text-align:center">前台网站设置</div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">自定义主页:</label>
                <div class="col-sm-8">
                    <input type="text" id="frontIndex" name="frontIndex" class="form-control" value="${customize.frontIndex?if_exists}">
                    <span class="help-block"><span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">自定义登录页:</label>
                <div class="col-sm-8">
                    <input type="text" id="frontLogin" class="form-control" value="${customize.frontLogin?if_exists}">
                    <span class="help-block"><span>
                </div>
            </div>
        </div>
        <hr>
        <div class="box-footer" style="margin-top: 10px">
            <button class="btn btn-default col-sm-1 col-sm-offset-5" style="margin-right: 10px" onclick="save()">确认
            </button>
            <button class="btn btn-default col-sm-1" style="margin-left: 10px" onclick="window.parent.closeModal()">取消
            </button>
        </div>
    </div>
</div>

<script>
    function save() {
        var adminLogin = $("#adminLogin").val();
        var adminIndex = $("#adminIndex").val();
        var adminLeft = $("#adminLeft").val();
        var adminTop = $("#adminTop").val();
        var adminWelcome = $("#adminWelcome").val();
        var frontIndex = $("#frontIndex").val();
        var frontLogin = $("#frontLogin").val();
        $.post(
                "/api/tenant/customize/save",
                {
                    tenantId: ${tenantId?c},
                    adminLogin: adminLogin,
                    adminIndex: adminIndex,
                    adminLeft: adminLeft,
                    adminTop: adminTop,
                    adminWelcome: adminWelcome,
                    frontIndex: frontIndex,
                    frontLogin: frontLogin
                },
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