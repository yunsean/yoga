<#macro footer>
<center><strong><@tenantTags tag="footer"/></strong></center>
</#macro>
<#macro platform>
    <@tenantTags tag="platform"/>
</#macro>
<#macro resource>
    <@tenantTags tag="resource"/>
</#macro>
<#macro roleAlias>
    <@tenantTags tag="role"/>
</#macro>
<#macro deptAlias>
    <@tenantTags tag="dept"/>
</#macro>
<#macro dutyAlias>
    <@tenantTags tag="duty"/>
</#macro>

<#macro html includeDate=false>
<!DOCTYPE html>
<html>
    <#setting url_escaping_charset='utf-8'>
<head>
    <title><@common.platform /></title>
    <meta charset="utf-8">
    <!--[if lt IE 9]>
    <script src="lib/ieonly/html5shiv.js"></script>
    <script src="lib/ieonly/respond.js"></script>
    <script src="lib/ieonly/excanvas.js"></script>
    <![endif]-->
    <script src="<@resource/>/scripts/alerts.js"></script>
    <link rel="stylesheet" type="text/css" href="<@resource/>/plugins/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="<@resource/>/css/SPSStyle.css">
    <link rel="stylesheet" type="text/css" href="<@resource/>/zui/zui.min.css">
    <script src="<@resource/>/zui/jquery.js" charset="UTF-8"></script>
    <script src="<@resource/>/zui/zui.min.js" charset="UTF-8"></script>
    <#--<script src="<@resource/>/jqueryform/jquery-form.js" charset="UTF-8"></script>-->
    <link href="<@resource/>/datatable/zui.datatable.min.css" rel="stylesheet" media="screen">
    <script src="<@resource/>/datatable/zui.datatable.min.js" charset="UTF-8"></script>

    <#if includeDate>
    <link href="<@resource/>/plugins/datetimepicker/jquery.datetimepicker.css" rel="stylesheet" media="screen">
    <script src="<@resource/>/plugins/datetimepicker/jquery.datetimepicker.js" charset="UTF-8"></script>
    </#if>
    <style>
        .table>tbody>tr>td{
            vertical-align: middle;
        }
    </style>
    <link rel="icon" type="image/x-icon" href="<@resource/>/img/favicon.ico">
</head>
    <#nested>
</html>
</#macro>
