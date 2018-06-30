<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>修改密码</title>
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
                        <input autofocus="" id="oldPwd" name="oldPwd" type="password" class="input" placeholder="请输入当前密码">
                        <label for="oldPwd" title="当前密码"><i class="icon icon-key"></i></label>
                        <p class="help-text"></p>
                    </div>
                    <div class="control has-label-left fluid">
                        <input id="newPwd" name="newPwd" type="password" class="input" placeholder="请输入新密码">
                        <label for="newPwd" title="新密码"><i class="icon icon-lock"></i></label>
                        <p class="help-text"></p>
                    </div>
                    <div class="control has-label-left fluid">
                        <input autofocus="" id="newPwd1" name="newPwd1" type="password" class="input" placeholder="请再次输入新密码">
                        <label for="newPwd1" title="确认密码"><i class="icon icon-lock"></i></label>
                        <p class="help-text"></p>
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
    function doSubmit() {
        var newPwd = $("#newPwd").val();
        var newPwd1 = $("#newPwd1").val();
        if (newPwd != newPwd1) {
            warning("两次输入的新密码不一致！");
            return;
        }
        var json = serialize($("#retrieve-form"));
        $.post(
                "/api/user/passwd",
                json,
                function (data) {
                    if (data.code < 0) {
                        warning(data.message);
                    } else {
                        message("info", "修改密码，下次登录请使用新密码！", function () {
                            window.history.back();
                        });
                    }
                }
        );
    }
</script>
</html>
