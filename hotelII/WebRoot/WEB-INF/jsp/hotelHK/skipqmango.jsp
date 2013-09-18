<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>【酒店预订】-芒果网</title>
</head>
<style type="text/css">
html, body, div, p, h1, h2, h3, h4, h5, h6, blockquote,
ol, ul, li, dl, dt, dd, 
form, fieldset, legend, button, input, textarea, 
pre, code, th, td, map{
   margin:0;
   padding:0;
}
img{border:none;}
body, button, input, select, textarea{ font:12px \5b8b\4f53, Arial, Helvetica, sans-serif;}

h1, h2, h3, h4, h5, h6{ font-size:100%;}

ul, ol, li{ list-style:none;}

a{ text-decoration:none;}
.container{width:500px; margin:0 auto;}
.pic1{ width:304px; h72px; margin:50px auto; margin-bottom:90px;}
.pic2{ width:403px; height:114px; margin:10px auto;}
.font16{ color:#333; line-height:30px; margin:10px 0; font-size:16px; text-align:center;}
.font14{ color:#333; line-height:30px; margin:10px 0; font-size:14px; text-align:center;}
.font12{ color:#777; line-height:18px; margin:10px 0; font-size:12px; text-align:center;}
.orange{color:#ff6600;}
</style>
<script type="text/javascript">
function blink() {
	document.getElementById('blinkNote').style.display = 'none';
	window.location.replace('${hotelURL}');
}
</script>
<body onLoad="blink()">
<div class="container" id="blinkNote">
   <div class="pic1"><img src="http://wimg.mangocity.com/img/h/2011/zj_logo.jpg" /></div>
   <p class="font16"><b>您预订的是：</b><b class="orange">${chnName}</b></p>
   <p class="font14">芒果网将为您转至青芒果网站，青芒果是芒果网旗下经济类酒店在线预订品牌</p>
   <div class="pic2"><img src="http://wimg.mangocity.com/img/h/2011/querywaiting.gif" /></div>
   <p class="font12">青芒果将为您提供在线预订及售后服务</p>
   <P class="font12">芒果网展示的价格仅供参考，具体信息请以青芒果网站为准</P>
</div>

<iframe id="qhotelInfo" src="${hotelURL}" width="100%" height="100%" style="display:none" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no"></iframe>

</body>
</html>
