/**@author bufanpu
 * @describe 自定义验证方法，验证方法的调用对象为jquery input元素
 */
/**验证是否填写
 * @return false 未填写  true 填写
 */
$.fn.requireValidate=function(){
	let $Obj=$(this);
	let length=$Obj.val().trim().length;
	if (length==0) {
		return false;
	}else{
		return true;
	}
}
/**验证长度是否符合条件
 * @param 要验证的长度区间（闭区间）
 * @return false 不符合区间 true 符合区间
 */
$.fn.lengthValidate=function(lengthArray){
	let $Obj=$(this);
	let length=$Obj.val().trim().length;
	if (length>=lengthArray[0]&&length<=lengthArray[1]) {
		return true;
	}else{
		return false;
	}
}
/**验证是否纯数字
 * @return false 不是纯数字 true 纯数字
 */
$.fn.numberValidate=function(){
	let $Obj=$(this);
	let val=$Obj.val().trim();
	let reg=/^\d+(\.{1}\d+)?$/;
	return reg.test(val);
}
/**验证是否为中国大陆手机号
 * @return false 手机号格式错误 true 格式正确
 */
$.fn.mobileValidate=function(){
	let $Obj=$(this);
	let val=$Obj.val().trim();
	let reg=/^(1[3456789]\d{9})$/;
	return reg.test(val);
}
/**验证是否为邮箱
 * @return false 邮箱格式错误 true 格式正确
 */
$.fn.emailValidate=function(){
	let $Obj=$(this);
	let val=$Obj.val().trim();
	let reg=/^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/;
	return reg.test(val);
}
/**验证密码是否符合规定
 * @return false 密码格式错误 true 格式正确
 */
$.fn.passwordValidate=function(){
	let $Obj=$(this);
	let val=$Obj.val().trim();
	//弱规则：由大小写字母，数字，和特殊字符组成，6-20位
	let reg=/^.{6,20}$/;
	//强规则：必须包含大小写字母，数字，和特殊字符的每一种，6-20位
//	let reg=/(?=^.{6,20}$)(?=.*\d)(?=.*\W+)(?=.*[A-Z])(?=.*[a-z])(?!.*\n).*$/;
	return reg.test(val);
}
/**验证是否相等
 * @param 要比较的元素
 * @return false 不相等 true 相等
 */
$.fn.equalToValidate=function(obj){
	let $Obj=$(this);
	let val=$Obj.val().trim();
	let objVal=obj.val();
	if (val==objVal) {
		return true;
	}else{
		return false;
	}
}
/**@author bufanpu
 * @describe 一些工具方法
 */
/**高亮元素的方法，根本做法是改变border和background属性，调用对象为要高亮的元素
 * @param border 可选，对象，{newBorder:"css样式字符串",originalBorder:"css样式字符串"}
 * 		  background 可选，对象，{newBackground:"css样式字符串",originalBackground:"css样式字符串"}
 * @describe 不传参时，默认border={newBorder:"1px solid #FC0E2A",originalBorder:"1px solid #CCCCCC",}
 *						background={newBackground:"#fffce9",originalBackground:"#ffffff",}
 */
$.fn.hightlightObj=function(border,background){
	if(border==null){
		border={
			newBorder:"1px solid #FC0E2A",
			originalBorder:"1px solid #CCCCCC",
		}
	}
	if(background==null){
		background={
			newBackground:"#fffce9",
			originalBackground:"#ffffff",
		}
	}
	let $Obj=$(this);
	$Obj.css("border",border.newBorder).css("background-color",background.newBackground);
	setTimeout(function(){
		$Obj.css("border",border.originalBorder).css("background-color",background.originalBackground);
	}, 1500);
}
/**展示警告消息的方法，在class=warning_message的元素（自主创建）中展示消息
 * @param message 要展示的消息 showTime 可选，数字类型，要展示的时长，以毫秒为单位
 * @describe showTime不传参时，默认为1500
 */
$.fn.showMessage=function(message,showTime){
	let $Obj=$(this);
	if(showTime==null){
		showTime=1500;
	}
	$Obj.nextAll(".warning_message").text(message).fadeIn("fast");
	setTimeout(function(){
		$Obj.nextAll(".warning_message").fadeOut("fast");;
	}, showTime);
}
/**
 * 倒计时读秒，调用该方法的元素是显示倒计时的容器
 * @param 倒计时最大时间，正整数类型，以秒为单位
 */
$.fn.countDown=function(maxTime,successFunction){
	let $Obj=$(this);
	if (0<maxTime) {
		let countDown=setInterval(function(){
			$Obj.text(maxTime);
			maxTime--;
			if (0>maxTime) {
				clearInterval(countDown);
				if (successFunction!=null) {
					successFunction();
				}
			}
		}, 1000);
	}
}

/**
 * 与自定义权限拦截器相对应的js文件
 * 拦截未登录状态的ajax请求，弹窗显示登录面板，登陆后，刷新当前页面
 */
$(document).ready(function(){
	$("head").append('<link href="css/login/log_in.css" rel="stylesheet">');
	let html=
	'<div class="modal fade" id="quick_login_box" tabindex="-1" role="dialog"'+
		'aria-labelledby="successModalTitle" aria-hidden="true">'+
			'<div class="modal-dialog">'+
				'<div class="modal-content">'+
					'<div class="modal-body">'+
						'<div class="login_box">'+
							'<div class="login_top">'+
								'<span class="login_title">登录爱上学习网</span>'+
							'</div>'+
							'<div class="login_center">'+
								'<div class="input_wrap tel_wrap">'+
									'<input type="text" id="tel" class="input_text tel" name="tel"'+
										'placeholder="请输入手机号" autocomplete="off" maxlength="11">'+
									'<div class="warning_message"></div>'+
								'</div>'+
								'<div class="input_wrap password_wrap">'+
									'<input type="password" id="password" class="input_text password"'+
										'name="password" placeholder="请输入密码"'+
										'autocomplete="off" maxlength="20">'+
									'<div class="warning_message"></div>'+
									'<div class="show_password">&#xe9ce</div>'+
								'</div>'+
								'<div class="input_wrap notice_wrap">&#xea08 手机号或密码错误，请检查后重新登录</div>'+
								'<div class="input_wrap span_wrap">'+
									'<span class="forget_password"><a href="pagers/login/passwordback.jsp">注册新账号</a></span>'+
									'<span class="forget_password"><a href="pagers/login/passwordback.jsp">忘记密码?</a></span>'+
								'</div>'+
								'<div class="input_wrap">'+
									'<input type="button" id="login" class="input_button"'+
										'value="登录">'+	
								'</div>'+
							'</div>'+
						'</div>'+
					'</div>'+
				'</div>'+
			'</div>'+
		'</div>';
	$("body").append(html);
	
	var $tel=$("body .tel");
	var $password=$("body .password");
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
		}else{
			$showPassword.addClass("selected");
			$password.attr("type","text");
		}
	})
	
	$.ajaxSetup({
		//设置ajax请求结束后的执行操作
		complete : 
		function(XMLHttpRequest, textStatus) {
			// 通过XMLHttpRequest取得响应头，sessionstatus
			var sessionstatus = XMLHttpRequest.getResponseHeader("sessionstatus");
			if (sessionstatus == "timeout") {
				$("#quick_login_box").modal("show");
			}
		}
	});
	//监控密码输入框，按下回车的事件
	$password.on("keyup",function(event){
		if(event.keyCode==13){
			$("#login").trigger("click");
		}
	})
	$("body").on("click","#login",function(){
		//触发手机号验证
		let $tel=$("#quick_login_box .tel");
		let $password=$("#quick_login_box .password");
		$tel.trigger("focusout");
		$password.trigger("focusout");
		if ($tel.mobileValidate()&&$password.passwordValidate()) {
			//登录验证
			$.post("log_in/login.do",{
				tel : $tel.val().trim(),
				password : hex_md5($password.val().trim())
			},function(data){
				if ("600"==data.code) {
					$("#quick_login_box").modal("hide");
					setTimeout(function(){
						location.reload(true);
					},1000)
				}else{
					$(".notice_wrap").hide().show("fast");
				}
			});
		}	
	});
})