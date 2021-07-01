<#macro footer>
<center><strong><@tenantTags tag="footer"/></strong></center>
</#macro>
<#macro platform>
    <@tenantTags tag="platform"/>
</#macro>
<#macro resource>
    <@tenantTags tag="resource"/>
</#macro>

<#macro html>
<!DOCTYPE html>
<html>
    <#setting url_escaping_charset='utf-8'>
    <#nested>
</html>
</#macro>

<#macro head includeDate=false includeUploader=false includeZTree=false includeECharts=false includeUEditor=false includeColor=false>
<head>
    <title><@platform /></title>
    <meta charset="utf-8">
    <script src="<@resource/>/jquery/jquery-3.3.1.js" charset="UTF-8"></script>
    <script src="<@resource/>/zui/js/zui.min.js" charset="UTF-8"></script>
    <link rel="shortcut icon" href="<@tenantTags tag="icon"/>" />
    <link rel="stylesheet" type="text/css" href="<@resource/>/css/SPSStyle.css">
    <link rel="stylesheet" type="text/css" href="<@resource/>/zui/css/zui.min.css">
    <#if includeUEditor>
        <script type="text/javascript" charset="utf-8" src="<@resource/>/ueditor/ueditor.config.js"></script>
        <script type="text/javascript" charset="utf-8" src="<@resource/>/ueditor/ueditor.all.min.js"></script>
        <script type="text/javascript" charset="utf-8" src="<@resource/>/ueditor/lang/zh-cn/zh-cn.js"></script>
    </#if>
    <#if includeECharts>
        <script src="<@resource/>/echarts/echarts.min.js"></script>
    </#if>
    <#if includeDate>
        <link href="<@resource/>/jquery/datetimepicker/datetimepicker.css" rel="stylesheet" media="screen">
        <script src="<@resource/>/jquery/datetimepicker/datetimepicker.js" charset="UTF-8"></script>
    </#if>
    <#if includeUploader>
        <link href="<@resource/>/zui/lib/uploader/zui.uploader.css" rel="stylesheet">
        <script src="<@resource/>/zui/lib/uploader/zui.uploader.js"></script>
    </#if>
    <#if includeZTree>
        <script src="<@resource/>/zTree_v3/js/jquery.ztree.all.js"></script>
        <script src="<@resource/>/zTree_v3/js/jquery.ztree.core.min.js"></script>
        <script src="<@resource/>/zTree_v3/js/jquery.ztree.excheck.min.js"></script>
        <script src="<@resource/>/zTree_v3/js/jquery.ztree.exedit.min.js"></script>
        <script src="<@resource/>/zTree_v3/js/jquery.ztree.exhide.min.js"></script>
        <link href="<@resource/>/zTree_v3/css/zTreeStyle/zTreeStyle.css" rel="stylesheet"></script>
    </#if>
    <#if includeColor>
        <script src="<@resource/>/js/jscolor.js" charset="UTF-8"></script>
    </#if>
    <script src="<@resource/>/js/alerts.js"></script>
    <script src="<@resource/>/js/common.js"></script>
    <script src="<@resource/>/js/date.js"></script>
    <#nested>
</head>
</#macro>

<#macro body>
<body>
    <#nested>
</body>
</#macro>

<#macro bodyContainer showFooter=true id="">
<div class="container-fluid <#if showFooter>content-bottom</#if>" <#if id != "">id="${id}"</#if>>
    <#nested>
</div>
</#macro>

<#macro bodyFooter>
<footer class="main-footer">
    <@footer />
</footer>
</#macro>

<#macro bodyFrame showFooter=true id="">
    <@body>
        <@bodyContainer showFooter id>
            <#nested>
        </@bodyContainer>
    <#if showFooter>
        <@bodyFooter>
        </@bodyFooter>
    </#if>
    </@body>
</#macro>

<#macro crumbRoot name href="#" icon="">
<div class="row">
    <ol class="breadcrumb">
        <li>
            <a href="${href}">
                <i class="icon ${icon}"></i>
                ${name}
            </a>
        </li>
        <#nested>
    </ol>
</div>
</#macro>

<#macro crumbItem name href="window.history.back()" backLevel=0>
<li class="active" <#if backLevel &gt; 0>onclick="history.back(-${backLevel});"</#if>><a href="${href}">${name}</a></li>
</#macro>

<#macro bodyContent>
<div class="row">
    <div class="col-sm-12">
        <#nested>
    </div>
</div>
</#macro>

<#macro panel class="panel-primary" id="">
<div class="panel ${class}" id="${id}">
    <#nested>
</div>
</#macro>

<#macro panelHeading name icon="">
<div class="panel-heading">
    <i class="icon ${icon}"></i>
    ${name}
</div>
</#macro>

<#macro panelBody class="">
<div class="panel-body ${class}">
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

<#macro backButton refresh=false>
    <span class="pull-right">
        <#if refresh>
        <button class="btn btn-success btn-sm" onclick="window.location.reload();" style="margin-right: 15px"><i class="icon icon-refresh"></i>&nbsp;刷新</button>
        </#if>
        <button class="btn btn-primary btn-sm" onclick="history.back();"><i class="icon icon-arrow-left"></i>&nbsp;返回</button>
    </span>
</#macro>