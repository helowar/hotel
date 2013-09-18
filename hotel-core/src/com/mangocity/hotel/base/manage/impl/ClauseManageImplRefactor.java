package com.mangocity.hotel.base.manage.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.mangocity.hotel.base.constant.HotelCalcuAssuAmoType;
import com.mangocity.hotel.base.dao.HtlRoomtypeDao;
import com.mangocity.hotel.base.dao.IClauseDaoRefactor;
import com.mangocity.hotel.base.manage.ClauseManage;
import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.persistence.HtlBookCaulClause;
import com.mangocity.hotel.base.persistence.HtlBookModifyField;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlFavourableDecrease;
import com.mangocity.hotel.base.persistence.HtlPopedomControl;
import com.mangocity.hotel.base.persistence.HtlPreconcertItemTemplet;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.service.IHotelFavourableReturnService;
import com.mangocity.hotel.base.service.assistant.CutDate;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.DateComponent;
import com.mangocity.util.hotel.constant.PayMethod;

/**
 */
public class ClauseManageImplRefactor implements ClauseManage {

    
    private IClauseDaoRefactor clauseDao;//引用Dao
    private HtlRoomtypeDao roomtypeDao;//引用房型dao
    private IHotelFavourableReturnService returnService;
    private ContractManage contractManage;
    /**
     * 考虑到单例,将两个全局成员变量删除
     */

/*    private List<HtlContract> contractList;

    private List<HtlPreconcertItemTemplet> clausesList;*/

    // 根据酒店ID获得其合同列表；
    public List<HtlContract> searchContactByHTlID(Long hotelID) {

        //修改clist定义方式
        return clauseDao.getContracts(hotelID);
        //这段多余
/*
        contractList = new ArrayList<HtlContract>();
        for (int i = 0; i < clist.size(); i++) {

            HtlContract htct = (HtlContract) clist.get(i);

            contractList.add(htct);

        }

        return contractList;*/
        //return clist;

    }
    public Map<String, ?> getContactContent(Long hotelId) {
    	List<HtlContract> list=clauseDao.getContracts(hotelId);
    	Date tDate = new Date();
    	Map<String,Object> dateMap=new HashMap<String,Object>();
    	dateMap.put("ROOM_TYPE_LIST",lstRoomTypeByHotelId(hotelId));
    	for(HtlContract contract:list){
    		if(tDate.after(contract.getBeginDate())&&tDate.before(contract.getEndDate())){
    			dateMap.put("BEGIN_DATE", contract.getBeginDate());
    			dateMap.put("END_DATE", contract.getEndDate());
    			break;
    		}
    	}
		return dateMap;
	}

    // 根据酒店ID得到其预定条款模板列表；
    public List<HtlPreconcertItemTemplet> searchClasesByHTLID(Long hotelID) {
//此方法同上

        return  clauseDao.getClauses(hotelID);

/*        clausesList = new ArrayList<HtlPreconcertItemTemplet>();

        for (int i = 0; i < cclist.size(); i++) {

            HtlPreconcertItemTemplet hpit = (HtlPreconcertItemTemplet) cclist.get(i);
            clausesList.add(hpit);

        }

        return clausesList;
*/    
    //return cclist;    
    }

   
    // 根据酒店ID得到起预定条款计算规则信息 
    public List<HtlBookCaulClause> searchBookCaulByHTLID(Long hotelID) {

        //同上

        return clauseDao.getClauseRules(hotelID);

        //return bclist;
    }

    /**
     * 
     * 保存预订条款计算规则 如果有重复，则覆盖时间段，有可能拆分时间段 
     * 
     * @param htlBookCaulClause
     * @param user
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public long createBookRules(HtlBookCaulClause htlBookCaulClause, UserWrapper user)
        throws IllegalAccessException, InvocationTargetException {

          /**
         * 查询出来有以下情况 查询出的结果1 |___________| 查询出的结果2,3,4 |_____| |___| |_______| 查询出的结果5
         * |________________| 本对象 |___________|
         */
        //改为dao方式，并用泛型
        List<HtlBookCaulClause> tempListForBothSides = clauseDao.getBookCaulClauses(htlBookCaulClause.getHotelId());
        
        if (tempListForBothSides.isEmpty()) {//判断方式修改; 如果记录不存在，直接新增或更新该记录
        	htlBookCaulClause.setModifyTime(new Date());
            if (null != user) {//user为null必须判断，这是历史问题
                htlBookCaulClause.setModifier(user.getName());
            }
            clauseDao.save(htlBookCaulClause);
            //super.saveOrUpdate(htlBookCaulClause);
        }else{// 如果记录存在，进行拆分
            boolean flag = false;
            if (1 < tempListForBothSides.size()) {
                List<DateComponent> dateCopList = new ArrayList<DateComponent>();//泛型
                // 把所有查询出来的记录的ID，开始结束日期，修改时间放到List中
                //修改for循环方式
                for ( HtlBookCaulClause bookCaulClauseForSingle:tempListForBothSides) {
                    
                    DateComponent dateCop = new DateComponent();
                    dateCop.setId(bookCaulClauseForSingle.getId());
                    dateCop.setBeginDate(bookCaulClauseForSingle.getClauseRuleBeginDate());
                    dateCop.setEndDate(bookCaulClauseForSingle.getClauseRuleEndDate());
                    dateCop.setModifyDate(bookCaulClauseForSingle.getModifyTime());
                    dateCopList.add(dateCop);
                }
                
                // 如果存在重叠，则要拆分
                if (CutDate.compareConflict(dateCopList)) {//判断日期是否出现重叠
                    for ( HtlBookCaulClause bookCaulClauseForSingle:tempListForBothSides) {
                        //HtlBookCaulClause bookCaulClauseForSingle = (HtlBookCaulClause) itForSingleSide.next();//没必要定义
                        // 重新查出最新的结果
/*                        List subList = super.doquery(hqlForBothSides,
                            new Object[] { htlBookCaulClause.getHotelId() }, false);*/
                        //没必要定义subList
                        // 拆分计算规则时间段
                        splitBookCaulClause(user, bookCaulClauseForSingle, clauseDao.getBookCaulClauses(htlBookCaulClause.getHotelId()));
                    }
                    flag = true;
                }
            }

            // 重新查出最新的结果
            if (flag) {
                tempListForBothSides = clauseDao.getBookCaulClauses(htlBookCaulClause.getHotelId());//dao改进
            }
            // 拆分计算规则时间段
            splitBookCaulClause(user, htlBookCaulClause, tempListForBothSides);
        }
        

        return 0;
    }

    /**
     * 拆分计算规则时间段
     * 
     * @param user
     * @param bookCaulClauseForSingle
     * @param subList
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @SuppressWarnings("unchecked")
	private void splitBookCaulClause(UserWrapper user, HtlBookCaulClause bookCaulClauseForSingle,
        List<HtlBookCaulClause> subList) throws IllegalAccessException, InvocationTargetException {
        DateComponent dateComponent = new DateComponent();
        dateComponent.setBeginDate(bookCaulClauseForSingle.getClauseRuleBeginDate());
        dateComponent.setEndDate(bookCaulClauseForSingle.getClauseRuleEndDate());
        //日期比较
        List<DateComponent> dateCops = new ArrayList<DateComponent>();//泛型
        
        
        
        //通过for追加到dateCops里
        for (HtlBookCaulClause subBookCaulClause:subList) {
            DateComponent aComponent = new DateComponent();
            aComponent.setId(subBookCaulClause.getId());
            aComponent.setBeginDate(subBookCaulClause.getClauseRuleBeginDate());
            aComponent.setEndDate(subBookCaulClause.getClauseRuleEndDate());
            dateCops.add(aComponent);
        }
        //根据日期，得到需要删除和更新的时间区间
        //Map<String,DateComponent> resultMap = new HashMap<String,DateComponent>();//泛型
        Map<String,DateComponent>  resultMap = CutDate.cut(dateComponent, CutDate.sort(dateCops));
        List<DateComponent> removeList = (List<DateComponent>) resultMap.get("remove");
        List<DateComponent> updateList = (List<DateComponent>) resultMap.get("update");
        
        String[] idArr = new String[removeList.size()];
        for (int jj = 0; jj < removeList.size(); jj++) {
            //DateComponent bb = (DateComponent) removeList.get(jj);没必要定义
        	//clauseDao.remove(HtlBookCaulClause.class, removeList.get(jj).getId());//删除移出来
        	idArr[jj]=String.valueOf(removeList.get(jj).getId().longValue());
        }
        clauseDao.remove(idArr, "HtlBookCaulClause");//批量删除
        // 根据拆分的时间段重新组装数据
        List<HtlBookCaulClause> results = new ArrayList<HtlBookCaulClause>();
        boolean nullFlag = false;
        
        String modifier = "";
	     if (null != user) {//移出for,不在循环里判断
	    	 modifier = user.getName();
	    }

        for (HtlBookCaulClause aRecord:subList) {
            int doubleFlag = 0;
            for (DateComponent dateCop:updateList) {
                if (null != dateCop.getId()) {
                    if (dateCop.getId().equals(aRecord.getID())) {
                        doubleFlag++;
                        // 如果存在多个相同的id则只有第一个id保留，其他的id都赋值为null
                        if (1 < doubleFlag) {
                            HtlBookCaulClause newRecord = new HtlBookCaulClause();
                            BeanUtils.copyProperties(newRecord, aRecord);
                            newRecord.setId(null);
                            newRecord.setClauseRuleBeginDate(dateCop.getBeginDate());
                            newRecord.setClauseRuleEndDate(dateCop.getEndDate());
                            newRecord.setModifyTime(new Date());
                            
/*                            if (null != user) {//判断移出去
                                newRecord.setModifier(user.getName());
                            }*/
                            newRecord.setModifier(modifier);
                            results.add(newRecord);
                        } else {
                            aRecord.setClauseRuleBeginDate(dateCop.getBeginDate());
                            aRecord.setClauseRuleEndDate(dateCop.getEndDate());
                            aRecord.setModifyTime(new Date());
   /*                         if (null != user) {
                                aRecord.setModifier(user.getName());
                            }*/
                            aRecord.setModifier(modifier);
                            results.add(aRecord);
                        }
                    }
                } else if (!nullFlag) {
                    if (null != bookCaulClauseForSingle.getID()) { // 处理修改情况
                        HtlBookCaulClause record = new HtlBookCaulClause();
                        BeanUtils.copyProperties(record, bookCaulClauseForSingle);
                        record.setId(null);
                        record.setModifyTime(new Date());
                        record.setModifier(modifier);
                        results.add(record);
                    } else {// 处理新增情况
                        results.add(bookCaulClauseForSingle);
                    }
                    nullFlag = true;
                }
            }
        }
        clauseDao.saveOrUpdateAll(results);
    }

    /**
     * 根据酒店id查询修改字段定义，返回一条记录
     * 
     * @param hotelID
     * @return
     */
    public HtlBookModifyField searchBookModifyFieldByHTLID(Long hotelID) {
    	//dao改造
        List<HtlBookModifyField> htlBookModifyFieldList = clauseDao.getBookModifyField(hotelID);
        if (htlBookModifyFieldList.isEmpty()){
        	return new HtlBookModifyField();//为保持原有逻辑
        } 
        return htlBookModifyFieldList.get(0);
    }

    /**
     * 添加修改字段定义
     * 
     * @param htlBookModifyField
     */
    public void createModifyField(HtlBookModifyField htlBookModifyField) {
    	clauseDao.saveOrUpdate(htlBookModifyField);
    }

    //public void saveOrUpdateAll(List objList) {
//        super.saveOrUpdateAll(objList);
//    }


    /**
     * 检查有没有需要删除的记录，有则删除
     * 
     * @param oldList
     * @param newList
     */
    public void removeOldBookCaulClause(List<HtlBookCaulClause> oldList,
        List<HtlBookCaulClause> newList) {
        // 检查有没有删除的记录，有则真删除
        if (null == oldList || oldList.isEmpty()) return;
        
        List<HtlBookCaulClause> removeList = new ArrayList<HtlBookCaulClause>(0);
        for (HtlBookCaulClause oldObj:oldList) {
            //HtlBookCaulClause oldObj = oldList.get(i);
            boolean bRemove = true;// 标志是否需要删除
            for (HtlBookCaulClause newObj:newList) {
                //HtlBookCaulClause newObj = newList.get(j);
                if (null != newObj.getId()) {
                    if (newObj.getId().equals(oldObj.getId())) {
                        bRemove = false;
                        break;
                    }
                }
            }
            if (bRemove) {
                removeList.add(oldObj);
            }

        }
            // 删除
        
            if (!removeList.isEmpty()) {
            	String[] idArr = new String[removeList.size()];
                for (int i=0;i<removeList.size();i++) {//删除方式修改,不在for里删除
                	
                	idArr[i]=String.valueOf(removeList.get(i).getId().longValue());
                }
                
                clauseDao.remove(idArr, "HtlBookCaulClause");//改为批量删除
                
            }
 
    }

    /**
     * 按酒店id和时间段查询计算规则，如果没有计算规则，默认累加判定
     * 
     * @param hotelID
     * @param checkInDate
     * @param checkOutDate
     * @return
     */
    public String drawoutHtlBookCaulClause(List<HtlBookCaulClause> list) {
        String clauseRule = "";
        /**
         * 用于统计优先级最高的计算规则 clauseRuleTemplate[0]存放 按check in day判定的值:1 clauseRuleTemplate[1]存放
         * 按全额判定的值:2 clauseRuleTemplate[2]存放 按累加判定的值:3
         */
        String[] clauseRuleTemplate = new String[] { "", "", "" };
        if (null == list || list.isEmpty()) return "3";
        
            if (1 ==list.size()) {
            	 clauseRule = list.get(0).getClauseRule();
            }else
            	
            {
                // 统计所有计算规则存在的情况
                for (Iterator<HtlBookCaulClause> it = list.iterator(); it.hasNext();) {
                    HtlBookCaulClause htlBookCaulClause = it.next();
                    if (StringUtil.isValidStr(htlBookCaulClause.getClauseRule())) {
                        // 按check in day判定
                        if (htlBookCaulClause.getClauseRule().equals(
                            String.valueOf(HotelCalcuAssuAmoType.CHECKIN))) {
                            clauseRuleTemplate[0] = String.valueOf(HotelCalcuAssuAmoType.CHECKIN);
                        }
                        // 按全额判定
                        else if (htlBookCaulClause.getClauseRule().equals(
                            String.valueOf(HotelCalcuAssuAmoType.ALLAMOUNT))) {
                            clauseRuleTemplate[1] = String.valueOf(HotelCalcuAssuAmoType.ALLAMOUNT);
                        }
                        // 按累加判定
                        else if (htlBookCaulClause.getClauseRule().equals(
                            String.valueOf(HotelCalcuAssuAmoType.TOTTINGUP))) {
                            clauseRuleTemplate[2] = String.valueOf(HotelCalcuAssuAmoType.TOTTINGUP);
                        }
                    }
                }
                
                // 如果clauseRuleTemplate[1]有值则返回按全额判定
                if (StringUtil.isValidStr(clauseRuleTemplate[1])) {
                    clauseRule = clauseRuleTemplate[1];
                }
                // 如果clauseRuleTemplate[2]有值则返回按累加判定
                else if (StringUtil.isValidStr(clauseRuleTemplate[2])) {
                    clauseRule = clauseRuleTemplate[2];
                }
                // 如果clauseRuleTemplate[0]有值则返回按check in day判定
                else if (StringUtil.isValidStr(clauseRuleTemplate[0])) {
                    clauseRule = clauseRuleTemplate[0];
                }
                // 其他默认累加
                else {
                    clauseRule = String.valueOf(HotelCalcuAssuAmoType.TOTTINGUP);
                }
            } 
        
        return clauseRule;
    }

    /**
     * 取出计算规则中最严格的计算规则
     * 
     * @param list
     * @return
     */
    public List<HtlBookCaulClause> searchBookCaulByDateRange(Long hotelID, Date checkInDate,
        Date checkOutDate) {
    	//放到Dao去实现
/*        String hql = " from HtlBookCaulClause where hotelId = ? "
            + " and ((clauseRuleBeginDate <= trunc(?) and clauseRuleEndDate >= trunc(?))"
            + " or (clauseRuleBeginDate <= trunc(?) and clauseRuleEndDate >= trunc(?)))";
        // 离店日期-1
        Date checkOurDateBefore = DateUtil.getDate(checkOutDate, -1);
        Object[] params = new Object[5];
        params[0] = hotelID;
        params[1] = checkInDate;
        params[2] = checkInDate;
        params[3] = checkOurDateBefore;
        params[4] = checkOurDateBefore;
        return super.doquery(hql, params, false);*/
    	
    	return clauseDao.getBookCaulByDateRange(hotelID,checkInDate,checkOutDate);
    }
    
    /**
     * 取出对应酒店的所有的优惠立减条款
     * 
     */
    public List<HtlFavourableDecrease> queryAllavourableclause(long hotelId) {
		
		/*List lstFavDecrease = new ArrayList();
		
		String sql = " select  a  from HtlFavourableDecrease a   where " +
		 " a.hotelId=? and a.endDate >= trunc(sysdate) order by a.modifyTime desc";
		Object[] obj = new Object[] {hotelId};
		lstFavDecrease=(List)super.query(sql, obj);
		
		return lstFavDecrease;*/
    	return clauseDao.getAllavourableclause(hotelId);
	}
    
    /**
     * 取出对应酒店价格类型优惠立减条款
     * 
     */
    public List queryFavourableclauseForPriceTypeId(long hotelId,long priceTypeId) {
		
/*		List lstFavDecrease = new ArrayList();
		
		String sql = " select  a  from HtlFavourableDecrease a   where " +
		 " a.hotelId=?  and a. priceTypeId=? and a.endDate >= trunc(sysdate) order by a.modifyTime desc";
		Object[] obj = new Object[] {hotelId,priceTypeId};
		lstFavDecrease=(List)super.query(sql, obj);
		
		return lstFavDecrease;*/
    	return clauseDao.getFavourableclauseForPriceTypeId(hotelId, priceTypeId);
	}
    /**
     * 列出一个酒店的所有房型
     * 
     * @param hotelId
     *            酒店id
     * @return roomtype 的 List;
     */
    public List<HtlRoomtype> lstRoomTypeByHotelId(long hotelId) {
        /*String hsql = "from HtlRoomtype where hotel_id =?";
        List lsRoomType = new ArrayList();
        lsRoomType = super.query(hsql, hotelId);
        return lsRoomType;*/
    	//移到HtlRoomtypeDaoImpl中
    	return roomtypeDao.lstHotelRoomType(hotelId);
    }
    /**
     * 新增优惠立减
     * 
     * @param hotelId
     *            酒店id
     * @return roomtype 的 List;
     */
    public Long createFavourableDecrease(HtlFavourableDecrease htlFavourableDecrease) throws IllegalAccessException, InvocationTargetException  {
		List<HtlFavourableDecrease> oldFavourableDecrease = clauseDao.getFavourableDecreaseOrder(htlFavourableDecrease.getHotelId(),htlFavourableDecrease.getPriceTypeId());
		//if(oldFavourableDecrease==null||oldFavourableDecrease.size()==0){
		if(oldFavourableDecrease.isEmpty()){
			clauseDao.save(htlFavourableDecrease);
		}else{
			boolean flag = false;
			/////////////////////////////////////////////////////
            //如果数据库有超过1条的记录，则先判断时间段是否出现重叠再进行拆分
			if(oldFavourableDecrease.size()>1){
				List<DateComponent> dateCopList = new ArrayList<DateComponent>();
				for(HtlFavourableDecrease fDecrease:oldFavourableDecrease){
					DateComponent dateCop = new DateComponent();
					//HtlFavourableDecrease fDecrease = oldFavourableDecrease.get(i);
					dateCop.setId(fDecrease.getId());
					dateCop.setBeginDate(fDecrease.getBeginDate());
					dateCop.setEndDate(fDecrease.getEndDate());
					dateCop.setWeeks(fDecrease.getWeek());
					dateCop.setModifyDate(fDecrease.getModifyTime());
					dateCopList.add(dateCop);
				}
				
				if(CutDate.compareConflictWithWeek(dateCopList)){//如果存在重叠，则要拆分
					
					for(HtlFavourableDecrease subFavDecrease:oldFavourableDecrease){
						//HtlFavourableDecrease subFavDecrease = oldFavourableDecrease.get(j);
						favClauseUtil(subFavDecrease, clauseDao.getSubFavDecrease(subFavDecrease.getHotelId(),subFavDecrease.getPriceTypeId(),subFavDecrease.getModifyTime()));
					}
					
					flag = true;
					
				}
				
			}
			///////////////////////////////////////////////////
			if(flag==true){
				oldFavourableDecrease = clauseDao.getFavourableDecreaseOrder(htlFavourableDecrease.getHotelId(),htlFavourableDecrease.getPriceTypeId());	
			}
			favClauseUtil(htlFavourableDecrease, oldFavourableDecrease);
			
		}
		
		return htlFavourableDecrease.getId();
	}
    
    /**
	 * 优惠立减拆分日期的辅助类  
	 * @param htlFavourableDecrease
	 * @param oldFavourableDecreaseList
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void favClauseUtil(HtlFavourableDecrease htlFavourableDecrease,List oldFavourableDecrease)throws IllegalAccessException, InvocationTargetException{
		
		DateComponent dateComponent = new DateComponent();
		
		dateComponent.setBeginDate(htlFavourableDecrease.getBeginDate());
		dateComponent.setEndDate(htlFavourableDecrease.getEndDate());
		dateComponent.setWeeks(htlFavourableDecrease.getWeek());
		
		List<DateComponent> dateCops = new ArrayList<DateComponent>();//泛型
		Map<String,DateComponent> resultMap = new HashMap<String,DateComponent>(); 	//泛型
		
		for(int ii=0;ii<oldFavourableDecrease.size();ii++){
			
			HtlFavourableDecrease favDecreaseII = (HtlFavourableDecrease)oldFavourableDecrease.get(ii);
			DateComponent aComponent = new DateComponent();
			aComponent.setId(favDecreaseII.getId());
			aComponent.setBeginDate(favDecreaseII.getBeginDate());
			aComponent.setEndDate(favDecreaseII.getEndDate());
			aComponent.setWeeks(favDecreaseII.getWeek());
			dateCops.add(aComponent);			
			
		}
		
		
		resultMap = CutDate.cutWithWeek(dateComponent,CutDate.sort(dateCops,htlFavourableDecrease.getBeginDate(),htlFavourableDecrease.getEndDate(),htlFavourableDecrease.getWeek()));
		
		List<DateComponent> removeList = (List<DateComponent>) resultMap.get("remove");
		List<DateComponent> updateList = (List<DateComponent>) resultMap.get("update");
		
		List<HtlFavourableDecrease> results = new ArrayList<HtlFavourableDecrease>();
		
		String[] idArr = new String[removeList.size()];
		for(int jj=0; jj<removeList.size(); jj++){
			DateComponent bb = (DateComponent)removeList.get(jj);
			//super.remove(HtlFavourableDecrease.class, bb.getId());//不要在for里删除	
			idArr[jj] = String.valueOf(bb.getId().longValue()); 
		}
		clauseDao.remove(idArr, "HtlFavourableDecrease");//批量删除
		
		 //根据拆分的时间段重新组装数据	
		boolean nullFlag = false;
		for(int i=0; i<oldFavourableDecrease.size(); i++){
			
			HtlFavourableDecrease favDecreaseObj= (HtlFavourableDecrease) oldFavourableDecrease.get(i);
			int doubleFlag = 0;
			for(DateComponent dateCop:updateList){
				//DateComponent dateCop = (DateComponent) updateList.get(j);				
				if(dateCop.getId()!=null){
					if(dateCop.getId().equals(favDecreaseObj.getId())){
						doubleFlag++;
						//如果存在多个相同的id则只有第一个id保留，其他的id都赋值为null
						if(doubleFlag>1){
							HtlFavourableDecrease newFavDecreaseObj= new HtlFavourableDecrease();
							BeanUtils.copyProperties(newFavDecreaseObj,favDecreaseObj);
							
							//List lstNewParameterObj = new ArrayList();//没用
							//HtlFavourableDecrease newParameterObj = new HtlFavourableDecrease();
							
							newFavDecreaseObj.setId(null);
							newFavDecreaseObj.setBeginDate(dateCop.getBeginDate());
							newFavDecreaseObj.setEndDate(dateCop.getEndDate());							
							results.add(newFavDecreaseObj);
							
						}else{
							favDecreaseObj.setBeginDate(dateCop.getBeginDate());
							favDecreaseObj.setEndDate(dateCop.getEndDate());	
							results.add(favDecreaseObj);
						}
					}	
				}else if(nullFlag==false){
					if(htlFavourableDecrease.getId()!= null){	//处理修改情况		
						
						HtlFavourableDecrease favDecreaseUpt = new HtlFavourableDecrease();
						BeanUtils.copyProperties(favDecreaseUpt,htlFavourableDecrease);
						
//						List lstUptNewParameterObj = new ArrayList();//删除
						//HtlFavouraParameter newUptParameterObj = new HtlFavouraParameter();
						
						favDecreaseUpt.setId(null);
						results.add(favDecreaseUpt);
						
					}else{//处理新增情况
						results.add(htlFavourableDecrease);
					}
					nullFlag = true;
					
				}
			}
		}
		
		clauseDao.saveOrUpdateAll(results);//批量更新
		
	}
	
	/**
	 * 根据价格类型id，获得价格类型的名称 
	 * @param priceTypeID
	 * @return
	 */
	public String priceTypeName(long priceTypeID) {
		

		HtlRoomtype roomtype = clauseDao.getHtlRoomTypeByPriceTypeId(priceTypeID);//用dao取出
		if(null==roomtype) return "";//为null表示没有此价格类型
		
		List<HtlPriceType>  lstPriceType = roomtype.getLstPriceType();
		
		
		StringBuffer sbuffer = new StringBuffer();//改成StringBuffer
		int  j=0;
		for(HtlPriceType  priceTypeObj:lstPriceType){
			/**
			 * 问题：取价格类型名称,为什么要取出HtlRoomtype，并且对priceType进行for循环??
			 * 直接取　HtlPriceType不就ok了嘛．业务逻辑暂不讨论
			 */
			//HtlPriceType  priceTypeObj = lstPriceType.get(i);//价格类型ID
			
			if(priceTypeObj.getID().longValue()==priceTypeID)//priceTypeObj.getID()是Long对象,priceTypeID是long,不能用==
			{	sbuffer.append(j>0?",":"");
				sbuffer.append(roomtype.getRoomName());
				sbuffer.append("(");
				sbuffer.append(priceTypeObj.getPriceType());
				sbuffer.append(")");
				//priceTypeStr+=(j>0?",":"")+roomtype.getRoomName()+"("+priceTypeObj.getPriceType()+")";
				j++;
			}
		}
		
		return sbuffer.toString();
	}
	
	/**
	 * hotel 2.9.3
	 * 
	 * 修改一个立减优惠的信息
	 * @param favourableDecrease
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public long modifyFavClause(HtlFavourableDecrease favourableDecrease) throws IllegalAccessException, InvocationTargetException {
		
		//如果优惠立减不存在，就先将favourableDecrease Insert到Database		
		if(null==favourableDecrease.getId()||0==favourableDecrease.getId().longValue()){
			createFavourableDecrease(favourableDecrease);
		}else{
			//泛型
			List<HtlFavourableDecrease> oldFavourableDecrease = clauseDao.getFavourableDecreaseOrder(favourableDecrease.getHotelId(), favourableDecrease.getPriceTypeId());	
			
			boolean flag = false;

            //如果数据库有超过1条的记录，则先判断时间段是否出现重叠再进行拆分
			if(oldFavourableDecrease.size()>1){
				List<DateComponent> dateCopList = new ArrayList<DateComponent>();
				for(HtlFavourableDecrease aFavDecrease:oldFavourableDecrease){
					DateComponent dateCop = new DateComponent();
					//HtlFavourableDecrease aFavDecrease = oldFavourableDecrease.get(i);
					dateCop.setId(aFavDecrease.getId());
					dateCop.setBeginDate(aFavDecrease.getBeginDate());
					dateCop.setEndDate(aFavDecrease.getEndDate());
					dateCop.setModifyDate(aFavDecrease.getModifyTime());
					dateCop.setWeeks(aFavDecrease.getWeek());
					dateCopList.add(dateCop);
				}
				if(CutDate.compareConflictWithWeek(dateCopList)){//如果存在重叠，则要拆分
					for(HtlFavourableDecrease subFavDecrease:oldFavourableDecrease){
						/**
						 * 问题同上
						 */
						//HtlFavourableDecrease subFavDecrease = (HtlFavourableDecrease)oldFavourableDecrease.get(j);
						List<HtlFavourableDecrease> subList = clauseDao.getSubFavDecrease(subFavDecrease.getHotelId(),subFavDecrease.getPriceTypeId(),subFavDecrease.getModifyTime());
						favClauseUtil(subFavDecrease,subList);
					}
					flag = true;
				}
			}
			///////////////////////////////////////////////////
			if(flag==true){
				oldFavourableDecrease = clauseDao.getFavourableDecreaseOrder(favourableDecrease.getHotelId(), favourableDecrease.getPriceTypeId());
				
			}
			favClauseUtil(favourableDecrease,oldFavourableDecrease);
		}
		return 1;	//??返回1有什么意义吗	
		
	}
	
	/**
	 * 根据ID查询对应的立减优惠条款 
	 * 
	 */
	public HtlFavourableDecrease getFavDecreaseById(long id) {
		
/*			String sql = " select  a  from HtlFavourableDecrease a   where " +
			 " a.id=? ";
			
			Object[] obj = new Object[] {id};
			
			HtlFavourableDecrease favDecrease=(HtlFavourableDecrease)super.find(sql, obj);
			
			return favDecrease;
*/
		return clauseDao.getFavDecreaseById(id);
	}
	
	/**
	 * 删除立减优惠
	 * @param favourableDecrease
	 * @return
	 */
	public long deleteFavDecreaseObj(HtlFavourableDecrease favourableDecrease) {
		//super.remove(favourableDecrease);
		clauseDao.remove(HtlFavourableDecrease.class, favourableDecrease.getId());//新DAO里不支持remove object,要根据id来删除
		return favourableDecrease.getId();//不需要返回
	}
	
	
	/**
	 * 获取权限表里数据 
	 * @return
	 */
	public List getAllPopedomList(){

	/*		List popedomControlLis = new ArrayList();
			String sql = " select  a  from HtlPopedomControl a";
			List popedomControlLis= clauseDao(sql);
			return popedomControlLis ;*/
			return clauseDao.getAllPopedomList();
		
	}
	
	public Map<String,?> getPopedoms(String popedomControlType, String loginName,Long hotelId) {
		
		List<HtlPopedomControl> list=clauseDao.getPopedoms(popedomControlType,loginName);
		Map resultMap=new HashMap();
		for(HtlPopedomControl popedom :list){
			resultMap.put("FAVOURABLE_RETURN_LIST",returnService.queryAllFavourableReturn(hotelId));
			resultMap.put("CURRENCY", contractManage.queryCurrentContractByHotelId(hotelId).getCurrency());
		}
		return resultMap;
	}
	
	public List<HtlPopedomControl> getPopedomListByLoginName(String loginName) {
		return clauseDao.getPopedomListByLoginName(loginName);
		
	}
	
	public long getEleDayPriceNum(long hotelId,long priceTypeIte,Date beginD,Date endD){
		
		/*String sql = "select count(*) from HtlPrice a where a.hotelId=? and a.childRoomTypeId=?"
				+ " and a.ableSaleDate>=? and a.ableSaleDate<=? and a.payMethod='pre_pay'";
		log.info(sql);
		Object[] obj = new Object[] {hotelId,priceTypeIte,beginD,endD};
		
		Long eleDayPriceNum=(Long)super.find(sql, obj);
		
		
		return eleDayPriceNum.longValue();*/
		return clauseDao.getEleDayPriceNum(hotelId,priceTypeIte,beginD,endD,PayMethod.PRE_PAY);
		
	}
	
	/**
	 * 一个房型在相应时间段内的面付个数
	 * @param hotelId
	 * @param priceTypeIte
	 * @param beginD
	 * @param endD
	 * @return
	 */
	public long getEleDayPayPriceNum(long hotelId,long priceTypeIte,Date beginD,Date endD){
		
		/*String sql = "select count(*) from HtlPrice a where a.hotelId=? and a.childRoomTypeId=?"
				+ " and a.ableSaleDate>=? and a.ableSaleDate<=? and a.payMethod='pay'";
		log.info(sql);
		Object[] obj = new Object[] {hotelId,priceTypeIte,beginD,endD};
		
		Long eleDayPriceNum=(Long)super.find(sql, obj);
		
		
		return eleDayPriceNum.longValue();*/
		return clauseDao.getEleDayPriceNum(hotelId,priceTypeIte,beginD,endD,PayMethod.PAY);
		
	}
	public void saveOrUpdateAll(List objList){
		clauseDao.saveOrUpdateAll(objList);
	}
	public IClauseDaoRefactor getClauseDao() {
		return clauseDao;
	}

	public void setClauseDao(IClauseDaoRefactor clauseDao) {
		this.clauseDao = clauseDao;
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

	public HtlRoomtypeDao getRoomtypeDao() {
		return roomtypeDao;
	}

	public void setRoomtypeDao(HtlRoomtypeDao roomtypeDao) {
		this.roomtypeDao = roomtypeDao;
	}
	

}
