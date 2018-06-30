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
            <div class="box no-padding">
                <div class="flex-center flex flex-column">
                    <img src="${img.pickLogin?if_exists}" width="auto" alt="请添加LoginImg.top配置项">
                </div>
            </div>
            <div class="cell box margin-5rem flex flex-column">
                <div class="cell"></div>
                <div class="layer center-block flex-center cell flex flex-column has-padding-h" style="width: 90%">
                    <div class="center-block margin-t-rem5 box"><@common.platform /></div>
                    <form class="center-block box" accept-charset="UTF-8" method="post" action="/web/login" style="width: 80%">
                        <input type="hidden" name="uri" value="${param.uri?if_exists}"/>
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
                            <button type="submit" class="btn primary fluid">登录</button>
                        </div>
                        <div class="control">
                            <div class="checkbox">
                                <input type="checkbox" name="remember" checked>
                                <label for="remember">保持登录</label>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="cell"></div>
            </div>
            <div class="cell box flex-none">
                <#if allowLogon?if_exists>
                    <div class="tile flex-center flex flex-column">
                        <a tabindex="-1" class="pull-right text-link" href="/web/logon">新用户注册</a>
                    </div>
                </#if>
                <div class="tile flex-center flex flex-column">
                    <a tabindex="-1" class="pull-right text-link" href="/web/retrieve">忘记密码?</a>
                </div>
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
