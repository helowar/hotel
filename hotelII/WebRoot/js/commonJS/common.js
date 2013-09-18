
/**
 * 函数的命名空间
 */
function common() {
}
/**
 * 返回选择的CHECKBOX数据
 * 返回的是一个对象，属性ID表示：是选择的ID用，号组成的串，属性name表示是text用，号组成的串
 * 调用方法是common.selectCheckbox()，必须要带common的命名空间
 */
common.selectCheckbox = function (paramName) {
    var checkerObjs = document.getElementsByName(paramName);
    var deviceNameStr = "";
    var deviceIDs = "";
    for (var m = 0; m < checkerObjs.length; m++) {
        var checkerObj = checkerObjs[m];
        if (!isValidObject(checkerObj)) {
            alert("\u975e\u6cd5\u7684CHECKBOX\u5bf9\u8c61");
        }
        if (checkerObj.checked != true) {
            continue;
        }
        var deviceID = checkerObj.value;
        deviceIDs = deviceIDs + deviceID + ",";
        var deviceName = isValidObject(checkerObj.nextSibling) ? checkerObj.nextSibling.nodeValue : "";
        deviceNameStr = deviceNameStr + deviceName + ",";
    }
    return {id:deviceIDs, name:deviceNameStr};
};
/**
 * 全选与全不选函数
 * obj 为全选CHECKBOX的对象，用于函数知道是全选或是全不选
 */
common.selectAll = function (obj, name) {
    if (!isValidString(name)) {
        alert("\u5168\u9009\u7684CHECKBOX\u4e0d\u80fd\u4e3a\u7a7a\uff01");
    }
    var objs = document.getElementsByName(name);
    for (var m = 0; m < objs.length; m++) {
        if (objs[m].type.toLowerCase() == "checkbox") {
            objs[m].checked = obj.checked;
        }
    }
};
/**
 * switch visible 
 * if obj is visisble then hidden
 * else display;
 */
common.switchVisisble = function (id) {
    var obj = $(id);
    if (isValidObject(obj)) {
        if (obj.style.display == "none") {
            obj.style.display = "block";
        } else {
            obj.style.display = "none";
        }
    } else {
        alert("invalid object:" + id);
    }
};

/**
 * 打开 窗口
 */
common.openWindow = function(url, width, height) {
    window.open(url, "aa", "scrollbars=0,width=" + width + ",height=" + height + ",top=50, left=50,toolbar=no, menubar=no, scrollbars=yes,  resizable=yes, location=no, status=yes");
}

/**
 *设置对象可见
 *@param id 控件对象ID
 *@param visible 对象是否见，boolean
 */
common.setVisisble = function (id, visible) {
    var obj = $(id);
    if (isValidObject(obj)) {
        obj.style.display = visible ? "block" : "none";
    } else {
        alert("invalid object:" + id);
    }
};
function openWindow(url) {
    openWindow(url, 520, 400);
}
function openWindow(url, width, height) {
    window.open(url, "aa", "scrollbars=0,width=" + width + ",height=" + height + ",top=50, left=50,toolbar=no, menubar=no, scrollbars=yes,  resizable=yes, location=no, status=yes");
}
function clearSelection(selectorID) {
    var selector = document.getElementById(selectorID);
    selector.options.length = 0;
    //for(var m = 0; m < myOptions.length; m++){
    //myOptions[m] = null;
    //}
}
/**remove space from a string*/
String.prototype.trim = function () {
    return this.replace(/(^\s*)|(\s*$)/g, "");
};

//String.prototype.len=function(){return this.replace([^x00-xff]/g,"aa").length;}
/** return a random num between start and end*/
function rnd(start, end) {
    return (Math.random() * (end - start)) + start;
}
function setCheck(name, defaultValues) {
    var checkers = document.getElementsByName(name);
    for (var m = 0; m < checkers.length; m++) {
        var checker = checkers[m];
        checker.checked = contain(defaultValues, checker.value);
    }
}
function isValidString(value) {
    if (value == "undefined" || value == null || value.length <= 0) {
        return false;
    }
    return true;
}
function isValidObject(value) {
    if (value == "undefined" || value == null) {
        return false;
    }
    return true;
}
function setCheckByString(name, value) {
    var defaultValues = [""];
    if (isValidString(value)) {
        defaultValues = value.split(";");
    }
    setCheck(name, defaultValues);
}
function setCheckValue(collections, name) {
    var obj = document.getElementById(name);
    if (isValidObject(obj)) {
        obj.checked = getBoolean(collections[name]);
    }
}
function getBoolean(value) {
    if (value == "true") {
        return true;
    }
    return false;
}
function contain(collections, value) {
    if (collections == "undefined" || collections == null) {
        return false;
    }
    if (collections == value) {
        return true;
    }
    for (var item in collections) {
        if (collections[item] == value) {
            return true;
        }
    }
    return false;
}
function getValues(formID) {
    var obj = document.getElementById(formID);
    if (obj == null) {
        alert("please assure form id is not null");
        return;
    }
    var entity = new Object();
    for (var i = 0; i < obj.length; i++) {
        var field = obj[i];
        var isCheck = field.getAttribute("checked");
        if (field.type == "radio" || field.type == "checkbox") {
            if (isCheck == true) {
                entity[field.name] = field.value;
            }
        } else {
            if (field.type != "button" && field.type != "submit") {
                entity[field.name] = field.value;
            }
        }
    }
    return entity;
}
function getDefaultValues(value, controlNum) {
    if (!isValidObject(value)) {
        alert("invalid object" + value);
    }
    var defaultValues = new Array();
    for (var m = 1; m <= controlNum; m++) {
        var len = m * 3;
        if (len <= value.length) {
            defaultValues.push(value.substring(0, m * 3));
        }
    }
    return defaultValues;
}
function split(value, separ) {
    if (!isValidString(value)) {
        return new Array();
    }
    return value.split(separ);
}
function getCheckValues(name) {
    var checkers = document.getElementsByName(name);
    var info = "";
    for (var m = 0; m < checkers.length; m++) {
        var checker = checkers[m];
        if (checker.checked == true && checker.type == "checkbox") {
            info = checker.value + ";" + info;
        }
    }
    return info;
}
function debug(prefix, arrays) {
    alert(prefix + ": " + arrays);
}
/**
 * abstract a integer from a string
 */
function myParseInt(strDay) {
    var len = strDay.length;
    for (var m = 0; m < len; m++) {
        if (strDay.charAt(m) != "0") {
            var value = strDay.substring(m, len);
            return parseInt(value);
        }
    }
    return -1;
}
/*
function formatDate(value) {
    if (!isValidString(value) || value.length < 8) {
        alert("invalid date string" + value);
        return "";
    }
    //var week = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
    var month = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    var reg = "[A-Z a-z]{3} [A-Z a-z]{3} [0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2} [A-Z]{3} [1-3][0-9]{3}";
    if (value.match(reg)) {
        var strMonth = value.substring(4, 7);
        var intMonth = getArrayIndex(month, strMonth) + 1;
        strMonth = intMonth > 9 ? ("" + intMonth) : ("0" + intMonth);
        if (intMonth > 0 && intMonth < 13) {
            var strDay = value.substring(8, 10);
            var intDay = myParseInt(strDay);
            if (intDay > 0 && intDay < 32) {
                var year = value.substring(value.length - 4, value.length);
                return year + "-" + strMonth + "-" + strDay;
            }
        }
    } else {
        if (value.length >= 10) {
            return value.substring(0, 10);
        }
    }
    return "";
}*/
function getArrayIndex(arrays, value) {
    for (var m = 0; m < arrays.length; m++) {
        if (arrays[m] == value) {
            return m;
        }
    }
    return -1;
}
/**
 * is object visible
 */
function isVisible(obj) {
    var visAtt, disAtt;
    try {
        disAtt = obj.style.display;
        visAtt = obj.style.visibility;
    }
    catch (e) {
    }
    if (disAtt == "none" || visAtt == "hidden") {
        return false;
    }
    return true;
}
function isDigit(s) {
    var patrn = /^[0-9]{1,20}$/;
    if (!patrn.exec(s)) {
        return false;
    }
    return true;
}
function isArray(data) {
    return (data && data.join) ? true : false;
}
function isSelect(multiSelectBox) {
    var obj = document.getElementsByName(multiSelectBox);

    //    for (var m in obj) {
    //        alert(obj[m]);
    //        if (isValidString(obj[m].value))
    //            return true;
    //    }
    return true;
}
/**
 * 调用后台的XML验证
 * submit after v
 */
function xSubmit(formID) {
    if (doValidate(formID)) {
        document.getElementById(formID).submit();
    }
}
function option_Selected(SelectObj,OptionValue){
	var j=0;
	for (var i=0; i<SelectObj.length;i++){
		if (SelectObj.options[i].value==OptionValue){
			j=i;
			break;
		}
	}
	SelectObj.selectedIndex = j;
}
function radio_Checked(RadioCheckObj,CheckValue){
	var j=0;
	for (var i=0; i<RadioCheckObj.length;i++){
		if (RadioCheckObj[i].value==CheckValue){
			j=i;
			break;
		}
	}
	RadioCheckObj[j].checked=true;
}
function convertHotelType(from,to){
	var hotelType="";
    var ss="1";
    for(var i=0;i<from.length;i++){                  
	    if(from[i].checked){
		    hotelType+="%"+from[i].value; 
		    ss='2';
	    } 
  	}
    if (ss=='2'){
		hotelType=hotelType+"%";
    }	
  	to.value=hotelType; 
}
function isContainSelect(resourceID){
	var result=false;
	switch (resourceID)
	{
		case "emergency"://订单紧急程度
			result=true;
			break;
		case "origin"://订单来源
			result=true;
			break;				
		case "res_appellative"://称谓
			result=true;
			break;		
		case "select_commend"://酒店主推类型
			result=true;
			break;
		case "presale_type"://促销类型
			result=true;
			break;
		case "select_commentStatus"://评论状态
			result=true;
			break;
		case "select_commentScore"://会员总体评论分数
			result=true;
			break;
		case "select_roomStatus"://房态
			result=true;
			break;
		case "select_dateType"://合同奖惩分期类型
			result=true;
			break;
		case "select_saleType"://合同奖惩销售类型
			result=true;
			break;
		case "select_doneType"://合同奖惩完成类型
			result=true;
			break;
		case "select_RoomTypeCommendLevel"://房型推荐级别
			result=true;
			break;
		case "hotel_classify"://酒店房型类别 供房型信息使用
			result=true;
			break;
		case "res_prepayType"://付款方式(类型)
			result=true;
			break;
		case "res_vehicleType"://交通方式
			result=true;
			break;												
		case "res_assureType"://担保类型
			result=true;
			break;						
		case "res_orderState"://订单状态
			result=true;
			break;						
		case "select_dept"://部门中文名设置
			result=true;
			break;						
		case "sel_fitmentDegree"://酒店装修程度
			result=true;
			break;						
		case "res_deduct_type"://扣款类型
			result=true;
			break;						
		case "res_balance_method"://信用卡结算方法
			result=true;
			break;						
		case "hotel_status"://酒店变价工单状态
			result=true;
			break;						
		case "close_room_cause"://关房原因
			result=true;
			break;	
		case "select_hotelGroup"://集团酒店
			result=true;
			break;		
		case "queryRoomTypes"://房型
		  	result=true;
			break;	
		case "res_webHotelStar"://网站酒店星级
		  	result=true;
			break;						
		default :
			result=false;
	}
	return result;
}
/**
 * add by kun.chen 2007-9-4
 * 比较两个日期大小,因为日期都是用控件选择的,所以不必判断日期格式是否正确
 */
function CompareDate(date1,date2)
    {
	  
        _date1 = date1.split('-');
        _date2 = date2.split('-');

        var d1 = new Date(_date1[0],_date1[1],_date1[2]);
        var d2 = new Date(_date2[0],_date2[1],_date2[2]);
        if (d1.getTime()<d2.getTime()) {
          return "LESS";
        }else if(d1.getTime()> d2.getTime()){
          return "GREATER";
        }else{
          return "EQUAL";
        }
    }

    
function IsStringNull(str) {
	if(str==null)
		return true;
	var trimStr=trim(str);
	if(trimStr.length==0)
		return true;
	return false;
}


function webCheck(formName){
	 
	  for(i = 0; i < formName.length; i++){
	  
	  	 var ob = formName.elements(i);
	  	 
       if(ob.empty == 'false'){
           debug(ob.value);
            debug(ob.disabled);
          if(ob.value==""&& ob.disabled == false){
            var alertValue = '[' + ob.propStr + ']' + '²»ÄÜÎª¿Õ';
            alert(alertValue); 
             try{
              ob.focus();
             }catch(Exception){
             
             }
            return true;
           }         
       }
      
       if(ob.value!=undefined){
       
       if(!len(ob.value,ob.len)){
       	   var alertValue = '[' + ob.propStr + ']' + '³¬¹ý×î´ó³¤¶È';
            alert(alertValue); 
            ob.focus();
            return true;
       }
      }
       
       if(ob.isNum == 'true'){
       	  var temp = ob.value;
       	  if(temp!=""&&isNaN(temp)){
       	  	var alertValue = '[' + ob.propStr + ']' + '±ØÐëÊäÈëÊý×Ö';
            alert(alertValue); 
            ob.focus();
            return true;
       	  }
       }  
        if(ob.date == 'true'){
          if(!checkCommonIsValidDate(ob.value)){
              var alertValue = '[' + ob.propStr + ']' + '±ØÐëÊÇºÏ·¨µÄÈÕÆÚYYYY/MM/DD';
              alert(alertValue);
              ob.focus();
              return true;
          }
       	  convertDate(ob);
       } 
       if(ob.isPhone == 'true'){
       	  var temp = ob.value;
       	  if(!is_phone(temp)){
       	  	var alertValue = '[' + ob.propStr + ']' + '±ØÐëÊäÈëºÏ·¨µç»°';
            alert(alertValue); 
            ob.focus();
            return true;
       	  }
       }
      
      //ÅÐ¶Ï¿ªÊ¼Ê±¼äÊÇ·ñÐ¡ÓÚÊ±¼ä
      if(ob.begin == 'true'){
       	  var begintime = ob.value;
       	  var endtime = getEndTime(formName);
       	  if(!checkDateTime(begintime,endtime)){
       	  	var alertValue = '¿ªÊ¼Ê±¼ä±ØÐëÐ¡ÓÚ½áÊøÊ±¼ä';
            alert(alertValue); 
            ob.focus();
            return true;
       	  }
       }
       
    
      if(ob.areaempty == 'false'){
       	  var temp = ob.value;
       	  if(!area(ob.value)){
       	  	var alertValue = '[' + ob.propStr + ']' + '²»ÄÜÎª¿Õ';
            alert(alertValue); 
            ob.focus();
            return true;
       	  }
       }
       if(ob.isInteger == 'true'){
       	  var temp = ob.value;
       	  if(!is_integer(temp)){
       	  	var alertValue = '[' + ob.propStr + ']' + '±ØÐëÊäÈëÕûÊý';
            alert(alertValue); 
            ob.focus();
            return true;
       	  }
       }
       
       if(ob.isIdCard == 'true'){
       	  var temp = ob.value;
       	  if(!is_id_card(temp)){
       	  	var alertValue = '[' + ob.propStr + ']' + '¸ñÊ½²»¶Ô£¬Ó¦Îª15Î»»òÕß18Î»£¬15Î»È«Êý×Ö£¬18Î»µÄ×îºóÒ»Î»¿ÉÄÜÊÇ×ÖÄ¸£¡';
            alert(alertValue); 
            ob.focus();
            return true;
       	  }
       }
       
       
       if(ob.isEMail == 'true'){
       	  var temp = ob.value;
       	  if(!is_email(temp)){
       	  	var alertValue = '[' + ob.propStr + ']' + '¸ñÊ½²»¶Ô£¬Ó¦ÎªXXX@XXX.XXX';
            alert(alertValue); 
            ob.focus();
            return true;
       	  }
       }
       
       
       if(ob.isZip == 'true'){
       	  var temp = ob.value;
       	  if(!is_zip(temp)){
       	  	var alertValue = '[' + ob.propStr + ']' + '¸ñÊ½²»¶Ô';
            alert(alertValue); 
            ob.focus();
            return true;
       	  }
       }
       
    }
    return false;
	
 }
 
 function debug(message){
   if(false){
     alert(message);
   }
 }
 
 //¿ªÊ¼Ê±¼äÐ¡ÓÚ½áÊøÊ±¼ä,¿ªÊ¼»òÕß½áÊøÊ±¼äÎª¿ÕµÄÊ±ºò·µ»Øtrue
 //¿ªÊ¼Ê±¼ä´óÓÚ½áÊøÊ±¼ä·µ»Øfalse
 function checkDateTime(beginDate,endDate) {

   if(trim(beginDate)==""||trim(endDate)=="")

   {
      return true;

   }

    var beginTime =beginDate.split("/");

   var start = new
   Date(beginTime[0],beginTime[1],beginTime[2],0,0,0,0);

   var endTime=endDate.split("/");

   var end = new
   Date(endTime[0],endTime[1],endTime[2],0,0,0,0);


   if (start.getTime() <
     end.getTime()||start.getTime()==end.getTime()) {
     return true;

   }else{
     return false;

   }

}
 
 //µÃµ½½áÊøÊ±¼ä
  function getEndTime(formName) {
    for(k = 0; k < formName.length; k++){
	  	 var ob = formName.elements(k);
	  	 if(ob.end=='true'){
	  	   return ob.value;
	  	 }
	}
	return "";
 }
 
 
  //µç»°Ð£Ñé
  function is_phone(js_value) {
   var re = /^[0-9\*\-( )]*$/;
   if(js_value.match(re))
   return true;
   return false;
 }
  
  function delSubmit(id){	
      if(confirm("ÊÇ·ñÈ·¶¨É¾³ý!")){	
		document.getElementById("delId").value = id;		
		
		delForm.submit();
	  }
	}
	
	function editSubmit(id){		
		document.getElementById("editId").value = id;		
		editForm.submit();
	}	
	

	
	function operSubmitSession(id,actionValue,formName){	
		//alert('sdfsd');	
		document.getElementById("operId").value = id;		
		//alert('sdfsd');	
		formName.action.value=actionValue;
		checkDate();
		//alert('sdfsd');	
		formName.submit();
	}	
	
	//ÅÐ¶Ï³¤¶È
	function len(val,asklength){	
	  var valLength = DataLength(val);
	  
	  if(valLength==null){
	    return true;
	  }else if(valLength>parseInt(asklength)){
	    return false;
	  }
	  return true;
	  
	}	
	//Ð£ÑéÇøÓò
	function area(value){
	    if(value == "" ||value <= 0){
	         return false;
	     }
	     return true;
	     
	}
	//Ð£ÑéÕûÊý
	function is_integer(js_value) {
   var re;
   re = /^\s*$/;
   if(js_value.match(re)) {
      return true;
   }
   if(isNaN(js_value) || js_value.indexOf('.', 0) >= 0) {
      return false;
   }
   return true;
   }
   
   //Ð£ÑéÉí·ÝÖ¤
   function is_id_card(js_value) {
   var re;
   re = /^\s*$/;
   reg1=/\d{17}\w{1}/;
   reg2=/\d{15}/;
   if(js_value.match(re)) {
      return true;
   }
   if(js_value.match(reg1)&&js_value.length==18) {
      return true;
   }
   
   if(js_value.match(reg2)&&js_value.length==15) {
      return true;
   }
   return false;
   }
   
   //Ð£Ñée-mail
   function is_email(js_value) {
   var pos;
   var re;
   re = /^\s*$/;
   if(js_value.match(re)) {
      return true;
   }
   pos = js_value.indexOf('@', 0);
   if(js_value.length <= 5)
   return false;
   if(pos == - 1 || pos == 0 || pos ==(js_value.length - 1))
   return false;
   pos = js_value.indexOf('.', 0);
   if(pos <= 0 || pos ==(js_value.length - 1))
   return false;
   return true;
  }
  
  //Ð£ÑéÓÊ±à
  function is_zip(js_value) {
   var re;
   re = /^\s*$/;
   if(js_value.match(re)) {
      return true;
   }
   if(!is_natural(js_value) || js_value.length != 6) {
      return false;
   }
   return true;
 }
 
 function is_natural(js_value) {
   var re;
   re = /^\s*$/;
   if(js_value.match(re)) {
      return true;
   }
   re = /^\+{0,1}[0-9]*$/;
   if(!js_value.match(re))
   return false;
   return true;
 }
	
	  //****************************************************************
//* Ãû¡¡¡¡³Æ£ºDataLength
//* ¹¦    ÄÜ£º¼ÆËãÊý¾ÝµÄ³¤¶È
//* Èë¿Ú²ÎÊý£ºfData£ºÐèÒª¼ÆËãµÄÊý¾Ý
//* ³ö¿Ú²ÎÊý£º·µ»ØfDataµÄ³¤¶È(Unicode³¤¶ÈÎª2£¬·ÇUnicode³¤¶ÈÎª1)
//*****************************************************************
function DataLength(fData)
{
    var intLength=0
    for (var i=0;i<fData.length;i++)
    {
        if ((fData.charCodeAt(i) < 0) || (fData.charCodeAt(i) > 255))
            intLength=intLength+4
        else
            intLength=intLength+1    
    }
    return intLength
}

	function formSubmit(formName){	 
			//alert('1111');
	  if(webCheck(formName)){
	    return false;
	  }
	  checkDate();
		//alert(formName.action.value);
		formName.submit();
	}
	
	//´øÌáÊ¾ÐÅÏ¢µÄÌá½»£¬µ±confirmInfoÎªtrueµÄÊ±ºòÏÈÌáÊ¾ÔÙÌá½»
	function confirmSubmit(formName,confirmInfo){	 
			//alert('1111');
		if(confirmInfo){
		  if(confirm("ÊÇ·ñÈ·ÈÏÌá½»£¡")){
		    if(webCheck(formName)){
	        return false;
	      }
	      checkDate();
		    formName.submit();
		  }
		}else{
	 
		    if(webCheck(formName)){
	        return false;
	      }
	      checkDate();
		    formName.submit();

	  }
	
	}
	
	
	function checkDate(){ 
	 	
	  for(i = 0; i < document.all.length; i++){	
	  	 var ob = document.all(i);  	
      if(ob.date == 'true'){
       	  convertDate(ob);
       }       
    }
	}
	
function convertDate(elementValue){	 
	
}

	//È¥×ó¿Õ¸ñ; 
	function ltrim(s){ 
	 return s.replace( /^\s*/, ""); 
	} 
	//È¥ÓÒ¿Õ¸ñ; 
	function rtrim(s){ 
	 return s.replace( /\s*$/, ""); 
	} 
	//È¥×óÓÒ¿Õ¸ñ; 
	function trim(s){ 
	 return rtrim(ltrim(s)); 
	} 
	
	//½«Êý×Ö×ª»»Îªºº×Ö
	function transNum(num)
   {  
	//×ª»»ÕûÊý²¿·Ö
	var i=1;
	var len = num.length;

	var dw2 = new Array("","Íò","ÒÚ");//´óµ¥Î»
	var dw1 = new Array("Ê®","°Ù","Ç§");//Ð¡µ¥Î»
	var dw = new Array("Áã","Ò»","¶þ","Èý","ËÄ","Îå","Áù","Æß","°Ë","¾Å");//ÕûÊý²¿·ÖÓÃ
	var k1=0;//¼ÆÐ¡µ¥Î»
	var k2=0;//¼Æ´óµ¥Î»
	var str="";

	for(i=1;i<=len;i++)
	{
	var n = num.charAt(len-i);
	if(n=="0")
	{
	if(k1!=0)
	str = str.substr( 1, str.length-1);
	}

	str = dw[Number(n)].concat(str);//¼ÓÊý×Ö

	if(len-i-1>=0)//ÔÚÊý×Ö·¶Î§ÄÚ
	{
	if(k1!=3)//¼ÓÐ¡µ¥Î»
	{
	str = dw1[k1].concat(str);
	k1++;
	}
	else//²»¼ÓÐ¡µ¥Î»£¬¼Ó´óµ¥Î»
	{
	k1=0;
	var temp = str.charAt(0);
	if(temp=="Íò" || temp=="ÒÚ")//Èô´óµ¥Î»Ç°Ã»ÓÐÊý×ÖÔòÉáÈ¥´óµ¥Î»
	str = str.substr( 1, str.length-1);
	str = dw2[k2].concat(str);
	}
	}
	if(k1==3)//Ð¡µ¥Î»µ½Ç§Ôò´óµ¥Î»½øÒ»
	{
	k2++;
	}
	 }
	 return str;
	}

//×Ö·û´®ÐÍÊý×Ö×ª»»Îª×Ö·û´®Ê±¼ä(HH:dd)
	function convertStrNumberToStrTime(strNumber){
		if(strNumber.search(/^(-|\+)?\d+(\.\d+)?$/) != -1){
			for(var i=0;i<4;i++){
				if(strNumber.length<4){
					strNumber='0'+strNumber;
				}
			}
			if(strNumber.substring(0,1)=="0"){
				return strNumber.substring(1,2)+":"+strNumber.substring(2,4);
			}
			else{
				return strNumber.substring(0,2)+":"+strNumber.substring(2,4);
			}
		}
		else{
			return strNumber;
		}
	}

	//ÅÐ¶ÏÊ±¼ä¸ñÊ½£¨HH:dd||H:dd£©ÊÇ·ñºÏ·¨²¢×ª»»Îª×Ö·û´®ÐÍÊý×Ö
	//¸ñÊ½´íÎó·µ»Ø'ErrorFormat'
	function convertStrTimeToStrNumber(strtime){
	     if(strtime.length !=0 && strtime!=""){
			  	var regexp = /^([0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/
			  	if(regexp.test(strtime)== false){
			  	     return "ErrorFormat";
			  	}	
			  	var array = strtime.split(":");
			  	var ar1 = array[0];
			  	var ar2 = array[1];
			  	if(ar1 == 0){
			  		if(ar2 == 00){
			  			return "0";
			  		}else if(ar2.indexOf('0')== 0 ){
			  			return ar2.substring(1);
			  		}else{
			  			return ar2;
			  		}
			  	}else{
			  	  	return ar1+ar2;
			  	}

		}else{
		  	return "0";
		}	
     }
     
     
function checkCommonIsValidDate(str){
    //Èç¹ûÎª¿Õ£¬ÔòÍ¨¹ýÐ£Ñé
    if(str == "")
        return true;
    var pattern = /^\d{4}\/\d{1,2}\/\d{1,2}$/g;
    if(!pattern.test(str))
        return false;
    //alert("¡¾" +str+"¡¿1");
    var arrDate = str.split("/");
    var date =  new Date(arrDate[0],(parseInt(arrDate[1],10) -1) +"",parseInt(arrDate[2],10) +"");
    //alert("a:¡¾" +date.getFullYear()+"¡¿¡¾" + date.getMonth() + "¡¿¡¾" + date.getDate() + "¡¿");
    //alert("b:¡¾" +arrDate[0]+"¡¿¡¾" + parseInt(arrDate[1],10) + "¡¿¡¾" + parseInt(arrDate[2],10) + "¡¿");
    if(date.getFullYear() == arrDate[0]
       && date.getMonth() == (parseInt(arrDate[1],10)-1) +""
       && date.getDate() == parseInt(arrDate[2],10) +"")
        return true;
    else
    	//alert("¡¾" +str+"¡¿2");
        return false;
}

/*****************************************
	ÉèÖÃformÖÐÃû³ÆÎªcheckBoxNameµÄcheckBoxµÄcheckedÊôÐÔ
******************************************/
function setChecked(form,checkBoxName,isChecked){
	for (var i=0;i<form.elements.length;i++)  {
		var e = form.elements[i];
		if (e.name == checkBoxName && e.type == "checkbox"){
		    e.checked = isChecked;       			
		}
	}
}

function myConfirm(urlStr){
  if(confirm('ÊÇ·ñÈ·¶¨É¾³ý!')){
    window.location=urlStr;
  }
}

/*****************************************
	//¸ù¾ÝvalueÑ¡ÔñselectÑ¡Ïî
******************************************/	
	function selectOptionByValue(SelectForm,optionValue)
	{
		for(i=0;i<SelectForm.length;i++)
		{
			if(SelectForm.options[i].value==optionValue)
			{
				SelectForm.options[i].selected=true;
			}
		}
	}

/*****************************************
	//±È½ÏÁ½¸ödateµÄ¼ä¸ôÌìÊý
******************************************/	
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

/**
 * 用另外一种方式生成js的Date对象，兼容firefox
 */
function getJsDate(date){
	var s1 = date.split("-"); 
	var dateVar = new Date(s1[0],s1[1]-1,s1[2],0,0,0); 
	return dateVar;
}




