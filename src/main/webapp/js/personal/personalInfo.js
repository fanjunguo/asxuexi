$(document).ready(function(){
	let Url="https://www.asxuexi.cn";
//	let Url="http://192.168.16.105:8080/webviews";
	//取得绑定成败的标识
	var message=$("#AuthenticationInformation .message").val();
	//失败，未知错误
	if ("0"==message) {
		$("#messageModal").modal("show");
		$("#messageModal .notification").html("&#xe9e5");
		$("#messageModal .message_content").html("绑定失败，请重试！");
	}
	//成功
	if ("1"==message) {
		$("#messageModal").modal("show");
		$("#messageModal .notification").html("&#xe9e1");
		$("#messageModal .message_content").html("恭喜您，绑定成功！");
	}
	//登录超时，重新登录
	if ("2"==message) {
		let html="<div>绑定失败！</div><div>原因：登录超时，请重新登录。</div>";
		$("#messageModal").modal("show");
		$("#messageModal .notification").html("&#xea08");
		$("#messageModal .message_content").html(html);
	}
	//绑定失败，三方账号已绑定
	if ("3"==message) {
		let html="<div>绑定失败，请先解绑！</div><div>原因：该合作账号已绑定其他爱上学习网账号。</div>";
		$("#messageModal").modal("show");
		$("#messageModal .notification").html("&#xe9e5");
		$("#messageModal .message_content").html(html);
	}
	
	let $body=$('body');
	let $wrongMessage=$('.wrong_message');
	let $background=$('.background');
	let $sendEmailClass=$('.sendEmail');
	//编辑面板:默认关闭,点击编辑展示

	$(".infoRight").on("click",function(event){
		$(event.target).parent().parent().find(".stripHide").show();
		//显示遮罩层
		$background.show();
	});

//	忘记密码
	$(".jump").on("click",function(){
		parent.location.href=Url+"/pagers/login/passwordback.jsp"
	});

//	修改密码:点击确认,验证数据并提交
	$("#password_validate").on("click",".updatePassword",function(event){
		if($("#password_validate").valid()){
			$.post("PersonalInfo/updatePassword.action",{oldPassword:$("#passwordNow").val(),newPassword:$("#passwordNew").val()},function(data){
				if(data==1){
					alert("密码修改成功,请重新登录");
					window.parent.location.href="pagers/login/log_in.jsp";  
					$(".form-control").val("");
					$(event.target).parents(".stripHide").hide();
				}else if(data==2){
					let passwordNowInput=$('#passwordNow');
					passwordNowInput.addClass('error');
					passwordNowInput.after('<label for="passwordNow" generated="true" class="error passwordIncorrect">密码不正确</label>');

				}else{
					alert("网络异常,密码修改失败,请刷新重试");
				}
			});
		}
	});
	//由于自己加了个密码不正确的提示,在input获得焦点后,要清除
	$body.on('focusin', '#passwordNow', function() {
		$('.passwordIncorrect').remove();
	});

//	修改密码校验
	let passwordValidate=$("#password_validate").validate({
		 onfocusout: function(element){
			 $(element).valid();
		 },
		 rules: {  
		    passwordNow: {  
		    	required: true,
                rangelength:[6,16]
            }, passwordNew:{
            	required:true,
               	rangelength:[6,16]
            },passwordValidate:{
               	required:true,
               	rangelength:[6,16],
               	isCodeMatched:true,
            },},
         messages: {  
        	 passwordNow: {
        		 required:"原密码不能为空",
        		 rangelength:"密码长度6—16位"
       		 },passwordNew:{
       			 required:"新密码不能为空",
       			 rangelength:"密码长度6—16位"
       		 },passwordValidate:{
       			 required:"请再次输入密码",
       			 rangelength:"密码长度6—16位"
       		 }  
         }
	});
	
//	对修改手机号进行验证
	let phoneValidate=$("#phone_validate").validate({
		onfocusout: function(element){
			 $(element).valid();
		 },
		rules: {  
		    passwordPhone: {  
		    	required: true,
		    	rangelength:[6,16],
		    	remote:{ //验证密码是否正确
		    		url:'personalInfo/validatePassword.action',
		    		type:'post',
		    		dataType:'json',
		    		data:{
		    			password:function(){
		    				return $('#passwordPhone').val();
		    			}
		    		}
		    	}
            }, 
            newPhone:{
               	required:true,
               	isMobile:true,
               	remote: {//验证手机号是否已经存在
               		url: "register/telIsExist.do",     //后台处理程序
               		type: "post",               //数据发送方式
               		dataType: "json",           //接受数据格式   
               		data: {                     //要传递的数据
               		    tel: function() {
               		        return $("#newPhone").val();
               		    }
               		}
               	}
            },
             
        },
         messages: {
        	 passwordPhone: {
        		 required:"原密码不能为空",
        		 rangelength:"密码长度6—16位",
        		 remote:"密码错误"
       		 },
       		 newPhone:{
       			required:"请填写新手机号",
       			isMobile : "手机号格式错误",
       			remote : "该手机号已经被注册",
       		 },
 
         }
	});
	
	let verificationCode=$("#phone_verification_code").validate({
		onfocusout: function(element){
			 $(element).valid();
		},
		rules: {
			validatePhone:{
            	required:true,
            	minlength:6
            },
		},
		messages:{
       		validatePhone:{
       			required:"请填写验证码",
       			minlength:"验证码长度错误"
       		} 
		}
	});


//	发送验证码  
//	状态变成全局变量
	// $("#drag").parents("tr").hide();
	var statuPhone=0;
	$body.off('click',"#sendSMS").on("click","#sendSMS",function(){
		if($('#phone_validate').valid()){
			let $countBtn=$('.countSeconds');
			$countBtn.css('cursor', 'progress');
			$.post("SMSVerificationCode/sendSMSVerificationCode.do",{tel:$("#newPhone").val()},function(data){
				if(data == 1){
					$countBtn.css('cursor', 'not-allowed');
					let count=60;
					countTime(count);
					return;
				}
				if(data == 0){
					alert("网络异常,请刷新后重试");
					return;
				}
				if(data == 2){
					$wrongMessage.text('验证码发送次数超出限制,24小时内不能再发送')
				}
			});
		}
	});
	$('#newPhone').focusin(function() {
		$wrongMessage.text('')
	});
	/*
	*	方法:发送验证码后,倒计时的方法
	*/
	function countTime(count){
		let $countSeconds=$('.countSeconds');
		$countSeconds.removeAttr('id');
		if (count>0) {
			$countSeconds.text(count);
			count--;
			setTimeout(function(){
				countTime(count);
			},1000);
		}else{
			$countSeconds.attr('id', 'sendSMS');
			$countSeconds.text('发送验证码');
		}
		
	}


	$body.off("click",".updatePhone").on("click",".updatePhone",function(event){
//			提交信息String password, String newPhone, String telCode
		if($('#phone_validate').valid()&$("#phone_verification_code").valid()){

			let $passwordPhone=$('#passwordPhone');
			let $newPhone=$("#newPhone");
			$.post("PersonalInfo/updatePhone.action",
				{
					password:$passwordPhone.val() ,
					newPhone:$newPhone.val() ,
					telCode:$("#validatePhone").val()
				},
				function(data){
					let $validatePhone=$('#validatePhone');
				if(data==1){
					let number=hidePhoneNumber($newPhone.val());
					$(event.target).parents(".stripHide").find("input").val("");
					$(event.target).parents(".stripHide").hide();
					$background.hide();				
					$("#phone").html(number);

				}else if(data==3){
					$validatePhone.addClass('error');
					$validatePhone.after('<label for="validatePhone" generated="true" class="error incorrect" style="">验证码错误或失效</label>');
				}else if(data==2){
					$passwordPhone.addClass('error');
					error.after('<label for="passwordPhone" generated="true" class="error incorrect">密码错误</label>')
				}else{
					alert("网络错误,请刷新后重试");
				}
			});
		}
	});

	//input获得焦点后,删除错误提示文本
	$body.off('focusin', '#validatePhone,#passwordPhone').on('focusin', '#validatePhone,#passwordPhone', function(event) {
		let $Incorrect=$(this).parent().find('.incorrect');
		if ($Incorrect.length>0) {
			$Incorrect.remove();
		}
	});

var statuEmail="";
//邮箱验证
  	let emailValidate=$("#email_validate").validate({
		onfocusout: function(element){
			$(element).valid();
		},
	    rules: {  
	   		passwordEmail: {  
             	required: true,
              	rangelength:[6,16]
            },
            newEmail:{
          		required:true,
          		email:true
          	},
      	},
     	messages: {  
    		passwordEmail: {
     		 	required:"密码不能为空",
     		 	rangelength:"密码长度6—16位"
    		},
    		newEmail:{
    			required:"邮箱不能为空",
    			email:"请填写正确的邮箱格式"
    		},
      	}
	});
  	let emailCode=$("#email_verification_code").validate({
  		onfocusout: function(element){
			$(element).valid();
		},
		rules:{
			validateEmail:{
          		required:true,
          		minlength:6
          	}
		},
		messages:{
			validateEmail:{
    			required:"验证码不能为空",
    			minlength:"验证码长度错误",
    		},
		}
  	});

//  发送邮箱验证码
$body.off("click","#sendEmail").on("click","#sendEmail",function(){

	if($("#email_validate").valid()){
		let $passwordEmail=$('#passwordEmail');
		$sendEmailClass.css('cursor', 'progress');
		$.post("personalInfo/emailValidate.action",{
			newEmail:$("#newEmail").val(),
			password:$("#passwordEmail").val()
		},function(data){
			if(data==1){
				statuEmail=data;
				$sendEmailClass.removeAttr('id').addClass('disabled');
				$sendEmailClass.css('cursor', 'not-allowed');
				setTimeEmail();

			}else if(data==3){
				$passwordEmail.after('<label for="passwordEmail" generated="true" class="error" style="">密码错误</label>')
				.addClass('error');
			}else{
				alert("网络异常,请刷新后重试");
			}
		})
	  }
   });

//  点击提交邮箱信息
$body.on("click",".updateEmail",function(event){
	if($("#email_validate").valid()&&$("#email_verification_code").valid()){
		let $newEmail=$('#newEmail');
		let $validateEmail=$('#validateEmail');

		$.post("personalInfo/updateEmail.action",{
				code:$("#validateEmail").val(),
				newEmail:$("#newEmail").val()
			},function(data){
				if(data==1){
					let $email=$("#email");
					let mail=$("#newEmail").val();
					$('.bind_mail').text('修改');
					$email.html(hideMail(mail));
					$(event.target).parents(".stripHide").find("input").val("");
					$(event.target).parents(".stripHide").hide();
					$background.hide();

					//改变“未绑定的样式”
					$email.removeClass();
				}else if(data==2){
					$validateEmail.after('<label for="validateEmail" generated="true" class="error">验证码错误</label>')
					.addClass('error');
				}else if(data==3){
					$validateEmail.after('<label for="validateEmail" generated="true" class="error">验证码超时失效</label>')
					.addClass('error');
				}else if(data==4){
					$newEmail.after('<label for="newEmail" generated="true" class="error">邮箱错误</label>')
					.addClass('error');
				}else{
					alert("网络异常");
				}
		});
	}
 });
	/*
	* @方法 处理邮箱,加星号隐藏
	* @param 待处理的邮箱
	*/
	function hideMail(mail){
		let end=mail.indexOf("@");
		let mailNumber=mail.substring(0,end);
		let star='*';
		let starStr=star.repeat(mailNumber.length-3);
		let result=mail.replace(mail.substring(2,end-1),starStr);
		return result;
	}
 
	let countDownEmail=60; 
    function setTimeEmail() {
    	if (countDownEmail>0) {
    		$sendEmailClass.html("已发送("+ countDownEmail+"s)" ); 
		    countDownEmail--;
		    setTimeout(function() { 
		    	setTimeEmail();
		    },1000);
    	} else {
    		countDownEmail=60;
    		$sendEmailClass.attr('id', 'sendEmail').removeClass('disabled').html("重新发送");
    	}
    }

//	第三方绑定
    let $third_container=$('.third_container');
    $third_container.off("click","#bindWeiXin").on("click","#bindWeiXin",function(){
	   	var unbindtime=$.cookie("wechatunbind");
	   	if (typeof (unbindtime)=="undefined") {
	   		unbindtime=0;
		}
	   	var nowtime = Date.parse(new Date())/1000;
	   	if(Math.abs(nowtime-unbindtime)>600){
	    	window.parent.location.href="https://open.weixin.qq.com/connect/qrconnect?appid=wxe60b803281a8a13a&response_type=code&scope=snsapi_login&redirect_uri="+Url+"/thirdlogin/wechat.do&state=bind";
	   	}else{
	   		alert("操作频繁，请稍后尝试！");
	   	}
    })
   $third_container.off("click","#bindZhiFuBao").on("click","#bindZhiFuBao",function(){
    	var unbindtime=$.cookie("alipayunbind");
    	if (typeof (unbindtime)=="undefined") {
	   		unbindtime=0;
		}
	   	var nowtime = Date.parse(new Date())/1000;
	   	if(Math.abs(nowtime-unbindtime)>600){
	    	window.parent.location.href="https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=2018071160521829&scope=auth_user&redirect_uri="+Url+"/thirdlogin/alipay.do&state=bind";
	   	}else{
	   		alert("操作频繁，请稍后尝试！");
	   	}
    });

//    请求绑定信息
    $.get("PersonalInfo/queryState.action",function(data){
    	if(data.wx!=null){
    		$('.wechat .second_column').first().removeClass('haveNoDate').html("已绑定");
    		$('.third_btn').first().text('取消绑定').attr('id','unbindWeixin');
    	}
    	if(data.zhifubao!=null){
    		$('.alipay .second_column').first().removeClass('haveNoDate').html("已绑定");
    		$('.third_btn').last().text('取消绑定').attr('id','unbindZhifubao');
    	}
    });

//    解绑微信
    $("body").on("click","#unbindWeixin",function(event){
    	var nowtime = Date.parse(new Date())/1000;
    	var confirm=window.confirm("解绑后，十分钟内不能再次绑定");
		 if(confirm){
			 $.post("PersonalInfo/delThridId.action",{state:"wx"},function(data){
		    	if(data==1){
		   			hidePanel(1,"微信解绑成功");
		   			$(event.target).text('点击绑定').removeAttr('id').attr('id', 'bindWeiXin');
		   			$('.wechat_info').addClass('haveNoDate').html('<img class="tip_img" src="img/personal/icon/tip.png">未绑定')
		   			$.cookie("wechatunbind", nowtime,{expires: 365,path:"/"});
		   		 }
		   	 });
		 }
    	
    });
//    解绑支付宝
     $("body").on("click","#unbindZhifubao",function(event){
    	 var nowtime = Date.parse(new Date())/1000;
		 var confirm=window.confirm("解绑后，十分钟内不能再次绑定");
		 if(confirm){
	    	 $.post("PersonalInfo/delThridId.action",{state:"zfb"},function(data){
	    		if(data==1){
	    			hidePanel(1,"支付宝解绑成功");
	    			$(event.target).text('点击绑定').removeAttr('id').attr('id', 'bindZhiFuBao');
	    			$('.alipay_info').addClass('haveNoDate').html('<img class="tip_img" src="img/personal/icon/tip.png">未绑定')
	    			$.cookie("alipayunbind", nowtime,{expires: 365,path:"/"});
	    		}
	    	 });
		 }
    })

	//关闭文本框
    $(".clear").on("click",function(){
    	let $window=$(this).parents(".stripHide");
    	let validatorName=$window.find('form').first().attr('id');
    	$window.hide();
    	$window.find('input').val('');
    	//关闭遮罩层
    	$background.hide();

    	//重置验证
		if (validatorName.search('password')!=-1) {
    		passwordValidate.resetForm();
    	} else if(validatorName.search('email')!=-1){
    		emailValidate.resetForm();
    		emailCode.resetForm();
    	}else{
    		phoneValidate.resetForm();
    		verificationCode.resetForm();
    		$wrongMessage.text('');
    	}
    	
    	
    });

//    面板方法
    function hidePanel(time,addText,position){
    	$("#hidebody").show();
    	setTime();
    	function setTime(val) {
    	    if (time == 0) {
    	    	$("#hidebody").hide();
    	    	return;
    	    } else { 
    	    	$("#hideContext").html(addText); 
    	    	time--; 
    	    } 
    	    setTimeout(function() { 
    	    	setTime(val);
    	    },1200)
    	}
    }
    
    
    
	
//	基本信息
	var savaShowInformation=$("#savaShowInformation");
	var editInformation=$("#editInformation");
	var editShowInformation=$("#editShowInformation")
	var showInformation=$("#showInformation");
	editInformation.hide();
	savaShowInformation.hide();
//	密码框内容清除
	$(".password").val("")
	$("#returnShowInformation").hide();
	editShowInformation.on("click",function(){
		showInformation.hide();
		editShowInformation.hide();
		savaShowInformation.show();
		editInformation.show();
		$("#returnShowInformation").show();
		uploader.refresh();
	})
	
	var yearBirth=$("#yearBirth");
	var monthBirth=$("#monthBirth");
	var dayBirth=$("#dayBirth");
//	 //父级刷新
	var editName=$("#editName");
	var editlogo=$("#editlogo");
//	保存值 保证修改
	var name="";
	var sex="";
	var oldYear="" ;
	var oldMonth="" ;
	var oldDay="" ;
	var address="";
	var photo="";
	var srcImg="";
	$.get("PersonalInfo/getPersonalInfo.action",function(data){
		if(data!=null){
			name=data.name;
			$("#name").text(data.name);
			$("#birth").text(data.birthday);
			editName.val(data.name);
			if(data.photo!=null){
				photo=data.photo;
				editlogo.attr("src",data.photo)
			}
			if(data.sex==false){
				$("#sex").text("女")
				sex=0;
				 $("#radio2").attr("checked","checked"); 
			}else if(data.sex==true){
				sex=1;
				$("#sex").text("男")
				 $("#radio1").attr("checked","checked"); 
			}
			if(data.areas_id!=0){
				$("#address").text(data.listAddress[0].name+" "+data.listAddress[1].name+" "+data.listAddress[2].name)
				address=data.areas_id;
				var allListAddress=data.allListAddress;
				var province="";
				var city="";
				var county="";
				for(var i=0;i<allListAddress.length;i++){
					if(allListAddress[i].LevelType==1){
						province=province+"<option value='"+allListAddress[i].id+"'>"+allListAddress[i].name+"</option>";
					}else if(allListAddress[i].LevelType==2){
						city=city+"<option value='"+allListAddress[i].id+"'>"+allListAddress[i].name+"</option>";
					}else{
						county=county+"<option value='"+allListAddress[i].id+"'>"+allListAddress[i].name+"</option>";
					}
				}
				 	$("#provinceAddress").append(province)
					 $("#cityAddress").append(city);
					 $("#countyAddress").append(county);
					 $("#provinceAddress").val(data.listAddress[0].id);
					 $("#cityAddress").val(data.listAddress[1].id);
					 $("#countyAddress").val(data.listAddress[2].id)
					 
			}else{
				listAddress(100000,$("#provinceAddress"))
			}
			if(data.birthday!=0){
				oldYear=data.year;
				oldMonth=data.month;
				oldDay=data.day;
				new YMDselect('year1','month1','day1',data.year,data.month,data.day);
			}else{
				new YMDselect('year1','month1','day1');
			}
			let number=hidePhoneNumber(data.tel+'');
			$("#phone").html(number);

			//如果邮箱是空,显示未绑定
			let $email=$("#email");
			let $bindMail=$('.bind_mail');
			if(data.email==""||data.email==null){
				$bindMail.text('绑定');
				$email.addClass('haveNoDate');
				$email.html('<img class="tip_img" src="img/personal/icon/tip.png">未绑定');
			}else{
				$bindMail.text('修改');
				$email.html(hideMail(data.email));
			}
		}else{
			window.location.href='pagers/homepage.jsp';
		}
	});


//	取消按钮
	$("#returnShowInformation").on("click",function(){
		showInformation.show();
		editShowInformation.show();
		savaShowInformation.hide();
		editInformation.hide();
		$("#returnShowInformation").hide()
	})
//	提交信息
	savaShowInformation.on("click",function(){
		if(editName.val()!=""){
			if(editName.val().length<25){
				filterWorf()
			}else{
				alert("昵称不要超过25个字符哦")
			}
		}else{
			alert("昵称不能为空")
		}
	});

/*@方法 :将手机号隐藏,中间加**   */
	function hidePhoneNumber(phoneNumber){
		let between='****';
		let head=phoneNumber.substring(0,3);
		let foot=phoneNumber.substring(7);
		return head+between+foot;
	}


//	验证是否符合规范
	function filterWorf(){
		var array=new Array();
		var object={
				name:"",
				text:""
		}
		var returnV=true;
		var inputLength=$(".filter_worf").length;
		for(var i=0;i<inputLength;i++){
			object.name=$(".filter_worf").eq(i).attr("id");
			object.text=$(".filter_worf").eq(i).val()
			array.push(object);
		}
		 var word=JSON.stringify(array);
		 $.post("filterWord/isFilterWord.do",{context:word},function(data){
			 $(".filter_word").empty();
			 for(var i=0;i<data.length;i++){
				 if(data[i].text!=""){
					 $("#"+data[i].name).after("<span class='filter_words'>"+data[i].text+"</span>")
				 }else{
					 savePost();//保存信息
				 }
			 }
		 });
	}
	//提交保存信息
	function savePost(){
		var newPhoto=editlogo.attr("src");
		$("#returnShowInformation").hide();
		var newSex=$('input[name="radio"]:checked ').val();
//		表示修改了
		if(newPhoto!=photo || name!=editName.val() || sex!=newSex || oldYear!=yearBirth.val() || oldMonth!=monthBirth || oldDay!=dayBirth || address!=$("#countyAddress").val()){
//			表示修改图片
			if(newPhoto!=photo&&newPhoto!="img/personal/icon/default.jpg"){
				uploader.option('formData',{
					userName:editName.val() ,
					sex:newSex,
					year:yearBirth.val(),
					month:monthBirth.val(),
					day:dayBirth.val(),
					address:$("#countyAddress").val()
				});
				uploader.upload(); 
			}else{//修改表单内容
				$.post("PersonalInfo/updateUserInfo.action",{
					userName:editName.val() ,
					sex:newSex,
					year:yearBirth.val(),
					month:monthBirth.val(),
					day:dayBirth.val(),
					address:$("#countyAddress").val()
				},function(data){
					$("#name").text(editName.val());
					var newSex=$('input[name="radio"]:checked ').val();
					if(newSex==1){
						$("#sex").html("男")
					}else if(newSex==0){
						$("#sex").html("女")
					}
					if(dayBirth.val()!=0){
						$("#birth").text(yearBirth.val()+"-"+monthBirth.val()+"-"+dayBirth.val())
					}else{
						$("#birth").text("")
					}
					if($("#countyAddress").val()!=0){
						$("#address").html($("#provinceAddress  option:selected").text()+" "+$("#cityAddress  option:selected").text()+" "+$("#countyAddress  option:selected").text())
					}else{
						$("#address").html("")
					}
					
					//显示
					showInformation.show();
					editShowInformation.show();
					savaShowInformation.hide();
					editInformation.hide()
				})
			}
			//修改父页面-顶部的用户名
			window.parent.document.getElementById('topmenu_0').innerHTML=editName.val();
		}else{
			alert("请填入全部信息")
		}
	
	}
	
//	更换图片  
		var uploader = WebUploader.create({
			    // swf文件路径
			    swf: 'js/webUploader/Uploader.swf',
			    method :'POST',
			    // 文件接收服务端。
			    server: 'PersonalInfo/updateUserInfoFile.action',
			    // 选择文件的按钮。可选。
			    pick: '#editLogoImg',
			    // 只允许选择jpg文件。
			    accept: {
			    	title: 'Images',
			        extensions: 'jpg',
			        mimeTypes: 'image/*'
			    },
			    fileNumLimit:1,
			});
		 uploader.on("beforeFileQueued",function( file ) {
			 uploader.reset();
		 });
			uploader.on( 'fileQueued', function( file ) {
				 uploader.makeThumb( file, function( error, src ) {
				        if ( error ) {
				            $img.replaceWith('<span>不能预览</span>');
				            return;
				        }
				        srcImg=src;
				        $("#editlogo").attr( 'src', src );
				    }, '170', '170' );
			});
			// 文件上传过程中创建进度条实时显示。
			uploader.on( 'uploadProgress', function( file, percentage ) {
			});
//			某个文件开始上传前触发，一个文件只会触发一次。
			uploader.on( 'uploadStart', function( file) {
			});
			// 文件上传成功，给item添加成功class, 用样式标记上传成功。
			uploader.on( 'uploadSuccess', function( file ,response ) {
				if(response==1){
					//成功之后,更新父页面顶部的用户名和头像
					window.parent.document.getElementById('topmenu_0').innerHTML=editName.val();
					$("body",parent.document).find('#personal_information img').attr("src",srcImg);
					
					//修改内容
					photo=srcImg
					$("#name").text(editName.val());
					var newSex=$('input[name="radio"]:checked ').val();
					if(newSex==1){
						$("#sex").html("男")
					}else if(newSex==0){
						$("#sex").html("女")
					}
					if(dayBirth.val()!=0){
						$("#birth").text(yearBirth.val()+"-"+monthBirth.val()+"-"+dayBirth.val())
					}else{
						$("#birth").text("")
					}
					if($("#countyAddress").val()!=0){
						$("#address").html($("#provinceAddress  option:selected").text()+" "+$("#cityAddress  option:selected").text()+" "+$("#countyAddress  option:selected").text())
					}else{
						$("#address").html("")
					}
				}else{
					alert("保存出现问题,请刷新后重试");
				}
				//隐藏保存按钮,显示编辑按钮
				showInformation.show();
				editShowInformation.show();
				savaShowInformation.hide();
				editInformation.hide();
			});

	
//	请求省市县 地区
	var choose="<option value='0'>请选择</option>";

	 $("#provinceAddress").change(function() {
		 $("#cityAddress").empty();
		 $("#countyAddress").empty();
		 $("#cityAddress").append(choose);
		 $("#countyAddress").append(choose);
		 listAddress($("#provinceAddress").val(),$("#cityAddress"));
		});
	 $("#cityAddress").change(function() {
		 $("#countyAddress").empty();
		 $("#countyAddress").append(choose);
		 listAddress($("#cityAddress").val(),$("#countyAddress"));
		});
//	 listAddress 请求地址
	 function  listAddress (parenId,positioning){
		 $.post("OrgEnroll/listAddress.do",{parentId:parenId},function(data){
			var city="";
			 for(var i=0;i<data.length;i++){
				 city=city+"<option value='"+data[i].id+"'>"+data[i].name+"</option>"; 	 
			 }
			 positioning.append(city)
		 });
	 }
});


jQuery.validator.addMethod("isMobile", function(value, element) {  
	 var length = value.length;
	 var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;  
	 return this.optional(element) || (length == 11 && mobile.test(value));  
	}, "请正确填写手机号码");

jQuery.validator.addMethod('isCodeMatched',function(value, element, param){
		if (value.length!=0) {
			let newPs=$('#passwordNew').val();
			return newPs==value;
		}
	},'密码不一致'
);
