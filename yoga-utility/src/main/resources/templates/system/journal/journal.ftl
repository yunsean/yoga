<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<#import "/macro_common.ftl" as macroCommon>
<@html>
<@head includeDate=true>
<script type="text/javascript" src="<@macroCommon.resource/>/js/date.js" charset="UTF-8"></script>
<style>
    td {
        vertical-align: middle !important;;
    }
</style>
</@head>
<@bodyFrame>
    <@crumbRoot name="系统日志" icon="icon-user">
        <@crumbItem href="#" name="系统日志" />
    </@crumbRoot>
    <@bodyContent>
        <@panel>
            <@panelHeading "系统日志" />
            <@panelBody>
                <@inlineForm class="margin-b-15">
                    <@formLabelGroup class="margin-r-15" label="开始时间">
                        <@inputDateTime name="beginTime" value="${(param.beginTime?string('yyyy-MM-dd HH:mm'))!}"/>
                    </@formLabelGroup>
                    <@formLabelGroup class="margin-r-15" label="结束时间">
                        <@inputDateTime name="endTime" value="${(param.endTime?string('yyyy-MM-dd HH:mm'))!}"/>
                    </@formLabelGroup>
                    <@formLabelGroup class="margin-r-15" label="用户名">
                        <@inputText name="user" value="${param.user?if_exists}"/>
                    </@formLabelGroup>
                    <@formLabelGroup class="margin-r-15" label="模块">
                        <@inputText name="module" value="${param.module?if_exists}"/>
                    </@formLabelGroup>
                    <@formLabelGroup class="margin-r-15" label="行为">
                        <@inputText name="action" value="${param.action?if_exists}"/>
                    </@formLabelGroup>
                    <@formLabelGroup class="margin-r-15" label="方法">
                        <@inputText name="method" value="${param.method?if_exists}"/>
                    </@formLabelGroup>
                    <@inputSubmit text="搜索" icon="icon icon-search" class="btn btn-success"/>
                </@inlineForm>
                <@table class="table-hover">
                    <@thead>
                    <@tr>
                        <@th 20>操作时间</@th>
                        <@th 20>行为</@th>
                        <@th 20>模块</@th>
                        <@th 20>操作人</@th>
                    </@tr>
                    </@thead>
                    <tbody>
                        <#list journals?if_exists as journal>
                        <tr onclick="doDetail(${journal.id?c})">
                            <td><#if journal.time??>${journal.time?string('yyyy-MM-dd HH:mm:ss')}</#if></td>
                            <td>${journal.action?if_exists}</td>
                            <td>${journal.module?if_exists}</td>
                            <td>${journal.user?if_exists}</td>
                        </tr>
                        </#list>
                    </tbody>
                </@table>
            </@panelBody>
            <@panelPageFooter action="/system/journal/list" />
        </@panel>
    </@bodyContent>
</@bodyFrame>


    <@modal title="详情">
        <@formShow label="操作时间：" name="time"/>
        <@formShow label="操作人：" name="user"/>
        <@formShow label="操作人ID：" name="userId"/>
        <@formShow label="所属模块：" name="module"/>
        <@formShow label="入口方法：" name="method"/>
        <@formShow label="行为：" name="action"/>
        <@formShow label="详细内容：" name="detail"/>
    </div>
    </@modal>
<script>
    function doDetail(id) {
        $("#add_form")[0].reset();
        $.get(
                "/api/system/journal/detail?id=" + id,
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#add_form input[name='time']").val((new Date(data.result.time)).Format("yyyy-M-d h:m"));
                        $("#add_form input[name='user']").val(data.result.user);
                        $("#add_form input[name='userId']").val(data.result.userId);
                        $("#add_form input[name='module']").val(data.result.module);
                        $("#add_form input[name='method']").val(data.result.method);
                        $("#add_form input[name='action']").val(data.result.action);
                        $("#add_form input[name='detail']").val(data.result.detail);
                        $("#add_modal").modal("show");
                    }
                }
        );
    }
</script>
</@html>
