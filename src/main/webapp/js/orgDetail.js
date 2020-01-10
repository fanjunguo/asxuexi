$(document).ready(function(){
//	请求机构信息
  $.post("orgOverspend/orgInfo.do",{orgId:$(".body").attr("orgId")},function(data){
	  if(data){
		  if (data.status==1) {
			  // 机构状态为在线时，加载机构信息
			  $("#orgInfoName").html(data.orgname);
			  $("#orgInfoName").attr("path",data.orgid);
			  if(-1==data.licence_status){
				  $("#org_validation").remove();
			  }
			  $("#orgInfoLink a").attr("href","org/"+data.orgid+".school");
			  $("#orgInfoHead span").html(data.head);
			  $("#orgInfoTel span").html(data.tel);
			  $("#orgInfoAddr span").eq(0).html(data.MergerName);
			  $("#orgInfoAddr span").eq(1).html(data.address);
			  $("#orgInfoAddr").attr("lat",data.lat);
			  $("#orgInfoAddr").attr("lng",data.lng);
			  $("#orgInfoDes span").html(data.des);
			  $(".orgImageShow img").attr("src",data.logo)
			  if(data.isCollected==1){
				  $(".collection span").attr("class","collectionActive");
			  }
			  // 请求机构课程
			  $.post("orgOverspend/listTopCourse.do",{orgId:$(".body").attr("orgId")},function(data){
				  if (data.length != 0) {
					  var ss="";
					  for(var i=0;i<data.length;i++){
						  ss +=`
						  <a target="_blank" class="tab_card" href="courseDetails.do?courseId=${data[i].courseid}">
							  <div class="course_img">
							  	<img src="${data[i].img_name}">
							  </div>
							  <div class="course_info">
								<div class="course_name">${data[i].coursename}</div>
							  </div>
						  </a>`;
					  }
					  $(".courseInfo").append(ss)
				  }else{
					  $(".courseInfo").html("<div style='padding:20px;'>暂无课程</div>");
				  } 
			  });
			  
			  // 请求问题回答
			  requestData(1,true)
		  }else{
			  // 机构状态为下线时,展示机构已经下线的图片
			  $(".org").empty().append("<div ><img src='img/courseNO.jpg'></div>");
			  $("#questions").off("click");
			  $(".orgCourseTitle").hide();
			  $(".questionAndAnswerTitle").hide();
			  $(".questionAndAnswer").hide();
		  }
	  }else{
		  //查询不到该机构时，展示找不到机构的图片
		  $(".org").empty().append("<div ><img src='img/orgDown.jpg'></div>");
		  $("#questions").off("click");
		  $(".orgCourseTitle").hide();
		  $(".questionAndAnswerTitle").hide();
		  $(".questionAndAnswer").hide();
	  }
  });
  
  
//	收藏添加\删除
	$(".collection").on("click",function(){
		let collectionAction=$(".collection span").attr("class");
		let orgId=$(".body").attr("orgId");
		if(collectionAction==undefined){
			$.post("orgOverspend/insertCollectionOrg.action",{orgId:orgId},function(data){
				if (data==1) {
					$(".collection span").attr("class","collectionActive");
				}
			})
		}else{
			$.post("orgOverspend/UpdateCollectionOrg.action",{orgId:orgId},function(data){
				$(".collection span").removeAttr("class");
			})
		}
	})

//  展开地图
  $("#spana").on("click",function(){
	  $("#courseAddrMap").show();
		$("body").mapWindow({
			lng: $("#orgInfoAddr").attr("lng" ) ,
			lat: $("#orgInfoAddr").attr("lat" )
		})
  })
//  提出问题
  $("#questionsPanel").hide();
	$("#questions").on("click", function() {
		$.get('log_in/isLogin.action', function() {
			$("#questionsPanel").show();
			$("#questionContent textarea").val("")
		});
	});
	$(".colsePanel").on("click", function() {
		$("#questionsPanel").hide();
		$filterWords.text('');
	});
	
	
	let $filterWords=$('.filter_words');
	$("#ask").on("click",function(){
		if($("#questionContent textarea").val().trim()!=""){
			filterWorf(1);//验证
		}else{
			$filterWords.text('提问内容不能为空');
		}
	});
	
	//问题输入框获得焦点时,提示文本消失
	$('.filter_worf').focusin(function() {
		$filterWords.text('');
	});
	
	/**
	 * 验证是否存在敏感词
	 * */	
	function filterWorf(sort){
		var array=new Array();
		var returnV=true;
		var inputLength=$(".filter_worf").length;
		for(var i=0;i<inputLength;i++){
			var object={name:"",text:""}
			object.name=$(".filter_worf").eq(i).attr("id");
			object.text=$(".filter_worf").eq(i).val();
			array.push(object);
		}
		var word=JSON.stringify(array);
		$.post("filterWord/isFilterWord.do",{context:word},function(data){
			 let text='您的问题中包含违禁词,请修改: ';
			 let sperator=' '
			 for(var i=0;i<data.length;i++){
				 if(data[i].text!=""){
					 text=text+data[i].text+sperator;
					 returnV=false;
				 }
			 }
			 
			 if(returnV){//调用另外post方法保存
				 savePost(sort);
			 }else{
				 $filterWords.text(text);
			 }
		 })
	}
	function savePost(sort){
		if(sort==1){
			$.post("orgOverspend/insertQuestion.action",{
				question:$("#questionContent textarea").val(),
				orgId:$(".body").attr("orgId")
			},function(data){
				if(data==1){
					alert("提问成功!提问的问题可在[个人中心]查看")
					$("#questionsPanel").hide();
					$filterWords.text('');
					location.reload(true);
				}else{
					alert("网络异常,请刷新后重试");
				}
			});
		}
	}
	

/**
 * page 跳转页数
 * isPage 是否重新加载页数
 * */	
	function requestData(page,isPage){
		$.post("orgOverspend/listQuestion.do",
				{page:page,rows:5,orgId:$(".body").attr("orgId")},
				function(data){
//					添加分页
					if(isPage){
						pageAddHtml($(".paginator"),data.totalPages);
						total=data.totalPages;
					}
					dataParsing(data.date)
				});
	}
	function dataParsing(data){
		document.getElementsByClassName("questionAndAnswerTitle")[0].scrollIntoView();
		$(".QAATable table").empty()
		var ss="";
		for(var i=0;i<data.length;i++){
			if(data[i].photo==null||data[i].photo==""){
				photo="img/img3.jpg"
			}else{
				photo=data[i].photo;
			}
			if(data[i].answer==null||data[i].answer==""){
				answers="暂未回答！！！"
			}else{
				answers=data[i].answer;
			}
			ss=ss+'<tr id="'+data[i].id+'"><td ><div class="userImg"><img alt="" src="'+photo+'"></div>\
			</td><td><div class="userName">'+data[i].name+'</div><div class="userQuestion">问：<span>'+data[i].question+'？</span></div>\
			<div class="orgAnswer">答：<span>'+answers+'</span></div></td><td><div class="QAAtime">'+data[i].gmt_create+'</div></td></tr>'
		}
		$(".QAATable table").append(ss);
	}
	
	
//	分页
	function pageAddHtml(contanerObj,total){
		contanerObj.empty();
		let app='';
		if(total>=7){
			 app='<li class="previous successive" id="previous"><span>上一页</span></li>\
				<li class="current"><span>1</span></li>\
				<li class="page-link"><span>2</span></li>\
				<li class="page-link"><span>3</span></li>\
				<li class="page-link"><span>4</span></li>\
				<li class="pagination-ellipsis"><span>···</span></li>\
				<li class="page-link"><span>'+total+'</span></li>\
				<li class="next successive" id="next"><span>下一页</span></li>';
		}else if(total==6){
			 app='<li class="previous successive" id="previous"><span>上一页</span></li>\
				<li class="current"><span>1</span></li>\
				<li class="page-link"><span>2</span></li>\
				<li class="page-link"><span>3</span></li>\
				<li class="page-link"><span>4</span></li>\
				<li class="pagination-ellipsis"><span>···</span></li>\
				<li class="page-link"><span>6</span></li>\
				<li class="next successive" id="next"><span>下一页</span></li>';
		}else if(total==5){
			 app='<li class="previous successive" id="previous"><span>上一页</span></li>\
				<li class="current"><span>1</span></li>\
				<li class="page-link"><span>2</span></li>\
				<li class="page-link"><span>3</span></li>\
				<li class="page-link"><span>4</span></li>\
				<li class="page-link"><span>5</span></li>\
				<li class="next successive" id="next"><span>下一页</span></li>';
		}else if(total==4){
			 app='<li class="previous successive" id="previous"><span>上一页</span></li>\
				<li class="current"><span>1</span></li>\
				<li class="page-link"><span>2</span></li>\
				<li class="page-link"><span>3</span></li>\
				<li class="page-link"><span>4</span></li>\
				<li class="next successive" id="next"><span>下一页</span></li>';
		}else if(total==3){
			 app='<li class="previous successive" id="previous"><span>上一页</span></li>\
				<li class="current"><span>1</span></li>\
				<li class="page-link"><span>2</span></li>\
				<li class="page-link"><span>3</span></li>\
				<li class="next successive" id="next"><span>下一页</span></li>';
		}else if(total==2){
			 app='<li class="previous successive" id="previous"><span>上一页</span></li>\
				<li class="current"><span>1</span></li>\
				<li class="page-link"><span>2</span></li>\
				<li class="next successive" id="next"><span>下一页</span></li>';
		}
		contanerObj.append(app);
	}
	
	
	//直接点击页数
	$(".paginator").on("click",".page-link span",function(event){
		$(event.target).parents(".paginator").find(".current").removeAttr("class").attr("class","page-link");
		let page=Number($(event.target).text());
		requestData(page);
		Paging(page);
		if (page==1) {
			$('.previous span').css('visibility', 'hidden');
			$('.next span').css('visibility', 'visible');
		}else if(page==total){
			$('.next span').css('visibility', 'hidden');
			$('.previous span').css('visibility', 'visible');
		}else{
			$('.next span').css('visibility', 'visible');
			$('.previous span').css('visibility', 'visible');
		}
	});
	//点击上下页
	$(".paginator").on("click",".successive span",function(event){
		let successive=$(event.target).parent().attr("id");
		let page=Number($(".current span").text());
			if("previous"==successive){
				if(page>1){
					$(".current").prev().removeAttr("class").attr("class","current");
					$(".current").eq(1).removeAttr("class").attr("class","page-link");
					Paging(page-1);
					requestData(page-1);
					$('.next span').css('visibility', 'visible');
				}
				if (page==2) {
					$('.previous span').css('visibility', 'hidden');
				}
			//点击下一页
			}else{
				if(page<total){
					$(".current").next().removeAttr("class").attr("class","current");
					$(".current").eq(0).removeAttr("class").attr("class","page-link");
					Paging(page+1);
					requestData(page+1);
					$('.previous span').css('visibility', 'visible');
				}
				//"下一页按钮yincang"
				if(page+1==total){
					$('.next span').css('visibility', 'hidden');
				}
			}
	});
	function Paging(page){
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
});