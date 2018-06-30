<!DOCTYPE html>
<#import "/macro_common.ftl" as macroCommon>
<@macroCommon.html/>
<html>
<!--[if lt IE 9]>
<script src="lib/ieonly/html5shiv.js"></script>
<script src="lib/ieonly/respond.js"></script>
<script src="lib/ieonly/excanvas.js"></script>
<![endif]-->
<!-- Tell the browser to be responsive to screen width -->
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<style type="text/css">
    body {
        height: 40px;
        margin: 0px;
        padding: 0px;
        font-family: "宋体", Helvetica, Tahoma, Arial, sans-serif;
    }

    .header {
        background-color: #354250;
        /*background:#f1f1f1;*/
        height: 40px;
        color: #FFFFFF;
    }

    .logoWrapper {
        width: 180px;
        height: 40px;
        float: left;
        text-align: center;
        margin: 0px;
        padding: 0px;
    }

    .navbar-menu {
        float: right;
    }

    .navbar-menu-items {
        list-style: none;
        margin: 0px;
        padding: 0px;
        font-size: 14px;
        height: 40px
    }

    .hidden {
        display: none;
    }

    .navbar-menu-items > li.firstMenu, li.common {
        height: 40px;
        line-height: 40px;
        display: inline-block;
        white-space: nowrap;
        overflow: hidden;
    }

    .navbar-menu-items > li.firstMenu > a, .navbar-menu-items > li.common > a {
        display: block;
        text-decoration: none;
        color: #FFFFFF;
        padding: 0px 8px;
        white-space: nowrap;
        overflow: hidden;
    }

    .navbar-menu-items > li > a:hover {
        background-color: rgba(0, 0, 0, 0.2);
        color: #FFFFFF;
    }

    .select {
        background-color: rgba(0, 0, 0, 0.2);
        color: #FFFFFF;
    }
</style>
</head>
<body>
<div class="wrapper">
    <div class="header">
        <div class="logoWrapper">
            <img src="${topImg}" height="40px" alt="logo" style="max-width:1000%"/>
        </div>
        <div class="navbar-menu">
            <ul class="navbar-menu-items">
                <li class="firstMenu">
                    <a href="#" class="menuButton" id="firstMenuButton">
                        <i class="icon icon-user"></i>
                    ${authName?if_exists}
                        <i class="icon icon-chevron-left" id="buttonArrow"></i>
                    </a>
                </li>
                <li class="navMenu hidden">
                <#if authName != 'superadmin'>
                    <a href="/admin/passwd" target="mainFrame" class="menuButton " id="updatePassword">
                        <i class="icon icon-user"></i>
                        修改密码
                    </a>
                </#if>
                </li>
                <li class="navMenu hidden">
                    <a href="/admin/logout" target="_parent" class="menuButton" id="logout">
                        <i class="icon icon-user"></i>
                        退出
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div><!-- ./wrapper -->
<script>
    jQuery(document).ready(function () {
        var show = false;
        $("#firstMenuButton").on("click", function () {
            if (show == false) {
                showMenu();
                setTimeout("hideMenu()", 6000);
            } else {
                hideMenu();
            }
        });

    });
    function showMenu() {
        $("li.navMenu").addClass("common").removeClass("hidden");
        $("#buttonArrow").attr("class", "icon icon-chevron-right");
        window.show = true;
    }
    function hideMenu() {
        $("li.navMenu").addClass("hidden").removeClass("common");
        $("#buttonArrow").attr("class", "icon icon-chevron-left");
        window.show = false;
    }
</script>
</body>
</html>