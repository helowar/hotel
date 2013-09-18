package com.mangocity.hotel.base.service.impl;

import java.util.List;

import com.mangocity.hotel.base.service.IOperationSetService;
import com.mangocity.hotel.base.service.assistant.OperationParam;
import com.mangocity.util.dao.DAO;
import com.mangocity.util.dao.DAOHibernateImpl;

public class OperationSetServiceImpl implements IOperationSetService {

	//must use DAOHibernateImpl;
	private DAO dao;

	public OperationParam getAssignMode() {
		String hsql = "from OperationParam a " + "where "
				+ "a.name = 'ASSIGN_MODE' ";
		OperationParam param = (OperationParam) dao.find(hsql);
		return param;
	}

	public String queryUserOrgInfo(String userLoginId) {
		  String orgName = "";
	        String sql = " select i.org_name " +
	                      " from css.css_organizaioninfo i, css.css_users u " +
	                      " where i.org_id = u.orginfoid " +
	                      " and u.loginname = ?";
	        List result = ((DAOHibernateImpl)dao).doquerySQL(sql, new Object[]{userLoginId}, false);
	        if(null != result && !result.isEmpty()){
	            orgName = String.valueOf(result.get(0));
	        }
	        return orgName;
	}
	
	public String[] queryLoginNameAndUserName(String userId){
		 String userName = "";
		 String loginName = "";
		 String sql = " select u.loginname,u.name from css.css_users u where u.id = ? ";
		 List<Object[]> result = ((DAOHibernateImpl)dao).doquerySQL(sql, new Object[]{userId}, false);
		 if(null != result && !result.isEmpty()){
			 loginName = String.valueOf(result.get(0)[0]);
			 userName = String.valueOf(result.get(0)[1]);
		 }
		 String[] strs = new String[]{loginName,userName};
		 return strs;
	}
	
	public void updateAssignMode(OperationParam sendFax) {
		dao.saveOrUpdate(sendFax);

	}

	public void setDao(DAO dao) {
		this.dao = dao;
	}

}
