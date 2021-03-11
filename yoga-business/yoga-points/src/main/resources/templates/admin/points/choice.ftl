<@operate.modal "请选择" "leave_users_modal" "afterChoice">
<div class="modalForm">
    <div class="form-group">
        <label class="col-sm-2  control-label">姓名:</label>
        <div class="col-sm-3">
            <input type="text" class="form-control" id="leave_users_search_name">
            <span class="help-block"></span>
        </div>
        <label class="col-sm-2  control-label">部门:</label>
        <div class="col-sm-3">
            <select class="form-control" id="leave_users_search_deptId">
                <option value="">全部部门</option>
                <#list depts as root>
                    <@m1_columns root 0 root_index/>
                </#list>
            </select>
            <span class="help-block"></span>
        </div>
        <div class="col-sm-2">
            <button type="button" class="btn btn-sm btn-success" onclick="usersSearch();">
                <i class="icon icon-search"></i>搜索
            </button>
        </div>
    </div>
</div>
<div class="modalForm">
    <div class="form-group">
        <div class=" col-sm-6" style="width: 100%">
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th class="tableCenter" style="width:20%"></th>
                    <th class="tableCenter" style="width:40%">姓名</th>
                    <th class="tableCenter" style="width:40%">部门</th>
                </tr>
                </thead>
                <tbody id="leave_users_user_list">
                <tr>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <hr>
    <div class="page" style="margin-left: 20%">
        <a class="btn btn-xs btn-default" href="javascript:void(0);" onclick="pickerPageChanged(parseInt($('#picker_page_index').val()) - 1)">上一页</a>
        <a class="btn btn-xs btn-default" href="javascript:void(0);" onclick="pickerPageChanged(parseInt($('#picker_page_index').val()) + 1)">下一页</a>
        找到<span style='color: #FD7B02;' id="picker_page_total">1</span>条数据，共<span style='color: #FD7B02;' id="picker_page_count">1</span>页，每页 <span id="picker_page_size">1</span> 条数据
        &nbsp;到第
        <input style="text-align:center;width:3em;" class="w20 mar0 alignCenter" id="picker_page_index" onblur="pickerPageChanged(parseInt(this.value))" maxlength="5" type="text" value="1"style="width:40px"> 页
    </div>
</div>
<input type="hidden" id="context_id">
</@operate.modal>
<script>
    var doChoiced = null;
    function usersSearch() {
        pickerLoadUser();
    }
    function afterChoice() {
        if (doChoiced != null) {
            doChoiced();
        }
    }

    function pickerLoadUser() {
        var name = $("#leave_users_search_name").val();
        var deptId = $("#leave_users_search_deptId").val();
        var pageIndex = $("#picker_page_index").val() - 1;
        $.post(
                "/api/user/list",
                {
                    name: name, 
                    deptId: deptId,
                    pageIndex: pageIndex
                },
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        var bady = $("#leave_users_user_list");
                        bady.children().remove();
                        $.each(data.result, function (i, user) {
                            bady.append(
                                    "<tr >" +
                                    "<td class='tableCenter'>" +
                                    "<input type='radio' name='users_user'' value='" + user.id + "' fullname='" + user.fullname + "'>" +
                                    "</td>" +
                                    "<td class='tableCenter'>" + user.fullname + "</td>" +
                                    "<td class='tableCenter'>" + user.department + "</td>" +
                                    "</tr>");
                        });
                        var page = data.page;
                        $("#picker_page_total").html(page.totalCount);
                        $("#picker_page_count").html(page.pageCount);
                        $("#picker_page_size").html(page.pageSize);
                        $("#picker_page_index").val(page.pageIndex + 1);
                    }
                },
                "json"
        );
    }
    function pickerPageChanged(index) {
        $("#picker_page_index").val(index);
        pickerLoadUser();
    }
</script>