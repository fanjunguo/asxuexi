// 初始化webuploader
function initializeUploader(frontUploaderId,backUploaderId,personUploaderId){
	let fileMap={frontFile:null,backFile:null,personFile:null};
	let uploader = WebUploader.create({
		auto:false,
	    swf: "js/webUploader/Uploader.swf",
	    method :"POST",
	    server: "asxuexi/org/uploadIdentityPhoto.action",
	    accept: {
	    	title: "Images",
	        extensions: "jpg,png",
	    },
	    fileNumLimit: 3,
        fileSingleSizeLimit: 2 * 1024 * 1024
	});
	uploader.addButton({
    	id:frontUploaderId,
	    multiple:false,
	    innerHTML:"<strong>点击更换图片</strong><br>大小不超过2MB，格式仅限jpg或png"
    })
    uploader.addButton({
    	id:backUploaderId,
	    multiple:false,
	    innerHTML:"<strong>点击更换图片</strong><br>大小不超过2MB，格式仅限jpg或png"
    })
    uploader.addButton({
    	id:personUploaderId,
	    multiple:false,
	    innerHTML:"<strong>点击更换图片</strong><br>大小不超过2MB，格式仅限jpg或png"
    })
    uploader.on("beforeFileQueued",function(file){
    	if ($(frontUploaderId).hasClass("selected")) {
    		if (fileMap.frontFile!=null) {
    			uploader.removeFile( fileMap.frontFile, true );
			}
    		$(frontUploaderId).removeClass("changed");
		}
    	if ($(backUploaderId).hasClass("selected")) {
    		if (fileMap.backFile!=null) {
    			uploader.removeFile( fileMap.backFile, true );
			}
    		$(backUploaderId).removeClass("changed");
		}
    	if ($(personUploaderId).hasClass("selected")) {
    		if (fileMap.personFile!=null) {
    			uploader.removeFile( fileMap.personFile, true );
			}
    		$(personUploaderId).removeClass("changed");
		}
    })
    uploader.on("fileQueued",function(file){
    	if ($(frontUploaderId).hasClass("selected")) {
    		file.name="front_photo.png";
    		fileMap.frontFile=file;
    		uploader.makeThumb( file, function( error, src ) {
                if ( error ) {
                	$(frontUploaderId).prev("img").replaceWith('<div>不能预览</div>');
                    return;
                }
                $(frontUploaderId).prev("img").attr( "src", src );
            }, 250, 160 );
        	$(frontUploaderId).addClass("changed");
        	$(frontUploaderId).trigger("fileChangedEvent");
        	$(frontUploaderId).nextAll(".upload_message").html("");
    	}
    	if ($(backUploaderId).hasClass("selected")) {
    		file.name="back_photo.png";
    		fileMap.backFile=file;
    		uploader.makeThumb( file, function( error, src ) {
                if ( error ) {
                	$(backUploaderId).prev("img").replaceWith('<div>不能预览</div>');
                    return;
                }
                $(backUploaderId).prev("img").attr( "src", src );
            }, 250, 160 );
        	$(backUploaderId).addClass("changed");
        	$(backUploaderId).trigger("fileChangedEvent");
        	$(backUploaderId).nextAll(".upload_message").html("");
    	}
    	if ($(personUploaderId).hasClass("selected")) {
    		file.name="person_photo.png";
    		fileMap.personFile=file;
    		uploader.makeThumb( file, function( error, src ) {
                if ( error ) {
                	$(personUploaderId).prev("img").replaceWith('<div>不能预览</div>');
                    return;
                }
                $(personUploaderId).prev("img").attr( "src", src );
            }, 250, 160 );
        	$(personUploaderId).addClass("changed");
        	$(personUploaderId).trigger("fileChangedEvent");
        	$(personUploaderId).nextAll(".upload_message").html("");
    	}
    	
    })
    
    return uploader;
}
// 验证下拉菜单的选中项的value是否不为空
function validateSelect(selectIdArray){
	let result=true;
	for (let i = 0; i < selectIdArray.length; i++) {
		result = result && ($(selectIdArray[i]+" option:selected").val()!="");
	}
	return result;
}
function setStatusAndButtons(data){
	// 设定认证状态，和对应的操作按钮
	let statusHtml='',buttonsHtml='',statementHtml='';
	if($.isEmptyObject(data)){
		// 查不到记录，未认证状态，显示认证按钮
		statusHtml=
			'<span>状态：</span>\
			<div class="not_authenticated"></div>\
			<span class="not_authenticated">未认证</span>';
		buttonsHtml=
			'<button type="button" class="btn btn-info btn_todo">进行认证</button>';
	}
	if (-1==data.status) {
		// 状态码为-1，未通过审核状态，显示修改按钮，显示未通过原因。
		statusHtml=
			'<span>状态：</span>\
			<div class="not_passed"></div>\
			<span class="not_passed">未通过审核</span>';
		buttonsHtml=
			'<button type="button" class="btn btn-primary btn_update">修改信息</button>';
		statementHtml=
			'<span>原因：</span><span class="text-muted">';
		if (-1==data.front_status) {
			statementHtml +='身份证正面信息不合格。';
		}
		if (-1==data.back_status) {
			statementHtml +='身份证反面信息不合格。';
		}
		if (-1==data.person_photo_status) {
			statementHtml +='手持身份证照片不合格。';
		}
		statementHtml +='(详细原因请查看系统消息)</span>';
	}
	if (0==data.status) {
		// 状态码为0，未审核状态，显示查看和修改按钮。
		statusHtml=
			'<span>状态：</span>\
			<div class="to_validate"></div>\
			<span class="to_validate">待审核</span>';
		buttonsHtml=
			'<button type="button" class="btn btn-primary btn_check">查看信息</button>\
			<button type="button" class="btn btn-primary btn_update">修改信息</button>';
		let now = new Date().getTime();
		if (now-data.gmt_create>=600000) {
			// 判断请求时间和记录的创建时间，大于10mins，将状态显示为审核中，显示查看按钮
			statusHtml=
			'<span>状态：</span>\
			<div class="validating"></div>\
			<span class="validating">审核中</span>';
			buttonsHtml=
			'<button type="button" class="btn btn-primary btn_check">查看信息</button>';
		}else{
			// 小于10mins，设置定时任务，将状态显示为审核中并显示查看按钮
			setTimeout(function(){
				statusHtml=
					'<span>状态：</span>\
					<div class="validating"></div>\
					<span class="validating">审核中</span>';
				buttonsHtml=
					'<button type="button" class="btn btn-primary btn_check">查看信息</button>';
				$("#identity_box .authentication_status").html(statusHtml);
				$("#identity_box .authentication_buttons").html(buttonsHtml);
			}, 600000-now+data.gmt_create);
		}
	}
	if (1==data.status) {
		// 状态码为1，通过审核状态，显示查看和修改按钮。
		statusHtml=
			'<span>状态：</span>\
			<div class="passed"></div>\
			<span class="passed">通过审核</span>';
		buttonsHtml=
			'<button type="button" class="btn btn-primary btn_check">查看信息</button>\
			<button type="button" class="btn btn-primary btn_update">修改信息</button>';
	}
	if (2==data.status) {
		// 状态码为2，审核进行中状态，显示查看按钮。
		statusHtml=
			'<span>状态：</span>\
			<div class="validating"></div>\
			<span class="validating">审核中</span>';
		buttonsHtml=
			'<button type="button" class="btn btn-primary btn_check">查看信息</button>';
	}
	$("#identity_box .authentication_status").html(statusHtml);
	$("#identity_box .authentication_buttons").html(buttonsHtml);
	$("#identity_box .authentication_statement").html(statementHtml);
}
//将毫秒时间戳转化为日期字符串(yyyy-mm-dd)
function formatDateString(timestamp){
	let date=new Date(timestamp);
	let dateStr=date.getFullYear()+"-"+(date.getMonth()+1 < 10 ? "0"+(date.getMonth()+1) : date.getMonth()+1)
			+"-"+(date.getDate()<10?"0"+date.getDate():date.getDate());
	return dateStr;
}
// 复制对象，主要处理是：将时间戳转化为日期字符串
function copyObject(source,target){
	target=source;
	let reg=/^\d{13}$/;
	for ( var key in target) {
		// 如果是十三位时间戳，将其转化为日期字符串
		if (key.includes("date")) {
			if(reg.test(target[key])){
				target[key]=formatDateString(target[key]);
			}
		}
		
	}
	return target;
}

function addLoadingAnimation(){
	let html=
		'<div class="modal_cover">\
			<div class="loading">\
				<h4>正在提交</h4>\
				<div class="loading-dot loading-dot-1"></div>\
				<div class="loading-dot loading-dot-2"></div>\
				<div class="loading-dot loading-dot-3"></div>\
				<div class="loading-dot loading-dot-4"></div>\
				<div class="loading-dot loading-dot-5"></div>\
				<div class="loading-dot loading-dot-6"></div>\
				<div class="loading-dot loading-dot-7"></div>\
				<div class="loading-dot loading-dot-8"></div>\
			</div>\
		</div>';
	$("#authenticationModal .modal-content").append(html);
}

$(document).ready(function(){
	var orgData;
	$.get("asxuexi/org/getIdentityAuthenticationStatus.action", function(data){
		orgData=copyObject(data,orgData);
		setStatusAndButtons(orgData);
	});
	// 定义验证框架的验证规则
	var frontValidateOptions={
			onfocusout: function(element){
				$(element).valid();
			},
			rules: {
				name_input:{
					required: true,
					minlength: 2,
					isChinesecharacter: true
				},
				id_number_input:{
					required: true,
					isChineseIDNumber:true,
				},
			},
			messages:{
				name_input:{
					required: "请填写姓名",
					minlength: "请核对姓名",
				},
				id_number_input:{
					required: "请填写身份证号",
				},
			}
	};
	// 点击身份证认证的 [进行认证]按钮
	$("#identity_box").on("click",".btn_todo",function(){
		let modalHtml=
			'<div class="front_info">\
				<div class="front_title text-muted">身份证正面</div>\
				<div class="front_photo">\
					<img src="img/org/authentication/front_photo.png">\
					<div id="front_uploader"></div>\
					<div class="upload_message"></div>\
				</div>\
				<div class="front_content">\
					<form class="front_validate">\
						<div><label>姓名</label><input type="text" id="name_input" name="name_input"\
							placeholder="请输入姓名" autocomplete="off" maxlength="10"></div>\
						<div><label>身份证号</label><input type="text" id="id_number_input" name="id_number_input"\
							placeholder="请输入身份证号" autocomplete="off" maxlength="18"></div>\
					</form>\
				</div>\
			</div>\
			<div class="back_info">\
				<div class="back_title text-muted">身份证反面</div>\
				<div class="back_photo">\
					<img src="img/org/authentication/back_photo.png">\
					<div id="back_uploader"></div>	\
					<div class="upload_message"></div>\
				</div>\
				<div class="back_content">\
					<div>\
						<label>有效期至</label>\
						<select id="year" name="year"></select>-\
						<select id="month" name="month"></select>-\
						<select id="day" name="day"></select>\
					</div>\
				</div>\
			</div>\
			<div class="person_info">\
				<div class="person_title text-muted">手持身份证照片</div>\
				<div class="person_photo">\
					<img src="img/org/authentication/person_photo.png">\
					<div id="person_uploader"></div>\
					<div class="upload_message"></div>\
				</div>\
			</div>';
		let footerHtml=
			'<div class="modal-footer">\
				<button type="button" class="btn btn-primary">确认提交</button>\
            	<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>\
			</div>';
		$("#authenticationModal .modal-body").html(modalHtml);
		$("#authenticationModal .modal-footer").remove();
		$("#authenticationModal .modal-body").after(footerHtml);
		
		new YMDselect("year","month","day");
		uploader=initializeUploader("#front_uploader","#back_uploader","#person_uploader");
		$("#authenticationModal").modal("show");
		$(".front_validate").validate(frontValidateOptions);
		// 监听日期下拉菜单的改变事件，发生变化，去除错误提示
		$("#day").on("change",function(){
			if(validateSelect(["#year","#month","#day"])){
				$(".back_content div .error").remove();
			}
		})
		// 点击[确认提交]按钮
		$("#authenticationModal .modal-footer .btn:first-child").off("click").on("click",function(){
			// 清除下拉框原有提示信息
			$(".back_content div .error").remove();
			// 下拉框未选择，进行提示
			let selectValidated=validateSelect(["#year","#month","#day"]);
			if(!selectValidated){
				$(".back_content div").append("<span class='error'>请选择有效期</span>");
			};
			// 判断是否通过验证
			let passValidation=$(".front_validate").valid()&&selectValidated;
			// 判断图片是否更换
			if (!$("#front_uploader").hasClass("changed")) {
				$("#front_uploader").next(".upload_message").html("请上传身份证正面照");
			}
			if (!$("#back_uploader").hasClass("changed")) {
				$("#back_uploader").next(".upload_message").html("请上传身份证反面照");
			}
			if (!$("#person_uploader").hasClass("changed")) {
				$("#person_uploader").next(".upload_message").html("请上传手持身份证照片");
			}
			let isChanged=$("#front_uploader").hasClass("changed")&&$("#back_uploader").hasClass("changed")
					&&$("#person_uploader").hasClass("changed");
			// 判断是否准备就绪
			let isReady=passValidation&&isChanged;
			if (isReady) {
				// 上传文件
				uploader.upload();
				// 展示过渡动画
				addLoadingAnimation();
			}
			let successCounter=0;// 计数器，统计文件上传成功的次数
			uploader.on("uploadSuccess",function(file,response){
				if (response.flag) {
					++successCounter;
					if (successCounter==3) {
						
						// 第三次成功后，上传文字信息
						let name=$("#name_input").val();
						let idNumber=$("#id_number_input").val();
						let expiryDate=$("#year").val()+"-"+$("#month").val()+"-"+$("#day").val();
						$.post("asxuexi/org/insertIdentityInfo.action",
						{name:name,idNumber:idNumber,expiryDate:expiryDate},function(data){
							if (data.flag) {
								orgData=copyObject(data,orgData);
								setStatusAndButtons(orgData);
								setTimeout(function(){
									$("#authenticationModal .modal_cover .loading").html("<h4>提交成功</h4>");
									setTimeout(function(){
										$("#authenticationModal").modal("hide");
										$("#authenticationModal .modal_cover").remove();
									}, 1500);
								},1500);
							}else{
								setTimeout(function(){
									$("#authenticationModal .modal_cover .loading")
										.html('<h4>提交失败，请重新<br><br><button type="button" class="btn btn-info btn_todo">进行认证</button></h4>');
								},1500);
								$("#authenticationModal .modal_cover .loading").off("click").on("click",".btn_todo",function(){
									$("#identity_box .btn_todo").trigger("click");
									$("#authenticationModal .modal_cover").remove();
								})
							}
						});
					}
				}else{
					$("#authenticationModal .modal_cover").remove();
					let selector=response.fileName.substring(0, response.fileName.indexOf("_"));
					let uploadMessageHtml='图片保存失败<br>请重新选择图片后再次点击[确认提交]';
					$("."+selector+"_photo .upload_message").html(uploadMessageHtml);
				}
			})
		})
	})
	
	// 点击身份证认证的 [修改信息]按钮
	$("#identity_box").on("click",".btn_update",function(){
		let status=orgData.status;
		let front_status=orgData.front_status;// 身份证正面认证状态
		let back_status=orgData.back_status;// 身份证反面认证状态
		let person_photo_status=orgData.person_photo_status;// 手持身份证照片认证状态
		let modalHtml=
			'<div class="front_info">\
				<div class="front_title text-muted">身份证正面</div>\
				<div class="front_photo">\
					<img src="/asxuexi_resource/'+orgData.org_id+'/validate/front_photo.png">\
					<div id="front_uploader"></div>\
					<div class="upload_message"></div>\
				</div>\
				<div class="front_content">\
					<form class="front_validate">\
						<div><label>姓名</label><input type="text" id="name_input" name="name_input"\
							placeholder="请输入姓名" autocomplete="off" maxlength="10"\
							value='+orgData.name+'>\
						</div>\
						<div><label>身份证号</label><input type="text" id="id_number_input" name="id_number_input"\
							placeholder="请输入身份证号" autocomplete="off" maxlength="18"\
							value='+orgData.id_number+'>\
						</div>\
					</form>\
				</div>\
			</div>\
			<div class="back_info">\
				<div class="back_title text-muted">身份证反面</div>\
				<div class="back_photo">\
					<img src="/asxuexi_resource/'+orgData.org_id+'/validate/back_photo.png">\
					<div id="back_uploader"></div>	\
					<div class="upload_message"></div>\
				</div>\
				<div class="back_content">\
					<div>\
						<label>有效期至</label>\
						<select id="year" name="year"></select>-\
						<select id="month" name="month"></select>-\
						<select id="day" name="day"></select>\
					</div>\
				</div>\
			</div>\
			<div class="person_info">\
				<div class="person_title text-muted">手持身份证照片</div>\
				<div class="person_photo">\
					<img src="/asxuexi_resource/'+orgData.org_id+'/validate/person_photo.png">\
					<div id="person_uploader"></div>\
					<div class="upload_message"></div>\
				</div>\
			</div>';
		let footerHtml=
			'<div class="modal-footer">\
				<button type="button" class="btn btn-primary disabled">提交修改</button>\
	        	<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>\
			</div>';
		$("#authenticationModal .modal-body").html(modalHtml);
		$("#authenticationModal .modal-footer").remove();
		$("#authenticationModal .modal-body").after(footerHtml);
		if (-1==status) {
			if (-1==front_status) {
				$(".front_title").removeClass("text-muted").addClass("text-danger").html("*身份证正面");
			}
			if (-1==back_status) {
				$(".back_title").removeClass("text-muted").addClass("text-danger").html("*身份证反面");
			}
			if (-1==person_photo_status) {
				$(".person_title").removeClass("text-muted").addClass("text-danger").html("*手持身份证照片");
			}
			if (1==front_status) {
				$("#front_uploader+.upload_message").remove();
				$("#front_uploader").remove();
				let frontContentHtml=
					'<div><label>姓名</label><span>'+orgData.name+'</span></div>\
					<div><label>身份证号码</label><span>'+orgData.id_number+'</span></div>';
				$(".front_content").html(frontContentHtml);
			}
			if (1==back_status) {
				$("#back_uploader+.upload_message").remove();
				$("#back_uploader").remove();
				let backContentHtml=
					'<div><label>有效期至</label><span>'+orgData.expiry_date+'</span></div>';
				$(".back_content").html(backContentHtml);
			}
			if (1==person_photo_status) {
				$("#person_uploader").remove();
			}
		}
		// 初始化年月日下拉菜单，并选择相应年月日
		if ($("#year").length!=0) {
			let YMDArray=orgData.expiry_date.split("-");
			new YMDselect("year","month","day",YMDArray[0],YMDArray[1],YMDArray[2]);
		}
		// 初始化webuploader
		uploader=initializeUploader("#front_uploader","#back_uploader","#person_uploader");
		$("#authenticationModal").modal("show");
		if (1==status) {
			let modalCoverHtml=
				'<div class="modal_cover">\
					<div class="loading">\
						<h3>您已<strong class="text-success">通过审核</strong>!</h3>\
						<h4>修改信息后，爱上学习网将会重新审核您提交的信息。</h4>\
						<h4>请<strong>慎重考虑</strong>是否继续操作</h4><br><br>\
						<button type="button" class="btn btn-default btn_continue">确认继续</button>\
						<button type="button" class="btn btn-primary btn_cancel">取消操作</button>\
					</div>\
				</div>';
			$("#authenticationModal .modal-content").append(modalCoverHtml);
			$("#authenticationModal .modal_cover .btn").on("click",function(){
				$("#authenticationModal .modal_cover").remove();
				if ($(this).hasClass("btn_cancel")) {
					$("#authenticationModal").modal("hide");
				}
			})
		}
		
		// 为表单增加验证规则
		$(".front_validate").validate(frontValidateOptions);
		
		// 监听图片的改变事件，图片发生改变，将[提交修改]按钮变为可用
		$("#front_uploader,#back_uploader,#person_uploader").on("fileChangedEvent",function(){
			$("#authenticationModal .modal-footer .btn:first-child.disabled").removeClass("disabled");
		})
		// 监听姓名输入框和身份证号输入框的改变事件，发生改变，将[提交修改]按钮变为可用
		$("#name_input,#id_number_input").on("input propertychange",function(){
			$(this).addClass("changed");
			$("#authenticationModal .modal-footer .btn:first-child.disabled").removeClass("disabled");
		})
		// 监听年月日下拉框的改变事件，发生改变，将[提交修改]按钮变为可用
		$("#year,#month,#day").on("change",function(){
			$(this).addClass("changed");
			$("#authenticationModal .modal-footer .btn:first-child.disabled").removeClass("disabled");
		})
		// 点击可用的[提交修改]按钮
		$("#authenticationModal .modal-footer").off("click").on("click",".btn:first-child:not(.disabled)",function(){
			let name=$("#name_input").length==0?orgData.name:$("#name_input").val();
			let idNumber=$("#id_number_input").length==0?orgData.id_number:$("#id_number_input").val();
			let expiryDate=$("#year").length==0?orgData.expiry_date:$("#year").val()+"-"+$("#month").val()+"-"+$("#day").val();
			let postURL,postData;
			if (0==status) {
				postURL="asxuexi/org/updateIdentityInfo.action";
				postData={name:name,idNumber:idNumber,expiryDate:expiryDate,gmtKey:"gmt_modified"};
			}
			if (-1==status) {
				postURL="asxuexi/org/updateIdentityInfo.action";
				let updateKey="";
				if (-1==front_status) {
					updateKey += "front_";
				}
				if (-1==back_status) {
					updateKey += "back_";
				}
				if (-1==person_photo_status) {
					updateKey += "person_";
				}
				postData={name:name,idNumber:idNumber,expiryDate:expiryDate,gmtKey:"gmt_create",updateKey:updateKey};
			}
			if (1==status) {
				postURL="asxuexi/org/insertIdentityInfo.action";
				postData={name:name,idNumber:idNumber,expiryDate:expiryDate};

			}
			let changedFileNumber=$(".webuploader-container.changed").length;
			let isValidated=$(".front_validate").length==0?true:$(".front_validate").valid();
			let personPhotoIsChanged=true;
			// 如果人证合一的照片不合格，则必须更改该照片后，才能上传数据
			if (-1==person_photo_status) {
				if (!$("#person_uploader").hasClass("changed")) {
					$("#person_uploader").next(".upload_message").html("请修改手持身份证照片");
					personPhotoIsChanged=false;
				}
			}
			if (isValidated&&personPhotoIsChanged) {
				addLoadingAnimation();
				if (0==changedFileNumber) {
					$.post(postURL,postData,function(data){
						if (data.flag) {
							orgData=copyObject(data,orgData);
							setStatusAndButtons(orgData);
							setTimeout(function(){
								$("#authenticationModal .modal_cover .loading").html("<h4>提交成功</h4>");
								setTimeout(function(){
									$("#authenticationModal").modal("hide");
									$("#authenticationModal .modal_cover").remove();
								}, 1500);
							},1500);
						}else{
							setTimeout(function(){
								$("#authenticationModal .modal_cover .loading")
									.html('<h4>提交失败，请重新<br><br><button type="button" class="btn btn-info btn_update">修改信息</button></h4>');
							},1500);
							$("#authenticationModal .modal_cover .loading").off("click").on("click",".btn_update",function(){
								$("#identity_box .btn_update").trigger("click");
								$("#authenticationModal .modal_cover").remove();
							})
						}
					});
				}else{
					uploader.upload();
					let successCounter=0;// 计数器，统计文件上传成功的次数
					uploader.on("uploadSuccess",function(file,response){
						if (response.flag) {
							++successCounter;
							if (successCounter==changedFileNumber) {
									$.post(postURL,postData,function(data){
										if (data.flag) {
											orgData=copyObject(data,orgData);
											setStatusAndButtons(orgData);
											setTimeout(function(){
												$("#authenticationModal .modal_cover .loading").html("<h4>提交成功</h4>");
												setTimeout(function(){
													$("#authenticationModal").modal("hide");
													$("#authenticationModal .modal_cover").remove();
												}, 1500);
											},1500);
										}else{
											setTimeout(function(){
												$("#authenticationModal .modal_cover .loading")
													.html('<h4>提交失败，请重新<br><br><button type="button" class="btn btn-info btn_update">修改信息</button></h4>');
											},1500);
											$("#authenticationModal .modal_cover .loading").off("click").on("click",".btn_update",function(){
												$("#identity_box .btn_update").trigger("click");
												$("#authenticationModal .modal_cover").remove();
											})
										}
									});
							}
						}else{
							$("#authenticationModal .modal_cover").remove();
							let selector=response.fileName.substring(0, response.fileName.indexOf("_"));
							let uploadMessageHtml='图片保存失败<br>请重新选择图片后再次点击[提交修改]';
							$("."+selector+"_photo .upload_message").html(uploadMessageHtml);
						}
					})
				}
			}
			
		})
	})
	
	// 点击身份证认证的 [查看信息]按钮
	$("#identity_box").on("click",".btn_check",function(){
		let modalHtml=
			'<div class="front_info">\
				<div class="front_title text-muted">身份证正面</div>\
				<div class="front_photo"><img src="/asxuexi_resource/'+orgData.org_id+'/validate/front_photo.png"></div>\
				<div class="front_content">\
					<div><label>姓名</label><span>'+orgData.name+'</span></div>\
					<div><label>身份证号码</label><span>'+orgData.id_number+'</span></div>\
				</div>\
			</div>\
			<div class="back_info">\
				<div class="back_title text-muted">身份证反面</div>\
				<div class="back_photo"><img src="/asxuexi_resource/'+orgData.org_id+'/validate/back_photo.png"></div>\
				<div class="back_content">\
					<div><label>有效期至</label><span>'+orgData.expiry_date+'</span></div>\
				</div>\
			</div>\
			<div class="person_info">\
				<div class="person_title text-muted">手持身份证照片</div>\
				<div class="person_photo"><img src="/asxuexi_resource/'+orgData.org_id+'/validate/person_photo.png"></div>\
			</div>';
		let footerHtml='';
		$("#authenticationModal .modal-body").html(modalHtml);
		$("#authenticationModal .modal-footer").remove();
		$("#authenticationModal .modal-body").after(footerHtml);
		$("#authenticationModal").modal("show");
	})
	// 监听鼠标移入移出认证面板中的图片的事件
	$("body").on({
		mouseenter:function(){
			// 鼠标移入时，增加hover类，移除webuploader-pick-hover类，使之显示遮罩层的提示
			$(this).addClass("hover");
			$(this).children(".webuploader-pick").removeClass("webuploader-pick-hover");
		},
		mouseleave:function(){
			// 鼠标移出，提示隐藏
			$(this).removeClass("hover");
		},
		click:function(){
			$("#authenticationModal .selected").removeClass("selected");
			$(this).addClass("selected");
		}
	},"#front_uploader,#back_uploader,#person_uploader")
	
	
})