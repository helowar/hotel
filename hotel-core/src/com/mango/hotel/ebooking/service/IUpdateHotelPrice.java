package com.mango.hotel.ebooking.service;

import java.io.Serializable;
import java.util.List;

import com.mango.hotel.ebooking.persistence.HtlEbookingPriceRedressal;
/**
 * 更新酒店价格的接口，主要用于本部与ＥＰ之间
 * @author chenjuesu
 *
 */
public interface IUpdateHotelPrice extends Serializable {
	/**
	 * 单个更新价格
	 * @param priceRedressalBean
	 * @param infos 数组：infos[0]='loginName',infos[1]='name'
	 * @return
	 */
	public String batchUpdatePrice(HtlEbookingPriceRedressal priceRedressalBean,String[] infos);
	
	/**
	 * 批量更新价格
	 * @param priceRedressalBean
	 * @param infos 数组：infos[0]='loginName',infos[1]='name'
	 * @return
	 */
	public String batchUpdatePriceList(List<HtlEbookingPriceRedressal> priceRedressalBean,String[] infos);
}
