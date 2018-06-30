<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<@macroCommon.html>
<link href="/zuiChosen/chosen.min.css" rel="stylesheet">

<body>
<div class="wrapper">
    <div class="container-fluid">
        <div class="row">
            <ol class="breadcrumb">
                <li><a href="#"><i class="icon icon-dashboard"></i>调试工具</a></li>
                <li>API接口</li>
            </ol>
        </div>
        <div class="row content-bottom">
            <div class="col-lg-12">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <i class="icon icon-comments-alt"></i>
                        API接口
                        <a style="float: right;" href="/debug/methods/refresh" class="btn btn-sm btn-success">刷新</a>
                    </div>
                    <div class="panel-body">
                        <nav class="menu" data-ride="menu" style="width:20%;float: left">
                            <form action="/debug/methods" class="form-inline" style="margin-bottom: 10px;">
                                <div class="input-group" style="width:100%">
                                    <input type="text" class="form-control" placeholder="接口名称" name="filter" value="${filter?if_exists}">
                                    <span class="input-group-btn">
                                        <button type="submit" class="btn btn-success">
                                            <i class="icon icon-search"></i>搜索
                                        </button>
                                    </span>
                                </div>
                            </form>
                            <div class="form-group">
                                <label>接口类型</label>
                                <select onchange="switchArea();" class="form-control" id="groupName" name="groupName" tabindex="2">
                                    <option value="0">请选择接口类型</option>
                                    <#list groups as group>
                                        <option value="${group_index}" name="${group.name?if_exists}">${group.name?if_exists}</option>
                                    <#else>
                                        <option value="">无接口</option>
                                    </#list>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>接口列表</label>
                                <select id="methods" class="chosen-select form-control">
                                </select>
                            </div>
                        </nav>
                        <iframe id="iframe" style="width: 79%; min-height: 1200px; margin-left: 10px" frameborder="0" scrolling="no" onload="setIframeHeight()" src="/debug/methods/call">
                        </iframe>
                    </div>
                    <div class="box-footer" style="text-align:center">
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
<script src="/zuiChosen/zui.min.js"></script>
<script src="/zuiChosen/chosen.min.js"></script>
<script>
    $("#browser").on("click", "li.nav-parent>a", function () {
        $(this).parent("li.nav-parent").find("ul.nav").slideToggle(400);
        $(this).find("i.nav-parent-fold-icon").toggleClass("icon-rotate-90");
        $(this).parent("li.nav-parent").siblings("li.nav-parent").find("ul.nav").slideUp(400);
        $(this).parent("li.nav-parent").siblings("li.nav-parent").removeClass("active");
        $(this).parent("li.nav-parent").siblings("li.nav-parent").find("a>i.icon-rotate-90").removeClass("icon-rotate-90");
    });
    $('.menu .nav').on('click', 'li', function () {
        var $this = $(this);
        $('.menu .nav .active').removeClass('active');
        $this.closest('li').addClass('active');
        $this.closest('.nav-parent').addClass('active');
    });
    function switchArea() {
        var groupVal = $("#groupName").val();
        var groupName=$("#groupName").find("option:selected").text();
        $.get(
                "/api/debug/i/list",
                function (data) {
                    if (data.code < 0) {
                        $("#methods").empty();
                        alertShow("danger", data.message, 3000);
                    } else {
                        var communityIds = $("#methods");
                        communityIds.empty();
                        communityIds.append("<option value='0'>请选择接口</option>");
                        var item = data.result[groupVal];
                       for (var i =0; i< data.result[groupVal].methods.length; i++){
                           var option = data.result[groupVal].methods[i];
                           communityIds.append("<option value='"+option.url+"'>" + option.explain + "</option>");
                       }
                    }
                    $('select.chosen-select').trigger('chosen:updated');
                    $('select.chosen-select').chosen({
                        no_results_text: '没有找到',    // 当检索时没有找到匹配项时显示的提示文本
                        disable_search_threshold: 10, // 10 个以下的选择项则不显示检索框
                        search_contains: true         // 从任意位置开始检索
                    });
                },
                "json"
        );
        $("#methods").change(function(){
            var methodsVal = $("#methods").val();
            doCall(methodsVal,groupName);
        });
    }
</script>
<script>
    function setIframeHeight() {
        var iframeid = document.getElementById("iframe");
        if (document.getElementById) {
            if (iframeid && !window.opera) {
                if (iframeid.contentDocument && iframeid.contentDocument.body.offsetHeight) {
                    iframeid.height = iframeid.contentDocument.body.offsetHeight + 40;
                } else if (iframeid.Document && iframeid.Document.body.scrollHeight) {
                    iframeid.height = iframeid.Document.body.scrollHeight + 40;
                }
            }
        }
    };
    window.onload = function () {
        setIframeHeight();
    };
    function doCall(url, name) {
        if (url == null || url == "") return;
        url = encodeURIComponent(url);
        name = encodeURIComponent(name);
        $("iframe").attr("src", "/debug/methods/call?group=" + name + "&url=" + url);
    };
</script>
</@macroCommon.html>
