<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh">
<head>
    <meta charset="utf-8">
    <title><@tenantTags tag="platform"/></title>
    <link rel="shortcut icon" href="<@tenantTags tag="icon"/>" />
    <link href="/admin/css/404.css" type="text/css"  rel="stylesheet" />
</head>
<!--/************************************************************
 *																*
 * 						      代码库							*
 *                        www.dmaku.com							*
 *       		  努力创建完善、持续更新插件以及模板			*
 * 																*
**************************************************************-->
<body>
<div class="mainPage" id="mainPage">
    <div class="errorMsgBox">
        <div class="errorMsgContentBox">
            <h5>${message!'页面不存在!'}</h5>
            <div class="error_help">
                <p>您可以尝试以下操作</p>
                <ul>
                    <li><a href="/admin/welcome">返回首页</a></li>
                    <li><a href="javascript:window.history.back()">回上一页</a></li>
                </ul>
            </div>
        </div>
        <div class="error_num">Error  404</div>
    </div>
</div>
</body>
</html>