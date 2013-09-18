package com.mangocity.hotel.base.web;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.ClauseManage;
import com.mangocity.hotel.base.manage.ClauseTemplateByDateManage;
import com.mangocity.hotel.base.manage.IPriceManage;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlFavourableDecrease;
import com.mangocity.hotel.base.persistence.HtlPopedomControl;
import com.mangocity.hotel.base.persistence.HtlPreconcertInfo;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.MyBeanUtil;

public class FavourableDecreaseAction extends PersistenceAction {
	
	private Long id;
	private Long hotelId;
	private Long contractId;
	private Long priceTypeId;
	private Date beginDate;
	private Date endDate;
	//优惠立减list
	private List<HtlFavourableDecrease> favDecreaseList;	
	//一个酒店的所有价格类型
	private List<HtlRoomtype> lstRoomType;
	private String week;
	 // 在增加或修改优惠立减条款信息时传入合同开始日期
	private Date bDate;
	 // 在增加或修改优惠条款立减信息时传入合同的结束日期
	private Date eDate;	
	private String addfor ;	
	private String forward;
	private long decreasePrice;
	private List<HtlContract> htlContractLis;	
	private String favClaID;
	private String payMethod;
	private HtlContract htlContractItems;
	private String basePrice;
	//用来判断保存成功之后跳转到哪个页面
	private String fwStr;
	private int onlyNum;
     // 常量星期
    private final String pweeks = "1,2,3,4,5,6,7,";
	//用来存放从每天预付条款那出来的支付类型和ValidDate
    //TODO 字段名改改（因设计到界面的，不好改）
	private List<HtlPreconcertInfo> htlPreconcertInfo = new ArrayList<HtlPreconcertInfo>();
	private List<HtlFavourableDecrease> htlFavDecreaseLis ;
	//注入的service
	private ClauseManage clauseManage;
	private ClauseTemplateByDateManage clauseTemplateByDateManage;
	
	private IPriceManage priceManage;
	
	//常量
	private static  final String MESSAGE_NOLOGIN = "获取登陆用户信息失效,请重新登陆";
	private static final String FLAGADDFOR = "addFavourableDecrease";
	
	protected Class getEntityClass(){	
		return HtlFavourableDecrease.class;
	}
	
	/**
     * hotel 2.9.3 立减优惠  进入立减优惠列表页面 add zhijie.gu  2009-10-15 
     */
	public String  queryFavClauseById(){
		if(super.getOnlineRoleUser() == null || super.getOnlineRoleUser().getLoginName() == null){
			return  super.forwardError(MESSAGE_NOLOGIN);
		}
		String forw = "";
			List<HtlPopedomControl> htlPopedomControlLis = new  ArrayList<HtlPopedomControl>();
			String loginName = super.getOnlineRoleUser().getLoginName();
			boolean havePopedomControl = false;
			//TODO 提供一个根据loginName查询的方法，与皮政华商量
			htlPopedomControlLis = clauseManage.getPopedomListByLoginName(loginName);
			
			//权限判断
			if(!htlPopedomControlLis.isEmpty()){
				for(int i=0;i<htlPopedomControlLis.size();i++){
					HtlPopedomControl htlPopedomControlItems = (HtlPopedomControl)htlPopedomControlLis.get(i);
					if("1".equals(htlPopedomControlItems.getControlType())){
						favDecreaseList = clauseManage.queryAllavourableclause(hotelId);
						havePopedomControl = true;
						forw = "forwardToFavDecreaseLst";
					}	
				}	
			}
		return havePopedomControl == true ? forw : super.forwardError("你没有操作的权限!");
	}
	
	
	/**
     * 点击新增按钮，跳转到新增页面 add by zhijie.gu hotel2.9.3  2009-10-15 
     */
	public String create(){	
		//根据酒店ID获取合同List
		//TODO 提供个可以根据当前日期查的
		htlContractLis = clauseManage.searchContactByHTlID(hotelId);
		int count_contractnotInToday = 0;
		//当有多个合同时，拿今天在合同开始日期和结束日期之间的合同
		if(!htlContractLis.isEmpty()){
			for(int i =0; i<htlContractLis.size();i++){
				htlContractItems = (HtlContract)htlContractLis.get(i);
				Date tDate = new Date();
				if(tDate.after(htlContractItems.getBeginDate()) && tDate.before(htlContractItems.getEndDate())){
					bDate =htlContractItems.getBeginDate();
					eDate =htlContractItems.getEndDate();
					break;       
				}else{
					count_contractnotInToday ++;
				}
			}	
		}
		if( htlContractLis.isEmpty() || count_contractnotInToday == htlContractLis.size()){
			return super.forwardError("酒店合同不存在或酒店合同过期!");
		}
		lstRoomType=clauseManage.lstRoomTypeByHotelId(hotelId);
		addfor =FLAGADDFOR;
		request.setAttribute("bDate", bDate);
		request.setAttribute("eDate", eDate);
		return CREATE;	
	}
	
	
	
	/**
     *  新增或修改页面。点保存时调用的方法 add by zhijie.gu hotel2.9.3  2009-10-15 
     */
	public String edit(){
		if(super.getOnlineRoleUser()==null || super.getOnlineRoleUser().getLoginName()==null){
				return  super.forwardError(MESSAGE_NOLOGIN); 
		}	
		super.setEntity(super.populateEntity());
		Map params = super.getParams();
		//从页面获取的参数。
		htlFavDecreaseLis = MyBeanUtil.getBatchObjectFromParam(params,HtlFavourableDecrease.class, onlyNum);
		HtlFavourableDecrease htlFavourableDecrease = (HtlFavourableDecrease)super.getEntity();
		//把获取的价格类型/房型ID数组转换为字符串
		String[] priceTstr = null;
		if (FLAGADDFOR.equals(addfor)){
			if(null!=htlFavourableDecrease.getPriceTypeIdStr() && !"".equals(htlFavourableDecrease.getPriceTypeIdStr()) ){
				priceTstr = htlFavourableDecrease.getPriceTypeIdStr().split(",");
					for(int i=0;i<priceTstr.length;i++){
						for(int k=0;k<htlFavDecreaseLis.size();k++){
							HtlFavourableDecrease htlFavDecreaseItems = htlFavDecreaseLis.get(k);
							String priceName = priceManage.getPriceTypeNameById(Long.parseLong(priceTstr[i]));
							htlFavourableDecrease.setPriceTypeId(Long.parseLong(priceTstr[i]));
							htlFavourableDecrease.setPriceTypeName(priceName);
							setParamsForhtlFavourableDecrease(htlFavourableDecrease,htlFavDecreaseItems);
							try {
								clauseManage.createFavourableDecrease(htlFavourableDecrease);
							} catch (IllegalAccessException e) {
								log.error(e.getMessage(),e);
							} catch (InvocationTargetException e) {
								log.error(e.getMessage(),e);
							}
						}			
					}
			}
		}else{//修改
			HtlFavourableDecrease  oldFavDecrease = new HtlFavourableDecrease();
			oldFavDecrease =clauseManage.getFavDecreaseById(Long.parseLong(favClaID));
			if(null != oldFavDecrease){
				HtlFavourableDecrease  updFavDecrease =new HtlFavourableDecrease();
				updFavDecrease.setBeginDate(beginDate);
				updFavDecrease.setEndDate(endDate);
				updFavDecrease.setWeek(week);
				updFavDecrease.setDecreasePrice(decreasePrice);
				updFavDecrease.setPayMethod(oldFavDecrease.getPayMethod());
				updFavDecrease.setId(oldFavDecrease.getId());
				updFavDecrease.setPriceTypeId(oldFavDecrease.getPriceTypeId());
				updFavDecrease.setHotelId(oldFavDecrease.getHotelId());
				updFavDecrease.setCreateBy(oldFavDecrease.getCreateBy());
				updFavDecrease.setCreateById(oldFavDecrease.getCreateById());
				updFavDecrease.setCreateTime(oldFavDecrease.getCreateTime());
				updFavDecrease.setPriceTypeName(oldFavDecrease.getPriceTypeName());
				if(basePrice!=null && !"".equals(basePrice)){
					updFavDecrease.setBasePrice(Double.parseDouble(basePrice));
				}else{
					updFavDecrease.setBasePrice(0);
				}
				updFavDecrease.setModifyBy(super.getOnlineRoleUser().getName());
				updFavDecrease.setModifyById(super.getOnlineRoleUser().getLoginName());
				updFavDecrease.setModifyTime(DateUtil.getSystemDate());
				try {
					clauseManage.modifyFavClause(updFavDecrease);
				} catch (IllegalAccessException e) {
					log.error(e.getMessage(),e);
				} catch (InvocationTargetException e) {
					log.error(e.getMessage(),e);
				}	
			}
		}
		if("returnToClauseJsp".equals(fwStr)){
			return "reclause";
		}else{
			return queryFavClauseById();	
		}
	}
	
	/**
	 * 修改优惠立减条款 add by zhijie.gu 2009-10-15
	 */
    public String view() {	
    	htlContractLis = clauseManage.searchContactByHTlID(hotelId);
		int count_contractnotInToday = 0;
		//当有多个合同时，拿今天在合同开始日期和结束日期之间的合同
		if(!htlContractLis.isEmpty()){
			for(int i =0; i<htlContractLis.size();i++){
				htlContractItems = (HtlContract)htlContractLis.get(i);
				Date tDate = new Date();
				if(tDate.after(htlContractItems.getBeginDate()) && tDate.before(htlContractItems.getEndDate())){
					bDate =htlContractItems.getBeginDate();
					eDate =htlContractItems.getEndDate();
					break;       
				}else{
					count_contractnotInToday ++;
				}
			}	
		}
		if( htlContractLis.isEmpty() || count_contractnotInToday == htlContractLis.size()){
			return super.forwardError("酒店合同不存在或酒店合同过期!");
		}
		
    	HtlFavourableDecrease viewFavourableDecrease = new  HtlFavourableDecrease();
    	viewFavourableDecrease = clauseManage.getFavDecreaseById(Long.parseLong(favClaID));
    	super.setEntity(viewFavourableDecrease);
    	lstRoomType=clauseManage.lstRoomTypeByHotelId(hotelId);
        addfor = "editFavourableClause";
        request.setAttribute("bDate", bDate);
        request.setAttribute("eDate", eDate);
        return VIEW;
    }
    
    /**
     *点保存按钮的时候调用的dwr方法，拿数据判断所选房型在所选时间段内的预付、面付情况。 add by zhijie.gu hotel2.9.3  2009-10-15 
     * 
     * 
     */
   public List<HtlPreconcertInfo> checkPayMethod(long hotelId,String priceTypeItemStr,String payMethodStr,String beginDatesStr,String endDatesStr){
    	//返回给页面用的List
    	List htlPreconcertInfoLis = new ArrayList();
    	//把传进来的字符串拆分，放到相应list里面。
    	List priceTypeIdLis = new ArrayList();
    	List beginDList = new ArrayList();
    	List endDList = new ArrayList();
    	List payMethodList = new ArrayList();
		int roomTypeSize = priceTypeItemStr.length()-1;
		String beginDstr = beginDatesStr.substring(0,beginDatesStr.length()-1);
		String endDstr = endDatesStr.substring(0,endDatesStr.length()-1);
		String payMetnods = payMethodStr.substring(0,payMethodStr.length()-1);
		String priceTypeId = priceTypeItemStr.substring(0,roomTypeSize);
		if(priceTypeId.indexOf(",")<0){
			priceTypeIdLis.add(priceTypeId);
		}else{
			String[] priceTypeIdStr = priceTypeId.split(",");
			for(int k=0;k<priceTypeIdStr.length;k++){
				String priceTypeItem = priceTypeIdStr[k];
				priceTypeIdLis.add(priceTypeItem);
			}
		}
		//开始日期、结束日期、适用价格，他们是一一对应的，所以以开始日期作边界即可
		if(beginDstr.indexOf(",")<0){
			beginDList.add(beginDstr);
			endDList.add(endDstr);
			payMethodList.add(payMetnods);
		}else{
			String[] beginStr = beginDstr.split(",");
			String[] endStr = endDstr.split(",");
			String[] payMehodSr = payMetnods.split(",");
			for(int i=0;i<beginStr.length;i++){
				beginDList.add(beginStr[i]);
				endDList.add(endStr[i]);
				payMethodList.add(payMehodSr[i]);				
			}
		}
		
		for(int i=0;i<priceTypeIdLis.size();i++){
		
			for(int n=0;n<beginDList.size();n++){
				Date beginD = DateUtil.getDate((String)beginDList.get(n));
		    	Date endD = DateUtil.getDate((String)endDList.get(n));
		    	String payMethod = (String)payMethodList.get(n);
				long priceTypeIte= Long.parseLong((String) priceTypeIdLis.get(i));		
				//价格类型名称
				String priceName = priceManage.getPriceTypeNameById(priceTypeIte);
				//一个房型在相应时间段内的预付个数，为0，只能存面付立减；不为0，只能存预付立减
				long countPrepayNum = priceManage.getEleDayPriceNum(hotelId,priceTypeIte,beginD,endD);
				
				//根据酒店id、价格类型，拿已存在优惠立减
				List htlFavourableDecreaseLis= clauseManage.queryFavourableclauseForPriceTypeId(hotelId, priceTypeIte);
				if(!htlFavourableDecreaseLis.isEmpty()){
					for(int j=0;j<htlFavourableDecreaseLis.size();j++){
						HtlPreconcertInfo htlPreconcertInfo = new HtlPreconcertInfo();
						htlPreconcertInfo.setPriceTypeId(priceTypeIte);
						htlPreconcertInfo.setHotelId(hotelId);
						htlPreconcertInfo.setPriceTypeName(priceName);
						HtlFavourableDecrease htlFavourableDecreaseItem = (HtlFavourableDecrease)htlFavourableDecreaseLis.get(j);
						htlPreconcertInfo.setBeginDate(DateUtil.dateToString(beginD));
						htlPreconcertInfo.setEndDate(DateUtil.dateToString(endD));
						if(1 == htlFavourableDecreaseItem.getPayMethod()){
							htlPreconcertInfo.setPayMethodName("面付");	
						}else if(2 == htlFavourableDecreaseItem.getPayMethod()){
							htlPreconcertInfo.setPayMethodName("预付");
							
						}
						if(payMethod!=null && payMethod.equals("1")){
							//对应房型时间段内有预付价、页面提示不能保存面付立减
							if(countPrepayNum>0){
								htlPreconcertInfo.setHavePay(true);
							}
						}
						if(payMethod!=null && payMethod.equals("2")){
							//对应房型时间段内没有、页面提示不能保存预付立减
							if(countPrepayNum == 0){
								htlPreconcertInfo.setHavePrypay(true);
							}
						}
						//时间段内如果存在面付立减，则不能保存相应时间段内的预付立减
						if(!((beginD.before(htlFavourableDecreaseItem.getBeginDate()) && endD.before(htlFavourableDecreaseItem.getBeginDate())) ||
								(beginD.after(htlFavourableDecreaseItem.getEndDate()) && endD.after(htlFavourableDecreaseItem.getEndDate())))){
							if(null!=payMethod && !payMethod.equals(htlFavourableDecreaseItem.getPayMethod()+"")){
								htlPreconcertInfo.setCheckIsPayMethod(false);
							}
						}
						htlPreconcertInfoLis.add(htlPreconcertInfo);	
					}
				}else{
					//如果之前没有优惠立减，就不用去判断所存数据是否跟已有优惠立减冲突。
					HtlPreconcertInfo htlPreconcertInfo = new HtlPreconcertInfo();
					htlPreconcertInfo.setPriceTypeId(priceTypeIte);
					htlPreconcertInfo.setHotelId(hotelId);
					htlPreconcertInfo.setPriceTypeName(priceName);
					htlPreconcertInfo.setBeginDate(DateUtil.dateToString(beginD));
					htlPreconcertInfo.setEndDate(DateUtil.dateToString(endD));
					
					if(payMethod!=null && payMethod.equals("1")){
						if(countPrepayNum>0){
							htlPreconcertInfo.setHavePay(true);
						}
					}
					if(payMethod!=null && payMethod.equals("2")){	
						if(countPrepayNum == 0){
							htlPreconcertInfo.setHavePrypay(true);
						}
					}
					htlPreconcertInfoLis.add(htlPreconcertInfo);		
				}
			}	
		}	
		return htlPreconcertInfoLis;
    }
    
   
   /**
	 * 删除立减优惠条款
	 * @return
	 */
	public String  delete(){	
		HtlFavourableDecrease delFavObj = clauseManage.getFavDecreaseById(Long.parseLong(favClaID));
		if(null !=delFavObj){
			clauseManage.deleteFavDecreaseObj(delFavObj);
		}		
		return queryFavClauseById();
	}
	
	/**
	 * 给新增的HtlFavourableDecrease赋值
	 * @param htlFavourableDecrease
	 * @param htlFavDecreaseItems  
	 */
	private void setParamsForhtlFavourableDecrease(HtlFavourableDecrease htlFavourableDecrease,HtlFavourableDecrease htlFavDecreaseItems){
		htlFavourableDecrease.setBeginDate(htlFavDecreaseItems.getBeginDate());
		htlFavourableDecrease.setEndDate(htlFavDecreaseItems.getEndDate());
		htlFavourableDecrease.setDecreasePrice(htlFavDecreaseItems.getDecreasePrice());
		htlFavourableDecrease.setHotelId(hotelId);
		htlFavourableDecrease.setPayMethod(htlFavDecreaseItems.getPayMethod());
		if(0 != htlFavDecreaseItems.getBasePrice()){
			htlFavourableDecrease.setBasePrice(htlFavDecreaseItems.getBasePrice());
		}
		if(null == htlFavDecreaseItems.getWeek()){
			htlFavourableDecrease.setWeek(pweeks);
		}else{
			htlFavourableDecrease.setWeek(htlFavDecreaseItems.getWeek());		
		}
		htlFavourableDecrease.setCreateBy(super.getOnlineRoleUser().getName());
		htlFavourableDecrease.setCreateById(super.getOnlineRoleUser().getLoginName());
		htlFavourableDecrease.setCreateTime(DateUtil.getSystemDate());
		htlFavourableDecrease.setModifyBy(super.getOnlineRoleUser().getName());
		htlFavourableDecrease.setModifyById(super.getOnlineRoleUser().getLoginName());
		htlFavourableDecrease.setModifyTime(DateUtil.getSystemDate());
	}
	
	
	public Long getHotelId() {
		return hotelId;
	}


	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public Long getPriceTypeId() {
		return priceTypeId;
	}

	public void setPriceTypeId(Long priceTypeId) {
		this.priceTypeId = priceTypeId;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List getFavDecreaseList() {
		return favDecreaseList;
	}

	public void setFavDecreaseList(List favDecreaseList) {
		this.favDecreaseList = favDecreaseList;
	}

	public List getLstRoomType() {
		return lstRoomType;
	}

	public void setLstRoomType(List lstRoomType) {
		this.lstRoomType = lstRoomType;
	}

	public String getAddfor() {
		return addfor;
	}

	public void setAddfor(String addfor) {
		this.addfor = addfor;
	}

	public String getForward() {
		return forward;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	public List getHtlContractLis() {
		return htlContractLis;
	}

	public void setHtlContractLis(List htlContractLis) {
		this.htlContractLis = htlContractLis;
	}

	public HtlContract getHtlContractItems() {
		return htlContractItems;
	}

	public void setHtlContractItems(HtlContract htlContractItems) {
		this.htlContractItems = htlContractItems;
	}

	public ClauseTemplateByDateManage getClauseTemplateByDateManage() {
		return clauseTemplateByDateManage;
	}

	public void setClauseTemplateByDateManage(
			ClauseTemplateByDateManage clauseTemplateByDateManage) {
		this.clauseTemplateByDateManage = clauseTemplateByDateManage;
	}

	public List getHtlPreconcertInfo() {
		return htlPreconcertInfo;
	}

	public void setHtlPreconcertInfo(List htlPreconcertInfo) {
		this.htlPreconcertInfo = htlPreconcertInfo;
	}

	public long getDecreasePrice() {
		return decreasePrice;
	}

	public void setDecreasePrice(long decreasePrice) {
		this.decreasePrice = decreasePrice;
	}

	public String getFavClaID() {
		return favClaID;
	}

	public void setFavClaID(String favClaID) {
		this.favClaID = favClaID;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(String basePrice) {
		this.basePrice = basePrice;
	}

	public String getFwStr() {
		return fwStr;
	}

	public void setFwStr(String fwStr) {
		this.fwStr = fwStr;
	}

	public int getOnlyNum() {
		return onlyNum;
	}

	public void setOnlyNum(int onlyNum) {
		this.onlyNum = onlyNum;
	}

    public ClauseManage getClauseManage() {
        return clauseManage;
    }

    public void setClauseManage(ClauseManage clauseManage) {
        this.clauseManage = clauseManage;
    }
    public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public void setPriceManage(IPriceManage priceManage) {
		this.priceManage = priceManage;
	}

	
}
