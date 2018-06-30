<div id="image_pick_modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" style="width:50%;min-height: 50%">
        <form class="form-horizontal" id="material_upload_modal_form">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title">
                        选择素材
                    </h4>
                </div>
                <div class="dialog_bd">
                    <div class="img_pick_panel inner_container_box side_l cell_layout">
                        <div class="inner_side" style="border-right:1px solid #e7e7eb">
                            <div class="group_list">
                                <div class="inner_menu_box">
                                    <dl class="inner_menu js_group">
                                        <dd id="js_group0" class="inner_menu_item js_groupitem selected">
                                            <a href="javascript:;" class="inner_menu_link" onclick="changeImageGroup(this, '')">
                                                <strong>全部图片</strong>
                                            </a>
                                        </dd>
                                        <dd id="js_group0" class="inner_menu_item js_groupitem">
                                            <a href="javascript:;" class="inner_menu_link" onclick="changeImageGroup(this, 0)">
                                                <strong>未分组</strong>
                                            </a>
                                        </dd>
                                        <#list groups as group>
                                            <dd id="js_group0" class="inner_menu_item js_groupitem">
                                                <a href="javascript:;" class="inner_menu_link" onclick="changeImageGroup(this, ${group.id})">
                                                    <strong>${group.name}</strong>
                                                </a>
                                            </dd>
                                        </#list>
                                    </dl>
                                </div>
                            </div>
                        </div>
                        <div class="inner_main" style="margin-left: 10px">
                            <input type="hidden" id="image_group_id" value="">
                            <div class="img_pick_area">
                                <div class="img_pick_area_inner">
                                    <div class="img_pick">
                                        <i class="icon_loading_small white js_loading" style="display: none;"></i>
                                        <ul class="group js_list img_list" id="image_list">

                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <hr>
                            <div class="page" style="margin-left: 20%">
                                <a class="btn btn-xs btn-default" href="javascript:void(0);" onclick="pickImagePage(parseInt($('#image_page_index').val()) - 1)">上一页</a>
                                <a class="btn btn-xs btn-default" href="javascript:void(0);" onclick="pickImagePage(parseInt($('#image_page_index').val()) + 1)">下一页</a>
                                找到<span style='color: #FD7B02;' id="image_page_total">1</span>条数据，共<span style='color: #FD7B02;' id="image_page_count">1</span>页，每页 <span id="image_page_size">1</span> 条数据
                                &nbsp;到第
                                <input style="text-align:center;width:3em;" class="w20 mar0 alignCenter" id="image_page_index" onblur="pickImagePage(parseInt(this.value)-1)" maxlength="5" type="text" value="1"style="width:40px"> 页
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <a href="javascript:void(0)" class="btn btn-danger" data-dismiss="modal"><i
                            class="fa fa-fw fa-remove"></i></button>取消</a>
                    <a href="javascript:void(0)" id="group_set_modal_submit" class="btn btn-info"
                       onclick="doImagePicked();"><i
                            class="fa fa-fw  fa-save"></i>确定</a>
                </div>
            </div>
        </form>
    </div>
</div>
<script>
    var afterPickImage = null;
    $("#checkImg").hide();
    $(".img_item").click(function () {
        $("#checkImg").slideToggle();
    });
    function doImagePicked() {
        if ($("input[name='image_choiced']:checked").length < 1) {
            alertShow("warning", "请选择一张图片！", 1000);
            return;
        }
        var choiced = $("input[name='image_choiced']:checked:first");
        var name = choiced.attr("data-name");
        var url = choiced.attr("data-url");
        if (afterPickImage != null) afterPickImage(choiced.val(), name, url);
    }
    function showImagePicker(after) {
        afterPickImage = after;
        $("#image_pick_modal").modal("show");
        $("#image_pick_modal").css("z-index",'9999');
        pickImagePage(1);
    }
    function hideImagePicker() {
        $("#image_pick_modal").modal("hide");
    }
    function pickImagePage(index) {
        $("#image_page_index").val(index);
        loadImage();
    }
    function changeImageGroup(elem, id) {
        $(".js_groupitem").each(function () {
            $(this).removeClass('selected');
        });
        if (elem != null) {
            $(elem).parent().addClass('selected');
        }
        $("#image_page_index").val(1);
        $("#image_group_id").val(id);
        loadImage();
    }
    function loadImage() {
        var groupId = $("#image_group_id").val();
        var pageIndex = $("#image_page_index").val() - 1;

        $.post(
                "/api/wechat/material/list",
                {
                    accountId: ${param.accountId?if_exists},
                    type: "image",
                    groupId: groupId,
                    pageIndex: pageIndex
                },
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#image_list").children().remove();
                        var images = data.result;
                        for (var i = 0; i < images.length; i++) {
                            var image = images[i];
                            $("#image_list").append(
                                    '<li class="img_item js_imageitem">' +
                                        '<label class="frm_checkbox_label img_item_bd">' +
                                            '<div class="pic_box">' +
                                                '<img class="pic js_pic" src="' + image.url + '">' +
                                                '<input type="radio" data-name="' + image.name + '" name="image_choiced" data-url="' + image.url + '" value="' + image.mediaId + '">' +
                                            '</div>' +
                                            '<span class="lbl_content">' + image.name + '</input>' +
                                        '</label>' +
                                    '</li>'
                            );
                        }
                        var page = data.page;
                        $("#image_page_total").html(page.totalCount);
                        $("#image_page_count").html(page.pageCount);
                        $("#image_page_size").html(page.pageSize);
                        $("#image_page_index").val(page.pageIndex + 1);
                    }
                },
                "json"
        );
    }
</script>
