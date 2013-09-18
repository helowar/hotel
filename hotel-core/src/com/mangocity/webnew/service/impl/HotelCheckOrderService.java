package com.mangocity.webnew.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import com.mangocity.hotel.base.dao.HtlRoomDao;
import com.mangocity.hotel.base.dao.ISaleDao;
import com.mangocity.hotel.base.persistence.HtlQuotaNew;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.base.service.assistant.AssureInforAssistant;
import com.mangocity.hotel.base.util.CreditCardWrapper;
import com.mangocity.hotel.ext.member.dto.MbrInfoDTO;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.ext.member.service.MemberBaseInfoDelegate;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.proxy.member.service.MemberInterfaceService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.log.MyLog;
import com.mangocity.webnew.persistence.HotelOrderFromBean;
import com.mangocity.webnew.service.IHotelCheckOrderService;

/**
 * 订单审核service实现，包括一些对会员信息获取的方法封装
 * 
 * @author chenjiajie
 * 
 */
public class HotelCheckOrderService implements IHotelCheckOrderService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8932665640455891360L;

	private static final MyLog log = MyLog.getLogger(HotelCheckOrderService.class);

	/**
	 * 可以从resourceDesc.xm中取值
	 */
	private ResourceManager resourceManager;

	/**
	 * 会员接口
	 */
	private MemberInterfaceService memberInterfaceService;
	
	private MemberBaseInfoDelegate memberBaseInfoDelegate;
	
	/**
	 * HotelManageWeb
	 */
	private HotelManageWeb hotelManageWeb;

    private HtlRoomDao htlRoomDao;
    private ISaleDao saleDao;

	/**
	 * 判断是继续预定还是已经登陆 如果是继续预订，则要搜索会员或者注册新会员 
	 * 
	 * @author chenjiajie
	 * @param hotelOrderFromBean 界面传递的订单Bean
	 * @param cookies request传递的cookies
	 * @param agentid 代理商id，可以为null
	 * @return
	 * <ol>
	 * <li>returnNewMemberCd=IHotelCheckOrderService.EXISTENT_MEMBER,会员已经存在</li>
	 * <li>returnNewMemberCd=新的会员CD,表明要注册会员,并注册了会员</li>
	 * </ol>
	 */
	public String searchRegisterMember(HotelOrderFromBean hotelOrderFromBean,
			Cookie[] cookies, String agentid) {
		String returnNewMemberCd = "";

		// 在会员系统中搜索订单联系人手机号或邮箱信息
		String mobile = hotelOrderFromBean.getMobile();
		String email = hotelOrderFromBean.getEmail();
		MemberDTO amember = null;
		List<MemberDTO> memberList = null;
		try {
			if (null != mobile) {
				amember = memberInterfaceService.getMemberByMobile(mobile);
			}
			if (null != email) {
				memberList = memberInterfaceService.getMemberByEmail(email);
			}
		} catch (Exception e) {
			log.error("getMemberByMobile or getMemberByEmail error " + e);
		}
		boolean isIn = false;
		//判断订单信息中手机或email是否已经存在会员
		if (null != memberList && 0 < memberList.size()) {
			if (null != amember) {
				for (int k = 0; k < memberList.size(); k++) {
					MemberDTO a = (MemberDTO) memberList.get(k);
					if (a.getMembercd().equals(amember.getMembercd())) {
						isIn = true;
						break;
					}
				}
				if (false == isIn) {
					memberList.add(amember);
				}
			}
		} else {
			memberList = new ArrayList<MemberDTO>();
			if (null != amember) {
				memberList.add(amember);
			}
		}
		if (0 == memberList.size()) {// 如果不存在重复的,则注册新的会员
			MbrInfoDTO mbrInfoDTO = new MbrInfoDTO();
			if (null != hotelOrderFromBean.getEmail()) {
				mbrInfoDTO.setEmailAddr(hotelOrderFromBean.getEmail());
			}
			if (null != hotelOrderFromBean.getMobile()) {
				mbrInfoDTO.setMobileNo(hotelOrderFromBean.getMobile());
			}
			if (null != hotelOrderFromBean.getCustomerFax()) {
				mbrInfoDTO.setFax(hotelOrderFromBean.getCustomerFax());
			}
			if (null != hotelOrderFromBean.getTelephone()) {
				String tempPhone = hotelOrderFromBean.getTelephone();
				mbrInfoDTO.setPhoneNo(tempPhone);
			}

			String englishName = hotelOrderFromBean.getLinkMan();
			if (null != englishName && !englishName.equals("")) {
				int b = englishName.indexOf("/");
				if (0 < b) {
					mbrInfoDTO.setFirstName(englishName.substring(b + 1,
							englishName.length())
							+ "/" + englishName.substring(0, b));
				} else {
					mbrInfoDTO.setName(hotelOrderFromBean.getLinkMan());
				}
			}

			String promotionsid = "";

			if (null != cookies) {
				for (int i = 0; i < cookies.length; i++) {
					Cookie sCookie = cookies[i];
					String svalue = sCookie.getValue();
					String sname = sCookie.getName();
					if (sname.equals("proid")) {
						promotionsid = svalue;
					}
				}
			}
		//	mbrInfoDTO.setMagazinecard(agentid);// 记录网络代理商id
			mbrInfoDTO.setMsn(promotionsid); // 记录活动号
			// end
			try {
				returnNewMemberCd = memberBaseInfoDelegate.registerNewMember(mbrInfoDTO).getDefaultMbrshipCd();
			} catch (Exception e) {
				log.error("register new member error: " + e);
			}
		} else {
			returnNewMemberCd = EXISTENT_MEMBER;
		}
		return returnNewMemberCd;
	}

	/**
	 * 计算订单的担保金额，并封装数据提供页面显示
	 * @param assureDetailString
	 * @param hotelOrderFromBean
	 * @param order
	 * @param reservation
	 */
//	public void calculateSuretyAmount(String assureDetailString,HotelOrderFromBean hotelOrderFromBean,
//			OrOrder order,OrReservation reservation) throws Exception {
//		if (StringUtil.isValidStr(assureDetailString)) {
//			List<AssureInforAssistant> assureDetailList = hotelManageWeb.convertToAssureInforAssistant(assureDetailString);
//			//需要使用原有的HotelOrderFromBean传递参数
//			com.mangocity.hotelweb.persistence.HotelOrderFromBean oldHotelOrderFromBean = new com.mangocity.hotelweb.persistence.HotelOrderFromBean();
//			MyBeanUtil.copyProperties(oldHotelOrderFromBean, hotelOrderFromBean);			
//
//            //原币种的1间房的担保金额
//            double orignalSuretyPrice = Math.ceil(hotelManageWeb.calculateSuretyAmount(assureDetailList,oldHotelOrderFromBean));
//            
//            //如果担保金额>0
//            if (0 != Double.compare(orignalSuretyPrice, 0.0d)) {
//                hotelOrderFromBean.setNeedAssure(true);                
//                //房间数量
//                int roomQuantity = Integer.parseInt(hotelOrderFromBean.getRoomQuantity());
//
//                hotelOrderFromBean.setOrignalSuretyPrice(orignalSuretyPrice * roomQuantity);
//                //转换成RMB的担保总金额（先逢一进十，再乘以房间数）
//                double orignalSuretyRMBPrice = Math.ceil(orignalSuretyPrice * order.getRateId()) * roomQuantity;
//                hotelOrderFromBean.setSuretyPriceRMB(orignalSuretyRMBPrice);
//                
//                reservation.setReservSuretyPrice(orignalSuretyRMBPrice * roomQuantity);
//            }
//
//        }
//	}
	public double calculateSuretyAmount(
			List<AssureInforAssistant> assureDetailLit,
			HotelOrderFromBean hotelOrderFromBean) {
		double assureAmout = 0.0;
        AssureInforAssistant assureInforAssistantFrist = assureDetailLit.get(0);
        //担保规则 check in day
        if ("1".equals(assureInforAssistantFrist.getAssureRule())) {
        	//是无条件担保
            if (assureInforAssistantFrist.isUnconditionAssure()) {
                assureAmout = assureInforAssistantFrist.getUnconditionAssureAmount();
            } 
            //是超时担保 
            else if (assureInforAssistantFrist.isOverTimeAssure()
                && 0 == DateUtil.getDay(hotelOrderFromBean.getCheckinDate(),
                    assureInforAssistantFrist.getAssureDate())
                && satisfiyCodition("1", assureInforAssistantFrist.getOverTimeStr(),
                    hotelOrderFromBean, assureInforAssistantFrist.getAssureDate())) {
                assureAmout = assureInforAssistantFrist.getOverTimeAssureAmount();
            } 
            //是超房担保 
            else if (assureInforAssistantFrist.isOverRoomsAssure()
                && 0 == DateUtil.getDay(hotelOrderFromBean.getCheckinDate(),
                    assureInforAssistantFrist.getAssureDate())
                && satisfiyCodition("2", assureInforAssistantFrist.getOverRoomsNum(),
                    hotelOrderFromBean, assureInforAssistantFrist.getAssureDate())) {
                assureAmout = assureInforAssistantFrist.getOverRoomsAssureAmount();
            }
            //是超间夜担保
            else if(assureInforAssistantFrist.isOverNightsAssure()
		            &&DateUtil.getDay(hotelOrderFromBean.getCheckinDate(),assureInforAssistantFrist.getAssureDate())==0
					&&satisfiyCodition("3",assureInforAssistantFrist.getOverNightsNum(),hotelOrderFromBean,assureInforAssistantFrist.getAssureDate())){
            	assureAmout = assureInforAssistantFrist.getOverNightsAssureAmount();
            }
        } 
        //担保规则 全额
        else if ("2".equals(assureInforAssistantFrist.getAssureRule())) {
            for (Iterator<AssureInforAssistant> assureDetailIterator = assureDetailLit.iterator(); assureDetailIterator.hasNext();) {
                AssureInforAssistant assureInforAssistant = assureDetailIterator.next();
                //担保类型 2:首日
                if ("2".equals(assureInforAssistant.getAssureType())
                    && 0 == Double.compare(0.0, assureAmout)) {
                	//是无条件担保
                    if (assureInforAssistant.isUnconditionAssure()) {
                        assureAmout = assureInforAssistant.getFristDayAssureAmount();
                    } 
                    //是超时担保 
                    else if (assureInforAssistant.isOverTimeAssure()
                        && 0 == DateUtil.getDay(hotelOrderFromBean.getCheckinDate(),
                            assureInforAssistant.getAssureDate())
                        && satisfiyCodition("1", assureInforAssistant.getOverTimeStr(),
                            hotelOrderFromBean, assureInforAssistant.getAssureDate())) {
                        assureAmout = assureInforAssistant.getFristDayAssureAmount();
                    } 
                    //是超房担保 
                    else if (assureInforAssistant.isOverRoomsAssure()
                        && satisfiyCodition("2", assureInforAssistant.getOverRoomsNum(),
                            hotelOrderFromBean, assureInforAssistant.getAssureDate())) {
                        assureAmout = assureInforAssistant.getFristDayAssureAmount();
                    }
                    //是超间夜担保
                    else if(assureInforAssistant.isOverNightsAssure()
							&&satisfiyCodition("3",assureInforAssistant.getOverNightsNum(),hotelOrderFromBean,assureInforAssistant.getAssureDate())){
						assureAmout = assureInforAssistant.getFristDayAssureAmount();
                    }
                } 
                //担保类型 4:全额
                else if ("4".equals(assureInforAssistant.getAssureType())) {
                	//是无条件担保
                    if (assureInforAssistant.isUnconditionAssure()) {
                        assureAmout = hotelOrderFromBean.getPriceNum();
                    } 
                    //是超时担保 
                    else if (assureInforAssistant.isOverTimeAssure()
                        && 0 == DateUtil.getDay(hotelOrderFromBean.getCheckinDate(),
                            assureInforAssistant.getAssureDate())
                        && satisfiyCodition("1", assureInforAssistant.getOverTimeStr(),
                            hotelOrderFromBean, assureInforAssistant.getAssureDate())) {
                        assureAmout = hotelOrderFromBean.getPriceNum();
                    } 
                    //是超房担保 
                    else if (assureInforAssistant.isOverRoomsAssure()
                        && satisfiyCodition("2", assureInforAssistant.getOverRoomsNum(),
                            hotelOrderFromBean, assureInforAssistant.getAssureDate())) {
                        assureAmout = hotelOrderFromBean.getPriceNum();
                    }
                    //是超间夜担保
                    else if(assureInforAssistant.isOverNightsAssure()
							&&satisfiyCodition("3",assureInforAssistant.getOverNightsNum(),hotelOrderFromBean,assureInforAssistant.getAssureDate())){
						assureAmout = hotelOrderFromBean.getPriceNum();
                    }
                }
            }

        } 
        //担保规则 累加
        else if ("3".equals(assureInforAssistantFrist.getAssureRule())) {
            for (Iterator<AssureInforAssistant> assureDetailIterator = assureDetailLit.iterator(); assureDetailIterator.hasNext();) {
                AssureInforAssistant assureInforAssistant = assureDetailIterator .next();
                //担保类型 2:首日
                if ("2".equals(assureInforAssistant.getAssureType())
                    && 0 == Double.compare(0.0, assureAmout)) {
                    if (assureInforAssistant.isUnconditionAssure()) {
                        assureAmout = assureInforAssistant.getFristDayAssureAmount();
                    } else if (assureInforAssistant.isOverTimeAssure()
                        && 0 == DateUtil.getDay(hotelOrderFromBean.getCheckinDate(),
                            assureInforAssistant.getAssureDate())
                        && satisfiyCodition("1", assureInforAssistant.getOverTimeStr(),
                            hotelOrderFromBean, assureInforAssistant.getAssureDate())) {
                        assureAmout = assureInforAssistant.getFristDayAssureAmount();
                    } else if (assureInforAssistant.isOverRoomsAssure()
                        && satisfiyCodition("2", assureInforAssistant.getOverRoomsNum(),
                            hotelOrderFromBean, assureInforAssistant.getAssureDate())) {
                        assureAmout = assureInforAssistant.getFristDayAssureAmount();
                    }else if(assureInforAssistant.isOverNightsAssure()
							&&satisfiyCodition("3",assureInforAssistant.getOverNightsNum(),hotelOrderFromBean,assureInforAssistant.getAssureDate())){
						assureAmout = assureInforAssistant.getFristDayAssureAmount();
                    }
                } 
                //担保类型 4:全额
                else if ("4".equals(assureInforAssistant.getAssureType())) {
                    if (assureInforAssistant.isUnconditionAssure()) {
                        assureAmout += assureInforAssistant.getUnconditionAssureAmount();
                    } else if (assureInforAssistant.isOverTimeAssure()
                        && 0 == DateUtil.getDay(hotelOrderFromBean.getCheckinDate(),
                            assureInforAssistant.getAssureDate())
                        && satisfiyCodition("1", assureInforAssistant.getOverTimeStr(),
                            hotelOrderFromBean, assureInforAssistant.getAssureDate())) {
                        assureAmout += assureInforAssistant.getOverTimeAssureAmount();
                    } else if (assureInforAssistant.isOverRoomsAssure()
                        && satisfiyCodition("2", assureInforAssistant.getOverRoomsNum(),
                            hotelOrderFromBean, assureInforAssistant.getAssureDate())) {
                        assureAmout += assureInforAssistant.getOverRoomsAssureAmount();
                    }else if(assureInforAssistant.isOverNightsAssure()
							&&satisfiyCodition("3",assureInforAssistant.getOverNightsNum(),hotelOrderFromBean,assureInforAssistant.getAssureDate())){
						assureAmout += assureInforAssistant.getOverNightsAssureAmount();
                    }
                }
            }
        }
        return assureAmout;
	}
	
	private boolean satisfiyCodition(String conditionType, Object obj,
	        HotelOrderFromBean hotelOrderFromBean, Date assuredate) {
	        boolean ret = false;
	        if ("1".equals(conditionType)) {
	            long assureTime = StringUtil.getStrTolong((((String) obj).replace(":", "")));
	            long orderTime = StringUtil.getStrTolong(hotelOrderFromBean.getLatestArrivalTime()
	                .replace(":", ""));
	            Date orderLatestArriveDate=hotelOrderFromBean.getLatestArrivalDate();
	            if (orderLatestArriveDate!=null && DateUtil.getDay(hotelOrderFromBean.getCheckinDate(), orderLatestArriveDate) > 0) {
					ret = true;
				} else {
					if (orderTime > assureTime) {
						ret = true;
					} else {
						ret = false;
					}
				}
	        } else if ("2".equals(conditionType)) {
	            long assureRooms = StringUtil.getStrTolong((obj.toString()));
	            long orderRooms = StringUtil.getStrTolong(hotelOrderFromBean.getRoomQuantity());
	            if (orderRooms > assureRooms) {
	                ret = true;
	            } else {
	                ret = false;
	            }
	        }else if("3".equals(conditionType)){
				long assureNights = StringUtil.getStrTolong((obj.toString()));
				long orderNights = DateUtil.getDay(hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate())*StringUtil.getStrTolong(hotelOrderFromBean.getRoomQuantity());
				if(orderNights>assureNights){
					ret = true;
				}else{
					ret = false;
				}
			}
	        return ret;
	    }
	
	/**
	 * 根据输入的邮箱和手机号码，查询出会员CD List;
	 * @param hotelOrderFromBean
	 * @return
	 * add by shengwei.zuo 2009-11-15
	 */
	public List<MemberDTO>  getMemberLst(HotelOrderFromBean hotelOrderFromBean){
		
		// 在会员系统中搜索订单联系人手机号或邮箱信息
		String mobile = hotelOrderFromBean.getMobile();
		String email = hotelOrderFromBean.getEmail();
		MemberDTO amember = null;
		List<MemberDTO> memberList = null;
		try {
			if (StringUtil.isValidStr(mobile)) {
				amember = memberInterfaceService.getMemberByMobile(mobile);
			}
			if (StringUtil.isValidStr(email)) {
				memberList = memberInterfaceService.getMemberByEmail(email);
			}
		} catch (Exception e) {
			
			log.error("getMemberByMobile or getMemberByEmail error " + e);
			
		}
		
		boolean isIn = false;
		//判断订单信息中手机或email是否已经存在会员
		if (null != memberList && 0 < memberList.size()) {
			if (null != amember) {
				for (int k = 0; k < memberList.size(); k++) {
					MemberDTO a = (MemberDTO) memberList.get(k);
					if (a.getMembercd().equals(amember.getMembercd())) {
						isIn = true;
						break;
					}
				}
				if (false == isIn) {
					memberList.add(amember);
				}
			}
		} else {
			memberList = new ArrayList<MemberDTO>();
			if (null != amember) {
				memberList.add(amember);
			}
		}
		
		return memberList;
		
	}
	
	//如果不存在重复的,则注册新的会员
	public String registerNewMember(HotelOrderFromBean hotelOrderFromBean,Cookie[] cookies){
		
		String returnNewMemberCd="";
		
		// 如果不存在重复的,则注册新的会员
		MbrInfoDTO mbrInfoDTO = new MbrInfoDTO();
		if (null != hotelOrderFromBean.getEmail()) {
			mbrInfoDTO.setEmailAddr(hotelOrderFromBean.getEmail());
		}
		if (null != hotelOrderFromBean.getMobile()) {
			mbrInfoDTO.setMobileNo(hotelOrderFromBean.getMobile());
		}
		if (null != hotelOrderFromBean.getCustomerFax()) {
			mbrInfoDTO.setFax(hotelOrderFromBean.getCustomerFax());
		}
		if (null != hotelOrderFromBean.getTelephone()) {
			String tempPhone = hotelOrderFromBean.getTelephone();
			mbrInfoDTO.setPhoneNo(tempPhone);
		}

		String englishName = hotelOrderFromBean.getLinkMan();
		if (null != englishName && !englishName.equals("")) {
			int b = englishName.indexOf("/");
			if (0 < b) {
				mbrInfoDTO.setFirstName(englishName.substring(b + 1,
						englishName.length())
						+ "/" + englishName.substring(0, b));
			} else {
				mbrInfoDTO.setName(hotelOrderFromBean.getLinkMan());
			}
		}

		String promotionsid = "";

		if (null != cookies) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie sCookie = cookies[i];
				String svalue = sCookie.getValue();
				String sname = sCookie.getName();
				if (sname.equals("proid")) {
					promotionsid = svalue;
				}
			}
		}
		
		String projectCode = "";

		if (null != cookies) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie sCookie = cookies[i];
				String svalue = sCookie.getValue();
				String sname = sCookie.getName();
				if (sname.equals("projectcode")) {
					projectCode = svalue;
				}
			}
		}
		String agentId = "";

		if (null != cookies) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie sCookie = cookies[i];
				String svalue = sCookie.getValue();
				String sname = sCookie.getName();
				if (sname.equals("agentid")) {
					agentId = svalue;
				}
			}
		}
		//mbrInfoDTO.setMagazinecard(agentId);// 记录网络代理商id
		//mbrInfoDTO.setProjectcode(projectCode);
		mbrInfoDTO.setMsn(promotionsid); // 记录活动号
		// end

		try {
			returnNewMemberCd = memberBaseInfoDelegate.registerNewMember(mbrInfoDTO).getDefaultMbrshipCd();
		} catch (Exception e) {
			log.error("register new member error: " + e);
		}
	
		return returnNewMemberCd;
		
	}

	public boolean isRoomFull(Map params) {
		String roomTypeIdStr=(String)params.get("roomTypeId");
		if(null==roomTypeIdStr || (null!=roomTypeIdStr&&"".equals(roomTypeIdStr))||"null".equals(roomTypeIdStr)){
			return true;
		}
        Long roomTypeId = Long.parseLong(roomTypeIdStr);

		String checkInDate = String.valueOf(params.get("checkinDate"));
		String checkOutDate = String.valueOf(params.get("checkoutDate"));
		if (("null".equals(checkInDate) || "".equals(checkInDate)) || ("null".equals(checkOutDate) || "".equals(checkOutDate))) {
			return true;
		}
		Date checkinDate = DateUtil.toDateByFormat(checkInDate, "yyyy-MM-dd");
		Date checkoutDate = DateUtil.toDateByFormat(checkOutDate, "yyyy-MM-dd");
		String roomSate = (String) params.get("bedType") + ":4";
		List<HtlRoom> htlRoomList = this.htlRoomDao.getHtlRoomByRoomTypeId(roomTypeId, checkinDate, checkoutDate);
		if (htlRoomList != null && htlRoomList.size() > 0) {
			for (HtlRoom htlroom : htlRoomList) {
				if (htlroom.getRoomState().indexOf(roomSate) != -1) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 判断满房，判断预订日期内，是否存在不可超和满房的床型型.add by ting.li
	 * @param hotelId
	 * @param roomTypeId
	 * @param checkinDate
	 * @param checkOutDate
	 * @return 满房的床型Id字符串
	 */
	public String checkRoomFull(HotelOrderFromBean hotelOrderFromBean) {
		boolean fagNotexceed = false;
		StringBuilder fullBedType = new StringBuilder();
		List<HtlRoom> htlRooms = htlRoomDao.getHtlRooms(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getRoomTypeId(), hotelOrderFromBean
				.getCheckinDate(), hotelOrderFromBean.getCheckoutDate());
		for (HtlRoom htlroom : htlRooms) {
			if (htlroom.getRoomState().indexOf(":3") >= 0 || htlroom.getRoomState().indexOf(":4") >= 0) {
				fagNotexceed = true;
				break;
			}
		}
		if (fagNotexceed) {
			String bedTypeStr = hotelOrderFromBean.getBedTypeStr();
			String[] bedType = bedTypeStr.split(",");
			HtlRoomtype roomType=hotelManageWeb.queryHtlRoomTypeForWeb(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getRoomTypeId());
			List<HtlQuotaNew> htlQuotas = saleDao.queryQuotaByRoomTypeId(hotelOrderFromBean.getHotelId(), hotelOrderFromBean.getRoomTypeId(),
					hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate());

			Map<String, HtlQuotaNew> htlQuotaMap = getHtlQuotaNewMap(htlQuotas);

			for (int i = 0; i < bedType.length; i++) {			
				if (checkIsFullBed(htlRooms, bedType[i], htlQuotaMap,roomType)) {
					if (!hasFullBedType(fullBedType.toString(), bedType[i])) {
						fullBedType.append(bedType[i]);
					}
				}				
			}
			
			htlQuotaMap=null;
			
		}
		htlRooms=null;
		return fullBedType.toString();
	}
	
	/**
	 * 判断是否是床型共享配额，并且是否有配额.共享配额只录在双床上，如果存在共享配额，就一定有双床。业务是这样的。
	 * @param roomType
	 * @param htlroom
	 * @param htlQuotaMap
	 * @return
	 */
	private boolean checkIsQuotaBedShare(HtlRoomtype roomType,HtlRoom htlRoom,Map<String,HtlQuotaNew> htlQuotaMap){
		
		if(roomType.getQuotaBedShare()==1L){		
				if(isQuotaFull(htlRoom,htlQuotaMap,"2")){
					return false;
				}				
			}
		
		return true;
	}
	
	/**
	 * add by ting.li 判断传进来的床型是否是不可超或满房
	 * @param htlRooms
	 * @param bedType
	 * @param htlQuotaMap
	 * @return
	 */
	private boolean checkIsFullBed(List<HtlRoom> htlRooms,String bedType,Map<String,HtlQuotaNew> htlQuotaMap,HtlRoomtype roomType){
		boolean fagFullBed=false;
		for(HtlRoom htlroom:htlRooms){
			
			if(htlroom.getRoomState().indexOf(bedType+":3")>=0||htlroom.getRoomState().indexOf(bedType+":4")>=0){
				//配额是否为0
				if(isQuotaFull(htlroom,htlQuotaMap,bedType)){
					//是共享配额
					if (roomType.getQuotaBedShare() == 1L) {
						if (!checkIsQuotaBedShare(roomType, htlroom, htlQuotaMap)) {
							fagFullBed = true;
							break;
						}
					}
					//不是共享配额
					else{
						fagFullBed=true;
						break;
					}
				}
			}
		}
		return fagFullBed;
	}
	
	/**
	 * 判断该床型在该房型下是否为满房。
	 * add by ting.li
	 * @param htlroom 房型
	 * @param htlQuotaMap 查出的配额
	 * @param bedType 床型
	 * @return 
	 */
	private boolean isQuotaFull(HtlRoom htlroom,Map<String,HtlQuotaNew> htlQuotaMap,String bedType){
		boolean fagQuotaFull = false;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		StringBuilder key = new StringBuilder();
		key.append(dateFormat.format(htlroom.getAbleSaleDate()));
		key.append(htlroom.getHotelId());
		key.append(htlroom.getRoomTypeId());
		key.append(bedType);

		HtlQuotaNew htlQuota = htlQuotaMap.get(key.toString());
		
		int buyQuotaAbleNum = (htlQuota.getBuyQuotaAbleNum() == null) ? 0 : htlQuota.getBuyQuotaAbleNum().intValue();
		int casualQuotaAbleNum = (htlQuota.getCasualQuotaAbleNum() == null) ? 0 : htlQuota.getCasualQuotaAbleNum().intValue();
		int commonQuotaAbleNum = (htlQuota.getCommonQuotaAbleNum() == null) ? 0 : htlQuota.getCommonQuotaAbleNum().intValue();
		int ableQuotaSum = buyQuotaAbleNum + casualQuotaAbleNum + commonQuotaAbleNum;
		
		if (ableQuotaSum == 0) {
			fagQuotaFull = true;
		}
		
		key = null;
		dateFormat = null;
		
		return fagQuotaFull;
	}
	
	/**
	 * add by ting.li
	 * @param htlQuotas，配额对象
	 * @return 配额的Map
	 */
	private Map<String,HtlQuotaNew> getHtlQuotaNewMap(List<HtlQuotaNew> htlQuotas){
		 SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
		 Map<String,HtlQuotaNew> htlQuotaMap=new HashMap<String,HtlQuotaNew>();
		 StringBuilder key;
		 for(HtlQuotaNew htlQuota : htlQuotas){
			 key=new StringBuilder();
			 key.append(dateFormat.format(htlQuota.getAbleSaleDate()));
			 key.append(htlQuota.getHotelId());
			 key.append(htlQuota.getRoomtypeId());
			 key.append(htlQuota.getBedId());
			 htlQuotaMap.put(key.toString(), htlQuota);
		 }
		 dateFormat=null;
		return htlQuotaMap;
	}
	
	private boolean hasFullBedType(String fullBedTypeStr,String bedType){
		if(fullBedTypeStr.indexOf(bedType)>=0){
			return true;
		}
		return false;
	}
	
	private boolean isAllFour(String text){
		String[] tempStrs=text.split("/");
		boolean fagAllFour=false;
		for(int i=0;i<tempStrs.length;i++){
			if(tempStrs[i].indexOf(":4")>=0){
				fagAllFour=true;
			}
			else{
				fagAllFour=false;
				break;
			}
		}
		return fagAllFour;
	}
	/** getter and setter **/

	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	public MemberInterfaceService getMemberInterfaceService() {
		return memberInterfaceService;
	}

	public void setMemberInterfaceService(
			MemberInterfaceService memberInterfaceService) {
		this.memberInterfaceService = memberInterfaceService;
	}

	public HotelManageWeb getHotelManageWeb() {
		return hotelManageWeb;
	}

    public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
        this.hotelManageWeb = hotelManageWeb;
    }

    public void setHtlRoomDao(HtlRoomDao htlRoomDao) {
        this.htlRoomDao = htlRoomDao;
    }

	public void setSaleDao(ISaleDao saleDao) {
		this.saleDao = saleDao;
	}

	public MemberBaseInfoDelegate getMemberBaseInfoDelegate() {
		return memberBaseInfoDelegate;
	}

	public void setMemberBaseInfoDelegate(
			MemberBaseInfoDelegate memberBaseInfoDelegate) {
		this.memberBaseInfoDelegate = memberBaseInfoDelegate;
	}

}
