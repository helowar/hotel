package com.mangocity.hotel.base.web;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlBatchCutoffDay;
import com.mangocity.hotel.base.persistence.HtlQuotabatch;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.DateSegment;
import com.mangocity.util.bean.MyBeanUtil;

/**
 * @author xiaowumi 批量设定cutoff Day的记录
 */
public class BatchCutoffDayAction extends PersistenceAction {

    private long batchQuotaId;

    /**
     * 将要调整的星期
     */
    private String[] week;

    /**
     * 编辑模式
     */
    private String editModal;

    private int rowNum;

    /**
     * 合同开始日期 只做传递用
     */
    private String bD;

    /**
     * 合同结束日期 只做传递用
     */
    private String eD;

    /**
	 * 
	 */
    private static final long serialVersionUID = -3560664104126088486L;

    private static final String EDITMODAL_MODIFY = "modify";

    private static final String EDITMODAL_CREATE = "create";

    protected Class getEntityClass() {
        return HtlBatchCutoffDay.class;
    }

    public String save() {
        Map params = super.getParams();
        super.setEntity(super.populateEntity());
        HtlBatchCutoffDay bcd = (HtlBatchCutoffDay) super.getEntity();
        this.setBatchQuotaId(bcd.getQuotabatch().getID());
        List lsDate = MyBeanUtil.getBatchObjectFromParam(params, DateSegment.class, rowNum);
        String weeks = DateUtil.weeksToString(week);
        for (int i = 0; i < lsDate.size(); i++) {
            DateSegment dDate = (DateSegment) lsDate.get(i);
            HtlBatchCutoffDay batchCutoffDay = new HtlBatchCutoffDay();
            MyBeanUtil.copyProperties(batchCutoffDay, bcd);
            new BizRuleCheck();
            if (null != super.getOnlineRoleUser())
                batchCutoffDay.setCreateBy(super.getOnlineRoleUser().getName());
            batchCutoffDay.setCreateTime(DateUtil.getSystemDate());
            batchCutoffDay.setBeginDate(dDate.getStart());
            batchCutoffDay.setEndDate(dDate.getEnd());
            batchCutoffDay.setAdjustWeek(weeks);
            batchCutoffDay.setModifyBy(super.getOnlineRoleUser().getName());
            batchCutoffDay.setModifyTime(DateUtil.getSystemDate());
            if (getEditModal().equals(EDITMODAL_MODIFY)) {
                super.getEntityManager().saveOrUpdate(batchCutoffDay);
            } else if (getEditModal().equals(EDITMODAL_CREATE)) {
                super.getEntityManager().save(batchCutoffDay);
            }
        }
        String result = "save";
        // 传递配额批次ID，用于返回页面
        this.setEntityID(batchQuotaId);
        return result;
    }

    public String view() {
        super.view();
        HtlBatchCutoffDay batchCutoffDay = (HtlBatchCutoffDay) this.getEntity();
        HtlQuotabatch quotaBatch = (HtlQuotabatch) super.getEntityManager().find(
            HtlQuotabatch.class, batchCutoffDay.getQuotabatch().getID());
        super.getParams().put("quotaBatch", quotaBatch);
        super.getParams().put("view", "view");
        setEditModal(EDITMODAL_MODIFY);
        return "view";
    }

    public String create() {
        HtlQuotabatch quotaBatch = (HtlQuotabatch) super.getEntityManager().find(
            HtlQuotabatch.class, batchQuotaId);
        Map params = super.getParams();
        if (null != quotaBatch) {
            params.put("quotaBatch", quotaBatch);
        }
        HtlBatchCutoffDay bc = new HtlBatchCutoffDay();
        bc.setBeginDate(quotaBatch.getBeginDate());
        bc.setEndDate(quotaBatch.getEndDate());
        super.setEntity(bc);
        setEditModal(EDITMODAL_CREATE);
        return "create";
    }

    public long getBatchQuotaId() {
        return batchQuotaId;
    }

    public void setBatchQuotaId(long batchQuotaId) {
        this.batchQuotaId = batchQuotaId;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public String[] getWeek() {
        return week;
    }

    public void setWeek(String[] week) {
        this.week = week;
    }

    public String getEditModal() {
        return editModal;
    }

    public void setEditModal(String editModal) {
        this.editModal = editModal;
    }

    public String getBD() {
        return bD;
    }

    public void setBD(String bd) {
        bD = bd;
    }

    public String getED() {
        return eD;
    }

    public void setED(String ed) {
        eD = ed;
    }
}
