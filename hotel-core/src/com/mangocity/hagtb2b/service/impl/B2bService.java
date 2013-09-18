package com.mangocity.hagtb2b.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.mangocity.hagtb2b.dao.IB2bOrderDao;
import com.mangocity.hagtb2b.dao.IB2bOrderIncDao;
import com.mangocity.hagtb2b.persistence.HtlB2bOrderIncrease;
import com.mangocity.hagtb2b.persistence.HtlB2bTempComminfo;
import com.mangocity.hagtb2b.persistence.HtlB2bTempComminfoItem;
import com.mangocity.hagtb2b.service.IB2bService;
import com.mangocity.hotel.base.persistence.CommisionAdjust;
import com.mangocity.hotel.base.persistence.CommisionSet;
import com.mangocity.hotel.base.service.assistant.CutDate;
import com.mangocity.hotel.order.dao.impl.OrOrderDao;
import com.mangocity.hotel.order.persistence.B2bModifyOrderInfo;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.util.bean.DateComponent;
import com.mangocity.util.hotel.constant.PayMethod;

public class B2bService implements IB2bService {
	
	private IB2bOrderDao b2bOrderDao;
	
	private IB2bOrderIncDao b2bOrderIncDao;
	
	private OrOrderDao orOrderDao;
	
	public void updateOrder(B2bModifyOrderInfo b2border) {
		b2bOrderDao.updateOrder(b2border);
	}

	/**
	 * 批量更新佣金设置公式
	 * add by yong.zeng
	 * 2010.3.16
	 * @param commLst
	 */
	public void batchUpdate(List commLst){
		b2bOrderDao.batchUpdate(commLst);
	}
	//b2b订单修改或者取消后转中台处理
	public boolean confirmToMid(long orderId, String operaterId,String state) {
        OrOrder order = b2bOrderDao.loadOrder(orderId);
        if (null == order) {
            return false;
        }
        Date now = new Date();
        String hql = "update OrOrder set hotelConfirmFax=0,hotelConfirmTel=0,"
            + "hotelConfirm=0,hotelConfirmFaxReturn=0,modifiedTime=?,toMidTime=?,isStayInMid=1 where ID=?";
        b2bOrderDao.doSqlUpdateBatch(hql, new Object[] { now,now,Long.valueOf(orderId) });
        OrHandleLog handleLog = new OrHandleLog();
        if("cancel".equals(state)){
        	handleLog.setContent("<font color ='red'>B2B取消</font>已出中台订单,转中台处理,<font color ='red'>酒店确认改为:否,已收酒店回传改为:否</font>");
        }else{
        	handleLog.setContent("<font color ='red'>B2B修改</font>已出中台订单,转中台处理,<font color ='red'>酒店确认改为:否,已收酒店回传改为:否</font>");
        }
        handleLog.setModifierRole(operaterId);
        handleLog.setModifierName(operaterId);
        handleLog.setModifiedTime(new Date());
        handleLog.setOrder(order);
        b2bOrderDao.insertObject(handleLog);
        return true;
    }
	
	/**
	 * b2b预付底价售卖假撤单处理
	 */
	public boolean cancelOrderForB2BMinPricePay(long orderId, String operaterId) {
        OrOrder order = b2bOrderDao.loadOrder(orderId);
        if (null == order) {
        	return false;
        }
        //更新b2b增幅表里面撤单状态
        HtlB2bOrderIncrease orderInc = b2bOrderIncDao.queryByOrderCD(order.getOrderCD());
        orderInc.setIsCancelForOutOfTime(1);
        b2bOrderIncDao.updateHtlB2bOrderIncrease(orderInc);
        //更新CC订单状态     注：这里的“orderstate”和OrOrder表不一样
    	B2bModifyOrderInfo b2bmodifyorderinfo = new B2bModifyOrderInfo();
		b2bmodifyorderinfo.setOrderCD(order.getOrderCD());
		b2bmodifyorderinfo.setOrderState(2);
		b2bmodifyorderinfo.setCreateDate(new Date());
		this.updateOrder(b2bmodifyorderinfo);
        OrHandleLog handleLog = new OrHandleLog();
        handleLog.setContent("<font color ='red'>B2B取消,取消原因:客人支付超时</font>转前台处理");
        handleLog.setModifierRole(operaterId);
        handleLog.setModifierName(operaterId);
        handleLog.setModifiedTime(new Date());
        order.setCancelReason(4);
        order.setGuestCancelMessage("12");
        handleLog.setOrder(order);
        b2bOrderDao.insertObject(handleLog);
        return true;
    }
	
    /**
     * 根据类名和ID来删除对象
     */
    public void remove(Class clazz, Long id){
    	b2bOrderDao.remove(clazz, id);
    }
	
	
	
	/**
	 * 
	 * 增加或更新佣金设置公式
	 * add by yong.zeng 2010-3-16
	 * @param commSet
	 */

	public void updateCommSet(CommisionSet commSet){
		b2bOrderDao.saveOrUpdate(commSet);
	}
	/**
	 * 调整佣金
	 * add by yong.zeng 2010-3-16
	 * @param commAdjust
	 */
	public void updateCommAdjust(CommisionAdjust commAdjust){
		b2bOrderDao.saveOrUpdate(commAdjust);
	}
	
	
	public IB2bOrderDao getB2bOrderDao() {
		return b2bOrderDao;
	}

	public void setB2bOrderDao(IB2bOrderDao orderDao) {
		b2bOrderDao = orderDao;
	}

	
	/**
	 * 根据ID得到CommSet
	 */
	public CommisionSet getCommSet(Long ID){

			return (CommisionSet)b2bOrderDao.find(CommisionSet.class, ID);
	}
	/**
	 * 根据ID得到ADJust
	 * @param ID
	 */
	public CommisionAdjust getCommAdjust(Long ID){
		return (CommisionAdjust)b2bOrderDao.find(CommisionAdjust.class, ID);
	}	
	
	/**
	 * 根据B2bID得到佣金设置列表
	 * @param B2bCD
	 * @return
	 */
	public List<CommisionSet> getCommSetByB2B(String B2bCD){
		
		List<CommisionSet> results =  b2bOrderDao.query("select c from CommisionSet c where b2BCd='"+B2bCD+"'");
		if(null==results ||results.isEmpty()){//如果为空就初始化
			for(int i=5;i>1;i--){
				CommisionSet setComm = new CommisionSet();
				setComm.setB2BCd(B2bCD);
				setComm.setCreateBy("system");
				setComm.setCreateById("system");
				setComm.setHotelStar(String.valueOf(i));//星级
				setComm.setSetType(2L);//=
				if(i==5){
					setComm.setValue1(9d);
				}else{
					setComm.setValue1(12d);	
				}
				setComm.setValueUnit(1L);//%
				
				results.add(setComm);
			}
			
		}
		
		return results;
	}
	
	/**
	 * 根据B2bID得到Adjust列表
	 * add by yong.zeng
	 * @param B2bID
	 * @return
	 */
		
	public List<CommisionAdjust> getCommAdjustByB2B(String B2bCD){
		String b2bStr = B2bCD.replaceAll(",", "','");
		b2bStr ="'"+b2bStr+"'";
		return b2bOrderDao.query("select c from CommisionAdjust c where b2BCd in("+b2bStr+")");
	}
	
	/**
	 * 根据B2bCd和hotelId得到Adjust列表
	 * add by yong.zeng
	 * @param B2bCD
	 * @param hotelId
	 * @return
	 */
	public List<CommisionAdjust> getCommAdjustByB2BHotel(String B2bCD,Long hotelId){
		String b2bStr = B2bCD.replaceAll(",", "','");
		b2bStr ="'"+b2bStr+"'";
		return b2bOrderDao.query("select c from CommisionAdjust c where b2BCd in ("+b2bStr+") and c.hotelId="+hotelId.longValue());
	}
	
	
	/**
	 * 根据hsql得到CommisionAdjust列表
	 * @param hsql
	 * @return
	 */
	public List<CommisionAdjust> getCommAdjustLst(String hsql){
		return b2bOrderDao.query(hsql);
	}
	
	/**
	 * 根据hotelId得到HtlB2bTempComminfoItem列表
	 * add by zhijie.gu
	 * @param hotelId
	 * @return
	 */
	public List<HtlB2bTempComminfoItem> getCommTempByHotel(Long hotelId,Long tempId){
		
		String hql = "select hi from HtlB2bTempComminfoItem hi where hi.active=1 and hi.hotelId="+hotelId+"and hi.tempId="+tempId;
		return b2bOrderDao.query(hql);
	}
	public void saveOrUpdateComTemp(HtlB2bTempComminfo htlB2bTempComminfo){
		b2bOrderDao.saveOrUpderComTemp(htlB2bTempComminfo);
	}
	

	/**
	 * 调整佣金
	 * add by zhijie.gu 2010-3-16
	 * @param commTempItem
	 */
	public void updateCommAdjust(HtlB2bTempComminfoItem commTempItem){
		b2bOrderDao.saveOrUpdate(commTempItem);
		
	}
	
	/**
     * 得到DB中已存在的日期区间
     * @param ca
     * @param b2BCD
     * @return
     */
	public List<HtlB2bTempComminfoItem> getDateCops(HtlB2bTempComminfoItem ca){
		List<HtlB2bTempComminfoItem> resultLst = null;
		if(null!=ca){
			String hsql = "select b from HtlB2bTempComminfoItem b where b.active=1 and b.tempId="+ca.getTempId()+" and b.hotelId="+ca.getHotelId() +
					" and b.roomtypeId="+ca.getRoomtypeId()+" and b.chileRoomtypeId="+ca.getChileRoomtypeId()+" and b.payMethod='"+ca.getPayMethod()+"'";
			
			resultLst  = b2bOrderDao.query(hsql);
			
		}
		
		return resultLst;
		
	}
	
    public void adjustUtil(HtlB2bTempComminfoItem addComm,List<HtlB2bTempComminfoItem> oldComms) throws IllegalAccessException, InvocationTargetException{
		
		DateComponent dateComponent = new DateComponent();
		dateComponent.setBeginDate(addComm.getBeginDate());
		dateComponent.setEndDate(addComm.getEndDate());
		List dateCops = new ArrayList();
		Map resultMap = new HashMap(); 			
		for(int ii=0; ii<oldComms.size(); ii++){
			HtlB2bTempComminfoItem curcomm = oldComms.get(ii);
			DateComponent aComponent = new DateComponent();
			aComponent.setId(curcomm.getItemId());
			aComponent.setBeginDate(curcomm.getBeginDate());
			aComponent.setEndDate(curcomm.getEndDate());
			dateCops.add(aComponent);				
		}
		resultMap = CutDate.cut(dateComponent,CutDate.sort(dateCops));
		List removeList = (List) resultMap.get("remove");
		List updateList = (List) resultMap.get("update");
		List results = new ArrayList();
		for(int jj=0; jj<removeList.size(); jj++){
			DateComponent bb = (DateComponent)removeList.get(jj);
			remove(HtlB2bTempComminfoItem.class, bb.getId());	
		}	
		 //根据拆分的时间段重新组装数据	
		boolean nullFlag = false;
		for(int i=0; i<oldComms.size(); i++){
			HtlB2bTempComminfoItem aRecord = oldComms.get(i);
			int doubleFlag = 0;
			for(int j=0; j<updateList.size(); j++){
				DateComponent dateCop = (DateComponent) updateList.get(j);				
				if(dateCop.getId()!=null){
					if(dateCop.getId().equals(aRecord.getItemId())){
						doubleFlag++;
						//如果存在多个相同的id则只有第一个id保留，其他的id都赋值为null
						if(doubleFlag>1){
							HtlB2bTempComminfoItem newRecord = new HtlB2bTempComminfoItem();
							BeanUtils.copyProperties(newRecord,aRecord);							
							newRecord.setItemId(null);
							newRecord.setBeginDate(dateCop.getBeginDate());
							newRecord.setEndDate(dateCop.getEndDate());							
							results.add(newRecord);
						}else{
							aRecord.setBeginDate(dateCop.getBeginDate());
							aRecord.setEndDate(dateCop.getEndDate());	
							results.add(aRecord);
						}
					}
				}else if(nullFlag==false){
					if(addComm.getItemId()!= null){	//处理修改情况					
						HtlB2bTempComminfoItem record = new HtlB2bTempComminfoItem();
						BeanUtils.copyProperties(record,addComm);						
						record.setItemId(null);						
						results.add(record);
					}else{//处理新增情况
						results.add(addComm);
					}
					nullFlag = true;
				}
			}		
		}	
		batchUpdate(results);
		
	}
    
    /**
     * 根据模板id获取数据
     * @param tempId
     * @return List<HtlB2bTempComminfoItem>
     */
	public List<HtlB2bTempComminfoItem> getcommTempItemByTempId(Long tempId)throws Exception{
		String hsql = "select hi from HtlB2bTempComminfoItem hi where hi.tempId="+tempId+" and hi.active=1";
		List tempComInfoItemList=b2bOrderDao.query(hsql);
		if(null !=tempComInfoItemList && !tempComInfoItemList.isEmpty()){
			for(Iterator it=tempComInfoItemList.iterator();it.hasNext();){
				HtlB2bTempComminfoItem tempComInfo = (HtlB2bTempComminfoItem)it.next();
				tempComInfo.getHotelId();
				tempComInfo.getRoomtypeId();
			}
			
		}
		
		return tempComInfoItemList;
	}
	
	/**
     * 根据模板id获取数据
     * @param tempId
     * @return List<HtlB2bTempComminfo>
     */
	public HtlB2bTempComminfo getcommTempTempId(Long tempId){
		String hsql = "select hc from HtlB2bTempComminfo hc where hc.id="+tempId+" and hc.active=1";
		return (HtlB2bTempComminfo)b2bOrderDao.queryObj(hsql);
	}
	
	/**
     * 根据模板id获取数据
     * @param tempId
     */
	public void deleteCommTempByTempId(Long tempId,String loginChnName,String loginId){

		b2bOrderDao.deleteList(tempId,loginChnName,loginId);
		
	}
	
	/**
	 * 校验模板名字是否存在
	 * @param temName
	 * @return
	 */
	public boolean checkTempNameIsExist(String temName){
		boolean isExist =false;
		String hsql = "select hc.id from HtlB2bTempComminfo hc where hc.commisionTempName ='"+temName+"' and active=1";
		List tempInfoList = b2bOrderDao.query(hsql);
		if(null!= tempInfoList && !tempInfoList.isEmpty())
			isExist=true;
		return isExist;
	}
	
	/**
	 * 填充酒店名称和价格类型名称
	 * @param hotelComTempList
	 */
	public List fillHotelNameAndPriceTypeName(List<HtlB2bTempComminfoItem> hotelComTempList){
		String priceTypeIdStr="";
		List resultLis = new ArrayList();
		for(Iterator itor = hotelComTempList.iterator();itor.hasNext();){
			HtlB2bTempComminfoItem temComInfo = (HtlB2bTempComminfoItem)itor.next();
			priceTypeIdStr=priceTypeIdStr+temComInfo.getChileRoomtypeId()+",";
		}
		priceTypeIdStr = priceTypeIdStr.substring(0, priceTypeIdStr.length()-1);
		List lis = b2bOrderDao.fillHotelNameAndPriceTypeName(priceTypeIdStr);
		Map nameMap= new HashMap();
		if(null!=lis && !lis.isEmpty()){
			String hotelId="";
			//组装map，key为priceTypeId,value为房型名称+价格类型名称和酒店名称
			for(int i=0;i<lis.size();i++){
				Object[]  obj= (Object[])lis.get(i);
				if(null !=obj[0] && null!=obj[1]){
					if(!obj[0].toString().equals(hotelId)){
						hotelId = obj[0].toString();
						nameMap.put(obj[0].toString(), obj[1].toString());
					}
				}
				if(null!=obj[2] && null !=obj[3]){
					nameMap.put(obj[2].toString(), obj[3].toString());
				}
				
			}
			for(Iterator it =hotelComTempList.iterator();it.hasNext();){
				HtlB2bTempComminfoItem temComInfo = (HtlB2bTempComminfoItem)it.next();
				temComInfo.setHotelChnName(String.valueOf(nameMap.get(String.valueOf(temComInfo.getHotelId()))));
				temComInfo.setRoomTypeName(String.valueOf(nameMap.get(String.valueOf(temComInfo.getChileRoomtypeId()))));
				resultLis.add(temComInfo);
			}
		}
		return resultLis;
	}
	/**
	 * 支持sql语句更新,返回更新记录数
	 */
	public int sqlUpdate(final String sql){
		return b2bOrderDao.doSqlUpdate(sql);
	}

	public OrOrderDao getOrOrderDao() {
		return orOrderDao;
	}

	public void setOrOrderDao(OrOrderDao orOrderDao) {
		this.orOrderDao = orOrderDao;
	}

	public void setB2bOrderIncDao(IB2bOrderIncDao b2bOrderIncDao) {
		this.b2bOrderIncDao = b2bOrderIncDao;
	}

	/**
	 * 根据代理商code得到享受政策范围
	 * add by yong.zeng
	 * @param agentcode
	 * @return
	 */
	public int getPolicyScope(String agentcode){
		int result  = 0;
		String sql = "select c.policyscope from b2b_organizaioninfo c where c.agentcode='"+agentcode+"' and c.STATUS=1";
		List tempLst  = orOrderDao.queryByNativeSQL(sql,new Object[]{});
		if(null!=tempLst&& !tempLst.isEmpty()){
			result = Integer.parseInt(tempLst.get(0).toString());
		}
		return result;
	}

	/**
	 * 判断是否设置了代理佣金
	 * @param b2bCD
	 * @param hotelStar
	 * @param abselDate
	 * @param hotelId
	 * @param roomTypeID
	 * @param priceTypeId
	 * @param payMethod
	 * @return
	 */
	public boolean isSetCommission(String b2bCD,int hotelStar,Date abselDate, Long hotelId,
			Long roomTypeID, Long priceTypeId,String payMethod){
		boolean result = false,setResult = false,adjustResult = false;

		//如果是面转预,也是按面付的佣金
		String payType = payMethod;
		if(PayMethod.CONVERSION.equals(payType)){
			payType = PayMethod.PAY;
		}
		
		//取出佣金设置
		List<CommisionSet> commLst = orOrderDao.queryByNamedQuery("queryHtlB2bCommset", new Object[] {b2bCD,String.valueOf(hotelStar)});
		//取出佣金调整
		List<CommisionAdjust> adjustLst = orOrderDao.queryByNamedQuery("queryHtlB2bCommAdjust", new Object[] {b2bCD,new Long(hotelId),
				new Long(roomTypeID),new Long(priceTypeId),payType,String.valueOf(hotelStar),abselDate,abselDate});
		
		if(commLst!=null &&!commLst.isEmpty()){//判断设置
			setResult = true;
			CommisionSet comm = commLst.get(0);
				if(hotelStar==5){//如果5星级的佣金设置是等于9%的,按不设置处理.如果其它星级的佣金设置是等于12%的,按不设置处理
					if(comm.getValue1()==9.0 &&comm.getValueUnit()==1 &&comm.getSetType()==2){
						setResult = false;
					}
				}else{
					if(comm.getValue1()==12.0 &&comm.getValueUnit()==1 &&comm.getSetType()==2){
						setResult = false;
					}
				}
		}

		if(adjustLst!=null&&!adjustLst.isEmpty()){//判断调整
			adjustResult = true;
			CommisionAdjust comm = adjustLst.get(0);
				if(hotelStar==5){//如果5星级的佣金设置是等于9%的,按不设置处理.如果其它星级的佣金设置是等于12%的,按不设置处理
					if(comm.getComm_value()==9.0 &&comm.getValueType()==1){
						adjustResult = false;
					}
				}else{
					if(comm.getComm_value()==12.0 &&comm.getValueType()==1){
						adjustResult = false;
					}
				}
		}
		
		if(setResult||adjustResult)result = true;
		return result;
	}
	/**
	 * 根据hotelId查找佣金调整list
	 * add by luoguangming
	 * @param hotelId
	 * @return
	 */
	public List<CommisionAdjust> getAllCommAdjustByHotelId(Long hotelId){
		return b2bOrderDao.query("select c from CommisionAdjust c where c.b2BCd='0' and c.hotelId="+hotelId.longValue()+" order by c.roomTypeId asc,c.childRoomId asc,c.payType asc,c.createDate desc");
	}
	/**
	 * 根据hotelId查找佣金调整list，去重复，只去每个价格类型的最新调整
	 * add by luoguangming
	 * @param hotelId
	 * @return
	 */
	public List<CommisionAdjust> getDistinctCommAdjustByHotelId(Long hotelId){
		 return orOrderDao.getCommisionAdjustByHotelIds(hotelId.toString());
	}
}
