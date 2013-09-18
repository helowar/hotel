$(function(){
	
	//0，浏览器兼容性样式设置
   	browserCompatibility();
	
	//1，初始化搜索框默认城市
   	initDefaultScity();
   	
   	//2，初始化地标处默认模块
   	initDefaultGcity();
	
	//2，页面加载初始化酒店品牌信息
	initbrandshow();
	
	//3,幻灯广告banner初始化
   	initAdBanner();
   	
   	//4,关键字搜索酒店，ajax同步酒店名称
    asynkeywordQueryHotel();
   	
    //5,"搜索"按钮提交
    $('#id_query').click(function(){
      formSubmit();
    })
   
    //6，"在地图上查找酒店"按钮提交
    $('#map_query').click(function(){
       mapSubmit();
    })  
    
	//7,初始化芒果网时间控件的调用
    initflowdatetool();
	
	//8,广告轮播控件调用
	$('#cycleWrap').mangocycle();
	
	//9,初始化城市弹出浮动框
    initflowcitytool();
    
    jQuery.fn.alert.defaults.alertClass = 'validate';
});




