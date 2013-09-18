package com.mangocity.hotel.base.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.mangocity.hotel.base.persistence.CEntityEvent;
import com.mangocity.hotel.base.persistence.HtlSupplierBookSetup;
import com.mangocity.hotel.base.persistence.HtlSupplierCtct;
import com.mangocity.hotel.base.persistence.HtlSupplierInfo;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.base.service.SupplierInfoService;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.web.webwork.GenericAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.exception.ClassInstantiateException;

public class SupplierInfoAction extends GenericAction  implements Serializable{
	     
    private String supplierInfoId;
    
    private String supplierName;
    
    private String active;
    
    private HtlSupplierInfo htlSupplierInfo;
    
    private int lsBookSetupNum;
    
    private int lsCtctNum;
    
    private String bookSetupID;
    
    private SupplierInfoService supplierInfoService;
    
    private ResourceManager resourceManager;
    
    private Map<String,String> depts;
    
    public String updateSupplierInfo(){
    	if (null != supplierInfoId) {
			htlSupplierInfo = supplierInfoService.querySupplierInfoByID(Long.valueOf(supplierInfoId));
			if(null != htlSupplierInfo && null != htlSupplierInfo.getLstBookSetup()){
				lsBookSetupNum = htlSupplierInfo.getLstBookSetup().size();
			}	
			if(null != htlSupplierInfo && null != htlSupplierInfo.getLstCtct()){
				lsCtctNum = htlSupplierInfo.getLstCtct().size();
			}	
		}else{
			return forwardError("供应商ID号不能为空！");
		}
    	return "update";
    }
    
    @SuppressWarnings("unchecked")
	public String saveOrUpdate(){
    	try {
        	Map params = super.getParams();
        	if(null == supplierInfoId){
        		htlSupplierInfo = new HtlSupplierInfo();
        	}else{
        		htlSupplierInfo = supplierInfoService.querySupplierInfoByID(Long.parseLong(supplierInfoId));
        	}

			BeanUtils.copyProperties(htlSupplierInfo, params);
			
	        List lsCtct = MyBeanUtil.getBatchObjectFromParam(params, HtlSupplierCtct.class, lsCtctNum);
	        List lsCtctTemp = new ArrayList();
	        for (int i = 0; i < lsCtct.size(); i++) {
	        	HtlSupplierCtct ct = (HtlSupplierCtct) lsCtct.get(i);
	            if ((null == ct.getCtctType() ||  ct.getCtctType().trim().equals(""))
	            		&& (null == ct.getCtctchnName() ||  ct.getCtctchnName().trim().equals(""))) {
	                continue;
	            }
	            ct.setHtlSupplierInfo(htlSupplierInfo);
	            lsCtctTemp.add(ct);
	        }
	        
	        if (null != super.getOnlineRoleUser()) {
	        	lsCtctTemp = CEntityEvent.setCEntity(lsCtctTemp, super.getOnlineRoleUser().getName(), super
	                .getOnlineRoleUser().getLoginName());
	        }
			
			List<HtlSupplierBookSetup> lsSupplierBookSetup = MyBeanUtil.getBatchObjectFromParam(params, HtlSupplierBookSetup.class, lsBookSetupNum);
			List<HtlSupplierBookSetup> lsBookSetupTemp = new ArrayList<HtlSupplierBookSetup>();
			for(HtlSupplierBookSetup bs : (List<HtlSupplierBookSetup>)lsSupplierBookSetup){
				bs.setSupplierInfo(htlSupplierInfo);
	            if (null == bs.getBookChnName() || bs.getBookChnName().trim().equals("")) {
	                continue;
	            }
	            new BizRuleCheck();
	            bs.setActive(BizRuleCheck.getTrueString());
	            if (null == bs.getID() || 0 == bs.getID()) {
	                if (null != super.getOnlineRoleUser()) {
	                    bs.setCreateById(super.getOnlineRoleUser().getLoginName());
	                    bs.setCreateBy(super.getOnlineRoleUser().getName());
	                }
	                bs.setCreateTime(DateUtil.getSystemDate());
	            }
	            if (null != super.getOnlineRoleUser()) {
	                bs.setModifyBy(super.getOnlineRoleUser().getName());
	                bs.setModifyById(super.getOnlineRoleUser().getLoginName());
	                bs.setModifyTime(DateUtil.getSystemDate());
	            }
	            lsBookSetupTemp.add(bs);
			}
			htlSupplierInfo.setLstCtct(lsCtctTemp);
			htlSupplierInfo.setLstBookSetup(lsBookSetupTemp);
			supplierInfoService.saveOrUpdateSupplierInfo(htlSupplierInfo);
			
		} catch (Exception e) {
			throw new ClassInstantiateException(e);
		}   	
    	return SUCCESS;
    }
    
    public String delSupplierInfo(){ 
    	if (null != supplierInfoId) {
			supplierInfoService.fakeDelSupplierInfo(Long.parseLong(supplierInfoId));
		} else {
			return forwardError("供应商ID号不能为空！");
		}
    	return SUCCESS;
    }
    
    public String addSupplierInfo(){
    	return "add";
    }
    
    @SuppressWarnings("unchecked")
	public String viewSupplierInfo(){
    	//从CDM读取部门设置的map用于显示
    	depts = resourceManager.getDescription("select_dept");
    	if(null == depts){
    		depts = new HashMap<String,String>();
    	}
    	if (null != supplierInfoId) {
    		htlSupplierInfo = supplierInfoService.querySupplierInfoByID(Long.parseLong(supplierInfoId));
    		
		} else {
			return forwardError("供应商ID号不能为空！");
		}
    	return "view";
    }

	public SupplierInfoService getSupplierInfoService() {
		return supplierInfoService;
	}

	public void setSupplierInfoService(SupplierInfoService supplierInfoService) {
		this.supplierInfoService = supplierInfoService;
	}

	public String getSupplierInfoId() {
		return supplierInfoId;
	}

	public void setSupplierInfoId(String supplierInfoId) {
		this.supplierInfoId = supplierInfoId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public int getLsBookSetupNum() {
		return lsBookSetupNum;
	}

	public void setLsBookSetupNum(int lsBookSetupNum) {
		this.lsBookSetupNum = lsBookSetupNum;
	}

	public int getLsCtctNum() {
		return lsCtctNum;
	}

	public void setLsCtctNum(int lsCtctNum) {
		this.lsCtctNum = lsCtctNum;
	}

	public HtlSupplierInfo getHtlSupplierInfo() {
		return htlSupplierInfo;
	}

	public void setHtlSupplierInfo(HtlSupplierInfo htlSupplierInfo) {
		this.htlSupplierInfo = htlSupplierInfo;
	}

	public String getBookSetupID() {
		return bookSetupID;
	}

	public void setBookSetupID(String bookSetupID) {
		this.bookSetupID = bookSetupID;
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	public Map<String, String> getDepts() {
		return depts;
	}

	public void setDepts(Map<String, String> depts) {
		this.depts = depts;
	}
}
