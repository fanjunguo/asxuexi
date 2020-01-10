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
<title>排课系统</title>
<link rel="stylesheet"
	href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" />
<link rel="stylesheet" href="css/easy-ui/themes/metro/easyui.css">
<link rel="stylesheet" href="css/easy-ui/themes/icon.css">
<link rel="stylesheet" href="css/headerStyle.css" />
<link rel="stylesheet" href="css/org/orgMenu.css" />
<link rel="stylesheet" href="css/footer.css" />
<link rel="stylesheet" href="css/org/courseArrangement.css" />
<script src="js/jquery-3.3.1.min.js"></script>
<script
	src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="js/easy-ui/jquery.easyui.min.js"></script>
<script src="js/easy-ui/locale/easyui-lang-zh_CN.js"></script>
<script src="js/login/md5.js"></script>
<script src="js/authorityFilter.js"></script>
<script src="js/validate/jquery.validate.pack.js"></script>
<script src="js/org/courseArrangement.js"></script>
<script src="js/org/courseArrangement-courseClass.js"></script>
<script src="js/org/courseArrangement-courseStudent.js"></script>
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
			<div class="title">排课系统</div>
			<div class="course_container">
				<div class="top_side">
					<div class="course_list">
						<table id="course_grid"></table>
					</div>
				</div>
				<div class="bottom_side"></div>
			</div>
			<div class="modal fade" id="add_modal" tabindex="-1" role="dialog"
				aria-hidden="true" data-backdrop="static">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-body">
							<button type="button" class="close" data-dismiss="modal"
								aria-hidden="true">×</button>
							<h4 class="modal-title"></h4>
							<div class="content_wrap"></div>
							<div class="buttons">
								<button type="button" class="btn btn-default"
									data-dismiss="modal">取消</button>
								<button type="button" class="btn btn-success">确定</button>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="modal fade" id="warn_modal" tabindex="-1" role="dialog"
				aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-body">
							<button type="button" class="close" data-dismiss="modal"
								aria-hidden="true">×</button>
							<h4 class="modal-title">提示</h4>
							<div class="warn_content"></div>
							<div class="buttons">
								<button type="button" class="btn btn-default"
									data-dismiss="modal">取消</button>
								<button type="button" class="btn btn-success">确定</button>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="modal fade" id="edit_modal" tabindex="-1" role="dialog"
				aria-hidden="true" data-backdrop="static">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-body">
							<button type="button" class="close" data-dismiss="modal"
								aria-hidden="true">×</button>
							<h4 class="modal-title">选择班级</h4>
							<div class="edit_content"></div>
							<div class="buttons">
								<button type="button" class="btn btn-default"
									data-dismiss="modal">取消</button>
								<button type="button" class="btn btn-success">确定</button>
							</div>
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>
	<script src="js/footer.js"></script>
</body>
</html>