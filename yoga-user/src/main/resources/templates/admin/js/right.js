<script>
jQuery(document).ready(function(){
	
		//编辑角色
		$("a[name='editRole']").click(function(){
			var id = $(this).attr("roleId");
			var name = $(this).attr("roleName");
			var remark = $(this).attr("roleRemark");
			$("#editRole").find("input[name='id']").val(id);
			$("#editRole").find("input[name='name']").val(name);
			$("#editRole").find("input[name='remark']").val(remark);
			
			$("#editRole").modal('show').on('show',function(){});
		});
		//停用/启用角色
		$("a[name='roleState']").click(function(){
			var id = $(this).attr("roleId");
			var state = $(this).attr("roleState");
			$("#forbiddenConfirm").find("input[name='id']").val(id);
			if(state=='1'){
				$("#forbiddenConfirm").find("b[name='state']").html('停用');
				$("#forbiddenConfirm").find("input[name='state']").val(0);
			}else{
				$("#forbiddenConfirm").find("b[name='state']").html('启用');
				$("#forbiddenConfirm").find("input[name='state']").val(1);
			}
			$("#forbiddenConfirm").modal('show').on('show',function(){});
		});
		//删除角色
		$("a[name='roleDel']").click(function(){
			var id = $(this).attr("roleId");
			$("#deleteConfirm").find("input[name='id']").val(id);
			$("#deleteConfirm").modal('show').on('show',function(){});
		});
		//角色授权
		$("a[name='roleAuth']").click(function(){
			var id = $(this).attr("roleId");
			$("#authorization").find("input[name='roleId']").val(id);
			$("#authorization").find("input[type='checkbox']").attr("checked",false);
			$(":checkbox").uniform();
			//获取角色权限数据
			$.post("/admin/operator/getRoleSecurity.json",{roleId:id},function(data){
				if(data.state=="SUCCESS"){
					for(var i=0;i<data.res.length;i++){
						var check = $("#authorization").find("input[name='"+data.res[i]+"']");
						if(check.length>0){
						$(check)[0].checked = true;
						}
					}
					$(":checkbox").uniform();
				}
			});
			$("#authorization").modal('show').on('show',function(){});
		});
		
		/*$(".pageButtonsCheckbox").on("click",function(){
			if($(this).attr("checked")){
				$(this).parents(".panel").find(".topMenuCheckbox").attr("checked","checked");
				$(this).parents(".secondeMenu").find(".secondMunuCheckbox").attr("checked","checked");
				$(":checkbox").uniform();
			}
		});*/
		//二级菜单
		$(".secondMunuCheckbox").on("click",function(){
			
			if($(this).attr("checked")){
				$(this).parents(".panel").find(".topMenuCheckbox").attr("checked","checked");
				$(":checkbox").uniform();
			}else{
				var isAttr = false;
				$(this).parents(".panel-body").find("input").each(function(){
					if($(this).attr("checked")){
						isAttr = true;
					}
				});
				if(!isAttr){
					$(this).parents('.panel-default').find(".topMenuCheckbox").removeAttr("checked");
					$(":checkbox").uniform();
				}
			}
		});
		//一级菜单
		$(".topMenuCheckbox").on("click",function(){
			var list = $(this).parents(".panel").find(".secondMunuCheckbox");
			if(list.length==0){
				$(this).removeAttr("checked");
			}
			if(!$(this).attr("checked")){
				$(this).parents(".panel").find(".secondMunuCheckbox").removeAttr("checked");
				$(":checkbox").uniform();
			}else{
				$(this).parents(".panel").find(".secondMunuCheckbox").attr("checked","checked");
				$(":checkbox").uniform();
			}
		});
		
		$("#editRole").find("input[type='submit']").click(function(){
			var name = $("#editRole").find("input[name='name']").val();
			var remark = $("#editRole").find("input[name='remark']").val();
			if($.trim(name)==""){
				alertShow("请输入岗位名称!",3000,"warning");
				return false;
			}
			if(name.length > 10){
				alertShow("岗位名称不能大于10个字符!",3000,"warning");
				return false;
			}
			if(remark.length > 50){
				alertShow("备注不能大于50个字符!",3000,"warning");
				return false;
			}
			return true;
		});
		
		$("#addPost").find("input[type='submit']").click(function(){
			var name = $("#addPost").find("input[name='name']").val();
			var remark = $("#addPost").find("input[name='remark']").val();
			if($.trim(name)==""){
				alertShow("请输入岗位名称!",3000,"warning");
				return false;
			}
			if(name.length > 10){
				alertShow("岗位名称不能大于10个字符!",3000,"warning");
				return false;
			}
			if(remark.length > 50){
				alertShow("备注不能大于50个字符!",3000,"warning");
				return false;
			}
			return true;
		});
	});
</script>