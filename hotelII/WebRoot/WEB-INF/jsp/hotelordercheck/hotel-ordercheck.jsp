<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="core"%>
<%@ taglib prefix="fmt" uri="fmt"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="h" uri="convert"%>
<%@ taglib prefix="fn" uri="fn"%>
<%@ taglib prefix="f" uri="functions"%>
<c:set var="showCreditCard" value="false"/>
<c:if test="${((hotelOrderFromBean.payMethod eq 'pre_pay' || hotelOrderFromBean.payToPrepay) && hotelOrderFromBean.orderPayType eq 2 && hotelOrderFromBean.acturalAmount > 0) 
    			|| (hotelOrderFromBean.payMethod eq 'pay' && !hotelOrderFromBean.payToPrepay && hotelOrderFromBean.needAssure)}">
	<c:set var="showCreditCard" value="true"/>    			
</c:if>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=7">

<link rel="stylesheet" type="text/css" href="http://hres.mangocity.com/css/h/2011/hotel2011.css" />
<script src="js/commonJS/creditCard.js" type="text/javascript"></script>
<script src="js/commonJS/validate.js" type="text/javascript"></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/home/cloud/js/jquery-1.3.2.js"></script>
<script type="text/javascript"> 	
  document.domain = "mangocity.com";
</script>
</head>

<body onload="initIframeHeight()"> 

<%--  值传递 --%> 
 <form id="orderCheckForm" name="orderCheckForm" action="hotel-complete.shtml" method="post">
 <s:token />
 <!-- 日期，时间 -->
  <input type="hidden" name="checkinDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkinDate}"/>">
  <input type="hidden" name="checkoutDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkoutDate}"/>">
  <input type="hidden" name="days" id="days" value="${hotelOrderFromBean.days}" />
  <input type="hidden" name="arrivalTime" id="arrivalTime" value="${hotelOrderFromBean.arrivalTime}" />
  <input type="hidden" name="latestArrivalTime" id="latestArrivalTime" value="${hotelOrderFromBean.latestArrivalTime}" />
  <input type="hidden" name="orderCD" value="${orderCD}" />
  <!-- 酒店 -->
  <input type="hidden" name="cityId"  value="${hotelOrderFromBean.cityId}" />
  <input type="hidden" name="cityCode"  value="${hotelOrderFromBean.cityCode}" />
  <input type="hidden" name="hotelId"  value="${hotelOrderFromBean.hotelId}" />
  <input type="hidden" name="hotelName"  value="${hotelOrderFromBean.hotelName}" />
  <input type="hidden" name="hotelStar"  value="${hotelOrderFromBean.hotelStar}"/> 
  
  <%--酒店提示信息--%>
   <input type="hidden" name="tipInfo" value="${hotelOrderFromBean.tipInfo}"/>
  
  <!-- 房型 -->
   <input type="hidden" name="childRoomTypeId" value="${hotelOrderFromBean.childRoomTypeId}" />
   <input type="hidden" name="childRoomTypeName"  value="${hotelOrderFromBean.childRoomTypeName}" />
   <input type="hidden" name="roomQuantity" id="roomQuantity" value="${hotelOrderFromBean.roomQuantity}" />
   <input type="hidden" name="flagCtsHK" id="flagCtsHK" value="${hotelOrderFromBean.flagCtsHK}" />	
   <input type="hidden" name="roomChannel" id="roomChannel" value="${hotelOrderFromBean.roomChannel}" />
   <input type="hidden" name="minRoomNumCts" id="minRoomNumCts" value="${hotelOrderFromBean.minRoomNumCts}" />
   
    <!-- 入住人 -->
    <input type="hidden" name="linkMan" id="linkMan" value="${hotelOrderFromBean.linkMan}"/>
 	<input type="hidden" name="mobile" id="mobile" value="${hotelOrderFromBean.mobile}"/>
 	<input type="hidden" name="email" id="email" value="${hotelOrderFromBean.email}"/>
 	<input type="hidden" id="linkeManStr" name="linkeManStr" value="${linkeManStr}"/>
  	<input type="hidden" name="confirmtype" id="confirmtype" value="${hotelOrderFromBean.confirmtype}"/>
 	<input type="hidden" name="specialRequest" id="specialRequest" value="${hotelOrderFromBean.specialRequest}"/>
  	<!-- 标记会员是否登录，已登录为false;未登录为true; add shengwei.zuo 2009-11-15 -->
 	<input name= "direckbook" id="direckbook" type="hidden" value="${hotelOrderFromBean.direckbook}"/>
 	<input type="hidden" name="memberCd" id="memberCd" value="${memberCd}">
   
   <input type="hidden" name="rate" id="rate" value="${rate}" />	 
   <input type="hidden" name="priceNum" value="${hotelOrderFromBean.priceNum}" />
   <input type="hidden" name="payMethod"  value="${hotelOrderFromBean.payMethod}" />
   <input type="hidden" name="roomTypeId"  value="${hotelOrderFromBean.roomTypeId}" />
   <input type="hidden" name="roomTypeName"  value="${hotelOrderFromBean.roomTypeName}" />
   <input type="hidden" name="quotaType"  value="${hotelOrderFromBean.quotaType}" />
   <input type="hidden" name="currency"  value="${hotelOrderFromBean.currency}"/>
   <input type="hidden" name="currencyStr"  value="${hotelOrderFromBean.currencyStr}"/>     
   <input type="hidden" name="breakfastType"  value="${hotelOrderFromBean.breakfastType}" />
   <input type="hidden" name="breakfastNum"  value="${hotelOrderFromBean.breakfastNum}" />
 
 <input type="hidden" name="rate" id="rate" value="${rate}" />
 <%-- hotel 2.9.2 记录房型的最大入住人数 add by chenjiajie 2009-08-05 --%>
 <input type="hidden" id="maxPersons" name="maxPersons" value="${hotelOrderFromBean.maxPersons}" /> 
 <input type="hidden" name="saleDepartmentPay" id="saleDepartmentPay" value="${saleDepartmentPay}" /> <!--  ? -->
 
 <input type="hidden" name="flagme" id="flagme" value="${flagme}" /> <!--  ? -->
 <input type="hidden" name="datePriceStr" id="datePriceStr" value="${hotelOrderFromBean.datePriceStr}" />
 <input type="hidden" name ="title" id="title" value="${hotelOrderFromBean.title}" />
 
 <%-- add by wuyun v2.6必须面转预 2009-06-04 --%>     
 <input type="hidden" name="payToPrepay" id="payToPrepay" value="${hotelOrderFromBean.payToPrepay}" />
 <input type="hidden" name="returnAmount" id="returnAmount" value="${hotelOrderFromBean.returnAmount}" />
 
  <%-- 房型对应的床型ID字符串 add shengwei.zuo 2009-10-26 --%>
 <input type="hidden"  name="bedType" value="${hotelOrderFromBean.bedType}" />
 
	 
 
 <%-- add by chenkeming v2.6预订规则相关--%>			
 <input type="hidden" name="clauseRule" value="<c:out value='${reservation.clauseRule}' />" />
 <input type="hidden" name="modifyField"  value="<c:out value='${reservation.modifyField}' />" />			
 <input type="hidden" name="clauseStr"  value="<c:out value='${reservation.clauseStr}' />" />
 <input type="hidden" name="creditRemark"  value="<c:out value='${creditRemark}' />" />
 <input type="hidden" name="lateSuretyTime"  value="<c:out value='${reservation.lateSuretyTime}' />" />
 <input type="hidden" name="reservation.assureLetter"  value="<c:out value='${reservation.assureLetter}' />" />
 <input type="hidden" name="needCredit"  value="<c:out value='${reservation.needCredit}' />" />
 <input type="hidden" name="unCondition"  value="<c:out value='${reservation.unCondition}' />" />		
 <input type="hidden" name="overTimeAssure"  value="<c:out value='${reservation.overTimeAssure}' />" />
 <input type="hidden" name="roomsAssure"  value="<c:out value='${reservation.roomsAssure}' />" />
 <input type="hidden" name="rooms"  value="<c:out value='${reservation.rooms}' />" />
 <input type="hidden" name="nightsAssure"  value="<c:out value='${reservation.nightsAssure}' />" />
 <input type="hidden" name="nights"  value="<c:out value='${reservation.nights}' />" />
 <input type="hidden" name="reservSuretyPrice"  value="<c:out value='${reservation.reservSuretyPrice}' />" />
 <input type="hidden" name="balanceMode"  value="<c:out value='${reservation.balanceMode}' />" />
 <input type="hidden" name="advancePayTime"  value="<c:out value='${reservation.advancePayTime}' />" />
 <input type="hidden" name="cancelModifyStr"  value="<c:out value='${reservation.cancelModifyStr}' />" />
 <input type="hidden" name="prepayLimitType"  value="<c:out value='${reservation.prepayLimitType}' />" />
 <input type="hidden" name="firstPrice"  value="<c:out value='${reservation.firstPrice}' />" />	
 <input type="hidden" name="assureDetailStr"  value="${assureDetailStr}" />
 
 <%-- hotel 2.9.3 房间数限制 add by guzhijie 2009-09-17 --%>
 <input type="hidden" name="canRoomNumberWeb" id="canRoomNumberWeb" value="${hotelOrderFromBean.canRoomNumberWeb}"/>
 <input type="hidden" name="needAssure" id="needAssure" value="${hotelOrderFromBean.needAssure}"/>
 <%-- 原币种担保总金额 --%>
 <input type="hidden" name="orignalSuretyPrice" id="orignalSuretyPrice" value="<c:out value='${hotelOrderFromBean.orignalSuretyPrice}' />" />
 <%-- 人民币币种担保总金额 --%>   
 <input type="hidden" name="suretyPriceRMB" id="suretyPriceRMB" value="<c:out value='${hotelOrderFromBean.suretyPriceRMB}' />" />
 
 <%-- 标志是否使用了积分 --%>
 <input id="useUlmPoint" name="useUlmPoint" type="hidden" value="${hotelOrderFromBean.useUlmPoint}"/>
 <input id="ulmPoint" name="ulmPoint" type="hidden" value="${hotelOrderFromBean.ulmPoint}"/>
 <%-- 标志是否使用了代金券 --%>
 <input id="usedCoupon" name="usedCoupon" type="hidden" value="${hotelOrderFromBean.usedCoupon}"/>
 <input id="ulmCoupon" name="ulmCoupon" type="hidden" value="${hotelOrderFromBean.ulmCoupon}"/>
 
 <input id="orderPayType" name="orderPayType" type="hidden" value="${hotelOrderFromBean.orderPayType}"/>
 <input id="onlinePaytype" name="onlinePaytype" type="hidden" value="${hotelOrderFromBean.onlinePaytype}"/>
 
 <%-- //需另缴政府税的判断 add by shengwei.zuo 2009-12-1 --%>
 <input name= "roomIncTaxStr" id="roomIncTaxStr" type="hidden" value="<c:out value='${hotelOrderFromBean.roomIncTaxStr}' />"/>
 
 <%-- 订单页面 日历 的年份，和月份 --%>
 <%-- 根据大床/双床拼装的字符串 --%>
  
 <!-- 预付立减 add by shengwei.zuo -->
 <input name= "isReduction" id="isReduction" type="hidden" value="<c:out value='${hotelOrderFromBean.isReduction}' />"/>
 <input name= "benefitAmount" id="benefitAmount" type="hidden" value="<c:out value='${hotelOrderFromBean.benefitAmount}' />"/>
  
 <%-- 实收金额 --%>
 <input type="hidden" name="acturalAmount" id="acturalAmount" value="${hotelOrderFromBean.acturalAmount}">
 
 <%-- 是否更新保存历史入住人 true:保存，false:不保存 --%>
 <input type="hidden" name="isSavePerson"  id="isSavePerson" value="${hotelOrderFromBean.isSavePerson}" />
 
 <%-- cardId --%>
 <c:if test="${showCreditCard}">
 <input type="hidden" name="sid" id="sid" value="${sid}" />
 <input type="hidden" name="cardID" id="cardID" value="">
 </c:if>
     
 
 <%-- 营业部付款的相关参数  add by shengwei.zuo --%>
  <input type="hidden" name="deliveryCityID" id="deliveryCityID" value="${orFulfillment.deliveryCityID}">
  <input type="hidden" name="deliveryAddress" id="deliveryAddress" value="${orFulfillment.deliveryAddress}">
  <input type="hidden" name="fulfillPayDate" id="fulfillPayDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${orFulfillment.fulfillPayDate}" />" />
  <input type="hidden" name="deliveryTime" id="deliveryTime" value="${orFulfillment.deliveryTime}">
  <input type="hidden" name="deliveryTimeEnd" id="deliveryTimeEnd" value="${orFulfillment.deliveryTimeEnd}">
    
 <c:forEach items="${reservation.guarantees}" var="item" varStatus="i">
	  <c:choose>
	     <c:when test="${i.index==0}">
	        <input type="hidden" name="night" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${item.night}" />" />
	        <input type="hidden" name="notConditional" value="${item.notConditional}" />
	        <input type="hidden" name="latestAssureTime" value="${item.latestAssureTime}" />
	        <input type="hidden" name="overRoomNumber" value="${item.overRoomNumber}" />
	        <input type="hidden" name="overNightsNumber" value="${item.overNightsNumber}" />
	        <input type="hidden" name="assureType" value="${item.assureType}" />
	        <input type="hidden" name="assureLetter" value="${item.assureLetter}" />
	        <input type="hidden" name="assureCondiction" value="${item.assureCondiction}" />
	     </c:when>
	     <c:otherwise>
		    <input type="hidden" name="night_${i.index}" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${item.night}" />" />
	        <input type="hidden" name="notConditional_${i.index}" value="${item.notConditional}" />
	        <input type="hidden" name="latestAssureTime_${i.index}" value="${item.latestAssureTime}" />
	        <input type="hidden" name="overRoomNumber_${i.index}" value="${item.overRoomNumber}" />
	        <input type="hidden" name="overNightsNumber_${i.index}" value="${item.overNightsNumber}" />
	        <input type="hidden" name="assureType_${i.index}" value="${item.assureType}" />
	        <input type="hidden" name="assureLetter_${i.index}" value="${item.assureLetter}" />
	        <input type="hidden" name="assureCondiction_${i.index}" value="${item.assureCondiction}" />
	     </c:otherwise>
	  </c:choose>						
	</c:forEach>

  <input type="hidden" name="guaranteeNum" id="guaranteeNum" value="${guaranteeNum}"/> 

  <%--取消修改信息类 add by lixiaoyong v2.6 --%>
     <c:forEach items="${reservation.assureList}" var="item" varStatus="i">
          <c:choose>
             <c:when test="${i.index==0}">
             	<input type="hidden" name="OrAssureItemEvery.type" value="${item.type}" />
             	<input type="hidden" name="OrAssureItemEvery.firstDateOrDays" value="${item.firstDateOrDays}" />
             	<input type="hidden" name="OrAssureItemEvery.firstTime" value="${item.firstTime}" />
             	<input type="hidden" name="OrAssureItemEvery.secondDateOrDays" value="${item.secondDateOrDays}" />
             	<input type="hidden" name="OrAssureItemEvery.secondTime" value="${item.secondTime}" />
             	<input type="hidden" name="OrAssureItemEvery.scope" value="${item.scope}" />
             	<input type="hidden" name="OrAssureItemEvery.deductType" value="${item.deductType}" />
             	<input type="hidden" name="OrAssureItemEvery.percentage" value="${item.percentage}" />
             	<input type="hidden" name="OrAssureItemEvery.night" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${item.night}" />" />
             	<input type="hidden" name="OrAssureItemEvery.beforeDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${item.beforeDate}" />" />
             </c:when>
             <c:otherwise>
            	<input type="hidden" name="OrAssureItemEvery.type_${i.index}" value="${item.type}" />
                <input type="hidden" name="OrAssureItemEvery.firstDateOrDays_${i.index}" value="${item.firstDateOrDays}" />
                <input type="hidden" name="OrAssureItemEvery.firstTime_${i.index}" value="${item.firstTime}" />
                <input type="hidden" name="OrAssureItemEvery.secondDateOrDays_${i.index}" value="${item.secondDateOrDays}" />
                <input type="hidden" name="OrAssureItemEvery.secondTime_${i.index}" value="${item.secondTime}" />
                <input type="hidden" name="OrAssureItemEvery.scope_${i.index}" value="${item.scope}" />
                <input type="hidden" name="OrAssureItemEvery.deductType_${i.index}" value="${item.deductType}" />
                <input type="hidden" name="OrAssureItemEvery.percentage_${i.index}" value="${item.percentage}" />
                <input type="hidden" name="OrAssureItemEvery.night_${i.index}" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${item.night}" />" />
                <input type="hidden" name="OrAssureItemEvery.beforeDate_${i.index}" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${item.beforeDate}" />" />
             </c:otherwise>
          </c:choose>                       
        </c:forEach>
    <input type="hidden" name="assureNum" id="assureNum" value="${assureNum}"/>
    
    <%--价格明细类 add by lixiaoyong v2.6 --%>    
    <input type="hidden" name="detailNum" id="detailNum" value="${fn:length(order.priceList)}"/>
     <c:forEach items="${order.priceList}" var="item" varStatus="i">
          <c:choose>
             <c:when test="${i.index==0}">
                <input type="hidden" name="OrPriceDetail.night" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${item.night}" />" />
                <input type="hidden" name="OrPriceDetail.dayIndex" value="${item.dayIndex}" />
                <input type="hidden" name="OrPriceDetail.roomState" value="${item.roomState}" />
                <input type="hidden" name="OrPriceDetail.quantity" value="${item.quantity}" />
                <input type="hidden" name="OrPriceDetail.salePrice" value="${item.salePrice}" />
                <input type="hidden" name="OrPriceDetail.basePrice" value="${item.basePrice}" />
                <input type="hidden" name="OrPriceDetail.marketPrice" value="${item.marketPrice}" />
                <input type="hidden" name="OrPriceDetail.breakfastStr" value="${item.breakfastStr}" />
                <input type="hidden" name="OrPriceDetail.dateStr" value="${item.dateStr}" />
                <input type="hidden" name="OrPriceDetail.hasReserv" value="${item.hasReserv}"/>
                <input type="hidden" name="OrPriceDetail.beforeTime" value="${item.beforeTime}" />
                <input type="hidden" name="OrPriceDetail.beforeDayNum" value="${item.beforeDayNum}" />
                <input type="hidden" name="OrPriceDetail.continueDay" value="${item.continueDay}" />
                <input type="hidden" name="OrPriceDetail.mustDate" value="${item.mustDate}" />
                <input type="hidden" name="OrPriceDetail.assureCond" value="${item.assureCond}" />
                <input type="hidden" name="OrPriceDetail.assureType" value="${item.assureType}" />
                <input type="hidden" name="OrPriceDetail.balanceMode" value="${item.balanceMode}" />
                <input type="hidden" name="OrPriceDetail.prepayTime" value="${item.prepayTime}"/>
             </c:when>
             <c:otherwise>
                <input type="hidden" name="OrPriceDetail.night_${i.index}" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${item.night}" />" />
                <input type="hidden" name="OrPriceDetail.dayIndex_${i.index}" value="${item.dayIndex}" />
                <input type="hidden" name="OrPriceDetail.roomState_${i.index}" value="${item.roomState}" />
                <input type="hidden" name="OrPriceDetail.quantity_${i.index}" value="${item.quantity}" />
                <input type="hidden" name="OrPriceDetail.salePrice_${i.index}" value="${item.salePrice}" />
                <input type="hidden" name="OrPriceDetail.basePrice_${i.index}" value="${item.basePrice}" />
                <input type="hidden" name="OrPriceDetail.marketPrice_${i.index}" value="${item.marketPrice}" />
                <input type="hidden" name="OrPriceDetail.breakfastStr_${i.index}" value="${item.breakfastStr}" />
                <input type="hidden" name="OrPriceDetail.dateStr_${i.index}" value="${item.dateStr}" />
                <input type="hidden" name="OrPriceDetail.hasReserv_${i.index}" value="${item.hasReserv}"/>
                <input type="hidden" name="OrPriceDetail.beforeTime_${i.index}" value="${item.beforeTime}" />
                <input type="hidden" name="OrPriceDetail.beforeDayNum_${i.index}" value="${item.beforeDayNum}" />
                <input type="hidden" name="OrPriceDetail.continueDay_${i.index}" value="${item.continueDay}" />
                <input type="hidden" name="OrPriceDetail.mustDate_${i.index}" value="${item.mustDate}" />
                <input type="hidden" name="OrPriceDetail.assureCond_${i.index}" value="${item.assureCond}" />
                <input type="hidden" name="OrPriceDetail.assureType_${i.index}" value="${item.assureType}" />
                <input type="hidden" name="OrPriceDetail.balanceMode_${i.index}" value="${item.balanceMode}" />
                <input type="hidden" name="OrPriceDetail.prepayTime_${i.index}" value="${item.prepayTime}"/>
             </c:otherwise>
          </c:choose>                       
        </c:forEach>

	<%-- 入住人信息 --%>    

	<%-- 代金券明细 --%>    
	<c:forEach items="${couponRecords}" var="couponItem" varStatus="i">            		
		<c:choose>
			<c:when test="${i.index==0}">
				<input type="hidden" name="couponCode" id="couponCode" value="${couponItem.couponCode}"/> 
				<input type="hidden" name="couponName" id="couponName" value="${couponItem.couponName}"/>
				<input type="hidden" name="couponValue" id="couponValue" value="${couponItem.couponValue}"/>
				<input type="hidden" name="couponBeginDate" id="couponBeginDate" value="${couponItem.couponBeginDate}"/>
				<input type="hidden" name="couponEndDate" id="couponEndDate" value="${couponItem.couponEndDate}"/>
				<input type="hidden" name="description" id="description" value="${couponItem.description}"/>
			</c:when>
			<c:otherwise>
				<input type="hidden" name="couponCode_${i.index}" id="couponCode_${i.index}" value="${couponItem.couponCode}"/> 
				<input type="hidden" name="couponName_${i.index}" id="couponName_${i.index}" value="${couponItem.couponName}"/>
				<input type="hidden" name="couponValue_${i.index}" id="couponValue_${i.index}" value="${couponItem.couponValue}"/>
				<input type="hidden" name="couponBeginDate_${i.index}" id="couponBeginDate_${i.index}" value="${couponItem.couponBeginDate}"/>
				<input type="hidden" name="couponEndDate_${i.index}" id="couponEndDate_${i.index}" value="${couponItem.couponEndDate}"/>
				<input type="hidden" name="description_${i.index}" id="description_${i.index}" value="${couponItem.description}"/>
			</c:otherwise>		            		
		</c:choose>	            		
	</c:forEach>
	<input type="hidden" name="couponNum" id="couponNum" value="${couponNum}"/>
	
</form>

<script type="text/javascript">
  var payMethod ='${hotelOrderFromBean.payMethod}';
   var payToPrepay = '${hotelOrderFromBean.payToPrepay}'
   var needAssure = '${hotelOrderFromBean.needAssure}';
   if(payMethod == "pay" && needAssure == "false" && payToPrepay=="false"){
       document.orderCheckForm.submit();
   }
</script>

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

<!--=S main -->
<div class="w960">
    <!--=S 预订信息 -->
<div class="orderbox mgb10">
    	<div class="boxtit"><h2>预订信息</h2></div>
        <div class="boxcont orderlist">
        	<ul>
            	<li><strong>酒店名称：</strong>${hotelOrderFromBean.hotelName}</li>
                <li><strong>入住房型及间数：</strong>${hotelOrderFromBean.roomTypeName}  &nbsp;&nbsp;${hotelOrderFromBean.roomQuantity}间</li>
                <li><strong>床型：</strong><h:convert name="bedType" value="${hotelOrderFromBean.bedType}"></h:convert></li>
                <li><strong>住店时间：</strong>
				<fmt:formatDate value="${hotelOrderFromBean.checkinDate}" pattern="yyyy-MM-dd"/> 至 <fmt:formatDate value="${hotelOrderFromBean.checkoutDate}" pattern="yyyy-MM-dd"/>&nbsp;&nbsp;共${hotelOrderFromBean.days }晚
				</li>
            </ul>
            <ul><li><strong>入住人：</strong>${linkeManStr}</li></ul>
            <ul>
                <li><strong>联系人：</strong> ${hotelOrderFromBean.linkMan}</li>
                <li><strong>联系电话：</strong>${hotelOrderFromBean.mobile}</li>
                <li><strong>电子邮箱：</strong>${hotelOrderFromBean.email}</li>
            </ul>
            <p class="total">房价总计：
            <c:if test="${hotelOrderFromBean.payMethod=='pay' and  false==hotelOrderFromBean.payToPrepay}">
                <strong>${hotelOrderFromBean.currencyStr}<span>${hotelOrderFromBean.priceNum * hotelOrderFromBean.roomQuantity}</span>(到店付款)</strong>  
            </c:if>
             <c:if test="${hotelOrderFromBean.payMethod=='pre_pay' || hotelOrderFromBean.payToPrepay}">
                <strong style="font-family:Arial, Helvetica, sans-serif;">&yen;<span id="roomPriceSum"></span>(提前付款)</strong>
                     <script type="text/javascript">
				         var o = Math.ceil(${hotelOrderFromBean.priceNum * hotelOrderFromBean.roomQuantity * rate});
	                     document.getElementById("roomPriceSum").innerHTML = o ;
                     </script>
                <c:if test="${hotelOrderFromBean.useUlmPoint}">
		        	 <strong style="font-family:Arial, Helvetica, sans-serif;">积分抵用:&yen;<c:out value="${hotelOrderFromBean.ulmPoint/100}"></c:out></strong> +
		        </c:if>
		        <c:if test="${hotelOrderFromBean.usedCoupon}">
		        	 <strong style="font-family:Arial, Helvetica, sans-serif;">代金券抵用:&yen;<c:out value="${hotelOrderFromBean.ulmCoupon}"></c:out></strong> +
		        </c:if>
		         <c:if test="${hotelOrderFromBean.usedCoupon or hotelOrderFromBean.useUlmPoint}">
		             <strong style="font-family:Arial, Helvetica, sans-serif;">实际支付金额:&yen;${hotelOrderFromBean.acturalAmount}</strong>
		         </c:if>
            </c:if>
            
            返现金额：<strong style="font-family:Arial, Helvetica, sans-serif;">&yen;<c:out value="${hotelOrderFromBean.returnAmount * hotelOrderFromBean.roomQuantity}" /></strong><a href="http://www.mangocity.com/help/recash.html"  name="recashExplain" target="_blank">(返现说明)</a>
            <a href="javascript:void(0);" id="showEveryPrice_js" >查看每日价格详情</a>
             </p>
        </div>
        <%@ include file="/WEB-INF/jsp/commonjsp/everyPriceDetail.jsp" %>
  </div>    
    <!--=E 预订信息 -->
 
 	<%-- 如果是(预付单并且是用信用卡支付)或者(面付信用卡担保)，显示信用卡填写页面 --%>    
    <c:if test="${showCreditCard}">
    <div class="creditpaywrap">
         <%-- payMethodStrForShow 传递给会员的参数，预付(2显示"支付"),面付(1显示"担保") --%>
        <c:choose>
        	
	        <c:when test="${hotelOrderFromBean.payMethod eq 'pre_pay' || hotelOrderFromBean.payToPrepay}">
	        	<c:set var="payMethodStrForShow" value="pay"/>
	        	<c:set var="faceAmount" value="${hotelOrderFromBean.acturalAmount}" scope="page"/>
	        	<c:set var="paymethodChn" value="支付"/>
	        </c:when>
	        
	        <c:otherwise>
	        	<c:set var="payMethodStrForShow" value="assure"/>
	        	<c:set var="faceAmount" value="${hotelOrderFromBean.suretyPriceRMB}" scope="page"/>
	        	<c:set var="paymethodChn" value="担保"/>
	        </c:otherwise>
        </c:choose>
        
      	<div class="mgcBoxWrap">
     		<div class="title">
        		<h2>信用卡${paymethodChn}</h2>
        		<span class="creditUse">
           			<a href="#">信用卡使用安全保障
              			<em>
                 			<strong>支付安全保障</strong>
                 			<ins>
                    			<ol>
			                       <li>1.港中旅集团实力雄厚，诚信经营历史品牌。</li>
			                       <li>2.多家银行及信用卡组织的授信，实名消费，安全消费。</li>
			                       <li>3.先进的网络安全保障技术，特设专线连接及加密方法。</li>
			                       <li>4.全面标准化的管理监控机制，严格的人员筛选、专职配备及角色分工。 </li>
                    			</ol>
                 			</ins>
              			</em>
           			</a>
        		</span>
     		</div>
     
     		<div class="mgcBox">
        		<p><strong>${paymethodChn}金额：<em class="yen orange">&yen;${faceAmount}</em></strong></p>
        	<div class="danbao">
        	<c:choose>
        		<c:when test="${payMethodStrForShow eq 'pay'}">
        		<h3>信用卡支付需知</h3>
				<p>
					<strong>请准确提供您的信用卡资料！</strong>如果您提供的信用卡信息无效或有误，芒果网将不能为您确认房间预订成功。<br>
					<strong>预付预订责任：</strong><br>
							no-show或取消订单扣除房费：预付订单经芒果网确认后，该房型一旦预订确认成功将不接受免费取消或修改，如需取消或修改将按酒店规定扣取您的全部预付金额。如果客人届时没有入住，酒店将视为no-show，也将按酒店规定扣除相应的房费。  		   	  	
				</p>
        		</c:when>
        			
        		<c:otherwise>
        		<h3>信用卡担保需知</h3>  
				<p>
					<strong>请仔细阅读以下条款，全部同意后填写信用卡信息：</strong><br>
				    <strong>担保订单处理规定：</strong>您提交信用卡担保订单后，芒果网将尽快向酒店进行“担保预订”，在酒店确认预订后，芒果网将给您确认。如果您提供
							的信用卡信息无效或有误，您的担保不能生效，酒店将不能为您确认或保留房间。<br>
					<strong>担保订单保留时间：</strong>得到芒果网酒店预订成功的确认后，房间将为您保留到次日中午12:00。<br>
					<strong>担保预订责任：</strong><br>
							1 no-show订单：担保预订进芒果网确认后酒店即为您保留房间。如果您届时未能入住，芒果网将按酒店no-show规定在您的信用卡内扣除全部担保房费。<br>
							2 取消修改订单：<br>
							3 您提供信用卡资料后并不代表您的预订已经得到确认，如果酒店不能确认您的预订，我们将会取消在您信用卡帐户上冻结的费用。
				</p>
        		</c:otherwise>
        		
        	</c:choose>
 
        	</div>
        		<p><input id="payAgree" name=""payAgree"" type="checkbox" value="1" /> 我已理解并同意以上担保/支付条款</p>
     		</div>

        
        <!-- 信用卡页面begin -->
        <iframe id="creditIFrame" name="creditIFrame" class="boxtit" src="${creditPath}?sid=${sid}&memberCd=${member.membercd}&tradeType=${payMethodStrForShow}" frameborder="no" width="700px" border="0" scrolling="no" allowtransparency="yes" style="margin: 0px;padding: 0px"></iframe>
        <!-- 信用卡页面end -->
        </div>
	</div>
	</c:if>
	    
    <div class="fillsub"><a href="javascript:void(0)" class="green" name="goback" onclick="history.go(-1)">&lt;&lt;返回上一步</a> <input name="confirmSubmit" id="confirmSubmit" type="button" class="btn96x27" value="提交订单" /></div>
    
</div>
<!--=E main -->

<!--=S footer-->
<jsp:include page="../commonjsp/_footer.jsp" flush="true" />
<!--=E footer -->
 
<script type="text/javascript" >
$(document).ready(function(){
   // 处理https的seo
	var objs = $('a[name="navigationPath"]');
	var hrefArray = document.location.href.split('/');
	if(objs != undefined && objs != null) {
		for(var i=0;i<objs.length;i++) {
			if(objs[i].href.indexOf("http") != -1) {
				objs[i].href = objs[i].href.replace('https','http');
			}else {
				objs[i].href = hrefArray[0].replace('https','http') + '://' + hrefArray[2] + '/' + objs[i].href;
			}
		}
	}			
});

//“提交订单”事件处理
jQuery("#confirmSubmit").click(function () {
	//信用卡“支付”或“担保”
   	<c:if test="${showCreditCard}">
   		var agreeState = document.getElementById('payAgree');
   		if(!agreeState.checked) {
	   		alert('提供信用卡前，需要确认支付或担保条款!');
	   		return false;
   		}
   		$('#confirmSubmit').attr('disabled',true);
		document.getElementById('creditIFrame').contentWindow.submitcreditcard(dopay);
   	</c:if>
   	
   	//非信用卡“支付”或者“担保”
   	<c:if test="${!showCreditCard}">	
   		//普通方式	
		var url = "hotel-complete.shtml";		
		//改变url并提交
		jQuery("#orderCheckForm").attr("action",url).submit();
	</c:if>
	
	     //异步记录是否点击提交按钮     
     	jQuery.ajax({
   		type: "post",
   		 url: "hotel-orderRecord!recordSubmitButton.shtml"
         });
});

//财务回调函数
function dopay(cardObj) {
	if(cardObj != null && cardObj != undefined) {
		if(cardObj.resultcode == '0' || cardObj.resultcode == '1' || cardObj.resultcode == '2') {
			jQuery("#cardID").val(cardObj.cardId);
			var url = location.href;
			url = url.replace("hotel-check","hotel-complete");
			jQuery("#orderCheckForm").attr("action",url).submit();
		}else {
			alert('信用卡提交失败，请重新提交！');
			$('#confirmSubmit').attr('disabled',false);
		}
	}
}

	//调整iframe高度
function initIframeHeight() {
				// init ifrme height
				var iframe = document.getElementById('creditIFrame');
				iframe.style.display="block" 
				iframe.height= iframe.contentWindow.document.documentElement.scrollHeight;
				var addheight= 20; 
				if (document.getElementById) {                  
					if (iframe) {                      
						if (iframe.contentDocument && iframe.contentDocument.body.offsetHeight) { 
							var iframe_height=parseInt(iframe.contentDocument.body.offsetHeight)+addheight;                                         
							iframe.height = iframe_height+"px"; 
						} else if (iframe.Document && iframe.Document.body.scrollHeight) { 
							var iframe_height=parseInt(iframe.Document.body.scrollHeight)+addheight;                                               
							iframe.height = iframe_height+"px";                                                           
						}
					}
				}
			}
</script> 
</body>
</html>