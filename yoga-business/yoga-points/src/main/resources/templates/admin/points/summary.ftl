<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
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
            <@crumbItem href="#" name="积分统计" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "积分统计" />
                <@panelBody>
                    <@inlineForm class="margin-b-15" id="filter_form">
                        <@formLabelGroup class="margin-r-15" label="积分年度">
                            <@inputDropdown name="year" value="${(param.year?c)!}">
                                <#list years as year>
                                    <option value="${(year?c)!}">${(year?c)!}年</option>
                                </#list>
                            </@inputDropdown>
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
                        <@formLabelGroup class="margin-r-15" label="">
                            <@inputCheckbox text="仅显示含扣分人员" name="penaltyOnly" checked="${(param.penaltyOnly?string('checked', ''))!}" />
                        </@formLabelGroup>

                        <div class="form-group">
                            <button type="submit" class="btn btn-success">
                                <i class="icon icon-search"></i>搜索
                            </button>                            &nbsp;&nbsp;
                            <button onclick="doExport();" type="button" class="btn  btn-info downloadButton">
                                <i class="icon icon-download"></i>导出报表
                            </button>
                        </div>

                        <@rightAction>
                            <@shiro.hasPermission name="points_summary.year" >
                                <a type="input" class="btn btn-success" href="/admin/points/summary/years">
                                    <i class="fa fa-fw icon-list"></i>年度管理
                                </a>
                            </@shiro.hasPermission>
                            <@shiro.hasPermission name="points_summary.update" >
                                <a type="input" onclick="doStatistic()" class="btn btn-outline">
                                    <i class="fa fa-fw icon-refresh"></i>立即刷新
                                </a>
                            </@shiro.hasPermission>
                        </@rightAction>
                    </@inlineForm>
                    <#if progress?? && progress?length gt 0>
                        <div style="color: red">${year.year?c}年度，积分正在更新中(${progress?if_exists})...</div>
                    <#elseif year?? && year.updateTime??>
                        <div>${year.year?c}年度，积分数据更新于${year.updateTime?string("yyyy-MM-dd HH:mm")}</div>
                    </#if>
                    <@table>
                        <@thead>
                            <@tr>
                                <@th 10>总/部门/职级排名</@th>
                                <@th 10 true>人员</@th>
                                <@th 10 true>部门</@th>
                                <@th 10 true>职级</@th>
                                <@th 10 true>分值</@th>
                                <@th 5 true>有扣分项</@th>
                                <@th>明细</@th>
                            </@tr>
                        </@thead>
                        <tbody>
                        <#list pointses as points>
                            <tr>
                                <@td>&nbsp;${(points.yearRank?c)!'0'}&nbsp;&nbsp;/&nbsp;<#if points.branchRank?? && (points.branchRank gt 0)>&nbsp;${(points.branchRank?c)!'0'}&nbsp;<#else>&nbsp;&nbsp;</#if>&nbsp;/&nbsp;<#if points.dutyRank?? && (points.dutyRank gt 0)>&nbsp;${(points.dutyRank?c)!'0'}&nbsp;<#else>&nbsp;&nbsp;</#if></@td>
                                <@td true>${points.nickname!}</@td>
                                <@td true>${points.branch!}</@td>
                                <@td true>${points.duty!}</@td>
                                <@td true>${(points.points / 100)?string('0.#')}</@td>
                                <@td true>${(points.penalty / 100)?string('0.#')}</@td>
                                <@td>${(points.detail?replace('\r\n', '<br>' ))!}</@td>
                            </tr>
                        </#list>
                        </tbody>
                    </@table>
                </@panelBody>
                <@panelPageFooter action="/admin/points/summary" />
            </@panel>
        </@bodyContent>
    </@bodyFrame>

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

    <@modal title="刷新统计数据" onOk="statistic">
        <div class="modalForm">
            <div class="form-group">
                <label class="col-sm-4 control-label">刷新年度</label>
                <div class="col-sm-4">
                    <select class="form-control" name="statistic_year" id="statistic_year">
                        <#list years as year>
                            <option value="${year?c}">${year?c}年</option>
                        </#list>
                    </select>
                </div>
            </div>
        </div>
    </@modal>

    <script>
        function doStatistic() {
            $("#add_form")[0].reset();
            $("#add_modal").modal("show");
        }
        function statistic() {
            var year = $("#statistic_year").val();
            $("#add_modal").modal("hide");
            warningModal("确定要立即汇总积分数据吗？这将需要较长的时间！", function () {
                $.post("/admin/points/summary/statistic.json",
                    {
                        year: year
                    },
                    function (data) {
                        if (data.code < 0) {
                            alertShow("warning", data.message, 3000);
                        } else {
                            alertShow("info", "提交请求成功，请过一段时间后重新进入页面查看统计数据！", 3000);
                        }
                    },
                    "json"
                );
            });
        }

        function doExport() {
            $("#filter_form").attr("action", "/admin/points/summary/export");
            $("#filter_form").submit();
            $("#filter_form").attr("action", "/admin/points/summary");
        }
    </script>
</@html>