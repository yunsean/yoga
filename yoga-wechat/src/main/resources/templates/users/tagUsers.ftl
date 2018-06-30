<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<@macroCommon.html>
<body>
<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>微信公众号</a></li>
            <li>用户管理</li>
        </ol>
    </div>
    <div class="row content-bottom">
        <div class="col-sm-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="icon icon-user"></i>用户管理
                </div>
                <div class="panel-body">
                    <div class="tableToolContainer" style="margin-bottom:15px">
                        <form action="/wechat/users/tags/users" method="POST" class="form-inline">
                            <input type="hidden" name="accountId" value="${param.accountId?default(0)?c}">
                            <input type="hidden" name="tagId" value="${param.tagId?default(0)?c}">
                            <div class="form-group">
                                <div class="form-group">
                                    <div class="form-group">
                                        <label class="exampleInputAccount4">用户昵称</label>
                                        &nbsp;&nbsp;
                                        <input type="text" class="form-control" name="nickname"
                                               value="${param.nickname?if_exists}">
                                    </div>
                                </div>
                            </div>
                            &nbsp;&nbsp;
                            <div class="form-group">
                                <div class="form-group">
                                    <div class="form-group">
                                        <label class="exampleInputAccount4">性别</label>
                                        &nbsp;&nbsp;
                                        <select type="text" class="form-control" name="gender" id="filter_gender">
                                            <option value="">全部</option>
                                            <option value="unknown">未知</option>
                                            <option value="male">男</option>
                                            <option value="female">女</option>
                                        </select>
                                        <script>
                                            $("#filter_gender").val('${param.gender?if_exists}');
                                        </script>
                                    </div>
                                </div>
                            </div>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <div class="form-group">
                                <button type="submit" class="btn btn-success"><i
                                        class="fa fa-fw fa-search"></i>搜索
                                </button>
                            </div>
                            <@shiro.hasPermission name="wx_users.update" >
                                &nbsp;&nbsp;&nbsp;&nbsp;
                                <div class="form-group" style="float: right">
                                    <button type="button" class="btn btn-danger" onclick="batchDelete()">
                                        <i class="fa fa-fw fa-remove"></i>移除选中用户
                                    </button>
                                </div>
                            </@shiro.hasPermission>
                        </form>
                    </div>
                    <table class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th style="width:15px"><input type="checkbox" id="all_checked" onchange="checkAll(this);">
                            </th>
                            <th style="width:15%">用户昵称</th>
                            <th style="width:100px" class="tableCenter">是否关注</th>
                            <th style="width:100px" class="tableCenter">性别</th>
                            <th style="width:20%" class="tableCenter">地区</th>
                            <th style="width:100px" class="tableCenter">语言</th>
                            <th style="width:15%">备注</th>
                            <th class="tableCenter">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list users?if_exists as user>
                            <tr>
                                <td><input type="checkbox" name="user_id_item" value="${user.openId?if_exists}"></td>
                                <td>${user.nickname?if_exists}</td>
                                <td class="tableCenter">${user.subscribe?if_exists?string('已关注', '未关注')}</td>
                                <td class="tableCenter">${user.sex.getName()?if_exists}</td>
                                <td class="tableCenter">${user.country?if_exists}${user.province?if_exists}${user.city?if_exists}</td>
                                <td class="tableCenter">${user.language?if_exists}</td>
                                <td>${user.remark?if_exists}</td>
                                <td class="tableCenter">
                                    <@shiro.hasPermission name="wx_users.update" >
                                        <a href="javascript:void(0)" style="margin-left: 20px"
                                           onclick="doDelete('${user.openId?if_exists}')"
                                           class="btn btn-sm btn-danger">
                                            <i class="fa fa-fw fa-remove"></i>移除标签</a>
                                    </@shiro.hasPermission>
                                </td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>
                <div class="box-footer" style="text-align:center">
                    <@html.paging page=page param=param action="/wechat/users/tags/users"/>
                </div>
            </div>
        </div>
    </div>
</div>
<footer class="main-footer">
    <@common.footer />
</footer>
</body>

<script>
    function checkAll(elem) {
        $("input[name='user_id_item']").prop("checked", $("#all_checked").prop('checked'));
    }
    function batchDelete() {
        var openIds = "";
        $("[name='user_id_item']:checked").each(function () {
            openIds += "," + $(this).val();
        });
        if (openIds.length > 0) openIds = openIds.substr(1);
        $.post("/api/wechat/users/tags/user/remove",
                {
                    accountId: ${param.accountId?if_exists?c},
                    tagId: ${param.tagId?if_exists?c},
                    openIds: openIds
                },
                function (data) {
                    if (data.code < 0) {
                        alertShow("warning", data.message, 3000);
                    } else {
                        window.location.reload();
                    }
                },
                "json"
        );
    }
</script>

<div class="modal fade" id="tags_add_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" style="width:50%">
        <form class="form-horizontal" id="tags_add_modal_form">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title">
                        添加标签
                    </h4>
                </div>
                <div class="modal-body" id="tags_add_modal_body">
                    <#list tags as tag>
                        <input type="radio" name="add_tag_item" value="${tag.id?if_exists}"
                               style="min-width: 200px; margin-left: 20px; margin-right: 10px">${tag.name?if_exists}
                    </#list>
                </div>
                <div class="modal-footer">
                    <a href="javascript:void(0)" class="btn btn-danger" data-dismiss="modal"><i
                            class="fa fa-fw fa-remove"></i></button>关闭</a>
                    <a href="javascript:void(0)" id="tags_add_modal_submit" class="btn btn-info"><i
                            class="fa fa-fw  fa-save"></i>保存</a>
                </div>
            </div>
        </form>
    </div>
</div>
<script>
    function addTag(openId) {
        $("#tags_add_modal_submit").attr("onclick", "doAddTag('" + openId + "')");
        $("#tags_add_modal").modal("show");
        $("#tags_add_modal_form")[0].reset();
    }
    function doAddTag(openId) {
        var tagId = $("input[name='add_tag_item']:checked").val();
        if (tagId == null) return;
        $.post(
                "/api/wechat/users/tag",
                {
                    accountId: ${param.accountId?default(0)?c},
                    openId: openId,
                    tagId: tagId
                },
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        alertShow("info", "添加标签成功！", 3000);
                        $("#tags_add_modal").modal("hide");
                    }
                }
        );
    }
</script>

<div class="modal fade" id="tags_list_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" style="width:50%">
        <form class="form-horizontal" id="tags_list_modal_form">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title">
                        用户标签
                    </h4>
                </div>
                <div class="modal-body" id="tags_list_model_body">
                </div>
                <div class="modal-footer">
                    <a href="javascript:void(0)" class="btn btn-danger" data-dismiss="modal"><i
                            class="fa fa-fw fa-remove"></i></button>关闭</a>
                </div>
            </div>
        </form>
    </div>
</div>
<script>
    function doTag(openId) {
        $.get(
                "/api/wechat/users/tags?accountId=${param.accountId?default(0)?c}&openId=" + openId,
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        var tbody = $("#tags_list_model_body");
                        tbody.empty();
                        for (var i = 0; i < data.result.length; i++) {
                            var tag = data.result[i];
                            tbody.append('<span class="btn btn-sm" style="margin-left: 20px;">' + tag.name + '<i onclick="removeTag(this, \'' + openId + '\', ' + tag.id + ');" style="margin-left: 5px" class="icon-remove-circle"></i></span>');
                        }
                        $("#tags_list_modal").modal("show");
                    }
                }
        );
    }
    function removeTag(elem, openId, tagId) {
        $.post(
                "/api/wechat/users/untag",
                {
                    accountId: ${param.accountId?default(0)?c},
                    openId: openId,
                    tagId: tagId
                },
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $(elem).parent().remove();
                    }
                }
        );
    }
    function doRefresh() {
        $.get(
                "/api/wechat/users/refresh?accountId=${param.accountId}",
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        alertShow("info", "提交刷新请求成功，请等待一段时候重新刷新页面！", 3000);
                    }
                }
        );
    }
</script>
</@macroCommon.html>