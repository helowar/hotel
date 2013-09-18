package com.mangocity.hotel.base.web;

import java.util.List;

import com.mangocity.hotel.base.dao.IContractDao;
import com.mangocity.hotel.base.manage.ChangePriceWarningManage;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlChangePriceWarning;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.bean.MyBeanUtil;

/**
 */
public class ChangePriceWarningAction extends PersistenceAction {

    private long hotelID;

    private String hotelName;

    private int exhibitNum;

    private HotelManage hotelManage;

    private HtlHotel htlHotel;

    private ChangePriceWarningManage changePriceWarningManage;

    private String BATCH_SAVE = "batchSave";

    private List lstChangePriceResult;

    private IContractDao contractDao;

    public List getLstChangePriceResult() {
        return lstChangePriceResult;
    }

    public void setLstChangePriceResult(List lstChangePriceResult) {
        this.lstChangePriceResult = lstChangePriceResult;
    }

    protected Class getEntityClass() {
        return HtlChangePriceWarning.class;
    }

    public String batchSave() {
    	log.info("exhibitNum==" + exhibitNum);
        List ls = MyBeanUtil.getBatchObjectFromParam(super.getParams(),
            HtlChangePriceWarning.class, exhibitNum);

        // 删除该酒店下的警告信息,再重新录入
        List lst = contractDao.getChangePriceWarningList(Long.valueOf(hotelID));
        log.info("lst======" + lst.size());
        for (int i = 0; i < lst.size(); i++) {
            HtlChangePriceWarning obj = (HtlChangePriceWarning) lst.get(i);
            contractDao.delChangePriceWarning(obj);
        }
        for (int k = 0; k < ls.size(); k++) {
            HtlChangePriceWarning changePriceWarning = (HtlChangePriceWarning) ls.get(k);

            // changePriceWarning.setID(new Long(0));
            changePriceWarning.setHotelId(hotelID);

            // 保存数据库

            super.getEntityManager().save(changePriceWarning);

        }
        // super.getEntityManager().saveOrUpdateAll(ls);
        return BATCH_SAVE;

    }

    /*
     * 跳到变价预警页面
     */
    public String toChangePriceWarning() {
        hotelName = hotelManage.findHotel(hotelID).getChnName();
        lstChangePriceResult = changePriceWarningManage.findPriceDate(hotelID);
        String TCPW = "TCPW";
        return TCPW;
    }

    public Long getHotelID() {
        return hotelID;
    }

    public void setHotelID(Long hotelID) {
        this.hotelID = hotelID;
    }

    public String getBATCH_SAVE() {
        return BATCH_SAVE;
    }

    public void setBATCH_SAVE(String batch_save) {
        BATCH_SAVE = batch_save;
    }

    public int getExhibitNum() {
        return exhibitNum;
    }

    public void setExhibitNum(int exhibitNum) {
        this.exhibitNum = exhibitNum;
    }

    public void setHotelID(long hotelID) {
        this.hotelID = hotelID;
    }

    public ChangePriceWarningManage getChangePriceWarningManage() {
        return changePriceWarningManage;
    }

    public void setChangePriceWarningManage(ChangePriceWarningManage changePriceWarningManage) {
        this.changePriceWarningManage = changePriceWarningManage;
    }

    public IContractDao getContractDao() {
        return contractDao;
    }

    public void setContractDao(IContractDao contractDao) {
        this.contractDao = contractDao;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public HtlHotel getHtlHotel() {
        return htlHotel;
    }

    public void setHtlHotel(HtlHotel htlHotel) {
        this.htlHotel = htlHotel;
    }

}
