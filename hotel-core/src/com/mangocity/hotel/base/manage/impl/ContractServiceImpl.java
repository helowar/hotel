package com.mangocity.hotel.base.manage.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.manage.ContractService;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlInternet;
import com.mangocity.hotel.base.persistence.HtlTaxCharge;
import com.mangocity.hotel.base.persistence.OrLockedRecords;
import com.mangocity.hotel.base.service.ILockedRecordService;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;

/**
 * 合同的管理，主要是为酒店本部提供的服务
 * @author xiaowumi
 *
 */

public class ContractServiceImpl implements ContractService {
	
	// 酒店，合同，房态，配额等加解锁操作接口 
	private ILockedRecordService lockedRecordService;
	
	public ILockedRecordService getLockedRecordService() {
		return lockedRecordService;
	}

	public void setLockedRecordService(ILockedRecordService lockedRecordService) {
		this.lockedRecordService = lockedRecordService;
	}

	public String checkHasLocked(HtlContract  htlContract,UserWrapper user,HtlHotel tempHotel){
		
		if (user == null) {
			return null;
		}
		
		OrLockedRecords orLockedRecord = new OrLockedRecords();
		orLockedRecord.setRecordCD(String.valueOf(htlContract.getHotelId()));
		orLockedRecord.setLockType(03);
		OrLockedRecords lockedRecords = lockedRecordService.loadLockedRecord(orLockedRecord);
		
		String lockedMSG = null;
		// 已锁
		if (null != lockedRecords){ 
			String lockerName = lockedRecords.getLockerName();
			String lockerLoginName = lockedRecords.getLockerLoginName();
			if(!user.getLoginName().equals(lockerLoginName)){ // 不是锁定人进入
				lockedMSG = "此酒店合同已被锁定，在被解锁之前，只有锁定人才能进入（锁定人：" + lockerName + "["+lockerLoginName+"]）";
			}
			return lockedMSG;
		}
		
		// 未锁，加入锁的记录
		orLockedRecord.setRemark(tempHotel.getChnName());
		orLockedRecord.setLockerName(user.getName());
		orLockedRecord.setLockerLoginName(user.getLoginName());
		orLockedRecord.setLockTime(DateUtil.getSystemDate());
		lockedRecordService.insertLockedRecord(orLockedRecord);
		
		return lockedMSG;		
	}
	
	public int deleteLockedRecordTwo(String recordCD, Integer lockedType, String lockedName){
		return lockedRecordService.deleteLockedRecordTwo(recordCD,lockedType,lockedName);
	}
	
	public HtlContract modifyTaxAndInterNetInfo(long hotelId,UserWrapper user,HtlContract contract,
			List<HtlTaxCharge> lisTaxCharge ,List<HtlInternet> lisInternet){
		/*
		 * 修改原因：在页面上，对于“宽带收费”和“税费设定”两项，表格中第一条记录有些字段存在默认值，
		 * 当执行上面三行代码时，将分别产生一条多余的记录，而且该记录将写入数据库中。因此，在下面的
		 * 代码中对该条多余的记录进行了过滤，至于判断条件，暂定为：记录的开始时间不能为空。 
		 * Modified By :  WU YUN
		 */
		Date currentDate = DateUtil.getSystemDate();
		List<HtlTaxCharge> taxList = new ArrayList<HtlTaxCharge>(lisTaxCharge.size());		
		for ( HtlTaxCharge tc : lisTaxCharge){
			if(tc.getTaxBeginDate() == null){
				continue;
			}
			
			tc.setHotelId(hotelId);
			if (user!=null){
				 if(tc.getID()==null || tc.getID()==0){
					 tc.setCreateBy(user.getName());
					 tc.setCreateById(user.getLoginName());
					 tc.setCreateTime(currentDate);
		         }
				tc.setModifyBy(user.getName());
				tc.setModifyById(user.getLoginName());
			}				
			tc.setModifyTime(currentDate);
			taxList.add(tc);			
		}
		
		List<HtlInternet> internetList = new ArrayList<HtlInternet>(lisInternet.size());
		for ( HtlInternet itn : lisInternet){
			if(itn.getInternetBeginDate() == null){
				continue;
			}
			
			itn.setHtlContract(contract);
			itn.setHotelId(hotelId);
			if (user!=null){
				 if(itn.getID()==null || itn.getID()==0){
					 itn.setCreateBy(user.getName());
					 itn.setCreateById(user.getLoginName());
					 itn.setCreateTime(currentDate);
		         }
				itn.setModifyBy(user.getName());
				itn.setModifyById(user.getLoginName());
			}
			itn.setModifyTime(currentDate);
			internetList.add(itn);			
		}
      
		contract.setHtlTaxCharge(taxList);
		contract.setHtlInternet(internetList);
		contract.setHotelId(hotelId);
		
		if (user!=null){
            if(contract.getID()==null || contract.getID()==0){
            	contract.setCreateBy(user.getName());
            	contract.setCreateById(user.getLoginName());        
            }
		    contract.setModifyBy(user.getName());
		    contract.setModifyById(user.getLoginName());
		    contract.setModifyTime(currentDate);
		}
		return contract;
		
	}
	
}

