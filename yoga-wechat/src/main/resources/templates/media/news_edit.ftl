<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>

<@macroCommon.html>
<link href="<@macroCommon.resource/>/uploader/zui.uploader.css" rel="stylesheet">
<script src="<@macroCommon.resource/>/uploader/zui.js"></script>
<script src="<@macroCommon.resource/>/uploader/zui.uploader.min.js"></script>
<!DOCTYPE HTML>
<html lang="en-US">
<head>
    <meta charset="UTF-8">
    <title>ueditor demo</title>
    <link href="/wechat.css" rel="stylesheet">
    <link rel="stylesheet" href="/css/news_edit.css">
    <link rel="stylesheet" href="/css/base381ecd.css">
    <link rel="stylesheet" href="/css/jquery.scrollbar.css">
    <style>
        html, body {
            height: 500px;
        }
    </style>
</head>

<body>
<div class="spinner">
    <div class="rect1"></div>
    <div class="rect2"></div>
    <div class="rect3"></div>
    <div class="rect4"></div>
    <div class="rect5"></div>
    <h5> 加载中...</h5>
</div>
<div class="col-md-12" style="margin-top: 20px">
    <div class="main_bd">
        <div class="main_bd_left">
            <#if medias??>
                <#list medias?if_exists as media>
                    <div class="msgItem" style="width: 100%;">
                        <div class="msgContent">
                            <div class="msgInfo" style="border: none">
                                <span>更新于${media.updateTime?date}</span>
                            </div>
                            <div id="existArticles">
                                <#list media.articles?if_exists as article>
                                    <#if article_index == 0>
                                        <div id="appmsgItem" onclick="news_preview(${article.id!0})" data-id="" data-msgindex="0" class="js_appmsg_item appmsg_item_wrp has_thumb current">
                                            <div class="first_appmsg_item" title="第1篇图文">
                                                <div class="cover_appmsg_item">
                                                    <h4 class="appmsg_title">
                                                        <a class="js_appmsg_title" href="javascript:void(0);" onclick="return false;">${article.title?if_exists}</a>
                                                    </h4>
                                                    <div class="appmsg_thumb_wrp js_appmsg_thumb" style="background-image:url('${article.thumbUrl?if_exists}');">
                                                        <!--<img class="js_appmsg_thumb appmsg_thumb" src="https://mmbiz.qlogo.cn/mmbiz_png/Dw0fwKGfAibQUXkG5gIOoj4ic7iaLWjbp2vqnmGt733pQq8hOS3rhLohFkj3nCxrdYzWSibeda15VuTNSH26XhglicQ/0?wx_fmt=png">-->
                                                    </div>
                                                </div>
                                                <div class="appmsg_edit_mask js_readonly">
                                                    <a onclick="return false;" class="icon20_common sort_down_white js_down" data-id="" href="javascript:;" title="下移" style="display: block;">向下</a>
                                                </div>
                                            </div>
                                        </div>
                                    <#else>
                                        <div id="appmsgItem" style="position: relative" onclick="news_preview(${article.id!0})" data-fileid="100000099" data-id="" data-msgindex="1" class="js_appmsg_item appmsg_item_wrp has_thumb">
                                            <div class="appmsg_item has_cover" title="第${article_index+1}篇图文">
                                                <div class="appmsg_thumb_wrp js_appmsg_thumb" style="background-image:url('${article.thumbUrl?if_exists}')">

                                                </div>
                                                <h4 class="appmsg_title js_appmsg_title">${article.title?if_exists}</h4>
                                                <div class="appmsg_edit_mask js_readonly">
                                                    <a onclick="return false;" class="icon20_common sort_up_white   js_up" data-id="" href="javascript:;" title="上移">向上</a>
                                                    <a onclick="return false;" class="icon20_common sort_down_white js_down" data-id="" href="javascript:;" title="下移" style="display: block;">向下</a>
                                                    <a onclick="deleteArticle(${article.id!0});" class="icon20_common del_media_white js_del" data-id="" href="javascript:;" title="删除" style="float: right;margin-top: 10px;">删除</a>
                                                </div>
                                            </div>
                                        </div>
                                    </#if>
                                </#list>
                            </div>
                        </div>
                    </div>
                </#list>
            <#else>
                <div class="msgItem" style="width: 100%;">
                    <div class="msgContent">
                        <div class="msgInfo">
                            <span>更新于${.now}</span>
                        </div>
                        <div id="existArticles">
                            <div class="msgCoverImg firstArticle" id="" ischoosed="true">
                                <h4><a href="#">标题</a></h4>
                                <div class="msgImg" style="background-image: url('');"></div>
                                <a href="#" class="msgImgCover previewMsg" onclick="">
                                    <div class=" msgCoverContent"><p>图文消息</p></div>
                                </a>
                                <div class="picControl edit_show" style="display: none">
                                    <span onclick="doMove()"><i class="fa pencil"></i></span>
                                    <span onclick=deleteArticle();><i class="fa trash"></i></span>
                                </div>
                            </div>
                            <#list (media.articles)?if_exists as article>
                                <#if article_index == 0>
                                    <div class="msgCoverImg" id="${article.id!0}">
                                        <h4><a href="#">${article.title?if_exists}</a></h4>
                                        <div class="msgImg"
                                             style="background-image: url('${article.thumbUrl?if_exists}');"></div>
                                        <a href="#" class="msgImgCover previewMsg"
                                           onclick="news_preview(${article.id!0})">
                                            <div class=" msgCoverContent"><p>编辑文章</p></div>
                                        </a>
                                        <div class="picControl edit_show" style="display: none">
                                            <span onclick="doMove()"><i class="fa pencil"></i></span>
                                            <span onclick=deleteArticle();><i class="fa trash"></i></span>
                                        </div>
                                    </div>
                                <#else>
                                    <div class="appMsg firstMsg" id="${article.id!0}">
                                        <div class="appMsgImg"
                                             style="background-image: url('${article.thumbUrl?if_exists}')"></div>
                                        <h4><a>${article.title?if_exists}</a></h4>
                                        <a href="#" class="msgImgCover firstMsgCover"
                                           onclick="news_preview(${article.id!0})">
                                            <div class="msgCoverContent"><p>编辑文章</p></div>
                                        </a>
                                        <div class="picControl edit_show" style="display: none">
                                            <span onclick="doMove()"><i class="fa pencil"></i></span>
                                            <span onclick=deleteArticle();><i class="fa trash"></i></span>
                                        </div>
                                    </div>
                                </#if>
                            </#list>
                        </div>
                        <div id="addArticle">
                            <div class="msgCoverImg">
                                <div class="msgImg" style="background-image: url('');"></div>
                                <a href="#" class="msgImgCover previewMsg" onclick="addArticle();">
                                    <div class=" msgCoverContent"><p>新增</p></div>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="picControl">
                    <span onclick="show_edit()" style="width: 100%"><i class="fa pencil"></i></span>
                </div>
            </#if>
        </div>
        <div class="main_bd_center">
            <form action="" id="saveNewsForm">
                <div style="display: none">
                    <input type="" value="${param.accountId!""}" name="accountId">
                    <input type="" value="${param.materialId?default(0)?c}" name="materialId">
                    <input type="" value="${(article.id)!""}" class="form-temp" name="articleId">
                    <h1>hashcode: <input type="" value="" id="hashcode"></h1>
                </div>
                <div class="main_bd_center_header">
                    <div class="form-group">
                        <label for="">标题：</label>
                        <input type="text" class="form-temp" id="" value="${(article.title)!""}" name="title"
                               placeholder="请输入标题">
                    </div>
                    <div class="form-group">
                        <label for="">作者：</label>
                        <input type="text" class=" form-temp" id="" value="${(article.author)!""}" name="author"
                               placeholder="请输入作者">
                    </div>
                </div>
                <script id="container" class="form-temp" type="text/plain" name="content">
                    ${(article.content)!"在此处输入图文消息"}
                </script>
                <div class="main_bd_center_footer">
                    <div class="footer_one">
                        <div class="form-group">
                            <label for="">原文地址:</label>
                            <input type="text" class="form-temp" id="" value="${(article.sourceUrl)!""}" name="sourceUrl">
                        </div>
                        <div class="form-group">
                            <label for="">跳转地址：</label>
                            <input type="text" class="form-temp" id="" value="${(article.clickUrl)!""}" name="clickUrl">
                        </div>
                    </div>
                    <div class="footer_two">
                        <div class="form-group">
                            <div for="" style="font-weight: bold">封面图片:</div>
                            <div style="margin: 5px 0">
                                <a type="input" class="btn btn-default">
                                    <i class="fa fa-fw fa-upload"></i>点击选择
                                </a>
                            </div>
                            <img src="${(article.thumbUrl)!""}" id="thumbImg" alt="" style="width: 188px; height: 120px">
                        </div>
                        <input type="hidden" class="form-control form-temp" id="" value="${(article.thumbUrl)!""}"
                               name="thumbUrl">
                        <input type="hidden" class="form-temp" id="thumbMediaId" value="${(article.thumbMediaId)!""}"
                               name="thumbMediaId">
                        <input type="hidden" value="0" name="showConverImage">
                    </div>
                    <div class="form-group">
                        <label for="">摘要</label>
                        <input type="text" class="form-control form-temp" id="" value="${(article.digest)!""}" name="digest"
                               style="width: 600px;">
                    </div>

                </div>
            </form>
        </div>

        <div class="main_bd_right appmsg_tpl_area">
            <div class="appmsg_container_bd">
                <ul id="js_media_list" class="tpl_list">
                    <li class="tpl_item pic" onclick="choiceImage(this)">
                        <i class="icon_media_choose" style=""></i>图片
                    </li>
                    <li class="tpl_item vid" onclick="choiceVideo(this)">
                        <i class="icon_media_choose"></i>视频
                    </li>
                    <li class="tpl_item aud" onclick="choiceVoice(this)">
                        <i class="icon_media_choose"></i>音频
                    </li>
                    <li class="tpl_item vot" style="">
                        <i class="icon_media_choose"></i>投票
                    </li>
                    <li class="tpl_item" style="display:none;">
                        <i class="icon_media_choose"></i>卡券
                    </li>
                    <li class="tpl_item app" style="">
                        <i class="icon_media_choose"></i>小程序
                    </li>
                </ul>
                <div id="media_list_mask" style="display: none;" class="share_appmsg_disabled_mask"></div>
            </div>
        </div>
    </div>
</body>

<script type="text/javascript" src="/ueditor/ueditor.config.js"></script>
<script type="text/javascript" src="/ueditor/ueditor.all.js"></script>
<script type="text/javascript">
    var ue = UE.getEditor('container');

    function news_preview(id) {
        var $articleId = $("[name='articleId']");
        if (id == $articleId.val()) {
            return false;
        }
        var formData = $("#saveNewsForm").serialize();
        if ($("#hashcode").val() != hashCode(formData)) {
            var flag = confirm("是否保存当前内容？");
            if (flag == true) {
                if ($("[name='articleId']").val() == "") {
                    var code = doAddArticle();
                    if (code = -1) return false;
                } else {
                    doEditArticle();
                }
            } else {
                if ($("[name='articleId']").val() == "") {
                    $("#existArticles > div").eq(-1).remove();

                }
            }
        } else {
            if ($("[name='articleId']").val() == "") {
                $("#existArticles > div").eq(-1).remove();
            }
        }

        $("[name='articleId']").val(id);
        $.post(
                "/api/wechat/material/detail",
                {
                    accountId: ${param.accountId!0},
                    id: id
                },
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        ue.ready(function () {
                            ue.setContent(data.result.content);

                        });
                        $(".form-temp").val("");
                        $("[name='title']").val(data.result.title);
                        $("[name='author']").val(data.result.author);
                        $("[name='digest']").val(data.result.digest);
                        $("#thumbImg").prop("src", data.result.thumbUrl);
                        $("[name='sourceUrl']").val(data.result.sourceUrl);
                        $("[name='clickUrl']").val(data.result.clickUrl);
                        $("[name='articleId']").val(data.result.id);

                        var formData = $("#saveNewsForm").serialize();
                        $("#hashcode").val(hashCode(formData));
                    }
                }
        )
    }



    function onAfterPickImage(id, name, url) {
        $("#reply_image_id").val(id);
        $("#reply_image").val(name);
        ue.ready(function () {
            var imageHtml = '<p><img src=' + url + ' alt=""></p>'
            ue.execCommand('inserthtml', imageHtml);
        });
        hideImagePicker();
    }

    function onAfterPickAido(id, name, url) {
        $("#reply_voice_id").val(id);
        $("#reply_voice").val(name);
        console.log(url);
        console.log(id);
        console.log(name);
        ue.ready(function () {
            var audioHtml = '<div class="audio_msg_wrp preview_card">' +
                    '<div class="audio_msg">' +
                    '<i class="icon_audio_msg"></i>' +
                    '<div style="display: inline-block;vertical-align: middle;">' + '<p style="width: 300px;text-overflow: ellipsis"><strong class="audio_title">' + name + '</strong></p>' + '<p class="audio_desc" style="margin: 0"><audio src=' + url + ' controls="controls"></p>' + '</div>' + '</div>' + '</div>'
            ue.execCommand('inserthtml', audioHtml);
        });
        hideVoicePicker();
    }

    function onAfterPickVideo(id, name, url) {
        $("#reply_video_id").val(id);
        $("#reply_video").val(name);
        var videoHtml = '<div class="audio_msg_wrp preview_card" style="width: 200px;">' + '<div style="text-overflow: ellipsis;width: 200px;height: 30px;line-height: 30px;">' + name + '</div>' + '<video src=' + url + ' controls="controls"></video>' + '</div>'
        ue.execCommand('inserthtml', videoHtml);
        hideVideoPicker();
    }

    function choiceImage(elem) {
        showImagePicker(onAfterPickImage);
    }

    function choiceVideo(elem) {
        showVideoPicker(onAfterPickVideo);
    }

    function choiceVoice(elem) {
        showVoicePicker(onAfterPickAido);
    }

</script>
<script>
    function addArticle() {
        var temp = $("#saveNewsForm").serialize();
        if ($("#hashcode").val() != hashCode(temp)) {
            var flag = confirm("是否保存当前内容？");
            if (flag == true) {
                if ($("[name='articleId']") != "") {
                    doEditArticle();
                    $(".form-temp").val("");
                    var formData = $("#saveNewsForm").serialize();
                    $("#hashcode").val(hashCode(formData));
                    addDiv();
                } else {
                    doAddArticle();
                    $(".form-temp").val("");
                    var ue = UE.getEditor('container');
                    ue.setContent("在此处输入图文消息");
                    var formData = $("#saveNewsForm").serialize();
                    $("#hashcode").val(hashCode(formData));
                    addDiv();
                }
            }
        } else {
            $(".form-temp").val("");
            var ue = UE.getEditor('container');
            ue.setContent("在此处输入图文消息");
            var formData = $("#saveNewsForm").serialize();
            $("#hashcode").val(hashCode(formData));
            addDiv();
        }

    }

    function addDiv() {
        var str_html = "<div class=\"msgCoverImg\" ischoosed=\"false\">\n" +
                "                            <h4><a href=\"#\">标题</a></h4>\n" +
                "                            <div class=\"msgImg\" style=\"background-image: url('');\"></div>\n" +
                "                            <a href=\"#\" class=\"msgImgCover previewMsg\" onclick=\"\">\n" +
                "                                <div class=\" msgCoverContent\"><p>图文消息</p></div>\n" +
                "                            </a>\n" +
                "                            <div class=\"picControl edit_show\" style=\"display: none\">\n" +
                "                                <span onclick=\"doMove()\"><i class=\"fa pencil\"></i></span>\n" +
                "                                <span onclick=deleteArticle();><i class=\"fa trash\"></i></span>\n" +
                "                            </div>\n" +
                "                        </div>"
        $("#existArticles").append(str_html);
    }

    function doAddArticle() {
        var fromData = getFormData($("#saveNewsForm"));
        $.ajax({
            url: "/api/wechat/material/article/add",
            type: "post",
            dataType: "json",
            cache: false,
            processData: false,
            contentType: false,
            data: fromData,
            complete: function (data) {
                if (data.responseJSON.code < 0) {
                    alertShow("warning", data.responseJSON.message, 5000);
                    return -1;
                } else {
                    $("[name='materialId']").val(data.responseJSON.result.materialId);
                    var ue = UE.getEditor('container');
                    ue.setContent("在此处输入图文消息");

                    $("#existArticles").find("a").each(function () {
                        if ($(this).attr("onclick") == "") {
                            $(this).attr("onclick", "news_preview(" + data.responseJSON.result.id + ")")
                            $(this).parent("div").attr("id", data.responseJSON.result.id);
                        }
                    });
                }
            }
        })
    }

    function doEditArticle() {
        var fromData = getFormData($("#saveNewsForm"));
        $.ajax({
            url: "/api/wechat/material/article/edit",
            type: "post",
            dataType: "json",
            cache: false,
            processData: false,
            contentType: false,
            data: fromData,
            complete: function (data) {
                if (data.responseJSON.code < 0) {
                    alertShow("warning", data.responseJSON.explain, 5000);
                } else {
//                    $(".form-temp").val("");
                }
            }
        })
    }

    function deleteArticle(id) {
        $.post(
                "/api/wechat/material/article/del",
                {
                    articleId: id
                },
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000)
                    } else {
                        alert("删除成功");
                        window.location.reload();
                    }
                }
        )

    }

    function getFormData(form) {
        var formData = new FormData();
        var a = $(form).serializeArray();
        $.each(a, function () {
            formData.append(this.name, this.value)
        });
        return formData;
    }

    function doUploadNews() {
        var accountId = $("[name='accountId']").val();
        var materialId = $("[name='materialId']").val();
        $.post(
                "/api/wechat/material/news/upload",
                {
                    accountId: accountId,
                    materialId: materialId
                },
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        window.location = "/wechat/material/document";
                    }
                }
        )
    }

    function hashCode(str) {
        var h = 0;
        var len = str.length;
        var t = 2147483648;
        for (var i = 0; i < len; i++) {
            h = 31 * h + str.charCodeAt(i);
            if (h > 2147483647) h %= t;//java int溢出则取模
        }
        return h;
    }

</script>
<script>
    window.onload = function () {
        var formData = $("#saveNewsForm").serialize();
        $("#hashcode").val(hashCode(formData));

        document.documentElement.scrollTop = 0
        document.body.scrollTop = 0
        window.onscroll = function () {
            console.log('滚动啦')
            var top = document.documentElement.scrollTop || document.body.scrollTop;
            var left = document.getElementsByClassName('main_bd_left')[0]
            var right = document.getElementsByClassName('main_bd_right')[0]
            var leftDistance, rightDistance
            if (left.style.position === 'absolute') {
                leftDistance = left.getBoundingClientRect().left
                rightDistance = right.getBoundingClientRect().left
            }
            if (top >= 20) {
                left.style.position = 'fixed'
                right.style.position = 'fixed'
                left.style.left = leftDistance + 'px'
                right.style.left = rightDistance + 'px'
            }
            else {
                left.style.position = 'absolute'
                right.style.position = 'absolute'
                left.style.left = null
                right.style.left = null
            }
        }
    }
</script>
</body>
</html>
    <#include "../views/pick_image.ftl" />
    <#include "../views/pick_video.ftl" />
    <#include "../views/pick_audio.ftl" />
    <#include "./materials_action.ftl" />
</@macroCommon.html>