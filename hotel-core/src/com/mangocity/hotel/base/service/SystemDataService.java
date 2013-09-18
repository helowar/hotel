package com.mangocity.hotel.base.service;

import java.util.List;

import com.mangocity.framework.base.model.Tree;
import com.mangocity.hotel.base.persistence.HtlArea;
import com.mangocity.hotel.base.persistence.OrParam;

public interface SystemDataService 
{
	
	/**
     * 根据城市代码查询区域
     * 
     * @param	cityCode
     * @return	HtlArea
     */
    public HtlArea queryAreaCode(String cityCode);
    
    /**
     * 根据参数名获取系统参数值
     * 
     * @param paramName	系统参数名
     * @return	系统参数对象
     */
    public OrParam getSysParamByName(String paramName);
    
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
    public Tree getParamsByPath(String path);
    
    /**
     * 查询需要更新推荐级别信息的酒店ID
     * @return
     */
    public List<String> queryID();

}
