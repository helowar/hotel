
function showMes(id,message){
	var obj = document.getElementById(id);
	 message=(message==null?obj.defaultValue:message);
	if(jQuery.trim(obj.value)==""){
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
	var dD = document.getElementById("mg_start_date").value;
	var eD = document.getElementById("mg_end_date").value;
	if(dD==null || dD=='' || dD == '入住日期'){
		jQuery("#mg_start_date").alert("请选择入住日期！");
		return false;
	}
	if(eD==null || eD=='' || eD == '离店日期'){
		jQuery("#mg_end_date").alert("请选择离店日期！");
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
		jQuery("#mg_start_date").alert("提示入住日期应早于离店日期");
		return;
	}
	//modify by alfred，入住晚数可以大于28天	--2011-6-22
	/*
	if(daysElapsed(date1,date2)>28){
		jQuery("#id_startDate").alert("入住晚数不能超过28天！");
		return;
	}
	*/
	var cityName = document.getElementById("mg_start_city").value;
	if(cityName==null || cityName=='' || cityName == '中文/拼音'){
		jQuery("#mg_start_city").alert("");
		return;
	}
	var cityCode = jQuery("#cityCode").val();
	if(cityCode==null || cityCode==''){
			jQuery("#mg_start_city").alert("");
			return;
		}

	//setTimeout(function(){document.queryForm.submit();},10);
	var geoName=jQuery.trim($('#geoName').val());
	var hotelName=jQuery.trim($('#hotelName').val());
	if(geoName == '您可以输入酒店地址查询'){
		geoName = '';
	}
	if(hotelName == '您可以输入酒店名称查询'){
		hotelName='';
	}
	url="hotel-baiduquery.shtml?cityCode="+cityCode+"&&inDate="+dD+"&&outDate="+eD+"&&geoName="+geoName+"&&hotelName="+hotelName+"&&projectCode=0020025";
	window.open(encodeURI(encodeURI(url)));
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
		  max:5,
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
		  max:5,
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
				var cityName = jQuery("#mg_start_city").val();
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

      //查询点击后做验证
   $('#id_query').click(function(){
      formSubmit();
   })
});
  



