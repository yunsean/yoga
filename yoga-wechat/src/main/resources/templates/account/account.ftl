<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<@macroCommon.html>
<link href="<@macroCommon.resource/>/uploader/zui.uploader.css" rel="stylesheet" media="screen">
<script src="<@macroCommon.resource/>/uploader/zui.uploader.min.js" charset="UTF-8"></script>
<body>
<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>微信公众号</a></li>
            <li>账号管理</li>
        </ol>
    </div>
    <div class="row content-bottom">
        <div class="col-sm-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="icon icon-user"></i>账号管理
                </div>
                <div class="panel-body">
                    <div class="tableToolContainer" style="margin-bottom:15px">
                        <form action="/wechat/account" method="POST" class="form-inline">
                            <input type="hidden" name="catalog" value="${param.catalog?if_exists}">
                            <input type="hidden" name="catalogName" value="${param.catalogName?if_exists}">
                            <div class="form-group">
                                <div class="form-group">
                                    <div class="form-group">
                                        <label class="exampleInputAccount4">账号名称</label>
                                        &nbsp;&nbsp;
                                        <input type="text" class="form-control" name="name"
                                               value="${param.name?if_exists}">
                                    </div>
                                </div>
                            </div>
                            &nbsp;&nbsp;
                            <div class="form-group">
                                <div class="form-group">
                                    <div class="form-group">
                                        <label class="exampleInputAccount4">账号信息</label>
                                        &nbsp;&nbsp;
                                        <input type="text" class="form-control" name="filter"
                                               value="${param.filter?if_exists}">
                                    </div>
                                </div>
                            </div>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <div class="form-group">
                                <button type="submit" class="btn btn-success"><i
                                        class="fa fa-fw fa-search"></i>搜索
                                </button>
                                <@shiro.hasPermission name="wx_account.add" >
                                <#if maxCount <= 0 || maxCount gt accounts?size>
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <div style="float: right">
                                        <a type="input" id="channelAddButton" class="btn btn-primary">
                                            <i class="fa fa-fw fa-plus-circle"></i>添加
                                        </a>
                                    </div>
                                </#if>
                                </@shiro.hasPermission>
                            </div>
                        </form>
                    </div>
                    <#if webIp?exists>&nbsp;&nbsp;服务器公网IP：<b style="color: red">${webIp}</b>（请在微信公众号设置/基本配置/IP白名单中加入该地址）</#if>
                    <table class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th style="width:10%">账号名称</th>
                            <th style="width:10%">微信号</th>
                            <th style="width:15%">公众帐号APPID</th>
                            <th style="width:30%">回调地址</th>
                            <th class="tableCenter">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list accounts?if_exists as account>
                            <tr>
                                <td>${account.name?if_exists}</td>
                                <td>${account.number?if_exists}</td>
                                <td>${account.appId?if_exists}</td>
                                <td>${baseUrl}/api/wechat/${tenantId?c}/${account.id?c}</td>
                                <td class="tableCenter">
                                    <div id="uploaderCover" class="uploader" style="display: inline-block;margin-bottom: 0px">
                                        <div class="file-list " style="display: none" data-drag-placeholder="请拖拽文件到此处"></div>
                                        <button type="button" class="btn btn-sm btn-primary uploader-btn-browse"> 上传配置文件</button>
                                    </div>
                                    <#--<a href="javascript:void(0)"-->
                                       <#--onclick="upload()"-->
                                       <#--class="btn btn-sm btn-primary">-->
                                        <#--<i class="fa fa-fw  fa-edit"></i>上传配置文件</a>-->
                                    <@shiro.hasPermission name="wx_account.update" >
                                        <a href="javascript:void(0)"
                                           onclick="edit(${account.id?default(0)?c})"
                                           class="btn btn-sm btn-primary">
                                            <i class="fa fa-fw  fa-edit"></i>编辑</a>
                                    </@shiro.hasPermission>
                                    <@shiro.hasPermission name="wx_account.del" >
                                        <a href="javascript:void(0)"
                                           onclick="doDelete(${account.id?default(0)?c})"
                                           class="btn btn-sm btn-danger">
                                            <i class="fa fa-fw  fa-remove"></i>删除</a>
                                    </@shiro.hasPermission>
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <@shiro.hasPermission name="wx_menu.update" >
                                        <a onclick="doRefreshMenu(${account.id?default(0)?c})" class="btn btn-sm btn-primary">
                                            <i class="fa fa-fw fa-th-list"></i>同步菜单</a>
                                    </@shiro.hasPermission>
                                    <@shiro.hasPermission name="wx_menu.publish" >
                                        <a onclick="doSubmitMenu(${account.id?default(0)?c})" class="btn btn-sm btn-danger">
                                            <i class="fa fa-fw fa-th-list"></i>发布菜单</a>
                                    </@shiro.hasPermission>
                                </td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>
                <div class="box-footer" style="text-align:center">
                    <@html.paging page=page param=param action="/wechat/account"/>
                </div>
            </div>
        </div>
    </div>
</div>
<footer class="main-footer">
    <@common.footer />
</footer>
</body>

    <@operate.add "新增账号" "#channelAddButton" "addAccount">
    <div class="modalForm" style="margin-top: 15px">
        <div class="form-group">
            <label class="col-sm-3  control-label">账号名称</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="new_name">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">Token</label>
            <div class="col-sm-8">
                <input type="text" id="new_token" class="form-control">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">AESKey</label>
            <div class="col-sm-8">
                <input type="text" id="new_aes_key" class="form-control" maxlength="43" onblur="changeValue(this)">
            </div>
        </div>
    </div>

    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">微信号</label>
            <div class="col-sm-8">
                <input type="text" id="new_number" class="form-control">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">原始ID</label>
            <div class="col-sm-8">
                <input type="text" id="new_raw_id" class="form-control">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">AppId</label>
            <div class="col-sm-8">
                <input type="text" id="new_app_id" class="form-control">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">AppSecret</label>
            <div class="col-sm-8">
                <input type="text" id="new_app_secret" class="form-control">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">描述</label>
            <div class="col-sm-8">
                <textarea name="" class="form-control" id="new_remark"></textarea>
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    </@operate.add>
<script>
    $('#operate_add_modal').modal({backdrop: 'static', keyboard: false});
    $('#operate_add_modal').modal("hide");;
    function addAccount() {
        var name = $("#new_name").val();
        var token = $("#new_token").val();
        var number = $("#new_number").val();
        var rawId = $("#new_raw_id").val();
        var appId = $("#new_app_id").val();
        var appSecret = $("#new_app_secret").val();
        var aesKey = $("#new_aes_key").val();
        var remark = $("#new_remark").val();
        $.post(
                "/api/wechat/account/add",
                {
                    name: name,
                    token: token,
                    number: number,
                    rawId: rawId,
                    appId: appId,
                    appSecret: appSecret,
                    aesKey: aesKey,
                    remark: remark
                },
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#operate_add_modal").modal("hide");
                        window.location.reload();
                        if (data.code != 0) {
                            alertShow("warning", data.message, 3000);
                        }
                    }
                }
        );
    }
    function doDelete(id) {
        var pageIndex = ${page.pageIndex};
        warningModal("确定要删除该模板吗?", function () {
            $.post(
                    "/api/wechat/account/delete",
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
    function changeValue(e) {
        if ($(e).val().length!==43){
            alertShow('danger','请输入43个字符',2000)
            $(e).focus()
        }
    }
    $('#uploaderCover').uploader({
        autoUpload: true,// 当选择文件后立即自动进行上传操作
        lang: 'zh_cn',
        url: '/uploader/zui/upload',  // 文件上传提交地址
        filters: {
            mime_types:[{title: '文档', extensions: 'txt'}],
            max_file_size: '1k',
        },
        headers:{
            adddate:0
        },
        multi_selection:false,
        file_data_name: 'file',
        limitFilesCount:1,
        chunk_size: 0,
        deleteActionOnDone: function (file, doRemoveFile) {
//            alertShow('danger','上传失败!',2000)
            return true;
        },
        responseHandler: function (responseObject, file) {
            file.data = responseObject.response;
            if (file.remoteData.result == 'ok'){
                var localPath = file.remoteData.file
                $.post('/api/wk/oauth2/mvTxtFile',{
                    localPath: localPath
                },function (data) {
                    if (data.code<0){
                        alertShow('danger',data.message,2000)
                        return
                    }
                    alertShow('success','上传成功!',2000)
                })
            }
        }
    });
    function upload() {

    }

</script>


    <@operate.editview "编辑账号">
    <div class="modalForm" style="margin-top: 15px">
        <div class="form-group">
            <label class="col-sm-3  control-label">账号名称</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="edit_name">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">Token</label>
            <div class="col-sm-8">
                <input type="text" id="edit_token" class="form-control" placeholder="****">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">AppId</label>
            <div class="col-sm-8">
                <input readonly type="text" id="edit_app_id" class="form-control">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">AppSecret</label>
            <div class="col-sm-8">
                <input type="text" id="edit_app_secret" class="form-control" placeholder="****">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">AESKey</label>
            <div class="col-sm-8">
                <input type="text" id="edit_aes_key" class="form-control" placeholder="****">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">微信号</label>
            <div class="col-sm-8">
                <input type="text" id="edit_number" class="form-control">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">回调地址</label>
            <div class="col-sm-8">
                <input type="text" id="edit_url" class="form-control" readonly style="background-color: white">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">服务器IP</label>
            <div class="col-sm-8">
                <input type="text" id="edit_ip" class="form-control" readonly style="background-color: white">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">描述</label>
            <div class="col-sm-8">
                <textarea name="" class="form-control" id="edit_remark"></textarea>
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    </@operate.editview>
<script>
    function edit(id) {
        $.get(
                "/api/wechat/account/get?id=" + id,
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#edit_submit_button").attr("onclick", "saveEdit(" + id + ")");
                        $("#operate_edit_modal").modal("show");
                        $("#edit_name").val(data.result.name);
                        $("#edit_token").val("");
                        $("#edit_number").val(data.result.number);
                        $("#edit_app_id").val(data.result.appId);
                        $("#edit_app_secret").val("");
                        $("#edit_aes_key").val("");
                        $("#edit_remark").val(data.result.remark);
                        $("#edit_url").val(data.result.callback);
                        $("#edit_ip").val(data.result.webip);
                    }
                },
                "json"
        );
    }
    function saveEdit(id) {
        var name = $("#edit_name").val();
        var token = $("#edit_token").val();
        var number = $("#edit_number").val();
        var appId = $("#edit_app_id").val();
        var appSecret = $("#edit_app_secret").val();
        var aesKey = $("#edit_aes_key").val();
        var remark = $("#edit_remark").val();
        $.post(
                "/api/wechat/account/update",
                {
                    id: id,
                    name: name,
                    token: token,
                    number: number,
                    appId: appId,
                    appSecret: appSecret,
                    aesKey: aesKey,
                    remark: remark
                },
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#operate_edit_modal").modal("hide");
                        window.location.reload();
                        if (data.code != 0) {
                            alertShow("warning", data.message, 3000);
                        }
                    }
                },
                "json"
        );
    }

    function doRefreshMenu(id) {
        warningModal("从公众号同步将删除本地保存的所有菜单内容，是否继续?", function () {
            $.get(
                    "/api/wechat/account/menu/refresh?accountId=" + id,
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);
                        } else {
                            alertShow("info", "同步成功，请到菜单管理和条件菜单页面查看！", 3000);
                        }
                    }
            );
        });
    }
    function doSubmitMenu(id) {
        warningModal("这将立即修改微信公众号设置，是否继续?", function () {
            $.get(
                    "/api/wechat/account/menu/publish?accountId=" + id,
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);
                        } else {
                            alertShow("info", "发布成功，请取消关注并重新关注查看效果！", 3000);
                        }
                    }
            );
        });
    }
</script>
</@macroCommon.html>