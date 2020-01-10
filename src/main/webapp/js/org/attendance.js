$(document).ready(function(){
	let $body=$('body');
	let $save=$('.save');
	let $background=$('.background');
	let $tableContainer=$('.table_container');
	let $previewTableContainer=$('.preview_table_container');
	let $detailTableContainer=$('.detail_table_container');
	let $tableHead=$('.table_head');
	let $tableBody=$('.table_body');
	let $detailHeader=$('.detail_header')


	let $classTable=$('#class_table');
	//获取当前日期
	var date = new Date();
    var seperator = "-";
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
	let today=year+seperator+month+seperator+strDate;
	var classId; //所选的课程id
	var rowIndex;//被选中的行的索引

	//班级名单
	$('#class_table').datagrid({
		url:'attendance/getClassList.action',
		loadFilter:function(data){
			if ((data.rows).length==0) {
				$body.append(`<div id="noClassAlert" class="alert alert-warning">
										 <a href="#" class="close" data-dismiss="alert">
									        &times;
									    </a>
									    <strong>您目前还没有排课,暂时无法考勤. 请先进行[<a href="org/courseArrangement.do">排课</a>]</strong>
									</div>`);
				$('#noClassAlert').show('fast').alert();
			}
			return data;
		},
		columns:[[
		        {field:'class_id',hidden:true},   
		        {field:'class_name',title:'班级名',width:200,halign:'center',align:'center'},   
		        {field:'coursename',title:'课程名',width:250,halign:'center',align:'center'},
		        {field:'attendance',title:'考勤情况 ('+today+')',width:150,halign:'center',align:'center',
		        	formatter: function(value,row,index){
						if (value==0){
							return '<div style="color:#ff7946">今日未考勤</div>';
						} else {
							return '<div style="color:green">今日已考勤</div>';
						}
					}
		        },
		        {field:'operate',title:'操作',width:210,halign:'center',align:'center',
		        	formatter: function(value,row,index){
						if (row.attendance==0){
							return '<button class="buttton" id="editAttendance">今日考勤</button>\
									<button class="buttton" id="getAttendance">查看本期考勤</button>';
						} else {
							return '<button class="buttton" id="getAttendance">查看本期考勤</button>';
						}
					}
		        }
		]],
		loadMsg:"加载中...",
		nowrap:true,
		singleSelect:true,
		pagination:true,
		pageNumber:1,
		pageSize:10,
		pageList:[10,20,30],
	});

	//点击考勤,弹窗显示考勤名单
	$body.on('click', '#editAttendance', function() {
		let rowObj=$classTable.datagrid('getSelected');
		rowIndex=$classTable.datagrid('getRowIndex',rowObj);
		classId=rowObj.class_id;
		loadAttendanceTable(classId);
	});

	//点击取消,关闭窗口
	$('.cancel').click(function() {
		modalWindowToggle(0);
	});
	
	//加载班级下的学生表格
	function loadAttendanceTable(classId){
		modalWindowToggle(1);
		$('#attendance_table').datagrid({
		    url:'attendance/getStuents.action',   
		    columns:[[   
		        {field:'student_id',title:'学生编号',hidden:true},   
		        {field:'student_name',title:'学生姓名',width:250,halign:'center',align:'center',height:40},
		        {field:'date',title:'考勤日期',width:250,halign:'center',align:'center',
		        	formatter:function(){
		        		return today;
		        	}
		    	}, 
		        {field:'attend',title:'出勤情况',width:350,halign:'center',align:'center',
		        	formatter: function(value,row,index){
		        		let stuentId=row.student_id;
		        		let chectbox=' <form class="attendance_form"><input name="'+stuentId+'" value="2" type="radio">出勤  <input name="'+stuentId+'" value="0" type="radio">未出勤 </form>';
		        		return chectbox;
					}
		        }
		    ]],
		    rownumbers:true,
		    loadMsg:"加载中...",
		    nowrap:true,
		    singleSelect:true,
		    pagination:true,
		    pageNumber:1,
		    pageSize:10,
		    pageList:[10,20,30],
		    fitColumns:true, //自适应宽度
		    queryParams:{		//额外参数
		    	classId:classId,
		    }
		});
	}


	$body.off('click', '#getAttendance').on('click', '#getAttendance', function() {
		let rowObj=$('#class_table').datagrid('getSelected');
		classId=rowObj.class_id;
		previewTableWindowToggle(1);
		$('.preview_table').datagrid({
			title:'考勤汇总',
			url:'attendance/getAttendance.action',
			loadFilter: function(json){
				let data=json.data;
				if (data.length==0) {
					$body.append(`<div id="myAlert" class="alert alert-warning">
										 <a href="#" class="close" data-dismiss="alert">
									        &times;
									    </a>
									    <strong>本期暂无考勤. 已生成账单的考勤可在[<a href="org/bill.do">账单管理</a>]中查看</strong>
									</div>`);
					$('#myAlert').show('fast').alert();
				}
				return {rows:data,total:10}
				
			},
			columns:[[
		        {field:'student_id',hidden:true},   
		        {field:'student_name',title:'学生姓名',width:150,halign:'center',align:'center'},
		        {field:'billPeriod',title:'考勤周期',width:200,halign:'center',align:'center'},
		        {field:'totalTimes',title:'出勤天数',width:80,halign:'center',align:'center'},
		        {field:'totalHour',title:'总课时',width:80,halign:'center',align:'center',
		        	formatter:function(value,row,index){
		        		if (value==0) {
							return '--';
		        		}else{
		        			return value;
		        		}
		        	}
		        },
		        {field:'operate',title:'操作',width:200,halign:'center',align:'center',
		        	formatter: function(){
								return '<button class="buttton" id="checkDetail">考勤明细</button>';
						}
		    	},
			]],
			loadMsg:"加载中...",
			nowrap:true,
			queryParams:{ //参数
				classId:classId,
			},
			fitColumns:true,
			singleSelect:true,
		});
	});
	
	$('.preview_close').click(function() {
		previewTableWindowToggle(0);
	});

	$body.on('click', '#checkDetail', function() {
		$background.css('z-index', 110);
		$detailTableContainer.css('display', 'block');
		let rowObj=$('.preview_table').datagrid('getSelected');
		$detailHeader.text(rowObj.student_name);
		$.post('attendance/getAttendanceDetail.action', {studentId: rowObj.student_id,classId:classId,billPeriod:rowObj.billPeriod}, function(json) {
			if(json.code=600){
				let data=json.data;
				let theadHtml=tbodyHtml='';
				for (var i = 0; i < data.length; i++) {
					theadHtml+='<th>'+data[i].date+'</th>';
					let att =data[i].attendance;
					if (att==2) {
						tbodyHtml+='<td class="green">出勤</td>';
					}else if(att==1){
						tbodyHtml+='<td class="orange">半天勤</td>';
					}else{
						tbodyHtml+='<td class="red">缺勤</td>';
					}
				}
				$tableHead.html(theadHtml);
				$tableBody.html(tbodyHtml);
			}
		});
	});

	$('.detail_close').click(function() {
		$background.css('z-index', 10);
		$detailTableContainer.css('display', 'none');
		$tableHead.html('');
		$tableBody.html('');
	});



	/*
	*	方法:模块框显示或隐藏
	* 	参数:operate:1-显示;0隐藏
	*/
	function modalWindowToggle(operate){
		if (operate==1) {
			$background.css('display', 'block');
			$tableContainer.css('display', 'block');
		} else {
			$background.css('display', 'none');
			$tableContainer.css('display', 'none');
		}
	}

	function previewTableWindowToggle(operate){
		if (operate==1) {
			$background.css('display', 'block');
			$previewTableContainer.css('display', 'block');
		} else {
			$background.css('display', 'none');
			$previewTableContainer.css('display', 'none');
		}
	}




	/*
	*	方法:显示提示信息框
	*	参数:message-消息内容
	*/
	function showTips(message){
		$('.modal-body').html(message);
		$('#tip_window').modal('show');
	}


	//点击提交按钮
	$('.save').click(function() {
		let allRows=$('#attendance_table').datagrid('getRows');
		console.log(allRows)
		let dataArray=new Array();
		let rowNumber=allRows.length;
		for (var i = 0; i <rowNumber; i++) {
			let stuentId=allRows[i].student_id;
			let val=$('input[name='+stuentId+']:checked').val();
			if (typeof(val)!='undefined') {
				dataArray[i]={id:stuentId,val:val};
			}else{
				showTips('您有未填写的考勤!请填写完整');
				break;
			}
		}
		if (rowNumber==dataArray.length) {
			//提交数据
			$.post('attendance/saveAttendance.action', 
				{attendance:JSON.stringify(dataArray),classId:classId}, function(data) {
				if (data.code==600) {
					showTips('保存成功');
					$classTable.datagrid('reload'); 
					modalWindowToggle(0);

				}else{
					showTips('网络异常,保存失败.请刷新后重试');
				}
			});
			
		}
	});

	
});