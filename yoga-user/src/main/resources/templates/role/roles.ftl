<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<!DOCTYPE html>
<html>
<@macroCommon.html>
<link href="/css/zui.min.css" rel="stylesheet">
<body>
<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="#"><i class="icon icon-user"></i> 权限管理</a></li>
            <li class="active">角色管理</li>
        </ol>
    </div>
    <div class="row content-bottom">
        <div class="col-sm-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="icon icon-user"></i>
                   角色
                </div>
                <div class="panel-body">
                    <div class="tableToolContainer" style="margin-bottom:15px; float: right">
                        <@shiro.hasPermission name="pri_role.add" >
                            <a href="#" class="btn btn-primary" id="roleAddButton">
                                <i class="icon icon-plus"></i>添加
                            </a>
                        </@shiro.hasPermission>
                    </div>
                    <table id="" class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th style="width:50%" class="tableCenter"><@common.roleAlias/>名称</th>
                            <th style="width:50%" class="tableCenter">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list roles as role>
                            <tr>
                                <td class="tableCenter">${role.name?if_exists}</td>
                                <td class="tableCenter">
                                <@shiro.hasPermission name="pri_role.update" >
                                    <a href="/privilege/permissions?roleId=${role.id?default(0)?c}&pageIndex=${page.pageIndex?default(0)?c}"
                                       class="btn btn-success permissions"><i class="icon icon-check"></i>授权</a>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="pri_role.del" >
                                    <a href="javascript:void(0)" onclick="delRole(${role.id?default(0)?c})"
                                       class="btn btn-danger"><i class="icon icon-remove"></i>删除</a>
                                </@shiro.hasPermission>
                                </td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>
                <div class="box-footer" style="text-align:center">
                    <@html.paging page=page param=param action="/privilege/roles"/>
                </div>
            </div>
        </div>
    </div>
</div>
<footer class="main-footer">
    <@common.footer />
</footer>
</div>

<script>
    jQuery(document).ready(function () {
        $("#roleAddButton").on("click", function () {
            $("#roleAddModal").modal("show");
        });
        $("#submitButton").on("click", function () {
            addRole();
        });
        $("#roleAddModal").on("hidden.bs.modal", function () {
            $("#roleAddForm")[0].reset();
        });
    });
</script>
</body>


<@operate.add "新增" "#roleAddButton" "addRole">
    <div class="modalForm" style="padding:10px;">
        <div class="form-group">
            <label class="col-sm-3  control-label"><@common.roleAlias/></label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="roleName" name="roleName">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
</@operate.add>
<script>
    function addRole() {
        var name = $("#roleName").val();
        var pageIndex = ${page.pageIndex};
        $.post(
                "/api/role/add",
                {name: name},
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#roleAddModal").modal("hide");
                        window.location.href = "/privilege/roles?pageIndex=" + pageIndex;
                    }
                }
        );
    }
    function delRole(roleId){
        var pageIndex = ${page.pageIndex};
        warningModal("确定要删除该<@common.roleAlias/>吗?", function(){
            $.post(
                    "/api/role/delete",
                    {id:roleId},
                    function(data){
                        if(data.code < 0){
                            alertShow("danger", data.message, 3000);
                        }else{
                            window.location.href="/privilege/roles?pageIndex=" + pageIndex;
                        }
                    }
            );
        });
    }
</script>

</@macroCommon.html>