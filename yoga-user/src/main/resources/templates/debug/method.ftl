<#import "/macro_paging.ftl" as html>
<#import "/macro_common.ftl" as macroCommon>
<#import "/macro_operate.ftl" as operate>
<@macroCommon.html>
<style>
    pre {
        outline: 0px solid #eee;
        padding: 5px;
        margin: 5px;
    }

    .string {
        color: green;
    }

    .number {
        color: darkorange;
    }

    .boolean {
        color: blue;
    }

    .null {
        color: magenta;
    }

    .key {
        color: red;
    }

    .table > thead > tr > td {
        vertical-align: middle;
    }
</style>
<body>
<div class="wrapper">
    <div class="container-fluid">
        <div class="row content-bottom">
            <div class="col-lg-12">
                <div class="panel panel-primary">
                    <div class="panel-body">
                        <div style="margin: 2px;">
                            <#if group??>
                                <span><i class="icon icon-dashboard"></i>&nbsp;${group} / </span>
                            </#if>
                            <span>${method?if_exists.explain?if_exists}</span>
                        </div>
                        <div class="input-group">
                            <div class="input-group-btn">
                                <span class="btn btn-default">POST
                                </span>
                            </div>
                            <input id="url" type="text" style="margin-right: 10px" class="form-control"
                                   value="${method?if_exists.url?if_exists}">
                            <span class="input-group-btn" style="margin-left: 10px">
                                <button class="btn btn-default" type="button" onclick="doSubmit()">调用</button>
                            </span>
                        </div>
                        <div class="input-group">
                            <div class="input-group-btn">
                                <span class="btn btn-default">Token
                                </span>
                            </div>
                            <input id="token" type="text" style="margin-right: 10px" class="form-control">
                        </div>
                        <div style="margin-top: 20px">
                            <table class="table table-bordered table-striped">
                                <thead>
                                <tr>
                                    <th style="width:10%">Key</th>
                                    <th style="width:20%">Value</th>
                                    <th style="width:10%">类型</th>
                                    <th style="width:20%">解释</th>
                                    <th><span style="padding-top:14px; position:absolute">约束</span>
                                        <button style="float:right;" class="btn btn-primary" type="button"
                                                onclick="doAdd()"><i class="icon icon-plus"></i>
                                        </button>
                                    </th>
                                </tr>
                                </thead>
                                <tbody id="tbody">
                                    <#list method?if_exists.parameters?if_exists as parameter>
                                    <tr>
                                        <#if parameter.requestBody>
                                            <td colspan="5">
                                                <textarea name="request_body" class="form-control" id="request_body"
                                                          style="min-height: 300px">${parameter.type?if_exists}</textarea>
                                            </td>
                                        <#else>
                                            <td>
                                            ${parameter.name?if_exists}
                                                <input name="key" type="hidden" value="${parameter.name?if_exists}">
                                            </td>
                                            <td>
                                                <#if parameter.values??>
                                                    <select name="value" class="form-control" >
                                                        <option value=""></option>
                                                        <#list parameter.values as value>
                                                            <option value="${value}">${value}</option>
                                                        </#list>
                                                    </select>
                                                <#else>
                                                    <input name="value" type="text" class="form-control" placeholder="${parameter.hint?if_exists}">
                                                </#if>
                                            </td>
                                            <td>${parameter.type?if_exists}</td>
                                            <td>${parameter.explain?if_exists}</td>
                                            <td>
                                                <ul>
                                                    <#list parameter.constraints?if_exists as constraint>
                                                        <li>${constraint?if_exists}</li>
                                                    </#list>
                                                </ul>
                                            </td>
                                        </#if>
                                    </tr>
                                    </#list>
                                </tbody>
                            </table>
                        </div>
                        <pre id="result_pre" class="prettyprint prettyprinted">
                        </pre>
                    </div>
                    <div class="box-footer" style="text-align:center">
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>
</body>
<script>
    function syntaxHighlight(json) {
        if (typeof json != 'string') {
            json = JSON.stringify(json, undefined, 2);
        }
        json = json.replace(/&/g, '&').replace(/</g, '<').replace(/>/g, '>');
        return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
            var cls = 'number';
            if (/^"/.test(match)) {
                if (/:$/.test(match)) {
                    cls = 'key';
                } else {
                    cls = 'string';
                }
            } else if (/true|false/.test(match)) {
                cls = 'boolean';
            } else if (/null/.test(match)) {
                cls = 'null';
            }
            return '<span class="' + cls + '">' + match + '</span>';
        });
    }

    function getCookie(c_name) {
        if (document.cookie.length > 0) {
            c_start = document.cookie.indexOf(c_name + "=");
            if (c_start != -1) {
                c_start = c_start + c_name.length + 1;
                c_end = document.cookie.indexOf(";", c_start);
                if (c_end == -1) c_end = document.cookie.length;
                return unescape(document.cookie.substring(c_start, c_end));
            }
        }
        return "";
    }
    $(document).ready(function() {
        $("#token").val(getCookie("def_token"));
    });
    function setCookie(c_name, value, expiredays) {
        var exdate = new Date();
        exdate.setDate(exdate.getDate() + expiredays);
        document.cookie = c_name + "=" + escape(value) + ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString());
    }
    function doSubmit() {
        var url = $("#url").val();
        var token = $("#token").val();
        var parameters = $("#tbody").find("tr");
        var query = {};
        var body = "";
        if ($("#request_body").length > 0) {
            body = $("#request_body").val();
        }
        parameters.each(function (i) {
            var key = $(this).find("input[name='key']").val();
            var value = $(this).find("[name='value']").val();
            if (value != "")query[key] = value;
        });
        if (token != "") {
            $.ajaxSetup({
                headers: { 'token': token }
            });
        }
        if (body == "") {
            $.post(
                    url,
                    query,
                    function (data) {
                        $('#result_pre').html(syntaxHighlight(data));
                        window.parent.window.setIframeHeight();
                        setCookie("def_token", token);
                    },
                    "json"
            );
        } else {
            $.ajax({
                url: url,
                type: 'POST',
                data: body,
                dataType: 'json',
                contentType: "application/json; charset=utf-8",
                success: function (data) {
                    $('#result_pre').html(syntaxHighlight(data));
                    window.parent.window.setIframeHeight();
                    setCookie("def_token", token);
                }
            });
        }
    }
    function doAdd() {
        $("tr").last().after("<tr>" +
                "<td><input name='key' type='text' class='form-control'></td>" +
                "<td><input name='value' type='text' class='form-control'></td>" +
                "<td></td>" +
                "<td>自定义</td>" +
                "<td></td>" +
                "</tr>");
    }
</script>
</@macroCommon.html>
