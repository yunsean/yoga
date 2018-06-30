<#import "/macro_common.ftl" as macroCommon>
<@macroCommon.html>
<body>
<style>
    .col-center-block {
        float: none;
        display: block;
        margin-left: auto;
        margin-right: auto;
    }
    .minWidth100px {
        min-width: 100px;
    }
</style>
<div class="container-fluid">
    <!-- Content Wrapper. Contains page content -->
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="#"><i class="icon icon-user"></i> 权限管理</a></li>
            <li><a href="#">用户管理</a></li>
            <li>编辑</li>
            <span class="pull-right">
                <button class="btn btn-sm" onclick="history.back(-1)"><i class="icon icon-share-alt"></i>返回</button>
            </span>
        </ol>
    </div>
    <!-- Main row -->
    <div class="row content-bottom">
        <div class="col-sm-12">
            <!-- Chat box -->
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="icon icon-user"></i>
                    用户基本信息
                </div>
                <div class="panel-body">
                    <input type="hidden" value="${user.id?default(0)?c}" id="id">
                    <div class="panel-body">
                        <div class="tableToolContainer" style="margin-bottom:15px">
                            <div class="form-horizontal">
                                <div class="center-block" style="width: 128px; margin-bottom: 10px">
                                    <div class="col-center-block">
                                        <img id="avatarImage" style="width: 128px; height: 128px; object-fit: cover; object-position: center; " class="center-block"
                                             <#if user.avatar?? && user.avatar?length &gt; 0>
                                             src="${user.avatar?if_exists}"
                                             <#else>
                                             src="/user/default_avatar.jpg"
                                             </#if>>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-4">姓:</label>
                                    <div class="col-md-4">
                                        <span class="form-control">${user.lastname?if_exists}</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-4">名:</label>
                                    <div class="col-md-4">
                                        <span class="form-control">${user.firstname?if_exists}</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-4">登录账号:</label>
                                    <div class="col-md-4">
                                        <span class="form-control">${user.username?if_exists}</span>
                                    </div>
                                </div>
                                <#if depts?exists && (depts?size > 0)>
                                    <div class="form-group">
                                        <label class="col-md-4">所属<@common.deptAlias/>:</label>
                                        <div class="col-md-4">
                                            <span class="form-control">
                                                <#if user.deptId?exists && deptMap?exists && deptMap[user.deptId?c]?exists>
                                                    ${deptMap[user.deptId?c].name!}
                                                </#if>
                                            </span>
                                        </div>
                                    </div>
                                </#if>
                                <#if dutyMap?exists && (dutyMap?size > 0)>
                                    <div class="form-group">
                                        <label class="col-md-4"><@common.dutyAlias/>:</label>
                                        <div class="col-md-4">
                                            <span class="form-control">
                                                <#if user.dutyId?exists && dutyMap?exists && dutyMap[user.dutyId?c]?exists>
                                                    ${dutyMap[user.dutyId?c].name!}
                                                </#if>
                                            </span>
                                        </div>
                                    </div>
                                </#if>
                                <#if roles?exists && (roles?size > 0)>
                                    <div class="form-group">
                                        <label class="col-md-4">赋予<@common.roleAlias/>:</label>
                                        <div class="col-md-4">
                                            <div style="margin-top: 5px">
                                            <#list roles as role>
                                                <input name="roleIds" disabled type="checkbox"
                                                       <#if userRoles?if_exists?seq_contains(role.id)>checked="checked"</#if>>
                                                &nbsp;&nbsp;${role.name}
                                            </#list>
                                            </div>
                                        </div>
                                    </div>
                                </#if>
                                <#if user.gender??>
                                    <div class="form-group">
                                        <label class="col-md-4">性别:</label>
                                        <div class="col-md-4">
                                            <span class="form-control">${user.gender.getName()}</span>
                                        </div>
                                    </div>
                                </#if>
                                <div class="form-group">
                                    <label class="col-md-4">电子邮箱:</label>
                                    <div class="col-md-4">
                                        <span class="form-control">${user.email?if_exists}</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-4">QQ号码:</label>
                                    <div class="col-md-4">
                                        <span class="form-control">${user.qq?if_exists}</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-4">手机号码:</label>
                                    <div class="col-md-4">
                                        <span class="form-control">${user.phone?if_exists}</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-4">微信号:</label>
                                    <div class="col-md-4">
                                        <span class="form-control">${user.wechat?if_exists}</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-4">联系地址:</label>
                                    <div class="col-md-4">
                                        <span class="form-control">${user.address?if_exists}</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-4">邮政编码:</label>
                                    <div class="col-md-4">
                                        <span class="form-control">${user.postcode?if_exists}</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-4">单位信息:</label>
                                    <div class="col-md-4">
                                        <span class="form-control">${user.company?if_exists}</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-4">创建时间:</label>
                                    <div class="col-md-4">
                                        <span class="form-control">${user.createTime?string("yyyy-MM-dd")?if_exists}</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-offset-5  col-md-4">
                                        <button class="btn btn-default" onclick="javascript:history.back(-1)">返回
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<footer class="main-footer">
    <@common.footer />
</footer>
</body>
</@macroCommon.html>