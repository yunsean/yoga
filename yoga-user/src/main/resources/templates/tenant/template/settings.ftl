<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<!DOCTYPE html>
<html>
<@macroCommon.html>
<link href="/css/zui.min.css" rel="stylesheet">
<body>
<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="/tenant/templates"><i class="icon icon-user"></i> 模板管理</a></li>
            <li class="active">模板预设置</li>
            <span class="pull-right">
                <button class="btn btn-primary btn-sm" onclick="history.back();">返回</button>
            </span>
        </ol>
    </div>
    <div class="row content-bottom">
        <div class="col-sm-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="icon icon-user"></i>
                    模板预设置
                </div>
                <div class="panel-body">
                    <div class="tableToolContainer" style="margin-bottom:15px">
                        <form action="/tenant/template/settings" class="form-inline">
                            <input type="hidden" name="tenantId" value="${tenantId?c}">
                            <div class="form-group">
                                <label>&nbsp;&nbsp;配置名称：&nbsp;</label>
                                <input type="text" class="form-control" name="filter" value="${filter?if_exists}">
                            </div>
                            <button type="submit" class="btn btn-success"><i class="icon icon-user"></i>搜索
                            </button>
                            <div class="form-group" style="float: right">
                                <@shiro.hasPermission name="gbl_settable.update">
                                    <a class="btn btn-info" onclick="javascript:window.location.reload();">
                                        <i class="icon icon-refresh"></i> 刷新
                                    </a>
                                    <a class="btn btn-primary" onclick="doSave()">
                                        <i class="icon icon-save"></i> 保存设置
                                    </a>
                                </@shiro.hasPermission>
                            </div>
                        </form>
                    </div>
                    <table id="" class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th style="width:35%">配置名称</th>
                            <th style="width:40%">配置内容</th>
                            <th style="width:25%" class="tableCenter">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list settings as setting>
                            <tr>
                                <td>${setting.setting.name?if_exists}</td>
                                <td>
                                    <#if setting.setting.url?? && setting.setting.url?length gt 0>
                                    ${setting.setting.showValue?if_exists}
                                    <#elseif setting.setting.type?? && setting.setting.type.simpleName = 'boolean'>
                                        <div>
                                            <input class="setting-radio" type="radio"
                                                   name="setting_${setting_index}"
                                                   key="${setting.setting.key?if_exists}"
                                                   module="${setting.setting.module?if_exists}"
                                                   <#if setting.setting.value?if_exists == 'true'>checked</#if>
                                                   value="true">&nbsp;&nbsp;是
                                            &nbsp;&nbsp;&nbsp;&nbsp;
                                            <input class="setting-radio" type="radio"
                                                   name="setting_${setting_index}"
                                                   key="${setting.setting.key?if_exists}"
                                                   module="${setting.setting.module?if_exists}"
                                                   <#if setting.setting.value?if_exists != 'true'>checked</#if>
                                                   value="false">&nbsp;&nbsp;否
                                        </div>
                                    <#elseif setting.setting.type?? && (setting.setting.type.simpleName = 'Long' || setting.setting.type.simpleName = 'Integer' || setting.setting.type.simpleName = 'long' || setting.setting.type.simpleName = 'int')>
                                        <input class="setting-item form-control" type="number"
                                               key="${setting.setting.key?if_exists}"
                                               module="${setting.setting.module?if_exists}"
                                               value="${setting.setting.value?if_exists}">
                                    <#elseif setting.enums?? >
                                        <select class="setting-item form-control" type="number"
                                                id="setting_${setting_index}"
                                                key="${setting.setting.key?if_exists}"
                                                module="${setting.setting.module?if_exists}"
                                                value="${setting.setting.value?if_exists}">
                                            <#list setting.enums as val>
                                                <option value="${val}">${val.getName()}</option>
                                            </#list>
                                        </select>
                                        <#if setting.setting.value??>
                                            <script>
                                                $("#setting_${setting_index}").val('${setting.setting.value?if_exists}');
                                            </script>
                                        </#if>
                                    <#else>
                                        <input class="setting-item form-control" type="text"
                                               key="${setting.setting.key?if_exists}"
                                               module="${setting.setting.module?if_exists}"
                                               value="${setting.setting.value?if_exists}">
                                    </#if>
                                </td>
                                <td class="tableCenter">
                                    <@shiro.hasPermission name="gbl_settable.update">
                                        <#if setting.setting.url?? && setting.setting.url?length gt 0>
                                            <#if setting.setting.showPage?if_exists>
                                                <a class="btn btn-info caseReport"
                                                   href="${setting.setting.url?if_exists}">
                                                    <i class="icon icon-magic"></i>配置
                                                </a>
                                            <#else>
                                                <button class="btn btn-info caseReport"
                                                        key="${setting.setting.key?if_exists}"
                                                        url="${setting.setting.url?if_exists}"
                                                        module="${setting.setting.module?if_exists}"
                                                        data-toggle="modal" data-target="#AssociatedModal"
                                                        onclick="autoReturnIframe(this)"><i
                                                        class="icon icon-pencil"></i>
                                                    配置
                                                </button>
                                            </#if>
                                        </#if>
                                    </@shiro.hasPermission>
                                </td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>
                <div class="box-footer" style="text-align:center">
                    <@html.paging page=page param="" action="/tenant/settings?tenantId=${tenantId?c}"/>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
<input type="hidden" id="thisPageIndex" value="${page.pageIndex?default(0)?c}">
<footer class="main-footer">
    <@common.footer />
</footer>
</div>
</body>

<div class="modal fade" id="AssociatedModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" style="width:70%;">
        <div class="form-horizontal" id="lawsAddForm" name="lawsAddForm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="myModalLabel">
                        配置管理
                    </h4>
                </div>
                <iframe id="autoIframe" onload="autoIframeHeight()" frameborder="0" width="100%" src=""/>
                </iframe>
            </div>
        </div>
    </div>
</div>

<script>
    function doSave() {
        var json = [];
        var inputs = $(".setting-item");
        $.each(inputs, function () {
            var item = {};
            item["module"] = $(this).attr("module");
            item["key"] = $(this).attr("key");
            item["value"] = $(this).val();
            json.push(item);
        });
        var radios = $(".setting-radio:checked");
        $.each(radios, function () {
            var item = {};
            item["module"] = $(this).attr("module");
            item["key"] = $(this).attr("key");
            item["value"] = $(this).val();
            json.push(item);
        });
        if (json.length < 1) return;
        var root = {};
        root["tenantId"] = ${tenantId?default(0)?c};
        root["settings"] = json;
        $.ajax({
            type: "POST",
            url: '/api/tenant/settings/save',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            async: false,
            data: JSON.stringify(root),
            success: function (data) {
                if (data.code < 0) {
                    alertShow("danger", data.message, 3000);
                } else {
                    alertShow("success", "保存设置成功！", 3000);
                    window.location.reload();
                }
            }
        });
    }
    function autoIframeHeight() {
        setTimeout(function () {
            $("#autoIframe").height($("#autoIframe").contents().find("#iframeDiv").height());
        }, 100);
    }
    function autoReturnIframe(data) {
        var key = $(data).attr("key");
        var module = $(data).attr("module");
        $("#autoIframe").attr("src", $(data).attr("url") + "&key=" + key + "&module=" + module)
    }
    function closeModal() {
        $("#AssociatedModal").modal('hide')
    }
</script>
</@macroCommon.html>