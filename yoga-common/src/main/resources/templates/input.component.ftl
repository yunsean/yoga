<#macro maskinput targetId maskType>
<script type="text/javascript">
    $(document).ready(function () {
        if ("mail" == "${maskType}") {
            $("#${targetId}").inputmask({
                mask: "*{1,20}[.*{1,20}][.*{1,20}][.*{1,20}]@*{1,20}[.*{2,6}][.*{1,2}]",
                greedy: false,
                onBeforePaste: function (pastedValue, opts) {
                    pastedValue = pastedValue.toLowerCase();
                    return pastedValue.replace("mailto:", "");
                },
                definitions: {
                    '*': {
                        validator: "[0-9A-Za-z!#$%&'*+/=?^_`{|}~\-]",
                        cardinality: 1,
                        casing: "lower"
                    }
                }
            });
        } else if ("date" == "${maskType}") {
            $("#${targetId}").inputmask({"mask": "y-m-d"});
        } else if ("time" == "${maskType}") {
            $("#${targetId}").inputmask({"mask": "y-m-d h:s"});
        } else if ("number" == "${maskType}") {
            $("#${targetId}").inputmask({"mask": "9{1,99}"});
        } else if ("decimal" == "${maskType}") {
            $("#${targetId}").inputmask({"mask": "9{0,999}.99", "placeholder": "0"});
        } else if ("varchar" == "${maskType}") {
            $("#${targetId}").inputmask({"mask": "a{1,999}"});
        } else if ("ip" == "${maskType}") {
            $("#${targetId}").inputmask({"mask": "9{1,3}.9{1,3}.9{1,3}.9{1,3}", "placeholder": "0"});
        }
    });
</script>
</#macro>

<#function randomInputId>
    <#if !code??>
        <#assign code=1>
    </#if>
    <#assign code = code + 1>
    <#assign id = "random_id_${code}" >
    <#return id>
</#function>

<#--功能:文本框-->
<#macro inputText id="" name="" class="" placeholder="" value="" mask="" ext="" readonly=false>
    <#local id=(id=="")?string("${randomInputId()}", id)>
    <input type="text" class="form-control ${class}" <#if name != "">name="${name}"</#if> <#if id != "">id="${id}"</#if> <#if placeholder != "">placeholder="${placeholder}"</#if> <#if value != "">value="${value}"</#if> <@formParams ext/> <#if readonly>readonly</#if> />
    <#if mask != "">
        <@maskinput id mask />
    </#if>
</#macro>
<#--功能：表单中的文本框-->
<#macro formText label id="" name="" class="" placeholder="" value="" icon="" mask="" divId="" ext="" postfix="" readonly=false>
<div class="form-group" <#if divId!="">id="${divId}"</#if>>
    <label class="col-sm-3 control-label">${label}</label>
    <div class="col-sm-8">
        <#if icon!="">
            <div class="input-group">
                <@inputText id, name, class, placeholder, value, mask, ext, readonly />
                <span class="input-group-addon ">
                    <i class="icon-append ${icon}"></i>
                </span>
            </div>
        <#else>
            <@inputText id, name, class, placeholder, value, mask, ext, readonly />
        </#if>
    </div>
    <#if postfix != "">
    <label class="col-sm-1" style="text-align: left">${postfix}</label>
    </#if>
</div>
</#macro>

<#macro inputTextArea id="" name="" class="" placeholder="" value="" ext="" readonly=false>
    <#local id=(id=="")?string("${randomInputId()}", id)>
    <textarea class="form-control ${class}" <#if name != "">name="${name}"</#if> <#if id != "">id="${id}"</#if> <@formParams ext/> <#if readonly>readonly</#if> <#if placeholder != "">placeholder="${placeholder}"</#if>><#if value != "">${value}</#if></textarea>
</#macro>
<#macro formTextArea label id="" name="" class="" placeholder="" value="" divId="" ext="" postfix="" readonly=false>
<div class="form-group" <#if divId!="">id="${divId}"</#if>>
    <label class="col-sm-3 control-label">${label}</label>
    <div class="col-sm-8">
        <@inputTextArea id name class placeholder value ext readonly />
    </div>
</div>
</#macro>


<#--功能:文本框-->
<#macro inputShow id="" name="" class="" value="" ext="">
    <#local id=(id=="")?string("${randomInputId()}", id)>
<input readonly style="background: #fff" type="text" class="form-control ${class}" <#if name != "">name="${name}"</#if>
       <#if id != "">id="${id}"</#if> <#if value != "">value="${value}"</#if> <@formParams ext/> />
</#macro>
<#--功能：表单中的文本框-->
<#macro formShow label id="" name="" class="" value="" icon="" divId="" ext="">
<div class="form-group" <#if divId!="">id="${divId}"</#if>>
    <label class="col-sm-3 control-label">${label}</label>
    <div class="col-sm-8">
        <#if icon!="">
            <div class="input-group">
                <@inputShow id, name, class, value, ext />
                <span class="input-group-addon ">
                    <i class="icon-append ${icon}"></i>
                </span>
            </div>
        <#else>
            <@inputShow id, name, class, value, ext />
        </#if>
    </div>
</div>
</#macro>

<#--功能：隐藏文本框-->
<#macro inputHidden id="" name="" value="" ext="">
<input type="hidden" <#if name != "">name="${name}"</#if> <#if id != "">id="${id}"</#if>
       <#if value != "">value="${value}"</#if> <@formParams ext/>/>
</#macro>

<#--功能：密码框-->
<#macro inputPassword id="" name="" placeholder="" value="" ext="">
    <#local id=(id=="")?string("${randomInputId()}", id)>
    <input class="form-control" type="password" <#if name != "">name="${name}"</#if> <#if id != "">id="${id}"</#if>
       <#if placeholder != "">placeholder="${placeholder}"</#if>
       <#if value != "">value="${value}"</#if> <@formParams ext/>/>
</#macro>
<#--功能：表单中的密码框-->
<#macro formPassword label id="" name="" placeholder="" value="" icon="" divId="" ext="">
<div class="form-group" <#if divId!="">id="${divId}"</#if>>
    <label class="col-sm-3 control-label">${label}</label>
    <div class="col-sm-8">
        <#if icon!="">
            <div class="input-group">
                <@inputPassword id name placeholder value ext />
                <span class="input-group-addon ">
                    <i class="icon-append ${icon}"></i>
                </span>
            </div>
        <#else>
            <@inputPassword id name placeholder value ext />
        </#if>
    </div>
</div>
</#macro>

<#--功能:日期选择框（格式：yyyy-MM-dd）-->
<#macro inputDate class="" id="" name="" placeholder="" value="" ext="" force=false>
    <#local id=(id=="")?string("${randomInputId()}", id)>
    <input type="text" class="form-control ${class}" <#if name != "">name="${name}"</#if> <#if id != "">id="${id}"</#if>
       <#if placeholder != "">placeholder="${placeholder}"</#if>
       <#if value != "">value="${value}"</#if> <@formParams ext/>/>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#${id}").datetimepicker({
                lang: 'ch',
                timepicker: false,
                format: 'Y-m-d',
                formatDate: 'Y-m-d',
                allowBlank: ${force?string("false", "true")}
            });
        });
    </script>
</#macro>
<#--功能:表单中的日期选择框（格式：yyyy-MM-dd）-->
<#macro formDate label class="" id="" name="" placeholder="" value="" icon="" divId="" ext="" force=false>
<div class="form-group" <#if divId!="">id="${divId}"</#if>>
    <label class="col-sm-3 control-label">${label}</label>
    <div class="col-sm-8">
        <#if icon!="">
            <div class="input-group">
                <@inputDate class id name placeholder value ext force />
                <span class="input-group-addon ">
                    <i class="icon-append ${icon}"></i>
                </span>
            </div>
        <#else>
            <@inputDate class id name placeholder value ext force />
        </#if>
    </div>
</div>
</#macro>

<#--功能:时间选择框（格式：HH:MM）-->
<#macro inputTime class="" id="" name="" placeholder="" value="" ext="" force=false>
    <#local id=(id=="")?string("${randomInputId()}", id)>
    <input type="text" class="form-control ${class}" <#if name != "">name="${name}"</#if> <#if id != "">id="${id}"</#if>
       <#if placeholder != "">placeholder="${placeholder}"</#if>
       <#if value != "">value="${value}"</#if> <@formParams ext/>/>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#${id}").datetimepicker({
                lang: 'ch',
                step: 30,
                timepicker: true,
                datepicker: false,
                format: 'H:i',
                formatTime: 'H:i',
                allowBlank: ${force?string("false", "true")}
            });
        });
    </script>
</#macro>
<#--功能：表单中的时间选择框（格式：HH:MM）-->
<#macro formTime label class="" id="" name="" placeholder="" value="" icon="" divId="" ext="" force=false>
<div class="form-group" <#if divId!="">id="${divId}"</#if>>
    <label class="col-sm-3 control-label">${label}</label>
    <div class="col-sm-8">
        <#if icon!="">
            <div class="input-group">
                <@inputTime class id name placeholder value ext force />
                <span class="input-group-addon ">
                    <i class="icon-append ${icon}"></i>
                </span>
            </div>
        <#else>
            <@inputTime class id name placeholder value ext force />
        </#if>
    </div>
</div>
</#macro>

<#--功能:日期时间选择框（格式：yyyy-MM-dd hh:mm）-->
<#macro inputDateTime class="" id="" name="" placeholder="" value="" ext="" force=false>
    <#local id=(id=="")?string("${randomInputId()}", id)>
    <input type="text" class="form-control ${class}" <#if name != "">name="${name}"</#if> <#if id != "">id="${id}"</#if>
       <#if placeholder != "">placeholder="${placeholder}"</#if>
       <#if value != "">value="${value}"</#if> <@formParams ext/>/>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#${id}").datetimepicker({
                lang: 'ch',
                step: 30,
                timepicker: true,
                datepicker: true,
                format: 'Y-m-d H:i',
                formatTime: 'H:i',
                formatDate: 'Y-m-d',
                allowBlank: ${force?string("false", "true")}
            });
        });
    </script>
</#macro>
<#--功能:表单中的日期时间选择框（格式：yyyy-MM-dd hh:mm）-->
<#macro formDateTime label class="" id="" name="" placeholder="" value="" icon="" divId="" ext="" force=false>
<div class="form-group" <#if divId!="">id="${divId}"</#if>>
    <label class="col-sm-3 control-label">${label}</label>
    <div class="col-sm-8">
        <#if icon!="">
            <div class="input-group">
                <@inputDateTime class id name placeholder value ext force />
                <span class="input-group-addon ">
                    <i class="icon-append ${icon}"></i>
                </span>
            </div>
        <#else>
            <@inputDateTime class id name placeholder value ext force />
        </#if>
    </div>
</div>
</#macro>

<#macro showTreeNode column level index>
<option value="${column.id?if_exists?c}">
    <#list 0..level as x>
        <#if x < level>
            │&nbsp;&nbsp;
        <#else>
            ├
        </#if>
    </#list>
${column.name?if_exists}
</option>
</#macro>
<#macro showTree columns level index>
    <@showTreeNode columns level index/>
    <#local level1 = level + 1/>
    <#if columns.children??>
        <#list columns.children as sub>
            <@showTree sub level1 sub_index/>
        </#list>
    </#if>
</#macro>

<#--功能：树形输入选择控件-->
<#--black value="" 一般为全部-->
<#--zero value="0" 一般为未定义-->
<#macro inputTree options class="" id="" name="" value="" ext="" blank="" zero="" blankValue="">
    <#local id=(id=="")?string("${randomInputId()}", id)>
    <select class="form-control ${class}" <#if name != "">name="${name}"</#if>
            <#if id != "">id="${id}"</#if> <@formParams ext/>/>
        <#if blank != ""><option value="${blankValue}">${blank}</option></#if>
        <#if zero != ""><option value="0">${zero}</option></#if>
        <#list options as option>
            <@showTree option 0 option_index/>
        </#list>
    </select>
    <script>
        $(document).ready(function () {
            $("#${id}").val("${value}");
        });
    </script>
</#macro>
<#--功能：表单中的树形输入选择控件-->
<#macro formTree label options class="" id="" name="" value="" icon ="" divId="" ext="" blank="" zero="" blankValue="">
<div class="form-group" <#if divId!="">id="${divId}"</#if>>
    <label class="col-sm-3 control-label">${label}</label>
    <div class="col-sm-8">
        <#if icon!="">
            <div class="input-group">
                <@inputTree options class id name value ext blank zero blankValue/>
                <span class="input-group-addon ">
                    <i class="icon-append ${icon}"></i>
                </span>
            </div>
        <#else>
            <@inputTree options class id name value ext blank zero blankValue/>
        </#if>
    </div>
</div>
</#macro>


<#--功能：枚举选择控件-->
<#--black value="" 一般为全部-->
<#macro inputEnum enums class="" id="" name="" value="" ext="" blank="" blankValue="">
    <#local id=(id=="")?string("${randomInputId()}", id)>
<select class="form-control ${class}" <#if name != "">name="${name}"</#if>
        <#if id != "">id="${id}"</#if> <@formParams ext/>/>
    <#if blank != ""><option value="${blankValue}">${blank}</option></#if>
    <#list enums as option>
    <option value="${option}">${option.getName()}</option>
    </#list>
</select>
    <script>
        $(document).ready(function () {
            $("#${id}").val("${value}");
        });
    </script>
</#macro>
<#--功能：表单中的树形输入选择控件-->
<#macro formEnum label enums class="" id="" name="" value="" icon ="" divId="" ext="" blank="" blankValue="">
<div class="form-group" <#if divId!="">id="${divId}"</#if>>
    <label class="col-sm-3 control-label">${label}</label>
    <div class="col-sm-8">
        <#if icon!="">
            <div class="input-group">
                <@inputEnum enums class id name value ext blank blankValue/>
                <span class="input-group-addon ">
                    <i class="icon-append ${icon}"></i>
                </span>
            </div>
        <#else>
            <@inputEnum enums class id name value ext blank blankValue/>
        </#if>
    </div>
</div>
</#macro>

<#--功能：列表选择控件 options是个数组，每个元素必须存在id和name两个成员-->
<#--black value="" 一般为全部-->
<#macro inputList options class="" id="" name="" value="" ext="" blank="" blankValue="">
    <#local id=(id=="")?string("${randomInputId()}", id)>
<select class="form-control ${class}" <#if name != "">name="${name}"</#if>
        <#if id != "">id="${id}"</#if> <@formParams ext/>/>
    <#if blank != ""><option value="${blankValue}">${blank}</option></#if>
    <#list options as option>
    <option value="${option.id}">${option.name}</option>
    </#list>
</select>
<script>
    $(document).ready(function () {
        $("#${id}").val("${value}");
    });
</script>
</#macro>
<#--功能：表单中的树形输入选择控件-->
<#macro formList label options class="" id="" name="" value="" icon ="" divId="" ext="" blank="" blankValue="">
<div class="form-group" <#if divId!="">id="${divId}"</#if>>
    <label class="col-sm-3 control-label">${label}</label>
    <div class="col-sm-8">
        <#if icon!="">
            <div class="input-group">
                <@inputList options class id name value ext blank blankValue/>
                <span class="input-group-addon ">
                    <i class="icon-append ${icon}"></i>
                </span>
            </div>
        <#else>
            <@inputList options class id name value ext blank blankValue/>
        </#if>
    </div>
</div>
</#macro>

<#--功能：复选框-->
<#macro inputCheckbox text class="" id="" name="" checked="">
    <label class="checkbox-inline">
        <input name="${name}" <#if id != "">id="${id}"</#if> type="checkbox" class="margin-r-5 ${class}" <#if checked != "">checked=""${checked}</#if> >${text}
    </label>
</#macro>
<#--功能：表单中的复选框控件-->
<#macro formCheckbox label text="" class="" id="" name="" icon ="" divId="" ext="" checked="">
<div class="form-group" <#if divId!="">id="${divId}"</#if>>
    <label class="col-sm-3 control-label">${label}</label>
    <div class="col-sm-8">
        <#if icon!="">
            <div class="input-group">
                <@inputCheckbox text class id name checked/>
                <span class="input-group-addon ">
                    <i class="icon-append ${icon}"></i>
                </span>
            </div>
        <#else>
            <@inputCheckbox text class id name checked/>
        </#if>
    </div>
</div>
</#macro>

<#--功能：复选框组-->
<#macro inputCheckboxGroup options class="" name="">
    <#list options as option>
    <label class="checkbox-inline">
        <input name="${name}" type="checkbox" value="${option.id?c}" class="margin-r-5 ${class}">${option.name}
    </label>
    </#list>
</#macro>

<#--功能：单选框组-->
<#macro inputRadioGroup options class="" name="">
    <#list options as option>
    <label class="checkbox-inline">
        <input name="${name}" type="radio" value="${option.id?c}" class="margin-r-5 ${class}"> ${option.name}
    </label>
    </#list>
</#macro>

<#--功能：下拉列表-->
<#macro inputDropdownGroup options class="" id="" name="" blank="" zero="" value="" blankValue="">
    <#local id=(id=="")?string("${randomInputId()}", id)>
    <select class="form-control ${class}" name="${name}" id="${id}">
        <#if blank != ""><option value="${blankValue}">${blank}</option></#if>
        <#if zero != ""><option value="0">${zero}</option></#if>
        <#list options as option>
        <option value="${option.id?c}">${option.name?if_exists}</option>
        </#list>
    </select>
        <script>
            $(document).ready(function () {
                $("#${id}").val("${value}");
            });
        </script>
</#macro>

<#macro inputOption value name>
<option value="${value}">${name}</option>
</#macro>

<#macro inputDropdown class="" id="" name="" blank="" zero="" value="" blankValue="">
    <#local id=(id=="")?string("${randomInputId()}", id)>
    <select class="form-control ${class}" name="${name}" id="${id}">
    <#if blank != ""><option value="${blankValue}">${blank}</option></#if>
    <#if zero != ""><option value="0">${zero}</option></#if>
    <#nested >
    </select>
    <script>
        $(document).ready(function () {
            $("#${id}").val("${value}");
        });
    </script>
</#macro>

<#--功能:表单提交按钮-->
<#macro inputSubmit class="" id="" name="" text="" icon="">
<button type="submit" id="${id}" name="${name}" class="btn btn-default btn-primary ${class}">
    <i class="icon ${icon}"></i>${text}
</button>
</#macro>


<#--功能：常规按钮-->
<#macro inputButton class="btn-primary" id="" name="" text="" icon="" href="#" onclick="">
<a href="${href}" class="btn ${class}" id="${id}" name="${name}" <#if onclick != "">onclick="${onclick}"</#if>>
    <#if icon!=""><i class="icon ${icon}"></i></#if>${text}
</a>
</#macro>