package com.mangocity.client.hotel.panel;

/**
 * @author xieyanhui
 *
 */
public interface IHotelConditionPanel {
	
    /**
     * 清空酒店过滤条件中所有选中的条件
     */
	public void clearSelectedHotelCondition();
	
    /**
     * 设置酒店过滤条件
     */
	public void setSelectedHotelCondition(String condtion);
}
