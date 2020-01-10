/*
 * @author junguo.fan
 * @version 2.0 增加搜索城市功能
 * */
$(document).ready(function() {
	
	var spanid;
	$.post('chooseCity/getProvinces.do', function(data) {
			// 获取省份数据
			var span_province="";
			for(var i=0;i<data.length;i++){
				span_province=span_province+"<span class='dp-city dp-province' id='"+data[i].id+"'>"+data[i].shortname+"</span>"
			}
			$(".provinces-wrapper").html(span_province);
		});

	//点击省份,展示省份下拉框
	$("#choose-province,#p1").click(function() {
		$("#dp-provinces").css("display", "block");
	});
	
	//点击任意位置，省份div隐藏/城市div隐藏
	$("body").click(function(event) {
		var target = event.target.id;
		if ( (target != 'choose-province')&&(target!='p1') ){
			if ($("#dp-provinces").css("display") == 'block') {
				$("#dp-provinces").css("display", "none");
			}
		}
		if ((target!='choose-city')&&(target!='p2')) {
			if ($("#dp-cities").css("display") == 'block') {
				$("#dp-cities").css("display", "none");
			}
		}
	});

//获取选择的省份
	$("#dp-provinces").click(function(event) {
		spanid=event.target.id;
		if (spanid!='dp-provinces') {
			var pro=$('#'+spanid).text();
			$("#p1").text(pro);
		}
	});
//选择城市 需要判断省份是否已经选择
	$('#choose-city').click(function() {
		var vv=$("#p1").text();

		if (vv!='省份') {
			$.post('chooseCity/getCities.do', {id: spanid}, function(data) {
				var city="";
				for (var i = 0; i < data.length; i++) {
					city=city+"<a href='location/redirect.do?cityid="+data[i].id+"&cityname="+data[i].shortname+"' class='dp-city'>"+data[i].shortname+"</a> "
				}
				$(".cities-wrapper").html(city);
				$('#choose-city').css('cursor','pointer');
				$("#dp-cities").css("display", "block");
			});
		}		
	});
	//获取热门城市
	$.get('chooseCity/getTopcities.do', function(data) {
		var topcity='';
		for (var i = 0; i < data.length; i++) {
			topcity=topcity+"<a href='location/redirect.do?cityid="+data[i].cityid+"&cityname="+data[i].cityname+"' class='linkcity'>"+data[i].cityname+"</a>"
		}
		$("#hotcitylist").html(topcity);
	});

	//获取首字母and城市list
	$.get('chooseCity/getFirstChars.do', function(data) {
		var letterlist=''; //按字母选择的html代码
		var allcity_div='';
		for (var i = 0; i < data.length; i++) {
			letterlist=letterlist+"<a class='linkcity' id='letter"+data[i]+"'>"+data[i]+"</a>";
			allcity_div=allcity_div+"<div class='cityarea' id='"+data[i]+"'><span class='city-label'>"+data[i]+"</span><span class='detail-citylist' id='id"+data[i]+"'></div>";
		}
		$("#letterlist").html(letterlist);
		$("#allcity").html(allcity_div);
		//获取详细城市list
		$.get('chooseCity/getCitylist.do', function(back) {
			for (var i = 0; i < data.length; i++) {
				var citylist='';
				var eachLetter=data[i];
				var temp_list=back[eachLetter];
				for (var j = 0; j < temp_list.length; j++) {
					citylist=citylist+"<a href='location/redirect.do?cityid="+temp_list[j].id+"&cityname="+temp_list[j].name+"' class='linkcity'>"+temp_list[j].name+"</a>";
				}
				$("#id"+eachLetter).html(citylist);
			}
		});
	});

	//给每个首字母动态绑定点击事件
	$("#letterlist").delegate('.linkcity', 'click', function() {
		var xx=$(this).html();
		document.getElementById("id"+xx).scrollIntoView();
	});
	
	//直接输入搜索城市
	let $inputSearch=$('.input_search_city');
	let timeout=null;
	$inputSearch.on('keyup',function() {
		if (timeout!=null) {
			clearTimeout(timeout);
		}
		//延时发送请求
		if ($inputSearch.val().trim()!='') {
			timeout=setTimeout(function(){
				let key=$inputSearch.val().trim();
				$.post('searchForCities.do', {keyword: key}, function(data) {
					let citylist=data.citylist;
					let html='';
					let $searchCitylist=$('.search_citylist');
					if (0!=citylist.length) {
						for (var i = 0; i < citylist.length; i++) {
							html=html+'<a href="location/redirect.do?cityid='+citylist[i].areas_id+'&cityname='+citylist[i].areas_shortname+'" class="search_result">'+citylist[i].areas_shortname+'</a>';
						}
					}else{
						html='未找到匹配城市';
					}
					$searchCitylist.html(html);
					$searchCitylist.css('display', 'block');
				});
			},300);
		}
	});

	$inputSearch.blur(function() {
		setTimeout(function(){
			$('.search_citylist').css('display', 'none');
		},1000);	
	});
});
