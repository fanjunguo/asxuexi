$(document).ready(function(){
	// [管理所有学生]面板
	// 点击[确定]按钮
	$("body").on("click","#search_confirm",function(){
		let keyword = $("#search_keyword").val().trim();
		$("#student_grid").datagrid("load",{
			courseId: $(".course_arrangement").attr("id"),
			keyword: keyword
		})
	})
	// 点击表格第一列的选择框
	$("body").on("click","input:checkbox",function(){
		// 根据选中的数量，开启或禁用[批量删除][批量转移]按钮
		if ($("input:checkbox:checked").length==0) {
			$(".batchdelete").removeClass("usable");
			$(".batchmove").removeClass("usable");
		}else{
			$(".batchdelete").addClass("usable");
			$(".batchmove").addClass("usable");
		}
	})
	// 点击可用的[批量删除]按钮，每一行的[删除]按钮
	$("body").on("click",".batchdelete.usable,.delete",function(){
		// 将要删除的课程的课程id保存到数组中
		let studentArray;
		let courseId = $(".course_arrangement").attr("id");
		if ($(this).hasClass("batchdelete")) {
			studentArray = [];
			$("input:checkbox:checked").each(function(){
				studentArray.push({
					course_id: courseId,
					student_id: $(this).val()
				});
			});
		}
		if ($(this).hasClass("delete")) {
			studentArray = [{
				course_id: courseId,
				student_id: $("#student_grid").datagrid("getSelected").student_id
			}]
		}
		// 为[确定]按钮加上用以区分的类
		$("#warn_modal .btn-success").attr("class","btn btn-success student_del_button");
		let html=`
		<div>
			<span>&#xea08</span>
			<p>确定删除选中的学生？</p>
		</div>`;
		$("#warn_modal .warn_content").html(html);
		$("#warn_modal").modal("show");
		// 点击[确定]按钮
		$("#warn_modal .student_del_button").off("click").on("click",function(){
			$.post("courseArrangement/deleteStudent.action",{
				students: JSON.stringify(studentArray)
			},function(map){
				$("#warn_modal").modal("hide");
				$("#student_grid").datagrid("reload");
			});
		})
	})
	// 点击可用的[批量转移]按钮，每一行的[分班]按钮
	$("body").on("click",".batchmove.usable,.update",function(){
		// 将要转移的学生的学生id保存到数组中
		let studentIdArray;
		if ($(this).hasClass("batchmove")) {
			studentIdArray = [];
			$("input:checkbox:checked").each(function(){
				studentIdArray.push($(this).val());
			});
		}
		if ($(this).hasClass("update")) {
			studentIdArray = [$("#student_grid").datagrid("getSelected").student_id]
		}
		// 为[确定]按钮加上用以区分的类
		$("#edit_modal .btn-success").attr("class","btn btn-success student_move_button disabled");
		let html=`
		<form class="student_move_form" role="form">
			<div class="form-group">
				<label class="">班级</label>
				<div class="">
					<input id="class" name="class" style="width: 411px; height: 34px">
				</div>
			</div>
		</form>`;
		$("#edit_modal .edit_content").html(html);
		// 初始化[班级]的下拉菜单
		$("#class").combogrid({
			editable: false,
		    delay: 500,
		    mode: "remote",
		    url: "courseArrangement/listClasses.action",
		    queryParams:{
		    	courseId: $(".course_arrangement").attr("id")
		    },
		    idField: "class_id",    
		    textField: "class_name",
		    prompt: "请选择班级",
		    columns: [[    
		        {field:"class_id",hidden:true},    
		        {field:"class_name",title:"班级名称",width:200},
		        {field:"classhour_eachtime",title:"每次上课课时数",width:100},
		        {field:"class_students",title:"班级人数",width:100,
		        	formatter: function(value,row,index){
		        		return row.studentList.length;
		        	}
		        },
		    ]],
		    striped: true,
		    loadFilter: function(map){
		    	if (map.code==600) {
		    		return map.data;
				}else{
					return map;
				}
		    },
		    onSelect: function(){
		    	$("#edit_modal .student_move_button").removeClass("disabled")
		    }
		});
		$("#edit_modal").modal("show");
		// 点击[确定]按钮
		$("#edit_modal .student_move_button").off("click").on("click",function(){
			if (!$(this).hasClass("disabled")) {
				let selectedClass = $("#class").combogrid("grid").datagrid("getSelected");
				$.post("courseArrangement/updateCourseStudentClass.action",{
					classId: selectedClass.class_id, 
					className: selectedClass.class_name,
					classhourEachtime: selectedClass.classhour_eachtime, 
					studentIds: JSON.stringify(studentIdArray), 
					status: "1"
				},function(map){
					$("#edit_modal").modal("hide");
					$("#student_grid").datagrid("reload");
				});
			}
		})
	})
	// 点击[新增]按钮
	$("body").on("click",".add",function(){
		// 更改面板标题
		$("#add_modal .modal-title").text("新增学生");
		// 为[确定]按钮加上用以区分的类
		$("#add_modal .btn-success").attr("class","btn btn-success student_add_button");
		// [新增学生]面板的内容
		let html=`
		<form class="form-horizontal student_add_form" role="form">
			<div class="form-group">
				<label class="col-sm-3">学生名称</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" id="student_name_input"
						autocomplete="off" maxlength="10" name="student_name_input"
						placeholder="请填写学生名称">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3">分配班级</label>
				<div class="col-sm-9">
					<input id="add_modal_class" name="add_modal_class"
						style="width: 403px; height: 34px">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3">关联用户</label>
				<div class="col-sm-9">
					<input id="user_tel" name="user_tel"
						style="width: 403px; height: 34px">
				</div>
			</div>
		</form>`;
		$("#add_modal .content_wrap").html(html);
		// 初始化[分配班级]的下拉菜单
		$("#add_modal_class").combogrid({
			editable: false,
		    delay: 500,
		    mode: "remote",
		    url: "courseArrangement/listClasses.action",
		    queryParams:{
		    	courseId: $(".course_arrangement").attr("id")
		    },
		    idField: "class_id",    
		    textField: "class_name",
		    prompt: "请选择班级",
		    columns: [[    
		        {field:"class_id",hidden:true},    
		        {field:"class_name",title:"班级名称",width:200},
		        {field:"classhour_eachtime",title:"每次上课课时数",width:100},
		        {field:"class_students",title:"班级人数",width:100,
		        	formatter: function(value,row,index){
		        		return row.studentList.length;
		        	}
		        },
		    ]],
		    striped: true,
		    loadFilter: function(map){
		    	if (map.code==600) {
		    		return map.data;
				}else{
					return map;
				}
		    }
		});
		// 初始化[手机号]的下拉菜单
		$("#user_tel").combogrid({
		    delay: 1000,
		    mode: "remote",
		    url: "courseArrangement/listUsers.action",
		    idField: "user_tel",    
		    textField: "user_name",
		    prompt: "请输入注册手机号搜索用户",
		    columns: [[  
		        {field:"user_name",title:"用户昵称",width:200},
		        {field:"user_tel",title:"手机号",width:200},
		    ]],
		    striped: true,
		    loadFilter: function(map){
		    	if (map.code==600) {
		    		return map.data;
				}else{
					return map;
				}
		    }
		});
		// 为表单添加验证规则
		$(".student_add_form").validate({
			rules:{
				student_name_input: "required",
			},
			messages:{
				student_name_input: "必须填写学生名称",
			}
		});
		// 点击[确定]按钮
		$("#add_modal .student_add_button").off("click").on("click",function(){
			let selectedClass = $("#add_modal_class").combogrid("grid").datagrid("getSelected");
			let selectedUser = $("#user_tel").combogrid("grid").datagrid("getSelected");
			let studentNameIsFilled = $(".student_add_form").valid();
			let classIsFilled = true;
			let userIsFilled = true;
			if ($("#add_modal_class").combogrid("getValue")== "") {
				classIsFilled = false;
				$("#add_modal_class").after(`<label class="error">必须选择班级</label>`);
			}else{
				$("#add_modal_class+label.error").remove();
			}
			if (selectedUser == null) {
				userIsFilled = false;
				$("#user_tel").after(`<label class="error">必须关联本站用户</label>`);
			}else{
				$("#user_tel+label.error").remove();
			}
			if (studentNameIsFilled && classIsFilled && userIsFilled) {
				$.post("courseArrangement/addStudent.action",{
					courseId: $(".course_arrangement").attr("id"),
					studentName: $("#student_name_input").val(),
					classId: selectedClass.class_id, 
					className: selectedClass.class_name,
					classhourEachtime: selectedClass.classhour_eachtime,
					userTel : selectedUser.user_tel
				},function(map){
					$("#add_modal").modal("hide");
					$("#student_grid").datagrid("reload");
				});
			}
		})
		$("#add_modal").modal("show");
	})
	
	
})