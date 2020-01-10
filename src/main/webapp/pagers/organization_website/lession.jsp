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
<link rel="stylesheet" href="css/organization_website/lession.css">
<script src="https://cdn.staticfile.org/jquery/2.1.1/jquery.min.js"></script>
<script type="text/javascript" src="js/organization_website/loadOrgWebsite.js"></script>
<title id="lession">所有课程</title>
</head>
<body>
<div class="header_container"></div>
<div class="lsession_container">
	<div class="lession_list">
		<div class="block_name">课程分类</div>
	</div>
	<div class="lession_display"></div>
</div>
</body>
</html>