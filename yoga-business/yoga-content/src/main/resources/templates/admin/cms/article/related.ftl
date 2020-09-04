<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<#setting number_format="##0.##">
<@html>
<@head>
    <style>
        body {
            margin: 0;
            padding: 0;
        }
    </style>
</@head>
<@bodyFrame showFooter=false id="iframeDiv">
    <@inlineForm class="margin-b-15">
        <@inputHidden name="fieldCode" id="${param.fieldCode!}"/>
        <@formLabelGroup class="margin-r-15" label="关键字">
            <@inputText name="filter" value="${(param.filter)!}"/>
        </@formLabelGroup>
        <@inputSubmit text="搜索" icon="icon icon-search" class="btn btn-success"/>
    </@inlineForm>
    <@table>
        <@thead>
            <@tr>
                <@th 10>选定</@th>
                <@th 60>文章标题</@th>
                <@th 20 true>添加日期</@th>
                <@th 10 true>添加人</@th>
            </@tr>
        </@thead>
        <tbody>
        <#list articles! as article>
            <@tr>
                <@td class="tableCenter">
                    <input name="id" type="checkbox" value="${article._id!}"/>
                </@td>
                <@td>${article.title!}</@td>
                <@td true>${(article.date?string("yyyy-MM-dd"))!}</@td>
                <@td>${article.author!}</@td>
            </@tr>
        </#list>
        </tbody>
    </@table>
    <@panelPageFooter action="/admin/cmd/article/related" />
</@bodyFrame>
</@html>
