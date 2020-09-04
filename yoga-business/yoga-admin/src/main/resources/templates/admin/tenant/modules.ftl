<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<@html>
    <@head includeDate=true>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="${tenantAlias!''}管理" icon="icon-user">
            <@crumbItem href="#" name="${tenantAlias!''}管理" backLevel=1/>
            <@crumbItem href="#" name="${tenantAlias!''}模块" />
            <@backButton/>
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "${tenantAlias!''}模块" />
                <@panelBody>
                    <@table id="" class="table table-bordered ">
                        <@thead>
                            <@tr>
                                <@th 10>版块</@th>
                                <@th 15>模块</@th>
                                <@th 30>路径</@th>
                                <@th 45>备注</@th>
                            </@tr>
                        </@thead>
                        <@tbody>
                            <#list modules! as menu>
                                <tr style="background-color:#eaeaea">
                                    <@td><b style="font-size:14px">${(menu.name)!}</b></@td>
                                    <@td></@td>
                                    <@td></@td>
                                    <@td></@td>
                                </tr>
                                <#list menu.children! as child>
                                    <@tr>
                                        <@td></@td>
                                        <@td>
                                            <label>
                                                <input name="${(child.code)!}" type="checkbox" <#if child.checked>checked</#if> value="">&nbsp;&nbsp;${child.name?if_exists}
                                            </label>
                                        </@td>
                                        <@td>${(child.url)!}</@td>
                                        <@td>${(child.remark)!}</@td>
                                    </@tr>
                                </#list>
                            </#list>
                        </@tbody>
                    </@table>
                </@panelBody>
                <div class="box-footer">
                    <div class="col-sm-5 col-sm-offset-5">
                        <button class="btn btn-default" onclick="saveModule()">保存</button>
                        <button class="btn btn-default" onclick="javascript:history.back(-1)">取消</button>
                    </div>
                </div>
            </@panel>
        </@bodyContent>
    </@bodyFrame>
<script>
    function saveModule() {
        var modules = [];
        var total = $("input[type='checkbox']").length;
        var selected = $("select").length;
        for (var i = 0; i < total; i++) {
            if ($("input[type='checkbox']").eq(i).prop("checked")) {
                modules.push($("input[type='checkbox']").eq(i).attr("name"));
            }
        }
        $.post(
                "/admin/tenant/tenant/module/set.json",
                $.param({modules: modules, tenantId: ${tenantId?c}}, true),
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        history.back(-1);
                    }
                },
                "json"
        );
    }
</script>
</@html>