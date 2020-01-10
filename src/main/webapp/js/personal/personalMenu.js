$(document).ready(function(){
	$leftContent=$(".left_content");
	$.post("asxuexi/getPersonalMenu.do", function(data){
		let menuContent=
			'<div class="menu_content">\
				<div class="menu_list">';
		for (let i = 0; i < data.length; i++) {
			let menuItem = 
				'<div class="menu_item">\
					<a href='+data[i].url+'>'+data[i].name+'</a>';
			for (let j = 0; j < data[i].children.length; j++) {
				menuItem +=
					'<div class="menu_child_item">\
						<a href='+data[i].children[j].url+'>'+data[i].children[j].name+'</a>\
					</div>';
			}
			menuItem += '</div>';
			menuContent += menuItem;
		}
		menuContent += '</div></div>';
		let qrcodeContent = `
			<div class="qrcode_content">
				<div class="qrcode_box">
					<img class="qrcode" src="img/personal/icon/download.png">
					<p class="qrcode_description">爱上学习网APP手机版</p>
				</div>
				<hr class="qrcode_divide">
				<div class="qrcode_box">
					<img class="qrcode" src="img/personal/icon/asxuexi.png">
					<p class="qrcode_description">爱上学习网公众号</p>
				</div>
			</div>`;
		let informationContent = `
			<div class="information_content">
				<div class="personal_photo">
					<img src="img/personal/icon/default.jpg">
				</div>
			</div>`;
		$leftContent.html(informationContent+menuContent+qrcodeContent);
		if ($(".personal_photo img").length!=0) {
			$.post("asxuexi/getUserInfo.action", function(data){
				$(".personal_photo img").attr("src",data.photo);
			});
		}
		let $a = $("a[href='personal/messageCenter.do']");
		if ($a.length!=0) {
			if ($a.next(".unread_message_count").length == 0) {
				$a.after(`<div class="unread_message_count"></div>`);
			}
			let setUnreadMessageCount = function (){
				$.get("message/countUserMessages.action", { status:"0" }, function(map){
					if (map.code == 600) {
						if (map.data > 0 && map.data <= 99) {
							$(".unread_message_count").text(map.data).show();
						}
						if (map.data > 99) {
							$(".unread_message_count").text("···").show();
						}
						if (map.data == 0) {
							$(".unread_message_count").hide();
						}
					}
				});
				return setUnreadMessageCount;
			}
			setInterval(setUnreadMessageCount(),1800000);
		}
	});
	
	
})