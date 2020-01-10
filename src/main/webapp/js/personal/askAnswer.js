/**
 * 
 */
$(document).ready(function(){
	
//	首先请求课程数据
	seedData(1,true);
/**
 * 请求数据
 * page 请求页数
 * reload 是否重新加载
 * */	
	function seedData(page,reload){
		//		判断查询表
		var sortQuestion=$(".active").attr("id");
		if(sortQuestion=="org"){
			table=1;
		}else if(sortQuestion=="course"){
			table=0;
		}
		$.post("PersonalAskAnswer/listAskAnswer.action",{table:table,page:page,rows:8},function(data){
				var ss="";
				if(reload){
					totle=data.totalPages;
					pageAddHtml(".paginator",totle);
				}
				$(".showCourseed").empty();
				if(data.date.length==0){
					ss='<ul id="content_ul" class="content_ul_active">您还没有提问，快去机构或课程详情提问吧～</ul>';
				}else{
					if(table==1){
						table="课程"
						for(var i=0;i<data.date.length;i++){
							var aa="";
							if(data.date[i].answer){
								 aa='<div class="answer"> 答：<span>'+data.date[i].answer+'</span> <span class="answerTime">'+data.date[i].gmt_modified+'</span>	</div>';
							}
							ss=ss+'<div class="questionAnswerInfo">您对<a  target="view_window" href="courseDetails.do?courseId='+data.date[i].course_id+'">'+data.date[i].coursename+'</a>'+table+'提问：<br><div class="question">	问：<span>'+data.date[i].question+'?</span>\
							<span class="questionTime">'+data.date[i].gmt_create+'</span></div>'+aa+'</div>'
						}
					}else{
						table="机构"
						for(var i=0;i<data.date.length;i++){
							var aa="";
							if(data.date[i].answer){
								 aa='<div class="answer"> 答：<span>'+data.date[i].answer+'</span> <span class="answerTime">'+data.date[i].gmt_modified+'</span>	</div>';
							}
							ss=ss+'<div class="questionAnswerInfo">您对<a  target="view_window" href="pagers/orgDetail.jsp?orgId='+data.date[i].org_id+'">'+data.date[i].orgname+'</a>'+table+'提问：<br><div class="question">	问：<span>'+data.date[i].question+'?</span>\
							<span class="questionTime">'+data.date[i].gmt_create+'</span></div>'+aa+'</div>'
						}
					}
				}
				
			$(".showCourseed").append(ss)
		})
	}
	
	
  $("#sortQuestion li").on("click",function(event){
	  $("#sortQuestion li").removeClass("active")
	  $(event.target).addClass("active");
	 seedData(1,true)
  })
  
  
  var totle=3;//总页数
  
	function pageAddHtml(id,totle){
		$(id).empty();
		if(totle>=7){
			var app='<li class="previous successive" id="previous"><span><</span></li>\
				<li class="current"><span>1</span></li>\
				<li class="page-link"><span>2</span></li>\
				<li class="page-link"><span>3</span></li>\
				<li class="page-link"><span>4</span></li>\
			<li class="pagination-ellipsis"><span>···</span></li>\
			<li class="page-link"><span>'+totle+'</span></li>\
			<li class="next successive" id="next"><span>></span></li>';
			$(id).append(app)
		}else if(totle==6){
			var app='<li class="previous successive" id="previous"><span><</span></li>\
				<li class="current"><span>1</span></li>\
				<li class="page-link"><span>2</span></li>\
				<li class="page-link"><span>3</span></li>\
				<li class="page-link"><span>4</span></li>\
				<li class="pagination-ellipsis"><span>···</span></li>\
				<li class="page-link"><span>6</span></li>\
				<li class="next successive" id="next"><span>></span></li>';
				$(id).append(app)
		}else if(totle==5){
			var app='<li class="previous successive" id="previous"><span><</span></li>\
				<li class="current"><span>1</span></li>\
				<li class="page-link"><span>2</span></li>\
				<li class="page-link"><span>3</span></li>\
				<li class="page-link"><span>4</span></li>\
				<li class="page-link"><span>5</span></li>\
				<li class="next successive" id="next"><span>></span></li>';
				$(id).append(app);
				console.log(totle)
		}else if(totle==4){
			var app='<li class="previous successive" id="previous"><span><</span></li>\
				<li class="current"><span>1</span></li>\
				<li class="page-link"><span>2</span></li>\
				<li class="page-link"><span>3</span></li>\
				<li class="page-link"><span>4</span></li>\
				<li class="next successive" id="next"><span>></span></li>';
				$(id).append(app);
		}else if(totle==3){
			var app='<li class="previous successive" id="previous"><span><</span></li>\
				<li class="current"><span>1</span></li>\
				<li class="page-link"><span>2</span></li>\
				<li class="page-link"><span>3</span></li>\
				<li class="next successive" id="next"><span>></span></li>';
				$(id).append(app);
		}else if(totle==2){
			var app='<li class="previous successive" id="previous"><span><</span></li>\
				<li class="current"><span>1</span></li>\
				<li class="page-link"><span>2</span></li>\
				<li class="next successive" id="next"><span>></span></li>';
				$(id).append(app);
		}else if(totle==1){
			var app='<li class="previous successive" id="previous"><span><</span></li>\
				<li class="current"><span>1</span></li>\
				<li class="next successive" id="next"><span>></span></li>';
				$(id).append(app);
		}
	}
	//直接点击页数
	$(".paginator").on("click",".page-link span",function(event){
		$(event.target).parents(".paginator").find(".current").removeAttr("class").attr("class","page-link");
		var page=$(event.target).html();
		seedData(page,false);
		Paging(page);
	});
	//点击上下页
	$(".paginator").on("click",".successive span",function(event){
		var successive=$(event.target).parent().attr("id");
		var page=$(".current span").html();
			if("previous"==successive){
				if((Number(page)-1)!=0){
					seedData(Number(page)-1,false);
					$(".current").prev().removeAttr("class").attr("class","current");
					$(".current").eq(1).removeAttr("class").attr("class","page-link");
					Paging(Number(page)-1);
				}
				
			}else{
				
				if((Number(page)+1)<=totle){
						seedData(Number(page)+1,false);
						$(".current").next().removeAttr("class").attr("class","current");
						$(".current").eq(0).removeAttr("class").attr("class","page-link");
						Paging(Number(page)+1);
				}
				
			}
	});
	function Paging(page){
		if(Number(totle)>=7){
				if(page>3&&page<=Number(totle-3)){
					if(page!=4||page<=Number(totle-3)){
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
				}else if(page>=Number(totle-2)){
					if(page==Number(totle-2)){	
						if($(".paginator li").eq(6).attr("class")=="pagination-ellipsis"){
							$(".paginator li").eq(6).remove();
						}
						$(".paginator li").eq(5).remove();
						$(".paginator li").eq(3).remove();
						$(".paginator li").eq(3).find("span").html(page).parents(".page-link").removeAttr("class").attr("class","current");
						$(".paginator li").eq(3).after('<li class="page-link"><span>'+(Number(page)+1)+'</span></li>')
						$(".paginator li").eq(3).before('<li class="page-link"><span>'+(Number(page)-1)+'</span></li>');
					}else if(page==Number(totle-1)){
						$(".paginator li").eq(5).find("span").html(page).parents(".page-link").removeAttr("class").attr("class","current");
					}else if(page==Number(totle)){
						if($(".paginator li").eq(6).attr("class")=="pagination-ellipsis"){
							$(".paginator li").eq(6).remove()
						}
						$(".paginator li").eq(6).find("span").html(page).parents(".page-link").removeAttr("class").attr("class","current");
						$(".paginator li").eq(5).remove();
						$(".paginator li").eq(4).remove();
						$(".paginator li").eq(3).remove();
						$(".paginator li").eq(2).remove();
						$(".paginator li").eq(1).after('<li class="page-link"><span>'+(Number(totle)-1)+'</span></li>')
						$(".paginator li").eq(1).after('<li class="page-link"><span>'+(Number(totle)-2)+'</span></li>')
						$(".paginator li").eq(1).after('<li class="page-link"><span>'+(Number(totle)-3)+'</span></li>')
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
			}else if(Number(totle)==6){
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