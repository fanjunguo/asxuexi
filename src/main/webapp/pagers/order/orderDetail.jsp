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
<title>订单详情</title>
<link rel="stylesheet"
	href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="css/headerStyle.css" />
<link rel="stylesheet" href="css/footer.css" />
<link rel="stylesheet" href="css/order/orderDetail.css" />
	
<script src="js/jquery-3.3.1.min.js"></script>
<script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="js/login/md5.js"></script>
<script src="js/authorityFilter.js"></script>
<script src="js/order/orderDetail.js"></script>
</head>
<body>
<script src="js/headerstyle_nosearch.js"></script>
	<div class="bg"></div>

	<div class="modal fade" id="cancel_confirm_window" data-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                请选择取消原因
	            </div>
	            <div class="modal-body">	
					<form id="cancel_reason">
						<p><input type="radio" name="cancelReason" value="11">个人原因,课程不想上了</p>
						<p><input type="radio" name="cancelReason" value="12">重复下单</p>
						<p><input type="radio" name="cancelReason" value="13">对课程不满意</p>
						<p><input type="radio" name="cancelReason" value="14">对机构不满意</p>
						<p><input type="radio" name="cancelReason" value="15">其他原因</p>
					</form>
	            </div>
	            <div class="modal-footer">
	                <button type="button" id="cancel" class="btn btn-default" data-dismiss="modal">再想想</button>
	                <button type="button" class="btn btn-primary">确定</button>
	            </div>
	        </div>
	    </div>
	</div>
	
	<div class="alert alert-warning">
		<a href="#" class="close" data-dismiss="alert">
			&times;
		</a>
		<strong>网络异常,请稍后重试</strong>
	</div>
<script src="js/footer.js"></script>
</body>
</html>