package com.mangocity.hotel.base.manage.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.constant.AreaType;
import com.mangocity.hotel.base.dao.ISaleDao;
import com.mangocity.hotel.base.dao.RoomControlDao;
import com.mangocity.hotel.base.manage.RoomStateManageRefactor;
import com.mangocity.hotel.base.manage.assistant.MsgAssist;
import com.mangocity.hotel.base.manage.assistant.RoomStateBean;
import com.mangocity.hotel.base.persistence.HtlArea;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlOpenCloseRoom;
import com.mangocity.hotel.base.persistence.HtlQuotaNew;
import com.mangocity.hotel.base.persistence.HtlRoomStatusProcess;
import com.mangocity.hotel.base.persistence.HtlTempRoomState;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.RoomTypeStatus;
import com.mangocity.util.hotel.constant.FaxEmailModel;
import com.mangocity.util.log.MyLog;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Mail;

/**
 * refactor
 * 
 * @author zuoshengwei
 * 
 */

public class RoomStateManageImplRefactor implements RoomStateManageRefactor {

	private static final MyLog log = MyLog.getLogger(RoomStateManageImplRefactor.class);

	private RoomControlDao roomControlDao;

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

	// 构造器
	public RoomStateManageImplRefactor(RoomControlDao roomControlDao,
			ISaleDao saleDao, CommunicaterService communicaterService) {
		this.roomControlDao = roomControlDao;
		this.saleDao = saleDao;
		this.communicaterService = communicaterService;
	}

	/**
	 * 拼装房型的房态
	 * 
	 * @param map
	 * @param roomTypeStatus
	 * @return
	 */
	public Map<String, String> packagingRoomBedStatus(List<RoomTypeStatus> roomTypeStatus) {

		Map<String, String> map = new HashMap<String, String>(roomTypeStatus.size());

		StringBuilder strBul = new StringBuilder(20);

		// 拼装房型的房态
		for (RoomTypeStatus roomTS : roomTypeStatus) {

			// 房态不为空
			if (null != roomTS.getRoomBedStatus()) {

				String roomStatus = map.get(roomTS.getRoomTypeID());

				if (null == roomStatus) {

					strBul.append(roomTS.getRoomBedId());
					strBul.append(":");
					strBul.append(roomTS.getRoomBedStatus());

				} else {

					strBul.append(roomStatus);
					strBul.append("/");
					strBul.append(roomTS.getRoomBedId());
					strBul.append(":");
					strBul.append(roomTS.getRoomBedStatus());

				}
				map.put(roomTS.getRoomTypeID(), strBul.toString());
			}
		}

		return map;
	}

	public void batchUpdateRoomStatus(Map<String, String> roomStateMap) {
		for (Map.Entry<String, String> roomStateEntry : roomStateMap.entrySet()) {
			roomControlDao.updateRoomState(Long.parseLong(roomStateEntry.getKey()), roomStateEntry.getValue());
		}
	}

	public void updateTempQuota(String isStr) {
		String sql = "{?= call FUN_QUOTAREFACTOR(?)}";
		String str[] = isStr.split(",");
		Long[] quotaId = new Long[str.length];
		for (int i = 0; i < str.length; i++) {
			quotaId[i] = Long.valueOf(str[i]);
		}
		// 根据插入id调用存储过程更新,配额新表,配额明细表 add by haibo.li 2.9.3配额改造
		roomControlDao.updateTempQuotaByPro(sql, quotaId);
	}

	public List<HtlOpenCloseRoom> selectRoomCloseDate(long hotelId) {
		return roomControlDao.selectCloseRoomDate(hotelId);
	}

	public void saveRoomStateProcess(RoomStateBean roomStateBean) {
		HtlRoomStatusProcess rsp = new HtlRoomStatusProcess();
		rsp.setHotelId(roomStateBean.getHotelID());
		rsp.setProcessBy(roomStateBean.getUserName());
		rsp.setProcessById(roomStateBean.getUserId());
		rsp.setProcessDate(DateUtil.getSystemDate());
		rsp.setProcessRemark(roomStateBean.getProcessRemark());
		rsp.setProcessDatetime(DateUtil.getSystemDate());
		if (!"".equals(roomStateBean.getIsRoomStatusReport()) && null != roomStateBean.getIsRoomStatusReport()) {
			rsp.setIsRoomStatusReport(Long.valueOf(roomStateBean.getIsRoomStatusReport()));
		}

		// 异步保存交接事项和是否主动报房态 add by zhijie.gu 2010-01-05
		roomControlDao.saveOrUpdateRoomStatuPro(rsp);

	}

	public void sendRoomStateFull(List<HtlTempRoomState> list, HtlHotel htlHotel, UserWrapper user, Integer mailType) {

		List<HtlTempRoomState> fullList = conversionTempRoomState(list, mailType, htlHotel);

		// mailType 0,为配额>0，房态设置为满房；1,连续设置满房超过7天发送mail；2，房态由F调整为满房发送邮件
		if (null != fullList && !fullList.isEmpty()) {
			HtlArea htlArea = saleDao.queryAreaCode(htlHotel.getCity());
			String toaddress = "";
			if (null != htlArea) {
				if (AreaType.BBQ.equals(htlArea.getAreaCode())) {
					toaddress = "Hotel-Hnsz";
				} else if (AreaType.HDQ.equals(htlArea.getAreaCode())) {
					toaddress = "Hotel_HuaDong";
				} else if (AreaType.HBQ.equals(htlArea.getAreaCode())) {
					toaddress = "Hotel_HuaBei";
				} else if (AreaType.GZQ.equals(htlArea.getAreaCode())) {
					toaddress = "Hotel_GuangZhou";
				}
			}

			StringBuilder strBui = new StringBuilder(100);

			if (mailType == MAIL_TYPE_QUOTA_WARNING) {
				strBui.append("收回配额预警：");
			} else if (mailType == MAIL_TYPE_FULL_WEEK) {
				strBui.append("满房超过7天：");
			} else if (mailType == MAIL_TYPE_FREESALE_TO_FULL) {
				strBui.append("F酒店满房预警：");
			}

			if (null != htlHotel.getCity()) {
				strBui.append(InitServlet.localCityObj.get(htlHotel.getCity()));
			}
			if (null != htlHotel.getZone()) {
				strBui.append(InitServlet.localCityObj.get(htlHotel.getZone()));
			}
			if (null != htlHotel.getBizZone()) {
				strBui.append(InitServlet.localCityObj.get(htlHotel.getBizZone()));
			}
			strBui.append(" ").append(htlHotel.getChnName()).append(" ").append(htlHotel.getProductManager()).append(
					" ").append(user.getName());

			Mail mail = new Mail();
			Long ret = null;
			mail.setApplicationName("hotel");
			String templateNo = FaxEmailModel.HOTEL_QUOTA__ROOM_STATE_MAIL;
			mail.setTo(new String[] { toaddress });

			log.info("配额预警标题:" + strBui.toString());
			mail.setSubject(strBui.toString());
			mail.setTemplateFileName(templateNo);
			mail.setFrom("cs@mangocity.com");
			mail.setUserLoginId(user.getLoginName());
			String xmlString = MsgAssist.genMailContractorByRoomState(fullList);
			log.info("======xmlString==" + xmlString);
			mail.setXml(xmlString);
			try {
				ret = communicaterService.sendEmail(mail);
				if (null == ret || 0 >= ret) {
					sendEmailLog(mailType);
				}
			} catch (Exception e) {
				sendEmailLog(mailType);
			}
		}
	}

	public List<Date> queryCCSetRoomDateList(long hotelID, int pageNo, int pageSize) {
		int startNo = (pageNo - 1) * pageSize;
		List<Date> listDate = roomControlDao.getCloseRoomDateByCCSet(hotelID, startNo, pageSize);
		if (pageNo > 1) {// 如果是拿第二页以上，则会多拿出一个行号出来，所以要把它去掉
			List<Date> tempList = new ArrayList<Date>();
			for (Date obt : listDate) {
				tempList.add(obt);
			}
			listDate = tempList;
		}
		return listDate;
	}

	/**
	 * 转换配额和房态之间的拼装字符串的显示
	 * 
	 * @param list
	 * @param mailType
	 * @param htlHotel
	 * @return
	 */
	public List<HtlTempRoomState> conversionTempRoomState(List<HtlTempRoomState> list, Integer mailType,
			HtlHotel htlHotel) {

		List<HtlTempRoomState> fullList = new ArrayList<HtlTempRoomState>();

		for (HtlTempRoomState tempRoom : list) {
			Long sumQuota = 0L;
			Long ableQuota = 0L;
			List<HtlQuotaNew> tempList = saleDao.queryTempQuotaByRoomState(htlHotel.getID(), tempRoom.getRoomType(),
					tempRoom.getSaleDate(), tempRoom.getBedId());
			for (HtlQuotaNew quotaNew : tempList) {
				sumQuota += (null == quotaNew.getCommonQuotaSum() ? 0 : quotaNew.getCommonQuotaSum())
						+ (null == quotaNew.getBuyQuotaSum() ? 0 : quotaNew.getBuyQuotaSum());
				Long buyQuotaAbleNum = (null == quotaNew.getBuyQuotaAbleNum() ? 0 : quotaNew.getBuyQuotaAbleNum())
						- (null == quotaNew.getBuyQuotaOutofdateNum() ? 0 : quotaNew.getBuyQuotaOutofdateNum());
				Long commonQuotaAbleNum = (null == quotaNew.getCommonQuotaAbleNum() ? 0 : quotaNew
						.getCommonQuotaAbleNum())
						- (null == quotaNew.getCommonQuotaOutofdateNum() ? 0 : quotaNew.getCommonQuotaOutofdateNum());
				ableQuota += buyQuotaAbleNum + commonQuotaAbleNum;
			}
			tempRoom.setAbleUseQuoatCount(ableQuota);
			tempRoom.setQuatoCount(sumQuota);
			if (MAIL_TYPE_QUOTA_WARNING == mailType && (tempRoom.getAbleUseQuoatCount() > 0)) {
				fullList.add(tempRoom);
			}
			if (MAIL_TYPE_FULL_WEEK == mailType) {
				fullList.add(tempRoom);
			}
			if (MAIL_TYPE_FREESALE_TO_FULL == mailType && ROOMSTATE_FREESALE.equals(tempRoom.getOldBedStatus())
					&& ROOMSTATE_FULL.equals(tempRoom.getNewBedStatus())) {
				fullList.add(tempRoom);
			}
		}
		return fullList;
	}

	/**
	 * 日志
	 * 
	 * @param mailType
	 */
	public void sendEmailLog(Integer mailType) {
		if (mailType == MAIL_TYPE_QUOTA_WARNING) {
			log.info("收回配额预警邮件发送失败！ ");
		} else if (mailType == MAIL_TYPE_FULL_WEEK) {
			log.info("满房超过7天邮件发送失败！ ");
		} else if (mailType == MAIL_TYPE_FREESALE_TO_FULL) {
			log.info("F酒店满房预警邮件发送失败！ ");
		}
	}

	public List<Long> queryCCSetRoomDateNum(long hotelID) {
		return roomControlDao.getCloseRoomDateNumByCCSet(hotelID);
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

	public RoomControlDao getRoomControlDao() {
		return roomControlDao;
	}

	public void setRoomControlDao(RoomControlDao roomControlDao) {
		this.roomControlDao = roomControlDao;
	}

}
