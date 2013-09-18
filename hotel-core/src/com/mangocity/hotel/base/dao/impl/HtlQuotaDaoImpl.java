package com.mangocity.hotel.base.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mangocity.hotel.base.dao.HtlQuotaDao;
import com.mangocity.hotel.base.persistence.HtlAssignCustom;
import com.mangocity.hotel.base.persistence.HtlBatchAssign;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlCutoffDayQuota;
import com.mangocity.hotel.base.persistence.HtlFreeOperate;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlQuota;
import com.mangocity.hotel.base.persistence.HtlQuotaNew;
import com.mangocity.hotel.base.persistence.HtlQuotabatch;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.base.persistence.HtlTempQuota;
import com.mangocity.hotel.base.persistence.QuotaForCC;
import com.mangocity.hotel.base.service.assistant.QuotaQuery;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.util.CodeName;
import com.mangocity.util.DateUtil;
import com.mangocity.util.ValidationUtil;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

/**
*@author chenhuizhong e-mail:huizong.chen@mangocity.com
*@version 1.0
*@since 1.0
*/
public class HtlQuotaDaoImpl extends GenericDAOHibernateImpl implements HtlQuotaDao
{
    
    public List<HtlQuotaNew> queryTempQuotaByRoomState(Long hotelId, Long roomType,
            Date saleDate, String bedId) {
        
        String sqlStr = "from HtlQuotaNew quotanew where quotanew.hotelId = ? and  quotanew.roomtypeId=?" +
        		" and quotanew.ableSaleDate = ? and quotanew.bedId = ? and   quotanew.quotaHolder = ?";
        String quotaHolder = "CC";
        Object[] obj = new Object[] { hotelId, roomType, saleDate, Long.parseLong(bedId), quotaHolder };
        
        return this.query(sqlStr, obj);
    }
    
    public List<HtlQuotaNew> queryTempQuotaNew(long hotelId, long roomTypeid, Date date,
            String quotaType, String holder, String bedId) {

        Long bedIdL = Long.parseLong(bedId);

        String sqlStr = " select new list (quotanew.ID,NVL(quotanew.casualQuotaSum,0) as casualQuotaSum,NVL(quotanew.forewarnQuotaNum,0) as forewarnQuotaNum,"
                + " NVL(quotanew.forewarnFlag,0) as forewarnFlag  (NVL(quotanew.commonQuotaAbleNum,0)+NVL(quotanew.buyQuotaAbleNum,0)"
                + " -NVL(quotanew.commonQuotaOutofdateNum,0)-NVL(quotanew.buyQuotaOutofdateNum,0)) as canUserQuotaNum ,"
                + " (NVL(quotanew.casualQuotaAbleNum,0)-NVL(quotanew.casualQuotaOutofdateNum,0))as casualQuotaAbleNum , " 
                + " quotanew.quotaPattern as quotaPattern,(NVL(quotanew.buyQuotaSum,0)+NVL(quotanew.commonQuotaSum,0)+"
                + " NVL(quotanew.casualQuotaSum,0)) as quotaNum , NVL(quotanew.casualQuotaUsedNum,0) as casualQuotaUsedNum," 
                + " (NVL(quotanew.buyQuotaUsedNum,0)+NVL(quotanew.commonQuotaUsedNum,0)) as usedQuotaNumPara) "
                + " from HtlQuotaNew quotanew where quotanew.hotelId = ? and  quotanew.roomtypeId=? "
                + " and quotanew.ableSaleDate = ?  and   quotanew.quotaHolder = ? "
                + " and quotanew.bedId = ? order by  quotanew.quotaShareType asc";

        Object[] obj = new Object[] { hotelId, roomTypeid, date, holder, bedIdL };

        return this.query(sqlStr, obj);
    }

	public int generateEveryDayRoom(long purchaseBatchId) {
		List<HtlRoom> htlRooms = new ArrayList<HtlRoom>();
        HtlQuotabatch quotaBatch = (HtlQuotabatch) super.get(HtlQuotabatch.class, purchaseBatchId);
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

                HtlRoom htlRoom = null;

                // 通过业务主健找出记录
                htlRoom = findRoomByBizKey(Long.parseLong(strRoomType), quotaBatch.getHotelId(),
                    bizDate);

                if (null == htlRoom) {
                    htlRoom = new HtlRoom();
                    htlRoom.setLastAssureTime(BizRuleCheck.getDefaultLastAssureTime());
                    htlRoom.setHasFree(BizRuleCheck.getFalseString());
                }

                htlRoom.setAbleSaleDate(calStart.getTime());
                htlRoom.setHotelId(quotaBatch.getHotelId());
                htlRoom.setRoomState("");
                htlRoom.setRoomTypeId(Long.parseLong(strRoomType));
                htlRoom.setContractId(quotaBatch.getContractId());
                htlRoom.setStatus(true);

                // 如果是面付配额,或者是面付预付共享，或者是面付，预付分别独占时要生成或者拿到一条面付记录
                if (BizRuleCheck.isFaceToPayQuota(quotaBatch.getShareType())
                    || BizRuleCheck.isQuotaShare(quotaBatch.getShareType())) {
                }

                // 如果是预付配额独占，或者是预付面付分别独占时要生成或者拿到一条预付记录
                if (BizRuleCheck.isPrepayQuota(quotaBatch.getShareType())) {
                   // prepayQuota = getQuotaRecord(room, quotaBatch, bizDate, true);
                }
                htlRooms.add(htlRoom);

                //
                calStart.add(Calendar.DATE, 1);
                String strCurrDate = DateUtil.dateToString(calStart.getTime());
                if (strCurrDate.equals(strEndDate)) {
                    break;
                }
            }
        }
        super.saveOrUpdateAll(htlRooms);
        return 0;
	}
    
	public HtlAssignCustom findHtlAssignCustom(long memberType, long quotaId) {
        String sqlStr = "FROM HtlAssignCustom WHERE quota_id = ? and member_type = ?";
        Object[] paraLong = new Object[] { quotaId, memberType };
        return (HtlAssignCustom)super.query(sqlStr, paraLong, 1, 0, false).get(0);
    }
	
	 public HtlRoom qryHtlRoomForCC(long roomTypeID, Date date) {
	        String hsql = "FROM HtlRoom WHERE room_type_id =? AND able_sale_date = ?";
	        Object[] obj = new Object[] { roomTypeID, date };
	        return (HtlRoom) super.query(hsql, obj,1,0,false).get(0);
	    }
	 
	 public HtlPrice qryHtlPriceForCC(long childRoomId, Date date, 
		        String payMethod, String quotaType) {
		        String hsql = "FROM HtlPrice WHERE able_sale_date = ? "
		            + "AND child_room_type_id =? AND pay_method =? " + "AND quota_type = ?";
		        Object[] obj = new Object[] { date, childRoomId, payMethod, quotaType };
		        return (HtlPrice) super.query(hsql, obj,1,0,false).get(0);
		    }
	 
	 public HtlQuota qryHtlQuotaForCC(long roomId, String quotaType, String shareType) {
	        String hsql = "FROM HtlQuota WHERE room_id =? AND share_type =? AND quota_type =?";
	        Object[] obj = new Object[] { roomId, shareType, quotaType };
	        return (HtlQuota) super.query(hsql, obj,1,0,false).get(0);
	    }
	 
	 public void updateQuotaForCC(QuotaForCC quotaForCC) {
	         super.update(quotaForCC);
	 }
	 
	 public HtlContract qryHtlcontractForCC(long hotelId, Date beginDate) {
	        String hsql = "FROM HtlContract WHERE begin_date <= ? AND end_date>=? AND hotel_id =?";
	        Object[] obj = new Object[] { beginDate, beginDate, hotelId };
	        return (HtlContract) super.query(hsql, obj,1,0,false).get(0);
	    }
	 
	 public QuotaForCC queryQuotaCharter(QuotaQuery quotaQuery, Date queryDate, int para) {
	        QuotaForCC quotaForCC = new QuotaForCC();
	        Criteria criteria = super.getSession().createCriteria(QuotaForCC.class);
	        criteria.add(Restrictions.eq("hotelId", quotaQuery.getHotelId()));
	        criteria.add(Restrictions.eq("ableSaleDate", queryDate));
	        criteria.add(Restrictions.eq("quotaType", quotaQuery.getQuotaType()));

	        if ("2".equals(quotaQuery.getQuotaType())) {
	            // 包房配额只有预付独占
	            criteria.add(Restrictions.eq("shareType", "2"));
	        } else if ("1".equals(quotaQuery.getQuotaType())) {
	            if (1 == para) {
	                criteria.add(Restrictions.eq("shareType", "2"));
	            } else if (2 == para) {
	                criteria.add(Restrictions.eq("shareType", "1"));
	            } else if (3 == para) {
	                criteria.add(Restrictions.eq("shareType", "3"));
	            }

	        }
	        List resultLis = criteria.list();

	        if (null != resultLis && 1 == resultLis.size()) {
	            Iterator iter = resultLis.iterator();
	            if (iter.hasNext()) {
	                quotaForCC = (QuotaForCC) iter.next();
	            }
	        }
	        return quotaForCC;
	    }
	 
	 public HtlCutoffDayQuota findHtlCutoffDayQuota( long cutoofDayId) {
	        return (HtlCutoffDayQuota) super.get(HtlCutoffDayQuota.class, cutoofDayId);
	    }
	 
	 public HtlTempQuota qryHtlTempQuotaForcc(long roomid, String bedid) {
	        String hsql = "FROM HtlTempQuota WHERE room_id = ? AND bed_id=?";
	        Object[] obj = new Object[] { roomid, bedid };
	        return (HtlTempQuota) super.query(hsql, obj,0,1,false).get(0);
	    }
	 
	 public void updateHtlTempQuotaForcc(HtlTempQuota htlTempQuota) {
	        super.update(htlTempQuota);
	    }
	 
	 public HtlAssignCustom findHtlAssignCustom(long assignCustomId) {

	        return (HtlAssignCustom) super.get(HtlAssignCustom.class, assignCustomId);
	    }
	 
	 public HtlQuota findHtlQuota(long quotaId) {
	        return (HtlQuota) super.get(HtlQuota.class, quotaId);
	    }
	 
	 public HtlQuotabatch queryHtlQuotabatch(long quotaBatchID) { 
	        return super.get(HtlQuotabatch.class, quotaBatchID);
	 }
	 
	 public long saveOrUpdateQuotabatch(HtlQuotabatch htlQuotabatch) {
		 super.saveOrUpdate(htlQuotabatch);
		 return htlQuotabatch.getID().longValue();
	 }

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

        if (0 < allQtyOLD - allQuota) {
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

	public int deleteQuota(final Long id) {

        HibernateCallback cb = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                long lId = id.longValue();
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

	public int freeAloneQuotaByDays(List lstQuota) {
		return 0;
	}

	public int generateEveryDayRoom(Long qbId) {
        List lstRoom = new ArrayList();
        HtlQuotabatch quotaBatch = (HtlQuotabatch) super.get(HtlQuotabatch.class, qbId);
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

	public void saveOrUpdate(HtlQuota htlQuota) {
		super.saveOrUpdate(htlQuota);
	}
	
	public void batchSaveOrUpdateQuota(List<HtlQuota> listHtlQuota) {
		super.saveOrUpdateAll(listHtlQuota);
	}

	public long update(HtlQuota htlQuota) {
		super.update(htlQuota);
		return 0;
	}

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
    
    @SuppressWarnings("unchecked")
    public List<HtlQuota> getQuotaByBizKeyWithDate(String quotaType, String shareType, long hotelId,
        long contractId, long roomTypeId, Date beginDate, Date endDate, String[] weeks) {
        StringBuilder hql = new StringBuilder();

        hql.append(" from HtlQuota quota where quota.quotaType = ? ");
        hql.append(" and quota.hotelId = ? ");
        hql.append(" and quota.shareType = ? ");
        hql.append(" and quota.contractId = ? ");
        hql.append(" and quota.room.roomTypeId = ? ");
        
        List paramList = new ArrayList();
    	paramList.add(quotaType);
    	paramList.add(Long.valueOf(hotelId));
    	paramList.add(shareType);
    	paramList.add(Long.valueOf(contractId));
    	paramList.add(Long.valueOf(roomTypeId));
    	
    	if(!ValidationUtil.isEmpty(weeks)) {
    		hql.append(" and week in (");
    		for(String weekday : weeks) {
    			hql.append("?, ");
    			paramList.add(weekday);
    		}
    		hql.setLength(hql.length() - 2);
    		hql.append(")");
    	}
    	
    	hql.append(" and quota.ableSaleDate between ? and ? order  by quota.shareType ");
    	paramList.add(beginDate);
    	paramList.add(endDate);
    	
        return super.query(hql.toString(), paramList.toArray());
    }
    
    @SuppressWarnings("unchecked")
    public List<HtlQuota> qryQuotaInWeekdaysByBatchIdSaleDate(long batchQuotaId, Date beginDate, 
    		Date endDate, String[] weeks) {
    	StringBuilder hql = new StringBuilder();
    	hql.append(" from HtlQuota quota where quota.quotaBatchId = ? ");
    	hql.append(" and quota.ableSaleDate between ? and ? ");
    	
    	List paramList = new ArrayList();
    	paramList.add(Long.valueOf(batchQuotaId));
    	paramList.add(beginDate);
    	paramList.add(endDate);
    	
    	if(!ValidationUtil.isEmpty(weeks)) {
    		hql.append(" and week in (");
    		for(String weekday : weeks) {
    			hql.append("?, ");
    			paramList.add(weekday);
    		}
    		hql.setLength(hql.length() - 2);
    		hql.append(")");
    	}
    	
    	hql.append(" order by quota.ableSaleDate, quota.shareType ");
    	
    	return super.query(hql.toString(), paramList.toArray());
    }
    
    public List<HtlQuota> qryQuotaByRoomTypeQuotaType(long hotelId, long roomType, String quotaType,  
    		Date beginDate, Date endDate) {
    	StringBuilder hql = new StringBuilder();
    	hql.append(" from HtlQuota quota where quota.room.roomTypeId = ? ");
    	hql.append(" and quota.quotaType = ? and quota.hotelId = ? "); 
    	hql.append(" and quota.ableSaleDate between ? and ? ");
    	hql.append(" order  by quota.ableSaleDate, quota.shareType ");
    	
    	return super.query(hql.toString(), new Object[]{Long.valueOf(roomType), quotaType, 
    		Long.valueOf(hotelId), beginDate, endDate});
    }
    
    @SuppressWarnings("unchecked")
	public List<HtlQuota> qryQuotaByRoomTypeQuotaTypeWithWeeks(long hotelId, long roomType, String quotaType, 
			Date beginDate, Date endDate, String[] weeks) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from HtlQuota quota where quota.room.roomTypeId = ? ");
        hql.append(" and quota.quotaType = ? and quota.hotelId = ? ");
        
        List paramList = new ArrayList();
    	paramList.add(Long.valueOf(roomType));
    	paramList.add(quotaType);
    	paramList.add(Long.valueOf(hotelId));
    	
        if(!ValidationUtil.isEmpty(weeks)) {
    		hql.append(" and week in (");
    		for(String weekday : weeks) {
    			hql.append("?, ");
    			paramList.add(weekday);
    		}
    		hql.setLength(hql.length() - 2);
    		hql.append(")");
    	}
        
        hql.append(" and quota.ableSaleDate between ? and ? ");
        hql.append(" order by quota.ableSaleDate, quota.shareType ");
        paramList.add(beginDate);
        paramList.add(endDate);
        
        return super.query(hql.toString(), paramList.toArray());
    }
    
    public List<HtlTempQuota> qryTempQuotaByHotelIdRoomId(long hotelId, long roomType, Date beginDate, Date endDate) {
    	StringBuilder hql = new StringBuilder();
    	hql.append(" from  HtlTempQuota  htlTempRoom  where  htlTempRoom.roomId in ");
    	hql.append(" (select htlRoom.ID from HtlRoom htlRoom where htlRoom.roomTypeId = ? ");
    	hql.append("   and htlRoom.hotelId = ?  and htlRoom.ableSaleDate between ? and ? ");
    	hql.append(" ) order by  htlTempRoom.roomId ");
    	
    	return super.query(hql.toString(), new Object[]{Long.valueOf(roomType), Long.valueOf(hotelId), 
    		beginDate, endDate});
    }
    
    
    public List queryContrCuf(long hotelId, long contractId) {
        return super.queryByNamedQuery("queryContrCuf", new Object[] { hotelId, contractId });
    }
}
