package com.mangocity.hotel.order.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.CEntityEvent;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.hotel.order.persistence.HtlAuditInfo;
import com.mangocity.hotel.order.persistence.HtlAuditInfoHotel;
import com.mangocity.hotel.order.persistence.HtlAuditInfoSetup;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.bean.MyBeanUtil;

/**
 * 酒店日审信息设置
 * 
 * @author chenkeming
 * 
 */
public class HotelAuditInfoAction extends PersistenceAction {

    private Long ID;

    private String[] hotelID;

    private String selIDs;

    private static final String BATCH_SELECT = "batchSelect";

    /**
     * 查询待促销的酒店调转名称
     */
    private static final String LISTHOTEL = "listHotel";

    /**
     * 查询已关联促销信息的酒店调转名称
     */
    private static final String DELETEPRESALEDHOTEL = "deletePresaledHotel";

    private int lsAuditSetupwNum;

    protected Class getEntityClass() {
        return HtlAuditInfo.class;
    }

    /**
     * 查询待促销的酒店
     * 
     * @return
     */
    public String listHotel() {

        return LISTHOTEL;
    }

    /**
     * 删除酒店与渠道信息的关联
     * 
     * @return
     */
    public String deleteAuditInfoHotel() {

        return DELETEPRESALEDHOTEL;
    }

    public String batchSelect() {

        List hotels = new ArrayList();
        // 循环赋值
        hotelID = selIDs.split(",");
        for (int i = 0; i < hotelID.length; i++) {
            HtlAuditInfoHotel hotel = new HtlAuditInfoHotel();
            hotel.setAuditInfoId(ID);
            hotel.setHotelId(Long.valueOf(hotelID[i]));
            if (null != super.getOnlineRoleUser()) {
                hotel.setCreate_by(super.getOnlineRoleUser().getName());
                hotel.setCreate_by_id(super.getOnlineRoleUser().getLoginName());
            }
            hotels.add(hotel);
        }
        super.getEntityManager().saveOrUpdateAll(hotels);

        return super.forwardMsgBox("审核渠道添加酒店成功！", "refreshSelf()");
    }

    /**
     * 新增或修改日审信息
     * 
     * @return
     */
    public String saveOrUpdate() {

        super.setEntity(super.populateEntity());

        Map params = super.getParams();
        UserWrapper roleUser = super.getOnlineRoleUser();

        List lisAuditSetup = MyBeanUtil.getBatchObjectFromParam(params, HtlAuditInfoSetup.class,
            lsAuditSetupwNum);
        HtlAuditInfo auditInfo = (HtlAuditInfo) getEntity();
        List lisAuditSetupTemp = auditInfo.getLstSetup();
        Map existMap = new HashMap();
        for (int j = 0; j < lisAuditSetupTemp.size(); j++) {
            existMap.put(((HtlAuditInfoSetup) lisAuditSetupTemp.get(j)).getSetupId(), "0");
        }
        HtlAuditInfoSetup setup = null;
        for (int i = 0; i < lisAuditSetup.size(); i++) {
            HtlAuditInfoSetup as = (HtlAuditInfoSetup) lisAuditSetup.get(i);
            boolean bFound = false;
            setup = null;
            for (int j = 0; j < lisAuditSetupTemp.size(); j++) {
                setup = (HtlAuditInfoSetup) lisAuditSetupTemp.get(j);
                if (setup.getSetupId().equals(as.getSetupId())) {
                    bFound = true;
                    break;
                }
            }
            if (!bFound) {
                as.setHtlAuditInfo(auditInfo);
                lisAuditSetupTemp.add(as);
                as.setCreate_by_id(roleUser.getLoginName());
                as.setCreate_by(roleUser.getName());
                as.setCreate_time(new Date());
                as.setModify_by_id(roleUser.getLoginName());
                as.setModify_by(roleUser.getName());
                as.setModify_time(new Date());
            } else {
                existMap.put(as.getSetupId(), "1");
                setup.setWeeks(as.getWeeks());
                setup.setAuditBeginDate(as.getAuditBeginDate());
                setup.setAuditEndDate(as.getAuditEndDate());
                setup.setAuditBeginTime(as.getAuditBeginTime());
                setup.setAuditEndTime(as.getAuditEndTime());
                setup.setAuditType(as.getAuditType());
                setup.setAuditNo(as.getAuditNo());
                setup.setAuditCtName(as.getAuditCtName());
                setup.setAuditCtPhone(as.getAuditCtPhone());
                setup.setAuditApartM(as.getAuditApartM());
                setup.setAuditRemark(as.getAuditRemark());
                setup.setCreate_by_id(roleUser.getLoginName());
                setup.setCreate_by(roleUser.getName());
                setup.setCreate_time(new Date());
                setup.setModify_by_id(roleUser.getLoginName());
                setup.setModify_by(roleUser.getName());
                setup.setModify_time(new Date());
            }
        }
        for (int j = 0; j < lisAuditSetupTemp.size(); j++) {
            int y =j;
            setup = (HtlAuditInfoSetup) lisAuditSetupTemp.get(y);
            if ("0".equals(existMap.get(setup.getSetupId()))) {
                lisAuditSetupTemp.remove(setup);
                y--;
            }
        }

        CEntityEvent.setCEntity(auditInfo, roleUser.getName(), roleUser.getLoginName());

        Serializable resId = super.saveOrUpdate(auditInfo);
        super.setEntityID(Long.valueOf(resId.toString()));
        super.setEntity(super.getEntityManager().find(this.getEntityClass(), this.getEntityID()));

        return SAVE_SUCCESS;
    }

    public String delete() {
        super.delete();
        return super.forwardMsgBox("删除审核渠道成功！", "refreshSelf()");
    }

    public int getLsAuditSetupwNum() {
        return lsAuditSetupwNum;
    }

    public void setLsAuditSetupwNum(int lsAuditSetupwNum) {
        this.lsAuditSetupwNum = lsAuditSetupwNum;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getSelIDs() {
        return selIDs;
    }

    public void setSelIDs(String selIDs) {
        this.selIDs = selIDs;
    }

    public String[] getHotelID() {
        return hotelID;
    }

    public void setHotelID(String[] hotelID) {
        this.hotelID = hotelID;
    }

}
