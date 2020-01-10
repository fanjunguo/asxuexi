<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>收银台</title>
<link rel="stylesheet"
	href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="css/headerStyle.css" />
<link rel="stylesheet" href="css/footer.css" />
<link rel="stylesheet" href="css/order/payment.css" />
	
<script src="js/jquery-3.3.1.min.js"></script>
<script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="js/login/md5.js"></script>
<script src="js/authorityFilter.js"></script>
</head>
<body>
<script src="js/headerstyle_nosearch.js"></script>
<div class="order_info_body">
	<div class="order-info">
		<p>报名成功!  订单号:<span id="order_id"></span></p> 
		<p>请在<em class="red">30分钟</em>内完成支付,否则订单会自动取消</p>
		<hr>
		<strong class="item_title">订单信息</strong>
		<div class="span_container">
			课程名称:<span class="course-name"> </span>
			<span class="price">支付金额 <em id="order_price"></em></span>
		</div>
		<div class="span_container">
			套餐: <span class="package-name"></span>
			<span class="package-info"></span>
		</div>
	</div>
	<hr>
	<div class="student-info">
		<strong class="item_title">上课人信息</strong>
		<div class="span_container">
			上课人: <span class="student-name"></span>
		</div>	
	</div>
	<hr>
	<div class="payment-container">
		<strong class="item_title">选择支付方式</strong>
		<div class="img_container">
			<img class="payment_icon active" src="img/payment/alipay.png">
			<img class="payment_icon" src="img/payment/wechat.png">
		</div>
	</div>
	<div class="button_container">
		<div class="submit">确认报名</div>
	</div>

	<div class="modal fade" id="payment_window" data-backdrop="static">
	    <div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="myModalLabel">
						支付结果
					</h5>
				</div>
				<div class="modal-body">
					<div class="payment_result success">支付成功,查看详情</div>
					<div class="payment_result failure">支付失败,重新支付</div>
				</div>
			</div>
		</div>
	</div>

</div>
<div class="error">
	<img src="img/payment/alert.png">出错了!请稍后再试
</div>

<script src="js/footer.js"></script>
</body>
<script src="js/order/payment.js"></script>

</html>