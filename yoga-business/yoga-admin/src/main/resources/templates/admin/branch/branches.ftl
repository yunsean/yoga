<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<#setting number_format="##0.##">
<@html>
    <@head includeDate=true>
        <style>
            .minHeight120px {
                min-height: 120px;
            }

            .checked {
                background-color: #eee;
            }

            #list-table {
                width: 100%;
            }

            .tableCenter {
                border: 1px solid gainsboro;
                height: 40px;
            }
        </style>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="权限管理" icon="icon-user">
            <@crumbItem href="#" name="部门列表" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "部门列表" />
                <@panelBody>
                    <@inlineForm class="margin-b-15">
                        <@rightAction>
                            <@shiro.hasPermission name="admin_branch.add" >
                                <@inputButton text="添加" icon="icon-plus" class="btn btn-primary" onclick="doAdd(0);" />
                            </@shiro.hasPermission>
                        </@rightAction>
                    </@inlineForm>
                    <@table>
                         <@thead>
                             <@tr>
                                <@th 20>部门名称</@th>
                                <@th 30>赋予角色</@th>
                                <@th 35>描述</@th>
                                <@th 15 true>操作</@th>
                             </@tr>
                         </@thead>
                         <@tbody>
                            <@m1_columns2 branches!/>
                         </@tbody>
                    </@table>
                </@panelBody>
            </@panel>
        </@bodyContent>
    </@bodyFrame>

<script>
    $(function () {
        $("td").mouseover(function(){
            $(this).css("cursor","pointer");
            $(this).parent().addClass("checked");
        });
        $("td").mouseout(function(){
            $(this).css("cursor","auto");
            $(this).parent().removeClass("checked");
        });
    });
    function rowClick(id, obj) {
        var list = document.getElementsByName(id);
        var objImg = $(obj).find("img").attr("src");
        if (objImg == undefined) return;
        if (objImg.indexOf("expansion.png") > 0) {
            $(obj).find("img").attr("src", "/admin/tree/shrink.png");
            for (var i = 0; i < list.length; i++) {
                list[i].style.display = "";
            }
        } else {
            $(obj).find("img").attr("src", "/admin/tree/expansion.png");
            for (var i = 0; i < list.length; i++) {
                list[i].style.display = "none";
                var img = $(list[i]).find("img").attr("src");
                if (img == undefined) continue;
                if (img.indexOf("shrink.png") > 0) {
                    $(list[i]).children().eq(0).click();
                }
            }
        }
    }
    function doDelete(id) {
        warningModal("确定要删除该部门吗？", function () {
            $.ajax({
                url: "/admin/operator/branch/delete.json?id=" + id,
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

    <#macro m1_column2 branch level index>
        <tr name="${(branch.parentId?c)!}" id="${(branch.id?c)!}">
            <td onclick="rowClick(${(branch.id?c)!},this)" class="${(branch.id?c)!}" style=" padding-left: ${level * 20 + 10}px;height: 40px;">
                <#if branch.children??>
                    <img src="/admin/tree/shrink.png" width="18px;" height="9px;">${(branch.name)!}
                <#else >
                    &nbsp;&nbsp;&nbsp;&nbsp;${(branch.name)!}
                </#if>
            </td>
            <td>${(branch.roles)!}</td>
            <td>${(branch.remark)!}</td>
            <td style="text-align: center">
                <@shiro.hasPermission name="admin_branch.add" >
                    <a href="javascript:void(0)" onclick="doAdd(${(branch.id?c)!})" class="btn btn-sm btn-info">
                        <i class="icon icon-plus"></i>添加
                    </a>
                </@shiro.hasPermission>
                <@shiro.hasPermission name="admin_branch.update" >
                    <a href="javascript:void(0)" onclick="doEdit(${(branch.id?c)!})" class="btn btn-sm btn-primary">
                        <i class="icon icon-edit"></i>编辑
                    </a>
                </@shiro.hasPermission>
                <@shiro.hasPermission name="admin_branch.del" >
                    <a href="javascript:void(0)" class="btn btn-sm btn-danger" onclick="doDelete(${(branch.id?c)!})">
                        <i class="icon icon-remove"></i>删除
                    </a>
                </@shiro.hasPermission>
            </td>
        </tr>
    </#macro>
    <#macro m1_columns2 columns level=0>
        <#if columns??>
            <#list columns as column>
                <@m1_column2 column level column_index/>
                <#local level1 = level + 1/>
                <#if column.children??>
                    <@m1_columns2 column.children level1/>
                </#if>
            </#list>
        </#if>
    </#macro>

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

    <@modal title="部门编辑" showId="userAddButton" onOk="doSave">
        <@inputHidden name="id" id="edit_id"/>
        <@formText name="name" label="部门名称：" />
        <div class="form-group">
           <label class="col-sm-3 control-label">上级部门：</label>
           <div class="col-sm-8">
               <select class="form-control" name="parentId" id="parentId">
                   <option value="0">顶级部门</option>
                    <#list branches! as root>
                        <@m1_columns root 0 root_index/>
                    </#list>
               </select>
           </div>
        </div>
        <@formTextArea name="remark" label="部门描述：" />
        <@formCheckboxGroup options=roles! name="roleIds" label="赋予角色：" />
    </@modal>

    <script>
        function doSave() {
            var id = $("#edit_id").val();
            var json = $("#add_form").serialize();
            $.post(id == 0 ? "/admin/operator/branch/add.json" : "/admin/operator/branch/update.json",
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
        function doAdd(parentId) {
            $("#add_form")[0].reset();
            $("#add_form input[name='id']").val(0);
            $("#add_form select[name='parentId']").val(parentId);
            $("#add_modal").modal("show");
        }
        function doEdit(id) {
            $("#add_form")[0].reset();
            $.get(
                    "/admin/operator/branch/get.json?id=" + id,
                    function (data) {
                        if (parseInt(data.code) < 0) {
                            alertShow("danger", data.message, 3000);
                        } else {
                            console.log(data)
                            $("#add_form input[name='id']").val(id);
                            $("#add_form input[name='name']").val(data.result.name);
                            $("#add_form select[name='parentId']").val(data.result.parentId);
                            $("#add_form textarea[name='remark']").val(data.result.remark);
                            if (data.result.roleIds) {
                                data.result.roleIds.forEach(function (roleId) {
                                    $("#add_form input[name='roleIds'][value=" + roleId + "]").prop("checked", true);
                                });
                            }
                            $("#add_modal").modal("show");
                        }
                    }
            );
        }
    </script>
</@html>
