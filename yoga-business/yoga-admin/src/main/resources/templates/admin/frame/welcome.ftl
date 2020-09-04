<#include "/page.component.ftl">
<@html>
    <@head>
    <script src="/admin/echarts/echarts.min.js"></script>
    <style>
        .scount {
            font-size: 64px;
            text-align: center!important;
            color: red;
        }
        .sname {
            font-size: 12px;
            text-align: center!important;
        }
        .info-box {
            display: block;
            min-height: 90px;
            width: 100%;
            border-radius: 4px;
            margin-bottom: 15px;
        }

        .info-box-icon {
            border-top-left-radius: 4px;
            border-top-right-radius: 0;
            border-bottom-right-radius: 0;
            border-bottom-left-radius: 4px;
            display: block;
            float: left;
            height: 90px;
            width: 90px;
            text-align: center;
            font-size: 45px;
            line-height: 90px;
            background: rgba(0, 0, 0, 0);
        }

        .info-box-icon .icon {
            text-align: center;
            font-size: 45px;
        }
        .info-box-icon .img {
            width: 64px;
            height: auto;
        }

        .info-box-content {
            padding: 18px 10px;
            margin-left: 90px;
        }

        .info-box-text {
            display: block;
            font-size: 14px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .info-box-number {
            display: inline-block;
            font-weight: bold;
            font-size: 24px;
        }
        .info-box-units {
            display: inline-block;
            font-weight: bold;
            font-size: 12px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
        .info-ml5 {
            margin-left: 5px;
        }
        .info-mt10 {
            margin-top: 10px;
        }
        a{
            text-decoration:none!important;
        }
        .echarts {
            height: 300px;
        }
    </style>
    </@head>
    <@bodyFrame>
    <div class="container-fluid">
        <div class="row">
            <ol class="breadcrumb">
                <li><a href="#"><i class="icon icon-user"></i> 欢迎页面</a></li>
            </ol>
        </div>
            <section>
                <div class="row">
                    <div class="col-md-12">
                        <center>
                            <h1>欢迎使用<@tenantTags tag="platform"/></h1>
                        </center>
                    </div>
                </div>
            </section>
            <#assign bgs=['bg-red', 'bg-green', 'bg-yellow', 'bg-brown', 'bg-purple', 'bg-success', 'bg-blue']/>
            <section class="content">
                <div class="row row_top">
                    <#list schedules as schedule>
                        <a class="col-md-2 col-sm-4 col-xs-6" href="${(schedule.url)!'#'}">
                            <div class="info-box ${bgs[schedule_index % bgs?size]}">
                                <div class="info-box-icon">
                                    <#if schedule.image! != ''>
                                        <img class="img" src="${schedule.image!}" />
                                    <#else>
                                        <i class="icon ${(schedule.icon)!}"></i>
                                    </#if>
                                </div>
                                <div class="info-box-content">
                                    <span class="info-box-text">${(schedule.name)!}</span>
                                    <#if schedule.value! != ''>
                                    <span class="info-box-number">${(schedule.value)!}</span>
                                    </#if>
                                    <span class="info-box-units pl <#if schedule.value! != ''>info-ml5<#else>info-mt10</#if>">${(schedule.units)!}</span>
                                </div>
                            </div>
                        </a>
                    </#list>
                </div>
            </section>
            <section class="content">
                <div class="row row_top">
                    <#list statements! as statement>
                        <div id='echart_${statement_index?c}' class='echarts col-md-4 col-sm-6 col-xs-12'></div>
                        <script>
                            var json = JSON.parse('${statement.json!}');
                            echarts.init(document.getElementById('echart_${statement_index?c}')).setOption(json);
                        </script>
                    </#list>
                </div>
            </section>
    </div>
    </@bodyFrame>
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
<script>
    jQuery(document).ready(function () {
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
        $("#buttonArrow").attr("class", "icon icon-angle-right");
        window.show = true;
    }
    function hideMenu() {
        $("li.navMenu").addClass("hidden").removeClass("common");
        $("#buttonArrow").attr("class", "icon icon-angle-left");
        window.show = false;
    }
</script>
</@html>