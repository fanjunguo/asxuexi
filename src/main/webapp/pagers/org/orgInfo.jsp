<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html >
<html>
<head>
<base href="<%=basePath%>" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机构中心</title>

<link rel="stylesheet" href="css/jquery-ui/jquery-ui-1.8.16.custom.css" />
<link rel="stylesheet"
	href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="css/webUploader/webuploader.css" />
<link rel="stylesheet" href="css/imageManager/imageManager.css" />
<link rel="stylesheet" href="css/org/orgInfo.css" />
<link rel="stylesheet" href="css/footer.css" />
<link rel="stylesheet" href="css/headerStyle.css" />
<link rel="stylesheet" href="css/org/orgMenu.css" />
<script src="js/jquery-3.3.1.min.js"></script>
<script
	src="https://api.map.baidu.com/api?v=3.0&ak=P9yAZyPp52QY8L2Hfk3gm655P4OnDjRz">
	
</script>
<script
	src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="js/validate/jquery.validate.pack.js"></script>
<script src="js/validate/additional-methods.js"></script>
<script src="js/validate/localization/messages_cn.js"></script>
<script src="js/date/DateClass.js"></script>
<script charset="utf-8" src="ueditor/ueditor.config.js"></script>
<script charset="utf-8" src="ueditor/ueditor.all.min.js"></script>
<script charset="utf-8" src="ueditor/lang/zh-cn/zh-cn.js"></script>
<script src="js/webUploader/webuploader.js"></script>
<script src="js/login/md5.js"></script>
<script src="js/authorityFilter.js"></script>
<script src="js/imageManager/imageManager.js"></script>
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
			<div id="context">
				<div id="essentialInformation">
					<div id="titleInformation" class="titleDiv">
						<span class="titleDiva">基本信息</span>
						<span class="titleDivb" id="editShowInformation">编辑</span>
						<span class="titleDivb" id="edit_cancel">取消</span>
						<span class="titleDivb" id="savaShowInformation">保存</span>
					</div>
					<!-- 展示信息 -->
					<div id="showInformation">
						<table class="tableShowInformation">
							<tbody>
								<tr>
									<th>机构名称：</th>
									<td id="orgName"></td>
									<!-- <td rowspan="3" id="orgLogoImg" class="orgLogoImg">
										<img class="showLogo" src="" width="200px" height="150px">
									</td> -->
								</tr>
								<tr>
									<th>联系电话:</th>
									<td id="orgtel"></td>

								</tr>
								<tr>
									<th>机构负责人:</th>
									<td id="orgLegalPerson"></td>
								</tr>
								<tr>
									<th>地址:</th>
									<td colspan="2" id="orgAddress"></td>

								</tr>
								<tr>
									<th>机构简介:</th>
									<td colspan="2" id="orgDes"></td>

								</tr>
							</tbody>
						</table>
						<img class="showLogo" src="" width="200px" height="150px">
					</div>


					<!-- 编辑信息 -->
					<div id="editInformation" style="display: none">
						<form id='validate'>
							<table class="tableEditInformation">
								<tbody>
									<tr class="tableEditInformationTr">
										<th class="star">机构名称：</th>
										<td><input id="editOrgName" type="text" name="orgName"
											class="form-control filter_worf" placeholder="请输入机构名称">
										</td>
										<td rowspan="3" class='editlogoTb'>
											<img id="editlogo" class="orgLogoImg" width="200px" height="150px">
											<span id="editOrgLogoImg" class="editOrgLogoImg btn btn-info">更换图片</span>
										</td>
									</tr>
									<tr class="tableEditInformationTr">
										<th class="star">联系电话:</th>
										<td><input id="editOrgtel" type="text" name="orgtel"
											class="form-control filter_worf" placeholder="请输入机构电话"></td>
									</tr>
									<tr class="tableEditInformationTr">
										<th class="star">机构负责人:</th>
										<td><input id="editOrgLegalPerson" name="orgLegalPerson"
											type="text" class="form-control filter_worf"
											placeholder="请输入机构负责人"></td>
									</tr>

									<tr class="tableEditInformationTr">
										<th style="display:inline-block;line-height: 45px;">机构简介:</th>
										<td colspan="2"><script id="editor" type="text/plain"
											name="gdesc" style="width: 100%; height: 80px;"></script></td>
									</tr>

									<tr class="tableEditInformationTr address">
										<th class="star" style="vertical-align: text-top;">地址:</th>
										<td colspan="2">
											<select id="provinceAddress" class="show-tick form-control widthInputC"  	data-style="btn-info"></select>
											<select id="cityAddress"
												class="show-tick form-control widthInputC" data-style="btn-info"></select>
											<select id="countyAddress" class="show-tick form-control widthInputC"
												data-style="btn-info" name="countyAddress"></select><br />
											<input id="detailedAddress" type="text" class="form-control filter_worf"
											placeholder="请输入详细地址" name="detailedAddress">
										</td>

									</tr>
									<tr class="tableEditInformationTr">
										<td colspan="3"><div id="container"></div></td>
									</tr>
								</tbody>
							</table>
						</form>
					</div>
				</div>
				<script src="js/org/identityAuthentication.js"></script>
				<script src="js/org/licenceAuthentication.js"></script>
				<div id="authenticationInformation">
					<div class="titleDiv">
						<span class="titleDiva">安全信息</span>
					</div>
					<div class="authentication_box" id="identity_box">
						<div class="authentication_icon">
							<img src="img/org/authentication/identity.png">
						</div>
						<div class="authentication_info">
							<div class="authentication_type">身份证认证</div>
							<div class="authentication_status"></div>
							<div class="authentication_statement"></div>
						</div>
						<div class="authentication_buttons"></div>
					</div>
					<hr>

					<div class="authentication_box" id="licence_box">
						<div class="authentication_icon">
							<img src="img/org/authentication/licence.png">
						</div>
						<div class="authentication_info">
							<div class="authentication_type">营业执照认证</div>
							<div class="authentication_status"></div>
							<div class="authentication_statement"></div>
						</div>
						<div class="authentication_buttons"></div>
					</div>
				</div>

			</div>
		</div>
	</div>

	<div class="modal fade" id="authenticationModal" tabindex="-1"
		role="dialog" aria-labelledby="authenticationModalLabel"
		aria-hidden="true" data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="authenticationModalLabel">身份证认证</h4>
				</div>
				<div class="modal-body"></div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="licenceModal" tabindex="-1" role="dialog"
		aria-labelledby="licenceModalLabel" aria-hidden="true"
		data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="licenceModalLabel">营业执照认证</h4>
				</div>
				<div class="modal-body"></div>
			</div>
		</div>
	</div>

	<script src="js/org/orgInfo.js"></script>
	<script src="js/footer.js"></script>
</body>
</html>