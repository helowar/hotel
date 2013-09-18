<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
      <ul class="passhotel hotelviewed">
      	<c:forEach  items="${hotelNameAndIdLis}" var="list">
      		<li><a href="jiudian-${list[0]}.html"><c:out value="${list[1]}"></c:out></a></li>
          </c:forEach>
      </ul>
</body>
</html>