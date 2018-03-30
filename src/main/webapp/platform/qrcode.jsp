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
<title>扫码支付</title>
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
				<div class="col-md-12"><h5><strong>打開手機支付寶掃描下方二維碼  Open your mobile AliPay and Scan this QRCode</strong></h5>
				<div style="height:1px;background-color:#206fd2;margin:5px 0;"></div>
				</div>				
			</div>
			<div class="row">
				<div class="col-md-12">
					<div style="width:200px; height:30px;margin:30px auto 0;text-align: center;">	
					掃一掃付款		
					</div>					
				</div>
			</div>	
			<div class="row">
				<div class="col-md-12">
					<div style="width:200px; height:30px;margin:0 auto 10px;text-align: center;color:orange;font-size:18px;">	
					 HK$:${amount}元		
					</div>					
				</div>
			</div>	
			<div class="row">
				<div class="col-md-12">
					<div style="width:204px; height:204px;margin:0px auto 50px;padding:2px;border:1px solid black;">
					<img src="data:image/png;base64,${qrcode}" width="200" height="200"/>
					<img src="<%=rootPath %>/static/images/${channel}_small.png" width="30" height="30" style="position: absolute;top: 50%;left: 50%;margin-left: -12px;margin-top: -35px;"/>
					</div>
					
				</div>
			</div>	
						
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

  
var maxtime = 0; //3分钟，按秒计算，自己调整!
function CountDown(){	
	if(maxtime<180){            
       maxtime=maxtime+3;
       $.post("<c:url value="/"/>syncnotify/apmp", { "txnId": "${txnId}", "orderId": "${orderId}", "batchNo": "${batchNo}","traceNo": "${traceNo}","transType": "${transType}","apiUrl": "${apiUrl}" },
  		   function(data){
  		     if(data.status){
  		    	location.href="<c:url value="/"/>syncnotify/apmp_return?orderId=${orderId}";
  		    	clearInterval(timer);
  		     }
  	       },"json"
       );
    }else{
       clearInterval(timer);
       alert("支付掃碼時間已經超過3分鐘，請重新選擇支付！");
       history.back();
    }
}
timer = setInterval("CountDown()",3000);

</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>