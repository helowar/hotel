<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
	<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
	<script type="text/javascript" src="js/hotelinfo/hotelmapinfobox.js"></script>
</head>
<body onload="initialize('${hotelInfo.latitude}','${hotelInfo.longitude}','${hotelInfo.chnName}')">
<div id="map_canvas" style="width:560px;height:385px;"></div>
</body>
</html>