// 增加过渡动画
$.fn.addLoadingAnimation = function (message){
	if (message ==null || typeof message == "undefined") {
		message ="";
	}
	let html= `
	<div class="background_wrap">
		<div class="cover">
			<div class="loading">
				<h4>${message}</h4>
				<div class="loading-dot loading-dot-1"></div>
				<div class="loading-dot loading-dot-2"></div>
				<div class="loading-dot loading-dot-3"></div>
				<div class="loading-dot loading-dot-4"></div>
				<div class="loading-dot loading-dot-5"></div>
				<div class="loading-dot loading-dot-6"></div>
			</div>
		</div>
	</div>`;
	$(this).append(html);
}
$(document).ready(function(){
	$("#course_grid").datagrid({
	    url: "orgCourse/listCourses.action",
	    columns: [[
			{field:"course_id",hidden:true},
			{field:"course_name",title:"课程名称",width:300,align:"center",
				formatter: function(value,row,index){
					return `<div class="course_name">${value}</div>`;
				}
			},
			{field:"course_sort",title:"课程分类",width:150,align:"center"},
			{field:"course_teacher",title:"授课老师",width:150,align:"center"},
			{field:"status",title:"课程状态",width:150,align:"center",
				formatter: function(value,row,index){
					if (value == 0) {
						return `<div class="offline">下线</div>`
					}
					if (value == 1) {
						return `<div class="online">在线</div>`
					}
				}
			},
			{field:"action",title:"操作",width:150,align:"center",
				formatter: function(value,row,index){
					if (row.status == 0) {
						return `<div class="button_group"></div>`;
					}
					if (row.status == 1) {
						return `<div class="button_group course_arrangement_btn"></div>`;
					}
				}
			},
	    ]],
	    title: "我的课程",
	    pagination: true,
	    striped: true,
	    singleSelect: true,
	    pageSize: 10,
	    pageList: [10,15],
	    loadFilter: function(data){
	    	data.data.code=data.code;
	    	return data.data;
	    },
	    onLoadSuccess: function(data){
	    	if (data.code==600) {
	    		pageData=data.rows;
	    		// 若数据为0，展示提示
	    		if (data.total == 0) {
	    			$(this).datagrid("appendRow", {course_name: '<div style="text-align:center;color:red">没有相关记录！</div>' })
	    				.datagrid("mergeCells", { index: 0, field: "course_name", colspan: 5 });
                }
	    	}
	    	if (data.code>=400&&data.code<500) {
	    		// 发生错误，展示提示
	    		$(this).datagrid("appendRow", {course_name: '<div style="text-align:center;color:red">发生异常，请联系网站工作人员！</div>' })
					.datagrid("mergeCells", { index: 0, field: "course_name", colspan: 5 });
			}
	    }
	})
	// 修改表格分页组件的样式
	$("#course_grid").datagrid("getPager").pagination({
		layout:["list","sep","prev","manual","next","sep"],
		displayMsg: "共{total}条",
		beforePageText: "第",
		afterPageText: "页&emsp;共{pages}页",
	});
	// 点击[我的课程]表格中每一行的[管理报名学生]按钮
	$("body").on("click",".course_arrangement_btn",function(){
		let course=$("#course_grid").datagrid("getSelected");
		console.log(course);//TODO 删除
//		$("body").addLoadingAnimation("加载中");//TODO 打开注释
		let html=`
		<div class="course_arrangement" id="${course.course_id}">
			<div class="course_title">
				<div class="course_name">${course.course_name}</div>
				<div class="return_button btn btn-default">返回</div>
			</div>
			<div class="feature_group">
				<div class="course_class feature_panel">
					<div class="feature_title" data-toggle="collapse"
						data-target="#course_class">班级概况</div>
					<div id="course_class" class="collapse feature_content in">
						<div class="class_list">
						</div>
					</div>
				</div>
				<div class="course_student feature_panel">
					<div class="feature_title" data-toggle="collapse"
						data-target="#course_student">管理所有学生</div>
					<div id="course_student" class="collapse feature_content in">
						<div id="tool_bar">
							<div class="search_box">
								<input type="text" class="form-control" id="search_keyword"
									autocomplete="off" maxlength="11" placeholder="输入姓名或手机号查找">
								<div id="search_confirm" class="confirm btn-default">确定</div>
							</div>
							<div class="add btn-success">新增</div>
							<div class="batchdelete btn-danger">批量删除</div>
							<div class="batchmove btn-primary">批量转移</div>
						</div>
						<div class="student_list">
							<table id="student_grid"></table>
						</div>
					</div>
				</div>
			</div>
		</div>`;
		$(".bottom_side").html(html);
		$("#student_grid").datagrid({
			url: "courseArrangement/listStudents.action",
			queryParams: {
				courseId: course.course_id,
			},
		    columns: [[
				{field:"student_id",width:30,align:"center",resizable:false,
					formatter: function(value,row,index){
						if (value != null) {
							return '<input type="checkbox" name='+index+' value='+value+'>';
						}
					}
				},
				{field:"user_name",title:"用户昵称",width:150,align:"center"},
				{field:"user_tel",title:"手机号",width:120,align:"center"},
				{field:"student_name",title:"学生名称",width:150,align:"center"},
				{field:"package_info",title:"套餐信息",width:150,align:"center",
					formatter: function(value,row,index){
						if (value != null) {
							if (value.package_id != "0") {
								if (value.course_length == 0) {
									return `
									<div>
										<div>名称：${value.package_name}</div>
										<div>价格：${value.package_price}</div>
										<div>课时：无</div>
									</div>`
								}else{
									return `
									<div>
										<div>名称：${value.package_name}</div>
										<div>价格：${value.package_price}</div>
										<div>课时：${value.course_length}</div>
									</div>`
								}
							}else{
								return "无";
							}
						}
					}
				},
				{field:"student_class",title:"所在班级",width:150,align:"center",
					formatter: function(value,row,index){
						if (row.class_id == null) {
							return "暂无班级"
						}else{
							return `${row.class_name}`
						}
					}
				},
				{field:"action",title:"操作",width:100,align:"center",
					formatter: function(value,row,index){
						return `
						<div class="operation_btn">
							<div class="update">分班</div><div class="delete">删除</div>
						</div>`
					}
				},
		    ]],
		    pagination: true,
		    striped: true,
		    singleSelect: true,
		    pageSize: 10,
		    pageList: [10,15],
		    loadFilter: function(data){
		    	// 表格数据成功返回后，请求班级概况的内容
		    	$.post("courseArrangement/listClasses.action", { courseId: course.course_id },function(map){
					let classListHtml= "";
					if (map.code >=400 && map.code<500) {
						classListHtml = `<div style="color:red">发生异常，请联系网站工作人员！</div>`;
					}
					if (map.code == 600) {
						let classList = map.data;
						console.log(classList)//TODO 删除
						for (let i = 0; i < classList.length; i++) {
							classListHtml += `
							<div class="class_item" id="${classList[i].class_id}">
								<div class="class_delete">&#xea0f</div>
								<div class="class_title btn btn-info">${classList[i].class_name}</div>
								<div class="class_content">
									<div class="class_name">
										<label>班级名称：</label>
										<div>${classList[i].class_name}</div>
									</div>
									<div class="course_length">
										<label>每次上课课时数：</label>
										<div>${classList[i].classhour_eachtime}</div>
									</div>
									<div class="class_student">
										<label>
											班级学生：
											<span class="badge">${classList[i].studentList.length}</span>
											<span class="btn btn-danger btn-xs disabled">移除学生</span>
										</label>
										<div>
											<div class="label label-info">
												<b class="student_name">+</b> <b class="user_tel">新增学生</b>
											</div>`;
										for (let j = 0; j < classList[i].studentList.length; j++) {
											classListHtml += `
											<div class="label label-info" id="${classList[i].studentList[j].student_id}">
												<b class="student_name">${classList[i].studentList[j].student_name}</b>
												<b class="user_tel">${classList[i].studentList[j].user_tel}</b>
												<div class="student_delete">&#xea10</div>
											</div>`;
										}
							classListHtml += `
										</div>
									</div>
								</div>
							</div>`;
						}
						classListHtml += `
						<div class="class_item ">
							<div class="class_title btn btn-success">+</div>
						</div>`;
					}
					$(".class_list").html(classListHtml);
				});
		    	data.data.code=data.code;
		    	return data.data;
		    },
		    onLoadSuccess: function(data){
		    	console.log(data);//TODO 删除
		    	// 每次刷新表格，重置[批量删除][批量转移]按钮
		    	$(".batchdelete").removeClass("usable");
		    	$(".batchmove").removeClass("usable");
		    	if (data.code==600) {
		    		pageData=data.rows;
		    		// 若数据为0，展示提示
		    		if (data.total == 0) {
		    			$(this).datagrid("appendRow", {user_name: '<div style="text-align:center;color:red">没有相关记录！</div>' })
		    				.datagrid("mergeCells", { index: 0, field: "user_name", colspan: 6 });
	                }
		    	}
		    	if (data.code>=400&&data.code<500) {
		    		// 发生错误，展示提示
		    		$(this).datagrid("appendRow", {user_name: '<div style="text-align:center;color:red">发生异常，请联系网站工作人员！</div>' })
						.datagrid("mergeCells", { index: 0, field: "user_name", colspan: 6 });
				}
		    }
		})
		// 修改表格分页组件的样式
		$("#student_grid").datagrid("getPager").pagination({
			layout:["list","sep","prev","manual","next","sep"],
			displayMsg: "共{total}条",
			beforePageText: "第",
			afterPageText: "页&emsp;共{pages}页",
		});
		$(".top_side").slideUp("slow");
		$(".bottom_side").slideDown("slow");
		// 在容器元素可见时，easyui的datagrid初始化才能自动适应宽高
		$("#student_grid").datagrid("resize");
	})
	// 点击[学生管理面板]右上角的[返回]按钮
	$("body").on("click",".return_button",function(){
		$(".bottom_side").html("");
		$(".bottom_side").slideUp("slow");
		$(".top_side").slideDown("slow");
	})
})