/*插件:图片管理器
  作用:机构上传图片时,可以统一调用此功能.机构可以统一管理自己的图片.并且如果多个地方用到同一张图片,可以不用再重复上传.
  @author:junguo.fan
  @version:1.0
  @updateTime:2018.10.16

  @version:1.1
  @updateTime:2018.12.29
  @更新:上传图片时,如果图片名称重复,上传按钮不可用.然后由于判断是否重复的触发事件是文本框失去焦点.这样的话在重新修改名字之后并点击确定
  		由于点击保存按钮的时候,同时触发了失去焦点事件,导致交互效果上不好.这次修改加了个延迟效果
*/
/*
 * @方法:写入整个图片管理面板,及其所有事件
 * @参数: buttonId_needed -用作确定按钮的id;并且用做整个面板的class,用于移除面板时使用
 		 fileType_needed -需要的文件类型 1是图片;2是视频.如果不传,默认展示图片
 */

(function($){
	jQuery.fn.loadImageManager=function(buttonId_needed,fileType_needed){
		$('head').append('<link rel="stylesheet" href="css/webUploader/webuploader.css"><script src="js/webUploader/webuploader.js"></script>');
		//$.getScript('js/webUploader/webuploader.js');
		//写入面板
		let imageManager_html='\
			<div class="imageManager_container '+buttonId_needed+'">\
				<!-- 遮罩层 -->\
				<div class="bg"></div>\
				<!-- 管理面板 -->\
				<div class="img_manager">\
					<div class="panel_title">\
						<div class="title">添加文件</div>\
						<div class="close_button"></div>\
					</div>\
					<div class="panel_leftmenu">\
						<div class="panel_tab selected_manager" id="tab_img">我的文件</div>\
						<div class="panel_tab" id="tab_video">我的视频</div>\
						<div class="panel_tab" id="tab_system">图片库</div>\
					</div>\
				<!-- 三个选项卡-->\
					<!-- 我的图片 -->\
					<div  class="panel_center tab_img tab_selected">\
						<div class="center_btn_group">\
							<div class="center_btn upload_img">上传图片</div>\
							<div class="center_btn create_folder">新建文件夹</div>\
							<div class="store_space">存储空间:<span class="space_used"></span>M</div>\
						</div>\
						<div class="center_content">\
							<div class="file_path"></div>\
							<div class="file_display"></div>\
						</div>\
					</div>\
					<!-- 我的视频 -->\
					<div  class="panel_center tab_video">\
						<div class="center_btn_group">\
							<div class="center_btn upload_video">上传视频</div>\
							<div class="center_btn create_folder">新建文件夹</div>\
							<div id="preview_btn" class="center_btn">预览</div>\
							<div class="store_space">存储空间:<span class="space_used"></span>M</div>\
						</div>\
						<div class="center_content">\
							<div class="file_path">\
							</div>\
							<div class="file_display">\
							</div>\
						</div>\
					</div>\
					<!-- 系统图片 -->\
					<div  class="panel_center tab_system">\
							<div class="file_display">\
							</div>\
					</div>\
					<div class="panel_footer">\
						<div class="btn_group">\
							<div class="YN_btn sure_btn">确定</div>\
							<div id="cancel_btn" class="YN_btn">取消</div>\
						</div>\
					</div>\
				</div>\
			</div>';
		$('body').append(imageManager_html);
		//全局变量:存放当前面板的根路径,默认初始是img(2019.10.8增加,之前写的逻辑,可以考虑利用这个变量做更简化的修改)
		var tabName="img";
		//判断需要哪种类型的文件
		if (fileType_needed==1) {//需要图片
			$('#tab_video').css('display', 'none');
			getFile(tabName);
		}else if(fileType_needed==2) {//需要视频
			tabName="video";
			getFile(tabName);
			$('#tab_video').addClass('selected_manager');
			$('.tab_video').addClass('tab_selected');

			$('#tab_img').css('display', 'none');
			$('.tab_img').removeClass('tab_selected')
			$('#tab_system').css('display', 'none');
		}else{
			getFile(tabName);
		}

		
	
		//声明变量
		var $managerContainer=$('.imageManager_container');
		var $body=$('body');
		//全局变量:存放文件路径的数组.根路径之后的路径
		var pathArray=new Array();
		//全局变量:存放文件夹名称和文件名称的数组,用于判断新建的时候不能重复
		var folderArray=['invalid'];
		var fileArray=new Array();

		//点击叉号和[取消]按钮,关闭整个面板
		$body.off('click','.close_button,#cancel_btn').on('click', '.close_button,#cancel_btn', function() {
			$managerContainer.fadeOut();
			setTimeout(function(){$managerContainer.remove()},500);
		});
		//点击切换选项卡
		$body.off('click','.panel_tab').on('click', '.panel_tab', function(event) {
			//选项卡内容
			var id=$(this).attr('id');
			tabName=(id.split("_"))[1];
			getFile(tabName);
			changeButtonStyle();
			//每次切换选项卡,都重置[预览]和[确定]两个按钮的样式
			$('#preview_btn,.sure_btn').removeClass('usable_status');

			$('.tab_selected').removeClass('tab_selected');
			$('.'+id).addClass('tab_selected')
			//选项卡样式
			$('.selected_manager').removeClass('selected_manager');
			$(this).addClass('selected_manager');

		});

		//删除按钮
		$body.off('mouseenter', '.file_container').on('mouseenter', '.file_container', function() {
			if(tabName!='system'){
			$(this).append('<div class="del_img_container"><img class="del_img" src="img/icons/close.png"/></div>')
			}
		});
		$body.off('mouseleave', '.file_container').on('mouseleave', '.file_container', function() {
			if(tabName!='system'){
			$('.del_img_container').remove();}
		});
	//删:删除文件或文件夹
		$body.off('click','.del_img').on('click', '.del_img', function() {
		  	//所以,首先判断类型:1表示文件夹;2表示文件
		  	let $this=$(this);
		  	let $space_used=$('.space_used');
		  	let className=$this.parents('.file_container').attr('class');
		  	let type=parseInt(className.substring(className.length-1));
		  	//文件(夹)的名字
		  	let name=$this.parent().prev().text();
		  	if (type!=1) {
		  		//如果是文件
		  		type=2;
		  		var path=$this.parent().prev().prev().attr('path');

				$.post('img/deleteFile.action', {type:type,path:path}, function(json) {
					if (json.code==600) {
						//移除图片/视频
						$this.parents('.file_container').remove();
						//更新空间
						$space_used.text(json.data);
						//改变按钮样式,移除path和id属性
						$('.file_selected').removeClass('file_selected');
						changeButtonStyle();
						//更新数组
						removeThisElement(name,fileArray);
					} else{
						alert("删除文件失败,建议刷新页面后重试");
					}
				});	  		
		  	}else{
		  		//如果是文件夹
		  		let parent_path=$this.parent().prev().prev().attr('path');
		  		var path=parent_path+'/'+name;
		  		//写入确认是否删除文件夹面板
		  		createPanel('删除文件夹','deleteFolder');
		  		let content='<div class="delete_info">删除文件夹,该文件夹中的内容将一并删除<br>请确认是否继续删除?</div>';
		  		$('.createFolder_content').html(content);
		  		//后面的点击,会导致dom结构改变,$this会失效.所以这里先找到父级元素
		  		var $fileContainer=$this.parents('.file_container');
				//点击确定删除文件夹
				$managerContainer.off('click').on('click', '#deleteFolder', function() {
					$.post('img/deleteFile.action', {type:type,path:path}, function(json) {
						//如果成功
						if (json.code==600) {
							//关闭输入名称的面板;并回调遮罩层z轴
				  			$('.input_panel').remove();
				  			$('.bg').css('z-index', '10005');
				  			//移除页面内容
							$fileContainer.remove();
							//更新数组
							removeThisElement(name,folderArray);
							$space_used.text(json.data);
						} else{
							alert("删除文件夹失败,建议刷新页面后重试");
						}
					});	
				});
		  	} 	
		});

		//选中图片,改变边框样式
		$body.off('click','.file_itself').on('click', '.file_itself', function() {
			//判断:如果已经被选中,则点击取消选中;如果没有被选中,则点击选中;并且,选中的时候,“确定”的按钮可以使用
			//视频:选中视频后,预览按钮可以使用
			let $thiss=$(this);
			var $sureBtn=$('.sure_btn');
			var $previewBtn=$('#preview_btn');
			var className=$thiss.attr('class');
			//如果该图片已经被选中,则取消选中
			if (className.search('file_selected')!=-1) {
				//移除按钮的属性id和path
				$sureBtn.removeAttr('path');
				$sureBtn.removeAttr('id')
				//移除图片选中样式
				$thiss.removeClass('file_selected');
				//改变按钮样式
				$previewBtn.removeClass('usable_status');
				$sureBtn.removeClass('usable_status');
			}else{
				//如果该图片没有被选中:其他已经被选中的取消,该图片被选中
				$('.file_selected').removeClass('file_selected');
				$thiss.addClass('file_selected');
				//改变确定按钮的自定义属性
				$sureBtn.attr('id', buttonId_needed);
				$sureBtn.attr('path', $thiss.attr('path'));
				//改变按钮样式
				$previewBtn.addClass('usable_status');
				$sureBtn.addClass('usable_status');
			}
		});

	//增:新建文件夹
	$body.off('click','.create_folder').on('click', '.create_folder', function() {
		//调用方法,传id参数,写入面板
		createPanel('新建文件夹','create_sure');
		//写入面板之后,增加面板内容
		let html='请输入文件夹名称:<input class="input_folderName"><span class="attention">*名称确定之后不能修改</span>';
		$('.createFolder_content').html(html);
	});
	  //确定:发送请求,新建文件夹
	  $body.off('click','#create_sure').on('click', '#create_sure', function() {
	  	//获得用户输入内容
	  	var input_content=$('.input_folderName').val().trim();
	  	if (input_content=='') {
	  		alert("文件夹名称不能为空");
	  	} else {
	  		if(isRepeat(input_content,folderArray)){
	  			alert("该名称已存在,请重新输入");
	  		}else{
	  			//获取当前路径
				let current_path=getCurrentPath();
				let path=current_path+"/"+input_content;
			  	$.post('img/addFileDir.action', {path: path}, function(data) {
			  		if (data==1) {
			  			//成功之后,关闭输入名称的面板;并回调遮罩层z轴
			  			$('.input_panel').remove();
			  			$('.bg').css('z-index', '10005');
			  			//并且,更新数组内容
			  			folderArray.push(input_content);
			  			let html='<div class="file_container 1">\
								<div class="file_folder" path="'+current_path+'" style="background-image: url(img/icons/icon_file.png);"></div>\
								<div class="file_name">'+input_content+'</div></div>';
						$('.file_display').append(html);
			  		} else {
			  			alert("创建失败,建议刷新网页后重试");
			  		}
			  	});
	  		}
	  	
	  	}
	  });
	  //取消:点击取消之后,关闭面板············取消按钮可以所有面板共用
	  $body.off('click','.create_cancel').on('click', '.create_cancel', function() {
	  	$('.input_panel').remove();
	  	$('.bg').css('z-index', '10005');
	  });	
	//点击文件夹,进入下一级
		$body.off('click','.file_folder').on('click','.file_folder',function(){
			//获取文件夹名字
			var folderName=$(this).next().text();
			//获取属性
			var attr_path=$(this).attr('path');
			//调用方法,请求文件夹数据
			getFile(attr_path+'/'+folderName);
			//移除按钮样式和属性
			changeButtonStyle();
		});
	//点击路径,返回目标文件位置
		$body.off('click','.other_path').on('click', '.other_path', function() {
			//判断特殊情况:数组没有初始化
			if (pathArray!='') {
				if (pathArray.length==1) {
					getFile(tabName);
				}else{
					var id=parseInt($(this).attr('id'));
					var path=tabName;
					for (var i = 0; i < id; i++) {
						path=path+"/"+pathArray[i];
					}
					getFile(path);
				}
			}
		});	
	//点击预览视频,弹窗播放视频
		$body.off('click','#preview_btn').on('click', '#preview_btn', function() {
			let video_src=$('.file_selected').attr('path');
			let player='<div class="video_player">\
			<div class="video_btn"></div>\
			<video class="video_label" controls preload="auto">\
			<source src="'+video_src+'" type="video/mp4"></video></div>';
			$managerContainer.append(player);
			adjustBg(10050);
		});
		//点击关闭按钮,关闭视频播放面板
		$body.off('click','.video_btn').on('click', '.video_btn', function() {
			$('.video_player').remove();
			adjustBg();
		});

	//[按钮]上传图片
		$body.off('click','.upload_img').on('click', '.upload_img', function() {
			changeButtonStyle();
			fileUploader(1);
		});	
		$body.off('click','.upload_video').on('click', '.upload_video', function() {
			changeButtonStyle();
			fileUploader(2);
		});
		
//--------------------方法--------------------------------	
    /* 
     * 方法:改变[确定]和[预览]按钮样式
     */
     function changeButtonStyle(){
     	let $sure_btn=$('.sure_btn');
     	 $sure_btn.removeClass('usable_status');
     	 $sure_btn.removeAttr('id');
     	 $sure_btn.removeAttr('path');
     	$('#preview_btn').removeClass('usable_status');

     }
		/*
		 * 方法:获取文件夹下的数据.封装成一个方法,方便后续使用
		 * 参数:
		 * 		pathName:要获取的数据的目标文件夹的路径(从根路径开始:img/我的文件夹/);
		 * */
		function getFile(pathName){
			//判断一下取不到数据的特殊情况
			if (typeof(pathName)!='undefined') {
				if (pathName=='system') {
					//系统图片,不展示路径
					$.get('img/getSysFileDir.action', function(data) {
						var html='';
						for (var i = 0; i < data.length; i++) {
							var path=data[i];
							var fileName=path.substring(path.lastIndexOf('/')+1,path.lastIndexOf('.'));
							html=html+'<div class="file_container">\
								<div class="file_itself" path="'+path+'" style="background-image: url(\''+path+'\');"></div>\
								<div class="file_name">'+fileName+'</div></div>';
						}
						$('.tab_selected .file_display').html(html);
					});
				} else {
					//1.处理展示文件路径
					writeFilePath(pathName);
					$.post('img/getFileDir.action', {fileDir: pathName}, function(data) {
						//重置数组folderArray和fileArray
						fileArray=new Array();
						folderArray=['invalid']
						//2.存储占用空间
						$('.space_used').text(data.size);
						//3.解析文件类型 1.文件夹 2.图片 3.视频 4.未通过审核的文件
						var fileList=data.fileList;
						var html='';
						for (var i = 0; i < fileList.length; i++) {
							var path=fileList[i].filePath;
							var type=fileList[i].fileType;
							if(type==1){
								//文件夹类型
								let fileName=path.substring(path.lastIndexOf('/')+1);
								//将文件夹名称存入数组
								folderArray.push(fileName);
								html=html+'<div class="file_container '+type+'">\
								<div class="file_folder" path="'+pathName+'" style="background-image: url(img/icons/icon_file.png);"></div>\
								<div class="file_name">'+fileName+'</div></div>';
							}else if(type==3){
								//视频
								let fileName=path.substring(path.lastIndexOf('/')+1,path.lastIndexOf('.'));
								fileArray.push(fileName);
								html=html+'<div class="file_container '+type+'">\
								<div class="file_itself" path="'+path+'" style="background-image: url(img/icons/icon_video.png);"></div>\
								<div class="file_name">'+fileName+'</div></div>';
							}
							else if(type==2){
								//图片类型
								let fileName=path.substring(path.lastIndexOf('/')+1,path.lastIndexOf('.'));
								fileArray.push(fileName);
								html=html+'<div class="file_container '+type+'">\
								<div class="file_itself" path="'+path+'" style="background-image: url(\''+path+'\');"></div>\
								<div class="file_name">'+fileName+'</div></div>';
							}else{  //未通过审核文件
								let fileName=path.substring(path.lastIndexOf('/')+1,path.lastIndexOf('.'));
								fileArray.push(fileName);
								html+=`<div class="file_container ${type}">
								<div class="invalid_file" path="${path}" style="background-image: url('${path}');"></div>
								<div class="file_name invalid">敏感图片,请修改</div></div>`;
							}
						}
						$('.tab_selected .file_display').html(html);
					});
				}
			}else{
				alert('数据获取失败,请刷新网页重试');
			}
			
		}
		
		/* 方法:显示文件路径
		 * 参数: 
		 * 		path:相对路径,从根路径开始(不为空)
		 */
		function writeFilePath(path){
			//重置pathArray
			pathArray=new Array();
			//先清空路径
			var $filepath=$('.tab_selected .file_path');
			$filepath.html('');
			//只有根路径时不展示路径;当有子路径时:
			if (tabName!=path) {
				//先写入根路径
				var html='<span id="0">'+tabName+'</span>';
				
				var start=path.indexOf(tabName)+tabName.length+1;
				//var end=path.lastIndexOf('/');
				//如果只有一个‘/’,说明总共只有两级目录;否则,有至少三级目录
				var childrenPath=path.substring(start);
				// if((start-1)==end){
				// 	var childrenPath=path.substring(start);
				// }else{
				// 	var childrenPath=path.substring(start);
				// 	var childrenPath=path.substring(start,end);
				// }
					pathArray=childrenPath.split('/');
					for (var i = 0; i < pathArray.length; i++) {
						let j=i+1;
						html=html+'<span id="'+j+'"> > '+pathArray[i]+'</span>';
					}
					
					$filepath.html(html);
					//然后,要改变每一级span的样式,class
					$filepath.children('span').attr('class','other_path');
					$filepath.children('span').last().attr('class','current_path');
			}
		}

		/* 方法:判断新建的文件夹是否重名
		 * 参数:
		 * 		newName:新建的文件夹或者文件名称
		 *		oldNameArray:现有的名称的数组
		 */
		 function isRepeat(newName,oldNameArray){
		 	for (var i = 0; i < oldNameArray.length; i++) {
		 		if (newName==oldNameArray[i]) {
		 			return true;
		 		}
		 	}
		 	return false;
		 	//两个return,执行其中任意一个,整个就会跳出方法,不再继续执行
		 }

		 /* 方法:从数组中删除指定的元素
		 * 	参数:
		 * 		element:元素
		 *		array:数组
		 */
		 function removeThisElement(element,array){
		 	let index;
		 	for (var i = 0; i < array.length; i++) {
		 		if (element==array[i]) {
		 			index=i;
		 		}
		 	}
		 	array.splice(index,1);
		 }

		 /*
			方法:写入提示面板
			参数:  panelHeader_name -面板标题的名称;
				  buttonId-确定按钮的id;

			说明:1.此方法仅为了达到此需求目的,基本没有复用性
				2.此方法写入的面板是空面板,内容需要调用的时候自己写入
				3.[确定]按钮没有id,需要根据方法传入的参数确定.以区别不同地方的按钮绑定不同的事件
				4.[取消]按钮仅用于关闭面板,可以共用
		 */
		 function createPanel(panelHeader_name,buttonId){
		 	let	input_html='<div class="input_panel"><div class="createFolder_header">'+panelHeader_name+'</div><div class="createFolder_content"></div>\
					<div class="createFolder_footer"><div id="'+buttonId+'" class="createFolder_btn">确定</div><div class="createFolder_btn create_cancel">取消</div></div></div>';
			$managerContainer.append(input_html);
			//上调灰色遮罩层的z轴
			adjustBg(10050);
		 }

	 /*
	  *	方法:调整灰色遮罩层的z轴
	  *	@param: 
	  *			z:z-index的值.如果不传,则调回默认值10005.(为了提高与其他插件的兼容性,所以把基础z-index值调得比较高)
	  */
		 function adjustBg(z){
		 	if (typeof(z)=='undefined') {
		 		z=10005;
		 	} 
		 	$('.bg').css('z-index', z);
		 }
		/*
		 *	方法:获取当前文件夹路径
		 */
		function getCurrentPath(){
			let current_path=tabName;
			if (pathArray!='') {
				for (var i = 0; i < pathArray.length; i++) {
					current_path=current_path+"/"+pathArray[i];
				}
			}
			return current_path;
		}

/*
 @方法:判断输入的文件/文件夹的名字是否有非法字符
 @参数:需要判断的文件(夹)名
	1.非法字符规则 1-不能带有\ / : * ? # " < > |
	2.把字符串前后的空格trim一下
 @返回值: 0-没有非法字符 1-有非法字符
 */
 	function isIllegalName(testName){
 		let string='\\ / : * ? # " < > |';
 		let symbol_array=string.split(' ');
 		for (var i = 0; i < symbol_array.length; i++) {
 			if(testName.search('\\'+symbol_array[i])!=-1){
 				return 1;
 			};
 		}
 		return 0
 	}


	/*
	 *	方法:写入上传文件的面板,并初始化webuploader
	 *  参数:type
	 			1-表示图片;2-表示视频
	 */
		function fileUploader(type){
		//0.定义变量
			//规定文件类型的参数
			param_extension='';
			parem_mimeTypes='';
			if (type==1) {
				//如果是图片
				param_extensions='gif,jpg,png';
				parem_mimeTypes='image/jpg,image/gif,image/png,'
			} else {
				//如果是视频
				param_extensions='mp4';
				parem_mimeTypes='video/mp4'
			}

			//判断文件是否可以上传,默认可以.当文件名重复、文件名存在非法字符时,不可以上传
			isFileUploaded=true,
			//全局dom元素
			$save_file=$('.save_file'); //如果元素不存在,$save_file.length=0
			$tips=$('.tips');
	        // 缩略图大小
	        thumbnailWidth = 110 ,
	        thumbnailHeight = 110,
	        theFileName='';
	        fileTypeName=''; //文件后缀名,包含“.”

		//1.写入webuploader面板
			let tips_content='*仅支持jpg、png、gif格式';
			let filePickerBtnName='点击选择图片';
			let saveFIleBtnName='保存图片';
			let icon_src_fileType='img/icons/addImg.png';
			let howToTransferVideo='';
			if (type==2) {//如果是视频
				tips_content='*仅支持mp4格式 ';
				filePickerBtnName='点击选择视频';
				saveFIleBtnName='保存视频';
				icon_src_fileType='img/icons/icon_videoType.png';
				howToTransferVideo='<a class="howToTransferVideo" href="javaScript:void(0)">如何转换视频格式?</a>'
			}
			dom='<div id="uploader" class="wu-example">\
			 		<div class="uploader_close"></div>\
					    <div class="queueList">\
					    	<div class="uploader-list">\
					    	<div class="file-item thumbnail">\
					    	<img src="'+icon_src_fileType+'">\
					    	<div class="info"></div>\
					    	</div></div>\
					        <p class="tips">大小不超过10M;'+tips_content+howToTransferVideo+'</p><div id="filePicker"></div>\
					</div>\
				</div>';
			$body.append(dom);
			$uploader=$('#uploader');
			adjustBg(10050);

		//2.初始化
			var uploader = WebUploader.create({
			    // 文件接收服务端
			    server: 'img/addFile.action',
		        pick: {
		            id: '#filePicker',
		            label: filePickerBtnName,
		            multiple:'false'
		        },
		        
			    // 选完文件后，是否自动上传。
			    auto: false,
		        //是否分片处理大文件上传
		        chunked: true,		    
			    // 文件大小和个数
			    fileSingleSizeLimit: 10 * 1024 * 1024,    // 10 M
			    fileNumLimit:1,

			    // 设定可以上传的文件类型
			    accept: {
			        title: 'Images',
			        extensions: param_extensions,
			        mimeTypes: parem_mimeTypes,
			    },
			});		
		//添加队列之前执行事件
				 uploader.on("beforeFileQueued",function( file ) {
					 uploader.reset();
					 $('.uploader-list').empty();
				 });

		//3 当有文件添加进来的时候
			uploader.on( 'fileQueued', function( file ) {
				//改变按钮名称为:点击选择图片
				$('.webuploader-pick').html(filePickerBtnName);
				//清空class=tips红字提示内容
				$tips=$('.tips');
				$tips.html('&nbsp');
				//处理下文件名,把文件后缀存下来.然后只展示给用户不带后缀的文件名
				let fileName=file.name;
				let lastindex=fileName.lastIndexOf('.');
				fileTypeName=fileName.substring(lastindex);
				//重复利用下fileName,不再新建变量了
				fileName=fileName.substring(0,lastindex);

			    var $li = $(
			            '<div id="'+file.id+'"class="file-item thumbnail">' +
			                '<img>' +
			                '<div class="info"><input id="fileName_input" type="text" value="'+fileName+'"></div>' +
			            '</div>'
			            ),
			        $img = $li.find('img');
			    $('.uploader-list').html($li);

			    //文本框默认选中
				var thisInfo=document.getElementById('fileName_input');
				thisInfo.focus();

				/*当有图片准备上传,上传按钮可用,上传完成后,按钮置灰更
				*并且,只有在初次加载上传面板时,需要加入按钮.后面如果不关闭面板,这个按钮可以一直存在,所以需要加判断,为避免重复创建:
				*/
				if($save_file.length==0){
					$('.webuploader-pick').before('<div class="save_file">'+saveFIleBtnName+'</div>');
					$save_file=$('.save_file');
				}
				$save_file.removeClass('save_file_unused');

				//调整“选择图片”的触发元素位置,使其跟按钮位置重合
				$('#filePicker').children().eq(2).css('left', '239px');

			    // 创建缩略图
			    // 如果为非图片文件，可以不用调用此方法。
			    // thumbnailWidth x thumbnailHeight 为 100 x 100
			    uploader.makeThumb( file, function( error, src ) {
			        if ( error ) {
			            $img.attr( 'src', 'img/icons/icon_video.png' );
			            return;
			        }
			        $img.attr( 'src', src );
			    }, thumbnailWidth, thumbnailHeight );
			});
		//如果文件大小超过限制
			uploader.on('error',function(error){
				$save_file.addClass('save_file_unused');
				let html='<div class="uploader-list">\
				    	<div class="file-item thumbnail">\
				    	<img src="'+icon_src_fileType+'">\
				    	<div class="info"></div>\
				    	</div>';
				$('.uploader-list').html(html);
				if (error=='F_EXCEED_SIZE') {
					alert('文件大小不能超过10M');
				}else{
					alert('上传的文件超出限制');
				}
			});
		//点击保存图片按钮,上传
		$uploader.off('click', '.save_file').on('click', '.save_file', function() {
			//如果文件名没有问题,上传
			if (isFileUploaded) {
				//获得当前的文件夹路径
				let path=getCurrentPath();
				let fileName=theFileName+fileTypeName;
				uploader.options.formData={
					fileName: fileName, //文件名
				    path: path //文件保存的路径
				}
				uploader.upload();
			}

		});
		//获得用户修改的文件名称
		var $fileName_input=$('#fileName_input');
		$uploader.on('blur', '#fileName_input', function() {
			//每次都清空tips内容
			$tips.html('&nbsp');

			$fileName_input=$('#fileName_input');
			theFileName=($fileName_input.val()).trim();
			if (theFileName=='') {
				$tips.text('文件名称不能为空');
			} else {
				//需要判断,名字中是否有非法字符:
				if (isIllegalName(theFileName)==1) {
					$tips.text('名字中不能非法字符:   \\ / : * ? # " < > |');
					isFileUploaded=false;
					$save_file.addClass('save_file_unused');
				}else{
					if (isRepeat(theFileName,fileArray)) {
						isFileUploaded=false;
						$tips.text('名称已经存在,请更换名称');

						//改变上传按钮样式为不可用
						$save_file.addClass('save_file_unused');
					} 
					else {
						//当按钮样式为不可用时,再改变样式
						if (isFileUploaded==false) {
							setTimeout(function(){
								isFileUploaded=true;
								//改回上传按钮样式
								$save_file.removeClass('save_file_unused');
							},500);
						}
					}				
				}
			}
		});

		//4.上传结果显示
			// 文件上传成功，给item添加成功class, 用样式标记上传成功。
			uploader.on( 'uploadSuccess', function(file) {
				$tips.text('上传成功!');
			    //按钮样式改变
			    $save_file.addClass('save_file_unused');
			    $fileName_input.replaceWith(theFileName);
				//更新数组
				fileArray.push(theFileName);
				//修改按钮名称为:继续上传
				$('.webuploader-pick').html('点击继续上传');
			});

			// 文件上传失败，显示上传出错。
			uploader.on( 'uploadError', function( file ) {
				$tips.text('上传失败!请刷新页面重试');
			    $save_file.addClass('save_file_unused');
			    $fileName_input.replaceWith(theFileName);
			});

		//关闭uploader面板
			$body.off('click','.uploader_close').on('click', '.uploader_close', function() {
				$uploader.remove();
				adjustBg();
				//刷新页面
				getFile(getCurrentPath());
			});

		}


	}
})(jQuery);
