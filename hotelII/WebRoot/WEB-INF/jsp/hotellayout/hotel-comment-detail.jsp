<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript" src="http://www.mangocity.com/HotelCommentWeb/js/viewBig.js"></script>
<script type="text/javascript" src="http://www.mangocity.com/HotelCommentWeb/js/pic-display.js"></script>
<script type="text/javascript" src="http://hres.mangocity.com/js/h/2011/viewbigpic.js"></script>

<link rel="stylesheet" type="text/css" href="http://wres.mangocity.com/css/promotion/shendu/jquery.fancybox-1.3.4.css" media="screen" />
<input type="hidden" name="hotelId" id="hotelId" value="${hotelId }" />
<input type="hidden" name="commentThete" value="${commentThete }" />
<input type="hidden" name="totalCount" id="totalCount" value="${paginationSupport.totalCount}" />
<input type="hidden" name="baseImgPath" id="baseImgPath" value="${sessionScope.basePath}" />
<div class="cm_list_page">

	<p class="dd_wrap">
		<span class="orange">已有<b class="orange">${paginationSupport.totalCount}</b>名住客发表点评</span>
		<span class="recomm"><font class="orange">${paginationSupport.recommendSum}人推荐</font>
		</span><span class="unrecomm"><font class="orange"
			style="color: red;">${paginationSupport.noRecommendSum}人不推荐&nbsp;&nbsp;推荐指数：${sessionScope.commentIndex}</font>
		</span>
	</p>
</div>

<c:if test="${paginationSupport.totalCount=='0' }">
	<font style="color: red;">&nbsp;酒店暂无点评记录！</font>
</c:if>
<div id="commentContent"></div>


<div class="hotel_web_more"><a href="javascript:void(0);" onclick="changeParentPage('${hotelId}');">查看该酒店全部${paginationSupport.totalCount}条住客的点评>></a></div>


<script type="text/javascript">
		function picShow(obj){
		$("a", "#"+obj).each(function(){
	         $(this).fancybox({
			'overlayShow'	: false ,
			'transitionIn'	: 'elastic',
			'transitionOut'	: 'elastic'
		    });
	      });
	    }  
		//初始点评菜单栏化样式
		function setClass(){
			$('.detail_nav a:gt(0)').attr('class','dnormal');
		}
		function computerCount(recordid,type){
		   if(document.getElementById(recordid+"_"+type+"Flag").value==0){
		   var val=0;
		   if(type==1){
		      val=parseInt($('#'+recordid+"_effectiveId").val());
		   }else{
		      val=parseInt($('#'+recordid+"_noeffectiveId").val());
		   }
		   var url="http://www.mangocity.com/HotelCommentWeb/hotel-comment-details!computerCount.shtml";
		   var parms="hotelId=${hotelId}&recordId="+recordid+"&effective="+type;
		   jQuery.ajax({
					url:url,
					type:'post',
					dataType:'text',
					contentType:'application/x-www-form-urlencoded',
					data:parms,
					error: function(XMLHttpRequest, textStatus, errorThrown) {
	                        alert('XMLHttpRequest.status='+XMLHttpRequest.status);
	                        alert('XMLHttpRequest.readyState='+XMLHttpRequest.readyState);
	                        alert('textStatus='+textStatus);
	                    },
					success:function(data){
					 if(data=='1' && type==1){
					  $("#"+recordid+"a").html(val+1);
					  $('#'+recordid+"_effectiveId").val(val+1);
	                  $('#'+recordid+"_1Flag").val(1);
	                  $('#'+recordid+"_0Flag").val(1);				  
					  $('#'+recordid+"_0Style").css('color','#666');
					 }
					 if(data=='1' && type==0){
					  $("#"+recordid+"b").html(val+1)
	                  $('#'+recordid+"noeffectiveId").val(val+1);
					  $('#'+recordid+"_1Flag").val(1);
	                  $('#'+recordid+"_0Flag").val(1);	
	                  $('#'+recordid+"_1Style").css('color','#666');
					 }
					 if(data=='2'){
					 	$("#"+recordid+"c").html("感谢您参与点评，请让您的食指休息一下吧！");				  
					 }
				    }
				   }
				  );
		   }else{
		      $("#"+recordid+"c").html("感谢您参与点评，请让您的食指休息一下吧！");				
		   }
		}
 
 		function getComment(num){
			$("#commentContent").html("&nbsp;&nbsp;评论加载中...<br/>");
		 	jQuery.ajax({
				url:"http://www.mangocity.com/HotelCommentWeb/hotel-comment-details!list.shtml",
				type:'post',
				dataType:'jsonp',
				jsonp: "callback",
				jsonpCallback:"success_jsonpCallback",
				contentType:'application/x-www-form-urlencoded',
				timeout:10000,
				data:{pageNo:"1",hotelId:$("#hotelId").val(),sumRow:$("#totalCount").val()},
				success:function(data){
					var jsonData = data[0];
					appendCmt(jsonData);
					$('.infiniteCarousel').infiniteCarousel();
				},
				error:function(data){
					alert("加载评论失败，请稍后重试");
				}
			});
 		}
 
        function appendCmt(rs){
       		if(rs.comments==null || rs.comments=='undefined'){
       			$("#commentContent").html(rs.msg);
       			return;
       		}
       		var htmlStr = "";
       		for(var i=0;i<rs.comments.length;i++){
       			var cmt = rs.comments[i];
  				htmlStr += '<div class="detail_panel">';
  				htmlStr += '<div class="hotel_web">';
  				//hotel_web_user
  				htmlStr += '<div class="hotel_web_user"><span>点评发表于：' 
  					+ cmt.commentTime + '&nbsp;来源：'
  					+ cmt.commentSource+'</span>'+cmt.commentName+'</div>';
  				//hotel_web_grade
  				if((cmt.avgScore+'') != '.0'){
  					htmlStr += '<div class="hotel_web_grade">评分：<span class="orange">' + cmt.avgScore + '分</span><p>';
  					htmlStr += (cmt.recommend=='1') ? ('<span class="recomm orange">推荐</span>') : ('<span class="unrecomm red">不推荐</span>');
                    for(var a in cmt.scoreList){
                      	htmlStr += cmt.scoreList[a].gradedName+'：<span class="orange">'+cmt.scoreList[a].graded+'分</span>';
                    }
                    htmlStr += '</p></div>';
  					//hotel_web_tag
  					var impressionArr = cmt.commentImpression.split("##");
  					if(impressionArr!=null && impressionArr.length>0){
	   					var tag = "";
	   					for(var a in impressionArr){
	   						if(impressionArr[a]!='') tag += '<span>'+impressionArr[a]+'</span>';
	   					}
	   					if(tag.length>0){
	   						htmlStr += '<div class="hotel_web_tag">酒店印象： '+tag+'</div>';
	   					}
  					}
  					//hotel_web_pic
  					if(cmt.smallImg!=null && cmt.smallImg.length>0){
   					   htmlStr += '<div class="hotel_web_pic" style="height:90px;">上传的酒店照片：'
                        	+ '<div class="infiniteCarousel">'
                          	+ '<div class="wrapper" id="picShow'+cmt.commentid+'">'
                           	+ '<input type="hidden" name="picSums" value="'+cmt.smallImg.length+'" />'
                           	+ '<input type="hidden" name="commentPicId" value="'+cmt.commentid+'" />'                                            
                           	+ '<input type="hidden" name="currentPageNum" value="1" id="currentPage'+cmt.commentid+'"/>'
                       		+ '<ul>';
                       var baseImgPath = $("#baseImgPath").val()+"/";
                       for(var i=0;i<cmt.smallImg.length;i++){
                       		htmlStr += '<li style="width:134px;">'
                         		+ '<a href="'+baseImgPath+cmt.orignalImg[i].addr+'" id="picAid'+i+'">'
                           		+ '<img name="Pic_'+cmt.commentid+''+i+'" src="'+baseImgPath+cmt.smallImg[i].addr
                           		+ '"  class="hasBigimg" onclick="picShow(\'picShow'+cmt.commentid+'\');" />'
                           		+ '</a></li>';
                       }
   					   htmlStr += '</ul></div></div><div style="display:none">';
                       for(var i=0;i<cmt.bigImg.length;i++){
                         	htmlStr += '<img src="'+baseImgPath+cmt.bigImg[i].addr+'"  id="bigPic'+cmt.commentid+''+i+'" />';
                       }
   					   htmlStr += '</div><div style="display:none">';
                       for(var i=0;i<cmt.orignalImg.length;i++){
                         	htmlStr += '<img src="'+baseImgPath+cmt.orignalImg[i].addr+'"  id="originalImg'+cmt.commentid+''+i+'" />';
                       }
                       htmlStr += '</div></div>';
  					}
  				}
  				//hotel_web_feel
                htmlStr += '<div class="hotel_web_feel">';
                if((cmt.avgScore+'') == '.0'){
                	htmlStr += (cmt.recommend=='1') ? ('<span class="recomm orange">推荐</span>') : ('<span class="unrecomm red">不推荐</span>');
                }
                htmlStr += '入住感受：<p>'+cmt.content+'</p>';
                if(cmt.hotelRevertContent!='' ||cmt.mangoRevertContent!=''){
               		htmlStr += '<div class="hotel_web_reply"><div class="hotel_web_top"></div><div class="hotel_web_bottom">';
               		if(cmt.hotelRevertContent!=''){
               			htmlStr += '<p><span class="orange">酒店回复：</span><br />'+cmt.hotelRevertContent+'</p>';
               		}
               		if(cmt.mangoRevertContent!=''){
               			htmlStr += '<p><span class="orange">芒果网回复：</span><br />'+cmt.mangoRevertContent+'</p>';
               		}
               		htmlStr += '</div></div>';
                }
                htmlStr += '</div>';
                 	
                htmlStr += '<input type="hidden" id="'+cmt.recordid+'_effectiveId" value="'+cmt.commentValidTotal +'"/>';
                htmlStr += '<input type="hidden" id="'+cmt.recordid+'_1Flag" value="0"/>';
                htmlStr += '<input type="hidden" id="'+cmt.recordid+'_0Flag" value="0"/>';
                htmlStr += '<input type="hidden" id="'+cmt.recordid+'_noeffectiveId" value="'+cmt.commentInvalidTotal+'"/>'
                 
				//hotel_web_shop
				htmlStr += '<div class="hotel_web_shop">您认为住客的点评：'
  							+'<a name="useful" href="javascript:void(0);" onclick="computerCount(\''+cmt.recordid+'\',1)" id="'+cmt.recordid+'_1Style" >'       							                                      
                   		+'有用（<span id="'+cmt.recordid+'a">'+cmt.commentValidTotal+'</span>）</a>'                   
                   		+'<a name="useless" href="javascript:void(0);" onclick="computerCount(\''+cmt.recordid+'\',0)" id="'+cmt.recordid+'_0Style">'
                   		+'无用（<span id="'+cmt.recordid+'b">'+cmt.commentInvalidTotal+'</span>）</a>'
                     +'&nbsp;&nbsp;<span id="'+cmt.recordid+'c" style="color:red;"></span></div>';
                   
				//hotel_web end
				htmlStr += '</div>';
				
				//detail_panel end
  				htmlStr += '</div>'; 		
		}
       	$("#commentContent").html(htmlStr);
    }
 
			 
	function readyfunc(){
		setClass();
		$('.detail_nav a:eq(1)').attr('class','don');
		$('.detail_nav a:gt(0)').click(function(){
			setClass();
			$(this).attr('class','don');
		});
		getComment(1);
	}
    </script>
    