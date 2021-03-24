<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<#setting number_format="##0.##">
<@html>
    <@head includeDate=true>
        <style>
            .minHeight120px {
                min-height: 120px;
            }
            td {
                vertical-align: middle!important;
            }
        </style>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="系统设置" icon="icon-user">
            <@crumbItem href="#" name="配置管理" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "配置项列表" />
                <@panelBody>
                    <@inlineForm class="margin-b-15">
                        <@formLabelGroup class="margin-r-15" label="配置名称">
                            <@inputText name="filter" value="${(param.filter)!}"/>
                        </@formLabelGroup>
                        <@inputSubmit text="搜索" icon="icon icon-search" class="btn btn-success"/>
                        <@rightAction>
                            <@shiro.hasPermission name="sys_config.update">
                                <@inputButton text="保存设置" icon="icon-save" class="btn btn-primary" onclick="doSave();" />
                            </@shiro.hasPermission>
                        </@rightAction>
                    </@inlineForm>
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
                                    <#elseif setting.setting.type?? && (setting.setting.type.simpleName = 'boolean' || setting.setting.type.simpleName = 'Boolean')>
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
                                               value="${setting.setting.value?if_exists}"
                                               placeholder="${setting.setting.placeHolder?if_exists}">
                                    <#elseif setting.setting.type?? && setting.setting.type.simpleName = 'LocalTime'>
                                        <#assign did=(id=="")?string("${randomInputId()}", id)>
                                        <input class="setting-item form-control" type="text" id="${did}"
                                               key="${setting.setting.key?if_exists}"
                                               module="${setting.setting.module?if_exists}"
                                               value="${setting.setting.value?if_exists}"
                                               placeholder="${setting.setting.placeHolder?if_exists}"
                                               autocomplete="off">
                                        <script type="text/javascript">
                                            $(document).ready(function () {
                                                $("#${did}").datetimepicker({
                                                    lang: 'ch',
                                                    step: 5,
                                                    timepicker: true,
                                                    datepicker: false,
                                                    format: 'H:i:00',
                                                    formatTime: 'H:i:00',
                                                    allowBlank: true
                                                });
                                            });
                                        </script>
                                    <#elseif setting.setting.type?? && setting.setting.type.simpleName = 'LocalDateTime'>
                                        <#assign did=(id=="")?string("${randomInputId()}", id)>
                                        <input class="setting-item form-control" type="text" id="${did}"
                                               key="${setting.setting.key?if_exists}"
                                               module="${setting.setting.module?if_exists}"
                                               value="${setting.setting.value?if_exists}"
                                               placeholder="${setting.setting.placeHolder?if_exists}"
                                               autocomplete="off">
                                        <script type="text/javascript">
                                            $(document).ready(function () {
                                                $("#${did}").datetimepicker({
                                                    lang: 'ch',
                                                    step: 30,
                                                    timepicker: true,
                                                    datepicker: true,
                                                    format: 'Y-m-d H:i:00',
                                                    formatTime: 'H:i:00',
                                                    formatDate: 'Y-m-d',
                                                    allowBlank: true
                                                });
                                            });
                                        </script>
                                    <#elseif setting.setting.type?? && setting.setting.type.simpleName = 'LocalDate'>
                                        <#assign did=(id=="")?string("${randomInputId()}", id)>
                                        <input class="setting-item form-control" type="text" id="${did}"
                                               key="${setting.setting.key?if_exists}"
                                               module="${setting.setting.module?if_exists}"
                                               value="${setting.setting.value?if_exists}"
                                               placeholder="${setting.setting.placeHolder?if_exists}"
                                               autocomplete="off">
                                        <script type="text/javascript">
                                            $(document).ready(function () {
                                                $("#${did}").datetimepicker({
                                                    lang: 'ch',
                                                    timepicker: false,
                                                    format: 'Y-m-d',
                                                    formatDate: 'Y-m-d',
                                                    allowBlank: true
                                                });
                                            });
                                        </script>
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
                                    <#elseif setting.values?? >
                                        <select class="setting-item form-control" type="number"
                                                id="setting_${setting_index}"
                                                key="${setting.setting.key?if_exists}"
                                                module="${setting.setting.module?if_exists}"
                                                value="${setting.setting.value?if_exists}">
                                            <#list setting.values as val>
                                                <option value="${val.getId()}">${val.getName()}</option>
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
                                               value="${setting.setting.value?if_exists}"
                                               placeholder="${setting.setting.placeHolder?if_exists}">
                                    </#if>
                                </td>
                                <td class="tableCenter">
                                    <@shiro.hasPermission name="sys_config.update">
                                        <#if setting.setting.url?? && setting.setting.url?length gt 0>
                                            <#if setting.setting.showPage?if_exists>
                                                <a class="btn btn-sm btn-info caseReport"
                                                   href="${setting.setting.url?if_exists}">
                                                    <i class="icon icon-magic"></i>配置
                                                </a>
                                            <#else>
                                                <button class="btn btn-sm btn-info caseReport"
                                                        key="${setting.setting.key?if_exists}"
                                                        url="${setting.setting.url?if_exists}"
                                                        module="${setting.setting.module?if_exists}"
                                                        onclick="showModal(this)"><i
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
                </@panelBody>
                <@panelPageFooter action="/admin/system/setting/list" />
            </@panel>
        </@bodyContent>
    </@bodyFrame>

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
        $.ajax({
            type: "POST",
            url: '/admin/system/setting/save.json',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            async: false,
            data: JSON.stringify(json),
            success: function (data) {
                if (data.code < 0) {
                    alertShow("danger", data.message, 3000);
                } else {
                    alertShow("success", "保存设置成功！", 3000);
                }
            }
        });
    }
    function autoIframeHeight() {
        setTimeout(function () {
            $("#autoIframe").height($("#autoIframe").contents().find("#iframeDiv").height());
            var $this = $("#AssociatedModal");
            var $modal_dialog = $this.find('.modal-dialog');
            var m_top = ($(window).height() - $modal_dialog.height()) / 2;
            $modal_dialog.css({'margin': m_top + 'px auto', "top": "0px"});
        }, 200);
    }
    function showModal(data) {
        $("#AssociatedModal").modal();
        var key = $(data).attr("key");
        var module = $(data).attr("module");
        $("#autoIframe").attr("src", $(data).attr("url") + "?key=" + key + "&module=" + module)
    }
</script>

<div class="modal fade" id="AssociatedModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
    function closeModal() {
        $("#AssociatedModal").modal('hide')
        window.location.reload()
    }
</script>
</@html>