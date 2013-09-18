package com.mangocity.hotel.base.manage.impl;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.constant.AreaType;
import com.mangocity.hotel.base.dao.IQueryQuotaDao;
import com.mangocity.hotel.base.dao.ISaleDao;
import com.mangocity.hotel.base.dao.RoomControlDao;
import com.mangocity.hotel.base.manage.RoomStateManage;
import com.mangocity.hotel.base.manage.assistant.MsgAssist;
import com.mangocity.hotel.base.manage.assistant.RoomStateBean;
import com.mangocity.hotel.base.persistence.HtlArea;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlQuotaJudge;
import com.mangocity.hotel.base.persistence.HtlQuotaLog;
import com.mangocity.hotel.base.persistence.HtlQuotaNew;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.base.persistence.HtlRoomStatusProcess;
import com.mangocity.hotel.base.persistence.HtlRoomcontrolHotelSchedule;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.HtlTempQuota;
import com.mangocity.hotel.base.persistence.HtlTempRoomState;
import com.mangocity.hotel.base.service.assistant.RoomBedRecord;
import com.mangocity.hotel.base.util.HotelStrUtil;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.HotelBaseConstantBean;
import com.mangocity.util.bean.RoomTypeStatus;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.hotel.constant.FaxEmailModel;
import com.mangocity.util.log.MyLog;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Mail;

/**
 */
public class RoomStateManageImpl extends DAOHibernateImpl implements
		RoomStateManage {
	private static final MyLog log = MyLog.getLogger(RoomStateManageImpl.class);
	private RoomControlDao roomControlDao;
	//private RoomControlManage roomControlManage;
	
	private IQueryQuotaDao qryQuotaDao;
	private ISaleDao saleDao;
	private CommunicaterService communicaterService;

	// 表示房态设置为满房，但配额大于0时激发邮件
	private static int MAIL_TYPE_QUOTA_WARNING = 0;

	// 表示房态联系设置满房超过7天时激发邮件
	private static int MAIL_TYPE_FULL_WEEK = 1;

	// 表示房态由freesale更改为满房时激发邮件。
	private static int MAIL_TYPE_FREESALE_TO_FULL = 2;

	// 表示房态为freesale
	private static String ROOMSTATE_FREESALE = "0";

	// 表示房态为满房
	private static String ROOMSTATE_FULL = "4";

	public int saveRoomState(RoomStateBean roomStateBean) {
		RoomStateBean roomSB = new RoomStateBean();
		roomSB.setBegindate(roomStateBean.getBegindate());
		roomSB.setEnddate(roomStateBean.getEnddate());
		roomSB.setHotelID(roomStateBean.getHotelID());
		roomSB.setWeek(roomStateBean.getWeek());

		HashMap<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < roomStateBean.getRoomTypeStatus().size(); i++) {
			RoomTypeStatus roomTS = (RoomTypeStatus) roomStateBean
					.getRoomTypeStatus().get(i);
			String roomStatus = map.get(roomTS.getRoomTypeID());

			if (null == roomTS.getRoomBedStatus()) {
				continue;
			} else {
				if (null == roomStatus) {
					roomStatus = roomTS.getRoomBedId() + ":"
							+ roomTS.getRoomBedStatus();
				} else {
					roomStatus += "/" + roomTS.getRoomBedId() + ":"
							+ roomTS.getRoomBedStatus();
				}
				map.put(roomTS.getRoomTypeID(), roomStatus);
			}

		}

		// 只允许全部更新的代码
		// for (Iterator it = map.keySet().iterator(); it.hasNext();) {
		// String roomTypeID = (String)it.next();
		// String roomStatus = (String)map.get(roomTypeID);
		// roomSB.setRoomTypeID(Long.valueOf(roomTypeID).longValue());
		// roomSB.setRoomStatus(roomStatus);
		// roomControlDao.updateRoomStatu(roomSB);
		// }

		List roomStatuList = roomControlDao.selectRoomStatu(roomSB);

		if (null != roomStatuList) {

			List<HtlRoom> listRoomStatus = new ArrayList<HtlRoom>();
			for (Iterator it = map.keySet().iterator(); it.hasNext();) {
				String roomTypeID = (String) it.next();
				long loogRoomTypeID = Long.valueOf(roomTypeID).longValue();
				String roomStatus = map.get(roomTypeID);
				String[] rs = roomStatus.split("\\/");

				roomSB.setRoomTypeID(loogRoomTypeID);
				roomSB.setRoomStatus(roomStatus);

				long lastRoomTypeID = 0;
				String lastDesRoomStatu = "";
				for (Iterator itr = roomStatuList.iterator(); itr.hasNext();) {
					HtlRoom htlRoom = (HtlRoom) itr.next();

					// 如果getRoomTypeId()与页面提交的loogRoomTypeID相同,并且RoomTypeId
					// 与前面的一个更新的RoomTypeId的相同
					if (htlRoom.getRoomTypeId() == loogRoomTypeID
							&& lastRoomTypeID == htlRoom.getRoomTypeId()) {

						htlRoom.setRoomState(lastDesRoomStatu);
						lastRoomTypeID = htlRoom.getRoomTypeId();

						// htlRomm实例添加到需要更新的listRoomStatus
						listRoomStatus.add(htlRoom);
					} else if (htlRoom.getRoomTypeId() == loogRoomTypeID) {

						String origRoomStatu = htlRoom.getRoomState();
						String desRoomStatu = origRoomStatu;

						// 将房态的字符记录更新成设定的记录
						for (int i = 0; i < rs.length; i++) {
							desRoomStatu = HotelStrUtil.replaceRoomStatus(
									desRoomStatu, rs[i]);
						}
						htlRoom.setRoomState(desRoomStatu);
						// 记录上一次得到的roomTypeId, desRoomStatus
						lastRoomTypeID = htlRoom.getRoomTypeId();
						lastDesRoomStatu = desRoomStatu;
						listRoomStatus.add(htlRoom);

					}

				}
			}

			for (Iterator itr = listRoomStatus.iterator(); itr.hasNext();) {
				HtlRoom htlRoom = (HtlRoom) itr.next();
				// log.info("--  hotelID     :"+htlRoom.getID());
				// log.info("--getRoomState  :"+htlRoom.getRoomState());
				roomControlDao.updateRoomStatuByHtlRoomID(htlRoom);
			}

		}
		// 改用AOP记录日志 zhuangzhineng
		// HtlRoomStatusProcess rsp = new HtlRoomStatusProcess();
		/*
		 * rsp.setHotelId(roomStateBean.getHotelID());
		 * rsp.setProcessBy(roomStateBean.getUserName());
		 * rsp.setProcessById(roomStateBean.getUserId());
		 * rsp.setProcessDate(DateUtil.getSystemDate());
		 * rsp.setProcessRemark(roomStateBean.getProcessRemark());
		 * rsp.setIsRoomStatusReport(Long.valueOf
		 * (roomStateBean.getIsRoomStatusReport()).longValue());
		 * logRoomStatusProcess(rsp);
		 */
		return 0;

	}

	/**
	 * 新房控按时段调整房态
	 * 
	 * @param roomBed
	 * @return
	 */
	public int saveRoomStateBatch(RoomBedRecord roomBed) {
		String[] bedTypes = roomBed.getBeds().split(",");
		Integer bigStatus = roomBed.getBigBedStatus();
		Integer doubleStatus = roomBed.getDoubleBedStatus();
		Integer singleStatus = roomBed.getSingleBedStatus();
		int count = 0;
		String roomStatus = "";
		String roomStatusReg = "";
		// 如果有三种床，只更新前后两种，中间的未设置时，需要分别更新
		if (bedTypes.length == 3 && bigStatus != null && doubleStatus == null
				&& singleStatus != null) {
			count += roomControlDao.saveRoomStateBatch(roomBed, 1, bigStatus);
			count += roomControlDao
					.saveRoomStateBatch(roomBed, 3, singleStatus);
		} else {
			if (bigStatus != null) {
				roomStatusReg += "1:-?[0-9]";
				roomStatus += "1:" + bigStatus;
			}
			if (doubleStatus != null) {
				roomStatusReg += "/2:-?[0-9]";
				roomStatus += "/2:" + doubleStatus;
			}
			if (singleStatus != null) {
				roomStatusReg += "/3:-?[0-9]";
				roomStatus += "/3:" + singleStatus;
			}
			if (roomStatus.startsWith("/")) {
				roomStatusReg = roomStatusReg.replaceFirst("/", "");
				roomStatus = roomStatus.replaceFirst("/", "");
			}
			count += roomControlDao.saveRoomStateBatch(roomBed, roomStatusReg,
					roomStatus);
		}
		return count;
	}

	

	// 加的参数roomStateBean是记录日志用的，用AOP记录
	public void batchUpdateRoomStatus(RoomStateBean roomStateBean,
			HashMap roomStateMap, List lstTempQuota) {
		for (Iterator it = roomStateMap.keySet().iterator(); it.hasNext();) {
			String roomId = (String) it.next();
			String roomState = (String) roomStateMap.get(roomId);
			roomControlDao.updateRoomState(Long.parseLong(roomId), roomState);
		}
	}

	/**
	 * 批量跟新临时配额,现在改为更新新表数据 add by haibo.li 配额改造
	 * 
	 */
	public String batchUpdateTempQuota(List lstquotaNew) {
		String str = "";
		try {
			for (int i = 0; i < lstquotaNew.size(); i++) {
				HtlQuotaJudge hj = (HtlQuotaJudge) lstquotaNew.get(i);
				super.save(hj);
				Long id = hj.getID();
				str += id.toString() + ",";
				// 调用存储过程
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);

		}
		return str;

	}

	/**
	 * 根据插入id调用存储过程更新,配额新表,配额明细表 add by haibo.li 2.9.3配额改造
	 */
	public void UpdateTempQuota(String isStr) {
		if (isStr != null && !isStr.equals("")) {
			String str[] = isStr.split(",");
			if (str.length > 0) {
				for (int i = 0; i < str.length; i++) {
					CallableStatement cstmt = null;
					try {
						String procedureName = "{?= call FUN_QUOTAREFACTOR(?)}";
						cstmt = super.getCurrentSession().connection()
								.prepareCall(procedureName);
						cstmt.setInt(1, 0);
						cstmt.setLong(2, new Long(str[i]));
						cstmt.executeUpdate();
						cstmt.close();
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					} finally {
						if (cstmt != null) {
							try {
								cstmt.close();
							} catch (SQLException e) {
								log.error(e.getMessage(), e);
							}
						}
					}

				}

			}

		}

	}

	public List selectRoomCloseDate(long hotelId) {
		return roomControlDao.selectCloseRoomDate(hotelId);
	}

	/**
	 *根据配额总表ID，查询对应的配额信息hotel2.9.3 add by shengwei.zuo 2009-10-19
	 */

	public HtlQuotaNew findQuotaNewInfoById(Long id) {

		HtlQuotaNew quotaNew = (HtlQuotaNew) super.find(HtlQuotaNew.class, id);

		return quotaNew;
	}

	/**
	 *修改对应的配额信息hotel2.9.3 add by shengwei.zuo 2009-10-19
	 */
	public int modifyQuotaNewInfo(List quotaNewLst) {
		Map map = new HashMap();
		Long hotelId = null;
		for (int i = 0; i < quotaNewLst.size(); i++) {

			HtlTempQuota tempQuotaObj = (HtlTempQuota) quotaNewLst.get(i);

			if (tempQuotaObj.getQuotaNewId() != null
					&& !""
							.equals((String.valueOf(tempQuotaObj
									.getQuotaNewId())))) {

				HtlQuotaNew quotaNewObj = new HtlQuotaNew();

				quotaNewObj = findQuotaNewInfoById(tempQuotaObj.getQuotaNewId());

				if (quotaNewObj != null) {
					hotelId = quotaNewObj.getHotelId();
					String keyDate = DateUtil.dateToString(quotaNewObj
							.getAbleSaleDate());
					if (map.containsKey(keyDate)) {
						List list = (List) map.get(keyDate);
						list.add(quotaNewObj);
					} else {
						List list = new ArrayList();
						list.add(quotaNewObj);
						map.put(keyDate, list);
					}
					// 当预警状态为 “强制解除时” 才会直接去更新该预警状态为 "强制解除" add by shengwei.zuo
					// 2010-1-8
					if (String.valueOf(tempQuotaObj.getForewarnFlagRoSta()) != null
							&& !"".equals(String.valueOf(tempQuotaObj
									.getForewarnFlagRoSta()))) {
						if (null != tempQuotaObj.getForewarnFlagRoSta()
								&& tempQuotaObj.getForewarnFlagRoSta()
										.intValue() == 2) {
							quotaNewObj.setForewarnFlag(Long.parseLong("2"));
							super.saveOrUpdate(quotaNewObj);
						}
					}

					// 记录日志
					HtlQuotaLog htlQuotaLog = new HtlQuotaLog();
					htlQuotaLog.setAbleDate(quotaNewObj.getAbleSaleDate());
					htlQuotaLog.setBedId(quotaNewObj.getBedId());
					htlQuotaLog.setHotelId(quotaNewObj.getHotelId());
					htlQuotaLog.setQuotaType(HotelBaseConstantBean.TEMPQUOTA);
					htlQuotaLog.setJudgeType("ROOM_UPT");
					htlQuotaLog.setRoomtypeId(quotaNewObj.getRoomtypeId());
					htlQuotaLog.setHtlQuotaId(quotaNewObj.getID());
					htlQuotaLog.setQuotaHolder(quotaNewObj.getQuotaHolder());
					htlQuotaLog.setQuotaShare(quotaNewObj.getQuotaShareType()
							.toString());

					htlQuotaLog.setOperatorDept(tempQuotaObj.getOperatorDept());
					htlQuotaLog.setOperatorId(tempQuotaObj.getOperatorId());
					htlQuotaLog.setOperatorName(tempQuotaObj.getOperatorName());
					htlQuotaLog.setOperatorTime(tempQuotaObj.getOperatorTime());

					super.save(htlQuotaLog);

				}

			}

		}
		if (null != hotelId) {
			this.updateScheduleWarnFlag(hotelId, new Date());
		}
		return 0;

	}

	/**
	 *异步保存交接事项和是否主动报房态 add by zhijie.gu 2010-01-05
	 * 
	 */
	public void saveRoomStateProcess(RoomStateBean roomStateBean) {

		if (null != roomStateBean) {
			HtlRoomStatusProcess rsp = new HtlRoomStatusProcess();
			rsp.setHotelId(roomStateBean.getHotelID());
			rsp.setProcessBy(roomStateBean.getUserName());
			rsp.setProcessById(roomStateBean.getUserId());
			rsp.setProcessDate(DateUtil.getSystemDate());
			rsp.setProcessRemark(roomStateBean.getProcessRemark());
			rsp.setProcessDatetime(DateUtil.getSystemDate());
			if (!"".equals(roomStateBean.getIsRoomStatusReport())
					&& null != roomStateBean.getIsRoomStatusReport()) {
				rsp.setIsRoomStatusReport(Long.valueOf(
						roomStateBean.getIsRoomStatusReport()).longValue());
			}
			super.saveOrUpdate(rsp);
		} else {
			log.info("roomStateBean is null!");
		}

	}

	/**
	 * mailType 0,为配额>0，房态设置为满房；1,连续设置满房超过7天发送mail；2，房态由F调整为满房发送邮件
	 */
	public void sendRoomStateFull(List list, HtlHotel htlHotel,
			UserWrapper user, Integer mailType) {

		List fullList = new ArrayList();
		if (null != list) {
			for (Iterator i = list.iterator(); i.hasNext();) {
				HtlTempRoomState tempRoom = (HtlTempRoomState) i.next();
				Long sumQuota = 0L;
				Long ableQuota = 0L;
				List tempList = saleDao.queryTempQuotaByRoomState(htlHotel
						.getID(), tempRoom.getRoomType(), tempRoom
						.getSaleDate(), tempRoom.getBedId());
				for (Iterator j = tempList.iterator(); j.hasNext();) {
					HtlQuotaNew quotaNew = (HtlQuotaNew) j.next();
					sumQuota += (null == quotaNew.getCommonQuotaSum() ? 0
							: quotaNew.getCommonQuotaSum())
							+ (null == quotaNew.getBuyQuotaSum() ? 0 : quotaNew
									.getBuyQuotaSum());
					Long buyQuotaAbleNum = (null == quotaNew
							.getBuyQuotaAbleNum() ? 0 : quotaNew
							.getBuyQuotaAbleNum())
							- (null == quotaNew.getBuyQuotaOutofdateNum() ? 0
									: quotaNew.getBuyQuotaOutofdateNum());
					Long commonQuotaAbleNum = (null == quotaNew
							.getCommonQuotaAbleNum() ? 0 : quotaNew
							.getCommonQuotaAbleNum())
							- (null == quotaNew.getCommonQuotaOutofdateNum() ? 0
									: quotaNew.getCommonQuotaOutofdateNum());
					ableQuota += buyQuotaAbleNum + commonQuotaAbleNum;
				}
				tempRoom.setAbleUseQuoatCount(ableQuota);
				tempRoom.setQuatoCount(sumQuota);
				if ((mailType.equals(MAIL_TYPE_QUOTA_WARNING))
						&& (tempRoom.getAbleUseQuoatCount() > 0)) {
					fullList.add(tempRoom);
				}
				if (mailType.equals(MAIL_TYPE_FULL_WEEK)) {
					fullList.add(tempRoom);
				}
				if (mailType.equals(MAIL_TYPE_FREESALE_TO_FULL)
						&& ROOMSTATE_FREESALE
								.equals(tempRoom.getOldBedStatus())
						&& ROOMSTATE_FULL.equals(tempRoom.getNewBedStatus())) {
					fullList.add(tempRoom);
				}
			}
		}

		if ((null != fullList) && (fullList.size() > 0)) {
			HtlArea htlArea = saleDao.queryAreaCode(htlHotel.getCity());
			String toaddress = "";
			if (null != htlArea) {
				if (htlArea.getAreaCode().equals(AreaType.BBQ)) {
					toaddress = "Hotel-Hnsz";
				} else if (htlArea.getAreaCode().equals(AreaType.HDQ)) {
					toaddress = "Hotel_HuaDong";
				} else if (htlArea.getAreaCode().equals(AreaType.HBQ)) {
					toaddress = "Hotel_HuaBei";
				} else if (htlArea.getAreaCode().equals(AreaType.GZQ)) {
					toaddress = "Hotel_GuangZhou";
				}else if(htlArea.getAreaCode().equals(AreaType.GAZ)){
	            	toaddress = "hotel-hnhk";
	            }
			}
			String hotelAddress = "";

			if (null != htlHotel.getCity()) {
				hotelAddress += InitServlet.localCityObj
						.get(htlHotel.getCity());
			}
			if (null != htlHotel.getZone()) {
				hotelAddress += InitServlet.citySozeObj.get(htlHotel.getZone());
			}
			if (null != htlHotel.getBizZone()) {
				hotelAddress += InitServlet.businessSozeObj.get(htlHotel
						.getBizZone());
			}
			hotelAddress += " " + htlHotel.getChnName();

			Mail mail = new Mail();
			Long ret = null;
			mail.setApplicationName("hotel");
			String templateNo = FaxEmailModel.HOTEL_QUOTA__ROOM_STATE_MAIL;
			mail.setTo(new String[] { toaddress });
			String title = "";
			if (mailType.equals(MAIL_TYPE_QUOTA_WARNING)) {
				title = "收回配额预警：" + hotelAddress + " "
						+ htlHotel.getProductManager() + " " + user.getName();
			} else if (mailType.equals(MAIL_TYPE_FULL_WEEK)) {
				title = "满房超过7天：" + hotelAddress + " "
						+ htlHotel.getProductManager() + " " + user.getName();
			} else if (mailType.equals(MAIL_TYPE_FREESALE_TO_FULL)) {
				title = "F酒店满房预警：" + hotelAddress + " "
						+ htlHotel.getProductManager() + " " + user.getName();
			}
			log.info("配额预警标题:" + title);
			mail.setSubject(title);
			mail.setTemplateFileName(templateNo);
			mail.setFrom("cs@mangocity.com");
			mail.setUserLoginId(user.getLoginName());
			String xmlString = MsgAssist.genMailContractorByRoomState(fullList);
			log.info("======xmlString==" + xmlString);
			mail.setXml(xmlString);
			try {
				ret = communicaterService.sendEmail(mail);
			} catch (Exception e) {
				if (mailType.equals(MAIL_TYPE_QUOTA_WARNING)) {
					log.error("收回配额预警邮件发送失败！ " + e);
				} else if (mailType.equals(MAIL_TYPE_FULL_WEEK)) {
					log.error("满房超过7天邮件发送失败！ " + e);
				} else if (mailType.equals(MAIL_TYPE_FREESALE_TO_FULL)) {
					log.error("F酒店满房预警邮件发送失败！ " + e);
				}
			}
			if (null == ret || 0 >= ret) {
				if (mailType.equals(MAIL_TYPE_QUOTA_WARNING)) {
					log.error("收回配额预警邮件发送失败！ ");
				} else if (mailType.equals(MAIL_TYPE_FULL_WEEK)) {
					log.error("满房超过7天邮件发送失败！ ");
				} else if (mailType.equals(MAIL_TYPE_FREESALE_TO_FULL)) {
					log.error("F酒店满房预警邮件发送失败！ ");
				}
			}
		}
	}

	public Map queryCCSetRoomDateList(long hotelID, List<HtlRoomtype> list,
			int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		int startNo = (pageNo - 1) * pageSize;
		Map map = new HashMap();
		List listDate = roomControlDao.getRoomTypeOrDatesByCCSet(2, hotelID,
				startNo, pageSize);
		if (pageNo > 1) {// 如果是拿第二页以上，则会多拿出一个行号出来，所以要把它去掉
			List tempList = new ArrayList();
			for (Object obt : listDate) {
				Object[] obts = (Object[]) obt;
				tempList.add(obts[0]);
			}
			listDate = tempList;
		}
		map.put("query_newquota_detail_list", listDate);
		List countList = roomControlDao.getRoomTypeOrDatesByCCSet(3, hotelID,
				startNo, pageSize);
		if ((null != countList) && (!countList.isEmpty())) {
			map.put("query_newquota_detail_totalnum",
					((BigDecimal) countList.get(0)).longValue());
		}
		return map;
	}
	
	private void updateScheduleWarnFlag(Long hotelId, Date schDate) {
		long count = qryQuotaDao.getQuotaNewCountByHtlId(hotelId);
		List<HtlRoomcontrolHotelSchedule> hotelScheduleList = roomControlDao.getHotelScheduleByHtlIdSchDate(hotelId, schDate);
		for (HtlRoomcontrolHotelSchedule hotelSchedule : hotelScheduleList){
			if(0L == count){
				hotelSchedule.setQuotawarninghotel(Integer.valueOf(0));
			}else{
				hotelSchedule.setQuotawarninghotel(Integer.valueOf(1));
			}
			
			roomControlDao.updateHtlRoomcontrolHotelSchedule(hotelSchedule);
		}
	}
	
	public ISaleDao getSaleDao() {
		return saleDao;
	}

	public void setSaleDao(ISaleDao saleDao) {
		this.saleDao = saleDao;
	}

	public CommunicaterService getCommunicaterService() {
		return communicaterService;
	}

	public void setCommunicaterService(CommunicaterService communicaterService) {
		this.communicaterService = communicaterService;
	}

	public IQueryQuotaDao getQryQuotaDao() {
		return qryQuotaDao;
	}

	public void setQryQuotaDao(IQueryQuotaDao qryQuotaDao) {
		this.qryQuotaDao = qryQuotaDao;
	}
	
	public RoomControlDao getRoomControlDao() {
		return roomControlDao;
	}

	public void setRoomControlDao(RoomControlDao roomControlDao) {
		this.roomControlDao = roomControlDao;
	}
	
}
