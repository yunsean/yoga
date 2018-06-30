<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<@macroCommon.html>
<body>
<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>内容管理</a></li>
            <li><a onclick="history.back(-1);">模版管理</a></li>
            <li>布局管理</li>
            <span class="pull-right">
                <button class="btn btn-primary btn-sm" onclick="history.back(-1);">返回</button>
            </span>
        </ol>
    </div>
    <div class="row content-bottom">
        <div class="col-lg-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="fa fa-comments-o"></i>
                    布局列表
                </div>
                <div class="panel-body">
                    <div class="tableToolContainer" style="padding-bottom:15px">
                        <form class="form-inline">
                            <input type="hidden" name="templateId" value="${param.templateId?c}">
                            <div class="form-group">
                                <label>布局类型</label>
                                <select class="form-control" style="min-width: 120px; margin-left: 10px" name="type" id="layout_type">
                                    <option value="" selected>全部</option>
                                    <option value="LIST">列表布局</option>
                                    <option value="DETAIL">详情布局</option>
                                </select>
                                <#if layout??>
                                    <script>
                                        $("#layout_type").val('${param.type?if_exists}');
                                    </script>
                                </#if>
                            </div>
                            <button type="submit" class="btn btn-success" style="margin-left: 15px"><i class="fa fa-fw fa-search"></i>搜索
                            </button>
                            <@shiro.hasPermission name="cms_template.update" >
                                <a href="/cms/layout/modify?templateId=${param.templateId?c}" class="btn btn-primary" style="float: right">
                                    <i class="fa fa-fw fa-plus "></i>添加</a>
                            </@shiro.hasPermission>
                        </form>
                    </div>
                    <table id="" class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th style="width:60px" class="tableCenter">#</th>
                            <th style="width:30%">布局名称</th>
                            <th style="width:10%" class="tableLeft">布局类型</th>
                            <th style="width:30%">所需字段</th>
                            <th class="tableCenter">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list layouts?if_exists as layout>
                            <tr>
                                <td class="tableCenter">${layout.id?c}</td>
                                <td>${layout.title?if_exists}</td>
                                <td class="tableLeft">
                                    <#list layoutEnum as val>
                                        <#if (val == layout.type!"")>${val.getName()}</#if>
                                    </#list>
                                </td>
                                <td>${layout.fields?if_exists}</td>
                                <td class="tableCenter">
                                    <@shiro.hasPermission name="cms_template.update" >
                                        <a href="/cms/layout/modify?id=${layout.id?c}&templateId=${param.templateId?c}" class="btn btn-info caseReport">
                                            <i class="fa fa-fw fa-pencil "></i>修改</a>
                                        <a href="#" class="btn btn-danger caseReport" onclick="delLayout(${layout.id?default(0)?c})">
                                            <i class="fa fa-fw fa-remove "></i>删除</a>
                                    </@shiro.hasPermission>
                                </td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<footer class="main-footer">
    <@common.footer />
</footer>
</div>
</body>

<script>
    function delLayout(id) {
        warningModal("确定要删除该布局吗?", function () {
            $.post(
                    "/api/cms/layout/delete",
                    {id: id},
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);;
                        } else {
                            window.location.reload();
                        }
                    }
            );
        });
    }
</script>
</@macroCommon.html>