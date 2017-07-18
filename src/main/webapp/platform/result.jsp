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

<head>
<meta charset="utf-8"/>
<title>JTC Payment Platform</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta content="JTC Payment Platform" name="description"/>
<link href="<%=rootPath %>/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="<%=rootPath %>/static/css/platform.css" rel="stylesheet" type="text/css"/>
<link rel="shortcut icon" href="<%=rootPath %>/static/images/favicon.ico"/>
</head>

<body>

<div class="container content">
	<div class="row content-title">		
	</div>
	<div class="row content-body">
		<c:if test="${not empty msg}">
			<div class="row">
				<div class="col-md-12"><h4 class="text-danger">${msg}</h4></div>
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

</body>

</html>