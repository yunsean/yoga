<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<@macroCommon.html>
<link href="<@macroCommon.resource/>/uploader/zui.uploader.css" rel="stylesheet">
<link href="/wechat.css" rel="stylesheet">
<script src="<@macroCommon.resource/>/uploader/zui.js"></script>
<script src="<@macroCommon.resource/>/uploader/zui.uploader.min.js"></script>
<script src="/js/masonry-docs.min.js"></script>

<body>
<div class="spinner">
    <div class="rect1"></div>
    <div class="rect2"></div>
    <div class="rect3"></div>
    <div class="rect4"></div>
    <div class="rect5"></div>
    <h5> 加载中...</h5>
</div>
<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>公众号素材管理</a></li>
            <li><#if param.type??>${param.type.getDesc()}</#if>素材</li>
        </ol>
    </div>
    <div class="row content-bottom">
        <div class="col-sm-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="icon icon-user"></i><#if param.type??>${param.type.getDesc()}</#if>素材
                </div>
                <div class="panel-body" style="min-height: 300px">
                    <div class="tableToolContainer">
                        <form action="/wechat/material/document" method="POST" class="form-inline" id="filter_form">
                            <input type="hidden" name="type" value="${param.type?if_exists}">
                            <div class="form-group">
                                <label>微信账号</label>
                                <select class="form-control" style="min-width: 200px" id="accountId"
                                        onchange="loadAccount()">
                                    <#list accounts?if_exists as account>
                                        <option value="${account.id?default(0)}">${account.name?if_exists}</option>
                                    </#list>
                                </select>
                                <script>
                                    $("#accountId").val('${param.accountId?default(0)}');

                                    function loadAccount() {
                                        var accountId = $("#accountId").val();
                                        if (accountId != ${param.accountId?default(0)}) {
                                            window.location = "/wechat/material?accountId=" + accountId + "&type=${param.type?if_exists}";
                                        }
                                    }
                                </script>
                            </div>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <div class="form-group">
                                <div class="form-group">
                                    <div class="form-group">
                                        <label class="exampleInputAccount4">名称</label>
                                        &nbsp;&nbsp;
                                        <input type="text" class="form-control" name="name"
                                               value="${param.name?if_exists}">
                                    </div>
                                </div>
                            </div>
                            <input type="hidden" name="groupId" id="filter_groupId">
                            &nbsp;&nbsp;
                            <div class="form-group">
                                <button type="submit" class="btn btn-success"><i
                                        class="fa fa-fw fa-search"></i>搜索
                                </button>
                            </div>
                            <@shiro.hasPermission name="wx_material.update" >
                                <div class="form-group" style="float: right">
                                    &nbsp;&nbsp;
                                    <a href="/weixin/dist/index.html?accountId=${param.accountId!""}&category=add"
                                       class="btn btn-primary" style="margin-left: 10px">
                                        <i class="fa fa-fw fa-upload"></i>添加图文
                                    </a>
                                    &nbsp;&nbsp;
                                    <a type="input" onclick="doRefresh()" class="btn btn-info"
                                       style="margin-left: 10px">
                                        <i class="fa fa-fw fa-download"></i>从公众号刷新
                                    </a>
                                </div>
                            </@shiro.hasPermission>
                        </form>
                    </div>
                    <div class="imgSelect" style="margin-top: 15px">
                        <div class="col-md-11">
                            <div class="msgBox" style="margin-top: 10px">
                                <div id="masonry" class="container-fluid">
                                    <#list medias?if_exists as media>
                                        <div class="masonryBox">
                                            <div class="msgItem" style="width: 260px">
                                                <div class="msgContent msgContent_materials">
                                                    <div <#if !media.uploaded>style="background-color: #f9cece"<#else>style="background-color: #cef9ed"</#if>>
                                                        <div class="msgInfo msgInfo__materials">
                                                            <span style="vertical-align: middle">更新于${media.updateTime?string("MM-dd HH:mm")}</span>
                                                            <span style="vertical-align: middle;cursor: pointer;float: right;font-size: 16px;">
                                                            <a title="设置分组" onclick="setGroup(this, '${media.mediaId?if_exists}')"><i
                                                                    class="icon icon-exchange"></i></a>
                                                            <a title="删除图文" onclick="delMaterial('${media.id?if_exists?c}')"><i
                                                                    class="icon icon-remove"></i></a>
                                                            <a title="上传图文" onclick="uploadMaterial('${media.id?if_exists?c}')"><i
                                                                    class="icon icon-globe"></i></a>
                                                            <!--<a title="发送预览" onclick="uploadMaterial('${media.id?if_exists?c}')"><i
                                                                    class="icon icon-mobile"></i></a>
                                                            <a title="群发消息" onclick="uploadMaterial('${media.id?if_exists?c}')"><i
                                                                    class="icon icon-rss"></i></a> -->
                                                        </span>
                                                        </div>
                                                    </div>
                                                    <#list media.articles?if_exists as article>
                                                        <#if article_index == 0>
                                                            <div class="msgCoverImg_materials">
                                                                <h4><a href="#">${article.title?if_exists}</a></h4>
                                                                <div class="msgImg"
                                                                     style="background-image: url('${article.thumbUrl?if_exists}');"></div>
                                                                <div class="appmsg_edit_mask js_readonly">
                                                                    <a onclick="return false;"
                                                                       class="icon20_common sort_up_white   js_up"
                                                                       data-id="" href="javascript:;" title="上移">向上</a>
                                                                    <a onclick="return false;"
                                                                       class="icon20_common sort_down_white js_down"
                                                                       data-id="" href="javascript:;" title="下移"
                                                                       style="display: block;">向下</a>
                                                                    <a onclick="delSingeArticle(${article.id!""},${param.accountId!0});"
                                                                       class="icon20_common del_media_white js_del"
                                                                       data-id="" href="javascript:;" title="删除">删除</a>
                                                                    <a href="/weixin/dist/index.html?accountId=${param.accountId!""}&id=${article.id!""}&materialId=${media.id?c!""}&category=edit"
                                                                       class="icon20_common edit_media_white js_edit"
                                                                       data-id="" title="编辑">编辑</a>
                                                                    <a href="/wechat/material/news/preview?accountId=${param.accountId!0}&articleId=${article.id!0}&updateTime=${media.updateTime?date}"
                                                                       target="view_window" class="preview" data-id=""
                                                                       title="预览"><i class="icon icon-eye-open"></i></a>
                                                                </div>
                                                            </div>
                                                        <#else>
                                                            <div class="appMsg appMsg_materials firstMsg">
                                                                <div class="appMsgImg"
                                                                     style="background-image: url('${article.thumbUrl?if_exists}')"></div>
                                                                <h4>
                                                                    <a href="javascript:;">${article.title?if_exists}</a>
                                                                </h4>
                                                                <div class="appmsg_edit_mask js_readonly">
                                                                    <a onclick="return false;"
                                                                       class="icon20_common sort_up_white   js_up"
                                                                       data-id="" href="javascript:;" title="上移">向上</a>
                                                                    <a onclick="return false;"
                                                                       class="icon20_common sort_down_white js_down"
                                                                       data-id="" href="javascript:;" title="下移"
                                                                       style="display: block;">向下</a>
                                                                    <a onclick="delSingeArticle(${article.id!""});"
                                                                       class="icon20_common del_media_white js_del"
                                                                       data-id="" href="javascript:;" title="删除">删除</a>
                                                                    <a href="/weixin/dist/index.html?accountId=${param.accountId!""}&id=${article.id!""}&materialId=${media.id?c!""}&category=edit"
                                                                       class="icon20_common edit_media_white js_edit"
                                                                       data-id="" title="编辑">编辑</a>
                                                                    <a href="/wechat/material/news/preview?accountId=${param.accountId!0}&articleId=${article.id!0}&updateTime=${media.updateTime?date}"
                                                                       target="view_window" class="preview" data-id=""
                                                                       title="预览"><i class="icon icon-eye-open"></i></a>
                                                                </div>
                                                            </div>
                                                        </#if>
                                                    </#list>
                                                    <div class="appMsg addMsg">
                                                        <i class="icon35_common add_gray">增加一条</i>
                                                        <a id="js_add_polo_appmsg"
                                                           href="/weixin/dist/index.html?accountId=${param.accountId!""}&materialId=${media.id?c!""}&category=add">
                                                            <i class="icon_appmsg_create"></i>
                                                            <strong>添加文章</strong>
                                                        </a>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </#list>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-1">
                            <div class="picGrowp">
                                <div class="padder">
                                    <ul class="growpName">
                                        <button class="btn btn-block " id="group_id_" type="button"
                                                onclick="showGroup('');">
                                            全部
                                        </button>
                                        <button class="btn btn-block " id="group_id_0" type="button"
                                                onclick="showGroup('0');">
                                            未分组
                                        </button>
                                        <#list groups?if_exists as group>
                                            <button class="btn btn-block " id="group_id_${group.id?if_exists?c}"
                                                    type="button"
                                                    onclick="showGroup('${group.id?if_exists?c}');">${group.name?if_exists}
                                                &nbsp;(${group.count?default(0)?c})
                                            </button>
                                        </#list>
                                        <script>
                                            $(function () {
                                                var btn = $("#group_id_"<#if param.groupId??> + ${param.groupId?if_exists?c}</#if>);
                                                btn.addClass("btn-primary");
                                            })
                                        </script>
                                    </ul>
                                    <div class="addGrowp">
                                        <button id="groupAddButton" class="btn btn-default text-muted custom-fans-add-btn" style="width: 100%;" type="button">
                                            <i class="addGropIcon"></i>
                                            新建分组
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="box-footer" style="text-align:center">
                    <@html.paging page=page param=param action="/wechat/material"/>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    function delSingeArticle(articleId) {
        warningModal("确定要删除该文章吗？", function () {
            $.post(
                    "/api/wechat/material/article/del",
                    {
                        articleId: articleId,
                        accountId: ${param.accountId?c}
                    },
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);
                        } else {
                            window.location.reload();
                        }
                    }
            );
        });
    }
    function delMaterial(id) {
        warningModal("确定要删除该素材及其所包含的文章吗？这将同时从微信公众号永久删除！", function () {
            $.post(
                    "/api/wechat/material/delete",
                    {
                        accountId: ${param.accountId?c},
                        id: id
                    },
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);
                        } else {
                            window.location.reload();
                        }
                    }
            );
        });
    }
    function uploadMaterial(id) {
        warningModal("确定要发布图文到微信公众号么！", function () {
            $.post(
                    "/api/wechat/material/news/upload",
                    {
                        accountId: ${param.accountId?c},
                        materialId: id
                    },
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);
                        } else {
                            window.location.reload();
                        }
                    }
            );
        });
    }
</script>

<script>
    $("#masonry").masonry({
        columWidth: 240,
        itemSelector: ".masonryBox",    // 要布局的网格元素
        gutter: 20,                     // 网格间水平方向边距，垂直方向边距使用css的margin-bottom设置
        percentPosition: true,          // 使用columnWidth对应元素的百分比尺寸
        stamp: '',                      // 网格中的固定元素，不会因重新布局改变位置，移动元素填充到固定元素下方
        fitWidth: true,                 // 设置网格容器宽度等于网格宽度，这样配合css的auto margin实现居中显示
        originLeft: true,               // 默认true网格左对齐，设为false变为右对齐
        originTop: true,                // 默认true网格对齐顶部，设为false对齐底部
        containerStyle: {position: 'relative'}, // 设置容器样式
        transitionDuration: '0.8s',     // 改变位置或变为显示后，重布局变换的持续时间，时间格式为css的时间格式
        stagger: '0.03s',               // 重布局时网格并不是一起变换的，排在后面的网格比前一个延迟开始，该项设置延迟时间
        resize: false,                  // 改变窗口大小将不会影响布局
        initLayout: true               // 初始化布局，设未true可手动初试化布局
    })


</script>


<footer class="main-footer">
    <@common.footer />
</footer>
</body>
    <#include "./materials_action.ftl" />
</@macroCommon.html>