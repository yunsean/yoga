<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<@macroCommon.html>
<link rel="stylesheet" type="text/css" href="<@macroCommon.resource/>/tree/jqtreetable.css">
<style>
    .listMenu {
        list-style: none;
        padding-left: 2px;
        font-size: 14px;
        color: #212121;
    }

    .menuItem {
        margin: 4px 0;

    }

    .menuItem > a {
        display: block;
        color: #212121;
        padding: 8px 4px;
    }

    .menuItem:hover {
        /*background:rgb(60, 141, 188);*/
        background: #f5f5f5;
    }

    .menuItem.selected {
        background: #66b3ff;
    }

    .menuItem:hover > a, .menuItem.selected > a {
        color: #212121;
    }

    .menuWrap {
        width: 16%;
        float: left;
        min-height: 560px;
        background: #f9f9f9;
        border-bottom: 0.5px solid #f4f4f4;
        border-left: 0.5px solid #f4f4f4;
        border-right: 0.5px solid #f4f4f4;

    }

    .titleMenu {
        padding: 8px 4px;
        margin-bottom: 10px;
        background-color: #f1f1f1;
        border-top: 0.5px solid #ddd;
        border-bottom: 0.5px solid #ddd;
    }

    .titleMenu:after {
        content: "";
        display: block;
        clear: both;
    }

    .tabelList {
        float: left;
        width: 82%;
        padding-left: 20px;
    }

    .totalTitle {
        height: 25px;
        line-height: 25px;
        float: left;
        font-weight: 550;
        font-size: 16px;
    }

    .totalControl {
        float: right;
    }
</style>
<body>
    <div class="container-fluid">
        <div class="row">
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i>内容管理</a></li>
                <li>选项管理</li>
            </ol>
        </div>
        <div class="row content-bottom">
            <div class="col-sm-12">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <i class="fa fa-comments-o"></i>
                        选项列表
                    </div>
                    <div class="panel-body">
                        <div class="menuWrap">
                            <div class="titleMenu">
                                <div class="totalTitle">
                                    <span>
                                        <i class="fa fa-fw fa-list"></i>选项列表
                                    </span>
                                </div>
                                <div class="totalControl">
                                    <@shiro.hasPermission name="cms_property.add">
                                        <a href="#" class="btn btn-primary" id="propertyAddButton">
                                            <i class="icon icon-user"></i>添加</a>
                                    </@shiro.hasPermission>
                                </div>
                            </div>
                            <ul class="listMenu">
                                <#list properies as property>
                                    <li <#if selected==property.code>class="menuItem selected"
                                        <#else>class="menuItem"  </#if>>
                                        <a href="/cms/property?code=${property.code}" style="text-decoration:none;"
                                           id="${property.id?c}">${property.name}</a></li>
                                </#list>
                                <ul>
                        </div>
                        <div class="tabelList">
                            <table id="" class="table table-bordered table-striped">
                                <thead>
                                <tr>
                                    <th style="width:30%">选项名称</th>
                                    <th class="tableLeft" style="width:40%">选项CODE</th>
                                    <th class="tableCenter" style="width:30%">操作</th>
                                </tr>
                                </thead>
                                <#macro m_column column>
                                    <tr class="lineH">
                                        <td class="titleName" style="vertical-align:middle;">${column.name?if_exists}</td>
                                        <td class="tableLeft" style="vertical-align:middle;">${column.code?if_exists}</td>
                                        <input type="hidden" name="parentId" value="${column.parentId?default(0)?c}">
                                        <input type="hidden" name="id" value="${column.id?c}">
                                        <input type="hidden" name="id" value="${column.name?if_exists}">
                                        <td class="tableCenter">
                                            <@shiro.hasPermission name="cms_property.add" >
                                                <a href="#" class="btn btn-success "
                                                   onclick="toAddValue(${column.id?default(0)?c})">
                                                    <i class="fa fa-fw fa-plus "></i>添加选项值</a>
                                            </@shiro.hasPermission>
                                            <@shiro.hasPermission name="cms_property.update" >
                                                <a href="#" class="btn btn-info" onclick="doEdit(this)">
                                                    <i class="fa fa-fw fa-pencil "></i>修改</a>
                                            </@shiro.hasPermission>
                                            <@shiro.hasPermission name="cms_property.del">
                                                <a href="#" class="btn btn-danger"
                                                   onclick="delProperty(${column.id?default(0)?c})">
                                                    <i class="fa fa-fw fa-remove "></i>删除</a>
                                            </@shiro.hasPermission>

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
                                <#list values as root>
                                    <tbody id="treet${root_index}">
                                        <@m_columns root/>
                                    </tbody>
                                </#list>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <footer class="main-footer">
        <@common.footer />
    </footer>
    <#macro m1_column column level index>
        <option value="${column.id?if_exists?c}">
            <#list 0..level as x>
                <#if x < level>
                    │&nbsp;&nbsp;
                <#else>
                    ├
                </#if>
            </#list>
        ${column.name?if_exists}
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

    <script type="text/javascript" src="/tree/jqtreetable.js"></script>
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
                    <#list values as root>
                        <#assign rowcount=0 />
                        <#assign map=maplist(root root_index) />
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
                        striped: true,
                        highlight: true,
                        state: false
                    };
                    <#list values as root>
                        if (map${root_index} != null && map${root_index}.length > 0) {
                            $("#treet${root_index}").jqTreeTable(map${root_index}, options);
                        }
                    </#list>
                }
        );
    </script>
</body>

    <@operate.add "新增选项（值）" "#propertyAddButton" "addProperty">
    <div class="modalForm" style="margin-top: 15px">
        <div class="form-group">
            <label class="col-sm-3  control-label">字段名称</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="name">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">字段CODE</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="code">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">父节点</label>
            <div class="col-sm-8">
                <select class="form-control" style="width:50%;" id="parentId">
                    <option value="0">根栏目</option>
                    <#list values as root>
                        <@m1_columns root 0 root_index/>
                    </#list>
                </select>
            </div>
        </div>
    </div>
    <div class="modalForm" id="edit_reload_div">
        <div class="form-group">
            <div style="float: right; margin-right: 30px;">
                <input id="edit_reload" type="checkbox" checked="getCookie('reload');">
                <label for="edit_reload">保存成功后刷新页面</label>
            </div>
        </div>
    </div>
    </@operate.add>
<script>
    function addProperty() {
        var name = $("#name").val();
        var code = $("#code").val();
        var parentId = $("#parentId").val();
        var reload = $("#edit_reload").prop("checked");
        setCookie("reload", reload);
        if (parentId == 0) {
            $.post("/api/cms/property/add",
                    {name: name, code: code, parentId: parentId},
                    function (data) {
                        if (data.code < 0) {
                            alertShow("warning", data.message, 3000);
                            return;
                        } else {
                            $("#operate_add_modal").modal("hide");
                            if (reload) window.location.reload();
                            $("#name").val('');
                            $("#code").val('');
                        }
                    },
                    "json"
            );
        } else {
            $.post("/api/cms/property/value/add",
                    {name: name, code: code, parentId: parentId},
                    function (data) {
                        if (data.code < 0) {
                            alertShow("warning", data.message, 3000);
                            return;
                        } else {
                            $("#operate_add_modal").modal("hide");
                            if (reload) window.location.reload();
                            $("#name").val('');
                            $("#code").val('');
                        }
                    },
                    "json"
            );
        }
    }
    function getCookie(c_name) {
        if (document.cookie.length > 0) {
            c_start = document.cookie.indexOf(c_name + "=");
            if (c_start != -1) {
                c_start = c_start + c_name.length + 1;
                c_end = document.cookie.indexOf(";", c_start);
                if (c_end == -1) c_end = document.cookie.length;
                return unescape(document.cookie.substring(c_start, c_end));
            }
        }
        return "checked";
    }
    function setCookie(c_name, value, expiredays) {
        var exdate = new Date();
        exdate.setDate(exdate.getDate() + expiredays);
        document.cookie = c_name + "=" + escape(value) + ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString());
    }
    function toAddValue(id) {
        $("#parentId").val(id);
        $("#operate_add_modal").modal("show");
    }
    function delProperty(id) {
        warningModal("确定要删除该选项吗?", function () {
            $.post(
                    "/api/cms/property/delete",
                    {id: id},
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);;
                        } else {
                            window.location.href = "/cms/property";
                        }
                    }
            );
        });
    }
</script>

    <@operate.editview "编辑选项（值）">
    <div class="modalForm" style="margin-top: 15px">
        <div class="form-group">
            <label class="col-sm-3  control-label">字段名称</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="edit_name">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">字段CODE</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="edit_code">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">父节点</label>
            <div class="col-sm-8">
                <select class="form-control" style="width:50%;" id="edit_parentId">
                    <option value="0">根栏目</option>
                    <#list values as root>
                        <@m1_columns root 0 root_index/>
                    </#list>
                </select>
            </div>
        </div>
    </div>
    <input type="hidden" id="edit_Id">
    </@operate.editview>
    <@operate.editjs "saveEdit">
    $("#edit_name").val($(elem).parent().siblings().eq(4).val());
    $("#edit_code").val($(elem).parent().siblings().eq(1).html());
    $("#edit_parentId").val($(elem).parent().siblings().eq(2).val());
    $("#edit_Id").val($(elem).parent().siblings().eq(3).val());
    </@operate.editjs>
<script>
    function saveEdit() {
        var id = $("#edit_Id").val();
        var name = $("#edit_name").val();
        var code = $("#edit_code").val();
        var parentId = $("#edit_parentId").val();
        $.post(
                "/api/cms/property/update",
                {name: name, code: code, id: id, parentId: parentId},
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#deptEditorModal").modal("hide");
                        window.location.reload();
                    }
                },
                "json"
        );
    }
</script>

</@macroCommon.html>

