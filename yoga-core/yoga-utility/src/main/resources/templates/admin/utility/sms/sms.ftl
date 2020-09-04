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
                        <label class="col-sm-2 control-label">短信网关：</label>
                        <div class="col-sm-4">
                            <select class="form-control" style="width:100%" id="service" name="service" onchange="serviceChanged()">
                                    <#list services?keys as key>
                                        <option value="${key}">${services[key]}</option>
                                    </#list>
                            </select>
                            <script>
                                $("#service").val('${param.service?default("")}');
                            </script>
                        </div>
                        <label class="col-sm-3"></label>
                    </div>
                    <#if configs??>
                        <#list configs?keys as key>
                            <div class="form-group">
                                <label class="col-sm-3"></label>
                                <label class="col-sm-2 control-label">${configs[key]}</label>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control" name="${key}" value="${values[key]?if_exists}">
                                </div>
                                <label class="col-sm-3"></label>
                            </div>
                        </#list>
                    </#if>
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
    function serviceChanged() {
        var form = $("#iframeDiv").serialize();
        window.location.href = "/admin/setting/sms?" + form;
    }
    function save() {
        var form = $("#iframeDiv").serialize();
        $.post(
                "/admin/setting/sms/save.json",
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