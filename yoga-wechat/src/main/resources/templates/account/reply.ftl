<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<@macroCommon.html>
<link href="/qq-wechat.css" rel="stylesheet">
<link href="/wechat.css" rel="stylesheet">

<body>
<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>微信公众号</a></li>
            <li>自定义回复</li>
        </ol>
    </div>
    <div class="row content-bottom">
        <div class="col-sm-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="icon icon-user"></i>自定义回复
                </div>
                <div class="panel-body">
                    <div class="tableToolContainer" style="margin-bottom:15px">
                        <form action="/wechat/reply" class="form-inline" id="filter_form">
                            <#if accounts??>
                                <div class="form-group">
                                    <label class="exampleInputAccount4" style="margin-right: 20px">微信账号</label>
                                    <select class="form-control" style="min-width: 200px" name="accountId" id="accountId" onchange="loadAccount()">
                                        <#list accounts as account>
                                            <option value="${account.id?default(0)}">${account.name?if_exists}</option>
                                        </#list>
                                    </select>
                                    <script>
                                        $("#accountId").val('${param.accountId?default(0)}');
                                        function loadAccount() {
                                            $("#filter_form").submit();
                                        }
                                    </script>
                                </div>
                            </#if>
                            <div class="form-group" style="margin-left: 15px">
                                <div class="form-group">
                                    <div class="form-group">
                                        <label class="exampleInputAccount4">名称</label>
                                        &nbsp;&nbsp;
                                        <input type="text" class="form-control" name="name" value="${param.name?if_exists}">
                                    </div>
                                </div>
                            </div>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <div class="form-group">
                                <button type="submit" class="btn btn-success"><i
                                        class="fa fa-fw fa-search"></i>搜索
                                </button>
                                &nbsp;&nbsp;&nbsp;&nbsp;
                                <div style="float: right">
                                    <a type="input" onclick="doAdd()" class="btn btn-primary">
                                        <i class="fa fa-fw fa-plus-circle"></i>添加
                                    </a>
                                </div>

                            </div>
                        </form>
                    </div>
                    <table class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th style="width:10%">名称</th>
                            <th style="width:10%">回复条件</th>
                            <th style="width:15%">条件对象</th>
                            <th style="width:15%">关键字</th>
                            <th style="80px" class="tableCenter">回复方式</th>
                            <th style="width:30%">回复内容</th>
                            <th class="tableCenter">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list replies as reply>
                            <tr>
                                <td>${reply.name?if_exists}</td>
                                <td>${reply.event.getDesc()}</td>
                                <td>
                                    <#if reply.tag?default(0) != 0 && tags[reply.tag?c]?exists>
                                        ${tags[reply.tag?c].name!}
                                        &nbsp;&nbsp;&nbsp;
                                    </#if>
                                    <#if reply.gender?default('unknown') != 'unknown'>
                                        ${reply.gender.getDesc()}
                                    </#if>
                                </td>
                                <td>${reply.keyword?if_exists}</td>
                                <td class="tableCenter">${reply.messageType.getDesc()}</td>
                                <td>
                                    <#if reply.messageType == 'text'>
                                        ${reply.text?if_exists}
                                    <#elseif reply.messageType == 'music'>
                                        ${reply.musicUrl?if_exists}
                                    <#elseif reply.messageType == 'plugin'>
                                        ${reply.pluginCode?if_exists}
                                    <#else>
                                        ${reply.mediaName?if_exists}
                                    </#if>
                                </td>
                                <td class="tableCenter">
                                    <a href="javascript:void(0)"
                                       onclick="doEdit(${reply.id?if_exists?c})"
                                       class="btn btn-sm btn-primary">
                                        <i class="fa fa-fw  fa-edit"></i>编辑</a>
                                    <a href="javascript:void(0)"
                                       onclick="doDelete(${reply.id?if_exists?c})"
                                       class="btn btn-sm btn-danger">
                                        <i class="fa fa-fw  fa-remove"></i>删除</a>
                                </td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>
                <div class="box-footer" style="text-align:center">
                    <@html.paging page=page param=param action="/wechat/reply"/>
                </div>
            </div>
        </div>
    </div>
</div>
<footer class="main-footer">
    <@common.footer />
</footer>
</body>

    <#include "../views/pick_image.ftl" />
    <#include "../views/pick_video.ftl" />
    <#include "../views/pick_audio.ftl" />
<script>
    function onAfterPickImage(id, name) {
        $("#reply_image_id").val(id);
        $("#reply_image").val(name);
        hideImagePicker();
    }
    function onAfterPickAido(id,name) {
        $("#reply_voice_id").val(id);
        $("#reply_voice").val(name);
        hideVoicePicker();
    }
    function onAfterPickVideo(id,name) {
        $("#reply_video_id").val(id);
        $("#reply_video").val(name);
        hideVideoPicker();
    }
    function audiopicker() {
        $("#voice_pick_modal").modal('show');
        $("#voice_pick_modal").css('z-index','9999');
    }
    function videopicker() {
        $("#video_pick_modal").modal('show');
        $("#video_pick_modal").css('z-index','9999');
    }
</script>

    <@operate.modal "自定义回复" "reply_modal" "addReply">
    <div class="modalForm" style="margin-top: 15px">
        <div class="form-group">
            <label class="col-sm-3  control-label">规则名称</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="reply_name">
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">回复条件</label>
            <div class="col-sm-8">
                <select id="replay_event" class="form-control" onchange="eventChanged()">
                    <#list actions as action>
                        <option value="${action.getCode()}">${action.getName()}</option>
                    </#list>
                </select>
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="control-label col-sm-3">条件对象</label>
            <div class="col-sm-2">
                <select id="reply_use_tag" class="form-control" onchange="tagChange()">
                    <option value="0"> 全部用户</option>
                    <option value="1">按标签</option>
                </select>
            </div>
            <div id="label_div">
                <label class="col-sm-1  control-label">标签</label>
                <div class="col-sm-2">
                    <select id="reply_tag" class="form-control">
                        <#list tags?keys as key>
                            <option value="${key}">${tags[key].name}</option>
                        </#list>
                    </select>
                </div>
            </div>
            <label class="col-sm-1  control-label">性别</label>
            <div class="col-sm-2">
                <select id="reply_gender" class="form-control">
                    <option value="unknown">全部</option>
                    <option value="male">男</option>
                    <option value="female">女</option>
                </select>
            </div>
        </div>
    </div>
    <div class="modalForm" id="reply_keyword_row">
        <div class="form-group">
            <label class="col-sm-3  control-label">关键字</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="reply_key" onkeypress="addKeyword(event)"
                       placeholder="输入关键字按回车增加"></input>
                <div class="col-sm-12" id="keyButton_div" style="margin-top: 10px">
                </div>
            </div>
        </div>
    </div>
    <div class="modalForm">
        <div class="form-group">
            <label class="col-sm-3  control-label">回复类型</label>
            <div class="col-sm-8">
                <select class="form-control" id="reply_action" onchange="actionChanged()">
                    <option value="text">文字消息</option>
                    <option value="image">图片消息</option>
                    <option value="voice">音频消息</option>
                    <option value="video">视频消息</option>
                    <option value="music">音乐消息</option>
                    <option value="news">图文消息</option>
                    <option value="plugin">跳转任务</option>
                </select>
            </div>
        </div>
    </div>
    <div class="modalForm param_row" id="reply_text_row">
        <div class="form-group">
            <label class="col-sm-3  control-label">回复内容</label>
            <div class="col-sm-8">
                <textarea name="" class="form-control" id="reply_text"></textarea>
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm param_row" id="reply_video_row">
        <div class="form-group">
            <label class="col-sm-3  control-label">视频素材</label>
            <div class="col-sm-8">
                <input type="hidden" id="reply_video_id">
                <input type="text" readonly class="form-control" id="reply_video" style="background-color: #eee"
                       placeholder="点击选择视频素材" onclick="choiceVideo(this)">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm param_row" id="reply_image_row">
        <div class="form-group">
            <label class="col-sm-3  control-label">图片素材</label>
            <div class="col-sm-8">
                <input type="hidden" id="reply_image_id">
                <input type="text" readonly class="form-control" id="reply_image" style="background-color: #eee"
                       placeholder="点击选择图片素材" onclick="choiceImage(this)">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm param_row" id="reply_news_row">
        <div class="form-group">
            <label class="col-sm-3  control-label">图文素材</label>
            <div class="col-sm-8">
                <input type="hidden" id="reply_news_id">
                <input type="text" readonly class="form-control" id="reply_news" style="background-color: #eee"
                       placeholder="点击选择图文素材" onclick="choiceNews(this)">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm param_row" id="reply_voice_row">
        <div class="form-group">
            <label class="col-sm-3  control-label">语音素材</label>
            <div class="col-sm-8">
                <input type="hidden" id="reply_voice_id">
                <input type="text" readonly class="form-control" id="reply_voice" style="background-color: #eee"
                       placeholder="点击选择音频素材" onclick="choiceVoice(this)">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm param_row" id="reply_title_row_video_music">
        <div class="form-group">
            <label class="col-sm-3  control-label">素材标题</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="reply_title">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm param_row" id="reply_intro_row_video_music">
        <div class="form-group">
            <label class="col-sm-3  control-label">素材描述</label>
            <div class="col-sm-8">
                <textarea name="" class="form-control" id="reply_intro"></textarea>
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm param_row" id="reply_music_row">
        <div class="form-group">
            <label class="col-sm-3  control-label">音乐URL</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="reply_music_url">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm param_row" id="reply_hq_music_row">
        <div class="form-group">
            <label class="col-sm-3  control-label">高质量音乐URL</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="reply_hq_music_url">
                <span class="help-block"></span>
            </div>
        </div>
    </div>
    <div class="modalForm param_row" id="reply_plugin_row">
        <div class="form-group">
            <label class="col-sm-3  control-label">执行任务</label>
            <div class="col-sm-8">
                <ul id="plugin_list">
                </ul>
            </div>
        </div>
    </div>
    <input id="use_plugin_code" type="hidden" value="">
    <input id="use_plugin_config" type="hidden" value="">
    <input id="edit_reply_id" type="hidden">
    </@operate.modal>

<div class="modal fade" id="PluginConfigModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog" style="width:70%;">
        <div class="form-horizontal" id="lawsAddForm" name="lawsAddForm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close"
                            data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="myModalLabel">
                        任务配置
                    </h4>
                </div>
                <div class="modal-body"  style="height: 500px !important;">
                    <iframe id="plugin_config" style="width: 100%;height: 100%" scrolling=auto scrolling=auto/>
                    </iframe>
                </div>
                <div class="modal-footer">
                    <a href="#" class="btn btn-danger" data-dismiss="modal">
                        <i class="fa fa-fw fa-remove"></i>取消
                    </a>
                    <a type="summit" class="btn btn-info" onclick="saveConfig()">
                        <i class="fa fa-fw  fa-save"></i>保存
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    $("#label_div").hide();
    function tagChange() {
        if ($("#reply_use_tag").val() == 0) {
            $("#label_div").hide();
        } else {
            $("#label_div").show();
        }
    }
    function choiceVideo(elem) {
        showVideoPicker(onAfterPickVideo);
    }
    function choiceImage(elem) {
        showImagePicker(onAfterPickImage);
    }
    function choiceVoice(elem) {
        showVoicePicker(onAfterPickAido);
    }

    function eventChanged() {
        var event = $("#replay_event").val();
        if (event == "keyword") $("#reply_keyword_row").get(0).style.display = 'block';
        else $("#reply_keyword_row").get(0).style.display = 'none';
        var cid = $("#reply_action").val();
        if (cid == "plugin") actionChanged();
    }
    function actionChanged() {
        var cid = $("#reply_action").val();
        $(".param_row").each(function () {
            if (this.id.indexOf(cid) > 0) this.style.display = 'block';
            else this.style.display = 'none';
        });
        if (cid == "plugin") {
            var event = $("#replay_event").val();
            if (event == 'keyword') event = 'text';
            if (event == 'common') event = '';
            $("#plugin_list").empty();
            $.get("/api/wechat/actions/items?event=" + event,
                    function (data) {
                        if (data.code < 0) {
                            $("#reply_plugin_row").css("visibility","hidden");
                            alertShow("danger", data.message, 3000);
                        } else {
                            $("#reply_plugin_row").css("visibility","visible");
                            $("#plugin_list").empty();
                            if (data.result.length > 0) {
                                for (var index = 0; index < data.result.length; index++) {
                                    var item = data.result[index];
                                    var html = "<li class='plugin-item' data-plugin-code=\"" + item.code + "\" onclick=\"usePlugin('" + item.code + "', " + item.needConfig + ", '" + item.clickedUrl + "')\" ";
                                    html += ">" +
                                            "<i class=\"icon icon-ok-sign\" style=\"color: darkgrey;\"></i>" +
                                            "<span class=\"task_content_name\">" + item.name + "</span>" +
                                            "<span class=\"task_content_name\" name='config_value'></span>";
                                    if (item.needConfig) html += "<span class=\"label label-info\">可配置</span>";
                                    html += "</li>";
                                    $("#plugin_list").append(html);
                                }
                            }
                        }
                    }
            );
        }
    }
    $(function () {
        eventChanged();
        actionChanged();
    });
    function usePlugin(code, needConfig, url) {
        if (needConfig) {
            $("#plugin_config").prop('src', "/wechat/actions/config?code=" + code);
            $("#PluginConfigModal").modal("show");
        } else if (!url) {
            var config = {};
            json["type"] = "view";
            json["url"] = url;
            $("#use_plugin_config").val(config);
            $("li[data-plugin-code='" + code + "'][name='config_value']").val(url);
        }
        $("#use_plugin_code").val(code);
    }
    function saveConfig() {
        var code = $("#use_plugin_code").val();
        var config = $('#plugin_config')[0].contentWindow.getConfig();
        if (config == null) return;
        $("#use_plugin_config").val(config);
        $("#PluginConfigModal").modal("hide");
        $("li[data-plugin-code='" + code + "'][name='config_value']").val(config);
    }
    function addKeyword(event) {
        event = event || window.event;
        if (event.keyCode == 13) {
            event.returnValue = false;
            var keyText = $("#reply_key").val();
            var keyInput = $("#key_input").val();
            $("#keyButton_div").append("<button name='reply_keyword' class='btn btn-sm btn-primary' style='margin-right:5px' id='keyButton' onclick='removeKeyword(this);' value='" + keyText + "'>" + keyText + "<i class='icon icon-times'></i></button>")
            $("#keyButton_div").append("<input type='hidden'  id='keyInput'  value='" + keyText + "'>");
            $('#reply_key').val('');
            return false;
        }
    }
    function removeKeyword(th) {
        $(th).remove();
    }
    function addReply() {
        var name = $("#reply_name").val();
        var event = $("#replay_event").val();
        var useTag = $("#reply_use_tag").val();
        var tag = useTag != 0 ? $("#reply_tag").val() : "";
        var gender = $("#reply_gender").val();
        var title = $("#reply_title").val();
        var intro = $("#reply_intro").val();
        var musicUrl = $("#reply_music_url").val();
        var hqMusicUrl = $("#reply_hq_music_url").val();
        var action = $("#reply_action").val();
        var text = $("#reply_text").val();
        var pluginCode = $("#use_plugin_code").val();
        var pluginConfig = $("#use_plugin_config").val();
        var keywords = "";
        $("button[name='reply_keyword']").each(function () {
            keywords += "," + $(this).val();
        });
        if (keywords.length > 0) keywords = keywords.substr(1);
        if (event == "keyword" && keywords.length < 1) {
            alertShow("warning", "请输入关键字！", 1000);
            return;
        }
        var mediaId = "";
        var mediaName = "";
        if (action == "video") {
            mediaId = $("#reply_video_id").val();
            mediaName = $("#reply_video").val();
        } else if (action == "voice") {
            mediaId = $("#reply_voice_id").val();
            mediaName = $("#reply_voice").val();
        } else if (action == "image") {
            mediaId = $("#reply_image_id").val();
            mediaName = $("#reply_image").val();
        }
        if ((action == "video" || action == "voice" || action == "image") && mediaId.length < 1) {
            alertShow("warning", "请选择素材！", 1000);
            return;
        }
        if (action == "music" && musicUrl.length < 1) {
            alertShow("warning", "请填写音乐URL！", 1000);
            return;
        }
        if (action == "text" && text.length < 1) {
            alertShow("warning", "请填写回复内容！", 1000);
            return;
        }
        if (action == 'plugin' && pluginCode.length < 1) {
            alertShow("warning", "请选择需要执行的任务！", 1000);
            return;
        }
        var id = $("#edit_reply_id").val();
        $.post(
                id == 0 ? "/api/wechat/reply/add" : "/api/wechat/reply/update",
                {
                    id: id,
                    accountId: ${param.accountId?if_exists?c},
                    event: event,
                    tag: tag,
                    gender: gender,
                    name: name,
                    keyword: keywords,
                    type: action,
                    text: text,
                    mediaId: mediaId,
                    mediaName: mediaName,
                    title: title,
                    description: intro,
                    musicUrl: musicUrl,
                    hqMusicUrl: hqMusicUrl,
                    pluginCode: pluginCode,
                    pluginConfig: pluginConfig
                },
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        window.location.reload();
                    }
                }
        );
    }
    function doDelete(id) {
        warningModal("确定要删除吗?", function () {
            $.post(
                    "/api/wechat/reply/delete",
                    {
                        accountId: ${param.accountId?if_exists?c},
                        id: id
                    },
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);
                        } else {
                            window.location.reload();
                        }
                    }
            );
        });
    }
    function doAdd() {
        $("#reply_modal").modal("show");
        $("#keyButton_div").empty();
        $("#edit_reply_id").val(0);
    }
    function doEdit(id) {
        $("#keyButton_div").empty();
        $.post(
                "/api/wechat/reply/get",
                {
                    accountId: ${param.accountId?if_exists?c},
                    id: id
                },
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        var action = data.result.type;
                        $("#reply_modal").modal("show");
                        $("#edit_reply_id").val(id);
                        $("#reply_name").val(data.result.name);
                        $("#replay_event").val(data.result.event);
                        $("#reply_use_tag").val(data.result.tag == 0 ? 0 : 1);
                        $("#reply_tag").val(data.result.tag);
                        $("#reply_gender").val(data.result.gender);
                        if ("title" in data.result) $("#reply_title").val(data.result.title);
                        if ("description" in data.result) $("#reply_intro").val(data.result.description);
                        if ("musicUrl" in data.result) $("#reply_music_url").val(data.result.musicUrl);
                        if ("hqMusicUrl" in data.result) $("#reply_hq_music_url").val(data.result.hqMusicUrl);
                        if ("text" in data.result) $("#reply_text").val(data.result.text);
                        $("#reply_action").val(action);
                        if ("keyword" in data.result) {
                            for (var i = 0; i < data.result.keyword.length; i++) {
                                var keyword = data.result.keyword[i];
                                $("#keyButton_div").append("<button name='reply_keyword' class='btn btn-sm btn-primary' style='margin-right:5px' id='keyButton' onclick='removeKeyword(this);' value='" + keyword + "'>" + keyword + "<i class='icon icon-times'></i></button>")
                                $("#keyButton_div").append("<input type='hidden'  id='keyInput'  value='" + keyword + "'>");
                            }
                        }
                        if ("mediaId" in data.result) {
                            var mediaName = "";
                            if ("mediaName" in data.result) mediaName = data.result.mediaName;
                            if (action == "video") {
                                $("#reply_video").val(mediaName);
                                $("#reply_video_id").val(data.result.mediaId);
                            } else if (action == "voice") {
                                $("#reply_voice").val(mediaName);
                                $("#reply_voice_id").val(data.result.mediaId);
                            } else if (action == "image") {
                                $("#reply_image").val(mediaName);
                                $("#reply_image_id").val(data.result.mediaId);
                            }
                        }
                        eventChanged();
                        tagChange();
                        actionChanged();
                    }
                }
        );
    }
</script>


</@macroCommon.html>