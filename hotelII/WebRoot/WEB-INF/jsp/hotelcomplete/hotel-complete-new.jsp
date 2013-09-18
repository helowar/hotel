<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="fmt"%>
<%@ taglib prefix="fn" uri="fn"%>
<%@ taglib prefix="c" uri="core"%>
<%@ taglib prefix="h" uri="convert"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="f" uri="functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="酒店查询,酒店,酒店预订,订商务酒店,快捷酒店,星级酒店." />
<meta name="description" content="【酒店预订】芒果网为您的出行提供方便的酒店预订服务，订商务酒店、快捷酒店、星级酒店当然选芒果网，国内酒店查询预订全国免费预订电话 40066-40066 ." />
<title>【酒店预订】酒店查询 订国内商务酒店、快捷酒店、星级酒店首选芒果网！</title>

<link rel="stylesheet" type="text/css" href="http://hres.mangocity.com/css/h/2011/hotel2011.css" />
<link href="http://wres.mangocity.com/css/home/2011/head2011.css" type="text/css" rel="stylesheet"  />
<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/home/module/datepicker.css" />
<script type="text/javascript" src="http://wres.mangocity.com/js/home/cloud/js/jquery-1.3.2.js"></script>

</head>
<script type="text/javascript">
jQuery(document).ready(function(){
	var channeltype = "${hotelOrderFromBean.roomChannel}";
	var beds = "${hotelOrderFromBean.bedTypeStr}";
	if(channeltype!=null && channeltype=="9" && beds!=null && beds.indexOf(",")>-1){
		jQuery("#bedAlert").append("&nbsp;&nbsp;此房间床型尽量安排，具体床型以到店后酒店安排为准！");
	}
});
</script>
<body>
<jsp:include page="../commonjsp/_header.jsp" flush="true" />

<!--=S top bar -->
<div class="w960 hn-topbar">
    <div class="status step4">
        <ul>
            <li>查询</li>
            <li>选择</li>
            <li>预订</li>
            <li  class="choice">完成</li>
        </ul>
    </div>
    <div class="crumbs">您现在的位置：<a href="http://www.mangocity.com" class="green" name="firstPage" >首页</a> &gt; <a href="hotel-search.shtml" class="green" name="domesticHotel" >国内酒店</a> &gt; 
           <a href="list-${fn:toLowerCase(hotelOrderFromBean.cityCode)}.html" class="green" name="daohangcity">${hotelOrderFromBean.cityName}酒店</a>
           &gt; <a href='jiudian-${fn:toLowerCase(hotelOrderFromBean.hotelId)}.html' class="green" name="daohangHotel">${hotelOrderFromBean.hotelName}</a> &gt; <em>订单完成</em>
    </div>
</div>
<!--=E top bar-->

<div class="w960">
   <div class="htoder_top"></div>
   <!--=S 订单完成 -->
   <div class="htorder_left">
   <c:if test="${autoFlag == 'true'}">
      <div class="htorder_qd_tip mgb10">
   	<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="13%" rowspan="2"><span class="bold font16">温馨提示：</span></td>
    <td width="87%">尊敬的客户，我们已经为您注册成为芒果网会员，会员号码为：<span class="bold red">${memberCd}</span>，密码将以短信的形式发送</td>
  </tr>
  <tr>
    <td>至您的预订联系人手机上。为保证您的账户安全，请尽快【<a class="bold gread" href="javascript:newWindow('http://www.mangocity.com/mbrweb/login/doLogin.action')">修改密码&gt;&gt;</a>】。</td>
  </tr>
</table>

   </div>
</c:if>
     <div class="htod_fin_text">
       <div class="successbar">
          <p style="padding-top:10px;"><strong>您的订单已提交，我们将尽快处理您的预定。请记录您的订单号：<span class="orange">${orderCD}</span>&nbsp;以便查询。</strong></p>
       </div>
       <dl>
         <dt><strong class="orange">订单信息</strong></dt>
         <dd>酒店名称：${hotelOrderFromBean.hotelName}</dd>
         <dd>入住房型：${hotelOrderFromBean.roomTypeName}<span class="grey">（床型：<h:convert name="bedType" value="${hotelOrderFromBean.bedType}"></h:convert><span id="bedAlert" style="color:red"></span>）</span>&nbsp;共<strong class="orange">${hotelOrderFromBean.roomQuantity}</strong>间</dd>
         
         <dd>酒店地址：<c:if test="${ not empty hotelBasicInfo}">${hotelBasicInfo.zoneName}${hotelBasicInfo.chnAddress}</c:if><c:if test="${empty hotelBasicInfo}">${hotelAddress}</c:if> </dd>
         <dd>酒店电话：<span class="green">${hotelBasicInfo.telephone}</span></dd>
         <dd>入住日期：<fmt:formatDate value="${hotelOrderFromBean.checkinDate}" pattern="yyyy-MM-dd"/>(${weekOfInDate})</dd>
         <dd>退房日期：<fmt:formatDate value="${hotelOrderFromBean.checkoutDate}" pattern="yyyy-MM-dd"/>(${weekOfOutDate})&nbsp;共<strong class="orange">${hotelOrderFromBean.days}</strong>晚</dd>
         <dd>房间留至：<c:if test="${hotelOrderFromBean.latestArrivalTime == '23:59'}">24:00</c:if><c:if test="${hotelOrderFromBean.latestArrivalTime != '23:59'}">${hotelOrderFromBean.latestArrivalTime}</c:if>
         </dd>
         <dd>入 住 人：${linkeManStr}</dd>
         <dd>联 系 人：${hotelOrderFromBean.linkMan}</dd>
         <dd>联系手机：${hotelOrderFromBean.mobile}<span class="grey">（预订确认后将发送短信至此号码）</span></dd>
         <c:if test="not empty ${hotelOrderFromBean.email }">
         <dd>&nbsp;&nbsp;E-mail：${hotelOrderFromBean.email }</dd>
         </c:if>
         <dd><strong>房费合计：
         <c:if test="${hotelOrderFromBean.payMethod=='pay' and  false==hotelOrderFromBean.payToPrepay}">
         <font class="orange font16" style="font-family:Arial, Helvetica, sans-serif;">${hotelOrderFromBean.currencyStr}</font><font class="orange font16" style="font-family:Arial, Helvetica, sans-serif;" id="roomPriceSum">${hotelOrderFromBean.priceNum * hotelOrderFromBean.roomQuantity}</font>
        </strong>
         <span class="orange">（到店付款）</span></dd>      
         </c:if>      
            <c:if test="${hotelOrderFromBean.payMethod=='pre_pay' || hotelOrderFromBean.payToPrepay}">
               					  <font class="orange font16" style="font-family:Arial, Helvetica, sans-serif;">&yen;</font><font class="orange font16" style="font-family:Arial, Helvetica, sans-serif;" id="roomPriceSum">${hotelOrderFromBean.priceNum * hotelOrderFromBean.roomQuantity * rate}</font></strong><span class="orange">(网上预付)</span></dd>  
               					      
               					
               				 <c:if test="${hotelOrderFromBean.useUlmPoint}">
		        				<dd><strong style="font-family:Arial, Helvetica, sans-serif;">积分抵用：
		        				 <font class="orange font16" >&yen;</font><font id="ulmPointAll" class="orange font16"/><c:out value="${hotelOrderFromBean.ulmPoint/100}"></c:out></font></strong></dd>
		       				 </c:if>
		        			<c:if test="${hotelOrderFromBean.usedCoupon}">
		        					<dd> <strong style="font-family:Arial, Helvetica, sans-serif;">代金券抵用：
		        					<font class="orange font16">&yen;</font><font id="ulmCouponAll" class="orange font16"><c:out value="${hotelOrderFromBean.ulmCoupon}"></c:out></font></strong></dd>
		       				 </c:if>
		         		    <c:if test="${hotelOrderFromBean.usedCoupon or hotelOrderFromBean.useUlmPoint}">
		                              <dd><strong style="font-family:Arial, Helvetica, sans-serif;">实际支付金额：
		                              <font class="orange font16">&yen;</font><font id="acturalAllprice" class="orange font16">${hotelOrderFromBean.acturalAmount}</font></strong></dd>
		         			</c:if>
            </c:if>
            <script type="text/javascript">
				 var o = Math.ceil(jQuery('#roomPriceSum').text());
	            document.getElementById("roomPriceSum").innerHTML = o;
	            
	            var ulmPointAll=jQuery('#ulmPointAll').text();
	            jQuery('#ulmPointAll').text(Math.floor(ulmPointAll));
	            
	            var ulmCouponAll=jQuery('#ulmCouponAll').text();
	            jQuery('#ulmCouponAll').text(Math.floor(ulmCouponAll));	            
	            var acturalAllprice=jQuery('#acturalAllprice').text();
	            jQuery('#acturalAllprice').text(Math.ceil(acturalAllprice));
	            
           </script>
           
         <c:if test="${hotelOrderFromBean.returnAmount gt 0.0}">
         <dd>
         <strong style="font-family:Arial, Helvetica, sans-serif;">返现金额：
         <font class="orange font16">&yen;</font><font class="orange font16" id="returnAllprice">${hotelOrderFromBean.returnAmount * hotelOrderFromBean.roomQuantity}</font></strong>（返现奖金将在您离店后的2-3个工作日内返还至您的
         <a href="http://www.mangocity.com/mbrweb/jsp/member/member.jsp?menuRedirect=cash" target="_blank">现金账户</a>中）
         </dd>
          <script type="text/javascript">
          var returnAllprice=jQuery('#returnAllprice').text();
          jQuery('#returnAllprice').text(Math.floor(returnAllprice));
          </script>
         </c:if>
         
       </dl>
       <c:if test="${autoFlag == 'true'}">
       <div class="htod_fin_text_else">
         <strong>您还可以：</strong><br /> <a href="#" onclick="window.print();" name="printMessage">打印预订信息</a>│
         <a href="javascript:newWindow('http://www.mangocity.com/mbrweb/login/doLogin.action')">查看订单</a>│
         <a href="javascript:newWindow('http://www.mangocity.com/mbrweb/login/doLogin.action')">取消订单</a>│
         <a href="hotel-information.shtml?hotelId=${hotelOrderFromBean.hotelId}" name="bookOtherRoom">继续预订其他房型</a>│
         <a href="hotel-query.shtml?cityCode=${hotelOrderFromBean.cityId}" name="bookOtherHotel" class="green">继续预订其他酒店</a>
       </div>
       </c:if>
        <c:if test="${autoFlag == 'false'}">
         <div class="htod_fin_text_else">
         <strong>您还可以：</strong><br /> <a href="#" onclick="window.print();" name="printMessage">打印预订信息</a>│
         <a href="javascript:newWindow('http://www.mangocity.com/mbrweb/login/doLogin.action')">查看订单</a>│
         <a href="javascript:newWindow('http://www.mangocity.com/mbrweb/login/doLogin.action')">取消订单</a>│
         <a href="hotel-information.shtml?hotelId=${hotelOrderFromBean.hotelId}" name="bookOtherRoom">继续预订其他房型</a>│
         <a href="hotel-query.shtml?cityCode=${hotelOrderFromBean.cityId}" name="bookOtherHotel" class="green">继续预订其他酒店</a>
       </div>
        </c:if>
     </div>
    
   </div>
   
   <div class="htorder_right">
     <div class="htorder_ponint">
       <h2>芒果预订服务保障！</h2>
       <p>
         <span class="htod_icon_nub01">到店有房，出行无忧</span>
         <span class="htod_icon_nub02">价格保证，三倍赔付</span>
       </p>
       <h2>温馨提示</h2>
       <ul>
         <li><label>1·</label><span>如需要修改或取消，请务必提前致电芒果网7x24小时服务热线：40066-40066</span></li>
         <li><label>2·</label><span>如果您未入住，也未提前通知取消，酒店将损失一晚房费，也将影响您在酒店预订的信用记录。</span></li>
       </ul>
     </div>
       <div class="ad_place">
     	<div id="orderBanner">          
      </div>
     </div>     
     
   </div>
  
   <!--=E 订单完成 -->
</div>
<!--=E main -->
<!-- _footer.jsp页面统计是需用到gethotel_orderno.js，因此将gethotel_orderno.js调整到_footer.jsp之前加载。modify by alfred-->
<script language="javascript" src="http://wres.mangocity.com/js/w/common/js/gethotel_orderno.js"></script>  

<%--=S footer --%>
<jsp:include page="../commonjsp/_footer_complete.jsp" flush="true" />

<!--=E footer -->


<%--QC1223 芒果网网站--添加GA订单成功页跟踪代码 add by xuyiwen 2011-2-23 --%>
  <script type="text/javascript">
  
  function newWindow(url){
  window.open (url, 'newWindow', 'left=0,top=0,width='+ (screen.availWidth - 10) +',height='+ (screen.availHeight-50) +',scrollbars,resizable=yes,toolbar=no');
  }
  
  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-21338131-1']);
  _gaq.push(['_setDomainName', '.mangocity.com']);
  _gaq.push(['_addIgnoredRef', '.mangocity.com']);
  _gaq.push(['_addOrganic', 'baidu', 'word']); 
  _gaq.push(['_addOrganic', 'soso', 'w']); 
  _gaq.push(['_addOrganic', 'youdao', 'q']); 
  _gaq.push(['_addOrganic', 'sogou', 'query']);  
  _gaq.push(['_setCampaignCookieTimeout', 2592000000]); //以毫秒算，把Cookies值设置为30天
  _gaq.push(['_trackPageview']);
  
	//判断付款类型
	function judgePayType(){
		var payType = '${hotelOrderFromBean.orderPayType}';
		var _payType = null;
		if(payType == 1) _payType = '前台面付';
		if(payType == 2) _payType = '信用卡支付';
		if(payType == 3) _payType = '营业部付款';
		if(payType == 4) _payType = '网上银行支付';
		if(payType == 5) _payType = '借记卡支付';
		return _payType;
	}
	
	//判断产品类型
	function judgeProduct(){
		var currency = '${hotelOrderFromBean.currency}';
		var product = '国内酒店';
		if(currency !='RMB') product = '港澳台海外酒店';
		return product;
	}
	
	_gaq.push(['_addTrans',
	'H${orderCD}', //订单号
	judgePayType(), //使用中文表示，例如：信用卡、支付宝、现金支付等。
	'${order.sumRmb}', // 订单总金额
	'', // 税 - 选填
	'', // 运费 - 选填
	'${hotelOrderFromBean.cityName}', // 城市（中文）
	'', // 省、自治区、直辖市（中文） - 选填 
	'中国' // 国家（中文）
	]);
	

		_gaq.push(['_addItem',
		'H${orderCD}',   //订单号
		'${order.hotelId}', //酒店ID
		'${order.hotelName}', // 酒店名称 - 选填
		judgeProduct(), //其值为国内酒店、港澳台海外酒店、国内机票、国际机票、度假旅游
		'<fmt:formatNumber type="number" value="${order.sumRmb/totalRoomNight}" maxFractionDigits="0" />',//订单间夜价即产品单价
		'${totalRoomNight}' //修改为总的间夜量 
		]); 	
	_gaq.push(['_trackTrans']); 
	
	
	 (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();
	
</script>

<%--QC1231 电商--与亿起发合作的订单数据传送方法 add by xuyiwen 2011-2-24--%>
<script src="http://wres.mangocity.com/js/w/hezuo/hezuo.js" type="text/javascript"></script>
<script type="text/javascript">
	var sumRmb = ${order.sumRmb}; 
	var createDate = <fmt:formatDate value="${order.createDate}" pattern="yyyyMMddHHmmss"/>;
	var orderNo = '${order.orderCD}'; 
    hezuoxiangmuHotel(sumRmb,createDate,orderNo);
     function switchDisplay(){
	document.getElementById("tel").style.display="";
}
</script>

<script type="text/javascript" src="http://wres.mangocity.com/js/home/cloud/js/city.mango.js"></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/w/build/mgtool.js"></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/w/build/mgselecter.js"></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/w/build/datepicker.js"></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/h/banner/HotelHomeBanner.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/banner/tobanner.js"></script>
<%--start-酒店VRM（访客关系管理）二次营销 --%>
<s:if test='${application.OPEN_VRM} ==true'> 
   
    <s:if test="${hotelOrderFromBean.currency=='RMB'}">
        <s:set name="curr" value="'￥'" />   
    </s:if>
    <s:if test="${hotelOrderFromBean.currency=='HKD'}">
        <s:set name="curr" value="'HK$'" />
    </s:if>
    <s:if test="${hotelOrderFromBean.currency=='MOP'}">
        <s:set name="curr" value="'MOP'" />
    </s:if>
<iframe src="http://serv1.vizury.com/analyze/analyze.php?account_id=VIZVRM128&param=h500&oid=${order.ID}&op=${order.sum}&curr=${f:encode(curr,"UTF-8")}&hotelid=${hotelOrderFromBean.hotelId}&city=${hotelOrderFromBean.cityId}&indate=<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkinDate}"/>&outdate=<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkoutDate}"/>&rooms=${hotelOrderFromBean.roomQuantity}&ad=&ch=&section=1&level=1" scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>
</s:if>
<%--end-酒店VRM（访客关系管理）二次营销 --%>

<%--start-百分比--%>
<s:if test='${application.OPEN_BFD} ==true'> 
<input id="orderId" type="hidden" value="${orderCD}" />
<input id="rooms" type="hidden" value="${hotelOrderFromBean.roomQuantity}" />
<input id="hotelId" type="hidden" value="${hotelOrderFromBean.hotelId}" />
<input id="sum" type="hidden" value=<fmt:formatNumber value="${hotelOrderFromBean.priceNum * hotelOrderFromBean.roomQuantity * rate}" pattern="##.##"  minFractionDigits="2"/>  />
<input id="unitPrice" type="hidden" value=<fmt:formatNumber value="${hotelOrderFromBean.priceNum * rate/hotelOrderFromBean.days}" pattern="##.##"  minFractionDigits="2"/>  />
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
var userId=getCookie("userid")||getCookie("memberid");
var orderId = $("#orderId").val();
var rooms = $("#rooms").val();
var hotelId = $("#hotelId").val();
var sum = $("#sum").val();
var unitPrice = $("#unitPrice").val();
var payName = $("strong:has(#roomPriceSum)+span[class='orange']").html();
var orderItems =[hotelId,rooms,sum,unitPrice];
if(payName){
    var beginIndex = payName.indexOf("（");
    var endIndex = payName.indexOf("）");
    if(-1!=beginIndex&&-1!=endIndex){
       payName = payName.substring(beginIndex+1,endIndex);
     }
}

/**if (navigator.userAgent.indexOf('Firefox') >= 0){
console.log("orderId:"+orderId+"\norderItems:"+orderItems+"\npayName:"+payName+"\nuserId:"+userId+"\nunitPrice:"+unitPrice);
}**/
window["_BFD"] = window["_BFD"] || {};
_BFD.BFD_INFO = {
	order_id : orderId, //当前的订单id号
	order_items : orderItems,//数组类型，订单中的商品的各种信息，具体参数的含义为 ['酒店的id号',订单中该酒店数量（固定为1）,本次预订酒店应支付的总价,单价]
	order_payName:payName,//支付方式，string类型；
	user_id : userId,	//当前用户的user_id，string类型。注意：user_id不是用户的真实注册名，而是其注册名的编号,如果匿名用户为0或者为空‘’；
	client : "Ctest_mg"  //百分点技术人员使用的帐号，请您不要修改这句代码！
};
_BFD.script = document.createElement("script");
_BFD.script.type = 'text/javascript';
_BFD.script.async = true;
_BFD.script.charset = 'utf-8';
_BFD.script.src = (('https:' == document.location.protocol?'https://ssl-static1':'http://static1')+'.baifendian.com/service/mangguo/mg_order_new.js');
document.getElementsByTagName("head")[0].appendChild(_BFD.script);
</script>
</s:if>
<%--end-百分比--%>

</body>
</html>
