package com.mangocity.fantiweb.action;

import com.opensymphony.xwork2.ActionSupport;

public class HotelListMapPrintAction  extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4510967292209814960L;
	private String content;
	public String execute(){
		content = content.replaceAll("(?s)<SCRIPT\\s*.*?>(.*?)</SCRIPT>","");
		return SUCCESS;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

}
