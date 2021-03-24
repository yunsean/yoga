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
        .listMenu {
            list-style: none;
            padding-left: 2px;
            font-size: 14px;
            color: #212121;
        }
        .menuItem {
            margin: 4px 0;

        }
        .menuItem > a {
            display: block;
            color: #212121;
            padding: 8px 4px;
        }
        .menuItem:hover {
            /*background:rgb(60, 141, 188);*/
            background: #f5f5f5;
        }
        .menuItem.selected {
            background: #66b3ff;
        }
        .menuItem:hover > a, .menuItem.selected > a {
            color: #212121;
        }
        .menuWrap {
            width: 16%;
            float: left;
            min-height: 560px;
            background: #f9f9f9;
            border-bottom: 0.5px solid #f4f4f4;
            border-left: 0.5px solid #f4f4f4;
            border-right: 0.5px solid #f4f4f4;

        }
        .titleMenu {
            padding: 8px 4px;
            margin-bottom: 10px;
            background-color: #f1f1f1;
            border-top: 0.5px solid #ddd;
            border-bottom: 0.5px solid #ddd;
        }
        .titleMenu:after {
            content: "";
            display: block;
            clear: both;
        }
        .tabelList {
            float: left;
            width: 82%;
            padding-left: 20px;
        }
        .totalTitle {
            height: 25px;
            line-height: 25px;
            float: left;
            font-weight: 550;
            font-size: 16px;
        }
        .totalControl {
            float: right;
        }
    </style>
</@head>
<@bodyFrame>
    <@crumbRoot name="内容管理" icon="icon-user">
        <@crumbItem href="#" name="选项管理" />
    </@crumbRoot>
    <@bodyContent>
        <@panel>
            <@panelHeading "选项管理" />
            <@panelBody>
                <div class="menuWrap">
                    <div class="titleMenu">
                        <div class="totalTitle">
                            <span>
                                <i class="icon icon-list"></i>选项列表
                            </span>
                        </div>
                        <div class="totalControl">
                            <@shiro.hasPermission name="cms_property.add">
                                <a href="#" class="btn btn-primary" onclick="doAdd()">
                                    <i class="icon icon-user"></i>添加
                                </a>
                            </@shiro.hasPermission>
                        </div>
                    </div>
                    <ul class="listMenu">
                        <#list properies! as property>
                            <li <#if (param.id?c)! == (property.id?c)!>class="menuItem selected"<#else>class="menuItem"  </#if>>
                                <a href="/admin/cms/property?id=${property.id?c}" style="text-decoration:none;" id="${property.id?c}">${property.name}</a>
                            </li>
                        </#list>
                    <ul>
                </div>
                <div class="tabelList">
                    <table id="" class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th style="width:30%">选项名称</th>
                            <th class="tableLeft" style="width:40%">选项编码</th>
                            <th class="tableCenter" style="width:30%">操作</th>
                        </tr>
                        </thead>
                        <@m1_columns2 values!/>
                    </table>
                </div>
            </@panelBody>
        </@panel>
    </@bodyContent>
</@bodyFrame>

    <#macro m1_column2 branch level index>
        <tr name="${(branch.parentId?c)!}" id="${(branch.id?c)!}">
            <td onclick="rowClick(${(branch.id?c)!},this)" class="${(branch.id?c)!}" style=" padding-left: ${level * 20 + 10}px;height: 40px;">
                <#if branch.children??>
                    <img src="/admin/tree/shrink.png" width="18px;" height="9px;">
                    <img src="${branch.poster!}" width="25px" height="25px" onerror="this.src='/admin/images/default_property.jpg'">
                    ${(branch.name)!}
                <#else >
                    &nbsp;&nbsp;&nbsp;&nbsp;<img src="${branch.poster!}" width="25px" height="25px" onerror="this.src='/admin/images/default_property.jpg'">
                    ${(branch.name)!}
                </#if>
            </td>
            <td>${(branch.code)!}</td>
            <td style="text-align: center">
                <@shiro.hasPermission name="cms_property.update" >
                    <a href="javascript:void(0)" onclick="doAdd(${(branch.id?c)!})" class="btn btn-sm btn-info">
                        <i class="icon icon-plus"></i>添加
                    </a>
                    <a href="javascript:void(0)" onclick="doEdit(${(branch.id?c)!})" class="btn btn-sm btn-primary">
                        <i class="icon icon-edit"></i>编辑
                    </a>
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


    <@modal title="选项（值）编辑" showId="userAddButton" onOk="doSave">
        <@inputHidden name="id" id="edit_id"/>
        <@formText name="name" label="选项名称：" />
        <@formText name="code" label="选项编码：" />
        <div class="modalForm">
            <div class="form-group">
                <label class="col-sm-3  control-label">上级节点：</label>
                <div class="col-sm-8">
                    <select class="form-control" name="parentId">
                        <option value="0">根栏目</option>
                        <#list values! as root>
                            <@m1_columns root 0 root_index/>
                        </#list>
                    </select>
                </div>
            </div>
        </div>
        <@formImage name="poster" label="选项图标：" divId="poster"/>
    </@modal>
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
    function doSave() {
        var id = $("#edit_id").val();
        var json = $("#add_form").serialize();
        $.post(id == 0 ? "/admin/cms/property/add.json" : "/admin/cms/property/update.json",
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
    function doAdd(id) {
        $("#add_form")[0].reset();
        $("#add_form input[name='id']").val(0);
        $("#add_form select[name='parentId']").val(id);
        $("#add_modal").modal("show");
    }
    function doEdit(id) {
        $("#add_form")[0].reset();
        $.get(
            "/admin/cms/property/get.json?id=" + id,
            function (data) {
                if (parseInt(data.code) < 0) {
                    alertShow("danger", data.message, 3000);
                } else {
                    $("#add_form input[name='id']").val(id);
                    $("#add_form input[name='name']").val(data.result.name);
                    $("#add_form input[name='code']").val(data.result.code);
                    $("#add_form input[name='poster']").val(data.result.poster);
                    $("#add_form select[name='parentId']").val(data.result.parentId);
                    $("#poster_image").prop("src", data.result.poster);
                    $("#add_modal").modal("show");
                }
            }
        );
    }
    function toAddValue(id) {
        $("#parentId").val(id);
        $("#operate_add_modal").modal("show");
    }
    function doDelete(id) {
        warningModal("确定要删除该分类吗？", function () {
            $.ajax({
                url: "/admin/cms/property/delete.json?id=" + id,
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


