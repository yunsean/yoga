<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<#setting number_format="##0.##">
<@html>
<@head includeDate=true includeUploader=true>
    <style>
        td {
            vertical-align: middle!important;
        }
        .param {
            color: #aaa;
            font-size: 8px;
        }
        .sort {
            width: 100%;
            text-align: center;
        }
    </style>
</@head>
<@bodyFrame>
    <@crumbRoot name="内容管理" icon="icon-user">
        <@crumbItem href="#" name="文章模板" backLevel=1/>
        <@crumbItem href="#" name="字段管理" />
        <@backButton />
    </@crumbRoot>
    <@bodyContent>
        <@panel>
            <@panelHeading "模板字段管理" />
            <@panelBody>
                <@inlineForm class="margin-b-15">
                    <@formLabelGroup class="margin-r-15" label="关键字">
                        <@inputHidden name="templateId" value="${(param.templateId?c)!}" />
                        <@inputText name="name" value="${(param.name)!}" />
                    </@formLabelGroup>
                    <@inputSubmit text="搜索" icon="icon icon-search" class="btn btn-success"/>
                    <@rightAction>
                        <@shiro.hasPermission name="cms_template.update" >
                            <@inputButton text="添加" icon="icon-plus" class="btn btn-primary" onclick="doAdd();" />
                        </@shiro.hasPermission>
                    </@rightAction>
                </@inlineForm>
                <@table>
                    <@thead>
                        <@tr>
                            <@th 5 true>排序</@th>
                            <@th 15>字段名称</@th>
                            <@th 10 true>字段编码</@th>
                            <@th 10 true>字段类型</@th>
                            <@th 40>字段描述/参数</@th>
                            <@th 5 true>启用状态</@th>
                            <@th 15 true>操作</@th>
                        </@tr>
                    </@thead>
                    <tbody>
                    <#list fields as field>
                        <@tr>
                            <@td>
                                <@shiro.hasPermission name="cms_template.update" >
                                    <input class="sort" type="number" onblur="doSort(this);" onkeypress="if(event.keyCode==13) {doSort(this);return false;}" fieldId="${(field.id?c)!}" value="${(field.sort?c)!'0'}">
                                </@shiro.hasPermission>
                            </@td>
                            <@td>${field.name!}</@td>
                            <@td true>${field.code!}</@td>
                            <@td true>${(field.type.getName())!}</@td>
                            <@td>
                                ${field.hint!}
                                <#if field.param?? && (field.param?length > 0)>
                                    <div class="param"> ${field.param!} </div>
                                </#if>
                            </@td>
                            <@td true><img src="/admin/images/${(field.enabled?string("yes", "no"))!"no"}.png"></@td>
                            <@td true>
                                <@shiro.hasPermission name="cms_template.update" >
                                    <#if field.enabled>
                                        <a href="#" class="btn btn-warning btn-sm"
                                           onclick="doDisable(${(field.id?c)!}, 'true')">
                                            <i class="icon icon-arrow-down"></i>
                                        </a>
                                    <#else>
                                        <a href="#" class="btn btn-primary btn-sm"
                                           onclick="doDisable(${(field.id?c)!}, 'false')">
                                            <i class="icon icon-arrow-up"></i>
                                        </a>
                                    </#if>
                                    <a href="javascript:void(0)" onclick="doEdit(${(field.id?c)!})" class="btn btn-sm btn-primary">
                                        <i class="icon icon-edit"></i>编辑
                                    </a>
                                    <a href="javascript:void(0)" class="btn btn-sm btn-danger" onclick="doDelete(${(field.id?c)!})">
                                        <i class="icon icon-remove"></i>删除
                                    </a>
                                </@shiro.hasPermission>
                            </@td>
                        </@tr>
                    </#list>
                    </tbody>
                </@table>
            </@panelBody>
        </@panel>
    </@bodyContent>
</@bodyFrame>

<@modal title="字段编辑" showId="userAddButton" onOk="doSave">
    <@inputHidden name="id" id="edit_id"/>
    <@inputHidden name="templateId" value="${(param.templateId?c)!}"/>
    <@formText name="name" label="字段名称：" />
    <@formEnum name="type" label="字段类型：" enums=fieldTypes! onchange="onTypeChanged()" />
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">字段编码：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="field_code" name="code" style="display: block">
                <select class="form-control" style="display: none" id="field_property" onchange="onPropertyChanged()">
                    <#list properties! as op>
                        <option value="${op.code}">${op.name}</option>
                    </#list>
                </select>
            </div>
        </div>
    </div>
    <div class="modalForm" id="param_div" style="display: none">
        <div class="form-group">
            <label class="col-sm-3  control-label">字段参数：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" name="param">
                <div id="field_param_hint" style="color: red;">每个参数间请使用英文","区分。</div>
            </div>
        </div>
    </div>
    <div class="modalForm" id="placeholder_div" style="display: none">
        <div class="form-group">
            <label class="col-sm-3 control-label">缺省图片：</label>
            <div class="col-sm-8">
                <@inputImage name="placeholder" value=""  errsrc="/admin/images/default_field.png"/>
            </div>
        </div>
    </div>
    <div class="modalForm" id="hint_div" style="display: none">
        <div class="form-group">
            <label class="col-sm-3  control-label">字段提示：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" name="hint">
            </div>
        </div>
    </div>
    <@formTextArea name="remark" label="字段描述：" />
</@modal>

<script>
    function onTypeChanged() {
        var type = $("#add_form select[name='type']").val();
        console.log(type);
        if ('DROPDOWN' == type || 'CHECKDOWN' == type) {
            $("#field_property").css("display", "block")
            $("#field_code").css("display", "none")
            $("#param_div").css("display", "none")
            $("#field_code").val($("#field_property").val());
        } else if ('CHOICE' == type || 'CHECKBOX' == type) {
            $("#param_div").css("display", "block")
            $("#field_param_hint").text("用半角竖线(|)分割选项，用半角冒号(:)分割选项值和选项名称。比如：yes:是|no:否");
            $("#field_property").css("display", "none")
            $("#field_code").css("display", "block")
        } else if ('DOCUMENT' == type) {
            $("#param_div").css("display", "block")
            $("#field_param_hint").text("输入所有支持的扩展名，用半角逗号(,)分割。比如：jpg,gif,png");
            $("#field_property").css("display", "none")
            $("#field_code").css("display", "block")
        } else if ('HIDDEN' == type) {
            $("#param_div").css("display", "block")
            $("#field_param_hint").text("输入字段的取值");
            $("#field_property").css("display", "none")
            $("#field_code").css("display", "block")
        } else {
            $("#field_property").css("display", "none")
            $("#field_code").css("display", "block")
            $("#param_div").css("display", "none")
        }
        if ('IMAGE' == type) {
            $("#placeholder_div").css("display", "block")
        } else {
            $("#placeholder_div").css("display", "none")
        }
        if ('HIDDEN' == type) {
            $("#hint_div").css("display", "none")
        } else {
            $("#hint_div").css("display", "block")
        }
    }
    function onPropertyChanged() {
        var code = $("#field_property").val();
        $("#field_code").val(code);
    }
    function doDisable(id, on) {
        $.post(
            "/admin/cms/template/field/enable.json?id=" + id + "&disabled=" + on,
            function (result) {
                if (result.code < 0) {
                    alertShow("danger", result.message, 3000);
                } else {
                    window.location.reload();
                }
            }
        );
    }
    function doSave() {
        var id = $("#edit_id").val();
        $("#add_form input[name='templateId']").val('${(param.templateId?c)!}');
        var json = $("#add_form").serialize();
        $.post(id == 0 ? "/admin/cms/template/field/add.json" : "/admin/cms/template/field/update.json",
            json,
            function (data) {
                if (data.code < 0) {
                    alertShow("warning", data.message, 3000);
                } else {
                    $("#add_modal").modal("hide");
                    window.location.reload();
                }
            },
            "json"
        );
    }
    function doAdd() {
        $("#add_form")[0].reset();
        $("#add_form input[name='id']").val(0);
        $("#add_modal").modal("show");
    }
    function doEdit(id) {
        $("#add_form")[0].reset();
        $.get(
            "/admin/cms/template/field/get.json?id=" + id,
            function (data) {
                if (parseInt(data.code) < 0) {
                    alertShow("danger", data.message, 3000);
                } else {
                    $("#add_form input[name='id']").val(id);
                    $("#add_form input[name='name']").val(data.result.name);
                    $("#add_form input[name='code']").val(data.result.code);
                    $("#add_form input[name='hint']").val(data.result.hint);
                    $("#add_form select[name='type']").val(data.result.type);
                    $("#add_form textarea[name='remark']").val(data.result.remark);
                    $("#add_form input[name='param']").val(data.result.param);
                    $("#field_property").val(data.result.code);
                    onTypeChanged();
                    $("#add_modal").modal("show");
                }
            }
        );
    }
    function doDelete(id) {
        warningModal("确定要删除该服务吗？", function () {
            $.ajax({
                url: "/admin/cms/template/field/delete.json?id=" + id,
                type: 'DELETE',
                success: function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        window.location.reload();
                    }
                }
            });
        });
    }
    function doSort(data) {
        $.post("/admin/cms/template/field/sort.json",
            {
                id: data.getAttribute("fieldId"),
                sort: data.value
            },
            function (data) {
                if (data.code < 0) {
                    alertShow("warning", data.message, 3000);
                } else {
                    $("#add_modal").modal("hide");
                    window.location.reload();
                }
            },
            "json"
        );
    }
</script>
</script>
</@html>
