package com.mangocity.hotel.base.log.aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.aop.MethodBeforeAdvice;

import com.mangocity.hotel.base.constant.ClaueType;
import com.mangocity.hotel.base.persistence.HtlAssureBatch;
import com.mangocity.hotel.base.persistence.HtlAssureItemEverydayBatch;
import com.mangocity.hotel.base.persistence.HtlAssureItemTemplate;
import com.mangocity.hotel.base.persistence.HtlAssureTemplate;
import com.mangocity.hotel.base.persistence.HtlPreconcertItemBatch;
import com.mangocity.hotel.base.persistence.HtlPreconcertItemTemplet;
import com.mangocity.hotel.base.persistence.HtlPrepayEverydayBatch;
import com.mangocity.hotel.base.persistence.HtlPrepayItemEverydayBatch;
import com.mangocity.hotel.base.persistence.HtlPrepayItemTemplate;
import com.mangocity.hotel.base.persistence.HtlPrepayTemplate;
import com.mangocity.hotel.base.persistence.HtlReservContBatch;
import com.mangocity.hotel.base.persistence.HtlReservContTemplate;
import com.mangocity.hotel.base.persistence.HtlReservationBatch;
import com.mangocity.hotel.base.persistence.HtlReservationTemplate;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.log.MyLog;

/**
 * 
 * 预定条款模板操作日志类； add by shengwei.zuo
 * 
 */
public class LogClausePublic extends DAOHibernateImpl implements MethodBeforeAdvice {
	private static final MyLog log = MyLog.getLogger(LogClausePublic.class);
    public void before(Method arg0, Object[] args, Object target) throws Throwable {

        HtlPreconcertItemTemplet htlPreconcertItemTemplet = new HtlPreconcertItemTemplet();

        htlPreconcertItemTemplet = (HtlPreconcertItemTemplet) args[0];

        if (null != htlPreconcertItemTemplet) {

            HtlPreconcertItemBatch htlPreconcertItemBatch = new HtlPreconcertItemBatch();

            // 判断如果是删除模板；add by shengwei.zuo
            if (htlPreconcertItemTemplet.getActive().equals(BizRuleCheck.getFalseString())) {

                Long ID = htlPreconcertItemTemplet.getID();

                // 根据模板ID，获得模板的相关信息；add by shengwei.zuo
                HtlPreconcertItemTemplet htlPreconItTplt = (HtlPreconcertItemTemplet) super.find(
                    HtlPreconcertItemTemplet.class, ID);

                // 设置是否删除标记
                htlPreconItTplt.setActive(htlPreconcertItemTemplet.getActive());

                // 设置删除人，删除时间
                htlPreconItTplt.setDelBy(htlPreconcertItemTemplet.getDelBy());
                htlPreconItTplt.setDelByID(htlPreconcertItemTemplet.getDelByID());
                htlPreconItTplt.setDelTime(htlPreconcertItemTemplet.getDelTime());

                // 设置修改人，修改时间；
                htlPreconItTplt.setModifyBy(htlPreconcertItemTemplet.getModifyBy());
                htlPreconItTplt.setModifyByID(htlPreconcertItemTemplet.getModifyByID());
                htlPreconItTplt.setModifyTime(htlPreconcertItemTemplet.getModifyTime());

                // 重新给模板对象赋值；
                htlPreconcertItemTemplet = htlPreconItTplt;

            }

            /*
             * 日志操作的相关实体；
             */

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
             * 要进行日志操作的实体
             */
            List<HtlAssureTemplate> htlAssureTemplateList = new ArrayList<HtlAssureTemplate>();

            List<HtlPrepayTemplate> htlPrepayTemplateList = new ArrayList<HtlPrepayTemplate>();

            List<HtlReservationTemplate> htlReservationTemplateList = 
                                            new ArrayList<HtlReservationTemplate>();

            List<HtlAssureItemTemplate> htlAssureItemTemplateList = 
                                            new ArrayList<HtlAssureItemTemplate>();

            List<HtlPrepayItemTemplate> htlPrepayItemTemplateList = 
                                            new ArrayList<HtlPrepayItemTemplate>();

            List<HtlReservContTemplate> htlReservContTemplateList = 
                                            new ArrayList<HtlReservContTemplate>();

            /*
             * 向日志主表中插入记录
             */

            // 当属性不为空时，才进行写入日志操作
            if ((null != htlPreconcertItemTemplet.getReservationName() && !htlPreconcertItemTemplet
                .getReservationName().equals(""))
                || (null != htlPreconcertItemTemplet.getActive() && !htlPreconcertItemTemplet
                    .getActive().equals(""))
                || (null != htlPreconcertItemTemplet.getHotelID())
                || (null != htlPreconcertItemTemplet.getCreateBy() && !htlPreconcertItemTemplet
                    .getCreateBy().equals(""))
                || (null != htlPreconcertItemTemplet.getCreateByID() && !htlPreconcertItemTemplet
                    .getCreateByID().equals(""))
                || (null != htlPreconcertItemTemplet.getCreateTime())
                || (null != htlPreconcertItemTemplet.getModifyBy() && !htlPreconcertItemTemplet
                    .getModifyBy().equals(""))
                || (null != htlPreconcertItemTemplet.getModifyByID() && !htlPreconcertItemTemplet
                    .getModifyByID().equals(""))
                || (null != htlPreconcertItemTemplet.getModifyTime())) {

                // 设定模板名称
                htlPreconcertItemBatch.setReservationName(htlPreconcertItemTemplet
                    .getReservationName());

                // 设定删除标记；
                htlPreconcertItemBatch.setActive(htlPreconcertItemTemplet.getActive());

                // 设定操作模块标记
                htlPreconcertItemBatch.setDoubletofalg(ClaueType.CLAUS_TMPLT);

                // 设定酒店id;
                htlPreconcertItemBatch.setHotelId(htlPreconcertItemTemplet.getHotelID());

                // 设置创建人，和创建时间；
                htlPreconcertItemBatch.setCreateBy(htlPreconcertItemTemplet.getCreateBy());
                htlPreconcertItemBatch.setCreateById(htlPreconcertItemTemplet.getCreateByID());
                htlPreconcertItemBatch.setCreateTime(htlPreconcertItemTemplet.getCreateTime());

                // 设置修改时间，修改人；
                htlPreconcertItemBatch.setModifyBy(htlPreconcertItemTemplet.getModifyBy());
                htlPreconcertItemBatch.setModifyById(htlPreconcertItemTemplet.getModifyByID());
                htlPreconcertItemBatch.setModifyTime(htlPreconcertItemTemplet.getModifyTime());

            }

            if (htlPreconcertItemTemplet.getActive().equals(BizRuleCheck.getFalseString())) {

                htlPreconcertItemBatch.setUpdateFalg("删除");

                // 当属性不为空时，才进行写入日志操作
                if ((null != htlPreconcertItemTemplet.getID())
                    || (null != htlPreconcertItemTemplet.getDelBy() && !htlPreconcertItemTemplet
                        .getDelBy().equals(""))
                    || (null != htlPreconcertItemTemplet.getDelByID() && !htlPreconcertItemTemplet
                        .getDelByID().equals(""))
                    || (null != htlPreconcertItemTemplet.getDelTime())) {

                    htlPreconcertItemBatch.setReservationTemplateId(htlPreconcertItemTemplet
                        .getID());

                    // 设置删除时间，和删除人
                    htlPreconcertItemBatch.setDelBy(htlPreconcertItemTemplet.getDelBy());
                    htlPreconcertItemBatch.setDelById(htlPreconcertItemTemplet.getDelByID());
                    htlPreconcertItemBatch.setDelTime(htlPreconcertItemTemplet.getDelTime());
                }

            } else {

                htlPreconcertItemBatch.setUpdateFalg("增加");

            }

            /*
             * 向担保条款日志表中插入记录
             */
            htlAssureTemplateList = htlPreconcertItemTemplet.getHtlAssureTemplateZ();

            if (null != htlAssureTemplateList && 0 < htlAssureTemplateList.size()) {

                HtlAssureTemplate htlAssureTemplate = htlAssureTemplateList.get(0);

                if (null != htlAssureTemplate) {

                    HtlAssureBatch htlAssureBatch = new HtlAssureBatch();

                    // 当属性不为空时，才进行写入日志操作
                    if ((null != htlAssureTemplate.getAssureLetter() && !htlAssureTemplate
                        .getAssureLetter().equals(""))
                        || (null != htlAssureTemplate.getAssureType() && !htlAssureTemplate
                            .getAssureType().equals(""))
                        || (null != htlAssureTemplate.getIsNotConditional() && !htlAssureTemplate
                            .getIsNotConditional().equals(""))
                        || (null != htlAssureTemplate.getLatestAssureTime() && !htlAssureTemplate
                            .getLatestAssureTime().equals(""))
                        || (null != htlAssureTemplate.getOverRoomNumber())

                    ) {

                        htlAssureBatch.setAssureLetter(htlAssureTemplate.getAssureLetter());

                        htlAssureBatch.setAssureType(htlAssureTemplate.getAssureType());

                        htlAssureBatch.setIsNotConditional(htlAssureTemplate.getIsNotConditional());

                        htlAssureBatch.setLatestAssureTime(htlAssureTemplate.getLatestAssureTime());

                        htlAssureBatch.setOverRoomNumber(htlAssureTemplate.getOverRoomNumber());

                        htlAssureBatch.setHtlPreconcertItemBatch(htlPreconcertItemBatch);

                    }

                    htlAssureItemTemplateList = htlAssureTemplate.getLisHtlAssureItemTemplate();

                    for (int i = 0; i < htlAssureItemTemplateList.size(); i++) {

                        HtlAssureItemTemplate htlAssureItemTemplate = htlAssureItemTemplateList
                            .get(i);

                        HtlAssureItemEverydayBatch htlAssureItemEverydayBatch = 
                            new HtlAssureItemEverydayBatch();

                        htlAssureItemEverydayBatch.setBeforeorafter(htlAssureItemTemplate
                            .getBeforeOrAfter());
                        htlAssureItemEverydayBatch.setDeductType(htlAssureItemTemplate
                            .getDeductType());
                        htlAssureItemEverydayBatch.setFirstDateOrDays(htlAssureItemTemplate
                            .getFirstDateOrDays());
                        htlAssureItemEverydayBatch.setFirstTime(htlAssureItemTemplate
                            .getFirstTime());

                        htlAssureItemEverydayBatch.setPercentage(htlAssureItemTemplate
                            .getPercentage());
                        htlAssureItemEverydayBatch.setScope(htlAssureItemTemplate.getScope());
                        htlAssureItemEverydayBatch.setSecondDateOrDays(htlAssureItemTemplate
                            .getSecondDateOrDays());
                        htlAssureItemEverydayBatch.setSecondTime(htlAssureItemTemplate
                            .getSecondTime());
                        htlAssureItemEverydayBatch.setType(htlAssureItemTemplate.getType());

                        htlAssureItemEverydayBatch.setHtlAssureBatch(htlAssureBatch);

                        htlAssureBatchEveryList.add(htlAssureItemEverydayBatch);

                    }

                    htlAssureBatch.setHtlAssureItemEverydayBatch(htlAssureBatchEveryList);

                    htlAssureBatchList.add(htlAssureBatch);

                }

            }

            /*
             * 向预付条款日志表中插入记录
             */
            htlPrepayTemplateList = htlPreconcertItemTemplet.getHtlPrepayTemplateZ();

            if (null != htlPrepayTemplateList && 0 < htlPrepayTemplateList.size()) {

                HtlPrepayTemplate htlPrepayTemplate = htlPrepayTemplateList.get(0);

                if (null != htlPrepayTemplate) {

                    HtlPrepayEverydayBatch htlPrepayEverydayBatch = new HtlPrepayEverydayBatch();
                    // 当属性不为空时，才进行写入日志操作
                    if ((null != htlPrepayTemplate.getAmountType() && !htlPrepayTemplate
                        .getAmountType().equals(""))
                        || (null != htlPrepayTemplate.getBalanceType() && !htlPrepayTemplate
                            .getBalanceType().equals(""))
                        || (null != htlPrepayTemplate.getDaysAfterConfirm())
                        || (null != htlPrepayTemplate.getLimitAheadDays())
                        || (null != htlPrepayTemplate.getLimitAheadDaysTime() && !htlPrepayTemplate
                            .getLimitAheadDaysTime().equals(""))
                        || (null != htlPrepayTemplate.getLimitDate())
                        || (null != htlPrepayTemplate.getLimitTime() && !htlPrepayTemplate
                            .getLimitTime().equals(""))
                        || (null != htlPrepayTemplate.getPaymentType() && !htlPrepayTemplate
                            .getPaymentType().equals(""))
                        || (null != htlPrepayTemplate.getTimeAfterConfirm() && !htlPrepayTemplate
                            .getTimeAfterConfirm().equals(""))
                        || (null != htlPrepayTemplate.getPrepayDeductType() && !htlPrepayTemplate
                            .getPrepayDeductType().equals(""))
                        || (null != htlPrepayTemplate.getTimeLimitType() && !htlPrepayTemplate
                            .getTimeLimitType().equals(""))

                    ) {

                        htlPrepayEverydayBatch.setAmountType(htlPrepayTemplate.getAmountType());
                        htlPrepayEverydayBatch.setBalanceType(htlPrepayTemplate.getBalanceType());
                        htlPrepayEverydayBatch.setDaysAfterConfirm(htlPrepayTemplate
                            .getDaysAfterConfirm());
                        htlPrepayEverydayBatch.setLimitAheadDays(htlPrepayTemplate
                            .getLimitAheadDays());
                        htlPrepayEverydayBatch.setLimitAheadDaysTime(htlPrepayTemplate
                            .getLimitAheadDaysTime());
                        htlPrepayEverydayBatch.setLimitDate(htlPrepayTemplate.getLimitDate());
                        htlPrepayEverydayBatch.setLimitTime(htlPrepayTemplate.getLimitTime());
                        htlPrepayEverydayBatch.setPaymentType(htlPrepayTemplate.getPaymentType());
                        htlPrepayEverydayBatch.setTimeAfterConfirm(htlPrepayTemplate
                            .getTimeAfterConfirm());
                        htlPrepayEverydayBatch.setPrepaydebuctType(htlPrepayTemplate
                            .getPrepayDeductType());
                        htlPrepayEverydayBatch.setTimelimitType(htlPrepayTemplate
                            .getTimeLimitType());

                        htlPrepayEverydayBatch.setHtlPreconcertItemBatch(htlPreconcertItemBatch);

                    }

                    htlPrepayItemTemplateList = htlPrepayTemplate.getLisHtlPrepayItemTemplate();

                    for (int i = 0; i < htlPrepayItemTemplateList.size(); i++) {

                        HtlPrepayItemTemplate htlPrepayItemTemplate = htlPrepayItemTemplateList
                            .get(i);

                        HtlPrepayItemEverydayBatch htlPrepayItemEverydayBatch = 
                            new HtlPrepayItemEverydayBatch();

                        htlPrepayItemEverydayBatch.setBeforeorafter(htlPrepayItemTemplate
                            .getBeforeOrAfter());
                        htlPrepayItemEverydayBatch.setDeductType(htlPrepayItemTemplate
                            .getDeductType());
                        htlPrepayItemEverydayBatch.setFirstDateOrDays(htlPrepayItemTemplate
                            .getFirstDateOrDays());
                        htlPrepayItemEverydayBatch.setFirstTime(htlPrepayItemTemplate
                            .getFirstTime());

                        htlPrepayItemEverydayBatch.setPercentage(htlPrepayItemTemplate
                            .getPercentage());
                        htlPrepayItemEverydayBatch.setScope(htlPrepayItemTemplate.getScope());
                        htlPrepayItemEverydayBatch.setSecondDateOrDays(htlPrepayItemTemplate
                            .getSecondDateOrDays());
                        htlPrepayItemEverydayBatch.setSecondTime(htlPrepayItemTemplate
                            .getSecondTime());
                        htlPrepayItemEverydayBatch.setType(htlPrepayItemTemplate.getType());

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
             * 向预订条款日志表中插入记录
             */

            htlReservationTemplateList = htlPreconcertItemTemplet.getHtlReservationTemplateZ();

            if (null != htlReservationTemplateList && 0 < htlReservationTemplateList.size()) {

                HtlReservationTemplate htlReservationTemplate = htlReservationTemplateList.get(0);

                if (null != htlReservationTemplate) {

                    HtlReservationBatch htlReservationBatch = new HtlReservationBatch();

                    // 当属性不为空时，才进行写入日志操作
                    if ((null != htlReservationTemplate.getAheadDay())
                        || (null != htlReservationTemplate.getAheadTime() && !htlReservationTemplate
                            .getAheadTime().equals(""))
                        || (null != htlReservationTemplate.getContinueNights())
                        || (null != htlReservationTemplate.getMustAheadDate())
                        || (null != htlReservationTemplate.getMustAheadTime() 
                                && !htlReservationTemplate
                            .getMustAheadTime().equals(""))) {

                        htlReservationBatch.setAheadDay(htlReservationTemplate.getAheadDay());
                        htlReservationBatch.setAheadTime(htlReservationTemplate.getAheadTime());

                        if (null != htlReservationTemplate.getContinueNights()) {
                            htlReservationBatch.setContinueNights(htlReservationTemplate
                                .getContinueNights().toString());
                        }
                        htlReservationBatch.setMustAheadDate(htlReservationTemplate
                            .getMustAheadDate());
                        htlReservationBatch.setMustAheadTime(htlReservationTemplate
                            .getMustAheadTime());

                        htlReservationBatch.setHtlPreconcertItemBatch(htlPreconcertItemBatch);
                    }

                    htlReservContTemplateList = htlReservationTemplate
                        .getLisHtlReservContTemplate();

                    for (int i = 0; i < htlReservContTemplateList.size(); i++) {

                        HtlReservContTemplate htlReservContTemplate = htlReservContTemplateList
                            .get(i);

                        HtlReservContBatch htlReservContBatch = new HtlReservContBatch();

                        htlReservContBatch.setContinueDate(htlReservContTemplate.getContinueDate());

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

            super.save(htlPreconcertItemBatch);

        } else {

            log.info("htlPreconcertItemTemplet 为空！");
        }

    }

}
