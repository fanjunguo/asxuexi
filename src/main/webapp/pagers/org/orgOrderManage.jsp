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
<title>订单管理</title>
<link rel="stylesheet"
	href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" />
<link rel="stylesheet" href="css/headerStyle.css" />
<link rel="stylesheet" href="css/org/orgMenu.css" />
<link rel="stylesheet" href="css/org/orgOrderManage.css">
<link rel="stylesheet" href="css/footer.css" />
<script src="js/jquery-3.3.1.min.js"></script>
<script
	src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="js/login/md5.js"></script>
<script src="js/authorityFilter.js"></script>
</head>
<body>
	<script src="js/headerstyle.js"></script>
	<div class="content">
		<div class="left_content">
			<script src="js/org/orgMenu.js"></script>
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
			<div class="modal fade" id="cancel_confirm_window" data-backdrop="static">
			    <div class="modal-dialog">
			        <div class="modal-content">
			            <div class="modal-header">
			                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			                请选择取消原因
			            </div>
			            <div class="modal-body">
							<form id="cancel_reason">
								<p><input type="radio" class="cancel_reason_radio" name="cancelReason" value="21">与用户协商,取消订单</p>
								<p><input type="radio" class="cancel_reason_radio"  name="cancelReason" value="22">招生名额已满</p>
								<p><input type="radio" class="cancel_reason_radio"  name="cancelReason" value="23">课程已停课</p>
								<p><input type="radio" class="cancel_reason_radio"  name="cancelReason" value="24">其他原因</p>
							</form>
			            </div>
			            <div class="modal-footer">
			            	<button type="button" class="btn btn-primary">确定</button>
			                <button type="button" id="cancel" class="btn btn-default" data-dismiss="modal">关闭</button>
			            </div>
			        </div>
			    </div>
			</div>
			<div class="table_container">
				<table class="order_table">
					<thead>
						<tr>
							<td class="order_info">订单信息</td>
							<td class="student_name">学生信息</td>
							<td class="price">收费方式</td>
							<td class="order_status">交易状态</td>
							<td class="operation">操作</td>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
	<script src="js/footer.js"></script>
</body>
<script type="text/javascript" src="js/org/orgOrderManage.js"></script>
</html>