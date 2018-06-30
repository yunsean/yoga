<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<@html>
    <@head includeDate=true>
    <style>
        .tl {
            text-align: right;
        }

        .img {
            width: 64px;
        }

        .cc {
            text-align: center
        }

        /*居中显示容器*/
        .ci {
            margin: 0 auto;
            border: 1px solid #F00
        }

        /*居中显示元素*/
    </style>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="顾问管理" icon="icon-user">
            <@crumbItem href="#" name="顾问审核" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "待审核顾问列表" />
                <@panelBody>
                    <@table>
                    <colgroup width="15%"></colgroup>
                    <colgroup width="35%"></colgroup>
                    <colgroup width="15%"></colgroup>
                    <colgroup width="35%"></colgroup>
                    <tbody>
                    <tr>
                        <td class="tableCenter" colspan="4">
                            <div><#if counselor.avatar??><img onclick="showImg(this)" style="width: 120px"
                                                              src="${counselor.avatar?if_exists}"></#if></div>
                            <label style="display: block;">${counselor.fullname?if_exists}</label>
                        </td>
                    </tr>
                    <tr>
                        <td class="tl">姓名：</td>
                        <td>${counselor.name?if_exists}</td>
                        <td class="tl">顾问类型：</td>
                        <td>${department.name?if_exists}</td>
                    </tr>
                    <tr>
                        <td class="tl">注册时间：</td>
                        <td>${counselor.createTime?string("yyyy-MM-dd")}</td>
                        <td class="tl">到期时间：</td>
                        <td><#if counselor.expire??>${counselor.expire?string('yyyy-MM-dd')}<#else>未付费</#if></td>
                    </tr>
                    <tr>
                        <td class="tl">身份证号：</td>
                        <td>${counselor.pid?if_exists}</td>
                        <td class="tl">审核状态：</td>
                        <td>${counselor.status.getName()}</td>
                    </tr>
                    <tr>
                        <td class="tl">民族：</td>
                        <td>${counselor.nation?if_exists}</td>
                        <td class="tl">出生日期：</td>
                        <td><#if counselor.birthday??>${counselor.birthday?string("yyyy-MM-dd")}</#if></td>
                    </tr>
                    <tr>
                        <td class="tl">性别：</td>
                        <td>${counselor.pidGender?if_exists}</td>
                        <td class="tl">地址：</td>
                        <td>${counselor.address?if_exists}</td>
                    </tr>
                    <tr>
                        <td class="tl">就职公司：</td>
                        <td>${counselor.company?if_exists}</td>
                        <td class="tl">职称：</td>
                        <td>${counselor.title?if_exists}</td>
                    </tr>
                    <tr>
                        <td class="tl">电子邮箱：</td>
                        <td>${counselor.email?if_exists}</td>
                        <td class="tl">QQ：</td>
                        <td>${counselor.qq?if_exists}</td>
                    </tr>
                    <tr>
                        <td class="tl">微信号：</td>
                        <td>${counselor.wechat?if_exists}</td>
                        <td class="tl">手机号：</td>
                        <td>${counselor.phone?if_exists}</td>
                    </tr>
                    <tr>
                        <td class="tl">身份证正面图：</td>
                        <td><#if counselor.pidFront??><img class="img" onclick="showImg(this)"
                                                           src="${counselor.pidFront?if_exists}"></#if></td>
                        <td class="tl">身份证背面图：</td>
                        <td><#if counselor.pidBack??><img class="img" onclick="showImg(this)"
                                                          src="${counselor.pidBack?if_exists}"></#if></td>
                    </tr>
                    <tr>
                        <td class="tl">其他图片：</td>
                        <td colspan="3">
                            <#if counselor.pidFront??>
                                <#list counselor.getImagesList() as image>
                                    <img class="img" onclick="showImg(this)" src="${image}">
                                </#list>
                            </#if>
                        </td>
                    </tr>
                    </tbody>
                    </@table>
                <div class="cc">
                    <#if counselor.status?if_exists == 'checking'>
                        <@shiro.hasPermission name="pri_user.update" >
                            <button class="btn btn-primary" onclick="doAccept()">审核通过</button>
                            <button class="btn btn-danger" onclick="doReject()">审核拒绝</button>
                        </@shiro.hasPermission>
                    </#if>
                    <button class="btn btn-default" onclick="javascript:history.back(-1)" style="margin-left: 50px">关闭
                    </button>
                </div>
                </@panelBody>
            </@panel>
        </@bodyContent>
    </@bodyFrame>

    <@modal title="详情" idPrefix="img">
        <@formGroup class="cc">
        <img src="${counselor.pidFront?if_exists}" id="big_image" class="ci">
        </@formGroup>
    </div>
    </@modal>
    <@modal title="审核拒绝" idPrefix="reject" onOk="reject">
        <@formText label="拒绝理由：" id="reject_reason" placeholder="请填写拒绝理由，方便顾问修改！"/>
    </div>
    </@modal>
<script>
    function showImg(elem) {
        $("#big_image").attr("src", $(elem).attr("src"));
        $("#img_modal").modal("show");
    }

    function doAccept() {
        warningModal("确定要通过顾问审核吗?", function () {
            $.get(
                    "/api/ewedding/counselor/verify",
                    {
                        id: ${param.id?c},
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
    function doReject() {
        $("#reject_modal").modal("show");
    }
    function reject() {
        var reason = $("#reject_reason").val();
        $.get(
                "/api/ewedding/counselor/verify",
                {
                    id: ${param.id?c},
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
