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
        <@crumbRoot name="权限管理" icon="icon-user">
            <@crumbItem href="#" name="角色管理" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "角色管理" />
                <@panelBody>
                    <@inlineForm class="margin-b-15">
                        <@formLabelGroup class="margin-r-15" label="名称：">
                            <@inputText name="filter" value="${(param.filter)!}"/>
                        </@formLabelGroup>
                        <@inputSubmit text="搜索" icon="icon icon-search" class="btn btn-success"/>
                        <@rightAction>
                            <@shiro.hasPermission name="admin_role.add" >
                                <@inputButton text="添加" icon="icon-plus" class="btn btn-primary" onclick="doAdd();" />
                            </@shiro.hasPermission>
                        </@rightAction>
                    </@inlineForm>
                    <@table>
                        <@thead>
                            <@tr>
                                <@th 5 true>#</@th>
                                <@th 20>名称</@th>
                                <@th 50>描述</@th>
                                <@th 25 true>操作</@th>
                            </@tr>
                        </@thead>
                        <@tbody>
                            <#list roles! as role>
                                <@tr>
                                    <@td true>${(role.id?c)!}</@td>
                                    <@td>${(role.name)!}</@td>
                                    <@td>${(role.remark)!}</@td>
                                    <@td true>
                                        <@shiro.hasPermission name="admin_role.update" >
                                            <a href="/admin/operator/role/privileges?roleId=${(role.id?c)!}" class="btn btn-sm btn-success permissions">
                                                <i class="icon icon-check"></i>授权
                                            </a>
                                            <a href="javascript:void(0)" onclick="doEdit(${(role.id?c)!})" class="btn btn-sm btn-primary">
                                                <i class="icon icon-edit"></i>编辑
                                            </a>
                                        </@shiro.hasPermission>
                                        <@shiro.hasPermission name="admin_role.del" >
                                            <a href="javascript:void(0)" onclick="doDelete(${(role.id?c)!})" class="btn btn-sm btn-danger">
                                                <i class="icon icon-remove"></i>删除
                                            </a>
                                        </@shiro.hasPermission>
                                    </@td>
                                </@tr>
                            </#list>
                        </@tbody>
                    </@table>
                </@panelBody>
                <@panelPageFooter action="/admin/operator/role/list" />
            </@panel>
        </@bodyContent>
    </@bodyFrame>

<script>
    function doDelete(templateId) {
        warningModal("确定要删除该角色吗?", function(){
            $.ajax({
                url: "/admin/operator/role/delete.json?id=" + templateId,
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

    <@modal title="角色编辑" onOk="doSave">
        <@inputHidden name="id" id="edit_id"/>
        <@formText name="name" label="角色名称：" />
        <@formTextArea name="remark" label="角色描述：" />
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
                    "/admin/operator/role/get.json?id=" + id,
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
            $.post(id == 0 ? "/admin/operator/role/add.json" : "/admin/operator/role/update.json",
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