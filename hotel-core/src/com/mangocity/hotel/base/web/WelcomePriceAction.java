package com.mangocity.hotel.base.web;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlChildWelcomePrice;
import com.mangocity.hotel.base.persistence.HtlWelcomePrice;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.bean.MyBeanUtil;

/**
 * 调价Action
 * 
 * @author zhengxin
 * 
 */
public class WelcomePriceAction extends PersistenceAction {

    private Long contractId;

    private Long hotelId;

    // 接送详细信息数据
    private List priceList;

    // 详细信息行数
    private int welcomeNum;

    // 批量查询
    private static final String BATCH_QUERY = "batchQuery";

    // 传入的合同开始日期
    private Date beginDate;

    // 传入的合同结束日期
    private Date endDate;

    // 删除时传的时间参数
    private String beDate;

    private String enDate;

    protected Object populateEntity() {

        List ls = MyBeanUtil.getBatchObjectFromParam(super.getParams(), HtlChildWelcomePrice.class,
            welcomeNum);

        HtlWelcomePrice welcomePrice = (HtlWelcomePrice) super.populateEntity();

        new BizRuleCheck();
        welcomePrice.setActive(BizRuleCheck.getTrueString());

        welcomePrice.getWelcomeFees().clear();

        for (int m = 0; m < ls.size(); m++) {
            HtlChildWelcomePrice childWelcomePrice = (HtlChildWelcomePrice) ls.get(m);

            childWelcomePrice.setParentWelcome(welcomePrice);

            welcomePrice.getWelcomeFees().add(childWelcomePrice);

        }
        if (null != super.getOnlineRoleUser()) {
            if (null == welcomePrice.getID() || 0 == welcomePrice.getID()) {
                welcomePrice.setCreateBy(super.getOnlineRoleUser().getName());
                welcomePrice.setCreateById(super.getOnlineRoleUser().getLoginName());

            }
            welcomePrice.setModifyBy(super.getOnlineRoleUser().getName());
            welcomePrice.setModifyById(super.getOnlineRoleUser().getLoginName());
        }
        welcomePrice.setModifyTime(new Date());
        return welcomePrice;
    }

    /**
     * 查看
     * 
     * @return
     */
    public String view() {
        if (null == super.getEntityID()) {
            String error = "entityID不能为空!，请传入entityID参数!";

            return super.forwardError(error);
        }
        super.setEntity(super.populateEntity());
        // HtlWelcomePrice welcomePrice = (HtlWelcomePrice) super.populateEntity();

        if (null == super.getEntity()) {
            String error = "找不到实体对象,id:" + super.getEntityID() + "; 类：" + getEntityClass();
            return super.forwardError(error);
        }

        super.setEntityForm(populateFormBean(super.getEntity()));
        super.getEntityForm();
        return VIEW;
    }

    public List getPriceList() {
        return priceList;
    }

    public void setPriceList(List priceList) {
        this.priceList = priceList;
    }

    public int getWelcomeNum() {
        return welcomeNum;
    }

    public void setWelcomeNum(int welcomeNum) {
        this.welcomeNum = welcomeNum;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
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

    protected Class getEntityClass() {
        return HtlWelcomePrice.class;
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

}
