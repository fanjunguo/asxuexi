/*
* created by 爱上学习网 范俊国
* 机构所有课程页面js:用来加载网站头部内容和所有课程内容
*/

$(document).ready(function() {

	let pathName=window.location.pathname;
	let orgId=pathName.substring(pathName.lastIndexOf('/')+1,pathName.indexOf('.'));
	orgId='org_1545994871';
	$.post('orgWebsite/getHeader.do', {orgId: orgId}, function(data) {
		if (data.result=='success') {
			$('.header_container').html(data.page_html);
			
			//机构所有课程所属的分类
			let sortArray=new Array();
			let $lessionList=$('.lession_list');
			let $lessionDisplay=$('.lession_display')
			$.post('orgCourse/getOrgInfo.do', {orgId: orgId}, function(data) {
				//获取机构所有的分类
				for (var i = 0; i < data.length; i++) {
					//写入左侧所有分类
					let sortId=data[i].sort_id;
					
					if (!sortArray.includes(sortId)) {
						sortArray.push(sortId);
						$lessionList.append('<div class="sort_third" sortId='+sortId+'>'+data[i].sort_name+'</div>')
					}

					//写入右侧课程
					$lessionDisplay.append('<div class="lession_item">\
						<img class="courseImg" src="http://www.asxuexi.cn/asxuexi_resource/org_1545994871/img/1ee452bac31421277ecab1b3c663cdf.jpg">\
						<div class="courseName">'+data[i].coursename+'</div>\
						<div class="coursePrice">\
							<span class="priceTyep">'+data[i].typename+'</span><span class="price">¥'+data[i].showingprice+'</span>\
						</div>\
					</div>');
				}

			});
	
		}else{
				let wrongHtml='<div class="wrong_container">\
						<img src="img/organization_website/tips.png">\
						<p>该机构暂未开通网站</p>\
					</div>';
				$('body').html(wrongHtml);
				$('title').html('404-错误');
			}
		});



});