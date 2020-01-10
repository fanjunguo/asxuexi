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