<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
<div class="hn-boxtop"><h2>查询结果</h2></div>
 <div class="hn-boxmid">
     <ul class="passhotel">
       <c:forEach items="${hotelBasicInfoMap}" var="entry">
        <li><c:out value="${entry.value.chnName}" /></li>
     </c:forEach>
     </ul>
 </div>
</body>
</html>