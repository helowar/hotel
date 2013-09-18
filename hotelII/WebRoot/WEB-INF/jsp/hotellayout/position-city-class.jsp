<%@ page contentType="text/html;charset=utf-8"%> 
<%@ taglib prefix="c" uri="core"%>
<%@ taglib prefix="fn" uri="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${cityName}酒店:${cityName}酒店预订首选芒果网！${cityName}宾馆查询预订 ${cityName}订酒店</title>
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
.cityHotelBox{ width:98%; margin:0 auto 10px;}
.cityHotelBox h2{ padding-left:20px; line-height:29px;} 
.cityHotelBox table{ width:100%;}
.cityHotelBox table td{padding:4px 0; text-align:center;}

.cityHotelBox dl{ width:100%; margin-bottom:10px; padding-bottom:15px; border-bottom:1px solid #CCCCCC;}
.cityHotelBox dt{padding-left:20px;}
.cityHotelBox dd{ margin:-21px 0 0 100px;}
.cityHotelBox dd a{ margin-right:15px;}
</style>
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
    <div class="crumbs">您现在的位置：<a href="http://www.mangocity.com" class="green" name="firstPage" >首页</a> &gt; <a href="hotel-search.shtml" class="green" name="domesticHotel">国内酒店</a> &gt; <em>${cityName}酒店预订</em></div>
</div>
<!--=E top bar-->
 <div class="w960">

<div class="hotelList">
   <h1>${cityName}酒店预订</h1>
   <p class="desciption">芒果网拥有中国领先的酒店预订服务中心，为会员提供即时预订服务。 我们的合作酒店超过28000家，遍布全球134个国家和地区的5900余个城市。不仅为会员提供2－7折优惠房价，更有2000余家酒店保留房为会员出行提供更多保障。</p>
   <div class="hotelListCont">
      <div class="cityHotelBox">
      <dl>
      <c:if test="${!empty resultMap.subway||!empty resultMap.airdrome||!empty resultMap.train}"> 
         <dt>交通：</dt>
         <dd><c:if test="${!empty resultMap.subway}"> 
			地铁：
			<c:forEach items="${resultMap.subway}" var="entry" varStatus="status">
		        <a href="list-${fn:toLowerCase(entry.cityCode)}-<c:out value="${entry.ID}"/>.html" title="<c:out value="${entry.name}"/>" ><c:out value="${entry.name}" /></a>
		     </c:forEach>
		     </c:if>
		     <c:if test="${!empty resultMap.airdrome}"> 
				机场：
				<c:forEach items="${resultMap.airdrome}" var="entry" varStatus="status">
			        <a href="list-${fn:toLowerCase(entry.cityCode)}-<c:out value="${entry.ID}"/>.html" title="<c:out value="${entry.name}"/>"><c:out value="${entry.name}" /></a>
			     </c:forEach>
		      </c:if>
		  	<c:if test="${!empty resultMap.train}">   
				火车站：
				<c:forEach items="${resultMap.train}" var="entry" varStatus="status">
				
			      <a href="list-${fn:toLowerCase(entry.cityCode)}-<c:out value="${entry.ID}"/>.html" title="<c:out value="${entry.name}"/>"><c:out value="${entry.name}" /></a>
			  </c:forEach>
		   </c:if>
		</dd>
		</c:if>
		<c:if test="${!empty resultMap.business}"> 
         <dt>商圈：</dt>
         <dd>
         	<c:forEach items="${resultMap.business}" var="entry" varStatus="status">
				
			      <a href="list-${fn:toLowerCase(cityCode)}-${fn:toLowerCase(entry[3])}.html" title="<c:out value="${entry[1]}" />"><c:out value="${entry[1]}" /></a>
			  </c:forEach>
		</dd>
		</c:if>
		<c:if test="${!empty resultMap.brand}"> 
         <dt>品牌：</dt>
         <dd>
         <c:forEach items="${resultMap.brand}" var="entry" varStatus="status">
			
			<a href="list-group-${fn:toLowerCase(cityCode)}-${entry[0]}.html" title="<c:out value="${entry[1]}" />"><c:out value="${entry[1]}" /></a>
			 </c:forEach>
         </dd>
         </c:if>
         <c:if test="${!empty resultMap.university}"> 
         <dt>大学：</dt>
         <dd>
         	<c:forEach items="${resultMap.university}" var="entry" varStatus="status">
				
		        <a href="list-${fn:toLowerCase(entry.cityCode)}-<c:out value="${entry.ID}"/>.html" title="<c:out value="${entry.name}" />"><c:out value="${entry.name}" /></a>
		        <c:if test="${status.count==20}">
					<a href="more-${fn:toLowerCase(cityCode)}-26.html" class="green">更多&gt;&gt;</a>
				</c:if>
		     </c:forEach>
		</dd>
         </c:if>
         <c:if test="${!empty resultMap.district}"> 
         <dt>行政区：</dt>
         <dd><c:forEach items="${resultMap.district}" var="entry" varStatus="status">
		
		      <a href="list-xingzheng-${fn:toLowerCase(cityCode)}-${fn:toLowerCase(entry[3])}.html" title="<c:out value="${entry[1]}" />"><c:out value="${entry[1]}" /></a>
		      
		  </c:forEach></dd>
         </c:if>
         <c:if test="${!empty resultMap.scenic}"> 
         <dt>景区：</dt>
         <dd>
         <c:forEach items="${resultMap.scenic}" var="entry" varStatus="status">
			
	        <a href="list-${fn:toLowerCase(entry.cityCode)}-<c:out value="${entry.ID}"/>.html" title="<c:out value="${entry.name}" />"><c:out value="${entry.name}" /></a>
	        <c:if test="${status.count==20}">
				<a href="more-${fn:toLowerCase(cityCode)}-23.html" class="green">更多&gt;&gt;</a>
			</c:if>
	     </c:forEach>
         </dd>
         </c:if>
         <c:if test="${!empty resultMap.hospital}"> 
         <dt>医院：</dt>
         <dd>
         <c:forEach items="${resultMap.hospital}" var="entry" varStatus="status">
			
	        <a href="list-${fn:toLowerCase(entry.cityCode)}-<c:out value="${entry.ID}"/>.html" title="<c:out value="${entry.name}" />"><c:out value="${entry.name}" /></a>
	        <c:if test="${status.count==27}">
				<a href="more-${fn:toLowerCase(cityCode)}-27.html" class="green">更多&gt;&gt;</a>
			</c:if>
	     </c:forEach>
         </dd>
         </c:if>
      </dl>
      
      <table cellpadding="0" cellspacing="0">
       <c:forEach items="${cityList}" var="beans" varStatus="status">
	     	<c:if test="${status.index%8==0}">
	     	<tr>
	    	</c:if>
	     	  <td><a href="list-${fn:toLowerCase(beans[0])}.html"><c:out value="${beans[1]}" />酒店</a></td>
	     		<c:if test="${status.index%8==7}">
	    	 </tr>
	   		</c:if>
	     </c:forEach>
         </table>
      </div>
      
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