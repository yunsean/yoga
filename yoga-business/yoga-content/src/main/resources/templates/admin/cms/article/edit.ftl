<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<#setting number_format="##0.##">
<@html>
<@head includeDate=true includeUploader=true includeUEditor=true>
    <script src="http://webapi.amap.com/maps?v=1.3&key=61dfd7a5aa538df0de297570f9221920&plugin=AMap.Autocomplete,AMap.PlaceSearch" type="text/javascript"></script>
    <script src="/admin/map/location.js" type="text/javascript"></script>
    <style>
        td {
            vertical-align: middle!important;
        }
        .noborder {
            border: none!important;
            -webkit-box-shadow: none!important;;
            box-shadow: none!important;
        }
        .hint {
            display: block;
            font-size: 8px;
            color: #aaa;
        }
        .whitereadonly {
            background-color: white;
            flex:1;
            display: inline-block
        }
        .img {
            height: 32px;
        }
        .imgWrapper {
            max-width: 150px;
            margin: 5px;
            display: inline-block;
        }
        .pl0 {
            padding-left: 0!important;
        }
        .ml20 {
            margin-left: 20px!important;
        }
        .removeButton {
            width: 100%;
            height: 30px;
            text-align: center;
        }
        .related {
            margin-right: 20px;
            padding: 5px 20px;
        }
</style>
</@head>
<@bodyFrame>
    <@crumbRoot name="内容管理" icon="icon-user">
        <@crumbItem href="#" name="文章管理" backLevel=1/>
        <@crumbItem href="#" name="文章编辑" />
        <@backButton />
    </@crumbRoot>
    <@bodyContent>
        <@panel>
            <@panelHeading "文章编辑" />
            <@panelBody>
                <form class="form-horizontal" id="addContentFrom">
                    <input type="hidden" name="columnId" value="${(columnId?c)!}">
                    <input type="hidden" name="templateId" value="${(templateId?c)!}">
                    <input type="hidden" name="columnCode" value="${columnCode!}">
                    <input type="hidden" name="templateCode" value="${templateCode!}">
                    <#list elements as element>
                        <#if element.fieldType == 'HIDDEN'>
                            <input class="form-control" type="hidden" name="${element.code!}" value="${element.value!}">
                        <#else>
                            <div class="form-group">
                                <label class="col-sm-2">${element.label!}</label>
                                <div class="col-sm-8">
                                    <#if element.fieldType == 'TEXT'>
                                        <input class="form-control" type="text" name="${element.code!}" value="${element.value!}">
                                        <span class="hint">${element.hint!}</span>
                                    <#elseif element.fieldType == 'SECTION'>
                                        <textarea class="form-control" style="min-height: 100px" name="${element.code!}" value="">${element.value!}</textarea>
                                        <span class="hint">${element.hint!}</span>
                                    <#elseif element.fieldType == 'CHOICE'>
                                        <#list (element.options?keys)! as key>
                                            <label class="checkbox-inline pl0">
                                                <input type="radio" name="${element.code}" value="${key}" <#if element.value! == key>checked</#if>>&nbsp;${element.options[key]!}                                         &nbsp;&nbsp;
                                            </label>
                                        </#list>
                                        <span class="hint">${element.hint!}</span>
                                    <#elseif element.fieldType == 'CHECKBOX'>
                                        <#list (element.options?keys)! as key>
                                            <label class="checkbox-inline">
                                                <input type="checkbox" name="(s)${element.code}" value="${key}" <#if (element.checkeds?seq_contains(key))!false>checked</#if>>&nbsp;${element.options[key]!}                                         &nbsp;&nbsp;
                                            </label>
                                        </#list>
                                        <span class="hint">${element.hint!}</span>
                                    <#elseif element.fieldType == 'IMAGE'>
                                        <div class="col-sm-8" style="width:100%;display: flex;padding: 0">
                                            <input type="text" class="form-control whitereadonly" name="${element.code}" id="image_value_${element.code}" value="<#if element.value??>${element.value}<#elseif element.placeholder??>${element.placeholder}</#if>" />
                                            <img id="image_div_${element.code}" src="<#if element.value??>${element.value}<#elseif element.placeholder??>${element.placeholder}</#if>" class="img"/>
                                            <div style="display: inline-block; margin-bottom:0px!important;" id="image_uploader_${element.code}" class="uploader" data-ride="uploader">
                                                <div class="uploader-files file-list file-list-grid" style="display: none;"></div>
                                                <button type="button" class="btn btn-success uploader-btn-browse" style="width: 98px; margin-left: 15px; display: inline-block">
                                                    <i class="icon icon-picture"></i>选择图片
                                                </button>
                                            </div>
                                        </div>
                                        <span class="hint">${element.hint!}</span>
                                        <script>
                                            $('#image_uploader_${element.code}').uploader({
                                                multi_selection: false,
                                                file_data_name: 'file',
                                                mime_types: [{title: '图片', extensions: 'jpg,gif,png'}],
                                                autoUpload: true,
                                                url: '/admin/system/uploader/zui/upload.json',
                                                previewImageSize: {width: 100, height: 100},
                                                deleteActionOnDone: function (file, doRemoveFile) {return true;}
                                            }).on('onFileUploaded', function(event, file, result) {
                                                var uploader = $('#image_uploader_${element.code}').data('zui.uploader');
                                                var plupload = uploader.plupload;
                                                var files = plupload.files;
                                                for (var i = 0; i < files.length; i++) uploader.removeFile(files[i]);
                                                $("#image_div_${element.code}").attr('src', file.url);
                                                $("#image_value_${element.code}").val(file.url);
                                            });
                                        </script>
                                    <#elseif element.fieldType == 'IMAGES'>
                                        <div class="col-sm-8" style="width:100%; padding: 0">
                                            <div id="image_uploader_${element.code}" class="uploader" data-ride="uploader"  style="display: block; padding: 0; display: flex">
                                                <span class="hint" style="flex: 1; margin-right: 20px; margin-top: 8px">${element.hint!}</span>
                                                <button type="button" class="btn btn-success uploader-btn-browse">
                                                    <i class="icon icon-file-picture"></i>添加图片
                                                </button>
                                                <div class="uploader-files file-list file-list-grid" style="display: none"></div>
                                            </div>
                                            <div id="image_div_${element.code}" style="display: block; width: 100%; padding: 0">
                                                <#list (element.value)! as image>
                                                    <div class="imgWrapper" style="max-width:150px;margin:5px;float:left">
                                                        <div class="imgBox" style="max-width:150px;">
                                                            <input type="hidden" name="(s)${element.code!}" value="${image!}">
                                                            <img src="${image!}"  height="150px"/>
                                                        </div>
                                                        <div class="removeButton" style="width:100%;height:30px;text-align:center;">
                                                            <a href="#" onclick="deleteOneFromImages(this);" class="btn btn-sm btn-danger">
                                                                <i class="icon icon-trash"></i> 删除
                                                            </a>
                                                        </div>
                                                    </div>
                                                </#list>
                                            </div>
                                            <script>
                                                $('#image_uploader_${element.code}').uploader({
                                                    multi_selection: true,
                                                    file_data_name: 'file',
                                                    mime_types: [ {title: '图片', extensions: 'jpg,gif,png'} ],
                                                    autoUpload: true,
                                                    url: '/admin/system/uploader/zui/upload.json',
                                                    deleteActionOnDone: function (file, doRemoveFile) { return true;}
                                                }).on('onFileUploaded', function(event, file, result) {
                                                    var uploader = $('#image_uploader_${element.code}').data('zui.uploader');
                                                    var html =
                                                        "<div  class='imgWrapper' style='max-width:150px;;margin:5px;float:left'>" +
                                                        "   <div class='file-icon'>" +
                                                        "       <input type='hidden' name='(s)${element.code}' value='" + file.url + "'>" +
                                                        "       <img src='" + file.url + "'  height='150px'/>" +
                                                        "   </div>" +
                                                        "   <div class='removeButton' style='width:100%;height:30px;text-align:center;'>" +
                                                        "       <a href='#' onclick='deleteFileFromImages(\"image_uploader_${element.code}\", \"" + file.id + "\", this);' class='btn btn-sm btn-danger'>" +
                                                        "           <i class='icon icon-trash'></i> 删除" +
                                                        "       </a>" +
                                                        "   </div>" +
                                                        "</div>";
                                                    $("#image_div_${element.code}").append(html);
                                                });
                                            </script>
                                        </div>
                                    <#elseif element.fieldType == 'HTML'>
                                        <script class="editorDiv" id="${element.code!}" name="${element.code!}" type="text/plain" style="height:300px; padding-top:5px;">${element.value!}</script>
                                        <span class="hint">${element.hint!}</span>
                                    <#elseif element.fieldType == 'GPS'>
                                        <div style="display: flex">
                                            <div style="flex: 1; margin-right: 20px">
                                                <input name="(N)${element.code+"_x"}" class="form-control" value="${(element.longitude!104.066138)?string("0.000000")}" type="text" placeholder="经度">
                                            </div>
                                            <div style="flex: 1; margin-right: 20px">
                                                <input name="(N)${element.code+"_y"}" class="form-control" value="${(element.latitude!30.572906)?string("0.000000")}" type="text" placeholder="纬度">
                                            </div>
                                            <button class="btn btn-success" type="button" onclick="showMap('${element.code!}')">
                                                <i class="icon icon-location-arrow"></i> 选取坐标
                                            </button>
                                        </div>
                                        <span class="hint">${element.hint!}</span>
                                    <#elseif element.fieldType == 'CHECKDOWN'>
                                        <#list (element.values)! as item>
                                            <label class="checkbox-inline">
                                                <input type="checkbox" name="(s)${element.code}" value="${(item.id?c)!}" <#if (element.checkeds?seq_contains(item.id?c))!false>checked</#if>>&nbsp;${(item.name)!}                                    &nbsp;&nbsp;
                                            </label>
                                        </#list>
                                        <span class="hint">${element.hint!}</span>
                                    <#elseif element.fieldType == 'DROPDOWN'>
                                        <select class="form-control" id="${element.code!}" name="${element.code!}">
                                            <option value=""></option>
                                            <#list (element.values)! as root>
                                                <@m1_columns root 0 root_index/>
                                            </#list>
                                        </select>
                                        <span class="hint">${element.hint!}</span>
                                        <script>
                                            $("#${element.code!}").val('${element.value!}');
                                        </script>
                                    <#elseif element.fieldType == 'DATE'>
                                        <input type="text" class="form-control" value="${(element.value?string("yyyy-MM-dd"))!}" name="(D)${element.code}" id="${element.code!}"/>
                                        <span class="hint">${element.hint!}</span>
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
                                        <input type="text" class="form-control" value="${(element.value?string("yyyy-MM-dd HH:mm"))!}" name="(D)${element.code}" id="${element.code}"/>
                                        <span class="hint">${element.hint!}</span>
                                        <script>
                                            $('#${element.code}').datetimepicker({
                                                lang: 'ch',
                                                format: 'Y-m-d H:i',
                                                formatDate: 'Y-m-d H:i'
                                            });
                                        </script>
                                    <#elseif element.fieldType == 'TIME'>
                                        <input type="text"class="form-control" value="${(element.value?string("HH:mm"))!}" name="(D)${element.code}" id="${element.code}"/>
                                        <span class="hint">${element.hint!}</span>
                                        <script>
                                            $('#${element.code}').datetimepicker({
                                                datepicker: false,
                                                format: 'H:i',
                                                step: 5
                                            });
                                        </script>
                                    <#elseif element.fieldType == 'ARTICLE'>
                                        <div style="width: 100%; padding: 0; display: flex; flex-direction: row;">
                                            <span class="hint" style="flex: 1; margin-right: 20px; margin-top: 8px">${element.hint!}</span>
                                            <a class="btn btn-success" onclick="addRelated('${element.code!}')">
                                                <i class="icon icon-link "></i>关联文章
                                            </a>
                                        </div>
                                        <div id="${element.code}_ul" style="list-style: none;padding-left: 2px;font-size: 14px;color: #212121;">
                                            <#list element.articles! as article>
                                                <span class="label label-badge label-primary related">${article.title!}
                                                    <input type="hidden" name="(s)${element.code!}" value="${article._id!}">
                                                    <a onclick="delRelated('${article._id!}','${element.code}',this)">
                                                        <i class="icon icon-remove"></i>
                                                    </a>
                                                </span>
                                            </#list>
                                        </div>
                                    <#elseif element.fieldType == 'DOCUMENT'>
                                        <div style="width: 100%; padding: 0; display: flex; flex-direction: row;">
                                            <span class="hint" style="flex: 1; margin-right: 20px; margin-top: 8px">${element.hint!}</span>
                                            <a class="btn btn-success" id="file_upload_button_${element.code}"">
                                                <i class="icon icon-plus-sign"></i>添加文档
                                            </a>
                                        </div>
                                        <div id="file_upload_${element.code}" style="width: 100%; padding: 0; display: flex; flex-direction: row;">
                                            <div class="file-list" data-drag-placeholder="请拖拽文件到此处" style="flex: 1"></div>
                                            <div style="margin-left: 20px; display: none">
                                                <button type="button" class="btn btn-success uploader-btn-browse">
                                                    <i class="icon icon-plus-sign"></i>添加文档
                                                </button>
                                            </div>
                                        </div>
                                        <div style="display: none;" id="${element.code}_files">
                                            <#list element.files! as file>
                                                <input name='(s)${element.code}' type='hidden' file-id='staticfiles-${(file_index?c)!}' value='${file}'/>
                                            </#list>
                                        </div>
                                        <span class="hint">${element.hint!}</span>
                                        <script>
                                            $('#file_upload_${element.code}').uploader({
                                                autoUpload: true,
                                                browse_button: "#file_upload_button_${element.code}",
                                                multi_selection: true,
                                                mime_types: [ {title: '支持的文件', extensions: '${element.fileType!}'} ],
                                                url: '/admin/system/uploader/zui/upload.json',
                                                deleteActionOnDone: function (file, doRemoveFile) {
                                                    $("[file-id='" + file.id + "']").remove();
                                                    return true
                                                },
                                                <#if element.value??>
                                                staticFiles: [
                                                    <#list element.files! as file>
                                                    {name: '${(file)!}', url: '${(file)!}', id: 'staticfiles-${(file_index?c)!}'},
                                                    </#list>
                                                ],
                                                </#if>
                                            }).on('onFileUploaded', function(event, file, result) {
                                                var content = "<input name='(s)${element.code}' type='hidden' file-id='" + file.id + "' value='" + file.remoteData.url + "'/>"
                                                $("#${element.code}_files").append(content)
                                            });
                                        </script>
                                    </#if>
                                </div>
                            </div>
                        </#if>
                    </#list>
                    <div class="box-footer" style="text-align:center">
                        <input type="button" onclick="doSubmit()" class="btn btn-info" style="width:150px;" value="提交">
                        <a href="#" class="btn btn-danger" style="width:150px;" onClick="javascript :history.back(-1);">取消</a>
                    </div>
                </form>
            </@panelBody>
        </@panel>
    </@bodyContent>
</@bodyFrame>


    <#macro m1_column column level index>
        <option value="${(column.id?c)!}">
            <#list 0..level as x><#if x < level>┃&nbsp;&nbsp;<#else>┠</#if></#list>${(column.name)!}
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

    <script>
        function autoIframeHeight() {
            setTimeout(function () {
                var height = $("#related_articles").contents().find("#iframeDiv").height();
                $("#related_articles").height(height);
            }, 100);
        }
    </script>

    <@modal title="添加关联文章" onOk="saveRelated">
        <@inputHidden name="code" id="edit_id"/>
        <div class="modalForm">
            <div class="form-group">
                <iframe id="related_articles" name="auto" width=100% onload="autoIframeHeight()" frameborder=0 scrolling=auto scrolling=auto src=""/>
                </iframe>
            </div>
        </div>
    </@modal>

    <script>
        function saveRelated() {
            var artileIds = $("#related_articles").contents().find("input[name='id']");
            var fieldCode = $("#add_form input[name='code']").val();
            var ul = $("#" + fieldCode + "_ul");
            for (var i = 0; i < artileIds.length; i++) {
                if (artileIds[i].checked) {
                    var html = '<span class="label label-badge label-primary related">' + $(artileIds[i]).parent().parent().children('td').eq(1).html() + '\n' +
                        '<input type="hidden" name="(s)' + fieldCode + '" value="' + $(artileIds[i]).val() + '">' +
                        '    <a onclick="delRelated(\'' + $(artileIds[i]).val() + '\',\'' + fieldCode + '\',this)">\n' +
                        '        <i class="icon icon-remove"></i>\n' +
                        '    </a>\n' +
                        '</span>';
                    ul.append(html);
                }
            }
            $("#add_modal").modal('hide')
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
        function addRelated(code) {
            $("#add_form input[name='code']").val(code);
            $("#related_articles").attr("src", "/admin/cms/article/related?title=")
            $("#add_modal").modal({
                keyboard : false,
                show     : true,
                position : 20,
                moveable : true
            });
        }
        function showMap(code) {
            var x = $("#addContentFrom input[name='(N)" + code + "_x']").val();
            var y = $("#addContentFrom input[name='(N)" + code + "_y']").val();
            clickLocation('', function(x, y) {
                $("#addContentFrom input[name='(N)" + code + "_x']").val(x);
                $("#addContentFrom input[name='(N)" + code + "_y']").val(y);
            }, x, y);
        }
        function doSubmit() {
            var fromData = getFormData($("#addContentFrom"));
            $.ajax({
                <#if ((param.articleId)!"") != "" >
                url: "/admin/cms/article/update.json?id=${param.articleId}",
                <#else>
                url: "/admin/cms/article/add.json",
                </#if>
                type: "post",
                dataType: "json",
                cache: false,
                processData: false,
                contentType: false,
                data: fromData,
                complete: function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        history.back(-1);
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

</@html>
