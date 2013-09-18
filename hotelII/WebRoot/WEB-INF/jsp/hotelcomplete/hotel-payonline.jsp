<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="core"%>
<%@ taglib prefix="fmt" uri="fmt"%>
<%@ taglib prefix="fn" uri="fn"%>
<%@ taglib prefix="h" uri="convert"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>国内酒店预订服务_酒店预订_芒果网</title>
		<meta name="description"
			content="芒果网提供国内、港澳、海外酒店查询预订，在芒果网预订酒店可享受便宜的折扣价格，高品质的预订服务，价格实惠的折扣酒店，可供在线预订，旅行方便快捷" />
		<meta name="keywords"
			content="国内酒店，港澳酒店，海外酒店查询，打折酒店，折扣，酒店预订，折扣酒店，预订酒店，低价酒店，旅游，旅行，出差，价格优惠，优惠价格，特惠，优惠，便宜，特价酒店，酒店查询" />
		<link rel="stylesheet" type="text/css"
			href="http://hres.mangocity.com/css/hotel10.css" />
		<script src="http://hres.mangocity.com/js/css/hotel.js"
			language="javascript"></script>
		<script type="text/javascript"
			src="http://wres.mangocity.com/js/home/cloud/js/jquery-1.3.2.js"></script>
		<script type="text/javascript">
	jQuery.noConflict();
</script>
		<script type="text/javascript"
			src="http://hres.mangocity.com/js/mangocity.simplemodal.js"></script>
		<link type="text/css"
			href="http://hres.mangocity.com/css/thickbox.css" rel="stylesheet"
			media="screen" />
		<script type="text/javascript">

function jumpPaypage(){

	var pageURL='hotel-payonline.shtml?orderID=${order.ID}';
	
	 jQuery("#id_waitPay").modal({
			closeHTML : '<div class="guanbi"><a title="Close" class="closeMode" style="cursor:pointer;" target="_blank"></a></div>',
			position : ["25%"],
			overlayId : 'contact-overlay',
			//onClose : $.modal.close(),
			closeClass : ('closeMode')
   });
   window.open(pageURL,'_blank', 'height=800, width=1000, top=0, left=0, toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=yes, status=no');  
	
}

jQuery(function(){ 
  //弹出支付网关页面
  //jumpPaypage();
  
  jQuery('#id_paysuccess,#id_payfail').live("click",function(){
  	 	
  	  jQuery('#id_paysuccess,#id_payfail').attr("disabled","true");		
  	 
	  //ajaxURl
	  var ajUrl = "hotel-payonlineStatus.shtml";			
	  //订单ID
	  var orderID = "<c:out value='${order.ID}'/>";
  		
	  jQuery.ajax({
		url:ajUrl,
		type:'post',
		dataType:'html',
		contentType:'application/x-www-form-urlencoded',
		data:"orderID="+orderID,
		error:function(){
			
			jQuery('.closeMode').click();    
			//jQuery('#cot4').show();
			//jQuery('#id_pay').html('继续支付').show();
			
		},
		success:function(xmlData){
			
			jQuery('#onlineStateDiv').html(xmlData);
			
			var payLastFlag = jQuery('#payLastFlag').val();
			
			if(payLastFlag!=null && payLastFlag!="" && typeof(payLastFlag) != "undefined"){
							
				if(jQuery.trim(payLastFlag)=='onlinePaySuccess'){
				
				    jQuery('#paySubmitForm').submit();	
				    
				}else if(jQuery.trim(payLastFlag)=='onlinePaySuccessHKExp'){
				
				    jQuery('#paySubmitForm').submit();	
				    
				}else if(jQuery.trim(payLastFlag)=='onlinePayWaiting'){
					alert("正在支付中，请稍候。。。");
					return;
				    
				}else if(jQuery.trim(payLastFlag)=='onlinePayFail'){
					jQuery('.closeMode').click();    
					jQuery('#noFoudOrder').html("支付失败");
				    //jQuery('#cot4').show();
				    
				}else {
					jQuery('.closeMode').click();    
					jQuery('#noFoudOrder').html("支付失败");	
				}
			
			}else{
			
				 jQuery('.closeMode').click();    
				 jQuery('#noFoudOrder').html("支付失败");
				
			}
			

			
		}
	  });	
	
  });
	
	//继续支付
	jQuery('#id_contpay,#id_pay').click(function(){
	  jQuery('#id_rePayMsg').show(); 
	  jQuery('#noFoudOrder').html('支付中');	
	  jQuery('#id_pay').html('继续支付').show(); 	
	  jumpPaypage();
	});
})


</script>
	</head>
	<body>
		<div id="layout">
			<!--页头区 -->
			<c:choose>
				<c:when
					test="${hotelOrderFromBean.cityCode == 'HKG' || hotelOrderFromBean.cityCode == 'MAC'}">
					<jsp:include page="../commonjsp/_headerHK.jsp" flush="true" />
				</c:when>
				<c:otherwise>
					<jsp:include page="../commonjsp/_header.jsp" flush="true" />
				</c:otherwise>
			</c:choose>

			<!-- 主体板块区 -->
			<div id="cot1">
				<h1>
					请支付票款来完成您的订单
				</h1>
			</div>
			<div id="cot2">
				<div>
					单价：¥${hotelOrderFromBean.acturalAmount}
				</div>
				<div>
					订单号:${order.orderCD}
				</div>
				<div style="overflow: hidden; zoom: 1;">
					<div style="width: 20%; float: left;">
						支付状态：
						<span id="noFoudOrder">等待支付</span>
					</div>
					<div id="id_rePayMsg" style="float: left; display: none;">
						如果支付失败，您可以再次尝试支付，
					</div>
					<div class="pay">
						<a id="id_pay" href="javascript:void(0);" onclick="return false;"
							target="_blank">立即支付</a>
					</div>
				</div>
			</div>
			<div id="cot3">
				请尽快完成支付，如果您支付失败，请点上面的“继续支付”按钮再次支付
				<br />
				30分钟内没有支付成功，订单可能被删除，您可以重新预订或致电40066-40066寻求人工服务。
				<table width="100%">
					<tr>
						<td>
							<img src="http://himg.mangocity.com/img/dengdai.gif" width="240"
								height="20" align="absmiddle" />
							<span class="dengdai">等待您的支付。。。。。。</span>
						</td>
					</tr>
				</table>
			</div>
			<div id="cot4" style="display: none">
				<span id="payFailedTip" color='red'></span>如果继续支付请点击这里,
				<a id="id_contpay" href="javascript:void(0);"
					onclick="return false;">继续支付</a>
			</div>
			<br />
			<br />

			<!-- 支付弹出页面 >-->
			<form id="paySubmitForm" name="paySubmitForm"
				action="hotel-onlineComplete.shtml" method="post">
				<input type="hidden" name="hidOrderId" value="${order.ID}" />
				<input type="hidden" name="payflag" value="fail" />
				<!-- 担保明细的字符串用#分割的 add by shengwei.zuo 2009-12-01-->
				<input type="hidden" name="assureDetailStr" id="assureDetailStr"
					value="${assureDetailStr}" />

				<input type="hidden" name="priceNum" id="priceNum"
					value="${hotelOrderFromBean.priceNum}" />
				<input type="hidden" name="childRoomTypeId" id="childRoomTypeId"
					value="${hotelOrderFromBean.childRoomTypeId}" />
				<input type="hidden" name="childRoomTypeName" id="childRoomTypeName"
					value="${hotelOrderFromBean.childRoomTypeName}" />
				<input type="hidden" name="cityId" id="cityId"
					value="${hotelOrderFromBean.cityId}" />
				<input type="hidden" name="cityCode" id="cityCode"
					value="${hotelOrderFromBean.cityCode}" />
				<input type="hidden" name="hotelId" id="hotelId"
					value="${hotelOrderFromBean.hotelId}" />
				<input type="hidden" name="hotelName" id="hotelName"
					value="${hotelOrderFromBean.hotelName}" />
				<input type="hidden" name="minPrice" id="minPrice"
					value="${hotelOrderFromBean.minPrice}" />
				<input type="hidden" name="maxPrice" id="maxPrice"
					value="${hotelOrderFromBean.maxPrice}" />
				<input type="hidden" name="payMethod" id="payMethod"
					value="${hotelOrderFromBean.payMethod}" />
				<input type="hidden" name="roomTypeId" id="roomTypeId"
					value="${hotelOrderFromBean.roomTypeId}" />
				<input type="hidden" name="roomTypeName" id="roomTypeName"
					value="${hotelOrderFromBean.roomTypeName}" />
				<input type="hidden" name="quotaType" id="quotaType"
					value="${hotelOrderFromBean.quotaType}" />
				<input type="hidden" name="days" id="days"
					value="${hotelOrderFromBean.days}" />
				<input type="hidden" name="hotelStar" id="hotelStar"
					value="${hotelOrderFromBean.hotelStar}" />
				<input type="hidden" name="currency" id="currency"
					value="${hotelOrderFromBean.currency}" />
				<input type="hidden" name="breakfastType" id="breakfastType"
					value="${hotelOrderFromBean.breakfastType}" />
				<input type="hidden" name="breakfastNum" id="breakfastNum"
					value="${hotelOrderFromBean.breakfastNum}" />
				<input type="hidden" name="rate" id="rate" value="${rate}" />
				<input type="hidden" name="isOlympic" id="isOlympic"
					value="${isOlympic}" />
				<input type="hidden" name="roomQuantity" id="roomQuantity"
					value="${hotelOrderFromBean.roomQuantity}" />
				<input type="hidden" name="agentCode" id="agentCode"
					value="${hotelOrderFromBean.agentCode}" />
				<%-- hotel 2.9.2 记录房型的最大入住人数 add by chenjiajie 2009-08-05 --%>
				<input type="hidden" id="maxPersons" name="maxPersons"
					value="${hotelOrderFromBean.maxPersons}" />

				<input type="hidden" name="saleDepartmentPay" id="saleDepartmentPay"
					value="${saleDepartmentPay}" />

				<input type="hidden" name="needOvertimeAssure"
					id="needOvertimeAssure" value="${needOvertimeAssure}" />
				<input type="hidden" name="needOverRoomAssure"
					id="needOverRoomAssure" value="${needOverRoomAssure}" />
				<input type="hidden" name="needOverNightsAssure"
					id="needOverNightsAssure" value="${needOverNightsAssure}" />
				<input type="hidden" name="needAssure" id="needAssure"
					value="${hotelOrderFromBean.needAssure}" />
				<input type="hidden" name="linkMan" id="linkMan"
					value="${hotelOrderFromBean.linkMan}" />
				<input type="hidden" name="mobile" id="mobile"
					value="${hotelOrderFromBean.mobile}" />
				<input type="hidden" name="fixedDistrictNum" id="fixedDistrictNum"
					value="${hotelOrderFromBean.fixedDistrictNum}" />
				<input type="hidden" name="fixedExtension" id="fixedExtension"
					value="${hotelOrderFromBean.fixedExtension}" />
				<input type="hidden" name="fixedPhone" id="fixedPhone"
					value="${hotelOrderFromBean.fixedPhone}" />
				<input type="hidden" name="telephone" id="telephone"
					value="${hotelOrderFromBean.telephone}" />
				<input type="hidden" name="email" id="email"
					value="${hotelOrderFromBean.email}" />
				<input type="hidden" name="faxDistrictNum" id="faxDistrictNum"
					value="${hotelOrderFromBean.faxDistrictNum}" />
				<input type="hidden" name="faxPhone" id="faxPhone"
					value="${hotelOrderFromBean.faxPhone}" />
				<input type="hidden" name="faxExtension" id="faxExtension"
					value="${hotelOrderFromBean.faxExtension}" />
				<input type="hidden" name="customerFax" id="customerFax"
					value="${hotelOrderFromBean.customerFax}" />
				<input type="hidden" name="specialRequest" id="specialRequest"
					value="${hotelOrderFromBean.specialRequest}" />
				<input type="hidden" name="confirmtype" id="confirmtype"
					value="${hotelOrderFromBean.confirmtype}" />
				<input type="hidden" name="checkinDate"
					value='<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkinDate}" />' />
				<input type="hidden" name="checkoutDate"
					value='<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkoutDate}" />' />
				<input type="hidden" name="arrivalTime" id="arrivalTime"
					value="${hotelOrderFromBean.arrivalTime}" />
				<input type="hidden" name="latestArrivalTime" id="latestArrivalTime"
					value="${hotelOrderFromBean.latestArrivalTime}" />
				<input type="hidden" name="flagme" id="flagme" value="${flagme}" />
				<input type="hidden" name="datePriceStr" id="datePriceStr"
					value="${hotelOrderFromBean.datePriceStr}" />
				<input type="hidden" name="cancelModifyItem" id="cancelModifyItem"
					value="${cancelModifyItem}" />
				<input type="hidden" name="bookhintSpanValue" id="bookhintSpanValue"
					value="${bookhintSpanValue}" />
				<input type="hidden" name="title" id="title"
					value="${hotelOrderFromBean.title}" />


				<input type="hidden" name="hotelAddress"
					value="${hotelBasicInfo.zoneName}${hotelBasicInfo.chnAddress}" />
				<input type="hidden" name="weekOfOutDate" value="${weekOfOutDate}" />
				<input type="hidden" name="weekOfInDate" value="${weekOfInDate}" />


				<input type="hidden" name="flagCtsHK" id="flagCtsHK"
					value="${hotelOrderFromBean.flagCtsHK}" />
				<input type="hidden" name="roomChannel" id="roomChannel"
					value="${hotelOrderFromBean.roomChannel}" />
				<input type="hidden" name="minRoomNumCts" id="minRoomNumCts"
					value="${hotelOrderFromBean.minRoomNumCts}" />
				<input type="hidden" name="overHalfDate" id="overHalfDate"
					value="${overHalfDate}" />
				<input type="hidden" name="hkPrices" id="hkPrices"
					value="${hkPrices}" />
				<input type="hidden" name="hkBasePrices" id="hkBasePrices"
					value="${hkBasePrices}" />

				<%-- add by wuyun v2.6必须面转预 2009-06-04 --%>
				<input type="hidden" name="payToPrepay" id="payToPrepay"
					value="${hotelOrderFromBean.payToPrepay}" />

				<%-- 房型对应的床型ID字符串 add shengwei.zuo 2009-10-26 --%>
				<input type="hidden" id="bedTypeStr" name="bedTypeStr"
					value="${hotelOrderFromBean.bedTypeStr}" />
				<input type="hidden" id="bedType" name="bedType"
					value="${hotelOrderFromBean.bedType}" />

				<%-- add by chenkeming v2.6预订规则相关--%>
				<input type="hidden" name="clauseRule" id="clauseRule"
					value="<c:out value='${reservation.clauseRule}' />" />
				<input type="hidden" name="modifyField" id="modifyField"
					value="<c:out value='${reservation.modifyField}' />" />
				<input type="hidden" name="clauseStr" id="clauseStr"
					value="<c:out value='${reservation.clauseStr}' />" />
				<input type="hidden" name="creditRemark" id="creditRemark"
					value="<c:out value='${creditRemark}' />" />
				<input type="hidden" name="lateSuretyTime" id="lateSuretyTime"
					value="<c:out value='${reservation.lateSuretyTime}' />" />
				<input type="hidden" name="reservation.assureLetter"
					id="assureLetter"
					value="<c:out value='${reservation.assureLetter}' />" />
				<input type="hidden" name="needCredit" id="needCredit"
					value="<c:out value='${reservation.needCredit}' />" />
				<input type="hidden" name="unCondition" id="unCondition"
					value="<c:out value='${reservation.unCondition}' />" />
				<input type="hidden" name="overTimeAssure" id="overTimeAssure"
					value="<c:out value='${reservation.overTimeAssure}' />" />
				<input type="hidden" name="roomsAssure" id="roomsAssure"
					value="<c:out value='${reservation.roomsAssure}' />" />
				<input type="hidden" name="rooms" id="rooms"
					value="<c:out value='${reservation.rooms}' />" />
				<input type="hidden" name="nightsAssure" id="nightsAssure"
					value="<c:out value='${reservation.nightsAssure}' />" />
				<input type="hidden" name="nights" id="nights"
					value="<c:out value='${reservation.nights}' />" />
				<input type="hidden" name="reservSuretyPrice" id="reservSuretyPrice"
					value="<c:out value='${reservation.reservSuretyPrice}' />" />
				<input type="hidden" name="balanceMode" id="balanceMode"
					value="<c:out value='${reservation.balanceMode}' />" />
				<input type="hidden" name="advancePayTime" id="advancePayTime"
					value="<c:out value='${reservation.advancePayTime}' />" />
				<input type="hidden" name="cancelModifyStr" id="cancelModifyStr"
					value="<c:out value='${reservation.cancelModifyStr}' />" />
				<input type="hidden" name="prepayLimitType" id="prepayLimitType"
					value="<c:out value='${reservation.prepayLimitType}' />" />
				<input type="hidden" name="firstPrice" id="firstPrice"
					value="<c:out value='${reservation.firstPrice}' />" />
				<%-- hotel 2.9.3 房间数限制 add by guzhijie 2009-09-17 --%>
				<input type="hidden" name="canRoomNumberWeb" id="canRoomNumberWeb"
					value="${hotelOrderFromBean.canRoomNumberWeb}" />
				<%-- 原币种担保总金额 --%>
				<input type="hidden" name="orignalSuretyPrice"
					id="orignalSuretyPrice"
					value="<c:out value='${hotelOrderFromBean.orignalSuretyPrice}' />" />
				<%-- 人民币币种担保总金额 --%>
				<input type="hidden" name="suretyPriceRMB" id="suretyPriceRMB"
					value="${hotelOrderFromBean.suretyPriceRMB}" />
				<%-- 标志是否使用了积分 --%>
				<input id="useUlmPoint" name="useUlmPoint" type="hidden"
					value="${hotelOrderFromBean.useUlmPoint}" />
				<input id="ulmPoint" name="ulmPoint" type="hidden"
					value="${hotelOrderFromBean.ulmPoint}" />
				<%-- 标志是否使用了代金券 --%>
				<input id="usedCoupon" name="usedCoupon" type="hidden"
					value="${hotelOrderFromBean.usedCoupon}" />
				<input id="ulmCoupon" name="ulmCoupon" type="hidden"
					value="${hotelOrderFromBean.ulmCoupon}" />

				<input id="orderPayType" name="orderPayType" type="hidden"
					value="${hotelOrderFromBean.orderPayType}" />

				<input id="onlinePaytype" name="onlinePaytype" type="hidden"
					value="${hotelOrderFromBean.onlinePaytype}" />



				<%-- 订单页面 日历 的年份，和月份 --%>
				<input id="currYearMonth" name="currYearMonth" type="hidden"
					value="${hotelOrderFromBean.currYearMonth}" />
				<input id="nextYearMonth" name="nextYearMonth" type="hidden"
					value="${hotelOrderFromBean.nextYearMonth}" />
				<%-- 根据大床/双床拼装的字符串 --%>
				<input id="bedTypeNameStr" name="bedTypeNameStr" type="hidden"
					value="${hotelOrderFromBean.bedTypeNameStr}" />

				<!-- 标记会员是否登录，已登录为false;未登录为true; add shengwei.zuo 2009-11-15 -->
				<input name="direckbook" id="direckbook" type="hidden"
					value="${hotelOrderFromBean.direckbook}" />

				<%--会员CD add by shengwei.zuo --%>
				<input type="hidden" name="memberCd" id="memberCd"
					value="${memberCd}" />


				<%-- 实收金额 --%>
				<input type="hidden" name="acturalAmount" id="acturalAmount"
					value="${hotelOrderFromBean.acturalAmount}" />
				<input type="hidden" name="detailNum" id="detailNum"
					value="${detailNum}" />

				<%-- 入住人信息 --%>
				<input type="hidden" id="linkeManStr" name="linkeManStr"
					value="${linkeManStr}" />

				<%-- 代金券明细 --%>
				<c:forEach items="${couponRecords}" var="couponItem" varStatus="i">
					<c:choose>
						<c:when test="${i.index==0}">
							<input type="hidden" name="couponCode" id="couponCode"
								value="${couponItem.couponCode}" />
							<input type="hidden" name="couponName" id="couponName"
								value="${couponItem.couponName}" />
							<input type="hidden" name="couponValue" id="couponValue"
								value="${couponItem.couponValue}" />
							<input type="hidden" name="couponBeginDate" id="couponBeginDate"
								value="${couponItem.couponBeginDate}" />
							<input type="hidden" name="couponEndDate" id="couponEndDate"
								value="${couponItem.couponEndDate}" />
							<input type="hidden" name="description" id="description"
								value="${couponItem.description}" />
						</c:when>
						<c:otherwise>
							<input type="hidden" name="couponCode_${i.index}"
								id="couponCode_${i.index}" value="${couponItem.couponCode}" />
							<input type="hidden" name="couponName_${i.index}"
								id="couponName_${i.index}" value="${couponItem.couponName}" />
							<input type="hidden" name="couponValue_${i.index}"
								id="couponValue_${i.index}" value="${couponItem.couponValue}" />
							<input type="hidden" name="couponBeginDate_${i.index}"
								id="couponBeginDate_${i.index}"
								value="${couponItem.couponBeginDate}" />
							<input type="hidden" name="couponEndDate_${i.index}"
								id="couponEndDate_${i.index}"
								value="${couponItem.couponEndDate}" />
							<input type="hidden" name="description_${i.index}"
								id="description_${i.index}" value="${couponItem.description}" />
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<input type="hidden" name="couponNum" id="couponNum"
					value="${couponNum}" />

				<input type="hidden" id="orderId" name="orderId" value="${order.ID}" />
				<!-- 存放在线支付的查询状态 add by shengwei.zuo 2009-12-02-->
				<div id="onlineStateDiv">
				</div>

				<div id="id_waitPay" style="display: none">
					<div class="zftishi">
						请您在支付网站付款，支付成功请点“支付成功”按钮 支付失败请点击“支付遇到问题”重新支付
						<br />
						<br />
						<br />
						<div class="yuding">
							<a id="id_payfail" href="javascript:void(0);"
								onclick="return false;">支付遇到问题</a>
						</div>
						<div class="yuding">
							<a id="id_paysuccess" href="javascript:void(0);"
								onclick="return false;">支付成功</a>
						</div>
					</div>
				</div>
			</form>
			<%--=S footer--%>
			<jsp:include page="../commonjsp/_footer.jsp" flush="true" />
			<%--=E footer --%>
		</div>
		<script type="text/javascript">
var _bdhmProtocol = (("https:" == document.location.protocol) ? " https://" : " http://");
document.write(unescape("%3Cscript src='" + _bdhmProtocol + "hm.baidu.com/h.js%3F6dd2342971d1d6c54e1e6258cf198831' type='text/javascript'%3E%3C/script%3E"));
</script>


  <%-- 这个放到最后，有百度监控，会影响速度--%>
<jsp:include page="../commonjsp/ _footer_monitor.jsp" flush="true" />
	</body>
</html>
