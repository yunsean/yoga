<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<@macroCommon.html>
<link href="<@macroCommon.resource/>/uploader/zui.uploader.css" rel="stylesheet">
<script src="<@macroCommon.resource/>/uploader/zui.js"></script>
<script src="<@macroCommon.resource/>/uploader/zui.uploader.min.js"></script>
<body>
<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>公众号素材管理</a></li>
            <li><#if param.type??>${param.type.getDesc()}</#if>素材</li>
        </ol>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="icon icon-user"></i><#if param.type??>${param.type.getDesc()}</#if>素材
                </div>
                <div class="panel-body">
                    <div class="tableToolContainer" style="margin-bottom:15px">
                        <form action="/wechat/material" method="POST" class="form-inline" id="filter_form">
                            <input type="hidden" name="type" value="${param.type?if_exists}">
                            <div class="form-group">
                                <label style="margin-right: 20px">微信账号</label>
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
                                        <label class="exampleInputAccount4">名称</label>
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
                                        <label class="exampleInputAccount4">分组</label>
                                        &nbsp;&nbsp;
                                        <select id="filter_groupId" class="form-control" name="groupId"
                                                style="min-width: 120px">
                                            <option value="">全部</option>
                                            <option value="0">未分组</option>
                                            <#list groups?if_exists as group>
                                                <option value="${group.id?if_exists?c}">${group.name?if_exists}</option>
                                            </#list>
                                        </select>
                                    </div>
                                </div>
                                <script>
                                        <#if param.groupId??>
                                        $("#filter_groupId").val('${param.groupId?default(0)?c}');
                                        </#if>
                                </script>
                            </div>
                            &nbsp;&nbsp;
                            <div class="form-group">
                                <button type="submit" class="btn btn-success"><i
                                        class="fa fa-fw fa-search"></i>搜索
                                </button>
                            </div>
                            <@shiro.hasPermission name="wx_material.update" >
                                <div class="form-group" style="float: right">
                                    <a type="input" onclick="doAdd()" class="btn btn-primary" style="margin-left: 10px">
                                        <i class="fa fa-fw fa-upload"></i>上传素材
                                    </a>
                                    &nbsp;&nbsp;
                                    <a type="input" onclick="batchAddGroup()" class="btn btn-primary"
                                       style="margin-left: 10px">
                                        <i class="fa fa-fw fa-briefcase"></i>批量分组
                                    </a>
                                    &nbsp;&nbsp;
                                    <a type="input" onclick="doRefresh()" class="btn btn-info"
                                       style="margin-left: 10px">
                                        <i class="fa fa-fw fa-download"></i>从公众号刷新
                                    </a>
                                </div>
                            </@shiro.hasPermission>
                        </form>
                    </div>
                    <table class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th style="width:10px"><input type="checkbox" id="all_checked" onchange="checkAll(this);">
                            </th>
                            <#if param.type='video'>
                                <th style="width:10%">名称</th>
                                <th style="width:15%">Title</th>
                                <th style="width:25%">Intro</th>
                                <th style="width:15%">所属分组</th>
                            <#else>
                                <th style="width:25%">素材编码</th>
                                <th style="width:25%">名称</th>
                                <th style="width:15%">所属分组</th>
                            </#if>
                            <th style="width:10%" class="tableCenter">更新时间</th>
                            <th class="tableCenter">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list medias?if_exists as media>
                            <tr>
                                <td><input type="checkbox" name="material_id_checkbox" value="${media.mediaId?if_exists}">
                                </td>
                                <#if param.type='video'>
                                    <td>${media.name?if_exists}</td>
                                    <td>${media.title?if_exists}</td>
                                    <td>${media.intro?if_exists}</td>
                                    <td>${media.groupName?default('未分组')}</td>
                                <#else>
                                    <td>${media.mediaId?if_exists}</td>
                                    <td>${media.name?if_exists}</td>
                                    <td>${media.groupName?default('未分组')}</td>
                                </#if>
                                <td class="tableCenter">${media.updateTime?datetime?if_exists}</td>
                                <td class="tableCenter">
                                    <a onclick="doPreview(this, '${media.url?if_exists}')"
                                       style="margin-right: 20px"
                                       class="btn btn-sm btn-primary">
                                        <i class="fa fa-fw fa-play"></i>预览</a>
                                    <@shiro.hasPermission name="wx_material.update" >
                                        <a onclick="addGroup(this, '${media.mediaId?if_exists}')" style="margin-right: 10px"
                                           class="btn btn-sm btn-primary">
                                            <i class="fa fa-fw fa-briefcase"></i>分组</a>
                                        <a href="javascript:void(0)" style="margin-right: 10px"
                                           onclick="doDelete(${media.id?if_exists?c})"
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
                    <@html.paging page=page param=param action="/wechat/material"/>
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
        $("input[name='material_id_checkbox']").prop("checked", $("#all_checked").prop('checked'));
    }
</script>
<div class="modal fade" id="group_set_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" style="width:50%">
        <form class="form-horizontal" id="group_set_modal_form">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title">
                        设置分组
                    </h4>
                </div>
                <div class="modal-body" id="group_set_modal_body">
                    <#list groups as group>
                        <input type="radio" name="add_group_item" value="${group.id?if_exists?c}"
                               style="min-width: 200px; margin-left: 20px; margin-right: 10px">${group.name?if_exists}
                    </#list>
                </div>
                <div class="modal-footer">
                    <a href="javascript:void(0)" class="btn btn-danger" data-dismiss="modal"><i
                            class="fa fa-fw fa-remove"></i></button>关闭</a>
                    <a href="javascript:void(0)" id="group_set_modal_submit" class="btn btn-info"><i
                            class="fa fa-fw  fa-save"></i>保存</a>
                </div>
            </div>
        </form>
    </div>
</div>
<script>
    function addGroup(elem, mediaId) {
        $("#group_set_modal_submit").attr("onclick", "doAddGroup('" + mediaId + "')");
        $("#group_set_modal").modal("show");
        $("#group_set_modal_form")[0].reset();
    }
    function doAddGroup(mediaId) {
        var groupId = $("input[name='add_group_item']:checked").val();
        if (groupId == null) return;
        $.post("/api/wechat/material/group/set",
                {
                    groupId: groupId,
                    mediaIds: mediaId
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
    function batchAddGroup() {
        if ($("[name='material_id_checkbox']:checked").length < 1) {
            alertShow("warning", "请选择素材！", 1000);
            return;
        }
        $("#group_set_modal_submit").attr("onclick", "doBatchAddGroup()");
        $("#group_set_modal").modal("show");
        $("#group_set_modal_form")[0].reset();
    }
    function doBatchAddGroup() {
        var groupId = $("input[name='add_group_item']:checked").val();
        if (groupId == null) return;
        var ids = "";
        $("[name='material_id_checkbox']:checked").each(function () {
            ids += "," + $(this).val();
        });
        if (ids.length > 0) ids = ids.substr(1);
        $.post("/api/wechat/material/group/set",
                {
                    groupId: groupId,
                    mediaIds: ids
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

<div class="modal fade" id="material_upload_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" style="width:50%">
        <form class="form-horizontal" id="material_upload_modal_form">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title">
                        上传素材
                    </h4>
                </div>
                <div class="modal-body" id="tags_list_model_body">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">格式限制</label>
                        <#if param.type == 'image'>
                            <div class="col-sm-8 control-label"
                                 style="text-align: left">小于2MB，bmp/png/jpeg/jpg/gif格式的文件
                            </div>
                        <#elseif param.type == 'voice'>
                            <div class="col-sm-8 control-label"
                                 style="text-align: left">小于2MB，播放长度不超过60s，mp3/wma/wav/amr格式的文件
                            </div>
                        <#elseif param.type == 'video'>
                            <div class="col-sm-8 control-label" style="text-align: left">小于10MB，mp4格式的文件</div>
                        </#if>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3  control-label">分组</label>
                        <div class="col-md-8">
                            <select id="add_groupId" class="form-control" name="groupId"
                                    style="min-width: 120px">
                                <option value="0" selected>不指定</option>
                                <#list groups?if_exists as group>
                                    <option value="${group.id?if_exists?c}">${group.name?if_exists}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3  control-label">素材名称</label>
                        <div class="col-md-8">
                            <input type="text" class="form-control" id="add_name">
                        </div>
                    </div>
                    <#if param.type == 'video'>
                        <div class="form-group">
                            <label class="col-sm-3  control-label">视频标题</label>
                            <div class="col-md-8">
                                <input type="text" class="form-control" id="add_title">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3  control-label">视频简介</label>
                            <div class="col-md-8">
                                <textarea type="text" class="form-control" id="add_intro"></textarea>
                            </div>
                        </div>
                    </#if>
                    <div class="form-group">
                        <label for="myUploader" class="col-md-3">上传文件</label>
                        <div class="col-md-8">
                            <div id="myUploader" class="uploader">
                                <div class="file-list" style="height: 200px; overflow:auto;"
                                     data-drag-placeholder="请拖拽文件到此处"></div>
                                <div class="uploader-actions">
                                    <div class="uploader-status pull-right text-muted"></div>
                                    <button type="button" class="btn btn-link uploader-btn-browse"><i
                                            class="icon icon-plus"></i> 选择文件
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <a href="javascript:void(0)" class="btn btn-danger" data-dismiss="modal"><i
                            class="fa fa-fw fa-remove"></i></button>关闭</a>
                    <a href="javascript:void(0)" id="group_set_modal_submit" class="btn btn-info"
                       onclick="addMaterial();"><i
                            class="fa fa-fw  fa-save"></i>保存</a>
                </div>
            </div>
        </form>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $('#myUploader').uploader({
            autoUpload: true,            // 当选择文件后立即自动进行上传操作
            url: '${uploadPath?if_exists}',     // 文件上传提交地址
            lang: 'zh_cn',
            filters: {
            mime_types:
                <#if param.type == 'image'>
                        [{title: '图片文件', extensions: 'bmp,png,jpeg,jpg,gif'}],
                    max_file_size: '2mb',
                <#elseif param.type == 'voice'>
                    [{title: '音频文件', extensions: 'mp3,wma,wav,amr'}],
                    max_file_size: '2mb',
                <#elseif param.type == 'video'>
                    [{title: '视频文件', extensions: 'mp4'}],
                    max_file_size: '10mb',
                </#if>
                prevent_duplicates: true
            },
            multi_selection: true,
            file_data_name: 'file',
            chunk_size: 0,
            deleteActionOnDone: function (file, doRemoveFile) {
                return true;
            },
            responseHandler: function (responseObject, file) {
                file.data = responseObject.response;
            }
        });
    });
    function doAdd() {
        $("#material_upload_modal").modal("show");
    }
    function addMaterial() {
        var uploader = $('#myUploader').data('zui.uploader');
        var plupload = uploader.plupload;
        var files = plupload.files;
        var title = "";
        var intro = "";
        <#if param.type == 'video'>
            title = $("#add_title").val();
            intro = $("#add_intro").val();
        </#if>
        var medias = [];
        if (files.length < 1) {
            alertShow("warning", "请选择文件上传！", 3000);
            return;
        }
        for (var i = 0; i < files.length; i++) {
            if (files[i].status != 5) {
                alertShow("warning", "部分文件尚未完成上传或者上传失败！", 3000);
                return;
            }
            medias.push(JSON.parse(files[i].data));
        }
        var groupId = $("#add_groupId").val();
        var name = $("#add_name").val();
        var json = {};
        json["accountId"] = ${param.accountId?if_exists?c};
        json["type"] = '${param.type?if_exists}';
        json["groupId"] = groupId;
        json["files"] = medias;
        json["name"] = name;
        json["title"] = title;
        json["introduction"] = intro;
        $.ajax({
            url: '/api/wechat/material/add',
            type: 'post',
            traditional: true,
            data: JSON.stringify(json),
            dataType: 'json',
            contentType: 'application/json',
            success: function (data) {
                if (data.code < 0) {
                    alertShow("warning", data.message, 3000);
                    return;
                } else {
                    window.location.reload();
                }
            },
            error: function (data) {
                alertShow("warning", data.message, 3000);
            }
        });
    }
</script>

<div class="modal fade" id="material_preview_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true" style="height: 80%; width: 80%">
    <div class="modal-dialog" style="width:50%">
        <form class="form-horizontal" id="material_preview_modal_form">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title">
                        素材预览
                    </h4>
                </div>
                <div class="modal-body" id="material_preview_modal_body">
                    <iframe id="preivew_frame" src="javascript:void(0)" width="100%" height="100%"
                            frameborder="0"></iframe>
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
    function doPreview(elem, url) {
        if (url.length > 0) {
            $("#material_preview_modal").modal("show");
            $("#preivew_frame").attr("src", url);
        }
    }
    function doDelete(id) {
        warningModal("确定要删除该素材吗?", function () {
            $.post(
                    "/api/wechat/material/delete",
                    {
                        accountId: ${param.accountId?c},
                        id: id
                    },
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);
                        } else {
                            window.location.reload();
                        }
                    }
            );
        });
    }
    function doRefresh() {
        $.get(
                "/api/wechat/material/refresh?accountId=${param.accountId?if_exists?c}&type=${param.type?if_exists}",
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