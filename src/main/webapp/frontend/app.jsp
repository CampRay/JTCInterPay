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
<title>應用信息</title>
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
				<div class="form-horizontal">
					<div class="form-group">						
						<label class="col-md-3 control-label"><s:message code="apps.app.id"/></label>														    
						<div class="col-md-5 bg-grey"><h4>${app.appId}</h4></div>						
					</div>
					<div class="form-group">
						<label class="col-md-3 control-label"><s:message code="apps.app.name"/></label>														    
						<div class="col-md-5 bg-grey"><h4>${app.name}</h4></div>
					</div>
					<div class="form-group">
						<label class="col-md-3 control-label"><s:message code="apps.app.desc"/></label>														    
						<div class="col-md-5 bg-grey"><h4>${app.desc}</h4></div>
					</div>
					<div class="form-group">
						<label class="col-md-3 control-label"><s:message code="apps.app.token"/></label>														    
						<div class="col-md-5 bg-grey"><h4>${app.token}</h4></div>
					</div>
					<div class="form-group">
						<label class="col-md-3 control-label"><s:message code="all.table.delete"/> APP</label>														    
						<div class="col-md-2"><a href="<c:url value="/"/>apps/delete/<c:out value="${app.id}"/>" class="btn blue-hoki form-control"><s:message code="all.table.delete"/></a></div>
					</div>
				</div>
				
				
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
	
	<script>
		jQuery(document).ready(function() {

			Metronic.init(); // init metronic core components
			Layout.init(); // init current layout							
			
		});
	</script>
</body>

<!-- END BODY -->
</html>