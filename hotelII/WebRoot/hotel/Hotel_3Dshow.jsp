<%@ page language="java" contentType="text/html; charset=utf-8"%>

<html>
	<head>
<title>酒店预订_芒果网</title>
<meta name="description" content="芒果网提供全国三百多个城市，超过五千家酒店宾馆的查询预订服务。从芒果网预订酒店，不管是商务酒店，还是假日酒店，您都可享受便宜的折扣价格和高品质的服务">
<meta name="keywords" content="酒店，宾馆，内地酒店，港澳酒店，海外酒店，集体订房，酒店预订，预订酒店，商务酒店，假日酒店">
<style type="text/css">
.hotelname {
	font-size: 16px;
	font-weight: bold;
	color: #FFFFFF;
}
</style>
	</head>
	<%
		request.setCharacterEncoding("UTF-8");
		String name = request.getParameter("hotelName");
		if(name==null || name.trim().length()<=0)
		{
    		name="";
		}

		String title = request.getParameter("title");
		if(title==null || title.trim().length()<=0)
		{
    		title="";
		}
		String bigPic = request.getParameter("bigPic");
	%>
	<body leftmargin="0" topmargin="0">
		<table width="340" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td height="20">
					&nbsp;

				</td>
			</tr>
			<tr>
				<td>
					<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
						codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0"
						width="340" height="320">
						<param name="movie"
							value="show.swf?fileName=../upload/Images/<%=bigPic%>" />
						<param name="quality" value="high" />
						<embed src="show.swf?fileName=../upload/Images/<%=bigPic%>"
							quality="high"
							pluginspage="http://www.macromedia.com/go/getflashplayer"
							type="application/x-shockwave-flash" width="340" height="320"></embed>
					</object>
				</td>
			</tr>
		</table>
	</body>
</html>
