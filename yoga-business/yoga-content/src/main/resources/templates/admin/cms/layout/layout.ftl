<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<@macroCommon.html>
<style>
    .field {
        margin-left: 15px;
    }
</style>
<script src="<@macroCommon.resource/>/fineUpload/fine-uploader.js" type="text/javascript"></script>
<link href="<@macroCommon.resource/>/fineUpload/fine-uploader-new.min.css" rel="stylesheet" type="text/css">
<link href="<@macroCommon.resource/>/uploader/zui.uploader.css" rel="stylesheet">
<style>
    #trigger-upload {
        color: white;
        background-color: #00ABC7;
        font-size: 14px;
        padding: 7px 20px;
        background-image: none;
    }

    .fine-uploader-trigger .qq-upload-button {
        margin-right: 15px;
    }

    ~
    .fine-uploader-trigger .qq-uploader .qq-total-progress-bar-container {
        width: 60%;
    }
</style>
<script type="text/template" id="qq-template-manual-trigger">
    <div class="qq-uploader-selector" qq-drop-area-text="" >
        <div class="qq-upload-drop-area-selector qq-upload-drop-area" qq-hide-dropzone>
            <span class="qq-upload-drop-area-text-selector"></span>
        </div>
        <div class="buttons">
            <div class="qq-upload-button-selector qq-upload-button">
                <div>选择文件</div>
            </div>
            <button type="button" id="trigger-upload" class="btn btn-primary">
                <i class="icon-upload icon-white"></i> 上传
            </button>
        </div>
        <span class="qq-drop-processing-selector qq-drop-processing">
        <span class="qq-drop-processing-spinner-selector qq-drop-processing-spinner"></span>
        </span>
        <ul class="qq-upload-list-selector qq-upload-list" aria-live="polite" aria-relevant="additions removals">
            <li>
                <div class="qq-progress-bar-container-selector">
                    <div role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"
                         class="qq-progress-bar-selector qq-progress-bar"></div>
                </div>
                <span class="qq-upload-spinner-selector qq-upload-spinner"></span>
                <img class="qq-thumbnail-selector" qq-max-size="100" qq-server-scale>
                <span class="qq-upload-file-selector qq-upload-file"></span>
                <span class="qq-edit-filename-icon-selector qq-edit-filename-icon" aria-label="Edit filename"></span>
                <input class="qq-edit-filename-selector qq-edit-filename" id="changeImage" tabindex="0" type="text">
                <span class="qq-upload-size-selector qq-upload-size"></span>
                <button type="button" class="qq-btn qq-upload-cancel-selector qq-upload-cancel">取消</button>
                <button type="button" class="qq-btn qq-upload-retry-selector qq-upload-retry">重试</button>
                <span role="status" class="qq-upload-status-text-selector qq-upload-status-text"></span>
            </li>
        </ul>
    </div>
</script>
<body>
<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>内容管理</a></li>
            <li><a onclick="history.back(-2);">模版管理</a></li>
            <li><a onclick="history.back(-2);">布局管理</a></li>
            <li>布局编辑</li>
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
                    布局编辑
                </div>
                <div class="panel-body">
                    <div class="tableToolContainer" style="margin-bottom:15px">
                        <div class="col-lg-12">
                            <form id="edit_form" class="form-horizontal">
                                <input type="hidden" name="templateId" value="${param.templateId?c}">
                                <input type="hidden" name="id"  value="${(layout.id)!0}">
                                <div class="form-group">
                                    <label for="">样式标题</label>
                                    <input type="text" class="form-control" name="title" value="${(layout.title)!''}">
                                </div>
                                <div class="form-group">
                                    <label for="">布局类型</label>
                                    <select class="form-control" style="min-width: 100px" name="type" id="layout_type">
                                        <option value="LIST" selected>列表布局</option>
                                        <option value="DETAIL">详情布局</option>
                                    </select>
                                    <#if layout??>
                                        <script>
                                            $("#layout_type").val('${layout.type?if_exists}');
                                        </script>
                                    </#if>
                                </div>
                                <div class="form-group">
                                    <label for="">效果图片（可选）</label>
                                    <div name="edit_uploader_files" id="edit_uploader_files" class="wu-example">
                                        <div id="edit-fine-uploader-trigger" class="fine-uploader-trigger"></div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="">所需字段（为空时，列表页只返回title，详情页将返回所有字段）</label>
                                    <div>
                                        <#list allFields?if_exists as field>
                                            <label class="field">
                                                <input name="fields" type="checkbox" value="${field.code?if_exists}"
                                                       <#if fields?if_exists?seq_contains(field.code)>checked="checked"</#if>>
                                            ${field.name?if_exists}
                                            </label>
                                        </#list>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="">关联文件（可以在这里上传外联CSS、JS等，然后直接在下方内容中引用显示的URL）</label>
                                    <div id="myUploader" class="uploader">
                                        <div class="file-list" data-drag-placeholder="请拖拽文件到此处">
                                            <#if layout?? && layout.linkFiles?? && layout.linkFiles?length gt 0>
                                            <#list layout.getLinkFileList() as link>
                                                <div class="file" data-status="done">
                                                    <div class="file-wrapper">
                                                        <div class="file-icon"><i class="icon icon-file-o"></i></div>
                                                        <div class="content">
                                                            <div class="file-name"><a href="${link}" target="_blank">${link}</a></div>
                                                            <input name="accessories" type="hidden" value="${link}">
                                                        </div>
                                                    </div>
                                                </div>
                                            </#list>
                                            </#if>
                                        </div>
                                        <div class="uploader-actions">
                                            <div class="uploader-status pull-right text-muted"></div>
                                            <button type="button" class="btn btn-link uploader-btn-browse">
                                                <i class="icon icon-plus"></i> 选择文件
                                            </button>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="">样式内容（直接粘贴HTML，含内嵌CSS、JS，可使用系统自带外联CSS、JS，也可引用其他服务器上的CSS、JS）</label>
                                    <ul>
                                        <li>列表布局时：使用<b style="color: red;">${baseUrl}/api/cms/article/list?tid=#[[tenantId]]&columnId=#[[columnId]]&pageIndex=0&pageSize=10</b>请求文章列表，渲染时服务器会自动将#[[columnId]]替换为栏目ID</li>
                                        <li>列表布局中需要跳转到详情页面时，使用<b style="color: red;">${baseUrl}/cms/layout/show?tid=#[[tenantId]]&articleId=xxx&detailLayoutId=#[[detailLayoutId]]</b>作为href，渲染时服务器会自动将#[[detailLayoutId]]替换为详情页布局ID</li>
                                        <li>文章布局时：使用<b style="color: red;">${baseUrl}/api/cms/article/detail?tid=#[[tenantId]]&articleId=#[[articleId]]</b>请求文章详情，渲染时服务器会自动将#[[articleId]]替换为文章ID</li>
                                    </ul>
                                    <textarea name="html"
                                              style="width: 100%; min-height: 400px">${(layout.html)!''}</textarea>
                                </div>
                                <input type="hidden" name="files">
                                <div style="float: right">
                                    <button type="button" class="btn btn-default" onclick="doSubmit()">保存</button>
                                    <button type="button" class="btn btn-default" onclick="history.back();">关闭</button>
                                    <button type="button" class="btn btn-default" onclick="doPreview()">预览</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<footer class="main-footer">
    <@common.footer />
</footer>
<script src="<@macroCommon.resource/>/uploader/zui.js"></script>
<script src="<@macroCommon.resource/>/uploader/zui.uploader.min.js"></script>
</body>

<script>
    $(document).ready(function () {
        $('#myUploader').uploader({
            autoUpload: true,            // 当选择文件后立即自动进行上传操作
            url: '${zuiUploadUrl?if_exists}',     // 文件上传提交地址
            lang: 'zh_cn',
            multi_selection: true,
//            file_data_name: 'file',
            deleteActionOnDone: function (file, doRemoveFile) {
                $.post(
                        "/uploader/zui/delete",
                        {
                            fileId: file.remoteData.fileId,
                        },
                        function (data) {
                            if (data.code < 0) {
                                alertShow("danger", data.message, 3000);
                            } else {
                                doRemoveFile();
                            }
                        },
                        "json"
                );
                return false;
            },
            responseHandler: function (responseObject, file) {
                file.data = responseObject.response;
            },
            fileFormater: function($file, file, status) {
                if (file.remoteData != null) {
                    $file.find('.file-name').html("<a href=\"" + file.remoteData.url + "\" target=\"_blank\">" + file.remoteData.url + "</a>");
                    $file.find('.file-size').text(file.size);
                    $file.find('input[name="accessories"]').remove();
                    $file.append("<input name='accessories' type='hidden' value='" + file.remoteData.url + "'>")
                } else {
                    $file.find('.file-name').text(file.name);
                    $file.find('.file-size').text(file.size);
                }
                $file.find('.btn-delete-file').remove();
            }
        });
    });

    var editManualUploader = new qq.FineUploader({
        element: document.getElementById('edit-fine-uploader-trigger'),
        template: 'qq-template-manual-trigger',
        request: {
            endpoint: '${fineUploadurl?if_exists}'
        },
        multiple: true,
        thumbnails: {
            placeholders: {
                waitingPath: '<@macroCommon.resource/>/fineUpload/waiting-generic.png',
                notAvailablePath: '<@macroCommon.resource/>/fineUpload/not_available-generic.png'
            }
        },
        autoUpload: true,
        debug: true,
        callbacks: {
            onComplete: function (id, name, response) {
                if (response.success) {
                    var url = response.tempLink + "?n=" + name;
                    $("div[name='edit_uploader_files']").append("<input type='hidden' name='image' value=" + url + " >");
                }
            }
        },
        deleteFile: {
            enabled: false,
            endpoint: '#',
            deletingFailedText: '删除失败！'
        }
    });
    $("#edit_uploader_files").find("#trigger-upload").attr("onclick", "editManualUploader.uploadStoredFiles();");

    function doSubmit() {
        var body = $("#edit_form").serialize();
        $.post(
                "/api/cms/layout/<#if param.id??>update<#else>add</#if>",
                body,
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        window.location.href = "/cms/layout?templateId=${param.templateId?c}"
                    }
                },
                "json"
        );
    }
    function doPreview() {
    }

</script>

</@macroCommon.html>