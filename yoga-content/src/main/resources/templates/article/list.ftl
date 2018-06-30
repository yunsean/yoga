<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<@macroCommon.html>
<script type="text/javascript" charset="utf-8" src="/cms/articles.js"></script>
<body>
<div class="container-fluid">
    <#if param.type?? && param.type?trim=="allContent">style="background-color:#ffffff; border:1px solid #f4f4f4"</#if>
    <#if param.type?? && param.type?trim=="contentPage">class="content"</#if>
    <div class="row content-bottom">
        <div class="col-lg-12">
            <div <#if param.type?? && param.type?trim=="contentPage">class="panel panel-primary"</#if>>
                <div class="panel-body">
                    <form action="/cms/article/list" id="form1" class="form-inline" style="float: left">
                        <div class="form-group">
                            <label style=" padding-top:5px;">文章标题：</label>
                            <input type="hidden" name="columnId" value="${param.columnId?default(0)?c}">
                            <input type="hidden" name="columnCode" value="${param.columnCode?if_exists}">
                            <input type="hidden" name="articleId" value="${param.articleId?if_exists}">
                            <input type="hidden" name="alone"
                                   value="${param.alone?if_exists?string('yes', 'no')}">
                            &nbsp;&nbsp;
                            <input type="text" class="form-control" id="paramName" name="name"
                                   value="${param.name!""}">
                        </div>
                        &nbsp;&nbsp;
                        <button type="submit" class="btn btn-success"><i class="fa fa-fw fa-search"></i>搜索
                        </button>
                    </form>
                    <@shiro.hasPermission name="cms_article.add" >
                        <#if (param.columnId?default(0) != 0)>
                            &nbsp;&nbsp;
                            <a href="#"
                               onclick="addArticle('${param.columnId?default(0)?c}', '${param.alone?if_exists?string('yes', 'no')}')"
                               class="btn btn-primary" id="addContentHref">
                                <i class="fa fa-fw fa-plus "></i>添加文章
                            </a>
                        </#if>
                    </@shiro.hasPermission>
                    <div style="float: right; display: none">
                        <label>每页显示条数</label>
                        <select name="pageSize" onchange="formPageSize()" id="pageSizeSelect">
                            <option value="10"<#if page.pageSize==10>selected="selected"</#if>>&nbsp;10条
                            </option>
                            <option value="20"<#if page.pageSize==20>selected="selected"</#if>>&nbsp;20条
                            </option>
                            <option value="30"<#if page.pageSize==30>selected="selected"</#if>>&nbsp;30条
                            </option>
                            <option value="40"<#if page.pageSize==40>selected="selected"</#if>>&nbsp;40条
                            </option>
                            <option value="50"<#if page.pageSize==50>selected="selected"</#if>>&nbsp;50条
                            </option>
                        </select>
                    </div>
                </div>
                <table class="table table-bordered table-striped">
                    <thead>
                    <tr>
                        <th style="width:10%" class="tableCenter">排序</th>
                        <th style="width:35%">名称</th>
                        <th style="width:15%" class="tableCenter">添加日期</th>
                        <th style="width:10%" class="tableCenter">添加人</th>
                        <th style="width:25%" class="tableCenter">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                        <#list articles as article>
                        <tr class="lineH">
                            <td class="tableCenter">
                                <@shiro.hasPermission name="cms_article.update" >
                                    <input class="sortInput" type="number"
                                           style="width: 50px;text-align: center"
                                           onblur="sortArticle(this);"
                                           onkeypress="if(event.keyCode==13) {sortArticle(this);return false;}"
                                           articleId="${article._id}" value="${article.sort?default(0)}">
                                </@shiro.hasPermission>
                            </td>
                            <td>${article.title}</td>
                            <td class="tableCenter">${article.date?date}</td>
                            <td class="tableCenter">${article.author?if_exists}</td>
                            <td class="tableCenter">
                                <@shiro.hasPermission name="cms_article.update" >
                                    <button onclick="editArticle('${article._id}','${article.columnId?c}', '${param.alone?if_exists?string('yes', 'no')}')"
                                            class="btn btn-success "><i class="fa fa-fw fa-pencil "></i>编辑
                                    </button>
                                    <button
                                        <#if article.online?default(0)==0> class="btn btn-info "
                                        <#else> class="btn btn-warning "
                                        </#if>
                                                                           onclick="onlineArticle('${article._id}', ${article.online?default(0)} == 0)">
                                        <i <#if article.online?default(0)==0>class="fa fa-fw fa-arrow-circle-up"
                                        <#else> class="fa fa-fw fa-arrow-circle-down"
                                        </#if>>
                                        </i>
                                        <#if article.online?default(0)==0>上线
                                        <#else>下线
                                        </#if>
                                    </button>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="cms_article.del" >
                                    <button class="btn btn-danger "
                                            onclick="delArticle('${article._id?if_exists}')">
                                        <i class="fa fa-fw fa-remove "></i>删除
                                    </button>
                                </@shiro.hasPermission>
                            </td>
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
                    <@html.paging page=page param=param action="/cms/article/list"/>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
<script>
    parent.autoAdjustIFrame();
</script>
</body>
</@macroCommon.html>
