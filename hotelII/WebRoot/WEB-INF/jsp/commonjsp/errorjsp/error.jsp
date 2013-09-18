<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="core"%>
<%@ taglib prefix="fn" uri="fn"%>
<%@ taglib prefix="fmt" uri="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>酒店预订_芒果网</title>
<meta name="description" content="芒果网提供全国三百多个城市，超过五千家酒店宾馆的查询预订服务。从芒果网预订酒店，不管是商务酒店，还是假日酒店，您都可享受便宜的折扣价格和高品质的服务">
<meta name="keywords" content="酒店，宾馆，内地酒店，港澳酒店，海外酒店，集体订房，酒店预订，预订酒店，商务酒店，假日酒店">
<link rel="stylesheet" type="text/css" href="http://hres.mangocity.com/css/h/2011/hotel2011.css" />
<style>
#fahui
{
	font-size: 12px;
	margin-left: auto;
	margin-right: auto;
	text-align: center;
	margin-top: 30px;
	margin-bottom: 10px;
	background-color: #FF6600;
	width: 100px;
	padding-top: 3px;
	padding-right: 5px;
	padding-bottom: 3px;
	padding-left: 5px;
}
#fahui a
{
	color: #FFFFFF;
	text-decoration: none;
	background-color: #FF6600;
	padding-top: 3px;
	padding-right: 5px;
	padding-bottom: 3px;
	padding-left: 5px;
	width: 100px;
	font-weight: bold;
}
#fahui a:hover
{
	color: #FFFFFF;
	text-decoration: none;
	background-color: #FF6600;
	padding-top: 3px;
	padding-right: 5px;
	padding-bottom: 3px;
	padding-left: 5px;
	font-weight: bold;
}


</style>
</head>
	<body>
<jsp:include page="../../commonjsp/_header.jsp" flush="true" />
<div class="hotel_ero">
   <h2>出错提示</h2>
   
   <p style="text-align:center"><c:if test="${not empty errorMessage}">${errorMessage}</c:if>
      <c:if test="${empty errorMessage}">很抱歉，芒果网暂未推出此产品，将来推出产品后联系您。</c:if></p>
   
   <p class="fahui" id="fahui">
   <c:choose>
   <c:when test="${hotelOrderFromBean.hotelId > 0}">
   <a href="hotel-information.shtml?hotelId=${hotelOrderFromBean.hotelId }&inDate=<fmt:formatDate value='${hotelOrderFromBean.checkinDate}' pattern='yyyy-MM-dd'/>&outDate=<fmt:formatDate value='${hotelOrderFromBean.checkoutDate}' pattern='yyyy-MM-dd'/>">返回重新预订</a>
   </c:when>
   <c:when test="${(hotelOrderFromBean.hotelId==0 or empty hotelOrderFromBean.hotelId) and not empty hotelOrderFromBean.cityCode}">
     <a href="hotel-query.shtml?cityName=${hotelOrderFromBean.cityName }&cityCode=${hotelOrderFromBean.cityCode }&inDate=<fmt:formatDate value='${hotelOrderFromBean.checkinDate}' pattern='yyyy-MM-dd'/>&outDate=<fmt:formatDate value='${hotelOrderFromBean.checkoutDate}' pattern='yyyy-MM-dd'/>">返回重新预订</a>
   </c:when>
   <c:otherwise>
    <a href="hotel-search.shtml">返回重新查询</a>
   </c:otherwise>
 </c:choose>
  
     
   </p>
      <input type="hidden" name="errorCode" value="${errorCode}"/>
</div>
<script language="javascript" src="http://wres.mangocity.com/js/w/common/js/gethotel_error.js"></script>
<%--=S footer--%>
<jsp:include page="../../commonjsp/_footer.jsp" flush="true" />
<%--=E footer --%>
<%-- 这个放到最后，有百度监控，会影响速度--%>
<jsp:include page="../../commonjsp/ _footer_monitor.jsp" flush="true" />
	</body>

</html>
