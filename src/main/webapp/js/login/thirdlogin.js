$(document).ready(function(){
	var thirdIdType=$(".hidden_box .third_id_type").val(); //第三方登录类型
	var thirdId=$(".hidden_box .third_id").val();	//第三方id
	var $registerTel=$(".register_center .tel");
	var $registerTelcode=$(".register_center .telcode");
	var $registerTelcodeBtn=$(".register_center .telcode_button");
	var $registerEmail=$(".register_center .email");
	var $registerPassword=$(".register_center .password");
	var $registerPasswordagain=$(".register_center .passwordagain");
	var $register=$("#register");
	var $loginTel=$(".login_center .tel");
	var $loginPassword=$(".login_center .password");
	var $login=$("#login");
	var $leftTab=$(".left_tab");
	var $rightTab=$(".right_tab");
	var $registerBox=$(".register_box");
	var $loginBox=$(".login_box");
	
	if (""==thirdIdType) {
		location.href=location.protocol+"//"+location.host+"/"
		+"pagers/login/log_in.jsp";
	}
	
	//监控密码输入框，按下回车的事件
	$loginPassword.on("keyup",function(event){
		if(event.keyCode==13){
			$login.trigger("click");
		}
	})
	$leftTab.on("click",function(){
		$(this).addClass("selected");
		$rightTab.removeClass("selected");
		$loginBox.show();
		$registerBox.hide();
	})
	$rightTab.on("click",function(){
		$(this).addClass("selected");
		$leftTab.removeClass("selected");
		$loginBox.hide();
		$registerBox.show();
	})
	//点击登录按钮
	$login.on("click",function(){
		//触发手机号，密码验证
		$loginTel.trigger("focusout");
		$loginPassword.trigger("focusout");
		if ($loginTel.mobileValidate()&&$loginPassword.passwordValidate()) {
			//登录验证
			$.post("log_in/login.do",{
				tel : $loginTel.val().trim(),
				password : hex_md5($loginPassword.val().trim())
			},function(data){
				if ("600"==data.code) {
					$(".notice_wrap").hide();
					//登录成功后，进行第三方账号绑定
					$.post("thirdlogin/bindThirdId.do",{thirdId:thirdId},function(data){
						if ("0"==data) {
							//绑定失败
							$("#noticeModal").modal("show");
						}
						if ("1"==data) {
							//成功后，显示过渡面板
							$("#successModal").modal("show");
							$("#successModal .second").countDown(2);
							//3秒后跳转首页
							setTimeout(function(){
								location.href=location.protocol+"//"+location.host+"/";
							}, 3000);
						}
						if ("2"==data) {
							if ("zfb"==thirdIdType) {
								$("#confirmModal .modal-body .third_id_type").text("支付宝");
							}
							if ("wx"==thirdIdType) {
								$("#confirmModal .modal-body .third_id_type").text("微信");
							}
							$("#confirmModal").modal("show");
						}
					})
					
				}else{
					//显示错误提示
					$(".wrong_num_pwd").hide().show("fast");
				}
			});
		}
	})
	//点击继续操作按钮
	$("#continue").on("click",function(){
		$("#confirmModal").modal("hide");
		$.post("thirdlogin/updateThirdId.do",{thirdId:thirdId},function(data){
		
			if ("600"==data.code) {
				//成功后，显示过渡面板
				$("#successModal").modal("show");
				$("#successModal .second").countDown(2);
				//3秒后跳转首页
				setTimeout(function(){
					location.href=location.protocol+"//"+location.host+"/";
				}, 3000);
			}else {
				$(".fail_bind").hide().show("fast");
			}
			
		});
	})
	
	//点击获取验证码按钮
	$registerTelcodeBtn.on("click",function(){
		$registerTel.trigger("focusout");//触发验证
		if ($registerTel.mobileValidate()) {
			//手机号格式正确，则请求后台，查询该手机号是否注册
			$.post("register/telIsExisted.do",{tel:$registerTel.val()},function(data){
				if ("false"==data) {
					//手机号未注册，并且发送按钮可用时，展示拖动验证框，进行拖动验证
					if (!$registerTelcodeBtn.hasClass("disabled")) {
						var captcha1 = new TencentCaptcha($("#drag")[0],"2013350953", function(res) {
							// 验证成功时
							if (res.ret == 0) {
								//发送验证码
								$.post("SMSVerificationCode/sendSMSVerificationCode.do",{tel:$registerTel.val()}, 
									function(data) {
										if (0==data) {
											//验证码发送失败,显示提示信息
											$registerTelcode.showMessage("验证码发送失败，请稍后重试",2000);
										}
										if(1==data){
											//验证码发送成功
											//展示倒计时
											$registerTelcodeBtn.addClass("disabled").countDown(60,function(){
												//倒计时结束后，恢复按钮样式，更改按钮文字
												$registerTelcodeBtn.removeClass("disabled").text("重新发送");
											})
										}
										if(2==data){
											//验证码发送次数超过限制
											$registerTelcode.showMessage("超过次数限制，请一小时或一自然日后重试!",2500);
										}
								});
							}
						});
						captcha1.show(); // 显示拖动验证
					}
				}else{
					$registerTel.showMessage("手机号已注册，请进行绑定！");
				}
			});
		}
	})
	//点击立即注册按钮
	$register.on("click",function(){
		//主动触发验证
		$registerTel.trigger("focusout");
		$registerTelcode.trigger("focusout");
		$registerEmail.trigger("focusout");
		$registerPassword.trigger("focusout");
		//取得验证结果
		let telIsValidated = $registerTel.mobileValidate();
		let telcodeIsValidated = $registerTelcode.numberValidate() && $registerTelcode.lengthValidate([6,6]);
		let emailIsValidated = $registerEmail.requireValidate()?
				($registerEmail.requireValidate() && $registerEmail.emailValidate()) : true;
		let passwordIsValidated = $registerPassword.passwordValidate();
		let isValidated = 
				telIsValidated &&
				telcodeIsValidated &&
				emailIsValidated &&
				passwordIsValidated;
		//如果通过电话，验证码，邮箱，密码的格式验证
		if (isValidated) {
			//确认密码框的验证，验证两次密码是否一致
			if($registerPasswordagain.equalToValidate($registerPassword)){
				//密码一致时，检验验证码
				$.post("register/codeIsMatched.do", {
					tel:$registerTel.val(),
					telcode:$registerTelcode.val()
				} ,function(data){
					if ("true"==data) {
						//验证码正确时，向后台传递基本注册信息
						$.post("register/register.do", {
							tel : $registerTel.val().trim(),
							email : $registerEmail.val().trim(),
							password : hex_md5($registerPassword.val().trim())
						}, function(data) {
							if (data.code==600) {
								// 绑定三方Id
								$.post("thirdlogin/bindThirdId.do",{thirdId:thirdId},function(data){
									if ("0"==data) {
										$("#noticeModal").modal("show");
									}
									if ("1"==data) {
										//成功后，显示过渡面板
										$("#successModal").modal("show");
										$("#successModal .second").countDown(2);
										//3秒后跳转首页
										setTimeout(function(){
											location.href=location.protocol+"//"+location.host+"/";
										}, 3000);
									}
								})
							}else{
								//注册失败的提示
								$("#failModal").modal("show");
							}
						});
					}else{
						//验证码错误，显示提示
						$telcode.hightlightObj();
						$telcode.showMessage("验证码错误！");
					}
				});
			}else{
				//密码不一致，显示提示
				$passwordagain.hightlightObj();
				$passwordagain.showMessage("两次密码不一致！");
			}
		}
		
	})
	//点击稍后绑定按钮
	$("#lateron").on("click",function(){
		$("#noticeModal").modal("hide");
		location.href=location.protocol+"//"+location.host+"/";
	})
	
	
})