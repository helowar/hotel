package com.mangocity.hotel.newroomcontrol.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlRoomcontrolSorting;
import com.mangocity.hotel.base.web.webwork.GenericAction;
import com.mangocity.hotel.newroomcontrol.service.NewRoomControlService;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.bean.MyBeanUtil;

/**
 * 修改房控酒店排序Action
 * @author chenjiajie
 *
 */
public class RoomControlSortingAction extends GenericAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5107342115071498039L;

	/**
	 * 房控改版Service 接口
	 */
	private NewRoomControlService newRoomControlService;
	
	/**
	 * 房控改版 动态排序 参数表 
	 */
	private List<HtlRoomcontrolSorting> htlRoomcontrolSortingList = new ArrayList<HtlRoomcontrolSorting>();
	
	/**
	 * 集合的大小
	 */
	private int sortingListNum = 0;
	
	/**
	 * 跳转至排序显示页面
	 * @return
	 */
	public String forward(){
		htlRoomcontrolSortingList = newRoomControlService.getRoomControlSortingList();
		if(null != htlRoomcontrolSortingList && !htlRoomcontrolSortingList.isEmpty()){
			sortingListNum = htlRoomcontrolSortingList.size();
		}else{
			sortingListNum = 0;
		}
		return "editRoomControlSorting";
	}
	
	/**
	 * 保存排序
	 * @return
	 */
	public String update(){
		Map params = super.getParams();
		List<HtlRoomcontrolSorting> sortingTempList = MyBeanUtil.getBatchObjectFromParam(params, HtlRoomcontrolSorting.class, sortingListNum);
		if(null != sortingTempList && !sortingTempList.isEmpty()){
			UserWrapper user = super.getCurrentUser();
			for (HtlRoomcontrolSorting htlRoomcontrolSorting : sortingTempList) {
				htlRoomcontrolSorting.setModifytime(new Date());
				if(null != user){
					htlRoomcontrolSorting.setModifybyid(user.getLoginName());
					htlRoomcontrolSorting.setModifybyname(user.getName());
				}
			}
			newRoomControlService.updateAllSorting(sortingTempList);
		}
		return "finishEdit";
	}

	/*getter and setter*/
	public NewRoomControlService getNewRoomControlService() {
		return newRoomControlService;
	}

	public void setNewRoomControlService(NewRoomControlService newRoomControlService) {
		this.newRoomControlService = newRoomControlService;
	}

	public List<HtlRoomcontrolSorting> getHtlRoomcontrolSortingList() {
		return htlRoomcontrolSortingList;
	}

	public void setHtlRoomcontrolSortingList(
			List<HtlRoomcontrolSorting> htlRoomcontrolSortingList) {
		this.htlRoomcontrolSortingList = htlRoomcontrolSortingList;
	}

	public int getSortingListNum() {
		return sortingListNum;
	}

	public void setSortingListNum(int sortingListNum) {
		this.sortingListNum = sortingListNum;
	}
}
