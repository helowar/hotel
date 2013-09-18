package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.dao.IContractDao;
import com.mangocity.hotel.base.persistence.CEntityEvent;
import com.mangocity.hotel.base.persistence.HtlRewardInfo;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.bean.DateSegment;
import com.mangocity.util.bean.MyBeanUtil;

/**
 */
public class RewardAction extends PersistenceAction {

    /**
     * 合同id
     */
    private Long contractID;

    private IContractDao contractDao;

    // 合同开始时间
    private Date beginDate;

    // 合同结束时间
    private Date endDate;

    /**
     * 酒店id
     */
    private long hotelID;

    /**
     * 房型
     */
    private String[] sRoomType;

    private List rewards = new ArrayList();

    private String List = "list";

    // 删除时传的时间参数
    private String beDate;

    private String enDate;

    // 输入的行数
    private int rewardRowNum;

    // private String[] roomType;

    protected Class getEntityClass() {
        return HtlRewardInfo.class;
    }

    /*
     * public String[] getRoomType() { return roomType; }
     * 
     * public void setRoomType(String[] roomType) { this.roomType = roomType; }
     * 
     * protected void prepare() { //
     * 
     * if (roomType != null) { StringBuffer roomTypes = new StringBuffer(); for (int i = 0; i <
     * roomType.length; i++) { roomTypes.append(roomType[i]).append(","); }
     * super.getParams().put("roomType", roomTypes.toString()); } }
     */
    // 删除
    // public String delete() {
    //
    // return super.delete();
    // }

    public String save() {
        Map params = super.getParams();
        List rewardDateList = MyBeanUtil.getBatchObjectFromParam(params, DateSegment.class,
            rewardRowNum);
        this.setEntity(this.populateEntity());
        HtlRewardInfo ri = (HtlRewardInfo) this.getEntity();

        if (null != rewardDateList && 0 != rewardDateList.size()) {
            for (int i = 0; i < rewardDateList.size(); i++) {
                DateSegment BEDate = (DateSegment) rewardDateList.get(i);
                ri.setBeginUseDate(BEDate.getStart());
                ri.setEndUseDate(BEDate.getEnd());

                if (!"1".equals(ri.getForwardType())) {
                    ri.setPresentRoomType(Long.valueOf(0));
                }
                // 赠送间夜数
                if (!"1".equals(ri.getForwardType())) {
                    ri.setPresentNight(0);
                    ri.setPresentRoomType(0);
                    ri.setBeginFreeUseDate(null);
                    ri.setEndFreeUseDate(null);
                }
                // 奖金
                if (!"0".equals(ri.getForwardType())) {
                    ri.setEverydayBonus(0);
                }
                // 销售额百分比
                if (!"2".equals(ri.getForwardType())) {
                    ri.setBonusScale(0);
                }
                ri.setRoomTypeId(BizRuleCheck.ArrayToString(sRoomType));
                if (null != super.getOnlineRoleUser()) {
                    ri = (HtlRewardInfo) CEntityEvent.setCEntity(ri, super.getOnlineRoleUser()
                        .getName(), super.getOnlineRoleUser().getLoginName());
                }
                if (null == this.getEntityID() || 0 == this.getEntityID())
                    super.getEntityManager().save(ri);
                else
                    super.getEntityManager().saveOrUpdate(ri);
            }
        }
        this.setEntityID((ri).getID());
        this.setEntityForm(this.populateFormBean(ri));
        return SAVE_SUCCESS;
    }

    public String list() {

        if (null == contractID)
            super.forwardError("contractID不能为空!");

        rewards = contractDao.getRewards(contractID);
        log.debug("size========" + rewards.size());
        return List;
    }

    public String listReward() {

        if (null == contractID)
            super.forwardError("contractID不能为空!");

        rewards = contractDao.getRewards(contractID);
        log.debug("size========" + rewards.size());
        return "listReward";
    }

    public String viewReward() {

        if (null == super.getEntityID()) {
            String error = "entityID不能为空!，请传入entityID参数!";

            return super.forwardError(error);
        }

        super.setEntity(populateEntity());

        if (null == super.getEntity()) {
            String error = "找不到实体对象,id:" + super.getEntityID() + "; 类：" + getEntityClass();
            return super.forwardError(error);
        }

        super.setEntityForm(populateFormBean(super.getEntity()));
        return "viewReward";
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

    public List getRewards() {
        return rewards;
    }

    public void setRewards(List rewards) {
        this.rewards = rewards;
    }

    public long getHotelID() {
        return hotelID;
    }

    public void setHotelID(long hotelID) {
        this.hotelID = hotelID;
    }

    public String[] getSRoomType() {
        return sRoomType;
    }

    public void setSRoomType(String[] roomType) {
        sRoomType = roomType;
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

    public int getRewardRowNum() {
        return rewardRowNum;
    }

    public void setRewardRowNum(int rewardRowNum) {
        this.rewardRowNum = rewardRowNum;
    }

}
