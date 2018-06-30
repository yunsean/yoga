<#import "/macro_paging.ftl" as html>
<@html.htmlPage title="<@common.roleAlias/>">
	<!-- BEGIN CONTENT -->
	<div class="wrapper">
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
								<div class="box-title">
									<div class="caption">
										<i class="fa fa-calendar"></i>岗位列表
									</div>
								</div>
								<div class="box-body">
									<div class="table-toolbar">
										<div class="col-sm-6 btn-group">
											<a class="btn btn-primary btn-sm" data-toggle="modal"  data-target="#addPost">
												<i class="fa fa-plus">
												</i>
												新增岗位
											</a>
										</div>
									</div>
									<!-- <div class="dataTables_wrapper form-inline" role="grid"> -->
										<table class="table table-striped table-bordered table-hover dataTable" id="postTable" >
											<thead>
												<tr >
													<th style="width: 10%;">
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
													<th class="tableCenter" style="width:35%">
														操作
													</th>
												</tr>
											</thead>
											<tbody >
												<tr class="odd gradeX">
													<td >
														restest
													</td>
													<td >
														20
													</td>
													<td >
														12.12.2012
													</td>
													<td >
														启用中
													</td>
													<td class="tableCenter">
														<span>
															<a data-target="#addPost" class="btn green btn-sm" role="button" data-toggle="modal">编辑</a>
														</span>
														<span>
															<a href="#forbiddenConfirm" class="btn yellow btn-sm" role="button" data-toggle="modal">禁用</a>
														</span>
														<span>
															<a href="#deleteConfirm" class="btn red btn-sm" role="button" data-toggle="modal">删除</a>
														</span>
		
														<span>
															<a  data-target="#authorization" class="btn blue btn-sm"  role="button" data-toggle="modal">授权</a>
														</span>
													</td>
												</tr>
											</tbody>
										</table>
									<!-- </div> -->
								</div>
								<div class="box-footer clearfix pagebar">
									<@html.paging paginator=paginator param=param action="/admin/operator/roleList.htm"/>
								</div>
							</div>
						</div>
					</div>
			</section>
		</div>
	</div>
	<!-- END CONTENT -->
</@html.htmlPage>