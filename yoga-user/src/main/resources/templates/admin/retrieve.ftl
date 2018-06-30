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
            color: #555555;
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
        .loginWrapper {
            width: 100%;
            padding: 0px 15px 25px 15px;
            background-color: rgba(250, 250, 250, 0.4);
            height: 300px;
            display: table;
        }
        .loginWrapper * {
            color: #555555;
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
        .loginWrapper {
            width: 100%;
            height: 60px;
            font-size: 20px;
            font-weight: 800;
            text-align: center;
            font-family: Helvetica, Tahoma, Arial, sans-serif;
        }
        .loginWrapper input {
            color: #555555;
            border-color: #76ccdc;
        }
        .loginform {
            display: table-cell;
            vertical-align: middle;
            padding-top: 20px;
        }
        label {
            color: #ffffff;
        }
        button {
            color: #ffffff;
        }
    </style>
</head>
<body>
<div class="mainContainer">
    <div class="picContainer">
        <img src="${img.loginBg}" alt="请添加LoginImg.bg">
    </div>
    <div class="loginContainer">
        <div class="loginWrapper">
            <div class="loginform">
                <div id="updatePassword" class="form-horizontal form-bordered">
                    <div class="form-group">
                        <label class="col-md-4">手机号：</label>
                        <div class="col-md-6" style="font-size: 16px; padding-top: 3px">
                            <input id="mobile" type="text" class="form-control">
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4">验证码：</label>
                        <div class="col-md-4">
                            <input id="captcha" type="text" class="form-control">
                            <span class="help-block"></span>
                        </div>
                        <button class="btn btn-info col-md-2" onclick="sendCaptcha();" type="button">发送验证码</button>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4">新密码：</label>
                        <div class="col-md-6">
                            <input id="newPwd" type="password" class="form-control">
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4">重复新密码：</label>
                        <div class="col-md-6">
                            <input id="rePwd" type="password" class="form-control">
                            <span class="help-block"></span>
                        </div>
                    </div>
                </div>
                <input id="uuid" type="hidden">
                <div class="col-sm-12">
                    <div class="col-sm-6 col-sm-offset-3">
                        <button type="input" onclick="doSubmit();" class="col-sm-6 btn btn-default">确认修改</button>
                        <button class="col-sm-4 btn btn-default col-sm-offset-2" onclick="history.back(-1);">取消</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <footer class="main-footer">
    <@common.footer />
    </footer>
</div>
</body>
<script>
    function doSubmit() {
        var mobile = $("#mobile").val();
        var uuid = $("#uuid").val();
        var captcha = $("#captcha").val();
        var newPwd = $("#newPwd").val();
        var rePwd = $("#rePwd").val();
        if (newPwd != rePwd) {
            alertShow("warning", "两次输入的密码不一致！", 3000);
            return;
        }
        $.post("/api/auth/retrieve",
                {
                    mobile: mobile,
                    uuid: uuid,
                    captcha: captcha,
                    newPwd: newPwd
                },
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        window.location.href = "/admin/login";
                    }
                }
        )
    }
    function sendCaptcha() {
        var mobile = $("#mobile").val();
        if (mobile == "") {
            alertShow("warning", "请输入手机号！", 3000);
            return;
        }
        $.post("/api/auth/captcha",
                {mobile: mobile},
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#uuid").val(data.result.uuid);
                        $("#captcha").val(data.result.captcha);
                        alertShow("info", "验证码已经发送到你手机，请查收短信！", 3000);
                    }
                }
        )
    }
</script>
</html>