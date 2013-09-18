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
import com.mangocity.hotel.base.dao.IClauseDao;
import com.mangocity.hotel.base.manage.ClauseManage;
import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.persistence.HtlBookCaulClause;
import com.mangocity.hotel.base.persistence.HtlBookModifyField;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlFavouraParameter;
import com.mangocity.hotel.base.persistence.HtlFavourableDecrease;
import com.mangocity.hotel.base.persistence.HtlPopedomControl;
import com.mangocity.hotel.base.persistence.HtlPreconcertItemTemplet;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.service.IHotelFavourableReturnService;
import com.mangocity.hotel.base.service.assistant.CutDate;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.DateComponent;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.log.MyLog;

/**
 */
public class ClauseManageImpl extends DAOHibernateImpl implements ClauseManage {

    /**
     * 
     */
    private static final long serialVersionUID = -7118334336911537265L;
    private static final MyLog log = MyLog.getLogger(ClauseManageImpl.class);
    private IClauseDao clauseDao;//引用Dao
    private IHotelFavourableReturnService returnService;
    private ContractManage contractManage;

    private List<HtlContract> contractList;

    private List<HtlPreconcertItemTemplet> clausesList;

    // 根据酒店ID获得其合同列表；
    public List searchContactByHTlID(Long hotelID) {

        List clist = new ArrayList();

        clist = super.queryByNamedQuery("queryContractsByHotelID", new Object[] { hotelID });

        contractList = new ArrayList<HtlContract>();

        for (int i = 0; i < clist.size(); i++) {

            HtlContract htct = (HtlContract) clist.get(i);

            contractList.add(htct);

        }

        return contractList;

    }

    // 根据酒店ID得到其预定条款模板列表；
    public List searchClasesByHTLID(Long hotelID) {

        List cclist = new ArrayList();

        cclist = super.queryByNamedQuery("queryClausesByHTLID", new Object[] { hotelID });

        clausesList = new ArrayList<HtlPreconcertItemTemplet>();

        for (int i = 0; i < cclist.size(); i++) {

            HtlPreconcertItemTemplet hpit = (HtlPreconcertItemTemplet) cclist.get(i);
            clausesList.add(hpit);

        }

        return clausesList;
    }

    // 根据酒店ID得到起预定条款计算规则信息 add by shengwei.zuo 2009-02-12
    public List<HtlBookCaulClause> searchBookCaulByHTLID(Long hotelID) {

        List<HtlBookCaulClause> bclist = new ArrayList();

        bclist = super.queryByNamedQuery("queryClauseRulesByHTLID", new Object[] { hotelID });

        return bclist;
    }

    /**
     * 
     * 保存预订条款计算规则 如果有重复，则覆盖时间段，有可能拆分时间段 hotel2.9.2 modify by chenjiajie 2009-08-10
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
         * 查询(开始日期<=本对象结束日期 并且 结束日期>=本对象开始日期的记录) 或者 (开始日期=本对象开始日期 并且 结束日期=本对象结束日期的记录)
         */
        String hqlForBothSides = " from HtlBookCaulClause where hotelId = ? order by modifyTime ";
        /**
         * 查询出来有以下情况 查询出的结果1 |___________| 查询出的结果2,3,4 |_____| |___| |_______| 查询出的结果5
         * |________________| 本对象 |___________|
         */
        List tempListForBothSides = super.doquery(hqlForBothSides, new Object[] { htlBookCaulClause
            .getHotelId() }, false);
        // 如果记录存在，进行拆分
        if (null != tempListForBothSides && !tempListForBothSides.isEmpty()) {
            boolean flag = false;
            if (1 < tempListForBothSides.size()) {
                List dateCopList = new ArrayList();
                // 把所有查询出来的记录的ID，开始结束日期，修改时间放到List中
                for (Iterator itForSingleSide = tempListForBothSides.iterator(); itForSingleSide
                    .hasNext();) {
                    HtlBookCaulClause bookCaulClauseForSingle = (HtlBookCaulClause) itForSingleSide
                        .next();

                    DateComponent dateCop = new DateComponent();
                    dateCop.setId(bookCaulClauseForSingle.getId());
                    dateCop.setBeginDate(bookCaulClauseForSingle.getClauseRuleBeginDate());
                    dateCop.setEndDate(bookCaulClauseForSingle.getClauseRuleEndDate());
                    dateCop.setModifyDate(bookCaulClauseForSingle.getModifyTime());
                    dateCopList.add(dateCop);
                }
                // 如果存在重叠，则要拆分
                if (CutDate.compareConflict(dateCopList)) {
                    for (Iterator itForSingleSide = tempListForBothSides.iterator(); itForSingleSide
                        .hasNext();) {
                        HtlBookCaulClause bookCaulClauseForSingle = 
                            (HtlBookCaulClause) itForSingleSide
                            .next();
                        // 重新查出最新的结果
                        List subList = super.doquery(hqlForBothSides,
                            new Object[] { htlBookCaulClause.getHotelId() }, false);
                        // 拆分计算规则时间段
                        splitBookCaulClause(user, bookCaulClauseForSingle, subList);
                    }
                    flag = true;
                }
            }

            // 重新查出最新的结果
            if (flag) {
                tempListForBothSides = super.doquery(hqlForBothSides,
                    new Object[] { htlBookCaulClause.getHotelId() }, false);
            }
            // 拆分计算规则时间段
            splitBookCaulClause(user, htlBookCaulClause, tempListForBothSides);
        }
        // 如果记录不存在，直接新增或更新该记录
        else {
            htlBookCaulClause.setModifyTime(new Date());
            if (null != user) {
                htlBookCaulClause.setModifier(user.getName());
            }
            super.saveOrUpdate(htlBookCaulClause);
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
    private void splitBookCaulClause(UserWrapper user, HtlBookCaulClause bookCaulClauseForSingle,
        List subList) throws IllegalAccessException, InvocationTargetException {
        DateComponent dateComponent = new DateComponent();
        dateComponent.setBeginDate(bookCaulClauseForSingle.getClauseRuleBeginDate());
        dateComponent.setEndDate(bookCaulClauseForSingle.getClauseRuleEndDate());
        List dateCops = new ArrayList();
        Map resultMap = new HashMap();
        for (int ii = 0; ii < subList.size(); ii++) {
            HtlBookCaulClause subBookCaulClause = (HtlBookCaulClause) subList.get(ii);
            DateComponent aComponent = new DateComponent();
            aComponent.setId(subBookCaulClause.getId());
            aComponent.setBeginDate(subBookCaulClause.getClauseRuleBeginDate());
            aComponent.setEndDate(subBookCaulClause.getClauseRuleEndDate());
            dateCops.add(aComponent);
        }
        resultMap = CutDate.cut(dateComponent, CutDate.sort(dateCops));
        List removeList = (List) resultMap.get("remove");
        List updateList = (List) resultMap.get("update");
        List results = new ArrayList();
        for (int jj = 0; jj < removeList.size(); jj++) {
            DateComponent bb = (DateComponent) removeList.get(jj);
            super.remove(HtlBookCaulClause.class, bb.getId());
        }
        // 根据拆分的时间段重新组装数据
        boolean nullFlag = false;
        for (int i = 0; i < subList.size(); i++) {
            HtlBookCaulClause aRecord = (HtlBookCaulClause) subList.get(i);
            int doubleFlag = 0;
            for (int j = 0; j < updateList.size(); j++) {
                DateComponent dateCop = (DateComponent) updateList.get(j);
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
                            if (null != user) {
                                newRecord.setModifier(user.getName());
                            }
                            results.add(newRecord);
                        } else {
                            aRecord.setClauseRuleBeginDate(dateCop.getBeginDate());
                            aRecord.setClauseRuleEndDate(dateCop.getEndDate());
                            aRecord.setModifyTime(new Date());
                            if (null != user) {
                                aRecord.setModifier(user.getName());
                            }
                            results.add(aRecord);
                        }
                    }
                } else if (false == nullFlag) {
                    if (null != bookCaulClauseForSingle.getID()) { // 处理修改情况
                        HtlBookCaulClause record = new HtlBookCaulClause();
                        BeanUtils.copyProperties(record, bookCaulClauseForSingle);
                        record.setId(null);
                        record.setModifyTime(new Date());
                        if (null != user) {
                            record.setModifier(user.getName());
                        }
                        results.add(record);
                    } else {// 处理新增情况
                        results.add(bookCaulClauseForSingle);
                    }
                    nullFlag = true;
                }
            }
        }
        super.saveOrUpdateAll(results);
    }

    /**
     * 根据酒店id查询修改字段定义，返回一条记录
     * 
     * @param hotelID
     * @return
     */
    public HtlBookModifyField searchBookModifyFieldByHTLID(Long hotelID) {
        HtlBookModifyField htlBookModifyField = new HtlBookModifyField();
        List<HtlBookModifyField> htlBookModifyFieldList = super.queryByNamedQuery(
            "queryBookModifyFieldByHTLID", new Object[] { hotelID });
        if (null != htlBookModifyFieldList && !htlBookModifyFieldList.isEmpty()) {
            htlBookModifyField = htlBookModifyFieldList.get(0);
        }
        return htlBookModifyField;
    }

    /**
     * 添加修改字段定义
     * 
     * @param htlBookModifyField
     */
    public void createModifyField(HtlBookModifyField htlBookModifyField) {
        super.saveOrUpdate(htlBookModifyField);
    }

    //public void saveOrUpdateAll(List objList) {
//        super.saveOrUpdateAll(objList);
//    }


    /**
     * 检查有没有需要删除的记录，有则删除 hotel2.9.2 add by chenjiajie 2009-08-16
     * 
     * @param oldList
     * @param newList
     */
    public void removeOldBookCaulClause(List<HtlBookCaulClause> oldList,
        List<HtlBookCaulClause> newList) {
        List<HtlBookCaulClause> removeList = new ArrayList<HtlBookCaulClause>();
        // 检查有没有删除的记录，有则真删除
        if (null != oldList && !oldList.isEmpty()) {
            for (int i = 0; i < oldList.size(); i++) {
                HtlBookCaulClause oldObj = oldList.get(i);
                boolean bRemove = true;// 标志是否需要删除
                for (int j = 0; j < newList.size(); j++) {
                    HtlBookCaulClause newObj = newList.get(j);
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
                for (Iterator it = removeList.iterator(); it.hasNext();) {
                    HtlBookCaulClause htlBookCaulClause = (HtlBookCaulClause) it.next();
                    super.remove(htlBookCaulClause);
                }
            }
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
        if (null != list && !list.isEmpty()) {
            if (1 < list.size()) {
                // 统计所有计算规则存在的情况
                for (Iterator it = list.iterator(); it.hasNext();) {
                    HtlBookCaulClause htlBookCaulClause = (HtlBookCaulClause) it.next();
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
            } else {
                clauseRule = list.get(0).getClauseRule();
            }
        } else {
            clauseRule = "3";
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
        String hql = " from HtlBookCaulClause where hotelId = ? "
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
        return super.doquery(hql, params, false);
    }
    
    /**
     * 取出对应酒店的所有的优惠立减条款
     * add by zhijie.gu
     */
    public List queryAllavourableclause(long hotelId) {
		
		List lstFavDecrease = new ArrayList();
		
		String sql = " select  a  from HtlFavourableDecrease a   where " +
		 " a.hotelId=? and a.endDate >= trunc(sysdate) order by a.modifyTime desc";
		Object[] obj = new Object[] {hotelId};
		lstFavDecrease=super.query(sql, obj);
		
		return lstFavDecrease;
	}
    
    /**
     * 取出对应酒店价格类型优惠立减条款
     * add by zhijie.gu
     */
    public List queryFavourableclauseForPriceTypeId(long hotelId,long priceTypeId) {
		
		List lstFavDecrease = new ArrayList();
		
		String sql = " select  a  from HtlFavourableDecrease a   where " +
		 " a.hotelId=?  and a. priceTypeId=? and a.endDate >= trunc(sysdate) order by a.modifyTime desc";
		Object[] obj = new Object[] {hotelId,priceTypeId};
		lstFavDecrease=(List)super.query(sql, obj);
		
		return lstFavDecrease;
	}
    /**
     * 列出一个酒店的所有房型
     * 
     * @param hotelId
     *            酒店id
     * @return roomtype 的 List;
     */
    public List lstRoomTypeByHotelId(long hotelId) {
        String hsql = "from HtlRoomtype where hotel_id =?";
        List lsRoomType = new ArrayList();
        lsRoomType = super.query(hsql, hotelId);
        return lsRoomType;
    }
    /**
     * 新增优惠立减
     * 
     * @param hotelId
     *            酒店id
     * @return roomtype 的 List;
     */
    public Long createFavourableDecrease(HtlFavourableDecrease htlFavourableDecrease) throws IllegalAccessException, InvocationTargetException  {
		
		List oldFavourableDecrease = super.queryByNamedQuery("queryFavourableDecreaseOrder", new Object[]{htlFavourableDecrease.getHotelId(),htlFavourableDecrease.getPriceTypeId()});	
		if(oldFavourableDecrease==null||oldFavourableDecrease.size()==0){
			super.save(htlFavourableDecrease);
		}else{
			
			boolean flag = false;
			/////////////////////////////////////////////////////
            //如果数据库有超过1条的记录，则先判断时间段是否出现重叠再进行拆分
			if(oldFavourableDecrease.size()>1){
				List dateCopList = new ArrayList();
				for(int i=0;i<oldFavourableDecrease.size();i++){
					DateComponent dateCop = new DateComponent();
					HtlFavourableDecrease fDecrease = (HtlFavourableDecrease)oldFavourableDecrease.get(i);
					dateCop.setId(fDecrease.getId());
					dateCop.setBeginDate(fDecrease.getBeginDate());
					dateCop.setEndDate(fDecrease.getEndDate());
					dateCop.setWeeks(fDecrease.getWeek());
					dateCop.setModifyDate(fDecrease.getModifyTime());
					dateCopList.add(dateCop);
				}
				
				if(CutDate.compareConflictWithWeek(dateCopList)){//如果存在重叠，则要拆分
					
					for(int j=1;j<oldFavourableDecrease.size();j++){
						HtlFavourableDecrease subFavDecrease = (HtlFavourableDecrease)oldFavourableDecrease.get(j);
						List subList = super.queryByNamedQuery("querySubFavDecrease", new Object[]{subFavDecrease.getHotelId(),subFavDecrease.getPriceTypeId(),subFavDecrease.getModifyTime()});
						this.favClauseUtil(subFavDecrease, subList);
					}
					
					flag = true;
					
				}
				
			}
			///////////////////////////////////////////////////
			if(flag==true){
				oldFavourableDecrease = super.queryByNamedQuery("queryFavourableDecreaseOrder", new Object[]{htlFavourableDecrease.getHotelId(),htlFavourableDecrease.getPriceTypeId()});	
			}
			this.favClauseUtil(htlFavourableDecrease, oldFavourableDecrease);
			
		}
		
		return htlFavourableDecrease.getId();
	}
    
    /**
	 * 优惠立减拆分日期的辅助类  add by zhijie.gu hotel 2.9.3 2009-10-20
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
		List dateCops = new ArrayList();
		Map resultMap = new HashMap(); 	
		
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
		List removeList = (List) resultMap.get("remove");
		List updateList = (List) resultMap.get("update");
		List results = new ArrayList();
		for(int jj=0; jj<removeList.size(); jj++){
			DateComponent bb = (DateComponent)removeList.get(jj);
			super.remove(HtlFavourableDecrease.class, bb.getId());	
		}	
		
		 //根据拆分的时间段重新组装数据	
		boolean nullFlag = false;
		for(int i=0; i<oldFavourableDecrease.size(); i++){
			
			HtlFavourableDecrease favDecreaseObj= (HtlFavourableDecrease) oldFavourableDecrease.get(i);
			int doubleFlag = 0;
			for(int j=0; j<updateList.size(); j++){
				DateComponent dateCop = (DateComponent) updateList.get(j);				
				if(dateCop.getId()!=null){
					if(dateCop.getId().equals(favDecreaseObj.getId())){
						doubleFlag++;
						//如果存在多个相同的id则只有第一个id保留，其他的id都赋值为null
						if(doubleFlag>1){
							HtlFavourableDecrease newFavDecreaseObj= new HtlFavourableDecrease();
							BeanUtils.copyProperties(newFavDecreaseObj,favDecreaseObj);
							
							List lstNewParameterObj = new ArrayList();
							HtlFavourableDecrease newParameterObj = new HtlFavourableDecrease();
							
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
						
						List lstUptNewParameterObj = new ArrayList();
						HtlFavouraParameter newUptParameterObj = new HtlFavouraParameter();
						
						favDecreaseUpt.setId(null);
						results.add(favDecreaseUpt);
						
					}else{//处理新增情况
						results.add(htlFavourableDecrease);
					}
					nullFlag = true;
					
				}
			}
		}
		
		super.saveOrUpdateAll(results);
		
	}
	
	/**
	 * hotel 2.9.3
	 * 
	 * 修改一个立减优惠的信息 add by zhijie.gu 2009-10-21
	 * @param favourableDecrease
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public long modifyFavClause(HtlFavourableDecrease favourableDecrease) throws IllegalAccessException, InvocationTargetException {
		
		if(favourableDecrease.getId()==null||favourableDecrease.getId()==0){
			this.createFavourableDecrease(favourableDecrease);
		}else{
			List oldFavourableDecrease = super.queryByNamedQuery("queryFavourableDecreaseOrder", new Object[]{favourableDecrease.getHotelId(),favourableDecrease.getPriceTypeId()});	
			
			boolean flag = false;
			/////////////////////////////////////////////////////
            //如果数据库有超过1条的记录，则先判断时间段是否出现重叠再进行拆分
			if(oldFavourableDecrease.size()>1){
				List dateCopList = new ArrayList();
				for(int i=0; i<oldFavourableDecrease.size(); i++){
					DateComponent dateCop = new DateComponent();
					HtlFavourableDecrease aFavDecrease = (HtlFavourableDecrease)oldFavourableDecrease.get(i);
					dateCop.setId(aFavDecrease.getId());
					dateCop.setBeginDate(aFavDecrease.getBeginDate());
					dateCop.setEndDate(aFavDecrease.getEndDate());
					dateCop.setModifyDate(aFavDecrease.getModifyTime());
					dateCop.setWeeks(aFavDecrease.getWeek());
					dateCopList.add(dateCop);
				}
				if(CutDate.compareConflictWithWeek(dateCopList)){//如果存在重叠，则要拆分
					for(int j=1; j<oldFavourableDecrease.size(); j++){
						HtlFavourableDecrease subFavDecrease = (HtlFavourableDecrease)oldFavourableDecrease.get(j);
						List subList = super.queryByNamedQuery("querySubFavDecrease", new Object[]{subFavDecrease.getHotelId(),subFavDecrease.getPriceTypeId(),subFavDecrease.getModifyTime()});
						this.favClauseUtil(subFavDecrease,subList);
					}
					flag = true;
				}
			}
			///////////////////////////////////////////////////
			if(flag==true){
				oldFavourableDecrease = super.queryByNamedQuery("queryFavourableDecreaseOrder", new Object[]{favourableDecrease.getHotelId(),favourableDecrease.getPriceTypeId()});
				
			}
			this.favClauseUtil(favourableDecrease,oldFavourableDecrease);
		}
		return 1;		
		
	}
	
	/**
	 * 根据ID查询对应的立减优惠条款 add by zhijie.gu 2009-10-20
	 * 
	 */
	public HtlFavourableDecrease getFavDecreaseById(long id) {
		
		String sql = " select  a  from HtlFavourableDecrease a   where " +
		 " a.id=? ";
		
		Object[] obj = new Object[] {id};
		
		HtlFavourableDecrease favDecrease=(HtlFavourableDecrease)super.find(sql, obj);
		
		return favDecrease;
	}
	
	/**
	 * 删除立减优惠 add by zhijie.gu 2009-10-21
	 * @param favourableDecrease
	 * @return
	 */
	public long deleteFavDecreaseObj(HtlFavourableDecrease favourableDecrease) {
		super.remove(favourableDecrease);
		return favourableDecrease.getId();
	}
	
	
	/**
	 * 获取权限表里所有数据 add by zhijie.gu 2009-10-21
	 * @return
	 */
	public List getAllPopedomList(){
		List popedomControlLis = new ArrayList();
		String sql = " select  a  from HtlPopedomControl a";
		popedomControlLis= super.query(sql);
		return popedomControlLis ;
		
	}
	
	public List<HtlPopedomControl> getPopedomListByLoginName(String loginName){
		String hsql = "from HtlPopedomControl a where a.logName = ?";
		List<HtlPopedomControl> popedomControlLis = super.query(hsql, loginName);
		return popedomControlLis == null ? Collections.EMPTY_LIST : popedomControlLis;
	}
	
    /** getter and setter **/
    public List<HtlPreconcertItemTemplet> getClausesList() {
        return clausesList;
    }

    public void setClausesList(List<HtlPreconcertItemTemplet> clausesList) {
        this.clausesList = clausesList;
    }

    public List<HtlContract> getContractList() {
        return contractList;
    }

    public void setContractList(List<HtlContract> contractList) {
        this.contractList = contractList;
    }
    

    public void setClauseDao(IClauseDao clauseDao) {
		this.clauseDao = clauseDao;
	}

	public void setContractManage(ContractManage contractManage) {
		this.contractManage = contractManage;
	}

	public void setReturnService(IHotelFavourableReturnService returnService) {
		this.returnService = returnService;
	}

	public void saveOrUpdateAll(List objList) {
        // TODO Auto-generated method stub
        
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

	public Map<String, ?> getPopedoms(String popedomControlType, String loginName, Long hotelId) {
		List<HtlPopedomControl> list=clauseDao.getPopedoms(popedomControlType,loginName);
		Map resultMap=new HashMap();
		for(HtlPopedomControl popedom :list){
			resultMap.put("FAVOURABLE_RETURN_LIST",returnService.queryAllFavourableReturn(hotelId));
			resultMap.put("CURRENCY", contractManage.queryCurrentContractByHotelId(hotelId).getCurrency());
		}
		return resultMap;
	}
}
