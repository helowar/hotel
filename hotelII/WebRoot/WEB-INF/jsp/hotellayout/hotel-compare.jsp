<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="core"%>
<%@ taglib prefix="fn" uri="fn"%>
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
<script type="text/javascript" src="js/jQuery/jquery.alert.js"></script>
<script type="text/javascript" src="js/hotelcompare/hotelcompare.js"></script>
</head>
  
<body>
<jsp:include page="../commonjsp/_header.jsp" flush="true" />
<div class="contrast">
  <div class="hn-topbar">
    <div class="crumbs">您现在的位置：<a href="http://www.mangocity.com" name="firstPage" class="green">首页</a> > 
    <a href="http://hotel.mangocity.com" name="domesticHotel" class="green">国内酒店</a> > <em>酒店对比</em></div>
    <div class="contrast_header">
        <div class="contrast_pic">酒店及图片</div>
        <div class="contrast_info">房型及房价</div>
        <div class="contrast_level">酒店星级</div>
        <div class="contrast_comment">用户点评</div>
        <div class="contrast_rim">位置及周边</div>
    </div>
    
    <input type="hidden" id="hotelCount" value="${fn:length(hotelVOList)}" />
    <%-- 对比内容start --%>
    <c:forEach items="${hotelVOList}" var="hotelVO">
	<div class="contrast_td" style="">
		<div class="contrast_pic">
			<a href="javascript:void(0)" onclick="closeThisDiv(this)" name="closeThisDiv" class="contrast_close"><img src="http://himg.mangocity.com/img/h/2011/hotel_close.jpg" /></a>
			<div class="img360">
				<h3>${hotelVO.chnName}</h3>
				<a href="hotel-information.shtml?hotelId=${hotelVO.hotelId}&inDate=${compareInDate}&outDate=${compareOutDate}" class="imgWrap" target="_blank">
				<img width="75px" height="75px" alt="" src="${hotelVO.outPictureName}"></a>
				<a href="hotel-information.shtml?hotelId=${hotelVO.hotelId}&inDate=${compareInDate}&outDate=${compareOutDate}" class="imgtit" target="_blank">查看酒店图片</a>
			</div>
		</div>
		<div class="contrast_info">
			<div class="contrast_box">
				<ul>
				    <c:set var="indexCommdity" value="0" />
					<c:forEach items="${hotelVO.roomTypes}" var="roomTypeVO">
					     <c:forEach items="${roomTypeVO.commodities}" var="commodityVO">
					         <c:if test="${indexCommdity<3}">
		                   	   <li><span class="orange"><strong>${commodityVO.currencySymbol}${commodityVO.avlPrice}</strong>(返现${commodityVO.avlReturnCashNum}元)</span>
		                        <label title="${roomTypeVO.roomtypeName}(${commodityVO.commodityName})">${roomTypeVO.roomtypeName}(${commodityVO.commodityName})</label> </li>
		                      </c:if>
		                     <c:set var="indexCommdity" value="${indexCommdity+1}"/>
					     </c:forEach>
					</c:forEach>
				</ul>
			</div>
			<a href="hotel-information.shtml?hotelId=${hotelVO.hotelId}&inDate=${compareInDate}&outDate=${compareOutDate}" target="_blank">查看详情，预订酒店>></a>
		</div>
		<div class="contrast_level">
		   <c:choose>    
           		<c:when test="${fn:indexOf(hotelVO.hotelStar,'star') ge 0}">     
           			<em class="hotelstar ${hotelVO.hotelStar}" ></em>		    
           		</c:when>    
           		<c:otherwise>     
           			<em class="hotelstarName" >&nbsp;&nbsp;${hotelVO.hotelStar}</em>   
           		</c:otherwise>   
           </c:choose> 
		</div>
		<div class="contrast_comment">
			<p>
				<span class="orange">${hotelVO.commentSummaryVO.commendUp}人推荐/${hotelVO.commentSummaryVO.commendDown}人不推荐</span>&nbsp;&nbsp;得分：
				<span class="orange">${hotelVO.commentSummaryVO.averAgepoint}分</span>
			</p>
			<a href="http://www.mangocity.com/HotelCommentWeb/hotel-comment-details.shtml?hotelId=${hotelVO.hotelId}" target="_blank">查看${hotelVO.commentSummaryVO.commentNum}位客户点评>></a>
		</div>
		<div class="contrast_rim">
			<a href="javascript:googleMapPanel(${hotelVO.hotelId});" class="map">电子地图</a>
			<ul><li>【酒店地址】</li>
                ${hotelVO.trafficInfo}
				<li>【商业区】</li>
				<li><a href="hotel-query.shtml?bizCode=${hotelVO.bizZone}&cityCode=${compareCityCode}&inDate=${compareInDate}&outDate=${compareOutDate}" target="_blank">
				${hotelVO.bizZoneValue}</a></li>
			</ul>
		</div>
	</div>
	</c:forEach>  
    <%-- 对比内容end --%>
  </div>
</div>
<jsp:include page="../commonjsp/_footer.jsp" flush="true" />
<%-- 地图浮动层 --%>
<div id="id_Emap" style="display:none">
    <div class="dlu" id="loadMap">
    </div>
</div>
<script type="text/javascript" src="http://hres.mangocity.com/js/mangocity.simplemodal.js"></script>
<script type="text/javascript">
   var hotelCount = jQuery("#hotelCount").val();
   if(hotelCount<"3"){
        jQuery("a:[name='closeThisDiv']").each(function(i){
        jQuery(this).remove();
     });
   }
</script>

  <%-- 这个放到最后，有百度监控，会影响速度--%>
<jsp:include page="../commonjsp/ _footer_monitor.jsp" flush="true" />
</body>
</html>
