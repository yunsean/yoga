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
            .minHeight120px {
                min-height: 120px!important;
            }
            .bgwhite {
                background-color: white!important;
            }
        </style>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="系统设置" icon="icon-user">
            <#if param.backPage! != ''>
            <@crumbItem href="#" name="${(param.backPage)!}" backLevel=1/>
            </#if>
            <@crumbItem href="#" name="操作日志" />
            <#if param.backPage! != ''>
            <@backButton />
            </#if>
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "操作日志" />
                <@panelBody>
                <#if param.backPage! == ''>
                    <@inlineForm class="margin-b-15">
                        <@formLabelGroup class="margin-r-15" label="模块：">
                            <@inputListMap options=modules name="module" value="${(param.module)!}" blank="全部"/>
                        </@formLabelGroup>
                        <@formLabelGroup class="margin-r-15" label="关键字：">
                            <@inputText name="filter" value="${(param.filter)!}" placeholder="方法/主记录/描述等"/>
                        </@formLabelGroup>
                        <@formLabelGroup class="margin-r-15" label="开始日期：">
                            <@inputDate name="beginDate" value="${(param.beginDate?string('yyyy-MM-dd'))!}" placeholder="开天辟地" />
                        </@formLabelGroup>
                        <@formLabelGroup class="margin-r-15" label="结束日期：">
                            <@inputDate name="endDate" value="${(param.endDate?string('yyyy-MM-dd'))!}" placeholder="海枯石烂" />
                        </@formLabelGroup>
                        <@inputSubmit text="搜索" icon="icon icon-search" class="btn btn-success"/>
                    </@inlineForm>
                </#if>
                    <@table>
                        <@thead>
                            <@tr>
                                <@th 15>日志时间</@th>
                                <@th 10>所属模块</@th>
                                <@th 10>操作人</@th>
                                <@th 20>主键信息</@th>
                                <@th 35>操作描述</@th>
                                <@th center=true>操作</@th>
                            </@tr>
                        </@thead>
                    <tbody>
                        <#list loggings as logging>
                            <@tr>
                            <td>${(logging.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                            <td><#if (modules[logging.module!])??>${(modules[logging.module!])!}<#else>${logging.module!}</#if></td>
                            <td>${logging.username!}<#if logging.userId??>（${logging.userId!}）</#if></td>
                            <td>${logging.primaryId!}<#if logging.primaryInfo??>（${logging.primaryInfo!}）</#if></td>
                            <td>${logging.description!}</td>
                            <td class="tableCenter">
                                <a href="javascript:void(0)" onclick="doDetail(${(logging.logId?c)!})" class="btn btn-sm btn-info">
                                    <i class="icon icon-edit"></i>详情
                                </a>
                            </td>
                            </@tr>
                        </#list>
                    </tbody>
                    </@table>
                </@panelBody>
                <@panelPageFooter action="/admin/logging/list" />
            </@panel>
        </@bodyContent>
    </@bodyFrame>

    <@modal title="管理员编辑" cancel="关闭">
        <div class="form-group">
            <label class="col-sm-4 control-label">日志时间：</label>
            <div class="col-sm-6">
                <@inputText name="createTime" class="bgwhite" readonly=true/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">所属模块：</label>
            <div class="col-sm-6">
                <@inputText name="module" class="bgwhite" readonly=true/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">主键信息：</label>
            <div class="col-sm-6">
                <@inputText name="primaryInfo" class="bgwhite" readonly=true/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">操作人：</label>
            <div class="col-sm-6">
                <@inputText name="username" class="bgwhite" readonly=true/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">描述信息：</label>
            <div class="col-sm-6">
                <@inputText name="description" class="bgwhite" readonly=true/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">函数式：</label>
            <div class="col-sm-6">
                <@inputText name="method" class="bgwhite" readonly=true/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">返回值：</label>
            <div class="col-sm-6">
                <@inputText name="result" class="bgwhite" readonly=true/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">日志详情：</label>
            <div class="col-sm-6">
                <@inputTextArea name="detail" class="bgwhite minHeight120px" readonly=true/>
            </div>
        </div>
    </@modal>
<script>
    function doDetail(id) {
        $("#add_form")[0].reset();
        $.get(
                "/admin/system/logging/get.json?id=" + id,
                function (data) {
                    if (parseInt(data.code) < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#add_form input[name='createTime']").val(data.result.createTime);
                        $("#add_form input[name='username']").val(data.result.username ? data.result.username : '' + (data.result.userId ? (" (" + data.result.userId + ")") : ''));
                        $("#add_form input[name='module']").val(data.result.moduleName);
                        $("#add_form input[name='description']").val(data.result.description);
                        $("#add_form input[name='method']").val(data.result.method);
                        $("#add_form input[name='result']").val(data.result.result);
                        $("#add_form textarea[name='detail']").val(data.result.detail);
                        $("#add_form input[name='primaryInfo']").val(data.result.primaryId?data.result.primaryId:'' + " （" + data.result.primaryInfo + "）");
                        $("#add_modal").modal("show");
                    }
                }
        );
    }
</script>
</@html>
