<#import "/macro_common.ftl" as macroCommon>
<@macroCommon.html>
<body>
<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="/tenant/templates"><i class="fa fa-dashboard"></i>模板管理</a></li>
            <li>模块设置</li>
            <span class="pull-right">
                <button class="btn btn-primary btn-sm" onclick="history.back(-1);">返回</button>
            </span>
        </ol>
    </div>
    <div class="row content-bottom">
        <div class="col-sm-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="fa fa-comments-o"></i>
                    租户模板设置
                </div>
                <div class="panel-body">
                    <table id="" class="table table-bordered ">
                        <thead>
                        <tr>
                            <th style="width:10">版块</th>
                            <th style="width:15%">模块</th>
                            <th style="width:30%">路径</th>
                            <th style="width:45%">备注</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list modules as menu>
                            <tr style="background-color:#eaeaea">
                                <td><b style="font-size:14px">${menu.name?if_exists}</b></td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
                                <#list menu.children as child>
                                <tr>
                                    <td></td>
                                    <td>
                                        <label>
                                            <input name="${child.code?if_exists}" type="checkbox"
                                                   <#if child.checked>checked</#if>
                                                   value="">
                                            &nbsp;&nbsp;${child.name?if_exists}
                                        </label>
                                    </td>
                                    <td>${child.url?if_exists}</td>
                                    <td>${child.remark?if_exists}</td>
                                </tr>
                                </#list>
                            </#list>
                        </tbody>
                    </table>
                </div>
                <div class="box-footer">
                    <div class="col-sm-5 col-sm-offset-5">
                        <button class="btn btn-default" onclick="saveModule()">保存</button>
                        <button class="btn btn-default" onclick="javascript:history.back(-1)">取消</button>
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

<script>
    function saveModule() {
        var modules = [];
        var total = $("input[type='checkbox']").size();
        var selected = $("select").size();
        for (var i = 0; i < total; i++) {
            if ($("input[type='checkbox']").eq(i).prop("checked")) {
                modules.push($("input[type='checkbox']").eq(i).attr("name"));
            }
        }
        $.post(
                "/api/tenant/template/module/set",
                $.param({modules: modules, templateId: ${templateId?c}}, true),
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        window.location.href = "/tenant/templates";
                    }
                },
                "json"
        );
    }
</script>
</body>
</@macroCommon.html>