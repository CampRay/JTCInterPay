<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%
HttpServletRequest httpRequest=(HttpServletRequest)request; 
String rootPath = request.getScheme()+"://" + request.getServerName()+ ( request.getServerPort()==80?"":(":"+ request.getServerPort()))+request.getContextPath(); 
%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8"/>
<title>JTC Payment Platform</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta content="JTC Payment Platform" name="description"/>
<link href="<%=rootPath %>/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="<%=rootPath %>/static/css/platform.css" rel="stylesheet" type="text/css"/>
<!-- END THEME STYLES -->
<link rel="shortcut icon" href="<%=rootPath %>/static/images/favicon.ico"/>
</head>

<body>

<div class="container content">
	<div class="row content-title">
		<!-- <div class="col-md-12 "><h3>JTC 支付平臺</h3></div> -->
	</div>
	<div class="row content-body">
		<c:if test="${not empty errorMsg}">
			<div class="row">
				<div class="col-md-12"><h4 class="text-danger">${errorMsg}</h4></div>
			</div>		
		</c:if>
		<c:if test="${empty errorMsg}">
		<div class="col-md-12">			
			<div class="row">
				<div class="col-md-12">
					<table width="100%">
						<tr>
							<td>
								<div style="margin:5px 0;"><div class="info-lable">Amount 支付金額：</div><div class="info-value">${symbols}${order.orderAmount}</div></div>								
							</td>
						</tr>
						<tr>	
							<td>
								<div style="margin:5px 0;"><div class="info-lable">Vendor 應用名稱：</div><div class="info-value">${order.app.name}</div></div>								
							</td>
						</tr>
						<tr>	
							<td>
								<div style="margin:5px 0;"><div class="info-lable">Order no. 訂單編號：</div><div class="info-value">${order.orderNo}</div></div>								
							</td>
						</tr>
					</table>
					
				</div>
			</div>
			<div class="row select-title">
				<div class="col-md-12"><h5><strong>Choose Payment Method 選擇支付方式：</strong></h5>
				<div style="height:1px;background-color:#206fd2;margin:5px 0;"></div>
				</div>				
			</div>
			<form id="selectForm" action="<%=rootPath %>/platform/pay" method="post">
			<input type="hidden" name="id" value="${order.id}">
			<input type="hidden" name="orderNo" value="${order.orderNo}">
			<input type="hidden" name="orderTitle" value="${order.orderTitle}">
			<input type="hidden" name="orderDesc" value="${order.orderDesc}">
			<input type="hidden" name="custom" value="${order.custom}">
			<input type="hidden" name="orderAmount" value="${order.orderAmount}">
			<input type="hidden" name="currencyCode" value="${order.currencyCode}">
			<input type="hidden" name="countryCode" value="${order.countryCode}">
			<input type="hidden" name="app.id" value="${order.app.id}">			
			<input type="hidden" name="clientIp" value="${order.clientIp}">
			<input type="hidden" name="returnUrl" value="${order.returnUrl}">
			<input type="hidden" name="notifyUrl" value="${order.notifyUrl}">	
			<div class="row">
				<div class="col-md-12">
					<c:forEach var="appChannel" items="${appChannelsList}">
					<div class="col-lg-2 col-md-3 col-xs-4"><div class="btn-channel ${appChannel.channel.code}"><input type="radio" name="channel.id" style="width:18px;height:38px;" value="${appChannel.channel.id}"/></div></div>														
					</c:forEach>
				</div>
			</div>	
			<div class="row content-bottom">
				<div class="col-xs-3 col-xs-offset-9">					
																	
						
						<button type="submit" class="col-md-12 button btn-blue">Next</button>
					
				</div>
			</div>	
			</form>					
		</div>
		</c:if>
	</div>
	<div class="row content-footer">
		<div class="col-md-12">
			<div class="footer-image"></div>
			<div class="footer-text">
			Payment service provided by JTC 
			</div>
		</div>		
	</div>							
</div>
<!-- END LOGIN -->


<script src="<%=rootPath %>/assets/global/plugins/jquery-1.11.0.min.js" type="text/javascript"></script>

<!-- IMPORTANT! Load jquery-ui-1.10.3.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->

<script src="<%=rootPath %>/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>

<script>
jQuery(document).ready(function() { 
	$(".btn-channel").click(function(){		
		$(this).children().attr('checked','true');
	});	
	
	$("#selectForm").on("submit",function(){		
		if($(".btn-channel input:checked").length>0){			
			return true;
		}
		else{
			alert("请選擇一個支付方式！");
			return false;
		}
	});
});
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>