<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>个人信息</title>
    <link rel="stylesheet" href="<@tenantTags tag="resource"/>/mzui/css/mzui.min.css">
    <style>
        .profile-header {position: relative; overflow: hidden; min-height: 8rem}
        .profile-header > .back {top: -2rem; left: -2rem; right: -2rem; bottom: -2rem; background-position: center; background-size: cover}
        .profile-header > .front {background: rgba(0,0,0,.1); text-align: center; padding: 1.5rem}
    </style>
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
<div id="partial" class="container display fade in no-padding gray" data-display-name="navs">
    <div class="profile-header space shadow">
        <div class="gray dock blur-lg back" style="background-image: url(/frontend/img/img2.jpg)"></div>
        <div class="front dock text-white">
            <div class="avatar avatar-xl circle space-sm">
                <img src="${user.avatar?default('/frontend/img/avatar.png')}" alt="">
            </div>
            <h4 class="lead text-shadow-black">${user.fullname?if_exists}</h4>
        </div>
    </div>
    <div class="white has-padding">
        <table class="table">
            <tbody>
            <tr>
                <td class="strong text-right">用户名：</td>
                <td>${user.username?if_exists}</td>
            </tr>
            <tr>
                <td class="strong text-right">姓：</td>
                <td>${user.lastname?if_exists}</td>
            </tr>
            <tr>
                <td class="strong text-right">名：</td>
                <td>${user.firstname?if_exists}</td>
            </tr>
            <#if deptName??>
            <tr>
                <td class="strong text-right">所属部门：</td>
                <td>${deptName?if_exists}</td>
            </tr>
            </#if>
            <#if dutyName??>
            <tr>
                <td class="strong text-right">职级：</td>
                <td>${dutyName?if_exists}</td>
            </tr>
            </#if>
            <tr>
                <td class="strong text-right">电话：</td>
                <td>${user.phone?if_exists}</td>
            </tr>
            <tr>
                <td class="strong text-right">邮箱：</td>
                <td>${user.email?if_exists}</td>
            </tr>
            <tr>
                <td class="strong text-right">QQ号：</td>
                <td>${user.qq?if_exists}</td>
            </tr>
            <tr>
                <td class="strong text-right">住址：</td>
                <td>${user.address?if_exists}</td>
            </tr>
            <tr>
                <td class="strong text-right">微信号：</td>
                <td>${user.wechat?if_exists}</td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="box gray">
        <a href="/web/modify" class="btn text-primary dark fluid with-padding">修改个人信息</a>
    </div>
</div>
</body>
<script src="<@tenantTags tag="resource"/>/mzui/js/mzui.js"></script>
<script src="<@tenantTags tag="resource"/>/mzui/wrapper/sketch.js"></script>
<script>
</script>
</html>
