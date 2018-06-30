var allData=null;
var menuData=null;
var index=null;//下标
var childrenData=null;
var subIndex=null;//子菜单下标
var accountId=null;
var typeDefault=null;
var menuId=null;
var radioType = '';

//页面加载
window.onload = function () {
    if ($("#accountId").val() == undefined){
        urlHref=window.location.href
    }
    // var urlHref = 'http://all.idea.com:8200/api/wechat/account/menu/entity/toedit?accountId=111&isdefault=1&menuId=239';
    //      var urlHref = window.location.href;
    function parseURL (url) {
        var urlB = url.split('?')[1]
        var para = urlB.split('&')
        var len = para.length
        var res = {}
        var arr = []
        for (var i = 0; i < len; i++) {
            arr = para[i].split('=')
            res[arr[0]] = arr[1]
        }
        return res
    }
    accountId = parseURL(urlHref).accountId
    typeDefault = parseURL(urlHref).isdefault
    menuId = parseURL(urlHref).menuId
    getData();
    //点击主菜单
    $("#menuList").on("click",".jsMenu",function () {
        if($(this).hasClass("disabled")){
            return
        }
        $(".jsMenu").each(function () {
            //移除主菜单选择标志
            $(this).removeClass("selected")
        })
        $(".jslevel2").each(function () {
            //移除子菜单选择标志
            $(this).removeClass("selected")
        })
        $(this).addClass("selected")
        index = $(this).data("index");
        //是否显示子菜单添加按钮
        if (allData.result.entities[index].children.length<5){
            $(this).find("div").find('li.js_addMenuBox_classB').show()
        }else {
            $(this).find("div").find('li.js_addMenuBox_classB').hide()
        }
        resetSelected();
        allData.result.entities[index].selected = 'selected';
        showRight(this);
    })
    //点击子菜单
    $("#menuList").on("click",".jslevel2",function (event) {
        event.stopPropagation();
        if($(this).hasClass("disabled")){
            return
        }
        $(".jsMenu").each(function () {
            //移除主菜单选择标志
            $(this).removeClass("selected")
        })
        $(".jslevel2").each(function () {
            //移除子菜单选择标志
            $(this).removeClass("selected")
        })
        $(this).addClass("selected");
        subIndex = $(this).data('index');
        resetSelected();
        allData.result.entities[index].children[subIndex].selected = 'selected';
        showRight(this);
    })
    //点击子菜单添加按钮
    $("#menuList").on("click",".js_addMenuBox_classB",function (event) {
        resetSelected();
        event.stopPropagation();
        allData.result.entities[index].children.push({'sort': allData.result.entities[index].children.length, 'name': '子菜单', 'type': 'view', 'mediaId': null, 'displayConfig': null, 'rawConfig': null, 'pluginCode': null, 'appid': null, 'pagePath': null, 'parentId': 0, 'key': null, 'selected': 'selected', 'url': null})
        radioType = 'view'
        menuData = allData.result.entities;
        var num = menuData[index].children.length;//length加1了
        var addSubLi = '<li class="jslevel2 current unselected" data-index="'+(num-1)+'">' +
            '<a href="javascript:void(0);" draggable="false" class="jsSubView aMove">' +
            '<span class="sub_pre_menu_inner js_sub_pre_menu_inner">' +
            '<i class="icon20_common "></i>' +
            ' <span class="js_l1Title">'+menuData[index].children[num-1].name+'</span>' +
            '</span></a></li>'
        $(this).parent().append(addSubLi)

        //是否显示子菜单添加按钮
        if (allData.result.entities[index].children.length<5){
            $(this).parent().find('li.js_addMenuBox_classB').show()
        }else {
            $(this).parent().find('li.js_addMenuBox_classB').hide();
        }
        $(".jsMenu").each(function () {
            //移除主菜单选择标志
            $(this).removeClass("selected")
        })
        $(".jslevel2").each(function () {
            //移除子菜单选择标志
            $(this).removeClass("selected")
        })
        $(this).parent().find("li.jslevel2").each(function () {
            if ($(this).data("index") == num-1){
                $(this).addClass("selected")
                showRight(this)
            }
        })
        $("#orderBt").show()
        if (allData.result.entities.length<=1){
            $("#orderBt").hide()
            if (allData.result.entities[0].children.length>1){
                $("#orderBt").show()
            }
        }
    })
    //点击菜单内容单选按钮
    $("#menu_content").on("click","span",function () {
        $(".frm_radio").each(function () {
            //单选按钮
            $(this).prop("checked",false)
        })
        $(this).find('input').prop("checked",true)
        $(".jsMain").each(function () {
            //二级选项
            $(this).hide()
        })
        switch ($(this).find('input')[0].name){
            case 'sendMsg':
                $("#edit").show();
                radioType='media_id';
                subType='media_id';
                clickSubmenu($(".tab_appmsg")[0],'news')
                break;
            case 'url':
                $("#url").show();
                $("#url input[name='urlText']").val("");
                radioType='view';
                break;
            case 'weapp':
                $("#weapp").show();
                radioType='miniprogram';
                $("#weapp input[name='urlText']").val("");
                break;
            case 'task':
                $("#task").show();
                radioType='task';
                taskData()
                break;
        }
        return
        // for (var i=0;i<allData.result.entities.length;i++) {
        //     if(allData.result.entities[i].selected == 'selected'){
        //         allData.result.entities[i].type = type;
        //     } else {
        //         if (allData.result.entities[i].children.length>0) {
        //             for (var j=0;j<allData.result.entities[i].children.length;j++) {
        //                 if(allData.result.entities[i].children[j].selected == 'selected') {
        //                     allData.result.entities[i].children[j].type = type;
        //                 }
        //             }
        //         }
        //     }
        // }
    })
    //点击从库中选择
    var currentPage=0;
    var currentGroupId='';
    var currentType;
    var maxPage;
    $(".tab_content").on("click",".js_clickChoice",function () {
        mediaId= '';
        $("#spinner").show()
        var _this = $(this)
        currentType=_this.data("type")
        $.ajax({
            type: "GET",
            url: "/api/wechat/material/list",
            data:{
                accountId: 111,
                type:$(this).data("type"),
                groupId: '',
                pageIndex: 0
            },
            dataType: "json",
            success: function(msg){
                $("#spinner").hide();
                $("#js_group").html("")
                var classDom =
                    ' <dd id="js_group0" class="inner_menu_item js_groupitem selected"  onclick="clickClass(\'\',\''+_this.data("type")+'\',this)">' +
                    '   <a href="javascript:;" class="inner_menu_link">' +
                    '     <strong>全部素材</strong>' +
                    '   </a>' +
                    '</dd>' +
                    '<dd id="js_group1" class="inner_menu_item js_groupitem"  onclick="clickClass(0,\''+_this.data("type")+'\',this)">' +
                    '   <a href="javascript:;" class="inner_menu_link">' +
                    '     <strong>未分组</strong>' +
                    '   </a>' +
                    '</dd>'
                $("#js_group").html(classDom)
                var subData = msg
                var detailData = subData.result
                var groupNames = []
                for (var i=0;i<detailData.length;i++) {
                    if (detailData[i].groupName) {
                        if(groupNames.indexOf(detailData[i].groupName) == -1){
                            groupNames.push({groupName:detailData[i].groupName,groupId:detailData[i].groupId})
                        }
                    }
                }
                if(groupNames.length>0){
                    for (var i=0;i<groupNames.length;i++){
                        var classDom=' <dd class="inner_menu_item js_groupitem" onclick="clickClass('+groupNames[i].groupId+',\''+_this.data("type")+'\',this)">' +
                            '<a href="javascript:void(0);" class="inner_menu_link">' +
                            '<strong>'+groupNames[i].groupName+'</strong>\n' +
                            '</a>\n' +
                            '</dd>'
                        $("#js_group1").after(classDom)
                    }
                }
                $("#data_page_total").html(subData.page.totalCount)
                $("#image_page_count").html(subData.page.pageCount)
                maxPage =  subData.page.pageCount
                $("#image_page_size").html(12)
                if (_this.data("type")=="news"){
                    $("#type_news").show();
                    $("#type_no_news").hide();
                    $("#type_news").html("");
                    for (var i=0;i<detailData.length;i++){
                        var liSubDom = "";
                        var liSubDomList = "";
                        for(var j=0;j<detailData[i].articles.length;j++) {
                            var clickUrl = 'http://www.baidu.com';
                            if (detailData[i].articles[j].clickUrl){
                                clickUrl = detailData[i].articles[j].clickUrl
                            }
                            if (j==0){
                                liSubDom =
                                    '<div class="msgCoverImg" v-for="(article, index) in picBranch.articles" :class="{\'appMsg\':index !== 0}">' +
                                    '<h4><a href="#">'+detailData[i].articles[j].title+'</a></h4>' +
                                    '<div class="msgImg"  v-bind:style="{backgroundImage:\'url(\'+article.thumbUrl+\')\'}"></div>' +
                                    '<a href='+clickUrl +' class="msgImgCover previewMsg"  target="view_window">' +
                                    '<div class="msgCoverContent"><p>预览文章</p></div>' +
                                    '</a>' +
                                    '</div>'
                            }else {
                                liSubDom =
                                    '<div class="msgCoverImg appMsg" v-for="(article, index) in picBranch.articles" :class="{\'appMsg\':index !== 0}">' +
                                    '<h4><a href="#">'+detailData[i].articles[j].title+'</a></h4>' +
                                    '<div class="msgImg"  v-bind:style="{backgroundImage:\'url(\'+article.thumbUrl+\')\'}"></div>' +
                                    '<a href='+clickUrl +' class="msgImgCover previewMsg"  target="view_window">' +
                                    '<div class="msgCoverContent"><p>预览文章</p></div>' +
                                    '</a>' +
                                    '</div>'
                            }

                            liSubDomList +=liSubDom;
                        }
                        var data=detailData[i].mediaId
                        var url=detailData[i].url
                        var liDom =
                            '<div class="msgItem" v-for="(picBranch, index) in dataShow">' +
                            ' <div class="msgContent">' +
                            '<div class="msgInfo">更新于'+detailData[i].updateTime+'</div>' + liSubDomList +
                            '<div class="msgOpr">' +
                            '<span onclick="clickChoiceNews(this,\''+data+'\',\''+url+'\')" class="glyphicon-ok-parent">选择</span>' +
                            '</div>' +
                            '</div>' +
                            '</div>'
                        $("#type_news").append(liDom)
                    }
                }else {
                    $("#type_news").hide();
                    $("#type_no_news").show();
//            $("#type_list>li").each(function () {
//                $(this).hide()
//            })
                    $("#type_list").html("");
                    switch (_this.data("type")){
                        case "image":
                            for (var i=0;i<detailData.length;i++){
                                var data = detailData[i].mediaId;
                                var url = detailData[i].url;
                                var liDom = '<li class="img_item js_imageitem " >' +
                                    '<label class="frm_checkbox_label img_item_bd">' +
                                    '<div class="pic_box" style="height:120px">' +
                                    '<img class="pic js_pic" src='+detailData[i].url+'>' +
                                    '</div>' +
                                    '<span class="lbl_content">'+detailData[i].title+'</span>' +
                                    '</label>' +
                                    '<div class="msgOpr">' +
                                    '<span onclick="clickChoiceNotNews(this,\'image\',\''+data+'\',\''+url+'\')" class="glyphicon-ok-parent">选择</span>' +
                                    '</div>' +
                                    '</li>'
                                $("#type_list").append(liDom);
                            }

                            break;
                        case "voice":
                            for (var i=0;i<detailData.length;i++){
                                var data = detailData[i].mediaId;
                                var data = detailData[i].url;
                                var liDom= '<li class="img_item img_item_aduio js_imageitem" >' +
                                    '<label class="frm_checkbox_label img_item_bd">' +
                                    '<div class="pic_box">' +
                                    '<span class="lbl_content">'+detailData[i].title+'</span>' +
                                    '<span class="lbl_content">更新于'+detailData[i].updateTime+'</span>' +
                                    '<audio src='+detailData[i].url+' controls="" style="width: 100%"></audio>' +
                                    '</div>' +
                                    '</label>' +
                                    '<div class="msgOpr">' +
                                    '<span onclick="clickChoiceNotNews(this,\'voice\',\''+data+'\',\''+url+'\')" class="glyphicon-ok-parent">选择</span>' +
                                    '</div>' +
                                    '</li>'
                                $("#type_list").append(liDom);
                            }
                            break;
                        case "video":
                            for (var i=0;i<detailData.length;i++){
                                var data = detailData[i].mediaId;
                                var data = detailData[i].url;
                                var liDom = ' <li class="img_item js_imageitem"   style="width:45% !important" >' +
                                    '<label class="frm_checkbox_label img_item_bd">' +
                                    '<div class="pic_box" style="height:10em !important">' +
                                    '<video  class="pic js_pic" controls="" src='+detailData[i].url+' style="width:100% !important;height:auto;cursor:default"></video>' +
                                    '</div>' +
                                    '<span class="lbl_content">'+detailData[i].title+'</span>' +
                                    '<span class="lbl_content">更新于'+detailData[i].updateTime+'</span>' +
                                    '</label>' +
                                    '<div class="msgOpr">' +
                                    '<span onclick="clickChoiceNotNews(this,\'video\',\''+data+'\',\''+url+'\')" class="glyphicon-ok-parent">选择</span>' +
                                    '</div>' +
                                    '</li>'
                                $("#type_list").append(liDom);
                            }
                            break;
                    }
                }

                $("#image_pick_modal").modal("show")
            }
        });

    })
}
//获取数据请求
function getData() {
    $.ajax({
        type: "GET",
        url: "/api/wechat/account/menu/edit",
        dataType: "json",
        data:{
            accountId:accountId,
            isdefault:typeDefault,
            menuId: menuId
        },
        success: function(msg){
            allData = msg;
            $("#orderBt").show()
            if (allData.result.entities.length<=1){
                $("#orderBt").hide()
                if(allData.result.entities.length>0){
                    if (allData.result.entities[0].children.length>1){
                        $("#orderBt").show()
                    }
                }
            }
            showLeft();
            $("#spinner").hide()
        }
    });
}
//点击下拉框选择微信账号
function transParam() {
    accountId = $("#accountId").val()
    getData()
    $("#orderBt").show()
    $("#drag-left").hide()
    $("#click-left").show()
    $(".panel-heading.menu-right").hide();
    $(".panel-body.menu-right").hide();
    // var src = "/wechat/menu/param?isdefault=1&menuId=0&accountId=" + $("#accountId").val() + "&bbb=233";
}
//左边菜单模块
function showLeft() {
    menuData = allData.result.entities;
    $("#menuList").html("")
    var addMainBtn= '<li class="js_addMenuBox pre_menu_item grid_item no_extra" id="js_addMenuBox_classA" onclick="clickAddMainMenu(this)">\n' +
        '<a href="javascript:void(0);" class="pre_menu_link js_addL1Btn"\n' +
        'title="最多添加3个一级菜单" draggable="false">\n' +
        '<i class="icon14_menu_add "></i>\n' +
        '</a>\n' +
        '</li>'
    $("#menuList").html(addMainBtn)
    for (var i= 0 ;i<menuData.length;i++) {
        var num;
        if (menuData.length>=2){
            num =3
        }else {
            num = 2
        }
        if (menuData.length>0){
            var addLi = '<li class="jsMenu pre_menu_item grid_item jslevel1 ui-sortable ui-sortable-disabled unselected size1of'+num+'" data-index="'+i+'">' +
                '<a href="javascript:void(0);" draggable="false" class="pre_menu_link aMove">' +
                '<i class="icon_menu_dot js_icon_menu_dot dn" style="display: none;"></i> ' +
                '<i class="icon20_common"></i> ' +
                '<span class="js_l1Title">'+menuData[i].name+'</span>' +
                '</a> ' +
                '<div class="sub_pre_menu_box js_l2TitleBox" style="display: none">' +
                '<ul data-selected="branch" class="sub_pre_menu_list" style="border-top: 1px solid rgb(231, 231, 235);">' +
                '<li class="js_addMenuBox_classB"  >' +
                '<a href="javascript:void(0);" title="最多添加5个子菜单" draggable="false" class="jsSubView js_addL2Btn">' +
                '<span class="sub_pre_menu_inner js_sub_pre_menu_inner"><i class="icon14_menu_add"></i></span></a></li></ul> <i class="arrow arrow_out"></i> <i class="arrow arrow_in"></i></div>'
            '</li>'
            $("#js_addMenuBox_classA").before(addLi)
            switch (menuData.length){
                case 3 :
                    $("#js_addMenuBox_classA").hide();
                    break;
                case 2 :
                    $("#js_addMenuBox_classA").addClass('size1of3');
                    break;
                case 1 :
                    $("#js_addMenuBox_classA").addClass('size1of2');
                    break;
            }
        }
    }
}
//点击主菜单添加按钮
function clickAddMainMenu(e) {
    resetSelected();
    allData.result.entities.push({'menuId': menuId, 'sort': allData.result.entities.length, 'name': '菜单名称', 'type': 'view', 'mediaId': null, 'displayConfig': null, 'rawConfig': null, 'pluginCode': null, 'appid': null, 'pagePath': null, 'parentId': 0, 'key': null, 'selected': 'selected', 'url': '', 'children': []})
    radioType = 'view'
    $("#orderBt").show()
    if (allData.result.entities.length<=1){
        $("#orderBt").hide()
        if (allData.result.entities[0].children.length>1){
            $("#orderBt").show()
        }
    }
    showLeft();
    var num= allData.result.entities.length-1
    index =num
    $("#menuList").find("li.jsMenu").each(function () {
        if ($(this).data("index") == num){
            $(this).addClass("selected")
            showRight(this)
        }
    })
//        showRight(e)
}
//显示子菜单
function showSubMenu(e) {
    $(e).find('div').find('ul').find('li').each(function () {
        if ($(this).hasClass('jslevel2')){
            $(this).remove()
        }
    })
    $(e).find('div').show()
    childrenData = menuData[index].children
    for (var i = 0;i<childrenData.length;i++) {
        var addSubLi = '<li class="jslevel2 current unselected" data-index="'+i+'">' +
            '<a href="javascript:void(0);" draggable="false" class="jsSubView aMove">' +
            '<span class="sub_pre_menu_inner js_sub_pre_menu_inner">' +
            '<i class="icon20_common "></i>' +
            ' <span class="js_l1Title">'+childrenData[i].name+'</span>' +
            '</span></a></li>'
        $(e).find('div').find('ul').append(addSubLi)
    }
//       $(e).append("<div class=\"sub_pre_menu_box js_l2TitleBox\"><ul data-selected=\"branch\" class=\"sub_pre_menu_list\" style=\"border-top: 1px solid rgb(231, 231, 235);\"><li class=\"jslevel2 current unselected\"><a href=\"javascript:void(0);\" draggable=\"false\" class=\"jsSubView aMove\"><span class=\"sub_pre_menu_inner js_sub_pre_menu_inner\"><i class=\"icon20_common \"></i> <span class=\"js_l1Title\">子菜单</span></span></a></li> <li class=\"js_addMenuBox_classB\"><a href=\"javascript:void(0);\" title=\"最多添加5个子菜单\" draggable=\"false\" class=\"jsSubView js_addL2Btn\"><span class=\"sub_pre_menu_inner js_sub_pre_menu_inner\"><i class=\"icon14_menu_add\"></i></span></a></li></ul> <i class=\"arrow arrow_out\"></i> <i class=\"arrow arrow_in\"></i></div>")
}
//点击右边二级选项
var subType='';
function clickSubmenu(e,type) {
    subType=''
    $(".tab_nav").each(function () {
        $(this).removeClass('selected')
    })
    $(".tab_content").each(function () {
        $(this).hide()
    })
    $(e).addClass('selected')
    subType='media_id';
    switch (type){
        case 'news':
            $("#click-news").show();
            $("#click-news .media_cover").show();
            $("#click-news .msgItemA").hide();
            break;
        case 'image':
            $("#click-image").show();
            $("#click-image .media_cover").show();
            $("#click-image .msgItemA").hide();
            break;
        case 'voice':
            $("#click-voice").show();
            $("#click-voice .media_cover").show();
            $("#click-voice .msgItemA").hide();
            break;
        case 'video':
            $("#click-video").show();
            $("#click-video .media_cover").show();
            $("#click-video .msgItemA").hide();
            break;
        case 'click':
            $("#click-words").show();
            $("#click-words textarea").val("");
            subType='click';
            break;
    }
    return
    // for (var i=0;i<allData.result.entities.length;i++) {
    //     if(allData.result.entities[i].selected == 'selected'){
    //         allData.result.entities[i].type = subType;
    //         allData.result.entities[i].url = null;
    //         allData.result.entities[i].mediaId = null;
    //     } else {
    //         if (allData.result.entities[i].children.length>0) {
    //             for (var j=0;j<allData.result.entities[i].children.length;j++) {
    //                 if(allData.result.entities[i].children[j].selected == 'selected') {
    //                     allData.result.entities[i].children[j].type = subType;
    //                     allData.result.entities[i].children[j].url = null;
    //                     allData.result.entities[i].children[j].mediaId = null;
    //                 }
    //             }
    //         }
    //     }
    // }
}
//点击"跳转任务"时发送的请求
function taskData() {
    var displayConfig;
    var rawConfig;
    var code;
    for (var i=0;i<allData.result.entities.length;i++) {
        if(allData.result.entities[i].selected == 'selected'){
            displayConfig = allData.result.entities[i].displayConfig;
            rawConfig = allData.result.entities[i].rawConfig;
            code = allData.result.entities[i].pluginCode;
        } else {
            if (allData.result.entities[i].children.length>0) {
                for (var j=0;j<allData.result.entities[i].children.length;j++) {
                    if(allData.result.entities[i].children[j].selected == 'selected') {
                        displayConfig =allData.result.entities[i].children[j].displayConfig;
                        rawConfig = allData.result.entities[i].children[j].rawConfig;
                        code = allData.result.entities[i].children[j].pluginCode;
                    }
                }
            }
        }
    }
    menuData = allData.result.entities;
    if (displayConfig==null){
        displayConfig='';
    }
    $.ajax({
        type: "GET",
        url: "/api/wechat/actions/items?event=menu",
        dataType: "json",
        success: function(msg){
            $("#task_ul").html("")
            for (var i=0;i<msg.result.length;i++){
                if (msg.result[i].needConfig==true){
                    var signDom = '<span class="label label-info" >可配置</span>'
                }else {
                    var signDom = '<span class="label label-default">不可配置</span>'

                }
                if (code == msg.result[i].code){
                    var codeDom = '<span class="task_content_display_config" style="cursor: pointer" >'+displayConfig+'</span>'
                    var iDom ='<i class="glyphicon glyphicon-ok-sign green" ></i>'
                }else {
                    var codeDom = '<span class="task_content_display_config" style="cursor: pointer" ></span>';
                    var iDom = '<i class="glyphicon glyphicon-ok-sign" ></i>'
                }
                var liDom = '<li onclick="openIframeModal(\''+msg.result[i].code+'\','+msg.result[i].needConfig+','+ JSON.stringify(rawConfig).replace(/"/g, '&quot;')+')">' +
                    iDom +
                    '<span class="task_content_name">'+msg.result[i].action.name+'</span>' +
                    codeDom+
                    signDom +
                    '</li>'
                $("#task_ul").append(liDom)
            }
        }
    });
}
//点击"跳转任务"列表加载出模态框
var pluginCode=null;
function openIframeModal(code, needConfig, config) {
    if (needConfig) {
        pluginCode = code
        var src = '/wechat/actions/config?code=' + code + "&config=" + encodeURIComponent(config)
        $("#taskIframe").attr("src", src)
        $('#iframeModal').modal('show')
    } else {
        for (var i=0;i<allData.result.entities.length;i++) {
            if(allData.result.entities[i].selected == 'selected'){
                allData.result.entities[i].type = "none";
                allData.result.entities[i].rawConfig = null;
                allData.result.entities[i].pluginCode = code;
                allData.result.entities[i].key = parseInt(Date.parse(new Date())/Math.ceil(Math.random()*10));
            } else {
                if (allData.result.entities[i].children.length > 0) {
                    for (var j=0;j<allData.result.entities[i].children.length;j++) {
                        if(allData.result.entities[i].children[j].selected == 'selected') {
                            allData.result.entities[i].children[j].type = "none";
                            allData.result.entities[i].children[j].rawConfig = null;
                            allData.result.entities[i].children[j].pluginCode = code;
                            allData.result.entities[i].children[j].key = parseInt(Date.parse(new Date())/Math.ceil(Math.random()*10));
                        }
                    }
                }
            }
        }
        menuData = allData.result.entities;
        taskData()
    }
}
//点击"跳转任务"模态框确定按钮
function sureIframe() {
    if ($('#taskIframe')[0].contentWindow.getConfig() == undefined) {
        return
    } else {
        var config = JSON.parse(JSON.stringify($('#taskIframe')[0].contentWindow.getConfig()))
        for (var i=0;i<allData.result.entities.length;i++) {    //遍历一级菜单
            if(allData.result.entities[i].selected == 'selected'){
                allData.result.entities[i].type = "none";
                allData.result.entities[i].rawConfig = config;
                allData.result.entities[i].pluginCode = pluginCode;
                allData.result.entities[i].key = parseInt(Date.parse(new Date())/Math.ceil(Math.random()*10));
            } else { //遍历二级菜单
                if (allData.result.entities[i].children.length > 0) {
                    for (var j=0;j<allData.result.entities[i].children.length;j++) {
                        if(allData.result.entities[i].children[j].selected == 'selected') {
                            allData.result.entities[i].children[j].type = "none";
                            allData.result.entities[i].children[j].rawConfig = config;
                            allData.result.entities[i].children[j].pluginCode = pluginCode;
                            allData.result.entities[i].children[j].key = parseInt(Date.parse(new Date())/Math.ceil(Math.random()*10));
                        }
                    }
                }
            }
        }
        menuData = allData.result.entities;
        $('#iframeModal').modal('hide')
        taskData()
    }
    $('#iframeModal').modal('hide')
    taskData()
}
//显示右边模块
function showRight(e) {
    $("#click-left").hide();
    $(".panel-heading.menu-right").show();
    $(".panel-body.menu-right").show();
    $(".frm_radio").each(function () {
        //单选按钮
        $(this).prop("checked",false)
    })
    $(".jsMain").each(function () {
        //二级选项
        $(this).hide()
    })
    $(".js_l2TitleBox").each(function () {
        //子菜单
        $(this).hide()
    })
    $("#menu_content").show()
    $("#js_innerNone").hide()
    for (var i=0;i<allData.result.entities.length;i++) {
        if(allData.result.entities[i].selected == 'selected'){
            var showMenuData = allData.result.entities[i]
            $("#js_menu_name").val(showMenuData.name)
        } else {
            if (allData.result.entities[i].children.length>0) {
                for (var j=0;j<allData.result.entities[i].children.length;j++) {
                    if(allData.result.entities[i].children[j].selected == 'selected') {
                        var showMenuData = allData.result.entities[i].children[j]
                        $("#js_menu_name").val(showMenuData.name)
                        $(e).parent().parent().show()
                    }
                }
            }
        }
    }
    showSubMenu(e);//显示子菜单
    if (showMenuData.pluginCode == null){

        switch (showMenuData.type){
            case 'click':
                $("input[name='sendMsg']").prop("checked",true);
                $("#edit").show();
                $("#click-words textarea").val(showMenuData.url)
                showRightSub(showMenuData.type);
                break;
            case 'media_id':
                $("input[name='sendMsg']").prop("checked",true);
                $("#edit").show();
                showRightSub(showMenuData.type,showMenuData.id);
                break;
            case 'view':
                $("input[name='url']").prop("checked",true);
                $("#url").show();
                $("#url .menu_content_input").val(showMenuData.url);
                break;
            case 'miniprogram':
                $("input[name='weapp']").prop("checked",true);
                $("#weapp").show();
                $("#weapp .menu_content_input").val(showMenuData.url);
                break;
            case 'none' :
                $("#menu_content").hide();
                $("#js_innerNone").show();
                break;
        }
    } else {
        $("input[name='task']").prop("checked",true);
        $("#task").show();
        $("#menu_content_container").show()
        taskData();
//            $(".frm_radio")
    }
}
//初始化右边二级菜单模块
function showRightSub(type,id) {
    $(".tab_nav").each(function () {
        $(this).removeClass('selected')
    })
    $(".tab_content").each(function () {
        $(this).hide()
    })
    if (type == "click") {
        $("#click-words").show();
        $('.tab_text').addClass('selected')
    } else if(type == "media_id"){
        $.ajax({
            type: "GET",
            url: "/api/wechat/material/getone",
            data: {entityId: id},
            dataType: "json",
            success: function(msg){
                switch (msg.result.type){
                    case 'news':
                        $("#click-news").show();
                        $("#click-news .media_cover").show();
                        $("#click-news .msgItemA").hide();
                        $('.tab_appmsg').addClass('selected');
                        $("#click-news .media_cover").hide();
                        $("#click-news .msgItemA").show();
                        $("#click-news .msgItemA").html("");
                        var liSubDom = "";
                        var liSubDomList = "";
                        for(var j=0;j<msg.result.media.length;j++) {
                            if (j==0){
                                liSubDom =
                                    '<div class="msgCoverImg" v-for="(article, index) in picBranch.articles" :class="{\'appMsg\':index !== 0}">' +
                                    '<h4><a href="#">'+msg.result.media[j].title+'</a></h4>' +
                                    '<div class="msgImg"  v-bind:style="{backgroundImage:\'url(\'+article.thumbUrl+\')\'}"></div>' +
                                    '<a href='+msg.result.media[j].clickUrl +' class="msgImgCover previewMsg"  target="view_window">' +
                                    '<div class="msgCoverCont' +
                                    'ent"><p>预览文章</p></div>' +
                                    '</a>' +
                                    '</div>'
                            }else {
                                liSubDom =
                                    '<div class="msgCoverImg appMsg" v-for="(article, index) in picBranch.articles" :class="{\'appMsg\':index !== 0}">' +
                                    '<h4><a href="#">'+msg.result.media[j].title+'</a></h4>' +
                                    '<div class="msgImg"  v-bind:style="{backgroundImage:\'url(\'+article.thumbUrl+\')\'}"></div>' +
                                    '<a href='+msg.result.media[j].clickUrl +' class="msgImgCover previewMsg"  target="view_window">' +
                                    '<div class="msgCoverContent"><p>预览文章</p></div>' +
                                    '</a>' +
                                    '</div>'
                            }

                            liSubDomList +=liSubDom;
                        }
                        var liDom =
                            '<div class="msgItem" v-for="(picBranch, index) in dataShow">' +
                            ' <div class="msgContent">' +
                            '<div class="msgInfo">更新于'+msg.result.updateTime+'</div>' + liSubDomList +
                            '</div>' +
                            '</div>'
                        $("#click-news .msgItemA").html(liDom);

                        break;
                    case 'image':
                        $("#click-image").show();
                        $("#click-image .media_cover").show();
                        $("#click-image .msgItemA").hide();
                        $('.tab_img').addClass('selected');
                        $("#click-image .media_cover").hide();
                        $("#click-image .msgItemA").show();
                        $("#click-image .msgItemA").html("");
                        var liDom = '<li class="img_item js_imageitem " >' +
                            '<label class="frm_checkbox_label img_item_bd">' +
                            '<div class="pic_box" style="height:120px">' +
                            '<img class="pic js_pic" src='+msg.result.media.url+'>' +
                            '</div>' +
                            '<span class="lbl_content">'+msg.result.media.title+'</span>' +
                            '</label>' +
                            '</li>'
                        $("#click-image .msgItemA").html(liDom);
                        break;
                    case 'voice':
                        $("#click-voice").show();
                        $("#click-voice .media_cover").show();
                        $("#click-voice .msgItemA").hide();
                        $('.tab_audio').addClass('selected');
                        $("#click-voice .media_cover").hide();
                        $("#click-voice .msgItemA").show();
                        $("#click-voice .msgItemA").html("");
                        var liDom= '<li class="img_item img_item_aduio js_imageitem" >' +
                            '<label class="frm_checkbox_label img_item_bd">' +
                            '<div class="pic_box">' +
                            '<span class="lbl_content">'+msg.result.media.title+'</span>' +
                            '<span class="lbl_content">更新于'+msg.result.media.updateTime+'</span>' +
                            '<audio src='+msg.result.media.url+' controls="" style="width: 100%"></audio>' +
                            '</div>' +
                            '</label>' +
                            '</li>'
                        $("#click-voice .msgItemA").html(liDom);
                        break;
                    case 'video':
                        $("#click-video").show();
                        $("#click-video .media_cover").show();
                        $("#click-video .msgItemA").hide();
                        $('.tab_video').addClass('selected');
                        $("#click-video .media_cover").hide();
                        $("#click-video .msgItemA").show();
                        $("#click-video .msgItemA").html("");
                        var liDom = ' <li class="img_item js_imageitem" >' +
                            '<label class="frm_checkbox_label img_item_bd">' +
                            '<div class="pic_box" style="height:10em !important">' +
                            '<video  class="pic js_pic" controls="" src='+msg.result.media.url+' style="width:100% !important;height:auto;cursor:default"></video>' +
                            '</div>' +
                            '<span class="lbl_content">'+msg.result.media.title+'</span>' +
                            '<span class="lbl_content">更新于'+msg.result.media.updateTime+'</span>' +
                            '</label>' +
                            '</li>'
                        $("#click-video .msgItemA").html(liDom);
                        break;
                }
            }
        });
    }
    switch (type){
        case 'news':
            $("#click-news").show();
            $("#click-news .media_cover").show();
            $("#click-news .msgItemA").hide();
            break;
        case 'image':
            $("#click-image").show();
            $("#click-image .media_cover").show();
            $("#click-image .msgItemA").hide();
            break;
        case 'voice':
            $("#click-voice").show();
            $("#click-voice .media_cover").show();
            $("#click-voice .msgItemA").hide();
            break;
        case 'video':
            $("#click-video").show();
            $("#click-video .media_cover").show();
            $("#click-video .msgItemA").hide();
            break;
        case 'click':
            $("#click-words").show();
            subType='click';
            break;
    }
}
//修改菜单名称
function modifyName(e) {
    $(e).removeClass("borderRed")
    if($(e).val().length==0){
        $(e).addClass("borderRed")
        return
    }
    $("#menuList li.selected>a>span").html($(e).val());
    for (var i=0;i<allData.result.entities.length;i++) {
        if(allData.result.entities[i].selected == 'selected'){
            allData.result.entities[i].name = $(e).val();
        } else {
            if (allData.result.entities[i].children.length>0) {
                for (var j=0;j<allData.result.entities[i].children.length;j++) {
                    if(allData.result.entities[i].children[j].selected == 'selected') {
                        allData.result.entities[i].children[j].name = $(e).val();
                    }
                }
            }
        }
    }
}
function inputBlur(e) {
    if($(e).hasClass("borderRed")){
        $(e).focus()
    }
}
//自动补齐https
function addHttp(e) {
    if ($(e).val().length === 0) {
        return false
    } else {
        var url
        var regex = /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/
        var result = regex.test($(e).val())
        if (result) {
            url = $(e).val()
        } else {
            url = 'http://' + $(e).val()
        }
    }
    $(e).val(url);
    if(radioType ==''){
        $('#menu_content input').each(function () {
            if ($(this).is(':checked')){
                switch ($(this)[0].name){
                    case 'sendMsg':
                        radioType='media_id';
                        break;
                    case 'url':
                        radioType='view';
                        break;
                    case 'weapp':
                        radioType='miniprogram';
                        break;
                    case 'task':
                        radioType='task';
                        break;
                }
            }
        })
    }

    for (var i=0;i<allData.result.entities.length;i++) {
        if(allData.result.entities[i].selected == 'selected'){
            allData.result.entities[i].url = url;
            allData.result.entities[i].type = radioType;
            allData.result.entities[i].pluginCode = null;
        } else {
            if (allData.result.entities[i].children.length>0) {
                for (var j=0;j<allData.result.entities[i].children.length;j++) {
                    if(allData.result.entities[i].children[j].selected == 'selected') {
                        allData.result.entities[i].children[j].url = url;
                        allData.result.entities[i].children[j].type = radioType;
                        allData.result.entities[i].children[j].pluginCode = null;
                    }
                }
            }
        }
    }
}
//重置selected属性为unselected
function resetSelected() {
    for (var i=0;i<allData.result.entities.length;i++) {
        allData.result.entities[i].selected = 'unselected';
        if (allData.result.entities[i].children.length>0) {
            for (var j=0;j<allData.result.entities[i].children.length;j++) {
                allData.result.entities[i].pluginCode = null
                allData.result.entities[i].type = 'none';
                allData.result.entities[i].children[j].selected = 'unselected';
            }
        }
    }
}
//点击图片素材的选择按钮
var mediaId = '';
var url = '';
function clickChoiceNews(e,data,urlL) {
    mediaId = data;
    url = urlL;
    console.log(url)
    $(".glyphicon-ok-parent").each(function () {
        $(this).css('backgroundColor','#f2f5f7')
        $(this).css('color','black')
    })
    $(e).css('backgroundColor','#1caf9a')
    $(e).css('color','white')
    $("#click-news .msgItemA").html("")
    $("#click-news .msgItemA").html($(e).parent().parent().parent().prop("outerHTML"))
}
//点击非图片类素材的选择按钮
function clickChoiceNotNews(e,type,data,urlL) {
    mediaId = data;
    url = urlL;
    $(".glyphicon-ok-parent").each(function () {
        $(this).css('backgroundColor','#f2f5f7')
        $(this).css('color','black')
    })
    $(e).css('backgroundColor','#1caf9a')
    $(e).css('color','white')
    switch (type){
        case 'image':
            $("#click-image .msgItemA").html("");
            $("#click-image .msgItemA").html($(e).parent().parent().prop("outerHTML"));
            break;
        case 'video':
            $("#click-video .msgItemA li").html("");
            $("#click-video .msgItemA li").html($(e).parent().parent().html());
            break;
        case 'voice':
            $("#click-voice .msgItemA").html("");
            $("#click-voice .msgItemA").html($(e).parent().parent().prop("outerHTML"));
            break;
    }

}
//点击素材确定按钮
function clickConfirm() {
    if(mediaId == ''){
        alert('请选择素材!')
        return
    }
    for (var i=0;i<allData.result.entities.length;i++) {
        if (allData.result.entities[i].selected== 'selected'){
            allData.result.entities[i].type = subType;
            allData.result.entities[i].mediaId = mediaId;
            allData.result.entities[i].pluginCode = null;
            allData.result.entities[i].url = url;
        }
        if (allData.result.entities[i].children.length>0) {
            for (var j=0;j<allData.result.entities[i].children.length;j++) {
                if (allData.result.entities[i].children[j].selected== 'selected'){
                    allData.result.entities[i].children[j].type = subType;
                    allData.result.entities[i].children[j].mediaId = mediaId;
                    allData.result.entities[i].children[j].pluginCode = null;
                    allData.result.entities[i].children[j].url = url;
                }
            }
        }
    }
    // return
    if($("#type_news").is(":visible")){
        $("#click-news .media_cover").hide();
        $("#click-news .msgItemA").show();
    }else {
        if($("#click-image").is(":visible")){
            $("#click-image .media_cover").hide();
            $("#click-image .msgItemA").show();
        }
        if($("#click-video").is(":visible")){
            $("#click-video .media_cover").hide();
            $("#click-video .msgItemA").show();
        }
        if($("#click-voice").is(":visible")){
            $("#click-voice .media_cover").hide();
            $("#click-voice .msgItemA").show();
        }
    }
    $("#image_pick_modal").modal("hide")
}
//点击模态框取消按钮
function clickCancel() {
    showRight($("#menuList li.selected"))
}
//点击分组选项
function clickClass(groupId,type,e) {
    currentPage = 0;
    currentGroupId = groupId;
    currentType = type;
    $(".js_groupitem").each(function () {
        $(this).removeClass("selected")
    })
    $(e).addClass("selected")
    $.ajax({
        url: '/api/wechat/material/list',
        type: 'GET',
        data: {accountId: 111, type: currentType, groupId: currentGroupId, pageIndex: currentPage, pageSize: 12},
        dataType: 'json',
        success: function (msg) {
            var subData = msg
            var detailData = subData.result
            $("#data_page_total").html(subData.page.totalCount)
            $("#image_page_count").html(subData.page.pageCount)
            $("#image_page_size").html(12)
            if (type=="news"){
                $("#type_news").show();
                $("#type_no_news").hide();
                $("#type_news").html("");
                for (var i=0;i<detailData.length;i++){
                    var liSubDom = "";
                    var liSubDomList = "";
                    for(var j=0;j<detailData[i].articles.length;j++) {
                        var clickUrl = 'http://www.baidu.com';
                        if (detailData[i].articles[j].clickUrl){
                            clickUrl = detailData[i].articles[j].clickUrl
                        }
                        if (j==0){
                            liSubDom =
                                '<div class="msgCoverImg" v-for="(article, index) in picBranch.articles" :class="{\'appMsg\':index !== 0}">' +
                                '<h4><a href="#">'+detailData[i].articles[j].title+'</a></h4>' +
                                '<div class="msgImg"  v-bind:style="{backgroundImage:\'url(\'+article.thumbUrl+\')\'}"></div>' +
                                '<a href='+clickUrl +' class="msgImgCover previewMsg"  target="view_window">' +
                                '<div class="msgCoverContent"><p>预览文章</p></div>' +
                                '</a>' +
                                '</div>'
                        }else {
                            liSubDom =
                                '<div class="msgCoverImg appMsg" v-for="(article, index) in picBranch.articles" :class="{\'appMsg\':index !== 0}">' +
                                '<h4><a href="#">'+detailData[i].articles[j].title+'</a></h4>' +
                                '<div class="msgImg"  v-bind:style="{backgroundImage:\'url(\'+article.thumbUrl+\')\'}"></div>' +
                                '<a href='+clickUrl +' class="msgImgCover previewMsg"  target="view_window">' +
                                '<div class="msgCoverContent"><p>预览文章</p></div>' +
                                '</a>' +
                                '</div>'
                        }

                        liSubDomList +=liSubDom;
                    }
                    var data=detailData[i].mediaId
                    var url=detailData[i].url
                    var liDom =
                        '<div class="msgItem" v-for="(picBranch, index) in dataShow">' +
                        ' <div class="msgContent">' +
                        '<div class="msgInfo">更新于'+detailData[i].updateTime+'</div>' + liSubDomList +
                        '<div class="msgOpr">' +
                        '<span onclick="clickChoiceNews(this,\''+data+'\',\''+url+'\')" class="glyphicon-ok-parent">选择</span>' +
                        '</div>' +
                        '</div>' +
                        '</div>'
                    $("#type_news").append(liDom)
                }
            }else {
                $("#type_news").hide();
                $("#type_no_news").show();
//            $("#type_list>li").each(function () {
//                $(this).hide()
//            })
                $("#type_list").html("");
                switch (type){
                    case "image":
                        for (var i=0;i<detailData.length;i++){
                            var data = detailData[i].mediaId;
                            var url = detailData[i].url;
                            var liDom = '<li class="img_item js_imageitem " >' +
                                '<label class="frm_checkbox_label img_item_bd">' +
                                '<div class="pic_box" style="height:120px">' +
                                '<img class="pic js_pic" src='+detailData[i].url+'>' +
                                '</div>' +
                                '<span class="lbl_content">'+detailData[i].title+'</span>' +
                                '</label>' +
                                '<div class="msgOpr">' +
                                '<span onclick="clickChoiceNotNews(this,\'image\',\''+data+'\',\''+url+'\')" class="glyphicon-ok-parent">选择</span>' +
                                '</div>' +
                                '</li>'
                            $("#type_list").append(liDom);
                        }

                        break;
                    case "voice":
                        for (var i=0;i<detailData.length;i++){
                            var data = detailData[i].mediaId;
                            var data = detailData[i].url;
                            var liDom= '<li class="img_item img_item_aduio js_imageitem" >' +
                                '<label class="frm_checkbox_label img_item_bd">' +
                                '<div class="pic_box">' +
                                '<span class="lbl_content">'+detailData[i].title+'</span>' +
                                '<span class="lbl_content">更新于'+detailData[i].updateTime+'</span>' +
                                '<audio src='+detailData[i].url+' controls="" style="width: 100%"></audio>' +
                                '</div>' +
                                '</label>' +
                                '<div class="msgOpr">' +
                                '<span onclick="clickChoiceNotNews(this,\'voice\',\''+data+'\',\''+url+'\')" class="glyphicon-ok-parent">选择</span>' +
                                '</div>' +
                                '</li>'
                            $("#type_list").append(liDom);
                        }
                        break;
                    case "video":
                        for (var i=0;i<detailData.length;i++){
                            var data = detailData[i].mediaId;
                            var data = detailData[i].url;
                            var liDom = ' <li class="img_item js_imageitem"   style="width:45% !important" >' +
                                '<label class="frm_checkbox_label img_item_bd">' +
                                '<div class="pic_box" style="height:10em !important">' +
                                '<video  class="pic js_pic" controls="" src='+detailData[i].url+' style="width:100% !important;height:auto;cursor:default"></video>' +
                                '</div>' +
                                '<span class="lbl_content">'+detailData[i].title+'</span>' +
                                '<span class="lbl_content">更新于'+detailData[i].updateTime+'</span>' +
                                '</label>' +
                                '<div class="msgOpr">' +
                                '<span onclick="clickChoiceNotNews(this,\'video\',\''+data+'\',\''+url+'\')" class="glyphicon-ok-parent">选择</span>' +
                                '</div>' +
                                '</li>'
                            $("#type_list").append(liDom);
                        }
                        break;
                }
            }
            if($("#type_list").is(":visible")){
            }
            if($("#type_news").is(":visible")){
            }
        },
        fail: function (status) {
        }
    })
}
//点击翻页
function flip(act) {
    switch (act){
        case 'prePage':
            currentPage--;
            break;
        case 'nextPage':
            currentPage++;
            break;
    }
    if(currentPage<=0 && act== 'prePage'){
        currentPage++
        return
    }
    if(currentPage>maxPage-1 && act== 'nextPage'){
        currentPage--;
        return
    }
    $("#spinner").show()
    $.ajax({
        url: '/api/wechat/material/list',
        type: 'GET',
        data: {accountId: 111, type: currentType, groupId: currentGroupId, pageIndex: currentPage, pageSize: 12},
        dataType: 'json',
        success: function (msg) {
            $("#spinner").hide()
            var subData = msg
            var detailData = subData.result
            $("#data_page_total").html(subData.page.totalCount)
            $("#image_page_count").html(subData.page.pageCount)
            $("#image_page_size").html(12)
            if (currentType=="news"){
                $("#type_news").show();
                $("#type_no_news").hide();
                $("#type_news").html("");
                for (var i=0;i<detailData.length;i++){
                    var liSubDom = "";
                    var liSubDomList = "";
                    for(var j=0;j<detailData[i].articles.length;j++) {
                        var clickUrl = 'http://www.baidu.com';
                        if (detailData[i].articles[j].clickUrl){
                            clickUrl = detailData[i].articles[j].clickUrl
                        }
                        if (j==0){
                            liSubDom =
                                '<div class="msgCoverImg" v-for="(article, index) in picBranch.articles" :class="{\'appMsg\':index !== 0}">' +
                                '<h4><a href="#">'+detailData[i].articles[j].title+'</a></h4>' +
                                '<div class="msgImg"  v-bind:style="{backgroundImage:\'url(\'+article.thumbUrl+\')\'}"></div>' +
                                '<a href='+clickUrl +' class="msgImgCover previewMsg"  target="view_window">' +
                                '<div class="msgCoverContent"><p>预览文章</p></div>' +
                                '</a>' +
                                '</div>'
                        }else {
                            liSubDom =
                                '<div class="msgCoverImg appMsg" v-for="(article, index) in picBranch.articles" :class="{\'appMsg\':index !== 0}">' +
                                '<h4><a href="#">'+detailData[i].articles[j].title+'</a></h4>' +
                                '<div class="msgImg"  v-bind:style="{backgroundImage:\'url(\'+article.thumbUrl+\')\'}"></div>' +
                                '<a href='+clickUrl +' class="msgImgCover previewMsg"  target="view_window">' +
                                '<div class="msgCoverContent"><p>预览文章</p></div>' +
                                '</a>' +
                                '</div>'
                        }

                        liSubDomList +=liSubDom;
                    }
                    var data=detailData[i].mediaId
                    var url=detailData[i].url
                    var liDom =
                        '<div class="msgItem" v-for="(picBranch, index) in dataShow">' +
                        ' <div class="msgContent">' +
                        '<div class="msgInfo">更新于'+detailData[i].updateTime+'</div>' + liSubDomList +
                        '<div class="msgOpr">' +
                        '<span onclick="clickChoiceNews(this,\''+data+'\',\''+url+'\')" class="glyphicon-ok-parent">选择</span>' +
                        '</div>' +
                        '</div>' +
                        '</div>'
                    $("#type_news").append(liDom)
                }
            }else {
                $("#type_news").hide();
                $("#type_no_news").show();
//            $("#type_list>li").each(function () {
//                $(this).hide()
//            })
                $("#type_list").html("");
                switch (currentType){
                    case "image":
                        for (var i=0;i<detailData.length;i++){
                            var data = detailData[i].mediaId;
                            var url = detailData[i].url;
                            var liDom = '<li class="img_item js_imageitem " >' +
                                '<label class="frm_checkbox_label img_item_bd">' +
                                '<div class="pic_box" style="height:120px">' +
                                '<img class="pic js_pic" src='+detailData[i].url+'>' +
                                '</div>' +
                                '<span class="lbl_content">'+detailData[i].title+'</span>' +
                                '</label>' +
                                '<div class="msgOpr">' +
                                '<span onclick="clickChoiceNotNews(this,\'image\',\''+data+'\',\''+url+'\')" class="glyphicon-ok-parent">选择</span>' +
                                '</div>' +
                                '</li>'
                            $("#type_list").append(liDom);
                        }

                        break;
                    case "voice":
                        for (var i=0;i<detailData.length;i++){
                            var data = detailData[i].mediaId;
                            var data = detailData[i].url;
                            var liDom= '<li class="img_item img_item_aduio js_imageitem" >' +
                                '<label class="frm_checkbox_label img_item_bd">' +
                                '<div class="pic_box">' +
                                '<span class="lbl_content">'+detailData[i].title+'</span>' +
                                '<span class="lbl_content">更新于'+detailData[i].updateTime+'</span>' +
                                '<audio src='+detailData[i].url+' controls="" style="width: 100%"></audio>' +
                                '</div>' +
                                '</label>' +
                                '<div class="msgOpr">' +
                                '<span onclick="clickChoiceNotNews(this,\'voice\',\''+data+'\',\''+url+'\')" class="glyphicon-ok-parent">选择</span>' +
                                '</div>' +
                                '</li>'
                            $("#type_list").append(liDom);
                        }
                        break;
                    case "video":
                        for (var i=0;i<detailData.length;i++){
                            var data = detailData[i].mediaId;
                            var data = detailData[i].url;
                            var liDom = ' <li class="img_item js_imageitem"   style="width:45% !important" >' +
                                '<label class="frm_checkbox_label img_item_bd">' +
                                '<div class="pic_box" style="height:10em !important">' +
                                '<video  class="pic js_pic" controls="" src='+detailData[i].url+' style="width:100% !important;height:auto;cursor:default"></video>' +
                                '</div>' +
                                '<span class="lbl_content">'+detailData[i].title+'</span>' +
                                '<span class="lbl_content">更新于'+detailData[i].updateTime+'</span>' +
                                '</label>' +
                                '<div class="msgOpr">' +
                                '<span onclick="clickChoiceNotNews(this,\'video\',\''+data+'\',\''+url+'\')" class="glyphicon-ok-parent">选择</span>' +
                                '</div>' +
                                '</li>'
                            $("#type_list").append(liDom);
                        }
                        break;
                }
            }
            if($("#type_list").is(":visible")){
            }
            if($("#type_news").is(":visible")){
            }
        },
        fail: function (status) {
        }
    })

}
//保存文字
function saveWords(e) {
    for (var i=0;i<allData.result.entities.length;i++){
        if (allData.result.entities[i].selected=='selected'){
            allData.result.entities[i].type = subType;
            allData.result.entities[i].url=$(e).val();
            allData.result.entities[i].pluginCode=null;
            allData.result.entities[i].key= parseInt(Date.parse(new Date())/Math.ceil(Math.random()*10));;
        } else {
            if(allData.result.entities[i].children.length>0){
                for(var j=0;j<allData.result.entities[i].children.length;j++){
                    if (allData.result.entities[i].children[j].selected == 'selected'){
                        allData.result.entities[i].children[j].type = subType;
                        allData.result.entities[i].children[j].url=$(e).val();
                        allData.result.entities[i].children[j].pluginCode=null;
                        allData.result.entities[i].children[j].key= parseInt(Date.parse(new Date())/Math.ceil(Math.random()*10));;

                    }
                }
            }

        }
    }
    menuData = allData.result.entities;
}
//删除菜单
function deleteMenu() {
    $('#deleteModal').modal('show')
}
//确定删除
function sureDelete() {
    $("#menuList").find('li.selected').remove()
    for (var i=0;i<allData.result.entities.length;i++){
        if (allData.result.entities[i].selected=='selected'){
            allData.result.entities.splice(allData.result.entities[i].sort, 1);
            for (var k = 0; k < allData.result.entities.length; k++) {
                allData.result.entities[k].sort = k
            }
        } else {
            var Record
            if(allData.result.entities[i].children.length>0){
                for(var j=0;j<allData.result.entities[i].children.length;j++){
                    if (allData.result.entities[i].children[j].selected == 'selected'){
                        Record = i
                        allData.result.entities[i].children.splice(allData.result.entities[i].children[j].sort, 1)
                        if(allData.result.entities[i].children.length>0){
                            for (var h = 0; h < allData.result.entities[i].children.length; h++) {
                                allData.result.entities[i].children[h].sort = h
                            }
                        }
                    }
                    if (Record){
                        if (allData.result.entities[Record].children.length ==0){
                            allData.result.entities[Record].type ='click';
                            allData.result.entities[Record].url = '欢迎关注本公众号';
                            allData.result.entities[Record].key = parseInt(Date.parse(new Date())/Math.ceil(Math.random()*10));
                        }
                    }
                }
            }

        }
    }
    $("#click-left").show();
    $(".panel-heading.menu-right").hide();
    $(".panel-body.menu-right").hide();
    $('#deleteModal').modal('hide')
    showLeft()
    $("#orderBt").show()
    if (allData.result.entities.length<=1){
        $("#orderBt").hide()
        if (allData.result.entities[0].children.length>1){
            $("#orderBt").show()
        }
    }
    return

}
//点击保存
function clickSave() {
    menuData = allData.result.entities
    var menuId = allData.result.id
    for (var i=0;i<menuData.length;i++){
        if (menuData[i].children.length == 0){
            switch (menuData[i].type){
                case 'click':
                    if (menuData[i].url == "" || menuData[i].url == null){
                        var str ='主菜单《'+menuData[i].name+'》，[发送消息]时文字内容不能为空!' ;
                        $("#dataNull .modal-body p").html(str);
                        $("#dataNull").modal('show');
                        return
                    }
                    break;
                case  'media_id':
                    if (menuData[i].mediaId== "" || menuData[i].mediaId == null){
                        var str ='主菜单《'+menuData[i].name+'》，[发送消息]时发送内容不能为空!' ;
                        $("#dataNull .modal-body p").html(str);
                        $("#dataNull").modal('show');
                        return
                    }
                    break;
                case 'view':
                    if (menuData[i].url== "" || menuData[i].url == null){
                        var str ='主菜单《'+menuData[i].name+'》，[跳转网页]时网址不能为空!' ;
                        $("#dataNull .modal-body p").html(str);
                        $("#dataNull").modal('show');
                        return
                    }
                    break;
                case 'miniprogram':
                    if (menuData[i].url== "" || menuData[i].url == null){
                        var str ='主菜单《'+menuData[i].name+'》，[跳转小程序]时网址不能为空!' ;
                        $("#dataNull .modal-body p").html(str);
                        $("#dataNull").modal('show');
                        return
                    }
                    break;
                case 'task':
                    if (menuData[i].pluginCode == "" || menuData[i].pluginCode == null){
                        var str ='主菜单《'+menuData[i].name+'》，[跳转任务]时数据不能为空!' ;
                        $("#dataNull .modal-body p").html(str);
                        $("#dataNull").modal('show');
                        return
                    }
                    break;
            }
        }else{
            for (var j = 0;j<menuData[i].children.length;j++){
                switch (menuData[i].children[j].type){
                    case 'click':
                        if (menuData[i].children[j].url == "" || menuData[i].children[j].url == null){
                            var str ='主菜单《'+menuData[i].name+'》的子菜单<'+menuData[i].children[j].name+'>，[发送消息]时文字内容不能为空!' ;
                            $("#dataNull .modal-body p").html(str);
                            $("#dataNull").modal('show');
                            return
                        }
                        break;
                    case  'media_id':
                        if (menuData[i].children[j].mediaId== "" || menuData[i].children[j].mediaId == null){
                            var str ='主菜单《'+menuData[i].name+'》的子菜单<'+menuData[i].children[j].name+'>，[发送消息]时发送内容不能为空!' ;
                            $("#dataNull .modal-body p").html(str);
                            $("#dataNull").modal('show');
                            return
                        }
                        break;
                    case 'view':
                        if (menuData[i].children[j].url== "" || menuData[i].children[j].url == null){
                            var str ='主菜单《'+menuData[i].name+'》的子菜单<'+menuData[i].children[j].name+'>，[跳转网页]时网址不能为空!' ;
                            $("#dataNull .modal-body p").html(str);
                            $("#dataNull").modal('show');
                            return
                        }
                        break;
                    case 'miniprogram':
                        if (menuData[i].children[j].url== "" || menuData[i].children[j].url == null){
                            var str ='主菜单《'+menuData[i].name+'》的子菜单<'+menuData[i].children[j].name+'>，[跳转小程序]时网址不能为空!' ;
                            $("#dataNull .modal-body p").html(str);
                            $("#dataNull").modal('show');
                            return
                        }
                        break;
                    case 'task':
                        if (menuData[i].children[j].pluginCode == "" || menuData[i].children[j].pluginCode == null){
                            var str ='主菜单《'+menuData[i].name+'》的子菜单<'+menuData[i].children[j].name+'>，[跳转任务]时数据不能为空!' ;
                            $("#dataNull .modal-body p").html(str);
                            $("#dataNull").modal('show');
                            return
                        }
                        break;
                }
            }
        }
    }
    var savaData = {entities:[],menuId: ''};
    savaData.entities = menuData;
    savaData.menuId = menuId;
    // savaData.menuId = menuId;
    $("#spinner h5").html('数据保存中...')
    $("#spinner").show()
    $.ajax({
        url: '/api/wechat/account/menu/save',
        type: 'POST',
        data: JSON.stringify(savaData),
        contentType:"application/json",
        dataType: 'json',
        success: function (response) {
            $("#spinner").hide()
            $("#spinner h5").html('数据加载中...')
            getData();
            $("#orderBt").show()
            $("#drag-left").hide()
            $("#click-left").show()
            $(".panel-heading.menu-right").hide();
            $(".panel-body.menu-right").hide();
        },
        fail: function (status) {
            alert('获取数据失败，状态码为' + status)
        }
    })
}
//菜单排序
function menuSort(e) {
    $(e).hide()
    $("#finishBt").show()
    $("#drag-left").show()
    $("#click-left").hide()
    $(".panel-heading.menu-right").hide();
    $(".panel-body.menu-right").hide();
    $(".icon20_common").each(function () {
        $(this).addClass("sort_gray")
    })
    $(".aMove").each(function () {
        $(this).addClass("nodisabled")
    })
    $("#menuList li").each(function () {
        $(this).addClass("disabled")
        $(this).removeClass("selected")
    })
    drag('MenuSorting', menuData)
}
//排序完成
function complete(e) {
    $(e).hide()
    $("#orderBt").show()
    $("#drag-left").hide()
    $("#click-left").show()
    $(".panel-heading.menu-right").hide();
    $(".panel-body.menu-right").hide();
    $(".icon20_common").each(function () {
        $(this).removeClass("sort_gray")
    })
    $(".aMove").each(function () {
        $(this).removeClass("nodisabled")
    })
    $("#menuList li").each(function () {
        $(this).removeClass("disabled")
        $(this).removeClass("selected")
    })
    allData.result.entities=numData();
    drag(null,null)
    clickSave();
}
///////////////////////排序的相关函数//////////////////////////////////

var dragSort=function () {
    var regId = /^#[\w\-]+$/,
        regCls = /^.[\w\-]+$/,
        p_silice = Array.prototype.slice,
        p_push = Array.prototype.push,
        markArr = function (obj) {
            return p_silice.call(obj, 0);
        };
    function MWood(selector) {
        return new MWood.prototype.init(selector);
    };

    function MWood(selector, context) {
        return new MWood.prototype.init(selector, context);
    }

    MWood.prototype = {
        init: function (selector) {
            if (regId.test(selector)) {
                this.length = 1;
                this[0] = document.getElementById(selector);
                return this;
            }
            else if (regCls.test(selector)) {
                if (document.querySelectorAll) {
                    p_push.apply(this, markArr(document.querySelectorAll(selector)));
                } else {
                    var len = documenet.getElementsByTagName('*').length,
                        clsArr = [],
                        el, i = 0,
                        selector = selector.replace(/\-/g, "\\-");
                    var oRegExp = new RegExp("(^|\\s)" + selector + "(\\s|$)");

                    for (; i < len; i++) {
                        el = els[i];
                        if (oRegExp.test(el[type])) {
                            clsArr.push(el);
                        }
                    }

                    pro_push.apply(this, markArr(clsArr));
                    return this;
                }
            }
        },
        length: 0
    }

    MWood.prototype.init.prototype = MWood.prototype;

    MWood.extend = function (obj) {
        for (var o in obj) {
            this[o] = obj[o];
        }
    }

    MWood.extend({
        addHandle: function (elm, type, fn) {
            if (elm.addEventListener) {
                elm.addEventListener(type, fn, false);
            }
            else if (el.attachEvent) {
                elm.attachEvent("on" + type, fn);
            } else {
                elm["on" + type] = fn;
            }
        },
        removeHandle: function (elm, type, fn) {
            if (elm.removeEventListener) {
                elm.removeEventListener(type, fn, false);
            }
            else if (el.detachEvent) {
                elm.detachEvent("on" + type, fn);
            } else {
                elm["on" + type] = null;
            }
        },
        getEvent: function (evnet) {
            return evnet ? evnet : window.evnet;
        },
        getTarget: function (event) {
            return event.target || event.srcElement;
        }
    });

    window.MWood = MWood;
};

var classLi;
var dragInfo = {};   //存放有关拖拽对象的一些信息
var sort_main_a;
var sort_main_b;
var sort_branch_a;
var sort_branch_b;
var jsonData=[];
function drag(b,c) {
    dragSort()
    //绑定事件
    var menuList=document.getElementById('menuList');//一级菜单栏
    if(b=='MenuSorting'){
        jsonData=JSON.parse(JSON.stringify(c))
        MWood.addHandle(document, "mousedown", handleEvent);
        MWood.addHandle(document, "mousemove", handleEvent);
        MWood.addHandle(document, "mouseup", handleEvent);
    }else {
        MWood.removeHandle(document, "mousedown", handleEvent);
        MWood.removeHandle(document, "mousemove", handleEvent);
        MWood.removeHandle(document, "mouseup", handleEvent);
    }

};
var handleEvent=function () {
    var e = MWood.getEvent(event);  //获得事件
    var target = MWood.getTarget(event); //获得事件对象
    if(target.tagName=="A"){
        target=target.parentNode;
    }
    if(target.tagName=="I"){
        target=target.parentNode.parentNode;
    }
    if(target.tagName=="SPAN"){
        target=target.parentNode.parentNode;
    }
    if($(target).hasClass("jsSubView")){
        target = target.parentNode
    }

    switch (event.type) {   //判断事件类型
        case "mousedown":
            if (target.className.indexOf("jsMenu") > -1) {
                for (var j=0;j<document.getElementsByClassName('jsMenu').length;j++){
                    if (target==document.getElementsByClassName('jsMenu')[j]){
                        sort_main_a=j;
                    }
                };

                classLi="jsMenu";

                dragInfo.dObj = target;   //存放事件对象
                var tlwh = getObjtlwh(target); //获得对象的 offsetTop,offsetLeft,offsetWidth,offsetHeight
                //计算出鼠标的坐标与对象offsetTop,offsetLeft的差值以便于鼠标移动时实时定位拖拽对象的位置
                target.x = e.clientX - tlwh[1];
                target.y = e.clientY - tlwh[0];



                //修改对象的position以便可以设置left和top进行拖拽 从这里开始此对象已经脱离文档流
                //何为文档流 当怎么样设置position或别的设置会脱离文档流请百度
                target.style.position = "absolute";

                //设置拖拽对象的left top width height
                target.style.left = (e.clientX - target.x) + "px";
                target.style.top = (e.clientY - target.y) + "px";
                target.style.width = tlwh[2] + "px";
                target.style.height = tlwh[3] + "px";

                //建立一个新对象填补被拖拽对象的位置，这样页面就还会按现在的排版，不会有任何更改。
                //如果没创建，被拖拽对象的位置就会被文档流上的其他对象占用，文章最后提供代码，可以注释掉下面4句试下两种情况
                dragInfo.vObj = document.createElement("li");
                dragInfo.vObj.className='jsMenu pre_menu_item grid_item jslevel1 ui-sortable ui-sortable-disabled vObj';
                dragInfo.vObj.style.width =tlwh[2]-1+ "px";
                dragInfo.vObj.style.height =  tlwh[3]-1+ "px";
                target.parentNode.insertBefore(dragInfo.vObj, target);
            }
            if (target.className.indexOf("jslevel2") > -1) {
                for (var j=0;j<document.getElementsByClassName('jsMenu').length;j++){
                    if (target.parentNode.parentNode.parentNode==document.getElementsByClassName('jsMenu')[j]){
                        sort_main_a=j;
                    }
                }
                for (var i=0;i<target.parentNode.getElementsByClassName('jslevel2 ').length;i++){
                    if (target==target.parentNode.parentNode.getElementsByClassName('jslevel2')[i]){
                        sort_branch_a=i;
                    }
                }
                classLi="jslevel2";

                dragInfo.dObj = target;   //存放事件对象
                var tlwh = getObjtlwh(target); //获得对象的 offsetTop,offsetLeft,offsetWidth,offsetHeight
                //计算出鼠标的坐标与对象offsetTop,offsetLeft的差值以便于鼠标移动时实时定位拖拽对象的位置
                target.x = e.clientX - tlwh[1];
                target.y = e.clientY - tlwh[0];



                //修改对象的position以便可以设置left和top进行拖拽 从这里开始此对象已经脱离文档流
                //何为文档流 当怎么样设置position或别的设置会脱离文档流请百度
                target.style.position = "absolute";

                //设置拖拽对象的left top width height
                target.style.left = (e.clientX - target.x) + "px";
                target.style.top = (e.clientY - target.y) + "px";
                target.style.width = tlwh[2] + "px";
                target.style.height = tlwh[3] + "px";

                //建立一个新对象填补被拖拽对象的位置，这样页面就还会按现在的排版，不会有任何更改。
                //如果没创建，被拖拽对象的位置就会被文档流上的其他对象占用，文章最后提供代码，可以注释掉下面4句试下两种情况
                dragInfo.vObj = document.createElement("li");
                dragInfo.vObj.className='jslevel2 current';
                dragInfo.vObj.style.width = target.style.width;
                dragInfo.vObj.style.height = target.style.height;
                target.parentNode.insertBefore(dragInfo.vObj, target);
            }
            break;
        case "mousemove":
            if (dragInfo.dObj) {
                //当鼠标移动的时候判断时候已经有对象存在（对象会在mousedown的时候存放进这个变量里）
                //设置拖拽对象的left和top改变位置，因为position已经在mousedown的时候改变为absolute了（鼠标坐标-保存的差值）
                dragInfo.dObj.style.left = (e.clientX - dragInfo.dObj.x) + "px";
                dragInfo.dObj.style.top = (e.clientY - dragInfo.dObj.y) + "px";

                //获取class为droggle的一组HTMl对象.Array.prototype.slice.call是给一个伪数组转为真正的数组用的,MWood(".droggle")就是取得class为droggle的一组对象
                var arr = Array.prototype.slice.call(MWood("."+classLi), 0);

                //循环对象
                for (var i = 0; i < arr.length; i++) {
                    if (arr[i] === dragInfo.dObj)  //过滤拖拽对象
                        continue;
                    var tlwh = getObjtlwh(arr[i]); //获得对象的 offsetTop,offsetLeft,offsetWidth,offsetHeight

                    //下面就主要是判断鼠标的位置是否为引起页面上各HTML元素在页面上位置的替换
                    //判断鼠标x > 对比对象offsetLeft && x < （对比对象的offsetWidth + 对象对象offsetLef）。 鼠标的坐标y同理鼠标坐标x的判断
                    //判断位置变换也可以按自己的标准来，我百度的时候看到例子里是这样判断我就直接像他那样写了
                    if (target.className.indexOf("jsMenu") > -1){
                        if (e.x >arr[i].getBoundingClientRect().left ) {
                            arr[i].parentNode.insertBefore(dragInfo.vObj, arr[i].nextSibling);
                            for (var j=0;j<document.getElementsByClassName('jsMenu').length;j++){
                                if (arr[i]==document.getElementsByClassName('jsMenu')[j]){
                                    sort_main_b=j;
                                }
                            };
                        }
                    }
                    if (target.className.indexOf("jslevel2") > -1){
                        if (e.y >arr[i].getBoundingClientRect().top ) {
                            arr[i].parentNode.insertBefore(dragInfo.vObj, arr[i].nextSibling);
                            for (var j=0;j<document.getElementsByClassName('jslevel2').length;j++){
                                if (arr[i]==document.getElementsByClassName('jslevel2')[j]){
                                    sort_branch_b=j;
                                }
                            };
                        }
                    }
                }
            }
            break;
        case "mouseup":
            if (dragInfo.dObj) {
                if(dragInfo.dObj.className.indexOf("jsMenu") > -1){
                    if(sort_main_b==0){
                        sort_main_b=1;
                    }
                    if (sort_main_a<sort_main_b){
                        sort_main_b=sort_main_b-1;
                    }
                    var sort_main_a_data=jsonData[sort_main_a];
                    jsonData.splice(sort_main_a,1);
                    jsonData.splice(sort_main_b,0,sort_main_a_data);
                    for(var i=0;i<jsonData.length;i++){
                        jsonData[i].sort=i;
                    }

                    for (var i=0;i<jsonData.length;i++) {
                    }
                };
                if(dragInfo.dObj.className.indexOf("jslevel2") > -1){
                    if(sort_branch_a<sort_branch_b){
                        sort_branch_b=sort_branch_b-1;
                    }
                    if(sort_branch_b==0){
                        sort_branch_b=1;
                    }

                    var sort_branch_a_data=jsonData[sort_main_a].children[sort_branch_a];
                    jsonData[sort_main_a].children.splice(sort_branch_a,1);//[1,2]
                    jsonData[sort_main_a].children.splice(sort_branch_b,0,sort_branch_a_data);
                    for(var i=0;i<jsonData[sort_main_a].children.length;i++){
                        jsonData[sort_main_a].children[i].sort=i;
                    }


                }
                //把拖拽对象重新弄进文档流（static就是默认的值），替换临时占位的对象，并初始化dragInfo；
                dragInfo.dObj.style.position = "relative";
                dragInfo.dObj.style.left =null;
                dragInfo.dObj.style.top =null;
                dragInfo.dObj.style.width =null;
                dragInfo.dObj.style.height =null;
                dragInfo.vObj.parentNode.insertBefore(dragInfo.dObj, dragInfo.vObj);
                dragInfo.dObj.parentNode.removeChild(dragInfo.vObj);
                dragInfo = {};
                var vObj=document.getElementsByClassName('vObj');
                if(vObj.length>0){
                    for (var i=0;i<vObj.length;i++){
                        vObj[i].parentNode.removeChild(vObj[i])
                    }
                }
                numData(sort_main_a,sort_branch_b)
            }
            break;
    }
};
var deepCopy= function(source) {
    var result={};
    for (var key in source) {
        result[key] = typeof source[key]==='object'? deepCoyp(source[key]): source[key];
    }
    return result;
}
function getObjtlwh(o) {
    var arr = [];
    arr[0] = o.offsetTop;
    arr[1] = o.offsetLeft;
    arr[2] = o.offsetWidth;
    arr[3] = o.offsetHeight;
    return arr
}
function numData(){
    return jsonData
}



