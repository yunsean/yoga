<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<#setting number_format="##0.##">
<@html>
    <@head includeDate=true>
        <style>
            td {
                vertical-align: middle!important;
            }
            .checked {
                background-color: #eee;
            }
            tr.on td{
                background-color: #66b3ff;
            }
        </style>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="内容管理" icon="icon-user">
            <@crumbItem href="#" name="文章管理" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "文章管理" />
                <@panelBody>
                    <table class="table table-borderless" style="width:20%;float: left">
                        <thead>
                            <tr>
                                <th style="width:60%">栏目名称</th>
                            </tr>
                        </thead>
                        <tbody>
                            <@m1_columns2 columns!/>
                        </tbody>
                    </table>
                    <iframe id="iframe" width=79% height=650px frameborder=0 float:right scrolling=auto src="/admin/cms/article/list?columnId=${(param.columnId?c)!}">
                    </iframe>
                </@panelBody>
            </@panel>
        </@bodyContent>
    </@bodyFrame>

    <script>
        $(function () {
            $("td").mouseover(function(){
                $(this).css("cursor","pointer");
                $(this).parent().addClass("checked");
            });
            $("td").mouseout(function(){
                $(this).css("cursor","auto");
                $(this).parent().removeClass("checked");
            });
        });
        function iconClick(id, obj) {
            var list = document.getElementsByName(id);
            var obj = $(obj).parent().parent()
            var objImg = $(obj).find("img").attr("src");
            if (objImg == undefined) return;
            if (objImg.indexOf("expansion.png") > 0) {
                $(obj).find("img").attr("src", "/admin/tree/shrink.png");
                for (var i = 0; i < list.length; i++) {
                    list[i].style.display = "";
                }
            } else {
                $(obj).find("img").attr("src", "/admin/tree/expansion.png");
                for (var i = 0; i < list.length; i++) {
                    list[i].style.display = "none";
                    var img = $(list[i]).find("img").attr("src");
                    if (img == undefined) continue;
                    if (img.indexOf("shrink.png") > 0) {
                        $(list[i]).children().eq(0).click();
                    }
                }
            }
        }
    </script>

    <#macro m1_column2 branch level index hidden=false>
        <tr <#if hidden>style="display: none"</#if> name="${(branch.parentId?c)!}" id="${(branch.id?c)!}" <#if branch.id == (param.columnId!0)>class="on"</#if>>
            <td onclick="rowClick(${(branch.id?c)!},this)" style=" padding-left: ${level * 20 + 10}px;height: 40px;">
                <#if branch.children??>
                    <img onclick="iconClick(${(branch.id?c)!},this)" <#if hidden>src="/admin/tree/shrink.png"<#else>src="/admin/tree/expansion.png"</#if> width="18px;" height="9px;">${(branch.name)!}
                <#else >
                    ${(branch.name)!}
                </#if>
            </td>
        </tr>
    </#macro>
    <#macro m1_columns2 columns level=0 hidden=false>
        <#if columns??>
            <#list columns as column>
                <@m1_column2 column level column_index hidden/>
                <#local level1 = level + 1/>
                <#if column.children??>
                    <@m1_columns2 column.children level1 true/>
                </#if>
            </#list>
        </#if>
    </#macro>

    <#macro m1_column column level index>
        <option value="${(column.id?c)!}">
            <#list 0..level as x><#if x < level>┃&nbsp;&nbsp;<#else>┠</#if></#list>${(column.name)!}
        </option>
    </#macro>
    <#macro m1_columns columns level index>
        <@m1_column columns level index/>
        <#local level1 = level + 1/>
        <#if columns.children??>
            <#list columns.children as sub>
                <@m1_columns sub level1 sub_index/>
            </#list>
        </#if>
    </#macro>

    <script>
        function autoAdjustIFrame() {
            var iframe = document.getElementById("iframe");
            if (iframe) {
                var iframeWin = iframe.contentWindow || iframe.contentDocument.parentWindow;
                if (iframeWin.document.body) {
                    iframe.height = iframeWin.document.documentElement.scrollHeight || iframeWin.document.body.scrollHeight;
                }
            }
        }
        function rowClick(columnId) {
            $("#iframe").attr("src","/admin/cms/article/list?columnId=" + columnId);
        }
        $('table tr').click(function(){
            $('table tr').removeClass('on');
            $(this).addClass('on');
        })
    </script>
</@html>
