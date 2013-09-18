<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="fmt"%>
<%@ taglib prefix="fn" uri="fn"%>
<%@ taglib prefix="c" uri="core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="酒店查询,酒店,酒店预订,订商务酒店,快捷酒店,星级酒店." />
<meta name="description" content="【酒店预订】芒果网为您的出行提供方便的酒店预订服务，订商务酒店、快捷酒店、星级酒店当然选芒果网，国内酒店查询预订全国免费预订电话 40066-40066 ." />
<title>【酒店预订】酒店查询 订国内商务酒店、快捷酒店、星级酒店首选芒果网！</title>
<link rel="stylesheet" type="text/css" href="http://hres.mangocity.com/css/h/2011/hotel2011.css" />
<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/h/2011/hotel2011.css" />
<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/w/module/datepicker.css" />
</head>

<body>
<!--=S head-->
<jsp:include page="../commonjsp/_header.jsp" flush="true" />
<!--=E head-->

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

<!--=S main -->
<div class="w960">
    <!--=S 订单完成 -->
    <div class="orderfill">
   	  <div class="orderbar">
          <div class="successbar">
              <p><strong>您的订单已提交，我们将尽快处理您的预订。请记录您的订单号：<em>${orderCD}</em> 以便查询。</strong></p>
              <c:if test="${not empty member}">
                   <p><a href="http://www.mangocity.com/mbrweb/login/doLogin.action" name="orderManage" class="green">进入我的订单管理&gt;&gt;</a></p>
              </c:if>
          </div>
      </div>
      
      <div class="orderendinfo">
          <div class="extentbg">
         
              <div class="extentcont">            
                  <h2>订单信息</h2>
                  <ul>
                      <li>酒店名称：${hotelOrderFromBean.hotelName}</li>
                      <li>入住房型：${hotelOrderFromBean.roomTypeName} 共<strong>${hotelOrderFromBean.roomQuantity}</strong>间</li>
                      <li>酒店地址：${hotelBasicInfo.chnAddress}</li>
                      <li>入住日期：<fmt:formatDate value="${hotelOrderFromBean.checkinDate}" pattern="yyyy年MM月dd日"/></li>
                      <li>退房日期：<fmt:formatDate value="${hotelOrderFromBean.checkoutDate}" pattern="yyyy年MM月dd日"/> 共<strong>${hotelOrderFromBean.days}</strong> 晚</li>
                      <li>到店时间：${hotelOrderFromBean.arrivalTime}~${hotelOrderFromBean.latestArrivalTime}</li>
                      <li>入 住 人：${linkeManStr}</li>
                      <li>联 系 人：${hotelOrderFromBean.linkMan}</li>
                      <li>联系电话：${hotelOrderFromBean.mobile} (预订成功后，芒果网将发送确认短信至此号码)</li>
                      <li>房价总计：
                          <c:if test="${hotelOrderFromBean.payMethod=='pay' and  false==hotelOrderFromBean.payToPrepay}">
                			<strong style="font-family:Arial, Helvetica, sans-serif;">${hotelOrderFromBean.currencyStr}<span>${hotelOrderFromBean.priceNum * hotelOrderFromBean.roomQuantity}</span>(到店付款)</strong>
                          </c:if>
                           <c:if test="${hotelOrderFromBean.payMethod=='pre_pay' || hotelOrderFromBean.payToPrepay}">
               					  <strong style="font-family:Arial, Helvetica, sans-serif;">&yen;<span id="roomPriceSum"></span>(提前付款)</strong>
               					         <script type="text/javascript">
				                                   var o = Math.ceil(${hotelOrderFromBean.priceNum * hotelOrderFromBean.roomQuantity * rate});
	                                               document.getElementById("roomPriceSum").innerHTML =o;
                                          </script>
               					 
               				 <c:if test="${hotelOrderFromBean.useUlmPoint}">
		        					 <strong style="font-family:Arial, Helvetica, sans-serif;">积分抵用:&yen;<c:out value="${hotelOrderFromBean.ulmPoint/100}"></c:out></strong>
		       				 </c:if>
		        			<c:if test="${hotelOrderFromBean.usedCoupon}">
		        					 <strong style="font-family:Arial, Helvetica, sans-serif;">代金券抵用:&yen;<c:out value="${hotelOrderFromBean.ulmCoupon}"></c:out></strong>
		       				 </c:if>
		         		    <c:if test="${hotelOrderFromBean.usedCoupon or hotelOrderFromBean.useUlmPoint}">
		                              <strong style="font-family:Arial, Helvetica, sans-serif;">实际支付金额:&yen;${hotelOrderFromBean.acturalAmount}</strong>
		         			</c:if>
            			</c:if>
                    </li>
                      <li>返现金额：<strong style="font-family:Arial, Helvetica, sans-serif;">&yen;${hotelOrderFromBean.returnAmount * hotelOrderFromBean.roomQuantity}</strong> (本次预订成交后，我们将在您成功入住并结账后的7个工作日内将返现金额返还至您的现金账户中)</li>
                  </ul>
                  <dl>
                  	  <dt>您还可以：</dt>
                      <dd>
                      <a href="#" onclick="window.print();" name="printMessage" class="green">打印预订信息</a>  <span class="gline">|</span> 
                      <a href="hotel-information.shtml?hotelId=${hotelOrderFromBean.hotelId}" name="bookOtherRoom" class="green">继续预订其他房型</a> 
                      <span class="gline">|</span> 
                      <a href="hotel-query.shtml?cityCode=${hotelOrderFromBean.cityId}" name="bookOtherHotel" class="green">继续预订其他酒店</a>
                      <span class="gline">|</span>
                      <a href="http://shop.mangocity.com"  name="goShpping" class="green">去积分商城</a>
                     <br /> 
                   
                       <a href="http://cars.mangocity.com/" name="carcoupons" class="green">成功预订酒店赠送租车自驾代金券，输入编号：HT0001，立减20元</a>
                      </dd>
                  </dl>

              </div>
              <div class="extentbar">
                  <p>感谢您选择芒果网！芒果网预订服务保障！</p>
                  <ol class="notice">
                      <li><span class="num">1、</span>到店有房、出行无忧</li>
                      <li><span class="num">2、</span>价格保证、三倍赔付</li>
                  </ol>
                  <p><strong>温馨提示：</strong></p>
                  <ol>
                      <li><span class="num">1、</span>如需修改或取消，请务必提前致电芒果网7x24小时服务热线：40066-40066。</li>
                      <li><span class="num">2、</span>如果您未入住，或者未提前通知我们取消，酒店将损失一晚房费，同时也将影响您在酒店的预订信用。</li>
                  </ol>
                  
              </div>
          </div>
          
      </div>
    </div>
    <!--=E 订单完成 -->
    <input type="hidden" name="orderNo" id="orderNo" value="${orderCD}"/>
	<input type="hidden" name="roomPrice" id="roomPrice" value="${roomPrice}"/> 

</div>
<!--=E main -->
<!-- _footer.jsp页面统计是需用到gethotel_orderno.js，因此将gethotel_orderno.js调整到_footer.jsp之前加载。modify by alfred-->
<script language="javascript" src="http://wres.mangocity.com/js/w/common/js/gethotel_orderno.js"></script>  

<!--=S footer-->
<jsp:include page="../commonjsp/_footer.jsp" flush="true" />
<!--=E footer -->

<script type="text/javascript" src="http://wres.mangocity.com/js/home/cloud/js/jquery-1.3.2.js"></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/w/mangodatepicker.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/hotel2011.js"></script>
<%--QC1223 芒果网网站--添加GA订单成功页跟踪代码 add by xuyiwen 2011-2-23 --%>
  <script type="text/javascript">
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
</script>
<%--QC1231 电商--与亿起发合作的订单数据传送方法 add by xuyiwen 2011-2-24--%>
<script src="http://wres.mangocity.com/js/w/hezuo/hezuo.js" type="text/javascript"></script>
<script type="text/javascript">
	var sumRmb = ${order.sumRmb}; 
	var createDate = <fmt:formatDate value="${order.createDate}" pattern="yyyyMMddHHmmss"/>;
	var orderNo = ${order.orderCD}; 
    hezuoxiangmuHotel(sumRmb,createDate,orderNo);
</script>
</body>
</html>
