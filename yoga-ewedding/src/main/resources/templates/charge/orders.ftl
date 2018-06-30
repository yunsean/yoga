<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<@html>
<@head includeDate=true>
<style>
    td {
        vertical-align: middle !important;;
    }
</style>
</@head>
<@bodyFrame>
    <@crumbRoot name="充值记录" icon="icon-user">
        <@crumbItem href="#" name="充值记录" />
    </@crumbRoot>
    <@bodyContent>
        <@panel>
            <@panelHeading "充值记录" />
            <@panelBody>
                <@inlineForm class="margin-b-15">
                    <@formLabelGroup class="margin-r-15" label="用户">
                        <@inputText name="user" value="${param.user?if_exists}"/>
                    </@formLabelGroup>
                    <@formLabelGroup class="margin-r-15" label="充值类型">
                        <@inputEnum name="type" enums=types value="${param.type?if_exists}" blank="全部"/>
                    </@formLabelGroup>
                    <@formLabelGroup class="margin-r-15" label="订单状态">
                        <@inputEnum name="status" enums=status value="${param.status?if_exists}" blank="全部"/>
                    </@formLabelGroup>
                    <@formLabelGroup class="margin-r-15" label="开始日期">
                        <@inputDate name="begin" value="${(param.begin?string('yyyy-MM-dd'))!''}"/>
                    </@formLabelGroup>
                    <@formLabelGroup class="margin-r-15" label="结束日期">
                        <@inputDate name="end" value="${(param.end?string('yyyy-MM-dd'))!''}"/>
                    </@formLabelGroup>
                    <@inputSubmit text="筛选" icon="icon icon-search" class="btn btn-success"/>
                </@inlineForm>
                <@table>
                    <@thead>
                    <@tr>
                        <@th 12>用户</@th>
                        <@th 12>充值日期</@th>
                        <@th 12>充值方式</@th>
                        <@th 12>金额</@th>
                        <@th 12>订单号</@th>
                        <@th 12>状态</@th>
                        <@th 12>充值到</@th>
                    </@tr>
                    </@thead>
                    <tbody>
                        <#list orders?if_exists as recharge>
                        <@tr>
                            <td><#if recharge.fullname?? && recharge.fullname?length gt 0>${recharge.fullname?if_exists}<#else>${recharge.username?if_exists}</#if></td>
                            <td>${recharge.createTime?string("yyyy-MM-dd")}</td>
                            <td><#if recharge.rechargeType??>${recharge.rechargeType.getName()}</#if></td>
                            <td>￥${recharge.amount?default(0)?string("0.00")}</td>
                            <td>${recharge.orderNo?if_exists}</td>
                            <td>${recharge.status.getName()}</td>
                            <td>${recharge.expireTo?string("yyyy-MM-dd")}</td>
                        </@tr>
                        </#list>
                    </tbody>
                </@table>
            </@panelBody>
            <@panelPageFooter action="/ewedding/charge/recharge" />
        </@panel>
    </@bodyContent>
</@bodyFrame>
</@html>
