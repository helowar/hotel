$(function(){
  //window.setTimeout(bindClick,8000);
  //alert(document.cookie);
  var clock=window.setInterval(checkBFDshow, 200);
  function checkBFDshow(){
    if($("#bfd_rec_box li a").html()){
           window.clearInterval(clock);
           bindClick();
           }
       }
  function bindClick(){
     var hotelIdArray = new Array();
    
     $("#bfd_rec_box li a").click(function(){
        var link = this.href;
        if(-1!=link.indexOf("jiudian-")&&-1!=link.indexOf(".html")){
           var begin=link.indexOf("jiudian-")+8;
           var end = link.indexOf(".html");
           var hotelId = link.substring(begin,end);
        }else if(-1!=link.indexOf("hotelId=")){
          var begin=link.indexOf("hotelId=")+8;
          var hotelId = link.substr(begin);
        }
         if(getCookie("hotelIdArray")){
        hotelIdArray = (decodeURIComponent(getCookie("hotelIdArray"))).split(",");
         }else{
         hotelIdArray=[];
         }    
        var as="";
          if(hotelIdArray.length>0){         
          as= hotelIdArray.join("-");
          }        
          if(hotelId&&-1==as.indexOf(hotelId)){
           hotelIdArray.push(hotelId);        
           addCookie("hotelIdArray",hotelIdArray,30);                        
          }
        
         // alert("arr="+decodeURIComponent(getCookie("hotelIdArray")));   
     });
  } 
  
   
  

});

function addCookie(objName,objValue,min){      //添加cookie

   var str = objName + "=" + escape(objValue);

    if(min > 0){                               //为时不设定过期时间，浏览器关闭时cookie自动消失
       var date = new Date();;
       date.setTime(date.getTime() + 1000*60*min);
       str += "; expires=" + date.toGMTString(); 
   }
   document.cookie = str;
}

 //根据name取得cookie的值      
 function getCookie(name){ 
     var strCookie=document.cookie; 
     var arrCookie=strCookie.split("; "); 
     for(var i=0;i<arrCookie.length;i++){ 
         var arr=arrCookie[i].split("="); 
         if(arr[0]==name)return arr[1]; 
      } 
    return ""; 
   }  