package com.mango.hotel.ebooking.dao.impl;

import java.util.List;

import com.mango.hotel.ebooking.dao.LeavewordAnnalAndRightDao;
import com.mango.hotel.ebooking.persistence.LeavewordAnnalBean;
import com.mango.hotel.ebooking.persistence.RightManageBean;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

/**
 * EBooking留言板和区域权限操作DAO
 * 
 * @author chenjuesu
 * 
 */
public class LeavewordAnnalAndRightDaoImpl extends GenericDAOHibernateImpl implements LeavewordAnnalAndRightDao{

    /** 根据当前用户返回他可查看的酒店区域 */
    public String getHotelAreaByLoginName(String loginName) {
        // 设置登陆人能查看的区域以作筛选留言用
        String hql = "from RightManageBean where memberId=?";
        List<RightManageBean> rights = super.query(hql, new Object[]{loginName});
        if (null != rights && 0 < rights.size()) {
            String areaCode = rights.get(0).getArea();
            return areaCode;
        }
        return "NONE";// 此用户无权限给ebooking留言
    }

    /** 返回同一主题的留言 */
    public List<LeavewordAnnalBean> queryTopicMsg(String topic) {
        String hql = "from LeavewordAnnalBean leaveword where leaveword.topic = ?"
        	+" order by leaveword.operationdate desc";
        
        return query(hql, new Object[]{topic});
    }

    /** 根据用户选定区域查询未读信息 */
	public int getUnReadMsgCount(String theAreaLoginerCanCheck) {
		String hql = null;
		Object[] paramValues = null;
		if("ALL".equals(theAreaLoginerCanCheck)){
			hql = "select count(leaveWordID) from LeavewordAnnalBean where addressee = '0' and hasRead = 0";
		}else{
			hql = "select count(leaveWordID) from LeavewordAnnalBean l,HtlHotel h ,HtlArea a where" 
				+" l.addresser = h.ID and h.city = a.cityCode and a.areaCode = ?" 
				+" and addressee = '0' and hasRead = 0";
			paramValues = new Object[]{theAreaLoginerCanCheck};
		}
		List<Long> result = super.query(hql,paramValues);
		
		return result.isEmpty()?0:result.get(0).intValue();
	}

	/** 设置指定信息状态 */
	public void setMsgHasReadOrLock(long msgId, int operType, String readId, String readName) {
		String hql = null;
		Object[] values = null;
		//operType 操作类型,1,设置已读，2,设置未锁
		switch (operType) {
		case 1:
			hql = "update LeavewordAnnalBean set hasRead = 1 ,hasReadId = ?,hasReadName = ?,hasReadTime = sysdate" +
					" where leaveWordID = ?";
			values = new Object[]{readId,readName,msgId};
			break;
		case 2:
			hql = "update LeavewordAnnalBean set hasLock = 0 where leaveWordID = ?";
			values = new Object[]{msgId};
			break;
		}
		super.updateByQL(hql, values);
	}

}
