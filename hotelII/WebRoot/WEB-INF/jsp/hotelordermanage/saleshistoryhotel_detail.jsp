<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="../commonjsp/paginationCC.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css"
		href="http://wres.mangocity.com/css/w/member/orderframe.css" />
		
	<title>酒店订单</title>
</head>
<%
	Cookie cookies[] = request.getCookies();
	Cookie sCookie = null;
	String cookieName = "";
	String cookieValue = "";
	String isPat = "false"; //是否平安万里通会员
	if (cookies != null) {
		for (int i = 0; i < cookies.length; i++) {
			sCookie = cookies[i];
			cookieValue = sCookie.getValue();
			cookieName = sCookie.getName();

			if (cookieName.equals("pat")) {
				if (cookieValue.equals("N") || cookieValue.equals("Y")) {
					isPat = "true";
				} else {
					isPat = "false";
				}
			}
		}
	}
%>
<body>
	<!--=S test wrap-->
	<div class="w770">
		<div class="crumbs">
			您现在的位置：
			<a href="http://www.mangocity.com/mbrweb/member/memberAction!mbrInfo.action" >我的账户</a> &gt;
			<a href="http://www.mangocity.com/hotelII/browseOrders!member.shtml?memberCD=${order.memberCd}">酒店订单</a> &gt;
			<span class="curr">订单详情</span>
		</div>
		<%-- 订单取消窗体浮动层 --%>
			    <div id="loadCancelMain" style="display:none;position:absolute">
			    </div>
			    <div id="loadDocMain" style='display:none;height:583px; width:567px' >
			    </div>
		<div class="infohotel">
			<h2>
				酒店订单详情
			</h2>
			<P class="hotel_dashed">
				<span>订单编号:${order.orderCD}</span><span>会员编号：${order.memberCd}</span>
			</P>
			<h3>
				酒店预订资料
			</h3>
			<P>
				<span>酒店名称：${order.hotelName}</span><span>房间：${order.roomTypeName}</span><span>房间数量：${order.roomQuantity}</span><span>
					入住时间：<fmt:formatDate pattern="yyyy-MM-dd"
						value="${order.checkinDate}" />至<fmt:formatDate
						pattern="yyyy-MM-dd" value="${order.checkoutDate}" /> </span>
			</P>
			<P>
				<span>订单金额：<strong style="font-family:Arial, Helvetica, sans-serif;">${currencyStr}</strong>${order.sum}</span>
				<c:if test="${order.type} == 1 && ${order.source} == 'NET' && ${order.agentid} != '0075001' }">
				<span>返现金额：${order.cashBackTotal}元</span>
				</c:if>
			</P>
			<h3>
				客人资料
			</h3>
			<P>
				<span>入住人：${order.fellowNames}</span>
			</P>
			<p>
				<span>联系人：${order.linkMan}</span><span>联系电话：${order.mobile}</span><span>电子邮箱：${order.email}</span>
			</p>
			<P>
				<span>最早达到时间：${order.arrivalTime}</span><span>最迟到达时间：${order.latestArrivalTime}</span>
			</P>
			<P>
				<span>特殊要求：${order.specialRequest}</span>
			</P>
			<h3>
				其他资料
			</h3>
			<P>
				<span>付款方式：<c:if test="${order.payMethod == 'pre_pay'}">	全额预付</c:if>
					<c:if test="${order.payMethod == 'pay'}">前台面付</c:if> </span>
				<span>预订确认方式：<h:convert name="res_confirmType" value="${order.confirmType}" /></span>
			</P>
			<h3>
				预订特别提示
			</h3>
			<p>
				${bookhintSpanValue}
				<s:if test="${bookhintSpanValue != null}">
					<br />
				</s:if>
				${cancelModifyItem}
				<s:if test="${cancelModifyItem != null}">
					<br />
				</s:if>
			</p>
		</div>

		<div class="infobtn">
			<table>
			<tr>
			
				<td>
				
					<input name="" type="submit" class="sbtn" onClick="goback()" value="返回" />
					
				<c:choose>
			<c:when test="${hintType==1}">
				<a target="blank"  href="javascript:void(0);" onclick="javascript:docancel();return false;"><font color="green">订单取消</font></a>
			</c:when>
			<c:when test="${hintType ==2}">
				<a herf=""  title="当前日期已超过该订单入住日期或该订单已经取消，无法操作取消申请.">----</a>
			</c:when>
			<c:when test="${hintType==3}">
				<a herf=""  title="此酒店对取消预订有特殊要求，取消需客户支付房费，如需申请取消请通过40066-40066电话取消.">电话取消</a>
			</c:when>
			<c:when test="${hintType==4}">
				<a herf=""  title="您已提交取消申请，芒果网正在跟进您的需求.">取消申请中</a>
			</c:when>
			<c:otherwise>
			</c:otherwise>
			</c:choose>
				</td>

				<td align="center" valign="top">
								
				</td>
				
			</tr>
			</table>
			</div>

	</div>

	<!--=E test wrap-->
</body>
<script src="http://wres.mangocity.com/js/w/build/mgtool.js" type="text/javascript"></script>
<script src="http://wres.mangocity.com/js/w/module/datepicker.js" type="text/javascript"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/hotel2011.js"></script>		
<script type="text/javascript" src="http://wres.mangocity.com/js/home/cloud/js/jquery-1.3.2.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/mangocity.simplemodal.js"></script>
<script type="text/javascript">   
	function goback(){
		var memberId = "${order.memberId}";
		var memberCD = "${order.memberCd}";
		var hotelName = "${order.hotelName}";
		var orderId="${order.ID}";
		var hotelId="${order.hotelId}";
		window.location='browseOrders!member.shtml?memberCD='+memberCD;
	}
	function docancel() {
		var memberId = "${order.memberId}";
		var memberCD = "${order.memberCd}";
		var hotelName = "${order.hotelName}";
		var orderId="${order.ID}";
		var hotelId="${order.hotelId}";
		var orderStatus="${order.orderState}";

		var src = 'hotel-orderCancel.shtml?requestType=main&orderId='+orderId+ '&hotelId='+hotelId+'&memberId='+memberId+'&memberCD='+memberCD+'&hotelName='+hotelName;
		var srcCode = encodeURI(encodeURI(src));
         send_request(srcCode);

}		

function send_request(url) {//初始化、指定处理函数、发送请求的函数   
        http_request = false;   
        //开始初始化XMLHttpRequest对象   
        if(window.XMLHttpRequest) { //Mozilla 浏览器   
            http_request = new XMLHttpRequest();   
            if (http_request.overrideMimeType) {//设置MiME类别   
                http_request.overrideMimeType('text/xml');   
            }   
        }   
        else if (window.ActiveXObject) { // IE浏览器   
            try {   
                http_request = new ActiveXObject("Msxml2.XMLHTTP");   
            } catch (e) {   
                try {   
                    http_request = new ActiveXObject("Microsoft.XMLHTTP");   
                } catch (e) {}   
            }   
        }   
        if (!http_request) { // 异常，创建对象实例失败   
            window.alert("不能创建XMLHttpRequest对象实例.");   
            return false;   
        }   
        http_request.onreadystatechange = processRequest;   
        // 确定发送请求的方式和URL以及是否同步执行下段代码   
        http_request.open("GET", url, true);   
        http_request.send(null);   
    }   
    
    // 处理返回信息的函数   
    function processRequest() {   
        if (http_request.readyState == 4) { // 判断对象状态   
            if (http_request.status == 200) { // 信息已经成功返回，开始处理信息   
                var divObj = document.getElementById('loadCancelMain');
                divObj.innerHTML =http_request.responseText;  
                divObj.style.display=""; 
               divObj.style.left=(document.body.clientWidth-divObj.offsetWidth)/2+document.body.scrollLeft +'px';   
               divObj.style.top=(document.body.clientHeight-divObj.offsetHeight)/2+document.body.scrollTop+'px'; 
               divObj.style.height='195px';
               divObj.style.width='437px';
            } else { //页面不正常   
                alert("您所请求的页面有异常。");   
            }   
        }   
    }   
    
      function closemainwindow(){
  document.getElementById('loadCancelMain').innerHTML="";
  document.getElementById('loadCancelMain').style.display = "none";
  }

function do_submit(orderId,hotelId){
	var reasonInput = document.getElementById("reason");
	var reasonCDInput = document.getElementById("reasonCD");
	var hotelName = "${order.hotelName}";
	var memberCD = document.getElementById("memberCD").value;
	var reason;
	var reasonCD=document.getElementById("reasonCD").value;
	var others;
	var radioNumber = document.ordercancel.reasonRadio.length;
	  if(document.ordercancel.reasonRadio.checked == true )
	  {
	   reason = document.ordercancel.reasonRadio.value;
	  }
	  
	   if(radioNumber >1)
	   {
	     for(var i =0;i<document.ordercancel.reasonRadio.length;i++)
	     {
	      if (document.ordercancel.reasonRadio[i].checked== true )
	      {
	       reason = document.ordercancel.reasonRadio[i].value;
	       if(reason == '其他原因'){
	    	   
	    	   others = document.ordercancel.others.value;
	    	   reasonInput.value=encodeURI(encodeURI(others));
	       }else{
	    	   reasonInput.value=encodeURI(encodeURI(reason));
	       }       
	       reasonCDInput.value=i+1;
	      }
	     }
	     if(reason == null){
	    	 alert('请选择一项取消原因');
	    	 return;
	     }
	    }
	    
 jQuery.ajax({
   url:"hotel-orderCancel.shtml",
   type:'post',
   dataType:'html',
   contentType:'application/x-www-form-urlencoded',
   data:"orderId="+orderId+"&hotelId="+hotelId+"&memberCD="+memberCD+"&hotelName="+hotelName+"&reason="+reasonInput.value+"&reasonCD"+reasonCDInput.value,
   error:function(){
    },
   success:function(xmlData){
          document.getElementById('loadCancelMain').innerHTML=xmlData;
          document.getElementById('loadCancelMain').style.display = "";
          }
  });
}

function hide_successwindow(){
         document.getElementById('loadCancelMain').innerHTML="";
         document.getElementById('loadCancelMain').style.display = "none";
         var orderId="${order.ID}";
         location.replace(location.href+"?orderId="+orderId); 
}

function showOthers(){
	var others;
	others = document.getElementById("others");
	others.style.display="block";
	others.focus();
}
function hideOthers(){
	var others;
	others = document.getElementById("others");
	others.style.display="none";
}
	</script>
</html>
