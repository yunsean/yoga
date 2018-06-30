<#setting url_escaping_charset='utf-8'>
<#macro paging page param action>
    <#if (page.pageCount>0)>
    <div class="page">
        <#assign showPageCount=9 />
        <#assign pageCount=page.pageCount/>
        <#assign pageIndex=page.pageIndex/>
        <#if (pageIndex > 0)>
            <a class="btn btn-xs btn-default" href="javascript:void(0);" onclick="navigatorPage(${page.pageIndex - 1})">上一页</a>
        </#if>
        <#if (pageIndex < pageCount - 1)>
            <a class="btn btn-xs btn-default" href="javascript:void(0);" onclick="navigatorPage(${page.pageIndex + 1})">下一页</a>
        </#if>
        找到<span style='color: #FD7B02;'>${page.totalCount}</span>条数据，
        共<span style='color: #FD7B02;'>${page.pageCount}</span>页，
        每页 ${page.pageSize} 条数据
        &nbsp;到第<input style="text-align:center;width:3em;" class="w20 mar0 alignCenter" onblur="navigatorPage(parseInt(this.value)-1)" maxlength="5" type="text" value="${pageIndex + 1}"style="width:40px"> 页
    </div>
    <script language="JavaScript" type="text/javascript">
        function navigatorPage(index) {
            var url = "${getUrl(action, page.pageSize, param)}&pageIndex=" + index;
            window.location = url;
        }
        document.querySelector("input.w20").onkeydown = function (e) {
            var keyNum;
            if (window.event) {
                keyNum = e.keyCode;
            } else if (e.which) {
                keyNum = e.which;
            }
            if (keyNum == 13) {
                navigatorPage(parseInt(this.value) - 1);
            }
        }
    </script>
    </#if>
</#macro>

<#function getUrl action size params>
    <#if (action?index_of('?')==-1)>
        <#assign aurl=action+"?pageSize=" + size />
    <#else>
        <#assign aurl=action+"&pageSize=" + size />
    </#if>
    <#if (params?exists && params?is_hash)>
        <#list params?keys as key>
            <#if params[key]?exists>
                <#if params[key]?is_collection || params[key]?is_indexable>
                    <#list params[key] as v>
                        <#if !(v?is_collection || v?is_hash)>
                            <#assign aurl = aurl + "&" + key + "=" + v?if_exists />
                        </#if>
                    </#list>
                <#elseif params[key]?is_boolean>
                    <#assign aurl = aurl + "&" + key + "=" + params[key]?if_exists?string('yes', 'no') />
                <#elseif params[key]?is_date>
                    <#assign aurl = aurl + "&" + key + "=" + params[key]?string("yyyy-MM-dd")?url />
                <#elseif params[key]?is_number>
                    <#assign aurl = aurl + "&" + key + "=" + params[key]?if_exists?c />
                <#else>
                    <#assign aurl = aurl + "&" + key + "=" + params[key]?if_exists?url />
                </#if>
            </#if>
        </#list>
    </#if>
    <#return aurl>
</#function>
