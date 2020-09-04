<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<@html>
    <@head includeUploader=true>
    </@head>
    <@body>
        <form id="iframeDiv">
            <div class="form-horizontal modalForm">
                <div class="modal-body">
                    <div class="form-group">
                        <label class="col-sm-3"></label>
                        <label class="col-sm-2 control-label">验证码长度：</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="length" value="${setting.length?default(4)}">
                        </div>
                        <label class="col-sm-3"></label>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3"></label>
                        <label class="col-sm-2 control-label">短信失效时间（秒）：</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="expire" value="${setting.expire?default(60)}">
                        </div>
                        <label class="col-sm-3" class="form-control"></label>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3"></label>
                        <label class="col-sm-2 control-label">发送最小间隔（秒）：</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="interval" value="${setting.interval?default(0)}">
                        </div>
                        <label class="col-sm-3" class="form-control"></label>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3"></label>
                        <label class="col-sm-2 control-label">APP自动填充：</label>
                        <div class="col-sm-4">
                            <input type="checkbox" style="margin-top: 10px" name="autofill" <#if setting.autofill?if_exists>checked</#if>>
                        </div>
                        <label class="col-sm-3"></label>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3"></label>
                        <label class="col-sm-2 control-label">信息格式：</label>
                        <div class="col-sm-4">
                            <div class="small" style="margin-top: 7px; margin-bottom: 5px">请使用#code#表示验证码占位[必须]，#time#表示失效时间占位！</div>
                            <textarea type="text" class="form-control" name="format" style="min-height: 100px;">${setting.format?if_exists}</textarea>
                        </div>
                        <label class="col-sm-3"></label>
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
        function save() {
            var form = $("#iframeDiv").serialize();
            $.post(
                "/admin/setting/captcha/save.json",
                form,
                function (data) {
                    if (data.code < 0) {
                        alertShow("warning", data.message, 3000);
                        return;
                    } else {
                        window.parent.closeModal();
                    }
                },
                "json"
            );
        }
    </script>
</@html>
