<#macro table id="" class="table-bordered table-striped table-compact" responsive=true>
    <#if responsive>
        <div class="table-responsive">
    </#if>
    <table class="table ${class}" <#if id != "">id="${id}"</#if>>
        <#nested>
    </table>
    <#if responsive>
        </div>
    </#if>
</#macro>

<#macro thead id="">
<thead <#if id != "">id="${id}"</#if>>
    <#nested>
</thead>
</#macro>

<#macro tbody id="">
<tbody <#if id != "">id="${id}"</#if>>
    <#nested>
</tbody>
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

