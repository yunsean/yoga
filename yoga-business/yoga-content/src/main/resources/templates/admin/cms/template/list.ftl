<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<#setting number_format="##0.##">
<@html>
    <@head includeDate=true includeUploader=true>
        <style>
            td {
                vertical-align: middle!important;
            }
            .img {
                width: 44px;
            }
            .number {
                width: 60%;
                text-align: center
            }
        </style>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="内容管理" icon="icon-user">
            <@crumbItem href="#" name="文章模板" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "文章模板列表" />
                <@panelBody>
                    <@inlineForm class="margin-b-15">
                        <@formLabelGroup class="margin-r-15" label="关键字">
                            <@inputText name="filter" value="${(param.filter)!}"/>
                        </@formLabelGroup>
                        <@inputSubmit text="搜索" icon="icon icon-search" class="btn btn-success"/>
                        <@rightAction>
                            <@shiro.hasPermission name="cms_template.add" >
                                <@inputButton text="添加" icon="icon-plus" class="btn btn-primary" onclick="doAdd(0);" />
                            </@shiro.hasPermission>
                        </@rightAction>
                    </@inlineForm>
                    <@table>
                        <@thead>
                            <@tr>
                                <@th 5>#</@th>
                                <@th 15>模板名称</@th>
                                <@th 10 true>模板编码</@th>
                                <@th 50>模板备注</@th>
                                <@th 5 true>启用状态</@th>
                                <@th 15 true>操作</@th>
                            </@tr>
                        </@thead>
                    <tbody>
                        <#list templates as template>
                            <@tr>
                                <@td>${(template.id?c)!}</@td>
                                <@td>${template.name!}</@td>
                                <@td true>${template.code!}</@td>
                                <@td>${template.remark!}</@td>
                                <@td true><img src="/admin/images/${(template.enabled?string("yes", "no"))!"no"}.png"></@td>
                                <@td true>
                                    <@shiro.hasPermission name="cms_template.update" >
                                        <#if template.enabled>
                                            <a href="#" class="btn btn-warning btn-sm"
                                               onclick="doDisable(${(template.id?c)!}, 'true')">
                                                <i class="icon icon-arrow-down"></i>
                                            </a>
                                        <#else>
                                            <a href="#" class="btn btn-primary btn-sm"
                                               onclick="doDisable(${(template.id?c)!}, 'false')">
                                                <i class="icon icon-arrow-up"></i>
                                            </a>
                                        </#if>
                                        <a href="javascript:void(0)" onclick="doEdit(${(template.id?c)!})" class="btn btn-sm btn-primary">
                                            <i class="icon icon-edit"></i>编辑
                                        </a>
                                        <a href="/admin/cms/template/field?templateId=${(template.id?c)!}" class="btn btn-primary btn-sm">
                                            <i class="icon icon-edit"></i>字段管理
                                        </a>
                                    </@shiro.hasPermission>
                                    <@shiro.hasPermission name="cms_template.del" >
                                        <a href="javascript:void(0)" class="btn btn-sm btn-danger" onclick="doDelete(${(template.id?c)!})">
                                            <i class="icon icon-remove"></i>删除
                                        </a>
                                    </@shiro.hasPermission>
                                </@td>
                            </@tr>
                        </#list>
                    </tbody>
                    </@table>
                </@panelBody>
                <@panelPageFooter action="/admin/cms/template" />
            </@panel>
        </@bodyContent>
    </@bodyFrame>

<@modal title="模板编辑" showId="userAddButton" onOk="doSave">
    <@inputHidden name="id" id="edit_id"/>
    <@formText name="name" label="模板名称：" />
    <@formText name="code" label="模板编码：" />
    <@formTextArea name="remark" label="描述：" />
</@modal>

<script>
    function doDisable(id, on) {
        $.post(
            "/admin/cms/template/enable.json?id=" + id + "&disabled=" + on,
            function (result) {
                if (result.code < 0) {
                    alertShow("danger", result.message, 3000);
                } else {
                    window.location.reload();
                }
            }
        );
    }
    function doSave() {
        var id = $("#edit_id").val();
        var json = $("#add_form").serialize();
        $.post(id == 0 ? "/admin/cms/template/add.json" : "/admin/cms/template/update.json",
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
    function doAdd() {
        $("#add_form")[0].reset();
        $("#add_form input[name='id']").val(0);
        $("#add_modal").modal("show");
    }
    function doEdit(id) {
        $("#add_form")[0].reset();
        $.get(
            "/admin/cms/template/get.json?id=" + id,
            function (data) {
                if (parseInt(data.code) < 0) {
                    alertShow("danger", data.message, 3000);
                } else {
                    console.log(data)
                    $("#add_form input[name='id']").val(id);
                    $("#add_form input[name='name']").val(data.result.name);
                    $("#add_form input[name='code']").val(data.result.code);
                    $("#add_form textarea[name='remark']").val(data.result.remark);
                    $("#add_modal").modal("show");
                }
            }
        );
    }
    function doDelete(id) {
        warningModal("确定要删除该模板吗？", function () {
            $.ajax({
                url: "/admin/cms/template/delete.json?id=" + id,
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
</script>
</@html>
