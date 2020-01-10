$(document).ready(function() {
	var $tel=$("body .tel");
	var $telcode=$("body .telcode");
	var $email=$("body .email");
	var $password=$("body .password");
	var $passwordagain=$("body .passwordagain");
	var $showPassword=$("body .show_password");
	//手机号输入框失去焦点，触发验证
	$tel.on("focusout",function(){
		if ($(this).requireValidate()) {
			if ($(this).mobileValidate()) {
				//TODO 可能添加某些逻辑
			}else{
				$(this).hightlightObj();
				$(this).showMessage("手机号有误！");
			}
		}else{
			$(this).hightlightObj();
		}
	});
	//验证码输入框失去焦点，触发验证
	$telcode.on("focusout",function(){
		if ($(this).requireValidate()) {
			if (!($(this).numberValidate()&&$(this).lengthValidate([6,6]))) {
				$(this).hightlightObj();
				$(this).showMessage("验证码格式有误！");
			}
		}else{
			$(this).hightlightObj();
		}
	})
	//邮箱输入框失去焦点，触发验证
	$email.on("focusout",function(){
		if ($(this).requireValidate()) {
			if (!$(this).emailValidate()) {
				$(this).hightlightObj();
				$(this).showMessage("邮箱格式有误！");
			}
		}
	})
	//密码输入框失去焦点，触发验证
	$password.on("focusout",function(){
		if ($(this).requireValidate()) {
			if (!$(this).passwordValidate()) {
				$(this).hightlightObj();
				$(this).showMessage("密码6-20位,同时包含字母、数字和字符！");
			}
		}else{
			$(this).hightlightObj();
		}
	})
	//点击显示密码
	$showPassword.on("click",function(){
		if ($(this).hasClass("selected")) {
			$showPassword.removeClass("selected");
			$password.attr("type","password");
			$passwordagain.attr("type","password");
		}else{
			$showPassword.addClass("selected");
			$password.attr("type","text");
			$passwordagain.attr("type","text");
		}
	})
	//点击记住手机号
	$("body .remember_check").on("click",function(){
		if ($(this).hasClass("selected")) {
			$(this).removeClass("selected").html("");
		}else{
			$(this).addClass("selected").html("&#xea10");
		}
	})
});
