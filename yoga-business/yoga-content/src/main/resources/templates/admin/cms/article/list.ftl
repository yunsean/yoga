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
        .sort {
            width: 100%;
            text-align: center;
        }
    </style>
</@head>
<@bodyFrame showFooter=false>
    <div class="col-lg-12">
        <div <#if (param.type?trim)! == "contentPage">class="panel panel-primary"</#if>>
            <div class="panel-body">
                <form id="form1" class="form-inline" style="float: left">
                    <div class="form-group">
                        <label style=" padding-top:5px;">文章标题：</label>
                        <input type="hidden" name="columnId" value="${(param.columnId?c)!}">
                        <input type="hidden" name="columnCode" value="${(param.columnCode)!}">
                        <input type="hidden" name="articleId" value="${(param.articleId)!}">
                        <input type="hidden" name="alone" value="${(param.alone?string('yes', 'no'))!'no'}">                        &nbsp;&nbsp;
                        <input type="text" class="form-control" name="name" value="${(param.name)!}">
                        <select class="form-control" name="pageSize" onchange="formPageSize()">
                            <option value="10"<#if (page.pageSize)! == 10>selected="selected"</#if>>&nbsp;10条/页</option>
                            <option value="20"<#if (page.pageSize)! == 20>selected="selected"</#if>>&nbsp;20条/页</option>
                            <option value="30"<#if (page.pageSize)! == 30>selected="selected"</#if>>&nbsp;30条/页</option>
                            <option value="40"<#if (page.pageSize)! == 40>selected="selected"</#if>>&nbsp;40条/页</option>
                            <option value="50"<#if (page.pageSize)! == 50>selected="selected"</#if>>&nbsp;50条/页</option>
                        </select>
                    </div>                    &nbsp;&nbsp;
                    <button type="submit" class="btn btn-success"><i class="icon icon-search"></i>搜索
                    </button>
                </form>
                <@rightAction>
                    <@shiro.hasPermission name="cms_article.add" >
                        <#if (((param.columnId)!0) != 0)>
                        <a href="/admin/cms/article/edit?columnId=${(param.columnId?c)!}" class="btn btn-info" target="_parent">
                            <i class="icon icon-plus"></i>添加
                        </a>
                        </#if>
                    </@shiro.hasPermission>
                </@rightAction>
            </div>
            <table class="table table-bordered table-striped">
                <thead>
                    <@tr>
                        <@th 5 true>排序</@th>
                        <@th 40>标题</@th>
                        <@th 15 true>添加日期</@th>
                        <@th 10 true>添加人</@th>
                        <@th 5 true>上线</@th>
                        <@th 25 true>操作</@th>
                    </@tr>
                </thead>
                <tbody>
                <#list articles! as article>
                    <tr class="lineH">
                        <@td>
                            <@shiro.hasPermission name="cms_article.update" >
                                <input class="sort" type="number" onblur="doSort(this);" onkeypress="if(event.keyCode==13) {doSort(this);return false;}" articleId="${article._id}" value="${(article.sort?c)!'0'}">
                            </@shiro.hasPermission>
                        </@td>
                        <@td>${(article.title)!}</@td>
                        <@td true>${(article.date?date)!}</@td>
                        <@td true>${(article.author)!}</@td>
                        <@td true><img src="/admin/images/${(((article.online?c)! == '1')?string("yes", "no"))!"no"}.png"></@td>
                        <@td true>
                            <@shiro.hasPermission name="cms_article.update" >
                                <a href="/admin/cms/article/edit?articleId=${article._id}" class="btn btn-sm btn-success" target="_parent">
                                    <i class="icon icon-pencil"></i>编辑
                                </a>
                                <#if (article.online?c)! == '1'>
                                    <button class="btn btn-sm btn-warning" onclick="doOnline('${article._id}', false)">
                                        <i class="icon icon-arrow-down"></i> 下线
                                    </button>
                                <#else>
                                    <button class="btn btn-sm btn-info" onclick="doOnline('${article._id}', true)">
                                        <i class="icon icon-arrow-up"></i> 上线
                                    </button>
                                </#if>
                            </@shiro.hasPermission>
                            <@shiro.hasPermission name="cms_article.del" >
                                <button class="btn btn-sm btn-danger" onclick="doDelete('${article._id?if_exists}')">
                                    <i class="icon icon-remove "></i>删除
                                </button>
                            </@shiro.hasPermission>
                        </@td>
                    </tr>
                </#list>

                <@shiro.hasPermission name="cms_article.update" >
                    <script>
                        var sortInput = document.getElementsByClassName("sortInput");
                        for (var i = 0; i < sortInput.length; i++) {
                            var sort = sortInput[i];
                            sort.removeAttribute("disabled");
                        }
                    </script>
                </@shiro.hasPermission>
                </tbody>
            </table>
            <div class="box-footer" style="text-align:center">
                <@panelPageFooter action="/admin/cms/article/list" />
            </div>
        </div>
    </div>
</@bodyFrame>
    <script>
        function doDelete(id) {
            warningModal("确定要删除该文章吗？", function () {
                $.ajax({
                    url: "/admin/cms/article/delete.json?id=" + id,
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
        function doOnline(articleId, online) {
            $.post("/admin/cms/article/online.json",
                {
                    id: articleId,
                    online: online
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
        function doSort(data) {
            $.post("/admin/cms/article/sort.json",
                {
                    id: data.getAttribute("articleId"),
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
    <script>
        parent.autoAdjustIFrame();
    </script>
</@html>
