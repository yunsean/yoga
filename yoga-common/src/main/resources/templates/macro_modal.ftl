<#macro modal id name
	columns fields types
	params = []
	triggle = ''
	keyclass = 'col-sm-3  control-label' valueclass = 'col-sm-8' >
	<div class="modal fade" id="modal_${id}" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-dialog" style="width:50%">
      	  <div class="form-horizontal">
	          <div class="modal-content">
	              <div class="modal-header">
	                  <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                  <h4 class="modal-title" id="myModalLabel">${name}</h4>
	              </div>
	              <div class="modal-body">	
		              <#list 1..columns?size as index>
                         <div class="form-group">
                            <label class="${keyclass}">${columns[index - 1]?if_exists}</label>
                            <div class="${valueclass}">  
                            	<#if (types[index - 1] == 'text')>
                            		<input type="text" class="form-control" id="${columns[index - 1]?if_exists}">
                            	<#elseif (type[index - 1] == 'select')>
                            		<select class="form-control" id="${fields[index - 1]?if_exists}">
                            			<#assign list=params[index - 1]?if_exists/> 
    		                  			<#list list as item>
    		                  				<option value="${role.id?default(0)?c}">${role.name?if_exists}</option>
    		                  			</#list>
    		                  		</select>
                            	<#else>
                            		<#nested index>
                            	</#if>                
                            </div>
                         </div>
		              </#list> 
	              </div>                   
	             <div class="modal-footer">
	                <a href="javascript:void(0)" class="btn btn-danger" data-dismiss="modal"><i class="fa fa-fw fa-remove"></i></button>取消</a> 
	                <a href="javascript:void(0)" id="${id}_save" class="btn btn-info"><i class="fa fa-fw  fa-save"></i>保存</a>
	             </div>
	         </div>
        </div>
    </div>  

	<script language="javascript">
		function ${"on_" + id}(obj) {
            <#nested "init" obj>	
			$("#modal_${id}").modal("show");
			$("#${id}_save").on("click", function() {
			});
		}
		<#if (triggle != '')>    	
		$(function() {
			$(${triggle}).on("click", function(){
				$("${'modal_' + id}").modal("show");
			});
		}
		</#if>
	</script>
</#macro>

<#macro confirm name requestUrl refreshUrl>
<script language="javascript">
function ${name}(param){
	warningModal("确定要删除该模板吗？", function(){
		$.get(
			"${requestUrl}" + templateId,
			function(data) {
				if (data.code < 0){
					alertShow("danger", data.message, 3000);
					return;
				} else {
					window.location.href="${refreshUrl}";
				}
			},
			"json"
		);
	});
}
</script>
</#macro>
