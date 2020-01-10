$(document).ready(function() {
	let $body=$('body');

	$("#title_ul").click(function(event){
		$("#title_ul .active").removeClass('active')
		$(event.target).attr('class', 'active');
	
	});
	$("#classify").click(function(event) {
		$("#classify .active").removeClass('active')
		$(event.target).attr('class', 'active');
	});

//控制div显示与隐藏
	$("#title_class").click(function(event) {
		$("#class_collection").css("display","block");
		$("#school_collection").css("display","none");
	});
	$("#title_school").click(function(event) {
		$("#class_collection").css("display","none");
		$("#school_collection").css("display","block");
	});


//根据登录用户的id，获取用户收藏的课程和学校
	//课程---因为打开收藏页面，默认显示的就是课程收藏，所以请求是在加载页面的时候就提交的
	getCourseCollection();
	
/*
 * @方法:请求用户收藏的课程,并写入dom
 * @参数: currentTime-当前时间戳.用于判断失效的课程
 */
	function getCourseCollection(currentTime){
		$.post("getcourse_collection.action",{currentTime:currentTime},function(json){
			let course_html='';
			let data=json.data;
			if (data.length==0) {
				course_html='您还没有添加收藏，快去添加吧～';
				$("#content_ul").attr('class', 'content_ul_active');
			} else {
				$("#content_ul").removeClass('content_ul_active');
				for (var i = 0; i < data.length; i++) {
					course_html=course_html+'<li class="fav-item">\
								<div class="father_container">\
									<div class="img_hover"><a target="_blank" href="courseDetails.do?courseId='+data[i].course_id+'"><img class="lesson_img" src="'+data[i].img_name+'" alt="课程图片"></a></div>\
									<div  id="'+data[i].course_id+'" class="delete_hover"><img class="delete-btn" src="img/delete_fjg.png" alt="删除"></div>\
									<div class="delete_confirm">\
										<div class="txt">确定删除？</div>\
										<div class="confirm_btn">\
											<div class="btn_yes">确定</div>\
											<div class="btn_no">取消</div>\
										</div>\
									</div>\
								</div>\
								<div class="class_name">'+data[i].coursename+'</div>\
								<div class="organization_name">'+data[i].orgname+'</div></li>';
				}
			}
			$("#content ul").html(course_html);
		});
	}
//选择【全部课程】
	$("#all_course").click(function() {
		getCourseCollection();
	});
	
//选择已失效课程
	$("#useless_course").click(function() {
		var currentTime=Math.round(new Date() / 1000);
		getCourseCollection(currentTime);
	});
//点击删除按钮，删除收藏(显示删除遮罩层的时候，把删除按钮的透明度设为0)
	//方法一：一次只能显示一个删除提示
	// var targetid=null;
	// $("#content").delegate('.delete_hover','click', function(evevt) {		
	// 	//下面的if判断是为了同一时间只能显示一个遮罩层
	// 	if (targetid!=null) {
	// 		$("#"+targetid).next(".delete_confirm").css('display', 'none');			
	// 	} 
	// 	targetid=evevt.currentTarget.id;		
	// 	$("#"+targetid).next(".delete_confirm").css('display', 'block');
		
	// });

	//方法二：一次可以显示多个删除提示
	 $("#content").delegate('.delete_hover','click', function(evevt) {
	 	var targetid=evevt.currentTarget.id;
	 	$("#"+targetid).next(".delete_confirm").css('display', 'block');
	 });
//取消删除
	$("#content").delegate('.btn_no', 'click', function() {
		//var $(this).parent().parent().prev("div").attr('id');
		$($(this).parent().parent()).css('display', 'none');
	});
//确定删除
	 $body.off('click', '.btn_yes').on('click', '.btn_yes', function() {
	 	let $this=$(this);
	 	//获取删除目标的id
	 	let courseId=$this.parents('.delete_confirm').prev().attr('id');
	 	$.post('personalCollections/deleteCollection.action', {courseId: courseId}, function(data) {
	 		if (data==1) {
				$this.parents('.fav-item').remove();
			} else {
				alert("网络异常,请刷新重试");
			}
	 	});

	 });

	function getSchool(){
		var currentTime=Math.round(new Date() / 1000);
		$.post('getschool_collection.action',{currentTime:currentTime}, function(data) {
			var school_html='';
			if (data[0].length==0) {
				school_html='您还没有添加收藏';
				$("#schoollist_ul").attr('class', 'content_ul_active');
			}else{
				$("#schoollist_ul").removeClass('content_ul_active');
			
			//先取出学校数据，并写入html
				for (var i = 0; i < data[0].length; i++) {
					school_html=school_html+'<li id="'+data[0][i].org_id+'" class="school_list">\
						<div class="org_del_confirm">\
							<div class="txt">确定移除收藏？</div>\
								<div class="confirm_btn">\
									<div class="org_del_yes">确定</div>\
									<div class="org_del_no">取消</div>\
						</div>\
						</div>\
						<!-- 机构信息 -->\
						<div class="organization_info">\
						   <div class="logo"><img class="organization_logo" src="'+data[0][i].name+'" alt="logo"></div>\
						   <div class="class_name">'+data[0][i].orgname+'</div>\
						   <div class="tel"><span>☎️</span>:'+data[0][i].tel+'</div>\
						   <div class="address">'+data[0][i].address+'</div>\
						</div>\
						<div class="school_edit">\
							<div class="btn_edit_more"><a target="_blank" href="pagers/orgDetail.jsp?orgId='+data[0][i].org_id+'">查看详情</a></div>\
							<div class="btn_edit_cancel">取消收藏</div>\
						</div>\
						<!-- 推荐课程list -->\
						<div class="recommend_list">\
							<ul id="recomment'+i+'"></ul>\
						</div>\
					</li>';
					
				}
			}
		$("#schoollist_ul").html(school_html);
			
			//再取出推荐课程数据，写入html
			for (var i = 0; i < data[0].length; i++) {
				var recomment_html='';
				for (var j = 0; j < data[i+1].length; j++) {
					recomment_html=recomment_html+'<li class="fav-item">\
									<div><img class="lesson_img" src="'+data[i+1][j].img_name+'" alt="课程图片"></div>\
									<div class="class_name">'+data[i+1][j].coursename+'</div>\
								</li>';
								
				}
				
			$("#recomment"+i).html(recomment_html);
			}
		});	
	}



	$("#title_school").click(function() {
		getSchool();
	});

//删除学校收藏
	$('#schoollist_ul').delegate('.btn_edit_cancel', 'click', function() {
		$(this).parent().prev().prev().css('display', 'block');
	});
	//点击取消
	$('#schoollist_ul').delegate('.org_del_no', 'click', function() {
		$(this).parents('.org_del_confirm').css('display', 'none');
	});
	//点击确定
	$('#schoollist_ul').delegate('.org_del_yes', 'click', function() {
		let $this=$(this);
		var organization_id=$this.parents('.school_list').attr('id');
		$.post('personalCollections/deleteCollection.action', {orgId: organization_id}, function(result) {
			if (result==1) {
				$this.parents('.school_list').remove();
			} else {
				alert("网络异常,请刷新后重试");
			}
			
		});
		
	});

});