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
            <@crumbItem href="#" name="${tenantAlias!''}列表" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "${tenantAlias!''}列表" />
                <@panelBody>
                    <@inlineForm class="margin-b-15">
                        <@formLabelGroup class="margin-r-15" label="名称：">
                            <@inputText name="name" value="${(param.name)!}"/>
                        </@formLabelGroup>
                        <@inputSubmit text="搜索" icon="icon icon-search" class="btn btn-success"/>
                        <@rightAction>
                            <@shiro.hasPermission name="gbl_tenant.add" >
                                <@inputButton text="添加" icon="icon-plus" class="btn btn-primary" onclick="doAdd();" />
                            </@shiro.hasPermission>
                        </@rightAction>
                    </@inlineForm>
                    <@table>
                        <@thead>
                            <@tr>
                                <@th 5 true>#</@th>
                                <@th 15>名称</@th>
                                <@th 10>编码</@th>
                                <@th 10>创建时间</@th>
                                <@th 35>备注</@th>
                                <@th 25 true>操作</@th>
                            </@tr>
                        </@thead>
                        <@tbody>
                            <#list tenants! as tenant>
                            <@tr>
                                <@td true>${(tenant.id?c)!}</@td>
                                <@td>${(tenant.name)!}</@td>
                                <@td>${(tenant.code)!}</@td>
                                <@td>${(tenant.createTime?string("YYYY-MM-dd"))!}</@td>
                                <@td>${(tenant.remark)!}</@td>
                                <@td true>
                                <#if tenant.deleted>
                                    <@shiro.hasPermission name="gbl_tenant.update" >
                                        <a href="javascript:void(0)" onclick="showRenew(${(tenant.id?c)!})"
                                           class="btn btn-sm btn-warning permissions"><i class="icon icon-pencil-square-o"></i>&nbsp;恢复
                                        </a>
                                    </@shiro.hasPermission>
                                <#else>
                                    <@shiro.hasPermission name="gbl_tenant.update" >
                                        <a href="javascript:void(0)" onclick="doEdit(${(tenant.id?c)!})"
                                           class="btn btn-sm btn-success permissions"><i class="icon icon-pencil-square-o"></i>&nbsp;编辑
                                        </a>
                                        <a href="/admin/tenant/tenant/modules?tenantId=${(tenant.id?c)!}"
                                           class="btn btn-sm btn-primary permissions"><i class="icon icon-eye-slash"></i>&nbsp;模块
                                        </a>
                                        <a href="/admin/tenant/tenant/menus?tenantId=${(tenant.id?c)!}"
                                           class="btn btn-sm btn-primary permissions"><i class="icon icon-check-square-o"></i>&nbsp;自定义菜单
                                        </a>
                                        <a href="/admin/tenant/tenant/settings?tenantId=${(tenant.id?c)!}"
                                           class="btn btn-sm btn-primary permissions"><i class="icon icon-pencil"></i>&nbsp;配置
                                        </a>
                                    </@shiro.hasPermission>
                                    <@shiro.hasPermission name="gbl_tenant.del" >
                                        <#if tenant.code! != 'system'>
                                            &nbsp;&nbsp;&nbsp;&nbsp;
                                            <a href="javascript:void(0)" onclick="doDelete(${tenant.id?default(0)?c})"
                                               class="btn btn-sm btn-danger"><i class="icon icon-remove"></i>&nbsp;删除</a>
                                        </#if>
                                    </@shiro.hasPermission>
                                </#if>
                                </@td>
                            </@tr>
                            </#list>
                        </@tbody>
                    </@table>
                </@panelBody>
                <@panelPageFooter action="/admin/tenant/tenant/list" />
            </@panel>
        </@bodyContent>
    </@bodyFrame>

<script>
    function doDelete(tenantId) {
        warningModal("确定要删除该${tenantAlias!''}吗?", function(){
            $.ajax({
                url: "/admin/tenant/tenant/delete.json?id=" + tenantId,
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

    <@modal title="${tenantAlias!''}回复" idPrefix="renew" onOk="doRenew">
        <@formText name="id" label="${tenantAlias!''}ID：" readonly=true/>
        <@formText name="code" label="${tenantAlias!''}编码：" placeholder="恢复后的新CODE，配置域名需要"/>
    </@modal>
<script>
    function showRenew(id) {
        warningModal("该${tenantAlias!''}的部分数据可能已经丢失以至于运行不正常，确定要恢复该${tenantAlias!''}吗?", function(){
            $("#renew_form")[0].reset();
            $("#renew_form input[name='id']").val(id);
            $("#renew_modal").modal("show");
        });
    }
    function doRenew() {
        var json = $("#renew_form").serialize();
        $.post("/admin/tenant/tenant/renew.json",
                json,
                function (data) {
                    if (data.code < 0) {
                        alertShow("warning", data.message, 3000);
                    } else {
                        $("#renew_modal").modal("hide");
                        window.location.reload();
                    }
                },
                "json"
        );
    }
</script>

    <@modal title="新建${tenantAlias!''}" onOk="doSave">
        <@formText name="name" label="${tenantAlias!''}名称：" />
        <@formText name="code" label="${tenantAlias!''}编码：" placeholder="域名配置时使用"/>
        <@formList name="templateId" label="${tenantAlias!''}模板：" options=templates! blank="无" blankValue=""/>
        <@formTextArea name="remark" label="备注信息：" />
        <hr>
        <@formText name="username" label="管理员账号：" value="admin"/>
        <@formText name="password" label="初始密码：" value="123456"/>
        <@formText name="nickname" label="管理员姓名：" value="系统管理员"/>
        <@formText name="mobile" label="手机号：" />
    </@modal>
    <script>
        function doAdd() {
            $("#add_form")[0].reset();
            $("#add_form input[name='id']").val(0);
            $("#add_modal").modal("show");
        }
        function doSave() {
            var json = $("#add_form").serialize();
            $.post("/admin/tenant/tenant/add.json",
                    json,
                    function (data) {
                        if (data.code < 0) {
                            alertShow("warning", data.message, 3000);
                        } else if (data.code > 0) {
                            alertShow("warning", data.message, 1000, function () {
                                $("#add_modal").modal("hide");
                                window.location.reload();
                            });
                        } else {
                            $("#add_modal").modal("hide");
                            window.location.reload();
                        }
                    },
                    "json"
            );
        }
    </script>


    <@modal title="编辑${tenantAlias!''}" idPrefix="edit" onOk="doSaveEdit">
        <@inputHidden name="id" id="edit_id"/>
        <@formText name="name" label="${tenantAlias!''}名称：" />
        <@formTextArea name="remark" label="备注信息：" />
    </@modal>
    <script>
        function doEdit(id) {
            $("#edit_form")[0].reset();
            $.get(
                    "/admin/tenant/tenant/get.json?id=" + id,
                    function (data) {
                        if (parseInt(data.code) < 0) {
                            alertShow("danger", data.message, 3000);
                        } else {
                            $("#edit_form input[name='id']").val(id);
                            $("#edit_form input[name='name']").val(data.result.name);
                            $("#edit_form textarea[name='remark']").val(data.result.remark);
                            $("#edit_modal").modal("show");
                        }
                    }
            );
        }
        function doSaveEdit() {
            var json = $("#edit_form").serialize();
            $.post("/admin/tenant/tenant/update.json",
                    json,
                    function (data) {
                        if (data.code < 0) {
                            alertShow("warning", data.message, 3000);
                        } else {
                            $("#edit_modal").modal("hide");
                            window.location.reload();
                        }
                    },
                    "json"
            );
        }
    </script>

</@html>