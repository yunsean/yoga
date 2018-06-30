<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<@macroCommon.html>
<link rel="stylesheet" type="text/css" href="<@macroCommon.resource/>/tree/jqtreetable.css">
<body>
<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="#"><i class="icon icon-user"></i> 权限管理</a></li>
            <li class="active"><@common.deptAlias/>管理</li>
        </ol>
    </div>
    <div class="row content-bottom">
        <div class="col-sm-12">
            <!-- Chat box -->
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="icon icon-user"></i>
                    <@common.deptAlias/>管理
                </div>
                <div class="panel-body">

                    <div class="tableToolContainer" style="margin-bottom:15px">
                        <form class="form-inline">
                            <div class="form-group">
                                <label><@common.deptAlias/>名称</label>
                                <input type="text" class="form-control" name="name" value="${param.name?if_exists}">
                            </div>
                            <button class="btn btn-success" type="submit"><i class="icon icon-search"></i>搜索
                            </button>
                            <@shiro.hasPermission name="pri_dept.add" >
                                <div class="tableToolContainer" style="margin-bottom:15px; float: right">
                                    <a href="#" class="btn btn-primary" id="deptAddButton">
                                        <i class="icon icon-plus"></i>添加</a>
                                </div>
                            </@shiro.hasPermission>
                        </form>
                    </div>

                    <table class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th style="width:25%"><@common.deptAlias/>名称</th>
                            <th style="width:50%"><@common.deptAlias/>描述</th>
                            <th style="width:25%" class="tableCenter">操作</th>
                        </tr>
                        </thead>
                    </table>
                    <div class="tabel_scroll">
                        <table class="table table-bordered">
                        <tbody>
                            <#macro m_column dept>
                            <tr>
                                <td style="width:25%">${dept.name?if_exists}</td>
                                <td style="width:50%">${dept.remark?if_exists}</td>
                                <input type="hidden" name="roleId" value="${dept.roleId?default(0)?c}">
                                <input type="hidden" name="id" value="${dept.id?default(0)?c}">
                                <input type="hidden" name="parentId" value="${dept.parentId?default(0)?c}">
                                <input type="hidden" name="name" value="${dept.name?if_exists}">
                                <td class="tableCenter" style="width:25%">
                                    <@shiro.hasPermission name="pri_dept.add" >
                                        <#if (maxLevel < 1 || dept.code?length < maxLevel * 2)>
                                            <a href="#" class="btn btn-sm btn-success "
                                               onclick="doAddChild(${dept.id?default(0)?c})">
                                                <i class="fa fa-fw fa-plus "></i>新建部门</a>
                                        </#if>
                                    </@shiro.hasPermission>
                                    <@shiro.hasPermission name="pri_dept.update" >
                                        <a href="javascript:void(0)" class="btn  btn-sm btn-info editor"
                                           onclick="editDept(${dept.id?if_exists?c}); doEdit(this, ${dept.id?if_exists?c})">
                                            <i class="icon icon-edit"></i>编辑</a>
                                    </@shiro.hasPermission>
                                    <@shiro.hasPermission name="pri_dept.del" >
                                        <a href="javascript:void(0)" onclick="delDept(${dept.id?default(0)?c})"
                                           class="btn btn-sm btn-danger"><i class="icon icon-remove"></i>删除</a>
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
                            <#list depts as root>
                                <tbody id="treet_dd_${root_index}_">
                                    <@m_columns root/>
                                </tbody>
                            </#list>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
<footer class="main-footer">
    <@common.footer />
</footer>
</div>


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
                <#list depts as root>
                    <#assign rowcount=0 />
                    <#assign map=maplist(root root_index) />
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
                    collapse: true,
                    column: 0,
                    striped: false,
                    highlight: true,
                    state: false
                };
                <#list depts as root>
                    if (map${root_index} != null && map${root_index}.length > 0) {
                        $("#treet_dd_${root_index}_").jqTreeTable(map${root_index}, options);
                    }
                </#list>
            }
    );
</script>
</body>

    <@operate.add "新增" "#deptAddButton" "addDept">
    <div class="modalForm" style="margin-top: 20px">
        <div class="form-group">
            <label class="col-sm-3  control-label">上级部门</label>
            <div class="col-sm-8">
                <select class="form-control" id="new_parentId">
                    <option value="0">顶级部门</option>
                    <#list depts as root>
                        <@m1_columns root 0 root_index/>
                    </#list>
                </select>
            </div>
        </div>
    </div>
    <div class="modalForm" style="padding-top: 20px">
        <div class="form-group">
            <label class="col-sm-3  control-label"><@common.deptAlias/>名称</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="new_name" name="new_name">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label"><@common.deptAlias/>描述</label>
            <div class="col-sm-8">
                <textarea name="" class="form-control" id="new_remark" name="new_remark"></textarea>
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">

            <label class="col-sm-3  control-label"><@common.roleAlias/></label>
            <div class="col-sm-8" style="padding-top: 6px">
                <#list roles as role>
                    <label class="checkbox-inline">
                        <input type="checkbox" name="addRoleIds" value="${role.id?c}"> ${role.name}
                    </label>
                </#list>
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    </@operate.add>
<script>
    function doAddChild(id) {
        $("#new_parentId").val(id);
        $("#operate_add_modal").modal("show");
    }
    function addDept() {
        var name = $("#new_name").val();
        var remark = $("#new_remark").val();
        var parentId = $("#new_parentId").val();
        var roleIds = "";
        $("input[type='checkbox'][name='addRoleIds']:checked").each(function () {
            roleIds += $(this).val() + ","
        })
        if (roleIds.length > 0) {
            roleIds = roleIds.substr(0, roleIds.length - 1)
        }
        $.post(
                "/api/dept/add",
                {parentId: parentId, name: name, remark: remark, roleIds: roleIds},
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#deptAddModal").modal("hide");
                        window.location.reload();
                    }
                }
        );
    }
    function delDept(id) {
        warningModal("确定要删除该<@common.deptAlias/>吗?", function () {
            $.post(
                    "/api/dept/delete",
                    {id: id},
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);
                            ;
                        } else {
                            window.location.reload();
                        }
                    }
            );
        });
    }
</script>

    <@operate.editview "编辑">
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">上级部门</label>
            <div class="col-sm-8">
                <select class="form-control" id="edit_parentId" disabled>
                    <option value="0">顶级部门</option>
                    <#list depts as root>
                        <@m1_columns root 0 root_index/>
                    </#list>
                </select>
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label"><@common.deptAlias/>名称</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="edit_dept_name">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label"><@common.deptAlias/>描述</label>
            <div class="col-sm-8">
                <textarea name="" class="form-control" id="edit_dept_ramark"></textarea>
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label"><@common.roleAlias/></label>
            <div class="col-sm-8" style="padding-top: 6px">
                <#list roles as role>
                    <input type="checkbox" name="editRoleIds" value="${role.id?c}">&nbsp;&nbsp;${role.name}
                </#list>
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <input type="hidden" id="edit_deptId" value="">
    </@operate.editview>

    <@operate.editjs "saveEdit">
    $("#edit_dept_name").val($(elem).parent().siblings().eq(5).val());
    $("#edit_dept_ramark").text($(elem).parent().siblings().eq(1).html());
    $("#edit_deptId").val($(elem).parent().siblings().eq(3).val());
    $("#edit_parentId").val($(elem).parent().siblings().eq(4).val());
    <#--动态获取原有的roleId-->
    $("#edit_roleId option[value='"+ $(elem).parent().siblings().eq(2).val() + "']").attr("checked", true);
    </@operate.editjs>

<script>
    function editDept(id) {
        var deptRoles = "";
        $.post(
                "/api/dept/roles",
                {id: id},
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        deptRoles = data.result;
                        $("input:checkbox").each(function () {
                            $(this).prop("checked", false)
                        });
                        $.each(deptRoles, function (i, value) {
                            $("input[type='checkbox'][name='editRoleIds'][value=" + value + "]").prop("checked", true);
                        });
                    }
                }, "json"
        )
    }
    function saveEdit() {
        var id = $("#edit_deptId").val();
        var name = $("#edit_dept_name").val();
        var remark = $("#edit_dept_ramark").val();
        var parentId = $("#edit_parentId").val();
        var roleIds = "";
        $("input[type='checkbox'][name='editRoleIds']:checked").each(function () {
            roleIds += $(this).val() + ","
        })
        if (roleIds.length > 0) {
            roleIds = roleIds.substr(0, roleIds.length - 1)
        }
        $.post(
                "/api/dept/update",
                {parentId: parentId, name: name, remark: remark, id: id, roleIds: roleIds},
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#deptEditorModal").modal("hide");
                        window.location.reload();
                    }
                },
                "json"
        );
    }
</script>
</@macroCommon.html>