/* 待完成的内容
*	1.部分退款和取消订单并且退款
*		由于未明确不同时期的退款比例,暂未做退款规则处理.当前默认取消订单,全部退款
*
*/
$(function(){
	var $body=$('body');
	var orderId;
	var reasonCode;
	$body.on('click', '.cancel_order', function() {
		// let typeCode=$(this).attr('type');  判断取消订单的时候是否发生退款,然后作出不同处理
		$('#cancel_confirm_window').modal();
		orderId=$(this).parent().attr('order_id');
		
	});

	
	$('.cancel_reason_radio').click(function(){
		reasonCode=$("input[name='cancelReason']:checked").val();
		if (typeof(reasonCode)!='undefined') {
			$('.btn-primary').attr('id', 'submit');
		}
	})

	$body.on('click', '#submit', function() {
		$.post('order/cancelOrder.action', {orderId: orderId,reasonCode:reasonCode,isOrg:true}, function(json) {
			$('#cancel_confirm_window').modal('hide');
			if (json.code==600) {
				window.location.reload();
			} else {
				alert('取消失败');
			}
		});
		
	});

	$('#cancel_confirm_window').on('hidden.bs.modal',function(){
		let radio=document.getElementsByName('cancelReason');
		for (var i = 0; i < radio.length; i++) {
			radio[i].checked=false;
		}
		$('#submit').removeAttr('id');
	});

	//部分退款
	$body.on('click', '.part_refund', function() {
		alert('功能暂未开放');
	});

	//加载订单数据*(不包括账单)
	$.post('order/getAllOrders.action',{identity:'org'}, function(json) {
		if (json.code==600) {
			var orderHtml='';
			var orderList=json.data;
			for (var i = 0; i < orderList.length; i++) {
				let chargeTypeText='';
				let operationHtml='取消订单并退款';
				let refundHtml='仅退款';
				let type=2; //1-仅取消订单 2-取消订单并且退款
				if (orderList[i].chargeType==1) {
					chargeTypeText='预收费';
				}else{
					chargeTypeText='后收费';
				}
				let orderStatus=orderList[i].orderStatus;
				switch(orderStatus){
					case 10:
						orderStatus='等待付款';
						operationHtml='取消订单';
						type=1;
						refundHtml='';
						break;
					case 11:
						orderStatus='付款成功';
						break;
					case 12: 
						orderStatus='交易完成';
						break;
					case 13: 
						orderStatus='退款中';
						operationHtml='';
						refundHtml='';
						break;
					case 14: 
						orderStatus='已取消(已退款)';
						operationHtml='';
						refundHtml='';
						break;
					case 20: 
						orderStatus='进行中';
						operationHtml='取消订单';
						type=1;
						refundHtml='';
						break;
					case 21: 
						orderStatus='交易完成';
						operationHtml='';
						refundHtml='';
						break;
					case -1: 
						orderStatus='已取消';
						operationHtml='';
						refundHtml='';
						break;
				}

				orderHtml+=`<tbody>
						<tr class="space_row"></tr>
						<tr class="order_number">
							<td colspan="5">
								<span>订单号: ${orderList[i].orderId}</span> 
								<span>创建时间: ${orderList[i].gmt_create.substring(0,orderList[i].gmt_create.length-4)}</span> 
							</td>
						</tr>
						<tr class="order_message">
							<td class="td1">
							<a target="_blank" href="courseDetails.do?courseId=${orderList[i].courseId}">
								<img class="course_img" src="${orderList[i].courseImg}">	
								<div class="course_name">${orderList[i].courseName}</div>
							</a>
								<div>${orderList[i].packageName}</div>
							</td>
							<td>
								<div>${orderList[i].studentName}</div>
								<div>${orderList[i].tel}</div>
							</td>
							<td>
								${chargeTypeText}
								<br>
								¥${orderList[i].paymentAmount}
							</td>
							<td>${orderStatus}</td>
							<td order_id="${orderList[i].orderId}">
								<a type=${type} class="cancel_order">${operationHtml}</a>
								<a class="part_refund">${refundHtml}</a>
							</td>
						</tr>
					</tbody>`;
			}
			$('.order_table').append(orderHtml);

		} else {
			alert('网络错误,加载失败');
		}
	});

});