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
            <li><a href="#"><i class="fa fa-dashboard"></i>内容管理</a></li>
            <li><a onclick="history.back(-1);">模版管理</a></li>
            <li>字段管理</li>
            <span class="pull-right">
                <button class="btn btn-primary btn-sm" onclick="history.back(-1);">返回</button>
            </span>
        </ol>
    </div>
    <div class="row content-bottom">
        <div class="col-lg-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="fa fa-comments-o"></i>
                    字段列表
                </div>
                <div class="panel-body">
                    <@shiro.hasPermission name="cms_template.add" >
                        <a href="#" class="btn btn-primary" id="fieldAddButton">
                            <i class="fa fa-fw fa-plus "></i>添加</a>
                    </@shiro.hasPermission>
                    <table id="" class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th style="width:10%">字段名称</th>
                            <th style="width:30%">字段描述</th>
                            <th style="width:10%">字段CODE</th>
                            <th style="width:10%">字段类型</th>
                            <th style="width:10%">参数</th>
                            <th style="width:10%" class="tableCenter">是否启用</th>
                            <th style="width:20%" class="tableCenter">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#if fields?exists>
                                <#list fields?if_exists as field>
                                <tr>
                                    <td>${field.name?if_exists}</td>
                                    <td>${field.hint?if_exists}</td>
                                    <td>${field.code?if_exists}</td>
                                    <td>
                                        <#list fieldType as typeVal>
                                            <#if (typeVal==field.type?if_exists)>${typeVal.getName()}</#if>
                                        </#list>
                                    </td>
                                    <td>${field.param?if_exists}</td>
                                    <td class="tableCenter">
                                        <#if field.enabled?if_exists>√</#if>
                                    </td>
                                    <input type="hidden" value="${field.enabled?default(0)?c}">
                                    <input type="hidden" value="${field.remark?if_exists}">
                                    <input type="hidden" value="${field.type.ordinal()?if_exists}">
                                    <input type="hidden" value="${field.placeholder?if_exists}">
                                    <td class="tableCenter">
                                        <@shiro.hasPermission name="cms_template.update" >
                                            <a href="#" onclick="doEdit(this, ${field.id?if_exists})" class="btn btn-primary ">
                                                <i class="fa fa-fw fa-cog"></i>修改</a>
                                            <a href="#" class="btn btn-danger " onclick="delField(${field.id?default(0)?c})">
                                                <i class="fa fa-fw fa-remove "></i>删除</a>
                                        </@shiro.hasPermission>
                                    </td>
                                </tr>
                                </#list>
                            </#if>
                        </tbody>
                    </table>
                </div>
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

<@operate.editview "编辑字段">
<div class="modalForm">
    <div class="form-group">
        <label class="col-sm-3  control-label">字段名称</label>
        <div class="col-sm-8">
            <input type="text" class="form-control" id="edit_name">
        </div>
    </div>
</div>
<div class="modalForm">
    <div class="form-group">
        <label class="col-sm-3  control-label">字段描述</label>
        <div class="col-sm-8">
            <input type="text" class="form-control" id="edit_hint">
        </div>
    </div>
</div>
<div class="modalForm">
    <div class="form-group">
        <label class="col-sm-3  control-label">字段类型</label>
        <div class="col-sm-8">
            <select class="form-control" style="width:50%" id="edit_type">
                <#list fieldType as typeVal>
                    <option value="${typeVal.getCode()}">${typeVal.getName()}</option>
                </#list>
            </select>
        </div>
    </div>
</div>
<div class="modalForm">
    <div class="form-group">
        <label class="col-sm-3  control-label">字段CODE</label>
        <div class="col-sm-8">
            <input type="text" class="form-control" id="edit_code" style="display: block">
            <select class="form-control" style="width:50%;display: none" id="edit_property">
                <#list properties as op>
                    <option value="${op.code}">${op.name}</option>
                </#list>
            </select>
        </div>
    </div>
</div>
<div class="modalForm">
    <div class="form-group">
        <label class="col-sm-3  control-label">启用</label>
        <div class="col-sm-8" style="padding-top:5px;">
            &nbsp;&nbsp;<input type="radio" name="edit_enabled" id="edit_enabled" checked>是&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="radio" name="edit_enabled" id="edit_disabled">否
        </div>
    </div>
</div>
<div class="modalForm" id="edit_paramDiv" style="display: none">
    <div class="form-group">
        <label class="col-sm-3  control-label">参数</label>
        <div class="col-sm-8">
            <input type="text" class="form-control" id="edit_param">
            <div id="edit_param_hint" style="color: red;">每个参数间请使用英文","区分。</div>
        </div>
    </div>
</div>
<div class="modalForm" id="edit_imageDiv" style="display: none">
    <div class="form-group">
        <label class="col-sm-3  control-label">默认图片</label>
        <div class="col-sm-8">
            <input type="hidden" id="edit_image_value">
            <div id="edit_image" style="margin-bottom: 5px;"></div>
            <div id="edit_uploader" class="uploader" data-ride="uploader">
                <div class="uploader-files file-list file-list-grid" style="display: none;"></div>
                <button type="button" class="btn btn-primary uploader-btn-browse" style="margin-top:10px;">
                    <i class="icon icon-plus"></i> 选择文件
                </button>
            </div>
        </div>
    </div>
</div>
</@operate.editview>
    <@operate.editjs "saveEdit">
    var enabled = $(elem).parent().siblings().eq(6).val();
    if (enabled == "true")
        $("#edit_enabled").prop("checked", true);
    else
        $("#edit_disabled").prop("checked", true);
    $("#edit_name").val($(elem).parent().siblings().eq(0).html());
    $("#edit_hint").val($(elem).parent().siblings().eq(1).html());
    $("#edit_type").val($(elem).parent().siblings().eq(8).val());
    $("#edit_code").val($(elem).parent().siblings().eq(2).html());
    $("#edit_property").val($(elem).parent().siblings().eq(2).html());
    $("#edit_param").val($(elem).parent().siblings().eq(4).html());
    if (5 == $("#edit_type").val()) {
        $("#edit_property").css("display", "block")
        $("#edit_code").css("display", "none")
        $("#edit_paramDiv").css("display", "none")
    } else if (1 == $("#edit_type").val()) {
        $("#edit_paramDiv").css("display", "block")
        $("#edit_param_hint").text("输入所有单选项，用半角逗号(,)分割。");
        $("#edit_property").css("display", "none")
        $("#edit_code").css("display", "block")
    } else if (9 == $("#edit_type").val()) {
        $("#edit_paramDiv").css("display", "block")
        $("#edit_param_hint").text("输入所有支持的扩展名，用半角分号(;)分割。");
        $("#edit_property").css("display", "none")
        $("#edit_code").css("display", "block")
    } else {
        $("#edit_property").css("display", "none")
        $("#edit_code").css("display", "block")
        $("#edit_paramDiv").css("display", "none")
    }
    if (2 == $("#edit_type").val()) {
        $("#edit_imageDiv").css("display", "block")
    } else {
        $("#edit_imageDiv").css("display", "none")
    }
    var avatar =$(elem).parent().siblings().eq(9).val();
    $("#edit_image").empty();
    if (avatar != "")$("#edit_image").append("<img style='max-width:100px' src='"+avatar+"'/>");
    $("#edit_image_value").val(avatar);
    </@operate.editjs>
<script>
    $('#edit_uploader').uploader({
        multi_selection: false,
        file_data_name: 'file',
        mime_types: [
            {title: '图片', extensions: 'jpg,gif,png'}
        ],
        autoUpload: true,            // 当选择文件后立即自动进行上传操作
        url: '${uploadPath?if_exists}',     // 文件上传提交地址
        previewImageSize: {width: 80, height: 100},
        deleteActionOnDone: function (file, doRemoveFile) {
            return true;
        }
    }).on('onFileUploaded', function(event, file, result) {
        var uploader = $('#edit_uploader').data('zui.uploader');
        var plupload = uploader.plupload;
        var files = plupload.files;
        for (var i = 0; i < files.length; i++) {
            if (files[i].id != file.id) uploader.removeFile(files[i]);
        }
        $("#edit_image").empty();
        $("#edit_image").append("<img style='max-width:100px' src='"+file.url+"'/>");
        $("#edit_image_value").val(file.url);
    });

    function saveEdit(id) {
        var name = $("#edit_name").val();
        var hint = $("#edit_hint").val();
        var type = $("#edit_type").val();
        var placeholder = $("#edit_image_value").val();
        var code;
        if (5 == type) {
            code = $("#edit_property").val();
        } else {
            code = $("#edit_code").val();
        }
        var param = $("#edit_param").val();
        var enabled = $("#edit_enabled").prop("checked");
        var remark = $("#edit_remark").val();
        var enabled = $("#edit_enabled").prop("checked");
        var templateId = ${templateId?c};
        $.post(
                "/api/cms/template/field/update",
                {id: id, code: code, name: name, hint: hint, type: type, param: param, remark: remark, placeholder: placeholder, enabled: enabled},
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        window.location.href = "/cms/template/field?templateId=" + templateId;
                        $("#operate_edit_modal").modal("hide");
                    }
                },
                "json"
        );
    }
</script>

<@operate.add "新增字段" "#fieldAddButton" "addField">
    <div class="modalForm" style="margin-top: 15px">
        <div class="form-group">
            <label class="col-sm-3  control-label">字段名称</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="field_name">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">字段描述</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="field_hint">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">字段类型</label>
            <div class="col-sm-8">
                <select class="form-control" style="width:50%" id="field_type">
                    <#list fieldType as typeVal>
                        <option value="${typeVal.getCode()}">${typeVal.getName()}</option>
                    </#list>
                </select>
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">字段CODE</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="field_code" style="display: block">
                <select class="form-control" style="width:50%;display: none" id="field_property">
                    <#list properties as op>
                        <option value="${op.code}">${op.name}</option>
                    </#list>
                </select>
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">启用</label>
            <div class="col-sm-8" style="padding-top:5px;">
                &nbsp;&nbsp;<input type="radio" name="field_enabled" id="field_enabled" checked>是&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="radio" name="field_enabled" id="field_disabled">否
            </div>
        </div>
    </div>
    <div class="modalForm" id="paramDiv" style="display: none">
        <div class="form-group">
            <label class="col-sm-3  control-label">参数</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="field_param">
                <div id="field_param_hint" style="color: red;">每个参数间请使用英文","区分。</div>
            </div>
        </div>
    </div>
    <div class="modalForm" id="field_imageDiv" style="display: none">
        <div class="form-group">
            <label class="col-sm-3  control-label">默认图片</label>
            <div class="col-sm-8">
                <input type="hidden" id="field_image_value">
                <div id="field_image" style="margin-bottom: 5px;"></div>
                <div id="field_uploader" class="uploader" data-ride="uploader">
                    <div class="uploader-files file-list file-list-grid" style="display: none;"></div>
                    <button type="button" class="btn btn-primary uploader-btn-browse" style="margin-top:10px;">
                        <i class="icon icon-plus"></i> 选择文件
                    </button>
                </div>
            </div>
        </div>
    </div>
</@operate.add>
<script>
    $('#field_uploader').uploader({
        multi_selection: false,
        file_data_name: 'file',
        mime_types: [
            {title: '图片', extensions: 'jpg,gif,png'}
        ],
        autoUpload: true,            // 当选择文件后立即自动进行上传操作
        url: '${uploadPath?if_exists}',     // 文件上传提交地址
        previewImageSize: {width: 80, height: 100},
        deleteActionOnDone: function (file, doRemoveFile) {
            return true;
        }
    }).on('onFileUploaded', function(event, file, result) {
        var uploader = $('#field_uploader').data('zui.uploader');
        var plupload = uploader.plupload;
        var files = plupload.files;
        for (var i = 0; i < files.length; i++) {
            if (files[i].id != file.id) uploader.removeFile(files[i]);
        }
        $("#field_image").empty();
        $("#field_image").append("<img style='max-width:100px' src='"+file.url+"'/>");
        $("#field_image_value").val(file.url);
    });

    $(function () {
        if (5 == $("#field_type").val()) {
            $("#field_property").css("display", "block")
            $("#field_code").css("display", "none")
        } else if (1 == $("#field_type").val()) {
            $("#paramDiv").css("visibility", "visible")
            $("#field_param_hint").text("输入所有单选项，用半角逗号(,)分割。");
        } else if (9 == $("#field_type").val()) {
            $("#paramDiv").css("visibility", "visible")
            $("#field_param_hint").text("输入所有支持的扩展名，用半角分号(;)分割。");
        }
        $("#field_type").change(function (data) {
            if (5 == $("#field_type").val()) {
                $("#field_property").css("display", "block")
                $("#field_code").css("display", "none")
                $("#paramDiv").css("display", "none")
            } else if (1 == $("#field_type").val()) {
                $("#paramDiv").css("display", "block")
                $("#field_param_hint").text("输入所有单选项，用半角逗号(,)分割。");
                $("#field_property").css("display", "none")
                $("#field_code").css("display", "block")
            } else if (9 == $("#field_type").val()) {
                $("#paramDiv").css("display", "block")
                $("#field_param_hint").text("输入所有支持的扩展名，用半角分号(;)分割。");
                $("#field_property").css("display", "none")
                $("#field_code").css("display", "block")
            } else {
                $("#field_property").css("display", "none")
                $("#field_code").css("display", "block")
                $("#paramDiv").css("display", "none")
            }
            if (2 == $("#field_type").val()) {
                $("#field_imageDiv").css("display", "block")
            } else {
                $("#field_imageDiv").css("display", "none")
            }
        });

        $("#edit_type").change(function (data) {
            if (5 == $("#edit_type").val()) {
                $("#edit_property").css("display", "block")
                $("#edit_code").css("display", "none")
                $("#edit_paramDiv").css("display", "hidden")
            } else if (1 == $("#edit_type").val()) {
                $("#edit_paramDiv").css("display", "block")
                $("#edit_param_hint").text("输入所有单选项，用半角逗号(,)分割。");
                $("#edit_property").css("display", "none")
                $("#edit_code").css("display", "block")
            } else if (9 == $("#edit_type").val()) {
                $("#edit_paramDiv").css("display", "block")
                $("#edit_param_hint").text("输入所有支持的扩展名，用半角分号(;)分割。");
                $("#edit_property").css("display", "none")
                $("#edit_code").css("display", "block")
            } else {
                $("#edit_property").css("display", "none")
                $("#edit_code").css("display", "block")
                $("#edit_paramDiv").css("display", "none")
            }
            if (2 == $("#edit_type").val()) {
                $("#edit_imageDiv").css("display", "block")
            } else {
                $("#edit_imageDiv").css("display", "none")
            }
        })
    });
    function addField() {
        var name = $("#field_name").val();
        var hint = $("#field_hint").val();
        var type = $("#field_type").val();
        var placeholder = $("#field_image_value").val();
        var code;
        if (5 == type) {
            code = $("#field_property").val();
        } else {
            code = $("#field_code").val();
        }
        var param = $("#field_param").val();
        var enabled = $("#field_enabled").prop("checked");
        var remark = $("#field_remark").val();
        var enabled = $("#field_enabled").prop("checked");
        var templateId = ${templateId?c};
        $.post(
                "/api/cms/template/field/add",
                {templateId: templateId, code: code, name: name, hint: hint, type: type, param: param, remark: remark, placeholder: placeholder, enabled: enabled},
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#deptAddModal").modal("hide");
                        window.location.href = "/cms/template/field?templateId=" + templateId;
                    }
                }
        );
    }
    function delField(id) {
        var templateId = ${templateId?c};
        warningModal("确定要删除该字段吗?", function () {
            $.post(
                    "/api/cms/template/field/delete",
                    {id: id},
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);;
                        } else {
                            window.location.href = "/cms/template/field?templateId=" + templateId;
                        }
                    }
            );
        });
    }
</script>

</@macroCommon.html>

