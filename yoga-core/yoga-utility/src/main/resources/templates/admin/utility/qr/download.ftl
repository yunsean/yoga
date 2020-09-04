<html>
<head>
    <title>扫码互动</title>
    <meta charset="utf-8">
    <link rel="shortcut icon" href="/admin/images/qr_favicon.ico" />
    <script src="/admin/jquery/jquery-3.3.1.js" charset="UTF-8"></script>
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
            margin-top:10%;
            height: 250px;
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
        a{text-decoration: none;}
        a:visited{text-decoration: none;}
        a:hover {text-decoration: none;}
        a:active{text-decoration:none;}
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row content-bottom">
        <div class="col-sm-12">
            <div class="col-sm-6 col-sm-offset-3">
                <div id="outer">
                    <div id="inner_top">
                        <div style="word-wrap:break-word; padding: 0 20px">
                            <h3>${(file.filename)!}</h3>
                        </div>
                        <div style="margin-top: 20px">
                            <small>文件大小：${(file.fileSize?c)!} Bytes</small>
                        </div>
                        <div style="margin-top: 50px">
                            <a href="${(file.remoteUrl)!}">点击下载</a>
                        </div>
                    </div>
                    <div id="inner_bottom">
                        <div>
                            <div class="infoDetail">
                                <a href="/admin/qr">重新扫码</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>