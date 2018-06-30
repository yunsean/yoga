<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<@macroCommon.html>
<style>
    #outer {
        width: 50%;
        height: 400px;
        border: 1px solid #DDDDDD;
        box-shadow: 3px 3px 3px 3px #DDDDDD;
        margin: 0px auto;
        text-align: center;
        padding: 10px;
        margin-top: 20%;
    }

    #inner_top {
        /*background: url(img/weixin.jpg);*/
        text-align: center;
        margin: 0px auto;
        margin-top:10%
    }

    #inner_bottom {

        text-align: left;
        margin: 0px auto;
        word-break: break-all;
    }

    #infoBox {

    }

    .infoDetail {
        margin: 5px;
        text-align: center;
    }

    .infoDetail div {

        margin: 5px;
        word-break: break-all;
        display: inline-block;
    }

    .buttonContainer {
        text-align: center;
        margin-top: 20px;
        display: none;
    }
</style>
</head>
<body>
<div class="container-fluid">
    <div class="row content-bottom">
        <div class="col-sm-12">
            <div class="col-sm-6 col-sm-offset-3">
                <div id="outer">
                    <div id="inner_top">
                        <img id="qrbarImg" src="/qr/chart?code=${code?if_exists}" width="276px" height="276px" alt="请等待系统生成二维码">
                    </div>
                    <div id="inner_bottom">
                        <div id="infoBox">
                            <div class="infoDetail">
                                <div><i class="glyphicon glyphicon-indent-right"></i>&nbsp;请通过手机APP扫描上方二维码</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    function query() {
        $.get(
                "/qr/query?code=${code?if_exists}",
                function(data) {
                    var code = data["code"];
                    if (code == "0") {
                        var url = data["result"];
                        window.location.href = url;
                    } else {
                        setTimeout(function(){
                            query();
                        }, 3000);
                    }
                }
        );
    }
    $(function () {
        query();
    });
</script>
</body>
</@macroCommon.html>