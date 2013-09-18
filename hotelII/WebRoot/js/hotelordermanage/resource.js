
/**
 * 全局数组，目前没有作用
 */
g_resource = new Object();
function getResource(controlID) {
    return g_resource[controlID];
}
function XResource(controlID, defaultValue, resourceID, type, v_params) {
    this.controlID = controlID;
    this.resourceID = resourceID;
    this.setType(type);
    this.params = v_params;
    this.eventName = "";
    this.event = null;
    this.setDefaultValue(defaultValue);
    this.handleValue =function handleValue(data){//fuhoujun add 
        return data;
    };
	//currently , we don't need this function
	//g_resource[controlID] = this;
}
/**
 * 将传入的默认值转统一转换成数组形式
 */
XResource.prototype.setDefaultValue = function (defaultValue) {
    if (!isValidObject(defaultValue)) {
        return;
    }
    var obj = defaultValue;
    if (!(defaultValue instanceof Array)) {
        obj = defaultValue.split(",");
    }
    this.defaultValue = obj;
};
XResource.prototype.setEvent = function (eventName, event) {
    this.eventName = eventName;
    this.event = event;
};
XResource.prototype.setSize = function (size) {
    this.size = size;
};
XResource.prototype.setType = function (type) {
    if (isValidObject(type)) {
        this.type = type;
    } else {
        this.type = "";
    }
};
XResource.prototype.setMaxLength = function (maxLength) {
    this.maxLength = maxLength;
};
XResource.prototype.setSelectAll = function (selectAll) {
    if (isValidObject(selectAll)) {
        this.selectAll = selectAll;
    } else {
        this.selectAll = true;
    }
};
XResource.prototype.draw = function (parentID) {
    this.parentID = parentID;
    var obj = this;
    obj.parentID = parentID;
    var myControlID = this.controID;
    var handleValue = this.handleValue;
    var callbackProxy = function (dataFromServer) {
        return loadResource(handleValue(dataFromServer), obj);
    };
    var callMetaData = {callback:callbackProxy};
    resourceManager.getResourceByParam(this.resourceID, this.params, callMetaData);
};


function loadResource(data, resourceObj) {
    var controlID = resourceObj.controlID;
    var type = resourceObj.type.toUpperCase();
    var defaultValue = resourceObj.defaultValue;
    var obj = document.getElementById(controlID);
    if (!isValidObject(obj)) {
        if (!isValidObject(resourceObj.parentID)) {
            alert("control's parentID is not allowed to null:" + controlID);
        }
        var control = new Control(resourceObj.parentID, type, controlID, defaultValue, defaultValue, resourceObj.maxLength);
        if (type == "SELECT") {
            if (resourceObj.resourceID=="select_hotelStarLevel" && defaultValue=="-100"){
	            var property=new Object();      
	            property.name="";
	            property.value="\u8bf7\u9009\u62e9";	            
            	data.push(property);            
            } else if(isContainSelect(resourceObj.resourceID)){
	            var property=new Object();      
	            property.name="";
	            property.value="\u8bf7\u9009\u62e9";	            
            	data.push(property);
            }
            data=sortSelectHotel(data);        
            control.createSelector();
        } else {
            if (type == "CHECKBOX" || type == "RADIO") {
            	data = getLandCity(data);
                var controlList = control.createMultiCheckbox(data, resourceObj.selectAll);
				//set the control's event.
                if (isValidString(resourceObj.eventName)  && isValidObject(resourceObj.event)) {
                    for (var i = 0; i < controlList.length; i++) {
                        Event.observe(controlList[i], resourceObj.eventName, resourceObj.event);
                    }
                }
                return;
            } else {
                alert("invalid control type:" + type);
            }
        }
    } else {/** 对于下拉框，则直接输出到下拉框的选项当中，不必要创建控件 */
        obj = document.getElementById(controlID);        
        if (isValidObject(obj) && obj.tagName == "SELECT") {
            DWRUtil.removeAllOptions(controlID);   
            if (resourceObj.resourceID=="select_hotelStarLevel" && defaultValue=="-100"){
	            var property=new Object();      
	            property.name="";
	            property.value="\u8bf7\u9009\u62e9";	            
            	data.push(property);            
            } else if(isContainSelect(resourceObj.resourceID)){
	            var property=new Object();      
	            property.name="";
	            property.value="\u8bf7\u9009\u62e9";	            
            	data.push(property);
            } 
          	data=sortSelectHotel(data);        
            DWRUtil.addOptions(controlID, data, "name", "value");
            
            if (isValidString(resourceObj.eventName) && isValidObject(resourceObj.event)) {
                Event.observe(obj, resourceObj.eventName, resourceObj.event);
            }
            DWRUtil.setValue(controlID, defaultValue);
        }
    }
}

function getLandCity(obj){	
	var dataObj=new Array();
	var len = obj.length;
	for (var i=0; i<len;i++){
	    if(obj[i].name !="MAC" && obj[i].name!="HKG"){
	    	var property=new Object();
	    	property.name=obj[i].name;
	    	property.value=obj[i].value;
	    	dataObj.push(property);
	    }
	}
	obj=dataObj;
	return obj;
}

function sortSelectHotel(obj){	
	var tempSelectArray=new Array(obj.length);	
	for (var i=0; i<obj.length;i++){
	    tempSelectArray[i]=new Array(obj[i].name,obj[i].value);
	  }
	if(tempSelectArray.length >1){
		tempSelectArray.sort();	
		for (var i = 0; i < tempSelectArray.length; i++) {		
			obj[i].name=tempSelectArray[i][0];
			obj[i].value=tempSelectArray[i][1];
		}			
	}  
	return obj;
}

