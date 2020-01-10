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
<title>支付结果</title>
<link rel="stylesheet"
	href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="css/headerStyle.css" />
<link rel="stylesheet" href="css/footer.css" />
<link rel="stylesheet" href="css/order/paymentResult.css">
<script src="js/jquery-3.3.1.min.js"></script>
<script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="js/login/md5.js"></script>
<script src="js/authorityFilter.js"></script>

</head>
<body>
<script src="js/headerstyle_nosearch.js"></script>
<div class="container">
	<div class="result">
		<img src="img/payment/success.png" class="icon"><p id="message">恭喜您,报名成功!</p>
	</div>
	<div class="order_info">
		订单号: <span class="order_id"></span>
	</div>
	<div class="link_btn">
		<a href="personal/myorder.do">查看订单</a> |
		<a href="">返回首页</a>
	</div>
</div>
<div class="error_container">
	<img src="img/payment/alert.png">系统繁忙,请稍后再试
</div>

<script src="js/footer.js"></script>
</body>
<script src="js/order/paymentResult.js"></script>
</html>