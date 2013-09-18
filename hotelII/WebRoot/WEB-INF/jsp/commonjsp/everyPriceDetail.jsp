<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
 <c:set var="everyPriceWidth" value="${fn:length(saleItemVOList)+1}" />
             <c:if test="${fn:length(saleItemVOList) > 7}">
                <c:set var="everyPriceWidth" value="8" />
             </c:if>
            <div id="pricebox" class="tipbox prtip" style="display:none;width:${everyPriceWidth*70}px">
			    <div class="tipborder">
			        <table cellpadding="0" cellspacing="0">
			              <tr>
			                   <th>周期</th>
			                   <c:set var="weekDays" value="${fn:length(saleItemVOList)}" />
			                   <c:if test="${fn:length(saleItemVOList) > 7}">
			                       <c:set var="weekDays" value="7" />
			                   </c:if>
			                   <c:forEach items="${saleItemVOList}" var="saleItemVO" begin="0" end="${weekDays-1}"> 
			                        <th>${saleItemVO.weekDay}</th>
			                   </c:forEach>
			              </tr>
			              <tr>
			                  <c:set var="weekNum" value="1" />
	                          <c:forEach items="${saleItemVOList}" var="saleItemVO" varStatus="status">
	                              <c:if test="${status.index%7==0}">
	                                   <td>第${weekNum}周</td>
	                                    <c:set var="weekNum" value="${weekNum+1}" />
	                              </c:if>
			                        <td style="font-family:Arial, Helvetica, sans-serif;">${saleItemVO.salePriceStr}</td>	
			                       <c:if test="${status.count%7==0}">
			                           </tr><tr>
			                       </c:if>		                                
			                  </c:forEach>
			               </tr>         
			        </table>
			    </div>
			</div>
			<script type="text/javascript" >
		        jQuery("#showEveryPrice_js").mouseover(function(){
		            jQuery("#pricebox").show();
		            var left_value = jQuery(this).offset().left -(jQuery("#pricebox").width()-jQuery(this).width())/2;
		            var top_value = jQuery(this).offset().top+20;
		            jQuery("#pricebox").css({top:top_value,left:left_value});
		        });
		         jQuery("#showEveryPrice_js").mouseout(function(){
		            jQuery("#pricebox").hide();
		        });		
			</script>
