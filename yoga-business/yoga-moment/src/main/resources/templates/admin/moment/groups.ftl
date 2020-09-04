<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<@html>
    <@head includeDate=true includeUploader=true>
        <style>
            td {
                vertical-align: middle!important;
            }
        </style>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="信息流管理" icon="icon-user">
            <@crumbItem href="#" name="群组管理" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "群组管理" />
                <@panelBody>
                    <@inlineForm class="margin-b-15">
                        <@formLabelGroup class="margin-r-15" label="群组名称">
                            <@inputText name="filter" value="${(param.filter)!}"/>
                        </@formLabelGroup>
                        <@inputSubmit text="搜索" icon="icon icon-search" class="btn btn-success"/>
                        <@rightAction>
                            <@shiro.hasPermission name="moment_group.add" >
                                <@inputButton text="添加" icon="icon-plus" class="btn btn-primary" onclick="doAdd();" />
                            </@shiro.hasPermission>
                        </@rightAction>
                    </@inlineForm>
                    <@table class="table table-bordered table-striped" id="workPlanTable">
                        <@thead>
                            <@tr>
                                <@th 10>名称</@th>
                                <@th 10 true>用户数量</@th>
                                <@th 60>描述</@th>
                                <@th 20 true>操作</@th>
                            </@tr>
                        </@thead>
                        <@tbody>
                            <#list groups! as group>
                            <@tr>
                                <@td>${group.name!}</@td>
                                <@td true>${(group.userCount?c)!}</@td>
                                <@td>${group.remark!}</@td>
                                <@td true>
                                    <@shiro.hasPermission name="moment_group.update" >
                                    <button onclick="doEdit(${(group.id?c)!})" class="margin-r-15 btn btn-sm btn-primary">
                                        <i class="icon icon-edit "></i>
                                        编辑
                                    </button>
                                    </@shiro.hasPermission>
                                    <a href="/admin/moment/group/users?groupId=${(group.id?c)!}" class="margin-r-15 btn btn-sm btn-success">
                                        <i class="icon icon-user "></i>
                                        用户
                                    </a>
                                    <@shiro.hasPermission name="moment_group.delete" >
                                    <button onclick="doDelete(${(group.id?c)!})" class="btn btn-sm btn-danger">
                                        <i class="icon icon-remove "></i>
                                        删除
                                    </button>
                                    </@shiro.hasPermission>
                                </@td>
                            </@tr>
                            </#list>
                        </@tbody>
                    </@table>
                </@panelBody>
                <@panelPageFooter action="/admin/moment/group/list" />
            </@panel>
        </@bodyContent>
    </@bodyFrame>


    <script>
        function doDelete(templateId) {
            warningModal("确定要删除该群组吗?", function(){
                $.ajax({
                    url: "/admin/moment/group/delete.json?id=" + templateId,
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
        <@inputHidden name="id"/>
        <@formText name="name" label="名称：" />
        <@formTextArea name="remark" label="描述：" />
    </@modal>
    <script>
        function doAdd(belowId, level) {
            $("#add_form")[0].reset();
            $("#add_form input[name='id']").val(0);
            $("#add_modal").modal("show");
        }
        function doEdit(id) {
            $("#add_form")[0].reset();
            $.get(
                    "/admin/moment/group/get.json?id=" + id,
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
            var id = $("#add_form input[name='id']").val();
            var json = $("#add_form").serialize();
            $.post(id == 0 ? "/admin/moment/group/add.json" : "/admin/moment/group/update.json",
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
