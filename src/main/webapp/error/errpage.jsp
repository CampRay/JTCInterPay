<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!--> <html lang="en" class="no-js"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8"/>
<title>Rights List</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0" name="viewport"/>
<meta content="" name="description"/>
<meta content="" name="author"/>
<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="<c:url value="/"/>assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
<link href="<c:url value="/"/>assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css"/>
<link href="<c:url value="/"/>assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>


<!-- END GLOBAL MANDATORY STYLES -->

<!-- BEGIN THEME STYLES -->
<link href="<c:url value="/"/>assets/global/css/components.css" rel="stylesheet" type="text/css"/>

<link href="<c:url value="/"/>assets/admin/layout/css/layout.css" rel="stylesheet" type="text/css"/>
<link id="style_color" href="<c:url value="/"/>assets/admin/layout/css/themes/light.css" rel="stylesheet" type="text/css"/>
<!-- END THEME STYLES -->
<link rel="shortcut icon" href="<c:url value="/"/>static/images/favicon.ico"/>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="page-header-fixed page-full-width">
	<!-- BEGIN HEADER -->
	<c:import url="/common/header"/>
	<!-- END HEADER -->
	<!-- BEGIN CONTAINER -->
	<div class="page-container">		
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">	
			<div class="page-content">								
				<!-- BEGIN PAGE CONTENT-->
				<div class="row">
					<div class="col-md-12">
						<!-- BEGIN EXAMPLE TABLE PORTLET-->
						<div class="portlet box yellow">
							<div class="portlet-title">
								<div class="caption">
									<i class="fa fa-bolt"></i>Error messages
								</div>								
							</div>							
							<div class="portlet-body">																
								<div class="alert alert-block alert-danger fade in">
									<h2 class="alert-heading">Error!</h2>
									<h3>You have not rights to access this function.</h3>
								</div>
							</div>
						</div>						
					</div>
				</div>
				<!-- END PAGE CONTENT -->
								
			</div>		
		</div>
	</div>	
	<!-- END CONTAINER -->
	<!-- BEGIN FOOTER -->
	<c:import url="/common/footer"/>
	<!-- END FOOTER -->
	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
	<!-- BEGIN CORE PLUGINS -->
	<!--[if lt IE 9]>
	<script src="<c:url value="/"/>assets/global/plugins/respond.min.js"></script>
	<script src="<c:url value="/"/>assets/global/plugins/excanvas.min.js"></script> 
	<![endif]-->
	<script src="<c:url value="/"/>assets/global/plugins/jquery-1.11.0.min.js" type="text/javascript"></script>	

	<script src="<c:url value="/"/>assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
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