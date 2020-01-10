<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>爱上学习网-切换城市</title>
<link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<link href="css/headerStyle.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="css/changecity.css">	
<link rel="stylesheet" href="css/footer.css">
<script src="js/jquery-3.3.1.min.js"></script>
<script src="js/changecity.js"></script>

</head>
<body>
<div class="basecontainer">
<script type="text/javascript" src="js/headerstyle.js"></script>
 <div id="center">
 <!-- 选择城市 -->
   <div id="city">
  	<div id="dropdown">
  		<div id="choosecity">
  			<p class="header1">按省份选择 :</p>
  			<div id="choose-province"> 
  				<p id="p1">省份</p>
  				<i class="icon-drop"></i>
  				<!-- 选择省份的div -->
  				<div id="dp-provinces">
                        
                        <div class="provinces-wrapper clearfix">
                        <!--通过js从数据库获取省份-->
                    </div>
                    </div>
  			</div>
  			<div id="choose-city"> <p id="p2">城市</p>
  				<i class="icon-drop"></i>
  				<div id="dp-cities">                       
                        <div class="cities-wrapper clearfix">
                        	
                        </div>
                    </div>
  			</div>

  		</div>
  		<div id="searchname">
  			<p class="header1">直接搜索:</p>
  			<input class="input_search_city" type="text" placeholder="请输入城市中文或拼音">
  			<div class="search_citylist"></div>
  		</div>
  	</div>
  	
  </div>
 <!-- 热门城市 -->
	<div id="hotcity">
		<h6 class="my_h6">热门城市:</h6>
		<p id="hotcitylist">
		</p>
	</div>
<!-- 按字母选择 -->
	<div id="letter">
		<h6 class="my_h6">按字母选择：</h6>
		<p id="letterlist">
			
		</p>
	</div>
<!-- 所有城市list -->
	<div id="allcity"></div>
		

</div>
</div>
<script type="text/javascript" src="js/footer.js"></script>
</body>
</html>