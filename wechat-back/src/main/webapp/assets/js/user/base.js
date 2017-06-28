var DOMAIN_REG = '[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+(.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})$'; //匹配域名的正则表达式
var URL_REG = '[a-zA-z]+://[^\s]*'; //匹配url的正则表达式

//页面跳转.
function skip(url) {
	window.location.href = url;
}

/**
 * 判断字符串是否为空
 * 
 * @param str
 *            传入的字符串
 * @returns
 */
function isEmpty(str) {
	if (str != null && str.length > 0) {
		return false;
	}
	return true;
}

/**
 * 正则表达式验证字符串
 * @param  {[type]} str [description]
 * @param  {[type]} reg [description]
 * @return {[type]}     [description]
 */
function validate(str, reg) {
	var patt1 = new RegExp(reg);
	if(patt1.test(str))
		return true;
	return false;
}

/**
 * 显示提示框
 */
function showMsg(msg) {
	$.gritter.add({
		position: 'center',
		title: '提示',
		text: msg,
		time: 2500,
		class_name: 'gritter-info gritter-center'
	});
}
