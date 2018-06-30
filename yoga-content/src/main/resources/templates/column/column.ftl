<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<@macroCommon.html>
<link rel="stylesheet" type="text/css" href="<@macroCommon.resource/>/tree/jqtreetable.css">
<style>
    .lineH {
        height: 40px;
    }
    td {
        vertical-align: middle !important;
    }
    .m30px {
        margin-left: 30px;
    }
</style>
<body>
<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>内容管理</a></li>
            <li>栏目管理</li>
        </ol>
    </div>
    <div class="row content-bottom">
        <div class="col-lg-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="fa fa-comments-o"></i>
                    栏目列表
                </div>
                <div class="panel-body">
                    <div class="tableToolContainer" style="margin-bottom:15px">
                        <form action="/cms/column" class="form-inline">
                            <div class="form-group">
                                <label>栏目名称</label>
                                <input type="text" class="form-control" name="filter" value="${param.filter?if_exists}">
                            </div>
                            <button type="submit" class="btn btn-success"><i class="fa fa-fw fa-search"></i>搜索
                            </button>
                            <@shiro.hasPermission name="cms_column.add" >
                            <div style="float: right">
                                <a href="#" class="btn btn-primary" id="columnAddButton">
                                    <i class="fa fa-fw fa-plus"></i>添加根栏目</a>
                            </div>
                            </@shiro.hasPermission>
                    </div>
                    </form>
                    <table id="" class="table table-bordered table-hover treetable">
                        <thead>
                        <tr>
                            <th class="tableCenter" style="width:60px">#</th>
                            <th style="width:25%">栏目名称</th>
                            <th style="width:10%" class="tableCenter">栏目CODE</th>
                            <th style="width:10%" class="tableCenter">模板类型</th>
                            <th style="width:10%" class="tableCenter">是否启用</th>
                            <th class="tableCenter">操作</th>
                        </tr>
                        </thead>
                        <#macro m_column column>
                            <tr class="lineH">
                                <td class="tableCenter">${column.id?c}</td>
                                <td id="titleName">${column.name}</td>
                                <td class="tableCenter">${column.code?if_exists}</td>
                                <td class="tableCenter"><#if column.template?exists>${column.template.name?if_exists}</#if></td>
                                <td class="tableCenter"><#if column.enabled>√</#if></td>
                                <input type="hidden" name="name" value="${column.name}">
                                <input type="hidden" name="enabled" value="${column.enabled?default(0)?c}">
                                <input type="hidden" name="parentId" value="${column.parentId?default(0)?c}">
                                <input type="hidden" name="templateId" value="${column.templateId?default(0)?c}">
                                <td class="tableCenter">
                                    <@shiro.hasPermission name="cms_column.add" >
                                        <a href="#" class="btn btn-sm btn-success " onclick="doAddChild(${column.id?default(0)?c})">
                                            <i class="fa fa-fw fa-plus "></i>添加子栏目</a>
                                    </@shiro.hasPermission>
                                    <@shiro.hasPermission name="cms_column.update" >
                                        <a href="#" class="btn btn-sm btn-info" onclick="doEdit(this, ${column.id?default(0)?c})">
                                            <i class="fa fa-fw fa-pencil "></i>修改</a>
                                    </@shiro.hasPermission>
                                    <@shiro.hasPermission name="cms_column.del" >
                                        <a href="#" class="btn btn-sm btn-danger" onclick="delColumn(${column.id?default(0)?c})">
                                            <i class="fa fa-fw fa-remove "></i>删除</a>
                                    </@shiro.hasPermission>
                                </td>
                            </tr>
                        </#macro>
                        <#macro m_columns columns>
                            <@m_column columns />
                            <#if columns.children??>
                                <#list columns.children as sub>
                                    <@m_columns sub/>
                                </#list>
                            </#if>
                        </#macro>
                        <#list columns as root>
                            <tbody id="treet_dd_${root_index}_">
                                <@m_columns root/>
                            </tbody>
                        </#list>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<footer class="main-footer">
    <@common.footer />
</footer>

    <#macro m1_column column level index>
    <option value="${column.id?if_exists?c}">
        <#list 0..level as x>
            <#if x < level>
                │&nbsp;&nbsp;
            <#else>
                ├
            </#if>
        </#list>
    ${column.name?if_exists}
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

<script type="text/javascript" src="<@macroCommon.resource/>/tree/jqtreetable.js"></script>
<script type="text/javascript" charset="utf-8">
    $(function () {
                <#function sublist parent level>
                    <#assign ret = "${level}, "/>
                    <#assign rowcount = rowcount + 1 />
                    <#local level1 = rowcount />
                    <#if parent.children??>
                        <#list parent.children as sub>
                            <#assign ret = ret + sublist(sub, level1) />
                        </#list>
                    </#if>
                    <#return ret/>
                </#function>
                <#function maplist root index>
                    <#assign var = "var map${index} = [" />
                    <#assign var = var + sublist(root, 0) />
                    <#assign var = var + "];" />
                    <#return var />
                </#function>
                <#list columns as root>
                    <#assign rowcount=0 />
                    <#assign map=maplist(root root_index)/>
                ${map}
                </#list>
                //声明参数选项
                var options = {
                    openImg: "<@macroCommon.resource/>/tree/tv-collapsable.png",
                    shutImg: "<@macroCommon.resource/>/tree/tv-expandable.png",
                    leafImg: "<@macroCommon.resource/>/tree/tv-item.png",
                    lastOpenImg: "<@macroCommon.resource/>/tree/tv-collapsable-last.png",
                    lastShutImg: "<@macroCommon.resource/>/tree/tv-expandable-last.png",
                    lastLeafImg: "<@macroCommon.resource/>/tree/tv-item-last.png",
                    vertLineImg: "<@macroCommon.resource/>/tree/vertline.png",
                    blankImg: "<@macroCommon.resource/>/tree/blank.png",
                    collapse: false,
                    column: 1,
                    striped: false,
                    highlight: true,
                    state: false
                };
                <#list columns as root>
                    if (map${root_index} != null && map${root_index}.length > 0) {
                        $("#treet_dd_${root_index}_").jqTreeTable(map${root_index}, options);
                    }
                </#list>
            }
    );
</script>
</body>

    <@operate.add "新增栏目" "#columnAddButton" "addColumn">
    <div class="modalForm" style="margin-top: 15px">
        <div class="form-group">
            <label class="col-sm-3  control-label">栏目名称</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="new_column_name">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">修改CODE</label>
            <div class="col-sm-8">
                <input type="text" id="new_column_code" class="form-control">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">启用</label>
            <div class="col-sm-8" style="padding-top:5px;">
                &nbsp;&nbsp;<input type="radio" name="new_enabled" id="new_column_enabled" checked>是&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="radio" name="new_enabled" id="enabled_disable">否
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">选择模板</label>
            <div class="col-sm-8">
                <select class="form-control" id="new_column_templateId">
                    <#list templates as template>
                        <option value="${template.id?if_exists}">${template.name?if_exists}</option>
                    </#list>
                </select>
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">父栏目</label>
            <div class="col-sm-8">
                <select class="form-control" id="new_column_parentId">
                    <option value="0">根栏目</option>
                    <#list columns as root>
                        <@m1_columns root 0 root_index/>
                    </#list>
                </select>
            </div>
        </div>
    </div>
    </@operate.add>
<script>
    function doAddChild(id) {
        $("#new_column_parentId").val(id);
        $("#operate_add_modal").modal("show");
    }
    function addColumn() {
        var name = $("#new_column_name").val();
        var code = $("#new_column_code").val();
        var enabled = $("#new_column_enabled").prop("checked");
        var templateId = $("#new_column_templateId").val();
        var parentId = $("#new_column_parentId").val();
        $.post(
                "/api/cms/column/add",
                {code: code, name: name, parentId: parentId, templateId: templateId, enabled: enabled},
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#deptAddModal").modal("hide");
                        window.location.href = "/cms/column";
                    }
                }
        );
    }
    function delColumn(id) {
        warningModal("确定要删除该栏目吗?", function () {
            $.post(
                    "/api/cms/column/delete",
                    {id: id},
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);;
                        } else {
                            window.location.href = "/cms/column";
                        }
                    }
            );
        });
    }
</script>

    <@operate.editview "编辑栏目">
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">栏目名称</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="edit_column_name">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">

            <label class="col-sm-3  control-label">修改CODE</label>
            <div class="col-sm-8">
                <input type="text" id="edit_column_code" class="form-control" readonly="readonly" >
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">启用</label>
            <div class="col-sm-8">
                &nbsp;&nbsp;<input type="radio" name="start" id="edit_column_enabled">是&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="radio" name="start" id="edit_column_disabled">否
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">选择模板</label>
            <div class="col-sm-8">
                <select class="form-control" id="edit_column_templateId">
                    <#list templates as template>
                        <option value="${template.id?if_exists}">${template.name?if_exists}</option>
                    </#list>
                </select>
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">父栏目</label>
            <div class="col-sm-8">
                <select class="form-control" id="edit_column_parentId">
                    <option value="0">根栏目</option>
                    <#list columns as root>
                        <@m1_columns root 0 root_index/>
                    </#list>
                </select>
            </div>
        </div>
    </div>
    </@operate.editview>
    <@operate.editjs "saveEdit">
    var enabled = $(elem).parent().siblings().eq(6).val();
    if (enabled == "true")
        $("#edit_column_enabled").prop("checked", true);
    else
        $("#edit_column_disabled").prop("checked", true);
    $("#edit_column_name").val($(elem).parent().siblings().eq(5).val());
    $("#edit_column_code").val($(elem).parent().siblings().eq(2).html());
    $("#edit_column_parentId").val($(elem).parent().siblings().eq(7).val());
    $("#edit_column_templateId").val($(elem).parent().siblings().eq(8).val());
    </@operate.editjs>
<script>
    function saveEdit(id) {
        var name = $("#edit_column_name").val();
        var code = $("#edit_column_code").val();
        var enabled = $("#edit_column_enabled").prop("checked");
        var columnId = $("#edit_column_columnId").val();
        var parentId = $("#edit_column_parentId").val();
        var templateId = $("#edit_column_templateId").val();
        $.post(
                "/api/cms/column/update",
                {id: id, code: code, name: name, parentId: parentId, templateId: templateId, enabled: enabled},
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#operate_edit_modal").modal("hide");
                        window.location.href = "/cms/column";
                    }
                },
                "json"
        );
    }
</script>

</@macroCommon.html>