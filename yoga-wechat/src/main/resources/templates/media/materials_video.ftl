<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<@macroCommon.html>
<link href="<@macroCommon.resource/>/uploader/zui.uploader.css" rel="stylesheet">
<link href="/wechat.css" rel="stylesheet">
<script src="<@macroCommon.resource/>/uploader/zui.js"></script>
<script src="<@macroCommon.resource/>/uploader/zui.uploader.min.js"></script>
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
                <div class="panel-body">
                    <div class="tableToolContainer" style="margin-bottom:15px">
                        <form action="/wechat/material" method="POST" class="form-inline" id="filter_form">
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
                                    <a type="input" onclick="doAdd()" class="btn btn-primary" style="margin-left: 10px">
                                        <i class="fa fa-fw fa-upload"></i>上传素材
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
                    <div class="imgSelect">
                        <div class="picHeader">
                            <label class="checkbox-inline">
                                <input type="checkbox" id="all_checked" onchange="checkAll(this);"/>
                                <span class="m-l-xs" onchange="checkAll(this);">全选</span>
                            </label>
                            <button class="picBtn" onclick="batchAddGroup()">移动分组</button>
                            <button class="picBtn">删除</button>
                        </div>
                        <div class="picBox">
                            <#list medias?if_exists as media>
                                <div class="videoItems">
                                    <div class="picCenter" style="float: left">
                                        <label class="checkbox-inline" style="overflow: hidden;text-overflow: ellipsis; width: 100%; height: 15px">
                                        <input type="checkbox" name="material_id_checkbox" value="${media.mediaId?if_exists}">${media.name?if_exists}
                                    </label>
                                        <#--<input type="checkbox" class="picCheckBox" style="float: left"  name="material_id_checkbox" value="${media.mediaId?if_exists}"/>-->
                                        <#--<span class="picNameBox">${media.name?if_exists}</span>-->
                                        <span class="videoTime">${media.updateTime?datetime?if_exists}</span>
                                        <div class="videoPlayer">
                                            <video class="vidoeMian" controls>
                                                <source src="${media.url?if_exists}" type="video/mp4">
                                            </video>
                                        </div>

                                    </div>
                                    <div class="videoControl">
                                        <span onclick="doMaterialEdit('${media.name!''}','${media.mediaId!0}')"><i class="fa pencil"></i></span>
                                        <span onclick="setGroup(this, '${media.mediaId?if_exists}')"><i
                                                class="fa exchange"></i></span>
                                        <span onclick="doDelete(${media.id?if_exists?c})"><i
                                                class="fa trash"></i></span>
                                    </div>
                                </div>
                            </#list>
                        </div>
                        <div class="picGrowp">
                            <div class="padder">
                                <ul class="growpName">
                                    <button class="btn btn-block " id="group_id_" type="button" onclick="showGroup('');">全部</button>
                                    <button class="btn btn-block " id="group_id_0" type="button" onclick="showGroup('0');">未分组</button>
                                    <#list groups?if_exists as group>
                                        <button class="btn btn-block " id="group_id_${group.id?if_exists?c}" type="button" onclick="showGroup('${group.id?if_exists?c}');">${group.name?if_exists}&nbsp;(${group.count?default(0)?c})</button>
                                    </#list>
                                    <script>
                                        $(function () {
                                            var btn = $("#group_id_"<#if param.groupId??> + ${param.groupId?if_exists?c}</#if>);
                                            btn.addClass("btn-primary");
                                        })
                                    </script>
                                </ul>
                                <div class="addGrowp">
                                    <a id="groupAddButton" class="btn btn-default btn-sm text-muted custom-fans-add-btn">
                                        <i class="addGropIcon"></i>
                                        新建分组
                                    </a>
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
<footer class="main-footer">
    <@common.footer />
</footer>
</body>

<#include "./materials_action.ftl" />
</@macroCommon.html>