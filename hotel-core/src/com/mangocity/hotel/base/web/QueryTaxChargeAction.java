package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.base.dao.IContractDao;
import com.mangocity.hotel.base.manage.IPriceManage;
import com.mangocity.hotel.base.persistence.HtlHdlAddscope;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.HtlTaxCharge;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;

/**
 */
public class QueryTaxChargeAction extends PersistenceAction {

    private Long contractId;

    private IContractDao contractDao;

    private Long hotelId;

    private List lstTaxCharge = new ArrayList();

    private List lstRoomTaxCharge = new ArrayList();

    private List lstCommTaxCharge = new ArrayList();

    private String List = "listall";

    private String beDate;

    private String enDate;

    private boolean roomOrComm;

    private Long entityID;

    private IPriceManage priceManage;
    
    private HotelRoomTypeService hotelRoomTypeService;

    private List<HtlHdlAddscope> lsAddScope = new ArrayList<HtlHdlAddscope>();

    protected Class getEntityClass() {
        return HtlTaxCharge.class;
    }

    public String listAll() {

        if (null == contractId)
            super.forwardError("ContractId不能为空!");

        lstTaxCharge = contractDao.getTaxCharges(contractId);
        lsAddScope = priceManage.loadAddScope(hotelId);
        List lstRoomType = new ArrayList();
        List hrPrice = new ArrayList();

        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId);

        for (HtlHdlAddscope htlHdlAddscope: lsAddScope) {
            String priceType = htlHdlAddscope.getAllPriceTypeId();
            String priceNameAndRoomName = "";
            String priceTypeName = "";
            String[] priceTypeNumSplit = priceType.split(",");
            for (String priceTypeNum: priceTypeNumSplit) {
                for (int j = 0; j < lstRoomType.size(); j++) {
                    HtlRoomtype hr = (HtlRoomtype) lstRoomType.get(j);
                    hrPrice = hr.getLstPriceType();
                    String hrPriceToStrig = "";
                    for (int z = 0; z < hrPrice.size(); z++) {
                        if (null != hrPrice.get(z)) {
                            HtlPriceType htlPriceType = (HtlPriceType) hrPrice.get(z);
                            hrPriceToStrig = htlPriceType.getID().toString();
                            String hrRoomType = hr.getRoomName();
                            if (hrPriceToStrig.equals(priceTypeNum)) {
                                priceTypeName = htlPriceType.getPriceType();
                                priceNameAndRoomName += hrRoomType + "(" + priceTypeName + "),";
                            }
                        }
                    }
                }
            }
            htlHdlAddscope.setRoomNamePriceName(priceNameAndRoomName);
        }

        lstRoomTaxCharge = new ArrayList();
        lstCommTaxCharge = new ArrayList();
        if (null != lstTaxCharge) {
            for (int i = 0; i < lstTaxCharge.size(); i++) {
                HtlTaxCharge taxCharge = (HtlTaxCharge) lstTaxCharge.get(i);
                if (null != taxCharge.getRoomIncTax()) {
                    lstRoomTaxCharge.add(taxCharge);
                    continue;
                }
                lstCommTaxCharge.add(taxCharge);
            }
        }
        super.getParams().put("hotelId", hotelId);
        super.getParams().put("hotelID", hotelId);
        return List;
    }

    // add by kun.chen 2007-9-21 合同信息查看时用
    public String lstTaxCharge() {

        if (null == contractId)
            super.forwardError("ContractId不能为空!");
        lstTaxCharge = contractDao.getTaxCharges(contractId);
        lstRoomTaxCharge = new ArrayList();
        lstCommTaxCharge = new ArrayList();
        if (null != lstTaxCharge) {
            for (int i = 0; i < lstTaxCharge.size(); i++) {
                HtlTaxCharge taxCharge = (HtlTaxCharge) lstTaxCharge.get(i);
                if (null != taxCharge.getRoomIncTax()) {
                    lstRoomTaxCharge.add(taxCharge);
                    continue;
                }
                lstCommTaxCharge.add(taxCharge);
            }
        }
        super.getParams().put("hotelId", hotelId);
        super.getParams().put("hotelID", hotelId);
        return "lstTaxCharge";
    }

    // public String create() {
    // return super.create();
    // }

    public String delete() {
        super.setEntityID(entityID);
        return super.delete();
    }

    public String view() {
        super.setEntityID(entityID);
        super.setEntity(super.populateEntity());
        if (null == super.getEntity()) {
            String error = "找不到实体对象,id:" + super.getEntityID() + "; 类：" + getEntityClass();
            return super.forwardError(error);
        }
        super.setEntityForm(super.populateFormBean(super.getEntity()));
        super.getEntityForm();
        return VIEW;
    }

    public String save() {
        if (null != entityID) {
            super.setEntityID(entityID);
        }
        super.setEntity(super.populateEntity());
        HtlTaxCharge taxCharge = (HtlTaxCharge) super.getEntity();
        if (null == taxCharge.getRoomIncTax()) {
            if (taxCharge.getCommIncTax().equals("false")) {
                taxCharge.setCommIncTax("0");
            } else {
                taxCharge.setCommIncTax("1");
            }
        } else {
            if (taxCharge.getRoomIncTax().equals("false")) {
                taxCharge.setRoomIncTax("0");
            } else {
                taxCharge.setRoomIncTax("1");
            }
        }
        super.getEntityManager().saveOrUpdate(super.getEntity());
        return SAVE_SUCCESS;
    }

    public IContractDao getContractDao() {
        return contractDao;
    }

    public void setContractDao(IContractDao contractDao) {
        this.contractDao = contractDao;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractID) {
        this.contractId = contractID;
    }

    public String getList() {
        return List;
    }

    public void setList(String list) {
        List = list;
    }

    public List getLstCommTaxCharge() {
        return lstCommTaxCharge;
    }

    public void setLstCommTaxCharge(List lstCommTaxCharge) {
        this.lstCommTaxCharge = lstCommTaxCharge;
    }

    public List getLstRoomTaxCharge() {
        return lstRoomTaxCharge;
    }

    public void setLstRoomTaxCharge(List lstRoomTaxCharge) {
        this.lstRoomTaxCharge = lstRoomTaxCharge;
    }

    public List getLstTaxCharge() {
        return lstTaxCharge;
    }

    public void setLstTaxCharge(List lstTaxCharge) {
        this.lstTaxCharge = lstTaxCharge;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getBeDate() {
        return beDate;
    }

    public void setBeDate(String beDate) {
        this.beDate = beDate;
    }

    public String getEnDate() {
        return enDate;
    }

    public void setEnDate(String enDate) {
        this.enDate = enDate;
    }

    public boolean isRoomOrComm() {
        return roomOrComm;
    }

    public void setRoomOrComm(boolean roomOrComm) {
        this.roomOrComm = roomOrComm;
    }

    public Long getEntityID() {
        return entityID;
    }

    public void setEntityID(Long entityID) {
        this.entityID = entityID;
    }

    public IPriceManage getPriceManage() {
        return priceManage;
    }

    public void setPriceManage(IPriceManage priceManage) {
        this.priceManage = priceManage;
    }

    public List getLsAddScope() {
        return lsAddScope;
    }

    public void setLsAddScope(List lsAddScope) {
        this.lsAddScope = lsAddScope;
    }

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}

}
