package com.mangocity.hotel.base.web;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlCtct;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;

/**
 */
public class AddHotelLinkmanAction extends PersistenceAction {

    private Long hotelID;

    private int lsCtctrowNum;

    private String REQUERY = "requery";

    // 酒店联系信息
    private List lsCtctSel;

    private HtlHotel htlHotel;

    private HotelManage hotelManage;
    private String hotelScheduleId;

    protected Class getEntityClass() {
        return HtlCtct.class;
    }

    /**
     * 新增酒店联系人
     * 
     * @return
     */
    public String addHotelLinkman() {
        Map params = super.getParams();
        String hotelID = (String) params.get("hotelID");
        htlHotel = hotelManage.findHotel(Long.valueOf(hotelID));
        lsCtctSel = htlHotel.getHtlCtct();
        // 加入判断(只要联系人部门为财务就从list里面romove掉,然后到页面的是除了财务部门的都显示了) add by haibo.li
        // 2008-12-31
        if (0 < lsCtctSel.size()) {
            for (int i = 0; i < lsCtctSel.size(); i++) {
                HtlCtct hc = (HtlCtct) lsCtctSel.get(i);
                if (StringUtil.isValidStr(hc.getCtctType()) && ("03").equals(hc.getCtctType())) {
                    lsCtctSel.remove(hc);
                }
            }
        }
        return "addHotelLinkman";
    }

    public String batchSave() {

        // 从页面获取到的联系部分信息 add by shengwei.zuo
        List ls = MyBeanUtil.getBatchObjectFromParam(super.getParams(), HtlCtct.class, lsCtctrowNum);
        // 检查联系部分中有财务部的，进行删除
        if (!ls.isEmpty()) {
            for (int z = 0; z < ls.size(); z++) {
                HtlCtct hcc = (HtlCtct) ls.get(z);
                // 生产bug620 当联系部门为空的时候，进入判断会报空指针异常 modify by chenjiajie 2009-07-09
                if (StringUtil.isValidStr(hcc.getCtctType()) && "03".equals(hcc.getCtctType())) {
                    ls.remove(hcc);
                }
            }
        }

        htlHotel = hotelManage.findHotel(hotelID);

        // 由于页面上没有显示联系部门为财务部的记录，ls就没有联系部门为财务部的信息，保存时，就得把联系部门为财务部的信息重新获取到。add by shengwei.zuo
        // hotel2.6 2009-05-25 begin
        // 本部:房态控制中更新了联系人信息并保存，由于房态中不能看到财务部，导致修改之后酒店基本信息中的财务部被删除了 BUG 修复
        List lsThree = htlHotel.getHtlCtct();
        if (!lsThree.isEmpty()) {
            for (int p = 0; p < lsThree.size(); p++) {
                HtlCtct htlCtctThree = (HtlCtct) lsThree.get(p);
                // 找出联系部门为财务部的信息，添加到其中 add by shengwei.zuo
                // 生产bug620 当联系部门为空的时候，进入判断会报空指针异常 modify by chenjiajie 2009-07-09
                if ("03".equals(htlCtctThree.getCtctType())) {
                    ls.add(htlCtctThree);
                    break;
                }
            }
        }

        // 由于页面上没有显示联系部门为财务部的记录，ls就没有联系部门为财务部的信息，保存时，就得把联系部门为财务部的信息重新获取到。add by shengwei.zuo
        // hotel2.6 2009-05-25 end

        for (int k = 0; k < ls.size(); k++) {
            HtlCtct htlCtct = (HtlCtct) ls.get(k);
            htlHotel.setActive(BizRuleCheck.getTrueString());
            if (null != super.getOnlineRoleUser()) {
                htlHotel.setCreateById(super.getOnlineRoleUser().getLoginName());
                htlHotel.setCreateBy(super.getOnlineRoleUser().getName());
            }
            htlCtct.setHtlHotel(htlHotel);
        }

        htlHotel.setHtlCtct(ls);
        hotelManage.saveOrUpdateHotel(htlHotel);
        return REQUERY;
    }

    /**
     * 保存或更新酒店联系人
     * 
     * @return
     */
    public String saveOrUpdateCtct() {
        Map params = super.getParams();
        List lsCtct = MyBeanUtil.getBatchObjectFromParam(params, HtlCtct.class, lsCtctrowNum);
        String hotelID = (String) params.get("hotelID");
        htlHotel = hotelManage.findHotel(Long.valueOf(hotelID));
        for (int i = 0; i < lsCtct.size(); i++) {
            HtlCtct ct = (HtlCtct) lsCtct.get(i);
            ct.setHtlHotel(htlHotel);
        }
        htlHotel.setActive(BizRuleCheck.getHotelActive());
        htlHotel.setHtlCtct(lsCtct);
        hotelManage.modifyHotel(htlHotel);
        return REQUERY;
    }

    public Long getHotelID() {
        return hotelID;
    }

    public void setHotelID(Long hotelID) {
        this.hotelID = hotelID;
    }

    public int getLsCtctrowNum() {
        return lsCtctrowNum;
    }

    public void setLsCtctrowNum(int lsCtctrowNum) {
        this.lsCtctrowNum = lsCtctrowNum;
    }

    public List getLsCtctSel() {
        return lsCtctSel;
    }

    public void setLsCtctSel(List lsCtctSel) {
        this.lsCtctSel = lsCtctSel;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public HtlHotel getHtlHotel() {
        return htlHotel;
    }

    public void setHtlHotel(HtlHotel htlHotel) {
        this.htlHotel = htlHotel;
    }

    public String getREQUERY() {
        return REQUERY;
    }

    public void setREQUERY(String requery) {
        REQUERY = requery;
    }

	public String getHotelScheduleId() {
		return hotelScheduleId;
	}

	public void setHotelScheduleId(String hotelScheduleId) {
		this.hotelScheduleId = hotelScheduleId;
	}

}
