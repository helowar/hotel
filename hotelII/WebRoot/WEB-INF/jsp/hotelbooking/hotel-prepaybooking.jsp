<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %> 
<%@ page import="com.mangocity.vch.app.service.VchService" %> 
<%@ taglib prefix="c" uri="core"%>
<%@ taglib prefix="fn" uri="fn"%>
<%@ taglib prefix="fmt" uri="fmt"%>
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
<script type="text/javascript" src="js/jQuery/jquery.alert.js"></script>	
<script type="text/javascript" src="http://wres.mangocity.com/js/promotion/jquery.fancybox-iframe-1.3.4.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/mangocity.simplemodal.js"></script>

<script type="text/javascript">
document.domain="mangocity.com";
</script>
</head>

<body>
<jsp:include page="../commonjsp/_header.jsp" flush="true" />
<%-- =S top bar --%>
<div class="w960 hn-topbar">
    <div class="status step3">
        <ul>
            <li>查询</li>
            <li>选择</li>
            <li  class="choice">预订</li>
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
                <span><em id="changedatebtn" class="changetime" name="changedatebtn">修改日期</em></span>

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
                        <select name="" id="arrivalHotelTime" onclick="__ozclk();return false;">
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
                    	     <input type='text' name='fellowName' id='fellowNameId1' value="客人1姓名" style="color:#999;"  onclick="__ozclk();return false;"/>
                        </span>                    
                    </li>
                    
                     <%-- 香港澳门城市标示 --%>
                     <input type="hidden" id="flagHKGORMAC" value="false" />    
                     <c:if test="${hotelOrderFromBean.currency =='HKD' or hotelOrderFromBean.currency =='MOP'}">
                    	<li style="padding-left:57px; height:27px;">
                               <script type="text/javascript">
                                       jQuery("#flagHKGORMAC").val("true");
                                </script>
                                 <span style="color:#999;" class="graytext">请填写与入住人所持证件一致的英文姓名(格式：姓/名)；到达酒店前台，请提供入住人姓名办理入住手续。</span>
                    	</li>
                    </c:if>   
                	<li>
                    	<label><strong>*</strong>联系人&nbsp;&nbsp;：</label><input name="linkmanName" id="linkmanName" type="text" onclick="__ozclk();return false;"/> 
                        <label><strong>*</strong>手机号码：</label><input name="linkmanTelephone" id="linkmanTelephone" type="text"  onclick="__ozclk();return false;"/>
                        <label><strong></strong>电子邮箱：</label><input name="linkmanEmail" id="linkmanEmail" type="text"  onclick="__ozclk();return false;"/><em></em>
                    </li>
                    
                </ul>
                <p class="total">房价总计：<strong style="font-family:Arial, Helvetica, sans-serif;">&yen;<span id="allPriceSum"></span>
                                          <script type="text/javascript">
				                                   var o =  Math.ceil(${hotelOrderFromBean.priceNum*rate});
	                                               document.getElementById("allPriceSum").innerHTML = o ;
                                          </script>
                
                (提前付款)</strong>  
                  返现金额：<strong style="font-family:Arial, Helvetica, sans-serif;">&yen;<span id="returnCash">${hotelOrderFromBean.returnAmount}</span></strong><a href="http://www.mangocity.com/help/recash.html"  name="recashExplain" target="_blank">(返现说明)</a>     
                <a href="javascript:void(0);" id="showEveryPrice_js" >查看每日价格详情</a></p>
            </div>
            <%@ include file="/WEB-INF/jsp/commonjsp/everyPriceDetail.jsp" %>
            <%--=E 表单 --%>
            
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
			    <div class="dlu" id="loadNoticDuplication">
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
                        <textarea name="specialrequireTextarea" id="specialrequireTextarea" onkeyup="isMax()" cols="" rows="" class="w597" maxlength="10"></textarea>
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
	        <div class="payment">
	              <div id="netBankPayDiv" style="display:none">
	                  <input name="paymentType" id="netBankPay" type="radio" value="4" class="iradio" checked="" /><label>网上银行支付</label>       
	             </div>
	             
	             <div id="creditCardPayDiv" style="display:none">
	              <input name="paymentType" id="creditCardPay" type="radio" value="2" class="iradio" checked="" /><label>信用卡支付</label>
	              </div>
	              <div id="pointAndCouponDiv" style="display:none">
	                 <input name="" type="checkbox" value="false" id="pointCheckbox" class="iradio" onclick="usePoint(this);" /><label>使用积分</label>
	                 <input name="" id="pointInput" type="text" class="w57" disabled= "true" />
	                 <span>（您的可用积分为<em id="useablePoint"><c:out value="${hotelOrderFromBean.ableUlmPoint}"/></em>，100积分相当于<em>&yen;1</em>） 
	                    如果您有芒果网代金券，可使用<a href="javascript:void(0)" name="prepayCouponLink" id="prepayCouponLink" class="green">代金券冲抵</a>
	                 </span>
	              </div>
	          </div>
	          
	          <p class="actualpa">实际支付金额：<strong>&yen;<span id="acturalAmount"></span>
	                                           <script type="text/javascript">
				                                   var o =  Math.ceil(${hotelOrderFromBean.priceNum*rate});
	                                               document.getElementById("acturalAmount").innerHTML = o ;
                                               </script>
	          </strong>
			  （房价：<em style="font-family:Arial, Helvetica, sans-serif;">&yen;<span id="sumAmount"></span>
			                              <script type="text/javascript">
				                                   var o =  Math.ceil(${hotelOrderFromBean.priceNum*rate});
	                                               document.getElementById("sumAmount").innerHTML = o ;
                                          </script>
			      </em> 
			  使用积分：<em style="font-family:Arial, Helvetica, sans-serif;">-&yen;<span id="pointAmount">0</span></em> 
			  代金券冲抵：<em style="font-family:Arial, Helvetica, sans-serif;">-&yen;<span id="couponAmount">0</span></em>）
	          <c:if test="${member==null}">
	                 <span class="attention">提示：登录后，可使用积分和代金券</span>
	          </c:if>
	          </p>
	          
	          <div class="netbank" id="netbankDiv" style="display:none" >
	          	<p class="notice"><a href="http://www.mangocity.com/common/bank.htm" target="_blank" class="green">可使用的银行卡种类</a>（请确保您的银行卡已开通网上支付功能）</p>
		        	<ul>
		        		  <li><input name="onlinePaytype" type="radio" value="25" class="iradio" /> <label>银联在线</label> <img src="http://himg.mangocity.com/img/yinlianzaixian.jpg" alt="银联互联网支付" /></li>
		                  <li><input name="onlinePaytype" type="radio" value="15" class="iradio"  /> <label>快钱</label> <img src="http://himg.mangocity.com/img/kuaiqian.jpg" alt="快钱" /></li>
		                  <li><input name="onlinePaytype" type="radio" value="11" class="iradio" checked="checked" /> <label>支付宝</label> <img src="http://himg.mangocity.com/img/zhifubao.jpg" alt="支付宝" /></li>
		                  <li><input name="onlinePaytype" type="radio" value="13" class="iradio" /> <label>交行在线</label> <img src="http://himg.mangocity.com/img/jiaohang.jpg" alt="交行在线" /></li>
		                  <li><input name="onlinePaytype" type="radio" value="10" class="iradio"  /> <label>招商银行</label></li>
		                  <!--  <li><input name="onlinePaytype" type="radio" value="" class="iradio" /> <label>建设银行</label></li>
		                  <li><input name="onlinePaytype" type="radio" value="" class="iradio" /> <label>工商银行</label></li> -->
		                  <li><input name="onlinePaytype" type="radio" value="16" class="iradio" /> <label>农业银行</label></li>
		                  <li><input name="onlinePaytype" type="radio" value="8" class="iradio" /> <label>IPS国内卡</label> <img src="http://himg.mangocity.com/img/ipsgn_log.jpg" alt="环迅支付" /></li>
		                  <li><input name="onlinePaytype" type="radio" value="21" class="iradio" /> <label>中信银行</label> <img src="http://himg.mangocity.com/img/zxbank.jpg" alt="中信银行" /></li>
		                  <li><input name="onlinePaytype" type="radio" value="26" class="iradio" /> <label>邮政储蓄</label> <img src="http://wimg.mangocity.com/img/home/online/psbc_logo.jpg" alt="邮政储蓄银行" /></li>
		            </ul>
	          </div>
	          <!-- 支付宝s -->
	          <div class="netbank" id="netbankDiv_zhifubao" style="display:none" >
	          	<p class="notice"><a href="http://www.mangocity.com/common/bank.htm" target="_blank" class="green">可使用的银行卡种类</a>（请确保您的银行卡已开通网上支付功能）</p>
		        	<ul>
		                  <li><input name="onlinePaytype" type="radio" value="11" class="iradio" checked="checked" /> <label>支付宝</label> <img src="http://himg.mangocity.com/img/zhifubao.jpg" alt="支付宝" /></li>
		            </ul>
	          </div>  
	          <!-- 支付宝e -->
	             
		  </div>
    </div>
    <%--=E 支付方式 --%>
    
    
    <p class="attention">注：请确保您所填的以上信息准确有效，以免耽误订单处理。为及早确认您预订的房间，请您尽快“提交”订单！</p>
    
    <div class="fillsub"> <input name="nextStep" onclick="prepaySubmitOrder();" id="submitOrder" type="submit" class="btn96x27" value="下一步" /></div>
	<input type="hidden" id="used_orderComfirmUrl" value="${orderComfirmUrl}" />
</div>
<%--=E main --%>
     <form id="submitForm" name="submitForm" action ="hotel-check.shtml" method="post" >
      <%-- 日期，时间 --%>
        <input type="hidden" name="checkinDate"  id="f_checkinDate"	value="<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkinDate}"/>">
        <input type="hidden" name="checkoutDate"id="f_checkoutDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkoutDate}"/>">
        <input type="hidden" name="days" value="${hotelOrderFromBean.days}" />
        <input type="hidden" name="arrivalTime" id="f_arrivalTime" value="">
		<input type="hidden" name="latestArrivalTime" id="f_latestArrivalTime" value="">
       <%-- 酒店 --%>
	    <input type="hidden" name="cityId"  value="${hotelOrderFromBean.cityCode}" />
	    <input type="hidden" name="cityCode" value="${hotelOrderFromBean.cityCode}" />
	    <input type="hidden" name="hotelId" id="checkDup_hotelId"value="${hotelOrderFromBean.hotelId}" />
	    <input type="hidden" name="hotelName" id="hotelName" value="${hotelOrderFromBean.hotelName}" />
	    <input type="hidden" name="hotelStar" value="${hotelOrderFromBean.hotelStar}"/>	
	    
	    <%--酒店提示信息--%>
  		<input type="hidden" name="tipInfo" value="${hotelOrderFromBean.tipInfo}"/>
	    
	    <%-- 房型 --%>
	    <input type="hidden" name="roomTypeId" id="checkDup_roomTypeId" value="${hotelOrderFromBean.roomTypeId}" />
	    <input type="hidden" name="roomTypeName" value="${hotelOrderFromBean.roomTypeName}" />
	    <input type="hidden" name="roomQuantity" id="f_roomQuantity" value="1" />
	    <input type="hidden" name="bedType" id="f_bedType" value="" />
	    <input type="hidden" name="roomChannel" value="${hotelOrderFromBean.roomChannel}" />
	    
		<%-- 会员的 --%>
	     <input type="hidden" name="linkMan" id="f_linkMan" value="" />
        <input type="hidden" name="linkeManStr" id="f_linkeManStr" value="" />
        <input type="hidden" name="mobile" id="f_mobile" value="" />	
        <input type="hidden" name="email" id="f_email" value="" />	
        <input type="hidden" name="useUlmPoint" id="f_useUlmPoint" value="false" />
        <input type="hidden" name="ulmPoint" id="f_ulmPoint" value="0" />
        <input type="hidden" name="usedCoupon" id="f_usedCoupon" value="false"/>
        <input type="hidden" name="ulmCoupon" id="f_ulmCoupon" type="hidden" value="0"/>
	    
	    <input type="hidden" name="rate" id="rate" value="${rate}" />	
	    <input type="hidden" name="payMethod" id="f_payMethod" value="${hotelOrderFromBean.payMethod}" />
	    <input type="hidden" name="priceNum"  value="${hotelOrderFromBean.priceNum}" />
        <input type="hidden" name="childRoomTypeId" value="${hotelOrderFromBean.childRoomTypeId}" />
	    <input type="hidden" name="childRoomTypeName" value="${hotelOrderFromBean.childRoomTypeName}" /> 
	    <input type="hidden" name="currency" value="${hotelOrderFromBean.currency}"/>   
	    <input type="hidden" name="breakfastType" id="breakfastType" value="${hotelOrderFromBean.breakfastType}" />
	    <input type="hidden" name="breakfastNum" id="breakfastNum" value="${hotelOrderFromBean.breakfastNum}" />
	    <input type="hidden" name="payToPrepay" id="f_payToPrepay"  value="${hotelOrderFromBean.payToPrepay}" />
	    <input type="hidden" name="returnAmount" id="f_returnAmount" value="${hotelOrderFromBean.returnAmount}" />
	    <input type="hidden" name="acturalAmount" id="f_acturalAmount" value="" /><%-- this param’s currency is RMB and only for pre_pay --%>
	    
	    <input type='hidden'  value='' name= 'specialRequest' id ="f_specialRequest" /> 
	    <%-- 预付的支付方式 --%>
	    <input type="hidden" name="orderPayType" id="f_paymentType" value="" />
	    <input type="hidden" name="onlinePaytype" id="f_onlinePaytype" value="" />
	    
	    <%-- 修改取消信息 --%>
		<input type="hidden" name="cancelModifyItem" id="f_cancelModifyItem" value="${bookhintCancelAndModifyStr}"/>
	    
	    <%-- 修改取消条款 --%>
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
    </form>

<%--=S 修改日期 --%>
<%@ include file="dateChangeDiv.jsp" %>
<%--=E 修改日期 --%>
<div id="fellowNameDiv" class="float_storey" style="display:none"><ol id="fellowNameOl"></ol></div>
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
<script type="text/javascript" src="http://wres.mangocity.com/js/w/online/online_hotel.js"></script>
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
		              linkmanArray.push(['${oftenLinkman.linkPersonName}','${linkman.linkMobileNo}','${linkman.linkPersonEmail}']);
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
    
    var oneRoomSumAmount = jQuery("#sumAmount").html(); 	
//当房间数变化时，要改变入住人填写框的个数，要考虑金额和返现的变化，预付的考虑积分和代金券	
jQuery("#roomNumSelect").change(
   function(){
    var roomNum = jQuery("#roomNumSelect").val();
    changeFellowPersonInputNum(roomNum);
	changeAllPriceAndReturnCash(roomNum);
	jQuery("#sumAmount").html(oneRoomSumAmount*roomNum);
	calculateAmount();
	}
);
	
//初始化特殊提示
function initSpecialHint(){
var sumGe = 0;//号码1，2，3
<c:if test="${not empty bookhintCancelAndModifyStr}">
    sumGe++;
	var bookhintCancelAndModifyStr = '${bookhintCancelAndModifyStr}';
	jQuery("#specialHint > ol").append("<li>"+sumGe+"、"+bookhintCancelAndModifyStr+"</li>");
</c:if>
    sumGe++;
    jQuery("#specialHint > ol").append("<li>"+sumGe+"、到达酒店前台，请提供入住人有效证件办理入住手续。</li>");
}

//初始化支付（预付）按钮是否被选上
function initPrePayTypeRadioChecked(){
 var roomChannel = '${hotelOrderFromBean.roomChannel}';
 var project_zhifubao = '${projectcode}';
 if(roomChannel == '8' ){
      if(project_zhifubao=='zhifubao'){
           jQuery("#netBankPay").attr("checked",true);
           jQuery("#netBankPayDiv").show();
           jQuery("#netbankDiv_zhifubao").show();
      }else{
           jQuery("#netBankPay").attr("checked",true);
           jQuery("#netBankPayDiv").show();
           jQuery("#netbankDiv").show();
      }
 }else{
      jQuery("#creditCardPay").attr("checked",true);
      jQuery("#creditCardPayDiv").show();
 }
    <c:if test="${not empty member}">
        jQuery("#pointAndCouponDiv").show();
    </c:if>
}

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
//初始化条件
jQuery(document).ready(function(){
   jQuery("#roomNumSelect").val(1); //房间数设为1
   initBedType();
   initSpecialHint();
   initPrePayTypeRadioChecked();
   //initArrivalHotelTime();
   calculateAmount();//防止刷新的时候总价格没有减掉积分和代金券
   
   jQuery("#submitOrder").attr("disabled",""); ////设置按钮有效
   
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
//积分

function usePoint(pointCheck){
    if(pointCheck.checked){
         jQuery("#pointInput").attr("disabled","");
	}else{
         jQuery("#pointInput").val("");
		 jQuery("#pointInput").attr("disabled","true");
	     calculateAmount();
	}	
}
function calculateAmount(){
   //积分
    var checkPoint = calculatePointAmount();
    if(checkPoint == false){
        return;
	}	
    var sumAmount = jQuery("#sumAmount").html();
	var pointAmount = jQuery("#pointAmount").html();
    var couponAmount = jQuery("#couponAmount").html();
	var acturalAmount = ((sumAmount - pointAmount - couponAmount).toFixed(2))*100/100;
	if(Number(sumAmount) < Number(pointAmount)){	
	    alert("输入积分数所能支付的金额超过了订单总额，请重新输入积分数！");
	    jQuery("#pointInput").val("");
	    calculateAmount();
	    return ;
	}
	//当代金券的金额大于积分的金额时，提示可以减少积分
	if(acturalAmount < 0){
	    if('${hotelOrderFromBean.roomChannel}' != '8'){	    
	         jQuery("#pointInput").alert("您使用的代金券总金额大于订单的总金额，如果您选择了积分消费，可以考虑减少使用的积分！");
	    }
	    acturalAmount = 0;
	}
	jQuery("#acturalAmount").html(acturalAmount);
}
	var useablePoint = jQuery("#useablePoint").html();
	
function calculatePointAmount(){
    var points = jQuery("#pointInput").val();
    var reg = /^\d+$/;
	if(!reg.test(points) && points != ""){
	   //alert("请输入的数字");
	   jQuery("#pointInput").val("");	
	   calculateAmount();
	   return false;
	}
	var lastPoint = useablePoint - points;
	if(lastPoint < 0){
	     alert("输入积分大于可用积分");
	     jQuery("#pointInput").val("");	
	     calculateAmount();
		 return false;
	}
	jQuery("#useablePoint").html(lastPoint);
	if(points == null || points ==""){
		points = 0;
	}
    var pointAmount = points/100;
    jQuery("#pointAmount").html(pointAmount);
}

  jQuery("#pointInput").keyup(
	   function(){
	      calculateAmount();
	   }
	);


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
		
		var nightCout  = jQuery("#roomNumSelect").val(); //房间数量 
		var totalamount = jQuery("#allPriceSum").html();
		orderDataMap += "\'<%=VchService.KEY_ORDER_AMOUNT%>\':\'"+parseInt(Math.ceil(totalamount))+"\',";//订单金额
		var checkInDateStr = "<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkinDate}"/>";
		var checkOutDateStr = "<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkoutDate}"/>";
		var checkInDate = getJsDate(checkInDateStr);
		var checkOutDate = getJsDate(checkOutDateStr);
		var roomNight = nightCout * daysElapsed(checkInDate,checkOutDate);//计算出来的间夜数
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
   		var formId = "#submitForm";//提交的表单的id
   		var length = Number(data.selectedVoucherCount);
   		var selectedVouchers = data.selectedVouchers;
   		jQuery("#submitForm>.couponInput").remove(); //删除上一次回调生成的隐藏域
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
		jQuery("#couponAmount").html(totalVoucherValue);
		calculateAmount();
   }
jQuery("#prepayCouponLink").click(function (){
		//genVoucherRequestURL()生成请求代金券系统的url						
		//var data = window.showModalDialog(genVoucherRequestURL(),"_VoucherWin","location=no,menubar=no,toolbar=no,titlebar=no,resizable=yes,scrollbars=yes,status=yes");
		//voucherRequestCallBack(data);
		window.open(genVoucherRequestURL(),"_VoucherWin","location=no,menubar=no,toolbar=no,titlebar=no,resizable=yes,scrollbars=yes,status=yes");
	}); 

</script>
<%-- S在线客服代码--%>
<%-- E在线客服代码--%>
</body>
</html>