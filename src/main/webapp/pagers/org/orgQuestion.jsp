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
<title>问答模块</title>
<link rel="stylesheet"
	href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" />
<link rel="stylesheet" href="css/headerStyle.css" />
<link rel="stylesheet" href="css/org/orgMenu.css" />
<link rel="stylesheet" href="css/footer.css" />
<link rel="stylesheet" href="css/org/orgQuestion.css" />
<script src="js/jquery-3.3.1.min.js"></script>
<script
	src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="js/jquery.paging.min.js"></script>
<script src="js/login/md5.js"></script>
<script src="js/authorityFilter.js"></script>
<script src="js/org/orgQuestion.js"></script>
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
			<div class="question_container">
				<ul class="nav nav-tabs" role="tablist">
					<li class="active"><a href="#org" data-toggle="tab">机构问题</a></li>
					<li class="dropdown"><a href="#course" data-toggle="tab">课程问题</a>
					</li>
				</ul>
				<div class="tab-content">
					<div role="tabpanel" class="tab-pane active" id="org">
						<div class="btn-group question_status">
							<button type="button" class="btn btn-default dropdown-toggle"
								data-toggle="dropdown">全部</button>
							<button type="button" class="btn btn-default dropdown-toggle"
								data-toggle="dropdown">
								<span class="caret"></span>
							</button>
							<ul class="dropdown-menu" role="menu">
								<li class="active"><a>全部</a></li>
								<li><a>未解答</a></li>
								<li><a>已解答</a></li>
							</ul>
						</div>
						<div class="question_wrap"></div>
						<div class="pagination" onselectstart="return false"></div>
					</div>
					<div role="tabpanel" class="tab-pane" id="course">
						<div class="btn-group question_status">
							<button type="button" class="btn btn-default dropdown-toggle"
								data-toggle="dropdown">全部</button>
							<button type="button" class="btn btn-default dropdown-toggle"
								data-toggle="dropdown">
								<span class="caret"></span>
							</button>
							<ul class="dropdown-menu" role="menu">
								<li class="active"><a>全部</a></li>
								<li><a>未解答</a></li>
								<li><a>已解答</a></li>
							</ul>
						</div>
						<div class="question_wrap"></div>
						<div class="pagination" onselectstart="return false"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src="js/footer.js"></script>
	<div class="modal fade" id="answer_modal" tabindex="-1" role="dialog"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title">问题答复</h4>
					<div class="answer_container">
						<div>
							<textarea id="answer_content" autocomplete="off"></textarea>
						</div>
					</div>
					<div class="answer_buttons">
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						<button type="button" class="btn btn-success">确定</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade" id="stick_modal" tabindex="-1" role="dialog"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title">提示</h4>
					<div class="stick_container">
						该问题暂未回答，确定将其置顶？
					</div>
					<div class="stick_buttons">
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						<button type="button" class="btn btn-success">确定</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	
</body>
</html>