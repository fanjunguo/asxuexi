/**
 * create by junguo.fan
 * @说明:二级网站查看各个页面时候,加载页面内容和相关所需的js、css等文件
 * */ 
$('head').append('<link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">'+
				'<link rel="stylesheet" href="css/organization_website/editor.css">'+
				'<link rel="stylesheet" href="css/organization_website/moduleStyle.css">'+
				'<script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>'+
				'<link rel="stylesheet" href="css/swiper/swiper.min.css">'+
				'<script  src="js/swiper/swiper.min.js"></script>'
				);
$(function() {
	//获取当前url,解析出机构id
	let $body=$('body');
	let href=window.location.href;
	let start=href.lastIndexOf('/')+1;
	let end =href.lastIndexOf('.');
	let orgId=href.substring(start,end);
	let $title=$('title');
	let pageName=$title.attr('id');
	$.post('orgWebsite/getPages.do', {pageName: pageName,orgId:orgId}, function(data) {
		//判断数据库是否有数据,如果没有数据,提示该机构未开通网站
		if (data.result==0) {
			let wrongHtml='<div class="wrong_container">\
					<img src="img/organization_website/tips.png">\
					<p>该机构暂未开通网站</p>\
				</div>';
			$body.html(wrongHtml);
			$title.html('404-错误');
		} else {
			if (pageName=='lession') {
				$('.header_container').html(data.header);
				let sortArray=new Array();
				let $lessionList=$('.lession_list');
				let $lessionDisplay=$('.lession_display');
				$.post('orgCourse/getOrgCourseInfo.do', {orgId: orgId}, function(data) {
					if (data.length!=0) {
						//获取机构所有的课程分类
						for (var i = 0; i < data.length; i++) {
							//写入左侧所有分类
							let sortId=data[i].sort_id;
							
							if (!sortArray.includes(sortId)) {
								sortArray.push(sortId);
								$lessionList.append('<div class="sort_third" sortId='+sortId+'>'+data[i].sort_name+'</div>')
							}

							//写入右侧课程
							writeCourseItem($lessionDisplay,data[i]);
						}

						//所有课程:点击侧边分类,查询该分类下的数据
						$body.off('click', '.sort_third').on('click', '.sort_third', function() {
							let sortId=$(this).attr('sortid');
							//先清空课程数据
							$lessionDisplay.empty();
							for (var i = 0; i < data.length; i++) {
								if (sortId==data[i].sort_id) {
									writeCourseItem($lessionDisplay,data[i]);
								}
							}
							//判断,如果没有课程数据,提示一下
							isCourseEmpty($lessionDisplay);
						});
					}else{
						//判断,如果没有课程数据,提示一下
						isCourseEmpty($lessionDisplay);
					}
				});

				
			}
			//如果是除了lession的其他页面
			else{
				$body.html(data.header+data.page_html);

				//获取轮播图的id,并初始化
				let $banner_background=$('.banner_background');
				$banner_background.each(function() {
					let id=$(this).children().first().attr('id');
					initSwiper(id);
				});
			}
			/* 在取出的网页后面,加上footer. 由于footer在编辑网站的时候是不能修改的,并且很有可能样式后期会做调整.所以在取出网页的时候统一加上比较合理一点*/
			let footerContent='<footer>本站由<a class="homepage" href="http://www.asxuexi.cn">爱上学习网</a>提供技术支持</footer>';
			$body.append(footerContent);
		}
	});
	
	//查询机构名称,作为title
	$.post('orgInfo/getOrgName.do', {orgId: orgId}, function(json) {
		if (json.code==200) {
			let name=$('#'+pageName).first().text();
			$title.html(json.orgname+"-"+name);
		}
		
	});

	//让a标签href失效,通过重定向实现跳转
	$body.off('click', '.nav-editable a').on('click', '.nav-editable a', function() {
		let id=$(this).attr('id');
			location.assign('org/'+orgId+'.school?pageName='+id);
		return false;
	});


	/*
	* @方法: [所有课程页面]如果课程数据为空,提示找不到数据
	*/
	function isCourseEmpty(obj){
		if (obj.html().length==0) {
			obj.html("<div class='course_empty'>抱歉,没有想到相关的课程</div>");
		}
	}

	/*
	* @方法:向容器中写入课程item
	*/
	function writeCourseItem(containObj,data){
		containObj.append('<div class="lession_item">\
			<a href="courseDetails.do?courseId='+data.courseid+'"><img class="courseImg" src="'+data.img_name+'"></a>\
			<div class="courseName">'+data.coursename+'</div>\
			<div class="coursePrice">\
				<span class="priceTyep">'+data.typename+'</span><span class="price">¥'+data.showingprice+'</span>\
			</div>\
		</div>');
	}

	//方法：初始化轮播图.分别初始化单个swiper
	function initSwiper(id){
		if (id.search('banner1')!=-1) {
			var swiper11 = new Swiper ('#'+id, {
			    observer:true,//修改swiper自己或子元素时，自动初始化swiper
			    observeParents:true,//修改swiper的父元素时，自动初始化swiper
			    loop: true,
			    autoplay:{
			    	disableOnInteraction: false,
			    },
			    effect:'slide',
			    // 分页器
			   	pagination: {
			      el: '.swiper-pagination',
			    },
			    // 前进后退按钮
			    navigation: {
			      nextEl: '.swiper-button-next',
			      prevEl: '.swiper-button-prev',
			    },
			});
			//禁止鼠标拖动滑动
			swiper11.allowTouchMove= false;
			//鼠标悬停,停止轮播;鼠标离开,重新轮播
			swiper11.el.onmouseover = function(){
			  	swiper11.autoplay.stop();
			}
			swiper11.el.onmouseleave = function(){
			  	swiper11.autoplay.start();
			}
		} else if (id.search('banner2')!=-1) {
			var swiper22=new Swiper('#'+id,{
				observer:true,
				observeParents:true,
				loop: true,
			    autoplay:{
			    	disableOnInteraction: false,
			    },
			    effect:'cube',
			    slideShadows:false,
			    shadow: false,
			    // 分页器
			   	pagination: {
			      el: '.swiper-pagination',
			    },
			    // 前进后退按钮
			    navigation: {
			      nextEl: '.swiper-button-next',
			      prevEl: '.swiper-button-prev',
			    },
			});
			//禁止鼠标拖动滑动
			swiper22.allowTouchMove= false;
			//鼠标悬停,停止轮播;鼠标离开,重新轮播
			swiper22.el.onmouseover = function(){
			  	swiper22.autoplay.stop();
			}
			swiper22.el.onmouseleave = function(){
			  	swiper22.autoplay.start();
			}
		} else if(id.search('banner3')!=-1){
			var swiper33=new Swiper('#'+id,{
				observer:true,
				observeParents:true,
				loop: true,
			    autoplay:{
			    	disableOnInteraction: false,
			    },
			    effect:'flip',
			    // 分页器
			   	pagination: {
			      el: '.swiper-pagination',
			    },
			    // 前进后退按钮
			    navigation: {
			      nextEl: '.swiper-button-next',
			      prevEl: '.swiper-button-prev',
			    },
			});
			//禁止鼠标拖动滑动
			swiper33.allowTouchMove= false;
			//鼠标悬停,停止轮播;鼠标离开,重新轮播
			swiper33.el.onmouseover = function(){
			  	swiper33.autoplay.stop();
			}
			swiper33.el.onmouseleave = function(){
			  	swiper33.autoplay.start();
			}
		} else {
			var swiper44=new Swiper('#'+id,{
				observer:true,
				observeParents:true,
				loop: true,
				direction: 'vertical',
			    autoplay:{
			    	disableOnInteraction: false,
			    },
			    // 分页器
			   	pagination: {
			      el: '.swiper-pagination',
			    },
			    // 前进后退按钮
			    navigation: {
			      nextEl: '.swiper-button-next',
			      prevEl: '.swiper-button-prev',
			    },
			});
			//禁止鼠标拖动滑动
			swiper44.allowTouchMove= false;
			//鼠标悬停,停止轮播;鼠标离开,重新轮播
			swiper44.el.onmouseover = function(){
			  	swiper44.autoplay.stop();
			}
			swiper44.el.onmouseleave = function(){
			  	swiper44.autoplay.start();
			}
		}
	}
});