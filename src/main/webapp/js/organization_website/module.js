function addModule(moduleid,targetPlace){
		let strpre=moduleid.substring(moduleid.indexOf("_")+1);
		let randomNum=Math.floor(Math.random()*900+100);
		let id=strpre+new Date().getTime()+randomNum;
		let html=$("."+strpre+"_style"+" .style_group.selected").html();
		if (undefined!=html) {
			let logohtml="<div class='all_used_background'><div id='"+id+"' class='"+strpre+"_background editable' style='width:100%'>"+html+"</div></div>"
		targetPlace.append(logohtml);
		}
		return id;
	}
	function addLogoStyle(stylename,targetPlace){
		let html="<div class='style_group'>"+
        
        			"<div class='logo_container "+stylename+" row' name='"+stylename+"'>"+
        				"<div class='logo col-xs-3'>"+
        					"<a class='logo_img_href' ><img class='logo_img img-basic' name='img-basic' src='img/organization_website/logo.png'></a>"+
        				"</div>"+
        				"<div class='title col-xs-9'>"+
        					"<a class='title_href' ><h1 style='margin: 0;text-align: center;font-size: 36px;line-height: 100px;height: 100px;color: #337ab7'>网站标题</h1></a>"+
        				"</div>"+
        			"</div>"+
        
        		 "</div>";
		targetPlace.append(html);
	}
	function loadLogoStyle(targetPlace){
		let html="<div class='logo_style'>"+
					"<p><b>整体样式</b></p>"+
					"<div></div>"+
				 "</div>";
		targetPlace.append(html);
		targetPlace=$(".logo_style div");
		addLogoStyle("logo-one",targetPlace);
		addLogoStyle("logo-two",targetPlace);
	}
	function addImgStyle(stylename,targetPlace){
		let name="";
		if ("img-basic"==stylename) {
			name="矩形";
		}
		if ("img-rounded"==stylename) {
			name="圆角矩形";
		}
		if ("img-circle"==stylename) {
			name="圆形";
		}
		if ("img-thumbnail"==stylename) {
			name="边框矩形";
		}
		let html="<div class='style_group' name='"+stylename+"'>"+
         			"<img src='img/organization_website/exampleimg.png' class='img "+stylename+"'> "+
         		 "</div>" +
         		 "<div class='style_name'>"+name+"</div>";
		targetPlace.append(html);
	}
	function loadImgStyle(targetPlace){
		let html="<div class='img_style'>"+
					"<p><b>图片样式</b></p>"+
					"<div class='row'>" +
						"<div class='col-xs-3'></div>"+
						"<div class='col-xs-3'></div>"+
						"<div class='col-xs-3'></div>"+
						"<div class='col-xs-3'></div>"+
					"</div>"+
				 "</div>";
		targetPlace.append(html);
		targetPlace=$(".img_style .row .col-xs-3:nth-child(1)");
		addImgStyle("img-basic",targetPlace);
		addImgStyle("img-rounded",targetPlace.next());
		addImgStyle("img-circle",targetPlace.next().next());
		addImgStyle("img-thumbnail",targetPlace.next().next().next());
	}
	function addLayoutStyle(stylename,targetPlace){
		let htmlbegin="<div class='style_group'>"+
					"<div class='"+stylename+" layout_container' name='"+stylename+"'>";				
		let length=0;
		let htmlbody="";
		let htmlend="";
		if (-1!=stylename.search("one")) {
			length=1;
			htmlend="</div></div><div class='style_name'>一列布局</div>";
		}
		if (-1!=stylename.search("two")) {
			length=2;
			htmlend="</div></div><div class='style_name'>二列布局</div>";
		}
		if (-1!=stylename.search("three")) {
			length=3;
			htmlend="</div></div><div class='style_name'>三列布局</div>";
		}
		if (-1!=stylename.search("four")) {
			length=4;
			htmlend="</div></div><div class='style_name'>四列布局</div>";
		}
		for (var i = 0; i < length; i++) {
			htmlbody +="<div class='layout_col'><div></div><div></div><div></div><div></div></div>";
		}
		let html=htmlbegin+htmlbody+htmlend;
		targetPlace.append(html);
	}
	function loadLayoutStyle(targetPlace){
		let html="<div class='layout_style'>"+
					"<p><b>布局样式</b></p>"+
					"<div class='parent'>"+
						"<div class='child'></div>"+
						"<div class='child'></div>"+
						"<div class='child'></div>"+
						"<div class='child'></div>"+
					"</div>"+
				 "</div>";
		targetPlace.append(html);
		targetPlace=$(".layout_style .parent .child:nth-child(1)");
		addLayoutStyle("layout-one",targetPlace);
		addLayoutStyle("layout-two",targetPlace.next());
		addLayoutStyle("layout-three",targetPlace.next().next());
		addLayoutStyle("layout-four",targetPlace.next().next().next());
	}
	function addImgTxtStyle(stylename,targetPlace){
		let html='<div class="style_group">'+
					'<div class="img_txt_container '+stylename+'" name="'+stylename+'">'+
						'<div class="container_cell img_cell"><img src="img/organization_website/exampleimg.png" class="img-basic" name="img-basic"/></div>'+
						'<div class="container_cell txt_cell">'+
							'<p>文本内容</p>'+
						'</div>'+
					'</div>'+
				 '</div>';
		targetPlace.append(html);
	}
	function loadImgTxtStyle(targetPlace){
		let html="<div class='img_txt_style'>"+
					"<p><b>图文样式</b></p>"+
					"<div class='parent'>"+
						"<div class='child'></div>"+
						"<div class='child'></div>"+
						"<div class='child'></div>"+
						"<div class='child'></div>"+
						"<div class='child'></div>"+
						"<div class='child'></div>"+
						"<div class='child'></div>"+
						"<div class='child'></div>"+
					"</div>"+
				  "</div>";
		targetPlace.append(html);
		targetPlace=$(".img_txt_style .parent .child:nth-child(1)");
		addImgTxtStyle("img-txt-one",targetPlace);
		addImgTxtStyle("img-txt-two",targetPlace.next());
		addImgTxtStyle("img-txt-three",targetPlace.next().next());
		addImgTxtStyle("img-txt-four",targetPlace.next().next().next());
		addImgTxtStyle("img-txt-five",targetPlace.next().next().next().next());
		addImgTxtStyle("img-txt-six",targetPlace.next().next().next().next().next());
		addImgTxtStyle("img-txt-seven",targetPlace.next().next().next().next().next().next());
		addImgTxtStyle("img-txt-eight",targetPlace.next().next().next().next().next().next().next());
	}
	function addVideoStyle(stylename,targetPlace){
		let html='<div class="style_group">'+
					'<div class="video_container '+stylename+'" name="'+stylename+'">'+
						'<div class="container_cell video_cell">'+
							'<img src="img/organization_website/exampleimg_2.png" class="img-basic" name="img-basic"/>'+
							'<video controls style="display:none"/>'+
						'</div>'+
						'<div class="container_cell txt_cell">'+
							'<p>文本内容</p>'+
						'</div>'+
					'</div>'+
				 '</div>';
		targetPlace.append(html);
	}
	function loadVideoStyle(targetPlace){
		let html="<div class='video_style'>"+
					"<p><b>视频样式</b></p>"+
					"<div class='parent'>"+
						"<div class='child'></div>"+
						"<div class='child'></div>"+
						"<div class='child'></div>"+
						"<div class='child'></div>"+
						"<div class='child'></div>"+
					"</div>"+
				  "</div>";
		targetPlace.append(html);
		targetPlace=$(".video_style .parent .child:nth-child(1)");
		addVideoStyle("video-one",targetPlace);
		addVideoStyle("video-two",targetPlace.next());
		addVideoStyle("video-three",targetPlace.next().next());
		addVideoStyle("video-four",targetPlace.next().next().next());
		addVideoStyle("video-five",targetPlace.next().next().next().next());
	}
	function loadImgChange(targetPlace,oldSrc){
		let html="<div class= 'parent'>"+
				"<img class='img-thumbnail' title='点击更换图片'>"+
			 "</div>";
		targetPlace.append(html);
		targetPlace.find("img").attr("src",oldSrc);
		targetPlace.find("img").on("click",function(){
			let oldImg=$(this);
			//打开图片管理器，更换图片
			$("body").loadImageManager("edit_content_change_img",1);
			$("body").on("click","#edit_content_change_img",function(){
					let src=$(this).attr("path");
					oldImg.attr("src",src);
					$(".edit_content_change_img").remove();
			})
		})
	}
	
	function loadVideoChange(targetPlace,oldImgSrc,oldVideoSrc){
		let html="<div class= 'parent'>"+
				"<img class='img-thumbnail'>"+
				"<video controls style='display:none;max-height:200px' />"+
				"<button type='button' class='btn btn-default btn-sm btn-block' style='margin-top:10px;'>更换视频</button>"+
			 "</div>";
		targetPlace.append(html);
		if (typeof(oldImgSrc)!="undefined") {
			targetPlace.find("img").attr("src",oldImgSrc);
		}else{
			targetPlace.find("img").hide().next("video").show().attr("src",oldVideoSrc);
		}
		targetPlace.find("button").on("click",function(){
			// 打开视频管理器，更换视频
			$("body").loadImageManager("edit_content_change_video",2);
			$("body").on("click","#edit_content_change_video",function(){
					let src=$(this).attr("path");
					targetPlace.find("img").hide().next("video").show().attr("src",src);
					$(".edit_content_change_video").remove();
			})
		})
	}
	