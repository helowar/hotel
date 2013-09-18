<%
	 /** ********************************************************
	 @author zhengxin
	 @project myoa
	 @version v0.1
	 @purpose provide context variable for other jsp to utilize.
	 @created at 2007-05-07 08:00
	 @edited by zhengxin
	 ********************************************************** */
%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="core"%>
<%@ taglib prefix="fmt" uri="fmt"%>
<%@ taglib prefix="fn" uri="fn"%>
<%@ taglib prefix="h" uri="convert"%>


<%
	String contextPath = request.getContextPath();
	String systemName = "mangocity";
	String imagePath = contextPath + "/image";
	
	
	String commonPath = contextPath + "/common";
	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	
	request.setAttribute("imagePath", "image");
	//fix lousy code add by chenjiajie 2009-06-16
	response.setContentType("text/html;charset=utf-8"); 
	request.setCharacterEncoding("utf-8");
	
%>
<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/w/module/datepicker.css" />
<script src="<c:url value="/js/hotelordermanage/validation-framework.js" />"></script>

<script src="<c:url value="/js/commonJS/common.js" />"></script>

<%-- <script src="<c:url value="/js/hotelordermanage/calendar.js" />"></script>--%>

<script src="<c:url value="/js/hotelordermanage/resource.js" />"></script>

<script src="<c:url value="/js/hotelordermanage/control.js" /> "> </script>

<script src="<c:url value="/js/hotelordermanage/xselector.js" /> "> </script>

<script src="<c:url value="/js/hotelordermanage/dateUtil.js" /> "> </script>

<script src="<c:url value="/js/hotelordermanage/hotel.js" /> "> </script>

<script src="<c:url value="/js/hotelordermanage/order.js" /> "> </script>

<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/hotelsearch/styles.css" />" />

<script src="<c:url value="/dwr/engine.js" />"></script>
<script src="<c:url value="/dwr/util.js" />"></script>



<script>

  
   function errorHandler(msg) {
      alert("DWR remote call error:" + msg);
   }

   dwr.engine.setErrorHandler(errorHandler);
   
   
   //dwr.util.toDescriptiveString( example,text, 012 )  
    
</script>


