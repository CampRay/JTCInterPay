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
<title>應用管理中心</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0" name="viewport"/>
<meta content="" name="description"/>
<meta content="" name="author"/>
<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="<c:url value="/"/>assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
<link href="<c:url value="/"/>assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css"/>
<link href="<c:url value="/"/>assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="<c:url value="/"/>assets/global/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
<link href="<c:url value="/"/>assets/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css" rel="stylesheet" type="text/css"/>
<!-- END GLOBAL MANDATORY STYLES -->

<!-- BEGIN THEME STYLES -->
<link href="<c:url value="/"/>assets/global/css/components.css" rel="stylesheet" type="text/css"/>
<link href="<c:url value="/"/>assets/global/css/plugins.css" rel="stylesheet" type="text/css"/>
<link href="<c:url value="/"/>assets/admin/layout/css/layout.css" rel="stylesheet" type="text/css"/>
<link href="<c:url value="/"/>assets/admin/layout/css/themes/light.css" rel="stylesheet" type="text/css"/>
<link href="<c:url value="/"/>static/css/frontend.css" rel="stylesheet" type="text/css"/>
<!-- END THEME STYLES -->
<link rel="shortcut icon" href="<c:url value="/"/>static/images/favicon.ico"/>
</head>
<body class="page-header-fixed page-full-width" style="background:#f4f4f4;">
	<!-- BEGIN HEADER -->
	<c:import url="/common/header"/>
	<!-- END HEADER -->
	<!-- BEGIN CONTAINER -->
	<div class="page-container">		
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">	
			<div class="page-content" style="background:#f4f4f4;">								
				<div style="padding:30px 42px 90px;position:relative;text-align:center;">
					<c:forEach var="apps" items="${appsList}">
												
														
					<div class="app-tile">
						<div class="app-tile-container" onclick="javascript:location='<c:url value="/"/>apps/app/<c:out value="${apps.app.id}"/>';">
							<h4><strong><c:out value="${apps.app.name}"/></strong></h4>
							<h5>APPID:<c:out value="${apps.app.id}"/></h5>						
						</div>
						<div class="app-tile-btns">
							<div class="app-tile-btn" style="border-bottom:dashed 1px #eee;" onclick="javascript:location='<c:url value="/"/>apps/orders/<c:out value="${apps.app.id}"/>';">
								<i class="fa fa-list-alt"></i>
								<h5><s:message code="apps.button.orders"/></h5>
							</div>
							<div class="app-tile-btn" style="border-bottom:dashed 1px #eee;border-left:dashed 1px #eee;" onclick="javascript:location='<c:url value="/"/>apps/reports/<c:out value="${apps.app.id}"/>';">
								<i class="fa fa-bar-chart-o"></i>
								<h5><s:message code="apps.button.reports"/></h5>
							</div>
							<div class="app-tile-btn" onclick="javascript:location='<c:url value="/"/>apps/channels/<c:out value="${apps.app.id}"/>';">
								<i class="fa fa-chain"></i>
								<h5><s:message code="apps.button.channels"/></h5>
							</div>
							<div class="app-tile-btn" style="border-left:dashed 1px #eee;" onclick="javascript:location='<c:url value="/"/>apps/setting/<c:out value="${apps.app.id}"/>';">
								<i class="fa fa-cogs"></i>
								<h5><s:message code="apps.button.setting"/></h5>
							</div>							
						</div>
					</div>
					</c:forEach>	
					
					
					<div class="app-tile-add">
						<div class="add-app-pannel">												
							<h3><s:message code="apps.app.add"/></h3>
						</div>
						<div class="add-app-form">
							<div class="app-form-content">
								<h4><s:message code="apps.app.add"/></h4>
								<form id="addAppForm" action="apps/addApp" method="post" name="addAppForm" class="form-horizontal form-bordered">
									<div class="form-group">
										<input id="input_name" name="name" type="text" class="form-control input-medium" placeholder="<s:message code="apps.app.name"/>"/>
										<textarea name="desc" class="form-control input-medium" rows="3" placeholder="<s:message code="apps.app.desc"/>"></textarea>
										<button id="btn_app_add" type="submit" class="btn btn-default"><s:message code="apps.button.add"/></button>
										<button id="btn_app_cancel" type="button" class="btn btn-default"><s:message code="apps.button.cancel"/></button>
									</div>
								</form>
							</div>
						</div>				
					</div>
					
				</div>											
			</div>		
		</div>
	</div>	
	<!-- END CONTAINER -->
	
	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
	<!-- BEGIN CORE PLUGINS -->
	<!--[if lt IE 9]>
	<script src="<c:url value="/"/>assets/global/plugins/respond.min.js"></script>
	<script src="<c:url value="/"/>assets/global/plugins/excanvas.min.js"></script> 
	<![endif]-->
	<script src="<c:url value="/"/>assets/global/plugins/jquery-1.11.0.min.js" type="text/javascript"></script>
	
	<!-- IMPORTANT! Load jquery-ui-1.10.3.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
	
	<script src="<c:url value="/"/>assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
	
	
	
	
	<!-- END CORE PLUGINS -->			
	
	<script>
	jQuery(document).ready(function() { 
		$(".add-app-pannel").click(function(){
			$(".add-app-form").animate({opacity: 'show'});
			$("#input_name").focus();
		});	
		
		$("#btn_app_cancel").click(function(){
			$(".add-app-form").animate({opacity: 'hide'});
		});
		$("#btn_app_add").click(function(){
			if($.trim($("#input_name").val())==""){
				$("#input_name").focus();
				return false;
			}
		});
		
     });
	</script>	
</body>
<!-- END BODY -->

</html>