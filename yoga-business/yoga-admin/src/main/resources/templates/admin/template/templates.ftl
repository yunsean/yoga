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
            <@crumbItem href="#" name="${tenantAlias!''}模板" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "${tenantAlias!''}模板" />
                <@panelBody>
                    <@inlineForm class="margin-b-15">
                        <@formLabelGroup class="margin-r-15" label="名称：">
                            <@inputText name="name" value="${(param.name)!}"/>
                        </@formLabelGroup>
                        <@inputSubmit text="搜索" icon="icon icon-search" class="btn btn-success"/>
                        <@rightAction>
                            <@shiro.hasPermission name="gbl_tenant_template.add" >
                                <@inputButton text="添加" icon="icon-plus" class="btn btn-primary" onclick="doAdd();" />
                            </@shiro.hasPermission>
                        </@rightAction>
                    </@inlineForm>
                    <@table>
                        <@thead>
                            <@tr>
                                <@th 5 true>#</@th>
                                <@th 20>名称</@th>
                                <@th 40>备注</@th>
                                <@th 35 true>操作</@th>
                            </@tr>
                        </@thead>
                        <@tbody>
                            <#list templates! as tenant>
                            <@tr>
                                <@td true>${(tenant.id?c)!}</@td>
                                <@td>${(tenant.name)!}</@td>
                                <@td>${(tenant.remark)!}</@td>
                                <@td true>
                                <@shiro.hasPermission name="gbl_tenant_template.update" >
                                    <a href="javascript:void(0)" onclick="doEdit(${(tenant.id?c)!})"
                                       class="btn btn-sm btn-success permissions"><i class="icon icon-pencil-square-o"></i>&nbsp;编辑
                                    </a>
                                    <a href="/admin/tenant/template/modules?templateId=${(tenant.id?c)!}"
                                       class="btn btn-sm btn-primary permissions"><i class="icon icon-eye-slash"></i>&nbsp;模块
                                    </a>
                                    <a href="/admin/tenant/template/menus?templateId=${(tenant.id?c)!}"
                                       class="btn btn-sm btn-primary permissions"><i class="icon icon-check-square-o"></i>&nbsp;自定义菜单
                                    </a>
                                    <a href="/admin/tenant/template/settings?templateId=${(tenant.id?c)!}"
                                       class="btn btn-sm btn-primary permissions"><i class="icon icon-pencil"></i>&nbsp;配置
                                    </a>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="gbl_tenant_template.del" >
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <a href="javascript:void(0)" onclick="doDelete(${tenant.id?default(0)?c})"
                                       class="btn btn-sm btn-danger"><i class="icon icon-remove"></i>&nbsp;删除</a>
                                </@shiro.hasPermission>
                                </@td>
                            </@tr>
                            </#list>
                        </@tbody>
                    </@table>
                </@panelBody>
                <@panelPageFooter action="/admin/logging/list" />
            </@panel>
        </@bodyContent>
    </@bodyFrame>

<script>
    function doDelete(templateId) {
        warningModal("确定要删除该模板吗?", function(){
            $.ajax({
                url: "/admin/tenant/template/delete.json?id=" + templateId,
                type: 'DELETE',
                success: function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        window.location.reload();
                    }
                }
            });
        });
    }
</script>

    <@modal title="${tenantAlias!''}模板编辑" onOk="doSave">
        <@inputHidden name="id" id="edit_id"/>
        <@formText name="name" label="${tenantAlias!''}名称：" />
        <@formTextArea name="remark" label="备注信息：" />
    </@modal>
    <script>
        function doAdd() {
            $("#add_form")[0].reset();
            $("#add_form input[name='id']").val(0);
            $("#add_modal").modal("show");
        }
        function doEdit(id) {
            $("#add_form")[0].reset();
            $.get(
                    "/admin/tenant/template/get.json?id=" + id,
                    function (data) {
                        if (parseInt(data.code) < 0) {
                            alertShow("danger", data.message, 3000);
                        } else {
                            $("#add_form input[name='id']").val(id);
                            $("#add_form input[name='name']").val(data.result.name);
                            $("#add_form textarea[name='remark']").val(data.result.remark);
                            $("#add_modal").modal("show");
                        }
                    }
            );
        }
        function doSave() {
            var id = $("#edit_id").val();
            var json = $("#add_form").serializeArray();
            $.post(id == 0 ? "/admin/tenant/template/add.json" : "/admin/tenant/template/update.json",
                    json,
                    function (data) {
                        if (data.code < 0) {
                            alertShow("warning", data.message, 3000);
                        } else {
                            $("#add_modal").modal("hide");
                            window.location.reload();
                        }
                    },
                    "json"
            );
        }
    </script>

</@html>