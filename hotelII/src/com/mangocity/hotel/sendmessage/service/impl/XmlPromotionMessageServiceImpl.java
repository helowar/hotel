package com.mangocity.hotel.sendmessage.service.impl;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.mangocity.hotel.sendmessage.model.PromotionMessage;
import com.mangocity.hotel.sendmessage.service.PromotionMessageService;
import com.mangocity.util.DateUtil;

public class XmlPromotionMessageServiceImpl implements PromotionMessageService {

	private String url;
	public PromotionMessage getPromotionMessage() throws Exception {
		PromotionMessage promotionMessage=new PromotionMessage();

		SAXReader saxReader = new SAXReader();
		String url=this.getUrl();
		Document  document= saxReader.read(url);  //读取XML文件,获得document对象
		Element rootElm = document.getRootElement(); 
        String beginDate=rootElm.element("beginDate").getText();
        String endDate=rootElm.element("endDate").getText();
        String status=rootElm.element("status").getText();
        String content=rootElm.element("content").getText();
        String funtionCode=rootElm.element("funtionCode").getText();
        String funtionRemark=rootElm.element("funtionRemark").getText();
        promotionMessage.setBeginDate(DateUtil.stringToDateMain(beginDate, "yyyy-MM-dd"));
        promotionMessage.setEndDate(DateUtil.stringToDateMain(endDate, "yyyy-MM-dd"));
        
        int statusInt=(status==null?0:Integer.parseInt(status));
        promotionMessage.setStatus(statusInt);
        promotionMessage.setContent(content);
        promotionMessage.setFuntionCode(funtionCode);
        promotionMessage.setFuntionRemark(funtionRemark);
		return promotionMessage;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
	
}
