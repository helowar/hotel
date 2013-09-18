package com.mangocity.hotel.base.web;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.dao.IContractDao;
import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.persistence.HtlBreakfast;
import com.mangocity.hotel.base.persistence.HtlBreakfastPrice;
import com.mangocity.hotel.base.persistence.HtlChargeBreakfast;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;

/**
 */
public class BreakfastAction extends PersistenceAction {

    private Long contractId;

    private Long hotelId;

    private IContractDao contractDao;

    private List rewards = new ArrayList();

    private String List = "list";

    private Map map;

    private int breakfastNum;

    protected static final String EDIT = "edit";

    // 传入的合同开始日期
    private Date beginDate;

    // 传入的合同结束日期
    private Date endDate;

    private String beDate;

    private String enDate;

    private ContractManage contractManage;

    private String payMethod;

    private String remark;

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    protected Class getEntityClass() {
        return HtlChargeBreakfast.class;
    }

    public ContractManage getContractManage() {
        return contractManage;
    }

    public void setContractManage(ContractManage contractManage) {
        this.contractManage = contractManage;
    }

    protected Object populateEntity() {

        List ls = MyBeanUtil.getBatchObjectFromParam(super.getParams(), HtlBreakfast.class,
            breakfastNum);

        // 修改
        HtlChargeBreakfast chargeBreakfast = (HtlChargeBreakfast) super.populateEntity();
        chargeBreakfast.getBreakfastFees().clear();

        // Comparator comp = Collections.reverseOrder();
        // Collections.sort(ls, comp);
        for (int m = ls.size() - 1; 0 <= m; m--) {
            HtlBreakfast breakfast = (HtlBreakfast) ls.get(m);

            breakfast.setBreakfast(chargeBreakfast);
            chargeBreakfast.getBreakfastFees().add(breakfast);

        }
        if (null != super.getOnlineRoleUser()) {
            if (null == chargeBreakfast.getID() || 0 == chargeBreakfast.getID()) {
                chargeBreakfast.setCreateBy(super.getOnlineRoleUser().getName());
                chargeBreakfast.setCreateById(super.getOnlineRoleUser().getLoginName());

            }
            chargeBreakfast.setModifyBy(super.getOnlineRoleUser().getName());
            chargeBreakfast.setModifyById(super.getOnlineRoleUser().getLoginName());
        }
        chargeBreakfast.setModifyTime(new Date());
        return chargeBreakfast;
    }

    // 为修改用
    // 比较奇怪,修改信息的时候取的值与新增的时候比是反的,所以在循环的时候又要倒回来
    protected Object populateEntityForEdit() {

        List ls = MyBeanUtil.getBatchObjectFromParam(super.getParams(), HtlBreakfast.class,
            breakfastNum);

        // 修改
        HtlChargeBreakfast chargeBreakfast = (HtlChargeBreakfast) super.populateEntity();
        if (null != chargeBreakfast.getBreakfastFees()) {
            chargeBreakfast.getBreakfastFees().clear();
        } else {
            chargeBreakfast.setBreakfastFees(new ArrayList());
        }

        // Comparator comp = Collections.reverseOrder();
        // Collections.sort(ls, comp);
        for (int m = 0; m < ls.size(); m++) {
            HtlBreakfast breakfast = (HtlBreakfast) ls.get(m);

            breakfast.setBreakfast(chargeBreakfast);
            chargeBreakfast.getBreakfastFees().add(breakfast);

        }
        if (null != super.getOnlineRoleUser()) {
            if (null == chargeBreakfast.getID() || 0 == chargeBreakfast.getID()) {
                chargeBreakfast.setCreateBy(super.getOnlineRoleUser().getName());
                chargeBreakfast.setCreateById(super.getOnlineRoleUser().getLoginName());

            }
            chargeBreakfast.setModifyBy(super.getOnlineRoleUser().getName());
            chargeBreakfast.setModifyById(super.getOnlineRoleUser().getLoginName());
        }
        chargeBreakfast.setModifyTime(new Date());
        return chargeBreakfast;
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

        // if (super.getEntity() == null) {
        // String error = "找不到实体对象,id:" + super.getEntityID() + "; 类：" + getEntityClass();
        // return super.forwardError(error);
        // }
        //  
        if (null != super.getEntity()) {
            super.setEntityForm(populateFormBean(super.getEntity()));
        }

        return VIEW;
    }

    //
    public String save() {
        MyBeanUtil.getBatchObjectFromParam(super.getParams(), HtlBreakfast.class, breakfastNum);
        MyBeanUtil.getBatchObjectFromParam(super.getParams(), HtlChargeBreakfast.class,
            breakfastNum);
        List ls2 = MyBeanUtil.getBatchObjectFromParam(super.getParams(), HtlBreakfastPrice.class,
            breakfastNum);

        for (int k = 0; k < ls2.size(); k++) {
            HtlBreakfastPrice breakfastPrice = (HtlBreakfastPrice) ls2.get(k);

            // chargebreakfast赋值
            HtlChargeBreakfast chargeBreakfast = new HtlChargeBreakfast();
            if (null != contractId)
                chargeBreakfast.setContractId(contractId);
            if (null != breakfastPrice.getBeginDate())
                chargeBreakfast.setBeginDate(breakfastPrice.getBeginDate());
            if (null != breakfastPrice.getEndDate())
                chargeBreakfast.setEndDate(breakfastPrice.getEndDate());
            if (null != breakfastPrice.getPayMethod())// 支付类型,add by haibo.li 酒店2.9
                chargeBreakfast.setPayMethod(breakfastPrice.getPayMethod());
            if (null != breakfastPrice.getRemark())// 备注 add by haibo.li 酒店2.9
                chargeBreakfast.setRemark(breakfastPrice.getRemark());

            // 给breakfast
            HtlBreakfast breakfast = new HtlBreakfast();
            breakfast.setType(breakfastPrice.getChineseFoodType());
            breakfast.setBasePrice(StringUtil.getStrTodouble(breakfastPrice.getChineseFood()));
            breakfast.setBreakfast(chargeBreakfast);
            chargeBreakfast.getBreakfastFees().add(breakfast);

            HtlBreakfast breakfast1 = new HtlBreakfast();
            breakfast1.setType(breakfastPrice.getWesternFoodType());
            breakfast1.setBasePrice(StringUtil.getStrTodouble(breakfastPrice.getWesternFood()));
            breakfast1.setBreakfast(chargeBreakfast);
            chargeBreakfast.getBreakfastFees().add(breakfast1);

            HtlBreakfast breakfast2 = new HtlBreakfast();
            breakfast2.setType(breakfastPrice.getBuffetDinneTyper());
            breakfast2.setBasePrice(StringUtil.getStrTodouble(breakfastPrice.getBuffetDinner()));
            breakfast2.setBreakfast(chargeBreakfast);
            chargeBreakfast.getBreakfastFees().add(breakfast2);

            new BizRuleCheck();
            chargeBreakfast.setActive(BizRuleCheck.getTrueString());
            /*
             * if (super.getOnlineRoleUser()!=null){
             * chargeBreakfast.setCreateById(super.getOnlineRoleUser().getLoginName());
             * chargeBreakfast.setCreateBy(super.getOnlineRoleUser().getName()); }
             */
            // chargeBreakfast.setCreateTime(DateUtil.getSystemDate());
            if (null != super.getOnlineRoleUser()) {
                if (null == chargeBreakfast.getID()) {
                    chargeBreakfast.setCreateBy(super.getOnlineRoleUser().getName());
                    chargeBreakfast.setCreateById(super.getOnlineRoleUser().getLoginName());

                }
                chargeBreakfast.setModifyBy(super.getOnlineRoleUser().getName());
                chargeBreakfast.setModifyById(super.getOnlineRoleUser().getLoginName());
            }
            chargeBreakfast.setModifyTime(new Date());
            // 保存数据库
            // super.getEntityManager().save(chargeBreakfast);
            try {
                contractManage.createChargeBreakfast(chargeBreakfast);
            } catch (IllegalAccessException e) {
            	log.error(e.getMessage(),e);
            } catch (InvocationTargetException e) {
            	log.error(e.getMessage(),e);
            }

        }
        return SAVE_SUCCESS;

    }

    public String edit() {
        super.setEntity(populateEntityForEdit());

        try {
            contractManage.modifyChargeBreakfast((HtlChargeBreakfast) super.getEntity());
        } catch (IllegalAccessException e) {
        	log.error(e.getMessage(),e);
        } catch (InvocationTargetException e) {
        	log.error(e.getMessage(),e);
        }
        // if (super.getEntityID() == null || super.getEntityID()== 0)
        // super.getEntityManager().save(super.getEntity());
        // else
        // super.getEntityManager().saveOrUpdate(super.getEntity());

        super.setEntityForm(populateFormBean(super.getEntity()));

        return EDIT;
    }

    public int getBreakfastNum() {
        return breakfastNum;
    }

    public void setBreakfastNum(int breakfastNum) {
        this.breakfastNum = breakfastNum;
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

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getList() {
        return List;
    }

    public void setList(String list) {
        List = list;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public List getRewards() {
        return rewards;
    }

    public void setRewards(List rewards) {
        this.rewards = rewards;
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

}
