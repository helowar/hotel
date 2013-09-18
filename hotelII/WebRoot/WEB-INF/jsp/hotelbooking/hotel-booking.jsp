<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="core"%>
<%@ taglib prefix="fmt" uri="fmt"%>
<%@ taglib prefix="fn" uri="fn"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="酒店查询,酒店,酒店预订,订商务酒店,快捷酒店,星级酒店." />
<meta name="description" content="【酒店预订】芒果网为您的出行提供方便的酒店预订服务，订商务酒店、快捷酒店、星级酒店当然选芒果网，国内酒店查询预订全国免费预订电话 40066-40066 ." />
<title>【酒店预订】酒店查询 订国内商务酒店、快捷酒店、星级酒店首选芒果网！</title>
<link rel="stylesheet" type="text/css" href="http://hres.mangocity.com/css/h/2011/hotel2011.css" />
<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/w/module/datepicker.css" />
<script type="text/javascript" src="http://wres.mangocity.com/js/home/cloud/js/jquery-1.3.2.js"></script>
<link rel="stylesheet" type="text/css" href="http://hres.mangocity.com/css/jquery.alert.css"/>
<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/w/module/mango_online.css" />
<script type="text/javascript" src="js/jQuery/jquery.alert.js"></script>	
<script type="text/javascript" src="http://hres.mangocity.com/js/mangocity.simplemodal.js"></script>
</head>

<body>
<jsp:include page="../commonjsp/_header.jsp" flush="true" />
<%-- =S top bar --%>
<div class="w960 hn-topbar">
    <div class="status step3">
        <ul>
            <li>查询</li>
            <li>选择</li>
            <li class="choice">预订</li>
            <li>完成</li>
        </ul>
    </div>
     <div class="crumbs">您现在的位置：<a href="http://www.mangocity.com" class="green" name="firstPage" >首页</a> &gt; <a href="hotel-search.shtml" class="green" name="domesticHotel" >国内酒店</a> &gt; 
           <a href="list-${fn:toLowerCase(hotelOrderFromBean.cityCode)}.html" class="green" name="daohangcity">${hotelOrderFromBean.cityName}酒店</a>
           &gt; <a href='jiudian-${fn:toLowerCase(hotelOrderFromBean.hotelId)}.html' class="green" name="daohangHotel">${hotelOrderFromBean.hotelName}</a> &gt; <em>订单填写</em>
    </div>
</div>
<%-- =E top bar--%>

<%-- =S main --%>
<div class="w960">
    <%--=S 订单填写 --%>
    <div class="orderfill">
    	<div class="orderbar">
        	<div class="hotelname" style="line-height:24px"><h1>${hotelOrderFromBean.hotelName}</h1>
                <c:choose>    
            		<c:when test="${fn:indexOf(hotelVO.hotelStar,'star') ge 0}">     
            			<em class="hotelstar ${hotelVO.hotelStar}" ></em>		    
            		</c:when>    
            		<c:otherwise>     
            			<em class="hotelstarName" >&nbsp;&nbsp;${hotelVO.hotelStar}</em>   
            		</c:otherwise>   
            	</c:choose>
            </div>
            <p class="simpleinfo">
                <span>入住房型：${hotelOrderFromBean.roomTypeName}</span>
                <span>入住日期：<label id ="checkinDate"><fmt:formatDate pattern="yyyy年MM月dd日" value="${hotelOrderFromBean.checkinDate}"/></label></span>
                <span>退房日期：<label id="checkoutDate"><fmt:formatDate pattern="yyyy年MM月dd日" value="${hotelOrderFromBean.checkoutDate}"/></label></span>
                <span>共<strong>${hotelOrderFromBean.days}</strong> 晚</span>
                <span><em id="changedatebtn" class="changetime" name="changedatebtn" >修改日期</em></span>

            </p>
        </div>
        
        <div class="orderform">
            <%--=S 表单 --%>
        	<div>
            	<ul>
                	<li>
                    	<label><strong>*</strong>预订间数：</label><select name="roomNumSelect" id="roomNumSelect" onclick="__ozclk();return false;">
                        <option value="1">1间</option>
                        <option value="2">2间</option>
                        <option value="3">3间</option>
                        <option value="4">4间</option>
                        <option value="5">5间</option></select> 
                    	<label><strong>*</strong>床型：</label><select name="bedtypeSelect" id="bedtypeSelect" onclick="__ozclk();return false;"></select>
                        <label><strong>*</strong>到店时间：</label>
                        <select name="arrivalHotelTime" id="arrivalHotelTime" onclick="__ozclk();return false;">
                             <option value="06:00-09:00" > 06:00-09:00 </option>
							 <option value="07:00-10:00" > 07:00-10:00 </option>
				  			 <option value="08:00-11:00" > 08:00-11:00 </option>
							 <option value="09:00-12:00" > 09:00-12:00 </option>
				             <option value="10:00-13:00" > 10:00-13:00 </option>
				             <option value="11:00-14:00" > 11:00-14:00 </option>
				             <option value="12:00-15:00" > 12:00-15:00 </option>
				             <option value="13:00-16:00" > 13:00-16:00 </option>
				             <option value="14:00-17:00" selected> 14:00-17:00 </option>
				             <option value="15:00-18:00" > 15:00-18:00 </option>
				             <option value="16:00-19:00" > 16:00-19:00 </option>
				             <option value="17:00-20:00" > 17:00-20:00 </option>
				             <option value="18:00-21:00" > 18:00-21:00 </option>
				             <option value="19:00-22:00" > 19:00-22:00 </option>
				             <option value="20:00-23:00" > 20:00-23:00 </option>
				             <option value="21:00-23:59" > 21:00-23:59 </option>                                         
                        </select>
                    </li>
                	<li>
                    	<label><strong>*</strong>客人姓名：</label>
                    	<span  id="fellowPersonLabel">
                    	     <input type='text' name='fellowName' value="客人1姓名" id="fellowNameId1" style="color:#999;" onclick="__ozclk();return false;"/>
                        </span>
                    </li>
                    
                      <%-- 香港澳门城市标示 --%>
                     <input type="hidden" id="flagHKGORMAC" value="false" />    
                     <c:if test="${hotelOrderFromBean.currency =='HKD' or hotelOrderFromBean.currency =='MOP'}">
                    	<li style="padding-left:57px; height:27px;">
                               <script type="text/javascript">
                                        jQuery("#flagHKGORMAC").val(true);
                                </script>
                                 <span style="color:#999;" class="graytext">请填写与入住人所持证件一致的英文姓名(格式：姓/名)；到达酒店前台，请提供入住人姓名办理入住手续。</span>
                    	</li>
                    </c:if>  
                    
                	<li>
                    	<label><strong>*</strong>联系人&nbsp;&nbsp;：</label><input name="linkmanName" id="linkmanName" type="text" onclick="__ozclk();return false;"/> 
                        <label><strong>*</strong>手机号码：</label><input name="linkmanTelephone" id="linkmanTelephone" type="text" onclick="__ozclk();return false;"/>
                        <label><strong></strong>电子邮箱：</label><input name="linkmanEmail" id="linkmanEmail" type="text" onclick="__ozclk();return false;"/><em></em>
                    </li>
                    
                </ul>
                <p class="total">房价总计：<strong style="font-family:Arial, Helvetica, sans-serif;">${hotelOrderFromBean.currencyStr}<span id="allPriceSum">${hotelOrderFromBean.priceNum}</span>(到店付款)</strong>  
                      返现金额：<strong style="font-family:Arial, Helvetica, sans-serif;">&yen;<span id="returnCash">${hotelOrderFromBean.returnAmount}</span></strong><a href="http://www.mangocity.com/help/recash.html" name="recashExplain" target="_blank">(返现说明)</a>     
                <a href="javascript:void(0);" id="showEveryPrice_js" >查看每日价格详情</a></p>
            </div>
            <%--=E 表单 --%>
            <%@ include file="/WEB-INF/jsp/commonjsp/everyPriceDetail.jsp" %>
			
            <%--=S 特别提示 --%>
            <div class="spaciltip">
            	<div class="tipwrap" id="specialHint">
                	<strong>特别提示</strong>
                    <ol></ol>
                </div>
            </div>
			<%--=E 特别提示 --%>
			<%-- 重复下单提示浮动层 --%>
			<div id="id_NoticDuplication" style='display:none'>
			    <div class="ht_od_tc" id="loadNoticDuplication">
			    </div>
			</div>			
            <div class="spaciltip">
            	<div class="tipwrap">
                	<c:if test="${not empty hotelOrderFromBean.tipInfo}">
	 	 	            <strong>酒店提示信息：</strong>
	 	 	            <br />
			            <span style="line-height:20px;"> ${hotelOrderFromBean.tipInfo}</span>
			             <br />            
	                </c:if>
	       
	                <c:if test="${not empty presaleList}">
	 	 	           <strong>酒店促销信息：</strong>
	 	 	           <br />
			              <span> <c:forEach items="${presaleList}" var="item">
			                        ${item.presaleContent}
			                     </c:forEach>
			              </span>
			           <br />   
	                  </c:if>
	                  
	                  <c:if test="${ not empty additionalServe}">
	                       <strong>酒店附加服务：</strong>
	                       <br />                              
			               <c:if test="${not empty additionalServe.bedServes}">加床价服务：
			               		<c:forEach items="${additionalServe.bedServes}" var="bedItem">
			               		<c:choose>
			               		<c:when test="${1 == bedItem.addType}">
			               		<c:out value="${bedItem.validDate}"></c:out>&nbsp;<c:out value="${hotelOrderFromBean.currency}" /><s:property value="getRoomPrice(${bedItem.amount})"/>/床
					            </c:when>
					            <c:otherwise>
					            <c:out value="${bedItem.validDate}"></c:out>&nbsp;房费的<s:property value="getRoomPrice(${bedItem.amount} * 100)"/>%/床
					            </c:otherwise>
					            </c:choose>
					            </c:forEach>&nbsp;
					       </c:if>
					       <c:if test="${!empty additionalServe.chineseServes || !empty additionalServe.westernServes || !empty additionalServe.buffetServes}">早餐服务：
					                        <c:if test="${not empty additionalServe.chineseServes}">中早：<c:forEach items="${additionalServe.chineseServes}" var="buffetItem"><c:out value="${buffetItem.validDate}"></c:out>&nbsp;<c:out value="${hotelOrderFromBean.currency}" /><s:property value="getRoomPrice(${buffetItem.amount})"/>/人
					                            	</c:forEach>
			                            	</c:if>		
					                        <c:if test="${not empty additionalServe.westernServes}">西早：<c:forEach items="${additionalServe.westernServes}" var="buffetItem"><c:out value="${buffetItem.validDate}"></c:out>&nbsp;<c:out value="${hotelOrderFromBean.currency}" /><s:property value="getRoomPrice(${buffetItem.amount})"/>/人
					                            	</c:forEach>
			                               	</c:if>				                            				                        
					                        <c:if test="${not empty additionalServe.buffetServes}">自助早：<c:forEach items="${additionalServe.buffetServes}" var="buffetItem"><c:out value="${buffetItem.validDate}"></c:out>&nbsp;<c:out value="${hotelOrderFromBean.currency}" /><s:property value="getRoomPrice(${buffetItem.amount})"/>/人
					                            	</c:forEach>
			                            	</c:if>
			               </c:if>
                      </c:if>   
                     <p>如果您有特殊要求,请点击 <a id="showspacilerequire" href="javascript:void(0);" name="specialRequest" class="green">这里</a></p>               	       
                </div>
            </div>
            
            
            <div id="spacilerequire" style="display:none;">
                <%--=S 特殊要求 --%>
                <div class="specailrequire">
                    <div class="sprqtip"><strong>特殊要求</strong><span>(我们将向酒店尽量协商您的要求，但不能保证一定能够满足，请谅解！)</span></div>
                    <div class="sprqcont">
                        <input name="specialrequireInput" type="checkbox" value="宽带" class="iradio" /><label>宽带</label>
                        <input name="specialrequireInput" type="checkbox" value="高层" class="iradio" /><label>高层</label>
                        <input name="specialrequireInput" type="checkbox" value="无烟房" class="iradio" /><label>无烟房</label>
                        <input name="specialrequireInput" type="checkbox" value="吸烟房" class="iradio" /><label>吸烟房</label>
                    </div>
                </div>
                <%--=E 特殊要求 --%>
                
                <%--=S 其他要求 --%>
                <div class="ohtrequire">
                    <label>其他要求</label>
                    <div>
                        <textarea name="specialrequireTextarea" id="specialrequireTextarea" onkeyup="isMax()"  cols="" rows="" class="w597"></textarea>
                    </div>
                </div>
                <%--=E 其他要求 --%>
                </div>
        </div>
        
    </div>
    <%--=E 订单填写 --%>
    
    <%--=S 支付方式 --%>
    <div class="orderbox">
    	<div class="boxtit"><h2>支付方式</h2></div>
        <div class="boxcont">
        	<p>酒店前台面付</p>
        </div>
    </div>
    <%--=E 支付方式 --%>
    
    <p class="attention">注：请确保您所填的以上信息准确有效，以免耽误订单处理。为及早确认您预订的房间，请您尽快“提交”订单！</p>
    
    <div class="fillsub"> <input name="submitOrder" onclick="paySubmitOrder();" id="submitOrder" type="submit" class="btn96x27" value="提交订单" /></div>
	<input type="hidden" id="used_orderComfirmUrl" value="${orderComfirmUrl}" />
</div>
<%--=E main --%>

     <form id="submitForm" name="submitForm" action ="hotel-check.shtml" method="post" >
     <s:token />
      <%-- 日期，时间 --%>
        <input type="hidden" name="checkinDate"	value="<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkinDate}"/>">
        <input type="hidden" name="checkoutDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkoutDate}"/>">
        <input type="hidden" name="days" value="${hotelOrderFromBean.days}" />
        <input type="hidden" name="arrivalTime" id="f_arrivalTime" value="">
		<input type="hidden" name="latestArrivalTime" id="f_latestArrivalTime" value="">
       <%-- 酒店 --%>
	    <input type="hidden" name="cityId"  value="${hotelOrderFromBean.cityCode}" />
	    <input type="hidden" name="cityCode" value="${hotelOrderFromBean.cityCode}" />
	    <input type="hidden" name="hotelId" id="checkDup_hotelId"value="${hotelOrderFromBean.hotelId}" />
	    <input type="hidden" name="hotelName" id="hotelName"value="${hotelOrderFromBean.hotelName}" />
	    <input type="hidden" name="hotelStar" value="${hotelOrderFromBean.hotelStar}"/>
	    
	    <%--酒店提示信息--%>
	    <input type="hidden" name="tipInfo" value="${hotelOrderFromBean.tipInfo}"/>
	    
	    <%-- 房型 --%>
	    <input type="hidden" name="roomTypeId" id="checkDup_roomTypeId"value="${hotelOrderFromBean.roomTypeId}" />
	    <input type="hidden" name="roomTypeName" value="${hotelOrderFromBean.roomTypeName}" />
	    <input type="hidden" name="roomQuantity" id="f_roomQuantity" value="1" />
	    <input type="hidden" name="bedType" id="f_bedType" value="" />
	    <input type="hidden" name="roomChannel" value="${hotelOrderFromBean.roomChannel}" />
	    
		<%-- 会员的 --%>
	    <input type="hidden" name="linkMan" id="f_linkMan" value="" />
        <input type="hidden" name="linkeManStr" id="f_linkeManStr" value="" />
        <input type="hidden" name="mobile" id="f_mobile" value="" />	
        <input type="hidden" name="email" id="f_email" value="" />	
	    
	    <input type="hidden" name="rate" id="rate" value="${rate}" />	
	    <input type="hidden" name="payMethod" id="f_payMethod" value="${hotelOrderFromBean.payMethod}" />
	    <input type="hidden" name="priceNum" value="${hotelOrderFromBean.priceNum}" />
        <input type="hidden" name="childRoomTypeId" value="${hotelOrderFromBean.childRoomTypeId}" />
	    <input type="hidden" name="childRoomTypeName" value="${hotelOrderFromBean.childRoomTypeName}" /> 
	    <input type="hidden" name="currency" value="${hotelOrderFromBean.currency}"/>   
	    <input type="hidden" name="breakfastType" id="breakfastType" value="${hotelOrderFromBean.breakfastType}" />
	    <input type="hidden" name="breakfastNum" id="breakfastNum" value="${hotelOrderFromBean.breakfastNum}" />
	    <input type="hidden" name="payToPrepay"  value="${hotelOrderFromBean.payToPrepay}" />
	    <input type="hidden" name="returnAmount" id="f_returnAmount" value="${hotelOrderFromBean.returnAmount}" />
	    <%-- 担保相关 --%>
	    <input type="hidden" name="payNeedAssure" id="f_payNeedAssure" value="false" />
		<input type="hidden" name="assureDetailStr" id="f_assureDetailStr" value="" />
		
		 <%-- 修改取消信息 --%>
		<input type="hidden" name="cancelModifyItem" id="f_cancelModifyItem" value="${bookhintCancelAndModifyStr}"/>
		                                  
	<input type='hidden'  value='' name= 'specialRequest' id ="f_specialRequest" />
	 
	<input type="hidden" name="clauseRule" id = "clauseRule" value="<c:out value='${reservation.clauseRule}' />" />
	 <input type="hidden" name="modifyField" id="modifyField" value="<c:out value='${reservation.modifyField}' />" />			
	 <input type="hidden" name="clauseStr" id="modifyField" value="<c:out value='${reservation.clauseStr}' />" />
	 <%-- modified by lixiaoyong 2009-05-13 v2.6 昨晚担保时间已经不采用数字方式，原来读取方式要改变--%>
	 <input type="hidden" name="lateSuretyTime" id="modifyField" value="<c:out value='${reservation.lateSuretyTime}' />" />
	 <input type="hidden" name="reservation.assureLetter" id="modifyField" value="<c:out value='${reservation.assureLetter}' />" />
	 <input type="hidden" name="needCredit" id="modifyField" value="<c:out value='${reservation.needCredit}' />" />
	 <input type="hidden" name="unCondition" id="modifyField" value="<c:out value='${reservation.unCondition}' />" />		
	 <input type="hidden" name="overTimeAssure" id="modifyField" value="<c:out value='${reservation.overTimeAssure}' />" />
	 <input type="hidden" name="roomsAssure" id="modifyField" value="<c:out value='${reservation.roomsAssure}' />" />
	 <input type="hidden" name="rooms" id="modifyField" value="<c:out value='${reservation.rooms}' />" />
	 <input type="hidden" name="nightsAssure" id="modifyField" value="<c:out value='${reservation.nightsAssure}' />" />
	 <input type="hidden" name="nights" id="modifyField" value="<c:out value='${reservation.nights}' />" /> 
	 <input type="hidden" name="reservSuretyPrice" id="modifyField" value="<c:out value='${reservation.reservSuretyPrice}' />" />
	 <input type="hidden" name="balanceMode" id="modifyField" value="<c:out value='${reservation.balanceMode}' />" />
 	 <input type="hidden" name="advancePayTime" id="modifyField" value="<c:out value='${reservation.advancePayTime}' />" />
	 <input type="hidden" name="cancelModifyStr" id="modifyField" value="<c:out value='${reservation.cancelModifyStr}' />" />
	 <!-- modified by wuyun -->
	 <input type="hidden" name="prepayLimitType" id="modifyField" value="<c:out value='${reservation.prepayLimitType}' />" />
 	 <input type="hidden" name="firstPrice" id="modifyField" value="<c:out value='${reservation.firstPrice}' />" />
 	 
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
 	 
 	 <%--价格明细类 add by lixiaoyong v2.6 --%>
     <c:forEach items="${order.priceList}" var="item" varStatus="i">
          <c:choose>
             <c:when test="${i.index==0}">
                <input type="hidden" name="OrPriceDetail.night" value='<fmt:formatDate pattern="yyyy-MM-dd" value="${item.night}" />' />
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
                <input type="hidden" name="OrPriceDetail.night_${i.index}" value='<fmt:formatDate pattern="yyyy-MM-dd" value="${item.night}" />' />
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
    <input type="hidden" name="detailNum" id="detailNum" value="${detailNum}"/>	
    
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
             	<input type="hidden" name="OrAssureItemEvery.night" value='<fmt:formatDate pattern="yyyy-MM-dd" value="${item.night}" />' />
             	<input type="hidden" name="OrAssureItemEvery.beforeDate" value='<fmt:formatDate pattern="yyyy-MM-dd" value="${item.beforeDate}" />' />
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
                <input type="hidden" name="OrAssureItemEvery.night_${i.index}" value='<fmt:formatDate pattern="yyyy-MM-dd" value="${item.night}" />' />
                <input type="hidden" name="OrAssureItemEvery.beforeDate_${i.index}" value='<fmt:formatDate pattern="yyyy-MM-dd" value="${item.beforeDate}" />' />
             </c:otherwise>
          </c:choose>                       
        </c:forEach>
    <input type="hidden" name="assureNum" id="assureNum" value="${assureNum}"/>
    </form>

<%--=S 修改日期 --%>
<%@ include file="dateChangeDiv.jsp" %>
<%--=E 修改日期 --%>
<%-- 历史入住人的div --%>
<div id="fellowNameDiv" class="float_storey" style="position:absolute; display:none;"><ol id="fellowNameOl"></ol></div>
<%-- 历史联系人的div --%>
<div id="linkmanNameDiv" class="float_storey" style="position:absolute; display:none;"><ol id="linkmanNameOl"></ol></div>

<%--=S footer--%>
<jsp:include page="../commonjsp/_footer.jsp" flush="true" />
<%--=E footer --%>

<script src="http://wres.mangocity.com/js/w/build/mgtool.js" type="text/javascript"></script>
<script src="http://wres.mangocity.com/js/w/module/datepicker.js" type="text/javascript"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/hotel2011.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/h_index_2012.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/jqhotel.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/jQuery_OperateSelector.js"></script>
<!-- 酒店企业QQ -->
<script src="http://wres.mangocity.com/js/w/module/mango_online.js"></script>
<script type="text/javascript">
$(function(){
    $('#mangoServerOnline').mangoOnline({
			  top : 205,
			  setclass : 'on_flight',
			  url : 'http://b.qq.com/webc.htm?new=0&sid=800016693&o=www.mangocity.com&q=7',
			  week : '周一至周日',
			  time : '08:30-20:30'
	});
});
</script>
<script type="text/javascript">
var fellowPersonArray = new Array();
        <c:if test="${not empty member.fellowList}">
		       <c:forEach items="${member.fellowList}" var="oftenMember" varStatus="i"> 
		              fellowPersonArray.push(['${oftenMember.chiName}','${oftenMember.mobileNo}',null]);
		       </c:forEach>
        </c:if>  

var linkmanArray = new Array();
        <c:if test="${not empty member.linkmanList}">
		       <c:forEach items="${member.linkmanList}" var="linkman" varStatus="i"> 
		              linkmanArray.push(['${oftenLinkman.linkPersonName}','${linkman.value.linkMobileNo}','${linkman.value.linkPersonEmail}']);
		       </c:forEach>
        </c:if>      
</script>
<script type="text/javascript" src="js/hotelbookJS/hotelbook.js"></script>
<script type="text/javascript" >
// 因为"${not empty bookhintAssureStr} 在js中无法识别，故放在界面     	
//变化金额和返现
var allpriceSumForOneRoom = jQuery("#allPriceSum").html();
var returnCashForOneRoom = jQuery("#returnCash").html();
function changeAllPriceAndReturnCash(roomNum){
    var allPriceSum = allpriceSumForOneRoom *roomNum;
	var retrunCashSum = returnCashForOneRoom *roomNum;
	jQuery("#allPriceSum").html(allPriceSum);
	jQuery("#returnCash").html(retrunCashSum);
}

jQuery("#arrivalHotelTime").change(
     function(){
       changePayNeedAssureValue();
});
     
//改变是否需要担保
function changePayNeedAssureValue(){
    var timeStr =  jQuery("#arrivalHotelTime").val();
    var timeArray = timeStr.split('-');
    var lastArriveTime = timeArray[1];
    var roomNum = jQuery("#roomNumSelect").val();
    var lastSuretyTime = "";
    var timeNeedAssure = false;
    var roomNeedAssure = false;
	 <c:if test="${not empty reservation.lateSuretyTime}">
         lastSuretyTime = '${reservation.lateSuretyTime}';
     </c:if>
     if(lastSuretyTime != ""){
	     if(lastArriveTime > lastSuretyTime){
	         timeNeedAssure = true;
		 }else{
		     timeNeedAssure = false;
		 }
	 }	 
   var days = '${hotelOrderFromBean.days}';
   <c:if test="${not empty reservation && reservation.roomsAssure==true || reservation.nightsAssure==true}">
			//超房数,超间夜 担保判断
			var overRoomNum = "<c:out value = '${reservation.rooms}'/>";
			var maxNightNum = '<c:out value="${reservation.nights}"/>';
	        var roomNight = days * 	roomNum ;
	        if(overRoomNum < roomNum && overRoomNum != 0){
	            roomNeedAssure = true;
	        }else if(maxNightNum < roomNight &&  maxNightNum != 0){
				 roomNeedAssure = true;
	        }else{
	            roomNeedAssure = false;
	        }	
   </c:if>   
   if(timeNeedAssure || roomNeedAssure ){
        jQuery("#f_payNeedAssure").val(true);
    }else{
        jQuery("#f_payNeedAssure").val(false);
    }
    //无条件担保
     var uncondition = '${reservation.unCondition}';
      if(uncondition == "true"){
          jQuery("#f_payNeedAssure").val(true);
      }
      
     //button value转换
       if(jQuery("#f_payNeedAssure").val() == "true") {
      		jQuery("#submitOrder").val("下一步");
      		jQuery("#submitOrder").attr("name","nextStep");
      }else {
      		jQuery("#submitOrder").val("提交订单");
      		jQuery("#submitOrder").attr("name","submitOrder");
      }
}
<c:if test="${not empty reservationAssist.assureInforList}">
		var total = '';
		var value = '';
	    <c:forEach items="${reservationAssist.assureInforList}" var="item">
			  value = '<c:out value="${item.assureDate}"/>'+'#'+'<c:out value="${item.assureRule}"/>'+'#'+
						'<c:out value="${item.assureType}"/>'+'#'+'<c:out value="${item.unconditionAssure}"/>'+'#'+'<c:out value="${item.unconditionAssureAmount}"/>'+'#'+
						'<c:out value="${item.overRoomsAssure}"/>'+'#'+'<c:out value="${item.overRoomsNum}"/>'+'#'+'<c:out value="${item.overRoomsAssureAmount}"/>'+'#'+
						'<c:out value="${item.overNightsAssure}"/>'+'#'+'<c:out value="${item.overNightsNum}"/>'+'#'+'<c:out value="${item.overNightsAssureAmount}"/>'+'#'+
						'<c:out value="${item.overTimeAssure}"/>'+'#'+'<c:out value="${item.overTimeStr}"/>'+'#'+'<c:out value="${item.overTimeAssureAmount}"/>'+'#'+
						'<c:out value="${item.fristDayAssureAmount}"/>';
			  value = value+"&";	
		      total += value;
		</c:forEach>
		document.getElementById("f_assureDetailStr").value = total;
	</c:if>
	
//当房间数变化时，要改变入住人填写框的个数，要考虑金额和返现的变化，预付的考虑积分和代金券	
jQuery("#roomNumSelect").change(
   function(){
    var roomNum = jQuery("#roomNumSelect").val();
    changeFellowPersonInputNum(roomNum);
	changeAllPriceAndReturnCash(roomNum);
	changePayNeedAssureValue();
	//changeShouldPaySum(roomNum);
	}
);

//加载床型列表到床型下拉框; add by shengwei.zuo 2009-10-26
	function initBedType(){
		var  bedTypeStr = '<c:out value="${hotelOrderFromBean.bedTypeStr}"/>';	
		var bedTypeArray = bedTypeStr.split(',');		
		for(i=0;i<bedTypeArray.length;i++){ 		
	           var bedType = bedTypeArray[i];
	           //1:双床
	           if (bedType == '2'){	           	                                           
                    jQuery('#bedtypeSelect').addOption("双床",bedType);                    	
               //2:大床       		                   
	           }else if (bedType == '1'){	             		                                                      
                    jQuery('#bedtypeSelect').addOption("大床",bedType);                     	
               //3:单床       		                 	             
	           }else if (bedType == '3'){	              	                                 	                  
                    jQuery('#bedtypeSelect').addOption("单床",bedType);                      	                	             
	           }	           
	    }
	 }  
	
//初始化特殊提示
function initSpecialHint(){
var sumGe = 0;//号码1，2，3
<c:if test="${not empty bookhintAssureStr}">
    sumGe++;
	var bookhintAssureStr = '${bookhintAssureStr}';
	jQuery("#specialHint > ol").append("<li>"+sumGe+"、"+bookhintAssureStr+"</li>");
</c:if>
<c:if test="${not empty bookhintCancelAndModifyStr}">
    sumGe++;
	var bookhintCancelAndModifyStr = '${bookhintCancelAndModifyStr}';
	jQuery("#specialHint > ol").append("<li>"+sumGe+"、"+bookhintCancelAndModifyStr+"</li>");
</c:if>
    sumGe++;
    jQuery("#specialHint > ol").append("<li>"+sumGe+"、到达酒店前台，请提供入住人有效证件办理入住手续。</li>");
}

//初始化条件
jQuery(document).ready(function(){
   jQuery("#roomNumSelect").val(1); //房间数设为1
   initBedType();
   initSpecialHint();
   changePayNeedAssureValue();
   jQuery("#submitOrder").attr("disabled",""); //设置按钮有效
  
 //登陆会员不为空时，则客人姓名、联系人、联系电话、邮箱都成会员资料中获取。
 <c:choose>
	 <c:when test="${not empty member}">
	  jQuery("input[name='fellowName']").val('${member.familyName}${member.name}');
	  jQuery("input[name='fellowName']").attr('style','color:#000000');
	  jQuery('#linkmanName').val('${member.familyName}${member.name}');
	  jQuery('#linkmanTelephone').val('${member.linkmobile}');
	  jQuery('#linkmanEmail').val('${member.email}');
	 </c:when>
	 <c:otherwise>
	 jQuery("input[name='fellowName']:first").bind('blur',
     function (){
		if(jQuery("#fellowNameId1").val() != null && jQuery("#fellowNameId1").val().indexOf('姓名') == -1) {
			jQuery('#linkmanName').val(jQuery("#fellowNameId1").val());
		}
	});
	 </c:otherwise>
 </c:choose>
});
</script>
<script type="text/javascript" src="http://float2006.tq.cn/floatcard?adminid=8837526&sort=7"></script>

</body>
</html>