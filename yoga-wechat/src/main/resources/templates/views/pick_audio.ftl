

<div id="voice_pick_modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" style="width:50%;min-height: 50%">
        <form class="form-horizontal" id="material_upload_modal_form">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title">
                        选择音频
                    </h4>
                </div>
                <div class="dialog_bd" style="min-height: 420px">
                    <input type="hidden" id="voice_group_id" value="">
                    <table class="table table-bordered table-hover">
                        <thead>
                        <tr>
                            <th style="width:70%">名称</th>
                            <th class="tableCenter">试听播放</th>
                        </tr>
                        </thead>
                        <tbody id="voice_liset">
                        </tbody>
                    </table>
                    <hr>
                    <div class="page" style="margin-left: 20%">
                        <a class="btn btn-xs btn-default" href="javascript:void(0);" onclick="pickVoicePage(parseInt($('#voice_page_index').val()) - 1)">上一页</a>
                        <a class="btn btn-xs btn-default" href="javascript:void(0);" onclick="pickVoicePage(parseInt($('#voice_page_index').val()) + 1)">下一页</a>
                        找到<span style='color: #FD7B02;' id="voice_page_total">1</span>条数据，共<span style='color: #FD7B02;' id="voice_page_count">1</span>页，每页 <span id="voice_page_size">1</span> 条数据
                        &nbsp;到第
                        <input style="text-align:center;width:3em;" class="w20 mar0 alignCenter" id="voice_page_index" onblur="pickVoicePage(parseInt(this.value)-1)" maxlength="5" type="text" value="1"style="width:40px"> 页
                    </div>
                </div>
                <div class="modal-footer">
                    <a href="javascript:void(0)" class="btn btn-danger" data-dismiss="modal"><i
                            class="fa fa-fw fa-remove"></i></button>取消</a>
                    <a href="javascript:void(0)" id="group_set_modal_submit" class="btn btn-info"
                       onclick="doVoicePicked();"><i
                            class="fa fa-fw  fa-save"></i>确定</a>
                </div>
            </div>
        </form>
    </div>
</div>
<script>
    var picker_current_Voice = null;
    var afterPickVoice = null;
    $("#video_pick_modal").on("hidden.bs.modal", function () {
        if (picker_current_Voice) picker_current_Voice.pause();
    });
    function setVoiceAction() {
        $("audio").each(function () {
            this.addEventListener('play', function () {
                $(this).parent().children()[1].style.display = "none";
                $(this).parent().children()[2].style.display = "block";
                if (picker_current_Voice) picker_current_Voice.pause();
                picker_current_Voice = this;
            });
            this.addEventListener('ended', function () {
                $(this).parent().children()[1].style.display = "block";
                $(this).parent().children()[2].style.display = "none";
            });
            this.addEventListener('pause', function () {
                $(this).parent().children()[1].style.display = "block";
                $(this).parent().children()[2].style.display = "none";
            });
        });
    }
    function showVoicePicker(after) {
        afterPickVoice = after;
        $("#voice_pick_modal").modal("show");
        $("#voice_pick_modal").css("z-index",'9999');
        pickVoicePage(1);
    }
    function hideVoicePicker() {
        $("#voice_pick_modal").modal("hide");
    }
    function pickVoicePage(index) {
        $("#voice_page_index").val(index);
        loadVoice();
    }
    function hideVideoPicker() {
        $("#video_pick_modal").modal("hide");
    }
    function loadVoice() {
        var groupId = $("#voice_group_id").val();
        var pageIndex = $("#voice_page_index").val()-1;
        $.post(
                "/api/wechat/material/list",
                {
                    accountId: ${param.accountId?if_exists},
                    type: "voice",
                    groupId: groupId,
                    pageIndex: pageIndex
                },
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#voice_liset").children().remove();
                        var voices = data.result;
                        for (var i = 0; i < voices.length; i++) {
                            var voice = voices[i];
                            $("#voice_liset").append(
                                    '<tr>'+
                                    '<td><div class="radio"> <label> <input type="radio" data-name="' + voice.name + '" name="voice_choiced" data-url="' + voice.url + '" value="' + voice.mediaId + '"> '+voice.name+' </label> </div></td>'+
                                    '<td class="tableCenter">' +
                                    '<div class="audio_replay" id="player_'+voice.id+'">' +
                                    '<audio src="'+voice.url+'"></audio>' +
                                    '<div class="audioDefault_reply" style="background-size: 22px 18px" onclick="playAudio(this)"></div>' +
                                    '<div class="audioPlaying_reply" style="background-size: 50px" onclick="playAudio(this)"></div>' +
                                    '</div>' +
                                    '</td>'+
                                    '</tr>'
                            );
                        }
                        setVoiceAction();
                        var page = data.page;
                        $("#voice_page_total").html(page.totalCount);
                        $("#voice_page_count").html(page.pageCount);
                        $("#voice_page_size").html(page.pageSize);
                        $("#voice_page_index").val(page.pageIndex + 1);
                    }
                },
                "json"
        );
    }
    function doVoicePicked() {
        if ($("input[name='voice_choiced']:checked").length < 1) {
            alertShow("warning", "请选择一个音频！", 1000);
            return;
        }
        var choiced = $("input[name='voice_choiced']:checked:first");
        var name = choiced.attr("data-name");
        var url = choiced.attr("data-url");
        if (afterPickVoice != null) afterPickVoice(choiced.val(), name, url);
    }
    function playAudio(elem){
        var parent = $(elem).parent();
        var player = parent.children()[0];
        if (player.paused){
            player.load();
            player.play();
        } else {
            player.pause();
        }
    }
</script>
