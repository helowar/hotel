package com.mangocity.client.hotel.panel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * @author xieyanhui
 *
 */
public class FloatingPopupPanel extends DecoratedPopupPanel{
	
	private String selectedAnchorValue = "";
	private String anchorPropertyName = "name";
	private Anchor[] anchor;
	private Anchor closeAnchor;
	
	public FloatingPopupPanel(Anchor[] anchor) {
		super(true);
		this.anchor = anchor;
	}
	
	public void addAnchors(){
		int widgetsizeOfRow = 3;
		if(anchor != null){
			FlowPanel anchorContainer = new FlowPanel();
			anchorContainer.setStyleName("shangquan");
			FlexTable flexTable = new FlexTable();
			int length = anchor.length;
			if(length > 0){
				closeAnchor = new Anchor("[关闭]");
				closeAnchor.setStyleName("css_closeSelectItem");
				anchorContainer.add(closeAnchor);
				for(int i = 0 ;i < length; i++){
					flexTable.setWidget(i / widgetsizeOfRow, i % widgetsizeOfRow, anchor[i]);
				}
				anchorContainer.add(flexTable);
				this.add(anchorContainer);
			}
		}
	}
	
	public void setClickHandler(ShowSelectedItemPanel showSelectedItemPanel,IHotelConditionPanel hotelConditionPanel){
		int length = anchor.length;
		for(int i = 0 ;i < length; i++){
			setClickHandlerForAnchor(anchor[i],showSelectedItemPanel,hotelConditionPanel);
		}
		setCloseButtonHandler();
		
	}
	
	private void setCloseButtonHandler(){
		closeAnchor.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				hide();
			}
		});
	}
	
	public void setClickHandler(ShowSelectedItemPanel showSelectedItemPanel){
		int length = anchor.length;
		for(int i = 0 ;i < length; i++){
			setClickHandlerForAnchor(anchor[i],showSelectedItemPanel);
		}
		setCloseButtonHandler();
	}
	
	private void setClickHandlerForAnchor(final Anchor anchor,final ShowSelectedItemPanel showSelectedItemPanel,final IHotelConditionPanel hotelConditionPanel){
		anchor.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					if(hotelConditionPanel != null){
						hotelConditionPanel.clearSelectedHotelCondition();
					}
					setAnchorEvent(anchor, showSelectedItemPanel);
				}
			});
	}
	
	private void setClickHandlerForAnchor(final Anchor anchor,final ShowSelectedItemPanel showSelectedItemPanel){
		anchor.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					setAnchorEvent(anchor, showSelectedItemPanel);
				}
			});
	}
	
	private void setAnchorEvent(final Anchor anchor,final ShowSelectedItemPanel showSelectedItemPanel){
		selectedAnchorValue = anchor.getElement().getPropertyString(anchorPropertyName);
		showSelectedItemPanel.setValueAndViewHiddenPanel(selectedAnchorValue, anchor.getText());
		if("".equals(selectedAnchorValue)){
			showSelectedItemPanel.setVisible(false);
		}
		hide();
	}

	public String getAnchorPropertyName() {
		return anchorPropertyName;
	}

	public void setAnchorPropertyName(String anchorPropertyName) {
		this.anchorPropertyName = anchorPropertyName;
	}

}
