<#import "/macro_common.ftl" as macroCommon>
<@macroCommon.html>

<!DOCTYPE HTML>
<html lang="en-US">
<head>
    <meta charset="UTF-8">
    <title>ueditor demo</title>
    <link href="/wechat.css" rel="stylesheet">
</head>

<body>

    <div class="col-md-offset-3 col-md-6" style="margin-top: 10px; border: 1px ; background-color: rgba(228, 215, 215, 0.15)">
        <h3>${article.title!""}</h3>
        <p>修改日期 ${param.updateTime!""}&nbsp;&nbsp;作者 ${article.author!""}</p>
        <div class="col-md-12">
            <div style="text-align: center">
                <img src="${article.thumbUrl}" alt="" >
            </div>
        </div>
        <div class="col-md-12">
            ${article.content!""}
        </div>
    </div>

</body>
</html>
</@macroCommon.html>