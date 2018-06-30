<#macro table id="" class="table-bordered table-striped table-compact">
<table class="table ${class}" <#if id != "">id="${id}"</#if>>
    <#nested>
</table>
</#macro>

<#macro thead id="">
<thead <#if id != "">id="${id}"</#if>>
    <#nested>
</thead>
</#macro>

<#macro tr id="" class="">
<tr <#if id != "">id="${id}"</#if> <#if class != "">class="${class}"</#if>>
    <#nested>
</tr>
</#macro>

<#macro th width=-1 center=false id="" class="">
<#if center><#local class="${class} tableCenter"></#if>
<th <#if width != -1>style="width:${width}%"</#if> <#if id != "">id="${id}"</#if> <#if class != "">class="${class}"</#if>>
    <#nested>
</th>
</#macro>

<#macro td center=false id="" class="">
<#if center><#local class="${class} tableCenter"></#if>
<td <#if id != "">id="${id}"</#if> <#if class != "">class="${class}"</#if>>
    <#nested>
</td>
</#macro>

