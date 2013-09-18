
function Control(parentID, type, id, value, defaultValue, size) {
	this.parentID = parentID;
	this.id = id;
	this.value = value;
	this.name = id;
	this.parent = document.getElementById(parentID);
	this.type = type;
	this.defaultValue = defaultValue;
	if (!isValidObject(size)) {
		this.size = 0;
	} else {
		this.size = size;
	}
}
Control.prototype.append = function (child) {
	if (isValidObject(parent)) {
		this.parent.appendChild(child);
	}
};
/**
 * 创建选择器
 */
Control.prototype.createSelector = function (size) {
	var selector = document.createElement("SELECT");
	selector.setAttribute("id", this.name);
	selector.setAttribute("name", this.name);
	selector.size = size;
	this.append(selector);
	return selector;
};
/**
 * the behaviour in ie environment and other browser is differnet.

 * according to document.uniqueID, you can quickly know it is ie environment.
 */
Control.prototype.createRadiobox = function (text) {
	var radioButton = null;
	if (document.uniqueID) {
        //only ie can do in this way. Internet Explorer
		radioButton = document.createElement("<input type='radio'  name='" + this.name + "' id='" + this.name + "'value='" + this.value + "'>");
	} else {
        //Standards Compliant, this way could not function in ie.
		radioButton = this.create();
	}
	this.append(radioButton);
	return radioButton;
};

Control.prototype.createMultiCheckboxInTbody = function (map, isSelectAll) {
	var colNum = this.parent.rows[0].cells.length;
	var k = 0;
	var rowIndex = 0;
	
	var checkerList = new Array();
	for (var m = 0 ; m < map.length; m++) {
		var property = map[m];
		var modNum = (k) % colNum;
		if (modNum == 0 && m > 0) {
			copyRow(this.parentID);
			rowIndex = rowIndex + 1;
		}
		var row = this.parent.rows[rowIndex].cells;
		var control = new Control("", this.type, this.id, property.name);
		control.parent = row[modNum];
		
		var checker = control.createCheckbox(property.value);
		k = k + 1;
		checker.checked = contain(this.defaultValue, property.name);
		
		checkerList.push(checker);
	}
	if (isSelectAll == true) {
		var modNum = (k) % colNum;
		if (modNum == 0 && m > 0) {
			copyRow(this.parentID);
			rowIndex = rowIndex + 1;
		}
		var row = this.parent.rows[rowIndex].cells;
		var control = new Control("", this.type);
		control.parent = row[modNum];
		var checker = control.createCheckbox("\u5168\u9009");
		var name = this.name;
		checker.onclick = function () {
			common.selectAll(checker, name);
		};
	}
	
	return checkerList;
};


Control.prototype.createMultiCheckbox = function (map, isSelectAll) {
    
    if (this.parent.tagName == "TBODY")
       return this.createMultiCheckboxInTbody(map, isSelectAll);
    else
       return this.createMultiCheckboxInDIV(map, isSelectAll);
}


Control.prototype.createMultiCheckboxInDIV = function (map, isSelectAll) {
	var k = 0;
	var checkerList = new Array();
	for (var m = 0 ; m < map.length; m++) {
		var property = map[m];
		var control = new Control(this.parentID, this.type, this.id, property.name);
		var checker = control.createCheckbox(property.value+ "    ");
		checker.checked = contain(this.defaultValue, property.name);		
		
		checkerList.push(checker);
		
	}
	if (isSelectAll == true) {
		var control = new Control(this.parentID, this.type);
		var checker = control.createCheckbox("全选");
		var name = this.name;
		checker.onclick = function () {
			common.selectAll(checker, name);
		};
	}
	
	return checkerList;
};


/**
 * create a checkbox or radio from parent id;
 *@ type 1. "checkbox" 2."radio"
 */
Control.prototype.createCheckbox = function (text) {
	var checker = null;
	if (this.type.toUpperCase() == "RADIO") {
		checker = this.createRadiobox();
	} else {
		checker = this.create();
	}
	this.append(checker);
	var textObj = document.createTextNode(text);
	this.append(textObj);
	return checker;
};
/**
 * create a html element
 */
Control.prototype.create = function () {
	var element = null;
	element = document.createElement("INPUT");
	element.setAttribute("type", this.type);
	element.setAttribute("id", this.name);
	element.setAttribute("name", this.name);
	element.setAttribute("value", this.value);
	this.append(element);
	return element;
};

