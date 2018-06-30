<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><@common.platform /></title>
    <link rel="stylesheet" href="<@tenantTags tag="resource"/>/mzui/css/mzui.min.css">
    <script src="<@tenantTags tag="resource"/>/plugins/jQuery/jquery-1.12.3.min.js"></script>
</head>
<body class='has-index-content'>
<div class="loading loading-light" id="pageLoading"></div>
<div id="partial" class="container display fade in" data-display-name="navs">
    <div class="page enter-from-bottom display fade in" id="login1">
        <div id="login" class="column fluid-v no-margin flex-nowrap">
            <div class="cell box margin-5rem flex flex-column">
                <div class="center-block flex flex-column has-padding-h" style="width: 100%">
                    <div class="center-block text-bold large"><@common.platform /></div>
                    <div class="center-block margin-t-rem5 box small text-gray">绑定系统账号</div>
                    <form class="center-block box" accept-charset="UTF-8" method="post" action="/wechat/oauth2/doBind?tid=${tenantId?default(0)?c}" style="width: 80%">
                        <input type="hidden" name="uri" value="${param.uri?if_exists}"/>
                        <input type="hidden" name="openId" value="${param.openId?if_exists}" />
                        <input type="hidden" name="aid" value="${accountId?default(0)?c}" />
                        <div class="control box danger form-message hide-empty">${reason?if_exists}</div>
                        <div class="control has-label-left fluid">
                            <input autofocus="" id="username" name="username" type="text" class="input" placeholder="用户名/手机号">
                            <label for="username" title="用户名"><i class="icon icon-user"></i></label>
                            <p class="help-text"></p>
                        </div>
                        <div class="control has-label-left fluid">
                            <input id="password" name="password" type="password" class="input" placeholder="密码">
                            <label for="password" title="密码"><i class="icon icon-lock"></i></label>
                            <p class="help-text"></p>
                        </div>
                        <div class="control">
                            <button type="submit" class="btn primary fluid">绑定系统账号</button>
                        </div>
                    </form>
                    <div class="cell flex-none">
                    <#if allowLogon?if_exists>
                        <div class="tile flex-center flex flex-column">
                            <a tabindex="-1" class="pull-right text-link small" href="/web/logon">新用户注册</a>
                        </div>
                    </#if>
                        <div class="tile flex-center flex flex-column">
                            <a tabindex="-1" class="pull-right text-gray small" href="/web/retrieve">忘记密码?</a>
                        </div>
                    </div>
                </div>
                <div class="cell"></div>
            </div>
        </div>
    </div>
</div>
<script src="<@tenantTags tag="resource"/>/mzui/js/mzui.min.js"></script>
</body>
<script>
    $(function () {
        $("#pageLoading").hide();
    });
</script>
</html>
