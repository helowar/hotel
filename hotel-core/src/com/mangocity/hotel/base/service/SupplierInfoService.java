package com.mangocity.hotel.base.service;

import java.util.List;

import com.mangocity.hotel.base.persistence.HtlBookSetup;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlSupplierInfo;

public interface SupplierInfoService {

	/**
     * 获得所有的供应商
     * @return
     */
    public List<HtlSupplierInfo> queryAllSupplierInfo();
    
	/**
     * 根据ID查找的供应商
     * @return
     */
    public HtlSupplierInfo querySupplierInfoByID(long ID);
    
	/**
     * 根据ID假删除供应商
     * @return
     */
    public void fakeDelSupplierInfo(long ID);
    
	/**
     * 根据ID删除供应商
     * @return
     */
    public void delSupplierInfo(long ID);
    
	/**
     * 更新供应商信息
     * @return
     */
    public void saveOrUpdateSupplierInfo(HtlSupplierInfo htlSupplierInfo);
    
    /**
     * 根据价格类型里保存的供应商id查询对应供应商传真
     * @param htlprice
     * @return
     */
    public List<HtlBookSetup> queryHtlSupplierFax(HtlPriceType htlprice);
	
}
