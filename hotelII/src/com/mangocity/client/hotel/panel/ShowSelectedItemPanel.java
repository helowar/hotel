package com.mangocity.client.hotel.panel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class ShowSelectedItemPanel extends FlowPanel{
	
	private String hiddenValue = "";
	private String hiddenText = "";
	private HTML textPanel;
	private HTMLPanel htmlPanel;
	
	public void setPanelOnContainer(String parentContainerId){
		this.setVisible(false);
		this.setStyleName("cdChosen");
		textPanel = new HTML(hiddenText);
		textPanel.setStyleName("ChosenNote");
		htmlPanel = new HTMLPanel("span","");
		htmlPanel.setStyleName("close");
		this.add(textPanel);
		this.add(htmlPanel);
		RootPanel.get(parentContainerId).add(this);
	}
	
	public void setClickHandler(final IHotelConditionPanel panel){
		htmlPanel.addDomHandler(new ClickHandler(){
			public void onClick(final ClickEvent event) {
				hiddenValue = "";
				hiddenText = "";
				textPanel.setText("");
				setVisible(false);
				if(panel != null){
					panel.clearSelectedHotelCondition();
				}
			}
		},ClickEvent.getType());
	}
	
	public void setValueAndViewHiddenPanel(String hiddenValue,String hiddenText){
		this.setHiddenText(hiddenText);
		textPanel.setText(hiddenText);
		this.setHiddenValue(hiddenValue);
		this.setVisible(true);
	}

	public String getHiddenValue() {
		return hiddenValue;
	}

	public void setHiddenValue(String hiddenValue) {
		this.hiddenValue = hiddenValue;
	}

	public void setHiddenText(String hiddenText) {
		this.hiddenText = hiddenText;
	}

	public String getHiddenText() {
		return hiddenText;
	}
}
