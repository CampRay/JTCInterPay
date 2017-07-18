<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="cureReqUri" value="${pageContext.request.requestURI}"/>

<c:set var="classStr1" value=""/>
<c:set var="classStr2" value=""/>
<c:set var="classStr3" value=""/>
<c:set var="classStr4" value=""/>
<c:set var="classStr5" value=""/>
<c:set var="path1" value="${contextPath}/frontend/app.jsp"/>
<c:set var="path2" value="${contextPath}/frontend/app_orders"/>

<c:set var="path4" value="${contextPath}/frontend/app_channel"/>
<c:set var="path5" value="${contextPath}/frontend/app_setting"/>

<c:if test="${cureReqUri.startsWith(path1)}">				
	<c:set var="classStr1" value="active"/>							
</c:if>	
<c:if test="${cureReqUri.startsWith(path2)}">				
	<c:set var="classStr2" value="active"/>							
</c:if>

<c:if test="${cureReqUri.startsWith(path4)}">				
	<c:set var="classStr4" value="active"/>							
</c:if>
<c:if test="${cureReqUri.startsWith(path5)}">				
	<c:set var="classStr5" value="active"/>							
</c:if>
	
    <!-- BEGIN SIDEBAR -->
	<div class="page-sidebar-wrapper">
		<!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
		<!-- DOC: Change data-auto-speed="200" to adjust the sub menu slide up/down speed -->
		<div class="page-sidebar navbar-collapse collapse">
		<!-- BEGIN SIDEBAR MENU -->        
		<ul class="page-sidebar-menu page-sidebar-menu-light" data-auto-scroll="true" data-slide-speed="200">						        											
			<li class="start ${classStr1}" style="line-height:40px;">
				<a href="<c:url value="/"/>apps/app/<c:out value="${sessionScope.appId}"/>">
					<span class="title" style="padding-left:20px;"><s:message code="app.menu.info"/></span>					
				</a>							 				
			</li>
			<li class="${classStr2}" style="line-height:40px;">
				<a href="<c:url value="/"/>apps/orders/<c:out value="${sessionScope.appId}"/>">
					<span class="title" style="padding-left:20px;"><s:message code="app.menu.orders"/></span>
					
				</a>
			</li>			
			<li class="${classStr4}" style="line-height:40px;">
				<a href="<c:url value="/"/>apps/channels/<c:out value="${sessionScope.appId}"/>">
					<span class="title" style="padding-left:20px;"><s:message code="app.menu.channels"/></span>
					
				</a>
			</li>	
			<li class="${classStr5}" style="line-height:40px;">
				<a href="<c:url value="/"/>apps/setting/<c:out value="${sessionScope.appId}"/>">
					<span class="title" style="padding-left:20px;"><s:message code="app.menu.setting"/></span>
					
				</a>	
			</li>			    
											
			
		</ul>
		<!-- END SIDEBAR MENU -->
	   </div>
    </div>    
   <!-- END SIDEBAR -->