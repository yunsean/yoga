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
            <li><i class="icon icon-dashboard"></i>租户管理</li>
        </ol>
    </div>
    <div class="row content-bottom">
        <div class="col-sm-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="fa fa-comments-o"></i>
                    租户
                </div>
                <div class="panel-body">
                    <div class="tableToolContainer" style="margin-bottom:15px">
                        <form action="/tenant/tenants" class="form-inline">
                            <div class="form-group">
                                <label class="exampleInputAccount4">&nbsp;&nbsp;租户名称：&nbsp;</label>
                                <input type="text" class="form-control" name="name" id="name"
                                       value="${param.name?if_exists}">
                            </div>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <div class="form-group">
                                <label class="exampleInputAccount4">&nbsp;&nbsp;租户CODE：</label>
                                <input type="text" class="form-control" name="code" id="code"
                                       value="${param.code?if_exists}">
                            </div>
                            &nbsp;&nbsp;
                            <button class="btn btn-success" type="submit">
                                <i class="fa fa-fw fa-search"></i>搜索
                            </button>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <@shiro.hasPermission name="gbl_tenant.add" >
                                <a style="float: right" href="#" class="btn btn-primary" id="tenantAddButton">
                                    <i class="fa fa-fw fa-plus"></i>添加
                                </a>
                            </@shiro.hasPermission>
                        </form>
                    </div>
                    <table id="" class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th style="width:20%">名称</th>
                            <th style="width:10%" class="tableCenter">CODE</th>
                            <#if !nerverExpire>
                                <th style="width:10%" class="tableCenter">到期时间</th>
                            </#if>
                            <th style="width:25%" class="tableLeft">备注</th>
                            <th class="tableCenter">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list tenants as tenant>
                            <tr>
                                <td>${tenant.name?if_exists}</td>
                                <td class="tableCenter">${tenant.code?if_exists}</td>
                                <#if !nerverExpire>
                                    <td class="tableCenter">${(tenant.expireDate?string("yyyy-MM-dd"))!}</td>
                                </#if>
                                <td class="tableLeft">${tenant.remark?if_exists}</td>
                                <td class="tableCenter">
                                    <@shiro.hasPermission name="gbl_tenant.update" >
                                        <a href="javascript:void(0)" onclick="doEdit(this, ${tenant.id?if_exists})"
                                           class="btn btn-success permissions"><i class="fa fa-pencil-square-o"></i>&nbsp;编辑</a>
                                        <a href="javascript:void(0)" onclick="doRepair(${tenant.id?if_exists})"
                                           class="btn btn-info permissions"><i class="icon icon-edit-sign"></i>&nbsp;权限修复</a>
                                        &nbsp;&nbsp;&nbsp;&nbsp;
                                        <a href="/tenant/modules?tenantId=${tenant.id?if_exists}"
                                           class="btn btn-primary permissions"><i
                                                class="fa fa-eye-slash"></i>&nbsp;模块</a>
                                        <a href="/tenant/menus?tenantId=${tenant.id?if_exists}"
                                           class="btn btn-primary permissions"><i class="fa fa-check-square-o"></i>&nbsp;自定义菜单</a>
                                        <a href="/tenant/settings?tenantId=${tenant.id?if_exists}"
                                           class="btn btn-primary permissions"><i class="icon icon-pencil"></i>&nbsp;配置</a>
                                    </@shiro.hasPermission>
                                    <@shiro.hasPermission name="gbl_tenant.del" >
                                        &nbsp;&nbsp;&nbsp;&nbsp;
                                        <a href="javascript:void(0)" onclick="doDelete(${tenant.id?default(0)?c})"
                                           class="btn btn-danger"><i class="fa fa-fw fa-remove"></i>&nbsp;删除</a>
                                    </@shiro.hasPermission>
                                </td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>
                <div class="box-footer" style="text-align:center">
                    <@html.paging page=page param=param action="/tenant/tenants"/>
                </div>
            </div>
        </div>
    </div>
</div>
<footer class="main-footer">
    <@common.footer />
</footer>
</div>

    <@operate.editview "编辑租户">
    <div class="modalForm" style="padding:10px;">
        <div class="form-group">
            <label class="col-sm-3  control-label">租户名称</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="edit_tenantName" name="edit_tenantName">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm" style="padding:10px;">
        <div class="form-group">
            <label class="col-sm-3  control-label">租户CODE</label>
            <div class="col-sm-8">
                <input type="text" readonly class="form-control" id="edit_tenantCode" name="edit_tenantCode">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm" style="padding:10px;">
        <div class="form-group">
            <label class="col-sm-3 control-label">备注信息</label>
            <div class="col-sm-8">
                <textarea rows="5" class="form-control" id="edit_tenantRemark" name="edit_tenantRemark"></textarea>
                <span class="help-block"><span>
            </div>
        </div>
    </div>
    </@operate.editview>
    <@operate.editjs "saveEdit">
    $("#edit_tenantName").val($(elem).parent().siblings().eq(0).html());
    $("#edit_tenantCode").val($(elem).parent().siblings().eq(1).html());
    $("#edit_tenantRemark").text($(elem).parent().siblings().eq(2).html());
    </@operate.editjs>
<script>
    function saveEdit(tenantId) {
        var name = $("#edit_tenantName").val();
        var remark = $("#edit_tenantRemark").val();
        $.post(
                "/api/tenant/update",
                {id: tenantId, name: name, remark: remark},
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#deptEditor").modal("hide");
                        window.location.href = "/tenant/tenants?pageIndex=" + ${page.pageIndex?default(0)?c};
                    }
                },
                "json"
        );
    }
    function doDelete(tenantId) {
        warningModal("确定要删除该租户吗?", function () {
            $.get(
                    "/api/tenant/delete?id=" + tenantId,
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);
                        } else {
                            $("#deptEditor").modal("hide");
                            window.location.href = "/tenant/tenants?pageIndex=" + ${page.pageIndex?default(0)?c};
                        }
                    },
                    "json"
            );
        });
    }
    function doRepair(tenantId) {
        confirmModal("修复权限将<b><font color='red'>重置</font></b>该租户的管理员账号及其权限信息，<br><br><br>你确定要修改该租户的权限吗?", function () {
            $.get(
                    "/api/tenant/repair?id=" + tenantId,
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);
                        } else {
                            alert(data.result.notes);
                        }
                    },
                    "json"
            );
        });
    }
    function addTenant() {
        var name = $("#tenantName").val();
        var code = $("#tenantCode").val();
        var username = $("#adminUsername").val();
        var password = $("#adminPassword").val();
        var firstName = $("#adminFirstName").val();
        var lastName = $("#adminLastName").val();
        var phone = $("#adminPhone").val();
        var templateId = null;
        <#if templates?? && (templates?size > 0) >
            templateId = $("#tenantTemplate").val();
        </#if>
        var remark = $("#tenantRemark").val();
        $.post(
                "/api/tenant/add",
                {
                    name: name,
                    code: code,
                    templateId: templateId,
                    remark: remark,
                    username: username,
                    password: password,
                    firstName: firstName,
                    lastName: lastName,
                    phone: phone
                },
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#roleAddModal").modal("hide");
                        window.location.href = "/tenant/tenants";
                    }
                }
        );
    }
</script>

    <@operate.add "新增租户" "#tenantAddButton" "addTenant">
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">租户名称</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="tenantName" name="tenantName">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">租户CODE</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="tenantCode" name="tenantCode">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
        <#if templates?? && (templates?size > 0) >
        <div class="modalForm">
            <div class="form-group">
                <label class="col-sm-3  control-label">预设模板</label>
                <div class="col-sm-8">
                    <select class="form-control" id="tenantTemplate">
                        <#list templates as t>
                            <option value="">不使用预设</option>
                            <option value="${t.id?c}">${t.name}</option>
                        </#list>
                    </select>
                </div>
            </div>
        </div>
        </#if>
    <hr>

    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">管理员登录账号</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="adminUsername" name="adminUsername" placeholder="admin">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">管理员登录密码</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="adminPassword" name="adminPassword" placeholder="123456">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">管理员姓名</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="adminLastName" name="adminLastName" placeholder="姓">
                <span class="help-block"></span>
            </div>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="adminFirstName" name="adminFirstName" placeholder="名">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">管理员手机号码</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="adminPhone" name="adminPhone" placeholder="同用户名（若用户名为手机号）">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <hr>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3 control-label">备注信息</label>
            <div class="col-sm-8">
                <textarea rows="5" class="form-control" id="tenantRemark" name="tenantRemark"></textarea>
                <span class="help-block"><span>
            </div>
        </div>
    </div>
    </@operate.add>

</@macroCommon.html>