package com.mangocity.hotel.base.manage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.assistant.RoomStateBean;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlQuotaNew;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.service.assistant.RoomBedRecord;
import com.mangocity.hotel.user.UserWrapper;

/**
 */
public interface RoomStateManage {
    /**
     * 保存房间房态
     * 
     * @return
     */
    public int saveRoomState(RoomStateBean roomStateBean);

    /**
     * 记录房态操作历史记录
     */
    // public Long logRoomStatusProcess(HtlRoomStatusProcess rsp);
    /**
     * 更新房态
     * 
     * @param 参数roomStateBean是记录日志用的
     *            ，用AOP记录
     * @param roomStateMap
     *            是一个hashMap，其中的值的形式为 key=房间Id value =房间状态(1:2/2:3)第一个1表示床型,":"表示床形与床型的状态，"/"分开床型与床型
     * @param lstTempQuota
     *            临时配额
     */
    public void batchUpdateRoomStatus(RoomStateBean roomStateBean, HashMap roomStateMap,
        List lstTempQuota);

    /**
     * 查询已经关房的时间段
     * 
     * @param hotelId
     *            酒店id
     * @return
     */
    public List selectRoomCloseDate(long hotelId);
    
    
    /**
     * 批量更新临时配额
     * add by haibo.li 2.9.3配额改造,改成往新表更新
     * @param quotaNew
     * @return
     */
    public String batchUpdateTempQuota(List quotaNew);
    
    /**
     * 批量更新临时配额
     * add by haibo.li 2.9.3配额改造,改成往新表更新
     * @param quotaNew
     * @return
     */
    public void UpdateTempQuota(String idLst);

    /**
     *根据配额总表ID，查询对应的配额信息hotel2.9.3 add by shengwei.zuo 2009-10-19
     */
    public HtlQuotaNew findQuotaNewInfoById(Long  id);
    
    /**
     *修改对应的配额信息hotel2.9.3 add by shengwei.zuo 2009-10-19
     */
    public int  modifyQuotaNewInfo(List quotaNewLst);
    /**
     * 新房控按时段调整房态 addby jeusuchen
     * @param record
     */
	public int saveRoomStateBatch(RoomBedRecord record);
    
	/**
     *异步保存交接事项和是否主动报房态 add by zhijie.gu 2010-01-05
     *
    */
	public void saveRoomStateProcess(RoomStateBean roomStateBean);
	
	public void sendRoomStateFull(List list,HtlHotel hotel,UserWrapper user,Integer mailType);

	public Map queryCCSetRoomDateList(long hotelID, List<HtlRoomtype> list, int pageNo, int pageSize);
	
    
}
