$(function(){
var userId=getCookie("userid")||getCookie("memberid");
var hotelId = $("input[name='hotelId']").val()||$("#id_hotelId").val();
var hotelName= $("#id_hotelName").val();
var hotelLink = "http://hotel.mangocity.com/jiudian-"+hotelId+".html";
var bigImageLink = $("#pic_glass img").attr("src")||"";
var smallImageLink =$("a[class='on'] img").attr("src")||"";
var cityCode= $("#id_cityCode").val();
var categoryLink = $("a[name='daohangcity']").attr("href")||"";
var cityName = $("a[name='daohangcity']").attr("title");
var bizzone =$("a[name='bizzone']").html();
var address = cityName+$("#zone_name").val()+$("#chnAddress").val();
var min="";
var returnPrice="";
var roomInfo="";
var cashbackrate= $("#cashbackrate").val();
if(-1==bigImageLink.indexOf("http")&&bigImageLink!=""){
bigImageLink ="http://hotel.mangocity.com/"+bigImageLink;
}
minprice(min,returnPrice);

function createBFD(){
/**if (navigator.userAgent.indexOf('Firefox') >= 0){
console.log("userId:"+userId+"\nhotelId:"+hotelId+"\nhotelName:"+hotelName+"\nhotelLink:"+hotelLink
+"\nbigImageLink:"+bigImageLink+"\nsmallImageLink:"+smallImageLink+"\ncityCode:"+cityCode
+"\ncategoryLink:"+categoryLink+"\ncityName:"+cityName+"\nbizzone:"+bizzone+"\naddress:"+address
+"\nmin:"+min+"\nreturnPrice:"+returnPrice);
console.log("roomInfo:");
console.log(roomInfo);
}**/
window["_BFD"] = window["_BFD"] || {};
_BFD.BFD_INFO = {
	id : hotelId,   //这里需要获取的是当前酒店id号，string类型；
	name : hotelName,  //当前酒店的名称,string类型；
	item_link : hotelLink,// 当前酒店的完整链接url，string类型	
	small_image_link : smallImageLink,// 当前酒店的标志图片的完整链接url（使用120×80的尺寸图片），string类型
	big_image_link:bigImageLink,//这里获取大图，尺寸为300×200；
	price : min,//当前酒店的起步价格，double类型；	
	cashbackrate:cashbackrate,//当前酒店的返现价格比例；
	return_price:returnPrice,//当前酒店的返现价格；
	category_id : ["国内酒店",cityCode], //当前酒店所属的类别id的详细信息，string[] 类型；
	category_tree :[["国内酒店","http://hotel.mangocity.com/"],[cityName+"酒店","http://hotel.mangocity.com/"+categoryLink]], // 当前酒店所属的类别详细信息，类别从大到小，第一个元素为类别名称，第二个元素为类别地址，string[] 类型；
	business_circle:[bizzone],//当前酒店所属商圈名称，Array类型；
	onsale : true,//判断酒店是否在架，下架为false，在架为true，布尔类型；
	address: address, //当前酒店的地址 String类型
	room_info:roomInfo,//酒店房间详细信息，2维数组，第一个参数为房间类型，第二个参数为对应房间单价56元/间；
	user_id : userId,//当前用户的user_id，string类型。注意：user_id不是用户的真实注册名，而是其注册名的编号,如果匿名用户为0或者为空‘’；
	client : "Ctest_mg"  			//百分点技术人员使用的帐号，请您不要修改这句代码！
};
_BFD.script = document.createElement("script");
_BFD.script.type = 'text/javascript';
_BFD.script.async = true;
_BFD.script.charset = 'utf-8';
_BFD.script.src = (('https:' == document.location.protocol?'https://ssl-static1':'http://static1')+'.baifendian.com/service/mangguo/mg_goods_new.js');
document.getElementsByTagName("head")[0].appendChild(_BFD.script);

}

function minprice(minPrice,recashPrice){  //计算数组中的酒店价格最低价
  var clock=window.setInterval(checkGWTshow, 200);
  function checkGWTshow(){
    if($("#hoteldata .hoteldata tr").html()){
           window.clearInterval(clock); 
            getMinPrice();
           }
       }
  window.setTimeout(clearClock,10000);//因为价格信息是gwt生成的，页面加载完成后需要一定的时间价格信息才会显示出来
  function clearClock(){
     window.clearInterval(clock);
  }
 
}

function getMinPrice(){
  var priceArr = initPriceArray();
  var recashArr = initRecashArray();

  var priceParam="";
  var recashParam="";
  for(var i=0;i<priceArr.length;i++){
    priceParam+=priceArr[i]+";";
  }
  for(var i=0;i<recashArr.length;i++){
    recashParam+=recashArr[i]+";";
  }
 var reg=new RegExp("¥","g"); //创建正则RegExp对象   ,g表示替换所有
 priceParam=priceParam.replace(reg,"RMB");  
 recashParam = recashParam.replace(reg,"RMB");
  $.ajax({ 
		    type: "post", 
		    url: "minPriceOfRoomType.shtml",	
		    dataType:'json',
		    async :true,
		    data:"priceArray="+priceParam+"&recashArray="+recashParam,    
		    success:function(data){
			   min= data.minPrice;
			   if(min==999999||min==null){
			      min="";
			    }else{
			      min=toDecimal2(min);//精确到小数点后两位
			    }
			      returnPrice = toDecimal2(data.recash);
			      var priceInfo = data.prices;
			      var roomType;
			      if(priceInfo==null)
			      {
			      return;
			      }
			      roomInfo = new Array();
			      for(var i=0;i<priceInfo.length;i++){
			          if(priceInfo[i]==999999){
			            priceInfo[i]="";//如果返回999999，表示网站酒店详情页没有显示改价格，我们传空字符串给“百分点”公司
			          }else{
			            priceInfo[i]=toDecimal2(priceInfo[i]);//精确到小数点后两位
			           }
			           var j=i;
			           roomType=$(".hoteldata .hasbf:eq("+j+")").parent().parent().find(".roomtype").html();
			           while(!roomType&&j!=0){ //如果该行没有房型，则用上一行的房型
			               j--;
			              roomType=$(".hoteldata .hasbf:eq("+j+")").parent().parent().find(".roomtype").html();
			             
			           }  			      
			          roomInfo[i]=[roomType,priceInfo[i]];
			        } 			   
			        createBFD();
			  
			   
		    } 
	});
 
  
}
function initPriceArray(){//从酒店详情页获取该酒店所有房型的价格信息来初始化数组
 var roomTypeNum=$(".hoteldata .hasbf").size();
 var priceArr=new Array(); 
 var j=0;
  for(var i=0;i<roomTypeNum;i++){
       var currencyPriceDom= $(".hoteldata .hasbf").get(i);
       var price=currencyPriceDom.innerHTML;
       if(-1!=price.indexOf("¥")){
           price=price.substr(1);
       }else if(-1!=price.indexOf("HKD")){ 
           price=price.substr(3);
       }else if(-1!=price.indexOf("MOP")){ 
           price=price.substr(3);
       }
       
        if(!isNaN(price)){//如果不是会员价或者不可售的房型，则把该价格放入待计算最低价的数组
           var currencyPriceDom= $(".hoteldata .hasbf").get(i);
           priceArr[j++]=currencyPriceDom.innerHTML;
       }else{
            priceArr[j++]="RMB999999";
       }
  }
  return priceArr;
}

function initRecashArray(){
  var roomTypeNum=$(".hoteldata .hasbf").size();
 var recashArr=new Array(); 
 var j=0;
  for(var i=0;i<roomTypeNum;i++){
  var recashPriceDom= $(".hoteldata .recash").get(i);
  var recash=recashPriceDom.innerHTML;
  if(-1!=recash.indexOf("¥")){
    recash=recash.substr(1);
    };
  if(-1!=recash.indexOf("HKD")){ 
   recash=recash.substr(3);
  }
  if(-1!=recash.indexOf("MOP")){ 
   recash=recash.substr(3);
  }
  if(!isNaN(recash)){
   var recashPriceDom= $(".hoteldata .recash").get(i);
    recashArr[j++]=recashPriceDom.innerHTML;
  }else{
    recashArr[j++]="RMB0";
  }
  }
  return recashArr;
}
//制保留2位小数，如：2，会在2后面补上00.即2.00  
 function toDecimal2(x) {  
            var f = parseFloat(x);  
            if (isNaN(f)) {  
                return false;  
            }  
            var f = Math.round(x*100)/100;  
            var s = f.toString();  
            var rs = s.indexOf('.');  
            if (rs < 0) {  
                rs = s.length;  
                s += '.';  
            }  
            while (s.length <= rs + 2) {  
                s += '0';  
            }  
            return s;  
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
 
 });