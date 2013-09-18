function browserCompatibility(){

if(navigator.appName == "Microsoft Internet Explorer") {

	}
	
if(!!window["performance"]&&("msDoNotTrack" in window.navigator)&&(!window.ScriptEngineMinorVersion())&&!!window.XDomainRequest && (function () {var obj = {'1':1, '0':1};for (var s in obj) return !+s;}())){
	$(".news").addClass("news_ie_9");
}else{
	var ua = navigator.userAgent.toLowerCase();
    var match = /(webkit)[ \/]([\w.]+)/.exec(ua) ||/(opera)(?:.*version)?[ \/]([\w.]+)/.exec(ua) ||/(msie) ([\w.]+)/.exec(ua) ||
    !/compatible/.test(ua) && /(mozilla)(?:.*? rv:([\w.]+))?/.exec(ua) ||[];
    switch(match[1]){
     case "msie":      //ie
      if (parseInt(match[2]) === 6){ 
			$("#slider-img").addClass("slider-img-ie6");
			$(".map_icon").addClass("map_icon_ie6");
		}
      else if (parseInt(match[2]) === 7) {   
			$(".news").addClass("news_ie");
		}
      else if (parseInt(match[2]) === 8) {   //ie8
      		$(".news").addClass("news_ie");
       }
      break;
     case "webkit":     //safari or chrome
      {      
			$(".news").addClass("news_google");
		}
      break;
     case "opera":      //opera
      {      
			
		}
      break;
     case "mozilla":    //Firefox
      {      
			
      }
      break;
     default:
      break;
    }
   }
}