package com.mangocity.webnew.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.mangocity.util.StringUtil;

import com.mangocity.hotel.base.web.webwork.PaginateAction;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.ext.member.util.MemberUtil;
import com.mangocity.model.mbrship.Mbrship;
import com.mangocity.util.DateUtil;

/**
 * 查询会员订单的分页Action
 * 
 * @author Neil
 */
public class PaginateQueryMemberOrders extends PaginateAction {

    private static final long serialVersionUID = -5044687536772573325L;
    @SuppressWarnings("unchecked")
    public String execute() {
    	Map params = super.getParams();
    	String memberCD = String.valueOf(params.get("memberCD"));
    	String begindate = String.valueOf(params.get("begindate"));
    	String enddate = String.valueOf(params.get("enddate"));
    	String memberCdStr = String.valueOf( params.get("memberCdStr"));
		if (StringUtil.isValidStr(memberCD) && !"null".equals(memberCD)) {
			begindate = String.valueOf(params.get("begindate"));
			enddate = String.valueOf(params.get("enddate"));
			if (null == memberCdStr || "null".equals(memberCdStr)|| "".equals(memberCdStr)) {
				Long mbrID = this.getMemberBaseInfoDelegate().queryMbrIdByOldMbrshipCd(memberCD);
				List<Mbrship> mbrShipList = this.getMemberBaseInfoDelegate().mbrshipListByMbrId(mbrID);
				if (null != mbrShipList && mbrShipList.size() > 0) {
					memberCdStr = "'" + mbrShipList.get(0).getOldMbrshipCd()+ "'";
					for (int i = 1; i < mbrShipList.size(); i++) {
						memberCdStr = memberCdStr + ",'"+ mbrShipList.get(i).getOldMbrshipCd() + "'";
					}
				}
				params.put("memberCdStr", memberCdStr);
			}

		} else {
			HttpSession session = request.getSession();
			memberCD = (String) session.getAttribute("memberCD");
			memberCdStr = (String) session.getAttribute("memberCdStr");
			params.put("memberCD", memberCD);
			params.put("memberCdStr", memberCdStr);
		}

		String mbrId = getMbrIdForWeb();
		String mbrSign = getMbrSignForWeb();
		boolean isAvalibleMember = false;
		if (StringUtil.isValidStr(memberCD)) {
			MemberDTO memerDTO = getMemberSimpleInfoByMemberCd(memberCD, false);
			if (null != memerDTO) {
				if (StringUtil.isValidStr(mbrId)
						&& StringUtil.isValidStr(mbrSign)
						&& String.valueOf(memerDTO.getId()).equals(mbrId)) {
					isAvalibleMember = MemberUtil.isAvalibleMemberByMbrId(
							mbrId, mbrSign);// 是否有效的会员
				}
			}
		}
		
		//如果日期为空，默认设置开始结束日期间隔60天
    	if(begindate == null 
			|| "null".equals(begindate) 
			|| "".equals(begindate) 
			|| enddate == null 
			|| "null".equals(enddate) 
			|| "".equals(enddate)){
    		begindate = DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), -30));
    		enddate = DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), 30));
    		params.put("begindate", begindate);
    		params.put("enddate", enddate);
    	}
    	log.info("memberCD in params is: " + memberCD
				+ " | mbrId in params is: " + mbrId
				+ " | Begindate in params is ：" + begindate
				+ " | Enddate in params is ：" + enddate);
		if (isAvalibleMember) {
			return super.execute();
		} else {
			log.error("memberCD is :"+memberCD+"   mbrId is :"+mbrId+"   mbrSign is :"+mbrSign+"     memberCdStr:"+memberCdStr);
			return forwardMsgBox("请登陆",null);
		}

    }

    
}
