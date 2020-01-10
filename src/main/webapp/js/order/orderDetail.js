$(function() {
	var $body=$('body');
	var url=window.location.href;
	var index=url.lastIndexOf('=')+1;
if (index>0) {
	var orderId=url.substring(index);
	var homepage='//www.asxuexi.cn';

	var reasonCode;


	$body.on('click', '.pay_order', function() {
		window.location.href=homepage+'/pagers/order/payment.jsp?'+orderId;
	});

	$body.on('click', '.cancel_order', function() {
		$('#cancel_confirm_window').modal();
	});

	$('#cancel_reason').on('input propertychange', function() {
		reasonCode=$("input[name='cancelReason']:checked").val();
		if (typeof(reasonCode)!='undefined') {
			$('#cancel_confirm_window .btn-primary').attr('id', 'submit');
		}
	});
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
	//重置样式
	$body.on('click','#cancel,.close',function(){
		let radio=document.getElementsByName('cancelReason');
		for (var i = 0; i < radio.length; i++) {
			radio[i].checked=false;
		}
	});


	//加载订单
	$.post('order/getOrderDetail.action', {orderId:orderId}, function(json) {
		if (json.code==600) {
			let order=json.data;
			let orderStatus;
			let buttonHtml=``;
			switch(order.order_status){
				case 10:
					orderStatus='待付款';
					buttonHtml=`<span class="span_btn pay_order">立即付款</span>
								<span class='span_btn cancel_order'>取消订单</span>`;
					break;
				case 11:
					orderStatus='支付成功';
					buttonHtml=`<span class='span_btn cancel_order'>取消订单</span>`;
					break;
				case 12: 
					orderStatus='交易完成';
					buttonHtml=`<span class='span_btn cancel_order'>取消订单</span>`;
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
					buttonHtml=`<span class='span_btn cancel_order'>取消订单</span>`;
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

			let courseTime;
			if (order.courseBegin==0) {
				courseTime='长期有效';
			} else {
				courseTime=timestampToTime(order.courseBegin)+'至'+timestampToTime(order.courseEnd);
			}
			
			let address=order.address;
			if (address!=null) {
				address=address.substring(3).replace(/,/g,'');
			}else{
				address=""
			}
			let courseLength=order.course_length;
			if (courseLength==0) {
				courseLength="——";
			}
			let orderTime=timestampToTimeMs(order.gmt_create);
			let html2;
			if (order.charge_type==1) {
				html2=`<div class="frame">
							<p>结算方式: <span>预付费</span></p>
							<p>课程总金额: <span>¥${order.payment_amount}</span></p>
							<p>优惠金额: <span>¥0</span></p>
							<p>支付金额: <span>¥${order.payment_amount}</span></p>
						</div>`;
			} else {
				html2=`<div>
							<p>结算方式: <span>后付费</span></p>
							<p>课程总金额: <span>*课程费用以每次机构产生的账单为准</span></p>
						</div>`;
			}
			
			let html1=`<div class="info_header" onselectstart="return false">
				订单号: <span>${order.order_id}</span>
				报名时间: <span>${orderTime}</span>
				订单状态: <span class="red">${orderStatus}</span>
				${buttonHtml}
			</div>
			<div class="courseInfo">
				<h5>课程信息</h5>
				课程名称: <p class="row1"><a href="courseDetails.do?courseId=${order.course_id}">${order.coursename}</a></p>
				套餐名称: <p class="row1">${order.package_name}</p>
				<br>	
				老师: <p class="row2">${order.teacher}</p>
				课程时间: <p class="row3">${courseTime}</p>
				课时: <p class="row2">${courseLength}</p>
			</div>
			<div class="orgInfo">
				<h5>机构信息</h5>
				机构名称: <p class="row1">${order.orgName}</p>
				联系电话: <p class="row1">${order.tel}</p>
				<br>
				地址: <p class="row4">${order.address}</p>
			</div>
			<div class="studentInfo">
				<h5>上课人信息</h5>
				上课人姓名: <p class="row2">${order.student_name}</p>
				联系电话: <p class="row2">${order.student_tel}</p>
			</div>
			<div class="orderInfo">
				<h5>支付信息</h5>
				${html2}
			</div>
			<div class="cancel_rule">
				<h5>退改规则</h5>
				<p>${order.cancelRules}</p>
			</div>`;

			$('.bg').html(html1);
		} else{
			$('.bg').html("<div class='error_message'>出错啦!,请稍后重试</div>");
		}
	});	
} else {
	window.location.href=homepage;
}
	function timestampToTime(timestamp){
	    var date = new Date(timestamp*1000);
	    var Y = date.getFullYear() + '-';
	    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
	    var D = (date.getDate()<10 ? '0' + date.getDate() : date.getDate() )+ ' ';

	    return Y+M+D;
	}

	function timestampToTimeMs(timestamp){
	    var date = new Date(timestamp);
	    var Y = date.getFullYear() + '-';
	    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
	    var D = (date.getDate()<10 ? '0' + date.getDate() : date.getDate() )+ ' ';
	    var Hour=addZero(date.getHours())+':';
	    var Min=addZero(date.getMinutes())+':';
	    var Second=addZero(date.getSeconds());
	    return Y+M+D+Hour+Min+Second;
	}

	function addZero(num){
	    if(num < 10){
	        num = '0'+num;
	    }
	    return num;
	}

});