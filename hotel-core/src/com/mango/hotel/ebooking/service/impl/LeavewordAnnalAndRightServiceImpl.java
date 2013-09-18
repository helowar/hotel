package com.mango.hotel.ebooking.service.impl;

import java.io.Serializable;
import java.util.List;

import com.mango.hotel.ebooking.dao.LeavewordAnnalAndRightDao;
import com.mango.hotel.ebooking.persistence.LeavewordAnnalBean;
import com.mango.hotel.ebooking.persistence.RightManageBean;
import com.mango.hotel.ebooking.service.ILeavewordAnnalAndRightService;
import com.mangocity.hotel.base.dao.HtHotelDao;
import com.mangocity.util.StringUtil;

/**
 * EBooking留言板和酒店区域查看权限业务实现类
 * 
 * @author chenjuesu
 * 
 */
public class LeavewordAnnalAndRightServiceImpl implements ILeavewordAnnalAndRightService {

	/**
	 * 留言和权限DAO
	 */
    private LeavewordAnnalAndRightDao leaAndRightDao;
    
    /**
     * 酒店DAO
     */
    private HtHotelDao hotelDao;

    /**
     * 根据ID返回留言实体
     * @param id
     * @return
     */
    public LeavewordAnnalBean getLeaveWordById(Serializable id) {
        return leaAndRightDao.get(LeavewordAnnalBean.class, id);
    }

    /**
     * 保存留言实体
     * @param id
     */
    public void saveLeavewordAnnal(LeavewordAnnalBean leaveAnnal) {
        leaAndRightDao.save(leaveAnnal);
    }
    /**
     * 更新留言实体
     * @param id
     */
    public void updateLeavewordAnnal(LeavewordAnnalBean leaveAnnal) {
        leaAndRightDao.update(leaveAnnal);
    }
    /**
     * 删除留言实体
     * @param id
     */
    public void delLeavewordAnnalById(Serializable id) {
        leaAndRightDao.remove(LeavewordAnnalBean.class, id);
    }

    /**
     * 删除一个酒店查看权限人员
     * @param rmb
     */
    public void delRightMemberById(long rmbId) {
        leaAndRightDao.remove(RightManageBean.class, rmbId);
    }

    /**
     * 增加一个酒店查看权限人员
     * @param rmb
     */
    public void addRightMember(RightManageBean rmb) {
        leaAndRightDao.save(rmb);
    }

    /**
     * 根据当前用户返回他可查看的酒店区域
     * @return(HDQ,HBQ,BBQ,GZQ,ALL,NONE之一)
     */
    public String getHotelAreaByLoginName(String loginName) {
        return leaAndRightDao.getHotelAreaByLoginName(loginName);
    }

    /**
     * 返回同一主题的留言
     * @param topic
     * @return
     */
    public List<LeavewordAnnalBean> queryTopicMsg(String topic) {
        return leaAndRightDao.queryTopicMsg(topic);
    }
    
    /** 根据酒店ID字符串得到酒店中文名称数组 */
    public String getHotelNamesByIds(String hotelIds) {
		hotelIds = hotelIds.substring(0, hotelIds.length() - 1);
		Object[] objs = hotelDao.getHotelNamesByIds(hotelIds);
		return StringUtil.describe(objs);
	}

	public int getUnReadMsgCount(String theAreaLoginerCanCheck) {
		return leaAndRightDao.getUnReadMsgCount( theAreaLoginerCanCheck);
	}

	public void setMsgHasReadOrLock(long msgId,int operType,String readId,String readName) {
		leaAndRightDao.setMsgHasReadOrLock(msgId,operType, readId, readName);
	}

    public LeavewordAnnalAndRightDao getLeaAndRightDao() {
        return leaAndRightDao;
    }

    public void setLeaAndRightDao(LeavewordAnnalAndRightDao leaAndRightDao) {
        this.leaAndRightDao = leaAndRightDao;
    }

	public void setHotelDao(HtHotelDao hotelDao) {
		this.hotelDao = hotelDao;
	}
}
