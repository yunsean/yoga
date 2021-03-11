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
        <div id="iframeDiv">
            <div class="form-horizontal modalForm">
                <div class="modal-body">
                    <form class="modalForm" id="form1">
                        <div class="form-group">
                            <label class="col-sm-3"></label>
                            <label class="col-sm-2 control-label">小程序ID：</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" name="appId" value="${(setting.appId)!}">
                            </div>
                            <label class="col-sm-3"></label>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3"></label>
                            <label class="col-sm-2 control-label">小程序密钥：</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" name="appSecret" value="${(setting.appSecret)!}">
                            </div>
                            <label class="col-sm-3"></label>
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
    </@body>

    <script>
        function save() {
            var form = $("#form1").serialize();
            $.post(
                "/admin/weixinapp/setting/save.json",
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