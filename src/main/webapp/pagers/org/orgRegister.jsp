<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html >
<html>
<head>
<base href="<%=basePath%>" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机构入驻</title>
<link rel="stylesheet"
	href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="css/org/enroll.css">
<script src="js/jquery-3.3.1.min.js"></script>
<script
	src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="js/validate/jquery.validate.js"></script>
<script src="js/validate/jquery.validate.pack.js"></script>
<script src="js/validate/additional-methods.js"></script>
<script src="js/validate/localization/messages_cn.js"></script>
<script src="js/login/md5.js"></script>
<script src="js/authorityFilter.js"></script>
<script src="js/org/orgRegister.js"></script>
<link rel="stylesheet" href="css/footer.css">
</head>
<body>
	<div id="header_parent">
		<div id="header">
			<div id="logo">
				<a href="pagers/homepage.jsp"><img src="img/logo.png"></a>
			</div>
			<div id="slogen">陪伴一生的学习网</div>
		</div>
	</div>
	<div class="background_img">
		<div class="register_box">
			<div class="register_top">
				<span class="register_title">机构入驻</span>
			</div>
			<div class="register_center">
				<form id="validate">
					<div class="input_wrap">
						<label for="orgName" class="input_label">机构名称：</label> <input
							type="text" id="orgName" name="orgName" autocomplete="off"
							placeholder="请输入机构名称">
					</div>
					<div class="input_wrap">
						<label for="orgHead" class="input_label">负责人：</label> <input
							type="text" id="orgHead" name="orgHead" autocomplete="off"
							placeholder="请输入机构负责人">
					</div>
					<div class="input_wrap">
						<label for="orgTel" class="input_label">联系电话：</label> <input
							type="text" id="orgTel" name="orgTel" autocomplete="off"
							placeholder="请输入机构电话">
					</div>
					<div class="input_wrap">
						<label for="areas_select" class="input_label">所在地区：</label>
						<div id="orgAreas">
							<select id="provinceAddress"><option>省份</option></select> <select
								id="cityAddress"><option>地级市</option></select> <select
								id="countyAddress" name="countyAddress"><option
									value=''>县区</option></select>
						</div>
					</div>
					<div class="input_wrap">
						<div id="sava">立即提交</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<div class="modal fade" id="protocolModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h5 class="modal-title center-block" id="protocolModalLabel">爱上学习网平台机构入驻协议</h5>
				</div>
				<div class="modal-body">
					<p>
						<strong><span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></strong><strong><span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">尊敬的用户，在您访问、浏览或使用爱上学习网网站(http://www.asxuexi.cn)、APP应用程序（以下统称“爱上学习网平台”）及相关服务时，表明您已审慎阅读、充分理解本协议项下所有条款并同意接受本协议的约束，保证遵守所有适用的法律法规。<br />
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;爱上学习网平台可能会根据需要修订或者更新本协议及其相关补充和附件、平台规则、单项服务规则等文件，修订或更新后的协议和文本自公布之日起生效。您应经常访问本页面以了解当前条款，如果您不接受相关修订和更新，请立即停止使用爱上学习网平台提供的服务；如您继续使用爱上学习网平台提供的服务，即表示您已经充分接受该修订或更新。
						</span></strong>
					</p>
					<h4 style="line-height: 20px">
						<strong><span
							style="font-size: 14px; color: #222222; background: white">1.
						</span><span style="font-size: 14px; color: #222222; background: white">服务条款的确认和接纳</span></strong>
					</h4>
					<p>
						<span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">1.1
						</span><span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">本协议是由东营市爱上学习网络科技股份有限公司（下称“爱上学习网公司”）与您签订的用户服务协议，约定由爱上学习网提供基于爱上学习网平台相关服务以及双方应遵守的协议条款。如爱上学习网平台的下属网站有所增加，则增加的网站作为爱上学习网平台提供的网络服务的一部分，仍将适用本协议的规定。爱上学习网平台的各项内容及服务由爱上学习网公司、关联方及合作方提供。<br />
							1.2
							您确认，在您入驻爱上学习网平台前，您应当具备中华人民共和国法律规定的与您行为相适应的民事行为能力，并具有进行相关交易的资格。若您不具备前述与您行为相适应的民事行为能力，则您及您的监护人应依照法律规定承担因此而导致的一切后果。此外，您还需确保您不是任何国家、国际组织或者地域实施的限制、制裁或其他法律、规则限制的对象，否则您可能无法正常注册和登录爱上学习网平台，无法正常使用爱上学习网平台提供的服务。
						</span>
					</p>
					<h4 style="line-height: 20px">
						<strong><span
							style="font-size: 14px; color: #222222; background: white">2.
						</span><span style="font-size: 14px; color: #222222; background: white">账户注册及使用</span></strong>
					</h4>
					<h5 style="line-height: 20px">
						<strong><span
							style="font-size: 14px; color: #222222; background: white">2.1</span><span
							style="font-size: 14px; color: #222222; background: white">【账户获得】</span></strong>
					</h5>
					<p>
						<span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">2.1.1</span><span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">当您按照入驻注册页面提示填写信息、阅读并同意本协议且完成入驻程序后，您可获得爱上学习网平台机构账户并成为爱上学习网平台机构用户。<br />
							2.1.2您一经注册爱上学习网平台帐号，除非子频道要求单独开通权限，您有权利用该帐号使用爱上学习网平台各个频道的各项服务，当您使用爱上学习网平台各项服务时，您的使用行为视为其对该机构入驻条款以及爱上学习网平台在各项服务中发出的各类公告的同意。
						</span>
					</p>
					<h5 style="line-height: 20px">
						<strong><span
							style="font-size: 14px; color: #222222; background: white">2.2</span><span
							style="font-size: 14px; color: #222222; background: white">【账户使用】</span></strong>
					</h5>
					<p>
						<span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">2.2.1</span><span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">您有权设置、更改账号密码，并通过用户名和密码登录。<br />
							2.2.2由于您的爱上学习网平台账户关联您的个人信息及爱上学习网平台商业信息，您的爱上学习网平台账户仅限您本人使用。未经爱上学习网平台同意，您直接或间接授权第三方使用您爱上学习网平台账户或获取您账户项下信息的行为无效。
						</span>
					</p>
					<h5 style="line-height: 20px">
						<strong><span
							style="font-size: 14px; color: #222222; background: white">2.3</span><span
							style="font-size: 14px; color: #222222; background: white">【账户安全】</span></strong>
					</h5>
					<p>
						<span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">2.3.1</span><span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">您的账户为您自行设置并由您保管，爱上学习网平台任何时候不会主动要求您提供您的账户密码。因此，建议您务必保管好您的账户。<br />
							2.3.2账户因您主动泄露或因您遭受他人攻击、诈骗等行为导致的损失及后果，爱上学习网平台并不承担责任，您应通过司法、行政等救济途径向侵权行为人追偿。除爱上学习网平台存在过错外，您应对您账户项下的所有行为结果包括但不限于签署各类协议、发布信息、提出交易需求及披露信息等）负责。<br />
							2.3.3如发现任何未经授权使用您账户登录爱上学习网平台或其他可能导致您账户遭窃、遗失的情况，建议您立即通知爱上学习网平台。您理解爱上学习网平台对您的任何请求采取行动均需要合理时间，且爱上学习网平台根据您请求而采取的行动可能无法避免或阻止侵害后果的形成或扩大。
						</span>
					</p>
					<h4 style="line-height: 20px">
						<strong><span
							style="font-size: 14px; color: #222222; background: white">3.</span><span
							style="font-size: 14px; color: #222222; background: white">信息管理</span></strong>
					</h4>
					<p>
						<span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">3.1
						</span><span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">【信息真实】在使用爱上学习网平台服务时，您应当按爱上学习网平台的要求和提示准确完整地提供您的信息和交易相关信息（包括但不限于您的姓名、联系方式、其他信息等），以便爱上学习网平台与您联系并提供相应服务。您了解并同意，您有义务保证您提供的所有信息的真实性及有效性。</span>
					</p>
					<h5 style="line-height: 20px">
						<span
							style="font-size: 14px; color: #222222; background: white">3.2
						</span><span style="font-size: 14px; color: #222222; background: white">【信息授权】</span>
					</h5>
					<p>
						<span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">3.2.1</span><span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">为保证交易安全并向您提供更优质的服务，您了解并同意不可撤销地授权爱上学习网平台对于您的信息进行第三方信息认证，爱上学习网平台、查询服务提供机构及其指定的第三方可获取、查询、留存、整理及加工您的认证信息，但该认证信息仅用于通过爱上学习网平台进行的交易及相关的服务。<br />
							3.2.2对于您提供、发布及在使用爱上学习网平台服务中形成的除个人信息外的文字、图片、视频、音频等非个人信息，在法律规定的保护期限内您免费授予爱上学习网平台、爱上学习网公司及其关联公司获得全球范围内排他的许可使用权利及再授权给其他第三方使用并可以自身名义对第三方侵权行为取证及提起诉讼的权利。您同意爱上学习网平台及其关联公司存储、使用、复制、修订、编辑、发布、展示、翻译、分发您的非个人信息或制作其派生作品，并以已知或日后开发的形式、媒体或技术将上述信息纳入其它作品内。<br />
							3.2.3为方便您使用爱上学习网平台等其他相关服务，您授权爱上学习网平台将您在账户注册和使用爱上学习网平台服务过程中提供、形成的信息传递给爱上学习网平台等其他相关服务提供者，或从爱上学习网平台等其他相关服务提供者获取您在注册、使用相关服务期间提供、形成的信息。
						</span>
					</p>
					<p>
						<span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">3.3
						</span><span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">【违规信息处理】您提交的任何信息等资料中不得出现违法和不良信息，经爱上学习网平台确认，如存在上述情况，爱上学习网平台有权不经通知单方采取限期改正、暂停使用、注销登记、中止/终止提供服务、收回账号、对于包含违规信息的内容屏蔽或断开链接等措施。爱上学习网平台对您提交的所有信息予以审核通过并不代表爱上学习网平台对其予以任何批准、许可、授权、同意、支持或承诺，您应当自行承担其法律责任。</span>
					</p>
					<p>
						<span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">3.4
						</span><span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">【信息纠正】在您浏览、使用爱上学习网平台时，如您发现爱上学习网平台上公示的信息与您提供的信息或其他与您利益相关的信息不一致时，您须及时联系爱上学习网公司予以纠正，爱上学习网平台对于信息展示的失误不免除您提供真实信息的义务和责任。</span>
					</p>
					<h4 style="line-height: 20px">
						<strong><span
							style="font-size: 14px; color: #222222; background: white">4.
						</span><span style="font-size: 14px; color: #222222; background: white">服务内容</span></strong>
					</h4>
					<p>
						<span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">4.1
						</span><span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">爱上学习网平台根据公示的协议或规则、您参与的服务项目及您与爱上学习网公司（或关联方、合作方）订立的任何形式的协议或条款向您提供服务。<br />
							4.2 除非本服务协议另有其它明示规定，爱上学习网平台所推出的新产品、新功能、新服务，均受到本协议之规范。<br />
							4.3鉴于网络服务的特殊性，您同意爱上学习网平台有权不经事先通知，随时变更、中断或终止部分或全部的网络服务。爱上学习网平台对网络服务的及时性、安全性、准确性、不中断性不作任何担保。<br />
							4.3鉴于网络服务的特殊性，您同意爱上学习网平台有权不经事先通知，随时变更、中断或终止部分或全部的网络服务。爱上学习网平台对网络服务的及时性、安全性、准确性、不中断性不作任何担保。
						</span>
					</p>
					<h4 style="line-height: 20px">
						<strong><span
							style="font-size: 14px; color: #222222; background: white">5.
						</span><span style="font-size: 14px; color: #222222; background: white">用户信息获取及保护</span></strong>
					</h4>
					<p>
						<span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">5.1</span><span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">爱上学习网平台非常重视您的个人信息保护，在您使用爱上学习网平台提供的服务时，您同意爱上学习网平台按照公示的《法律声明和用户隐私权政策》收集、存储、使用、披露和保护您的个人信息。<br />
							5.2非个人信息的保证与授权<br />
							5.2.1【信息的发布】您声明并保证，您对您在网站所发布的信息拥有相应、合法的权利。否则，爱上学习网平台可对您发布的信息依法或依本协议进行删除或屏蔽。<br />
							5.2.2【禁止性信息】您应当确保您所发布的信息不包含以下内容：<br /> 1）违反国家法律法规禁止性规定的；<br />
							2）政治宣传、封建迷信、淫秽、色情、赌博、暴力、恐怖或者教唆犯罪的；<br /> 3）欺诈、虚假、不准确或存在误导性的；<br />
							4）侵犯他人知识产权或涉及第三方商业秘密及其他专有权利的；<br /> 5）侮辱、诽谤、恐吓、涉及他人隐私等侵害他人合法权益的；<br />
							6）存在可能破坏、篡改、删除、影响爱上学习网平台任何系统正常运行或未经授权秘密获取爱上学习网平台及其他用户的数据、个人资料的病毒、木马、爬虫等恶意软件、程序代码的；<br />
							7）其他违背社会公共利益或公共道德或依据相关爱上学习网平台协议、规则的规定不适合在爱上学习网平台上发布的。
						</span>
					</p>
					<h4 style="line-height: 20px">
						<strong><span
							style="font-size: 14px; color: #222222; background: white">6.
						</span><span style="font-size: 14px; color: #222222; background: white">责任限制</span></strong>
					</h4>
					<p>
						<span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">1.</span><span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">因以下情况造成网络服务在合理时间内的中断，爱上学习网平台无需为此承担任何责任：<br />
							1)
							爱上学习网平台需要定期或不定期地对提供网络服务的平台或相关的设备进行检修或者维护，爱上学习网平台保留不经事先通知为维修保养、升级或其它目的暂停本服务任何部分的权利。&nbsp;<br />
							2) 因台风、地震、洪水、雷电或恐怖袭击等不可抗力原因；&nbsp;<br /> 3)
							用户的电脑软硬件和通信线路、供电线路出现故障的；&nbsp;<br /> 4)
							因病毒、木马、恶意程序攻击、网络拥堵、系统不稳定、系统或设备故障、通讯故障、电力故障、银行原因、第三方服务瑕疵或政府行为等原因。
						</span>
					</p>
					<p>
						<span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">2.</span><span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">您理解并接受对于爱上学习网公司向您提供的下列产品或服务的质量缺陷本身及其引发的任何损失（如有），爱上学习网公司无需承担任何责任:&nbsp;<br />
							1) 爱上学习网平台向您免费提供的各项产品或服务；&nbsp;<br /> 2) 爱上学习网平台向您赠送的任何产品或服务。
						</span>
					</p>
					<p>
						<span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">3.</span><span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">爱上学习网平台所载的信息，包括但不限于文本、图片、数据、观点、网页或链接，虽然竭力准确和详尽，但爱上学习网公司并不就其所包含的信息和内容的准确、完整、充分和可靠性做任何承诺，也不对这些信息和内容的错误或遗漏承担责任，也不对这些信息和内容作出任何明示或默示的包括但不限于没有侵犯第三方合法权益的保证。</span>
					</p>
					<h4 style="line-height: 20px">
						<strong><span
							style="font-size: 14px; color: #222222; background: white">7.
						</span><span style="font-size: 14px; color: #222222; background: white">通知</span></strong>
					</h4>
					<h5 style="line-height: 20px">
						<strong><span
							style="font-size: 14px; color: #222222; background: white">7.1</span><span
							style="font-size: 14px; color: #222222; background: white">您的有效联系方式包括：</span></strong>
					</h5>
					<p>
						<span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">1</span><span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">）您在注册成为爱上学习网平台用户，并接受爱上学习网平台服务时，您应该向爱上学习网平台提供真实有效的联系方式（包括您的电子邮件地址、联系电话、联系地址等），对于联系方式发生变更的，您有义务及时更新有关信息，并保持可被联系的状态。<br />
							2）您在注册爱上学习网平台用户时生成的用于登陆爱上学习网平台接收系统通知或其他即时信息的用户账号（包括子账号），也作为您的有效联系方式。<br />
							3）爱上学习网平台将向您的上述联系方式的其中之一或其中若干向您送达各类通知，而此类通知的内容可能对您的权利义务产生重大的有利或不利影响，请您务必及时关注。
						</span>
					</p>
					<h5 style="line-height: 20px">
						<strong><span
							style="font-size: 14px; color: #222222; background: white">7.2
						</span><span style="font-size: 14px; color: #222222; background: white">通知的送达</span></strong>
					</h5>
					<p>
						<span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">1</span><span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">）爱上学习网平台通过上述联系方式向您发出通知，其中以电子的方式发出的书面通知，包括但不限于在爱上学习网平台公告，向您提供的联系电话发送手机短信，向您提供的电子邮件地址发送电子邮件，向您的账号发送系统消息，在发送成功后即视为送达；以纸质载体发出的书面通知，按照提供联系地址交邮后的第五个自然日即视为送达。<br />
							2）对于在爱上学习网平台上因交易活动引起的任何纠纷，您同意司法机关、行政机关可以通过手机短信、电子邮件等现代通讯方式或邮寄方式向您送达法律相关文书。您指定接收法律文书的手机号码、电子邮箱或爱上学习网平台账号等联系方式为您在爱上学习网平台注册、更新时提供的手机号码、电子邮箱联系方式等，司法机关或行政机关向上述联系方式发出法律文书即视为送达。您指定的邮寄地址为您的法定联系地址或您提供的有效联系地址。<br />
							3）您同意司法机关或行政机关可采取以上一种或多种送达方式向您达法律文书，司法机关或行政机关采取多种方式向您送达法律文书，送达时间以上述送达方式中最先送达的为准。<br />
							4）您同意上述送达方式适用于各个行政司法程序阶段。<br />
							5）你应当保证所提供的联系方式是准确、有效的，并进行实时更新。如果因提供的联系方式不确切，或不及时告知变更后的联系方式，使法律文书无法送达或未及时送达，由您自行承担由此可能产生的法律后果。
						</span>
					</p>
					<h4 style="line-height: 20px">
						<strong><span
							style="font-size: 14px; color: #222222; background: white">8.
						</span><span style="font-size: 14px; color: #222222; background: white">协议的终止</span></strong>
					</h4>
					<p>
						<span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">8.1</span><span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">【用户发起的终止】您有权通过以下任一方式终止本协议：<br />
							1）在满足爱上学习网平台公示的账户注销条件时您通过网站自助服务注销您的账户的；<br />
							2）变更事项生效前您停止使用并明示不愿接受变更事项的；<br />
							3）您明示不愿继续使用爱上学习网平台服务，且符合爱上学习网平台终止条件的。
						</span>
					</p>
					<p>
						<span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">8.2</span><span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">【爱上学习网平台发起的终止】出现以下情况时，爱上学习网平台可以本协议第6条的所列的方式通知您终止本协议：<br />
							1）您违反本协议约定，爱上学习网平台依据违约条款终止本协议的；<br />
							2）您盗用他人账户、发布违禁信息、骗取他人财物、售假、扰乱市场秩序、采取不正当手段谋利等行为，爱上学习网平台依据爱上学习网平台规则对您的账户予以查封的；<br />
							3）您的账户被爱上学习网平台依据本协议回收的；<br />
							4）您在爱上学习网平台有欺诈、侵犯他人合法权益或其他严重违法违约行为的；<br /> 5）其它应当终止服务的情况。
						</span>
					</p>
					<p>
						<span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">8.3
						</span><span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">协议终止后的处理<br />
							8.3.1【用户信息披露】本协议终止后，除法律有明确规定外，爱上学习网平台无义务向您或您指定的第三方披露您账户中的任何信息。<br />
							8.3.2 【权利保留】本协议终止后，爱上学习网平台仍享有下列权利：<br />
							1）继续保存您留存于爱上学习网平台的本协议第5条所列的各类信息；<br />
							2）对于您过往的违约行为，爱上学习网平台仍可依据本协议向您追究违约责任。
						</span>
					</p>
					<h4 style="line-height: 20px">
						<strong><span
							style="font-size: 14px; color: #222222; background: white">9.
						</span><span style="font-size: 14px; color: #222222; background: white">法律适用、管辖与其他</span></strong>
					</h4>
					<p>
						<span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">9.1</span><span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">【法律适用】本协议之订立、生效、解释、修订、补充、终止、执行与争议解决均适用中华人民共和国大陆地区法律；如法律无相关规定的，参照商业惯例及/或行业惯例。</span>
					</p>
					<p>
						<span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">9.2</span><span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">【管辖】您因使用爱上学习网平台服务所产生及与爱上学习网平台服务有关的争议，由爱上学习网平台与您协商解决。协商不成时，任何一方均可向爱上学习网公司所在地有管辖权的人民法院提起诉讼。</span>
					</p>
					<p>
						<span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">9.3</span><span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">【可分性】本协议任一条款被视为废止、无效或不可执行，该条应视为可分的且并不影响本协议其余条款的有效性及可执行性。</span>
					</p>
					<p>
						<span
							style="font-size: 14px; font-family: &amp; #39; 微软雅黑 &amp;#39; , sans-serif; color: #222222; background: white">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							协议生效时间：&nbsp;&nbsp;2019年1月1日</span> <br />
					</p>
				</div>
				<div class="modal-footer">
					<div class="button_wrap">
						<div id="agree" class="btn btn-primary ">同意并入驻</div>
						<div id="disAgree" class="btn btn-default">取消</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src="js/footer.js"></script>
</body>

</html>