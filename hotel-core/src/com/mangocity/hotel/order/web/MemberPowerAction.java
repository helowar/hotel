package com.mangocity.hotel.order.web;

import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.hotel.order.persistence.OrUserPower;
import com.mangocity.hotel.order.service.IOrderService;

/**
 * 权限编辑
 * 
 * @author zhuangzhineng
 * 
 */
public class MemberPowerAction extends PersistenceAction {

    /**
     * 所选权限
     */
    private String[] powerId;

    private IOrderService orderService;

    @Override
    protected Class getEntityClass() {
        return OrUserPower.class;
    }

    /**
     * 新增，修改，查看时
     * 
     * @return
     */
    public String forward() {
        if (VIEW.equals(super.getOperateType())) {
            super.view();
            return VIEW;
        } else if (EDIT.equals(super.getOperateType())) {
            super.view();
            return EDIT;
        } else {
            return CREATE;
        }
    }

    /**
     * 保存时
     * 
     * @return
     */
    @Override
    public String save() {
        OrUserPower orUserPower = (OrUserPower) super.populateEntity();
        orUserPower.setPowerId(BizRuleCheck.ArrayToString(powerId));
        orderService.saveOrUpdateUserPower(orUserPower);
        // super.getEntityManager().saveOrUpdate(orUserPower);
        // super.save();
        return "savePower";
    }

    /*
     * (non-Javadoc) 删除时
     * 
     * @see com.mangocity.util.webwork.PersistenceAction#delete()
     */
    @Override
    public String delete() {
        orderService.delUserPower(super.getEntityID());
        // super.getEntityManager().remove(getEntityClass(), super.getEntityID());
        return "deleted";
    }

    public String[] getPowerId() {
        return powerId;
    }

    public void setPowerId(String[] powerId) {
        this.powerId = powerId;
    }

    public IOrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(IOrderService orderService) {
        this.orderService = orderService;
    }

}
