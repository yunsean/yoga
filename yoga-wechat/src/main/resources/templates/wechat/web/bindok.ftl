<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><@common.platform /></title>
    <link rel="stylesheet" href="<@tenantTags tag="resource"/>/mzui/css/mzui.min.css">
    <script src="<@tenantTags tag="resource"/>/plugins/jQuery/jquery-1.12.3.min.js"></script>
</head>
<body class='has-index-content'>
<div class="loading loading-light" id="pageLoading"></div>
<div id="partial" class="container display fade in" data-display-name="navs">
    <div class="page enter-from-bottom display fade in" id="login1">
        <div id="login" class="column fluid-v no-margin flex-nowrap">
            <div class="cell box margin-5rem flex flex-column">
                <div class="cell"></div>
                <div class="layer center-block flex flex-column has-padding-h" style="width: 80%;">
                    <div class="cell"></div>
                    <div class="cell">
                        ${username?if_exists}你好，账号绑定成功，请点击左上角关闭按钮！
                    </div>
                    <div class="cell"></div>
                </div>
                <div class="cell"></div>
            </div>
        </div>
    </div>
</div>
<script src="<@tenantTags tag="resource"/>/mzui/js/mzui.min.js"></script>
</body>
</html>
