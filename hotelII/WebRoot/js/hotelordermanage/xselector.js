
var g_selectors = new Object();
function getSelector(selectorID) {
	return g_selectors[selectorID];
}
function XSelector(id, queryID, paramMap, defaultValue, header) {
	this.defaultValue = defaultValue;
	this.id = id;
	this.queryID = queryID;
	this.size = 0;
	if (isValidObject(paramMap)) {
		this.paramMap = paramMap;
	} else {
		this.paramMap = new Object();
	}
	this.selectObj = document.getElementById(id);
	if (!isValidObject(this.selectObj)) {
		alert("\u975e\u6cd5\u7684\u4e0b\u62c9\u6846ID");
	}
	this.header = "\u8bf7\u9009\u62e9";
	this.parentCode = "";
	this.children = new Array();
	g_selectors[id] = this;
}
XSelector.prototype.addChild = function (child) {
	this.children.push(child);
};
XSelector.prototype.setParentCode = function (parentCode) {
	this.paramMap["parent"] = parentCode;
};
XSelector.prototype.setSize = function (size) {
	this.size = size;
};
XSelector.prototype.initial = function (header) {
	v_header = header;
};
XSelector.prototype.setDefaultValues = function (defaultValue) {
	v_defaultValue = defaultValue;
};
/**
     * 将数据展现在指定的ID的区域内
     */
XSelector.prototype.draw = function () {
	this.load();
};
/**
	  * 根据父类ID和父类代码加载子类
	  */
XSelector.prototype.load = function () {
	if (this.selectObj.onchange == null && this.hasChild()) {
		var localSelector = this;
		this.selectObj.onchange = function () {
			changeSelector(localSelector);
		};
	}
	this.clear();
	var mySelector = this;
	var callbackProxy = function (dataFromServer) {
		return myPopulate(dataFromServer, mySelector);
	};
	var callMetaData = {callback:callbackProxy};
	resourceManager.getResourceByParam(this.queryID, this.paramMap, callMetaData);
};
function changeSelector(selector) {
	selector.loadChild();
}
/**
     * populate data into selector
	 */
XSelector.prototype.populate = function (data) {       
	data=sortSelect(data);
       //填充下列列表框
	DWRUtil.addOptions(this.id, data, "name", "value");
	DWRUtil.setValue(this.id, this.defaultValue);
	this.loadChild();
};
XSelector.prototype.loadChild = function () {
	if (!this.hasChild()) {
		return;
	}
	var selectObj = this.selectObj;
	var index = selectObj.options.selectedIndex;
	if (index < 0) {
		return;
	}
	var selectValue = selectObj.options[index].value;
	if (!isValidString(selectValue)) {
		this.clearChild();
	} else {
		for (var m = 0; m < this.children.length; m++) {
			var child = this.children[m];
			if (isValidObject(child)) {
				child.setParentCode(selectValue);
				child.load();
			}
		}
	}
};
XSelector.prototype.clearChild = function () {
	for (var m = 0; m < this.children.length; m++) {
		var child = this.children[m];
		if (isValidObject(child)) {
			child.clear();
		}
	}
};
XSelector.prototype.initSelector = function () {
	DWRUtil.addOptions(this.selectObj, [{id:"", name:this.header}], "id", "name");
};
/**
    * 重置下拉框
	*/
XSelector.prototype.clear = function () {
	DWRUtil.removeAllOptions(this.id);
	this.initSelector();
	this.clearChild();
};
XSelector.prototype.hasChild = function () {
	return this.children.length > 0;
};
function myPopulate(data, selector) {
	if (selector.populate) {
		selector.populate(data);
	}
}

function sortSelect(obj){		
	var tempSelectArray=new Array(obj.length);	
	for (var i=0; i<obj.length;i++){
	    tempSelectArray[i]=new Array(obj[i].value,obj[i].name);
	  }
	if(tempSelectArray.length >1){
		tempSelectArray.sort();	
		for (var i = 0; i < tempSelectArray.length; i++) {
			obj[i].name=tempSelectArray[i][1];
			obj[i].value=tempSelectArray[i][0];
		}			
	}  
	return obj;
}
