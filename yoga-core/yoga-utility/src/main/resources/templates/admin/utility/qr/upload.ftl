<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<@html>
    <@head includeUploader=true>
    <style>
        #outer {
            width: 60%;
            min-height: 500px;
            border: 1px solid #DDDDDD;
            box-shadow: 3px 3px 3px 3px #DDDDDD;
            margin: 0px auto;
            text-align: center;
            padding: 10px;
            margin-top: 10%;
        }
    </style>
    </@head>
    <@bodyFrame>
        <div id="outer">
            <form class="form-horizontal" style="margin-top: 30px">
                <label class="col-md-12" style="margin-bottom: 50px">上传附件</label>
                <div class="form-group">
                    <div class="col-md-10 col-md-offset-1">
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
                <div class="form-group">
                    <div class="col-md-12" style="text-align: center">
                        <a onclick="doSubmit();" class="btn btn-primary">提交</a>
                        <button type="reset" class="btn btn-danger">重置</button>
                    </div>
                </div>
            </form>
        </div>
    </@bodyFrame>
<script type="text/javascript">
    $(document).ready(function () {
        $('#myUploader').uploader({
            autoUpload: true,
            url: '/admin/system/uploader/zui/upload.json',
            lang: 'zh_cn',
            headers: {
                token: "${token!}",
                tid: "${(tid)!}"
            },
            multi_selection: ${(param.singleFile?string('false', 'true'))!'true'},
            file_data_name: 'file',
            deleteActionOnDone: function (file, doRemoveFile) {
                return true;
            },
            responseHandler: function (responseObject, file) {
                file.data = JSON.parse(responseObject.response);
            }
        });
    });
    function doSubmit() {
        var uploader = $('#myUploader').data('zui.uploader');
        var plupload = uploader.plupload;
        var files = plupload.files;
        var json = [];
        for (var i = 0; i < files.length; i++) {
            if (files[i].status != 5) {
                alertShow("warning", "部分文件尚未完成上传或者上传失败！", 3000);
                return;
            }
            json.push(files[i].data);
        }
        $.ajax({
            url: '/admin/qr/uploaded.json?code=${code!}',
            type: 'post',
            traditional: true,
            data: JSON.stringify(json),
            headers: {
                token: "${token!}",
                tid: "${tid!}"
            },
            dataType: 'json',
            contentType: 'application/json',
            success: function (data) {
                if (data.code < 0) {
                    alertShow("warning", data.message, 3000);
                    return;
                } else {
                    alertShow("info", "绑定数据成功，请到手机上进行下一步操作！", 3000, function () {
                        window.location.href = "/admin/qr";
                    });
                }
            },
            error: function (data) {
                alertShow("warning", data.message, 3000);
            }
        });
    }
</script>
</body>
</@html>
