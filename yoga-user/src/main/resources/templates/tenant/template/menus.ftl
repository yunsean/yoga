<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<@macroCommon.html>
<body>
<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="/tenant/templates"><i class="fa fa-dashboard"></i>模板管理</a></li>
            <li>自定义菜单</li>
            <span class="pull-right">
                <button class="btn btn-primary btn-sm" onclick="history.back(-1);">返回</button>
            </span>
        </ol>
    </div>
    <div class="row content-bottom">
        <div class="col-sm-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="fa fa-comments-o"></i>
                    模板菜单设置
                </div>
                <div class="panel-body">
                    <div class="tableToolContainer" style="margin-bottom: 15px">
                        <@shiro.hasPermission name="gbl_tenant.add" >
                            <a href="#" style="float: right" class="btn btn-primary" id="tenantAddButton">
                                <i class="fa fa-fw fa-plus"></i>添加
                            </a>
                        </@shiro.hasPermission>
                    </div>
                    <table class="table table-bordered ">
                        <thead>
                        <tr>
                            <th style="width:10">版块</th>
                            <th style="width:15%">菜单项</th>
                            <th style="width:25%">路径</th>
                            <th style="width:30%">备注</th>
                            <th style="width:20%">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list menus as menu>
                            <tr style="background-color:#eaeaea">
                                <td><b style="font-size:14px">${menu.name?if_exists}</b></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
                                <#list menu.children as child>
                                <tr>
                                    <td></td>
                                    <td>${child.name?if_exists}</td>
                                    <td>${child.url?if_exists}</td>
                                    <td>${child.remark?if_exists}</td>
                                    <input type="hidden" value="${menu.name?if_exists}"/>
                                    <input type="hidden" value="${child.code?if_exists}"/>
                                    <input type="hidden" value="${child.sort?if_exists}"/>
                                    <td>
                                        <a href="javascript:void(0)" onclick="doEdit(this, ${child.menuId?default(0)?c})"
                                           class="btn btn-success permissions"><i class="fa fa-pencil-square-o"></i>&nbsp;编辑</a>
                                        <a href="javascript:void(0)" onclick="doDelete(${child.menuId?default(0)?c})"
                                           class="btn btn-danger"><i class="fa fa-fw fa-remove"></i>&nbsp;删除</a>
                                    </td>
                                </tr>
                                </#list>
                            </#list>
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

    <@operate.editview "编辑菜单">
    <div class="modalForm" style="padding:10px;">
        <div class="form-group">
            <label class="col-sm-3  control-label">所属板块</label>
            <div class="col-sm-4">
                <label style="display: inline">
                    <input type="radio" name="edit_group_from" id="edit_groupInput" checked>
                    <input type="text" class="form-control" id="edit_menuGroupText" style="width: 80%; display: inline-block; margin-left: 5px">
                </label>
            </div>
            <div class="col-sm-4">
                <label>
                    <input type="radio" name="edit_group_from" id="edit_groupChoice">
                    <select class="form-control" id="edit_menuGroupSelect" style="width: 80%; display: inline-block; margin-left: 5px">
                        <#list groups as group>
                            <option value="${group}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${group}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
                        </#list>
                    </select>
                </label>
            </div>
        </div>
    </div>
    <div class="modalForm" style="padding:10px;">
        <div class="form-group">
            <label class="col-sm-3  control-label">菜单名称</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="edit_menuName"">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm" style="padding:10px;">
        <div class="form-group">
            <label class="col-sm-3  control-label">权限编码</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="edit_menuCode"">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm" style="padding:10px;">
        <div class="form-group">
            <label class="col-sm-3  control-label">排序值</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="edit_menuSort"">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm" style="padding:10px;">
        <div class="form-group">
            <label class="col-sm-3  control-label">菜单URL</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="edit_menuUrl">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm" style="padding:10px;">
        <div class="form-group">
            <label class="col-sm-3 control-label">备注信息</label>
            <div class="col-sm-8">
                <textarea rows="5" class="form-control" id="edit_menuRemark"></textarea>
                <span class="help-block"><span>
            </div>
        </div>
    </div>
    </@operate.editview>
    <@operate.editjs "saveEdit">
    $("#edit_menuGroupText").val($(elem).parent().siblings().eq(4).val());
    $("#edit_menuCode").val($(elem).parent().siblings().eq(5).val());
    $("#edit_menuName").val($(elem).parent().siblings().eq(1).html());
    $("#edit_menuUrl").val($(elem).parent().siblings().eq(2).html());
    $("#edit_menuRemark").text($(elem).parent().siblings().eq(3).html());
    $("#edit_menuSort").val($(elem).parent().siblings().eq(6).val());
    </@operate.editjs>
<script>
    function saveEdit(id) {
        var name = $("#edit_menuName").val();
        var code = $("#edit_menuCode").val();
        var url = $("#edit_menuUrl").val();
        var remark = $("#edit_menuRemark").val();
        var sort = $("#edit_menuSort").val();
        if ($('input:radio[id="edit_groupInput"]:checked').val()) {
            var group = $("#edit_menuGroupText").val();
        } else {
            var group = $("#edit_menuGroupSelect").val();
        }
        $.post(
                "/api/tenant/template/menu/update",
                {menuId: id, group: group, code: code, name: name, url: url, remark: remark, sort: sort},
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#operate_edit_modal").modal("hide");
                        window.location.reload();
                    }
                },
                "json"
        );
    }
    function doDelete(id) {
        warningModal("确定要删除该菜单项吗?", function () {
            $.get(
                    "/api/tenant/template/menu/delete?menuId=" + id,
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
</script>
    <@operate.add "新增菜单" "#tenantAddButton" "addMenu">
    <div class="modalForm" style="padding:10px;">
        <div class="form-group">
            <label class="col-sm-3  control-label">所属板块</label>
            <div class="col-sm-4">
                <label>
                    <input type="radio" name="group_from" id="groupInput" checked>
                </label>
                <label>
                    <input type="text" class="form-control" id="menuGroupText" style="width: 80%; display: inline-block; margin-left: 5px">
                </label>
            </div>
            <div class="col-sm-4">
                <label>
                    <input type="radio" name="group_from" id="groupChoice">
                </label>
                <label>
                    <select class="form-control" id="menuGroupSelect" style="width: 80%; display: inline-block; margin-left: 5px">
                        <#list groups as group>
                            <option value="${group}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${group}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
                        </#list>
                    </select>
                </label>
            </div>
        </div>
    </div>
    <div class="modalForm" style="padding:10px;">
        <div class="form-group">
            <label class="col-sm-3  control-label">菜单名称</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="menuName"">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm" style="padding:10px;">
        <div class="form-group">
            <label class="col-sm-3  control-label">权限编码</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="menuCode"">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm" style="padding:10px;">
        <div class="form-group">
            <label class="col-sm-3  control-label">排序值</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="menuSort"">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm" style="padding:10px;">
        <div class="form-group">
            <label class="col-sm-3  control-label">菜单URL</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="menuUrl">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm" style="padding:10px;">
        <div class="form-group">
            <label class="col-sm-3 control-label">备注信息</label>
            <div class="col-sm-8">
                <textarea rows="5" class="form-control" id="menuRemark"></textarea>
                <span class="help-block"><span>
            </div>
        </div>
    </div>
    </@operate.add>
<script>
    function addMenu() {
        var name = $("#menuName").val();
        var code = $("#menuCode").val();
        var url = $("#menuUrl").val();
        var sort = $("#menuSort").val();
        var remark = $("#menuRemark").val();
        if ($('input:radio[id="groupInput"]:checked').val()) {
            var group = $("#menuGroupText").val();
        } else {
            var group = $("#menuGroupSelect").val();
        }
        $.post(
                "/api/tenant/template/menu/add",
                {templateId: ${templateId?c}, code: code, group: group, name: name, url: url, remark: remark, sort: sort},
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#operate_add_modal").modal("hide");
                        window.location.reload();
                    }
                }
        );
    }
</script>

</body>
</@macroCommon.html>