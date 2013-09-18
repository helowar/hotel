package com.mangocity.hotel.order.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ecside.common.util.RequestUtil;

import com.ibatis.common.util.PaginatedList;
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
public class PaginatePersonalAction
        extends GenericCCAction implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 7645693026317666926L;

    private DAOIbatisImpl queryDao;// parasoft-suppress SERIAL.NSFSC "暂不修改" 

    protected String isFromUnlock;
    
    private List roomList;
    private long hotelId;

    /**
     * 查询ID
     */
    private String queryID;

    /**
     * 跳转
     */
    private String forward;

    public String queryPersonal() {

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
            if ("".equals(areaCode) && !areaCode.equals(BaseConstant.SZCODE))
                params.put("area", getAreaCode());

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

        } else {
            return forwardError("查询ID号不能为空！");
        }

    }
    
    public String getAreaCode() {
        String area = "";
        String orgName = "";
        if (null != super.getOnlineRoleUser()) {

            OrgInfo orgInfo = super.getOnlineRoleUser().getOrgInfo();
            if (null != orgInfo) {
                orgName = orgInfo.getOrgName();
                if (null != orgName && "".equals(orgName) && 2 < orgName.length()) {
                    // area=orgName.substring(0,2);//HN_华南地区,取得前面的HN
                    String[] names = orgName.split("_");
                    if (1 < names.length) {
                        area = names[0];// HN_华南地区,取得前面的HN
                    } else {
                        return "";
                    }
                }
            }
        }
        // ---深圳排除在外
        /*
         * if(area.equalsIgnoreCase(BaseConstant.SZCODE)) area="SZ";
         */
        if (null == area)
            area = "";
        return area;
    }
    /**
     * dongjietao
     * @return
     */
    public String queryRoomType(){
    	 BigDecimal  room_type_id=null;
    	 BigDecimal  id=null;  //中转变量
    	 String room_name=null;
    	 String name=null; //中转变量
    	 String room_type=null;
    	 String type; //中转变量
    	 roomList=null;
    	 roomList=queryDao.queryForList("select_queryid", hotelId);
    	 List l=queryDao.queryForList("select_hotel_name", hotelId);
    	 request.setAttribute("hotel_name", ((Map)l.iterator().next()).get("CHN_NAME"));
    	 Iterator<Map> iter=roomList.iterator();
    	 while(iter.hasNext()){   //把床型代号转换为具体床型
    		 Map map=iter.next();
    		 String bed_type=(String) map.get("BED_TYPE");
     		 if("1".equals(bed_type)){
    			  map.put("BED_TYPE", "大床");
    		 }
    		 if("1,2".equals(bed_type)){	 
	   			  map.put("BED_TYPE", "大床,双床");  
	   		     }
    		 if("1,2,3".equals(bed_type)){
	   			  map.put("BED_TYPE", "大床,双床,单床");
	   		     }
    		 if("2,3".equals(bed_type)){
	   			  map.put("BED_TYPE", "双床,单床");
	   		     }
    		 if("2".equals(bed_type)){
    			 
   			  map.put("BED_TYPE", "双床");
   		     }
    		 if("1,3".equals(bed_type)){
      			  map.put("BED_TYPE", "单床");
      		     }
    		 if("3".equals(bed_type)){
   			  map.put("BED_TYPE", "单床");
   		     }		 
    		 id=(BigDecimal) map.get("ROOM_TYPE_ID");
    		 name=(String) map.get("ROOM_NAME");
    		 if(((BigDecimal) map.get("ROOM_TYPE_ID")).equals(room_type_id)&&map.get("ROOM_NAME").equals(room_name)){
    			 
    			 map.put("ROOM_NAME", null);
    		 }
    		 room_name=name;
    		 if(((BigDecimal) map.get("ROOM_TYPE_ID")).equals(room_type_id)){
    			       
    		           map.put("ROOM_TYPE_ID", null);
    		           type=(String) map.get("BED_TYPE");
    		          if(map.get("BED_TYPE").equals(room_type)){
    		    			 
    		    			 map.put("BED_TYPE", null);
    		    	   } 
    		          room_type=type;
    		 }
    		 room_type_id=id;
    		
    	 }
    	
    	return "roomtype";
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

	public long getHotelId() {
		return hotelId;
	}

	public void setHotelId(long hotelId) {
		this.hotelId = hotelId;
	}

	public List getRoomList() {
		return roomList;
	}

	public void setRoomList(List roomList) {
		this.roomList = roomList;
	}
    
}
