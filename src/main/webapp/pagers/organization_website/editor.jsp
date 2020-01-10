<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="css/organization_website/editor.css">
<script src="https://cdn.staticfile.org/jquery/2.1.1/jquery.min.js"></script>
<script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>

<!-- UEditor -->
<script src="ueditor/ueditor.config.js"></script>
<script src="ueditor/ueditor.all.js"></script>
<!-- 图片管理器 -->
<link rel="stylesheet" href="css/imageManager/imageManager.css">
<script src="js/imageManager/imageManager.js"></script>
<!-- 模块-bfp- -->
<link rel="stylesheet" href="css/organization_website/moduleStyle.css">
<script src="js/organization_website/module.js"></script>
<!-- 轮播图 -->
<link rel="stylesheet" href="css/swiper/swiper.min.css">
<script  src="js/swiper/swiper.min.js"></script>

<script src="js/organization_website/editor.js"></script>

<title id="">爱上学习网</title>
</head>
<body>
	<div class="function_bar">
		<div class="function_bar_btn function_bar_save">保存并预览</div>
		<div class="function_bar_btn function_bar_cancel">取消</div>
		<div class="function_bar_btn store_head">保存header</div>
		<div class="function_bar_btn store_template">保存模版</div>
		<div class="edit_revoke function_bar_edit"><img src="img/icon/icon_revoke.png" alt="撤销"></div>
		<div class="edit_redo function_bar_edit"><img src="img/icon/icon_redo.png" alt="重做"></div>
		
	</div>
	<div class="header_content"></div>
	<div class="org_content"></div>
	<div class="background_container">
	<div class="add_module">
		<button id="add_module_btn" class="btn btn-default">添加新模块</button>
		
	</div>	
	
	<!-- 模态窗背景 -->
	<div id="hover_layout" ></div>
	<!-- 可添加模块的list弹窗(左侧) -->
	<div id="module_list">
		<div class="modal-header">
				<button id="module_list_close" class="close" data-dismiss="modal" >
					&times;
				</button>
				<h5 class="modal-title">
					选择模块
				</h5>
		</div>
		
		<ul>
			<li class="module_list_li" id="module_logo">LOGO</li>
			<li class="module_list_li" id="module_nav">导航栏</li>
			<li class="module_list_li" id="module_banner">轮播图</li>
			<li class="module_list_li" id="module_img_txt">图文展示</li>
			<li class="module_list_li" id="module_video">视频展示</li>
		</ul>
	</div>
	<!-- 右侧新增模块的弹窗 -->
	<div id="add_module_panel" >
		<div class="modal-header">
				<button  class="close add_module_panel_close" data-dismiss="modal" >
					&times;
				</button>
				<h5 class="modal-title">
					选择样式
				</h5>
		</div>
		<!-- 中间内容 -->
		<div id="add_content" class="modal-body">
		</div>
		<!-- 底部 -->
		 <div class="modal-footer">
				<button class="btn btn-default add_module_panel_close" data-dismiss="modal">取消
				</button>
				<button type="button" id="OK_btn_add" class="btn btn-primary">
					确定
				</button>
		</div>
	</div>

<!-- 右侧编辑模块的弹窗 -->
	<div id="edit_module_panel">
		<div class="modal-header">
				<button  class="close edit_module_panel_close" data-dismiss="modal" >
					&times;
				</button>				
				<ul id="myTab" class="nav nav-tabs">
					<li class="active">
						<a href="#choose_style" data-toggle="tab">
							 选择样式
						</a>
					</li>
					<li><a href="#edit_content" data-toggle="tab">编辑内容</a></li>		
				</ul>
		</div>
		<!-- 中间内容 -->
		<div class="tab-content modal-body">
			<div id="choose_style" class="tab-pane fade in active">
					选择样式
			</div>
			<div id="edit_content" class="tab-pane fade">
					编辑内容
			</div>
		</div>
		<!-- 底部 -->
		 <div class="modal-footer">
				<button class="btn btn-default edit_module_panel_close" data-dismiss="modal">取消
				</button>
				<button type="button" id="OK_btn_edit" class="btn btn-primary">
					确定
				</button>
		</div>
	</div>

<!-- 删除组件确认弹窗 -->
	<div id="del_module_panel">
		<div class="modal-header">
			<button  class="close del_module_panel_close" data-dismiss="modal" >
					&times;
			</button>
			<h4 class="modal-title">请确定是否删除模块？</h4>
		</div>
		<!-- 底部 -->
		 <div class="modal-footer">
			<button class="btn btn-default del_module_panel_close" data-dismiss="modal">取消
			</button>
			<button type="button" id="OK_btn_del" class="btn btn-primary">确定</button>
		</div>
	</div>
	
	<!-- 跳转页面前,保存确认窗-->
	<div id="switch_makesure_panel">
		<div class="modal-header">
				<span  class="close switch_panle_close" data-dismiss="modal" >
					&times;
				</span>
		</div>
		<div class="save_info_content">
			<div class="makesure_content"></div>
		</div>
		<!-- 底部 -->
		 <div class="modal-footer">
				<button class="btn btn-default switch_cancel_btn" data-dismiss="modal">取消
				</button>
				<button type="button" id="switch_sure_btn" class="btn btn-primary"> </button>
		</div>
	</div>
	
<!-- for store-->
	<div class="forStore">["img/icon/img5.jpg","img/icon/img6.jpg","img/icon/timg.jpg"]</div>
	</div>
</body>
</html>