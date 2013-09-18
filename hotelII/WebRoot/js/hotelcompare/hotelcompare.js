function closeThisDiv(object){
     var parentDiv = $(object).parent().parent();
     $("a:[name='closeThisDiv']").each(function(i){
         $(this).remove();
     });
     parentDiv.attr("style","display:none");     
}

	// 地图浮动层
function googleMapPanel(hotelId) { 
    var a = '<iframe src="http://www.mangocity.com/hotelEmap/hotel-information-map.shtml?hotelId='+hotelId+'" name="framename" width="100%" marginwidth="0" height="100%" marginheight="0" scrolling="No" frameborder="no" id="framename" border="0"></iframe>';
    $("#loadMap").html(a);
	$("#id_Emap").modal({
		closeHTML : '<div class="guanbi"><a title="Close" class="closeMode" style="cursor:pointer;" target="_blank">关闭</a></div>',
		position : ["25%"],
		overlayId : 'contact-overlay',
		closeClass : ('closeMode'),
		overlayClose: true
	});	
}
