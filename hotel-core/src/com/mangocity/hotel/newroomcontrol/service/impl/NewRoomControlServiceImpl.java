package com.mangocity.hotel.newroomcontrol.service.impl;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.dom4j.DocumentHelper;

import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.assistant.RoomStateBean;
import com.mangocity.hotel.base.persistence.HotelRoomcontrolLogsDisplay;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlPopedomControl;
import com.mangocity.hotel.base.persistence.HtlRoomControlWorkSchedule;
import com.mangocity.hotel.base.persistence.HtlRoomStatusProcess;
import com.mangocity.hotel.base.persistence.HtlRoomcontrolHotelSchedule;
import com.mangocity.hotel.base.persistence.HtlRoomcontrolOperationLogs;
import com.mangocity.hotel.base.persistence.HtlRoomcontrolOptsublogs;
import com.mangocity.hotel.base.persistence.HtlRoomcontrolSorting;
import com.mangocity.hotel.base.persistence.HtlRoomcontrolWorkstation;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.HtlTempQuota;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.base.service.assistant.CutDate;
import com.mangocity.hotel.base.service.assistant.RoomBedRecord;
import com.mangocity.hotel.newroomcontrol.dao.impl.NewRoomControlDao;
import com.mangocity.hotel.newroomcontrol.service.NewRoomControlService;
import com.mangocity.hotel.newroomcontrol.service.assistant.FaxInfoForRoomcontrol;
import com.mangocity.hotel.newroomcontrol.service.assistant.NewRoomControlFaxInfo;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.BeanUtil;
import com.mangocity.util.bean.DateComponent;
import com.mangocity.util.hotel.constant.BedType;
import com.mangocity.util.hotel.constant.FaxEmailModel;
import com.mangocity.util.hotel.constant.RoomState;
import com.mangocity.util.log.MyLog;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Fax;

/**
 */
public class NewRoomControlServiceImpl  implements NewRoomControlService {
	
	private static final MyLog log = MyLog.getLogger(NewRoomControlServiceImpl.class);
	
	private NewRoomControlDao newRoomControlDao;
	
	private ResourceManager resourceManager;
	
	private HotelManage hotelManage;
	
	private CommunicaterService communicaterService;
	
	//房控操作日志用默认分页为10
	private final static double ROOM_STATE_PAGE_SIZE = 10.0;
	
	private final static String THE_WEEKS = "1,2,3,4,5,6,7";
	
	String[] areaGroupTemp = {"GAZ-港澳组","SZZ-深圳组","GZZ-广州组","SHZ-上海组","BJZ-北京组","DBZ-北方组","JZZ-江浙组"};
	
	/**
	 * 根据查询条件查询当天上班人员信息。默认查询条件日期为今天
	 * @param personId
	 * @param personIdName
	 * @param queryDate
	 * @param queryAreaGroup
	 * @return
	 * add by zhijie.gu
	 */
	public List searchRoomControlPerson(String personId,String personIdName,String queryBeginDate,String queryEndDate,String queryAreaGroup){
		
		List<HtlRoomControlWorkSchedule> listForWorkSchedule = new ArrayList();
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select hs.workschedualid, hs.username,hs.ondutytime,hs.begindate,hs.enddate,hs.workgroup,hs.modifybyname,hs.remark ," +
					"case when instr(hs.workgroup,'GAZ')=1 then 8 "+
					"when  instr(hs.workgroup,'SZZ')=1 then 7 "+
					"when  instr(hs.workgroup,'GZZ')=1 then 6 "+
					"when  instr(hs.workgroup,'SHZ')=1 then 5 "+
					"when  instr(hs.workgroup,'JZZ')=1 then 4 "+
					"when  instr(hs.workgroup,'BJZ')=1 then 3 "+
					"when  instr(hs.workgroup,'GZZ')=1 then 2 "+
					"when  instr(hs.workgroup,'DBZ')=1 then 1 end as paixi "+
					"from htl_roomcontrol_work_schedule hs where hs.active = '1' ");
		if(!"".equals(queryBeginDate) && null != queryBeginDate && !"".equals(queryEndDate) && null != queryEndDate){
			sqlBuffer.append("and ((hs.enddate >= to_date('"+queryBeginDate+"','yyyy-MM-dd') and hs.enddate <= to_date('"+queryEndDate+"','yyyy-MM-dd')) ");
			sqlBuffer.append("or (hs.begindate >= to_date('"+queryBeginDate+"','yyyy-MM-dd') and hs.begindate <= to_date('"+queryEndDate +"','yyyy-MM-dd')) ");
			sqlBuffer.append("or (hs.begindate <= to_date('"+queryBeginDate+"','yyyy-MM-dd') and hs.enddate >= to_date('"+queryEndDate +"','yyyy-MM-dd'))) ");
		}
		if(!"".equals(personId) && null != personId){
			sqlBuffer.append("and hs.loginname = '"+personId+"' ");
		}
		if(!"".equals(personIdName) && null != personIdName){
			sqlBuffer.append(" and hs.username like '%"+personIdName+"%' ");
		}
		if(!"".equals(queryAreaGroup) && null != queryAreaGroup){
			sqlBuffer.append(" and (instr(hs.workgroup,'"+queryAreaGroup+"') > 0 ");
			sqlBuffer.append(" or instr('"+queryAreaGroup+"',hs.workgroup) >0 "+") ") ;
		}
		sqlBuffer.append("order by paixi desc,hs.ondutytime asc,hs.begindate desc ");
		String sql = sqlBuffer.toString();
		List workScheduleList = newRoomControlDao.doquerySQL(sql, false);
		
		//把sql返回的结果封装成HtlRoomControlWorkSchedule对象
		listForWorkSchedule = setObjecValue(workScheduleList);
		////把组别3字码的组转换成中文
		for(int k=0;k<listForWorkSchedule.size();k++){
			String areaGroupVlue="";
			HtlRoomControlWorkSchedule roomControlWorkScheduleItems = listForWorkSchedule.get(k);
			String [] areaGroups = roomControlWorkScheduleItems.getWorkGroup().split(",");
			if(null != areaGroups){
				for(int kk=0;kk<areaGroups.length;kk++){
					for(int jj = 0;jj<areaGroupTemp.length;jj++){
						if(areaGroupTemp[jj].indexOf(areaGroups[kk]) > -1){
							String[]  areaGroupTempItems = areaGroupTemp[jj].split("-");
							areaGroupVlue = areaGroupVlue+areaGroupTempItems[1]+",";
							break;
						}
						
					}
				}
			roomControlWorkScheduleItems.setWorkGroupZHValue(areaGroupVlue);
			}
			
		}
		
		
		return listForWorkSchedule;
		
	}
	
	/**
	 * 新房态新建批量增加上班人员时，拿出所有员工信息和今天上班每个人员对应处理的区域
	 * @return
	 * add by zhijie.gu
	 */
	public List queryAllWorkSchedule(){
		
		List listForAllRoomStatusWorker = new ArrayList();
		List oldRoomStatusWorkerRecords = new ArrayList();
		String sql = "from HtlPopedomControl c where c.controlType = '2'";
		String sql1 = "from HtlRoomControlWorkSchedule s where s.beginDate <= trunc(sysdate) "
					  +"and s.endDate >= trunc(sysdate) and s.active = '1'";
		listForAllRoomStatusWorker = newRoomControlDao.doquery(sql, false);
		//listForAllWorkSchedule = super.doquerySQL(sql, false);
		oldRoomStatusWorkerRecords = newRoomControlDao.doquery(sql1, false);
    	
		if(!oldRoomStatusWorkerRecords.isEmpty() && !listForAllRoomStatusWorker.isEmpty()){
			//把今天上班员工所在的组别带到批量设置员工上班表里面去
			for(int k =0; k<listForAllRoomStatusWorker.size();k++){
				String areaGroupZHValue="";
				String areaGroupValue="";
				String areasValue="";
				HtlPopedomControl htlPopedomControl = (HtlPopedomControl)listForAllRoomStatusWorker.get(k);
				
				for(int n=0;n<oldRoomStatusWorkerRecords.size();n++){
					
					HtlRoomControlWorkSchedule roomControlWorkSchedule = (HtlRoomControlWorkSchedule)oldRoomStatusWorkerRecords.get(n);
					if(htlPopedomControl.getLogName().equals(roomControlWorkSchedule.getLoginName())){
						if(!"".equals(roomControlWorkSchedule.getWorkGroup()) && null !=roomControlWorkSchedule.getWorkGroup()){
							areaGroupValue=roomControlWorkSchedule.getWorkGroup();
							areasValue = roomControlWorkSchedule.getWorkAreas();
							//把3字码的组转换成中文
							String [] areaGroups = roomControlWorkSchedule.getWorkGroup().split(",");
							if(null != areaGroups){
			    				for(int kk=0;kk<areaGroups.length;kk++){
			    					for(int jj = 0;jj<areaGroupTemp.length;jj++){
			    						if(areaGroupTemp[jj].indexOf(areaGroups[kk]) > -1){
			    							String[]  areaGroupTempItems = areaGroupTemp[jj].split("-");
			    							areaGroupZHValue = areaGroupZHValue+areaGroupTempItems[1]+",";
			    							break;
			    						}
			    					}
			    				}
			    			}
							htlPopedomControl.setWorkGroup(areaGroupValue);
							htlPopedomControl.setWorkGroupZHValue(areaGroupZHValue);
							htlPopedomControl.setWorkAreas(areasValue);
							//一个员工对应某一天只有一条记录，所以结束这次循环，查找下一个员工是否有记录
							
						}
						break;
					}
					
				}
				
			}
		}
    	return listForAllRoomStatusWorker;
		
	}
	
	/**
	 * 新房态批量保存员工排班信息
	 * @return
	 * add by zhijie.gu
	 */
	public long saveOrUpdateRoomSchedule (HtlRoomControlWorkSchedule roomControlWorkSchedule) throws IllegalAccessException, InvocationTargetException {
		
		List oldRoomControlWorkSchedule = newRoomControlDao.queryByNamedQuery("queryRoomControlWorkSchedule", new Object[]{roomControlWorkSchedule.getLoginName()});	
		if(oldRoomControlWorkSchedule==null||oldRoomControlWorkSchedule.size()==0){
			newRoomControlDao.save(roomControlWorkSchedule);
		}else{
			
			boolean flag = false;
			/////////////////////////////////////////////////////
            //如果数据库有超过1条的记录，则先判断时间段是否出现重叠再进行拆分
			if(oldRoomControlWorkSchedule.size()>1){
				List dateCopList = new ArrayList();
				for(int i=0;i<oldRoomControlWorkSchedule.size();i++){
					DateComponent dateCop = new DateComponent();
					HtlRoomControlWorkSchedule roomSchedule = (HtlRoomControlWorkSchedule)oldRoomControlWorkSchedule.get(i);
					dateCop.setId(roomSchedule.getWorkSchedualId());
					dateCop.setBeginDate(roomSchedule.getBeginDate());
					dateCop.setEndDate(roomSchedule.getEndDate());
					dateCop.setModifyDate(roomSchedule.getModifyTime());
					dateCopList.add(dateCop);
				}
				
				if(CutDate.compareConflict(dateCopList)){//如果存在重叠，则要拆分
					
					for(int j=1;j<oldRoomControlWorkSchedule.size();j++){
						HtlRoomControlWorkSchedule subRoomSchedule = (HtlRoomControlWorkSchedule)oldRoomControlWorkSchedule.get(j);
						List subList = newRoomControlDao.queryByNamedQuery("querySubRoomControlWorkSchedule", new Object[]{subRoomSchedule.getLoginName(),subRoomSchedule.getModifyTime()});
						this.favClauseUtil(subRoomSchedule, subList);
					}
					
					flag = true;
					
				}
				
			}
			///////////////////////////////////////////////////
			if(flag==true){
				oldRoomControlWorkSchedule = newRoomControlDao.queryByNamedQuery("queryRoomControlWorkSchedule", new Object[]{roomControlWorkSchedule.getLoginName()});
			}
			this.favClauseUtil(roomControlWorkSchedule, oldRoomControlWorkSchedule);
			
		}
		return 0;
    }
	/**
	 * 新房态批量增加上班人员排班日期拆分  add by zhijie.gu hotel 2.9.3 2009-12-28
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void favClauseUtil(HtlRoomControlWorkSchedule roomControlWorkSchedule,List oldRoomControlWorkSchedule)throws IllegalAccessException, InvocationTargetException{
		
		DateComponent dateComponent = new DateComponent();
		dateComponent.setBeginDate(roomControlWorkSchedule.getBeginDate());
		dateComponent.setEndDate(roomControlWorkSchedule.getEndDate());
		List dateCops = new ArrayList();
		Map resultMap = new HashMap(); 	
		
		for(int ii=0;ii<oldRoomControlWorkSchedule.size();ii++){
			
			HtlRoomControlWorkSchedule roomControlWorkScheduleII = (HtlRoomControlWorkSchedule)oldRoomControlWorkSchedule.get(ii);
			DateComponent aComponent = new DateComponent();
			aComponent.setId(roomControlWorkScheduleII.getWorkSchedualId());
			aComponent.setBeginDate(roomControlWorkScheduleII.getBeginDate());
			aComponent.setEndDate(roomControlWorkScheduleII.getEndDate());
			dateCops.add(aComponent);			
			
		}
		
		resultMap = CutDate.cutForRoomStatusSchedule(dateComponent,CutDate.sort(dateCops));
		List removeList = (List) resultMap.get("remove");
		List updateList = (List) resultMap.get("update");
		List results = new ArrayList();
		for(int jj=0; jj<removeList.size(); jj++){
			DateComponent bb = (DateComponent)removeList.get(jj);
			newRoomControlDao.remove(HtlRoomControlWorkSchedule.class, bb.getId());	
		}	
		
		 //根据拆分的时间段重新组装数据	
		boolean nullFlag = false;
		for(int i=0; i<oldRoomControlWorkSchedule.size(); i++){
			
			HtlRoomControlWorkSchedule roomControlWorkScheduleObj= (HtlRoomControlWorkSchedule) oldRoomControlWorkSchedule.get(i);
			int doubleFlag = 0;
			for(int j=0; j<updateList.size(); j++){
				DateComponent dateCop = (DateComponent) updateList.get(j);				
				if(dateCop.getId()!=null){
					if(dateCop.getId().equals(roomControlWorkScheduleObj.getWorkSchedualId())){
						doubleFlag++;
						//如果存在多个相同的id则只有第一个id保留，其他的id都赋值为null
						if(doubleFlag>1){
							HtlRoomControlWorkSchedule newRoomControlWorkScheduleObj= new HtlRoomControlWorkSchedule();
							BeanUtils.copyProperties(newRoomControlWorkScheduleObj,roomControlWorkScheduleObj);
							List lstNewParameterObj = new ArrayList();
							newRoomControlWorkScheduleObj.setWorkSchedualId(null);
							newRoomControlWorkScheduleObj.setBeginDate(dateCop.getBeginDate());
							newRoomControlWorkScheduleObj.setEndDate(dateCop.getEndDate());							
							results.add(newRoomControlWorkScheduleObj);
						}else{
							roomControlWorkScheduleObj.setBeginDate(dateCop.getBeginDate());
							roomControlWorkScheduleObj.setEndDate(dateCop.getEndDate());	
							results.add(roomControlWorkScheduleObj);
						}
					}	
				}else if(nullFlag==false){
					if(roomControlWorkSchedule.getWorkSchedualId()!= null){	//处理修改情况		
						
						HtlRoomControlWorkSchedule roomControlWorkScheduleUpt = new HtlRoomControlWorkSchedule();
						BeanUtils.copyProperties(roomControlWorkScheduleUpt,roomControlWorkSchedule);
						roomControlWorkScheduleUpt.setWorkSchedualId(null);
						results.add(roomControlWorkScheduleUpt);
						
					}else{//处理新增情况
						results.add(roomControlWorkSchedule);
					}
					nullFlag = true;
					
				}
			}
		}
		
		newRoomControlDao.saveOrUpdateAll(results);
		
	}
	/**
	 * 根据workSchedualId删除某个员工排班信息
	 * @param workSchedualId
	 * @return
	 * @author zhijie.gu
	 */
	 public int deleteWorkerSchedule(long workSchedualId,String loginName,String chnName){
		 
		 HtlRoomControlWorkSchedule htlRoomControlWorkSchedule = (HtlRoomControlWorkSchedule)newRoomControlDao.find(HtlRoomControlWorkSchedule.class,workSchedualId);
		 //软删除，最后修改人信息为删除人信息
		 htlRoomControlWorkSchedule.setActive("0");
		 htlRoomControlWorkSchedule.setModifyById(loginName);
		 htlRoomControlWorkSchedule.setModifyByName(chnName);
		 htlRoomControlWorkSchedule.setModifyTime(DateUtil.getSystemDate());
		 newRoomControlDao.update(htlRoomControlWorkSchedule);
		 return 0;
	 }
	 /**
		 * 根据workSchedualId获取某个员工排班信息
		 * @param workSchedualId
		 * @return
		 * @author zhijie.gu
		 */
	    public HtlRoomControlWorkSchedule getOneWorkerSchedule(long workSchedualId){
	    	HtlRoomControlWorkSchedule roomControlWorkSchedule = (HtlRoomControlWorkSchedule)newRoomControlDao.find(HtlRoomControlWorkSchedule.class, workSchedualId);
	    	String areaGroupVlue="";
	    	String [] areaGroups = roomControlWorkSchedule.getWorkGroup().split(",");
			if(null != areaGroups){
				for(int kk=0;kk<areaGroups.length;kk++){
					for(int jj = 0;jj<areaGroupTemp.length;jj++){
						if(areaGroupTemp[jj].indexOf(areaGroups[kk]) > -1){
							String[]  areaGroupTempItems = areaGroupTemp[jj].split("-");
							areaGroupVlue = areaGroupVlue+areaGroupTempItems[1]+",";
							break;
						}
						
					}
				}
				roomControlWorkSchedule.setWorkGroupZHValue(areaGroupVlue);
			}
	    	return roomControlWorkSchedule;
	    }
	/**
	 * 获取时设置用户信息
	 * @param hotelSchedule
	 * @param user
	 * @param acquireway //获取方式：1，自动2，手工
	 * @author chenjuesu
	 */
	private void setUserInfo(HtlRoomcontrolHotelSchedule hotelSchedule,UserWrapper user,Integer acquireway){
		hotelSchedule.setAcquirestate(1); //获取状态：0,未获取 1,已获取
		hotelSchedule.setAcquireway(acquireway);//获取方式：1，自动2，手工
		hotelSchedule.setNowassignedid(user.getLoginName());//当前分配人工号
		hotelSchedule.setNowassignedname(user.getName());//当前分配人名
	}
	/**
	 * 完成或释放时清除用户信息
	 * @param hotelSchedule
	 * @author chenjuesu
	 */
	private void clearUserInfo(HtlRoomcontrolHotelSchedule hotelSchedule,UserWrapper user){
		hotelSchedule.setAcquirestate(0); //获取状态：0,未获取 1,已获取
		hotelSchedule.setAcquireway(0);//获取方式：1，自动2，手工
		hotelSchedule.setNowassignedid(null);//当前分配人工号
		hotelSchedule.setNowassignedname(null);//当前分配人名
		hotelSchedule.setOperationlogid(null);//清空操作ID
	}
	/**
	 * 根据ID获取酒店列表实体
	 * @param hotelScheduleId
	 * @return
	 * @author chenjuesu
	 */
	public HtlRoomcontrolHotelSchedule getRoomcontrolHotelScheduleById(String hotelScheduleId){
		if(null == hotelScheduleId)
			return null;
		return (HtlRoomcontrolHotelSchedule)newRoomControlDao.find(HtlRoomcontrolHotelSchedule.class, Long.valueOf(hotelScheduleId));
	}
	/**
	 * 用户自动手工获取一条酒店记录
	 * @param user
	 * @return
	 */
	public boolean allotHotelScheduleByHand(HtlRoomcontrolHotelSchedule hotelSchedule,UserWrapper user){
		//分配成功，设置信息
		setUserInfo(hotelSchedule,user,2);
		createOperationLog(hotelSchedule,user);
		newRoomControlDao.update(hotelSchedule);
		return true;
	}
	/**
	 * 关闭工作状态时，把工作档案里未处理的酒店退出
	 * @param user
	 * addby chenjuesu at 2009-12-30上午10:41:29
	 */
	public void clearUpHotelScheduleInMyWorkSpace(UserWrapper user){
		List myHotelSchedules = newRoomControlDao.checkUserHasAllotHotelSchedule(user.getLoginName(),false);
		for (Object obj : myHotelSchedules) {
			HtlRoomcontrolHotelSchedule hotelSchedule = (HtlRoomcontrolHotelSchedule)obj;
			completeOrReleaseOptLogs(hotelSchedule.getScheduleid().toString(),user,"询房记录因关闭工作状态自动退出。",3,null,null,null);
		}
	}
	/**
	 * 跟据登陆名取得其现分配到的酒店
	 * @param loginName
	 * @return
	 * addby chenjuesu at 2009-12-31下午02:59:42
	 */
	public List queryHotelScheduleByLoginName(String loginName){
		return newRoomControlDao.checkUserHasAllotHotelSchedule(loginName,false);
	}
	/**
	 * 为用户自动分配一条酒店记录
	 * @param user
	 * @return
	 */
	public boolean allotHotelSchedule(UserWrapper user){
		if(!checkUserHasAllotHotelSchedule(user)){
			HtlRoomcontrolHotelSchedule hotelSchedule = newRoomControlDao.allotHotelSchedule(user.getLoginName(),null);
			if(null != hotelSchedule){
				//分配成功，设置信息
				setUserInfo(hotelSchedule,user,1);
				createOperationLog(hotelSchedule,user);
				newRoomControlDao.update(hotelSchedule);
				return true;
			}
		}
		return false;
	}
	/**
	 * 分配成功时，生成一条操作日志
	 * @param hotelSchedule
	 * @param user
	 */
	private void createOperationLog(HtlRoomcontrolHotelSchedule hotelSchedule, UserWrapper user) {
		// TODO Auto-generated method stub
		HtlRoomcontrolOperationLogs operationLogs = new HtlRoomcontrolOperationLogs();
		operationLogs.setHotelid(hotelSchedule.getHotelid());
		operationLogs.setHotelname(hotelSchedule.getHotelname());
		operationLogs.setOperatorid(user.getLoginName());
		operationLogs.setOperatorname(user.getName());
		operationLogs.setStarttime(new Date());
		operationLogs.setAcquireway(hotelSchedule.getAcquireway());
		Serializable optLogId = newRoomControlDao.save(operationLogs);
		
		hotelSchedule.setOperationlogid((Long)optLogId);
	}
	/**
	 * 保存房态时，记录操作日志
	 * @param hotelScheduleId
	 * @param user
	 * @param changeContent
	 */
	public void saveRoomStatusOptLogs(String hotelScheduleId,UserWrapper user,String changeContent){
		if(null == hotelScheduleId)
			return;
		HtlRoomcontrolHotelSchedule hotelSchedule = (HtlRoomcontrolHotelSchedule)
			newRoomControlDao.load(HtlRoomcontrolHotelSchedule.class, Long.valueOf(hotelScheduleId));//得到列表记录
		HtlRoomcontrolOperationLogs operationLogs = (HtlRoomcontrolOperationLogs)
			newRoomControlDao.load(HtlRoomcontrolOperationLogs.class, hotelSchedule.getOperationlogid());//得到操作记录
		String subLogIds = saveSubLogs(operationLogs,changeContent);
		StringBuffer sb = new StringBuffer();
		String optContents = operationLogs.getOperatorcontent();
		sb.append(optContents == null ? "" : optContents).
				append(DateUtil.formatDateToYMDHMS(new Date())).append(" LOG: \n").append("保存房态,详见子表ID(s):").append(subLogIds).append("\n");
		operationLogs.setOperatorcontent(sb.toString());
		boolean beSave = false;
		if(2 == hotelSchedule.getHotelSellState()){
			hotelSchedule.setHotelSellState(1);
			beSave = true;
		}
		newRoomControlDao.update(operationLogs);
		if(1 == hotelSchedule.getCcsetroomfullorclose()){//如果存在CC设置满房或关房，则取消状态
			newRoomControlDao.setCCSetRoomStateReviewed(hotelSchedule.getHotelid(),user);
			hotelSchedule.setCcsetroomfullorclose(0);
			beSave = true;
		}
		if(beSave)
			newRoomControlDao.update(hotelSchedule);
	}
	private String saveSubLogs(HtlRoomcontrolOperationLogs operationLogs, String changeContent) {
		// TODO Auto-generated method stub
		int eachRowLength = 1000;
		int conLength = changeContent.length();
		int rows = (conLength-1)/eachRowLength+1;
		Serializable sid = null;
		String returnIds = "";
		for (int i = 1; i <= rows; i++) {
			HtlRoomcontrolOptsublogs sublog = new HtlRoomcontrolOptsublogs();
			sublog.setOptid(operationLogs.getLogid());
			int begin = (i-1) * eachRowLength;
			int end = i * eachRowLength;
			if(end > conLength)
				end = conLength;
			String subCont = changeContent.substring(begin,end);
			sublog.setContent(subCont);
			if(null != sid)
				sublog.setPreid((Long)sid);
			sid = newRoomControlDao.save(sublog);
			returnIds += sid + ",";
		}
		return returnIds;
	}

	/**
	 * 
	 * @param hotelScheduleId
	 * @param user
	 * @param changeContent
	 * @param optType 操作类型，1：完成 2,：释放 3,工作状态关闭时退出的
	 * @param relaxreason 释放原因：1：电话未通 2：负责人不在 3：稍后提供
	 * @param priTime 下次询房点
	 */
	public void completeOrReleaseOptLogs(String hotelScheduleId,UserWrapper user,String changeContent,int optType,Integer relaxreason, Integer priTime, String dealWithSource){
		if(null == hotelScheduleId)
			return;
		HtlRoomcontrolHotelSchedule hotelSchedule = (HtlRoomcontrolHotelSchedule)
			newRoomControlDao.load(HtlRoomcontrolHotelSchedule.class, Long.valueOf(hotelScheduleId));//得到列表记录
		HtlRoomcontrolOperationLogs operationLogs = (HtlRoomcontrolOperationLogs)
			newRoomControlDao.load(HtlRoomcontrolOperationLogs.class, hotelSchedule.getOperationlogid());//得到操作记录
		//点关闭或释放但之前有保存过，则算工作量
		if(null != operationLogs.getOperatorcontent()){
			updateUserWorkstations(hotelSchedule,user);
		}
		//下面对操作记录操作
		StringBuffer sb = new StringBuffer();
		String optContents = operationLogs.getOperatorcontent();
		sb.append(optContents == null ? "" : optContents).
				append(DateUtil.formatDateToYMDHMS(new Date())).append(" LOG: \n").append(changeContent).append("\n");
		operationLogs.setOperatorcontent(sb.toString());
		operationLogs.setOperationtype(optType);
		operationLogs.setRelaxreason(relaxreason);
		operationLogs.setFinishtime(new Date());
		hotelSchedule.setSetup(hotelSchedule.getSetup()+1);
		operationLogs.setSetup(hotelSchedule.getSetup());
		if(null !=dealWithSource){
			operationLogs.setDealWithSource(dealWithSource);
		}
		long costTime = operationLogs.getFinishtime().getTime() - operationLogs.getStarttime().getTime();
		operationLogs.setCosttime(costTime);
		newRoomControlDao.update(operationLogs);
		//下面对列表记录操作
		if(2 == optType){
			hotelSchedule.setState(1);
			hotelSchedule.setCheckPoint(priTime);
		}
		clearUserInfo(hotelSchedule,user);
		newRoomControlDao.update(hotelSchedule);
	}
	/**
	 * 完成或释放后,计算工作量
	 * @param hotelSchedule
	 * @param user
	 */
	private void updateUserWorkstations(HtlRoomcontrolHotelSchedule hotelSchedule, UserWrapper user) {
		// TODO Auto-generated method stub
		hotelSchedule.setState(2);//设置至少完成一次
		String modifybyids = hotelSchedule.getModifybyid();
		String modifybynames = hotelSchedule.getModifybyname();
		HtlRoomcontrolWorkstation workstation = newRoomControlDao.findMyWorkStation(user);
		if(null == modifybyids || !modifybyids.contains(user.getLoginName())){
			//此人之前没处理过，需要记录处理人
			workstation.setHotelmount(workstation.getHotelmount() + 1);
		}
		if(null == modifybyids){
			modifybyids = "";
			modifybynames = "";
		}
		modifybyids += ","+user.getLoginName();
		modifybynames += ","+user.getName();
		hotelSchedule.setModifybyid(modifybyids);
		hotelSchedule.setModifybyname(modifybynames);
		hotelSchedule.setModifytime(new Date());
		if(1 == hotelSchedule.getAcquireway())
			workstation.setAutogethotelmount(workstation.getAutogethotelmount() + 1);
		else
			workstation.setHandbygethotelmount(workstation.getHandbygethotelmount()+ 1);
		newRoomControlDao.saveOrUpdate(workstation);
	}

	public String getRelaxReason(int relaxreason){
    	if(1 == relaxreason)
    		return "电话未通";
    	else if(2 == relaxreason)
    		return "负责人不在";
    	else
    		return "稍后提供";
	}
	public void completeAndUnlock(String hotelScheduleId, UserWrapper roleUser, int optType, Integer relaxreason, Integer priTime,String dealWithSource){
		String changeContent = null;
    	if(1 == optType){//完成
    		changeContent = "关闭记录退出";
    	}else if(2 == optType){//释放
    		changeContent = "释放记录,释放原因:"+getRelaxReason(relaxreason)+" 下次询房点:"+priTime;
    	}
    	completeOrReleaseOptLogs(hotelScheduleId,roleUser,changeContent,optType,relaxreason,priTime,dealWithSource);
	}
	/**
	 * 跟据登陆名查找此时的工作状态
	 * @param loginName
	 * @return
	 * addby chenjuesu at 2010-1-7下午05:17:10
	 */
	public HtlRoomcontrolWorkstation findWorkStationBy(UserWrapper user) {
		// TODO Auto-generated method stub
		newRoomControlDao.updateWorkstation(user.getLoginName());
		HtlRoomcontrolWorkstation workstation = newRoomControlDao.findMyWorkStation(user);
		workstation.setWorkingTimes(getWorkingTimeMinutesByDuty(workstation.getOndutytime(),false));
		return workstation;
	}
	/**
	 * 查看今天 所有员工的工作进度
	 * @return
	 * addby chenjuesu at 2010-1-7下午05:34:12
	 */
	public List findAllWorkStations(UserWrapper roleUser){
		if(roleUser.isRSCAdmin())
			newRoomControlDao.updateWorkstation("ALL");
		else
			newRoomControlDao.updateWorkstation(roleUser.getLoginName());
 		List result = newRoomControlDao.findAllWorkStations(roleUser);
		int costA = getWorkingTimeMinutesByDuty("A",false);
		int costB = getWorkingTimeMinutesByDuty("B",false);
		for (Object obj : result) {
			HtlRoomcontrolWorkstation workstation = (HtlRoomcontrolWorkstation)obj;
			if("A".equals(workstation.getOndutytime()))
				workstation.setWorkingTimes(costA);
			else if("B".equals(workstation.getOndutytime()))
				workstation.setWorkingTimes(costB);
		}
		return result;
	}
	/**
	 * 根据A B 班次返回上班到现在的时间分钟数,
	 * @param duty
	 * @param isExtSpareTime 是否包括休息时间
	 * @return
	 * addby chenjuesu at 2010-1-12上午11:14:47
	 */
	public int getWorkingTimeMinutesByDuty(String duty,boolean isIncSpareTime){
		Calendar begin = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		Calendar spareBegin = Calendar.getInstance();
		Calendar spareEnd = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		long spareTime = 0;//休息的分钟数 A 12.0-13.30是一个半小时 B 是半小时，08：30-12：00，13：30-17：30，12：00-17：00，17：30-20：00
		long workingPartOne = 0; //第一部分工作时间
		long workingAll = 450;//总工作时间
		if("A".equals(duty)){
			begin.set(Calendar.HOUR_OF_DAY, 9);
			begin.set(Calendar.MINUTE,0);
			end.set(Calendar.HOUR_OF_DAY, 18);
			end.set(Calendar.MINUTE,0);
			spareBegin.set(Calendar.HOUR_OF_DAY, 12);
			spareBegin.set(Calendar.MINUTE, 0);
			spareEnd.set(Calendar.HOUR_OF_DAY, 13);
			spareEnd.set(Calendar.MINUTE, 30);
			workingPartOne = 210;
			spareTime = 90;
		}else if("B".equals(duty)){
			begin.set(Calendar.HOUR_OF_DAY, 12);
			begin.set(Calendar.MINUTE, 0);
			end.set(Calendar.HOUR_OF_DAY, 21);
			end.set(Calendar.MINUTE, 0);
			spareBegin.set(Calendar.HOUR_OF_DAY, 16);
			spareBegin.set(Calendar.MINUTE, 30);
			spareEnd.set(Calendar.HOUR_OF_DAY, 18);
			spareEnd.set(Calendar.MINUTE, 0);
			spareTime = 30;
			workingPartOne = 300;
		}
		if(now.after(begin) && !now.after(end)){
			if(now.after(spareBegin) && !now.after(spareEnd))
				return (int)workingPartOne;
			if(now.after(end))
				return (int)workingAll;
			long costMinutes = (now.getTimeInMillis() - begin.getTimeInMillis())/60000;//得到上班到现在的分钟数
			if(now.after(spareEnd) && !isIncSpareTime){
				costMinutes -= spareTime;
			}
			return (int)costMinutes;
		}
		return 0;
	}
	/**
	 * 根据登陆名和日期查询员工上班信息，add by zhijie.gu
	 * @param hotelScheduleId
	 * @param loginName
	 * @param todayD
	 */
	public HtlRoomControlWorkSchedule queryOneWokerSchedule (String loginName,Date todayD){
		
		HtlRoomControlWorkSchedule controlWorkSchedule = null;
		String sql ="From HtlRoomControlWorkSchedule ws where ws.loginName =?" 
					+"and ws.beginDate <= trunc(?) and ws.endDate >= trunc(?)";
		Object[] obj = new Object[] {loginName,todayD,todayD};
		List wokerSchedule = newRoomControlDao.doquery(sql, obj, false);
		if(null!=wokerSchedule && wokerSchedule.size()>1){
			log.info("wokerSchedule.size()为"+wokerSchedule.size()+"，同一天同一个人有多条数据（应该只有一条数据）");
		}
		if(wokerSchedule.size() > 0)
			controlWorkSchedule = (HtlRoomControlWorkSchedule)wokerSchedule.get(0);
		return controlWorkSchedule;
		
	}
	private boolean checkUserHasAllotHotelSchedule(UserWrapper user) {
		// TODO Auto-generated method stub
		List myAutoAllots = newRoomControlDao.checkUserHasAllotHotelSchedule(user.getLoginName(),true);
		if(!myAutoAllots.isEmpty())
			return true;
		return false;
	}

	public long saveOrUpdateHotel(HtlRoomControlWorkSchedule roomControlWorkSchedule) throws IllegalAccessException, InvocationTargetException {
		// TODO Auto-generated method stub
		return 0;
	}

	
	/**
	 * 获取酒店列表排序字符串 即order by 后面的字符串
	 * @return
	 */
	public String getRoomControlSortingStr(){
		return newRoomControlDao.getRoomControlSortingStr();
	}
	
	/**
	 * 获取酒店列表排序集合
	 * @return
	 */
	public List<HtlRoomcontrolSorting> getRoomControlSortingList(){
		List<HtlRoomcontrolSorting> htlRoomcontrolSortingList = newRoomControlDao.findConditionSorting();
		return htlRoomcontrolSortingList;
	}
	
	/**
	 * 批量保存排序集合
	 * @param htlRoomcontrolSortingList
	 */
	public void updateAllSorting(List<HtlRoomcontrolSorting> htlRoomcontrolSortingList){
		if(null != htlRoomcontrolSortingList){
			newRoomControlDao.saveOrUpdateAll(htlRoomcontrolSortingList);
		}
	}
	
	public String findWorkareasByUser(String loginName) {
		String sql="select w.workareas  from htl_roomcontrol_work_schedule w  where w.loginname = ?" +
				"  and w.begindate <= trunc(sysdate) and w.enddate >= trunc(sysdate)    and w.active = 1";
		List list=newRoomControlDao.doquerySQL(sql, new Object[]{loginName}, false);
		if(list.isEmpty())
			return null;
		return (String) list.get(0);
	}
	
	/**
	 * 把sql返回的结果封装成HtlRoomControlWorkSchedule对象
	 * @param workScheduleList
	 * @return
	 */
	private List setObjecValue(List workScheduleList){
		
		List<HtlRoomControlWorkSchedule> li = new ArrayList();
		if(!workScheduleList.isEmpty()){
			for(int i = 0;i<workScheduleList.size();i++){
				HtlRoomControlWorkSchedule roomControlWorkScheduleItems = new HtlRoomControlWorkSchedule();
				Object[] workSchedules = (Object[])workScheduleList.get(i);
				if(null != workSchedules[0]){
					roomControlWorkScheduleItems.setWorkSchedualId(Long.parseLong(workSchedules[0].toString()));
				}
				if(null != workSchedules[1]){
					roomControlWorkScheduleItems.setUserName(workSchedules[1].toString());
				}
				if(null != workSchedules[2]){
					roomControlWorkScheduleItems.setOnDutyTime(workSchedules[2].toString());
				}
				if(null != workSchedules[3]){
					roomControlWorkScheduleItems.setBeginDate(DateUtil.getDate(workSchedules[3].toString()));
				}
				if(null != workSchedules[4]){
					roomControlWorkScheduleItems.setEndDate(DateUtil.getDate(workSchedules[4].toString()));
				}
				if(null != workSchedules[5]){
					roomControlWorkScheduleItems.setWorkGroup(workSchedules[5].toString());
				}
				if(null != workSchedules[6]){
					roomControlWorkScheduleItems.setModifyByName(workSchedules[6].toString());
				}
				if(null != workSchedules[7]){
					roomControlWorkScheduleItems.setRemark(workSchedules[7].toString());
				}
				if(null != workSchedules[8]){
					roomControlWorkScheduleItems.setPaixi(Integer.parseInt(workSchedules[8].toString()));
				}
				li.add(roomControlWorkScheduleItems);
				
			}
		}
		return li;
		
		
	}
	
	/**
	 * 延时操作新增一条交接事项 add by zhijie.gu
	 */
	public void saveProcessRemarkAndIsRoomStatus(long hotelId ,UserWrapper roleUser,int relaxreason ,int priTime, long htlRoomStatusProcessID ) {
	    	RoomStateBean roomStateBean = new RoomStateBean();
	    	roomStateBean.setHotelID(hotelId);
	    	String priTimeStr="延时处理：";
	    	if( 0!=priTime && !"".equals(priTime)){
	    		priTimeStr ="下次询房时间点:"+resourceManager.getDescription("hotel_pri_time", priTime)+",";
	    	}
	    	String relaxreasonStr ="原因：" + getRelaxReason(relaxreason);
	    	if(0 != htlRoomStatusProcessID){
	    		HtlRoomStatusProcess roomStatusProcess =queryHtlRoomStatusProcessByID(htlRoomStatusProcessID);
	    		roomStateBean.setUserName(roleUser.getName());
	            roomStateBean.setUserId(roleUser.getLoginName());
	            roomStateBean.setIsRoomStatusReport(roomStatusProcess.getIsRoomStatusReport().toString());
		        roomStateBean.setProcessRemark(roomStatusProcess.getProcessRemark()+" "+priTimeStr+relaxreasonStr);
	    	
	    	}else{//如果交接事项为空，则生成一条交接事项，是否主动报房态默认为0
	    		
	    		roomStateBean.setUserName(roleUser.getName());
	            roomStateBean.setUserId(roleUser.getLoginName());
	            roomStateBean.setIsRoomStatusReport("0");
		        roomStateBean.setProcessRemark(priTimeStr+relaxreasonStr);
		    }
	    	
	    	saveRoomStateProcess(roomStateBean);
	    	
	    }
	
	/**
	 * 查询房态操作日志
	 * @param hotelId
	 */
	public List queryRommControlLog(long hotelId,Date beginDate,Date endDate,int pageNumber){
		String beginDateStr = DateUtil.dateToString(beginDate);
		String endDateStr =  DateUtil.dateToString(endDate);
		
		StringBuffer queryStringBuf = new StringBuffer();
		queryStringBuf.append("select count(*) from htl_roomcontrol_operation_logs l "+" where l.logid in(");
        queryStringBuf.append(" select ll.logid from  htl_roomcontrol_operation_logs ll,htl_roomcontrol_optsublogs  oo where ll.hotelid = "+hotelId+" and ll.logid =oo.optid ");
        queryStringBuf.append("and ll.finishtime is not null and trunc(ll.finishtime) >=to_date('"+beginDateStr+"','yyyy-MM-dd') and trunc(ll.finishtime) <= to_date('"+endDateStr+"','yyyy-MM-dd') group by ll.logid ) ");
        String hsql = queryStringBuf.toString();
        List sqlResult = newRoomControlDao.doquerySQL(hsql, false);
        int totalPageNumber = 0;
        if(!sqlResult.isEmpty()){
        	for(int i=0;i<sqlResult.size();i++){
        		Object ob = (Object)sqlResult.get(i);
            	totalPageNumber = Integer.parseInt(ob.toString());
            }
        	
        }
        List listForRoomstateLogs= newRoomControlDao.pageListForRoomstateLogs(pageNumber, 10,hotelId,beginDateStr,endDateStr);
        List roomstateLogsLists = new ArrayList();
		String queryConditionStr = "";
		if(!listForRoomstateLogs.isEmpty()){
			for(int i=0;i<listForRoomstateLogs.size();i++){
				Object[] roomstateLogs = (Object[])listForRoomstateLogs.get(i);
				HotelRoomcontrolLogsDisplay roomControlLogsDisplayItems = new HotelRoomcontrolLogsDisplay();
				roomControlLogsDisplayItems.setLogid(Long.parseLong(roomstateLogs[0].toString()));
				roomControlLogsDisplayItems.setOperatorname(roomstateLogs[1].toString());
				roomControlLogsDisplayItems.setFinishtime(DateUtil.stringToDateTime(roomstateLogs[2].toString()));
				if(null != roomstateLogs[3]){
					roomControlLogsDisplayItems.setDealWithSource(roomstateLogs[3].toString());
				}
				roomControlLogsDisplayItems.setTotalPageNumber(totalPageNumber);
				roomstateLogsLists.add(roomControlLogsDisplayItems);
				queryConditionStr = queryConditionStr+roomstateLogs[0].toString()+",";
			}
			if(!"".equals(queryConditionStr)){
				queryConditionStr =queryConditionStr.substring(0,queryConditionStr.length()-1);
			}
			
			String sql ="from HtlRoomcontrolOptsublogs ls where ls.optid in("+queryConditionStr+") order by ls.id";
			List roomcontrolOptsublogsLists =newRoomControlDao.doquery(sql, false);
			
			if(!roomcontrolOptsublogsLists.isEmpty()){
				for(int j=0;j<roomstateLogsLists.size();j++){
					String logContent ="";
					HotelRoomcontrolLogsDisplay roomControlLogsItems = (HotelRoomcontrolLogsDisplay)roomstateLogsLists.get(j);
					
					for(int k =0;k<roomcontrolOptsublogsLists.size();k++){
						HtlRoomcontrolOptsublogs roomcontrolOptsublogsItem = (HtlRoomcontrolOptsublogs)roomcontrolOptsublogsLists.get(k);
						if(roomControlLogsItems.getLogid().equals(roomcontrolOptsublogsItem.getOptid()))
							logContent =logContent+roomcontrolOptsublogsItem.getContent();
					}
					roomControlLogsItems.setRoomStateLogContent(logContent);
					String str =""+Math.ceil(roomControlLogsItems.getTotalPageNumber()/ROOM_STATE_PAGE_SIZE);
					str = str.substring(0, str.indexOf("."));
					roomControlLogsItems.setPageSize(Integer.parseInt(str));
					roomstateLogsLists.set(j, roomControlLogsItems);
				}
			}
		}
		return roomstateLogsLists;
	}
	
	/**
	 * 根据id查询交接事项数据
	 * @param htlRoomStatusProcessID
	 */
	private HtlRoomStatusProcess queryHtlRoomStatusProcessByID(long htlRoomStatusProcessID){
		HtlRoomStatusProcess k =new HtlRoomStatusProcess();
		String sql ="from HtlRoomStatusProcess p where p.id ="+htlRoomStatusProcessID;
		k = (HtlRoomStatusProcess)newRoomControlDao.find(sql);
		return k;
	}
	
	/**
     *异步保存交接事项和是否主动报房态 add by zhijie.gu 2010-01-05
     *
    */
	private void saveRoomStateProcess(RoomStateBean roomStateBean){
		
		if(null != roomStateBean) {
            HtlRoomStatusProcess rsp = new HtlRoomStatusProcess();
            rsp.setHotelId(roomStateBean.getHotelID());
            rsp.setProcessBy(roomStateBean.getUserName());
            rsp.setProcessById(roomStateBean.getUserId());
            rsp.setProcessDate(DateUtil.getSystemDate());
            rsp.setProcessRemark(roomStateBean.getProcessRemark());
            rsp.setProcessDatetime(DateUtil.getSystemDate());
            if(!"".equals(roomStateBean.getIsRoomStatusReport()) && null != roomStateBean.getIsRoomStatusReport()){
            	 rsp.setIsRoomStatusReport(Long.valueOf(roomStateBean.getIsRoomStatusReport())
                         .longValue());
            }
            newRoomControlDao.saveOrUpdate(rsp);
        } else {
        	log.info("roomStateBean is null!");
        }
		
		
	}	
	
	/**
     * 找前一天到现在的所有房态更改记录
     * 
     * @param hotelId
     * @return
     */
    public List findRoomStatusDateProcess(long hotelId) {
        List dateRoomProcess = new ArrayList();
        dateRoomProcess = newRoomControlDao.queryByNamedQuery("queryRoomStatus", new Object[] { hotelId,
            hotelId });
        return dateRoomProcess;
    }
    
    /**
	 * 根据酒店id查找所以的房型+床型的所有数据
	 * @param hotelId
	 * @return
	 */
	public List findAllRoomBedByHotelId(long hotelId){
		Map bedType = new HashMap();
		bedType.put("1", "大床");
		bedType.put("2", "双床");
		bedType.put("3", "单床");
		List targetList = new ArrayList();
		List<HtlRoomtype> lis = (List<HtlRoomtype>)newRoomControlDao.queryByNamedQuery("lstHotelRoomType", new Object[] {hotelId});
		String roomBedTypeStr="";
		for(HtlRoomtype roomType :lis){
			roomBedTypeStr = roomType.getBedType();
			if(null!= roomBedTypeStr && !"".equals(roomBedTypeStr)){
				if(roomBedTypeStr.indexOf(",") > -1){
					String[] bedTypesStr=roomBedTypeStr.split(",");
					for(int i=0;i<bedTypesStr.length;i++){
						FaxInfoForRoomcontrol faxInfo = new FaxInfoForRoomcontrol();
						faxInfo.setRoomTypeName(roomType.getRoomName());
						faxInfo.setRoomTypeName(bedType.get(bedTypesStr[i]).toString());
						targetList.add(faxInfo);
					}
				}else{
					FaxInfoForRoomcontrol faxInfo = new FaxInfoForRoomcontrol();
					faxInfo.setRoomTypeName(roomType.getRoomName());
					faxInfo.setRoomTypeName(bedType.get(roomBedTypeStr).toString());
					targetList.add(faxInfo);					
				}
			}
		}
		return targetList; 
	}
	
	/**
	 * 根据酒店id获取所有的房型数据
	 * @return
	 */
	public Map getRoomTypeIdNameMapByHotelId(long hotelId){
		Map roomTypeMap = new HashMap();
		List<HtlRoomtype> roomTypelis = newRoomControlDao.queryByNamedQuery("lstHotelRoomType", new Object[] { hotelId });
		for(HtlRoomtype roomType:roomTypelis){
			roomTypeMap.put(roomType.getID().toString(), roomType.getRoomName());
		}
		return roomTypeMap;
	}
	
	/**
	 * 按日历调整调用方法
	 * 根据房态，查找相应配额是否预警，如果满房或不可超，配额预警的需取消预警，
	 * 如果房态为其他，如果已取消配额预警，需设为配额预警
	 * @return
	 */
	public void changeQuotaWarningByRoomstate(List lstTempQuota,long hotelId){
		String roomTypeIdStr ="";
    	String bedTypeIdStr = "";
    	String ableSaleDateStr = "";
    	String roomStateStr = "";
		for (int i = 0; i < lstTempQuota.size(); i++) {
			HtlTempQuota tq = (HtlTempQuota) lstTempQuota.get(i);
	        //如果房态之前是为满房或不可超改为良好、紧张或freesale的数据，
	        //或者，房态由良好、紧张或freesale改为满房或不可超的，需要调用存储过程处理配额预警状态
	        if(tq.getBedStatus() != tq.getOldBedStatus() && (RoomState.ROOM_STATE_FULL.equals(tq.getBedStatus()) || RoomState.ROOM_STATE_NOTOVERRUN.equals(tq.getBedStatus()) || RoomState.ROOM_STATE_FREESALE.equals(tq.getBedStatus())
	            	|| RoomState.ROOM_STATE_FULL.equals(tq.getOldBedStatus()) || RoomState.ROOM_STATE_NOTOVERRUN.equals(tq.getOldBedStatus()) || RoomState.ROOM_STATE_FREESALE.equals(tq.getOldBedStatus()))){
	            roomTypeIdStr = roomTypeIdStr + tq.getRoomTypeId()+ ",";
	            bedTypeIdStr = bedTypeIdStr + tq.getBedId() + ",";
	            ableSaleDateStr = ableSaleDateStr + DateUtil.dateToString(tq.getStartDate()) + ",";
	            roomStateStr = roomStateStr+tq.getBedStatus() + ",";
	        }
	            
	    }
		newRoomControlDao.updateQuotaWarningByRoomState(hotelId, roomTypeIdStr, bedTypeIdStr, roomStateStr, ableSaleDateStr);
	}
	
	/**
	 * 批量调整房态调用方法，
	 * 根据房态，查找相应配额是否预警，如果满房或不可超，配额预警的需取消预警，
	 * 如果房态为其他，如果已取消配额预警，需设为配额预警
	 * @return
	 */
	public void changeQuotaWarningByRoomstatePeriod(List templs,long hotelId){
		String roomTypeIdStr = "";
        String bedIdStr = "";
        String roomStateStr ="";
        String ableSaleDateStr = "";
        for (Iterator it = templs.iterator();it.hasNext();) {
        	RoomBedRecord record = (RoomBedRecord)it.next();
        	List dateLis = DateUtil.getDates(record.getTheStart(), record.getTheEnd());
        	//如果星期为全选
        	boolean selAllWeek=((null==record.getTheWeeks())||("".equals(record.getTheWeeks()))||(THE_WEEKS.equals(record.getTheWeeks())))?true:false;
        	if(selAllWeek){
        		for(Iterator i = dateLis.iterator();i.hasNext();){
        			Date d = (Date)i.next();
        			String[] bedIdItemStr = record.getBeds().split(",");
        			for(int j=0;j<bedIdItemStr.length;j++){
        				if(String.valueOf(BedType.BIG).equals(bedIdItemStr[j]) && null != record.getBigBedStatus()){
        					roomTypeIdStr = roomTypeIdStr + record.getTheRoomTypeId() + ",";
        					bedIdStr = bedIdStr + bedIdItemStr[j] + ",";
        					roomStateStr = roomStateStr + record.getBigBedStatus() + ",";
        					ableSaleDateStr = ableSaleDateStr + DateUtil.dateToString(d) + ",";
            				
            			}else if(String.valueOf(BedType.DOUBLE).equals(bedIdItemStr[j]) && null != record.getDoubleBedStatus()){
            				roomTypeIdStr = roomTypeIdStr + record.getTheRoomTypeId() + ",";
        					bedIdStr = bedIdStr + bedIdItemStr[j] + ",";
        					roomStateStr = roomStateStr + record.getDoubleBedStatus() + ",";
        					ableSaleDateStr = ableSaleDateStr + DateUtil.dateToString(d) + ",";
            				
            			}else if(String.valueOf(BedType.SINGLE).equals(bedIdItemStr[j]) && null != record.getSingleBedStatus()){
            				roomTypeIdStr = roomTypeIdStr + record.getTheRoomTypeId() + ",";
        					bedIdStr = bedIdStr + bedIdItemStr[j] + ",";
        					roomStateStr = roomStateStr + record.getSingleBedStatus() + ",";
        					ableSaleDateStr = ableSaleDateStr + DateUtil.dateToString(d) + ",";
            			}
        				
        			}
        			
        			
        		}
        	}//星期不是全选
        	else{
        		for(Iterator i = dateLis.iterator();i.hasNext();){
        			Date d = (Date)i.next();
        			if(DateUtil.isMatchWeek(d, record.getTheWeeks().split(","))){
        				String[] bedIdItemStr = record.getBeds().split(",");
            			for(int j=0;j<bedIdItemStr.length;j++){
            				if(String.valueOf(BedType.BIG).equals(bedIdItemStr[j]) && null != record.getBigBedStatus()){
            					roomTypeIdStr = roomTypeIdStr + record.getTheRoomTypeId() + ",";
            					bedIdStr = bedIdStr + bedIdItemStr[j] + ",";
            					roomStateStr = roomStateStr + record.getBigBedStatus() + ",";
            					ableSaleDateStr = ableSaleDateStr + DateUtil.dateToString(d) + ",";
                				
                			}else if(String.valueOf(BedType.DOUBLE).equals(bedIdItemStr[j]) && null != record.getDoubleBedStatus()){
                				roomTypeIdStr = roomTypeIdStr + record.getTheRoomTypeId() + ",";
            					bedIdStr = bedIdStr + bedIdItemStr[j] + ",";
            					roomStateStr = roomStateStr + record.getDoubleBedStatus() + ",";
            					ableSaleDateStr = ableSaleDateStr + DateUtil.dateToString(d) + ",";
                				
                			}else if(String.valueOf(BedType.SINGLE).equals(bedIdItemStr[j]) && null != record.getSingleBedStatus()){
                				roomTypeIdStr = roomTypeIdStr + record.getTheRoomTypeId() + ",";
            					bedIdStr = bedIdStr + bedIdItemStr[j] + ",";
            					roomStateStr = roomStateStr + record.getSingleBedStatus() + ",";
            					ableSaleDateStr = ableSaleDateStr + DateUtil.dateToString(d) + ",";
                			}
            			}
        			}
        		}
        	}
        	
        }
        log.info("=roomTypeIdStr======"+roomTypeIdStr);
        log.info("==bedIdStr====="+bedIdStr);
        log.info("=roomStateStr======"+roomStateStr);
        log.info("==ableSaleDateStr====="+ableSaleDateStr);
        newRoomControlDao.updateQuotaWarningByRoomState(hotelId, roomTypeIdStr, bedIdStr, roomStateStr, ableSaleDateStr);
		
		
	}
	
	public Long sendFaxForRoomcontrol(long hotelId,String faxType,String faxNum,UserWrapper roleUser, 
			NewRoomControlFaxInfo roomControlFaxInfo){
    	HtlHotel hotel = hotelManage.findHotel(hotelId);
    	roomControlFaxInfo.setHotelChnName(hotel.getChnName()== null?hotel.getEngName():hotel.getChnName());
    	
    	Fax fax = new Fax();
        fax.setXml(getNewRoomcontrolProtocol(roomControlFaxInfo));
        fax.setApplicationName("hotel");
        fax.setTemplateFileName(FaxEmailModel.HOTEL_PROTOCOLROOM_FAX);
        fax.setTo(new String[] { faxNum });
        fax.setFrom(roleUser.getLoginName());
        Long ret = communicaterService.sendFax(fax); // 需要返回一个渠道实例号

        return ret;
    }
	
	private String getNewRoomcontrolProtocol(NewRoomControlFaxInfo roomControlFaxInfo){		
		String strXML = "";
		org.dom4j.Document document = DocumentHelper.createDocument();
        org.dom4j.Element rootElement = document.addElement("roomcontrolProtocol");
        BeanUtil.SetNullDefault(roomControlFaxInfo);
        PropertyUtilsBean bean = new PropertyUtilsBean();
        PropertyDescriptor[] origDescriptors = bean.getPropertyDescriptors(roomControlFaxInfo.getClass());

        try {
            for (int j = 0; j < origDescriptors.length; j++) {
                String name = origDescriptors[j].getName();
                if ("class".equals(name)) {
                    continue;
                }
                if (bean.isReadable(roomControlFaxInfo, name)) {
                	if (name.equals("roomBedInfo")) {
                		org.dom4j.Element roomBedInfosLisEl = rootElement.addElement(name);
                		List roomBedInfoList = roomControlFaxInfo.getRoomBedInfo();
                		if(null != roomBedInfoList){
                			for(int itemNum = 0; itemNum < roomBedInfoList.size(); itemNum++){
                    			
                				FaxInfoForRoomcontrol roomControlFaxInfoItem = (FaxInfoForRoomcontrol)roomBedInfoList.get(itemNum);
                    			BeanUtil.SetNullDefault(roomControlFaxInfoItem);
                    			PropertyUtilsBean itemBean = new PropertyUtilsBean();
                    			PropertyDescriptor[] itemDescr =  itemBean.getPropertyDescriptors(roomControlFaxInfoItem.getClass());
                    			org.dom4j.Element roomBedinfoItemsEl = roomBedInfosLisEl.addElement("roomBedItemList");
                    			for(int i = 0; i < itemDescr.length ; i++){
                    				String itemName = itemDescr[i].getName();
                                    if ("class".equals(itemName)) {
                                        continue;
                                    }
                                    if (bean.isReadable(roomControlFaxInfoItem, itemName)) {
                                    	org.dom4j.Element itemEl = roomBedinfoItemsEl.addElement(itemName);
                                		Object value = bean.getProperty(roomControlFaxInfoItem, itemName);
                                		itemEl.setText(value.toString());
                                    }
                    			}
                    		}
                		}
                	}else{
                		org.dom4j.Element faxInfoEl = rootElement.addElement(name);
                		Object value = bean.getProperty(roomControlFaxInfo, name);
                		faxInfoEl.setText(value.toString());
                	}
                }
            }
        }catch (Exception e) {
        	log.error(e.getMessage(),e);
        }
        strXML = document.asXML();
        
        return strXML;
	}

	/*getter and setter*/
	public NewRoomControlDao getNewRoomControlDao() {
		return newRoomControlDao;
	}
	public void setNewRoomControlDao(NewRoomControlDao newRoomControlDao) {
		this.newRoomControlDao = newRoomControlDao;
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	public void setHotelManage(HotelManage hotelManage) {
		this.hotelManage = hotelManage;
	}

	public void setCommunicaterService(CommunicaterService communicaterService) {
		this.communicaterService = communicaterService;
	}

}
