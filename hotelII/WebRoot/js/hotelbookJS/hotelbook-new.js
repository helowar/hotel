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
	
	document.hotelBookForm.submit();	
	$('#mango-greybox').hide();
	$('#mango-overlay').remove();
	$('embed, object, select').css({ 'visibility' : 'visible' });
	$(window).unbind("scroll").unbind("resize");
}
 
// 点击确定修改时间按钮之后触发的函数
	function changetime(){
	    $('#changetime').unbind('click').click(function(){
	    	formSubmit();
			
		});
	}
    $('#changedatebtn').greybox({closeid:'popclose', callback:changetime});
	
	$('.dateIcon').each(function(i){
	    $(this).click(function(){
		    var parent = $(this).parent();
			if($(parent).children('.calendar')){
			    $(parent).children('.calendar').focus();
			}
			//__ozclk();
			return false;
		});
	});
	
	
//特殊要求的
//jQuery('#showspecialrequire').toggle(
 //   function(){$('#specialrequire').show();},
//	function(){$('#specialrequire').hide();}
//);


  jQuery("#showspecialrequire").click(function(){
  $("#specialrequire").css('display',"block");
  $("#showspecialrequireP").hide();
  });


//酒店设施和服务 更多
   $(".htod_more").each(function(){
       $(this).bind('click',function(){
	    if($(this).hasClass("htod_more")){
	     $(this).attr("class","htod_more1");
		 $(this).prev().css('height',"auto");
		 $(this).prev().css('overflow',"");
		 $(this).children().html("收起");
	   }
	   else{
	     $(this).attr("class","htod_more");
		 $(this).prev().height(98);
		 $(this).prev().css('overflow',"hidden");
		 $(this).children().html("查看更多");
		 }
	   })
   });
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
          var left_value = jQuery(this).offset().left
          jQuery("#fellowNameDiv").css({top:top_value,left:left_value});
          setTimeout("jQuery('#fellowNameDiv').show()",100);//防止在hide前执行           
          var indexValue = jQuery("input[name='fellowName']").index(jQuery(this));
          jQuery("#fellowNameOl").val(indexValue);
    }
});

jQuery("input[name='fellowName']").bind('focus', 
	function (){
	    this.style.color='#000000';
   	if(jQuery(this).val() != null && jQuery(this).val().indexOf('姓名') != -1||jQuery(this).val().indexOf('Lastname')!=-1) {
   		jQuery(this).val("");
   		this.style.color='#000000';
   	}
});

jQuery("input[name='fellowName']").bind('blur', 
   function (){
     var fellowNameString="住客姓名";
     if(jQuery("#flagHKGORMAC").val()=="true"){
     fellowNameString="Lastname/Firstname";
     }
   	if(jQuery(this).val() == null || jQuery(this).val() == ""){
   		jQuery(this).val(fellowNameString);
   		this.style.color='#999999';
   		jQuery(this).css('background-color','');
		jQuery(this).css('border-color','');	  
   	}else{
   	    if(!validateFellowinfo(this)){
   	       jQuery("#alertMes").css('display','');
   	    }
   	    else{
   	     jQuery("#alertMes").hide();
   	    }
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
          jQuery("input[name='fellowName']").eq(indexValue).blur();
     });
//给fellowNameDiv,linkmanNameDiv添加效果  
jQuery("#fellowNameDiv,#linkmanNameDiv").bind('mouseleave',
    function(){
        jQuery(this).hide();
    });  
//历史联系人

jQuery(document.body).bind('click',function(){
   jQuery("#fellowNameDiv").hide();
   jQuery('#linkmanNameDiv').hide();
    if(jQuery('#loginMemberTipDiv').is(":visible")){
   		jQuery('#loginMemberTipDiv').hide();
   }
    if(jQuery('#loginMemberHrefTipDiv').is(":visible")){
   		jQuery('#loginMemberHrefTipDiv').hide();
   }
    if(jQuery('#fellowPersonTipDiv').is(":visible")){
   		jQuery('#fellowPersonTipDiv').hide();
   }
    if(jQuery('#returnCashTipDiv').is(":visible")){
   		jQuery('#returnCashTipDiv').hide();
   }
    if(jQuery('#pointTipDiv').is(":visible")){
   		jQuery('#pointTipDiv').hide();
   }
});

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
jQuery("#linkmanTelephone").bind('blur',function (){
	bindFunction_checkDup();}
);

 //当用户填写完联系电话并移开焦点时，进行订单重复的校验，QC3891 add by zengming 2012-03-15 end

//当用户填写完联系人并移开焦点时，进行订单重复的校验，QC3891 add by zengming 2012-03-24 begin
/*Query("#linkmanName").bind('blur',function (){

checkDuplicationOrder();}
);
*/
 //当用户填写完联系人并移开焦点时，进行订单重复的校验，QC3891 add by zengming 2012-03-24 end

//弹出历史联系人 
 jQuery("#linkmanName").bind('click', 
    function (){
        if(linkmanArray.length>0){
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
     
 
//变化入住人填写框的个数
function changeFellowPersonInputNum(roomNum){
     var fellowNameString="住客姓名";
     if(jQuery("#flagHKGORMAC").val()=="true"){
     fellowNameString="Lastname/Firstname";
     }

      //获取原来的人数
       var frontRoomNum=jQuery("#fellowPersonLabel").children("input").length;
       var fellowPersonInput = jQuery("input[name='fellowName']:first");
       var num=roomNum*2 - frontRoomNum;
       var oldCopyInput;
        if(num > 0){
            for(var i = 0 ; i < num ; i++){
               var copyInput = fellowPersonInput.clone(true);
               copyInput.val(fellowNameString);
               copyInput.attr("style","color:#999;margin-right:2px");
               copyInput.attr("id",'fellowNameId' + (frontRoomNum + i+1));              
               copyInput.insertAfter(jQuery("input[name='fellowName']:last"));
            }
        if(jQuery("input[name='fellowName']").length>5 && frontRoomNum<=5 && jQuery("input[name='fellowName']").length<=10){
          $("#fellowNameId5").after("<br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
          } 
        }
        //移除input
        if(num < 0){
        var inputId="#fellowNameId"+2*roomNum;
        jQuery(inputId).nextAll().remove();
        }
         var maxNum=roomNum*2;
	 	 var information="&nbsp;&nbsp;(至少填"+roomNum+"人,最多填"+maxNum+"人)";
	 	 jQuery("#input_information").html(information);
	 	 
} 

function initLinkMans(){
	var linkManStr=jQuery('#f_linkeManStr').val();
	if(linkManStr!=null){
	var linkMans=linkManStr.split(",");
	for(var i=0;i<linkMans.length;i++){
	    //if(!(/^房间.住客姓名$/.test(jQuery('#fellowNameId'+(i+1)).val())))
	   
	      if(!(linkMans[i]==null || linkMans[i].trim()=='')){
	     	jQuery('#fellowNameId'+(i+1)).val(linkMans[i]);
		}
		
		
	}
	}
}



//检查是否有重复订单，QC3891 add by zengming 2012-03-15 begin

function bindFunction_checkDup(){
	//var valLinkMan = validateLinkMan();
	var phoneValue = jQuery("#linkmanTelephone").val();
	var valphone = blurValidatePhone(phoneValue);
	//if(valLinkMan&&valphone){
	if(valphone){
		checkDuplicationOrder();
	}
	
}


function checkDuplicationOrder(){

	var iframeUrl='';
	var result=false;
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
        //timeout:20000, //设置请求超时时间(毫秒)
   		async: false,
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
                //jQuery("#submitOrder").attr('disabled',true); 
                jQuery("#submitOrder").attr('disabled',true); 
	 			jQuery("#submitOrder").addClass('show_grey');
        		//noticeDuplication(iframeUrl);
                OpenMyModal(iframeUrl,width,height);
        		result = true;
        	}else{
        		jQuery("#submitOrder").removeAttr('disabled'); 
        		jQuery("#submitOrder").attr('class','');
        	}
        }
   });
	return result;
}

function OpenMyModal(src, width, height) {
  var frame = '<iframe width="' + width + '" height="' + height + '"src="' + src + '" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes"></iframe>';
  var option = {
      escClose: false,
      close: false,
      minHeight: height,
      minWidth: width,
      autoResize: true,
      position : ["25%"],
	  overlayId : 'contact-overlay',
	  containerId: 'contact-container',
	  closeClass : ('closeMode'),
	  overlayClose: false
  };
  //$("loadNoticDuplication").html(frame);
  //$("id_NoticDuplication").modal(option);
  $.modal(frame, option);
}
//检查是否有重复订单，QC3891 add by zengming 2012-03-15 end


function changeAllPriceAndReturnCash(roomNum){
    var allPriceSum = Math.ceil(allpriceSumForOneRoom *roomNum);
	var retrunCashSum = Math.floor(returnCashForOneRoom *roomNum);
	jQuery("#allPriceSum").html(allPriceSum);
	jQuery("#returnCash").html(retrunCashSum);
}
jQuery("#roomNumSelect").change(
   function(){
    var roomNum = jQuery("#roomNumSelect").val();
    changeFellowPersonInputNum(roomNum);
    changeAllPriceAndReturnCash(roomNum);
    accessAssure();
	}
);




//验证开始
function validateAll(){
    var valFellowNames= validateNoSame();
    var byteLinkManLen=0;
	if(!valFellowNames){
	   jQuery("#alertMes").css('display','');
	   return false;
	}
	var valLinkName = jQuery.trim(jQuery("#linkmanName").val());
	if(valLinkName==""){
	   //jQuery("#linkmanName").alert("联系人不能为空！");
	   jQuery('#linkmanNameMes').text("您还没有填写联系人姓名。请填写正确的姓名，以便我们后续能更快与您联系！");
       errerMessageLinkman('linkmanName');
	   return false;
	}
	/**
	   校验联系人
	**/
    if(!validateLinkman(valLinkName)){
       return false;
    }
			
	var phoneValue = jQuery("#linkmanTelephone").val();
	var valphone = validatePhone(phoneValue);
	if(valphone==false){ 
	  // jQuery("#linkmanTelephone").alert("手机号码是以13、15、18或147开头的11位数字，请正确输入！");	  
	   return false;
	}
    
	   var valEmail = validateEmail();
	   if(valEmail==false){
	       //jQuery("#linkmanEmail").alert("请输入正确的电邮！");	     
	       return false;
	   }
	
    var checkDupResult = checkDuplicationOrder();
	if(checkDupResult){
		return false;
	}
	return true;
}

function validateEmail() {
var emailValue = jQuery("#linkmanEmail").val();
if(jQuery.trim(emailValue)!=""){
    var pattern = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
    if (!pattern.test(emailValue)) {
    
          jQuery('#linkmanNameMes').text("请输入正确的电子邮箱！");
	      errerMessageLinkman('linkmanEmail');
        return false;
    }
    else{
      jQuery("#linkmanEmail").css('background-color','');
      jQuery("#linkmanEmail").css('border-color','');
      jQuery('#linkmanMes').hide();
      rightMessageLinkman('linkmanEmail');
      return true;
    }
    }
  else{
  	  jQuery("#linkmanEmail").css('background-color','');
      jQuery("#linkmanEmail").css('border-color','');
      jQuery('#linkmanMes').hide();
      return true;
  }
}

function validatePhone(str){

    var pattern=/^(13[0-9]|15[0-9]|18[0-9]|147)\d{8}$/; 
	if (!pattern.test(str)) {
       jQuery('#linkmanNameMes').text('手机号码是以13、15、18或147开头的11位数字，请正确输入！');
       errerMessageLinkman('linkmanTelephone');
       return false;	
    }
    else{
      rightMessageLinkman('linkmanTelephone');
    }
    return true;
}
//验证fellow的值不同
function validateNoSame(){
     var count=0;
     var fellowNames={};
     jQuery("input[name='fellowName']").each(
          function(i){
          if(jQuery.trim(this.value)!=null && jQuery.trim(this.value)!=''&& jQuery.trim(this.value).indexOf('住客姓名') == -1&&jQuery.trim(this.value).indexOf('Lastname') == -1){
	          var thisValue = jQuery.trim(this.value);
			  fellowNames[i] = thisValue;	  
			  }
		else{
		      count++;
		 }
	  });
	  if(count > jQuery('#roomNumSelect').val())
	  {
	  jQuery('#fellowPersonAlertMes').text('您填写的住客姓名少于您的预订间数!');
	  return false;
	  }
	  var flagHKGOrMAC = jQuery("#flagHKGORMAC").val();
	  var flag =true;
     jQuery("input[name='fellowName']").each(
          function(i){
            var fellowNameVal=this.value;
		    var thisValue = jQuery.trim(this.value);
		    var reg1=/^[\u4e00-\u9fa5 A-Za-z]*$/;
		    var byteValLen=0;
		    if(flagHKGOrMAC=="true"){
		   
		        var reg = /(^[A-Za-z .]+\/[A-Za-z .]+$)/;
		        if(!reg.test(thisValue)){
		           //jQuery(this).alert("请您填写与入住人所持证件完全一致的姓名！");
		            jQuery('#fellowPersonAlertMes').text('入住人填写格式错误，请参考填写规则');
		           flag =false;
		        }    
		    }
		    if(flagHKGOrMAC!="true"){
		    if(thisValue.indexOf('住客姓名') == -1&&thisValue.indexOf('Lastname') == -1 && !reg1.test(thisValue) && !(/(^[A-Za-z .]+\/[A-Za-z .]+$)/.test(thisValue))){	
		             
		          // jQuery(this).alert("请您填写与入住人所持证件完全一致的姓名！");
		           jQuery('#fellowPersonAlertMes').text('入住人填写格式错误，请参考填写规则');
		           flag =false;
		        }  
		    }
            if(fellowNameVal.length>0 && thisValue.length==0){
                //jQuery(this).alert("请您填写与入住人所持证件完全一致的姓名！");
                 jQuery('#fellowPersonAlertMes').text('入住人填写格式错误，请参考填写规则');
				flag = false;
			}
			 if(thisValue.indexOf('房间N住客姓名') != -1){
                //jQuery(this).alert("您还没有填写入住人姓名！");
                 jQuery('#fellowPersonAlertMes').text('您还没有填写入住人姓名！如果您是帮朋友预订，请填写您朋友的姓名。');
				flag = false;
			}
			/*if(thisValue.indexOf('住客姓名') != -1){
                //jQuery(this).alert("您还没有填写入住人姓名！");
                 jQuery('#fellowPersonAlertMes').text('您还没有填写入住人姓名！如果您是帮朋友预订，请填写您朋友的姓名。');
				flag = false;
			}*/
			if(thisValue.length==1){
			//jQuery(this).alert("请您填写与入住人所持证件完全一致的姓名！");
			 jQuery('#fellowPersonAlertMes').text('入住人填写格式错误，请参考填写规则');
				flag = false;
			}
			
			for (var k = 0; k < thisValue.length; k++) {  
				if (thisValue.charAt(k).match(/[^\x00-\xff]/ig) != null)    
				byteValLen += 3;    
				else    
				byteValLen += 1;
			}    
			
			if(byteValLen>60){
			//jQuery(this).alert("请您填写与入住人所持证件完全一致的姓名！");
			 jQuery('#fellowPersonAlertMes').text('入住人填写格式错误，请参考填写规则');
				flag = false;
			}
			
		   for (var j = 0; j < fellowNameVal.length; j++) {  
		      var c=1;
		      for(var t=1;t<=4;t++){
		         if(fellowNameVal.charAt(j)==fellowNameVal.charAt(j+t)){
		         c++;
		         }
		      }
		      if(c==4){
		       // jQuery(this).alert("请您填写与入住人所持证件完全一致的姓名！");
		        jQuery('#fellowPersonAlertMes').text('入住人填写格式错误，请参考填写规则');
				flag = false;
		      }
		   }
						 
           for(var n=0;n<5;n++){
                if(thisValue==fellowNames[n] && n<i){
				   // jQuery(this).alert("您填写的入住人有重复，请修正！");
				    jQuery('#fellowPersonAlertMes').text('您填写的入住人有重复，请修正！如果您是帮朋友预订，请填写您朋友的姓名。');
					flag = false;
				}
		   }
		   
		   if(flag){
		     jQuery(this).css('background-color','');
		     jQuery(this).css('border-color','');	   
		   }else{
		     jQuery(this).css('background-color','#FFFCD1');
		     jQuery(this).css('border-color','red');
		     return flag;
		   }
		   
	  });	 
	  return flag;
}
//验证end
  
function inputCommonValueToSubmitForm(){
    //到店时间
    //var timeStr =  jQuery("#arrivalHotelTime").val();
    //var timeArray = timeStr.split('-');
    var latestArrivalTime=jQuery("input[name*='checkInTime']:checked").val(); 
	jQuery("#f_arrivalTime").val(timePast(latestArrivalTime,2));
	var checkInTimeS=document.getElementById("checkInTime1");
	if('01:00'==latestArrivalTime && checkInTimeS.checked){
		jQuery("#f_latestArrivalTime").val('01:00');
   		 setLastestArrivalDate();
	}else{
    jQuery("#f_latestArrivalTime").val(latestArrivalTime);
    }
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
	       if(this.value!=null && this.value!='' && this.value.indexOf('住客姓名')==-1 && this.value.indexOf('Lastname')){
		      fellowNamesValue += this.value+",";
		      }
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
  var oldSpecialRequest=jQuery("#f_specialRequest").val();
  specialStr=oldSpecialRequest+specialStr;
  jQuery("#f_specialRequest").val(specialStr);
}

function submitPayForm(){  
     if(jQuery("#f_payNeedAssure").val()== "true"){
       var url = location.href;
       var fullPath = url.replace("hotel-booking","hotel-check");
       jQuery("#submitForm").attr("action",fullPath);
     }
   
     else if(jQuery("#f_payMethod").val()=='pre_pay'){
     	var url = location.href;
       var fullPath = url.replace("hotel-booking","hotel-check");
       jQuery("#submitForm").attr("action",fullPath);
     }
     
     else if(jQuery("#f_payMethod").val()=='pay'&&jQuery("#payToPrepay").val()=='true'){
       var url = location.href;
       var fullPath = url.replace("hotel-booking","hotel-check");
       jQuery("#submitForm").attr("action",fullPath);
     }
     
     else{
       var url = location.href;
       var fullPath = url.replace("hotel-booking","hotel-complete"); 
       jQuery("#submitForm").attr("action",fullPath);
     }
         
    document.submitForm.submit();
}

//提交按钮	
function paySubmitOrder(){
    var validateFlag  =  validateAll();
    if(!validateFlag){
 			return;
 	}
 	else{
 		var mbrId=getCookieHistory("mbrID");
		var membercd=getCookieHistory("membercd");
 		var returnAmount = document.getElementById("f_returnAmount").value;
 		if(returnAmount==0.0){//无返现不弹出选择框，直接提交订单
 			submitOrderForm(2);
 		}else{
			var linkmanTelephone=document.getElementById("linkmanTelephone").value;
			var linkmanEmail=document.getElementById("linkmanEmail").value;
			var url ='hotel-booking!getMemberShipList.shtml';   
			var params = "mbrId="+mbrId+"&telephone="+linkmanTelephone+"&email="+linkmanEmail;
			jQuery.ajax({
		   		type: "post",
		   		dataType:'json',
				contentType:'application/x-www-form-urlencoded',
		   		url: url,
		   		data: encodeURI(encodeURI(params)),
		   		async: true,
		        error:function (XmlHttpRequest, textStatus, errorThrown){
		        	if(XmlHttpRequest.status!='200'&&XmlHttpRequest.status!='0'){
		        		alert(XmlHttpRequest.status);
		                alert(XmlHttpRequest.responseText);
		        	}
		        },
		        success:function(result){ //请求成功后回调函数
		        	var mbrhipListStr="";
		    		var flag=false;
		    		clearDiv();//加载之前先清空，防止重复点击
		        	if(typeof(result)!="undefined" &&  result.length>0){
		        		$('#choosetitle').append("请选择会籍");
		        		$('#chooseTable').append("<tr><td colspan='3' align='left' style='border:0;height:35px;color:#595959;'>您已是芒果网会员，为保证您的会员权益，请选择本次预订使用的会籍：</td></tr>"+
		 	    	 		                                   "<tr><th  align='left'>会籍名称</th><th width='80'>会籍编号</th><th width='100'>是否享受返现</th></tr>");
		        		for(var i=0;i<result.length;i++){
		            		if(result[i].returnCash){
		            		flag= result[i].returnCash;
		            		}
		            		var returncashStr;
		            		if(result[i].returnCash){
		            			returncashStr = "是";
		            		}else{
		            			returncashStr = "否";
		            		}
		        			if(i==0){
		        				$('#chooseTable').append("<tr><td align='left'><input type='radio' name='radiobutton' id='mbrradio_"+i+"' value='"+result[i].oldMbrshipCd+"-"+result[i].mbrId+"-"+result[i].returnCash+"'  checked>"+result[i].categoryName+
		        						                         "</td><td>"+result[i].oldMbrshipCd+"</td><td>"+returncashStr+"</td></tr>");
		        			}else{
		        				if(membercd==result[i].oldMbrshipCd){
		        					jQuery("#mbrradio_0").attr("checked",false);
		        					$('#chooseTable').append("<tr><td align='left'><input type='radio' name='radiobutton' id='mbrradio_"+i+"' value='"+result[i].oldMbrshipCd+"-"+result[i].mbrId+"-"+result[i].returnCash+"' checked>"+result[i].categoryName+
					                         "</td><td>"+result[i].oldMbrshipCd+"</td><td>"+returncashStr+"</td></tr>");
		        				}else{
		        				$('#chooseTable').append("<tr><td align='left'><input type='radio' name='radiobutton' id='mbrradio_"+i+"' value='"+result[i].oldMbrshipCd+"-"+result[i].mbrId+"-"+result[i].returnCash+"'>"+result[i].categoryName+
				                                                "</td><td>"+result[i].oldMbrshipCd+"</td><td>"+returncashStr+"</td></tr>");	
		        				}
		        			}
		        			
		        		}
		        		if(flag){
		        			$('#chooseTable').append("<tr><td style='border:0;height:20px;' colspan='3'></td></tr>");
		        			$('#choosebtdiv').append("<input type='button' class='buttonbg' id='submitmbr' value='已选择好会籍' style='width:87px;height:26px;' onclick='submitOrderForm(2,this);'/>");
		        		}else{
		        			$('#chooseTable').append("<tr><td style='border:0;height:50px;' colspan='3'><div style='width:385px;color:#e50110;text-align:left;line-height:18px;height: 45px;'>您目前的会籍不能享受返现，如需享受返现，请点击“确认注册”，芒果网将自动帮您注册一个新会籍。</div></td></tr>");	
		            		$('#choosebtdiv').append("<div style='width:270px;padding-bottom:10px;'><input  class='buttonbg' type='button' value='确认注册' style='float:left;width:64px;height:25px;' onclick='submitOrderForm(1,this);' />"+
		            				                               "<input class='buttonbg' type='button' value='不注册，已选择好会籍' style='float:right;width:134px;height:25px;'  onclick='submitOrderForm(2,this);' /></div>");
		        		}	
		        	}else{
		        		$('#choosetitle').append("您尚未注册芒果网会员");
		        		$('#chooseTable').append("<tr><td width='100%' style='border:0;font-size:14px;color:#e50110;height:68px;line-height:20px;'>此手机或邮箱尚未注册为芒果网会员，不能享受返现，如需享受返现，请点击“确认注册”，芒果网将自动帮您注册为会员。</td></tr>");
		        		$('#choosebtdiv').append("<div style='width:270px;padding-bottom:10px;'><input class='buttonbg' type='button' value='确认注册' style='float:left;width:64px;height:25px;' onclick='submitOrderForm(1,this);' />"+
		        				                               "<input type='button' value='不注册，直接预订' class='buttonbg' style='float:right;width:134px;height:25px;'  onclick='submitOrderForm(3,this);' /></div>");
		        	}
		          //点击确定修改时间按钮之后触发的函数
		            function choosembr(){
		            }
		        	$('#submitOrder').mbrgreybox({closeid:'closesp', callback:choosembr});
		        }
		   });
	}
	}
}  



//确定提交订单
function submitOrderForm(isRegister,obj){
 		 inputSomeValueToPaySubmitForm();
 		 jQuery("#isRegisterMember").val(isRegister);//1.注册并用新会籍下单；2.当前会籍下单；3.公共会籍下单
 		 var returnAmount = document.getElementById("f_returnAmount").value;
 		 if("2"==isRegister && returnAmount != 0.0){
 			var tempStr= $(":radio[name='radiobutton']:checked").val(); 
 			var tempArray = new Array();
 			if("null" != tempStr && "" != tempStr){
 				tempArray = tempStr.split('-');
 			}
 			if(tempArray.length>1){
 			jQuery("#memberCd").val(tempArray[0]);
 			jQuery("#mbrID").val(tempArray[1]);
 			jQuery("#isReturnCash").val(tempArray[2]);
 			}
 		 }
 		  submitPayForm();
 		  obj.disabled = true;
          obj.className="show_grey"; 
}
//时间提前2个小时
function timePast(time,hourNum){
	var pastTime='00:00';
	if(time=='01:00'){
		pastTime='23:00';
	}
	else{
		var temptime=Number(time.split(":")[0]);
		if(temptime==1){
			 pastTime = '23:00';
		}
		else if(temptime==2){
		 	pastTime = '23:59';
		}
		else{
   		var hour = temptime - hourNum;   		
  		 pastTime = hour+":00";
  		 }
   }
   return pastTime;
}

function checkLinkmanPhoneDup(){
	 var pattern=/^(13[0-9]|15[0-9]|18[0-9]|147)\d{8}$/; 
	 var phone=jQuery('#linkmanTelephone').val();
	if (pattern.test(phone)) {
		return true;
	}
	else{
		return false;
	}
}


jQuery('#linkmanName').blur(

	function(){
	var byteLinkManLen=0;  					
	var linkmanName=jQuery('#linkmanName').val();
	if(validateLinkman(linkmanName)){
	         jQuery(this).css('background-color','');
		     jQuery(this).css('border-color','');	
		     jQuery('#linkmanMes').hide();
		     rightMessageLinkman('linkmanName');
		     
		     var fagCheck=jQuery('#linkmanNameDiv').is(":visible");	
		     if(checkLinkmanPhoneDup()&&!fagCheck){ 
		     	checkDuplicationOrder();
		     }	     
    }else{   
     errerMessageLinkman('linkmanName');
    }

	}
	
);
  //促销信息 
  jQuery('#promoSaleText').hover(
	function (){accesssMouseOverTipDiv('promoSaleText','promoSaleTipDiv',-12,20);},
	function (){hideMouseOverTipDiv('promoSaleTipDiv');}
	);

  
  //酒店级别提示信息
  if(document.getElementById("hotelAlertMsg")!=null){
	  jQuery('#hotelAlertMsg').hover(
			function (){accesssMouseOverTipDiv('hotelAlertMsg','hotelAlertMsgDiv',-12,20);},
			function (){hideMouseOverTipDiv('hotelAlertMsgDiv');}
	  );
  }

  
  
 //登录会员提示
  jQuery('#loginMember').hover(
	function (){accesssMouseOverTipDiv('loginMember','loginMemberTipDiv',20,38);},
	function (){hideMouseOverTipDiv('loginMemberTipDiv');
	}
	);

  jQuery('#loginMemberHref').hover(
	function (){accesssMouseOverTipDiv('loginMemberHref','loginMemberHrefTipDiv',12,13);},
	function (){hideMouseOverTipDiv('loginMemberHrefTipDiv');
	}
	);


 //港澳酒店入住人填写规则，提示
	jQuery('#fellowPersonText').hover(
	function (){accesssMouseOverTipDiv('fellowPersonText','fellowPersonTipDiv',-14,11);},
	function (){hideMouseOverTipDiv('fellowPersonTipDiv');}
	);
	//代金券提示
	
	//jQuery('#prepayCouponText').hover(
	//function (){accesssMouseOverTipDiv('prepayCouponText','prepayCouponTipDiv',50,44);},
	//function (){hideMouseOverTipDiv('prepayCouponTipDiv');}
	//);	
	
	//返现提示
	jQuery('#returnCashText').hover(
	function (){accesssMouseOverTipDiv('returnCashText','returnCashTipDiv',50,44);},
	function (){hideMouseOverTipDiv('returnCashTipDiv');}
	);
	
	//积分提示
	jQuery('#pointText').hover(
	function (){accesssMouseOverTipDiv('pointText','pointTipDiv',50,44);},
	function (){hideMouseOverTipDiv('pointTipDiv');}
	);
	
 	//用于显示提示信息	
	function accesssMouseOverTipDiv(targetId,showTipDivId,changeLeftPx,changeTopPx){   
	var objectDiv=jQuery('#'+showTipDivId);

   var  leftPx = jQuery('#'+targetId).offset().left;
   var  topPx = jQuery('#'+targetId).offset().top;
  
    objectDiv.fadeIn();	
	var divWidth = objectDiv.width();
	var divHeight = objectDiv.height();
	var objectLeft=leftPx+changeLeftPx;
	var objectTop=topPx+changeTopPx;
	objectDiv.css({
	    'left' : leftPx+changeLeftPx,
		'top' : objectTop
	});
		
	 objectDiv.show();	
	
}


//隐藏提示
function hideMouseOverTipDiv(howTipDivId){
    $('#'+howTipDivId).hide();	
}

  jQuery('#fellowPersonTipDiv').hover(function (){showAndHideElement('fellowPersonTipDiv');},function(){jQuery('#fellowPersonTipDiv').hide(); });
  
  jQuery('#promoSaleTipDiv').hover(function (){showAndHideElement('promoSaleTipDiv');},function(){jQuery('#promoSaleTipDiv').hide(); });
 
  function showAndHideElement(id){
    var element=jQuery('#'+id);
    if(element.is(":hidden")){
    	element.show();
    }  
  }
  

function validateLinkman(obj){   
    var flag=true;
    var byteLinkManLen=0;
    var linkmanName=obj;
    for (var h = 0; h < linkmanName.length; h++) {  
				if (linkmanName.charAt(h).match(/[^\x00-\xff]/ig) != null)    
				byteLinkManLen += 3;    
				else    
				byteLinkManLen += 1;
		} 
		var reg1=/^[\u4e00-\u9fa5 A-Za-z]*$/;
    if(byteLinkManLen>60){
	//jQuery('#linkmanName').alert("您填写的姓名存在非法字符。请填写正确的姓名，以便我们后续能更快与您联系！");
	jQuery('#linkmanNameMes').text("您填写的姓名存在非法字符。请填写正确的姓名，以便我们后续能更快与您联系！");
	jQuery('#linkmanName').val("");

	}
	
	 if(!(/^[\u4e00-\u9fa5 A-Za-z]*$/.test(linkmanName)) && !(/(^[A-Za-z .]+\/[A-Za-z .]+$)/.test(linkmanName))){
	 			      
		           //jQuery('#linkmanName').alert("您填写的姓名存在非法字符。请填写正确的姓名，以便我们后续能更快与您联系！");
		           jQuery('#linkmanNameMes').text("您填写的姓名存在非法字符。请填写正确的姓名，以便我们后续能更快与您联系！");
		           return false;
	
     }  

	if(linkmanName.length==1){
			//jQuery('#linkmanName').alert("您填写的姓名存在非法字符。请填写正确的姓名，以便我们后续能更快与您联系！");
			jQuery('#linkmanNameMes').text("您填写的姓名存在非法字符。请填写正确的姓名，以便我们后续能更快与您联系！");
			return false;
	}
	
	if(linkmanName.length>0 && jQuery.trim(linkmanName)==""){
                //jQuery('#linkmanName').alert("您填写的姓名存在非法字符。请填写正确的姓名，以便我们后续能更快与您联系！");
                jQuery('#linkmanNameMes').text("您填写的姓名存在非法字符。请填写正确的姓名，以便我们后续能更快与您联系！");
                return false;
	}
	

	
	for (var j = 0; j < linkmanName.length; j++) {  
		      var c=1;
		      for(var t=1;t<=4;t++){
		         if(linkmanName.charAt(j)==linkmanName.charAt(j+t)){
		         c++;
		         }
		      }
		      if(c==4){
		        //jQuery('#linkmanName').alert("您填写的姓名存在非法字符。请填写正确的姓名，以便我们后续能更快与您联系！");
		        jQuery('#linkmanNameMes').text("您填写的姓名存在非法字符。请填写正确的姓名，以便我们后续能更快与您联系！");		  
				flag = false;
		      }
		   }  
   return flag;
}

function validateFellowinfo(obj){
	  var num=jQuery(obj).attr('id').split('Id')[1];
	  var fellowNames={};
	  var flagHKGOrMAC = jQuery("#flagHKGORMAC").val();
	  var flag =true;
	  var fellowNameVal=obj.value;
	  var thisValue = jQuery.trim(obj.value);
	  var reg1=/^[\u4e00-\u9fa5 A-Za-z]*$/;
	  var byteValLen=0;
	     jQuery("input[name='fellowName']").each(
	          function(i){
		          var thisValue = jQuery.trim(this.value);
				  fellowNames[i] = thisValue;	  
		  });

	    if(flagHKGOrMAC=="true"){
	        var reg = /(^[A-Za-z .]+\/[A-Za-z .]+$)/;
	        if(!reg.test(thisValue)){
	          // jQuery(obj).alert("请您填写与入住人所持证件完全一致的姓名！");
	          jQuery('#fellowPersonAlertMes').text('入住人填写格式错误，请参考填写规则');
	         
	           flag =false;
	        }    
	    }
	  
	    if(flagHKGOrMAC!="true"){
	    if(thisValue.indexOf('住客姓名') == -1 && !reg1.test(thisValue) && !(/(^[A-Za-z .]+\/[A-Za-z .]+$)/.test(thisValue))){
	          // jQuery(obj).alert("请您填写与入住人所持证件完全一致的姓名！");

	          jQuery('#fellowPersonAlertMes').text('入住人填写格式错误，请参考填写规则');
	           flag =false;
	        }  
	    }
	          if(fellowNameVal.length>0 && thisValue.length==0){
	              //jQuery(obj).alert("请您填写与入住人所持证件完全一致的姓名！");
	             jQuery('#fellowPersonAlertMes').text('入住人填写格式错误，请参考填写规则');
			flag = false;
		}
		 if(thisValue.indexOf('房间N住客姓名') != -1){
	              //jQuery(obj).alert("您还没有填写入住人姓名！");
	         jQuery('#fellowPersonAlertMes').text('您还没有填写入住人姓名！如果您是帮朋友预订，请填写您朋友的姓名。');
			flag = false;
		}
		if(thisValue.indexOf('姓名') != -1){
	              //jQuery(obj).alert("请您填写与入住人所持证件完全一致的姓名！");
	          jQuery('#fellowPersonAlertMes').text('入住人填写格式错误，请参考填写规则');
			flag = false;
		}
		if(thisValue.length==1){
		//jQuery(obj).alert("请您填写与入住人所持证件完全一致的姓名！");
		 jQuery('#fellowPersonAlertMes').text('入住人填写格式错误，请参考填写规则');
			flag = false;
		}
		
		for (var k = 0; k < thisValue.length; k++) {  
			if (thisValue.charAt(k).match(/[^\x00-\xff]/ig) != null)    
			byteValLen += 3;    
			else    
			byteValLen += 1;
		}    
		
		if(byteValLen>60){
		//jQuery(obj).alert("请您填写与入住人所持证件完全一致的姓名！");
		jQuery('#fellowPersonAlertMes').text('入住人填写格式错误，请参考填写规则');
			flag = false;
		}
		
	   for (var j = 0; j < fellowNameVal.length; j++) {  
	      var c=1;
	      for(var t=1;t<=4;t++){
	         if(fellowNameVal.charAt(j)==fellowNameVal.charAt(j+t)){
	         c++;
	         }
	      }
	      if(c==4){
	       // jQuery(obj).alert("请您填写与入住人所持证件完全一致的姓名！");
	       jQuery('#fellowPersonAlertMes').text('入住人填写格式错误，请参考填写规则');
			flag = false;
	      }
	   }	
	   		 
       for(var n=0;n<num-1;n++){
               if(thisValue==fellowNames[n]){
			    //jQuery(obj).alert("您填写的入住人有重复，请修正！");
			    jQuery('#fellowPersonAlertMes').text('您填写的入住人有重复，请修正！如果您是帮朋友预订，请填写您朋友的姓名。');
				flag = false;
			}
	   }
		   

	 if(flag){
		     jQuery(obj).css('background-color','');
		     jQuery(obj).css('border-color','');	   
		   }else{
		     jQuery(obj).css('background-color','#FFFCD1');
		     jQuery(obj).css('border-color','red');
		     flag = false;
		   }
		    

	  return flag;
}
  
  	 //初始化最晚到店时间的单击事件
  	 function initLatestArriveTime(){	
	 //	var assureType=${assureInfoVO.assureType};
	    var checkInTimeObject=document.getElementById("checkInTime1");
	    if(checkInTimeObject.checked){
	    	changeHintPoint("24:00","htchkin_point01");
	    }
	 	//if(jQuery("input[name*='checkInTime']:checked").val() == "24:00"){
	 	//    changeHintPoint("24:00","htchkin_point01");
	 	//}	
	 	 jQuery("#checkInTime0").click(
	 	 	function(){
	 	 		var latestArriveTimeValue=jQuery("input[name*='checkInTime']:checked").val();	 	 		
	 	 		if(latestArriveTimeValue!=null){
	 	 			changeHintPoint(latestArriveTimeValue,"htchkin_point");
	 	 			setSecondLatestTimehint('checkInTime0');
	 	 			if(assureType==1){
		 				unconditionAssure();
	 				}
	 				
	 	 			if(assureType==2){
	 	 				initTimeAssure();
	 	 			}
	 	 			
	 	 			if(assureType==3){
	  					roomAssure();
	  			}
	  
				  if(assureType==4){
					 nigthAssure();
					}
					
	 	 			if(assureType==5){
	 	 				timeAndRoomAssure();
	 	 			}	 		 	 		
	 	 		} 	 	
	 	 	}	 	 
	 	 );
	 	 jQuery("#checkInTime1").click(
	 	 	function(){
	 	 		var latestArriveTimeValue=jQuery("input[name='checkInTime']:checked").val();	 	 		 	 		
	 	 		if(latestArriveTimeValue!=null){ 	 			
	 	 			changeHintPoint(latestArriveTimeValue,"htchkin_point");
	 	 			setSecondLatestTimehint('checkInTime1');
	 	 			if(assureType==1){
		 				unconditionAssure();
	 				}
	 				
	 	 			if(assureType==2){	 	 			
	 	 				initTimeAssure();
	 	 			}
	 	 			
	 	 			if(assureType==3){
	  				roomAssure();
	  			}
	  			
				  if(assureType==4){
					 nigthAssure();
					}
	 	 			if(assureType==5){
	 	 				timeAndRoomAssure();
	 	 			}	 	 	 		
	 	 		} 	 	
	 	 	}	 	 
	 	 );	 
	 	  jQuery("#checkInTime2").click(
	 	 	function(){
	 	 		var latestArriveTimeValue=jQuery("input[name*='checkInTime']:checked").val();	 	 		
	 	 		if(latestArriveTimeValue!=null){
	 	 			changeHintPoint(latestArriveTimeValue,"htchkin_point");
	 	 			setSecondLatestTimehint('checkInTime2');
	 	 			if(assureType==1){
		 				unconditionAssure();
	 				}
	 				
	 	 			if(assureType==2){
	 	 				initTimeAssure();
	 	 			}
	 	 			
	 	 			if(assureType==3){
	  					roomAssure();
	  			}
	  			
				  if(assureType==4){
					 nigthAssure();
					}
					
	 	 			if(assureType==5){
	 	 				timeAndRoomAssure();
	 	 			}	 	 	 		
	 	 		} 	 	
	 	 	}	 	 
	 	 );
	 	  jQuery("#checkInTime3").click(
	 	 	function(){
	 	 		var latestArriveTimeValue=jQuery("input[name*='checkInTime']:checked").val();	 	 		
	 	 		if(latestArriveTimeValue!=null){
	 	 			changeHintPoint(latestArriveTimeValue,"htchkin_point");
	 	 			setSecondLatestTimehint('checkInTime3');
	 	 			if(assureType==1){
		 				unconditionAssure();
	 				}
	 				
	 	 			if(assureType==2){
	 	 				initTimeAssure();
	 	 			}
	 	 			
	 	 			if(assureType==3){
	  					roomAssure();
	  			}

				  if(assureType==4){
					 nigthAssure();
					}

	 	 			if(assureType==5){
	 	 				timeAndRoomAssure();
	 	 			}		 	 		
	 	 		} 	 	
	 	 	}	 	 
	 	 );
	 }
	 
	 //改变选择了最晚到店时间的提示
	 function changeHintPoint(latestArriveTimeValue,cssstyle){	        
	 		if(latestArriveTimeValue!=null){			
	 			//jQuery('#lastestArrirveTimeText').text(latestArriveTimeValue);
	 			//jQuery('#lastestArrirveTimeText1').text(latestArriveTimeValue);
	 			jQuery("#lastestArrirveTime").attr("class",cssstyle);
	 			jQuery("#lastestArrirveTime").show();	 			
	 		}
	 }
	
	 function simpleHiddenDiv(id){
	      jQuery('#'+id).hide();
	 }  	 
	 //超房担保
	 function roomAssure(){	 
		
	 	var selectRoomNum=jQuery("#roomNumSelect").val();
	 	if(selectRoomNum > maxRoomNum){
	 		jQuery("#f_payNeedAssure").val(true);
	 		var assureMoney=countAssureMoneySum();
	 		var hint='<lable>预订该房型超过'+maxRoomNum+'间房以上，需提供信用卡担保。担保金额为<strong class="orange" style="font-family:Arial">&yen;'+assureMoney+'元</strong><lable>';
	 		jQuery("#submitOrder").val("下一步");
	 		jQuery("#assureHint").html(hint);
	 		
	 		$("#htchkin_point_text").css("display","none");
	  	$("#htchkin_point_text_asure").css("display","block");	
	 	}
	 	else{
	 		jQuery("#assureHint").html("");
	 		jQuery("#f_payNeedAssure").val(false);
	 		jQuery("#submitOrder").val("提交订单");
	 		
	 		$("#htchkin_point_text").css("display","block");
	  	$("#htchkin_point_text_asure").css("display","none");	
	 		}
	 }
	 
	 //超间夜担保
	 function nigthAssure(){	 		 		 	
	 		var selectRoomNum=jQuery("#roomNumSelect").val();
	 		var nightNum=selectRoomNum*allDays;	
	 		if(nightNum > maxNightNum){	 		
	 			jQuery("#f_payNeedAssure").val(true);
	 			var assureMoney=countAssureMoneySum();
	 			var hint='预订该房型超过'+maxNightNum+'间夜以上，需提供信用卡担保。担保金额为<strong class="orange" style="font-family:Arial">&yen;'+assureMoney+'元</strong>';
	 		jQuery("#submitOrder").val("下一步");
	 		jQuery("#assureHint").html(hint);
	 		
	 		$("#htchkin_point_text").css("display","none");
	  	$("#htchkin_point_text_asure").css("display","block");	
	 		}
	 		else{
	 		jQuery("#assureHint").html("");
	 		jQuery("#f_payNeedAssure").val(false);
	 		jQuery("#submitOrder").val("提交订单");
	 		
	 		$("#htchkin_point_text").css("display","block");
	  	$("#htchkin_point_text_asure").css("display","none");	
	 		}	
	 }

	 //初始超时担保
    function initTimeAssure(){  	  	
	   if(jQuery("#checkInTime1:checked").val()!=null){
	  			jQuery("#f_payNeedAssure").val(true);
	  			timeAssureHint();
	  			jQuery("#submitOrder").val("下一步");				  						
	  		}else{
	  			 jQuery("#f_payNeedAssure").val(false);
	  			 jQuery("#submitOrder").val("提交订单");
	  			 jQuery("#assureHint").html("");
	  			 $("#htchkin_point_text").css("display","block");
	  			$("#htchkin_point_text_asure").css("display","none");
	  			  }	  				 	
    }
    
    //初始超时且超房量担保
    function timeAndRoomAssure(){  
    	var selectRoomNum=jQuery("#roomNumSelect").val();
    	var assureMoney=countAssureMoneySum();	
	   	if(jQuery("#checkInTime1:checked").val()!=null){
	   		jQuery("#f_payNeedAssure").val(true);
	   		if(selectRoomNum > maxRoomNum){
	 			var hint='<lable>预订该房型超过'+lastestAssureTime+'或超过'+maxRoomNum+'间房，需提供信用卡担保。担保金额为<strong class="orange" style="font-family:Arial">&yen;'+assureMoney+'元</strong><lable>';
	   		}else{
	  			var hint='<lable>入住时间超过酒店规定最晚时间'+lastestAssureTime+'，需提供信用卡担保。担保金额为<strong class="orange" style="font-family:Arial">&yen;'+assureMoney+'元</strong></lable>';
	  		}	
	   		jQuery("#submitOrder").val("下一步");
	  		jQuery("#assureHint").html(hint);	
	  			
	  		$("#htchkin_point_text").css("display","none");
	  		$("#htchkin_point_text_asure").css("display","block");
	  	}else{
	  	 	if(selectRoomNum > maxRoomNum){
	  	 		jQuery("#f_payNeedAssure").val(true);
	 			var hint='<lable>预订该房型超过'+maxRoomNum+'间房以上，需提供信用卡担保。担保金额为<strong class="orange" style="font-family:Arial">&yen;'+assureMoney+'元</strong><lable>';
	  			jQuery("#submitOrder").val("下一步");
	  			jQuery("#assureHint").html(hint);
	  			
	  	 		$("#htchkin_point_text").css("display","none");
	  			$("#htchkin_point_text_asure").css("display","block");
	  	 	}else{
	  			jQuery("#f_payNeedAssure").val(false);
	  			jQuery("#submitOrder").val("提交订单");
	  			jQuery("#assureHint").html("");
	  			
	  			$("#htchkin_point_text").css("display","block");
	  			$("#htchkin_point_text_asure").css("display","none");
	  		}
	  	} 				 	
    }
    
    //超时担保
    function timeAssure(){
       	
    	if(jQuery("#checkInTime1:checked").val()!=null){
    		
    		jQuery("#f_payNeedAssure").val(true);
    		timeAssureHint();
    		jQuery("#submitOrder").val("下一步");	
    	}else{
    		jQuery("#f_payNeedAssure").val(false);
    	}
    } 
    
    //超时担保的提示
    function timeAssureHint(){
    	var assureMoneySum=countAssureMoneySum();
    		
            var hint='<lable>入住时间超过酒店规定最晚时间'+lastestAssureTime+'，需提供信用卡担保。担保金额为<strong class="orange" style="font-family:Arial">&yen;'+assureMoneySum+'元</strong></lable>';
            jQuery("#assureHint").html(hint);
            $("#htchkin_point_text").css("display","none");
	  				$("#htchkin_point_text_asure").css("display","block");	
    }
    
    //无条件担保
    function unconditionAssure(){   			   	
	  		jQuery("#f_payNeedAssure").val('true');  				 					  				
	  		var hint='<lable>该时段内入住该房型，需按酒店要求提供信用卡担保。担保金额为<strong class="orange" style="font-family:Arial">&yen;'+countAssureMoneySum()+'元</strong></lable>';
	  		jQuery("#submitOrder").val("下一步");
	  		jQuery("#assureHint").html(hint); 		
	  		$("#htchkin_point_text").css("display","none");
	  		$("#htchkin_point_text_asure").css("display","block");			 	
    }
    
	 //担保计算担保金额
	 function countAssureMoneySum(){
	   var roomQuarity=jQuery("#roomNumSelect").val();
	   var assureMoneySum=assureMoney*roomQuarity;	 	   
	   return Math.ceil(assureMoneySum);
	 }
    
  	 //处理所有的担保
	function accessAssure(){
	
	if(assureType==1){
		 unconditionAssure();
	 }
	 else if(assureType==2){
	 	timeAssure();
	 }
	 else if(assureType==3){
	  	roomAssure();
	  }
	  else if(assureType==4){
		 nigthAssure();
		}else if(assureType==5){
			timeAndRoomAssure();//艺龙既超时又超房量担保
		}
	else{}
	}
  
 
	 	 
	 function accessLongHotelName(){
	 if(hotelNameStr.length>30){
	 	var shortHotelName=hotelNameStr.substring(0,28);
	 	jQuery("#hotelName").text(shortHotelName+'....');
	 	jQuery("#hotelName").mouseover(
			function (){
				jQuery("#hotelName").text(hotelNameStr);
				jQuery("#hotelNameDiv").css("width","1500px");
				}	
				);
		jQuery("#hotelName").mouseout(
		function (){
		jQuery("#hotelName").text(shortHotelName+"....");
		jQuery("#hotelNameDiv").css("width","700px");
		}
		);
	 }
	 } 
  
	 function initHotelBook(){
	 	//jQuery("#roomNumSelect").val(1);
	 	 var roomNum = jQuery("#f_roomQuantity").val();	 
	 	 var initbedType = jQuery("#f_bedType").val();	
	    initBedType();
	    if(initbedType==null || ""==initbedType){
     		initbedType = jQuery("#f_bedType").val();     		
     	}
	    jQuery("#bedtypeSelect").val(initbedType);	    
	 	setroomNumSelect(initbedType);
	 	addBedtypeSelectChange();
	 	setBedTypeTip();
	    var fellowNameString="住客姓名";
        if(jQuery("#flagHKGORMAC").val()=="true"){
         fellowNameString="Lastname/Firstname";
        }
         jQuery("#fellowNameId1").val(fellowNameString);
	     changeFellowPersonInputNum(roomNum);    	 
   		 changeAllPriceAndReturnCash(roomNum); 
	 	 jQuery("#roomNumSelect").val(roomNum );
	 	 
	 	initLatestArriveTime();
	 	initPrepaySubmitValue();
	 	
	 	accessAssure();
	 	setOnlyOneLatestArriveTime();
	 	accessLongHotelName();
	 	initLinkMans();
	 	
	 	jQuery('#linkmanName').val(jQuery('#f_linkMan').val());
	 	jQuery('#linkmanTelephone').val(jQuery('#f_mobile').val());
	 	jQuery('#linkmanEmail').val(jQuery('#f_email').val());	
	 	jQuery('#submitOrder').attr("disabled",　false);　 	
	 }
  

	
	function blurValidatePhone(str){
	
	    var pattern=/^(13[0-9]|15[0-9]|18[0-9]|147)\d{8}$/; 
		if (!pattern.test(str) && str.length!=0) {
	       jQuery('#linkmanNameMes').text('手机号码是以13、15、18或147开头的11位数字，请正确输入！');	  
	       	errerMessageLinkman('linkmanTelephone');
	       return false;	
	    }
	    else{
	   		rightMessageLinkman('linkmanTelephone');
	    }
	    return true;
	} 
	
	function errerMessageLinkman(elementId){
		  jQuery('#'+elementId).css('background-color','#FFFCD1');
	       jQuery('#'+elementId).css('border-color','red');
	       jQuery('#linkmanMes').show();
	}
	
	function rightMessageLinkman(elementId){
		jQuery('#'+elementId).css('background-color','');
	     jQuery('#'+elementId).css('border-color','');
	     jQuery('#linkmanMes').hide();
	}

     
//add by ting.li，添加床型改变事件
	  function addBedtypeSelectChange(){
	 	 jQuery("#bedtypeSelect").change(
	  		 function(){
	   			 var bedType = jQuery("#bedtypeSelect").val();
	    		 var roomNumSelect=jQuery("#roomNumSelect");
				 var roomNum = roomNumSelect.val();
	    		 var minQuota=bedTypeMinQuatoJson['bedtype'+bedType];
			
				setroomNumSelect(bedType);
				if(roomNum>minQuota){	
					roomNumSelect.val(minQuota);
					roomNumSelect.change();			
				}
				else{
					roomNumSelect.val(roomNum);
				}
				setBedTypeTip();
			}
		);
	  }
	
	// add by ting.li ,当前的床型的最大预订间数小于5，在床型右边进行提示
	function setBedTypeTip(){
		var bedType = jQuery("#bedtypeSelect").val();
		var bedTypeName='';
		if(bedType==1){
			bedTypeName='大床';
		}
		else if(bedType==2){
			bedTypeName='双床';
		}
		else if(bedType==3){
			bedTypeName='单床';
		}
		else{}
		var minQuota=bedTypeMinQuatoJson['bedtype'+bedType];
		if(minQuota<5){
		jQuery("#bedtypeTip").text(bedTypeName+'还有'+minQuota+'间房');
		}
		else{
		jQuery("#bedtypeTip").text('');
		}  
	}
	//居中显示浮动层
	function showDiv(divName){
		var divObj = document.getElementById(divName);
	    divObj.style.display="";
	    divObj.style.left=(document.documentElement.clientWidth-divObj.offsetWidth)/2+document.documentElement.scrollLeft +'px';   
	    divObj.style.top=(document.documentElement.clientHeight-divObj.offsetHeight)/2+document.documentElement.scrollTop+'px'; 
	}	
	//隐藏浮动层
	function hiddenDiv(divName){
		var divObj = document.getElementById(divName);
	    divObj.style.display="none";
	    clearDiv();
	}
	//清楚动态加载内容
	function clearDiv(){
	    $('#chooseTable').empty();
	    $('#choosetitle').empty();
	    $('#choosebtdiv').empty();
	}
	//获取cookie值
    function getCookieHistory(name){
        var cookies=document.cookie.split("; ");
        for(var i=0;i<cookies.length;i++){
          var temp=cookies[i].split("=");
          if(temp[0]==name){
            return temp[1];
          }
        }
        return "";
        }
