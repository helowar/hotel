<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="../commonjsp/paginationCC.jsp"%>

<html>
	<head>
		<title>酒店确认单_芒果网</title>
		<link href="../css/history.css" rel="stylesheet" type="text/css">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta content="all" name="robots" />
		
		<meta name="description" content="芒果网提供全国三百多个城市，超过五千家酒店宾馆的查询预订服务。从芒果网预订酒店，不管是商务酒店，还是假日酒店，您都可享受便宜的折扣价格和高品质的服务">
		<meta name="keywords" content="酒店，宾馆，内地酒店，港澳酒店，海外酒店，集体订房，酒店预订，预订酒店，商务酒店，假日酒店">
		<style type="text/css">
		        #contain{
					width: 970px;
					margin-right: auto;
					margin-left: 0px;
				
				}
				
				#mid{
					width: 970px;
					float: left;
					padding: 0px;
					background-color: #FBF1C0;
				}
				#midleft ul{
					margin: 0px;
				}
				
				#midleft{
					width: 228px;
					float: left;
					height: auto;
					text-align: center;	
					background-color: #FBF1C0;
				}
				#midleft1{
					width: 217px;
					margin-right: auto;
					margin-left: auto;
					margin-top: 8px;
				}
				#midleft1zi{
					border: 1px solid #E8C976;
					background-color: #FFE493;
					text-align: left;
					font-size: 12px;
					line-height: 20px;
					padding-left: 8px;
				
				}
				#midleft1zi li{
					display: block;
					height: 19px;
					margin-right: 5px;
					margin-left: 5px;
					text-align: left;
					border-bottom-width: 1px;
					border-bottom-style: solid;
					border-bottom-color: #ECD180;
					padding-top: 3px;
					list-style-type: none;
				
				
				}
				#midleft1title2{
					margin: 0px;
					font-size: 15px;
					text-align:left;	
					padding-left: 27px;
					padding-top:6px;
					background-image: url(../images/commertleft1title_bg.jpg);
					wdith:217px;
					height:27px;
					background-repeat: no-repeat;
					background-position: left top;
				}
				
				#midleft2{
					margin-top: 8px;
					margin-bottom: 8px;
					width: 217px;
					margin-right: auto;
					margin-left: auto;
					text-align: left;
					font-size: 12px;
					line-height: 20px;
				
				}
				#midright{
					float: right;
					width: 742px;
					background-color: #FFFFFF;
				
				}
				
				
				
				
				#commentleft1{
					background-color: #F9EFB4;
					margin-top: 5px;
					margin-right: 2px;
					margin-left: 2px;
					text-align: left;
					margin-bottom: 4px;
				
				}
				.hotlekuang {
					background-color: #FFFFFF;
					border: 1px solid #999999;
					margin-top: 3px;
					margin-bottom: 3px;
				
				}
				#midleft2zi{
					margin-right: 8px;
					margin-left: 8px;
				}
				#midleft4zi{
					background-color: #FFF7D0;
					border-top-width: 1px;
					border-right-width: 1px;
					border-bottom-width: 1px;
					border-left-width: 1px;
					border-top-style: none;
					border-right-style: solid;
					border-bottom-style: solid;
					border-left-style: solid;
					border-top-color: #E8C976;
					border-right-color: #E8C976;
					border-bottom-color: #E8C976;
					border-left-color: #E8C976;
					margin-bottom: 8px;
					padding: 5px;
					text-align: center;
					font-size: 12px;
					line-height: 20px;
				
				}
				#midleft4zi li{
					text-align: left;
					display: block;
					background-color: #FBF1C0;
					border: 1px solid #FFF7D0;
					list-style-type: none;
				
				}
				
				
				
				td {
					font-size: 12px;
					color: #333333;
					line-height: 150%;
				
				}
				body {
					text-align: center;
					font-size: 12px;
					color: #333333;
					margin: 0px;
				}
				a:link {
					font-size: 12px;
					color: #333333;
					text-decoration: none;
				}
				a:visited {
					font-size: 12px;
					color: #666666;
					text-decoration: none;
				}
				a:hover {
					font-size: 12px;
					color: #990000;
					text-decoration: none;
				}
				a:active {
					font-size: 12px;
					color: #990000;
					text-decoration: none;
				}
				
				.price_color {
					color: #990000;
				}
						        
		</style>	
	</head>
	
	<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
		<OBJECT id=WebBrowser classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 height=0 width=0></OBJECT> 
		
		<table width="100%">
			<tr>
				<td align="center">
					<a href="#" onclick="document.all.WebBrowser.ExecWB(4,1);">另存为</a>
				</td>
			</tr>
		</table>
<form id="222f">
		<table width="900" class="table_bg" align="center" border="1" cellpadding="1" cellspacing="0">
			<tr>
		  		<td id="eMailHtml" valign="top" height="400px">
		  			
		  			<div id="eMailHtml222">
		  				${mailContent }
		  			</div>
				</td>
		  	</tr>
		</table>
		</form>
	</body>
</html>
