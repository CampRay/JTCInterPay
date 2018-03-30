<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%
HttpServletRequest httpRequest=(HttpServletRequest)request; 
String rootPath = request.getScheme()+"://" + request.getServerName()+ ( request.getServerPort()==80?"":(":"+ request.getServerPort())); 
%>
<!DOCTYPE html>

<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->

<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->

<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8" />
<title>渠道配置</title>
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
<link href="<c:url value="/"/>static/css/frontend.css" rel="stylesheet" type="text/css"/>
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
				<div class="page-bar">
				<h4 class="page-breadcrumb"><s:message code="app.channel.title"/></h4>
				</div>
				<div class="row">
				<div class="col-md-12">
					<div class="portlet box green-haze">
						<div class="portlet-title">
							<div class="caption">
								<span class="caption-subject bold">${appChannel.channel.name }</span>
								<c:if test="${appChannel.channel.status}">				
									<span>(<s:message code="app.channels.enable"/>)</span>							
								</c:if>	
								<c:if test="${!appChannel.channel.status}">				
									<span>(<s:message code="app.channels.disable"/>)</span>						
								</c:if>	 
								
							</div>
							<div class="actions"><a class="btn btn-green"></a></div>
						</div>
						<div class="portlet-body form">
							<form class="form-horizontal" action="<c:url value="/"/>apps/channelSetting" id="channelSettingForm" method="post" name="channelSettingForm">
								<input type="hidden" class="form-control" name="id" value="${appChannel.id}"/>
								<input type="hidden" class="form-control" id="isSandbox" name="channelSetting.sandbox" value="${appChannel.channelSetting.sandbox}"/>
								<input type="hidden" class="form-control" name="app.id" value="${appChannel.app.id}"/>
								<input type="hidden" class="form-control" name="channel.id" value="${appChannel.channel.id}"/>
								<div class="form-body">	
									<div class="form-group">
										<label class="col-md-3 control-label"><h5>Sandbox Model</h5></label>
										<div class="col-md-5">
											<c:if test="${appChannel.channelSetting.sandbox}">
												<input type="checkbox" id="checkSandbox" name="channelSettingSandbox" checked="checked"/>
											</c:if>
											<c:if test="${!appChannel.channelSetting.sandbox}">
												<input type="checkbox" id="checkSandbox" name="channelSettingSandbox"/>
											</c:if>											
											<span class="help-block">此處配置是否啟用PayPal測試模式</span>
										</div>
									</div>								
								    <div class="form-group">
										<label class="col-md-3 control-label"><h5><s:message code="app.channel.paypal.businessEmail"/></h5></label>
										<div class="col-md-5">
											<input type="text" class="form-control" name="channelSetting.businessEmail" value="${appChannel.channelSetting.businessEmail }"/>
											<span class="help-block"><s:message code="app.channel.paypal.businessEmail.hint"/></span>
										</div>
									</div>
									<div class="form-group">
										<label class="col-md-3 control-label"><h5><s:message code="app.channel.paypal.token"/></h5></label>
										<div class="col-md-5">
											<input type="text" class="form-control" name="channelSetting.token" value="${appChannel.channelSetting.token }"/>
											<span class="help-block"><s:message code="app.channel.paypal.token.hint"/></span>
										</div>
									</div>
									<div class="form-group">
										<label class="col-md-3 control-label"><h5><s:message code="app.channel.paypal.returnURL"/></h5></label>
										<div class="col-md-5">
											<div class="col-md-12 form-control-static bg-grey">
												<input type="hidden" class="form-control" name="channelSetting.returnURL" value="<%=rootPath%><c:url value="/syncnotify/"/>${appChannel.channel.code}"/>
												<h5><%=rootPath%><c:url value="/syncnotify/"/>${appChannel.channel.code}</h5>
											</div>
											<span class="help-block"><s:message code="app.channel.paypal.returnURL.hint"/></span>
										</div>
									</div>
									<div class="form-group">
										<label class="col-md-3 control-label"><h5><s:message code="app.channel.paypal.ipnURL"/></h5></label>
										<div class="col-md-5">
											<div class="col-md-12 form-control-static bg-grey">
												<input type="hidden" class="form-control" name="channelSetting.ipnURL" value="<%=rootPath%><c:url value="/asyncnotify/"/>${appChannel.channel.code}"/>
												<h5><%=rootPath%><c:url value="/asyncnotify/"/>${appChannel.channel.code}</h5>
											</div>
											<span class="help-block"><s:message code="app.channel.paypal.ipnURL.hint"/></span>
										</div>
									</div>
									<div class="form-group">
										<div class="col-md-5 col-md-offset-3">
											<input type="submit" class="btn green" value="<s:message code="apps.button.save"/>" class="form-control"/>
											<a href="<c:url value="/"/>apps/channels/${appChannel.app.id}" class="btn default"><s:message code="apps.button.cancel"/></a>
										</div>
									</div>
								</div>
							</form>
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
			
			$("#checkSandbox").change(function(){
				$("#isSandbox").val($("#checkSandbox").is(':checked'));
			});
		});
	</script>
</body>

<!-- END BODY -->


</html>