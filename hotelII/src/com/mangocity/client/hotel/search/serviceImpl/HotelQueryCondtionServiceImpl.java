package com.mangocity.client.hotel.search.serviceImpl;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.mangocity.client.HotelListSearchService;
import com.mangocity.client.HotelListSearchServiceAsync;
import com.mangocity.client.hotel.gwt.queryCondition.GWTQueryCondition;
import com.mangocity.client.hotel.panel.CheckBoxGroupPanel;
import com.mangocity.client.hotel.panel.DefinePricePanel;
import com.mangocity.client.hotel.panel.FloatingPopupPanel;
import com.mangocity.client.hotel.panel.RadioButtonGroupPanel;
import com.mangocity.client.hotel.panel.ShowSelectedItemPanel;
import com.mangocity.client.hotel.search.service.HotelQueryConditonService;
import com.mangocity.client.hotel.search.serviceImpl.HotelQueryConditonConstant.HotelBrand;
import com.mangocity.client.hotel.search.serviceImpl.HotelQueryConditonConstant.HotelFacility;
import com.mangocity.client.hotel.search.serviceImpl.HotelQueryConditonConstant.HotelPrice;
import com.mangocity.client.hotel.search.serviceImpl.HotelQueryConditonConstant.HotelStar;

/**
 * @author xieyanhui
 *
 */
public class HotelQueryCondtionServiceImpl implements HotelQueryConditonService {

	private HotelListSearchServiceAsync hotelListSearchService = GWT.create(HotelListSearchService.class);
	private final int brandSize =7;
	//各条件面板
	private RadioButtonGroupPanel hotelPriceRadioBtnPanel;
	private DefinePricePanel definePricePanel;
	private RadioButtonGroupPanel hotelBrandRadioBtnPanel;
	private FloatingPopupPanel moreHotelBandPanel;
	private CheckBoxGroupPanel hotelStarCBoxGroupPanel;
	private CheckBoxGroupPanel hotelFacilityCBoxGroupPanel;
	private FloatingPopupPanel hotelCommercialDistrictPanel;
	private FloatingPopupPanel administrativeDistictPanel;
	private FloatingPopupPanel transportHubPanel;
	public 	static GWTQueryCondition gwtQueryCondition;
	
	//提示所选择条件面板
	//依次为酒店价格、酒店集团（酒店品牌）、酒店星级、酒店设施、酒店所在商业区、行政区、交通枢纽
	private ShowSelectedItemPanel []selectedConditonPanel = new ShowSelectedItemPanel[7];
	
	
	public void setHotelQueryCondtion() {
		
		addSelectedConditonPanel("hotel_search_selectedItems");//添加显示所选择条件面板
		initSelectedConditonPanel();
		addHotelPriceRadioBtns();	//添加酒店价格单选按钮
		addDefinePriceLabel();		//添加酒店价格自定义标签
		addHotelGroupRadioBtns(); //添加酒店品牌单选按钮
		addMoreHotelGroup();    //添加更多品牌标签
		addHotelStarCheckBoxes();   //添加酒店星级复选按钮
		addHotelFacilityCheckBoxes();//酒店设施复选按钮
		addHotelLocation();//酒店地理位置
		
		selectedConditonPanel[0].setClickHandler(hotelPriceRadioBtnPanel);
		selectedConditonPanel[1].setClickHandler(hotelBrandRadioBtnPanel);
		selectedConditonPanel[2].setClickHandler(hotelStarCBoxGroupPanel);
		selectedConditonPanel[3].setClickHandler(hotelFacilityCBoxGroupPanel);
		selectedConditonPanel[4].setClickHandler(null);
		selectedConditonPanel[5].setClickHandler(null);
		selectedConditonPanel[6].setClickHandler(null);
	}
	
	public GWTQueryCondition getGWTQueryCondition(){
		
		String cityName = DOM.getElementById("id_startCity").getPropertyString("value");
		String cityCode = RootPanel.get("cityCode").getElement().getPropertyString("value");
		if(cityCode == null || "".equals(cityCode.trim()) ){
                	cityCode ="PEK";
		}
		String startDate = RootPanel.get("id_startDate").getElement().getPropertyString("value");
		String backDate = RootPanel.get("id_backDate").getElement().getPropertyString("value");
		String hotelName = RootPanel.get("hotelName").getElement().getPropertyString("value");
		String promoteType = RootPanel.get("f_promoteType").getElement().getPropertyString("value");
		String payMethod = RootPanel.get("f_payMethod").getElement().getPropertyString("value");
		String hotelPrice = selectedConditonPanel[0].getHiddenValue();
		String hotelGroup = selectedConditonPanel[1].getHiddenValue();
		String hotelStar = selectedConditonPanel[2].getHiddenValue();
		String hotelFacility = selectedConditonPanel[3].getHiddenValue();
		String bissiness = selectedConditonPanel[4].getHiddenValue();
		String distict = selectedConditonPanel[5].getHiddenValue();
		String geoId = selectedConditonPanel[6].getHiddenValue();
		String geoName = selectedConditonPanel[6].getHiddenText();
		if(	 null == gwtQueryCondition ){
			gwtQueryCondition = new GWTQueryCondition();
		}
		gwtQueryCondition.setCityCode(cityCode);
		gwtQueryCondition.setCityName(cityName);
		gwtQueryCondition.setInDate(startDate);
		gwtQueryCondition.setOutDate(backDate);
		
		if(payMethod!=null && !"".equals(payMethod)){
			gwtQueryCondition.setPayMethod(payMethod);
		}
		
		if (hotelName != null && hotelName.indexOf("酒店名称") > 0) {
			gwtQueryCondition.setHotelName(null);
		} else {
			gwtQueryCondition.setHotelName(hotelName.trim());
		}
		gwtQueryCondition.setFromChannel("1");//对应hotel-core中的SaleChannel.WEB，由于GWT的原因直接写成"1"
		gwtQueryCondition.setStarLeval(null);
		gwtQueryCondition.setMaxPrice(null);
		gwtQueryCondition.setMinPrice(null);
		gwtQueryCondition.setPromoteType(Integer.parseInt(promoteType));
		if(!"".equals(hotelStar) && hotelStar != null ){
		    hotelStar = hotelStar.substring(0, hotelStar.length()-1);
			gwtQueryCondition.setStarLeval(hotelStar);
		}
		if(!"".equals(hotelPrice)){
			gwtQueryCondition.setMaxPrice(hotelPrice.split("-")[1]);
			gwtQueryCondition.setMinPrice(hotelPrice.split("-")[0]);
		}
		gwtQueryCondition.setDistrict(distict);
		gwtQueryCondition.setBizZone(bissiness);
		gwtQueryCondition.setGeoId(geoId);
		gwtQueryCondition.setGeoName(geoName.trim());
		gwtQueryCondition.setSpecialRequestString(hotelFacility);
		gwtQueryCondition.setHotelGroup(hotelGroup);
		
		//获取渠道编号 add by hushunqiang
		String projectCode = RootPanel.get("projectCode").getElement().getPropertyString("value");
		gwtQueryCondition.setProjectCode(projectCode);
		return gwtQueryCondition;
	}
	
	private void setValueAndViewHiddenPanel(ShowSelectedItemPanel showSelectedItemPanel, String value,String text) {
		if ((null != value && !"".equals(value)) || (null!=text && !"".equals(text))) {
			showSelectedItemPanel.setValueAndViewHiddenPanel(value, text);
		}
	}
	
	private void initSelectedConditonPanel(){
		String f_bizCode = RootPanel.get("f_bizCode").getElement().getPropertyString("value");
		String f_bizValue = RootPanel.get("f_bizValue").getElement().getPropertyString("value");
		String f_distinctCode = RootPanel.get("f_distinctCode").getElement().getPropertyString("value");
		String f_distinctValue = RootPanel.get("f_distinctValue").getElement().getPropertyString("value");
		String f_geoId = RootPanel.get("f_geoId").getElement().getPropertyString("value");
		String f_geoName = RootPanel.get("f_geoName").getElement().getPropertyString("value");
		String f_hotelGroupId = RootPanel.get("f_hotelGroupId").getElement().getPropertyString("value");
		String f_hotelGroupName = RootPanel.get("f_hotelGroupName").getElement().getPropertyString("value");
		setValueAndViewHiddenPanel(selectedConditonPanel[1],f_hotelGroupId,f_hotelGroupName);
		setValueAndViewHiddenPanel(selectedConditonPanel[4],f_bizCode,f_bizValue);
		setValueAndViewHiddenPanel(selectedConditonPanel[5],f_distinctCode,f_distinctValue);
		setValueAndViewHiddenPanel(selectedConditonPanel[6],f_geoId,f_geoName);
	}
	
	private void addSelectedConditonPanel(String containerId) {
		int len = selectedConditonPanel.length;
		for (int i = 0; i < len; i++) {
			selectedConditonPanel[i] = new ShowSelectedItemPanel();
			selectedConditonPanel[i].setPanelOnContainer(containerId);
		}

	}
	
	private RadioButton createRadioBtn(String radioBtnName, String value,
			String key, String style) {
		RadioButton radioButton = new RadioButton(radioBtnName, value);
		radioButton.setFormValue(key);
		radioButton.addStyleName(style);
		return radioButton;
	}
	
	//酒店价格
	private void addHotelPriceRadioBtns(){
		int len = HotelPrice.values().length;
		RadioButton []hotelprice = new RadioButton[len];
		for(int i = 0 ; i < len;i++){
			hotelprice[i] = createRadioBtn("hotelprice", HotelPrice.values()[i].getValue(),HotelPrice.values()[i].getKey(),"iradio");
		}
		hotelPriceRadioBtnPanel = new RadioButtonGroupPanel(hotelprice);
		hotelPriceRadioBtnPanel.addRadioBtns();
		hotelPriceRadioBtnPanel.setClickHandler(selectedConditonPanel[0]);
		String f_priceStr = RootPanel.get("f_priceStr").getElement().getPropertyString("value");
		if(null != f_priceStr && !"".equals(f_priceStr)){
			String priceHiddenText = "";
			for(int i = 0 ; i < len;i++){
				if(HotelPrice.values()[i].getKey().equals(f_priceStr)){
				   priceHiddenText += HotelPrice.values()[i].getValue();
				   break;
				}
			}
			selectedConditonPanel[0].setValueAndViewHiddenPanel(f_priceStr, priceHiddenText);
		}
		hotelPriceRadioBtnPanel.setSelectedHotelCondition(f_priceStr);
		RootPanel.get("hotel_search_hotelprice").add(hotelPriceRadioBtnPanel);
	}
	
	@SuppressWarnings("deprecation")
	private void addDefinePriceLabel(){
		final Anchor definePrice = new Anchor("自定义价格");
		RootPanel.get("hotel_search_hotelprice").add(definePrice);
		definePrice.addClickHandler(new ClickHandler(){
			public void onClick(final ClickEvent event) {
				definePricePanel = new DefinePricePanel("-");
				definePricePanel.createPanel();
				definePricePanel.setPopupPosition(definePrice.getElement().getAbsoluteLeft()-50, definePrice.getElement().getAbsoluteTop()+20);
				definePricePanel.show();
				definePricePanel.setClickHandler(selectedConditonPanel[0],hotelPriceRadioBtnPanel);
			}		
		});
	}
	
	//酒店集团(页面显示为酒店品牌)
	private void addHotelGroupRadioBtns(){
		RadioButton []hotelBrand = new RadioButton[brandSize];
		for(int i = 0 ; i < brandSize;i++){
			hotelBrand[i] = createRadioBtn("hotelbrand", HotelBrand.values()[i].getValue(),HotelBrand.values()[i].getKey(),"iradio");
		}	
		hotelBrandRadioBtnPanel = new RadioButtonGroupPanel(hotelBrand);
		hotelBrandRadioBtnPanel.addRadioBtns();
		hotelBrandRadioBtnPanel.setClickHandler(selectedConditonPanel[1]);
//		hotelBrandRadioBtnPanel.setSelectedHotelCondition("洲际");
		String f_hotelGroupId = RootPanel.get("f_hotelGroupId").getElement().getPropertyString("value");
		hotelBrandRadioBtnPanel.setSelectedHotelCondition(f_hotelGroupId);
		RootPanel.get("hotel_search_hotelband").add(hotelBrandRadioBtnPanel);
	}
	
	//添加更多酒店集团(品牌)
	@SuppressWarnings("deprecation")
	private void addMoreHotelGroup(){
		final Anchor moreHotelBand = new Anchor("更多品牌");
		moreHotelBand.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {	
				int size = HotelBrand.values().length;
				int len = size-brandSize;
				Anchor[] anchor = new Anchor[len];
				int j = 0;
				for(int i = brandSize;i < size;i++){
					j = i - brandSize;
					anchor[j] = new Anchor(HotelBrand.values()[i].getValue());
					anchor[j].setName(HotelBrand.values()[i].getKey());
					
				}
				moreHotelBandPanel = new FloatingPopupPanel(anchor);
				moreHotelBandPanel.addAnchors();
				moreHotelBandPanel.setClickHandler(selectedConditonPanel[1],hotelBrandRadioBtnPanel);
				moreHotelBandPanel.setAnchorPropertyName("name");
				moreHotelBandPanel.setPopupPosition(moreHotelBand.getElement().getAbsoluteLeft()-50, moreHotelBand.getElement().getAbsoluteTop()+20);
				moreHotelBandPanel.show();
			}	
		});
		RootPanel.get("hotel_search_hotelband").add(moreHotelBand);
	}
	
	//酒店星级
	private void addHotelStarCheckBoxes(){
		int len = HotelStar.values().length;
		CheckBox []hotelStar = new CheckBox[len];
		for(int i = 0 ; i < len;i++){
			hotelStar[i] = creatCheckBox("hotelStar",HotelStar.values()[i].getValue(),HotelStar.values()[i].getKey(),"iradio");
		}
		hotelStarCBoxGroupPanel =  new CheckBoxGroupPanel(hotelStar);
		hotelStarCBoxGroupPanel.addCheckBoxes();
		String f_hotelStar = RootPanel.get("f_hotelStar").getElement().getPropertyString("value");
		if(null != f_hotelStar &&!"".equals(f_hotelStar)){
			String hiddenText = "";
			String []items = f_hotelStar.split("#");
			for(String item : items){
				for(int i = 0 ; i < len;i++){//组装选择的酒店星级文本
					if(HotelStar.values()[i].getKey().equals(item)){
						hiddenText += HotelStar.values()[i].getValue();
						break;
					}
				}
			}
			selectedConditonPanel[2].setValueAndViewHiddenPanel(f_hotelStar.replace("#", ""), hiddenText);
		}else{
			f_hotelStar = "";
		}
		hotelStarCBoxGroupPanel.setSelectedHotelCondition(f_hotelStar);
		hotelStarCBoxGroupPanel.setClickHandler(selectedConditonPanel[2]);
		RootPanel.get("hotel_search_hotelstar").add(hotelStarCBoxGroupPanel);
	}
	
	private CheckBox creatCheckBox(String name,String value,String key, String style) {
		CheckBox checkBox = new CheckBox(value);
		checkBox.setName(name);
		checkBox.setFormValue(key);
		checkBox.addStyleName(style);
		return checkBox;
	}
	
	//添加酒店设施复选按钮
	private void addHotelFacilityCheckBoxes(){
		int len = HotelFacility.values().length;
		CheckBox []hotelFacility = new CheckBox[len];
		for(int i = 0 ; i < len;i++){
			hotelFacility[i] = creatCheckBox("hotelFacility",HotelFacility.values()[i].getValue(),HotelFacility.values()[i].getKey(),"iradio");
		}
		hotelFacilityCBoxGroupPanel = new CheckBoxGroupPanel(hotelFacility);
		hotelFacilityCBoxGroupPanel.addCheckBoxes();
//		hotelFacilityCBoxGroupPanel.setSelectedHotelCondition("6,#1,");
		hotelFacilityCBoxGroupPanel.setClickHandler(selectedConditonPanel[3]);
		RootPanel.get("hotel_search_hotelfacility").add(hotelFacilityCBoxGroupPanel);
	}
	
	//添加地理位置上的标签
	private void addHotelLocation(){
		HorizontalPanel hPanel = new HorizontalPanel();
		//hPanel.setSpacing(10);
		final Anchor commercialDistrict = new Anchor("商业区");
		final Anchor administrativeDistrict = new Anchor("行政区");
		final Anchor transportHub = new Anchor("交通枢纽");
		//final Anchor  universityHospitalAnchor = new Anchor("大学医院");
	//	final Anchor  metroAroundAnchor = new Anchor("地铁周边");
//		String cityCode = DOM.getElementById("cityCode").getPropertyString("value");
//		HTML universityHospital = new HTML("<a href='hotel-position.shtml?cityCode="+cityCode+"&type=26' target='_blank'>大学医院</a>");
//		HTML metroAround  = new HTML("<a href='hotel-position.shtml?cityCode=+="+cityCode+"&type=27'  target='_blank'>地铁周边</a>");
		hPanel.add(commercialDistrict);
		hPanel.add(administrativeDistrict);
		hPanel.add(transportHub);
	//	hPanel.add(universityHospitalAnchor);
	//	hPanel.add(metroAroundAnchor);
		RootPanel.get("hotel_search_location").add(hPanel);
		
//		universityHospitalAnchor.addClickHandler(new ClickHandler(){
//			public void onClick(final ClickEvent event) {
//				String cityCode = DOM.getElementById("cityCode").getPropertyString("value");
//				universityHospitalAnchor.setHref("hotel-position.shtml?cityCode="+cityCode+"&type=26");
//				universityHospitalAnchor.setTarget("_blank");
//			}
//		});
//		
//		universityHospitalAnchor.addClickHandler(new ClickHandler(){
//			public void onClick(final ClickEvent event) {
//				String cityCode = DOM.getElementById("cityCode").getPropertyString("value");
//				metroAroundAnchor.setHref("hotel-position.shtml?cityCode="+cityCode+"&type=27");
//				metroAroundAnchor.setTarget("_blank");
//			}
//		});
		
		
		commercialDistrict.addClickHandler(new ClickHandler(){
			public void onClick(final ClickEvent event) {
				//弹出商业区窗口
				String cityName = DOM.getElementById("id_startCity").getPropertyString("value");
				hotelListSearchService.getHotelBizDistrictByCityName(cityName,"business",new AsyncCallback<Map<String,String>>(){
					public void onFailure(Throwable caught) {
						System.out.println(caught.getMessage());
						caught.printStackTrace();
					}
					public void onSuccess(Map<String,String> map) {
						hotelCommercialDistrictPanel = createAndShowPanel(map,commercialDistrict.getAbsoluteLeft()+10,
								commercialDistrict.getAbsoluteTop()+10);
						hotelCommercialDistrictPanel.setClickHandler(selectedConditonPanel[4]);
					}
				});
			}
		});
		
		administrativeDistrict.addClickHandler(new ClickHandler(){
			public void onClick(final ClickEvent event) {
				//弹出行政区窗口
				String cityName = DOM.getElementById("id_startCity").getPropertyString("value");
				hotelListSearchService.getHotelBizDistrictByCityName(cityName,"district", new AsyncCallback<Map<String,String>>(){
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
						System.out.println(caught.getMessage());
					}
					public void onSuccess(Map<String,String> map) {
						administrativeDistictPanel = createAndShowPanel(map,administrativeDistrict.getAbsoluteLeft()+10,
								administrativeDistrict.getAbsoluteTop()+10);
						administrativeDistictPanel.setClickHandler(selectedConditonPanel[5]);
					}
				});
				
			}
		});
		
		transportHub.addClickHandler(new ClickHandler() {
			public void onClick(final ClickEvent event) {
				String cityName = DOM.getElementById("id_startCity").getPropertyString("value");
				hotelListSearchService.getHotelTransportHub(cityName,
						new AsyncCallback<Map<String, String>>() {
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}
							public void onSuccess(Map<String, String> map) {
								transportHubPanel = createAndShowPanel(map,transportHub.getAbsoluteLeft()+10,
										transportHub.getAbsoluteTop()+10);
								transportHubPanel.setClickHandler(selectedConditonPanel[6],null);
							}
						});
			}
		});
	}
	
	/**
	 * 显示弹出浮动层.
	 * @param map 浮动层中的数据
	 * @param left  显示位置横坐标
	 * @param top  显示位置纵坐标
	 * @return   FloatingPopupPanel 返回浮动层面板  
	 */
	private FloatingPopupPanel createAndShowPanel(Map<String, String> map,int left,int top) {
		int size = map.size();
		FloatingPopupPanel popupPanel = null;
		if (size > 0) {
			Anchor[] anchor = new Anchor[size];
			Iterator<Entry<String, String>> it = map.entrySet().iterator();
			int i = 0;
			while (it.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
				anchor[i] = new Anchor(entry.getValue());
				anchor[i].setName(entry.getKey());
				i++;
			}
			popupPanel = new FloatingPopupPanel(anchor);
			popupPanel.addAnchors();
			popupPanel.setPopupPosition(left, top);
			popupPanel.show();
		}
		return popupPanel;
	}
}