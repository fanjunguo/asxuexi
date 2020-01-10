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
<title>我的订单</title>
<link rel="stylesheet"
	href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" />
<link rel="stylesheet" href="css/headerStyle.css" />
<link rel="stylesheet" href="css/personal/personalMenu.css" />
<link rel="stylesheet" href="css/footer.css" />
<link rel="stylesheet" href="css/personal/myOrder.css">
<link rel="stylesheet" href="css/order/simplePagination.css" />
<script src="js/jquery-3.3.1.min.js"></script>
<script
	src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="js/attendance/attendanceDetail.js"></script>
<script src="js/order/jquery.simplePagination.js"></script>
<script src="js/login/md5.js"></script>
<script src="js/authorityFilter.js"></script>
</head>
<body>
	<script src="js/headerstyle.js"></script>
	<div class="content">
		<div class="left_content">
			<script src="js/personal/personalMenu.js"></script>
			<div class="menu_content">
				<div class="menu_list">
					<div class="menu_item_empty"></div>
					<div class="menu_child_item_empty"></div>
					<div class="menu_item_empty"></div>
					<div class="menu_child_item_empty"></div>
					<div class="menu_item_empty"></div>
					<div class="menu_child_item_empty"></div>
					<div class="menu_item_empty"></div>
				</div>
			</div>
		</div>
		<div class="right_content">
			<div class="table_container">
				<table>
					<thead>
						<tr>
							<td class="thead1">订单信息</td>
							<td class="student_name">上课人</td>
							<td class="price">价格</td>
							<td class="order_status">状态</td>
							<td class="operation">操作</td>
						</tr>
					</thead>
				</table>
			</div>

		</div>
	</div>
	<div class="modal fade" id="cancel_confirm_window" data-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                请选择取消原因
	            </div>
	            <div class="modal-body">	
					<form id="cancel_reason">
						<p><input type="radio" name="cancelReason" class="cancel_reason_radio" value="11">个人原因,课程不想上了</p>
						<p><input type="radio" name="cancelReason" class="cancel_reason_radio"  value="12">重复下单</p>
						<p><input type="radio" name="cancelReason" class="cancel_reason_radio"  value="13">对课程不满意</p>
						<p><input type="radio" name="cancelReason" class="cancel_reason_radio"  value="14">对机构不满意</p>
						<p><input type="radio" name="cancelReason" class="cancel_reason_radio"  value="15">其他原因</p>
					</form>
	            </div>
	            <div class="modal-footer">
	                <button type="button" id="cancel" class="btn btn-default" data-dismiss="modal">再想想</button>
	                <button type="button" class="btn btn-primary">确定</button>
	            </div>
	        </div>
	    </div>
	</div>
	
	<div class="modal fade" id="delete_confirm" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                <h4 class="modal-title" id="myModalLabel">删除订单</h4>
	            </div>
	            <div class="modal-body">订单删除后无法恢复,请确定是否删除</div>
	            <div class="modal-footer">
	            	<button type="button" class="btn btn-primary" id="delete_sure">确定</button>
	                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	            </div>
	        </div>
	    </div>
	</div>
	<!--付款给机构的确认  -->
	<div class="modal fade" id="pay_to_org_confirm" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                <h4 class="modal-title" id="myModalLabel">付款给机构</h4>
	            </div>
	            <div class="modal-body" style="font-size: 14px;">
	            	<p>付款给机构后,您支付的款项将打给机构账户</p>
	            	<p>请确认是否继续?</p>
	            </div>
	            <div class="modal-footer">
	            	<button type="button" class="btn btn-primary" id="pay_sure">确定</button>
	                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
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

<script src="js/personal/myOrder.js"></script>
</html>