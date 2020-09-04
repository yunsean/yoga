<#include "/page.component.ftl">
<@html>
    <@head includeDate=false>
    <style>
        .radio1 {
            display: inline-block;
            height: 1em;
            border-radius: 4px;
            background-color: gray;
        }
        .radio1:checked+span {
            background-color: red;
        }
    </style>
    </@head>
    <@body>
    <div id="iframeDiv">
        <div class="form-horizontal modalForm">
            <div class="modal-body">
                <form class="modalForm" id="form1">
                    <div class="form-group">
                        <label class="col-sm-2"></label>
                        <label class="col-sm-3 control-label">邮件服务器是否要求加密连接(SSL)：</label>
                        <div class="col-sm-4">
                            <label class="checkbox-inline">
                                <input class="radio1" type="radio" name="useSsl" <#if !((setting.useSsl)!false)>checked</#if> value="yes">&nbsp;是
                            </label>
                            <label class="checkbox-inline">
                                <input class="radio1" type="radio" name="useSsl" <#if !((setting.useSsl)!false)>checked</#if> value="no">&nbsp;否
                            </label>
                        </div>
                        <label class="col-sm-3"></label>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3"></label>
                        <label class="col-sm-2 control-label">发送邮件服务器地址(SMTP)	：</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="smtpServer" value="${(setting.smtpServer)!}">
                        </div>
                        <label class="col-sm-3" class="form-control"></label>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3"></label>
                        <label class="col-sm-2 control-label">服务器端口：</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="smtpPort" value="${(setting.smtpPort?c)!}">
                        </div>
                        <label class="col-sm-3" class="form-control"></label>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3"></label>
                        <label class="col-sm-2 control-label">邮件发送帐号：</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="sendAccount" value="${(setting.sendAccount)!}">
                        </div>
                        <label class="col-sm-3" class="form-control"></label>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3"></label>
                        <label class="col-sm-2 control-label">帐号密码：</label>
                        <div class="col-sm-4">
                            <input type="password" class="form-control" name="sendPassword" value="${(setting.sendPassword)!}">
                        </div>
                        <label class="col-sm-3" class="form-control"></label>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3"></label>
                        <label class="col-sm-2 control-label">邮件回复地址：</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="replyAddress" value="${(setting.replyAddress)!}">
                        </div>
                        <label class="col-sm-3" class="form-control"></label>
                    </div>
                    <hr>
                    <div class="form-group">
                        <label class="col-sm-3"></label>
                        <label class="col-sm-2 control-label">测试邮箱地址：</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="testAddress">
                        </div>
                        <div class="col-sm-3"><a class="btn btn-info" href="#" onclick="doTest()">发送测试邮件</a></div>
                    </div>
                </form>
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
            var form = $("#form1").serialize();
            $.post(
                    "/admin/setting/email/save.json",
                    form,
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
        function doTest() {
            var form = $("#form1").serialize();
            $.post(
                    "/admin/setting/email/test.json",
                    form,
                    function (data) {
                        if (data.code < 0) {
                            alertShow("warning", data.message, 3000);
                        } else {
                            alertShow("info", "发送邮件成功，请查收测试邮箱！", 3000);
                        }
                    },
                    "json"
            );
        }
    </script>
    </@body>
</@html>