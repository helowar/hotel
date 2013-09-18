package com.mangocity.hagtb2b.web;


import java.util.ArrayList;
import java.util.List;

import com.mangocity.hagtb2b.persistence.CommPolicySecond;
import com.mangocity.hagtb2b.service.IB2bService;
import com.mangocity.hotel.base.web.webwork.GenericAction;
import com.mangocity.util.dao.DAOIbatisImpl;

public class B2bAgentPolicySecond extends GenericAction {
	
	
	private static  Integer NIGHTROOMNUM =100;
	private static  double COMM1 =20;
	private static  double COMM2 =25;
	
	private IB2bService b2bService;
	private List<CommPolicySecond> policyLst;
	
	private String b2BIdLst;
	
	private String active;
	
	private DAOIbatisImpl queryDao;


	public String save(){
		if(b2BIdLst!=null){
			String[] arrB2B = b2BIdLst.split(",");
			policyLst = new ArrayList();
			for(int i=0;i<arrB2B.length;i++){
				CommPolicySecond cp = new CommPolicySecond();
				cp.setActive(Integer.parseInt(active));
				cp.setAgentCode(arrB2B[i]);
				cp.setNightRoomNum(NIGHTROOMNUM);
				cp.setComm1(COMM1);
				cp.setComm2(COMM2);
				policyLst.add(cp);
			}
			try{
				
				String tempStr  = b2BIdLst.replaceAll(",", "','");
				b2bService.sqlUpdate("delete from b2b_commpolicy_second where agentcode in ('"+b2BIdLst+"')");
				b2bService.batchUpdate(policyLst);
			}catch(Exception ex){
				log.error(ex.getMessage(),ex);
				return super.forwardError("保存阶梯返佣错误!"+ex.getMessage());
			}
		}
		return "success";
	}
	
	
	public String getB2BIdLst() {
		return b2BIdLst;
	}
	public void setB2BIdLst(String idLst) {
		b2BIdLst = idLst;
	}


	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public DAOIbatisImpl getQueryDao() {
		return queryDao;
	}

	public void setQueryDao(DAOIbatisImpl queryDao) {
		this.queryDao = queryDao;
	}

	public IB2bService getB2bService() {
		return b2bService;
	}

	public void setB2bService(IB2bService service) {
		b2bService = service;
	}


	public List<CommPolicySecond> getPolicyLst() {
		return policyLst;
	}


	public void setPolicyLst(List<CommPolicySecond> policyLst) {
		this.policyLst = policyLst;
	}

	

}
