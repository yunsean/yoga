<#import "/macro_paging.ftl" as html>
<@html.htmlPage title="">
<div class="wrapper">
	<!-- BEGIN CONTENT -->
	<div class="content-wrapper">
		<!-- BEGIN PAGE HEADER-->
		<section class="content-header">
			<ol class="breadcrumb">
				<!-- BEGIN PAGE TITLE & BREADCRUMB-->
				<li>
					<a href="#"><i class="fa fa-dashboard"></i>用户与权限</a>
				</li>
				<li>
					 <a href="#">用户权限</a>
				</li>
					<!-- END PAGE TITLE & BREADCRUMB-->
			</ol>
		</section>
		<!-- END PAGE HEADER-->
		<section class="content">
			<div class="row">
				<div class="col-md-12">
					<div class="box box-primary">
						<div class="box-header  with-border">
							<i class="fa fa-calendar"></i><h3 class="box-title">用户列表</h3>
						</div>
						<div class="box-body">
							<div class="table-toolbar">
								<div class="btn-group">
									<a class="btn btn-primary btn-sm" data-toggle="modal"  data-target="#addUser">
										<i class="fa fa-plus">
										</i>
										新增用户
									</a>
								</div>
							</div>
							<!-- <div class="dataTables_wrapper form-inline" role="grid"> -->
								<table class="table table-striped table-bordered table-hover" id="userTable" >
									<thead>
										<tr >
											<th style="width: 10%;">
												序号
											</th>
											<th style="width: 15%;">
												用户姓名
											</th>
											<th style="width: 15%;">
												登录账号
											</th>
											<th style="width: 20%;">
												分配岗位
											</th>
											<th style="width: 20%;">
												创建日期
											</th>
											<th class="tableCenter">
												操作
											</th>
										</tr>
									</thead>
									<tbody >
									<#list users as user>
										<tr class="odd gradeX">
											<td >
												${user.id?if_exists}
											</td>
											<td >
												${user.name?if_exists}
											</td>
											<td >
												${user.loginId?if_exists}
											</td>
											<td name="roleId" roleId="${user.roleId?if_exists}">
																					
											</td>
											<td >
												${((user.createTime)?string("yyyy-MM-dd"))!}
											</td>
											<td class="tableCenter">
												<#if user.loginId!="superadmin">
												<span>
													<#if user.enabled >
														<a href="#" name="forbiddenConfirm" id="${user.id?if_exists}" userEnabled="1" class="btn btn-sm btn-warning" role="button" data-toggle="modal">禁用</a>
													<#else>
														<a href="#" name="forbiddenConfirm" id="${user.id?if_exists}" userEnabled="0" class="btn btn-sm btn-success" role="button" data-toggle="modal">启用</a>
													</#if>
												</span>
												<span>
													<a href="#" name="userDel" id="${user.id?if_exists}" class="btn btn-sm btn-danger" role="button" data-toggle="modal">删除</a>
												</span>
												</#if>
												<span>
													<a name="editUser" id="${user.id?if_exists}" loginId="${user.loginId?if_exists}" class="btn btn-sm btn-info"  role="button" data-toggle="modal">修改</a>
												</span>
											</td>
										</tr>
									</#list>
									</tbody>
									<!--<tfoot>
										<tr>
											<td  colspan="7" align="center">
												<@html.paging paginator=paginator param=param action="/admin/operator/userList.htm"/>
											</td>
										</tr>
									</tfoot>-->
								</table>
							<!-- </div> -->
						</div>
						<div class="box-footer clearfix pagebar">
							<@html.paging paginator=paginator param=param action="/admin/operator/userList.htm"/>
						</div>						
					</div>
				</div>
			</div>
		</section>
	</div>
	<!-- END CONTENT -->
</div>
<!-- END CONTAINER -->
<!-- BEGIN FOOTER -->
<footer class="main-footer">
    <div class="pull-right hidden-xs">
      <b>Version</b> 1.0
    </div>
    <strong>Copyright &copy; 2014-2016 uWedding</strong> All rights reserved.
</footer>
<!-- END JAVASCRIPTS -->
<div id="addUser" class="modal fade" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog" style="width:800px">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">新增用户</h4>
			</div>
			<form action="/admin/operator/userSave.htm" method="post" class="form-horizontal">
				<div class="modal-body">
						<div class="form-body">
							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<lable class="control-label col-md-4">用户姓名
										<span class="required">*</span>
										</lable>
										<div class="col-md-8"> 
											<input type="text" id="name" name="name" class="form-control">
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<lable class="control-label col-md-4">登录账号
										<span class="required">*</span>
										</lable>
										<div class="col-md-8"> 
											<input type="text" id="loginId" name="loginId" class="form-control">
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<lable class="control-label col-md-4">登录密码
										<span class="required">*</span>
										</lable>
										
										<div class="col-md-8"> 
											<input type="text" id="pwd" name="pwd" class="form-control">
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<lable class="control-label col-md-4">选择岗位
										<span class="required">*</span>
										</lable>
										<div class="col-md-8"> 
											<select class="form-control" name="roleId">
												<#list roles as rl>
												<option value="${rl.id?if_exists}">${rl.name?if_exists}</option>
												</#list>
											</select>
										</div>
									</div>
								</div>
								
							</div>
						</div>
				</div>
				<div class="modal-footer">
					<button type="button" data-dismiss="modal" class="btn btn-default btn-sm">关闭</button>
					<input type="submit"  class="btn btn-sm btn-primary" value="保存">
				</div>
			</form>
		</div>
	</div>
</div>
<div id="editUser" class="modal fade" tabindex="-1" aria-hidden="true">
	<div class="modal-dialog" style="width:800px">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">编辑用户</h4>
			</div>
			<form action="/admin/operator/userSave.htm" method="post" class="form-horizontal">
				<div class="modal-body">
						<div class="form-body">
							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<lable class="control-label col-md-4">用户姓名
										<span class="required">*</span>
										</lable>
										<div class="col-md-8"> 
											<input type="hidden" name="id">
											<input type="text" name="name" class="form-control">
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<lable class="control-label col-md-4">登录密码
										<span class="required">*</span>
										</lable>
										<div class="col-md-8"> 
											<input type="text" name="pwd" class="form-control">
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<lable class="control-label col-md-4">选择岗位
										<span class="required">*</span>
										</lable>
										<div class="col-md-8"> 
											<select class="form-control" name="roleId">
												<option></option>
												<#list roles as rl>
												<option value="${rl.id?if_exists}">${rl.name?if_exists}</option>
												</#list>
											</select>
										</div>
									</div>
								</div>						
							</div>
						</div>
				</div>
				<div class="modal-footer">
					<button type="button" data-dismiss="modal" class="btn btn-default">关闭</button>
					<input type="submit"  class="btn blue" value="保存">
				</div>
			</form>
		</div>
	</div>
</div>
<div id="deleteConfirm" class="modal fade" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<form action="/admin/operator/user.htm?action=del" method="post" class="form-horizontal">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
					<h4 class="modal-title">删除确认</h4>
				</div>
				<div class="modal-body">
					<input type="hidden" name="id"/>
					<p>
						您确定要<b style="color:red">删除</b>该岗位吗？
					</p>
				</div>
				<div class="modal-footer">
					<button class="btn default" data-dismiss="modal" aria-hidden="true">关闭</button>
					<input type="submit" class="btn blue" value="确认">
				</div>
			</div>
			</form>
		</div>
	</div>
</div>
<div id="forbiddenConfirm" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="deleteConfirm" aria-hidden="true">
	<form action="/admin/operator/user.htm?action=enabled" method="post" class="form-horizontal">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
			<h4 class="modal-title">提示</h4>
		</div>
		<div class="modal-body">
			<input type="hidden" name="id"/>
			<input type="hidden" name="enabled"/>
			<p>
				您确定要<b name="enabled" style="color:red">禁用</b>该用户吗？
			</p>
		</div>
		<div class="modal-footer">
			<button class="btn default" data-dismiss="modal" aria-hidden="true">关闭</button>
			<input type="submit" class="btn blue" value="确认">
		</div>
	</div>
	</form>
</div>
</@html.htmlPage>
<#include "js/operator.js">