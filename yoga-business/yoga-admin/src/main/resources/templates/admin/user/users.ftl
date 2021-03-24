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
            .minHeight120px {
                min-height: 120px;
            }
        </style>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="权限管理" icon="icon-user">
            <@crumbItem href="#" name="用户列表" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "用户列表" />
                <@panelBody>
                    <@inlineForm class="margin-b-15">
                        <#if branches?? && (((branches?size)!0) gt 0)>
                        <@formLabelGroup class="margin-r-15" label="所属部门">
                            <select class="form-control" name="branchId" id="dept_select">
                                <option value="">全部</option>
                                <#list branches! as root>
                                    <@m1_columns root 0 root_index/>
                                </#list>
                            </select>
                            <script>
                                $("#dept_select").val('${(param.branchId?c)!}');
                            </script>
                        </@formLabelGroup>
                        </#if>
                        <@formLabelGroup class="margin-r-15" label="用户名称">
                            <@inputText name="filter" value="${(param.filter)!}"/>
                        </@formLabelGroup>
                        <@inputSubmit text="搜索" icon="icon icon-search" class="btn btn-success"/>
                        <@rightAction>
                            <@shiro.hasPermission name="admin_user.add" >
                                <@inputButton text="添加" icon="icon-plus" class="btn btn-primary" onclick="doAdd();" />
                            </@shiro.hasPermission>
                        </@rightAction>
                    </@inlineForm>
                    <@table>
                        <@thead>
                            <@tr>
                                <@th 7>用户名</@th>
                                <@th 7>姓名</@th>
                                <@th 12>Email</@th>
                                <@th 7>手机号</@th>
                                <#if branches?? && (((branches?size)!0) gt 0)>
                                    <@th 7 true>部门</@th>
                                </#if>
                                <#if roles?? && (((branches?size)!0) gt 0)>
                                    <@th 7 true>职级</@th>
                                </#if>
                                <@th>角色</@th>
                                <@th 10 true>最后登录</@th>
                                <@th 15 true>操作</@th>
                            </@tr>
                        </@thead>
                    <tbody>
                        <#list users as operator>
                            <@tr>
                                <@td>${operator.username!}</@td>
                                <@td>${operator.nickname!}</@td>
                                <@td>${operator.email!}</@td>
                                <@td>${operator.mobile!}</@td>
                                <#if branches?? && (((branches?size)!0) gt 0)>
                                    <@td true>${operator.branch!}</@td>
                                </#if>
                                <#if roles?? && (((branches?size)!0) gt 0)>
                                    <@td true>${operator.duty!}</@td>
                                </#if>
                                <@td>${operator.roles!}</@td>
                                <@td true>${(operator.lastLogin?string('yyyy-MM-dd HH:mm:ss'))!}</@td>
                                <@td true>
                                    <@shiro.hasPermission name="admin_user.update" >
                                        <a href="javascript:void(0)" onclick="doEdit(${(operator.id?c)!})" class="btn btn-sm btn-info">
                                            <i class="icon icon-edit"></i>编辑
                                        </a>
                                    </@shiro.hasPermission>
                                    <@shiro.hasPermission name="admin_user.del" >
                                        <a href="javascript:void(0)" class="btn btn-sm btn-danger" onclick="doDelete(${(operator.id?c)!})">
                                            <i class="icon icon-remove"></i>删除
                                        </a>
                                    </@shiro.hasPermission>
                                </@td>
                            </@tr>
                        </#list>
                    </tbody>
                    </@table>
                </@panelBody>
                <@panelPageFooter action="/admin/operator/user/list" />
            </@panel>
        </@bodyContent>
    </@bodyFrame>

    <#macro m1_column column level index>
    <option value="${(column.id?c)!}">
        <#list 0..level as x><#if x < level>┃&nbsp;&nbsp;<#else>┠</#if></#list>${(column.name)!}
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
        function doDelete(id) {
            warningModal("确定要删除该用户吗？", function () {
                $.ajax({
                    url: "/admin/operator/user/delete.json?id=" + id,
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

    <@modal title="用户编辑" showId="userAddButton" onOk="saveOperator" width=75>
        <@inputHidden name="id" id="edit_id"/>
        <div style="width: 128px; float: none; display: block; margin-left: auto; margin-right: auto;" id="edit_avatar">
            <@inputAvatar name="avatar"/>
        </div>
        <div class="form-group">
            <label class="col-sm-offset-1 col-sm-1 control-label">用户名：</label>
            <div class="col-sm-4">
                <@inputText name="username"/>
            </div>
            <label class="col-sm-1 control-label">真实姓名：</label>
            <div class="col-sm-4">
                <@inputText name="nickname"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-offset-1 col-sm-1 control-label">手机号：</label>
            <div class="col-sm-4">
                <@inputText name="mobile"/>
            </div>
            <label class="col-sm-1 control-label">Email：</label>
            <div class="col-sm-4">
                <@inputText name="email"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-offset-1 col-sm-1 control-label">设置密码：</label>
            <div class="col-sm-4">
                <@inputPassword name="password" id="password" />
            </div>
            <#if branches?? && (((branches?size)!0) gt 0)>
                <label class="col-sm-1 control-label">所属部门：</label>
                <div class="col-sm-4">
                    <select class="form-control" name="branchId" id="branchId">
                        <option value="0">未指定</option>
                        <#list branches! as root>
                            <@m1_columns root 0 root_index/>
                        </#list>
                    </select>
                </div>
            </#if>
        </div>
        <div class="form-group">
            <label class="col-sm-offset-1 col-sm-1 control-label">确认密码：</label>
            <div class="col-sm-4">
                <@inputPassword class="col-sm-4" id="repwd"  />
            </div>
            <#if roles?? && (((branches?size)!0) gt 0)>
                <label class="col-sm-1 control-label">所属职级：</label>
                <div class="col-sm-4">
                    <@inputList options=duties! name="dutyId" blank="未指定" blankValue="0"/>
                </div>
            </#if>
        </div>
        <div class="form-group">
            <label class="col-sm-offset-1 col-sm-1 control-label">赋予角色：</label>
            <input type="hidden" name="roleIds" value="0">
            <div class="col-sm-8">
                <@inputCheckboxGroup options=roles! name="roleIds"/>
            </div>
        </div>
    </@modal>

<script>
    function saveOperator() {
        var id = $("#edit_id").val();
        var json = $("#add_form").serializeArray();
        var password = $("#password").val();
        var repwd = $("#repwd").val();
        if (password != repwd) {
            alertShow("warning", "两次输入的密码不一致！", 3000);
            return;
        }
        $.post(id == 0 ? "/admin/operator/user/add.json" : "/admin/operator/user/update.json",
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
                "/admin/operator/user/get.json?id=" + id,
                function (data) {
                    if (parseInt(data.code) < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#add_form input[name='id']").val(id);
                        if (data.result.avatar != '')$("#edit_avatar img").attr("src", data.result.avatar);
                        else $("#edit_avatar img").attr("src", '/admin/images/default_avatar.jpg');
                        $("#add_form select[name='branchId']").val(data.result.branchId);
                        $("#add_form select[name='dutyId']").val(data.result.dutyId);
                        $("#add_form input[name='username']").val(data.result.username);
                        $("#add_form input[name='nickname']").val(data.result.nickname);
                        $("#add_form input[name='email']").val(data.result.email);
                        $("#add_form input[name='mobile']").val(data.result.mobile);
                        if (data.result.roleIds) {
                            data.result.roleIds.forEach(function(roleId) {
                                $("#add_form input[name='roleIds'][value="+roleId+"]").prop("checked", true);
                            });
                        }
                        $("#add_modal").modal("show");
                    }
                }
        );
    }
</script>
</@html>
