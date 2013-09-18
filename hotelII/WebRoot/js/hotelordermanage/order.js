/**
 * 配送发票项目
 * added by chenkeming@2007.06.07 
 */
function setFulfillInvoiceType(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "res_invoiceType", defaultValue);
}

/**
 * 配送收费方式
 * added by chenkeming@2007.06.07 
 */
function setFulfillPayMethod(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "res_fulfillPayMethod", defaultValue);
}

/**
 * 配送任务类型
 * added by chenkeming@2007.06.07 
 */
function setFulfillTaskType(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "res_fulfillTaskType", defaultValue);
}

/**
 * 配送方式
 * added by chenkeming@2007.06.07 
 */
function setFulfillType(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "res_fulfillType", defaultValue);
}


/**
 * 订单类型
 * added by chenkeming@2007.06.07 
 */
function setOrderType(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "res_modelType", defaultValue);
}

/**
 * 选择信用卡月份
 * added by chenkeming@2007.06.20 
 */
function setCreditMonth(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "credit_month", defaultValue);
}

/**
 * 选择信用卡年份
 * added by chenkeming@2007.06.20 
 */
function setCreditYear(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "credit_year", defaultValue);
}

/**
 * 选择有效证件类型
 * added by chenkeming@2007.06.20 
 */
function setCertificateType(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "certificate_type", defaultValue);
}

/**
 * 选择信用卡种类
 * added by chenkeming@2007.06.20 
 */
function setCardType(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "creditCard", defaultValue);
}

/**
 * 选择中台订单组别
 * added by chenkeming@2007.06.20 
 */
function setHraOrderType(divID, paramName, defaultValue) {
    var resource = new XResource(paramName, defaultValue, "res_hraOrderType", "CHECKBOX");
    //resource.setSelectAll(true);
    resource.draw(divID);
}

/**
 * 选择退款原因
 * added by chenkeming@2007.06.20 
 */
function setRefundMessage(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "res_refundMessage", defaultValue);
}

/**
 * 选择转账银行
 * added by chenkeming@2007.06.20 
 */
function setBankCode(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "res_bankCode", defaultValue);
}

/**
 *设置省、市
 *可以不输入参数，但下拉框ID必须是以下的默认值
 */
function setCascadeProvCity(provinceID, cityID) {
    provinceID = isValidString(provinceID) ? provinceID : "select_province";
    cityID = isValidString(cityID) ? cityID : "select_city";
    var selector1 = new XSelector(provinceID, "queryBaseData",{type:"province"});
    selector1.header = "\u8bf7\u9009\u62e9\u7701";
    var selector2 = new XSelector(cityID, "queryBaseData",{type:"city"});
    selector2.header = "\u8bf7\u9009\u62e9\u57ce\u5e02";
    selector1.addChild(selector2);
    selector1.draw();
    return selector1;
}

/**
 * 填充城市、城区
 */
function setCascadeCityZone(cityValue, cityZoneValue) {
    var selector2 = new XSelector("select_city", "queryBaseData",{type:"city"});
    selector2.defaultValue=cityValue;
    selector2.header = "\u8bf7\u9009\u62e9\u57ce\u5e02";
    
    var selector3 = new XSelector("select_district", "queryBaseData", {type:"district",parent:cityValue});
    selector3.defaultValue=cityZoneValue;
    selector3.header = "\u8bf7\u9009\u62e9\u57ce\u533a";
    selector2.addChild(selector3);
    
    selector2.draw();
}

/**
 * 选择床型
 * added by chenkeming@2007.06.20 
 */
function setSelBedType(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "bedType", defaultValue);
}

/**
 * 获取酒店类别
 * added by chenkeming@2007.05.17
 * @divID 你设置的DIV的ID号或其它父控件ID
 * @paramName 控件名称
 */
function setRadioHotelType1(divID, paramName, defaultValue) {
    var resource = new XResource(paramName, defaultValue, "res_hotelType", "RADIO");
    resource.draw(divID);
}

/**
 * 撤单原因
 * added by chenkeming@2007.06.20 
 */
function setCancelReason(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "res_cancelReason", defaultValue);
}

/**
 * 撤单原因 -- 客人原因
 * added by zhangwei@2007.09.24 
 */
function setGuestCancelMessage(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "res_guestCancelMessage", defaultValue);
}

/**
 * 入住人国籍
 * added by chenkeming@2007.06.20 
 */
function setFellowNation(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "res_fellowNation", defaultValue);
}

/**
 * 日审状态
 * added by chenkeming@2007.06.20 
 */
function setAuditState(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "res_auditState", defaultValue);
}

/**
 * 订单状态
 * added by chenkeming@2007.06.20 
 */
function setOrderStatus(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "res_orderState", defaultValue);
}

/**
 * 订单(请求)来源
 * added by chenkeming@2007.06.07 
 */
function setSource(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "origin", defaultValue);
}

/**
 * 交通方式
 * added by chenkeming@2007.06.07 
 */
function setVehicleType(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "res_vehicleType", defaultValue);
}

/**
 * 付款方式(类型)
 * added by chenkeming@2007.05.27 
 */
function setPrepayType(selecorID, defaultValue, changeEvent) {
    var selector = setSelector(selecorID, "res_prepayType", defaultValue, changeEvent);
}

/**
 * 付款时限(分)
 * added by chenkeming@2007.05.27 
 */
function setMinutesBakType(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "res_minutesBak", defaultValue);
}

/**
 * 付款时限(小时)
 * added by chenkeming@2007.05.27 
 */
function setHoursBakType(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "res_hoursBak", defaultValue);
}

/**
 * 客人确认方式
 * added by chenkeming@2007.05.27 
 */
function setConfirmType(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "res_confirmType", defaultValue);
}
/**
 * 称谓
 * added by chenkeming@2007.05.27 
 */
function setAppellative(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "res_appellative", defaultValue);
}
/**
 * 紧急程度
 * added by chenkeming@2007.05.21 
 */
function setEmergencyLevel(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "emergency", defaultValue);
}
/**
 * 到达时间
 * added by chenkeming@2007.05.21 
 */
function setArrivalTime(selecorID, defaultValue) {
    var selector = setSelector(selecorID, "res_arrivalTime", defaultValue);
}
/**
 * 获取酒店预定人附加服务
 * added by chenkeming@2007.05.20
 * @divID 你设置的DIV的ID号或其它父控件ID
 * @paramName 控件名称

 */
/*
function setAddOnService(divID, paramName) {
    var resource = new XResource(paramName, ["0"], "res_addOnService", "CHECKBOX");
    resource.draw(divID);
}

function setAddOnService(divID, paramName){   
   var resource = new XResource(paramName, ["0"], "res_addOnService", "CHECKBOX");   
   resource.setSelectAll(true);
   resource.draw(divID);   
}
*/
function setAddOnService(divID, paramName, defaultValue) {
    var resource = new XResource(paramName, defaultValue, "res_addOnService", "CHECKBOX");
    resource.setSelectAll(true);
    resource.draw(divID);
}
/**
 * 获取酒店房间特殊要求
 * added by chenkeming@2007.05.17
 * @divID 你设置的DIV的ID号或其它父控件ID
 * @paramName 控件名称
 */
function setSpecialReq(divID, paramName, defaultValue) {
    var resource = new XResource(paramName, defaultValue, "res_specialReq", "CHECKBOX");
    resource.setSelectAll(true);
    resource.draw(divID);
}



/**
 * 获取常用城市,目前只用于酒店查询
 * added by chenkeming@2007.05.16
 * @divID 你设置的DIV的ID号或其它父控件ID
 * @paramName 控件名称
 */
 /*已经在hotel.js里设好，此处去掉 modify by zhineng.zhuang
function setCommonCity12(divID, paramName) {
    var resource = new XResource(paramName, [""], "res_commonCity", "RADIO");
    resource.setEvent("click", function (event) {
        var checkerObj = Event.element(event);
        if (!isValidObject(checkerObj)) {
            alert("\u975e\u6cd5\u7684\u57ce\u5e02\u5bf9\u8c61");
        }
        var cityCode = checkerObj.value;
        var cityName = isValidObject(checkerObj.nextSibling) ? checkerObj.nextSibling.nodeValue : "";
        
       // var cityID = "select_city";
       // document.getElementById("city_name").value=cityName;
 
        document.getElementById("queryHotelForWebBean.cityId").value = cityCode;
        document.getElementById("queryHotelForWebBean.cityName").value = cityName;
      //  }
        
       // var selector = getSelector(cityID);
      //  if (!isValidObject(selector)) {
      //      alert("\u627e\u4e0d\u5230\u4e0b\u62c9\u6846\u5bf9\u8c61!:" + cityID);
      //  }
        // alert("cityCode:" + cityCode + ",cityName:" + cityName);
		
		try
		{
			// if choose city again , then clear the province list ;
			//var provinceID="select_province" ;
			//var cityName1="cityName1" ;
			//document.getElementById(provinceID).value="" ;
			//document.getElementById(cityName1).value="" ;
		}
		catch(e){}        

        //DWRUtil.addOptions(cityID, [{id:cityCode, name:cityName}], "id", "name");
        //DWRUtil.setValue(cityID, cityCode);
        //var selector = getSelector(cityID);
        //selector.loadChild();
    });
    resource.draw(divID);
}
*/
/**
 * 订单状态
 */
function setOrderState(selecorID, defaultValue, event) {
    var selector = setSelector(selecorID, "requirement_state", defaultValue, event);
}


/**
*    设置工作状态组的选择checkBox
*/

function setWorkStates(divID, paramName, resource,defaultValue) {
    var resource = new XResource(paramName, defaultValue, resource, "CHECKBOX");
    resource.setSelectAll(true);
    resource.draw(divID);
}



/**
*    设置工作状态组的选择checkBox
*/
function setWorkStatesGroupService(divID, paramName, defaultValue) {
    var resource = new XResource(paramName, defaultValue, "res_workStatesGroup", "CHECKBOX");
    resource.setSelectAll(true);
    resource.draw(divID);
}



/**
*    表单提交后，将checkBox的选择值形成一个以逗号分隔的字符串，设置到paramNameHidden参数中，提交到ACTION ,如 “01,02”.
*/



  function setCheckBooks(paramNameId,paramNameHidden){	
      
         var obj = document.getElementsByName(paramNameId);			         
		var   groupHra="";
		for(var i=0;i<obj.length;i++) {           
			               
			if(obj[i].checked)  {
			     groupHra="'"+obj[i].value+"'"+","+groupHra; 
			               
			} 
		}
		document.getElementById(paramNameHidden).value=groupHra.substring(0,groupHra.length-1);  	
      
       document.getElementById(paramNameId).value=groupHra.substring(0,groupHra.length-1);  	
	
	}
	
	
	/**
*    根据参数值，设置reaido的选择。

 setRadioCheckedByValue(document.getElementsByName("type"),'${workStates.type}');

*/
	
	function setRadioCheckedByValue(elements, optionValue) {
    //alert(elements.length);
    	for (i = 0; i < elements.length; i++) {
        //alert("&" + elements[i].value + "&" + optionValue);
        	if (elements[i].value == optionValue) {
            	elements[i].checked = true;
        	}
    	}
	}
	
	/**
	 * 网站设置入住证件类型(yun.wu 2008-5-28)
	 */
	function setFellowNationality(selecorID, defaultValue) {
    	var selector = setSelector(selecorID, "res_fellowNationality", defaultValue);
	}
	
	/**
	 * 网站到达时间(yun.wu 2008-5-28)
	 */
	function setWebArrivalTime(selecorID, defaultValue) {
	    var selector = setSelector(selecorID, "res_webArrivalTime", defaultValue);
	}

     /**
	 * 网站预定房间数(yun.wu 2008-5-29)
	 */
	function setWebRoomBookNumber(selecorID, defaultValue) {
	    var selector = setSelector(selecorID, "res_roomBookNumber", defaultValue);
	}
	
	 /**
	 * 网站付款时间(yun.wu 2008-5-30)
	 */
	function setWebPayTime(selecorID, defaultValue) {
	    var selector = setSelector(selecorID, "res_webPayTime", defaultValue);
	}
	
	/**
	 * 网站营业部门地址城市(yun.wu 2008-5-30)
	 */
	function setSaleDepartmentCity(selecorID, defaultValue) {
	    var selector = setSelector(selecorID, "res_saleDepartmentCity", defaultValue);
	}
	
	/**
	 * 网站营业部门详细地址(yun.wu 2008-5-30)
	 */
	function setSaleDepartmentAddress(selecorID, defaultValue) {
	    var selector = setSelector(selecorID, "res_saleDepartmentAddress", defaultValue);
	}
