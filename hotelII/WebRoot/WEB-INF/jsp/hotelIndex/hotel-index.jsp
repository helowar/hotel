<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="酒店,酒店预订,酒店查询,宾馆预订,宾馆查询." />
<meta name="description" content="酒店预订：芒果网为您提供方便的酒店预订服务，商务酒店、快捷酒店、星级酒店、宾馆预订当然选芒果网，国内酒店查询预订全国免费预订电话40066-40066." />
<title>酒店预订-国内酒店预订查询-国内宾馆预订-芒果网</title>
<%
String projectCode;
projectCode = request.getParameter("projectcode");
if (projectCode != null && !projectCode.equals("")) {	
	Cookie cookieProject = new Cookie("projectcode",projectCode);
	cookieProject.setPath("/");
	response.addCookie(cookieProject);
}
%>

<link href="css/hotel-index/global.css" rel="stylesheet" type="text/css" />
<link href="css/hotel-index/hotel_index.css" rel="stylesheet" type="text/css" />
<link href="http://wres.mangocity.com/css/w/module/datepicker.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/hotel-index/jquery.autocomplete.css" />
<link rel="stylesheet" type="text/css" href="css/hotel-index/jquery.alert.css"/>

<script type="text/javascript" src="http://hres.mangocity.com/hotel/js/hotel_neiqian/hotelbanner.js"></script>
<script type="text/javascript" src="js/hotel-index/module/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="js/hotel-index/module/jquery.alert.js"></script>
<script type="text/javascript" src="js/hotelsearchJS/jquery.autocomplete.js"></script>
<script type="text/javascript" src="js/hotel-index/browserCompatibility.js"></script>


<script type="text/javascript" src="js/hotel-index/hotel_index_init.js"></script>
<script type="text/javascript" src="js/hotel-index/hotel_index_hander.js"></script>
<script type="text/javascript" src="js/hotel-index/hotel_index_show.js"></script>


<script type="text/javascript">

	document.domain = "mangocity.com"; //指定 document 所属的域,对接会员系统用

</script>
</head>
<body onkeydown="keydown(event)">
<!--=S head-->
<jsp:include page="../commonjsp/_header.jsp" flush="true" />
<!--=E head-->
<div class="w1016 clearfix" style="margin-top:10px;">
	<!--  酒店条件搜索框开始  -->
    <div class="hotel_search">
    	
    	<!--  隐藏查询酒店表单，用于搜索框及热门城市提交查询酒店  -->
	    <form id="queryForm" name="queryForm" action="hotel-query-home.shtml" method="post">
	    	<input type="hidden" name="cityCode" id = "e_cityCode"/>
	    	<input type="hidden" name="cityName" id = "e_cityName"/>
	    	<input type="hidden" name="inDate" id = "e_inDate"/>
	    	<input type="hidden" name="outDate" id = "e_outDate"/>
	    	<input type="hidden" name="hotelName" id = "e_hotelName"/>
	    	<input type="hidden" name="geoName" id = "e_geoName"/>
	    	<input type="hidden" name="geoId" id = "e_geoId"/>
	    </form>
    
    	<h1>国内酒店预订</h1>
    	<p>
			<span class="first">
            	<a href="http://ihotel.mangocity.com" class="haiwai" target="_brank" title="海外酒店">海外酒店</a>
            </span>
        </p>
        	<ul>
        		<li>
            		<span class="title" id="spancity">城市</span><span class="red">*</span>
                	 <input type="text" class="inp_txt start_city inputSel" id="start_city" maxlength="30" value="请选择城市"  onfocus="onfocusProcessor(this.id)"  onblur="blurProcessor(this.id)" />
                	<input type="hidden" class="inp_txt" id="hide_city" />
            	</li>           	
            	<li>
            		<span class="title">入住日期</span><span class="red">*</span>
                	<input type="text"  class="inp_time" id="start_time" readonly="true" />
            	</li>
            	<li>
            		<span class="title">退房日期</span><span class="red">*</span>
                	<input type="text" class="inp_time" id="end_time" readonly="true" />
            	</li>
           	 	<li>
            		<span class="title">关键词</span>
            		  &nbsp;<input type="text" id="hotelName" class="inp_txt keywordinput inputSel" value="酒店名称/品牌/地区"  type="text" onfocus="onfocusProcessor(this.id)"  onblur="blurProcessor(this.id)"  autocomplete="off" maxlength="30"/>
            	</li>
            	<li class="clearfix" style="margin-top:32px;">
                	<em class="map_icon" onclick="mapSubmit();"></em>
                	<a href="#" class="map_btn" id="map_query" >酒店地图</a>
                	<a href="javascript:;" class="search_btn" id="id_query"></a>
            	</li>
        	</ul>
    </div>   
    
    <!--  酒店条件搜索框结束  -->    
    <div class="right">
    	<!--  轮播广告开始  -->
    	<div class="cycleWrap" id="cycleWrap">
            <div id="cycle" style="width:610px; height:265px; overflow:hidden; position:relative;">
                <ul id="cycleul">
                </ul>
              </div>
              <ol class="cycleList" id="cycleList">
              </ol>
        </div>
        <!--  轮播广告结束  -->
       <!--  地标周边酒店开始  -->
        <div class="nearby_hotel" style="position:relative;">
        	<div class="nearby_til">
                <h3>地标周边酒店</h3>
                <input type="text" class="inputCity" id="mod_city" value="北京"/>
                <input type="hidden"  id="hide_mod" />
            </div>
		     <div class="nearby_con">
            	<div class="mod clearfix">
                	<ul class="clearfix">
                    	<li>
                        	<a href="javascript:;" class="mod_icon mod_1" id="airtrain-img"></a>
                            <a href="javascript:;" id="airtrain-txt">机场/火车</a>
                        </li>
                        <li>
                        	<a href="javascript:;" class="mod_icon mod_2" id="subway-img"></a>
                            <a href="javascript:;" id="subway-txt">地铁周边</a>
                        </li>
                        <li>
                        	<a href="javascript:;" class="mod_icon mod_3" id="district-img"></a>
                            <a href="javascript:;" id="district-txt">行政区</a>
                        </li>
                        <li>
                        	<a href="javascript:;" class="mod_icon mod_4" id="business-img"></a>
                            <a href="javascript:;"  id="business-txt">商业区</a>
                        </li>
                        <li>
                        	<a href="javascript:;" class="mod_icon mod_5" id="scenic-img"></a>
                            <a href="javascript:;" id="scenic-txt">旅游景区</a>
                        </li>
                        <li>
                        	<a href="javascript:;" class="mod_icon mod_6" id="hospital-img"></a>
                            <a href="javascript:;" id="hospital-txt">医院附近</a>
                        </li>
                        <li>
                        	<a href="javascript:;" class="mod_icon mod_7" id="university-img"></a>
                            <a href="javascript:;" id="university-txt">学校附近</a>
                        </li>
                    </ul>
                </div>
            </div>
            <!--  周边弹出层开始  -->
             <div class="catemod" method="detail" style="" id="catemod">
                <i class="action01"></i>
                <ul class="cate_more" method="tag" style="" id="cate_more">
                </ul>
                <ul class="cate_area" method="poiDetail" id="cate_area">
                </ul>
            </div>
            <!--  周边弹出层结束  -->
        </div>
        <!--  地标周边酒店结束  -->
    </div>    
</div>
<!--  特惠酒店开始 本地开发用  "/WEB-INF/jsp/hotelIndex/tehui.jsp"   -->

<jsp:include page="../commonjsp/includeconfig/tehui.jsp" flush="true" />


<!--  特惠酒店结束  -->

<div class="w1016 clearfix">
	<!--  手机客户端开始  -->
	<div class="hol_phone">
    	<h3>芒果网酒店 手机客户端 新鲜采摘 便捷体验</h3>
        <a href="http://itunes.apple.com/cn/app/id441029483?mt=8"  target="_Blank" class="iphone_btn" title="iphone客户端下载"></a>
        <a href="https://play.google.com/store/apps/details?id=mangocity.activity&feature=search_result#?t=W251bGwsMSwxLDEsIm1hbmdvY2l0eS5hY3Rpdml0eSJd"  target="_Blank" class="android_btn" title="android客户端下载"></a>
        <p class="code_wei">
			<img src="http://wimg.mangocity.com/img/f/banner/banner_2013/code_wei.gif"/>
			<br/>扫码下载客户端
		</p>
        <p class="clear"></p>
       
        <!-- 酒店新闻开始 本地开发用 /WEB-INF/jsp/hotelIndex/zixun.jsp -->
         <jsp:include page="../commonjsp/includeconfig/zixun.jsp" flush="true" />
        <!-- 酒店新闻结束-->

    </div>
	<!--  手机客户端结束  -->
    <!--  微博开始  -->  
    <div class="weibo">
		<iframe width="100%" height="220" class="share_self"  frameborder="0" scrolling="no" src="http://widget.weibo.com/weiboshow/index.php?language=&width=0&height=220&fansRow=2&ptype=0&speed=100&skin=5&isTitle=0&noborder=0&isWeibo=1&isFans=0&uid=2443376325&verifier=4cb952ba&dpc=1"></iframe>
	</div>
    <!--  微博结束  -->
</div>
<div class="w1016 clearfix">
	<div class="sidebar">
        <!--  广告信息开始 本地开发用 /WEB-INF/jsp/hotelIndex/guanggao.jsp -->
          <jsp:include page="../commonjsp/includeconfig/guanggao.jsp" flush="true" />
        <!--  广告信息结束  -->
        <div class="whyus">
        	<h3>为什么选择芒果酒店？</h3>
            <dl>
            	<dt>
                	<i></i>源于近百年历史的港中旅集团
                </dt>
                <dd>中国最大旅游集团旗下唯一在线品牌</dd>
                <dt>
                	<i></i>全球24万家酒店即时预订
                </dt>
                <dd>国内4万家，海外20万家酒店覆盖全球</dd>
                <dt>
                	<i></i>最便捷、最真实的现金返还
                </dt>
                <dd>无需点评、无需用劵、提现无门槛，返现快速到账</dd>
                <dt>
                	<i></i>连续5年全国最佳客服中心
                </dt>
                <dd>7X24小时全天候服务，客户满意率达99.6%</dd>
                <dt>
                	<i></i>出行无忧的服务承诺
                </dt>
                <dd>到店有房，出行无忧！<a href="http://www.mangocity.com/promotion/h100802chengnuo2.html" target="_brank">低价保障，三倍赔付！</a></dd>
            </dl>
        </div>    
        <!--  广告信息结束  -->
    </div>
    <!--  热门城市开始  -->
    <div class="hot_city">
    	<h3>热门城市酒店</h3>
        <div class="cityBox" onclick="location.href='http://hotel.mangocity.com/list-1-pek.html'">
            <a href="http://hotel.mangocity.com/list-1-pek.html" id="PEK-img" title="北京酒店预定">
                <img src="http://himg.mangocity.com/hotel/img/h/hotel-index/PEK.png" width="100" height="100" alt="北京酒店预定" />
            </a>
            <a href="http://hotel.mangocity.com/list-1-pek.html" title="北京酒店预定" class="txt" id="PEK-txt">
                <em>北京</em>
                <span>约 2020家酒店</span>
            </a>
            <p class="clear"></p>
        </div>
        <div class="cityBox" onclick="location.href='http://hotel.mangocity.com/list-1-sha.html'">
            <a href="http://hotel.mangocity.com/list-1-sha.html" id="SHA-img" title="上海酒店预定">
                <img src="http://himg.mangocity.com/hotel/img/h/hotel-index/SHA.png" width="100" height="100" alt="上海酒店预定" />
            </a>
            <a href="http://hotel.mangocity.com/list-1-sha.html" title="上海酒店预定" class="txt" id="SHA-txt">
                <em>上海</em>
                <span>约 2000家酒店</span>
            </a>
            <p class="clear"></p>
        </div>
        <div class="cityBox" onclick="location.href='http://hotel.mangocity.com/list-1-can.html'">
            <a href="http://hotel.mangocity.com/list-1-can.html" id="CAN-img" title="广州酒店预定">
                <img src="http://himg.mangocity.com/hotel/img/h/hotel-index/CAN.png" width="100" height="100" alt="广州酒店预定"/>
            </a>
            <a href="http://hotel.mangocity.com/list-1-can.html" title="广州酒店预定" class="txt" id="CAN-txt">
                <em>广州</em>
                <span>约 1000家酒店</span>
            </a>
            <p class="clear"></p>
        </div>
        <div class="cityBox" onclick="location.href='http://hotel.mangocity.com/list-1-szx.html'">
            <a href="http://hotel.mangocity.com/list-1-szx.html" id="SZX-img" title="深圳酒店预定">
                <img src="http://himg.mangocity.com/hotel/img/h/hotel-index/SZX.png" width="100" height="100" alt="深圳酒店预定" />
            </a>
            <a href="http://hotel.mangocity.com/list-1-szx.html" title="深圳酒店预定" class="txt" id="SZX-txt">
                <em>深圳</em>
                <span>约 800家酒店</span>
            </a>
            <p class="clear"></p>
        </div>
        <div class="cityBox" onclick="location.href='http://hotel.mangocity.com/list-1-hkg.html'">
            <a href="http://hotel.mangocity.com/list-1-hkg.html" id="HKG-img" title="香港酒店预定">
                <img src="http://himg.mangocity.com/hotel/img/h/hotel-index/HKG.png" width="100" height="100" alt="香港酒店预定" />
            </a>
            <a href="http://hotel.mangocity.com/list-1-hkg.html" title="香港酒店预定" class="txt" id="HKG-txt">
                <em>香港</em>
                <span>约 200家酒店</span>
            </a>
            <p class="clear"></p>
        </div>
        <div class="cityBox" onclick="location.href='http://hotel.mangocity.com/list-1-syx.html'">
            <a href="http://hotel.mangocity.com/list-1-syx.html" id="SYX-img" title="三亚酒店预定">
                <img src="http://himg.mangocity.com/hotel/img/h/hotel-index/SYX.png" width="100" height="100" alt="三亚酒店预定" />
            </a>
            <a href="http://hotel.mangocity.com/list-1-syx.html" title="三亚酒店预定" class="txt" id="SYX-txt">
                <em>三亚</em>
                <span>约 580家酒店</span>
            </a>
            <p class="clear"></p>
        </div>
        <div class="cityBox" onclick="location.href='http://hotel.mangocity.com/list-1-ctu.html'">
            <a href="http://hotel.mangocity.com/list-1-ctu.html" id="CTU-img" title="成都酒店预定">
                <img src="http://himg.mangocity.com/hotel/img/h/hotel-index/CTU.png" width="100" height="100" alt="成都酒店预定" />
            </a>
            <a href="http://hotel.mangocity.com/list-1-ctu.html" title="成都酒店预定" class="txt" id="CTU-txt">
                <em>成都</em>
                <span>约 1000家酒店</span>
            </a>
            <p class="clear"></p>
        </div>
        <div class="cityBox" onclick="location.href='http://hotel.mangocity.com/list-1-hgh.html'">
            <a href="http://hotel.mangocity.com/list-1-hgh.html" id="HGH-img" title="杭州酒店预定">
                <img src="http://himg.mangocity.com/hotel/img/h/hotel-index/HGH.png" width="100" height="100" alt="杭州酒店预定" />
            </a>
            <a href="http://hotel.mangocity.com/list-1-hgh.html" title="杭州酒店预定" class="txt" id="HGH-txt">
                <em>杭州</em>
                <span>约 1050家酒店</span>
            </a>
            <p class="clear"></p>
        </div>
        <div class="cityBox" onclick="location.href='http://hotel.mangocity.com/list-1-nkg.html'">
            <a href="http://hotel.mangocity.com/list-1-nkg.html" id="NKG-img" title="南京酒店预定">
                <img src="http://himg.mangocity.com/hotel/img/h/hotel-index/NKG.png" width="100" height="100" alt="南京酒店预定" />
            </a>
            <a href="http://hotel.mangocity.com/list-1-nkg.html" title="南京酒店预定" class="txt" id="NKG-txt">
                <em>南京</em>
                <span>约 750家酒店</span>
            </a>
            <p class="clear"></p>
        </div>
        <div class="cityBox" onclick="location.href='http://hotel.mangocity.com/list-1-xmn.html'">
            <a href="http://hotel.mangocity.com/list-1-xmn.html" id="XMN-img" title="厦门酒店预定">
                <img src="http://himg.mangocity.com/hotel/img/h/hotel-index/XMN.png" width="100" height="100" alt="厦门酒店预定" />
            </a>
            <a href="http://hotel.mangocity.com/list-1-xmn.html" title="厦门酒店预定" class="txt" id="XMN-txt">
                <em>厦门</em>
                <span>约 980家酒店</span>
            </a>
            <p class="clear"></p>
        </div>
    </div>
    <!--  热门城市结束  -->
</div>
<div class="w1016">
	<!--  品牌酒店开始  -->
    <div class="brand_til">
    	<span>品牌酒店</span>
        <a href="javascript:;" class="cleftb lno" id="btn_prev"></a>      
        <span id="slider-img" class="slider-img">
            <a href="javascript:;" class="cur"></a>
            <a href="javascript:;"></a>
            <a href="javascript:;" ></a>
            <a href="javascript:;"></a>
            <a href="javascript:;" ></a>
            <a href="javascript:;" ></a>
		</span>
        <a href="javascript:;" class="crightb ryes" id="btn_next"></a>
    </div>
    <!--  品牌切换  -->
    <div id="slider">
    	<div id="scroll_wrapper">
        	<!--  存放酒店图标开始  -->
            <div class="child">
                <ul class="clearfix">
                      <li><a href="hotel-brand-1041.html"><img src="http://himg.mangocity.com/img/h/2011/1041.jpg"  alt="如家快捷酒店"/></a><br />
			          <a href="hotel-brand-1041.html" title="如家快捷酒店" class="bd_a_name">如家快捷..</a></li>
			          <li><a href="hotel-brand-550.html"><img src="http://himg.mangocity.com/img/h/2011/550.jpg" alt="汉庭快捷酒店"/></a><br />
			          <a href="hotel-brand-550.html" title="汉庭快捷酒店" class="bd_a_name">汉庭快捷..</a></li>
			          <li><a href="hotel-brand-1321.html"><img src="http://himg.mangocity.com/img/h/2011/1321.jpg" alt="锦江之星"/></a><br />
			          <a href="hotel-brand-1321.html" title="锦江之星" class="bd_a_name">锦江之星</a></li>
			          <li><a href="hotel-brand-1061.html"><img src="http://himg.mangocity.com/img/h/2011/1061.jpg" alt="格林豪泰"/></a><br />
			          <a href="hotel-brand-1061.html" title="格林豪泰" class="bd_a_name">格林豪泰</a></li>
			          <li><a href="hotel-brand-461.html"><img src="http://himg.mangocity.com/img/h/2011/461.jpg" alt="莫泰168"/></a><br />
			          <a href="hotel-brand-461.html" title="莫泰168" class="bd_a_name">莫泰168</a></li>
			          <li><a href="hotel-brand-512.html"><img src="http://himg.mangocity.com/img/h/2011/512.jpg" alt="速8"/></a><br />
			          <a href="hotel-brand-512.html" title="速8" class="bd_a_name">速8</a></li>
			          <li><a href="hotel-brand-625.html"><img src="http://himg.mangocity.com/img/h/2011/625.jpg" alt="南苑e家"/></a><br />
			          <a href="hotel-brand-625.html" title="南苑e家" class="bd_a_name">南苑e家</a></li>
			          <li><a href="hotel-brand-741.html"><img src="http://himg.mangocity.com/img/h/2011/741.jpg" alt="宜必思"/></a><br />
			          <a href="hotel-brand-741.html" title="宜必思" class="bd_a_name">宜必思</a></li>
			          <li><a href="hotel-brand-423.html"><img src="http://himg.mangocity.com/img/h/2011/423.jpg" alt="城市客栈"/></a><br />
			          <a href="hotel-brand-423.html" title="城市客栈" class="bd_a_name">城市客栈</a></li>
			          <li><a href="hotel-brand-483.html"><img src="http://himg.mangocity.com/img/h/2011/483.jpg" alt="欣燕都"/></a><br />
			          <a href="hotel-brand-483.html" title="欣燕都" class="bd_a_name">欣燕都</a></li>
                </ul>             	
            </div>
            <div class="child">
                <ul class="clearfix">
                      <li><a href="hotel-brand-553.html"><img src="http://himg.mangocity.com/img/h/2011/553.jpg" alt="富驿时尚"/></a><br />
			          <a href="hotel-brand-553.html" title="富驿时尚" class="bd_a_name">富驿时尚</a></li>
			          <li><a href="hotel-brand-502.html"><img src="http://himg.mangocity.com/img/h/2011/502.jpg" alt="金一村"/></a><br />
			          <a href="hotel-brand-502.html" title="金一村" class="bd_a_name">金一村</a></li>
			          <li><a href="hotel-brand-723.html"><img src="http://himg.mangocity.com/img/h/2011/723.jpg" alt="瑞心快捷"/></a><br />
			          <a href="hotel-brand-723.html" title="瑞心快捷" class="bd_a_name">瑞心快捷</a></li>
			          <li><a href="hotel-brand-1165.html"><img src="http://himg.mangocity.com/img/h/2011/1165.jpg" alt="飘HOME"/></a><br />
			          <a href="hotel-brand-1165.html" title="飘HOME" class="bd_a_name">飘HOME</a></li>
			          <li><a href="hotel-brand-564.html"><img src="http://himg.mangocity.com/img/h/2011/564.jpg" alt="山水时尚"/></a><br />
			          <a href="hotel-brand-564.html" title="山水时尚" class="bd_a_name">山水时尚</a></li>
			          <li><a href="hotel-brand-1101.html"><img src="http://himg.mangocity.com/img/h/2011/1101.jpg" alt="中安之家"/></a><br />
			          <a href="hotel-brand-1101.html" title="中安之家" class="bd_a_name">中安之家</a></li>
			          <li><a href="hotel-brand-1141.html"><img src="http://himg.mangocity.com/img/h/2011/1141.jpg" alt="神舟商旅"/></a><br />
			          <a href="hotel-brand-1141.html" title="神舟商旅" class="bd_a_name">神舟商旅</a></li>
			          <li><a href="hotel-brand-560.html"><img src="http://himg.mangocity.com/img/h/2011/560.jpg" alt="扬子江"/></a><br />
			          <a href="hotel-brand-560.html" title="扬子江" class="bd_a_name">扬子江</a></li>
			          <li><a href="hotel-brand-1242.html"><img src="http://himg.mangocity.com/img/h/2011/1242.jpg" alt="旅居"/></a><br />
			          <a href="hotel-brand-1242.html" title="旅居" class="bd_a_name">旅居</a></li>
			          <li><a href="hotel-brand-781.html"><img src="http://himg.mangocity.com/img/h/2011/781.jpg" alt="蓝海大饭店"/></a><br />
			          <a href="hotel-brand-781.html" title="蓝海大饭店" class="bd_a_name">蓝海大饭店</a></li>
                </ul>             	
            </div>
            <!--  存放酒店图标结束  -->
        </div>
    </div>  
    <!--  品牌酒店结束  -->
</div>
<div class="hol_footer clearfix">
	<dl>
    	<dt class="dt1">新手指南</dt>
        <dd>
        	<ul>
            	<li><a href="http://club.mangocity.com/faq/faqclass.aspx?cid=30" target="_brank">酒店预订注意事项</a></li>
            	<li><a href="http://www.mangocity.com/help/zhuce.htm" target="_brank">如何注册成为芒果网会员？</a></li>
                <li><a href="http://club.mangocity.com/faq/faqclass.aspx?cid=27" target="_brank">网上预订有哪些支付方式？</a></li>
                <li><a href="http://club.mangocity.com/faq/faqclass.aspx?cid=35" target="_brank">如何修改及取消酒店订单？</a></li>
            </ul>
        </dd>
    </dl>
    <dl>
    	<dt class="dt2">预订问题</dt>
        <dd>
        	<ul>
            	<li><a href="http://www.mangocity.com/help/hotel.htm" target="_brank">如何预订国内酒店?</a></li>
                <li><a href="http://club.mangocity.com/faq/faq.aspx?fid=5723" target="_brank">如何预订到更优惠的酒店？</a></li>
                <li><a href="http://club.mangocity.com/faq/faqclass.aspx?cid=59" target="_brank">酒店代金劵的使用规则</a></li>
                <li><a href="http://club.mangocity.com/faq/faqclass.aspx?cid=39" target="_brank">预订港澳酒店的注意事项</a></li>
            </ul>
        </dd>
    </dl>
    <dl>
    	<dt class="dt3">返现提现</dt>
        <dd>
        	<ul>
            	<li><a href="http://www.mangocity.com/help/recash.html" target="_brank">什么是返现奖金及现金账户？</a></li>
                <li><a href="http://www.mangocity.com/help/recash.html#r10" target="_brank">返现奖金在哪里可以查看？</a></li>
                <li><a href="http://www.mangocity.com/help/return.html" target="_brank">返现奖金的提现流程</a></li>
                <li><a href="http://www.mangocity.com/help/recash.html#r21" target="_brank">现金返还及赠送积分规则</a></li>
            </ul>
        </dd>
    </dl>
    <dl>
    	<dt class="dt4">会员服务</dt>
        <dd>
        	<ul>
            	<li><a href="http://club.mangocity.com/faq/faqclass.aspx?cid=21" target="_brank">芒果网会员及会籍介绍</a></li>
                <li><a href="http://club.mangocity.com/faq/faqclass.aspx?cid=24" target="_brank">芒果网会员积分规则</a></li>
                <li><a href="http://club.mangocity.com/faq/faqclass.aspx?cid=23" target="_brank">关于会员账户的密码问题</a></li>
                <li><a href="http://club.mangocity.com/faq/faqclass.aspx?cid=37" target="_brank">芒果网积分的用途</a></li>
            </ul>
        </dd>
    </dl>
    <div class="telphone">24小时酒店预订热线40066-40066</div>
</div>
<!--  酒店预订热门城市开始  -->
<div class="w1016 hotcity">
    <h2>酒店预订快捷入口  &nbsp;&nbsp;<a href="jiudian-map-index.html"><strong>更多热门城市酒店预订&gt;&gt;</strong></a>
     &nbsp;<a href="hotel-brandEnter.shtml"><strong>品牌酒店预订&gt;&gt;</strong></a>
    </h2>
    <p>
      <a href="list-pek.html">北京酒店</a>
      <a href="list-sha.html">上海酒店</a>
      <a href="list-tsn.html">天津酒店</a>
      <a href="list-ckg.html">重庆酒店</a>
      <a href="list-can.html">广州酒店</a>
      <a href="list-szx.html">深圳酒店</a>
      <a href="list-hgh.html">杭州酒店</a>
      <a href="list-hrb.html">哈尔滨酒店</a>
      <a href="list-urs.html">乌鲁木齐酒店</a>
      <a href="list-syx.html">三亚酒店</a>
      <a href="list-xmn.html">厦门酒店</a>
      <a href="list-ctu.html">成都酒店</a>
      <a href="list-tao.html">青岛酒店</a>
      <a href="list-dlc.html">大连酒店</a>		  
      <a href="list-hkg.html">香港酒店</a>
      <a href="list-mac.html">澳门酒店</a>	  

    </p>
</div>
<!--  酒店预订热门城市结束  -->



<!-- 参数隐藏临时保存 -->
<input type="hidden" value="${cityName}" name="default_cityName" id = "default_cityName"/>
<input type="hidden" value="${cityCode}" name="default_cityCode" id = "default_cityCode"/>
<input type="hidden" value="${brandString}" name="brandString" id = "brandString"/>
<input type="hidden" name="mod_cityCode" id="mod_cityCode" value="PEK" />
<input type="hidden" id="daysOfStay" type="text" readonly="true" value="1" name=""/>

<script type="text/javascript" src="http://wres.mangocity.com/js/w/ued_js/jquery.mangoUI.js"></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/home/cloud/js/city.hotel.js"></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/w/build/mgtool.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/hotel/js/h/hotel-index/ToolCitySelecter.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/hotel/js/h/hotel-index/ToolDateSelecter.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/hotel/js/h/hotel-index/pislider.js"></script>

<!--  footer开始  -->
<jsp:include page="../commonjsp/_footer.jsp" flush="true" />
<!--  footer结束  -->

  <%-- 这个放到最后，有百度监控，会影响速度--%>
<jsp:include page="../commonjsp/ _footer_monitor.jsp" flush="true" />
</body>
</html>
