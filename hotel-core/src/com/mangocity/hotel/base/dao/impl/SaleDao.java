package com.mangocity.hotel.base.dao.impl;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mangocity.hotel.base.dao.ISaleDao;
import com.mangocity.hotel.base.persistence.HtlArea;
import com.mangocity.hotel.base.persistence.HtlAssignCustom;
import com.mangocity.hotel.base.persistence.HtlBatchAssign;
import com.mangocity.hotel.base.persistence.HtlCutoffDayQuota;
import com.mangocity.hotel.base.persistence.HtlFreeOperate;
import com.mangocity.hotel.base.persistence.HtlHdlAddscope;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlOpenCloseRoom;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlQuota;
import com.mangocity.hotel.base.persistence.HtlQuotaNew;
import com.mangocity.hotel.base.persistence.HtlQuotabatch;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.util.CodeName;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.ContinueDatecomponent;
import com.mangocity.util.bean.HotelBaseConstantBean;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.log.MyLog;

/**
 */
public class SaleDao extends DAOHibernateImpl implements ISaleDao {


	private static final MyLog log = MyLog.getLogger(SaleDao.class);
    
    /**
     * 查找一个房间，如果不存在就新建一个房间
     */
    public HtlRoom createRoomIfNotExist(Long roomTypeId, Long hotelId, Date ableSaleDate,
        Long contractId) {
        List lstRoom = super.queryByNamedQuery("findRoomByBizKey", new Object[] { roomTypeId,
            hotelId, ableSaleDate });
        if (0 < lstRoom.size()) {
            HtlRoom room = (HtlRoom) lstRoom.get(0);
            return room;
        } else {
            HtlRoom room = new HtlRoom();

            room.setRoomTypeId(roomTypeId);
            room.setHotelId(hotelId);
            room.setAbleSaleDate(ableSaleDate);
            room.setLastAssureTime(BizRuleCheck.getDefaultLastAssureTime());
            room.setContractId(contractId.longValue());
            room.setHasFree(BizRuleCheck.getFalseString());
            super.save(room);

            return room;
        }
    }

    /**
     * 通过业务主键找到一个房间
     */
    public HtlRoom findRoomByBizKey(Long roomTypeId, Long hotelId, Date ableSaleDate) {

        List lstRoom = super.queryByNamedQuery("findRoomByBizKey", new Object[] { roomTypeId,
            hotelId, ableSaleDate });
        if (0 < lstRoom.size()) {
            HtlRoom room = (HtlRoom) lstRoom.get(0);
            room.getLstQuotas();
            room.getLstPrices();
            return room;
        }
        return null;
    }

    /**
     * 通过业务主键找到一个配额对象
     */
    public HtlQuota findQuotaByBizKey(String quotaType, String shareType, Long hotelId,
        Long contractId, Date ableSaleDate, long roomTypeid) {
        /*
         * List lstQuota = super.queryByNamedQuery("findQuotaByBizKey", new Object[] {
         * quotaType,shareType, hotelId, contractId, ableSaleDate }); if (lstQuota.size() > 0) {
         * return (HtlQuota) lstQuota.get(0); }
         */
        return null;
    }

    /**
     * 通过业务主键找到一个价格
     */
    public HtlPrice findPriceBizKey(long hotelId, long roomTypeId, long childRoomTypeId,
        String quotaType, Date ableSaleDate, String payMethod) {

        List lstPrice = super.queryByNamedQuery("findPriceBizKey", new Object[] { hotelId,
            roomTypeId, childRoomTypeId, quotaType, payMethod, ableSaleDate });
        if (0 < lstPrice.size()) {
            HtlPrice htlPrice = (HtlPrice) lstPrice.get(0);

            return htlPrice;
        }
        return null;
    }

    public List getQuotaByBizKeyWithDate(String quotaType, String shareType, long hotelId,
        long contractId, long roomTypeId, Date beginDate, Date endDate, String[] weeks) {
        StringBuffer sb = new StringBuffer();
        String sWeek = BizRuleCheck.ArrayToString(weeks);

        sb.append(" from HtlQuota quota where quota.quotaType=").append(quotaType).append(
            " and quota.hotelId=").append(hotelId);
        sb.append(" and quota.shareType=").append(shareType).append(" and quota.contractId=")
            .append(contractId).append(" and quota.room.roomTypeId=").append(roomTypeId);
        if (null != sWeek && !sWeek.equals("")) {
            sb.append(" and week in (").append(sWeek).append(") ");
        }
        sb.append(" and quota.ableSaleDate between ? and ? order  by quota.shareType ");
        log.info(sb.toString());
        return super.query(sb.toString(), new Object[] { beginDate, endDate });
    }

    public HtlQuota findQuotaByBatchId(Date ableSaleDate, Long batchId, String shareType) {
        List lstQuota = super.queryByNamedQuery("findQuotaByBatchId", new Object[] { ableSaleDate,
            batchId, shareType });
        if (0 < lstQuota.size()) {
            return (HtlQuota) lstQuota.get(0);
        }
        return null;

    }

    /**
     * 列出一个酒店的所有房型
     */
    public List lstHotelRoomType(Long hotelId) {
        return super.queryByNamedQuery("lstHotelRoomType", new Object[] { hotelId });
    }

    /**
     * 通过房型id,酒店id,起止日期 找出合符条件的房间
     */
    public List lstRooms(Long hotelId, Long roomTypeId, String beginDate, String endDate) {
        // Date bDate = DateUtil.getDate(beginDate);
        // Date eDate = DateUtil.getDate(endDate);

        return super.queryByNamedQuery("lstRooms", new Object[] { hotelId, roomTypeId,
            java.sql.Date.valueOf(beginDate), java.sql.Date.valueOf(endDate) });
    }

    /**
     * 列出所有酒店
     */
    public List lstAllHotels(String status) {
        return super.queryByNamedQuery("lstAllHotels", new Object[] { status });
    }

    /**
     * 列出一个酒店所有有效配额
     */
    public List lstAllQuotas(Long hotelId) {
        return super.queryByNamedQuery("lstAllQuotas", new Object[] { hotelId,
            DateUtil.getSystemDate() });
    }

    /**
     * 查询配额不带星期的配额通过配额类型，共享方式，酒店id,房型id
     */
	@SuppressWarnings("unchecked")
	public List<HtlQuota> lstQuotaWithoutWeek(long roomType, String quotaType, long hotelId, Date beginDate,
        Date endDate) {
        return super.queryByNamedQuery("queryQuotaWithoutWeekEX", new Object[] { roomType,
            quotaType, hotelId, beginDate, endDate });
    }

    /**
     * 查询配额带星期
     * 
     * @param roomType
     *            房型id
     * @param quotaType
     *            配额类型
     * @param hotelId
     *            酒店id
     * @param beginDate
     *            开始日期
     * @param endDate
     *            结束日期
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<HtlQuota> lstQuotaWithWeekEX(long roomType, String quotaType, long hotelId, Date beginDate,
        Date endDate, String[] weeks) {

        StringBuilder sb = new StringBuilder();
        String sWeek = BizRuleCheck.ArrayToString(weeks);
        sb.append(" from HtlQuota quota where quota.room.roomTypeId = ? and quota.quotaType = ? ");
        sb.append(" and quota.hotelId = ? and week in (?) and quota.ableSaleDate between ? and ? ");
        sb.append(" order by quota.ableSaleDate, quota.shareType ");
        log.debug(sb.toString());
        return super.query(sb.toString(), new Object[] {roomType, quotaType, hotelId, sWeek, beginDate, endDate});
    }

    /**
     * 通过采购批次id，在数据层生成每一天的房间。最好是用storeProcedure. 要检查这个批次一定要配额，cutoffday。
     * 
     * @param purchaseBatchId
     *            采购批次id
     * @return 返回一个操作结果。
     */
    public int generateEveryDayRoom(long purchaseBatchId) {
        List lstRoom = new ArrayList();
        HtlQuotabatch quotaBatch = (HtlQuotabatch) super.find(HtlQuotabatch.class, purchaseBatchId);
        if (null == quotaBatch)
            return 0;
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(quotaBatch.getBeginDate());// set the begin date
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(quotaBatch.getEndDate());// set the end date
        if (quotaBatch.getBeginDate().before(quotaBatch.getEndDate())) {
            // 最后一天也要处理，所以最后一天加1
            String strEndDate = DateUtil.dateToString(DateUtil.getDate(quotaBatch.getEndDate(), 1));
            while (true) {

                // 检查这一天的配额是不是存在一条记录,如果存在，就不要新增一条记录
                //HtlQuota quota = null; // 面付配额记录
                //HtlQuota prepayQuota = null; // 预付配额
                Date bizDate = DateUtil.getDate(calStart.getTime());

                String strRoomType = "" + quotaBatch.getRoomType();
                // 写房间记录,检查这个房间是否存在。

                HtlRoom room = null;

                // 通过业务主健找出记录
                room = findRoomByBizKey(Long.parseLong(strRoomType), quotaBatch.getHotelId(),
                    bizDate);

                if (null == room) {
                    room = new HtlRoom();
                    room.setLastAssureTime(BizRuleCheck.getDefaultLastAssureTime());
                    room.setHasFree(BizRuleCheck.getFalseString());
                }

                room.setAbleSaleDate(calStart.getTime());
                room.setHotelId(quotaBatch.getHotelId());
                room.setRoomState("");
                room.setRoomTypeId(Long.parseLong(strRoomType));
                room.setContractId(quotaBatch.getContractId());
                room.setStatus(true);

                // 如果是面付配额,或者是面付预付共享，或者是面付，预付分别独占时要生成或者拿到一条面付记录
                if (BizRuleCheck.isFaceToPayQuota(quotaBatch.getShareType())
                    || BizRuleCheck.isQuotaShare(quotaBatch.getShareType())) {
                   // quota = getQuotaRecord(room, quotaBatch, bizDate, false);
                }

                // 如果是预付配额独占，或者是预付面付分别独占时要生成或者拿到一条预付记录
                if (BizRuleCheck.isPrepayQuota(quotaBatch.getShareType())) {
                   // prepayQuota = getQuotaRecord(room, quotaBatch, bizDate, true);
                }
                lstRoom.add(room);

                //
                calStart.add(Calendar.DATE, 1);
                String strCurrDate = DateUtil.dateToString(calStart.getTime());
                if (strCurrDate.equals(strEndDate)) {
                    break;
                }
            }
        }
        super.saveOrUpdateAll(lstRoom);
        return 0;
    }

//    private HtlQuota findQuotaInList(List lstQuota, String shareType, String quotaType) {
//        HtlQuota quota = null;
//        if (null != lstQuota) {
//            for (int i = 0; i < lstQuota.size(); i++) {
//                HtlQuota qa = (HtlQuota) lstQuota.get(i);
//                if (null != qa && (shareType.equals(qa.getShareType()))
//                    && (quotaType.equals(qa.getQuotaType()))) {
//                    quota = qa;
//                    break;
//                }
//            }
//        }
//        return quota;
//    }

//    private HtlQuota getQuotaRecord(HtlRoom room, HtlQuotabatch quotaBatch, Date bizDate,
//        boolean IsPrepay) {
//        HtlQuota quota = null;
//        HtlContract contract = (HtlContract) super.find(HtlContract.class, quotaBatch
//            .getContractId());
//        String st = ""; // 记录临时共享类型
//        int iRecord = 1; // 首先假设为一条记录
//        if (BizRuleCheck.isAlonePrivateQuota(quotaBatch.getShareType())) {
//            iRecord = 2;// 表示有两条记录,分别保存了面付独占，预付独占,共享类型为面预付分别独占
//        }
//        if (1 == iRecord) {
//            quota = findQuotaInList(room.getLstQuotas(), quotaBatch.getShareType(), quotaBatch
//                .getQuotaType());
//            st = quotaBatch.getShareType();
//        } else {
//            if (IsPrepay) {
//                quota = findQuotaInList(room.getLstQuotas(), BizRuleCheck.getPrepayQuotaValue(),
//                    quotaBatch.getQuotaType());
//                st = BizRuleCheck.getPrepayQuotaValue();
//            } else {
//                quota = findQuotaInList(room.getLstQuotas(), 
    //BizRuleCheck.getFaceToPayQuotaValue(),
//                    quotaBatch.getQuotaType());
//                st = BizRuleCheck.getFaceToPayQuotaValue();
//            }
//        }
//        // 如果没有则生成一条新记录
//        if (null == quota || null == quota.getID()) {
//            // 生成每天的配额数
//            quota = new HtlQuota();
//            /**
//             * 这里记录没有区分包配与普配,是一个bug。现已修改 xiaowu_mi quota.setQuotaType(st); quota.setShareType(st);
//             * 2007-7-11号修改
//             */
//
//            quota.setQuotaType(quotaBatch.getQuotaType());
//            quota.setQuotaBatchId(quotaBatch.getID());
//            quota.setHotelId(quotaBatch.getHotelId());
//            quota.setContractId(quotaBatch.getContractId());
//            quota.setTakebackQuota(quotaBatch.hasTakebackQuota());
//            quota.setAbleSaleDate(bizDate);
//            quota.setRoom(room);
//            room.getLstQuotas().add(quota);
//        }
//
//        // 求可用配额的数量
//        int ableQty = 0;
//        int totalQty = 0;
//
//        // 生成每天配额的cutoffDay
//        for (int i = 0; i < quotaBatch.getHtlBatchCutoffDays().size(); i++) {
//            // 得到采购批次的cutoffDay
//            HtlBatchCutoffDay batchCutoffDay = (HtlBatchCutoffDay) quotaBatch
//                .getHtlBatchCutoffDays().get(i);
//
//            // 检查这一天在不在这个cutoffDay的开始日期与结束日期并且是调整周当中
//            if (!BizRuleCheck.bizDateInAdjustDates(bizDate, batchCutoffDay.getBeginDate(),
//                batchCutoffDay.getEndDate(), batchCutoffDay.getAdjustWeek())) {
//                continue;
//            }
//            // quota.getLstAssign().clear();
//            // quota.getLstCutOffDay().clear();
//            HtlCutoffDayQuota cutDayQuota = null;
//            // 检查当前某一天的cutoffDay记录是否存在,存在就要更新配额数量
//            cutDayQuota = BizRuleCheck.checkCutoffDayIsExist(quota.getLstCutOffDay(),
//                batchCutoffDay.getCutoffDay());
//            boolean isNew = false;
//            if (null == cutDayQuota) {
//                cutDayQuota = new HtlCutoffDayQuota();
//                isNew = true;
//            }
//
//            cutDayQuota.setQuotaQty(batchCutoffDay.getQuantity());
//
//            cutDayQuota.setCutoffDay(batchCutoffDay.getCutoffDay());
//            cutDayQuota.setCutoffTime(batchCutoffDay.getCutoffTime());
//            cutDayQuota.setQuota(quota);
//
//            // 检查当前cutoffDay是否过期
//            if (BizRuleCheck.checkCutOffDayIsAvail(cutDayQuota.getCutoffDay(), cutDayQuota
//                .getCutoffTime(), bizDate)) {
//                cutDayQuota.setStatus(BizRuleCheck.getAvailStatus());
//            } else {
//                cutDayQuota.setStatus(BizRuleCheck.getNotAvailStatus());
//            }
//            if (isNew) {
//                quota.getLstCutOffDay().add(cutDayQuota);
//            }
//        }
//        // 重计算已录入的配额总数
//        for (int taq = 0; taq < quota.getLstCutOffDay().size(); taq++) {
//            HtlCutoffDayQuota cq = (HtlCutoffDayQuota) quota.getLstCutOffDay().get(taq);
//            if (BizRuleCheck.checkCutOffDayIsAvail(cq.getCutoffDay(),
//                cq.getCutoffTime(), bizDate)) {
//                // 配额
//                ableQty += cq.getQuotaQty();
//            }
//            totalQty += cq.getQuotaQty();
//        }
//        quota.setAbleQty(ableQty); // 设定配额
//        quota.setTotalQty(totalQty);
//        quota.setQuotaBatchId(quotaBatch.getID());// 更新为当前批次
//        quota.setShareType(st);
//        quota.setQuotaPattern(contract.getQuotaPattern());// 取到合同的配额模式，更新到每一个配额记录中
//        return quota;
//    }

    // /**
    // * 检查当前星期是不是在被选的星期集合之中
    // *
    // * @param currWeek
    // * 当前星期值 如,1
    // * @param allSeleWeek
    // * 星期集合，如：1,3,4,5
    // * @return
    // */
    // private boolean findSelectWeek(String currWeek, Object[] allSeleWeek) {
    // boolean bFlag = false;
    // if (null != allSeleWeek) {
    // for (int i = 0; i < allSeleWeek.length; i++) {
    // String s = (String) allSeleWeek[i];
    // if (s.equals(currWeek)) {
    // bFlag = true;
    // break;
    // }
    // }
    // }
    // return bFlag;
    // }

    /**
     * 批次释放配额
     * 
     * @param batchFreeQuota
     * @return 批次释放配额的id
     */

    // Modify by Xiaowu_mi 20071018
    public int saleBatchFreeQuota(HtlFreeOperate batchFreeQuota) {
        String[] weeks = batchFreeQuota.getAdjustWeek().split(",");
        List lstQuota = this.getQuotaByBizKeyWithDate(batchFreeQuota.getQuotaType(), batchFreeQuota
            .getShareType(), batchFreeQuota.getHotelId(), batchFreeQuota.getContractId(),
            batchFreeQuota.getRoomTypeId(), batchFreeQuota.getBeginDate(), batchFreeQuota
                .getEndDate(), weeks);
        for (int k = 0; k < lstQuota.size(); k++) {
            HtlQuota quota = (HtlQuota) lstQuota.get(k);
            if (null != quota) {
                quota = setupQuota(batchFreeQuota, quota);
            }
        }
        freeAloneQuotaByDays(lstQuota);
        return 0;
    }

    /**
     * public int saleBatchFreeQuota(HtlFreeOperate batchFreeQuota) { Calendar calStart =
     * Calendar.getInstance(); calStart.setTime(batchFreeQuota.getBeginDate()); // 释放配额开始日期 List
     * lstQuota = new ArrayList(); Calendar calEnd = Calendar.getInstance();
     * calEnd.setTime(batchFreeQuota.getEndDate());// set the end date
     * 
     * if (batchFreeQuota.getBeginDate().before(batchFreeQuota.getEndDate()) ) { String strEndDate =
     * DateUtil.dateToString(DateUtil.getDate( batchFreeQuota.getEndDate(), 1)); while (true) { Date
     * bizDate = DateUtil.getDate(calStart.getTime()); // 没有判断星期,所以导致释放配额的时候选星期无效,每天都产生了影响 add by
     * kun.chen 2007-10-18 if (BizRuleCheck.bizDateInAdjustDates(bizDate,
     * batchFreeQuota.getBeginDate(), batchFreeQuota.getEndDate(), batchFreeQuota.getAdjustWeek())){
     * // 取到这一天的配额 HtlQuota quota = findQuotaByBizKey( batchFreeQuota.getQuotaType(),
     * batchFreeQuota.getShareType(), batchFreeQuota.getHotelId(), batchFreeQuota.getContractId(),
     * bizDate,batchFreeQuota.getRoomTypeId()); if (quota != null) { quota =
     * setupQuota(batchFreeQuota, quota); lstQuota.add(quota); } for (int
     * j=0;j<batchFreeQuota.getLstBatchAssign().size();j++){ HtlBatchAssign
     * ba=(HtlBatchAssign)batchFreeQuota.getLstBatchAssign().get(j); HtlAssignCustom ac=null; for
     * (int i=0;i<quota.getLstAssign().size();i++){ ac=
     * (HtlAssignCustom)quota.getLstAssign().get(i); if (ac.getMemberType()==ba.getMemberType()){
     * break; } } if (ac==null){ ac= new HtlAssignCustom(); ac.setMemberType(ba.getMemberType());
     * quota.getLstAssign().add(ac); } if
     * ((ac.getSaledQuota()<=ba.getMaxAbleQuota())||ba.getMaxAbleQuota()==-1)
     * ac.setMaxAbleQuota(ba.getMaxAbleQuota()); else{ ac.setMaxAbleQuota(ac.getSaledQuota()); }
     * ac.setPrivateQuota(ba.getPrivateQuota()); ac.setQuotaId(quota.getID().longValue()); } }
     * calStart.add(Calendar.DATE, 1); String strCurrDate =
     * DateUtil.dateToString(calStart.getTime()); if (strCurrDate.equals(strEndDate)) { break; }
     * 
     * } } freeAloneQuotaByDays(lstQuota); return 0; }
     **/
    /**
     * 重新分配记录
     * 
     * @param batchFreeQuota
     *            批次释放操作记录
     * @param quota
     *            配额记录
     * @return
     */
    private HtlQuota setupQuota(HtlFreeOperate batchFreeQuota, HtlQuota pQuota) {

        // 设定分配表
        HtlQuota quota = pQuota;
        int allAssignCustom = 0;
        for (int j = 0; j < batchFreeQuota.getLstBatchAssign().size(); j++) {
            HtlAssignCustom ac = null;
            HtlBatchAssign bac = (HtlBatchAssign) batchFreeQuota.getLstBatchAssign().get(j);
            boolean findFlag = false;
            for (int i = 0; i < quota.getLstAssign().size(); i++) {
                ac = (HtlAssignCustom) quota.getLstAssign().get(i);
                if (ac.getMemberType() == bac.getMemberType()) {
                    findFlag = true;
                    break;
                }
            }
            if (!findFlag) {
                ac = null;
            }
            if (null == ac) {
                ac = new HtlAssignCustom();
                quota.getLstAssign().add(ac);
            }
            ac.setMaxAbleQuota(bac.getMaxAbleQuota());// 面付最大上限
            ac.setPrivateQuota(bac.getPrivateQuota()); // 面付独占
            allAssignCustom += bac.getPrivateQuota(); // 总共独占数

            ac.setQuotaId(quota.getID().longValue());
            ac.setMemberType(bac.getMemberType());
        }

        // 更新这一天的配额
        int temFreeQty = 0;// 释放数
        int shareQty = 0; // 共享数
        int privateQty = 0; // 独占数
        // 当配额总数小于将要释放的数量，那么就全部释放 否则就释放将要释放的数量
        // 首先扣减共享数，如果共享不够，再扣独占的，扣独占的配额要按会员的优先级开始扣，直到扣完为止
        if (quota.getAbleQty() < batchFreeQuota.getFreeQty()) {
            temFreeQty = quota.getAbleQty();
            // 将要释放配额比可销售配额多多少
            int spilthQty = batchFreeQuota.getFreeQty() - quota.getAbleQty();
            if (0 < batchFreeQuota.getShareQty() - spilthQty) {
                shareQty = batchFreeQuota.getShareQty() - spilthQty;
                privateQty = temFreeQty - shareQty;
            } else {// 共享的不够扣减
                shareQty = 0;
            }

        } else {
            temFreeQty = batchFreeQuota.getFreeQty();
            shareQty = batchFreeQuota.getShareQty();
        }
        privateQty = temFreeQty - shareQty;
        quota.setFreeQty(temFreeQty); // 释放数
        quota.setShareQty(shareQty);// 共享数
        quota.setPrivateQty(privateQty);// 独占 = 释放 - 共享, 独占 = 分配表独占配额之和。
        quota.setAvailQty(temFreeQty - quota.getUsedQty()); // 已释放未销售 = 释放 -已销售

        if (allAssignCustom > privateQty) {
            int cutAc = allAssignCustom - privateQty;
            List lstLevel = BizRuleCheck.getCutMemberQuotaLevel();
            for (int m = 0; m < lstLevel.size(); m++) {
                CodeName cn = (CodeName) lstLevel.get(m);
                for (int i = 0; i < quota.getLstAssign().size(); i++) {
                    HtlAssignCustom ac = (HtlAssignCustom) quota.getLstAssign().get(i);
                    if (ac.getMemberType() == Integer.parseInt(cn.getCode())) {
                        if (0 >= cutAc - (ac.getPrivateQuota() - ac.getSaledQuota())) {
                            ac.setPrivateQuota(ac.getPrivateQuota() - ac.getSaledQuota() - cutAc);
                            cutAc = 0;
                            break;
                        } else {
                            cutAc -= (ac.getPrivateQuota() - ac.getSaledQuota());
                            ac.setPrivateQuota(ac.getSaledQuota());
                        }
                        break;
                    }
                }
            }
        }
        return quota;
    }

    // /**
    // * 重新计算配额分配情况
    // */
    // private void reCalcQuotaAssign(HtlFreeOperate batchFreeQuota,
    // HtlQuota quota, int spilthQty) {
    // List lstLevel = BizRuleCheck.getCutMemberQuotaLevel();
    // // 已扣减数
    // int minus = spilthQty - batchFreeQuota.getShareQty();
    // for (int m = 0; m < lstLevel.size(); m++) {
    // CodeName cn = (CodeName) lstLevel.get(m);
    // for (int n = 0; n < quota.getLstAssign().size(); n++) {
    // HtlAssignCustom hac = (HtlAssignCustom) quota.getLstAssign()
    // .get(n);
    // if (hac.getMemberType() == Integer.parseInt(cn.getCode())) {
    // int pq = hac.getPrivateQuota();
    // if (pq > minus) {
    // hac.setPrivateQuota(pq - minus);
    // minus = 0;
    // } else {
    // minus -= pq;
    // hac.setPrivateQuota(0);
    // }
    // break;
    // }
    // }
    // // 当不需要扣减时退出
    // if (1 > minus) {
    // break;
    // }
    // }
    // }

    /**
     * 重新计算cutoffDay的配额 这是一个job来完成的事，但因业务还没有完全定型，所以暂时还不能写storeProcedure
     */
    public void calcCutoffDayQuota() {
        // 列出有效酒店，
        List lstHotels = lstAllHotels("1");

        // 循环酒店
        for (int i = 0; i < lstHotels.size(); i++) {
            Long hotelId = ((HtlHotel) lstHotels.get(i)).getID();
            List lstQuotas = lstAllQuotas(hotelId);
            // 循环配额
            for (int j = 0; j < lstQuotas.size(); j++) {
                HtlQuota quota = (HtlQuota) lstQuotas.get(j);
                quota = calcQuota(quota);
                super.saveOrUpdate(quota);
            }
        }
    }

    /**
     * 计算当前配额记录中一条配额记录的cutoff Day的有效配额
     */
    public HtlQuota calcQuota(HtlQuota quota) {

        // 注意规则定义 1、已使用数 = 过期已使用数 + 未过期已使用数
        // 2、平台可售数 = 总数 -过期数 + 过期已使用数 = 共享数 + 独占数 +
        // 已使用数+ 未释放数 = 已使用数 + 未使用数+ 未释放数 =
        // 过期已用数 + 已释放数 + 未释放数
        // 3、已释放数 = 共享数 + 独占数 + 已使用数 - 过期已使用数
        // 4、未使用数 = 共享数+独占数
        // 5、可释放数 = 总数 - 过期数

        int cutQuota = 0;
        int allQuota = 0;
        int ableQuota = 0; // 平台可销售数
        int useQuotaOLD = 0; // 原来过期使用数
        List lstCutoffDay = quota.getLstCutOffDay();
        Date day = DateUtil.getDate(DateUtil.getSystemDate(), -1);
        if (day.after(quota.getAbleSaleDate())) {
            return quota;
        }

        // 记录原来的平台可卖数
        int ableQtyOLD = quota.getAbleQty();
        // 记录原来的已释放数
        int freeQtyOLD = quota.getFreeQty();
        // 记录原来的总数
        int allQtyOLD = quota.getTotalQty();

        // 计算cutoffDay
        for (int k = 0; k < lstCutoffDay.size(); k++) {
            HtlCutoffDayQuota cq = (HtlCutoffDayQuota) lstCutoffDay.get(k);
            allQuota += cq.getQuotaQty();
            if ("A".equals(cq.getStatus())) {
                if (BizRuleCheck.checkCutOffDayIsAvail(cq.getCutoffDay(), cq.getCutoffTime(), quota
                    .getAbleSaleDate())) {
                    // 有效配额为面付配额 +预付配额 - 已使用的
                    ableQuota += cq.getQuotaQty();
                    // availQuota += (cq.getQuotaQty() - cq.getCutoffUsedQty());
                } else {
                    ableQuota += cq.getCutoffUsedQty();
                }
            } else {
                ableQuota += cq.getCutoffUsedQty();
                useQuotaOLD += cq.getCutoffUsedQty();
            }
        }
        quota.setTotalQty(allQuota);
        quota.setAbleQty(ableQuota); // 设定可销售配额
        /*
         * if(quota.getFreeQty()>quota.getAbleQty()){ quota.setFreeQty(quota.getAbleQty()); } if
         * ((quota.getAvailQty()+quota.getUsedQty())!=quota.getFreeQty()){
         * quota.setAvailQty(quota.getFreeQty()-quota.getUsedQty()); }
         */

        if (0 < allQtyOLD - allQuota) {
            // 如果没有释放的配额大于cut掉的，则表示不需要修改已释放的配额,也就是先cut掉未释放的就行了,否则就执行下面代码
            /*
             * cutQuota = cutQuota - (quota.getAbleQty() - quota.getFreeQty());// 这时已经cut掉了未释放的
             * quota.setFreeQty(quota.getFreeQty() - cutQuota);// 减少已释放的
             */
            // ableQty - freeQty = 未释放配额数
            cutQuota = allQtyOLD - allQuota;// 这个计算表示现在需要cut掉的配额数
            // (原平台可销售-原已释放数-过期已用数)=原来的未释放数

            // 如果现在需要cut的数大于或等于原来未释放数，则需要用 原来释放数-（现在需要cut数-原来未释放数）= 现在已释放数 ,否则现在已释放数等于原来释放数

            if (cutQuota > (ableQtyOLD - freeQtyOLD - useQuotaOLD)) {
                quota.setFreeQty(quota.getFreeQty()
                    - (cutQuota - (ableQtyOLD - freeQtyOLD - useQuotaOLD)));
                quota.setAvailQty(quota.getFreeQty() - quota.getUsedQty() + useQuotaOLD);
                if ((cutQuota - (ableQtyOLD - freeQtyOLD - useQuotaOLD)) > 
                quota.getShareQty()) {// 再来与共享的比较，如果比共享的大
                    int priQty = (cutQuota - (ableQtyOLD - freeQtyOLD - useQuotaOLD))
                        - quota.getShareQty();
                    // 将要扣减的独占配额
                    quota.setShareQty(0);// 扣完共享配额
                    quota.setPrivateQty(quota.getPrivateQty() - priQty);// 扣减独占的
                    // 共享配额不够扣减,就要循环扣减会员独占配额
                    List lstLevel = BizRuleCheck.getCutMemberQuotaLevel();
                    for (int m = 0; m < lstLevel.size(); m++) {
                        CodeName cn = (CodeName) lstLevel.get(m);
                        for (int n = 0; n < quota.getLstAssign().size(); n++) {
                            HtlAssignCustom hac = (HtlAssignCustom) quota.getLstAssign().get(n);
                            if (hac.getMemberType() == Integer.parseInt(cn.getCode())) {
                                int pq = hac.getPrivateQuota();
                                if (pq > priQty) {
                                    hac.setPrivateQuota(pq - priQty);
                                    priQty = 0;
                                } else {
                                    priQty -= pq;
                                    hac.setPrivateQuota(0);
                                }
                                break;
                            }
                        }
                        // 当不需要扣减时退出
                        if (1 > priQty) {
                            break;
                        }
                    }
                } else {
                    // 共享配额够扣减,直接扣减共享配额
                    quota.setShareQty(quota.getShareQty()
                        - (cutQuota - (ableQtyOLD - freeQtyOLD - useQuotaOLD)));
                }

            }

        }
        return quota;
    }

    // /**
    // * 是不是有效日期，如果不是有效日期，则返回false
    // */
    // private boolean isAvailableDay(Date recordDate, int cutDay) {
    // boolean result = false;
    // Date sysDate = DateUtil.getSystemDate();
    // int day = DateUtil
    // .getDay(sysDate, DateUtil.getDate(recordDate, cutDay));
    // if (0 <= day) {
    // result = true;
    // }
    // return result;
    // }

    /**
     * 通过酒店id,开始日期，结束日期,选择的星期，选择的房型 找出房间列表
     * 
     * @param hotelId
     *            酒店id
     * @param beginDate
     *            开始日期
     * @param endDate
     *            结束日期
     * @param week
     *            选择的星期
     * @param roomTypes
     *            选择的房型
     * @return 房间列表
     */
    public List<HtlRoom> qryRoomState(long hotelId, Date beginDate, Date endDate, String[] weeks,
        String[] roomTypes) {
        Criteria criteria = super.getCurrentSession().createCriteria(HtlRoom.class);
        criteria.add(Restrictions.eq("hotelId", hotelId));
        criteria.add(Restrictions.between("ableSaleDate", beginDate, endDate));

        if (!BizRuleCheck.isAllWeek(weeks)) {
            Collection<Integer> lstWeeks = new ArrayList<Integer>(weeks.length);
            for (String week:weeks) {
                lstWeeks.add(Integer.valueOf(week));
            }
            criteria.add(Restrictions.in("week", lstWeeks));
        }

        Collection<Long> lstRoomTypes = new ArrayList<Long>(roomTypes.length);
        if (null != roomTypes) {
            for (String roomtype:roomTypes) {
                lstRoomTypes.add(Long.valueOf(roomtype));
            }
            criteria.add(Restrictions.in("roomTypeId", lstRoomTypes));
        }
        criteria.addOrder(Order.asc("ableSaleDate"));
        return criteria.list();
    }

    /**
     * 当更新预订条款的时候，要更新每一天的担保情况
     * 
     * @param bDate
     *            开始日期
     * @param eDate
     *            结束日期
     * @param roomTypes
     *            房型数组
     * @param hotelId
     *            酒店ID
     * @return 如果操作成功，返回0，如果操作失败，返回一个非0数
     */
    public int updateRoomLastAssureTime(Date bDate, Date eDate, String[] roomTypes, long hotelId,
        String lastAssureTime) {
        int result = -1;
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(bDate);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(eDate);
        // 表示不需要更新房型
        if (null == roomTypes || 0 == roomTypes.length) {
            return 0;
        }
        if (bDate.before(eDate)) {
            while (true) {
                // 最后一天也要处理，所以最后一天加1
                String strEndDate = DateUtil.dateToString(DateUtil.getDate(eDate, 1));

                for (int n = 0; n < roomTypes.length; n++) {
                    long roomTypeId = Long.parseLong(roomTypes[n]);
                    Date ableSaleDate = DateUtil.getDate(calStart.getTime());
                    saveOrUpdateRoomWithTime(roomTypeId, hotelId, ableSaleDate, lastAssureTime);
                }
                calStart.add(Calendar.DATE, 1);
                String strCurrDate = DateUtil.dateToString(calStart.getTime());
                if (strCurrDate.equals(strEndDate)) {
                    break;
                }
            }
        }
        result = 0;
        return result;
    }

    private void saveOrUpdateRoomWithTime(long roomTypeId, long hotelId, Date ableSaleDate,
        String lastAssureTime) {
        HtlPriceType htlPriceType = (HtlPriceType) super.find(HtlPriceType.class, roomTypeId);

        List lstRoom = super.queryByNamedQuery("findRoomByBizKey", new Object[] {
            htlPriceType.getRoomType().getID(), hotelId, ableSaleDate });
        HtlRoom room;
        if (0 < lstRoom.size()) {
            room = (HtlRoom) lstRoom.get(0);
            room.setLastAssureTime(lastAssureTime);
        } else {
            room = new HtlRoom();

            room.setRoomTypeId(htlPriceType.getRoomType().getID());
            room.setHotelId(hotelId);
            room.setAbleSaleDate(ableSaleDate);
            room.setLastAssureTime(lastAssureTime);
        }
        super.saveOrUpdate(room);
    }

    /**
     * 删除这个合同的配额及价格
     * 
     * @param contractId
     *            合同Id
     * @param bDate
     *            起始日期
     * @param eDate
     *            结束日期
     * @return 操作结束 1成功 0失败 如果效率不行，要改成存储过程
     */
    public int removeQuotaAndPrice(Long contractId, Date bDate, Date eDate) {
        List lstRoom = super.queryByNamedQuery("findRoomByContractId", new Object[] { contractId,
            bDate, eDate });
        for (int i = 0; i < lstRoom.size(); i++) {
            HtlRoom room = (HtlRoom) lstRoom.get(i);
            super.remove(room);
        }

        return 1;
    }

    /**
     * 检查合同的起止时间是否重复
     * 
     * @param contract
     * @return 如果重复就返回1，否则就返回0
     */
    public int checkContractDate(long pHotelId, Date beginD, Date endD) {
        List lstContract = super.queryByNamedQuery("checkContractDate", new Object[] { pHotelId,
            beginD, endD, beginD, endD, beginD, endD });
        if (null != lstContract) {
            if (0 < lstContract.size()) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * 修改合同信息时检查合同的起止时间是否重复
     * 
     * @param contract
     * @return 如果重复就返回1，否则就返回0
     */
    public int checkEditContractDate(long pHotelId, Date beginD, Date endD, Date beginDOld,
        Date endDOld) {
        List lstContract = super.queryByNamedQuery("checkEditContractDate", new Object[] {
            pHotelId, beginD, beginDOld, beginDOld, beginD, endD, endDOld, endDOld, endD });
        if (null != lstContract) {
            if (0 < lstContract.size()) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * 检查区域和城市信息是否存在
     * 
     * @param
     * @return 如果存在就返回1，否则就返回0
     */
    public int checkAreaExist(String areaCode, String cityCode) {
        List lstArea = super.queryByNamedQuery("checkAreaExist",
            new Object[] { areaCode, cityCode });
        if (null != lstArea) {
            if (0 < lstArea.size()) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * 通过房间ID查询临时配额
     * 
     * @param roomId
     * @return
     */
    public List getTempQuotaByRoomId(Long roomId) {
        return super.queryByNamedQuery("getTempQuotaByRoomId", new Object[] { roomId });
    }

    public int deleteQuota(final Long Id) throws HibernateException {
        HibernateCallback cb = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                long lId = Id.longValue();
                String Hql = " delete HtlCutoffDayQuota c where c.quota.ID = ?";
                Query query = session.createQuery(Hql);
                query.setLong(0, lId);
                query.executeUpdate();

                Hql = " delete HtlAssignCustom a where a.quotaId=?";
                Query query1 = session.createQuery(Hql);
                query1.setLong(0, lId);
                query1.executeUpdate();

                Hql = " delete HtlQuota a where a.ID=?";
                Query query2 = session.createQuery(Hql);
                query2.setLong(0, lId);
                query2.executeUpdate();
                return 0;
            }
        };

        Integer ret = (Integer) getHibernateTemplate().execute(cb);
        if (null == ret) {
            return -1;
        } else {
            return ret.intValue();
        }

    }

    public int updateRoomHasFree(final Long Id) throws HibernateException {
        HibernateCallback cb = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                long lId = Id.longValue();
                String Hql = " update HtlRoom c set c.hasFree=" + "" + BizRuleCheck.getTrueString()
                    + " where c.ID = ?";
                Query query = session.createQuery(Hql);
                query.setLong(0, lId);
                query.executeUpdate();
                return 0;
            }
        };

        Integer ret = (Integer) getHibernateTemplate().execute(cb);
        if (null == ret) {
            return -1;
        } else {
            return ret.intValue();
        }

    }

    public int freeAloneQuotaByDays(List lstQuota) {

        for (int i = 0; i < lstQuota.size(); i++) {
            HtlQuota quota = (HtlQuota) lstQuota.get(i);

            HtlRoom room = quota.getRoom();
            if (null != room) {
                // room.setHasFree(BizRuleCheck.getTrueString());
                if (!room.getHasFree().equals(BizRuleCheck.getTrueString())) {
                    room.setHasFree(BizRuleCheck.getTrueString());
                    saveOrUpdate(room);
                }
            } else {
                log.info("room is null quota.id=" + quota.getID());
            }
            // this.updateRoomHasFree(quota.getRoom().getID());
        }
        this.saveOrUpdateAll(lstQuota);
        return 1;
    }

    /**
     * 检查合同的起止时间是否重复
     * 
     * @param contract
     * @return 如果重复就返回1，否则就返回0
     */
    public int checkRoomType(long pHotelId, long roomTypeID, String roomName) {
        List lstRoomType = new ArrayList();
        if (0 != roomTypeID)
            lstRoomType = super.queryByNamedQuery("checkRoomTypeWithID", new Object[] { pHotelId,
                roomName, roomTypeID });
        else
            lstRoomType = super.queryByNamedQuery("checkRoomType", new Object[] { pHotelId,
                roomName });
        if (null != lstRoomType) {
            if (0 < lstRoomType.size()) {
                return 1;
            }
        }
        return 0;
    }

    public String saveOrUpdatePrice(long hotelId, long contractId, String quotaType,
        String currency, String week) throws SQLException {
        CallableStatement cstmt = null;
        try {

            String procedureName = "{call SP_HOTEL_BATCHPRICE(?,?,?,?,?,?)} ";
            cstmt = super.getCurrentSession().connection().prepareCall(
                procedureName);

            cstmt.setLong(1, hotelId);
            cstmt.setLong(2, contractId);
            cstmt.setString(3, quotaType);
            cstmt.setString(4, currency);
            cstmt.setString(5, week);
            cstmt.registerOutParameter(6, oracle.jdbc.driver.OracleTypes.VARCHAR);

            cstmt.executeUpdate();
            cstmt.close();
            return "1";
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
            log.error(" callUpdatePriceStoreProcedure error: SaleDao error!");
            cstmt.close();
            return "0";
        }finally{
            cstmt.close();
        }
    }

    /**
     * 加幅调用存储过程修改价格 add by zhineng.zhuang 2008-12-01
     * 
     * @return
     * @throws SQLException 
     */
    public String saveAddScopePrice(HtlHdlAddscope htlHdlAddscope) throws SQLException {
        CallableStatement cstmt = null;
        try {
            String procedureName = "{call SP_HOTEL_ADDSCOPEPRICE(?,?,?,?,?,?)}";
            cstmt = super.getCurrentSession().connection().prepareCall(
                procedureName);
            cstmt.setLong(1, htlHdlAddscope.getHotelId());
            cstmt.setLong(2, htlHdlAddscope.getAddScope());
            cstmt.setString(3, DateUtil.dateToString(htlHdlAddscope.getBeginDate()));
            cstmt.setString(4, DateUtil.dateToString(htlHdlAddscope.getEndDate()));
            cstmt.setString(5, htlHdlAddscope.getAllPriceTypeId());
            cstmt.registerOutParameter(6, oracle.jdbc.driver.OracleTypes.VARCHAR);
            cstmt.executeUpdate();
            Object o = cstmt.getObject(6);
            cstmt.close();
            return o.toString();
        } catch (Exception e) {
            log.error("call SP_ADDSCOPEPRICE ERROR");
            cstmt.close();
            return "error";
        }finally{
            cstmt.close();
        }
    }

    public void saveOrUpdatePrice(List lstPrice) {

        super.saveOrUpdateAll(lstPrice);
    }

    public void saveOrUpdatePrice() throws SQLException {
        CallableStatement cstmt = null;
        try {

            String procedureName = "{call PKGMTNPRICE.mtnPrice()} ";
            cstmt = super.getCurrentSession().connection().prepareCall(
                procedureName);

            cstmt.execute();
            cstmt.close();

        } catch (Exception e) {
            cstmt.close();
            log.error(" saveOrUpdatePrice()--- "
                + "callUpdatePriceStoreProcedure error: SaleDao error!");
            log.error(e.getMessage(),e);

        }finally{
            cstmt.close();
        }
    }

    public int checkContinueContractDate(long hotelId, long contractId, Date oldBeginDate,
        Date continueDate) {
        List lstContract = super.queryByNamedQuery("checkContinueContractDate", new Object[] {
            hotelId, contractId, oldBeginDate, continueDate });
        if (null != lstContract) {
            if (0 < lstContract.size()) {
                return 1;
            }
        }
        return 0;
    }

    public List<ContinueDatecomponent> checkContinuePrice(long hotelId, long contractId,
        List<ContinueDatecomponent> dateComponents) {
        List<ContinueDatecomponent> resultList = new ArrayList();
        for (int i = 0; i < dateComponents.size(); i++) {
            ContinueDatecomponent component = dateComponents.get(i);
            Date beginDate = java.sql.Date.valueOf(component.getBeginDate());
            Date endDate = java.sql.Date.valueOf(component.getEndDate());
            long roomTypeId = 0;
            long childRoomTypeId = 0;
            String[] kk = component.getIds().split("&&");
            if (null != kk && 1 < kk.length) {
                roomTypeId = Long.valueOf(kk[0]).longValue();
                childRoomTypeId = Long.valueOf(kk[1]).longValue();
            }
            List lstPrices = super.queryByNamedQuery("checkContinuePrice", new Object[] { hotelId,
                roomTypeId, childRoomTypeId, beginDate, endDate });
            if (null != lstPrices && 0 < lstPrices.size()) {
                resultList.add(component);
            }
        }
        return resultList;
    }

    /**
     * 检查房型是否有关房历史 add by zhineng.zhuang 2008-10-30
     * 
     * @param hotelId
     * @param childRoomTypeId
     * @param beginDate
     * @param EndDate
     * @return
     */
    public List checkCloseHistory(long hotelId, String childRoomTypeId,
        List<ContinueDatecomponent> dateComponents) {
        String[] childRId = childRoomTypeId.split(",");
        List lisOpenCloseRoom = new ArrayList();
        for (int j = 0; j < childRId.length; j++) {
            for (int i = 0; i < dateComponents.size(); i++) {
                ContinueDatecomponent continueDatecomponent = dateComponents.get(i);
                Date beginDate = DateUtil.getDate(continueDatecomponent.getBeginDate());
                Date endDate = DateUtil.getDate(continueDatecomponent.getEndDate());
                lisOpenCloseRoom = super.queryByNamedQuery("queryHtlOpenClose", new Object[] {
                    hotelId, childRId[j], beginDate, endDate, beginDate, endDate, beginDate,
                    endDate });
            }
        }
        List lisReason = new ArrayList();
        Set hs = new HashSet();
        HtlOpenCloseRoom htlOpenCloseRoom = new HtlOpenCloseRoom();
        for (int i = 0; i < lisOpenCloseRoom.size(); i++) {
            Object[] obj = new Object[2];
            htlOpenCloseRoom = (HtlOpenCloseRoom) lisOpenCloseRoom.get(i);
            obj[0] = htlOpenCloseRoom.getRoomType();
            obj[1] = htlOpenCloseRoom.getCauseSign();
            Object o = htlOpenCloseRoom.getRoomType() + htlOpenCloseRoom.getCauseSign();
            if (!lisReason.contains(o)) {
                lisReason.add(o);
                hs.add(obj);
            }
        }
        List liNoSameReason = new ArrayList(hs);
        return liNoSameReason;
    }

    /**
     * 查询呼出配额
     * 
     * @param roomType
     * @param hotelId
     * @param beginDate
     * @param endDate
     * @return
     */
    public List lstOutsideQuota(long roomType, long hotelId, Date beginDate, Date endDate) {
        // TODO Auto-generated method stub
        return super.queryByNamedQuery("queryOutsideQuota", new Object[] { roomType, hotelId,
            beginDate, endDate });
    }

    /**
     * 查询临时配额
     * 
     * @param hotelId
     * @param childRoomTypeId
     * @param beginDate
     * @param EndDate
     * @return
     */
    public List lstTempQuota(long roomType, long hotelId, Date beginDate, Date endDate) {
        // TODO Auto-generated method stub
        return super.queryByNamedQuery("queryTempQuota", new Object[] { roomType, hotelId,
            beginDate, endDate });
    }

    /**
     * 检查区域by cityCode
     * 
     * @param
     * @return HtlArea
     */
    public HtlArea queryAreaCode(String cityCode) {
        List list = super.queryByNamedQuery("queryAreaCode", new Object[] { cityCode });
        if (null != list && 0 < list.size()) {
            return (HtlArea) list.get(0);
        } else {
            return null;
        }
    }
    
    /**
     * 查询临时配额
     * add by haibo.li 临时配额改造,改查询另外的表
     */
    public List queryTempQuota(long hotelId,long roomTypeid,Date date,String quotaType,String holder,String bedID){
//    	return super.queryByNamedQuery("qryTempQuota", new Object []{hotelId,roomTypeid,date,quotaType,share,holder});
    	 List dataLis = new ArrayList();
    	 
    	 Long bedL = 0L;
    	 
    	 if(bedID!=null&&!"".equals(bedID)){
    		 
    		 bedL = Long.parseLong(bedID);
    		 
    	 }
         
    	 String sqlStr = "select new list (quotanew as quotanewPara,NVL(quota.forewarnQuotaNum,0) as forewarnQuotaNumPara,\n"+
    	 				 " NVL(quota.forewarnFlag,0) as forewarnFlagPara,\n"+
    	 				 " (NVL(quota.commonQuotaAbleNum,0)+NVL(quota.buyQuotaAbleNum,0)-NVL(quota.commonQuotaOutofdateNum,0)-NVL(quota.buyQuotaOutofdateNum,0)) as canUserQuotaNum ,\n"+
    	 				 " (NVL(quota.casualQuotaAbleNum,0)-NVL(quota.casualQuotaOutofdateNum,0))as casualQuotaAbleNum,quota.quotaPattern as quotaPattern,(NVL(quota.buyQuotaSum,0)+NVL(quota.commonQuotaSum,0)+NVL(quota.casualQuotaSum,0)) as quotaNum, \n"+
    	 				 " NVL(quota.casualQuotaUsedNum,0) as casualQuotaUsedNum, (NVL(quota.buyQuotaUsedNum,0)+NVL(quota.commonQuotaUsedNum,0)) as usedQuotaNumPara )\n"+
    	 				 " from HtlQuotaCutoffDayNew quotanew,HtlQuotaNew quota \n"+
    	 				 " where quotanew.quotaId = quota.ID and quotanew.cutoffday = 0 \n"+
    	 				 " and  quotanew.hotelId = ? and quotanew.roomtypeId=? \n"+
   			  			 " and  quotanew.ableDate = ? and quotanew.quotaType in (?,?) and  quotanew.quotaHolder = ? \n"+
   			  			 " and quotanew.bedId = ?  order by  quotanew.quotaShare asc ";
         
    	 Object[] obj = new Object[] { hotelId, roomTypeid, date, quotaType,HotelBaseConstantBean.GENERALQUOTA,holder,bedL};
    	 
         dataLis = super.query(sqlStr, obj);
         
         return dataLis;
    	
    }
    
    //配额该造，根据相关查询条件，查询配额总表的相关信息 add by shengwei.zuo 2009-10-19
    public List queryTempQuotaNew(long hotelId,long roomTypeid,Date date,String quotaType,String holder,String bedId){
    		
    	    List  dataList   = new  ArrayList();
    	
    		Long bedIdL = Long.parseLong(bedId);
    		
    		String  sqlStr = 	"select new list (quotanew.ID,NVL(quotanew.casualQuotaSum,0) as casualQuotaSum,NVL(quotanew.forewarnQuotaNum,0) as forewarnQuotaNum,\n"+
							 	" NVL(quotanew.forewarnFlag,0) as forewarnFlag,\n"+
								 " (NVL(quotanew.commonQuotaAbleNum,0)+NVL(quotanew.buyQuotaAbleNum,0)-NVL(quotanew.commonQuotaOutofdateNum,0)-NVL(quotanew.buyQuotaOutofdateNum,0)) as canUserQuotaNum ,\n"+
								 " (NVL(quotanew.casualQuotaAbleNum,0)-NVL(quotanew.casualQuotaOutofdateNum,0))as casualQuotaAbleNum ,quotanew.quotaPattern as quotaPattern,(NVL(quotanew.buyQuotaSum,0)+NVL(quotanew.commonQuotaSum,0)+NVL(quotanew.casualQuotaSum,0)) as quotaNum ,\n"+
								 " NVL(quotanew.casualQuotaUsedNum,0) as casualQuotaUsedNum, (NVL(quotanew.buyQuotaUsedNum,0)+NVL(quotanew.commonQuotaUsedNum,0)) as usedQuotaNumPara) \n"+
								 " from HtlQuotaNew quotanew where quotanew.hotelId = ? and  quotanew.roomtypeId=? \n"+
								 " and quotanew.ableSaleDate = ?  and   quotanew.quotaHolder = ? \n"+
								 " and quotanew.bedId = ? order by  quotanew.quotaShareType asc";
    		
    		 Object[] obj = new Object[] {hotelId,roomTypeid,date,holder,bedIdL};
        	 
             dataList = super.query(sqlStr, obj);
             
             return dataList;
    		
    		//return super.queryByNamedQuery("qryTempQuotaNew", new Object []{hotelId,roomTypeid,date,holder,bedIdL});
    	
    }

	public List<HtlQuotaNew> queryTempQuotaByRoomState(Long hotelId, Long roomType, Date saleDate, String bedId) {
		String sqlStr="from HtlQuotaNew quotanew where quotanew.hotelId = ? and  quotanew.roomtypeId=? and quotanew.ableSaleDate = ? and quotanew.bedId = ? and   quotanew.quotaHolder = ?";
		Object[] obj=new Object[]{hotelId,roomType,saleDate,Long.parseLong(bedId),"CC"};
		return super.query(sqlStr,obj);
	}
	
	@SuppressWarnings("unchecked")
	public List<HtlQuotaNew> queryQuotaByRoomTypeId(Long hotelId,Long roomTypeId,Date checkinDate,Date checkoutDate){
		
		return super.queryByNamedQuery("qryTempQuotaNewNotBedId",new Object[]{hotelId,roomTypeId,checkinDate,checkoutDate});
		
	}
	
	
	public List getHtlOpenClose(Object[] obj){
	      return super.queryByNamedQuery("queryHtlOpenClose",obj);
	}
	
}