<#function addContextPath action>
    <#if !action??>
        <#return "">
    <#else>
        <#return action >
    </#if>
</#function>
<#macro formParams ext>${ext}</#macro>

<#--功能:一般form表单-->
<#macro form id="" class="" action="" isFile=false>
<#local action = "${addContextPath(action)}" >
<#local id=(id=="")?string("${randomInputId()}", id)>
<form role="form" id="${id}" class="${class}" action="${(!action??)?string("", action)}" method="post" <#if isFile>enctype="multipart/form-data"</#if>>
    <#nested>
</form>
<script>
    $(function () {
        var url = document.location.toString();
        var arrUrl = url.split("?");
        var baseUrl = arrUrl[0];
        var action = $("#${id!}").attr("action");
        if (action == "" || action == "#") {
            $("#${id!}").attr("action", baseUrl);
        }
    })
</script>
</#macro>

<#--功能:搜索表单宏 内联表单-->
<#macro inlineForm id="" class="" action="" >
<@form id "form-inline ${class}" action false>
    <#nested>
</@form>
</#macro>

<#--功能:水平表单布局-->
<#macro horizontalForm id="" action="" isFile = false>
<@form id "form-horizontal" action isFile>
    <#nested>
</@form>
</#macro>

<#--form表单块  标签和控件放入其中 自动配置合理间距-->
<#--此为表单内置组件  单独使用无效-->
<#macro formGroup class="">
<div class="form-group ${class}">
    <#nested>
</div>
</#macro>

<#--功能:表单label标签 表单宏 配合formgroup使用-->
<#macro formLabel class="" for="" force=false>
<label class="control-label formLabelBold ${class}" for="${for}">
    <#if force?if_exists>
        <font color="red" >* </font>
    </#if>
    <#nested>
</label>
</#macro>

<#macro formLabelGroup class="" label="">
<div class="form-group ${class}">
    <#if label != ""><@formLabel class="margin-r-5">${label}</@formLabel></#if>
    <#nested>
</div>
</#macro>

<#--功能:编辑域-->
<#macro formInputArea class="" icon="">
<div class="${class}">
    <#if icon!="">
        <div class="form-control-area">
            <#nested>
            <span class="input-group-addon ">
                    <i class="icon-append ${icon}"></i>
                </span>
        </div>
    <#else>
        <#nested>
    </#if>
</div>
</#macro>



<#--功能:静态文字块-->
<#macro p id="" class="" name="" ext="">
<p <#if id != "">id="${id}"</#if> <#if name!="">name="${name}"</#if> <@formParams ext/> class="form-control-static ${class}">
    <#nested>
</p>
</#macro>

<#--功能:表单分块-->
<#macro formFieldset class="" available=true>
<fieldset class="${class}" ${available?string("true", "false")}>
    <#nested>
</fieldset>
</#macro>

<#--功能:表单区块title-->
<#macro formlegend class="">
<legend class="${class}">
    <#nested>
</legend>
</#macro>

<#--功能:表单尾,放置按钮一般居右-->
<#macro formFooter class="">
<div class="form-actions ${class}">
    <#nested>
</div>
</#macro>

<#macro rightAction>
<div style="float: right">
    <#nested>
</div>
</#macro>
