$(function() {
	var $body=$('body');
	var orderId='';
	var reasonCode;
	
	//取消订单
	$body.on('click', '.cancel_order', function() {
		$('#cancel_confirm_window').modal();
		orderId=$(this).parent().attr('order_id');
		return false;
	});
	
	$('.cancel_reason_radio').click(function(){
		reasonCode=$("input[name='cancelReason']:checked").val();
		if (typeof(reasonCode)!='undefined') {
			$('#cancel_confirm_window .btn-primary').attr('id', 'submit');
		}
	})

	//确定取消订单
	$body.on("click","#submit",function(){
		$.post('order/cancelOrder.action', {orderId: orderId,reasonCode:reasonCode}, function(json) {
			if (json.code==600) {
				window.location.reload();
			} else {
				$('.alert-warning').show();
			}
		});
		$('#cancel_confirm_window').modal('hide');
	});
	
	$('#cancel_confirm_window').on('hidden.bs.modal',function(){
		//重置已选择的内容
		let radio=document.getElementsByName('cancelReason');
		for (var i = 0; i < radio.length; i++) {
			radio[i].checked=false;
		}
		//移除提交状态
		$('#submit').removeAttr('id');
	});
	
	
	//点击箭头按钮,展开所有的订单
	$body.on('click', '.pull', function() {
		//查询账单信息
		let $this=$(this);
		$this.addClass('push').removeClass('pull');
		$this.css('top','16px');
		$this.prev().children('.all_bills').slideDown('slow');
	});

	//收起账单
	$body.on('click', '.push', function() {
		let $this=$(this);
		$this.addClass('pull').removeClass('push');
		$this.css('top','19px');
		$this.prev().children('.all_bills').slideUp('slow');
	});

	
	//点击“查看考勤明细”,请求考勤详情
	$body.on('click', '.btn_checkAttendance', function() {
		let $this=$(this);
		let classId=$this.attr('class_id');
		let billPeriod=$this.prevAll('.bill_period').html();
		obj.loadAttendanceDetail($this.attr('student_id'),classId,billPeriod);
		
	});

	//删除订单
	$body.on('click','.del_button',function(){
		let $deleteModal=$('#delete_confirm');
		$deleteModal.modal();
		let $tbody=$(this).parents('tbody');
		let _id=$(this).attr('_id');
		$body.off('click','#delete_sure').on('click','#delete_sure',function(){
			$.post('order/deleteOrder.action',{orderId:_id},function(json){
				if (json.code==600) {
					$deleteModal.modal('hide');
					$tbody.remove();
				}else{
					$('.alert-warning').show();
				}
			});
		});
	})
	
	//支付订单
	$body.on('click','.payment',function(){
		let orderId=$(this).parent().attr('order_id');
		window.location.href="//www.asxuexi.cn/pagers/order/payment.jsp?"+orderId;
		
	});
	
	//付款给机构
	$body.on('click','.pay_to_org',function(){
		$('#pay_to_org_confirm').modal();
		orderId=$(this).parent().attr('order_id');
	});
	$('#pay_sure').click(function(){
		$.post('order/PersonPayToOrg.action',{orderId:orderId},function(json){
			if (json.code=600) {
				window.location.reload();
			} else {
				$('.alert-warning').show();
			}
		});
	});
	
	//加载所有订单数据
	loadAllOrders();
	
	function loadAllOrders(){
		$.post('order/getAllOrders.action',{identity:'person'}, function(json) {
			if (json.code==600) {
				let allOrders=json.data;
				let order_html='';
				if (allOrders.length>0) {
					for (var i = 0; i < allOrders.length; i++) {
						let time=allOrders[i].orderMap.gmt_create;
						let gmt_create=time.substring(0,time.lastIndexOf('.'));
						let address=allOrders[i].orderMap.address;
						if (address!=null) {
							address=address.substring(3).replace(/,/g,'');
						}else{
							address=""
						}
						let orderStatus=allOrders[i].orderMap.orderStatus;
						let operationHtml;  //操作按钮组
						let billMessageHtml='';//账期信息
						let chargeTypeText='';
						let eachOrderId=allOrders[i].orderMap.orderId;
						
						if (allOrders[i].orderMap.chargeType==1) {
							chargeTypeText='预付学费';
							operationHtml=`<a class="cancel_order">取消订单</a> |
								<a class="pay_to_org" href="javascript:void(0)">付款给机构</a>`;  //操作按钮
							if (orderStatus==10) {
								operationHtml=`<a class="payment">立即支付</a> | <a class="cancel_order">取消订单</a>`;  
							}

						}else{
							chargeTypeText='后付学费';
							operationHtml=`<a class="cancel_order">取消订单</a> |
							<a href="javascript:void(0)">查看账单</a>`;
							let billPeriodHtml='';
							let studentId=allOrders[i].orderMap.studentId;
							if (allOrders[i].billList.length>0) {
								let aHtml='';
								for (var j = 0; j < allOrders[i].billList.length; j++) {
									let status=(allOrders[i].billList)[j].status
									if (status==0) {
										status='未支付';
										aHtml="您有未支付的账单,请及时结清";
									}else{
										status='已支付';
									}
									billPeriodHtml+=`<div class="all_bills">
											<ul>
												<li>
													<span class="bill_period">${(allOrders[i].billList)[j].billPeriod}</span>
													<span>¥${(allOrders[i].billList)[j].bill_amount}</span>
													<span>${status}</span>
													<span class="btn_checkAttendance" class_id="${(allOrders[i].billList)[j].class_id}" student_id=${studentId}>查看考勤明细</span>
												</li>
											</ul>
										</div>`;
								}
								billMessageHtml=`<tr class="bill_message">
									<td colspan="5">
										<a>${aHtml}</a>
										<div class="toggle">${billPeriodHtml}</div>
										<span class="pull_container pull"><img src="img/icons/pull.png"></span>
									</td>
								</tr>`;
							}else{
								//todo:暂无账单,进行相应的处理,不展示下拉按钮或者下拉无效
							}
						}
						
						switch(orderStatus){
							case 10:
								orderStatus='待付款';
								break;
							case 11:
								orderStatus='支付成功';
								break;
							case 12: 
								orderStatus='交易完成';
								operationHtml='';
								break;
							case 13: 
								orderStatus='退款中';
								operationHtml='';
								break;
							case 14: 
								orderStatus='已取消(已退款)';
								operationHtml='';
								break;
							case 20: 
								orderStatus='进行中';
								break;
							case 21: 
								orderStatus='交易完成';
								operationHtml='';
								break;
							case -1: 
								orderStatus='已取消';
								operationHtml='';
								break;
						}

						order_html+=`<tbody>
							<tr class="space_row"></tr>
							<tr class="order_number">
								<td colspan="5">
									订单号: <span class="order_id">${eachOrderId}</span>
									<span class="order_date">${gmt_create}</span>
									<a target="_blank" href="pagers/orgDetail.jsp?orgId=${allOrders[i].orderMap.orgId}">${allOrders[i].orderMap.orgName}</a>
									<a class="del_button" _id="${eachOrderId}"></a>
								</td>
							</tr>
							<tr class="order_message">
								<td class="td1">
									<a target="_blank" href="courseDetails.do?courseId=${allOrders[i].orderMap.courseId}"><img class="course_img" src="${allOrders[i].orderMap.courseImg}">
									</a>
									<div class="course_name">
										<a target="_blank" href="courseDetails.do?courseId=${allOrders[i].orderMap.courseId}"><strong>${allOrders[i].orderMap.courseName}</strong></div></a>
									<div>套餐: ${allOrders[i].orderMap.packageName}</div>
									<div>地址:${address}</div>
								</td>
								<td>
									<div>${allOrders[i].orderMap.studentName}</div>
									<div>${allOrders[i].orderMap.tel}</div>
								</td>
								<td>
									<span>¥${allOrders[i].orderMap.paymentAmount}</span>
									<br>
									<span>${chargeTypeText}</span>
								</td>
								<td>${orderStatus}</td>
								<td order_id=${eachOrderId}>
									${operationHtml}
									<p><a target="_blank" href="order/orderDetail.action?orderId=${eachOrderId}">查看详情</a></p>
								</td>
							</tr>
							${billMessageHtml}
						</tbody>`;
					}

				}else{
					order_html='<td colspan="5" class="no_data">您当前没有订单~</td>';
				}
				$('.table_container>table').append(order_html);
			}else{
				//数据错误
				$('.alert-warning').show();
			}
		});
	}
	
});