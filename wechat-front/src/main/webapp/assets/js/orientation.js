$(function(){ 
	clouda.lightInit({ 
		ak:"4tjhI1dx8WPmCIKtl365x7Xg", //apikey 
		module:["geolocation","app","account"] 
}); 
});
//成功回调
var dingwei_onsuccess = function(result) {
	console.log(result);
	result.status=1;
	
	dingwei_succ_callback(result);
};
//失败回调
var dingwei_onfail = function(err) {
	console.log(err);
	err.status=0;
	
	dingwei_succ_callback(err);
};
//定位方法
function startListenLocation() {
	clouda.device.geolocation.get({
		onsuccess:dingwei_onsuccess,
		onfail:dingwei_onfail
	});
}
