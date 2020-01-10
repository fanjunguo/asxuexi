$(document).ready(function(){
	var $tel=$("#tel");
	var $password=$("#password");
	var userMobile=$.cookie("usermobile");
	if (userMobile!=null && userMobile!="null") {
		$tel.val(userMobile);
	}
	if ("nullalipayid"==$(".message").val()) {
		//TODO 优化提示方式
		alert("支付宝登录失败，请稍后重试");
	}
	if ("nullwechatid"==$(".message").val()) {
		//TODO 优化提示方式
		alert("微信登录失败，请稍后重试");
	}
	//监控密码输入框，按下回车的事件
	$password.on("keyup",function(event){
		if(event.keyCode==13){
			$("#login").trigger("click");
		}
	})
	
	//点击登录按钮
	$("#login").on("click",function(){
		//触发手机号验证
		$tel.trigger("focusout");
		$password.trigger("focusout");
		if ($tel.mobileValidate()&&$password.passwordValidate()) {
			//登录验证
			$.post("log_in/login.do",{
				tel : $tel.val().trim(),
				password : hex_md5($password.val().trim())
			},function(data){
				if (data.code==600) {
					if ($(".remember_check").hasClass("selected")) {
						//保存cookie
						$.cookie("usermobile", $tel.val().trim(), {expires: 365,path:"/"});
					}else{
						//清除cookie
						$.cookie("usermobile", null,{expires: 365,path:"/"});
					}
					//跳转首页
					location.href=location.protocol+"//"+location.host+"/";
				}else{
					$(".notice_wrap").hide().show("fast");
				}
			});
		}
	})
});
