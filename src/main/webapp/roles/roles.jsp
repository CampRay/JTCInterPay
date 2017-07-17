<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!--> <html lang="en" class="no-js"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8"/>
<title>Roles List</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="" name="description"/>
<meta content="" name="author"/>
<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="<c:url value="/"/>assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
<link href="<c:url value="/"/>assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css"/>
<link href="<c:url value="/"/>assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="<c:url value="/"/>assets/global/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
<link href="<c:url value="/"/>assets/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css" rel="stylesheet" type="text/css"/>
<!-- END GLOBAL MANDATORY STYLES -->
<!-- BEGIN PAGE LEVEL STYLES -->
<link href="<c:url value="/"/>assets/global/plugins/select2/select2.css" rel="stylesheet" type="text/css"/>
<link href="<c:url value="/"/>assets/global/plugins/datatables/plugins/bootstrap/dataTables.bootstrap.css" rel="stylesheet" type="text/css"/>
<!-- BEGIN THEME STYLES -->
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<!-- DOC: Apply "page-header-fixed-mobile" and "page-footer-fixed-mobile" class to body element to force fixed header or footer in mobile devices -->
	<!-- BEGIN HEADER -->
	<!-- END HEADER -->
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN SIDEBAR -->
		<c:import url="/common/left"/>
		<!-- END SIDEBAR -->
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">	
				<!-- BEGIN PAGE TITLE & BREADCRUMB-->
				 <div class="page-bar">
					<%--<ul class="page-breadcrumb">
						<li>
							<i class="fa fa-home"></i>
							<a href="<c:url value="/"/>home"><s:message code="home"/></a>
							<i class="fa fa-angle-right"></i>
						</li>
						<li>
							<a href="<c:url value="/"/>roles"><s:message code="system.management.roles.title" /></a>
						</li>
				</div> 
				<!-- END PAGE TITLE & BREADCRUMB-->
				
				<!-- BEGIN SEARCH FORM -->
				<div class="portlet-body">
					<form id="searchForm" name="searchForm" action="rolelist" class="form-horizontal" method="post">
					<div class="row">
						<div class="col-md-6">					
							<div class="form-group">
								<label class="col-md-3 control-label"><s:message code="system.management.roles.search.rolename" /></label>
								<div class="col-md-9">
									<input name="roleName" type="text" class="form-control">							
								</div>
							</div>
						</div>
						<div class="col-md-6">	
							<div class="form-group">
								<label class="col-md-3 control-label"><s:message code="system.management.user.searchform.status" /></label>
								<div class="col-md-9">
									<div class="radio-list">
										<label class="radio-inline">
										<input type="radio" name="status" value="" checked/>All </label>
										<label class="radio-inline">
										<input type="radio" name="status" value="true"/><s:message code="all.status.enable" /> </label>
										<label class="radio-inline">
										<input type="radio" name="status" value="false"/><s:message code="all.status.disable" /></label>
									</div>									
								</div>
							</div>
						</div>
					</div>					
					<div class="row">	
						<div class="col-md-6">	
							<div class="form-group">								
								<div class="col-md-offset-3 col-md-9">
									<button type="submit" class="btn blue">Search <i class="fa fa-search"></i></button>
									<button type="reset" class="btn grey-cascade">Reset <i class="fa fa-reply"></i></button>
								</div>
							</div>					
						</div>
					</div>	
					</form>
				</div>
				<!-- END SEARCH FORM -->
				
					<div class="col-md-12">
						<!-- BEGIN EXAMPLE TABLE PORTLET-->
							<div class="portlet-title">
								<div class="caption">
									<i class="fa fa-edit"></i><s:message code="system.management.roles.tablename" />
								</div>
								<div class="actions">									
								    <a class="btn btn-default btn-sm" data-toggle="modal" href="#add_role"><i class="fa fa-plus"></i><s:message code="all.table.add" /></a>
										<a class="btn default" href="#" data-toggle="dropdown">
										Columns <i class="fa fa-angle-down"></i>
										</a>
										<div id="column_toggler" class="dropdown-menu hold-on-click dropdown-checkboxes pull-right">
											<label><input type="checkbox" checked data-column="0">Checkbox</label>
											<label><input type="checkbox" checked data-column="1"><s:message code="system.management.user.searchform.id"/></label>
											<label><input type="checkbox" checked data-column="2"><s:message code="system.management.roles.search.rolename" /></label>											
											<label><input type="checkbox" checked data-column="3"><s:message code="system.management.roles.parentid" /></label>
											<label><input type="checkbox" checked data-column="4"><s:message code="system.management.user.searchform.status"/></label>
											<label><input type="checkbox" checked data-column="5"><s:message code="system.management.roles.roleright" /></label>
										</div>
									</div>								    																
								</div>
							</div>							
							<div class="portlet-body">																
								<table class="table table-striped table-hover table-bordered" id="roles_table">
									<thead>
										<tr>
											<th class="table-checkbox">
												<input type="checkbox" class="group-checkable" data-set="#roles_table .checkboxes"/>
											</th>
											<th><s:message code="system.management.user.searchform.id"/></th>											
											<th><s:message code="system.management.roles.search.rolename" /></th>											
											<th><s:message code="system.management.roles.parentid" /></th>											
											<th><s:message code="system.management.user.searchform.status"/></th>	
											<th><s:message code="system.management.roles.roleright" /></th>										
										</tr>
									</thead>
																						
								</table>
							</div>
						</div>
						<!-- END EXAMPLE TABLE PORTLET-->
					</div>
				</div>
				<!-- END PAGE CONTENT -->
				
				<div class="modal" id="add_role" tabindex="-1" data-width="760">
					<div class="modal-header">
						<button id="closeAddModal" type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
						<h4 class="modal-title"><s:message code="system.management.addrole.title" /></h4>
					</div>
					<div id="addFormMsg"></div>
					<!-- <div class="modal-body"> -->
					<div class="portlet-body form">
						<!-- BEGIN FORM	-->					
						<form id="addRoleForm" action="addRole" method="post" name="addRoleForm" class="form-horizontal form-bordered">
							<div class="form-body">
								<div class="alert alert-danger display-hide">
									<button class="close" data-close="alert"></button>
									<s:message code="system.management.user.adduser.message"/>
								</div>								
								<div class="form-group">
									<label class="control-label col-md-3"><s:message code="system.management.roles.search.rolename" /><span class="required"> * </span></label>
									<div class="col-md-9">										
										<input name="roleName" class="form-control"/>										
									</div>
								</div>								
								<div class="form-group">
									<label class="control-label col-md-3"><s:message code="system.management.roles.parentid" /><span class="required">* </span></label>
									<div class="col-md-9">
										<input name="pid" class="form-control"/>									
									</div>
								</div>								
								<%-- <div class="form-group">
									<label class="control-label col-md-3"><s:message code="system.management.user.searchform.status"/><span class="required">* </span></label>
									<div class="col-md-9">										
										<div class="radio-list">
											<label class="radio-inline">
											<input type="radio" name="status" value="true" checked/>true </label>
											<label class="radio-inline">
											<input type="radio" name="status" value="false"/>false </label>
										</div>
									</div>
								</div>	 --%>														
								
							</div>
							<div class="form-actions" style="border-top:0;">
								<div class="row">
									<div class="col-md-offset-6 col-md-6">
										<button type="submit" class="btn green" id="addFormSubmit"><i class="fa fa-check"></i> Submit</button>
										<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
									</div>
								</div>
							</div>
						</form>
						<!-- END FORM-->
					</div>					
				</div>				
				<!-- END ADD MODAL FORM-->
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
						<h4 class="modal-title"><s:message code="system.management.editrole.title"/></h4>
					</div>
					<div id="editFormMsg"></div>
					<!-- <div class="modal-body"> -->
						<!-- BEGIN FORM-->						
						<form id="editRoleForm" action="editRole" method="post" name="editRoleForm" class="form-horizontal form-bordered">
							<input name="roleId" type="hidden" value=""/>
							<div class="form-body">
								<div class="alert alert-danger display-hide">
									<button class="close" data-close="alert"></button>
									<s:message code="system.management.user.adduser.message"/>
								</div>								
								<div class="form-group">
									<label class="control-label col-md-3"><s:message code="system.management.roles.search.rolename" /><span class="required"> * </span></label>
									<div class="col-md-9">										
										<input name="roleName" class="form-control"/>										
									</div>
								</div>								
								<div class="form-group">
									<label class="control-label col-md-3"><s:message code="system.management.roles.parentid" /><span class="required"> * </span></label>
									<div class="col-md-9">
										<input name="pid" class="form-control"/>									
									</div>
								</div>								
								<div class="form-group">
									<label class="control-label col-md-3"><s:message code="system.management.user.searchform.status"/><span class="required"> * </span></label>
									<div class="col-md-9">										
										<div class="radio-list">
											<label class="radio-inline">
											<input type="radio" name="status" value="true" checked/>Active </label>
											<label class="radio-inline">
											<input type="radio" name="status" value="false"/>Inactive </label>
										</div>
									</div>
								</div>														
								
							</div>
							<div class="form-actions" style="border-top:0;">
								<div class="row">
									<div class="col-md-offset-6 col-md-6">
										<button type="submit" class="btn green" id="editFormSubmit"><i class="fa fa-check"></i> Submit</button>
										<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
									</div>
								</div>
							</div>
						</form>
						<!-- END FORM-->
					</div>					
				</div>				
				<!-- END EDIT MODAL FORM-->
				
				<!-- BEGIN Edit Role Rights MODAL FORM-->
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
						<h4 class="modal-title"><s:message code="system.management.assign.title"/></h4>
					</div>
					<div id="editRoleRightsFormMsg"></div>
					<!-- <div class="modal-body"> -->
						<!-- BEGIN FORM-->						
						<form id="editRoleRightsForm" action="editRoleRights" method="post" name="editRoleRightsForm" class="form-horizontal form-bordered">
							<input name="roleId" type="hidden" value=""/>							
							<div class="form-body">
								<div class="alert alert-danger display-hide">
									<button class="close" data-close="alert"></button>
									<s:message code="system.management.user.adduser.message"/>
								</div>
							<c:forEach var="rightsGroup" items="${rightsList}" varStatus="status">
								<div class="form-group">
									<label class="control-label col-md-3">${rightsGroup.key} </label>
									<div class="col-md-9">
									<c:forEach var="rights" items="${rightsGroup.value}" varStatus="status">
										<c:if test="${(status.index+1)%3==1}">										
										</c:if>																			
											<label class="checkbox-inline">
											<input type="checkbox" name="rights" value="${rights.bitFlag}"/>${rights.name}</label>
										<c:if test="${(status.index+1)%3==0||status.last}">
										</c:if>																			
									</div>
							</c:forEach>
							</div>
							<div class="form-actions" style="border-top:0;">
								<div class="row">
									<div class="col-md-offset-6 col-md-6">
										<button type="submit" class="btn green" id="assignFormSubmit"><i class="fa fa-check"></i> Submit</button>
										<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
									</div>
								</div>
							</div>
						</form>
						<!-- END FORM-->
					</div>					
				</div>				
				<!-- END EDIT Role Rights MODAL FORM-->
				
				<!-- BEGIN DELETE MODAL FORM-->
				<div class="modal" id="delete_roles" tabindex="-1" data-backdrop="static" data-keyboard="false">
					<div class="modal-body">
						<p>
							<s:message code="system.management.user.deletemessage" />
						</p>
					</div>
					<div class="modal-footer">
						<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>
						<button id="deleteBtn" type="button" data-dismiss="modal" class="btn blue">Confirm</button>
					</div>					
				</div>				
				<!-- END DELETE MODAL FORM-->
			</div>		
		</div>
	</div>	
	<!-- END CONTAINER -->
	<!-- BEGIN FOOTER -->
	<!-- END FOOTER -->
	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
	<!-- BEGIN CORE PLUGINS -->
	<!--[if lt IE 9]>
	<script src="<c:url value="/"/>assets/global/plugins/respond.min.js"></script>
	<script src="<c:url value="/"/>assets/global/plugins/excanvas.min.js"></script> 
	<![endif]-->
	<script src="<c:url value="/"/>assets/global/plugins/jquery-1.11.0.min.js" type="text/javascript"></script>
	<!-- IMPORTANT! Load jquery-ui-1.10.3.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
	<script src="<c:url value="/"/>assets/global/plugins/jquery.cokie.min.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>assets/global/plugins/jquery-validation/js/jquery.validate.min.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>assets/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js" type="text/javascript"></script>
	<!-- BEGIN PAGE LEVEL PLUGINS -->
	<script src="<c:url value="/"/>assets/global/plugins/select2/select2.min.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>assets/global/plugins/datatables/media/js/jquery.dataTables.js" type="text/javascript"></script>
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<script src="<c:url value="/"/>assets/global/plugins/json/json2.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>assets/admin/layout/scripts/layout.js" type="text/javascript"></script>	
	<script>
	jQuery(document).ready(function() {       
	   Metronic.init(); // init metronic core components
	   //Demo.init(); // init demo features
	   RolesTable.init("<c:url value="/"/>");	   
	</script>
<!-- END BODY -->
