$(document).ready(function() {
	var courseId= $(".course_detail").attr("id");
	var orgId= $(".org_abstract").attr("id");
	var course;
	var org;
	var isSelectedPackage=0; //是否点击选中了套餐.默认0为未选择
	var packageId; //选择的套餐id
	var $body=$('body');
	var chargeType;
	var paymentAmount; 
	function getTimestamp(dateStr, timeStr){
		let dateArray = dateStr.split("-");
		for (let i = 0; i < dateArray.length; i++) {
			dateArray[i] = Number(dateArray[i]);
		}
		let timeArray = timeStr.split(":");
		for (let i = 0; i < timeArray.length; i++) {
			timeArray[i] = Number(timeArray[i]);
		}
		let time = new Date(dateArray[0],dateArray[1]-1,dateArray[2],timeArray[0],timeArray[1])
		return time.getTime();
	}
	$.post("courseDetails/getOrg.do", { orgId: orgId, courseId: courseId }, function(map){
		org=map.data;
		if (org.status == 0) {
			// 机构违规被下线，则不展示该课程，展示提示信息
			let html=`
				<div class="cover">
					<img alt="" src="img/details/courseDown.jpg">
					<label>机构已下线</label>
				</div>`;
			$(".course_wrap").html(html);
		}else{
			// 展示机构基本信息
			// 机构logo
			$(".org_logo>img").attr("src",org.logo_name);
			// 机构名称
			$(".org_name>div").text(org.org_name);
			// 机构联系方式
			$(".org_tel>div").text(org.org_tel);
			// 机构地址
			$(".org_address>div").text(org.county_name+org.org_address);
			// 机构详情页url
			$(".org_abstract .feature_part .feature_button:nth-child(1)>a")
				.attr("href","pagers/orgDetail.jsp?orgId="+org.org_id);
			// 机构推荐课程
			let courseGroupHtml= "";
			for (let i = 0; i < org.courseList.length; i++) {
				courseGroupHtml +=`
				<a class="course_group" target="_blank" href="courseDetails.do?courseId=${org.courseList[i].course_id}" >
					<img src="${org.courseList[i].img_name}">
					<label>
						<div>${org.courseList[i].course_name}</div>
					</label>
				</a>`;
			}
			$(".org_course").html(courseGroupHtml);
		}
	});
	$.post("courseDetails/getCourse.do", { courseId: courseId }, function(map){
		course=map.data;
		if (course.status == 0) {
			// 课程状态为下线，展示提示信息
			let html=`
			<div class="cover">
				<img alt="" src="img/details/courseDown.jpg">
				<label>课程违规，已下线</label>
			</div>`;
			$(".course_detail").html(html);
		}else{
			// 左侧展示图片和视频
			$(".picture_wrap img").attr("src",course.pictureList[0].img_name);
			$(".thumb.selected img").attr("src",course.pictureList[0].img_name);
			let thumbHtml="";
			for (let i = 1; i < course.pictureList.length; i++) {
				thumbHtml += `
				<div class="thumb">
					<img src="`+course.pictureList[i].img_name+`">
				</div>`
			}
			$(".thumb_wrap").append(thumbHtml);
			if (course.videoList.length == 0) {
				$(".btn_video_play").text("");
			}else{
				$(".video_wrap>video").attr("src",course.videoList[0].video_name);
			}
			// 基本信息
			$(".course_teacher>div").text(course.course_teacher);
			// 右侧根据课程状态展示不同信息
			if (course.status == -1) {
				// 课程已删除，展示提示信息
				let html=`
				<div class="title_info">`+course.course_name+`</div>
				<div class="warning_info">该课程已下架!</div>`
				$(".top_part_right").html(html);
			}
			if (course.status == 1) {
				// 课程在线，正常显示课程信息
				// 课程名称
				$(".title_info").text(course.course_name);
				// 课程价格
				if (course.packageList.length == 1) {
					$(".price_value").text(course.packageList[0].package_price.toFixed(2));
				}else{
					$(".price_value").text(course.packageList[0].package_price.toFixed(2)+" - "
					+course.packageList[course.packageList.length-1].package_price.toFixed(2));
				}
				// 套餐信息
				let packageHtml = "";
				for (let i = 0; i < course.packageList.length; i++) {
					packageHtml += `
					<div id="`+course.packageList[i].package_id+`" class="package_info_group">`+course.packageList[i].package_name+`</div>`;
				}
				$(".package_info").append(packageHtml);
				// 隐藏课时展示区域
				$(".length_info").hide();
				// 付费方式
				chargeType=course.course_charging;
				if (chargeType ==1 ) {
					$(".charging_info .tips").text("预付费").tooltip({title:"开课前缴付所有费用",placement:"right"});
				}
				if (chargeType ==2 ) {
					$(".price_title").text("参考价格");
					$(".charging_info .tips").text("后付费").tooltip({title:"上课后根据机构发送的账单缴付相应费用",placement:"right"});
				}
			}
			// 课程的图文描述信息
			$("#course_description .tab_pane_content").html(course.course_description);
			// 课程安排信息
			let courseTimetableHtml = "";
			if (course.courseTimetable.length == 0) {
				courseTimetableHtml = "暂无课程安排"
			}
			for (let i = 0; i < course.courseTimetable.length; i++) {
				courseTimetableHtml += `
				<div class="table_group" id="${course.courseTimetable[i].course_id}">
					<div class="chapter_number">
						第<span class="number_value">${course.courseTimetable[i].chapter_number}</span>讲
					</div>
					<div class="chapter_content">
						<div class="chapter_name">${course.courseTimetable[i].chapter_name}</div>
						<div class="chapter_time">
							<span class="chapter_datetime"><span
								class="chapter_date">${course.courseTimetable[i].chapter_date}</span> <span
								class="chapter_begin">${course.courseTimetable[i].chapter_begin}</span><span>至</span><span
								class="chapter_end">${course.courseTimetable[i].chapter_end}</span> </span><span class="chapter_length"><span
								class="length_value">${course.courseTimetable[i].chapter_length}</span>课时 </span>
						</div>
					</div> `
				let begin = getTimestamp(course.courseTimetable[i].chapter_date,course.courseTimetable[i].chapter_begin);
				let end = getTimestamp(course.courseTimetable[i].chapter_date,course.courseTimetable[i].chapter_end);
				let now = new Date().getTime();
				if (now < begin) {
					courseTimetableHtml += `
						<div class="chapter_status">未开课</div>
					</div>`;
				}
				if (now >= begin && now <= end) {
					courseTimetableHtml += `
						<div class="chapter_status">进行中</div>
					</div>`;
				}
				if (now > end) {
					courseTimetableHtml += `
						<div class="chapter_status">已结束</div>
					</div>`;
				}
			}
			$("#course_timetable .tab_pane_content").html(courseTimetableHtml);
		}
	});
	// 监听缩略图的鼠标移入事件，改变大图的图片路径
	$("body").on("mouseenter",".thumb",function(){
		if (!$(this).hasClass("selected")) {
			let src= $(this).children("img").attr("src");
			$(".thumb.selected").removeClass("selected");
			$(this).addClass("selected");
			$(".picture_wrap img").attr("src",src);
		}
	})
	// 监听套餐选项的点击事件，展示套餐对应的价格和课时
	$("body").on("click",".package_info_group",function(){
		isSelectedPackage=1;
		packageId=$(this).attr("id");
		$('.warning_bar').hide();
		$('.package_info_border').removeClass('package_info_border');

		function findPackageById(packageId,packageArray){
			let packageInfo;
			for (let i = 0; i < packageArray.length; i++) {
				if (packageArray[i].package_id == packageId) {
					packageInfo = packageArray[i];
				}
			}
			return packageInfo;
		}
		let packageInfo = findPackageById(packageId,course.packageList);
		if (!$(this).hasClass("selected")) {
			$(".package_info_group.selected").removeClass("selected");
			$(this).addClass("selected");
			paymentAmount=packageInfo.package_price.toFixed(2);
			$(".price_value").text(paymentAmount);
			if (packageInfo.course_length == 0) {
				$(".length_info").slideUp("fast");
				$(".length_info>div").text("");
			}else{
				$(".length_info>div").text(packageInfo.course_length);
				$(".length_info").slideDown("fast");
			}
		}
	})
	// 监听播放视频按钮
	$("body").on({
		mouseenter: function(){
			// 鼠标移入，淡化背景
			$(".picture_wrap").css("opacity","0.6");
		},
		mouseleave: function(){
			// 鼠标移入，还原背景
			$(".picture_wrap").css("opacity","1");
		},
		click: function(){
			// 点击，显示视频区域
			$(".video_wrap").show();
		}
	},".btn_video_play")
	// 点击视频右上方按钮，隐藏视频区域，重载视频
	$("body").on("click",".video_close",function(){
		$(".video_wrap").hide();
		document.querySelector(".video_wrap>video").load();
	})

	// 点击[收藏课程]
	$("body").on("click",".top_part_left .feature_part .feature_button:nth-child(1)",function(){
		$.post("courseDetails/storeCourse.action", { courseId: courseId, orgId: orgId },function(data){
			if (data.code == 600) {
				$(".message_content").html("<div class='success'>&#xea10</div>成功加入<a href='personal/mycollection.do'>我的收藏</a>")
				$("#message_modal").modal("show");
			}else{
				if (data.code == 403) {
					$(".message_content").html("<div class='info'>!</div>您已收藏过该课程</a>")
					$("#message_modal").modal("show");
				}else{
					$(".message_content").html("<div class='warn'>!</div>收藏失败，请重试</a>")
					$("#message_modal").modal("show");
				}
			}
		});
	})
	// 点击[收藏机构]
	$("body").on("click",".org_abstract .feature_part .feature_button:nth-child(2)",function(){
		$.post("courseDetails/storeOrg.action", { orgId: orgId },function(data){
			if (data.code == 600) {
				$(".message_content").html("<div class='success'>&#xea10</div>成功加入<a href='personal/mycollection.do'>我的收藏</a>")
				$("#message_modal").modal("show");
			}else{
				if (data.code == 403) {
					$(".message_content").html("<div class='info'>!</div>您已收藏过该机构</a>")
					$("#message_modal").modal("show");
				}else{
					$(".message_content").html("<div class='warn'>!</div>收藏失败，请重试</a>")
					$("#message_modal").modal("show");
				}
			}
		});
	})


	// 点击[立即报名]
	$("body").on("click",".top_part_right .feature_part .feature_button:nth-child(1)",function(){
		/*
			1.先判断用户是否登录
			2.弹窗,用户选择或填写上课人
		*/ 
		$.get('log_in/isLogin.action', function() {
			if (isSelectedPackage==0) {
				$('.warning_bar').show();
				$('.package_info').addClass('package_info_border');
			}else{
				//查询用户已有的上课人信息
				$.get('studentManagement/getAllStudents.action', function(json) {
					if (json.code==600) {
						let data=json.data;
						let h='';
						if (data.length>0) {
							for (var i = 0,len=data.length; i < len; i++) {
								h+=`<div class="student_item" id="${data[i].id}">
					    				<p>姓名: ${data[i].name}</p>
					    				<p>联系电话: ${data[i].tel}</p>
					    			</div>`;
							}
						}else{
							h='暂无保存的上课人信息,请先新增上课人';
						}
						h=h+`<a class="a_btn new_student">新增上课人</a>`;
						$('.choose_student').html(h);
					}
				});
				$('.background, .student_info_window').show();
			}
		});
		
	})

	var $btn=$('.cancel').prev();
	var chooseStudentStyle=1; //选择上课人的方式. 1-选择 ;2-新增
	var studentId=''; //选中的学生id
	var studentName='';
	var studentTel='';
	var $wrongMessage=$('.wrong_message');

	//填写上课人信息
	$('.student_name,.phone_num').on('input propertychange', function() {
		studentName=$('.student_name').val().trim();
		studentTel=$('.phone_num').val().trim();

		if (studentName.length>0&&studentTel>0) {
			let className=$(this).attr('class');
			if (className=='phone_num') {
				$wrongMessage.html('');
			}
			changeButtonStatus(1);
		}else{
			changeButtonStatus(2);
			
		}
	});

	//切换新增上课人
	$body.on('click', '.new_student', function(event) {
		$('.choose_student').hide();
		$('.new_student_form').show();
		$('.active').removeClass('active');
		studentId='';
		chooseStudentStyle=2;
		changeButtonStatus(2);
	});

	//切换选择联系人
	$('.choose_btn').click(function() {
		chooseStudentStyle=1;
		$('.choose_student').show();
		$('.new_student_form').hide();
		changeButtonStatus(2);
		$('.student_name,.phone_num').val('');
	});

	//选择上课人
	$body.on('click', '.student_item', function() {
		$('.active').removeClass('active');
		let $this=$(this);
		$this.addClass('active');
		studentId=$this.attr('id');
		changeButtonStatus(1);
	});

	/* 方法:改变提交按钮的状态(可用/不可用)
		参数:operation:1-变为可用 2-变为不可用
	*/
	function changeButtonStatus(operation){
		if (operation==1) {
			if ($('.goahead').length==0) {
				$btn.addClass('goahead').removeClass('not_allowed');
			}
		} else {
			if ($('.not_allowed').length==0) {
				$btn.addClass('not_allowed').removeClass('goahead');
			}
		}
	}

/* 方法:创建订单
	参数:studentId-学生ID
*/
function createOrder(studentId){
	$.post('order/createOrder.action', {
		studentId: studentId,
		packageId:packageId,
		paymentAmount:paymentAmount
	}, function(json) {
		let code=json.code;
		let orderInfo=json.data;
		if (code==600) {
			//判断付款类型,跳转不同页面
			if (orderInfo.chargeType==1) {
				window.location.href='pagers/order/payment.jsp?'+json.orderId;
			} else {
				window.location.href='pagers/order/paymentResult.jsp?orderId='+json.orderId;
			}
			
		}else{
			let message;
			if (code == 411){
				message='套餐价格发生变化,请重新提交订单';
			}
			let alertHtml=`<div class="alert alert-warning order_alert">
						<a href="#" class="close" data-dismiss="alert">
							&times;
						</a>
						<strong>${message}</strong>
					</div>`;
			$('body').append(alertHtml);
			setTimeout(function(){
				$('.order_alert').remove();
			},3000);
			$('.background, .student_info_window').hide();
		}
	});
}

	//提交订单
	$body.off('click', '.goahead').on('click', '.goahead', function() { 
		if (chooseStudentStyle==1) { //选择上课人
			createOrder(studentId);
		}else{	//新增上课人
			let patt=/^1[3-9]\d{9}$/;
			if (patt.test(studentTel)) {
				$.post('studentManagement/insertNewStudent.action', {name: studentName,tel:studentTel}, function(json) {
					if (json.code==600) {
						createOrder(json.data);
					}else{
						alert('网络错误,请稍后重试');
					}
				});
			}else{
				$wrongMessage.html('*请填写正确的手机号');
			}

		}
	});
	
	//取消提交订单
	$('.cancel').click(function() {
		$('.background, .student_info_window').hide();
		chooseStudentStyle=1;
		$('.choose_student').animate({height:'show'});
		$('.new_student_form').animate({height:'hide'});
		changeButtonStatus(2);
		$('.student_name,.phone_num').val('');

	});


	// 点击[咨询课程]，展示提问面板
	$("body").on("click",".top_part_right .feature_part .feature_button:nth-child(2)",function(){
		$.get('log_in/isLogin.action', function() {
			$("#ask_content").val("");
			$("#ask_modal").modal("show");
			// 点击[确定]按钮
			$(".ask_buttons .btn-success").off("click").on("click",function(){
				let question = $("#ask_content").val().trim();
				if (question != "") {
					$.post("courseDetails/askQuestion.action",{
						courseId: courseId, 
						orgId: orgId, 
						question: $("#ask_content").val() 
					},function(map){
						if (map.code == 600) {
							// 保存成功，隐藏面板
							$("#ask_modal").modal("hide");
							// 页面跳转至锚点处
							window.location.hash = "#additional_wrap";
							// 触发事件，请求所有提问
							$("a[href='#ask_answer']").trigger("click");
						}else{
							alert("保存失败，请重试");
						}
					})
				} else {
					alert("请填写问题")
				}
				
			})
		})
	})
	// 点击[分享]
	$("body").on("click",".top_part_left .feature_part .feature_button:nth-child(2)",function(){
		alert("功能暂未开放");
		// TODO
	})
	
	function showQuestions(questionArray){
		let html="";
		for (let i = 0; i < questionArray.length; i++) {
			html += `
			<div class="question_group">
				<div class="user_info">
					<img src="${questionArray[i].photo}">
					<div class="user_name">${questionArray[i].name}</div>
				</div>
				<div class="question_text">
					<div class="ask_text">
						<div>${questionArray[i].question}</div>
						<div class="ask_date">${questionArray[i].gmt_create}</div>
					</div>`;
			if (typeof questionArray[i].answer == "undefined") {
				html += `
					<div class="answer_text">
						<div>暂无回答</div>
					</div>`;
			}else{
				html += `	
					<div class="answer_text">
						<div>${questionArray[i].answer}</div>
						<div class="answer_date">${questionArray[i].gmt_modified}</div>
					</div>`
			}
			html +=	`
				</div>
			</div>`;
		}
		$("#ask_answer .question_content").html(html);
	}
	// 点击[提问]的选项卡
	$("body").on("click","a[href='#ask_answer']",function(){
		$content = $("#ask_answer .tab_pane_content");
		$questionContent = $("#ask_answer .question_content");
		$.post("courseDetails/listQuestions.do",{
			courseId: courseId, 
			page: 1, 
			rows: 10
		},function(map){
			if (map.code == 401) {
				$content.html("发生错误，请联系网站工作人员。");
			}
			if (map.code == 600) {
				let questionArray = map.data.rows;
				let total = map.data.total;
				if (questionArray.length == 0) {
					$content.html("暂无数据");
				} else {
					// 展示首页问答
					showQuestions(questionArray);
					// 初始化分页组件
					$("#pagination").paging(total, {
						format: '< (q-) nncnn (-p) >',
						stepwidth: 1, 
						perpage: 10,
						onSelect: function (page) {
							// 展示当页问答
							$.post("courseDetails/listQuestions.do",{
								courseId: courseId, 
								page: page, 
								rows: 10
							},function(map){
								if (map.code == 600) {
									showQuestions(map.data.rows);
								}
							})
						},
						onFormat: function (type) {
					        switch (type) {
					          case 'block':
					            if (!this.active) {
					            	return `<span class="disabled">${this.value}</span>`;
					            } else if (this.value != this.page) {
					            	return `<a href="#${this.value}">${this.value}</a>`;
					            } else {
					            	return `<span class="current">${this.value}</span>`;
					            }
					          case 'next':
					            if (this.page == this.pages) {
					            	return `<span class="disabled">&gt;</span>`;
					            } else {
					            	return `<a href="#${this.value}" class="next">&gt;</a>`;
					            }
					          case 'prev':
					            if (this.page == 1) {
					            	return `<span class="disabled">&lt;</span>`;
					            } else {
					            	return `<a href="#${this.value}" class="prev">&lt;</a>`;
					            }
					          case 'fill':
					            if (this.active) {
					            	return `<span class="ellipsis">···</span>`;
					            }
					          case 'left': 
					        	  return "";
					          
					          case 'right': 
					        	  return "";
					       }
				      }
				    });
				}
			}
		})
	})
	
	
})