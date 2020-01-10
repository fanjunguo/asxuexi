$(document).ready(function() {
	let $body=$('body');
	
	let $classTable=$('.class_table');
	let $editTable=$('.edit_table');

	let $classListContainer=$('.class_list_container');
	let $editorContainer=$('.editor_container');
	var classId; //全局变量:存放所选班级的id
	var courseId;//所选课程的id
	

	//账单编辑的原始数组(包含总价)
	var totolPrice=0; //服务器参数
	var resetColumn=[
		        {field:'student_id',hidden:true},
		        {field:'student_name',title:'学生姓名',width:100,halign:'center',align:'center'},
		        {field:'billPeriod',title:'账单周期',width:200,halign:'center',align:'center'},
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
		        {field:'totalPrice',title:'费用总计',width:90,halign:'center',align:'center',
		        	formatter:function(value,row,index){ //row仅仅是从服务器返回的数据,不包含后期前端加入的数据
		        		return (row.totalTimes*totalOfUnitPrice+totalOfPriceOfOnce).toFixed(2);
		        	}
		    	}
			];
	var column=[].concat(resetColumn);
	
	$classTable.datagrid({
		title:'账单管理',
		url:'bill/getClassList.action',
		columns:[[
		        {field:'class_id',hidden:true},
		        {field:'course_id',hidden:true},
		        {field:'class_name',title:'班级名',width:200,halign:'center',align:'center'},   
		        {field:'coursename',title:'课程名',width:250,halign:'center',align:'center'},
		        {field:'lastBillDate',title:'上次结算日期',width:150,halign:'center',align:'center',
		        	formatter:function(value,row,index){
		        		if (value=='null') {
		        			return '——';
		        		}else{
		        			return value;
		        		}

		        	}
		    	},
		        {field:'operate',title:'操作',width:250,halign:'center',align:'center',
		        	formatter: function(){
							return '<button class="button" id="editCurrentBill">编辑本期账单</button>\
								<button class="button" id="thePastPeriodBill">查看往期账单</button>';
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

	//编辑本期账单
	$body.on('click', '#editCurrentBill', function() {
		$classListContainer.css('display', 'none');
		$editorContainer.css('display', 'block');
		let rowObj=$classTable.datagrid('getSelected');
		classId=rowObj.class_id;
		courseId=rowObj.course_id;
		loadDataGrid();
	});

	//返回按钮
	$body.off('click', '.back').on('click', '.back', function() {
		if (index!=0) {
			appendConfirmWindow('提示','您编辑的账单还未保存,是否放弃本次编辑内容?','btn_yes');
		}else{
			returnToClassList();
		}
	});

	$body.off('click', '#btn_yes').on('click', '#btn_yes', function() {
		returnToClassList();
		
	});



	//增加收费项
	$('.add_item').click(function() {
		$('#add_item_window').modal('show');
	});
	
	//点击选择计费类型
	let $priceType=$('.price_type');
	let id='by_times';
	$priceType.click(function() {
		let $this=$(this);
		let className=$this.attr('class');
		if (className.search("active")==-1) {
			$priceType.removeClass('active');
			$this.addClass('active');
			id=$this.attr('id');
			$('.price_input_div').removeClass('show');
			$(`.${id}`).addClass('show');
			$('.item_price_once,.item_price_times').numberbox('clear');  //清除价格框
		}
		
	});


	//确定增加收费项
	let $nameInput=$('.item_name');
	let index=0;
	// --5.14新增更新:为了计算费用总和----
	let totalOfUnitPrice=0; //所有按次计费的单价的和
	let totalOfPriceOfOnce=0; //所有一次性收费的费用的和

	let $priceInputByTimes=$('.item_price_times');
	let $priceInputByOnce=$('.item_price_once');
	//数组:记录用户增加的收费项
	let itemArray=new Array();

	$('#btn_sure').click(function() {
		index++;
		let newItem;
		let itemName=$nameInput.val();
		let itemObject; //记录用户增加的每一个收费项信息 type:1表示按学期计费 2表示一次性收费
		if (id=='by_times') {
			let itemPriceByTimes=parseFloat($priceInputByTimes.val()).toFixed(2);
			totalOfUnitPrice+=parseFloat(itemPriceByTimes);
			newItem={field:'newItem'+index,title:itemName,width:80,halign:'center',align:'center',
						formatter:function(value,row,index){
			        		return (itemPriceByTimes*row.totalTimes).toFixed(2);
			        	}
					};
			itemObject={field:'newItem'+index,title:itemName,type:1,price:itemPriceByTimes};  //type:1表示按学期计费 2表示一次性收费
		} else {
			let itemPriceByOnce=parseFloat($priceInputByOnce.val()).toFixed(2);
			totalOfPriceOfOnce+=parseFloat(itemPriceByOnce);
			newItem={field:'newItem'+index,title:itemName,width:80,halign:'center',align:'center',
						formatter:function(){
			        		return itemPriceByOnce;
			        	}
					};
			itemObject={field:'newItem'+index,title:itemName,type:2,price:itemPriceByOnce};
		}
		column.splice(column.length-1,0,newItem);
		itemArray.push(itemObject);
		loadDataGrid();
		clearInput();
	});

	$('#btn_close,.close_icon').click(function() {
		clearInput();
	});

	
	//点击保存账单
	$('.send_bill').click(function() {
		if (index==0) {
			appendConfirmWindow('提示','当前账单还没有增加费用项,是否继续保存?','sureToSave');
		} else {
			appendConfirmWindow('提示','账单生成之后将会发送给学生收款,请确认是否保存','sureToSave');
		}
	});

	$body.off('click', '#sureToSave').on('click', '#sureToSave', function() {
		let billPeriod=$editTable.datagrid('getData').rows[0].billPeriod;
		$.post('bill/saveBill.action',{items:JSON.stringify(itemArray),courseId:courseId,classId:classId,period:billPeriod} ,function(data) {
			if (data.code==600) {
				$body.append(`<div id="myAlert" class="alert alert-success">
								    <strong>账单发送成功</strong>
								</div>`);
				$('#myAlert').show('fast');
				setTimeout(function(){
					$('#myAlert').remove();
					returnToClassList();
					$classTable.datagrid('reload');
				},2000);
				
			} else {
				appendConfirmWindow('错误','网络错误,账单保存失败!请刷新后重试');
			}
		});
	});


/*   --查看往期账单--    */
	

	let classId_Selected;  //选择的班级的 id
	let $billPeriodInput=$('.bill_period_input');
	let billPeriod_Select;  //选择的账期

	$body.off('click', '#thePastPeriodBill').on('click', '#thePastPeriodBill', function() {
		let rowData=$classTable.datagrid('getSelected');
		classId_Selected=rowData.class_id;
		var allBillColumn=[
	        {field:'student_id',hidden:true},   
	        {field:'student_name',title:'学生姓名',width:150,halign:'center',align:'center'},
	        {field:'totalTimes',title:'出勤次数',width:80,halign:'center',align:'center'},
	        {field:'totalHour',title:'总课时',width:80,halign:'center',align:'center',
	        	formatter:function(value,row,index){
	        		if (row.classhour_eachtime==0) {
						return '--';
	        		}else{
	        			return row.classhour_eachtime*row.totalTimes;
	        		}
	        	}
	        },
		];
		let rows; //表格数据
		$.post('bill/getAllBills.action', {classId: rowData.class_id}, function(json) {
			if (json.code==500) {
				rows=[];
				$body.append(`<div id="noBillAlert" class="alert alert-warning">
								    <strong>${rowData.class_name}班 目前没有账单</strong>
								</div>`);
				$('#noBillAlert').show('fast');
				setTimeout(function(){
					$('#noBillAlert').remove();
				},2000);

			}else if(json.code==600){
				rows=json.data.rows;
				getTableColumn(json,allBillColumn);
			
			}
			$('.past_period_table').datagrid({
				title:`"${rowData.class_name}"班账单`,
				columns:[allBillColumn],
				data:rows,
				loadMsg:"加载中...",
				nowrap:true,
				singleSelect:true,
			});

			//处理账期
			let allPeriods=json.data.allPeriods;
			let initDate='';
			if (allPeriods.length>0) {
				for (var i = 0; i < allPeriods.length; i++) {
					allPeriods[i].period=allPeriods[i].start_date+'——'+allPeriods[i].end_date;
				}
				billPeriod_Select=initDate=allPeriods[0].period;
			}

			//初始化下拉框
			$billPeriodInput.combobox({
				data:allPeriods,
			});
			$billPeriodInput.combobox('setValue',initDate);
		});

		$classListContainer.css('display', 'none');
		$('.past_period_container').css('display', 'block');

	});


	//在选择下拉选项时:
	$billPeriodInput.combobox({
		onSelect:function(record){
			billPeriod_Select=record.period;
			$.post('bill/getBill.action', {billId: record.id,classId:classId_Selected ,startDate:record.start_date,endDate:record.end_date}, function(json) {
				var allBillColumn=[
			        {field:'student_id',hidden:true},   
			        {field:'student_name',title:'学生姓名',width:150,halign:'center',align:'center'},
			        {field:'totalTimes',title:'出勤次数',width:80,halign:'center',align:'center'},
			        {field:'totalHour',title:'总课时',width:80,halign:'center',align:'center',
			        	formatter:function(value,row,index){
			        		if (row.classhour_eachtime==0) {
								return '--';
			        		}else{
			        			return row.classhour_eachtime*row.totalTimes;
			        		}
			        	}
			        },
				];
				rows=json.data.rows;
				getTableColumn(json,allBillColumn);

				$('.past_period_table').datagrid({
				columns:[allBillColumn],
				data:rows,
				loadMsg:"加载中...",
				nowrap:true,
				singleSelect:true,
			});
			});
		}
	});


	//点击返回
	$('.back_btn').click(function() {
		$classListContainer.css('display', 'block');
		$('.past_period_container').css('display', 'none');
		$billPeriodInput.combobox('clear');
	});

	//查看考勤明细
	let $background=$('.background');
	let $detailTableContainer=$('.detail_table_container');
	let $tableHead=$('.table_head');
	let $tableBody=$('.table_body');
	
	
	$body.off('click','.getDetail').on('click','.getDetail',function(){
		let thisRowData=$('.past_period_table').datagrid('getSelected');
		$background.css({
			'z-index': 110,
			'display': 'block'
		});
		//计算尺寸
		let width=$detailTableContainer.width();
		$detailTableContainer.css("left",`calc((100vw - ${width}px) / 2)`);
		
		$detailTableContainer.css('display', 'block');
		$.post('attendance/getAttendanceDetail.action', {studentId: thisRowData.student_id,classId:classId_Selected,billPeriod:billPeriod_Select}, function(json) {
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
		$background.css({
			'z-index': 10,
			'display': 'none'
		});
		$detailTableContainer.css('display', 'none');
		$tableHead.html('');
		$tableBody.html('');
	});
	
	
	
	


	/* 方法:根据传入的数据,获取表头
	 * 参数:json-后台得到的数据
	 		allBillColumn-表头的数组
	 */
	function getTableColumn(json,allBillColumn){
		let items=eval(json.data.items);
		//构建表头
		if (items.length>0) {
			for (var i = 0; i < items.length; i++) {
				let thisItems=items[i];
				let tempArray={field:thisItems.field,title:thisItems.title,width:200,halign:'center',align:'center',
						formatter:function(value,row,index){
							if (thisItems.type==1) {
								return thisItems.price*row.totalTimes;
							}else{
								return thisItems.price;
							}
						}
					};
				allBillColumn.push(tempArray);
			}
		}
		allBillColumn.push({field:'operate',title:'操作',width:200,halign:'center',align:'center',
						        	formatter: function(){
											return '<button class="button getDetail">查看考勤明细</button>';
									}
						    });
	}


	/* 方法:写入确认窗口
		参数:confirmContext 文本内容;
			buttonId 确定按钮的id
	*/	
	function appendConfirmWindow(title,confirmContext,buttonId){
		$('.confirmContainer').html(`<div class="modal fade" id="confirm_window" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
							&times;
						</button>
						<h4 class="modal-title" id="myModalLabel">${title}</h4>
					</div>
					
					<div class="modal-body">
						${confirmContext}
					</div>

					<div class="modal-footer">
						<button type="button" id="${buttonId}" class="btn btn-primary" data-dismiss="modal">确定</button>
						 <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					</div>
				</div>
			</div>
		</div>`);

		$('#confirm_window').modal('show');
	}

	/* 方法:返回到课程列表页面,同时重置各种变量 */
	function returnToClassList(){
		$classListContainer.css('display', 'block');
		$editorContainer.css('display', 'none');
		//重置表头和计数index
		column=[].concat(resetColumn);
		index=0;
		totalOfUnitPrice=0;
		totalOfPriceOfOnce=0;
		itemArray=new Array();
	}
	
	/* 方法:加载学生名单 */
	function loadDataGrid(){
		$('.edit_table').datagrid({
			url:'attendance/getAttendance.action',
			loadFilter: function(json){
				let data=json.data;
				if (data.length==0) {
					alert("当前没有未结算的考勤");
				}
				return {rows:data,total:40}
			},
			columns:[column],
			loadMsg:"加载中...",
			nowrap:true,
			queryParams:{ //参数
				classId:classId,
			},
			fitColumns:true,
			singleSelect:true,
			toolbar:'.toolbar',
		});
	}
	
	
	/* 清除输入框的内容*/
	function clearInput(){
		$priceInputByTimes.numberbox('clear');
		$priceInputByOnce.numberbox('clear');
		$nameInput.val('');
	}
});

	