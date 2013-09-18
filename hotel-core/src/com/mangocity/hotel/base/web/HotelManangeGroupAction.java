package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hotel.base.manage.HotelManageGroup;
import com.mangocity.hotel.base.persistence.CEntityEvent;
import com.mangocity.hotel.base.persistence.ExMapping;
import com.mangocity.hotel.base.persistence.HtlContactInfo;
import com.mangocity.hotel.base.persistence.HtlGroupChinaHq;
import com.mangocity.hotel.base.persistence.HtlGroupHQ;
import com.mangocity.hotel.base.persistence.HtlGroupOffshootOrgan;
import com.mangocity.hotel.base.persistence.HtlHotelGroup;
import com.mangocity.hotel.base.persistence.HtlNameplate;
import com.mangocity.hotel.base.web.webwork.GenericAction;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.hotel.constant.HotelMappingType;

/**
 */
public class HotelManangeGroupAction extends GenericAction {
    // 集团信息类
    private HtlHotelGroup htlHotelGroup;

    private HtlNameplate htlNameplate;

    private String FORWARD;

    // 跳转的定位参数
    private String refor;

    private long hotel_group_id;

    private HotelManageGroup hotelManageGroup;

    // 分支机构信息类
    private HtlContactInfo htlContactInfo;

    private HtlGroupChinaHq htlGroupChinaHq;

    private HtlGroupHQ htlGroupHQ;

    private long htlNameplateID;

    private long contactInfoID;

    // 分支机构信息列表
    private List<HtlGroupOffshootOrgan> lisHtlGroupOffshootOrgan = 
        new ArrayList<HtlGroupOffshootOrgan>();

    // 集团酒店中国总部联系信息
    private List<HtlGroupChinaHq> lisHtlGroupChinaHq = 
        new ArrayList<HtlGroupChinaHq>();

    // 集团酒店总部联系信息
    private List<HtlGroupHQ> lisHtlGroupHQ = new ArrayList<HtlGroupHQ>();

    private List lisNameplate = new ArrayList();

    // 酒店集团映射
    private List hotelGPMappingList = new ArrayList();

    // 合作商总个数，用于界面新增按钮的控制
    private final int cooperatorCount = ChannelType.TOTEL_CHANNEL_COUNT;

    private int groupMappingNum;

    /**
     * 合作商开关 add by chenjiajie V2.5@2009-02-05
     */
    private Long cooperatorChecked = Long.valueOf(-1);

    /* ----------重 构 的 新 方 法 开 始---------- */

    public String allForward() {
        if (0 < hotel_group_id) {
            htlHotelGroup = hotelManageGroup.queryHotelManageGroup(hotel_group_id);
            lisHtlGroupOffshootOrgan = hotelManageGroup
                .queryLisHtlGroupOffshootOrgan(hotel_group_id);
            lisNameplate = hotelManageGroup.queryLisNameplate(hotel_group_id);
            /*
             * lisHtlGroupChinaHq = htlHotelGroup.getLisHtlGroupChinaHq(); if
             * (!lisHtlGroupChinaHq.isEmpty()){ htlGroupChinaHq = lisHtlGroupChinaHq.get(0); }
             * lisHtlGroupHQ = htlHotelGroup.getLisHtlGroupHQ(); if (!lisHtlGroupHQ.isEmpty()){
             * htlGroupHQ = lisHtlGroupHQ.get(0); }
             */
            // lisHtlContactInfo = htlHotelGroup.getLisHtlContactInfo();
            htlGroupChinaHq = hotelManageGroup.queryHtlGroupChinaHq(hotel_group_id);
            htlGroupHQ = hotelManageGroup.queryHtlGroupHQ(hotel_group_id);
            /** 查询酒店集团映射记录 V2.5 chenjiajie 2008-12-10 begin **/
            hotelGPMappingList = hotelManageGroup.queryHtlGroupMapping(hotel_group_id,
                new long[] { HotelMappingType.HOTEL_GROUP_TYPE });
            groupMappingNum = hotelGPMappingList.size();
            /** 查询酒店集团映射记录 V2.5 chenjiajie 2008-12-10 end **/
        }

        FORWARD = "query";

        return FORWARD;
    }

    public String saveOrUpdate() {
        if (null != super.getOnlineRoleUser()) {
            htlHotelGroup = (HtlHotelGroup) CEntityEvent.setCEntity(htlHotelGroup, super
                .getOnlineRoleUser().getName(), super.getOnlineRoleUser().getLoginName());
        }
        if (null != htlHotelGroup && null != htlGroupHQ) {
            htlHotelGroup.setHqAddress(htlGroupHQ.getAddress());
            hotelManageGroup.saveOrUpdateHotelManageGroup(htlHotelGroup);
            htlHotelGroup.getID();
            if (null != htlGroupChinaHq) {
                htlGroupChinaHq.setHotelGroup(htlHotelGroup);
                hotelManageGroup.saveOrUpdateHtlGroupChinaHq(htlGroupChinaHq);
            }
            if (null != htlGroupHQ) {
                htlGroupHQ.setHotelGroup(htlHotelGroup);
                hotelManageGroup.saveOrUpdateHtlGroupHQ(htlGroupHQ);
            }
            /** 增加酒店集团映射记录 V2.5 chenjiajie 2008-12-10 begin **/
            List objToSave = new ArrayList(); // 保存批量保存的对象
            Map params = super.getParams();
            // 参数解析
            List lsExMapping = MyBeanUtil.getBatchObjectFromParam(params, ExMapping.class,
                groupMappingNum);
            for (Iterator it = lsExMapping.iterator(); it.hasNext();) {
                ExMapping em = (ExMapping) it.next();
                // 合作方&&合作方对应编码不能为空
                if (null != em.getChanneltype() && 0 < em.getChanneltype()
                    && null != em.getCodeforchannel() && !"".equals(em.getCodeforchannel())) {
                    em.setHotelid(Long.valueOf(0));
                    em.setCode(htlHotelGroup.getID().toString());
                    em.setGroupcode(htlHotelGroup.getGroupCode());
                    em.setType(HotelMappingType.HOTEL_GROUP_TYPE);
                    // 是否启用的判断 V2.5 add by chenjiajie 2009-02-05
                    if (em.getChanneltype().longValue() == cooperatorChecked.longValue()) {
                        em.setIsActive("1");
                    } else {
                        em.setIsActive("0");
                    }
                    objToSave.add(em); // 加入批量保存
                    // 更新某个渠道下酒店集团对应的酒店，房型，价格计划的渠道集团编码
                    hotelManageGroup.saveOrUpdateExMappingGroup(htlHotelGroup.getID().toString(),
                        em.getChanneltype(), em.getCodeforchannel());
                }
            }
            // 批量保存
            hotelManageGroup.saveOrUpdateAll(objToSave);
            /** 增加酒店集团映射记录 V2.5 chenjiajie 2008-12-10 end **/
            FORWARD = refor;
        }
        if (null == FORWARD) {
            FORWARD = super.SUCCESS;
        }
        return FORWARD;
    }

    /* ----------重 构 的 新 方 法 结 束---------- */
    public String addForward() {
        FORWARD = "addInfo";
        return FORWARD;
    }

    public String updateForward() {
        htlHotelGroup = hotelManageGroup.queryHotelManageGroup(hotel_group_id);
        // lisHtlContactInfo = htlHotelGroup.getLisHtlContactInfo();
        refor = "update";
        FORWARD = "update";
        return FORWARD;
    }

    public String delForwardHML() {
        htlHotelGroup.setID(hotel_group_id);
        hotelManageGroup.upHotelManageGroup(htlHotelGroup);
        FORWARD = "delHML";
        return FORWARD;
    }

    public String upForwardHML() {
        htlHotelGroup.setID(hotel_group_id);
        hotelManageGroup.upHotelManageGroup(htlHotelGroup);
        FORWARD = "upHML";
        return FORWARD;
    }

    public String addHotelManageGroup() {
        if ("addhml".equals(refor)) {
            hotelManageGroup.createHotelManageGroup(htlHotelGroup);
            hotel_group_id = htlHotelGroup.getID().longValue();
            FORWARD = refor;
        } else {
            FORWARD = "success";

        }
        return FORWARD;
    }

    public String updateHotelManangeGroup() {
        htlHotelGroup.setID(hotel_group_id);
        hotelManageGroup.upHotelManageGroup(htlHotelGroup);
        FORWARD = refor;
        return FORWARD;
    }

    public String queryHotelManageGroup() {
        htlHotelGroup = hotelManageGroup.queryHotelManageGroup(hotel_group_id);
        // lisHtlContactInfo = htlHotelGroup.getLisHtlContactInfo();
        if ("addhmlup".equals(refor)) {
            refor = "update";
            FORWARD = "update";
        } else {
            FORWARD = "query";
        }
        return FORWARD;
    }

    public String delHotelManageGroup() {
        hotelManageGroup.delHotelManageGroup(hotel_group_id);
        return SUCCESS;
    }

    public String getFORWARD() {
        return FORWARD;
    }

    public void setFORWARD(String forward) {
        FORWARD = forward;
    }

    public HtlHotelGroup getHtlHotelGroup() {
        return htlHotelGroup;
    }

    public void setHtlHotelGroup(HtlHotelGroup htlHotelGroup) {
        this.htlHotelGroup = htlHotelGroup;
    }

    public HtlNameplate getHtlNameplate() {
        return htlNameplate;
    }

    public void setHtlNameplate(HtlNameplate htlNameplate) {
        this.htlNameplate = htlNameplate;
    }

    public HotelManageGroup getHotelManageGroup() {
        return hotelManageGroup;
    }

    public void setHotelManageGroup(HotelManageGroup hotelManageGroup) {
        this.hotelManageGroup = hotelManageGroup;
    }

    public HtlContactInfo getHtlContactInfo() {
        return htlContactInfo;
    }

    public void setHtlContactInfo(HtlContactInfo htlContactInfo) {
        this.htlContactInfo = htlContactInfo;
    }

    public long getHotel_group_id() {
        return hotel_group_id;
    }

    public void setHotel_group_id(long hotel_group_id) {
        this.hotel_group_id = hotel_group_id;
    }

    public String getRefor() {
        return refor;
    }

    public void setRefor(String refor) {
        this.refor = refor;
    }

    public long getContactInfoID() {
        return contactInfoID;
    }

    public void setContactInfoID(long contactInfoID) {
        this.contactInfoID = contactInfoID;
    }

    public List<HtlGroupOffshootOrgan> getLisHtlGroupOffshootOrgan() {
        return lisHtlGroupOffshootOrgan;
    }

    public void setLisHtlGroupOffshootOrgan(List<HtlGroupOffshootOrgan> lisHtlGroupOffshootOrgan) {
        this.lisHtlGroupOffshootOrgan = lisHtlGroupOffshootOrgan;
    }

    public HtlGroupChinaHq getHtlGroupChinaHq() {
        return htlGroupChinaHq;
    }

    public void setHtlGroupChinaHq(HtlGroupChinaHq htlGroupChinaHq) {
        this.htlGroupChinaHq = htlGroupChinaHq;
    }

    public HtlGroupHQ getHtlGroupHQ() {
        return htlGroupHQ;
    }

    public void setHtlGroupHQ(HtlGroupHQ htlGroupHQ) {
        this.htlGroupHQ = htlGroupHQ;
    }

    public List<HtlGroupChinaHq> getLisHtlGroupChinaHq() {
        return lisHtlGroupChinaHq;
    }

    public void setLisHtlGroupChinaHq(List<HtlGroupChinaHq> lisHtlGroupChinaHq) {
        this.lisHtlGroupChinaHq = lisHtlGroupChinaHq;
    }

    public List<HtlGroupHQ> getLisHtlGroupHQ() {
        return lisHtlGroupHQ;
    }

    public void setLisHtlGroupHQ(List<HtlGroupHQ> lisHtlGroupHQ) {
        this.lisHtlGroupHQ = lisHtlGroupHQ;
    }

    public List getLisNameplate() {
        return lisNameplate;
    }

    public void setLisNameplate(List lisNameplate) {
        this.lisNameplate = lisNameplate;
    }

    public long getHtlNameplateID() {
        return htlNameplateID;
    }

    public void setHtlNameplateID(long htlNameplateID) {
        this.htlNameplateID = htlNameplateID;
    }

    public List getHotelGPMappingList() {
        return hotelGPMappingList;
    }

    public void setHotelGPMappingList(List hotelGPMappingList) {
        this.hotelGPMappingList = hotelGPMappingList;
    }

    public int getCooperatorCount() {
        return cooperatorCount;
    }

    public int getGroupMappingNum() {
        return groupMappingNum;
    }

    public void setGroupMappingNum(int groupMappingNum) {
        this.groupMappingNum = groupMappingNum;
    }

    public Long getCooperatorChecked() {
        return cooperatorChecked;
    }

    public void setCooperatorChecked(Long cooperatorChecked) {
        this.cooperatorChecked = cooperatorChecked;
    }

}
