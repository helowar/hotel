<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="core"%>
<%String contextPath=request.getContextPath();
%>
<c:if test='${payLastFlag!=null && payLastFlag!="" }'>
		<input type="hidden" name="payLastFlag" id="payLastFlag" value="<c:out value='${payLastFlag}'/>" />
</c:if>
