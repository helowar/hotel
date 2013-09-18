package com.mangocity.hotel.newroomcontrol.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlRoomControlWorkSchedule;
import com.mangocity.hotel.base.persistence.HtlRoomcontrolHotelSchedule;
import com.mangocity.hotel.base.persistence.HtlRoomcontrolSorting;
import com.mangocity.hotel.base.persistence.HtlRoomcontrolWorkstation;
import com.mangocity.hotel.newroomcontrol.service.assistant.NewRoomControlFaxInfo;
import com.mangocity.hotel.user.UserWrapper;

/**
 * 房控改版Service 接口
 */
public interface NewRoomControlService{
	
	/**
	 * 根据查询条件查询当天上班人员信息。默认查询条件日期为今天
	 * @param personId
	 * @param personIdName
	 * @param queryDate
	 * @param queryAreaGroup
	 * @return
	 * add by zhijie.gu
	 */
	public List searchRoomControlPerson(String personId,String personIdName,String queryBeginDate,String queryEndDate,String queryAreaGroup);
	
	/**
	 * 新房态新建批量增加上班人员时，拿出所有员工信息和今天上班每个人员对应处理的区域
	 * @return
	 * add by zhijie.gu
	 */
	public List queryAllWorkSchedule();
	

	/**
	 * 新房态批量保存员工排班信息
	 * @return
	 * add by zhijie.gu
	 */
	public long saveOrUpdateRoomSchedule(HtlRoomControlWorkSchedule roomControlWorkSchedule) throws IllegalAccessException, InvocationTargetException;
    
	/**
	 * 根据workSchedualId删除某个员工排班信息
	 * @param workSchedualId
	 * @return
	 * @author zhijie.gu
	 */
    public int deleteWorkerSchedule(long workSchedualId ,String loginName,String chnName);
    
    /**
	 * 根据workSchedualId获取某个员工排班信息
	 * @param workSchedualId
	 * @return
	 * @author zhijie.gu
	 */
    public HtlRoomControlWorkSchedule getOneWorkerSchedule(long workSchedualId);
	
	/**
	 * 完成或释放操作时
	 * @param hotelScheduleId
	 * @param user
	 * @param changeContent
	 * @param optType 操作类型，1：完成 2,：释放 3,工作状态关闭时退出的
	 * @param relaxreason 释放原因：1：电话未通 2：负责人不在 3：稍后提供
	 */
	public void completeOrReleaseOptLogs(String hotelScheduleId,UserWrapper user,String changeContent,int optType,Integer relaxreason, Integer priTime,String dealWithSource);
  
	/**
	 * 保存房态时，记录操作日志
	 * @param hotelScheduleId
	 * @param user
	 * @param changeContent
	 */
	public void saveRoomStatusOptLogs(String hotelScheduleId,UserWrapper user,String changeContent);
	
	/**
	 * 为用户自动分配一条酒店记录
	 * @param user
	 * @return
	 */
	public boolean allotHotelSchedule(UserWrapper user);
	
	/**
	 * 关闭工作状态时，把工作档案里未处理的酒店退出
	 * @param user
	 * addby chenjuesu at 2009-12-30上午10:41:29
	 */
	public void clearUpHotelScheduleInMyWorkSpace(UserWrapper user);
	
	/**
	 * 用户自动手工获取一条酒店记录
	 * @param user
	 * @return
	 */
	public boolean allotHotelScheduleByHand(HtlRoomcontrolHotelSchedule hotelSchedule,UserWrapper user);
	
	/**
	 * 根据ID获取酒店列表实体
	 * @param hotelScheduleId
	 * @return
	 * @author chenjuesu
	 */
	public HtlRoomcontrolHotelSchedule getRoomcontrolHotelScheduleById(String hotelScheduleId);
	/**
	 * 跟据登陆名取得其现分配到的酒店
	 * @param loginName
	 * @return
	 * addby chenjuesu at 2009-12-31下午02:59:42
	 */
	public List queryHotelScheduleByLoginName(String loginName);
	/**
	 * 跟据用户查找此时的工作状态
	 * @param loginName
	 * @return
	 * addby chenjuesu at 2010-1-7下午05:17:10
	 */
	public HtlRoomcontrolWorkstation findWorkStationBy(UserWrapper user);
	/**
	 * 查看今天 所有员工的工作进度
	 * @param roleUser 
	 * @return
	 * addby chenjuesu at 2010-1-7下午05:34:12
	 */
	public List findAllWorkStations(UserWrapper roleUser);
	
	/**
	 * 获取酒店列表排序字符串 即order by 后面的字符串
	 * @return
	 */
	public String getRoomControlSortingStr();

	
	/**
	 * 获取酒店列表排序集合
	 * @return
	 */
	public List<HtlRoomcontrolSorting> getRoomControlSortingList();
	
	/**
	 * 批量保存排序集合
	 * @param htlRoomcontrolSortingList
	 */
	public void updateAllSorting(List<HtlRoomcontrolSorting> htlRoomcontrolSortingList);
	public String getRelaxReason(int relaxreason);

	public void completeAndUnlock(String hotelScheduleId, UserWrapper roleUser, int optType, Integer relaxreason, Integer priTime,String dealWithSource);

	public String findWorkareasByUser(String loginName);
	
	/**
	 * 延时操作新增一条交接事项 add by zhijie.gu
	 */
	public void saveProcessRemarkAndIsRoomStatus(long hotelId ,UserWrapper roleUser,int relaxreason ,int priTime, long htlRoomStatusProcessID ) ;
	
	/**
	 * 查询房态操作日志
	 * @param hotelId
	 */
	public List queryRommControlLog(long hotelId,Date beginDate,Date endDate,int pageNumber);
	
	/**
     * 找前一天到现在的所有房态更改记录
     * 
     * @param hotelId
     * @return
     */
	public List findRoomStatusDateProcess(long hotelId) ;
	
	/**
	 * 根据酒店id查找所以的房型+床型的所有数据
	 * @param hotelId
	 * @return
	 */
	public List findAllRoomBedByHotelId(long hotelId);
	
	
	/**
	 * 根据酒店id获取所有的房型数据
	 * @return
	 */
	public Map getRoomTypeIdNameMapByHotelId(long hotelID);
	
	/**
	 * 按日历调整调用方法
	 * 根据房态，查找相应配额是否预警，如果满房或不可超，配额预警的需取消预警，
	 * 如果房态为其他，如果已取消配额预警，需设为配额预警
	 * @return
	 */
	public void changeQuotaWarningByRoomstate(List lstTempQuota,long hotelId);
	
	/**
	 * 批量调整房态调用方法，
	 * 根据房态，查找相应配额是否预警，如果满房或不可超，配额预警的需取消预警，
	 * 如果房态为其他，如果已取消配额预警，需设为配额预警
	 * @return
	 */
	public void changeQuotaWarningByRoomstatePeriod(List lstTempQuota,long hotelId);
	
	public Long sendFaxForRoomcontrol(long hotelId,String faxType,String faxNum,UserWrapper roleUser, 
			NewRoomControlFaxInfo roomControlFaxInfo);
	
	
}
