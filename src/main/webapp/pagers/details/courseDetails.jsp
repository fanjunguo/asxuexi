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
<title>课程详情</title>
<link rel="stylesheet"
	href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="css/headerStyle.css" />
<link rel="stylesheet" href="css/footer.css" />
<link rel="stylesheet" href="css/detailPage/QAmodule.css" />
<link rel="stylesheet" href="css/details/courseDetails.css" />
<script src="js/jquery-3.3.1.min.js"></script>
<script
	src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="js/jquery.paging.min.js"></script>
<script src="js/login/md5.js"></script>
<script src="js/authorityFilter.js"></script>
<script src="js/details/courseDetails.js"></script>

</head>
<body>
	<script src="js/headerstyle.js"></script>
	<div class="content">
		<div class="course_wrap">
			<div class="course_detail" id="${courseId}">
				<div class="top_part">
					<div class="top_part_left">
						<div class="main_wrap">
							<div class="picture_wrap">
								<img src="">
							</div>
							<div class="btn_video_play">&#xea15</div>
							<div class="video_wrap">
								<video src="" controls loop></video>
								<div class="video_close">&#xea0f</div>
							</div>
						</div>
						<div class="thumb_wrap">
							<div class="thumb selected">
								<img src="">
							</div>
						</div>
						<div class="feature_part">
							<div class="feature_button">
								<span>&#xe9d7</span><a>收藏课程</a>
							</div>
							<div class="feature_button">
								<span>&#xe9cb</span><a>分享</a>
							</div>
						</div>
					</div>
					<div class="top_part_right">
						<div class="title_info">课程名称</div>
						<div class="price_info">
							<label class="price_title">价格</label><span class="price_symbol">¥</span><span
								class="price_value"></span>
						</div>
						<div class="package_info">
							<div class="warning_bar">请选择套餐</div>
							<label class="package_title">套餐</label>
						</div>
						<div class="length_info">
							<label class="length_title">课时</label>
							<div></div>
						</div>
						<div class="charging_info">
							<label class="charging_title">付费方式</label>
							<div class="tips">预付费</div>
						</div>
						<div class="feature_part">
							<div class="feature_button">
								<a>立即报名</a>
							</div>
							<div class="feature_button">
								<span>&#xe96c</span><a>咨询课程</a>
							</div>
						</div>
					</div>
				</div>
				<div class="bottom_part">
					<div class="other_info">
						<label class="other_title">基本信息</label>
						<div class="other_info_group course_teacher">
							<span>老师：</span>
							<div></div>
						</div>
					</div>
				</div>
			</div>
			<div class="org_abstract" id="${org_id}">
				<div class="org_logo">
					<img src="">
				</div>
				<div class="org_name">
					<label>机构名称</label>
					<div></div>
				</div>
				<div class="org_tel">
					<label>联系方式</label>
					<div></div>
				</div>
				<div class="org_address">
					<label>机构地址</label>
					<div></div>
				</div>
				<div class="feature_part">
					<div class="feature_button">
						<a target="_blank">机构详情</a>
					</div>
					<div class="feature_button">
						<a>收藏机构</a>
					</div>
				</div>
				<div class="org_course"></div>
			</div>
		</div>
		<div class="additional_wrap" id="additional_wrap">
			<div class="additional_info">
				<ul class="nav nav-tabs" role="tablist">
					<li role="presentation" class="active"><a
						href="#course_description" aria-controls="home" role="tab"
						data-toggle="tab">课程详情</a></li>
					<li role="presentation"><a href="#course_timetable"
						aria-controls="profile" role="tab" data-toggle="tab">课程安排</a></li>
					<li role="presentation"><a href="#ask_answer"
						aria-controls="messages" role="tab" data-toggle="tab">提问</a></li>
					<!--  
					<li role="presentation"><a href="#comment"
						aria-controls="settings" role="tab" data-toggle="tab">评价</a></li>
					-->
				</ul>
				<div class="tab-content">
					<div role="tabpanel" class="tab-pane active"
						id="course_description">
						<div class="tab_pane_content"></div>
					</div>
					<div role="tabpanel" class="tab-pane" id="course_timetable">
						<div class="tab_pane_content"></div>
					</div>
					<div role="tabpanel" class="tab-pane" id="ask_answer">
						<div class="tab_pane_content">
							<div class="question_content"></div>
							<div id="pagination" onselectstart="return false"></div>
						</div>
					</div>
					<!--  
					<div role="tabpanel" class="tab-pane" id="comment">
						<div class="tab_pane_content">敬请期待</div>
					</div>
					-->
				</div>
			</div>
			<div class="adv_container">
				<div class="adv_title">
					<span>为您推荐</span><span>广告</span>
				</div>
				<div class="adv_content"></div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="message_modal" tabindex="-1" role="dialog"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<div class="message_content"></div>
				</div>
			</div>
		</div>
	</div>

	<div class="background"></div>
	<div class="student_info_window panel panel-default">
		<div class="panel-heading">上课人信息</div>
		<div class="penal-body">
			<div class="choose_student"></div>
			<div class="new_student_form">
				<div class="one_line">
					上课人姓名: <input type="text" class="student_name"> <span
						class="tips">*请填写真实姓名</span>
				</div>
				<div class="one_line">
					<span class="letter_space">联系电话</span>: <input type="text"
						class="phone_num">
				</div>
				<a class="a_btn choose_btn">选择上课人</a>
			</div>

		</div>
		<div class="penal_footer">
			<span class="wrong_message"></span>
			<div class="btn not_allowed">提交订单</div>
			<div class="btn cancel">取消</div>
		</div>
	</div>
	<script src="js/footer.js"></script>

	<div class="modal fade" id="ask_modal" tabindex="-1" role="dialog"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title">课程咨询</h4>
					<div class="ask_container">
						<div>
							请输入您的问题。您可以前往<a href="personal/askAnswer.do" target="_blank">个人中心-我的问答</a>查看机构的回复。
						</div>
						<div>
							<textarea id="ask_content" autocomplete="off"></textarea>
						</div>
					</div>
					<div class="ask_buttons">
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						<button type="button" class="btn btn-success">确定</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>