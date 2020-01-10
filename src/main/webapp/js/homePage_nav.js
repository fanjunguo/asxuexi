$(function() {
	let cityId=$('.cityname').attr('id');
	
	// 显示隐藏
	$('.nav_containor').on('mouseenter', function() {
		$(".nav_right").removeClass('nav_hide');
	}).on('mouseleave', function() {
		$(".nav_right").addClass('nav_hide');
		$(".nav_sub").addClass('nav_hide');
	}).on('mouseenter', 'li', function(e) {
		var li_data = $(this).attr('data-id');
		$(".nav_sub").addClass('nav_hide');
		$('.nav_sub[data-id="' + li_data + '"]').removeClass('nav_hide');
	})

	// 请求数据

	$.get("Navigation/getLeftNav.do", function(data) {
		var sortData = data.sortData[0];
		// 添加一级分类标签
		// 获取一级标签分几组
		for (var i = 1; i <= data.rowcount; i++) {
			// 右侧显示标签
			$(".nav_right").append("<div class='nav_sub nav_hide' data-id='" + i + "'></div>")
			// 获取一行中有多少个大类
			var number = 0;
			// 一级标签初始化
			var erji=""
			// 二级标签初始化
			for (var j = 0; j < data.sortData[0].children[i-1].children.length; j++) {
				var thrid="";
				for(var z=0;z<data.sortData[0].children[i-1].children[j].children.length;z++){
					thrid=thrid+"<a target='_blank' href='search.do?searchSortId=" + data.sortData[0].children[i-1].children[j].children[z].id+"'>"+data.sortData[0].children[i-1].children[j].children[z].text+"</a>"
				}
				erji=erji+"<dl><dt><a target='_blank' href='search.do?searchSortId=" + data.sortData[0].children[i-1].children[j].id+"'>" + data.sortData[0].children[i-1].children[j].text
				+ " <i> &gt;</i></a> </dt><br><dd>"+thrid+"</dd></dl>"
			}
			$("div[data-id='" + i + "']").append(erji)

			// 一共有多少个一级类
			// 去除最後一個字符‘/’
			$(".nav_left ul").append('<li class="addArrow" data-id=' + i + '>\
				<span class="homepage_menu"><a target="_blank" href="search.do?level=1&searchSortId='+data.sortData[0].children[i-1].id+'">' + data.sortData[0].children[i-1].text + '</a></span></li>');
		}

	});
	
	$.post("Navigation/getTopNav.do",{cityId:cityId}, function(data) {
		var topNavData = data.topNavData;
		for (var i = 0; i < topNavData.length; i++) {
			// <li data-id="0"> <span>全部菜单</span></li>
			var li = "<li id='" + topNavData[i].nav_top_id + "'><a target='_blank' href='"
					+ topNavData[i].nav_top_href + "'>"
					+ topNavData[i].nav_top_name + "</a></li>";
			$("#banner_menu ul").append(li);
		}
	});

})