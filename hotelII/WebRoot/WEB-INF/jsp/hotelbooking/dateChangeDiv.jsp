<%@ page language="java" pageEncoding="UTF-8"%>
<div class="popbox changedate"  id="mango-greybox" style="position:absolute;z-index:100">
	<div class="poptit"><span class="popclose" id="popclose">点击关闭</span><h3>修改日期</h3></div>
    <form name="hotelBookForm" id="hotelBookForm" method="post"  action="hotel-booking.shtml">
        <div class="hn-form">
            <ul>
                <li class="w158"><label class="input_label"><input id="id_startDate" name="inDate" type="text" class="w144 greytxt calendar" value='<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkinDate}"/>' /><span id="sdatetip" class="hidetxt">入住</span><span class="holitip"></span><a href="#" class="dateIcon"></a></label>
                </li>
                <li class="w158"><label class="input_label"><input id="id_backDate" name="outDate" type="text" class="w144 greytxt calendar" value='<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrderFromBean.checkoutDate}"/>' /><span id="edatetip" class="hidetxt">离店</span><span class="holitip"></span><a href="#" class="dateIcon"></a></label>
                 </li>
                <li><input id="changetime" name="changetime" type="button" class="btn81x26" value="确定修改" /></li>
            </ul>
            <input type="hidden" name="fagChangeDate" id="fagChangeDate" value="1" />
        	<input type="hidden" name="priceTypeId" id="rebook_priceTypeId" value="${hotelOrderFromBean.childRoomTypeId}" />
        	<input type="hidden" name="payMethod" id="rebook_payMethod" value="${hotelOrderFromBean.payMethod}" />    
        </div>
	</form>  	      
</div>