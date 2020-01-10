/**
 * 当前网站中,有多处用到了查看考勤明细.为方便实现和维护,单独抽出了一个js,用于在页面中写入考勤明细表和相关功能、css样式.
 * 当需要展示考勤明细时,引入此js.然后在需要的地方调用方法即可.调用方式:obj.loadAttendanceDetail(studentId,classId,billPeriod)
 * 
 * todo:在机构中心,展示考勤明细的地方没有用此js,需要替换成js
 */
$('head').append(`
<style>
	.detail_table_container{
	position: absolute;
	z-index: 120;
	background: #fff;
	top: 120px;
	left: 5vw;
	padding: 30px 10px 10px;
	display: none;
	border-radius: 4px;
/* 	width: 90vw; */
	overflow: auto;
}
/* Extra small devices (phones, 600px and down) 
@media only screen and (max-width:1399px){
    .detail_table_container{
		width: 90vw;
		left: 5vw;
	}
}*/
/* Extra large devices (large laptops and desktops, 1200px and up) 
@media only screen and (min-width: 1400px) {
    .detail_table_container {
		width: 80vw;
		left: 10vw;
    }
}*/

.detail_table td,.detail_table th{
	border: 1px solid #ccc;
    padding: 8px 10px;
    text-align: center;
    font-weight: normal;
    font-size: 13px;
    word-break: keep-all;
    white-space: nowrap;
}
.detail_close{
	background-image: url(img/icon/icon_close.png);
	width: 24px;
    height: 24px;
    float: right;
    margin-top: -26px;
    cursor: pointer;
}
.overflow_div{
	overflow: auto;
	margin-top: 10px;
}
.green{
	color: green;
}
.red{
	color: red;
}
.orange{
	color:orange;
}
.background{
	position: fixed;
	top: 0;
	background: #ccc;
	opacity: 0.5;
	z-index: 10;
	width: 100%;
	height: 100%;
	display: none;
}
</style>
`);
var obj;
$(function(){
	var $body=$('body');
	$body.append(`<div class="detail_table_container">
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
			<div class="background"></div>`);
	
	//查看考勤明细
	let $background=$('.background');
	let $detailTableContainer=$('.detail_table_container');
	let $tableHead=$('.table_head');
	let $tableBody=$('.table_body');
	
	/**
	 * 方法:请求学生考勤明细,并写入页面
	 * 参数: 
	 * 		studentId-学生id
	 * 		classId-班级id
	 * 		billPeriod-要查询的帐期
	 */
	obj={
		loadAttendanceDetail:function(studentId,classId,billPeriod){
			$background.css({
				'z-index': 110,
				'display': 'block'
			});
			//计算尺寸
			let width=$detailTableContainer.width();
			$detailTableContainer.css("left",`calc((100vw - ${width}px) / 2)`);
			
			$detailTableContainer.css('display', 'block');
			
			$.post('attendance/getAttendanceDetail.action', 
					{studentId: studentId,classId:classId,billPeriod:billPeriod}, 
					function(json) {
						if(json.code=600){
							let data=json.data;
							let theadHtml=tbodyHtml='';
							for (var i = 0; i < data.length; i++) {
								theadHtml+='<th>'+data[i].date+'</th>';
								let att =data[i].attendance;
								if (att==2) {
									tbodyHtml+='<td class="green">出勤</td>';
								}else if(att==1){
									tbodyHtml+='<td class="orange">半天勤</td>';
								}else{
									tbodyHtml+='<td class="red">缺勤</td>';
								}
							}
							$tableHead.html(theadHtml);
							$tableBody.html(tbodyHtml);
						}
						
					});
			
		}
	}
	
	
	
	$body.on('click','.detail_close',function(){
		$background.css('z-index', 10);
		$background.css({
			'z-index': 10,
			'display': 'none'
		});
		$detailTableContainer.css('display', 'none');
		$tableHead.html('');
		$tableBody.html('');
	});
});