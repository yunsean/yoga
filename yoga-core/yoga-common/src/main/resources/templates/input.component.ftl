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
<#macro inputText id="" name="" class="" placeholder="" value="" mask="" ext="" valueregex="" readonly=false>
    <#local id=(id=="")?string("${randomInputId()}", id)>
    <input type="text" class="form-control ${class}" <#if name != "">name="${name}"</#if>  <#if valueregex != "">oninput="value=value.replace(${valueregex}, '')"</#if> <#if id != "">id="${id}"</#if> <#if placeholder != "">placeholder="${placeholder}"</#if> <#if value != "">value="${value}"</#if> <@formParams ext/> <#if readonly>readonly</#if> />
    <#if mask != "">
        <@maskinput id mask />
    </#if>
</#macro>
<#--功能：表单中的文本框-->
<#macro formText label id="" name="" class="" placeholder="" value="" icon="" mask="" divId="" ext="" postfix="" valueregex="" readonly=false>
<div class="form-group" <#if divId!="">id="${divId}"</#if>>
    <label class="col-sm-3 control-label">${label}</label>
    <div class="col-sm-8">
        <#if icon!="">
            <div class="input-group">
                <@inputText id, name, class, placeholder, value, mask, ext, valueregex, readonly />
                <span class="input-group-addon ">
                    <i class="icon-append ${icon}"></i>
                </span>
            </div>
        <#else>
            <@inputText id, name, class, placeholder, value, mask, ext, valueregex, readonly />
        </#if>
    </div>
    <#if postfix != "">
    <label class="col-sm-1" style="text-align: left">${postfix}</label>
    </#if>
</div>
</#macro>

<#macro divName label="" class="" id="">
    <div class="${class}" style="width: 100%; text-align: center" <#if id!="">id="${id}"</#if>>${label}</div>
</#macro>
<#macro formName label="" class="" id="">
    <div class="form-group">
        <label class="${class}" style="width: 100%; text-align: center" <#if id!="">id="${id}"</#if>>${label}</label>
    </div>
</#macro>


<#--功能:文本框-->
<#macro inputNumber id="" name="" min="" max="" step="" class="" placeholder="" value="" mask="" ext="" readonly=false>
    <#local id=(id=="")?string("${randomInputId()}", id)>
    <input type="number" class="form-control ${class}"<#if name != ""> name="${name}"</#if><#if id != ""> id="${id}"</#if><#if placeholder != ""> placeholder="${placeholder}"</#if><#if min != ""> min="${min}"</#if><#if max != ""> max="${max}"</#if><#if step != ""> step="${step}"</#if> value="${value?c}" <@formParams ext/> <#if readonly>readonly</#if> />
    <#if mask != "">
        <@maskinput id mask />
    </#if>
</#macro>
<#--功能：表单中的文本框-->
<#macro formNumber label id="" name="" min="" max="" step="" class="" placeholder="" value="" icon="" mask="" divId="" ext="" postfix="" readonly=false>
    <div class="form-group" <#if divId!="">id="${divId}"</#if>>
        <label class="col-sm-3 control-label">${label}</label>
        <div class="col-sm-8">
            <#if icon!="">
                <div class="input-group">
                    <@inputNumber id, name, min, max, step, class, placeholder, value, mask, ext, readonly />
                    <span class="input-group-addon ">
                    <i class="icon-append ${icon}"></i>
                </span>
                </div>
            <#else>
                <@inputNumber id, name, class, placeholder, value, mask, ext, readonly />
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
<#macro inputPassword class="" id="" name="" placeholder="" value="" ext="">
    <#local id=(id=="")?string("${randomInputId()}", id)>
    <input class="form-control ${class}" type="password" <#if name != "">name="${name}"</#if> <#if id != "">id="${id}"</#if>
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
<#macro inputDate class="" id="" name="" placeholder="" value="" ext="" force=false readonly=false>
    <#local id=(id=="")?string("${randomInputId()}", id)>
    <input type="text" class="form-control ${class}" <#if name != "">name="${name}"</#if> <#if id != "">id="${id}"</#if>
       <#if placeholder != "">placeholder="${placeholder}"</#if> <#if readonly>readonly</#if>
       <#if value != "">value="${value}"</#if> <@formParams ext/> autocomplete="off"/>
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
<#macro formDate label class="" id="" name="" placeholder="" value="" icon="" divId="" ext="" force=false readonly=false>
<div class="form-group" <#if divId!="">id="${divId}"</#if>>
    <label class="col-sm-3 control-label">${label}</label>
    <div class="col-sm-8">
        <#if icon!="">
            <div class="input-group">
                <@inputDate class id name placeholder value ext force readonly/>
                <span class="input-group-addon ">
                    <i class="icon-append ${icon}"></i>
                </span>
            </div>
        <#else>
            <@inputDate class id name placeholder value ext force readonly/>
        </#if>
    </div>
</div>
</#macro>

<#--功能:时间选择框（格式：HH:MM）-->
<#macro inputTime class="" id="" name="" placeholder="" value="" ext="" force=false format="H:i" step=5>
    <#local id=(id=="")?string("${randomInputId()}", id)>
    <input type="text" class="form-control ${class}" <#if name != "">name="${name}"</#if> <#if id != "">id="${id}"</#if>
       <#if placeholder != "">placeholder="${placeholder}"</#if>
       <#if value != "">value="${value}"</#if> <@formParams ext/> autocomplete="off"/>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#${id}").datetimepicker({
                lang: 'ch',
                step: ${step},
                timepicker: true,
                datepicker: false,
                format: '${format}',
                formatTime: '${format}',
                allowBlank: ${force?string("false", "true")}
            });
        });
    </script>
</#macro>
<#--功能：表单中的时间选择框（格式：HH:MM）-->
<#macro formTime label class="" id="" name="" placeholder="" value="" icon="" divId="" ext="" force=false format="H:i" step=5>
<div class="form-group" <#if divId!="">id="${divId}"</#if>>
    <label class="col-sm-3 control-label">${label}</label>
    <div class="col-sm-8">
        <#if icon!="">
            <div class="input-group">
                <@inputTime class id name placeholder value ext force format step/>
                <span class="input-group-addon ">
                    <i class="icon-append ${icon}"></i>
                </span>
            </div>
        <#else>
            <@inputTime class id name placeholder value ext force format step/>
        </#if>
    </div>
</div>
</#macro>

<#--功能:日期时间选择框（格式：yyyy-MM-dd hh:mm）-->
<#macro inputDateTime class="" id="" name="" placeholder="" value="" ext="" force=false>
    <#local id=(id=="")?string("${randomInputId()}", id)>
    <input type="text" class="form-control ${class}" <#if name != "">name="${name}"</#if> <#if id != "">id="${id}"</#if>
       <#if placeholder != "">placeholder="${placeholder}"</#if>
       <#if value != "">value="${value}"</#if> <@formParams ext/> autocomplete="off"/>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#${id}").datetimepicker({
                lang: 'ch',
                step: 30,
                timepicker: true,
                datepicker: true,
                format: 'Y-m-d H:i:00',
                formatTime: 'H:i:00',
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
<option value="${(column.id?c)!}">
    <#list 0..level as x>
        <#if x < level>
            │&nbsp;&nbsp;
        <#else>
            ├
        </#if>
    </#list>
${column.name!}
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
<#macro inputEnum enums class="" id="" name="" value="" ext="" blank="" blankValue="" onchange=''>
    <#local id=(id=="")?string("${randomInputId()}", id)>
<select class="form-control ${class}" <#if name != "">name="${name}"</#if>
        <#if id != "">id="${id}"</#if> <@formParams ext/> <#if onchange != ''>onchange="${onchange}"</#if>/>
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
<#macro formEnum label enums class="" id="" name="" value="" icon ="" divId="" ext="" blank="" blankValue="" onchange=''>
<div class="form-group" <#if divId!="">id="${divId}"</#if>>
    <label class="col-sm-3 control-label">${label}</label>
    <div class="col-sm-8">
        <#if icon!="">
            <div class="input-group">
                <@inputEnum enums class id name value ext blank blankValue onchange/>
                <span class="input-group-addon ">
                    <i class="icon-append ${icon}"></i>
                </span>
            </div>
        <#else>
            <@inputEnum enums class id name value ext blank blankValue onchange/>
        </#if>
    </div>
</div>
</#macro>

<#--功能：Map选择控件-->
<#--black value="" 一般为全部-->
<#macro inputMap maps class="" id="" name="" value="" ext="" blank="" blankValue="" onchange=''>
    <#local id=(id=="")?string("${randomInputId()}", id)>
<select class="form-control ${class}" <#if name != "">name="${name}"</#if>
        <#if id != "">id="${id}"</#if> <@formParams ext/> <#if onchange != ''>onchange="${onchange}"</#if>/>
    <#if blank != ""><option value="${blankValue}">${blank}</option></#if>
    <#if maps??>
    <#list maps?keys as key>
    <option value="${key}">${maps[key]}</option>
    </#list>
    </#if>
</select>
    <script>
        $(document).ready(function () {
            $("#${id}").val("${value}");
        });
    </script>
</#macro>
<#--功能：表单中的树形输入选择控件-->
<#macro formMap label maps class="" id="" name="" value="" icon ="" divId="" ext="" blank="" blankValue="" onchange=''>
<div class="form-group" <#if divId!="">id="${divId}"</#if>>
    <label class="col-sm-3 control-label">${label}</label>
    <div class="col-sm-8">
        <#if icon!="">
            <div class="input-group">
                <@inputMap maps=maps! id=id name=name value=value ext=ext blank=blank blankValue=blankValue onchange=onchange/>
                <span class="input-group-addon ">
                    <i class="icon-append ${icon}"></i>
                </span>
            </div>
        <#else>
            <@inputMap maps=maps! id=id name=name value=value ext=ext blank=blank blankValue=blankValue onchange=onchange/>
        </#if>
    </div>
</div>
</#macro>

<#--功能：列表选择控件 options是个数组，每个元素必须存在id和name两个成员-->
<#--black value="" 一般为全部-->
<#macro inputList options class="" id="" name="" value="" ext="" blank="" blankValue="">
    <#local id=(id=="")?string("${randomInputId()}", id)>
<select class="form-control ${class}" <#if name != "">name="${name}"</#if>
        <#if id != "">id="${id}"</#if> <@formParams ext/>>
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

<#--功能：列表选择控件（从Map获取数据） options是个Map-->
<#--black value="" 一般为全部-->
<#macro inputListMap options class="" id="" name="" value="" ext="" blank="" blankValue="">
    <#local id=(id=="")?string("${randomInputId()}", id)>
<select class="form-control ${class}" <#if name != "">name="${name}"</#if>
        <#if id != "">id="${id}"</#if> <@formParams ext/>/>
    <#if blank != ""><option value="${blankValue}">${blank}</option></#if>
    <#list options?keys as key>
        <option value="${key!}">${(options[key])!}</option>
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
<#macro inputCheckbox text class="" id="" name="" checked="" value="1" onchange="">
    <label class="checkbox-inline">
        <input name="${name}" <#if onchange != "">onchange="${onchange}"</#if> <#if id != "">id="${id}"</#if> type="checkbox" class="margin-r-5 ${class}" <#if checked != "">checked="${checked}"</#if> value="${value}" style="vertical-align:middle; margin-top: 5px!important;">
        <span style="vertical-align:middle;">${text}</span>
    </label>
</#macro>
<#--功能：表单中的复选框控件-->
<#macro formCheckbox label text="&nbsp;" class="" id="" name="" icon ="" divId="" ext="" checked="" value="1">
<div class="form-group" <#if divId!="">id="${divId}"</#if>>
    <label class="col-sm-3 control-label">${label}</label>
    <div class="col-sm-8">
        <#if icon!="">
            <div class="input-group">
                <@inputCheckbox text class id name checked value/>
                <span class="input-group-addon ">
                    <i class="icon-append ${icon}"></i>
                </span>
            </div>
        <#else>
            <@inputCheckbox text class id name checked value/>
        </#if>
    </div>
</div>
</#macro>

<#--功能：复选框组-->
<#macro inputCheckboxGroup options class="" name="" showCheckAll=false checkAllValue="-1">
    <#if showCheckAll>
    <#local id=(id=="")?string("${randomInputId()}", id)>
    <label class="checkbox-inline">
        <input name="${name}" type="checkbox" class="boxcheckall margin-l-5 ${class}" id="${id}" value="${checkAllValue}"><b>全部</b>
    </label>
    </#if>
    <#list options as option>
    <label class="checkbox-inline">
        <input name="${name}" type="checkbox" value="${option.id?c}" class="margin-l-5 ${class}" onchange="_cbgc_(this)">${option.name}
    </label>
    </#list>
    <script>
        $(document).ready(function () {
            $("#${id}").click(function () {
                var checked = this.checked
                var parent = $("#${id}").parent().parent()
                parent.find("input").each(function (index) {
                    $(this).prop('checked', checked);
                })
            })
        });
        function _cbgc_(elem) {
            var parent = $(elem).parent().parent()
            var all = parent.find(".boxcheckall")
            var allChecked = true
            parent.find("input[name='" + $(elem).prop("name") + "']").each(function (index) {
                if ($(this).attr("class").indexOf("boxcheckall") != -1) return
                if (!$(this).prop('checked')) allChecked = false
            })
            parent.find(".boxcheckall").prop('checked', allChecked);
        }
    </script>
</#macro>
<#--功能：表单中的复选框组控件-->
<#macro formCheckboxGroup label options text="" class="" id="" name="" icon ="" divId="" ext="" checked="" showCheckAll=false checkAllValue="">
<div class="form-group" <#if divId!="">id="${divId}"</#if>>
    <label class="col-sm-3 control-label">${label}</label>
    <div class="col-sm-8">
        <#if icon!="">
            <div class="input-group">
                <@inputCheckboxGroup options class name showCheckAll checkAllValue/>
                <span class="input-group-addon ">
                    <i class="icon-append ${icon}"></i>
                </span>
            </div>
        <#else>
            <@inputCheckboxGroup options class name showCheckAll checkAllValue/>
        </#if>
    </div>
</div>
</#macro>

<#--功能：上传头像-->
<#macro inputImage name="" class="" useFileId=false btnicon="icon-file-image" editable=false button="选择" value="" errsrc="/admin/images/default_avatar.jpg" id="">
    <#local id=(id=="")?string("${randomInputId()}", id)>
    <div style="display:flex; ">
        <input type="text" style="background-color: white; flex:1; display: inline-block" class="form-control ${class}" <#if name != "">name="${name}"</#if> id="${id}_value" <#if value != "">value="${value}"</#if> <#if !editable>readonly</#if> />
        <a href="${value}" target="_blank"><img id="${id}_image" class="${class}" src="${value}" onerror="this.src='${errsrc}'" style="display: inline-block; margin-left: 15px; width: 32px; height: 32px;"></a>
        <div style="display: inline-block; margin-bottom:0px!important;" id="${id}_uploader" class="uploader" data-ride="uploader">
            <div id="${id}_new_avatar" class="uploader-files file-list file-list-grid" style="display: none;"></div>
            <button type="button" class="btn btn-primary uploader-btn-browse" style="width: 98px; margin-left: 15px; display: inline-block">
                <i class="icon ${btnicon}"></i>${button}
            </button>
        </div>
    </div>
<script>
    $('#${id}_uploader').uploader({
        multi_selection: false,
        file_data_name: 'file',
        mime_types: [
            {title: '图片', extensions: 'jpg,gif,png'}
        ],
        autoUpload: true,
        url: '/admin/system/uploader/zui/upload.json',
        previewImageSize: {width: 100, height: 100},
        deleteActionOnDone: function (file, doRemoveFile) {
            return true;
        }
    }).on('onFileUploaded', function(event, file, result) {
        var uploader = $('#${id}_uploader').data('zui.uploader');
        var plupload = uploader.plupload;
        var files = plupload.files;
        for (var i = 0; i < files.length; i++) {
            uploader.removeFile(files[i]);
        }
        $("#${id}_image").attr("src", file.url);
        <#if useFileId>
            $("#${id}_value").val(file.remoteData.id);
        <#else>
            $("#${id}_value").val(file.url);
        </#if>
    });
</script>
</#macro>
<#--功能：表单中的复选框组控件-->
<#macro formImage label name="" useFileId=false class="" editable=false icon ="" divId="" ext="" checked="" btnicon="icon-file-image" button="选择" value=""  errsrc="/admin/images/default_avatar.jpg">
<div class="form-group" <#if divId!="">id="${divId}"</#if>>
    <label class="col-sm-3 control-label">${label}</label>
    <div class="col-sm-8">
        <#if icon!="">
            <div class="input-group">
                <@inputImage name class useFileId btnicon editable button value errsrc divId/>
                <span class="input-group-addon ">
                    <i class="icon-append ${icon}"></i>
                </span>
            </div>
        <#else>
            <@inputImage name class useFileId btnicon editable button value errsrc divId/>
        </#if>
    </div>
</div>
</#macro>

<#macro inputColor name="" class="" btnicon="icon-file-image" button="选择" value="" id="">
    <#local id=(id=="")?string("${randomInputId()}", id)>
    <div style="display:flex; ">
        <input onchange="onColorChanged_${id}()" style="background-color: white; flex:1; display: inline-block; text-align: center; " type="text" class="form-control ${class}" name="${name}" id="color-${id}" value="${value!'364150'}" />
        <div style="display: inline-block; margin-left: 15px; width: 32px; height: 32px; background-color: #${value!'364150'}" id="color-demo-${id}"></div>
        <button style="width: 98px; margin-left: 15px; display: inline-block" type="button"
                class="btn btn-primary jscolor {valueElement:'chosen-value', onFineChange:'setTextColor_${id}(this)'}">
            <i class="icon ${btnicon}"></i>${button}
        </button>
    </div>
    <script>
        function onColorChanged_${id}() {
            var color = $("#color-${id}").val();
            $("#color-demo-${id}").css("background-color", "#" + color);
        }
        function setTextColor_${id}(picker) {
            $("#color-${id}").val(picker.toString());
            $("#color-demo-${id}").css("background-color", '#' + picker.toString());
        }
    </script>
</#macro>
<#macro formColor label name="" class="" btnicon="icon-file-image" button="选择" value="" icon ="" divId="">
    <div class="form-group" <#if divId!="">id="${divId}"</#if>>
        <label class="col-sm-3 control-label">${label}</label>
        <div class="col-sm-8">
            <#if icon!="">
                <div class="input-group">
                    <@inputColor name class btnicon button value divId/>
                    <span class="input-group-addon ">
                    <i class="icon-append ${icon}"></i>
                </span>
                </div>
            <#else>
                <@inputColor name class btnicon button value divId/>
            </#if>
        </div>
    </div>
</#macro>

<#--功能：上传头像-->
<#macro inputAvatar name="" id="" class="" useFileId=false btnicon="icon-plus" button="上传头像" src="/admin/images/default_avatar.jpg" errsrc="/admin/images/default_avatar.jpg">
    <#local id=(id=="")?string("${randomInputId()}", id)>
    <input type="hidden" name="${name}" id="${id}_value">
    <div class="col-center-block">
        <a href="${src}" target="_blank"><img id="${id}_image" class="${class}" src="${src}" onerror="this.src='${errsrc}'" style="width: 128px; height: 128px; object-fit: cover; object-position: center;float: none;display: block;margin-left: auto;margin-right: auto;"></a>
    </div>
    <div id="${id}_uploader" class="uploader" data-ride="uploader">
        <div id="${id}_new_avatar" class="uploader-files file-list file-list-grid" style="display: none;"></div>
        <button type="button" class="btn btn-primary uploader-btn-browse" style="width: 98px; margin-left: 15px; margin-top: 5px">
            <i class="icon ${btnicon}"></i>${button}
        </button>
    </div>
<script>
    $('#${id}_uploader').uploader({
        multi_selection: false,
        file_data_name: 'file',
        mime_types: [
            {title: '图片', extensions: 'jpg,gif,png'}
        ],
        autoUpload: true,
        url: '/admin/system/uploader/zui/upload.json',
        previewImageSize: {width: 100, height: 100},
        deleteActionOnDone: function (file, doRemoveFile) {
            return true;
        }
    }).on('onFileUploaded', function(event, file, result) {
        var uploader = $('#${id}_uploader').data('zui.uploader');
        var plupload = uploader.plupload;
        var files = plupload.files;
        for (var i = 0; i < files.length; i++) {
            uploader.removeFile(files[i]);
        }
        $("#${id}_image").attr("src", file.url);
        <#if useFileId>
            $("#${id}_value").val(file.remoteData.id);
        <#else>
            $("#${id}_value").val(file.url);
        </#if>
    });
</script>
</#macro>
<#--功能：表单中的复选框组控件-->
<#macro formAvatar label name="" useFileId=false class="" icon ="" divId="" btnicon="icon-plus" button="上传头像" src="/admin/images/default_avatar.jpg"  errsrc="/admin/images/default_avatar.jpg">
<div class="form-group" <#if divId!="">id="${divId}"</#if>>
    <label class="col-sm-3 control-label">${label}</label>
    <div class="col-sm-8">
        <@inputAvatar name class useFileId btnicon button src errsrc/>
    </div>
</div>
</#macro>

<#--功能：上传文件-->
<#macro inputFiles name="" class="" useFileId=false button="选择文件" icon="icon-cloud-upload" allowDelete=true allowMulti=true files=[] readonly=false autoUpload=true>
    <#local id=(id=="")?string("${randomInputId()}", id)>
    <div id="${id}" class="uploader">
        <div class="file-list" data-drag-placeholder="请拖拽文件到此处"></div>
        <button style="float: right" type="button" class="btn btn-primary uploader-btn-browse">
            <i class="icon ${icon}"></i> ${button}
        </button>
    </div>
    <div style="display: none;" id="${id}_files">
    <#if files??>
        <#list files as file>
            <input name='${name}' type='hidden' file-id='staticfiles-${(file.id?c)!}' value='${(file.id?c)!}'/>
        </#list>
    </#if>
    </div>
    <script>
        <#if readonly>
        $("#${id} .uploader-btn-browse").hide();
        </#if>
        $('#${id}').uploader({
            <#if autoUpload>
            autoUpload: true,
            <#else>
            autoUpload: false,
            </#if>
            <#if readonly>
            browse_button: null,
            drop_element: null,
            </#if>
            <#if !allowMulti>
                multi_selection: false,
            </#if>
            url: '/admin/system/uploader/zui/upload.json',
            <#if allowDelete && !readonly>
                deleteActionOnDone: function (file, doRemoveFile) {
                    $("[file-id='" + file.id + "']").remove();
                    return true
                },
            </#if>
            <#if files??>
                staticFiles: [
                    <#list files as file>
                    {name: '${(file.filename)!}', size: ${(file.fileSize?c)!}, url: '${(file.remoteUrl)!}', id: 'staticfiles-${(file.id?c)!}'},
                    </#list>
                ],
            </#if>
        }).on('onFileUploaded', function(event, file, result) {
            <#if !allowMulti>
                var uploader = $('#${id}').data('zui.uploader');
                var plupload = uploader.plupload;
                var files = plupload.files;
                for (var i = 0; i < files.length; i++) {
                    if (file != files[i]) uploader.removeFile(files[i]);
                }
                $("#${id}_files").empty()
            </#if>
            <#if useFileId>
                var content = "<input name='${name}' type='hidden' file-id='" + file.id + "' value='" + file.remoteData.id + "'/>"
            <#else>
                var content = "<input name='${name}' type='hidden' file-id='" + file.id + "' value='" + file.remoteData.url + "'/>"
            </#if>
            $("#${id}_files").append(content)
        });
    </script>
</#macro>
<#--功能：表单中的复选框组控件-->
<#macro formFiles label name="" useFileId=false class="" icon ="" divId="" button="选择文件" icon="icon-cloud-upload" allowDelete=true allowMulti=true files=[] readonly=false>
<div class="form-group" <#if divId!="">id="${divId}"</#if>>
    <label class="col-sm-3 control-label">${label}</label>
    <div class="col-sm-8">
        <@inputFiles name class useFileId button icon allowDelete allowMulti files readonly/>
    </div>
</div>
</#macro>

<#--功能：UEditor网页编辑器控件-->
<#macro inputHtml name id='' value='' class='' height="300px">
<#local id=(id=="")?string("${randomInputId()}", id)>
<script class="editorDiv textarea ${class}" id="${id}" name="${name}" type="text/plain" style="height: ${height}">
    ${value!}
</script>
<script>
    $(function () {
        var ue = UE.getEditor('${id}');
    });
</script>
</#macro>

<#--功能：表单中的UEditor网页编辑器控件-->
<#macro formHtml label name="" value='' class="" divId="" id='' height="300px">
<div class="form-group" <#if divId!="">id="${divId}"</#if>>
    <label class="col-sm-3 control-label">${label}</label>
    <div class="col-sm-8">
        <@inputHtml name id value class height/>
    </div>
</div>
</#macro>

<#--功能：表单中的UEditor网页编辑器控件-->
<#macro formNested label divId="">
<div class="form-group" <#if divId!="">id="${divId}"</#if>>
    <label class="col-sm-3 control-label">${label}</label>
    <div class="col-sm-8">
        <#nested />
    </div>
</div>
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
<#macro inputDropdownGroup options class="" id="" name="" blank="" zero="" blankValue="" value="" onchange="">
    <#local id=(id=="")?string("${randomInputId()}", id)>
    <select class="form-control ${class}" name="${name}" id="${id}" onchange="${onchange}">
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

<#--功能：下拉列表联动-->
<#macro inputDropdownChain options childid="" urlprefix="" class="" id="" name="" blank="" zero="" blankValue="" value="">
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
            <#if childid != "" && urlprefix != "">
                $("#${id}").change(function () {
                    var id = $("#${id}").val();
                    if (id == null || id == undefined){
                        $("#${childid}").empty();
                        return
                    }
                    $.get("${urlprefix}" + id,
                            function (data) {
                                if (data.code < 0) {
                                    alertShow("warning", data.message, 3000);
                                } else {
                                    $("#${childid}").empty();
                                    data.result.forEach(function (child) {
                                        $("#${childid}").append("<option value='" + child.id + "'>" + child.name + "</option>");
                                    })
                                    $("#${childid}").trigger('change');
                                }
                            },
                            "json"
                    );
                })
            </#if>
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
    <i class="icon ${icon}"></i>
    ${text}
</button>
</#macro>


<#--功能：常规按钮-->
<#macro inputButton class="btn-primary" id="" name="" text="" icon="" href="#" onclick="">
<a href="${href}" class="btn ${class}" id="${id}" name="${name}" <#if onclick != "">onclick="${onclick}"</#if>>
    <#if icon!=""><i class="icon ${icon}"></i></#if>${text}
</a>
</#macro>
