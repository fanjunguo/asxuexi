$(document).ready(function(){
	$.fn.addQuestions = function (questionArray){
		let $wrap = $(this);
		let html = ``;
		for (let i = 0; i < questionArray.length; i++) {
			html +=`
			<div class="question_group" id="${questionArray[i].id}">
				<div class="top">
					<div class="user_name">${questionArray[i].name}</div>
					<div class="ask_date">${questionArray[i].gmt_create}</div>
					<div class="operation">`;
			if (typeof questionArray[i].stick == "undefined") {
				html +=`<span class="stick">置顶</span>`;
			} else {
				html +=`<span class="unstick">取消置顶</span>`;
			}	
			html +=`</div>
				</div>
				<div class="center">
					<div class="ask_text">
						<div>${questionArray[i].question}</div>
					</div>`
			if (typeof questionArray[i].answer == "undefined") {
				html +=`
					<div class="answer_text not_answer">
						<div>暂无回答</div>
						<button class="btn btn-default">填写回答</button>
					</div>`
			} else {
				html +=`
					<div class="answer_text">
						<div>${questionArray[i].answer}</div>
						<button class="btn btn-default">修改回答</button>
					</div>`
			}
			html += `
				</div>
			</div>`;
		}
		$wrap.html(html);
	}
	
	$.fn.showQusetions = function (url,requestParam){
		let $wrap = $(this);
		$.post(url,requestParam,function(map){
			if (map.code = 600) {
				let questionArray = map.data.rows;
				let total = map.data.total;
				if (questionArray.length == 0) {
					$wrap.html("暂无数据");
					$wrap.next(".pagination").empty();
				} else {
					// 初始化分页组件
					$wrap.next(".pagination").paging( total, {
						format: '< (q-) nncnn (-p) >',
						stepwidth: 1, 
						perpage: requestParam.rows,
						onSelect: function (page) {
							requestParam.page = page;
							// 展示当页问答
							$.post(url,requestParam,function(map){
								if (map.code == 600) {
									$wrap.addQuestions(map.data.rows);
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
				    })
				}
			} else {
				// 发生错误的提示
				$wrap.html("发生错误，请联系网站工作人员。");
			}
		})
	}
	// 请求机构的所有课程，填充[课程问题]的下拉菜单
	$.post("orgQuestion/listOrgCourses.action",function(map){
		if (map.code == 600) {
			let html = `
			<a href="#" id="course_dropdown" class="dropdown-toggle" data-toggle="dropdown">
				课程问题<b class="caret"></b>
			</a>
			<ul class="dropdown-menu">`;
			for (let i = 0; i < map.data.length; i++) {
				html += `
				<li><a href="#course" data-toggle="tab"
						id="${map.data[i].course_id}">${map.data[i].course_name}</a></li>`;
			}
			html += `
			</ul>`;
			$(".question_container li.dropdown").html(html);
		}
	})
	// 展示该机构的全部机构问题
	$("#org .question_wrap").showQusetions("orgQuestion/listOrgQuestions.action",{page:1,rows:10,status:0})
	// 点击[课程问题]下拉菜单中的按钮
	$("body").on("click","#course_dropdown+.dropdown-menu li",function(){
		let courseId= $(this).children("a").attr("id");
		$("#course .question_wrap").showQusetions("orgQuestion/listCourseQuestions.action",{
			page:1,
			rows:10,
			status:0,
			courseId:courseId
		})
	})
	// 点击[机构问题]面板中的下拉菜单
	$("body").on("click","#org .dropdown-menu li",function(){
		if (!$(this).hasClass("active")) {
			$(this).siblings(".active").removeClass("active");
			$(this).addClass("active");
			let index = $(this).index();
			let text = $(this).children("a").text();
			$(this).parents(".question_status").find("button:first-child").text(text);
			$("#org .question_wrap").showQusetions("orgQuestion/listOrgQuestions.action",{page:1,rows:10,status:index})
		}
	})
	// 点击[课程问题]面板中的下拉菜单
	$("body").on("click","#course .dropdown-menu li",function(){
		if (!$(this).hasClass("active")) {
			$(this).siblings(".active").removeClass("active");
			$(this).addClass("active");
			let index = $(this).index();
			let text = $(this).children("a").text();
			let courseId = $("#course_dropdown+.dropdown-menu li.active a").attr("id");
			$(this).parents(".question_status").find("button:first-child").text(text);
			$("#course .question_wrap").showQusetions("orgQuestion/listCourseQuestions.action",{
				page: 1,
				rows: 10,
				status: index,
				courseId: courseId
			})
		}
	})
	// 敏感词验证的方法
	function wordFilter(textArray, passed , notPassed){
		let contextArray = [];
		for (let i = 0; i < textArray.length; i++) {
			contextArray.push({text:textArray[i]});
		}
		$.post("filterWord/isFilterWord.do", {context:JSON.stringify(contextArray)},function(data){
			let contextIsPassed = true;// 是否通过敏感词验证的标识
			let invalidWords="";// 敏感词
			let set = new Set();
			for (let i = 0; i < data.length; i++) {
				if (data[i].text != "") {
					set.add(data[i].text);
				}
			}
			if (set.size != 0) {
				contextIsPassed = false;
			}
			for (let text of set.values()) {
				invalidWords += text+"&emsp;";
			}
			if (contextIsPassed) {
				passed();
			}else{
				notPassed(invalidWords);
			}
		});
	}
	// 点击[填写答案]或[修改答案]按钮
	$("body").on("click",".answer_text .btn",function(){
		$button = $(this);
		// 清空文本域
		$("#answer_content").val("");
		// 获得问题的ID
		let questionId = $button.parents(".question_group").attr("id");
		// 获得问题类型，机构问题(org)或课程问题(course)
		let type = $(".tab-pane.active").attr("id");
		$("#answer_modal").modal("show");
		// 点击[确定]按钮
		$(".answer_buttons .btn-success").on("click",function(){
			let text = $("#answer_content").val().trim();
			if (text.length == 0) {
				// 输入的内容为空，展示提示
				let errorHtml = `
				<div class="alert alert-danger" role="alert">
					<button type="button" class="close" data-dismiss="alert" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<div>回答内容不能为空</div>
				</div>`;
				if ($("#answer_modal .answer_container .alert").length == 0) {
					$("#answer_modal .answer_container").prepend(errorHtml);
				}
			} else {
				// 不为空，移除原有提示，进行敏感词验证
				$("#answer_modal .answer_container .alert").remove();
				wordFilter([text], function(){
					// 通过验证，保存回答
					$.post("orgQuestion/updateAnswer.action",{
						questionId: questionId,
						type: type,
						answer: text
					}, function(map){
						if (map.code == 600) {
							// 保存成功
							$button.text("修改回答");
							$button.prev("div").text(text);
							$button.parent(".answer_text").removeClass("not_answer");
							$("#answer_modal").modal("hide");
						}else {
							// 保存失败，提示
							alert("提交失败，请重试！")
						}
					})
				} , function(invalidWords){
					// 未通过验证，展示敏感词提示
					let errorHtml = `
					<div class="alert alert-danger" role="alert">
						<button type="button" class="close" data-dismiss="alert" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<div>您提交的信息中包含如下敏感词汇，请修改后提交：<strong class="invalid_words">${invalidWords}</strong></div>
					</div>`;
					$("#answer_modal .answer_container").prepend(errorHtml);
				});
			}
		})
	})
	// 点击[置顶]按钮
	$("body").on("click",".operation .stick",function(){
		function stickQuestion(questionId, type, $button){
			$.post("orgQuestion/updateStick.action",{
				questionId: questionId, 
				type: type, 
				stick: true
			},function(map){
				if (map.code == 600) {
					$button.attr("class","unstick");
					$button.text("取消置顶");
				} else {
					alert("操作失败，请重试！")
				}
			})
		}
		$button = $(this);
		// 获得问题的ID
		let questionId = $button.parents(".question_group").attr("id");
		// 获得问题类型，机构问题(org)或课程问题(course)
		let type = $(".tab-pane.active").attr("id");
		let length = $button.parents(".question_group").find(".not_answer").length;
		if (length != 0) {
			$("#stick_modal").modal("show");
			$(".stick_buttons .btn-success").off("click").on("click",function(){
				stickQuestion(questionId, type, $button);
				$("#stick_modal").modal("hide");
			})
		} else{
			stickQuestion(questionId, type, $button);
		}
	})
	// 点击[取消置顶]按钮
	$("body").on("click",".operation .unstick",function(){
		$button = $(this);
		// 获得问题的ID
		let questionId = $button.parents(".question_group").attr("id");
		// 获得问题类型，机构问题(org)或课程问题(course)
		let type = $(".tab-pane.active").attr("id");
		$.post("orgQuestion/updateStick.action",{
			questionId: questionId, 
			type: type, 
			stick: false
		},function(map){
			if (map.code == 600) {
				$button.attr("class","stick");
				$button.text("置顶");
			} else {
				alert("操作失败，请重试！")
			}
		})
	})
})