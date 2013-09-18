package com.mangocity.client.hotel.search.serviceImpl;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.mangocity.client.HotelListSearchService;
import com.mangocity.client.HotelListSearchServiceAsync;
import com.mangocity.client.hotel.gwt.queryCondition.GWTQueryCondition;
import com.mangocity.client.hotel.search.service.HotelQueryConditonService;
import com.mangocity.hotel.search.vo.HotelPageForWebBean;
import com.mangocity.hotel.search.vo.HotelTemplateVO;

public class HotelQueryAndShowImpl {
	
	private boolean hasQueryHotel=true;
    private HotelListSearchServiceAsync hotelListSearchService ;
	public static HotelQueryConditonService queryConService ;
    private Button hotelSearchButton ;
	private static HotelQueryAndShowImpl hotelQueryAndShowImpl;
	public static   GWTQueryCondition gwtQueryCondition ;
	public String hotelIdsStr = "";
	public static final String BOOKTOKEN ="bookToken";
	public static final String PROMPT_MSG_INNERHTML_FOR_NO_HOTEL_FOUND = "<p>很抱歉，没有找到符合您要求的酒店，建议您重新修改酒店的搜索条件"
			+ "（例如：调整筛选条件，看看输入的文字是否有误、是否有空格等）。"
			+ "您也可以看看下面列表中芒果网推荐的酒店是否合适？</p>";
	public static final String PROMPT_MSG_INNERHTML_FOR_NO_HOTEL_FOUND_TWO = "<p>很抱歉，没有找到符合您要求的酒店，建议您重新修改酒店的搜索条件"
			+ "（例如：调整筛选条件，看看输入的文字是否有误、是否有空格等）。";

	
	private HotelQueryAndShowImpl(){
		
	}
	public static HotelQueryAndShowImpl getInstance(){
		if(hotelQueryAndShowImpl==null){
			hotelQueryAndShowImpl = new HotelQueryAndShowImpl();
		}
		return hotelQueryAndShowImpl;
	}
	public void init() {
		hotelListSearchService = GWT.create(HotelListSearchService.class);
		queryConService = new HotelQueryCondtionServiceImpl();
		hotelSearchButton = new Button("重新搜索");
		hotelSearchButton.setStyleName("btn92x26a");
		hotelSearchButton.getElement().setId("gwt_reSearch");
		queryConService.setHotelQueryCondtion();
		gwtQueryCondition = queryConService.getGWTQueryCondition();
		RootPanel.get("hotelSearch").add(hotelSearchButton);
		String hotelIdsStr = RootPanel.get("f_hotelIdsStr").getElement().getPropertyString("value");
		//改变对比酒店的样式
    	changeSelectedHotelStyle();
		hotelSearchButton.addClickHandler(
				new ClickHandler(){
					public void onClick(ClickEvent event) {
						//必须用给gwtQueryCondition赋值（gwt的原因）
						RootPanel.get("gwt_cityName").clear();
						clearCompareHotelDiv();
						gwtQueryCondition = queryConService.getGWTQueryCondition();
						RootPanel.get("gwt_cityName").getElement().setInnerHTML(gwtQueryCondition.getCityName());
						if(null==gwtQueryCondition.getCityCode()||"".equals(gwtQueryCondition.getCityCode())){
							Window.alert("查询城市不存在!");
							return ;
						}
						//page清1
						gwtQueryCondition.setPageNo(1);
						String promoteType = Cookies.getCookie("promoteType");
						if(null != promoteType && !"".equals(promoteType)){
							gwtQueryCondition.setPromoteType(Integer.parseInt(promoteType));
						}
						setConditonAndHotelShow(gwtQueryCondition);
						writeCookies("inDate",gwtQueryCondition.getInDate(),null);
						writeCookies("outDate",gwtQueryCondition.getOutDate(),null);
					}
				});
		
		
		//当点击返回按钮时改变事件
		if(BOOKTOKEN.equals(History.getToken())){		
			hotelSearchButton.click();
		}else{			
		    setHotelList(hotelIdsStr,gwtQueryCondition);
		    
		}
	}
	
	
	
	public void setConditonAndHotelShow(GWTQueryCondition queryCon){
		hotelSearchButton.setStyleName("gbtn92x26");
		hotelSearchButton.setEnabled(false);
		if(queryCon.getCityCode() == null){
			queryCon.setCityCode("PEK");
		}
		RootPanel.get("requeryTip").getElement().setInnerText("");
		clearHotelContext();
		RootPanel.get("waitImg").setVisible(true);
		RootPanel.get("topPage").setStyleName("");		
		setHotelTemplate(queryCon);
				
	}
	
	
	
	private void setHotelTemplate(final GWTQueryCondition gwtQueryConditon){
		hotelListSearchService.getHotelTemplate(gwtQueryCondition,new AsyncCallback<HotelTemplateVO>(){
	        public void onFailure(Throwable caught){
	        }
	        public void onSuccess(HotelTemplateVO hotelTemplateVO){
	        
	        	RootPanel.get("waitImg").setVisible(false);
	        	RootPanel.get("topPage").setStyleName("reqtab");
	        	hotelSearchButton.setStyleName("btn92x26a");
	        	hotelSearchButton.setEnabled(true);
	        	if(hotelTemplateVO.getHotelIdsStr() != null && !hotelTemplateVO.getHotelIdsStr().equals("")){
		        	String hotelListOut = hotelTemplateVO.getHotelListOut(); 
		        	InlineHTML html = new InlineHTML(hotelListOut);
		        	RootPanel.get("hotelList").add(html);
		        	hotelIdsStr = hotelTemplateVO.getHotelIdsStr();
		        	RootPanel.get("f_hotelCount").getElement().setPropertyInt("value",hotelTemplateVO.getHotelCount());
		        	//改变对比酒店的样式
		        	changeSelectedHotelStyle();
		        	setHotelList(hotelIdsStr,gwtQueryCondition);
		        	RootPanel.get("requeryTip").setVisible(false);
		        	hasQueryHotel=true;
	        	}else{	 
	        	 if(null!= gwtQueryCondition.getHotelName() && !"".equals(gwtQueryCondition.getHotelName())){
	        		if("pre_pay".equals(gwtQueryCondition.getPayMethod())){	
	        			setNoPrePayQueryHotels(gwtQueryCondition);
	        		}else{
	        			gwtQueryCondition.setHotelName(null);
	        			setNoQueryHotel(gwtQueryCondition);
		        		RootPanel.get("requeryTip").setVisible(true);
	        		}
	        	}
	        	 else{	        		
	        		 if("pre_pay".equals(gwtQueryCondition.getPayMethod())){
		        			setNoPrePayQueryHotels(gwtQueryCondition);
		        		}else{			        			
		        			RootPanel.get("requeryTip").getElement().setInnerHTML(PROMPT_MSG_INNERHTML_FOR_NO_HOTEL_FOUND_TWO);
			        		RootPanel.get("requeryTip").setVisible(true);
		        		}
	        	 }
	        	}
	        }
      });
	}
	
	/**
	 * 查不到酒店的处理
	 * @param newGwtQueryConditon
	 */
	private void setNoPrePayQueryHotels(final GWTQueryCondition gwtQueryConditon){	
    		hasQueryHotel=false;
    	 	GWTQueryCondition newGWTQueryCondition=new GWTQueryCondition();
        	newGWTQueryCondition.setCityCode(gwtQueryCondition.getCityCode());
        	newGWTQueryCondition.setCityName(gwtQueryCondition.getCityName());
        	newGWTQueryCondition.setOutDate(gwtQueryCondition.getOutDate());
        	newGWTQueryCondition.setInDate(gwtQueryCondition.getInDate());
        	newGWTQueryCondition.setSorttype(1);
        	newGWTQueryCondition.setSortUpOrDown(1);
        	newGWTQueryCondition.setFromChannel(gwtQueryCondition.getFromChannel());
        	newGWTQueryCondition.setFagReQuery(true);
        	newGWTQueryCondition.setPromoteType(1);
        	gwtQueryCondition=newGWTQueryCondition;
    		setNoQueryHotel(gwtQueryCondition);		
	}
	
	private void setNoQueryHotel(final GWTQueryCondition newGwtQueryConditon){
		hotelListSearchService.getHotelTemplate(newGwtQueryConditon,new AsyncCallback<HotelTemplateVO>(){
	        public void onFailure(Throwable caught){
	        }
	        public void onSuccess(HotelTemplateVO hotelTemplateVO){
	        	RootPanel.get("waitImg").setVisible(false);
	        	RootPanel.get("topPage").setStyleName("reqtab");
	        	hotelSearchButton.setStyleName("btn92x26a");
	        	hotelSearchButton.setEnabled(true);
	        	if(hotelTemplateVO.getHotelIdsStr() != null && !hotelTemplateVO.getHotelIdsStr().equals("")){
	        		
		        	String hotelListOut = hotelTemplateVO.getHotelListOut(); 
		        	InlineHTML html = new InlineHTML(hotelListOut);
		        	RootPanel.get("hotelList").add(html);
		        	hotelIdsStr = hotelTemplateVO.getHotelIdsStr();
		        	RootPanel.get("f_hotelCount").getElement().setPropertyInt("value",0);
		        	//改变对比酒店的样式
		        	changeSelectedHotelStyle();
		       		        	
		        	setHotelList(hotelIdsStr,newGwtQueryConditon);
		        	RootPanel.get("requeryTip").setVisible(true);
		        	RootPanel.get("requeryTip").getElement().setInnerHTML(PROMPT_MSG_INNERHTML_FOR_NO_HOTEL_FOUND);
	        	}else{
	        		RootPanel.get("requeryTip").setVisible(true);
	        		RootPanel.get("requeryTip").getElement().setInnerHTML(PROMPT_MSG_INNERHTML_FOR_NO_HOTEL_FOUND_TWO);
	        		//醒狮计划 add by wj
	        		HotelResultShowImpl hotelResultShow = new HotelResultShowImpl();
	        		hotelResultShow.setHotelListTop();	
	        	}
	        }
      });
	}
	// add by diandian.hou 
	private void setHotelList(String hotelIdsStr, GWTQueryCondition gwtQueryCondition){
		hotelListSearchService.searchHotelListInfo(hotelIdsStr, gwtQueryCondition, new AsyncCallback<HotelPageForWebBean>(){
	        public void onFailure(Throwable caught){
	        	//Window.alert(caught.getMessage());
	        }
	        public void onSuccess(HotelPageForWebBean hotelpageForWebBean){
	        	if(null != hotelpageForWebBean){
	        		HotelResultShowImpl hotelResultShow = new HotelResultShowImpl();
		        	long time1 = System.currentTimeMillis();
		        	hotelResultShow.setHotelResult(hotelpageForWebBean,1,3);//从第几个酒店到第几个酒店
		        	setHotelRusultAjax(hotelpageForWebBean);
	        	}else{
	        		//RootPanel.get("requeryTip").getElement().setInnerText("没有查询到结果，请重新查询！");
	        	}
	        }
      });		
	}
	
	private void setHotelRusultAjax(HotelPageForWebBean hotelpageForWebBean1){
		hotelListSearchService.sameObject(hotelpageForWebBean1, new AsyncCallback<HotelPageForWebBean>(){
	        public void onFailure(Throwable caught){
	        	//Window.alert(caught.getMessage());
	        }
	        public void onSuccess(HotelPageForWebBean hotelpageForWebBean){
	        	if(null != hotelpageForWebBean){
	        		HotelResultShowImpl hotelResultShow = new HotelResultShowImpl();
		        	hotelResultShow.setHotelResult(hotelpageForWebBean,4,15);//从第几个酒店到第几个酒店
	        	}	        
	        }
      });		
	}
	
	//clear context
	private void clearHotelContext(){
		RootPanel.get("hotelCount").clear();
		RootPanel.get("hotelList").getElement().setInnerText("");
		RootPanel.get("topPage").clear();
		RootPanel.get("hotelSortDiv").clear();
		RootPanel.get("bottomPage").clear();
	}
	
	private void writeCookies(String name,String value,Date expires) {
		if(null!=name&&null!=value&&!"".equals(name)&&!"".equals(value)) {
			String cookieValue = Cookies.getCookie(name);
			if(null!=cookieValue&&!"".equals(cookieValue)) {
				Cookies.removeCookieNative(name,"/");
				Cookies.setCookie(name, value, expires, "", "/", false);
			}else {
				Cookies.setCookie(name, value, expires, "", "/", false);
			}
		}
	}
	
	//外部的js
	 public static native void clearCompareHotelDiv()/*-{ 
        $wnd.clearCompareHotelDiv(); 
    }-*/;
	 //酒店对比的样式变化
	 public static native void changeSelectedHotelStyle()/*-{ 
         $wnd.changeSelectedHotelStyle(); 
     }-*/;
}
