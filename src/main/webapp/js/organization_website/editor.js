
$(document).ready(function() {
/*加载机构二级网站首页内容*/
	//获取当前url,解析出机构id
	let href=window.location.href;
	let start=href.lastIndexOf('/')+1;
	let end =href.lastIndexOf('.');
	let orgId=href.substring(start,end);

	//解析pageName,如果没有参数,则默认是index
	let pageName=href.lastIndexOf('=')+1;
	if (pageName==0) {
		pageName='index';
	}else{
		pageName=href.substring(pageName);
	}

	//所有课程 页面单独处理
	if (pageName!='lession') {
		$.post('orgWebsite/getPages.action', {pageName: pageName}, function(data) {
			if (data.result==0) {
				alert("网络异常,请刷新后重试");
			}else{
				$('.header_content').html(data.header);
				$(".org_content").html(data.page_html);
				
				//加载完网页之后,初始化轮播图
				let $banner_background=$('.banner_background');
				$banner_background.each(function() {
					let id=$(this).children().first().attr('id');
					initSwiper(id);
				});
			}
		});
	}else{
		$('.header_content').html('<div>所有课程页面正在准备中,敬请期待</div>');
	}



	var selectedid;
	var module_id;
	var $hover_layout=$("#hover_layout");
	var $add_content=$("#add_content");
	var $edit_content=$("#edit_content");
	var $choose_style=$("#choose_style");
	var $org_content=$(".org_content");
	let $header_content=$('.header_content');
	// junguo.fan
	var which_mol;
	var $body=$('body');
	var nav_content_array;  //存放dom中，导航栏内容的数组
	var nav_editcontent_array; //编辑面板修改后的内容数组
	var banner_img_array=["img/icon/img5.jpg","img/icon/img6.jpg","img/icon/timg.jpg"];  //全局数组，存放轮播图图片地址。数组有一个初始内容，在编辑内容时更改数组
	//撤销和重做功能所需数组
	var operation_array=new Array();
	//记录撤销和重做之后,数组的索引
	var operation_step_number=0;
	//标记网页是否编辑过
	var editSymbol=0;

	//点击添加按钮，弹窗展示可添加模块
	$('#add_module_btn').click(function() {
		$("#module_list,#hover_layout").fadeIn();
	});
	//点击叉号关闭左侧面板(同时关闭右侧和背景)
	$("#module_list_close").click(function() {
		$("#module_list,#hover_layout,#add_module_panel").fadeOut();
		$(".selected").removeClass('selected');
		//清空面板 by范俊国
		$("#add_content").empty();
	});

	//点击模块列表，展示编辑面板	
		$('.module_list_li').click(function(){
			//改变被选中li的样式
			$(".selected").removeClass('selected');
			$(this).addClass('selected');
			//清空面板数据
			$("#add_content").empty();
			module_id=$(this).attr('id');
			//------Begin---------
			if ("module_logo"==module_id) {
				loadLogoStyle($add_content);
				$add_content.find(".logo_style .style_group:nth-child(1)").addClass("selected");
			}
			if ("module_img_txt"==module_id) {
				loadLayoutStyle($add_content);
				loadImgTxtStyle($add_content);
				$add_content.find(".layout_style .parent .child:nth-child(1) .style_group").addClass("selected");
				$add_content.find(".img_txt_style .parent .child:nth-child(1) .style_group").addClass("selected");
			}
			if ("module_video"==module_id) {
				loadLayoutStyle($add_content);
				loadVideoStyle($add_content);
				$add_content.find(".layout_style .parent .child:nth-child(1) .style_group").addClass("selected");
				$add_content.find(".video_style .parent .child:nth-child(1) .style_group").addClass("selected");
			}
			if (module_id=="module_nav") {
				//which_mol用于判断选了哪个组件。如果选择了导航栏，默认选第一个，所以在这个时候重置
				 which_mol="nav_style1";
				//重置颜色属性
				temp_bgcolor_class="bg_light_blue";
				temp_text_color="#00CCFF";
				colorTab($add_content);
				nav1($add_content);
				nav2($add_content);
		   	}
		   	if (module_id=="module_banner") {
		   		which_mol="banner";
		   		allBanner($add_content);
		   		
		   	}
			//-------End----------
			$("#add_module_panel").fadeIn();
		});
	
	//点击叉号或“取消”按钮，关闭新增组件的面板（右侧）
	$(".add_module_panel_close").click(function() {
		$("#add_module_panel").fadeOut();
		//同时，恢复被选中的li样式
		$(".selected").removeClass('selected');
		//清空面板 by范俊国
		$("#add_content").empty();
	});
	//点击叉号或“取消”按钮，关闭编辑组件的面板（右侧）
	$(".edit_module_panel_close").click(function() {
		$("#edit_module_panel,#hover_layout").fadeOut();
		if(-1!=selectedid.search("logo")){
			//销毁ueditor
			delUEditor("fontEditor");
		}
		if(-1!=selectedid.search("img_txt")){
			//销毁ueditor
			delUEditor("txtEditor");
		}if(-1!=selectedid.search("video")){
			//销毁ueditor
			delUEditor("txtEditor");
		}
	});


//------------------- 【新增组件】点击确定按钮事件：----------------------------
	$("#OK_btn_add").click(function() {
		//获取选择的模块html代码
		var selected_html=$(".selected_border").html();
		//判断是否是导航模块1
		if ("module_nav"==module_id) {
			//获取当前时间，组成id
			let id=selectedid="nav_"+new Date().getTime();
			if (which_mol=='nav_style1') {
				bgcolor_class=temp_bgcolor_class;
				text_color=temp_text_color;
				$org_content.append("<div class='all_used_background nav-editable "+bgcolor_class+"'><div  id='"+id+"' class='editable nav_banner_content'>"+selected_html+"</div></div>");
			}
			else {//导航模块2
				bgcolor_class=temp_bgcolor_class;
				text_color=temp_text_color;
				$org_content.append("<div class='all_used_background nav-editable bg_white'><div id='"+id+"' class='editable nav_banner_content'>"+selected_html+"</div></div>");			
			}	
		}
		//轮播图
		if("module_banner"==module_id){
			//获取所选banner样式的id和时间组成id
			let id=selectedid=$(".selected_border").attr('id')+new Date().getTime();
			$org_content.append("<div class='banner_background'><div id='"+id+"' class='swiper-container banner editable nav_banner_content'>"+selected_html+"</div></div>");
			//往页面里写入banner之后，并且在初始化之前，要增加图片
			var $swiper_img=$('#'+id+' .swiper_img');
			for (var i = 0; i < banner_img_array.length; i++) {
				$swiper_img.eq(i+1).attr('src',banner_img_array[i]);
			}
			//初始化
			initSwiper(id);
		}
		//--------Begin--------
		if ("module_logo"==module_id) {
			selectedid=addModule(module_id,$org_content);
		}
		if ("module_img_txt"==module_id) {
			//判断，选中布局样式和图文样式之后，再进行增加操作
			if ($add_content.find(".style_group.selected").length>1) {
				let layOutId=addModule("module_layout",$org_content);
				selectedid=layOutId;
				$("#"+layOutId).removeClass("editable");
				let length=$("#"+layOutId+" .layout_col").length;
				for (let i = 0; i < length; i++) {
					let j=i;
					$("#"+layOutId+" .layout_col:nth-child("+(j+1)+")").empty();
					addModule(module_id,$("#"+layOutId+" .layout_col:nth-child("+(j+1)+")"));
				}
			}
		}
		if ("module_video"==module_id) {
			//判断，选中布局样式和视频样式之后，再进行增加操作
			if ($add_content.find(".style_group.selected").length>1) {
				let layOutId=addModule("module_layout",$org_content);
				selectedid=layOutId;
				$("#"+layOutId).removeClass("editable");
				let length=$("#"+layOutId+" .layout_col").length;
				for (let i = 0; i < length; i++) {
					let j=i;
					$("#"+layOutId+" .layout_col:nth-child("+(j+1)+")").empty();
					addModule(module_id,$("#"+layOutId+" .layout_col:nth-child("+(j+1)+")"));
				}
			}
		}
		//---------End---------
		//最后关闭弹窗,清除选中样式
		$("#module_list,#hover_layout,#add_module_panel").fadeOut();
		$(".selected").removeClass('selected');
		//清空面板 by范俊国
		$("#add_content").empty();
		recordOperation('add');
		//修改是否编辑的标志
		editSymbol=1;
	});
	
	//鼠标滑过每个组件div，显示编辑和删除按钮,并且显示虚线边框
	//全局变量
	$body.off('mouseenter','.editable').on('mouseenter','.editable',function() {
		let $this=$(this);
		selectedid=$this.attr("id");
		if (selectedid.search('nav')==-1) {
			$this.addClass('selected');
		}
		$this.css('position', 'relative');
		
		//判断logo和nav两个模块,如果是这两个模块,只显示编辑按钮,不能删除
		let buttonGroupHtml;
		if (selectedid.search('nav')!=-1||selectedid.search('logo')!=-1) {
			buttonGroupHtml='<div class="edit_button_group">\
				<div class="edit_button bg_white">编辑模块</div>\
				</div>';
		}else{
			buttonGroupHtml='<div class="edit_button_group">\
				<div class="edit_button bg_white">编辑模块</div>\
				<div class="del_button bg_white">删除模块</div></div>';
		}
		$this.append(buttonGroupHtml);
	});

	$("body").undelegate(".editable", 'mouseleave').delegate(".editable", 'mouseleave', function() {
		$(this).css('position', '').removeClass("selected");//此处做了修改		
		$(".edit_button_group").remove();
	});


/*上移/下移:为了兼容视频和图文模块,这两个模块可能会有多个editable类.这样会导致同一个模块出现多个按钮. 所以上移和下移按钮统一放在最外层容器中
		给最外层的容器绑定鼠标移入事件
*/
	let thisModule;
	$body.off('mouseenter', '.all_used_background,.banner_background').on('mouseenter', '.all_used_background,.banner_background', function() {
		//先判断是否是最外层的all_used_background. 只在最外层写入按钮
		let parentClassName=$(this).parent().attr('class');
		if (parentClassName=='org_content') {
			thisModule=$(this);

			//判断第一个模块和最后一个模块
			let prev=thisModule.prev();
			let next=thisModule.next();

			let removeBtnHtml='';
			if (prev.length==0&&next.length!=0) {
				removeBtnHtml='<div class="remove_bg">\
				<div class="module_remove_down bg_white"><img src="img/icon/icon_remove_down.png">下移</div>\
				</div>';
			}else if (next.length==0&&prev.length!=0) {
				removeBtnHtml='<div class="remove_bg">\
				<div class="module_remove_up bg_white"><img src="img/icon/icon_remove_up.png">上移</div>\
				</div>';
			}else if(prev.length==0&&next.length==0){
				
			}else{
				removeBtnHtml='<div class="remove_bg">\
				<div class="module_remove_up bg_white"><img src="img/icon/icon_remove_up.png">上移</div>\
				<div class="module_remove_down bg_white"><img src="img/icon/icon_remove_down.png">下移</div>\
				</div>';
			}

			$(this).append(removeBtnHtml);
		}
		

	});
	//鼠标移出,移除按钮
	$body.off('mouseleave', '.all_used_background,.banner_background').on('mouseleave', '.all_used_background,.banner_background', function() {
		let parentClassName=$(this).parent().attr('class');
		if (parentClassName=='org_content') {
			$('.remove_bg').remove();
		}
	});





	 /*[特殊处理]导航栏虚线外框:由于是两个人一起做的,逻辑不太一样.导航栏模块的虚线外框不同于其他模块,所以单独加了个class:nav-editable
		当需要保存整个页面时,也需要把这个class连同editable一起处理下*/
	$body.off('mouseenter', '.nav-editable').on('mouseenter', '.nav-editable', function() {
		$(this).css('border', 'dashed #3379b7 1px');
	});
	$body.off('mouseleave', '.nav-editable').on('mouseleave', '.nav-editable', function() {
		$(this).css('border', '');
	});

	$("body").on({
		mouseenter:function(){
			let layoutDelBtn="<div class='layout_del_btn' data-toggle='tooltip' data-placement='right' title='删除该布局' ><img src='img/icon/remove_red1.png'></div>";
			$(this).css('position', 'relative');
			$(this).append(layoutDelBtn);
			$(this).find(".layout_container").addClass("selected");
		},
		mouseleave:function(){
			$(this).css('position', '');
			$(this).find(".layout_del_btn").remove();
			$(this).find(".layout_container").removeClass("selected");
		},
	},".layout_background");
	$("body").on({
		mouseenter:function(){
			$(this).attr("src","img/icon/remove_red.png");
		},
		mouseleave:function(){
			$(this).attr("src","img/icon/remove_red1.png");
		},
		click:function(){
			selectedid=$(this).parents(".layout_background").attr("id");
			$("#del_module_panel").fadeIn();
			$hover_layout.fadeIn();
		}
	},".layout_del_btn img");
	
//------------编辑组件部分------------------
	//上移模块
	$body.off('click', '.module_remove_up').on('click', '.module_remove_up', function() {
		let length=$(this).parents('.all_used_background').next().length;
		thisModule.insertBefore(thisModule.prev());
		editSymbol=1;
		let id=$(this).parent().prev().attr('id');
		recordOperation('up',id);
		//判断边界条件:如果是最后一个模块,在操作完成之后,写入下移按钮.原因:如果模块高度足够大,在移动之后,鼠标没有离开模块,导致仍然之后一个按钮
		if(length==0){
			$(this).after('<div class="module_remove_down bg_white"><img src="img/icon/icon_remove_down.png">下移</div>');
		}

	});
	//下移模块
	$body.off('click', '.module_remove_down').on('click', '.module_remove_down', function() {
		let length=$(this).parents('.all_used_background').prev().length;
		thisModule.insertAfter(thisModule.next());
		editSymbol=1;
		let id=$(this).parent().prev().attr('id');
		recordOperation('down',id);
		if (length==0) {
			$(this).before('<div class="module_remove_up bg_white"><img src="img/icon/icon_remove_up.png">上移</div>');
		}
	});

	//点击编辑，显示编辑面板
	$body.off('click','.edit_button').on('click','.edit_button',function() {
		//首先清空面板内容
		$choose_style.empty();
		$edit_content.empty();
		if(-1!=selectedid.search("logo")){
			loadLogoEditPanel();
		}
		if(-1!=selectedid.search("img_txt")){
			loadImgTxtEditPanel();
		}
		if (-1!=selectedid.search("video")) {
			loadVideoEditPanel();
		}
		//编辑导航栏
		if (selectedid.search("nav")!=-1) {
			//每次打开面板，重置which_mol
			which_mol="nav_style1";
			//获取当前模块的背景颜色
			let theClass=$("#"+selectedid).parent().attr("class");
			var selected_module_color_class=theClass.substring(theClass.lastIndexOf(' ')+1);
			
			if ('bg_white'==selected_module_color_class) {
				selected_module_color_class="bg_light_blue";
			} 
			initial_color=selected_module_color_class;
			colorTab($choose_style);
			nav1($choose_style);
			nav2($choose_style);

		//【编辑内容】
			//重置数组
			nav_content_array=new Array();
			var a_array=$("#"+selectedid+" .get_innerHtml");
			for (let i = 2; i < a_array.length; i++) {
				let id=$(a_array[i]).attr('id');
				nav_content_array.push({id:id,name:a_array[i].innerHTML});
			}
			nav_content();
			$("#choose_style>#nav_style1").children('.nav_background').attr('class', 'nav_background '+selected_module_color_class);
		}
		//编辑轮播图
		if (selectedid.search("banner")!=-1) {
		//【选择样式】
			//面板展示加载所有样式
			allBanner($choose_style);
		//【编辑内容】
			banner_content();
		}

		$("#edit_module_panel,#hover_layout").fadeIn();
	});

	//------【编辑组件】点击确定按钮事件：-----------
	$("#OK_btn_edit").click(function(){

		recordOperation('edit');

		//判断用户选择了什么，修改class
		if(-1!=selectedid.search("logo")){
			changeLogo(selectedid);
		}
		if(-1!=selectedid.search("img_txt")){
			changeImgTxt(selectedid);
		}
		if(-1!=selectedid.search("video")){
			changeVideo(selectedid);
		}
		if (selectedid.search("nav")!=-1) {
			//修改样式
			text_color=temp_text_color;
			bgcolor_class=initial_color;
			//获取选择的模块html代码
			var selected_html=$("#choose_style>.selected_border").html();
			$("#"+selectedid).html(selected_html);
			//改变最外层div的背景色
			if (which_mol=='nav_style2') {
				$("#"+selectedid).parent().attr('class', 'all_used_background nav-editable bg_white');
			}else{
				$("#"+selectedid).parent().attr('class', 'all_used_background nav-editable '+bgcolor_class);
			}
			//导航栏:修改内容
			nav_editcontent_array=new Array();
			var array_for_count=$(".nav_tbody .column1");
			for (var i = 0; i < array_for_count.length; i++) {
				let id=$(array_for_count[i]).attr('id');
				nav_editcontent_array.push({id:id,name:array_for_count[i].innerHTML});
			}

			let classname=$("#"+selectedid).parent().attr("class");
			nav_writeback(classname.substring(classname.lastIndexOf(' ')+1));
		}
		if (selectedid.search("banner")!=-1){
			var newId=$(".selected_border").attr('id')+new Date().getTime();
			//更新操作步骤数组:将修改前的id,赋值给oldId;新的id给thisId
			let thisObject=operation_array[operation_step_number-1];
			thisObject.oldId=thisObject.thisId
			thisObject.thisId=newId;
			//将修改后的图片和链接,分别存入数组.如果链接地址是空,也要存个空字符串
			var array_img=new Array();
				//图片个数
				var img_number=$('.banner_tbody .column1').length;
				var $column1=$('.column1 .banner_edit_img');
				for (var i = 0; i < img_number; i++) {
					array_img.push($column1.eq(i).attr('src'));
				}
				
				var html=banner(newId,array_img);
				$('#'+selectedid).parent().html(html);

			initSwiper(newId);
		}
		$("#edit_module_panel,#hover_layout").fadeOut();
		//修改是否编辑的标志
		editSymbol=1;
	});

//------------删除-------------
	//编辑删除模块，显示删除确认面板
	$("body").undelegate('.del_button', 'click').delegate('.del_button', 'click', function() {
		$("#del_module_panel").fadeIn();
		$hover_layout.fadeIn();
	});
	//确定删除
	$("#OK_btn_del").click(function() {
		recordOperation('del');
		let $selected=$("#"+selectedid);
		if (selectedid.search('img_txt')!=-1||selectedid.search('video')!=-1) {
			$selected.parents('.layout_col').remove();
		} else {
			$selected.parent().remove();
		}
		
		//最后关闭面板
		$("#del_module_panel").fadeOut();
		$hover_layout.fadeOut();
		//修改是否编辑的标志
		editSymbol=1;
	});
	//点击取消按钮，关闭面板
	$(".del_module_panel_close").click(function() {
		$("#del_module_panel").fadeOut();
		$hover_layout.fadeOut();
	});

	//增加模块的面板中，选中选项时外框变色
	$add_content.off("click", ".style_group").on("click", ".style_group", function() {
		$(this).prevAll(".selected").removeClass("selected");
		$(this).nextAll(".selected").removeClass("selected");
		$(this).parent().prevAll().children(".selected").removeClass("selected");
		$(this).parent().nextAll().children(".selected").removeClass("selected");
		$(this).addClass("selected");
	})
	$choose_style.off("click", ".style_group").on("click", ".style_group", function() {
		$(this).prevAll(".selected").removeClass("selected");
		$(this).nextAll(".selected").removeClass("selected");
		$(this).parent().prevAll().children(".selected").removeClass("selected");
		$(this).parent().nextAll().children(".selected").removeClass("selected");
		$(this).addClass("selected");
	})

	//------------添加模块的方法-------------
	
	function loadFontEditor(targetPlace,editorId,initialContent){
		let html="<div id='"+editorId+"'></div>";
		targetPlace.append(html);
		var fontEditor=UE.getEditor(editorId,{
			toolbars:[["bold", "italic", "underline","|","forecolor","fontfamily","fontsize","|","justifyleft","justifycenter","justifyright","|","lineheight","|","undo","redo"]],
			wordCount:false,
			elementPathEnabled:false,
			initialContent:initialContent,
			lineheight:["1","1.5","2","2.5","3","3.5","4","4.5"],
			fontsize:[20,24,28,32,36,40,44],
			initialFrameHeight:100});
		return fontEditor;
	}
	function loadTxtEditor(targetPlace,editorId,initialContent,selectedId){
		let html="<div id='"+editorId+"'></div>";
		targetPlace.append(html);
		//注册背景图片按钮
		UE.registerUI("bgimg",function(editor,uiName){
		    editor.registerCommand(uiName,{
		        execCommand:function(){
		        	let me=$(this.body);
		        	if (me.children(".bgimg").length==0) {
		        		me.wrapInner("<div class='bgimg'></div>");
					}
		        	//打开图片管理器，更换图片
		        	$("body").loadImageManager("change_bgimg",1);
					$("body").on("click","#change_bgimg",function(){
							let src=$(this).attr("path");
							me.children(".bgimg").css("background","url('"+src+"') no-repeat center");
							$(".change_bgimg").remove();
					});
		        }
		    });
		    var btn = new UE.ui.Button({
		        name:uiName,
		        title:"背景图片",
		        cssRules :"background-position: -380px 0;",
		        onclick:function () {
		           editor.execCommand(uiName);
		        }
		    });

		    editor.addListener("selectionchange", function () {
		        var state = editor.queryCommandState(uiName);
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
		//注册清除背景图片按钮
		UE.registerUI("removebgimg",function(editor,uiName){
		    editor.registerCommand(uiName,{
		        execCommand:function(){
		        	$(this.body).children(".bgimg").removeAttr("style").removeClass("bgimg");
		        }
		    });
		    var btn = new UE.ui.Button({
		        name:uiName,
		        title:"清除背景图片",
		        cssRules :"background-position: -580px 0;",
		        onclick:function () {
		           editor.execCommand(uiName);
		        }
		    });

		    editor.addListener("selectionchange", function () {
		        var state = editor.queryCommandState(uiName);
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
		var txtEditor=UE.getEditor(editorId,{
			initialFrameHeight:150,
			wordCount:true,
			elementPathEnabled:false,
			initialContent:initialContent,
			lineheight:["1","1.5","2","2.5","3","3.5","4","4.5"],
			fontsize:[10,11,12,14,16,18,20,22,24,26,28,32,36,40,44],
			shortcutMenu:["fontfamily", "fontsize", "bold", "italic", "underline", "forecolor", "backcolor", "insertorderedlist", "insertunorderedlist"],
			insertunorderedlist:{circle: '',disc: '',square: ''},
			toolbars:[[
				"fullscreen","preview","source","|","undo","redo","|",
				"customstyle","paragraph","fontfamily","fontsize","|",
				"bold", "italic", "underline","fontborder","strikethrough","superscript","subscript","blockquote","horizontal","cleardoc","|",
				"forecolor","backcolor","insertorderedlist","insertunorderedlist","|",
				"justifyleft","justifycenter","justifyright","directionalityltr","directionalityrtl","indent","rowspacingtop","rowspacingbottom","lineheight","|",
				"inserttable","deletetable","insertparagraphbeforetable","insertrow","deleterow","insertcol","deletecol","mergecells",
				"splittocells","splittorows","splittocols","|","bgimg","removebgimg"]],
			enableContextMenu:false
			});
		
		txtEditor.ready(function(){
			//富文本编辑器渲染完毕后，判断原本的内容是否有背景图。有，将背景图加入富文本编辑器
			//XXX 较好的方式是创建ueditor实例时，注册输入过滤规则 （addInputRule(Function rule)），待日后完善
			let $bgimg=$("#"+selectedId).find(".bgimg");
			if ($bgimg.length) {
				let attr=" ";
				for (let i = 0; i < $bgimg[0].attributes.length; i++) {
					attr += $bgimg[0].attributes[i].nodeName+"='"+$bgimg[0].attributes[i].nodeValue+"' ";
				}
				$(txtEditor.body).wrapInner("<div "+attr+"></div>");
			}
		})
		
		return txtEditor;
	}
	function delUEditor(editorId){
		//避免用户在面板关闭前看到销毁的动画过程
		setTimeout(function() {
			UE.getEditor(editorId).destroy();
		},600);
		
	}
	function showDefaultPanel(){
		$choose_style.empty();
		$edit_content.empty();
	}
	
	function loadLogoEditPanel(){
		showDefaultPanel();
		//加载“选择样式面板”的内容
		loadLogoStyle($choose_style);
		loadImgStyle($choose_style);
		//根据当前样式，设置选项的选中状态
		let logostyle=$("#"+selectedid).children(".logo_container").attr("name");
		let imgstyle=$("#"+selectedid).find(".logo_img").attr("name");
		$choose_style.find("."+logostyle).parent(".style_group").addClass("selected");
		$choose_style.find("."+imgstyle).parent(".style_group").addClass("selected");
		
		//写入“编辑内容面板”的框架
		let html="<div class='change_logo'>"+
					"<p><b>更换Logo</b></p>"+
					"<div></div>"+
				 "</div>"+
				 "<div class='edit_title'>"+
				 	"<p><b>编辑标题</b></p>"+
				 	"<div></div>"+
				 "</div>";
		$edit_content.append(html);
		//图片更换面板
		loadImgChange($(".change_logo div"),$("#"+selectedid).find(".logo_img").attr("src"));
		//编辑标题面板
		fontEditor=loadFontEditor($(".edit_title div"),"fontEditor",$("#"+selectedid).find(".title_href").html());
		//根据新选定的logo样式决定是否启用编辑标题的面板
		$("#myTab li:nth-child(2)").off("click").on("click",function(){
			let newlogostyle=$choose_style.children(".logo_style").find(".selected").children(".logo_container").attr("name");
			if("logo-two"==newlogostyle ){
				$(".edit_title").hide();
			}else{
				$(".edit_title").show();
			}
		});
	}
	function changeLogo(selectedid){
		//更改样式
		let $logo_container=$("#"+selectedid).children(".logo_container");
		let $logo_img=$("#"+selectedid).find(".logo_img");
		let logostyle=$logo_container.attr("name");
		let imgstyle=$logo_img.attr("name");
		let newlogostyle=$choose_style.children(".logo_style").find(".selected").children(".logo_container").attr("name");
		let newimgstyle=$choose_style.children(".img_style").find(".selected").attr("name");
		$logo_container.removeClass(logostyle).addClass(newlogostyle).attr("name",newlogostyle);
		$logo_img.removeClass(imgstyle).addClass(newimgstyle).attr("name",newimgstyle);
		//更改标题内容
		$logo_container.find(".title_href").html(fontEditor.getContent());
		//更改logo图片
		$logo_img.attr("src",$edit_content.find(".change_logo img").attr("src"));
		//销毁编辑器实例，否则不能(对于同一元素)创建新实例
		delUEditor("fontEditor");
	}
	
	function loadImgTxtEditPanel(){
		//初始化编辑面板
		showDefaultPanel();
		//加载图文样式，图片样式
		loadImgTxtStyle($choose_style);
		loadImgStyle($choose_style);
		//根据当前样式，设置选项的选中状态
		let imgTxtStyle=$("#"+selectedid).children(".img_txt_container").attr("name");
		let imgStyle=$("#"+selectedid).find("img").attr("name");
		$choose_style.find("."+imgTxtStyle).parent(".style_group").addClass("selected");
		$choose_style.find("."+imgStyle).parent(".style_group").addClass("selected");
		//根据选定的图文样式决定是否启用图片样式的面板
		if ("img-txt-one"==imgTxtStyle) {
			$(".img_style").hide();
		}
		$choose_style.on("click",".img_txt_container ",function(){
			if ("img-txt-one"==$(this).attr("name")) {
				$(".img_style").hide();
			} else {
				$(".img_style").show();
			}
		})
		//写入“编辑内容面板”的框架
		let html="<div class='change_img'>"+
					"<p><b>更换图片</b></p>"+
					"<div></div>"+
				 "</div>"+
				 "<div class='edit_txt'>"+
				 	"<p><b>编辑文本内容</b></p>"+
				 	"<div></div>"+
				 "</div>";
		$edit_content.append(html);
		loadImgChange($(".change_img div"),$("#"+selectedid).find(".img_cell>img").attr("src"));
		txtEditor=loadTxtEditor($(".edit_txt div"),"txtEditor",$("#"+selectedid).find(".txt_cell").html(),selectedid);
		//根据选定的图文样式决定是否启用更换图片和编辑文本内容的面板
		$("#myTab li:nth-child(2)").on("click",function(){
			let newImgTxtStyle=$choose_style.children(".img_txt_style").find(".selected").children(".img_txt_container").attr("name");
			if("img-txt-one"==newImgTxtStyle ){
				$(".change_img").hide();
			}else{
				$(".change_img").show();
			}
			if("img-txt-two"==newImgTxtStyle ){
				$(".edit_txt").hide();
			}else{
				$(".edit_txt").show();
			}
		});
	}
	function changeImgTxt(selectedid){
		//更改样式
		let $img_txt_container=$("#"+selectedid).children(".img_txt_container");
		let $img=$("#"+selectedid).find(".img_cell>img");
		let imgTxtStyle=$img_txt_container.attr("name");
		let imgStyle=$img.attr("name");
		let newImgTxtStyle=$choose_style.children(".img_txt_style").find(".selected").children(".img_txt_container").attr("name");
		let newImgStyle=$choose_style.children(".img_style").find(".selected").attr("name");
		$img_txt_container.removeClass(imgTxtStyle).addClass(newImgTxtStyle).attr("name",newImgTxtStyle);
		$img.removeClass(imgStyle).addClass(newImgStyle).attr("name",newImgStyle);
		//更改文本内容
		$img_txt_container.find(".txt_cell").html(txtEditor.getContent());
		//更改图片
		$img.attr("src",$edit_content.find(".change_img img").attr("src"));
		//销毁编辑器实例，否则不能(对于同一元素)创建新实例
		delUEditor("txtEditor");
	}
	function loadVideoEditPanel(){
		//初始化编辑面板
		showDefaultPanel();
		//加载视频样式
		loadVideoStyle($choose_style);
		//根据当前样式，设置选项的选中状态
		let videoStyle=$("#"+selectedid).children(".video_container").attr("name");
		$choose_style.find("."+videoStyle).parent(".style_group").addClass("selected");
		//写入“编辑内容面板”的框架
		let html="<div class='change_video'>"+
					"<p><b>更换视频</b></p>"+
					"<div></div>"+
				 "</div>"+
				 "<div class='edit_txt'>"+
				 	"<p><b>编辑文本内容</b></p>"+
				 	"<div></div>"+
				 "</div>";
		$edit_content.append(html);
		loadVideoChange($(".change_video div"),$("#"+selectedid).find(".video_cell>img").attr("src"),$("#"+selectedid).find(".video_cell>video").attr("src"));
		txtEditor=loadTxtEditor($(".edit_txt div"),"txtEditor",$("#"+selectedid).find(".txt_cell").html(),selectedid);
		//根据选定的视频样式决定是否启用编辑文本内容的面板
		$("#myTab li:nth-child(2)").on("click",function(){
			let newVideoStyle=$choose_style.children(".video_style").find(".selected").children(".video_container").attr("name");
			if("video-one"==newVideoStyle ){
				$(".edit_txt").hide();
			}else{
				$(".edit_txt").show();
			}
		});
	}
	function changeVideo(selectedid){
		//更改样式
		let $video_container=$("#"+selectedid).children(".video_container");
		let $video=$("#"+selectedid).find(".video_cell>video");
		let $img=$("#"+selectedid).find(".video_cell>img");
		let videoStyle=$video_container.attr("name");
		let newVideoStyle=$choose_style.children(".video_style").find(".selected").children(".video_container").attr("name");
		$video_container.removeClass(videoStyle).addClass(newVideoStyle).attr("name",newVideoStyle);
		//更改视频
		$img.remove();
		$video.attr("src",$edit_content.find(".change_video video").attr("src")).show();
		//更改文本内容
		$video_container.find(".txt_cell").html(txtEditor.getContent());
		//销毁编辑器实例，否则不能(对于同一元素)创建新实例
		delUEditor("txtEditor");
	}
//-----------范俊国------------------
	
	//点击选择模块的时候，切换边框样式
	$add_content.delegate('.border', 'click', function() {
		// $(".selected_border").attr('class', 'border');
		// $(this).attr('class', 'selected_border');
		$(".selected_border").addClass('border');
		$(".selected_border").removeClass('selected_border');
		$(this).addClass('selected_border');
		$(this).removeClass('border');
		which_mol=$(".selected_border").attr("id");
	});
	$choose_style.delegate('.border', 'click', function() {
		$(".selected_border").addClass('border');
		$(".selected_border").removeClass('selected_border');
		$(this).addClass('selected_border');
		which_mol=$(".selected_border").attr("id");
	});
	//选择颜色，更改导航栏背景颜色以及字体颜色
		var bgcolor_class="bg_light_blue";
		var text_color="#00CCFF";
		//临时存储颜色
		var temp_bgcolor_class="bg_light_blue";
		var temp_text_color="#00CCFF";
		//编辑导航栏时，导航栏初始颜色
		var	initial_color;
		$("#add_content,#choose_style").delegate('.colorTab', 'click', function() {
			//获取背景颜色和字体颜色
			initial_color=temp_bgcolor_class=$(this).attr("class").substring(9);
			temp_text_color=$(this).css("background-color");			
			$("#nav_style1>.nav_background").attr('class', 'nav_background '+temp_bgcolor_class);
		});
//以下四个方法，用于改变“无背景”样式导航栏的hover样式和focus样式
		$("body").delegate('.li_nav', 'mouseenter', function() {
		 	$(this).css('color', temp_text_color);
		});
		$("body").delegate('.li_nav', 'mouseleave', function() {
			$(this).css('color', '#000');
		});
		//获得焦点时，改变样式
		$("body").delegate('.li_nav', 'focus', function() {
			$(this).addClass('focused');
			$(this).css('border-bottom-color', temp_text_color);
		});
		$("body").delegate('.li_nav', 'blur', function() {
			$(this).removeClass('focused');
		});
	
//以下几个方法，用于在“编辑内容”面板点击按钮（导航栏和轮播图）
		//编辑按钮（需要判断是编辑导航栏还是轮播图）
		$edit_content.off('click','.icon_edit').on('click','.icon_edit',function(){
			if (selectedid.search('nav')!=-1) {//如果是导航栏
				var $th=$(this).parent().prev();
				var name=$th.html();
				$th.html("<input class='input_edit_panel' type='text' value='"+name+"'/>");
				//让文本框自动获得焦点
				$('.input_edit_panel').focus();
			} 
			if(selectedid.search('banner')!=-1) {//如果是轮播图
				//找个该条数据的img元素
				let $this_banner_img=$(this).parent().prevAll('.column1').find('img');
				$edit_content.loadImageManager('getBannerImg_fjg',1);
				/*在图片管理器面板,选中图片,点击确定时:
				  1.获取图片地址,并且替换原图片地址;
				  2.关闭图片管理器面板*/
				$('body').off('click', '#getBannerImg_fjg').on('click', '#getBannerImg_fjg', function() {
					$this_banner_img.attr('src', $(this).attr('path'));
					$('.getBannerImg_fjg').remove();
				});
			}	
		});

		//输入框失去焦点时，修改后的内容覆盖原内容
 		$edit_content.off('blur', '.input_edit_panel').on('blur', '.input_edit_panel', function() {
 			$(this).parent().text($(this).val());
 		});

	 	//删除按钮
	 	$edit_content.off('click', '.icon_del').on('click', '.icon_del', function() {
	 		let $this=$(this);
	 		if (selectedid.search('nav')!=-1) {
	 			let id=$this.parent().prev().attr('id');
	 			let content=$this.parent().prev().text();
	 			$('.backup').append('<tr><td class="column1" id="'+id+'">'+content+'</td>\
	 					<td class="column2"><div class="icon_add"></div></td><td></td></tr>');
	 		}

	 		$this.parents("tr").remove();
	 		//删除完成之后，对当前的第一个和最后一个元素，处理排序箭头
	 		//导航栏和轮播图没有差别，所以在这里不做区分
	 		var $column3=$('.column3');
	 			$column3.eq(0).children('.icon_up').remove();
	 			$column3.last().children('.icon_down').remove();
	 	});

	 	//增加按钮
	 	$edit_content.off('click', '.icon_add').on('click', '.icon_add', function() {
	 		let $tr=$(this).parents('tr');
	 		let target=$(this).parent().prev();
	 		let id=target.attr('id');
	 		let content=target.text();

	 		$('.nav_tbody tr').last().children('.column3').html('<div class="icon_down"></div><div class="icon_up">');
	 			$('.nav_tbody').append('<tr><td id="'+id+'" class="column1">'+content+'</td><td class="column2"><div class="icon_edit"></div><div class="icon_del"></div></td>\
	 			<td class="column3"><div class="icon_up"></div></td></tr>');

	 		$tr.remove();

	 		let count=$('#edit_content .column3').length;
	 		if (count<3) {
	 			$('#edit_content .column3').first().children('.icon_up').remove();
	 		}
	 	});

	 	//上移按钮(不止要换名字,还要换id)
	 	$edit_content.off('click', '.icon_up').on('click', '.icon_up', function() {
	 		if (selectedid.search('nav')!=-1) {
		 			let obj_this=$(this).parent().prev().prev();
		 			let obj_prev=$(this).parents("tr").prev().children().first();

		 			let temp_data=obj_this.html();
		 			let id=obj_this.attr('id');

		 			obj_this.html(obj_prev.html());
		 			obj_this.attr('id', obj_prev.attr('id'));

		 			obj_prev.html(temp_data);
		 			obj_prev.attr('id',id);
	 		} else {
	 			//本行
	 			var $this_p=$(this).parent();
		 			var $this_td1=$this_p.prevAll('.column1');
		 			var $this_td2=$this_p.prevAll('.column2');
		 		//上一行
	 			var $tr_prev=$(this).parents('tr').prev();
		 			var $prev_td1=$tr_prev.children('.column1');
		 			var $prev_td2=$tr_prev.children('.column2');
		 			
		 		//本行内容	
		 		var this_column1_html=$this_td1.html();
		 		var this_column2_html=$this_td2.html();
		 		//内容替换
		 		$this_td1.html($prev_td1.html());
		 		$this_td2.html($prev_td2.html());
		 		
		 		$prev_td1.html(this_column1_html);
		 		$prev_td2.html(this_column2_html);

	 		}
	 		
	 	});
	 	//下移按钮
	 	$edit_content.on('click', '.icon_down', function() {
	 		if (selectedid.search('nav')!=-1) {
		 		let obj_this=$(this).parent().prev().prev();
		 		let obj_prev=$(this).parents("tr").next().children().first();

		 		let temp_data=obj_this.html();
		 		let id=obj_this.attr('id');

		 		obj_this.attr('id', obj_prev.attr('id')).html(obj_prev.html());
		 		obj_prev.attr('id', id).html(temp_data);	 
		 		
	 		} else {
	 			//本行
	 			var $this_p=$(this).parent();
		 			var $this_td1=$this_p.prevAll('.column1');
		 			var $this_td2=$this_p.prevAll('.column2');
		 		//本行内容	
		 		var this_column1_html=$this_td1.html();
		 		var this_column2_html=$this_td2.html();
		 		//下一行
		 		var $tr_next=$(this).parents('tr').next();
		 			 var $next_td1=$tr_next.children('.column1');
		 			 var $next_td2=$tr_next.children('.column2');
		 		//内容替换
		 		$this_td1.html($next_td1.html());
		 		$this_td2.html($next_td2.html());
		 		$next_td1.html(this_column1_html);
		 		$next_td2.html(this_column2_html);

	 		}
	 	});
	 	//轮播图编辑:新增按钮
	 	$edit_content.on('click', '#btn_add_new_item', function() {

	 		//首先，要给新增之前的最后一个元素加上“下移”按钮
	 		$('.banner_tbody tr').last().children('.column3').html('<div class="icon_down"></div><div class="icon_up">');
	 		//然后，新增
	 		$('.banner_tbody').append('<tr>\
				<td class="column1"><img class="banner_edit_img" src="img/icon/timg.jpg"></td>\
				<td class="column3"></div><div class="icon_up"></div></td>\
				<td class="column4"><div class="icon_edit"></div><div class="icon_del"></div></td>\
				</tr>');

	 		let count=$('#edit_content .column3').length;
	 		if (count<3) {
	 			$('#edit_content .column3').first().children('.icon_up').remove();
	 		}
	 	});

	 	
	
//------------添加模块的方法-------------


	//颜色选择按钮
	function colorTab(content_id){
		//颜色选择按钮样式
		let choose_color='<div class="choose_color">\
					<button class="colorTab bg_red"></button>\
					<button class="colorTab bg_orange"></button>\
					<button class="colorTab bg_yellow"></button>\
					<button class="colorTab bg_green"></button>\
					<button class="colorTab bg_blue"></button>\
					<button class="colorTab bg_light_blue"></button>\
					<button class="colorTab bg_purple"></button>\
					<button class="colorTab bg_gray"></button>\
					<button class="colorTab bg_light_gray"></button>\
					<button class="colorTab bg_black"></button>\
				</div>';
		content_id.append(choose_color);
	}
	//模块：添加导航栏
	function nav1(content_id){
		var div='<div id="nav_style1" class="selected_border">\
		<div class="nav_background bg_light_blue"><ul class="nav navbar-nav">\
			<li class="active"><a class="get_innerHtml li_nav2 text_white" href="javascript:void(0)" id="index" >首页</a></li>\
			<li><a id="lession" class="get_innerHtml li_nav2 text_white" href="javascript:void(0)">所有课程</a></li>\
			<li><a id="school" class="get_innerHtml li_nav2 text_white" href="javascript:void(0)">学校风采</a></li>\
			<li><a id="teacher" class="get_innerHtml li_nav2 text_white" href="javascript:void(0)">名师介绍</a></li>\
			<li><a id="recruit" class="get_innerHtml li_nav2 text_white" href="javascript:void(0)">招生信息</a></li>\
		</ul>\
		</div>\
		</div>';
		content_id.append(div);

	}
	function nav2(content_id){
		var div='<div id="nav_style2" class="border">\
		<div class="nav_background bg_white"><ul class="nav navbar-nav">\
			<li class="active"><a class="get_innerHtml li_nav text_black" href="javascript:void(0)" id="index">首页</a></li>\
			<li><a id="lession" class="get_innerHtml li_nav text_black" href="javascript:void(0)">所有课程</a></li>\
			<li><a id="school" class="get_innerHtml li_nav text_black" href="javascript:void(0)">学校风采</a></li>\
			<li><a id="teacher" class="get_innerHtml li_nav text_black" href="javascript:void(0)">名师介绍</a></li>\
			<li><a id="recruit" class="get_innerHtml li_nav text_black" href="javascript:void(0)">招生信息</a></li>\
		</ul>\
		</div>\
		</div>';
		content_id.append(div);
	}

	//导航栏内容编辑:将导航栏内容写入编辑面板
	function nav_content(){
		let div_static='<table class="table table-hover for_static_width">\
			<thead>\
				<tr>\
					<th>名称</th>\
					<th>编辑</th>\
					<th>排序</th>\
				</tr>\
			</thead>\
			<tbody class="nav_tbody"><tr>\
				<td>首页</td>\
				<td></td>\
				<td></td>\
			</tr>\
			<tr>\
				<td>所有课程</td>\
				<td></td>\
				<td></td>\
			</tr></tbody></table>\
			<table class="table backup for_static_width"></table>';
		$edit_content.html(div_static);	
		if (nav_content_array.length>0) {
			let div='';
			if (nav_content_array.length==1) {
				div='<tr><td class="column1" id="'+nav_content_array[0].id+'">'+nav_content_array[0].name+'</td><td class="column2"><div class="icon_edit"></div><div class="icon_del"></div></td>\
				<td class="column3"></td></tr>';
			}else{
				//同轮播图，以下变量div分成了三部分，是为了单独处理第一个（没有上移按钮）和最后一个（没有下移按钮）
				div='<tr><td class="column1" id="'+nav_content_array[0].id+'">'+nav_content_array[0].name+'</td><td class="column2"><div class="icon_edit"></div><div class="icon_del"></div></td>\
					<td class="column3"><div class="icon_down"></div></td></tr>';
				for (let i = 1; i < nav_content_array.length-1; i++) {
					div=div+'<tr><td class="column1" id="'+nav_content_array[i].id+'">'+nav_content_array[i].name+'</td><td class="column2"><div class="icon_edit"></div><div class="icon_del"></div></td>\
					<td class="column3"><div class="icon_up"></div><div class="icon_down"></div></td></tr>'
				}
				let jj=nav_content_array.length-1;
				div=div+'<tr><td class="column1" id="'+nav_content_array[jj].id+'">'+nav_content_array[jj].name+'</td><td class="column2"><div class="icon_edit"></div><div class="icon_del"></div></td>\
					<td class="column3"><div class="icon_up"></div></td></tr>';
			}
			$('.nav_tbody').append(div);
		}

		if (nav_content_array.length<3) {
			let compareArray=[{id:'school',name:'学校风采'},{id:'teacher',name:'名师介绍'},{id:'recruit',name:'招生信息'}];
			for (var i = 0; i < compareArray.length; i++) {
				for (var k = 0; k < nav_content_array.length; k++) {
					if (nav_content_array[k].id==compareArray[i].id) {
						compareArray.splice(i,1);
					}
				}
			}

			for (var i = 0; i < compareArray.length; i++) {
				$('.backup').append('<tr><td class="column1" id="'+compareArray[i].id+'">'+compareArray[i].name+'</td>\
	 					<td class="column2"><div class="icon_add"></div></td><td></td></tr>');
			}
		}
	}
	
	
	//方法：修改之后的导航栏，写回到html
	function nav_writeback(classname){
		let html;
		//判断是哪种导航栏样式
		if (classname=='bg_white') {
			html='<li class="active"><a class="get_innerHtml li_nav text_black" href="javascript:void(0)" id="index">首页</a></li>\
				<li><a id="lession" class="get_innerHtml li_nav text_black" href="javascript:void(0)">所有课程</a></li>';
			for (let i = 0; i < nav_editcontent_array.length; i++) {
				html=html+'<li><a id="'+nav_editcontent_array[i].id+'" class="get_innerHtml li_nav text_black" href="javascript:void(0)">'+nav_editcontent_array[i].name+'</a></li>';
			}
		} else {
			html='<li class="active"><a id="index" class="get_innerHtml li_nav2 text_white" href="javascript:void(0)">首页</a></li>\
				<li><a id="lession" class="get_innerHtml li_nav2 text_white" href="javascript:void(0)">所有课程</a></li>';
			for (let i = 0; i < nav_editcontent_array.length; i++) {
				html=html+'<li><a id="'+nav_editcontent_array[i].id+'" class="get_innerHtml li_nav2 text_white" href="javascript:void(0)">'+nav_editcontent_array[i].name+'</a></li>';
			}
		}
		$('#'+selectedid+' .navbar-nav').html(html);
	}
	
	/*
	方法：同时添加所有轮播图，用于在选择样式时，同时展示所有样式
	参数：在哪个弹窗面板添加
	*/
	function allBanner(container){
		var html='<div id="banner1" class="swiper-container myswiper selected_border">\
	    <div class="swiper-wrapper">\
	        <div class="swiper-slide example1"><img class="swiper_img" src=""></div>\
	        <div class="swiper-slide example2"><img class="swiper_img" src=""></div>\
	        <div class="swiper-slide example3"><img class="swiper_img" src=""></div>\
	    </div>\
	    <!-- 如果需要分页器 -->\
	    <div class="swiper-pagination"></div>\
	    <!-- 如果需要导航按钮 -->\
	    <div class="swiper-button-prev"></div>\
	    <div class="swiper-button-next"></div>\
		</div>\
		\
		<div id="banner2" class="swiper-container myswiper border">\
	    <div class="swiper-wrapper">\
	        <div class="swiper-slide example1"><img class="swiper_img" src=""></div>\
	        <div class="swiper-slide example2"><img class="swiper_img" src=""></div>\
	        <div class="swiper-slide example3"><img class="swiper_img" src=""></div>\
	    </div>\
	    <!-- 如果需要分页器 -->\
	    <div class="swiper-pagination"></div>\
	    <!-- 如果需要导航按钮 -->\
	    <div class="swiper-button-prev"></div>\
	    <div class="swiper-button-next"></div>\
		</div>\
		\
		<div id="banner3" class="swiper-container myswiper border">\
	    <div class="swiper-wrapper">\
	        <div class="swiper-slide example1"><img class="swiper_img" src=""></div>\
	        <div class="swiper-slide example2"><img class="swiper_img" src=""></div>\
	        <div class="swiper-slide example3"><img class="swiper_img" src=""></div>\
	    </div>\
	    <!-- 如果需要分页器 -->\
	    <div class="swiper-pagination"></div>\
	    <!-- 如果需要导航按钮 -->\
	    <div class="swiper-button-prev"></div>\
	    <div class="swiper-button-next"></div>\
		</div>\
		\
		<div id="banner4" class="swiper-container myswiper border">\
	    <div class="swiper-wrapper">\
	        <div class="swiper-slide example1"><img class="swiper_img" src=""></div>\
	        <div class="swiper-slide example2"><img class="swiper_img" src=""></div>\
	        <div class="swiper-slide example3"><img class="swiper_img" src=""></div>\
	    </div>\
	    <!-- 如果需要分页器 -->\
	    <div class="swiper-pagination"></div>\
	    <!-- 如果需要导航按钮 -->\
	    <div class="swiper-button-prev"></div>\
	    <div class="swiper-button-next"></div>\
		</div>'

		container.html(html);
		//初始化所有Swiper
		 initAllSwiper();	
	}

	/*
	*	方法:根据传入的参数,添加单个轮播图样式,并写好图片内容和链接地址
	*	return: 返回拼接好的字符串
	*	参数:styleId-新的随机id
	*		array-存放图片地址的数组
	*/
	function banner(styleId,array_img){
		var img_html='';
		for (var i = 0; i < array_img.length; i++) {
			img_html=img_html+'<div class="swiper-slide"><img class="swiper_img" src="'+array_img[i]+'"></div>'
		}
		var html='<div id="'+styleId+'" class="swiper-container banner nav_banner_content editable"><div class="swiper-wrapper">'+
	    img_html+'</div>\
	    <div class="swiper-pagination"></div>\
	    <div class="swiper-button-prev"></div>\
	    <div class="swiper-button-next"></div>\
		</div>';
		return html;
	}
	/*
		方法：轮播图内容编辑:在编辑面板写入内容编辑的功能
	*/
	function banner_content(){
		var $swiper_img=$('#'+selectedid+' .swiper_img');
		var html='';
		if ($swiper_img.length!=0) {
			//从html的轮播图中,取出数据重置数组
			var this_img_array=new Array();
			for (var i = 1; i <$swiper_img.length-1; i++) {
				this_img_array.push($swiper_img.eq(i).attr('src'));
			}
			if ($swiper_img.length==3) {
				html='<tr><td class="column1"><img class="banner_edit_img" src="'+this_img_array[0]+'" ></td>\
					<td class="column3"></td>\
					<td class="column4"><div class="icon_edit"></div><div class="icon_del"></div></td></tr>';
			}else{
				//以下变量html分成了三部分，是为了单独处理第一个（没有上移按钮）和最后一个（没有下移按钮）
				html='<tr><td class="column1"><img class="banner_edit_img" src="'+this_img_array[0]+'" ></td>\
						<td class="column3"><div class="icon_down"></div></td>\
						<td class="column4"><div class="icon_edit"></div><div class="icon_del"></div></td></tr>';
				for (var i = 1; i < this_img_array.length-1; i++) {
					html=html+'<tr><td class="column1"><img class="banner_edit_img" src="'+this_img_array[i]+'" ></td>\
						<td class="column3"><div class="icon_down"></div><div class="icon_up"></div></td>\
						<td class="column4"><div class="icon_edit"></div><div class="icon_del"></div></td></tr>'
				}
				var j=this_img_array.length-1;
				html=html+'<tr><td class="column1"><img class="banner_edit_img" src="'+this_img_array[j]+'" ></td>\
						<td class="column3"><div class="icon_up"></div></td>\
						<td class="column4"><div class="icon_edit"></div><div class="icon_del"></div></td></tr>';
			}

		}
		
		var bannerEditor_html='<div class="remind">*建议上传的图片的尺寸为1190*500</div>'+
			'<table class="table table-hover for_static_width">\
				<thead><tr>\
					<th>图片</th>\
					<th>排序</th>\
					<th>操作</th>\
				</tr></thead>\
				<tbody class="banner_tbody"></tbody>\
			</table>';
		$edit_content.html(bannerEditor_html);
		$('.banner_tbody').html(html);
		var add_new_item='<div id="btn_add_new_item">增加新图片</div>';
		$edit_content.append(add_new_item);
	}
	//方法：分别初始化单个swiper
	function initSwiper(id){
		if (id.search('banner1')!=-1) {
			var swiper11 = new Swiper ('#'+id, {
			    observer:true,//修改swiper自己或子元素时，自动初始化swiper
			    observeParents:true,//修改swiper的父元素时，自动初始化swiper
			    loop: true,
			    autoplay:{
			    	disableOnInteraction: false,
			    },
			    effect:'slide',
			    // 分页器
			   	pagination: {
			      el: '.swiper-pagination',
			    },
			    // 前进后退按钮
			    navigation: {
			      nextEl: '.swiper-button-next',
			      prevEl: '.swiper-button-prev',
			    },
			});
			//禁止鼠标拖动滑动
			swiper11.allowTouchMove= false;
		} else if (id.search('banner2')!=-1) {
			var swiper22=new Swiper('#'+id,{
				observer:true,
				observeParents:true,
				loop: true,
			    autoplay:{
			    	disableOnInteraction: false,
			    },
			    effect:'cube',
			    slideShadows:false,
			    shadow: false,
			    // 分页器
			   	pagination: {
			      el: '.swiper-pagination',
			    },
			    // 前进后退按钮
			    navigation: {
			      nextEl: '.swiper-button-next',
			      prevEl: '.swiper-button-prev',
			    },
			});
			//禁止鼠标拖动滑动
			swiper22.allowTouchMove= false;
			//鼠标悬停,停止轮播;鼠标离开,重新轮播
			// swiper22.el.onmouseover = function(){
			//   	swiper22.autoplay.stop();
			// }
			// swiper22.el.onmouseleave = function(){
			//   	swiper22.autoplay.start();
			// }
		} else if(id.search('banner3')!=-1){
			var swiper33=new Swiper('#'+id,{
				observer:true,
				observeParents:true,
				loop: true,
			    autoplay:{
			    	disableOnInteraction: false,
			    },
			    effect:'flip',
			    // 分页器
			   	pagination: {
			      el: '.swiper-pagination',
			    },
			    // 前进后退按钮
			    navigation: {
			      nextEl: '.swiper-button-next',
			      prevEl: '.swiper-button-prev',
			    },
			});
			//禁止鼠标拖动滑动
			swiper33.allowTouchMove= false;
			//鼠标悬停,停止轮播;鼠标离开,重新轮播
			// swiper33.el.onmouseover = function(){
			//   	swiper33.autoplay.stop();
			// }
			// swiper33.el.onmouseleave = function(){
			//   	swiper33.autoplay.start();
			// }
		} else {
			var swiper44=new Swiper('#'+id,{
				observer:true,
				observeParents:true,
				loop: true,
				direction: 'vertical',
			    autoplay:{
			    	disableOnInteraction: false,
			    },
			    // 分页器
			   	pagination: {
			      el: '.swiper-pagination',
			    },
			    // 前进后退按钮
			    navigation: {
			      nextEl: '.swiper-button-next',
			      prevEl: '.swiper-button-prev',
			    },
			});
			//禁止鼠标拖动滑动
			swiper44.allowTouchMove= false;
			//鼠标悬停,停止轮播;鼠标离开,重新轮播
			// swiper44.el.onmouseover = function(){
			//   	swiper44.autoplay.stop();
			// }
			// swiper44.el.onmouseleave = function(){
			//   	swiper44.autoplay.start();
			// }
		}
	}
	//方法：初始化所有swiper,用于在新增模块时，同时初始化所有的swiper
	function initAllSwiper(){
		var swiper1 = new Swiper ('#banner1', {
		    observer:true,//修改swiper自己或子元素时，自动初始化swiper
		    observeParents:true,//修改swiper的父元素时，自动初始化swiper
		    loop: true,
		    effect:'slide',
		    // 分页器
		   	pagination: {
		      el: '.swiper-pagination',
		    },
		    // 前进后退按钮
		    navigation: {
		      nextEl: '.swiper-button-next',
		      prevEl: '.swiper-button-prev',
		    },
		});
		var swiper2=new Swiper('#banner2',{
			observer:true,
			observeParents:true,
			loop: true,
		    effect:'cube',
		    slideShadows:false,
		    shadow: false,
		   	// 分页器
		   	pagination: {
		      el: '.swiper-pagination',
		    },
		    // 前进后退按钮
		    navigation: {
		      nextEl: '.swiper-button-next',
		      prevEl: '.swiper-button-prev',
		    },
		});
		var swiper3=new Swiper('#banner3',{
			observer:true,
			observeParents:true,
			loop: true,
		    effect:'flip',
		    // 分页器
		   	pagination: {
		      el: '.swiper-pagination',
		    },
		    // 前进后退按钮
		    navigation: {
		      nextEl: '.swiper-button-next',
		      prevEl: '.swiper-button-prev',
		    },
		});
		var swiper4=new Swiper('#banner4',{
			observer:true,
			observeParents:true,
			loop: true,
			direction: 'vertical',
			height:150,
		    // 分页器
		   	pagination: {
		      el: '.swiper-pagination',
		    },
		    // 前进后退按钮
		    navigation: {
		      nextEl: '.swiper-button-next',
		      prevEl: '.swiper-button-prev',
		    },
		});
		//禁止鼠标拖动滑动
		swiper1.allowTouchMove= false;
		swiper2.allowTouchMove= false;
		swiper3.allowTouchMove= false;
		swiper4.allowTouchMove= false;

	}

	//全局变量
	let $editRevoke=$('.edit_revoke');
	let $editRevokeImg=$('.edit_revoke img');
	let $editRedo=$('.edit_redo');
	let $editRedoImg=$('.edit_redo img');
	
	/*
	 * @方法:记录执行的操作,将编辑的操作类型和操作的内容,记录到数组中. 
	 * 操作类型见参数;操作的内容记录的是本次操作之前的内容,也就是说,如果是修改,操作内容就是修改前的内容.
	 * 参数:operation-操作类型:1/add.增加 2/del.删除 3/edit.修改 4/up.上移 5/down.下移;
	 		id:所操作的模块id.由于上移和下移操作,触发的元素是最外层的容器.所以此时的selectid不一定的被操作的模块id.所以需要传入
	 */
	 function recordOperation(operation,paramId){ 
	 	//将撤销按钮变成“可用状态”
	 	if (operation_step_number==0) {
	 		$editRevokeImg.attr('src', 'img/icon/icon_revoke_usable.png');
	 		$editRevoke.addClass('revoke_usable');
	 	}
	 	if (operation_step_number<=4) {
			operation_step_number+=1;
	 	}
	 	
	 	let step_object;
	 	let $selectedid=$('#'+selectedid);
	 	let $parent=$selectedid.parent();


	    /* 获得模块的id.如果是删除操作,还需要记录下一个相邻模块的id.
	       并且需要判断:如果没有下一个模块(也就是此模块是最后一个模块或唯一一个模块),nextId为空 */	
	    /*另外,由于视频和图文的特殊性,每个模块里面有很多小模块.所以在删除和编辑操作时,需要记录整个模块的内容,在撤销和重做时,替换整个模块*/
		if (operation=='del') {
			//判断视频和图片模块
			let parentId='';
			if (selectedid.search('img_txt')!=-1||selectedid.search('video')!=-1) {
				let layBg=$selectedid.parents('.layout_background');
				$parent=layBg.parent();
				parentId=layBg.attr('id');
			}
			let nextId='';
			let next_module=$parent.next();
			if (next_module.length!=0) { //如果有下一个模块
				nextId=next_module.children().first().attr('id');
		 	}
		 	step_object={thisId:selectedid,parentId:parentId,nextId:nextId,operation:operation,content_html:$parent.clone(true, true)};
	 	}
	 	//判断,如果是“上移”或者“下移”操作,只记录操作类型和thisId
	 	else if(operation=='up'||operation=='down'){
	 		step_object={thisId:paramId,operation:operation};
	 	}
	 	//如果是增加模块,只需要记录本模块的id即可.因为增加的撤销的操作是将此模块移除; 并且新增都是加在最后,所以重做直接将内容恢复到最后即可
	 	else if(operation=='add'){
	 		step_object={thisId:selectedid,operation:operation,content_html:$parent.clone(true, true)};
	 	}
	 	/*编辑:由于轮播图的特殊性,在轮播图更换样式之后,需要有一个新的id,用来初始化.
	 	所以在数组中,需要记下前后两个id,以便在撤销和重做时,能够初始化(字段名:oldId)*/
	 	else{
	 		step_object={thisId:selectedid,oldId:'',operation:operation,content_html:$parent.clone(true, true)};
	 	}

	 	//只保留5步,如果数组超长,要覆盖掉之前的步骤
	 	if (operation_step_number>5) {
	 		operation_array.shift();
	 	}else{
	 		operation_array.splice(operation_step_number-1,6-operation_step_number);
	 	}
	 	operation_array.push(step_object);
	 }

	/*
	 * @方法:撤销操作
	 * @说明:根据变量operation_step_number,判断撤销到哪一步,并且每撤销一次,operation_step_number减1
	 */
	 function revoke(){
	 	//当这个方法执行的时候,重做按钮就可以生效(以下判断是为了不重复对dom操作)
	 	if (operation_step_number==operation_array.length) {
	 		$editRedoImg.attr('src', 'img/icon/icon_redo_usable.png');
	 		$editRedo.addClass('redo_usable');
	 	}
	 	//如果operation_step_number>0,撤销功能可用 //.....其实这个判断可以不需要,做了新的逻辑
	 	if (operation_step_number>0) {
		 	//把数组中的对象提出来,方便操作
		 	let this_object=operation_array[operation_step_number-1];
		 	let operation=this_object.operation;
		 	console.log(operation);
		 	let parent=$('#'+this_object.thisId).parent();

		 	//如果是增加,那么撤销就要移除
	 		if (operation=='add') {
	 			parent.remove();
	 		}
	 		//如果是删除,撤销就是加回来.
	 		else if(operation=='del'){
	 			//图文和视频模块需要特殊处理:将整个大模块替换
	 			let thisId=this_object.thisId;
	 			if (thisId.search('video')!=-1||thisId.search('img_txt')!=-1) {
	 				$('#'+this_object.parentId).parent().html(this_object.content_html.html());
	 			}else{
	 				//如果没有下一个模块(最后一个模块,直接加在文档后面)
		 			if (this_object.nextId=='') {
		 				$org_content.append(this_object.content_html);
		 			}
		 			else {
		 				console.log(this_object.nextId);
		 				$('#'+this_object.nextId).parent().before(this_object.content_html)
		 			}
			 		//然后,如果是轮播图, 还需要初始化
			 		if(this_object.thisId.search('banner')!=-1){
			 			initSwiper(this_object.thisId);
			 		}
	 			}
	 		}
	 		else if(operation=='edit'){
	 		/*如果操作是修改,改回修改之前的内容.
	 		同时,为了在执行“重做”的时候能再恢复修改后的内容,需要将修改后的内容替换掉修改前的内容.同样,在重做方法中,也要做这种的操作*/
	 			let afterEditContent=parent.clone(true, true);
	 			parent.replaceWith(this_object.content_html);
	 			this_object.content_html=afterEditContent; //替换修改内容

	 			if(this_object.thisId.search('banner')!=-1){
		 			initSwiper(this_object.oldId);
		 		}
	 		}
	 		else if(operation=='up'){
	 			parent.insertAfter(parent.next());
	 		}
	 		else{
	 			parent.insertBefore(parent.prev());
	 		}
	 		operation_step_number--;
	 		if (operation_step_number==0) {
	 			$editRevokeImg.attr('src', 'img/icon/icon_revoke.png');
	 			$editRevoke.removeClass('revoke_usable');
	 		}
	 	}
	}

	/*
	 * 方法:重做操作
	*/
	function redo(){
		//当执行重做方法的时候,撤销按钮样式改变就可用
		if (operation_step_number==0) {
			$editRevokeImg.attr('src', 'img/icon/icon_revoke_usable.png');
	 		$editRevoke.addClass('revoke_usable');
	 	}
		//把数组中的对象提出来,方便操作
		let this_object=operation_array[operation_step_number];
		let operation=this_object.operation;
		let id=this_object.thisId;
		let parent=$('#'+id).parent();

		if (operation=='add') {
			$org_content.append(this_object.content_html);
			if(id.search('banner')!=-1){
				initSwiper(id);
			}
		}
		else if(operation=='del'){
			//判断视频和图文
			if (id.search('video')!=-1||id.search('img_txt')!=-1) {
				parent.parent().remove();
			} else {
				parent.remove();
			}
			
		}
		else if(operation=='edit'){
			//重做:
			if(id.search('banner')!=-1){
				id=this_object.oldId;
			}
			parent=$('#'+id).parent();
			let beforeEditContent=parent.clone(true, true);
			parent.replaceWith(this_object.content_html);
			this_object.content_html=beforeEditContent;
			if(id.search('banner')!=-1){
				initSwiper(this_object.thisId);
			}
		}
		else if(operation=='up'){
			parent.insertBefore(parent.prev());
		}
		else{
			parent.insertAfter(parent.next());
		}

		operation_step_number++;
		//操作完之后,如果当前数组长度与数字相等,重做按钮不可用
		if (operation_step_number==operation_array.length) {
			$editRedoImg.attr('src', 'img/icon/icon_redo.png');
			$editRedo.removeClass('redo_usable');
		}
	}


	//让a标签href失效,通过重定向实现跳转
	$body.off('click', '.nav-editable a').on('click', '.nav-editable a', function() {
		let id=$(this).attr('id');
		//需要判断页面是否发生过更改,如果发生过,需要先提示用户保存
		if (editSymbol==1) {
			makesureWindow('switch',id);
		}else{
			if (id=='lession') {
				window.open('org/'+orgId+'.school?pageName=lession');
			}else{
				location.assign('edit/'+orgId+'.action?pageName='+id);
			}
		}
		return false;
	});



/*  @方法: 点击导航栏时,跳转页面的提醒窗口及不同操作的处理
	@参数 type:操作类型 ‘cancle’表示取消保存; ‘switch’表示切换页面;
		  id:切换页面时需要用到: 所点击的导航栏id
*/
function makesureWindow(type,id){
	let $switchPanel=$('#switch_makesure_panel');
	let $makesureContent=$('.makesure_content');
	let $sureBtn=$('#switch_sure_btn');
	$switchPanel.fadeIn();
	$hover_layout.fadeIn();

	if (type=='switch') {
		//需要判断页面是否发生过更改,如果发生过,需要先提示用户保存
		$makesureContent.text('页面即将跳转,是否保存已修改内容?');
		$sureBtn.html('立即保存');
		//确定保存
		$sureBtn.off('click').on('click', function() {
			console.log('id:'+id)
			clearOperation();
			$.post('orgWebsite/savePages.action',
				{
					contentHtml:$org_content.html(),
					headerHtml:$header_content.html(),
					pageName:pageName
				},
				function(data) {
					if (data==1) {
						if (id!='lession') {
							let url=window.location.href;
							let position=url.lastIndexOf('=');
							if (position!=-1) {
								url=url.substring(0,position+1);
							}else{
								url=url+'?pageName=';
							}
							location.assign(url+id);
						}else{
							window.open('org/'+orgId+'.school?pageName=lession');
						}

					} else {
						alert("网络异常,保存失败!");
					}
			});
			$switchPanel.fadeOut();
		  	$hover_layout.fadeOut();
		});
	}
	if(type=='cancle') {
		$makesureContent.text('确定放弃所有的修改吗?');
		$sureBtn.html('确定');
		$('#switch_sure_btn').off('click').on('click', function() {
			//刷新页面
			window.location.reload();
		});
	}

	//取消:关闭确认面板
	$('.switch_panle_close, .switch_cancel_btn').off('click').on('click', function(event) {
	  	$switchPanel.fadeOut();
	  	$hover_layout.fadeOut();
	});
}


//网站编辑顶部功能条
	//保存:
	$body.off('click', '.function_bar_save').on('click', '.function_bar_save', function() {
		clearOperation();
	  	//3.获取class=org_content里面的内容,保存到数据库
	  	let org_html=$('.org_content').html();
	  	if (typeof(pageName)=='undefined') {
	  		pageName=href.substring(href.lastIndexOf('=')+1);
	  	}
	  	
	  	//为防止在回调函数中打开的窗口被拦截,先预打开窗口
	  	let templateWindow=window.open();
		$.post('orgWebsite/savePages.action', 
			{
				contentHtml:$org_content.html(),
				headerHtml:$header_content.html(),
				pageName:pageName
			},
			function(data){
				if (data==1) {
					templateWindow.location='org/'+orgId+'.school?pageName='+pageName;
				} else {
					//提示错误
					templateWindow.close();
					alert("网络故障,保存失败!");
				}
		});
	});	
	
	/**
	* @方法:保存的时候,清空操作记录,重置重做和撤销按钮的样式
	*/
	function clearOperation(){
		//1.记录操作步骤的数组重置,操作步骤索引重置为0
		operation_array=new Array();
		operation_step_number=0;
		//2.编辑的标志变量重置,重做和撤销按钮重置
		editSymbol=0;
		$editRevoke.removeClass('revoke_usable');
		$editRevokeImg.attr('src', 'img/icon/icon_revoke.png');
		$editRedo.removeClass('redo_usable');
		$editRedoImg.attr('src', 'img/icon/icon_redo.png');
	}



	//功能条:点击取消——刷新当前页面
	$('.function_bar_cancel').click(function(event) {
		if (editSymbol==1) {
			makesureWindow('cancle');
		} else {
			window.location.reload();
		}
		
	});

	//撤销
	$('.function_bar').off('click', '.revoke_usable').on('click', '.revoke_usable', function() {
		revoke();
	});
	//重做
	$('.function_bar').off('click', '.redo_usable').on('click', '.redo_usable', function() {
		redo();
	});


/*
	开发者模式:点击保存模版.这个功能只是为了前期制作模版用的
*/
	let templateName="template1";
	//保存header
	$('.store_head').click(function(event) {
		let pageName='header';
		let htmlContent=$('.header_content').html();
		$.post('orgWebsite/insertTemplate.do', {htmlContent:htmlContent,pageName:pageName,templateName:templateName}, function(data) {
			if (data==0) {
				alert("header内容保存失败");
			} 
			if (data==1) {
				alert("保存成功!");
			}
		});
	});

	//保存网页其他内容
	$body.off('click', '.store_template').on('click', '.store_template', function() {
		let htmlContent=$('.org_content').html();
		let pageName=pageName;
		pageName='school'
		$.post('orgWebsite/insertTemplate.do', {htmlContent:htmlContent,pageName:pageName,templateName:templateName}, function(data) {
			if (data==0) {
				alert("保存失败,请检查原因");
			} 
			if (data==1) {
				alert("保存成功!");
			}
		});
	});


});