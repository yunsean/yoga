<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<@macroCommon.html>
<script type="text/javascript" src="/ueditor/ueditor.streamline.js"></script>
<!-- 编辑器源码文件 -->
<script type="text/javascript" src="/ueditor/ueditor.all.js"></script>
<script type="text/javascript" src="/ueditor/lang/zh-cn/zh-cn.js"></script>
<link href="<@macroCommon.resource/>/uploader/zui.uploader.css" rel="stylesheet">
<script src="<@macroCommon.resource/>/uploader/zui.js"></script>
<script src="<@macroCommon.resource/>/uploader/zui.uploader.min.js"></script>
<link href="<@macroCommon.resource/>/css/group.css" rel="stylesheet">
<style>
    .example {
        position: relative;
        padding: 5px;
        border: 1px solid #ddd;
        outline: 0;
        transition: all .3s;
    }
</style>
<body>
<div class="spinner" style="display: none">
    <div class="spinner-container container1">
        <div class="circle1"></div>
        <div class="circle2"></div>
        <div class="circle3"></div>
        <div class="circle4"></div>
    </div>
    <div class="spinner-container container2">
        <div class="circle1"></div>
        <div class="circle2"></div>
        <div class="circle3"></div>
        <div class="circle4"></div>
    </div>
    <div class="spinner-container container3">
        <div class="circle1"></div>
        <div class="circle2"></div>
        <div class="circle3"></div>
        <div class="circle4"></div>
    </div>
</div>
<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>微信公众号</a></li>
            <li>条件菜单管理</li>
        </ol>
    </div>
    <div class="row content-bottom">
        <div class="col-sm-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="icon icon-user"></i>个性化条件菜单
                </div>
                <div class="panel-body">
                    <div class="tableToolContainer" style="margin-bottom:15px">
                        <form action="/wechat/menu/condition" method="POST" class="form-inline" id="filter_form">
                            <div class="form-group">
                                <label class="exampleInputAccount4" style="margin-right: 20px">微信账号</label>
                                <select class="form-control" style="min-width: 200px" id="accountId" name="accountId" onchange="loadAccount()">
                                    <#list accounts as account>
                                        <option value="${account.id?default(0)}">${account.name?if_exists}</option>
                                    </#list>
                                </select>
                                <script>
                                    $("#accountId").val('${param.accountId?default(0)}');
                                    function loadAccount() {
                                        $("#filter_form").submit();
                                    }
                                </script>
                            </div>
                            <a onclick="doAddMenu()" class="btn btn-info">
                                <i class="fa fa-fw fa-plus"></i>增加个性化菜单
                            </a>
                        </form>
                    </div>
                    <table class="table table-bordered table-striped">
                        <thead>
                            <tr>
                                <th style="width:20%">菜单名称</th>
                                <th style="width:10%">标签</th>
                                <th style="width:10%">性别</th>
                                <th style="width:10%">手机操作系统</th>
                                <th style="width:10%">语言</th>
                                <th style="width:20%">地区</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <#if conditionalMenus??>
                                <#list conditionalMenus  as menu>
                                    <tr>
                                        <td>${menu.name?if_exists}</td>
                                        <td>${menu.tagId?if_exists}</td>
                                        <td><#if menu.gender??>${menu.gender.getName()?if_exists}</#if></td>
                                        <td><#if menu.clientOS??>${menu.clientOS.getName()?if_exists}</#if></td>
                                        <td><#if menu.language??>${menu.language.getName()?if_exists}</#if></td>
                                        <td>${menu.country?if_exists} ${menu.province?if_exists} ${menu.city?if_exists}</td>
                                        <td>
                                            <a href="javascript:void(0)" onclick="doEditMenu(${menu.id?default(0)?c})" class="btn btn-sm btn-info">
                                                <i class="fa fa-fw  fa-edit"></i>编辑条件</a>
                                            <a href="/wechat/menu/edit?isdefault=0&accountId=${menu.accountId!0}&menuId=${menu.id!0}" class="btn btn-sm btn-primary">
                                                <i class="fa fa-fw  fa-edit"></i>编辑菜单</a>
                                            <a href="javascript:void(0)" onclick="doDeleteMenu(${menu.id?default(0)?c})" class="btn btn-sm btn-danger" style="margin-left: 20px">
                                                <i class="fa fa-fw  fa-remove"></i>删除</a>
                                        </td>
                                    </tr>
                                </#list>
                            </#if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

</div>
<footer class="main-footer">
    <@common.footer />
</footer>
</body>

    <@operate.modal "条件菜单" "menu_edit_modal" "saveMenu">
    <div class="modalForm" style="margin-top: 15px">
        <div class="form-group">
            <label class="col-sm-2 control-label">菜单名称</label>
            <div class="col-sm-9">
                <input type="text" class="form-control" placeholder="菜单名长度不超过15个字符" id="add_menu_name">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="control-label col-sm-2">用户标签</label>
            <div class="col-sm-4">
                <select id="add_menu_tags" class="form-control">
                    <option value="">全部</option>
                    <option value="development">研发</option>
                </select>
            </div>
            <label class="col-sm-1  control-label">语言</label>
            <div class="col-sm-4">
                <select id="add_menu_language" class="form-control">
                    <#list enums["com.yoga.wechat.menu.RuleLanguage"]?values as item>
                        <option value="${item}">${item.desc}</option>
                    </#list>
                </select>
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-2  control-label">手机操作系统</label>
            <div class="col-sm-4">
                <select id="add_menu_os" class="form-control">
                    <#list enums["com.yoga.wechat.menu.RuleOs"]?values as item>
                        <option value="${item}">${item.desc}</option>
                    </#list>
                </select>
            </div>
            <label class="col-sm-1  control-label">性别</label>
            <div class="col-sm-4">
                <select id="add_menu_gender" class="form-control">
                    <#list enums["com.yoga.wechat.menu.RuleGender"]?values as item>
                        <option value="${item}">${item.desc}</option>
                    </#list>
                </select>
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group" id="address">
            <label class="col-sm-2  control-label">地区</label>
            <div class="col-sm-2">
                <select id="country_add" class="form-control" onchange="country_change()">
                    <option value="0">--请选择--</option>
                </select>
            </div>
        </div>
    </div>
    <input type="hidden" id="add_menu_id">
    </@operate.modal>

<script>
    function doAddMenu() {
        country_load(0)
        $("#menu_edit_modal_form")[0].reset();
        $("#add_menu_id").val('0');
        $("#menu_edit_modal").modal("show");
    }

    function doEditMenu(id) {
        $.get(
                "/api/wechat/account/menu/get?id=" + id,
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#add_menu_id").val(id);
                        $("#add_menu_name").val(data.result.name);
                        $("#add_menu_tags").val(data.result.tagId);
                        $("#add_menu_gender").val(data.result.gender);
                        $("#add_menu_os").val(data.result.clientOS);
                        $("#add_menu_language").val(data.result.language);
                        $("#menu_edit_modal").modal("show");

                        country_load(data.result.country)
                        country_change(data.result.province)
                        province_change(data.result.city)


                    }
                }
        );
    }

    function doDeleteMenu(id) {
        warningModal("确定要删除该个性化菜单吗?", function () {
            $.get(
                    "/api/wechat/account/menu/delete?id=" + id,
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);
                        } else {
                            window.location.reload();
                        }
                    }
            );
        });
    }


    function saveMenu() {
        var id = $("#add_menu_id").val();
        var name = $("#add_menu_name").val();
        var tag = $("#add_menu_tags").find("option:selected").val();
        var gender = $("#add_menu_gender").find("option:selected").val();
        var clientOS = $("#add_menu_os").find("option:selected").val();
        var language = $("#add_menu_language").find("option:selected").val();
        if ($("#country_add").val() != 0)
            var country = $("#country_add option:selected").html()
        if ($("#province_add").val() != 0)
            var province = $("#province_add option:selected").html()
        if ($("#city_add").val() != 0)
            var city = $("#city_add option:selected").html()
        $.post(id == '0' ? "/api/wechat/account/menu/add" : "/api/wechat/account/menu/update",
                {
                    id: id,
                    accountId: ${param.accountId?default(0)},
                    name: name,
                    isDefault: true,
                    tagId: tag,
                    gender: gender,
                    clientOS: clientOS,
                    language: language,
                    country: country,
                    province: province,
                    city: city
                },
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#operate_add_modal").modal("hide");
                        window.location.reload();
                        if (data.code != 0) {
                            alertShow("warning", data.message, 3000);
                        }
                    }
                }
        );
    }
</script>


<script>
    function country_load(name) {
        $("#address div").remove()
        var str_div = "<div class='col-sm-2'><select id='country_add' class='form-control' name='country' onchange='country_change(0)'><option value='0'>--请选择--</option></select></div>"
        $("#address").append(str_div)

        $.getJSON(
                "/address.json",
                "",
                function (data) {
                    $.each(data, function (i, item) {
                        if (name == 0) {
                            var str = "<option value=" + item.id + " index=" + i + ">" + item.name + "</option>"
                            $("#country_add").append(str)
                        } else {
                            if (item.name == name) {
                                var str = "<option selected='selected' value=" + item.id + " index=" + i + ">" + item.name + "</option>"
                                $("#country_add").append(str)
                            } else {
                                var str = "<option value=" + item.id + " index=" + i + ">" + item.name + "</option>"
                                $("#country_add").append(str)
                            }
                        }

                    })
                }
        );
    }

    function country_change(name) {
        $.getJSON(
                "/address.json",
                "",
                function (data) {
                    $("#address div:eq(1)").remove()
                    $("#address div:eq(1)").remove()

                    var index = $("#country_add option:selected").attr("index")
                    if (data[index].children != null) {
                        var str_div = "<div class='col-sm-2'><select id='province_add' class='form-control' name='province' onchange='province_change(0)'><option value='0'>--请选择--</option></select></div>"
                        $("#address").append(str_div)
                        $.each(data[index].children, function (i, item) {
                            if (name == 0) {
                                var str = "<option value=" + item.id + " index=" + i + ">" + item.name + "</option>"
                                $("#province_add").append(str)
                            } else {
                                if (item.name == name) {
                                    var str = "<option selected='selected' value=" + item.id + " index=" + i + ">" + item.name + "</option>"
                                    $("#province_add").append(str)
                                } else {
                                    var str = "<option value=" + item.id + " index=" + i + ">" + item.name + "</option>"
                                    $("#province_add").append(str)
                                }
                            }
                        })

                    }
                }
        );
    }

    function province_change(name) {
        $.getJSON(
                "/address.json",
                "",
                function (data) {
                    $("#address div:eq(2)").remove()

                    var index_c = $("#country_add option:selected").attr("index")
                    var index_p = $("#province_add option:selected").attr("index")
                    if (data[index_c].children[index_p].children != null) {
                        var str_div = "<div class='col-sm-2'><select id='city_add' class='form-control' name='city' onchange=''><option value='0'>--请选择--</option></select></div>"
                        $("#address").append(str_div)
                        $.each(data[index_c].children[index_p].children, function (i, item) {
                            if (name == 0) {
                                var str = "<option value=" + item.id + " index=" + i + ">" + item.name + "</option>"
                                $("#city_add").append(str)
                            } else {
                                if (item.name == name) {
                                    var str = "<option selected='selected' value=" + item.id + " index=" + i + ">" + item.name + "</option>"
                                    $("#city_add").append(str)
                                } else {
                                    var str = "<option value=" + item.id + " index=" + i + ">" + item.name + "</option>"
                                    $("#city_add").append(str)
                                }
                            }

                        })

                    }
                }
        );
    }

</script>

</@macroCommon.html>