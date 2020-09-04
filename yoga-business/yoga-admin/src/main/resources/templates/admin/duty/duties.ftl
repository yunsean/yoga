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
            <li class="active"><a href="#">职级管理</a></li>
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <div class="panel-heading">
                    <i class="icon"></i>
                    职级管理
                </div>
                <@panelBody>
                    <@inlineForm class="margin-b-15">
                        <@formLabelGroup class="margin-r-15" label="名称：">
                            <@inputText name="filter" value="${(param.filter)!}"/>
                        </@formLabelGroup>
                        <@inputSubmit text="搜索" icon="icon icon-search" class="btn btn-success"/>
                    </@inlineForm>
                    <@table>
                        <@thead>
                            <@tr>
                                <@th 5 true>#</@th>
                                <@th 10>职级名称</@th>
                                <@th 5 true>级别</@th>
                                <@th 7 true>编码</@th>
                                <@th 25>赋予角色</@th>
                                <@th 33>描述</@th>
                                <@th 15 true>操作</@th>
                            </@tr>
                        </@thead>
                        <@tbody>
                            <#list duties! as duty>
                                <@tr>
                                    <@td true>${(duty.id?c)!}</@td>
                                    <@td>${(duty.name)!}</@td>
                                    <@td true>${(duty.level?c)!}</@td>
                                    <@td true>${(duty.code)!}</@td>
                                    <@td>${(duty.roles)!}</@td>
                                    <@td>${(duty.remark)!}</@td>
                                    <@td true>
                                        <@shiro.hasPermission name="admin_role.add" >
                                        <a href="javascript:void(0)" onclick="doAdd(${(duty.id?c)!}, ${(duty.level?c)!})" class="btn btn-sm btn-success">
                                            <i class="icon icon-reply"></i>插入
                                        </a>
                                        </@shiro.hasPermission>
                                        <@shiro.hasPermission name="admin_role.update" >
                                        <a href="javascript:void(0)" onclick="doEdit(${(duty.id?c)!})" class="btn btn-sm btn-info editor">
                                            <i class="icon icon-edit"></i>编辑
                                        </a>
                                        </@shiro.hasPermission>
                                        <@shiro.hasPermission name="admin_role.del" >
                                        <a href="javascript:void(0)" onclick="doDelete(${(duty.id?c)!})" class="btn btn-sm btn-danger">
                                            <i class="icon icon-remove"></i>删除
                                        </a>
                                        </@shiro.hasPermission>
                                    </@td>
                                </@tr>
                            </#list>
                            <@tr>
                                <@td></@td>
                                <@td>默认</@td>
                                <@td true>0</@td>
                                <@td>无</@td>
                                <@td>未配置职级用户的默认级别</@td>
                                <@td true>
                                    <@shiro.hasPermission name="admin_role.add" >
                                    <a href="javascript:void(0)" onclick="doAdd(0, 0)" class="btn btn-sm btn-success">
                                        <i class="icon icon-reply"></i>插入
                                    </a>
                                    </@shiro.hasPermission>
                                </@td>
                            </@tr>
                        </@tbody>
                    </@table>
                </@panelBody>
                <@panelPageFooter action="/admin/operator/duty/list" />
            </@panel>
        </@bodyContent>
    </@bodyFrame>

    <script>
        function doDelete(templateId) {
            warningModal("确定要删除该职级吗?", function(){
                $.ajax({
                    url: "/admin/operator/duty/delete.json?id=" + templateId,
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

    <@modal title="编辑" onOk="doSave">
        <@inputHidden name="id" id="edit_id"/>
        <@inputHidden name="belowId"/>
        <@formText name="name" label="名称：" />
        <@formText name="code" label="编码：" placeholder="可选"/>
        <@formTextArea name="remark" label="描述：" />
        <@formCheckboxGroup options=roles! name="roleIds" label="角色：" />
    </@modal>
    <script>
        function doAdd(belowId, level) {
            $("#add_form")[0].reset();
            $("#add_form input[name='id']").val(0);
            $("#add_form input[name='belowId']").val(belowId);
            $("#add_modal").modal("show");
        }
        function doEdit(id) {
            $("#add_form")[0].reset();
            $.get(
                    "/admin/operator/duty/get.json?id=" + id,
                    function (data) {
                        if (parseInt(data.code) < 0) {
                            alertShow("danger", data.message, 3000);
                        } else {
                            $("#add_form input[name='id']").val(id);
                            $("#add_form input[name='name']").val(data.result.name);
                            $("#add_form input[name='code']").val(data.result.code);
                            $("#add_form textarea[name='remark']").val(data.result.remark);
                            data.result.roleIds.forEach(function (self) {
                                $("#add_form input[name='roleIds'][value='" + self + "']").prop("checked", "checked");
                            });
                            $("#add_modal").modal("show");
                        }
                    }
            );
        }
        function doSave() {
            var id = $("#edit_id").val();
            var json = $("#add_form").serialize();
            $.post(id == 0 ? "/admin/operator/duty/add.json" : "/admin/operator/duty/update.json",
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