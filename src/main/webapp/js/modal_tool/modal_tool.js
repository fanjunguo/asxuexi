/*
 * @插件使用说明:
 * 	1.此插件是向页面中写入一个模态框.使用者只需要自己写入面板的内容,以及实现点击确定按钮之后的事件(包括确定之后的移除面板)
 * 	2.确定按钮默认是灰色不可点击的,需要使用者在满足条件后,为其增加:usable类名
 * @参数以及传参方式,通过传递参数,可以修改面板的部分属性和内容:
 	$('selector').modalWindow({
		width:'',	//面板的宽度
		height:'',	//面板的高度
		btnId:'',	//确定按钮的id
		bodyId:'',	//面板内容容器的id
		panelTitle:'',	//面板标题
 	})
 * @使用实例:
 	html:
 	<div class='container'></div>
 	js:
 	$('.container').modalWindow();	
 */
(function($){
	//定义默认属性
	var defaults={
		width:'800px',
		height:'450px',
		btnId:'',
		bodyId:'',
		panelTitle:'标题',
	};
	var panel_html='<div class="modalWindow_background"></div>\
		<div class="modalWindow_panel">\
			<div class="modalWindow_panel_header"><p class="modal_panel_title">标题</p><span class="panel_close_span">×</span></div>\
			<div class="modalWindow_panel_body"></div>\
			<div class="modalWindow_panel_footer">\
				<div class="sure_button footer_btn">确定</div>\
				<div class="cancel_button footer_btn">取消</div>\
			</div></div>';
	//方法:写入模态框面板
	var createPanel=function(obj){
		$(obj).append(panel_html);
	}

	$.fn.modalWindow=function(options){ /*1.这里的参数是必须写的吗?经过测试,这个参数是必须的,有这个参数,
				才能在调用方法的时候,将参数传进来*/
		var options=$.extend(defaults,options);
		var $body=$('body');
		//方法:根据外部传参,改变属性
		var adjust=function(){
			//根据参数,给面板内容容器和确定按钮设置id
			$('.sure_button').attr('id', options.btnId);
			$('.modal_panel_title').text(options.panelTitle);
			if (options.bodyId!='') {
				$('.modalWindow_panel_body').attr('id', options.bodyId);
			}
			//根据参数,设定面板尺寸
			$('.modalWindow_panel').css({
				width: options.width,
				height: options.height,
				left:'calc((100vw - '+options.width+') / 2)',
			});
		}
		//调用方法
		createPanel(this);
		adjust();

		//点击叉号和取消按钮,移除面板
		$body.off('click', '.panel_close_span,.cancel_button').on('click', '.panel_close_span,.cancel_button', function() {
			$('.modalWindow_panel,.modalWindow_background').remove();
		});
	}
})(jQuery)