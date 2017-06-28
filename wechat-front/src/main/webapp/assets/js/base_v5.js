// JavaScript Document
var SysSecond;
var InterValObj;
var t = n =0, count;// 轮播参数
var LT_LOADING_MSG = "请稍等..."; // 提示信息
var CONTENT_LOGDING = "loading"; // 提示信息
var LT_LOADING_TIME = 10000; // 加载等待时间 30秒
var LT_LOADING_CLOSE = 3000; // 关闭时间3秒
var LT_ORDER_SUBMIT_ERROR = "哎呀,网络不给力,请稍后再试试吧";
//页面跳转.
function union_skip(url) {
	//lvToast(CONTENT_LOGDING, LT_LOADING_MSG, LT_LOADING_TIME);
	window.location.href = url;
}
String.prototype.startWith=function(str){
	if(str==null||str==""||this.length==0||str.length>this.length)
	  return false;
	if(this.substr(0,str.length)==str)
	  return true;
	else
	  return false;
	return true;
}

function getUrlParam(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); // 构造一个含有目标参数的正则表达式对象
	var r = window.location.search.substr(1).match(reg); // 匹配目标参数
	if (r != null)
		return unescape(r[2]);
	return null; // 返回参数值
}

function getcookie(name) {
	var cookie_start = document.cookie.indexOf(name);
	var cookie_end = document.cookie.indexOf(";", cookie_start);
	return cookie_start == -1 ? '' : unescape(document.cookie.substring(
			cookie_start + name.length + 1,
			(cookie_end > cookie_start ? cookie_end : document.cookie.length)));
}
function setcookie(cookieName, cookieValue, seconds, path, domain, secure) {
	var expires = new Date();
	expires.setTime(expires.getTime() + seconds);
	document.cookie = escape(cookieName) + '=' + escape(cookieValue)
			+ (expires ? '; expires=' + expires.toGMTString() : '')
			+ (path ? '; path=' + path : '/')
			+ (domain ? '; domain=' + domain : '') + (secure ? '; secure' : '');
}






function lvToast(isSuccess, toastText, time) {
    var that = this;
    //设置默认的时间
    if (time) {
        time = time;
    } else {
        time = 3000;
    }
    if ($("#lv-temp-toast").length != 0) {
        return false;
    } else {
        $("body").append("<div id='lv-temp-toast' class='lv-toast' style='display:none;'><div class='lv-toast-div'></div><p></p></div>");
    }

    var toastLeft = ($("html").width() - 200) / 2;
    var toastTop = $("html").scrollTop() + 200;
    var $toast = $("#lv-temp-toast");
    $toast.css({
        "left": toastLeft,
        "top": toastTop,
        "opacity": "0.7"
    });
    $toast.children("p").html(toastText);
    if (isSuccess == that.CONTENT_LOGDING) {
        $toast.addClass('lv-toast-loading').css({
            top: toastTop
        }).empty().append('<img style="width:32px;" src="//pics.lvjs.com.cn/img/mobile/touch/img/3.gif />').fadeIn(600);
        setTimeout(function() {
            $toast.addClass("lv-toast").addClass('lv-toast-loading').css({
                top: toastTop
            }).fadeOut(1000, function() {
                    $(this).remove();
                });
        }, time);
    } else if (isSuccess) {
        $toast.children('div').append('<img src="//pics.lvjs.com.cn/img/mobile/touch/img/smile-grey.png" />');
        $toast.addClass("lv-toast").addClass("lv-toast-success").fadeIn(600);
        setTimeout(function() {
            $toast.fadeOut(1000, function() {
                $(this).remove();
            });
        }, time);
    } else {
        $toast.children('div').append('<img src="//pics.lvjs.com.cn/img/mobile/touch/img/sad-big.png" />');
        $toast.addClass('lv-toast').addClass("lv-toast-fail").fadeIn(600);
        setTimeout(function() {
            $toast.fadeOut(1000, function() {
                $(this).remove();
            });
        }, time);
    }
}