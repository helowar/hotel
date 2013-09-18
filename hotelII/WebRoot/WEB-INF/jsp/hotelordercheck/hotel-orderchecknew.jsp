<%@ page language="java" pageEncoding="UTF-8"%> 
<%@ page import="com.mangocity.vch.app.service.VchService" %> 
<%@ taglib prefix="c" uri="core"%>
<%@ taglib prefix="fmt" uri="fmt"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fn" uri="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>【酒店预订】酒店查询 订国内商务酒店、快捷酒店、星级酒店首选芒果网！</title>
<link rel="stylesheet" type="text/css" href="http://hres.mangocity.com/css/h/2011/hotel2011.css" />
<script type="text/javascript" src="http://wres.mangocity.com/js/home/cloud/js/jquery-1.3.2.js"></script>
<link rel="stylesheet" type="text/css" href="http://hres.mangocity.com/css/jquery.alert.css"/>

<script type="text/javascript" src="js/jQuery/jquery.alert.js"></script>	
<script type="text/javascript"> 	
  document.domain = "mangocity.com";
</script>

<style type="text/css">

.showOnlinePayHint{
border: 1px solid black;
font-size: 14px;
padding: 8px;
width: 350px;
text-align: center;
margin-top:-19px;
margin-left:40px;

</style>
</head>

<body>
<jsp:include page="../commonjsp/_header.jsp" flush="true" />

<!--=S top bar -->
<div class="w960 hn-topbar">
    <div class="status step3">
        <ul>
            <li>查询</li>
            <li>选择</li>
            <li class="choice">预订</li>
            <li>完成</li>
        </ul>
    </div>
    <div class="crumbs">您现在的位置：<a href="http://www.mangocity.com" class="green" name="firstPage" >首页</a> &gt; <a name="navigationPath" href="hotel-search.shtml" class="green" name="domesticHotel" >国内酒店</a> &gt; 
           <a name="navigationPath" href='list-${fn:toLowerCase(hotelOrderFromBean.cityCode)}.html' class="green" name="daohangcity">${hotelOrderFromBean.cityName}酒店</a>
           &gt; <a name="navigationPath" href='jiudian-${fn:toLowerCase(hotelOrderFromBean.hotelId)}.html' class="green" name="daohangHotel">${hotelOrderFromBean.hotelName}</a> &gt; <em>订单核对</em>
    </div>
</div>
<!--=E top bar-->
<div class="w960">
    <div class="htoder_top"></div>
    <!--=S 订单支付 -->
     <div class="htod_pay">
        <div class="htod_pay_text">
           <h2>核对订单</h2>
           <ul>
             <li class="clear"><strong class="orange"><fmt:formatDate value="${hotelOrderFromBean.checkinDate}" pattern="yyyy-MM-dd"/>(${weekOfInDate})</strong>入住；
                               <strong class="orange"><fmt:formatDate value="${hotelOrderFromBean.checkoutDate}" pattern="yyyy-MM-dd"/>(${weekOfOutDate})</strong>退房；&nbsp;共<strong class="orange">${hotelOrderFromBean.days}</strong>晚</li>
             <li class="htod_float">入住酒店：${hotelOrderFromBean.hotelName}</li>
             <li>房间留至：<strong class="orange"><c:if test="${hotelOrderFromBean.latestArrivalTime == '23:59'}">24:00</c:if><c:if test="${hotelOrderFromBean.latestArrivalTime != '23:59'}">${hotelOrderFromBean.latestArrivalTime}</c:if></strong>
             </li>
             <li class="htod_float">入住房间：${hotelOrderFromBean.roomTypeName}<span class="grey">（${hotelOrderFromBean.childRoomTypeName}）</span>共<strong class="orange">${hotelOrderFromBean.roomQuantity}</strong>间</li>
             <li class="htod_float">入  住 人：${linkeManStr}</li>
             <li class="htod_float">房费合计：   
	            <c:if test="${hotelOrderFromBean.payMethod=='pay' and  false==hotelOrderFromBean.payToPrepay}">
                   <strong class="orange font16" style="font-family:Arial, Helvetica, sans-serif;">${hotelOrderFromBean.currencyStr}<span id="allPriceSum">${hotelOrderFromBean.priceNum * hotelOrderFromBean.roomQuantity}</span></strong> 
	            </c:if>
	            <c:if test="${hotelOrderFromBean.payMethod=='pre_pay' || hotelOrderFromBean.payToPrepay}">
	               <strong class="orange font16" style="font-family:Arial, Helvetica, sans-serif;">&yen;<span id="allPriceSum">${hotelOrderFromBean.priceNum * hotelOrderFromBean.roomQuantity * rate}</span></strong>
	                           
	            </c:if>
	             <script type="text/javascript">
				                 var o = jQuery('#allPriceSum').text();
	                             document.getElementById("allPriceSum").innerHTML = Math.ceil(o) ;
	                          </script>
             </li>
             <li class="htod_float">联 系 人：${hotelOrderFromBean.linkMan}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;联系手机：${hotelOrderFromBean.mobile}</li>
           </ul>
        </div>
        <%-- 金额start --%>
        <div class="htod_pay_cd">
            <c:if test="${hotelOrderFromBean.payMethod=='pre_pay' || hotelOrderFromBean.payToPrepay}">
            
            <c:if test="${not empty member}">
            <div class="htod_pay_cd_jg">
                积分冲抵：<span class="orange" style="font-family:Arial, Helvetica, sans-serif;">-&yen;<em id="pointAmount">0</em></span><br />
                代金券冲抵：<span class="orange" style="font-family:Arial, Helvetica, sans-serif;">-&yen;<em id="couponAmount">0</em></span><br />
              <strong>应付金额：</strong>
                   <strong class="orange  font16" style="font-family:Arial, Helvetica, sans-serif;">&yen;<span id="acturalAmount"></span>
                       <script type="text/javascript">
				            var o =  Math.ceil(${hotelOrderFromBean.priceNum * hotelOrderFromBean.roomQuantity * rate});
	                        document.getElementById("acturalAmount").innerHTML = o ;
                       </script>
                    </strong>
            </div>
            <div class="htod_pay_cd_jf">
               <c:if test="${hotelOrderFromBean.payMethod=='pre_pay' || hotelOrderFromBean.payToPrepay }">
               
	         
                    
                           使用：<input name="" id="pointInput" type="text" class="w57" />&nbsp;<font class="orange">元</font>积分（共有<span class="orange" id="useablePoint"></span><font class="orange">元</font>积分）<br />
                         
                        <a href="#" name="prepayCouponLink" id="prepayCouponLink">使用代金券冲抵</a>
             
               </c:if>         
            </div>
            </c:if>
            
            <c:if test="${empty member}">
               <div class="htod_pay_cd_jg" style="display:none">
                <%-- 代金券和积分赋值用的 --%>
                <em id="pointAmount">0</em>
                <em id="couponAmount">0</em>
                   <strong>应付金额：</strong>
                   <strong class="orange  font16" style="font-family:Arial, Helvetica, sans-serif;">&yen;<span id="acturalAmount"></span>
                       <script type="text/javascript">
				            var o =  Math.ceil(${hotelOrderFromBean.priceNum * hotelOrderFromBean.roomQuantity * rate});
	                        document.getElementById("acturalAmount").innerHTML = o ;
                       </script>
                    </strong>
               </div>
            </c:if>
            
            </c:if>
            <c:if test="${hotelOrderFromBean.payMethod=='pay' and  false==hotelOrderFromBean.payToPrepay}">
                <strong class="orange font16" style="font-family:Arial, Helvetica, sans-serif;">担保金额：&yen;<span id="assureAllPrice">${order.suretyPrice}</span></strong>
                <script type="text/javascript">
				            var o =  jQuery('#assureAllPrice').text();
	                        document.getElementById("assureAllPrice").innerHTML = Math.ceil(o );
                       </script>
            </c:if>  
                    
        </div>
        <%-- 金额end --%>
                
        <c:if test="${hotelOrderFromBean.payMethod=='pre_pay' || hotelOrderFromBean.payToPrepay}">
	        <div class="htod_pay_wy" id="payInfoDiv">
	          <h2 id="creditPayOrOnlinePay" >信用卡支付   &nbsp;&nbsp;<font style="font-family:Arial, Helvetica, sans-serif;font-size:12px;font-weight:normal">${bookhintCancelAndModifyStr}</font></h2>
              <c:if test="${showCreditCard}">
                       <div class="" id="prePayCreditDiv">
                          <jsp:include page="hotel-credit.jsp" flush="true" />
                       </div>
              </c:if>
              
              <div class="htod_wy" id="prepayOnlineDiv" style="display:none">
                     <jsp:include page="hotel-netbank.jsp" flush="true" />
              </div>
	        </div>	           
        </c:if>
        
        <c:if test="${hotelOrderFromBean.payMethod=='pay' and  false==hotelOrderFromBean.payToPrepay}">
        	<div class="htod_pay_wy">
	          <h2>信用卡担保 &nbsp;&nbsp;<font style="font-family:Arial, Helvetica, sans-serif;font-size:12px;font-weight:normal">${bookhintCancelAndModifyStr}</font></h2>
	                  <div  id="payCreditDiv">
	                      <jsp:include page="hotel-credit.jsp" flush="true" />
	                  </div>
	        </div>
        </c:if>
        
     </div>
    <!--=E 订单支付 -->
    <div class="htod_btn"><input name="confirmSubmit" id="confirmSubmit" type="button" class="htod_btn" value="提交订单" /></div>
</div>
 <form id="orderCheckForm" name="orderCheckForm" action="hotel-complete.shtml" method="post">
 <s:token />
    <!-- 日期，时间,返现会员 -->
   <input type="hidden" name="memberCd" value="${memberCd}" />
   <input type="hidden" name="mbrID" value="${mbrID}" />
   <input type="hidden" name="autoFlag" value="${autoFlag}" />
  <input type="hidden" name="orderCD" value="${orderCD}" />
  
  <input type="hidden" name="checkinDate" value='<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkinDate}"/>' />
  <input type="hidden" name="checkoutDate" value='<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkoutDate}"/>' />
  <input type="hidden" name="latestArrivalTime" value="${hotelOrderFromBean.latestArrivalTime}" />
  <input type="hidden" name="arrivalTime" value="${hotelOrderFromBean.arrivalTime}" />
  <input type="hidden" name="source" value="${hotelOrderFromBean.source}" />
    <!-- 酒店 -->
  <input type="hidden" name="cityId"  value="${hotelOrderFromBean.cityId}" />
  <input type="hidden" name="hotelChnAddress" value="${hotelOrderFromBean.hotelChnAddress}" />
  <input type="hidden" name="cityCode"  value="${hotelOrderFromBean.cityCode}" />
  <input type="hidden" name="hotelId"  value="${hotelOrderFromBean.hotelId}" />
  <input type="hidden" name="hotelName"  value="${hotelOrderFromBean.hotelName}" />
  <input type="hidden" name="hotelStar"  value="${hotelOrderFromBean.hotelStar}"/> 
     <!-- 房型 -->
   <input type="hidden" name="roomTypeId"  value="${hotelOrderFromBean.roomTypeId}" />
   <input type="hidden" name="roomTypeName"  value="${hotelOrderFromBean.roomTypeName}" />
   <input type="hidden" name="childRoomTypeId" value="${hotelOrderFromBean.childRoomTypeId}" />
   <input type="hidden" name="childRoomTypeName"  value="${hotelOrderFromBean.childRoomTypeName}" />
   <input type="hidden" name="roomQuantity" id="roomQuantity" value="${hotelOrderFromBean.roomQuantity}" />
   <input type="hidden" name="roomChannel" id="roomChannel" value="${hotelOrderFromBean.roomChannel}" />
   <input type="hidden" name="minRoomNumCts" id="minRoomNumCts" value="${hotelOrderFromBean.minRoomNumCts}" />
   <input type="hidden" name="bedType" value="${hotelOrderFromBean.bedType}" />
   <!--  -->
   <%-- 预付的支付方式 --%>
   <input type="hidden" name="payToPrepay" id="f_payToPrepay"  value="${hotelOrderFromBean.payToPrepay}" />
   <input type="hidden" name="payMethod" value="${hotelOrderFromBean.payMethod}" />
   <input type="hidden" name="priceNum"  value="${hotelOrderFromBean.priceNum}" />
   <input type="hidden" name="acturalAmount" id="f_acturalAmount" value="" />
   <input type="hidden" name="orderPayType" id="f_orderPayType" value="" />
   <input type="hidden" name="onlinePaytype" id="f_onlinePaytype" value="" />
   <input type="hidden" name="returnAmount" id="returnAmount" value="${hotelOrderFromBean.returnAmount}" />
   <input type="hidden" name="currency"  value="${hotelOrderFromBean.currency}" />
   <%-- 担保--%>
   <input type="hidden" name="needAssure" id="needAssure" value="${hotelOrderFromBean.needAssure}" />
   
   <!-- 会员的 -->
   <input type="hidden" name="useUlmPoint" id="f_useUlmPoint" value="false" />
   <input type="hidden" name="ulmPoint" id="f_ulmPoint" value="0" />
   <input type="hidden" name="usedCoupon" id="f_usedCoupon" value="false" />
   <input type="hidden" name="ulmCoupon" id="f_ulmCoupon" type="hidden" value="0" />
   <input type="hidden" name="linkeManStr" value="${linkeManStr}" />
   <input type="hidden" name="linkMan" value="${hotelOrderFromBean.linkMan}" />
   <input type="hidden" name="mobile" value="${hotelOrderFromBean.mobile}" />
   
    <input type="hidden" name="sid" id="sid" value="${sid}" />
    <input type="hidden" name="cardID" id="cardID" value="">  
    
      <!-- 记录游比比的平台号 -->
    <input type="hidden" name="trace_code" id="trace_code" value="${trace_code}"/>
 </form>
 
 <jsp:include page="../commonjsp/_footer.jsp" flush="true" />
 <script type="text/javascript" >
//“提交订单”事件处理
jQuery("#confirmSubmit").click(function () {
	//信用卡“支付”或“担保”
	inputSomeValueToPrepaySubmitForm();
   	<c:if test="${showCreditCard}">
   	    <c:if test="${hotelOrderFromBean.payMethod=='pre_pay' || hotelOrderFromBean.payToPrepay}">
   	        if(jQuery("#acturalAmount").html() > 0){
		        document.getElementById('creditIFrame').contentWindow.submitcreditcard(dopay);
		    }else{
		        //document.orderCheckForm.submit();
		        //jQuery("#confirmSubmit").attr('class','show_grey');
		        //jQuery("#confirmSubmit").attr("disabled",　true);
		        submitCheckForm();
		    }
		</c:if>
		<c:if test="${hotelOrderFromBean.payMethod=='pay' and  false==hotelOrderFromBean.payToPrepay}">
		    document.getElementById('creditIFrame').contentWindow.submitcreditcard(dopay);
		</c:if>
   	</c:if>
   	<c:if test="${!showCreditCard}">
		submitCheckForm();
	</c:if>	
	
});

//财务回调函数
function dopay(cardObj) {
	if(cardObj != null && cardObj != undefined) {
		if(cardObj.resultcode == '00' || cardObj.resultcode == '1' || cardObj.resultcode == '2') {
			jQuery("#cardID").val(cardObj.cardId);
			submitCheckForm();
		}else {
			alert("请正确填写信用卡信息!");
		}
	}
}

function submitCheckForm(){
 			document.orderCheckForm.submit();
			 jQuery("#confirmSubmit").attr('class','show_grey');
			 jQuery("#confirmSubmit").attr("disabled",　true);
}

function initPrePayTypeDiv(){
    var roomChannel = '${hotelOrderFromBean.roomChannel}';
    //roomChannel = '8';
     if(roomChannel == '8' ){
        jQuery("#creditPayOrOnlinePay").html("网上银行支付");
        //jQuery("#prePayCreditDiv").hide();
        jQuery("#prepayOnlineDiv").show();
     }
     <c:if test="${not empty member}">
        jQuery("#pointAndCouponDiv").show();
    </c:if>
}

//面付
function inputSomeValueToPaySubmitForm(){
   //inputCommonValueToSubmitForm();
}

//预付
function inputSomeValueToPrepaySubmitForm(){
    var roomChannel = '${hotelOrderFromBean.roomChannel}';
    if (roomChannel == '8'){
        jQuery("#f_orderPayType").val(4);
        jQuery("#f_onlinePaytype").val(jQuery("input[name='onlinePaytype']:checked").val());
    }else{
        jQuery("#f_orderPayType").val(2);
    }
    jQuery("#f_acturalAmount").val(jQuery("#acturalAmount").html());
    //会员的积分
	jQuery("#f_useUlmPoint").val("false");
	var pointAmount = jQuery("#pointAmount").html();//该值为积分相当的RMB
    if(pointAmount>= 1){
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

function validateonlinePaytype(){
     var roomChannel = '${hotelOrderFromBean.roomChannel}';
     if (roomChannel == '8'){
        jQuery("input[name='onlinePaytype']:checked");
     }
}

//积分start
function calculateAmount(){
   //积分
    var checkPoint = calculatePointAmount();
    if(checkPoint == false){
        return;
	}	
    var sumAmount = jQuery("#allPriceSum").html();
	var pointAmount = jQuery("#pointAmount").html();
    var couponAmount = jQuery("#couponAmount").html();
	var acturalAmount = ((sumAmount - pointAmount - couponAmount).toFixed(2))*100/100;
	acturalAmount=Math.ceil(acturalAmount);
	if(Number(sumAmount) < Number(pointAmount)){	
	    jQuery('#prepayCouponLink').alert("输入积分数所能支付的金额超过了订单总额，请重新输入积分数！");
	    jQuery("#pointInput").val("");
	    calculateAmount();
	    jQuery('#alertContainer').css({'margin-left':-88, 'margin-top':-5});
	    return ;
	}
	//当代金券的金额大于积分的金额时，提示可以减少积分
	if(acturalAmount < 0){ 
	    jQuery("#prepayCouponLink").alert("您使用的代金券总金额大于订单的总金额，如果您选择了积分消费，可以考虑减少使用的积分！");
	    acturalAmount = 0;
	    jQuery('#alertContainer').css({'margin-left':-88, 'margin-top':-8});
	}
	jQuery("#acturalAmount").html(acturalAmount);
	hideOrShowPayInfoDiv(acturalAmount);
	
}

function hideOrShowPayInfoDiv(acturalAmount){
    if(acturalAmount>0){
       jQuery("#payInfoDiv").slideDown(300);
    }else{
       jQuery("#payInfoDiv").fadeOut('show');
       //jQuery("#payInfoDiv").slideUp('fast');
    }
}

var ablePiont=${hotelOrderFromBean.ableUlmPoint};
calulatePointToRMB();	 
function calulatePointToRMB(){  
	var PiontToRMB=Math.floor(ablePiont/100);
	jQuery("#useablePoint").text(PiontToRMB);
}

var useablePoint = jQuery("#useablePoint").html();
	
	
function calculatePointAmount(){

if(jQuery("#pointInput").length>0){
	
    var points = jQuery("#pointInput").val();
    var reg = /^\d+$/;
	if(!reg.test(points) && points != ""){
	   jQuery('#prepayCouponLink').alert("请输入的数字");
	   jQuery("#pointInput").val("");	
	   calculateAmount();
	   jQuery('#alertContainer').css({'margin-left':-88,'margin-top':-8});
	   return false;
	}
	var lastPoint = useablePoint - points;
	if(lastPoint < 0){
	     jQuery('#prepayCouponLink').alert("输入积分大于可用积分");
	     jQuery("#pointInput").val("");	
	     calculateAmount();
	     jQuery('#alertContainer').css({'margin-left':-88,'margin-top':-8});
		 return false;
	}
	jQuery("#useablePoint").html(lastPoint);
	if(points == null || points ==""){
		points = 0;
	}
    var pointAmount = Math.floor(points);
    jQuery("#pointAmount").html(pointAmount);
   }
}

  jQuery("#pointInput").keyup(
	   function(){
	      calculateAmount();
	   }
	);

//积分end

//初始化放到最后
function initHotelOrderCheck(){
   initPrePayTypeDiv();
   jQuery("#confirmSubmit").attr("disabled",　false);
    <c:if test="${hotelOrderFromBean.payMethod=='pre_pay' || hotelOrderFromBean.payToPrepay}">
       calculateAmount();
    </c:if>
}

   initHotelOrderCheck();

// 代金券的相关代码，需添加的代码请写在上面
function getJsDate(date){
	var s1 = date.split("-"); 
	var dateVar = new Date(s1[0],s1[1]-1,s1[2],0,0,0); 
	return dateVar;
}

/**
    * 生成请求代金券系统的url
    * hotel2.9.3 add by chenjiajie 2009-08-27
    */
   function genVoucherRequestURL(){
   		var returnUrl;
 		//CC调用的代金券页面接口URL
		var url = "<c:out value="${hwebVoucherRequestURL}" />" + "/vchInputUI.action";
		//第一个参数，json格式
		var orderDataMap = "{";
		orderDataMap += "\'<%=VchService.KEY_ORDER_NO%>\':\'000000\',";//订单编号，可以为空，但是需要有key
		var nightCout  = ${hotelOrderFromBean.roomQuantity}; //房间数量 
		var totalamount = jQuery("#allPriceSum").html();
		orderDataMap += "\'<%=VchService.KEY_ORDER_AMOUNT%>\':\'"+parseInt(Math.ceil(totalamount))+"\',";//订单金额
		var checkInDateStr = "<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkinDate}"/>";
		var checkOutDateStr = "<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkoutDate}"/>";
		var checkInDate = getJsDate(checkInDateStr);
		var checkOutDate = getJsDate(checkOutDateStr);
		var roomNight = nightCout * ${hotelOrderFromBean.days};//计算出来的间夜数
		orderDataMap += "\'<%=VchService.KEY_ORDER_COUNT%>\':\'"+roomNight+"\',";//订单间夜	
		orderDataMap += "\'<%=VchService.KEY_CONSUMEDATE%>\':\'"+checkInDateStr+"\',";//入住日期
		orderDataMap += "\'<%=VchService.KEY_PRODUCT_TYPE%>\':\'<%=VchService.PRODUCT_TYPE_HOTEL%>\',";//业务类型(酒店)
		orderDataMap += "\'<%=VchService.KEY_USED_PLATFORM%>\':\'<%=VchService.USED_BY_WEB%>\',";//使用平台：网站或者呼叫中心
		orderDataMap += "\'<%=VchService.KEY_MEMBER_NO%>\':\'${member.membercd}\',";//会员编号	
		var memberLevel = "";//会员卡级别
		var memberType = "";//会员卡类别
		<c:choose>
			<c:when test="${not empty member.type and member.type eq '4'}">
				//如果是职员卡，卡级别是金卡，卡类型是职员卡
				memberLevel = "2";
				memberType = "<%=VchService.CARD_TYPE_PERSONAL%>";
			</c:when>
			<c:otherwise>
				memberLevel = "${member.type}";
				memberType = "${member.travelbusiness}" == "N" ? "<%=VchService.CARD_TYPE_PERSONAL%>" : "<%=VchService.CARD_TYPE_TMC%>";
			</c:otherwise>		
		</c:choose>		
		orderDataMap += "\'<%=VchService.KEY_MEMBER_CARD_LEVEL%>\':\'"+memberLevel+"\',";//会员卡级别
		/**
		 * 会员卡类别 个人卡personal, 职员卡staff, TMC卡:TMC
		 */
		orderDataMap += "\'<%=VchService.KEY_MEMBER_CARD_TYPE%>\':\'"+memberType+"\',";
		orderDataMap += "\'<%=VchService.KEY_HOTEL_BE_SHIPED%>\':\'N\',";//是否配送
		orderDataMap += "\'<%=VchService.KEY_ORDER_OPTERATOR%>\':\'${order.creator}\',";//订单操作员
		orderDataMap += "\'<%=VchService.KEY_ORDER_DATE%>\':\'${order.createDate}\',";//订单时间
		orderDataMap += "\'<%=VchService.KEY_HOTEL_HOTEL%>\':\'${hotelOrderFromBean.hotelId}\',";//酒店--酒店编码
		orderDataMap += "\'<%=VchService.KEY_HOTEL_CITY%>\':\'${htlHotel.city}\',";//酒店--城市编码
		/**
		 * 酒店相关基本信息
		**/
		var hotelStar = "${htlHotel.hotelStar}"; //酒店星级
		var hotelStarForVoucher = "";
		//5星级
		if("19" == hotelStar || "29" == hotelStar){
			hotelStarForVoucher = "<%=VchService.STAR_5%>";
		}
		//4星级
		else if("39" == hotelStar || "49" == hotelStar){
			hotelStarForVoucher = "<%=VchService.STAR_4%>";
		}
		//3星级
		else if("59" == hotelStar || "64" == hotelStar){
			hotelStarForVoucher = "<%=VchService.STAR_3%>";
		}
		//3星级以下
		else {
			hotelStarForVoucher = "<%=VchService.STAR_BELOW_3%>";
		}
		orderDataMap += "\'<%=VchService.KEY_HOTEL_STAR_LEVEL%>\':\'"+hotelStarForVoucher+"\',";//酒店--星级
		orderDataMap += "\'<%=VchService.KEY_HOTEL_COUNTRY%>\':\'${htlHotel.country}\',";//酒店--国家编码
		orderDataMap += "\'<%=VchService.KEY_HOTEL_PROVINCE%>\':\'${htlHotel.state}\',";//酒店--省份编码
		orderDataMap += "\'<%=VchService.KEY_AGENT%>\':\'${member.agentid}\'";//渠道号
		orderDataMap += "}";	
		//url传递的参数列表
		var params = "?";				
		params += "orderData="+encodeURI(orderDataMap);
		//本页面上次点击使用的代金券
		var selectedVoucherCodes = "";		
		jQuery("input[alt='couponCode']").each(function (){
			selectedVoucherCodes += jQuery(this).val() + ",";
		});
		if("" != selectedVoucherCodes && selectedVoucherCodes.lastIndexOf(',') > 0){
			selectedVoucherCodes = selectedVoucherCodes.substring(0,selectedVoucherCodes.length-1); 	      
		}
		params += "&selectedVoucherCodes="+selectedVoucherCodes;	
		params += "&callbackFunc=voucherRequestCallBack"; //回调函数名称
		returnUrl = url+params;
		
		return returnUrl;
   }
   
   /**
    * 调用代金券系统后的回调函数，在genVoucherRequestURL()中传递
    * hotel2.9.3 add by chenjiajie 2009-08-28
    */
   function voucherRequestCallBack(data){
   		var formId = "#orderCheckForm";//提交的表单的id
   		var length = Number(data.selectedVoucherCount);
   		var selectedVouchers = data.selectedVouchers;
   		jQuery("#orderCheckForm>.couponInput").remove(); //删除上一次回调生成的隐藏域
   		var totalVoucherValue = 0;
   		if(length > 0){
   			//返回的代金券数量大于1，需要用 "变量名_下标" 的形式组装隐藏域
   			if(length > 1){
   				for(var i=0;i<selectedVouchers.length;i++){
   					jQuery("<input class='couponInput' alt='couponCode' type='hidden' name='couponCode_"+(i+1)+"' id='couponCode_"+(i+1)+"' value='"+selectedVouchers[i].code+"' />").appendTo(formId);
   					jQuery("<input class='couponInput' type='hidden' name='couponName_"+(i+1)+"' id='couponName_"+(i+1)+"' value='"+selectedVouchers[i].name+"' />").appendTo(formId);
   					jQuery("<input class='couponInput' type='hidden' name='couponValue_"+(i+1)+"' id='couponValue_"+(i+1)+"' value='"+selectedVouchers[i].favourableValue+"' />").appendTo(formId);
   					jQuery("<input class='couponInput' type='hidden' name='couponBeginDate_"+(i+1)+"' id='couponBeginDate_"+(i+1)+"' value='"+selectedVouchers[i].dateBegin+"' />").appendTo(formId);
   					jQuery("<input class='couponInput' type='hidden' name='couponEndDate_"+(i+1)+"' id='couponEndDate_"+(i+1)+"' value='"+selectedVouchers[i].dateEnd+"' />").appendTo(formId);
   					jQuery("<input class='couponInput' type='hidden' name='description_"+(i+1)+"' id='description_"+(i+1)+"' value='"+selectedVouchers[i].description+"' />").appendTo(formId);
   					totalVoucherValue += Number(selectedVouchers[i].favourableValue);
   				}
   			}else{
				jQuery("<input class='couponInput' alt='couponCode' type='hidden' name='couponCode' id='couponCode' value='"+selectedVouchers[0].code+"' />").appendTo(formId);
				jQuery("<input class='couponInput' type='hidden' name='couponName' id='couponName' value='"+selectedVouchers[0].name+"' />").appendTo(formId);
				jQuery("<input class='couponInput' type='hidden' name='couponValue' id='couponValue' value='"+selectedVouchers[0].favourableValue+"' />").appendTo(formId);
				jQuery("<input class='couponInput' type='hidden' name='couponBeginDate' id='couponBeginDate' value='"+selectedVouchers[0].dateBegin+"' />").appendTo(formId);
				jQuery("<input class='couponInput' type='hidden' name='couponEndDate' id='couponEndDate' value='"+selectedVouchers[0].dateEnd+"' />").appendTo(formId);
				jQuery("<input class='couponInput' type='hidden' name='description' id='description' value='"+selectedVouchers[0].description+"' />").appendTo(formId);
				totalVoucherValue += Number(selectedVouchers[0].favourableValue);
   			}
   			//总行数
   			jQuery("<input class='couponInput' type='hidden' name='couponNum' id='couponNum' value='"+length+"' />").appendTo(formId);
   			//标志使用了代金券
   			jQuery("#f_usedCoupon").val(true);
   		}
		//记录使用了代金券金额
		jQuery("#f_ulmCoupon").val(totalVoucherValue);
		jQuery("#couponAmount").html(Math.floor(totalVoucherValue));
		calculateAmount();
   }
jQuery("#prepayCouponLink").click(function (){
		//genVoucherRequestURL()生成请求代金券系统的url						
		//var data = window.showModalDialog(genVoucherRequestURL(),"_VoucherWin","location=no,menubar=no,toolbar=no,titlebar=no,resizable=yes,scrollbars=yes,status=yes");
		//voucherRequestCallBack(data);
		window.open(genVoucherRequestURL(),"_VoucherWin","location=no,menubar=no,toolbar=no,titlebar=no,resizable=yes,scrollbars=yes,status=yes");
	});


</script>


  <%-- 这个放到最后，有百度监控，会影响速度--%>
<jsp:include page="../commonjsp/ _footer_monitor.jsp" flush="true" />
</body>
</html>
