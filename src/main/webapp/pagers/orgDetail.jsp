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
<title>机构详情</title>
<link rel="stylesheet" href="css/overspend/map.css" /> 
<link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="css/detailPage/orgDetail.css" /> 
<link rel="stylesheet" href="css/detailPage/QAmodule.css">
<script src="js/jquery-3.3.1.min.js"></script>
<script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src = "https://api.map.baidu.com/api?v=3.0&ak=P9yAZyPp52QY8L2Hfk3gm655P4OnDjRz">  
</script>
<script src="js/login/md5.js"></script>
<script src="js/authorityFilter.js"></script>	
<script src="js/orgDetail.js"></script>
<script src="js/overspend/map.js"></script>
<link rel="stylesheet" href="css/footer.css">
<link rel="stylesheet" href="css/headerStyle.css"/>
</head>
<style>
#org_img{
background-image: url(../img/orgDown.jpg) 
}
</style>
<body>
<script src="js/headerstyle.js"></script>
<div class="body"  orgId='<%=request.getParameter("orgId") %>'>
		<div class="org">
			<div class="orgImage">
				<div class="orgImageShow" id="">
					<img alt="" src="">
				</div>
			</div>
						<div class="orgInfo">
				<div class="orgInfoTitle">
					<span class="orgInfoName" id="orgInfoName"></span>
					<span class="org_validation" id="org_validation">
						<img src="img/icons/org_validation_orange.png">资质认证</span>
					<span class="orgInfoLink" id="orgInfoLink"><a >进入机构网站>></a></span>
				</div>
				<hr>	
				
				<div class="orgInfoHead orgInfoHeight" id="orgInfoHead">
					负责人：<span class="" id=""></span>
				</div>
				<div class="orgInfoTel orgInfoHeight" id="orgInfoTel">
				电话：<span></span>
				</div>
					
				<div class="orgInfoAddr" id="orgInfoAddr">
					地址：<span></span>
						<span id='spana'>&nbsp</span>
				</div>
				<div class="orgInfoDes" id="orgInfoDes">
					简介：
					<span> </span>
				</div>
			</div>
			<div class="collection"><span ></span></div>
		</div>
		<div class="orgCourseTitle">
			<span class="showOrgCourse showQuestionAndAnswer">机构课程</span>
		</div>
		<div class="showCourse">
			<div class="courseInfo">
				
			</div>
		</div>
		<div class="questionAndAnswerTitle">
			<span class="showQuestionAndAnswer">展示问答</span>
			<span class="questions" id="questions">提问</span>
		</div>
			<div class="questionAndAnswer">
				<div class="QAATable">
					<table>
					</table>
				</div>
				<div class="QAAPager">
					<ul class="paginator" onselectstart="return false;"></ul>
				</div>
			</div>
		</div>
		
		<div id="questionsPanel">
			<div class="cover">
				
			</div>
			<div class="contentPanel">
					<div class="headerPanel">
						提问<span class="panelColse colsePanel" ></span>
					</div>
					<div class="bodyPanel">
						您对机构的疑问将推送给机构,Ta们来为您解答<br />
					<div class="textarea" id="questionContent">
						<textarea rows="4" cols="50" placeholder="请输入您的问题吧~" class='filter_worf' id="filter_worf_text"></textarea>
					</div>
					<span class='filter_words'></span>
					</div>
					<div class="footerPanel">
						<span id="ask">提问</span><span class="colsePanel">取消</span>
					</div>
				</div>
		</div>
		<script src="js/footer.js"></script>
</body>
</html>