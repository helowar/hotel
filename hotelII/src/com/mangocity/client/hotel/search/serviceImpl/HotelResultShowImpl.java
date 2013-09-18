package com.mangocity.client.hotel.search.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Cookies;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.mangocity.client.hotel.gwt.queryCondition.GWTQueryCondition;
import com.mangocity.client.hotel.panel.NestAnchorForLion;
import com.mangocity.client.hotel.search.service.HotelResultShow;
import com.mangocity.hotel.common.vo.CommentSummaryVO;
import com.mangocity.hotel.common.vo.HotelPromoVO;
import com.mangocity.hotel.common.vo.PreSaleVO;
import com.mangocity.hotel.search.vo.CommodityVO;
import com.mangocity.hotel.search.vo.HotelPageForWebBean;
import com.mangocity.hotel.search.vo.HotelResultForWebVO;
import com.mangocity.hotel.search.vo.HotelResultVO;
import com.mangocity.hotel.search.vo.RoomTypeVO;
import com.mangocity.hotel.search.vo.SaleItemVO;
import com.mangocity.client.MonitorGWTPageService;
import com.mangocity.client.MonitorGWTPageServiceAsync;
public class HotelResultShowImpl extends HotelListPageImpl implements HotelResultShow {
	private static final String SYMBOL_ON ="on";
	private static final String SYMBOL_OFF ="off";
	private static final String CHINA_SYMBOL_HAVE ="有";
	private static final String CHINA_SYMBOL_NOHAVE ="无";
	private static final int SHOW_ROOM_COUNT = 3;
	private static final String PRICE_NOTSHOWREASON = "该价格仅适用于芒果会员，请先登录";
	//private static final 
	//每天的价格框共享
	private final DecoratedPopupPanel saleItemPopup = new DecoratedPopupPanel(true);
	private final FlexTable saleTable = new FlexTable();
	private final static int ROOMTABLESPAN = 9;//该值可能会变化
	private MonitorGWTPageServiceAsync monitorGWTPageService = GWT.create(MonitorGWTPageService.class);
	//商品的促销信息提示框，共享
	private final DecoratedPopupPanel commodityPromoPopup = new DecoratedPopupPanel(true);
	private final HTML commodityPromoHTML = new HTML("abccdfd");
	//分页的bar
	private HorizontalPanel mainPagePanel ;
	private HorizontalPanel samplePagePanel ; 
	
	final HotelQueryAndShowImpl hotelQueryAndShowImpl = HotelQueryAndShowImpl.getInstance();
	final GWTQueryCondition queryCondtion = hotelQueryAndShowImpl.gwtQueryCondition;
	private String memberId ;//取cookie中的会员ID
	
	public void setHotelResult(HotelPageForWebBean hotelpageForWebBean){
		//Document document = Document.get();
		initSomePopup();
		int hotelCount = RootPanel.get("f_hotelCount").getElement().getPropertyInt("value");
		super.setPageParams(hotelCount, hotelpageForWebBean.getPageSize(), hotelpageForWebBean.getPageIndex());
		mainPagePanel = super.getMainPageBar();
		samplePagePanel = super.getSamplePageBar();
		RootPanel.get("hotelCount").clear();
		RootPanel.get("hotelCount").add(new InlineHTML("<em class='feedback'>共为您找到<strong>" +
				+hotelCount+"</strong>家酒店</em><h2>预订酒店</h2>"));
		setHotelListTop();
		
		RootPanel.get("bottomPage").add(mainPagePanel);//分页
		setCommodityDiv(hotelpageForWebBean);
	}
	
	//分布显示酒店，一个一个酒店的商品信息展示
	public void setHotelResult(HotelPageForWebBean hotelpageForWebBean,int firstIndex,int endIndex){
		if(firstIndex == 1){
			initSomePopup();
			int hotelCount = RootPanel.get("f_hotelCount").getElement().getPropertyInt("value");
			super.setPageParams(hotelCount, hotelpageForWebBean.getPageSize(), hotelpageForWebBean.getPageIndex());
			mainPagePanel = super.getMainPageBar();
			samplePagePanel = super.getSamplePageBar();
			RootPanel.get("hotelCount").clear();
			RootPanel.get("hotelCount").add(new InlineHTML("<em class='feedback'>共为您找到<strong>" +
					+hotelCount+"</strong>家酒店</em><h2>预订酒店</h2>"));		
			setHotelListTop();
			
			RootPanel.get("bottomPage").add(mainPagePanel);//分页
		}
		setCommodityDiv(hotelpageForWebBean,firstIndex,endIndex);
	}
	
	public void setCommodityDiv(HotelPageForWebBean hotelpageForWebBean,int firstIndex,int endIndex){
		initSomePopup();
		List<HotelResultVO> hotelVOList = hotelpageForWebBean.getList();
		int size = hotelVOList.size()<endIndex?hotelVOList.size():endIndex;
		for(int i = firstIndex - 1; i<size;i++){
			HotelResultForWebVO hotelInfo = (HotelResultForWebVO)hotelVOList.get(i);
			setPrepayhint(hotelInfo);
			String hotelId = String.valueOf(hotelInfo.getHotelId());
			RootPanel.get("date_"+hotelId).clear();
			RootPanel.get("date_"+hotelId).setStyleName("hoteldata");
			Panel hotelDiv = RootPanel.get("date_"+hotelId);
			setRoomType(hotelInfo,hotelDiv);
			
		}
		//防止在酒店基本信息有，商品信息没有该酒店
		String hotelIdsStr = hotelpageForWebBean.getHotelIdsStr();
		if(hotelIdsStr!=null){
			String[] hotelIds = hotelIdsStr.split(",");
			for(int i=0 ;i<hotelIds.length;i++){
				if(i==endIndex){break;}//超过最大的值的话，返回
				String hotelId = hotelIds[i];
				if("hoteldata roomtypeload".equals(RootPanel.get("date_"+hotelId).getStyleName())){
					RootPanel.get("date_"+hotelId).setStyleName("hoteldata");
				}
			}
		}
	}
    
	// 该方法继承父类的方法
	public void  onclickService(){
		queryCondtion.setPageNo(super.getCurPage());
		hotelQueryAndShowImpl.setConditonAndHotelShow(queryCondtion);
		monitorGWTPageService.pageMethod(null);
	}
	
	private void setPrepayhint(HotelResultForWebVO hotelInfo){
		if(hotelInfo.isPrepayHotel()){
			RootPanel.get("prepayHint"+hotelInfo.getHotelId()).setVisible(true);
		}
	}
	
	public void setHotelListTop(){
		Panel panelTop = RootPanel.get("topPage");
    	InlineHTML html1 = new InlineHTML("<h2><span>酒店列表</span></h2>");
    	html1.addStyleName("reqtab");
    	panelTop.add(html1); 
    	
    	//醒狮活动结束  --2012-9-19 delete by wangjian
    	/*FlowPanel titlePanel = new FlowPanel();
		titlePanel.setStyleName("req_tjfb");
		final NestAnchorForLion[] tabAnchor = new NestAnchorForLion[4];
    	tabAnchor[0] = new NestAnchorForLion("全城风暴",queryCondtion,1,tabAnchor);
    	tabAnchor[0].setClickHandler();
    	titlePanel.add(tabAnchor[0]);
    	tabAnchor[1] = new NestAnchorForLion("半价疯抢",queryCondtion,2,tabAnchor);
    	tabAnchor[1].setClickHandler();
    	titlePanel.add(tabAnchor[1]);
    	tabAnchor[2] = new NestAnchorForLion("7折特惠",queryCondtion,3,tabAnchor);
    	tabAnchor[2].setClickHandler();
    	titlePanel.add(tabAnchor[2]);
    	tabAnchor[3] = new NestAnchorForLion("底价放送",queryCondtion,4,tabAnchor);
    	tabAnchor[3].setClickHandler();
    	titlePanel.add(tabAnchor[3]);   	
    	panelTop.add(titlePanel);
    	for(int i = 0; i<tabAnchor.length;i++){
			if((i+1) == queryCondtion.getPromoteType()){
				tabAnchor[i].setStyleName("req_tjfb_bf");
				continue;
			}
			tabAnchor[i].setStyleName("");
		}*/
		String mapHref= "http://hotel.mangocity.com/hotelEmap/hotelEmap-list-"+queryCondtion.getCityCode().toLowerCase()+".html";

    	InlineHTML htmlMapList = new InlineHTML("<div class='req_map'><a href='"+mapHref+"' target='_blank'>地图展示酒店</a></div>");
    	htmlMapList.addStyleName("reqtab"); 
    	panelTop.add(htmlMapList);
    	//分页
    	panelTop.add(samplePagePanel);
    	addSomeElment();
	}
	
	private void setHotelSortClickEvent(RadioButton radio,final int sortType){
		radio.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				queryCondtion.setPageNo(1);
				queryCondtion.setSorttype(sortType);
				queryCondtion.setSortUpOrDown(1);
				hotelQueryAndShowImpl.setConditonAndHotelShow(queryCondtion);
			}
		});
	}
	
	private void addSomeElment(){
		HorizontalPanel hotelSortPanel=new HorizontalPanel();
		hotelSortPanel.setWidth("740px");
		Label hotelSortLabel=new Label("排序：");
		hotelSortPanel.add(hotelSortLabel);
		hotelSortPanel.setCellWidth(hotelSortLabel,"40px");
		RadioButton mangocitySort=new RadioButton("hotelSort","芒果推荐");
		setHotelSortClickEvent(mangocitySort,1);
		RadioButton priceSort=new RadioButton("hotelSort","价格从低到高");
		setHotelSortClickEvent(priceSort,2);
		RadioButton starSort=new RadioButton("hotelSort","星级从高到低");	
		setHotelSortClickEvent(starSort,3);
		hotelSortPanel.add(mangocitySort);
		hotelSortPanel.setCellWidth(mangocitySort,"80px");
		hotelSortPanel.add(priceSort);
		hotelSortPanel.setCellWidth(priceSort,"100px");
		hotelSortPanel.add(starSort);
		hotelSortPanel.setCellWidth(starSort,"100px");
			
		if(queryCondtion.getSorttype()==1){
			mangocitySort.setValue(true);
		}else if(queryCondtion.getSorttype()==2){
			priceSort.setValue(true);
		}else if(queryCondtion.getSorttype()==3){
			starSort.setValue(true);
		}
		
		CheckBox prepayChekcBox=setShowOnlyPrepay();
		hotelSortPanel.add(prepayChekcBox);
		hotelSortPanel.setCellHorizontalAlignment(prepayChekcBox,hotelSortPanel.ALIGN_RIGHT);
		Panel hotelSortDiv = RootPanel.get("hotelSortDiv");
		hotelSortDiv.add(hotelSortPanel);
	}

	private CheckBox setShowOnlyPrepay() {
		final CheckBox prepayCheckBox = new CheckBox("只显示预付酒店<font class='orange'>（预付特惠）</font>",true);
		prepayCheckBox.setName("showPrepayRoom");
		if ("pre_pay".equals(queryCondtion.getPayMethod())) {
			prepayCheckBox.setValue(true);
		} else {
			prepayCheckBox.setValue(false);
		}
		prepayCheckBox.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				boolean checked = prepayCheckBox.getValue();
				if (checked) {
					queryCondtion.setPayMethod("pre_pay");
					queryCondtion.setPageNo(1);
					hotelQueryAndShowImpl.setConditonAndHotelShow(queryCondtion);
				} else {

					queryCondtion.setPageNo(1);
					queryCondtion.setPayMethod(null);
					hotelQueryAndShowImpl.setConditonAndHotelShow(queryCondtion);
				}
			}

		});
		
		return prepayCheckBox;

	}
	
	private void setHotelListForHotel(List<HotelResultVO> result){
		FlowPanel panelMiddle = new FlowPanel();
		panelMiddle.addStyleName("queryWrap");
		RootPanel.get("hotelList").add(panelMiddle);
		for(int i=0 ;i<result.size();i++){
			HotelResultForWebVO hotelInfo = (HotelResultForWebVO)result.get(i);
			FlowPanel hotelDiv = new FlowPanel();
			hotelDiv.setStyleName("queryBox");
			FlowPanel hotelPic = new FlowPanel();
			hotelPic.addStyleName("img360");
			String hotelImgUrl = hotelInfo.getOutPictureName();
			InlineHTML picHTML = new InlineHTML("<a href='hotel-information.shtml?hotelId="+hotelInfo.getHotelId()+"&inDate="+queryCondtion.getInDate()+"&outDate="+queryCondtion.getOutDate()+"' class='imgWrap' target='_blank'><img width='75px' height='75px' src='"+hotelImgUrl+"' /></a>");
			hotelPic.add(picHTML);
			hotelDiv.add(hotelPic);
			
			StringBuffer hotelContextBf = new StringBuffer();
			hotelContextBf.append("<h2 class='hotelName "+hotelInfo.getCommendType()+"'><a href='hotel-information.shtml?hotelId="+hotelInfo.getHotelId()+"&inDate="+queryCondtion.getInDate()+"&outDate="+queryCondtion.getOutDate()+"' target='_blank'>"
					+hotelInfo.getChnName()+"</a>");
			String hotelStar = hotelInfo.getHotelStar();
			if(hotelStar.contains("star")){
			     hotelContextBf.append("<em class='hotelstar "+hotelInfo.getHotelStar()+"' title=''></em></h2>");
			}else{
				hotelContextBf.append("&nbsp;&nbsp;<em class='hotelstarName'>"+hotelInfo.getHotelStar()+"</em></h2>");
			}
			//hotelContextBf.append("<a htid='3357' htname='"+hotelInfo.getChnName()+"' href='#' class='tocompare'>加入对比</a>");
			String autoIntroduce = hotelInfo.getAutoIntroduce();
			//只显示一行
			if(autoIntroduce!=null && autoIntroduce.length() > 48){
				autoIntroduce = autoIntroduce.substring(0,48)+"...";
			}
			hotelContextBf.append("<p class='hotelDescription'>"+autoIntroduce+"</p>");
			hotelContextBf.append("<ul class='hotelInfo'>");
			hotelContextBf.append("<li class='map'><a href='javascript:googleMapPanel("+hotelInfo.getHotelId()+");' name='eMap' >电子地图</a></li>");
			hotelContextBf.append("<li class='businessarea'><span>商业区：</span><a href='hotel-query.shtml?cityCode="+queryCondtion.getCityCode()+"&bizCode="+hotelInfo.getBizZone()+"&inDate="+queryCondtion.getInDate()+"&outDate="+queryCondtion.getOutDate()+"' name='bizZone'>"+hotelInfo.getBizZoneValue()+"</a></li>");
			hotelContextBf.append("<li class='spacstructure'><span>特色设施：</span>");
			String flagOnForPlane = hotelInfo.isForPlane() ? SYMBOL_ON : SYMBOL_OFF;
			String flagHaveForPlane = hotelInfo.isForPlane() ? CHINA_SYMBOL_HAVE : CHINA_SYMBOL_NOHAVE;
			String flagOnForNet =hotelInfo.isForNetBand() ? SYMBOL_ON : SYMBOL_OFF;
			String flagHaveForNet = hotelInfo.isForNetBand() ? CHINA_SYMBOL_HAVE : CHINA_SYMBOL_NOHAVE;
			String flagOnForFreeGym =hotelInfo.isForFreeGym() ? SYMBOL_ON : SYMBOL_OFF;
			String flagHaveForFreeGym =hotelInfo.isForFreeGym() ? CHINA_SYMBOL_HAVE : CHINA_SYMBOL_NOHAVE;
			String flagOnForFreeStop=hotelInfo.isForFreeStop() ? SYMBOL_ON : SYMBOL_OFF;
			String flagHaveForFreeStop = hotelInfo.isForFreeStop()? CHINA_SYMBOL_HAVE : CHINA_SYMBOL_NOHAVE;
			String flagOnForFreePool= hotelInfo.isForFreePool()? SYMBOL_ON : SYMBOL_OFF;
			String flagHaveForFreePool = hotelInfo.isForFreePool()? CHINA_SYMBOL_HAVE : CHINA_SYMBOL_NOHAVE;
			hotelContextBf.append("<em class='spc_icon spc_01_"+flagOnForPlane+"' title='"+flagHaveForPlane+"接机服务'></em>");
			//hotelContextBf.append("<em class='spc_icon spc_02_"+flagOnForNet+"' title='"+flagHaveForNet+"宽带服务'></em>");
			hotelContextBf.append("<em class='spc_icon spc_03_"+flagOnForFreeGym+"' title='"+flagHaveForFreeGym+"免费健身设施'></em>");
			hotelContextBf.append("<em class='spc_icon spc_04_"+flagOnForFreeStop+"' title='"+flagHaveForFreeStop+"免费停车场'></em>");
			hotelContextBf.append("<em class='spc_icon spc_05_"+flagOnForFreePool+"' title='"+flagHaveForFreePool+"免费游泳池'></em></li>");
			hotelContextBf.append("</ul>");
			CommentSummaryVO commentVO = hotelInfo.getCommentSummaryVO();
			String geoDistance = hotelInfo.getGeoDistance();
			if(null!=geoDistance && !"".equals(geoDistance)){
				hotelContextBf.append("<ul class='hotelEvaluation'>"+geoDistance+"</ul>");
			}
			hotelContextBf.append("<ul class='hotelEvaluation'><li>酒店口碑：<em>"+commentVO.getCommendUp()+"人推荐/"+commentVO.getCommendDown()+"人不推荐</em></li><li>得分：<em>"+commentVO.getAverAgepoint()+"分</em></li>");
			hotelContextBf.append("<li><a href='http://club.mangocity.com/hotelcomment/default.aspx?hotelId="+hotelInfo.getHotelId()+"' target='_blank' name='commends'>查看"+commentVO.getCommentNum()+"位住客点评&gt;&gt;</a></li>");
			hotelContextBf.append ("</ul>");

			String hotelContext = hotelContextBf.toString();
			HTML hotelInfoDiv = new HTML(hotelContext);
			hotelInfoDiv.addStyleName("txtWrap");
			hotelDiv.add(hotelInfoDiv);
			
			panelMiddle.add(hotelDiv);
			setRoomType(hotelInfo,hotelDiv);
			
		}
	}
	
	// add by diandian.hou
	public void setCommodityDiv(HotelPageForWebBean hotelpageForWebBean){
		initSomePopup();
		List<HotelResultVO> hotelVOList = hotelpageForWebBean.getList();
		for(int i = 0; i< hotelVOList.size();i++){
			HotelResultForWebVO hotelInfo = (HotelResultForWebVO)hotelVOList.get(i);
			String hotelId = String.valueOf(hotelInfo.getHotelId());
			RootPanel.get("date_"+hotelId).clear();
			RootPanel.get("date_"+hotelId).setStyleName("hoteldata");
			Panel hotelDiv = RootPanel.get("date_"+hotelId);
			setRoomType(hotelInfo,hotelDiv);
			//filterNoRoomTypeHotel(hotelInfo);
		}
	}
		
	public void setRoomType(HotelResultForWebVO hotelInfo, Panel hotelDiv) {		
		FlowPanel roomPanel = new FlowPanel();
		roomPanel.setStyleName("hoteldata");
		final FlexTable roomTable = new FlexTable();
		roomTable.setCellPadding(0);
		roomTable.setCellSpacing(0);
		roomPanel.add(roomTable);
		hotelDiv.add(roomPanel);
        int currentRow = 0;
        setRoomTableHead(roomTable);
		currentRow+=1;
		//房型数是否大于3个
		boolean roomTypeBiggerShowRoom = false;
		//放置从第4个房型开始的Rowcount 
		List<Integer> displayRows = new ArrayList<Integer>();
		List<RoomTypeVO> roomTypesVO = hotelInfo.getRoomTypes();
		for (int i = 0; i < roomTypesVO.size(); i++) {
			RoomTypeVO roomType = roomTypesVO.get(i);
			String roomTypeName = roomType.getRoomtypeName();
			//如果房间名称太长，取10个字显示
			String roomTypeNameShow = roomTypeName;
			if(roomTypeName.length() > 9+1){
				roomTypeNameShow = roomTypeName.substring(0, 9)+"...";
			}
            final Anchor roomtypeNameHL = new Anchor(roomTypeNameShow);
            roomtypeNameHL.getElement().setAttribute("name", "roomtypeName");
			if(roomTypeName.length() > 9+1){
				roomtypeNameHL.setTitle(roomTypeName);
			}
			roomtypeNameHL.setStyleName("roomtype viewDefault");
			roomTable.setWidget(currentRow, 0, roomtypeNameHL);
			List<CommodityVO> commodities = roomType.getCommodities();
			for (int j = 0; j < commodities.size(); j++) {
				CommodityVO commodity = commodities.get(j);	
				roomTable.setWidget(currentRow + j, 1, addCommodityNameAndPromp(commodity));//说明
				roomTable.setText(currentRow + j, 2, commodity.getBedType());//床型
				roomTable.setText(currentRow + j, 3, commodity.getNet()); //宽带
				roomTable.setText(currentRow + j, 4, commodity.getBreakfastNum());//早餐
				roomTable.setWidget(currentRow + j, 5, addAvlPriceHL(commodity));//日均价
				roomTable.setWidget(currentRow + j, 6, new InlineHTML("<em class='recash'>&yen;"+commodity.getAvlReturnCashNum()+"</em>"));//返现
				roomTable.setText(currentRow + j, 7, commodity.getPayMethodStr());//支付方式
				roomTable.setWidget(currentRow + j, 8, addBookButton(commodity));
				if(i > SHOW_ROOM_COUNT -1 ){
					roomTypeBiggerShowRoom = true;
					roomTable.getRowFormatter().setVisible(currentRow + j, false);
					displayRows.add(currentRow + j);
				}
				// 在第一个商品下添加一个roomInfo的隐藏的Div
				if (j == 0) {
                    final int tempRow = currentRow + 1;
					insertRoomInfoDiv(roomTable,tempRow, roomType,roomtypeNameHL);
					currentRow+=1;
					final int roomDivRow = tempRow;
					//添加效果
					roomtypeNameHL.addClickHandler(new ClickHandler(){
						public void onClick(final ClickEvent event) {
							if(roomTable.getRowFormatter().isVisible(roomDivRow)){								
						 	    roomTable.getRowFormatter().setVisible(roomDivRow, false);
						 	    roomtypeNameHL.setStyleName("roomtype viewDefault");
							}else{
								roomTable.getRowFormatter().setVisible(roomDivRow, true);
								roomtypeNameHL.setStyleName("roomtype");
							}
						}
					});	
				}
			}
			currentRow += commodities.size();
		}
	// 查看更多房型
		FlowPanel moreRoomPanel = new FlowPanel();
		moreRoomPanel.setStyleName("viewmore");
		//添加酒店促销信息
		InlineHTML hotelPreSaleHTML = new InlineHTML();
		if(!hotelInfo.getPreSaleLst().isEmpty()){
			StringBuffer context = new StringBuffer("促销信息:");
			for(int i = 0 ; i < hotelInfo.getPreSaleLst().size(); i++){
				PreSaleVO preSale = hotelInfo.getPreSaleLst().get(i);
				context.append(preSale.getPreSaleContent());
				context.append(preSale.getPreSaleBeginEnd());
			}
			hotelPreSaleHTML.setHTML(context.toString());
			moreRoomPanel.add(hotelPreSaleHTML);
		}
		
		if (roomTypeBiggerShowRoom == true) {
			final Anchor moreRoomHL = new Anchor("查看所有房型/价格");
			moreRoomHL.setStyleName("moreroom viewDefault");
			moreRoomHL.getElement().setAttribute("name", "moreroom");
			final List<Integer> fDisplayRows = displayRows;
			// 添加事件,显示和隐藏房型
			moreRoomHL.addClickHandler(new ClickHandler() {
				public void onClick(final ClickEvent event) {
					for (int i = 0; i < fDisplayRows.size(); i++) {
						int row = fDisplayRows.get(i);
						if (roomTable.getRowFormatter().isVisible(row)) {
							roomTable.getRowFormatter().setVisible(row, false);
							// 如果roominfo是显示的，也关闭 
							if (row+1<roomTable.getRowCount() && roomTable.getRowFormatter().isVisible(row + 1) && ROOMTABLESPAN==roomTable.getFlexCellFormatter().getColSpan(row+1, 0)) {
								roomTable.getRowFormatter().setVisible(row + 1,false);
								Element roomNameHL = (Element)(roomTable.getCellFormatter().getElement(row, 0).getChild(0));
								roomNameHL.setClassName("roomtype viewDefault");
							}
							// 修改样式和内容（收起）
							moreRoomHL.setText("查看所有房型/价格");
							moreRoomHL.setStyleName("moreroom viewDefault");
						} else {
							roomTable.getRowFormatter().setVisible(row, true);
							moreRoomHL.setText("收起");
							moreRoomHL.setStyleName("moreroom");
						}
					}
				}
			});
			moreRoomPanel.add(moreRoomHL);
		}
		roomPanel.add(moreRoomPanel);
	}
		
	private void setRoomTableHead(FlexTable roomTable){
		roomTable.setText(0, 0, "房型");
		roomTable.getFlexCellFormatter().addStyleName(0, 0, "w150 tit");
		roomTable.setText(0, 1, "说明");
		roomTable.getFlexCellFormatter().addStyleName(0, 1, "w125 tit");
		roomTable.setText(0, 2, "床型");
		roomTable.getFlexCellFormatter().addStyleName(0, 2, "w64 tit");
		roomTable.setText(0, 3, "宽带");
		roomTable.getFlexCellFormatter().addStyleName(0, 3, "w60 tit");
		roomTable.setText(0, 4, "早餐");
		roomTable.getFlexCellFormatter().addStyleName(0, 4, "w60 tit");
		roomTable.setText(0, 5, "日均价");
		roomTable.getFlexCellFormatter().addStyleName(0, 5, "w70 tit");
		roomTable.setText(0, 6, "日均返现");
		roomTable.getFlexCellFormatter().addStyleName(0, 6, "w60 tit");
		roomTable.setText(0, 7, "支付方式");
		roomTable.getFlexCellFormatter().addStyleName(0, 7, "w60 tit");
		roomTable.setText(0, 8, "操作");
		roomTable.getFlexCellFormatter().addStyleName(0, 8, "w60 tit");
		
	}
	
	private void insertRoomInfoDiv(final FlexTable roomTable ,final int insertRow ,RoomTypeVO roomType,final Anchor roomtypeNameHL){
		roomTable.insertRow(insertRow);
		roomTable.getFlexCellFormatter().setColSpan(insertRow, 0, ROOMTABLESPAN);
		roomTable.getRowFormatter().addStyleName(insertRow, "roominfomation");
		roomTable.getRowFormatter().setVisible(insertRow, false);
		StringBuffer roomInfoContextBf = new StringBuffer();
		roomInfoContextBf.append( "<ul class='roominfolist'>");
		roomInfoContextBf.append(" <li class='w200'>房间面积："+roomType.getRoomAcreage()+"</li>");
		roomInfoContextBf.append(" <li class='w360'>所属楼层："+roomType.getRoomFloor()+"</li>");
		roomInfoContextBf.append(" <li class='w200'>最多可入住人数："+roomType.getMaxNumOfPersons()+"</li>");
		roomInfoContextBf.append(" <li class='w360'>可否加床："+roomType.getFlagAddBed()+"</li>");
		String otherExplaien = roomType.getOtherExplain();
		if(null != otherExplaien && otherExplaien.trim().length() > 0) {
			roomInfoContextBf.append(" <li class='w360'>其他说明："+roomType.getOtherExplain()+"</li></ul>");	
		}		
        HTML roomInfoDiv = new HTML(roomInfoContextBf.toString());
		Anchor hiddenRoomInfoHL = new Anchor("隐藏");
		hiddenRoomInfoHL.setStyleName("hidebox");
		final int roomInfoRow = insertRow;
		hiddenRoomInfoHL.addClickHandler(new ClickHandler(){
						public void onClick(final ClickEvent event) {							
						 	   roomTable.getRowFormatter().setVisible(roomInfoRow, false);
						 	   roomtypeNameHL.setStyleName("roomtype viewDefault");
						}
					});	
		FlowPanel roomPanel = new FlowPanel();
		roomPanel.setStyleName("roomInfo");
		roomPanel.add(hiddenRoomInfoHL);
		roomPanel.add(roomInfoDiv);
		roomTable.setWidget(insertRow, 0, roomPanel);
	}
		
	//给均价添加浮动层
	private Anchor addAvlPriceHL(final CommodityVO commodity){
		if((null==memberId||"".equals(memberId))&&commodity.isHasSupplier()&& !commodity.isShowMemberPrice()&&"pre_pay".equals(commodity.getPayMethod())) {
	     //TODO test 	
		    final Anchor avlPriceHL = new Anchor("会员价",true);
			avlPriceHL.setStyleName("hasbf yen");
			avlPriceHL.addMouseOverHandler(new MouseOverHandler(){
				public void onMouseOver(MouseOverEvent event){
					saleItemPopup.setWidth("470px"); 
					saleItemPopup.setPopupPosition(avlPriceHL.getElement().getAbsoluteLeft()-10, avlPriceHL.getElement().getAbsoluteTop()+20);
					saleTable.removeAllRows();
					saleTable.getRowFormatter().setStyleName(0, "tipheadnobg");
					saleTable.setText(0,0,PRICE_NOTSHOWREASON);
					saleItemPopup.show();
				}
			});
			avlPriceHL.addMouseOutHandler(new MouseOutHandler(){
				public void onMouseOut(MouseOutEvent event){
					saleItemPopup.hide();
				}
			});
			
			return avlPriceHL;
		}else {
			final Anchor avlPriceHL = new Anchor(commodity.getCurrencySymbol()+commodity.getAvlPrice(),true);
			avlPriceHL.setStyleName("hasbf yen");
			avlPriceHL.addMouseOverHandler(new MouseOverHandler(){
				public void onMouseOver(MouseOverEvent event){
					saleItemPopup.setWidth("470px"); 
					saleItemPopup.setPopupPosition(avlPriceHL.getElement().getAbsoluteLeft()-10, avlPriceHL.getElement().getAbsoluteTop()+20);
					int days = commodity.getSaleInfoList().size();
					//得到周数，现在最多4周（28天）
					int weeksNum = (days - 1) / 7 + 1;
					saleTable.removeAllRows();
					saleTable.setText(0, 0, "周期");
					saleTable.getRowFormatter().setStyleName(0, "tiphead");
					int tempCommodityNum = 0;
					for (int week = 1; week <= weeksNum; week++) {
						int column = 0;
						saleTable.setText(week, column, "第"+week+"周");
						column++;
				        int tempdays = (week*7-days < 0)?week*7:days;
						for (;tempCommodityNum < tempdays ; tempCommodityNum++) {
							SaleItemVO saleItemVO = commodity.getSaleInfoList().get(tempCommodityNum);
							if(tempCommodityNum<=7){
							   saleTable.setText(0, column, saleItemVO.getWeekDay());
							   saleTable.getCellFormatter().setStyleName(0, column, "tiphead");
							}
							saleTable.setText(week, column, saleItemVO.getSalePriceStr());
							column++;
							//补齐最后一行的td
							if(week == weeksNum && week >= 2 ){
								for(int i = column+1;i<8;i++){
									saleTable.setText(week, i, "");
								}
							}
						}
						saleItemPopup.show();
					}
				}
			});
			avlPriceHL.addMouseOutHandler(new MouseOutHandler(){
				public void onMouseOut(MouseOutEvent event){
					saleItemPopup.hide();
				}
			});
			
			return avlPriceHL;
		}
	}
	
	//给商品促销信息加浮动框
   private FlowPanel addCommodityNameAndPromp(final CommodityVO commodity){
	   FlowPanel prompPanel = new FlowPanel();
	   String commodityName = commodity.getCommodityName();
	   String commodityNameShow = commodityName;
	   if(commodityName.length()>7+1){
		   commodityNameShow = commodityName.substring(0,7)+"...";
	   }
	   InlineHTML inHtml = new InlineHTML(commodityNameShow);
	   if(commodityName.length()>7+1){
	        inHtml.setTitle(commodityName);
	   }
	   inHtml.setStyleName("pricetypeName");
	   prompPanel.add(inHtml);
	   if(!commodity.getPromoLst().isEmpty()){
		   final Anchor giftAnchor = new Anchor();
		   giftAnchor.setStyleName("gifts");
		   prompPanel.add(giftAnchor);
		   giftAnchor.addMouseOverHandler(new MouseOverHandler(){
			   public void onMouseOver(MouseOverEvent event){
				   commodityPromoPopup.setWidth("400px");
				   commodityPromoPopup.setPopupPosition(giftAnchor.getElement().getAbsoluteLeft()-10, giftAnchor.getElement().getAbsoluteTop()+20);
				   commodityPromoPopup.show();
				   String content = "<p>通过芒果网预订且入住，可以享受以下优惠：</p>";
				   for(int i = 0 ; i< commodity.getPromoLst().size() ; i++){
					   HotelPromoVO promoVO = commodity.getPromoLst().get(i); 
					   content += "<p>"+promoVO.getPromoContent()+"</p>"
					                   +"<em>有效期："+promoVO.getPromoBeginDate()+"开始,"+promoVO.getPromoEndDate()+"结束。</em></ br>";
				   }
				   commodityPromoHTML.setHTML(content);
			   }
		   });
		   giftAnchor.addMouseOutHandler(new MouseOutHandler(){
			   public void onMouseOut(MouseOutEvent event){
				   commodityPromoPopup.hide();
			   }
		   });
	   }
	   return prompPanel;
   }
	
	//设置预定按钮
	private Button addBookButton(final CommodityVO commodity){
	    Button bookButton = new Button();
		//可预订
	    bookButton.getElement().setId(String.valueOf(commodity.getPriceTypeId()));
	    bookButton.getElement().setAttribute("name", "bookHotel");
	    if(commodity.isCanBook() &&  commodity.isShow()){	
	    	bookButton.setText("预订");
	    	bookButton.setStyleName("bookbtn");
	    	bookButton.addClickHandler(new ClickHandler(){
	    		public void onClick(ClickEvent event){
	    			History.newItem(HotelQueryAndShowImpl.BOOKTOKEN);
	    			hotelBookEvent(String.valueOf(commodity.getPriceTypeId()),commodity.getPayMethod());
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
	
	// this method is for one hotel
	public void setCommondityDivForOneHotel(HotelPageForWebBean hotelpageForWebBean){
		initSomePopup();
		HotelResultForWebVO hotelInfo = (HotelResultForWebVO)hotelpageForWebBean.getList().get(0);
		FlowPanel roomPanel = new FlowPanel();
		roomPanel.setStyleName("hoteldata");
		final FlexTable roomTable = new FlexTable();
		roomTable.setCellPadding(0);
		roomTable.setCellSpacing(0);
		roomPanel.add(roomTable);
		RootPanel.get("hoteldata").add(roomPanel);
		int currentRow = 0;
        setRoomTableHead(roomTable);
		currentRow+=1;
		List<RoomTypeVO> roomTypesVO = hotelInfo.getRoomTypes();
		for (int i = 0; i < roomTypesVO.size(); i++) {
			RoomTypeVO roomType = roomTypesVO.get(i);
			String roomTypeName = roomType.getRoomtypeName();
			//如果房间名称太长，取10个字显示
			String roomTypeNameShow = roomTypeName;
			if(roomTypeName.length() > 9+1){
				roomTypeNameShow = roomTypeName.substring(0, 9)+"...";
			}
            final Anchor roomtypeNameHL = new Anchor(roomTypeNameShow);
			if(roomTypeName.length() > 9+1){
				roomtypeNameHL.setTitle(roomTypeName);
			}
			roomtypeNameHL.getElement().setAttribute("name", "roomtypeName");
			roomtypeNameHL.setStyleName("roomtype viewDefault");
			roomTable.setWidget(currentRow, 0, roomtypeNameHL);
			List<CommodityVO> commodities = roomType.getCommodities();
			for (int j = 0; j < commodities.size(); j++) {
				CommodityVO commodity = commodities.get(j);
				roomTable.setWidget(currentRow + j, 1, addCommodityNameAndPromp(commodity));//说明
				roomTable.setText(currentRow + j, 2, commodity.getBedType());//床型
				roomTable.setText(currentRow + j, 3, commodity.getNet()); //宽带
				roomTable.setText(currentRow + j, 4, commodity.getBreakfastNum());//早餐
				roomTable.setWidget(currentRow + j, 5, addAvlPriceHL(commodity));//日均价
				roomTable.setWidget(currentRow + j, 6, new InlineHTML("<em class='recash'>&yen;"+commodity.getAvlReturnCashNum()+"</em>"));//返现
				roomTable.setText(currentRow + j, 7, commodity.getPayMethodStr());//支付方式
				roomTable.setWidget(currentRow + j, 8, addBookButton(commodity));
				// 在第一个商品下添加一个roomInfo的隐藏的Div
				if (j == 0) {
                    final int tempRow = currentRow + 1;
					insertRoomInfoDiv(roomTable,tempRow, roomType,roomtypeNameHL);
					currentRow+=1;
					final int roomDivRow = tempRow;
					//添加效果
					roomtypeNameHL.addClickHandler(new ClickHandler(){
						public void onClick(final ClickEvent event) {
							if(roomTable.getRowFormatter().isVisible(roomDivRow)){								
						 	    roomTable.getRowFormatter().setVisible(roomDivRow, false);
						 	    roomtypeNameHL.setStyleName("roomtype viewDefault");
							}else{
								roomTable.getRowFormatter().setVisible(roomDivRow, true);
								roomtypeNameHL.setStyleName("roomtype");
							}
						}
					});	
				}
			}
			currentRow += commodities.size();
		}		
	}
	//初始化浮动层 
	private void initSomePopup(){
		saleTable.setStyleName("tipborder");
		saleItemPopup.setWidget(saleTable);
		commodityPromoHTML.setStyleName("tipborder");
		commodityPromoPopup.setWidget(commodityPromoHTML);
		memberId = Cookies.getCookie("memberid");
	}
	
	//外部的js
	 public static native void hotelBookEvent(String pricetypeId,String payMethod) /*-{ 
         $wnd.hotelBookEvent(pricetypeId,payMethod); 
     }-*/;
}
