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
    function serviceChanged() {
        var form = $("#form1").serialize();
        window.location.href = "/common/sms?" + form;
    }
    function save() {
        var form = $("#form1").serialize();
        var showValue = $("#dept_select").find("option:selected").text();
        $.post(
                "/common/sms/setting/save",
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