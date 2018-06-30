<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<@macroCommon.html>
<body>
<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>内容管理</a></li>
            <li>模版管理</li>
        </ol>
    </div>
    <div class="row content-bottom">
        <div class="col-lg-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="fa fa-comments-o"></i>
                    模版列表
                </div>
                <div class="panel-body">
                    <div class="tableToolContainer" style="margin-bottom:15px">
                        <form class="form-inline">
                            <div class="form-group">
                                <label>模板名称</label>
                                <input type="text" class="form-control" name="filter" style="margin-left: 10px">
                            </div>
                            <button class="btn btn-success" type="submit"><i class="icon icon-user"></i>搜索
                            </button>
                            <@shiro.hasPermission name="cms_template.add" >
                                <a href="#" class="btn btn-primary" id="templateAddButton" style="float: right">
                                    <i class="fa fa-fw fa-plus "></i>添加</a>
                            </@shiro.hasPermission>
                        </form>
                    </div>

                    <table id="" class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th style="width:30%">模版名称</th>
                            <th style="width:20%" class="tableLeft">模板CODE</th>
                            <th style="width:10%" class="tableCenter">是否启用</th>
                            <th class="tableCenter">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list templates as template>
                            <tr>
                                <td>${template.name}</td>
                                <td class="tableLeft">${template.code?if_exists}</td>
                                <td class="tableCenter">
                                    <#if template.enabled>√</#if>
                                </td>
                                <input type="hidden" value="${template.enabled?default(0)?c}">
                                <input type="hidden" value="${template.remark?if_exists}">
                                <td class="tableCenter">
                                    <@shiro.hasPermission name="cms_template.update" >
                                        <a href="/cms/template/field?templateId=${template.id?c}" class="btn btn-primary caseReport">
                                            <i class="fa fa-fw fa-cog "></i>字段管理</a>
                                        <a href="#" class="btn btn-info caseReport" onclick="doEdit(this, ${template.id?default(0)?c})">
                                            <i class="fa fa-fw fa-pencil "></i>修改</a>
                                    </@shiro.hasPermission>
                                    <@shiro.hasPermission name="cms_template.del" >
                                        <a href="#" class="btn btn-danger caseReport" onclick="delTemplate(${template.id?default(0)?c})">
                                            <i class="fa fa-fw fa-remove "></i>删除</a>
                                    </@shiro.hasPermission>
                                    <@shiro.hasPermission name="cms_template.update" >
                                        <a style="margin-left: 20px" class="btn btn-primary m30px" href="/cms/layout?templateId=${template.id?default(0)?c}">
                                            <i class="fa fa-fw fa-list-alt "></i>布局</a>
                                    </@shiro.hasPermission>
                                </td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>
                <div class="box-footer" style="text-align:center">
                    <@html.paging page=page param=param action="/content/allTemplate"/>
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

<@operate.add "新增模板" "#templateAddButton" "addTemplate">
<div class="modalForm" style="margin-top: 15px">
    <div class="form-group">
        <label class="col-sm-3  control-label">模板名称</label>
        <div class="col-sm-8">
            <input type="text" class="form-control" id="new_name">
        </div>
    </div>
</div>
<div class="modalForm">
    <div class="form-group">
        <label class="col-sm-3  control-label">模板CODE</label>
        <div class="col-sm-8">
            <input type="text" id="new_code" class="form-control">
        </div>
    </div>
</div>
<div class="modalForm">
    <div class="form-group">
        <label class="col-sm-3  control-label">模板描述</label>
        <div class="col-sm-8">
            <textarea name="" class="form-control" id="new_remark"></textarea>
            <span class="help-block"></span>
        </div>
    </div>
</div>
<div class="modalForm">
    <div class="form-group">
        <label class="col-sm-3  control-label">启用</label>
        <div class="col-sm-8" style="padding-top:5px;">
            &nbsp;&nbsp;<input type="radio" name="new_enabled" id="new_enabled" checked>是&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="radio" name="new_enabled" id="new_disabled">否
        </div>
    </div>
</div>
</@operate.add>
<script>
    function addTemplate() {
        var name = $("#new_name").val();
        var code = $("#new_code").val();
        var remark = $("#new_remark").val();
        var enabled = $("#new_enabled").prop("checked");
        var pageIndex = ${page.pageIndex};
        $.post(
                "/api/cms/template/add",
                {name: name, remark: remark, code: code, enabled: enabled},
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#deptAddModal").modal("hide");
                        window.location.href = "/cms/template?pageIndex=" + pageIndex;
                    }
                }
        );
    }
    function delTemplate(id) {
        var pageIndex = ${page.pageIndex};
        warningModal("确定要删除该模板吗?", function () {
            $.post(
                    "/api/cms/template/delete",
                    {id: id},
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);;
                        } else {
                            window.location.href = "/cms/template?pageIndex=" + pageIndex;
                        }
                    }
            );
        });
    }
</script>

<@operate.editview "编辑模板">
<div class="modalForm">
    <div class="form-group">
        <label class="col-sm-3  control-label">模板名称</label>
        <div class="col-sm-8">
            <input type="text" class="form-control" id="edit_name">
        </div>
    </div>
</div>
<div class="modalForm">
    <div class="form-group">
        <label class="col-sm-3  control-label">模板CODE</label>
        <div class="col-sm-8">
            <input type="text" id="edit_code" class="form-control">
        </div>
    </div>
</div>
<div class="modalForm">
    <div class="form-group">
        <label class="col-sm-3  control-label">模板描述</label>
        <div class="col-sm-8">
            <textarea name="" class="form-control" id="edit_remark"></textarea>
            <span class="help-block"></span>
        </div>
    </div>
</div>
<div class="modalForm">
    <div class="form-group">
        <label class="col-sm-3  control-label">启用</label>
        <div class="col-sm-8" style="padding-top:5px;">&nbsp;&nbsp;
            <input type="radio" name="edit_enabled" id="edit_enabled" checked>是&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="radio" name="edit_enabled" id="edit_disabled">否
        </div>
    </div>
</div>
<input type="hidden" id="edit_templateId" value="">
</@operate.editview>
<@operate.editjs "saveEdit">
var enabled = $(elem).parent().siblings().eq(3).val();
if (enabled == "true")
    $("#edit_enabled").prop("checked", true);
else
    $("#edit_disabled").prop("checked", true);
$("#edit_name").val($(elem).parent().siblings().eq(0).html());
$("#edit_code").val($(elem).parent().siblings().eq(1).html());
$("#edit_remark").val($(elem).parent().siblings().eq(4).val());
</@operate.editjs>
<script>
    function saveEdit(id) {
        var name = $("#edit_name").val();
        var code = $("#edit_code").val();
        var remark = $("#edit_remark").val();
        var enabled = $("#edit_enabled").prop("checked");
        var pageIndex = ${page.pageIndex};
        $.post(
                "/api/cms/template/update",
                {id: id, name: name, remark: remark, code: code, enabled: enabled},
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#operate_edit_modal").modal("hide");
                        window.location.href = "/cms/template?pageIndex=" + pageIndex;
                    }
                },
                "json"
        );
    }
</script>
</@macroCommon.html>