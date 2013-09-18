<%@ include file="commonCC.jsp"%>

<%@ page import="org.ecside.core.ECSideConstants"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>

 <link rel="stylesheet" type="text/css"
	href="<c:url value="/css/hotelsearch/td_style_ec.css" />" />

<script type="text/javascript"
	src="<c:url value="/js/hotelordermanage/prototypeajax.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/hotelordermanage/ecside.js" />"></script>



<script>
   function doQuery(formID, tableID){		
	    var queryPara = getValues(formID);	
	    ECSideUtil.queryECForm(tableID, true, queryPara);
   
   }
   
   
	   
	function init(){
	
		var ecside1=new ECSide();
		ecside1.doPrep=false;
		ecside1.findAjaxZoneAtClient=true;
		ecside1.init();
		if(typeof(childFunc) =="undefined"){
		}else if (isValidObject(childFunc)){
		   childFunc();
		}
	}
	
	window.onload=init;



</script>


