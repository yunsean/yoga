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
    <@crumbRoot name="收费方式" icon="icon-user">
        <@crumbItem href="#" name="收费方式" />
    </@crumbRoot>
    <@bodyContent>
        <@panel>
            <@panelHeading "收费方式" />
            <@panelBody>
                <@table>
                    <@thead>
                    <@tr>
                        <@th 12>顾问类型</@th>
                        <@th 12>原价（元/月）</@th>
                        <@th 12>按月付费</@th>
                        <@th 12>按季度付费</@th>
                        <@th 12>按半年付费</@th>
                        <@th 12>按年付费</@th>
                        <@th center=true>操作</@th>
                    </@tr>
                    </@thead>
                    <tbody>
                        <#list charges?if_exists as charge>
                        <@tr>
                            <td><#if charge.id?exists && deptMap?exists && deptMap[charge.id?c]?exists>
                                    ${deptMap[charge.id?c].name!}
                                </#if>
                            </td>
                            <td>${charge.originalFee?default(-1)?string("0.##")}</td>
                            <td>${charge.monthlyFee?default(-1)?string("0.##")}</td>
                            <td>${charge.quarterlyFee?default(-1)?string("0.##")}</td>
                            <td>${charge.halfyearFee?default(-1)?string("0.##")}</td>
                            <td>${charge.yearlyFee?default(-1)?string("0.##")}</td>
                            <td class="tableCenter">
                                <@shiro.hasPermission name="ew_charge.update" >
                                <a href="#" class="btn btn-primary" onclick="doDetail(${charge.id?c})">
                                    <i class="icon icon-edit"></i>修改
                                </a>
                                </@shiro.hasPermission>
                            </td>
                        </@tr>
                        </#list>
                    </tbody>
                </@table>
            </@panelBody>
        </@panel>
    </@bodyContent>
</@bodyFrame>


    <@modal title="修改价格" onOk="doSave">
        <@inputHidden name="typeId"/>
        <@formText label="原价：" name="originalFee" postfix="元"/>
        <@formText label="按月：" name="monthlyFee" postfix="元"/>
        <@formText label="按季度：" name="quarterlyFee" postfix="元"/>
        <@formText label="按半年：" name="halfyearFee" postfix="元"/>
        <@formText label="按年：" name="yearlyFee" postfix="元"/>
    </div>
    </@modal>
<script>
    function doDetail(id) {
        $("#add_form")[0].reset();
        $.get(
                "/api/ewedding/charge/get?typeId=" + id,
                function (data) {
                    if (data.code < 0) {
                        $("#add_form input[name='typeId']").val(id);
                        $("#add_form input[name='originalFee']").val("0");
                        $("#add_form input[name='monthlyFee']").val("0");
                        $("#add_form input[name='quarterlyFee']").val("0");
                        $("#add_form input[name='halfyearFee']").val("0");
                        $("#add_form input[name='yearlyFee']").val("0");
                        $("#add_modal").modal("show");
                    } else {
                        $("#add_form input[name='typeId']").val(id);
                        $("#add_form input[name='originalFee']").val(data.result.originalFee);
                        $("#add_form input[name='monthlyFee']").val(data.result.monthlyFee);
                        $("#add_form input[name='quarterlyFee']").val(data.result.quarterlyFee);
                        $("#add_form input[name='halfyearFee']").val(data.result.halfyearFee);
                        $("#add_form input[name='yearlyFee']").val(data.result.yearlyFee);
                        $("#add_modal").modal("show");
                    }
                }
        );
    }
    function doSave() {
        var json = $("#add_form").serializeArray();
        $.post("/api/ewedding/charge/set",
                json,
                function (data) {
                    if (data.code < 0) {
                        alertShow("warning", data.message, 3000);
                        return;
                    } else {
                        window.location.reload();
                    }
                },
                "json"
        );
    }
</script>
</@html>
