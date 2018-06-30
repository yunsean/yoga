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
            <li><i class="icon icon-dashboard"></i>租户模板</li>
        </ol>
    </div>
    <div class="row content-bottom">
        <div class="col-sm-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="fa fa-comments-o"></i>
                    租户模板
                </div>
                <div class="panel-body">
                    <div class="tableToolContainer" style="margin-bottom:15px">
                        <form action="/tenant/tenants" class="form-inline">
                            <div class="form-group">
                                <label class="exampleInputAccount4">&nbsp;&nbsp;模板名称：&nbsp;</label>
                                <input type="text" class="form-control" name="name" id="name" value="${param.name?if_exists}">
                            </div>
                            &nbsp;&nbsp;
                            <button class="btn btn-success" type="submit">
                                <i class="fa fa-fw fa-search"></i>搜索
                            </button>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                        <@shiro.hasPermission name="gbl_tenant_template.add" >
                            <a style="float: right" href="#" class="btn btn-primary" id="templateAddButton">
                                <i class="fa fa-fw fa-plus"></i>添加
                            </a>
                        </@shiro.hasPermission>
                        </form>
                    </div>
                    <table id="" class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th style="width:50px">#</th>
                            <th style="width:20%">名称</th>
                            <th style="width:30%" class="tableLeft">备注</th>
                            <th style="width:40%" class="tableCenter">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list templates as tenant>
                        <tr>
                            <td>${tenant.id?if_exists}</td>
                            <td>${tenant.name?if_exists}</td>
                            <td class="tableLeft">${tenant.remark?if_exists}</td>
                            <td class="tableCenter">
                            <@shiro.hasPermission name="gbl_tenant_template.update" >
                                <a href="javascript:void(0)" onclick="doEdit(this, ${tenant.id?if_exists})"
                                   class="btn btn-success permissions"><i class="fa fa-pencil-square-o"></i>&nbsp;编辑</a>
                                &nbsp;&nbsp;&nbsp;&nbsp;
                                <a href="/tenant/template/modules?templateId=${tenant.id?if_exists}"
                                   class="btn btn-primary permissions"><i class="fa fa-eye-slash"></i>&nbsp;模块</a>
                                <a href="/tenant/template/menus?templateId=${tenant.id?if_exists}"
                                   class="btn btn-primary permissions"><i class="fa fa-check-square-o"></i>&nbsp;自定义菜单</a>
                                <a href="/tenant/template/settings?tenantId=${tenant.id?if_exists}"
                                   class="btn btn-primary permissions"><i class="icon icon-pencil"></i>&nbsp;配置</a>
                            </@shiro.hasPermission>
                            <@shiro.hasPermission name="gbl_tenant_template.del" >
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
                <@html.paging page=page param=param action="/tenant/templates"/>
                </div>
            </div>
        </div>
    </div>
</div>
<footer class="main-footer">
<@common.footer />
</footer>
</div>

<@operate.editview "编辑模板">
<div class="modalForm" style="padding:10px;">
    <div class="form-group">
        <label class="col-sm-3  control-label">模板名称</label>
        <div class="col-sm-8">
            <input type="text" class="form-control" id="edit_tenantName" name="edit_tenantName">
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
    $("#edit_tenantRemark").text($(elem).parent().siblings().eq(2).html());
</@operate.editjs>
<script>
    function saveEdit(tenantId) {
        var name = $("#edit_tenantName").val();
        var remark = $("#edit_tenantRemark").val();
        $.post(
                "/api/tenant/template/update",
                {id:tenantId, name:name, remark: remark},
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        window.location.reload();
                    }
                },
                "json"
        );
    }
    function doDelete(templateId) {
        warningModal("确定要删除该模板吗?", function(){
            $.get(
                    "/api/tenant/template/delete?id=" + templateId,
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);
                        } else {
                            window.location.reload();
                        }
                    },
                    "json"
            );
        });
    }
    function addTemplate() {
        var name = $("#tenantName").val();
        var remark = $("#tenantRemark").val();
        $.post(
                "/api/tenant/template/add",
                {name: name, remark: remark},
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        window.location.reload();
                    }
                }
        );
    }
</script>

<@operate.add "新增模板" "#templateAddButton" "addTemplate">
<div class="modalForm" style="padding:10px;">
    <div class="form-group">
        <label class="col-sm-3  control-label">模板名称</label>
        <div class="col-sm-8">
            <input type="text" class="form-control" id="tenantName" name="tenantName">
            <span class="help-block"></span>
        </div>
    </div>
</div>
<div class="modalForm" style="padding:10px;">
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