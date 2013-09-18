document.domain = "mangocity.com";
// 会员浮动层
function memberLoginPanel() { 
	var memberUrl = jQuery('#memberPath').val().trim();
	var testmember = document.getElementById("testmember");
	var testmember_HK = document.getElementById("testmember_HK");
	if(testmember==null && testmember_HK!=null){
	    var a = '<iframe src="http://www.mangocity.com/mbrweb/login/loginDirectBook.action?from=directBook&projectCode=null" name="framename" width="100%" marginwidth="0" height="100%" marginheight="0" scrolling="No" frameborder="no" id="framename" border="0"></iframe>';
	    document.getElementById("testmember_HK").innerHTML = a;
	}else{
	    var a = '<iframe src="http://www.mangocity.com/mbrweb/login/loginDirectBook.action?from=directBook&projectCode=null" name="framename" width="100%" marginwidth="0" height="100%" marginheight="0" scrolling="No" frameborder="no" id="framename" border="0"></iframe>';
	    document.getElementById("testmember").innerHTML = a;
	}
	$("#id_fudong").modal({
		closeHTML : '<div class="guanbi"><a title="Close" class="closeMode" style="cursor:pointer;" target="_blank">关闭</a></div>',
		position : ["25%"],
		overlayId : 'contact-overlay',
		//onClose : $.modal.close(),
		closeClass : ('closeMode'),
		overlayClose: true
	});	
}

//'预订按钮'的事件（在gwt使用了）
function hotelBookEvent(pricetypeId,payMethod){
	//modify by alfred.如果是在酒店详情页面中点击预订，则更新记录表
	
	/*
	var logId = document.getElementById("logId");
	if(logId != null && logId != undefined) {
		addChannelLog();
	}
	*/
	
    if(pricetypeId!=null){
        jQuery("#book_priceTypeId").val(pricetypeId);
    }
    if(payMethod!=null){
        jQuery("#book_payMethod").val(payMethod);
    }
     jQuery("#book_inDate").val(jQuery("#id_startDate").val());
     jQuery("#book_outDate").val(jQuery("#id_backDate").val());
     var userIdIndex = document.cookie.indexOf("userid");
	// if(userIdIndex == -1){
	    // memberLoginPanel();
	//}else{
		 document.hotelBookForm.submit();
	//}
}
 /*
//通过异步机制，统计用户的预订率 modify by alfred
function addChannelLog(){
   var logId = document.getElementById("logId").value;
   if(logId!=0){
	   $.ajax({
	       type:"post",
	       url:"hotel-information!updateBookingLog.shtml",
	       data:'logId='+logId
	   });
   }
}
*/

//会员调用的2个函数
function afterDirectBook(){
    document.hotelBookForm.submit();
}
function afterMemberLogin(){
    document.hotelBookForm.submit();
}
function loadHotelBanner(id){
	var bannerStr="";
	for(var i=0;i<bannerInfos2.length;i++){
		bannerStr=bannerStr+'<div class="hotelbanner"><a href="'+bannerInfos2[i][1]+'" target="_blank" name="'+bannerInfos2[i][2]+'"><img src="'+bannerInfos2[i][0]+'" alt="'+bannerInfos2[i][3]+'" width="'+bannerInfos2[i][4]+'" height="'+bannerInfos2[i][5]+'" border="0" /></a></div>';	
	}
	document.getElementById(id).innerHTML=bannerStr;
}

function loadHotelAccess(){
	jQuery.ajax({ 
	  type: "post", 
	  url: "hotel-access.shtml", 
	  success:function(response){
	  		 jQuery("#hotelAccess").html(response);		
	  }
	});
}


//酒店是否在对比框中
var isHotelIn = false;
//酒店对比的
function comparehotel(object){
        isHotelIn = false;
	    // 阻止a标签点击链接
	    //jQuery.Event(event).preventDefault();
		$(object).blur();
		var htname = $(object).attr('htlname');
		var short_htname = htname.substring(0,13);
		var hotelid = $(object).attr('htlid');
		var hotelclass = $(object).attr('class');
		if(hotelclass=='offcompare'){
		    return ;
		}
		// 如果对比框不存在，创建一个
		//if(!$('#comparebox')){
		//    var div = document.createElement('div');
		//	div.id = 'comparebox';
		//	div.innerHTML('<div class="cptit"><span id="closecopare"  class="close"></span><span class="cpnote">已有<strong id="hotelNum">0</strong>家酒店</span><h3>酒店对比栏</h3></div><div class="compareList"><ol id="hotelName_compare"></ol></div><p class="tocare">最多只能选择三家酒店</p><p class="comparebtn"><a href="javascript:void(0);" class="btn92x26">开始对比</a></p>');
		//	document.body.appendChild(div);
		//}
		var x = $(object).offset().left, y = ($(object).offset().top + $(object).height());

	    $('#comparebox').show().css({'left':x+715, 'top':y+10});
		var compare_hotels = jQuery("input[name='selectedHotel']");
		if(compare_hotels.length<5){
		    //验证是否已经在对比框中
		    if(compare_hotels.length>0){
		        jQuery("input[name='selectedHotel']").each(function(itemIndex,item){
		            if(hotelid == item.value){
		                alert("该酒店已经加入对比");
		                isHotelIn = true;
		            }
		        });
		    }
		     //添加一条酒店
		    if(isHotelIn==false){
		        var selected_hotels = jQuery("input[name='selectedHotel']:checked");
		        if(selected_hotels.length >2){
		            $('#hotelName_compare').prepend('<li><input name="selectedHotel" type="checkbox" value="'+hotelid+'" class="iradio" /><a href="#" title='+htname+'>'+short_htname+'</a><span class="removli"></span></li>');
		        }else{
		            $('#hotelName_compare').prepend('<li><input name="selectedHotel" checked="checked" type="checkbox" value="'+hotelid+'" class="iradio" /><a href="#" title='+htname+'>'+short_htname+'</a><span class="removli"></span></li>');
		        }
		    }
		}else{
		       alert("最多只能选择5家酒店");
		       return ;
		}
		$('#hotelNum').text($('#hotelName_compare').children().length);
		//移除酒店
		$('span.removli').click(function(){
			var li = $(this).parent();
			//改变样式，变回原来的样式
			$("a[htlid="+$(this).prev().prev().val()+"]").html("加入对比");
            $("a[htlid="+$(this).prev().prev().val()+"]").attr("class","tocompare");
			li.remove();
			$('#hotelNum').text($('#hotelName_compare').children().length);
		});
		//关闭酒店对比框
		$('#closecopare').click(function(){
		    $('#comparebox').hide();
		});	
		$("input[name='selectedHotel']").click(function(){
		    var selected_hotels = jQuery("input[name='selectedHotel']:checked");
		    if(selected_hotels.length>3 ){
                $("#compareAlart").show();
                return false;
            }
		});
		changeSelectedHotelStyle();
}	

//开始对比
function startCompareHotel(){
    var selected_hotels = jQuery("input[name='selectedHotel']:checked");
    if(selected_hotels.length <2 || selected_hotels.length>3 ){
        $("#compareAlart").show();
        return false;
    }
    var hotelIdsStr = "";
    jQuery("input[name='selectedHotel']:checked").each(function(itemIndex,item){
        hotelIdsStr += item.value+",";
    });
    jQuery("#comparedHotelIdsStr").val(hotelIdsStr);
    jQuery("#compareInDate").val(jQuery("#id_startDate").val());
    jQuery("#compareOutDate").val(jQuery("#id_backDate").val());
    jQuery("#compareCityCode").val(jQuery("#cityCode").val());
    document.hotelCompareForm.submit();
}

//已加入对比的酒店改变样式
function changeSelectedHotelStyle(){
   $("input[name='selectedHotel']").each(function(){
       $("a[htlid="+$(this).val()+"]").html("已加对比");
       $("a[htlid="+$(this).val()+"]").attr("class","offcompare");
   });
}

function clearCompareHotelDiv(){	
   $("#hotelName_compare").html("");
   $('#comparebox').hide();
}
		

//酒店名称
function showMes(id,message){
	var obj = document.getElementById(id);
	var _message=(message==null?obj.defaultValue:message);
	if(obj.value.trim()===""){
	    obj.value=_message;
	    obj.style.color="#777777";
	}
}	

function hiddenMes(id,message){
    var obj = document.getElementById(id);
   var _message=(message==null?obj.defaultValue:message);
	if(obj.value==_message){
	    obj.value="";
	    obj.style.color ="#333333";
	}
}

$(document).ready(function(){
	loadHotelBanner("hotelBanner");
	loadHotelAccess();
	if(jQuery("#hotelName").val()==""){
	   jQuery("#hotelName").val("您可以输入酒店名称查询");
	   jQuery("hotelName").css("color","#777777");
	}
});