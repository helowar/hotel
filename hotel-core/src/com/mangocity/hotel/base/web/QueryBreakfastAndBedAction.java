package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.base.dao.IContractDao;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;

/**
 */
public class QueryBreakfastAndBedAction extends PersistenceAction {
    private Long contractID;

    private IContractDao contractDao;

    private Long hotelId;

    private List lstBreakfast = new ArrayList();

    private List lstAddBed = new ArrayList();

    private List lstWelcomePrice = new ArrayList();

    private List lstInfo = new ArrayList();

    private String List = "list";

    protected Class getEntityClass() {
        return HtlRoomtype.class;
    }

    public String list() {

        if (null == contractID)
            super.forwardError("ContractID不能为空!");

        lstBreakfast = contractDao.getBreakfast(contractID);
        lstAddBed = contractDao.getAddBedPrice(contractID);

        lstWelcomePrice = contractDao.getWelcomePrice(contractID);
        // lstInfo.add(0,lstBreakfast);
        // lstInfo.add(1,lstAddBed);
        // lstInfo.add(2,lstWelcomePrice);
        super.getParams().put("hotelId", hotelId);
        super.getParams().put("hotelID", hotelId);

        return List;
    }

    // add by kun.chen 2007-9-21 合同信息查看时用
    public String listBreakfastAndBed() {

        if (null == contractID)
            super.forwardError("ContractID不能为空!");

        lstBreakfast = contractDao.getBreakfast(contractID);
        lstAddBed = contractDao.getAddBedPrice(contractID);

        lstWelcomePrice = contractDao.getWelcomePrice(contractID);
        lstInfo.add(0, lstBreakfast);
        lstInfo.add(1, lstAddBed);
        lstInfo.add(2, lstWelcomePrice);

        return "listBreakfastAndBed";
    }

    public IContractDao getContractDao() {
        return contractDao;
    }

    public void setContractDao(IContractDao contractDao) {
        this.contractDao = contractDao;
    }

    public Long getContractID() {
        return contractID;
    }

    public void setContractID(Long contractID) {
        this.contractID = contractID;
    }

    public String getList() {
        return List;
    }

    public void setList(String list) {
        List = list;
    }

    public List getLstInfo() {
        return lstInfo;
    }

    public void setLstInfo(List lstInfo) {
        this.lstInfo = lstInfo;
    }

    public List getLstAddBed() {
        return lstAddBed;
    }

    public void setLstAddBed(List lstAddBed) {
        this.lstAddBed = lstAddBed;
    }

    public List getLstBreakfast() {
        return lstBreakfast;
    }

    public void setLstBreakfast(List lstBreakfast) {
        this.lstBreakfast = lstBreakfast;
    }

    public List getLstWelcomePrice() {
        return lstWelcomePrice;
    }

    public void setLstWelcomePrice(List lstWelcomePrice) {
        this.lstWelcomePrice = lstWelcomePrice;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

}
