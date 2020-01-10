/* 说明:用于机构中心选择及编辑二级网站页面使用的js
*/
//todo:请求改成.action

$(document).ready(function() {
	let $body=$('body');
	//初始时先判断机构是否已经有二级网站,然后确定展示选择模版还是展示机构二级网站的管理入口
	$.get('orgWebsite/getWebsite.action', function(data) {
		let list=data.list;
		if (list.length==0) {
			//展示入口
			let contentHtml='\
				<a href="org/'+data.orgId+'.school" target="_blank">\
					<div class="entrance my_website">\
						<img class="img" src="img/organization_website/my_website.png">\
						<p>查看网站</p>\
					</div>\
				</a>\
				<a href="edit/'+data.orgId+'.action" target="_blank">\
					<div class="entrance manage_website">\
						<img class="img" src="img/organization_website/manage_website.png">\
						<p>编辑网站</p>\
					</div>\
				</a>\
				<div class="entrance change_template">\
					<img class="img" src="img/organization_website/change.png">\
					<p>更换模版</p>\
				</div>\
				<div class="template_container"></div>';

			$('.org_website_content').html(contentHtml);
		} else {
			//展示模版
			writeTemplate($('.org_website_content'),'select_btn');
		}
		
	});

	//绑定手风琴动画
	$body.off('mouseenter', '.page_li').on('mouseenter', '.page_li', function() {
			$(this).stop().animate({
				"width" : "435px"
			}, 300, "linear").siblings().stop().animate({
				"width" : "50px"
			}, 300, "linear")
	});

	//当鼠标移到模版区域内时,“选择模版”的按钮才显示;鼠标移出时隐藏
	$body.off('mouseenter', '.tab_content').on('mouseenter', '.tab_content', function() {
		$(this).children('.mybtn').css('visibility', 'visible');
	});
	$body.off('mouseleave', '.tab_content').on('mouseleave', '.tab_content', function() {
		$(this).children('.mybtn').css('visibility', 'hidden');
	});

	//点击选择模版
	$body.off('click', '.select_btn').on('click', '.select_btn', function() {
		//获取所选的模版名称
		let tempalteName=$(this).attr('template_name');
		let newWindow=window.open();
		$.post('orgWebsite/chooseTemplate.action', {tempalteName: tempalteName}, function(data) {
			newWindow.location=data;
			window.location.reload(); //刷新当前页面
		});
	});

	$body.on('click', '.change_template', function() {
		let templateContainer=$('.template_container');
		writeTemplate(templateContainer,'change_btn',function(){
			templateContainer.slideDown('slow');
			$('.page_title').show('slow');
		});
		
	});

	//选择模版
	$body.on('click', '.change_btn', function() {
		$('#window').modal();
	});

	//更换模版
	$('#sure_change').click(function() {
		let tempalteName=$('.change_btn').attr('template_name');
		let newWindow=window.open();
		$.post('orgWebsite/changeTemplate.action', {tempalteName: tempalteName}, function(data) {
			newWindow.location=data;
			window.location.reload(); //刷新当前页面
		});
	});

	/* @方法: 向容器内写入模版
	*  @参数: container 容器对象;
	*		  btn_name 按钮的类名
	*		  callback 回调函数
	*/
	function writeTemplate(container,btn_name,callback){
		let contentHtml='<div class="tab_content">\
					<div class="mybtn '+btn_name+'" template_name="template1">使用模版</div>\
					<div class="tab_panel">\
						<ul class="model">\
							<li class="page_li">\
								<div class="page_screenshot">\
									<img src="img/organization_website/template1-index.png">\
								</div>\
								<div class="page_title">首页</div>\
							</li>\
							<li class="page_li">\
								<div class="page_screenshot">\
									<img src="img/organization_website/template-lession.png">\
								</div>\
								<div class="page_title">所有课程</div>\
							</li>\
							<li class="page_li">\
								<div class="page_screenshot">\
									<img src="img/organization_website/template1-school.png">\
								</div>\
								<div class="page_title">学校风采</div>\
							</li>\
							<li class="page_li">\
								<div class="page_screenshot">\
									<img src="img/organization_website/template1-teacher.png">\
								</div>\
								<div class="page_title">名师介绍</div>\
							</li>\
							<li class="page_li">\
								<div class="page_screenshot">\
									<img src="img/organization_website/template1-recruit.png">\
								</div>\
								<div class="page_title">招生信息</div>\
							</li>\
						</ul>\
					</div>\
				</div>';
		container.html(contentHtml);
		callback.call();
	}

})