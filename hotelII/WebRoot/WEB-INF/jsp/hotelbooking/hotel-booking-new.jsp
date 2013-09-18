<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="core"%>
<%@ taglib prefix="fmt" uri="fmt"%>
<%@ taglib prefix="fn" uri="fn"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="酒店查询,酒店,酒店预订,订商务酒店,快捷酒店,星级酒店." />
<meta name="description" content="【酒店预订】芒果网为您的出行提供方便的酒店预订服务，订商务酒店、快捷酒店、星级酒店当然选芒果网，国内酒店查询预订全国免费预订电话 40066-40066 ." />
<title>【酒店预订】酒店查询 订国内商务酒店、快捷酒店、星级酒店首选芒果网！</title>
<link rel="stylesheet" type="text/css" href="http://hres.mangocity.com/css/h/2011/hotel2011.css" />
<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/w/module/datepicker.css" />
<script type="text/javascript" src="http://wres.mangocity.com/js/home/cloud/js/jquery-1.3.2.js"></script>
<link rel="stylesheet" type="text/css" href="http://hres.mangocity.com/css/jquery.alert.css"/>
<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/w/module/mango_online.css" />
<script type="text/javascript" src="js/hotelbookJS/greybox.js"></script>
<script type="text/javascript" src="js/jQuery/jquery.alert.js"></script>	
<script type="text/javascript" src="http://hres.mangocity.com/js/mangocity.simplemodal.js"></script>
<script type="text/javascript" src="js/commonJS/projectcode.js"></script>
<script type="text/javascript" src="js/commonJS/o_code_https.js"></script>
<%
String projectCode;
projectCode = request.getParameter("projectcode");
if (projectCode != null && !projectCode.equals("")) {	
	Cookie cookieProject = new Cookie("projectcode",projectCode);
	cookieProject.setPath("/");
	response.addCookie(cookieProject);
}
%>
<script type="text/javascript"> 	
  document.domain = "mangocity.com";
  $(document).ready(function() {
		var projectName = getProjectName('${param.projectcode}');
		if(null != projectName){
			$('#projectName').html(projectName);
			$('#projectView').css('display','block');
		}
	});
  
 
</script>	

<style type="text/css">
.localLogin{
font-size:20px;
font-weight:800;
}
#input_information{
line-height:24px;
margin-top:0px;
color:#777777;
margin-left:68px;
float:left;
}
a.localLogin:link{ color:#C76114;}
a.localLogin:hover{color:#FF6600;}
a.localLogin:visited{ color:#FF6600;}
.brackets{
color:#C76114;
font-size:20px;
}
.choosediv{
line-height:18px;
text-align: center;
width:512px;
position:absolute; 
background-color:#fff;
color:#595959;
z-index: 998;
opacity: 100;
}
.choosediv .choosettbg{ 
border:#f4a13d 1px solid;
line-height:26px;
padding-left:10px;
background-color:#f2c085;
height:26px;
}
.choosediv h3{
float:left;
color:#fff;
font-size:14px;
}
.choosebtdiv{
height:40px;
vertical-align:middle;
}
.buttonbg{
background-color:#ffa405;
color:#fff;
border:0 none;
cursor: pointer;
}
.choosetb{
cellpadding:0;
cellspacing:0;
align:center;
text-align:center;
line-height:24px;
}
.choosetb th{
color:#595959;
border-bottom:#e1e1e1 1px dotted;
}
.choosetb td{
backgroud-color:#ffa405;
border-bottom:#e1e1e1 1px dotted;
}
</style>
</head>
  
 <body onload="initHotelBook()">

  <jsp:include page="../commonjsp/_header.jsp" flush="true" />  
  <%-- =S top bar --%>
<div class="w960 hn-topbar">
    <div class="status step3">
        <ul>
            <li>查询</li>
            <li>选择</li>
            <li class="choice">预订</li>
            <li>完成</li>
        </ul>
    </div>
     <div class="crumbs">您现在的位置：<a href="http://www.mangocity.com" class="green" name="firstPage" >首页</a> &gt; <a href="hotel-search.shtml" class="green" name="domesticHotel" >国内酒店</a> &gt; 
           <a href="list-${fn:toLowerCase(hotelOrderFromBean.cityCode)}.html" class="green" name="daohangcity">${hotelOrderFromBean.cityName}酒店</a>
           &gt; <a href='jiudian-${fn:toLowerCase(hotelOrderFromBean.hotelId)}.html' class="green" name="daohangHotel">${hotelOrderFromBean.hotelName}</a> &gt; <em>订单填写</em>
    </div>
</div>
<%-- =E top bar--%>
  
  <div class="w960">
  	<!--=S 订单填写去哪儿 -->
		    <div class="htorder_qd" id="projectView" style="display:none">
		      尊敬的用户，您的本次预订来自于“<span id="projectName"></span>”，芒果网欢迎您的到来！
		    </div>
		  <!--=E 订单填写去哪儿 -->
       <!--=S 订单填写 -->
       <div class="htorder_left" >
          <div class="htorder_fill">
          
             <div class="htod_tit">
                <div class="hotelname" id="hotelNameDiv">
                   <h1 class="${hotelVO.commendType}" id="hotelName">${hotelOrderFromBean.hotelName}</h1>
                   <c:choose>    
            		<c:when test="${fn:indexOf(hotelVO.hotelStar,'star') ge 0}">     
            			<em class="hotelstar ${hotelVO.hotelStar}" ></em>		    
            		</c:when>    
            		<c:otherwise>     
            			<em class="hotelstarName" >&nbsp;&nbsp;${hotelVO.hotelStar}</em>   
            		</c:otherwise>   
            	    </c:choose>
                </div>
                <p class="ename">地址：${hotelVO.district}${hotelVO.chnAddress} 
                <c:if test="${not empty hotelAlertMsg}">&nbsp;&nbsp;
                <span id="hotelAlertMsg"><a style="font-family:微软雅黑;cursor:pointer;font-size:12px;font-weight:bold;font-style:normal;color:#009900;">温馨提示</a></span>
                <div class="ht_fc" id="hotelAlertMsgDiv" style="display: none;">
                   <div class="ht_fc_top"><img src="http://himg.mangocity.com/img/h/2011/ht_fcsm.jpg"></div>
                   <div class="ht_fc_main">
                   ${hotelAlertMsg}
                   </div>
                   <div class="ht_fc_b"><img src="http://himg.mangocity.com/img/h/2011/ht_fcsm_b.jpg"></div>
                 </div>
                 </c:if>
                 </p>
             </div>
             
             <ul class="htod_ul">
                <li class="li_float"><label>入住房型：</label><span>${hotelOrderFromBean.roomTypeName}</span><span class="grey">（${hotelOrderFromBean.childRoomTypeName}）</span>
                <c:if test="${not empty roomPresaleList}">
                    <span  id="promoSaleText"><a class="gifts"></a></span>
                   
                    <div class="ht_fc"  id="promoSaleTipDiv"  style="display:none;">
                   <div class="ht_fc_top"><img src="http://himg.mangocity.com/img/h/2011/ht_fcsm.jpg" /></div>
                   <div class="ht_fc_main">
						 <strong>酒店促销信息：</strong>
	 	 	               <br />	 	 	  	 	 	                
			            <c:forEach items="${roomPresaleList}" var="item">
			            <c:if test="${item.webShow=='1'}">
			                        <p>${item.content}</p>
			                        <em class="orange">有效期：${item.beginEnd}。</em></ br>
			             </c:if>
			             </c:forEach>

                    </div>
                   <div class="ht_fc_b"><img src="http://himg.mangocity.com/img/h/2011/ht_fcsm_b.jpg" /></div>
                 </div>
            
                </c:if>
                </li>
                <li><label>入住日期：</label><span class="red"><span id="checkinDate"><fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkinDate}"/></span>(${weekOfInDate})入住，
                    <span id="checkoutDate"><fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkoutDate}"/></span>(${weekOfOutDate})退房，共${hotelOrderFromBean.days}晚。</span>
                    <span class="simpleinfo"><em id="changedatebtn" class="changetime" name="changedatebtn" >修改日期</em></span>
                </li>
                <li><label>房间数量：</label>
                <select name="roomNumSelect" id="roomNumSelect" onclick="return false;">
                        </select>
                    <label>床型：</label><select name="bedtypeSelect" id="bedtypeSelect" onclick="__ozclk();return false;"></select>
                    <label id="bedtypeTip" class="red"></label>
                    <label id="bedAlert" class="red"></label>
                </li>
                <li><label>房费合计：</label>
                                      <c:if test="${hotelOrderFromBean.payMethod=='pay' and  false==hotelOrderFromBean.payToPrepay}">
                                        <strong class="orange">${hotelOrderFromBean.currencyStr}<span id="allPriceSum">${hotelOrderFromBean.priceNum}</span></strong>
                                      </c:if>
                                      <c:if test="${hotelOrderFromBean.payMethod=='pre_pay' || hotelOrderFromBean.payToPrepay}">
                                        <strong class="orange">&yen;<span id="allPriceSum">${hotelOrderFromBean.priceNum*rate}</span></strong>
                                       
                                      </c:if>   
                                       <script type="text/javascript">
                                                  var oldPrice=jQuery("#allPriceSum").text();
				                                   var o =  Math.ceil(oldPrice);
	                                               document.getElementById("allPriceSum").innerHTML = o ;
                                         </script> 
                <span class="grey" id="extras">
                </span>
                <script type="text/javascript">
                var showInformation="";
                if('${hotelOrderFromBean.days}'!='1'){
                if('${hotelOrderFromBean.payMethod}'=='pay' &&  false==${hotelOrderFromBean.payToPrepay}){
                   showInformation="(到店支付)";
                }
                if('${hotelOrderFromBean.payMethod}'=='pre_pay' || ${hotelOrderFromBean.payToPrepay}){
                   showInformation="(网上支付)";
                }
                }   
                document.getElementById("extras").innerHTML = showInformation ;                              
                 </script> 
                <c:if test="${not empty hotelOrderFromBean.tipInfo}">
                <span id="hotelTip" style="font-family:微软雅黑;font-size:12px;font-weight:bold;font-style:normal;text-decoration:underline;color:#009900;">预订提示</span>
                    <div id="hotelTipDiv" class="ht_fc" style="display:none">
                    <div class="ht_fc_top"><img src="http://himg.mangocity.com/img/h/2011/ht_fcsm.jpg" /></div>
			            <div class="ht_fc_main"> 
			              <strong>酒店提示信息：</strong>
	 	 	              <br />${hotelOrderFromBean.tipInfo}
			            </div>
			         <div class="ht_fc_b"><img src="http://himg.mangocity.com/img/h/2011/ht_fcsm_b.jpg" /></div>     
                    </div>
                    <script type="text/javascript">
                        jQuery("#hotelTip").mouseover(function(){
                           jQuery("#hotelTipDiv").show();
                           var left_value = jQuery(this).offset().left-13 ;
		                   var top_value = jQuery(this).offset().top+15;
		                   jQuery("#hotelTipDiv").css({top:top_value,left:left_value});
                        });
                        jQuery("#hotelTip").mouseout(function(){
                           jQuery("#hotelTipDiv").hide();
                        });
                    </script>
                </c:if>
                </li>
             </ul>

             <div class="htod_calendar" id="htod_calendar_id">               
               <ul>
               <c:forEach items="${breakfastAndPriceItemVOList}" var="breakfastAndPrice" varStatus="status">
               <c:if test="${hotelOrderFromBean.days=='1'}">
                <script type="text/javascript">
                var showInformation="";
                if('${hotelOrderFromBean.payMethod}'=='pay' &&  false==${hotelOrderFromBean.payToPrepay}){
                   showInformation="(到店支付";
                }
                if('${hotelOrderFromBean.payMethod}'=='pre_pay' || ${hotelOrderFromBean.payToPrepay}){
                   showInformation="(网上支付";
                }       
                showInformation+="、"+'${breakfastAndPrice.breakfastNumName }'+")";
                document.getElementById("extras").innerHTML = showInformation ;                               
                 </script> 
               </c:if>
               <c:if test="${hotelOrderFromBean.days!='1'}">
                 <c:choose>
                 	<c:when test="${breakfastAndPrice.inTheDays}">
                 	    <c:if test="${status.index%7==0}"><li class="firstprice htod_calendar_col"></c:if>
                        <c:if test="${status.index%7!=0}"><li class="htod_calendar_col"></c:if>                                        
                 	    <span>${breakfastAndPrice.fellowDate }（${breakfastAndPrice.weekDay }）</span>
                 	    <p><strong>${breakfastAndPrice.currencyStr}${breakfastAndPrice.salePrice }<br />${breakfastAndPrice.breakfastNumName }</strong></p></li>
                 	</c:when>
                   <c:otherwise>
                         <c:if test="${status.index%7==0}"><li class="firstprice"></c:if>
                         <c:if test="${status.index%7!=0}"><li class=""></c:if> 
                  		 <span>${breakfastAndPrice.fellowDate }（${breakfastAndPrice.weekDay }）</span>
                  		 <p>
                  		 <c:choose>
                  		 <c:when test="${breakfastAndPrice.salePrice >= 499999.00 }">
                  		 ${breakfastAndPrice.currencyStr}--
                  		 </c:when>
                  		 <c:otherwise>
                  		 ${breakfastAndPrice.currencyStr}${breakfastAndPrice.salePrice }
                  		 </c:otherwise>
                  		 </c:choose>
                  		 <br />${breakfastAndPrice.breakfastNumName}
                  		 </p>
                  		 </li>
                 	</c:otherwise>
                  </c:choose> 
                  </c:if>                
              </c:forEach>    
               </ul>
          </div>
                <c:if test="${hotelOrderFromBean.returnAmount gt 0.0}">
                <div class="htod_enjoy"><span class="cleft"><strong>可以享受优惠：</strong></span>
                <strong><span class="htod_enjoy_price" id="returnCash">${hotelOrderFromBean.returnAmount} 元</span></strong>
                </div>
                 </c:if>       	  
</div>  
          <!-- checkin start -->
          <div class="htorder_checkin">
             		<h2>入住信息</h2>  
             <ul class="htchkin_ul">
                <li><label><strong class="red">*</strong>入 住 人：</label><em  id="fellowPersonLabel" name="fellowPersonLabel">
                    	     <input type='text' name='fellowName' value="住客姓名" id="fellowNameId1" style="color:#999;margin-right:2px" onclick="__ozclk();return false;"/></em>  
                    	   <br/><span id="input_information"></span>
                    	     <%-- 香港澳门城市标示 --%>
                    	      <input type="hidden" id="flagHKGORMAC" value="false" />  
                    	      <c:choose>
                    	       <c:when test="${hotelOrderFromBean.currency =='HKD' or hotelOrderFromBean.currency =='MOP' or hotelOrderFromBean.roomChannel==8}">
                    	        <script type="text/javascript">
                                  jQuery("#flagHKGORMAC").val(true);
                             </script>
                   <strong class="green" id="fellowPersonText" style="border-bottom: 1px dotted gray;">填写规则</strong>
                    	     <em style="display:none;" id="alertMes" ><br/><label style="margin-left:78px;float:left">
                    	     <font style="color:red;" id="fellowPersonAlertMes">入住人填写格式错误，请参考填写规则</font></label></em>
                    	     <div class="ht_fc"  id="fellowPersonTipDiv"  style="display:none;">
                   <div class="ht_fc_top"><img src="http://himg.mangocity.com/img/h/2011/ht_fcsm.jpg" /></div>
                   <div class="ht_fc_main">请填写与入住人所持证件完全一致的姓名拼音或英文姓名(格式：Last name / First name)。例如：李建国 应为 li/jianguo
					</div>
                   <div class="ht_fc_b"><img src="http://himg.mangocity.com/img/h/2011/ht_fcsm_b.jpg" /></div>
 			</div>
			
			</c:when>
			<c:otherwise>
                    	     <strong class="green" id="fellowPersonText" style="border-bottom: 1px dotted gray;">填写规则</strong>
                    	     <em style="display:none;" id="alertMes" ><br/><label style="margin-left:78px;float:left">
                    	     <font style="color:red;" id="fellowPersonAlertMes">入住人填写格式错误，请参考填写规则</font>
                    	     </label>
                    	     </em>
                    	     <div class="ht_fc"  id="fellowPersonTipDiv"  style="display:none;">
                   <div class="ht_fc_top"><img src="http://himg.mangocity.com/img/h/2011/ht_fcsm.jpg" /></div>
                   <div class="ht_fc_main">请填写与入住人所持证件完全一致的姓名（如果您是帮朋友预订，请填写您朋友的姓名）
					</div>
                   <div class="ht_fc_b"><img src="http://himg.mangocity.com/img/h/2011/ht_fcsm_b.jpg" /></div>
			</c:otherwise>
			</c:choose>
			
             </li>
                               
               <li><label><strong class="red">*</strong>联 系 人：</label>
                   <input name="linkmanName" id="linkmanName" type="text" onclick="addHistorylinkman();__ozclk();return false;"/>&nbsp;&nbsp;<label> 
                   <strong class="red">*</strong>联系手机：</label><input name="linkmanTelephone" id="linkmanTelephone" type="text" onclick="__ozclk();return false;"/>&nbsp;&nbsp;
               <label>电子邮箱：</label><input name="linkmanEmail" id="linkmanEmail" type="text" onclick="__ozclk();return false;"/>
               <em style="display:none;" id="linkmanMes"><br/><br/>
               <label style="color:red;margin-left:78px;float:left" id="linkmanNameMes">&nbsp;您还没有填写联系人姓名。请填写正确的姓名，以便我们后续能更快与您联系。</label>
               </em>
               </li>
               <!-- 会员未登录的情况：显示一个提示框 -->
               <c:if test="${whetherLogin == 'false'}">
               <li>
                  <c:if test="${hotelOrderFromBean.returnAmount == 0.0}">
                  	<div class="htchkin_sm">
                    	<a href="#" onclick="closeHtlChkin();" class="htchkin_sm_close"><img src="http://himg.mangocity.com/img/h/2011/htorder_icon_close.jpg" /></a>
               		    <p class="pad10">登陆或注册芒果网会员预订酒店，可获得积分奖励！&nbsp;&nbsp;<a style="background-image: url('http://himg.mangocity.com/img/h/2011/htod_enjoy_button.jpg');background-repeat: no-repeat;line-height: 26px;padding: 5px 45px 10px 47px" href="javascript:void(0)" id="loginMemberForAct" >&nbsp;</a>&nbsp;&nbsp;<a href="http://www.mangocity.com/mbrweb/register/init.action" target="_blank" style="background-image: url('http://himg.mangocity.com/img/h/2011/htod_login_button.jpg');background-repeat: no-repeat;line-height: 26px;padding: 5px 45px 10px 47px"></a></p>
                   	</div>               
                 </c:if>
               </li>   		

               </c:if>
                <li class="htchkin_time"><label><strong class="red">*</strong>房间留到：</label>  
                <c:choose>   
                                         
               <c:when test="${not empty assureInfoVO.midFirstRetentionTime && not empty assureInfoVO.midSecondRetentionTime &&not empty assureInfoVO.firstRetentionTime}">                   
               <input name="checkInTime" type="radio" value="${assureInfoVO.firstRetentionTime }" checked="checked" id="checkInTime0"/>${assureInfoVO.firstRetentionTime }&nbsp;&nbsp;              
               <input name="checkInTime" type="radio" value="${assureInfoVO.midFirstRetentionTime }"  id="checkInTime2"/>${assureInfoVO.midFirstRetentionTime }&nbsp;&nbsp;
               <input name="checkInTime" type="radio" value="${assureInfoVO.midSecondRetentionTime }"  id="checkInTime3"/>${assureInfoVO.midSecondRetentionTime }&nbsp;&nbsp;
               <input name="checkInTime" type="radio" value="01:00" id="checkInTime1"/>${assureInfoVO.secondRetentionTime }&nbsp;&nbsp; 
               
               <em id="assureHint"></em>                 
                  <div class="htchkin_point" id="lastestArrirveTime">
                      <a class="htodchkin_close" href="javascript:void(0);" onclick="simpleHiddenDiv('lastestArrirveTime');" id="lastestArrirveTimeClose">&nbsp;</a>
                         <label id="htchkin_point_text"> 房间保留至<fmt:formatDate pattern="MM月dd日" value="${hotelOrderFromBean.checkinDate}"/><font id="lastestArrirveTimeText">${assureInfoVO.firstRetentionTime }</font>；如果入住人不能在<font id="lastestArrirveTimeText1">${assureInfoVO.firstRetentionTime }</font>前到达酒店，请及时联系芒果网：
                        </label>  
                        
                        <label id="htchkin_point_text_asure" style="display:none;"> 担保后房间将为您整晚保留，如您在办理入住时遇到问题，请随时联系芒果网：
                        </label>  
                        
                 <font class="orange"><b>40066-40066。</b></font>
                  </div>
              </c:when>
              
              <c:when test="${not empty assureInfoVO.midFirstRetentionTime && not empty assureInfoVO.midSecondRetentionTime && empty assureInfoVO.firstRetentionTime}">                               
               <input name="checkInTime" type="radio" value="${assureInfoVO.midFirstRetentionTime }"  id="checkInTime2"/>${assureInfoVO.midFirstRetentionTime }&nbsp;&nbsp;
               <input name="checkInTime" type="radio" value="${assureInfoVO.midSecondRetentionTime }"  id="checkInTime3"/>${assureInfoVO.midSecondRetentionTime }&nbsp;&nbsp;
               <input name="checkInTime" type="radio" value="01:00" id="checkInTime1"/>${assureInfoVO.secondRetentionTime }&nbsp;&nbsp; 
               
               <em id="assureHint"></em>                 
                  <div class="htchkin_point" id="lastestArrirveTime">
                      <a class="htodchkin_close" href="javascript:void(0);" onclick="simpleHiddenDiv('lastestArrirveTime');" id="lastestArrirveTimeClose">&nbsp;</a>
                         <label id="htchkin_point_text"> 房间保留至<fmt:formatDate pattern="MM月dd日" value="${hotelOrderFromBean.checkinDate}"/><font id="lastestArrirveTimeText">${assureInfoVO.firstRetentionTime }</font>；如果入住人不能在<font id="lastestArrirveTimeText1">${assureInfoVO.firstRetentionTime }</font>前到达酒店，请及时联系芒果网：
                        </label>  
                        
                         <label id="htchkin_point_text_asure" style="display:none;"> 担保后房间将为您整晚保留，如您在办理入住时遇到问题，请随时联系芒果网：
                        </label>  
                 <font class="orange"><b>40066-40066。</b></font>
                  </div>
              </c:when>
              
               <c:when test="${ empty assureInfoVO.midFirstRetentionTime && not empty assureInfoVO.midSecondRetentionTime && empty assureInfoVO.firstRetentionTime}">                               
               <input name="checkInTime" type="radio" value="${assureInfoVO.midSecondRetentionTime }"  id="checkInTime3"/>${assureInfoVO.midSecondRetentionTime }&nbsp;&nbsp;
               <input name="checkInTime" type="radio" value="01:00" id="checkInTime1"/>${assureInfoVO.secondRetentionTime }&nbsp;&nbsp; 
               
               <em id="assureHint"></em>                 
                  <div class="htchkin_point" id="lastestArrirveTime">
                      <a class="htodchkin_close" href="javascript:void(0);" onclick="simpleHiddenDiv('lastestArrirveTime');" id="lastestArrirveTimeClose">&nbsp;</a>
                         <label id="htchkin_point_text"> 房间保留至<fmt:formatDate pattern="MM月dd日" value="${hotelOrderFromBean.checkinDate}"/><font id="lastestArrirveTimeText">${assureInfoVO.firstRetentionTime }</font>；如果入住人不能在<font id="lastestArrirveTimeText1">${assureInfoVO.firstRetentionTime }</font>前到达酒店，请及时联系芒果网：
                        </label>
                        
                         <label id="htchkin_point_text_asure" style="display:none;"> 担保后房间将为您整晚保留，如您在办理入住时遇到问题，请随时联系芒果网：
                        </label>    
                 <font class="orange"><b>40066-40066。</b></font>
                  </div>
              </c:when>
              
              
               <c:when test="${not empty assureInfoVO.firstRetentionTime && empty assureInfoVO.midFirstRetentionTime && empty assureInfoVO.midSecondRetentionTime}">                   
               <input name="checkInTime" type="radio" value="${assureInfoVO.firstRetentionTime }" checked="checked" id="checkInTime0"/>${assureInfoVO.firstRetentionTime }&nbsp;&nbsp;              
               <input name="checkInTime" type="radio" value="01:00" id="checkInTime1"/>${assureInfoVO.secondRetentionTime }&nbsp;&nbsp; 
               
               <em id="assureHint"></em>                 
                  <div class="htchkin_point" id="lastestArrirveTime">
                      <a class="htodchkin_close" href="javascript:void(0);" onclick="simpleHiddenDiv('lastestArrirveTime');" id="lastestArrirveTimeClose">&nbsp;</a>
                         <label id="htchkin_point_text"> 房间保留至<fmt:formatDate pattern="MM月dd日" value="${hotelOrderFromBean.checkinDate}"/><font id="lastestArrirveTimeText">${assureInfoVO.firstRetentionTime }</font>；如果入住人不能在<font id="lastestArrirveTimeText1">${assureInfoVO.firstRetentionTime }</font>前到达酒店，请及时联系芒果网：
                        </label>  
                        
                          <label id="htchkin_point_text_asure" style="display:none;"> 担保后房间将为您整晚保留，如您在办理入住时遇到问题，请随时联系芒果网：
                        </label>  
                 <font class="orange"><b>40066-40066。</b></font>
                  </div>
              </c:when>
              
              <c:otherwise>             
                <input name="checkInTime" type="radio" value="01:00" checked="checked" id="checkInTime1"/>${assureInfoVO.secondRetentionTime }&nbsp;&nbsp; 
               <em id="assureHint"></em>                 
                  <div class="htchkin_point" id="lastestArrirveTime">
                       <a class="htodchkin_close" href="javascript:void(0);" onclick="simpleHiddenDiv('lastestArrirveTime');" id="lastestArrirveTimeClose">&nbsp;</a>
                     <label id="htchkin_point_text">房间保留至<fmt:formatDate pattern="MM月dd日" value="${hotelOrderFromBean.checkinDate}"/><font id="lastestArrirveTimeText">${assureInfoVO.secondRetentionTime }</font>；如果入住人不能在<font id="lastestArrirveTimeText1">${assureInfoVO.firstRetentionTime }</font>前到达酒店，请及时联系芒果网：
                  </label>   
                    <label id="htchkin_point_text_asure" style="display:none;"> 担保后房间将为您整晚保留，如您在办理入住时遇到问题，请随时联系芒果网：
                        </label>  
                  <font class="orange"><b>40066-40066。</b></font>
                  
                  </div>
              
              </c:otherwise>
             </c:choose> 
                 
             <div class="htchkin_box" style="display:none">
               <p id="showspecialrequireP">如果您有特殊要求,请点击<a href="javascript:void(0);" id="showspecialrequire" name="showspecialrequire">这里</a></p>
               <div class="htchkin_box_c" id="specialrequire" style="display:none" >
                <h3>备注特殊要求</h3>
                 <p class="htchkin_box_p1">
                        <input name="specialrequireInput" type="checkbox" value="宽带" class="iradio" /><label>宽带</label>
                        <input name="specialrequireInput" type="checkbox" value="高层" class="iradio" /><label>高层</label>
                        <input name="specialrequireInput" type="checkbox" value="无烟房" class="iradio" /><label>无烟房</label>
                        <!--  
                        <input name="specialrequireInput" type="checkbox" value="吸烟房" class="iradio" /><label>吸烟房</label>
                        -->
                 <p class="htchkin_box_p2">
                    <textarea name="specialrequireTextarea" id="specialrequireTextarea" onkeyup="isMax()" cols="" rows="" class="w597"></textarea>
                 </p>               
               </div>
             </div>
             
          </div>        
          <!-- checkin end -->
          
          <div class="htod_putin">
          <c:if test="${hotelOrderFromBean.payMethod=='pay' and  false==hotelOrderFromBean.payToPrepay}">
          <span>注：如需发票，请从酒店前台索取</span>
          </c:if>         
          <input name="submitOrder"  onclick="paySubmitOrder();" id="submitOrder" type="submit" class="btn96x27" style="float: right;margin-right: 290px" value="提交订单" />        
          </div>
       </div>
                       	    
       <div class="htorder_right">
          <div class="htoder_help">
                 <c:if test="${empty member}">
                    <a href="#" id="loginMember"  class="htoder_lg">立即登录</a>                                      
                    <a href="http://www.mangocity.com/mbrweb/register/init.action" target="_blank" class="htoder_reg">免费注册</a>
                 </c:if>
                 <c:if test="${not empty member}">
                      <div class="htoder_help_name">尊敬的会员：<strong class="orange" id="memberUserName"></strong></div>
                       <script type="text/javascript">                                                
                           var aliasname = getCookieHistory("aliasname");                           
                           jQuery('#memberUserName').text(aliasname);
                           function getCookieHistory(name){
                           var cookies=document.cookie.split("; ");
                           for(var i=0;i<cookies.length;i++){
                             var temp=cookies[i].split("=");
                             if(temp[0]==name){
                               return temp[1];
                             }
                           }
                           return "";
                           }
                          
                       </script>
                 </c:if>
			    
          </div>                  
                                      
          <div class="htoder_details">
            <h2>酒店信息</h2>
           <c:if test="${not empty freeServiceToShow}">
           
             <c:choose>
             <c:when test = "${empty additionalServe and empty mealAndFixtureToShow and empty roomEquipToShow}"> 
            <div class="htoder_details_box htdoer_none">
            </c:when>
            <c:otherwise>
               		<div class="htoder_details_box" >
           	</c:otherwise>
            </c:choose>
                                   
            <c:choose>
            <c:when test = "${fn:length(freeServiceToShow) lt 6}"> 
             <dl class="htoder_details_box_dl">
            </c:when>
            <c:otherwise>
               <dl>
            </c:otherwise>
            </c:choose>
	         
	              <dt>免费服务</dt>
	              <c:forEach items="${freeServiceToShow}"  var="freeService">	
	              <c:choose>
	              <c:when test="${fn:length(freeService) gt 7}">
	               <dd style="width: 180px">${freeService}</dd>
	              </c:when>
	              <c:otherwise><dd>${freeService}</dd></c:otherwise>
	              </c:choose>             	                    
	              </c:forEach>
	            </dl>
	            <c:if test = "${fn:length(freeServiceToShow) gt 6}">          
	                <div class="htod_more"><a href="javascript:void(0);">查看更多</a></div>
	            </c:if> 
            </div>
            </c:if>
            <c:if test="${not empty roomEquipToShow}">
            
             <c:choose>
             <c:when test = "${empty additionalServe and empty mealAndFixtureToShow}"> 
            <div class="htoder_details_box htdoer_none">
            </c:when>
            <c:otherwise>
               		<div class="htoder_details_box" >
           	</c:otherwise>
            </c:choose>
           
            
	         <c:choose>
            	<c:when test = "${fn:length(roomEquipToShow) lt 6}"> 
            		 <dl class="htoder_details_box_dl">
            	</c:when>
            	<c:otherwise>
               		<dl>
           		 </c:otherwise>
            </c:choose>
	              <dt>客房设施</dt>
	              <c:forEach items="${roomEquipToShow}" var="freeService">
	                    <dd>${freeService}</dd>
	              </c:forEach>
	            </dl>
	            <c:if test = "${fn:length(roomEquipToShow) gt 6}">          
	                <div class="htod_more"><a href="javascript:void(0);">查看更多</a></div>
	            </c:if> 
            </div>
            </c:if>
             <c:if test="${not empty mealAndFixtureToShow}">
             
             <c:choose>
             <c:when test = "${empty additionalServe}"> 
            <div class="htoder_details_box htdoer_none">
            </c:when>
            <c:otherwise>
               		<div class="htoder_details_box" >
           	</c:otherwise>
            </c:choose>
            
	             <c:choose>
            		<c:when test = "${fn:length(mealAndFixtureToShow) lt 6}"> 
            		 	<dl class="htoder_details_box_dl">
            		</c:when>
            		<c:otherwise>
               			<dl>
           		 	</c:otherwise>
            </c:choose>
	              <dt>休闲设施</dt>
	              <c:forEach items="${mealAndFixtureToShow}" var="freeService">
	                    <dd>${freeService}</dd>
	              </c:forEach>
	            </dl>
	            <c:if test = "${fn:length(mealAndFixtureToShow) gt 6}">          
	                <div class="htod_more"><a href="javascript:void(0);">查看更多</a></div>
	            </c:if> 
            </div>
            </c:if>
             <c:if test="${ not empty additionalServe}">
             <div class="htoder_details_box htdoer_none">
                 <dl>
                    <dt>附加服务说明</dt>
                         <c:if test="${not empty additionalServe.bedServes}"><dt>加床价服务：</dt>
			               		<c:forEach items="${additionalServe.bedServes}" var="bedItem">
			               		 <dd class="htoder_details_box_dd">
			               		<c:choose>
			               		
			               		<c:when test="${1 == bedItem.addType}">
			               		
			               		<c:out value="${bedItem.validDate}"></c:out>&nbsp;<c:out value="${hotelOrderFromBean.currency}" /><s:property value="getRoomPrice(${bedItem.amount})"/>/床
					            </c:when>
					           
					            <c:otherwise>
					            <c:out value="${bedItem.validDate}"></c:out>&nbsp;房费的<s:property value="getRoomPrice(${bedItem.amount} * 100)"/>%/床

					            </c:otherwise>
					         
					            </c:choose>
					             </dd>
					            </c:forEach>
					       </c:if>
			       
					       <c:if test="${!empty additionalServe.chineseServes || !empty additionalServe.westernServes || !empty additionalServe.buffetServes}">
					             <dt>早餐服务：</dt>          
					                        <c:if test="${not empty additionalServe.chineseServes}">
					                          		
					                        	<c:forEach items="${additionalServe.chineseServes}" var="buffetItem">	          
					                        <dd class="htoder_details_box_dd">中早：<c:out value="${buffetItem.validDate}"></c:out>&nbsp;<c:out value="${hotelOrderFromBean.currency}" />
					                        <s:property value="getRoomPrice(${buffetItem.amount})"/>/人</dd>
					                         </c:forEach>
			                            	</c:if>		
					                        <c:if test="${not empty additionalServe.westernServes}">					                       		                        
					                        <c:forEach items="${additionalServe.westernServes}" var="buffetItem">
					                        <dd class="htoder_details_box_dd">西早：<c:out value="${buffetItem.validDate}"></c:out>&nbsp;<c:out value="${hotelOrderFromBean.currency}" /><s:property value="getRoomPrice(${buffetItem.amount})"/>/人</dd>
					                        </c:forEach>
			                               	</c:if>				                            				                        
					                        <c:if test="${not empty additionalServe.buffetServes}"><c:forEach items="${additionalServe.buffetServes}" var="buffetItem">					                   
					                        <dd class="htoder_details_box_dd">自助早：<c:out value="${buffetItem.validDate}"></c:out>&nbsp;<c:out value="${hotelOrderFromBean.currency}" /><s:property value="getRoomPrice(${buffetItem.amount})"/>/人</dd>		                       
					                            	</c:forEach>
			                            	</c:if>
			               </c:if>
			             
                 </dl>
                   <c:if test="${(fn:length(additionalServe.bedServes)+fn:length(additionalServe.chineseServes)+fn:length(additionalServe.westernServes)+fn:length(additionalServe.buffetServes)) gt 1}">
                    <div class="htod_more"><a href="javascript:void(0);">查看更多</a></div>
                   </c:if>
             </div>                           
             </c:if>    
            
          </div>
       </div>
       
      	 	<!-- 立即登陆的提示框 --> 
 			<div class="ht_fc"  id="loginMemberTipDiv" style="display:none">
              		<div class="ht_fc_top"><img src="http://himg.mangocity.com/img/h/2011/ht_fcsm.jpg" /></div>
              		<div class="ht_fc_main">
                   	商旅客户、一卡代理客户和其他参与了如返航空里程的联名合作客户等不享受现金奖励。
					</div>
            		<div class="ht_fc_b"><img src="http://himg.mangocity.com/img/h/2011/ht_fcsm_b.jpg" /></div>
 	    	</div>
    
   	    	<!-- 选择会籍提示框 -->
 	    	 <div id="chooseMemberCd"  class="choosediv" style="display:none;">
 	    	    <table width="100%" height="100%"  cellpadding="0" cellspacing="0" style="margin:0;padding:0;">
 	    	    	<tr>
 	    	    		<td width="100%" style="height:26px;">
 	    	    			<div class="choosettbg"><span onclick="hiddenDiv('chooseMemberCd');"  class="popclose" id="closesp"></span>
 	    	 		        <h3 id="choosetitle" ></h3></div>
 	    	    	    </td>
 	    	        </tr>
 	    	    	<tr><td><div style="width:100%;max-height:580px;overflow-y: auto;"><table width="84%" align="center" id="chooseTable" class="choosetb" ></table></div> </td></tr>
 	    	    	<tr ><td width="100%" align="center"><div id="choosebtdiv" class="choosebtdiv"></div></td></tr>
 	    	    </table>

 	    	 </div>    
 <%--=S 修改日期 --%>
<%@ include file="dateChangeDiv.jsp" %>
<%--=E 修改日期 --%>
<%-- 历史入住人的div --%>
<div id="fellowNameDiv" class="float_storey" style="position:absolute;display:none;"><ol id="fellowNameOl"></ol></div>
<%-- 历史联系人的div --%>
<div id="linkmanNameDiv" class="float_storey" style="position:absolute;display:none;"><ol id="linkmanNameOl"></ol></div>

<form id="submitForm" name="submitForm" action ="hotel-check.shtml" method="post" >
     <s:token />
      <%-- 日期，时间 --%>
        <!-- 记录游比比的平台号 -->
       <input type="hidden" name="trace_code" id="trace_code" value="${track_code}"/>
       <input type="hidden" name="projectCode" id="projectCode" value="${projectCode }"/>
        <input type="hidden" name="checkinDate"	value="<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkinDate}"/>"/>
        <input type="hidden" name="checkoutDate" id="checkoutDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkoutDate}"/>"/>
        <input type="hidden" name="days" value="${hotelOrderFromBean.days}" />
        <input type="hidden" name="arrivalTime" id="f_arrivalTime" value=""/>
		<input type="hidden" name="latestArrivalTime" id="f_latestArrivalTime" value=""/>
		<input type="hidden" name="latestArrivalDate" id="f_latestArrivalDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkoutDate}"/>"/>
		<input type="hidden" name="source" value="${hotelOrderFromBean.source}" />
       <%-- 酒店 --%>
	    <input type="hidden" name="cityId"  value="${hotelOrderFromBean.cityCode}" />
	    <input type="hidden" name="cityCode" value="${hotelOrderFromBean.cityCode}" />
	    <input type="hidden" name="hotelId" id="checkDup_hotelId"value="${hotelOrderFromBean.hotelId}" />
	    <input type="hidden" name="hotelName" id="hotelName"value="${hotelOrderFromBean.hotelName}" />
	    <input type="hidden" name="hotelStar" value="${hotelOrderFromBean.hotelStar}"/>
	    
	    <%--酒店提示信息--%>
	    <input type="hidden" name="tipInfo" value="${hotelOrderFromBean.tipInfo}"/>
	    
	    <%-- 房型 --%>
	    <input type="hidden" name="roomTypeId" id="checkDup_roomTypeId"value="${hotelOrderFromBean.roomTypeId}" />
	    <input type="hidden" name="roomTypeName" value="${hotelOrderFromBean.roomTypeName}" />
	    <input type="hidden" name="roomQuantity" id="f_roomQuantity" value="1" />
	    <input type="hidden" name="bedType" id="f_bedType" value="" />
	    <input type="hidden" name="bedTypeStr" id="bedTypeStr" value="${hotelOrderFromBean.bedTypeStr}" />
	    <input type="hidden" name="roomChannel" value="${hotelOrderFromBean.roomChannel}" />
	    
		<%-- 会员的 --%>
	    <input type="hidden" name="linkMan" id="f_linkMan" value="" />
        <input type="hidden" name="linkeManStr" id="f_linkeManStr" value="" />
        <input type="hidden" name="mobile" id="f_mobile" value="" />	
        <input type="hidden" name="email" id="f_email" value="" />	
	    
	    <input type="hidden" name="rate" id="rate" value="${rate}" />	
	    <input type="hidden" name="payMethod" id="f_payMethod" value="${hotelOrderFromBean.payMethod}" />
	    <input type="hidden" name="priceNum" value="${hotelOrderFromBean.priceNum}" />
	    <input type="hidden" name="firstNightSalePrice" value="${hotelOrderFromBean.firstNightSalePrice}" />
        <input type="hidden" name="childRoomTypeId" value="${hotelOrderFromBean.childRoomTypeId}" />
	    <input type="hidden" name="childRoomTypeName" value="${hotelOrderFromBean.childRoomTypeName}" /> 
	    <input type="hidden" name="currency" value="${hotelOrderFromBean.currency}"/>   
	    <input type="hidden" name="breakfastType" id="breakfastType" value="${hotelOrderFromBean.breakfastType}" />
	    <input type="hidden" name="breakfastNum" id="breakfastNum" value="${hotelOrderFromBean.breakfastNum}" />
	    <input type="hidden" name="payToPrepay"  id="payToPrepay" value="${hotelOrderFromBean.payToPrepay}" />
	    <input type="hidden" name="returnAmount" id="f_returnAmount" value="${hotelOrderFromBean.returnAmount}" />
	    <%-- 担保相关 --%>
	    <input type="hidden" name="needAssure" id="f_payNeedAssure" value="false" />
		<input type="hidden" name="assureDetailStr" id="f_assureDetailStr" value="" />
		
		 <%-- 修改取消信息 --%>
		<input type="hidden" name="cancelModifyItem" value="${bookhintCancelAndModifyStr}"/>
		                                  
	<input type='hidden'  value='' name= 'specialRequest' id ="f_specialRequest" />
	<input type='hidden'  value='M' name= 'title' id ="title" />
	<input type="hidden" name="clauseRule" id = "clauseRule" value="<c:out value='${reservation.clauseRule}' />" />
	 <input type="hidden" name="modifyField" id="modifyField" value="<c:out value='${reservation.modifyField}' />" />			
	 <input type="hidden" name="clauseStr" id="modifyField" value="<c:out value='${reservation.clauseStr}' />" />
	 <%-- modified by lixiaoyong 2009-05-13 v2.6 昨晚担保时间已经不采用数字方式，原来读取方式要改变--%>
	 
	 <input type="hidden" name="lateSuretyTime" id="modifyField" value="<c:out value='${reservation.lateSuretyTime}' />" />
	 <input type="hidden" name="reservation.assureLetter" id="modifyField" value="<c:out value='${reservation.assureLetter}' />" />
	 <input type="hidden" name="needCredit" id="modifyField" value="<c:out value='${reservation.needCredit}' />" />
	 <input type="hidden" name="unCondition" id="modifyField" value="<c:out value='${reservation.unCondition}' />" />		
	 <input type="hidden" name="overTimeAssure" id="modifyField" value="<c:out value='${reservation.overTimeAssure}' />" />
	 <input type="hidden" name="roomsAssure" id="modifyField" value="<c:out value='${reservation.roomsAssure}' />" />
	 <input type="hidden" name="rooms" id="modifyField" value="<c:out value='${reservation.rooms}' />" />
	 <input type="hidden" name="nightsAssure" id="modifyField" value="<c:out value='${reservation.nightsAssure}' />" />
	 <input type="hidden" name="nights" id="modifyField" value="<c:out value='${reservation.nights}' />" /> 
	 <input type="hidden" name="reservSuretyPrice" id="modifyField" value="<c:out value='${reservation.reservSuretyPrice}' />" />
	 <input type="hidden" name="balanceMode" id="modifyField" value="<c:out value='${reservation.balanceMode}' />" />
 	 <input type="hidden" name="advancePayTime" id="modifyField" value="<c:out value='${reservation.advancePayTime}' />" />
	 <input type="hidden" name="cancelModifyStr" id="modifyField" value="<c:out value='${reservation.cancelModifyStr}' />" />
	 <!-- modified by wuyun -->
	 <input type="hidden" name="prepayLimitType" id="modifyField" value="<c:out value='${reservation.prepayLimitType}' />" />
 	 <input type="hidden" name="firstPrice" id="modifyField" value="<c:out value='${reservation.firstPrice}' />" />
 	 
 	 <c:forEach items="${reservation.guarantees}" var="item" varStatus="i">
          <c:choose>
             <c:when test="${i.index==0}">
                <input type="hidden" name="night" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${item.night}" />" />
                <input type="hidden" name="notConditional" value="${item.notConditional}" />
                <input type="hidden" name="latestAssureTime" value="${item.latestAssureTime}" />
                <input type="hidden" name="overRoomNumber" value="${item.overRoomNumber}" />
                <input type="hidden" name="overNightsNumber" value="${item.overNightsNumber}" />
                <input type="hidden" name="assureType" value="${item.assureType}" />
                <input type="hidden" name="assureLetter" value="${item.assureLetter}" />
                <input type="hidden" name="assureCondiction" value="${item.assureCondiction}" />
             </c:when>
             <c:otherwise>
                <input type="hidden" name="night_${i.index}" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${item.night}" />" />
                <input type="hidden" name="notConditional_${i.index}" value="${item.notConditional}" />
                <input type="hidden" name="latestAssureTime_${i.index}" value="${item.latestAssureTime}" />
                <input type="hidden" name="overRoomNumber_${i.index}" value="${item.overRoomNumber}" />
                <input type="hidden" name="overNightsNumber_${i.index}" value="${item.overNightsNumber}" />
                <input type="hidden" name="assureType_${i.index}" value="${item.assureType}" />
                <input type="hidden" name="assureLetter_${i.index}" value="${item.assureLetter}" />
                <input type="hidden" name="assureCondiction_${i.index}" value="${item.assureCondiction}" />
             </c:otherwise>
          </c:choose>                       
        </c:forEach>
            
    <input type="hidden" name="guaranteeNum" id="guaranteeNum" value="${guaranteeNum}"/>
 	 
 	 <%--价格明细类 add by lixiaoyong v2.6 --%>
     <c:forEach items="${order.priceList}" var="item" varStatus="i">
          <c:choose>
             <c:when test="${i.index==0}">
                <input type="hidden" name="OrPriceDetail.night" value='<fmt:formatDate pattern="yyyy-MM-dd" value="${item.night}" />' />
                <input type="hidden" name="OrPriceDetail.dayIndex" value="${item.dayIndex}" />
                <input type="hidden" name="OrPriceDetail.roomState" value="${item.roomState}" />
                <input type="hidden" name="OrPriceDetail.quantity" value="${item.quantity}" />
                <input type="hidden" name="OrPriceDetail.salePrice" value="${item.salePrice}" />
                <input type="hidden" name="OrPriceDetail.basePrice" value="${item.basePrice}" />
                <input type="hidden" name="OrPriceDetail.marketPrice" value="${item.marketPrice}" />
                <input type="hidden" name="OrPriceDetail.breakfastStr" value="${item.breakfastStr}" />
                <input type="hidden" name="OrPriceDetail.dateStr" value="${item.dateStr}" />
                <input type="hidden" name="OrPriceDetail.hasReserv" value="${item.hasReserv}"/>
                <input type="hidden" name="OrPriceDetail.beforeTime" value="${item.beforeTime}" />
                <input type="hidden" name="OrPriceDetail.beforeDayNum" value="${item.beforeDayNum}" />
                <input type="hidden" name="OrPriceDetail.continueDay" value="${item.continueDay}" />
                <input type="hidden" name="OrPriceDetail.mustDate" value="${item.mustDate}" />
                <input type="hidden" name="OrPriceDetail.assureCond" value="${item.assureCond}" />
                <input type="hidden" name="OrPriceDetail.assureType" value="${item.assureType}" />
                <input type="hidden" name="OrPriceDetail.balanceMode" value="${item.balanceMode}" />
                <input type="hidden" name="OrPriceDetail.prepayTime" value="${item.prepayTime}"/>
             </c:when>
             <c:otherwise>
                <input type="hidden" name="OrPriceDetail.night_${i.index}" value='<fmt:formatDate pattern="yyyy-MM-dd" value="${item.night}" />' />
                <input type="hidden" name="OrPriceDetail.dayIndex_${i.index}" value="${item.dayIndex}" />
                <input type="hidden" name="OrPriceDetail.roomState_${i.index}" value="${item.roomState}" />
                <input type="hidden" name="OrPriceDetail.quantity_${i.index}" value="${item.quantity}" />
                <input type="hidden" name="OrPriceDetail.salePrice_${i.index}" value="${item.salePrice}" />
                <input type="hidden" name="OrPriceDetail.basePrice_${i.index}" value="${item.basePrice}" />
                <input type="hidden" name="OrPriceDetail.marketPrice_${i.index}" value="${item.marketPrice}" />
                <input type="hidden" name="OrPriceDetail.breakfastStr_${i.index}" value="${item.breakfastStr}" />
                <input type="hidden" name="OrPriceDetail.dateStr_${i.index}" value="${item.dateStr}" />
                <input type="hidden" name="OrPriceDetail.hasReserv_${i.index}" value="${item.hasReserv}"/>
                <input type="hidden" name="OrPriceDetail.beforeTime_${i.index}" value="${item.beforeTime}" />
                <input type="hidden" name="OrPriceDetail.beforeDayNum_${i.index}" value="${item.beforeDayNum}" />
                <input type="hidden" name="OrPriceDetail.continueDay_${i.index}" value="${item.continueDay}" />
                <input type="hidden" name="OrPriceDetail.mustDate_${i.index}" value="${item.mustDate}" />
                <input type="hidden" name="OrPriceDetail.assureCond_${i.index}" value="${item.assureCond}" />
                <input type="hidden" name="OrPriceDetail.assureType_${i.index}" value="${item.assureType}" />
                <input type="hidden" name="OrPriceDetail.balanceMode_${i.index}" value="${item.balanceMode}" />
                <input type="hidden" name="OrPriceDetail.prepayTime_${i.index}" value="${item.prepayTime}"/>
             </c:otherwise>
          </c:choose>                       
        </c:forEach>
    <input type="hidden" name="detailNum" id="detailNum" value="${detailNum}"/>	
    
    <%--取消修改信息类 add by lixiaoyong v2.6 --%>
     <c:forEach items="${reservation.assureList}" var="item" varStatus="i">
          <c:choose>
             <c:when test="${i.index==0}">
             	<input type="hidden" name="OrAssureItemEvery.type" value="${item.type}" />
             	<input type="hidden" name="OrAssureItemEvery.firstDateOrDays" value="${item.firstDateOrDays}" />
             	<input type="hidden" name="OrAssureItemEvery.firstTime" value="${item.firstTime}" />
             	<input type="hidden" name="OrAssureItemEvery.secondDateOrDays" value="${item.secondDateOrDays}" />
             	<input type="hidden" name="OrAssureItemEvery.secondTime" value="${item.secondTime}" />
             	<input type="hidden" name="OrAssureItemEvery.scope" value="${item.scope}" />
             	<input type="hidden" name="OrAssureItemEvery.deductType" value="${item.deductType}" />
             	<input type="hidden" name="OrAssureItemEvery.percentage" value="${item.percentage}" />
             	<input type="hidden" name="OrAssureItemEvery.night" value='<fmt:formatDate pattern="yyyy-MM-dd" value="${item.night}" />' />
             	<input type="hidden" name="OrAssureItemEvery.beforeDate" value='<fmt:formatDate pattern="yyyy-MM-dd" value="${item.beforeDate}" />' />
             </c:when>
             <c:otherwise>
            	<input type="hidden" name="OrAssureItemEvery.type_${i.index}" value="${item.type}" />
                <input type="hidden" name="OrAssureItemEvery.firstDateOrDays_${i.index}" value="${item.firstDateOrDays}" />
                <input type="hidden" name="OrAssureItemEvery.firstTime_${i.index}" value="${item.firstTime}" />
                <input type="hidden" name="OrAssureItemEvery.secondDateOrDays_${i.index}" value="${item.secondDateOrDays}" />
                <input type="hidden" name="OrAssureItemEvery.secondTime_${i.index}" value="${item.secondTime}" />
                <input type="hidden" name="OrAssureItemEvery.scope_${i.index}" value="${item.scope}" />
                <input type="hidden" name="OrAssureItemEvery.deductType_${i.index}" value="${item.deductType}" />
                <input type="hidden" name="OrAssureItemEvery.percentage_${i.index}" value="${item.percentage}" />
                <input type="hidden" name="OrAssureItemEvery.night_${i.index}" value='<fmt:formatDate pattern="yyyy-MM-dd" value="${item.night}" />' />
                <input type="hidden" name="OrAssureItemEvery.beforeDate_${i.index}" value='<fmt:formatDate pattern="yyyy-MM-dd" value="${item.beforeDate}" />' />
             </c:otherwise>
          </c:choose>                       
        </c:forEach>
    <input type="hidden" name="assureNum" id="assureNum" value="${assureNum}"/>
    <!-- 是否需要注册新会籍 -->
    <input type="hidden" name="isRegisterMember" id="isRegisterMember" value=""/>
     <input type="hidden" name="memberCd" id="memberCd" value="${member.membercd}"/>
     <input type="hidden" name="mbrID" id="mbrID" value="${member.id}"/>
     <input type="hidden" name="isReturnCash" id="isReturnCash" value=""/>
    </form>

<%-- 会员登录界面 --%>
<%@ include file="../commonjsp/member/memberlogin.jsp" %>
<jsp:include page="../commonjsp/_footer.jsp" flush="true" />


<script src="http://wres.mangocity.com/js/w/build/mgtool.js" type="text/javascript"></script>
<script src="http://wres.mangocity.com/js/w/module/datepicker.js" type="text/javascript"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/hotel2011.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/h_index_2012.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/jqhotel.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/jQuery_OperateSelector.js"></script>
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
<script type="text/javascript">
var fellowPersonArray = new Array();
var linkmanArray = new Array();
//fellowPersonArray.push(["dlcs","",""]);
//fellowPersonArray.push(["ljcs","",""]);
//fellowPersonArray.push(["3","",""]);
//fellowPersonArray.push(["4","",""]);

//linkmanArray.push(["1","01","001"]);
//linkmanArray.push(["2","02","002"]);
//linkmanArray.push(["3","03","003"]);
//linkmanArray.push(["4","04","004"]);
        <c:if test="${not empty member.fellowList}">
		       <c:forEach items="${member.fellowList}" var="oftenMember" varStatus="i"> 
		              fellowPersonArray.push(['${oftenMember.chiName}','${oftenMember.mobileNo}',null]);
		       </c:forEach>
        </c:if>  
        <c:if test="${not empty member.linkmanList}">
		       <c:forEach items="${member.linkmanList}" var="linkman" varStatus="i"> 
		              linkmanArray.push(['${linkman.linkPersonName}','${linkman.linkMobileNo}','${linkman.linkPersonEmail}']);
		       </c:forEach>
        </c:if>
var linkmanArraybak = linkmanArray;


function addHistorylinkman(){

   linkmanArray = new Array();
   var sameFlag=false;
   jQuery("#linkmanNameOl").empty();
   jQuery("input[name='fellowName']").each(
          function(i){
	          var thisValue = jQuery.trim(this.value);
	        
			  if(thisValue!="" && thisValue.indexOf('住客姓名') == -1 && validateFellowman(thisValue) && !sameFlag){	            
			    linkmanArray.push([thisValue,"",""]);	 
			  } 
   });
   var linkmanArrayBack=linkmanArray;
   var toVal=true;
    for(var k = 0 ; k < linkmanArraybak.length;k++){
        for(var r=0;r<linkmanArray.length;r++){
      		if(linkmanArray[r][0]==linkmanArraybak[k][0]){
      		   linkmanArray[r][1]=linkmanArraybak[k][1];
      		   toVal=false;
      		   break;
      		}else{
      		toVal=true;
      		}
      }
      if(toVal){
      linkmanArray.push([linkmanArraybak[k][0],linkmanArraybak[k][1],linkmanArraybak[k][2]]);
      }
    } 
    
    var m=0; 
    for(var i = 0 ; i < linkmanArray.length;i++){
          if(!validateFellowman(linkmanArray[i][0])){
             continue;
          }
          if(linkmanArray[i][0]!="" && linkmanArray[i][0].indexOf('住客姓名') == -1){
          m++;
          jQuery("#linkmanNameOl").append("<li class='storer_li' method='link_m' value='"+i+"' onclick='fzLinkInfo(this);'>"+linkmanArray[i][0]+"</li>");
          if(m%3==0){
             jQuery("#linkmanNameOl").append("<li class='clear_li'></li>");
          }
          if(m==6){
             break;
          } 
          }
   }
         
}

function fzLinkInfo(obj){      
         var thisValue = obj.value;
         var mobile = linkmanArray[thisValue][1];
         var email = linkmanArray[thisValue][2];
         jQuery("#linkmanTelephone").val(mobile);       
         jQuery("#linkmanEmail").val(email);
         jQuery("#linkmanName").val(linkmanArray[thisValue][0]);
         jQuery("#linkmanName").focus();
}

function validateFellowman(obj){
			

            var flag=true;
		    var thisValue = obj;
		    var reg1=/^[\u4e00-\u9fa5 A-Za-z]*$/;
		    var byteValLen=0;
		    var flagHKGOrMAC = jQuery("#flagHKGORMAC").val();
		   /*
		    if(flagHKGOrMAC=="true"){
		        var reg = /(^[A-Za-z .]+\/[A-Za-z .]+$)/;
		      
		        if(thisValue.indexOf('住客姓名') == -1){
		           flag =false;
		           return flag;
		        }    
		    }
		  */  
		    if(flagHKGOrMAC!="true"){
		    if(thisValue.indexOf('住客姓名') == -1 && !reg1.test(thisValue) && !(/(^[A-Za-z .]+\/[A-Za-z .]+$)/.test(thisValue))){		   
		           flag =false;
		           return flag;
		        }  
		    }
		    
		   
            if(thisValue=="" || thisValue.indexOf('房间N住客姓名') != -1){
				flag = false;
				return flag;
			}
			if(thisValue.indexOf('姓名') != -1){
				flag = false;
				return flag;
			}
			if(thisValue.length==1){
				flag = false;
				return flag;
			}
			
			for (var k = 0; k < thisValue.length; k++) {  
				if (thisValue.charAt(k).match(/[^\x00-\xff]/ig) != null)    
				byteValLen += 3;    
				else    
				byteValLen += 1;
			}    
			
			if(byteValLen>60){
				flag = false;
				return flag;
			}
		   for (var j = 0; j < thisValue.length; j++) {  
		      var c=1;
		      for(var t=1;t<=4;t++){
		         if(thisValue.charAt(j)==thisValue.charAt(j+t)){
		         c++;
		         }
		      }
		      if(c==4){
				flag = false;
				return flag;
		      }
		   }
		   			           
		   
		   return flag;		   
	  }
	
 	var assureMoney=${assureInfoVO.assureMoney};	  
	var maxRoomNum = ${reservation.rooms};	
	var assureType=${assureInfoVO.assureType};
	<c:if test="${hotelOrderFromBean.roomChannel == 9}">
	if(assureType==2){
		assureType=3;
	}else if(assureType==3){
		assureType=2;
	}else if(assureType==4){
		assureType=5;
	}
	</c:if>
	var maxNightNum = ${reservation.nights};
	var allDays=${days};
	var lastestAssureTime='${reservation.lateSuretyTime}';
	

	<c:if test="${hotelOrderFromBean.roomChannel == 9}">
		maxRoomNum = '${assureInfoVO.maxRoomNum}';
		lastestAssureTime='${assureInfoVO.firstRetentionTime}';
		document.getElementsByName("reservSuretyPrice")[0].value=assureMoney;
	</c:if>
	var hotelNameStr='${hotelOrderFromBean.hotelName}';	
	var rate=${rate};

	//将床型和最小配额数初始化为json
	function initBedTypeMinQuato(){
			var bedTypeMinQuatoStr='';
	<c:forEach items="${bedTypeMinQuatoMap }" var="bedTypeMinQuato">
		bedTypeMinQuatoStr=bedTypeMinQuatoStr+'\"bedtype'+${bedTypeMinQuato.key}+'\":'+${bedTypeMinQuato.value}+",";
	</c:forEach>
	var bedTypeMinQuatoLen=bedTypeMinQuatoStr.length;
	bedTypeMinQuatoStr=bedTypeMinQuatoStr.substring(0,bedTypeMinQuatoLen-1);
	bedTypeMinQuatoStr='{'+bedTypeMinQuatoStr+'}';
	return eval('('+bedTypeMinQuatoStr+')');
	}
	var bedTypeMinQuatoJson=initBedTypeMinQuato();
//处理返现和总金额

var allpriceSumForOneRoom = jQuery("#allPriceSum").html();
//保留一位小数
jQuery("#allPriceSum").html((Math.round(allpriceSumForOneRoom*10))/10);
var returnCashForOneRoom = jQuery("#returnCash").html();
returnCashForOneRoom=returnCashForOneRoom.replace(' 元','');
</script>
<script type="text/javascript" src="js/hotelbookJS/hotelbook-new.js"></script>

		<script type="text/javascript">
//加载床型列表到床型下拉框; add by shengwei.zuo 2009-10-26	 
function initBedType(){
		var  bedTypeStr = '${hotelOrderFromBean.bedTypeStr}';  
		var bedTypeArray = bedTypeStr.split(',');
		var bedTypeSelect=document.getElementById("bedtypeSelect");	
		for(i=0;i<bedTypeArray.length;i++){ 		
	           var bedType = bedTypeArray[i];
	           //1:双床
	           if (bedType == '2'){	           	                                           
                     var varItem = new Option("双床",bedType);                  	
                    bedTypeSelect.options.add(varItem);	                   
	           }
	           else if (bedType == '1'){	             		                                                      
                     var varItem = new Option("大床",bedType);                  	
                    bedTypeSelect.options.add(varItem);	                      	                  		                 	             
	           }else if (bedType == '3'){	              	                                 	                  
                    var varItem = new Option("单床",bedType);                  	
                    bedTypeSelect.options.add(varItem);	                  	                	             
	           }	           
	    }
	    var bigBedType=bedTypeStr.indexOf('1');
	    var doubleBedType=bedTypeStr.indexOf('2');
	    
	    //床型如果有多种不种床型，则默认"双床"，如果没有双床，则系统默认为大床，若大双床都没有，则系统随机默认，但当只有一种房型，则显示默认为该床型。
	    if(doubleBedType>=0){	    
	      bedTypeSelected('2');
	    }
	    else if(doubleBedType<0 && bigBedType>=0){
	    	bedTypeSelected('1');
	    }
	    else{
	    	bedTypeSelected('3');
	    }
	 	//add bed alert msg
	 	var channel = '${hotelOrderFromBean.roomChannel}';
	 	if(bedTypeStr!=null && bedTypeStr.indexOf(",")>-1 && channel=="9"){
	 		jQuery("#bedAlert").html("（此房间床型尽量安排，具体床型以到店后酒店安排为准！）");
	 	}
	 }
	 
	 //初始化默认的床型
	function bedTypeSelected(bedType){
	 jQuery('#bedtypeSelect').children("option").each(
	 function(){
	 var bedtypeValue=jQuery(this).val();
	 if(bedtypeValue==bedType){
	 $(this).attr("selected",true);
	 jQuery("#f_bedType").val(bedType)
	 }
	 }
	 );	
	}			

//设置预订房间数的下拉框中的值
 function setroomNumSelect(bedType){ 
 	var roomNum=bedTypeMinQuatoJson['bedtype'+bedType];
 	var roomNumSelect=document.getElementById("roomNumSelect");	
 	roomNumSelect.options.length=0;
 	for(var i=1;i<=roomNum;i++){
 		  var varItem = new Option(i+'间',i);                  	
          roomNumSelect.options.add(varItem);	
 	}
 	
 }

 //登陆会员不为空时，则客人姓名、联系人、联系电话、邮箱都成会员资料中获取。
 <c:choose>
	 <c:when test="${not empty member}">
	  
	 // jQuery("input[name='fellowName']").val('${member.name}');
	  //jQuery("input[name='fellowName']").attr('style','color:#000000');
	  //jQuery('#linkmanName').val('${member.name}');
	  //jQuery('#linkmanTelephone').val('${member.linkmobile}');
	 // jQuery('#linkmanEmail').val('${member.email}');
	 </c:when>
	 <c:otherwise>
	 jQuery("input[name='fellowName']:first").bind('blur',
     function (){
		if(jQuery("#fellowNameId1").val() != null && jQuery("#fellowNameId1").val().indexOf('姓名') == -1) {
			//jQuery('#linkmanName').val(jQuery("#fellowNameId1").val());
		}
	});
	 </c:otherwise>
 </c:choose>
//担保
 <c:if test="${not empty reservationAssist.assureInforList}">
		var total = '';
		var value = '';
	    <c:forEach items="${reservationAssist.assureInforList}" var="item">
			  value = '<c:out value="${item.assureDate}"/>'+'#'+'<c:out value="${item.assureRule}"/>'+'#'+
						'<c:out value="${item.assureType}"/>'+'#'+'<c:out value="${item.unconditionAssure}"/>'+'#'+'<c:out value="${item.unconditionAssureAmount}"/>'+'#'+
						'<c:out value="${item.overRoomsAssure}"/>'+'#'+'<c:out value="${item.overRoomsNum}"/>'+'#'+'<c:out value="${item.overRoomsAssureAmount}"/>'+'#'+
						'<c:out value="${item.overNightsAssure}"/>'+'#'+'<c:out value="${item.overNightsNum}"/>'+'#'+'<c:out value="${item.overNightsAssureAmount}"/>'+'#'+
						'<c:out value="${item.overTimeAssure}"/>'+'#'+'<c:out value="${item.overTimeStr}"/>'+'#'+'<c:out value="${item.overTimeAssureAmount}"/>'+'#'+
						'<c:out value="${item.fristDayAssureAmount}"/>';
			  value = value+"&";	
		      total += value;
		</c:forEach>
		document.getElementById("f_assureDetailStr").value = total;
	</c:if>
  
	 //预付
	 function initPrepaySubmitValue(){
	     <c:if test="${hotelOrderFromBean.payMethod=='pre_pay' || hotelOrderFromBean.payToPrepay}">
	          jQuery("#submitOrder").val("下一步");
	     </c:if>
	 }
	 
	 function setOnlyOneLatestArriveTime(){
	  <c:if test="${empty assureInfoVO.firstRetentionTime}">
	  var latestArriveTimeValue='${assureInfoVO.secondRetentionTime}';	 
	 	changeHintPoint(latestArriveTimeValue,"htchkin_point");
	 </c:if>	
	 }
	 
	 
	 //设置最晚到店时间提示，add by ting.li
	 function setSecondLatestTimehint(checkTimeId){
	 var hintText='房间保留至<fmt:formatDate pattern="MM月dd日" value="${hotelOrderFromBean.checkinDate}"/><font id="lastestArrirveTimeText">${assureInfoVO.firstRetentionTime }</font>；'+
	 '如果入住人不能在<font id="lastestArrirveTimeText1">${assureInfoVO.firstRetentionTime }</font>前到达酒店，请及时联系芒果网：';
	 
	 var hintTextOne='房间保留至<fmt:formatDate pattern="MM月dd日" value="${afterCheckInDate}"/><font id="lastestArrirveTimeText">'+
				 '01:00</font>；如果入住人不能在<font id="lastestArrirveTimeText1">次日凌晨1:00</font>前到达酒店，请及时联系芒果网：';
	 var hintTextTwo='房间保留至<fmt:formatDate pattern="MM月dd日" value="${hotelOrderFromBean.checkinDate}"/><font id="lastestArrirveTimeText">'+
				 '20:00</font>；如果入住人不能在<font id="lastestArrirveTimeText1">20:00</font>前到达酒店，请及时联系芒果网：';
	 var hintTextThree='房间保留至<fmt:formatDate pattern="MM月dd日" value="${hotelOrderFromBean.checkinDate}"/><font id="lastestArrirveTimeText">'+
				 '22:00</font>；如果入住人不能在<font id="lastestArrirveTimeText1">22:00</font>前到达酒店，请及时联系芒果网：';
	 
	 if(checkTimeId=='checkInTime1'){
	  jQuery("#htchkin_point_text").html(hintTextOne);
	  if('${hotelOrderFromBean.payMethod}'=='pay' && '${assureInfoVO.assureType}'=='0'){
	  jQuery("#f_specialRequest").val('次日凌晨 1:00入住  ');
	  }
	  
	  }
	  else if(checkTimeId=='checkInTime0'){
	  	jQuery("#htchkin_point_text").html(hintText);
	  }
	 else if(checkTimeId=='checkInTime2'){
	    jQuery("#htchkin_point_text").html(hintTextTwo);
	 }
	 else if(checkTimeId=='checkInTime3'){
	    jQuery("#htchkin_point_text").html(hintTextThree);
	 }
	 }
	function setLastestArrivalDate(){
	 	var afterCheckInDate='<fmt:formatDate pattern="yyyy-MM-dd" value="${afterCheckInDate}"/>';
	 	jQuery("#f_latestArrivalDate").val(afterCheckInDate);
	 	
	}
	
	function closeHtlChkin(){
	   jQuery(".htchkin_sm").hide();
	}

</script>

<%--酒店VRM（访客关系管理）二次营销 --%>
<s:if test='${application.OPEN_VRM} ==true'> 
<iframe src="http://serv1.vizury.com/analyze/analyze.php?account_id=VIZVRM128&param=h400&city=${hotelOrderFromBean.cityCode}&indate=<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkinDate}"/>&outdate=<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkoutDate}"/>&hotelid=${hotelOrderFromBean.hotelId}&rooms=&ad=&ch=&section=1&level=1" scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>
</s:if>
<%-- 这个放到最后，有百度监控，会影响速度--%>
<jsp:include page="../commonjsp/ _footer_monitor.jsp" flush="true" />
</body>
</html>
