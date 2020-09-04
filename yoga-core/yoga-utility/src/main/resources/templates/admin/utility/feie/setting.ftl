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
                    <@formText name="sn" label="打印机编号：" value="${(config.sn)!}"/>
                    <@formText name="key" label="打印机识别码：" value="${(config.key)!}"/>
                    <@formText name="name" label="打印机别名：" value="${(config.name)!}"/>
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
                "/admin/utility/feie//setting/save.json",
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