package com.mango.hotel.ebooking.service;

import java.io.Serializable;
import java.util.List;

import com.mango.hotel.ebooking.persistence.LeavewordAnnalBean;
import com.mango.hotel.ebooking.persistence.RightManageBean;

/**
 * EBooking留言板和酒店区域查看权限业务接口
 * 
 * @author chenjuesu
 * 
 */
public interface ILeavewordAnnalAndRightService extends Serializable {

    /**
     * 根据ID返回留言实体
     * 
     * @param id
     * @return
     */
    public LeavewordAnnalBean getLeaveWordById(Serializable id);

    /**
     * 保存留言实体
     * 
     * @param id
     */
    public void saveLeavewordAnnal(LeavewordAnnalBean leaveAnnal);

    /**
     * 删除留言实体
     * 
     * @param id
     */
    public void delLeavewordAnnalById(Serializable id);

    /**
     * 删除一个酒店查看权限人员
     * 
     * @param rmb
     */
    public void delRightMemberById(long rmbId);

    /**
     * 增加一个酒店查看权限人员
     * 
     * @param rmb
     */
    public void addRightMember(RightManageBean rmb);

    /**
     * 根据当前用户返回他可查看的酒店区域
     * 
     * @return(HDQ,HBQ,BBQ,GZQ,ALL-全国,NONE-无权限)之一
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
     * 根据酒店ID，返回酒店名称
     * @param hotelIds
     * @return
     */
	public String getHotelNamesByIds(String hotelIds);
	/**
	 * 查看此人的未读留言个数
	 * @param theAreaLoginerCanCheck
	 * @param loginName
	 * @return
	 */
	public int getUnReadMsgCount(String theAreaLoginerCanCheck);
	
    /**
     * 跟新留言实体类
     * @param leavewordAnnal 留言实体对象
     */
	public void updateLeavewordAnnal(LeavewordAnnalBean leavewordAnnal);
	/**
	 * 
	 * @param msgId 信息ID
	 * @param operType 操作类型,1,设置已读，2,设置未锁
	 * @param readName 
	 * @param readId 
	 */
	public void setMsgHasReadOrLock(long msgId,int operType, String readId, String readName);

}
