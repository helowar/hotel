<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
	            String orderId = (String)request.getSession().getAttribute("orderId");
				String hotelId = (String)request.getSession().getAttribute("hotelId");
				String hotelName = (String)request.getSession().getAttribute("hotelName");
				String memberId = (String)request.getSession().getAttribute("memberId");
				String memberCD =(String)request.getSession().getAttribute("memberCD");
	
%>
<link rel="stylesheet" type="text/css" href="http://hres.mangocity.com/css/h/2011/hotel2011.css" />
<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/w/member/orderframe.css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>订单网站取消主窗体</title>

</head>
<body>
<div class="order_hotel_pop02" id="loadCancelMainItem">
<form name="ordercancel" id="ordercancel" action="hotel-orderCancel.shtml" target="_self">
<input type="hidden"  name="orderId"  id="orderId" value="<%=orderId%>"/>
<input type="hidden"  name="hotelId"  id="hotelId" value="<%=hotelId%>"/>
<input type="hidden"  name="memberId" id="memberId" value="<%=memberId%>"/>
<input type="hidden"  name="memberCD"id="memberCD"  value="<%=memberCD%>"/>
<input type="hidden"  name="hotelName" id="hotelName" value="<%=hotelName%>"/>
<input type="hidden" name = "reason" id="reason" value=""/>
<input type="hidden" name = "reasonCD"  id="reasonCD" value=""/>

   <h2>申请取消订单</h2>
   <ul>
     <li><strong class="orange">为了给您提供更好服务，请告诉我们取消订单的原因：</strong></li>
     <li><input type="radio" name="reasonRadio" value="酒店位置不合适" onclick="javascript:hideOthers()"/>酒店位置不合适&nbsp;<input type="radio" name="reasonRadio"value="通过其他途径预订" onclick="javascript:hideOthers()"/>通过其他途径预订&nbsp;<input type="radio"name="reasonRadio" value="行程改变" onclick="javascript:hideOthers()"/>行程改变&nbsp;<input type="radio" name="reasonRadio" value="重复或错误预订" onclick="javascript:hideOthers()"/>重复或错误预订</li>
     <li><input type="radio" name="reasonRadio" value="其他原因" onclick="javascript:showOthers()"/>其他原因<input type="text" name="others" id="others" class="ht_pop_ipt"  style="font-size:12px;padding-top:8px; height:19px; display:none"/></li>
   </ul>
   <div class="ht_pop_btn"><a href="javascript:do_submit(<%=orderId%>,<%=hotelId%>)" class="ht_pop_btn02">确认取消</a><a href="javascript:void(0)" onclick="closemainwindow()">不取消</a></div>

</form>
</div>
</body>
</html>