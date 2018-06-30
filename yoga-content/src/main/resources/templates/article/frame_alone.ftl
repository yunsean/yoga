<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<@macroCommon.html>
<body>
<div class="wrapper">
    <div class="container-fluid">
        <div class="row">
            <ol class="breadcrumb">
                <li><a href="#"><i class="icon icon-dashboard"></i>内容管理</a></li>
                <li>文章管理</li>
            </ol>
        </div>
        <div class="row content-bottom">
            <div class="col-lg-12">
                    <div class="panel panel-primary">
                        <div class="panel-heading">
                            <i class="icon icon-comments-o"></i>文章详情
                        </div>
                        <iframe id="iframe" width=100% height=650px frameborder=0 float:right scrolling=auto src="/cms/article/list?columnId=${columnId?default(0)}&alone=true">
                        </iframe>
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
<script>
    function autoAdjustIFrame() {
        var iframe = document.getElementById("iframe");
        if (iframe) {
            var iframeWin = iframe.contentWindow || iframe.contentDocument.parentWindow;
            if (iframeWin.document.body) {
                iframe.height = iframeWin.document.documentElement.scrollHeight || iframeWin.document.body.scrollHeight;
            }
        }
    };
</script>
</body>
</@macroCommon.html>
