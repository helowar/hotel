package com.mangocity.hotel.base.web;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.persistence.CEntity;
import com.mangocity.hotel.base.persistence.CEntityEvent;
import com.mangocity.hotel.base.persistence.HtlAddBedPrice;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.MyBeanUtil;

/**
 */
public class AddBedPriceAction extends PersistenceAction {
    private Long contractId;

    private Long hotelId;

    private int bedPriceNum;

    private String BATCH_SAVE = "batchSave";

    // 传入的合同开始日期
    private Date beginDate;

    // 传入的合同结束日期
    private Date endDate;

    // 删除时传的时间参数
    private String beDate;

    private String enDate;

    private List lstRoomType;

    private ContractManage contractManage;
    
    private HotelRoomTypeService hotelRoomTypeService;

    private String roomTypes;

    public String getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(String roomTypes) {
        this.roomTypes = roomTypes;
    }

    public ContractManage getContractManage() {
        return contractManage;
    }

    public void setContractManage(ContractManage contractManage) {
        this.contractManage = contractManage;
    }

    protected Class getEntityClass() {
        return HtlAddBedPrice.class;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String create() {
        super.setEntity(populateEntity());
        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId);
        return CREATE;
    }

    public String batchSave() {
        List ls = MyBeanUtil.getBatchObjectFromParam(super.getParams(), HtlAddBedPrice.class, bedPriceNum);
        String[] roomTypeArray = roomTypes.split(",");

        for (int k = 0; k < ls.size(); k++) {
            for (String roomType : roomTypeArray) {
                HtlAddBedPrice addPricePrice = (HtlAddBedPrice) ls.get(k);
                addPricePrice.setActive(BizRuleCheck.getTrueString());
                addPricePrice.setContractId(contractId);
                addPricePrice.setRoomType(roomType);
                // 保存数据库
                if (null != super.getOnlineRoleUser()) {
                    if (null == addPricePrice.getID() || 0 == addPricePrice.getID()) {
                        addPricePrice.setCreateBy(super.getOnlineRoleUser().getName());
                        addPricePrice.setCreateById(super.getOnlineRoleUser().getLoginName());
                        addPricePrice.setCreateTime(DateUtil.getSystemDate());
                    }
                    addPricePrice.setModifyBy(super.getOnlineRoleUser().getName());
                    addPricePrice.setModifyById(super.getOnlineRoleUser().getLoginName());
                }
                addPricePrice.setModifyTime(new Date());
                try {
                    contractManage.createAddBedPrice(addPricePrice);
                } catch (IllegalAccessException e) {
                	log.error(e.getMessage(),e);
                } catch (InvocationTargetException e) {
                	log.error(e.getMessage(),e);
                }
            }
        }
        return BATCH_SAVE;

    }

    public String save() {

        super.setEntity(super.populateEntity());

        HtlAddBedPrice addBedPrice = (HtlAddBedPrice) super.getEntity();

        if (addBedPrice instanceof CEntity// parasoft-suppress OPT.UISO "业务问题\暂不修改。" 
            && null != super.getOnlineRoleUser()) {
            addBedPrice = (HtlAddBedPrice) CEntityEvent.setCEntity(addBedPrice, super
                .getOnlineRoleUser().getName(), super.getOnlineRoleUser().getLoginName());
        }

        try {
            contractManage.modifyAddBedPrice(addBedPrice);
        } catch (IllegalAccessException e) {
        	log.error(e.getMessage(),e);
        } catch (InvocationTargetException e) {
        	log.error(e.getMessage(),e);
        }
        super.setEntityForm(populateFormBean(super.getEntity()));

        return SAVE_SUCCESS;
    }

    public List getLstRoomType() {
        return lstRoomType;
    }

    public void setLstRoomType(List lstRoomType) {
        this.lstRoomType = lstRoomType;
    }

    public int getBedPriceNum() {
        return bedPriceNum;
    }

    public void setBedPriceNum(int bedPriceNum) {
        this.bedPriceNum = bedPriceNum;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
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
