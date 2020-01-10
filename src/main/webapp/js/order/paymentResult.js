$(document).ready(function() {
	let url=window.location.href;
	let index=url.lastIndexOf('=')+1;
	if (index>0) {
		let orderId=url.substring(index);
		$.post('order/getOrderPaymentResult.action', {orderId: orderId}, function(json) {
			if (json.code==600) {
				let orderStatus=json.data.orderStatus
				if (orderStatus==11||orderStatus==20) { 
					$('.order_id').text(orderId)
				}else{
					$('.error_container').show();
					$('.container').hide();
				}
			} else {
				$('.error_container').show();
				$('.container').hide();
			}
		});
	}else{
		window.location.href="https://www.asxuexi.cn";
	}
	

});