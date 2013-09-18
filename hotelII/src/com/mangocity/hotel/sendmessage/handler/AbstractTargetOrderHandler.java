package com.mangocity.hotel.sendmessage.handler;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.sendmessage.model.TargetOrder;

public abstract class AbstractTargetOrderHandler {
	public abstract <T> T handleTargetOrderValue(Object[] targetOrder);
	
	public <T extends TargetOrder> List<T>  handleTemplete(List<Object[]> targetOrderList){
		List<T> productOrderList=new ArrayList<T>();
		if(targetOrderList!=null && targetOrderList.size()>0){
			for(Object[] targetOrder:targetOrderList){
				productOrderList.add(this.<T>handleTargetOrderValue(targetOrder));
			}
		}
	
		return productOrderList;
		
	};
	
}
