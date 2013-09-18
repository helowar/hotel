<%@ page language="java" import="java.util.*" pageEncoding="utf-8"  isELIgnored="false"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${cityName}酒店价格趋势图</title>
<script type="text/javascript" src="http://wres.mangocity.com/js/home/cloud/js/jquery_mango.js"></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/w/module/raphael-min-2.1.0.js"></script>
 <script  type="text/javascript" src="js/quotationdisplay/pricetrendmap.js"></script>
  <!-- <script  type="text/javascript" src="js/quotationdisplay/MG_trend_map.js"></script> -->
 <script type="text/javascript" src="http://wres.mangocity.com/js/w/module/MG_trend_map.js"></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/w/promotion/trend/banner_sycle.js"></script>

<link rel="stylesheet" href="http://wres.mangocity.com/css/w/module/hotelMap.css" />
 
<style> 
  a{ text-decoration:none;}
*{ padding:0; margin:0;}
body{ font-size:12px;}
 
 
</style>
 
</head>

<body>&nbsp; 
<input id="cityCode" type="hidden" value="${cityCode}"/>
<div class="mainWrap">
      <div class="mapPath"><span>您现在的位置：</span><a href="http://www.mangocity.com" target="_blank">首页</a><em>&gt;</em><a href="http://hotel.mangocity.com" target="_blank">国内酒店</a><em>&gt;</em><span class="cff6600">${cityName}酒店价格趋势</span></div>
     <h2 class="mainBoxT"><p>可点击图中曲线上的点进行查询，更新时间：<span id="updateTime">3012-6-15</span></p>${cityName}酒店价格趋势</h2>
    <div class="mainBox">
	        <div class="areaWrap" id="areaWrap">
			  
			</div>	
			<div id="container"></div> 
			 <div class="hotTips">
                    <h6>温馨小贴士：</h6>
                    <p>
                          1、适当的时间游香港，订酒店价格更便宜，您的旅游将节约更多的成本；<br />
                          2、香港酒店价格多变，提前预订酒店房间，能让您避免酒店价格上涨风险；<br />
                          3、畅享香港旅游，提前7天左右确定行程和酒店，是您节约成本舒适出行的最佳选择；   
                    </p>
                   
 
             </div> 
			 
			 <div class="polyNote">
				 <p>${cityName}酒店报价更新频繁，以上价格仅供参考，最终价格以支付时为准！</p>曲线说明：<span class="ad">芒</span>&nbsp;中高端酒店  <span class="ec">芒</span>&nbsp;经济型酒店
			</div> 
	</div>
    
	 <div id="bannerFadeCycle"></div>
			
            
</div>
</body>
</html>


