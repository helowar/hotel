
$(function(){
var bxdq;//不限区域
var zone="bxdq"; //城区编码
var cityCode=$("#cityCode").val();
if(cityCode=="null"){
   cityCode="HKG";
}
var dataSource; //存储返回的json数据，用于绘图
var maxAvgPrice=1000;//平均价格的最大值，用于动态确定yLineNum,进而动态确定Y轴高度
var yLineNum;//Y轴的单元格数
    //初始化商圈
	$.ajax({ 
		    type: "post", 
		    url: "zonejson.shtml",	
		    dataType:'json',
		    async :false,   //在会展平均价格趋势图之前，首先要得到城市城区的zoneCode,是同步关系   
		    data:" param.cityCode="+cityCode,    
		    success:function(data){
			   if(null != data){
			    $("#areaWrap").append("<span name='bxdq'>不限地区</span>");
					var result =  $.parseJSON(data.content);
					for(var i=0;i<result.length;i++){
					$("#areaWrap").append("<span name='"+result[i].zoneCode+"'>"+result[i].zoneName+"</span>");
				  }
			   	}
		    },
		   	    
		    error:function(xhr,status,error){  
          //alert("pricetrendmap.js--->初始化商圈出错");
        }  
	});
	
$("#areaWrap").children("span").click(function(){
  if($(this).hasClass("on")) return;
  $(this).addClass("on").siblings(".on").removeClass("on");
  zone=$(this).attr("name");
  queryAvgPrice(zone,dataSource);
  $("#container").MG_trend_map({yAxisSV:0,
                                vLineNum:30,
								yRS:200,
								startFT:0,
								hLineNum:yLineNum,
								xRS:7,
								gridW:22,
								gridH:22,
								eLength:50,
								dataSource:eval(dataSource)});
}).eq(0).trigger("click");


//根据bizZone返回未来30天酒店平均价格数据
function queryAvgPrice(zone){
    $.ajax({ 
		    type: "post", 
		    url: "avgpricejson.shtml",	
		    dataType:'json',
		    async :false,  //更新dataSource的值之后才能执行后面的代码才有意义，故设为同步请求
		    data: "param.zone="+zone+"&param.cityCode="+cityCode,
			success:function(data){
			   if(null != data){
					var result =  $.parseJSON(data.content);
					$("#updateTime").text(result[0].advanced[0].date);//显示更新时间
					if(zone=="bxdq"){
					maxAvgPrice=result[0].maxAvgPrice;
					}
					yLineNum= Math.ceil(maxAvgPrice/200)+5;
				    dataSource=result[0];//存储返回的json数据，用于绘图
			   	}
		    },
		   	    
		    error:function(xhr,status,error){  
          
        }  
	});
}
});

		  