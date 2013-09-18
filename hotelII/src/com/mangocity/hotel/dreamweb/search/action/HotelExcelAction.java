package com.mangocity.hotel.dreamweb.search.action;

import java.util.List;

import com.mangocity.hotel.base.service.GeographicalPositionService;
import com.opensymphony.xwork2.ActionSupport;

public class HotelExcelAction extends ActionSupport{

		public GeographicalPositionService geographicalPositionService;
		private List memberList;
		public String execute(){
			geographicalPositionService.generatorPosition();
			return SUCCESS;
		}
		public GeographicalPositionService getGeographicalPositionService() {
			return geographicalPositionService;
		}
		public void setGeographicalPositionService(
				GeographicalPositionService geographicalPositionService) {
			this.geographicalPositionService = geographicalPositionService;
		}
		public List getMemberList() {
			return memberList;
		}
		public void setMemberList(List memberList) {
			this.memberList = memberList;
		}
		
}
