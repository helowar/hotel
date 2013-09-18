<%@ page contentType="text/html;charset=utf-8"%>
<div class="hn-boxmid">      
    <div class="hn-form">
         <form id="queryForm" name="queryForm" action="hotel-query.shtml" method="post">
               <ul>
                <li class="w189"><label class="input_label"><input id="id_startCity" name="cityName" type="text" class="w182 greytxt MGcity" value="选择城市" datatype="hotel" />
                      <input type="hidden" id="cityCode" name="cityCode" value="${hotelInfo.cityId}" />
                <span id="scitytip" class="hidetxt" style="display:none;">选择城市</span><a id="scityIcon" href="#" class="cityIcon"></a></label></li>
                <li class="w189"><label class="input_label"><input id="id_startDate" name="inDate" type="text" class="w182 required greytxt calendar" readonly="true" value="入住日期" />
                <span id="sdatetip" class="hidetxt" style="display:none;">入住</span><span class="holitip"></span><a  href="#" id="icon_startDate" name="icon_startDate" class="dateIcon"></a></label>
                </li>
                <li class="w189"><label class="input_label"><input id="id_backDate" name="outDate" type="text" class="w182 required greytxt calendar" readonly="true" value="离店日期" />
                <span id="edatetip" class="hidetxt" style="display:none;">离店</span><span class="holitip"></span><a href="#" id="icon_endDate" name="icon_endDate" class="dateIcon"></a></label>
                 </li>
                
                <li class="w189"><label class="input_label">
                 <input name="hotelName" id ="hotelName" type="text" value="您可以输入酒店名称查询" class="w182 greytxt"  onblur="hs_showMes(this.id);" onclick="hs_hiddenMes(this.id);" />
                </label></li>
                <li class="w189"><input type="button" class="btn92x26a" id="id_query" name="researchHotel" value="重新搜索" /></li>
              </ul>
          </form></div>
</div>
<link rel="stylesheet" type="text/css" href="css/hotelsearch/jquery.autocomplete.css" />
<script type="text/javascript" src="js/hotelsearchJS/jquery.autocomplete.js"></script>
<script type="text/javascript">
function hs_showMes(id,message){
	var obj = document.getElementById(id);
	 message=(message==null?obj.defaultValue:message);
	if(obj.value.trim()===""){
	    obj.value=message;
	    obj.style.color="#777777";
	}
}	
 
function hs_hiddenMes(id,message){
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
   autoQueryHotelName();   
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

$('#id_query').click(function(){
     formSubmit();
  });    

</script>
