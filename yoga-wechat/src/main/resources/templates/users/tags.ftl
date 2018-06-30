<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<@macroCommon.html>
<body>
<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>微信公众号</a></li>
            <li>标签管理</li>
        </ol>
    </div>
    <div class="row content-bottom">
        <div class="col-sm-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="icon icon-user"></i>标签管理
                </div>
                <div class="panel-body">
                    <div class="tableToolContainer" style="margin-bottom:15px">
                        <form action="/wechat/users/tags" method="POST" class="form-inline" id="filter_form">
                            <div class="form-group">
                                <label class="exampleInputAccount4" style="margin-right: 20px">微信账号</label>
                                <select class="form-control" style="min-width: 200px" id="accountId" name="accountId" onchange="loadAccount()">
                                    <#list accounts?if_exists as account>
                                        <option value="${account.id?default(0)}">${account.name?if_exists}</option>
                                    </#list>
                                </select>
                                <script>
                                    $("#accountId").val('${param.accountId?default(0)}');
                                    function loadAccount() {
                                        $("#filter_form").submit();
                                    }
                                </script>
                            </div>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <div class="form-group">
                                <div class="form-group">
                                    <div class="form-group">
                                        <label>标签名称</label>
                                        &nbsp;&nbsp;
                                        <input type="text" class="form-control" name="name"
                                               value="${param.name?if_exists}">
                                    </div>
                                </div>
                            </div>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <div class="form-group">
                                <button type="submit" class="btn btn-success"><i
                                        class="fa fa-fw fa-search"></i>搜索
                                </button>
                            </div>
                            <@shiro.hasPermission name="wx_tags.update" >
                                &nbsp;&nbsp;&nbsp;&nbsp;
                                <div style="float: right">
                                    <a type="input" id="tagAddButton" class="btn btn-primary">
                                        <i class="fa fa-fw fa-plus-circle"></i>添加
                                    </a>
                                </div>
                            </@shiro.hasPermission>
                        </form>
                    </div>
                    <table class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th style="width:50px" class="tableCenter">编号</th>
                            <th style="width:40%">标签名称</th>
                            <th style="width:20%" class="tableCenter">成员数量</th>
                            <th class="tableCenter">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list tags?if_exists as tag>
                            <tr>
                                <td class="tableCenter">${tag.id?if_exists?c}</td>
                                <td>${tag.name?if_exists}</td>
                                <td class="tableCenter">${tag.count?if_exists}</td>
                                <td class="tableCenter">
                                    <@shiro.hasPermission name="wx_tags.update" >
                                        <a href="javascript:void(0)"
                                           onclick="doEdit(this, ${tag.id?default(0)?c})"
                                           class="btn btn-sm btn-primary">
                                            <i class="fa fa-fw  fa-edit"></i>编辑</a>
                                        <a href="javascript:void(0)"
                                           onclick="doDelete(${tag.id?default(0)?c})"
                                           class="btn btn-sm btn-danger">
                                            <i class="fa fa-fw  fa-remove"></i>删除</a>
                                    </@shiro.hasPermission>
                                    <@shiro.hasPermission name="wx_users.update" >
                                        <a href="/wechat/users/tags/users?accountId=${param.accountId?default(0)?c}&tagId=${tag.id?default(0)?c}" style="margin-left: 20px" class="btn btn-sm btn-info">
                                            <i class="fa fa-fw fa-adjust"></i>用户列表</a>
                                    </@shiro.hasPermission>
                                </td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>
                <div class="box-footer" style="text-align:center">
                </div>
            </div>
        </div>
    </div>
</div>
<footer class="main-footer">
    <@common.footer />
</footer>
</body>

    <@operate.add "新增标签" "#tagAddButton" "addTag">
    <div class="modalForm" style="margin-top: 15px">
        <div class="form-group">
            <label class="col-sm-3  control-label">名称</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="new_name">
            </div>
        </div>
    </div>
    </@operate.add>
    <script>
        function addTag() {
            var name = $("#new_name").val();
            $.post(
                    "/api/wechat/users/tags/add",
                    {
                        accountId: ${param.accountId?c},
                        name: name
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

    <@operate.editview "编辑标签">
    <div class="modalForm" style="margin-top: 15px">
        <div class="form-group">
            <label class="col-sm-3  control-label">名称</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="edit_name">
            </div>
        </div>
    </div>
    </@operate.editview>

    <@operate.editjs "saveEdit">
    $("#edit_name").val($(elem).parent().siblings().eq(1).html());
    </@operate.editjs>
<script>
    function saveEdit(id) {
        var name = $("#edit_name").val();
        $.post(
                "/api/wechat/users/tags/update",
                {
                    accountId: ${param.accountId?c},
                    id: id,
                    name: name
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
        warningModal("确定要删除该标签吗?", function () {
            $.post(
                    "/api/wechat/users/tags/delete",
                    {accountId: ${param.accountId?c}, id: id},
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