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
<title>图片管理器</title>
<link rel="stylesheet" href="css/imageManager/imageManager.css">
<script src="js/jquery-2.1.4.js"></script>
<script src="js/imageManager/imageManager.js"></script>
</head>
<body>
<button id="fanjunguo">按钮</button>
<script>
$('#fanjunguo').click(function(){
	$('body').loadImageManager('abcd');
});

</script>

</body>
</html>