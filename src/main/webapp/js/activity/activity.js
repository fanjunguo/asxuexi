$(function(){
	var url=window.location.href;
	var activityId=url.substring(url.lastIndexOf('=')+1); //活动id
	$.post('activity/getAdInfo.do',{id:activityId},function(json){
		if (json.code==600) {
			let data=json.data;
			let imgUrl=data.parentPath+activityId+".png";
			let imgH=data.imgHeight*$('.container').width()/data.imgWidth; //根据宽度缩放,计算容器的高度
			$('.container').css({
				'background-image':`url(${data.url})`,
				'height':imgH,
			});
			$('.form').show();
		}else{
			$('#myModal').modal()
		}
	});



	//提交信息
	$('.submit').click(function() {
		let name=$('.name').val().trim();
		let tel=$('.tel').val().trim();
		//校验填写信息
		if ($('.validtate_form').valid()) {
			$.post('activity/submit.do', {id:activityId ,name: name,tel:tel}, function(json) {
				if (json.code==600) {
					alert("报名成功!");
				}
			});
		}

	});


//验证
var validator=$('.validtate_form').validate({
    //指定错误信息容器
    // errorContainer:$('.error_message'),
    // errorLabelContainer:'.error_message',
    wrapper:'span',
    //触发事件
    onfocusout: function(element){
        $(element).valid();
    },
    //验证规则
    rules:{
        name:{
             required:true,
             maxlength:20,
	 	},
	 	tel:{
	 	     required:true,
	 	     telephone:true
	 	}
    },
    //错误提示内容
	messages:{
	        name:{
	 	    required:'姓名不能为空',
	 	    maxlength:'姓名不能超过20个字符',
	        },
	 	tel:{
	 	    required:'请填写电话号码',
	 	   telephone:'电话号码格式错误',
	 	}
    }
});

jQuery.validator.addMethod('telephone',function(value, element){
	let tel_pattern=/^1[3-9]{1}[0-9]{9}$/;
	return this.optional(element) || (tel_pattern.test(value));
});

})