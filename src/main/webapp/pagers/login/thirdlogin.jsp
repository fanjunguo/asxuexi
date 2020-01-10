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
<title>合作登录</title>
<link
	href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css"
	rel="stylesheet">
<link href="css/login/log_in.css" rel="stylesheet">
<link href="css/login/register.css" rel="stylesheet">
<link href="css/login/thirdlogin.css" rel="stylesheet">
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
<script src="js/login/thirdlogin.js"></script>

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
			<div class="navigation">
				<div class="left_tab">&#xe9cd 已有帐号,请绑定</div>
				<div class="right_tab selected">&#xe991 新用户,请完善资料</div>
			</div>
			<div class="box_wrap">
				<div class="register_box">
					<div class="register_center">
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
						<div class="input_wrap email_wrap">
							<input type="text" id="email" class="input_text email" name="email"
								placeholder="请输入邮箱" autocomplete="off">
							<div class="warning_message"></div>
						</div>
						<div class="input_wrap password_wrap">
							<input type="password" id="password" class="input_text password"
								name="password" placeholder="密码6-20位，数字、字母、符号组合"
								autocomplete="off" maxlength="20">
							<div class="warning_message"></div>
							<div class="show_password">&#xe9ce</div>
						</div>
	
						<div class="input_wrap password_wrap">
							<input type="password" id="passwordagain" class="input_text passwordagain"
								name="passwordagain" placeholder="再次输入密码" autocomplete="off"
								maxlength="20">
							<div class="warning_message"></div>
						</div>
						<div class="input_wrap">
							<input type="button" id="register" class="input_button"
								value="注册并登录">
						</div>
	
					</div>
					<div class="register_bottom">
						<div>点击“注册并登录”，即表示您已阅读并同意遵守爱上学习网</div>
						<div>
							<a target="_blank" href="pagers/protocol.jsp">用户协议&nbsp;</a>和<a target="_blank" href="pagers/privacy.jsp">&nbsp;隐私政策</a>
						</div>
					</div>
				</div>
				
				<div class="login_box">
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
						<div class="input_wrap notice_wrap wrong_num_pwd">&#xea08 手机号或密码错误，请检查确认后重试</div>
						<div class="input_wrap notice_wrap fail_bind">&#xea08 绑定失败，请点击登录按钮重试</div>
						<div class="input_wrap span_wrap">
							<span class="remember_check"></span>
							<span >记住手机号</span>
							<span class="forget_password"><a href="pagers/login/passwordback.jsp">忘记密码?</a></span>
						</div>
						<div class="input_wrap">
							<input type="button" id="login" class="input_button"
								value="登录并绑定">
						</div>
					</div>
				</div>
				
				<div class="hidden_box">
					<input type="hidden" class="third_id_type" value="${thirdIdType}">
					<input type="hidden" class="third_id" value="${thirdId}">
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
					<div>绑定第三方账号成功</div>
					<div style="font-size: 12px">
						<span class="second">3</span>秒后跳转至首页
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade" id="failModal" tabindex="-1" role="dialog"
		aria-labelledby="failModalTitle" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<div class="notification">&#xe9e5</div>
					<div>操作失败，请重新尝试！</div>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="confirmModal" tabindex="-1" role="dialog"
		aria-labelledby="confirmModalTitle" aria-hidden="true" data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<div class="notification">&#xea08</div>
					<div>
						您的<span>爱上学习网账号</span>已经绑定<span class="third_id_type"></span>账号
					</div>
					<div>点击<span>继续操作</span>，将解绑原有<span class="third_id_type"></span>账号，并绑定新的<span class="third_id_type"></span>账号</div>
					<div class="input_wrap">
						<a><input type="button" id="continue"
							class="input_button" value="继续操作"></a>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade" id="noticeModal" tabindex="-1" role="dialog"
		aria-labelledby="noticeModalTitle" aria-hidden="true" data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<div class="notification">&#xea0c</div>
					<div>
						绑定失败，请前往<span><a href="pagers/personal/myleft.jsp">个人中心</a></span>
					</div>
					<div>重新进行第三方账号的绑定</div>
					<div class="input_wrap">
						<a><input type="button" id="lateron"
							class="input_button" value="稍后绑定"></a>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript" src="js/footer.js"></script>
</body>
</html>