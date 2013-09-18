
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
		jQuery("#id_startDate").alert("提示入住日期应早于离店日期");
		return;
	}
	//modify by alfred，入住晚数可以大于28天	--2011-6-22
	/*
	if(daysElapsed(date1,date2)>28){
		jQuery("#id_startDate").alert("入住晚数不能超过28天！");
		return;
	}
	*/
	var cityName = document.getElementById("id_startCity").value;
	if(cityName==null || cityName=='' || cityName == '选择城市'){
		jQuery("#id_startCity").alert("请输入城市");
		return;
	}
	var cityCode = jQuery("#cityCode").val();
	if(cityCode==null || cityCode==''){
			jQuery("#id_startCity").alert("请正确输入查询的城市");
			return;
		}
	//酒店星级
	var star = document.getElementsByName("hotelStarStr");
	var addStarstr = "";	
	for(var i=0; i<star.length;i++){
	    if(star[i].checked){
	         addStarstr += star[i].value;
	         addStarstr += "#";
	    }
	}
	if(addStarstr==""){
	    document.getElementById("hotelStar").value=addStarstr;
	}
	if(addStarstr!=""){
	    document.getElementById("hotelStar").value=addStarstr.substr(0,addStarstr.length-1);
	}
	//价格处理
	var priceStr = jQuery("#priceStr_input").val();
	if(priceStr=="不限"){
	  jQuery("#priceStr").val("");
	}
	if(priceStr=="200元以下"){
	   jQuery("#priceStr").val("0-200");
	}
	if(priceStr=="200-500元"){
       jQuery("#priceStr").val("200-500");
	}
	if(priceStr=="500-800元"){
	   jQuery("#priceStr").val("500-800");
	}
	if(priceStr=="800元以上"){
	   jQuery("#priceStr").val("800-");
	}
	
	
	//alert($("#cityCode").val());
	//alert("星级："+ document.getElementById("hotelStar").value);
	jQuery("#queryDiv").hide();
	jQuery("#waitQuery").show();
	if(jQuery("#hotelName").val()==document.getElementById("hotelName").defaultValue){
	   jQuery("#hotelName").val("");
	}
	if(jQuery("#geoName").val()==document.getElementById("geoName").defaultValue){
	   jQuery("#geoName").val("");
	}
	setTimeout(function(){document.queryForm.submit();},10);
	
}

function mapSubmit(){
	var dD = document.getElementById("id_startDate").value;
	var eD = document.getElementById("id_backDate").value;
	if(dD==null || dD=='' || dD == '入住日期'){
		jQuery("#id_backDate").alert("请选择入住日期！");
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
		jQuery("#id_backDate").alert("提示入住日期应早于离店日期");
		return;
	}
	
	var cityName = document.getElementById("id_startCity").value;
	if(cityName==null || cityName=='' || cityName == '选择城市'){
		jQuery("#id_startCity").alert("请输入城市");
		return;
	}
	var cityCode = jQuery("#cityCode").val();
	if(cityCode==null || cityCode==''){
			jQuery("#id_startCity").alert("请正确输入查询的城市");
			return;
		}
	if($("#hotelName").val()==document.getElementById("hotelName").defaultValue){
	  $("#hotelName").val("");
	 }
	 if($("#geoName").val()==document.getElementById("geoName").defaultValue){
	  $("#geoName").val("");
	  }
	 if($("#priceStr_input").val()!=document.getElementById("priceStr_input").defaultValue){
	  $("#priceStr").val($("#priceStr_input").val());
	  }else{
	   $("#priceStr").val("");
	  }
	//alert($("#cityCode").val());
	//alert("星级："+ document.getElementById("hotelStar").value);
	jQuery("#queryDiv").hide();
	jQuery("#waitQuery").show();
	
	document.queryForm.action="http://hotel.mangocity.com/hotelEmap/";
	document.queryForm.method="post";
	setTimeout(function(){document.queryForm.submit();},10);
}

function keydown(evt) {
    if(evt.keyCode==13) {
       formSubmit();
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

function autoQueryHotelAddress(){
 $("#geoName").autocomplete("hotelAddressAutoShow.shtml",{
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
              distanceName: function() { 
                return $("#geoName").val(); 
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

//商区
	jQuery.fn.selectCity = function(targetId) {
		var _seft = this;
		var targetId = $(targetId);
		this.click(function(){
		    hiddenMes(this.id);
			var A_top = $(this).offset().top + $(this).outerHeight(true)+2;  //  1
			var A_left =  $(this).offset().left;//定位浮动层位置
			targetId.bgiframe();
				targetId.show().css({"position":"absolute","top":A_top+"px" ,"left":A_left+"px"});
			if((null==$('#cityCode').val())||(''==$('#cityCode').val())){
				targetId.html("<span style='color:#ff6600'>请先选择城市或输入正确的城市！</span>");
		        targetId.show().css({"width":"200px"});
			}else{
			    targetId.show().css({"width":"395px"});
				targetId.html("<img src='http://himg.mangocity.com/img/emap/ajax-loader.gif'/>")
				jQuery("#selectItemCount").html("");
				var cityName = jQuery("#id_startCity").val();
				cityName = encodeURIComponent(cityName);
				jQuery.ajax({ 
				  type: "post", 
				  url: "hotel_query_business.shtml", 
				  data: "cityName="+cityName,
				  success:function(response){
				  	if(jQuery.trim(response)==null||jQuery.trim(response)==""){
						targetId.html('非法的城市名或者该城市还没有添加数据！');
						return;	
					}
					targetId.find("#selectItemClose").click(function(){
						jQuery("#selectbiz").hide();
					});
				  	targetId.html(response);
				  	targetId.show().css({"position":"absolute","top":A_top+"px" ,"left":A_left+"px"});			
				  }
				});
		     }	 
		});
	    return this;
	}
	function hideItem(){
		document.getElementById("hoteladdress").value="";
		document.getElementById("bizCode").value="";
		document.getElementById("bizFlag").value="";
		jQuery("#selectbiz").hide();
	}
	
	function selectItemSeftVal(bizName,bizCode,bizFlag){
		document.getElementById("hoteladdress").value=bizName;
		//1为商区，2为行政区 badmethod
		if(bizFlag == "1" ){
		    document.getElementById("bizCode").value=bizCode;
		}
		if(bizFlag == "2"){
		   document.getElementById("distinctCode").value=bizCode;
		}
		jQuery("#selectbiz").hide();
	}

$(document).ready(function() {
   autoQueryHotelName();
   autoQueryHotelAddress();
   loadMemberCheckList();
   loadBottomBanner();
   loadSearchBanner();
   //商区
   /*
    $(document).click(function(event){
			if((event.target.id!=jQuery("#selectbiz").selector.substring(1))&&(event.target.id!="hoteladdress")){
			   jQuery("#selectbiz").hide();
			   var obj_hoteladdress = document.getElementById("hoteladdress");
			   if(obj_hoteladdress.value.trim()===""){
			       obj_hoteladdress.value="您可以输入酒店地址或位置模糊查询";
			       obj_hoteladdress.style.color="#777777";
			   }
			}   	
	     });
   $("#hoteladdress").selectCity("#selectbiz");*/   
      //查询点击后做验证
   $('#id_query').click(function(){
      formSubmit();
   })
    $('#map_query').click(function(){
      mapSubmit();
   })       
   
});
  
   // 预订浮动层
	function doFuDong() {
		var memberid = getCookieHistory("memberid");
		if(!memberid){
			/*var memeberUrl = jQuery('#memberPath').val().trim();
			var testmember = document.getElementById("testmember");
			var testmember_HK = document.getElementById("testmember_HK");
			if(testmember==null && testmember_HK!=null){
			    var a = '<iframe src="http://www.mangocity.com/mbrweb/login/loginDirectBook.action?from=directBook&projectCode=null" name="framename" width="100%" marginwidth="0" height="100%" marginheight="0" scrolling="No" frameborder="no" id="framename" border="0"></iframe>';
			    document.getElementById("testmember_HK").innerHTML = a;
			}else{
			    var a = '<iframe src="http://www.mangocity.com/mbrweb/login/loginDirectBook.action?from=directBook&projectCode=null" name="framename" width="100%" marginwidth="0" height="100%" marginheight="0" scrolling="No" frameborder="no" id="framename" border="0"></iframe>';
			    document.getElementById("testmember").innerHTML = a;
			}*/
			$("#id_fudong").modal({
				closeHTML : '<div class="guanbi"><a title="Close" class="closeMode" style="cursor:pointer;" target="_blank">关闭</a></div>',
				position : ["25%"],
				overlayId : 'contact-overlay',
				//onClose : $.modal.close(),
				closeClass : ('closeMode'),
				overlayClose: true
			});
		}
	}
	
	function afterDirectBook() {
	   window.location.replace(window.location);
	}
	
	function loadMemberCheckList(){
    try{
	   var memberid = getCookieHistory("memberid");
	   if(memberid){
		 $.ajax({ 
		    type: "post", 
		    url: "hotel-member.shtml", 
			dataType:'html',
		    data:"memberId="+memberid,
			success:function(response){
			   document.getElementById("memberCheckInList").innerHTML=response;
		    }
		});  
	  }
	
	}catch(e){
	}
	}
  
  
  	function linkGEOToHotelSearch(cityCode,geoCode,type,intInDate,intOutDate){
	   var checkInDate = new Date(+new Date()+24*3600000*intInDate);
	   var checkOutDate = new Date(+new Date()+24*3600000*intOutDate);
	     var ckInDay = checkInDate.getDate();
         var ckInMonth = checkInDate.getMonth()+1;//getMonth的值为0-11,故要加1
         var ckInYear = checkInDate.getFullYear();
         if(ckInMonth<10){
            ckInMonth = "0"+ckInMonth;
         } 
         if(ckInDay<10){
            ckInDay = "0"+ckInDay;
         }
	     var inDateStr = ckInYear+"-"+ckInMonth+"-"+ckInDay;
         
		 var ckOutDay = checkOutDate.getDate();
         var ckOutMonth = checkOutDate.getMonth()+1;//getMonth的值为0-11,故要加1
         var ckOutYear = checkOutDate.getFullYear();
          if(ckOutMonth<10){
            ckOutMonth = "0"+ckOutMonth;
         } 
         if(ckOutDay<10){
            ckOutDay = "0"+ckOutDay;
         }
	     var outDateStr = ckOutYear+"-"+ckOutMonth+"-"+ckOutDay;  		
	   var reallyUrl = "hotel-query.shtml?cityCode="+cityCode
	                   +"&inDate="+inDateStr+"&outDate="+outDateStr;
	   var geoUrl="";
	   if(type=="geo"){
	        geoUrl = "&geoId="+geoCode;
	   }else if(type=="biz"){
	        geoUrl = "&bizCode="+geoCode;
       }else if(type=="distinct"){
	        geoUrl = "&distinctCode="+geoCode;
	   }
	  reallyUrl +=geoUrl;
	  //window.loaction=reallUrl;
	  window.open(reallyUrl);
	
	}
	
	
	function switchCity(cityCode,obj){
		initTab();
		obj.className="on";
	 	$.ajax({ 
		    type: "post", 
		    url: "hotel-hotArea.shtml", 
			dataType:'html',
		    data:"cityCode="+cityCode,
			success:function(response){
			   document.getElementById("hothotelPanel").innerHTML=response;
			   changeDivColor();
		    }
		});  
	}
	function initTab(){
		var a=document.getElementById("hothotelNav").getElementsByTagName("A");
		for(var x=0;x<a.length;x++){   
			a[x].className="";
		}  
	}
function loadRightBanner(id){
	var bannerStr="";
	for(var i=0;i<bannerInfos1.length;i++){
		bannerStr=bannerStr+'<div class="gg"><a href="'+bannerInfos1[i][1]+'" target="_blank" name="'+bannerInfos1[i][2]+'"><img src="'+bannerInfos1[i][0]+'" alt="'+bannerInfos1[i][3]+'" width="'+bannerInfos1[i][4]+'" height="'+bannerInfos1[i][5]+'" border="0" /></a></div>';	
	}
	document.getElementById(id).innerHTML=bannerStr;
}
function loadSearchBanner(){
	var searchStr="";
	for(var i=0;i<bannerInfos0.length;i++){
		if(bannerInfos0[i][0]=="Y"){
			document.getElementById("searchBanner").style.display="";
			searchStr=searchStr+'<a href="'+bannerInfos0[i][2]+'" target="_blank" name="'+bannerInfos0[i][3]+'"><img src="'+bannerInfos0[i][1]+'" alt="'+bannerInfos0[i][4]+'" width="'+bannerInfos0[i][5]+'" height="'+bannerInfos0[i][6]+'" border="0" /></a>';
		}	
	}
	document.getElementById("searchBanner").innerHTML=searchStr;
}
function loadBottomBanner(){
var bannerStr="";
	for(var i=0;i<bannerInfos1.length;i++){
	   if(i==0||i==2){
	      bannerStr=bannerStr+'<div class="newBanner"><a href="'+bannerInfos1[i][1]+'" target="_blank" name="'+bannerInfos1[i][2]+'"><img src="'+bannerInfos1[i][0]+'" alt="'+bannerInfos1[i][3]+'" width="'+bannerInfos1[i][4]+'" height="'+bannerInfos1[i][5]+'" border="0" /></a></div>';	
		 }else if(i==4){
		  bannerStr=bannerStr+'<div class="newBanner nmar"><a href="'+bannerInfos1[i][1]+'" target="_blank" name="'+bannerInfos1[i][2]+'"><img src="'+bannerInfos1[i][0]+'" alt="'+bannerInfos1[i][3]+'" width="'+bannerInfos1[i][4]+'" height="'+bannerInfos1[i][5]+'" border="0" /></a></div>';	
		 }
	}
	$(".mg_newBanner").append(bannerStr);
}
jQuery.fn.alert.defaults.alertClass = 'validate';