<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>修改个人信息</title>
    <link rel="stylesheet" href="<@tenantTags tag="resource"/>/mzui/css/mzui.min.css">
</head>

<body class='white has-index-content'>
<div class="heading black">
    <a class="nav title" href="/web">
    <#if user.avatar??>
        <img src="${user.avatar?if_exists}" width="24px" height="24px">
    <#else>
        <i class="icon-user"></i>
    </#if>
        <strong>${user.fullname?if_exists}</strong>
    </a>
    <nav class="nav">
        <a href="/web/login">退出登录</a>
    </nav>
</div>
<nav class="affix dock-top nav primary-pale nav-secondary" id="navs">
</nav>
<div id="partial" class="container display fade in" data-display-name="navs">
    <div class="panel">
        <div class="panel-body">
            <div class="box"></div>
            <form class="white" accept-charset="UTF-8" id="retrieve-form">
                <input type="hidden" name="uuid" id="uuid">
                <div class="control has-label-left fluid">
                    <input autofocus="" id="lastname" name="lastname" type="text" class="input" placeholder="姓" value="${user.lastname?if_exists}">
                    <label for="lastname" title="姓"><i class="icon icon-user"></i></label>
                    <p class="help-text"></p>
                </div>
                <div class="control has-label-left fluid">
                    <input autofocus="" id="firstname" name="firstname" type="text" class="input" placeholder="名" value="${user.firstname?if_exists}">
                    <label for="firstname" title="名"><i class="icon icon-user"></i></label>
                    <p class="help-text"></p>
                </div>
                <div class="control has-label-left fluid">
                    <input autofocus="" id="username" name="username" type="text" class="input" placeholder="登录账号" value="${user.username?lower_case?if_exists}">
                    <label for="username" title="登录账号"><i class="icon icon-user"></i></label>
                    <p class="help-text"></p>
                </div>
                <div class="control has-label-left fluid">
                    <input autofocus="" id="phone" name="phone" type="text" class="input" placeholder="手机号" value="${user.phone?if_exists}">
                    <label for="phone" title="登录密码"><i class="icon icon-user"></i></label>
                    <p class="help-text"></p>
                </div>
                <div class="control has-label-left fluid">
                    <input autofocus="" id="email" name="email" type="text" class="input" placeholder="电子邮箱" value="${user.email?if_exists}">
                    <label for="email" title="电子邮箱"><i class="icon icon-user"></i></label>
                    <p class="help-text"></p>
                </div>
                <div class="control has-label-left fluid">
                    <input autofocus="" id="qq" name="qq" type="text" class="input" placeholder="QQ号" value="${user.qq?if_exists}">
                    <label for="qq" title="手机号"><i class="icon icon-user"></i></label>
                    <p class="help-text"></p>
                </div>
                <div class="control has-label-left fluid">
                    <input autofocus="" id="address" name="address" type="text" class="input" placeholder="联系地址" value="${user.address?if_exists}">
                    <label for="address" title="联系地址"><i class="icon icon-user"></i></label>
                    <p class="help-text"></p>
                </div>
                <div class="control has-label-left fluid">
                    <input autofocus="" id="qq" name="wechat" type="wechat" class="input" placeholder="微信号" value="${user.wechat?if_exists}">
                    <label for="wechat" title="微信号"><i class="icon icon-user"></i></label>
                    <p class="help-text"></p>
                </div>
                <div class="control">
                    <button type="button" onclick="doSubmit()" class="btn primary fluid with-padding">保存</button>
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
        var newPwd = $("#password").val();
        var newPwd1 = $("#password1").val();
        if (newPwd != newPwd1) {
            warning("两次输入的密码不一致！");
            return;
        }
        var json = serialize($("#retrieve-form"));
        $.post(
                "/api/user/update",
                json,
                function (data) {
                    if (data.code < 0) {
                        warning(data.message);
                    } else {
                        message("info", "修改个人信息成功，请重新登录！", function () {
                            window.location.href = "/web/login";
                        });
                    }
                }
        );
    }
</script>
</html>
