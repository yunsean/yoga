<div id="menu_edit_panel" style="display: none;">
    <form class="form-horizontal" id="menu_entity_form">
        <div class="modal-content">
            <div class="modal-header">

                <h4 class="modal-title" id="menu_entity_modal_header">
                    菜单编辑
                </h4>
            </div>
            <div class="modal-body">
                <div class="modalForm" style="margin-top: 10px">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">菜单名称</label>
                        <div class="col-sm-8" style="margin-left: 10px; margin-right: 10px">
                            <input type="text" class="form-control" id="new_name" placeholder="字数不超过4个汉字或8个字母">
                        </div>
                    </div>
                </div>
                <div class="modalForm">
                    <div class="form-group">
                        <label class="col-sm-3  control-label">菜单类型</label>
                        <div class="form-group col-sm-8" style="margin-left: 10px; margin-right: 10px">
                            <div id="menu_action_selector" style="margin-left: 10px">
                                <label class="radio-inline">
                                    <input type="radio" name="new_menu_action" checked onclick="actionChanged();"
                                           value="new_message"> 发送消息
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" name="new_menu_action" onclick="actionChanged();"
                                           value="new_clickview"> 跳转网页
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" name="new_menu_action" onclick="actionChanged();"
                                           value="new_function"> 功能键
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" name="new_menu_action" onclick="actionChanged();"
                                           value="new_miniprogram"> 跳转小程序
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" name="new_menu_action" onclick="actionChanged();"
                                           value="new_popup"> 弹出菜单
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modalForm" id="new_panel_collect">
                    <div class="form-group">
                        <label class="col-sm-3  control-label">菜单内容</label>
                        <div class="form-group col-sm-8">
                            <div id="new_message_panel">
                                <div class="form-group example" style="margin-left: 20px; margin-right: 20px">
                                    <ul class="nav nav-secondary" id="message_type_selector">
                                        <li id="message_type_tab_text" class="active">
                                            <a onclick="messageChanged('text'); ">
                                                <i class="icon icon-edit"></i>文字
                                            </a>
                                        </li>
                                        <li id="message_type_tab_image">
                                            <a onclick="messageChanged('image'); ">
                                                <i class="icon icon-picture"></i>图片
                                            </a>
                                        </li>
                                        <li id="message_type_tab_voice">
                                            <a onclick="messageChanged('voice'); ">
                                                <i class="icon icon-music"></i>音频
                                            </a>
                                        </li>
                                        <li id="message_type_tab_video">
                                            <a onclick="messageChanged('video'); ">
                                                <i class="icon icon-play"></i>视频
                                            </a>
                                        </li>
                                        <li id="message_type_tab_news">
                                            <a onclick="messageChanged('news'); ">
                                                <i class="icon icon-bar-chart-alt"></i>图文消息
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                                <div id="message_type_container">
                                    <div id="message_type_container_text" class="form-group example"
                                         style="margin-left: 20px; margin-right: 20px;margin-top: -15px">
                                        <textarea id="text_first" rows="10" style="width: 100%"
                                                  placeholder="请输入回复内容"></textarea>
                                    </div>
                                    <div id="message_type_container_image" class="form-group example"
                                         style="margin-left: 20px; margin-right: 20px;margin-top: -15px">
                                        <input type="hidden" id="reply_image_id">
                                        <input type="text" readonly class="form-control" id="reply_image"
                                               style="margin-top: 15px; margin-bottom: 15px; background-color: #eee"
                                               placeholder="点击选择图片素材" onclick="choiceImage(this)">
                                    </div>
                                    <div id="message_type_container_voice" class="form-group example"
                                         style="margin-left: 20px; margin-right: 20px;margin-top: -15px">
                                        <input type="hidden" id="reply_voice_id">
                                        <input type="text" readonly class="form-control" id="reply_voice"
                                               style="margin-top: 15px; margin-bottom: 15px; background-color: #eee"
                                               placeholder="点击选择音频素材" onclick="choiceVoice(this)">
                                    </div>
                                    <div id="message_type_container_video" class="form-group example"
                                         style="margin-left: 20px; margin-right: 20px;margin-top: -15px">
                                        <input type="hidden" id="reply_video_id">
                                        <input type="text" readonly class="form-control" id="reply_video"
                                               style="margin-top: 15px; margin-bottom: 15px; background-color: #eee"
                                               placeholder="点击选择视频素材" onclick="choiceVideo(this)">
                                    </div>
                                </div>
                            </div>
                            <div id="new_clickview_panel">
                                <div class="form-group example" style="margin-left: 20px; margin-right: 20px">
                                    <span>粉丝点击该菜单会跳转到以下链接</span>
                                    <div class="form-group" style="margin-top: 15px">
                                        <label class="col-sm-3 control-label">页面地址</label>
                                        <div class="col-sm-8">
                                            <input type="text" class="form-control" id="new_viewclick_url"
                                                   placeholder="请填写正确的URL格式">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div id="new_function_panel" class="modalForm">
                                <div class="form-group example" style="margin-left: 20px; margin-right: 20px">
                                    <span>粉丝点击该菜单会跳转到以下功能</span>
                                    <div class="form-group" style="margin-top: 15px">
                                        <div class="radio col-sm-12 col-sm-offset-1">
                                            <label>
                                                <input type="radio" name="key"> 打开手机拍照功能
                                            </label>
                                        </div>
                                        <div class="radio col-sm-12 col-sm-offset-1">
                                            <label>
                                                <input type="radio" name="key"> 打开手机拍照或相册
                                            </label>
                                        </div>
                                        <div class="radio col-sm-12 col-sm-offset-1">
                                            <label>
                                                <input type="radio" name="key"> 打开微信相册
                                            </label>
                                        </div>
                                        <div class="radio col-sm-12 col-sm-offset-1">
                                            <label>
                                                <input type="radio" name="key"> 使用地理位置
                                            </label>
                                        </div>
                                        <div class="radio col-sm-12 col-sm-offset-1">
                                            <label>
                                                <input type="radio" name="key"> 扫一扫
                                            </label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div id="new_miniprogram_panel" class="modalForm">
                                <div class="form-group example" style="margin-left: 20px; margin-right: 20px">
                                    <span>订阅者点击该子菜单会跳到以下小程序</span>
                                    <div class="form-group" style="margin-top: 15px">
                                        <label class="col-sm-3 control-label">小程序APPID</label>
                                        <div class="col-sm-8">
                                            <input type="text" class="form-control" id=""
                                                   placeholder="填写的APPID必须为已经绑定到该公众号的小程序">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label">小程序路径</label>
                                        <div class="col-sm-8">
                                            <input type="text" class="form-control" id="">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label">备用网页</label>
                                        <div class="col-sm-8">
                                            <input type="text" class="form-control" id=""
                                                   placeholder="旧版微信客户端无法支持小程序，用户点击菜单将会打开备用网页">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <input type="hidden" id="new_parent_id">
            <input type="hidden" id="new_id">
            <div class="modal-footer">
                <a href="javascript:void(0)" onclick="doReset();" class="btn btn-danger">
                    <i class="fa fa-fw fa-remove"></i>重置
                </a>
                <a href="javascript:void(0)" onclick="doSave();" class="btn btn-info">
                    <i class="fa fa-fw  fa-save"></i>保存
                </a>
            </div>
        </div>
    </form>
</div>
</body>
<#include "../views/pick_image.ftl" />

<script>
    function onAfterPickImage(id, name) {
        $("#reply_image_id").val(id);
        $("#reply_image").val(name);
        hideImagePicker();
    }
    function onAfterPickAido(id, name) {
        $("#reply_voice_id").val(id);
        $("#reply_voice").val(name);
        hideVoicePicker();
    }
    function onAfterPickVideo(id, name) {
        $("#reply_video_id").val(id);
        $("#reply_video").val(name);
        hideVideoPicker();
    }
    $("#message_type_container_image").hide();
    $("#message_type_container_voice").hide();
    $("#message_type_container_video").hide();
    function choiceImage(elem) {
        showImagePicker(onAfterPickImage);
    }
    function choiceVoice(elem) {
        showVoicePicker(onAfterPickAido);
    }
    function choiceVideo(elem) {
        showVideoPicker(onAfterPickVideo);
    }
    function messageChanged(type) {
        var tab = "message_type_tab_" + type;
        $("#message_type_selector").children().each(function () {
            if (this.id == tab) $(this).addClass("active");
            else $(this).removeClass("active");
        });
        var content = "message_type_container_" + type;
        $("#message_type_container").children().each(function () {
            if (this.id == content) $(this).show();
            else $(this).hide();
        });
    }
    function actionChanged() {
        var item = $("input[name='new_menu_action']:checked").val();
        if (item == "new_message") {
            $("#new_panel_collect").show();
            $("#new_message_panel").show();
        } else {
            $("#new_message_panel").hide();
            $("#new_clickview_panel").hide();
        }
        if (item == "new_clickview") {
            $("#new_panel_collect").show();
            $("#new_clickview_panel").show();
        } else {
            $("#new_clickview_panel").hide();
        }
        if (item == "new_function") {
            $("#new_panel_collect").show();
            $("#new_function_panel").show();
        } else {
            $("#new_function_panel").hide();
        }
        if (item == "new_keyword") {
            $("#new_panel_collect").show();
            $("#new_keyword_panel").show();
        } else {
            $("#new_keyword_panel").hide();
        }
        if (item == "new_miniprogram") {
            $("#new_panel_collect").show();
            $("#new_miniprogram_panel").show();
        } else {
            $("#new_miniprogram_panel").hide();
        }
        if (item == "new_popup") {
            $("#new_panel_collect").hide();
        }
    }
    $(function () {
        actionChanged();
    });
    function doAdd(root, parentId) {
        if (root) $("#menu_type_pop").show();
        else $("#menu_type_pop").hide();
        $("#menu_entity_form")[0].reset();
        $("#new_parent_id").val(parentId);
        $("#new_id").val('0');
        $("#menu_entity_modal").modal("show");
    }
    function doEdit(root, id) {
        if (root) $("#menu_type_pop").show();
        else $("#menu_type_pop").hide();
        $.get(
                "/api/wechat/account/menu/entity/get?id=" + id,
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#new_id").val(id);
                        $("#new_name").val(data.result.name);
                        $("#menu_entity_modal").modal("show");
                    }
                }
        );
    }
    function addEntity() {
        var item = $("input[name='new_menu_action']:checked").val();
        var name = $("#new_name").val();
        var parentId = $("#new_parent_id").val();
        var id = $("#new_id").val();
        if (item == "new_message") {
        } else if (item == "new_clickview") {
            var url = $("#new_viewclick_url").val()
            $.post(
                    "/api/wechat/account/menu/entity/add",
                    {
                    menuId: <#if menu??>${menu.id?default(0)}<#else>0</#if>,
                        type: "click",
                        key: url,
                        parentId: parentId,
                        name: name
                    },
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);
                        } else {
                            window.location.reload();
                        }
                    },
                    "json"
            );
        } else if (item == "new_function") {

        } else if (item == "new_keyword") {

        } else if (item == "new_miniprogram") {

        } else if (item == "new_popup") {
            $.post(
                    "/api/wechat/account/menu/entity/add",
                    {
                    menuId: <#if menu??>${menu.id?default(0)}<#else>0</#if>,
                        type: "none",
                        name: name
                    },
                    function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);
                        } else {
                            window.location.reload();
                        }
                    },
                    "json"
            );
        }
    }
    function doDelete(id) {
        warningModal("确认删除该菜单项?", function () {
            $.get(
                    "/api/wechat/account/menu/entity/delete?id=" + id,
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
</script>