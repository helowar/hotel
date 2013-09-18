$(function(){
  var daodaoTotalCommentNum = $("#daodao_total_comment").val();
  var daodaoId = $("#daodao_id").val();
  if(daodaoTotalCommentNum==0){
     var height = 98;
   $("#detail_panel3").css("height",height);
   }
   $("#tab3").click(function(){
    $("#detail_panel3").html('<iframe id ="iframe_daodao" src="http://www.daodao.com/WidgetEmbed-daodaopropertydetail?locationId='+daodaoId+'&partnerId=DFE6971EC86842CC9CB3C33C57208049&display=true" scrolling="no"  marginheight="0" marginwidth="0" frameborder="0" width="100%"  height="100%"></iframe>');
   });
   
   var freeServices = $.trim($("#free_service").html());
   var freeServiceArray = new Array();
   freeServiceArray=freeServices.split(",");
   if(freeServiceArray.length>1){
      var freeServiceStr="";
      for(var i=0;i<freeServiceArray.length;i++){
         freeServiceStr=freeServiceStr+(i+1)+","+freeServiceArray[i]+"; ";
      }
      freeServiceStr=freeServiceStr.substring(0,freeServiceStr.length-2);
      $("#free_service").html(freeServiceStr);
  }
});