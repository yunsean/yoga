<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<@html>
    <@head includeUploader=true>
    </@head>
    <@body>
        <div id="iframeDiv">
            <div class="form-horizontal modalForm">
                <div class="modal-body">
                    <form class="modalForm" id="form1">
                        <div class="form-group">
                            <label class="col-sm-offset-3 col-sm-2 control-label">当前年度：</label>
                            <div class="col-sm-4">
                                <select type="text" class="form-control" id="annualNum" name="annualNum">
                                    <#list years as year>
                                        <option value="${year?c}">${year?c}年</option>
                                    </#list>
                                </select>
                                <script>
                                    $("#annualNum").val(${setting.annualNum?default(0)?c});
                                </script>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-offset-3 col-sm-2 control-label">汇总日期：</label>
                            <div class="col-sm-4">
                                <select type="text" class="form-control" id="weekAt" name="weekAt">
                                    <option value="0">星期日</option>
                                    <option value="1">星期一</option>
                                    <option value="2">星期二</option>
                                    <option value="3">星期三</option>
                                    <option value="4">星期四</option>
                                    <option value="5">星期五</option>
                                    <option value="6">星期六</option>
                                </select>
                                <script>
                                    $("#weekAt").val(${setting.weekAt?default(0)?c});
                                </script>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-offset-3 col-sm-2 control-label">汇总说明：</label>
                            <div class="col-sm-4" style="margin-top: 7px">
                                <p>积分管理将在每周设定的日期进行积分汇总，并且将汇总从年度开始日期至今（如果当前时间已经超过年度结束时间，则汇总到年度结束日期）以来所有人员的积分数据。</p>
                            </div>
                        </div>
                    </form>
                </div>
                <hr>
                <div class="box-footer" style="margin-top: 10px">
                    <button class="btn btn-default col-sm-1 col-sm-offset-5" style="margin-right: 10px" onclick="save()">确认
                    </button>
                    <button class="btn btn-default col-sm-1" style="margin-left: 10px" onclick="window.parent.closeModal()">取消
                    </button>
                </div>
            </div>
        </div>
    </@body>

    <script>
        function save() {
            var annualNum = $("#annualNum").val();
            var weekAt = $("#weekAt").val();
            $.post("/admin/points/summary/setting/save.json",
                {
                    annualNum: annualNum,
                    weekAt: weekAt
                },
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
</@html>