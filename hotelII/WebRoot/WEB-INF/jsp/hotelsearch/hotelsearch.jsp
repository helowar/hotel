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
<link rel="stylesheet" type="text/css" href="http://hres.mangocity.com/css/h/2011/hotel2011.css" />
<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/w/module/datepicker.css" />
<link rel="stylesheet" type="text/css" href="css/hotelsearch/jquery.autocomplete.css" />
<link rel="stylesheet" type="text/css" href="css/member/memberlogin.css" />
<link rel="stylesheet" type="text/css" href=" http://hres.mangocity.com/css/jquery.alert.css"/>
<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/w/module/mango_online.css" />
<link rel="stylesheet" href="http://wres.mangocity.com/css/w/module/cityselecter.css" type="text/css" />
<script type="text/javascript" src="http://wres.mangocity.com/js/home/cloud/js/jquery-1.3.2.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/DomElemAbsPosition.js"></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/h/banner/HotelHomeBanner.js"></script>
<script src="http://hres.mangocity.com/js/mangocity.simplemodal.js"  type="text/javascript"></script>
<script type="text/javascript" src="js/hotelsearchJS/jquery.autocomplete.js"></script>
<script type="text/javascript" src="js/jQuery/jquery.alert.js"></script>
<script type="text/javascript" src="js/hotelsearchJS/hotelsearch.js"></script>

<script type="text/javascript">

	document.domain = "mangocity.com"; //指定 document 所属的域,对接会员系统用

</script>
</head>

<body onkeydown="keydown(event)">
<%--=S head--%>
<jsp:include page="../commonjsp/_header.jsp" flush="true" />
<%--=E head--%>

<%--=S top bar --%>
<div class="w960 hn-topbar">
    <div class="status step1">
        <ul>
            <li class="choice">查询</li>
            <li>选择</li>
            <li>预订</li>
            <li>完成</li>
        </ul>
    </div>
    <h1>国内酒店预订</h1>
</div>
<%--=E top bar--%>

<%--=S main --%>
<div class="w960 cFloat">
    <div class="hn-main">
        <!--==S search box-->
        <div class="hn-search mgb10">
        <input type="hidden" name="memberPath" id="memberPath" value="${memberPath}" />
        <form id="queryForm" name="queryForm" action="hotel-query.shtml" method="get">
        <input type="hidden" id="minPrice" name="minPrice" />
        <input type="hidden" id="maxPrice" name="maxPrice" />
        <input type="hidden" id="priceStr" name="priceStr" />
        <input type="hidden" id="promoteType" name="promoteType" value="1" />
            <div class="hn-form">
                <ul>
                    <li class="w213"><label class="input_label">
                        <input id="id_startCity" name="cityName" type="text" class="w199 greytxt MGcity" value="请选择城市" datatype="hotel" /><input type="hidden" id="cityCode" name="cityCode" value="" />
                        <span id="scitytip" class="hidetxt" style="display:none;">选择城市</span><a name="cityIcon" id="scityIcon" href="#" class="cityIcon"></a>
                    </label></li>
                    <li class="w213"><label class="input_label">
                        <input id="id_startDate" name="inDate" type="text" class="w199 required greytxt calendar" value="入住日期" readonly="true" />
                        <span id="sdatetip" class="hidetxt" style="display:none;">入住</span><span class="holitip"></span><a name="icon_startDate" href="#" id="icon_startDate" class="dateIcon"></a>
                    </label></li>
                    <li class="w280">
                        <label class="input_label">
                           <input id="id_backDate" name="outDate" type="text" class="w199 required greytxt calendar" value="离店日期"  readonly="true" />
                           <span id="edatetip" class="hidetxt" style="display:none;">离店</span><span class="holitip"></span><a name="icon_outDate" href="#" id="icon_backDate" class="dateIcon"></a>
                        </label>
                        
                        <label class="input_label mg0508"><input name="" id="daysOfStay" type="text" class="w20 greytxt" value="1" readonly="true" /></label> <span>晚</span>
                    </li>

                    <li class="w213"><label class="select_label">
                        <input id="priceStr_input" name="priceStr_input" type="text" class="w176 greytxt" value="不限" readonly="true" /><span id="pricetip" class="hidetxt">价格范围</span>
                    </label></li>
                    <li class="w213"><label class="input_label">
                        <input name="hotelName" id ="hotelName" type="text" value="您可以输入酒店名称查询" class="w199 greytxt"  onblur="showMes(this.id);" onclick="hiddenMes(this.id);" />
                    </label></li>
                    <li class="w280"><label class="input_label">
                        <input name="geoName" id="geoName" type="text" class="w272 greytxt" value="您可以输入酒店地址或位置模糊查询" onblur="showMes(this.id);" onclick="hiddenMes(this.id);"/></label></li>
                        <!-- 
                        <input type="hidden" name="bizCode" id="bizCode" />
                        <input type="hidden" name="bizFlag" id="bizFlag" />
                         -->
                </ul>
                <div class="cboxWrap">
                    <span>酒店星级：</span>
                    <input name="hotelStarStr" type="checkbox" value="19,29," class="iradio" /><label>5星/豪华型</label>
                    <input name="hotelStarStr" type="checkbox" value="39,49," class="iradio" /><label>4星/高档型</label>
                    <input name="hotelStarStr" type="checkbox" value="59,64," class="iradio" /><label>3星/舒适型</label>
                    <input name="hotelStarStr" type="checkbox" value="66,69," class="iradio" /><label>2星/经济型</label>
                    <input name="hotelStarStr" type="checkbox" value="79," class="iradio" /><label>2星级以下/公寓</label>
                    <input type="hidden" id="hotelStar" name="hotelStar" value="" />
                </div>
                
                <div id="queryDiv" class="btnWrap"> 
	               <a href="javascript:void(0);" name="queryHotelInMap" id="map_query" class="green">在地图上查找酒店&gt;&gt;</a>
	               <a href="http://www.mangocity.com/assistant/hotel.shtml" class="green hotel_roll">酒店团房申请>></a>
	               <a href="javascript:void(0);" name="queryHotel" id="id_query" class="btn135x32a">查询酒店</a> 
	               <label style="width:140px"></label>
	               <a class="green" style="font-size:18px;font-weight:bold;width:140px;float:right"
	               href="javascript:void(0)" onclick="javascript:window.open('http://hotel.mangocity.com/hk.shtml')">特惠港澳酒店>></a>
                </div>
                <div id="waitQuery" class="hn-waiting" style="display:none"><img src="http://himg.mangocity.com/img/h/2011/waiting.gif" alt="" /></div>
            </div>
        </form>
        </div>
        <%--==E search box--%>
        <div id="searchBanner" class="banner740x80" style="display:none"></div>
        <%--==S hot hotel--%>
        <div class="hothotelBar">
			<h2>
				热点区域周边酒店
			</h2>
			<div id="hothotelNav" class="hothotelNav">
				<div id="hothotelNav" class="hothotelNav">
				<h3><a href="javascript:void(0)" onclick="switchCity('PEK',this);" name="beijingHot" class="on" >北京</a></h3>
				<h3><a href="javascript:void(0)" onclick="switchCity('SHA',this);" name="shanghaiHot" >上海</a></h3>
				<h3><a href="javascript:void(0)" onclick="switchCity('CAN',this);" name="guangzhouHot" >广州</a></h3>
				<h3><a href="javascript:void(0)" onclick="switchCity('SZX',this);" name="shenzhunHot" >深圳</a></h3>
				<h3><a href="javascript:void(0)" onclick="switchCity('HKG',this);" name="xiangguangHot" >香港</a></h3>
				<h3><a href="javascript:void(0)" onclick="switchCity('MAC',this);" name="aomenHot" >澳门</a></h3>
				<h3><a href="javascript:void(0)" onclick="switchCity('SYX',this);" name="sanyaHot" >三亚</a></h3>
				<h3><a href="javascript:void(0)" onclick="switchCity('HGH',this);" name="zhengzhouHot" >杭州</a></h3>
				<h3><a href="javascript:void(0)" onclick="switchCity('NKG',this);" name="nanjingHot" >南京</a></h3>
				<h3><a href="javascript:void(0)" onclick="switchCity('CTU',this);" name="chengdouHot" >成都</a></h3>
			</div>
			</div>
		</div>
		<div id="hothotelPanel" class="hothotelPanel" >
		        <%@include file="/WEB-INF/jsp/hotellayout/hotcities.jsp" %>
		</div>
        
        <%--==E hot hotel--%>
        <%--==S hotel ad--%>
        <div class="mg_newBanner mgt10 mgb10">
           <div class="firstBnner"><img src="http://himg.mangocity.com/img/h/2011/120911_emp2.jpg" /></div>
        </div>
        <%--==S hotel ad--%> 
        
    </div>
    <div class="hn-sidebar">
    	 <%--==S 百分点推荐栏--%>
         <div id="bfd_rec_box"></div>
         <%--==E 百分点推荐栏--%>
         
    	
         <%--==S want review--%>
       <div class="myreviews mgb10"><a href="javascript:void(0);" onclick="doFuDong();" name="commendHotel" >我要点评酒店</a></div>

        <%--==E want review--%>
        
        <%--==S passed hotel--%>
        <div id="memberCheckInList"></div>
        <%--==E  passed hotel--%>
        <%--==S why choice--%>
        <%@include file="/WEB-INF/jsp/commonjsp/staticjsp/whychoiceus.jsp" %>
        <%--==E  why choice--%>
        
        <%--==S 到到网--%>
        <%@include file="/WEB-INF/jsp/commonjsp/staticjsp/daodao.jsp" %>
        <%--==E 到到网--%>
       
       
        
    </div>
</div>
<%--=E main --%>

<%--=S hot city --%>

<%@include  file="/WEB-INF/jsp/commonjsp/staticjsp/morehotcities.jsp" %>
<%--=E hot city --%>

<%--=S select list--%>

<div id="priceSelect" class="mgSelect" style="width:203px;">
   <div class="mgsltit">可使用键盘&uarr;&darr;选择</div>
   <div id="pricelist" class="mgcitylist">
       <ul>
           <li><a href="#" title="" class="current" name="priceBuxian" >不限</a></li>
           <li><a href="#" title="" name="price200" >200元以下</a></li>
           <li><a href="#" name="price200-500" >200-500元</a></li>
           <li><a href="#" name="price500-800" >500-800元</a></li>
           <li><a href="#" name="price800" >800元以上</a></li>
       </ul>
   </div>
   
</div>
<%--=E select list--%>

<%--=S footer--%>
<jsp:include page="../commonjsp/_footer.jsp" flush="true" />
<%--=E footer --%>
<script type="text/javascript" src="http://wres.mangocity.com/js/home/cloud/js/city.hotel.js"></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/w/build/mgtool.js" ></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/w/module/cityselecter.js" ></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/w/module/datepicker.js" ></script>
<%--<script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/hotel2011.js"></script>--%>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/h_index_2012.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2012/hotel2012.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2012/justforhotelcity.js"></script>
<!-- 酒店企业QQ -->
<script src="http://wres.mangocity.com/js/w/module/mango_online.js"></script>
<script type="text/javascript">

$(function(){
    $('#mangoServerOnline').mangoOnline({
			  top : 205,
			  setclass : 'on_flight',
			  url : 'http://b.qq.com/webc.htm?new=0&sid=800016693&o=www.mangocity.com&q=7',
			  week : '周一至周日',
			  time : '09:00-18:00'
	});
});

</script>

<div class="menberlogin" id="id_fudong" style='display:none'>
    <div class="dlu" id="testmember">
    	<iframe src="http://www.mangocity.com/mbrweb/login/loginDirectBook.action?from=directBook&projectCode=null" name="framename" width="100%" marginwidth="0" height="100%" marginheight="0" scrolling="no" frameborder="no" id="framename" border="0"></iframe>
    </div>
</div>
<div class="shangquan"  id="selectbiz" style="display:none;"></div>
<%--start-酒店VRM（访客关系管理）二次营销 --%>
<s:if test='${application.OPEN_VRM} ==true'> 
<iframe src="http://serv1.vizury.com/analyze/analyze.php?account_id=VIZVRM128&param=h100&section=1&level=1" scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>
</s:if>
<%--end-酒店VRM（访客关系管理）二次营销 --%>

<%--start-百分比--%>
<input type="hidden" id="cashbackrate" name="cashbackrate" value="${resultMap.cashbackrate}"/>
<s:if test='${application.OPEN_BFD} ==true'> 
<script type="text/javascript" src="js/baifendian/ordersource.js"></script>
<script type="text/javascript">
var userId=getCookie("userid")||getCookie("memberid");
var cashbackrate =$("#cashbackrate").val();
/**if (navigator.userAgent.indexOf('Firefox') >= 0){
  console.log("userId:"+userId);
  }**/
window["_BFD"] = window["_BFD"] || {};
_BFD.BFD_INFO = {
		user_id : userId, 
		cashbackrate:cashbackrate,
		client : "Ctest_mg"  			//百分点技术人员使用的帐号，请您不要修改这句代码！
};
_BFD.script = document.createElement("script");
_BFD.script.type = 'text/javascript';
_BFD.script.async = true;
_BFD.script.charset = 'utf-8';
_BFD.script.src = (('https:' == document.location.protocol?'https://ssl-static1':'http://static1')+'.baifendian.com/service/mangguo/mg_default_new.js');
document.getElementsByTagName("head")[0].appendChild(_BFD.script);

function getCookie(name){ 
var strCookie=document.cookie; 
var arrCookie=strCookie.split("; "); 
for(var i=0;i<arrCookie.length;i++){ 
var arr=arrCookie[i].split("="); 
if(arr[0]==name)return arr[1]; 
} 
return ""; 
} 
</script>
</s:if>
<%--end-百分比--%>

  <%-- 这个放到最后，有百度监控，会影响速度--%>
<jsp:include page="../commonjsp/ _footer_monitor.jsp" flush="true" />
</body>

</html>

