<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>My JSP 'zixun.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
        <ul class="news">
        	<li><a href="http://news.mangocity.com/6406.html" target="_Blank" title="">【酒店聚焦】芒果网成“第二届亚青会酒店预订服务类供应商”</a></li>
            <li><a href="http://news.mangocity.com/6400.html" target="_Blank" title="">【酒店聚焦】中国与塞舌尔签署中塞互免签证协议 可停留30天</a></li>
            <li><a href="http://news.mangocity.com/6300.html" target="_Blank" title="">【酒店聚焦】去哪儿网酒店比价功能几近丧失</a></li>
            <li><a href="http://news.mangocity.com/6409.html" target="_Blank" title="">【酒店聚焦】北京园博会“首日限量票”半价开订</a></li>
        </ul>
        <a href="http://www.mangocity.com/promotion/hotel_zixun/index.html" target="_Blank" class="more_news">更多资讯&gt;&gt;</a>
  </body>
</html>
