var LT_ACTIVITY_12580 = "mobile_activity_12580"; // 12580活动
var LT_ACTIVITY_10000 = "mobile_activity_10000"; // 10000活动
var loginUrl=decodeURIComponent(getCookie('loginUrl'));//存入请求的window.location.href,页面跳转时用到/注册也用


function lvToast(isSuccess,toastText,time){
	var that=this;if(time){
		var time=time
		}else{
			time=3e3
			}if($("#lv-temp-toast").length!=0){
				return false
				}else{
					$("body").append("<div id='lv-temp-toast' class='lv-toast' style='display:none;'><div class='lv-toast-div'></div><p></p></div>")
					}
	var toastLeft=($(window).width()-200)/2;
	var toastTop=$(window).scrollTop()+200;
	var $toast=$("#lv-temp-toast");
	$toast.css({left:toastLeft,top:toastTop,opacity:"0.7"});
	$toast.children("p").html(toastText);
	if(isSuccess==that.CONTENT_LOGDING){
				$toast.addClass("lv-toast-loading").css({top:toastTop}).empty().append('<img style="width:32px;" src="//pics.lvjs.com.cn/img/mobile/touch/img/3.gif"/>').fadeIn(600);
				setTimeout(function(){$toast.addClass("lv-toast").addClass("lv-toast-loading").
				css({top:toastTop}).fadeOut(1e3,function(){$(this).remove()})},time)
		}else if(isSuccess){
				$toast.children("div").append('<img src="//pics.lvjs.com.cn/img/mobile/touch/img/smile-grey.png" />');$toast.addClass("lv-toast").addClass("lv-toast-success").fadeIn(600);
				setTimeout(function(){$toast.fadeOut(1e3,function(){$(this).remove()})},time)
		}else{
			$toast.children("div").append('<img src="//pics.lvjs.com.cn/img/mobile/touch/img/sad-big.png" />');		
			$toast.addClass("lv-toast").addClass("lv-toast-fail").fadeIn(600);
		setTimeout(function(){$toast.fadeOut(1e3,function(){$(this).remove()})},time)
		}
	}

//验证码登录 ，发送验证码
function login_send_authcode(){
	var c = $("#mobile").val();
	if(!v_mobile(c)) {
		return false;
	}
	
	var authCode=$("#authCode").val();
	if($.trim(authCode) == ""){
		lvToast(true,"请输入右侧校验码",LT_LOADING_CLOSE);
		return false;
	}
	var needImageAuthCode = "true";
	var params = {mobileOrEMail:c,
			authCode:authCode,
			needImageAuthCode:needImageAuthCode
				};
	$.ajax({
		url : "/binding/sendMobileCode",
		data:params,
		type : "POST",
		success : function(data) {
			login_send_sutocode_callback(data);
		},
		error:function() {
			lvToast(false,"请求失败",LT_LOADING_CLOSE);
		}
	});
}

// 验证码登录回调
function login_send_sutocode_callback(data) {
	if(data.code=='1') {
		lvToast(true,"验证码已发送",LT_LOADING_CLOSE);
		$("#sendCode").show();
		$("#login_autocode_id").hide();
		codeWait(60);
	} else {
		if(data.message != null){
			lvToast(false,data.message,LT_LOADING_CLOSE);
		}else{
			lvToast(false,'服务忙，请稍后再试!',LT_LOADING_CLOSE);
		}
		return;
	}
}

// 发送验证码倒计时60秒
function codeWait(num){//倒计时方法 num是要倒计时的秒数
    var nowtime = new Date();
    var newtime = nowtime;
    var count;
    nowtime = parseInt(nowtime.getTime()/1000);
    var obj = $("#sendCode");
    obj.html(num+"秒后重发");
    var timer = setInterval(function(){
        newtime = new Date();
        count = num-(parseInt(newtime.getTime()/1000)-nowtime);
        obj.html(count+"秒后重发");
        if(count<=0){
            clearInterval(timer);
            $("#login_autocode_id").show();
    		$("#sendCode").hide();
        }
    },1000);
}


//跳转请求URL
function loginParms(loginUrl,userId,activityChannel,flag){
	var parms=loginUrl.split("callUrl=")[1];
	var callBack="&callBack="+encodeURIComponent("//"+hostName+"/promotionAction/promotionAction.htm?activityChannel="+activityChannel+flag);
	var backUrl=parms+"&userId="+userId+callBack;	
	setcookie('loginUrl', null, 72000000, '/', ".lvmama.com",false); //设置cookie 秒
	
	return backUrl;
}
//跳转请求URL微信调用
function loginParmsWeixin(loginUrl,userId){
	var parms=loginUrl.split("callUrl=")[1];
	var backUrl=parms+"&userId="+userId;
	$.cookie('loginUrl', null);
	return backUrl;
}
//读取Cookie
function getCookie(objName) {
	var arrStr = document.cookie.split("; ");
	for (var i = 0; i < arrStr.length; i++) {
		var temp = arrStr[i].split("=");
		if (temp[0] == objName) {
			return temp[1];
		}
	}
	return '';
}
//删除Cookie
function delCookie(name){ 
	var date=new Date(); 
	date.setTime(date.getTime()-10000); 
	document.cookie=name+"=n;expire="+date.toGMTString(); 
}
/**
 * 第三方账号登陆. 
 * @param url
 */
function union_login(url){
	window.open(url);
}

// 注册 - 获取验证码 .
function getAuthCode() {
	var c = $("#mobile").val();
	if(!v_mobile(c)) {
		return false;
	}
	var check = $("#checkbox").attr('checked');
	if(undefined == check || 'undefined' == check || !(check == 'checked')) {
		lvToast(false,"请同意 《驴妈妈旅游网会员服务条款》！",LT_LOADING_CLOSE);
		return;
	}
	var authCode = "";
	if($("#authCode").val() != undefined){
		authCode=$("#authCode").val();
	}
	if(authCode.length != 4){
		lvToast(false,"请输入正确的验证码！",LT_LOADING_CLOSE);
		return;
	}
	if($.trim(authCode).length != 4){
		lvToast(false,"请输入正确的验证码！",LT_LOADING_CLOSE);
		return;
	}
	var url = "/binding/register_code";
	var params={"mobileOrEMail":c,"authCode":authCode};
	
	$.ajax({
		url : url,
		data:params,
		type : "POST",
		success : function(datas) {
			regAuthCode_callback(datas);
		},
		error:function() {
			lvToast(false,"请求失败",LT_LOADING_CLOSE);
		}
	});
} 

/**
 * 校验验证码
 */
function commitAutoCode() {
	// 校验码
	var acode = $("#authenticationCode").val();
	if(!v_authenticationCode(acode)) {
		return false;
	}
	
	var c = $("#mobile").val();
	if(!v_mobile(c)) {
		return false;
	}		
	$.post("/binding/validateMsgauthCode",{ mobileOrEMail : c, authenticationCode : acode},function(data){
		// 校验成功 
		if(data.code != null && data.code=='1') {
			nextStep(2,3);
		} else {
			lvToast(false,data.errorText,3000);	
		}
	});
}

//注册-获取验证码回调
function regAuthCode_callback(data) {
	if(data.success) {
		lvToast(true,"验证码已发送",LT_LOADING_CLOSE);
		nextStep(1,2);
	} else {
		lvToast(false,data.errorText,LT_LOADING_CLOSE);
		return;
	}
}

// 忘记密码首页
function submitFindPW() {
	 $("#findPassworldForm").submit();
}

function commiting_show(tag) {
	if(tag == '1') {
		$("#step3_show").hide();
		$("#commiting_show").show();
	} else if(tag == '2') {
		$("#commiting_show_a_id").html('注册成功...');
	} else {
		$("#commiting_show").hide();
		$("#step3_show").show();
	}
}
// 注册 - 验证校验 
function reg(activityChannel) {
	var mobile = $("#mobile").val();
	// 校验码
	var acode = $("#authenticationCode").val();
	if(!v_authenticationCode(acode)) {
		return false;
	}
	// 密码
	var p = $("#password").val();
	if(!v_passworld(p)) {
		return false;
	}
	
	if(null != activityChannel &&  activityChannel == LT_ACTIVITY_12580) {
		secondChannel = "12580";
	} else if(null != activityChannel &&  activityChannel == LT_ACTIVITY_10000) {
		secondChannel = "10000";
	}
	// 提交注册中 
	commiting_show('1');
	var url = $("#redirect_url").val();
	var param = {"mobileOrEMail":mobile,"authenticationCode":acode,"password":encodeURIComponent(p)};
	$.post("/binding/userRegister",param,function(data) {
		if(data.success) {
			commiting_show('2');
			//统计代码JS
			cmCreateRegistrationTag(data.data.userId);
			cmCreateElementTag(statisticsType+"_注册","登录与注册");

			//注册成功活动处理
			if(loginUrl.indexOf("callUrl")>0 && activityChannel!=null && activityChannel!=''){
				var callBackUrl=loginParms(loginUrl,data.data.userId,activityChannel,"_reg");
				window.location.href=callBackUrl;
			}else if(url!=""){
				if(url.indexOf("//")>=0){
					window.location.href=decodeURIComponent(url);
				}else{
					window.location.href=url;
				}
			}else if(loginUrl.indexOf("activityChannel")!=-1 && loginUrl.indexOf("service=")!=-1){
				window.location.href="//"+hostName+loginUrl.split("service=")[1];
			}else{
				window.location.href="//"+hostName+"/user/";
			}
		} else {
			commiting_show('3');
			lvToast(false,data.errorText,3000);
		}
	});
	
}

// 手机号
function v_mobile(c) {
	if($.trim(c) == ""){
		lvToast(false,"请输入手机号",LT_LOADING_CLOSE);
		$("#mobile").focus();
		return false;
	} else if(!isMobile(c)) {
		lvToast(false,"请输入正确的手机号",LT_LOADING_CLOSE);
		$("#mobile").focus();
		return false;
	}
	
	return true;
}
// 校验码 
function v_authenticationCode(acode) {
	if($.trim(acode) == ""){
		lvToast(false,"请输入验证码",LT_LOADING_CLOSE);
		$("#authenticationCode").focus();
		return false;
	} else if($.trim(acode).length != 6) {
		lvToast(false,"请输入正确的验证码",LT_LOADING_CLOSE);
		$("#authenticationCode").focus();
		return false;
	} 
	
	return true;
}

/**
 * 校验密码
 * @param p
 * @returns {Boolean}
 */
function v_passworld(p) {
	if($.trim(p) == ""){
		lvToast(false,"请输入密码",LT_LOADING_CLOSE);
		$("#password").focus();
		return false;
	} else if(isZh(p)) {
		lvToast(false,"请不要输入中文",LT_LOADING_CLOSE);
		$("#password").focus();
		return false;
	}else if(p.length < 6) {
		lvToast(false,"密码长度不能小于6",LT_LOADING_CLOSE);
		$("#password").focus();
		return false ;
	}else if(p.length > 16) {
		lvToast(false,"密码长度不能大于16",LT_LOADING_CLOSE);
		$("#password").focus();
		return false ;
	}
	
	return true;
}

/**
 * 是否字母数字组成 
 * @param value
 * @returns {Boolean}
 */
function checknum(value) {
    var regx = /^[A-Za-z0-9]*$/;
    if (regx.test(value)) {
        return true;
    } else {
        return false;
    }
}

// 是否中文 ，是；true  ；
function isZh(txt1) {
	var pattern = /[\u4E00-\u9FA5]|[\uFE30-\uFFA0]/i;  //中文简体及繁体unicode码范围  
    if(pattern.test(txt1)){ //正则表达式模式匹配，返回true，则输入的中文字符  
        return true;  
    }
    return false;
}

/**
 * 验证码下一步 
 */
function nextStep(c,next){
	$("#step"+c).hide();
	$("#step"+next).show();
	$("#c_step"+next).removeClass("lv-progress-node").addClass("lv-progress-active");
	$("#c_step"+c).removeClass("lv-progress-active").addClass("lv-progress-node");
}


// 注册删除小图标默认不显示 
var mobile = $("#mobile").val();
$("#mobile, #authenticationCode, #password").keyup(function() {
	var _clearVal = $(this).val();
	if(_clearVal != ''){
		$(this).siblings('.clear_password').show();
	}else {
		$(this).siblings('.clear_password').hide();
	}
});
function step1change() {
	mobile = $("#mobile").val();
	// 如果是合法的手机号
	if(isMobile(mobile)) {
		$("#step1_show").show();
		$("#step1_hide").hide();
	} else {
		$("#step1_show").hide();
		$("#step1_hide").show();
	}
	//点击删除消失
	$('.clear_password').live('click',function(){
		if(mobile != ''){
			$('.clear_password').show();
		}else {
			$('.clear_password').hide();
		}
	});	
}

$('.clear_password').hide();

// 注册 - 验证码  按钮状态改变  
function step2change() {
	var authenticationCode = $("#authenticationCode").val();
	// 如果是合法的验证码 ，验证码长度为6
	if($.trim(authenticationCode) != "" && authenticationCode.length ==6 ) {
		$("#step2_show").show();
		$("#step2_hide").hide();
	} else {
		$("#step2_show").hide();
		$("#step2_hide").show();
	}
	
	// 验证码
	
	
}

//注册 - 提交 按钮状态改变  
function step3change() {
	var password = $("#password").val();
	// 如果是合法的手机号
	if($.trim(password) != "" && password.length > 5 ) {
		$("#step3_show").show();
		$("#step3_hide").hide();
		$("#commiting_show").hide();
	} else {
		$("#step3_show").hide();
		$("#step3_hide").show();
		$("#commiting_show").hide();
	}
}

//手机号校验
function isMobile(m) {
	if ($.trim(m) == "" || !m.match(/^1[3|4|5|7|8][0-9]\d{4,8}$/)
			|| m.length != 11) {
		return false;
	} else {
		return true;
	}
}

// 统一登录 
function union_login(url,serviceUrl) {
	// 把url存到cookies中 setcookie(cookieName, cookieValue, seconds, path, domain, secure) 
	if(null == serviceUrl ) {
		serviceUrl = "";
	}
	try{
		setcookie('serviceUrl', serviceUrl, 72000000, '/', ".lvmama.com",false); //设置cookie 秒
	}catch(e){
		
	}
	window.location.href=url;
}

