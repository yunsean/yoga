<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<!DOCTYPE html>
<html>
<@macroCommon.html>
<body class="hold-transition skin-blue">
<div style="background-color:#ffffff;">
    <div class="row content-bottom">
        <section class="col-lg-12 connectedSortable">
            <div id="iframeDiv">
                <div class="box-body">
                    <div class="form-group">
                        <form action="/cms/article/related">
                            <div class="tableToolBar row">
                                <div class="col-sm-5">
                                    <div class="form-group">
                                        <label class="col-sm-5 control-label">文章名称</label>
                                        <div class="col-sm-7">
                                            <input type="hidden" name="articlesCode" value="${articlesCode?if_exists}">
                                            <input type="text" class="form-control" value="${title!""}" name="title">
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-2">
                                    <button type="submit" class="btn btn-sm btn-success"><i
                                            class="fa fa-fw fa-search"></i>搜索
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="form-group">
                        <table id="" class="table table-bordered table-hover">
                            <thead>
                            <tr>
                                <th style="width:10%" class="tableCenter">选择</th>
                                <th style="width:70%" class="tableCenter">文章名称</th>
                                <th style="width:20%" class="tableCenter">创建时间</th>
                            </tr>
                            </thead>
                            <tbody>
                                <#list contentLsit as content>
                                <tr>
                                    <td class="tableCenter">
                                        <input name="id" type="checkbox" value="${content._id}"/>
                                    </td>
                                    <td class="tableCenter">${content.title}</td>
                                    <td class="tableCenter">${content.date?string("yyyy-MM-dd")}</td>
                                </tr>
                                </#list>
                            </tbody>
                        </table>
                        <input type="hidden" value="${fieldCode}" name="fieldCode">
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>
</body>
</@macroCommon.html>

