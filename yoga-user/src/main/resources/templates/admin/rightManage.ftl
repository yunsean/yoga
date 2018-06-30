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
					 <a href="#">岗位管理</a>
				</li>
					<!-- END PAGE TITLE & BREADCRUMB-->
			</ol>
		</section>
			<!-- END PAGE HEADER-->
			<!-- BEGIN DASHBOARD STATS -->
		<section class="content">
			<div class="row">
				<div class="col-md-12">
					<div class="box box-default">
						<div class="box-header  with-border">
							<i class="fa fa-calendar"></i><h3 class="box-title">岗位列表</h3>
						</div>
						<div class="box-body">
							<div class="table-toolbar">
								<div class="col-sm-3 form-group">
									<label class="col-sm-4">姓名</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" id="showme">
									</div>
								</div>
								<div class="col-sm-6 button-group">
									<a class="btn btn-primary btn-sm" data-toggle="modal"  data-target="#addPost">
										<i class="fa fa-plus">
										</i>
										新增岗位
									</a>
								</div>
							</div>
							<table class="table table-striped table-bordered table-hover" id="postTable" >
								<thead>
									<tr >
										<th style="width: 15%;">
											序号
										</th>
										<th style="width: 15%;">
											岗位名称
										</th>
										<th style="width: 25%;">
											岗位授权
										</th>
										<th style="width: 15%;">
											状态
										</th>
										<th class="tableCenter" style="width:30%">
											操作
										</th>
									</tr>
								</thead>
								<tbody >
								<#list roles as role>
									<tr class="odd gradeX">
										<td >
											${role.id?if_exists}
										</td>
										<td >
											${role.name?if_exists}
										</td>
										<td >
											${role.remark?if_exists}
										</td>
										<td >
											${role.states?if_exists}
										</td>
										<td class="tableCenter">
											<span>
												<a data-target="#" name="editRole" roleId="${role.id?if_exists}" roleName="${role.name?if_exists}" roleRemark="${role.remark?if_exists}" class="btn btn-primary btn-sm" role="button" data-toggle="modal">编辑</a>
											</span>
											<span>
												<#if role.state==1 >
													<a href="#" name="roleState" roleId="${role.id?if_exists}" roleState="${role.state?if_exists}" class="btn btn-warning btn-sm" role="button" data-toggle="modal">禁用</a>
												<#else>
													<a href="#" name="roleState" roleId="${role.id?if_exists}" roleState="${role.state?if_exists}" class="btn btn-success btn-sm" role="button" data-toggle="modal">启用</a>
												</#if>
											</span>
											<span>
												<a href="#" name="roleDel" roleId="${role.id?if_exists}" class="btn btn-danger btn-sm" role="button" data-toggle="modal">删除</a>
											</span>

											<span>
												<a  data-target="#" name="roleAuth" roleId="${role.id?if_exists}" class="btn btn-info btn-sm"  role="button" data-toggle="modal">授权</a>
											</span>
										</td>
									</tr>
								</#list>
								</tbody>
								<!--<tfoot>
									<tr>
										<td  colspan="7" align="center">
											<@html.paging paginator=paginator param=param action="/admin/operator/roleList.htm"/>
										</td>
									</tr>
								</tfoot>-->
							</table>
						</div>
						<div class="box-footer clearfix pagebar">
							<@html.paging paginator=paginator param=param action="/admin/operator/roleList.htm"/>
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
<div id="addPost" class="modal fade" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog" style="width:800px">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">新增岗位</h4>
			</div>
			<form action="/admin/operator/role.htm?action=new" method="post" class="form-horizontal">
				<div class="modal-body">
					<div class="form-body">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<lable class="control-label col-md-3">岗位名称<span class="required">*</span>
									</lable>
									<div class="col-md-8"> 
										<input type="text" id="name" name="name" class="form-control">
										<span class="help-block"></span>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<lable class="control-label col-md-3">岗位备注
									</lable>
									<div class="col-md-8"> 
										<input type="text" id="remark" name="remark" class="form-control">
										<span class="help-block"></span>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" data-dismiss="modal" class="btn btn-default">关闭</button>
					<input type="submit" class="btn btn-primary" value="保存"/>
				</div>
			</form>
		</div>
	</div>
</div>
<div id="editRole" class="modal fade" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog" style="width:800px">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">编辑岗位</h4>
			</div>
			<form action="/admin/operator/role.htm?action=edit" method="post" class="form-horizontal">
				<div class="modal-body">
				
					<div class="form-body">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<lable class="control-label col-md-3">岗位名称<span class="required">*</span>
									</lable>
									<div class="col-md-8"> 
										<input type="hidden" name="id" >
										<input type="text" name="name" class="form-control">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<lable class="control-label col-md-3">岗位备注
									</lable>
									<div class="col-md-8"> 
										<input type="text" name="remark" class="form-control">
									</div>
								</div>
							</div>
						</div>
					</div>
				
				</div>
				<div class="modal-footer">
					<button type="button" data-dismiss="modal" class="btn btn-default btn-sm">关闭</button>
					<input type="submit" class="btn btn-primary" value="保存"/>
				</div>
			</form>
		</div>
	</div>
</div>
<div id="authorization" class="modal fade" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog" style="width:800px">
		<div class="modal-content">
			<form action="/admin/operator/saveSecurity.htm" method="post" class="form-horizontal">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">授权</h4>
			</div>
			<div class="modal-body">
				<div class="form-body">
					<div class="row">
						<div class="form-group">
							<div class="col-md-12">
							<input type="hidden" name="roleId">
								<div class="panel-group accordion" id="wholeBox">
								<#list menus as menu>
									<#if (!menu.admin)>
									<div class="panel panel-default">
										<div class="panel-heading topMenu">
											<h3 class="panel-title">
											<input name="${menu.id?if_exists}" type="checkbox" id="topMenu_1_checkbox" class="topMenuCheckbox"/>
											<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#wholeBox" href="#collapse_${menu.id?if_exists}">${menu.title?if_exists}</a>
											</h3>
										</div>
										<div id="collapse_${menu.id?if_exists}" class="panel-collapse collapse">
											<div class="panel-body">
												<div class="secondeMenu">
												<#list menu.children as mc>
													<div class="secondMenuPage">
														<input name="${mc.id?if_exists}" type="checkbox" class="secondMunuCheckbox"><label><h4>${mc.title?if_exists}</h4></label>
													</div>
													<div class="secondPageButtons" style="width:100%;background-color:#EEEEEE">
													<#list mc.children as mcc>
														<span><input name="${mcc.id?if_exists}" type="checkbox" checkde="checked" class="pageButtonsCheckbox"/><label>${mcc.title?if_exists}</label>
														</span>
													</#list>
													</div>
												</#list>
												</div>
											</div>
										</div>
									</div>
									</#if>
									</#list>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" data-dismiss="modal" class="btn btn-default btn-sm">关闭</button>
				<input type="submit" class="btn btn-primary btn-sm" value="保存"/>
			</div>
			</form>
		</div>
	</div>
</div>
<div id="deleteConfirm" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="deleteConfirm" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<form action="/admin/operator/role.htm?action=del" method="post" class="form-horizontal">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title">删除确认</h4>
				</div>
				<div class="modal-body">
					<input type="hidden" name="id"/>
					<p style="text-align:center">
						您确定要<b style="color:red">删除</b>该岗位吗？
					</p>
				</div>
				<div class="modal-footer">
					<button class="btn btn-default btn-sm" data-dismiss="modal" aria-hidden="true">关闭</button>
					<input type="submit" class="btn btn-primary btn-sm" value="确认">
				</div>
			</div>
			</form>
		</div>
	</div>
</div>
<div id="forbiddenConfirm" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="forbiddenConfirm" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<form action="/admin/operator/role.htm?action=state" method="post" class="form-horizontal">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title">提示</h4>
				</div>
				<div class="modal-body">
					<input type="hidden" name="id"/>
					<input type="hidden" name="state"/>
					<p style="text-align:center">
						您确定要<b name="state" style="color:red"></b>该岗位吗？
					</p>
				</div>
				<div class="modal-footer">
					<button class="btn btn-default btn-sm" data-dismiss="modal" aria-hidden="true">关闭</button>
					<input type="submit" class="btn btn-primary btn-sm" value="确认">
				</div>
			</div>
			</form>
		</div>
	</div>
</div>
<!-- END BODY -->
</@html.htmlPage>
<#include "js/right.js">