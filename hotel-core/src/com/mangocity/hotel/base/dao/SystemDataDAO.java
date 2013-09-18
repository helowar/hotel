package com.mangocity.hotel.base.dao;

import java.util.List;

import com.mangocity.framework.base.model.Tree;
import com.mangocity.hotel.base.persistence.HtlArea;
import com.mangocity.hotel.base.persistence.HtlExchange;
import com.mangocity.hotel.base.persistence.OrParam;

public interface SystemDataDAO
{
	
	/**
     * 根据城市代码查询区域
     * 
     * @param	cityCode
     * @return	区域
     */
    public HtlArea queryAreaCode(String cityCode);
    
    /**
     * 查询以某种货币为基准的其他货币汇率
     * 
     * @return 汇率记录列表
     */
    public List<HtlExchange> getExchangeRateBasedOnCurrency(String currency);
    
    /**
     * 根据参数名查询系统参数值
     * 
     * @param paramName	系统参数名
     * @return	系统参数对象
     */
    public OrParam querySysParamByName(String paramName);
    
    /**
     * 更新系统参数
     * 
     * @param sysParam
     */
    public void updateSysParamByName(OrParam sysParam);
    
    /**
     * 获取CDM数据
     * 
     * @param path
     * @return
     */
    public Tree qryCDMDataByPath(String path);
    
    /**
     * 查询需要更新推荐级别的酒店ID
     * @return
     */
    public List<String> queryHotelIDforUpd();

}
