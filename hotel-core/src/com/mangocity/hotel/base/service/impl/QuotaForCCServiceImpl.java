package com.mangocity.hotel.base.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.dao.IQuotaForCCDao;
import com.mangocity.hotel.base.manage.QuotaPriceManage;
import com.mangocity.hotel.base.persistence.HtlAssignCustom;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlCutoffDayQuota;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlQuota;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.base.persistence.HtlTempQuota;
import com.mangocity.hotel.base.service.IQuotaForCCService;
import com.mangocity.hotel.base.service.assistant.QuotaQueryPo;
import com.mangocity.hotel.base.service.assistant.QuotaReturnPo;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.HotelBaseConstantBean;

/**
 */
public class QuotaForCCServiceImpl implements IQuotaForCCService {

	private QuotaPriceManage quotaPriceManage;

	private IQuotaForCCDao quotaForCCDao;

	public void setQuotaPriceManage(QuotaPriceManage quotaPriceManage) {
		this.quotaPriceManage = quotaPriceManage;
	}

	/*
	 * 扣配额方法
	 */
	public List<QuotaReturnPo> deductQuota(QuotaQueryPo quotaQueryPo) {

		List<QuotaReturnPo> quotaReturnPoLis = new ArrayList<QuotaReturnPo>();

		// 判断传入类是否为空
		if (null == quotaQueryPo || 0 == quotaQueryPo.getQuotaNum()) {
			QuotaReturnPo quotaReturnPo = new QuotaReturnPo();
			quotaReturnPoLis.add(quotaReturnPo);
			return quotaReturnPoLis;
		}

		/*
		 * 计算入住日期和退房日期相间隔天数，即入住几晚
		 */
		int dayNum = DateUtil.getDay(quotaQueryPo.getBeginDate(), quotaQueryPo
				.getEndDate());
		int qpNum = dayNum;
		/*
		 * 计算CUTOFFDAY天数
		 */
		int cutOffDayNum = DateUtil.getDay(new Date(), quotaQueryPo
				.getBeginDate());

		/*
		 * 查找合同判断配额模式，根据配额模式扣配额， 如果为S-I在店每天都扣，如果为C-I进店只扣第一天
		 * 合同查找条件为入住第一天所在合同里的配额模式
		 */
		int quotaPatternNum = 0;
		HtlContract htlContract = quotaPriceManage.qryHtlcontractForCC(
				quotaQueryPo.getHotelId(), quotaQueryPo.getBeginDate());
		// 查不到合同则直接返回
		if (null == htlContract || 1 > htlContract.getID()) {
			for (int i = 0; i < dayNum; i++) {
				for (int j = 0; j < quotaQueryPo.getQuotaNum(); j++) {
					QuotaReturnPo quotaReturnPo = new QuotaReturnPo();

					// C-1进店模式，填写下面参数
					// 酒店id
					quotaReturnPo.setHotelId(quotaQueryPo.getHotelId());
					// 房型id
					quotaReturnPo.setRoomTypeId(quotaQueryPo.getRoomTypeId());
					// 价格类型
					quotaReturnPo.setChildRoomTypeId(quotaQueryPo
							.getChildRoomId());
					// 床型ID
					quotaReturnPo.setBedId(quotaQueryPo.getBedID());
					// 要扣配额类型( 包房配额2，普通配额1)
					quotaReturnPo.setQuotaTypeOld(quotaQueryPo.getQuotaType());
					// 支付方式
					quotaReturnPo.setPayMethod(quotaQueryPo.getPayMethod());
					// 会员类型(TMC,CC,TP)
					quotaReturnPo.setMemberType(quotaQueryPo.getMemberType());
					// 被扣配额的所属日期
					quotaReturnPo.setQuotaDate(DateUtil.getDate(quotaQueryPo
							.getBeginDate(), i));
					// 扣配额日期
					quotaReturnPo.setUseQuotaDate(new Date());
					// 扣配额数量
					quotaReturnPo.setQuotaNum(0);

					/***********************************************************
					 * //底价 quotaReturnPo.setBasePrice(htlPrice.getBasePrice());
					 * //门市价
					 * quotaReturnPo.setSalesroomPrice(htlPrice.getSalesroomPrice());
					 * //销售价
					 * quotaReturnPo.setSalePrice(htlPrice.getSalePrice()); //房态
					 * quotaReturnPo.setRoomState(htlRoom.getRoomState());
					 */
					// 配额模式(如果为S-I在店每天都扣，如果为C-I进店只扣第一天)
					// quotaReturnPo.setQuotaPattern(HotelBaseConstantBean.QUOTAPATTERNCI);
					// 扣退配额成功标志(0扣成功 1扣失败 2退成功 3退失败)
					quotaReturnPo.setSign(1);

					quotaReturnPoLis.add(quotaReturnPo);
				}
			}
			return quotaReturnPoLis;
		}

		if (HotelBaseConstantBean.QUOTAPATTERNCI.equals(htlContract
				.getQuotaPattern())) {
			dayNum = 1;
			quotaPatternNum = 1;
		}

		/*
		 * 根据每天每间房for循环扣取配额
		 */

		// 每天循环
		for (int i = 0; i < dayNum; i++) {
			/* 定义每种扣配额可能性的，扣配额数 */
			// 临时计配额数
			int ls = 0;

			// 循环的日期
			Date day = DateUtil.getDate(quotaQueryPo.getBeginDate(), i);
			// 获取当天房间状态
			HtlRoom htlRoom = quotaPriceManage.qryHtlRoomForCC(quotaQueryPo
					.getRoomTypeId(), day);

			if (null == htlRoom || 1 > htlRoom.getID()) {
				for (int j = 0; j < quotaQueryPo.getQuotaNum(); j++) {
					QuotaReturnPo quotaReturnPo = new QuotaReturnPo();

					// C-1进店模式，填写下面参数
					// 酒店id
					quotaReturnPo.setHotelId(quotaQueryPo.getHotelId());
					// 房型id
					quotaReturnPo.setRoomTypeId(quotaQueryPo.getRoomTypeId());
					// 价格类型
					quotaReturnPo.setChildRoomTypeId(quotaQueryPo
							.getChildRoomId());
					// 床型ID
					quotaReturnPo.setBedId(quotaQueryPo.getBedID());
					// 要扣配额类型( 包房配额2，普通配额1)
					quotaReturnPo.setQuotaTypeOld(quotaQueryPo.getQuotaType());
					// 支付方式
					quotaReturnPo.setPayMethod(quotaQueryPo.getPayMethod());
					// 会员类型(TMC,CC,TP)
					quotaReturnPo.setMemberType(quotaQueryPo.getMemberType());
					// 被扣配额的所属日期
					quotaReturnPo.setQuotaDate(DateUtil.getDate(quotaQueryPo
							.getBeginDate(), i));
					// 扣配额日期
					quotaReturnPo.setUseQuotaDate(new Date());
					// 扣配额数量
					quotaReturnPo.setQuotaNum(0);

					/*
					 * //底价 quotaReturnPo.setBasePrice(htlPrice.getBasePrice());
					 * //门市价
					 * quotaReturnPo.setSalesroomPrice(htlPrice.getSalesroomPrice());
					 * //销售价
					 * quotaReturnPo.setSalePrice(htlPrice.getSalePrice()); //房态
					 * quotaReturnPo.setRoomState(htlRoom.getRoomState());
					 */
					// 配额模式(如果为S-I在店每天都扣，如果为C-I进店只扣第一天)
					// quotaReturnPo.setQuotaPattern(HotelBaseConstantBean.QUOTAPATTERNCI);
					// 扣退配额成功标志(0扣成功 1扣失败 2退成功 3退失败)
					quotaReturnPo.setSign(1);

					quotaReturnPoLis.add(quotaReturnPo);
				}
				return quotaReturnPoLis;
			}

			// 获取当天房间价格
			HtlPrice htlPrice = quotaPriceManage.qryHtlPriceForCC(quotaQueryPo
					.getChildRoomId(), day, quotaQueryPo.getPayMethod(),
					quotaQueryPo.getQuotaType());
			// 判断支付方式为面预付
			HtlQuota htlQuotaA = new HtlQuota();

			// 增加一个可退参数
			boolean backQuotaSign = true;

			if (HotelBaseConstantBean.PREPAY
					.equals(quotaQueryPo.getPayMethod())) {
				htlQuotaA = quotaPriceManage.qryHtlQuotaForCC(htlRoom.getID()
						.longValue(), quotaQueryPo.getQuotaType(),
						HotelBaseConstantBean.QUOTASHARETYPEPREPAY);
			} else {
				htlQuotaA = quotaPriceManage.qryHtlQuotaForCC(htlRoom.getID()
						.longValue(), quotaQueryPo.getQuotaType(),
						HotelBaseConstantBean.QUOTASHARETYPEPAY);
			}

			// 查询面预付的独占配额
			InQuota inQuotaA = deductQuota(htlQuotaA, quotaQueryPo
					.getMemberType(), cutOffDayNum, quotaQueryPo.getQuotaNum());
			if (1 == inQuotaA.getSign()) {

				ls = inQuotaA.getQuoSNum();
				quotaPriceManage.UpdateQuota(inQuotaA.getHtlQuota());
				backQuotaSign = htlQuotaA.isTakebackQuota();
			} else {
				ls = quotaQueryPo.getQuotaNum();
			}
			// 面预付独占配额不足所扣配额数查找面预付共享配额
			if (0 < ls) {
				htlQuotaA = quotaPriceManage.qryHtlQuotaForCC(htlRoom.getID()
						.longValue(), quotaQueryPo.getQuotaType(),
						HotelBaseConstantBean.QUOTASHARETYPE);
				inQuotaA = deductQuota(htlQuotaA, quotaQueryPo.getMemberType(),
						cutOffDayNum, inQuotaA.getQuoSNum());
				if (1 == inQuotaA.getSign()) {
					ls = inQuotaA.getQuoSNum();
					quotaPriceManage.UpdateQuota(inQuotaA.getHtlQuota());
					backQuotaSign = htlQuotaA.isTakebackQuota();
				}

			}
			// 已扣配额 生成返回类
			for (int k = 0; k < quotaQueryPo.getQuotaNum() - ls; k++) {
				// 给返回类赋值
				QuotaReturnPo quotaReturnPo = new QuotaReturnPo();
				// 酒店id
				quotaReturnPo.setHotelId(quotaQueryPo.getHotelId());
				// 房型id
				quotaReturnPo.setRoomTypeId(quotaQueryPo.getRoomTypeId());
				// 价格类型
				quotaReturnPo.setChildRoomTypeId(quotaQueryPo.getChildRoomId());
				// 床型ID
				quotaReturnPo.setBedId(quotaQueryPo.getBedID());
				// 要扣配额类型( 包房配额2，普通配额1)
				quotaReturnPo.setQuotaTypeOld(quotaQueryPo.getQuotaType());
				// 支付方式
				quotaReturnPo.setPayMethod(quotaQueryPo.getPayMethod());
				// 会员类型(TMC,CC,TP)
				quotaReturnPo.setMemberType(quotaQueryPo.getMemberType());
				// 被扣配额的所属日期
				quotaReturnPo.setQuotaDate(day);
				// 扣配额日期
				quotaReturnPo.setUseQuotaDate(new Date());
				// 扣配额数量
				quotaReturnPo.setQuotaNum(1);
				// 所扣配额类型( 包房配额2，普通配额1，临时配额3,呼出配额4)
				quotaReturnPo.setQuotaType(quotaQueryPo.getQuotaType());
				// 底价
				quotaReturnPo.setBasePrice(null != htlPrice ? htlPrice
						.getBasePrice() : 0.0);
				// 门市价
				quotaReturnPo.setSalesroomPrice(null != htlPrice ? htlPrice
						.getSalesroomPrice() : 0.0);
				// 销售价
				quotaReturnPo.setSalePrice(null != htlPrice ? htlPrice
						.getSalePrice() : 0.0);
				// 房态
				quotaReturnPo.setRoomState(htlRoom.getRoomState());
				// 配额模式(如果为S-I在店每天都扣，如果为C-I进店只扣第一天)
				quotaReturnPo.setQuotaPattern(htlContract.getQuotaPattern());
				// 配额是否可退 0为不可退 1为可退
				quotaReturnPo.setTakebackQuota(backQuotaSign);
				// 扣退配额成功标志(0扣成功 1扣失败 2退成功 3退失败)
				quotaReturnPo.setSign(0);
				quotaReturnPoLis.add(quotaReturnPo);
			}
			// 扣了面预付的独占与共享配额后不够扣，开始扣临时配额

			Map<String, Object> params = new HashMap<String, Object>();

			if (0 < ls) {

				// 如果没有床型ID传入则不扣临时配额
				if (0 < quotaQueryPo.getBedID()) {
					HtlTempQuota htlTempQuota = quotaPriceManage
							.qryHtlTempQuotaForcc(htlRoom.getID(), String
									.valueOf(quotaQueryPo.getBedID()));

					if (null != htlTempQuota && 0 < htlTempQuota.getQuotaQty()) {
						// 更新临时配额表3
						int nu = 0;
						if (htlTempQuota.getQuotaQty() > ls) {
							nu = ls;
							htlTempQuota.setQuotaQty(htlTempQuota.getQuotaQty()
									- ls);
							ls = 0;
						} else {

							ls -= htlTempQuota.getQuotaQty();
							nu = htlTempQuota.getQuotaQty();
							htlTempQuota.setQuotaQty(0);
						}
						quotaPriceManage.updateHtlTempQuotaForcc(htlTempQuota);
						// 更新房间表

						// 判断扣配额会员类型
						int ccTempQty = 0;
						// TP_TEMP_QTY TP临时配额数
						int tpTempQty = 0;
						// BB_TEMP_QTY b2b临时配额数
						int bbTempQty = 0;
						// TMC_TEMP_QTY TMC临时配额数
						int tmcTempQty = 0;
						int useTempQty = 0;

						if (HotelBaseConstantBean.CC.equals(String
								.valueOf(quotaQueryPo.getMemberType()))) {
							useTempQty = htlRoom.getUseTempQty() + nu;
							ccTempQty = htlRoom.getCcTempQty() + nu;
							tpTempQty = htlRoom.getTpTempQty();
							bbTempQty = htlRoom.getBbTempQty();
							tmcTempQty = htlRoom.getTmcTempQty();
						}
						if (HotelBaseConstantBean.TMC.equals(String
								.valueOf(quotaQueryPo.getMemberType()))) {
							useTempQty = htlRoom.getUseTempQty() + nu;
							ccTempQty = htlRoom.getCcTempQty();
							tpTempQty = htlRoom.getTpTempQty();
							bbTempQty = htlRoom.getBbTempQty();
							tmcTempQty = htlRoom.getTmcTempQty() + nu;
						}
						if (HotelBaseConstantBean.B2B.equals(String
								.valueOf(quotaQueryPo.getMemberType()))) {
							useTempQty = htlRoom.getUseTempQty() + nu;
							ccTempQty = htlRoom.getCcTempQty();
							tpTempQty = htlRoom.getTpTempQty();
							bbTempQty = htlRoom.getBbTempQty() + nu;
							tmcTempQty = htlRoom.getTmcTempQty();
						}
						if (HotelBaseConstantBean.TP.equals(String
								.valueOf(quotaQueryPo.getMemberType()))) {
							useTempQty = htlRoom.getUseTempQty() + nu;
							ccTempQty = htlRoom.getCcTempQty();
							tpTempQty = htlRoom.getTpTempQty() + nu;
							bbTempQty = htlRoom.getBbTempQty();
							tmcTempQty = htlRoom.getTmcTempQty();
						}

						params.put("useTempQty", useTempQty);
						params.put("ccTempQty", ccTempQty);
						params.put("tpTempQty", tpTempQty);
						params.put("bbTempQty", bbTempQty);
						params.put("tmcTempQty", tmcTempQty);
						params.put("roomId", htlRoom.getID());
						params.put("cancelOutsideQty", htlRoom
								.getCancelOutsideQty());

						for (int k = 0; k < nu; k++) {
							// 给返回类赋值
							QuotaReturnPo quotaReturnPo = new QuotaReturnPo();
							// 酒店id
							quotaReturnPo.setHotelId(quotaQueryPo.getHotelId());
							// 房型id
							quotaReturnPo.setRoomTypeId(quotaQueryPo
									.getRoomTypeId());
							// 价格类型
							quotaReturnPo.setChildRoomTypeId(quotaQueryPo
									.getChildRoomId());
							// 床型ID
							quotaReturnPo.setBedId(quotaQueryPo.getBedID());
							// 要扣配额类型( 包房配额2，普通配额1)
							quotaReturnPo.setQuotaTypeOld(quotaQueryPo
									.getQuotaType());
							// 支付方式
							quotaReturnPo.setPayMethod(quotaQueryPo
									.getPayMethod());
							// 会员类型(TMC,CC,TP)
							quotaReturnPo.setMemberType(quotaQueryPo
									.getMemberType());
							// 被扣配额的所属日期
							quotaReturnPo.setQuotaDate(day);
							// 扣配额日期
							quotaReturnPo.setUseQuotaDate(new Date());
							// 扣配额数量
							quotaReturnPo.setQuotaNum(1);
							// 所扣配额类型( 包房配额2，普通配额1，临时配额3,呼出配额4)
							quotaReturnPo
									.setQuotaType(HotelBaseConstantBean.TEMPQUOTA);
							// 底价
							quotaReturnPo
									.setBasePrice(null != htlPrice ? htlPrice
											.getBasePrice() : 0.0);
							// 门市价
							quotaReturnPo
									.setSalesroomPrice(null != htlPrice ? htlPrice
											.getSalesroomPrice()
											: 0.0);
							// 销售价
							quotaReturnPo
									.setSalePrice(null != htlPrice ? htlPrice
											.getSalePrice() : 0.0);
							// 房态
							quotaReturnPo.setRoomState(htlRoom.getRoomState());
							// 配额模式(如果为S-I在店每天都扣，如果为C-I进店只扣第一天)
							quotaReturnPo.setQuotaPattern(htlContract
									.getQuotaPattern());
							// 扣退配额成功标志(0扣成功 1扣失败 2退成功 3退失败)
							quotaReturnPo.setSign(0);

							quotaReturnPoLis.add(quotaReturnPo);
						}
					}
				}
				/**
				 * 如果系统配额和临时配额都没有扣到，则扣呼出配额，实现自动呼出功能
				 * 
				 * 为了只写一次房间表，所以把呼出配额的判断放在临时配额内
				 * 
				 */
				if (0 < ls) {
					// 判断扣配额会员类型
					// CC_OUTSIDE_QTY CC呼出配额数
					int ccOutsideQty = 0;
					// TP_OUTSIDE_QTY TP呼出配额数
					int tpOutsideQty = 0;
					// BB_OUTSIDE_QTY b2b呼出配额数
					int bbOutsideQty = 0;
					// TMC_OUTSIDE_QTY TMC呼出配额数
					int tmcOutsideQty = 0;
					int outsideQty = 0;

					if (HotelBaseConstantBean.CC.equals(String
							.valueOf(quotaQueryPo.getMemberType()))) {
						outsideQty = htlRoom.getOutsideQty() + ls;
						ccOutsideQty = htlRoom.getCcOutsideQty() + ls;
						tpOutsideQty = htlRoom.getTpOutsideQty();
						bbOutsideQty = htlRoom.getBbOutsideQty();
						tmcOutsideQty = htlRoom.getTmcOutsideQty();
					}
					if (HotelBaseConstantBean.TMC.equals(String
							.valueOf(quotaQueryPo.getMemberType()))) {
						outsideQty = htlRoom.getOutsideQty() + ls;
						ccOutsideQty = htlRoom.getCcOutsideQty();
						tpOutsideQty = htlRoom.getTpOutsideQty();
						bbOutsideQty = htlRoom.getBbOutsideQty();
						tmcOutsideQty = htlRoom.getTmcOutsideQty() + ls;
					}
					if (HotelBaseConstantBean.B2B.equals(String
							.valueOf(quotaQueryPo.getMemberType()))) {
						outsideQty = htlRoom.getOutsideQty() + ls;
						ccOutsideQty = htlRoom.getCcOutsideQty();
						tpOutsideQty = htlRoom.getTpOutsideQty();
						bbOutsideQty = htlRoom.getBbOutsideQty() + ls;
						tmcOutsideQty = htlRoom.getTmcOutsideQty();
					}
					if (HotelBaseConstantBean.TP.equals(String
							.valueOf(quotaQueryPo.getMemberType()))) {
						outsideQty = htlRoom.getOutsideQty() + ls;
						ccOutsideQty = htlRoom.getCcOutsideQty();
						tpOutsideQty = htlRoom.getTpOutsideQty() + ls;
						bbOutsideQty = htlRoom.getBbOutsideQty();
						tmcOutsideQty = htlRoom.getTmcOutsideQty();
					}

					params.put("outsideQty", outsideQty);
					params.put("ccOutsideQty", ccOutsideQty);
					params.put("tpOutsideQty", tpOutsideQty);
					params.put("bbOutsideQty", bbOutsideQty);
					params.put("tmcOutsideQty", tmcOutsideQty);
					params.put("roomId", htlRoom.getID());
					params.put("cancelOutsideQty", htlRoom
							.getCancelOutsideQty());

					// 更新room表
					quotaForCCDao.updateHtlRoomQuota(params);
					for (int k = 0; k < ls; k++) {
						// 填写下面参数
						QuotaReturnPo quotaReturnPo = new QuotaReturnPo();
						// 酒店id
						quotaReturnPo.setHotelId(quotaQueryPo.getHotelId());
						// 房型id
						quotaReturnPo.setRoomTypeId(quotaQueryPo
								.getRoomTypeId());
						// 价格类型
						quotaReturnPo.setChildRoomTypeId(quotaQueryPo
								.getChildRoomId());
						// 床型ID
						quotaReturnPo.setBedId(quotaQueryPo.getBedID());
						// 要扣配额类型( 包房配额2，普通配额1)
						quotaReturnPo.setQuotaTypeOld(quotaQueryPo
								.getQuotaType());
						// 支付方式
						quotaReturnPo.setPayMethod(quotaQueryPo.getPayMethod());
						// 会员类型(TMC,CC,TP)
						quotaReturnPo.setMemberType(quotaQueryPo
								.getMemberType());
						// 被扣配额的所属日期
						quotaReturnPo.setQuotaDate(day);
						// 扣配额日期
						quotaReturnPo.setUseQuotaDate(new Date());
						// 扣配额数量
						quotaReturnPo.setQuotaNum(1);
						// 所扣配额类型( 包房配额2，普通配额1，临时配额3,呼出配额4)
						quotaReturnPo
								.setQuotaType(HotelBaseConstantBean.OUTSIDEQUOTA);
						// 底价
						quotaReturnPo.setBasePrice(null != htlPrice ? htlPrice
								.getBasePrice() : 0.0);
						// 门市价
						quotaReturnPo
								.setSalesroomPrice(null != htlPrice ? htlPrice
										.getSalesroomPrice() : 0.0);
						// 销售价
						quotaReturnPo.setSalePrice(null != htlPrice ? htlPrice
								.getSalePrice() : 0.0);
						// 房态
						quotaReturnPo.setRoomState(htlRoom.getRoomState());
						// 配额模式(如果为S-I在店每天都扣，如果为C-I进店只扣第一天)
						quotaReturnPo.setQuotaPattern(htlContract
								.getQuotaPattern());
						// 扣退配额成功标志(0扣成功 1扣失败 2退成功 3退失败)
						quotaReturnPo.setSign(0);

						quotaReturnPoLis.add(quotaReturnPo);
					}
				} else {
					quotaForCCDao.updateHtlRoomQuota(params);
				}
			}

		}

		// 如果quotaPatternNum=1 为C-1进店模式 补足剩下天数的价格查询
		if (1 == quotaPatternNum) {
			for (int i = 1; i < qpNum; i++) {

				// 循环的日期
				Date day = DateUtil.getDate(quotaQueryPo.getBeginDate(), i);
				// 获取当天房间状态
				HtlRoom htlRoom = quotaPriceManage.qryHtlRoomForCC(quotaQueryPo
						.getRoomTypeId(), day);
				// 获取当天房间价格
				HtlPrice htlPrice = quotaPriceManage.qryHtlPriceForCC(
						quotaQueryPo.getChildRoomId(), day, quotaQueryPo
								.getPayMethod(), quotaQueryPo.getQuotaType());

				// 每间房循环
				for (int j = 0; j < quotaQueryPo.getQuotaNum(); j++) {
					QuotaReturnPo quotaReturnPo = new QuotaReturnPo();

					// C-1进店模式，填写下面参数
					// 酒店id
					quotaReturnPo.setHotelId(quotaQueryPo.getHotelId());
					// 房型id
					quotaReturnPo.setRoomTypeId(quotaQueryPo.getRoomTypeId());
					// 价格类型
					quotaReturnPo.setChildRoomTypeId(quotaQueryPo
							.getChildRoomId());
					// 床型ID
					quotaReturnPo.setBedId(quotaQueryPo.getBedID());
					// 要扣配额类型( 包房配额2，普通配额1)
					quotaReturnPo.setQuotaTypeOld(quotaQueryPo.getQuotaType());
					// 支付方式
					quotaReturnPo.setPayMethod(quotaQueryPo.getPayMethod());
					// 会员类型(TMC,CC,TP)
					quotaReturnPo.setMemberType(quotaQueryPo.getMemberType());
					// 被扣配额的所属日期
					quotaReturnPo.setQuotaDate(day);
					// 扣配额日期
					quotaReturnPo.setUseQuotaDate(new Date());
					// 扣配额数量
					quotaReturnPo.setQuotaNum(0);

					// 底价
					quotaReturnPo.setBasePrice(htlPrice.getBasePrice());
					// 门市价
					quotaReturnPo.setSalesroomPrice(htlPrice
							.getSalesroomPrice());
					// 销售价
					quotaReturnPo.setSalePrice(htlPrice.getSalePrice());
					// 房态
					quotaReturnPo.setRoomState(htlRoom.getRoomState());
					// 配额模式(如果为S-I在店每天都扣，如果为C-I进店只扣第一天)
					quotaReturnPo
							.setQuotaPattern(HotelBaseConstantBean.QUOTAPATTERNCI);
					// 扣退配额成功标志(0扣成功 1扣失败 2退成功 3退失败)
					quotaReturnPo.setSign(0);

					quotaReturnPoLis.add(quotaReturnPo);
				}
			}
		}
		return quotaReturnPoLis;
	}

	/**
	 * 实际扣配额的方法
	 * 
	 * @param HtlQuota
	 * @return InQuota
	 */
	private InQuota deductQuota(HtlQuota htlQuota, int memberType,
			int cutOffDayNum, int quotaNum) {

		InQuota inQuota = new InQuota();

		if (null != htlQuota && 0 < htlQuota.getID()) {

			// 如果已释放可用配额小于1则不能扣
			if (1 > htlQuota.getAvailQty()) {
				inQuota.setSign(-1);
				inQuota.setQuoSNum(quotaNum);
				return inQuota;
			}
			// 查出会员类型的相关记录
			HtlAssignCustom htlAssignCustom = null;
			for (int i = 0; i < htlQuota.getLstAssign().size(); i++) {
				HtlAssignCustom htlAssignCustomTemp = (HtlAssignCustom) htlQuota
						.getLstAssign().get(i);
				if (memberType == htlAssignCustomTemp.getMemberType()) {
					htlAssignCustom = htlAssignCustomTemp;
				}
			}
			/*
			 * 判断该会员是否有配额可以继续扣 扣配额的情况 一、会员已售数量小于上限 A 会员独占配额大于需扣配额数（默认为1） B
			 * 会员独占配额数小于需扣配额数，则扣共享配额 二、上限为-1，扣共享配额
			 * 
			 * 注CUTOFFDAY中是否过期 A代表没过期 S代表过期
			 */
			if (null != htlAssignCustom
					&& (htlAssignCustom.getMaxAbleQuota() > htlAssignCustom
							.getSaledQuota() || -1 == htlAssignCustom
							.getMaxAbleQuota())) {
				// 比较未使用配额数是否大于0，大于0表示释放的配额数大于0

				int qNum = 0;
				int mNum = 0;

				// if (htlQuota.getAvailQty()>= quotaNum ){
				qNum = quotaNum;
				mNum = quotaNum;
				// }else{
				// qNum = htlQuota.getAvailQty();
				// mNum = htlQuota.getAvailQty();
				// }

				// 循环CUTOFFDAY表，扣除配额，设置KK标志，如果已扣了则中断循环
				// int j = 0;
				// long[] cutoffDayId = null;
				for (int i = 0; i < htlQuota.getLstCutOffDay().size(); i++) {
					if (0 < qNum) {
						HtlCutoffDayQuota htlCutoffDayQuota = (HtlCutoffDayQuota) htlQuota
								.getLstCutOffDay().get(i);
						// 判断CUTOFFDAY天数是否小于要扣的cutOffDayNum
						if (htlCutoffDayQuota.getCutoffDay() <= cutOffDayNum) {
							// 判断CUTOFFDAY状态是否有效
							if (htlCutoffDayQuota.getStatus().equals(
									HotelBaseConstantBean.CUTOFFDAYSTATEA)) {
								// 判断配额数量是否大于待扣数量

								if (htlCutoffDayQuota.getQuotaQty()
										- htlCutoffDayQuota.getCutoffUsedQty() > qNum) {
									// 开始扣配额更新CUTOFFDAY表
									htlCutoffDayQuota
											.setCutoffUsedQty(htlCutoffDayQuota
													.getCutoffUsedQty()
													+ qNum);
									qNum = 0;
								} else {
									qNum -= (htlCutoffDayQuota.getQuotaQty() - htlCutoffDayQuota
											.getCutoffUsedQty());
									// 开始扣配额更新CUTOFFDAY表
									htlCutoffDayQuota
											.setCutoffUsedQty(htlCutoffDayQuota
													.getQuotaQty());
								}
								// cutoffDayId[j++]=htlCutoffDayQuota.getID();

							}
						}

					} else {

						// 更新配额表
						htlQuota.setUsedQty(htlQuota.getUsedQty() + mNum);
						htlQuota.setAvailQty(htlQuota.getAvailQty() - mNum);

						if (htlAssignCustom.getPrivateQuota() > htlAssignCustom
								.getSaledQuota()) {
							if (htlAssignCustom.getPrivateQuota()
									- htlAssignCustom.getSaledQuota() >= mNum) {
								htlQuota.setPrivateQty(htlQuota.getPrivateQty()
										- mNum);
							} else {
								htlQuota
										.setPrivateQty(htlQuota.getPrivateQty()
												- (htlAssignCustom
														.getPrivateQuota() - htlAssignCustom
														.getSaledQuota()));
								htlQuota
										.setShareQty(htlQuota.getShareQty()
												- (mNum - (htlAssignCustom
														.getPrivateQuota() - htlAssignCustom
														.getSaledQuota())));
							}
							// htlQuota.setPrivateQty(htlQuota.getPrivateQty()-)
						} else {
							htlQuota.setShareQty(htlQuota.getShareQty() - mNum);
						}
						// 更新分配表
						htlAssignCustom.setSaledQuota(htlAssignCustom
								.getSaledQuota()
								+ mNum);
						// inQuota.setCutoffDayId(cutoffDayId);
						inQuota.setSign(1);
						inQuota.setAssignCustomId(htlAssignCustom.getID());
						inQuota.setQuoSNum(0);
						inQuota.setHtlQuota(htlQuota);
						return inQuota;
					}
				}
				// 更新配额表
				htlQuota.setUsedQty(htlQuota.getUsedQty() + (mNum - qNum));
				htlQuota.setAvailQty(htlQuota.getAvailQty() - (mNum - qNum));

				if (htlAssignCustom.getPrivateQuota() > htlAssignCustom
						.getSaledQuota()) {
					if (htlAssignCustom.getPrivateQuota()
							- htlAssignCustom.getSaledQuota() >= (mNum - qNum)) {
						htlQuota.setPrivateQty(htlQuota.getPrivateQty()
								- (mNum - qNum));
					} else {
						htlQuota
								.setPrivateQty(htlQuota.getPrivateQty()
										- (htlAssignCustom.getPrivateQuota() - htlAssignCustom
												.getSaledQuota()));
						htlQuota.setShareQty(htlQuota.getShareQty()
								- ((mNum - qNum) - (htlAssignCustom
										.getPrivateQuota() - htlAssignCustom
										.getSaledQuota())));
					}
					// htlQuota.setPrivateQty(htlQuota.getPrivateQty()-)
				} else {
					htlQuota
							.setShareQty(htlQuota.getShareQty() - (mNum - qNum));
				}
				htlAssignCustom.setSaledQuota(htlAssignCustom.getSaledQuota()
						+ mNum - qNum);
				// inQuota.setCutoffDayId(cutoffDayId);
				inQuota.setSign(1);
				inQuota.setAssignCustomId(htlAssignCustom.getID());
				inQuota.setQuoSNum(qNum);
				inQuota.setHtlQuota(htlQuota);
				return inQuota;
			}
			// 如果会员配额上限等于会员已售配额，或者循环后没有扣到配额都在这里返回
			inQuota.setSign(0);
			inQuota.setQuoSNum(quotaNum);
			return inQuota;

		} else {
			inQuota.setSign(-1);
			inQuota.setQuoSNum(quotaNum);
		}

		return inQuota;

	}

	public void setQuotaForCCDao(IQuotaForCCDao quotaForCCDao) {
		this.quotaForCCDao = quotaForCCDao;
	}

	// 声明一个内部类用于实际扣配额的方法的返回类
	private class InQuota {
		// 扣配额方法返回的状态标志 (如果传入参数为空返回-1,如果配额不足返回0，如果扣配额成功返回1)
		private int sign;

		// 返回扣了配额的会员分配表id
		private long assignCustomId;

		// 返回扣了配额的cutoffdayID
		private long[] cutoffDayId;

		// 扣的配额对会员属于共享还是独占,独占为1，共享为0
		private int memberQuotaType;

		// 还剩的配额数量
		private int quoSNum;

		// 返回的配额类
		private HtlQuota htlQuota;

		public HtlQuota getHtlQuota() {
			return htlQuota;
		}

		public void setHtlQuota(HtlQuota htlQuota) {
			this.htlQuota = htlQuota;
		}

		public int getSign() {
			return sign;
		}

		public void setSign(int sign) {
			this.sign = sign;
		}

		public long getAssignCustomId() {
			return assignCustomId;
		}

		public void setAssignCustomId(long assignCustomId) {
			this.assignCustomId = assignCustomId;
		}

		public int getMemberQuotaType() {
			return memberQuotaType;
		}

		public void setMemberQuotaType(int memberQuotaType) {
			this.memberQuotaType = memberQuotaType;
		}

		public long[] getCutoffDayId() {
			return cutoffDayId;
		}

		public void setCutoffDayId(long[] cutoffDayId) {
			this.cutoffDayId = cutoffDayId;
		}

		public int getQuoSNum() {
			return quoSNum;
		}

		public void setQuoSNum(int quoSNum) {
			this.quoSNum = quoSNum;
		}

	}

	/*
	 * 退配额方法 (non-Javadoc)
	 * 
	 * @see com.mangocity.hotel.base.service.IQuotaForCCService#returnQuota(java.util.List)
	 */
	public List<QuotaReturnPo> returnQuota(QuotaQueryPo quotaQueryPo) {

		// 退配额原则 先退呼出配额，再退临时配额，再退系统配额
		List<QuotaReturnPo> retList = new ArrayList<QuotaReturnPo>();

		// 判断传入类是否为空
		if (null == quotaQueryPo || 0 == quotaQueryPo.getQuotaNum()) {
			QuotaReturnPo quotaReturnPo = new QuotaReturnPo();
			retList.add(quotaReturnPo);
			return retList;
		}

		/*
		 * 计算入住日期和退房日期相间隔天数，即入住几晚
		 */
		int dayNum = DateUtil.getDay(quotaQueryPo.getBeginDate(), quotaQueryPo
				.getEndDate());
		int qpNum = dayNum;

		/*
		 * 查找合同判断配额模式，根据配额模式扣配额， 如果为S-I在店每天都扣，如果为C-I进店只扣第一天
		 * 合同查找条件为入住第一天所在合同里的配额模式
		 */
		int quotaPatternNum = 0;
		HtlContract htlContract = quotaPriceManage.qryHtlcontractForCC(
				quotaQueryPo.getHotelId(), quotaQueryPo.getBeginDate());
		if (HotelBaseConstantBean.QUOTAPATTERNCI.equals(htlContract
				.getQuotaPattern())) {
			dayNum = 1;
			quotaPatternNum = 1;
		}

		// 每天循环
		for (int i = 0; i < dayNum; i++) {
			/** ***** 先退呼出配额 ****** */
			int ls = quotaQueryPo.getQuotaNum();
			// 循环的日期
			Date day = DateUtil.getDate(quotaQueryPo.getBeginDate(), i);
			// 获取当天房间状态
			HtlRoom htlRoom = quotaPriceManage.qryHtlRoomForCC(quotaQueryPo
					.getRoomTypeId(), day);

			Map<String, Object> params = new HashMap<String, Object>();
			int notCancelQty = 0; // 不可退配额数
			// CC_OUTSIDE_QTY CC呼出配额数
			int ccOutsideQty = 0;
			// TP_OUTSIDE_QTY TP呼出配额数
			int tpOutsideQty = 0;
			// BB_OUTSIDE_QTY b2b呼出配额数
			int bbOutsideQty = 0;
			// TMC_OUTSIDE_QTY TMC呼出配额数
			int tmcOutsideQty = 0;
			int outsideQty = 0;// 呼出配额数
			int ccTempQty = 0;// cc临时配额数
			// TP_TEMP_QTY TP临时配额数
			int tpTempQty = 0;
			// BB_TEMP_QTY b2b临时配额数
			int bbTempQty = 0;
			// TMC_TEMP_QTY TMC临时配额数
			int tmcTempQty = 0;
			int useTempQty = 0;// 已用临时配额数
			if (null != htlRoom) {
				if (0 < ls) {
					// 退呼出配额
					if (0 < htlRoom.getOutsideQty()) {
						int kk = 0;
						if (ls >= htlRoom.getOutsideQty()) {
							outsideQty = 0;
							ls -= htlRoom.getOutsideQty();
							kk = htlRoom.getOutsideQty();
						} else {
							outsideQty = htlRoom.getOutsideQty() - ls;
							kk = ls;
							ls = 0;
						}
						if (HotelBaseConstantBean.CC.equals(String
								.valueOf(quotaQueryPo.getMemberType()))) {
							ccOutsideQty = htlRoom.getCcOutsideQty() - kk;
							tpOutsideQty = htlRoom.getTpOutsideQty();
							bbOutsideQty = htlRoom.getBbOutsideQty();
							tmcOutsideQty = htlRoom.getTmcOutsideQty();
						}
						if (HotelBaseConstantBean.TMC.equals(String
								.valueOf(quotaQueryPo.getMemberType()))) {
							ccOutsideQty = htlRoom.getCcOutsideQty();
							tpOutsideQty = htlRoom.getTpOutsideQty();
							bbOutsideQty = htlRoom.getBbOutsideQty();
							tmcOutsideQty = htlRoom.getTmcOutsideQty() - kk;
						}
						if (HotelBaseConstantBean.B2B.equals(String
								.valueOf(quotaQueryPo.getMemberType()))) {
							ccOutsideQty = htlRoom.getCcOutsideQty();
							tpOutsideQty = htlRoom.getTpOutsideQty();
							bbOutsideQty = htlRoom.getBbOutsideQty() - kk;
							tmcOutsideQty = htlRoom.getTmcOutsideQty();
						}
						if (HotelBaseConstantBean.TP.equals(String
								.valueOf(quotaQueryPo.getMemberType()))) {
							ccOutsideQty = htlRoom.getCcOutsideQty();
							tpOutsideQty = htlRoom.getTpOutsideQty() - kk;
							bbOutsideQty = htlRoom.getBbOutsideQty();
							tmcOutsideQty = htlRoom.getTmcOutsideQty();
						}
						params.put("outsideQty", outsideQty);
						params.put("ccOutsideQty", ccOutsideQty);
						params.put("tpOutsideQty", tpOutsideQty);
						params.put("bbOutsideQty", bbOutsideQty);
						params.put("tmcOutsideQty", tmcOutsideQty);
						params.put("roomId", htlRoom.getID());
						params.put("cancelOutsideQty", kk
								+ htlRoom.getCancelOutsideQty());
					}
					// 退临时配额
					if (0 < ls) {
						if (0 < htlRoom.getUseTempQty()) {
							int kk = 0;
							if (ls >= htlRoom.getUseTempQty()) {
								useTempQty = 0;
								ls -= htlRoom.getUseTempQty();
								kk = htlRoom.getUseTempQty();
							} else {
								useTempQty = htlRoom.getUseTempQty() - ls;
								kk = ls;
								ls = 0;
							}
							if (HotelBaseConstantBean.CC.equals(String
									.valueOf(quotaQueryPo.getMemberType()))) {
								ccTempQty = htlRoom.getCcTempQty() - kk;
								tpTempQty = htlRoom.getTpTempQty();
								bbTempQty = htlRoom.getBbTempQty();
								tmcTempQty = htlRoom.getTmcTempQty();
							}
							if (HotelBaseConstantBean.TMC.equals(String
									.valueOf(quotaQueryPo.getMemberType()))) {
								ccTempQty = htlRoom.getCcTempQty();
								tpTempQty = htlRoom.getTpTempQty();
								bbTempQty = htlRoom.getBbTempQty();
								tmcTempQty = htlRoom.getTmcTempQty() - kk;
							}
							if (HotelBaseConstantBean.B2B.equals(String
									.valueOf(quotaQueryPo.getMemberType()))) {
								ccTempQty = htlRoom.getCcTempQty();
								tpTempQty = htlRoom.getTpTempQty();
								bbTempQty = htlRoom.getBbTempQty() - kk;
								tmcTempQty = htlRoom.getTmcTempQty();
							}
							if (HotelBaseConstantBean.TP.equals(String
									.valueOf(quotaQueryPo.getMemberType()))) {
								ccTempQty = htlRoom.getCcTempQty();
								tpTempQty = htlRoom.getTpTempQty() - kk;
								bbTempQty = htlRoom.getBbTempQty();
								tmcTempQty = htlRoom.getTmcTempQty();
							}
							params.put("useTempQty", useTempQty);
							params.put("ccTempQty", ccTempQty);
							params.put("tpTempQty", tpTempQty);
							params.put("bbTempQty", bbTempQty);
							params.put("tmcTempQty", tmcTempQty);
							params.put("roomId", htlRoom.getID());
							if (!params.containsKey("cancelOutsideQty"))
								params.put("cancelOutsideQty", htlRoom
										.getCancelOutsideQty());
						}

					}
				}
				// 退面预付共享配额
				if (0 < ls) {
					HtlQuota htlQuotaA = quotaPriceManage.qryHtlQuotaForCC(
							htlRoom.getID().longValue(), quotaQueryPo
									.getQuotaType(),
							HotelBaseConstantBean.QUOTASHARETYPE);
					InQuota inQuota = cancleQuota(htlQuotaA, quotaQueryPo
							.getMemberType(), 0, ls);
					if (1 == inQuota.getSign()) {
						ls = inQuota.getQuoSNum();
						quotaPriceManage.UpdateQuota(htlQuotaA);
					}
				}
				// 退面预付独占配额
				if (0 < ls) {
					HtlQuota htlQuotaA = new HtlQuota();
					if (HotelBaseConstantBean.PREPAY.equals(quotaQueryPo
							.getPayMethod())) {
						htlQuotaA = quotaPriceManage.qryHtlQuotaForCC(htlRoom
								.getID().longValue(), quotaQueryPo
								.getQuotaType(),
								HotelBaseConstantBean.QUOTASHARETYPEPREPAY);
					} else {
						htlQuotaA = quotaPriceManage.qryHtlQuotaForCC(htlRoom
								.getID().longValue(), quotaQueryPo
								.getQuotaType(),
								HotelBaseConstantBean.QUOTASHARETYPEPAY);
					}
					InQuota inQuota = cancleQuota(htlQuotaA, quotaQueryPo
							.getMemberType(), 0, ls);
					if (1 == inQuota.getSign()) {
						ls = inQuota.getQuoSNum();
						quotaPriceManage.UpdateQuota(htlQuotaA);
					}
				}

				// 写房间表
				if (params.containsKey("roomId")) {
					notCancelQty = htlRoom.getNotCancelQty() + ls;
					params.put("notCancelQty", notCancelQty);
					quotaForCCDao.updateHtlRoomQuota(params);
				}
			}

			// 给已退配额生成返回数据
			for (int y = 0; y < quotaQueryPo.getQuotaNum() - ls; y++) {
				QuotaReturnPo quotaReturnPo = new QuotaReturnPo();
				// 要扣配额类型( 包房配额2，普通配额1)
				quotaReturnPo.setQuotaTypeOld(quotaQueryPo.getQuotaType());
				// 支付方式
				quotaReturnPo.setPayMethod(quotaQueryPo.getPayMethod());
				// 会员类型(TMC,CC,TP)
				quotaReturnPo.setMemberType(quotaQueryPo.getMemberType());
				// 被扣配额的所属日期
				quotaReturnPo.setQuotaDate(day);
				// 扣配额数量
				quotaReturnPo.setQuotaNum(1);
				// 退成功以否标志
				quotaReturnPo.setSign(2);
				// 配额模式(如果为S-I在店每天都扣，如果为C-I进店只扣第一天)
				quotaReturnPo.setQuotaPattern(htlContract.getQuotaPattern());
				retList.add(quotaReturnPo);
			}
			// 给未退配额生成返回数据
			for (int y = 0; y < ls; y++) {
				QuotaReturnPo quotaReturnPo = new QuotaReturnPo();
				// 要扣配额类型( 包房配额2，普通配额1)
				quotaReturnPo.setQuotaTypeOld(quotaQueryPo.getQuotaType());
				// 支付方式
				quotaReturnPo.setPayMethod(quotaQueryPo.getPayMethod());
				// 会员类型(TMC,CC,TP)
				quotaReturnPo.setMemberType(quotaQueryPo.getMemberType());
				// 被扣配额的所属日期
				quotaReturnPo.setQuotaDate(day);
				// 扣配额数量
				quotaReturnPo.setQuotaNum(0);
				// 退成功以否标志
				quotaReturnPo.setSign(3);
				quotaReturnPo.setQuotaPattern(htlContract.getQuotaPattern());
				retList.add(quotaReturnPo);
			}
		}
		// 补齐C-I模式的返回数据
		if (1 == quotaPatternNum) {
			for (int i = 1; i < qpNum; i++) {

				// 循环的日期
				Date day = DateUtil.getDate(quotaQueryPo.getBeginDate(), i);

				// 每间房循环
				for (int j = 0; j < quotaQueryPo.getQuotaNum(); j++) {
					QuotaReturnPo quotaReturnPo = new QuotaReturnPo();
					// 要扣配额类型( 包房配额2，普通配额1)
					quotaReturnPo.setQuotaTypeOld(quotaQueryPo.getQuotaType());
					// 支付方式
					quotaReturnPo.setPayMethod(quotaQueryPo.getPayMethod());
					// 会员类型(TMC,CC,TP)
					quotaReturnPo.setMemberType(quotaQueryPo.getMemberType());
					// 被扣配额的所属日期
					quotaReturnPo.setQuotaDate(day);
					// 扣配额数量
					quotaReturnPo.setQuotaNum(0);
					// 退成功以否标志
					quotaReturnPo.setSign(2);
					quotaReturnPo
							.setQuotaPattern(htlContract.getQuotaPattern());
					retList.add(quotaReturnPo);
				}
			}
		}

		return retList;
	}

	/**
	 * 实际退配额的方法
	 * 
	 * @param HtlQuota
	 * @return InQuota
	 */
	private InQuota cancleQuota(HtlQuota htlQuota, int memberType,
			int cutOffDayNum, int quotaNum) {

		InQuota inQuota = new InQuota();

		int quotaNumTemp = quotaNum;

		if (null != htlQuota && 0 < htlQuota.getID()) {
			if (htlQuota.isTakebackQuota()) {
				// 循环cutoffday退配额
				// for(int i=0;i<htlQuota.getLstCutOffDay().size();i++){
				for (int i = htlQuota.getLstCutOffDay().size() - 1; 0 <= i; i--) {
					if (0 < quotaNumTemp) {
						HtlCutoffDayQuota htlCutoffDayQuota = (HtlCutoffDayQuota) htlQuota
								.getLstCutOffDay().get(i);
						// 判断cutoffday已用配额是否大于0，并且是有效的
						if (0 < htlCutoffDayQuota.getCutoffUsedQty()
								&& htlCutoffDayQuota.getStatus().equals(
										HotelBaseConstantBean.CUTOFFDAYSTATEA)) {
							if (htlCutoffDayQuota.getCutoffUsedQty() > quotaNumTemp) {

								htlCutoffDayQuota
										.setCutoffUsedQty(htlCutoffDayQuota
												.getCutoffUsedQty()
												- quotaNumTemp);
								quotaNumTemp = 0;

							} else {
								quotaNumTemp -= htlCutoffDayQuota
										.getCutoffUsedQty();
								htlCutoffDayQuota.setCutoffUsedQty(0);
							}
						}

					}
				}
				// 退分配表中的已用配额
				for (int i = 0; i < htlQuota.getLstAssign().size(); i++) {
					HtlAssignCustom htlAssignCustomTemp = (HtlAssignCustom) htlQuota
							.getLstAssign().get(i);
					if (memberType == htlAssignCustomTemp.getMemberType()) {
						htlAssignCustomTemp.setSaledQuota(htlAssignCustomTemp
								.getSaledQuota()
								- (quotaNum - quotaNumTemp));
					}
				}
				// 退配额表中的配额数，目前都退入共享配额
				htlQuota.setUsedQty(htlQuota.getUsedQty()
						- (quotaNum - quotaNumTemp));
				htlQuota.setAvailQty(htlQuota.getAvailQty()
						+ (quotaNum - quotaNumTemp));
				htlQuota.setShareQty(htlQuota.getShareQty()
						+ (quotaNum - quotaNumTemp));

				inQuota.setSign(1);
				inQuota.setQuoSNum(quotaNumTemp);
				return inQuota;

			} else {
				inQuota.setSign(-1);
			}
		} else {
			inQuota.setSign(-1);
		}

		return inQuota;
	}
}
