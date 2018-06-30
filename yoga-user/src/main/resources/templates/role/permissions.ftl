<#import "../macro_common.ftl" as macroCommon>
<!DOCTYPE html>
<html>
<@macroCommon.html/>
<link href="../css/zui.min.css" rel="stylesheet">
<body>
<div class="container-fluid">
    <!-- Content Wrapper. Contains tenants content -->
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i> 权限管理</a></li>
            <li><a href="#"><@common.roleAlias/>管理</a></li>
            <li>权限设定</li>
            <span class="pull-right">
                <button class="btn btn-primary btn-sm" onclick="history.back();">返回</button>
            </span>
        </ol>
    </div>
    <div class="row content-bottom">
        <div class="col-sm-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="fa fa-comments-o"></i>
                <@common.roleAlias/>权限
                </div>
                <div class="panel-body">
                    <table id="" class="table table-bordered ">
                        <thead>
                        <tr>
                            <th style="width:10">版块</th>
                            <th style="width:15%">模块</th>
                            <th style="width:75%">权限</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list menuList as menu>
                        <tr style="background-color:#eaeaea">
                            <td><b style="font-size:14px" >${menu.name}</b></td>
                            <td><label class="checkbox-inline"><input type="checkbox"  class="left_${menu_index}" sign="parent"> 全选</label></td>
                            <td><label class="checkbox-inline"><input type="checkbox" class="right_${menu_index}" sign="parent"> 全选</label></td>
                        </tr>
                            <#list menu.children as child>
                            <tr>
                                <td></td>
                                <td>
                                    <label>
                                        <#if child.url?if_exists?length gt 0>
                                            <input name="${child.code}"  type="checkbox" value="" parentIndex="left_${menu_index}" class="left_${menu_index}_${child_index}" sign="child">&nbsp;${child.name}
                                        <#else>
                                            <i class="icon icon-circle-blank"></i>&nbsp;${child.name}
                                        </#if>
                                    </label>
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                </td>
                                <td>
                                    <#if child.children??>
                                        <#list child.children as permission>
                                            <#assign code = permission.code/>
                                            <label><input name="${permission.code}" type="checkbox" parentIndex="right_${menu_index}" class="right_${menu_index}_${child_index}" sign="child"
                                                          value="${code?replace(".", "_")}">&nbsp;${permission.name}</label>&nbsp;&nbsp;&nbsp;&nbsp;
                                        </#list>
                                    </#if>
                                </td>
                            </tr>
                            </#list>
                        </#list>
                        </tbody>
                    </table>
                    <div class="box-footer">
                        <div class="col-sm-5 col-sm-offset-5">
                            <button class="btn btn-default"
                                    onclick="modifyPermission(${roleId?default(0)?c}, ${pageIndex?default(0)})">确认修改
                            </button>
                            <button class="btn btn-default" onclick="javascript:history.back(-1)">取消</button>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
<footer class="main-footer">
<@common.footer/>
</footer>
</div>
</body>
<script>
    $(document).ready(function () {
        permissions();
        $("input[type='checkbox']").on("change", function () {
            if ($(this).attr("checked")) {
                $(this).removeAttr("checked");
            } else {
                $(this).attr("checked", true);
            }
        });
       $.each($("input[sign='parent']"),function (i,n) {
           var parentIndex=$(n).attr("class");
           var sign=true;
           $.each($("input[parentIndex="+parentIndex+"]"),function (x,y) {
               if(!$(y).prop('checked')){
                   sign=false;
               }
           });
           if(sign==true){
               $(n).prop("checked", 'true');
           }else {
               $(n).removeAttr("checked");
           }
       });
        $("input[sign='parent']").on("click",function () {
            var checkPar=$(this);
            var parent=$(this).attr("class");
            $.each($("input[parentIndex="+parent+"]"),function (i,n) {
                if(checkPar.prop('checked')){
                    $(n).prop("checked",'true');
                }else {
                    $(n).removeAttr("checked");
                }
            });
        });
        $("input[sign='child']").on("click",function () {
            var parentIndex=$(this).attr("parentIndex");
            var sign=true;
            $.each($("input[parentIndex="+parentIndex+"]"),function (i,n) {
                if(!$(n).prop('checked')){
                    sign=false;
                }
            });
            if(sign==true){
                $("."+parentIndex).prop("checked", 'true');
            }else {
                $("."+parentIndex).removeAttr("checked");
            }
        });
    });

    function permissions() {
        var permissions = [];
    <#list permissions as permission>
        <#if permission?exists>
            permissions.push("${permission}");
        </#if>
    </#list>
        var checkBoxLength = $("input[type='checkbox']").size();
        var optionLength = $("option").size();
        for (var i = 0; i < checkBoxLength; i++) {
            var checkboxName = $("input[type='checkbox']").eq(i).attr("name");
            if (permissions.indexOf(checkboxName) != -1) {
                $("input[type='checkbox']").eq(i).attr("checked", true);
            } else {
                $("input[type='checkbox']").eq(i).removeAttr("checked");
            }
        }
        for (var j = 0; j < optionLength; j++) {
            var selectName = $("option").eq(j).attr("name");
            if (permissions.indexOf(selectName) != -1) {
                $("option").eq(j).attr("selected", "selected");
            } else {
                $("option").eq(j).removeAttr("selected");
            }
        }
    }

    function modifyPermission(roleId, pageIndex) {
        var privileges = [];
        var checkboxLength = $("input[type='checkbox']").size();
        var selectLength = $("select").size();
        for (var i = 0; i < checkboxLength; i++) {
            if($("input[type='checkbox']").eq(i).attr("sign")=='child'){
                if ($("input[type='checkbox']").eq(i).prop('checked')) {
                    privileges.push($("input[type='checkbox']").eq(i).attr("name"));
                }
            }
        }
        $.post(
                "/api/role/permission/set",
                $.param({"privileges": privileges, roleId: roleId}, true),
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        window.location.href = "/privilege/roles?pageIndex=" + pageIndex;
                    }
                },
                "json"
        );
    }
</script>
</html>
