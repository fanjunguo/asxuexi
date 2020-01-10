//分页方法
function pageAddHtml(id,total,searchCondition,pagenum){
	if (1==Number(pagenum)) {
		$(id).empty();
		if(total>=7){
			var app='<li class="previous successive" id="previous"><span><</span></li>\
			<li class="current"><span>1</span></li>\
			<li class="page-link"><span>2</span></li>\
			<li class="page-link"><span>3</span></li>\
			<li class="page-link"><span>4</span></li>\
		<li class="pagination-ellipsis"><span>···</span></li>\
		<li class="page-link"><span>'+total+'</span></li>\
		<li class="next successive" id="next"><span>></span></li>';
		$(id).append(app)
	}else if(total==6){
		var app='<li class="previous successive" id="previous"><span><</span></li>\
			<li class="current"><span>1</span></li>\
			<li class="page-link"><span>2</span></li>\
			<li class="page-link"><span>3</span></li>\
			<li class="page-link"><span>4</span></li>\
			<li class="pagination-ellipsis"><span>···</span></li>\
			<li class="page-link"><span>6</span></li>\
			<li class="next successive" id="next"><span>></span></li>';
			$(id).append(app)
	}else if(total==5){
		var app='<li class="previous successive" id="previous"><span><</span></li>\
			<li class="current"><span>1</span></li>\
			<li class="page-link"><span>2</span></li>\
			<li class="page-link"><span>3</span></li>\
			<li class="page-link"><span>4</span></li>\
			<li class="page-link"><span>5</span></li>\
			<li class="next successive" id="next"><span>></span></li>';
			$(id).append(app);
	}else if(total==4){
		var app='<li class="previous successive" id="previous"><span><</span></li>\
			<li class="current"><span>1</span></li>\
			<li class="page-link"><span>2</span></li>\
			<li class="page-link"><span>3</span></li>\
			<li class="page-link"><span>4</span></li>\
			<li class="next successive" id="next"><span>></span></li>';
			$(id).append(app);
	}else if(total==3){
		var app='<li class="previous successive" id="previous"><span><</span></li>\
			<li class="current"><span>1</span></li>\
			<li class="page-link"><span>2</span></li>\
			<li class="page-link"><span>3</span></li>\
			<li class="next successive" id="next"><span>></span></li>';
			$(id).append(app);
	}else if(total==2){
		var app='<li class="previous successive" id="previous"><span><</span></li>\
			<li class="current"><span>1</span></li>\
			<li class="page-link"><span>2</span></li>\
			<li class="next successive" id="next"><span>></span></li>';
			$(id).append(app);
	}else if(total==1){
		var app='<li class="previous successive" id="previous"><span><</span></li>\
			<li class="current"><span>1</span></li>\
			<li class="next successive" id="next"><span>></span></li>';
			$(id).append(app);
	}
	}
	
	
		//直接点击页数
		$(".paginator").off("click",".page-link span").on("click",".page-link span",function(event){
			$(event.target).parents(".paginator").find(".current").removeAttr("class").attr("class","page-link");
			var page=$(event.target).html();
			Paging(page,total)
			//请求搜索数据
			searchCondition.pageNum=page;
			search(searchCondition);
		});
		//点击上下页
		$(".paginator").off("click",".successive span").on("click",".successive span",function(event){
			var successive=$(event.target).parent().attr("id");
			var page=$(".current span").html();
				if("previous"==successive){
					if((Number(page)-1)!=0){
						$(".current").prev().removeAttr("class").attr("class","current");
						$(".current").eq(1).removeAttr("class").attr("class","page-link");
						Paging(Number(page)-1,total);
						//请求搜索数据
						searchCondition.pageNum=Number(page)-1;
						search(searchCondition);
					}
				}else{
					
					if((Number(page)+1)<=total){
						$(".current").next().removeAttr("class").attr("class","current");
						$(".current").eq(0).removeAttr("class").attr("class","page-link");
						Paging(Number(page)+1,total);
						//请求搜索数据
						searchCondition.pageNum=Number(page)+1;
						search(searchCondition);
					}
				}
		});
}
//分页选中效果
function Paging(page,total){
	if(Number(total)>=7){
			if(page>3&&page<=Number(total-3)){
				if(page!=4||page<=Number(total-3)){
					$(".paginator li").eq(5).remove();
				}
				$(".paginator li").eq(3).remove();
				if($(".paginator li").eq(2).attr("class")!="pagination-ellipsis"){
					$(".paginator li").eq(2).remove();
					$(".paginator li").eq(2).before('<li class="pagination-ellipsis"><span>···</span></li>')
				}
				if($(".paginator li").eq(4).attr("class")!="pagination-ellipsis"){
					$(".paginator li").eq(4).before('<li class="pagination-ellipsis"><span>···</span></li>')
				}
				$(".paginator li").eq(3).find("span").html(page).parents(".page-link").removeAttr("class").attr("class","current");
				$(".paginator li").eq(3).after('<li class="page-link"><span>'+(Number(page)+1)+'</span></li>')
				$(".paginator li").eq(3).before('<li class="page-link"><span>'+(Number(page)-1)+'</span></li>');
			}else if(page>=Number(total-2)){
				if(page==Number(total-2)){	
					if($(".paginator li").eq(6).attr("class")=="pagination-ellipsis"){
						$(".paginator li").eq(6).remove();
					}
					$(".paginator li").eq(5).remove();
					$(".paginator li").eq(3).remove();
					$(".paginator li").eq(3).find("span").html(page).parents(".page-link").removeAttr("class").attr("class","current");
					$(".paginator li").eq(3).after('<li class="page-link"><span>'+(Number(page)+1)+'</span></li>')
					$(".paginator li").eq(3).before('<li class="page-link"><span>'+(Number(page)-1)+'</span></li>');
				}else if(page==Number(total-1)){
					$(".paginator li").eq(5).find("span").html(page).parents(".page-link").removeAttr("class").attr("class","current");
				}else if(page==Number(total)){
					if($(".paginator li").eq(6).attr("class")=="pagination-ellipsis"){
						$(".paginator li").eq(6).remove()
					}
					$(".paginator li").eq(6).find("span").html(page).parents(".page-link").removeAttr("class").attr("class","current");
					$(".paginator li").eq(5).remove();
					$(".paginator li").eq(4).remove();
					$(".paginator li").eq(3).remove();
					$(".paginator li").eq(2).remove();
					$(".paginator li").eq(1).after('<li class="page-link"><span>'+(Number(total)-1)+'</span></li>')
					$(".paginator li").eq(1).after('<li class="page-link"><span>'+(Number(total)-2)+'</span></li>')
					$(".paginator li").eq(1).after('<li class="page-link"><span>'+(Number(total)-3)+'</span></li>')
					$(".paginator li").eq(1).after('<li class="pagination-ellipsis"><span>···</span></li>')
				}
			}else if(page<=3){
				if(page==3){
					if($(".paginator li").eq(5).attr("class")!="pagination-ellipsis"){
						$(".paginator li").eq(5).remove();
					}
					$(".paginator li").eq(4).remove();
					$(".paginator li").eq(2).remove();
					$(".paginator li").eq(2).find("span").html(page).parents(".page-link").removeAttr("class").attr("class","current");
					$(".paginator li").eq(2).after('<li class="page-link"><span>'+(Number(page)+1)+'</span></li>')
					$(".paginator li").eq(2).before('<li class="page-link"><span>'+(Number(page)-1)+'</span></li>');
				}else if(page==2){
					$(".paginator li").eq(page).find("span").html(page).parents(".page-link").removeAttr("class").attr("class","current");
				}else if(page==1){
					if($(".paginator li").eq(6).attr("class")=="pagination-ellipsis"){
						$(".paginator li").eq(6).remove()
					}
					$(".paginator li").eq(page).find("span").html(page).parents(".page-link").removeAttr("class").attr("class","current");
					$(".paginator li").eq(5).remove();
					$(".paginator li").eq(4).remove();
					$(".paginator li").eq(3).remove();
					$(".paginator li").eq(2).remove();
					$(".paginator li").eq(1).after('<li class="pagination-ellipsis"><span>···</span></li>')
					$(".paginator li").eq(1).after('<li class="page-link"><span>'+4+'</span></li>')
					$(".paginator li").eq(1).after('<li class="page-link"><span>'+3+'</span></li>')
					$(".paginator li").eq(1).after('<li class="page-link"><span>'+2+'</span></li>')
				}
			}
		}else if(Number(total)==6){
			if(page<=3){
				$(".paginator li").eq(5).remove();
				$(".paginator li").eq(2).remove();
				$(".paginator li").eq(1).after('<li class="page-link"><span>'+2+'</span></li>')
				$(".paginator li").eq(4).after('<li class="pagination-ellipsis"><span>···</span></li>')
				$(".paginator li").eq(page).find("span").html(page).parents(".page-link").removeAttr("class").attr("class","current");
			}else{
				$(".paginator li").eq(5).remove();
				$(".paginator li").eq(4).after('<li class="page-link"><span>'+5+'</span></li>')
				$(".paginator li").eq(page).find("span").html(page).parents(".page-link").removeAttr("class").attr("class","current");
				$(".paginator li").eq(2).remove();
				$(".paginator li").eq(1).after('<li class="pagination-ellipsis"><span>···</span></li>')
			}
		}else{
			$(".paginator li").eq(page).find("span").html(page).parents(".page-link").removeAttr("class").attr("class","current");
		}
}


function search(searchCondition){
	$.post("searchForResult.do",searchCondition,function(data){
		let html="";
		if (0!=data.pagedata.length) {
			// 加载分页选择器
			pageAddHtml(".paginator",data.toatlpage,searchCondition,data.pagenum);
			Paging(data.pagenum,data.toatlpage);
			$(".list_pager").show();
		}else{
			$(".list_pager").hide();
		}
		if ("2"==searchCondition.type) {
			// 加载机构信息
			if (0==data.pagedata.length) {
				html="<div class='no_result'>对不起，没有找到符合条件的机构</div>";
			}else{
				for (let i = 0; i < data.pagedata.length; i++) {
					html+=`
					<div class="list_item org_item">
						<a class="org_img" target="_blank" href="pagers/orgDetail.jsp?orgId=${data.pagedata[i].org_id}">
							<img src="${data.pagedata[i].logo_name}">
						</a>
						<div class="org_card">
							<div class="org_info">
								<div class="org_name">
									<a target="_blank" href="pagers/orgDetail.jsp?orgId=${data.pagedata[i].org_id}">${data.pagedata[i].org_name}</a>	
								</div>
								<div class="org_scores icomoon" style="display: none">
									<div class="score_stars">
										<span class="bottom_stars"><a></a><a></a><a></a><a></a><a></a></span>		
										<span class="covered_stars"><a></a><a></a><a></a><a></a><a></a></span>		
									</div>
									<div class="score_value">4.5分</div>	
								</div>
								<div class="org_address">${data.pagedata[i].org_address}</div>
									<div class="map icomoon" data-geo-point="${data.pagedata[i].geo_point}">查看地图</div>
								</div>
								<div class="org_courses">`;
					let topCourseListLength = data.pagedata[i].top_course_list.length;
					for (let j = 0; j < topCourseListLength; j++) {
						if (!$.isEmptyObject(data.pagedata[i].top_course_list[0])) {
							html +=`<a class="course_card" target="_blank" href="courseDetails.do?courseId=${data.pagedata[i].top_course_list[j].course_id}">
										<div class="course_img">
											<img src="${data.pagedata[i].top_course_list[j].img_name}">
										</div>
										<div class="course_name">${data.pagedata[i].top_course_list[j].course_name}</div>
									</a>`;
						}
					}
					html+=`		</div>
							</div>
						</div>
					</div>`;
				}
			}
		}else{
			// 加载课程信息
			if (0==data.pagedata.length) {
				html="<div class='no_result'>对不起，没有找到符合条件的课程</div>";
			}else{
				for (let i = 0; i < data.pagedata.length; i++) {
					html+=`
					<div class="list_item course_item">
						<a class="course_img" target="_blank" href="courseDetails.do?courseId=${data.pagedata[i].course_id}">
							<img src="${data.pagedata[i].course_img[0].img_name}">
						</a>
						<div class="course_info">
							<div class="course_name">
								<a target="_blank" href="courseDetails.do?courseId=${data.pagedata[i].course_id}">${data.pagedata[i].course_name}</a>
							</div>
							<div class="org_name">
								<a target="_blank" href="pagers/orgDetail.jsp?orgId=${data.pagedata[i].org_id}">${data.pagedata[i].org_name}</a>
							</div>`;
					let packageListLength = data.pagedata[i].package_list.length;
					if (packageListLength > 1) {
						html+=`
							<div class="course_price">
								<span class="price_symbol">¥</span>
								<span class="price_value">${data.pagedata[i].package_list[0].package_price}</span>
								<span class="price_symbol"> - ¥</span>
								<span class="price_value">${data.pagedata[i].package_list[packageListLength-1].package_price}</span>
							</div>`;
					}else{
						html+=`
							<div class="course_price">
								<span class="price_symbol">¥</span>
								<span class="price_value">${data.pagedata[i].package_list[0].package_price}</span>
							</div>`;
					}
					html+=`
						</div>
					</div>`;
				}
			}
		}
		$(".result_list").html(html);
	});
}

function addFilterComponent(componentId,componentLabel){
	let html='<div id="'+componentId+'" class="filter_component">'+
					'<div class="component_label">'+
						'<label>'+componentLabel+'</label>'+
						'<div><span class="selected">全部</span></div>'+
					'</div>'+
					'<div class="tags">'+
					'</div>'+
			 '</div>';
	$(".filter_option").append(html);
}

function addSubjectFilter(subjectList){
	addFilterComponent("subject_filter","主题");
	let html='';
	for (let i = 0; i < subjectList.length; i++) {
		html +='<div class="tag_group">'+
					'<span id='+subjectList[i].sort_id+' class="parent" level='+subjectList[i].sort_grade+'>'+subjectList[i].sort_name+'</span>'+
				'</div>';
	}
	$("#subject_filter").find(".tags").append(html);
}

function addCategoryFilter(subjectList,categoryList){
	for (let i = 0; i < subjectList.length; i++) {
		let subjectId=subjectList[i].sort_id;
		addFilterComponent(subjectId,"分类");
		let html='';
		for (let i = 0; i < categoryList[subjectId].length; i++) {
			if (0==categoryList[subjectId][i].subcategory.length) {
				html +='<div class="tag_group">'+
							'<span id='+categoryList[subjectId][i].sort_id+' class="parent" level='+categoryList[subjectId][i].sort_grade+'>'+categoryList[subjectId][i].sort_name+'</span>'+
					   '</div>';
			}else{
				html +='<div class="tag_group">'+
							'<span id='+categoryList[subjectId][i].sort_id+' class="parent has_children" level='+categoryList[subjectId][i].sort_grade+'>'+categoryList[subjectId][i].sort_name+'</span>'+
							'<div class="children">'+
								'<div class="parent_name">'+categoryList[subjectId][i].sort_name+'</div>'+
								'<div class="children_names">'+
									'<div class="child"><span id='+categoryList[subjectId][i].sort_id+' class="child_name all_children">全部</span></div>';
				for (let j = 0; j < categoryList[subjectId][i].subcategory.length; j++) {
					html +='<div class="child"><span id='+categoryList[subjectId][i].subcategory[j].sort_id+' class="child_name" level='+categoryList[subjectId][i].subcategory[j].sort_grade+'>'+categoryList[subjectId][i].subcategory[j].sort_name+'</span></div>';
				}				
				html +=	'</div>'+
					'</div>'+
				'</div>';
			}
			
		}
		$(html).appendTo($("#"+subjectId+".filter_component .tags"))
			.parents(".filter_component").hide();
	}
}

function addAreaFilter(areaList){
	addFilterComponent("area_filter","地区");
	let html='';
	for (let i = 0; i < areaList.length; i++) {
		html +='<div class="tag_group">'+
					'<span id='+areaList[i].ID+' class="parent" level='+areaList[i].LevelType+'>'+areaList[i].Name+'</span>'+
				'</div>';
	}
	$("#area_filter").find(".tags").append(html);
}

$(document).ready(function(){
	var $search_keyword=$("#search_keyword");
	var $search_content_type=$("#search_content_type");
	var $search_city_id=$("#search_city_id");
	var $search_city_name=$("#search_city_name");
	var $search_sort_id=$("#search_sort_id");
	var $searchBorder=$("#search-border");
	var $search_sort_level=$("#search_sort_level");
	var type="";
	var filterData;
	$searchBorder.find(".keyword").val($search_keyword.val());
	if ("org"==$search_content_type.val()) {
		$searchBorder.find("input.searchContentType").val("org");
		$searchBorder.find(".selected_type").text("机构").attr("value","org");
		$searchBorder.find(".unselected_type").text("课程").attr("value","course");
		$(".page_index .search_city_name").after("机构: ");
		type="2";
	}
	if ("course"==$search_content_type.val()) {
		$searchBorder.find("input.searchContentType").val("course");
		$searchBorder.find(".selected_type").text("课程").attr("value","course");
		$searchBorder.find(".unselected_type").text("机构").attr("value","org");
		$(".page_index .search_city_name").after("课程: ");
		type="1";
	}
	var searchCondition={
		type:type,
		keyword:$search_keyword.val(),
		sortId:$search_sort_id.val().trim()==""?"":JSON.stringify([$search_sort_id.val()]),
		cityId:$search_city_id.val(),
		sortLevel:$search_sort_level.val(),
		pageNum:"1",
		pageRows:"12",
		order:"default",
		latitude:"",
		longitude:"",
	}
	//请求筛选条件：包括所有分类，地区等
	//TODO 后期增加更多的筛选条件,首先增加品牌的筛选项
	$.post("getFilterComponent.do",
			{cityId:$search_city_id.val()},
			function(data){
				filterData=data;
				addSubjectFilter(data.subject);
				addCategoryFilter(data.subject,data.category);
				addAreaFilter(data.area);
				let sortId=$search_sort_id.val();
				if (""!=sortId) {
					//根据sortid，设定相应的条件为选中状态；在已选择条件的容器中增加初始选择的条件
					let level=$("[id="+sortId+"]").addClass("selected").attr("level");
					let sortName=$(".selected[id="+sortId+"][level="+level+"]").text();
					$(".filter_selected .sort_condition").text(sortName)
						.attr("id",sortId).addClass("has_condition").show()
						.nextAll(".clear_condition").show();
					if ("1"==level||"2"==level||"3"==level) {
						$(".selected[id="+sortId+"]").show().parents(".tags")
							.prev(".component_label").find("span")
							.removeClass("selected");
						if ("2"==level||"3"==level) {
							$(".selected[id="+sortId+"]").show().parents(".tags")
								.prev(".component_label").find("span")
								.removeClass("selected");
							let parentId=$(".selected[id="+sortId+"]").parents(".filter_component").show().attr("id");
							$(".parent[id="+parentId+"]").addClass("selected").parents(".tags")
								.prev(".component_label").find("span")
								.removeClass("selected");
							if ("3"==level) {
								$(".selected[id="+sortId+"]").parents(".children").prev(".parent")
									.text($(".selected[id="+sortId+"]").text())
									.addClass("selected");
							}
						}
					}
				}
				for (let i = 0; i < data.subject.length; i++) {
					let id=data.subject[i].sort_id;
					//为分类筛选条件添加事件
					$(".filter_component[id="+id+"] .tag_group .parent").on({
						//鼠标移入时，展示下级筛选条件
						mouseenter:function(){
							$(this).next(".children").show().on({
								mouseenter:function(){
									$(this).show();
								},
								mouseleave:function(){
									$(this).hide();
								}
							});
						},
						//鼠标移出时，隐藏下级筛选条件
						mouseleave:function(){
							$(this).next(".children").hide();
						}
					});
					//为第二级的分类筛选条件增加点击事件
					$(".filter_component[id="+id+"] .tag_group .child_name").on("click",
						function(){
							let text=$(this).text();
							//如果选中的是全部选项，展示第一级的分类名称
							if ($(this).hasClass("all_children")) {
								text=$(this).parents(".children_names").prev(".parent_name").text();
							}
							let sortConditionId=$(this).attr("id");
							let categoryId=$(this).parents(".filter_component").attr("id");
							resetCategoryFilter(categoryId);
							$(".filter_component[id="+categoryId+"]").find(".component_label span")
								.removeClass("selected");
							//为选中的项目增加选中效果
							$(this).addClass("selected").parents(".children").hide()
								.prev(".parent").text(text).addClass("selected");
							//根据选中的项目，更改已选择条件
							$(".filter_selected .sort_condition").text(text).attr("id",sortConditionId)
								.addClass("has_condition").show().nextAll(".clear_condition").show();
							//修改筛选条件，查询数据
							searchCondition.sortId=JSON.stringify([sortConditionId]);
							search(searchCondition);
					});
					//为分类的全部按钮添加事件
					$(".filter_component[id="+id+"] .component_label span").on("click",
						function(){
						//展示分类筛选的初始状态
						resetCategoryFilter(id);
						//更改已选择条件
						let text=$(".parent[id="+id+"]").text();
						$(".filter_selected .sort_condition").text(text).attr("id",id)
							.addClass("has_condition").show().nextAll(".clear_condition").show();
						//修改筛选条件，查询数据
						let sortIdArray=new Array();
						for (let j = 0; j < data.category[id].length; j++) {
							sortIdArray.push(data.category[id][i].sort_id);
						}
						searchCondition.sortId=JSON.stringify(sortIdArray);
						search(searchCondition);
					});
				}	
	});
	//根据条件查询结果,展示初始结果页面
	search(searchCondition);
	
	function resetCategoryFilter(categoryId){
		$(".filter_component[id="+categoryId+"]").find(".component_label span")
			.addClass("selected");
		let tagGroup=$(".filter_component[id="+categoryId+"]").find(".tag_group");
		for (let i = 0; i < tagGroup.length; i++) {
			let parentName=$(tagGroup[i]).find(".parent_name").text();
			$(tagGroup[i]).find(".parent").text(parentName).removeClass("selected");
			$(tagGroup[i]).find(".child_name").removeClass("selected");
		}
	}
	
	//为主题筛选条件添加点击事件
	$(".filter_option").on("click",
		"#subject_filter .tag_group .parent",
		function(){
			let text=$(this).text();
			let id=$(this).attr("id");
			//为选中的项目增加选中效果，移除其他未选中项的选中效果
			$(this).addClass("selected").parent(".tag_group").prevAll()
				.children(".parent").removeClass("selected");
			$(this).parent(".tag_group").nextAll().children(".parent")
				.removeClass("selected");
			$(this).parents("#subject_filter").find(".component_label span")
				.removeClass("selected");
			//根据选中的项目，更改已选择条件
			$(".filter_selected .sort_condition").text(text).attr("id",id)
				.addClass("has_condition").show().nextAll(".clear_condition").show();
			//根据选中的项目，并展示相应的分类筛选条件
			resetCategoryFilter(id);
			$(".filter_component[id="+id+"]").show();
			$(".filter_component[id!="+id+"][id!=subject_filter][id!=area_filter]").hide();
			//修改筛选条件，查询数据
			let sortIdArray=new Array();
			for (let k = 0; k < filterData.category[id].length; k++) {
				sortIdArray.push(filterData.category[id][k].sort_id);
			}
			searchCondition.sortId=JSON.stringify(sortIdArray);
			search(searchCondition);
	});
	//为主题的全部按钮添加点击事件
	$(".filter_option").on("click",
		"#subject_filter .component_label span",
		function(){
			//为选中的项目增加选中效果，移除其他未选中项的选中效果
			$(this).addClass("selected").parents("#subject_filter").find(".parent")
				.removeClass("selected");
			//清除已选择条件
			$(".filter_selected .sort_condition").text("").removeClass("has_condition").hide();
			if (0==$(".filter_selected .has_condition").length) {
				$(".filter_selected .clear_condition").hide();
			}
			//隐藏分类筛选条件
			$(".filter_component[id!=subject_filter][id!=area_filter]").hide();
			//修改筛选条件，查询数据
			searchCondition.sortId="";
			search(searchCondition);
	});
	//为地区筛选条件添加点击事件
	$(".filter_option").on("click",
		"#area_filter .tag_group .parent",
		function(){
			let text=$(this).text();
			let id=$(this).attr("id");
			//为选中的项目增加选中效果，移除其他未选中项的选中效果
			$(this).addClass("selected").parent(".tag_group").prevAll()
				.children(".parent").removeClass("selected");
			$(this).parent(".tag_group").nextAll().children(".parent")
				.removeClass("selected");
			$(this).parents("#area_filter").find(".component_label span")
				.removeClass("selected");
			//根据选中的项目，更改已选择条件
			$(".filter_selected .area_condition").text(text).attr("id",id)
				.addClass("has_condition").show().nextAll(".clear_condition").show();
			//修改筛选条件，查询数据
			searchCondition.cityId=id;
			search(searchCondition);
	});
	//为地区的全部按钮添加点击事件
	$(".filter_option").on("click",
			"#area_filter .component_label span",
			function(){
				//为选中的项目增加选中效果，移除其他未选中项的选中效果
				$(this).addClass("selected").parents("#area_filter").find(".parent")
					.removeClass("selected");
				//清除已选择条件
				$(".filter_selected .area_condition").text("").removeClass("has_condition").hide();
				if (0==$(".filter_selected .has_condition").length) {
					$(".filter_selected .clear_condition").hide();
				}
				//修改筛选条件，查询数据
				searchCondition.cityId=$search_city_id.val();
				search(searchCondition);
	});
	//为已经选择的条件添加点击事件
	$(".filter_selected .sort_condition").on("click",function(){
		let id=$(this).hide().attr("id");
		$("[id="+id+"]").parents(".filter_component")
			.find(".component_label span").trigger("click");
	});
	$(".filter_selected .area_condition").on("click",function(){
		$(this).hide();
		$("#area_filter .component_label span").trigger("click");
	});
	$(".filter_selected .clear_condition").on("click",function(){
		$(this).hide();
		$(".filter_selected .search_condition").text("").removeClass("has_condition").hide();
		//为主题的全部按钮增加选中效果，移除其他未选中项的选中效果
		$("#subject_filter .component_label span").addClass("selected").parents("#subject_filter")
			.find(".parent").removeClass("selected");
		//隐藏分类筛选条件
		$(".filter_component[id!=subject_filter][id!=area_filter]").hide();
		//为地区的全部按钮增加选中效果，移除其他未选中项的选中效果
		$("#area_filter .component_label span").addClass("selected").parents("#area_filter")
			.find(".parent").removeClass("selected");
		//修改筛选条件，查询数据
		searchCondition.sortId="";
		searchCondition.cityId=$search_city_id.val();
		search(searchCondition);
	});
	//为智能排序添加点击事件
	$(".sort_option .default_sort").on("click",function(){
		if(!$(this).hasClass("selected")){
			//此处只对价格排序做了处理
			//TODO 增加对于其他排序条件的效果处理
			$(this).addClass("selected").next(".price_sort").text("价格排序")
				.removeClass("selected sort_asc sort_desc");
			searchCondition.order="default";
			search(searchCondition);
		}
	});
	//为价格排序添加点击事件
	$(".sort_option .price_sort").on("click",function(){
		$(".sort_component").removeClass("selected");
		$(this).addClass("selected");
		if ($(this).hasClass("sort_asc")) {
			$(this).removeClass("sort_asc").addClass("sort_desc").text("价格最高");
			searchCondition.order="price_desc";
		}else{
			$(this).removeClass("sort_desc").addClass("sort_asc").text("价格最低");
			searchCondition.order="price_asc";
		}
		search(searchCondition);
	});
	//点击展示地图的图标
	$(".result_list").on("click",".org_item .org_card .org_info .map",function(){
		let geoArray = $(this).attr("data-geo-point").split(",");
		let lat=geoArray[0];
		let lon=geoArray[1];
		$("#courseAddrMap").show();
		$("body").mapWindow({
			lng:lon,
			lat:lat,
		});
	});
	
	
})