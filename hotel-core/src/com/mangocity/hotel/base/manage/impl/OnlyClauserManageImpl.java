package com.mangocity.hotel.base.manage.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.dao.IOnlyClauserDao;
import com.mangocity.hotel.base.dao.impl.ContractDao;
import com.mangocity.hotel.base.manage.OnlyClauserManage;
import com.mangocity.hotel.base.persistence.AssureClauseTemplate;
import com.mangocity.hotel.base.persistence.HtlAssureItemTemplate;
import com.mangocity.hotel.base.persistence.HtlAssureItemTemplateFive;
import com.mangocity.hotel.base.persistence.HtlAssureItemTemplateFour;
import com.mangocity.hotel.base.persistence.HtlAssureItemTemplateOne;
import com.mangocity.hotel.base.persistence.HtlAssureItemTemplateThree;
import com.mangocity.hotel.base.persistence.HtlAssureItemTemplateTwo;
import com.mangocity.hotel.base.persistence.HtlAssureTemplate;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlPreconcertItemBatch;
import com.mangocity.hotel.base.persistence.HtlPreconcertItemTemplet;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplate;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplateFive;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplateFour;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplateOne;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplateThree;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplateTwo;
import com.mangocity.hotel.base.persistence.HtlPrepayTemplate;
import com.mangocity.hotel.base.persistence.PrepayClauseTemplate;
import com.mangocity.util.log.MyLog;

/**
 * 用于对单条款操作的manage实现类
 * 
 * @author lihaibo
 * 
 */
public class OnlyClauserManageImpl implements OnlyClauserManage {
	private static final MyLog log = MyLog.getLogger(OnlyClauserManageImpl.class);
	private IOnlyClauserDao onlyClauserDao;
	private ContractDao contractDao;
	
	public void setContractDao(ContractDao contractDao) {
		this.contractDao = contractDao;
	}

	// 查询酒店预定条款模板 add by haibo.li 2009-2-6 Hotel V2.6
    public List queryHotelModel(long hotelId) {
    	List<HtlPreconcertItemTemplet> onlyList = onlyClauserDao.queryHtlPreconcertItemTempletListByHotelId(Long.valueOf(hotelId));
        List<AssureClauseTemplate> lisHtlAssureItemTemplateOne = new ArrayList<AssureClauseTemplate>();
        List<AssureClauseTemplate> lisHtlAssureItemTemplateTwo = new ArrayList<AssureClauseTemplate>();
        List<AssureClauseTemplate> lisHtlAssureItemTemplateThree = new ArrayList<AssureClauseTemplate>();
        List<AssureClauseTemplate> lisHtlAssureItemTemplateFour = new ArrayList<AssureClauseTemplate>();
        List<AssureClauseTemplate> lisHtlAssureItemTemplateFivo = new ArrayList<AssureClauseTemplate>();
        
        List<PrepayClauseTemplate> lisHtlPrepayItemTemplateOne = new ArrayList<PrepayClauseTemplate>();
        List<PrepayClauseTemplate> lisHtlPrepayItemTemplateTwo = new ArrayList<PrepayClauseTemplate>();
        List<PrepayClauseTemplate> lisHtlPrepayItemTemplateThree = new ArrayList<PrepayClauseTemplate>();
        List<PrepayClauseTemplate> lisHtlPrepayItemTemplateFour = new ArrayList<PrepayClauseTemplate>();
        List<PrepayClauseTemplate> lisHtlPrepayItemTemplateFive = new ArrayList<PrepayClauseTemplate>();
        if(!onlyList.isEmpty()){
        	HtlPreconcertItemTemplet hi = (HtlPreconcertItemTemplet) onlyList.get(0);
            List<HtlAssureTemplate> lstHtlAssureTemp = hi.getHtlAssureTemplateZ();// 担保条款
            
            for (HtlAssureTemplate htlAssureTemplate :lstHtlAssureTemp) {
                List<HtlAssureItemTemplate> lstHtlAssureItemTemplate = htlAssureTemplate.getLisHtlAssureItemTemplate();
                for (HtlAssureItemTemplate htlAssureItemTemplate : lstHtlAssureItemTemplate) {
                	if ("1".equals(htlAssureItemTemplate.getType())) {
                        HtlAssureItemTemplateOne htlAssureItemTemplateOne = new HtlAssureItemTemplateOne();
                        htlAssureItemTemplateOne.setParams(htlAssureItemTemplate);
                        lisHtlAssureItemTemplateOne.add(htlAssureItemTemplateOne);
                        htlAssureTemplate
                            .setLisHtlAssureItemTemplateOne(lisHtlAssureItemTemplateOne);
                        continue;
                    }else if("2".equals(htlAssureItemTemplate.getType())) {
                        HtlAssureItemTemplateTwo htlAssureItemTemplateTwo = new HtlAssureItemTemplateTwo();
                        htlAssureItemTemplateTwo.setParams(htlAssureItemTemplate);
                        lisHtlAssureItemTemplateTwo.add(htlAssureItemTemplateTwo);
                        htlAssureTemplate.setLisHtlAssureItemTemplateTwo(lisHtlAssureItemTemplateTwo);
                        continue;
                    }
                    if ("3".equals(htlAssureItemTemplate.getType())) {
                        HtlAssureItemTemplateThree htlAssureItemTemplateThree = 
                            new HtlAssureItemTemplateThree();
                        htlAssureItemTemplateThree.setParams(htlAssureItemTemplate);
                        lisHtlAssureItemTemplateThree.add(htlAssureItemTemplateThree);
                        htlAssureTemplate
                            .setLisHtlAssureItemTemplateThree(lisHtlAssureItemTemplateThree);
                        continue;

                    }
                    if ("4".equals(htlAssureItemTemplate.getType())) {
                        HtlAssureItemTemplateFour htlAssureItemTemplateFour = 
                            new HtlAssureItemTemplateFour();
                        htlAssureItemTemplateFour.setParams(htlAssureItemTemplate);
                        lisHtlAssureItemTemplateFour.add(htlAssureItemTemplateFour);
                        htlAssureTemplate
                            .setLisHtlAssureItemTemplateFour(lisHtlAssureItemTemplateFour);
                        continue;

                    }
                    if ("5".equals(htlAssureItemTemplate.getType())) {
                        HtlAssureItemTemplateFive htlAssureItemTemplateFive =
                            new HtlAssureItemTemplateFive();
                        htlAssureItemTemplateFive.setParams(htlAssureItemTemplate);
                        lisHtlAssureItemTemplateFivo.add(htlAssureItemTemplateFive);
                        htlAssureTemplate
                            .setLisHtlAssureItemTemplateFive(lisHtlAssureItemTemplateFivo);
                        continue;

                    }
                }
            }
            // lstHtlAssureTemp.add(htlAssureTemplate);
            // hi.setHtlAssureTemplateZ(lstHtlAssureTemp);
            // 预付条款类型注入
            List<HtlPrepayTemplate> lstPrepany = hi.getHtlPrepayTemplateZ();
            for(HtlPrepayTemplate htlPrepayTemplate : lstPrepany) {
                List<HtlPrepayItemTemplate> lsthtlPrepayItemTemplate = htlPrepayTemplate.getLisHtlPrepayItemTemplate();
                for (HtlPrepayItemTemplate htlPrepayItemTemplate : lsthtlPrepayItemTemplate) {
                    if ("1".equals(htlPrepayItemTemplate.getType())) {
                        HtlPrepayItemTemplateOne htlPrepayItemTemplateOne = new HtlPrepayItemTemplateOne();
                        htlPrepayItemTemplateOne.setParams(htlPrepayItemTemplate);
                        lisHtlPrepayItemTemplateOne.add(htlPrepayItemTemplateOne);
                        htlPrepayTemplate
                            .setLisHtlPrepayItemTemplateOne(lisHtlPrepayItemTemplateOne);
                        continue;
                    }
                    if ("2".equals(htlPrepayItemTemplate.getType())) {
                        HtlPrepayItemTemplateTwo htlPrepayItemTemplateTwo = new HtlPrepayItemTemplateTwo();
                        htlPrepayItemTemplateTwo.setParams(htlPrepayItemTemplate);
                        lisHtlPrepayItemTemplateTwo.add(htlPrepayItemTemplateTwo);
                        htlPrepayTemplate.setLisHtlPrepayItemTemplateTwo(lisHtlPrepayItemTemplateTwo);
                        continue;
                    }
                    if ("3".equals(htlPrepayItemTemplate.getType())) {
                        HtlPrepayItemTemplateThree htlPrepayItemTemplateThree = new HtlPrepayItemTemplateThree();
                        htlPrepayItemTemplateThree.setParams(htlPrepayItemTemplate);
                        lisHtlPrepayItemTemplateThree.add(htlPrepayItemTemplateThree);
                        htlPrepayTemplate
                            .setLisHtlPrepayItemTemplateThree(lisHtlPrepayItemTemplateThree);
                        continue;
                    }
                    if ("4".equals(htlPrepayItemTemplate.getType())) {
                        HtlPrepayItemTemplateFour htlPrepayItemTemplateFour = new HtlPrepayItemTemplateFour();
                        htlPrepayItemTemplateFour.setParams(htlPrepayItemTemplate);
                        lisHtlPrepayItemTemplateFour.add(htlPrepayItemTemplateFour);
                        htlPrepayTemplate
                            .setLisHtlPrepayItemTemplateFour(lisHtlPrepayItemTemplateFour);
                        continue;
                    }
                    if ("5".equals(htlPrepayItemTemplate.getType())) {
                        HtlPrepayItemTemplateFive htlPrepayItemTemplateFive = new HtlPrepayItemTemplateFive();
                        htlPrepayItemTemplateFive.setParams(htlPrepayItemTemplate);
                        lisHtlPrepayItemTemplateFive.add(htlPrepayItemTemplateFive);
                        htlPrepayTemplate
                            .setLisHtlPrepayItemTemplateFive(lisHtlPrepayItemTemplateFive);
                        continue;
                    }

                }

            }
        }
        
        return onlyList;

    }

    // 插入批次表中
    public boolean saveOrUpdateClause(HtlPreconcertItemBatch htlPreconcertItemBatch) {
    	return onlyClauserDao.saveOrUpdateClause(htlPreconcertItemBatch);
    }
    
   

    // 调用存储过程,往批次表中拿到数据再插入每天表
    public boolean saveOrUpdateClausePro(long hotelId, long id, long contractId,
        String priceTypeid, Date beginDate, Date engDate, String active) throws SQLException {
    	
    	return onlyClauserDao.saveOrUpdateClausePro(Long.valueOf(hotelId),Long.valueOf(id),Long.valueOf(contractId),
    	        priceTypeid,beginDate,engDate,active);
    }
    // 查询酒店合同
    public List<HtlContract> queryHtlContract(long contractId) {
    	return contractDao.getContractListByContractId(contractId);
    }

    // 根据某一条款查询值出来
    public List quertHotelModelAll(long modelId, long hotelId) {
        List lisHtlAssureItemTemplateOne = new ArrayList();
        List lisHtlAssureItemTemplateTwo = new ArrayList();
        List lisHtlAssureItemTemplateThree = new ArrayList();
        List lisHtlAssureItemTemplateFour = new ArrayList();
        List lisHtlAssureItemTemplateFivo = new ArrayList();
        List lisHtlPrepayItemTemplateTwo = new ArrayList();
        List lisHtlPrepayItemTemplateThree = new ArrayList();
        List lisHtlPrepayItemTemplateFour = new ArrayList();
        List lisHtlPrepayItemTemplateFive = new ArrayList();
        List lisHtlPrepayItemTemplateOne = new ArrayList();
        List<HtlPreconcertItemTemplet> lstModel = onlyClauserDao.queryHtlPreconcertItemTempletById(Long.valueOf(modelId));
        if(!lstModel.isEmpty()){
        	HtlPreconcertItemTemplet hi = lstModel.get(0);
            // 担保条款
            for (HtlAssureTemplate htlAssureTemplate : hi.getHtlAssureTemplateZ()) {
                for (HtlAssureItemTemplate htlAssureItemTemplate : htlAssureTemplate.getLisHtlAssureItemTemplate()) {
                    if ("1".equals(htlAssureItemTemplate.getType())) {
                        HtlAssureItemTemplateOne htlAssureItemTemplateOne = 
                            new HtlAssureItemTemplateOne();
                        htlAssureItemTemplateOne.setScopeOne(htlAssureItemTemplate.getScope());
                        htlAssureItemTemplateOne.setDeductTypeOne(htlAssureItemTemplate
                            .getDeductType());
                        htlAssureItemTemplateOne.setPercentageOne(htlAssureItemTemplate
                            .getPercentage());
                        lisHtlAssureItemTemplateOne.add(htlAssureItemTemplateOne);
                        htlAssureTemplate
                            .setLisHtlAssureItemTemplateOne(lisHtlAssureItemTemplateOne);
                        continue;
                    }
                    if ("2".equals(htlAssureItemTemplate.getType())) {
                        HtlAssureItemTemplateTwo htlAssureItemTemplateTwo = 
                            new HtlAssureItemTemplateTwo();
                        htlAssureItemTemplateTwo.setFirstDateOrDaysTwo(htlAssureItemTemplate
                            .getFirstDateOrDays());
                        htlAssureItemTemplateTwo.setFirstTimeTwo(htlAssureItemTemplate
                            .getFirstTime());
                        htlAssureItemTemplateTwo.setSecondDateOrDaysTwo(htlAssureItemTemplate
                            .getSecondDateOrDays());
                        htlAssureItemTemplateTwo.setSecondTimeTwo(htlAssureItemTemplate
                            .getSecondTime());
                        htlAssureItemTemplateTwo.setScopeTwo(htlAssureItemTemplate.getScope());
                        htlAssureItemTemplateTwo.setDeductTypeTwo(htlAssureItemTemplate
                            .getDeductType());
                        htlAssureItemTemplateTwo.setPercentageTwo(htlAssureItemTemplate
                            .getPercentage());
                        lisHtlAssureItemTemplateTwo.add(htlAssureItemTemplateTwo);
                        htlAssureTemplate
                            .setLisHtlAssureItemTemplateTwo(lisHtlAssureItemTemplateTwo);
                        continue;

                    }
                    if ("3".equals(htlAssureItemTemplate.getType())) {
                        HtlAssureItemTemplateThree htlAssureItemTemplateThree = 
                            new HtlAssureItemTemplateThree();
                        htlAssureItemTemplateThree.setFirstDateOrDaysThree(htlAssureItemTemplate
                            .getFirstDateOrDays());
                        htlAssureItemTemplateThree.setFirstTimeThree(htlAssureItemTemplate
                            .getFirstTime());
                        htlAssureItemTemplateThree.setSecondDateOrDaysThree(htlAssureItemTemplate
                            .getSecondDateOrDays());
                        htlAssureItemTemplateThree.setSecondTimeThree(htlAssureItemTemplate
                            .getSecondTime());
                        htlAssureItemTemplateThree.setScopeThree(htlAssureItemTemplate.getScope());
                        htlAssureItemTemplateThree.setDeductTypeThree(htlAssureItemTemplate
                            .getDeductType());
                        htlAssureItemTemplateThree.setPercentageThree(htlAssureItemTemplate
                            .getPercentage());
                        lisHtlAssureItemTemplateThree.add(htlAssureItemTemplateThree);
                        htlAssureTemplate
                            .setLisHtlAssureItemTemplateThree(lisHtlAssureItemTemplateThree);
                        continue;

                    }
                    if ("4".equals(htlAssureItemTemplate.getType())) {
                        HtlAssureItemTemplateFour htlAssureItemTemplateFour = 
                            new HtlAssureItemTemplateFour();
                        htlAssureItemTemplateFour.setFirstDateOrDaysFour(htlAssureItemTemplate
                            .getFirstDateOrDays());
                        htlAssureItemTemplateFour.setFirstTimeFour(htlAssureItemTemplate
                            .getFirstTime());
                        htlAssureItemTemplateFour.setSecondDateOrDaysFour(htlAssureItemTemplate
                            .getSecondDateOrDays());
                        htlAssureItemTemplateFour.setSecondTimeFour(htlAssureItemTemplate
                            .getSecondTime());
                        htlAssureItemTemplateFour.setScopeFour(htlAssureItemTemplate.getScope());
                        htlAssureItemTemplateFour.setDeductTypeFour(htlAssureItemTemplate
                            .getDeductType());
                        htlAssureItemTemplateFour.setPercentageFour(htlAssureItemTemplate
                            .getPercentage());
                        htlAssureItemTemplateFour.setBeforeOrAfterFour(htlAssureItemTemplate
                            .getBeforeOrAfter());
                        lisHtlAssureItemTemplateFour.add(htlAssureItemTemplateFour);
                        htlAssureTemplate
                            .setLisHtlAssureItemTemplateFour(lisHtlAssureItemTemplateFour);
                        continue;

                    }

                    if ("5".equals(htlAssureItemTemplate.getType())) {
                        HtlAssureItemTemplateFive htlAssureItemTemplateFive = 
                            new HtlAssureItemTemplateFive();
                        htlAssureItemTemplateFive.setFirstDateOrDaysFive(htlAssureItemTemplate
                            .getFirstDateOrDays());
                        htlAssureItemTemplateFive.setFirstTimeFive(htlAssureItemTemplate
                            .getFirstTime());
                        htlAssureItemTemplateFive.setSecondDateOrDaysFive(htlAssureItemTemplate
                            .getSecondDateOrDays());
                        htlAssureItemTemplateFive.setSecondTimeFive(htlAssureItemTemplate
                            .getSecondTime());
                        htlAssureItemTemplateFive.setScopeFive(htlAssureItemTemplate.getScope());
                        htlAssureItemTemplateFive.setDeductTypeFive(htlAssureItemTemplate
                            .getDeductType());
                        htlAssureItemTemplateFive.setPercentageFive(htlAssureItemTemplate
                            .getPercentage());

                        // 增加之后的设定 add by shengwei.zuo hotel2.6 2009-06-04 begin;
                        htlAssureItemTemplateFive.setBeforeOrAfterFive(htlAssureItemTemplate
                            .getBeforeOrAfter());
                        // 增加之后的设定 add by shengwei.zuo hotel2.6 2009-06-04 end;

                        lisHtlAssureItemTemplateFivo.add(htlAssureItemTemplateFive);
                        htlAssureTemplate
                            .setLisHtlAssureItemTemplateFive(lisHtlAssureItemTemplateFivo);
                        continue;

                    }

                }

            }
            // lstHtlAssureTemp.add(htlAssureTemplate);
            // hi.setHtlAssureTemplateZ(lstHtlAssureTemp);
            // 预付条款类型注入
            for (HtlPrepayTemplate htlPrepayTemplate : hi.getHtlPrepayTemplateZ()) {
                for (HtlPrepayItemTemplate htlPrepayItemTemplate : htlPrepayTemplate.getLisHtlPrepayItemTemplate()) {
                    if ("1".equals(htlPrepayItemTemplate.getType())) {
                        HtlPrepayItemTemplateOne htlPrepayItemTemplateOne = 
                            new HtlPrepayItemTemplateOne();
                        htlPrepayItemTemplateOne.setScopePPOne(htlPrepayItemTemplate.getScope());
                        htlPrepayItemTemplateOne.setDeductTypePPOne(htlPrepayItemTemplate
                            .getDeductType());
                        htlPrepayItemTemplateOne.setPercentagePPOne(htlPrepayItemTemplate
                            .getPercentage());
                        lisHtlPrepayItemTemplateOne.add(htlPrepayItemTemplateOne);
                        htlPrepayTemplate
                            .setLisHtlPrepayItemTemplateOne(lisHtlPrepayItemTemplateOne);
                        continue;
                    }
                    if ("2".equals(htlPrepayItemTemplate.getType())) {
                        HtlPrepayItemTemplateTwo htlPrepayItemTemplateTwo =
                            new HtlPrepayItemTemplateTwo();
                        htlPrepayItemTemplateTwo.setFirstDateOrDaysPPTwo(htlPrepayItemTemplate
                            .getFirstDateOrDays());
                        htlPrepayItemTemplateTwo.setFirstTimePPTwo(htlPrepayItemTemplate
                            .getFirstTime());
                        htlPrepayItemTemplateTwo.setSecondDateOrDaysPPTwo(htlPrepayItemTemplate
                            .getSecondDateOrDays());
                        htlPrepayItemTemplateTwo.setSecondTimePPTwo(htlPrepayItemTemplate
                            .getSecondTime());
                        htlPrepayItemTemplateTwo.setScopePPTwo(htlPrepayItemTemplate.getScope());
                        htlPrepayItemTemplateTwo.setDeductTypePPTwo(htlPrepayItemTemplate
                            .getDeductType());
                        htlPrepayItemTemplateTwo.setPercentagePPTwo(htlPrepayItemTemplate
                            .getPercentage());
                        lisHtlPrepayItemTemplateTwo.add(htlPrepayItemTemplateTwo);
                        htlPrepayTemplate
                            .setLisHtlPrepayItemTemplateTwo(lisHtlPrepayItemTemplateTwo);
                        continue;
                    }
                    if ("3".equals(htlPrepayItemTemplate.getType())) {
                        HtlPrepayItemTemplateThree htlPrepayItemTemplateThree = 
                            new HtlPrepayItemTemplateThree();
                        htlPrepayItemTemplateThree.setFirstDateOrDaysPPThree(htlPrepayItemTemplate
                            .getFirstDateOrDays());
                        htlPrepayItemTemplateThree.setFirstTimePPThree(htlPrepayItemTemplate
                            .getFirstTime());
                        htlPrepayItemTemplateThree.setSecondDateOrDaysPPThree(htlPrepayItemTemplate
                            .getSecondDateOrDays());
                        htlPrepayItemTemplateThree.setSecondTimePPThree(htlPrepayItemTemplate
                            .getSecondTime());
                        htlPrepayItemTemplateThree
                            .setScopePPThree(htlPrepayItemTemplate.getScope());
                        htlPrepayItemTemplateThree.setDeductTypePPThree(htlPrepayItemTemplate
                            .getDeductType());
                        htlPrepayItemTemplateThree.setPercentagePPThree(htlPrepayItemTemplate
                            .getPercentage());
                        lisHtlPrepayItemTemplateThree.add(htlPrepayItemTemplateThree);
                        htlPrepayTemplate
                            .setLisHtlPrepayItemTemplateThree(lisHtlPrepayItemTemplateThree);
                        continue;
                    }
                    if ("4".equals(htlPrepayItemTemplate.getType())) {
                        HtlPrepayItemTemplateFour htlPrepayItemTemplateFour = 
                            new HtlPrepayItemTemplateFour();
                        htlPrepayItemTemplateFour.setFirstDateOrDaysPPFour(htlPrepayItemTemplate
                            .getFirstDateOrDays());
                        htlPrepayItemTemplateFour.setFirstTimePPFour(htlPrepayItemTemplate
                            .getFirstTime());
                        htlPrepayItemTemplateFour.setSecondDateOrDaysPPFour(htlPrepayItemTemplate
                            .getSecondDateOrDays());
                        htlPrepayItemTemplateFour.setSecondTimePPFour(htlPrepayItemTemplate
                            .getSecondTime());
                        htlPrepayItemTemplateFour.setScopePPFour(htlPrepayItemTemplate.getScope());
                        htlPrepayItemTemplateFour.setDeductTypePPFour(htlPrepayItemTemplate
                            .getDeductType());
                        htlPrepayItemTemplateFour.setPercentagePPFour(htlPrepayItemTemplate
                            .getPercentage());
                        lisHtlPrepayItemTemplateFour.add(htlPrepayItemTemplateFour);
                        htlPrepayTemplate
                            .setLisHtlPrepayItemTemplateFour(lisHtlPrepayItemTemplateFour);
                        continue;
                    }
                    if ("5".equals(htlPrepayItemTemplate.getType())) {
                        HtlPrepayItemTemplateFive htlPrepayItemTemplateFive = 
                            new HtlPrepayItemTemplateFive();
                        htlPrepayItemTemplateFive.setFirstDateOrDaysPPFive(htlPrepayItemTemplate
                            .getFirstDateOrDays());
                        htlPrepayItemTemplateFive.setFirstTimePPFive(htlPrepayItemTemplate
                            .getFirstTime());
                        htlPrepayItemTemplateFive.setSecondDateOrDaysPPFive(htlPrepayItemTemplate
                            .getSecondDateOrDays());
                        htlPrepayItemTemplateFive.setSecondTimePPFive(htlPrepayItemTemplate
                            .getSecondTime());
                        htlPrepayItemTemplateFive.setScopePPFive(htlPrepayItemTemplate.getScope());
                        htlPrepayItemTemplateFive.setDeductTypePPFive(htlPrepayItemTemplate
                            .getDeductType());
                        htlPrepayItemTemplateFive.setPercentagePPFive(htlPrepayItemTemplate
                            .getPercentage());

                        // 增加之后的设定 add by shengwei.zuo hotel2.6 2009-06-04 begin;
                        htlPrepayItemTemplateFive.setBeforeOrAfterPPFive(htlPrepayItemTemplate
                            .getBeforeOrAfter());
                        // 增加之后的设定 add by shengwei.zuo hotel2.6 2009-06-04 end;

                        lisHtlPrepayItemTemplateFive.add(htlPrepayItemTemplateFive);
                        htlPrepayTemplate
                            .setLisHtlPrepayItemTemplateFive(lisHtlPrepayItemTemplateFive);
                        continue;
                    }

                }

            }
        }
        return lstModel;
    }

    // 查询批次表,来找到操作记录
    public List<HtlPreconcertItemBatch> queryHtlBatch(long hotelId) {
        return onlyClauserDao.queryHtlPreconcertItemBatchListByHotelId(Long.valueOf(hotelId));
    }

    public HtlPreconcertItemBatch findHtlPreconcertItemBatch(Long id) {
    	return onlyClauserDao.queryHtlPreconcertItemBatchById(id);
    }
    
    public IOnlyClauserDao getOnlyClauserDao() {
		return onlyClauserDao;
	}

	public void setOnlyClauserDao(IOnlyClauserDao onlyClauserDao) {
		this.onlyClauserDao = onlyClauserDao;
	}
}
