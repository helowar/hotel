package com.mangocity.hotel.base.manage.assistant;

import java.util.Iterator;
import java.util.List;

import org.dom4j.DocumentHelper;

import com.mangocity.hotel.base.persistence.HtlTempRoomState;
import com.mangocity.util.DateUtil;

public class MsgAssist {

	public static String genMailContractorByRoomState(List fullList) {
		String strXML = "";
        org.dom4j.Document document = DocumentHelper.createDocument();
        org.dom4j.Element rootElement = document.addElement("roomState");
        for(Iterator i = fullList.iterator();i.hasNext();){
        	 HtlTempRoomState roomState=(HtlTempRoomState) i.next();
        	 org.dom4j.Element roomStateItem = rootElement.addElement("roomStateItem");
        	 org.dom4j.Element saleDate = roomStateItem.addElement("saleCode");
             org.dom4j.Element roomName = roomStateItem.addElement("roomName");
             org.dom4j.Element bedName = roomStateItem.addElement("bedName");
             org.dom4j.Element sumQuotaCount=roomStateItem.addElement("sumQuotaCount");
             org.dom4j.Element ableQuotaCount=roomStateItem.addElement("ableQuotaCount");
             saleDate.setText(DateUtil.dateToString(roomState.getSaleDate()));
             roomName.setText(roomState.getRoomName());
             String bed = "";
             if ("1".equals(roomState.getBedId())) {
                 bed = "大床";
             } else if ("2".equals(roomState.getBedId())) {
                 bed = "双床";
             } else if ("3".equals(roomState.getBedId())) {
                 bed = "单床";
             } else if ("0".equals(roomState.getBedId())) {
                 bed = "无需求";
             }
             bedName.setText(bed);
             sumQuotaCount.setText(String.valueOf(roomState.getQuatoCount()));
             ableQuotaCount.setText(String.valueOf(roomState.getAbleUseQuoatCount()));
        }
        strXML+=document.asXML();
		return strXML;
	}
}
