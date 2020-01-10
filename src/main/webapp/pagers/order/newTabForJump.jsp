<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script src="js/jquery-3.3.1.min.js"></script>
<script type="text/javascript">
	let href=window.location.href;
	let orderId=href.substring(href.lastIndexOf('=')+1);
	$.post('order/alipay.action', {orderId: orderId}, function(data) {	
		$('body').append(data);
	});
</script>
</head>
<body>

</body>
</html>