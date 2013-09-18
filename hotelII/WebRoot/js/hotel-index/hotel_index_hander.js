//酒店搜索按钮提交处理	add by hushunqiang
function formSubmit(){
	
	//参数获取
	var cityName = document.getElementById("start_city").value;
	var citycode = document.getElementById("hide_city").value;
	var indate = document.getElementById("start_time").value;
	var outdate = document.getElementById("end_time").value;
	var hotelName = $("#hotelName").val();
	hotelName = hotelName.split(" ").join("");
	
	var geoName ="";
	var brandcode = "";
	//1,城市输入框必填项校验

	if(cityName=='' || cityName == '请选择城市' || cityName == "中文/拼音"){
		jQuery("#start_city").alert("请填写城市！");
		return;
	}
	
	if(cityName.length > 30){
		jQuery("#start_city").alert("城市输入不合法！");
		return;
	}
	
	if(citycode==null || citycode==''){
		jQuery("#start_city").alert("请正确输入正确的查询城市！");
		return;
	}
	
	//2,入住日期必填项校验
	if(indate==null || indate==''){
		jQuery("#start_time").alert("请选择正确的入住日期！");
		return false;
	}
	
	//3,入住日期必填项校验
	if(outdate==null || outdate==''){
		jQuery("#end_time").alert("请选择正确的离店日期！");
		return false;
	}

	//4,离店日期需大于入住日期校验
	var instrlist = indate.split(" "); 
	var datestri = instrlist[0].split("-");	
	var date1 = new Date(datestri[0],datestri[1]-1,datestri[2],0,0,0); 	
	var outstrlist = outdate.split(" "); 
	var datestro = outstrlist[0].split("-"); 	
	var date2 = new Date(datestro[0],datestro[1]-1,datestro[2],0,0,0); 
	if(date1>=date2){
		jQuery("#end_time").alert("离店日期不能小于入住日期！");
		return;
	}
	
    var date0 = new Date(datestri[0],datestri[1]-1,datestri[2],0,0,0);
    date0.setDate(date0.getDate()+29);
	if(date0 < date2){
		jQuery("#end_time").alert("离店日期与入住日期相差不能大于28天！");
		return;
	}
	var dd= GetDateStr(60);
	var yy=GetDateStr(61);
	
	var dd1 = dd.split("-");	
	var date60 = new Date(dd1[0],dd1[1]-1,dd1[2],0,0,0); 	

	var yy1 = yy.split("-"); 	
	var date61 = new Date(yy1[0],yy1[1]-1,yy1[2],0,0,0); 
	
	if(date1> date60){
		jQuery("#start_time").alert("入住日期选择范围为60天内！");
		return;
	}
	
	if(date2> date61){
		jQuery("#end_time").alert("离店日期选择范围为61天内！");
		return;
	}
	
	if(hotelName === "无相应记录"){
		hotelName="";
	}
	
	if(hotelName.length > 30){
		jQuery("#hotelName").alert("关键词输入不合法！");
		return;
	}
	
	var flaghotel = 0;
	var flaggis = 0;
	//5,关键词搜索处理
	if(hotelName!=null && hotelName !=='' && hotelName !== "无相应记录"){
		
		 var hotellist = hotelnamekeyarray.split("|");
		 for(var i=0;i<hotellist.length;i++){
		    var hotel = hotellist[i];
		    if(hotel == hotelName){
		    	flaghotel=1;
		    	geoName = "";
		    	break;
		    }
		  }
		 if(flaghotel == 0){
		 	var gislist = hotelgiskeyarray.split("|");
		 	for(var i=0;i<gislist.length;i++){
		    var gis = gislist[i];
		    if(gis == hotelName){
		    	flaggis=1;
		    	geoName = hotelName;
		    	hotelName = "";
		    	break;
		    	}
		  	}
		 }
		 
		 if(flaghotel == 0 && flaggis == 0){
			  var list = brandkeyarray.split("|");
			  for(var i=0;i<list.length;i++){
			    var cashbrand = list[i].split("=");
			    if(cashbrand[0] == hotelName){
			    	brandcode = cashbrand[1];
			    }
			   }
			   
			  if(brandcode !==""){
			  	brandkeyarray="";
				window.location.href="hotel-brand-"+brandcode+".html";
				window.event.returnValue = false;
				return;
			  }
		 }
		hotelnamekeyarray="";
		hotelgiskeyarray="";
	}

	//6,提交表单赋值
	$("#e_cityCode").attr("value",citycode);
	$("#e_cityName").attr("value",cityName);
	$("#e_inDate").attr("value",instrlist[0]);
	$("#e_outDate").attr("value",outstrlist[0]);
	$("#e_hotelName").attr("value",hotelName);
	$("#e_geoName").attr("value",geoName);
	$("#e_hotelName").attr("value",hotelName);
	if(hotelName=="酒店名称/品牌/地区" || hotelName==""){
		$("#e_hotelName").attr("value","");
	}
	
	//6,cookie设置
	setCookie("cityName",cityName,"h1");
	setCookie("cityCode",citycode,"h1");
	setCookie("cachecity",cityName+"|"+citycode,"h1");

	
	//7,提交查询条件
	setTimeout(function(){document.queryForm.submit();},10);
}


//地图查找酒店搜索按钮链接验证 add by hushunqiang
function mapSubmit(){
	//参数获取
	var cityName = document.getElementById("start_city").value;
	var citycode = document.getElementById("hide_city").value;
	var indate = document.getElementById("start_time").value;
	var outdate = document.getElementById("end_time").value;
	var hotelName = document.getElementById("hotelName").value;
	hotelName = hotelName.split(" ").join("");
	
	//1,城市输入框必填项校验
	
	if(cityName=='' || cityName == '请选择城市' || cityName == "中文/拼音"){
		jQuery("#start_city").alert("城市不能为空！");
		return;
	}
	
	if(citycode==null || citycode==''){
		jQuery("#start_city").alert("请正确输入查询的城市！");
		return;
	}
	
	//2,入住日期必填项校验
	if(indate==null || indate==''){
		jQuery("#start_time").alert("请选择正确的入住日期！");
		return false;
	}
	
	//3,入住日期必填项校验
	if(outdate==null || outdate==''){
		jQuery("#end_time").alert("请选择正确的离店日期！");
		return false;
	}

	//4,离店日期需大于入住日期校验
	var instrlist = indate.split(" "); 
	var datestri = instrlist[0].split("-");	
	var date1 = new Date(datestri[0],datestri[1]-1,datestri[2],0,0,0); 	
	var outstrlist = outdate.split(" "); 
	var datestro = outstrlist[0].split("-"); 	
	var date2 = new Date(datestro[0],datestro[1]-1,datestro[2],0,0,0); 
	if(date1>=date2){
		jQuery("#end_time").alert("离店日期不能小于入住日期！");
		return;
	}
	
	//5,提交表单赋值
	$("#e_cityCode").attr("value",citycode);
	$("#e_cityName").attr("value",cityName);
	$("#e_inDate").attr("value",instrlist[0]);
	$("#e_outDate").attr("value",outstrlist[0]);
	$("#e_hotelName").attr("value","");
	if(hotelName=="酒店名称/品牌/地区" || hotelName==""){
		$("#e_hotelName").attr("value","");
	}

	document.queryForm.action="http://hotel.mangocity.com/hotelEmap/";
	document.queryForm.method="post";
	setTimeout(function(){document.queryForm.submit();},10);
}



  //模糊查询酒店
function asynkeywordQueryHotel(){

   	$("#hotelName").autocomplete("asyhotelkeywordAutoShow.shtml",{
          minChars:1,
		  autoFill:false,
		  mustMatch:false,
		  matchContains:false,
		  max:10,
		  delay:200,
		  cacheLength:1,
		  matchSubset:false,
		  matchCase:false,
		  scroll:false,
		  width : 230,
		  dataType : 'json',
           //如果需要另外传值给action的话可以用这样的方式,如果只是把input里面的值传递给action的话,可以不写  
           extraParams: {   
              keyword: function() { 
                return $("#hotelName").val(); 
              },
              cityCode: function(){
                return $("#hide_city").val();
              }  
           },
           //进行对返回数据的格式处理
           parse: function(data){
               //content为我action里面定义的值
               var rows = [];       
               //关键词搜索结果展示处理
               Temporarykeywordsave(data.content);
               if(data == null || data.content == null){
               	return;
               }
               for(var i=0; list = data.content,i<list.length; i++){
                 rows[rows.length] = 
                {
                   //我没有特殊的要求,所以三个写的一样                  
                   data:list[i],
                   value:list[i],
                  //result里面显示的是要返回到列表里面的值  
                   result:list[i]
                 };
                 $("#hotelName").removeClass("inputSel");
               }           
               return rows;
           },
           formatItem:function(item){
                   //我没有特殊的要求,直接返回了
                   return item;
           }
      });
}

//关键词搜索品牌中品牌编码保存，用于选择品牌是页面链接组装跳转
var brandkeyarray="";
var hotelnamekeyarray="";
var hotelgiskeyarray="";
function Temporarykeywordsave(list){
	if(list == null){
	 return;
	}

	for(var i=0 ;i<list.length;i++){
		if(list[i].indexOf("品牌：") == 0) 
		{   
		    var keylist = list[i].split("：");
		    var brand  = keylist[1].split("|");
		    brandkeyarray = brandkeyarray+brand[0]+"="+brand[1]+"|";
		    list[i]=brand[0];
		} 
		
		if(list[i].indexOf("酒店：") == 0) 
		{   
		    var hotel = list[i].split("：");
		    hotelnamekeyarray = hotelnamekeyarray+hotel[1]+"|";
		    list[i]=hotel[1];
		} 
		
		if(list[i].indexOf("位置：") == 0) 
		{   
		    var gis = list[i].split("：");
		    hotelgiskeyarray = hotelgiskeyarray+gis[1]+"|";
		    list[i]=gis[1];
		} 
	}
}

//城市切换时，地标更新
function gitswitchCity(citycode,callbackFun){

	var url = "asyhotelCityGitSearch.shtml";
	var params = {
		"cityCode":citycode
	};
	try {
		asynchronousJs(url,params,'POST',false,'json',callbackFun);
		return;
	} catch(e){ 
		alert(e); 
	}
}


// jquery ajax 数据同步获取
function asynchronousJs(url,params,type,async,dataType,successFun) {
	//$.ajaxTimeout(5000);
	$.ajax({   
        url: url,//地址   
        cache: false,
       	data: params,//参数   
       	type: type,//提交方式 可以选择post/get 推荐post    
        async: async,//是否异步    
       	dataType:dataType,//返回数据类型    
       	success: successFun
    });	
}

// 热门城市酒店查询处理
function hotcityquery(id){
	//1,数据获取
	var clickid = id;
	var indate = GetDateStr(1);
	var outdate = GetDateStr(2);
	var strlist = clickid.split("-")
	var citycode = strlist[0];
	
	//2,查询条件数据重新封装
	$("#e_cityCode").attr("value",citycode);
	$("#e_cityName").attr("value","");
	$("#e_inDate").attr("value",indate);
	$("#e_outDate").attr("value",outdate);
	$("#e_hotelName").attr("value","");

	//3,form表单提交
	setTimeout(function(){document.queryForm.submit();},10);
}

//选择城市cookie记录
function setCookie(name,value,time)
{
	var strsec = 30*24*60*60*1000;
    var exp = new Date();
    exp.setTime(exp.getTime() + strsec*1);
    document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
}

//获取城市cookie记录
function getCookie(c_name){
	if(document.cookie.length>0){
	   c_start=document.cookie.indexOf(c_name + "=")
	   if(c_start!=-1){
	     c_start=c_start + c_name.length+1
	     c_end=document.cookie.indexOf(";",c_start)
	     if(c_end==-1) c_end=document.cookie.length
	     return unescape(document.cookie.substring(c_start,c_end))
	   }
	}
	return ""
}

//日期处理器，本次用于获取明天及后天时间 add by hushunqiang
function GetDateStr(AddDayCount) {
    var dd = new Date();
    dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
    var y = dd.getFullYear();
    var m = (dd.getMonth()+1) < 10 ? ("0" + (dd.getMonth() + 1)) : (dd.getMonth() + 1);
    var d = dd.getDate()<10?("0" + (dd.getDate())) : (dd.getDate());
    return y+"-"+m+"-"+d;
}

function keydown(evt) {
    if(evt.keyCode==13) {
       formSubmit();
    }
}

