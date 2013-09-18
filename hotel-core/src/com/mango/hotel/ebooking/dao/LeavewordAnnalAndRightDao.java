/**
 * 
 */
package com.mango.hotel.ebooking.dao;

import java.util.List;

import com.mango.hotel.ebooking.persistence.LeavewordAnnalBean;
import com.mangocity.util.dao.GenericDAO;

/**
 * @author xiongxiaojun
 *
 */
public interface LeavewordAnnalAndRightDao extends GenericDAO {
	

    /**
     * 根据当前用户返回他可查看的酒店区域
     * 
     * @return(HDQ,HBQ,BBQ,GZQ,ALL,NONE之一)
     */
    public String getHotelAreaByLoginName(String loginName);

    /**
     * 返回同一主题的留言
     * 
     * @param topic
     * @return
     */
    public List<LeavewordAnnalBean> queryTopicMsg(String topic);
    
    /**
     * 根据用户名和用户选定区域查询未读信息
     * @param theAreaLoginerCanCheck 用户区域
     * @param loginName 用户名
     * @return
     */
	public int getUnReadMsgCount(String theAreaLoginerCanCheck);

	/**
	 * 设置指定信息的状态(已读、未锁)
	 * @param msgId
	 * @param operType
	 * @param readId
	 * @param readName
	 */
	public void setMsgHasReadOrLock(long msgId, int operType, String readId, String readName);

}
