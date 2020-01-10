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
<title>登录</title>
<link
	href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css"
	rel="stylesheet">
<link href="css/login/log_in.css" rel="stylesheet">
<link href="css/footer.css" rel="stylesheet">

<script src="js/jquery-3.3.1.min.js"></script>
<script
	src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<!-- cookie操作 -->
<script src="js/login/jquery.cookie.js"></script>
<!-- 自定义验证 -->
<script src="js/login/customvalidate.js"></script>
<!-- 自定义工具 -->
<script src="js/login/customtool.js"></script>
<!-- md5加密 -->
<script src="js/login/md5.js"></script>
<script src="js/login/basicValidate.js"></script>
<script src="js/login/log_in.js"></script>
</head>
<body>
	<div class="basecontainer">
		<div id="header_parent">
			<div id="header">
				<div id="logo">
					<a href="pagers/homepage.jsp"><img src="img/logo.png"></a>
				</div>
				<div id="slogen">陪伴一生的学习网</div>
			</div>
		</div>

		<div id="background_img">
			<div class="box_wrap">
				<div class="login_box">
					<div class="login_top">
						<span class="login_title">密码登录</span>
						<span class="login_tip"><a href="pagers/login/register.jsp">注册新账号</a></span>
					</div>
					<div class="login_center">
						
						<div class="input_wrap tel_wrap">
							<input type="text" id="tel" class="input_text tel" name="tel"
								placeholder="请输入手机号" autocomplete="off" maxlength="11">
							<div class="warning_message"></div>
						</div>
						<div class="input_wrap password_wrap">
							<input type="password" id="password" class="input_text password"
								name="password" placeholder="请输入密码"
								autocomplete="off" maxlength="20">
							<div class="warning_message"></div>
							<div class="show_password">&#xe9ce</div>
						</div>
						<div class="input_wrap notice_wrap">&#xea08 手机号或密码错误，请检查后重新登录</div>
						<div class="input_wrap span_wrap">
							<span class="remember_check"></span>
							<span >记住手机号</span>
							<span class="forget_password"><a href="pagers/login/passwordback.jsp">忘记密码?</a></span>
						</div>
						<div class="input_wrap">
							<input type="button" id="login" class="input_button"
								value="登录">
						</div>
					</div>
					<div class="login_bottom">
						<div>更多合作网站账号登录</div>
						<div class="third_login">
							<a id="alipay_login" href="https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=2018071160521829&scope=auth_user&redirect_uri=http://www.asxuexi.cn/thirdlogin/alipay.do&state=login"><img src="img/login/alipay_square.png"></a>
							<a id="wechat_login" href="https://open.weixin.qq.com/connect/qrconnect?appid=wxe60b803281a8a13a&response_type=code&scope=snsapi_login&redirect_uri=https://www.asxuexi.cn/thirdlogin/wechat.do&state=login"><img src="img/login/wechat_square.png"></a>
						</div>
					</div>
				</div>
				<div class="hidden_box">
					<input type="hidden" class="message" value="${message}">
				</div>
			</div>
		</div>
	</div>
	
	<script type="text/javascript" src="js/footer.js"></script>
</body>
</html>