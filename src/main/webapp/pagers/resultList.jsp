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
<title>搜索结果-爱上学习网</title>
<link
	href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet" href="css/headerStyle.css">
<link rel="stylesheet" href="css/footer.css">
<link rel="stylesheet" href="css/overspend/map.css">
<link rel="stylesheet" href="css/JQPaginate/JQPaginate.css">
<link rel="stylesheet" href="css/resultlist.css">
<script src="js/jquery-3.3.1.min.js"></script>
<script
	src="https://api.map.baidu.com/api?v=3.0&ak=P9yAZyPp52QY8L2Hfk3gm655P4OnDjRz"></script>
<script
	src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="js/overspend/map.js"></script>
<script src="js/resultlist.js"></script>

</head>
<body>
	<script src="js/headerstyle.js"></script>
	<div class="page">
		<div class="center_body">
			<div class="page_index">
				<a class="homepage" href="#">爱上学习网</a> <a
					class="search_content"> <span class="search_city_name">${search_city_name}</span>
					<span class="search_keyword">${search_keyword}</span>
				</a>
				<div class="hidden">
					<input class="hidden_input" id="search_keyword" type="hidden"
						value="${search_keyword}"> <input class="hidden_input"
						id="search_city_id" type="hidden" value="${search_city_id}">
					<input class="hidden_input" id="search_city_name" type="hidden"
						value="${search_city_name}"> <input class="hidden_input"
						id="search_content_type" type="hidden"
						value="${search_content_type}"> <input
						class="hidden_input" id="search_sort_id" type="hidden"
						value="${search_sort_id}"> <input class="hidden_input"
						id="search_sort_level" type="hidden" value="${search_sort_level}">
				</div>
			</div>
			<div class="page_content">
				<div class="top_container">
					<div class="filter_selected">
						<label>已选择条件</label>
						<div>
							<span class="search_condition sort_condition">分类条件</span> <span
								class="search_condition area_condition">地区条件</span> <span
								class="search_condition brand_condition">品牌条件</span> <span
								class="clear_condition">清除全部</span>
						</div>
					</div>
					<div class="filter_option"></div>
				</div>
				<div class="center_container">
					<div class="left_container">
						<div class="sort_option">
							<div class="sort_component selected default_sort">智能排序</div>
							<div class="sort_component price_sort">价格排序</div>
							<div class="sort_component popularity_sort" style="display: none">人气最旺</div>
							<div class="sort_component score_sort" style="display: none">评价最优</div>
						</div>
						<div class="result_list"></div>
						<div class="list_pager">
							<ul class="paginator" onselectstart="return false;">
							</ul>
						</div>
					</div>
					<div class="right_container">
						<div class="adv_title">
							<span>为您推荐</span><span>广告</span>
						</div>
						<div class="adv_container"></div>
					</div>
				</div>

			</div>
		</div>
	</div>
	<script src="js/footer.js"></script>
</body>
</html>