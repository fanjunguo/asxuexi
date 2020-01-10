/* 
 * drag 1.0
 * create by tony@jentian.com
 * date 2015-08-18
 * 拖动滑块
 */
var countdown = 60;
var sending = false;
var bt = "#sendSMS";
(function($) {

	// 初始页面查看是否读秒为结束

	// 验证滑动条
	$.fn.drag = function(obj) {
		
		var x, drag = this, isMove = false, defaults = {};
		countdown = 60;
		var options = $.extend(defaults, options);
		// 添加背景，文字，滑块
		var html = '<div class="drag_bg"></div>'
				+ '<div class="drag_text" onselectstart="return false;" unselectable="on">滑动到最右侧获取验证码1111111</div>'
				+ '<div class="handler handler_bg"></div>';
		this.html(html);
		var handler = drag.find('.handler');
		var drag_bg = drag.find('.drag_bg');
		var text = drag.find('.drag_text');
		var maxWidth = drag.width() - handler.width(); // 能滑动的最大间距
		console.log(maxWidth);
		
		// 鼠标按下时候的x轴的位置
		handler.mousedown(function(e) {
			isMove = true;
			x = e.pageX - parseInt(handler.css('left'), 10);
		});

		// 鼠标指针在上下文移动时，移动距离大于0小于最大间距，滑块x轴位置等于鼠标移动距离
		$(document).mousemove(function(e) {
			var _x = e.pageX - x;
			if (isMove) {
				if (_x > 0 && _x <= maxWidth) {
					handler.css({
						'left' : _x
					});
					drag_bg.css({
						'width' : _x
					});
				} else if (_x > maxWidth) { // 鼠标指针移动距离达到最大时验证成功并清空事件
					dragOk();
					alert("验证通过")
					$("#drag").parents("tr").hide();
//					var jsptel = $("#tel").val();
					var newPhone=$("#newPhone").val();
					if($("#phone").html()!=newPhone){
//						setTime();
//						SMSVerificationCode/sendSMSVerificationCode.do
						$.post("SMSVerificationCode/sendSMSVerificationCode.do",{tel:newPhone},function(data){
							if(data == 1){
								sending = true;
								setTime();
								return;
							}
							if(data == 0){
								alert("验证码功能暂未开放，请知悉");
								return;
							}
							if(data == 2){
								alert("您今天已请求5次验证码，请明天再进行操作!")
							}
						})
					}
					
				}
			}
		}).mouseup(function(e) {
			isMove = false;
			var _x = e.pageX - x;
			if (_x < maxWidth) { // 鼠标松开时，如果没有达到最大距离位置，滑块就返回初始位置
				handler.css({
					'left' : 0
				});
				drag_bg.css({
					'width' : 0
				});
			}
		});

		// 清空事件
		// 同时进行发送短信请求
		function dragOk() {
			handler.removeClass('handler_bg').addClass('handler_ok_bg');
			text.text('验证通过');
			drag.css({
				'color' : '#fff'
			});
			handler.unbind('mousedown');
			$(document).unbind('mousemove');
			$(document).unbind('mouseup');
			// 验证成功后将验证div隐藏掉
			document.getElementById("drag").style.display = "none";
		}
		var countDown=60; 
	    function setTime(val) {
	    	$("#second").parent().off("click");
	    	$("#sendSMS").removeAttr("id","sendSMS")
	    if (countDown == 0) {
	    	$("#second").parent().attr("id","sendSMS");
	    	$("#sendSMS").on("click",function(event){
	    		sendValidate(event)
	    	});
	    	 $("#second").html(""); 
	    countDown = 60;
	    return;
	    } else { 
	    $("#second").html( countDown ); 
	    countDown--; 
	    } 
	    setTimeout(function() { 
	    setTime(val)
	    	},1000)
	    }
	};
})(jQuery);

