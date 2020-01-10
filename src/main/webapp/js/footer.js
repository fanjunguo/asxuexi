document.write(`
<footer id="footer">
	<div class="footer_link">
		<div class="link_center">
			<a target="_blank" href="pagers/company.jsp#description" class="link_a">关于我们</a>
			<a target="_blank" href="pagers/company.jsp#contact" class="link_a">联系方式</a>
			<a target="_blank" href="pagers/company.jsp#business" class="link_a">商务合作</a>
			<a target="_blank" href="pagers/protocol.jsp" class="link_a">用户协议</a>
			<a target="_blank" href="pagers/privacy.jsp" class="link_a">隐私政策</a>
		</div>
	</div>
	<div class="copyright">
		<span class="copyright_text">Copyright©2018&nbsp;爱上学习网asxuexi.cn&nbsp;版权所有|鲁ICP备18014316号-1</span>
	</div>
</footer>`); 
$(document).ready(function(){
	let html = `
	<div class="side_toolbar">
		<a class="feature_section as_service">
			<img src="img/icon/qrcode.png">
			<div class="feature_description">手机<br>扫码</div>
			<div class="feature_content_wrap">
				<div class="feature_content">
					<div class="qrcode_box">
						<img class="qrcode" src="img/personal/icon/download.png">
						<p class="qrcode_description">爱上学习网APP手机版</p>
					</div>
					<div class="qrcode_box">
						<img class="qrcode" src="img/personal/icon/asxuexi.png">
						<p class="qrcode_description">爱上学习网公众号</p>
					</div>
				</div>
			</div>
			
		</a>
		<a class="feature_section as_service" target="_blank" 
			href="http://wpa.qq.com/msgrd?v=3&uin=1059327557&site=qq&menu=yes">
			<img src="img/icon/QQ.png">
			<div class="feature_description">QQ<br>客服</div>
		</a>
		<a class="feature_section scroll_top" onclick="window.scrollTo(0,0)">
			<img src="img/icon/scrolltop.png">
			<div class="feature_description">返回<br>顶部</div>
		</a>
	</div>`;
	$("body").append(html);
})