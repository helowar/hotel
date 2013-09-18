<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${cityName}${distinctValue}${bizValue}${hotelGroupName}${geoName}酒店-${cityName}${distinctValue}${bizValue}${hotelGroupName}${geoName}酒店预订-${cityName}宾馆查询预订-芒果网</title>
<meta name="keywords" content="${cityName}${distinctValue}${bizValue}${hotelGroupName}${geoName}酒店,${cityName}${distinctValue}${bizValue}${hotelGroupName}${geoName}酒店预订,
${cityName}${distinctValue}${bizValue}${hotelGroupName}${geoName}查询,
${cityName}${distinctValue}${bizValue}${hotelGroupName}${geoName}宾馆,${cityName}${distinctValue}${bizValue}${hotelGroupName}${geoName}宾馆预订"/>
<meta name="description" content="${cityName}${distinctValue}${bizValue}${hotelGroupName}${geoName}酒店:芒果网${cityName}${distinctValue}${bizValue}${hotelGroupName}${geoName}酒店预订
及${cityName}${distinctValue}${bizValue}${hotelGroupName}${geoName}宾馆查询预定服务，为您提供详细的${cityName}${distinctValue}${bizValue}${hotelGroupName}${geoName}酒店，
${cityName}${distinctValue}${bizValue}${hotelGroupName}${geoName}宾馆的详细介绍点评、价格．预订电话 40066-40066"/>
<link rel="stylesheet" type="text/css" href="http://hres.mangocity.com/css/h/2011/hotel2011.css" />
<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/w/module/datepicker.css" />
<link rel="stylesheet" type="text/css" href="css/hotelsearch/jquery.autocomplete.css" />
<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/w/module/mango_online.css" />
<link rel="stylesheet" href="http://wres.mangocity.com/css/w/module/cityselecter.css" type="text/css" />
<script type="text/javascript" src="http://wres.mangocity.com/js/home/cloud/js/jquery-1.3.2.js"></script>
<script type="text/javascript" src="js/hotelsearchJS/jquery.autocomplete.js"></script>
<style type="text/css">
.fixed{ display:none; width:21px; height:79px; background:#ddd; position:fixed; right:200px; top:75%; margin-top:-25px;}
.topmiddle{   color: #FFFFFF;
    height: 60px;
    padding-top: 10px;
    text-align: center;
    width: 21px;
    background:#FE9601;}
    .topmiddle a{color:#fff;}
    .topmiddle a:hover{color:#008800;}
.reqtab_new label{
float:none
}
</style>
</head>

<body>
<%
String projectCode;
projectCode = request.getParameter("projectcode");
if (projectCode != null && !projectCode.equals("")) {	
	Cookie cookieProject = new Cookie("projectcode",projectCode);
	cookieProject.setPath("/");
	response.addCookie(cookieProject);
}
%>
<a id="a1" name="a1"></a>
<form name="hotelBookForm" id="hotelBookForm" method="post" action="hotel-booking.shtml">
    <input type="hidden" name="priceTypeId" id="book_priceTypeId" value="" />
    <input type="hidden" name="payMethod" id="book_payMethod" value="" />
    <input type="hidden" name="inDate" id="book_inDate" value="" />
    <input type="hidden" name="outDate" id="book_outDate" value="" />
</form>

<%-- 从上个界面传过来的参数 --%>
<input type ="hidden" id="f_bizCode" value="${bizCode}" />
<input type ="hidden" id="f_bizValue" value="${bizValue}" />
<input type ="hidden" id="f_distinctCode" value="${distinctCode}" />
<input type ="hidden" id="f_distinctValue" value="${distinctValue}" />
<input type ="hidden" id="f_priceStr" value="${priceStr}" />
<input type ="hidden" id="f_hotelStar" value="${hotelStar}" />
<input type ="hidden" id="f_geoId" value="${geoId}" />
<input type ="hidden" id="f_geoName" value="${geoName}" />
<input type ="hidden" id="f_hotelGroupId" value="${hotelGroupId}" />
<input type ="hidden" id="f_hotelGroupName" value="${hotelGroupName}" />
<input type ="hidden" id="f_hotelIdsStr" value="${hotelIdsStr}" />
<input type ="hidden" id="f_hotelCount" value="${hotelCount}" />
<input type ="hidden" id="f_promoteType" value="${promoteType}" />
<input type ="hidden" id="f_payMethod" value="${payMethod}" />

<%-- 渠道编号获取 --%>
<input type ="hidden" id="projectCode" value="${projectcode}" />

<div id="querytransport"></div>

<c:choose>
  	 <c:when test="${label == 'broad'}">
  		<jsp:include page="../commonjsp/_headerHK.jsp" flush="true" />
  	 </c:when>
  	 <c:otherwise>
  	 	<jsp:include page="../commonjsp/_header.jsp" flush="true" />
  	 </c:otherwise>
  </c:choose>
<%--=S top bar --%>
<div class="w960 hn-topbar">
    <div class="status step2">
        <ul>
            <li>查询</li>
            <li  class="choice">选择</li>
            <li>预订</li>
            <li>完成</li>
        </ul>
    </div>
    <div class="crumbs">您现在的位置：<a href="http://www.mangocity.com" class="green" name="firstPage" >首页</a> &gt; <a href="hotel-search.shtml" class="green" name="domesticHotel" >国内酒店</a> &gt; <em id="gwt_cityName">${cityName}</em></div>
</div>
<%--=E top bar--%>
<div class="w960 cFloat">
   <div class="hn-main">
   <c:choose>
   <c:when test="${fagHasSearchHotel==true}">
      <div id="requeryTip" class="hotel_prompt" style="display:${display_helper}"><p>很抱歉，没有找到符合您要求的酒店，建议您重新修改酒店的搜索条件（例如：调整筛选条件，看看输入的文字是否有误、是否有空格等）。您也可以看看下面列表中芒果网推荐的酒店是否合适？</p></div>
		<script type="text/javascript">
		setrequeryTip();
		 function setrequeryTip(){
		 	
		 	var fagHasSearchHotel=${fagHasSearchHotel};
		 	if(fagHasSearchHotel){
		 		jQuery("#requeryTip").show();
		 	}
		 	
		 }
		</script>
      </c:when>
      <c:otherwise>
      <div id="requeryTip" class="hotel_prompt" style="display:${display_helper}"><p>很抱歉，没有找到符合您要求的酒店，建议您重新修改酒店的搜索条件（例如：调整筛选条件，看看输入的文字是否有误、是否有空格等）。</p></div>
      </c:otherwise>
    </c:choose>  
      <div class="requery">
            <div class="requerytit" id="hotelCount"></div>
            <div class="hn-form">
                <ul>
                    <li class="w158"><label class="input_label"><input id="id_startCity" name="cityName" type="text" class="w144 greytxt MGcity" value="${cityName}" datatype="hotel" /><input type="hidden" id="cityCode" name="cityCode" value="${cityCode}" /><span id="scitytip" class="hidetxt" style="display:none;">选择城市</span><a id="scityIcon" href="javascript:void(0);" class="cityIcon"></a></label></li>
                    <li class="w158"><label class="input_label"><input id="id_startDate" name="inDate" type="text" class="w144 required greytxt calendar" value="${inDate}" readonly="true" /><span id="sdatetip" class="hidetxt" style="display:none;">入住</span><span class="holitip"></span><a href="#" class="dateIcon"></a></label>
                    </li>
                    <li class="w158"><label class="input_label"><input id="id_backDate" name="outDate" type="text" class="w144 required greytxt calendar" value="${outDate}" readonly="true" /><span id="edatetip" class="hidetxt" style="display:none;">离店</span><span class="holitip"></span><a href="#" class="dateIcon"></a></label>
                     </li>
                    <li class="w150"><label class="input_label"><input id="hotelName" name="hotelName" type="text" class="w134 greytxt" value="${hotelName}" onblur='showMes(this.id,"您可以输入酒店名称查询");' onclick='hiddenMes(this.id,"您可以输入酒店名称查询")'; /></label></li>
                    <li class="w92"><div id="hotelSearch"></div></li>
                </ul>
            </div>
            <div id="selectedConditions" class="selectedConditions">
                <dl class="">
                  <dt>所选条件:</dt>
                  <dd><div id="hotel_search_selectedItems"></div></dd>
                </dl>
            </div>
            <div id="conditions" class="conditions">
                <dl class="odd">
                    <dt>酒店价格:</dt>
                    <dd><div id="hotel_search_hotelprice"></div></dd> 
                </dl>
                <dl>
                    <dt>酒店品牌:</dt>
                    <dd><div id="hotel_search_hotelband"></div></dd> 
                </dl>
                <dl class="odd">
                   <dt>酒店星级:</dt>
                   <dd><div id="hotel_search_hotelstar" ></div></dd>        
                </dl>
                
                <dl>
                    <dt>酒店设施:</dt>
                    <dd><div id="hotel_search_hotelfacility"></div></dd>                     
                </dl>           
			    <dl class="odd">
                  <dt>地理位置:</dt>
                  <dd><div id="hotel_search_location"></div></dd>
                </dl>
           </div>
            <div class="reqbot"></div>
        </div>
        <a id="pushpull" class="contract pull" href="javascript:void(0);" name="conditionShowOrHide" >展开筛选条件</a>      
      
      <div id="showTime"></div>
      <div id="showTimeAll"></div>
      
      <div id="waitImg" class="waitImg" style="display:none"><img src="http://himg.mangocity.com/img/h/2011/querywaiting.gif" alt="请稍等，正在查询..."/></div>
      <%-- hotelList --%>
      <div class="requeryList" id="hotelAll">
          <input  type="hidden" value="地图展示酒店" /><%--用于搜索引擎优化 --%>
          <div class="reqtab" id="topPage">                   
          </div>
          <div class="reqtab_new">
            <div id="hotelSortDiv"></div>         
           </div>
           
          <div class="queryWrap" id="hotelList">${hotelListOut}</div>
          <div id="bottomPage"></div>
         
      </div>
   </div>
    <div class="hn-sidebar" id="rightDiv">
    	 <%--==S passed hotel--%>
        <div class="hn-boxtop"><h2>最近浏览过的酒店</h2></div>
        <div class="hn-boxmid" id="hotelAccess">
        </div>
        <div class="hn-boxbot mgb10"></div>
        <%--==E  passed hotel--%>
        
         <%--==S 百分点推荐栏--%>
         <div id="bfd_rec_box"></div>
         <%--==E 百分点推荐栏--%>
         
         
        <%--==S hotel ad--%>
        <div id="hotelBanner">
        
        </div>
        <%--==S hotel ad--%>
        <%--==S why choice--%>
        <div class="hn-boxtop whychoice"><h2>为什么选择我们?</h2></div>
        <div class="hn-boxmid">
            <ul class="choiceul">
                <li class="pay">
                    <h3 class="no_1">港中旅集团品牌保证</h3>
                </li>
                <li class="pay">
                    <h3 class="no_2">数量丰富的酒店选择</h3>
                </li>
                <li class="pay">
                    <h3 class="no_3">专业优质的服务团队</h3>
                </li>
                <li class="pay">
                    <h3 class="no_4">出行无忧的服务承诺</h3>
                </li>
                <li class="pay">
                    <h3 class="no_5">高达20% 的现金返还</h3>
                </li>
            </ul>
        </div>
        <div class="hn-boxbot mgb10"></div>
        <%--==E  why choice--%>
         
    </div>
</div>
    

<%--=S footer--%>
<jsp:include page="../commonjsp/_footer.jsp" flush="true" />
<%--=E footer --%>
<%-- 会员信息start --%>
<input type="hidden" name="memberPath" id="memberPath" value="${memberPath}" />
 <div class="hotellogin" id="id_fudong" style='display:none'>
    <div class="dlu" id="testmember">
    </div>
 </div>
<%-- 会员信息end --%> 

<!--=S comparebox-->
<div id="comparebox" class="comparebox">
   <div class="cptit"><span id="closecopare" class="close"></span><span class="cpnote">已有<strong id="hotelNum">0</strong>家酒店</span><h3>酒店对比栏</h3></div>
   <div id="compareAlart" class="queryBoxcompare" style="display:none" >请选择2到3家进行对比</div>
   <div class="compareList">
       <ol id="hotelName_compare">
       </ol>
   </div>
   <p class="tocare">最多只能对比3家酒店</p>
   <p class="comparebtn"><a id="startcompare" href="javascript:void(0);" onclick="startCompareHotel();" class="btn92x26">开始对比</a></p>
</div>
<!--=E comparebox-->

<%--start 对比酒店的参数 --%>
<form id="hotelCompareForm" name="hotelCompareForm" method="post" action="hotel-compare.shtml" target="_blank">
    <input type="hidden" name="comparedHotelIdsStr" id="comparedHotelIdsStr" value="" />
    <input type="hidden" name="compareInDate" id="compareInDate" value="${inDate}" /> 
    <input type="hidden" name="compareOutDate" id="compareOutDate" value="${outDate}" />
    <input type="hidden" name="compareCityCode" id="compareCityCode" value="" />
</form>
<%--end 对比酒店的参数 --%>

<div id="id_Emap" style='display:none'>
    <div class="dlu" id="loadMap">
    </div>
</div>
  <%-- topbutton start --%>
<div id="topfixed" class="fixed" > 
	 <a href="#a1" name="goTop1" ><div class="topmiddle">返回顶部</div></a>
	 <div class="bottom3"><a href="#a1" name="goTop2" ><img src="http://wimg.mangocity.com/img/promotion/gangaoyou/fanhui_top.jpg" border="0" /></a></div>
</div>
	<%-- topbutton end --%>
		
        
<script type="text/javascript" src="http://wres.mangocity.com/js/home/cloud/js/city.hotel.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/hotelquery.js"></script>
<script src="http://wres.mangocity.com/js/w/build/mgtool.js" type="text/javascript"></script>
<script src="http://wres.mangocity.com/js/w/module/cityselecter.js" type="text/javascript"></script>
<script src="http://wres.mangocity.com/js/w/module/datepicker.js" type="text/javascript"></script>
<%--<script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/hotel2011.js"></script>--%>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/h_index_2012.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2012/hotel2012.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2012/justforhotelcity.js"></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/h/banner/HotelHomeBanner.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/mangocity.simplemodal.js"></script>
<script type="text/javascript" src="js/hotellist/hotellist.js"></script>
<script type="text/javascript" src="js/jQuery/jquery.alert.js"></script>
<script type="text/javascript" src="js/hotelinfo/hotelinfo.js"></script>
<script type="text/javascript" language="javascript" src="hotelGWT/hotelGWT.nocache.js"></script>
<!-- 酒店企业QQ -->
<script src="http://wres.mangocity.com/js/w/module/mango_online.js"></script>
<script type="text/javascript">
$(function(){
      //默认展开
     jQuery("#pushpull").click();
     
    $('#mangoServerOnline').mangoOnline({
			  top : 205,
			  setclass : 'on_flight',
			  url : 'http://b.qq.com/webc.htm?new=0&sid=800016693&o=www.mangocity.com&q=7',
			  week : '周一至周日',
			  time : '09:00-18:00'
	});
});
</script>

<script type="text/javascript">
//更多的函数
function formSubmit(){
   jQuery("button[id='gwt_reSearch']").click();
}
//topbutton js 
function scro(){
	var scrHight= document.documentElement.scrollTop;
	var left_value = jQuery("#rightDiv").offset().left-7;
   jQuery("#topfixed").css({left:left_value});
   var heightToshow = 500;
	if(scrHight>heightToshow){
		document.getElementById('topfixed').style.display="block";
	}
	if(scrHight<heightToshow){
		document.getElementById('topfixed').style.display="none";
	}
}
window.onscroll = scro;
</script>
<%-- S在线客服代码--%>
<%-- E在线客服代码--%>
<%--start-酒店VRM（访客关系管理）二次营销 --%>
<s:if test='${application.OPEN_VRM} ==true'> 
<script type="text/javascript">
$("#hotelSearch").click(function()
	     { 
	        $("#vizury").remove(); 
	        var inDate=$("#id_startDate").val();
	        var outDate= $("#id_backDate").val();
	        var cityCode =$("#cityCode").val();
	          
	          $.getJSON("http://serv1.vizury.com/analyze/analyze.php?callback=?&account_id=VIZVRM128&param=h200&city="+cityCode+"&indate="+inDate+"&outdate="+outDate+"&rooms=&ad=&ch=&section=1&level=1",
	          function(data){}); 
	        });
</script>
<iframe id="vizury" src="http://serv1.vizury.com/analyze/analyze.php?account_id=VIZVRM128&param=h200&city=${cityCode }&indate=${inDate }&outdate=${outDate}&rooms=&ad=&ch=&section=1&level=1" scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>
</s:if>
<%--end-酒店VRM（访客关系管理）二次营销 --%>

<%--start-百分比--%>
<input type="hidden" name="cashbackrate" id="cashbackrate" value="${cashbackrate}" /> 

<s:if test='${application.OPEN_BFD} ==true'> 
<script type="text/javascript" src="js/baifendian/ordersource.js"></script>
<script type="text/javascript">
function getCookie(name){ 
var strCookie=document.cookie; 
var arrCookie=strCookie.split("; "); 
for(var i=0;i<arrCookie.length;i++){ 
var arr=arrCookie[i].split("="); 
if(arr[0]==name)return arr[1]; 
} 
return ""; 
} 
function createBFD(){
var userId=getCookie("userid")||getCookie("memberid")||"";
var cashbackrate =$("#cashbackrate").val();
var cityCode =$("#cityCode").val();
var cityName=$("#gwt_cityName").html();
var parentCategory =$("a[name='domesticHotel']").html();
/**if (navigator.userAgent.indexOf('Firefox') >= 0){
  console.log("cityCode:"+cityCode+"\ncityName:"+cityName+"\nparentCategory:"+parentCategory+"\nuserId:"+userId);
}**/
window["_BFD"] = window["_BFD"] || {};
_BFD.BFD_INFO = {
		cat_id:cityCode,//当前类别对应的唯一标识id号，string类型；
		cat_name:cityName+"酒店",//当前类别名称；
		parent_category:parentCategory,  //当前类别的父级类别名称；
		user_id : userId,
		cashbackrate:cashbackrate,
		client : "Ctest_mg"  			//百分点技术人员使用的帐号，请您不要修改这句代码！
	};
_BFD.script = document.createElement("script");
_BFD.script.type = 'text/javascript';
_BFD.script.async = true;
_BFD.script.id = 'bdf_script';
_BFD.script.charset = 'utf-8';
_BFD.script.src = (('https:' == document.location.protocol?'https://ssl-static1':'http://static1')+'.baifendian.com/service/mangguo/mg_list_new.js');
document.getElementsByTagName("head")[0].appendChild(_BFD.script);
}
createBFD();
$("#hotelSearch").click(function(){ 
          $("#bdf_script").remove();
           createBFD();	        
	          	        });
</script>
</s:if>
<%--end-百分比--%>

 <%-- 这个放到最后，有百度监控，会影响速度--%>
<jsp:include page="../commonjsp/ _footer_monitor.jsp" flush="true" />
</body>
</html>
