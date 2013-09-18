package com.mangocity.hotel.base.web;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.mangocity.hotel.base.dao.IContractDao;
import com.mangocity.hotel.base.dao.ISaleDao;
import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.persistence.CEntityEvent;
import com.mangocity.hotel.base.persistence.HtlAssureCardItem;
import com.mangocity.hotel.base.persistence.HtlAssureItem;
import com.mangocity.hotel.base.persistence.HtlCreditAssure;
import com.mangocity.hotel.base.persistence.HtlCreditAssureDate;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.DateSegment;
import com.mangocity.util.bean.MustDate;
import com.mangocity.util.bean.MyBeanUtil;

/**
 */
public class CreditCardAction extends PersistenceAction {

    private Long contractId;

    private long hotelId;

    private String addfor;

    private String forward;
    
    private HotelRoomTypeService hotelRoomTypeService;

    private IContractDao contractDao;

    private List htlAssureItem;

    private int htlAssureItemNum;

    private String[] roomTypes;

    private int rowNum;

    private int fengrowNum;

    private int assureCardNum;

    private int dateNum;

    private Long roomTypeId;

    private String creditDate;

    private HtlCreditAssure htlCreditAssure;

    private List htlCreditAssureDateLis = new ArrayList();

    private List htlAssureCardItemLis = new ArrayList();

    private List lsmustDate = new ArrayList();

    private List lstRoomType = new ArrayList();

    private List lstCreditAssure = new ArrayList();

    /**
     * 在增加或修改预定条款信息时传入合同开始日期
     */
    private String beDate;

    /**
     * 在增加或修改预定条款信息时传入合同的结束日期
     */
    private String enDate;

    private ISaleDao saleDao;

    private ContractManage contractManage;

    private static final long serialVersionUID = -2292609240480807081L;

    protected Class getEntityClass() {
        return HtlCreditAssure.class;
    }

    public ContractManage getContractManage() {
        return contractManage;
    }

    public void setContractManage(ContractManage contractManage) {
        this.contractManage = contractManage;
    }

    public String view() {

        super.view();
        HtlCreditAssure htlCreditAssure = (HtlCreditAssure) super.getEntity();
        htlAssureItem = htlCreditAssure.getHtlAssureItem();
        htlAssureItemNum = htlAssureItem.size();
        htlCreditAssureDateLis = htlCreditAssure.getHtlCreditAssureDate();
        fengrowNum = htlCreditAssureDateLis.size();
        htlAssureCardItemLis = htlCreditAssure.getHtlAssureCardItem();
        assureCardNum = htlAssureCardItemLis.size();
        String mustDateStr = htlCreditAssure.getMustDate();
        if (null != mustDateStr && 0 < mustDateStr.length()) {
            String[] strTemp = mustDateStr.split(",");
            dateNum = strTemp.length;
            for (int i = 0; i < strTemp.length; i++) {
                MustDate mustDate = new MustDate();
                mustDate.setMustDateT(DateUtil.getDate(strTemp[i]));
                lsmustDate.add(mustDate);
            }
        }

        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId);
        return VIEW;

    }

    public String viewCreditCard() {

        super.view();
        htlCreditAssure = (HtlCreditAssure) super.getEntity();
        htlAssureItem = htlCreditAssure.getHtlAssureItem();
        htlAssureItemNum = htlAssureItem.size();
        htlCreditAssureDateLis = htlCreditAssure.getHtlCreditAssureDate();
        fengrowNum = htlCreditAssureDateLis.size();
        htlAssureCardItemLis = htlCreditAssure.getHtlAssureCardItem();
        assureCardNum = htlAssureCardItemLis.size();
        String mustDateStr = htlCreditAssure.getMustDate();
        if (null != mustDateStr && 0 < mustDateStr.length()) {
            String[] strTemp = mustDateStr.split(",");
            dateNum = strTemp.length;
            for (int i = 0; i < strTemp.length; i++) {
                MustDate mustDate = new MustDate();
                mustDate.setMustDateT(DateUtil.getDate(strTemp[i]));
                lsmustDate.add(mustDate);
            }
        }
        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId);
        // 根据预定信息里的价格类型去对应
        return "viewCreditCard";
    }

    public String show() {

        // 先求预定条款数
        List creditList = new ArrayList();
        creditList = contractDao.getCreditAssure(Long.valueOf(hotelId), java.sql.Date
            .valueOf(creditDate));
        // 根据房型求价格类型
        List priceList = new ArrayList();
        priceList = contractDao.getPriceList(roomTypeId);
        // 过滤---预定条款里的价格类型要包含在所查出的价格类型里面
        if (null != creditList) {

            int flag = 0;
            StringTokenizer tokenizer;
            for (int i = 0; i < creditList.size(); i++) {
                // 得到价格类型
                HtlCreditAssure creditAssure = (HtlCreditAssure) creditList.get(i);
                htlCreditAssure = (HtlCreditAssure) creditList.get(i);
                String strPrice = creditAssure.getRoomType();

                tokenizer = new StringTokenizer(strPrice, ",");
                flag = 0;
                while (tokenizer.hasMoreTokens()) {
                    String priceType = tokenizer.nextToken();
                    // 比较价格类型是否包含在价格类型列表中
                    if (0 == flag) {
                        for (int j = 0; j < priceList.size(); j++) {
                            HtlPriceType htlPrice = (HtlPriceType) priceList.get(j);
                            if (priceType.equals(htlPrice.getPriceType())) {
                                lstCreditAssure.add(creditAssure);
                                flag = 1;
                            }
                        }
                    }

                }
            }
        }

        return "show";
    }

    public String delete() {

        super.delete();

        forward = "recontract";

        return forward;
    }

    public String create() {
        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId);

        forward = "create";

        return forward;
    }

    public String edit() {
        super.setEntity(super.populateEntity());
        HtlCreditAssure htlCreditAssure;
        if (null != super.getEntity()) {
            htlCreditAssure = (HtlCreditAssure) super.getEntity();
        } else {
            htlCreditAssure = (HtlCreditAssure) super.populateEntity();
        }

        Map params = super.getParams();
        // 预付取消条款
        List lisAssureItem = MyBeanUtil.getBatchObjectFromParam(params, HtlAssureItem.class,
            htlAssureItemNum);
        List lisCreditAssureDate = MyBeanUtil.getBatchObjectFromParam(params,
            HtlCreditAssureDate.class, fengrowNum);
        // 信用卡担保取消条款
        List htlAssureCardItemLis = MyBeanUtil.getBatchObjectFromParam(params,
            HtlAssureCardItem.class, assureCardNum);
        List mustDateLis = MyBeanUtil.getBatchObjectFromParam(params, MustDate.class, dateNum);

        String mustDateStr = "";
        for (int i = 0; i < mustDateLis.size(); i++) {
            MustDate dateTemp = (MustDate) mustDateLis.get(i);
            String strDate = DateUtil.dateToString(dateTemp.getMustDateT());
            mustDateStr += strDate + ",";
        }

        htlCreditAssure.setMustDate(mustDateStr);

        if (htlCreditAssure.getTimeLimitType().equals("0")) {
            htlCreditAssure.setTimeLimit(htlCreditAssure.getTimeLimitDate());
        } else if (htlCreditAssure.getTimeLimitType().equals("2")) {
            htlCreditAssure.setTimeLimit(String.valueOf(htlCreditAssure.getTimeLimitDateNum()));
        } else {
            htlCreditAssure.setTimeLimit(String.valueOf(htlCreditAssure.getTimeLimitNum()));
        }

        String sc = "";
        StringBuffer sb = new StringBuffer();
        String[] str = null;
        if (null != roomTypes) {
            for (int i = 0; i < roomTypes.length; i++) {
                if (i == roomTypes.length - 1) {
                    sb.append(roomTypes[i]);
                } else {
                    sb.append(roomTypes[i]).append(",");
                }
                str = roomTypes[i].split(",");
            }

            sc = sb.toString();
        }
        htlCreditAssure.setRoomType(sc);
        if (null != super.getOnlineRoleUser())
            htlCreditAssure.setCreator(super.getOnlineRoleUser().getName());
        if (null != super.getOnlineRoleUser() && null != super.getOnlineRoleUser().getLoginName())
            htlCreditAssure.setModifyBy(super.getOnlineRoleUser().getName());
        htlCreditAssure.setCreateTime(DateUtil.getSystemDate());
        htlCreditAssure.setModifyTime(new Date());

        if ("addCreditCard".equals(addfor)) {
            Map paramsDate = super.getParams();
            List ls = MyBeanUtil.getBatchObjectFromParam(paramsDate, DateSegment.class, rowNum);

            for (int i = 0; i < ls.size(); i++) {
                DateSegment ds = new DateSegment();
                ds = (DateSegment) ls.get(i);
                htlCreditAssure.setBeginDate(ds.getStart());
                htlCreditAssure.setEndDate(ds.getEnd());
                htlCreditAssure.setID(Long.valueOf(0));

                List lishtlAssureItem1 = new ArrayList();
                List lishtlCreditAssureDate1 = new ArrayList();
                List lishtlAssureCardItem1 = new ArrayList();

                for (int j = 0; j < lisAssureItem.size(); j++) {
                    HtlAssureItem htlAssureItem1 = (HtlAssureItem) lisAssureItem.get(j);
                    htlAssureItem1.setAssureItemID(Long.valueOf(0));
                    htlAssureItem1.setHtlCreditAssure(htlCreditAssure);
                    lishtlAssureItem1.add(htlAssureItem1);
                }
                // --
                if (null != super.getOnlineRoleUser()) {
                    lishtlAssureItem1 = CEntityEvent.setCEntity(lishtlAssureItem1, super
                        .getOnlineRoleUser().getName(), super.getOnlineRoleUser().getLoginName());
                }
                htlCreditAssure.setHtlAssureItem(lishtlAssureItem1);
                for (int j = 0; j < lisCreditAssureDate.size(); j++) {
                    HtlCreditAssureDate htlCreditAssureDate1 = 
                        (HtlCreditAssureDate) lisCreditAssureDate
                        .get(j);
                    htlCreditAssureDate1.setCreditAssureDateID(Long.valueOf(0));
                    htlCreditAssureDate1.setHtlCreditAssure(htlCreditAssure);
                    lishtlCreditAssureDate1.add(htlCreditAssureDate1);
                }
                // --
                if (null != super.getOnlineRoleUser()) {
                    lishtlCreditAssureDate1 = CEntityEvent.setCEntity(lishtlCreditAssureDate1,
                        super.getOnlineRoleUser().getName(), super.getOnlineRoleUser()
                            .getLoginName());
                }
                htlCreditAssure.setHtlCreditAssureDate(lishtlCreditAssureDate1);
                for (int j = 0; j < htlAssureCardItemLis.size(); j++) {
                    HtlAssureCardItem htlAssureCardItem1 = (HtlAssureCardItem) htlAssureCardItemLis
                        .get(j);
                    htlAssureCardItem1.setAssureCardItemID(Long.valueOf(0));
                    htlAssureCardItem1.setHtlCreditAssure(htlCreditAssure);
                    lishtlAssureCardItem1.add(htlAssureCardItem1);
                }
                // --
                if (null != super.getOnlineRoleUser()) {
                    lishtlAssureCardItem1 = CEntityEvent.setCEntity(lishtlAssureCardItem1, super
                        .getOnlineRoleUser().getName(), super.getOnlineRoleUser().getLoginName());
                }
                htlCreditAssure.setHtlAssureCardItem(lishtlAssureCardItem1);
                // ---
                if (null != super.getOnlineRoleUser()) {
                    htlCreditAssure = (HtlCreditAssure) CEntityEvent.setCEntity(htlCreditAssure,
                        super.getOnlineRoleUser().getName(), super.getOnlineRoleUser()
                            .getLoginName());
                }

                // Modified by Wuyun 2008-4-4
                // super.getEntityManager().saveOrUpdate(htlCreditAssure);
                try {
                    contractManage.createCreditAssure(htlCreditAssure);
                } catch (IllegalAccessException e) {
                	log.error(e.getMessage(),e);
                } catch (InvocationTargetException e) {
                	log.error(e.getMessage(),e);
                }

                // 如果选择了担保条件的超时担保，则需要将担保时间写入房间表 ,modify by zhineng.zhuang

                if ("01".equals(htlCreditAssure.getAssureConditions())) {
                    saleDao.updateRoomLastAssureTime(ds.getStart(), ds.getEnd(), str, hotelId,
                        htlCreditAssure.getLastAssureTime());
                }
            }
        } else {
            // --
            if (null != super.getOnlineRoleUser()) {
                lisAssureItem = CEntityEvent.setCEntity(lisAssureItem, super.getOnlineRoleUser()
                    .getName(), super.getOnlineRoleUser().getLoginName());

                htlCreditAssure.setHtlAssureItem(lisAssureItem);
                // --
                lisCreditAssureDate = CEntityEvent.setCEntity(lisCreditAssureDate, super
                    .getOnlineRoleUser().getName(), super.getOnlineRoleUser().getLoginName());

                htlCreditAssure.setHtlCreditAssureDate(lisCreditAssureDate);
                // --
                htlAssureCardItemLis = CEntityEvent.setCEntity(htlAssureCardItemLis, super
                    .getOnlineRoleUser().getName(), super.getOnlineRoleUser().getLoginName());

                htlCreditAssure.setHtlAssureCardItem(htlAssureCardItemLis);

                // ---
                htlCreditAssure = (HtlCreditAssure) CEntityEvent.setCEntity(htlCreditAssure, super
                    .getOnlineRoleUser().getName(), super.getOnlineRoleUser().getLoginName());
            }
            super.setEntity(htlCreditAssure);

            // Modified by Wuyun 2008-4-4
            // super.edit();
            try {
                contractManage.modifyCreditAssure(htlCreditAssure);
            } catch (IllegalAccessException e) {
            	log.error(e.getMessage(),e);
            } catch (InvocationTargetException e) {
            	log.error(e.getMessage(),e);
            }
            if ("01".equals(htlCreditAssure.getAssureConditions())) {
                saleDao.updateRoomLastAssureTime(htlCreditAssure.getBeginDate(), htlCreditAssure
                    .getEndDate(), str, hotelId, htlCreditAssure.getLastAssureTime());
            }
        }

        if ("recontract".equals(forward)) {

            return forward;
        }

        return SAVE_SUCCESS;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public List getHtlAssureItem() {
        return htlAssureItem;
    }

    public void setHtlAssureItem(List htlAssureItem) {
        this.htlAssureItem = htlAssureItem;
    }

    public int getHtlAssureItemNum() {
        return htlAssureItemNum;
    }

    public void setHtlAssureItemNum(int htlAssureItemNum) {
        this.htlAssureItemNum = htlAssureItemNum;
    }

    public String[] getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(String[] roomTypes) {
        this.roomTypes = roomTypes;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public String getAddfor() {
        return addfor;
    }

    public void setAddfor(String addfor) {
        this.addfor = addfor;
    }

    public List getHtlAssureCardItemLis() {
        return htlAssureCardItemLis;
    }

    public void setHtlAssureCardItemLis(List htlAssureCardItemLis) {
        this.htlAssureCardItemLis = htlAssureCardItemLis;
    }

    public List getHtlCreditAssureDateLis() {
        return htlCreditAssureDateLis;
    }

    public void setHtlCreditAssureDateLis(List htlCreditAssureDateLis) {
        this.htlCreditAssureDateLis = htlCreditAssureDateLis;
    }

    public int getFengrowNum() {
        return fengrowNum;
    }

    public void setFengrowNum(int fengrowNum) {
        this.fengrowNum = fengrowNum;
    }

    public int getAssureCardNum() {
        return assureCardNum;
    }

    public void setAssureCardNum(int assureCardNum) {
        this.assureCardNum = assureCardNum;
    }

    public ISaleDao getSaleDao() {
        return saleDao;
    }

    public void setSaleDao(ISaleDao saleDao) {
        this.saleDao = saleDao;
    }

    public int getDateNum() {
        return dateNum;
    }

    public void setDateNum(int dateNum) {
        this.dateNum = dateNum;
    }

    public List getLsmustDate() {
        return lsmustDate;
    }

    public void setLsmustDate(List lsmustDate) {
        this.lsmustDate = lsmustDate;
    }

    public List getLstRoomType() {
        return lstRoomType;
    }

    public void setLstRoomType(List lsRoomType) {

        this.lstRoomType = lsRoomType;
    }

    public IContractDao getContractDao() {
        return contractDao;
    }

    public void setContractDao(IContractDao contractDao) {
        this.contractDao = contractDao;
    }

    public String getCreditDate() {
        return creditDate;
    }

    public void setCreditDate(String creditDate) {
        this.creditDate = creditDate;
    }

    public List getLstCreditAssure() {
        return lstCreditAssure;
    }

    public void setLstCreditAssure(List lstCreditAssure) {
        this.lstCreditAssure = lstCreditAssure;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public HtlCreditAssure getHtlCreditAssure() {
        return htlCreditAssure;
    }

    public void setHtlCreditAssure(HtlCreditAssure htlCreditAssure) {
        this.htlCreditAssure = htlCreditAssure;
    }

    public String getBeDate() {
        return beDate;
    }

    public void setBeDate(String date) {
        beDate = date;
    }

    public String getEnDate() {
        return enDate;
    }

    public void setEnDate(String date) {
        enDate = date;
    }

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}

}
