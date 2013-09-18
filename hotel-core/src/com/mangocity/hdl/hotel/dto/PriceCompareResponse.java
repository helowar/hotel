package com.mangocity.hdl.hotel.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "result",
    "message",
    "inventoryPriceResponses"
})
@XmlRootElement(name = "PriceCompareResponse")
public class PriceCompareResponse {
	/**
	 * 
	 */
	 protected int result = 1;
	 
	 protected String message;
	 
	 @XmlElement(nillable = true)
	 protected List<InventoryPriceResponse> inventoryPriceResponses;



	public List<InventoryPriceResponse> getInventoryPriceResponses() {
		return inventoryPriceResponses;
	}

	public void setInventoryPriceResponses(List<InventoryPriceResponse> inventoryPriceResponses) {
		this.inventoryPriceResponses = inventoryPriceResponses;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	 
	 
   
}
