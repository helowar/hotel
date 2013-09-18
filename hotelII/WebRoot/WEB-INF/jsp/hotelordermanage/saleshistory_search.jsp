<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.Date"%>
<%@page import="com.mangocity.util.DateUtil"%>
<script type="text/javascript" src="http://wres.mangocity.com/js/home/cloud/js/jquery-1.3.2.js"></script>
	<head>

	    <%@ include file="../commonjsp/paginationCC.jsp"%>
	    <style type="text/css">
		.eXtremeTable .pageJumpTool { line-height:20px;height:20px; overflow:hidden;}
		.eXtremeTable .pageNav {height:20px; }
		.eXtremeTable .jumpPageInput {height:18px;margin-top:0px;}
		</style>
		<%	
				Cookie cookies[] = request.getCookies();
				//公司三字码
				String cmpn = "";
				if (cookies != null) {
					for (int i = 0; i < cookies.length; i++) {
						if ("CMPN".equals(cookies[i].getName())) {
							cmpn = cookies[i].getValue();
							break;
						}
					}
				}
				pageContext.setAttribute("cmpn", cmpn);
		%>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css"
			href="http://wres.mangocity.com/css/w/member/orderframe.css" />
		<title>酒店订单</title>
		<script type="text/javascript">
		function validateForm(){
			var begindate = document.getElementById("begindate").value;
			var enddate = document.getElementById("enddate").value;			
	
			if(IsStringNull(begindate)){
				alert("请输入[预订日期]查询的起始日期!");
				return false;
			}
			if(IsStringNull(enddate)){
				alert("请输入[预订日期]查询的结束日期!");
				return false;
			}

			if(!IsStringNull(begindate) && !IsStringNull(enddate)){
				var beginTime =begindate.split("-");
				var start = new Date(beginTime[0],beginTime[1],beginTime[2]);
		   	    var endTime=enddate.split("-");
				var end = new Date(endTime[0],endTime[1],endTime[2]);
				if(start.getTime()>end.getTime()){
				    alert('查询的起始日期要在查询的结束日期之前! ');
				    return false;
				}
			}
			return true;
		}
		function IsStringNull(obj) {
			if(obj == null || obj == '') {
				return true;
			}else {
				return false;
			}
		}	
		function ModifyDateFormat(){
			var beginDateObject = document.getElementById("begindate");	
			if(beginDateObject.value.length ==10){
				beginDateObject.value+=" 00:00:00";
			}
			var endDateObject = document.getElementById("enddate");
			if(endDateObject.value.length == 10){
				endDateObject.value += " 23:59:59";
			}
		}
		function formSubmit(){
		    validateForm();	    
			doQuery('queryMemberOrderHistoryForm','ec');	
		}
		
	
		function printConfirm(orderId) {
				window.open("webEmailAction!printConfirm.shtml?orderId=" + orderId + "&sendtype=1&templateType=''");
		}
		function saveConfirm(orderId) {
				window.open("webEmailAction!saveConfirm.shtml?orderId=" + orderId + "&sendtype=1&templateType=''");
		} 
		
	</script>
	</head>
	<body>
		<div class="w760">
			<div class="crumbs">
				您现在的位置：
				<a href="http://www.mangocity.com/mbrweb/member/memberAction!mbrInfo.action">我的账户</a>
				&gt;
				
				<span class="curr">酒店订单</span>
			</div>
			<!--==S search wrap-->
			<div class="searchWrap">
				<div class="searchtit">
					<h2>
						查询历史交易信息
					</h2>
				</div>
				<div class="sform">
					<form name="queryMemberOrderHistoryForm"
						id="queryMemberOrderHistoryForm">
						<input type="hidden" name="queryID"
							value="queryMemberOrderHistoryByNet" />
						<input type="hidden" name="forward"
							value="queryMemberOrderHistoryByNet" />
						<input type="hidden" name="memberId" value="${params.memberId}" />
						<input type="hidden" name="queryForList" value="1" />
						<ul>
							<li>
								<label>
									入住日期：
								</label>
								<input class="txtinw102h20" name="begindate" type="text" date="true" id="begindate"
									size="10"
									value="<fmt:formatDate pattern="yyyy-MM-dd" value="${now}"/>"
									readonly>
							</li>
							<li>
								<label>
									离店日期：
								</label>
								<input class="txtinw102h20" name="enddate" type="text" date="true" id="enddate"
									size="10" value="" readonly>
							</li>
							<li>
								<input id="searchButton" type="button" name="Submit32322"
									class="sbtn" onClick="formSubmit()" value="查询">
							</li>
						</ul>
					</form>
				</div>
			</div>
			<!--==E search wrap-->

			<!--==S order infomation-->
			<div class="infotab">
				<ul>
					<li class="selected">
						<a href="#">酒店订单</a>
					</li>
				</ul>
			</div>
			<%-- 订单取消窗体浮动层 --%>
			    <div id="loadCancelMain" style="display:none;position:absolute">
			    </div>
			    <div id="loadDocMain" style='display:none;height:583px; width:567px' >
			    </div>
			<div class="infopanel sresult">
				<div id="showDocDiv"  align="right" class="snotice">
					<a id="showDoc" href="#" class="btna93x19"
						onClick="openDoc()">
						订单状态说明</a>
				</div>
				<table cellpadding="0" cellspacing="0">
					<thead>
						<tr>
							<td>
								<ec:table items="records" var="orderInfo"
									action="${pageContext.request.contextPath}/paginateQueryMemberOrders.shtml"
									toolbarLocation="bottom"
									toolbarContent="navigation|pagejump|pagesize|status"
									nearPageNum="3" maxRowsExported="400"
									pageSizeList="max:310,1,10,15,30,50,100,all" minColWidth="20"
									rowsDisplayed="10"
									width="768px"
									>


									<ec:row style="TEXT-ALIGN: center">

										<ec:column property="abcd" title="订单编号" viewsAllowed="html" width="33"
											filterable="false">
																				<input type="hidden" name="hotelName" value="${orderInfo.HOTELNAME }"/>
											   <c:set var="mCD" value="${params.memberCD}" scope="page"></c:set>
											   <c:set var="nowDate"><fmt:formatDate value="<%=DateUtil.getSystemDate()%>" pattern="yyyy-MM-dd HH:mm:ss" /></c:set>
											    <c:set var="cancelCode" value="${orderInfo.CANCELCODE}" scope="page"></c:set>
											   <c:set var="cancelDate" value="${orderInfo.CANCELDATE}" scope="page"></c:set>
									   		   <c:set var="reservationType" value="${orderInfo.RESERVATIONTYPE}" scope="page"></c:set>	
									   		   <c:set var="firstDateOrDays" value="${orderInfo.FIRSTDATEORDAYS}" scope="page"></c:set>		
									   		   	<c:set var="firstTime" value="${orderInfo.FIRSTTIME}" scope="page"></c:set>
									   		   	<c:set var="checkInDate" value="${orderInfo.CHECKINDATE}" scope="page"></c:set>	
									   		   	<c:set var="arrivalTime" value="${orderInfo.ARRIVALTIME}" scope="page"></c:set>	
									   		   	<c:set var="orderStates" value="${orderInfo.ORDERSTATES}" scope="page"></c:set>
									   		   	<c:set var="originOrderStates" value="${orderInfo.ORIGINSTATE}" scope="page"></c:set>
									   		   	<c:set var="payMethod" value="${orderInfo.PAYMETHOD}" scope="page"></c:set>
									   		   	<c:set var="suretyCode" value="${orderInfo.SURETYCODE}" scope="page"></c:set>
                                                                                                <c:set var="auditDate" value="${orderInfo.AUDITEDDATE}" scope="page"></c:set>
                                                                                                <c:set var="customConfirm" value="${orderInfo.CUSTOMERCONFIRM}" scope="page"></c:set>
                                                
									   		   	<c:set var="latestDate"><fmt:formatDate value='<%=DateUtil.getLatestDateByReservType(pageContext.getAttribute("reservationType"),pageContext.getAttribute("firstDateOrDays") , pageContext.getAttribute("firstTime"), pageContext.getAttribute("checkInDate"),pageContext.getAttribute("arrivalTime") )%>' pattern="yyyy-MM-dd HH:mm:ss" /></c:set>		
									   		   	<c:set var="hintType"  value='<%=DateUtil.getHintType4WebCancel(pageContext.getAttribute("cancelCode"), pageContext.getAttribute("checkInDate"),pageContext.getAttribute("arrivalTime"),pageContext.getAttribute("suretyCode"),pageContext.getAttribute("payMethod"),pageContext.getAttribute("cancelDate"),pageContext.getAttribute("originOrderStates"),pageContext.getAttribute("auditDate"),pageContext.getAttribute("customConfirm"))%>' scope="page"></c:set>
											<a href="#" onclick="submitBrowse('${orderInfo.ORDERID}','${orderInfo.ORDERSTATES}','${hintType}','${orderInfo.CANCELCODE}','${orderInfo.CHECKINDATE}','${orderInfo.ARRIVALTIME}','${orderInfo.SURETYCODE}','${orderInfo.PAYMETHOD}','${orderInfo.CANCELDATE}','${orderInfo.ORIGINSTATE}');">
												${orderInfo.ORDERCD} </a>
										</ec:column>
										<ec:column property="CREATEDATE" width="70" title="预订日期"
											cell="date" format="yyyy-MM-dd"></ec:column>
										<ec:column property="SUM" width="40" title="金额"></ec:column>
										<ec:column property="ORDERSTATES" width="90" title="订单状态" cell="resource"
											format="res_hotelWebOrderState"></ec:column>

										<ec:column property="PAYMETHOD" width="70" title="付款方式" cell="resource"
											format="payMethod"></ec:column>
										<ec:column property="HOTELNAME" width="100" title="酒店名称"></ec:column>
										<ec:column property="ROOMTYPENAME" title="房型"></ec:column>
										<ec:column property="ROOMQUANTITY" width="30" title="间数"></ec:column>
										<ec:column property="CHECKINDATE" width="65" title="入住时间"
											cell="date" format="yyyy-MM-dd"></ec:column>
										<ec:column property="CHECKOUTDATE" width="65" title="离店时间"
											cell="date" format="yyyy-MM-dd"></ec:column>
										<ec:column property="none" title="操作" width="65"
											viewsAllowed="html" filterable="false">
											<c:choose>
											 <c:when test="${cmpn=='PNT'||isPatBusiness=='PNW'}">
												<img src="${pageContext.request.contextPath}/image/printconfirm.jpg" border="0"
													onclick="printConfirm(${orderInfo.ORDERID});">
														/
														<img src="${pageContext.request.contextPath}/image/saveas.jpg" border="0"
													onclick="printConfirm(${orderInfo.ORDERID});">
											</c:when>
											<c:otherwise>
											<a target="blank" href="http://www.mangocity.com/HotelCommentWeb/htl-comment_record.shtml?hotelId=${orderInfo.HOTELID}">点评 </a>
											   
									   		   	<c:choose>
									   		   	<c:when test="${hintType==1}">
											     <br/>
											     <a  href="javascript:void(0);" onclick="docancel(${orderInfo.ORDERID },${orderInfo.HOTELID})">取消订单</a>
											   </c:when>
											   <c:when test="${hintType ==2}">
											     <br/>
											     <a herf="javascript:void(0);"  title="当前日期已超过该订单入住日期或该订单已经取消，无法操作取消申请." onclick="javascript:void(0)">----</a>
											   </c:when>
											   <c:when test="${hintType==3}">
											     <br/>
											     <a herf="javascript:void(0);"  title="此酒店对取消预订有特殊要求，取消需客户支付房费，如需申请取消请通过40066-40066电话取消">电话取消.</a>
											   </c:when>
											   <c:when test="${hintType==4}">
											     <br/>
											     <a herf="javascript:void(0);"  title="您已提交取消申请，芒果网正在跟进您的需求.">取消申请中</a>
											   </c:when>
											   <c:otherwise>
											   </c:otherwise>
											   </c:choose>						   		   	
											<c:if test="${order.type} == 1 && ${order.source} == 'NET' && ${order.agentid} != '0075001' }">
											/
											<a target="blank" href="http://hotel.mangocity.com/hotel-information.shtml?hotelId=${orderInfo.HOTELID}">预订</a>
											</c:if>
										    </c:otherwise>
										    </c:choose>  
										    
										</ec:column>
										
									</ec:row>
								</ec:table>
							</td>
						</tr>
					</thead>

				</table>


			</div>
			<!--==E order infomation-->

		</div>
<script src="http://wres.mangocity.com/js/w/build/mgtool.js" type="text/javascript"></script>
<script src="http://wres.mangocity.com/js/w/module/datepicker.js" type="text/javascript"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/hotel2011.js"></script>		

<%@ include file="../commonjsp/hotelOrderStates.jsp"%>

<script type="text/javascript"> 
(function(){
	
	var start = new Datepicker(document.getElementById('begindate'), {eachW:206,ttMonth:2, startDate:[2000, 1, 1], endDate:[2020, 12, 12], callback:callhotel});
	
	function callhotel(o, t){
	    o.value = t.title;
	}
	
	var end = new Datepicker(document.getElementById('enddate'), {eachW:206,ttMonth:2, startDate:[2000, 1, 1], endDate:[2020, 12, 12], callback:callhotel});


})();
</script>		
	</body>
</html>



<script type="text/javascript">   
	// 查询会员历史订单
	function initial() {				
		//初始化日期
		var todayTime = new Date();					
		var beginDate = dateUtil.toFormatString(todayTime);			
		document.getElementById("begindate").value=beginDate;			
		var endDateTime = dateUtil.addDays(todayTime,1);			
		var endDate = dateUtil.toFormatString(endDateTime);			
		document.getElementById("enddate").value=endDate;
	}
	initial();
	
	function submitBrowse(orderId,orderStatus,hintType)
	{
	  var formSubmit;
	  formSubmit = document.getElementById('browseOrdersForm');
	  formSubmit.orderId.value=orderId;
	  formSubmit.orderStatus.value=orderStatus;
	  formSubmit.hintType.value=hintType;
	  formSubmit.submit();
	   
	}
	function docancel(orderId,hotelId) {

	var memberId = "${params.memberId}";
	var memberCD = "${params.memberCD}";
	var hotelName = "${orderInfo.HOTELNAME}";

	var src = 'hotel-orderCancel.shtml?requestType=main&orderId='+orderId+ '&hotelId='+hotelId+'&memberId='+memberId+'&memberCD='+memberCD+'&hotelName='+hotelName;

	var srcCode = encodeURI(encodeURI(src));
    send_request(srcCode);

}		

function openDoc(){
	if(isMouseLeaveOrEnter){
	var divObj = document.getElementById('orderStatesDiv');
    divObj.style.display="";
    divObj.style.left=(document.documentElement.clientWidth-divObj.offsetWidth)/2+document.documentElement.scrollLeft +'px';   
    divObj.style.top=(document.documentElement.clientHeight-divObj.offsetHeight)/2+document.documentElement.scrollTop+'px'; 
}
}
function isMouseLeaveOrEnter(e, handler) {   
if (e.type != 'mouseout' && e.type != 'mouseover') return false;   
var reltg = e.relatedTarget ? e.relatedTarget : e.type == 'mouseout' ? e.toElement : e.fromElement;   
while (reltg && reltg != handler)   
reltg = reltg.parentNode;   
return (reltg != handler);   
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
                divObj.style.left=(document.documentElement.clientWidth-divObj.offsetWidth)/2+document.documentElement.scrollLeft +'px';   
                divObj.style.top=(document.documentElement.clientHeight-divObj.offsetHeight)/2+document.documentElement.scrollTop+'px'; 
                divObj.style.height='195px';
                divObj.style.width='437px';
            } else { //页面不正常   
                alert("您所请求的页面有异常。");   
            }   
        }   
    }   
</script>
<form id="browseOrdersForm" name="browseOrdersForm" method="post"
	action="browseOrders!detail.shtml" />
	<input type="hidden" id="orderId" name="orderId" value="" />
	<input type="hidden" id="orderStatus" name="orderStatus" value="" />
	<input type="hidden" id="cancelCode" name="cancelCode" value="" />
	<input type="hidden" id="checkInDate" name="checkInDate" value="" />
	<input type="hidden" id="hintType" name="hintType" value="" />
</form>

<script type="text/javascript">

 function addApproval(orderid,item)
  {
      document.getElementById('searchButton').disabled= true;
      var approval = document.getElementById('approval');
      approval.style.left=item.parentNode.offsetLeft-180;
      approval.style.top=item.parentNode.offsetTop+110;  
      approval.style.display = "";
      document.getElementById("budgetNo").value =  document.getElementById("budgetNo"+orderid).value;
      document.getElementById("approvalOrderId").value = orderid;
    
  }
  
  function saveApproval(){
      var approvalForm = document.getElementById('approvalForm');
      var approveNo = document.getElementById('budgetNo').value;
      if(approveNo == null || approveNo == ''){
          alert('请输入费用预算单号');
          return false;
      }
      if(approveNo.length>200){
         alert("费用预算单号长度不能超过200");
         return false;
      }
      approvalForm.submit();
      
  }
  function hideApproval()
  {
  	document.getElementById("approval").style.display="none";
  	document.getElementById('searchButton').disabled= false;
  } 
  
  function closemainwindow(){
  document.getElementById('loadCancelMain').innerHTML="";
  document.getElementById('loadCancelMain').style.display = "none";
  }

function do_submit(orderId,hotelId){
	var reasonInput = document.getElementById("reason");
	var reasonCDInput = document.getElementById("reasonCD");
	var hotelName = "${orderInfo.HOTELNAME}";
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
function hide_successwindow(){
         document.getElementById('loadCancelMain').innerHTML="";
         document.getElementById('loadCancelMain').style.display = "none";
         location.replace(location.href); 
}
</script>
