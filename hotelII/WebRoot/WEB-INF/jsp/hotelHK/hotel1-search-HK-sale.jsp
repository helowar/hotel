<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="core"%>
<%@ taglib prefix="fn" uri="fn"%>
<%@ taglib prefix="f" uri="functions"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>港澳酒店-香港 澳门酒店预订-港澳宾馆查询预订-芒果网</title>
<meta name='keywords' content='酒店预订,香港酒店,港澳酒店,澳门酒店,预订,查询' />
<meta name='description' content='香港酒店，芒果网提供香港 澳门酒店详细介绍, 香港酒店最新价格及优惠, 香港 澳门酒店电子地图, 香港酒店用户评论.商旅出差,旅游度假,免费预订查询香港澳门酒店宾馆。 ' />		
<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/h/hk_hotel_agoda/hotel_ga.css"/>
<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/h/hk_hotel_agoda/hotel1.css"/>
<link rel="stylesheet" type="text/css" href="http://www.mangocity.com/cloud/css/calendar.css"/>
<link href="http://hres.mangocity.com/css/h/hk_hotel/hk_hotel2012.css" type="text/css" rel="stylesheet">
<link href="http://hres.mangocity.com/css/h/hk_hotel/hk_hotel.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/w/module/mango_online.css" />
<script type="text/javascript" src="http://www.mangocity.com/cloud/js/jquery-1.3.2.js"></script>
<script type="text/javascript" src="http://www.mangocity.com/cloud/js/calendar.common.js"></script>
<script type="text/javascript" src="http://www.mangocity.com/cloud/js/calendar.js"></script>
<script type="text/javascript" src="http://www.mangocity.com/cloud/js/calendar.config.js"></script>
<script src="http://ajaxsearch.partners.agoda.com/partners/SearchBox/Scripts/Agoda.SearchBoxV2.js" type="text/javascript"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/agodaSeachBox.js"></script>

<script type="text/javascript" src="js/hotelhk/agodaSeachBox.js"></script>

<script type="text/javascript" >

//link to GTA
function linkToGTA(){
	window.open("http://gbs.gta-travel.com/fe/Enter.jsp?siteid=GTC");
}

//link to agoda with the header and the footer of mangocity by cityId add by diandian.hou
function linkWithHeaderByCityId(cityId,type){
    var checkInDate = new Date(+new Date()+24*3600000);//明天
    var checkoutDate = new Date(+new Date()+24*3600000*2);//后天
    var ckInDay = checkInDate.getDate();
    var ckInMonth = checkInDate.getMonth()+1;//getMonth的值为0-11,故要加1
    var ckInYear = checkInDate.getFullYear();
    var ckOutDay = checkoutDate.getDate();
    var ckOutMonth = checkoutDate.getMonth()+1;
    var ckOutYear = checkoutDate.getFullYear();
    var reallyUrl = "http://ajaxsearch.partners.agoda.com.cn/partners/partnersearch.aspx?"
              +"CkInDay="+ckInDay+"&CkInMonth="+ckInMonth+"&CkInYear="+ckInYear
              +"&CkOutDay="+ckOutDay+"&CkOutMonth="+ckOutMonth+"&CkOutYear="+ckOutYear+"&CityCode="+cityId
              +"&cid=1442917&backlink=http://hotel.mangocity.com/hk.shtml&"
              +"header=http://hotel.mangocity.com/headmango/header.jsp?from=1&"
              +"footer=http://hotel.mangocity.com/headmango/footer_news.jsp";
     if(type==null){
          window.location.href=reallyUrl;
      }else if(type="_blank"){
          window.open(reallyUrl);      
      }else  {window.location.href=reallyUrl;}  
}
</script>
</head>
<body>

<div class="top mgb10"><jsp:include page="../commonjsp/_headerHK.jsp" flush="true" /></div>
<!--zhuti-->
<div class="cont mgb10">
  <div class="cont01">
    <div class="cont01_left">
			<jsp:include page="../commonjsp/_hk_left.jsp" flush="true" />
			<%@include file="/WEB-INF/jsp/commonjsp/staticjsp/daodao.jsp" %>
    </div>
    <div class="cont01_right">
      <div class="cont01_right01 mgb10">
        <div class="cont01_right01_left">
          <div class="cont01_right01_nav">
            <ul>
             <li class="bg_button1" style="display:none" id="button1"><span onclick="queryByTag('HKG')">香港</span></li>
              <li class="bg_button2" id="button2" style="display:none"><span onclick="queryByTag('MAC')">澳门</span></li>
              <li class="bg_button2" id="button3"><span onclick="linkToGTA()">海外酒店</span></li>
              <!-- 默认跳到曼谷 -->
              <li class="bg_button2" id="button3"><span onclick="linkWithHeaderByCityId(9395,'_blank')">东南亚热卖酒店</span></li>
            </ul>
          </div>          
        </div>
        <div class="cont01_right01_right">
          <div id="right0102">
            <ul>
              <li class="cxz">查询</li>
              <li>选择</li>
              <li>预订</li>
              <li>完成</li>
            </ul>
          </div>
        </div>
      </div>

	<form id="queryAllHkHotelForm" name="queryAllHkHotelForm" action="hk.shtml" method ="post">
      <div id="mango_Hotel_Context">
	      <div class="bianhuan" id="content1" style="display:block" >
	        <div class="cont01_right02 mgb10"><img src="http://himg.mangocity.com/img/h/hk_hotel/map1.jpg" width="746" height="342" border="0" usemap="#Map" /></div>
	        <div class="cont01_right03">* 因香港酒店价格及房态变化频繁，以下价格不能保证完全正确，仅供参考。最终价格以确认为准。<br />
	         * 香港酒店预订时，部分酒店需要支付全款或提供信用卡担保，请预订时务必了解阅读取消或变更条款，以避免损失。</div>
	        <div class="cont01_right04">
	          <table width="100%" border="0" cellspacing="0" cellpadding="0">
	            <tr>
	              <td width="12%">显示价格日期： </td>
	               <td width="12%"><input name="ableSaleDate" type="text"  class="bg_input" readonly="readonly" value="${ableSaleDate }" id="id_startDate" style="cursor: pointer;"/></td>
	              <td><div class="bg_buttom"><input class="bg_buttom" style="cursor: pointer;" name="doQuery" onclick="queryByTag('${cityCode }')" value="再查询" /> </div></td>
	            </tr>
	          </table>
	        </div>
	        <input name="cityCode" id="cityCode" type="hidden" value="${cityCode}"/>
	        
	        <div class="cont01_right05 mgb10">
	      		<div class="cont01_right05_left">
					<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#c0c0c0">
						  <tr>
				                <td width="55%" align="center" bgcolor="#008000" class="baise nav font14">酒店</td>
				                <td width="21%" align="center" bgcolor="#008000" class="baise nav font14">星级</td>
				                <td width="24%" align="center" bgcolor="#008000" class="baise nav font14">最低价格</td>
						  </tr>
			        <c:forEach items="${hotelBookingResultInfoForHkSaleList}" var="result" varStatus="resultIndex" begin='0' end='7'>
			        <tr>
						    <td colspan="3" align="center" bgcolor="#ffffcc" class="bold font14 nav1"><a href="hotel-query.shtml?cityCode=${fn:toLowerCase(cityCode)}&bizCode=${fn:toLowerCase(result.businessArea)}&label=broad"  name="hk${resultIndex.count }" id="hk${resultIndex.count }">(${resultIndex.count })${result.businessAreaName }</a></td>
						  </tr>
						  <!-- 
						  <tr>
						  	<td colspan="3" bgcolor="#FFFFFF" class="orange"><img src="http://himg.mangocity.com/img/h/hk_hotel/table_icon.jpg" />&nbsp;<b>芒果网酒店</b></td>
						  </tr>
						   -->
						<c:if test="${result.districtHotelInfo == null && empty qHotelBookingResultInfoForHkSaleMap[result.businessArea]}">
						<tr>
							<td colspan="3" bgcolor="#FFFFFF">&nbsp;</td>
						</tr>
						</c:if>
						<c:forEach items="${result.districtHotelInfo}" var="districtHotelInfo">
						<tr>
						    <td align="left" bgcolor="#FFFFFF" <c:if test="${districtHotelInfo.bookingFlag == 'false'}">class="huise"</c:if>>
						      <div class="w190">
						        <a href="<c:url value="hotel-information.shtml?hotelId=${districtHotelInfo.hotelId }&inDate=${ableSaleDate}&outDate=${ableSaleEndDate}&label=broad"/>" target="_blank">${districtHotelInfo.hotelChnName }<c:if test="${districtHotelInfo.tuijian >0 && districtHotelInfo.tuijian < 4}"><img src="http://himg.mangocity.com/img/h/hk_hotel/tetui.png" width="30" height="13" /></c:if></a></div>
						      </div>
						    </td>
						    <td align="center" valign="middle" bgcolor="#FFFFFF" class="red">${districtHotelInfo.hotelStarChnName }</td>
	                		<td align="center" bgcolor="#FFFFFF" <c:if test="${districtHotelInfo.bookingFlag == 'false'}">class="huise"</c:if>>
		                		<c:if test="${districtHotelInfo.minSalePrice >999998.0}">${districtHotelInfo.currency }***</c:if>
		                		<c:if test="${districtHotelInfo.minSalePrice < 1 }">${districtHotelInfo.currency }--</c:if>
		                		<c:if test="${ districtHotelInfo.minSalePrice > 1 && districtHotelInfo.minSalePrice <999998.0}">${districtHotelInfo.currency }${districtHotelInfo.minSalePrice }</c:if>
	                		</td>
						</tr>
						 </c:forEach>

						<!-- 读取青芒果酒店信息start -->
						 <c:if test="${not empty qHotelBookingResultInfoForHkSaleMap[result.businessArea]}">
						 <!--  
						 <tr>
						  	<td colspan="3" bgcolor="#FFFFFF" class="orange"><img src="http://himg.mangocity.com/img/h/hk_hotel/table_icon.jpg" />&nbsp;<b>青芒果酒店</b></td>
						 </tr>
						 -->
						<c:forEach items="${qHotelBookingResultInfoForHkSaleMap[result.businessArea]}" var="qHotelInfo">
						<tr>
						    <td align="left" bgcolor="#FFFFFF" <c:if test="${qHotelInfo.bookingFlag == 'false'}">class="huise"</c:if>>
						      <div class="w190">
						        <a href='hk!skipQmango.shtml?chnName=${f:encode(qHotelInfo.chnName,"utf-8")}&hotelURL=${qHotelInfo.hotelURL}' target="_blank">${qHotelInfo.chnName}<c:if test="${qHotelInfo.recommend == 'true'}"><img src="http://himg.mangocity.com/img/h/hk_hotel/tetui.png" width="30" height="13" /></c:if></a>
						      </div>
						    </td>
						    <td align="center" valign="middle" bgcolor="#FFFFFF" class="red">经济型</td>
	                		<td align="center" bgcolor="#FFFFFF" <c:if test="${qHotelInfo.bookingFlag == 'false'}">class="huise"</c:if>>
		                		<c:if test="${qHotelInfo.minSalePrice >999998.0}">${qHotelInfo.currency }***</c:if>
		                		<c:if test="${qHotelInfo.minSalePrice < 1 }">${qHotelInfo.currency }--</c:if>
		                		<c:if test="${qHotelInfo.minSalePrice > 1 && qHotelInfo.minSalePrice <999998.0}">${qHotelInfo.currency }${qHotelInfo.minSalePrice }</c:if>
	                		</td>
						 </tr>
						 </c:forEach>
						 </c:if>
						 <!-- 读取青芒果酒店信息end -->
						
					</c:forEach>
					</table>	  
					</div>          
	      			<div class="cont01_right05_right">
	          			<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#c0c0c0">
			              <tr>
				               <td width="55%" align="center" bgcolor="#008000" class="baise nav2 font14">酒店</td>
				               <td width="21%" align="center" bgcolor="#008000" class="baise nav2 font14">星级</td>
				               <td width="24%" align="center" bgcolor="#008000" class="baise nav2 font14">最低价格</td>
	           				  </tr>
	           			<c:forEach items="${hotelBookingResultInfoForHkSaleList}" var="result" varStatus="resultIndex" begin='8' end='20'>
			             				  <tr>
			               					<td colspan="3" align="center" bgcolor="#ffffcc" class="bold font14 nav3"><a href="hotel-query.shtml?cityCode=${fn:toLowerCase(cityCode)}&bizCode=${fn:toLowerCase(result.businessArea)}&label=broad"  name="hk${resultIndex.count+8 }" id="hk${resultIndex.count+8 }">(${resultIndex.count+8 })${result.businessAreaName }</a></td>
			             				  </tr>
			             				  <!--  
			             				  <tr>
						  								<td colspan="3" bgcolor="#FFFFFF" class="orange"><img src="http://himg.mangocity.com/img/h/hk_hotel/table_icon.jpg" />&nbsp;<b>芒果网酒店</b></td>
						  					</tr>
						  					-->
			             				  <c:if test="${result.districtHotelInfo == null && empty qHotelBookingResultInfoForHkSaleMap[result.businessArea]}"><tr><td colspan="3" bgcolor="#FFFFFF">&nbsp;</td></tr></c:if>
			             				  <c:forEach items="${result.districtHotelInfo}" var="districtHotelInfo">
								              <tr>
								                <td align="left" bgcolor="#FFFFFF" <c:if test="${districtHotelInfo.bookingFlag == 'false'}">class="huise"</c:if>>
								                <div  class="w190">
								                		<a href="<c:url value="hotel-information.shtml?hotelId=${districtHotelInfo.hotelId }&inDate=${ableSaleDate}&outDate=${ableSaleEndDate}&label=broad"/>" target="_blank">${districtHotelInfo.hotelChnName }<c:if test="${districtHotelInfo.tuijian >0 && districtHotelInfo.tuijian < 4}"><img src="http://himg.mangocity.com/img/h/hk_hotel/tetui.png" width="30" height="13" /></c:if></a></div></td>
								                <td align="center" valign="middle" bgcolor="#FFFFFF" class="red">${districtHotelInfo.hotelStarChnName }</td>
								                <td align="center" bgcolor="#FFFFFF" <c:if test="${districtHotelInfo.bookingFlag == 'false'}">class="huise"</c:if>>
								                <c:if test="${districtHotelInfo.minSalePrice >999998.0}">${districtHotelInfo.currency }***</c:if>
								                <c:if test="${districtHotelInfo.minSalePrice <1}">${districtHotelInfo.currency }--</c:if>
								                <c:if test="${districtHotelInfo.minSalePrice >1 && districtHotelInfo.minSalePrice <999998.0}">${districtHotelInfo.currency }${districtHotelInfo.minSalePrice }</c:if>
								                </td>
								              </tr>
							              </c:forEach>
    
										<!-- 读取青芒果酒店信息start -->
										 <c:if test="${not empty qHotelBookingResultInfoForHkSaleMap[result.businessArea]}">
										 <!--  
										 <tr>
										  	<td colspan="3" bgcolor="#FFFFFF" class="orange"><img src="http://himg.mangocity.com/img/h/hk_hotel/table_icon.jpg" />&nbsp;<b>青芒果酒店</b></td>
										 </tr>
										 -->
										<c:forEach items="${qHotelBookingResultInfoForHkSaleMap[result.businessArea]}" var="qHotelInfo">
										<tr>
										    <td align="left" bgcolor="#FFFFFF" <c:if test="${qHotelInfo.bookingFlag == 'false'}">class="huise"</c:if>>
										      <div class="w190">
										        <a href='hk!skipQmango.shtml?chnName=${f:encode(qHotelInfo.chnName,"utf-8")}&hotelURL=${qHotelInfo.hotelURL}' target="_blank">${qHotelInfo.chnName}<c:if test="${qHotelInfo.recommend == 'true'}"><img src="http://himg.mangocity.com/img/h/hk_hotel/tetui.png" width="30" height="13" /></c:if></a>
										      </div>
										    </td>
										    <td align="center" valign="middle" bgcolor="#FFFFFF" class="red">经济型</td>
					                		<td align="center" bgcolor="#FFFFFF" <c:if test="${qHotelInfo.bookingFlag == 'false'}">class="huise"</c:if>>
						                		<c:if test="${qHotelInfo.minSalePrice >999998.0}">${qHotelInfo.currency }***</c:if>
						                		<c:if test="${qHotelInfo.minSalePrice < 1 }">${qHotelInfo.currency }--</c:if>
						                		<c:if test="${qHotelInfo.minSalePrice > 1 && qHotelInfo.minSalePrice <999998.0}">${qHotelInfo.currency }${qHotelInfo.minSalePrice }</c:if>
					                		</td>
										 </tr>
										 </c:forEach>
										 </c:if>
										<!-- 读取青芒果酒店信息end -->
			        </c:forEach>
			        </table>
				 </div>
	        </div>
	      </div>
	      <div class="bianhuan" id="content2" style="display:none" >
	        <div class="cont01_right02 mgb10"><img src="http://himg.mangocity.com/img/h/hk_hotel/map.jpg" border="0" usemap="#Map2" /></div>
	        <div class="cont01_right03">* 因澳门酒店价格及房态变化频繁，以下价格不能保证完全正确，仅供参考。最终价格以确认为准。<br />
	           * 澳门酒店预订时，部分酒店需要支付全款或提供信用卡担保，请预订时务必了解阅读取消或变更条款，以避免损失。 </div>
	        <div class="cont01_right04">
	          <table width="100%" border="0" cellspacing="0" cellpadding="0">
	            <tr>
	              <td width="12%">显示价格日期： </td>
	              <td width="12%"><input name="ableSaleDateAoment" type="text"  class="bg_input" value="${ableSaleDate }" id="id_startDate1" readonly="readonly"  style="cursor: pointer;"/></td>
	              <td width="73%"><div class="bg_buttom"><input class="bg_buttom" style="cursor: pointer;" name="doQuery" onclick="queryByTag('${cityCode }')" value="再查询" /></div></td>
	            </tr>
	          </table>
	        </div>
	        <div class="cont01_right05 mgb10">
	          <div class="cont01_right05_left">
	            <table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#c0c0c0">
	              <tr>
	                <td width="55%" align="center" bgcolor="#008000" class="baise nav font14">酒店</td>
	                <td width="21%" align="center" bgcolor="#008000" class="baise nav font14">星级</td>
	                <td width="24%" align="center" bgcolor="#008000" class="baise nav font14">最低价格</td>
	              </tr>
	               <c:forEach items="${hotelBookingResultInfoForHkSaleList}" var="result" varStatus="resultIndex" begin='0' end='0'>
	               	
			        	<tr>
						      <td colspan="3" align="center" bgcolor="#ffffcc" class="bold font14 nav1"><a href="hotel-query.shtml?cityCode=${fn:toLowerCase(cityCode)}&bizCode=${fn:toLowerCase(result.businessArea)}&label=broad"  name="am${resultIndex.count }" id="am${resultIndex.count }">(${resultIndex.count })${result.businessAreaName }</a></td>
						 </tr>
						 <!--  
						 <tr>
									<td colspan="3" bgcolor="#FFFFFF" class="orange"><img src="http://himg.mangocity.com/img/h/hk_hotel/table_icon.jpg" />&nbsp;<b>芒果网酒店</b></td>
						 </tr>
						 -->
						 <c:if test="${result.districtHotelInfo == null && empty qHotelBookingResultInfoForHkSaleMap[result.businessArea]}"><tr><td colspan="3" bgcolor="#FFFFFF">&nbsp;</td></tr></c:if>
						<c:forEach items="${result.districtHotelInfo}" var="districtHotelInfo" begin='0' end='20'>
						     <tr>
						           <td align="left" bgcolor="#FFFFFF" <c:if test="${districtHotelInfo.bookingFlag == 'false'}">class="huise"</c:if>>
						                	<div class="w190">
						                	<a href="hotel-information.shtml?hotelId=${districtHotelInfo.hotelId }&inDate=${ableSaleDate}&outDate=${ableSaleEndDate}&label=broad" target="_blank">${districtHotelInfo.hotelChnName }<c:if test="${districtHotelInfo.tuijian >0 && districtHotelInfo.tuijian < 4}"><img src="http://himg.mangocity.com/img/h/hk_hotel/tetui.png" width="30" height="13" /></c:if></a></div>
						            </div></td>
						           <td align="center" valign="middle" bgcolor="#FFFFFF" class="red">${districtHotelInfo.hotelStarChnName }</td>
						                		<td align="center" bgcolor="#FFFFFF" <c:if test="${districtHotelInfo.bookingFlag == 'false'}">class="huise"</c:if>>
						                		<c:if test="${districtHotelInfo.minSalePrice >999998.0}">${districtHotelInfo.currency }***</c:if>
						                		<c:if test="${districtHotelInfo.minSalePrice < 1 }">${districtHotelInfo.currency }--</c:if>
						                		<c:if test="${ districtHotelInfo.minSalePrice > 1 && districtHotelInfo.minSalePrice <999998.0}">${districtHotelInfo.currency }${districtHotelInfo.minSalePrice }</c:if>
						                		</td>
						      </tr>
						 </c:forEach>	
						 <c:if test="${fn:length(result.districtHotelInfo)<= 20}" >
						 			<!-- 读取青芒果酒店信息start	
										 <c:if test="${not empty qHotelBookingResultInfoForHkSaleMap[result.businessArea]}">
										<!--
										 <tr>
										  	<td colspan="3" bgcolor="#FFFFFF" class="orange"><img src="http://himg.mangocity.com/img/h/hk_hotel/table_icon.jpg" />&nbsp;<b>青芒果酒店</b></td>
										 </tr>
										  -->
										<c:forEach items="${qHotelBookingResultInfoForHkSaleMap[result.businessArea]}" var="qHotelInfo">
										<tr>
										    <td align="left" bgcolor="#FFFFFF" <c:if test="${qHotelInfo.bookingFlag == 'false'}">class="huise"</c:if>>
										      <div class="w190">
										        <a href='hk!skipQmango.shtml?chnName=${f:encode(qHotelInfo.chnName,"utf-8")}&hotelURL=${qHotelInfo.hotelURL}' target="_blank">${qHotelInfo.chnName}<c:if test="${qHotelInfo.recommend == 'true'}"><img src="http://himg.mangocity.com/img/h/hk_hotel/tetui.png" width="30" height="13" /></c:if></a>
										      </div>
										    </td>
										    <td align="center" valign="middle" bgcolor="#FFFFFF" class="red">经济型</td>
					                		<td align="center" bgcolor="#FFFFFF" <c:if test="${qHotelInfo.bookingFlag == 'false'}">class="huise"</c:if>>
						                		<c:if test="${qHotelInfo.minSalePrice >999998.0}">${qHotelInfo.currency }***</c:if>
						                		<c:if test="${qHotelInfo.minSalePrice < 1 }">${qHotelInfo.currency }--</c:if>
						                		<c:if test="${qHotelInfo.minSalePrice > 1 && qHotelInfo.minSalePrice <999998.0}">${qHotelInfo.currency }${qHotelInfo.minSalePrice }</c:if>
					                		</td>
										 </tr>
										 </c:forEach>
										 </c:if>
										<!-- 读取青芒果酒店信息end -->	
									</c:if>				
					</c:forEach>
	            </table>
	          </div>
	          <div class="cont01_right05_right">
	            <table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#c0c0c0">
	              <tr>
	                <td width="55%" align="center" bgcolor="#008000" class="baise nav2 font14">酒店</td>
	                <td width="21%" align="center" bgcolor="#008000" class="baise nav2 font14">星级</td>
	                <td width="24%" align="center" bgcolor="#008000" class="baise nav2 font14">最低价格</td>
	              </tr>
	              <c:forEach items="${hotelBookingResultInfoForHkSaleList}" var="result" varStatus="resultIndex" begin='0' end='4'>
			             				  <c:if test="${resultIndex.count == 1}">
			             				  	<c:if test="${fn:length(result.districtHotelInfo) >20}" >
			             				  		<tr>
			               							<td colspan="3" align="center" bgcolor="#ffffcc" class="bold font14 nav3"><a href="hotel-query.shtml?cityCode=${fn:toLowerCase(cityCode)}&bizCode=${fn:toLowerCase(result.businessArea)}&label=broad"  name="am${resultIndex.count }" id="am${resultIndex.count }">(${resultIndex.count })${result.businessAreaName }</a></td>
			             				  		</tr>
			             				  		<!-- 
			             				  	<tr>
																<td colspan="3" bgcolor="#FFFFFF" class="orange"><img src="http://himg.mangocity.com/img/h/hk_hotel/table_icon.jpg" />&nbsp;<b>芒果网酒店</b></td>
						 									</tr>
						 					 -->
			             				  	</c:if>
		             				  	  <c:set var='beginVar' value="21" />
		             				  	  <c:set var='endVar' value="40" />
										 </c:if>
										 
										 <c:if test="${resultIndex.count > 1}">
										 			<tr>
			               				<td colspan="3" align="center" bgcolor="#ffffcc" class="bold font14 nav3"><a href="hotel-query.shtml?cityCode=${fn:toLowerCase(cityCode)}&bizCode=${fn:toLowerCase(result.businessArea)}&label=broad"  name="am${resultIndex.count }" id="am${resultIndex.count }">(${resultIndex.count })${result.businessAreaName }</a></td>
			             				</tr>
			             				<!--  
	             				  	<tr>
														<td colspan="3" bgcolor="#FFFFFF" class="orange"><img src="http://himg.mangocity.com/img/h/hk_hotel/table_icon.jpg" />&nbsp;<b>芒果网酒店</b></td>
			 										</tr>
			 							-->
			             				<c:if test="${result.districtHotelInfo == null && empty qHotelBookingResultInfoForHkSaleMap[result.businessArea]}"><tr><td colspan="3" bgcolor="#FFFFFF">&nbsp;</td></tr></c:if>
		             				  <c:set var='beginVar' value="0" />
	             				  	<c:set var='endVar' value="40" />
										 </c:if>
									<tr>
										
						 			</tr>
			             	
			             				  <c:forEach items="${result.districtHotelInfo}" var="districtHotelInfo" begin='${beginVar}' end='${endVar}'>
								              <tr>
								                <td align="left" bgcolor="#FFFFFF" <c:if test="${districtHotelInfo.bookingFlag == 'false'}">class="huise"</c:if>>
								                <div  class="w190">
								                		<a href="<c:url value="hotel-information.shtml?hotelId=${districtHotelInfo.hotelId }&inDate=${ableSaleDate}&outDate=${ableSaleEndDate}"/>&label=broad" target="_blank">${districtHotelInfo.hotelChnName }<c:if test="${districtHotelInfo.tuijian >0 && districtHotelInfo.tuijian < 4}"><img src="http://himg.mangocity.com/img/h/hk_hotel/tetui.png" width="30" height="13" /></c:if></a></div></td>
								                <td align="center" valign="middle" bgcolor="#FFFFFF" class="red">${districtHotelInfo.hotelStarChnName }</td>
								                <td align="center" bgcolor="#FFFFFF" <c:if test="${districtHotelInfo.bookingFlag == 'false'}">class="huise"</c:if>>
								                <c:if test="${districtHotelInfo.minSalePrice >999998.0}">${districtHotelInfo.currency }***</c:if>
								                <c:if test="${districtHotelInfo.minSalePrice <1}">${districtHotelInfo.currency }--</c:if>
								                <c:if test="${districtHotelInfo.minSalePrice >1 && districtHotelInfo.minSalePrice <999998.0}">${districtHotelInfo.currency }${districtHotelInfo.minSalePrice }</c:if>
								                </td>
								              </tr>
							              </c:forEach>
							  <c:if test="${resultIndex.count == 1}">            
									<c:if test="${fn:length(result.districtHotelInfo) > 20}" >
									 			<!-- 读取青芒果酒店信息start-->	
													 <c:if test="${not empty qHotelBookingResultInfoForHkSaleMap[result.businessArea]}">
													<!-- 
													 <tr>
													  	<td colspan="3" bgcolor="#FFFFFF" class="orange"><img src="http://himg.mangocity.com/img/h/hk_hotel/table_icon.jpg" />&nbsp;<b>青芒果酒店</b></td>
													 </tr>
													 -->	
													<c:forEach items="${qHotelBookingResultInfoForHkSaleMap[result.businessArea]}" var="qHotelInfo">
													<tr>
													    <td align="left" bgcolor="#FFFFFF" <c:if test="${qHotelInfo.bookingFlag == 'false'}">class="huise"</c:if>>
													      <div class="w190">
													        <a href='hk!skipQmango.shtml?chnName=${f:encode(qHotelInfo.chnName,"utf-8")}&hotelURL=${qHotelInfo.hotelURL}' target="_blank">${qHotelInfo.chnName}<c:if test="${qHotelInfo.recommend == 'true'}"><img src="http://himg.mangocity.com/img/h/hk_hotel/tetui.png" width="30" height="13" /></c:if></a>
													      </div>
													    </td>
													    <td align="center" valign="middle" bgcolor="#FFFFFF" class="red">经济型</td>
								                		<td align="center" bgcolor="#FFFFFF" <c:if test="${qHotelInfo.bookingFlag == 'false'}">class="huise"</c:if>>
									                		<c:if test="${qHotelInfo.minSalePrice >999998.0}">${qHotelInfo.currency }***</c:if>
									                		<c:if test="${qHotelInfo.minSalePrice < 1 }">${qHotelInfo.currency }--</c:if>
									                		<c:if test="${qHotelInfo.minSalePrice > 1 && qHotelInfo.minSalePrice <999998.0}">${qHotelInfo.currency }${qHotelInfo.minSalePrice }</c:if>
								                		</td>
													 </tr>
													 </c:forEach>
													 </c:if>
												<!-- 	读取青芒果酒店信息end -->	
									</c:if>
								</c:if>
								<c:if test="${resultIndex.count > 1}"> 
										<!-- 读取青芒果酒店信息start -->	
													 <c:if test="${not empty qHotelBookingResultInfoForHkSaleMap[result.businessArea]}">
													 <!--
													 <tr>
													  	<td colspan="3" bgcolor="#FFFFFF" class="orange"><img src="http://himg.mangocity.com/img/h/hk_hotel/table_icon.jpg" />&nbsp;<b>青芒果酒店</b></td>
													 </tr>
													 -->
													<c:forEach items="${qHotelBookingResultInfoForHkSaleMap[result.businessArea]}" var="qHotelInfo">
													<tr>
													    <td align="left" bgcolor="#FFFFFF" <c:if test="${qHotelInfo.bookingFlag == 'false'}">class="huise"</c:if>>
													      <div class="w190">
													        <a href='hk!skipQmango.shtml?chnName=${f:encode(qHotelInfo.chnName,"utf-8")}&hotelURL=${qHotelInfo.hotelURL}' target="_blank">${qHotelInfo.chnName}<c:if test="${qHotelInfo.recommend == 'true'}"><img src="http://himg.mangocity.com/img/h/hk_hotel/tetui.png" width="30" height="13" /></c:if></a>
													      </div>
													    </td>
													    <td align="center" valign="middle" bgcolor="#FFFFFF" class="red">经济型</td>
								                		<td align="center" bgcolor="#FFFFFF" <c:if test="${qHotelInfo.bookingFlag == 'false'}">class="huise"</c:if>>
									                		<c:if test="${qHotelInfo.minSalePrice >999998.0}">${qHotelInfo.currency }***</c:if>
									                		<c:if test="${qHotelInfo.minSalePrice < 1 }">${qHotelInfo.currency }--</c:if>
									                		<c:if test="${qHotelInfo.minSalePrice > 1 && qHotelInfo.minSalePrice <999998.0}">${qHotelInfo.currency }${qHotelInfo.minSalePrice }</c:if>
								                		</td>
													 </tr>
													 </c:forEach>
													 </c:if>
										<!-- 读取青芒果酒店信息end -->	 
							  </c:if>      
			        </c:forEach>
	            </table>
	          </div>
	        </div>
	      </div>
      </div>
    </form>  
    </div>
  </div>
</div>
<!--zhuti END-->



<!--botom-->
<jsp:include page="../commonjsp/_footer.jsp" flush="true" />
<!--botom END-->

<map name="Map" id="Map">
  <area shape="circle" coords="441,217,8" href="#hk11" />
  <area shape="circle" coords="404,231,8" href="#hk9" />
  <area shape="circle" coords="381,233,8" href="#hk10" />
  <area shape="circle" coords="359,287,8" href="#hk13" />
  <area shape="circle" coords="186,332,8" href="#hk18" />
  <area shape="circle" coords="410,194,8" href="#hk2" />
  <area shape="circle" coords="395,204,8" href="#hk1" />
  <area shape="circle" coords="391,189,8" href="#hk4" />
  <area shape="circle" coords="472,181,8" href="#hk7" />
  <area shape="circle" coords="387,171,8" href="#hk3" />
  <area shape="circle" coords="374,176,8" href="#hk5" />
  <area shape="circle" coords="335,226,8" href="#hk12" />
  <area shape="circle" coords="137,249,8" href="#hk17" />
  <area shape="circle" coords="119,76,8" href="#hk19" /><area shape="circle" coords="53,175,8" href="#hk14" />
  <area shape="circle" coords="211,181,8" href="#hk17" />
  <area shape="circle" coords="293,112,8" href="#hk16" />
  <area shape="circle" coords="361,143,8" href="#hk7" />
  <area shape="circle" coords="400,144,8" href="#hk6" />
  <area shape="circle" coords="428,77,8" href="#hk8" />
  <area shape="circle" coords="309,92,8" href="#hk15" />
</map>
<map name="Map2" id="Map2">
<area shape="rect" coords="66,39,88,56" href="#am1" />
<area shape="rect" coords="101,165,116,182" href="#am2" />
<area shape="rect" coords="131,268,153,285" href="#am3" />
<area shape="rect" coords="505,297,541,332" href="#am1" />
<area shape="rect" coords="706,303,736,331" href="#am3" />
<area shape="rect" coords="708,84,742,119" href="#am2" />
</map>
<script type="text/javascript">
var initDate=$('#id_startDate').val();
$(document).ready(function(){
    //当mac.shtml 修改澳门的样式
     var cityCodeValue = $('#cityCode').val();
     if(cityCodeValue=='MAC'){
          $('button1').attr('class','bg_button2');
          $('button2').attr('class','bg_button1');
     }
   

	$('#id_startDate').click(function(event) {
			initDate=$('#id_startDate').val();
			showSearchCalendarn(this, 0, new Date(), AddDay('d', 60, new Date()), undefined, 0, undefined, event, undefined);	
			
			 $('#calendarDiv').click(
			 	function(event){
					var hkDate=$('#id_startDate').val();
					if(hkDate != initDate){
						 queryByTag('HKG');
					 }
					}	 
								   
		   );
						
			return false;
		}).blur(function() {
			showSearchCalendarn(this, 1);	
			
			return false;
	});
	$('#id_startDate1').click(function(event) {
			initDate=$('#id_startDate1').val();
			showSearchCalendarn(this, 0, new Date(), AddDay('d', 60, new Date()), undefined, 0, undefined, event, undefined);
		   $('#calendarDiv').click(
				function(event){			   	
					var macDate=$('#id_startDate1').val();
					if(macDate != initDate){
						 queryByTag('MAC');
					 }
					}
					);
			return false;
		}).blur(function() {
			showSearchCalendarn(this, 1);			
			return false;
	});
	document.getElementById("button1").style.display="block";
	document.getElementById("button2").style.display="block";
	var cityCode ='${cityCode}';
	if(cityCode == 'HKG'){
		chg_btn(1);
	}else if(cityCode == 'MAC'){
		chg_btn(2);
	}	
});


function queryByTag(cityCode){
    document.getElementById("cityCode").value=cityCode;
    if(cityCode == 'MAC'){
	    var ableSaleDateAoment = document.getElementById("id_startDate1").value;
	    document.getElementById("id_startDate").value = ableSaleDateAoment;
	    document.queryAllHkHotelForm.action = "mac.shtml";
    }
    document.queryAllHkHotelForm.submit();
	
}
function chg_btn(num){
	var id_name="button"+num;
	var tr_name="content"+num;
	for(i=1;i<=2;i++){
		document.getElementById("button"+i).className="bg_button2";
		document.getElementById("content"+i).style.display="none";
	}
	document.getElementById(id_name).className="bg_button1";
	document.getElementById(tr_name).style.display="block";
}
</script>
<!-- 酒店企业QQ -->
<script src="http://wres.mangocity.com/js/w/module/mango_online.js"></script>
<script type="text/javascript">
$(function(){
    $('#mangoServerOnline').mangoOnline({
			  top : 205,
			  setclass : 'on_flight',
			  url : 'http://b.qq.com/webc.htm?new=0&sid=800016693&o=www.mangocity.com&q=7',
			  week : '周一至周日',
			  time : '09:00-18:00'
	});
});
</script>

<!-- 广告 -->
<script type="text/javascript" src="http://wres.mangocity.com/js/w/hezuo/jsad.js"></script>
<%--酒店VRM（访客关系管理）二次营销 --%>
<s:if test='${application.OPEN_VRM} ==true'> 
<iframe src="http://serv1.vizury.com/analyze/analyze.php?account_id=VIZVRM128&param=h200&city=${cityCode }&indate=${ableSaleDate}&outdate=${ableSaleEndDate}&rooms=&ad=&ch=&section=1&level=1" scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>
</s:if>
  <%-- 这个放到最后，有百度监控，会影响速度--%>
<jsp:include page="../commonjsp/ _footer_monitor.jsp" flush="true" />
</body>
</html>
