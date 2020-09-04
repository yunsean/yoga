<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<#setting number_format="##0.##">
<@html>
    <@head>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="内容管理" icon="icon-user">
            <@crumbItem href="#" name="文章管理" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "文章管理" />
                <div class="panel-body" style="padding: 0px!important;">
                    <iframe id="iframe" width=100% height=650px frameborder=0 float:right scrolling=auto src="/admin/cms/article/list?${queryString!}">
                    </iframe>
                </div>
            </@panel>
        </@bodyContent>
    </@bodyFrame>

    <script>
        function autoAdjustIFrame() {
            var iframe = document.getElementById("iframe");
            if (iframe) {
                var iframeWin = iframe.contentWindow || iframe.contentDocument.parentWindow;
                if (iframeWin.document.body) {
                    iframe.height = iframeWin.document.documentElement.scrollHeight || iframeWin.document.body.scrollHeight;
                }
            }
        };
    </script>
</@html>
