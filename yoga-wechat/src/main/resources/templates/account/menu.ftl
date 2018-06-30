<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<@macroCommon.html>
<script type="text/javascript" src="/ueditor/ueditor.streamline.js"></script>
<!-- 编辑器源码文件 -->
<script type="text/javascript" src="/ueditor/ueditor.all.js"></script>
<script type="text/javascript" src="/ueditor/lang/zh-cn/zh-cn.js"></script>
<link href="<@macroCommon.resource/>/uploader/zui.uploader.css" rel="stylesheet">
<script src="<@macroCommon.resource/>/uploader/zui.js"></script>
<script src="<@macroCommon.resource/>/uploader/zui.uploader.min.js"></script>
<link href="<@macroCommon.resource/>/css/group.css" rel="stylesheet">
<link rel="stylesheet" href="/css/menu-base.css">
<link rel="stylesheet" href="/css/menu-qq-wechat.css">
<link rel="stylesheet" href="/css/menu-wechat.css">
<script src="/js/menu.js"></script>
<style>
    .example {
        position: relative;
        padding: 5px;
        border: 1px solid #ddd;
        outline: 0;
        transition: all .3s;
    }

    .selected {
        background-color: #ddf3f5;
    }
</style>
<body>
<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>微信公众号</a></li>
            <li>菜单管理</li>
        </ol>
    </div>
    <div class="row content-bottom">
        <div class="col-sm-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="icon icon-user"></i>
                    <#if !menu?exists>
                        菜单管理
                    <#elseif menu.isDefault()?if_exists>
                        默认菜单
                    <#else>
                        ${menu.name?if_exists}
                    </#if>
                </div>
                <div class="panel-body">
                    <#if (param.isdefault)??>
                        <div class="tableToolContainer" style="margin-bottom:15px">
                            <form class="form-inline" id="filter_form">
                                <input type="hidden" name="isdefault" value="${(param.isdefault)?c!1}">
                                <input type="hidden" name="menuId" value="${(param.menuId)!0}">
                                <div class="form-group">
                                    <#if (param.isdefault!0) == 1>
                                        <label class="" style="margin-right: 20px">微信账号</label>
                                        <select class="form-control" style="min-width: 200px" id="accountId" name="accountId" onchange="transParam()">
                                            <#list accounts as account>
                                                <option value="${account.id?default(0)}">${account.name?if_exists}</option>
                                            </#list>
                                        </select>
                                    </#if>
                                    <script>
                                        $("#accountId").val('${param.accountId?default(0)}');
                                        var hhh =  "/wechat/menu/param?isdefault=1&menuId=0&accountId="
                                        var urlHref = "/wechat/menu/param?isdefault=1&menuId=0&accountId=" + $("#accountId").val()
                                    </script>
                                </div>
                            </form>
                        </div>
                    </#if>
                    <div class="col-sm-12">
                        <div class="hello">
                            <div class="container-fluid">
                                <div class="row">
                                    <div class="col-sm-12 col-lg-12 col-md-12">
                                        <div class="menu_setting_area js_editBox">
                                            <div class="row">
                                                <div class="menu_preview_area">
                                                    <div class="mobile_menu_preview">
                                                        <div class="mobile_hd tc">公众号</div>
                                                        <div class="mobile_bd">
                                                            <ul class="pre_menu_list grid_line ui-sortable ui-sortable-disabled" data-selected="main" id="menuList">
                                                            </ul>
                                                        </div>
                                                    </div>
                                                    <div class="sort_btn_wrp">
                                                        <a id="orderBt" class="btn btn-default " href="javascript:void(0);" onclick="menuSort(this)">菜单排序</a>
                                                        <a id="finishBt" href="javascript:void(0);" class="dn btn btn-success" onclick="complete(this)" style="display: none">排序完成</a>
                                                    </div>
                                                </div>

                                                <div class="col-xs-12 col-sm-6 col-lg-9 col-md-7">
                                                    <div class="panel panel-default" style="height: 576px;background-color: #f5f5f5;position: relative">
                                                        <div class="alert alert-success" id="click-left" role="alert" style="margin-top: 20%;display: block;text-align: center;background-color: #dff0d8 !important;color: #3c763d !important;" >点击左侧菜单进行编辑操作</div>
                                                        <div class="alert alert-success" id="drag-left" role="alert" style="margin-top: 20%;display: none;text-align: center;background-color: #dff0d8 !important;color: #3c763d !important;">拖拽左侧菜单进行排序操作</div>
                                                        <!--<div id="js_none" class="menu_initial_tips tips_global" ></div>-->
                                                        <div class="panel-heading menu-right" style="display: none">
                                                            <div class="row">
                                                                <div class="col-sm-6 col-lg-10 col-md-8"></div>
                                                                <div class="col-sm-6 col-lg-2 col-md-4">
                                                                    <a href="javascript:void(0);" id="jsDelBt" style="font-size: 16px" onclick="deleteMenu()">删除菜单</a>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="panel-body menu-right"  style="background-color: #f5f5f5;display: none">
                                                            <div id="js_innerNone" style="display: none;background-color: #faebcc !important;color: #8a6d3b !important;" class="alert alert-warning" role="alert">
                                                                已添加子菜单，仅可设置菜单名称。
                                                            </div>
                                                            <div class="frm_control_group js_setNameBox">
                                                                <div style="text-align: left">
                                                                    <label class="menu_content_name">菜单名称</label>
                                                                    <input type="text" class="menu_content_input" id="js_menu_name" placeholder="菜单名称不能为空" onblur="inputBlur(this)" oninput="modifyName(this)">
                                                                </div>
                                                                <div style="text-align: left;margin-top: 20px" id="menu_content">
                                                                    <!--<span class="input-group-addon" id="basic-addon2">@菜单内容</span>-->
                                                                    <p style="display: inline-block" class="menu_content_name">菜单内容</p>
                                                                    <span  data-name="sendMsg">
                                                <input type="radio" name="sendMsg" class="frm_radio" >
                                                <label class="menu_content_label">发送消息</label>
                                            </span>
                                                                    <span  data-name="url">
                                                <input type="radio" name="url" class="frm_radio" >
                                                <label class="menu_content_label">跳转网页</label>
                                            </span>
                                                                    <span  data-name="weapp">
                                                <input type="radio" name="weapp" class="frm_radio" >
                                                <label class="menu_content_label">跳转小程序</label>
                                             </span>
                                                                    <span  data-name="task">
                                                <input type="radio" name="task" class="frm_radio" >
                                                <label class="menu_content_label">跳转任务</label>
                                            </span>
                                                                </div>
                                                            </div>
                                                            <div class="menu_content_container" id="menu_content_container" >
                                                                <div class="menu_content send jsMain" id="edit" >
                                                                    <div class="msg_sender" id="editDiv" :>
                                                                        <div class="msg_tab">
                                                                            <div class="tab_navs_panel">
                                                                                <div class="tab_navs_wrp">
                                                                                    <ul class="tab_navs js_tab_navs"  style="margin-bottom: 0px">

                                                                                        <li class="tab_nav tab_appmsg width5 selected" data-type="10" data-tab="js_appmsgArea" onclick="clickSubmenu(this,'news')" data-tooltip="图文消息">
                                                                                            <a href="javascript:void(0);"
                                                                                               onclick="return false;">&nbsp;
                                                                                                <i class="icon_msg_sender"></i>
                                                                                                <span class="msg_tab_title">图文消息</span>
                                                                                            </a>
                                                                                        </li>

                                                                                        <li class="tab_nav tab_text width5" style="border-left: none" data-type="1" data-tab="js_textArea"  onclick="clickSubmenu(this,'click')" data-tooltip="文字">
                                                                                            <a href="javascript:void(0);"
                                                                                               onclick="return false;">&nbsp;
                                                                                                <i class="icon_msg_sender"></i>
                                                                                                <span class="msg_tab_title">文字</span>
                                                                                            </a>
                                                                                        </li>

                                                                                        <li class="tab_nav tab_img width5" style="border-left: none"  data-type="2" data-tab="js_imgArea" onclick="clickSubmenu(this,'image')" data-tooltip="图片">
                                                                                            <a href="javascript:void(0);" onclick="return false;">&nbsp;
                                                                                                <i class="icon_msg_sender"></i>
                                                                                                <span class="msg_tab_title">图片</span>
                                                                                            </a>
                                                                                        </li>

                                                                                        <li class="tab_nav tab_audio width5" style="border-left: none"  data-type="3" data-tab="js_audioArea"  onclick="clickSubmenu(this,'voice')" data-tooltip="语音">
                                                                                            <a href="javascript:void(0);"
                                                                                               onclick="return false;">&nbsp;
                                                                                                <i class="icon_msg_sender"></i>
                                                                                                <span class="msg_tab_title">语音</span>
                                                                                            </a>
                                                                                        </li>

                                                                                        <li class="tab_nav tab_video width5 no_extra" style="border-left: none" data-type="15" data-tab="js_videoArea" onclick="clickSubmenu(this,'video')" data-tooltip="视频">
                                                                                            <a href="javascript:void(0);"
                                                                                               onclick="return false;">&nbsp;
                                                                                                <i class="icon_msg_sender"></i>
                                                                                                <span class="msg_tab_title">视频</span>
                                                                                            </a>
                                                                                        </li>

                                                                                    </ul>
                                                                                </div>
                                                                            </div>
                                                                            <div class="tab_panel" style="height: 310px;overflow-y: auto;border-top:1px solid rgb(231,231,235)">

                                                                                <div class="tab_content" id="click-news" >
                                                                                    <div class="js_appmsgArea inner js_clickChoice" data-type="news">
                                                                                        <!--type 10图文 2图片  3语音 15视频 11商品消息-->
                                                                                        <div class="tab_cont_cover jsMsgSendTab" data-index="0">
                                                                                            <div class="media_cover" >
                                                                        <span class="create_access" @click="clickNews">
                                                                        <a class="add_gray_wrp jsMsgSenderPopBt" href="javascript:;" data-type="10" data-index="0">
                                                                        <i class="icon36_common add_gray"></i>
                                                                        <strong>点击从素材库中选择</strong>
                                                                        </a>
                                                                        </span>
                                                                                            </div>
                                                                                            <div class="msgItemA" style="display: none">

                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>

                                                                                <div class="tab_content" id="click-words" style="display: none">
                                                                                    <div class="js_textArea inner no_extra">
                                                                                        <div class="emotion_editor">
                                                                                            <textarea name="" cols="60" rows="10"  placeholder="请输入要发送的文字" onblur="saveWords(this)"></textarea>
                                                                                            <!--表情-->
                                                                                        </div>
                                                                                    </div>
                                                                                </div>

                                                                                <div class="tab_content" id="click-image" style="display: none">
                                                                                    <div class="js_imgArea inner js_clickChoice" data-type="image">
                                                                                        <div class="tab_cont_cover jsMsgSendTab" data-index="0">
                                                                                            <div class="media_cover">
                                                                        <span class="create_access">
                                                                        <a class="add_gray_wrp jsMsgSenderPopBt" href="javascript:;" data-type="10" data-index="0">
                                                                        <i class="icon36_common add_gray"></i>
                                                                        <strong>点击从图片库中选择</strong>
                                                                        </a>
                                                                        </span>
                                                                                            </div>
                                                                                            <div class="msgItemA img_pick" style="display: none">

                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>

                                                                                <div class="tab_content" id="click-voice" style="display: none">
                                                                                    <div class="js_audioArea inner js_clickChoice" data-type="voice">
                                                                                        <!--type 10图文 2图片  3语音 15视频 11商品消息-->
                                                                                        <div class="tab_cont_cover jsMsgSendTab" data-index="0">
                                                                                            <div class="media_cover" >
                                                                        <span class="create_access" >
                                                                        <a class="add_gray_wrp jsMsgSenderPopBt" href="javascript:;" data-type="10" data-index="0">
                                                                        <i class="icon36_common add_gray"></i>
                                                                        <strong>点击从语音库中选择</strong>
                                                                        </a>
                                                                        </span>
                                                                                            </div>
                                                                                            <div class="msgItemA img_pick" style="display: none">

                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>

                                                                                <div class="tab_content" id="click-video" style="display: none">
                                                                                    <div class="js_imgArea inner js_clickChoice" data-type="video">
                                                                                        <div class="tab_cont_cover jsMsgSendTab" data-index="0">
                                                                                            <div class="media_cover" >
                                                                        <span class="create_access">
                                                                        <a class="add_gray_wrp jsMsgSenderPopBt" href="javascript:;" data-type="10" data-index="0">
                                                                        <i class="icon36_common add_gray"></i>
                                                                        <strong>点击从视频库中选择</strong>
                                                                        </a>
                                                                        </span>
                                                                                            </div>
                                                                                            <div class="msgItemA img_pick" style="display: none">
                                                                                                <li class="img_item js_imageitem red"></li>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <p class="profile_link_msg_global menu_send mini_tips warn dn js_warn" style="display: none;"> 请勿添加其他公众号的主页链接 </p>
                                                                </div>
                                                                <div class="menu_content send jsMain" id="task" style="display: none">
                                                                    <div class="menu_content task_content">
                                                                        <p class="menu_content_tips" style="text-align: left;">
                                                                            订阅者点击以下选项可自定义回复内容</p>
                                                                        <ul id="task_ul">
                                                                        </ul>
                                                                    </div>
                                                                    <p class="profile_link_msg_global menu_send mini_tips warn dn js_warn" style="display: none;"> 请勿添加其他公众号的主页链接 </p>
                                                                </div>
                                                                <div class="menu_content url jsMain" id="url" style="text-align: left;display: none">
                                                                    <form action="" id="urlForm" onsubmit="return false;">
                                                                        <p class="menu_content_tips">订阅者点击该子菜单会跳到以下链接</p>
                                                                        <div class="input-group" style="width: 100%;">
                                                                            <label class="menu_content_name">页面地址</label>
                                                                            <input type="text" class="menu_content_input" name="urlText" placeholder="请输入网址" onblur="addHttp(this)">
                                                                        </div>
                                                                        <div class="frm_control_group btn_appmsg_wrap">
                                                                            <div class="frm_controls">
                                                                                <p class="frm_msg fail dn" id="urlUnSelect" style="display: none;">
                                                                                    <span for="urlText" class="frm_msg_content" style="display: inline;">请选择一篇文章</span>
                                                                                </p>
                                                                                <a href="javascript:void(0);" class="dn btn btn_default"
                                                                                   id="js_reChangeAppmsg" style="display: none;">重新选择</a>
                                                                            </div>
                                                                        </div>
                                                                    </form>
                                                                </div>
                                                                <div class="menu_content weapp jsMain" id="weapp" style="text-align: left;display: none">
                                                                    <div class="link_weapp_loading js_link_weapp_loading" style="display: none;"><i class="icon_loading_small white"></i></div>
                                                                    <div class="weapp_empty_box js_weapp_no_binded_hint" style="display: none;">
                                                                        <p class="desc">
                                                                            自定义菜单可跳转已绑定的小程序，本公众号尚未绑定小程序。
                                                                        </p>
                                                                        <a href="https://mp.weixin.qq.com/cgi-bin/wxopen?action=list&amp;token=247057149&amp;lang=zh_CN"
                                                                           class="btn btn_default">前往绑定</a>
                                                                    </div>
                                                                    <form action="" id="weappSettingsForm" onsubmit="return false;" style="display: block;">
                                                                        <p class="menu_content_tips">订阅者点击该子菜单会跳到以下小程序</p>
                                                                        <div>
                                                                            <label class="menu_content_name" style="margin-right: 39px;">小程序</label>
                                                                            <input type="button" class="btn btn-default" value="选择小程序">
                                                                        </div>
                                                                        <div class="frm_control_group js_weapp_path_group" style="display: none;">
                                                                            <label for="" class="frm_label">小程序路径</label>
                                                                            <div class="frm_controls">
                                                        <span class="frm_input_box">
                                                        <input type="text" class="frm_input js_weapp_path" name="">
                                                        </span>
                                                                                <p class="frm_tips"> 已选择小程序 -
                                                                                    <span class="js_weapp_title"></span>
                                                                                </p>
                                                                                <a href="" class="btn btn_default js_weapp_select">重新选择</a>
                                                                            </div>
                                                                        </div>
                                                                        <div style="margin-top: 10px">
                                                                            <label class="menu_content_name">备用网页</label>
                                                                            <input type="text" name="urlText" class="menu_content_input"  onblur="addHttp(this)" placeholder="请输入小程序备用网址">
                                                                        </div>
                                                                        <p class="frm_tips" id="" style="margin-top: 3%">
                                                                            旧版微信客户端无法支持小程序，用户点击菜单时将会打开备用网页。 </p>
                                                                    </form>
                                                                </div>
                                                                <div class="menu_content sended jsMain" style="display:none;">
                                                                    <p class="menu_content_tips tips_global">订阅者点击该子菜单会跳到以下链接</p>
                                                                    <div class="msg_wrp" id="viewDiv"></div>
                                                                    <p class="frm_tips">来自<span class="js_name">素材库</span><span style="display:none;"> -《<span class="js_title"></span>》</span></p>
                                                                </div>
                                                                <div id="js_errTips" style="display:none;" class="msg_sender_msg mini_tips warn"></div>
                                                            </div>
                                                        </div>

                                                    </div>
                                                    <div class="" style="text-align: center" >
                                                        <a href="javascript:void(0)" onclick="clickSave()" class="btn btn-info">
                                                            <i class="fa fa-fw  fa-save"></i>保存
                                                        </a>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                            </div>
                                            <modal :SubData="choiceData" :subPage="choicePage" :type="modalType" @pic-message="picMessage" v-if="modal"></modal>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div id="image_pick_modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                                <div class="modal-dialog" style="width:50%;min-height: 50%">
                                    <form class="form-horizontal" id="material_upload_modal_form">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="clickCancel()">
                                                    &times;
                                                </button>
                                                <h4 class="modal-title">
                                                    选择素材
                                                </h4>
                                            </div>
                                            <div class="modal-body">
                                                <div class="dialog_bd">
                                                    <div class="img_pick_panel inner_container_box side_l cell_layout">
                                                        <div class="inner_side" style="border-right:1px solid #e7e7eb">
                                                            <div class="group_list">
                                                                <div class="inner_menu_box">
                                                                    <dl class="inner_menu js_group" id="js_group">
                                                                        <!--<dd id="js_group0" class="inner_menu_item js_groupitem" :class="{'selected':groupId == ''}" @click="clickClass('')">-->
                                                                        <!--<a href="javascript:;" class="inner_menu_link">-->
                                                                        <!--<strong>全部素材</strong>-->
                                                                        <!--</a>-->
                                                                        <!--</dd>-->
                                                                        <!--<dd id="js_group1" class="inner_menu_item js_groupitem" :class="{'selected':groupId == '0'}" @click="clickClass('0')">-->
                                                                        <!--<a href="javascript:;" class="inner_menu_link">-->
                                                                        <!--<strong>未分组</strong>-->
                                                                        <!--</a>-->
                                                                        <!--</dd>-->
                                                                        <!--<dd class="inner_menu_item js_groupitem">-->
                                                                        <!--<a href="javascript:void(0);" class="inner_menu_link">-->
                                                                        <!--<strong>{{picBranch.groupName}}</strong>-->
                                                                        <!--</a>-->
                                                                        <!--</dd>-->

                                                                    </dl>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="inner_main" style="margin-left: 10px">
                                                            <input type="hidden" id="image_group_id" value="">
                                                            <div class="img_pick_area">
                                                                <div class="img_pick_area_inner" id="type_no_news">
                                                                    <div class="img_pick">
                                                                        <i class="icon_loading_small white js_loading" style="display: none;"></i>
                                                                        <ul class="group js_list img_list" id="type_list">
                                                                            <!--<li class="img_item js_imageitem " id="type_image"  >-->
                                                                            <!--<label class="frm_checkbox_label img_item_bd">-->
                                                                            <!--<div class="pic_box" style="height:120px">-->
                                                                            <!--<img class="pic js_pic" :src="picBranch.url">-->
                                                                            <!--<input type="radio" style="position:absolute" :data-name="picBranch.name" name="image_choiced" :value="picBranch.mediaId" @click="clickPicRadio(picBranch)">-->
                                                                            <!--</div>-->
                                                                            <!--<span class="lbl_content">{{picBranch.name}}</span>-->
                                                                            <!--</label>-->
                                                                            <!--</li>-->
                                                                            <!--<li class="img_item img_item_aduio js_imageitem" id="type_voice">-->
                                                                            <!--<label class="frm_checkbox_label img_item_bd">-->
                                                                            <!--<div class="pic_box">-->
                                                                            <!--<span class="lbl_content">{{picBranch.name}}</span>-->
                                                                            <!--<span class="lbl_content">更新于{{picBranch.updateTime}}</span>-->
                                                                            <!--<audio :src="picBranch.url" controls="" style="width: 100%"></audio>-->
                                                                            <!--<input type="radio" style="position:absolute;top:0;left:0" :data-name="picBranch.name" name="image_choiced" :value="picBranch.mediaId" @click="clickPicRadio(picBranch)">-->
                                                                            <!--</div>-->
                                                                            <!--</label>-->
                                                                            <!--</li>-->
                                                                            <!--<li class="img_item js_imageitem" id="type_video"  style="width:45% !important" >-->
                                                                            <!--<label class="frm_checkbox_label img_item_bd">-->
                                                                            <!--<div class="pic_box" style="height:10em !important">-->
                                                                            <!--<video  class="pic js_pic" controls="" :src="picBranch.url" style="width:100% !important;height:auto;cursor:default"></video>-->
                                                                            <!--<input type="radio" style="position:absolute;top:0;left:0" :data-name="picBranch.name" name="image_choiced" :value="picBranch.mediaId" @click="clickPicRadio(picBranch)">-->
                                                                            <!--</div>-->
                                                                            <!--<span class="lbl_content">{{picBranch.name}}</span>-->
                                                                            <!--<span class="lbl_content">更新于{{picBranch.updateTime}}</span>-->
                                                                            <!--</label>-->
                                                                            <!--</li>-->
                                                                        </ul>
                                                                    </div>
                                                                </div>
                                                                <div id="type_news" class="waterfall">
                                                                    <!--<div class="msgItem" v-for="(picBranch, index) in dataShow">-->
                                                                    <!--<div class="msgContent">-->
                                                                    <!--<div class="msgInfo">更新于{{picBranch.updateTime}}</div>-->
                                                                    <!--<div class="msgCoverImg" v-for="(article, index) in picBranch.articles" :class="{'appMsg':index !== 0}">-->
                                                                    <!--<h4><a href="#">{{article.title}}</a></h4>-->
                                                                    <!--<div class="msgImg"  v-bind:style="{backgroundImage:'url('+article.thumbUrl+')'}"></div>-->
                                                                    <!--<a :href=article.clickUrl target="view_window" class="msgImgCover previewMsg">-->
                                                                    <!--<div class="msgCoverContent"><p>预览文章</p></div>-->
                                                                    <!--</a>-->
                                                                    <!--</div>-->
                                                                    <!--</div>-->
                                                                    <!--<div class="msgOpr">-->
                                                                    <!--<span @click="clickChoiceNews(picBranch)" class="glyphicon-ok-parent">选择</span>-->
                                                                    <!--</div>-->
                                                                    <!--</div>-->
                                                                </div>
                                                            </div>
                                                            <hr>
                                                            <div class="page" style="float: right;text-align: right;">
                                                                <a class="btn btn-xs btn-default" href="javascript:void(0);" onclick='flip("prePage")'>上一页</a>
                                                                <a class="btn btn-xs btn-default" href="javascript:void(0);"  onclick='flip("nextPage")'>下一页</a>
                                                                <div>
                                                                    找到<span style='color: #FD7B02;' id="data_page_total">null</span>条数据，共<span style='color: #FD7B02;' id="image_page_count">null</span>页，每页最多 <span id="image_page_size">null</span> 条数据
                                                                    <!--&nbsp;到第-->
                                                                    <!--<input style="text-align:center;width:3em;" class="w20 mar0 alignCenter" id="data_page_index"  maxlength="5" type="text" :value='(pageIndex+1)'> 页-->
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <a href="javascript:void(0)" class="btn btn-danger" data-dismiss="modal" onclick="clickCancel()">
                                                    <i class="fa fa-fw fa-remove"></i>
                                                    取消
                                                </a>
                                                <a href="javascript:void(0)" id="group_set_modal_submit" class="btn btn-info" onclick="clickConfirm()">
                                                    <i class="fa fa-fw  fa-save"></i>确定
                                                </a>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                        <div id="spinner" class="spinner">
                            <div class="rect1"></div>
                            <div class="rect2"></div>
                            <div class="rect3"></div>
                            <div class="rect4"></div>
                            <div class="rect5"></div>
                            <h5>数据加载中...</h5>
                        </div>
                        <div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" id="deleteModal" style="display: none">
                            <div class="modal-dialog" role="document">
                                <div class="modal-content">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                <span aria-hidden="true">×</span>
                                            </button>
                                            <h4 class="modal-title" style="text-align: left">警告</h4>
                                        </div>
                                        <div class="modal-body">
                                            <p style="text-align:center">确定要删除该菜单吗?</p>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-primary confirmButton" onclick="sureDelete()">确定</button>
                                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button></div></div>
                                </div>
                            </div>
                        </div>
                        <div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" id="iframeModal" style="display: none">
                            <div class="modal-dialog modal-lg" role="document" style="width: 80%">
                                <div class="modal-content">
                                    <div class="modal-content">
                                        <div class="modal-header" >
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"  onclick="clickCancel()">
                                                <span aria-hidden="true">×</span>
                                            </button>
                                            <h4 class="modal-title" style="text-align: left">自定义任务</h4>
                                        </div>
                                        <div class="modal-body" style="height: 500px !important;">
                                            <iframe id="taskIframe"  frameborder="0" style="width: 100%;height: 100%"></iframe>
                                        </div>
                                        <div class="modal-footer" style="">
                                            <button type="button" class="btn btn-default" data-dismiss="modal" onclick="clickCancel()">关闭</button>
                                            <button type="button" class="btn btn-primary confirmButton" onclick="sureIframe()">确定</button>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>
                        <div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" id="dataNull" style="display: none">
                            <div class="modal-dialog" role="document">
                                <div class="modal-content">
                                    <div class="modal-content">
                                        <div class="modal-header" >
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                <span aria-hidden="true">×</span>
                                            </button>
                                            <h4 class="modal-title" style="text-align: left">警告</h4>
                                        </div>
                                        <div class="modal-body">
                                            <p style="text-align:center">未知错误！请刷新页面</p>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button></div></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<footer class="main-footer">
    <@common.footer />
</footer>
</@macroCommon.html>