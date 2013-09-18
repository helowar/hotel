package com.mangocity.client.hotel.panel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * @author xieyanhui
 *
 */
public class CheckBoxGroupPanel extends FlowPanel implements IHotelConditionPanel{
	
	private String selectedCheckBoxValues = "";
	private String selectedCheckBoxTexts = "";
	private final static int norestriction = 0;//不限
	private CheckBox[] checkBoxes;
	
	public CheckBoxGroupPanel(CheckBox[] checkBox) {
		super();
		this.checkBoxes = checkBox;
	}
	
	public void addCheckBoxes(){
		if(checkBoxes != null){
			int length = checkBoxes.length;
			if(length > 0){
				for(int i = 0 ;i < length; i++){
					this.add(checkBoxes[i]);
				}
			}
		}
	}
	
	public void setClickHandler(final ShowSelectedItemPanel showSelectedItemPanel){
		int length = checkBoxes.length;
		for(int i = 0 ;i < length; i++){
			setClickHandlerForCheckBox(checkBoxes[i],showSelectedItemPanel);
		}
	}
	
	private void setClickHandlerForCheckBox(final CheckBox checkBox,final ShowSelectedItemPanel showSelectedItemPanel){
		checkBox.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if(checkBox.getValue()){
					//如果不限选中，则其它的不能选中，值为空
					//如果其它的选中，则不限就不能选中，各复选框列表值相加即可
					int length = checkBoxes.length;
					if(checkBoxes[norestriction].equals(checkBox)){
						for(int i = 0;i < length;i++){
							if(i != norestriction){
								checkBoxes[i].setValue(false);
							}
						}
						selectedCheckBoxValues ="";
						selectedCheckBoxTexts = "";
					}else{
						checkBoxes[norestriction].setValue(false);
						selectedCheckBoxValues += checkBox.getFormValue();
						selectedCheckBoxTexts += checkBox.getText()+" ";
					}
				}else{
					selectedCheckBoxValues = selectedCheckBoxValues.replace(checkBox.getFormValue(), "");
					selectedCheckBoxTexts = selectedCheckBoxTexts.replace(checkBox.getText()+" ", "");
				}
				showSelectedItemPanel.setValueAndViewHiddenPanel(selectedCheckBoxValues, selectedCheckBoxTexts);
				if("".equals(selectedCheckBoxTexts)){
					showSelectedItemPanel.setVisible(false);
					showSelectedItemPanel.setHiddenValue("");
				}
			}	
		});
	}
	
	public void clearSelectedHotelCondition() {
		int length = checkBoxes.length;
		for(int i = 0 ;i < length; i++){
			checkBoxes[i].setValue(false);
		}
		selectedCheckBoxTexts = "";
		selectedCheckBoxValues = "";
	}
	
	public void setSelectedHotelCondition(String condtion) {
		if (!"".equals(condtion) && null != condtion) {
			String[] items = condtion.split("#");
			int size = items.length;
			int length = checkBoxes.length;
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < length; j++) {
					if (checkBoxes[j].getFormValue().equals(items[i])) {
						checkBoxes[j].setValue(true);
						selectedCheckBoxValues += checkBoxes[j].getFormValue();
						selectedCheckBoxTexts += checkBoxes[j].getText()+" ";
						break;
					}
				}
			}
		}
	}
}
