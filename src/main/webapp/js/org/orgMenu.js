$(document).ready(function(){
	$leftContent=$(".left_content");
	$.post("OrgCenter/getOrgMenu.action", function(data){
		let orgMenu=data.orgMenu;
		let menuContent=
			'<div class="menu_content">\
				<div class="menu_list">';
		for (let i = 0; i < orgMenu.length; i++) {
			let menuItem = 
				'<div class="menu_item">\
					<a href='+orgMenu[i].url+'>'+orgMenu[i].name+'</a>';
			for (let j = 0; j < orgMenu[i].children.length; j++) {
				menuItem +=
					'<div class="menu_child_item">\
						<a href='+orgMenu[i].children[j].url+'>'+orgMenu[i].children[j].name+'</a>\
					</div>';
			}
			menuItem += '</div>';
			menuContent += menuItem;
		}
		
		let qrcodeContent=
			'<div class="qrcode_content">\
				<div class="qrcode_box">\
					<img class="qrcode" src="img/personal/icon/download.png">\
					<p class="qrcode_description">爱上学习网APP手机版</p>\
				</div>\
				<hr class="qrcode_divide">\
				<div class="qrcode_box">\
					<img class="qrcode" src="img/personal/icon/asxuexi.png">\
					<p class="qrcode_description">爱上学习网公众号</p>\
				</div>\
			</div>';
		menuContent += '</div></div>';
		$leftContent.html(menuContent+qrcodeContent);
		let $a = $("a[href='org/orgMessage.do']");
		if ($a.length!=0) {
			if ($a.next(".unread_message_count").length == 0) {
				$a.after(`<div class="unread_message_count"></div>`);
			}
			let setUnreadMessageCount= function(){
				$.get("message/countOrgMessages.action", { status:"0" }, function(map){
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
		$(".menu_child_item a[href='javascript:void(0)']").on("click",function(){
			let html = `
			<div class="modal fade" id="course_release_modal" tabindex="-1" role="dialog"
				aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-body">
							<button type="button" class="close" data-dismiss="modal"
								aria-hidden="true">×</button>
							<h4 class="modal-title">选择课程类型</h4>
							<div class="course_type_buttons">
								<a class="offline_course" target="_blank" href="courseRelease.do?courseType=1">
									<button type="button" class="btn btn-primary">线下课程</button>
								</a>
							</div>
						</div>
					</div>
				</div>
			</div>`;
			if ($("#course_release_modal").length == 0) {
				$("body").append(html);
			}
			$.get('log_in/isLogin.action', function() {
				$("#course_release_modal").modal("show");
			});
			$(".offline_course").off("click").on("click",function(){
				$("#course_release_modal").modal("hide");
			})
			
		})
	})
	
})