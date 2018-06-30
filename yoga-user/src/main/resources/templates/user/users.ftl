<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<#setting number_format="##0.##">
<@html>
    <@head includeDate=true>
    <link href="<@resource/>/uploader/zui.uploader.css" rel="stylesheet">
    <script src="<@resource/>/uploader/zui.js"></script>
    <script src="<@resource/>/uploader/zui.uploader.min.js"></script>
    <style>
        .col-center-block {
            float: none;
            display: block;
            margin-left: auto;
            margin-right: auto;
        }
        .minWidth100px {
            min-width: 100px;
        }
    </style>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="权限管理" icon="icon-user">
            <@crumbItem href="#" name="用户管理" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "用户信息列表" />
                <@panelBody>
                    <@inlineForm class="margin-b-15">
                        <@formLabelGroup class="margin-r-15" label="姓名">
                            <@inputText name="name" value="${param.name?if_exists}"/>
                        </@formLabelGroup>
                        <#if depts?exists && (depts?size > 0)>
                        <@formLabelGroup class="margin-r-15">
                            <@formLabel><@common.deptAlias/></@formLabel>
                            <@inputTree options=depts class="minWidth100px" name="deptId" value="${param.deptId?default('')}" blank="全部" blankValue=""/>
                        </@formLabelGroup>
                        </#if>
                        <#if duties?exists && (duties?size > 0)>
                        <@formLabelGroup class="margin-r-15">
                            <@formLabel><@common.dutyAlias/></@formLabel>
                            <@inputTree options=duties class="minWidth100px" name="dutyId" value="${param.dutyId?default('')}" blank="全部" blankValue=""/>
                        </@formLabelGroup>
                        </#if>
                        <@inputSubmit text="搜索" icon="icon icon-search" class="btn btn-success"/>
                        <@rightAction>
                            <@inputButton text="添加" icon="icon-plus" class="btn btn-primary" onclick="doAdd();" />
                        </@rightAction>
                    </@inlineForm>
                    <@table>
                        <@thead>
                            <@tr>
                                <@th 10>姓名</@th>
                                <@th 10>登录帐号</@th>
                                <#if deptMap?exists && (deptMap?size > 0)>
                                    <@th 10>所属<@common.deptAlias/></@th>
                                </#if>
                                <#if dutyMap?exists && (dutyMap?size > 0)>
                                    <@th 10><@common.dutyAlias/></@th>
                                </#if>
                                <@th 18>邮箱</@th>
                                <@th 12>电话</@th>
                                <@th center=true>操作</@th>
                            </@tr>
                        </@thead>
                    <tbody>
                        <#list users as user>
                            <@tr>
                            <td>${user.lastname?if_exists}${user.firstname?if_exists}</td>
                            <td>${user.username?lower_case?if_exists}</td>
                                <#if deptMap?exists && (deptMap?size > 0)>
                                <td>
                                    <#if user.deptId?exists && deptMap?exists && deptMap[user.deptId?c]?exists>
                                    ${deptMap[user.deptId?c].name!}
                                </#if>
                                </td>
                                </#if>
                                <#if dutyMap?exists && (dutyMap?size > 0)>
                                <td>
                                    <#if user.dutyId?exists && dutyMap?exists && dutyMap[user.dutyId?c]?exists>
                                        ${dutyMap[user.dutyId?c].name!}
                                    </#if>
                                </td>
                                </#if>
                            <td>${user.email?if_exists}</td>
                            <td>${user.phone?if_exists}</td>
                            <td class="tableCenter">
                                <@shiro.hasPermission name="pri_user.update" >
                                    <a href="javascript:void(0)" onclick="doEdit(${user.id?default(0)?c})"
                                       class="btn btn-info"><i class="icon icon-edit"></i>编辑</a>
                                </@shiro.hasPermission>
                                <a href="/privilege/userInfo?id=${user.id?default(0)?c}&pageIndex=${page.pageIndex?default(0)?c}"
                                   class="btn btn-primary"><i class="icon icon-th-list"></i>详情</a>
                                <@shiro.hasPermission name="pri_user.del" >
                                    <a href="javascript:void(0)" class="btn btn-danger"
                                       onclick="delUser(${user.id?default(0)?c}, ${page.pageIndex?default(0)?c})">
                                        <i class="icon icon-remove"></i>删除</a>
                                </@shiro.hasPermission>
                            </td>
                            </@tr>
                        </#list>
                    </tbody>
                    </@table>
                </@panelBody>
                <@panelPageFooter action="/privilege/users" />
            </@panel>
        </@bodyContent>
    </@bodyFrame>

    <@modal title="用户" showId="userAddButton" onOk="saveUser">
        <@inputHidden name="id" id="edit_id"/>
        <div class="center-block" style="width: 128px;">
            <input type="hidden" name="avatar" id="avatar">
            <div class="col-center-block">
                <img id="avatarImage" style="width: 128px; height: 128px; object-fit: cover; object-position: center; " class="center-block">
            </div>
            <div id="myUploader" class="uploader" data-ride="uploader">
                <div id="new_avatar" class="uploader-files file-list file-list-grid" style="display: none;"></div>
                <button type="button" class="btn btn-primary uploader-btn-browse" style="width: 98px; margin-left: 15px; margin-top: 5px">
                    <i class="icon icon-plus"></i>上传头像
                </button>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">姓：</label>
            <div class="col-sm-3">
                <@inputText name="lastname" />
            </div>
            <label class="col-sm-2 control-label">名<span class="point">*</span>：</label>
            <div class="col-sm-3">
                <@inputText name="firstname" />
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">用户名<span class="point">*</span>：</label>
            <div class="col-sm-3">
                <@inputText name="username" />
            </div>
            <label class="col-sm-2 control-label">密码<span class="point">*</span>：</label>
            <div class="col-sm-3">
                <@inputText name="password" />
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">性别：</label>
            <div class="col-sm-3">
                <select class="form-control" name="gender">
                    <option value="unknown">保密</option>
                    <option value="male">男</option>
                    <option value="female">女</option>
                </select>
            </div>
            <label class="col-sm-2 control-label">手机号码：</label>
            <div class="col-sm-3">
                <@inputText name="phone" />
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">邮箱：</label>
            <div class="col-sm-3">
                <@inputText name="email" />
            </div>
            <label class="col-sm-2 control-label">QQ号码：</label>
            <div class="col-sm-3">
                <@inputText name="qq" />
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-1"></div>
            <#if depts?exists && (depts?size > 0)>
                <label class="col-sm-2 control-label"><@common.deptAlias/>：</label>
                <div class="col-sm-3">
                    <@inputTree options=depts name="deptId" />
                </div>
            </#if>
            <#if duties?exists && (duties?size > 0)>
                <label class="col-sm-2 control-label"><@common.dutyAlias/>：</label>
                <div class="col-sm-3">
                    <@inputTree options=duties name="dutyId" zero="默认" />
                </div>
            </#if>
        </div>
        <#if roles?exists && (roles?size > 0)>
        <div class="modalForm">
            <@formGroup>
                <@formLabel class="col-sm-3"><@common.roleAlias/>:</@formLabel>
                <@formInputArea class="col-sm-8">
                    <@inputCheckboxGroup options=roles name="roleIds"/>
                </@formInputArea>
            </@formGroup>
        </div>
        </#if>
        <div class="modalForm" id="edit_reload_div">
            <div class="form-group">
                <div style="float: right; margin-right: 20px;">
                    <input id="edit_reload" type="checkbox" checked="checked">
                    <label for="edit_reload">保存成功后刷新页面</label>
                </div>
            </div>
        </div>
    </@modal>
<script>
    $('#myUploader').uploader({
        multi_selection: false,
        file_data_name: 'file',
        mime_types: [
            {title: '图片', extensions: 'jpg,gif,png'}
        ],
        autoUpload: true,            // 当选择文件后立即自动进行上传操作
        url: '${uploadPath?if_exists}',     // 文件上传提交地址
        previewImageSize: {width: 80, height: 100},
        deleteActionOnDone: function (file, doRemoveFile) {
            return true;
        }
    }).on('onFileUploaded', function(event, file, result) {
        var uploader = $('#myUploader').data('zui.uploader');
        var plupload = uploader.plupload;
        var files = plupload.files;
        for (var i = 0; i < files.length; i++) {
            if (files[i].id != file.id) uploader.removeFile(files[i]);
        }
        $("#avatarImage").attr("src", file.url);
        $("#avatar").val(file.url);
    });

    function saveUser() {
        var id = $("#edit_id").val();
        var password = $("#add_form input[name='password']").val();
        var reload = $("#edit_reload").prop("checked");
        setCookie("reload", reload);
        var json = $("#add_form").serializeArray();
        $.post(id == 0 ? "/api/user/add" : "/api/user/update",
                json,
                function (data) {
                    if (data.code < 0) {
                        alertShow("warning", data.message, 3000);
                        return;
                    } else {
                        $("#add_modal").modal("hide");
                        if (reload) window.location.reload();
                    }
                },
                "json"
        );
    }
    function delUser(id) {
        warningModal("确定要删除该用户吗？", function () {
            $.post(
                    "/api/user/delete",
                    {id: id},
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);
                            return;
                        } else {
                            window.location.reload();
                        }
                    },
                    "json"
            );
        });
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
        return "";
    }
    function setCookie(c_name, value, expiredays) {
        var exdate = new Date();
        exdate.setDate(exdate.getDate() + expiredays);
        document.cookie = c_name + "=" + escape(value) + ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString());
    }
    function clearModal() {
        $("#add_form")[0].reset();
        var uploader = $('#myUploader').data('zui.uploader');
        var plupload = uploader.plupload;
        var files = plupload.files;
        for (var i = 0; i < files.length; i++) {
            uploader.removeFile(files[i]);
        }
        $("#avatarImage").attr("src", "/user/default_avatar.jpg");
        $("#avatar").val("");
    }
    function doAdd() {
        clearModal();
        $("#add_modal").modal("show");
    }
    function doEdit(id) {
        clearModal();
        $.get(
                "/api/user/info?id=" + id,
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#add_form input[name='id']").val(id);
                        $("#add_form input[name='lastname']").val(data.result.lastname);
                        $("#add_form input[name='firstname']").val(data.result.firstname);
                        $("#add_form input[name='username']").val(data.result.username);
                        $("#add_form input[name='password']").val();
                        <#if depts?exists && (depts?size > 0)>
                            $("#add_form select[name='deptId']").val(data.result.departmentId);
                        </#if>
                        <#if duties?exists && (duties?size > 0)>
                            $("#add_form select[name='dutyId']").val(data.result.dutyId);
                        </#if>
                        data.result.roles.forEach(function (self) {
                            $("#add_form input[name='roleIds'][value='" + self + "']").prop("checked", "checked");
                        });
                        $("#add_form select[name='gender']").val(data.result.gender);
                        $("#add_form input[name='phone']").val(data.result.phone);
                        $("#add_form input[name='email']").val(data.result.email);
                        $("#add_form input[name='qq']").val(data.result.qq);
                        if (data.result.avatar == '') $("#avatarImage").attr("src", "/user/default_avatar.jpg");
                        else $("#avatarImage").attr("src", data.result.avatar);
                        var reload = getCookie("reload");
                        $("#edit_reload").prop("checked", reload == "false" ? false : true);
                        $("#add_modal").modal("show");
                    }
                }
        );
    }
</script>
</@html>
