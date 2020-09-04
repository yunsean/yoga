<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<#setting number_format="##0.##">
<@html>
    <@head includeDate=true>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="权限管理" icon="icon-user">
            <@crumbItem href="#" name="角色管理" backLevel=1/>
            <@crumbItem href="#" name="角色授权" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "权限设置" />
                <@panelBody>
                    <@table>
                        <@thead>
                            <@tr>
                                <@th 10>版块</@th>
                                <@th 15>模块</@th>
                                <@th 75>权限</@th>
                            </@tr>
                        </@thead>
                        <@tbody>
                            <#list menuList as menu>
                            <tr style="background-color:#eaeaea">
                                <td><b style="font-size:14px" >${menu.name}</b></td>
                                <td><label class="checkbox-inline"><input type="checkbox" class="left_${menu_index}" sign="parent"> 全选</label></td>
                                <td><label class="checkbox-inline"><input type="checkbox" class="right_${menu_index}" sign="parent"> 全选</label></td>
                            </tr>
                                <#list menu.children! as child>
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
                        </@tbody>
                    </@table>
                    <@panelFooter>
                        <div class="col-sm-offset-12">
                            <button class="btn btn-default" onclick="save(${(roleId?c)!})">保存</button>
                            <button class="btn btn-default" onclick="javascript:history.back(-1)">取消</button>
                        </div>
                    </@panelFooter>
                </@panelBody>
            </@panel>
        </@bodyContent>
    </@bodyFrame>
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
            var checkBoxLength = $("input[type='checkbox']").length;
            var optionLength = $("option").length;
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

        function save(roleId) {
            var privileges = [];
            var checkboxLength = $("input[type='checkbox']").length;
            var selectLength = $("select").length;
            for (var i = 0; i < checkboxLength; i++) {
                if($("input[type='checkbox']").eq(i).attr("sign")=='child'){
                    if ($("input[type='checkbox']").eq(i).prop('checked')) {
                        privileges.push($("input[type='checkbox']").eq(i).attr("name"));
                    }
                }
            }
            $.post(
                    "/admin/operator/role/privilege.json",
                    $.param({privileges: privileges, id: roleId}, true),
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);
                        } else {
                            history.back();
                        }
                    },
                    "json"
            );
        }
    </script>
</@html>
