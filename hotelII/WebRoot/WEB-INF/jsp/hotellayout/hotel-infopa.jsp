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
<link rel="stylesheet" type="text/css" href="http://hres.mangocity.com/css/h/2011/hotel2011.css" />
<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/w/module/datepicker.css" />
<link rel="stylesheet" type="text/css" href="css/hotelsearch/jquery.autocomplete.css" />
<link rel="stylesheet" type="text/css" href=" http://hres.mangocity.com/css/jquery.alert.css"/>
<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/promotion/shendu/jquery.fancybox-1.3.4.css" media="screen" />
<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/w/module/mango_online.css" />
<script type="text/javascript" src="http://wres.mangocity.com/js/home/cloud/js/jquery_mango.js"></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/promotion/jquery.mousewheel-3.0.4.pack.js"></script>
<script type="text/javascript" src="http://wres.mangocity.com/js/promotion/jquery.fancybox-iframe-1.3.4.js"></script>
<script type="text/javascript" src="js/hotelsearchJS/jquery.autocomplete.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/banner/HotelHomeBanner.js"></script>
<script type="text/javascript" src="js/jQuery/jquery.alert.js"></script>
<script type="text/javascript" src="js/hotelinfo/hotelinfo.js"></script>

<style type="text/css">
	.hta_yfrx{
		float: right;
	    font-size: 16px;
	    font-weight: bold;
	    line-height: 26px;
	}
</style>

<script type="text/javascript">  
     document.domain = "mangocity.com";
      
     function changeDiv(height){
      document.getElementById("detail_panel2").style.height=height+"px";
     }
     
     function changePage(obj){
        window.open('http://www.mangocity.com/HotelCommentWeb/hotel-comment-details.shtml?hotelId='+obj,'_blank','')
     }
     // 当房间总数为0时，不显示房间总数
     $(document).ready(function(){
  		var id_roomAmount1 = document.getElementById("id_roomAmount1");
  		var id_roomAmount2 = document.getElementById("id_roomAmount2");
  		var layerCount = ${hotelInfo.layerCount};
  		if(layerCount < 1){
  			id_roomAmount1.style.display = "none";
  			id_roomAmount2.style.display = "none";
  		}
  	 });
  	 
  	 function changeParentPage(obj){
         parent.changePage(obj);
      }
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
</form>

<%--=S main --%>
		<div class="w960 cFloat">
			<div class="hn-main mgb10">
				<div class="detailwrap">
					<!--==S detail_bar-->
					<div class="detail_bar">
						<p class="toolbar">
							<a
								href="http://hotel.mangocity.com/jiudian-${hotelInfo.hotelId}.html"
								name="stowHotel"
								onclick="window.external.addFavorite(this.href,'${hotelInfo.chnName}：${hotelInfo.cityName}酒店预订首选-芒果网');return false;"
								title="${hotelInfo.chnName}：${hotelInfo.cityName}酒店预订首选-芒果网"
								rel="sidebar">收藏酒店</a>
							<a href="#" onclick="javascript:print()" name="print"
								class="print">打印</a>
						</p>
						<div class="hotelname" style="line-height: 24px">
							<h1 class="${hotelInfo.commendType}">
								${hotelInfo.chnName}
							</h1>
							<c:choose>
								<c:when test="${fn:indexOf(hotelInfo.hotelStar,'star') ge 0}">
									<em class="hotelstar ${hotelInfo.hotelStar}"></em>
								</c:when>
								<c:otherwise>
									<em class="hotelstarName">&nbsp;&nbsp;${hotelInfo.hotelStar}</em>
								</c:otherwise>
							</c:choose>
						</div>
						<p class="ename">
							${hotelInfo.engName}
						</p>
					</div>
					<%--==E detail_bar--%>

					<%--==S hotel information--%>
					<div class="detail_info w718">
						<div class="view360">
							<div class="viewrap">
								<div class="imgWrap">
									<img
										src="http://himg.mangocity.com/img/upload/Photo/${hotelInfo.outPictureName}"
										alt="${hotelInfo.chnName}" width="180" height="180" />
								</div>
								<p class="txtWrap">
									<c:if test="${not empty hotelInfo.hotelPictureList}">
									    <c:set var="flag" value="0" />
									    <c:forEach items="${hotelInfo.hotelPictureList}" var="picInfoItem" varStatus="i">
                                             <c:if test="${picInfoItem.pictureType eq '3'}">
                                                 <c:set var="flag" value="1" />
                                             </c:if>
                                        </c:forEach>
									    <c:choose>
									      <c:when test="${flag eq 1}">
									        <a href="javascript:void(0)" name="hotelPictures"
											    onclick="hotelPicPanel()">酒店图片(<c:out
												value="${fn:length(hotelInfo.hotelPictureList)-1}"></c:out>张)</a>
									      </c:when>
									      <c:otherwise>
									        <a href="javascript:void(0)" name="hotelPictures"
											    onclick="hotelPicPanel()">酒店图片(<c:out
												value="${fn:length(hotelInfo.hotelPictureList)}"></c:out>张)</a>
									      </c:otherwise>
									    </c:choose>
									</c:if>
									<c:choose>
										<c:when test="${not empty hotelInfo.hotel360PictureList}">
											<a href="javascript:void(0)" name="hotelPic360"
												onclick="hotelPic360Panel()"><span class="gline">|</span>
												360度全景</a>
										</c:when>
										<c:otherwise>
											<span class="gline">|</span> 360度全景
			            		</c:otherwise>
									</c:choose>
								</p>
							</div>
						</div>

						<div class="viewinfo">
							<dl>
								<dt>
									所属商圈：
								</dt>
								<dd>
									<a
										href="list-${fn:toLowerCase(hotelInfo.cityId)}-${fn:toLowerCase(hotelInfo.bizZone)}.html"
										name="bizzone" title="${hotelInfo.bizChnName}">${hotelInfo.bizChnName}</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<a href="#" name="eMap"
										onclick="googleMapPanel('${hotelInfo.hotelId}')">电子地图</a>
								</dd>

								<dt>
									酒店地址：
								</dt>
								<dd>
									<a href="list-${fn:toLowerCase(hotelInfo.cityId)}.html"
										name="cityNameInHotelAddress" title="${hotelInfo.cityName}">${hotelInfo.cityName}</a>${hotelInfo.zoneName}${hotelInfo.chnAddress}
								</dd>

								<dt>
									联系方式：
								</dt>
								<dd>
									<span id="mtel"><a href=""
										onclick="switchDisplay();return false;" name="hotelTelephone">40066-40066(7x24小时服务)</a>
									</span><span id="tel" style="display: none">&nbsp;&nbsp;酒店联系电话：<a>${hotelInfo.telephone}</a>
									</span>
								</dd>

								<dt id="id_roomAmount1">
									房间总数：
								</dt>
								<dd id="id_roomAmount2">
									${hotelInfo.layerCount}间
								</dd>

								<dt>
									开业时间：
								</dt>
								<dd>
									${hotelInfo.praciceDate}
								</dd>

								<dt>
									最近装修：
								</dt>
								<dd>
									<c:if test="${empty hotelInfo.fitmentDate}">${hotelInfo.praciceDate}</c:if>
									<c:if test="${not empty hotelInfo.fitmentDate}">${hotelInfo.fitmentDate}</c:if>
								</dd>

								<dt>
									特色设施：
								</dt>
								<dd>
									<p class="dd_wrap">
										<c:choose>
											<c:when test="${hotelInfo.forPlane}">
												<em class="spc_icon spc_01_on" title="有接机服务"></em>
											</c:when>
											<c:otherwise>
												<em class="spc_icon spc_01_off" title="无接机服务"></em>
											</c:otherwise>
										</c:choose>
										<c:choose>
											<c:when test="${hotelInfo.forFreeGym}">
												<em class="spc_icon spc_03_on" title="有免费健身设施"></em>
											</c:when>
											<c:otherwise>
												<em class="spc_icon spc_03_off" title="无免费健身设施"></em>
											</c:otherwise>
										</c:choose>
										<c:choose>
											<c:when test="${hotelInfo.forFreeStop}">
												<em class="spc_icon spc_04_on" title="有免费停车场"></em>
											</c:when>
											<c:otherwise>
												<em class="spc_icon spc_04_off" title="无免费停车场"></em>
											</c:otherwise>
										</c:choose>
										<c:choose>
											<c:when test="${hotelInfo.forFreePool}">
												<em class="spc_icon spc_05_on" title="有免费游泳池"></em>
											</c:when>
											<c:otherwise>
												<em class="spc_icon spc_05_off" title="无免费游泳池"></em>
											</c:otherwise>
										</c:choose>
									</p>
								</dd>
								<dt>
									交通信息：
								</dt>
								<dd>
									<c:forEach items="${hotelInfo.listTraffic}" var="traffic">
                        	距离<c:out value="${traffic.address}"></c:out>约<c:out
											value="${traffic.distance}"></c:out>公里;<br />
									</c:forEach>
									&nbsp;
								</dd>
							</dl>
							<div class="viewshop">

								<!-- 酒店点评统计信息 by ting.li-->
								<div id="hotelEvaluation">
									<div class="viewshop_IF">
								
										<c:choose>
										 
											<c:when test="${empty commnentStatistics}">
												<li>
													<b></b><b></b><b></b><b></b>
												</li>
												<li class='viewshop_orange'>
													暂无点评，欢迎抢沙发！
												</li>
											</c:when>
											
											<c:otherwise>
												<ul>
													<li class="viewshop_orange">
														酒店总评分：<font>${commnentStatistics.totalScore}</font>分<b>（满分5分）</b>
													</li>
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

							</div>
						</div>
						<!-- 点评统计信息结束 -->
					</div>
					<%--==E hotel information--%>

					<%--==S hotel detail--%>
					<div class="detail_con w718">
						<div class="detail_nav">
							<a href="javascript:void(0);" name="hotelInfoAndPrice"
								class="don" id="tab1" onclick="switchTab(1,2)"><span>酒店详情</span>
							</a>
							
						</div>
                        
					</div>
                    
						<div class="detail_panel" id="detail_panel1">
							<div class="dtprice">
								<h3>
									酒店详细介绍
								</h3>
								<p>
									${hotelInfo.hotelIntroduce}
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
									酒店设施与服务
								</h3>
								<p>
									<c:if test="${not empty hotelInfo.roomFixtrue}">
                        		酒店客房设施：<h:convert name="room_equipment"
											value="${hotelInfo.roomFixtrue}" />
									</c:if>
									<c:if test="${not empty hotelInfo.mealAndFixture}">
										<br />餐饮休闲设施：<h:convert name="hotel_liefallow"
											value="${hotelInfo.mealAndFixture}" />
									</c:if>
									<c:if test="${not empty hotelInfo.handicappedFixtrue}">
										<br />残疾人设施：<h:convert name="hotel_deformity"
											value="${hotelInfo.handicappedFixtrue}" />
									</c:if>
									<c:if test="${not empty hotelInfo.freeService}">
										<br />酒店免费服务：<h:convert name="res_freeService"
											value="${hotelInfo.freeService}" />
									</c:if>
								</p>
								<h3>
									酒店可接受信用卡类型
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
										<%@include file="hotel-comment-detail.jsp"%>
									</div>
								</div>

							</div>
						</div>

					</div>

            
        </div>
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
                    <li class="w189"><label class="input_label"><input id="id_startDate" name="inDate" type="text" class="w182 required greytxt calendar" value="${inDate}" /><span id="sdatetip" class="hidetxt" style="display:none;">入住</span><span class="holitip"></span><a href="#" class="dateIcon"></a></label>
                    </li>
                    <li class="w189"><label class="input_label"><input id="id_backDate" name="outDate" type="text" class="w182 required greytxt calendar" value="${outDate}" /><span id="edatetip" class="hidetxt" style="display:none;">离店</span><span class="holitip"></span><a href="#" class="dateIcon"></a></label>
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
  <c:if test="${not empty hotelInfo.hotelPictureList}">
    <div id="hotelPic" style="display:none">
      <div class="kuang">
        <div class="quanjing_left"><img id="hotelPicBig" src="http://himg.mangocity.com/img/upload/Photo/${hotelInfo.hotelPictureList[0].pictureName}" width="400" height="300" /></div>
        <div class="quanjing_right">
        <table width="100" border="0" cellpadding="0" cellspacing="0">
          <c:forEach items="${hotelInfo.hotelPictureList}" var="picInfoItem" varStatus="i">
          <c:if test="${picInfoItem.pictureType != '3' and not empty picInfoItem.pictureName}">
	  <tr>
	    <td align="center">
	    <img class="hotelPicSmall" style="cursor:pointer; " src="http://himg.mangocity.com/img/upload/Photo/${picInfoItem.pictureName}" width="82" height="60" pictype="${picInfoItem.pictureType}" />
	    <br/><c:choose><c:when test="${picInfoItem.pictureType eq '0'}">酒店外观照片</c:when><c:when test="${picInfoItem.pictureType eq '1'}">酒店大堂照片</c:when><c:when test="${picInfoItem.pictureType eq '2'}">酒店房间照片</c:when></c:choose><br/>
	    </td>
	  </tr>	
	  </c:if>
	  </c:forEach>
	</table>
	</div>
      </div>
    </div>
   </c:if>
   <c:if test="${not empty hotelInfo.hotel360PictureList}">
   <div id="showPic360" style="display:none">
        <div class="kuang">
          <div class="quanjing_left" style="width:340px;">
          	<iframe name="inserthotel" src="hotel/Hotel_3Dshow.jsp?hotelName=${f:encode(hotelInfo.chnName,"UTF-8")}&title=${f:encode(hotelInfo.hotel360PictureList[0].title,"UTF-8")}&bigPic=${f:encode(hotelInfo.hotel360PictureList[0].bigPic,"UTF-8")}" width="340" height="380" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no"></iframe>
          </div>
         <div class="quanjing_right" style="width:190px;">
          <table width="190" border="0" cellpadding="0" cellspacing="0">
          <c:forEach items="${hotelInfo.hotel360PictureList}" var ="HPL" varStatus="HPLIndex">
          <c:if test="${HPLIndex.count%2 ==1}">
     	  	 <tr>
     		</c:if>	
          <td>
          <table>
           <tr>
     	  		<td  align="center">
        			<span style="cursor: hand" onClick="inserthotel.location.href='hotel/Hotel_3Dshow.jsp?hotelName=${f:encode(hotelInfo.chnName,"UTF-8")}&title=${f:encode(HPL.title,"UTF-8")}&bigPic=${f:encode(HPL.bigPic,"UTF-8")}'">
        			<c:choose>
        			<c:when test="${HPL.smollPic ==null||HPL.smollPic==''}"><img src="http://himg.mangocity.com/img/no-photo-1.jpg" width="80" height="60" border="0"/> </c:when>
        			<c:otherwise><img src="http://himg.mangocity.com/img/upload/Photo/${HPL.smollPic}"  width="80" height="60" border="0"/></c:otherwise>
        			</c:choose>
        			</span> 
    			</td>
    		</tr>
    			<tr>
    			<td align="center" height="40">
       			<span style="cursor: hand" href="javascript:void(0);" onClick="inserthotel.location.href='hotel/Hotel_3Dshow.jsp?hotelName=${f:encode(hotelInfo.chnName,"UTF-8")}&title=${f:encode(HPL.title,"UTF-8")}&bigPic=${f:encode(HPL.bigPic,"UTF-8")}'">
             			<c:out value="${HPL.title}"></c:out>
       			</span>
    			</td>
    			
    		</tr>
    		</table>
    		</td>
    		<c:if test="${HPLIndex.count%2 ==0}">
     	  	 </tr>
     		</c:if>
     		</c:forEach>
		</table>
	</div>
   </div>
  </div>
 </c:if>

<%-- 会员信息start --%>
<input type="hidden" name="memberPath" id="memberPath" value="${memberPath}" />
 <div class="hotellogin" id="id_fudong" style='display:none'>
    <div class="dlu" id="testmember">
    </div>
 </div>
<%-- 会员信息end --%> 

<%-- 酒店VRM（访客关系管理）二次营销--%>
<iframe src="http://serv1.vizury.com/analyze/analyze.php?account_id=VIZVRM128&param=h300&city=${hotelInfo.cityId}&indate=${inDate }&outdate=${outDate }&hotelid=${hotelInfo.hotelId }&rooms=&ad=&ch=&section=1&level=1" scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>

<script type="text/javascript" src="http://wres.mangocity.com/js/home/cloud/js/city.hotel.js"></script>
<script src="http://wres.mangocity.com/js/w/build/mgtool.js" type="text/javascript"></script>
<script src="http://wres.mangocity.com/js/w/module/datepicker.js" type="text/javascript"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/hotel2011.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/h_index_2012.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/hotelquery.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/mangocity.simplemodal.js"></script>
<script type="text/javascript" src="js/hotellist/hotellist.js"></script>
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
 //iframe 大图展示 start----------------------------------------------
	 function imgMagnifierOnIframe(frameID,divID){
		 var iframe = document.getElementById(frameID);  
		 iframeEventHandle(frameID,divID);
		 }
		 		
		function iframeEventHandle(frameID,divID){		    
			$("#"+divID+" a",document.getElementById(frameID).contentWindow.document).each(function(){
			         $(this).fancybox({
			        'frameID'       : 'frameTest',
					'overlayShow'	: false ,
					'transitionIn'	: 'elastic',
					'transitionOut'	: 'elastic'
				});
		       }); 
			}
</script>
</body>
</html>
