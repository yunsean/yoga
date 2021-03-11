<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<#include "./actions.ftl">
<@html>
    <@head includeDate=true includeUploader=true>
        <style>
            td {
                vertical-align: middle!important;
            }
            .small {
                color: #aaa!important;
                font-size: 8px;
                overflow: hidden;
                text-overflow: ellipsis;
                display: -webkit-box;
                -webkit-line-clamp: 2;
                -webkit-box-orient: vertical;
            }
        </style>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="积分管理" icon="icon-user">
            <@crumbItem href="#" name="积分调整" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "积分调整" />
                <@panelBody>
                    <@inlineForm class="margin-b-15" id="filter_form">
                        <@formLabelGroup class="margin-r-15" label="开始日期：">
                            <@inputDate name="beginDate" value="${(param.beginDate?string('yyyy-MM-dd'))!}" placeholder="开天辟地" />
                        </@formLabelGroup>
                        <@formLabelGroup class="margin-r-15" label="结束日期：">
                            <@inputDate name="endDate" value="${(param.endDate?string('yyyy-MM-dd'))!}" placeholder="海枯石烂" />
                        </@formLabelGroup>
                        <#if branches??>
                            <@formLabelGroup class="margin-r-15" label="部门">
                                <select class="form-control" name="branchId" id="dept_select">
                                    <option value="">全部</option>
                                    <#list branches! as root>
                                        <@m1_columns root 0 root_index/>
                                    </#list>
                                </select>
                                <script>
                                    $("#dept_select").val('${(param.branchId?c)!}');
                                </script>
                            </@formLabelGroup>
                        </#if>
                        <@formLabelGroup class="margin-r-15" label="职级">
                            <@inputDropdown name="dutyId" value="${(param.dutyId?c)!}">
                                <option value="">全部</option>
                                <#list duties as duty>
                                    <option value="${(duty.id?c)!}">${(duty.name)!}</option>
                                </#list>
                            </@inputDropdown>
                        </@formLabelGroup>
                        <@formLabelGroup class="margin-r-15" label="关键字">
                            <@inputText name="keyword" value="${(param.keyword)!}"/>
                        </@formLabelGroup>
                        <div class="form-group">
                            <button type="submit" class="btn btn-success">
                                <i class="icon icon-search"></i>搜索
                            </button>
                        </div>

                        <@rightAction>
                            <@shiro.hasPermission name="points_adjust.update" >
                                <@inputButton text="添加" icon="icon-plus" class="btn btn-primary" onclick="doAdd();" />
                            </@shiro.hasPermission>
                        </@rightAction>
                    </@inlineForm>
                    <@table>
                        <@thead>
                            <@tr>
                                <@th 10>人员</@th>
                                <@th 10 true>部门</@th>
                                <@th 10 true>时间</@th>
                                <@th 10 true>分值</@th>
                                <@th>调整原因</@th>
                                <@th 10 true>提交人</@th>
                                <@th 10 true>操作</@th>
                            </@tr>
                        </@thead>
                        <tbody>
                        <#list adjusts as adjust>
                            <tr>
                                <@td>${adjust.nickname!}</@td>
                                <@td true>${adjust.branch!}</@td>
                                <@td true><#if adjust.addTime??>${(adjust.addTime?string("yyyy-MM-dd"))!}</#if></@td>
                                <@td true>${((adjust.points / 100.0)?string('0.#'))!}</@td>
                                <@td>${adjust.reason!}</@td>
                                <@td true>${adjust.submitter!}</@td>
                                <@td true>
                                    <@shiro.hasPermission name="points_adjust.update" >
                                        <button class="btn btn-sm btn-danger" onclick="doDelete(${adjust.id?c})">
                                            <i class="icon icon-remove "></i>删除
                                        </button>
                                    </@shiro.hasPermission>
                                </@td>
                            </tr>
                        </#list>
                        </tbody>
                    </@table>
                </@panelBody>
                <@panelPageFooter action="/admin/points/adjust" />
            </@panel>
        </@bodyContent>
    </@bodyFrame>
    <@userChoice/>

    <#macro m1_column column level index>
        <option value="${(column.id?c)!}">
            <#list 0..level as x><#if x < level>┃&nbsp;&nbsp;<#else>┠</#if></#list>${(column.name)!}
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

    <@modal title="积分调整" onOk="doSave">
        <div class="form-group">
            <label class="col-sm-3 control-label">调整对象：</label>
            <div class="col-sm-8">
                <input class="form-control" id="agent-input" type="text" autocomplete="off"/>
                <input name="userId" type="hidden" id="agent-value"/>
                <div class="autocomplete-dropdown" id="agent-result" style="display: none;"></div>
            </div>
        </div>
        <@formText name="points" label="调整分值：" />
        <@formDate name="date" label="调整日期："/>
        <@formTextArea name="reason" label="调整原因："/>
    </@modal>

    <script>
        $(function () {
            setupAutoCompleteUser($("#agent-input"), $("#agent-result"), $("#agent-value"), "agent-table", '/admin/operator/user/list.json?filter=');
        });

        function doAdd(belowId, level) {
            $("#add_form")[0].reset();
            $("#add_modal").modal("show");
        }
        function doSave() {
            var json = $("#add_form").serialize();
            $.post("/admin/points/adjust/add.json",
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
        function doDelete(id) {
            warningModal("确定要撤销该调整项吗？", function () {
                $.ajax({
                    url: "/admin/points/adjust/repeal.json?id=" + id,
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
    </script>
</@html>