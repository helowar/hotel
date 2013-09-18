<%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib prefix="c" uri="core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="http://hres.mangocity.com/css/h/2011/hotel2011.css" />
<script type="text/javascript" src="js/hotelsearchJS/linkgeo.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
<br /><!--=S head-->
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
    <div class="crumbs">您现在的位置：<a href="http://www.mangocity.com" class="green" name="firstPage">首页</a> &gt; <a href="hotel-search.shtml" class="green" name="domesticHotel">国内酒店</a> &gt; <em>目的地周边酒店列表大全</em></div>
</div>
<!--=E top bar-->
 <div class="w960 hotcity">
     <p>
      <c:forEach items="${classMap}" var="entry">
        <a href="hotel-city-position.shtml?type=<c:out value="${entry.key}" />"><c:out value="${entry.value}" />附近酒店</a>
     </c:forEach>
     </p>
 </div>
 <!--=S footer-->
<jsp:include page="../commonjsp/_footer.jsp" flush="true" />
<!--=E footer -->

  <%-- 这个放到最后，有百度监控，会影响速度--%>
<jsp:include page="../commonjsp/ _footer_monitor.jsp" flush="true" />
</body>
</html>