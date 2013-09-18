package com.mangocity.hotel.order.web;


import java.util.List;

import com.mangocity.hotel.order.service.IDebitCardService;

/**
 */
public class DebitCardAction extends OrderAction {

    private static final long serialVersionUID = 7387855879548843772L;

    private IDebitCardService debitCardService;
    
    private List debitCardList = null;

    private String memberCd;
     
    public String execute() {
        try {
            roleUser = getOnlineRoleUser();
            // member = TranslateUtil.translateMember(memberService.getMemberByMemberCd(membercd));
            // memberid = member.getId();
            debitCardList = debitCardService.getDebitCardHistoryLis(memberCd);
        } catch (Exception e1) {
        	log.error("" + e1.getMessage());
        }

        return SUCCESS;
    }


	public IDebitCardService getDebitCardService() {
		return debitCardService;
	}


	public void setDebitCardService(IDebitCardService debitCardService) {
		this.debitCardService = debitCardService;
	}


	public String getMemberCd() {
		return memberCd;
	}


	public void setMemberCd(String memberCd) {
		this.memberCd = memberCd;
	}


	public List getDebitCardList() {
		return debitCardList;
	}

	public void setDebitCardList(List debitCardList) {
		this.debitCardList = debitCardList;
	}

   

}
