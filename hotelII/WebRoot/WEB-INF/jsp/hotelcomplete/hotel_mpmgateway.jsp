<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
</head>
<body>
<form name="toPay" id="toPay" action="${executeURL}" method="post">
	<input type="hidden" name="orderCd" value="${orderCd}" />
	<input type="hidden" name="outTradeNo" value="${outTradeNo}" />
	<input type="hidden" name="customerId" value="${customerId}" />
	<input type="hidden" name="gatheringUnitCode" value="${gatheringUnitCode}" />
	<input type="hidden" name="currencyType" value="${currencyType}" />
	<input type="hidden" name="requestDate" value="${requestDate}" />
	<input type="hidden" name="productType" value="${productType}" />
	<input type="hidden" name="returnURL" value="${returnURL}" />
	<input type="hidden" name="payMode" value="${payMode}" />
	<input type="hidden" name="amount" value="${amount}" /><!-- 1.00 -->
	<input type="hidden" name="remark" value="${remark}" />
	<input type="hidden" name="signType" value="${signType}" />
	<input type="hidden" name="sign" value="${sign}" />
	
</form>
<script type="text/javascript">	
	document.toPay.submit();
</script>
</body>
</html>