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
        </style>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="信息交流" icon="icon-user">
            <@crumbItem href="#" name="群组管理" backLevel=1/>
            <@crumbItem href="#" name="群组用户"/>
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "群组用户" />
                <@panelBody>
                    <@inlineForm class="margin-b-15">
                        <@inputHidden name="groupId" value="${(param.groupId?c)!}" />
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
                        <@formLabelGroup class="margin-r-15" label="所属职级">
                            <@inputList name="dutyId" options=duties! value="${(param.dutyId?c)!}" blank="全部"/>
                        </@formLabelGroup>
                        <@formLabelGroup class="margin-r-15" label="用户名称">
                            <@inputText name="filter" value="${(param.filter)!}"/>
                        </@formLabelGroup>
                        <@shiro.hasPermission name="moment_group.update" >
                        <@formLabelGroup class="margin-r-15" label="">
                            <@inputCheckbox text="只显示群组用户" name="includedOnly" checked="${(param.includedOnly?string('checked', ''))!}" />
                        </@formLabelGroup>
                        </@shiro.hasPermission>
                        <@inputSubmit text="搜索" icon="icon icon-search" class="btn btn-success"/>
                        <@rightAction>
                            <@shiro.hasPermission name="moment_group.update" >
                                <@inputButton text="保存" icon="icon-save" class="btn btn-primary" onclick="doSave();" />
                            </@shiro.hasPermission>
                        </@rightAction>
                    </@inlineForm>
                    <@table>
                        <@thead>
                            <@tr>
                                <th style="width:20%">
                                <@shiro.hasPermission name="moment_group.update" >
                                    <label class="checkbox-inline">
                                        <input name="" onchange="doCheckAll()" id="checkAll" type="checkbox" class="margin-r-5 " value="1">
                                </@shiro.hasPermission>
                                        姓名
                                <@shiro.hasPermission name="moment_group.update" >
                                    </label>
                                </@shiro.hasPermission>
                                </th>
                                <@th 20 true>部门</@th>
                                <@th 20 true>职级</@th>
                                <@th 20 true>Email</@th>
                                <@th 20 true>手机号</@th>
                            </@tr>
                        </@thead>
                        <tbody id="user_table">
                            <#list users as user>
                                <@tr>
                                    <td>
                                    <@shiro.hasPermission name="moment_group.update" >
                                        <label class="checkbox-inline">
                                            <input name="id" type="checkbox" class="margin-r-5 " ${(user.included?string('checked', ''))!} value="${(user.id?c)!}">
                                    </@shiro.hasPermission>
                                            ${user.nickname!}
                                    <@shiro.hasPermission name="moment_group.update" >
                                        </label>
                                    </@shiro.hasPermission>
                                    </td>
                                    <@td true>${user.branch!}</@td>
                                    <@td true>${user.duty!}</@td>
                                    <@td true>${user.email!}</@td>
                                    <@td true>${user.mobile!}</@td>
                                </@tr>
                            </#list>
                        </tbody>
                    </@table>
                </@panelBody>
                <@panelPageFooter action="/admin/moment/group/users" />
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
        function doCheckAll() {
            var checked = $("#checkAll").prop('checked');
            $.each($("#user_table input[name='id']"), function (i, id) {
                $(id).prop("checked", checked);
            })
        }
        function doSave() {
            var adds = []
            var deletes = []
            $("#user_table").find("tr").each(function () {
                var tds = $(this).children();
                var checked = tds.eq(0).find('input').prop('checked');
                if (checked) adds.push(tds.eq(0).find('input').val());
                else deletes.push(tds.eq(0).find('input').val());
            })
            if (adds.length > 0 || deletes.length > 0) {
                $.ajax({
                    type: "post",
                    url: "/admin/moment/group/users/update.json",
                    data: JSON.stringify({
                        groupId: ${(param.groupId?c)!},
                        addIds: adds,
                        deleteIds: deletes
                    }),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function (data) {
                        if (data.code < 0) {
                            alertShow("warning", data.message, 3000);
                        } else {
                            window.location.reload();
                        }
                    }
                });
            }
        }
    </script>
</@html>
