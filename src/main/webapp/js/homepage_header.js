//网页的header样式复写
document.write(`
<header>
	<div id="topbar">
		<div id="top">
			<div id="poi">
				<div class="cityname"></div>
				<span id="change"><a href="pagers/changecity.jsp">[切换]</a></span>
			</div>
			<div id="link">
  				<ul id="topmenu">
 				</ul>
			</div>
		</div>
	</div>
	<div id="header_parent">
		<div id="header">
			<div id="logo"><a href="pagers/homepage.jsp"><img src="img/logo.png"></a></div>
			<div id="search">
				<div id="search-border">
					<div class="content_type_selection">
						<div class="selected_type" value="course">课程</div>
						<div class="unselected_type" style="display:none" value="org">机构</div>
					</div>
					<form action="search.do" method="GET">
						<input class="search_input keyword" maxlength="20" size="50" name="keyword" type="text" placeholder="请输入搜索内容" autocomplete="off">
						<input class="hidden_input searchCityId" name="searchCityId" type="hidden" value="">
						<input class="hidden_input searchCityName" name="searchCityName" type="hidden" value="">
						<input class="hidden_input searchContentType" name="searchContentType" type="hidden" value="course">
						<input class="hidden_input searchSortId" name="searchSortId" type="hidden" value="">
						<button type="submit" class="btn">搜索</button>
						<div class="suggestion"></div>
					</form>
				</div>
			</div>
		</div>
	</div>
</header>`);
//判断浏览器是否禁用cookie
if (!navigator.cookieEnabled) {
	alert("为了保证网站功能的正常使用,请您打开cookie功能");
}


function getCookieValue(c_name){
	if (document.cookie.length>0){
		c_start=document.cookie.indexOf(c_name + "=");
		if (c_start!=-1){
			c_start=c_start + c_name.length+1 ;
			c_end=document.cookie.indexOf(";",c_start);
			if (c_end==-1){
				c_end=document.cookie.length;
			} 
			return unescape(document.cookie.substring(c_start,c_end));
	    } 
	}
	return "";
}
function getSuggestions(ajaxCache,inputval,container){
	//延时执行，为了尽可能保证用户输入完整的词汇后再执行搜索，一般英文输入延时300ms,中文输入延时500ms
	setTimeout(function(){
		let suggestions=[];
		if ("undefined"!=typeof ajaxCache[inputval]) {
			suggestions=ajaxCache[inputval];
		} else {
			//TODO ajax请求搜索的提示
//			$.post("", inputval,function(data){
//				suggestions=data;
//				ajaxCache[inputval]=[];
//                ajaxCache[inputval]=data;
//			});
		}
		let html="";
		for (let i = 0; i < suggestions.length; i++) {
			html +="<div>"+suggestions[i]+"</div>";
		}
		container.html(html).show();
	},"500");
}

function fillTabContent(url,params,container,type){
	$.post(url,params,function(map){
		if (map.code == 600) {
			let array = map.data;
			if (array.length != 0) {
				let tabContent = "";
				for (let i = 0; i < array.length; i++) {
					if (type == "course") {
						tabContent +=`
						<div class='tab_card'>
							<a class='course_img' target='_blank' href='courseDetails.do?courseId=${array[i].courseId}'>
								<img src='${array[i].imgUrl}' />
							</a>
							<div class='course_info'>
								<div class='course_name'>
									<a target='_blank' href='courseDetails.do?courseId=${array[i].courseId}'>
										${array[i].courseName}</a>
								</div>
								<div class='org_name'>
									<a target='_blank' href='pagers/orgDetail.jsp?orgId=${array[i].orgId}'>
										${array[i].orgName}</a>
								</div>
								<div class="course_price">
									<span class="price_symbol">¥</span>
									<span class="price_value">${array[i].minPrice}</span>
								</div>
							</div>
						</div>`;
					}
					if (type == "org") {
						tabContent +=`
						<a id='${array[i].orgid}' class='tab_card' target='_blank' href='pagers/orgDetail.jsp?orgId=${array[i].orgid}'>
							<div class='org_img'>
								<img src='${array[i].img_name}'/>
							</div>
							<div class='org_info'>
								<div class='org_name'>${array[i].orgname}</div>
								<div class='org_area'>${array[i].parentAddress}</div>
							</div>
						</a>`;
					}
				}
				container.html(tabContent);
			}else{
				container.html(`<div class="empty_tab">招募中，敬请期待。。。</div>`);
			}
		}else{
			container.html(`<div class="empty_tab">招募中，敬请期待。。。</div>`);
		}
	})
}

$(document).ready(function() {
	var $cityname=$('.cityname');
	var $searchborder=$("#search-border");
	var $selected_type=$(".selected_type");
	var $unselected_type=$(".unselected_type");
	var orgData;
	var courseData;
	
	$(".items_container").on("click",".tab_name", function(){
		let index = $(this).parent("li").index();
		$(this).parent("li").siblings("li").children(".tab_name").removeClass("active");
		$(this).addClass("active");
		$(this).parents(".item_container").children(".tab_content").hide();
		$(this).parents(".item_container").children(".tab_content").eq(index-1).css("display","flex");
	});
	
	$(".course_container").on("click",".tab_name", function(){
		let index = $(this).parent("li").index();
		let cityId = $cityname.attr("id");
		fillTabContent("items/getCourseOfRecommended.do",
				{ cityId : cityId, orderRule : index },
				$(".item_container").eq(0).children(".tab_content").eq(index-1),"course");
	});
	
	$(".org_container").on("click",".tab_name", function(){
		let index = $(this).parent("li").index();
		let cityId = $cityname.attr("id");
		fillTabContent("items/getOrgOfRecommended.do",
				{ cityId : getCookieValue("cityid"), orderRule : index },
				$(".item_container").eq(1).children(".tab_content").eq(index-1),"org");
	});
	//从cookie中查询城市信息
	if (""!=getCookieValue("cityid")&&""!=getCookieValue("cityname")) {
		$cityname.html(getCookieValue("cityname"));
		$cityname.attr('id', getCookieValue("cityid"));
		$searchborder.find("input.searchCityId").val(getCookieValue("cityid"));
		$searchborder.find("input.searchCityName").val(getCookieValue("cityname"));
		// 加载栏目内容
		fillTabContent("items/getCourseOfRecommended.do",
				{ cityId : getCookieValue("cityid"), orderRule : 1 },
				$(".item_container").eq(0).children(".tab_content").eq(0),"course");
		fillTabContent("items/getOrgOfRecommended.do",
				{ cityId : getCookieValue("cityid"), orderRule : 1 },
				$(".item_container").eq(1).children(".tab_content").eq(0),"org");
	}else{
		//搜狐接口返回内容：var returnCitySN = {"cip": "xxxxxx", "cid": "370500", "cname": "山东省东营市"};
		$.getScript("https://pv.sohu.com/cityjson?ie=utf-8",function(data){
	  		var localIP = returnCitySN["cip"]; 
	  		// 根据ip定位相应的城市
	  		$.post('location/ipLocation.do',{ip:localIP}, function(data) {
	  			$cityname.html(data.cityname);
	  			$cityname.attr('id', data.cityid);
	  			$searchborder.find("input.searchCityId").val(data.cityid);
	  			$searchborder.find("input.searchCityName").val(data.cityname);
	  			// 加载栏目内容
	  			fillTabContent("items/getCourseOfRecommended.do",
	  					{ cityId : getCookieValue("cityid"), orderRule : 1 },
	  					$(".item_container").eq(0).children(".tab_content").eq(0),"course");
	  			fillTabContent("items/getOrgOfRecommended.do",
	  					{ cityId : getCookieValue("cityid"), orderRule : 1 },
	  					$(".item_container").eq(1).children(".tab_content").eq(0),"org");
	  		});
		});
	}
	
	
	//获取顶部菜单，在后端根据获取session中的useid并处理，不从前端传入
	$.get('getTopMenu/getTopMenu.do', function(data) {
		var topmenu_html='';
		for (var i = 0; i < data.length; i++) {
			topmenu_html=topmenu_html+"<li><a id='topmenu_"+i+"'href='"+data[i].href+"'>"+data[i].name+"</a></li>"
		}
		$("#topmenu").html(topmenu_html);
	});
	
	//搜索框选择类型时的展示效果和选择后触发的事件
	$(".content_type_selection").on({
		mouseenter:function(){
			$(".unselected_type").show();
		},
		mouseleave:function(){
			$(".unselected_type").hide();
		}
	});
	$unselected_type.on("click",function(){
		$(this).hide();
		let newSelectedType=$(this).attr("value");
		let oldSelectedType=$selected_type.attr("value");
		let selectedText,unselectedText;
		if ("org"==newSelectedType) {
			selectedText="机构";
		}
		if ("course"==newSelectedType) {
			selectedText="课程";
		}
		if ("org"==oldSelectedType) {
			unselectedText="机构";
		}
		if ("course"==oldSelectedType) {
			unselectedText="课程";
		}
		$selected_type.text(selectedText).attr("value",newSelectedType);
		$(this).text(unselectedText).attr("value",oldSelectedType);
		$searchborder.find("input.searchContentType").val(newSelectedType);
	});
	
	//搜索提示功能
	var ajaxCache={};//提示得缓存，以关键字为键，提示数组为值。保证出现重复关键字时，不再向后台请求。
	var $suggestionContainer=$searchborder.find("div.suggestion");
	$searchborder.find("input.keyword").on({
		keyup:function(){
			let $val=$(this).val().trim();
			if (""!=$val) {
				//功能暂不开放 getSuggestions(ajaxCache,$val,$suggestionContainer)
			}else{
				$suggestionContainer.hide();
			}
		},
		focusout:function(){
			$suggestionContainer.hide();
		},
		focusin:function(){
			let $val=$(this).val().trim();
			if (""!=$val) {
//				getSuggestions(ajaxCache,$val,$suggestionContainer)
			}else{
				$suggestionContainer.hide();
			}
		}
	});
	$suggestionContainer.on("mousedown","div",function(){
		$searchborder.find("input.keyword").val($(this).text());
	});
	
});