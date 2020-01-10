<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html >
<html>
<head>
<base href="<%=basePath%>" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>个人问答</title>
<link rel="stylesheet" href="css/JQPaginate/JQPaginate.css">
<link rel="stylesheet" href="css/personal/askAnswer.css">
<link rel="stylesheet"
	href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="css/headerStyle.css" />
<link rel="stylesheet" href="css/personal/personalMenu.css" />
<link rel="stylesheet" href="css/footer.css" />
<script src="js/jquery-3.3.1.min.js"></script>
<script
	src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="js/login/md5.js"></script>
<script src="js/authorityFilter.js"></script>
<script src="js/personal/askAnswer.js"></script>
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
			<div class="body">
				<div class="titlea">
					<h3>问答模块</h3>
				</div>
				<hr>
				<div class="bodyContext">
					<div class="titleB" id="sortQuestion">
						<ul>
							<li class="active" id="org">对课程的提问</li>
							<li id="course">对机构的提问</li>
						</ul>
					</div>

					<div class="answerBody">
						<div class="courseAnswer">

							<div class="showCourseed">

								<div class='questionAnswerInfo'>
									<span></span><br>
									<div class="question">
										<span></span> <span class="questionTime"></span>

									</div>
									<div class="answer">
										<span></span> <span class="answerTime"></span>
									</div>
								</div>

							</div>
						</div>
						<div class="QAAPager">
							<ul class="paginator" onselectstart="return false;">
								<li class="previous successive" id="previous"><span><</span></li>
								<li class="current"><span>1</span></li>
								<li class="page-link"><span>2</span></li>
								<li class="page-link"><span>3</span></li>
								<li class="page-link"><span>4</span></li>
								<li class="pagination-ellipsis"><span>···</span></li>
								<li class="page-link"><span>6</span></li>
								<li class="next successive" id="next"><span>></span></li>
							</ul>
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>
	<script src="js/footer.js"></script>
</body>
</html>