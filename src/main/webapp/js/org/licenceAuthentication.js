function initializeLicenceUploader(licenceUploaderId){
	var uploader=WebUploader.create({
		auto:false,
	    swf: "js/webUploader/Uploader.swf",
	    method :"POST",
	    server: "asxuexi/org/uploadIdentityPhoto.action",
	    accept: {
	    	title: "Images",
	        extensions: "jpg,png",
	    },
	    fileNumLimit: 1,
        fileSingleSizeLimit: 2 * 1024 * 1024
	});
	uploader.addButton({
    	id:licenceUploaderId,
	    multiple:false,
    })
    uploader.on("beforeFileQueued",function(file){
    	uploader.reset();
		$(licenceUploaderId).removeClass("changed");
	})
    uploader.on("fileQueued",function(file){
		file.name="licence_photo.png";
		uploader.makeThumb( file, function( error, src ) {
            if ( error ) {
            	$(licenceUploaderId).nextAll("img").replaceWith('<div>不能预览</div>');
                return;
            }
            $(licenceUploaderId).nextAll("img").attr( "src", src );
        }, 250, 160 );
    	$(licenceUploaderId).addClass("changed");
    	$(licenceUploaderId).trigger("fileChangedEvent");
    	$(licenceUploaderId).nextAll(".upload_message").html("");
	})
	return uploader;
}

function setStatusAndButtonsOfLicence(data){
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
			'<span>原因：</span><span class="text-muted">请查看系统消息</span>';
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
				$("#licence_box .authentication_status").html(statusHtml);
				$("#licence_box .authentication_buttons").html(buttonsHtml);
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
	$("#licence_box .authentication_status").html(statusHtml);
	$("#licence_box .authentication_buttons").html(buttonsHtml);
	$("#licence_box .authentication_statement").html(statementHtml);
}

function addLoadingAnimationOfLicence(){
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
	$("#licenceModal .modal-content").append(html);
}

$(document).ready(function(){
	var licenceData;
	$.post("asxuexi/org/getLicenceAuthenticationStatus.action", function(data){
		licenceData=data;
//		console.log(data)
		setStatusAndButtonsOfLicence(licenceData);
	});
	var licenceValidateOptions={
			onfocusout: function(element){
				$(element).valid();
			},
			rules: {
				licence_number_input:{
					required: true,
				},
				company_name_input:{
					required: true,
				},
				company_address_input:{
					required: true,
				},
				legal_representative_input:{
					required: true,
				}
			},
			messages:{
				licence_number_input:{
					required: "请填写营业执照号码",
				},
				company_name_input:{
					required: "请填写注册名称",
				},
				company_address_input:{
					required: "请填写注册地址",
				},
				legal_representative_input:{
					required: "请填写法定代表人",
				}
			}
	}
	
	// 点击营业执照认证的 [进行认证]按钮
	$("#licence_box").on("click",".btn_todo",function(){
		let modalHtml=
			'<div class="notice text-warning">*请确保填写的信息与营业执照展示的信息一致</div>\
			<div class="licence_content">\
				<form class="licence_validate">\
					<div class="licence_number">\
						<input type="text" id="licence_number_input" class="form-control"\
							name="licence_number_input" placeholder="营业执照号码" autocomplete="off"\
							maxlength="30" >\
					</div>\
					<div class="company_name">\
						<input type="text" id="company_name_input" class="form-control"\
							name="company_name_input" placeholder="注册名称" autocomplete="off"\
							maxlength="30">\
					</div>\
					<div class="company_address">\
						<input type="text" id="company_address_input" class="form-control"\
							name="company_address_input" placeholder="注册地址" autocomplete="off"\
							maxlength="30">\
					</div>\
					<div class="legal_representative">\
						<input type="text" id="legal_representative_input" class="form-control"\
							name="legal_representative_input" placeholder="法定代表人" autocomplete="off"\
							maxlength="20">\
					</div>\
					<div class="licence_photo">\
						<div class="licence_photo_container">\
							<div class="licence_photo_requirement">照片大小不能超过2MB，必须为jpg或png格式。照片上的信息要求清晰可见。</div>\
							<div class="licence_uploader_box">\
								<div id="licence_uploader"></div>\
								<div class="upload_message"></div>\
								<img class="not_existed" src="img/org/authentication/addlicence.png">\
							</div>\
						</div>\
					</div>\
				</form>\
			</div>';
		let footerHtml=
			'<div class="modal-footer">\
				<button type="button" class="btn btn-primary">确认提交</button>\
            	<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>\
			</div>';
		$("#licenceModal .modal-body").html(modalHtml);
		$("#licenceModal .modal-footer").remove();
		$("#licenceModal .modal-body").after(footerHtml);
		licenceUploader=initializeLicenceUploader("#licence_uploader");
		$("#licenceModal").modal("show");
		$(".licence_validate").validate(licenceValidateOptions);
		// 点击[确认提交]按钮
		$("#licenceModal .modal-footer .btn:first-child").off("click").on("click",function(){
			if (!$("#licence_uploader").hasClass("changed")) {
				$("#licence_uploader").nextAll(".upload_message").html("请上传营业执照照片");
			}
			let isReady=$(".licence_validate").valid()&&$("#licence_uploader").hasClass("changed");
			if (isReady) {
				licenceUploader.upload();
				addLoadingAnimationOfLicence();
			}
			licenceUploader.on("uploadSuccess",function(file,response){
				if (response.flag) {
					let licenceNumber=$("#licence_number_input").val();
					let companyName=$("#company_name_input").val();
					let companyAddress=$("#company_address_input").val();
					let legalRepresentative=$("#legal_representative_input").val();
					$.post("asxuexi/org/insertLicenceInfo.action",
					{
						licenceNumber:licenceNumber,
						companyName:companyName,
						companyAddress:companyAddress,
						legalRepresentative:legalRepresentative
					},function(data){
						if (data.flag) {
							licenceData=data;
							setTimeout(function(){
								$("#licenceModal .modal_cover .loading").html("<h4>提交成功</h4>");
								setStatusAndButtonsOfLicence(licenceData);
								setTimeout(function(){
									$("#licenceModal").modal("hide");
									$("#licenceModal .modal_cover").remove();
								}, 1500);
							},1500);
						}else{
							setTimeout(function(){
								$("#licenceModal .modal_cover .loading")
									.html('<h4>提交失败，请重新<br><br><button type="button" class="btn btn-info btn_todo">进行认证</button></h4>');
							},1500);
							$("#licenceModal .modal_cover .loading").off("click").on("click",".btn_todo",function(){
								$("#licence_box .btn_todo").trigger("click");
								$("#licenceModal .modal_cover").remove();
							})
						}
					})
				}else{
					$("#licenceModal .modal_cover").remove();
					let uploadMessageHtml='图片保存失败<br>请重新选择图片后再次点击[确认提交]';
					$("#licence_uploader+.upload_message").html(uploadMessageHtml);
				}
			})
		})
	})
	
	// 点击营业执照认证的 [修改信息]按钮
	$("#licence_box").on("click",".btn_update",function(){
		let status=licenceData.status;
		let modalHtml=
			'<div class="notice text-warning">*请确保填写的信息与营业执照展示的信息一致</div>\
			<div class="licence_content">\
				<form class="licence_validate">\
					<div class="licence_number">\
						<input type="text" id="licence_number_input" class="form-control"\
							name="licence_number_input" placeholder="营业执照号码" autocomplete="off"\
							maxlength="30" value="'+licenceData.licence_number+'">\
					</div>\
					<div class="company_name">\
						<input type="text" id="company_name_input" class="form-control"\
							name="company_name_input" placeholder="注册名称" autocomplete="off"\
							maxlength="30" value="'+licenceData.company_name+'">\
					</div>\
					<div class="company_address">\
						<input type="text" id="company_address_input" class="form-control"\
							name="company_address_input" placeholder="注册地址" autocomplete="off"\
							maxlength="30" value="'+licenceData.company_address+'">\
					</div>\
					<div class="legal_representative">\
						<input type="text" id="legal_representative_input" class="form-control"\
							name="legal_representative_input" placeholder="法定代表人" autocomplete="off"\
							maxlength="20" value="'+licenceData.legal_representative+'">\
					</div>\
					<div class="licence_photo">\
						<div class="licence_photo_container">\
							<div class="licence_photo_requirement">照片大小不能超过2MB，必须为jpg或png格式。照片上的信息要求清晰可见。</div>\
							<div class="licence_uploader_box">\
								<div id="licence_uploader"></div>\
								<div class="upload_message"></div>\
								<img class="not_existed" src="/asxuexi_resource/'+licenceData.org_id+'/validate/licence_photo.png">\
							</div>\
						</div>\
					</div>\
				</form>\
			</div>';
		let footerHtml=
			'<div class="modal-footer">\
				<button type="button" class="btn btn-primary disabled">提交修改</button>\
            	<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>\
			</div>';
		$("#licenceModal .modal-body").html(modalHtml);
		$("#licenceModal .modal-footer").remove();
		$("#licenceModal .modal-body").after(footerHtml);
		licenceUploader=initializeLicenceUploader("#licence_uploader");
		$("#licenceModal").modal("show");
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
			$("#licenceModal .modal-content").append(modalCoverHtml);
			$("#licenceModal .modal_cover .btn").on("click",function(){
				$("#licenceModal .modal_cover").remove();
				if ($(this).hasClass("btn_cancel")) {
					$("#licenceModal").modal("hide");
				}
			})
		}
		// 为表单增加验证规则
		$(".licence_validate").validate(licenceValidateOptions);
		
		// 监听图片的改变事件，图片发生改变，将[提交修改]按钮变为可用
		$("#licence_uploader").on("fileChangedEvent",function(){
			$("#licenceModal .modal-footer .btn:first-child.disabled").removeClass("disabled");
		})
		// 监听输入框的改变事件，发生改变，将[提交修改]按钮变为可用
		$("#licence_number_input,#company_name_input,#company_address_input,#legal_representative_input").on("input propertychange",function(){
			$(this).addClass("changed");
			$("#licenceModal .modal-footer .btn:first-child.disabled").removeClass("disabled");
		})
		// 点击可用的[提交修改]按钮
		$("#licenceModal .modal-footer").off("click").on("click",".btn:first-child:not(.disabled)",function(){
			let licenceNumber=$("#licence_number_input").val();
			let companyName=$("#company_name_input").val();
			let companyAddress=$("#company_address_input").val();
			let legalRepresentative=$("#legal_representative_input").val();
			
			if (0==status) {
				postURL="asxuexi/org/updateLicenceInfo.action";
				postData={
						licenceNumber:licenceNumber,
						companyName:companyName,
						companyAddress:companyAddress,
						legalRepresentative:legalRepresentative,
						gmtKey:"gmt_modified"
				};
			}
			if (-1==status) {
				postURL="asxuexi/org/updateLicenceInfo.action";
				postData={
						licenceNumber:licenceNumber,
						companyName:companyName,
						companyAddress:companyAddress,
						legalRepresentative:legalRepresentative,
						gmtKey:"gmt_create"
				};
			}
			if (1==status) {
				postURL="asxuexi/org/insertLicenceInfo.action";
				postData={
						licenceNumber:licenceNumber,
						companyName:companyName,
						companyAddress:companyAddress,
						legalRepresentative:legalRepresentative
				};
			}
			
			if ($(".licence_validate").valid()) {
				addLoadingAnimationOfLicence();
				if ($("#licence_uploader").hasClass("changed")) {
					licenceUploader.upload();
					licenceUploader.on("uploadSuccess",function(file,response){
						if (response.flag) {
							$.post(postURL,postData,function(data){
								if (data.flag) {
									licenceData=data;
									setTimeout(function(){
										$("#licenceModal .modal_cover .loading").html("<h4>提交成功</h4>");
										setStatusAndButtonsOfLicence(licenceData);
										setTimeout(function(){
											$("#licenceModal").modal("hide");
											$("#licenceModal .modal_cover").remove();
										}, 1500);
									},1500);
								}else{
									setTimeout(function(){
										$("#licenceModal .modal_cover .loading")
											.html('<h4>提交失败，请重新<br><br><button type="button" class="btn btn-info btn_update">修改信息</button></h4>');
									},1500);
									$("#licenceModal .modal_cover .loading").off("click").on("click",".btn_update",function(){
										$("#licence_box .btn_update").trigger("click");
										$("#licenceModal .modal_cover").remove();
									})
								}
							})
						}else{
							$("#licenceModal .modal_cover").remove();
							let uploadMessageHtml='图片保存失败<br>请重新选择图片后再次点击[提交修改]';
							$("#licence_uploader+.upload_message").html(uploadMessageHtml);
						}
					})
				}else{
					$.post(postURL,postData,function(data){
						if (data.flag) {
							licenceData=data;
							setTimeout(function(){
								$("#licenceModal .modal_cover .loading").html("<h4>提交成功</h4>");
								setStatusAndButtonsOfLicence(licenceData);
								setTimeout(function(){
									$("#licenceModal").modal("hide");
									$("#licenceModal .modal_cover").remove();
								}, 1500);
							},1500);
						}else{
							setTimeout(function(){
								$("#licenceModal .modal_cover .loading")
									.html('<h4>提交失败，请重新<br><br><button type="button" class="btn btn-info btn_update">修改信息</button></h4>');
							},1500);
							$("#licenceModal .modal_cover .loading").off("click").on("click",".btn_update",function(){
								$("#licence_box .btn_update").trigger("click");
								$("#licenceModal .modal_cover").remove();
							})
						}
					})
				}
			}
		})
	})
	
	// 点击营业执照认证的 [查看信息]按钮
	$("#licence_box").on("click",".btn_check",function(){
		let modalHtml=
			'<div class="licence_content">\
				<div class="licence_number">\
					<label>营业执照号码</label>\
					<div class="text-muted">'+licenceData.licence_number+'</div>\
				</div>\
				<div class="company_name">\
					<label>注册名称</label>\
					<div class="text-muted">'+licenceData.company_name+'</div>\
				</div>\
				<div class="company_address">\
					<label>注册地址</label>\
					<div class="text-muted">'+licenceData.company_address+'</div>\
				</div>\
				<div class="legal_representative">\
					<label>法定代表人</label>\
					<div class="text-muted">'+licenceData.legal_representative+'</div>\
				</div>\
				<div class="licence_photo">\
					<label>营业执照照片</label>\
					<img class="existed" src="/asxuexi_resource/'+licenceData.org_id+'/validate/licence_photo.png">\
				</div>\
			</div>';
		let footerHtml='';
		$("#licenceModal .modal-body").html(modalHtml);
		$("#licenceModal .modal-footer").remove();
		$("#licenceModal .modal-body").after(footerHtml);
		$("#licenceModal").modal("show");
	})
	
})