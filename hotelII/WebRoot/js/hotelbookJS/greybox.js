(function($){
	$.fn.mbrgreybox = function(settings){
	   settings = jQuery.extend({
			overlayBgColor : '#000',
			overlayOpacity : 0.6,
				   overlay : 'mango-overlay',
			  overlayframe : 'mango-overframe',
				   popitem : 'chooseMemberCd',
				   closeid : 'close',
			  overlayclose : false,
			         ispop : true,
				  callback : null
	   }, settings || {});
	   
	   var obj = this;
	   
	   function _init(){
		   //settings.callback();
		   //alert(settings.callback.constructor);
		   //if(!settings.callback()) return false;
		   //if(settings.ispop){
		       _start();
		   //}else{
		       settings.callback && settings.callback();
		   //}   
		   
		   return false;
	   }
	   
	   function _start(){
		  $('embed, object, select').css({ 'visibility' : 'hidden' });   
		  $('body').append('<div id="'+settings.overlay+'"></div>');
		   _setInterface();
			   
		   $(window).scroll(function(){
			   var pagesize = _getpagesize(); 
			   var pagescroll = _getpagescroll();
			   $('#'+settings.popitem).css({
				   left : (pagesize[2]/2+pagescroll[0]),
					top : (pagesize[3]/2+pagescroll[1]), //document.body.scrollTop --chrome, safari
					marginLeft : 0-($('#'+settings.popitem).width()/2),
					marginTop : 0-($('#'+settings.popitem).height()/2)
			   });  
		   });
		   
		   $(window).resize(function() {
			   var pagesize = _getpagesize(); 
			   var pagescroll = _getpagescroll();
			   $('#'+settings.popitem).css({
				   left : (pagesize[2]/2+pagescroll[0]),
					top : (pagesize[3]/2+pagescroll[1]), //document.body.scrollTop --chrome, safari
					marginLeft : 0-($('#'+settings.popitem).width()/2),
					marginTop : 0-($('#'+settings.popitem).height()/2)
			   });  
		   });		  

	   };
	   
	   function _setInterface(){
		   
		   var pagesize = _getpagesize(); 
		   var pagescroll = _getpagescroll(); 
		   $('#'+settings.overlay).css({
			   backgroundColor : settings.overlayBgColor,
			   opacity : settings.overlayOpacity,
			   width : pagesize[0], //$(document).width()
			   height: pagesize[1]
		   }).fadeIn();

		   $('#'+settings.popitem).css({
			   left : (pagesize[2]/2 + pagescroll[0]),
				top : (pagesize[3]/2 + pagescroll[1]),
				marginLeft : 0-($('#'+settings.popitem).width()/2),
				marginTop : 0-($('#'+settings.popitem).height()/2)
		   }).show();
		   $('#'+settings.closeid).click(function(){
			   _closeall();
			   return false;
		   });
		   //console.log(settings.overlayclose);
		   if(settings.overlayclose){
			   $('#'+settings.overlay).click(function(){
				   _closeall();
				   return false;
			   });		   
		   }

		   
	   };
	   
	   function _getpagesize(){
			var fullWidth, fullHeight, viewWidth, viewHeight;
			if(window.innerHeight && window.scrollMaxY){ //for firefox when scroll exist
				 //fullWidth = window.innerWidth + window.scrollMaxX;
				 //alert(window.scrollMaxX);
				 fullWidth = (window.scrollMaxX > window.innerWidth) ? window.innerWidth + window.scrollMaxX : window.scrollMaxX;
				 fullHeight = window.innerHeight + window.scrollMaxY;
			}else if(document.body.scrollHeight > document.body.offsetHeight){ // chrome safari
				 fullWidth = document.body.scrollWidth;
				 fullHeight = document.body.scrollHeight;
			}else{ //ie opera
				 fullWidth = Math.max(document.body.offsetWidth, document.documentElement.scrollWidth);
				 fullHeight = Math.max(document.body.offsetHeight, document.documentElement.scrollHeight);
			}
			viewWidth = (document.compatMode=="CSS1Compat") ? document.documentElement.clientWidth : document.body.clientWidth;
			viewHeight = (document.compatMode=="CSS1Compat") ? document.documentElement.clientHeight : document.body.clientHeight;
			fullWidth = Math.max(fullWidth, viewWidth);
			fullHeight = Math.max(fullHeight, viewHeight);
			return [fullWidth, fullHeight, viewWidth, viewHeight];
	   }
	   
	   function _getpagescroll() {
			var xScroll, yScroll;
			if (self.pageYOffset) {
				yScroll = self.pageYOffset;
				xScroll = self.pageXOffset;
			} else if (document.documentElement && document.documentElement.scrollLeft || document.documentElement.scrollTop) { //for firefox ie
				yScroll = document.documentElement.scrollTop;
				xScroll = document.documentElement.scrollLeft;
			} else if (document.body) {
				yScroll = document.body.scrollTop;
				xScroll = document.body.scrollLeft;	
			}
			return [xScroll,yScroll];
		};
		
		function _closeall(){
			$('#'+settings.popitem).hide();
			$('#'+settings.overlay).remove();
			$('embed, object, select').css({ 'visibility' : 'visible' });
			$(window).unbind("scroll").unbind("resize");
		};
		_init();
		return this;

	};

	})(jQuery);