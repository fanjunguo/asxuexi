/**@author bufanpu
 * @describe 自定义验证方法，验证方法的调用对象为jquery input元素
 */
/**验证是否填写
 * @return false 未填写  true 填写
 */
$.fn.requireValidate=function(){
	let $Obj=$(this);
	let length=$Obj.val().trim().length;
	if (length==0) {
		return false;
	}else{
		return true;
	}
}
/**验证长度是否符合条件
 * @param 要验证的长度区间（闭区间）
 * @return false 不符合区间 true 符合区间
 */
$.fn.lengthValidate=function(lengthArray){
	let $Obj=$(this);
	let length=$Obj.val().trim().length;
	if (length>=lengthArray[0]&&length<=lengthArray[1]) {
		return true;
	}else{
		return false;
	}
}
/**验证是否纯数字
 * @return false 不是纯数字 true 纯数字
 */
$.fn.numberValidate=function(){
	let $Obj=$(this);
	let val=$Obj.val().trim();
	let reg=/^\d+(\.{1}\d+)?$/;
	return reg.test(val);
}
/**验证是否为中国大陆手机号
 * @return false 手机号格式错误 true 格式正确
 */
$.fn.mobileValidate=function(){
	let $Obj=$(this);
	let val=$Obj.val().trim();
	let reg=/^(1[3456789]\d{9})$/;
	return reg.test(val);
}
/**验证是否为邮箱
 * @return false 邮箱格式错误 true 格式正确
 */
$.fn.emailValidate=function(){
	let $Obj=$(this);
	let val=$Obj.val().trim();
	let reg=/^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/;
	return reg.test(val);
}
/**验证密码是否符合规定
 * @return false 密码格式错误 true 格式正确
 */
$.fn.passwordValidate=function(){
	let $Obj=$(this);
	let val=$Obj.val().trim();
	//弱规则：由大小写字母，数字，和特殊字符组成，6-20位
	let reg=/^.{6,20}$/;
	//强规则：必须包含大小写字母，数字，和特殊字符的每一种，6-20位
//	let reg=/(?=^.{6,20}$)(?=.*\d)(?=.*\W+)(?=.*[A-Z])(?=.*[a-z])(?!.*\n).*$/;
	return reg.test(val);
}
/**验证是否相等
 * @param 要比较的元素
 * @return false 不相等 true 相等
 */
$.fn.equalToValidate=function(obj){
	let $Obj=$(this);
	let val=$Obj.val().trim();
	let objVal=obj.val();
	if (val==objVal) {
		return true;
	}else{
		return false;
	}
}