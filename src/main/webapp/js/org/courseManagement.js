$(document).ready(function(){
	var pageData;
	// 初始化校区选择框
	$.post("orgCourse/listOrgs.action", function(json){
		$("#org_select").combobox({
			prompt:"选择校区",
			editable:false, 
			valueField: "orgId",
			textField: "orgName",
			data: json.data,
		}).combobox("setValue",json.data[0].orgId)
	})
	
	$("#course_grid").datagrid({
	    url: "orgCourse/listCourses.action",
	    columns: [[
			{field:"course_id",width:30,align:"center",resizable:false,
				formatter: function(value,row,index){
					if (value != null) {
						return '<input type="checkbox" name='+index+' value='+value+'>';
					}
				}
			},
			{field:"course_name",title:"课程名称",width:250,align:"center",
				formatter: function(value,row,index){
					return `<div class="course_name"><a href="courseDetails.do?courseId=${row.course_id}" target="_blank">${value}</a></div>`;
				}
			},
			{field:"org_name",title:"机构名称",width:150,align:"center"},
			{field:"status",title:"课程状态",width:100,align:"center",
				formatter: function(value,row,index){
					if (value == 0) {
						return '<div class="offline">下线</div>'
					}
					if (value == 1) {
						return '<div class="online">在线</div>'
					}
				}
			},
			{field:"action",title:"操作",width:150,align:"center",
				formatter: function(value,row,index){
					return `<div class="button_group">
								<a href="courseRelease.do?courseId=${row.course_id}" target="_blank"><div class="update">修改</div></a>
								<div class="delete">删除</div>
							</div>`
				}
			},
	    ]],
	    fitColumns: true,
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
	    	// 每次刷新表格，重置[批量删除]按钮
	    	$(".batchdelete").removeClass("usable");
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
	// 点击校区筛选的[确定]按钮
	$("#org_confirm").on("click",function(){
		// 获取选择的分类选项，根据选项刷新表格
		let orgId = $("#org_select").combobox("getValue");
		$("#course_grid").datagrid("load",{
			orgId: orgId
		})
	})
	// 点击表格第一列的选择框
	$("body").on("click","input:checkbox",function(){
		// 根据选中的数量，开启或禁用[批量删除]按钮
		if ($("input:checkbox:checked").length==0) {
			$(".batchdelete").removeClass("usable");
		}else{
			$(".batchdelete").addClass("usable");
		}
	})
	// 点击可用的[批量删除]按钮
	$("body").on("click",".batchdelete.usable",function(){
		// 将要删除的课程的课程id保存到数组中
		let idArray = [];
		$("input:checkbox:checked").each(function(){
			idArray.push($(this).val());
		});
		// 打开提示面板
		$("#warn_modal").modal("show");
		$(".warn_buttons .btn-success").off("click").on("click",function(){
			// 点击[确定]按钮后
			$.post("orgCourse/deleteCourses.action", {courseIdList:JSON.stringify(idArray)}, function(map){
				if (map.code == 600) {
					// 更改成功，刷新表格，关闭提示面板
					$("#course_grid").datagrid("load");
					$("#warn_modal").modal("hide");
				}
			});
		})
	})
	// 点击每一行的[删除]按钮
	$("body").on("click",".delete",function(){
		// 获取选中的行和行索引
		let selectedRow=$("#course_grid").datagrid("getSelected");
		let selectedRowIndex=$("#course_grid").datagrid("getRowIndex",selectedRow);
		// 将要删除的课程的课程id保存到数组中
		let idArray=[selectedRow.course_id];
		// 打开提示面板
		$("#warn_modal").modal("show");
		$(".warn_buttons .btn-success").off("click").on("click",function(){
			// 点击[确定]按钮后
			$.post("orgCourse/deleteCourses.action", {courseIdList:JSON.stringify(idArray)}, function(map){
				if (map.code == 600) {
					// 更改成功，删除对应行，关闭提示面板
					$("#warn_modal").modal("hide");
					$("#course_grid").datagrid("deleteRow",selectedRowIndex);
				}
			});
		})
	})
	
})