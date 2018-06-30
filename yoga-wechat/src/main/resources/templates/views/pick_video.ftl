
<div id="video_pick_modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" style="width:50%;min-height: 50%">
        <form class="form-horizontal" id="material_upload_modal_form">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title">
                        选择视频
                    </h4>
                </div>
                <div class="dialog_bd" style="min-height: 420px">
                    <input type="hidden" id="video_group_id"  value="">
                    <div id="video_liset" class="picBox_reply">

                    </div>
                    <hr>
                    <div class="page" style="margin-left: 20%">
                        <a class="btn btn-xs btn-default" href="javascript:void(0);" onclick="pickVideoPage(parseInt($('#video_page_index').val()) - 1)">上一页</a>
                        <a class="btn btn-xs btn-default" href="javascript:void(0);" onclick="pickVideoPage(parseInt($('#video_page_index').val()) + 1)">下一页</a>
                        找到<span style='color: #FD7B02;' id="video_page_total">1</span>条数据，共<span style='color: #FD7B02;' id="video_page_count">1</span>页，每页 <span id="video_page_size">1</span> 条数据
                        &nbsp;到第
                        <input style="text-align:center;width:3em;" class="w20 mar0 alignCenter" id="video_page_index" onblur="pickVideoPage(parseInt(this.value)-1)" maxlength="5" type="text" value="1"style="width:40px"> 页
                    </div>
                </div>
                <div class="modal-footer">
                    <a href="javascript:void(0)" class="btn btn-danger" data-dismiss="modal"><i
                            class="fa fa-fw fa-remove"></i></button>取消</a>
                    <a href="javascript:void(0)" id="group_set_modal_submit" class="btn btn-info"
                       onclick="doVideoPicked();"><i
                            class="fa fa-fw  fa-save"></i>确定</a>
                </div>
            </div>
        </form>
    </div>
</div>
<script>
    var picker_current_video = null;
    var afterPickVideo = null;
    $("#video_pick_modal").on("hidden.bs.modal", function () {
        if (picker_current_video) picker_current_video.pause();
    });
    function setVideoAction() {
        $("video").each(function () {
            this.addEventListener('play', function () {
                if (picker_current_video) picker_current_video.pause();
                picker_current_video = this;
            });
        });
    }
    function showVideoPicker(after) {
        afterPickVideo = after;
        $("#video_pick_modal").modal("show");
        $("#video_pick_modal").css("z-index",'9999');
        pickVideoPage(1);
    }
    function pickVideoPage(index) {
        $("#video_page_index").val(index);
        loadVideo();
    }
    function hideVideoPicker() {
        $("#video_pick_modal").modal("hide");
    }
    function loadVideo() {
        var groupId = $("#video_group_id").val();
        var pageIndex = $("#video_page_index").val()-1;
        $.post(
                "/api/wechat/material/list",
                {
                    accountId: ${param.accountId?if_exists},
                    type: "video",
                    groupId: groupId,
                    pageIndex: pageIndex
                },
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#video_liset").children().remove();
                        var videos = data.result;
                        for (var i = 0; i < videos.length; i++) {
                            var video = videos[i];
                            $("#video_liset").append(
                                    '<div  class="videoItems_reply">'+
                                    '<div class="picCenter">' +
                                    '<label class="checkbox-inline" style="overflow: hidden;text-overflow: ellipsis; width: 100%; height: 30px">'+
                                    '<input type="radio" data-name="' + video.name + '" class="picCheckBox" name="video_choiced" data-url="' + video.url + '" value="'+video.mediaId+'">'+
//                                    '<input type="radio" data-name="' + video.name + '" class="picCheckBox" name="video_choiced" data-url="' + video.url + '" value="'+video.mediaId+'">'+
//                                    '<span class="picNameBox" style="margin-top: 7px;color: black">'+video.name+'</span>'+
                                    '<span>'+video.name+'</span>'+
                                    '</label>'+
                                    '<div class="videoPlayer"><video class="vidoeMian" controls src="'+video.url+'" ></video></div>'+
                                    '</div>'+
                                    '</div>'
                            );
                        }
                        setVideoAction();
                        var page = data.page;
                        $("#video_page_total").html(page.totalCount);
                        $("#video_page_count").html(page.pageCount);
                        $("#video_page_size").html(page.pageSize);
                        $("#video_page_index").val(page.pageIndex + 1);
                    }
                },
                "json"
        );
    }
    function doVideoPicked() {
        if ($("input[name='video_choiced']:checked").length < 1) {
            alertShow("warning", "请选择一个视频！", 1000);
            return;
        }
        var choiced = $("input[name='video_choiced']:checked:first");
        var name = choiced.attr("data-name");
        var url = choiced.attr("data-url");
        if (afterPickVideo != null) afterPickVideo(choiced.val(), name, url);
    }
</script>