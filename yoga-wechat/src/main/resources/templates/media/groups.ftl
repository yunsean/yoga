<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<@macroCommon.html>
<link href="<@macroCommon.resource/>/uploader/zui.uploader.css" rel="stylesheet">
<body>
<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>公众号素材管理</a></li>
            <li>素材组</li>
        </ol>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="icon icon-user"></i>素材组
                </div>
                <div class="panel-body">
                    <div class="tableToolContainer" style="margin-bottom:15px">
                        <form action="/wechat/material/group" method="POST" class="form-inline">
                            <div class="form-group">
                                <div class="form-group">
                                    <div class="form-group">
                                        <label class="exampleInputAccount4">名称</label>
                                        &nbsp;&nbsp;
                                        <input type="text" class="form-control" name="name" value="${param.name?if_exists}">
                                    </div>
                                </div>
                            </div>
                            &nbsp;&nbsp;
                            <div class="form-group">
                                <button type="submit" class="btn btn-success"><i
                                        class="fa fa-fw fa-search"></i>搜索
                                </button>
                            </div>
                            <@shiro.hasPermission name="wx_material_group.update" >
                                &nbsp;&nbsp;&nbsp;&nbsp;
                                <div class="form-group">
                                    <a type="input" id="groupAddButton" class="btn btn-primary" style="margin-left: 10px">
                                        <i class="fa fa-fw fa-upload"></i>新建分组
                                    </a>
                                </div>
                            </@shiro.hasPermission>
                        </form>
                    </div>
                    <table class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th style="width:30%">名称</th>
                            <th style="width:45%">描述</th>
                            <th class="tableCenter">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list groups?if_exists as group>
                            <tr>
                                <td>${group.name?if_exists}</td>
                                <td>${group.remark?if_exists}</td>
                                <td class="tableCenter">
                                    <@shiro.hasPermission name="wx_material_group.update" >
                                        <a onclick="doEdit(this, ${group.id?if_exists?c})"
                                           class="btn btn-sm btn-primary">
                                            <i class="fa fa-fw fa-edit"></i>修改</a>
                                        <a href="javascript:void(0)" style="margin-right: 20px"
                                           onclick="doDelete(${group.id?if_exists?c})"
                                           class="btn btn-sm btn-danger">
                                            <i class="fa fa-fw fa-remove"></i>删除</a>
                                    </@shiro.hasPermission>
                                </td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>
                <div class="box-footer" style="text-align:center">
                    <@html.paging page=page param=param action="/wechat/material/group"/>
                </div>
            </div>
        </div>
    </div>
</div>
<footer class="main-footer">
    <@common.footer />
</footer>
</body>


    <@operate.add "新增分组" "#groupAddButton" "addGroup">
    <div class="modalForm" style="margin-top: 15px">
        <div class="form-group">
            <label class="col-sm-3  control-label">名称</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="new_name">
            </div>
        </div>
    </div>
    <div class="modalForm" style="margin-top: 15px">
        <div class="form-group">
            <label class="col-sm-3  control-label">描述</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="new_remark">
            </div>
        </div>
    </div>
    </@operate.add>
<script>
    function addGroup() {
        var name = $("#new_name").val();
        var remark = $("#new_remark").val();
        $.post(
                "/api/wechat/material/group/add",
                {
                    name: name,
                    remark: remark
                },
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
</script>

    <@operate.editview "编辑分组">
    <div class="modalForm" style="margin-top: 15px">
        <div class="form-group">
            <label class="col-sm-3  control-label">名称</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="edit_name">
            </div>
        </div>
    </div>
    <div class="modalForm" style="margin-top: 15px">
        <div class="form-group">
            <label class="col-sm-3  control-label">描述</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="edit_remark">
            </div>
        </div>
    </div>
    </@operate.editview>

    <@operate.editjs "saveEdit">
    $("#edit_name").val($(elem).parent().siblings().eq(0).html());
    $("#edit_remark").val($(elem).parent().siblings().eq(1).html());
    </@operate.editjs>
<script>
    function saveEdit(id) {
        var name = $("#edit_name").val();
        var remark = $("#edit_remark").val();
        $.post(
                "/api/wechat/material/group/update",
                {
                    id: id,
                    name: name,
                    remark: remark
                },
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
    function doDelete(id) {
        warningModal("确定要删除该分组吗?", function () {
            $.post(
                    "/api/wechat/material/group/delete",
                    {id: id},
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);;
                        } else {
                            window.location.reload();
                        }
                    }
            );
        });
    }
</script>

</@macroCommon.html>