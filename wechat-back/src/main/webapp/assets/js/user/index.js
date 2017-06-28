function iFrameHeight() {
	var ifm = document.getElementById("mainFrame");
	var subWeb = document.frames ? document.frames["mainFrame"].document : ifm.contentDocument;
	if (ifm != null && subWeb != null) {
		var docHeight = document.documentElement.clientHeight;
		var navbarHeight = document.getElementById("navbar").offsetHeight;
		var breadcrumbsHeight = document.getElementById("breadcrumbs").offsetHeight;
		var footerHeight = 80;
		var ifmMinHeight = docHeight - navbarHeight - breadcrumbsHeight - footerHeight;
		console.log(docHeight + " " + navbarHeight + " " + breadcrumbsHeight + " " + footerHeight + " " + ifmMinHeight);
		ifm.height = subWeb.body.scrollHeight > ifmMinHeight ? subWeb.body.scrollHeight : ifmMinHeight;
	}
}
/**
 * 高亮选中的菜单项
 * @return {[type]} [description]
 */
function activeMenu(obj, url) {
	$("#mainFrame").attr('src',url)
	//移除所以li的active状态
	$(".submenu li").removeClass('active');
	$(".nav-list li").removeClass('active');
	//高亮当前选择菜单和父菜单
	$(obj).parent().addClass('active');
	$(obj).parent().parent().parent().addClass('active');
	//更新面包屑
	$("#breadcrumbs #1stBreadcrumb").text($(obj).parent().parent().parent().find(".menu-text").text());
	$("#breadcrumbs #2ndBreadcrumb").text(obj.text);
}