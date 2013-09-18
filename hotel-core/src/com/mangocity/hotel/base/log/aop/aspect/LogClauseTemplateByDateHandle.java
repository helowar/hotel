package com.mangocity.hotel.base.log.aop.aspect;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.aspectj.lang.JoinPoint;

import com.mangocity.hotel.base.constant.ClaueType;
import com.mangocity.hotel.base.manage.ClauseTemplateByDateManage;
import com.mangocity.hotel.base.persistence.HtlAssure;
import com.mangocity.hotel.base.persistence.HtlAssureBatch;
import com.mangocity.hotel.base.persistence.HtlAssureItemEveryday;
import com.mangocity.hotel.base.persistence.HtlAssureItemEverydayBatch;
import com.mangocity.hotel.base.persistence.HtlPreconcertItem;
import com.mangocity.hotel.base.persistence.HtlPreconcertItemBatch;
import com.mangocity.hotel.base.persistence.HtlPrepayEveryday;
import com.mangocity.hotel.base.persistence.HtlPrepayEverydayBatch;
import com.mangocity.hotel.base.persistence.HtlPrepayItemEveryday;
import com.mangocity.hotel.base.persistence.HtlPrepayItemEverydayBatch;
import com.mangocity.hotel.base.persistence.HtlReservCont;
import com.mangocity.hotel.base.persistence.HtlReservContBatch;
import com.mangocity.hotel.base.persistence.HtlReservation;
import com.mangocity.hotel.base.persistence.HtlReservationBatch;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.DAO;
import com.mangocity.util.log.MyLog;

/**
 * AOP 按日期调整预定条款日志 类
 * 
 * add by shengwei.zuo 2010-3-29
 * 
 */
public class LogClauseTemplateByDateHandle {
	
	private static final MyLog log = MyLog.getLogger(LogClauseTemplateByDateHandle.class);
    
    private DAO  entityManager;
    
    private IHotelService hotelserive;
    
    
    private ClauseTemplateByDateManage  clauseTemplateByDateManage;
	
    /**
     * 按日期调整预定条款日志的写入
     * @param jp
     */
    public void  logClauseTemplateByDate(JoinPoint jp) {
    	
    	Object[] objLog =  jp.getArgs();
    	
        HtlPreconcertItem htlPreconcertItem = new HtlPreconcertItem();

        htlPreconcertItem = (HtlPreconcertItem) objLog[0];
        
        String methodStr =jp.getSignature().getName();
    	
    	log.info("=====================按日期调整预定条款日志的写入=====Aop method : "+methodStr);

        if (null != htlPreconcertItem) {

            /*
             * 日志操作的相关实体；
             */
            HtlPreconcertItemBatch htlPreconcertItemBatch = new HtlPreconcertItemBatch();

            List<HtlAssureBatch> htlAssureBatchList = new ArrayList<HtlAssureBatch>();
            List<HtlPrepayEverydayBatch> htlPrepayEverydayBatchList = 
                                                new ArrayList<HtlPrepayEverydayBatch>();
            List<HtlReservationBatch> htlReservationBatchList = 
                                                new ArrayList<HtlReservationBatch>();
            List<HtlAssureItemEverydayBatch> htlAssureBatchEveryList = 
                                                 new ArrayList<HtlAssureItemEverydayBatch>();
            List<HtlPrepayItemEverydayBatch> htlPrepayEverydayBatchEveryList =
                                                new ArrayList<HtlPrepayItemEverydayBatch>();
            List<HtlReservContBatch> htlReservationBatchEveryList = 
                                                new ArrayList<HtlReservContBatch>();

            /*
             * 需要记录日志的实体；
             */
            List<HtlAssure> htlAssureList = new ArrayList<HtlAssure>();
            List<HtlPrepayEveryday> htlPrepayEverydayList = new ArrayList<HtlPrepayEveryday>();
            List<HtlReservation> htlReservationList = new ArrayList<HtlReservation>();

            List<HtlAssureItemEveryday> lisHtlAssureItemEveryday = 
                                                new ArrayList<HtlAssureItemEveryday>();
            List<HtlPrepayItemEveryday> lisHtlPrepayItemEveryday = 
                                                new ArrayList<HtlPrepayItemEveryday>();
            List<HtlReservCont> lisHtlReservacont = new ArrayList<HtlReservCont>();

            /*
             * 进行日志主表的插入操作
             */

            // 当属性不为空时，才进行写入日志操作
            if ((null != htlPreconcertItem.getReservationName() && !htlPreconcertItem
                .getReservationName().equals(""))
                || (null != htlPreconcertItem.getActive() && !htlPreconcertItem.getActive().equals(
                    ""))
                || (null != htlPreconcertItem.getCreateBy() && !htlPreconcertItem.getCreateBy()
                    .equals(""))
                || (null != htlPreconcertItem.getCreateById() && !htlPreconcertItem.getCreateById()
                    .equals(""))
                || (null != htlPreconcertItem.getCreateTime())
                || (null != htlPreconcertItem.getModifyBy() && !htlPreconcertItem.getModifyBy()
                    .equals(""))
                || (null != htlPreconcertItem.getModifyById() && !htlPreconcertItem.getModifyById()
                    .equals(""))
                || (null != htlPreconcertItem.getModifyTime())
                || (null != htlPreconcertItem.getHotelId())
                || (null != htlPreconcertItem.getValidDate())
                || (null != htlPreconcertItem.getPriceTypeId())) {
                // 模板名称
                htlPreconcertItemBatch.setReservationName(htlPreconcertItem.getReservationName());

                // 是否删除的标记
                htlPreconcertItemBatch.setActive(htlPreconcertItem.getActive());

                // 创建人，修改时间
                htlPreconcertItemBatch.setCreateBy(htlPreconcertItem.getCreateBy());
                htlPreconcertItemBatch.setCreateById(htlPreconcertItem.getCreateById());
                htlPreconcertItemBatch.setCreateTime(htlPreconcertItem.getCreateTime());

                // 修改人，修改时间
                htlPreconcertItemBatch.setModifyBy(htlPreconcertItem.getModifyBy());
                htlPreconcertItemBatch.setModifyById(htlPreconcertItem.getModifyById());
                htlPreconcertItemBatch.setModifyTime(htlPreconcertItem.getModifyTime());

                // 酒店ID
                htlPreconcertItemBatch.setHotelId(htlPreconcertItem.getHotelId());

                // 调整日期
                htlPreconcertItemBatch.setValidDate(htlPreconcertItem.getValidDate());

                // 价格类型
                htlPreconcertItemBatch.setPriceTypeId(htlPreconcertItem.getPriceTypeId());

            }

            // 操作模块标志
            htlPreconcertItemBatch.setDoubletofalg(ClaueType.CLAUS_DATE);

            // 是否为修改
            htlPreconcertItemBatch.setUpdateFalg("修改");

            /*
             * 向担保条款批次表中插入记录
             */
            htlAssureList = htlPreconcertItem.getHtlAssureList();

            if (null != htlAssureList && 0 < htlAssureList.size()) {

                HtlAssure htlAssure = htlAssureList.get(0);

                if (null != htlAssure) {

                    HtlAssureBatch htlAssureBatch = new HtlAssureBatch();

                    // 当属性不为空时，才进行写入日志操作
                    if ((null != htlAssure.getAssureLetter() && !htlAssure.getAssureLetter()
                        .equals(""))
                        || (null != htlAssure.getAssureType() && !htlAssure.getAssureType().equals(
                            ""))
                        || (null != htlAssure.getIsNotConditional() && !htlAssure
                            .getIsNotConditional().equals(""))
                        || (null != htlAssure.getLatestAssureTime() && !htlAssure
                            .getLatestAssureTime().equals(""))
                        || (null != htlAssure.getOverRoomNumber())

                    )

                    {

                        htlAssureBatch.setAssureLetter(htlAssure.getAssureLetter());

                        htlAssureBatch.setAssureType(htlAssure.getAssureType());

                        htlAssureBatch.setIsNotConditional(htlAssure.getIsNotConditional());

                        htlAssureBatch.setLatestAssureTime(htlAssure.getLatestAssureTime());

                        htlAssureBatch.setOverRoomNumber(htlAssure.getOverRoomNumber());

                        htlAssureBatch.setHtlPreconcertItemBatch(htlPreconcertItemBatch);

                    }

                    lisHtlAssureItemEveryday = htlAssure.getHtlAssureItemEverydayList();

                    for (int i = 0; i < lisHtlAssureItemEveryday.size(); i++) {

                        HtlAssureItemEveryday htlAssureItemEveryday = lisHtlAssureItemEveryday
                            .get(i);

                        HtlAssureItemEverydayBatch htlAssureItemEverydayBatch = 
                                                            new HtlAssureItemEverydayBatch();

                        htlAssureItemEverydayBatch.setBeforeorafter(htlAssureItemEveryday
                            .getBeforeOrAfter());
                        htlAssureItemEverydayBatch.setDeductType(htlAssureItemEveryday
                            .getDeductType());
                        htlAssureItemEverydayBatch.setFirstDateOrDays(htlAssureItemEveryday
                            .getFirstDateOrDays());
                        htlAssureItemEverydayBatch.setFirstTime(htlAssureItemEveryday
                            .getFirstTime());

                        htlAssureItemEverydayBatch.setPercentage(htlAssureItemEveryday
                            .getPercentage());
                        htlAssureItemEverydayBatch.setScope(htlAssureItemEveryday.getScope());
                        htlAssureItemEverydayBatch.setSecondDateOrDays(htlAssureItemEveryday
                            .getSecondDateOrDays());
                        htlAssureItemEverydayBatch.setSecondTime(htlAssureItemEveryday
                            .getSecondTime());
                        htlAssureItemEverydayBatch.setType(htlAssureItemEveryday.getType());

                        htlAssureItemEverydayBatch.setHtlAssureBatch(htlAssureBatch);

                        htlAssureBatchEveryList.add(htlAssureItemEverydayBatch);

                    }

                    htlAssureBatch.setHtlAssureItemEverydayBatch(htlAssureBatchEveryList);

                    htlAssureBatchList.add(htlAssureBatch);

                }

            }

            /*
             * 向预付条款批次表中插入记录
             */
            htlPrepayEverydayList = htlPreconcertItem.getHtlPrepayEverydayList();
            if (null != htlPrepayEverydayList && 0 < htlPrepayEverydayList.size()) {

                HtlPrepayEveryday htlPrepayEveryday = htlPrepayEverydayList.get(0);

                if (null != htlPrepayEveryday) {

                    HtlPrepayEverydayBatch htlPrepayEverydayBatch = new HtlPrepayEverydayBatch();

                    // 当属性不为空时，才进行写入日志操作
                    if ((null != htlPrepayEveryday.getAmountType() && !htlPrepayEveryday
                        .getAmountType().equals(""))
                        || (null != htlPrepayEveryday.getBalanceType() && !htlPrepayEveryday
                            .getBalanceType().equals(""))
                        || (null != htlPrepayEveryday.getDaysAfterConfirm())
                        || (null != htlPrepayEveryday.getLimitAheadDays())
                        || (null != htlPrepayEveryday.getLimitAheadDaysTime() && !htlPrepayEveryday
                            .getLimitAheadDaysTime().equals(""))
                        || (null != htlPrepayEveryday.getLimitDate())
                        || (null != htlPrepayEveryday.getLimitTime() && !htlPrepayEveryday
                            .getLimitTime().equals(""))
                        || (null != htlPrepayEveryday.getPaymentType() && !htlPrepayEveryday
                            .getPaymentType().equals(""))
                        || (null != htlPrepayEveryday.getTimeAfterConfirm() && !htlPrepayEveryday
                            .getTimeAfterConfirm().equals(""))
                        || (null != htlPrepayEveryday.getPrepayDeductType() && !htlPrepayEveryday
                            .getPrepayDeductType().equals(""))
                        || (null != htlPrepayEveryday.getTimeLimitType() && !htlPrepayEveryday
                            .getTimeLimitType().equals(""))) {

                        htlPrepayEverydayBatch.setAmountType(htlPrepayEveryday.getAmountType());
                        htlPrepayEverydayBatch.setBalanceType(htlPrepayEveryday.getBalanceType());
                        htlPrepayEverydayBatch.setDaysAfterConfirm(htlPrepayEveryday
                            .getDaysAfterConfirm());
                        htlPrepayEverydayBatch.setLimitAheadDays(htlPrepayEveryday
                            .getLimitAheadDays());
                        htlPrepayEverydayBatch.setLimitAheadDaysTime(htlPrepayEveryday
                            .getLimitAheadDaysTime());
                        htlPrepayEverydayBatch.setLimitDate(htlPrepayEveryday.getLimitDate());
                        htlPrepayEverydayBatch.setLimitTime(htlPrepayEveryday.getLimitTime());
                        htlPrepayEverydayBatch.setPaymentType(htlPrepayEveryday.getPaymentType());
                        htlPrepayEverydayBatch.setTimeAfterConfirm(htlPrepayEveryday
                            .getTimeAfterConfirm());
                        htlPrepayEverydayBatch.setPrepaydebuctType(htlPrepayEveryday
                            .getPrepayDeductType());
                        htlPrepayEverydayBatch.setTimelimitType(htlPrepayEveryday
                            .getTimeLimitType());

                        htlPrepayEverydayBatch.setHtlPreconcertItemBatch(htlPreconcertItemBatch);

                    }

                    lisHtlPrepayItemEveryday = htlPrepayEveryday.getHtlPrepayItemEverydayList();

                    for (int i = 0; i < lisHtlPrepayItemEveryday.size(); i++) {

                        HtlPrepayItemEveryday htlPrepayItemEveryday = lisHtlPrepayItemEveryday
                            .get(i);

                        HtlPrepayItemEverydayBatch htlPrepayItemEverydayBatch = 
                                                            new HtlPrepayItemEverydayBatch();

                        htlPrepayItemEverydayBatch.setBeforeorafter(htlPrepayItemEveryday
                            .getBeforeOrAfter());
                        htlPrepayItemEverydayBatch.setDeductType(htlPrepayItemEveryday
                            .getDeductType());
                        htlPrepayItemEverydayBatch.setFirstDateOrDays(htlPrepayItemEveryday
                            .getFirstDateOrDays());
                        htlPrepayItemEverydayBatch.setFirstTime(htlPrepayItemEveryday
                            .getFirstTime());

                        htlPrepayItemEverydayBatch.setPercentage(htlPrepayItemEveryday
                            .getPercentage());
                        htlPrepayItemEverydayBatch.setScope(htlPrepayItemEveryday.getScope());
                        htlPrepayItemEverydayBatch.setSecondDateOrDays(htlPrepayItemEveryday
                            .getSecondDateOrDays());
                        htlPrepayItemEverydayBatch.setSecondTime(htlPrepayItemEveryday
                            .getSecondTime());
                        htlPrepayItemEverydayBatch.setType(htlPrepayItemEveryday.getType());

                        htlPrepayItemEverydayBatch
                            .setHtlPrepayEverydayBatch(htlPrepayEverydayBatch);

                        htlPrepayEverydayBatchEveryList.add(htlPrepayItemEverydayBatch);

                    }

                    htlPrepayEverydayBatch
                        .setHtlPrepayItemEverydayBatch(htlPrepayEverydayBatchEveryList);

                    htlPrepayEverydayBatchList.add(htlPrepayEverydayBatch);

                }

            }

            /*
             * 向预订条款批次表中插入记录
             */
            htlReservationList = htlPreconcertItem.getHtlReservationList();

            if (null != htlReservationList && 0 < htlReservationList.size()) {

                HtlReservation htlReservation = htlReservationList.get(0);

                if (null != htlReservation) {

                    HtlReservationBatch htlReservationBatch = new HtlReservationBatch();

                    // 当属性不为空时，才进行写入日志操作
                    if ((null != htlReservation.getAheadDay())
                        || (null != htlReservation.getAheadTime() && !htlReservation.getAheadTime()
                            .equals(""))
                        || (null != htlReservation.getContinueNights())
                        || (null != htlReservation.getMustAheadDate())
                        || (null != htlReservation.getMustAheadTime() && !htlReservation
                            .getMustAheadTime().equals(""))

                    ) {

                        htlReservationBatch.setAheadDay(htlReservation.getAheadDay());
                        htlReservationBatch.setAheadTime(htlReservation.getAheadTime());

                        if (null != htlReservation.getContinueNights()) {
                            htlReservationBatch.setContinueNights(htlReservation
                                .getContinueNights().toString());
                        }
                        htlReservationBatch.setMustAheadDate(htlReservation.getMustAheadDate());
                        htlReservationBatch.setMustAheadTime(htlReservation.getMustAheadTime());

                        htlReservationBatch.setHtlPreconcertItemBatch(htlPreconcertItemBatch);

                    }

                    lisHtlReservacont = htlReservation.getHtlReservacontList();

                    for (int i = 0; i < lisHtlReservacont.size(); i++) {

                        HtlReservCont htlReservCont = lisHtlReservacont.get(i);

                        HtlReservContBatch htlReservContBatch = new HtlReservContBatch();

                        htlReservContBatch.setContinueDate(htlReservCont.getContinueDate());

                        htlReservContBatch.setHtlReservationBatch(htlReservationBatch);

                        htlReservationBatchEveryList.add(htlReservContBatch);

                    }

                    htlReservationBatch.setHtlReservContBatch(htlReservationBatchEveryList);

                    htlReservationBatchList.add(htlReservationBatch);

                }

            }

            htlPreconcertItemBatch.setHtlAssureBatch(htlAssureBatchList);
            htlPreconcertItemBatch.setHtlPrepayEverydayBatch(htlPrepayEverydayBatchList);
            htlPreconcertItemBatch.setHtlReservationBatch(htlReservationBatchList);
            // 插入每天表之前,要调用hotelservice中的putRecord方法，用来对比修改了那些记录，保存起来，做操作日志用
            
            HtlPreconcertItem htp = (HtlPreconcertItem) clauseTemplateByDateManage.findHtlPreconItemInfo(
                htlPreconcertItem.getHotelId(), htlPreconcertItem.getPriceTypeId(),
                DateUtil.dateToString(htlPreconcertItem.getValidDate()));
            entityManager.save(htlPreconcertItemBatch);
            if (null != htp) {
                hotelserive.putRecord(htlPreconcertItem, htp, htlPreconcertItemBatch.getId());
            } else {
                log.info("用于比对每天表的记录为空！");
            }

        } else {

        	 log.info("htlPreconcertItem 为空！");

        }
    	
    }


	public DAO getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(DAO entityManager) {
		this.entityManager = entityManager;
	}
	
    public IHotelService getHotelserive() {
		return hotelserive;
	}

	public void setHotelserive(IHotelService hotelserive) {
		this.hotelserive = hotelserive;
	}

	public ClauseTemplateByDateManage getClauseTemplateByDateManage() {
		return clauseTemplateByDateManage;
	}

	public void setClauseTemplateByDateManage(
			ClauseTemplateByDateManage clauseTemplateByDateManage) {
		this.clauseTemplateByDateManage = clauseTemplateByDateManage;
	}
	
}
