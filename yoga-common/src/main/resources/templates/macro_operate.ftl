<#macro add name buttonid func idPrefix="operate_add">
<script>
    jQuery(document).ready(function () {
        $("${buttonid}").on("click", function () {
            $("#${idPrefix}_form")[0].reset();
            $("#${idPrefix}_modal").modal("show");
        });
        $("#${idPrefix}_submit").on("click", function () {
            ${func}();
        });
        $("#${idPrefix}_modal").on("hidden.bs.modal", function () {
            $("#${idPrefix}_form")[0].reset();
        });
    });
</script>
<div class="modal fade" id="${idPrefix}_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width:50%">
        <form class="form-horizontal" id="${idPrefix}_form">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="${idPrefix}_header">
                    ${name}
                    </h4>
                </div>
                <div class="modal-body">
                    <#nested>
                </div>
                <div class="modal-footer">
                    <a href="#" class="btn btn-danger" data-dismiss="modal">
                        <i class="fa fa-fw fa-remove"></i>取消
                    </a>
                    <a type="summit" class="btn btn-info" id="${idPrefix}_submit">
                        <i class="fa fa-fw  fa-save"></i>保存
                    </a>
                </div>
            </div>
        </form>
    </div>
</div>
</#macro>

<#macro modal name id func>
<div class="modal fade" id="${id}" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width:50%">
        <form class="form-horizontal" id="${id}_form">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="${id}_header">
                    ${name}
                    </h4>
                </div>
                <div class="modal-body">
                    <#nested>
                </div>
                <div class="modal-footer">
                    <a href="javascript:void(0)" class="btn btn-danger" data-dismiss="modal"><i
                            class="fa fa-fw fa-remove"></i></button>取消</a>
                    <a href="javascript:void(0)" onclick="${func}()" id="edit_submit_button" class="btn btn-info"><i
                            class="fa fa-fw  fa-save"></i>保存</a>
                </div>
            </div>
        </form>
    </div>
</div>
</#macro>

<#macro editview name>
<div class="modal fade" id="operate_edit_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width:50%">
        <form class="form-horizontal">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="operate_edit_header">
                    ${name}
                    </h4>
                </div>
                <div class="modal-body">
                    <#nested>
                </div>
                <div class="modal-footer">
                    <a href="javascript:void(0)" class="btn btn-danger" data-dismiss="modal"><i
                            class="fa fa-fw fa-remove"></i></button>取消</a>
                    <a href="javascript:void(0)" id="edit_submit_button" class="btn btn-info"><i
                            class="fa fa-fw  fa-save"></i>保存</a>
                </div>
            </div>
        </form>
    </div>
</div>
</#macro>
<#macro editjs func>
<script>
    function doEdit(elem, id){
        $("#edit_submit_button").attr("onclick", "${func}('" + id + "')");
        $("#operate_edit_modal").modal("show");
        <#nested>
    }
</script>
</#macro>
