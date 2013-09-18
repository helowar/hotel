/**
 * 
 */
package com.mangocity.hotel.base.web;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.ClauseManage;
import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.IPriceManage;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlFavourableReturn;
import com.mangocity.hotel.base.persistence.HtlFavourableReturnInfo;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.service.IHotelFavourableReturnService;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.MyBeanUtil;

/**
 * @author xiongxiaojun
 *
 */
public class FavourableReturnAction extends PersistenceAction {
	
	private static final long serialVersionUID = 1L;

	private static final String POPEDOM_CONTROL_TYPE = "1";

	/**
	 * 酒店ID
	 */
	private Long hotelId;
	
	/**
	 * 酒店对象
	 */
	private HtlHotel htlHotel;
	/**
	 * 在增加或修改现金返还规则信息时传入合同开始日期
	 */
	private Date bDate;
	
	/**
	 * 在增加或修改现金返还规则信息时传入合同的结束日期
	 */
	private Date eDate;
	
	/**
	 * 一个酒店的所有价格类型
	 */
	private List lstRoomType;
	
	/**
	 * 提交页面规则的数目
	 */
	private int onlyNum;
	
	/**
     * 常量星期
     */
    private final String pweeks = "1,2,3,4,5,6,7,";
    
    //币种
    private String currency;
	
	/**
	 * 现金返还规则集合
	 */
	private List<HtlFavourableReturn> htlFavReturnLis ;
	
	// 开始日期
	private Date beginDate;

	// 结束日期
	private Date endDate;
	
	//修改返现的星期
	private String week;
	
	private Integer payMethod;
	
	//是否为添加操作
	private String addfor;
	
	//返现id
	private String favReturnID;
	
	private String hotelIdStr;
	
	private Double returnScale;
	
	//hotel management
	private HotelManage hotelManage;
	private ContractManage contractManage;
	private ClauseManage clauseManage;
	private IHotelFavourableReturnService returnService;
	//price management
	private IPriceManage priceManage;
	private HtlContract htlContractItems;
		
	private List htlHotelList;
	private List<HtlContract> htlContractList;
	private List<HtlFavourableReturn> favReturnList;
	
	//批量合共信息
	private Map<Long,Date> bDates = new HashMap<Long,Date>();
	private Map<Long,Date> eDates = new HashMap<Long,Date>();
	
    @SuppressWarnings("unchecked")
	public String queryFavReturnByHotelId(){
    	if(null==super.getOnlineRoleUser()||null==super.getOnlineRoleUser().getLoginName()){
    		return  super.forwardError("获取登陆用户信息失效,请重新登陆");
    	}
    	Map popedomMap=clauseManage.getPopedoms(POPEDOM_CONTROL_TYPE,super.getOnlineRoleUser().getLoginName(),hotelId);
    	//权限判断
    	if(popedomMap.isEmpty()){
    		return super.forwardError("你没有操作的权限!");
    	}
    	favReturnList=(List<HtlFavourableReturn>) popedomMap.get("FAVOURABLE_RETURN_LIST");
    	currency=(String) popedomMap.get("CURRENCY");
		return "forwardToFavReturnLst";
    }
    

	/**
     * 点击新增按钮，跳转到新增页面 add by zhijie.gu hotel2.9.3  2009-10-15 
     * 
     * 
     */
	@SuppressWarnings("unchecked")
	public String create(){
		//根据酒店ID得到酒店实体
		htlHotel = hotelManage.findHotel(hotelId);
		//根据酒店ID获取合同List;当有多个合同时，拿今天在合同开始日期和结束日期之间的合同
		Map<String,Object> contentMap=(Map<String, Object>) clauseManage.getContactContent(hotelId);
		lstRoomType=(List) contentMap.get("ROOM_TYPE_LIST");
		bDate=(Date) contentMap.get("BEGIN_DATE");
		eDate=(Date) contentMap.get("END_DATE");
		return "create";
		
	}
	
	/**
     *  新增或修改页面。点保存时调用的方法 add by zhijie.gu hotel2.9.3  2009-10-15 
     * 
     * 
     */
	public String edit(){
		if(null==super.getOnlineRoleUser()||null==super.getOnlineRoleUser().getLoginName()){
    		return  super.forwardError("获取登陆用户信息失效,请重新登陆");
    	}
		Map params = super.getParams();
		//从页面获取的参数。
		htlFavReturnLis = MyBeanUtil.getBatchObjectFromParam(params,HtlFavourableReturn.class, onlyNum);
		HtlFavourableReturn htlFavourableReturn=getFavourableReturnEntity();
		//批量增加现金返现信息
		if ("addFavourableReturn".equals(addfor)){
			if(null!=htlFavourableReturn.getPriceTypeIdStr() && !"".equals(htlFavourableReturn.getPriceTypeIdStr())){
				try {
					returnService.batchFavourableReturn(htlFavReturnLis,htlFavourableReturn,hotelId,super.getOnlineRoleUser());
				} catch (IllegalAccessException e) {
					log.error(e.getMessage(),e);
				} catch (InvocationTargetException e) {
					log.error(e.getMessage(),e);
				}
			}
		}else{//修改
			try {
				returnService.updateFavourableReturn(getFavourableReturnBean(),favReturnID,super.getOnlineRoleUser());
			} catch (IllegalAccessException e) {
				log.error(e.getMessage(),e);
			} catch (InvocationTargetException e) {
				log.error(e.getMessage(),e);
			}
		}
		return queryFavReturnByHotelId();
	}
	 

	private HtlFavourableReturn getFavourableReturnBean() {
		HtlFavourableReturn  favourableReturn =new HtlFavourableReturn();
		favourableReturn.setBeginDate(beginDate);
		favourableReturn.setEndDate(endDate);
		favourableReturn.setWeek("".equals(week)? pweeks : week);
		favourableReturn.setReturnScale(returnScale);
		return favourableReturn;
	}


	private HtlFavourableReturn getFavourableReturnEntity() {
		super.setEntity(super.populateEntity());
		if (null!=super.getEntity()){
			return (HtlFavourableReturn)super.getEntity();
		}
		return (HtlFavourableReturn)super.populateEntity();
	}


	/**
	 * 删除现金返还条款
	 * @return
	 */
	public String  delete(){
		returnService.removeFavReturn(favReturnID);
		return queryFavReturnByHotelId();
	}
	
	/**
	 * 进入修改返现页面 add by xiaojun.xiong 2010-9-14
	 */
    @SuppressWarnings("unchecked")
	public String view() {
    	//根据酒店ID得到酒店实体
		htlHotel = hotelManage.findHotel(hotelId);
		//根据酒店ID获取合同List;当有多个合同时，拿今天在合同开始日期和结束日期之间的合同
		Map<String,Object> contentMap=(Map<String, Object>) clauseManage.getContactContent(hotelId);
		lstRoomType=(List) contentMap.get("ROOM_TYPE_LIST");
		bDate=(Date) contentMap.get("BEGIN_DATE");
		eDate=(Date) contentMap.get("END_DATE");
    	super.setEntity(returnService.getFavReturnById(Long.parseLong(favReturnID)));
        return VIEW;
    }
    
    //批量设置
    @SuppressWarnings("unchecked")
	public String batchSearch(){
    	if(null==super.getOnlineRoleUser()||null==super.getOnlineRoleUser().getLoginName()){
    		return  super.forwardError("获取登陆用户信息失效,请重新登陆");
    	}
        returnService.batchCreateFavReturn(hotelIdStr,getFavourableReturnBean(),getOnlineRoleUser());
        return SUCCESS;
    }
    
    /**
     *点保存按钮的时候调用的dwr方法，拿数据判断所选房型在所选时间段内的预付、面付情况。
     * 
     * 
     */
   public List<HtlFavourableReturnInfo> checkPayMethod(long hotelId,String priceTypeItemStr,String payMethodStr,String beginDatesStr,String endDatesStr){
    	//把传进来的字符串拆分，放到相应list里面,封装到MAP中。
    	Map<String,List> convertMap=new HashMap<String,List>();
    	List<String> beginDateList=convertStringList(beginDatesStr);
    	convertMap.put("PRICE_TYPEI_DATE_LIS", convertStringList(priceTypeItemStr));
    	convertMap.put("BEGIN_DATE_LIST",beginDateList );
    	convertMap.put("END_DATE_LIST", convertStringList(endDatesStr));
    	convertMap.put("PAY_METHOD_LIST", convertStringList(payMethodStr));
    	//返回给页面用的List
    	List<HtlFavourableReturnInfo> htlFavourableReturnInfoLis =getFavReturnInfoList(hotelId,beginDateList.size(),convertMap);
		return htlFavourableReturnInfoLis;
    }
    
   @SuppressWarnings("unchecked")
   private List<HtlFavourableReturnInfo> getFavReturnInfoList(Long hotel_id,int size, Map map) {
	   List<String>priceTypeIdLis=(List<String>) map.get("PRICE_TYPEI_DATE_LIS");
	   List<HtlFavourableReturnInfo> htlFavourableReturnInfoLis =new ArrayList<HtlFavourableReturnInfo>();
	   for(String priceTypeIteStr:priceTypeIdLis){
		   long priceTypeIte= Long.parseLong(priceTypeIteStr);
		   List<HtlFavourableReturn> htlFavourableReturnLis= returnService.queryFavourableReturnForPriceTypeId(hotel_id,  priceTypeIte);
		   //价格类型名称
			String priceName = priceManage.getPriceTypeNameById(priceTypeIte);
			for(int n=0;n<size;n++){
				Date beginD = DateUtil.getDate((String)((List) map.get("BEGIN_DATE_LIST")).get(n));
				Date endD = DateUtil.getDate((String)((List) map.get("END_DATE_LIST")).get(n));
				//一个房型在相应时间段内的预付个数，为0，只能存面付立减；不为0，只能存预付立减
				long countPrepayNum = priceManage.getEleDayPriceNum(hotel_id,priceTypeIte,beginD,endD);
				long countPayNum = priceManage.getEleDayPayPriceNum(hotel_id, priceTypeIte, beginD,endD);
				if(!htlFavourableReturnLis.isEmpty()){
					for(int j=0;j<htlFavourableReturnLis.size();j++){
						HtlFavourableReturnInfo htlFavourableReturnInfo = convertFavReturn(hotel_id,priceTypeIte,priceName,countPrepayNum,countPayNum,n,map);
						HtlFavourableReturn htlFavourableReturnItem = htlFavourableReturnLis.get(j);
//						判断现金返还的支付类型
						if(1 == htlFavourableReturnItem.getPayMethod()){
							htlFavourableReturnInfo.setPayMethodName("面付");
						}else if(2 == htlFavourableReturnItem.getPayMethod()){
							htlFavourableReturnInfo.setPayMethodName("预付");
						}
						htlFavourableReturnInfoLis .add(htlFavourableReturnInfo);
					}
					
				}else{
					//如果之前没有现金返还，就不用去判断所存数据是否跟已有现金返还冲突。
					htlFavourableReturnInfoLis.add(convertFavReturn(hotel_id,priceTypeIte,priceName,countPrepayNum,countPayNum,n,map));
				}
			}
	   }
	   return htlFavourableReturnInfoLis;
	}



private HtlFavourableReturnInfo convertFavReturn(Long hotel_id,long priceTypeIte, String priceName, long countPrepayNum, long countPayNum, int n, Map<String, List> map) {
	String payMethod = (String)map.get("PAY_METHOD_LIST").get(n);
	HtlFavourableReturnInfo htlFavourableReturnInfo = new HtlFavourableReturnInfo();
	htlFavourableReturnInfo.setPriceTypeId(priceTypeIte);
	htlFavourableReturnInfo.setHotelId(hotel_id);
	htlFavourableReturnInfo.setPriceTypeName(priceName);
	String beginD= (String)map.get("BEGIN_DATE_LIST").get(n);;
	htlFavourableReturnInfo.setBeginDate(beginD);
	String endD= (String)map.get("END_DATE_LIST").get(n);;
	htlFavourableReturnInfo.setEndDate(endD);
	if(null!=payMethod && "1".equals(payMethod)&&0==countPayNum){
		//对应房型时间段内有预付价、页面提示不能保存面付返现
			htlFavourableReturnInfo.setPay(true);
	}
	if(null!=payMethod && "2".equals(payMethod)&&0==countPrepayNum){
		//对应房型时间段内没有、页面提示不能保存预付返现
			htlFavourableReturnInfo.setPryPay(true);
	}						
	return htlFavourableReturnInfo;
}



/**
    * 字符串拆分,拼装到LIST中
    * @param objectStr
    * @return
    */
	private List<String> convertStringList(String objectStr) {
		String objectStrId = objectStr.substring(0, objectStr.length()-1);
		List stringList=new ArrayList();
		if(objectStrId.indexOf(",")<0){
			stringList.add(objectStrId);
		}else{
			stringList=java.util.Arrays.asList(objectStrId.split(","));
		}
		return stringList;
	}


	public Long getHotelId() {
		return hotelId;
	}


	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}


	public HtlHotel getHtlHotel() {
		return htlHotel;
	}

	public void setHtlHotel(HtlHotel htlHotel) {
		this.htlHotel = htlHotel;
	}

	public HotelManage getHotelManage() {
		return hotelManage;
	}

	public void setHotelManage(HotelManage hotelManage) {
		this.hotelManage = hotelManage;
	}

	public ClauseManage getClauseManage() {
		return clauseManage;
	}


	public void setClauseManage(ClauseManage clauseManage) {
		this.clauseManage = clauseManage;
	}


	public List<HtlFavourableReturn> getFavReturnList() {
		return favReturnList;
	}


	public void setFavReturnList(List<HtlFavourableReturn> favReturnList) {
		this.favReturnList = favReturnList;
	}

	public HtlContract getHtlContractItems() {
		return htlContractItems;
	}

	public IPriceManage getPriceManage() {
		return priceManage;
	}

	public void setPriceManage(IPriceManage priceManage) {
		this.priceManage = priceManage;
	}

	public void setHtlContractItems(HtlContract htlContractItems) {
		this.htlContractItems = htlContractItems;
	}

	public Date getBDate() {
		return bDate;
	}

	public void setBDate(Date date) {
		bDate = date;
	}

	public Date getEDate() {
		return eDate;
	}

	public void setEDate(Date date) {
		eDate = date;
	}

	public List getLstRoomType() {
		return lstRoomType;
	}

	public void setLstRoomType(List lstRoomType) {
		this.lstRoomType = lstRoomType;
	}

	public int getOnlyNum() {
		return onlyNum;
	}

	public void setOnlyNum(int onlyNum) {
		this.onlyNum = onlyNum;
	}

	public List<HtlFavourableReturn> getHtlFavReturnLis() {
		return htlFavReturnLis;
	}

	public void setHtlFavReturnLis(List<HtlFavourableReturn> htlFavReturnLis) {
		this.htlFavReturnLis = htlFavReturnLis;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getWeek() {
		return week;
	}

	public Integer getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(Integer payMethod) {
		this.payMethod = payMethod;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getAddfor() {
		return addfor;
	}

	public void setAddfor(String addfor) {
		this.addfor = addfor;
	}

	public String getFavReturnID() {
		return favReturnID;
	}

	public void setFavReturnID(String favReturnID) {
		this.favReturnID = favReturnID;
	}
	
	//必须重写该方法,否则会引起后段程序出现空指针
	protected Class getEntityClass(){
		return HtlFavourableReturn.class;
	}

	public Double getReturnScale() {
		return returnScale;
	}

	public void setReturnScale(Double returnScale) {
		this.returnScale = returnScale;
	}

	public String getHotelIdStr() {
		return hotelIdStr;
	}

	public void setHotelIdStr(String hotelIdStr) {
		this.hotelIdStr = hotelIdStr;
	}

	public List<HtlContract> getHtlContractList() {
		return htlContractList;
	}

	public void setHtlContractList(List<HtlContract> htlContractList) {
		this.htlContractList = htlContractList;
	}

	public List getHtlHotelList() {
		return htlHotelList;
	}

	public void setHtlHotelList(List htlHotelList) {
		this.htlHotelList = htlHotelList;
	}

	public Map<Long, Date> getBDates() {
		return bDates;
	}

	public void setBDates(Map<Long, Date> dates) {
		bDates = dates;
	}

	public Map<Long, Date> getEDates() {
		return eDates;
	}

	public void setEDates(Map<Long, Date> dates) {
		eDates = dates;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public ContractManage getContractManage() {
		return contractManage;
	}

	public void setContractManage(ContractManage contractManage) {
		this.contractManage = contractManage;
	}

	public IHotelFavourableReturnService getReturnService() {
		return returnService;
	}

	public void setReturnService(IHotelFavourableReturnService returnService) {
		this.returnService = returnService;
	}	
}
