$(function() {
	var $body=$('body');
	var amountEnable;
	var allRecords; //记录总的收支记录

	//加载账户余额信息
	getAccountInfo();
	//加载收支记录明细
	$.get('orgAccount/getCashFlowInfo.action', function(json) {
		if (json.code==600) {
			allRecords=json.data;
			loadRecords();
		}else{
			//服务器异常
			$('.result_message').text('服务器异常,请联系网站客服处理');
			$('#result_window').modal();
		}
	});

	/*方法:加载记录
	 *参数:用于筛选,通过参数确定加载哪一项纪录 1-收入 2-提现 3-退款 4-服务费;如果不传,表示加载所有的纪录(不做筛选)	
	 */
	function loadRecords(operation){
		let data=filtRecords(operation);
		let html='';
		if (data.length>0) {
			for (var i = 0; i < data.length; i++) {
				let time=data[i].gmt_created;
				let index=time.lastIndexOf('.');
				time=time.substring(0,index).replace('T',' ');

				let operation=data[i].operation;
				let remark='';
				if (operation==1) {
					operation="订单收入";
				}else if (operation==2) {
					operation="提现";
				}else if(operation==3){
					operation="退款";
					remark='订单退款';
				}else if(operation==4){
					operation='服务费';
				}

				html+=`
					<tr>
						<td class="time_container">${time}</td>
						<td class="type${data[i].flowDirection}">${data[i].amount*data[i].flowDirection}</td>
						<td>${operation}</td>
						<td>${remark}</td>
					</tr>`;
				}
			}else{
				html=`<div class="no_data">暂无记录</div> `;
			}
			$('.cash_flow_record').html(html);
	}

	/*方法:筛选数据
	 *参数:用于筛选,通过参数确定加载哪一项纪录 1-收入 2-提现 3-退款;如果不传,表示加载所有的纪录(不做筛选)
	*/
	function filtRecords(operation){
		let data=new Array();
		if (typeof(operation)=='undefined') {
			data=allRecords;	
		}else{
			for (var i = 0,len=allRecords.length; i <len ; i++) {
				if (allRecords[i].operation==operation) {
					data.push(allRecords[i]);
				}
			}
		}
		return data;
	}

	//点击切换记录
	$('.selection_li').click(function() {
		$('.active').removeClass('active');
		$(this).addClass('active');
		let id=$(this).attr('id');
		if(id=='all_records'){
			loadRecords();
		}else{
			loadRecords(2);
		}
	});

	/*提现*/
	
	var ownerName='';
	var nameSymbol=false;
	var cardNumber='';
	var cardSymbol=false;
	var cardInfo;
	var amount='';
	var amountSymbol=false;
	var $amount=$('.textbox-text');
	var $bankCard= $('.bank_card');
	var $ownerName=$('.owner_name');
	//展示窗口
	$('.cash_out').click(function() {
		$('.submit_btn').attr('id', 'submit');
		$('#bank').modal();
		$amount.attr('placeholder', '填写提现金额');
	});
	//验证
	$ownerName.focusout(function() {
		let $this=$(this);
		ownerName=$this.val().trim();
		if (ownerName.length==0) {
			$this.addClass('error');
		}else{
			nameSymbol=true;
		}
	});

	$amount.focusout(function() {
		amount=$amount.val().trim();
		if (amount.length==0||Number(amount)>Number(amountEnable)) {
			$amount.addClass('error');
		}else{
			amountSymbol=true;
		}
	});
	//提现金额不能超过可用金额
	$amount.on('input propertychange',function() {
		let $this=$(this);
		let temp=$this.val().trim();
		if (Number(temp)>Number(amountEnable)) {
			$this.addClass('error');
			if ($('.wrong_message').length==0) {
				$this.parent().parent().after(`<div class="wrong_message">提现金额不能超过可用金额</div>`)
			}
		}else{
			$('.wrong_message').remove();
			$amount.removeClass('error')
		}
	});

	$bankCard.focusout(function() {
		let $this=$(this);
		cardNumber=$this.val().trim();
		if (cardNumber.length==0) {
			$this.addClass('error');
			$('.band_icon').removeAttr('src');
		}else{
			cardInfo=bankCardAttribution(cardNumber);
			if (cardInfo=='error') {
				$('.wrong_tips').text('卡号填写错误');
				$('.band_icon').removeAttr('src');
			}else{
				cardSymbol=true;
				$('.wrong_tips').text('');
				$('.band_icon').attr('src', 'https://apimg.alipay.com/combo.png?d=cashier&t='+cardInfo.bankCode);
			}
		}
	});

	//获得焦点,清除错误样式
	$('.owner_name,.bank_card,.textbox-text').focusin(function() {
		$(this).removeClass('error');
	});

	//提现申请提交
	$('#submit').click(function() {
		//清除id,防止重复提交
		$(this).removeAttr('id');
		if (nameSymbol&&cardSymbol&&amountSymbol) {
			cardInfo.ownerName=ownerName;
			cardInfo.cardNumber=cardNumber;
			cardInfo.amount=amount;
			$.post('orgAccount/getCashOut.action', cardInfo, function(json) {
				let message='';
				if (json.code==600) {
					message='提交成功!审核无误后将在3个工作日内到账';
					getAccountInfo();
					//重新加载记录
					let id=$('.active').attr('id');
					if(id=='all_records'){
						loadRecords();
					}else{
						loadRecords(2);
					}

				}else if(json.code==400){
					message='网络错误,提交失败!请刷新后重试';
				}else{
					message='申请失败,请联系客服处理';
				}
				$('.result_message').text(message);
				$('#bank').modal('hide');
				resetWindow();
				$('#result_window').modal();
			});
		}else if(!amountSymbol){
			$amount.addClass('error');
		}else if(!cardSymbol){
			$bankCard.addClass('error');
		}else if(!nameSymbol){
			$ownerName.addClass('error');
		}
	});

	$('#bank').on('hidden.bs.modal',function() {
		resetWindow();
	});

/* 方法:请求账户信息 */
	function getAccountInfo(){
		$.get('orgAccount/getAccountInfo.action',function(json) {	
			if (json.code==600) {
				let data=json.data;
				$('.account_amount').text( (data.deposit+data.amount_usable+data.amount_entering).toFixed(2));
				$('.deposit').text(data.deposit.toFixed(2)); //保证金
				amountEnable=data.amount_usable.toFixed(2)
				$('.account_enable').text(amountEnable); //可用金额
				$('.amount_entering').text(data.amount_entering.toFixed(2)); //入账中
 			}else{
				//网络错误
			}
		});
	}

	/*方法:重置窗口内容和样式*/
	function resetWindow(){
		$('#amount').numberbox('clear');
		$bankCard.val('')
		$ownerName.val('');
		$('.band_icon').removeAttr('src');
		$('.wrong_tips').text('');
		$('.error').removeClass('error');
	}


});	