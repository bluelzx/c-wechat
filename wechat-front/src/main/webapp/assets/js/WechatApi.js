/**
 * ! 驴妈妈微信分享通用API，功能包括：
 * 
 * 1、分享到微信朋友圈 给微信好友 到腾讯微博 QQ QQ空间
 * 
 */
var WechatApi = (function() {

	var apiUrls = {
		getJsApiSign : '//weixin.lvmama.com/api/jsApiSign'
	}

	/**
	 * 初始化配置
	 */
	function init() {
		$.ajax({
			url : apiUrls.getJsApiSign,
			data : {
				shareUrl : encodeURIComponent(location.href.split('#')[0])
			},
			type : "GET",
			success : function(result) {
				if (result.code == '1') {
					wx.config({
						debug : false,
						appId : result.data.appId,
						timestamp : result.data.timestamp,
						nonceStr : result.data.nonceStr,
						signature : result.data.signature,
						jsApiList : [ 'onMenuShareTimeline',
								'onMenuShareAppMessage', 'onMenuShareQQ',
								'onMenuShareWeibo', 'onMenuShareQZone','checkJsApi',
						        'openLocation','getLocation']
					});
				}
			},
			error : function() {
				console.log('初始化微信接口失败....');
			}
		});
	}

	/**
	 * 注册分享（一次注册所有分享渠道）
	 */
	function share(data, successCallback, cancelCallback) {
		successCallback = successCallback || function() {
		};
		cancelCallback = cancelCallback || function() {
		};
		wx.ready(function() {
			// 分享给微信好友
			wx.onMenuShareAppMessage({
				title : data.title,
				desc : data.desc,
				link : data.link,
				imgUrl : data.imgUrl,
				type : data.type, // 分享类型,music、video或link，不填默认为link
				dataUrl : data.dataUrl, // 如果type是music或video，则要提供数据链接，默认为空
				success : successCallback,
				cancel : cancelCallback
			});
			// 分享到朋友圈
			wx.onMenuShareTimeline({
				title : data.title,
				link : data.link,
				imgUrl : data.imgUrl,
				success : successCallback,
				cancel : cancelCallback
			});
			// 分享给QQ好友
			wx.onMenuShareQQ({
				title : data.title,
				desc : data.desc,
				link : data.link,
				imgUrl : data.imgUrl,
				success : successCallback,
				cancel : cancelCallback
			});
			// 分享到QQ空间
			wx.onMenuShareQZone({
				title : data.title,
				desc : data.desc,
				link : data.link,
				imgUrl : data.imgUrl,
				success : successCallback,
				cancel : cancelCallback
			});
			// 分享到腾讯微博
			wx.onMenuShareWeibo({
				title : data.title,
				desc : data.desc,
				link : data.link,
				imgUrl : data.imgUrl,
				success : successCallback,
				cancel : cancelCallback
			});			
			
		});
	}
	//获取地理位置信息
	function getlocation(data, successCallback, cancelCallback){
		successCallback = successCallback || function() {
		};
		cancelCallback = cancelCallback || function() {
		};
		wx.ready(function () {
			wx.getLocation({
				success : successCallback,
			    cancel: function (res) {
			        alert('用户拒绝授权获取地理位置');
			    }
			});
		});
	}

	(function() {
		init();
	})()

	return {
		share : share,
		getlocation:getlocation
	}
})();