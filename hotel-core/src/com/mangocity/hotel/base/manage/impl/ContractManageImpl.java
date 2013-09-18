package com.mangocity.hotel.base.manage.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

import com.mangocity.hotel.base.constant.FavourableTypeUtil;
import com.mangocity.hotel.base.dao.ContractFileDao;
import com.mangocity.hotel.base.dao.IContractDao;
import com.mangocity.hotel.base.dao.ISaleDao;
import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.persistence.ContractFile;
import com.mangocity.hotel.base.persistence.HtlAddBedPrice;
import com.mangocity.hotel.base.persistence.HtlAlerttypeInfo;
import com.mangocity.hotel.base.persistence.HtlAssureCardItem;
import com.mangocity.hotel.base.persistence.HtlAssureItem;
import com.mangocity.hotel.base.persistence.HtlBreakfast;
import com.mangocity.hotel.base.persistence.HtlChargeBreakfast;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlCreditAssure;
import com.mangocity.hotel.base.persistence.HtlCreditAssureDate;
import com.mangocity.hotel.base.persistence.HtlEveningsRent;
import com.mangocity.hotel.base.persistence.HtlFavouraParameter;
import com.mangocity.hotel.base.persistence.HtlFavourableclause;
import com.mangocity.hotel.base.persistence.HtlInternet;
import com.mangocity.hotel.base.persistence.HtlOpenCloseRoom;
import com.mangocity.hotel.base.persistence.HtlPresale;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlQuotabatch;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.HtlSalesPromo;
import com.mangocity.hotel.base.persistence.HtlWelcomePrice;
import com.mangocity.hotel.base.service.assistant.CutDate;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.ContinueDatecomponent;
import com.mangocity.util.bean.ContractContinue;
import com.mangocity.util.bean.DateComponent;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.bean.SingleContractContinue;
import com.mangocity.util.log.MyLog;

/**
 * 合同的管理，主要是为酒店本部提供的服务
 * @author xiaowumi
 *
 */

public class ContractManageImpl implements ContractManage {
	private static final MyLog log = MyLog.getLogger(ContractManageImpl.class);
	private ISaleDao saleDao;
	
	private ContractFileDao contractFileDao;

	/**
	 * 合同Dao接口
	 */
	private IContractDao  contractDao;
	
	/**
	 * 新建一个合同
	 * @param contract 没有id的合同对象
	 * @return 返回合同id
	 */
	public Long createContract(HtlContract contract){
		contractDao.saveOrUpdate(contract);
		return contract.getID();
	}
	
	/**
	 * 修改一个合同
	 * @param contract 有id的合同对象
	 * @return 返回一个整数，如果为1则表示修改成功，否则表示失败
	 */
	public int modifyContract(HtlContract contract){
		contractDao.merge(contract);
		return 1;
	}
	
	/**
	 * 通过一个id找回合同信息
	 * @param contract_id
	 * @return 合同信息
	 */
	public HtlContract loadContract(Long contract_id){
		return (HtlContract)contractDao.findHtlContractByID(HtlContract.class, contract_id);		
	}
	
	/**
	 * 创建一个信用卡担保信息
	 * @param creditAssure
	 * @return 记录的id
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public Long createCreditAssure(HtlCreditAssure creditAssure) throws IllegalAccessException, InvocationTargetException{

		List<HtlCreditAssure> oldCreditAssures = contractDao.getCreditAssures(creditAssure);
		if(oldCreditAssures.isEmpty()){
			contractDao.saveOrUpdate(creditAssure);
		}else{
            //如果数据库有超过1条的记录，则先判断时间段是否出现重叠再进行拆分
			if(oldCreditAssures.size()>1){
				//指明具体类型
				List<DateComponent> dateCopList = new ArrayList<DateComponent>();
				for(HtlCreditAssure arecord: oldCreditAssures){
					DateComponent dateCop = new DateComponent();
					dateCop.setId(arecord.getID());
					dateCop.setBeginDate(arecord.getBeginDate());
					dateCop.setEndDate(arecord.getEndDate());
					dateCop.setModifyDate(arecord.getModifyTime());
					dateCopList.add(dateCop);
				}
				if(CutDate.compareConflict(dateCopList)){//如果存在重叠，则要拆分
					for(HtlCreditAssure subCreditAssure: oldCreditAssures){
						List subList = contractDao.getSubCreditAssures(creditAssure.getContractId(),subCreditAssure.getRoomType(),subCreditAssure.getModifyTime());
						this.creditAssureUtil(subCreditAssure,subList);
					}
					oldCreditAssures = contractDao.getCreditAssures(creditAssure);
				}
			}
			///////////////////////////////////////////////////
			this.creditAssureUtil(creditAssure,oldCreditAssures);
		}
		return creditAssure.getID();
	}
	
    /**
     * 修改一个信用卡担保信息
     * 
     * @param creditAssure
     * @return 返回一个整数，如果为1则表示修改成功，否则表示失败
     */
	private void creditAssureUtil(HtlCreditAssure creditAssure,List oldCreditAssures) throws IllegalAccessException,
			InvocationTargetException {
		
		DateComponent dateComponent = new DateComponent();
		dateComponent.setBeginDate(creditAssure.getBeginDate());
		dateComponent.setEndDate(creditAssure.getEndDate());
		List<DateComponent> dateCops = new ArrayList<DateComponent>();
		Map resultMap = new HashMap(); 			
		for(int ii=0; ii<oldCreditAssures.size(); ii++){
			HtlCreditAssure assure = (HtlCreditAssure)oldCreditAssures.get(ii);
			DateComponent aComponent = new DateComponent();
			aComponent.setId(assure.getID());
			aComponent.setBeginDate(assure.getBeginDate());
			aComponent.setEndDate(assure.getEndDate());
			dateCops.add(aComponent);				
		}
		resultMap = CutDate.cut(dateComponent,CutDate.sort(dateCops));
		List removeList = (List) resultMap.get("remove");
		List updateList = (List) resultMap.get("update");
		List results = new ArrayList();
		for(int jj=0; jj<removeList.size(); jj++){
			DateComponent bb = (DateComponent)removeList.get(jj);
			contractDao.remove(HtlCreditAssure.class, bb.getId());
		}	

		 //根据拆分的时间段重新组装数据	
		boolean nullFlag = false;
		for(int i=0; i<oldCreditAssures.size(); i++){
			HtlCreditAssure aRecord = (HtlCreditAssure) oldCreditAssures.get(i);
			int doubleFlag = 0;
			for(int j=0; j<updateList.size(); j++){
				DateComponent dateCop = (DateComponent) updateList.get(j);				
				if(dateCop.getId()!=null){
					if(dateCop.getId().equals(aRecord.getID())){
						doubleFlag++;
						//如果存在多个相同的id则只有第一个id保留，其他的id都赋值为null
						if(doubleFlag>1){
							HtlCreditAssure newRecord = new HtlCreditAssure();
							BeanUtils.copyProperties(newRecord,aRecord);
							///
							///List:HtlAssureCardItem
							List templist = aRecord.getHtlAssureCardItem();
							if(templist!=null){
								List alis = MyBeanUtil.copyCollection(templist, HtlAssureCardItem.class);
								for(int mm=0; mm<alis.size(); mm++){
									HtlAssureCardItem aB= (HtlAssureCardItem)alis.get(mm);
									aB.setAssureCardItemID(0);
									aB.setHtlCreditAssure(newRecord);
								}
								newRecord.setHtlAssureCardItem(alis);
							}							
							///List:HtlAssureItem
							templist = aRecord.getHtlAssureItem();
							if(templist!=null){
								List alis = MyBeanUtil.copyCollection(templist, HtlAssureItem.class);
								for(int mm=0; mm<alis.size(); mm++){
									HtlAssureItem aB= (HtlAssureItem)alis.get(mm);
									aB.setAssureItemID(0);
									aB.setHtlCreditAssure(newRecord);
								}
								newRecord.setHtlAssureItem(alis);
							}
							///List:HtlCreditAssureDate
							templist = aRecord.getHtlCreditAssureDate();
							if(templist!=null){
								List alis = MyBeanUtil.copyCollection(templist, HtlCreditAssureDate.class);
								for(int mm=0; mm<alis.size(); mm++){
									HtlCreditAssureDate aB= (HtlCreditAssureDate)alis.get(mm);
									aB.setCreditAssureDateID(0);
									aB.setHtlCreditAssure(newRecord);
								}
								newRecord.setHtlCreditAssureDate(alis);
							}
							///
							newRecord.setID(null);
							newRecord.setBeginDate(dateCop.getBeginDate());
							newRecord.setEndDate(dateCop.getEndDate());							
							results.add(newRecord);
						}else{
							aRecord.setBeginDate(dateCop.getBeginDate());
							aRecord.setEndDate(dateCop.getEndDate());	
							results.add(aRecord);
						}
					}
				}else if(nullFlag==false){
					if(creditAssure.getID()!= null){	//处理修改情况					
						HtlCreditAssure record = new HtlCreditAssure();
						BeanUtils.copyProperties(record,creditAssure);
						
						///List:HtlAssureCardItem
						List templist = creditAssure.getHtlAssureCardItem();						
						if(templist!=null){		
							List alis = MyBeanUtil.copyCollection(templist, HtlAssureCardItem.class);
							for(int mm=0; mm<alis.size(); mm++){
								HtlAssureCardItem aB= (HtlAssureCardItem)alis.get(mm);								
								aB.setAssureCardItemID(0);
								aB.setHtlCreditAssure(record);
							}	
							record.setHtlAssureCardItem(alis);
						}
						///List:HtlAssureItem
						templist = creditAssure.getHtlAssureItem();
						if(templist!=null){
							List alis = MyBeanUtil.copyCollection(templist, HtlAssureItem.class);
							for(int mm=0; mm<alis.size(); mm++){
								HtlAssureItem aB= (HtlAssureItem)alis.get(mm);
								aB.setAssureItemID(0);
								aB.setHtlCreditAssure(record);
							}
							record.setHtlAssureItem(alis);
						}
						///List:HtlCreditAssureDate
						templist = creditAssure.getHtlCreditAssureDate();
						if(templist!=null){
							List alis = MyBeanUtil.copyCollection(templist, HtlCreditAssureDate.class);
							for(int mm=0; mm<alis.size(); mm++){
								HtlCreditAssureDate aB= (HtlCreditAssureDate)alis.get(mm);
								aB.setCreditAssureDateID(0);
								aB.setHtlCreditAssure(record);
							}
							record.setHtlCreditAssureDate(alis);
						}
						
						record.setID(null);
						results.add(record);
					}else{//处理新增情况
						results.add(creditAssure);
					}
					nullFlag = true;
				}
			}		
		}	
		contractDao.saveOrUpdateAll(results);
		
	}
	
	/**
	 * 修改一个信用卡担保信息
	 * @param creditAssure
	 * @return 返回一个整数，如果为1则表示修改成功，否则表示失败
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public int modifyCreditAssure(HtlCreditAssure creditAssure) throws IllegalAccessException, InvocationTargetException{
		if( null == creditAssure ){
			return 0;
		}
		if(creditAssure.getID()==null||creditAssure.getID()==0){
			this.createCreditAssure(creditAssure);
		}else{
			List<HtlCreditAssure> oldCreditAssures = contractDao.getCreditAssures(creditAssure);
			/////////////////////////////////////////////////////
            //如果数据库有超过1条的记录，则先判断时间段是否出现重叠再进行拆分
			if(oldCreditAssures.size()>1){
				List<DateComponent> dateCopList = new ArrayList<DateComponent>();
				for(HtlCreditAssure arecord : oldCreditAssures){
					DateComponent dateCop = new DateComponent();
					dateCop.setId(arecord.getID());
					dateCop.setBeginDate(arecord.getBeginDate());
					dateCop.setEndDate(arecord.getEndDate());
					dateCop.setModifyDate(arecord.getModifyTime());
					dateCopList.add(dateCop);
				}
				if(CutDate.compareConflict(dateCopList)){//如果存在重叠，则要拆分
					for(HtlCreditAssure subPrice : oldCreditAssures){
						List subList = contractDao.getSubCreditAssures(creditAssure.getContractId(),subPrice.getRoomType(),subPrice.getModifyTime());
						this.creditAssureUtil(subPrice,subList);
					}
					oldCreditAssures = contractDao.getCreditAssures(creditAssure);
				}
			}
			///////////////////////////////////////////////////
			this.creditAssureUtil(creditAssure,oldCreditAssures);
		}
		return 1;		
	}
	
	/**
	 *  新建一个促销信息
	 * @param salesPromo=促销信息
	 * @return 返回促销信息的id;
	 */
	public Long createSalesPromotion(HtlSalesPromo salesPromo){
		contractDao.saveOrUpdate(salesPromo);
		return salesPromo.getID();		
	}
	
	/**
	 * 修改一个促销信息
	 * @param salesPromo
	 * @return 返回一个整数，如果为1则表示修改成功，否则表示失败
	 */
	public int modifySalesPromotion(HtlSalesPromo salesPromo){
		contractDao.saveOrUpdate(salesPromo);
		return 1;		
	}
	
	/**
	 * 新建一个收费早餐情况，
	 * @param chargeBreakfast
	 * @return 返回收费早餐的id;
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@SuppressWarnings("unchecked")
	public Long createChargeBreakfast(HtlChargeBreakfast chargeBreakfast) throws IllegalAccessException, InvocationTargetException{
		List<HtlChargeBreakfast> oldBreakfasts = contractDao.getBreakfastsOrder(chargeBreakfast);
		if(oldBreakfasts.isEmpty()){
			contractDao.saveOrUpdate(chargeBreakfast);
		}else{
			/////////////////////////////////////////////////////
            //如果数据库有超过1条的记录，则先判断时间段是否出现重叠再进行拆分
			if(oldBreakfasts.size()>1){
				List dateCopList = new ArrayList();
				for(HtlChargeBreakfast arecord : oldBreakfasts){
					DateComponent dateCop = new DateComponent();
					dateCop.setId(arecord.getID());
					dateCop.setBeginDate(arecord.getBeginDate());
					dateCop.setEndDate(arecord.getEndDate());
					dateCop.setModifyDate(arecord.getModifyTime());
					dateCopList.add(dateCop);
				}
				
				if(CutDate.compareConflict(dateCopList)){//如果存在重叠，则要拆分
					for(HtlChargeBreakfast subBreakfast : oldBreakfasts){
						List subList = contractDao.getSubBreakfasts(chargeBreakfast.getContractId(),subBreakfast.getPayMethod(),subBreakfast.getModifyTime());
						this.chargeBreakfastUtil(subBreakfast,subList);
					}
					oldBreakfasts = contractDao.getBreakfastsOrder(chargeBreakfast)	;
				}
			}
			///////////////////////////////////////////////////
			this.chargeBreakfastUtil(chargeBreakfast,oldBreakfasts);
		}
		return chargeBreakfast.getID();
	}
	
	private void chargeBreakfastUtil(HtlChargeBreakfast chargeBreakfast,List<HtlChargeBreakfast> oldBreakfasts) throws IllegalAccessException, InvocationTargetException{
		
		DateComponent dateComponent = new DateComponent();
		dateComponent.setBeginDate(chargeBreakfast.getBeginDate());
		dateComponent.setEndDate(chargeBreakfast.getEndDate());
		List<DateComponent> dateCops = new ArrayList<DateComponent>();
		Map resultMap = new HashMap(); 	
		//放入旧数据
		for(HtlChargeBreakfast charge : oldBreakfasts){
			DateComponent aComponent = new DateComponent();
			aComponent.setId(charge.getID());
			aComponent.setBeginDate(charge.getBeginDate());
			aComponent.setEndDate(charge.getEndDate());
			dateCops.add(aComponent);				
		}
		//旧数据放入Map
		resultMap = CutDate.cut(dateComponent,CutDate.sort(dateCops));
		List removeList = (List) resultMap.get("remove");
		List updateList = (List) resultMap.get("update");
		List results = new ArrayList();
		for(int jj=0; jj<removeList.size(); jj++){
			DateComponent bb = (DateComponent)removeList.get(jj);
			contractDao.remove(HtlChargeBreakfast.class, bb.getId());	
		}	
		 //根据拆分的时间段重新组装数据	
		boolean nullFlag = false;
		for(int i=0; i<oldBreakfasts.size(); i++){
			HtlChargeBreakfast aRecord = (HtlChargeBreakfast) oldBreakfasts.get(i);
			int doubleFlag = 0;
			for(int j=0; j<updateList.size(); j++){
				DateComponent dateCop = (DateComponent) updateList.get(j);				
				if(dateCop.getId()!=null){
					if(dateCop.getId().equals(aRecord.getID())){
						doubleFlag++;
						//如果存在多个相同的id则只有第一个id保留，其他的id都赋值为null
						if(doubleFlag>1){
							HtlChargeBreakfast newRecord = new HtlChargeBreakfast();
							BeanUtils.copyProperties(newRecord,aRecord);
							List templist = aRecord.getBreakfastFees();
							if(templist!=null){
								List alis = MyBeanUtil.copyCollection(templist, HtlBreakfast.class);
								for(int mm=0; mm<alis.size(); mm++){
									HtlBreakfast aB= (HtlBreakfast)alis.get(mm);
									aB.setID(null);
									aB.setBreakfast(newRecord);
								}
								newRecord.setBreakfastFees(alis);
							}
							newRecord.setID(null);
							newRecord.setBeginDate(dateCop.getBeginDate());
							newRecord.setEndDate(dateCop.getEndDate());							
							results.add(newRecord);
						}else{
							aRecord.setBeginDate(dateCop.getBeginDate());
							aRecord.setEndDate(dateCop.getEndDate());	
							results.add(aRecord);
						}
					}
				}else if(nullFlag==false){
					if(chargeBreakfast.getID()!= null){	//处理修改情况					
						HtlChargeBreakfast record = new HtlChargeBreakfast();
						BeanUtils.copyProperties(record,chargeBreakfast);
						List templist = chargeBreakfast.getBreakfastFees();
						record.setID(null);
						if(templist!=null){		
							List alis = MyBeanUtil.copyCollection(templist, HtlBreakfast.class);
							for(int mm=0; mm<alis.size(); mm++){
								HtlBreakfast aB= (HtlBreakfast)alis.get(mm);
								aB.setID(null);
								aB.setBreakfast(record);
							}	
							record.setBreakfastFees(alis);
						}
						results.add(record);
					}else{//处理新增情况
						results.add(chargeBreakfast);
					}
					nullFlag = true;
				}
			}		
		}	
		contractDao.saveOrUpdateAll(results);
		
	}
	
	/**
	 * 修改一个收费早餐情况，
	 * @param chargeBreakfast
	 * @return 返回一个整数，如果为1则表示修改成功，否则表示失败
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@SuppressWarnings("unchecked")
	public int modifyChargeBreakfast(HtlChargeBreakfast chargeBreakfast) throws IllegalAccessException, InvocationTargetException{
		//mod by guojun
		if(null == chargeBreakfast){
			return 0;
		}
		if(chargeBreakfast.getID()==null||chargeBreakfast.getID()==0){
			this.createChargeBreakfast(chargeBreakfast);
		}else{
			List<HtlChargeBreakfast> oldBreakfasts = this.lstChargeBreakfastByContractId(chargeBreakfast.getContractId());
			boolean flag = false;
			/////////////////////////////////////////////////////
            //如果数据库有超过1条的记录，则先判断时间段是否出现重叠再进行拆分
			if(oldBreakfasts.size()>1){
				List dateCopList = new ArrayList();
				for(HtlChargeBreakfast arecord : oldBreakfasts){
					DateComponent dateCop = new DateComponent();
					dateCop.setId(arecord.getID());
					dateCop.setBeginDate(arecord.getBeginDate());
					dateCop.setEndDate(arecord.getEndDate());
					dateCop.setModifyDate(arecord.getModifyTime());
					dateCopList.add(dateCop);
				}
				if(CutDate.compareConflict(dateCopList)){//如果存在重叠，则要拆分
					for(HtlChargeBreakfast subBreakfast : oldBreakfasts){
						List subList = contractDao.getSubBreakfasts(chargeBreakfast.getContractId(),subBreakfast.getPayMethod(),subBreakfast.getModifyTime());
						this.chargeBreakfastUtil(subBreakfast,subList);
					}
					oldBreakfasts = contractDao.getBreakfastsOrder(chargeBreakfast);
				}
			}
			///////////////////////////////////////////////////
			this.chargeBreakfastUtil(chargeBreakfast,oldBreakfasts);
		}
		return 1;		
	}
	
private void bedPriceUtil(HtlAddBedPrice addBedPrice,List oldBedprices) throws IllegalAccessException, InvocationTargetException{
		
		DateComponent dateComponent = new DateComponent();
		dateComponent.setBeginDate(addBedPrice.getBeginDate());
		dateComponent.setEndDate(addBedPrice.getEndDate());
		List dateCops = new ArrayList();
		Map resultMap = new HashMap(); 			
		for(int ii=0; ii<oldBedprices.size(); ii++){
			HtlAddBedPrice price = (HtlAddBedPrice)oldBedprices.get(ii);
			DateComponent aComponent = new DateComponent();
			aComponent.setId(price.getID());
			aComponent.setBeginDate(price.getBeginDate());
			aComponent.setEndDate(price.getEndDate());
			dateCops.add(aComponent);				
		}
		resultMap = CutDate.cut(dateComponent,CutDate.sort(dateCops));
		List removeList = (List) resultMap.get("remove");
		List updateList = (List) resultMap.get("update");
		List results = new ArrayList();
		for(int jj=0; jj<removeList.size(); jj++){
			DateComponent bb = (DateComponent)removeList.get(jj);
			contractDao.remove(HtlAddBedPrice.class, bb.getId());	
		}	
		 //根据拆分的时间段重新组装数据	
		boolean nullFlag = false;
		for(int i=0; i<oldBedprices.size(); i++){
			HtlAddBedPrice aRecord = (HtlAddBedPrice) oldBedprices.get(i);
			int doubleFlag = 0;
			for(int j=0; j<updateList.size(); j++){
				DateComponent dateCop = (DateComponent) updateList.get(j);				
				if(dateCop.getId()!=null){
					if(dateCop.getId().equals(aRecord.getID())){
						doubleFlag++;
						//如果存在多个相同的id则只有第一个id保留，其他的id都赋值为null
						if(doubleFlag>1){
							HtlAddBedPrice newRecord = new HtlAddBedPrice();
							BeanUtils.copyProperties(newRecord,aRecord);							
							newRecord.setID(null);
							newRecord.setBeginDate(dateCop.getBeginDate());
							newRecord.setEndDate(dateCop.getEndDate());							
							results.add(newRecord);
						}else{
							aRecord.setBeginDate(dateCop.getBeginDate());
							aRecord.setEndDate(dateCop.getEndDate());	
							results.add(aRecord);
						}
					}
				}else if(nullFlag==false){
					if(addBedPrice.getID()!= null){	//处理修改情况					
						HtlAddBedPrice record = new HtlAddBedPrice();
						BeanUtils.copyProperties(record,addBedPrice);						
						record.setID(null);						
						results.add(record);
					}else{//处理新增情况
						results.add(addBedPrice);
					}
					nullFlag = true;
				}
			}		
		}	
		//super.saveOrUpdateAll(results);
		contractDao.saveOrUpdateAll(results);
		
	}
	
	/**
	 * 新建一个加床价格信息
	 * @param addBedPrice
	 * @return 返回加床价格信息id
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public Long createAddBedPrice(HtlAddBedPrice addBedPrice) throws IllegalAccessException, InvocationTargetException{
		List<HtlAddBedPrice> oldBedPrices = contractDao.getAddBedPricesOrder(addBedPrice);
		if(oldBedPrices.isEmpty()){
			contractDao.saveAddBedPrice(addBedPrice);
		}else{
			boolean flag = false;
			/////////////////////////////////////////////////////
            //如果数据库有超过1条的记录，则先判断时间段是否出现重叠再进行拆分
			if(oldBedPrices.size()>1){
				List<DateComponent> dateCopList = new ArrayList<DateComponent>();
				for(HtlAddBedPrice arecord : oldBedPrices){
					DateComponent dateCop = new DateComponent();
					dateCop.setId(arecord.getID());
					dateCop.setBeginDate(arecord.getBeginDate());
					dateCop.setEndDate(arecord.getEndDate());
					dateCop.setModifyDate(arecord.getModifyTime());
					dateCopList.add(dateCop);
				}

				if(CutDate.compareConflict(dateCopList)){//如果存在重叠，则要拆分
					for(HtlAddBedPrice subBedprice : oldBedPrices){
						List subList = contractDao.getSubAddBedPrices(subBedprice.getContractId(),subBedprice.getRoomType(),subBedprice.getModifyTime(),subBedprice.getPayMethod());
						this.bedPriceUtil(subBedprice,subList);
					}
					oldBedPrices = contractDao.getAddBedPricesOrder(addBedPrice);
				}
			}
			///////////////////////////////////////////////////
			this.bedPriceUtil(addBedPrice,oldBedPrices);
		}
		return addBedPrice.getID();		
	}
	
	/**
	 * 修改一个加床价格信息
	 * @param addBedPrice
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @return返回一个整数，如果为1则表示修改成功，否则表示失败
	 */
	public int modifyAddBedPrice(HtlAddBedPrice addBedPrice) throws IllegalAccessException, InvocationTargetException{
		if(null == addBedPrice){
			return 0;
		}
		if(addBedPrice.getID()==null||addBedPrice.getID()==0){
			this.createAddBedPrice(addBedPrice);
		}else{
			List<HtlAddBedPrice> oldBedprices = contractDao.getAddBedPricesOrder(addBedPrice);
			boolean flag = false;
			/////////////////////////////////////////////////////
            //如果数据库有超过1条的记录，则先判断时间段是否出现重叠再进行拆分
			if(oldBedprices.size()>1){
				List dateCopList = new ArrayList();
				for(HtlAddBedPrice arecord : oldBedprices){
					DateComponent dateCop = new DateComponent();
					dateCop.setId(arecord.getID());
					dateCop.setBeginDate(arecord.getBeginDate());
					dateCop.setEndDate(arecord.getEndDate());
					dateCop.setModifyDate(arecord.getModifyTime());
					dateCopList.add(dateCop);
				}
				if(CutDate.compareConflict(dateCopList)){//如果存在重叠，则要拆分
					for(HtlAddBedPrice subPrice : oldBedprices){
						List subList = contractDao.getSubAddBedPrices(addBedPrice.getContractId(),subPrice.getRoomType(),subPrice.getModifyTime());
						this.bedPriceUtil(subPrice,subList);
					}

					oldBedprices = contractDao.getAddBedPricesOrder(addBedPrice);
				}
			}
			///////////////////////////////////////////////////
			this.bedPriceUtil(addBedPrice,oldBedprices);
		}
		return 1;				
	}


	/**
	 *  通过合同号列出当前合同的所有收费早餐情况信息。
	 * @param contractId 合同id
	 * @return HtlChargeBreakfast 的list
	 */		
	public List lstChargeBreakfastByContractId(long contractId){
		return contractDao.getBreakfasts(contractId);
	}
	

	public HtlQuotabatch findByQuotabatchId(long quotaBatcId) {
		return (HtlQuotabatch)contractDao.findByQuotabatchId(HtlQuotabatch.class, quotaBatcId);
	}
	
	public void continueContract(Long contractId, long hotelId, List lstDates, boolean isPeriod, Object user){		
		if (lstDates!=null && lstDates.size()>0){
			if(isPeriod){				
				for (int k=0; k<lstDates.size();k++){
					ContractContinue cc=(ContractContinue) lstDates.get(k);
					if (callContinueContractStoreProcedure(hotelId,contractId,cc,user)==0){
						break;
					}						
				}					
			}else{
				for (int k=0; k<lstDates.size();k++){
					SingleContractContinue cc=(SingleContractContinue) lstDates.get(k);
					if (callSingleContinueContractStoreProcedure(hotelId,contractId,cc,user)==0){
						break;
					}						
				}
			}
						
		}
	//	tx.commit();
	}
	
	private int callContinueContractStoreProcedure(long hotelId,long contractId,ContractContinue cc,Object user){
		
		return contractDao.callContinueContractStoreProcedure(hotelId, contractId, cc, user);
    }
    
    private int callSingleContinueContractStoreProcedure(long hotelId,long contractId,SingleContractContinue cc,Object user){       
    	
    	return contractDao.callSingleContinueContractStoreProcedure(hotelId,contractId,cc,user);
    }
	
	/**
	 * 缩短合同
	 * @param cutDate 缩短合同至
	 */
	public void cutContractDate(Long contractId,Date cutDate){
		HtlContract contract = (HtlContract) contractDao.findHtlContractByID(HtlContract.class, contractId);
		Date endDate= contract.getEndDate();
		
		Date newCutDate = DateUtil.getDate(cutDate, 1);
		saleDao.removeQuotaAndPrice(contractId, newCutDate, endDate);
		contract.setEndDate(cutDate);
		contractDao.saveOrUpdate(contract);
	}
	
	/**
	 * 检查合同的起止时间是否重复
	 * @param contract
	 * @return 如果重复就返回1，否则就返回0
	 */
	public int checkContractDate(HtlContract contract){
		long pHotelId = contract.getHotelId();
		Date beginD = contract.getBeginDate();
		Date endD = contract.getEndDate();		
		return saleDao.checkContractDate(pHotelId,beginD,endD);
	}

	
	public int checkContractDate(long hotelid,String beginDate,String endDate){
		Date beginD =DateUtil.getDate(beginDate);
		Date endD = DateUtil.getDate(endDate);
		return saleDao.checkContractDate(hotelid,beginD,endD);
	}
	
	public int checkAreaExist(String areaCode,String cityCode){
		
		return saleDao.checkAreaExist(areaCode,cityCode);
	}
	
	public int checkEditContractDate(long hotelid,String beginDate,String endDate,String beginDateOld,String endDateOld){
		Date beginD =DateUtil.getDate(beginDate);
		Date endD = DateUtil.getDate(endDate);
		Date beginDOld =DateUtil.getDate(beginDateOld);
		Date endDOld = DateUtil.getDate(endDateOld);		
		return saleDao.checkEditContractDate(hotelid,beginD,endD,beginDOld,endDOld);	
	}
	
	public ISaleDao getSaleDao() {
		return saleDao;
	}

	public void setSaleDao(ISaleDao saleDao) {
		this.saleDao = saleDao;
	}	
	

	//增加合同信息
	public boolean addContractFile(ContractFile contractFile)
	{
		boolean flag = false;
		Object o = contractFileDao.addContractFile("insertContractFile", contractFile);
		if(o != null)
		{
			flag = true;
		}
		return flag;
	}		

	//删除合同信息
	public boolean deleteContractFile(ContractFile contractFile)
	{
		boolean flag = false;
		int o = contractFileDao.deleteContractFile("deleteContractFile", contractFile);
		if(o > 0)
		{
			flag = true;
		}
		return flag;
	}	
	
	//修改合同片信息
	public boolean modifyContractFile(ContractFile contractFile)
	{
		boolean flag = false;
		int o = contractFileDao.modifyContractFile("updateContractFile", contractFile);
		if(o > 0)
		{
			flag = true;
		}
		return flag;
	}	

	//查询合同信息by Id
	public ContractFile queryContractFileById(ContractFile contractFile)
	{
		ContractFile o = contractFileDao.queryContractFileById("selectContractFileById", contractFile);
		if(o != null)
		{
			return o;
		}
		else
		{
			return null;
		}
	}	
	
	//查询合同信息列表
	public List queryContractFileList(ContractFile contractFile)
	{
		List list = contractFileDao.queryContractFileList("selectContractFile", contractFile);
		if(list != null && list.size()>0)
		{
			return list;
		}
		else
		{
			return new ArrayList();
		}
	}

	public ContractFileDao getContractFileDao() {
		return contractFileDao;
	}

	public void setContractFileDao(ContractFileDao contractFileDao) {
		this.contractFileDao = contractFileDao;
	}

	public HtlContract checkContractDateNew(long hotelid, Date beginDate) {
		return contractDao.getHtlContract(hotelid);
	}
	
	public int checkRoomType(long hotelid,long roomTypeID,String roomName){
		return saleDao.checkRoomType(hotelid,roomTypeID,roomName);
	}

	public int updateCurrencyPattern(Long hotelId, Long contractId, String currency, String quotaPattern, Date beginDate, Date endDate) {	
		String hsql = null ;
		Object obj[] = null ;
		if(currency!=null){
			contractDao.updateContract(currency, hotelId, beginDate, endDate);
		}
		if(quotaPattern!=null){
			contractDao.updateHtlQuota(quotaPattern,contractId);
		}
		return 1;
	}
	
	public boolean justContinueContract(long hotelId, long contractId, String oldBeginDate, String continueDate){
		Date beginD =DateUtil.getDate(oldBeginDate);
		Date endD = DateUtil.getDate(continueDate);
		if(saleDao.checkContinueContractDate(hotelId, contractId, beginD, endD)==1){
			return false;
		}
		HtlContract contract = (HtlContract) contractDao.findHtlContractByID(HtlContract.class, contractId);
		contract.setEndDate(DateUtil.getDate(continueDate));
		contractDao.saveOrUpdate(contract);
		return true;
	}
	
	public List<ContinueDatecomponent> checkContinuePrice(long hotelId, long contractId, List<ContinueDatecomponent> dateComponents){
		return saleDao.checkContinuePrice(hotelId, contractId, dateComponents);
	}

	/**
	 * 检查房型是否有关房历史
	 * @param hotelId
	 * @param childRoomTypeId
	 * @param beginDate
	 * @param EndDate
	 * @return
	 */
	public List checkCloseHistory(long hotelId,String childRoomTypeId,List<ContinueDatecomponent> dateComponents){
		return saleDao.checkCloseHistory(hotelId, childRoomTypeId, dateComponents);
	}

	/**
     * 检查开房是否会导致bug add by huizhong.chen 2008-9-20
     * 
     * @param hotelId
     * @param childRoomTypeId
     * @param ContinueDatecomponent
     * @return
     */
	public boolean checkCloseHistoryBySameReason(long hotelId, String childRoomTypeId, List<ContinueDatecomponent> dateComponents)
	  {
	    String[] childRId = childRoomTypeId.split(",");
	    List<HtlOpenCloseRoom> lisOpenCloseRoom = new ArrayList<HtlOpenCloseRoom>();
	    boolean flag = false;
	    for (int i = 0; i < childRId.length; i++) {
	      ContinueDatecomponent continueDatecomponent = (ContinueDatecomponent)dateComponents.get(i);
	      Date beginDate = DateUtil.getDate(continueDatecomponent.getBeginDate());
	      Date endDate = DateUtil.getDate(continueDatecomponent.getEndDate());
	      lisOpenCloseRoom = this.saleDao.getHtlOpenClose(new Object[] {Long.valueOf(hotelId), childRId[i], beginDate, endDate, beginDate, endDate, beginDate, 
	        endDate });
	      if (lisOpenCloseRoom.size() > 1) {
	        Set<String> reasonSet = new HashSet<String>();
	        for (HtlOpenCloseRoom htlOpenCloseRoom : lisOpenCloseRoom)
	          if (!reasonSet.add(htlOpenCloseRoom.getCauseSign())) return flag = true;
	      }
	    }
	    return flag;
	  }
	
	/**
	 * 按酒店id查询当前所在的合同实体
	 * @param hotelId
	 * @return
	 */
	public HtlContract queryCurrentContractByHotelId(long hotelId) {
		/*
		String hql = " from HtlContract where hotelId = ? and beginDate <= trunc(sysdate) and endDate >= trunc(sysdate)";
		List<HtlContract> contractList = super.doquery(hql, new Object[]{hotelId}, false);
		if(contractList != null && !contractList.isEmpty()){
			return contractList.get(0);
		}else{
			return new HtlContract();
		}*/
		return contractDao.getHtlContracts(new Object[]{hotelId});
	}

	/**
	 * 查询某合同下某房型的加床信息 hotel 2.9.2 add by chenjiajie 2009-07-23 
	 */
	public List<HtlAddBedPrice> queryAddBed(long contractId, long roomType,Date checkInDate,Date checkOutDate,String payMethod) {
		return (List<HtlAddBedPrice>)contractDao.getAddBedPrice(contractId,String.valueOf(roomType),checkInDate,checkOutDate,payMethod);
	}

	/**
	 * 查询某合同的加早信息 hotel 2.9.2 add by chenjiajie 2009-07-23 
	 */
	public List<HtlChargeBreakfast> queryBreakfast(long contractId,Date checkInDate,Date checkOutDate) {
		return (List<HtlChargeBreakfast>)contractDao.getBreakfast(contractId,checkInDate,checkOutDate);
	}

	/**
	 * 查询某合同下的房型的免费宽带信息 hotel 2.9.2 add by chenjiajie 2009-07-23 
	 */
	public List<HtlInternet> queryInternet(long contractId, long roomType,Date checkInDate,Date checkOutDate) {
		return (List<HtlInternet>)contractDao.getInternetByRoomType(contractId, roomType,checkInDate,checkOutDate);
	}

	/**
	 * 查询某合同下的接送价信息 hotel 2.9.2 add by chenjiajie 2009-07-23 
	 */
	public List<HtlWelcomePrice> queryWelcomePrice(long contractId,Date checkInDate,Date checkOutDate) {
		return (List<HtlWelcomePrice>)contractDao.getWelcomePrice(contractId,checkInDate,checkOutDate);
	}

	public IContractDao getContractDao() {
		return contractDao;
	}

	public void setContractDao(IContractDao contractDao) {
		this.contractDao = contractDao;
	}
	
	/**
	 * 新增一个提示信息 hotel 2.9.2 add by shengwei.zuo 2009-08-07
	 */
	public Long createAlerttypeInfo(HtlAlerttypeInfo htlAlerttypeInfo)
			throws IllegalAccessException, InvocationTargetException {
		if (null == htlAlerttypeInfo) {
			return 0L;
		}

		contractDao.saveHtlAlerttypeInfo(htlAlerttypeInfo);

		return htlAlerttypeInfo.getId();
	}
	
	
	/**
	 * 修改一个提示信息
	 * @param creditAssure
	 * @return 返回一个整数，如果为1则表示修改成功，否则表示失败
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public int modifyAlerttypeInfo(HtlAlerttypeInfo htlAlerttypeInfo) throws IllegalAccessException, InvocationTargetException{
		if( null == htlAlerttypeInfo){
			return 0;
		}
		if(htlAlerttypeInfo.getId()==null||htlAlerttypeInfo.getId()==0){
			this.createAlerttypeInfo(htlAlerttypeInfo);
		}else{
			contractDao.saveHtlAlerttypeInfo(htlAlerttypeInfo);
		}
		return 1;		
	}
	
	
	
	private void alertInfoUtil(HtlAlerttypeInfo alertInfo,List oldAlertInfoList)throws IllegalAccessException, InvocationTargetException{
		
		DateComponent dateComponent = new DateComponent();
		dateComponent.setBeginDate(alertInfo.getBeginDate());
		dateComponent.setEndDate(alertInfo.getEndDate());
		List dateCops = new ArrayList();
		Map resultMap = new HashMap(); 	
		
		for(int ii=0;ii<oldAlertInfoList.size();ii++){
			
			HtlAlerttypeInfo alertInfoII = (HtlAlerttypeInfo)oldAlertInfoList.get(ii);
			DateComponent aComponent = new DateComponent();
			aComponent.setId(alertInfoII.getId());
			aComponent.setBeginDate(alertInfoII.getBeginDate());
			aComponent.setEndDate(alertInfoII.getEndDate());
			dateCops.add(aComponent);			
		}

		resultMap = CutDate.cut(dateComponent,CutDate.sort(dateCops));
		List removeList = (List) resultMap.get("remove");
		List updateList = (List) resultMap.get("update");
		List results = new ArrayList();
		for(int jj=0; jj<removeList.size(); jj++){
			DateComponent bb = (DateComponent)removeList.get(jj);
			contractDao.remove(HtlAlerttypeInfo.class, bb.getId());	
		}	
		
		 //根据拆分的时间段重新组装数据	
		boolean nullFlag = false;
		for(int i=0; i<oldAlertInfoList.size(); i++){
			
			HtlAlerttypeInfo alertInfoObj= (HtlAlerttypeInfo) oldAlertInfoList.get(i);
			int doubleFlag = 0;
			for(int j=0; j<updateList.size(); j++){
				DateComponent dateCop = (DateComponent) updateList.get(j);				
				if(dateCop.getId()!=null){
					if(dateCop.getId().equals(alertInfoObj.getId())){
						doubleFlag++;
						//如果存在多个相同的id则只有第一个id保留，其他的id都赋值为null
						if(doubleFlag>1){
							HtlAlerttypeInfo newAlertInfoObj= new HtlAlerttypeInfo();
							BeanUtils.copyProperties(newAlertInfoObj,alertInfoObj);
							
							newAlertInfoObj.setId(null);
							newAlertInfoObj.setBeginDate(dateCop.getBeginDate());
							newAlertInfoObj.setEndDate(dateCop.getEndDate());							
							results.add(newAlertInfoObj);
						}else{
							alertInfoObj.setBeginDate(dateCop.getBeginDate());
							alertInfoObj.setEndDate(dateCop.getEndDate());	
							results.add(alertInfoObj);
						}
					}	
				}else if(nullFlag==false){
					if(alertInfo.getId()!= null){	//处理修改情况		
						
						HtlAlerttypeInfo alerttypeInfoUpt = new HtlAlerttypeInfo();
						BeanUtils.copyProperties(alerttypeInfoUpt,alertInfo);
						
						alerttypeInfoUpt.setId(null);
						results.add(alerttypeInfoUpt);
						
					}else{//处理新增情况
						results.add(alertInfo);
					}
					nullFlag = true;
					
				}
			}
		}
		
		contractDao.saveOrUpdateAll(results);
		
	}
	
	/**
	 * 根据价格类型id，获得价格类型的名称 add by shengwei.zuo 2009-08-07
	 * @param priceTypeID
	 * @return
	 */
	public String priceTypeName(long priceTypeID) {

		HtlRoomtype roomtype= contractDao.findHtlRoomType(priceTypeID);
		List  lstPriceType = roomtype.getLstPriceType();
		String priceTypeStr="";
		int  j=0;
		for(int i =0;i<lstPriceType.size();i++){
			
			HtlPriceType  priceTypeObj = (HtlPriceType)lstPriceType.get(i);
			
			if(priceTypeObj.getID()==priceTypeID)
			{
				priceTypeStr+=(j>0?",":"")+roomtype.getRoomName()+"("+priceTypeObj.getPriceType()+")";
				j++;
			}
		}
		
		return priceTypeStr;
	}
	
	/**
	 * 根据提示信息ID，查询对应的提示信息内容
	 * @param id
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public HtlAlerttypeInfo queryAlerttypeInfoById(long id)
			throws IllegalAccessException, InvocationTargetException {
		HtlAlerttypeInfo alerttypeInfo = contractDao.findHtlAlerttypeInfo(id);
		return alerttypeInfo;
	}
	
	
	/**
	 * 根据提示信息ID，删除对应的提示信息内容
	 * @param id
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public long deleteAlerttypeInfoById(HtlAlerttypeInfo alerttypeInfo)
		throws IllegalAccessException, InvocationTargetException {
		contractDao.remove(HtlFavourableclause.class,alerttypeInfo.getId());
		return alerttypeInfo.getId();
	}

	public List queryAlertInfoByConId(long contractId) {
		
		return contractDao.getAlertInfosByConId(contractId);
	}
	public List queryAlertInfoByConId(long contractId,boolean assemble) {
		List lstAlertInfo = null;
		lstAlertInfo = this.queryAlertInfoByConId(contractId);
		if(assemble){
			lstAlertInfo = this.queryAlertInfoByConId(lstAlertInfo);
		}		
		return lstAlertInfo;
	}
	
	private List queryAlertInfoByConId(List list){
		List listAlertInfo = new ArrayList();
		boolean  fristReadFlag = false;
		Iterator   iter = null;
		HtlAlerttypeInfo info = null;
		String modifiedTime = null;
		String preModifiedTime = "preModifiedTime";
		String priceTypeName = null;
		String ids = null;
		HtlAlerttypeInfo infoCopyTo = new HtlAlerttypeInfo();
		iter = list.iterator();
		while(iter.hasNext()){
			info = (HtlAlerttypeInfo)iter.next();
			if(!fristReadFlag){//第一次读
				MyBeanUtil.copyProperties(infoCopyTo, info);
				infoCopyTo.setIds(info.getId().toString());
				infoCopyTo.setPriceTypeName(info.getPriceTypeName());
				fristReadFlag = true;
			}else{
				if(preModifiedTime.equals(info.getModifyTime().toString())){//如果是读取过
					infoCopyTo.setPriceTypeName(infoCopyTo.getPriceTypeName()+","+info.getPriceTypeName());
					if(null==infoCopyTo.getIds()){
						infoCopyTo.setIds(infoCopyTo.getId().toString()+","+info.getId().toString());
					}else{
						infoCopyTo.setIds(infoCopyTo.getIds().toString()+","+info.getId().toString());
					}
				}else{
					listAlertInfo.add(infoCopyTo);
					infoCopyTo = new HtlAlerttypeInfo();
					MyBeanUtil.copyProperties(infoCopyTo, info);
				}
			}
			preModifiedTime = info.getModifyTime().toString();
			if(!iter.hasNext()){
				listAlertInfo.add(infoCopyTo);
			}
		}
		return listAlertInfo;
	}
	
	/**
	 * 优惠条款拆分日期的辅助类  add by shengwei.zuo hotel 2.9.3 2009-08-26
	 * @param favourableclause
	 * @param oldFavourableclauseList
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void favClauseUtil(HtlFavourableclause favourableclause,List oldFavClauseList)throws IllegalAccessException, InvocationTargetException{
		
		DateComponent dateComponent = new DateComponent();
		dateComponent.setBeginDate(favourableclause.getBeginDate());
		dateComponent.setEndDate(favourableclause.getEndDate());
		List dateCops = new ArrayList();
		Map resultMap = new HashMap(); 	
		
		for(int ii=0;ii<oldFavClauseList.size();ii++){
			
			HtlFavourableclause favClauseII = (HtlFavourableclause)oldFavClauseList.get(ii);
			DateComponent aComponent = new DateComponent();
			aComponent.setId(favClauseII.getId());
			aComponent.setBeginDate(favClauseII.getBeginDate());
			aComponent.setEndDate(favClauseII.getEndDate());
			dateCops.add(aComponent);			
			
		}
		
		resultMap = CutDate.cut(dateComponent,CutDate.sort(dateCops));
		List removeList = (List) resultMap.get("remove");
		List updateList = (List) resultMap.get("update");
		List results = new ArrayList();
		for(int jj=0; jj<removeList.size(); jj++){
			DateComponent bb = (DateComponent)removeList.get(jj);
			contractDao.remove(HtlFavourableclause.class, bb.getId());	
		}	
		
		 //根据拆分的时间段重新组装数据	
		boolean nullFlag = false;
		for(int i=0; i<oldFavClauseList.size(); i++){
			
			HtlFavourableclause favClauseObj= (HtlFavourableclause) oldFavClauseList.get(i);
			int doubleFlag = 0;
			for(int j=0; j<updateList.size(); j++){
				DateComponent dateCop = (DateComponent) updateList.get(j);				
				if(dateCop.getId()!=null){
					if(dateCop.getId().equals(favClauseObj.getId())){
						doubleFlag++;
						//如果存在多个相同的id则只有第一个id保留，其他的id都赋值为null
						if(doubleFlag>1){
							HtlFavourableclause newFavClauseObj= new HtlFavourableclause();
							BeanUtils.copyProperties(newFavClauseObj,favClauseObj);
							
							List lstNewParameterObj = new ArrayList();
							HtlFavouraParameter newParameterObj = new HtlFavouraParameter();
							
							List lstOldParameterObj =  favClauseObj.getLstPackagerate();
							
							//不为空的判断
							if(!lstOldParameterObj.isEmpty()){
								
								//优惠条款参数实体类
								HtlFavouraParameter oldParameterObj =(HtlFavouraParameter)lstOldParameterObj.get(0);
								
								newParameterObj.setCirculateType(oldParameterObj.getCirculateType());
								newParameterObj.setContinueNight(oldParameterObj.getContinueNight());
								
								newParameterObj.setDecimalPointType(oldParameterObj.getDecimalPointType());
								newParameterObj.setDiscount(oldParameterObj.getDiscount());
								
								newParameterObj.setDonateNight(oldParameterObj.getDonateNight());
								newParameterObj.setFavourableType(oldParameterObj.getFavourableType());
								
								newParameterObj.setPackagerateCommission(oldParameterObj.getPackagerateCommission());
								newParameterObj.setPackagerateNight(oldParameterObj.getPackagerateNight());
								
								newParameterObj.setPackagerateSaleprice(oldParameterObj.getPackagerateSaleprice());
								
								//当优惠类型为 连住N晚送M晚时，才会有各晚房费的实体类
								if(oldParameterObj.getFavourableType().equals(FavourableTypeUtil.continueDonate)||oldParameterObj.getFavourableType()==FavourableTypeUtil.continueDonate){
								
										List lstOldEvenObj =   oldParameterObj.getLstEveningsRent();
										
										List lstNewEvenObj = new ArrayList(); 
										
										for(int p=0;p<lstOldEvenObj.size();p++){
											
											HtlEveningsRent oldEvenObj = (HtlEveningsRent)lstOldEvenObj.get(p);
											HtlEveningsRent newEvenObj = new HtlEveningsRent();
											
											newEvenObj.setCommission(oldEvenObj.getCommission());
											newEvenObj.setNight(oldEvenObj.getNight());
											newEvenObj.setSalePrice(oldEvenObj.getSalePrice());
											newEvenObj.setFavouraParameterEntiy(newParameterObj);
											
										    lstNewEvenObj.add(newEvenObj);
											
										}
										
										newParameterObj.setLstEveningsRent(lstNewEvenObj);
								
								}
								
								lstNewParameterObj.add(newParameterObj);
								
								newFavClauseObj.setLstPackagerate(lstNewParameterObj);
							
							}
							
							newFavClauseObj.setId(null);
							newFavClauseObj.setBeginDate(dateCop.getBeginDate());
							newFavClauseObj.setEndDate(dateCop.getEndDate());							
							results.add(newFavClauseObj);
						}else{
							favClauseObj.setBeginDate(dateCop.getBeginDate());
							favClauseObj.setEndDate(dateCop.getEndDate());	
							results.add(favClauseObj);
						}
					}	
				}else if(nullFlag==false){
					if(favourableclause.getId()!= null){	//处理修改情况		
						
						HtlFavourableclause favClauseUpt = new HtlFavourableclause();
						BeanUtils.copyProperties(favClauseUpt,favourableclause);
						
						List lstUptNewParameterObj = new ArrayList();
						HtlFavouraParameter newUptParameterObj = new HtlFavouraParameter();
						
						List lstUptOldParameterObj =  favourableclause.getLstPackagerate();
						
						//不为空的判断
						if(!lstUptOldParameterObj.isEmpty()){
						
							HtlFavouraParameter oldUptParameterObj =(HtlFavouraParameter)lstUptOldParameterObj.get(0);
							
	
							newUptParameterObj.setCirculateType(oldUptParameterObj.getCirculateType());
							newUptParameterObj.setContinueNight(oldUptParameterObj.getContinueNight());
							
							newUptParameterObj.setDecimalPointType(oldUptParameterObj.getDecimalPointType());
							newUptParameterObj.setDiscount(oldUptParameterObj.getDiscount());
							
							newUptParameterObj.setDonateNight(oldUptParameterObj.getDonateNight());
							newUptParameterObj.setFavourableType(oldUptParameterObj.getFavourableType());
							
							newUptParameterObj.setPackagerateCommission(oldUptParameterObj.getPackagerateCommission());
							newUptParameterObj.setPackagerateNight(oldUptParameterObj.getPackagerateNight());
							
							newUptParameterObj.setPackagerateSaleprice(oldUptParameterObj.getPackagerateSaleprice());
							
							//当优惠类型为 连住N晚送M晚时，才会有各晚房费的实体类
							if(oldUptParameterObj.getFavourableType().equals(FavourableTypeUtil.continueDonate)||oldUptParameterObj.getFavourableType()==FavourableTypeUtil.continueDonate){
							
									List lstUptOldEvenObj =   oldUptParameterObj.getLstEveningsRent();
									
									List lstUptNewEvenObj = new ArrayList(); 
									
									for(int p=0;p<lstUptOldEvenObj.size();p++){
										
										HtlEveningsRent oldUptEvenObj = (HtlEveningsRent)lstUptOldEvenObj.get(p);
										HtlEveningsRent newUptEvenObj = new HtlEveningsRent();
										
										newUptEvenObj.setCommission(oldUptEvenObj.getCommission());
										newUptEvenObj.setNight(oldUptEvenObj.getNight());
										newUptEvenObj.setSalePrice(oldUptEvenObj.getSalePrice());
										newUptEvenObj.setFavouraParameterEntiy(newUptParameterObj);
										
										lstUptNewEvenObj.add(newUptEvenObj);
										
									}
									
									newUptParameterObj.setLstEveningsRent(lstUptNewEvenObj);
							
							}
							
							lstUptNewParameterObj.add(newUptParameterObj);
							
							favClauseUpt.setLstPackagerate(lstUptNewParameterObj);
						}
						
						favClauseUpt.setId(null);
						results.add(favClauseUpt);
						
					}else{//处理新增情况
						results.add(favourableclause);
					}
					nullFlag = true;
					
				}
			}
		}
		
		contractDao.saveOrUpdateAll(results);
		
	}
	
	/**
	 * 新增一个优惠条款
	 * @param favourableclause
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public Long createFavourableclause(HtlFavourableclause favourableclause) throws IllegalAccessException, InvocationTargetException  {
		
		if(null == favourableclause){
			return 0L;
		}
		List<HtlFavourableclause> oldFavourableclause = contractDao.getFavourableclauseOrder(favourableclause);
		if(oldFavourableclause==null||oldFavourableclause.size()==0){
			contractDao.saveOrUpdate(favourableclause);
		}else{
			
			boolean flag = false;
			/////////////////////////////////////////////////////
            //如果数据库有超过1条的记录，则先判断时间段是否出现重叠再进行拆分
			if(oldFavourableclause.size()>1){
				List dateCopList = new ArrayList();
				for(HtlFavourableclause fClause : oldFavourableclause){
					DateComponent dateCop = new DateComponent();
					dateCop.setId(fClause.getId());
					dateCop.setBeginDate(fClause.getBeginDate());
					dateCop.setEndDate(fClause.getEndDate());
					dateCop.setModifyDate(fClause.getModifyTime());
					dateCopList.add(dateCop);
				}
				if(CutDate.compareConflict(dateCopList)){//如果存在重叠，则要拆分
					for(HtlFavourableclause subFavClause : oldFavourableclause){
						List subList = contractDao.getSubFavClause(favourableclause.getContractId(),subFavClause.getPriceTypeId(),subFavClause.getModifyTime());
						this.favClauseUtil(subFavClause, subList);
					}
					oldFavourableclause = contractDao.getFavourableclauseOrder(favourableclause);
				}
				
			}
			///////////////////////////////////////////////////
			this.favClauseUtil(favourableclause, oldFavourableclause);
			
		}
		
		return favourableclause.getId();
	}
	
	
	/**
	 * 根据合同ID查询出该酒店下的优惠条款  hotel2.9.3
	 * @param contractId  add by shengwei.zuo 2009-08-25
	 * @return
	 */
	
	public List queryAllavourableclause(long contractId) {
		List lstFavClause = contractDao.getHtlFavourableclauseByContractId(contractId);
		lstFavClause=pingSameFavClause(lstFavClause);
		return lstFavClause;
	}
	
	/**
	 * 将优惠类型相同，时间段相同的子房型拼成字符串，显示成一条记录  add by shengwei.zuo 2009-08-27 hotel2.9.3
	 * @param lstFavClause
	 * @return
	 */
	public List pingSameFavClause(List lstFavClause){
		
		for(int i=0;i<lstFavClause.size();i++){
			
			StringBuffer favClauseStrBuff= new StringBuffer();
			
			StringBuffer favClauseNameStrBuff= new StringBuffer();
			
			StringBuffer favIdStrBuff = new StringBuffer();
			
			//价格类型ID字符串
			String  favClauseStr="";
			
			//价格类型名称字符串
			String  favClauseNameStr="";
			
			//ID的字符串
			String  favIdStr = "";
			
			
			HtlFavourableclause favClauseOne = (HtlFavourableclause)lstFavClause.get(i);
			
			favClauseStrBuff.append(favClauseOne.getPriceTypeId()+",");
			
			favClauseNameStrBuff.append(favClauseOne.getPriceTypeName()+",");
			
			favIdStrBuff.append(favClauseOne.getId()+",");
			
			Long randomNumberOne= favClauseOne.getRandomNumber();
			
			for(int j=lstFavClause.size()-1;j>i;j--){
				
				HtlFavourableclause favClauseTwo = (HtlFavourableclause)lstFavClause.get(j);
				
				Long randomNumberTwo = favClauseTwo.getRandomNumber();
				
				//随机数相同的价格类型，就拼装成一个字符串，显示在查询页面
				if(randomNumberOne==randomNumberTwo||randomNumberTwo.equals(randomNumberOne)){
					
					if( (favClauseOne.getBeginDate().equals(favClauseTwo.getBeginDate())||favClauseOne.getBeginDate()==favClauseTwo.getBeginDate())
						&&(favClauseOne.getEndDate().equals(favClauseTwo.getEndDate())||favClauseOne.getEndDate()==favClauseTwo.getEndDate())
					){
						favClauseStrBuff.append(favClauseTwo.getPriceTypeId()+",");
						
						favClauseNameStrBuff.append(favClauseTwo.getPriceTypeName()+",");
						
						favIdStrBuff.append(favClauseTwo.getId()+",");
						
						lstFavClause.remove(j);
					}
						
				}
				
			}
			
			favClauseStr = favClauseStrBuff.toString();
			favClauseNameStr = favClauseNameStrBuff.toString();
			favIdStr = favIdStrBuff.toString();
			
			favClauseStr=favClauseStr.substring(0,favClauseStr.length()-1);
			favClauseNameStr=favClauseNameStr.substring(0,favClauseNameStr.length()-1);
			favIdStr = favIdStr.substring(0,favIdStr.length()-1);
			
			favClauseOne.setPriceTypeIdStr(favClauseStr);
			favClauseOne.setPriceTypeNameStr(favClauseNameStr);
			favClauseOne.setFavClauseIdStr(favIdStr);
			
		}
		
		return lstFavClause;
	}
	
	/**
	 * 删除优惠条款 add by shengwei.zuo 2009-08-31
	 * @param favourableclause
	 * @return
	 */
	public long deleteFavClauseObj(HtlFavourableclause favourableclause) {
		contractDao.remove(HtlFavourableclause.class,favourableclause.getId());
		return favourableclause.getId();
	}
	
	
	/**
	 * 根据ID查询对应的优惠条款ID
	 * 
	 */
	public HtlFavourableclause getFavClauseById(long id) {
		return contractDao.findHtlFavourableclause(id);
	}
	
	/**
	 * hotel 2.9.3
	 * 
	 * 修改一个优惠条款的信息 add by shengwei.zuo 2009-08-31
	 * @param favourableclause
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public long modifyFavClause(HtlFavourableclause favourableclause) throws IllegalAccessException, InvocationTargetException {
		if(null == favourableclause){
			return 0L;
		}
		if(favourableclause.getId()==null||favourableclause.getId()==0){
			this.createFavourableclause(favourableclause);
		}else{
			List<HtlFavourableclause> oldFavClauseLst = contractDao.getFavourableclauseOrder(favourableclause);
			boolean flag = false;
			/////////////////////////////////////////////////////
            //如果数据库有超过1条的记录，则先判断时间段是否出现重叠再进行拆分
			if(oldFavClauseLst.size()>1){
				List dateCopList = new ArrayList();
				for(HtlFavourableclause aFavCla : oldFavClauseLst){
					DateComponent dateCop = new DateComponent();
					dateCop.setId(aFavCla.getId());
					dateCop.setBeginDate(aFavCla.getBeginDate());
					dateCop.setEndDate(aFavCla.getEndDate());
					dateCop.setModifyDate(aFavCla.getModifyTime());	
					dateCopList.add(dateCop);
				}
				if(CutDate.compareConflict(dateCopList)){//如果存在重叠，则要拆分
					for(HtlFavourableclause subFavCla : oldFavClauseLst){
						List subList = contractDao.getSubFavClause(favourableclause.getContractId(),subFavCla.getPriceTypeId(),subFavCla.getModifyTime());
						this.favClauseUtil(subFavCla,subList);
					}
					//flag = true;
					oldFavClauseLst =  contractDao.getFavourableclauseOrder(favourableclause);
				}
			}
			///////////////////////////////////////////////////
			this.favClauseUtil(favourableclause,oldFavClauseLst);
		}
		return 1;		
		
	}

	public void deleteAlertTypeInfoByIds(String[] idArray,String className) {
		contractDao.deleteAlertTypeInfoByIds(idArray,className);
		
	}

	public List<HtlAlerttypeInfo> queryAlerttTypeInfoByIds(String ids) {
		return contractDao.queryAlerttypeInfoByIds(ids);
	}
	
	public HtlContract getHtlContractByHtlIdDateRange(long hotelId, Date beginDate, Date endDate) {
		return contractDao.qryHtlContractByHtlIdValidDate(hotelId, beginDate, endDate);
	}
	
	/**
	 * 
	 * refactor: 根据"lstPreOrderSalePromosRoom" sql语句查询酒店促销信息
	 * 
	 * @param params
	 * @return
	 */	
	public List<HtlSalesPromo> lstPreOrderSalePromosRoom(Object[] params) {
		return contractDao.lstPreOrderSalePromosRoom(params);
	}
	
	/**
	 * 
	 * refactor: 根据"lstPreOrderPresale" sql语句查询芒果促销信息
	 * 
	 * @param params
	 * @return
	 */
	public List<HtlPresale> lstPreOrderPresale(Object[] params) {
		return contractDao.lstPreOrderPresale(params);
	}
	
	/**
	 * 
	 * refactor: 根据hotelId和priceTypeId查询芒果优惠信息
	 * 
	 * @param hotelId
	 * @param priceTypeId
	 * @return
	 */
	public List<HtlFavourableclause> queryFavourableclauseByHotelAndPriceType(
			Long hotelId, Long priceTypeId) {
		return contractDao.queryFavourableclauseByHotelAndPriceType(hotelId,
				priceTypeId);
	}
}

