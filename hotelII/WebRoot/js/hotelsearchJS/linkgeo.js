function linkGEOToHotelSearch(cityCode,geoCode,type,intInDate,intOutDate){
	   var checkInDate = new Date(+new Date()+24*3600000*intInDate);
	   var checkOutDate = new Date(+new Date()+24*3600000*intOutDate);
	     var ckInDay = checkInDate.getDate();
         var ckInMonth = checkInDate.getMonth()+1;//getMonth的值为0-11,故要加1
         var ckInYear = checkInDate.getFullYear();
	     var inDateStr = ckInYear+"-"+ckInMonth+"-"+ckInDay;
         
		 var ckOutDay = checkOutDate.getDate();
         var ckOutMonth = checkOutDate.getMonth()+1;//getMonth的值为0-11,故要加1
         var ckOutYear = checkOutDate.getFullYear();
	     var outDateStr = ckOutYear+"-"+ckOutMonth+"-"+ckOutDay;  		
	   var reallyUrl = "hotel-query.shtml?cityCode="+cityCode
	                   +"&inDate="+inDateStr+"&outDate="+outDateStr;
	   var geoUrl="";
	   if(type=="geo"){
	        geoUrl = "&geoId="+geoCode;
	   }else if(type=="biz"){
	        geoUrl = "&bizCode="+geoCode;
       }else if(type=="distinct"){
	        geoUrl = "&distinctCode="+geoCode;
	   }
	  reallyUrl +=geoUrl;
	  window.loaction=reallyUrl;
	
	}
	
	