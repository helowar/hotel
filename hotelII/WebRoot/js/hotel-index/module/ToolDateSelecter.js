//oop datepicker
/*
* 面板两个状态：1、打开 true 2、关闭 false
* 样式三个状态：1、载入状态（最原始状态）2、获取焦点状态 3、失去焦点状态（a.合理日期, b.非法日期）
*
* 面板点击事件：1、点击面板之外 2、点击面板范围之内
*
* 属性的获取:1、创建实例时赋予基本属性 2、在表单元素获取焦点时开始查找各种属性
*
*/

//var holiday={"2010-01-01":{holidayName:"元旦节",beforeTime:3,afterTime:3,dayindex:0},"2011-02-14":{holidayName:"情人节",beforeTime:7,afterTime:0,dayindex:0},"2010-04-05":{holidayName:"清明节",beforeTime:3,afterTime:3,dayindex:0},"2010-05-01":{holidayName:"劳动节",beforeTime:3,afterTime:3,dayindex:0},"2010-06-16":{holidayName:"端午节",beforeTime:3,afterTime:3,dayindex:0},"2010-09-22":{holidayName:"中秋节",beforeTime:3,afterTime:3,dayindex:0},"2010-10-01":{holidayName:"国庆节",beforeTime:3,afterTime:0,dayindex:0},"2011-01-01":{holidayName:"元旦节",beforeTime:3,afterTime:3,dayindex:0},"2011-02-03":{holidayName:"春节",beforeTime:7,afterTime:3,dayindex:0},"2011-04-05":{holidayName:"清明节",beforeTime:3,afterTime:3,dayindex:0},"2011-05-01":{holidayName:"劳动节",beforeTime:3,afterTime:3,dayindex:0},"2011-06-06":{holidayName:"端午节",beforeTime:3,afterTime:3,dayindex:0},"2011-09-12":{holidayName:"中秋节",beforeTime:3,afterTime:3,dayindex:0},"2011-10-01":{holidayName:"国庆节",beforeTime:3,afterTime:0,dayindex:0}};
var holiday = ["2012-01-22", "2012-01-23", "2012-02-06", "2012-04-04", "2012-05-01", "2012-06-23", "2012-09-30", "2012-10-01", "2013-01-01"]

var Datepicker = function(target,config){
    this.target = target || null; //日历目标节点
    this.eachW = config.eachW || 177; //单列宽度
	this.eachH = config.eachH || 178; //单列高度
	
	this.year = config.year || new Date().getFullYear(); //当前年
	this.month = config.month || (new Date().getMonth() + 1); //当前月
	this.day = config.day || new Date().getDate(); //当前日

	this.startDate = config.startDate || [];
	this.endDate = config.endDate || [];
	this.getsDatefrom = config.getsDatefrom || null; //临时获取开始日期
	this.ttMonth = config.ttMonth || 2; //共显示几个月的数据，默认两个月，即双日历
	this.direct = config.direct || '';
	this.fromtarget = config.fromtarget || false;
	this.targetValue = null;
	this.selected = [];
	this.isclick = false;
	this.addDays = config.addDays || 0; //自动增加天数
	this.cday = 0; //选中日期
	this.isopen = false; //默认关闭
	this.inputs = []; //日历输入框(数量)
	this.callback = config.callback || null;
	this._init(); //初始化
	
};
Datepicker.prototype = {
    _init : function(){
		this._startEvent();		
	},
	_createPanel : function(){
	    var dp_wrap = MGTOOL.$id('dp_id');
		if(dp_wrap){
		    return;
		}else{
		    var div = document.createElement('div');
			div.className = "datepicker";
			div.id = "dp_id";
			div.innerHTML = '<div class="pickerwrap"><em class="prevMonth"></em><em class="nextMonth"></em><div class="pickwidth" id="pickwidth"> </div> </div><iframe frameborder="0" marginheight="0" marginwidth="0" id="dateframe" style="position:absolute; left:-8px; top:-8px; overflow:hidden; background:#fff; border:0; z-index:-1; opacity:0; filter:alpha(opacity = 0);"></iframe></div>';
			document.body.appendChild(div);
		}
	},
	_getCalendar : function(date){
	    this.year = date.getFullYear(), this.month = date.getMonth(), this.day = date.getDate(); //获取当前日期
		var ttm = this.ttMonth, dpanel = [];
		var ndate = new Date(this.year,this.month,1); //日期函数在调用前必须先初始化
		for(var i=0; i<ttm; i++){
			dpanel[i] = this._getDayList(ndate, i);
			ndate.setMonth(ndate.getMonth()+1);
			//var addd = new Date(ndate.getFullYear(),ndate.getMonth()+i+1,0).getDate();
			//ndate.setFullYear(ndate.getFullYear(), ndate.getMonth(), 1+addd);
		}
		
		MGTOOL.$id('pickwidth').innerHTML = dpanel.join('');
		MGTOOL.$id('dp_id').style.width = ttm*this.eachW + "px";
		MGTOOL.$id('dateframe').style.width  = (ttm*this.eachW + 8) + "px";
		MGTOOL.$id('dateframe').style.height  = 188 + "px";
	},
	_getDayList : function(date, tn){

	    var dplist = ['日','一','二','三','四','五','六'],
		    today = (this.fromtarget) ? this.targetValue[2] : typeof(this.startDate[2]) !== "undefined" ? this.startDate[2] : new Date().getDate(),
			tmonth = (this.fromtarget) ? this.targetValue[1]-1 : typeof(this.startDate[1]) !== "undefined" ? this.startDate[1] : new Date().getMonth(),
			tyear = (this.fromtarget) ? this.targetValue[0] : typeof(this.startDate[0]) !== "undefined" ? this.startDate[0] : new Date().getFullYear(),
			cmonth = date.getMonth(), //取得的月数字比实际小1
			cyear = date.getFullYear(),
			cday = this.cday,
			addDay = (this.fromtarget) ? this._operaAddDay(this.addDays, new Date(this.targetValue[0], (this.targetValue[1]-1), this.targetValue[2])) : this._operaAddDay(this.addDays),
			eyear = this.endDate[0] || addDay[0],
			emonth = this.endDate[1] || addDay[1],
			eday =  this.endDate[2] || addDay[2],
			M = this.target.value.split("-")[1]-1,
			Y = this.target.value.split("-")[0]-0;
			//console.log(this.fromtarget);
			//console.log(this.startDate);
			//console.log(cyear+'-'+cmonth+'-'+cday);
			//console.log(this.year+'-'+this.month+'-'+this.day);
			//console.log(this.endDate);
			//console.log(eyear+'-'+emonth+'-'+eday);
			if(this.endDate.length && (new Date(addDay[0],addDay[1],addDay[2]) < new Date(this.endDate[0],this.endDate[1],this.endDate[2]))){
			    eyear = addDay[0];
				emonth = addDay[1];
				eday = addDay[2];
			}
			//console.log(eyear+'-'+emonth+'-'+eday);
			//eyear = (this.endDate[0] && addDay[0]) ? (addDay[0]<this.endDate[0] ? addDay[0] : this.endDate[0]) : addDay[0] || this.endDate[0];
			//emonth = (this.endDate[1] && addDay[1]) ? (addDay[0]==this.endDate[0] && addDay[1]<this.endDate[1] ? addDay[1] : this.endDate[1]) : addDay[1] || this.endDate[1];
			//eday = (this.endDate[2] && addDay[2]) ? (addDay[0]==this.endDate[0] &&  new Date(addDay[0],addDay[1],addDay[2]) < new Date(this.endDate[0],this.endDate[1],this.endDate[2]) && addDay[1]<=this.endDate[1] || addDay[0]==this.endDate[0] &&  addDay[1]==this.endDate[1] &&  addDay[2]<this.endDate[2] ? addDay[2] : this.endDate[2]) : addDay[2] || this.endDate[2];
            //console.log(addDay);
			//console.log('ayear:--'+addDay[0]+' amonth:--'+addDay[1]+' aoday:--'+addDay[2]);
			//console.log('tyear:--'+tyear+' tmonth:--'+tmonth+' today:--'+today);
			//console.log('cyear:--'+cyear+' cmonth:--'+cmonth+' cday:--'+cday);
			//console.log('eyear:--'+this.endDate[0]+' emonth:--'+this.endDate[1]+' eday:--'+this.endDate[2]);
			//console.log('eyear:--'+eyear+' emonth:--'+emonth+' eday:--'+eday);
			//console.log('[start]:--'+this.startDate + '  [end]:--'+this.endDate + '  [add]:--' + addDay + '  [cyear]:--'+cyear+'  [cmonth]:--'+cmonth+'  [cday]:--'+cday);
		var arr = [], firstday, lastday, list = [], plist;
		firstday = new Date(cyear, cmonth, 1).getDay(); //当月第一天在第一周中的位置
		lastday = new Date(cyear, cmonth+1, 0).getDate(); //返回当月天数
		for(var i=1; i<=firstday; i++){arr.push(0);}
		for(var i=1; i<=lastday; i++){arr.push(i);}
		
		if(!Array.prototype.isIn){
			Array.prototype.isIn = function (obj){
				var k = this.length;
				while(k--){

						  if(this[k]==obj){
						  return true;
					}
				}
				return false;
			};
		}
		
		

		//console.log((tyear+'-'+((tmonth+1)<10 ? ("0"+(tmonth+1)) : (cmonth+1))+'-'+(arr[j]<10 ? ("0"+arr[j]) : arr[j])));
		for(var j=0, k=arr.length; j<k; j++){ //console.log((cyear+'-'+((cmonth+1)<10 ? ("0"+(cmonth+1)) : (cmonth+1))+'-'+(arr[j]<10 ? ("0"+arr[j]) : arr[j])));
			if((tyear > cyear && arr[j]) || ((tyear == cyear) && (tmonth > cmonth) && arr[j]) || ((tyear == cyear) && (tmonth == cmonth) && arr[j] && (arr[j] < today)) || (eyear < cyear && arr[j]) || ((eyear == cyear) && (emonth < cmonth) && arr[j]) || ((eyear == cyear) && (emonth == cmonth) && arr[j] && (eday < arr[j]))){
			//if((tyear > Y && arr[j]) || ((tyear == Y) && (tmonth > M) && arr[j]) || ((tyear == Y) && (tmonth == M) && arr[j] && (arr[j] < today)) || (eyear < Y && arr[j]) || ((eyear == Y) && (emonth < M+1) && arr[j]) || ((eyear == Y) && (emonth == M+1) && arr[j] && (eday < arr[j]))){
				//console.log(arr[j])
				list.push('<li>'+ arr[j] +'</li>');
			}else if(holiday && holiday.isIn((cyear+'-'+((cmonth+1)<10 ? ("0"+(cmonth+1)) : (cmonth+1))+'-'+(arr[j]<10 ? ("0"+arr[j]) : arr[j])))){ //console.log('dd');
			//console.log(tyear+''+((tmonth+1)<10 ? ("0"+(tmonth+1)) : (cmonth+1))+''+(arr[j]<10 ? ("0"+arr[j]) : arr[j]))
				list.push('<li><a href="javascript:void(0);" class="hld hl'+cyear+''+((cmonth+1)<10 ? ("0"+(cmonth+1)) : (cmonth+1))+''+(arr[j]<10 ? ("0"+arr[j]) : arr[j])+'" title="'+cyear+'-'+((cmonth+1)<10 ? ("0"+(cmonth+1)) : (cmonth+1))+'-'+(arr[j]<10 ? ("0"+arr[j]) : arr[j])+'">&nbsp;</a></li>');
			    //continue;
			}else if((tyear == cyear) && (tmonth == cmonth) && arr[j] && arr[j]==today){
			    //console.log(arr[j]);
				list.push('<li><a href="javascript:void(0);" class="now" title="'+cyear+'-'+((cmonth+1)<10 ? ("0"+(cmonth+1)) : (cmonth+1))+'-'+(arr[j]<10 ? ("0"+arr[j]) : arr[j])+'">'+ arr[j] +'</a></li>');
			}else if((cyear == this.selected[0]) && (cmonth == this.selected[1]) && arr[j] && arr[j]==cday){
				list.push('<li><a href="javascript:void(0);" class="choice" title="'+cyear+'-'+((cmonth+1)<10 ? ("0"+(cmonth+1)) : (cmonth+1))+'-'+(arr[j]<10 ? ("0"+arr[j]) : arr[j])+'">'+ arr[j] +'</a></li>');
			}else if(arr[j]){
				list.push('<li><a href="javascript:void(0);" title="'+cyear+'-'+((cmonth+1)<10 ? ("0"+(cmonth+1)) : (cmonth+1))+'-'+(arr[j]<10 ? ("0"+arr[j]) : arr[j])+'">'+ arr[j] +'</a></li>');
			}else{
				list.push('<li>&nbsp;</li>');
			}
		}			
        if(tn != 0){
		    plist = '<div class="pickerbody"><div class="pickhead"><strong>'+ cyear +'年'+ (cmonth+1) +'月</strong></div><div class="pickweek wbleft"><span class="wkfont">'+ dplist[0] +'</span><span>'+ dplist[1] +'</span><span>'+ dplist[2] +'</span><span>'+ dplist[3] +'</span><span>'+ dplist[4] +'</span><span>'+ dplist[5] +'</span><span class="wkfont">'+ dplist[6] +'</span></div><div class="dateswrap bleft"><ul>'+ list.slice(0).join('') +'</ul></div></div>';
		}else{
		    plist = '<div class="pickerbody"><div class="pickhead"><strong>'+ cyear +'年'+ (cmonth+1) +'月</strong></div><div class="pickweek"><span class="wkfont">'+ dplist[0] +'</span><span>'+ dplist[1] +'</span><span>'+ dplist[2] +'</span><span>'+ dplist[3] +'</span><span>'+ dplist[4] +'</span><span>'+ dplist[5] +'</span><span class="wkfont">'+ dplist[6] +'</span></div><div class="dateswrap"><ul>'+ list.slice(0).join('') +'</ul></div></div>';
		}
		return plist;		
		
		
	},
	_operaAddDay : function(num, date){
		var now = date ? new Date(date.getFullYear(), date.getMonth(), date.getDate()) : new Date();
	    var obj = [];
		if(!!num){
		now.setFullYear(now.getFullYear(), now.getMonth(), now.getDate()+num);
		//now.setDate(now.getDate()+num);
		obj.push(now.getFullYear());
		obj.push((now.getMonth())<10 ? "0"+(now.getMonth()): (now.getMonth()));
		obj.push(now.getDate()<10 ? "0"+now.getDate() :now.getDate());
		}
		return obj;
	},
	_onclick : function(e, target, cur){
	    var eTarget = MGTOOL.getTarget(e),
		    sTag = eTarget.nodeName,
		    panel = MGTOOL.$id('dp_id'),
		    thisday,
			date = new Date(this.year, this.month, this.day);
		if(!this.isopen){ return false; }
		var cb = this.callback;
		//console.log(sTag);
		if(MGTOOL.contains(panel, eTarget)){
		
			switch(sTag){
				case 'EM' :
					if(/prevMonth/.test(eTarget.className)) {this._preMonth(date);} //date come from input
				    if(/nextMonth/.test(eTarget.className)) {this._nextMonth(date);}
					break;
				case 'A' :
					if(cb){
					    cb(cur, eTarget);
						this.valuechanged = eTarget.title;
						this._resetPosition(target, panel);
					}					
				    break;
				default :
					break;

			}
		
		    this.isclick = true;
		}else{
            this._resetPosition(target, panel);
            this.isclick = false;			
		}
	},
	_preMonth : function(date){
		date.setMonth(date.getMonth()-1);
	    this._getCalendar(date);
	},
	_nextMonth : function(date){
		date.setMonth(date.getMonth()+1);
	    this._getCalendar(date);
	},
	_setposition : function(input, el, direct){
	    el.style.display = 'block';
		var pos = MGTOOL.getoffset(input),
		    py = pos.top, 
			px = pos.left, 
			st = document.documentElement ? document.documentElement.scrollTop : document.body.scrollTop, 
			sl = document.documentElement ? document.documentElement.scrollLeft : document.body.scrollLeft, 
			th = input.offsetHeight,
            tw = input.offsetWidth, 			
			oh = el.offsetHeight, //弹出框高度
			ow = el.offsetWidth, //弹出框宽度
			dch = document.documentElement.clientHeight, //浏览器可视高度
			dcw = document.documentElement.clientWidth; //浏览器可视宽度
		//console.log(dch);
		if((py-st+oh>dch && py-st>oh) || direct == 'up'){
			y = py-oh;
			//el.style.left = px + "px";
			el.style.top = y + "px";
		}else{
			y = py+th;
			//el.style.left = px + "px";
			el.style.top = y + "px";
		}
		//alert(ow);
		//console.log(px-sl<ow);
		if(px-sl+ow>dcw && px-sl>ow){
		    x = px-ow+tw;
		    el.style.left = x + "px";
			//el.style.right = "0px";
		}else{
		    x = px;
		    el.style.left = x + "px";
		}
		
		
		
		el.style.display = 'block';
		//el.style.left = pos.left + "px";
		//el.style.top = pos.top + input.offsetHeight + "px";
	    this.isopen = true;
	},
	_resetPosition : function(input, el){
	    el.style.display = 'none';
		this.isopen = false;
	},
	_startEvent : function(){
	    var gss = [];
		if(this.getsDatefrom){
		    gss = this.getsDatefrom.value.split('-');
			//console.log(gss);
		}	
	
		function isDate(str){
			var reg = /(?:[19|20]\d{2})-(?:0[1-9]|1[0-2])-(?:0[1-9]|[12][0-9]|3[01])/;
			return reg.test(str) && str.length == 10;
		}	
			
	    var that = this;
		if(that.target){
		    //var curvalue = that.target.value;
			//this.valuechanged = that.target.value;
			that.target.onfocus = that.target.onclick = function(e){
			    //curvalue = that.valuechanged;
				that._createPanel();
				//alert(isDate(this.value));
				that.targetValue = this.value.split('-');
				var date = this.value && isDate(this.value) ? new Date(this.value.split('-')[0], this.value.split('-')[1]-1, this.value.split('-')[2]) : (that.startDate.length && (that.startDate[0]!=="NaN" || !isNaN(that.startDate[0]))) ? new Date(that.startDate[0],that.startDate[1],that.startDate[2])  : new Date();
                //console.log(that.startDate)
				//console.log(isNaN(that.startDate[0]))
				if(this.value && !isDate(this.value)){
				    that.target.value = '';
				}
				that.selected = [date.getFullYear(), date.getMonth(), date.getDate()];
				//console.log(that.selected);
				that.cday = date.getDate();
				that._getCalendar(date);
				that._setposition(this, MGTOOL.$id('dp_id'), that.direct);
				
				document.onmousedown = function(e){
					that._onclick(e, this, that.target);
				};
				
			};
			
			that.target.onblur = function(){
			    if(!that.isclick){
				    //console.log(1);
				    that._resetPosition(that.target, MGTOOL.$id('dp_id'));
					that.isclick = false;
				}
				blurProcessor(that.target.id);
				//curvalue = that.valuechanged;
			};
			
        }else{
		    var targets = MGTOOL.getbyClass(document,'input','mg_q_datepikcer');
			if(targets.length){
				for(var i=0, len=targets.length; i<len; i++){
					targets[i].onfocus = targets[i].onclick = function(){
						that.target = this;
						that._createPanel();
						that.targetValue = this.value.split('-');
						//var date = this.value && isDate(this.value) ? new Date(this.value.split('-')[0], this.value.split('-')[1]-1, this.value.split('-')[2]) : new Date();
				        var date = this.value && isDate(this.value) ? new Date(this.value.split('-')[0], this.value.split('-')[1]-1, this.value.split('-')[2]) : (that.startDate.length && (that.startDate[0]!=="NaN" || !isNaN(that.startDate[0]))) ? new Date(that.startDate[0],that.startDate[1],that.startDate[2])  : new Date();
						that.selected = [date.getFullYear(), date.getMonth(), date.getDate()];
						
						that.cday = date.getDate();
						that._getCalendar(date);
						that._setposition(this, MGTOOL.$id('dp_id'), that.direct);
						
						document.onmousedown = function(e){
							that._onclick(e, this, that.target);
						};
								
					};
				}
			}
		}
		
	}
};
