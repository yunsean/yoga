<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<!DOCTYPE html>
<html>
<@macroCommon.html>
<body>
<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="#"><i class="icon icon-user"></i> 权限管理</a></li>
            <li><@common.dutyAlias/>管理</li>
        </ol>
    </div>
    <div class="row content-bottom">
        <div class="col-sm-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="icon icon-user"></i>
                    <@common.dutyAlias/>管理
                </div>
                <div class="panel-body">
                    <table id="" class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th style="width:15%" class="tableCenter"><@common.dutyAlias/></th>
                            <th style="width:15%" class="tableCenter">级别</th>
                            <th style="width:40%" class="tableCenter">描述</th>
                            <th style="width:30%" class="tableCenter">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#if duties?exists && (duties?size > 0)>
                                <#list duties as duty>
                                <tr>
                                    <td class="tableCenter">${duty.name?if_exists}</td>
                                    <td class="tableCenter">${duty.level?if_exists}</td>
                                    <td class="tableCenter">${duty.remark?if_exists}</td>
                                    <input type="hidden" name="id" value="${duty.id?default(0)?c}">
                                    <td class="tableCenter">
                                        <a href="javascript:void(0)"
                                           onclick="toAddDuty(${duty.id?default(0)?c})"
                                           class="btn btn-success"><i class="icon icon-reply"></i>插入</a>
                                        <a href="javascript:void(0)"
                                           onclick="editDuty(${duty.id?if_exists}); doEdit(this, ${duty.id?if_exists})"
                                           class="btn btn-info editor"><i class="icon icon-edit"></i>编辑</a>
                                        <a href="javascript:void(0)"
                                           onclick="delDuty(${duty.id?default(0)?c})"
                                           class="btn btn-danger"><i class="icon icon-remove"></i>删除</a>
                                    </td>
                                </tr>
                                </#list>
                            </#if>
                        <tr>
                            <td class="tableCenter">默认</td>
                            <td class="tableCenter">0</td>
                            <td class="tableCenter">未配置<@common.dutyAlias/>用户的默认级别</td>
                            <td class="tableCenter">
                                <a href="javascript:void(0)" onclick="toAddDuty(0)"
                                   class="btn btn-success"><i class="icon icon-reply"></i>插入</a>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<footer class="main-footer">
    <@common.footer />
</footer>
</div>
</body>
    <@operate.editview "编辑">
    <div class="modalForm" style="padding-top: 20px">
        <div class="form-group">
            <label class="col-sm-3 control-label"><@common.dutyAlias/>名称</label>
            <div class="col-sm-8">
                <input type="hidden" id="edit_id">
                <input type="text" id="edit_name" class="form-control"
                       onKeypress="">
                <span class="help-block"><span>
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3 control-label"><@common.dutyAlias/>描述</label>
            <div class="col-sm-8">
                <textarea rows="5" class="form-control" id="edit_desc"></textarea>
                <span class="help-block"><span>
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
    <input type="hidden" id="edit_dutyId" value="">
    </@operate.editview>

    <@operate.editjs "saveEdit">
    $("#edit_name").val($(elem).parent().siblings().eq(0).html());
    $("#edit_desc").text($(elem).parent().siblings().eq(2).html());
    $("#edit_dutyId").val($(elem).parent().siblings().eq(3).val());
    </@operate.editjs>

<script>
    function editDuty(id) {
        var deptRoles = "";
        $.post(
                "/api/duty/roles",
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
        var id = $("#edit_dutyId").val();
        var name = $("#edit_name").val();
        var remark = $("#edit_desc").val();
        var roleIds = "";
        $("input[type='checkbox'][name='editRoleIds']:checked").each(function () {
            roleIds += $(this).val() + ","
        })
        if (roleIds.length > 0) {
            roleIds = roleIds.substr(0, roleIds.length - 1)
        }
        $.post(
                "/api/duty/update",
                {name: name, remark: remark, id: id, roleIds: roleIds},
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#deptEditorModal").modal("hide");
                        window.location.href = "/privilege/duties";
                    }
                },
                "json"
        );
    }
</script>

    <@operate.add "添加" "#dutyAddButton" "addDuty">
    <div class="modalForm" style="padding-top: 20px">
        <div class="form-group">
            <label class="col-sm-3 control-label"><@common.dutyAlias/>名称</label>
            <div class="col-sm-8">
                <input type="hidden" id="new_id">
                <input type="text" id="new_name" class="form-control"
                       onKeypress="">
                <span class="help-block"><span>
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3 control-label"><@common.dutyAlias/>描述</label>
            <div class="col-sm-8">
                <textarea rows="5" class="form-control" id="new_desc"></textarea>
                <span class="help-block"><span>
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label"><@common.roleAlias/></label>
            <div class="col-sm-8" style="padding-top: 6px">
                <#list roles as role>
                    <input type="checkbox" name="addRoleIds" value="${role.id?c}">&nbsp;&nbsp;${role.name}
                </#list>
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <input type="hidden" id="add_blowId" value="">
    </@operate.add>
<script>
    function toAddDuty(id) {
        $("#operate_add_modal").modal("show");
        $("#add_blowId").val(id);
    }
    function addDuty() {
        var id = $("#add_blowId").val();
        var name = $("#new_name").val();
        var desc = $("#new_desc").val();
        var roleIds = "";
        $("input[type='checkbox'][name='addRoleIds']:checked").each(function () {
            roleIds += $(this).val() + ",";
        });
        if (roleIds.length > 0) {
            roleIds = roleIds.substr(0, roleIds.length - 1);
        }
        $.post(
                "/api/duty/add",
                {belowId: id, name: name, remark: desc, roleIds: roleIds},
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);;
                    } else {
                        $("#dutyAddModal").modal("hide");
                        window.location.href = "/privilege/duties";
                    }
                }
        );
    }
    function delDuty(id) {
        warningModal("确定要删除该<@common.dutyAlias/>吗?", function () {
            $.post(
                    "/api/duty/delete",
                    {id: id},
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);
                        } else {
                            window.location.href = "/privilege/duties";
                        }
                    }
            );
        });
    }
</script>
</html>
</@macroCommon.html>