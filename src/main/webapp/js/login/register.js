$(document).ready(function() {
	var $tel=$("#tel");
	var $telcode=$("#telcode");
	var $telcodeBtnOfRegister=$(".register_center #telcode_button");
	var $email=$("#email");
	var $password=$("#password");
	var $passwordagain=$("#passwordagain");
	var $register=$("#register");
	//点击获取验证码按钮，触发验证
	$telcodeBtnOfRegister.on("click",function(){
		$tel.trigger("focusout");//触发验证
		if ($tel.mobileValidate()) {
			//手机号格式正确，则请求后台，查询该手机号是否注册
			$.post("register/telIsExisted.do",{tel:$tel.val()},function(data){
				//手机号已注册，则弹出面板，提示登录
				if ("true"==data) {
					$("#loginModal .modal-body .mobile").text($tel.val());
					$("#loginModal").modal("show");
				}else{
					//手机号未注册，并且发送按钮可用时，展示拖动验证框，进行拖动验证
					if (!$telcodeBtnOfRegister.hasClass("disabled")) {
						var captcha1 = new TencentCaptcha($("#drag")[0],"2013350953", function(res) {
							// 验证成功时
							if (res.ret == 0) {
								//发送验证码
								$.post("SMSVerificationCode/sendSMSVerificationCode.do",{tel:$tel.val()}, 
									function(data) {
										if (0==data) {
											//验证码发送失败,显示提示信息
											$telcode.showMessage("验证码发送失败，请稍后重试",2000);
										}
										if(1==data){
											//验证码发送成功
											//展示倒计时
											$telcodeBtnOfRegister.addClass("disabled").countDown(60,function(){
												//倒计时结束后，恢复按钮样式，更改按钮文字
												$telcodeBtnOfRegister.removeClass("disabled").text("重新发送");
											})
										}
										if(2==data){
											//验证码发送次数超过限制
											$telcode.showMessage("请求次数超过限制。请一小时或一自然日后重新操作!",2500);
										}
								});
							}
						});
						captcha1.show(); // 显示拖动验证
					}
					
				}
			});
		}
	});
	//点击注册按钮
	$register.on("click",function(){
		//主动触发验证
		$tel.trigger("focusout");
		$telcode.trigger("focusout");
		$email.trigger("focusout");
		$password.trigger("focusout");
		//取得验证结果
		let telIsValidated = $tel.mobileValidate();
		let telcodeIsValidated = $telcode.numberValidate() && $telcode.lengthValidate([6,6]);
		let emailIsValidated = $email.requireValidate()?
				($email.requireValidate() && $email.emailValidate()) : true;
		let passwordIsValidated = $password.passwordValidate();
		let isValidated = 
				telIsValidated &&
				telcodeIsValidated &&
				emailIsValidated &&
				passwordIsValidated;
		//如果通过电话，验证码，邮箱，密码的格式验证
		if (isValidated) {
			//确认密码框的验证，验证两次密码是否一致
			if($passwordagain.equalToValidate($password)){
				//密码一致时，检验验证码
				$.post("register/codeIsMatched.do", {
					tel:$tel.val(),
					telcode:$telcode.val()
				} ,function(data){
					if ("true"==data) {
						//验证码正确时，向后台传递基本注册信息
						$.post("register/register.do", {
							tel : $tel.val().trim(),
							email : $email.val().trim(),
							password : hex_md5($password.val().trim())
						}, function(data) {
							if (data.code==600) {
								//注册成功后，显示过渡面板
								$("#successModal").modal("show");
								$("#successModal .second").countDown(2);
								//3秒后跳转首页
								setTimeout(function(){
									location.href=location.protocol+"//"+location.host+"/";
								}, 3000);
							}else{
								//TODO 可以采用更人性化的提示方式
								alert("注册失败，请稍后重试！");
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
	
});
