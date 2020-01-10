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
<title>账单管理</title>
<link rel="stylesheet"
	href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" />
<link rel="stylesheet" href="css/headerStyle.css" />
<link rel="stylesheet" href="css/org/orgMenu.css" />
<link rel="stylesheet" href="css/easy-ui/themes/metro/easyui.css">
<link rel="stylesheet" href="css/org/bill.css">
<link rel="stylesheet" href="css/footer.css" />
<script src="js/jquery-3.3.1.min.js"></script>

</head>
<body>
	<script src="js/headerstyle.js"></script>
	<div class="background"></div>
	<div class="content">
		<div class="left_content">
			<script src="js/org/orgMenu.js"></script>
			<div class="menu_content">
				<div class="menu_list">
					<div class="menu_item_empty"></div>
					<div class="menu_child_item_empty"></div>
					<div class="menu_item_empty"></div>
					<div class="menu_child_item_empty"></div>
					<div class="menu_item_empty"></div>
					<div class="menu_child_item_empty"></div>
					<div class="menu_item_empty"></div>
				</div>
			</div>
		</div>
		<div class="right_content">
			<div class="class_list_container">
				<table class="class_table"></table>
			</div>
			<div class="editor_container">
				<div class="toolbar">
					<button class="button back">&#60;返回</button>
					<button class="button add_item">增加收费项</button>
					<button class="button send_bill">保存并发送账单</button>
				</div>
				<table class="edit_table"></table>
			</div>
			<div class="past_period_container">
				
				<div class="input_container">
					<button class="button back_btn">&#60;返回</button>
					<input class="bill_period_input" class="easyui-combobox" data-options='valueField:"id", textField:"period"' style="height: 27px;">
				</div>
				<table class="past_period_table"></table>
			</div>

			<div class="modal fade" id="add_item_window" data-backdrop="static" data-keyboard="false">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close close_icon" data-dismiss="modal" aria-hidden="true">
								&times;
							</button>
							<h4 class="modal-title" id="myModalLabel">增加费用项</h4>
						</div>
						
						<div class="modal-body">
							费用名称: <input maxlength="20" class="item_input item_name" type="text" placeholder="如:学费、校服">
							<br>
							费用类型: <div class="price_type active" id="by_times">按考勤计费</div><div class="price_type" id="by_once">统一计费</div>
							<br>
							<div class="by_times price_input_div show">
								费用单价: <input class="item_input easyui-numberbox item_price_times" type="number" data-options="min:0,precision:2"> 元/每次考勤
							</div>
							<div class="by_once price_input_div">
								总费用: <input class="item_input easyui-numberbox item_price_once" type="number" data-options="min:0,precision:2"> 元
							</div>
							
						</div>

						<div class="modal-footer">
							<button type="button" id="btn_sure" class="btn btn-primary" data-dismiss="modal">确定
							</button>
							 <button type="button" id="btn_close" class="btn btn-default" data-dismiss="modal">关闭</button>
						</div>
					</div>
				</div>
			</div>
			
			<div class="confirmContainer"></div>
			
			<div class="detail_table_container">
				学生 <span class="detail_header"></span><span> 考勤明细</span>
				<div class="detail_close"></div>
				<div class="overflow_div">
					<table class="detail_table">
						<thead>
							<tr class='table_head'></tr>
						</thead>
						<tbody>
							<tr class="table_body"></tr>
						</tbody>
					</table>
				</div>
			</div>
			
			
		</div>
	</div>
	<script src="js/footer.js"></script>
</body>
<script src="js/easy-ui/jquery.easyui.min.js"></script>
<script src="js/easy-ui/locale/easyui-lang-zh_CN.js"></script>
<script src="js/org/bill.js"></script>
<script
	src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="js/login/md5.js"></script>
<script src="js/authorityFilter.js"></script>
</html>