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
            .img {
                width: 44px;
            }
            .number {
                width: 60%;
                text-align: center
            }
        </style>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="评论管理" icon="icon-user">
            <@crumbItem href="#" name="评论审核" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "评论审核" />
                <@panelBody>
                    <@inlineForm class="margin-b-15">
                        <@formLabelGroup class="margin-r-15" label="关键字">
                            <@inputText name="filter" value="${(param.filter)!}"/>
                        </@formLabelGroup>
                        <div class="form-group margin-r-15">
                            <label class="control-label formLabelBold margin-r-5" for="">发布状态</label>
                            <select class="form-control" name="issued" id="issue_select">
                                <option value="">全部</option>
                                <option value="true">已发布</option>
                                <option value="false">未发布</option>
                            </select>
                            <script>
                                $("#issue_select").val('${(param.issued?string('true', 'false'))!''}');
                            </script>
                        </div>
                        <@inputSubmit text="搜索" icon="icon icon-search" class="btn btn-success"/>
                    </@inlineForm>
                    <form id="goods_form" onsubmit="#">
                    <@table>
                        <@thead>
                            <@tr>
                                <@th 5 true><@inputCheckbox id="checkAll" text="#" value="1" onchange="doCheckAll()"/></@th>
                                <@th 7>评论用户</@th>
                                <@th 20>评论文章</@th>
                                <@th 50>评论内容</@th>
                                <@th 5 true>发布状态</@th>
                                <@th 13 true>操作</@th>
                            </@tr>
                        </@thead>
                    <tbody id="goods_table">
                        <#list comments as comment>
                            <@tr>
                                <@td true><@inputCheckbox name="id" text="${(comment.id?c)!}" value="${(comment.id?c)!}"/></@td>
                                <@td>${comment.replierName!}</@td>
                                <@td>${comment.articleTitle!}</@td>
                                <@td>${comment.content!}</@td>
                                <@td true><img src="/admin/images/${(comment.issued?string("yes", "no"))!"no"}.png"></@td>
                                <@td true>
                                    <@shiro.hasPermission name="cms_comment.audit" >
                                        <#if comment.issued>
                                            <a href="#" class="btn btn-warning btn-sm"
                                               onclick="doIssue(${(comment.id?c)!}, 'false')">
                                                <i class="icon icon-arrow-down"></i>取消发布
                                            </a>
                                        <#else>
                                            <a href="#" class="btn btn-primary btn-sm"
                                               onclick="doIssue(${(comment.id?c)!}, 'true')">
                                                <i class="icon icon-arrow-up"></i>发布
                                            </a>
                                        </#if>
                                        <a href="javascript:void(0)" class="btn btn-sm btn-danger" onclick="doDelete(${(comment.id?c)!})">
                                            <i class="icon icon-remove"></i>删除
                                        </a>
                                    </@shiro.hasPermission>
                                </@td>
                            </@tr>
                        </#list>
                    </tbody>
                    </@table>
                        <div class="col-sm-offset-12" style="text-align:center">
                            <a class="btn btn-danger" onclick="doIssue2('false')">取消发布</a>
                            <a class="btn btn-primary" onclick="doIssue2('true')" style="margin-left: 30px">发布</a>
                        </div>
                    </form>
                </@panelBody>
                <@panelPageFooter action="/admin/cms/template" />
            </@panel>
        </@bodyContent>
    </@bodyFrame>

<script>

    function doIssue2(issued) {
        var comments = [];
        $("#goods_table").find("tr").each(function () {
            var tds = $(this).children();
            var checked = tds.eq(0).find('input').prop('checked');
            if (checked) comments.push(tds.eq(0).find('input').val());
        })
        if (comments.length < 1) {
            alertShow("warning", "请选择需要操作的评论", 3000);
            return
        }
        $.ajax({
            type: "post",
            url: "/admin/cms/comment/issues.json",
            data: JSON.stringify({
                ids: comments,
                issued: issued
            }),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                if (data.code < 0) {
                    alertShow("warning", data.message, 3000);
                } else {
                    window.location.reload();
                }
            }
        });
    }
    function doCheckAll() {
        var checked = $("#checkAll").prop('checked');
        $.each($("#goods_table input[name='id']"), function (i, id) {
            $(id).prop("checked", checked);
        })
    }
    function doIssue(id, on) {
        $.post(
            "/admin/cms/comment/issue.json?id=" + id + "&issued=" + on,
            function (result) {
                if (result.code < 0) {
                    alertShow("danger", result.message, 3000);
                } else {
                    window.location.reload();
                }
            }
        );
    }
    function doDelete(id) {
        warningModal("确定要删除该评论吗？", function () {
            $.ajax({
                url: "/admin/cms/comment/delete.json?id=" + id,
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
</script>
</@html>
