<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>账号绑定</title>
    <link rel="stylesheet" href="<@tenantTags tag="resource"/>/mzui/css/mzui.min.css">
    <script src="<@tenantTags tag="resource"/>/plugins/jQuery/jquery-1.12.3.min.js"></script>
    <script type="text/javascript">
        function countDown(secs, surl){
            var jumpTo = document.getElementById('jumpTo');
            jumpTo.innerHTML = secs;
            if(--secs > 0){
                setTimeout("countDown("+secs+", '"+surl+"')", 1000);
            } else {
                location.href=surl;
            }
        }
    </script>
</head>
<body class='has-index-content'>
<div class="loading loading-light" id="pageLoading"></div>
<div id="partial" class="container display fade in" data-display-name="navs">
    <div class="page enter-from-bottom display fade in" id="login1">
        <div id="login" class="column fluid-v no-margin flex-nowrap">
            <div class="cell box margin-5rem flex flex-column">
                <div class="center-block flex flex-column has-padding-h" style="width: 100%">
                    你还没有绑定系统账号，即将跳转到登录页面！
                    <br>
                    请在登录页面输入系统账号和密码，确认后将自动当前微信账号和系统账号的绑定！
                    <br><br>
                    <div style="display: inline">
                    <span id="jumpTo" style="color: red">3</span>秒后自动跳转到<a href="${url!}" style="color: blue">绑定登录</a>
                    </div>
                    <script type="text/javascript">
                        countDown(3, '${url!}');
                    </script>
                </div>
                <div class="cell"></div>
            </div>
        </div>
    </div>
</div>
<script src="<@tenantTags tag="resource"/>/mzui/js/mzui.min.js"></script>
</body>
<script>
    $(function () {
        $("#pageLoading").hide();
    });
</script>
</html>
