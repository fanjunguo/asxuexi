$(document).ready(function(){
	$("input").val("");
	var choose="<option value=''>请选择</option>";
//	显示模态框
	$("#protocolModal").modal({ keyboard: false,backdrop: 'static',show:true });
	
	$("#disAgree,#protocolModal .close").on("click",function(){
		location.href="personal/personalInfo.do";
	});
	
	 $("#agree").on("click",function(){
		 $("#protocolModal").modal('hide');
	 });
	 $.get("OrgEnroll/address.action",function(data){
		 cityId=data.cityId;
		 provinceId=data.provinceId;
		 listCity=data.listCity;
		 listCounty=data.listCounty;
		 province="";
		 city="";
		 county="";
		 for(var i=0;i<listCity.length;i++){
			 if(listCity[i].LevelType==1){
				 if(listCity[i].id==provinceId){
					 province=province+"<option value='"+listCity[i].id+"' selected = 'selected'>"+listCity[i].name+"</option>"
				 }else{
					 province=province+"<option value='"+listCity[i].id+"'>"+listCity[i].name+"</option>"; 
				 }
			 }else{
				 if(listCity[i].id==cityId){
					 city=city+"<option value='"+listCity[i].id+"' selected = 'selected'>"+listCity[i].name+"</option>"
				 }else{
					 city=city+"<option value='"+listCity[i].id+"'>"+listCity[i].name+"</option>"; 
				 }
			 }
		 }
		 for(var i=0;i<listCounty.length;i++){
			 county=county+"<option value='"+listCounty[i].id+"'>"+listCounty[i].name+"</option>"; 
		 }

		 $("#provinceAddress").empty();
		 $("#cityAddress").empty();
		 $("#provinceAddress").append(province);
		 $("#cityAddress").append(city);
		 $("#countyAddress").append(county);
	 });
	 $("#provinceAddress").change(function() {
		 $("#cityAddress").empty();
		 $("#countyAddress").empty();
		 $("#cityAddress").append(choose);
		 $("#countyAddress").append(choose);
		 listAddress($("#provinceAddress").val(),$("#cityAddress"));
		});
	 $("#cityAddress").change(function() {
		 $("#countyAddress").empty();
		 $("#countyAddress").append(choose);
		 listAddress($("#cityAddress").val(),$("#countyAddress"));
		});
//	 保存信息
	 $("#sava").on("click",function(){
		 if($("#validate").valid()){
			 if($("#countyAddress").val()!=0){
				$.post("OrgEnroll/insertOrgInfo.action",{
					orgName:$("#orgName").val(),
					orgHead:$("#orgHead").val(),
					orgTel:$("#orgTel").val(),
					location:$("#countyAddress").val()
					},function(data){
						if(data==1){
							window.location.href='org/orgInfo.do';
						}else{
							alert("注册机构失败，请重试！");
						}
					})
			 }else{
				 alert("选择县区")
			 }
		 }else{
			 alert("您填写的信息不符合规范")
		 }
	 })
	 
	 
//	 listAddress 请求地址
	 function  listAddress (parenId,positioning){
		 $.post("OrgEnroll/listAddress.do",{parentId:parenId},function(data){
			var city="";
			 for(var i=0;i<data.length;i++){
				 city=city+"<option value='"+data[i].id+"'>"+data[i].name+"</option>"; 	 
			 }
			 positioning.append(city)
		 });
	 }
	 
//	 验证信息
	 $("#validate").validate({ 
		 onfocusout: function(element){
			 $(element).valid();
		 },
		    rules: {  
		    	orgName: {  
	                required: true,
	                rangelength:[2,20]
	               },
	              
	            	orgTel:{
	            		required:true,
	            		number:true
	            	},orgHead:{
	            		required:true,
	            		rangelength:[2,20]
	            	},countyAddress:{
	            		required:true,
	            	}
	            	
	      },
	      messages: {  
	    	  orgName: {
	    		  required:"请填写机构名称",
	    		  rangelength:"只能输入2—20个字符"
	    		  },
	    	
	    	  orgTel:{
	    		  required: "请填入电话号码",
	    		  number:"填写正确的手机号"
	    		  },
	    		  orgHead:{
	            		required:"请填写机构负责人",
	            		rangelength:"只能输入2—20个字符"
	            	},countyAddress:{
	            		required:"请选择县区",
	            	}
	      }  
	});
});
jQuery.validator.addMethod("isMobile", function(value, element) {  
	 var length = value.length;  
	 var mobile = /^(1[3456789]\d{9})$/;  
	 return this.optional(element) || (length == 11 && mobile.test(value));  
	}, "请正确填写手机号码");