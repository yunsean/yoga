<#import "/macro_common.ftl" as macroCommon>
<@macroCommon.html>
<div id="iframeDiv">
    <div class="form-horizontal modalForm">
        <div class="modal-body">
            <form class="modalForm" id="form1">
                <div class="form-group">
                    <label class="col-sm-3"></label>
                    <label class="col-sm-2 control-label">验证码长度：</label>
                    <div class="col-sm-4">
                        <input type="text" class="form-control" name="length" value="${setting.length?default(4)}">
                    </div>
                    <label class="col-sm-3"></label>
                </div>
                <div class="form-group">
                    <label class="col-sm-3"></label>
                    <label class="col-sm-2 control-label">短信失效时间（秒）：</label>
                    <div class="col-sm-4">
                        <input type="text" class="form-control" name="expire" value="${setting.expire?default(60)}">
                    </div>
                    <label class="col-sm-3" class="form-control"></label>
                </div>
                <div class="form-group">
                    <label class="col-sm-3"></label>
                    <label class="col-sm-2 control-label">发送最小间隔（秒）：</label>
                    <div class="col-sm-4">
                        <input type="text" class="form-control" name="interval" value="${setting.interval?default(0)}">
                    </div>
                    <label class="col-sm-3" class="form-control"></label>
                </div>
                <div class="form-group">
                    <label class="col-sm-3"></label>
                    <label class="col-sm-2 control-label">APP自动填充：</label>
                    <div class="col-sm-4">
                        <input type="checkbox" style="margin-top: 10px" id="autofill"
                               <#if setting.autofill?if_exists>
                                   checked
                               </#if>>
                    </div>
                    <label class="col-sm-3"></label>
                </div>
                <div class="form-group">
                    <label class="col-sm-3"></label>
                    <label class="col-sm-2 control-label">信息格式：</label>
                    <div class="col-sm-4">
                        <div class="small" style="margin-top: 7px; margin-bottom: 5px">请使用#code#表示验证码占位[必须]，#time#表示失效时间占位！</div>
                        <textarea type="text" class="form-control" name="format" style="min-height: 100px;">${setting.format?if_exists}</textarea>
                    </div>
                    <label class="col-sm-3"></label>
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
        var autofill = $("#autofill").is(':checked');
        $.post(
                "/common/captcha/setting/save?autofill=" + autofill,
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
</@macroCommon.html>