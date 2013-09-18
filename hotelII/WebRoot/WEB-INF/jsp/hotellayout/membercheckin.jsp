<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
<div class="hn-boxtop"><h2>点评酒店列表</h2></div>
 <div class="hn-boxmid">
     <ul class="passhotel">
     <c:if test="${empty memberList}">
     	您最近没有已入住过的酒店
     </c:if>
     <c:forEach items="${memberList}" var="hotelNameAndId">
			<li>
			  <span class="hoteltit"><a href="http://www.mangocity.com/HotelCommentWeb/htl-comment_record.shtml?hotelId=<c:out value="${hotelNameAndId[1]}"/>"><c:out value="${hotelNameAndId[0]}"/></a></span> 
			  <a href="http://www.mangocity.com/HotelCommentWeb/htl-comment_record.shtml?hotelId=<c:out value="${hotelNameAndId[1]}"/>" class="green reviews">点评</a>
		   </li>
      </c:forEach>
        
     </ul>
 </div>  
 <div class="hn-boxbot mgb10"></div>
</body>
</html>