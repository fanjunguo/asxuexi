$(document).ready(function(){
	var $tel=$("#tel");
	var $telcode=$("#telcode");
	var $password=$("#password");
	var $passwordagain=$("#passwordagain");
	var $telcodeBtnOfPWDback=$(".passwordback_center #telcode_button");
	var $passwordback=$("#passwordback");
	//点击获取验证码按钮，触发验证
	$telcodeBtnOfPWDback.on("click",function(){
		$tel.trigger("focusout");//触发验证
		if ($tel.mobileValidate()) {
			//手机号格式正确，则请求后台，查询该手机号是否注册
			$.post("register/telIsExisted.do",{tel:$tel.val()},function(data){
				if ("true"==data) {
					//手机号已注册，并且发送按钮可用时，展示拖动验证框，进行拖动验证
					if (!$telcodeBtnOfPWDback.hasClass("disabled")) {
						var captcha1 = new TencentCaptcha($("#drag")[0],"2013350953", function(res) {
							// 验证成功时
							if (res.ret == 0) {
								//发送手机验证码
								$.post("SMSVerificationCode/sendSMSVerificationCode.do",{tel:$tel.val()}, 
									function(data) {
										if (0==data) {
											//验证码发送失败,显示提示信息
											$telcode.showMessage("验证码发送失败，请稍后重试",2000);
										}
										if(1==data){
											//验证码发送成功
											//展示倒计时
											$telcodeBtnOfPWDback.addClass("disabled").countDown(60,function(){
												//倒计时结束后，恢复按钮样式，更改按钮文字
												$telcodeBtnOfPWDback.removeClass("disabled").text("重新发送");
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
					
				}else{
					$tel.showMessage("手机号未注册！");
				}
			});
		}
	});
	
	$passwordback.on("click",function(){
		//主动触发验证
		$tel.trigger("focusout");
		$telcode.trigger("focusout");
		$password.trigger("focusout");
		//取得验证结果
		let telIsValidated = $tel.mobileValidate();
		let telcodeIsValidated = $telcode.numberValidate() && $telcode.lengthValidate([6,6]);
		let passwordIsValidated = $password.passwordValidate();
		let isValidated = 
				telIsValidated &&
				telcodeIsValidated &&
				passwordIsValidated;
		//如果通过电话，验证码，密码的格式验证
		if (isValidated) {
			//确认密码框的验证，验证两次密码是否一致
			if($passwordagain.equalToValidate($password)){
				//密码一致时，检验验证码
				$.post("register/codeIsMatched.do", {
					tel:$tel.val(),
					telcode:$telcode.val()
				} ,function(data){
					if ("true"==data) {
						//验证码正确时，向后台传递新密码
						$.post("log_in/setNewPassWord.do", {
							tel : $tel.val().trim(),
							password : hex_md5($password.val().trim())
						}, function(data) {
							if ("1"==data) {
								//密码设定成功后，显示过渡面板
								$("#successModal").modal("show");
								$("#successModal .second").countDown(4);
								//5秒后跳转登录页面
								setTimeout(function(){
									// TODO 登录页面的名字会更改
									location.href=location.protocol+"//"+location.host+"/"
										+"pagers/login/log_in.jsp";
								}, 5000);
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
	
	
	
	
})