<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<@html>
    <@head includeDate=true>
    <style>
        td {
            vertical-align: middle!important;
        }
    </style>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="${tenantAlias!''}管理" icon="icon-user">
            <@crumbItem href="#" name="${tenantAlias!''}模板" backLevel=1/>
            <@crumbItem href="#" name="模板菜单" />
            <@backButton/>
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "模板菜单" />
                <@panelBody>
                    <@rightAction>
                        <@shiro.hasPermission name="gbl_tenant_template.update" >
                            <@inputButton text="添加" icon="icon-plus" class="btn btn-primary" onclick="doAdd();" />
                        </@shiro.hasPermission>
                    </@rightAction>
                    <@table class="table table-bordered ">
                        <@thead>
                            <@tr>
                                <@th 10>版块</@th>
                                <@th 15>菜单项</@th>
                                <@th 25>路径</@th>
                                <@th 30>备注</@th>
                                <@th 20 true>操作</@th>
                            </@tr>
                        </@thead>
                        <@tbody>
                            <#list menus! as menu>
                                <tr style="background-color:#eaeaea">
                                    <@td><b style="font-size:14px">${(menu.name)!}</b></@td>
                                    <@td></@td>
                                    <@td></@td>
                                    <@td></@td>
                                    <@td></@td>
                                </tr>
                                <#list menu.children! as child>
                                    <@tr>
                                        <@td></@td>
                                        <@td>${(child.name)!}</@td>
                                        <@td>${(child.url)!}</@td>
                                        <@td>${(child.remark)!}</@td>
                                        <@td true>
                                            <a href="javascript:void(0)" onclick="doEdit(${(child.menuId?c)!})" class="btn btn-sm btn-success permissions">
                                                <i class="icon icon-pencil-square-o"></i>&nbsp;编辑
                                            </a>
                                            <a href="javascript:void(0)" onclick="doDelete(${(child.menuId?c)!})" class="btn btn-sm btn-danger">
                                                <i class="icon icon-remove"></i>&nbsp;删除
                                            </a>
                                        </@td>
                                    </@tr>
                                </#list>
                            </#list>
                        </@tbody>
                    </@table>
                </@panelBody>
            </@panel>
        </@bodyContent>
    </@bodyFrame>

    <script>
        function doDelete(id) {
            warningModal("确定要删除该菜单项吗?", function () {
                $.ajax({
                    url: "/admin/tenant/template/menu/delete.json?templateId=${(templateId?c)!}&menuId=" + id,
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

    <@modal title="菜单编辑" onOk="doSave">
        <@inputHidden name="menuId" id="edit_id"/>
        <@inputHidden name="templateId" value="${(templateId?c)!}" />
        <div class="form-group">
            <label class="col-sm-3  control-label">所属板块</label>
            <div class="col-sm-5">
                <input type="text" name="group" class="form-control" id="edit_menuGroupText" style="width: 100%; display: inline-block; margin-left: 5px">
            </div>
            <div class="col-sm-3">
                <select class="form-control" onchange="onGroupChanged()"  id="edit_menuGroupSelect" style="width: 100%; display: inline-block; margin-left: 5px">
                    <option value="">------快速选择------</option>
                    <#list groups as group>
                        <option value="${group}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${group}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
                    </#list>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3  control-label">菜单名称</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" name="name"">
                <span class="help-block"></span>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3  control-label">权限编码</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" name="code"">
                <span class="help-block"></span>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3  control-label">排序值</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" name="sort" value="100">
                <span class="help-block"></span>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3  control-label">菜单URL</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" name="url">
                <span class="help-block"></span>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">备注信息</label>
            <div class="col-sm-8">
                <textarea rows="5" class="form-control" name="remark"></textarea>
                <span class="help-block"><span>
            </div>
        </div>
    </@modal>
<script>
    function onGroupChanged() {
        var group = $("#edit_menuGroupSelect").val();
        if (group != '') $("#edit_menuGroupText").val(group);
    }
    function doAdd() {
        $("#add_form")[0].reset();
        $("#add_form input[name='menuId']").val(0);
        $("#add_modal").modal("show");
    }
    function doEdit(id) {
        $("#add_form")[0].reset();
        $.get(
                "/admin/tenant/template/menu/get.json?templateId=${(templateId?c)!}&menuId=" + id,
                function (data) {
                    if (parseInt(data.code) < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#add_form input[name='menuId']").val(id);
                        $("#add_form input[name='group']").val(data.result.group);
                        $("#add_form input[name='code']").val(data.result.code);
                        $("#add_form input[name='name']").val(data.result.name);
                        $("#add_form input[name='sort']").val(data.result.sort);
                        $("#add_form input[name='url']").val(data.result.url);
                        $("#add_form textarea[name='remark']").val(data.result.remark);
                        $("#add_modal").modal("show");
                    }
                }
        );
    }
    function doSave() {
        var id = $("#edit_id").val();
        var json = $("#add_form").serializeArray();
        $.post(id == 0 ? "/admin/tenant/template/menu/add.json" : "/admin/tenant/template/menu/update.json",
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