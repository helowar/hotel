package com.mangocity.hotel.base.web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.dao.IContractDao;
import com.mangocity.hotel.base.persistence.HtlPresale;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.HtlSalesPromo;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.StringUtil;

/**
 */
public class SalePromoAction extends PersistenceAction {

    /**
     * 合同ID
     */
    private Long contractID;

    private IContractDao contractDao;
    
    private HotelRoomTypeService hotelRoomTypeService;

    /**
     * 促销列表
     */
    private List salePromos = new ArrayList();

    /**
     * 酒店ID
     */
    private long hotelID;

    private String sList = "list";

    private List lstRoomType;

    private List lstPresale;

    private String[] roomType;

    private Date beginDate;

    private Date endDate;

    // 删除时传的时间参数
    private String beDate;

    private String enDate;

    protected Class getEntityClass() {
        return HtlSalesPromo.class;
    }

    public String[] getRoomType() {
        return roomType;
    }

    public void setRoomType(String[] roomType) {
        this.roomType = roomType;
    }

    protected void prepare() {
        //

        if (null != roomType) {
            StringBuffer roomTypes = new StringBuffer();
            for (int i = 0; i < roomType.length; i++) {
                roomTypes.append(roomType[i]).append(",");
            }
            super.getParams().put("roomType", roomTypes.toString());
        }
    }

    public String create() {
        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelID);
        String result = super.create();
        return result;
    }

    public String view() {
        String result = super.view();
        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelID);
        return result;
    }

    public String save() {
        String result = null;
        try {
            result = super.save();
        } catch (SQLException e) {
        	log.error(e.getMessage(),e);
        }
        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelID);
        return result;
    }

    public String list() {

        if (null == contractID)
            super.forwardError("contractID不能为空!");

        salePromos = contractDao.getSalePromos(contractID);
        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelID);

        List hrPrice = new ArrayList();

        for (int i = 0; i < salePromos.size(); i++) {
            HtlSalesPromo hsp = (HtlSalesPromo) salePromos.get(i);
            String priceType = hsp.getRoomType();
            String priceNameAndRoomName = "";
            String priceTypeName = "";
            String[] priceTypeNumSplit = priceType.split(",");
            for (int k = 0; k < priceTypeNumSplit.length; k++) {
                for (int j = 0; j < lstRoomType.size(); j++) {
                    HtlRoomtype hr = (HtlRoomtype) lstRoomType.get(j);

                    hrPrice = hr.getLstPriceType();
                    String hrPriceToStrig = "";
                    for (int z = 0; z < hrPrice.size(); z++) {
                        if (null != hrPrice.get(z)) {
                            HtlPriceType htlPriceType = (HtlPriceType) hrPrice.get(z);
                            hrPriceToStrig = htlPriceType.getID().toString();
                            String hrRoomType = hr.getRoomName();
                            if (hrPriceToStrig.equals(priceTypeNumSplit[k])) {
                                priceTypeName = htlPriceType.getPriceType();
                                priceNameAndRoomName += hrRoomType + "(" + priceTypeName + "),";
                            }
                        }
                    }
                }

            }
            hsp.setRoomTypeName(priceNameAndRoomName);
        }

        super.getParams().put("hotelID", hotelID);
        return sList;
    }

    // 查看合同信息时需要查看这些信息 add begin----by kun.chen 2007-9-21----
    public String listSalePromo() {

        if (null == contractID)
            super.forwardError("contractID不能为空!");

        salePromos = contractDao.getSalePromos(contractID);

        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelID);

        List hrPrice = new ArrayList();
        for (int i = 0; i < salePromos.size(); i++) {
            HtlSalesPromo hsp = (HtlSalesPromo) salePromos.get(i);
            String priceType = hsp.getRoomType();
            String priceNameAndRoomName = "";
            String priceTypeName = "";
            String[] priceTypeNumSplit = priceType.split(",");
            for (int k = 0; k < priceTypeNumSplit.length; k++) {
                for (int j = 0; j < lstRoomType.size(); j++) {
                    HtlRoomtype hr = (HtlRoomtype) lstRoomType.get(j);
                    hrPrice = hr.getLstPriceType();
                    String hrPriceToStrig = "";
                    for (int z = 0; z < hrPrice.size(); z++) {
                        if (null != hrPrice.get(z)) {
                            HtlPriceType htlPriceType = (HtlPriceType) hrPrice.get(z);
                            hrPriceToStrig = htlPriceType.getID().toString();
                            String hrRoomType = hr.getRoomName();
                            if (hrPriceToStrig.equals(priceTypeNumSplit[k])) {
                                priceTypeName = htlPriceType.getPriceType();
                                priceNameAndRoomName += hrRoomType + "(" + priceTypeName + "),";
                            }
                        }
                    }
                }

            }
            hsp.setRoomTypeName(priceNameAndRoomName);
        }

        super.getParams().put("hotelID", hotelID);
        // ---根据酒店ID,查询芒果网促销信息
        lstPresale = contractDao.getPresaleList(hotelID);
        return "listSalePromo";
    }

    // 查看预订单信息时需要查看这些信息 add begin----by kun.chen 2007-10-19----
    public String listPreOrderSalePromo() {
        // 业务规则:
        // 1.属于该酒店的促销信息,包括酒店促销和芒果网促销
        // 2.入住酒店时间只要有一天落在促销时间内就算

        // --根据酒店ID和入住时间查询酒店促销信息
        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelID);
        salePromos = contractDao.getPreOrderSalePromos(hotelID, beginDate, endDate);
        List hrPrice = new ArrayList();
        for (int i = 0; i < salePromos.size(); i++) {
            HtlSalesPromo sale = (HtlSalesPromo) salePromos.get(i);
            String str = sale.getSalePromoCont();
            if (!StringUtil.isValidStr(str))
                continue;
            String temp = new String();
            temp = str.replace("\r\n", "");
            sale.setSalePromoCont(temp);
            String priceType = sale.getRoomType();
            String priceNameAndRoomName = "";
            String priceTypeName = "";
            String[] priceTypeNumSplit = priceType.split(",");
            for (int k = 0; k < priceTypeNumSplit.length; k++) {
                for (int j = 0; j < lstRoomType.size(); j++) {
                    HtlRoomtype hr = (HtlRoomtype) lstRoomType.get(j);
                    hrPrice = hr.getLstPriceType();
                    String hrPriceToStrig = "";
                    for (int z = 0; z < hrPrice.size(); z++) {
                        if (null != hrPrice.get(z)) {
                            HtlPriceType htlPriceType = (HtlPriceType) hrPrice.get(z);
                            hrPriceToStrig = htlPriceType.getID().toString();
                            String hrRoomType = hr.getRoomName();
                            if (hrPriceToStrig.equals(priceTypeNumSplit[k])) {
                                priceTypeName = htlPriceType.getPriceType();
                                priceNameAndRoomName += hrRoomType + "(" + priceTypeName + "),";
                            }
                        }
                    }
                }

            }
            sale.setRoomTypeName(priceNameAndRoomName);
        }
        // ---根据酒店ID和入住时间,查询芒果网促销信息
        lstPresale = contractDao.getPreOrderPresaleList(hotelID, beginDate, endDate);
        for (int j = 0; j < lstPresale.size(); j++) {
            HtlPresale presale = (HtlPresale) lstPresale.get(j);
            String str = presale.getPresaleContent();
            if (!StringUtil.isValidStr(str))
                continue;
            String temp = new String();
            temp = str.replace("\r\n", "");
            presale.setPresaleContent(temp);
        }
        return "listSalePromo";
    }

    public String viewSalePromo() {
    	super.view();
        HtlSalesPromo sale = (HtlSalesPromo) super.getEntity();
        String str = sale.getSalePromoCont();
        String temp = new String();
        if (StringUtil.isValidStr(str)) {
            temp = str.replace("\r\n", "");
        }
        sale.setSalePromoCont(temp);
        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelID);
        return "viewSalePromo";
    }

    // ---and end---------
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

    public List getSalePromos() {
        return salePromos;
    }

    public void setSalePromos(List salePromos) {
        this.salePromos = salePromos;
    }

    public long getHotelID() {
        return hotelID;
    }

    public void setHotelID(long hotelID) {
        this.hotelID = hotelID;
    }

    public List getLstRoomType() {
        return lstRoomType;
    }

    public void setLstRoomType(List lstRoomType) {
        this.lstRoomType = lstRoomType;
    }

    public List getLstPresale() {
        return lstPresale;
    }

    public void setLstPresale(List lstPresale) {
        this.lstPresale = lstPresale;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}

}
