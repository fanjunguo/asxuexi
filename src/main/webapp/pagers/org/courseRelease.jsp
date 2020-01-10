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
<title>课程发布</title>
<link rel="stylesheet"
	href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" />
<link rel="stylesheet"
	href="css/bootstrap-datetimepicker/bootstrap-datetimepicker.css">
<link rel="stylesheet" href="css/easy-ui/themes/metro/easyui.css">
<link rel="stylesheet" href="css/easy-ui/themes/icon.css">
<link rel="stylesheet" href="css/headerStyle.css" />
<link rel="stylesheet" href="css/footer.css" />
<link rel="stylesheet" href="css/imageManager/imageManager.css" />
<link rel="stylesheet" href="css/org/courseRelease.css" />
<script src="js/jquery-3.3.1.min.js"></script>
<script
	src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="ueditor/ueditor.config.js"></script>
<script src="ueditor/ueditor.all.js"></script>
<script
	src="js/bootstrap-datetimepicker/bootstrap-datetimepicker.min.js"></script>
<script
	src="js/bootstrap-datetimepicker/bootstrap-datetimepicker.zh-CN.js"></script>
<script src="js/easy-ui/jquery.easyui.min.js"></script>
<script src="js/easy-ui/locale/easyui-lang-zh_CN.js"></script>
<script src="js/imageManager/imageManager.js"></script>
<script src="js/validate/jquery.validate.pack.js"></script>
<script src="js/org/courseRelease.js"></script>
</head>
<body>
	<script src="js/headerstyle_nosearch.js"></script>
	<input class="course_type" type="hidden" value="${courseType}">
	<div class="content course_id" id="${courseId}">
	</div>
	<script src="js/footer.js"></script>

	<div class="modal fade" id="login_modal" tabindex="-1" role="dialog"
		aria-hidden="true" data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<h4 class="modal-title">登录超时</h4>
					<div class="login_content">
						<span>&#xea08</span>
						<p>登录超时请重新登录。</p>
					</div>
					<div class="login_buttons">

						<a href="login/log_in.do" target="_blank">
							<button type="button" class="btn btn-primary"
								onclick="closeLoginModal()">在新窗口打开登录页</button>
						</a>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="package_modal" tabindex="-1" role="dialog"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title"></h4>
					<div class="package_container"></div>
					<div class="package_buttons">
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						<button type="button" class="btn btn-success">确定</button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="chapter_modal" tabindex="-1" role="dialog"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title"></h4>
					<div class="chapter_container"></div>
					<div class="chapter_buttons">
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						<button type="button" class="btn btn-success">确定</button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="jump_modal" tabindex="-1" role="dialog"
		aria-hidden="true" data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<div class="jump_content">
						保存成功！
					</div>
				</div>
			</div>
		</div>
	</div>


</body>
</html>