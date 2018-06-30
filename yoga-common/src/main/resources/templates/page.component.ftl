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

<#macro html>
<!DOCTYPE html>
<html>
    <#setting url_escaping_charset='utf-8'>
    <#nested>
</html>
</#macro>

<#macro head includeDate=false>
<head>
    <title><@common.platform /></title>
    <meta charset="utf-8">
    <script src="<@resource/>/scripts/alerts.js"></script>
    <link rel="stylesheet" type="text/css" href="<@resource/>/plugins/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="<@resource/>/css/SPSStyle.css">
    <link rel="stylesheet" type="text/css" href="<@resource/>/zui/zui.min.css">
    <script src="<@resource/>/zui/jquery.js" charset="UTF-8"></script>
    <script src="<@resource/>/zui/zui.min.js" charset="UTF-8"></script>
    <#if includeDate>
        <link href="<@resource/>/plugins/datetimepicker/jquery.datetimepicker.css" rel="stylesheet" media="screen">
        <script src="<@resource/>/plugins/datetimepicker/jquery.datetimepicker.js" charset="UTF-8"></script>
    </#if>
    <#nested>
</head>
</#macro>

<#macro body>
<body>
    <#nested>
</body>
</#macro>

<#macro bodyContainer>
<div class="container-fluid">
    <#nested>
</div>
</#macro>

<#macro bodyFooter>
<footer class="main-footer">
    <@common.footer />
</footer>
</#macro>

<#macro bodyFrame>
    <@body>
        <@bodyContainer>
            <#nested>
        </@bodyContainer>
        <@bodyFooter>
        </@bodyFooter>
    </@body>
</#macro>

<#macro crumbRoot name href="#" icon="">
<div class="row">
    <ol class="breadcrumb">
        <li><a href="${href}"><i class="icon ${icon}"></i>${name}</a></li>
        <#nested>
    </ol>
</div>
</#macro>

<#macro crumbItem name href="window.history.back()">
<li class="active"><a href="${href}">${name}</a></li>
</#macro>

<#macro bodyContent>
<div class="row content-bottom">
    <div class="col-sm-12">
        <#nested>
    </div>
</div>
</#macro>

<#macro panel class="panel-primary">
<div class="panel ${class}">
    <#nested>
</div>
</#macro>

<#macro panelHeading name icon="">
<div class="panel-heading">
    <i class="icon ${icon}"></i>
    ${name}
</div>
</#macro>

<#macro panelBody>
<div class="panel-body">
    <#nested>
</div>
</#macro>

<#macro panelFooter>
<div class="box-footer" style="text-align:center">
    <#nested>
</div>
</#macro>

<#macro panelPageFooter action>
<@panelFooter>
    <@paging page=page param=param action="${action}"/>
</@panelFooter>
</#macro>