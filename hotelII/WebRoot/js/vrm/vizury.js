$(function(){

var param=$("#vrm_param").val();  //h100表示酒店首页，h200表示酒店查寻页面或港澳酒店首页，h300表示酒店详情页，h400订单填写也，h500订单完成页
var cityCode= $("#cityCode").val()||$("input[name='cityCode']").val();
var inDate = $("#id_startDate").val();
var outDate = $("#id_backDate").val();
var rooms = "";//该酒店最低价格的房型所对应的日均返现价
var hname= $("#hotelName").val();//酒店名
var oid= $("#oid").val();//订单id
var ad= "";//币种
var op= $("#op").val();//订单价格
var rt=$("#hotelStar").val();//酒店星级
if(typeof rt !="undefined"){
   if(-1!=rt.indexOf("star")){
   rt=rt.substr(4);
    }else if(-1!=rt.indexOf("豪华型酒店")){
    rt=5;
    }else if(-1!=rt.indexOf("高档型酒店")){
    rt=4;
    }else if(-1!=rt.indexOf("舒适型酒店")){
    rt=3;
    }else{
    rt=2;
    }
}

var newp;//酒店售价
var minIndex;//酒店所有房型中价格最小的下标
if(hname=="您可以输入酒店名称查询"){
   hname=""
}
if(hname==""){
if($(".hotelname h1").html()!=null){
  hname=$.trim($(".hotelname h1").html());
  }
}
//var image=$(".detailwrap .imgWrap img").attr("src");//酒店图片路径
var image=$("#pic_glass img").attr("src");
if(image==null){
image="";
}
if(image!=""&&-1==image.indexOf("http")){
image="http://hotel.mangocity.com/"+image;
}
var lp=$("#lp").val();//酒店详情页路径
var hotelid;
hotelid=$("input[name='hotelId']").val()||$("#id_hotelId").val();
if ($("#vrm_param").val()=="h300"){//如果是酒店详情页，就需要计算该酒店的所有房型的最低价
window.setTimeout(minPriceAndsendRequest,1000);//因为价格信息是gwt生成的，页面加载完成后需要一定的时间价格信息才会显示出来
}else{
  sendRequest();//直接发请求，不需要算最低价
}



function minprice(){  //计算数组中的酒店价格最低价
  var arr= initPriceArray();//从酒店详情页获取该酒店所有房型的价格信息来初始化数组
  if(arr.length!=0){
     var min=arr[0];
     var minIndex=0;
     for(var i=1;i<arr.length;i++){
      if(min>arr[i]){
          min=arr[i];
          minIndex=i;
          }
      }
      var currencyPriceDom= $(".hoteldata .hasbf").get(minIndex);
      var recashPriceDom= $(".hoteldata .recash").get(minIndex);
      newp=currencyPriceDom.innerHTML;
      recash=recashPriceDom.innerHTML;
      if(-1!=newp.indexOf("¥")){
        ad="￥";
       }else if(-1!=newp.indexOf("HKD")){
         ad="HK$";
       }else if(-1!=newp.indexOf("MOP")){
         ad="MOP";
       }
        if(-1!=recash.indexOf("¥")){
         rooms=recash.substr(1);
       }else if(-1!=recash.indexOf("HKD")){
         rooms=recash.substr(3);
       }else if(-1!=recash.indexOf("MOP")){
         rooms=recash.substr(3);
       }
       if(!isNaN(rooms)){
         rooms=rooms-0;
       }else{
       rooms=0;
       }
       
       newp=min;
       if(newp==1000000){
       newp="";
       }
       
  }else{
   newp="";
  }
  
}
function initPriceArray(){//从酒店详情页获取该酒店所有房型的价格信息来初始化数组
 var roomTypeNum=$(".hoteldata .hasbf").size();
 var arr=new Array(); 
 var j=0;
  for(var i=0;i<roomTypeNum;i++){
  var currencyPriceDom= $(".hoteldata .hasbf").get(i);
  var price=currencyPriceDom.innerHTML;
  if(-1!=price.indexOf("¥")){
    price=price.substr(1);
    };
  if(-1!=price.indexOf("HKD")){ 
   price=price.substr(3);
  }
  if(-1!=price.indexOf("MOP")){ 
   price=price.substr(3);
  }
  if(!isNaN(price)){//如果不是会员价或者不可售的房型，则把该价格放入待计算最低价的数组
    arr[j++]=price-0;
  }else{
    arr[j++]=1000000;//如果页面没有显示价格，则把价格赋一个极大值，求最低价的时候会排除该价格
  }
  }
  return arr;
}
function sendRequest(){//发送请求给Vizury
   hname=encodeURIComponent(hname);
   ad=encodeURIComponent(ad);
   $.getJSON("http://serv1.vizury.com/analyze/analyze.php?callback=?&account_id=VIZVRM128&param=h300&hotelid="+hotelid+"&city="+cityCode+"&indate="+inDate+"&outdate="+outDate+"&rooms="+rooms+"&ad="+ad+"&ch=&hname="+hname+
"&oldp=&newp="+newp+"&image="+image+"&lp="+lp+"&rt="+rt+"&section=1&level=1",function(data){});
   
}
function minPriceAndsendRequest(){
  minprice();
  if(newp!=""){
    sendRequest();
  }
}

})