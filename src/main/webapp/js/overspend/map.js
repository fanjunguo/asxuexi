/**
 * 
 */
(function($){
	var defaults={
	        lng:'117.504523',
	        lat:'38.827205',
	    };
	var panel_html='<div id="courseAddrMap" style="display: none;z-index: 10000;position: absolute;width: 100vw;height: 100%;top: 0px;left: 0px;">\
		<div class="cover"></div>\
	<div class="contaierPanel" >\
	<div style="background: #e1e1e1;height:22px;"><span class="panelColse colsePanel" id="mapColse"></span></div>\
	<div id="container"></div>\
	</div></div>';
	var createPanel=function(obj){
		$(obj).append(panel_html);
	}
    //可以从外部调用的方法
    $.fn.mapWindow=function(options){
    	var options=$.extend(defaults,options);
    	var $body=$('body');
    	createPanel(this);
    	creatMap(options.lat,options.lng);
    	creatMap(options.lat,options.lng)
    	$body.off("click","#mapColse").on("click","#mapColse",function(){
    		$("#courseAddrMap").hide();
    	})
    	function creatMap(lat,lng){
    		var map = new BMap.Map("container");
    		$("#courseAddrMap").show();
    		var point = new BMap.Point(lng,lat);  // 创建点坐标  
    		map.centerAndZoom(point, 16);  
    		map.enableScrollWheelZoom(true);
    		 map.addControl(new BMap.NavigationControl());
    		 var marker = new BMap.Marker(point);        // 创建标注    
    		 map.addOverlay(marker);
    		 map.centerAndZoom(point, 15);
//    	      地图的平移缩放控件，对地图进行上下左右四个方向的平移和缩放操作
    	   map.addControl(new BMap.ScaleControl({anchor:BMAP_ANCHOR_BOTTOM_LEFT}));
    	}
    }
})(jQuery)
