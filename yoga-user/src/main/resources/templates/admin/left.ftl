<#import "/macro_common.ftl" as macroCommon>
<!DOCTYPE html>
<@macroCommon.html>
<html >
<head>
    <!--[if lt IE 9]>
    <script src="lib/ieonly/html5shiv.js"></script>
    <script src="lib/ieonly/respond.js"></script>
    <script src="lib/ieonly/excanvas.js"></script>
    <![endif]-->
    <style>
        body{
            font-family: Helvetica, Tahoma, Arial, sans-serif;
            font-size: 14px;
            line-height: 1.53846154;
            color: #FFFFFF;
            background-color:#364150;
        }
        nav{
            width:160px;
            display:block;
            margin-top:20px;
        }
        nav.menu>ul.nav{
            padding-left: 0;
            margin-bottom: 0;
            list-style: none;
            margin-top:0;
        }
        nav.menu>ul.nav>li{
            display:block;
            position:relative;
            float:none;
        }
        nav.menu>ul.nav>li,nav.menu>ul.nav>li>a{
            margin-left:0;
            color:#FFFFFF;
        }
        nav.menu>ul.nav>li:first-child{
            margin-top:0;
        }
        nav.menu>ul.nav>li:first-child>a,nav.menu>ul.nav>li:last-child>a{
            border-top-left-radius: 0;
            border-top-right-radius: 0;
            border-bottom-right-radius: 0;
            border-bottom-left-radius: 0;
        }
        nav.menu>ul.nav-primary>li>a{
            border-top:1px solid #6F6F6F;
            border-bottom:1px solid #6F6F6F;
            border-left:none;
            border-right:none;
        }
        nav.menu>ul.nav-primary>li.active>a,nav.menu>ul.nav-primary>li>a:focus,nav.menu>ul.nav-primary>li>a:hover{
            background-color:#2c3542;
            color:#FFFFFF;
            border-color: #6F6F6F;
        }
        nav.menu>ul.nav>li>ul.nav>li>a,.menu>ul.nav>li>ul.nav>li>a:focus,.menu>ul.nav >li>ul.nav>li>a:hover{
            border:none;
        }
        nav.menu>ul.nav>li>ul.nav>li>a{
            background-color:#384552;
            color:#FFFFFF;
        }
        nav.menu>ul.nav>li>ul.nav>li>a:hover{
            background-color:#A0A0A0;
            color:#FFFFFF;
        }
        .menu>ul.nav>li>ul.nav>li.active>a,.menu>.nav>li>.nav>li.active>a:hover,.menu>.nav>li>.nav> li.active>a:focus{
            background-color:#1caf9a;
            color:#FFFFFF;
            border:none;
        }
        nav.menu > ul.nav > li > ul.nav > li > a {
        background-color: #384552;
        color: #FFFFFF;
        }
        .menu>.nav>li.show>a, .menu>.nav>li.show>a:focus, .menu>.nav>li.show>a:hover{
        color:#FFFFFF;
        background-color: #384552;
        border-color: #6F6F6F;
        }
        .menu>.nav>li.show>a:focus>[class*=icon-], .menu>.nav>li.show>a:hover>[class*=icon-], .menu>.nav>li.show>a>[class*=icon-] {
        color: #FFFFFF;
        }
    </style>
</head>
<body>
<!--[if lt IE 8]>
<div class="alert alert-danger">您正在使用 <strong>过时的</strong> 浏览器. 是时候 <a href="http://browsehappy.com/">更换一个更好的浏览器</a>
    来提升用户体验.
</div>
<![endif]-->
<nav class="menu" data-toggle="menu">
    <ul class="nav nav-primary" id="menusList">

    </ul>
</nav>
<script type="text/javascript">
    function sidebarInit(){
        $.post("/admin/findMenus", function (result) {
            $("#menusList").empty();
            var primaryLength = result.modules.length;
            for (var i = 0; i < primaryLength; i++) {
                var primaryIcon = result.modules[i].icon;
                var primaryName = result.modules[i].name;
                var primaryHref;
                var primaryMenu;
                if (result.modules[i].url) {
                    primaryHref = result.modules[i].url;
                } else {
                    primaryHref = "javascript:void(0)";
                }
                var subTreeLength;
                var gettype = Object.prototype.toString;
                if (gettype.call(result.modules[i].children) == "[object Array]") {
                    primaryMenu = "<li id='primary-" + i + "' class='" + "nav-parent" + "'>" + "<a href='" + primaryHref + "'><i class='icon " + primaryIcon + "'></i><span> " + primaryName + " </span><i class='icon-chevron-right nav-parent-fold-icon'></i></a>";
                    subTreeLength = result.modules[i].children.length;
                } else {
                    primaryMenu = "<li id='primary-" + i + "'>" + "<a target='mainFrame' href='" + primaryHref + "'><i class='icon-" + primaryIcon + "'></i>" + primaryName + "</a>";
                    subTreeLength = 0;
                }
                var subTree = "<ul class='nav ulNav'>";
                for (var j = 0; j < subTreeLength; j++) {
                    var subItemName = result.modules[i].children[j].name;
                    var subItemHref = result.modules[i].children[j].url;
                    subTree += " <li><a target='mainFrame' href='" + subItemHref + "'><i class='fa fa-circle-o'></i>&nbsp;&nbsp;" + subItemName + "</a></li>";
                }
                subTree += "</ul>";
                primaryMenu += subTree;
                primaryMenu += "</li>";
                $("#menusList").append(primaryMenu);
            }
        });
    }
    var num =0;
    function sidebarSlide(){
        $("#menusList").on("click","li.nav-parent>a",function(){
        $(this).parent("li.nav-parent").find("ul.nav").slideToggle(400);
        $(this).parent("li.nav-parent").toggleClass("active");
        $(this).parent("li.nav-parent").siblings('li').removeClass('show');
        $(this).find("i.nav-parent-fold-icon").toggleClass("icon-rotate-90");
        $(this).parent("li.nav-parent").siblings("li.nav-parent").find(".ulNav").slideUp(400);
        $(this).parent("li.nav-parent").siblings("li.nav-parent").removeClass("active");
        $(this).parent("li.nav-parent").siblings("li.nav-parent").find("a>i.icon-rotate-90").removeClass("icon-rotate-90");
    });
        var prevClick=$("ul.nav>li>a");
        $("#menusList").on("click","ul.nav>li>a",function(){
            prevClick.parent("li").removeClass("active");
            $(this).parent("li").addClass("active");
            prevClick=$(this);
        });
    }
    $(document).ready(function(){
        sidebarInit();
        sidebarSlide()
    });
</script>
</body>
</@macroCommon.html>