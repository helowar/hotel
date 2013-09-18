package com.mangocity.hotel.base.web.webwork;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ecside.common.util.RequestUtil;

import com.ibatis.common.util.PaginatedList;
import com.mangocity.ep.service.EpDailyAuditService;
import com.mangocity.hotel.base.dao.EpOrderDAO;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.hotel.constant.BaseConstant;

/**
 * 需要传入queryID－ibatis的sql查询ID
 * 
 * @author zhengxin 分页Action
 */
public class PaginateAction extends GenericAction {// parasoft-suppress SERIAL.NSFSC "暂不修改" 
	
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

    private EpOrderDAO epOrderDAO;
    
    private EpDailyAuditService epDailyAuditService;
    
	public String execute() {

        if (null != queryID) {

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
            String beginDate = (String) params.get("beginDate");
            if (null != beginDate && !beginDate.equals("")) {
                request.setAttribute("beginDate1", DateUtil.addStringDate(beginDate, 0));
                request.setAttribute("beginDate2", DateUtil.addStringDate(beginDate, 1));
                request.setAttribute("beginDate3", DateUtil.addStringDate(beginDate, 2));
            }

            params.put("pageNo", Integer.valueOf(pageNo));
            String areaCode = getAreaCode();
            if (StringUtil.isValidStr(areaCode) && !BaseConstant.SZCODE.equals(areaCode))
                params.put("area", areaCode);

            int pageSize = RequestUtil.getCurrentRowsDisplayed(request);
            PaginatedList results = queryDao.queryByPagination(queryID, params, pageSize);
            
            //如果在第N页查询时,结果可能会查不到,要重新查才有结果,针对这个的解决办法 by juesuchen
            //getPageSize() means return the maximum number of items per page ,it will equals totalNum
            if(results.size() == 0 && results.getPageSize() > 0){
            	params.put("pageNo", 1);
            	results = queryDao.queryByPagination(queryID, params, pageSize);
            }
            if(queryID.equals("queryAll_Da_DailyAudit") || queryID.equals("queryMy_Da_DailyAudit")){
            	List<String> hotelIds= epOrderDAO.queryEpHotelId();
            	String dailyAuditIds = "";
            	int index =0;
            	if(results!=null && (index=results.size())>0){
            		for(int i=0 ; i<=index-1 ;i++){
                		dailyAuditIds= dailyAuditIds.length()>0 ? dailyAuditIds+","+String.valueOf(((Map)results.get(i)).get("DAILYAUDIT_ID")) : 
                			String.valueOf(((Map)results.get(i)).get("DAILYAUDIT_ID"));	
            		}
            		Map<String,String> mapIds = epDailyAuditService.queryHotelIdByDaliyAuditId(dailyAuditIds);
            		if(mapIds!=null && mapIds.size()>0){
            			for(int i=0 ; i<=index-1 ;i++){
            				boolean isEpHotel=false;
                    		String id = mapIds.get(String.valueOf(((Map)results.get(i)).get("DAILYAUDIT_ID")));
            				for(String hotelId : hotelIds){
            					if(id!=null && id.equals(hotelId)){
            						isEpHotel=true;
            						break;
            					}
            				}
            				if(isEpHotel){
            					String channelName=((Map)results.get(i)).get("CHANNELNAME")!=null ? String.valueOf(((Map)results.get(i)).get("CHANNELNAME")) : "";
            					((Map)results.get(i)).remove("CHANNELNAME");
            					((Map)results.get(i)).put("CHANNELNAME", channelName+"<font color='red'><Strong>"+"&nbsp;&nbsp;&nbsp;EP"+"</Strong></font>");
            				}
                		}
      
            		}
            	}
            	
            }
            if(params.get("param_request_uri")!=null && params.get("param_request_uri").toString().indexOf("HAGTB2B")>-1){
            	//B2B的不执行setOrderStates
            }else{
            	setOrderStates(results);
            }
            
            RequestUtil.initLimit(request, results.getPageSize(), results.size());
            request.setAttribute("records", results);

            forward = StringUtil.isValidStr(forward) ? forward : queryID;

            return forward;

        } else {
            return forwardError("查询ID号不能为空！");
        }

    }

    /**
     * 查询登陆人的区域代码
     * @return
     */
    public String getAreaCode() {
        String area = "";
        if (null != super.getOnlineRoleUser()) {
            Map session = super.getSession();
            //Map中存放登陆人的组织结构名称
            Map<String,String> orgInfoMap = (Map<String,String>) session.get("orgInfo");
            //登陆人工号
            String loginName = super.getOnlineRoleUser().getLoginName();
            //如果Map中存在组织人的组织结构直接返回
            if(null != orgInfoMap && orgInfoMap.containsKey(loginName)){
                area = orgInfoMap.get(loginName);
            }else{
                //查询组织名 HN_华南地区
//                String orgName = hraManager.queryUserOrgInfo(loginName);
                String orgName = super.getOnlineRoleUser().getOrgInfo().getOrgName();
                if(StringUtil.isValidStr(orgName) && 2 < orgName.length()){
                    // area=orgName.substring(0,2);//HN_华南地区,取得前面的HN
                    String[] names = orgName.split("_");
                    if (1 < names.length) {
                        area = names[0];// HN_华南地区,取得前面的HN
                        if(null == orgInfoMap){
                            orgInfoMap = new HashMap<String,String>();
                        }
                        //把区域缓存到Session中
                        orgInfoMap.put(loginName, area);
                        session.put("orgInfo", orgInfoMap);
                    }
                }
            }
        }
        return area;
    }

    private void setOrderStates(PaginatedList results){
    	
    	if(results!=null){
    		for(int i=0;i<results.size();i++){
    			Map resultMap=(Map) results.get(i);
    			String payMethod=String.valueOf(resultMap.get("PAYMETHOD"));
    			if("pre_pay".equals(payMethod)){
    				setPrePayOrderState(resultMap);
    			}
    			else{
    				serPayOrderState(resultMap);
    			}
    		}
    	}
    }
    
    private void setPrePayOrderState(Map resultMap){
   
    	String orderStateStr=String.valueOf(resultMap.get("ORIGINSTATE"));
    	int orderState=(resultMap.get("ORIGINSTATE")==null ? 0 : Integer.valueOf(orderStateStr));

    	String comfirmStr=String.valueOf(resultMap.get("CUSTOMERCONFIRM"));

    	int comfirm=(resultMap.get("CUSTOMERCONFIRM")==null?0:Integer.valueOf(comfirmStr));
    	int audit= (resultMap.get("AUDITEDDATE")==null?0:1);//1.已审核，0.未审核
    	
    	if(orderState==1){
    		resultMap.put("ORDERSTATES", "1");//预付未提交
    	}
    	else if(orderState==2||(orderState==3 && comfirm==0)||(orderState==8 && comfirm==0)||orderState==9 || orderState==10 || orderState==12){
    		resultMap.put("ORDERSTATES", "3");//处理中
    	}
    	else if(orderState==3 && comfirm==1){
    		resultMap.put("ORDERSTATES", "4");//预订成功
    	}
    	else if(orderState==8 && comfirm==1){
    		resultMap.put("ORDERSTATES", "5");//交易成功
    	}
    	else if(orderState==14 ){
    		resultMap.put("ORDERSTATES", "7");//预订取消
    	}
    	else {
    		resultMap.put("ORDERSTATES", resultMap.get("ORIGINSTATE"));
    	}
    	
    }
    private void serPayOrderState(Map resultMap){
    	String orderStateStr=String.valueOf(resultMap.get("ORIGINSTATE"));
    	int orderState=(resultMap.get("ORIGINSTATE")==null ? 0 : Integer.valueOf(orderStateStr));

    	String comfirmStr=String.valueOf(resultMap.get("CUSTOMERCONFIRM"));
    	int comfirm=(resultMap.get("CUSTOMERCONFIRM")==null?0:Integer.valueOf(comfirmStr));
    	
    	int audit= (resultMap.get("AUDITEDDATE")==null?0:1);
    	if(orderState==1){
    		resultMap.put("ORDERSTATES", "1");
    	}
    	else if(orderState==2||(orderState==3 && comfirm==0)){
    		resultMap.put("ORDERSTATES", "3");
    	}
    	else if(orderState==4 ||(orderState==3 && comfirm==1)||(orderState==5 && audit==0)||(orderState==13 && audit==0)){
    		resultMap.put("ORDERSTATES", "4");
    	}
    	else if((orderState==5 && audit==1)|| orderState==6 || orderState==7){
    		resultMap.put("ORDERSTATES", "5");
    	}
    	else if((orderState==13 && audit==1) || orderState==14 ){
    		resultMap.put("ORDERSTATES", "7");
    	}
    	else {
    		resultMap.put("ORDERSTATES", resultMap.get("ORIGINSTATE"));
    	}
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

	public EpOrderDAO getEpOrderDAO() {
		return epOrderDAO;
	}

	public void setEpOrderDAO(EpOrderDAO epOrderDAO) {
		this.epOrderDAO = epOrderDAO;
	}

	public EpDailyAuditService getEpDailyAuditService() {
		return epDailyAuditService;
	}

	public void setEpDailyAuditService(EpDailyAuditService epDailyAuditService) {
		this.epDailyAuditService = epDailyAuditService;
	}
    
}
