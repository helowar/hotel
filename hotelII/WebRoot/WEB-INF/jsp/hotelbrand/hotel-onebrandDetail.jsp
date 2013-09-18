<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="core"%>
<%@ taglib prefix="fn" uri="fn"%>
<%@ taglib prefix="f" uri="functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">



 <head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>${cityName}${brandName}酒店,${cityName}${brandName}特价酒店预订—芒果网酒店 mangocity.com</title>
    <meta name="description" content="芒果网品牌酒店为您提供${cityName}${brandName}酒店、${cityName}${brandName}经济酒店预订服务，预订电话40066-40066。">
    <meta name="keywords" content="${cityName}${brandName}酒店连锁,${cityName}${brandName}经济连锁酒店,连锁酒店,">
	<link rel="stylesheet" type="text/css" href="http://hres.mangocity.com/css/h/2011/hotel2011.css" />
	<link rel="stylesheet" type="text/css" href=" http://hres.mangocity.com/css/jquery.alert.css"/>
    <link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/w/module/datepicker.css" />
	<link rel="stylesheet" type="text/css" href=" http://hres.mangocity.com/css/jquery.alert.css"/>
	<link rel="stylesheet" href="http://wres.mangocity.com/css/w/module/cityselecter.css" type="text/css" />
	<script type="text/javascript" src="http://wres.mangocity.com/js/home/cloud/js/jquery-1.3.2.js"></script>
    <script type="text/javascript" src="js/jQuery/jquery.alert.js"></script>
    <script type="text/javascript" src="http://hres.mangocity.com/js/mangocity.simplemodal.js"></script>
</head>
   
 <body>  
  <jsp:include page="../commonjsp/_header.jsp" flush="true" />
  
   <!--=S top bar -->
<div class="w960 hn-topbar">
    <div class="crumbs">您现在的位置：<a href="http://www.mangocity.com" class="green" name="firstPage" >首页</a>  &gt; 
    <a href="hotel-search.shtml" class="green" name="domesticHotel" >国内酒店</a> &gt; 
    <a href="hotel-brandEnter.shtml" class="green" name="brandHotel">品牌酒店</a> &gt; <em>${cityName}${brandName}</em></div>
</div>
<!--=E top bar-->

<!--=S main -->
<div class="w960 cFloat">
  <div class="bdct_info w960">
    <h2><span><a href="hotel-brandEnter.shtml">更多品牌>></a></span>${brandName}</h2>    
    <dl>
      <dt><img src="http://himg.mangocity.com/img/h/2011/${brandPicture}.jpg" alt="${brandName}连锁酒店" /></dt>
      <dd class="">${brandName}</dd>
      <dd id="brandCities">
         <c:forEach items="${brandHotelList}" var="hotelsForOneCity" begin="0" end="31" varStatus="status">
            <a href="hotel-brand-${fn:toLowerCase(hotelsForOneCity.cityCode)}-${brandCode}.html" title="${hotelsForOneCity.cityName}">
               <c:if test= "${fn:length(hotelsForOneCity.hotelList) gt 1}">
                   ${fn:substring(hotelsForOneCity.cityName,0,2)}（${fn:length(hotelsForOneCity.hotelList)}）
                </c:if>
                 <c:if test= "${fn:length(hotelsForOneCity.hotelList) eq 1}">
                   ${hotelsForOneCity.cityName}
                </c:if>
            </a>
         </c:forEach>
          <c:if test="${fn:length(brandHotelList) gt 32}">
                <a style="color:#ff6600" href="javascript:void(0);" onclick="hiddenAndShow('#brandCities','#moreBrandCities')" id="showMoreBrandCities">更多&gt;&gt;</a>
            </c:if>
      </dd>
      
      <dd id="moreBrandCities" class="dd4" style="display:none;">
           <c:forEach items="${brandHotelList}" var="hotelsForOneCity" varStatus="status">
            <a href="hotel-brand-${fn:toLowerCase(hotelsForOneCity.cityCode)}-${brandCode}.html" title="${hotelsForOneCity.cityName}">
               <c:if test= "${fn:length(hotelsForOneCity.hotelList) gt 1}">
                   ${fn:substring(hotelsForOneCity.cityName,0,2)}（${fn:length(hotelsForOneCity.hotelList)}）
                </c:if>
                <c:if test= "${fn:length(hotelsForOneCity.hotelList) eq 1}">
                   ${hotelsForOneCity.cityName}
                </c:if>
            </a>
         </c:forEach>  
          <a style="color:#ff6600" href="javascript:void(0);" onclick="hiddenAndShow('#moreBrandCities','#brandCities')" id="shouqi">收起</a>
      </dd>
    </dl>
  </div>
  
  <div class="bd-sidebar">
     <div class="hn-boxtop"><h2>附近酒店搜索</h2></div>
     <jsp:include page="../commonjsp/simpleHotelSearchFrame.jsp" flush="true" />
     <div class="hn-boxbot mgb10"></div>
     
     <!--  
     <div class="hn-boxtop"><h2>芒果会员推荐的酒店</h2></div>
     <div class="bdct_recommen">
       <ul>
         <li><a href="#" title="北京天缘府北京天缘府北京天缘府北京天缘府宾馆">北京天缘府宾馆</a><span class="orange">&yen 132</span></li>
         <li><a href="#" title="北京天缘府北京天缘府北京天缘府北京天缘府宾馆">北京天缘府宾馆</a><span class="orange">&yen 132</span></li>
         <li><a href="#" title="北京天缘府北京天缘府北京天缘府北京天缘府宾馆">北京天缘府宾馆</a><span class="orange">&yen 132</span></li>
         <li><a href="#" title="北京天缘府北京天缘府北京天缘府北京天缘府宾馆">北京天缘府宾馆</a><span class="orange">&yen 132</span></li>
         <li><a href="#" title="北京天缘府北京天缘府北京天缘府北京天缘府宾馆">北京天缘府宾馆</a><span class="orange">&yen 132</span></li>
         <li><a href="#" title="北京天缘府北京天缘府北京天缘府北京天缘府宾馆">北京天缘府宾馆</a><span class="orange">&yen 132</span></li>
       </ul>
     <p><a href="#">更多>></a></p>
     </div>
     <div class="hn-boxbot mgb10"></div>
      -->
        
     <div class="hn-boxtop"><h2>${cityName}其他品牌酒店</h2></div>
	 <div class="bdct_else">
	     <ul><c:forEach items="${otherBrandsList}" var="hotelBrand"> 
	           <li><a href="hotel-brand-${fn:toLowerCase(cityCode)}-${hotelBrand.brandCode}.html"><img src="http://himg.mangocity.com/img/h/2011/${hotelBrand.brandPicture}.jpg" alt="${hotelBrand.brandName}品牌酒店" /></a>
	                <a href="hotel-brand-${hotelBrand.brandCode}.html" class="bd_a_name" style="text-align:center;" title="${hotelBrand.brandName}">
	                  	 <c:if test="${fn:length(hotelBrand.brandName) gt 5}">
	                         ${fn:substring(hotelBrand.brandName,0,4)}..
	                     </c:if>
	                      <c:if test="${fn:length(hotelBrand.brandName) lt 6}">
	                      ${hotelBrand.brandName}
	                      </c:if>
	                  </a>
	            </li>
	         </c:forEach>
	     </ul>
		 <p><a href="hotel-brandEnter.shtml">更多>></a></p>
	  </div>
      <div class="hn-boxbot mgb10"></div>
  </div>
  
  <div class="bd-mainct mgb10">
    <div class="detail_nav">
                    <a href="#" class="don"><span>${hotelForOneCity.cityName}${brandName}（共${fn:length(hotelForOneCity.hotelList)}家）</span></a>
                    <a href="list-${fn:toLowerCase(hotelForOneCity.cityCode)}.html" class="don_right">更多${hotelForOneCity.cityName}酒店预订>></a>
    </div>
    
    <div class="bd-main-hn">
      <c:forEach items="${hotelForOneCity.hotelList}" var="oneHotel">
            <dl>
		        <dt><a href="jiudian-${oneHotel.hotelId}.html"><img src="${oneHotel.outPictureName}" width="60px" height="55px" /></a></dt>
		        <dd class="ctInfo_title">
		        <a href="jiudian-${oneHotel.hotelId}.html" title="${oneHotel.chnName}">
		               <c:if test="${fn:length(oneHotel.chnName) gt 17}">
		                   ${fn:substring(oneHotel.chnName,0,16)}..
		               </c:if>
		               <c:if test="${fn:length(oneHotel.chnName) lt 18}">
		                    ${oneHotel.chnName}
		               </c:if>
		        </a>
		        </dd>
		        <dd>${oneHotel.chnAddress}</dd>
		        <dd><a href="javascript:void(0);" onclick="googleMapPanel('${oneHotel.hotelId}')">[查看地图]</a>&nbsp;<a href="jiudian-${oneHotel.hotelId}.html">预订>></a></dd>
		        
		   </dl>
      </c:forEach>
    </div>

  </div>
</div>

<!--=E main -->
  <jsp:include page="../commonjsp/_footer.jsp" flush="true" />
  <jsp:include page="../emap/singleHotelMap.jsp" flush="true" />
    <script type="text/javascript" src="http://wres.mangocity.com/js/home/cloud/js/city.hotel.js"></script>
	<script src="http://wres.mangocity.com/js/w/build/mgtool.js" type="text/javascript"></script>
	<script src="http://wres.mangocity.com/js/w/module/cityselecter.js" type="text/javascript"></script>
    <script src="http://wres.mangocity.com/js/w/module/datepicker.js" type="text/javascript"></script>
<%--	<script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/hotel2011.js"></script>--%>
	<script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/h_index_2012.js"></script>
	<script type="text/javascript" src="http://hres.mangocity.com/js/h/2012/hotel2012.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2012/justforhotelcity.js"></script>
	<script type="text/javascript" />
        function hiddenAndShow(hiddenId,showId){
            jQuery(hiddenId).hide();
            jQuery(showId).show();
        }
	</script>
	
  <%-- 这个放到最后，有百度监控，会影响速度--%>
<jsp:include page="../commonjsp/ _footer_monitor.jsp" flush="true" />
  </body>
</html>
