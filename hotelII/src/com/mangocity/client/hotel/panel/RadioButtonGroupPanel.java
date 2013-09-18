package com.mangocity.client.hotel.panel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;

/**
 * @author xieyanhui
 *
 */
public class RadioButtonGroupPanel extends FlowPanel implements IHotelConditionPanel{
	
	private String selectedRadioBtnValue = "";
	private RadioButton[] radioBtns;
	
	public RadioButtonGroupPanel(RadioButton[] radioBtn) {
		super();
		this.radioBtns = radioBtn;
	}
	
	public void addRadioBtns(){
		if(radioBtns != null){
			int length = radioBtns.length;
			if(length > 0){
				for(int i = 0 ;i < radioBtns.length; i++){
					this.add(radioBtns[i]);
				}
			}
		}
	}
	
	public void setClickHandler(ShowSelectedItemPanel showSelectedItemPanel){
		for(int i = 0 ;i < radioBtns.length; i++){
			setClickHandlerForRadioBtn(radioBtns[i],showSelectedItemPanel);
		}
	}
	
	private void setClickHandlerForRadioBtn(final RadioButton radioBtn,final ShowSelectedItemPanel showSelectedItemPanel){
		radioBtn.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					selectedRadioBtnValue = radioBtn.getFormValue();
					showSelectedItemPanel.setValueAndViewHiddenPanel(selectedRadioBtnValue, radioBtn.getText());
					if("".equals(selectedRadioBtnValue)){
						showSelectedItemPanel.setVisible(false);
					}
				}
			});
	}

	public void clearSelectedHotelCondition() {
		int length = radioBtns.length;
		if(length > 0){
			for(int i = 0 ;i < radioBtns.length; i++){
				radioBtns[i].setValue(false);
			}
		}
	}
	
	public void setSelectedHotelCondition(String condtion) {
		if(!"".equals(condtion) && null != condtion){
			int length = radioBtns.length;
			if(length > 0){
				for(int i = 0 ;i < length; i++){
					if(radioBtns[i].getFormValue().equals(condtion)){
						radioBtns[i].setValue(true);
						break;
					}
				}
			}
		}
	}
}
