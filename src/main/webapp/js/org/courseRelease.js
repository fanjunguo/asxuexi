// 监听页面的重新加载
window.onbeforeunload = function(e) {
	console.log(e)
	e = e || window.event;
	// 兼容IE8和Firefox 4之前的版本
	if (e) {
		e.returnValue = '';
	}
	// Chrome, Safari, Firefox 4+, Opera 12+ , IE 9+
	return '';
}
// 关闭登录提示窗口
function closeLoginModal(){
	$("#login_modal").modal("hide");
}
//增加过渡动画
$.fn.addLoadingAnimation = function (){
	let html=`
	<div class="loading">
		<div class="loading-dot loading-dot-1"></div>
		<div class="loading-dot loading-dot-2"></div>
		<div class="loading-dot loading-dot-3"></div>
		<div class="loading-dot loading-dot-4"></div>
		<div class="loading-dot loading-dot-5"></div>
		<div class="loading-dot loading-dot-6"></div>
		<div class="loading-dot loading-dot-7"></div>
		<div class="loading-dot loading-dot-8"></div>
	</div>`;
	$(this).append(html);
}
// 移除过渡动画
$.fn.removeLoadingAnimation = function (){
	$(this).children(".loading").remove();
}
// 敏感词验证
function wordFilter(textArray, passed , notPassed){
	let contextArray = [];
	for (let i = 0; i < textArray.length; i++) {
		contextArray.push({text:textArray[i]});
	}
	$.post("filterWord/isFilterWord.do", {context:JSON.stringify(contextArray)},function(data){
		let contextIsPassed = true;// 是否通过敏感词验证的标识
		let invalidWords="";// 敏感词
		let set = new Set();
		for (let i = 0; i < data.length; i++) {
			if (data[i].text != "") {
				set.add(data[i].text);
			}
		}
		if (set.size != 0) {
			contextIsPassed = false;
		}
		for (let text of set.values()) {
			invalidWords += text+"&emsp;";
		}
		if (contextIsPassed) {
			passed();
		}else{
			notPassed(invalidWords);
		}
	});
}
// 初始化页面组件
function initializePage(courseType, course){
	let basicInfoHtml =`
	<form id="basic_info" class="form-horizontal basic_info" role="form">
		<label class="partial_title"><b class="required">*</b>基本信息</label>`;
	if (course == null) {
		basicInfoHtml += `
		<div class="form-group">
			<label class="col-sm-2 control-label">课程名称</label>
			<div class="col-sm-9">
				<input type="text" class="form-control" id="course_name"
					autocomplete="off" maxlength="25" name="course_name"
					placeholder="请填写课程名称">
				<div class="number_limit">
					<span class="count_number">0</span>/25
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">课程分类</label>
			<div class="col-sm-9 sort_select">
				<input type="hidden" id="course_sort" name="course_sort"
					autocomplete="off" value=""> <select id="first_sort"
					style="width: 150px; height: 34px;"></select> <select
					id="second_sort" style="width: 150px; height: 34px;"></select> <select
					id="third_sort" style="width: 150px; height: 34px;"></select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"> 收费方式 <sup
				data-toggle="tooltip" id="charging_tip"
				title="请仔细核对收费方式，课程添加后无法修改！">i</sup>
			</label>
			<div class="col-sm-9" id="charging_mode_container">
				<input type="hidden" id="charging_mode" name="charging_mode">
				<label class="btn-default" id="pre_charging"> 课前预收费 <sup
					data-toggle="tooltip" title="在课程开始前收取固定费用，用户缴费后上课。">?</sup>
				</label> <label class="btn-default" id="post_charging"> 上课后收费 <sup
					data-toggle="tooltip"
					title="开课后，自主决定收费日期。向用户发送账单收取相应费用。用户先上课，后缴费。">?</sup>
				</label>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">授课老师</label>
			<div class="col-sm-9">
				<input type="text" class="form-control" id="course_teacher"
					autocomplete="off" maxlength="15" name="course_teacher"
					placeholder="请填写授课老师">
				<div class="number_limit">
					<span class="count_number">0</span>/15
				</div>
			</div>
		</div>`;
	}else{
		if (course.course_charging == 1) {
			course.course_charging="课前预收费";
		}
		if (course.course_charging == 2) {
			course.course_charging="上课后收费";
		}
		basicInfoHtml += `
		<div class="form-group">
			<label class="col-sm-2 control-label">课程名称</label>
			<div class="col-sm-9">
				<input type="text" class="form-control" id="course_name"
					autocomplete="off" maxlength="25" name="course_name"
					placeholder="请填写课程名称" value="${course.course_name}">
				<div class="number_limit">
					<span class="count_number">${course.course_name.length}</span>/25
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">课程分类</label>
			<div class="col-sm-9 sort_select">
				<input type="hidden" id="course_sort" name="course_sort"
					autocomplete="off" value="${course.sort_id}"> <select id="first_sort"
					style="width: 150px; height: 34px;"></select> <select
					id="second_sort" style="width: 150px; height: 34px;"></select> <select
					id="third_sort" style="width: 150px; height: 34px;"></select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">
				收费方式
			</label>
			<div class="col-sm-10" id="charging_mode_container">
				<div class="show_area">${course.course_charging}</div>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">授课老师</label>
			<div class="col-sm-9">
				<input type="text" class="form-control" id="course_teacher"
					autocomplete="off" maxlength="15" name="course_teacher"
					placeholder="请填写授课老师" value="${course.course_teacher}">
				<div class="number_limit">
					<span class="count_number">${course.course_teacher.length}</span>/15
				</div>
			</div>
		</div>`;
	}
	basicInfoHtml += `
		<div class="form-group">
			<label class="col-sm-2 control-label">图文描述</label>
			<div class="col-sm-9">
				<textarea id="course_description" name="course_description"
					autocomplete="off"></textarea>
			</div>
		</div>
	</form>`;
	let pricingPackageHtml = `
	<form class="pricing_package">
		<label class="partial_title"> <b class="required">*</b>套餐信息 <b
			class="tips">请至少添加一个套餐。我们建议您添加一个免费试听的套餐。</b>
		</label>
		<div class="package_group package_head">
			<div class="package_name">套餐名称</div>
			<div class="package_price">套餐价格</div>
			<div class="course_length">课时数量</div>
			<div class="package_action">操作</div>
		</div>`;
	if (course == null) {
		pricingPackageHtml +=`
		<div class="package_group package_body">
			<div class="package_name">
				<div class="show_area">免费试听</div>
			</div>
			<div class="package_price">
				<div class="show_area">0.00</div>
			</div>
			<div class="course_length">
				<div class="show_area">1</div>
			</div>
			<div class="package_action">
				<span class="package_edit">&#xe906编辑</span> <span
					class="package_delete">&#xe9ad删除</span>
			</div>
		</div>`;
	}else{
		for (let i = 0; i < course.packageList.length; i++) {
			if (course.packageList[i].course_length == 0) {
				course.packageList[i].course_length = "无";
			}
			pricingPackageHtml += `
			<div class="old_package package_group package_body" id=${course.packageList[i].package_id}>
				<div class="package_name">
					<div class="show_area">${course.packageList[i].package_name}</div>
				</div>
				<div class="package_price">
					<div class="show_area">${course.packageList[i].package_price}</div>
				</div>
				<div class="course_length">
					<div class="show_area">${course.packageList[i].course_length}</div>
				</div>
				<div class="package_action">
					<span class="package_edit">&#xe906编辑</span>
					<span class="package_delete">&#xe9ad删除</span>
				</div>
			</div>`;
		}
	}
	pricingPackageHtml += `
		<div class="package_group package_foot">
			<div class="package_add">新增课程套餐</div>
		</div>
	</form>`;
	let picturesVideosHtml = `
	<div class="pictures_videos">
		<label class="partial_title"> <b class="required">*</b>图片视频 <b
			class="tips">请至少上传一张课程主图。课程视频可以空缺。</b>
		</label>
		<div class="picture_container">
			<div class="picture_group" id="picture_1">
				<img src="img/icons/addImg.png">
			</div>
			<div class="picture_bar" id="picture_1_bar">
				<div>第1张</div>
				<span class="">&#xe9ac</span>
			</div>
			<div class="picture_group" id="picture_2">
				<img src="img/icons/addImg.png">
			</div>
			<div class="picture_bar" id="picture_2_bar">
				<div>第2张</div>
				<span>&#xe9ac</span>
			</div>
			<div class="picture_group" id="picture_3">
				<img src="img/icons/addImg.png">
			</div>
			<div class="picture_bar" id="picture_3_bar">
				<div>第3张</div>
				<span>&#xe9ac</span>
			</div>
			<div class="picture_group" id="picture_4">
				<img src="img/icons/addImg.png">
			</div>
			<div class="picture_bar" id="picture_4_bar">
				<div>第4张</div>
				<span>&#xe9ac</span>
			</div>
		</div>
		<div class="video_container">
			<div class="video_group" id="video_1">
				<img src="img/icons/icon_videoType.png">
			</div>
			<div class="video_bar" id="video_1_bar">
				<div>宣传视频</div>
				<span>&#xe9ac</span>
			</div>
		</div>
	</div>`;
	let courseTimetableHtml = `
	<div class="course_timetable">
		<label class="partial_title">课程安排 <b class="tips">非必填。建议填写课程安排，方便通知用户上课。</b>
		</label>`
		if (course != null) {
			for (let i = 0; i < course.courseTimetable.length; i++) {
				courseTimetableHtml += `
				<div class="table_group table_body" id="${course.courseTimetable[i].chapter_id}">
					<div class="chapter_number">
						第<span class="number_value">${course.courseTimetable[i].chapter_number}</span>讲
					</div>
					<div class="chapter_content">
						<div class="chapter_name">${course.courseTimetable[i].chapter_name}</div>
						<div class="chapter_time">
							<span class="chapter_datetime"><span class="chapter_date">${course.courseTimetable[i].chapter_date}</span>
								<span class="chapter_begin">${course.courseTimetable[i].chapter_begin}</span><span>-</span><span
								class="chapter_end">${course.courseTimetable[i].chapter_end}</span> </span><span class="chapter_length"><span
								class="length_value">${course.courseTimetable[i].chapter_length}</span>课时 </span>
						</div>
					</div>
					<div class="table_action">
						<div class="chapter_edit">&#xe906编辑</div>
						<div class="chapter_delete">&#xe9ad删除</div>
					</div>
				</div>`;
			}
		}
	courseTimetableHtml +=`
		<div class="table_group table_foot">
			<div class="chapter_add">新增课程章节</div>
		</div>
	</div>`;
	let html = basicInfoHtml + pricingPackageHtml + picturesVideosHtml + courseTimetableHtml;
	$(".content").html(html);
	let floatButtonHtml =`
	<div class="float_button_container">
		<span>校区：</span>`;
	if (course == null) {
		floatButtonHtml +=`
		<select id="org_select" style="width: 150px; height: 34px;"></select>
		<button type="button" class="btn btn-primary">提交课程信息</button>
	</div>`;
	}else {
		floatButtonHtml +=`
			<span>${course.org_name}</span>
			<button type="button" class="btn btn-primary">提交课程信息</button>
		</div>`;
	}
	
	$("body").append(floatButtonHtml);
	// 请求分类表所有分类，并填充下拉菜单
	$.post("asxuexi/android/getSorts.do", function(array){
		if (course == null) {
			// 初始化三个分类下拉菜单
			$("#first_sort").combobox({
				prompt:"一级分类",editable:false,valueField: "id",textField: "text"
			})
			$("#second_sort").combobox({
				prompt:"二级分类",editable:false,valueField: "id",textField: "text"
			})
			$("#third_sort").combobox({
				prompt:"三级分类",editable:false,valueField: "id",textField: "text",multiple:true
			})
			if (array.length != 0) {
				// 数据不为空，将所有一级分类加入[一级分类下拉菜单]
				$("#first_sort").combobox({
					data: array[0].children,
					onSelect: function(firstSort){
						// 点击一级分类，清空[三级分类下拉菜单]
						$("#third_sort").combobox("loadData",[]).combobox("clear");
						// 清空[input#course_sort]的值
						$("#course_sort").val("");
						// 将该一级分类所有的二级分类加入[二级分类下拉菜单]
						$("#second_sort").combobox({
							data: firstSort.children,
							onSelect: function(secondSort){
								// 点击二级分类，清空[input#course_sort]的值
								$("#course_sort").val("");
								// 将该二级分类所有的三级分类加入[三级分类下拉菜单]
								$("#third_sort").combobox({
									data: secondSort.children,
									onSelect: function(thirdSort){
										// 点击三级分类，将该级ID赋值给[input#course_sort]
										$("#course_sort").val(thirdSort.id);
									}
								})
							}
						})
					}
				})
			}
		}else{
			// 递归遍历树形数组，根据Id返回相应节点
			function findNodeById( nodeArray, id ){
				for (let i = 0; i < nodeArray.length; i++) {
					if (nodeArray[i].id == id) {
						return nodeArray[i];
					}
					let node = findNodeById( nodeArray[i].children, id );
					if (typeof node != "undefined") {
						return node;
					}
				}
			}
			let courseSort = course.sortIdList[0];
			let thirdSortNode = findNodeById(array , courseSort);
			let secondSortNode = findNodeById(array , thirdSortNode.pid);
			let firstSortNode = findNodeById(array , secondSortNode.pid);
			if (array.length != 0) {
				// 数据不为空，初始化三个分类下拉菜单，并将相应分类选中
				$("#first_sort").combobox({
					prompt:"一级分类",
					editable:false,
					valueField: "id",
					textField: "text",
					data: array[0].children,
					onSelect: function(firstSort){
						// 点击一级分类，清空[三级分类下拉菜单]
						$("#third_sort").combobox("loadData",[]).combobox("clear");
						// 清空[input#course_sort]的值
						$("#course_sort").val("");
						// 将该一级分类所有的二级分类加入[二级分类下拉菜单]
						$("#second_sort").combobox({
							data: firstSort.children,
							onSelect: function(secondSort){
								// 点击二级分类，清空[input#course_sort]的值
								$("#course_sort").val("");
								// 将该二级分类所有的三级分类加入[三级分类下拉菜单]
								$("#third_sort").combobox({
									data: secondSort.children,
									onSelect: function(thirdSort){
										// 点击三级分类，将该级ID赋值给[input#course_sort]
										$("#course_sort").val(thirdSort.id);
									}
								})
							}
						})
					}
				}).combobox("setValue",firstSortNode.id);
				$("#second_sort").combobox({
					prompt:"二级分类",
					editable:false,
					valueField: "id",
					textField: "text",
					data: firstSortNode.children,
					onSelect: function(secondSort){
						// 点击二级分类，清空[input#course_sort]的值
						$("#course_sort").val("");
						// 将该二级分类所有的三级分类加入[三级分类下拉菜单]
						$("#third_sort").combobox({
							data: secondSort.children,
							onSelect: function(thirdSort){
								// 点击三级分类，将该级ID赋值给[input#course_sort]
								$("#course_sort").val(thirdSort.id);
							}
						})
					}
				}).combobox("setValue",secondSortNode.id);
				$("#third_sort").combobox({
					prompt: "三级分类",
					editable: false,
					valueField: "id",
					textField: "text",
					data: secondSortNode.children,
					multiple: true,
					onSelect: function(thirdSort){
						// 点击三级分类，将该级ID赋值给[input#course_sort]
						$("#course_sort").val(thirdSort.id);
					}
				}).combobox("setValues",course.sortIdList);
			}
		}
	});
	// 为富文本编辑器增加自定义的按钮和功能
	UE.registerUI("insertsingleimage",function(editor,uiName){
	    editor.registerCommand(uiName,{
	        execCommand:function(cmd, option){
	        	this.execCommand( "insertimage", option);
	        }
	    });

	    var btn = new UE.ui.Button({
	        name:uiName,
	        title:"插入图片",
	        cssRules :"background-position: -380px 0;",
	        onclick:function () {
	        	openImageManager("picture_select",1);
	        	// 选择图片后，点击[确定]按钮
	    		$("body").off("click","#picture_select").on("click","#picture_select",function(){
	    			let src=$(this).attr("path");
	    			// 关闭图片空间面板
	    			$(".picture_select").remove();
	    			let img = {
	    					src: src,
	    					width: "100%"
	    			}
	    			editor.execCommand(uiName,img);
	    		})
	        }
	    });

	    editor.addListener("selectionchange", function () {
	        var state = editor.queryCommandState("insertsingleimage");
	        if (state == -1) {
	            btn.setDisabled(true);
	            btn.setChecked(false);
	        } else {
	            btn.setDisabled(false);
	            btn.setChecked(state);
	        }
	    });

	    return btn;
	});
	// 实例化百度ueditor
	var txtEditor=UE.getEditor("course_description",{
		initialFrameHeight: 400,
		initialFrameWidth : 855,
		initialContent: course==null?"":course.course_description,
		wordCount: false,
		elementPathEnabled: false,
		lineheight: ["1","1.5","2","2.5","3","3.5","4","4.5"],
		fontsize: [10,11,12,14,16,18,20,22,24,26,28,32,36,40,44,48,72],
		insertunorderedlist: {circle: '',disc: '',square: ''},
		toolbars: [
			["undo","redo","|","customstyle","paragraph","fontfamily","fontsize","|",
			"bold", "italic", "underline","fontborder","strikethrough","superscript","subscript",
			"blockquote","horizontal","cleardoc","|","forecolor","backcolor","insertorderedlist","insertunorderedlist"],
			["justifyleft","justifycenter","justifyright","|","directionalityltr","directionalityrtl","indent",
			"rowspacingtop","rowspacingbottom","lineheight","|","inserttable","deletetable","insertparagraphbeforetable",
			"insertrow","deleterow","insertcol","deletecol","mergecells","splittocells","splittorows","splittocols","|",
			"insertsingleimage","imagenone","imageleft","imagecenter","imageright"]
		],
		enableContextMenu: true,
		maximumWords: 100000,
		autoHeightEnabled: false
	});
	if (course == null) {
		// 新增课程时
		// 初始化[收费方式]input的值
		$("#charging_mode").val("");
		// 为[收费方式]右上角的[i]及其选项按钮右上角的[?]，[图片视频]右上角的[i]应用bootstrap提示组件
		$("#pre_charging sup,#post_charging sup,#charging_tip").tooltip();
		// 初始化校区选择框
		$.post("orgCourse/listOrgs.action", function(json){
			$("#org_select").combobox({
				prompt:"选择校区",
				editable:false, 
				valueField: "orgId",
				textField: "orgName",
				data: json.data,
				multiple:true
			}).combobox("setValue",json.data[0].orgId)
		})
		
	}else{
		// 编辑课程时
		// 在对应位置展示当前课程图片
		for (let i = 0; i < course.pictureList.length; i++) {
			let index=course.pictureList[i].img_order;
			$("#picture_"+index+" img").attr("src",course.pictureList[i].img_name);
			$("#picture_"+index+"_bar span").addClass("usable");
		}
		// 在对应位置展示当前课程视频
		for (let i = 0; i < course.videoList.length; i++) {
			let index=course.videoList[i].video_order;
			$("#video_"+index+" img").remove();
			$("#video_"+index).append("<video src="+course.videoList[i].video_name+"></video>")
			$("#video_"+index+"_bar span").addClass("usable");
		}
	}
	// 给form.basic_info中的元素添加验证规则
	$(".basic_info").validate({
		rules: {
			course_name: {
				required: true,
				maxlength: 25
			},
			course_sort: "required",
			charging_mode: "required",
			course_teacher: {
				required: true,
				maxlength: 15
			},
			course_description : "required"
		},
		messages: {
			course_name: {
				required: "请填写课程名称",
				maxlength: "课程名称不能超过25个字符"
			},
			course_sort: "请选择课程分类",
			charging_mode: "请选择收费方式",
			course_teacher: {
				required: "请填写授课老师",
				maxlength: "老师姓名不能超过15个字符"
			},
			course_description: "请填写图文描述"
		}
	})
	return txtEditor;
}
// 发生错误时，初始化页面组件
function initializeErrorPage(){
	let html =`
	<div>发生错误，请关闭重试！</div>`;
	$(".content").html(html);
}
// 初始化课程套餐面板
function initializePackageModal(param){
	let html=`
	<form id="package_form">
		<div class="form-group">
			<label>套餐名称</label>
			<div>
				<input type="text" class="form-control" id="package_name" 
				 autocomplete="off" name="package_name" maxlength="20">
				<div class="number_limit">
					<span class="count_number">0</span>/20
				</div>
			</div>
		</div>
		<div class="form-group">
			<label>套餐价格</label>
			<div>
				<input id="package_price" name="package_price" style="width: 105px; height: 34px">
			</div>
		</div>
		<div class="form-group">
			<label>课时数</label>
			<div>
				<input class="" id="course_length" name="course_length"
					style="width: 105px; height: 34px">
			</div>
		</div>
	</form>`;
	$("#package_modal .package_container").html(html);
	$("#package_modal .modal-title").text("新增课程套餐");
	// 监听[套餐名称]的键盘事件，输入停止时，统计字数，显示在右侧
    $("#package_name").on("keyup",function(){
    	let number=$(this).val().length;
    	$(this).siblings(".number_limit").children(".count_number").text(number);
    })
    // 初始化课程套餐面板[套餐价格]的数字微调组件
	$("#package_price").numberspinner({
        min: 0, precision: 2, increment: 0.1, value: 0
    });
	// 初始化课程套餐面板[课时数]的数字微调组件
    $("#course_length").numberspinner({
        min: 1, increment: 1, value: ""
    });
	if (typeof param != "undefined") {
		$("#package_modal .modal-title").text("编辑课程套餐");
		$("#package_name").val(param.packageName);
		$("#package_name+.number_limit .count_number").text(param.packageName.length);
		$("#package_price").numberspinner("setValue",param.packagePrice);
		if (param.courseLength!="无") {
			$("#course_length").numberspinner("setValue",param.courseLength);
		}
	}
	// 给form#package_form中的元素添加验证规则
	$("#package_form").validate({
		rules: {
			package_name: {
				required: true,
				maxlength: 20
			}
		},
		messages: {
			package_name: {
				required: "请填写套餐名称",
				maxlength: "套餐名称不能超过20个字符"
			}
		}
	})
}
// 初始化课程章节面板
function initializeChapterModal(chapterNum , param){
	let html = `
	<label>第<span id="chapter_num"></span>讲</label>
	<form id="chapter_form">
		<div class="form-group">
			<label>章节名称</label>
			<div>
				<input type="text" class="form-control" id="chapter_name"
					autocomplete="off" maxlength="25" name="chapter_name">
				<div class="number_limit">
					<span class="count_number">0</span>/25
				</div>
			</div>
		</div>
		<div class="form-group">
			<label>上课日期</label>
			<div>
				<input type="text" class="form-control" id="chapter_date"
					name="chapter_date" placeholder="请选择上课日期" readonly>
			</div>
		</div>
		<div class="form-group">
			<label>上课时间</label>
			<div>
				<input class="" id="chapter_begin" name="chapter_begin"
					style="width: 105px; height: 34px"> <span
					style="padding: 5px">至</span> <input class="" id="chapter_end"
					name="chapter_end" style="width: 105px; height: 34px">
			</div>
		</div>
		<div class="form-group">
			<label>课时数</label>
			<div>
				<input class="" id="chapter_length" name="chapter_length"
					style="width: 105px; height: 34px">
			</div>
		</div>
	</form>`;
	$("#chapter_modal .chapter_container").html(html);
	$("#chapter_modal .modal-title").text("新增课程章节");
	$("#chapter_num").text(chapterNum);
	// 监听[章节名称]的键盘事件，输入停止时，统计字数，显示在右侧
    $("#chapter_name").on("keyup",function(){
    	let number=$(this).val().length;
    	$(this).siblings(".number_limit").children(".count_number").text(number);
    })
    // 初始化[上课日期]选择组件
	$("#chapter_date").val("").datetimepicker({
        format: "yyyy-mm-dd",
        language: "zh-CN",
        minView: "month",
        autoclose: true,
        todayHighlight: true,
        initialDate: new Date(),
    })
	// 初始化课程章节面板中[上课时间]的时间微调组件
	$("#chapter_begin").timespinner({
		value: "00:00"
	});
	$("#chapter_end").timespinner({
		value: "00:00"
	});
	// 初始化课程章节面板中[课时数]的数字微调组件
	$("#chapter_length").numberspinner({
        min: 1, increment: 1, value: 1
    });
	if (typeof param != "undefined") {
		$("#chapter_name").val(param.chapterName);
		$("#chapter_name+.number_limit .count_number").text(param.chapterName.length);
		$("#chapter_date").val(param.chapterDate);
		$("#chapter_begin").timespinner("setValue",param.chapterBegin);
		$("#chapter_end").timespinner("setValue",param.chapterEnd);
		$("#chapter_length").numberspinner("setValue",param.chapterLength);
	}
	// 给form#chapter_form中的元素添加验证规则
	$("#chapter_form").validate({
		rules: {
			chapter_name: {
				required: true,
				maxlength: 25
			},
			chapter_date: "required"
		},
		messages: {
			chapter_name: {
				required: "请填写章节名称",
				maxlength: "章节名称不能超过25个字符"
			},
			chapter_date: "请选择上课日期"
		}
	})
}
// 验证是否登录，未登录弹出提示框，登录了执行自定义函数
function isLogin(successFunction){
	$.ajax({
		url: "log_in/isLogin.action",
		success: successFunction,
		error: function(XMLHttpRequest, textStatus){
			var sessionstatus = XMLHttpRequest.getResponseHeader("sessionstatus");
			if (sessionstatus == "timeout") {
				$("#login_modal").modal("show");
			}
		}
	});
}
// 打开图片或视频管理器面板
function openImageManager(selectorId,type){
	isLogin(function(){
		$("body").loadImageManager(selectorId,type);
	});
}
// 将编辑过的课程套餐加入数组中
function pushIntoUpdatePackageArray(packageArray, packageObj){
	let pushed = false;
	for (let i = 0; i < packageArray.length; i++) {
		if (packageArray[i].packageId == packageObj.packageId) {
			packageArray[i] = packageObj;
			pushed = true;
		}
	}
	if (!pushed) {
		packageArray.push(packageObj);
	}
}
// 将编辑过的课程安排信息加入数组中
function pushIntoUpdateCourseTimetableArray(courseTimetableArray, courseTimetableObj){
	let pushed = false;
	for (let i = 0; i < courseTimetableArray.length; i++) {
		if (courseTimetableArray[i].chapterId == courseTimetableObj.chapterId) {
			courseTimetableArray[i] = courseTimetableObj;
			pushed = true;
		}
	}
	if (!pushed) {
		courseTimetableArray.push(courseTimetableObj);
	}
}

$(document).ready(function() {
	// 在页面logo旁添加页面标语
	let slogen = `<div id="slogen">课程发布</div>`;
	$("#header").append(slogen);
	// 定义全局变量
	var courseId = $(".course_id").attr("id");// 课程ID
	var courseType = $(".course_type").val();// 课程类型
	var textEditor;// 富文本编辑器对象
	var insertPackageArray = [];// 新增的课程套餐数组
	var deletePackageArray = [];// 删除的课程套餐数组
	var updatePackageArray = [];// 修改的课程套餐数组
	var insertCourseTimetableArray = [];// 新增的课程安排数组
	var deleteCourseTimetableArray = [];// 删除的课程安排数组
	var updateCourseTimetableArray = [];// 修改的课程安排数组
	// 初始化页面
	if (courseId == "") {
		// 课程ID为空时，新增课程
		txtEditor = initializePage(courseType);
	}else{
		// 编辑课程
		$.ajax({
			type: "POST",
			url: "orgCourse/getCourse.action",
			data: {
				courseId: courseId
			},
			success: function(map){
				if (map.data != null) {
					txtEditor = initializePage(courseType,map.data);
				}else{
					initializeErrorPage();
				}
			},
			error: function(){
				initializeErrorPage();
			}
		});
	}
	// 点击[收费方式]的按钮
	$("body").on("click","#charging_mode_container>label:not(.error)",function(){
		// 隐藏文字提示
		$(".course_peroid_tips").slideUp();
		// 移除未选中按钮的选中效果；为选中按钮添加选中效果
		$(".btn-info.selected").attr("class","btn-default");
		$(this).attr("class","btn-info selected");
		// 获取点击的按钮的id，以此判断是哪个按钮
		let chargingMode=$(this).attr("id");
		if (chargingMode == "pre_charging") {
			// 点击[课前预收费]，将收费方式的input赋值为1
			$("#charging_mode").val(1);
		}
		if (chargingMode == "post_charging") {
			// 点击[上课后收费]，将收费方式的input赋值为2
			$("#charging_mode").val(2);
		}
	})
    // 监听[课程名称][授课老师]的键盘事件，输入停止时，统计字数，显示在右下角
    $("body").on("keyup","#course_name, #course_teacher",function(){
    	let number=$(this).val().length;
    	$(this).siblings(".number_limit").children(".count_number").text(number);
    })
    // 套餐的增删改
	// 点击套餐每一行的[编辑]图标时，编辑该行套餐
	$("body").on("click",".package_action .package_edit",function(){
		let $packageBody = $(this).parents(".package_group.package_body");
		let packageId = $packageBody.attr("id");
		let $packageName = $packageBody.children(".package_name").children(".show_area");
		let $packagePrice = $packageBody.children(".package_price").children(".show_area");
		let $courseLength = $packageBody.children(".course_length").children(".show_area");
		let param = {
				packageName: $packageName.text(),
				packagePrice: $packagePrice.text(),
				courseLength: $courseLength.text()
		}
		initializePackageModal(param);
		$("#package_modal").modal("show");
		// 点击[确定]按钮
		$(".package_buttons .btn-success").off("click").on("click",function(){
			if ($("#package_form").valid()) {
				if (typeof packageId != "undefined") {
					pushIntoUpdatePackageArray(updatePackageArray, {
						packageId : packageId,
						packageName : $("#package_name").val(),
						packagePrice : $("#package_price").numberspinner("getValue"),
						courseLength : $("#course_length").numberspinner("getValue")
					})
				}
				$packageName.text($("#package_name").val());
				$packagePrice.text($("#package_price").numberspinner("getValue"));
				$courseLength.text($("#course_length").numberspinner("getValue"));
				$("#package_modal").modal("hide");
			}
		})
	})
	// 点击套餐每一行的[删除]图标时，删除该行
	$("body").on("click",".package_action .package_delete",function(){
		$(this).parents(".package_group.package_body").remove();
		let packageId = $(this).parents(".package_group.package_body").attr("id");
		if (typeof packageId != "undefined") {
			deletePackageArray.push(packageId);
		}
	})
	// 点击套餐最下方的[新增课程套餐]
	$("body").on("click",".package_foot .package_add",function(){
		initializePackageModal();
		let number=$(".package_body").length + 1;
		$("#package_modal").modal("show");
		// 点击[确定]按钮
		$(".package_buttons .btn-success").off("click").on("click",function(){
			let packageName = $("#package_name").val();
			let packagePrice = $("#package_price").numberspinner("getValue");
			let courseLength = $("#course_length").numberspinner("getValue");
			courseLength = courseLength == "" ? "无" : courseLength ;
			if ($("#package_form").valid()) {
				let html=`
				<div class="package_group package_body">
					<div class="package_name">
						<div class="show_area">${packageName}</div>
					</div>
					<div class="package_price">
						<div class="show_area">${packagePrice}</div>
					</div>
					<div class="course_length">
						<div class="show_area">${courseLength}</div>
					</div>
					<div class="package_action">
						<span class="package_edit">&#xe906编辑</span> <span
							class="package_delete">&#xe9ad删除</span>
					</div>
				</div>`;
				$(".package_foot").before(html);
				// 关闭窗口
				$("#package_modal").modal("hide");
			}
		})
	})
	
	// 图片的增删改
	// 点击[图片区域]的图片，展示图片空间面板
	$("body").on("click",".picture_group",function(){
		openImageManager("picture_select",1);
		$picture=$(this);
		// 点击[确定]按钮
		$("body").off("click","#picture_select").on("click","#picture_select",function(){
			let src=$(this).attr("path");
			// 显示删除图标
			$picture.next(".picture_bar").children("span").addClass("usable");
			// 改变当前显示的图片
			$picture.children("img").attr("src",src);
			// 关闭图片空间面板
			$(".picture_select").remove();
		})
	})
	// 点击[图片区域]图片右上角的删除图标
	$("body").off("click",".picture_bar span.usable")
		.on("click",".picture_bar span.usable",function(){
		// 隐藏删除图标，改变当前显示的图片
		$(this).removeClass("usable").parent(".picture_bar").prev(".picture_group")
			.children("img").attr("src","img/icons/addImg.png");
	})
	// 视频的增删改
	// 点击[视频区域]的视频，展示视频空间面板
	$("body").on("click","#video_1",function(){
		openImageManager("video_select",2);
		$video=$(this);
		// 点击[确定]按钮
		$("body").off("click","#video_select").on("click","#video_select",function(){
			let src=$(this).attr("path");
			// 显示删除图标
			$video.next(".video_bar").children("span").addClass("usable");
			// 判断当前区域显示的是初始图片还是视频
			if ($video.children("img").length == 0) {
				// 若是视频，则替换视频源，展示选择的视频
				$video.children("video").attr("src",src);
			}else{
				// 若是图片，移除显示的图片，展示选择的视频
				$video.children("img").remove();
				$video.append("<video src="+src+"></video>");
			}
			// 关闭视频空间面板
			$(".video_select").remove();
		})
	})
	// 点击[视频区域]视频右上角的删除图标
	$("body").on("click",".video_bar span.usable",function(){
		// 隐藏删除图标
		$(this).removeClass("usable");
		$videoGroup = $(this).parent(".video_bar").prev(".video_group");
		// 移除视频
		$videoGroup.children("video").remove();
		// 展示图片
		$videoGroup.append('<img src="img/icons/icon_videoType.png">');
	})
	// 课程章节的增删改
	// 点击课程章节每一行的[编辑]图标时，编辑该行
	$("body").on("click",".table_action .chapter_edit",function(){
		let $tableBody = $(this).parents(".table_group.table_body");
		let chapterId = $tableBody.attr("id");
		let $chapterContent = $tableBody.children(".chapter_content");
		let $chapterNumber = $tableBody.children(".chapter_number").children(".number_value");
		let $chapterName = $chapterContent.children(".chapter_name");
		let $chapterDate = $chapterContent.find(".chapter_date");
		let $chapterBegin = $chapterContent.find(".chapter_begin");
		let $chapterEnd = $chapterContent.find(".chapter_end");
		let $chapterLength = $chapterContent.find(".chapter_length").children(".length_value");
		let param = {
				chapterName: $chapterName.text(),
				chapterDate: $chapterDate.text(),
				chapterBegin: $chapterBegin.text(),
				chapterEnd: $chapterEnd.text(),
				chapterLength: $chapterLength.text()
		}
		initializeChapterModal( $chapterNumber.text() , param);
		$("#chapter_modal").modal("show");
		// 点击[确定]按钮
		$(".chapter_buttons .btn-success").off("click").on("click",function(){
			if ($("#chapter_form").valid()) {
				if (typeof chapterId != "undefined") {
					pushIntoUpdateCourseTimetableArray(updateCourseTimetableArray, {
						chapterId : chapterId,
						chapterName: $("#chapter_name").val(),
						chapterDate: $("#chapter_date").val(),
						chapterBegin: $("#chapter_begin").timespinner("getValue"),
						chapterEnd: $("#chapter_end").timespinner("getValue"),
						chapterLength: $("#chapter_length").numberspinner("getValue")
					})
				}
				$chapterName.text($("#chapter_name").val());
				$chapterDate.text($("#chapter_date").val());
				$chapterBegin.text($("#chapter_begin").timespinner("getValue"));
				$chapterEnd.text($("#chapter_end").timespinner("getValue"));
				$chapterLength.text($("#chapter_length").numberspinner("getValue"));
				$("#chapter_modal").modal("hide");
			}
		})
	})
	// 点击课程章节每一行的[删除]图标时，删除该行
	$("body").on("click",".table_action .chapter_delete",function(){
		$(this).parents(".table_group.table_body").remove();
		let chapterId = $(this).parents(".table_group.table_body").attr("id");
		if (typeof chapterId != "undefined") {
			deleteCourseTimetableArray.push(chapterId);
		}
	})
	// 点击[新增课程章节]按钮
	$("body").on("click",".chapter_add",function(){
		let chapterNum = $(".course_timetable .table_body").length + 1;
		initializeChapterModal(chapterNum);
		$("#chapter_modal").modal("show");
		// 点击[确定]按钮
		$(".chapter_buttons .btn-success").off("click").on("click",function(){
			let chapterName = $("#chapter_name").val();
			let chapterDate = $("#chapter_date").val();
			let chapterBegin = $("#chapter_begin").timespinner("getValue");
			let chapterEnd = $("#chapter_end").timespinner("getValue");
			let chapterLength = $("#chapter_length").numberspinner("getValue");
			if ($("#chapter_form").valid()) {
				let html = `
				<div class="table_group table_body">
					<div class="chapter_number">
						第<span class="number_value">${chapterNum}</span>讲
					</div>
					<div class="chapter_content">
						<div class="chapter_name">${chapterName}</div>
						<div class="chapter_time">
							<span class="chapter_datetime"><span class="chapter_date">${chapterDate}</span>
								<span class="chapter_begin">${chapterBegin}</span><span>-</span><span
								class="chapter_end">${chapterEnd}</span> </span><span class="chapter_length"><span
								class="length_value">${chapterLength}</span>课时 </span>
						</div>
					</div>
					<div class="table_action">
						<div class="chapter_edit">&#xe906编辑</div>
						<div class="chapter_delete">&#xe9ad删除</div>
					</div>
				</div>`;
				$(".table_foot").before(html);
				// 关闭窗口
				$("#chapter_modal").modal("hide");
			}
		})
	})
	// 点击[提交课程信息]按钮
	$("body").off("click",".float_button_container .btn-primary")
		.on("click",".float_button_container .btn-primary",function(){
		$("textarea[name=course_description]").val(txtEditor.getContent());
		let basicInfoEntity={};// 保存基本信息的对象
		let packageArray=[];// 保存套餐信息的数组
		let pictureArray=[];// 保存课程图片信息的数组
		let videoArray=[];// 保存课程视频信息的数组
		let courseTimetable=[];// 保存课程安排的数组
		// 将基本信息全部保存到basicInfoEntity中
		basicInfoEntity.courseName=$("#course_name").val().trim();
		let tempArray = $("#third_sort").combobox("getValues");
		basicInfoEntity.courseSort= JSON.stringify(tempArray);
		basicInfoEntity.chargingMode=$("#charging_mode").val();
		basicInfoEntity.courseTeacher=$("#course_teacher").val().trim();
		basicInfoEntity.courseDescription=txtEditor.getContent();
		// 将套餐信息保存到packageArray中
		$(".package_body").each(function(index){
			let $packageBody = $(this);
			let $packageName = $packageBody.children(".package_name").children(".show_area");
			let $packagePrice = $packageBody.children(".package_price").children(".show_area");
			let $courseLength = $packageBody.children(".course_length").children(".show_area");
			let obj = {
					packageName: $packageName.text(),
					packagePrice: $packagePrice.text(),
					courseLength: $courseLength.text()=="无"?0:$courseLength.text()
			}
			packageArray.push(obj);
			if (typeof $packageBody.attr("id") == "undefined") {
				insertPackageArray.push(obj);
			}
		})
		// 将图片信息保存到pictureArray中
		$(".picture_bar span.usable").each(function(index){
			pictureArray.push({
				index: index + 1,
				picture: $(this).parent(".picture_bar").prev(".picture_group").children("img").attr("src"),
			})
		})
		// 将视频信息保存到videoArray中
		$(".video_bar span.usable").each(function(index){
			videoArray.push({
				index: index + 1,
				video: $(this).parent(".video_bar").prev(".video_group").children("video").attr("src"),
			})
		})
		// 将课程安排信息保存到courseTimetable中
		$(".table_body").each(function(index){
			let $tableBody = $(this);
			let $chapterContent = $tableBody.children(".chapter_content");
			let $chapterNumber = $tableBody.children(".chapter_number").children(".number_value");
			let $chapterName = $chapterContent.children(".chapter_name");
			let $chapterDate = $chapterContent.find(".chapter_date");
			let $chapterBegin = $chapterContent.find(".chapter_begin");
			let $chapterEnd = $chapterContent.find(".chapter_end");
			let $chapterLength = $chapterContent.find(".chapter_length").children(".length_value");
			let obj = {
					chapterNumber: $chapterNumber.text(),
					chapterName: $chapterName.text(),
					chapterDate: $chapterDate.text(),
					chapterBegin: $chapterBegin.text(),
					chapterEnd: $chapterEnd.text(),
					chapterLength: $chapterLength.text()
			}
			courseTimetable.push(obj);
			if (typeof $tableBody.attr("id") == "undefined") {
				insertCourseTimetableArray.push(obj);
			}
		})
		let basicInfoIsPassed=$(".basic_info").valid();// 基本信息是否通过验证的标识
		let pictureIsPassed= true;// 是否选择了至少一张图片的标识
		let pricingPackageIsPassed = true;// 是否添加了至少一个套餐的标识
		if (pictureArray.length == 0) {
			// 没有选择图片
			pictureIsPassed = false;
			// 没有错误提示时，显示错误提示
			if($(".picture_container label.error").length==0){
				$(".picture_container").append("<label class='error'>缺少图片</label>")
			}
		}else{
			// 选择图片了，将错误提示移除
			$(".picture_container label.error").remove();
		}
		if (packageArray.length == 0) {
			// 没有添加套餐
			pricingPackageIsPassed = false;
			// 若没有错误提示，显示错误提示
			if ($(".pricing_package>.error").length == 0) {
				$(".pricing_package").append("<label class='error'>缺少课程套餐</label>");
			}
		}else{
			// 添加了，移除错误提示
			$(".pricing_package>.error").remove();
		}
		if (basicInfoIsPassed && pictureIsPassed && pricingPackageIsPassed) {
			let textArray=[];
			textArray.push(basicInfoEntity.courseName);
			textArray.push(basicInfoEntity.courseTeacher);
			for (let i = 0; i < packageArray.length; i++) {
				textArray.push(packageArray[i].packageName);
			}
			for (let i = 0; i < courseTimetable.length; i++) {
				textArray.push(courseTimetable[i].chapterName);
			}
			let $this = $(this);
			isLogin(function(){
				$this.addLoadingAnimation();
				wordFilter(textArray, function(){
					// 通过验证，将课程信息保存到服务器
					if (courseId == "") {
						// 新增课程
						// 获取校区ID数组
						let orgArray = $("#org_select").combobox("getValues");
						$.ajax({
							type: "POST",
							url: "orgCourse/addCourse.action",
							data: {
								basicInfo: JSON.stringify(basicInfoEntity), 
								packageArray: JSON.stringify(packageArray),
								pictureArray: JSON.stringify(pictureArray),
								videoArray: JSON.stringify(videoArray),
								courseTimetable: JSON.stringify(courseTimetable),
								orgArray: JSON.stringify(orgArray)
							},
							success: function(map){
								if (map.code == 600) {
									// 保存成功，显示跳转面板
									$this.removeLoadingAnimation();
									$("#jump_modal").modal("show");
								}
							},
							error: function(){
								// 保存失败，进行提示
								let html = `
								<div id="release_alert" class="alert alert-danger" role="alert">
									<button type="button" class="close" data-dismiss="alert" aria-label="Close">
										<span aria-hidden="true">&times;</span>
									</button>
									<div>保存失败，请重试</div>
								</div>`;
								$("body").append(html);
								$this.removeLoadingAnimation();
							}
							
						});
					}else{
						// 修改课程
						$.ajax({
							type: "POST",
							url: "orgCourse/editCourse.action",
							data: {
								courseId : courseId,
								basicInfo : JSON.stringify(basicInfoEntity), 
								insertPackageArray : JSON.stringify(insertPackageArray),
								deletePackageArray : JSON.stringify(deletePackageArray),
								updatePackageArray : JSON.stringify(updatePackageArray),
								pictureArray : JSON.stringify(pictureArray),
								videoArray : JSON.stringify(videoArray),
								insertCourseTimetableArray : JSON.stringify(insertCourseTimetableArray),
								deleteCourseTimetableArray : JSON.stringify(deleteCourseTimetableArray),
								updateCourseTimetableArray : JSON.stringify(updateCourseTimetableArray)
							},
							success: function(map){
								if (map.code == 600) {
									// 保存成功，显示跳转面板
									$this.removeLoadingAnimation();
									$("#jump_modal .course_details").attr("href","courseDetails.do?courseId="+courseId);
									$("#jump_modal").modal("show");
								}
							},
							error: function(){
								// 保存失败，进行提示
								let html = `
								<div id="release_alert" class="alert alert-danger" role="alert">
									<button type="button" class="close" data-dismiss="alert" aria-label="Close">
										<span aria-hidden="true">&times;</span>
									</button>
									<div>保存失败，请重试</div>
								</div>`;
								$("body").append(html);
								$this.removeLoadingAnimation();
							}
							
						});
					}
					
				} , function(invalidWords){
					// 未通过验证，显示提示
					let html = `
					<div id="release_alert" class="alert alert-danger" role="alert">
						<button type="button" class="close" data-dismiss="alert" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<div>您提交的信息中包含如下敏感词汇，请修改后提交：<strong class="invalid_words">${invalidWords}</strong></div>
					</div>`;
					$("body").append(html);
					$this.removeLoadingAnimation();
				});
			});
		}
	})
	
})