
jQuery.fn.alert.defaults.alertClass = 'validate';
//特殊要求的
jQuery('#showspacilerequire').toggle(
    function(){$('#spacilerequire').show();},
	function(){$('#spacilerequire').hide();}
);
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
	//modify by alfred. 2011.6.7
	/*
	if(daysElapsed(date1,date2)>28){
		jQuery("#id_backDate").alert("入住晚数不能超过28天！");
		return false;
	}
	*/
	document.hotelBookForm.submit();	
	$('#mango-greybox').hide();
	$('#mango-overlay').remove();
	$('embed, object, select').css({ 'visibility' : 'visible' });
	$(window).unbind("scroll").unbind("resize");
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

// 点击确定修改时间按钮之后触发的函数
	function changetime(){
	    $('#changetime').unbind('click').click(function(){
	    	formSubmit();
			//修改日期后将修改后的日期加到cookie中
			document.cookie = "inDate="+jQuery("#id_startDate").val()+";"
				+ ("") + ("path=/;") + ("") + (false);
	    	document.cookie = "outDate="+jQuery("#id_backDate").val()+";"
	    		+ ("") + ("path=/;") + ("") + (false); 
		});
	}
    $('#changedatebtn').greybox({closeid:'popclose', callback:changetime});
	
	$('.dateIcon').each(function(i){
	    $(this).click(function(){
		    var parent = $(this).parent();
			if($(parent).children('.calendar')){
			    $(parent).children('.calendar').focus();
			}
			__ozclk();
			return false;
		});
	});
// from here add by IT

//   textarea maxlength验证
function isMax(){
    var textarea = document.getElementById("specialrequireTextarea");  
    var max_length = 50;
	if(textarea.value.length > max_length){
	  textarea.value = textarea.value.substring(0, max_length);
   }
}

//获得历史入住人的信息
 for(var i = 0 ; i < fellowPersonArray.length;i++){
          jQuery("#fellowNameOl").append("<li class='storer_li' method='fel_m' value='"+i+"'>"+fellowPersonArray[i][0]+"</li>");
          if((i+1)%3==0){
             jQuery("#fellowNameOl").append("<li class='clear_li'></li>");
          }
          if(i==6-1){
             break;
          } 
 }  
         
jQuery("input[name='fellowName']").bind('click', 
	function (){
	    this.style.color='#000000';
   	if(jQuery(this).val() != null && jQuery(this).val().indexOf('姓名') != -1) {
   		jQuery(this).val("");
   		this.style.color='#000000';
   	}
    if(fellowPersonArray.length>0){
          var top_value = jQuery(this).offset().top+28;
          var left_value = jQuery(this).offset().left;
          jQuery("#fellowNameDiv").css({top:top_value,left:left_value});
          setTimeout("jQuery('#fellowNameDiv').show()",100);//防止在hide前执行           
          var indexValue = jQuery("input[name='fellowName']").index(jQuery(this));
          jQuery("#fellowNameOl").val(indexValue);
    }
});

jQuery("input[name='fellowName']").bind('focus', 
	function (){
	    this.style.color='#000000';
   	if(jQuery(this).val() != null && jQuery(this).val().indexOf('姓名') != -1) {
   		jQuery(this).val("");
   		this.style.color='#000000';
   	}
});

jQuery("input[name='fellowName']").bind('blur', 
   function (){
   	if(jQuery(this).val() == null || jQuery(this).val() == ""){
   		jQuery(this).val('客人' + jQuery(this).attr('id').split('Id')[1] + "姓名");
   		this.style.color='#999999';
   	}
});

jQuery("li[method='fel_m']").bind('click', 
     function(){
         var thisValue = this.value;
         var mobile = fellowPersonArray[thisValue][1];
         var email = fellowPersonArray[thisValue][2];
         //jQuery("#linkmanTelephone").val(mobile);
         //jQuery("#linkmanEmail").val(email);
         var indexValue = parseInt(jQuery("#fellowNameOl").val());
         jQuery("input[name='fellowName']").eq(indexValue).val(fellowPersonArray[thisValue][0]);
         jQuery("input[name='fellowName']").eq(indexValue).click();//change事件必须点上去再点出去
         jQuery("#fellowNameDiv").hide();
         jQuery("input[name='fellowName']").eq(indexValue+1).click();
         jQuery("input[name='fellowName']").eq(indexValue+1).focus();   
     });
//给fellowNameDiv,linkmanNameDiv添加效果  
jQuery("#fellowNameDiv,#linkmanNameDiv").bind('mouseleave',
    function(){
        jQuery(this).hide();
    });  
//历史联系人
 for(var i = 0 ; i < linkmanArray.length;i++){
          jQuery("#linkmanNameOl").append("<li class='storer_li' method='link_m' value='"+i+"'>"+linkmanArray[i][0]+"</li>");
          if((i+1)%3==0){
             jQuery("#linkmanNameOl").append("<li class='clear_li'></li>");
          }
          if(i==6-1){
             break;
          } 
 }
 
 //当用户填写完联系电话并移开焦点时，进行订单重复的校验，QC3891 add by zengming 2012-03-15 begin
jQuery("#linkmanTelephone").bind('blur',checkDuplicationOrder);
 //当用户填写完联系电话并移开焦点时，进行订单重复的校验，QC3891 add by zengming 2012-03-15 end

//当用户填写完联系人并移开焦点时，进行订单重复的校验，QC3891 add by zengming 2012-03-24 begin
jQuery("#linkmanName").bind('blur',checkDuplicationOrder);
 //当用户填写完联系人并移开焦点时，进行订单重复的校验，QC3891 add by zengming 2012-03-24 end
 
 jQuery("#linkmanName").bind('click', 
    function (){
        if(fellowPersonArray.length>0){
              var top_value = jQuery(this).offset().top+28;
              var left_value = jQuery(this).offset().left
              jQuery("#linkmanNameDiv").css({top:top_value,left:left_value});
              setTimeout("jQuery('#linkmanNameDiv').show()",100);//防止在hide前执行           
        }
});
jQuery("li[method='link_m']").bind('click', 
     function(){
         var thisValue = this.value;
         var mobile = linkmanArray[thisValue][1];
         var email = linkmanArray[thisValue][2];
         jQuery("#linkmanTelephone").val(mobile);
         jQuery("#linkmanEmail").val(email);
         var indexValue = parseInt(jQuery("#fellowNameOl").val());
         jQuery("#linkmanName").val(linkmanArray[thisValue][0]);
         jQuery("#linkmanNameDiv").hide();  
     });
 
	
//初始化到店时间
//function initArrivalHotelTime(){
  // jQuery("#arrivalHotelTime option[value='14:00-17:00']").attr("selected", true); 
//}	
	
	
//变化入住人填写框的个数
function changeFellowPersonInputNum(roomNum){
       //获得原来的roomNum
        var frontRoomNum = jQuery("#fellowPersonLabel").children("input").length;
        var fellowPersonInput = jQuery("input[name='fellowName']:first");
        //添加和移除的个数
        var num = roomNum  - frontRoomNum ;
        //添加input
        if(num > 0){
            for(var i = 0 ; i < num ; i++){
               var copyInput = fellowPersonInput.clone(true);
               copyInput.val("客人" + (frontRoomNum + i+1) + "姓名");
               copyInput.attr("style","color:#999");
               copyInput.attr("id",'fellowNameId' + (frontRoomNum + i+1));
               copyInput.insertAfter(jQuery("input[name='fellowName']:last"));
            }
        }
        //移除input
        if(num < 0){
            for(var i= 0; i > num ; i--){
                jQuery("input[name='fellowName']:last").remove();
            }
        }
}	
//检查是否有重复订单，QC3891 add by zengming 2012-03-15 begin

function checkDuplicationOrder(){
	var iframeUrl='';
	var result='';
	var valHotelId = jQuery("#checkDup_hotelId").val();

	if(jQuery("#checkDup_hotelId") == undefined){
		valHotelId='-1';
	}
	var valHotelName = jQuery("#hotelName").val();
	var valRoomTypeId = jQuery("#checkDup_roomTypeId").val();
	
	if(jQuery("#checkDup_roomTypeId") == undefined){
		valRoomTypeId='-1';
	}
	var valLinkManName = jQuery.trim(jQuery("#linkmanName").val());
	var valPhone = jQuery("#linkmanTelephone").val();
	var  valcheckinDate =jQuery("#checkinDate").text();
	var  valcheckoutDate = jQuery("#checkoutDate").text();
	var valRoomQuantity = jQuery("#roomNumSelect").val();

	var url ='hotel-orderDupCheck!checkOrderDuplication.shtml';   
	var params = "hotelid="+valHotelId+"&roomtypeid="+valRoomTypeId+"&linkman="+valLinkManName+"&mobile="+valPhone+"&checkindate="+valcheckinDate+"&checkoutdate="+valcheckoutDate;
	jQuery.ajax({
   		type: "post",
   		dataType:'text',
		contentType:'application/x-www-form-urlencoded',
   		url: url,
   		//url:"hotel-information!computerCount.shtml",
   		data: encodeURI(encodeURI(params)),
   		//data:"hotelid="+valHotelId+"&roomtypeid="+valRoomTypeId+"&linkman="+valLinkManName+"&mobile="+valPhone+"&checkindate="+valcheckinDate+"&checkoutdate="+valcheckoutDate,
        //回传函数
        timeout:20000, //设置请求超时时间(毫秒)
        error:function (XmlHttpRequest, textStatus, errorThrown){
        	if(XmlHttpRequest.status!='200'&&XmlHttpRequest.status!='0'){
        		alert(XmlHttpRequest.status);
                alert(XmlHttpRequest.responseText);
        	}
            
        },
        success:function(data){ //请求成功后回调函数
        	//result = data;        	
        	if(data>0){
        		var number = parseInt(data) + parseInt(valRoomQuantity);//alert(number);
        		var width = '558';
        		var height ='143';
                if(number<=5){
             	   iframeUrl = 'html/ht-order-fill-tc2.html';
                }else if(number > 5){
             	   iframeUrl = 'html/ht-order-fill-tc.html';
             	   height = '157';
                }
                jQuery("#submitOrder").attr('disabled',true); 
        		//noticeDuplication(iframeUrl);
                OpenMyModal(iframeUrl,width,height);
        		return true;
        	}else{
        		jQuery("#submitOrder").removeAttr('disabled'); 
        		return false;
        	}
        }
   });

}

function OpenMyModal(src, width, height) {
  var frame = '<iframe width="' + width + '" height="' + height + '"src="' + src + '" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes"></iframe>';
  var option = {
      escClose: true,
      close: true,
      minHeight: height,
      minWidth: width,
      autoResize: true,
      position : ["25%"],
	  overlayId : 'contact-overlay',
	  containerId: 'contact-container',
	  closeClass : ('closeMode'),
	  overlayClose: false
  };
  $.modal(frame, option);
}
//检查是否有重复订单，QC3891 add by zengming 2012-03-15 end

//提交按钮	
function  prepaySubmitOrder(){
	//var validateDuplication = checkDuplicateOrder();
     var validateFlag  =  validateAll();
     if(validateFlag == false){return;}
	 inputSomeValueToPrepaySubmitForm();
	 jQuery("#submitOrder").attr("disabled","true");
	 submitPrepayForm();
	 
	 //异步记录是否点击下一步
     	jQuery.ajax({
   		type: "post",
   		url: "hotel-orderRecord!recordNextStep.shtml",
   		contentType:"application/x-www-form-urlencoded; charset=UTF-8"
   });
}

function paySubmitOrder(){
     var validateFlag  =  validateAll();
     if(validateFlag == false){return;}
     inputSomeValueToPaySubmitForm();
      jQuery("#submitOrder").attr("disabled","true");
     submitPayForm();
     
      //异步记录是否点击提交按钮或下一步（担保）
      if("提交订单"==jQuery("#submitOrder").val()){
     	jQuery.ajax({
   		type: "post",
   		url: "hotel-orderRecord!recordSubmitButton.shtml",
   		contentType:"application/x-www-form-urlencoded; charset=UTF-8"
   		});
   		}
     else{
     	jQuery.ajax({
   		type: "post",
   		url: "hotel-orderRecord!recordNextStep.shtml",
   		contentType:"application/x-www-form-urlencoded; charset=UTF-8"
     });
     }
}

//验证开始
function validateAll(){
    var valFellowNames= validateNoSame();
	if(!valFellowNames){
	   return false;
	}
	var valLinkName = jQuery.trim(jQuery("#linkmanName").val());
	if(valLinkName==""){
	   jQuery("#linkmanName").alert("联系人不能为空！");
	   return false;
	}
	var phoneValue = jQuery("#linkmanTelephone").val();
	var valphone = validatePhone(phoneValue);
	if(valphone==false){
	   jQuery("#linkmanTelephone").alert("手机号码是以13、15、18或147开头的11位数字，请正确输入！");
	   return false;
	}
    var emailValue = jQuery("#linkmanEmail").val();
	if(jQuery.trim(emailValue)!=""){
	   var valEmail = validateEmail(emailValue);
	   if(valEmail==false){
	       jQuery("#linkmanEmail").alert("请输入正确的电邮！");
	       return false;
	   }
	}
	var validateDuplication = checkDuplicationOrder();
	if(validateDuplication){
		//alert(validateDuplication);
		return false;
	}
	return true;
}

function validateEmail(str) {
    var pattern = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
    if (!pattern.test(str)) {
        return false;
    }
    return true;
}

function validatePhone(str){
    var pattern=/^(13[0-9]|15[0-9]|18[0-9]|147)\d{8}$/; 
	if (!pattern.test(str)) {
        return false;
    }
    return true;
}
//验证fellow的值不同
function validateNoSame(){
     var fellowNames={};
     jQuery("input[name='fellowName']").each(
          function(i){
	          var thisValue = jQuery.trim(this.value);
			  fellowNames[i] = thisValue;	  
	  });
	  var flagHKGOrMAC = jQuery("#flagHKGORMAC").val();
	  var flag =true;
     jQuery("input[name='fellowName']").each(
          function(i){
		    var thisValue = jQuery.trim(this.value);
		    if(flagHKGOrMAC=="true"){
		        var reg = /(^[A-Za-z .]+\/[A-Za-z .]+$)/;
		        if(!reg.test(thisValue)){
		           jQuery(this).alert("请输入正确的入住人姓名！");
		           flag =false;
		           return flag;
		        }    
		    }
            if(thisValue=="" || thisValue.indexOf('姓名') != -1){
                jQuery(this).alert("请输入正确的入住人姓名！");
				flag = false;
				return flag;
			}			 
           for(var n=0;n<5;n++){
                if(thisValue==fellowNames[n] && n<i){
				    jQuery(this).alert("客户姓名不能相同！");
					flag = false;
					return flag;
				}
		   }		   
	  });
	  return flag;
}

function validateLinkMan(){
    
}

//验证end

function inputCommonValueToSubmitForm(){
    //到店时间
    var timeStr =  jQuery("#arrivalHotelTime").val();
    var timeArray = timeStr.split('-');
	jQuery("#f_arrivalTime").val(timeArray[0]);
    jQuery("#f_latestArrivalTime").val(timeArray[1]);
    	//房间数
	jQuery("#f_roomQuantity").val(jQuery("#roomNumSelect").val());
	jQuery("#f_bedType").val(jQuery("#bedtypeSelect").val());
	//会员信息
	jQuery("#f_linkMan").val(jQuery("#linkmanName").val());
	jQuery("#f_mobile").val(jQuery("#linkmanTelephone").val());
	jQuery("#f_email").val(jQuery("#linkmanEmail").val());
	var fellowNamesValue="";
	   jQuery("input[name='fellowName']").each(
	       function(){
		      fellowNamesValue += this.value+",";
		});
		fellowNamesValue = fellowNamesValue.substr(0,fellowNamesValue.length-1);
		jQuery("#f_linkeManStr").val(fellowNamesValue);
	//特殊要求
	putSpeicalValuesToForm();
	
 }

//面付
function inputSomeValueToPaySubmitForm(){
   inputCommonValueToSubmitForm();
}

//预付
function inputSomeValueToPrepaySubmitForm(){
	inputCommonValueToSubmitForm();
	jQuery("#f_acturalAmount").val(jQuery("#acturalAmount").html());
	if(jQuery("#f_payMethod").val()=="pre_pay" || jQuery("#f_payToPrepay").val()=="true" ){
	   var paymentType = jQuery("input[name='paymentType']:checked").val();
	   jQuery("#f_paymentType").val(paymentType);
       if(paymentType == 4){
          jQuery("#f_onlinePaytype").val(jQuery("input[name='onlinePaytype']:checked").val());
       }	
	}
	
	//会员的积分
	jQuery("#f_useUlmPoint").val("false");
	var pointAmount = jQuery("#pointAmount").html();//该值为积分相当的RMB
    if(pointAmount*100 >= 1){
         jQuery("#f_useUlmPoint").val("true");
    }
    jQuery("#f_ulmPoint").val(pointAmount*100);
    //代金券赋值
    jQuery("#f_usedCoupon").val("false");
    if(jQuery("#couponAmount").html()>0){
        jQuery("#f_usedCoupon").val("true");
    }
    jQuery("#f_ulmCoupon").val(jQuery("#couponAmount").html());
}

//特殊要求的值放到form中
function  putSpeicalValuesToForm(){
   var specialStr = "";
   jQuery('input[name=specialrequireInput]:checked').each(
       function(){
            var oneSpecial = this.value;
	        specialStr += oneSpecial+",";
   });
  var specialrequireTextarea = jQuery("#specialrequireTextarea").val();
  specialStr += specialrequireTextarea;
  jQuery("#f_specialRequest").val(specialStr);
}

function submitPayForm(){
     if(jQuery("#f_payNeedAssure").val()== "true"){
       var url = location.href;
       var fullPath = url.replace("hotel-booking","hotel-check");
       jQuery("#submitForm").attr("action",fullPath);
     }else{
       var url = location.href;
       var fullPath = url.replace("hotel-booking","hotel-complete");
       jQuery("#submitForm").attr("action",fullPath);
     }
    document.submitForm.submit();
}
function submitPrepayForm(){
      var acturalAmount = jQuery("#acturalAmount").html() - 0; //把String类型变为Number型
     if(jQuery("#f_paymentType").val()=="2" && acturalAmount > 0){
      var url = location.href;
      var fullPath = url.replace("hotel-booking","hotel-check");
      jQuery("#submitForm").attr("action",fullPath);
    }
    document.submitForm.submit();
}

//异步记录联系人和联系电话
jQuery('input[name*=linkman]').blur(

	function(){
	var linkmanName=jQuery('#linkmanName').val();
	var linkmanTelephone=jQuery('#linkmanTelephone').val();
	var linmanEmail=jQuery('#linkmanEmail').val();
	var url="hotel-orderRecord.shtml";
    var parms="linkmanName="+linkmanName+"&linkmanTelephone="+linkmanTelephone+"&linkmanEmail="+linmanEmail;
    jQuery.ajax({
   	type: "post",
   	 url: url,
   	data: encodeURI(encodeURI(parms)),
   	contentType:"application/x-www-form-urlencoded; charset=UTF-8"
   });
	}

);
