package com.mangocity.hotel.newroomcontrol.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.mangocity.hotel.base.persistence.HtlRoomcontrolHotelSchedule;
import com.mangocity.hotel.base.persistence.HtlRoomcontrolSorting;
import com.mangocity.hotel.base.persistence.HtlRoomcontrolWorkstation;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.collections.FormatMap;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.log.MyLog;

public class NewRoomControlDao extends DAOHibernateImpl{
	private static final MyLog log = MyLog.getLogger(NewRoomControlDao.class);
	private DAOIbatisImpl queryDao;
	
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	/**
	 * 取得指定用户的一条待分配记录
	 * @param loginName
	 * @return
	 * addby chenjuesu at 2009-12-30上午11:42:30
	 */
	public HtlRoomcontrolHotelSchedule allotHotelSchedule(String loginName,Map params) {
		// TODO Auto-generated method stub
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		String basesql = "select * from(select scheduleid,state SCH_STATE from HTL_ROOMCONTROL_HOTEL_SCHEDULE sch	 where sch.SCHEDULEDATE = trunc(sysdate) "
				+" and fnc_match_rm_area_condition('"+loginName
				+"',sch.HOTELCITY,sch.HOTELSTATE) > 0 and sch.ACQUIRESTATE = 0 and sch.HOTELSELLSTATE = 1 and sch.cooperatechannel = 0 and (sch.epricehotel = 0 or sch.epriceaudithotel = 1) ";
		//先分预约的,按时间先后顺序排
		String sql = basesql + " and sch.STATE = 1 and sch.CHECKPOINT <= "+hour+" order by " + getRoomControlSortingStr() +") where rownum <= 1";
		log.info(sql);
		List result = super.doquerySQL(sql ,false);
		if(result.isEmpty()){
			//找不到，再分一般的，按设定的排序
			sql = basesql + "  and sch.STATE = 0 order by " + getRoomControlSortingStr() +") where rownum <= 1";
			log.info(sql);
			result = super.doquerySQL(sql, false);
		}
		HtlRoomcontrolHotelSchedule hotelSchedule = null;
		if(!result.isEmpty()){
			Object[] rets = (Object[])result.get(0);
			Object ret = rets[0];
			hotelSchedule = (HtlRoomcontrolHotelSchedule)super.find(HtlRoomcontrolHotelSchedule.class, Long.valueOf(ret.toString()));
		}
		return hotelSchedule;
	}
	/**
	 * 取得用户分配的酒店列表记录
	 * @param loginName
	 * @param isAutoAlloted 是否获取自动分配的
	 * @return
	 * addby chenjuesu at 2009-12-30上午10:26:50
	 */
	public List checkUserHasAllotHotelSchedule(String loginName,boolean isAutoAlloted) {
		// TODO Auto-generated method stub
		String hql = "From HtlRoomcontrolHotelSchedule where scheduledate = trunc(sysdate) and nowassignedid = ? and acquirestate = 1";
		if(isAutoAlloted)
			hql += " and acquireway = 1";
		hql +=" order by acquireway,state";
		return super.doquery(hql, loginName, false) ;
	}

	
	public DAOIbatisImpl getQueryDao() {
		return queryDao;
	}
	public void setQueryDao(DAOIbatisImpl queryDao) {
		this.queryDao = queryDao;
	}
	/**
	 * 跟据登陆名取得其现分配到的酒店，用ibatis查询的
	 * @param loginName
	 * @return
	 * addby chenjuesu at 2009-12-31下午02:59:42
	 */
	public List queryHotelScheduleByLoginName(String loginName) {
		// TODO Auto-generated method stub
		Map params = new FormatMap();
		params.put("loginName", loginName);
		params.put("getMyList", "true");
		return queryDao.queryForList("newSelectRoomState", params);
	}
	/**
	 * 在查看工作进度时，应该要先搞下这个更新
	 * @param loginName
	 * addby chenjuesu at 2010-1-7下午05:37:43
	 */
	public void updateWorkstation(String loginName){
		CallableStatement cstmt = null;
		Connection con = null;
		String procedureName = "{call PRC_NEWROOMCONTROLWORKSTATION(?)}";
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			cstmt = con.prepareCall(procedureName);
			cstmt.setString(1, loginName);
			cstmt.executeUpdate();
		} catch (DataAccessResourceFailureException e) {
			log.error(e.getMessage(),e);
		} catch (HibernateException e) {
			log.error(e.getMessage(),e);
		} catch (IllegalStateException e) {
			log.error(e.getMessage(),e);
		} catch (SQLException e) {
			log.error(e.getMessage(),e);
		}finally{
    		if(cstmt!=null){
    			try {
					cstmt.close();
				} catch (SQLException e) {
					log.error(e.getMessage(),e);
				}
    		}
    		if(con!=null){
    			try {
					con.close();
				} catch (SQLException e) {
					log.error(e.getMessage(),e);
				}
    		}
    	}
	}
	
	public HtlRoomcontrolWorkstation findMyWorkStation(UserWrapper user) {
		// TODO Auto-generated method stub
		HtlRoomcontrolWorkstation workstation = null;
		String hql = "from HtlRoomcontrolWorkstation where operatorid = ? and auditdate = trunc(sysdate)";
		List result = super.doquery(hql, user.getLoginName(), false);
		if(result.isEmpty()){
			workstation = new HtlRoomcontrolWorkstation();
			workstation.setAuditdate(DateUtil.getDate(new Date()));
			workstation.setOperatorid(user.getLoginName());
			workstation.setOperatorname(user.getName());
			//得到班次
			workstation.setOndutytime(getCurrentOndutyTime(user));
		}else{
			workstation = (HtlRoomcontrolWorkstation)result.get(0);
		}
		return workstation;
	}
	/**
	 * 如果是管理员,则返回全部员工的,否则只返回本人的
	 * @param roleUser
	 * @return
	 * addby chenjuesu at 2010-1-9下午02:46:51
	 */
	public List findAllWorkStations(UserWrapper roleUser) {
		// TODO Auto-generated method stub
		if(roleUser.isRSCAdmin()){//如果不是管理员,则只能查看自己的进度
			String hql = "from HtlRoomcontrolWorkstation where auditdate = trunc(sysdate)";
			return super.doquery(hql,false);
		}else{
			List result = new ArrayList();
			HtlRoomcontrolWorkstation workstation = findMyWorkStation(roleUser);
			result.add(workstation);
			return result;
		}
	}
	public String getCurrentOndutyTime(UserWrapper user){
		String hql = "select ws.onDutyTime from HtlRoomControlWorkSchedule ws where ws.loginName =? " +
				"and ws.beginDate <= trunc(?) and ws.endDate >= trunc(?) and ws.active='1'";
		Date today = DateUtil.getDate(new Date());
		List result = super.doquery(hql, new Object[]{user.getLoginName(),today,today}, false);
		if(!result.isEmpty())
			return String.valueOf(result.get(0));
		return null;
	}
	/**
	 * 酒店排序设置
	 * @return
	 */
	public List<HtlRoomcontrolSorting> findConditionSorting(){
		String hql = " from HtlRoomcontrolSorting where sorting > 0 order by sorting";
		List<HtlRoomcontrolSorting> htlRoomcontrolSortingList = super.doquery(hql,false);
		return htlRoomcontrolSortingList;
	}
	
	/**
	 * 查询房态酒店列表排序
	 * @return
	 */
	public List<HtlRoomcontrolSorting> findConditionSorting(String allCondition){
		String hql = " from HtlRoomcontrolSorting order by sorting";
		List<HtlRoomcontrolSorting> htlRoomcontrolSortingList = super.doquery(hql,false);
		return htlRoomcontrolSortingList;
	}
	
	/**
	 * 获取酒店列表排序字符串 即order by 后面的字符串
	 * @return
	 */
	public String getRoomControlSortingStr(){
		String allCondition ="allCondition";
		List<HtlRoomcontrolSorting> htlRoomcontrolSortingList = findConditionSorting(allCondition);
		String sortingStr = null;
		if(null != htlRoomcontrolSortingList && !htlRoomcontrolSortingList.isEmpty()){
			StringBuffer sortingStrBuf = new StringBuffer();
			int size = htlRoomcontrolSortingList.size();
			for (int i = 0; i < size; i++) {
				HtlRoomcontrolSorting htlRoomcontrolSorting = htlRoomcontrolSortingList.get(i);
				sortingStrBuf.append(htlRoomcontrolSorting.getValue());
				if(StringUtil.isValidStr(htlRoomcontrolSorting.getSortingType())){
					sortingStrBuf.append(" " + htlRoomcontrolSorting.getSortingType());
				}
				if(i < size - 1){
					sortingStrBuf.append(",");
				}
			}
			sortingStr = sortingStrBuf.toString();
		}
		return sortingStr;
	}
	
	public void updateQuotaWarningByRoomState(long hotelId,String roomTypeIdStr,String bedTypeIdStr,String roomStateStr,String ableSaleDateStr){
		CallableStatement cstmt = null;
		Connection con = null;
		String procedureName = "{call PROC_ROOMCONTROL_QUOTAWARNING(?,?,?,?,?)}";
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			cstmt = con.prepareCall(procedureName);
			cstmt.setString(1,String.valueOf(hotelId));
			cstmt.setString(2,roomTypeIdStr);
			cstmt.setString(3,bedTypeIdStr);
			cstmt.setString(4,roomStateStr);
			cstmt.setString(5,ableSaleDateStr);
			cstmt.executeUpdate();
		} catch (DataAccessResourceFailureException e) {
			log.error(e.getMessage(),e);
		} catch (HibernateException e) {
			log.error(e.getMessage(),e);
		} catch (IllegalStateException e) {
			log.error(e.getMessage(),e);
		} catch (SQLException e) {
			log.error(e.getMessage(),e);
		}finally{
    		if(cstmt!=null){
    			try {
					cstmt.close();
				} catch (SQLException e) {
					log.error(e.getMessage(),e);
				}
    		}
    		if(con!=null){
    			try {
					con.close();
				} catch (SQLException e) {
					log.error(e.getMessage(),e);
				}
    		}
    	}
	}
	/**
	 * 当房控保存房态时，如果此酒店有CC设置满房或关房，则设置为已阅读
	 * @param hotelID
	 * @param user
	 */
	public void setCCSetRoomStateReviewed(Long hotelID,UserWrapper user){
		String hql = "update HtlRoomstateCc set processdate = sysdate,reviewremark='房控保存时设置review',reviewdept='本部',reviewname=?," +
				"reviewid=?,reviewstate='1' where hotelid=? and reviewstate = '0' and processdatetime > trunc(sysdate - 2)";
		Object[] values = new Object[]{user.getName(),user.getLoginName(),hotelID};
		super.doUpdateBatch(hql, values);
	}
	
	/**
     * 房太操作日志分页用
     * maxSize 固定为10,房控专用
     */
    public List pageListForRoomstateLogs(final int startIndex, final int maxSize,long hotelId,String beginDate,String endDate) {
    	
    	StringBuffer queryStringBuf = new StringBuffer();
        queryStringBuf.append("select lo.logid,lo.operatorname,to_char(lo.finishtime,'yyyy-mm-dd hh24:mi:ss'),lo.dealWithSource from ");
        queryStringBuf.append("HtlRoomcontrolOperationLogs lo "+" where lo.logid in(");
        queryStringBuf.append("select ol.logid from HtlRoomcontrolOperationLogs ol , HtlRoomcontrolOptsublogs op where ol.hotelid = "+hotelId);
        queryStringBuf.append(" and ol.logid =op.optid and ol.finishtime is not null and trunc(ol.finishtime) >= to_date('"+beginDate+"','yyyy-MM-dd') and trunc(ol.finishtime) <= to_date('"+endDate+"','yyyy-MM-dd') group by ol.logid ) order by  lo.finishtime desc");
    
        final String queryString = queryStringBuf.toString();
        final HibernateTemplate template = getHibernateTemplate();
        return template.executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query queryObject = session.createQuery(queryString);
                queryObject.setFirstResult(startIndex);
                queryObject.setMaxResults(maxSize);
                List list = queryObject.list(); 
                return list;
            }
        });
    }
}
