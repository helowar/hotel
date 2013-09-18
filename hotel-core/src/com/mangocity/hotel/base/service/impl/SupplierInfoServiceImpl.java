package com.mangocity.hotel.base.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlBookSetup;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlSupplierBookSetup;
import com.mangocity.hotel.base.persistence.HtlSupplierInfo;
import com.mangocity.hotel.base.service.SupplierInfoService;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class SupplierInfoServiceImpl implements SupplierInfoService {
	
	private GenericDAOHibernateImpl genericDAOHibernateImpl;
	
	public List<HtlSupplierInfo> queryAllSupplierInfo() {
		String hsql = "from HtlSupplierInfo";
		return genericDAOHibernateImpl.find(hsql, null);
	}
	
	public HtlSupplierInfo querySupplierInfoByID(long ID) {
		return genericDAOHibernateImpl.get(HtlSupplierInfo.class, ID);
	}
	
	public void delSupplierInfo(long ID) {
		genericDAOHibernateImpl.remove(HtlSupplierInfo.class, ID);
	}
	
	public void fakeDelSupplierInfo(long ID) {
		String hql = "update HtlSupplierInfo hs set hs.active = 0 where hs.ID=?";
		genericDAOHibernateImpl.updateByQL(hql, new Object[]{ID});	
	}
	
	/**
     * 更新供应商信息
     * @return
     */
    public void saveOrUpdateSupplierInfo(HtlSupplierInfo htlSupplierInfo){
    	genericDAOHibernateImpl.saveOrUpdate(htlSupplierInfo);
    }
    
    public List<HtlBookSetup> queryHtlSupplierFax(HtlPriceType htlprice){
    	if(null==htlprice || null == htlprice.getSupplierID() || 0 == htlprice.getSupplierID())return Collections.EMPTY_LIST;
    	
    	HtlSupplierInfo htlSupplierInfo = this.querySupplierInfoByID(htlprice.getSupplierID());
    	if(null == htlSupplierInfo || 0 == htlSupplierInfo.getActive())return Collections.EMPTY_LIST;
    	
    	List<HtlSupplierBookSetup> lstBookSetup = htlSupplierInfo.getLstBookSetup();
    	if(lstBookSetup.isEmpty())return Collections.EMPTY_LIST;
    	
    	List<HtlSupplierBookSetup> htlSupplierBookList = new ArrayList<HtlSupplierBookSetup>();
    	for(HtlSupplierBookSetup htlSupplierBook : lstBookSetup){
    		if("1".equals(htlSupplierBook.getActive()) && "02".equals(htlSupplierBook.getBookctctType())){
    			htlSupplierBookList.add(htlSupplierBook);
    		}
    	}
    	if(htlSupplierBookList.isEmpty())return Collections.EMPTY_LIST;
    	
    	return changeObjetToHtlBookSetup(htlSupplierInfo.getAlias(),htlSupplierBookList);
    }
	
    private List<HtlBookSetup> changeObjetToHtlBookSetup(String alias,List<HtlSupplierBookSetup> htlSupplierBookList){
    	List<HtlBookSetup> htlBookSetupList= new ArrayList<HtlBookSetup>();
    	for(HtlSupplierBookSetup htlSupplierBookSetup : htlSupplierBookList){
    		HtlBookSetup htlBookSetup = new HtlBookSetup();
    		MyBeanUtil.copyProperties(htlBookSetup, htlSupplierBookSetup);
    		htlBookSetup.setBookChnName(alias+"（"+htlBookSetup.getBookChnName()+"）");
    		htlBookSetupList.add(htlBookSetup);
    	}
    	return htlBookSetupList;
    	
    }
	public GenericDAOHibernateImpl getGenericDAOHibernateImpl() {
		return genericDAOHibernateImpl;
	}
	
	public void setGenericDAOHibernateImpl(
			GenericDAOHibernateImpl genericDAOHibernateImpl) {
		this.genericDAOHibernateImpl = genericDAOHibernateImpl;
	}
}
