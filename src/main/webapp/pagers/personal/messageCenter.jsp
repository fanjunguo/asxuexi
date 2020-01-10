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
<title>我的消息</title>
<link rel="stylesheet"
	href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" />
<link rel="stylesheet" href="css/easy-ui/themes/metro/easyui.css">
<link rel="stylesheet" href="css/easy-ui/themes/icon.css">
<link rel="stylesheet" href="css/headerStyle.css" />
<link rel="stylesheet" href="css/personal/personalMenu.css" />
<link rel="stylesheet" href="css/footer.css" />
<link rel="stylesheet" href="css/personal/messageCenter.css" />
<script src="js/jquery-3.3.1.min.js"></script>
<script
	src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="js/easy-ui/jquery.easyui.min.js"></script>
<script src="js/easy-ui/locale/easyui-lang-zh_CN.js"></script>
<script src="js/login/md5.js"></script>
<script src="js/authorityFilter.js"></script>
<script src="js/personal/messageCenter.js"></script>
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
			<div class="tab_container">
				<div class="tab selected">全部消息</div>
				<div class="tab">未读消息</div>
			</div>
			<div class="tab_content">
				<div class="button_group">
					<button class="read btn btn-primary disabled">标记已读</button>
					<button class="delete btn btn-danger disabled">删除消息</button>
				</div>
				<table id="message_grid"></table>
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
					<div class="warn_content">
						<p>确定删除选中的消息？</p>
					</div>
					<div class="warn_buttons">
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						<button type="button" class="btn btn-success">确定</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="notice_modal" tabindex="-1" role="dialog"
		aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-body">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title">提示</h4>
					<div class="notice_content">
						<p>操作失败，请重试。</p>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src="js/footer.js"></script>
</body>
</html>