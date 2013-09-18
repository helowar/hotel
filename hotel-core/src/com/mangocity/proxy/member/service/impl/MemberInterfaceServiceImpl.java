package com.mangocity.proxy.member.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.ext.member.dto.HisLinkPersonDTO;
import com.mangocity.hotel.ext.member.dto.MbrInfoDTO;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.ext.member.dto.PassengerInfoDTO;
import com.mangocity.hotel.ext.member.service.MemberBaseInfoDelegate;
import com.mangocity.hotel.ext.member.service.MemberHisLinkPersonDelegate;
import com.mangocity.hotel.ext.member.service.MemberPassengerDelegate;
import com.mangocity.hotel.order.persistence.OrFellowInfo;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.model.mbrship.Mbrship;
import com.mangocity.model.mbrship.MbrshipVO;
import com.mangocity.model.mbrsys.MbrTransactionLogVO;
import com.mangocity.model.person.PersonMainInfo;
import com.mangocity.proxy.member.service.MemberInterfaceService;
import com.mangocity.proxy.vo.MembershipVO;
import com.mangocity.services.mbrship.MbrshipServiceException;
import com.mangocity.util.StringUtil;
import com.mangocity.util.log.MyLog;
import com.mangocity.webnew.constant.FITAliasConstant;

/**
 * @author oliverkang
 */
public class MemberInterfaceServiceImpl implements MemberInterfaceService {
	private static final MyLog log = MyLog.getLogger(MemberInterfaceServiceImpl.class);

    /**
     * 历史入住人接口
     */
    private MemberPassengerDelegate memberPassengerDelegate;
    /**
     * 历史联系人接口
     */
    private MemberHisLinkPersonDelegate memberHisLinkPersonDelegate;
    /**
     * 会员基本信息接口
     */
	private MemberBaseInfoDelegate memberBaseInfoDelegate;
	
	
	 private HotelManage hotelManage;
	 
	/**
	 * 根据memebrcd获取会员基本信息
	 */
    public MemberDTO getMemberByCode(String memberCd) {
        log.debug("getMemberByCode() starts!");
        log.info("拿会员memberCD======================"+memberCd);
        if (StringUtils.isEmpty(memberCd)) {
            String msg = "\"code\" is empty!";
            throw new IllegalArgumentException(msg);
        } else {
        	memberCd = memberCd.trim();
        }
        return memberBaseInfoDelegate.getMemberByMemberCd(memberCd);
    }
    /**
     * 根据手机号获取会员基本信息
     */
    public MemberDTO getMemberByMobile(String mobile) {
        log.debug("getMemberByMobile() starts!");
        if (StringUtils.isEmpty(mobile)) {
            String msg = "\"mobile\" is empty!";
            throw new IllegalArgumentException(msg);
        } else {
            mobile = mobile.trim();
        }
        return memberBaseInfoDelegate.getMemberByMobile(mobile);
    }
    /**
     * 根据Email获取会员基本信息
     */
    public List getMemberByEmail(String email) {
        log.debug("getMemberByEmail() starts!");
        if (StringUtils.isEmpty(email)) {
            String msg = "\"email\" is empty!";
            throw new IllegalArgumentException(msg);
        } else {
            email = email.trim();
        }
        return memberBaseInfoDelegate.getMemberByEmail(email);
    }
    
    
    /**
     * 根据membercd获取会员常入住人和常联系人
     * 
     * @param membercd
     * @param fellow
     * @param linkman
     */
    public void getFellowAndLinkmanByMemberCd(MemberDTO member) {
   	 //判断是否为公共会籍，是则将常入住人，常联系人等信息设置为空
    	List<OrParam> orParamList =  hotelManage.queryCommonMemberCd();
    	for(int i =0;i<orParamList.size();i++){
    		if(member.getMembercd().equals(orParamList.get(i).getValue())){
    		return;
    		}
    	}
		try {
			member.setFellowList(memberPassengerDelegate
					.getMemberPassengerByMemberCd(member.getMembercd()));
		} catch (Exception e) {
			log.error("get member passenger error:", e);
		}
		try {
			member.setLinkmanList(memberHisLinkPersonDelegate
					.getHisLinkPersonByMemberCd(member.getMembercd()));
		} catch (Exception e) {
			log.error("get history link person error:", e);
		}
    }
    /**
     * 更新常联系人
     * @param memberDTO
     * @param order
     */
	private void updateLinkMan(MemberDTO memberDTO, OrOrder order){
		String linkman = order.getLinkMan();
		List<HisLinkPersonDTO> hisLinkPersonDTOList = memberHisLinkPersonDelegate.getHisLinkPersonByMemberCd(order.getMemberCd());
		HisLinkPersonDTO extMp = null;
		if(null != hisLinkPersonDTOList && hisLinkPersonDTOList.size()>0){
			for(int i=0;i<hisLinkPersonDTOList.size();i++){
				if(hisLinkPersonDTOList.get(i).getLinkPersonName().equals(linkman)){
					extMp = hisLinkPersonDTOList.get(i);
					break;
				}
			}
		}
		if (null != extMp) {
		    if (order.isNewOrder()) {
		        boolean bChg = false;
		        if (!StringUtil.StringEquals1(extMp.getLinkMobileNo(), order.getMobile())) {
		            bChg = true;
		            extMp.setLinkMobileNo(order.getMobile());
		        }
		        if (bChg) {
		        	try {
		        		extMp.setUpdateBy(order.getCreator());						
						memberHisLinkPersonDelegate.updateHisLinkPerson(extMp);
					} catch (Exception e) {
						log.error("update history link person error when is a new  order:", e);
					}
		        }
		    } else {
		        extMp.setAppellation(order.getTitle());
		        extMp.setLinkMobileNo(order.getMobile());
		        extMp.setLinkPhoneNo(order.getTelephone());
		        extMp.setLinkPersonFax(order.getCustomerFax());
		        extMp.setLinkPersonEmail(order.getEmail());
		        extMp.setUpdateBy(order.getCreator());
		        try {
					memberHisLinkPersonDelegate.updateHisLinkPerson(extMp);
				} catch (Exception e) {
					log.error("update history link person error when is an old order:", e);
				}
		    }
		} else {
			HisLinkPersonDTO newLinkPerson = new HisLinkPersonDTO();
			Long mbrId = memberBaseInfoDelegate.queryMbrIdByOldMbrshipCd(memberDTO.getMembercd());
			newLinkPerson.setMbrId(mbrId);//三期会员Id
			newLinkPerson.setLinkPersonName(order.getLinkMan());
			newLinkPerson.setAppellation(order.getTitle());
			newLinkPerson.setLinkMobileNo(order.getMobile());
			newLinkPerson.setLinkPhoneNo(order.getTelephone());
			newLinkPerson.setLinkPersonFax(order.getCustomerFax());
			newLinkPerson.setLinkPersonEmail(order.getEmail());
			newLinkPerson.setCreateBy(order.getCreator());
			newLinkPerson.setUpdateBy(order.getCreator());
			Boolean flag=false;
			try {
				flag = memberHisLinkPersonDelegate
						.addHisLinkPerson(newLinkPerson);
			} catch (Exception e) {
				log.error("add history link person error when HisLinkPersonDTO is null:", e);
			}
		    if (flag) {
		    	hisLinkPersonDTOList.add(newLinkPerson);
		    }
			memberDTO.setLinkmanList(hisLinkPersonDTOList);
		}
	}
	
    /**
     * 更新常入住人
     * @param memberDTO
     * @param order
     */
	@SuppressWarnings("unchecked")
	private void updateFellow(MemberDTO memberDTO, OrOrder order){
		if(memberDTO!=null){
		List fellowList = order.getFellowList();
		List<PassengerInfoDTO> passengerInfoDTOList  = memberPassengerDelegate.getMemberPassengerByMemberCd(memberDTO.getMembercd());
		for (int i = 0; i < fellowList.size(); i++) {
			PassengerInfoDTO extMp =null;
		    OrFellowInfo mp = (OrFellowInfo) fellowList.get(i);
		    if(null != passengerInfoDTOList && passengerInfoDTOList.size()>0){
		    	for(int j=0;j<passengerInfoDTOList.size();j++){
		    		if(passengerInfoDTOList.get(j).getChiName().equals(mp.getFellowName())){
		    			extMp = passengerInfoDTOList.get(j);
		    			break;
		    		}
		    	}
		    }
		    if (null == extMp)  {
		    	PassengerInfoDTO newPsg = new PassengerInfoDTO();
		        newPsg.setChiName(mp.getFellowName());
		        newPsg.setCountry(mp.getFellowNationality());
                newPsg.setCreateBy(order.getCreator());
                newPsg.setUpdateBy(order.getCreator());
		        Long mbrId = memberBaseInfoDelegate.queryMbrIdByOldMbrshipCd(memberDTO.getMembercd());
		        newPsg.setMbrId(mbrId);
		        Boolean returnMsg=false;
				try {
					returnMsg = memberPassengerDelegate
							.addMemberPassenger(newPsg);
				} catch (Exception e) {
					log.error("add member passenger error:", e);
				}
		        if (returnMsg) {
		        	passengerInfoDTOList.add(newPsg);
		        }
		    }
		}
		memberDTO.setFellowList(passengerInfoDTOList);
		}
	}
    
	/**
     * 根据订单的入住人列表和联系人更新会员的常入住人列表和常联系人
     * 
     * @param member	会员
     * @param order		订单
     * @param bFellow	是否更新常入住人
     * @param bLinkman	是否更新常联系人
     */
    public void updateMemberFellowAndLinkman(MemberDTO memberDTO, OrOrder order, boolean bFellow, boolean bLinkman) {
        try {
            // 更新常入住人
            if (bFellow) {
                updateFellow(memberDTO, order);
            }

            // 更新常联系人
            if (bLinkman) {
                updateLinkMan(memberDTO, order);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    
    /**
     * TMC-V2.0 根据membercd获取历史入住人 add by shengwei.zuo  2010-4-28
     * 
     * @param membercd
     * @param fellow
     * @param linkman
     */
    public void getFellowByMemberCd(String memberCd, Map fellow) {
    	List<PassengerInfoDTO> passengerInfoDTOList=null;
		try {
			passengerInfoDTOList = memberPassengerDelegate
					.getMemberPassengerByMemberCd(memberCd);
		} catch (Exception e) {
			log.error("get member passenger by member cd error:", e);
		}
		if(null !=passengerInfoDTOList){
	    	for(int i = 0; i < passengerInfoDTOList.size(); i++){
	    		PassengerInfoDTO pi = passengerInfoDTOList.get(i);
	    		fellow.put(pi.getChiName(), pi);
	    	}
		}
    }
	/**
	 * 根据当前登录会员或输入手机号、Email查询会员的所有会籍信息
	 * @param mobilePhone
	 * @param email
	 * @return
	 */
    public  List<MembershipVO> getMemberShipList(long mbrId,String mobilePhone,String email){
    	List<MbrshipVO> mbrShipList = null;
    	List<MembershipVO> membershipVO = null; 
    	if(0 == mbrId){
		MemberDTO memberDTO=null;
		List<MemberDTO>  memberDTOList=null;
		if(null !=mobilePhone &&!mobilePhone.equals("")){
			 memberDTO=memberBaseInfoDelegate.getMemberByMobile(mobilePhone);
			if(memberDTO ==null && null !=email &&!email.equals("")){
				memberDTOList=memberBaseInfoDelegate.getMemberByEmail(email);
			}
		}else if(null !=email &&!email.equals("")){
			memberDTOList=memberBaseInfoDelegate.getMemberByEmail(email);
		}
		if(memberDTOList != null && memberDTOList.size()>0){
			memberDTO= memberDTOList.get(0);
		}
		if(memberDTO != null){
		mbrId = memberDTO.getId();
		}
    	}
    	MbrshipVO myMbrship=new MbrshipVO();
		myMbrship.setMbrId(mbrId);
		mbrShipList = memberBaseInfoDelegate.myMbrshipListByExp(myMbrship);
		if(null !=mbrShipList && mbrShipList.size()>0){
			membershipVO = new  ArrayList<MembershipVO>();
			for(int i=0;i<mbrShipList.size();i++){
				MembershipVO membership = new MembershipVO();
				membership.setMbrId(mbrShipList.get(i).getMbrId());
				membership.setOldMbrshipCd(mbrShipList.get(i).getOldMbrshipCd());
				membership.setCategoryName(mbrShipList.get(i).getCategoryName());
				membership.setCategoryCd(mbrShipList.get(i).getCategoryCd());
				String id = FITAliasConstant.fitAliasObj.get(mbrShipList.get(i).getCategoryCd());
				membership.setReturnCash(id==null?false:true);
				membershipVO.add(membership);
			}
		}
		return membershipVO;
    }

	public MemberPassengerDelegate getMemberPassengerDelegate() {
		return memberPassengerDelegate;
	}
	public void setMemberPassengerDelegate(
			MemberPassengerDelegate memberPassengerDelegate) {
		this.memberPassengerDelegate = memberPassengerDelegate;
	}
	public MemberHisLinkPersonDelegate getMemberHisLinkPersonDelegate() {
		return memberHisLinkPersonDelegate;
	}
	public void setMemberHisLinkPersonDelegate(
			MemberHisLinkPersonDelegate memberHisLinkPersonDelegate) {
		this.memberHisLinkPersonDelegate = memberHisLinkPersonDelegate;
	}
	public MemberBaseInfoDelegate getMemberBaseInfoDelegate() {
		return memberBaseInfoDelegate;
	}
	public void setMemberBaseInfoDelegate(
			MemberBaseInfoDelegate memberBaseInfoDelegate) {
		this.memberBaseInfoDelegate = memberBaseInfoDelegate;
	}
	public HotelManage getHotelManage() {
		return hotelManage;
	}
	public void setHotelManage(HotelManage hotelManage) {
		this.hotelManage = hotelManage;
	}
	public PersonMainInfo register(MbrInfoDTO mbrInfoDTO) {
		return memberBaseInfoDelegate.registerNewMember(mbrInfoDTO);
	}
	public List<Mbrship> mbrshipListByMbrId(Long mbrId){
		return memberBaseInfoDelegate.mbrshipListByMbrId(mbrId);
	}
	public Long getMbrIdByMemberCd(String memberCd){
		return memberBaseInfoDelegate.queryMbrIdByOldMbrshipCd(memberCd);
		
	}
	public Mbrship mbrshipCreate(Long mbrId,Long mbrshipCategoryId,String aliasNo,String isBloc, MbrTransactionLogVO mbrTransactionLogVO)throws MbrshipServiceException{
		return memberBaseInfoDelegate.mbrshipCreate(mbrId, mbrshipCategoryId, aliasNo, isBloc, mbrTransactionLogVO);
		
	}
}
