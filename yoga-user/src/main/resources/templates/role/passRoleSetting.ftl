<#import "/macro_common.ftl" as macroCommon>
<@macroCommon.html>
<div id="iframeDiv">
    <div class="form-horizontal modalForm">
        <div class="modal-body">
            <div class="modalForm">
                <div class="form-group">
                    <label class="col-sm-3"></label>
                    <label class="col-sm-2 control-label">默认注册团队的<@common.deptAlias/>：</label>
                    <div class="col-sm-4">
                        <select class="form-control" style="width:100%" id="role_select">
                            <option value="0">禁止注册</option>
                            <#list roles as root>
                                <@m1_columns root 0 root_index/>
                            </#list>
                        </select>
                        <script>
                            $("#role_select").val('${param.value?if_exists?c}');
                        </script>
                    </div>
                    <label class="col-sm-3"></label>
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

    <#macro m1_column column level index>
    <option value="${column.id?if_exists?c}">
        <#list 0..level as x>
            <#if x < level>
                │&nbsp;&nbsp;
            <#else>
                ├
            </#if>
        </#list>
    ${column.name?if_exists}
    </option>
    </#macro>
    <#macro m1_columns columns level index>
        <@m1_column columns level index/>
        <#local level1 = level + 1/>
        <#if columns.children??>
            <#list columns.children as sub>
                <@m1_columns sub level1 sub_index/>
            </#list>
        </#if>
    </#macro>

<script>
    function save() {
        var value = $("#role_select").val();
        var showValue = $("#role_select").find("option:selected").text();
        $.post(
                "/api/role/setting/pass/save",
                {roleId: value, roleName: showValue},
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
</@macroCommon.html>