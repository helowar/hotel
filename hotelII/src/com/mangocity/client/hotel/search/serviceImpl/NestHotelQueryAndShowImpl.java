package com.mangocity.client.hotel.search.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.mangocity.client.HotelListSearchService;
import com.mangocity.client.HotelListSearchServiceAsync;
import com.mangocity.client.hotel.gwt.queryCondition.GWTQueryCondition;
import com.mangocity.client.hotel.panel.NestAnchor;
import com.mangocity.hotel.search.vo.CommodityVO;
import com.mangocity.hotel.search.vo.HotelPageForWebBean;
import com.mangocity.hotel.search.vo.HotelResultForWebVO;
import com.mangocity.hotel.search.vo.HotelResultVO;
import com.mangocity.hotel.search.vo.RoomTypeVO;

public class NestHotelQueryAndShowImpl  extends HotelListPageImpl {

	private HotelListSearchServiceAsync hotelListSearchService ;
    //分页
	private HorizontalPanel mainPagePanel ;
	public static GWTQueryCondition gwtQueryCondition ;
	private static NestHotelQueryAndShowImpl nestHotelQueryAndShowImpl;
	private final static  int PAGENUM_SHOW = 6;
	
	private HotelPageForWebBean cur_hotelPageForWebBean ;
	
	public void init() {
		hotelListSearchService = GWT.create(HotelListSearchService.class);
		addSearchDiv();
		gwtQueryCondition = initQueryCondition();
		setConditonAndHotelShow(gwtQueryCondition);
	}
	
	public static NestHotelQueryAndShowImpl getInstance(){
		if(nestHotelQueryAndShowImpl==null){
			nestHotelQueryAndShowImpl = new NestHotelQueryAndShowImpl();
		}
		return nestHotelQueryAndShowImpl;
	}
	
	public void setConditonAndHotelShow(GWTQueryCondition queryCon){
		super.curPage = 1;
		super.hotelCount = 0;
		super.pageSize = PAGENUM_SHOW;
		super.mainPageBar.clear();
	
		cleansomeDiv();
		if(queryCon.getCityCode() == null){
			queryCon.setCityCode("HKG");
		}
		setHotelList(queryCon);
	}
	
		
	// add by diandian.hou 
	private void setHotelList(GWTQueryCondition gwtQueryCondition){
		hotelListSearchService.searchHotelListInfo(gwtQueryCondition, new AsyncCallback<HotelPageForWebBean>(){
	        public void onFailure(Throwable caught){
	        	//Window.alert(caught.getMessage());
	        }
	        public void onSuccess(HotelPageForWebBean hotelpageForWebBean){
	        	if(null != hotelpageForWebBean){
	        		setHotelResult(hotelpageForWebBean);
	        	}else{
	        		RootPanel.get("requeryTip").getElement().setInnerText("没有查询到结果，请重新查询！");
	        	}
	        }
      });		
	}
	public static GWTQueryCondition initQueryCondition(){
		GWTQueryCondition queryCondition = new GWTQueryCondition();
		queryCondition.setCityCode("HKG");
		//queryCondition.setInDate("2011-12-01");
		//queryCondition.setOutDate("2011-12-02");
		queryCondition.setFromChannel("1");
		queryCondition.setPageSize(300);
		return queryCondition;
	}
	
	public void addSearchDiv(){
		FlowPanel titlePanel = new FlowPanel();
		titlePanel.setStyleName("city");
		final Anchor titleHKGAnchor = new Anchor("香港");
		titleHKGAnchor.setStyleName("hk_o");
		titlePanel.add(titleHKGAnchor);
		final Anchor titleMACAnchor = new Anchor("澳门");
		titleMACAnchor.setStyleName("am_c");
		titlePanel.add(titleMACAnchor);
		RootPanel.get("right_title_gwt").add(titlePanel);
		RootPanel.get("right_title_gwt").add(new InlineHTML("<h1>热门预付酒店</h1>"));
		
		
		//香港
		FlowPanel hkgbizsDiv = new FlowPanel();
		RootPanel.get("hkgbizs").add(hkgbizsDiv);
		hkgbizsDiv.setStyleName("area");
		final NestAnchor[] hkgAnchors = new NestAnchor[5];
		hkgAnchors[0] = new NestAnchor("尖沙咀","HKG","HKGJSZB",hkgAnchors);
		hkgAnchors[0].setClickHandler();
		hkgbizsDiv.add(hkgAnchors[0]);
		hkgAnchors[1] = new NestAnchor("旺角,油麻地","HKG","HKGWJYMDB",hkgAnchors);
		hkgAnchors[1].setClickHandler();
		hkgbizsDiv.add(hkgAnchors[1]);
		
		hkgAnchors[2] = new NestAnchor("湾仔,铜锣湾（会展中心）","HKG","HKGWZTLWB",hkgAnchors);
		hkgAnchors[2].setClickHandler();
		hkgbizsDiv.add(hkgAnchors[2]);
		
		hkgAnchors[3] = new NestAnchor("佐敦","HKG","HKGZTB",hkgAnchors);
		hkgAnchors[3].setClickHandler();
		hkgbizsDiv.add(hkgAnchors[3]);
		
		hkgAnchors[4] = new NestAnchor("中环,金钟","HKG","HKGZHJZB",hkgAnchors);
		hkgAnchors[4].setClickHandler();
		hkgbizsDiv.add(hkgAnchors[4]);
				
		//澳门  澳门MACAMB,氹仔MACDZB,路环MACLHB
		FlowPanel macbizsDiv = new FlowPanel();
		RootPanel.get("macbizs").add(macbizsDiv);
		macbizsDiv.setStyleName("area");
		final NestAnchor[] macAnchors = new NestAnchor[3];
		macAnchors[0] = new NestAnchor("澳门","MAC","MACAMB",macAnchors);
		macAnchors[0].setClickHandler();
		macbizsDiv.add(macAnchors[0]);
		
		macAnchors[1] = new NestAnchor("氹仔","MAC","MACDZB",macAnchors);
		macAnchors[1].setClickHandler();
		macbizsDiv.add(macAnchors[1]);
		
		macAnchors[2] = new NestAnchor("路环","MAC","MACLHB",macAnchors);
		macAnchors[2].setClickHandler();
		macbizsDiv.add(macAnchors[2]);
		
		
		//添加title事件
		titleHKGAnchor.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent event) {
						gwtQueryCondition.setCityCode("HKG");
						gwtQueryCondition.setBizZone(null);
						titleHKGAnchor.setStyleName("hk_o");
						titleMACAnchor.setStyleName("am_c");
						RootPanel.get("hkgbizs").setVisible(true);
						RootPanel.get("macbizs").setVisible(false);
						for(int i = 0; i<hkgAnchors.length;i++){
							hkgAnchors[i].setStyleName("");
						}
						setConditonAndHotelShow(gwtQueryCondition);
					}
				});
		titleMACAnchor.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				gwtQueryCondition.setCityCode("MAC");
				gwtQueryCondition.setBizZone(null);
				titleHKGAnchor.setStyleName("hk_c");
				titleMACAnchor.setStyleName("am_o");
				RootPanel.get("hkgbizs").setVisible(false);
				RootPanel.get("macbizs").setVisible(true);
				for(int i = 0; i<macAnchors.length;i++){
					macAnchors[i].setStyleName("");
				}
				setConditonAndHotelShow(gwtQueryCondition);
			}
		});
		
	}
		  
	  public void queryHotelList(String cityCode,String bizCode){
		  gwtQueryCondition.setCityCode(cityCode);
		  gwtQueryCondition.setBizZone(bizCode);
		  setConditonAndHotelShow(gwtQueryCondition);	  
	  }
	  
	  public void queryHotelList(String cityCode,String bizCode,int pageNo){
		  gwtQueryCondition.setCityCode(cityCode);
		  gwtQueryCondition.setBizZone(bizCode);
		  setConditonAndHotelShow(gwtQueryCondition);
		  
	  }
	  
	  
	  public void setHotelResult(HotelPageForWebBean hotelpageForWebBean){
			setHotelList(hotelpageForWebBean);
		}
		
		private void setHotelList(HotelPageForWebBean hotelpageForWebBean){
			cur_hotelPageForWebBean = hotelpageForWebBean;
			RootPanel.get("hotelList").clear();
			//找出中旅酒店
			List<HotelResultVO> result = hotelpageForWebBean.getList();
			List<HotelResultVO> ctsHotelList = getCtsHotels(result);
			super.setPageParams(ctsHotelList.size(), PAGENUM_SHOW, super.getCurPage());
			mainPagePanel = super.getMainPageBar();
			RootPanel.get("bottomPage").clear();
			RootPanel.get("bottomPage").add(mainPagePanel);//分页
			int startIndex = (super.getCurPage()-1)*PAGENUM_SHOW;
			int endIndex = startIndex+PAGENUM_SHOW;
			endIndex = (ctsHotelList.size()<endIndex)?ctsHotelList.size():endIndex;
			
			for(int i=startIndex ;i<endIndex;i++){
				HotelResultForWebVO hotelInfo = (HotelResultForWebVO)ctsHotelList.get(i);
				FlowPanel hotelDiv = new FlowPanel();
				hotelDiv.setStyleName("area_hotel_dl");
				HTML picHTML = new HTML("<a href='http://www.mangocity.com/hotelII/hotel-information.shtml?label=nest&hotelId="+hotelInfo.getHotelId()+"' target='_blank' title=''>" +		
	                    "<img src='"+hotelInfo.getOutPictureName()+"'  alt=''/></a>");
				picHTML.setStyleName("area_hotel_dt");
				hotelDiv.add(picHTML);
	            HTML hotelNameHTML = new HTML(""+
	                                          "<h3><a href='http://www.mangocity.com/hotelII/hotel-information.shtml?label=nest&hotelId="+hotelInfo.getHotelId()+"' target='_blank'>"+
	                                          hotelInfo.getChnName()+"</a></h3>");
	            hotelNameHTML.setStyleName("area_hotel_dd_tit");
	            hotelDiv.add(hotelNameHTML);
	            RootPanel.get("hotelList").add(hotelDiv);
				setRoomType(hotelInfo,hotelDiv);
			}
		}
		
		private void setRoomType(HotelResultForWebVO hotelInfo,FlowPanel hotelDiv){
			final FlowPanel roomPanel = new  FlowPanel();
			final FlexTable roomTable = new FlexTable();
			roomTable.setCellPadding(0);
			roomTable.setCellSpacing(0);
			roomPanel.setStyleName("area_hotel_dd_table");
			hotelDiv.add(roomPanel);
			roomPanel.add(roomTable);
			int currentRow = 0;
			setRoomTableHead(roomTable);
			currentRow+=1;
			List<RoomTypeVO> roomTypesVO = hotelInfo.getRoomTypes();
			int commoditiesNum = 0;
			for(int i = 0 ; i<roomTypesVO.size();i++){
				RoomTypeVO roomType = roomTypesVO.get(i);
				List<CommodityVO> commodities = roomType.getCommodities();
				for(int j = 0 ; j < commodities.size();j++){
					CommodityVO commodity = commodities.get(j);
					if( "pre_pay".equals(commodity.getPayMethod()) && "8".equals(commodity.getHdltype()) ){
						commoditiesNum++;
						if(commoditiesNum<2){   //超过1不显示
							roomTable.setText(currentRow, 0, roomType.getRoomtypeName());//说明
							roomTable.setText(currentRow, 1, commodity.getBedType());//床型
							roomTable.setText(currentRow, 2, commodity.getNet()); //宽带
							roomTable.setText(currentRow, 3, commodity.getBreakfastNum());//早餐
							roomTable.setWidget(currentRow, 4, new Anchor(commodity.getCurrencySymbol()+commodity.getAvlPrice(),true));//日均价
							roomTable.setWidget(currentRow, 5, addBookButton(commodity,hotelInfo.getHotelId()));
							roomTable.getFlexCellFormatter().addStyleName(currentRow, 1, "tdcenter");
							roomTable.getFlexCellFormatter().addStyleName(currentRow, 2, "tdcenter");
							roomTable.getFlexCellFormatter().addStyleName(currentRow, 3, "tdcenter");
							roomTable.getFlexCellFormatter().addStyleName(currentRow, 4, "tdcenter");
							roomTable.getFlexCellFormatter().addStyleName(currentRow, 5, "tdcenter");
							currentRow++;
					   }
					}
				}
			}
		}
		
		private void setRoomTableHead(FlexTable roomTable){
			roomTable.setText(0, 0, "房型");
			roomTable.getFlexCellFormatter().addStyleName(0, 0, "w155 area_tit");
			roomTable.setText(0, 1, "床型");
			roomTable.getFlexCellFormatter().addStyleName(0, 1, "w62 area_tit");
			roomTable.setText(0, 2, "宽带");
			roomTable.getFlexCellFormatter().addStyleName(0, 2, "w41 area_tit");
			roomTable.setText(0, 3, "早餐");
			roomTable.getFlexCellFormatter().addStyleName(0, 3, "w52 area_tit");
			roomTable.setText(0, 4, "日均价");
			roomTable.getFlexCellFormatter().addStyleName(0, 4, "w77 area_tit");
			roomTable.setText(0, 5, "操作");
			roomTable.getFlexCellFormatter().addStyleName(0, 5, "w129 area_tit");
		}
		
		
		
		// 该方法继承父类的方法
		public void  onclickService(){
			setHotelList(cur_hotelPageForWebBean);
	    }

		
		private Button addBookButton(final CommodityVO commodity,final long hotelId){
		    Button bookButton = new Button();
			//可预订
		    bookButton.getElement().setId(String.valueOf(commodity.getPriceTypeId()));
		    bookButton.getElement().setAttribute("name", "bookHotel");
		    if(/*commodity.isCanBook() && */ commodity.isShow()){
		    	bookButton.setText("查看");
		    	bookButton.setStyleName("bookbtn");
		    	bookButton.addClickHandler(new ClickHandler(){
		    		public void onClick(ClickEvent event){
		    			String url = "http://www.mangocity.com/hotelII/hotel-information.shtml?label=nest&hotelId="+hotelId;
		    			Window.open(url,"_blank","resizable=yes,scrollbars=yes");
		    		}
		    	});
		    }else{  //不可预定，1满房，2其他,这里不能用NotBookReason.Roomful
		    	if("满房".equals(commodity.getCantbookReason())){
		    		bookButton.setText("满房");
		    		bookButton.setStyleName("fullbtn");
		    	}else{
		    	    bookButton.setText("查看");
		     	    bookButton.setTitle(commodity.getCantbookReason());
		     	    bookButton.setStyleName("viewbtn");
		     	    bookButton.addClickHandler(new ClickHandler(){
		     	   	    public void onClick(ClickEvent event) {
		     	            //Window.alert(commodity.getCantbookReason());
		     	   	    }
		    	    });
		         }
		    }
		   return bookButton;
		}
		
	     //过滤中旅酒店
		private List<HotelResultVO> getCtsHotels(List<HotelResultVO> hotelRestultVOList){
			List<HotelResultVO> ctsHotelsList= new ArrayList<HotelResultVO>();
			//设计渠道号
			for(int i=0;i<hotelRestultVOList.size();i++){
				HotelResultVO hotelResultVO = hotelRestultVOList.get(i);
			    for(int j=0;j<hotelResultVO.getRoomTypes().size();j++){
					RoomTypeVO roomType = hotelResultVO.getRoomTypes().get(j);
					List<CommodityVO> commodities = roomType.getCommodities();
					for(int k=0;k<commodities.size();k++){
						CommodityVO commodity = commodities.get(k);
						if("pre_pay".equals(commodity.getPayMethod()) && "8".equals(commodity.getHdltype()) ){
						    hotelResultVO.setCooperateChannel("8");
					   }
				   }
			    }
			}
			//添加中旅酒店
		    for(int i=0;i<hotelRestultVOList.size();i++){
		    	HotelResultVO hotelResultVO = hotelRestultVOList.get(i);
		    	if("8".equals(hotelResultVO.getCooperateChannel())){
		    		ctsHotelsList.add(hotelResultVO);
		    	}
		    }
			return ctsHotelsList;
		}
		
		private void cleansomeDiv(){
			RootPanel.get("hotelList").clear();
			RootPanel.get("bottomPage").clear();
		}	
}
