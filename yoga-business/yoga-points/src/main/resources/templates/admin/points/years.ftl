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
            <@crumbItem href="#" name="年度管理" backLevel=1/>
            <@backButton/>
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "年度管理" />
                <@panelBody>
                    <@inlineForm class="margin-b-15">
                        <@rightAction>
                            <@inputButton text="添加" icon="icon-plus" class="btn btn-primary" onclick="doAdd()" />
                        </@rightAction>
                    </@inlineForm>
                    <@table>
                        <@thead>
                            <@tr>
                                <@th 20>年度</@th>
                                <@th 20 true>开始日期</@th>
                                <@th 20 true>结束日期</@th>
                                <@th 10 true>操作</@th>
                            </@tr>
                        </@thead>
                        <tbody>
                        <#list years as year>
                            <tr>
                                <@td>${(year.year?c)!}</@td>
                                <@td true><#if year.beginDate??>${year.beginDate?string("yyyy-MM-dd")}</#if></@td>
                                <@td true><#if year.endDate??>${year.endDate?string("yyyy-MM-dd")}</#if></@td>
                                <@td true>
                                    <@shiro.hasPermission name="points_summary.year" >
                                        <button class="btn btn-sm btn-primary" onclick="doEdit(${(year.id?c)!})">
                                            <i class="icon icon-remove "></i>编辑
                                        </button>
                                    </@shiro.hasPermission>
                                    <a href="#" target="_blank" onclick="doExport(this, ${year.year?c});"
                                       class="btn btn-sm btn-info">
                                        <i class="fa fa-fw  fa-download"></i>积分报表
                                    </a>
                                </@td>
                            </tr>
                        </#list>
                        </tbody>
                    </@table>
                </@panelBody>
                <@panelPageFooter action="/admin/points/years" />
            </@panel>
        </@bodyContent>
    </@bodyFrame>

    <@modal title="积分年度" onOk="doSave">
        <@inputHidden name="id" id="edit_id"/>
        <@formText name="year" label="年度年份：" />
        <@formDate name="beginDate" label="开始日期："/>
        <@formDate name="endDate" label="结束日期："/>
    </@modal>

    <script>
        function doAdd(belowId, level) {
            $("#add_form")[0].reset();
            $("#add_form input[name='id']").val(0);
            $("#add_modal").modal("show");
        }
        function doEdit(id) {
            $("#add_form")[0].reset();
            $.get(
                "/admin/points/summary/year/get.json?id=" + id,
                function (data) {
                    if (parseInt(data.code) < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#add_form input[name='id']").val(id);
                        $("#add_form input[name='year']").val(data.result.year);
                        $("#add_form input[name='beginDate']").val(data.result.beginDate);
                        $("#add_form input[name='endDate']").val(data.result.endDate);
                        $("#add_modal").modal("show");
                    }
                }
            );
        }
        function doSave() {
            var id = $("#edit_id").val();
            var json = $("#add_form").serialize();
            $.post(id == 0 ? "/admin/points/summary/year/add.json" : "/admin/points/summary/year/update.json",
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
    </script>
</@html>