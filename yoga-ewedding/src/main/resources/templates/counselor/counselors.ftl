<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<@html>
<@head includeDate=true>
<style>
    td {
        vertical-align: middle !important;;
    }
    .mr20 {
        margin-right: 30px;
    }
</style>
</@head>
<@bodyFrame>
    <@crumbRoot name="顾问管理" icon="icon-user">
        <@crumbItem href="#" name=checking?if_exists?string("顾问审核", "顾问列表") />
    </@crumbRoot>
    <@bodyContent>
        <@panel>
            <@panelHeading "待审核顾问列表" />
            <@panelBody>
                <@inlineForm class="margin-b-15">
                    <@formLabelGroup class="margin-r-15" label="用户名">
                        <@inputText name="name" value="${param.name?if_exists}"/>
                    </@formLabelGroup>
                    <@formLabelGroup class="margin-r-15" label="所属公司">
                        <@inputText name="company" value="${param.company?if_exists}"/>
                    </@formLabelGroup>
                    <@formLabelGroup class="margin-r-15" label="审核状态">
                        <@inputDropdown name="status" value="${param.status?if_exists}" blank=checking?if_exists?string("", "全部")>
                            <@inputOption value="filling" name="资料待完善" />
                            <@inputOption value="checking" name="待审核" />
                        <#if !checking?if_exists>
                            <@inputOption value="rejected" name="被拒绝" />
                            <@inputOption value="accepted" name="已通过" />
                        </#if>
                        </@inputDropdown>
                    </@formLabelGroup>
                    <@inputSubmit text="搜索" icon="icon icon-search" class="btn btn-success"/>
                </@inlineForm>
                <@table>
                    <@thead>
                    <@tr>
                        <@th 7>姓名</@th>
                        <@th 8>类型</@th>
                        <@th 7>手机号</@th>
                        <@th 8>状态</@th>
                        <@th 10>所属公司</@th>
                        <@th 7>性别</@th>
                        <@th 7>生日</@th>
                        <@th 10>邮箱</@th>
                        <@th 10>到期时间</@th>
                        <@th center=true>操作</@th>
                    </@tr>
                    </@thead>
                    <tbody>
                        <#list counselors as user>
                        <@tr>
                            <td>${user.lastname?if_exists}${user.firstname?if_exists}</td>
                            <td><#if user.deptId?exists && deptMap?exists && deptMap[user.deptId?c]?exists>
                                    ${deptMap[user.deptId?c].name!}
                                </#if>
                            </td>
                            <td>${user.username?if_exists}</td>
                            <td>${user.status.getName()}</td>
                            <td>${user.company?if_exists}</td>
                            <td>${user.pidGender?if_exists}</td>
                            <td><#if user.birthday??>${user.birthday?string('yyyy-MM-dd')}</#if></td>
                            <td>${user.email?if_exists}</td>
                            <td><#if user.expire??>${user.expire?string('yyyy-MM-dd')}<#else>未付费</#if></td>
                            <td class="tableCenter">
                                <a href="/ewedding/counselor/detail?id=${user.id?default(0)?c}" class="btn btn-primary mr20">
                                    <i class="icon icon-edit"></i>详情
                                </a>
                                <#if user.status == 'checking'>
                                    <@shiro.hasPermission name="pri_user.update" >
                                        <a href="javascript:void(0)" onclick="doAccept(${user.id?default(0)?c})"
                                           class="btn btn-info"><i class="icon icon-edit"></i>通过</a>
                                        <a href="javascript:void(0)" onclick="doReject(${user.id?default(0)?c})"
                                           class="btn btn-danger"><i class="icon icon-edit"></i>拒绝</a>
                                    </@shiro.hasPermission>
                                </#if>
                            </td>
                        </@tr>
                        </#list>
                    </tbody>
                </@table>
            </@panelBody>
            <@panelPageFooter action=checking?if_exists?string("/ewedding/counselor/checking", "/ewedding/counselor/list") />
        </@panel>
    </@bodyContent>
</@bodyFrame>


    <@modal title="详情">
        <@inputHidden name="id" id="edit_id"/>
        <@formShow label="姓名：" name="name" value="ddddd"/>
        <@formShow label="身份证号：" name="number"/>
        <@formShow label="民族：" name="nation"/>
        <@formShow label="家庭地址：" name="address"/>
        <@formShow label="性别：" name="gender"/>
        <@formShow label="生日：" name="birthday"/>
    </div>
    </@modal>

    <@modal title="审核拒绝" idPrefix="reject" onOk="reject">
        <@inputHidden name="id" id="reject_id"/>
        <@formText label="拒绝理由：" id="reject_reason" placeholder="请填写拒绝理由，方便顾问修改！"/>
    </div>
    </@modal>
<script>
    function doDetail(id) {
        $("#add_form")[0].reset();
        $.get(
                "/api/ewedding/counselor/info?id=" + id,
                function (data) {
                    if (data.code < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        $("#add_form input[name='id']").val(id);
                        $("#add_form input[name='name']").val(data.result.name);
                        $("#add_form input[name='number']").val(data.result.pid);
                        $("#add_form input[name='nation']").val(data.result.nation);
                        $("#add_form input[name='address']").val(data.result.address);
                        $("#add_form input[name='gender']").val(data.result.pidGender);
                        $("#add_form input[name='birthday']").val(data.result.birthday);
                        $("#add_modal").modal("show");
                    }
                }
        );
    }
    function doAccept(id) {
        warningModal("确定要通过顾问审核吗?", function () {
            $.get(
                    "/api/ewedding/counselor/verify",
                    {
                        id: id,
                        rejected: false
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
    function doReject(id) {
        $("#reject_id").val(id);
        $("#reject_modal").modal("show");
    }
    function reject() {
        var id = $("#reject_id").val();
        var reason = $("#reject_reason").val();
        $.get(
                "/api/ewedding/counselor/verify",
                {
                    id: id,
                    rejected: true,
                    reason: reason
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
</script>
</@html>
