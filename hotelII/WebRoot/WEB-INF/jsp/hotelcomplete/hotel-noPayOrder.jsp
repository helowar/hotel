<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<meta http-equiv="keywords" content="酒店查询,酒店,酒店预订,订商务酒店,快捷酒店,星级酒店.">
	<meta http-equiv="description" content="【酒店预订】芒果网为您的出行提供方便的酒店预订服务，订商务酒店、快捷酒店、星级酒店当然选芒果网，国内酒店查询预订全国免费预订电话 40066-40066 .">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>【酒店预订】酒店查询 订国内商务酒店、快捷酒店、星级酒店首选芒果网！</title>
	<link rel="stylesheet" type="text/css" href="http://hres.mangocity.com/css/h/2011/hotel2011.css" />
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  <jsp:include page="../commonjsp/_header.jsp" flush="true" />
  
   <div class="w960">
  	 <div class="htoder_top"></div>
  	 <!--=S 订单完成 -->
  	 <div class="htorder_left">
   	 	 <div class="htod_fin_text">
    	   <div class="successbar_error">
         	 <p><strong>您的预订需求我们已收到，我们的客服会在30分钟内尽快联系您！</strong></p>
         	 <p><strong>请记下您的订单号：<span class="orange"><em>${orderCD}</em></span>&nbsp;以便查询。</strong></p>
       		</div>
       	<dl>
         <dt><strong class="orange">订单信息</strong></dt>
         <dd>酒店名称：${hotelOrderFromBean.hotelName}</dd>
         <dd>入住房型：${hotelOrderFromBean.roomTypeName} 共<strong>${hotelOrderFromBean.roomQuantity}</strong>间</dd>
         <dd>酒店地址：${hotelBasicInfo.chnAddress}</dd>
         <dd>入住日期：<fmt:formatDate value="${hotelOrderFromBean.checkinDate}" pattern="yyyy年MM月dd日"/></dd>
         <dd>退房日期：<fmt:formatDate value="${hotelOrderFromBean.checkoutDate}" pattern="yyyy年MM月dd日"/> 共<strong>${hotelOrderFromBean.days}</strong> 晚</dd>
         <dd>到店时间：${hotelOrderFromBean.arrivalTime}~${hotelOrderFromBean.latestArrivalTime}</dd>
         <dd>入 住 人：${linkeManStr}</dd>
         <dd>联 系 人：${hotelOrderFromBean.linkMan}</dd>
         <dd>联系电话：${hotelOrderFromBean.mobile} (预订成功后，芒果网将发送确认短信至此号码)</dd>
         <dd>&nbsp;&nbsp;E-mail：${hotelOrderFromBean.email}</dd>
       </dl>
       
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
   </div>
  
   <!--=E 订单完成 -->
   <input type="hidden" name="orderNo" id="orderNo" value="${orderCD}"/>
	</div>
  </body>
</html>
