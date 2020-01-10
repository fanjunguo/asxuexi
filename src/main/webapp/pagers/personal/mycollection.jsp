<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的收藏</title>
<link rel="stylesheet"
	href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="css/headerStyle.css" />
<link rel="stylesheet" href="css/personal/personalMenu.css" />
<link rel="stylesheet" href="css/footer.css" />
<link rel="stylesheet" href="css/personal/mycollection.css">
<script src="js/jquery-3.3.1.min.js"></script>
<script
	src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="js/login/md5.js"></script>
<script src="js/authorityFilter.js"></script>
<script src="js/personal/mycollection.js"></script>

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
			<div id="body">
				<div id="title">
					<ul id="title_ul">
						<li id="title_class" class="active">课程收藏</li>
						<li id="title_school" class="">学校收藏</li>
					</ul>
				</div>

				<!-- 课程收藏 -->
				<div id="class_collection" style="display: block;">
					<!-- 筛选分类:全部课程/已失效 -->
					<div id="classify">
						<ul>
							<li id="all_course" class="active">全部课程</li>
							<li id="useless_course" class="">已失效</li>
						</ul>
					</div>
					<!-- 收藏的课程list -->
					<div id="content">
						<ul id="content_ul">
						</ul>
					</div>

				</div>

				<!-- 学校收藏 -->
				<div id="school_collection" style="display: none;">
					<ul id="schoollist_ul">
					</ul>
				</div>
			</div>
		</div>
	</div>
	<script src="js/footer.js"></script>
</body>
</html>