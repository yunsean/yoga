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
            .whitebg {
                background-color: white!important;
            }
        </style>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="系统设置" icon="icon-user">
            <@crumbItem href="#" name="定时任务" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "定时任务" />
                <@panelBody>
                    <@inlineForm class="margin-b-15">
                        <@formLabelGroup class="margin-r-15" label="名称">
                            <@inputText name="filter" value="${(param.filter)!}" placeholder="任务名称"/>
                        </@formLabelGroup>
                        <@inputSubmit text="搜索" icon="icon icon-search" class="btn btn-success"/>
                    </@inlineForm>
                    <@table>
                        <@thead>
                            <@tr>
                                <@th 20>名称</@th>
                                <@th 10>任务组</@th>
                                <@th 20>任务描述</@th>
                                <@th 10>状态</@th>
                                <@th 20>表达式</@th>
                                <@th 10>创建时间</@th>
                                <@th>操作</@th>
                            </@tr>
                        </@thead>
                    <tbody>
                        <#list tasks! as task>
                        <@tr>
                            <@td>${task.name!}</@td>
                            <@td>${task.group!}</@td>
                            <@td>${task.description!}</@td>
                            <@td>${task.status!}</@td>
                            <@td>${task.expression!}</@td>
                            <@td>${task.createTime!}</@td>
                            <@td>
                                <@shiro.hasPermission name="cfg_quartz.update" >
                                <#if task.status! == "NORMAL">
                                    <a href="javascript:void(0)" class="btn btn-sm btn-danger" onclick="doPause('${task.name!}', '${task.group!}')">
                                        <i class="icon icon-pause"></i>暂停
                                    </a>
                                <#else>
                                    <a href="javascript:void(0)" class="btn btn-sm btn-primary" onclick="doResume('${task.name!}', '${task.group!}')">
                                        <i class="icon icon-play"></i>执行
                                    </a>
                                </#if>
                                <a href="javascript:void(0)" class="btn btn-sm btn-info" onclick="doEdit('${task.name!}', '${task.group!}')">
                                    <i class="icon icon-edit"></i>设置
                                </a>
                                </@shiro.hasPermission>
                            </@td>
                        </@tr>
                        </#list>
                    </tbody>
                    </@table>
                </@panelBody>
            </@panel>
        </@bodyContent>

        <@modal title="任务编辑" showId="userAddButton" onOk="saveRole">
        <div class="form-group">
            <label class="col-sm-4 control-label">任务名称：<span class="point">*</span></label>
            <div class="col-sm-4">
                <@inputText name="name" readonly=true class="whitebg" />
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">任务组：<span class="point">*</span></label>
            <div class="col-sm-4">
                <@inputText name="group" readonly=true class="whitebg" />
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">定时方式：<span class="point">*</span></label>
            <div class="col-sm-4">
                <@inputText name="expression" />
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">任务描述：</label>
            <div class="col-sm-4">
                <@inputTextArea name="description" class="minHeight120px"/>
            </div>
        </div>
        </@modal>
    </@bodyFrame>
<script>
    function saveRole() {
        var id = $("#edit_id").val();
        var json = $("#add_form").serializeArray();
        $.post("/admin/setting/quartz/update.json",
                json,
                function (data) {
                    if (data.code < 0) {
                        alertShow("warning", data.message, 3000);
                    } else {
                        $("#add_modal").modal("hide");
                        window.location.reload();
                    }
                },
                "json"
        );
    }
    function doPause(name, group) {
        $("#add_form")[0].reset();
        $.post(
                "/admin/setting/quartz/pause.json?name=" + name + "&group=" + group,
                function (data) {
                    if (parseInt(data.code) < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        window.location.reload()
                    }
                }
        );
    }
    function doResume(name, group) {
        $("#add_form")[0].reset();
        $.post(
                "/admin/setting/quartz/resume.json?name=" + name + "&group=" + group,
                function (data) {
                    if (parseInt(data.code) < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        window.location.reload()
                    }
                }
        );
    }
    function doEdit(name, group) {
        $("#add_form")[0].reset();
        $.get(
                "/admin/setting/quartz/get.json?name=" + name + "&group=" + group,
                function (data) {
                    if (parseInt(data.code) < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#add_form input[name='name']").val(data.result.name);
                        $("#add_form input[name='group']").val(data.result.group);
                        $("#add_form input[name='expression']").val(data.result.expression);
                        $("#add_form textarea[name='description']").val(data.result.description);
                        $("#add_modal").modal("show");
                    }
                }
        );
    }
</script>
</@html>
