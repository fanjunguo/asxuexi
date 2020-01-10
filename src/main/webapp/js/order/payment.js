$(function() {
	$('#logo').append('<div class="page-name">--收银台</div>');
	var homepage='https://www.asxuexi.cn';
	var orderPrice;
	var orderId;
	var url=window.location.href;
	var index=url.lastIndexOf('?')+1;
	if (index>0) {
		orderId=url.substring(index);
		if (orderId.trim().length==0) {
			window.location.href=homepage;
		} else {
			//查询订单信息
			$.post('order/getOrderInfo.action', {orderId: orderId}, function(data) {
				if (data.code==412) {
					window.location.href=homepage;
				}else if(data.code==600){
					let orderInfo=data.data;
					if (orderInfo!=null) {
						orderPrice=orderInfo.payment_amount;
						if (orderPrice>0) {
							$('.payment-container').show();
							$('.submit').text('确认支付');
						}
						//写入订单信息
						$('#order_id').text(orderInfo.order_id);
						$('.course-name').text(orderInfo.coursename);
						$('#order_price').text(orderPrice.toFixed(2));
						$('.package-name').text(orderInfo.package_name);
						$('.student-name').text(orderInfo.student_name);
					} else {
						$('.order_info_body').hide();
						$('.error').show();
					}
				}else{
					$('.order_info_body').hide();
					$('.error').show();
				}
			});
		}
	} else {
		window.location.href=homepage;
	}


	//确定支付/报名,弹出新的窗口
	$('.submit').click(function() {
		if (orderPrice==0) {
			let newWindow=window.open();
			$.post('order/updateOrderStatus.action', {orderId: orderId}, function(json) {
				if (json.code==600) {
					newWindow.location=homepage+"/pagers/order/paymentResult.jsp?orderId="+orderId;
				} else {
					newWindow.close();
					alert("网络错误,请稍后重试");
				}
			});
		} else {
			window.open("pagers/order/newTabForJump.jsp?orderId="+orderId);
		}
		$('#payment_window').modal();
	});


	$('.success').click(function() {
		window.location.href=homepage+"/order/orderDetail.action?orderId="+orderId;
	});
	$('.failure').click(function() {
		window.location.reload();
	});

});