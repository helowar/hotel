<%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib prefix="c" uri="core"%>
<%@ taglib prefix="fn" uri="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>芒果网网站地图</title>
<link rel="stylesheet" type="text/css" href="http://hres.mangocity.com/css/h/2011/hotel2011.css" />
<script type="text/javascript" src="js/hotelsearchJS/linkgeo.js"></script>
<style type="text/css">
.w960{ width:960px; margin:0 auto;}
a:link, a:visited{ color:#008800; text-decoration:none;}
a:hover{ color:#ff6600; text-decoration:underline;}
.hotelList{ padding:10px 0; background-color:#f8f8f8; border:1px solid #CCCCCC; line-height:21px;}
.hotelList h1{ padding-left:20px; font-size:16px; line-height:35px;}
.hotelList .desciption{ width:92%; margin:0 auto 8px;}
.hotelListCont{ width:92%; margin:0 auto; padding:10px 0; background-color:#FFFFFF; border:1px solid #e4e4e4;}
.cityHotelBox{ width:98%; margin:0 auto 10px; border-bottom:1px dashed #CCCCCC;}
.cityHotelBox h2{ padding-left:20px; line-height:29px;} 
.cityHotelBox table{ width:100%;}
.cityHotelBox table td{ width:12.5%; padding:4px 0; text-align:center;}
</style>
</head>
<body>
<!--=S head-->
<script type="text/javascript" src="http://www.mangocity.com/include/css/tel.js"></script>
<jsp:include page="../commonjsp/_header.jsp" flush="true" />
<!--=E head-->
<!--=S top bar -->
<div class="w960 hn-topbar">
    <div class="status step1">
        <ul>
            <li class="choice">查询</li>
            <li>选择</li>
            <li>预订</li>
            <li>完成</li>
        </ul>
    </div>
    <div class="crumbs">您现在的位置：<a href="http://www.mangocity.com" class="green" name="firstPage">首页</a> &gt; <a href="hotel-search.shtml" class="green" name="domesticHotel">国内酒店</a> &gt; <em>芒果网酒店预订</em></div>
</div>
<!--=E top bar-->
 <div class="w960">
<div class="hotelList">
  <h1>芒果网酒店预订</h1>
   <p class="desciption">芒果网拥有中国领先的酒店预订服务中心，为会员提供即时预订服务。 我们的合作酒店超过28000家，遍布全球134个国家和地区的5900余个城市。不仅为会员提供2－7折优惠房价，更有2000余家酒店保留房为会员出行提供更多保障。</p>
   <div class="hotelListCont">
     <c:forEach items="${cityList}" var="entry">
      <div class="cityHotelBox">
      <h2><c:out value="${entry.key}" />酒店</h2>
      <table cellpadding="0" cellspacing="0">
	    <c:set var="i" value="0"></c:set>
	     <c:forEach items="${entry.value}" var="beans" varStatus="status">
	     	<c:if test="${entry.key==beans[2]}">
		     	<c:if test="${i%8==0}">
		     	<tr>
		    	</c:if>
		     	  <td><a href="jiudian-map-list-${fn:toLowerCase(beans[0])}.html"><c:out value="${beans[1]}" />酒店</a></td>
		     	<c:if test="${i%8==7}">
		    	 </tr>
		   		</c:if>
		   		<c:set var="i" value="${i+1}"></c:set>
	   		</c:if>
	     </c:forEach>
      </table>
     </div>
     </c:forEach>
   </div>
</div>
</div>
 <!--=S footer-->
<jsp:include page="../commonjsp/_footer.jsp" flush="true" />
<!--=E footer -->

  <%-- 这个放到最后，有百度监控，会影响速度--%>
<jsp:include page="../commonjsp/ _footer_monitor.jsp" flush="true" />
</body>
</html>