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
<meta name="render" content="webkit">
<title>爱上学习网</title>

<link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<link href="css/homepage_style.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="css/headerStyle.css">
<link rel="stylesheet" href="css/homePage_nav.css">
<link rel="stylesheet" href="css/footer.css">
<link rel="stylesheet" href="css/swiper/swiper.min.css">
<script src="js/jquery-3.3.1.min.js"></script>

</head>
<body>
	<script src="js/homepage_header.js"></script>
	<div class="basecontainer">
		<div id="bannercontainer">
			<div id="letf_banner" style="width: 220px; float: left;">
				<div class="nav_containor">
					<div class="nav_left">
						<ul>
							<li data-id="0" style="height: 40px; font-size: 20px;"><span
								id="nav_title_fjg">全部分类</span></li>
						</ul>
					</div>
					<div class="nav_right"></div>
				</div>
			</div>

			<div id="right_banner">
				<div id="banner_menu">
					<ul></ul>
				</div>
				<div id="adv_banner">
					<!-- 轮播图 -->
					<div class="homepage_banner swiper-container">
						<div class="swiper-wrapper"></div>
						<!-- 如果需要分页器 -->
						<div class="swiper-pagination"></div>

						<!-- 如果需要导航按钮 -->
						<div class="swiper-button-prev"></div>
						<div class="swiper-button-next"></div>
					</div>
					<!-- 轮播图 -->
				</div>
			</div>
		</div>
		<div id="100000" class="items_container">
			<div class="item_container course_container">
				<div class="item_nav">
					<ul class="nav_ul">
						<li><a class="disabled item_name">精品课程</a></li>
						<li><a class="active tab_name">新品课程</a></li>
						<li><a class=" tab_name ">热门课程</a></li>
						<li><a class=" tab_name ">推荐课程</a></li>
					</ul>
				</div>
				<div class="tab_content"></div>
				<div class="tab_content"></div>
				<div class="tab_content"></div>
			</div>
			<div class="item_container org_container">
				<div class="item_nav">
					<ul class="nav_ul">
						<li><a class="disabled item_name">精品机构</a></li>
						<li><a class="active tab_name ">推荐机构</a></li>
						<li><a class=" tab_name ">热门机构</a></li>
					</ul>
				</div>
				<div class="tab_content"></div>
				<div class="tab_content"></div>
			</div>
		</div>
	</div>
	<script src="js/footer.js"></script>
</body>
<script src="js/swiper/swiper.min.js"></script>
<script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="js/homePage_nav.js"></script>
<script src="js/banner/banner.js"></script>
</html>