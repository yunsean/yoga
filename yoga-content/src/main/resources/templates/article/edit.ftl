<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<!DOCTYPE html>
<html>
<#macro m1_column column level index>
<option value="${column.value?if_exists}">
    <#list 0..level as x>
        <#if x < level>
            │&nbsp;&nbsp;
        <#else>
            ├
        </#if>
    </#list>
${column.name?if_exists}
</option>
</#macro>
<#macro m1_columns columns level index>
    <@m1_column columns level index/>
    <#local level1 = level + 1/>
    <#if columns.children??>
        <#list columns.children as sub>
            <@m1_columns sub level1 sub_index/>
        </#list>
    </#if>
</#macro>
<@macroCommon.html/>
<script src="http://webapi.amap.com/maps?v=1.3&key=61dfd7a5aa538df0de297570f9221920&plugin=AMap.Autocomplete,AMap.PlaceSearch"
        type="text/javascript"></script>
<script src="/map/location.js" type="text/javascript"></script>
<script src="<@macroCommon.resource/>/fineUpload/fine-uploader.js" type="text/javascript"></script>
<link href="<@macroCommon.resource/>/fineUpload/fine-uploader-new.min.css" rel="stylesheet" type="text/css">
<link href="<@macroCommon.resource/>/uploader/zui.uploader.css" rel="stylesheet">
<script src="<@macroCommon.resource/>/uploader/zui.js"></script>
<script src="<@macroCommon.resource/>/uploader/zui.uploader.min.js"></script>
<script type="text/javascript" charset="utf-8" src="<@macroCommon.resource/>/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="<@macroCommon.resource/>/ueditor/ueditor.all.min.js"></script>
<script type="text/javascript" charset="utf-8" src="<@macroCommon.resource/>/ueditor/lang/zh-cn/zh-cn.js"></script>
<link href="<@macroCommon.resource/>/plugins/datetimepicker/jquery.datetimepicker.css" rel="stylesheet" media="screen">
<script type="text/javascript" src="<@macroCommon.resource/>/plugins/datetimepicker/jquery.datetimepicker.js"
        charset="UTF-8"></script>

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

    .fine-uploader-trigger .qq-uploader .qq-total-progress-bar-container {
        width: 60%;
    }
</style>
<script type="text/template" id="qq-template-manual-trigger">
    <div class="qq-uploader-selector" qq-drop-area-text="">

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
                <button type="button" class="qq-btn qq-upload-delete-selector qq-upload-delete">删除</button>
                <span role="status" class="qq-upload-status-text-selector qq-upload-status-text"></span>
            </li>
        </ul>
    </div>
</script>
<body>
<div class="container-fluid">
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>文章管理</a></li>
            <li>文章详情</li>
            <span class="pull-right">
                <button class="btn btn-primary btn-sm" onclick="history.back(-1);">返回</button>
            </span>
        </ol>
    <div class="row content-bottom">
        <div class="col-sm-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="fa fa-comments-o"></i>
                    文章编辑
                </div>

                <div class="panel-body">
                    <div class="tableToolContainer" style="margin-bottom:15px">
                        <form class="form-horizontal" id="addContentFrom">
                            <div class="form-group">
                                <label for="" class="col-sm-2">标题</label>
                                <div class="col-sm-8">
                                    <input class="form-control" id="title" name="title"
                                           value="${(content.title)?if_exists}">
                                </div>
                                <input type="hidden" name="columnId" value="${columnId?c}">
                            </div>

                        <#list elements as element>
                            <div class="form-group">
                                <label class="col-sm-2">${element.label?if_exists}</label>
                                <div class="col-sm-8">
                                    <#if element.fieldType == 'TEXT'>
                                        <input class="form-control" type="text" placeholder="${element.hint?if_exists}"
                                               name="${element.code}" value="${element.value?if_exists}">
                                    <#elseif element.fieldType == 'SECTION'>
                                        <textarea class="form-control" style="min-height: 100px" name="${element.code}" value="">${element.value?if_exists}</textarea>
                                    <#elseif element.fieldType == 'CHOICE'>
                                        <div class="col-sm-8">
                                            <#list element.options as option>
                                                <input type="radio" name="${element.code}" value="${option}"
                                                       <#if element.value?if_exists == option>checked</#if>>&nbsp;${option}
                                                &nbsp;&nbsp;
                                            </#list>
                                        </div>
                                    <#elseif element.fieldType == 'IMAGE'>
                                        <div id="uploader_${element.code}" name="${element.code}_div" class="col-sm-8" style="width:100%; ">
                                            <div id="image_uploader_${element.code}" class="uploader col-sm-10" data-ride="uploader">
                                                <button type="button" class="btn btn-primary uploader-btn-browse" style="margin-top: 10px">
                                                    <i class="icon icon-plus"></i> 选择图片
                                                </button>
                                                <span style="display: inline-block; color: red;">${element.hint?if_exists}</span>
                                                <span style="display: block; margin-top: 10px">也可以直接输入图片网址</span>
                                                <input type="text" name="${element.code}" id="image_value_${element.code}" class="form-control" value="<#if element.value??>${element.value}<#elseif element.placeholder??>${element.placeholder}</#if>">
                                                <div class="uploader-files file-list file-list-grid" style="display: none;"></div>
                                            </div>
                                            <div id="image_div_${element.code}" class="col-sm-2">
                                                <#if element.value??>
                                                    <img src="${element.value}" style='max-width:100%;'/>
                                                <#elseif element.placeholder??>
                                                    <img src="${element.placeholder}" style='max-width:100%;'/>
                                                </#if>
                                            </div>
                                        </div>
                                        <script>
                                            $('#image_uploader_${element.code}').uploader({
                                                multi_selection: false,
                                                file_data_name: 'file',
                                                mime_types: [
                                                    {title: '图片', extensions: 'jpg,gif,png'}
                                                ],
                                                autoUpload: true,            // 当选择文件后立即自动进行上传操作
                                                url: '${uploadPath?if_exists}',     // 文件上传提交地址
                                                deleteActionOnDone: function (file, doRemoveFile) { return true;}
                                            }).on('onFileUploaded', function(event, file, result) {
                                                var uploader = $('#image_uploader_${element.code}').data('zui.uploader');
                                                var plupload = uploader.plupload;
                                                var files = plupload.files;
                                                for (var i = 0; i < files.length; i++) {
                                                    if (files[i].id != file.id) uploader.removeFile(files[i]);
                                                }
                                                $("#image_div_${element.code}").empty();
                                                $("#image_div_${element.code}").append("<img src='"+file.url+"' style='max-width:100%;'/>");
                                                $("#image_value_${element.code}").val(file.url);
                                            });
                                        </script>
                                    <#elseif element.fieldType == 'IMAGES'>
                                        <div class="col-sm-12" style="margin-left: -10px">
                                            <div id="image_uploader_${element.code}" class="uploader col-sm-10" data-ride="uploader">
                                                <button type="button" class="btn btn-primary uploader-btn-browse">
                                                    <i class="icon icon-plus"></i> 选择图片
                                                </button>
                                                <span style="display: block; color: red;">${element.hint?if_exists}</span>
                                                <div class="uploader-files file-list file-list-grid" style="display: none"></div>
                                            </div>
                                            <div id="image_div_${element.code}" class="col-sm-12">
                                                <#list element.value?if_exists as image>
                                                    <div class="imgWrapper" style="max-width:150px;margin:5px;float:left">
                                                        <div class="imgBox" style="max-width:150px;">
                                                            <input type="hidden" name="${element.code}" value="${image?if_exists}">
                                                            <img src="${image?if_exists}"  width="150px" height="150px"/>
                                                        </div>
                                                        <div class="removeButton" style="width:100%;height:30px;text-align:center;">
                                                            <a href="#" onclick="deleteOneFromImages(this);" class="removeImg btn btn-sm red">
                                                                <i class="fa fa-trash-o"></i> 删除
                                                            </a>
                                                        </div>
                                                    </div>
                                                </#list>
                                            </div>
                                            <script>
                                                $('#image_uploader_${element.code}').uploader({
                                                    multi_selection: true,
                                                    file_data_name: 'file',
                                                    mime_types: [
                                                        {title: '图片', extensions: 'jpg,gif,png'}
                                                    ],
                                                    autoUpload: true,            // 当选择文件后立即自动进行上传操作
                                                    fileTemplate: "",
                                                    url: '${uploadPath?if_exists}',     // 文件上传提交地址
                                                    deleteActionOnDone: function (file, doRemoveFile) { return true;}
                                                }).on('onFileUploaded', function(event, file, result) {
                                                    var uploader = $('#image_uploader_${element.code}').data('zui.uploader');
                                                    var html =
                                                            "<div  class='imgWrapper' style='max-width:150px;;margin:5px;float:left'>" +
                                                            "   <div class='file-icon'>" +
                                                            "       <input type='hidden' name='${element.code}' value='" + file.url + "'>" +
                                                            "       <img src='" + file.url + "'  width='150px' height='150px'/>" +
                                                            "   </div>" +
                                                            "   <div class='removeButton' style='width:100%;height:30px;text-align:center;'>" +
                                                            "       <a href='#' onclick='deleteFileFromImages(\"image_uploader_${element.code}\", \"" + file.id + "\", this);' class='removeImg btn btn-sm red'>" +
                                                            "           <i class='fa fa-trash-o'></i> 删除" +
                                                            "       </a>" +
                                                            "   </div>" +
                                                            "</div>";
                                                    $("#image_div_${element.code}").append(html);
                                                });
                                            </script>
                                        </div>
                                    <#elseif element.fieldType == 'HTML'>
                                        <script class="editorDiv" id="${element.code}" name="${element.code}"
                                                type="text/plain"
                                                style="height:300px; padding-top:5px;">${element.value?if_exists}</script>
                                    <#elseif element.fieldType == 'GPS'>
                                        <div class="col-sm-3" style="margin-left: -10px">
                                            <input name="(N)${element.code+"x"}" class="form-control"
                                                   value="${(element.longitude!104.066138)?string("0.000000")}" type="text" placeholder="经度">
                                        </div>
                                        <div class="col-sm-3">
                                            <input name="(N)${element.code+"y"}" class="form-control"
                                                   value="${(element.latitude!30.572906)?string("0.000000")}" type="text" placeholder="纬度">
                                        </div>
                                        <div class="col-sm-2">
                                            <span>
                                                <button class="btn btn-success" type="button"
                                                        onclick="clickLocation('(N)${element.code+"x"}','(N)${element.code+"y"}');">选取坐标</button>
                                            </span>
                                        </div>
                                    <#elseif element.fieldType == 'DROPDOWN'>
                                        <select class="form-control" id="${element.code}" name="${element.code}">
                                            <#list element.values as root>
                                                <@m1_columns root 0 root_index/>
                                            </#list>
                                        </select>
                                        <script>
                                            $("#${element.code}").val('${element.value?if_exists}');
                                        </script>
                                    <#elseif element.fieldType == 'DATE'>
                                        <input type="text" class="form-control" value="${(element.value?string("yyyy-MM-dd"))!}"
                                               name="(D)${element.code}"
                                               id="${element.code}"/>
                                        <script>
                                            $('#${element.code}').datetimepicker({
                                                lang: 'ch',
                                                timepicker: false,
                                                format: 'Y-m-d',
                                                formatDate: 'Y-m-d',
                                                allowBlank: true
                                            });
                                        </script>
                                    <#elseif element.fieldType == 'DATETIME'>
                                        <input type="text" class="form-control" value="${(element.value?string("yyyy-MM-dd HH:mm"))!}"
                                               name="(D)${element.code}"
                                               id="${element.code}"/>
                                        <script>
                                            $('#${element.code}').datetimepicker({
                                                lang: 'ch',
                                                format: 'Y-m-d H:i',
                                                formatDate: 'Y-m-d H:i'
                                            });
                                        </script>
                                    <#elseif element.fieldType == 'TIME'>
                                        <input type="text"class="form-control" value="${(element.value?string("HH:mm"))!}"
                                               name="(D)${element.code}"
                                               id="${element.code}"/>
                                        <script>
                                            $('#${element.code}').datetimepicker({
                                                datepicker: false,
                                                format: 'H:i',
                                                step: 5
                                            });
                                        </script>
                                    <#elseif element.fieldType == 'ARTICLE'>
                                        <input type="hidden" class="form-control" id="${element.code}" name="${element.code}"
                                               value="${element.value?if_exists}">
                                        <ul id="${element.code}_ul" style="list-style: none;padding-left: 2px;font-size: 14px;color: #212121;">
                                            <#list element.articles?if_exists as article>
                                                <li style="margin:4px 0;">${article.title?if_exists}
                                                    <span class="btn btn-xs btn-danger" onclick="delRelated('${article._id?if_exists}','${element.code}',this)">
                                                        <i class="fa  fa-remove"></i></span>
                                                </li>
                                            </#list>
                                        </ul>
                                        <a class="btn btn-success"
                                           onclick="doAddRelated('${element.code}')" data-toggle="modal"
                                           data-target="#AssociatedModal"><i class="fa fa-fw fa-plus "></i>关联文章</a>
                                    <#elseif element.fieldType == 'DOCUMENT'>
                                        <div id="uploader_${element.code}" name="${element.code}_div" class="wu-example"
                                             style="width:100%">
                                            <div id="fileList" class="uploader-list">
                                                <span style="color: red">${element.hint?if_exists}</span>
                                                <#if element.value??>
                                                    <img src="${element.value}" width="30%"
                                                         style="border:1px solid #ccc;overflow:hidden;"/>
                                                </#if>
                                            </div>
                                            <div id="fine-uploader-trigger-${element.code}"
                                                 class="fine-uploader-trigger"></div>
                                        </div>
                                        <script>
                                            var manualUploader_${element.code} = new qq.FineUploader({
                                                element: document.getElementById('fine-uploader-trigger-${element.code}'),
                                                template: 'qq-template-manual-trigger',
                                                request: {
                                                    endpoint: '${fineUploadPath?if_exists}'
                                                },
                                                thumbnails: {
                                                    placeholders: {
                                                        waitingPath: '/fineUpload/waiting-generic.png',
                                                        notAvailablePath: '/fineUpload/not_available-generic.png'
                                                    }
                                                },
                                                <#if element.fileType?exists && (element.fileType?length > 0) >
                                                    validation: {
                                                        allowedExtensions: [${element.fileType?if_exists}]
                                                    },
                                                </#if>
                                                autoUpload: false,
                                                debug: true,
                                                callbacks: {
                                                    onComplete: function (id, name, response) {
                                                        if (response.success) {
                                                            $("div[name='${element.code}_div']").append("<input type='hidden' name='${element.code}' value=" + response.tempLink + " >");
                                                        }
                                                    }
                                                },
                                                deleteFile: {
                                                    enabled: true,
                                                    endpoint: '#',
                                                    deletingFailedText: '删除失败！'
                                                }
                                            });
                                            $("#uploader_${element.code}").find("#trigger-upload").attr("onclick", "manualUploader_${element.code}.uploadStoredFiles();");
                                        </script>
                                    </#if>
                                </div>
                            </div>
                        </#list>

                            <div class="box-footer" style="text-align: center">
                                <span class="btn btn-primary">
                                    <input type="radio" name="(N)online" value="1" checked="checked"
                                           style="width:50px;height:15px;display:block">
                                    <span>立即上线</span>
                                </span>
                                <span class="btn btn-primary">
                                    <input type="radio" name="(N)online" value="0"
                                           style="width:50px;height:15px;display:block">
                                    <span>不上线</span>
                                </span>
                            </div>
                            <div class="box-footer" style="text-align:center">
                                <input type="button" onclick="doSubmit()" class="btn btn-info" style="width:150px;"
                                       value="提交">
                                <a href="#" class="btn btn-danger" style="width:150px;"
                                   onClick="javascript :history.back(-1);">取消</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <footer class="main-footer">
        <@common.footer />
        </footer>
    </div>
    <script>
        function deleteOneFromImages(elem) {
            $(elem).parent().parent().remove();
        }
        function deleteFileFromImages(uploaderId, fileId, elem) {
            var uploader = $("#" + uploaderId).data('zui.uploader');
            var plupload = uploader.plupload;
            var files = plupload.files;
            for (var i = 0; i < files.length; i++) {
                if (files[i].id == fileId) {
                    uploader.removeFile(files[i]);
                    break;
                }
            }
            $(elem).parent().parent().remove();
        }
        $(function () {
            var editorDiv = $(".editorDiv");
            for (var i = 0; i < editorDiv.length; i++) {
                var editor = editorDiv[i];
                UE.getEditor($(editor).attr("id"));
            }
        });
        $(function () {
            $('.form_datetime').datetimepicker({
                language: 'zh-CN',
                weekStart: 1,
                todayBtn: 1,
                autoclose: 1,
                todayHighlight: 1,
                startView: 2,
                forceParse: 0,
                showMeridian: 1
            });
            $(".timeInput").datetimepicker({
                showInputs: false
            });
            $(".dateInput").datetimepicker({
                format: 'yyyy-mm-dd',
                language: 'zh-CN'
            });
        });
    </script>
</body>

<script>
    function doSubmit() {
        var fromData = getFormData($("#addContentFrom"));
        $.ajax({
        <#if articleId?default("") != "" >
            url: "/api/cms/article/update?id=${articleId}",
        <#else>
            url: "/api/cms/article/add",
        </#if>
            type: "post",
            dataType: "json",
            cache: false,
            processData: false,
            contentType: false,
            data: fromData,
            complete: function (data) {
                if (data.responseJSON.code < 0) {
                    alertShow("warning", data.responseJSON.message, 5000);
                } else {
                    <#if alone?if_exists>
                        window.location.href="/cms/article/alone?columnId="+ "${columnId?c}";
                    <#else>
                        window.location.href="/cms/article?columnId="+ "${columnId?c}";
                    </#if>
                }
            }
        })
    }
    function getFormData(form) {
        var formData = new FormData();
        var a = $(form).serializeArray();
        $.each(a, function () {
            formData.append(this.name, this.value)
        });
        return formData;
    }
    function autoIframeHeight() {
        setTimeout(function () {
            $("#related_articles").height($("#related_articles").contents().find("#iframeDiv").height() + 50);
        }, 100);
    }
</script>

<div class="modal fade" id="AssociatedModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" style="width:70%;">
        <div class="form-horizontal" id="lawsAddForm" name="lawsAddForm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close"
                            data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="myModalLabel">
                        文章选取
                    </h4>
                </div>
                <div class="modal-body">
                    <div class="modalForm">
                        <div class="form-group">
                            <iframe id="related_articles" name="auto" width=100% onload="autoIframeHeight()" frameborder=0 float:right
                                    scrolling=auto scrolling=auto src=""/>
                            </iframe>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <a href="#" class="btn btn-danger" data-dismiss="modal">
                        <i class="fa fa-fw fa-remove"></i>取消
                    </a>
                    <a type="summit" class="btn btn-info" onclick="addRelated()">
                        <i class="fa fa-fw  fa-save"></i>添加
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    function addRelated() {
        var artileIds = $("#related_articles").contents().find("input[name='id']");
        var fieldCode = $("#related_articles").contents().find("input[name='fieldCode']").val();
        var relatedIds = "";
        var ul = document.getElementById(fieldCode + "_ul");
        ul.innerHTML = "";
        for (var i = 0; i < artileIds.length; i++) {
            if (artileIds[i].checked) {
                relatedIds += $(artileIds[i]).val() + ",";
                var li = document.createElement("li");
                li.setAttribute("style", "margin:4px 0;")
                li.innerText = $(artileIds[i]).parent().parent().children('td').eq(1).html() + "  ";
                var span = document.createElement("span");
                span.setAttribute("class", "btn btn-xs btn-danger")
                span.setAttribute("onclick", "delRelated('" + $(artileIds[i]).val() + "','" + fieldCode + "',this)")
                var iElement = document.createElement("i");
                iElement.setAttribute("class", "fa  fa-remove");
                span.appendChild(iElement);
                li.appendChild(span);
                ul.appendChild(li);
            }
        }
        document.getElementById(fieldCode).value = relatedIds.substring(0, relatedIds.length - 1)
        $("#AssociatedModal").modal('hide')
    }
    function delRelated(id, name, span) {
        $(span.parentNode).remove()
        var value = $("#" + name + "").val();
        var values = value.split(",")
        var newValue = "";
        for (var val = 0; val < values.length; val++) {
            if (values[val] != id) {
                newValue += values[val] + ",";
            }
        }
        $("#" + name + "").val(newValue.substring(0, newValue.length - 1));
    }
    function doAddRelated(fieldCode) {
        $("#related_articles").attr("src", "/cms/article/related?fieldCode=" + fieldCode + "&title=")
    }
</script>

</html>
