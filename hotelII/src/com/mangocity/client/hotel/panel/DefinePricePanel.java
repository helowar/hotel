package com.mangocity.client.hotel.panel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;

/**
 * @author xieyanhui
 *
 */
public class DefinePricePanel extends DecoratedPopupPanel{
	
	private Anchor okAnchor;
	private Anchor closeAnchor;
	private String definePrice = "";
	private IntegerBox minPrice;
	private IntegerBox maxPrice;
	private String splitChar;
	
	
	public DefinePricePanel(final String splitChar) {
		super(true);
		this.splitChar = splitChar;
	}
	
	public void createPanel(){
		okAnchor = new Anchor("确定");
		closeAnchor = new Anchor("[关闭]");
		minPrice = new IntegerBox();
		maxPrice = new IntegerBox();
		FlowPanel fowPanel = new FlowPanel();
		fowPanel.setStyleName("priceBox");
		fowPanel.add(closeAnchor);
		FlexTable flexTable = new FlexTable();
		flexTable.setWidget(0, 0, minPrice);
		flexTable.setText(0, 1, "到");
		flexTable.setWidget(0, 2, maxPrice);
		flexTable.setWidget(1, 0, okAnchor);
		fowPanel.add(flexTable);
		this.add(fowPanel);
	}
	
	public void setClickHandler(final ShowSelectedItemPanel showSelectedItemPanel,final RadioButtonGroupPanel hotelPriceRadioBtnPanel){
		// 关闭
		closeAnchor.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				DefinePricePanel.this.hide();
			}
		});
		
		// 确定
		okAnchor.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(minPrice.getValue() != null && maxPrice.getValue() != null){
					int min = minPrice.getValue();
					int max = maxPrice.getValue();
					if(min > max || min <0 || max <0){
						Window.alert("输入不对，请重新输入!");
					}else{
						if(hotelPriceRadioBtnPanel != null){//清空酒店价格
							hotelPriceRadioBtnPanel.clearSelectedHotelCondition();
						}
						definePrice = min+splitChar+max;
						DefinePricePanel.this.hide();
						showSelectedItemPanel.setValueAndViewHiddenPanel(definePrice, min+splitChar+max+"元");
					}
				}else{
					Window.alert("输入不对，请输入数字!");
				}
			}
		});
	}
}
