<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/input.component.ftl">
<!DOCTYPE html>
<html>
<@macroCommon.html>
<link href="<@macroCommon.resource/>/plugins/datetimepicker/jquery.datetimepicker.css" rel="stylesheet" media="screen">
<script type="text/javascript" src="<@macroCommon.resource/>/plugins/datetimepicker/jquery.datetimepicker.js"></script>
<body>
<div class="container-fluid">
    <div class="row">
        <ol class="breadcrumb">
            <li><i class="icon icon-dashboard"></i>租户续费记录</li>
        </ol>
    </div>
    <div class="row content-bottom">
        <div class="col-sm-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <i class="fa fa-comments-o"></i>
                    租户续费记录
                </div>
                <div class="panel-body">
                    <div class="tableToolContainer" style="margin-bottom:15px">
                        <form action="/tenant/recharge" class="form-inline">
                            <div class="form-group">
                                <label class="exampleInputAccount4">&nbsp;&nbsp;租户名称：&nbsp;</label>
                                <input type="text" class="form-control" name="tenantName" id="tenantName" value="${param.tenantName?if_exists}">
                            </div>
                            &nbsp;&nbsp;
                            <button class="btn btn-success" type="submit">
                                <i class="fa fa-fw fa-search"></i>搜索
                            </button>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                        <@shiro.hasPermission name="gbl_tenant_recharge.add" >
                            <a style="float: right" href="#" class="btn btn-primary" id="rechargeAddButton">
                                <i class="fa fa-fw fa-plus"></i>添加
                            </a>
                        </@shiro.hasPermission>
                        </form>
                    </div>
                    <table id="" class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th style="width:10%">租户名称</th>
                            <th style="width:10%">续费时间</th>
                            <th style="width:10%">订单金额</th>
                            <th style="width:10%">甲方代表</th>
                            <th style="width:10%">乙方代表</th>
                            <th style="width:10%">续费到期日期</th>
                            <th style="width:10%">是否出票</th>
                            <th style="width:20%">发票编号</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list recharges as recharge>
                        <tr>
                            <td>${recharge.tenantName?if_exists}</td>
                            <td>${(recharge.time?string("yyyy-MM-dd HH:mm:ss"))!}</td>
                            <td>${recharge.amount?if_exists}</td>
                            <td>${recharge.refereeA?if_exists}</td>
                            <td>${recharge.refereeB?if_exists}</td>
                            <td>${(recharge.expireDate?string("yyyy-MM-dd"))!}</td>
                            <td>
                                <#if !recharge.invoiced>
                                    <@shiro.hasPermission name="gbl_tenant_recharge.add" >
                                        <a href="javascript:void(0)" onclick="doInvoiced(${recharge.id?default(0)?c})"
                                           class="btn btn-sm btn-info"><i class="icon icon-edit"></i>出票</a>
                                    </@shiro.hasPermission>
                                <#else>
                                ${recharge.invoiced?string("是", "否")}
                                </#if>
                            </td>
                            <td>${recharge.invoiceNo?if_exists}</td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
                <div class="box-footer" style="text-align:center">
                <@html.paging page=page param=param action="/tenant/recharge"/>
                </div>
            </div>
        </div>
    </div>
</div>
<footer class="main-footer">
<@common.footer />
</footer>
</div>
</body>


<@operate.add "续费" "#rechargeAddButton" "addRecharge">
    <@formList name="tenantId" label="租户" options=tenants />
    <@formText name="refereeA" label="甲方代表" />
    <@formText name="refereeB" label="乙方代表" />
    <@formText name="amount" label="续费金额" />
    <@formDate name="expireDate" label="过期日期" />
    <@formCheckbox name="invoiced" label="已开票" text="已开票"/>
    <@formText name="invoiceNo" label="发票编号"/>
    <@formText name="orderNo" label="订单编号" />
    <@formText name="tradeNo" label="交易流水号" />
    <@formTextArea name="remark" label="备注" />
</@operate.add>
<script>
    function addRecharge() {
        var json = $("#operate_add_form").serializeArray();
        $.post("/api/tenant/recharge/add",
                json,
                function (data) {
                    if (data.code < 0) {
                        alertShow("warning", data.message, 3000);
                        return;
                    } else {
                        window.location.reload();
                    }
                },
                "json"
        );
    }
</script>

    <@operate.modal "已出票" "invoiced" "onInvoiced">
        <input type="hidden" id="invoiced_id" name="id">
        <@formText name="invoiceNo" label="发票编号"/>
        <@formText name="orderNo" label="订单编号" />
        <@formText name="tradeNo" label="交易流水号" />
        <@formTextArea name="remark" label="备注" />
    </@operate.modal>
<script>
    function doInvoiced(id) {
        $("#invoiced_id").val(id);
        $("#invoiced").modal("show");
    }
    function onInvoiced() {
        var json = $("#invoiced_form").serializeArray();
        $.post("/api/tenant/recharge/invoiced",
                json,
                function (data) {
                    if (data.code < 0) {
                        alertShow("warning", data.message, 3000);
                        return;
                    } else {
                        window.location.reload();
                    }
                },
                "json"
        );
    }
</script>

</@macroCommon.html>