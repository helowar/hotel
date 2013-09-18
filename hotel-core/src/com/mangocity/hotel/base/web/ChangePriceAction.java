package com.mangocity.hotel.base.web;

import java.util.List;

import com.mangocity.hotel.base.manage.IChangePriceManage;
import com.mangocity.hotel.base.persistence.HtlChangePrice;
import com.mangocity.hotel.base.persistence.HtlChangePriceLog;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.hotel.constant.BaseConstant;

/**
 */
public class ChangePriceAction extends PersistenceAction {

    /**
     * 变价工单ID
     */
    private Long ID;

    /**
     * 变价工单CD
     */
    private String taskCode;

    /**
     * 跳转到询问用户是不是要创建一个新的变价工单
     */
    private static final String SHOWMESSAGE = "showmessage";

    /**
     * 跳转到
     */
    private static final String CREATECHANGEPRICE = "createChangePrice";

    /**
     * 酒店编码
     */
    private String hotelCD;

    /**
     * 酒店ID
     */
    private long hotelId;

    private String operateResult;

    /**
     * 变价工单历史记录列表
     */
    private List lstChangePriceLog;

    /**
     * 酒店名称
     */
    private String hotelName;

    /**
     * 变价工单管理类
     */
    private IChangePriceManage changePriceManage;

    public String getHotelCD() {
        return hotelCD;
    }

    public void setHotelCD(String hotelCD) {
        this.hotelCD = hotelCD;
    }

    protected Class getEntityClass() {
        return HtlChangePrice.class;
    }

    /**
     * 创建变价工单，不管当天这个酒店是不是已经存在一个变价工单
     * 
     * @return
     */
    public String createChangePriceMust() {
        if (operateResult.equals("Y")) {
            newChangePrice();
        }
        return "createChangePrice";
    }

    /**
     * 创建变价工单
     * 
     * @return createChangePrice 跳转到
     */
    public String createChangePrice() {
        String result = "";
        if (changePriceManage.checkChangePriceExist(hotelId)) {
            result = "askUser";
        } else {
            newChangePrice();
            result = "createChangePrice";
        }
        return result;
    }

    /**
     * 更新为变价中
     * 
     * @return
     */
    public String updateChangeing() {
        HtlChangePrice cp = this.getChangePrice();
        changePriceManage.updateChangeing(cp, this.createHtlChangePriceLog(cp));
        return "updateChangeing";
    }

    /**
     * 更新为已核查
     * 
     * @return
     */
    public String updateCheck() {
        HtlChangePrice cp = this.getChangePrice();
        changePriceManage.updateCheck(cp, this.createHtlChangePriceLog(cp));
        return "updateCheck";
    }

    /**
     * 更新为再次跟进
     * 
     * @return
     */
    public String updateAgainChange() {
        HtlChangePrice cp = this.getChangePrice();
        changePriceManage.updateAgainChange(cp, this.createHtlChangePriceLog(cp));
        return "updateAgainChange";
    }

    /**
     * 更新为再次核查
     * 
     * @return
     */
    public String updateAgainCheck() {
        HtlChangePrice cp = this.getChangePrice();
        changePriceManage.updateAgainCheck(cp, this.createHtlChangePriceLog(cp));
        return "updateAgainCheck";
    }

    /**
     * 更新为已调整
     * 
     * @return
     */
    public String updateAdjusted() {
        HtlChangePrice cp = this.getChangePrice();
        changePriceManage.updateAdjusted(cp, this.createHtlChangePriceLog(cp));
        return "updateAdjusted";
    }

    /**
     * 更新为QA已审查
     * 
     * @return
     */
    public String updateQAcheck() {
        HtlChangePrice cp = this.getChangePrice();
        changePriceManage.updateQAcheck(cp, this.createHtlChangePriceLog(cp));
        return "updateQAcheck";
    }

    public String viewHistory() {
        HtlHotel hotel = (HtlHotel) super.getEntityManager().find(HtlHotel.class, hotelId);
        if (null != hotel) {
            this.setHotelName(hotel.getChnName());
        }
        lstChangePriceLog = changePriceManage.getChangePriceLog(taskCode);
        return "viewHistory";
    }

    private HtlChangePriceLog createHtlChangePriceLog(HtlChangePrice cp) {
        HtlChangePriceLog cpl = new HtlChangePriceLog();
        cpl.setChangeDate(cp.getChangeDate());
        cpl.setHotelId(cp.getHotelId());
        cpl.setOperateDate(DateUtil.getDate(DateUtil.getSystemDate()));
        cpl.setOperateState(cp.getStatus());
        if (null != super.getOnlineRoleUser())
            cpl.setOperater(super.getOnlineRoleUser().getName());
        if (null != super.getOnlineRoleUser())
            cpl.setOperaterId(super.getOnlineRoleUser().getLoginName());
        cpl.setTaskCode(cp.getTaskCode());
        return cpl;
    }

    private HtlChangePrice getChangePrice() {
        return changePriceManage.getChangePriceById(ID);
    }

    private void newChangePrice() {
        HtlChangePrice changePrice = new HtlChangePrice();
        changePrice.setHotelId(hotelId);
        if (null != super.getOnlineRoleUser()) {
            changePrice.setCreateByUserId(super.getOnlineRoleUser().getLoginName());
            changePrice.setCreateBy(super.getOnlineRoleUser().getName());
        }
        changePrice.setCreateTime(DateUtil.getDate(DateUtil.getSystemDate()));
        changePrice.setChangeDate(DateUtil.getDate(DateUtil.getSystemDate()));
        changePrice.setStatus(BaseConstant.CP_NEW);
        changePrice.setTaskCode(BizRuleCheck.getCreateChangePriceCode(hotelCD));
        HtlChangePriceLog cpl = new HtlChangePriceLog();
        cpl.setChangeDate(changePrice.getChangeDate());
        cpl.setHotelId(changePrice.getHotelId());
        cpl.setOperateDate(DateUtil.getDate(DateUtil.getSystemDate()));
        cpl.setOperateState(changePrice.getStatus());
        if (null != super.getOnlineRoleUser()) {
            cpl.setOperater(super.getOnlineRoleUser().getName());
            cpl.setOperaterId(super.getOnlineRoleUser().getLoginName());
        }
        cpl.setTaskCode(changePrice.getTaskCode());

        changePriceManage.createOrUpdateChangePriceWithLog(changePrice, cpl);
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public IChangePriceManage getChangePriceManage() {
        return changePriceManage;
    }

    public void setChangePriceManage(IChangePriceManage changePriceManage) {
        this.changePriceManage = changePriceManage;
    }

    public String getOperateResult() {
        return operateResult;
    }

    public void setOperateResult(String operateResult) {
        this.operateResult = operateResult;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public List getLstChangePriceLog() {
        return lstChangePriceLog;
    }

    public void setLstChangePriceLog(List lstChangePriceLog) {
        this.lstChangePriceLog = lstChangePriceLog;
    }

}
