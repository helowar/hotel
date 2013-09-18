<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="core"%>
<%@ taglib prefix="h" uri="convert"%>
<%@ taglib prefix="fn" uri="fn"%>
<%@ taglib prefix="f" uri="functions"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="${hotelInfo.chnName},${hotelInfo.chnName}预订." />
<meta name="description" content="${hotelInfo.chnName}: 芒果网提供${hotelInfo.chnName}预订并为您提供详细酒店介绍、酒店点评、酒店图片和各种房型的价格优惠。预订电话40066-40066" />
<title>${hotelInfo.chnName}-${hotelInfo.chnName}预订-芒果网</title>
<%
String projectCode;
projectCode = request.getParameter("projectcode");
if (projectCode != null && !projectCode.equals("")) {	
	Cookie cookieProject = new Cookie("projectcode",projectCode);
	cookieProject.setPath("/");
	response.addCookie(cookieProject);
}
%>
<link rel="stylesheet" type="text/css" href="css/advisor/daodao.css" />
<link rel="stylesheet" type="text/css" href="http://hres.mangocity.com/css/h/2011/hotel2011_vpic.css" />
<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/w/module/datepicker.css" />
<link rel="stylesheet" type="text/css" href="css/hotelsearch/jquery.autocomplete.css" />
<link rel="stylesheet" type="text/css" href=" http://hres.mangocity.com/css/jquery.alert.css"/>
<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/promotion/shendu/jquery.fancybox-1.3.4.css" media="screen" />
<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/w/module/mango_online.css" />
<link rel="stylesheet" href="http://wres.mangocity.com/css/w/module/cityselecter.css" type="text/css" />
<script type="text/javascript" src="http://wres.mangocity.com/js/home/cloud/js/jquery_mango.js"></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/promotion/jquery.mousewheel-3.0.4.pack.js"></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/promotion/jquery.fancybox-1.3.4.pack.js"></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/promotion/jquery.fancybox-iframe-1.3.4.js"></script>
<script type="text/javascript" src="js/hotelsearchJS/jquery.autocomplete.js"></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/h/banner/HotelHomeBanner.js"></script>
<script type="text/javascript" src="js/jQuery/jquery.alert.js"></script>
<script type="text/javascript" src="js/hotelinfo/hotelinfo.js"></script>
<script type="text/javascript" src="js/comment/daodao/daodao_hotel_info.js"></script>
<script type="text/javascript" src="http://www.mangocity.com/HotelCommentWeb/js/viewBig.js"></script>
<script type="text/javascript" src="http://www.mangocity.com/HotelCommentWeb/js/pic-display.js"></script>
<script type="text/javascript">  
     document.domain = "mangocity.com";
      
     function changeDiv(height){
      document.getElementById("detail_panel2").style.height=height+"px";
     }
     
     function changePage(obj){
        window.open('http://www.mangocity.com/HotelCommentWeb/hotel-comment-details.shtml?hotelId='+obj,'_blank','')
     }

  	 
  	 function changeParentPage(obj){
         parent.changePage(obj);
      }
      
       $(document).ready(function() {
		var recommendNum = parseInt($("#recommendSum").val());
		var noRecommendNum = parseInt($("#noRecommendSum").val());
		if(noRecommendNum==0 && recommendNum==0){
			$("#recommentdRate").html("0%");
		}else{
			$("#recommentdRate").html((recommendNum*100/(noRecommendNum+recommendNum)).toFixed(0) +"%");
		}
		
		$("a", "#wraper").each(function(){
		         $(this).fancybox({
				'overlayShow'	: false ,
				'transitionIn'	: 'elastic',
				'transitionOut'	: 'elastic'
			});
	       }); 
	       
       <c:if test="${pictureCount>5}">
               $('#more').bind('click',function(){
               		switchTab(4,4);
               });
        </c:if>
        readyfunc();
	  });
</script>
<style>
.kuang {
	border: 1px solid #ff6600;
	background:#fff;
	float: left;
	width: 542px;
	padding:10px 9px;
	margin-top:2px;
	line-height:150%
}
.quanjing_left {
	float: left;
    width: 400px;
	margin-bottom: 30px;
	margin-top: 10px;
}
.quanjing_right {
	float: right;
    width: 120px;	
	text-align: center;
	margin-top: 15px;
	margin-bottom: 30px;
}
.detail_nav{
overflow:hidden;
}
</style>
</head>

<body>
<%--=S head--%>
<script type="text/javascript" src="http://www.mangocity.com/include/css/tel.js"></script>
  <c:choose>
  	 <c:when test="${label == 'broad'}">
  		<jsp:include page="../commonjsp/_headerHK.jsp" flush="true" />
  	 </c:when>
  	 <c:otherwise>
  	 	<jsp:include page="../commonjsp/_header.jsp" flush="true" />
  	 </c:otherwise>
  </c:choose>
 <form action="">
	<input type="hidden" name="hotelId" id="hotelId" value="${hotelId }" />
	<input type="hidden" name="commentThete" value="${commentThete }" />
	<input type="hidden" name="totalCount" id="totalCount" value="${paginationSupport.totalCount}" />
	<input type="hidden" name="baseImgPath" id="baseImgPath" value="${sessionScope.basePath}" />
	<input type="hidden" name="recommendSum" id="recommendSum" value="${paginationSupport.recommendSum}" />
	<input type="hidden" name="noRecommendSum" id="noRecommendSum" value="${paginationSupport.noRecommendSum}" />
 </form>
 <form action="http://www.mangocity.com/HotelCommentWeb/hotel-comment-details.shtml" method="post" id="hotel-comment-details">
                 <input type="hidden"  name="hotelId" id="comment-details"/>
 </form>   
<%--=E head--%>
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
    <div class="crumbs">您现在的位置：<a href="http://www.mangocity.com" name="firstPage" class="green">首页</a> &gt; <a href="http://hotel.mangocity.com" name="domesticHotel" class="green">国内酒店</a> &gt;<a href="list-${fn:toLowerCase(hotelInfo.cityId)}.html" name="daohangcity" title="${hotelInfo.cityName}" class="green">${hotelInfo.cityName}酒店</a> &gt;<em>${hotelInfo.chnName}</em></div>
</div>
<%--=E top bar--%>

<form name="hotelBookForm" id="hotelBookForm" method="post" action="hotel-booking.shtml">
    <input type="hidden" name="priceTypeId" id="book_priceTypeId" value="" />
    <input type="hidden" name="payMethod" id="book_payMethod" value="" />
    <input type="hidden" name="inDate" id="book_inDate" value="" />
    <input type="hidden" name="outDate" id="book_outDate" value="" />
    <!-- 记录详情页面预订按钮的点击率 -->
	<input type="hidden" name="logId" id="logId" value="${logId}"/>
	<input type="hidden" name="projectcode" id="projectcode" value="${requestScope.projectcode}"/>
	<!-- 记录游比比平台号 -->
	<input type="hidden" name="track_code"  id="track_code" value="${track_code}"/>
</form>

<input type="hidden" name="channelcode" id="channelcode" value="${projectcode}"/>

<%--=S main --%>
		<div class="w960 cFloat">
			<div class="hn-main mgb10">
				<div class="detailwrap">				
			<!--==S detail_bar-->
        	<div class="detail_bar">
                <div class="detail_barL">
            	<div class="hotelname"><h1 class="${hotelInfo.commendType}">${hotelInfo.chnName}</h1>
            	      <c:choose>
						<c:when test="${fn:indexOf(hotelInfo.hotelStar,'star') ge 0}">
							<em class="hotelstar ${hotelInfo.hotelStar}"></em>
						</c:when>
						<c:otherwise>
							<em class="hotelstarName">&nbsp;&nbsp;${hotelInfo.hotelStar}</em>
						</c:otherwise>
					</c:choose>
				</div>
                <p class="ename">${hotelInfo.engName}</p>
               
                <p>酒店地址：
                <c:if test="${hotelInfo.cityShowFlag == true}">
                	<a href="list-${fn:toLowerCase(hotelInfo.cityId)}.html"name="cityNameInHotelAddress" title="${hotelInfo.cityName}">${hotelInfo.cityName}</a> </c:if> <c:if test="${hotelInfo.zooeShowFlag == true && hotelInfo.cityShowFlag == true}">${hotelInfo.zoneName}</c:if>${hotelInfo.chnAddress}
					(<a href="list-${fn:toLowerCase(hotelInfo.cityId)}-${fn:toLowerCase(hotelInfo.bizZone)}.html" name="bizzone" title="${hotelInfo.bizChnName}">${hotelInfo.bizChnName}</a>)
                <span id="emap_pic"><a href="http://hotel.mangocity.com/hotelEmap/jiudian-${hotelInfo.hotelId}.html" name="eMap"	>[查看地图]</a></span>
                </p>
                </div>
            </div>
            <!--==E detail_bar-->

			<!--==S hotel information-->
            <div class="detail_info w718">
            	<div class="viewshop">
                     <div class="viewshop_IF">
                       <c:choose>
										 
							<c:when test="${empty commnentStatistics}">
								<li>
									<b></b><b></b><b></b><b></b>
								</li>
								<li class='viewshop_orange'>
									&nbsp;&nbsp;&nbsp;暂无点评，欢迎抢沙发！
								</li>
							</c:when>
							
							<c:otherwise>
								<ul>
									<c:if test="${commnentStatistics.totalScore != 0.0}">
										<li class="viewshop_orange">
											酒店总评分：<font>${commnentStatistics.totalScore}</font>分<b>（满分5分）</b>
										</li>
									</c:if>
									<li class="viewshop_orange">
										酒店印象：
									</li>

									<c:forEach items="${impressionStatisticses}" var="impression" begin="0" end="3">
										<li>
											<span>${impression.impressionName }</span>
											<b>${impression.impressionNumber }</b> 人点评
										</li>
									</c:forEach>

								</ul>
								<p>
									共有
									<b class="orange">${commnentStatistics.commentNumber}</b>
									人点评
									<a name='detailComments' target="_blank"
										href="http://www.mangocity.com/HotelCommentWeb/hotel-comment-details.shtml?hotelId=${hotelId }">查看点评详情&gt;&gt;</a>
								</p>
								<p class="dd_wrap">
									<span class="recomm">${commnentStatistics.recommendNumber}人推荐</span>
									<span class="unrecomm red">${commnentStatistics.unrecommendNumber}人不推荐</span>
								</p>

							</c:otherwise>
											
						</c:choose>
                      <div class="detail_nav">
						<a name='speak' class="toreview green" target="_blank"
							href="http://www.mangocity.com/HotelCommentWeb/htl-comment_record.shtml?hotelId=${hotelId }">我要点评</a>
						</div>
                      </div>
                    </div>
                
                <div class="viewinfo">
                    <div class="viewinfo_pic">
                      <div class="pic_glass" id="pic_glass">
                      	<img src="${appearanceAlbumPicture }" />
                      </div>
                      <div class="pre_List" id="pre_List">
	                   	<c:if test="${pictureCount>5}">
	                             <a href="javascript:void(0)"  class="more" id="more">
	                                   更多图片&gt;&gt;
	                             </a>
	                    </c:if>
                      </div>
                    </div>
                	<dl>
                    	
                    	
                    	<dt><font class="daodao_c">交通信息：</font></dt>
                        <dd>
                        <c:forEach items="${hotelInfo.listTraffic}" var="traffic">
                        	距离<c:out value="${traffic.address}"></c:out>约<c:out
											value="${traffic.distance}"></c:out>公里;<br />
									</c:forEach>
									&nbsp;
                        </dd>
                    </dl> 

                </div>
                
            </div>
            <!--==E hotel information-->

					<%--==S hotel detail--%>
					<div class="detail_con w718" id="detail_con">
						<div class="detail_nav">
						 
						<c:if test="${hotelInfo.prepayHotel}">
						<div id="prepayHint${hotelInfo.hotelId }" class="ht_yfrx">会员预付可专享更多优惠！请拨打<font class="orange">4006640011</font>预订</div>						
						</c:if>	
					
							<a href="javascript:void(0);" name="hotelInfoAndPrice"
								class="don" id="tab1" onclick="switchTab(1,4)"><span>房型与价格</span>
							</a>
							<a href="#" name="trueCommends" class="dnormal" id="tab2"
								onclick="switchTab(2,4)"><span>真实住客点评</span>
							</a>
							<a href="#" name="hotelPicture" class="dnormal" id="tab4" onclick="switchTab(4,4)"><span>酒店图片（<em>${pictureCount}张</em>）</span></a>
						</div>
                        
					</div>
                    
						<div class="detail_panel" id="detail_panel1">

							<div class="hn-form">
								<form name="info_query" action="jiudian-${hotelInfo.hotelId}.html"
									method="post">
									<ul>
										<li class="w158">
											<label class="input_label">
												<input id="id_startDate1" name="inDate" type="text"
													class="w144 greytxt calendar  h_datepikcer"
													value="${inDate}" readonly="true" />
												<span id="sdatetip" class="hidetxt" style="display: none;">入住</span><span
													class="holitip"></span><a href="#" class="dateIcon"></a>
											</label>
										</li>
										<li class="w158">
											<label class="input_label">
												<input id="id_backDate1" name="outDate" type="text"
													class="w144 greytxt calendar  h_datepikcer"
													value="${outDate}" readonly="true" />
												<span id="edatetip" class="hidetxt" style="display: none;">离店</span><span
													class="holitip"></span><a href="#" class="dateIcon"></a>
											</label>
										</li>
										<input type="hidden" name="hotelId" id="id_hotelId"
											value="${hotelInfo.hotelId}" />
										<input type="hidden" name="" id="id_cityCode"
											value="${hotelInfo.cityId}" />
										<input type="hidden" name="" id="id_hotelName"
											value="${hotelInfo.chnName}" />
                          				<input type="hidden" id="promoteType" name="promoteType" value="${hotelInfo.promoteType}" />
										<li>
											<button class="btn59x26" type="submit">
												修改日期
											</button>
										</li>
									</ul>

								</form>
							</div>


							<div class="hoteldata" id="hoteldata">

							</div>
							<div class="dtprice">
								<h3>
									酒店简介
								</h3>
								<p>
									${hotelInfo.hotelIntroduce}
									<br/>				
								<font class="daodao_c">房间总数：${hotelInfo.layerCount}间</font>
								</p>
								<h3>
									预订特别提示
								</h3>
								<p>
									入住须知：规定入住时间：${hotelInfo.checkInTime}
									规定退房时间：${hotelInfo.checkOutTime}
									<br />
									付费说明：房费包含酒店服务费,不包括酒店其他费用、税收及客人额外要求的费用。
								</p>
								<h3>
									酒店设施
								</h3>
								<p>
									<c:if test="${not empty hotelInfo.roomFixtrue}">
                        		          <font class="daodao_c">酒店客房设施：</font><h:convert name="room_equipment"
											value="${hotelInfo.roomFixtrue}" />
									</c:if>
									<c:if test="${not empty hotelInfo.mealAndFixture}">
										<br /><font class="daodao_c">餐饮休闲设施：</font><h:convert name="hotel_liefallow"
											value="${hotelInfo.mealAndFixture}" />
									</c:if>
									<c:if test="${not empty hotelInfo.handicappedFixtrue}">
										<br /><font class="daodao_c">残疾人设施：</font><h:convert name="hotel_deformity"
											value="${hotelInfo.handicappedFixtrue}" />
									</c:if>
									
								</p>
								<h3>
									免费服务
								</h3>
								<p id="free_service">
								    <c:if test="${not empty hotelInfo.freeService}">
										<h:convert name="res_freeService"
											value="${hotelInfo.freeService}" />
									</c:if>
								</p>	
								<h3>
									可接受信用卡
								</h3>
								<p>
									<script>showCardCadre('<h:convert name="creditCardCadre" value="${hotelInfo.creditCard}"></h:convert>')</script>
								</p>
							</div>
						</div>

					<!-- 点评信息列表 by ting.li-->
					<div class="detail_panel" style="display: none;" id="detail_panel2">
						<div class="w960 cFloat" style="width: 710px;">
							<div class="hn-main mgb10" style="width: 710px;">
								<div class="cm_details"
									style="width: 710px; border-top: 0px; border-left: 0px; border-right: 0px; border-bottom: 0px;">
									<div id="comment-content">
										<div class="cm_list_page">
										
											<p class="dd_wrap">
												<span class="orange">已有<b class="orange">${paginationSupport.totalCount}</b>名住客发表点评</span>
												<span class="recomm"><font class="orange">${paginationSupport.recommendSum}人推荐</font>
												</span><span class="unrecomm"><font class="orange"
													style="color: red;">${paginationSupport.noRecommendSum}人不推荐&nbsp;&nbsp;推荐指数：<a id="recommentdRate"></a></font>
												</span>
											</p>
										</div>
										
										<c:if test="${paginationSupport.totalCount=='0' }">
											<font style="color: red;">&nbsp;酒店暂无点评记录！</font>
										</c:if>
										<div id="commentContent"></div>
										
										<div class="hotel_web_more"><a href="javascript:void(0);" onclick="changeParentPage('${hotelId}');">查看该酒店全部${paginationSupport.totalCount}条住客的点评>></a></div>

									</div>
								</div>

							</div>
						</div>
	
		
					</div>
					
					<!-- 点评信息列表结束 -->
					
					<%--==S 到到网点评 by jianping.pan--%>
                    <div class="detail_panel" style="display:none;height:850px;" id="detail_panel3">
                        <input id="daodao_id" type="hidden" value="${daoDaoBasicComment.daodaoId}"/>
                    </div>
                    <%--==E 到到网点评 by jianping.pan--%>

				<div class="detail_panel" style="display:none;" id="detail_panel4"></div>
					<%--==E hotel detail--%>
            
        </div>
        <%--==S 百分点推荐栏--%>
        <div id="bfd_rec_box"></div>
        <%--==E 百分点推荐栏--%>	
    </div>
 
 
 
    <div class="hn-sidebar">
    	 <%--==S 重新搜索酒店--%>
        <div class="hn-boxtop"><h2>重新搜索酒店</h2></div>
        <div class="hn-boxmid">
        
	            <div class="hn-form">
             <input type="hidden" name="memberPath" id="memberPath" value="${memberPath}" />
        	<form id="queryForm" name="queryForm" action="hotel-query.shtml" method="post">
                <ul>
                    <li class="w189"><label class="input_label"><input id="id_startCity" name="cityName" type="text" class="w182 greytxt MGcity" value="${hotelInfo.cityName}" datatype="hotel" />
                          <input type="hidden" id="cityCode" name="cityCode" value="${hotelInfo.cityId}" />
                    <span id="scitytip" class="hidetxt" style="display:none;">选择城市</span><a id="scityIcon" href="#" class="cityIcon"></a></label></li>
                    <li class="w189"><label class="input_label"><input id="id_startDate" name="inDate" type="text" class="w182 required greytxt calendar" value="${inDate}" readonly="true" /><span id="sdatetip" class="hidetxt" style="display:none;">入住</span><span class="holitip"></span><a href="#" class="dateIcon"></a></label>
                    </li>
                    <li class="w189"><label class="input_label"><input id="id_backDate" name="outDate" type="text" class="w182 required greytxt calendar" value="${outDate}" readonly="true" /><span id="edatetip" class="hidetxt" style="display:none;">离店</span><span class="holitip"></span><a href="#" class="dateIcon"></a></label>
                     </li>
                    
                    <li class="w189"><label class="input_label">
                     <input name="hotelName" id ="hotelName" type="text" value="您可以输入酒店名称查询" class="w182 greytxt"  onblur="showMes(this.id);" onclick="hiddenMes(this.id);" />
                    </label></li>
                    <li class="w189"><input type="button" class="btn92x26a" id="id_query" name="researchHotel" value="重新搜索" /></li>
                </ul>
                
            </form>   
            </div>

        </div>
        <div class="hn-boxbot mgb10"></div>
        <%--==E 重新搜索酒店--%>
         <%--==S passed hotel--%>
        <div class="hn-boxtop"><h2>最近浏览过的酒店</h2></div>
        <div class="hn-boxmid">
            <ul class="passhotel hotelviewed">
            	<c:forEach  items="${hotelNameAndIdLis}" var="list">
            		<li><a href="jiudian-${list[0]}.html" name="currencyViewedHotels"><c:out value="${list[1]}"></c:out></a></li>
                </c:forEach>
            </ul>
        </div>
        <div class="hn-boxbot mgb10"></div>
        <%--==E  passed hotel--%>
         <%--==S 商业区酒店--%>    
        <div class="hn-boxtop">
            <div id="hotelNav">
                <h2 class="navl"><span class="on" id="button_bizZonehtl_show" onclick="javascript:changeZone(1)">商业区酒店</span></h2>
                <h2 class="navr"><span class="" id="button_distincthtl_show" onclick="javascript:changeZone(2)">行政区酒店</span></h2>
            </div>
        </div>
        
        <div id="hotelPanel" style="" class="hn-boxmid">
            <div class="panelist" style="display" id="show_bizZonehtl">
                <ul class="passhotel hotelviewed">
                <c:forEach items="${hotelInfo.cityBizZoneMap.business}" var="entry">
			       <li> <a href="list-${fn:toLowerCase(hotelInfo.cityId)}-${fn:toLowerCase(entry[3])}.html" name="bizZone" title="<c:out value="${entry[1]}" />"><c:out value="${entry[1]}" /></a></li>
			     </c:forEach>
                </ul>
            </div>
            
            <div class="panelist" style="display:none" id="show_distincthtl">
                <ul class="passhotel hotelviewed">
                     <c:forEach items="${hotelInfo.cityBizZoneMap.district}" var="entry">
				       <li> <a href="list-xingzheng-${fn:toLowerCase(hotelInfo.cityId)}-${fn:toLowerCase(entry[3])}.html" name="distinct" title="<c:out value="${entry[1]}" />"><c:out value="${entry[1]}" /></a></li>
				     </c:forEach>
                </ul>
            </div>            
        </div>
        <div class="hn-boxbot mgb10"></div>
        <%--==E 商业区酒店--%>
        <%--==E  行政区酒店--%>
        <div id="hotelBanner"></div>
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
<%--=E main --%>

<%--=S hot city --%>
<%@include  file="/WEB-INF/jsp/commonjsp/staticjsp/morehotcities.jsp" %>
<%--=E hot city --%>



<%--=S footer--%>
<jsp:include page="../commonjsp/_footer.jsp" flush="true" />
<%--=E footer --%>
<%-- 地图浮动层 --%>
<div id="id_Emap" style='display:none'>
    <div class="dlu" id="loadMap">
    </div>
 </div>

<%-- 会员信息start --%>
<input type="hidden" name="memberPath" id="memberPath" value="${memberPath}" />
 <div class="hotellogin" id="id_fudong" style='display:none'>
    <div class="dlu" id="testmember">
    </div>
 </div>
<%-- 会员信息end --%> 


<script type="text/javascript">
		function picShow(obj){
		$("a", "#"+obj).each(function(){
	         $(this).fancybox({
			'overlayShow'	: false ,
			'transitionIn'	: 'elastic',
			'transitionOut'	: 'elastic'
		    });
	      });
	    }  
		//初始点评菜单栏化样式
		function setClass(){
			$('.detail_nav a:gt(0)').attr('class','dnormal');
		}
		
		
	function computerCount(recordid,type){	 
	   if(document.getElementById(recordid+"_"+type+"Flag").value==0){
	   var val=0;
	   if(type==1){
	      val=parseInt($('#'+recordid+"_effectiveId").val());
	   }else{
	      val=parseInt($('#'+recordid+"_noeffectiveId").val());
	   }
	   var url="hotel-information!computerCount.shtml";
	   var parms="hotelId=${hotelId}&recordId="+recordid+"&effective="+type;
	   jQuery.ajax({
				url:url,
				type:'post',
				dataType:'text',
				contentType:'application/x-www-form-urlencoded',
				data:parms,
				error: function(XMLHttpRequest, textStatus, errorThrown) {
                        alert('XMLHttpRequest.status='+XMLHttpRequest.status);
                        alert('XMLHttpRequest.readyState='+XMLHttpRequest.readyState);
                        alert('textStatus='+textStatus);
                    },
				success:function(data){
				
				$('#'+recordid+"_1Flag").val(1);
                $('#'+recordid+"_0Flag").val(1);	
                if(data=='1' && type==1){	
				  $("#"+recordid+"a").html(val+1);
				  $('#'+recordid+"_effectiveId").val(val+1);				  
				  $('#'+recordid+"_0Style").css('color','#666');
				 }
				if(data=='1' && type==0){			
				  $("#"+recordid+"b").html(val+1)
                  $('#'+recordid+"noeffectiveId").val(val+1); 
                  $('#'+recordid+"_1Style").css('color','#666');

				 }
				 if(data=='2'){			
				 $("#"+recordid+"c").html("感谢您参与点评，请让您的食指休息一下吧！");				  
				 }
			   }
			   }
			  );
	   }else{
	      	      	      	      	      
	      $("#"+recordid+"c").html("感谢您参与点评，请让您的食指休息一下吧！");				
	   }
	}
	
 		function getComment(num){
			$("#commentContent").html("&nbsp;&nbsp;评论加载中...<br/>");
			jQuery.getJSON("http://www.mangocity.com/HotelCommentWeb/hotel-comment-details!list.shtml?pageNo=1&hotelId="+$("#hotelId").val()+"&sumRow="+$("#totalCount").val()+"&callback=?", function(data){
				var jsonData = data[0];
				appendCmt(jsonData);
				$('.infiniteCarousel').infiniteCarousel();
			});
 		}
 
        function appendCmt(rs){
       		if(rs.comments==null || rs.comments=='undefined'){
       			$("#commentContent").html(rs.msg);
       			return;
       		}
       		var htmlStr = "";
       		for(var i=0;i<rs.comments.length;i++){
       			var cmt = rs.comments[i];
  				htmlStr += '<div class="detail_panel">';
  				htmlStr += '<div class="hotel_web">';
  				//hotel_web_user
  				htmlStr += '<div class="hotel_web_user"><span>点评发表于：' 
  					+ cmt.commentTime + '&nbsp;来源：'
  					+ cmt.commentSource+'</span>'+cmt.commentName+'</div>';
  				//hotel_web_grade
  				if((cmt.avgScore+'') != '.0'){
  					htmlStr += '<div class="hotel_web_grade">评分：<span class="orange">' + cmt.avgScore + '分</span><p>';
					if(cmt.recommend=='1')	htmlStr+='<span class="recomm orange">推荐</span>';
  					else if(cmt.recommend=='0')	htmlStr+='<span class="unrecomm red">不推荐</span>';
                    if(cmt.scoreList!=null && cmt.scoreList.length>0){
                    	for(var a=0;a<cmt.scoreList.length;a++){
                      		htmlStr += cmt.scoreList[a].gradedName+'：<span class="orange">'+cmt.scoreList[a].graded+'分</span>';
                    	}
                    }
                    
                    htmlStr += '</p></div>';
  					//hotel_web_tag
  					var impressionArr = cmt.commentImpression.split("##");
  					if(impressionArr!=null && impressionArr.length>0){
	   					var tag = "";
	   					for(var d=0;d<impressionArr.length;d++){
	   						if(impressionArr[d]!=null && impressionArr[d]!=''){
	   							tag += '<span>'+impressionArr[d]+'</span>';
	   						} 
	   					}
	   					if(tag.length>0){
	   						htmlStr += '<div class="hotel_web_tag">酒店印象： '+tag+'</div>';
	   					}
  					}
  					//hotel_web_pic
  					if(cmt.smallImg!=null && cmt.smallImg.length>0){
   					   htmlStr += '<div class="hotel_web_pic" style="height:90px;">上传的酒店照片：'
                        	+ '<div class="infiniteCarousel">'
                          	+ '<div class="wrapper" id="picShow'+cmt.commentid+'">'
                           	+ '<input type="hidden" name="picSums" value="'+cmt.smallImg.length+'" />'
                           	+ '<input type="hidden" name="commentPicId" value="'+cmt.commentid+'" />'                                            
                           	+ '<input type="hidden" name="currentPageNum" value="1" id="currentPage'+cmt.commentid+'"/>'
                       		+ '<ul>';
                       var baseImgPath = $("#baseImgPath").val()+"/";
                       for(var i=0;i<cmt.smallImg.length;i++){
                       		htmlStr += '<li style="width:134px;">'
                         		+ '<a href="'+baseImgPath+cmt.orignalImg[i].addr+'" id="picAid'+i+'">'
                           		+ '<img name="Pic_'+cmt.commentid+''+i+'" src="'+baseImgPath+cmt.smallImg[i].addr
                           		+ '"  class="hasBigimg" onclick="picShow(\'picShow'+cmt.commentid+'\');" />'
                           		+ '</a></li>';
                       }
   					   htmlStr += '</ul></div></div><div style="display:none">';
                       for(var i=0;i<cmt.bigImg.length;i++){
                         	htmlStr += '<img src="'+baseImgPath+cmt.bigImg[i].addr+'"  id="bigPic'+cmt.commentid+''+i+'" />';
                       }
   					   htmlStr += '</div><div style="display:none">';
                       for(var i=0;i<cmt.orignalImg.length;i++){
                         	htmlStr += '<img src="'+baseImgPath+cmt.orignalImg[i].addr+'"  id="originalImg'+cmt.commentid+''+i+'" />';
                       }
                       htmlStr += '</div></div>';
  					}
  				}
  				//hotel_web_feel
                htmlStr += '<div class="hotel_web_feel">';
                if((cmt.avgScore+'') == '.0'){
                	if(cmt.recommend=='1') htmlStr += '<span class="recomm orange">推荐</span>';
                	else if(cmt.recommend=='0') htmlStr += '<span class="unrecomm red">不推荐</span>';
                }
                htmlStr += '入住感受：<p>'+cmt.content+'</p>';
                if(cmt.hotelRevertContent!='' ||cmt.mangoRevertContent!=''){
               		htmlStr += '<div class="hotel_web_reply"><div class="hotel_web_top"></div><div class="hotel_web_bottom">';
               		if(cmt.hotelRevertContent!=''){
               			htmlStr += '<p><span class="orange">酒店回复：</span><br />'+cmt.hotelRevertContent+'</p>';
               		}
               		if(cmt.mangoRevertContent!=''){
               			htmlStr += '<p><span class="orange">芒果网回复：</span><br />'+cmt.mangoRevertContent+'</p>';
               		}
               		htmlStr += '</div></div>';
                }
                htmlStr += '</div>';
                 	
                htmlStr += '<input type="hidden" id="'+cmt.recordid+'_effectiveId" value="'+cmt.commentValidTotal +'"/>';
                htmlStr += '<input type="hidden" id="'+cmt.recordid+'_1Flag" value="0"/>';
                htmlStr += '<input type="hidden" id="'+cmt.recordid+'_0Flag" value="0"/>';
                htmlStr += '<input type="hidden" id="'+cmt.recordid+'_noeffectiveId" value="'+cmt.commentInvalidTotal+'"/>'
                 
				//hotel_web_shop
				htmlStr += '<div class="hotel_web_shop">您认为住客的点评：'
  							+'<a name="useful" href="javascript:void(0);" onclick="computerCount(\''+cmt.recordid+'\',1)" id="'+cmt.recordid+'_1Style" >'       							                                      
                   		+'有用（<span id="'+cmt.recordid+'a">'+cmt.commentValidTotal+'</span>）</a>'                   
                   		+'<a name="useless" href="javascript:void(0);" onclick="computerCount(\''+cmt.recordid+'\',0)" id="'+cmt.recordid+'_0Style">'
                   		+'无用（<span id="'+cmt.recordid+'b">'+cmt.commentInvalidTotal+'</span>）</a>'
                     +'&nbsp;&nbsp;<span id="'+cmt.recordid+'c" style="color:red;"></span></div>';
                   
				//hotel_web end
				htmlStr += '</div>';
				
				//detail_panel end
  				htmlStr += '</div>'; 		
		}
       	$("#commentContent").html(htmlStr);
    }
 
			 
	function readyfunc(){
		setClass();
		$('.detail_nav a:eq(1)').attr('class','don');
		$('.detail_nav a:gt(0)').click(function(){
			setClass();
			$(this).attr('class','don');
		});
		if($("#totalCount").val()!="0"){
			getComment(1);
		}
	}
    </script>
    
<script type="text/javascript" src="http://wres.mangocity.com/js/home/cloud/js/city.hotel.js"></script>
<script src="http://wres.mangocity.com/js/w/build/mgtool.js" type="text/javascript"></script>
<script src="http://wres.mangocity.com/js/w/module/cityselecter.js" type="text/javascript"></script>
<script src="http://wres.mangocity.com/js/w/module/datepicker.js" type="text/javascript"></script>

<script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/h_index_2012.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2012/hotel2012.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2012/justforhotelcity.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/hotelquery.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/mangocity.simplemodal.js"></script>
<script type="text/javascript" src="js/hotellist/hotellist.js"></script>
<script type="text/javascript" language="javascript" src="hotelInfoGWT/hotelInfoGWT.nocache.js"></script>

<script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/viewbigpic.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/hotelImgScan.js"></script>
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
 
	
		var moreList = ${moreList};
		
		
		var dateList = ${dataList};
		 //more mudule				
		 simpleSan( moreList, function( obj ){
		      /* hotelImgScan.setIndex( $( obj ).attr( "name" ));
				hotelImgScan.freshInfo();
				hotelImgScan.fixed(); */
		  });
		//img scanner  
		var hotelImgScan = new hotelImgScan({ dateList: dateList, fixedNum: 4});
		$( "#detail_panel4" ).html( hotelImgScan.block );
  
</script>
<%-- S在线客服代码--%>
<%-- E在线客服代码--%>
<%--start-酒店VRM（访客关系管理）二次营销 --%>
<s:if test='${application.OPEN_VRM} ==true'> 
<input type="hidden" id="vrm_param"  value="h300" />
<input type="hidden" id="hotelStar"  value="${hotelInfo.hotelStar}"/>
<input type="hidden" id="lp"  value="http://hotel.mangocity.com/hotel-information.shtml?hotelId=${hotelInfo.hotelId}" />
<script type="text/javascript" src="js/vrm/vizury.js"></script>
</s:if>
<%--end-酒店VRM（访客关系管理）二次营销 --%>


<%--start-百分比--%>
<input type="hidden" name="cashbackrate" id="cashbackrate" value="${cashbackrate}" />
<s:if test='${application.OPEN_BFD} ==true'> 
<script type="text/javascript" src="js/baifendian/ordersource.js"></script>
<input type="hidden" id="chnAddress" name="chnAddress" value="${hotelInfo.chnAddress}" />
<input type="hidden" id="zone_name" name="zone_name" value="${hotelInfo.zoneName}" />
<script type="text/javascript" src="js/baifendian/bfd_hotel_info.js"></script>
</s:if>
<%--end-百分比--%>

  <%-- 这个放到最后，有百度监控，会影响速度--%>
<jsp:include page="../commonjsp/ _footer_monitor.jsp" flush="true" />
</body>
</html>
