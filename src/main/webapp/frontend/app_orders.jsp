<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>

<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->

<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->

<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8" />
<title>應用訂單</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0" name="viewport" />

<meta content="" name="description" />

<meta content="" name="author" />

<!-- BEGIN GLOBAL MANDATORY STYLES -->

<link href="<c:url value="/"/>assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
<link href="<c:url value="/"/>assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css" />
<link href="<c:url value="/"/>assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="<c:url value="/"/>assets/global/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css" />
<link href="<c:url value="/"/>assets/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css" rel="stylesheet" type="text/css" />
<!-- END GLOBAL MANDATORY STYLES -->
<!-- BEGIN PAGE LEVEL STYLES -->
<link href="<c:url value="/"/>assets/global/plugins/select2/select2.css" rel="stylesheet" type="text/css" />
<link href="<c:url value="/"/>assets/global/plugins/datatables/plugins/bootstrap/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />
<link href="<c:url value="/"/>assets/global/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css" rel="stylesheet" type="text/css" />
<link href="<c:url value="/"/>assets/global/plugins/bootstrap-modal/css/bootstrap-modal.css" rel="stylesheet" type="text/css" />
<link href="<c:url value="/"/>assets/global/plugins/bootstrap-datepicker/css/datepicker.css" rel="stylesheet" type="text/css"/>
<!-- END PAGE LEVEL STYLES -->
<!-- BEGIN THEME STYLES -->
<link href="<c:url value="/"/>assets/global/css/components.css" rel="stylesheet" type="text/css" />
<link href="<c:url value="/"/>assets/global/css/plugins.css" rel="stylesheet" type="text/css" />
<link href="<c:url value="/"/>assets/admin/layout/css/layout.css" rel="stylesheet" type="text/css" />
<link id="style_color" href="<c:url value="/"/>assets/admin/layout/css/themes/light.css" rel="stylesheet" type="text/css" />
<!-- END THEME STYLES -->
<link rel="shortcut icon" href="<c:url value="/"/>static/images/favicon.ico" />

</head>

<!-- END HEAD -->

<!-- BEGIN BODY -->

<!-- DOC: Apply "page-header-fixed-mobile" and "page-footer-fixed-mobile" class to body element to force fixed header or footer in mobile devices -->
<!-- DOC: Apply "page-sidebar-closed" class to the body and "page-sidebar-menu-closed" class to the sidebar menu element to hide the sidebar by default -->
<!-- DOC: Apply "page-sidebar-hide" class to the body to make the sidebar completely hidden on toggle -->
<!-- DOC: Apply "page-sidebar-closed-hide-logo" class to the body element to make the logo hidden on sidebar toggle -->
<!-- DOC: Apply "page-sidebar-hide" class to body element to completely hide the sidebar on sidebar toggle -->
<!-- DOC: Apply "page-sidebar-fixed" class to have fixed sidebar -->
<!-- DOC: Apply "page-footer-fixed" class to the body element to have fixed footer -->
<!-- DOC: Apply "page-sidebar-reversed" class to put the sidebar on the right side -->
<!-- DOC: Apply "page-full-width" class to the body element to have full width page without the sidebar menu -->
<body class="page-header-fixed">

	<!-- BEGIN HEADER -->
	<c:import url="/common/header" />
	<!-- END HEADER -->

	<!-- BEGIN CONTAINER -->
	<div class="page-container">

		<!-- BEGIN SIDEBAR -->
		<c:import url="/apps/left" />
		<!-- END SIDEBAR -->

		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<div class="page-content">
				<!-- BEGIN PAGE TITLE & BREADCRUMB-->
				<div id="msg"></div>
				<!-- END PAGE TITLE & BREADCRUMB-->
				<!-- BEGIN SEARCH FORM -->
				<div class="portlet-body">
					<form id="searchForm" name="searchForm" action="orderlist" class="form-horizontal" method="post">						
						<div class="row">
							<div class="col-md-4 col-sm-6 col-xs-12">
								<div class="form-group">
									<div class="col-md-12">
										<input name="id" type="text" class="form-control" placeholder="<s:message code="app.order.no" />">
									</div>
								</div>
							</div>
							<div class="col-md-4 col-sm-6 col-xs-12">
								<div class="form-group">
									<div class="col-md-12">
										<div data-date-format="mm/dd/yyyy" class="input-group date date-picker">
											<input type="text" name="startTime" readonly="true" class="form-control" placeholder="<s:message code="app.order.time.start" />"/> <span class="input-group-btn">
												<button type="button" class="btn default">
													<i class="fa fa-calendar"></i>
												</button>
											</span>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md-4 col-sm-6 col-xs-12">
								<div class="form-group">
									<div class="col-md-12">
										<div data-date-format="mm/dd/yyyy" class="input-group date date-picker">
											<input type="text" name="endTime" readonly="true" class="form-control" placeholder="<s:message code="app.order.time.end" />"/> <span class="input-group-btn">
												<button type="button" class="btn default">
													<i class="fa fa-calendar"></i>
												</button>
											</span>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-4 col-sm-6 col-xs-12">
								<div class="form-group">
									<div class="col-md-12">
										<select name="channel.id" class="form-control">
											<option value=""><s:message code="app.order.channel.select" /></option>
											<c:forEach var="channel" items="${channelList}">
												<option value="${channel.id}">${channel.name}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-4 col-sm-6 col-xs-12">
								<div class="form-group">
									<div class="col-md-12">
										<select name="status" class="form-control">
											<option value=""><s:message code="app.order.status.select" /></option>
											<option value="0"><s:message code="app.order.status.process" /></option>
											<option value="1"><s:message code="app.order.status.success" /></option>
											<option value="2"><s:message code="app.order.status.failure" /></option>										
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-4 col-sm-6 col-xs-12">
								<div class="form-group">
									<div class="col-md-12">
										<button type="submit" class="btn blue">
											<s:message code="all.table.search" />
										</button>
										<button type="reset" class="btn grey-cascade">
											<s:message code="all.table.reset" />
										</button>
									</div>
								</div>
							</div>
						</div>						
					</form>

				</div>
				<!-- END SEARCH FORM -->
				<!-- END PAGE CONTENT -->
				<div class="row">
					<div class="col-md-12">
						<!-- BEGIN EXAMPLE TABLE PORTLET-->
						<div class="portlet">
							<div class="portlet-title">
								<div class="caption">
									<i class="fa fa-edit"></i><s:message code="app.order.table"/>
								</div>							
							</div>
							<div class="portlet-body">
								<table class="table table-hover table-bordered" id="orders_table">
									<thead class="bg-grey-steel">
										<tr>											
											<th><s:message code="app.order.no" /></th>
											<th><s:message code="app.order.time" /></th>
											<th><s:message code="app.order.name" /></th>
											<th><s:message code="app.order.amount" /></th>
											<th><s:message code="app.order.channel" /></th>
											<th><s:message code="app.order.status" /></th>
										</tr>
									</thead>
									<tbody></tbody>
								</table>
							</div>
						</div>
						<!-- END EXAMPLE TABLE PORTLET-->
					</div>
				</div>
				<!-- END PAGE CONTENT -->
			<!-- END PAGE CONTENT -->			
			</div>
		</div>
	</div>




	<!-- END CONTAINER -->

	<!-- BEGIN FOOTER -->
	<c:import url="/common/footer" />
	<!-- END FOOTER -->

	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->

	<!-- BEGIN CORE PLUGINS -->

	<!--[if lt IE 9]>

	<script src="<c:url value="/"/>assets/global/plugins/respond.min.js"></script>

	<script src="<c:url value="/"/>assets/global/plugins/excanvas.min.js"></script> 

	<![endif]-->

	<script src="<c:url value="/"/>assets/global/plugins/jquery-1.11.0.min.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>assets/global/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>

	<!-- IMPORTANT! Load jquery-ui-1.10.3.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
	<script src="<c:url value="/"/>assets/global/plugins/jquery-ui/jquery-ui-1.10.3.custom.min.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>assets/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>	

	<script src="<c:url value="/"/>assets/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js" type="text/javascript"></script>
	<!-- END CORE PLUGINS -->
	<script type="text/javascript" src="<c:url value="/"/>assets/global/plugins/bootstrap-wizard/jquery.bootstrap.wizard.min.js"></script>
	<!-- BEGIN PAGE LEVEL PLUGINS -->
	<script src="<c:url value="/"/>assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>assets/global/plugins/jquery-i18n/jquery.i18n.properties-1.0.9.js" type="text/javascript"></script>
	
	<script src="<c:url value="/"/>assets/global/plugins/select2/select2.min.js" type="text/javascript"></script>
	
	<script src="<c:url value="/"/>assets/global/plugins/datatables/media/js/jquery.dataTables.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>assets/global/plugins/datatables/plugins/bootstrap/dataTables.bootstrap.js" type="text/javascript"></script>
	
	<!-- END PAGE LEVEL PLUGINS -->

	<!-- BEGIN PAGE LEVEL SCRIPTS -->

	<script src="<c:url value="/"/>assets/global/plugins/json/json2.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>assets/global/scripts/metronic.js" type="text/javascript"></script>	
	<script src="<c:url value="/"/>assets/admin/layout/scripts/layout.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>static/js/common.js"></script>	
	<script src="<c:url value="/"/>static/js/appOrdersTableData.js"></script>
	<script>
		jQuery(document).ready(function() {

			Metronic.init(); // init metronic core components
			Layout.init(); // init current layout							
			AppOrdersTable.init("<c:url value="/"/>","${sessionScope.locale}");
		});
	</script>
</body>

<!-- END BODY -->


</html>