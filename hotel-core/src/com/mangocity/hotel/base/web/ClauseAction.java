package com.mangocity.hotel.base.web;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;

import com.mangocity.hotel.base.manage.ClauseManage;
import com.mangocity.hotel.base.persistence.HtlBookCaulClause;
import com.mangocity.hotel.base.persistence.HtlBookModifyField;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlPreconcertItemTemplet;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;

/**
 */
public class ClauseAction extends PersistenceAction {

    private ClauseManage clauseManage;

    private long hotelId;

    private Long modifyFieldId;

    private String modifyField;

    private String clauseRule;

    private long contractId;

    private String falg;

    private List<HtlContract> contractList = new ArrayList<HtlContract>();

    private List<HtlPreconcertItemTemplet> clausesList = new ArrayList<HtlPreconcertItemTemplet>();

    private HtlBookCaulClause htlBookCaulClause;

    /**
     * 预订条款计算规则 hotel 2.9.2 add by chenjiajie 2009-08-06
     */
    private List<HtlBookCaulClause> htlBookCaulClauseList;

    /**
     * 预订条款计算规则记录数 hotel 2.9.2 add by chenjiajie 2009-08-06
     */
    private int htlBookCaulClauseListNum;

    /**
     * 预订条款修改字段定义 hotel 2.9.2 add by chenjiajie 2009-08-06
     */
    private HtlBookModifyField htlBookModifyField;

    @Override
    protected Class getEntityClass() {
        return HtlBookModifyField.class;
    }

    /*
     * Add by Shengwei.Zuo 2009-02-02 查看该酒店的合同列表和预定条款模板列表
     */
    public String getContractListByHTID() {

        contractList = clauseManage.searchContactByHTlID(hotelId);

        clausesList = clauseManage.searchClasesByHTLID(hotelId);

        // 根据酒店ID得到起预定条款计算规则信息 add by shengwei.zuo 2009-02-12
        htlBookCaulClauseList = clauseManage.searchBookCaulByHTLID(hotelId);
        htlBookCaulClauseListNum = null != htlBookCaulClauseList ? htlBookCaulClauseList.size() : 0;
        /** 根据酒店Id查询预订条款修改字段定义 add by chen jiajie 2009-08-06 **/
        htlBookModifyField = clauseManage.searchBookModifyFieldByHTLID(hotelId);
        if (null != contractList) {
            for (int i = 0; i < contractList.size(); i++) {
                HtlContract hc = contractList.get(i);
                contractId = hc.getID();
            }
        }

        return "getContractList";

    }

    /*
     * 添加预定条款计算规则 add by shengwei.zuo 2009-02-12
     */
    public String addBookRules() {
        Map params = super.getParams();
        super.setEntityID(modifyFieldId);
        super.setEntity(super.populateEntity());
        this.setHtlBookModifyField((HtlBookModifyField) this.getEntity());

        /*
         * 添加或者修改预定条款修改字段定义
         */
        if (null != htlBookModifyField) {
            clauseManage.createModifyField(htlBookModifyField);
        }

        // 根据酒店ID查询本酒店所有计算规则
        htlBookCaulClauseList = clauseManage.searchBookCaulByHTLID(hotelId);
        /*
         * 添加或保存预订条款计算规则（可能多条记录）
         */
        List tempBookCaulClauseList = MyBeanUtil.getBatchObjectFromParam(params,
            HtlBookCaulClause.class, htlBookCaulClauseListNum);

        // 检查有没有需要删除的记录，有则删除
        clauseManage.removeOldBookCaulClause(htlBookCaulClauseList, tempBookCaulClauseList);

        if (null != tempBookCaulClauseList) {
            for (Iterator it = tempBookCaulClauseList.iterator(); it.hasNext();) {
                HtlBookCaulClause tmpBookCaulClause = (HtlBookCaulClause) it.next();
                if (StringUtil.isValidStr(tmpBookCaulClause.getClauseRule())) {
                    tmpBookCaulClause.setHotelId(hotelId);
                    try {
                        // 保存预订条款计算规则 如果有重复，则覆盖时间段，有可能拆分时间段 hotel2.9.2
                        clauseManage.createBookRules(tmpBookCaulClause, super.getOnlineRoleUser());
                    } catch (IllegalAccessException e) {
                    	log.error(e.getMessage(),e);
                    } catch (InvocationTargetException e) {
                    	log.error(e.getMessage(),e);
                    }
                }
            }
        }

        if (!("".equals(falg)) && falg.equals("contract")) {
            contractList = clauseManage.searchContactByHTlID(hotelId);
            for (int i = 0; i < contractList.size(); i++) {
                HtlContract hc = contractList.get(i);
                contractId = hc.getID();
            }
            return "backContract";

        }
        return SUCCESS;

    }

    public String selfreSForward() {
        return "selfr";
    }

    public String back() {
        return "back";
    }

    public ClauseManage getClauseManage() {
        return clauseManage;
    }

    public void setClauseManage(ClauseManage clauseManage) {
        this.clauseManage = clauseManage;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public List<HtlPreconcertItemTemplet> getClausesList() {
        return clausesList;
    }

    public void setClausesList(List<HtlPreconcertItemTemplet> clausesList) {
        this.clausesList = clausesList;
    }

    public void setContractList(List<HtlContract> contractList) {
        this.contractList = contractList;
    }

    public List<HtlContract> getContractList() {
        return contractList;
    }

    public HtlBookCaulClause getHtlBookCaulClause() {
        return htlBookCaulClause;
    }

    public void setHtlBookCaulClause(HtlBookCaulClause htlBookCaulClause) {
        this.htlBookCaulClause = htlBookCaulClause;
    }

    public String getClauseRule() {
        return clauseRule;
    }

    public void setClauseRule(String clauseRule) {
        this.clauseRule = clauseRule;
    }

    public Long getModifyFieldId() {
        return modifyFieldId;
    }

    public void setModifyFieldId(Long modifyFieldId) {
        this.modifyFieldId = modifyFieldId;
    }

    public String getModifyField() {
        return modifyField;
    }

    public void setModifyField(String modifyField) {
        this.modifyField = modifyField;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    public String getFalg() {
        return falg;
    }

    public void setFalg(String falg) {
        this.falg = falg;
    }

    public List<HtlBookCaulClause> getHtlBookCaulClauseList() {
        return htlBookCaulClauseList;
    }

    public void setHtlBookCaulClauseList(List<HtlBookCaulClause> htlBookCaulClauseList) {
        this.htlBookCaulClauseList = htlBookCaulClauseList;
    }

    public HtlBookModifyField getHtlBookModifyField() {
        return htlBookModifyField;
    }

    public void setHtlBookModifyField(HtlBookModifyField htlBookModifyField) {
        this.htlBookModifyField = htlBookModifyField;
    }

    public int getHtlBookCaulClauseListNum() {
        return htlBookCaulClauseListNum;
    }

    public void setHtlBookCaulClauseListNum(int htlBookCaulClauseListNum) {
        this.htlBookCaulClauseListNum = htlBookCaulClauseListNum;
    }

}
