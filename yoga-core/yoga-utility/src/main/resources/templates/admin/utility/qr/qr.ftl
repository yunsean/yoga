<html>
<head>
    <title>扫码互动</title>
    <meta charset="utf-8">
    <link rel="shortcut icon" href="/images/qr_favicon.ico" />
    <script src="/jquery/jquery-3.3.1.js" charset="UTF-8"></script>
    <style>
        #outer {
            position: absolute;
            top: 48%;
            height: 400px;
            margin-top: -200px;
            left: 50%;
            border: 1px solid #DDDDDD;
            box-shadow: 3px 3px 3px 3px #DDDDDD;
            width: 500px;
            margin-left: -250px;
        }
        #inner_top {
            text-align: center;
            margin: 0px auto;
            margin-top:10%
        }
        #inner_bottom {
            text-align: left;
            margin: 0px auto;
            word-break: break-all;
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
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row content-bottom">
        <div class="col-sm-12">
            <div class="col-sm-6 col-sm-offset-3">
                <div id="outer">
                    <div id="inner_top">
                        <img id="qrbarImg" src="/admin/qr/chart?code=${code?if_exists}" width="276px" height="276px" alt="请等待系统生成二维码">
                    </div>
                    <div id="inner_bottom">
                        <div>
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
                "/admin/qr/query.json?code=${code?if_exists}",
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
</html>