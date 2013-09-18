package com.mangocity.hotel.base.web;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.mangocity.hotel.base.constant.FavourableTypeUtil;
import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlEveningsRent;
import com.mangocity.hotel.base.persistence.HtlFavouraParameter;
import com.mangocity.hotel.base.persistence.HtlFavourableclause;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.MyBeanUtil;

public class FavourableClauseAction extends PersistenceAction {
	
	//Id
	private Long id;
	
	//酒店ID
	private Long hotelId;
	
	private String hotelChnName;
	
	// 合同ID
	private Long contractId;

	// 价格类型ID
	private Long priceTypeId;

	// 价格类型名称
	private String priceTypeName;

	// 优惠类型
	private String favourableType;

	// 开始日期
	private Date beginDate;

	// 结束日期
	private Date endDate;

	// 随机数
	private Long randomNumber;
	
	//用于在优惠条款页面单击返回按钮的时候是否返回到合同修改页面
	private String contractEditDetail;
	
	/***
	 * 优惠参数实体类  add by shengwei.zuo 2009-08-27 begin
	 */
	//住几晚
	private Long continueNight;
	
	//送几晚
	private Long donateNight;
	
	//循环类型
	private Long circulateType;
	
	//折扣
	private Double discount;
	
	//小数点类型
	private Long decimalPointType;
	
	//包价售价
	private Double packagerateSaleprice;
	
	//包价佣金
	private Double packagerateCommission;
	
	//包价晚数
	private Long packagerateNight;
	
	/***
	 * 优惠参数实体类  add by shengwei.zuo 2009-08-27 end
	 */

	
	/**
	 * hotel2.9.3 连住N晚送M晚的各晚房费实体类 add by shengwei.zuo 2009-08-25 begin;
	 */
	
	// 第几晚
	private Long night;

	// 售价
	private Double salePrice;

	// 佣金
	private Double commission;
	
	/**
	 * hotel2.9.3 连住N晚送M晚的各晚房费实体类 add by shengwei.zuo 2009-08-25 end;
	 */
	
	
	private String addfor;
	
	private String  forward;
	
	private int eveningsRentNum;
	
	/**
	 * 在增加或修改优惠条款信息时传入合同开始日期
	 */
	private Date bDate;
	
	/**
	 * 在增加或修改优惠条款信息时传入合同的结束日期
	 */
	private Date eDate;
	
	
	private String  start;
	
	private String  end;
	
	private  String  favClaID;
	
	//Id的字符串
	private String favIdStr;
	
	//价格类型ID字符串
	private String priceTypeIdStr;
	
	private HotelManage hotelManage;
	
	private HotelRoomTypeService hotelRoomTypeService;
	
	private ContractManage contractManage;
	
	private List lstRoomType = new ArrayList();
	
	private String roomTypes;
	
	private List lisEveningsRent  = new ArrayList();
	
	private List  favClauseList;
	
	
	protected Class getEntityClass(){
		return HtlFavourableclause.class;
	}

	//hotel 2.9.3 连住优惠 合同页面点击 设定优惠条款 进入优惠条款列表页面 add shengwei.zuo 2009-09-13 
	public String  queryFavClauseById(){
		
		favClauseList=contractManage.queryAllavourableclause(contractId);
		
		hotelChnName = hotelManage.findHotel(hotelId).getChnName();
		
		return "forwardToFavClauseLst";
	}
	
	//点击新增按钮，跳转到新增页面 add by shengwei.zuo hotel2.9.3 
	public String create(){
		
		lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId);

		addfor ="addFavourableClause";
		
		forward = "create";
			
		return forward;
		
	}
	
	//保存操作时的方法.add by shengwei.zuo hotel2.9.3 2009-08-26 
	public String edit(){
		
		super.setEntity(super.populateEntity());
		HtlFavourableclause htlFavourableclause;
		if (super.getEntity()!=null){
			htlFavourableclause=(HtlFavourableclause)super.getEntity();
		}else{
			htlFavourableclause =(HtlFavourableclause)super.populateEntity();
		}
		
		//把获取的价格类型ID数组转换为字符串
		String[] str = null ;

		if ("addFavourableClause".equals(addfor)){
				
				if (roomTypes!=null){
					
					str = roomTypes.split(",");
					//随机数
					Random random = new Random(System.currentTimeMillis());
					randomNumber = random.nextLong();
					
					for(int i=0;i<str.length;i++){
						
						String  priceName =  new  String();
						priceName = contractManage.priceTypeName(Long.parseLong(str[i]));
						
						//优惠条款总表实体类的各个属性赋值
						htlFavourableclause.setBeginDate(DateUtil.getDate(start));
						htlFavourableclause.setEndDate(DateUtil.getDate(end));
					
						
						htlFavourableclause.setPriceTypeId(Long.parseLong(str[i]));
						htlFavourableclause.setPriceTypeName(priceName);
						htlFavourableclause.setHotelId(hotelId);
						htlFavourableclause.setContractId(contractId);
						htlFavourableclause.setFavourableType(favourableType);
						htlFavourableclause.setRandomNumber(randomNumber);
						
						
						//优惠条款的参数实体类的属性赋值;add by shengwei.zuo  2009-08-27
						
						
						//连住N晚送M晚
						if(favourableType.equals(FavourableTypeUtil.continueDonate)||favourableType==FavourableTypeUtil.continueDonate){
							
							 List contDonaLst= new ArrayList();
							 HtlFavouraParameter  contDonaEntity = new HtlFavouraParameter();
							
							 //连住N晚送M晚的参数赋值
							 contDonaEntity.setCirculateType(circulateType);
							 contDonaEntity.setContinueNight(continueNight);
							 contDonaEntity.setDonateNight(donateNight);
							 contDonaEntity.setFavourableType(favourableType);
							 contDonaEntity.setFavourableclauseEntiy(htlFavourableclause);
							 
							 Map params = super.getParams();
							//连住N晚送M晚，每晚房费的实体类
							lisEveningsRent = MyBeanUtil.getBatchObjectFromParam(params, HtlEveningsRent.class, eveningsRentNum);
							 
							 //各晚房费相应参数设置
							 List eveningsRentEntityLst = new ArrayList();
							 
							 for(int z=0;z<lisEveningsRent.size();z++){
								 HtlEveningsRent  eveningsRentEntity  = new  HtlEveningsRent();
								 HtlEveningsRent eveningsRentObj =  new HtlEveningsRent();
								 eveningsRentObj =(HtlEveningsRent)lisEveningsRent.get(z);
								 
								 eveningsRentEntity.setNight(eveningsRentObj.getNight());
								 eveningsRentEntity.setSalePrice(eveningsRentObj.getSalePrice());
								 eveningsRentEntity.setCommission(eveningsRentObj.getCommission());
								 eveningsRentEntity.setFavouraParameterEntiy(contDonaEntity);
								 
								 eveningsRentEntityLst.add(eveningsRentEntity);
								 
							 }
							 contDonaEntity.setLstEveningsRent(eveningsRentEntityLst);
							 
							 contDonaLst.add(contDonaEntity);
							 htlFavourableclause.setLstPackagerate(contDonaLst);

							
						//原价打折
						}else if(favourableType.equals(FavourableTypeUtil.discount)||favourableType==FavourableTypeUtil.discount){
							
							 List discountLst= new ArrayList();
							 HtlFavouraParameter  discountEntity = new HtlFavouraParameter();
							
							 discountEntity.setDiscount(discount);
							 discountEntity.setDecimalPointType(decimalPointType);
							 discountEntity.setFavourableType(favourableType);
							 discountEntity.setFavourableclauseEntiy(htlFavourableclause);
							
							 discountLst.add(discountEntity);
							 htlFavourableclause.setLstPackagerate(discountLst);
						//连住包价	
						}else if(favourableType.equals(FavourableTypeUtil.packagerate)||favourableType==FavourableTypeUtil.packagerate){
							
							 List packagerateLst= new ArrayList();
							 HtlFavouraParameter  packagerateEntity = new HtlFavouraParameter();
							
							 packagerateEntity.setPackagerateNight(packagerateNight);
							 packagerateEntity.setPackagerateCommission(packagerateCommission);
							 packagerateEntity.setPackagerateSaleprice(packagerateSaleprice);
							 packagerateEntity.setFavourableType(favourableType);
							 packagerateEntity.setFavourableclauseEntiy(htlFavourableclause);
							
							 packagerateLst.add(packagerateEntity);
							 htlFavourableclause.setLstPackagerate(packagerateLst);
						}
						
						if(super.getOnlineRoleUser()!=null && super.getOnlineRoleUser().getLoginName()!=null){
							htlFavourableclause.setCreateBy(super.getOnlineRoleUser().getName());
							htlFavourableclause.setCreateById(super.getOnlineRoleUser().getLoginName());
							htlFavourableclause.setCreateTime(DateUtil.getSystemDate());
							htlFavourableclause.setModifyBy(super.getOnlineRoleUser().getName());
							htlFavourableclause.setModifyById(super.getOnlineRoleUser().getLoginName());
							htlFavourableclause.setModifyTime(DateUtil.getSystemDate());
						}else{
							return  super.forwardError("获取登陆用户信息失效,请重新登陆");
						}
						
						try {
							contractManage.createFavourableclause(htlFavourableclause);
						} catch (IllegalAccessException e) {
							log.error(e.getMessage(),e);
						} catch (InvocationTargetException e) {
							log.error(e.getMessage(),e);
						}
					}
				}
			
		}else {//修改的操作
			
			//优惠条款ID的字符串
			str = favIdStr.split(",");
			
			for(int i=0;i<str.length;i++){
				
				Long favID=Long.parseLong(str[i]);
				
				HtlFavourableclause uptFavObj =  new HtlFavourableclause();
				
				//根据优惠条款ID得到优惠条款的信息
				uptFavObj = contractManage.getFavClauseById(favID);
				
				HtlFavourableclause uptFavEntity = new HtlFavourableclause();
				
				uptFavEntity.setBeginDate(beginDate);
				uptFavEntity.setEndDate(endDate);
				
				uptFavEntity.setContractId(uptFavObj.getContractId());
				uptFavEntity.setCreateBy(uptFavObj.getCreateBy());
				uptFavEntity.setCreateById(uptFavObj.getCreateById());
				uptFavEntity.setCreateTime(uptFavObj.getCreateTime());
				
				uptFavEntity.setFavourableType(uptFavObj.getFavourableType());
				uptFavEntity.setHotelId(uptFavObj.getHotelId());
				uptFavEntity.setId(uptFavObj.getId());
				
				uptFavEntity.setPriceTypeId(uptFavObj.getPriceTypeId());
				uptFavEntity.setPriceTypeName(uptFavObj.getPriceTypeName());
				uptFavEntity.setRandomNumber(uptFavObj.getRandomNumber());
				
				
				
				//优惠条款参数实体类
				List lstFavParmObj = new ArrayList();
				lstFavParmObj=uptFavObj.getLstPackagerate();
				
				if(!lstFavParmObj.isEmpty()){
					
					HtlFavouraParameter favouraParEntity = null;
					List  lstFavParEntity = null;
					
					HtlFavouraParameter favouraParObj = (HtlFavouraParameter)lstFavParmObj.get(0);
					
					if(uptFavObj.getFavourableType().equals(FavourableTypeUtil.continueDonate)||FavourableTypeUtil.continueDonate==uptFavObj.getFavourableType()){
					//连住N晚送M晚
						favouraParEntity = new  HtlFavouraParameter();
						lstFavParEntity = new ArrayList();
						
						favouraParEntity.setContinueNight(continueNight);
						favouraParEntity.setDonateNight(donateNight);
						favouraParEntity.setCirculateType(circulateType);
						favouraParEntity.setFavourableType(favouraParObj.getFavourableType());
						favouraParEntity.setId(favouraParObj.getId());
						favouraParEntity.setFavourableclauseEntiy(uptFavEntity);
						
						Map params = super.getParams();
							//连住N晚送M晚，每晚房费的实体类
						lisEveningsRent = MyBeanUtil.getBatchObjectFromParam(params, HtlEveningsRent.class, eveningsRentNum);
							 
						
						List lstEveningsRentEntity = new ArrayList();
						
						for(int j=0;j<lisEveningsRent.size();j++){
							HtlEveningsRent  eveningsRentObj = (HtlEveningsRent)lisEveningsRent.get(j);
							HtlEveningsRent  eveningsRentEntity = new  HtlEveningsRent();
							eveningsRentEntity.setNight(eveningsRentObj.getNight());
							eveningsRentEntity.setSalePrice(eveningsRentObj.getSalePrice());
							eveningsRentEntity.setCommission(eveningsRentObj.getCommission());
							eveningsRentEntity.setFavouraParameterEntiy(favouraParEntity);
							
							lstEveningsRentEntity.add(eveningsRentEntity);
							
						}
						
						favouraParEntity.setLstEveningsRent(lstEveningsRentEntity);
						
						lstFavParEntity.add(favouraParEntity);
						uptFavEntity.setLstPackagerate(lstFavParEntity);
						
					}else if(uptFavObj.getFavourableType().equals(FavourableTypeUtil.discount)||FavourableTypeUtil.discount==uptFavObj.getFavourableType()){
					//原价打折	
						favouraParEntity = new  HtlFavouraParameter();
						lstFavParEntity = new ArrayList();
						
						favouraParEntity.setDiscount(discount);
						favouraParEntity.setDecimalPointType(decimalPointType);
						favouraParEntity.setFavourableType(favouraParObj.getFavourableType());
						favouraParEntity.setFavourableclauseEntiy(uptFavEntity);
						
						lstFavParEntity.add(favouraParEntity);
						uptFavEntity.setLstPackagerate(lstFavParEntity);
						
					}else if(uptFavObj.getFavourableType().equals(FavourableTypeUtil.packagerate)||FavourableTypeUtil.packagerate==uptFavObj.getFavourableType()){
					//连住包价	
						favouraParEntity = new  HtlFavouraParameter();
						lstFavParEntity = new ArrayList();
						
						favouraParEntity.setPackagerateCommission(packagerateCommission);
						favouraParEntity.setPackagerateNight(packagerateNight);
						favouraParEntity.setPackagerateSaleprice(packagerateSaleprice);
						favouraParEntity.setFavourableType(favouraParObj.getFavourableType());
						favouraParEntity.setFavourableclauseEntiy(uptFavEntity);
						
						lstFavParEntity.add(favouraParEntity);
						uptFavEntity.setLstPackagerate(lstFavParEntity);	
						
					}
					
				}
				
				
				
				if(super.getOnlineRoleUser()!=null && super.getOnlineRoleUser().getLoginName()!=null){
					uptFavEntity.setModifyBy(super.getOnlineRoleUser().getName());
					uptFavEntity.setModifyById(super.getOnlineRoleUser().getLoginName());
					uptFavEntity.setModifyTime(DateUtil.getSystemDate());
				}else{
					return  super.forwardError("获取登陆用户信息失效,请重新登陆");
				}
 				
		       try {
				contractManage.modifyFavClause(uptFavEntity);
			} catch (IllegalAccessException e) {
				log.error(e.getMessage(),e);
			} catch (InvocationTargetException e) {
				log.error(e.getMessage(),e);
			}
		           
				
			}

        }

		
		
		return queryFavClauseById();
	}

	/**
	 * 删除优惠条款
	 * @return
	 */
	public String  delete(){
		
		String[] favIdArr = favIdStr.split(",");
			
		for(int i=0;i<favIdArr.length;i++){
				
			Long favID=Long.parseLong(favIdArr[i]);
				
			HtlFavourableclause delFavObj = contractManage.getFavClauseById(favID);
				
			contractManage.deleteFavClauseObj(delFavObj);
				
		}
				
		
		
		return queryFavClauseById();
	}
	
	/**
	 * 修改优惠条款 add by shengwei.zuo 2009-08-31
	 */
    public String view() {

    	HtlFavourableclause viewFavourableclause = new  HtlFavourableclause();
    	
    	viewFavourableclause = contractManage.getFavClauseById(Long.parseLong(favClaID));
    	
    	viewFavourableclause.setFavClauseIdStr(favIdStr);
    	
    	viewFavourableclause.setPriceTypeIdStr(priceTypeIdStr);
    	
    	if(viewFavourableclause.getFavourableType().equals(FavourableTypeUtil.continueDonate)||FavourableTypeUtil.continueDonate==viewFavourableclause.getFavourableType()){
    		
    		List lstParmView = viewFavourableclause.getLstPackagerate();
    		if(!lstParmView.isEmpty()){
    			
    			HtlFavouraParameter favParamView = (HtlFavouraParameter)lstParmView.get(0);
    			List lstEvenView = favParamView.getLstEveningsRent();
    			eveningsRentNum=lstEvenView.size();
    		}
    		
    	}

    	super.setEntity(viewFavourableclause);

        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId);
        
        addfor = "editFavourableClause";

        return VIEW;
    }
	
	public Long getHotelId() {
		return hotelId;
	}


	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}




	public HotelManage getHotelManage() {
		return hotelManage;
	}




	public void setHotelManage(HotelManage hotelManage) {
		this.hotelManage = hotelManage;
	}




	public ContractManage getContractManage() {
		return contractManage;
	}




	public void setContractManage(ContractManage contractManage) {
		this.contractManage = contractManage;
	}




	public List getLstRoomType() {
		return lstRoomType;
	}




	public void setLstRoomType(List lstRoomType) {
		this.lstRoomType = lstRoomType;
	}




	public String getRoomTypes() {
		return roomTypes;
	}




	public void setRoomTypes(String roomTypes) {
		this.roomTypes = roomTypes;
	}




	public String getForward() {
		return forward;
	}




	public void setForward(String forward) {
		this.forward = forward;
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




	public String getPriceTypeName() {
		return priceTypeName;
	}




	public void setPriceTypeName(String priceTypeName) {
		this.priceTypeName = priceTypeName;
	}




	public String getFavourableType() {
		return favourableType;
	}




	public void setFavourableType(String favourableType) {
		this.favourableType = favourableType;
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


	public Long getRandomNumber() {
		return randomNumber;
	}


	public void setRandomNumber(Long randomNumber) {
		this.randomNumber = randomNumber;
	}


	public String getAddfor() {
		return addfor;
	}




	public void setAddfor(String addfor) {
		this.addfor = addfor;
	}


	public String getStart() {
		return start;
	}




	public void setStart(String start) {
		this.start = start;
	}




	public String getEnd() {
		return end;
	}




	public void setEnd(String end) {
		this.end = end;
	}




	public Long getId() {
		return id;
	}




	public void setId(Long id) {
		this.id = id;
	}

	public int getEveningsRentNum() {
		return eveningsRentNum;
	}

	public void setEveningsRentNum(int eveningsRentNum) {
		this.eveningsRentNum = eveningsRentNum;
	}

	public Long getContinueNight() {
		return continueNight;
	}

	public void setContinueNight(Long continueNight) {
		this.continueNight = continueNight;
	}

	public Long getDonateNight() {
		return donateNight;
	}

	public void setDonateNight(Long donateNight) {
		this.donateNight = donateNight;
	}

	public Long getCirculateType() {
		return circulateType;
	}

	public void setCirculateType(Long circulateType) {
		this.circulateType = circulateType;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Long getDecimalPointType() {
		return decimalPointType;
	}

	public void setDecimalPointType(Long decimalPointType) {
		this.decimalPointType = decimalPointType;
	}

	public Double getPackagerateSaleprice() {
		return packagerateSaleprice;
	}

	public void setPackagerateSaleprice(Double packagerateSaleprice) {
		this.packagerateSaleprice = packagerateSaleprice;
	}

	public Double getPackagerateCommission() {
		return packagerateCommission;
	}

	public void setPackagerateCommission(Double packagerateCommission) {
		this.packagerateCommission = packagerateCommission;
	}

	public Long getPackagerateNight() {
		return packagerateNight;
	}

	public void setPackagerateNight(Long packagerateNight) {
		this.packagerateNight = packagerateNight;
	}

	public Long getNight() {
		return night;
	}

	public void setNight(Long night) {
		this.night = night;
	}

	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	public Double getCommission() {
		return commission;
	}

	public void setCommission(Double commission) {
		this.commission = commission;
	}


	public String getFavIdStr() {
		return favIdStr;
	}


	public void setFavIdStr(String favIdStr) {
		this.favIdStr = favIdStr;
	}


	public String getPriceTypeIdStr() {
		return priceTypeIdStr;
	}


	public void setPriceTypeIdStr(String priceTypeIdStr) {
		this.priceTypeIdStr = priceTypeIdStr;
	}


	public List getLisEveningsRent() {
		return lisEveningsRent;
	}


	public void setLisEveningsRent(List lisEveningsRent) {
		this.lisEveningsRent = lisEveningsRent;
	}
	
	
	
	public List getFavClauseList() {
		return favClauseList;
	}


	public void setFavClauseList(List favClauseList) {
		this.favClauseList = favClauseList;
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


	public String getFavClaID() {
		return favClaID;
	}

	public void setFavClaID(String favClaID) {
		this.favClaID = favClaID;
	}

	public String getContractEditDetail() {
		return contractEditDetail;
	}

	public void setContractEditDetail(String contractEditDetail) {
		this.contractEditDetail = contractEditDetail;
	}

	public String getHotelChnName() {
		return hotelChnName;
	}

	public void setHotelChnName(String hotelChnName) {
		this.hotelChnName = hotelChnName;
	}

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}
	
}
