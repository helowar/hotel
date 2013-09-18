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
<div class="hotel_ero">
   <h2>出错提示</h2>
   
   <p style="text-align:center"><c:if test="${not empty errorMessage}">${errorMessage}</c:if>
      <c:if test="${empty errorMessage}">请登录</c:if></p>
   
   <p class="fahui" id="fahui">

    <a href="javascript:history.go(-1)">返回重新查询</a>

  
     
   </p>
      <input type="hidden" name="errorCode" value="${errorCode}"/>
</div>
<script language="javascript" src="http://wres.mangocity.com/js/w/common/js/gethotel_error.js"></script>&nbsp; 
	</body>

</html>
