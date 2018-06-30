<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<!DOCTYPE html>
<html>
<@macroCommon.html/>
<link href="/css/zui.min.css" rel="stylesheet">

<!-- END HEAD -->
<!-- BEGIN BODY -->
<body>
<!-- BEGIN CONTAINER -->
<!-- BEGIN CONTENT -->
<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="#"><i class="icon icon-user"></i> 欢迎页面</a></li>
        </ol>
    </div>
        <!-- END PAGE HEADER-->
        <!-- BEGIN DASHBOARD STATS -->
        <section class="content">
            <div class="row">
                <div class="col-md-12">
                    <center>
                    <h1>欢迎使用<@common.platform /></h1>
                    <h3>请从左边的菜单栏选择项目来开始您的工作。</h3>
                    </center>
                </div>
            </div>
        </section>
        <!-- END PAGE CONTENT-->
    <!-- BEGIN FOOTER -->

    <!-- END FOOTER -->
</div>
<footer class="main-footer">
<@common.footer />
</footer>
<!-- END CONTENT -->
<!-- END CONTAINER -->

<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<!-- BEGIN CORE PLUGINS -->

<script>
    if (navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.split(";")[1].replace(/[ ]/g, "") == "MSIE6.0") {
        alertShow("danger", "请升级到最新版本IE或者使用Chrome、FireFox、360浏览器!", 20000);
    }
    else if (navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.split(";")[1].replace(/[ ]/g, "") == "MSIE7.0") {
        alertShow("danger", "请升级到最新版本IE或者使用Chrome、FireFox、360浏览器!", 20000);
    }
    else if (navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.split(";")[1].replace(/[ ]/g, "") == "MSIE8.0") {
        alertShow("danger", "请升级到最新版本IE或者使用Chrome、FireFox、360浏览器!", 20000);
    }
    else if (navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.split(";")[1].replace(/[ ]/g, "") == "MSIE9.0") {

    }
</script>
<!-- AdminLTE App -->

<script>
    jQuery(document).ready(function ()
    var show = false;
    $("#firstMenuButton").on("click", function () {
        if (show == false) {
            showMenu();
            setTimeout("hideMenu()", 5000);
        } else {
            hideMenu();
        }
    });
    $("#logout").on("click", function () {
        $.post("/loginOut.json", {}, function (data) {
            window.parent.location.href = data.url;
        });
    });
    })
    ;
    function showMenu() {
        $("li.navMenu").addClass("common").removeClass("hidden");
        $("#buttonArrow").attr("class", "fa fa-angle-right");
        window.show = true;
    }
    function hideMenu() {
        $("li.navMenu").addClass("hidden").removeClass("common");
        $("#buttonArrow").attr("class", "fa fa-angle-left");
        window.show = false;
    }
</script>

<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>