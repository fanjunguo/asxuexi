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
<title>个人信息</title>
<link rel="stylesheet" href="css/webUploader/webuploader.css">
<link rel="stylesheet"
	href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="css/headerStyle.css" />
<link rel="stylesheet" href="css/personal/personalMenu.css" />
<link rel="stylesheet" href="css/footer.css" />
<!-- <link rel="stylesheet" href="css/login/drag.css" > -->
<link rel="stylesheet" href="css/personal/personalInfo.css">
<script src="js/jquery-3.3.1.min.js"></script>
<script
	src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="js/webUploader/webuploader.js"></script>
<script src="js/validate/jquery.validate.js"></script>
<script src="js/validate/jquery.validate.pack.js"></script>
<script src="js/validate/additional-methods.js"></script>
<script src="js/validate/localization/messages_cn.js"></script>
<!-- <script src="js/org/drag.js"></script> -->
<!-- cookie操作 -->
<script src="js/login/jquery.cookie.js"></script>
<script src="js/login/md5.js"></script>
<script src="js/authorityFilter.js"></script>
<script src="js/date/YMDClass.js"></script>
<script src="js/personal/personalInfo.js"></script>
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
			<div class="background"></div>
			<div id="context">
				<div id="essentialInformation">
					<div id="titleInformation" class="titleDiv">
						<span class="titleDiva">基本信息</span> <span class="edit_btn"
							id="returnShowInformation">取消</span> <span class="edit_btn"
							id="editShowInformation">编辑</span> <span class="edit_btn"
							id="savaShowInformation">保存</span>
					</div>
					<!-- style="display:none" -->
					<div id="showInformation">
						<form id='validate'>
							<table class="tableShowInformation">
								<tbody>
									<tr>
										<th>昵称:</th>
										<td id="name" name="name"></td>
										<th>性别:</th>
										<td id="sex" name="sex"></td>
									</tr>
									<tr>
										<th>生日:</th>
										<td id="birth" name="birth"></td>
									</tr>
									<tr>
										<th>地址:</th>
										<td id="address"></td>

									</tr>
								</tbody>
							</table>
						</form>
					</div>

					<div id="editInformation">
						<table class="tableEditInformation">
							<tbody>
								<tr>
									<td>
										<table class="personal_info_table">
											<tr>
												<th>昵称：</th>
												<td><input id="editName"
													class="form-control filter_worf" type="text"></td>
											</tr>
											<tr>
												<th>性别:</th>
												<td><input id="radio1" type="radio" name="radio"
													value="1">男 <input id="radio2" type="radio"
													name="radio" value="0">女</td>

											</tr>
											<tr>
												<th>出生日期:</th>
												<td><select id="yearBirth"
													class="show-tick form-control controlWidth" name="year1">
												</select>&nbsp;年&nbsp;&nbsp; <select id="monthBirth"
													class="show-tick form-control controlWidth" name="month1">
												</select>&nbsp;月&nbsp;&nbsp; <select id="dayBirth"
													class="show-tick form-control controlWidth" name="day1">
												</select></td>
											</tr>
											<tr>
												<th>地址:</th>
												<td colspan="2"><select id="provinceAddress"
													class="show-tick form-control" data-style="btn-info"><option>请选择</option></select>
													<select id="cityAddress" class="show-tick form-control"
													data-style="btn-info"><option>请选择</option></select> <select
													id="countyAddress" class="show-tick form-control"
													name="countyAddress" data-style="btn-info"><option
															value='0'>请选择</option></select><br /></td>
											</tr>
										</table>
									</td>
									<td>
										<table>
											<td rowspan="3" class="orgLogoImg"><img id="editlogo"
												class="showLogo" src="img/personal/icon/default.jpg">
												<span id="editLogoImg">更换头像</span></td>
										</table>
									</td>
								</tr>
							</tbody>
						</table>

					</div>
				</div>

				<!--安全信息  -->
				<div id="AuthenticationInformation">
					<div class="titleDiv">
						<span class="titleDiva">安全信息</span>
					</div>
					<div id="showAuthentication" class="">
						<div class="block">
							<div class="stripShow">
								<div class="infoLeft">
									<img src="img/personal/icon/password.png">
								</div>
								<div class="infoCenter">密码</div>
								<div class="infoRight">修改</div>
							</div>
							<div class="stripHide">
								<div class="window_header">修改密码</div>
								<form id="password_validate">
									<div class="line">
										原密码:<input type="password" class="form-control"
											id="passwordNow" name="passwordNow" autocomplete="off">
									</div>
									<div class="line">
										新密码:<input type="password" class="form-control"
											id="passwordNew" name="passwordNew" autocomplete="off">
									</div>
									<div class="line">
										确认密码:<input type="password" class="form-control"
											id="passwordValidate" name="passwordValidate"
											autocomplete="off" />
									</div>
									<div class="footer_btn">
										<span class="btn updatePassword">确定</span> <span
											class="btn clear">取消</span>
									</div>

								</form>
							</div>
						</div>

						<!-- 手机号 -->
						<div class="block">
							<div class="stripShow">
								<div class="infoLeft">
									<img src="img/personal/icon/phone.png">
								</div>
								<div class="infoCenter">
									手机号 <span id="phone"></span>
								</div>
								<div class="infoRight">修改</div>
							</div>
							<div class="stripHide">

								<div class="window_header">更换手机号</div>
								<form id="phone_validate">
									<div class="line">
										验证当前密码:<input type="password" class="form-control"
											id="passwordPhone" name="passwordPhone" />
									</div>
									<div class="line">
										新手机号:<input type="text" class="form-control" id="newPhone"
											name="newPhone" />
									</div>
								</form>
								<form id="phone_verification_code">
									<div class="line">
										<span style="display: block;">短信验证码:</span><input type="text"
											class="form-control" id="validatePhone" name="validatePhone" />
										<div id="sendSMS" class="btn countSeconds">发送验证码</div>
										<span class="wrong_message error"></span>
									</div>
								</form>
								<div class="footer_btn">
									<span class="btn  updatePhone">确定</span> <span
										class="btn  clear">取消</span>
								</div>
							</div>
						</div>

						<!--绑定邮箱/修改邮箱  -->
						<div class="block">
							<div class="stripShow">
								<div class="infoLeft">
									<img src="img/personal/icon/mail.png">
								</div>
								<div class="infoCenter">
									邮箱 <span id="email"></span>
								</div>
								<div class="infoRight bind_mail"></div>
							</div>
							<div class="stripHide">
								<div class="window_header">
									<span class="bind_mail"></span>邮箱
								</div>
								<form id="email_validate">
									<div class="line">
										验证当前密码:<input type="password" class="form-control"
											id="passwordEmail" name="passwordEmail" autocomplete="off" />
									</div>
									<div class="line">
										邮箱:<input type="text" class="form-control" id="newEmail"
											name="newEmail" />

									</div>
								</form>

								<form id="email_verification_code">
									<div class="line">
										<span style="display: block;">验证码:</span><input type="text"
											class="form-control" id="validateEmail" name="validateEmail" />
										<div class="btn btn-info sendEmail" id="sendEmail">发送邮箱验证码</div>
									</div>
								</form>
								<div class="footer_btn">
									<span class="btn  updateEmail">确定</span> <span
										class="btn  clear">取消</span>
								</div>
							</div>
						</div>

						<div class="block third_block">
							<div class="stripShow">
								<div class="infoLeft">
									<img src="img/personal/icon/third.png">
								</div>
								<div class="infoCenter">第三方</div>

								<div class="thirdShow">
									<div class="third_container wechat">
										<table class="third_table">
											<tr>
												<td class="first_column"><img
													src="img/personal/info/wechat.png"></td>
												<td class="second_column haveNoDate wechat_info"><img
													class="tip_img" src="img/personal/icon/tip.png">未绑定</td>
											</tr>
											<tr>
												<td class="first_column">微信</td>
												<td class="second_column wechat_btn"><div id="bindWeiXin"
														class="third_btn">点击绑定</div></td>
											</tr>
										</table>
									</div>
									<div class="third_container alipay">
										<table class="third_table">
											<tr>
												<td class="first_column"><img
													src="img/personal/info/alipay.png"></td>
												<td class="second_column haveNoDate alipay_info"><img
													class="tip_img" src="img/personal/icon/tip.png">未绑定</td>
											</tr>
											<tr>
												<td class="first_column">支付宝</td>
												<td class="second_column alipay_btn"><div id="bindZhiFuBao"
														class="third_btn">点击绑定</div></td>
											</tr>
										</table>

									</div>

								</div>
							</div>
						</div>
					</div>
					<div style="display: none">
						<input type="hidden" class="message" value="${message}">
					</div>
					<div class="modal fade" id="messageModal" tabindex="-1"
						role="dialog" aria-labelledby="loginModalTitle" aria-hidden="true">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-body">
									<button type="button" class="close" data-dismiss="modal"
										aria-hidden="true">&times;</button>
									<div class="notification"></div>
									<div class="message_content"></div>
									<div class="input_wrap">
										<a href="pagers/login/log_in.jsp"><input type="button"
											class="input_button" value="立即登录"></a>
									</div>
								</div>
							</div>
						</div>
					</div>

				</div>

			</div>
		</div>
	</div>
	<div id="hidebody" class="hidebody" style="display: none">
		<div id="hidePanel" class="hidePanel">
			<span id="hideContext" class="hideContext"></span>
		</div>
	</div>
	<script src="js/footer.js"></script>
</body>
</html>