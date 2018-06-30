<!-- BEGIN HEAD -->
<#import "/macro_common.ftl" as macroCommon>
<!DOCTYPE html>
<html>
<@macroCommon.html/>
<head>
    <link href="/css/zui.min.css" rel="stylesheet">
    <style>
        html {
            width: 100%;
            height: 100%;
        }

        body {
            height: 100%;
            width: 100%;
            font-family: "宋体", Helvetica, Tahoma, Arial, sans-serif;
            background-color: #EEEEEE;
        }

        .mainContainer {
            width: 100%;
            height: 90%;
            padding: 0;
            margin: 0;
            position: relative;
        }

        .picContainer {
            width: 100%;
            height: 100%;
            position: fixed;
            z-index: -1;
        }

        .picContainer img {
            width: 100%;
            height: 100%;
        }

        .loginContainer {
            width: 600px;
            position: fixed;
            z-index: 10;
            top: 20%;
            left: 43%;
            margin-left: -200px;
            color: #FFFFFF;
        }

        .pickImage {
            width: 300px;
            text-align:center;
        }

        .loginWrapper {
            width: 100%;
            padding: 0px 15px 25px 15px;
            background-color: rgba(250, 250, 250, 0.4);
            height: 300px;
            display: table;
        }

        .loginWrapper * {
            color: #FFFFFF;
        }

        @media \0screen\,screen\9 {
            /*针对IE6,IE7,IE8*/
            .loginWrapper {
                width: 100%;
                padding: 10px 20px 20px 20px;
                background-color: rgb(220, 220, 220);
                filter: Alpha(opacity=30); /*仅支持ie*/
                position: static; /* IE6、7、8只能设置position:static(默认属性) ，否则会导致子元素继承Alpha值 */
                *zoom: 1; /* 激活IE6、7的haslayout属性，让它读懂Alpha */
                height: 300px;
            }
            .loginWrapper * {
                position: relative; /* 设置子元素为相对定位，可让子元素不继承Alpha值 */
            }
        }
        .loginWrapper .logo {
            width: 100%;
            height: 60px;
            font-size: 20px;
            font-weight: 800;
            text-align: center;
            font-family: Helvetica, Tahoma, Arial, sans-serif;
        }
        .loginWrapper input {
            color: #222d32;
            border-color: #76ccdc;
        }
        .login_input {
            padding-left: 60px;
            width: 260px;
        }
        .login_button {
            padding-left: 60px;
            width: 260px
        }
        .loginform {
            display: table-cell;
            vertical-align: middle;
            padding-top: 20px;
        }
    </style>
</head>
<body>
<div class="mainContainer">
    <div class="picContainer">
        <img src="${img.loginBg}" alt="请添加LoginImg.bg">
    </div>
    <div class="loginContainer">
        <div style="padding-bottom: 20px">
            <img src="${img.topImage}" alt="请添加LoginImg.top配置项">
        </div>
        <div class="loginWrapper">
            <div class="loginform">
                <img src="${img.pickLogin}" style="width: 300px;" alt="请添加LoginImg.pick配置项"/>
            </div>
            <div class="loginform">
            <form id="loginform" action="/admin/login" method="post" class="form-horizontal"
                  style="padding-top: 15px;float: right;margin-right: 30px">
            <#if tenants??>
                <div class="login_input form-group">
                    <select class="form-control" name="tid" id="tenantId" onchange="saveTenantId();" style="color: #777">
                        <#list tenants as tenant>
                            <option value="${tenant.id}">${tenant.name}</option>
                        </#list>
                    </select>
                </div>
            </#if>
                <div class="login_input form-group">
                    <input class="form-control" type="text" placeholder="请输入您的帐号" autocomplete="off" name="username"
                           id="username">
                </div>
                <div class="login_input form-group">
                    <input class="form-control" type="password" placeholder="请输入密码" autocomplete="off" name="password"
                           id="password">
                    <input class="form-control" style="width: 50%; margin-top: 10px;float: left" type="text"
                           placeholder="请输入验证码" autocomplete="off" id="patchca" name="patchca" maxlength="4"/>
                    <img id="patchcaImg" src="patchca" onclick="this.src='patchca?d=' + new Date().getTime()"
                         style="color:red;float: right;margin-top:10px" ; width="45%" ; height="30px"/>
                </div>
                <div class="form-group">
                    <div  style="margin-bottom: 10px;">
                        <div class="login_button" style="display: inline">
                            <label style="color: #777"><input type="checkbox" name="rememberMe" id="rememberMe">&nbsp;15天内免登陆</label>
                        </div>
                        <a href="/admin/retrieve" style="display: inline; color: blue; float: right">找回密码</a>
                    </div>
                    <div class="login_button">
                        <button class="btn btn-block  btn-info" id="login">登录</button>
                    </div>
                </div>
                <div id="login_error" style="color:red; text-align:center; margin-bottom:5px; float: right;">${error?if_exists}</div>
            </form>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    function getCookie(c_name) {
        if (document.cookie.length > 0) {
            c_start = document.cookie.indexOf(c_name + "=");
            if (c_start != -1) {
                c_start = c_start + c_name.length + 1;
                c_end = document.cookie.indexOf(";", c_start);
                if (c_end == -1) c_end = document.cookie.length;
                return unescape(document.cookie.substring(c_start, c_end));
            }
        }
        return "";
    }
    function setCookie(c_name, value, expiredays) {
        var exdate = new Date();
        exdate.setDate(exdate.getDate() + expiredays);
        document.cookie = c_name + "=" + escape(value) + ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString());
    }
    $(document).ready(function () {
        $("#login").click(function () {
            var username = $("input[name='username']").val();
            var password = $("input[name='password']").val();
            var patchca = $("input[name='patchca']").val();
        });
        $("body").on("keydown", function (event) {
            if (event.keyCode == 13) return login();
        });
    <#if tenants??>
        var tenantId = getCookie("tenantId");
        if (tenantId != null) $("#tenantId").val(tenantId);
    </#if>
    });
    function saveTenantId() {
        var tenantId = $("#tenantId").val();
        setCookie("tenantId", tenantId, 365);
    }

</script>
</html>