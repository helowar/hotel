package com.mangocity.hotel.order.persistence.assistant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.mangocube.corenut.commons.exception.ErrorCode;

import com.ctol.mango.pge.common.ParamServiceImpl;
import com.mangocity.hotel.ext.member.service.PointsDelegate;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.ext.member.dto.PointDTO;
import com.mangocity.delivery.bill.DSBillService;
import com.mangocity.delivery.domain.DeliveryInfo;
import com.mangocity.delivery.domain.Gathering;
import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hotel.base.constant.HraOrderType;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.exception.BusinessException;
import com.mangocity.hotel.order.constant.EmergencyLevel;
import com.mangocity.hotel.order.constant.FulfillTaskType;
import com.mangocity.hotel.order.constant.OrderSource;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.constant.OrderType;
import com.mangocity.hotel.order.constant.PrepayType;
import com.mangocity.hotel.order.constant.ResType;
import com.mangocity.hotel.order.manager.AliasManager;
import com.mangocity.hotel.order.persistence.MemberCreditCard;
import com.mangocity.hotel.order.persistence.OrCreditCard;
import com.mangocity.hotel.order.persistence.OrCreditCardTemp;
import com.mangocity.hotel.order.persistence.OrFellowInfo;
import com.mangocity.hotel.order.persistence.OrFulfillment;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.persistence.OrPayment;
import com.mangocity.hotel.order.persistence.OrRefund;
import com.mangocity.hotel.order.persistence.OrRemark;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.hotel.constant.BaseConstant;
import com.mangocity.util.log.MyLog;
/**
 * Order辅助类(非静态方法)
 * 
 * @author chenkeming
 * 
 */
public class OrderAssist implements Serializable {
	private static final MyLog log = MyLog.getLogger(OrderAssist.class);
	
	public enum ExceptionCode{
		@ErrorCode(comment = "Member's points is not found.")
		POINTS_NOT_FOUND_ERROR,
	      
		@ErrorCode(comment = "Exception occured when updating member's points.")
	    UPDATE_POINTS_ERROR
	}
	
	/**
	 * 配送接口
	 */
	private DSBillService deliveryServic;

	/**
	 * 资源接口
	 */
    private ResourceManager resourceManager;// parasoft-suppress SERIAL.NSFSC "暂不修改" 

	/**
	 * 会员积分接口
	 */
	protected PointsDelegate pointsDelegate;

	/**
	 * 支付方式
	 * @see PrepayType.strNames[]
	 * PrepayType.strNames[]已经存在同样的数组，没必要重新建立
	 * hotel2.9.3 modify by chenjiajie 2009-08-26 
	private static final String names[] = { "Cash", "CredInt", "CredDom", "Pt",
			"Bank", "Voucher", "Pos", "IPSDom", "IPSInt", "CMBInt", "ALIInt", "TENInt",
			"COMMInt", "YEEInt", "BILLInt", "ABCInt", "COUPON"};
	 */

	protected IOrderService orderService;
	
	/**
	 * add by shizhongwen 2009-02-19 联名商家查询manager
	 */
	private AliasManager aliasManager;
	/**
	 * 保存入住人员
	 * 
     * modify by chenkeming@2008.10.14 修改生产环境td:762, 修改订单页面, 删除入住人并保存订单，发现入住人实际没有被删除
	 * 
	 * @param order
	 * @param fellowList
	 * @param forCmp
	 */
	public String saveOrderFellow(OrOrder order, List fellowList, boolean forCmp) {
		StringBuffer strCmp = new StringBuffer("");
		StringBuffer fellowNames = new StringBuffer("");

		// 删除订单原有的入住人员
		HashMap felMap = new HashMap();
		OrFellowInfo extFellow = null;
		List existFellowList = order.getFellowList();
		boolean[] bExists = new boolean[fellowList.size()];
		HashMap numFellowMap = new HashMap();
		HashMap numExtMap = new HashMap();
		String tempName;
		int nSize;
		nSize = fellowList.size();
		for (int i = 0; i < nSize; i++) {
			OrFellowInfo tempFellow = (OrFellowInfo) fellowList.get(i);
			bExists[i] = false;
			tempName = tempFellow.getFellowName();
			if (null != numFellowMap.get(tempName)) {
				Integer count = (Integer) numFellowMap.get(tempName);
				numFellowMap.put(tempName, Integer
						.valueOf(count.intValue() + 1));
			} else {
				numFellowMap.put(tempName, Integer.valueOf(1));
			}
		}
		nSize = existFellowList.size();
		for (int i = 0; i < nSize; i++) {
			OrFellowInfo tempFellow = (OrFellowInfo) existFellowList.get(i);
			tempName = tempFellow.getFellowName();
			if (null != numExtMap.get(tempName)) {
				Integer count = (Integer) numExtMap.get(tempName);
				numExtMap.put(tempName, Integer.valueOf(count.intValue() + 1));
			} else {
				numExtMap.put(tempName, Integer.valueOf(1));
			}
		}
		List waitList = new ArrayList();
		nSize = existFellowList.size();
		String extName;

		for (int i = 0; i < nSize; i++) {
			if (i >= existFellowList.size())
				break;
			extFellow = (OrFellowInfo) existFellowList.get(i);
			boolean isExist = false;
			extName = extFellow.getFellowName();
			Integer countFellow = (Integer) numFellowMap.get(extName);
			Integer countExt = (Integer) numExtMap.get(extName);
			if (null != countFellow && 0 < countFellow.intValue()) {
				if (countFellow.intValue() < countExt.intValue()) {
					for (int j = 0; j < fellowList.size(); j++) {
						OrFellowInfo tempFellow = (OrFellowInfo) fellowList
								.get(j);
						tempName = tempFellow.getFellowName();
						// 是否被删除
						if (!bExists[j] && null != tempName && null != extName
								&& tempName.equals(extName)) {
							if (StringUtil.StringEquals1(extFellow
									.getFellowNationality(), tempFellow
									.getFellowNationality())
									&& extFellow.isFellowSub() == tempFellow
											.isFellowSub()) {
								isExist = true;
								bExists[j] = true;
								break;
							}
						}
					}
					if (!isExist) {
						waitList.add(extFellow);
						isExist = true;
					}
				} else {
					isExist = true;
				}
			}
			if (!isExist) {
				if (forCmp) {
					strCmp.append("删除入住人:<font color='blue'>"
							+ extFellow.getFellowName() + "</font><br>");
				}
				existFellowList.remove(i--);// parasoft-suppress PB.FLVA "业务需要"
				
				// JTEST分析：业务需要不作修改
				//break;

			} else {
				if (null == felMap.get(extFellow.getFellowName())) {
					felMap.put(extFellow.getFellowName(), extFellow);
				}
			}
		}

		for (int i = 0; i < waitList.size(); i++) {
			extFellow = (OrFellowInfo) waitList.get(i);

			boolean isExist = false;
			for (int j = 0; j < fellowList.size(); j++) {
				OrFellowInfo tempFellow = (OrFellowInfo) fellowList.get(j);
				// 是否被删除
				if (!bExists[j]
						&& null != tempFellow.getFellowName()
						&& null != extFellow.getFellowName()
						&& tempFellow.getFellowName().equals(
								extFellow.getFellowName())) {
					isExist = true;
					bExists[j] = true;
					break;
				}
			}
			if (!isExist) {
				for (int k = 0; k < existFellowList.size(); k++) {
					OrFellowInfo delFellow = (OrFellowInfo) existFellowList
							.get(k);
					if (delFellow.equals(extFellow)) {
						if (forCmp) {
							strCmp
									.append("删除入住人:<font color='blue'>"
											+ extFellow.getFellowName()
											+ "</font><br>");
						}
						existFellowList.remove(k--);// parasoft-suppress PB.FLVA
													// "业务需要"
						// JTEST分析：业务需要不作修改
						break;
					}
				}
			}
		}

		// 更新入住人员
		List newList = new ArrayList();
		boolean[] bExtUse = new boolean[existFellowList.size()];
		for (int i = 0; i < existFellowList.size(); i++) {
			bExtUse[i] = false;
		}
		for (int i = 0; i < fellowList.size(); i++) {
			OrFellowInfo tempFellow = (OrFellowInfo) fellowList.get(i);
			tempName = tempFellow.getFellowName();
			fellowNames.append(tempName + " ");
			extFellow = (OrFellowInfo) felMap.get(tempName);
			if (null != extFellow) { // update
				// TODO: need to update member fellow ?
				boolean bNew = true;
				boolean bModify = false;
				int tmpUseIndex = -1;
				for (int j = 0; j < existFellowList.size(); j++) {
					OrFellowInfo tmpExtFellow = (OrFellowInfo) existFellowList
							.get(j);
					if (!bExtUse[j]
							&& tmpExtFellow.getFellowName().equals(tempName)) {
						if (StringUtil.StringEquals1(tmpExtFellow
								.getFellowNationality(), tempFellow
								.getFellowNationality())
								&& tmpExtFellow.isFellowSub() == tempFellow
										.isFellowSub()) {
							bExtUse[j] = true;
							bModify = false;
							bNew = false;
							break;
						} else {
							extFellow = tmpExtFellow;
							tmpUseIndex = j;
							bModify = true;
							bNew = false;
						}
					}
				}
				if (!bNew && bModify && forCmp) {
					strCmp.append("修改入住人信息:<font color='red'>"
							+ extFellow.getFellowName() + "</font>");
					if (!StringUtil.StringEquals1(extFellow
							.getFellowNationality(), tempFellow
							.getFellowNationality())) {
						strCmp.append("&nbsp;国籍:"
								+ resourceManager.getDescription(
										ResType.FELLOWNATION, extFellow
												.getFellowNationality())
								+ "-><font color='red'>"
								+ resourceManager.getDescription(
										ResType.FELLOWNATION, tempFellow
												.getFellowNationality())
								+ "</font>");
					}
					if (extFellow.isFellowSub() != tempFellow.isFellowSub()) {
						strCmp.append("&nbsp;代订:"
								+ (extFellow.isFellowSub() ? "是" : "否")
								+ "-><font color='red'>"
								+ (tempFellow.isFellowSub() ? "是" : "否")
								+ "</font>");
					}
					strCmp.append("<br>");
				}
				// TODO: 原有ID是否保留？
				if (bModify) {
					bExtUse[tmpUseIndex] = true;
					Long tempId = extFellow.getID();
					MyBeanUtil.copyProperties(extFellow, tempFellow);
					extFellow.setID(tempId);
					extFellow.setOrder(order);
				} else if (bNew) {
					newList.add(tempFellow);
				}
			} else { // insert fellow
				newList.add(tempFellow);
			}
		}
		for (int i = 0; i < newList.size(); i++) {
			OrFellowInfo newFellow = (OrFellowInfo) newList.get(i);
			newFellow.setOrder(order);
			existFellowList.add(newFellow);
			if (forCmp) {
				strCmp.append("新增入住人:<font color='red'>"
						+ newFellow.getFellowName() + "</font><br>");
			}
		}
		if (0 < strCmp.length()) {
			order.setFellowNames(fellowNames.toString());
		}
		return strCmp.toString();
	}

	/**
	 * 设置订单中台类型
	 * 
	 * @param order
	 */
	public void setOrderHraType(OrOrder order) {
		
		// xiejiangfeng--只要是香港和澳门的订单，不管是芒果网还是114或是其他来源的，都需要自动分配到“香港”/“澳门”的组别中处理。
		/** 中台订单流转优化后，组别模式已改变，所以现在的暂时没用，但因为高用该方法地方多，所以直接返回 addby juesuchen 2010-3-30 begin**/
		if(true)
			return;
		/** 中台订单流转优化后，组别模式已改变，所以现在的暂时没用，但因为高用该方法地方多，所以直接返回 addby juesuchen 2010-3-30 end**/
		if (order.isNewOrder()){		
            if (order.getChannel() == ChannelType.CHANNEL_CTS && order.isPrepayOrder()) {// 如果是中旅订单
				order.setHraOrderType(HraOrderType.HK_CTS);
            } else if (order.getCity().equals(OrderUtil.HK_CODE)
                || order.getCity().equals(OrderUtil.MA_CODE)) {
				if (order.isCreditAssured()) {
					order.setHraOrderType(HraOrderType.HK_SURETY);
				} else if (order.isPrepayOrder()) {
					order.setHraOrderType(HraOrderType.HK_PREPAY);
				} else {
					order.setHraOrderType(HraOrderType.HK_NORMAL);
				}
			}
			//屏蔽原因：香港和澳门的订单统一合并为"港澳(预付)，港澳(担保)，港澳(其它)" v2.4.2 by chenjiajie 2008-12-26 
//			else if (order.getCity().equals(OrderUtil.MA_CODE)) {
//				if (order.isCreditAssured()) {
//					order.setHraOrderType(HraOrderType.MA_SURETY);
//				} else if (order.isPrepayOrder()) {
//					order.setHraOrderType(HraOrderType.MA_PREPAY);
//				} else {
//					order.setHraOrderType(HraOrderType.MA_NORMAL);
//				}
//			}
            else if(OrderType.TYPE_B2BAGENT==order.getType()){             
                order.setHraOrderType(HraOrderType.AGENT_B2B);
        
            }
			else if (OrderType.TYPE_114 != order.getType()){				
					if (order.isCreditAssured()) {
						order.setHraOrderType(HraOrderType.MANGO_SURETY);
					} else if (order.isPrepayOrder()) {
						order.setHraOrderType(HraOrderType.MANGO_PREPAY);
					} else {
						order.setHraOrderType(HraOrderType.MANGO_NORMAL);
					}				
			}
			else{ 			
					if (order.isCreditAssured()) {
						order.setHraOrderType(HraOrderType.SURETY_114);
					} else if (order.isPrepayOrder()) {
						order.setHraOrderType(HraOrderType.PREPAY_114);
					} else {
						order.setHraOrderType(HraOrderType.NORMAL_114);
					}
				}		
			}
		}

	/**
	 * 处理114信用卡
	 * 
	 * @param order
	 * @param params
	 * @param forCmp
	 * @return
	 */
	public String handle114CreditCard(OrOrder order, Map params, boolean forCmp) {
		StringBuffer strCmp = new StringBuffer("");
		List cardList = order.getCardTempList();
		OrCreditCardTemp cardTemp = null;
        if (null == cardList || 0 >= cardList.size()) {
			cardTemp = new OrCreditCardTemp();
			MyBeanUtil.copyProperties(cardTemp, params);
			cardTemp.setOrder(order);
			cardTemp.setMemberId(order.getMemberId());
			cardList.add(cardTemp);
		} else {
			cardTemp = (OrCreditCardTemp) cardList.get(0);
			OrCreditCardTemp cmpCard = new OrCreditCardTemp();
			MyBeanUtil.copyProperties(cmpCard, params);
			boolean bModify = false;

			String strCur, strNew;
			strCur = cardTemp.getCardHolder();
			strNew = cmpCard.getCardHolder();
			if (!StringUtil.StringEquals1(strCur, strNew)) {
				cardTemp.setCardHolder(strNew);
				if (forCmp) {
					if (!bModify) {
						strCmp.append("修改信用卡:");
						bModify = true;
					}
                    strCmp.append("&nbsp;持卡人:" + getStr(strCur) + "-><font color='red'>"
                        + getStr(strNew) + "</font>");
				}
			}
			strCur = cardTemp.getCertificate();
			strNew = cmpCard.getCertificate();
			if (!StringUtil.StringEquals1(strCur, strNew)) {
				cardTemp.setCertificate(strNew);
				if (forCmp) {
					if (!bModify) {
						strCmp.append("修改信用卡:");
						bModify = true;
					}
					strCmp.append("&nbsp;证件类型:"
                        + getStr(resourceManager.getDescription(ResType.CERTIFICATETYPE, strCur))
							+ "-><font color='red'>"
                        + getStr(resourceManager.getDescription(ResType.CERTIFICATETYPE, strNew))
							+ "</font>");
				}
			}
			strCur = cardTemp.getCertificateNo();
			strNew = cmpCard.getCertificateNo();
			if (!StringUtil.StringEquals1(strCur, strNew)) {
				cardTemp.setCertificateNo(strNew);
				if (forCmp) {
					if (!bModify) {
						strCmp.append("修改信用卡:");
						bModify = true;
					}
                    strCmp.append("&nbsp;证件号码:" + getStr(strCur) + "-><font color='red'>"
                        + getStr(strNew) + "</font>");
				}
			}
			strCur = cardTemp.getCreditCardNo();
			strNew = cmpCard.getCreditCardNo();
			if (!StringUtil.StringEquals1(strCur, strNew)) {
				cardTemp.setCreditCardNo(strNew);
				if (forCmp) {
					if (!bModify) {
						strCmp.append("修改信用卡:");
						bModify = true;
					}
                    strCmp.append("&nbsp;卡号:" + getStr(strCur) + "-><font color='red'>"
                        + getStr(strNew) + "</font>");
				}
			}
			strCur = cardTemp.getCreditType();
			strNew = cmpCard.getCreditType();
			if (!StringUtil.StringEquals1(strCur, strNew)) {
				cardTemp.setCreditType(strNew);
				if (forCmp) {
					if (!bModify) {
						strCmp.append("修改信用卡:");
						bModify = true;
					}
					strCmp.append("&nbsp;卡种:"
                        + getStr(resourceManager.getDescription(ResType.CREDITCARDTYPE, strCur))
							+ "-><font color='red'>"
                        + getStr(resourceManager.getDescription(ResType.CREDITCARDTYPE, strNew))
							+ "</font>");
				}
			}
			strCur = cardTemp.getVerifyCode();
			strNew = cmpCard.getVerifyCode();
			if (!StringUtil.StringEquals1(strCur, strNew)) {
				cardTemp.setVerifyCode(strNew);
				if (forCmp) {
					if (!bModify) {
						strCmp.append("修改信用卡:");
						bModify = true;
					}
                    strCmp.append("&nbsp;验证码:" + getStr(strCur) + "-><font color='red'>"
                        + getStr(strNew) + "</font>");
				}
			}
			strCur = cardTemp.getExpireDate();
			strNew = cmpCard.getExpireDate();
			if (!StringUtil.StringEquals1(strCur, strNew)) {
				cardTemp.setExpireDate(strNew);
				if (forCmp) {
					if (!bModify) {
						strCmp.append("修改信用卡:");
						bModify = true;
					}
                    strCmp.append("&nbsp;有效期:" + getStr(strCur) + "-><font color='red'>"
                        + getStr(strNew) + "</font>");
				}
			}
			if(bModify) {
				strCmp.append("<br>");
			}
		}

		return strCmp.toString();
	}

	/**
	 * 设置订单基本修改信息
	 * 
	 * @param order
	 * @param tempOrder
	 * @param strCmp
	 * @param param
	 * 
	 * @return true: 联系人信息有所修改 false: 联系人信息没有修改
	 */
	public boolean setBasicOrderLog(OrOrder order, OrOrder tempOrder,
			StringBuffer strCmp, Map param) {
		boolean bRes = false;
		// 房间数量的修改属于基本信息修改
        /*
         * if (order.getRoomQuantity() != tempOrder.getRoomQuantity()) {
         * strCmp.append("房间数量:<font color='blue'>" + order.getRoomQuantity() +
         * "</font>&nbsp;改成&nbsp;<font color='red'>" + tempOrder.getRoomQuantity() + "</font><br>");
         * }
         */


		if  (!StringUtil.StringEquals1(order.getSource(),tempOrder.getSource())){
            strCmp.append("请求来源:<font color='blue'>"
                + getStr(resourceManager.getDescription(ResType.ORIGIN, order.getSource()))
					+ " </font>&nbsp;改成&nbsp;<font color='red'>" 
                + getStr(resourceManager.getDescription(ResType.ORIGIN, tempOrder.getSource()))
                + "</font><br>");
		}


		if  (order.getVipLevel() != tempOrder.getVipLevel()){
            strCmp.append("VIP级别:<font color='blue'>"
                + getStr(resourceManager.getDescription(ResType.VIPLEVEL, order.getVipLevel()))
					+ " </font>&nbsp;改成&nbsp;<font color='red'>" 
                + getStr(resourceManager.getDescription(ResType.VIPLEVEL, tempOrder.getVipLevel()))
                + "</font><br>");
		}
		
        if (!StringUtil.StringEquals1(String.valueOf(order.isCreditAssured()), String
            .valueOf(tempOrder.isCreditAssured()))) {
			strCmp.append("是否担保:<font color='blue'>" + order.isCreditAssured() 
                + " </font>&nbsp;改成&nbsp;<font color='red'>" + tempOrder.isCreditAssured()
                + "</font><br>");
		}
		
        if (0 != Double.compare(order.getSuretyPrice(),tempOrder.getSuretyPrice())
            && null != param.get("suretyPrice")) {
			strCmp.append("担保金额(RMB￥):&nbsp;<font color='blue'>" + order.getSuretyPrice() 
                + " </font>&nbsp;改成&nbsp;<font color='red'>" + tempOrder.getSuretyPrice()
                + "</font><br>");
		}
		
        if (null != param.get("isNotifyBalance")) {
			strCmp.append("<font color='red'>设置通知结算组</font><br>");		
		}
		
		String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";   
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT,Locale.CHINA);
		String newPrepayLimitDate = null;
		if(!StringUtil.DateEquals1(order.getPrepayLimitDate(), tempOrder.getPrepayLimitDate())){
            if (null != tempOrder.getPrepayLimitDate())
                newPrepayLimitDate = sdf.format(tempOrder.getPrepayLimitDate());
			strCmp.append("酒店付款时限:&nbsp;<font color='blue'>" + order.getPrepayLimitDate() 
                + " </font>&nbsp;改成&nbsp;<font color='red'>" + newPrepayLimitDate + "</font><br>");
		}
		
		String newGuestPrepayLimitDate = null;
        if (!StringUtil.DateEquals1(order.getGuestPrepayLimitDate(), tempOrder
            .getGuestPrepayLimitDate())) {
            if (null != tempOrder.getGuestPrepayLimitDate())
                newGuestPrepayLimitDate = sdf.format(tempOrder.getGuestPrepayLimitDate());
			strCmp.append("要求客人付款时限:&nbsp;<font color='blue'>" + order.getGuestPrepayLimitDate() 
                + " </font>&nbsp;改成&nbsp;<font color='red'>" + newGuestPrepayLimitDate
                + "</font><br>");
		}		
		
        if (!StringUtil.StringEquals1(order.getArrivalTraffic(), tempOrder.getArrivalTraffic())) {
            strCmp.append("交通工具:"
							+ "<font color='blue'>"
                + getStr(resourceManager.getDescription(ResType.VEHICLETYPEA, order
											.getArrivalTraffic()))
							+ "</font>&nbsp;改成&nbsp;<font color='red'>"
                + getStr(resourceManager.getDescription(ResType.VEHICLETYPEA, tempOrder
                    .getArrivalTraffic())) + "</font><br>");
		}
		if (!StringUtil.StringEquals1(order.getFlight(), tempOrder.getFlight())) {
            strCmp.append("班次:" + "<font color='blue'>" + getStr(order.getFlight())
                + "</font>&nbsp;改成&nbsp;<font color='red'>" + getStr(tempOrder.getFlight())
                + "</font><br>");
		}
        if (!StringUtil.StringEquals1(order.getArrivalTime(), tempOrder.getArrivalTime())) {
            strCmp.append("到店时间 最早:" + "<font color='blue'>" + order.getArrivalTime()
                + "</font>&nbsp;改成&nbsp;<font color='red'>" + tempOrder.getArrivalTime()
                + "</font><br>");
		}
		if (!StringUtil.StringEquals1(order.getLatestArrivalTime(), tempOrder
				.getLatestArrivalTime())) {
            strCmp.append("到店时间 最晚:" + "<font color='blue'>" + getStr(order.getLatestArrivalTime())
					+ "</font>&nbsp;改成&nbsp;<font color='red'>"
					+ getStr(tempOrder.getLatestArrivalTime()) + "</font><br>");
		}
        if (!StringUtil.StringEquals1(order.getLinkMan(), tempOrder.getLinkMan())
            || !StringUtil.StringEquals1(order.getTitle(), tempOrder.getTitle())) {
            strCmp.append("联系人:" + "<font color='blue'>" + getStr(order.getLinkMan()) + "("
                + getStr(resourceManager.getDescription(ResType.GENDER, order.getTitle())) + ")"
                + "</font>&nbsp;改成&nbsp;<font color='red'>" + getStr(tempOrder.getLinkMan()) + "("
                + getStr(resourceManager.getDescription(ResType.GENDER, tempOrder.getTitle()))
					+ ")</font><br>");
			bRes = true;
		}
		if (!StringUtil.StringEquals1(order.getMobile(), tempOrder.getMobile())) {
            strCmp.append("手机:" + "<font color='blue'>" + getStr(order.getMobile())
                + "</font>&nbsp;改成&nbsp;<font color='red'>" + getStr(tempOrder.getMobile())
                + "</font><br>");
			bRes = true;
		}
        if (!StringUtil.StringEquals1(order.getTelephone(), tempOrder.getTelephone())) {
            strCmp.append("电话:" + "<font color='blue'>" + getStr(order.getTelephone())
                + "</font>&nbsp;改成&nbsp;<font color='red'>" + getStr(tempOrder.getTelephone())
                + "</font><br>");
			bRes = true;
		}
		if (!StringUtil.StringEquals1(order.getCustomerFax(), tempOrder.getCustomerFax())) {
            strCmp.append("传真:" + "<font color='blue'>" + getStr(order.getCustomerFax())
                + "</font>&nbsp;改成&nbsp;<font color='red'>" + getStr(tempOrder.getCustomerFax())
                + "</font><br>");
			bRes = true;
		}
		if (!StringUtil.StringEquals1(order.getEmail(), tempOrder.getEmail())) {
            strCmp.append("电子邮件:" + "<font color='blue'>" + getStr(order.getEmail())
                + "</font>&nbsp;改成&nbsp;<font color='red'>" + getStr(tempOrder.getEmail())
                + "</font><br>");
			bRes = true;
		}
		if (order.getConfirmType() != tempOrder.getConfirmType()) {
            strCmp.append("预订完成确认方式:" + "<font color='blue'>"
                + resourceManager.getDescription(ResType.CONFIRMTYPE, order.getConfirmType())
					+ "</font>&nbsp;改成&nbsp;<font color='red'>"
                + resourceManager.getDescription(ResType.CONFIRMTYPE, tempOrder.getConfirmType())
                + "</font><br>");
			bRes = true;
		}
        if (!StringUtil.StringEquals1(order.getSpecialRequest(), tempOrder.getSpecialRequest())) {
            strCmp.append("特殊要求:" + "<font color='blue'>" + getStr(order.getSpecialRequest())
                + "</font>&nbsp;改成&nbsp;<font color='red'>" + getStr(tempOrder.getSpecialRequest())
                + "</font><br>");
		}

		
		
		OrRemark remark = order.getRemark();
        if (null != remark) {
			OrRemark tempRemark = new OrRemark();
			MyBeanUtil.copyProperties(tempRemark, param);

			String strRemark;
			String strTempRemark = tempRemark.getPrivateRemark();

			if (StringUtil.isValidStr(strTempRemark)) {
                strCmp.append("内部备注:"
								+ "增加:&nbsp;<font color='red'>"
                    + (null != strTempRemark && 100 < strTempRemark.length() ? strTempRemark
										.substring(0, 99)
                        + ".." : getStr(strTempRemark)) + "</font><br>");
			}

			strRemark = remark.getMemberRemark();
			strTempRemark = tempRemark.getMemberRemark();
			if (!StringUtil.StringEquals1(strRemark, strTempRemark)) {
                strCmp.append("给客人的备注:"
								+ "<font color='blue'>"
                    + (null != strRemark && 100 < strRemark.length() ? strRemark.substring(0, 99)
										+ ".." : getStr(strRemark))
								+ "</font>&nbsp;改成&nbsp;<font color='red'>"
                    + (null != strTempRemark && 100 < strTempRemark.length() ? strTempRemark
										.substring(0, 99)
                        + ".." : getStr(strTempRemark)) + "</font><br>");
			}

			strRemark = remark.getHotelRemark();
			strTempRemark = tempRemark.getHotelRemark();
			if (!StringUtil.StringEquals1(strRemark, strTempRemark)) {
                strCmp.append("给酒店的备注:"
								+ "<font color='blue'>"
                    + (null != strRemark && 100 < strRemark.length() ? strRemark.substring(0, 99)
										+ ".." : getStr(strRemark))
								+ "</font>&nbsp;改成&nbsp;<font color='red'>"
                    + (null != strTempRemark && 100 < strTempRemark.length() ? strTempRemark
										.substring(0, 99)
                        + ".." : getStr(strTempRemark)) + "</font><br>");
			}
		}
		
		
		// 以下为cc修改回传，酒店，客人确认等状态
		
		// 如果是前台修改则不用处理 
		String isFromFront = (String)param.get("isFromFront");
        boolean bFromFront = StringUtil.isValidStr(isFromFront) && isFromFront.equals("1") ? true
            : false;
		if(bFromFront) {
			return bRes;			
		}		

		boolean bChange = false;
		boolean bFirst = true;
		boolean bOld = order.isSendedHotelFax();
		boolean bNew = tempOrder.isSendedHotelFax();
		// 已发送酒店确认		
		if (bOld != bNew) {
			order.setSendedHotelFax(bNew);
			if(bFirst) {
				strCmp.append("订单改为:");
				bFirst = false;
			}
            strCmp.append(" <font color='red'>" + (bNew ? "已发送酒店确认" : "未发送酒店确认") + "</font>");
			bChange = true;
		}
		
		bOld = order.isHotelConfirmTel();
		bNew = tempOrder.isHotelConfirmTel();
		// 已口头酒店确认		
		if (bOld != bNew) {
			if(bFirst) {
				strCmp.append("订单改为:");
				bFirst = false;
			}
			order.setHotelConfirmTel(bNew);
            strCmp.append(" <font color='red'>" + (bNew ? "已口头确认酒店" : "未口头确认酒店") + "</font>");
			bChange = true;
		}
		
		bOld = order.isHotelConfirmFax();
		bNew = tempOrder.isHotelConfirmFax();
		// 已书面酒店确认		
		if (bOld != bNew) {
			if(bFirst) {
				strCmp.append("订单改为:");
				bFirst = false;
			}
			order.setHotelConfirmFax(bNew);
            strCmp.append(" <font color='red'>" + (bNew ? "已书面确认酒店" : "未书面确认酒店") + "</font>");
			bChange = true;
		}
		
		bOld = order.isHotelConfirmFaxReturn();
		bNew = tempOrder.isHotelConfirmFaxReturn();
		// 已收酒店回传		
		if (bOld != bNew) {
			if(bFirst) {
				strCmp.append("订单改为:");
				bFirst = false;
			}
			order.setHotelConfirmFaxReturn(bNew);
            strCmp.append(" <font color='red'>" + (bNew ? "已收回传" : "未收回传") + "</font>");
			bChange = true;
		}
		
		bOld = order.isCustomerConfirm();
		bNew = tempOrder.isCustomerConfirm();
		// 已确认客户		
		if (bOld != bNew) {
			if(bFirst) {
				strCmp.append("订单改为:");
				bFirst = false;
			}
			order.setCustomerConfirm(bNew);
            strCmp.append(" <font color='red'>" + (bNew ? "已确认客户" : "未确认客户") + "</font>");
			bChange = true;
		}
		if(bChange) {
			strCmp.append("<br>");
		}
		
		return bRes;
	}	
	
	/**
	 * 修改订单时处理修改配送信息，同时添加修改日志
	 * 
	 * @param fulfill
	 * @param strCmp
	 * @param params
	 */
	public void handleModifyFulfill(OrFulfillment fulfill, StringBuffer strCmp, Map params) {
							
		boolean bFirst = true;
		
		String strOri = fulfill.getDeliveryType();
		String strNew = (String)params.get("deliveryType");
		if (!StringUtil.StringEquals1(strOri, strNew)) {
			if(bFirst) {
				strCmp.append("修改配送信息:");
				bFirst = false;
			}
            strCmp.append("&nbsp;配送方式:<font color='blue'>"
                + getStr(resourceManager.getDescription(ResType.FULFILLTYPE, strOri))
					+ "</font>&nbsp;改成&nbsp;<font color='red'>" 
                + getStr(resourceManager.getDescription(ResType.FULFILLTYPE, strNew)) + "</font>");
		}
		strOri = String.valueOf(fulfill.getFulfillTaskType());
		strNew = (String)params.get("fulfillTaskType");
		if (!StringUtil.StringEquals1(strOri, strNew)) {
			if(bFirst) {
				strCmp.append("修改配送信息:");
				bFirst = false;
			}
            strCmp.append("&nbsp;任务类型:<font color='blue'>"
                + getStr(resourceManager.getDescription(ResType.FULFILLTASKTYPE, strOri))
					+ "</font>&nbsp;改成&nbsp;<font color='red'>"
                + getStr(resourceManager.getDescription(ResType.FULFILLTASKTYPE, strNew))
                + "</font>");
		}
		strOri = fulfill.getDeliveryCityID();
		String strNewCity = (String)params.get("deliveryCityID");
		if (!StringUtil.StringEquals1(strOri, strNewCity)) {
			if(bFirst) {
				strCmp.append("修改配送信息:");
				bFirst = false;
			}
            strCmp.append("&nbsp;配送城市:<font color='blue'>"
                + InitServlet.diliveryCityObj.get(strOri)
					+ "</font>&nbsp;改成&nbsp;<font color='red'>" 
					+ InitServlet.diliveryCityObj.get(strNewCity) + "</font>");
		}
		strOri = fulfill.getDeliveryZoneID();
		strNew = (String)params.get("deliveryZoneID");
		if (!StringUtil.StringEquals1(strOri, strNew)) {
			if(bFirst) {
				strCmp.append("修改配送信息:");
				bFirst = false;
			}
            strCmp.append("&nbsp;配送城区:<font color='blue'>"
                + InitServlet.mapDiliveryCityZoneObj.get(fulfill.getDeliveryCityID()).get(strOri)
					+ "</font>&nbsp;改成&nbsp;<font color='red'>" 
					+ InitServlet.mapDiliveryCityZoneObj.get(strNewCity).get(strNew) + "</font>");			
		}	
		double fOri = fulfill.getAccount();
		double fNew = Double.parseDouble((String)params.get("account"));		
        if (0 != Double.compare(fOri,fNew)) {
			if(bFirst) {
				strCmp.append("修改配送信息:");
				bFirst = false;
			}
			strCmp.append("&nbsp;配送收费:<font color='blue'>" + fOri 
                + "</font>&nbsp;改成&nbsp;<font color='red'>" + fNew + "</font>");
		}
		strOri = fulfill.getDeliveryAddress();
		strNew = (String)params.get("deliveryAddress");
		if (!StringUtil.StringEquals1(strOri, strNew)) {
			if(bFirst) {
				strCmp.append("修改配送信息:");
				bFirst = false;
			}
			strCmp.append("&nbsp;配送详细地址:<font color='blue'>" + strOri 
                + "</font>&nbsp;改成&nbsp;<font color='red'>" + strNew + "</font>");
		}
        strOri = DateUtil.dateToString(fulfill.getDeliveryDate()) + " " + fulfill.getDeliveryTime()
            + "<s>&nbsp;</s>" + fulfill.getDeliveryTimeEnd();
		strNew = (String)params.get("deliveryDate") + " " + (String)params.get("deliveryTime") 
				+ "<s>&nbsp;</s>" + (String)params.get("deliveryTimeEnd");
		if (!StringUtil.StringEquals1(strOri, strNew)) {
			if(bFirst) {
				strCmp.append("修改配送信息:");
				bFirst = false;
			}
			strCmp.append("&nbsp;配送时间:<font color='blue'>" + strOri 
                + "</font>&nbsp;改成&nbsp;<font color='red'>" + strNew + "</font>");
		}
		strOri = String.valueOf(fulfill.isFulfillConfirm());
        strNew = null != (String)params.get("fulfillConfirm") ? "true" : "false";
		if (!StringUtil.StringEquals1(strOri, strNew)) {
			if(bFirst) {
				strCmp.append("修改配送信息:");
				bFirst = false;
			}
			strCmp.append("&nbsp;配送入住凭证:<font color='blue'>" + strOri 
                + "</font>&nbsp;改成&nbsp;<font color='red'>" + strNew + "</font>");
			fulfill.setFulfillConfirm(Boolean.parseBoolean(strNew));
		}
		strOri = fulfill.getAddresseeName();
		strNew = (String)params.get("addresseeName");
		if (!StringUtil.StringEquals1(strOri, strNew)) {
			if(bFirst) {
				strCmp.append("修改配送信息:");
				bFirst = false;
			}
			strCmp.append("&nbsp;配送联系人:<font color='blue'>" + strOri 
                + "</font>&nbsp;改成&nbsp;<font color='red'>" + strNew + "</font>");
		}
		strOri = fulfill.getAddresseePhone();
		strNew = (String)params.get("addresseePhone");
		if (!StringUtil.StringEquals1(strOri, strNew)) {
			if(bFirst) {
				strCmp.append("修改配送信息:");
				bFirst = false;
			}
			strCmp.append("&nbsp;配送联系电话:<font color='blue'>" + strOri 
                + "</font>&nbsp;改成&nbsp;<font color='red'>" + strNew + "</font>");
		}
		
		strOri = fulfill.getFulfillNote();
		strNew = (String)params.get("fulfillNote");
		if (!StringUtil.StringEquals1(strOri, strNew)) {
			if(bFirst) {
				strCmp.append("修改配送信息:");
				bFirst = false;
			}
			strCmp.append("&nbsp;给配送的备注:"
							+ "<font color='blue'>"
                + (null != strOri && 500 < strOri.length() ? strOri.substring(0, 499) + ".."
                    : getStr(strOri))
							+ "</font>&nbsp;改成&nbsp;<font color='red'>"
                + (null != strNew && 500 < strNew.length() ? strNew.substring(0, 499) + ".."
                    : getStr(strNew)) + "</font>");
		}
		strOri = fulfill.getFulfillPostCode();
		strNew = (String)params.get("fulfillPostCode");
		if (!StringUtil.StringEquals1(strOri, strNew)) {
			if(bFirst) {
				strCmp.append("修改配送信息:");
				bFirst = false;
			}
			strCmp.append("&nbsp;邮政编码:<font color='blue'>" + strOri 
                + "</font>&nbsp;改成&nbsp;<font color='red'>" + strNew + "</font>");
		}
		strOri = String.valueOf(fulfill.isInvoice());
		String tmpStr = (String)params.get("isInvoice"); 
		strNew = StringUtil.isValidStr(tmpStr) && tmpStr.equals("1") ? "true" : "false";
		if (!StringUtil.StringEquals1(strOri, strNew)) {
			if(bFirst) {
				strCmp.append("修改配送信息:");
				bFirst = false;
			}
			strCmp.append("&nbsp;需要发票:<font color='blue'>" + strOri 
                + "</font>&nbsp;改成&nbsp;<font color='red'>" + strNew + "</font>");
			fulfill.setInvoice(Boolean.parseBoolean(strNew));
		}
		if(fulfill.isInvoice()) {
			strOri = fulfill.getFulfillInvoiceTitle();
			strNew = (String)params.get("fulfillInvoiceTitle");
			if (!StringUtil.StringEquals1(strOri, strNew)) {
				if(bFirst) {
					strCmp.append("修改配送信息:");
					bFirst = false;
				}
				strCmp.append("&nbsp;发票抬头:<font color='blue'>" + strOri 
                    + "</font>&nbsp;改成&nbsp;<font color='red'>" + strNew + "</font>");
			}
			fOri = fulfill.getFulfillInvoiceFee();
			fNew = Double.parseDouble((String)params.get("fulfillInvoiceFee"));		
            if (0 != Double.compare(fOri,fNew)) {
				if(bFirst) {
					strCmp.append("修改配送信息:");
					bFirst = false;
				}
				strCmp.append("&nbsp;发票金额:<font color='blue'>" + fOri 
                    + "</font>&nbsp;改成&nbsp;<font color='red'>" + fNew + "</font>");
			}
		}
		if(!bFirst) {
			strCmp.append("<br>");
		}
		MyBeanUtil.copyProperties(fulfill, params);		
	}

	/**
	 * 设置订单紧急程度
     * 
     *  chenkeming Feb 18, 2009 12:00:50 AM
	 * @param order
	 * @param now
	 * @param member
	 * @param params
	 * @return
	 */
    public int getEmergency(OrOrder order, Date now, MemberDTO memberDTO, Map params) {
		//v2.7.1 订单需要记录客人的VIP级别 chenjiajie 2009-02-25
		String strVip = memberDTO.getViplevel();
		if(StringUtil.isValidStr(strVip)){
			order.setMemberVipLevel(translateToNumber(strVip));
		}else{
			order.setVipLevel(0);
		}		
        String sVipLevel = null != params ? (String) params.get("vipLevel") : null;
		String vipLevel;
		if (StringUtil.isValidStr(sVipLevel)) {
			vipLevel = sVipLevel;
		} else {
			vipLevel = "One";
		}
		String nVip;
        if (null == order.getID() || 0 == order.getID().longValue()) {
			String isInHotel = (String) params.get("isInHotel");
			if (StringUtil.isValidStr(isInHotel) && isInHotel.equals("1")) {
                return EmergencyLevel.ARRIVED;
				//order.setEmergencyLevel(EmergencyLevel.Arrived);				
				//bSetEmerg = true;
			} else {
				// 判断是否两小时内
				//判断入住时间 add by baofeng.si V2.3 2008-5-30 Start
				Date tempDate = order.getCheckinDate();
                if (10 > Integer.parseInt(order.getArrivalTime().substring(0,2))) {
					tempDate = DateUtil.getDate(tempDate, 1);
				}				
				String dateStr = DateUtil.toStringByFormat(tempDate, "yyyy-MM-dd ");
				//判断入住时间 add by baofeng.si V2.3 2008-5-30 End
				
				String day1 = dateStr + order.getArrivalTime();
				String day2 = DateUtil.datetimeToString(now);
				
                if (DateUtil.getDateByHour(DateUtil.stringToDatetime(day2), 2).getTime() >= DateUtil
                    .stringToDatetime(day1).getTime()) {
					return EmergencyLevel.IN_TWO_HOURS;
                    /*
                     * order.setEmergencyLevel(EmergencyLevel.IN_TWO_HOURS); bSetEmerg = true;
                     */
				}
			}
			if (StringUtil.isValidStr(strVip)) {
				if (translateToNumber(vipLevel) <=translateToNumber(strVip)) {
					nVip = strVip;
				} else {
					nVip = vipLevel;
				}
			} else {// 该会员没有VIP level
				nVip = vipLevel;
			}
			int nEmerg = EmergencyLevel.VIP1;
			if(null != nVip && "Four".equals(nVip)){
				nEmerg = EmergencyLevel.VIP4;
			}else if(null != nVip && "Three".equals(nVip)){
				nEmerg = EmergencyLevel.VIP3;
			}else if(null != nVip && "Two".equals(nVip)){
				nEmerg = EmergencyLevel.VIP2;
			}else if(null != nVip && "One".equals(nVip)){
				nEmerg = EmergencyLevel.VIP1;
			}
			return nEmerg;
			// order.setEmergencyLevel(nEmerg);
		} else if (order.getEmergencyLevel() != EmergencyLevel.IN_TWO_HOURS
            && order.getEmergencyLevel() != EmergencyLevel.ARRIVED) {
			
			//判断入住时间 add by baofeng.si V2.3 2008-5-30 Start
			Date tempDate = order.getCheckinDate();
            if (10 > Integer.parseInt(order.getArrivalTime().substring(0,2))) {
				tempDate = DateUtil.getDate(tempDate, 1);
			}				
			String dateStr = DateUtil.toStringByFormat(tempDate, "yyyy-MM-dd ");
			//判断入住时间 add by baofeng.si V2.3 2008-5-30 End
			
			String day1 = dateStr + order.getArrivalTime();
            if (DateUtil.getDateByHour(now, 2).getTime() >= DateUtil.stringToDatetime(day1)
                .getTime()) {
				return EmergencyLevel.IN_TWO_HOURS;
                /*
                 * order.setEmergencyLevel(EmergencyLevel.IN_TWO_HOURS); isTwoHours = true;
                 */
			}

			if (StringUtil.isValidStr(strVip)) {
				if (translateToNumber(vipLevel) <= translateToNumber(strVip)) {
					nVip = strVip;
				} else {
					nVip = vipLevel;
				}
			} else {// 该会员没有VIP level
				nVip = vipLevel;
			}
			int nEmerg = EmergencyLevel.VIP1;
			if(null != nVip && "Four".equals(nVip)){
				nEmerg = EmergencyLevel.VIP4;
			}else if(null != nVip && "Three".equals(nVip)){
				nEmerg = EmergencyLevel.VIP3;
			}else if(null != nVip && "Two".equals(nVip)){
				nEmerg = EmergencyLevel.VIP2;
			}else if(null != nVip && "One".equals(nVip)){
				nEmerg = EmergencyLevel.VIP1;
			}
			return nEmerg;
			// order.setEmergencyLevel(nEmerg);
		}
		return order.getEmergencyLevel();
	}

	/**
	 * 新增预付单时，处理表单的预付款数据
	 * 
	 * @param order
	 * @param params
	 */
	public void handleNewPrepay(OrOrder order, Map params, UserWrapper roleUser) {

		double amount = 0.0;
		double money;
		//增加了代金券，需要循环到代金券
		for (int i = PrepayType.Cash - 1; i < PrepayType.UNTIONPAYPHONE; i++) {
			String prepayInput = (String) params.get("prepay" + PrepayType.strNames[i] + "Input");
			if (StringUtil.isValidStr(prepayInput)) {
				money = Double.parseDouble(prepayInput);
                if (0.001 < money) {

					OrPayment payment = new OrPayment();
					payment.setMoney(money);

					payment.setPaySucceed(false);
					payment.setPayType(i + 1);
					Date now = new Date();
					payment.setCreator(roleUser.getName());
					payment.setOperator(roleUser.getName());
					payment.setCreateTime(now);
					payment.setOperateTime(now);
					payment.setOrder(order);
					order.getPaymentList().add(payment);

					amount += money;
				}
			}
		}

		order.setPrepayTotalRmb(amount);
		order.setHasPrepayed(false);
	}

	/**
	 * 处理表单的退款数据
	 * 
	 * @param order
	 * @param params
	 * @param forCmp
	 * @param roleUser
	 */
    public String handleRefund(OrOrder order, Map params, boolean forCmp, UserWrapper roleUser) {
		StringBuffer strCmp = new StringBuffer("");
		double amount = 0.0;
		Map refundMap = new HashMap();
		for (int i = PrepayType.Cash - 1; i < PrepayType.UNTIONPAYPHONE; i++) {
			String refundInput = (String) params.get("refund" + PrepayType.strNames[i] + "Input");
			if (StringUtil.isValidStr(refundInput)) {
				double money = Double.parseDouble(refundInput);
                if (0.001 < money) {
					OrRefund refund = new OrRefund();
					refund.setMoney(money);
					refund.setRefundType(i + 1);
					refundMap.put(i + 1, refund);
					amount += money;

					String notes = (String) params.get("refund" + PrepayType.strNames[i] + "Notes");
					refund.setNotes(notes);
				}
			}
		}
		order.setRefund(amount);
		order.setHasRefund(false);

		// delete
		Map extMap = new HashMap();
		OrRefund extRefund = null;
		List extList = order.getRefundList();
        boolean bFirstEmpty = 0 < extList.size() ? false : true;
		for (int i = 0; i < extList.size(); i++) {
			extRefund = (OrRefund) extList.get(i);
			if (refundMap.containsKey(extRefund.getRefundType())) {
				extMap.put(extRefund.getRefundType(), extRefund);
			} else {
                extList.remove(i--);// parasoft-suppress PB.FLVA "业务需要"
				if (forCmp) {
					strCmp.append("删除退款方式:<font color='blue'>"
                        + resourceManager.getDescription(ResType.PREPAYTYPE, extRefund
											.getRefundType()) + "</font><br>");
				}
			}
		}

		// update or insert
		boolean bModify;
		for (Iterator iter = refundMap.entrySet().iterator(); iter.hasNext();) {
			Map.Entry element = (Map.Entry) iter.next();
			OrRefund tempRefund = (OrRefund) element.getValue();

			bModify = false;
			extRefund = (OrRefund) extMap.get(tempRefund.getRefundType());
            if (null != extRefund) { // update
                if (0.01 < Math.abs(extRefund.getMoney() - tempRefund.getMoney())
                    || !StringUtil.StringEquals1(extRefund.getNotes(), tempRefund.getNotes())) {
					bModify = true;
				}
				if (bModify) {
					strCmp.append("修改退款方式:<font color='red'>"
                        + resourceManager.getDescription(ResType.PREPAYTYPE, extRefund
											.getRefundType()) + "</font>");
                    if (0.01 < Math.abs(extRefund.getMoney() - tempRefund.getMoney())) {
                        strCmp.append("&nbsp;金额:" + extRefund.getMoney() + "-><font color='red'>"
								+ tempRefund.getMoney() + "</font>");
					}
                    if (!StringUtil.StringEquals1(extRefund.getNotes(), tempRefund.getNotes())) {
                        strCmp.append("&nbsp;备注:" + extRefund.getNotes() + "-><font color='red'>"
								+ tempRefund.getNotes() + "</font>");
					}
					strCmp.append("<br>");
					extRefund.setOperator(roleUser.getName());
					extRefund.setOperateTime(new Date());
					extRefund.setNotes(tempRefund.getNotes());
					extRefund.setMoney(tempRefund.getMoney());
				}

			} else { // insert
				Date now = new Date();
				tempRefund.setCreator(roleUser.getName());
				tempRefund.setCreateTime(now);
				tempRefund.setOperator(roleUser.getName());
				tempRefund.setOperateTime(now);
				tempRefund.setRefundSucceed(false);
				tempRefund.setOrder(order);
				extList.add(tempRefund);
				if (forCmp) {
					strCmp.append("新增退款方式:<font color='red'>"
                        + resourceManager.getDescription(ResType.PREPAYTYPE, tempRefund
											.getRefundType()) + "</font><br>");
				}
			}
		}

        if (bFirstEmpty && 0 < extList.size() && order.isNeedRefund()) {
			order.setOrderState(OrderState.HAS_CREATE_REFUND);
        } else if (!bFirstEmpty && 0 >= extList.size()) {
			order.setOrderState(OrderState.HAS_PAID);
			order.setNeedRefund(false);
		}

		return strCmp.toString();

	}

	/**
	 * 修改预付单时，处理表单的预付款数据<br>
	 * 如果是网站预付单，则删除支付不成功的在线支付方式
	 * 
	 * @param order
	 * @param params
     * @param forCmp
     *            - 是否要记录修改日志
	 * @param member
	 * @param roleUser
	 */
    public String handleEditPrepay(OrOrder order, Map params, boolean forCmp, MemberDTO member,
        UserWrapper roleUser) throws BusinessException {
		StringBuffer strCmp = new StringBuffer("");

		Date now = new Date();
		double amount = 0.0;
		Map prepayMap = new HashMap();
		for (int i = PrepayType.Cash - 1; i < PrepayType.UNTIONPAYPHONE; i++) {
			String prepayInput = (String) params.get("prepay" + PrepayType.strNames[i] + "Input");
			if (StringUtil.isValidStr(prepayInput)) {
				double money = Double.parseDouble(prepayInput);
                if (0.001 < money) {
					OrPayment payment = new OrPayment();
					payment.setMoney(money);
					payment.setPayType(i + 1);
					prepayMap.put(i + 1, payment);
					amount += money;
					payment.setCreateTime(now);
					payment.setOperateTime(now);
					String notes = (String) params.get("prepay" + PrepayType.strNames[i] + "Notes");
					payment.setNotes(notes);
				}
			}
		}
		order.setPrepayTotalRmb(amount);

		// delete
		Map extMap = new HashMap();
		OrPayment extPayment = null;
		List extList = order.getPaymentList();
		boolean bDel = false; // 是否有删除支付方式
		for (int i = 0; i < extList.size(); i++) {
			extPayment = (OrPayment) extList.get(i);
			if (prepayMap.containsKey(extPayment.getPayType())) {
				extMap.put(extPayment.getPayType(), extPayment);
			} else if (!extPayment.isPaySucceed()) { // 如果已经预付成功则不扣除
                extList.remove(i--);// parasoft-suppress PB.FLVA "业务需要"
                bDel = true;
				if (!extPayment.isWebWay() && forCmp) { // 删除网站订单在线支付方式不用log
					strCmp.append("删除预付付款方式:<font color='blue'>"
                        + resourceManager.getDescription(ResType.PREPAYTYPE, extPayment
                            .getPayType()) + "</font><br>");
				}
			}
		}

		// update or insert
		boolean bModify;
		boolean bModifyMoney = false; // 是否修改支付方式的金额
		boolean bAdd = false; // 是否添加新的支付方式
		for (Iterator iter = prepayMap.entrySet().iterator(); iter.hasNext();) {
			Map.Entry element = (Map.Entry) iter.next();
			OrPayment tempPayment = (OrPayment) element.getValue();

			bModify = false;
			extPayment = (OrPayment) extMap.get(tempPayment.getPayType());
            if (null != extPayment) { // update
                if (0.01 < Math.abs(extPayment.getMoney()
						- tempPayment.getMoney())) {
					bModify = true;
					bModifyMoney = true;
				}
				if (!StringUtil.StringEquals1(extPayment.getNotes(),
						tempPayment.getNotes())) {
					bModify = true;
				}
				if (bModify) {
					if (forCmp) {
						strCmp.append("修改预付付款方式:<font color='red'>"
                            + resourceManager.getDescription(ResType.PREPAYTYPE, extPayment
												.getPayType()) + "</font>");
                        if (0.01 < Math.abs(extPayment.getMoney() - tempPayment.getMoney())) {
							strCmp.append("&nbsp;金额:" + extPayment.getMoney()
                                + "-><font color='red'>" + tempPayment.getMoney() + "</font>");
						}
                        if (!StringUtil
                            .StringEquals1(extPayment.getNotes(), tempPayment.getNotes())) {
							strCmp.append("&nbsp;备注:" + extPayment.getNotes()
                                + "-><font color='red'>" + tempPayment.getNotes() + "</font>");
						}
						strCmp.append("<br>");
					}
					extPayment.setMoney(tempPayment.getMoney());
					extPayment.setOperateTime(new Date());
					extPayment.setOperator(roleUser.getName());
					extPayment.setNotes(tempPayment.getNotes());
				}

			} else { // insert

                if (member.isMango() && tempPayment.getPayType() == PrepayType.Points) { // 扣会员积分
					String prepayInput1 = (String) params.get("prepayPtInput1");
					double points = Long.parseLong(prepayInput1);
					try {
						// TODO: 如果扣积分成功，但订单保存失败，则应该退积分
						PointDTO pt = null;
						if (order.getSource() != null
								&& order.getSource().equals(
										OrderSource.FROM_WEB)) {
							pt = pointsDelegate.changePonitsByMemberCd(
									member.getMembercd(), "mango", "1", points,
									order.getOrderCD(),
									BaseConstant.TRANSCHANNEL_NET,
									BaseConstant.TRANSCHANNELSN_NET);
						} else if (order.getSource() != null
								&& order.getSource().equals(
										OrderSource.FAN_TI_NET)) {
							pt = pointsDelegate.changePonitsByMemberCd(
									member.getMembercd(), "mango", "1", points,
									order.getOrderCD(),
									BaseConstant.TRANSCHANNEL_NET,
									BaseConstant.TRANSCHANNELSN_BIG);
						} else {
							pt = pointsDelegate.changePonitsByMemberCd(
									member.getMembercd(), "mango", "1", points,
									order.getOrderCD(),
									BaseConstant.TRANSCHANNEL_CC,
									BaseConstant.TRANSCHANNELSN_CC);
						}
                         
                        if (null != pt && StringUtil.isValidStr(pt.getRc())
                            && pt.getRc().equals("0")) {
                            long extPt = Long.parseLong(member.getPoint().getBalanceValue());
							extPt -= BigDecimal.valueOf(points).longValue();
							member.getPoint().setBalanceValue("" + extPt);
						} else {
							throw new BusinessException(ExceptionCode.POINTS_NOT_FOUND_ERROR);
						}						
					} catch (Exception e) {
                        throw new BusinessException(ExceptionCode.UPDATE_POINTS_ERROR, e);
					}
					tempPayment.setPaySucceed(true);
					tempPayment.setConfirmer(roleUser.getName());
					tempPayment.setConfirmTime(new Date());
				} else {
					tempPayment.setPaySucceed(false);
				}
                
				tempPayment.setCreator(roleUser.getName());
				tempPayment.setOperator(roleUser.getName());
				tempPayment.setCreateTime(now);
				tempPayment.setOperateTime(now);
				tempPayment.setOrder(order);
				extList.add(tempPayment);
				
				bAdd = true;
				
				if (forCmp) {
					strCmp.append("新增预付付款方式:<font color='red'>"
                        + resourceManager.getDescription(ResType.PREPAYTYPE, tempPayment
											.getPayType()) + "</font><br>");
				}
			}
		}
		
		/** QC1317: 积分支付问题反馈 add by chenkeming@2011.03.07 begin **/
        if ((bDel || bModifyMoney || bAdd) && OrderUtil.checkHasPrepayed(order)) {
            order.setHasPrepayed(true);
            order.setOrderState(OrderState.HAS_PAID);            
            // 中旅单积分全额支付成功后的处理
            if (order.isCtsHK()) {
                OrHandleLog handleLog = new OrHandleLog();
                handleLog.setOrder(order);
                handleLog.setModifiedTime(new Date());
                handleLog.setModifierName(roleUser.getName());
                handleLog.setModifierRole(roleUser.getLoginName());
                order.getLogList().add(handleLog);
                orderService.getPayFinishCts(order, handleLog, true);
            }
        }
		/** QC1317: 积分支付问题反馈 add by chenkeming@2011.03.07 end **/        

		return strCmp.toString();
	}

	/**
	 * mango会员新增订单为担保单或预付单时处理信用卡信息
	 * 
	 * @param order
	 * @param cardList
	 */
	public void handleNewCards(OrOrder order, List cardList) {
		String creditCardIds = "";
		boolean bFirst = true;
		for (int i = 0; i < cardList.size(); i++) {
			MemberCreditCard card = (MemberCreditCard) cardList.get(i);
			if (!card.isCardActive()) {
				continue;
			}
			if (!bFirst) {
				creditCardIds += ",";
			} else {
				bFirst = false;
			}
			creditCardIds += card.getCardId();
			OrCreditCard newCard = new OrCreditCard();
			newCard.setCardId(card.getCardId());
			newCard.setOrder(order);
			order.getCreditCardList().add(newCard);
		}
		order.setCreditCardIdsSelect(creditCardIds);
	}

	/**
	 * 保存信用卡列表
	 * 
	 * @param order
	 * @param fellowList
	 * @param forCmp
	 */
	public String saveOrderCards(OrOrder order, List cardList, boolean forCmp) {
		StringBuffer strCmp = new StringBuffer("");
		StringBuffer cardIds = new StringBuffer("");

		// 删除订单原有的信用卡
		HashMap cardMap = new HashMap();
		OrCreditCard extCard = null;
		List existCardList = order.getCreditCardList();
		for (int i = 0; i < existCardList.size(); i++) {
			extCard = (OrCreditCard) existCardList.get(i);
			boolean isExist = false;
			for (int j = 0; j < cardList.size(); j++) {
				MemberCreditCard tempCard = (MemberCreditCard) cardList.get(j);
				// 是否被删除
				if (tempCard.isCardActive()
                    && tempCard.getCardId().longValue() == extCard.getCardId().longValue()) {
					isExist = true;
					break;
				}
			}
			if (!isExist) {
                existCardList.remove(i--);// parasoft-suppress PB.FLVA "业务需要"
				if (forCmp) {
                    strCmp
                        .append("删除信用卡:<font color='blue'>" + extCard.getCardId() + "</font><br>");
				}
			} else {
				cardMap.put(extCard.getCardId(), extCard);
			}
		}

		// 更新订单的信用卡
		boolean bFirst = true;
		for (int i = 0; i < cardList.size(); i++) {
			MemberCreditCard tempCard = (MemberCreditCard) cardList.get(i);
			if (!tempCard.isCardActive()) {
				continue;
			}
			if (!bFirst) {
				cardIds.append(",");
			} else {
				bFirst = false;
			}
			cardIds.append("" + tempCard.getCardId());
			extCard = (OrCreditCard) cardMap.get(tempCard.getCardId());
            if (null == extCard) { // insert card
				OrCreditCard newCard = new OrCreditCard();
				newCard.setOrder(order);
				newCard.setCardId(tempCard.getCardId());
				existCardList.add(tempCard);
				if (forCmp) {
                    strCmp
                        .append("新增信用卡:<font color='red'>" + tempCard.getCardId() + "</font><br>");
				}
			}
		}
        if (0 < strCmp.length()) {
			order.setCreditCardIdsSelect(cardIds.toString());
		}
		return strCmp.toString();
	}

	/**
	 * 创建配送单。
	 * 
	 * @param order
	 * @param member
	 */
    public boolean createDeliveryBill(OrOrder order, MemberDTO memberDTO, UserWrapper roleUser,
        String sOrderCD) {
		String fulfillCD = null;
		DeliveryInfo deliveryInfo = new DeliveryInfo();
		deliveryInfo.setBillCreator(roleUser.getLoginName());
		deliveryInfo.setOrderId(order.getID());
		deliveryInfo.setOrderCD(sOrderCD);
		deliveryInfo.setBillResouce(2); // 2:酒店
		deliveryInfo.setUrgency(EmergencyUtil.convert(order.getEmergencyLevel()));
		
		deliveryInfo.setMemberCD(memberDTO.getMembercd());
        deliveryInfo.setMemberLevel(null == memberDTO.getViplevel() ? "1" : memberDTO.getViplevel());
		OrFulfillment fulfill = order.getFulfill();
		deliveryInfo.setAddresseeName(fulfill.getAddresseeName());
		deliveryInfo.setAddresseePhone(fulfill.getAddresseePhone());
		deliveryInfo.setDeliveryAddress(fulfill.getDeliveryAddress());
		deliveryInfo.setDeliveryCityID(fulfill.getDeliveryCityID());
		deliveryInfo.setDeliveryZoneID(fulfill.getDeliveryZoneID());
		deliveryInfo.setPostcode(fulfill.getFulfillPostCode());
		deliveryInfo.setAliasCode(memberDTO.getAliasid());//联各商家的代码
		
		
		/***************** 20090806 支付对接,配送接口更改新加 by Mob ***************************/
		
		String customerId = ParamServiceImpl.getInstance().getConfValue("{hotel.payment.customerId}");
		deliveryInfo.setOutTradeNo(customerId + sOrderCD + "00");        // 设置外部交易号
		deliveryInfo.setCustomerId(customerId);
		
//		List<OrPayment> PaymentList = order.getPaymentList();
//		for(OrPayment pay : PaymentList){
//		    
//		}
		
		deliveryInfo.setCardPayType("");                             // 离线支付类别,默认非离线,默认值空字符串
		deliveryInfo.setStageCount("0");                             // 分期付款数
		deliveryInfo.setBy1("");                                     // 备用字段,未定义
		deliveryInfo.setBy2("");                                     // 备用字段,未定义
		
		/*************************************************************************************/
		
		
		deliveryInfo.setAliasName(memberDTO.getCatogaryName());//联各商家的名称
			
		// 计算配送应收金额
		double leftFee = order.getOrderState() == OrderState.REFUND_SUCCESS ? (order
            .getPrepayTotalRmb() - order.getRefund()) : 0.0;
		double fulFee = 0.0;
		List<OrPayment> list = order.getPaymentList();
		StringBuffer payBuf = new StringBuffer("");
		for (int i = 0; i < list.size(); i++) {
			OrPayment payment = (OrPayment) list.get(i);	
			int payType = payment.getPayType();
			if(payType == PrepayType.Cash ||         // 现金离线支付
					payType == PrepayType.POS) {     // POS离线支付
				fulFee += payment.getMoney();
				deliveryInfo.setCardPayType("ALL");  // 离线支付类别,全额付款,酒店目前没有分期付款 20090806 by Mob
			}						
			
			if(payType > PrepayType.POS && !payment.isPaySucceed()) {
				continue;
			}
			
			// 20090821 积分方式不生成配送单给配送,因为不必收款.
			if(payType != PrepayType.Points){
            if (0 < i) {
				payBuf.append(",");	
			}
    			payBuf.append(PrepayType.payMap.get(payType));
			}
		}
        fulFee += (null != fulfill ? fulfill.getAccount() : 0.0) - leftFee;
		deliveryInfo.setAccount(fulFee);				
		
		// 预付的支付方式, 如果支付类型为空就认为是POS刷卡
		// 例如: "CASH,CST" 20090820 支付只要1种收款方式
		String payType = payBuf.toString().split(",")[0];
		if(payType == null || payType.length() < 1) {
		    payType = PrepayType.payMap.get(PrepayType.POS);
		}
		deliveryInfo.setPayType(payType);
		Date deliveryDate = fulfill.getDeliveryDate();
		deliveryInfo.setDeliveryDate(deliveryDate);
        deliveryInfo.setDsBeginTime(DateUtil.stringToDatetime(DateUtil.dateToString(deliveryDate)
				+ " " + fulfill.getDeliveryTime()));
        deliveryInfo.setDsEndTime(DateUtil.stringToDatetime(DateUtil.dateToString(deliveryDate)
				+ " " + fulfill.getDeliveryTimeEnd()));
		deliveryInfo.setDeliveryType(fulfill.getDeliveryType());		
		deliveryInfo.setMemberName(memberDTO.getName());
        deliveryInfo.setVipType(null == memberDTO.getViplevel() ? "1" : memberDTO.getViplevel());
		deliveryInfo.setRemark(fulfill.getFulfillNote());

		int taskType = fulfill.getFulfillTaskType();
		try {
			if (taskType == FulfillTaskType.DELIVER_PAY_SAME
                || taskType == FulfillTaskType.DELIVER_ONLY || 
                taskType == FulfillTaskType.PAY_SAME) {
                fulfillCD = deliveryServic.createDeliveryBill(taskType, deliveryInfo, null);
				fulfill.setFulfillCheck(true); // 是否已经生成过配送单
				fulfill.setFulfillmentCD(fulfillCD);
				order.setFulfillmentCD(fulfillCD);
				return true;
			}

			Gathering gathering = new Gathering();
			gathering.setUnitCD(fulfill.getUnitCD());
			deliveryDate = fulfill.getFulfillPayDate();
			gathering.setGatheringDate(deliveryDate);
            gathering.setGBeginTime(DateUtil.stringToDatetime(DateUtil.dateToString(deliveryDate)
					+ " " + fulfill.getFulfillPayTime()));
            gathering.setGEndTime(DateUtil.stringToDatetime(DateUtil.dateToString(deliveryDate)
					+ " " + fulfill.getFulfillPayTimeEnd()));
			gathering.setAddress(fulfill.getFulfillPayAddress());
			gathering.setLinkman(fulfill.getFulfillPayLinkman());
			gathering.setLinkmanPhone(fulfill.getFulfillPayPhone());

			if (taskType == FulfillTaskType.PAY_DIF) {
				deliveryInfo.setDeliveryDate(gathering.getGatheringDate());
				deliveryInfo.setDsBeginTime(gathering.getGBeginTime());
				deliveryInfo.setDsEndTime(gathering.getGEndTime());
				deliveryInfo.setDeliveryAddress(gathering.getAddress());
				deliveryInfo.setAddresseeName(gathering.getLinkman());
				deliveryInfo.setAddresseePhone(gathering.getLinkmanPhone());
			}

            fulfillCD = deliveryServic.createDeliveryBill(taskType, deliveryInfo, gathering);
			order.setFulfillmentCD(fulfillCD);
			fulfill.setFulfillCheck(true); // 是否已经生成过配送单
			fulfill.setFulfillmentCD(fulfillCD);

		} catch (Exception e) {
			log.error(e.getMessage(),e);
			order.setFulfillmentCD(null);
			fulfill.setFulfillCheck(false);
			fulfill.setFulfillmentCD(null);
			return false;
		}

		return true;
	}

	/**
	 * 供修改订单日志用
	 * 
	 * @param str
	 * @return
	 */
	private String getStr(String str) {
        return null != str && 0 < str.length() ? str : "无";
	}

	/**
	 * 生成订单编号
	 * 
	 * @return
	 */
	/*
     * public String GenerateOrderCD(){ //7位流水号 long tmp = orderService.getOrderSeq(); String
     * sevenNum = ""+tmp; while(sevenNum.length()<7){ sevenNum="0"+sevenNum; } //日期 SimpleDateFormat
     * formatter = new SimpleDateFormat("yyyyMMdd"); String date = formatter.format(new Date());
     * //最后一位字符 String lastChar = "H"; // yyyyMMddxxxxxxxC 年+月+日+7位数字+1位代码字符 例:200512310000001H
     * return date+sevenNum+lastChar; }
	 */
	
	/**
	 * add update haibo.li 2008-12-08 重新分配撤单原因
     * 
	 * @author chenkeming Feb 16, 2009 1:06:17 PM
	 * @param guestCancelMsgId
	 * @return
	 */
	private String getGuestCancelMessage(int guestCancelMsgId) {
		String guestCancelMessage = "";
		switch (guestCancelMsgId) {
		case 1:
			guestCancelMessage = "againNewOrder_guestReason";
			break;
		case 2:
			guestCancelMessage = "againNewOrder_hotelReason";
			break;
		case 3:
			guestCancelMessage = "againNewOrder_mangoReason";
			break;
		case 4:
			guestCancelMessage = "cancelOrder_guestReason";
			break;
		case 5:
			guestCancelMessage = "cancelOrder_hotelReason";
			break;
		case 6:
			guestCancelMessage = "cancelOrder_mangoReason";
			break;
		default:
			guestCancelMessage = "";
			break;
		}

		return guestCancelMessage;
	}
	
	/**
	 * 撤单时生成撤单原因日志字符串
     * 
	 * @author chenkeming Feb 16, 2009 1:06:43 PM
	 * @param order
	 * @param cancelReason
	 * @param cancelMessage
	 * @param guestCancelMessage
	 * @param roleUser
	 */
    public void getCancelLog(OrOrder order, int cancelReason, String cancelMessage,
        String guestCancelMessage, UserWrapper roleUser) {
		String strModify = null;
        Integer guestCancelMsg = Integer.valueOf(0);
		if (null != guestCancelMessage && !"".equals(guestCancelMessage)) {
			guestCancelMsg = Integer.parseInt(guestCancelMessage);
		}

		// 更新操作日志
        if ((1 == cancelReason && 6 == guestCancelMsg)
            || (2 == cancelReason && 6 == guestCancelMsg)
            || (3 == cancelReason && 2 == guestCancelMsg)
            || (4 == cancelReason && 12 == guestCancelMsg)
            || (5 == cancelReason && 10 == guestCancelMsg)
            || (6 == cancelReason && 4 == guestCancelMsg)
            || (4 == cancelReason && 14 == guestCancelMsg)) {//4，客人原因，14短信取消 add by luoguangming 短信取消订单
            if ((null != guestCancelMessage && !guestCancelMessage.equals(""))
                && (null != cancelMessage && !cancelMessage.equals("")))
			strModify = "撤销订单:<font color='red'>"
					+ order.getOrderCD()
					+ "</font>原因:"
                    + getStr(resourceManager.getDescription(ResType.CANCELREASON, cancelReason))
					+ " ";
			if(guestCancelMsg != 14){//短信取消这段不要
				strModify += getStr(resourceManager.getDescription(getGuestCancelMessage(cancelReason),
                        guestCancelMessage)) + " ";
			}
			strModify +=  cancelMessage + "<br>";
        } else if (7 == cancelReason) {
            strModify = "撤销订单:<font color='red'>" + order.getOrderCD() + "</font>原因:"
                + getStr(resourceManager.getDescription(ResType.CANCELREASON, cancelReason));
        } else if (14 == cancelReason && 88 == guestCancelMsg) {
			//hotel2.5 直联系统下单失败
            strModify = "撤销订单:<font color='red'>" + order.getOrderCD() + "</font>原因:"
				+ getStr(cancelMessage) + "<br>";
			
		} else {
            strModify = "撤销订单:<font color='red'>" + order.getOrderCD() + "</font>原因:"
                + getStr(resourceManager.getDescription(ResType.CANCELREASON, cancelReason))
                + "<br>";
		}
		
		OrHandleLog handleLog = new OrHandleLog();
		handleLog.setModifierName(roleUser.getName());
		handleLog.setModifierRole(roleUser.getLoginName());
		handleLog.setBeforeState(order.getOrderState());
		handleLog.setAfterState(OrderState.CANCEL);
		handleLog.setContent(strModify);
		handleLog.setModifiedTime(new Date());
		handleLog.setHisNo(order.getHisNo());
		handleLog.setOrder(order);
		order.getLogList().add(handleLog);
		order.setOrderState(OrderState.CANCEL);		
	}
    
    /**
     * 记录直联提交异常订单的日志
     * @param order
     * @param exceptionType
     * @param roleUser
     */
    public void handleExceptionLog(OrOrder order,int exceptionType,String message,UserWrapper roleUser) {
    	StringBuffer reasonStr = new StringBuffer();
    	if(1==exceptionType) {
    		reasonStr.append("网站直联订单:<font color='red'>");
    		reasonStr.append(order.getOrderCD());
    		reasonStr.append("</font>提交失败，需要人工跟进。");
    		reasonStr.append("原因：<font color='red'>");
    		reasonStr.append(message);
    		reasonStr.append("</font><br>");
    	}else if(2==exceptionType) {
    		reasonStr.append("网站直联订单:<font color='red'>");
    		reasonStr.append(order.getOrderCD());
    		reasonStr.append("</font>提交失败，需要人工跟进。");
    		reasonStr.append("原因：<font color='red'>");
    		reasonStr.append("直联合作方系统原因，具体：");
    		reasonStr.append(message);
    		reasonStr.append("</font><br>");
    	}else if(3==exceptionType) {
    		reasonStr.append("网站直联订单:<font color='red'>");
    		reasonStr.append(order.getOrderCD());
    		reasonStr.append("</font>提交成功，但需要人工跟进。");
    		reasonStr.append("原因：<font color='red'>");
    		reasonStr.append(message);
    		reasonStr.append("</font><br>");
    	}else if(4==exceptionType) {
    		reasonStr.append("网站中旅订单:<font color='red'>");
    		reasonStr.append(order.getOrderCD());
    		reasonStr.append("</font>提交失败，客人未支付，需要人工跟进。");
    		reasonStr.append("原因：<font color='red'>");
    		reasonStr.append(message);
    		reasonStr.append("</font><br>");
    	}else if(5==exceptionType) {
    		reasonStr.append("网站中旅订单:<font color='red'>");
    		reasonStr.append(order.getOrderCD());
    		reasonStr.append("</font>提交失败，客人未支付，需要人工跟进。");
    		reasonStr.append("原因：<font color='red'>");
    		reasonStr.append(message);
    		reasonStr.append("</font><br>");
    	}
    	OrHandleLog handleLog = new OrHandleLog();
    	handleLog.setModifierName(roleUser.getName());
    	handleLog.setModifierRole(roleUser.getLoginName());
    	handleLog.setBeforeState(order.getOrderState());
    	handleLog.setAfterState(OrderState.NOT_SUBMIT);
    	handleLog.setContent(reasonStr.toString());
    	handleLog.setModifiedTime(new Date());
    	handleLog.setHisNo(order.getHisNo());
    	handleLog.setOrder(order);
    	order.getLogList().add(handleLog);
    	order.setOrderState(OrderState.NOT_SUBMIT);
    	order.setIsStayInMid(false);
    	//本步骤需要删除OrderItem对象，需要创建一个空的List对象 add by zengzhouwu 2010-03-16
    	order.getOrderItems().clear(); 	
    }
    //VIP级别英文单词转换为对应的数字
    public int translateToNumber(String vipLevel){
    	int level = 0;
    	if(null != vipLevel){
    		if("Four".equals(vipLevel)){
    			level = 4;
    		}else if("Three".equals(vipLevel)){
    			level = 3;
    		}else if("Two".equals(vipLevel)){
    			level = 2;
    		}else if("One".equals(vipLevel)){
    			level = 1;
    		}
    	}
		return level;
    }
	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}


	public PointsDelegate getPointsDelegate() {
		return pointsDelegate;
	}

	public void setPointsDelegate(PointsDelegate pointsDelegate) {
		this.pointsDelegate = pointsDelegate;
	}

	public DSBillService getDeliveryServic() {
		return deliveryServic;
	}

	public void setDeliveryServic(DSBillService deliveryServic) {
		this.deliveryServic = deliveryServic;
	}

	public AliasManager getAliasManager() {
		return aliasManager;
	}

	public void setAliasManager(AliasManager aliasManager) {
		this.aliasManager = aliasManager;
	}
	
	

}
