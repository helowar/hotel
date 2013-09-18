//信用卡显示方式
function showCardCadre(cardStr) {
 	    //var cardStr = '大莱,运通,JCB,万事达,银联卡,Visa,深发行卡,长城,牡丹,龙卡,金穗卡,广发行卡,招行信用卡,兴业信用卡';
		if(cardStr.indexOf("Visa")>0){
			cardStr=cardStr.replace("Visa", "");
			cardStr="Visa,"+cardStr;
		}
		if(cardStr.indexOf("所有银联卡")>0){
			cardStr=cardStr.replace("所有银联卡", "");
			cardStr="所有银联卡,"+cardStr;
		}
		if(cardStr.indexOf("所有银联卡")>=0){
			cardStr=cardStr.replace("发展卡", "").replace("长城卡", "").replace("牡丹卡", "").replace("龙卡", "").replace("金穗卡", "").replace("广发卡", "").replace("招行卡", "").replace("兴业卡", "");
		}
		cardStr=cardStr.replace(/\,/g,"    ");
		
		var left = "<img src='http://himg.mangocity.com/img/card";
		var right = ".jpg' align='absmiddle' title='"
		var right1 = "'/>";
		cardStr = cardStr.replace("DC（大莱）",left+1+right+"DC（大莱）"+right1).replace("AE（运通卡）",left+2+right+"AE（运通卡）"+right1)
		.replace("JCB",left+3+right+"JCB"+right1).replace("Visa",left+8+right+"Visa"+right1)
		.replace("Master（万事达卡）",left+9+right+"Master（万事达卡）"+right1).replace("所有银联卡",left+10+right+"所有银联卡"+right1);
		document.write(cardStr);
	}
	
	// 地图浮动层
function googleMapPanel(hotelId) { 
    var a = '<iframe src="http://www.mangocity.com/hotelEmap/hotel-information-map.shtml?hotelId='+hotelId+'" name="framename" width="100%" marginwidth="0" height="100%" marginheight="0" scrolling="No" frameborder="no" id="framename" border="0"></iframe>';
    $("#loadMap").html(a);
	$("#id_Emap").modal({
		closeHTML : '<div class="guanbi"><a title="Close" class="closeMode" style="cursor:pointer;" target="_blank">关闭</a></div>',
		position : ["25%"],
		overlayId : 'contact-overlay',
		closeClass : ('closeMode'),
		overlayClose: true
	});	
}
//酒店图片浮动层

function hotelPicPanel(){
	$("#hotelPic").modal({
		closeHTML : '<div class="guanbi"><a title="Close" class="closeMode" style="cursor:pointer;" target="_blank">关闭</a></div>',
		position : ["25%"],
		overlayId : 'contact-overlay',
		closeClass : ('closeMode'),
		overlayClose: true
	});
}
//酒店360图片浮动层
function hotelPic360Panel(){
	$("#showPic360").modal({
		closeHTML : '<div class="guanbi"><a title="Close" class="closeMode" style="cursor:pointer;" target="_blank">关闭</a></div>',
		position : ["25%"],
		overlayId : 'contact-overlay',
		closeClass : ('closeMode'),
		overlayClose: true
	});
}

function switchTab(num,piCount){
	var id_name="tab"+num;
	var tr_name="detail_panel"+num;
	var temp = $( document ).scrollTop();
	for(i=1;i<=piCount;i++){
		if(document.getElementById("tab"+i) != null)
			document.getElementById("tab"+i).className="dnormal";
		if(document.getElementById("detail_panel"+i) != null)
			document.getElementById("detail_panel"+i).style.display="none";
	}
	document.getElementById(id_name).className="don";
	document.getElementById(tr_name).style.display="block";
	
	setTimeout( function(){ $(document).scrollTop(temp); },10) 
	
}


function switchDisplay(){
	document.getElementById("tel").style.display="";
}

$(document).ready(function(){
 autoQueryHotelName();
$(".hotelPicSmall").click(function (){
	$("#hotelPicBig").attr("src",$(this).attr("src"));
});
loadHotelBanner("hotelBanner");
$('#id_query').click(function(){
     formSubmit();
  })    
});

function showMes(id,message){
	var obj = document.getElementById(id);
	 message=(message==null?obj.defaultValue:message);
	if(obj.value.trim()===""){
	    obj.value=message;
	    obj.style.color="#777777";
	}
}	

function hiddenMes(id,message){
    var obj = document.getElementById(id);
    message=(message==null?obj.defaultValue:message);
	if(obj.value==message){
	    obj.value="";
	    obj.style.color ="#333333";
	}
}

  //模糊查询酒店名称
function autoQueryHotelName(){
   $("#hotelName").autocomplete("hotelNameAutoShow.shtml",{
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
		  width : 210,
		  dataType : 'json',
           //如果需要另外传值给action的话可以用这样的方式,如果只是把input里面的值传递给action的话,可以不写  
           extraParams: {   
              hotelName: function() { 
                return $("#hotelName").val(); 
              },
              cityCode: function(){
                return $("#cityCode").val();
              }  
           },
           //进行对返回数据的格式处理
           parse: function(data){
               //content为我action里面定义的值
               var rows = [];       
               for(var i=0; list = data.content,i<list.length; i++){
                 rows[rows.length] = 
                {
                   //我没有特殊的要求,所以三个写的一样                  
                   data:list[i],
                   value:list[i],
                  //result里面显示的是要返回到列表里面的值  
                   result:list[i]
                 };
               }           
               return rows;
           },
           formatItem:function(item){
                   //我没有特殊的要求,直接返回了
                   return item;
           }
      });
}

//提交验证	
function formSubmit(){
	
	var dD = document.getElementById("id_startDate").value;
	var eD = document.getElementById("id_backDate").value;
	if(dD==null || dD=='' || dD == '入住日期'){
		jQuery("#id_startDate").alert("请选择入住日期！");
		return false;
	}
	if(eD==null || eD=='' || eD == '离店日期'){
		jQuery("#id_backDate").alert("请选择离店日期！");
		return false;
	}
	//modify by shizhongwen 2.10 start
	var s = dD.split(" "); 
	var s1 = s[0].split("-");	
	var date1 = new Date(s1[0],s1[1]-1,s1[2],0,0,0); 	
	var b = eD.split(" "); 
	var b1 = b[0].split("-"); 	
	var date2 = new Date(b1[0],b1[1]-1,b1[2],0,0,0); 
	//modify by shizhongwen 2.10 end

	if(date1>=date2){
		jQuery("#id_backDate").alert("提示入住日期应早于离店日期！");
		return false;
	}
	
	//modify by alfred,入住晚数可以大于28天。--2011-6-22
	/*
	if(daysElapsed(date1,date2)>28){
		jQuery("#id_backDate").alert("入住晚数不能超过28天！");
		return false;
	}
	*/
	var cityName = document.getElementById("id_startCity").value;
	if(cityName==null || cityName=='' || cityName == '选择城市'){
		jQuery("#id_startCity").alert("请选择城市！");
		return false;
	}
	var cityCode = jQuery("#cityCode").val();
	if(cityCode==null || cityCode==''){
			jQuery("#id_startCity").alert("请正确输入查询的城市！");
			return false;
		}
	if(jQuery("#hotelName").val()==document.getElementById("hotelName").defaultValue){
	   jQuery("#hotelName").val("");
	}
	setTimeout(function(){document.queryForm.submit();},10);
	
}


//日历比较
function daysElapsed(date1,date2) {
    var difference = Date.UTC(date1.getYear(),date1.getMonth(),date1.getDate(),0,0,0)
                   - Date.UTC(date2.getYear(),date2.getMonth(),date2.getDate(),0,0,0);
    var difdays=difference/(1000*60*60*24);
    if(difdays<0){
    	return 0-difdays;
    }else{
    	return difdays;
    }
}

function loadHotelBanner(id){
	var bannerStr="";
	for(var i=0;i<bannerInfos3.length;i++){
		bannerStr=bannerStr+'<div class="hotelbanner"><a href="'+bannerInfos3[i][1]+'" target="_blank" name="'+bannerInfos3[i][2]+'"><img src="'+bannerInfos3[i][0]+'" alt="'+bannerInfos3[i][3]+'" width="'+bannerInfos3[i][4]+'" height="'+bannerInfos3[i][5]+'" border="0" /></a></div>';	
	}
	document.getElementById(id).innerHTML=bannerStr;
}

//商区和行政区的button切换
function changeZone(index){
	switch(index) {
		case 1: {
			document.getElementById('show_bizZonehtl').style.display = "";
			document.getElementById('show_distincthtl').style.display = "none";
			jQuery("#button_bizZonehtl_show").attr("class","on");
			jQuery("#button_distincthtl_show").attr("class","");
			break;
		}
		case 2: {
			document.getElementById('show_bizZonehtl').style.display = "none";
			document.getElementById('show_distincthtl').style.display = "";
			jQuery("#button_distincthtl_show").attr("class","on");
			jQuery("#button_bizZonehtl_show").attr("class","");
			break;
		}	
	}
}
jQuery.fn.alert.defaults.alertClass = 'validate';