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
<div class="container-fluid">
    <div class="row content-bottom" style="padding: 15px">
        <div class="panel panel-primary col-sm-10 col-sm-offset-1">
            <div class="panel-body col-sm-4">
                <table class="table table-borderless">
                    <thead>
                    <tr>
                        <th style="width:60%">选择栏目</th>
                    </tr>
                    </thead>
                    <#macro m_column column>
                        <tr data-id="${column.id?c}" data-show="${column.name?replace('"', '\"')}" class="lineH" style="height: 40px">
                            <td id="titleName" style="vertical-align:middle; " onclick="showColumn(${column.templateId?default(0)?c})">
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
            </div>
            <div class="panel-body col-sm-4">
                <table class="table table-borderless">
                    <thead>
                    <tr>
                        <th style="width:60%">栏目列表布局</th>
                    </tr>
                    </thead>
                </table>
                <ul class="group js_list img_list" id="layout_list" style="list-style-type:none">

                </ul>
            </div>
            <div class="panel-body col-sm-4">
                <table class="table table-borderless">
                    <thead>
                    <tr>
                        <th style="width:60%">文章页面布局</th>
                    </tr>
                    </thead>
                </table>
                <ul class="group js_list img_list" id="layout_detail" style="list-style-type:none">

                </ul>
            </div>
        </div>
    </div>
</div>
<footer class="main-footer">
    <@common.footer />
</footer>

<script type="text/javascript" src="<@macroCommon.resource/>/tree/jqtreetable.js"></script>
<script type="text/javascript" charset="utf-8">
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
    $(function(){
        $('table tr').click(function(){
            $('table tr').removeClass('on');
            $(this).addClass('on');
            $('table tr').removeClass('column-checked');
            $(this).addClass('column-checked');
        })
    });
</script>

<script>
    function showColumn(templateId){
        $.post(
                "/api/cms/layout/list",
                {
                    templateId: templateId,
                    type: 'LIST'
                },
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#layout_list").children().remove();
                        var layouts = data.result;
                        for (var i = 0; i < layouts.length; i++) {
                            var layout = layouts[i];
                            $("#layout_list").append(
                                   '<li class="img_item js_imageitem" style="float: left; margin-right: 25px; margin-bottom: 10px">' +
                                    '<label class="frm_checkbox_label img_item_bd">' +
                                    '<div class="pic_box">' +
                                    '<img class="pic js_pic" src="' + layout.image + '" onerror="this.src=\'/images/onerror.jpg\'" style="width: 188px; height: 150px">' +
                                    '</div>' +
                                    '<label class="radio-inline">'+
                                    '<input name="layout_list_choiced" type="radio" value="' + layout.id + '">'+
                                   '<span class="lbl_content">' + layout.title + '</span>' +
                                    '</label>'+
                                    '</label>' +
                                    '</li>'
                            );
                        }
                    }
                },
                "json"
        );
        $.post(
                "/api/cms/layout/list",
                {
                    templateId: templateId,
                    type: 'DETAIL'
                },
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#layout_detail").children().remove();
                        var layouts = data.result;
                        for (var i = 0; i < layouts.length; i++) {
                            var layout = layouts[i];
                            $("#layout_detail").append(
                                    '<li class="img_item js_imageitem" style="float: left; margin-right: 25px; margin-bottom: 10px">' +
                                    '<label class="frm_checkbox_label img_item_bd">' +
                                    '<div class="pic_box">' +
                                    '<img class="pic js_pic" src="' + layout.image + '" onerror="this.src=\'/images/onerror.jpg\'" style="width: 188px; height: 150px">' +
                                    '</div>' +
                                    '<label class="radio-inline">'+
                                    '<input name="layout_detail_choiced" type="radio" value="' + layout.id + '">'+
                                    '<span class="lbl_content">' + layout.title + '</span>' +
                                    '</label>'+
                                    '</label>' +
                                    '</li>'
                            );
                        }
                    }
                },
                "json"
        );
    }
    function getConfig() {
        if ($(".column-checked").length < 1) {
            alertShow("warning", "请选择栏目！", 1000);
            return;
        }
        var column = $(".column-checked:first");
        if ($("input[name='layout_list_choiced']:checked").length < 1) {
            alertShow("warning", "请选择列表布局样式！", 1000);
            return;
        }
        var layoutList = $("input[name='layout_list_choiced']:checked:first");
        if ($("input[name='layout_detail_choiced']:checked").length < 1) {
            alertShow("warning", "请选择详情布局样式！", 1000);
            return;
        }
        var layoutDetail = $("input[name='layout_detail_choiced']:checked:first");

        var config = {};
        config["listLayoutId"] = layoutList.val();
        config["detailLayoutId"] = layoutDetail.val();
        config["columnId"] = column.attr("data-id");
        config["tenantId"] = ${tenantId?c};
        return "${baseUrl?if_exists}?tid=${tenantId?c}&listLayoutId=" + layoutList.val() + "&detailLayoutId=" + layoutDetail.val() + "&columnId=" + column.attr("data-id");
    }
</script>

</@macroCommon.html>