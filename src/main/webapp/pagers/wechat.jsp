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
<title>微信登录测试</title>

</head>
<body>
	<div class="login">点击登录</div>
	<img class="qrcode" src="">
</body>
<script src="js/jquery-3.3.1.min.js"></script>
<script type="text/javascript">
	$('.login').click(function(){
		$.get('wechat/getQrcode.do', function(data) {
			if (data!=null) {
				// window.location.href='https://login.weixin.qq.com/qrcode/'+data;
				$('.qrcode').attr('src','https://login.weixin.qq.com/qrcode/'+data);
			}
		});
	});
</script>
</html>