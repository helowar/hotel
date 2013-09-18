 //幻灯广告banner初始化
 function initAdBanner(){
 	cleartagli("cycleul");
	cleartagli("cycleList");
 	var size = hotel_index_Ad.length;
 	var strcycleul = "";
 	var strcycleList = "";
 	for(var i = 0 ;i<size;i++){
 	  strcycleul = strcycleul +"<li><div class='imgWrap'><a  target='_brank' href='"+hotel_index_Ad[i][1]+"'><img src='"+hotel_index_Ad[i][0]+"' alt='"+hotel_index_Ad[i][2]+"' /></a></div></li>";
 	  if(i==0){
 	   	  strcycleList = strcycleList+"<li class='on'></li>";
 	  }else{
 	     strcycleList = strcycleList+"<li></li>";
 	  } 	
 	}
 	$("#cycleul").append(strcycleul);
 	$("#cycleList").append(strcycleList);
 }
 
//初始化搜索框默认城市函数
 function initDefaultScity(){
  	var default_cityName = document.getElementById("default_cityName").value;
   	var default_cityCode = document.getElementById("default_cityCode").value;
   	var cookieStr = getCookie("cachecity");
   	if(cookieStr !== "" && cookieStr !== null){
   		var list = cookieStr.split("|");
   		$("#start_city").attr("value",list[0]);
   		$("#hide_city").attr("value",list[1]);
   		$("#mod_city").attr("value",list[0]);
   		$("#hide_mod").attr("value",list[1]);
   		$("#start_city").removeClass("inputSel");
   		return;
   	}
   	
   	if(default_cityCode !=="" && default_cityCode !== null && default_cityName !==null){
   		$("#start_city").attr("value",default_cityName);
   		$("#hide_city").attr("value",default_cityCode);
   		$("#mod_city").attr("value",default_cityName);
   		$("#hide_mod").attr("value",default_cityCode);
   		$("#start_city").removeClass("inputSel");
   		return;
   	}
   	
   	$("#start_city").attr("value",default_cityName);
   	$("#hide_city").attr("value","");
   	$("#mod_city").attr("value","北京");
   	$("#hide_mod").attr("value","PEK");
  }


//初始化地标处默认城市函数
function initDefaultGcity(){
 	//进入页面地标初始化，默认北京
	var mod_cityCode = document.getElementById("hide_mod").value;
	if(mod_cityCode==null || mod_cityCode==''){
		mod_cityCode="PEK";
	}
	gitswitchCity(mod_cityCode,setAreagitMsg);
	/* 地标周边酒店弹出层 */	
	$(".mod li").click(function(){
		var idx = $(this).index()+1;
		$(this).children().toggleClass("on");
		$(this).siblings().children().removeClass('on');
		
		$(".catemod i").removeClass().addClass("action0"+idx);					
		var li_len = $(this).children('.on');
		if(li_len[0]){

			//地标信息展示
			selectMgisShow(idx)
		}else{
			$(".catemod").hide();	
		}
	});	
 }

//初始化芒果网时间控件的调用
function initflowdatetool() {

	$("#start_time").attr("value",GetDateStr(1)+"  明天");
	$("#end_time").attr("value",GetDateStr(2)+"  后天");

    var doc = window.document;
    function $id(id) {
        return doc.getElementById(id);
    }
    var start = new Datepicker($id('start_time'), {
        eachW: 206,
        ttMonth: 2,
        addDays: 60,
        callback: callnext
    }); //首次点击开始日期只能选择60天
    var firstVal0 = $id('start_time').value;
    var firstVal1 = firstVal0.split(" ");
    var firstVal = firstVal1[0];
    //console.log(firstVal);
    var today = new Date(firstVal.split('-')[0], firstVal.split('-')[1] - 1, firstVal.split('-')[2]); //去开始日期时间作为结束日期的起始时间
    //console.log(today);
    var firststart = addDay(1, today).split('-'); //默认结束日期从入住日期后一天开始
    //console.log(firststart);
    //var firststart = addDay(1, new Date()).split('-'); //默认结束日期从明天开始
    var days = Math.ceil((today - new Date()) / 86400000); //获取开始日期距离今天的时间差
    var firstend = addDay(60, new Date()).split('-'); //默认开始日期的endDate
    //console.log(firststart);
    var end = new Datepicker($id('end_time'), {
        eachW: 206,
        ttMonth: 2,
        startDate: [firststart[0], (firststart[1] - 1), firststart[2]],
        addDays: parseInt(60 + days),
        endDate: firstend,
        callback: callhotel
    });
    function callhotel(o, t) {
        o.value = t.title;
        var sd = $id('start_time');
        blurProcessor("end_time");
        getalldays(sd, o);
    }
    function getalldays(s, e) {
        //var eo = $id('id_startDate');
        if (!$id('daysOfStay')) {
            return false;
        }
        var edd = new Date(e.value.split('-')[0], e.value.split('-')[1] - 1, e.value.split('-')[2]);
        var sdd = new Date(s.value.split('-')[0], s.value.split('-')[1] - 1, s.value.split('-')[2]);
        var days = Math.ceil((edd - sdd) / 86400000);
        if (days < 0) {
            days = 0;
        }
        $id('daysOfStay').value = days;
    }
    function callnext(o, t) {
        o.value = t.title;
        var eo = $id('end_time');
       
        var sdd = new Date(o.value.split('-')[0], o.value.split('-')[1] - 1, o.value.split('-')[2]);
        var edd = new Date(eo.value.split('-')[0], eo.value.split('-')[1] - 1, eo.value.split('-')[2]);
        //if(sdd > edd){
        eo.value = addDay(1, sdd);
        //eo.value = addDay(7, sdd);
        //}
        getalldays(o, eo);
        var days = Math.ceil((sdd - new Date()) / 86400000);
        //console.log(days);
        var setend = addDay(60, new Date()).split('-');
        //console.log(parseInt(28+days));
        //console.log(o.value);
        var _e_ = new Date(o.value.split('-')[0], (o.value.split('-')[1] - 1), o.value.split('-')[2]);
        var _f_ = addDay(1, _e_).split('-');
        end = new Datepicker(eo, {
            eachW: 206,
            ttMonth: 2,
            startDate: [_f_[0], (_f_[1] - 1), _f_[2]],
            addDays: parseInt(28 + days),
            endDate: setend,
            callback: callhotel
        });
       
        blurProcessor("start_time");
        window.setTimeout(function() {
            eo.focus();
            blurProcessor("end_time");
        },
        0);
    }
    function addDay(num, date) {
        var now = date ? new Date(date.getFullYear(), date.getMonth(), date.getDate()) : new Date();
        var olddate = now.getDate();
        now.setDate(now.getDate() + num);
        return now.getFullYear() + '-' + ((now.getMonth() + 1) < 10 ? "0" + (now.getMonth() + 1) : (now.getMonth() + 1)) + '-' + (now.getDate() < 10 ? "0" + now.getDate() : now.getDate());
    }
    function getbyClass(parent, tag, name) {
        parent = parent || document;
        tag = tag || "*";
        var arr = [];
        var reg = new RegExp("(^|\\s)" + name + "(\\s|$)");
        var ts = parent.getElementsByTagName(tag);
        for (var i = 0,
        j = ts.length; i < j; i++) {
            if (reg.test(ts[i].className)) {
                arr.push(ts[i]);
            }
        }
        return arr;
    }
    var inputs = getbyClass(document, 'input', 'h_datepikcer');
    //console.log(inputs);
    if (inputs && inputs.length) {
        var iobj = eobj = {};
        for (var i = 0,
        len = inputs.length; i < len; i++) {
            var p = i; (function() {
                var iid = inputs[p].id;
                //console.log(iid);
                if (iid && iid.indexOf('start_time') != -1) {
                    var num = iid.slice(12);
                    //console.log(num);
                    //if($id('backDate_'+num)){
                    var _stval = $id(iid).value.split('-');
                    var _s_ = addDay(1, new Date(_stval[0], (_stval[1] - 1), _stval[2])).split('-');
                    //console.log(_s_);
                    //var _setstart = [_stval[0], (_stval[1]-1), _stval[2]];
                    var _setstart = [_s_[0], (_s_[1] - 1), _s_[2]];
                    var _days = new Date(_stval[0], _stval[1] - 1, _stval[2]);
                    var _addday = Math.ceil((_days - new Date()) / 86400000);
                    var _setend = addDay(60, new Date()).split('-'); //$id(iid).value.split('-');
                    //console.log(_setstart);
                    eobj[num] = new Datepicker($id('end_time' + num), {
                        eachW: 206,
                        ttMonth: 2,
                        startDate: _setstart,
                        addDays: parseInt(28 + _addday),
                        endDate: _setend,
                        callback: callhotel
                    });
                    function dynamiccall(o, t) {
                        o.value = t.title;
                        var eo = $id('end_time' + num);
                        if (!eo) {
                            return false;
                        }
                        var _sdd = new Date(o.value.split('-')[0], o.value.split('-')[1] - 1, o.value.split('-')[2]);
                        //var edd = new Date(eo.value.split('-')[0], eo.value.split('-')[1]-1, eo.value.split('-')[2]);
                        //if(sdd > edd){
                        eo.value = addDay(1, _sdd);
                        //eo.value = addDay(7, sdd);
                        //}
                        var days = Math.ceil((_sdd - new Date()) / 86400000);
                        //console.log(parseInt(7+days));
                        //setend = $id(iid).value.split('-');
                        //console.log(setend);
                        var e_ = new Date(o.value.split('-')[0], (o.value.split('-')[1] - 1), o.value.split('-')[2]);
                        var f_ = addDay(1, e_).split('-');
                        eobj[num] = new Datepicker(eo, {
                            eachW: 206,
                            ttMonth: 2,
                            startDate: [f_[0], (f_[1] - 1), f_[2]],
                            addDays: parseInt(28 + days),
                            endDate: _setend,
                            callback: callhotel
                        });
                        window.setTimeout(function() {
                            eo.focus();
                        },
                        0);
                    }
                    iobj[num] = new Datepicker($id(iid), {
                        eachW: 206,
                        ttMonth: 2,
                        addDays: 60,
                        callback: dynamiccall
                    });
                    //}
                }
            })();
        }
    }

}

 
//初始化城市弹出浮动框
function initflowcitytool(){

	
	/*小城市JS调用**/
	var mod_city = new MGTOOL.citySelecter({
		target : $('#mod_city')[0],
		hideInput : $('#hide_mod')[0],
		datatype : 'hotel',
		hotDatas : commoncitysHotel,
		cityDatas : citysForHotel,
		group : ['ABCDEFG','HIJKLM','NOPQRST','UVWXYZ'], 
		wrapid : 'mangocityBox'
	});
	//输入框获取焦点时添加的自定义事件，这里的e对象为 {type:"onFirst", parentBox:控件外框对象, hotlist:热门数据, datalist:所有城市列表数据}
	mod_city.onFocus.addFns('onFirst', function(e){
		mod_city.addHotCommon(e.parentBox, e.hotlist, e.datalist); 
	});
	//点击热门面板时添加的自定义事件，这里e对象为{type:'hotCommonClick', target:输入框对象, hideinput:隐藏表单域对象, node:点击对象}
	mod_city.onClick.addFns('hotCommonClick', function(e){ 
		mod_city.setCommonValue(e);
		e.target.style.color = '#333';
		var citycode = document.getElementById("hide_mod").value;
		
		var cityname = document.getElementById("mod_city").value;
		$("#hide_city").attr("value",citycode);
		$("#start_city").attr("value",cityname);
		
		$("#mod_cityCode").attr("value",citycode);
		//如果dosomething之后关闭弹出框则执行以下操作
		$(".catemod").hide();
		$(".mod li").siblings().children().removeClass('on')
		//选择城市，地标信息更新
		gitswitchCity(citycode,setAreagitMsg);
		//$('#id_endCity').focus();
		//removeWarn(flight_city.target, 'warniw124');		
	});
	//输入框发生变化时（也就是输入）添加的自定义事件，这里的e对象为{type:"inputChange", wrap:控件外框对象, str:每次输入的字符, hotDatas:热门数据, cityDatas:所有城市列表数据}
	mod_city.onKey.addFns('inputChange', function(e){ 
		mod_city.commenInputChange(e);
	});
	//点击下拉列表时添加的自定义事件，这里的e对象为{type:"keyClick", target:输入框对象, hideinput:隐藏表单域对象, node:点击对象}
	mod_city.onKey.addFns('keyClick', function(e){ 
		mod_city.setKeyClick(e);
		e.target.style.color = '#333';
		//$('#id_endCity').focus();
		//removeWarn(flight_city.target, 'warniw124');
	});
	//下拉列表触发回车时添加的自定义事件，这里的e对象为{type:"keyEnter", target:输入框对象, hideinput:隐藏表单域对象, node:键盘选中对象 }
	mod_city.onKey.addFns('keyEnter', function(e){ 
		mod_city.keyEnter(e);
		e.target.style.color = '#333';
		//$('#id_endCity').focus();
		//removeWarn(flight_city.target, 'warniw124');
	});		

	/* 搜索框城市空间调用*/
	var flight_city = new MGTOOL.citySelecter({
		target : $('#start_city')[0],
		hideInput : $('#hide_city')[0],
		datatype : 'hotel',
		hotDatas : commoncitysHotel,
		cityDatas : citysForHotel,
		group : ['ABCDEFG','HIJKLM','NOPQRST','UVWXYZ'], 
		wrapid : 'mangocityBox'
	});
	//输入框获取焦点时添加的自定义事件，这里的e对象为 {type:"onFirst", parentBox:控件外框对象, hotlist:热门数据, datalist:所有城市列表数据}
	flight_city.onFocus.addFns('onFirst', function(e){
		flight_city.addHotCommon(e.parentBox, e.hotlist, e.datalist); 
	});
	//点击热门面板时添加的自定义事件，这里e对象为{type:'hotCommonClick', target:输入框对象, hideinput:隐藏表单域对象, node:点击对象}
	flight_city.onClick.addFns('hotCommonClick', function(e){ 
		flight_city.setCommonValue(e);
		
		
		var citycode = document.getElementById("hide_city").value;
		var cityname = document.getElementById("start_city").value;
		$("#mod_cityCode").attr("value",citycode);
		$("#mod_city").attr("value",cityname);
		//如果dosomething之后关闭弹出框则执行以下操作
		$(".catemod").hide();
		$(".mod li").siblings().children().removeClass('on')
		$("#start_city").removeClass("inputSel");
		//选择城市，地标信息更新
		gitswitchCity(citycode,setAreagitMsg);
		//$('#id_endCity').focus();
		//removeWarn(flight_city.target, 'warniw124');		
	});
	//输入框发生变化时（也就是输入）添加的自定义事件，这里的e对象为{type:"inputChange", wrap:控件外框对象, str:每次输入的字符, hotDatas:热门数据, cityDatas:所有城市列表数据}
	flight_city.onKey.addFns('inputChange', function(e){ 
		flight_city.commenInputChange(e);
		$("#start_city").removeClass("inputSel");
	});
	//点击下拉列表时添加的自定义事件，这里的e对象为{type:"keyClick", target:输入框对象, hideinput:隐藏表单域对象, node:点击对象}
	flight_city.onKey.addFns('keyClick', function(e){ 
		flight_city.setKeyClick(e);
		e.target.style.color = '#333';
		//$('#id_endCity').focus();
		//removeWarn(flight_city.target, 'warniw124');
	});
	//下拉列表触发回车时添加的自定义事件，这里的e对象为{type:"keyEnter", target:输入框对象, hideinput:隐藏表单域对象, node:键盘选中对象 }
	flight_city.onKey.addFns('keyEnter', function(e){ 
		flight_city.keyEnter(e);
		e.target.style.color = '#333';
		//$('#id_endCity').focus();
		//removeWarn(flight_city.target, 'warniw124');
		
	});	

 }
 
// 失去焦点处理器 add by hushunqiang
function blurProcessor(inputName) {
	var inputtemp = inputName;
	var inputTempx = "#"+inputName;
	var inputValue = $(inputTempx).val();
	
	if(inputValue == "NaN-NaN-NaN"){
		inputValue="";
	}
	switch (inputtemp) {
		case 'start_city':
			if(inputValue==""){
				$("#start_city").attr("value","请选择城市");
				$("#start_city").addClass("inputSel");
			}
			if(inputValue=="请选择城市"){
				$("#start_city").addClass("inputSel");
			}
			if(inputValue=="中文/拼音"){
				$("#start_city").addClass("inputSel");
			}
			break;
		case 'start_time':
			if(inputValue==""){
				$("#start_time").attr("value",GetDateStr(1)+" 明天");
			
				break;
			}
			var instrlist = inputValue.split(" "); 
			var inputValue = instrlist[0];	
			var sview = dateViewProcessor(inputValue);
			$(inputTempx).attr("value",inputValue+" "+sview);
			break;
		case 'end_time':
			if(inputValue==""){
				var st = $("#start_time").val();
				var ddd = st.substr(0,10);
				var nowdate = new Date();
				
				var instrlist = ddd.split(" "); 
				var datestri = instrlist[0].split("-");	
				var tttt = new Date(datestri[0],datestri[1],datestri[2]);
				nowdate.setDate(tttt.getDate()+1);
				var y = nowdate.getFullYear();
    			var m = (nowdate.getMonth()+1) < 10 ? ("0" + (nowdate.getMonth() + 1)) : (nowdate.getMonth() + 1);
   		 		var d = nowdate.getDate()<10?("0" + (nowdate.getDate())) : (nowdate.getDate());
    			var now = y+"-"+m+"-"+d;
    			var sview = dateViewProcessor(now);
				$("#end_time").attr("value",now+" "+sview);
				
				break;
			}
			var instrlist = inputValue.split(" "); 
			var inputValue = instrlist[0];	
			var eview = dateViewProcessor(inputValue);
			$(inputTempx).attr("value",inputValue+" "+eview);
			//$(inputTempx).css("color","#000");
			break;
	
		case 'hotelName':
			if(inputValue==""){
				$("#hotelName").attr("value","酒店名称/品牌/地区");
				$("#hotelName").addClass("inputSel");
			}
			break;
		default:
			return;
	}

}


// 获取焦点处理器 add by hushunqiang
function onfocusProcessor(inputName) {

	var inputtemp = inputName;
	var inputTempx = "#"+inputName;
	var inputValue = $(inputTempx).val();

	if(inputValue=="请选择城市" && inputName == "start_city" ){
		$("#start_city").attr("value","");
	}
	if(inputValue=="中文/拼音" && inputName == "start_city"){
		$("#start_city").attr("value","");
	}
	
	if(inputValue=='酒店名称/品牌/地区' && inputName=="hotelName"){
		$("#hotelName").attr("value","");
	}

}

//日期显示处理器，时间为今天加上“今天”,时间为明天，加上明天-----
function dateViewProcessor(inputValue) {
	var result ="";
	
	var list= inputValue.split("-");
	var selectdate= new Date(list[0],list[1]-1,list[2]);
	var nowdate= new Date();
	var y = nowdate.getFullYear();
    var m = (nowdate.getMonth()+1) < 10 ? ("0" + (nowdate.getMonth() + 1)) : (nowdate.getMonth() + 1);
    var d = nowdate.getDate()<10?("0" + (nowdate.getDate())) : (nowdate.getDate());
    var now = y+"-"+m+"-"+d;
    
    var tomrrow = GetDateStr(1);
    var houtian = GetDateStr(2);
	var num = 0;
	
	if(inputValue==now){
		result= "今天";
	}else if(inputValue == tomrrow){
		result= "明天";
	}else if(inputValue == houtian){
		result= "后天";
	}else{
			num=selectdate.getDay();
			if(num == 0){
				result= "星期天";
			}
			
			if(num == 1){
				result= "星期一";
			}
			
			if(num == 2){
				result= "星期二";
			}
			
			if(num == 3){
				result= "星期三";
			}
			
			if(num==4){
				result= "星期四";
			}
			
			if(num == 5){
				result= "星期五";
			}
			
			if(num == 6){
				result= "星期六";
			}
		}
		return result;
} 

var georesultMap;
var brand;
var scenic;
var subway;
var district;
var train;
var university;
var airdrome;
var business;
var hospital;
var MgisCode;
//初始化相应城市的地标信息
function setAreagitMsg(data){
	if (data!==null) {
		georesultMap = data.resultMap;
		MgisCode = data.cityCode;
		if (georesultMap!==null) {
			var i=0;
			$.each(georesultMap, function(key, value){
				i++;

				if(key=="scenic"){
					scenic = value;
					if(scenic==""){
						$("#scenic-img").removeClass("mod_5");
						$("#scenic-img").addClass("mod_5_not_allowed");
						$("#scenic-txt").css("cursor","not-allowed");
						$("#scenic-txt").css("color","#666666");
					}else{
						$("#scenic-img").removeClass("mod_5_not_allowed");
						$("#scenic-img").addClass("mod_5");
						$("#scenic-txt").css("cursor","pointer");
						$("#scenic-txt").css("color","");
					}
				}
				
				if(key=="subway"){
					subway = value;
					if(subway==""){
						$("#subway-img").removeClass("mod_2");
						$("#subway-img").addClass("mod_2_not_allowed");
						$("#subway-txt").css("cursor","not-allowed");
						$("#subway-txt").css("color","#666666");
					}else{
						$("#subway-img").removeClass("mod_2_not_allowed");
						$("#subway-img").addClass("mod_2");
						$("#subway-txt").css("cursor","pointer");
						$("#subway-txt").css("color","");
					}
				}
				
				if(key=="district"){
					district = value;
					if(district==""){
			
						$("#district-img").removeClass("mod_3");
						$("#district-img").addClass("mod_3_not_allowed");
						$("#district-txt").css("cursor","not-allowed");
						$("#district-txt").css("color","#666666");
					}else{
						$("#district-img").removeClass("mod_3_not_allowed");
						$("#district-img").addClass("mod_3");
						$("#district-txt").css("cursor","pointer");
						$("#district-txt").css("color","");
					}
				}
				
				if(key=="train"){
					train = value;
				}
				
				if(key=="airdrome"){
					airdrome = value;
				}
				
				if(train=="" && airdrome==""){
					$("#airtrain-img").removeClass("mod_1");
					$("#airtrain-img").addClass("mod_1_not_allowed");
					$("#airtrain-txt").css("cursor","not-allowed");
					$("#airtrain-txt").css("color","#666666");
				}else{
						$("#airtrain-img").removeClass("mod_1_not_allowed");
						$("#airtrain-img").addClass("mod_1");
						$("#airtrain-txt").css("cursor","pointer");
						$("#airtrain-txt").css("color","");
					}
				
				if(key=="university"){
					university = value;
					if(university==""){
						$("#university-img").removeClass("mod_7");
						$("#university-img").addClass("mod_7_not_allowed");
						$("#university-txt").css("cursor","not-allowed");
						$("#university-txt").css("color","#666666");
					}else{
						$("#university-img").removeClass("mod_7_not_allowed");
						$("#university-img").addClass("mod_7");
						$("#university-txt").css("cursor","pointer");
						$("#university-txt").css("color","");
					}
				}

				if(key=="business"){
					business = value;
					if(business==""){
						$("#business-img").removeClass("mod_4");
						$("#business-img").addClass("mod_4_not_allowed");
						$("#business-txt").css("cursor","not-allowed");
						$("#business-txt").css("color","#666666");
					}else{
						$("#business-img").removeClass("mod_4_not_allowed");
						$("#business-img").addClass("mod_4");
						$("#business-txt").css("cursor","pointer");
						$("#business-txt").css("color","");
					}
				}
				
				if(key=="hospital"){
					hospital = value;
					if(hospital==""){
						$("#hospital-img").removeClass("mod_6");
						$("#hospital-img").addClass("mod_6_not_allowed");
						$("#hospital-txt").css("cursor","not-allowed");
						$("#hospital-txt").css("color","#666666");
					}else{
						$("#hospital-img").removeClass("mod_6_not_allowed");
						$("#hospital-img").addClass("mod_6");
						$("#hospital-txt").css("cursor","pointer");
						$("#hospital-txt").css("color","");
					}
				}
			});
		}
	}
}

//地标信息展示
function selectMgisShow(index){
			cleartagli("cate_more");
			cleartagli("cate_area");
			//1，机场/车站地标选择
			if(index==1){
				if(airdrome=="" && train==""){
					$("#airtrain-txt").removeClass('on');
					$(".catemod").hide();
					return;
				}
				var i=0;
				if(airdrome!=="" && train==""){
					$("#cate_more").append("<li class='cur'><a href='javascript:;' title='机场' >机场</a></li>");
					$.each(airdrome, function(key, value){
						i++;
						var airdromea = value;
						$("#cate_area").append("<li><a href='list-"+MgisCode.toLowerCase()+"-"+airdromea.ID+".html"+"' title='"+airdromea.name+"' >"+airdromea.name+"</a></li>");
					});
					$("#catemod").show();
					return;
				}
				if(train!=="" && airdrome==""){
					$("#cate_more").append("<li class='cur'><a href='javascript:;' title='火车' >火车</a></li>");
						var j=0;
						$.each(train, function(key, value){
							j++;
							var traina = value;
							$("#cate_area").append("<li><a href='list-"+MgisCode.toLowerCase()+"-"+traina.ID+".html"+"' title='"+traina.name+"' >"+traina.name+"</a></li>");
		
						});
					$("#catemod").show();
					return;
				}
				
				if(train!=="" && airdrome!==""){
					$("#cate_more").append("<li class='cur' id='airchoose' onclick='tagselect(this.id)' ><a href='javascript:;' title='机场' >机场</a></li>");
					$("#cate_more").append("<li id='trainchoose' onclick='tagselect(this.id)'><a href='javascript:;' title='火车' >火车</a></li>");
					tagselect("airchoose");
					$("#catemod").show();
					return;
				}
			}
			
			//2，地铁周边
			if(index==2){
				if(subway==""){
					$("#subway-txt").removeClass('on');
					$(".catemod").hide();
					return;
				}
				
				$("#cate_more").append("<li class='cur'><a href='javascript:;' title='地铁站' >地铁站</a></li>");
				var i=0;
				$.each(subway, function(key, value){

					i++;
					var subwaya = value;
					$("#cate_area").append("<li><a href='list-"+MgisCode.toLowerCase()+"-"+subwaya.ID+".html"+"' title='"+subwaya.name+"' >"+subwaya.name+"</a></li>");

				});
				$("#catemod").show();
				return;
			}
			
			//3,选择行政区地标处理
			if(index==3){
				if(district==""){
					$("#district-txt").removeClass('on');
					$(".catemod").hide();
					return;
				}
				$("#cate_more").append("<li class='cur'><a href='javascript:;' title='行政区' >行政区</a></li>");
				var i=0;
				$.each(district, function(key, value){

					i++;
					var vname = value[1];
					var vcode = value[3];

					$("#cate_area").append("<li><a href='list-xingzheng-"+MgisCode.toLowerCase()+"-"+vcode.toLowerCase()+".html"+"' title='"+vname+"' >"+vname+"</a></li>");
				});
				$("#catemod").show();
				return;
			}
			
			//4,选择商业区地标处理
			if(index==4){
				if(business==""){
					$("#business-txt").removeClass('on');
					$(".catemod").hide();
					return;
				}
				$("#cate_more").append("<li class='cur'><a href='javascript:;' title='热门位置' >热门位置</a></li>");
			
				var i=0;
				$.each(business, function(key, value){
					i++;
					var vname = value[1];
					var vcode = value[3];
					$("#cate_area").append("<li><a href='list-"+MgisCode.toLowerCase()+"-"+vcode.toLowerCase()+".html"+"' title='"+vname+"' >"+vname+"</a></li>");

				});
				$("#catemod").show();
				return;
			}
			
			//5,选择旅游景区地标处理
			if(index==5){
				if(scenic==""){
					$("#scenic-txt").removeClass('on');
					$(".catemod").hide();
					return;
				}
				$("#cate_more").append("<li class='cur'><a href='javascript:;' title='景点附近' >景点附近</a></li>");
				var i=0;
				$.each(scenic, function(key, value){
					i++;
					var scenica = value;
					$("#cate_area").append("<li><a href='list-"+MgisCode.toLowerCase()+"-"+scenica.ID+".html"+"' title='"+scenica.name+"' >"+scenica.name+"</a></li>");
				});
				$("#catemod").show();
				return;
			}
			
			//6,选择医院附近地标处理
			if(index==6){
				if(hospital==""){
					$("#hospital-txt").removeClass('on');
					$(".catemod").hide();
					return;
				}
				$("#cate_more").append("<li class='cur'><a href='javascript:;' title='医院附近' >医院附近</a></li>");
				var i=0;
				$.each(hospital, function(key, value){
					i++;
					var hospitala = value;
					$("#cate_area").append("<li><a href='list-"+MgisCode.toLowerCase()+"-"+hospitala.ID+".html"+"' title='"+hospitala.name+"' >"+hospitala.name+"</a></li>");
				});
				$("#catemod").show();
				return;
			}
			
			//7,选择学校附近地标处理
			if(index==7){
				if(university==""){
					$("#university-txt").removeClass('on');
					$(".catemod").hide();
					return;
				}
				$("#cate_more").append("<li class='cur'><a href='javascript:;' title='学校附近' >学校附近</a></li>");
				var i=0;
				$.each(university, function(key, value){
					i++;
					var universitya = value;
					$("#cate_area").append("<li><a href='list-"+MgisCode.toLowerCase()+"-"+universitya.ID+".html"+"' title='"+universitya.name+"' >"+universitya.name+"</a></li>");
				});
				$("#catemod").show();
				return;
			}
			
}
 
  //清除div下子标签
function cleartagbranddiv()
{
 	var div = document.getElementById("scroll_wrapper");
    while(div.hasChildNodes()) //当div下还存在子节点时 循环继续
    {
        div.removeChild(div.firstChild);
    }
    
    var div1 = document.getElementById("slider-img");
    while(div1.hasChildNodes()) //当div下还存在子节点时 循环继续
    {
        div1.removeChild(div1.firstChild);
    }
  	return;
 }
 
 
 //清除li标签
function cleartagli(id)
{
  	var s=document.getElementById(id);
  	var c=s.childNodes.length;
  	for (var i=c-1;i>=0;i--)
  	{	
    	s.removeChild(s.childNodes[i]);
  	}
  	return;
 }
 
 
 //机场车站tag切换展示
function tagselect(id){
	cleartagli("cate_area");
	if(id=="airchoose"){
		$("#airchoose").addClass("cur");
		$("#trainchoose").removeClass('cur');
		
		$.each(airdrome, function(key, value){
			var airdromea = value;
			$("#cate_area").append("<li><a href='list-"+MgisCode.toLowerCase()+"-"+airdromea.ID+".html"+"' title='"+airdromea.name+"' >"+airdromea.name+"</a></li>");
		});
	}else{
		$("#trainchoose").addClass("cur");
		$("#airchoose").removeClass('cur');
		
		$.each(train, function(key, value){
			var traina = value;
			$("#cate_area").append("<li><a href='list-"+MgisCode.toLowerCase()+"-"+traina.ID+".html"+"' title='"+traina.name+"' >"+traina.name+"</a></li>");
		});
	}
}

 //酒店品牌初始化
 function initbrandshow(){
 	var brandString = document.getElementById("brandString").value;
 	cleartagbranddiv();
 	
 	if(brandString == null){
 		return;
 	}
 	var i = 0;
 	var listbrand = brandString.split(";");
 	for(var i=1;i<listbrand.length;i++){
 		var brand = listbrand[i-1]
		var list = brand.split("=");
		var key;
		var value;
		for(var j=0;j<list.length;j++){
			key=list[0];
			value=list[1];
		}
 		if(i == 1 ){
 			$("#slider-img").append("&nbsp;<a href='javascript:;' class='cur'></a>");
 			$("#scroll_wrapper").append("<div class='child'><ul class='clearfix' id='clearfix'></ul></div>");
 			$("#clearfix").append("<li><a target='_blank' href='hotel-brand-"+key+".html'><img src='http://himg.mangocity.com/img/h/2011/"+key+".jpg'  alt='"+value+"'/></a><br /><a target='_blank' href='hotel-brand-"+key+".html' title='"+value+"' class='bd_a_name'>"+value+"</a></li>");
 		}
 		if(i >1 && i<11){
 			if(key=="1041"){
 			 	$("#clearfix").append("<li><a target='_blank' href='http://www.mangocity.com/promotion/group/h_hcg/index.html'><img src='http://himg.mangocity.com/hotel/img/h/brank/hongcha.jpg'  alt='香港红茶宾馆'/></a><br /><a target='_blank' href='http://www.mangocity.com/promotion/group/h_hcg/index.html' title='香港红茶宾馆' class='bd_a_name'>香港红茶宾馆</a></li>");
 			}else{
 				$("#clearfix").append("<li><a target='_blank' href='hotel-brand-"+key+".html'><img src='http://himg.mangocity.com/img/h/2011/"+key+".jpg'  alt='"+value+"'/></a><br /><a target='_blank' href='hotel-brand-"+key+".html' title='"+value+"' class='bd_a_name'>"+value+"</a></li>");
 			}
 		}
 		
 		if(i==11){
 			$("#slider-img").append("&nbsp;<a href='javascript:;'></a>");
 			$("#scroll_wrapper").append("<div class='child'><ul class='clearfix' id='clearfix10'></ul></div>");
 			$("#clearfix10").append("<li><a target='_blank' href='hotel-brand-"+key+".html'><img src='http://himg.mangocity.com/img/h/2011/"+key+".jpg'  alt='"+value+"'/></a><br /><a target='_blank' href='hotel-brand-"+key+".html' title='"+value+"' class='bd_a_name'>"+value+"</a></li>");
 		}
 		if(i >11 && i<21){
 			$("#clearfix10").append("<li><a target='_blank' href='hotel-brand-"+key+".html'><img src='http://himg.mangocity.com/img/h/2011/"+key+".jpg'  alt='"+value+"'/></a><br /><a target='_blank' href='hotel-brand-"+key+".html' title='"+value+"' class='bd_a_name'>"+value+"</a></li>");
 		}
 		
 		if(i==21){
 			$("#slider-img").append("&nbsp;<a href='javascript:;'></a>");
 			$("#scroll_wrapper").append("<div class='child'><ul class='clearfix' id='clearfix20'></ul></div>");
 			$("#clearfix20").append("<li><a target='_blank' href='hotel-brand-"+key+".html'><img src='http://himg.mangocity.com/img/h/2011/"+key+".jpg'  alt='"+value+"'/></a><br /><a target='_blank' href='hotel-brand-"+key+".html' title='"+value+"' class='bd_a_name'>"+value+"</a></li>");
 		}
 		if(i >21 && i<31){
 			$("#clearfix20").append("<li><a target='_blank' href='hotel-brand-"+key+".html'><img src='http://himg.mangocity.com/img/h/2011/"+key+".jpg'  alt='"+value+"'/></a><br /><a target='_blank' href='hotel-brand-"+key+".html' title='"+value+"' class='bd_a_name'>"+value+"</a></li>");
 		}
 		
 		if(i==31){
 			$("#slider-img").append("&nbsp;<a href='javascript:;'></a>");
 			$("#scroll_wrapper").append("<div class='child'><ul class='clearfix' id='clearfix30'></ul></div>");
 			$("#clearfix30").append("<li><a target='_blank' href='hotel-brand-"+key+".html'><img src='http://himg.mangocity.com/img/h/2011/"+key+".jpg'  alt='"+value+"'/></a><br /><a target='_blank' href='hotel-brand-"+key+".html' title='"+value+"' class='bd_a_name'>"+value+"</a></li>");
 		}
 		if(i >31 && i<41){
 			$("#clearfix30").append("<li><a target='_blank' href='hotel-brand-"+key+".html'><img src='http://himg.mangocity.com/img/h/2011/"+key+".jpg'  alt='"+value+"'/></a><br /><a target='_blank' href='hotel-brand-"+key+".html' title='"+value+"' class='bd_a_name'>"+value+"</a></li>");
 		}
 		
 		if(i==41){
 			$("#slider-img").append("&nbsp;<a href='javascript:;'></a>");
 			$("#scroll_wrapper").append("<div class='child'><ul class='clearfix' id='clearfix40'></ul></div>");
 			$("#clearfix40").append("<li><a target='_blank' href='hotel-brand-"+key+".html'><img src='http://himg.mangocity.com/img/h/2011/"+key+".jpg'  alt='"+value+"'/></a><br /><a target='_blank' href='hotel-brand-"+key+".html' title='"+value+"' class='bd_a_name'>"+value+"</a></li>");
 		}
 		if(i >41 && i<51){
 			$("#clearfix40").append("<li><a target='_blank' href='hotel-brand-"+key+".html'><img src='http://himg.mangocity.com/img/h/2011/"+key+".jpg'  alt='"+value+"'/></a><br /><a target='_blank' href='hotel-brand-"+key+".html' title='"+value+"' class='bd_a_name'>"+value+"</a></li>");
 		}
 		
 		if(i==51){
 			$("#slider-img").append("&nbsp;<a href='javascript:;'></a>");
 			$("#scroll_wrapper").append("<div class='child'><ul class='clearfix' id='clearfix50'></ul></div>");
 			$("#clearfix50").append("<li><a target='_blank' href='hotel-brand-"+key+".html'><img src='http://himg.mangocity.com/img/h/2011/"+key+".jpg'  alt='"+value+"'/></a><br /><a target='_blank' href='hotel-brand-"+key+".html' title='"+value+"' class='bd_a_name'>"+value+"</a></li>");
 		}
 		if(i >51 && i<61){
 			$("#clearfix50").append("<li><a target='_blank' href='hotel-brand-"+key+".html'><img src='http://himg.mangocity.com/img/h/2011/"+key+".jpg'  alt='"+value+"'/></a><br /><a target='_blank' href='hotel-brand-"+key+".html' title='"+value+"' class='bd_a_name'>"+value+"</a></li>");
 		}
 		
 		if(i==61){
 			$("#slider-img").append("&nbsp;<a href='javascript:;'></a>");
 			$("#scroll_wrapper").append("<div class='child'><ul class='clearfix' id='clearfix60'></ul></div>");
 			$("#clearfix60").append("<li><a target='_blank' href='hotel-brand-"+key+".html'><img src='http://himg.mangocity.com/img/h/2011/"+key+".jpg'  alt='"+value+"'/></a><br /><a target='_blank' href='hotel-brand-"+key+".html' title='"+value+"' class='bd_a_name'>"+value+"</a></li>");
 		}
 		if(i >61 && i<71){
 			$("#clearfix60").append("<li><a target='_blank' href='hotel-brand-"+key+".html'><img src='http://himg.mangocity.com/img/h/2011/"+key+".jpg'  alt='"+value+"'/></a><br /><a target='_blank' href='hotel-brand-"+key+".html' title='"+value+"' class='bd_a_name'>"+value+"</a></li>");
 		}
 		
 		if(i==71){
 			$("#slider-img").append("&nbsp;<a href='javascript:;'></a>");
 			$("#scroll_wrapper").append("<div class='child'><ul class='clearfix' id='clearfix70'></ul></div>");
 			$("#clearfix70").append("<li><a target='_blank' href='hotel-brand-"+key+".html'><img src='http://himg.mangocity.com/img/h/2011/"+key+".jpg'  alt='"+value+"'/></a><br /><a target='_blank' href='hotel-brand-"+key+".html' title='"+value+"' class='bd_a_name'>"+value+"</a></li>");
 		}
 		if(i >71 && i<81){
 			$("#clearfix70").append("<li><a target='_blank' href='hotel-brand-"+key+".html'><img src='http://himg.mangocity.com/img/h/2011/"+key+".jpg'  alt='"+value+"'/></a><br /><a target='_blank' href='hotel-brand-"+key+".html' title='"+value+"' class='bd_a_name'>"+value+"</a></li>");
 		}
 		
 		if(i==81){
 			$("#slider-img").append("&nbsp;<a href='javascript:;'></a>");
 			$("#scroll_wrapper").append("<div class='child'><ul class='clearfix' id='clearfix80'></ul></div>");
 			$("#clearfix80").append("<li><a target='_blank' href='hotel-brand-"+key+".html'><img src='http://himg.mangocity.com/img/h/2011/"+key+".jpg'  alt='"+value+"'/></a><br /><a target='_blank' href='hotel-brand-"+key+".html' title='"+value+"' class='bd_a_name'>"+value+"</a></li>");
 		}
 		if(i >81 && i<91){
 			$("#clearfix80").append("<li><a target='_blank' href='hotel-brand-"+key+".html'><img src='http://himg.mangocity.com/img/h/2011/"+key+".jpg'  alt='"+value+"'/></a><br /><a target='_blank' href='hotel-brand-"+key+".html' title='"+value+"' class='bd_a_name'>"+value+"</a></li>");
 		}
 		
 		if(i==91){
 			$("#slider-img").append("&nbsp;<a href='javascript:;'></a>");
 			$("#scroll_wrapper").append("<div class='child'><ul class='clearfix' id='clearfix90'></ul></div>");
 			$("#clearfix90").append("<li><a target='_blank' href='hotel-brand-"+key+".html'><img src='http://himg.mangocity.com/img/h/2011/"+key+".jpg'  alt='"+value+"'/></a><br /><a target='_blank' href='hotel-brand-"+key+".html' title='"+value+"' class='bd_a_name'>"+value+"</a></li>");
 		}
 		if(i >91 && i<100){
 			$("#clearfix90").append("<li><a target='_blank' href='hotel-brand-"+key+".html'><img src='http://himg.mangocity.com/img/h/2011/"+key+".jpg'  alt='"+value+"'/></a><br /><a target='_blank' href='hotel-brand-"+key+".html' title='"+value+"' class='bd_a_name'>"+value+"</a></li>");
 		}
 	}
 	
 	/*品牌酒店*/
	$("#slider").pislider({
		child:"child",
		slider_bar:"slider-img",
		scrollTime:500,
		autoScroll:"true",
		autoTime:3000
	});
	$("#btn_prev").click(function(){
		$(this).removeClass('lno').addClass('lyes');
		$(this).siblings('a').removeClass('ryes').addClass('rno');
	});
	$("#btn_next").click(function(){
		$(this).removeClass('rno').addClass('ryes');
		$(this).siblings('a').removeClass('lyes').addClass('lno');
	});		
 }