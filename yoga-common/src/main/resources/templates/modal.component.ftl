<#macro modal title showId="" onOk="" idPrefix="add" autoReset=true>
<script>
    jQuery(document).ready(function () {
        <#if showId!="">
            $("#${showId}").on("click", function () {
                <#if autoReset>
                    $("#${idPrefix}_form")[0].reset();
                </#if>
                $("#${idPrefix}_modal").modal("show");
            });
        </#if>
        <#if onOk!= "">
            $("#${idPrefix}_submit").on("click", function () {
                ${onOk}();
            });
        </#if>
        <#if autoReset>
            $("#${idPrefix}_modal").on("hidden.bs.modal", function () {
                $("#${idPrefix}_form")[0].reset();
            });
        </#if>
    });
</script>
<div class="modal fade" id="${idPrefix}_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" style="width:50%">
        <form class="form-horizontal" id="${idPrefix}_form">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="${idPrefix}_header">
                    ${title}
                    </h4>
                </div>
                <div class="modal-body">
                    <#nested>
                </div>
                <div class="modal-footer">
                    <a href="#" class="btn btn-danger" data-dismiss="modal">
                        <i class="fa fa-fw fa-remove"></i>取消
                    </a>
                    <#if onOk != "">
                    <a type="summit" class="btn btn-info" id="${idPrefix}_submit">
                        <i class="fa fa-fw  fa-save"></i>保存
                    </a>
                    </#if>
                </div>
            </div>
        </form>
    </div>
</div>
</#macro>