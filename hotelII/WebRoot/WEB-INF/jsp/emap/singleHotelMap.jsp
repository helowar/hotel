<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%-- 地图浮动层 --%>
<div id="id_Emap" style='display:none'>
    <div class="dlu" id="loadMap">
    </div>
 </div>
<script type="text/javascript">
// 地图浮动层
function googleMapPanel(hotelId) { 
    var a = '<iframe src="hotel-information-map.shtml?hotelId='+hotelId+'" name="framename" width="100%" marginwidth="0" height="100%" marginheight="0" scrolling="No" frameborder="no" id="framename" border="0"></iframe>';
    $("#loadMap").html(a);
	$("#id_Emap").modal({
		closeHTML : '<div class="guanbi"><a title="Close" class="closeMode" style="cursor:pointer;" target="_blank">关闭</a></div>',
		position : ["25%"],
		overlayId : 'contact-overlay',
		closeClass : ('closeMode'),
		overlayClose: true
	});	
} 
//酒店图片浮动层
</script>
