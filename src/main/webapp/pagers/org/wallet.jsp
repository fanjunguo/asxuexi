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
<title>钱包</title>
<link rel="stylesheet"
	href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" />
<link rel="stylesheet" href="css/headerStyle.css" />
<link rel="stylesheet" href="css/org/orgMenu.css" />
<link rel="stylesheet" href="css/org/wallet.css">
<link rel="stylesheet" href="css/footer.css" />
<script src="js/jquery-3.3.1.min.js"></script>
<script
	src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="js/easy-ui/jquery.easyui.min.js"></script>
<script src="js/login/md5.js"></script>
<script src="js/authorityFilter.js"></script>
</head>
<body>
	<script src="js/headerstyle.js"></script>
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
			<div class="account_header">
				<div class="account_info">
					<div class="column1">
						<h4>账户余额<sup data-toggle="tooltip" title="账户余额包含可用金额,保证金和入账中金额">?</sup></h4>
						<span class="account_amount"></span>	
					</div>
					<div class="column2">
						<div>可提现金额: <span class="account_enable"></span></div>
						<div>保证金: <span class="deposit"></span></div>
						<div>入帐中:<sup data-toggle="tooltip" title="买家已经付款到平台.买家确认打款给机构之后,订单的费用会打到您到账户">?</sup> <span class="amount_entering"></span></div>
					</div>
				</div>
				<div class="operattion">
					<span class="button cash_out">提现</span>
					<span class="button cash_in">充值</span>
				</div>
			</div>
			<div class="detail_container">
				<ul class="selection">
					<li class="selection_li active" id="all_records">收支记录</li>
					<em>|</em>
					<li class="selection_li" id="cash_out_records">提现记录</li>
				</ul>
			</div>
			<table>
				<thead>
					<tr>
						<th class="th1">时间</th>
						<th class="th2">金额</th>
						<th class="th3">操作</th>
						<th class="th4">备注</th>
					</tr>
				</thead>
				<tbody class="cash_flow_record">
				</tbody>
			</table>

			<!--  -->
			<div class="modal fade" id="bank" data-backdrop="static">
			    <div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
								&times;
							</button>
							<h4 class="modal-title" id="myModalLabel">
								提现申请
							</h4>
						</div>
						<div class="modal-body">
							<div>
								提现金额: <input id="amount" type="text" class="easyui-numberbox form-control amount" placeholder="请输入提现金额" data-options="min:0,precision:2">
								<i class="special_style">可提现金额: <em class="account_enable"></em></i>
							</div>
							<div>
								卡号: <input class="form-control bank_card" type="text" placeholder="填写银行卡账号">
							</div>
							<div>
								姓名: <input class="form-control owner_name" type="text" placeholder="填写持卡人姓名" maxlength="20">
							</div>
							<div>
								银行: <!-- <span class="band_name"></span> -->
								<span class="wrong_tips"></span>	
								<img class="band_icon">
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary submit_btn" id="submit">
								提交
							</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">取消
							</button>
						</div>
					</div>
				</div>
			</div>

			<div class="modal fade" id="result_window">
			    <div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-body">
							<p class="result_message"></p>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">确定
							</button>
							
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>
	<script src="js/footer.js"></script>
</body>
<script src="js/org/wallet.js"></script>
<script src="js/tool/bankBardAttribution.js"></script>
</html>