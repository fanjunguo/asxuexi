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
<title>爱上学习网介绍</title>
<link rel="stylesheet" href="css/headerStyle.css">
<style>
body {
	margin: 0;
	padding: 0;
}

header {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	background: rgba(255, 255, 255, .95);
	box-shadow: 0 1px 2px rgba(0, 0, 0, .2);
	z-index: 2;
}

.container {
	width: 1190px;
	margin: 0 auto;
	display: flex;
	justify-content: space-between;
}

.logo {
	padding: 15px 0;
}

.logo>img {
	height: 40px;
}

.navigation {
	display: flex;
}

.navigation>div {
	padding: 25px 10px;
}

.navigation>div>a {
	color: #4d4d4d;
	text-decoration: none;
}

.navigation>div>a:hover {
	color: red;
}

.content {
	height: 100%;
	width: 100%;
	position: absolute;
	z-index: 1;
}

.bg-img {
	position: relative;
	height: 100%;
	z-index: 1;
}

.bg-img>img {
	width: 100%;
	height: 100%;
	object-fit: cover;
}

.img-info {
	position: absolute;
	bottom: 40px;
	left: 20%;
	width: 60%;
	color: white;
	width: 60%;
	padding-top: 180px;
}

.img-info>h1 {
	font-weight: lighter;
	font-size: 40px;
}

.img-info>div {
	border-top: 1px solid white;
	margin-top: 5px;
	padding: 5px 0;
}

.detail {
	width: 60%;
	left: 20%;
	position: relative;
	padding: 40px 0;
	color: #4d4d4d;
	line-height: 30px;
}

.detail>h2 {
	padding-bottom: 10px;
	padding-top: 10px; border-bottom : 1px solid #ccc;
	margin-bottom: 10px;
	border-bottom: 1px solid #ccc
}

.position-group {
	display: flex;
	flex-flow: wrap;
}

.position {
	width: 33%;
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 20px 0;
}

.position>div {
	width: 120px;
	height: 120px;
	border-radius: 50%;
	border: 1px solid white;
	text-align: center;
	line-height: 120px;
	font-size: 25px;
	color: white;
}

.position>p {
	color: #747474;
	padding-top: 5px;
}

.bgcolor-1 {
	background-color: #ef343b;
}

.bgcolor-2 {
	background-color: #4283ce;
}

.bgcolor-3 {
	background-color: #d1c080;
}

.bgcolor-4 {
	background-color: #df6dbc;
}

.bgcolor-5 {
	background-color: #77c159;
}

.bgcolor-6 {
	background-color: #a7b2b8;
}

.bgcolor-7 {
	background-color: #ffd80a;
}

.bgcolor-8 {
	background-color: #ef7034;
}

.bgcolor-9 {
	background-color: #45dad3;
}
</style>
</head>
<body>
	<header>
		<div class="container">
			<div class="logo">
				<img src="img/logo.png">
			</div>
			<div class="navigation">
				<div>
					<a target="_blank" href="#">首页</a>
				</div>
				<div>
					<a href="pagers/company.jsp#description">公司简介</a>
				</div>
				<div>
					<a href="pagers/company.jsp#recruit">招贤纳士</a>
				</div>
				<div>
					<a href="pagers/company.jsp#business">商务合作</a>
				</div>
				<div>
					<a href="pagers/company.jsp#contact">联系方式</a>
				</div>
			</div>
		</div>
	</header>
	<div class="content">
		<div class="bg-img">
			<img src="img/company/bg-img-1.jpg">
			<div id="description" class="img-info">
				<h1>公司简介</h1>
				<div>
					<p>爱上学习网使命：让教育互联网化、智能化</p>
					<p>爱上学习网（asxuexi.cn）成立于2018年，致力于为中国众多教育机构和广大学生提供优质便捷的互联网服务。</p>
				</div>
			</div>
		</div>
		<div class="detail">
			爱上学习网是服务于教育机构和学生的教育平台，平台为教育机构提供展位、招生、学生管理、课程提醒、订单管理、考勤等诸多用途；为学生提供优质课程、就近课程和培训机构、上课提醒、上课人管理等诸多用途，让传统教育快速实现“互联网+教育”，并且技术团队会根据市场需要及时迭代升级新产品，更有效地满足更多客户群体。
		</div>
		<div class="bg-img">
			<img src="img/company/bg-img-2.jpg">
			<div id="recruit" class="img-info">
				<h1>招贤纳士</h1>
				<div>
					<p>爱上学习网面向社会招聘各路英才。有意者请联系18254644076。</p>
				</div>
			</div>
		</div>
		<div class="detail">
			<div class="position-group">
				<div class="position">
					<div class="bgcolor-1">Java</div>
					<p>Java开发工程师</p>
				</div>
				<div class="position">
					<div class="bgcolor-2">Web</div>
					<p>前端开发工程师</p>
				</div>
				<div class="position">
					<div class="bgcolor-3">UI</div>
					<p>UI设计师</p>
				</div>
				<div class="position">
					<div class="bgcolor-4">UE</div>
					<p>交互设计师</p>
				</div>
				<div class="position">
					<div class="bgcolor-5">Android</div>
					<p>Android开发工程师</p>
				</div>
				<div class="position">
					<div class="bgcolor-6">iOS</div>
					<p>iOS开发工程师</p>
				</div>
				<div class="position">
					<div class="bgcolor-7">运营</div>
					<p>内容运营</p>
				</div>
				<div class="position">
					<div class="bgcolor-8">市场</div>
					<p>市场管理</p>
				</div>
				<div class="position">
					<div class="bgcolor-9">销售</div>
					<p>销售人员</p>
				</div>
			</div>
		</div>
		<div class="bg-img">
			<img src="img/company/bg-img-3.jpg">
			<div id="business" class="img-info">
				<h1>商务合作</h1>
				<div>
					<p>欢迎机构或个人前来洽谈网站业务。详情请致电18254644076。</p>
				</div>
			</div>
		</div>
		<div class="detail">
			<h2>商业模式</h2>
			<p>为幼儿、小初高、艺术、体育、大学、公考、考证、职业教育、语言培训、计算机、兴趣拓展等领域内的培训机构提供技术支持及推广服务，收取一定的服务费，以及交易佣金。</p>
			<p>为普通用户提供方便快捷的通道找到适合自己的课程或机构，积累用户，提升网站知名度。</p>
			<p>提供其他有助于学习的服务，如售卖书籍，学习辅导等，收取相应服务费用。</p>
			<p>提供广告栏位，收取广告费。</p>
			<h2>市场策略</h2>
			<p>组建市场管理网络，市场销售网络，不断使用人力切入全国各个城市。</p>
			<p>结合目前资金状况，线下采用地推、路演商演及发布会的形式，线上采用网络推广的形式，主要先从三四线城市开始推广，先帮助三四线城市教育机构和普通用户实现教育互联网化，再逐步向一二线城市蔓延。</p>
			<h2>招商加盟，共筑梦想</h2>
			<p>欢迎加入爱上学习网，与我们一同见证奇迹！</p>
		</div>
		<div class="bg-img">
			<img src="img/company/bg-img-4.jpg">
			<div id="contact" class="img-info">
				<h1>联系方式</h1>
			</div>
		</div>
		<div class="detail">
			<h2>地址</h2>
			<p>东营市南一路228号东营软件园C座</p>
			<h2>座机</h2>
			<p>0546-6079686</p>
			<h2>手机</h2>
			<p>18254644076</p>
			<h2>QQ</h2>
			<p>1059327557</p>
			<h2>邮箱</h2>
			<p>zyj@asxuexi.cn</p>
		</div>
	</div>

</body>
</html>