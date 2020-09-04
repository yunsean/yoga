<#include "/page.component.ftl">
<@html>
    <@head includeDate=false>
    <style>
        .radio1 {
            display: inline-block;
            height: 1em;
            border-radius: 4px;
            background-color: gray;
        }
        .radio1:checked+span {
            background-color: red;
        }
    </style>
    </@head>
    <@body>
    <div id="iframeDiv">
        <div class="form-horizontal modalForm">
            <div class="modal-body">
                <form class="modalForm" id="form1">
                    <div class="form-group">
                        <label class="col-sm-3"></label>
                        <label class="col-sm-2 control-label">AppKey：</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="appKey" value="${(setting.appKey)!}">
                        </div>
                        <label class="col-sm-3" class="form-control"></label>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3"></label>
                        <label class="col-sm-2 control-label">Master Secret：</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="masterSecret" value="${(setting.masterSecret)!}">
                        </div>
                        <label class="col-sm-3" class="form-control"></label>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3"></label>
                        <label class="col-sm-2 control-label"></label>
                        <div class="col-sm-4">
                            <label class="checkbox-inline">
                                <input name="product" type="checkbox" ${(setting.product?string('checked', ''))!}>iOS生产环境
                            </label>
                        </div>
                        <label class="col-sm-3" class="form-control"></label>
                    </div>
                </form>
            </div>
            <hr>
            <div class="box-footer" style="margin-top: 10px">
                <button class="btn btn-default col-sm-1 col-sm-offset-5" style="margin-right: 10px" onclick="save()">确认</button>
                <button class="btn btn-default col-sm-1" style="margin-left: 10px" onclick="window.parent.closeModal()">取消</button>
            </div>
        </div>
    </div>

    <script>
        function save() {
            var form = $("#form1").serialize();
            $.post(
                    "/admin/setting/push/jiguang/save.json",
                    form,
                    function (data) {
                        if (data.code < 0) {
                            alertShow("warning", data.message, 3000);
                        } else {
                            window.parent.closeModal();
                        }
                    },
                    "json"
            );
        }
    </script>
    </@body>
</@html>