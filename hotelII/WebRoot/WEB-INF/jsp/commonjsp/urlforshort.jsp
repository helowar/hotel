<%@ page contentType="text/html;charset=utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String basePathTop = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
String wresPath = "http://wres.mangocity.com/";
String wimgPath = "http://wimg.mangocity.com/";
String imgPath = "http://himg.mangocity.com/";
String resPath = "http://hres.mangocity.com/";
pageContext.setAttribute("path",path);
request.setAttribute("basePath",basePath);
request.setAttribute("basePathTop",basePathTop); 
request.setAttribute("wresPath",wresPath);
request.setAttribute("wimgPath",wimgPath);
request.setAttribute("imgPath",imgPath);
request.setAttribute("resPath",resPath);
%>
