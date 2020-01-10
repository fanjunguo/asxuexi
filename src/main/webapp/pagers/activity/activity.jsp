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
<title>活动</title>
<link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="css/headerStyle.css">
<link rel="stylesheet" href="css/footer.css">
<script src="js/jquery-3.3.1.min.js"></script>
<script src="js/validate/jquery.validate.js"></script>
<style>
input{
	outline: none;
}
.container{
	background-repeat: no-repeat;
	background-size: contain;
	margin: 0 auto;
	width: 1000px;
	height:500px;
	padding:0!important;
}
.form{
	width: 1000px;
	margin: 0px auto 40px;
	border: 1px solid #ccc;
    background: #dfe4f142;
    display: none;
}
.item_header{
	width: 240px;
    padding: 5px 10px;
    margin: 10px 0;
    background: #ff3030;
    color: #fff;
}
.item_header{
	background: -webkit-linear-gradient(left, red , #dfe4f142); /* Safari 5.1 - 6.0 */
	background: -o-linear-gradient(right, red, #dfe4f142); /* Opera 11.1 - 12.0 */
	background: -moz-linear-gradient(right, red, #dfe4f142); /* Firefox 3.6 - 15 */
	background: linear-gradient(to right, red , #dfe4f142); /* 标准的语法 */
}
.input_c {
    margin: 20px auto;
    width: 360px;
}
.input_c input{
	width:300px;
	border-radius: 4px;
    border: 1px solid #ccc;
    padding: 0 5px;
    height: 30px;
}
.input_c:before{
	content: '*';
	color: red;
}
.submit{
	margin: 34px auto;
	width: 100px;
	border: 1px solid;
	border-color: transparent;
	background: red;
	border-radius: 4px;
    padding: 5px;
    text-align: center;
    color: #fff;
    cursor: pointer;
    user-select:none;
    margin-bottom: 50px;
}
.modal-body {
	text-align: center;
}
/*验证*/
input.error{
    border:1px solid red;
}
.input_c span{
	display: inline-block;
	height: 30px;
	position: absolute;
	line-height: 30px;
	color: red;
    font-size: 13px;
    margin-left: 3px;
}

</style>
</head>
<body>
<script src="js/homepage_header.js"></script>
<div class="container">
</div>
<div class="form">
	<div class="item_header">我要报名</div>
	<form class='validtate_form'>
		<div class="input_c">姓名: <input name="name" class="name" type="text">
		</div>
		<div class="input_c">电话: <input name="tel" class="tel" type="text"></div>
	</form>
	<div class="submit">提交</div>
</div>

<div class="modal fade" id="myModal">
    <div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title" id="myModalLabel">
					
				</h4>
			</div>
			<div class="modal-body">
				网络异常,加载失败! 
				<p>可联系客服了解活动详情以及报名!</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">确定
				</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>
<script src="js/footer.js"></script>
</body>
<script src="js/activity/activity.js"></script>
<script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>

</html>