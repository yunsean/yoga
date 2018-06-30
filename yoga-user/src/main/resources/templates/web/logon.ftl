<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>找回密码</title>
    <link rel="stylesheet" href="<@tenantTags tag="resource"/>/mzui/css/mzui.min.css">
</head>
<body class='with-heading-top white has-index-content'>
    <nav class="affix dock-top nav primary-pale nav-secondary" id="navs">
    </nav>
    <div id="partial" class="container display fade in" data-display-name="navs">
        <div class="panel">
            <div class="panel-body">
                <form class="white" accept-charset="UTF-8" id="retrieve-form">
                    <input type="hidden" name="uuid" id="uuid">
                    <div class="control has-label-left fluid">
                        <input autofocus="" id="lastname" name="lastname" type="text" class="input" placeholder="姓">
                        <label for="lastname" title="姓"><i class="icon icon-user"></i></label>
                        <p class="help-text"></p>
                    </div>
                    <div class="control has-label-left fluid">
                        <input autofocus="" id="firstname" name="firstname" type="text" class="input" placeholder="名">
                        <label for="firstname" title="名"><i class="icon icon-user"></i></label>
                        <p class="help-text"></p>
                    </div>
                    <div class="control has-label-left fluid">
                        <input autofocus="" id="username" name="username" type="text" class="input" placeholder="登录账号">
                        <label for="username" title="登录账号"><i class="icon icon-user"></i></label>
                        <p class="help-text"></p>
                    </div>
                    <div class="control has-label-left fluid">
                        <input autofocus="" id="password" name="password" type="password" class="input" placeholder="登录密码">
                        <label for="password" title="登录密码"><i class="icon icon-user"></i></label>
                        <p class="help-text"></p>
                    </div>
                    <div class="control has-label-left fluid">
                        <input autofocus="" id="password1" name="password1" type="password" class="input" placeholder="确认密码">
                        <label for="password1" title="确认密码"><i class="icon icon-user"></i></label>
                        <p class="help-text"></p>
                    </div>
                    <div class="control has-label-left fluid">
                        <input autofocus="" id="mobile" name="mobile" type="text" class="input" placeholder="手机号（可选）">
                        <label for="mobile" title="手机号"><i class="icon icon-user"></i></label>
                        <p class="help-text"></p>
                    </div>
                    <div class="fluid flex flex-row no-padding">
                        <div class="control has-label-left fluid cell no-padding">
                            <input id="captcha" name="captcha" type="text" class="input" placeholder="短信验证码">
                            <label for="captcha" title="短信验证码"><i class="icon icon-key"></i></label>
                            <p class="help-text"></p>
                        </div>
                        <div>
                            <button type="button" onclick="doCaptcha()" class="btn cell info">发送验证码</button>
                        </div>
                    </div>
                    <div class="control">
                        <button type="button" onclick="doSubmit()" class="btn primary fluid with-padding">登录</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
<script src="<@tenantTags tag="resource"/>/mzui/js/mzui.js"></script>
<script src="<@tenantTags tag="resource"/>/mzui/wrapper/sketch.js"></script>
<script>
    function doCaptcha() {
        var json = serialize($("#retrieve-form"));
        $.post(
                "/api/auth/captcha",
                json,
                function (data) {
                    if (data.code < 0) {
                        warning(data.message);
                    } else {
                        $("#uuid").val(data.result.uuid);
                        if (data.result.hasOwnProperty("captcha")) {
                            $("#captcha").val(data.result.captcha);
                        }
                        info("验证码发送成功，请注意查收短信！");
                    }
                }
        );
    }
    function doSubmit() {
        var newPwd = $("#password").val();
        var newPwd1 = $("#password1").val();
        if (newPwd != newPwd1) {
            warning("两次输入的密码不一致！");
            return;
        }
        var json = serialize($("#retrieve-form"));
        $.post(
                "/api/auth/logon",
                json,
                function (data) {
                    if (data.code < 0) {
                        warning(data.message);
                    } else {
                        message("info", "注册用户成功，请登录！", function () {
                            window.location.href = "/web/login";
                        });
                    }
                }
        );
    }
</script>
</html>
