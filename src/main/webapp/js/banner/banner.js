/**
 * @说明:asxuexi首页轮播图功能相关js,包括写入轮播图html内容,以及请求后台图片和链接数据
 * @version 2.0 后台请求地址没变,但是插件更换 by junguo.fan
 */

$(document).ready(function() {
	let cityId=$(".cityname").attr('id');
	if (typeof(cityId)=='undefined') {
		$.getScript("https://pv.sohu.com/cityjson?ie=utf-8",function(data){
	  		var localIP = returnCitySN["cip"]; 
	  		// 根据ip定位相应的城市
	  		$.post('location/ipLocation.do',{ip:localIP}, function(data) {
	  			cityId=data.cityid;
	  			
	  		});
		});
	}
	getBannerImg(cityId);
	
	function getBannerImg(cityId){
		$.post('homepageBanner/getImg.do',{cityId:cityId}, function(data) {
				//写入图片文件
				let html='';
				for (var i = 0; i < data.length; i++) {
					let href=data[i].href;
					if (href.search('org')!=-1) {
						href='pagers/orgDetail.jsp?orgId='+href;
					}else if(href.search('course')!=-1){
						href='courseDetails.do?courseId='+href;
					}else if(href.search('ad'!=-1)){ //如果是广告
						href='pagers/activity/activity.jsp?id='+href;
					}
					else{
						href='';
					}
					html=html+'<div class="swiper-slide"><a target="_blank" href="'+href+'"><img src="'+data[i].src+'"></a></div>';
				}
				$('.swiper-wrapper').html(html);
				//初始化swiper
				var swiper1 = new Swiper('.swiper-container', {
		      		observer:true, //修改swiper自己或子元素时，自动初始化swiper
		      	    observeParents:true, //修改swiper的父元素时，自动初始化swiper
		      	    loop: true,
		      	    autoplay:{
		      	    	delay:2500,
		      	    	disableOnInteraction: false, //手动滑动后,依然可以自动滑动
		      	    },  	    
		      	     // 如果需要分页器
				    pagination: {
				      el: '.swiper-pagination',
				    },
				    // 如果需要前进后退按钮
				    navigation: {
				      nextEl: '.swiper-button-next',
				      prevEl: '.swiper-button-prev',
				    },
				});
			});
	}
	
	
});