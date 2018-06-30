<#import "/macro_common.ftl" as macroCommon>
<@macroCommon.html>
<div id="iframeDiv">
    <div class="form-horizontal modalForm">
        <div class="modal-body">
            <form class="modalForm" id="form1">
                <div class="form-group">
                    <label class="col-sm-3"></label>
                    <label class="col-sm-2 control-label">用户AppID：</label>
                    <div class="col-sm-4">
                        <input type="text" class="form-control" name="appId" value="${setting.appId?if_exists}">
                    </div>
                    <label class="col-sm-3"></label>
                </div>
                <div class="form-group">
                    <label class="col-sm-3"></label>
                    <label class="col-sm-2 control-label">支付宝公匙：</label>
                    <div class="col-sm-4">
                        <input type="text" class="form-control" name="alipayPublicKey" placeholder="****">
                    </div>
                    <label class="col-sm-3"></label>
                </div>
                <div class="form-group">
                    <label class="col-sm-3"></label>
                    <label class="col-sm-2 control-label">用户私匙：</label>
                    <div class="col-sm-4">
                        <textarea type="text" class="form-control" style="min-height: 100px" name="appPrivateKey" placeholder="****"></textarea>
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
        $.post(
                "/api/pay/alipay/setting/save",
                form,
                function (data) {
                    if (data.code < 0) {
                        alertShow("warning", data.message, 3000);
                        return;
                    } else {
                        window.parent.closeModal();
                    }
                },
                "json"
        );
    }
</script>
</@macroCommon.html>