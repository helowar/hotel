package com.mangocity.hagtb2b.web;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.ecside.common.util.RequestUtil;

import com.ibatis.common.util.PaginatedList;
import com.mangocity.hagtb2b.persistence.AgentOrg;
import com.mangocity.hotel.order.web.GenericCCAction;
import com.mangocity.security.domain.OrgInfo;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.hotel.constant.BaseConstant;
/**
 * 需要传入queryID－ibatis的sql查询ID
 * 
 * @author zhengxin 分页Action
 */
public class B2bAgentOrgAction
        extends GenericCCAction implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 7645693026317666926L;

    private DAOIbatisImpl queryDao;// parasoft-suppress SERIAL.NSFSC "暂不修改" 

    protected String isFromUnlock;

    /**
     * 查询ID
     */
    private String queryID;

    /**
     * 跳转
     */
    private String forward;

    List<AgentOrg> orgList;


	public String getAgentList(){
		orgList = queryDao.queryForList("queryAgentOrg", null);
		return "agentList";
	}
    public String queryAgentList() {

             Map params = this.getParams();
            int pageNo = 1;
            if (!StringUtil.isValidStr(isFromUnlock)) {
                pageNo = RequestUtil.getPageNo(request);
            } else {
                String sPageNo = (String) params.get("ec_p");
                if (StringUtil.isValidStr(sPageNo)) {
                    pageNo = Integer.parseInt(sPageNo);
                }
                putSession("isFromUnlock", null);
                request.setAttribute("fromUnlock", "1");
            }
            params.put("pageNo", Integer.valueOf(pageNo));
           
            String beginDate = (String) params.get("beginDate");
            int pageSize = RequestUtil.getCurrentRowsDisplayed(request);
            PaginatedList results = queryDao.queryByPagination(queryID, params, pageSize);
            
            //如果在第N页查询时,结果可能会查不到,要重新查才有结果,针对这个的解决办法 by juesuchen
            //getPageSize() means return the maximum number of items per page ,it will equals totalNum
            if(results.size() == 0 && results.getPageSize() > 0){
            	params.put("pageNo", 1);
            	results = queryDao.queryByPagination(queryID, params, pageSize);
            }

            RequestUtil.initLimit(request, results.getPageSize(), results.size());
            request.setAttribute("records", results);
            
            //TlvUtil.getMapToString(super.getSession());
            forward = StringUtil.isValidStr(forward) ? forward : queryID;
            
            return forward;

   

    }
    
  
    public DAOIbatisImpl getQueryDao() {
        return queryDao;
    }

    public void setQueryDao(DAOIbatisImpl queryDao) {
        this.queryDao = queryDao;
    }

    public String getQueryID() {
        return queryID;
    }

    public void setQueryID(String queryID) {
        this.queryID = queryID;
    }

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }
    
	public List<AgentOrg> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<AgentOrg> orgList) {
		this.orgList = orgList;
	}
}


