// JavaScript Document
function GetObj(objName){
	if(document.getElementById){
		return eval('document.getElementById("' + objName + '")');
	}else if(document.layers){
		return eval("document.layers['" + objName +"']");
	}else{
		return eval('document.all.' + objName);
	}
}
function SetBtn(preFix, idx){
	
	if(GetObj(preFix+"_btn_"+idx).className=="on"){
		return;
	}
	for(var i=0;i<10;i++){
		if(GetObj(preFix+"_btn_"+i)) GetObj(preFix+"_btn_"+i).className = "off";
	}
	GetObj(preFix+"_btn_"+idx).className = "on";
	var arr_hotelId_commodityId = loadHotel(idx);
	var today = getStrFromDate(new Date());
	var tomorrow = getAfterDateStr(today,1);
	loadPrice(arr_hotelId_commodityId.hotelIds,arr_hotelId_commodityId.commodityIds,today,tomorrow);
}
//加载hotel,返回此页hotelId
function loadHotel(index){
	$("#hotel_list").html("");
	var hotelIds = "";
	var commodityIds = "";
	var template = $("#template").val();
	if(hotel_arr[index]==undefined) return {"hotelIds":"","commodityIds":""};
	for(var i=0;i<hotel_arr[index].commodity.length;i++){
		var item = hotel_arr[index].commodity[i];
		hotelIds += hotel_arr[index].commodity[i].hotelId+",";
		commodityIds += hotel_arr[index].commodity[i].commodityId+",";
		var html =  template.replace(/#hotelName#/g,item.hotelName);
		html = html.replace(/#index1#/g,index)
					.replace(/#index2#/g,i)
					.replace(/#img#/g,item.img)
					.replace(/#star#/g,item.star)
					.replace(/#address#/g,item.address)
					.replace(/#roomName#/g,item.roomName+"-"+item.commodityName)
					.replace(/#commodityId#/g,item.commodityId);
		if(parseInt(item.star)>2){
			html = html.replace("#imgDisplay#","");
		}else{
			html = html.replace("#imgDisplay#","none");
		}
		if(i%3!=0) html = html.replace("#marginleft#","margin-left:25px");
		if(i%3==2) html += "<div class='clear'></div>";
		$("#hotel_list").append(html);
	}
	//给酒店名加链接到详情
	$("span[class='hotelName']").each(function(i,o){
		$(o).click(function(){
			window.open("http://hotel.mangocity.com/hotel-infopa.shtml?hotelId="+hotel_arr[index].commodity[i].hotelId,"_blank");
		});
	});
	if(hotelIds.length>0)	hotelIds = hotelIds.substring(0, hotelIds.length-1);
	if(commodityIds.length>0)	commodityIds = commodityIds.substring(0, commodityIds.length-1);
	return {"hotelIds":hotelIds,"commodityIds":commodityIds};
}
//查两天的价格
function loadPrice(hotelIds,commodityIds,begin,end){
	if(hotelIds==''||hotelIds=='undefined') return;
	$.ajax({
		  type:"post",
		  url:"flight-hotel.shtml",
		  dataType:"json",
		  contentType:"application/x-www-form-urlencoded",
		  data:{hotelIds:hotelIds,commodityIds:commodityIds,beginDate:begin,endDate:end,queryType:"1"},
		  success:function(jsonData){
			if(jsonData.PriceList.length==0){
				if($("#errorMsg").length<1){
					  $("body").append("<input type='hidden' id='errorMsg' value="+jsonData.message+"/>")
				}else{
					$("#errorMsg").val(jsonData.message);
				}
				return;
			}
			for(var d=0;d<jsonData.PriceList.length;d++){
				$("span[name=currency_"+jsonData.PriceList[d].ratePlanId+"]").html(jsonData.PriceList[d].currency);
				var price = getPrice(jsonData.PriceList[d].salePrice);
				$("span[name=price_"+jsonData.PriceList[d].ratePlanId+"]").html(parseInt(price));
			}
		  },
		  error:function(XMLHttpRequest, textStatus, errorThrown){
			  if($("#errorMsg").length<1){
				  $("body").append("<input type='hidden' id='errorMsg' value="+"很抱歉获取价格出错，请稍候重试！"+"/>")
			  }else{
					$("#errorMsg").val("很抱歉获取价格出错，请稍候重试！");
				}
		  }
	});
}
function book(){
	var city_defalut_vl="中文/拼音";
	if($("#id_dpt").val()=='') $("#id_startCity").val(city_defalut_vl);
	if($("#id_arr").val()=='') $("#id_endCity").val(city_defalut_vl);
	
	if($("#id_startCity").val()==city_defalut_vl){
		$("#id_startCity").focus();
		return;
	}
	if( $("#id_endCity").val()==city_defalut_vl){
		$("#id_endCity").focus();
		return;
	}
	if($("#id_startDate").val()==''){
		$("#id_startDate").focus();
		return;
	}
	var index1 = $("#index1").val();
	var index2 = $("#index2").val();
	GetObj("queryParamVO.depCity").value = $("#id_dpt").val();
	GetObj("queryParamVO.depCityCn").value = $("#id_startCity").val();
	GetObj("queryParamVO.arrCity").value = $("#id_arr").val();
	GetObj("queryParamVO.arrCityCn").value = $("#id_endCity").val();
	GetObj("queryParamVO.depDate").value = $("#id_startDate").val();
	GetObj("queryParamVO.retDate").value = $("#id_backDate").val();
	GetObj("hotelOrderVO.hotelId").value = hotel_arr[index1].commodity[index2].hotelId;
	GetObj("hotelOrderVO.hotelName").value = hotel_arr[index1].commodity[index2].hotelName;
	GetObj("hotelOrderVO.hotelAddress").value = hotel_arr[index1].commodity[index2].address;
	GetObj("hotelOrderVO.ratePlanId").value = hotel_arr[index1].commodity[index2].commodityId;
	GetObj("hotelOrderVO.roomtypeId").value = hotel_arr[index1].commodity[index2].roomtypeId;
	GetObj("hotelOrderVO.roomName").value = hotel_arr[index1].commodity[index2].roomName;
	GetObj("hotelOrderVO.checkInDate").value = $("#id_startDate").val();
	GetObj("hotelOrderVO.checkOutDate").value = getAfterDateStr($("#id_startDate").val(),1);
	GetObj("hotelOrderVO.currency").value = hotel_arr[index1].commodity[index2].currency;
    
  
	//getBedTyeps
	var bedTypes = hotel_arr[index1].commodity[index2].bedTypes;
	$("#bedtype_div").html("&nbsp;");
	for(var a=0;a<bedTypes.split(",").length;a++){
		$("#bedtype_div").append('<input type="hidden" name="hotelOrderVO.bedTypeList['+a+'].name" id="hotelOrderVO.bedTypeList['+a+'].name" value="'+getBedTypeName(bedTypes.split(",")[a])+'"/>');
		$("#bedtype_div").append('<input type="hidden" name="hotelOrderVO.bedTypeList['+a+'].code" id="hotelOrderVO.bedTypeList['+a+'].code" value="'+bedTypes.split(",")[a]+'"/>');
	}
	//get pricelist for all eleven days arround
	var today = getStrFromDate(new Date());//4.1
	var selectDate = $("#id_startDate").val();
	var begin,end;
	if(compareDateStr(selectDate,getAfterDateStr(today,19)) <=0){
		begin = getAfterDateStr(today,19);
		end = getAfterDateStr(today,29);
	}else{
		begin = selectDate;
		end = getAfterDateStr(selectDate,10);
	}
	fillPriceList(hotel_arr[index1].commodity[index2].hotelId, hotel_arr[index1].commodity[index2].commodityId,selectDate,begin,end);
}

//获取价格列表
function fillPriceList(hotelIds,commodityIds,selectDate,begin,end){
	$.ajax({
		  type:"post",
		  url:"flight-hotel.shtml",
		  dataType:"json",
		  contentType:"application/x-www-form-urlencoded",
		  data:{hotelIds:hotelIds,commodityIds:commodityIds,beginDate:begin,endDate:end,queryType:"2"},
		  success:function(jsonData){
			  $("#priceinfo_div").html("&nbsp;");
			  if(jsonData.PriceList.length==0){
				  alert("此酒店在"+selectDate+"暂不可订，您可以选择其它酒店或其它日期！");
				  return;
			  }
			  var isSelectDateHasPrice = false;
			  for(var i=0;i<jsonData.PriceList.length;i++){
				  if(jsonData.PriceList[i].saleDate==selectDate){
					  GetObj("hotelOrderVO.totalAmount").value = getPrice(jsonData.PriceList[i].salePrice); 
                      GetObj("packedProductVO.totalPrice").value =getPrice(jsonData.PriceList[i].salePrice); 
					  isSelectDateHasPrice = true;
				  }
				  var price = getPrice(jsonData.PriceList[i].salePrice);
				  $("#priceinfo_div").append('<input type="hidden" name="hotelOrderVO.salePriceList['+i+'].saleDate" id="hotelOrderVO.salePriceList['+i+'].saleDate" value="'+jsonData.PriceList[i].saleDate+'"/>');
				  $("#priceinfo_div").append('<input type="hidden" name="hotelOrderVO.salePriceList['+i+'].salePrice" id="hotelOrderVO.salePriceList['+i+'].salePrice" value="'+parseInt(price)+'"/><br/>');
			  }
			  
			  //如果选择的日期没价格，提示出来
			  if(!isSelectDateHasPrice){
				  alert("此酒店在"+selectDate+"暂不可订，您可以选择其它酒店或其它日期！");
				  return;
			  }
			  document.flightform.submit();
		  },
		  error:function(XMLHttpRequest, textStatus, errorThrown){
			  alert("很抱歉获取价格出错，请稍候重试！\n");
		  }
	});
}
/******** date fn*******/
function getStrFromDate(dt){
	var t_month = (dt.getMonth()+1)<10 ? "0"+(dt.getMonth()+1) : (dt.getMonth()+1);
	var t_date = dt.getDate()<10 ? "0"+dt.getDate() : dt.getDate();
	return dt.getFullYear()+"-"+t_month+"-"+t_date;
}
function getDateFromStr(str){
	return new Date(str.replace(/-/g,"/"));
}
function compareDateStr(str1,str2){
	var d1 = getDateFromStr(str1);
	var d2 = getDateFromStr(str2);
	return (d2.getTime() - d1.getTime());
}
function getAfterDateStr(str,num){
	var dt = new Date(str.replace(/-/g,"/"));
	dt.setDate(dt.getDate()+num);
	return getStrFromDate(dt);
}
function getPrice(price){
	if(price==null) return 0;
	if(price.indexOf(".")>-1 && parseInt(price.substring(price.indexOf(".")+1))>0 ){
		price = parseInt(price)+1;
	}else{
		price = parseInt(price);
	}
	return price;
}