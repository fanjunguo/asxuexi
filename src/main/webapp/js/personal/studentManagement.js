$(function(){
	var $body=$('body');
	var studentName='';
	var studentTel='';
	var studentId='';


	//加载上课人
	loadStudentList();

	//新增
	$('#addStudent').click(function() {
		initEditWindow('add_submit');
	});

	//填写上课人信息
	$body.on('input propertychange', '.student_name,.phone_num', function() {
		studentName=$('.student_name').val().trim();
		studentTel=$('.phone_num').val().trim();

		if (studentName.length>0&&studentTel>0) {
			let className=$(this).attr('class');
			if (className=='phone_num') {
				$('.wrong_message').html('');
			}
			changeButtonStatus(1);
		}else{
			changeButtonStatus(2);
			
		}
	});

	//点击确定,提交上课人信息
	$body.on('click', '#add_submit,#edit_submit', function() {
		let patt=/^1[3-9]\d{9}$/;
		if (patt.test(studentTel)) {
			let postUrl;
			if ($(this).attr('id').includes('add')) { //新增
				postUrl='studentManagement/insertNewStudent.action';
			} else {								//修改
				postUrl='studentManagement/updateStudent.action';
			}
			$.post(postUrl, {name: studentName,tel:studentTel,id:studentId}, function(json) {
				if (json.code==600) {
					loadStudentList();
					$('#addContainer').empty()//清除窗口
				}else{
					alert('网络错误,请稍后重试');
				}
			});

		}else{
			$('.wrong_message').html('*手机号格式错误');
		}
	});

	//删除
	$body.on('click', '.delete', function() {
		$('#comfirm_container').modalWindow({
			width:'410px',
			height:'240px',
			panelTitle:'删除上课人',
			bodyId:'delele_content',
			btnId:'delele_submit',
		});
		$('#delele_content').html('确定要删除该上课人吗?');
		$('#delele_submit').addClass('usable');

		let id=$(this).parent().attr('id');
		$body.off('click', '#delele_submit').on('click', '#delele_submit', function() {
			$.post('studentManagement/deleteStudent.action', {studentId: id}, function(data) {
				if (data.code==600) {
					loadStudentList();
					$('#comfirm_container').empty();
				} else {
					alert("网络错误,请稍后重试");
				}
			});
		});

	});

	//修改
	$body.on('click', '.edit', function() {
		let $this=$(this);
		studentId=$this.parent().attr('id');
		studentName=$this.parents('.item_content').children('.span_name').html().substring(3);
		studentTel=$this.parents('.item_content').children('.span_tel').html().substring(3);
		initEditWindow('edit_submit');
		$('.student_name').val(studentName);
		$('.phone_num').val(studentTel);
	});


/* 方法:初始化编辑窗口(新增/编辑) */
function initEditWindow(buttonId){
	$('#addContainer').modalWindow({
		width:'410px',
		height:'270px',
		panelTitle:'新增上课人',
		bodyId:'add_content',
		btnId:buttonId
	});
	$('#add_content').html(`
		<div class="new_student_form">
   			<div class="one_line">
   				上课人姓名: <input type="text" class="student_name">
   				<span class="tips">*请填写真实姓名</span>
   			</div>
   			<div class="one_line">
   				<span class="letter_space">联系电话</span>: <input type="text" class="phone_num">
   			</div>
   			<div class="wrong_message"></div>
   		</div>`);
}

	/* 方法:改变提交按钮的状态(可用/不可用)
		参数:operation:1-变为可用 2-变为不可用
	*/
	
	function changeButtonStatus(operation){
		let $sure_button=$('.sure_button');
		if (operation==1) {
			if ($('.usable').length==0) {
				$sure_button.addClass('usable');
			}
		} else {
			if ($('.usable').length>0) {
				$sure_button.removeClass('usable');
			}
		}
	}

	/*
		方法:加载所有的上课人
	*/
	function loadStudentList(){
		$.get('studentManagement/getAllStudents.action', function(json) {
			if (json.code==600) {
				var list=json.data;
				let h='';
				let len=list.length;
				if (len>0) {
					for (var i = 0; i < len; i++) {
						h+=`<div class="student_item">
							<div class="item_header"></div>
							<div class="item_content">
								<span class="span_name">姓名:${list[i].name}</span>
								<span class="span_tel">电话:${list[i].tel}</span>
								<div class="btn_container" id="${list[i].id}">
									<a class="edit student_edit_btn">编辑</a>
									<a class="delete student_edit_btn">删除</a>
								</div>
							</div>
						</div>`;
					}
				} else {
					h=`<div class="no_data">暂无上课人,请先添加</div>`
				}
				$('.student_list').html(h);
			}else{
				alert("加载失败,请稍后重试");
			}
		});
	}


});
