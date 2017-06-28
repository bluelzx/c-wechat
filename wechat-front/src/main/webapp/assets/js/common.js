var DOMAIN_REG = '[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+(.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})$'; //匹配域名的正则表达式
var URL_REG = '[a-zA-z]+://[^\s]*'; //匹配url的正则表达式
var MOBILE_REG = '^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$';
var URL_RANDOM_PARAM_REG = /random=[0-9]+([.][0-9]+){0,1}$/g;

//页面跳转.
function skip(url) {
	window.location.href = url;
}
//页面跳转带上随机数
function skipRandom(url) {
	url = url.replace(URL_RANDOM_PARAM_REG, 'random=' + Math.random());
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
 * 验证手机号码正确性
 */
var validateMobile = function(mobile) {
	return validate(mobile, MOBILE_REG);
}

/**
 * 显示提示框
 */
/**
 * 提示框
 * @param  {[type]} content [description]
 * @param  {[type]} second  [description]
 * @return {[type]}         [description]
 */
var tipsModal = function(content, second) {
	$("body").append("<div class='tip-sign'><span>" + content + "</span></div>");
	$('.tip-sign').show();
	setTimeout(function(){$('.tip-sign').remove();}, second*1000);
}

/**
 * loading框
 */
var lvLoading = function(content){
	var $loading = $("<div class='loadingModal'>"+
				    	"<span class='loading'>"+
				    		"<i></i>"+
				    	"</span>" + content + 
			    	"</div>");
	$loading.appendTo("body");
}
/**
 * 移除loading
 */
var rmLvLoading = function(){
	$(".loadingModal").remove();
}

/**
 * 加载分享模板
 */
var loadShareMode = function(shareTemplateId,channel){
	var wxData;
	$.ajax({
		url : '/api/shareTemplate/'+shareTemplateId+"/"+channel,
		type : "POST",
		success:function(data) {
			if(data.code == 1) {
				$("#img_share").attr("src", data.data.shareTemplate.wxShareContent.imageList[0].url); 
				wxData = {
					  imgUrl: data.data.shareTemplate.wxShareContent.imageList[0].url,
 					  link: data.data.shareTemplate.wxShareContent.url,
					  desc: data.data.shareTemplate.wxShareContent.shareDesc,
					  title: data.data.shareTemplate.wxShareContent.title
				}
			}
			if(wxData != undefined) {
			    WechatApi.share(wxData);
			}
		}		
	});
}