<#import "/macro_paging.ftl" as html>
<!DOCTYPE html>
<#import "/macro_common.ftl" as macroCommon>
<@macroCommon.html/>
<html>
<head>
    <style type="text/css">
        .typeLabel {
            text-align: center;
        }

        .typeContentWrapper {
            border: 1px solid rgb(235, 235, 235);
            height: 15em;
            overflow-y: scroll;
        }

        .typeContent {
            list-style: none;
            padding-left: 0px;
        }

        .typeItem {
            display: block;
            padding-left: 1px;
        }

        .typeItem a {
            display: block;
            text-decoration: none;
            color: rgb(30, 30, 30);
            font-size: 14px;
            background-color: rgb(235, 235, 235);
            margin: 2px;
        }

        .typeTotalInfo {
            font-size: 14px;
        }

        .choiced {
            background-color: #4b8df8;
        }

        .hidden {
            display: none;
        }

        .oneLine {
            margin: 0px;
            border-bottom: 1px solid #efefef;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i> 修改密码</a></li>
            <span class="pull-right">
                        <button class="btn btn-primary btn-sm" onclick="history.back(-1);">返回</button>
                    </span>
        </ol>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="fa fa-comments-o"></i>
                    修改密码
                </div>
                <div class="panel-body">
                    <div class="tableToolContainer" style="margin-bottom:15px">
                        <div id="updatePassword" class="form-horizontal form-bordered">
                            <div class="form-group">
                                <label class="col-md-4">账号</label>
                                <div class="col-md-4" style="font-size: 16px; padding-top: 3px">
                                ${user.username?if_exists}
                                    <span class="help-block"></span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4">原密码</label>
                                <div class="col-md-4">
                                    <input name="oldPwd" id="oldPwd" type="password" class="form-control">
                                    <span class="help-block"></span>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-4">新密码</label>
                                <div class="col-md-4">
                                    <input name="newPwd" id="newPwd" type="password" class="form-control">
                                    <span class="help-block"></span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4">重复新密码</label>
                                <div class="col-md-4">
                                    <input name="repeatNewPwd" id="repeatNewPwd" type="password" class="form-control">
                                    <span class="help-block"></span>
                                </div>
                            </div>
                        </div>
                        <div class="box-footer col-sm-12">
                            <div class="col-sm-5 col-sm-offset-5">
                                <button type="input" onclick="doSubmit();" class="btn btn-default">确认修改</button>
                                <button class="btn btn-default" onclick="history.back(-1);">取消</button>
                            </div>
                        </div>
                        </form>
                    </div>
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
<script>
    function doSubmit() {
        var oldPwd = $("#oldPwd").val();
        var newPwd = $("#newPwd").val();
        var repeatNewPwd = $("#repeatNewPwd").val();
        if (newPwd != repeatNewPwd) {
            alertShow("danger", "两次输入的新密码不一致！", 3000);
            return;
        }
        $.post("/api/user/passwd",
                {oldPwd: oldPwd, newPwd: newPwd},
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        window.top.location.href = "/admin";
                    }
                }
        )
        ;
    }
</script>
</html>

