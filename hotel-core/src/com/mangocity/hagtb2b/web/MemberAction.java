package com.mangocity.hagtb2b.web;

import com.mangocity.hotel.order.web.PopulateOrderAction;

public class MemberAction extends PopulateOrderAction {
	

	private static final long serialVersionUID = 46244827472442088L;
	private boolean loginStatus = false;
	
	
	public String execute(){
		try{
			member = getMemberInfoForWeb(false);
			if(member!=null){
				loginStatus = true;
			}
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
		log.info(loginStatus);
		return SUCCESS;
	}


	public boolean isLoginStatus() {
		return loginStatus;
	}


	public void setLoginStatus(boolean loginStatus) {
		this.loginStatus = loginStatus;
	}

}
