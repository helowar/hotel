package com.mangocity.hotel.base.web;

import java.io.Serializable;
import java.util.Map;

import org.ecside.common.util.RequestUtil;

import com.ibatis.common.util.PaginatedList;
import com.mangocity.hotel.base.web.webwork.GenericAction;
import com.mangocity.util.StringUtil;
import com.mangocity.util.dao.DAOIbatisImpl;

public class PaginateSupplierInfoAction extends GenericAction  implements Serializable{
	
    /**
     * 查询ID
     */
    private String queryID;
    
    /**
     * queryDao
     */
    private DAOIbatisImpl queryDao;
    
    /**
     * 跳转
     */
    private String forward;
    
    protected String isFromUnlock;
    
    public String querySupplierInfo(){
        if (null != queryID) {

            Map params = this.getParams();
            
            if(null!=params.get("queryID")&&"selectSupplierInfo".equals(params.get("queryID"))){
                if(null!=super.getOnlineRoleUser()&&null!=super.getOnlineRoleUser().getName())
                   params.put("userName", super.getOnlineRoleUser().getLoginName());
            }
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

            int pageSize = RequestUtil.getCurrentRowsDisplayed(request);
            PaginatedList results = queryDao.queryByPagination(queryID, params, pageSize);
            
            if(results.size() == 0 && results.getPageSize() > 0){
            	params.put("pageNo", 1);
            	results = queryDao.queryByPagination(queryID, params, pageSize);
            }

            RequestUtil.initLimit(request, results.getPageSize(), results.size());
            request.setAttribute("records", results);
            
            forward = StringUtil.isValidStr(forward) ? forward : queryID;
            
            return forward;

        } else {
            return forwardError("查询ID号不能为空！");
        }
    }   

	public String getQueryID() {
		return queryID;
	}

	public void setQueryID(String queryID) {
		this.queryID = queryID;
	}

	public DAOIbatisImpl getQueryDao() {
		return queryDao;
	}

	public void setQueryDao(DAOIbatisImpl queryDao) {
		this.queryDao = queryDao;
	}

	public String getForward() {
		return forward;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	public String getIsFromUnlock() {
		return isFromUnlock;
	}

	public void setIsFromUnlock(String isFromUnlock) {
		this.isFromUnlock = isFromUnlock;
	}

}
