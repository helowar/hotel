//参数说明：图片路径, 链接地址, name值, alt的值, 图片宽, 图片高

//酒店首页搜索框下方的长条banner
//第一个参数说明：Y表示显示此banner, N表示隐藏此banner
var hotel_index_Ad = new Array();
hotel_index_Ad[0]=new Array('http://www.mangocity.com/promotion/hotel_neiqian/img/beijing.jpg','http://www.mangocity.com/promotion/h_zj20130422bjxq/index.html','北京新侨诺富特','573','250');
hotel_index_Ad[1]=new Array('http://www.mangocity.com/promotion/hotel_neiqian/img/gaokao.jpg','http://www.mangocity.com/promotion/h_zj20130515gaokao/index.html','高考酒店专辑','573','250');
hotel_index_Ad[2]=new Array('http://www.mangocity.com/promotion/hotel_neiqian/img/changlong.jpg','http://www.mangocity.com/promotion/h_zj20130506gzcl/index.html','广州长隆','573','250');
hotel_index_Ad[3]=new Array('http://www.mangocity.com/promotion/hotel_neiqian/img/hcg.jpg','http://www.mangocity.com/promotion/h20130409hcg/index.html','香港红茶馆”','573','250');
hotel_index_Ad[4]=new Array('http://www.mangocity.com/promotion/hotel_neiqian/img/xuancai.jpg','http://www.mangocity.com/promotion/h_zj20130502hwjd/index.html','炫彩一夏','573','250');

 




function loadBanner_common(id,point,classStyle){
	var bannerStr="";
	var bannerInfos_used ;
	var classStyle_uesd="cont01_left02 mgb10";
	//找到相关的bannerInfos
	if(point=="hotel_hk_left"){
	   bannerInfos_used = bannerInfos4;
    }
    if(classStyle!=null){
       classStyle_uesd = classStyle;
    }
	for(var i=0;i<bannerInfos_used.length;i++){
		bannerStr=bannerStr+'<div class="'+classStyle_uesd+'"><a href="'+bannerInfos_used[i][1]+'" target="_blank" name="'+bannerInfos_used[i][2]+'"><img src="'+bannerInfos_used[i][0]+'" alt="'+bannerInfos_used[i][3]+'" width="'+bannerInfos_used[i][4]+'" height="'+bannerInfos_used[i][5]+'" border="0" /></a></div>';	
	}
	document.getElementById(id).innerHTML=bannerStr;
}