package com.mangocity.hotel.base.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.manage.ContractService;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.ContractFile;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlInternet;
import com.mangocity.hotel.base.persistence.HtlTaxCharge;
import com.mangocity.hotel.base.util.SendLuceneMQ;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.ContractContinue;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.bean.SingleContractContinue;

public class ContractAction extends PersistenceAction{
	
	private ContractService contractService;
	
	private SendLuceneMQ sendLuceneMQ;
	/**
	 * 延长合同至
	 */	
	private Date continueDate;
	
	/**
	 * 缩短合同至
	 */
	private Date cutDate;
    
	private Long contractId;
	
	private int rowNum;
	
	private ContractManage contractManage;
	
	private HotelManage hotelManage;
	
	private int lsTaxChargeNum;
	
	private List lsHtlTaxCharge;
	
	private String alerttypeInfoIDs;
	
    private int lsInternetNum;
	
	private List lsHtlInternet;
	
	private List  htlAlerttypeInfoLst;
	
	private String forward;
	
	private long creditAssureID;
	
	private long alerttypeInfoID;
	
	/**
	 * hotel 2.9.3 优惠条款 add by shengwei.zuo 2009-08-31 begin
	 */
	
	//优惠条款ID字符串
	private String favClauseID;
	
	//优惠条款ID
	private Long  favID;
	
	//价格类型ID字符串
	private String priTpIdStr;
	
	/**
	 * hotel 2.9.3 优惠条款 add by shengwei.zuo 2009-08-31 end
	 */
	
	/**
	 * 酒店id
	 */
	private long hotelId;
	/**
	 * 在缩短合同的时候传入合同开始日期
	 */
	private String bDate;
	
	/**
	 * 在缩短合同的时候传入合同的结束日期
	 */
	private String eDate;
	
	private String srcBeginDate;
	private String srcEndDate;

	private static final int BUFFER_SIZE = 16 * 1024;
	
	
	/*
	 * 在上传的服务上的文件名
	 */
	private String serverFileName;
	
	/*
	 * 在本地的完整路径
	 */
	private String fileName;
	
	/*
	 * 在服务器上的完整路径
	 */
	private File serverFile;
	
	/*
	 * 在本地的完整路径,是file类型
	 */	
	private File fileUpload;
	
	/*
	 * 在服务器上相对路径，访问该文件要用
	 */
	private String relaPath;
	
	private ContractFile contractFile;
	
	private List contractFileList;
	
	//酒店中文名
	private String hotelCnName;
	
	private List htlOpenCloseRoomLis;
	
	private List roomTypePriceTypeLis;
	
	private int secondRowNum;
	/**
	 * 
	 */
	private static final long serialVersionUID = -2282309240480807081L;
	
	
	//房态改造，从房态页面跳到合同修改页面，保存或取消操作时，关闭IE；
    private String closeIeOrnot;
	
	protected Class getEntityClass(){
		return HtlContract.class;
	}
	
	
	public String create(){
		String create = super.create();			 
		HtlHotel htlHotel = hotelManage.findHotel(hotelId);		 
		hotelCnName = htlHotel.getChnName();			 
		return create;
	}
	
	/**
	 * 2008-03-28 添加保存并返回合同按钮
	 * @return
	 */
	
	public String saveAndContract() {
		HtlHotel htlHotel = hotelManage.findHotel(hotelId);		 
		hotelCnName = htlHotel.getChnName();	
		if("saveAndContract".equals(forward)){
			return "saveAndContract";
		}
		return CREATE;
	}	
	
	
	public String view() {
		super.view();
		 
		HtlContract  htlContract = (HtlContract)super.getEntity();
		 
		lsHtlTaxCharge = htlContract.getHtlTaxCharge();
		 
		lsTaxChargeNum = lsHtlTaxCharge.size();
		 
		lsHtlInternet = htlContract.getHtlInternet();
		 
		lsInternetNum = lsHtlInternet.size();
		 
		/** 检查酒店合同是否锁定 add by chenjiajie V2.4 2008-8-26 Begin* */
		Map params = super.getParams();
		//String lockedParams = (String) params.get("locked");
		// 不是从点击"保存"进入的
		String lockedMSG = contractService.checkHasLocked(htlContract,super.getOnlineRoleUser(),hotelManage.findHotel(htlContract.getHotelId()));
		//已锁
		if(StringUtil.isValidStr(lockedMSG)){
			request.setAttribute("lockedMSG", lockedMSG);
			return "lockedHint";
		}
		contractFile = new ContractFile();
		contractId = super.getEntityID();
		contractFile.setContractId(contractId);
		contractFileList = contractManage.queryContractFileList(contractFile); 
		htlOpenCloseRoomLis = hotelManage.queryCloseRoom(htlContract.getHotelId());
		 
		//hote2.9.2 提示信息 add by shengwei.zuo 2009-08-16
		//htlAlerttypeInfoLst= contractManage.queryAlertInfoByConId(contractId);
		htlAlerttypeInfoLst= contractManage.queryAlertInfoByConId(contractId,true);
		 
		return VIEW;
	}
	
	public String contractView() {
		 super.view();
		 
		 HtlContract  htlContract = (HtlContract)super.getEntity();
		 
		 lsHtlTaxCharge = htlContract.getHtlTaxCharge();
		 
		 lsTaxChargeNum = lsHtlTaxCharge.size();
		 
		 lsHtlInternet = htlContract.getHtlInternet();
		 
		 lsInternetNum = lsHtlInternet.size();
		 
		 contractFile = new ContractFile();
		 
		 contractId = super.getEntityID();
		 		 
		 contractFile.setContractId(contractId);
		
		 contractFileList = contractManage.queryContractFileList(contractFile); 
		 htlOpenCloseRoomLis = hotelManage.queryCloseRoom(htlContract.getHotelId());
		 //hote2.9.2 提示信息 add by shengwei.zuo 2009-08-16
		 htlAlerttypeInfoLst= contractManage.queryAlertInfoByConId(contractFile.getContractId());
		 
		
		return "contractView";
	}
	
	public String edit() {
		super.setEntity(super.populateEntity());
		HtlContract contract= (HtlContract)super.getEntity();
		Map params = super.getParams();
		/*
		 * 修改合同时同步更新酒店基本信息表的：提示信息、其它联系事项、提示信息
		 * 修改人：黄鑫洋
		 */
		HtlHotel htlHotel = hotelManage.findHotel(hotelId);
		//同步合同信息到基本信息表中
		hotelManage.modifyHotel(modifyHotelInfo(params,htlHotel));//持久化
		
		//发MQ同步文件中酒店基本信息add by wupingxiang at 2012-10-9
		sendLuceneMQ.send("hotelInfo#" + htlHotel.getID());
		
		List<HtlTaxCharge> lisTaxCharge = MyBeanUtil.getBatchObjectFromParam(params, HtlTaxCharge.class, lsTaxChargeNum);
		List<HtlInternet> lisInternet = MyBeanUtil.getBatchObjectFromParam(params, HtlInternet.class, lsInternetNum);
		
		contract = contractService.modifyTaxAndInterNetInfo(hotelId,super.getOnlineRoleUser(),contract,lisTaxCharge,lisInternet);
		super.setEntity(contract);
		//super.getEntityManager().saveOrUpdate(contract);
		super.edit();
		
		/** 解除酒店合同锁定 add by chenjiajie V2.4 2008-8-26 Begin* */
		contractService.deleteLockedRecordTwo(String.valueOf(hotelId),03,super.getOnlineRoleUser().getLoginName());
		/** 解除酒店合同锁定 add by chenjiajie V2.4 2008-8-26 End* */
		
		if ("addCreditCard".equals(forward)||"delCreditCard".equals(forward)||"editCreditCard".equals(forward)||"viewCreditCard".equals(forward)){
				
			return forward; 
		}
		
		//提示信息 hotel2.9.2 add by shengwei.zuo 
		if ("addAlerttypeInfo".equals(forward)||"delAlerttypeInfo".equals(forward)||"editAlerttypeInfo".equals(forward)||"viewAlerttypeInfo".equals(forward)){
			
			return forward; 
			
		}
		
		return SAVE_SUCCESS;
	}

	/**
	 * 返回合同查询页面，并解锁
	 * @return
	 */
	public String backToList(){
		Map params = super.getParams();
		String hotelIdStr = (String) params.get("hotelId");
		/** 解除酒店合同锁定 add by chenjiajie V2.4 2008-8-26 Begin* */
		contractService.deleteLockedRecordTwo(hotelIdStr,03,super.getOnlineRoleUser().getLoginName());
		/** 解除酒店合同锁定 add by chenjiajie V2.4 2008-8-26 End* */
		return "queryContract";
	}
	
	
	/**
	 * 点合同页面修改酒店信息按扭后.需要对合同解锁
	 * 
	 * @return
	 */
	public String backToHotelInfo()
	{
		Map params = super.getParams();
		String hotelIdStr =(String)params.get("hotelId");
		params.put("hotelId", hotelIdStr);
		params.put("forwordHotelInfo", "forwordHotelInfo");
		contractService.deleteLockedRecordTwo(hotelIdStr,03,super.getOnlineRoleUser().getLoginName());
		return "backHotelInfo";
	}
	
	
	
	

	
	public String save(){
		super.setEntity(super.populateEntity());
		HtlContract contract= (HtlContract)super.getEntity();
		Map params = super.getParams();
		List<HtlTaxCharge> lisTaxCharge = MyBeanUtil.getBatchObjectFromParam(params, HtlTaxCharge.class, lsTaxChargeNum);
		List<HtlInternet> lisInternet = MyBeanUtil.getBatchObjectFromParam(params, HtlInternet.class, lsInternetNum);
		contract = contractService.modifyTaxAndInterNetInfo(hotelId,super.getOnlineRoleUser(),contract,lisTaxCharge,lisInternet);
		//		合约
		contractManage.modifyContract(contract);
		super.setEntityID(contract.getID());
		return SAVE_SUCCESS;
	}
	
	public String createNewContract(){
		/*
		 * 新加合同时同步更新酒店基本信息表的：提示信息、其它联系事项、提示信息
		 * 修改人：黄鑫洋
		 */
		Map params = super.getParams(); 
		HtlHotel htlHotel = hotelManage.findHotel(hotelId);
		hotelManage.modifyHotel(modifyHotelInfo(params,htlHotel));//持久化
		
		super.setEntity(super.populateEntity());
		HtlContract contract= (HtlContract)super.getEntity();
		contract.setHotelId(hotelId);
		
		if (super.getOnlineRoleUser()!=null){
		    contract.setCreateBy(super.getOnlineRoleUser().getName());
		    contract.setCreateById(super.getOnlineRoleUser().getLoginName());
		    contract.setCreateTime(new Date());
		    contract.setModifyBy(super.getOnlineRoleUser().getName());
		    contract.setModifyById(super.getOnlineRoleUser().getLoginName());
		    contract.setModifyTime(new Date());
		}
		//默认为含服务费 modify by zhineng.zhuang
		contract.setIncServiceCharge(true);
		contractManage.createContract(contract);
		    
		super.setEntityID(contract.getID());
		return "createNewContract";
	}
	
	/**
	 * 延长合同
	 * @return
	 */
	public String continueContract(){
		Map params = super.getParams();
		List lstDate = null;
		if("periodContinue".equals(forward)){
			lstDate = MyBeanUtil.getBatchObjectFromParam(params, ContractContinue.class, rowNum);
			try {
				contractManage.continueContract(contractId, hotelId,lstDate, true,super.getOnlineRoleUser());
			} catch (SQLException e) {
				log.error(e.getMessage(),e);
			}
		}else if("singleContinue".equals(forward)){
			lstDate = MyBeanUtil.getBatchObjectFromParam(params, SingleContractContinue.class, secondRowNum);
			try {
				contractManage.continueContract(contractId, hotelId,lstDate, false,super.getOnlineRoleUser());
			} catch (SQLException e) {
				log.error(e.getMessage(),e);
			}
		}
		return view();
	}
	
	/**
	 * 缩短合同
	 * @return
	 */
	public String cutContractDate(){
		contractManage.cutContractDate(contractId,cutDate);
		return view();
	}

	/**
	 * 初始化延长合同
	 * @return
	 */
	public String initContinue(){
		roomTypePriceTypeLis = hotelManage.findRoomTypePriceTypeLis(hotelId);
		hotelCnName = hotelManage.findHotel(hotelId).getChnName();
		return "initContinue";
	}
	
	/**
	 * 初始化缩短合同
	 * @return
	 */
	public String initCutContractDate(){
		
		return "initCutContractDate";
	}
	
	/*
	 * 跳到上传合同文件页面
	 */
	public String toUploadContractFile(){
			
		return "toUploadFile";
	}
	
	/*
	 * 实现上传
	 */
	private static void copy(File src, File dst) {
		try {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new BufferedInputStream(new FileInputStream(src),
						BUFFER_SIZE);
				out = new BufferedOutputStream(new FileOutputStream(dst),
						BUFFER_SIZE);
				byte[] buffer = new byte[BUFFER_SIZE];
				while (in.read(buffer) > 0) {
					out.write(buffer);
				}
			} finally {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	
	/*
	 * 取文件名
	 */
	private static String getExtention(String fileName) {
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(pos);
	}
	
	/*
	 * 上载合同
	 */
	public String uploadContractFile(){		
		serverFileName = new Date().getTime() + getExtention(fileName);
		serverFile = new File(ServletActionContext.getServletContext().getRealPath("upload")+ "/" +"ContractFile"+ "/"+ serverFileName);	
		copy(fileUpload, serverFile);
		relaPath="/"+"upload"+"/"+"ContractFile"+"/"+serverFileName;//要取相对路径		
		contractFile = new ContractFile();
		contractFile.setContractId(contractId);
		contractFile.setFileName(serverFileName);
		contractFile.setFilePath(relaPath);
		contractManage.addContractFile(contractFile);
		return "toUploadFile";
	}	
	
	/**
	 * 删除合同
	 * @return
	 */
	public String deleteContractFile(){
		
		contractManage.deleteContractFile(contractFile);
		
		super.setEntityID(contractFile.getContractId());
		
		super.view();
		 
		HtlContract  htlContract = (HtlContract)super.getEntity();
		 
		lsHtlTaxCharge = htlContract.getHtlTaxCharge();
		 
		lsTaxChargeNum = lsHtlTaxCharge.size();
		 
		lsHtlInternet = htlContract.getHtlInternet();
		 
		lsInternetNum = lsHtlInternet.size();
		 
		contractFileList = contractManage.queryContractFileList(contractFile); 
		
		 //hote2.9.2 提示信息 add by shengwei.zuo 2009-08-16
		htlAlerttypeInfoLst= contractManage.queryAlertInfoByConId(contractFile.getContractId());
		
		return VIEW;
	}
	
	/**
	 * 同步合同信息到基本信息表中
	 * @param params
	 * @param htlHotel
	 * @return
	 */
	private HtlHotel modifyHotelInfo(Map params,HtlHotel htlHotel){
		htlHotel.setAlertMessage((String)params.get("insideNotes"));//提示信息
		htlHotel.setOthersNotes((String)params.get("othersRemark"));//其它联系事项
		htlHotel.setChangePriceHint((String)params.get("changePriceHint"));//提示信息
		return htlHotel;
	}
	
	public Date getContinueDate() {
		return continueDate;
	}

	public void setContinueDate(Date continueDate) {
		this.continueDate = continueDate;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public ContractManage getContractManage() {
		return contractManage;
	}


	public void setContractManage(ContractManage contractManage) {
		this.contractManage = contractManage;
	}


	public int getLsTaxChargeNum() {
		return lsTaxChargeNum;
	}


	public void setLsTaxChargeNum(int lsTaxChargeNum) {
		this.lsTaxChargeNum = lsTaxChargeNum;
	}


	public List getLsHtlTaxCharge() {
		return lsHtlTaxCharge;
	}


	public void setLsHtlTaxCharge(List lsHtlTaxCharge) {
		this.lsHtlTaxCharge = lsHtlTaxCharge;
	}


	public String getForward() {
		return forward;
	}


	public void setForward(String forward) {
		this.forward = forward;
	}


	public long getCreditAssureID() {
		return creditAssureID;
	}


	public void setCreditAssureID(long creditAssureID) {
		this.creditAssureID = creditAssureID;
	}


	public Date getCutDate() {
		return cutDate;
	}


	public void setCutDate(Date cutDate) {
		this.cutDate = cutDate;
	}


	public String getBDate() {
		return bDate;
	}


	public void setBDate(String date) {
		bDate = date;
	}


	public String getEDate() {
		return eDate;
	}


	public void setEDate(String date) {
		eDate = date;
	}


	public long getHotelId() {
		return hotelId;
	}


	public void setHotelId(long hotelId) {
		this.hotelId = hotelId;
	}


	public List getLsHtlInternet() {
		return lsHtlInternet;
	}


	public void setLsHtlInternet(List lsHtlInternet) {
		this.lsHtlInternet = lsHtlInternet;
	}


	public int getLsInternetNum() {
		return lsInternetNum;
	}


	public void setLsInternetNum(int lsInternetNum) {
		this.lsInternetNum = lsInternetNum;
	}


	public HotelManage getHotelManage() {
		return hotelManage;
	}


	public void setHotelManage(HotelManage hotelManage) {
		this.hotelManage = hotelManage;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public File getFileUpload() {
		return fileUpload;
	}


	public void setFileUpload(File fileUpload) {
		this.fileUpload = fileUpload;
	}


	public String getRelaPath() {
		return relaPath;
	}


	public void setRelaPath(String relaPath) {
		this.relaPath = relaPath;
	}


	public File getServerFile() {
		return serverFile;
	}


	public void setServerFile(File serverFile) {
		this.serverFile = serverFile;
	}


	public String getServerFileName() {
		return serverFileName;
	}


	public void setServerFileName(String serverFileName) {
		this.serverFileName = serverFileName;
	}


	public ContractFile getContractFile() {
		return contractFile;
	}


	public void setContractFile(ContractFile contractFile) {
		this.contractFile = contractFile;
	}


	public List getContractFileList() {
		return contractFileList;
	}


	public void setContractFileList(List contractFileList) {
		this.contractFileList = contractFileList;
	}


	public String getSrcBeginDate() {
		return srcBeginDate;
	}


	public void setSrcBeginDate(String srcBeginDate) {
		this.srcBeginDate = srcBeginDate;
	}


	public String getSrcEndDate() {
		return srcEndDate;
	}


	public void setSrcEndDate(String srcEndDate) {
		this.srcEndDate = srcEndDate;
	}


	public String getHotelCnName() {
		return hotelCnName;
	}


	public void setHotelCnName(String hotelCnName) {
		this.hotelCnName = hotelCnName;
	}


	public List getHtlOpenCloseRoomLis() {
		return htlOpenCloseRoomLis;
	}


	public void setHtlOpenCloseRoomLis(List htlOpenCloseRoomLis) {
		this.htlOpenCloseRoomLis = htlOpenCloseRoomLis;
	}


	public List getRoomTypePriceTypeLis() {
		return roomTypePriceTypeLis;
	}


	public void setRoomTypePriceTypeLis(List roomTypePriceTypeLis) {
		this.roomTypePriceTypeLis = roomTypePriceTypeLis;
	}

	public int getSecondRowNum() {
		return secondRowNum;
	}


	public void setSecondRowNum(int secondRowNum) {
		this.secondRowNum = secondRowNum;
	}

	public long getAlerttypeInfoID() {
		return alerttypeInfoID;
	}


	public void setAlerttypeInfoID(long alerttypeInfoID) {
		this.alerttypeInfoID = alerttypeInfoID;
	}	

	public List getHtlAlerttypeInfoLst() {
		return htlAlerttypeInfoLst;
	}


	public void setHtlAlerttypeInfoLst(List htlAlerttypeInfoLst) {
		this.htlAlerttypeInfoLst = htlAlerttypeInfoLst;
	}

	public String getFavClauseID() {
		return favClauseID;
	}


	public void setFavClauseID(String favClauseID) {
		this.favClauseID = favClauseID;
	}


	public Long getFavID() {
		return favID;
	}


	public void setFavID(Long favID) {
		this.favID = favID;
	}


	public String getPriTpIdStr() {
		return priTpIdStr;
	}


	public void setPriTpIdStr(String priTpIdStr) {
		this.priTpIdStr = priTpIdStr;
	}


	public String getAlerttypeInfoIDs() {
		return alerttypeInfoIDs;
	}


	public void setAlerttypeInfoIDs(String alerttypeInfoIDs) {
		this.alerttypeInfoIDs = alerttypeInfoIDs;
	}


	public String getCloseIeOrnot() {
		return closeIeOrnot;
	}


	public void setCloseIeOrnot(String closeIeOrnot) {
		this.closeIeOrnot = closeIeOrnot;
	}


	public ContractService getContractService() {
		return contractService;
	}


	public void setContractService(ContractService contractService) {
		this.contractService = contractService;
	}

	public SendLuceneMQ getSendLuceneMQ() {
		return sendLuceneMQ;
	}

	public void setSendLuceneMQ(SendLuceneMQ sendLuceneMQ) {
		this.sendLuceneMQ = sendLuceneMQ;
	}	
}
