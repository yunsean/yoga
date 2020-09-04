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
            td {
                vertical-align: middle!important;
            }
            .checked {
                background-color: #eee;
            }
        </style>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="内容管理" icon="icon-user">
            <@crumbItem href="#" name="栏目管理" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "栏目管理" />
                <@panelBody>
                    <@inlineForm class="margin-b-15">
                        <@rightAction>
                            <@shiro.hasPermission name="cms_column.add" >
                                <@inputButton text="添加" icon="icon-plus" class="btn btn-primary" onclick="doAdd(0);" />
                            </@shiro.hasPermission>
                        </@rightAction>
                    </@inlineForm>
                    <@table>
                        <@thead>
                            <@tr>
                                <@th 5>#</@th>
                                <@th 15>栏目名称</@th>
                                <@th 15 true>栏目编码</@th>
                                <@th 10 true>选定模板</@th>
                                <@th 25>栏目备注</@th>
                                <@th 5 true>文章数量</@th>
                                <@th 5 true>启用状态</@th>
                                <@th 15 true>操作</@th>
                            </@tr>
                        </@thead>
                        <@tbody>
                            <@m1_columns2 columns!/>
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
    </script>

    <#macro m1_column2 branch level index>
        <tr name="${(branch.parentId?c)!}" id="${(branch.id?c)!}">
            <td onclick="rowClick(${(branch.id?c)!},this)" class="${(branch.id?c)!}" style=" padding-left: ${level * 20 + 10}px;height: 40px;">
                <#if branch.children??>
                    <img src="/admin/tree/shrink.png" width="18px;" height="9px;">${(branch.id?c)!}
                <#else >
                    &nbsp;&nbsp;&nbsp;&nbsp;${(branch.id?c)!}
                </#if>
            </td>
            <td>${(branch.name)!}</td>
            <@td true>${(branch.code)!}</@td>
            <@td true>${(branch.templateName)!}</@td>
            <td>${(branch.remark)!}</td>
            <@td true>${(branch.articleCount)!}</@td>
            <@td true><img src="/admin/images/${(branch.enabled?string("yes", "no"))!"no"}.png"></@td>
            <td style="text-align: center">
                <@shiro.hasPermission name="cms_column.add" >
                    <a href="javascript:void(0)" onclick="doAdd(${(branch.id?c)!})" class="btn btn-sm btn-info">
                        <i class="icon icon-plus"></i>添加
                    </a>
                </@shiro.hasPermission>
                <@shiro.hasPermission name="cms_column.update" >
                    <a href="javascript:void(0)" onclick="doEdit(${(branch.id?c)!})" class="btn btn-sm btn-primary">
                        <i class="icon icon-edit"></i>编辑
                    </a>
                </@shiro.hasPermission>
                <@shiro.hasPermission name="cms_column.del" >
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

    <@modal title="栏目编辑" showId="userAddButton" onOk="doSave">
        <@inputHidden name="id" id="edit_id"/>
        <@formText name="name" label="栏目名称：" />
        <@formText name="code" label="栏目编码：" />
        <div class="form-group">
            <label class="col-sm-3 control-label">上级栏目：</label>
            <div class="col-sm-8">
                <select class="form-control" name="parentId" id="parentId">
                    <option value="0">顶级栏目</option>
                    <#list columns! as root>
                        <@m1_columns root 0 root_index/>
                    </#list>
                </select>
            </div>
        </div>
        <@formList name="templateId" label="使用模板：" options=templates!/>
        <@formTextArea name="remark" label="栏目描述：" />
        <@formCheckbox name="hidden" text="从列表隐藏" label="列表显示：" value="1"/>
    </@modal>

    <script>
        function doSave() {
            var id = $("#edit_id").val();
            var json = $("#add_form").serialize();
            $.post(id == 0 ? "/admin/cms/column/add.json" : "/admin/cms/column/update.json",
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
                "/admin/cms/column/get.json?id=" + id,
                function (data) {
                    if (parseInt(data.code) < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        console.log(data)
                        $("#add_form input[name='id']").val(id);
                        $("#add_form input[name='name']").val(data.result.name);
                        $("#add_form input[name='code']").val(data.result.code);
                        $("#add_form input[name='hidden']").prop("checked", data.result.hidden);
                        $("#add_form select[name='parentId']").val(data.result.parentId);
                        $("#add_form select[name='templateId']").val(data.result.templateId);
                        $("#add_form textarea[name='remark']").val(data.result.remark);
                        $("#add_modal").modal("show");
                    }
                }
            );
        }
        function doDelete(id) {
            warningModal("确定要删除该栏目吗？", function () {
                $.ajax({
                    url: "/admin/cms/column/delete.json?id=" + id,
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
</@html>
