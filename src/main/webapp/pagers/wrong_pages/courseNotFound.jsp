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
<title>课程详情</title>
<link rel="stylesheet"
	href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="css/headerStyle.css" />
<link rel="stylesheet" href="css/footer.css" />
<link rel="stylesheet" href="css/details/courseDetails.css" />
<script src="js/jquery-3.3.1.min.js"></script>
<script
	src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>

</head>
<body>
	<script src="js/headerstyle.js"></script>
	<div class="content" style="height:480px;padding-top:20px;">
		<img alt="找不到该课程" src="img/details/courseNotFound.jpg" style="height:100%;width:100%;">
	</div>
	<script src="js/footer.js"></script>
</body>
</html>