<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="fmt" uri="fmt"%>
 <div id="member_div" style='display:none;position:absolute;'>
    <div  id="member_subDiv">
    <iframe frameborder="0" src="http://www.mangocity.com/mbrweb/hotelMiniLogin/init.action" name="framename" style="width:214px;height:250px;padding:-1px" scrolling="No"  border="0"></iframe>
    </div>
 </div>
 
 <form id="memberForm" name="memberForm"  action ="hotel-booking.shtml" method="post">
     <input type="hidden" name="inDate"  value='<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkinDate}"/>' />
     <input type="hidden" name="outDate"  value='<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkoutDate}"/>' />
     <input type="hidden" name="priceTypeId"  value="${hotelOrderFromBean.childRoomTypeId}" />
     <input type="hidden" name="payMethod"  value="${hotelOrderFromBean.payMethod}" />
 </form>
 <script type="text/javascript">
 
  function memberLoginPanel(obj,marginTop,marginLeft) { 
	var pos = $(obj).offset();
	jQuery("#member_div").css(
	{
	'top':pos.top+marginTop,
	'left':pos.left+marginLeft
	}
	);
	jQuery("#member_div").show();
	
  }
    
 jQuery(document).click(function(e){                       
   jQuery("#member_div").hide();
});
  jQuery("#loginMember").click(
       function(event){
         event=event||window.event;  
         event.stopPropagation();
         memberLoginPanel(this,40,0);
  });
  
    jQuery("#loginMemberHref").click(
       function(event){
         event=event||window.event;  
         event.stopPropagation();
         memberLoginPanel(this,20,0);
  });
  jQuery("#loginMemberForAct").click(
       function(event){
         event=event||window.event;  
         event.stopPropagation();
         memberLoginPanel(this,20,0);
  });
    
    
  //会员调用的2个函数
function afterDirectBook(){
   
}
function afterMemberLogin(){
   document.memberForm.submit();
}
 
 </script>