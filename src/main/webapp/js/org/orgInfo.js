$(document).ready(function() {
	var Point={};
 //用户定义坐标
	var addrMap={};
// 机构所在城市
	var provinceId;
	var cityId;
	var countyId;//机构所在城市
	var LogoImg="";//原来的图片
//	首先请求数据
//	定义id
	var $orgName=$("#orgName");
	var $orgLogoImg=$("#orgLogoImg");
	var $orgtel=$("#orgtel");
	var $orgLegalPerson=$("#orgLegalPerson");
	var $orgAddress=$("#orgAddress");
	var $orgDes=$("#orgDes");
//	编辑div
	var $editOrgName=$("#editOrgName");
	var $editOrgLogoImg=$("#editOrgLogoImg");
	var $editOrgtel=$("#editOrgtel");
	var $editOrgLegalPerson=$("#editOrgLegalPerson");
	var $editOrgAddress=$("#detailedAddress");
	var $editOrgDes=$("#editor p");
	
	var $savaShowInformation=$("#savaShowInformation");
	var $editShowInformation=$("#editShowInformation");
	var $editCancel=$("#edit_cancel");
	var $editInformation=$("#editInformation");
	var $showInformation=$("#showInformation");
	
	var $provinceAddress=$("#provinceAddress");
	var $cityAddress=$("#cityAddress");
	var $countyAddress=$("#countyAddress");
	$editInformation.hide();
	$.get("asxuexi/org/orgInfo.action",function(data){
		if(data!=""){
			$orgName.html(data.orgname);
			$orgtel.html(data.tel);
			$orgLegalPerson.html(data.head);
			let seperator=" ";
			let provinceAndCity=data.province+seperator+data.city+seperator+data.county+seperator;
			if(data.address==null){
				$orgAddress.html("<span id='orgAddressa'>"+provinceAndCity+" </span><span id='orgAddressb'></span>");
			}else{
				$orgAddress.html("<span id='orgAddressa'>"+provinceAndCity+"</span><span id='orgAddressb'>"+data.address+"</span>");
			}
			$orgDes.html(data.des);
			$editOrgName.val(data.orgname);
			$editOrgtel.val(data.tel);
			$editOrgLegalPerson.val(data.head);
			$editOrgAddress.val(data.address);
			var editOrgDes=data.des;
			if(data.name!=null&&data.name!=""){
				$(".showLogo").attr("src",data.name);
				LogoImg=data.name;
			}else{
				$(".showLogo").attr("src",'img/logo.png');
			}
			Point.lng=data.lng;
			Point.lat=data.lat;
			provinceId=data.provinceId;
			cityId=data.cityId;
			countyId=data.countyId;
			addrMap.province=data.province;
			addrMap.city=data.city ;
			addrMap.county=data.county;
//			容易报错  editor “TypeError: f.body is undefined”
			UE.getEditor('editor').setContent(editOrgDes);
		}
	});
	//显示编辑div
	$editShowInformation.on("click",function(){
		$editInformation.show();
		$showInformation.hide();
		$savaShowInformation.show();
		$editShowInformation.hide();
		$editCancel.show();

		//将机构头像写入
		let logoImg=$('.showLogo').attr('src');
		$('#editlogo').attr('src', logoImg);


		getAreas( 100000,$provinceAddress,provinceId);
		getAreas( provinceId,$cityAddress,cityId);
		getAreas( cityId,$countyAddress,countyId);
		//调用地图
		orgMap(Point.lng,Point.lat,"point");
		$("#detailedAddress").val($("#orgAddressb").text())
		//给文本编译器赋值
		UE.getEditor('editor').setContent($orgDes.html());

		//文本库获得焦点时,错误提示文字消息
		$("#ueditor_0").contents().find("body").focusin(function() {
			$('.illegal_word').remove();
		});
		
	});
//	点击取消按钮
	$editCancel.on("click",function(){
		$editInformation.hide();
		$showInformation.show();
		$savaShowInformation.hide();
		$editShowInformation.show();
		$editCancel.hide();
	})
	
	
//改变省市县
	$provinceAddress.on("change",function(){
		$cityAddress.empty();
		$countyAddress.empty();
		provinceId=$provinceAddress.find("option:selected").attr("id");
		getAreas( provinceId,$cityAddress," ");
		orgMap(Point.lng,Point.lat,$provinceAddress.val())
	});
	$cityAddress.on("change",function(){
		$countyAddress.empty();
		cityId=$cityAddress.find("option:selected").attr("id");
		getAreas( cityId,$countyAddress,"",true);
		orgMap(Point.lng,Point.lat,$provinceAddress.val()+$cityAddress.val())
	});
	$countyAddress.on("change",function(){
		cityId=$cityAddress.find("option:selected").attr("id");
		var cente=$provinceAddress.find("option:selected").text()+$cityAddress.find("option:selected").text()+$countyAddress.find("option:selected").text();
		orgMap(Point.lng,Point.lat,$provinceAddress.val()+$cityAddress.val()+$countyAddress.val())
	});
//	上传logo
	$("#editOrgLogoImg").on("click",function(){
		$('body').loadImageManager('addOrgLogoImg',1);
	});
//	机构的logo
	$("body").on("click","#addOrgLogoImg",function(){
		var imgPath=$("#addOrgLogoImg").attr("path");
		$(".imageManager_container").remove();
		$("#editlogo").attr("src",imgPath);
	})

//	提交保存信息
	$("#savaShowInformation").on("click",function(){
		if($("#validate").valid()){
			filterWorf();
		}else{
			alert('请按要求填写信息');
		}
	});
//	提交保存方法
	function savePost(){
		var array=new Array();
		var content=UE.getEditor('editor').getContent();
		content=content.substring(3,content.lastIndexOf('<'));
		var obj={name:"editor",text:content};
		array.push(obj)
		var context=JSON.stringify(array);
		LogoSrc=$("#editlogo").attr("src");
		if(LogoSrc=="img/logo.png"){
			LogoSrc='';
		}
		 $.post("asxuexi/org/editOrg.action",{//提交信息
				file:LogoSrc,
				orgName:$editOrgName.val(),
				orgtel:$editOrgtel.val(),
				orgLegalPerson:$editOrgLegalPerson.val(),
				orgAddress:$editOrgAddress.val(),
				orgDes:content,
				lat:Point.lat,lng:Point.lng,
				localId:$countyAddress.find("option:selected").attr("id"),
				LogoImg:LogoImg,
			},function(json){
				if (json.code==600) {
					$(".showLogo").attr("src",$("#editlogo").attr("src"));
					$orgName.html($editOrgName.val());
					$orgtel.html($editOrgtel.val());
					$orgLegalPerson.html($editOrgLegalPerson.val());
					$orgAddress.html("<span id='orgAddressa'>"+$("#provinceAddress").find("option:selected").text()+" "+
						$("#cityAddress").find("option:selected").text()+" "+
						$("#countyAddress").find("option:selected").text()+" </span><span id='orgAddressb'>"+$("#detailedAddress").val()+"</span>");
					$orgDes.html(content);
					//隐藏显示
					$showInformation.show();
					$editInformation.hide();
					$editShowInformation.show();
					$savaShowInformation.hide();
					$('#edit_cancel').hide();
					preClickTime = 0;
				}else{
					alert('网络异常,保存失败');
				}
			});
	 
	}

//可以根据地址定位地图
//也可以根据坐标定位地图
	function orgMap(lng,lat,cente){
		var map = new BMap.Map("container");
		map.enableScrollWheelZoom(true);
		map.addControl(new BMap.NavigationControl());
		map.addControl(new BMap.ScaleControl({anchor:BMAP_ANCHOR_BOTTOM_LEFT}));
		var point = new BMap.Point(lng, lat); 
		var gc = new BMap.Geocoder(); 
		if(cente=="point"){//如果传输坐标
			map.centerAndZoom(point, 13);    
			var marker = new BMap.Marker(point); 
			map.addOverlay(marker);
		}else{
			gc.getPoint(cente, function(pt){ 
				if(pt){//解析成功
					map.centerAndZoom(pt, 13);
					map.addOverlay(new BMap.Marker(pt)); //如果地址解析成功，则添加红色marker
				}else{
					map.centerAndZoom(cente, 13);
					map.addOverlay(new BMap.Marker(cente)); //如果地址解析不成功，按地址添加红色marker
				}
			});
		}
		map.addEventListener("click",function(e){//点击地图获取坐标、地址
			Point.lat=e.point.lat;
			Point.lng=e.point.lng;
			var point = new BMap.Point(e.point.lng, e.point.lat);
			gc.getLocation(point, function(rs){
				var surrounding="";
				if(rs.surroundingPois.length>0){
					surrounding=rs.surroundingPois[0].title;
				}
				var addComp = rs.addressComponents;
					addrSelectChange(addComp);//对比省市县是否更改
					$editOrgAddress.val(addComp.street+addComp.streetNumber+" "+surrounding)
			});        
		});
		var ac = new BMap.Autocomplete({//联想文本框
		    "input" : "detailedAddress",
		    "location" : map,
		});
		var c=document.getElementById("detailedAddress").value;
		ac.setInputValue(c);
		//鼠标放在下拉列表的事件
		ac.addEventListener("onhighlight", function(e){
		    var str = "";
		    var _value = e.fromitem.value;
		    var value = "";
		    if(e.fromitem.index > -1){
		        value = _value.province + _value.city + _value.district + _value.street + _value.business;
		    }
		    str = "FromItem<br/>index = " + e.fromitem.index + "<br/>value = " + value;
		    value = "";
		    if (e.toitem.index > -1) {
		        _value = e.toitem.value;
		        value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
		    }    
		    str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
		    $("#detailedAddress").innerHTML = str;
		});

		var myValue;
		//鼠标点击下拉列表后的事件
		ac.addEventListener("onconfirm", function(e,target,item) {
			var _value = e.item.value;
		    myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
		    $("#detailedAddress").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " +myValue;
		    searchAddressMap(myValue);
		    $("#detailedAddress").val(_value.business)
		});
//		$("#detailedAddress").blur(function(){
//			var	_value=ac.getResults().getPoi(0)
//			var myValue=	_value.province +  _value.city +  _value.district +  _value.street +  _value.business;
//			searchAddressMap(myValue)
//		})
		$("#detailedAddress").keyup(function(event){
			if(event.keyCode == 13){
			var	_value=ac.getResults().getPoi(0)
			var myValue=	_value.province +  _value.city +  _value.district +  _value.street +  _value.business;
					searchAddressMap(myValue)
			}
		});
		function searchAddressMap(myValue){
//				获取列表中值，    myValue为详细信息值
//				myValue="东营市东营区东营软件园";
				 var local = new BMap.LocalSearch(map,   
				            {renderOptions: {map: map,autoViewport: true},pageCapacity: 8});
				 	map.clearOverlays();
				    local.search(myValue);
				    gc.getPoint(myValue, function(rs){
				    	Point.lat=rs.lat;
				    	Point.lng=rs.lng;
				    	var point=new BMap.Point(rs.lng, rs.lat)
				    	gc.getLocation(point, function(rs){
							var surrounding="";
							if(rs.surroundingPois.length>0){
								surrounding=rs.surroundingPois[0].title;
							}
							var addComp = rs.addressComponents;
							 addrSelectChange(addComp)
				    	});
				    });
			}
		
	}
	
	function addrSelectChange(addComp){
		if($provinceAddress.val()!=addComp.province){//更改省份
			addrMap.province=addComp.province;
			addrMap.city=addComp.city;
			addrMap.county=addComp.district;
			$provinceAddress.val(addrMap.province);
			editProvince(addComp.province,addComp.city,addComp.district)
		}
		if($cityAddress.val()!=addComp.city){//更改城市
			addrMap.city=addComp.city;
			addrMap.county=addComp.district;
			$cityAddress.val(addrMap.city);
			editCity($cityAddress.find("option:selected").attr("id"),addrMap.county)
		}
		if($countyAddress.val()!=addComp.district){//更改县区
			addrMap.county=addComp.district;
			$countyAddress.val(addrMap.county);
		}
	}
	
//	点击地图查询更改 select
//	更改省
	function editProvince(province,city,county){
		$cityAddress.empty();
		$countyAddress.empty();
		$.post("asxuexi/org/editProvince.do",{province:province,city:city,county:county},function(data){
			var cityappend="";
			var countyappend="";
			for(var i=0;i<data[0].length;i++){
				if(data[0][i].name==city){
					cityappend=cityappend+"<option id="+data[0][i].id+" value='"+data[0][i].name+"' selected = 'selected'>"+data[0][i].name+"</option>"
				}else{
					cityappend=cityappend+"<option id="+data[0][i].id+" value='"+data[0][i].name+"'>"+data[0][i].name+"</option>"	
				}
			}
			$cityAddress.append(cityappend);
			for(var i=0;i<data[1].length;i++){
				if(data[1][i].name==county){
					countyappend=countyappend+"<option id="+data[1][i].id+" value='"+data[1][i].name+"' selected = 'selected'>"+data[1][i].name+"</option>"
				}else{
					countyappend=countyappend+"<option id="+data[1][i].id+" value='"+data[1][i].name+"'>"+data[1][i].name+"</option>"	
				}
			}
			$countyAddress.append(countyappend);
		});
	}
//	更改城市
	function editCity(cityId,county){
		$countyAddress.empty();
		$.post("asxuexi/org/getAreas.do",{parentId:cityId},function(data){
			var append="";
			for(var i=0;i<data.length;i++){
				if(data[i].name==county){
					append=append+"<option id="+data[i].id+" value='"+data[i].name+"' selected = 'selected'>"+data[i].name+"</option>"
				}else{
					append=append+"<option id="+data[i].id+" value='"+data[i].name+"'>"+data[i].name+"</option>"	
				}
			}
			$countyAddress.append(append);
		});
	}
//	更改县城
	
	
//	请求地址下拉列表
/**
 * id 父级id
 * areasPosition 追加到哪
 * Level 当前城市id
 * map 是否加载地图
 * */	
	function getAreas( id,areasPosition,Level,map){
		$.post("asxuexi/org/getAreas.do",{parentId:id},function(data){
			var append="<option  value='0' selected = 'selected'>请选择</option>";
			for(var i=0;i<data.length;i++){
				if(data[i].id==Level){
					append=append+"<option id="+data[i].id+" value='"+data[i].name+"' selected = 'selected'>"+data[i].name+"</option>"
				}else if(i==0){
					append=append+"<option id="+data[i].id+" value='"+data[i].name+"' >"+data[i].name+"</option>"
				}else{
					append=append+"<option id="+data[i].id+" value='"+data[i].name+"'>"+data[i].name+"</option>"	
				}
			}
			areasPosition.append(append);
			if(map==true){
				var cente=$provinceAddress.find("option:selected").text()+$cityAddress.find("option:selected").text()+$countyAddress.find("option:selected").text();
//				orgMap(Lng,Lat,cente);
			}
		});
	};
/**
 * 验证是否存在敏感词
 * */	
	function filterWorf(){
		//写一个动画遮罩层.当返回数据时消失
		$('body').append('<div class="saving_bg"></div><div class="saving_animation"><div class="loading">\
				<h4>正在提交</h4>\
				<div class="loading-dot loading-dot-1"></div>\
				<div class="loading-dot loading-dot-2"></div>\
				<div class="loading-dot loading-dot-3"></div>\
				<div class="loading-dot loading-dot-4"></div>\
				<div class="loading-dot loading-dot-5"></div>\
				<div class="loading-dot loading-dot-6"></div>\
				<div class="loading-dot loading-dot-7"></div>\
				<div class="loading-dot loading-dot-8"></div>\
			</div></div>');

		var array=new Array();
		var returnV=true;
		var inputLength=$(".filter_worf").length;
		for(var i=0;i<inputLength;i++){
			var object={name:"",text:""}
			object.name=$(".filter_worf").eq(i).attr("id");
			object.text=$(".filter_worf").eq(i).val();
			array.push(object);
		}
		let descContent=UE.getEditor('editor').getContent();
		array.push({name:'editor',text:descContent});
		let word=JSON.stringify(array);
		$.post("filterWord/isFilterWord.do",{context:word},function(data){
			$('.saving_bg,.saving_animation').remove();
			 for(var i=0;i<data.length;i++){
				 if(data[i].text!=""){
				 	let $thisInput=$("#"+data[i].name);
				 	if (data[i].name=='editor') {
				 		$('.illegal_word').remove();
				 		$thisInput.after("<label class='illegal_word'>你输入的内容中有非法词汇:"+data[i].text+"</span>");
				 	}else{
				 		$('.illegal').remove();
				 		$thisInput.addClass('error');
				 		$thisInput.after("<label for="+data[i].name+" generated='true' class='error illegal'>你输入的内容中有非法词汇:"+data[i].text+"</span>")
				 	}
				 	returnV=false;
				 }
			 }
			 if(returnV){//调用post方法保存
				 savePost();
			 }
		});
	}


	$("#validate").validate({
		onfocusout: function(element){
			$(element).valid();
		},
		rules: {
			orgName: {  
			   	required: true,
               	rangelength:[2,20],
		  	},orgtel:{
				required: true,
			   	number:true,
		   	},orgLegalPerson:{
			   required: true,
               rangelength:[2,20] ,
           	}, countyAddress:{
	           required:true,
	       	},detailedAddress:{
	    	   required:true,
	       	}
	    },
	    messages: {  
        	orgName: {
        		required:"机构姓名不能为空",
        		rangelength:"输入长度为2-30字符"
        	},
        	orgtel:{
        		required: "联系电话不能为空",
        		number:"请输入正确的电话号码",
            },
            orgLegalPerson:{
            	required:"机构负责人不能为空",
            	rangelength:"输入长度为2-30字符" 
            },
            countyAddress:{
            	required:"请完整选择地址",
	        },
	        detailedAddress:{
	        	required:"详细地址不能为空",
		    }
        } 
	});
	var ue = UE.getEditor('editor', {
		    toolbars: [
		        ],elementPathEnabled : false,
		        maximumWords:100,
	});
	
});