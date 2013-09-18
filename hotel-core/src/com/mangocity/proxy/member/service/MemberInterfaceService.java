package com.mangocity.proxy.member.service;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.ext.member.dto.MbrInfoDTO;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.model.mbrship.Mbrship;
import com.mangocity.model.mbrsys.MbrTransactionLogVO;
import com.mangocity.model.person.PersonMainInfo;
import com.mangocity.proxy.vo.MembershipVO;
import com.mangocity.services.mbrship.MbrshipServiceException;

/**
 * @author oliverkang
 */
public interface MemberInterfaceService {
    
	
	/**
	 * 公用的会员编号, CD
	 */ 
	
	public static final String COMMONMEMBERCD = "0001397022";
    /**
	 * 公用的会员编号, ID
	 */
	public static final Long COMMONMEMBERID = 3065103L;
	/**
     * 根据会员编码获取会员。
     * 
     * @param code
     *            会员编码。
     * @return 会员。
     */
	MemberDTO getMemberByCode(String memberCd);
    /**
     * 根据会员移动电话号码获取会员
     * 
     * @param mobile
     *            移动电话号码。
     * @return 会员。
     */
    MemberDTO getMemberByMobile(String mobile);

    /**
     * 根据会员email获取会员。
     * 
     * @param email
     *            会员email
     * @return 会员。
     */
    List getMemberByEmail(String email);

    /**
     * 根据membercd获取会员常入住人和常联系人
     * 
     * @param membercd
     * @param fellow
     * @param linkman
     */
    public void getFellowAndLinkmanByMemberCd(MemberDTO member);

    /**
     * 根据订单的入住人列表和联系人更新会员的常入住人列表和常联系人
     * 
     * @param member	会员
     * @param order		订单
     * @param bFellow	是否更新常入住人
     * @param bLinkman	是否更新常联系人
     */
    public void updateMemberFellowAndLinkman(MemberDTO memberDTO, OrOrder order, boolean bFellow,
        boolean bLinkman);
    
    /**
     * TMC-V2.0 根据membercd获取历史入住人 add by shengwei.zuo  2010-4-28
     * 
     * @param membercd
     * @param fellow
     * @param linkman
     */
    public void getFellowByMemberCd(String memberCd, Map fellow);
    
    /**
     * 注册一个会员
     * @param mbrInfoDTO
     * @return
     */
	public PersonMainInfo register(MbrInfoDTO mbrInfoDTO);
	/**
	 * 根据会员ID获取该会员下的所有的会籍
	 * @param mbrId
	 * @return
	 */
	public List<Mbrship> mbrshipListByMbrId(Long mbrId);
	/**
	 * 通过会籍Id获取会员Id
	 * @param memberCd
	 * @return
	 */
	public Long getMbrIdByMemberCd(String memberCd);
	/**
	 * 给某个会员增加一个会籍
	 * @param mbrId 会员Id
	 * @param mbrshipCategoryId 会籍类型Id
	 * @param aliasNo
	 * @param isBloc
	 * @param mbrTransactionLogVO
	 * @return
	 * @throws MbrshipServiceException
	 */
	public Mbrship mbrshipCreate(Long mbrId,Long mbrshipCategoryId,String aliasNo,String isBloc, MbrTransactionLogVO mbrTransactionLogVO)throws MbrshipServiceException;
	/**
	 * 根据当前登录会员或输入手机号、Email查询会员的所有会籍信息
	 * @param mobilePhone
	 * @param email
	 * @return
	 */
    public  List<MembershipVO> getMemberShipList(long mbrId,String mobilePhone,String email);

}
