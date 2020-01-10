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
<title>学生考勤</title>
<link rel="stylesheet"
	href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" />
<link rel="stylesheet" href="css/headerStyle.css" />
<link rel="stylesheet" href="css/org/orgMenu.css" />
<link rel="stylesheet" href="css/footer.css" />
<link rel="stylesheet" href="css/org/attendance.css">
<link rel="stylesheet" href="css/easy-ui/themes/metro/easyui.css">
<script src="js/jquery-3.3.1.min.js"></script>
</head>
<body>
	<script src="js/headerstyle.js"></script>
	<div class="background"></div>
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
			<table id='class_table'></table>

			<div class="table_container">
				<table id='attendance_table'></table>
				<div id="toolbar">
					<div class="toolbar save">提交</div>
					<div class="toolbar cancel">取消</div>
				</div>
			</div>

			<div class="preview_table_container">
				<table class="preview_table"></table>
				<div class="close_btn preview_close">关闭</div>
			</div>

			<div class="detail_table_container">
				学生 <span class="detail_header"></span><span> 考勤明细</span>
				<div class="detail_close"></div>
				<div class="overflow_div">
					<table class="detail_table">
						<thead>
							<tr class='table_head'></tr>
						</thead>
						<tbody>
							<tr class="table_body"></tr>
						</tbody>
					</table>
				</div>
			</div>
			
			
			<div class="modal fade" id="tip_window">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
								&times;
							</button>
							<h4 class="modal-title" id="myModalLabel">提示</h4>
						</div>
						
						<div class="modal-body"></div>

						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">确定
							</button>
						</div>
					</div>
				</div>
			</div>
			
		</div>
	</div>
	<script src="js/footer.js"></script>
</body>
<script
	src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="js/login/md5.js"></script>
<script src="js/authorityFilter.js"></script>
<script src="js/easy-ui/jquery.easyui.min.js"></script>
<script src="js/easy-ui/locale/easyui-lang-zh_CN.js"></script>
<script src="js/org/attendance.js"></script>
</html>