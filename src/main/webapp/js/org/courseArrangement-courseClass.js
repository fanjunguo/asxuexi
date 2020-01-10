$(document).ready(function(){
	// [班级概况]面板
	var hideClassContent;//隐藏[班级详情]面板的定时函数
	// 点击每个班级按钮
	$("body").on("click",".class_title.btn-info",function(){
		$classContent = $(this).next(".class_content");
		$classContent.slideToggle("fast");
		$(this).parent(".class_item").siblings(".class_item").each(function(){
			$(this).children(".class_content").slideUp("fast");
		});
//		hideClassContent=setTimeout(function(){
//			$classContent.hide("fast");
//		},3000);
	})
	// 监听[班级详情]面板的鼠标移入，移出事件
//	$("body").on({
//		mouseenter: function(){
//			clearTimeout(hideClassContent);
//		},
//		mouseleave: function(event){
//			hideClassContent=setTimeout(function(){
//				$(event.currentTarget).hide("fast");
//			},3000);
//		}
//	},".class_content")
	// 点击新增课程的[+]按钮
	$("body").on("click",".class_title.btn-success",function(){
		// 更改面板标题
		$("#add_modal .modal-title").text("新增班级");
		// 为[确定]按钮加上用以区分的类
		$("#add_modal .btn-success").attr("class","btn btn-success class_add_button");
		// [新增课程]面板的内容
		let html=`
		<form class="form-horizontal class_add_form" role="form">
			<div class="form-group">
				<label class="col-sm-3 control-label">班级名称</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" id="class_name_input"
						autocomplete="off" maxlength="10" name="class_name_input"
						placeholder="请填写班级名称，不超过十个字符">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label">每次上课课时数</label>
				<div class="col-sm-9">
					<input id="classhour_eachtime" name="classhour_eachtime"
						style="width: 411px; height: 34px">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label">班级学生</label>
				<div class="col-sm-9">
					<input id="class_student" name="class_student"
						style="width: 411px; height: 34px">
				</div>
			</div>
		</form>`;
		$("#add_modal .content_wrap").html(html);
		// 初始化[每次上课课时数]的数字选择器
		$("#classhour_eachtime").numberspinner({
			min: 0, increment: 1, value: 0
		});
		// 初始化[班级学生]的下拉菜单
		$("#class_student").combogrid({
			editable: false,
		    delay: 500,
		    multiple: true,
		    mode: "remote",    
		    url: "courseArrangement/listUndividedStudents.action",
		    queryParams:{
		    	courseId: $(".course_arrangement").attr("id")
		    },
		    idField: "student_id",    
		    textField: "student_name",
		    prompt: "分配学生（非必须），以下学生均为未分班学生",
		    columns: [[    
		        {field:"student_id",hidden:true},    
		        {field:"student_name",title:"学生名称",width:200},
		        {field:"user_tel",title:"手机号",width:200}
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
		$("#add_modal").modal("show");
		// 为表单添加验证规则
		$(".class_add_form").validate({
			rules:{
				class_name_input: "required",
			},
			messages:{
				class_name_input: "请输入课程名称"
			}
		});
		// 点击[新增班级]面板中的[确定]按钮
		$("#add_modal .class_add_button").off("click").on("click",function(){
			if ($(".class_add_form").valid()) {
				let className = $("#class_name_input").val();
				let classhourEachtime = $("#classhour_eachtime").numberspinner("getValue");
				let studentIdArray=$("#class_student").combogrid("getValues");
				$.post("courseArrangement/addClass.action",{
					courseId: $(".course_arrangement").attr("id"), 
					className: className,
					classhourEachtime: classhourEachtime,
					studentIds: JSON.stringify(studentIdArray)
				},function(map){
					$("#add_modal").modal("hide");
					$("#student_grid").datagrid("reload");
				});
			}
		})
	})
	// 点击课程按钮右上角的[x]按钮
	$("body").on("click",".class_delete",function(){
		let classId = $(this).parent(".class_item").attr("id");
		let studentIdArray=[];
		$(this).nextAll(".class_content").find(".class_student>div>.label").each(function(i){
			if (i !=0 ) {
				studentIdArray.push(this.id)
			}
		})
		// 为[确定]按钮加上用以区分的类
		$("#warn_modal .btn-success").attr("class","btn btn-success class_del_button");
		let html=`
		<div>
			<span>&#xea08</span>
			<p>确定删除选中的课程？</p>
		</div>
		<p>注意：删除班级后，该班级原有学生都需要重新分配班级。</p>`;
		$("#warn_modal .warn_content").html(html);
		$("#warn_modal").modal("show");
		// 点击[确定]按钮
		$("#warn_modal .class_del_button").off("click").on("click",function(){
			$.post("courseArrangement/deleteClass.action",{
				courseId: $(".course_arrangement").attr("id"),
				classId: classId,
				studentIds: JSON.stringify(studentIdArray)
			},function(map){
				$("#warn_modal").modal("hide");
				$("#student_grid").datagrid("reload");
			});
		})
	})
	// 点击[班级学生]区域中的[新增学生]按钮
	$("body").on("click",".class_student>div>.label:first-child",function(){
		let classId = $(this).parents(".class_item").attr("id");
		let className = $(this).parents(".class_student").prevAll(".class_name")
							.children("div").text();
		let classhourEachtime =  $(this).parents(".class_student").prevAll(".course_length")
									.children("div").text();
		// 更改面板标题
		$("#add_modal .modal-title").text("选择学生");
		// 为[确定]按钮加上用以区分的类
		$("#add_modal .btn-success").attr("class","btn btn-success disabled student_add_button");
		// [选择学生]面板的内容
		let html=`
		<form class="student_add_form" role="form">
			<div class="form-group">
				<label class="">待分班学生</label>
				<div class="">
					<input id="class_student" name="class_student"
						style="width: 411px; height: 34px">
				</div>
			</div>
		</form>`;
		$("#add_modal .content_wrap").html(html);
		// 初始化[待分班学生]的下拉菜单
		$("#class_student").combogrid({
			editable: false,
		    delay: 500,
		    multiple: true,
		    mode: "remote",    
		    url: "courseArrangement/listUndividedStudents.action",
		    queryParams:{
		    	courseId: $(".course_arrangement").attr("id")
		    },
		    idField: "student_id",    
		    textField: "student_name",
		    prompt: "请选择学生",
		    columns: [[    
		        {field:"student_id",hidden:true},    
		        {field:"student_name",title:"学生名称",width:200},
		        {field:"user_tel",title:"手机号",width:200}
		    ]],
		    striped: true,
		    loadFilter: function(map){
		    	if (map.code==600) {
		    		return map.data;
				}else{
					return map;
				}
		    },
		    onClickRow: function(){
		    	let length=$("#class_student").combogrid("getValues").length;
		    	if (length == 0) {
		    		$("#add_modal .student_add_button").addClass("disabled");
				}else{
					$("#add_modal .student_add_button").removeClass("disabled");
				}
		    }
		});
		$("#add_modal").modal("show");
		// 点击[选择学生]面板中的[确定]按钮
		$("#add_modal .student_add_button").off("click").on("click",function(){
			if (!$(this).hasClass("disabled")) {
				let studentIdArray=$("#class_student").combogrid("getValues");
				$.post("courseArrangement/updateCourseStudentClass.action",{ 
					classId: classId,
					className: className,
					classhourEachtime: classhourEachtime,
					studentIds: JSON.stringify(studentIdArray),
					status: "1"
				},function(map){
					$("#add_modal").modal("hide");
					$("#student_grid").datagrid("reload");
				});
			}
		})
	})
	// 点击[班级学生]区域中每个学生名称旁的[✔]
	$("body").on("click",".student_delete",function(){
		$(this).toggleClass("selected");
		let deletebtnArray = $(this).parents(".class_student").find(".student_delete.selected");
		let length=deletebtnArray.length;
		if(length == 0){
			$(this).parents(".class_student").find(".btn-danger").addClass("disabled");
		}else{
			$(this).parents(".class_student").find(".btn-danger").removeClass("disabled");
		}
		// 点击[班级学生]区域右上角的[移除学生]按钮
		$(".class_student label .btn").off("click").on("click",function(){
			if (!$(this).hasClass("disabled")) {
				let studentIdArray=[];
				deletebtnArray.each(function(i){
					studentIdArray.push($(this).parent(".label").attr("id"));
				})
				$.post("courseArrangement/updateCourseStudentClass.action",{
					studentIds: JSON.stringify(studentIdArray),
					status: "0"
				},function(map){
					$("#student_grid").datagrid("reload");
				});
			}
		})
	})
	
	
})