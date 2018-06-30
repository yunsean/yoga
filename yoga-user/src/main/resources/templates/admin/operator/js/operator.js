<script>
$(document).ready(function(){
	//加载角色名称
	$("td[name='roleId']").each(function(){
		var role = $(this);
		var roleId = $(this).attr("roleId");
		$.post("/admin/operator/role.json?action=load",{id:roleId},function(data){
			if(data.role!=null){
				$(role).html(data.role.name);
			}
		});
	});
	
	//编辑用户
		$("a[name='editUser']").click(function(){
			var id = $(this).attr("userId");
			$("#editUser").find("input[name='name']").removeAttr("disabled");
			$("#editUser").find("select[name='roleId']").removeAttr("disabled");
			$.get("/admin/operator/user.json",{userId:id},function(data){
				if(data.user!=null){
					$("#editUser").find("input[name='id']").val(data.user.id);
					$("#editUser").find("input[name='name']").val(data.user.name);
					$("#editUser").find("input[name='loginId']").val(data.user.loginId);
					//$("#editUser").find("input[name='pwd']").val(data.user.password);
					$("#editUser").find("select[name='roleId']").val(data.user.roleId);
					
					if(data.user.loginId=="superadmin"){
						$("#editUser").find("input[name='name']").attr("disabled","disabled");
						$("#editUser").find("select[name='roleId']").attr("disabled","disabled");
					}
					
					$("#editUser").modal('show').on('show',function(){});
				}
			});
		});
		
		//停用/启用用户
		$("a[name='forbiddenConfirm']").click(function(){
			var id = $(this).attr("userId");
			var enabled = $(this).attr("userEnabled");
			$("#forbiddenConfirm").find("input[name='id']").val(id);
			if(enabled=='1'){
				$("#forbiddenConfirm").find("b[name='enabled']").html('禁用');
				$("#forbiddenConfirm").find("input[name='enabled']").val(0);
			}else{
				$("#forbiddenConfirm").find("b[name='enabled']").html('启用');
				$("#forbiddenConfirm").find("input[name='enabled']").val(1);
			}
			$("#forbiddenConfirm").modal('show').on('show',function(){});
		});

		//删除用户
		$("a[name='userDel']").click(function(){
			var id = $(this).attr("userId");
			$("#deleteConfirm").find("input[name='id']").val(id);
			$("#deleteConfirm").modal('show').on('show',function(){});
		});
		
		$("#addUser").find("input[type='submit']").click(function(){
			var name = $("#addUser").find("input[name='name']").val();
			var loginId = $("#addUser").find("input[name='loginId']").val();
			var pwd = $("#addUser").find("input[name='pwd']").val();
			var roleId = $("#addUser").find("select[name='roleId']").val();
			
			if(name==""){
				alertShow("请输入用户姓名 !",3000,"warning");
				return false;
			}
			if(name.length > 10){
				alertShow("用户姓名不能大于10个字符 !",3000,"warning");
				return false;
			}
			if(loginId==""){
				alertShow("请输入登录账号!",3000,"warning");
				return false;
			}
			if(loginId.length > 20 && loginId.length < 6){
				alertShow("登陆账号的长度为6～20个字符之间!",3000,"warning");
				return false;
			}
			if(pwd==""){
				alertShow("请输入登录密码!",3000,"warning");
				return false;
			}
			if(pwd.length > 20 && pwd.length < 8){
				alertShow("登陆密码的长度为8～20个字符之间!",3000,"warning");
				return false;
			}
			if(roleId==""){
				alertShow("请选择岗位!",3000,"warning");
				return false;
			}
			return true;
		});
		
		$("#editUser").find("input[type='submit']").click(function(){
			var name = $("#editUser").find("input[name='name']").val();
			//var loginId = $("#editUser").find("input[name='loginId']").val();
			var pwd = $("#editUser").find("input[name='pwd']").val();
			var roleId = $("#editUser").find("select[name='roleId']").val();
			
			if(name==""){
				alertShow("请输入用户姓名 !",3000,"warning");
				return false;
			}
			if(pwd==""){
				alertShow("请输入登录密码!",3000,"warning");
				return false;
			}
			if(roleId==""){
				alertShow("请选择岗位!",3000,"warning");
				return false;
			}
			return true;
		});
});
</script>