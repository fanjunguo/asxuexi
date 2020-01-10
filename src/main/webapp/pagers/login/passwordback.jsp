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
<title>找回密码</title>
<link
	href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css"
	rel="stylesheet">
<link href="css/login/passwordback.css" rel="stylesheet">
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
<!-- 滑动验证 -->
<script src="https://ssl.captcha.qq.com/TCaptcha.js"></script>
<!-- md5加密 -->
<script src="js/login/md5.js"></script>
<script src="js/login/basicValidate.js"></script>
<script src="js/login/passwordback.js"></script>
</head>
<body>
	<div class="base_container">
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
				<div class="passwordback_box">
					<div class="passwordback_top">
						<span class="passwordback_title">找回密码</span>
						<span class="passwordback_tip"><a href="pagers/login/register.jsp">注册新账号</a></span>
					</div>
					<div class="passwordback_center">
						<div class="input_wrap tel_wrap">
							<input type="text" id="tel" class="input_text tel" name="tel"
								placeholder="请输入手机号" autocomplete="off" maxlength="11">
							<div class="warning_message"></div>
						</div>
						<div id="drag"></div>
						<div class="input_wrap telcode_wrap">
							<input type="text" id="telcode" class="input_text telcode" name="telcode"
								placeholder="请输入短信验证码" autocomplete="off" maxlength="6">
							<div id="telcode_button" class="telcode_button">获取验证码</div>
							<div class="warning_message"></div>
						</div>
						
				
						<div class="input_wrap password_wrap">
							<input type="password" id="password" class="input_text password"
								name="password" placeholder="请设定新密码"
								autocomplete="off" maxlength="20">
							<div class="warning_message"></div>
							<div class="show_password">&#xe9ce</div>
						</div>
						<div class="input_wrap notice_wrap">&#xea0c 新密码长度6~20位，包含数字、字母、符号</div>
						<div class="input_wrap password_wrap">
							<input type="password" id="passwordagain" class="input_text passwordagain"
								name="passwordagain" placeholder="再次输入密码" autocomplete="off"
								maxlength="20">
							<div class="warning_message"></div>
						</div>
						<div class="input_wrap">
							<input type="button" id="passwordback" class="input_button"
								value="立即提交">
						</div>
	
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="successModal" tabindex="-1" role="dialog"
		aria-labelledby="successModalTitle" aria-hidden="true"
		data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<div class="notification">&#xe9e1</div>
					<div>密码修改成功</div>
					<div style="font-size: 12px">
						<span class="second">5</span>秒后跳转至登录页面
					</div>
					<div class="input_wrap">
						<a href="pagers/login/log_in.jsp"><input type="button"
							class="input_button" value="立即登录"></a>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript" src="js/footer.js"></script>
</body>
</html>