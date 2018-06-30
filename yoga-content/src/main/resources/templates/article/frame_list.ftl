<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<@macroCommon.html>
<link rel="stylesheet" type="text/css" href="<@macroCommon.resource/>/tree/jqtreetable.css">
<style>
    #main{
        width: 16%;
        float: left;
        min-height: 560px;
        background: #f9f9f9;
        border-bottom: 0.5px solid #f4f4f4;
        border-left: 0.5px solid #f4f4f4;
        border-right: 0.5px solid #f4f4f4;
    }
    tr.on td{
        background-color: #66b3ff;
    }

    #article{
        float: right;
        width: 84%;
        padding-left: 20px;
    }

    .form-inline{
        float: left;
    }

    #addContentHref{
        margin-left: 15px;
        margin-top: 4px;
    }

    .li_style{
        list-style: none;
        padding-left: 10px;
    }
    .li_style li{
        padding-left: 10px;
    }
</style>
<body>
<div class="spinner">
    <div class="rect1"></div>
    <div class="rect2"></div>
    <div class="rect3"></div>
    <div class="rect4"></div>
    <div class="rect5"></div>
    <h5> 加载中...</h5>
</div>
<div class="wrapper">
    <div class="container-fluid">
        <div class="row">
            <ol class="breadcrumb">
                <li><a href="#"><i class="icon icon-dashboard"></i>内容管理</a></li>
                <li>文章管理</li>
            </ol>
        </div>
        <div class="row content-bottom">
            <div class="col-lg-12">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <i class="icon icon-comments-alt"></i>
                        文章详情
                    </div>
                    <div class="panel-body">
                        <table class="table table-borderless" style="width:20%;float: left">
                            <thead>
                            <tr>
                                <th style="width:60%">栏目名称</th>
                            </tr>
                            </thead>
                            <#macro m_column column>
                                <tr class="lineH" style="height: 40px">
                                    <td id="titleName" style="vertical-align:middle; " onclick="showColumn(${column.id?c})">
                                        <#if column.children?? && column.children?size gt 0>
                                            <i class="icon icon-folder-open"></i>
                                        <#else>
                                            <i class="icon icon-th-large"></i>
                                        </#if>
                                        <a href="#"  style="color:#333; text-decoration:none;" >${column.name}</a>
                                    </td>
                                </tr>
                            </#macro>
                            <#macro m_columns columns>
                                <@m_column columns />
                                <#if columns.children??>
                                    <#list columns.children as sub>
                                        <@m_columns sub/>
                                    </#list>
                                </#if>
                            </#macro>
                            <#list columns as root>
                                <tbody id="treet_dd_${root_index}_">
                                    <@m_columns root/>
                                </tbody>
                            </#list>
                        </table>
                        <iframe id="iframe" width=79% height=650px frameborder=0 float:right scrolling=auto src="/cms/article/list?columnId=${columnId?default(0)}">
                        </iframe>
                    </div>
                    <div class="box-footer" style="text-align:center">
                    </div>
                </div>
            </div>
        </div>
    </div>
    <footer class="main-footer">
        <@common.footer />
    </footer>
</div>
<script type="text/javascript" src="<@macroCommon.resource/>/tree/jqtreetable.js"></script>
<script type="text/javascript" charset="utf-8">
    function autoAdjustIFrame() {
        var iframe = document.getElementById("iframe");
        if (iframe) {
            var iframeWin = iframe.contentWindow || iframe.contentDocument.parentWindow;
            if (iframeWin.document.body) {
                iframe.height = iframeWin.document.documentElement.scrollHeight || iframeWin.document.body.scrollHeight;
            }
        }
    };
    $(function () {
                <#function sublist parent level>
                    <#assign ret = "${level}, "/>
                    <#assign rowcount = rowcount + 1 />
                    <#local level1 = rowcount />
                    <#if parent.children??>
                        <#list parent.children as sub>
                            <#assign ret = ret + sublist(sub, level1) />
                        </#list>
                    </#if>
                    <#return ret/>
                </#function>
                <#function maplist root index>
                    <#assign var = "var map${index} = [" />
                    <#assign var = var + sublist(root, 0) />
                    <#assign var = var + "];" />
                    <#return var />
                </#function>
                <#list columns as root>
                    <#assign rowcount=0 />
                    <#assign map=maplist(root root_index)/>
                ${map}
                </#list>
                //声明参数选项
                var options = {
                    openImg: "<@macroCommon.resource/>/tree/tv-collapsable.png",
                    shutImg: "<@macroCommon.resource/>/tree/tv-expandable.png",
                    leafImg: "<@macroCommon.resource/>/tree/tv-item.png",
                    lastOpenImg: "<@macroCommon.resource/>/tree/tv-collapsable-last.png",
                    lastShutImg: "<@macroCommon.resource/>/tree/tv-expandable-last.png",
                    lastLeafImg: "<@macroCommon.resource/>/tree/tv-item-last.png",
                    vertLineImg: "<@macroCommon.resource/>/tree/vertline.png",
                    blankImg: "<@macroCommon.resource/>/tree/blank.png",
                    collapse: false,
                    column: 0,
                    striped: false,
                    highlight: true,
                    state: false,
                    parentAnchor: false
                };
                <#list columns as root>
                    if (map${root_index} != null && map${root_index}.length > 0) {
                        $("#treet_dd_${root_index}_").jqTreeTable(map${root_index}, options);
                    }
                </#list>
            }
    );
</script>

<script>
    $(".spinner").hide();
    function showColumn(columnId){
        var pageIndex = $("#thisPageIndex").val();
        var pageSize = getCookie("pageSize");
        if(!pageSize){
            pageSize="10";
            setCookie("pageSize","10");
        }
        var param = "type=allContent&columnId=" + columnId;
        $("#iframe").attr("src","/cms/article/list?" + param);
    }
    function getCookie(name) {
        var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
        if(arr=document.cookie.match(reg))
            return unescape(arr[2]);
        else
            return null;
    }
    function setCookie(name,value) {
        var Days = 30;
        var exp = new Date();
        exp.setTime(exp.getTime() + Days*24*60*60*1000);
        document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
    }
    function ul_fold(){
        $(this).next('ul').toggle();
    }
    $(function(){
        $('table tr').click(function(){
            $('table tr').removeClass('on');
            $(this).addClass('on');
        })
    });
</script>
</body>
</@macroCommon.html>
