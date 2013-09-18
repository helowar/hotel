<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="core"%>
<%@ taglib prefix="fn" uri="fn"%>
<%@ taglib prefix="f" uri="functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">


  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>${brandName}酒店,${brandName}连锁酒店预订—芒果网酒店 mangocity.com</title>
    <meta name="description" content="芒果网品牌酒店为您提供${brandName}酒店、${brandName}经济酒店预订服务，预订电话40066-40066。">
    <meta name="keywords" content="${brandName}酒店连锁,${brandName}经济连锁酒店,连锁酒店,">
	<link rel="stylesheet" type="text/css" href="http://hres.mangocity.com/css/h/2011/hotel2011.css" />
	<link rel="stylesheet" type="text/css" href=" http://hres.mangocity.com/css/jquery.alert.css"/>
    <link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/w/module/datepicker.css" />
	<link rel="stylesheet" type="text/css" href=" http://hres.mangocity.com/css/jquery.alert.css"/>
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
    <a href="hotel-brandEnter.shtml" class="green" name="brandHotel">品牌酒店</a> &gt; <em>${brandName}</em></div>
</div> 
<!--=E top bar-->
  
<!--=S main -->
<div class="w960 cFloat">
	  <div class="bdct_info w960">
	    <h2><span><a href="hotel-brandEnter.shtml">更多品牌>></a></span>${brandName}</h2>    
	    <dl>
	      <dt><img src="http://himg.mangocity.com/img/h/2011/${brandPicture}.jpg" alt="${brandName}品牌酒店" /></dt>
	      <dd class="bdct_info_title">${brandName}</dd>
	      <dd class="grey">${brandIntroduce}<a href="#" class="bdct_info_none"></a></dd>
	    </dl>
	  </div>
  
	  <div class="bd-sidebar">
	     <div class="hn-boxtop"><h2>${brandName}分布城市</h2></div>
	     
	     <div class="bd-city">
	       <table cellpadding="0" cellspacing="0">
	          <c:forEach items = "${brandCitiesList}" var="letterCities">
	             <tr>
	            	<td class="letter orange" valign="top" align="center" width="10%">${letterCities.cityFirstLetter}</td>
	            	<td class="90%">
	              		 <c:forEach items = "${letterCities.brandCitiesList}" var ="brandCity" >
	              		   <c:if test = "${fn:length(brandCity.hotelList) gt 1}">
	              		      <a href="hotel-brand-${fn:toLowerCase(brandCity.cityCode)}-${brandCode}.html" title="${brandCity.cityName}"><span class="orange">${fn:substring(brandCity.cityName,0,2)}（${fn:length(brandCity.hotelList)}）</span></a>
	              		   </c:if>
	              		   <c:if test= "${fn:length(brandCity.hotelList) lt 2}">
	              		      <a href="hotel-brand-${fn:toLowerCase(brandCity.cityCode)}-${brandCode}.html" title="${brandCity.cityName}"><span class="">${brandCity.cityName}</span></a>
	              		   </c:if>
	              		 </c:forEach>  
	                 </td>
	          	</tr>   
	          </c:forEach>	          
	       </table>   
	     </div>
	     
	     <div class="hn-boxbot mgb10"></div>
	     
	    	<div class="hn-boxtop"><h2>附近酒店搜索</h2></div>
	    	<jsp:include page="../commonjsp/simpleHotelSearchFrame.jsp" flush="true" />
	        <div class="hn-boxbot mgb10"></div>
	        
	       <div class="hn-boxtop"><h2>其他品牌酒店</h2></div>
	       <div class="bdct_else">
	         <ul>	           
	           <c:forEach items="${otherBrandsList}" var="hotelBrand"> 
	              <li><a href="hotel-brand-${hotelBrand.brandCode}.html"><img src="http://himg.mangocity.com/img/h/2011/${hotelBrand.brandPicture}.jpg" alt="${hotelBrand.brandName}品牌酒店" /></a>
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
  
	  <div class="bd-main mgb10">
	      <div class="bdct_ct">
		      <c:forEach items="${brandHotelList}" var="cityHotels">
		          <c:if test="${fn:length(cityHotels.hotelList) gt 1}">
		              <h3>
		              	  <c:if test="${fn:length(cityHotels.hotelList) gt 3}">
		                     <span><a href="hotel-brand-${fn:toLowerCase(cityHotels.cityCode)}-${brandCode}.html">更多>></a></span>
		                  </c:if>
		                  ${cityHotels.cityName}${brandName}<b class="green">（${fn:length(cityHotels.hotelList)}）</b>
		              </h3>
		               <div class="bdct_ctInfo">
		                   <c:forEach items="${cityHotels.hotelList}" var="oneCityHotel" begin="0" end="2">
		                      <dl>
		                         <dt><a href="jiudian-${oneCityHotel.hotelId}.html"><img src="${oneCityHotel.outPictureName}" /></a></dt>
		                         <dd class="ctInfo_title">
		                         <a href="jiudian-${oneCityHotel.hotelId}.html" title="${oneCityHotel.chnName}">
		                                <c:if test="${fn:length(oneCityHotel.chnName) gt 11}">
		                                    ${fn:substring(oneCityHotel.chnName,0,10)}..
		                                </c:if>
		                                <c:if test="${fn:length(oneCityHotel.chnName) lt 12}">
		                                    ${oneCityHotel.chnName}
		                                </c:if>
		                          </a>      
                                  </dd>
		                         <dd class="grey">${oneCityHotel.chnAddress}</dd>
		                         <dd><a href="javascript:void(0);" onclick="googleMapPanel('${oneCityHotel.hotelId}')">[查看地图]</a>&nbsp;<a href="jiudian-${oneCityHotel.hotelId}.html">预订>></a></dd>
		                      </dl>
		                   </c:forEach>
		               </div>
		          </c:if>
		          </c:forEach>
		          <div style="clear:both"></div>		          
		          <c:forEach items="${brandHotelList}" var="cityHotels">
		           <c:if test="${fn:length(cityHotels.hotelList) lt 2}">
				      <div class="ctInfo_short">
					       <h3>${cityHotels.cityName}${brandName}</h3>
					       <c:forEach items="${cityHotels.hotelList}" var="oneCityHotel" begin="0" end="2">
			                   <dl>
			                      <dt><a href="jiudian-${oneCityHotel.hotelId}.html"><img src="${oneCityHotel.outPictureName}" /></a></dt>
			                      <dd class="ctInfo_title">
		                         <a href="jiudian-${oneCityHotel.hotelId}.html" title="${oneCityHotel.chnName}">
		                                <c:if test="${fn:length(oneCityHotel.chnName) gt 11}">
		                                    ${fn:substring(oneCityHotel.chnName,0,10)}..
		                                </c:if>
		                                <c:if test="${fn:length(oneCityHotel.chnName) lt 12}">
		                                    ${oneCityHotel.chnName}
		                                </c:if>
		                          </a>      
                                  </dd>
			                      <dd class="grey">${oneCityHotel.chnAddress}</dd>
			                      <dd><a href="javascript:void(0);" onclick="googleMapPanel('${oneCityHotel.hotelId}')">[查看地图]</a>&nbsp;<a href="jiudian-${oneCityHotel.hotelId}.html">预订>></a></dd>
			                   </dl>
			               </c:forEach>
				      </div>
		           </c:if>	          
		          </c:forEach>  
	       </div>
	  </div>
  
</div>

<!--=E main -->
  
  
  <jsp:include page="../commonjsp/_footer.jsp" flush="true" />
  <jsp:include page="../emap/singleHotelMap.jsp" flush="true" />
  
    <script type="text/javascript" src="http://wres.mangocity.com/js/home/cloud/js/city.hotel.js"></script>
	<script src="http://wres.mangocity.com/js/w/build/mgtool.js" type="text/javascript"></script>
    <script src="http://wres.mangocity.com/js/w/module/datepicker.js" type="text/javascript"></script>
    <script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/hotel2011.js"></script>
    <script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/h_index_2012.js"></script>
    
      <%-- 这个放到最后，有百度监控，会影响速度--%>
<jsp:include page="../commonjsp/ _footer_monitor.jsp" flush="true" />
  </body>
</html>

