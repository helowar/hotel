<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="core"%>
<%-- 如果是(预付单并且是用信用卡支付)或者(面付信用卡担保)，显示信用卡填写页面 --%>    
         <%-- payMethodStrForShow 传递给会员的参数，预付(2显示"支付"),面付(1显示"担保") --%>
        <c:choose>
        	
	        <c:when test="${hotelOrderFromBean.payMethod eq 'pre_pay' || hotelOrderFromBean.payToPrepay}">
	        	<c:set var="payMethodStrForShow" value="pay"/>
	        	<c:set var="faceAmount" value="${hotelOrderFromBean.acturalAmount}" scope="page"/>
	        	<c:set var="paymethodChn" value="支付"/>
	        </c:when>
	        
	        <c:otherwise>
	        	<c:set var="payMethodStrForShow" value="assure"/>
	        	<c:set var="faceAmount" value="${hotelOrderFromBean.suretyPriceRMB}" scope="page"/>
	        	<c:set var="paymethodChn" value="担保"/>
	        </c:otherwise>
        </c:choose>
      	<div class="mgcBoxWrap">
    <c:choose>
    <c:when test="${hotelOrderFromBean.payMethod eq 'pre_pay' || hotelOrderFromBean.payToPrepay}">
        <iframe id="creditIFrame" name="creditIFrame" onload="javascript:initIframeHeight()" 
        src="http://www.mangocity.com/MpmWeb/jsp/creditcard/web/creditcard.jsp?sid=${sid}&memberCd=${memberCd}&tradeType=${payMethodStrForShow}&cardTypeConfig=${prePayCreditCard }" 
        frameborder="no" border="0" width="700" scrolling="no" allowtransparency="yes"></iframe>
         <!--  信用卡页面end -->
     </c:when>   
     <c:otherwise>
      <iframe id="creditIFrame" name="creditIFrame" onload="javascript:initIframeHeight()" 
        src="http://www.mangocity.com/MpmWeb/jsp/creditcard/web/creditcard.jsp?sid=${sid}&memberCd=${memberCd}&tradeType=${payMethodStrForShow}&cardTypeConfig=${assureCreditCard }" 
        frameborder="no" border="0" width="700" scrolling="no" allowtransparency="yes"></iframe>
     </c:otherwise> 
      </c:choose> 
        </div>

	<script type="text/javascript">
	<!--
	function initIframeHeight() {             
	// creditcardIFrame为内嵌财务系统iframe的id号
			var iframe = document.getElementById('creditIFrame'); 
              iframe.height= iframe.contentWindow.document.documentElement.scrollHeight;
                                     
	// addheight为信用卡页面预留高度
               var addheight= "5px";          
              if (document.getElementById) {                  
                  if (iframe) {                      
                     if (iframe.contentDocument && iframe.contentDocument.body.offsetHeight) {                          
                         iframe.height = iframe.contentDocument.body.offsetHeight;                          
                     } else if (iframe.Document && iframe.Document.body.scrollHeight) {                             
                         iframe.height = iframe.Document.body.scrollHeight;                               
                     }
                  }
              }
           }

	//-->
	</script>
