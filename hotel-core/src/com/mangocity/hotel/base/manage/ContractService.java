package com.mangocity.hotel.base.manage;

import java.util.List;

import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlInternet;
import com.mangocity.hotel.base.persistence.HtlTaxCharge;
import com.mangocity.hotel.user.UserWrapper;


/**
 * 合同的管理，主要是为酒店本部提供的服务
 * @author xiaowumi
 *
 */
public interface ContractService{
	/**
	 * 判断当前合同是否已经锁定
	 */
	public String checkHasLocked(HtlContract  htlContract,UserWrapper user,HtlHotel hotel);
	
	/**
	 * 解锁
	 * @param recordCD
	 * @param lockedType
	 * @param lockedName
	 * @return
	 */
	public int deleteLockedRecordTwo(String recordCD, Integer lockedType, String lockedName);
	
	/**
	 * 修改合同税费、宽带、创建人、修改人等信息
	 * @param user
	 * @param contract
	 * @param lisTaxCharge
	 * @param lisInternet
	 */
	public HtlContract modifyTaxAndInterNetInfo(long hotelId,UserWrapper user,HtlContract contract,List<HtlTaxCharge> lisTaxCharge ,List<HtlInternet> lisInternet);
}

