package com.mangocity.hotel.order.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mangocity.hotel.base.dao.RoomControlDao;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.order.dao.impl.OrOrderDao;
import com.mangocity.hotel.order.persistence.HtlRoomstateCc;
import com.mangocity.hotel.order.persistence.HtlRoomstateCcBean;
import com.mangocity.hotel.order.persistence.HtlRoomstateCcBed;
import com.mangocity.hotel.order.service.IRoomStratFromCCService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.RoomTypeStatus;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.log.MyLog;

/**
 */
public class RoomStratFromCCService extends DAOHibernateImpl implements IRoomStratFromCCService {
	private static final MyLog log = MyLog.getLogger(RoomStratFromCCService.class);
    private OrOrderDao orOrderDao;

    private RoomControlDao roomControlDao;

    public HtlRoomstateCc findRoomFulLog(Long RoomStateCCID) {
        return orOrderDao.get(HtlRoomstateCc.class, RoomStateCCID);
    }

    public HtlRoom findRoomStatus(Long HotelId, Date ableSaleDate, Long roomTypeId) {
        String hsql = "from HtlRoom htlRoom where hotel_id = ?" +
                " and ableSaleDate = ? and roomTypeId = ?";
        //生产bug1131 CC设置满房的时候，如果某个房型没有房态记录，出现数组越界 modify by chenjiajie 2010-01-12
        List<HtlRoom> htlRoomList = orOrderDao.query(hsql,new Object[] { HotelId, ableSaleDate, roomTypeId });
        HtlRoom htlRoom = null;
        if(null != htlRoomList && !htlRoomList.isEmpty()){
            htlRoom = htlRoomList.get(0);
        }else{
            htlRoom = new HtlRoom();
        }
        return htlRoom;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.mangocity.hotel.base.dao.impl.IRoomControlDao#updateRoomStatu(com.mangocity.hotel.base
     * .manage.assistant.RoomStateBean)
     */
    public int updateRoomStatu(final String roomstate, final Long HotelId, final Date ableSaleDate,
        final Long roomTypeId) {
        HibernateCallback cb = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {

                int k = 0;

                String Hql = "update HtlRoom htlRoom set htlRoom.roomState = ? "
                    + "where htlRoom.hotelId = ? " + "and htlRoom.ableSaleDate = ? "
                    + "and htlRoom.roomTypeId = ?";
                Query query = session.createQuery(Hql);
                query.setString(k++, roomstate);
                query.setLong(k++, HotelId);
                query.setDate(k++, ableSaleDate);
                query.setLong(k++, roomTypeId);
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

    // public int updateRoomStatu(String roomstate,Long HotelId, Date ableSaleDate, Long roomTypeId)
    // {
    // Session session = super.getHibernateTemplate().getSessionFactory().openSession();
    // Transaction tx = null;
    //
    // try{
    // tx = session.beginTransaction();
    // int k = 0;
    // String Hql = "update HtlRoom htlRoom set htlRoom.roomState = ? " +
    // "where htlRoom.hotelId = ? " +
    // "and htlRoom.ableSaleDate = ? " +
    // "and htlRoom.roomTypeId = ?";
    // Query query = session.createQuery(Hql);
    // query.setString(k++, roomstate);
    // query.setLong(k++, HotelId);
    // query.setDate(k++, ableSaleDate);
    // query.setLong(k++, roomTypeId);
    // query.executeUpdate();
    // tx.commit();
    // }
    // catch (Exception he){
    // tx.rollback();
    // }
    // finally{
    // session.close();
    // }
    // return 0;
    // }

    public Long findQuotanum(Long HotelId, Date ableSaleDate, Long room_id) {

        String[] str = DateUtil.dateToString(ableSaleDate).split("-");
        String SaleDate = "";
        for (int i = 0; i < str.length; i++) {
            SaleDate += str[i].trim();
        }
        String sql = "select sum(q.avail_qty) Quotanum ,q.able_sale_date,r.room_type_id from"
            + " htl_quota q ,htl_room r where q.room_id = r.room_id"
            + " and r.able_sale_date = to_date(?, 'yymmdd') and q.hotel_id = ? "
            + " and r.room_id = ? group by r.room_type_id , q.able_sale_date"
            + " order by sum(q.avail_qty),r.room_type_id";
        List<?> resultList = orOrderDao.queryByNativeSQL(sql, new Object[]{SaleDate, HotelId, room_id});
        
        Long quotaNum = null;
        if(!resultList.isEmpty()) {
        	quotaNum = Long.valueOf(((Object[])resultList.get(0))[0].toString());
        }
        
        return quotaNum;
    }

    /**
     * 记录房态操作历史记录
     */
    public Long logRoomStatusProcess(HtlRoomstateCc hrsc) {
        super.saveOrUpdate(hrsc);
        return hrsc.getID();
    }

    public int saveRoomState(HtlRoomstateCcBean htlRoomstateCcBean) throws SQLException {
        HtlRoomstateCc hrsc = new HtlRoomstateCc();
        hrsc.setBegindate(htlRoomstateCcBean.getBegindate());
        hrsc.setEnddate(htlRoomstateCcBean.getEnddate());
        hrsc.setHotelid(htlRoomstateCcBean.getHotelid());

        Calendar calStart = Calendar.getInstance();
        calStart.setTime(hrsc.getBegindate());
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(hrsc.getEnddate());
        Calendar calTmp = (Calendar) calStart.clone();
        List<HtlRoomstateCcBed> listhtlRoomstateCcBed = new ArrayList<HtlRoomstateCcBed>();
        HtlRoomstateCcBed htlRoomstateCcBed = new HtlRoomstateCcBed();
        // 循环页面上的每一天
        while (!calEnd.equals(calTmp)) {
            Iterator it = htlRoomstateCcBean.getRoomTypeStatus().iterator();
            Long tmproomtypeid = 0L;
            Long curroomtypeid = 0L; // 初始化房型ID为0，如果为0则表示开始进入
            String roomStateStr = "";
            Long roomId = 0L;
            HtlRoom htlRoom = null;
            // /每天一个房型加床型组成一个RoomTypeStatus对象
            while (it.hasNext()) {
                RoomTypeStatus roomTypeStatus = (RoomTypeStatus) it.next();
                Long quotaQty = 0L;
                curroomtypeid = Long.valueOf(roomTypeStatus.getRoomTypeID());
                // 当相等的时候表示还在处理同一个房型。
                if (tmproomtypeid.longValue() == curroomtypeid.longValue()) {

                    String roomBedIdStr = roomTypeStatus.getRoomBedId();
                    String bedstatus = bedStateGet(roomStateStr, roomBedIdStr);
                    roomStateStr = roomSateSet(roomStateStr, roomBedIdStr);
                    if (!StringUtil.isValidStr(roomStateStr)) {
                        roomStateStr = roomBedIdStr + ":4";
                    }
                    htlRoomstateCcBed.setBedidstr(roomBedIdStr);
                    htlRoomstateCcBed.setQuotanum(findQuotanum(hrsc.getHotelid(), calTmp.getTime(),
                        roomId));
                    htlRoomstateCcBed.setRoomdate(calTmp.getTime());

                    String strbedstatus = null;
                    if (bedstatus.equals("0")) {
                        strbedstatus = "freesale";
                    } else if (bedstatus.equals("1")) {
                        strbedstatus = "良好";
                    } else if (bedstatus.equals("2")) {
                        strbedstatus = "紧张";
                    } else if (bedstatus.equals("3")) {
                        strbedstatus = "不可超";
                    } else if (bedstatus.equals("4")) {
                        strbedstatus = "满房";
                    }
                    htlRoomstateCcBed.setRoomstate(strbedstatus);
                    htlRoomstateCcBed.setRoomtypeid(Long.parseLong(roomTypeStatus.getRoomTypeID()));

                    htlRoomstateCcBed.setRoomtypenm(roomTypeStatus.getRoomName());
                    listhtlRoomstateCcBed.add(htlRoomstateCcBed);
                    htlRoomstateCcBed = new HtlRoomstateCcBed();
                } else {
                    // 表示不同的房间，需要更新房态。
                    if (0 != tmproomtypeid.longValue()) {
                        updateRoomStatu(roomStateStr, hrsc.getHotelid(), calTmp.getTime(),
                            tmproomtypeid);

                    }
                    htlRoom = findRoomStatus(hrsc.getHotelid(), calTmp.getTime(), Long
                        .parseLong(roomTypeStatus.getRoomTypeID()));
                    roomId = htlRoom.getID();
                    if(null == roomId || roomId == 0) break;  // 判断为空的情况
                    roomStateStr = htlRoom.getRoomState();// /数据库里的房态字串 "1:2/2:"
                    String roomBedIdStr = roomTypeStatus.getRoomBedId();
                    // bedStateGet一定要在roomSateSet之前调用。
                    String bedstatus = bedStateGet(roomStateStr, roomBedIdStr);
                    roomStateStr = roomSateSet(roomStateStr, roomBedIdStr);
                    if (!StringUtil.isValidStr(roomStateStr)) {
                        roomStateStr = roomBedIdStr + ":4";
                    }
                    htlRoomstateCcBed.setBedidstr(roomTypeStatus.getRoomBedId());
                    log.info("----------------roomId=" + roomId);
                    quotaQty = findQuotanum(hrsc.getHotelid(), calTmp.getTime(), roomId);
                    htlRoomstateCcBed.setQuotanum(quotaQty);
                    htlRoomstateCcBed.setRoomdate(calTmp.getTime());

                    String strbedstatus = null;
                    if (bedstatus.equals("0")) {
                        strbedstatus = "freesale";
                    } else if (bedstatus.equals("1")) {
                        strbedstatus = "良好";
                    } else if (bedstatus.equals("2")) {
                        strbedstatus = "紧张";
                    } else if (bedstatus.equals("3")) {
                        strbedstatus = "不可超";
                    } else if (bedstatus.equals("4")) {
                        strbedstatus = "满房";
                    }
                    htlRoomstateCcBed.setRoomstate(strbedstatus);
                    htlRoomstateCcBed.setRoomtypeid(Long.parseLong(roomTypeStatus.getRoomTypeID()));
                    htlRoomstateCcBed.setRoomtypenm(roomTypeStatus.getRoomName());
                    listhtlRoomstateCcBed.add(htlRoomstateCcBed);
                    htlRoomstateCcBed = new HtlRoomstateCcBed();
                    tmproomtypeid = curroomtypeid;

                }

            }
            log.info("--------");
            log.info("--------" + roomStateStr);
            log.info("--------" + hrsc.getHotelid());
            log.info("--------" + calTmp.getTime());
            log.info("--------" + tmproomtypeid);
            updateRoomStatu(roomStateStr, hrsc.getHotelid(), calTmp.getTime(), tmproomtypeid);

            calTmp.add(Calendar.DATE, 1);
        }
        hrsc.setHtlRoomstateCcBed(listhtlRoomstateCcBed);
        log.info("----------listhtlRoomstateCcBed.size()" + listhtlRoomstateCcBed.size());
        hrsc.setInform(htlRoomstateCcBean.getInform());
        hrsc.setOperatedept("CC");
        hrsc.setProcessby(htlRoomstateCcBean.getProcessby());
        hrsc.setProcessbyid(htlRoomstateCcBean.getProcessbyid());
        hrsc.setProcessdatetime(new Date());
        logRoomStatusProcess(hrsc);

        return 0;
    }

    private String roomSateSet(String strRoomstate, String bed) {
        String temp = "";
        if (StringUtil.isValidStr(strRoomstate)) {
            String[] str = strRoomstate.split("/");
            for (int i = 0; i < str.length; i++) {
                String[] testStr2 = str[i].split(":");
                if (testStr2[0].equals(bed)) {
                    testStr2[1] = "4";
                    temp += bed + ":" + testStr2[1] + "/";
                } else {
                    temp += testStr2[0] + ":" + testStr2[1] + "/";
                }

            }
        }
        if (StringUtil.isValidStr(temp)) {
            if (temp.endsWith("/")) {
                temp = temp.substring(0, temp.length() - 1);
            }
        }
        return temp;
    }

    private String bedStateGet(String strRoomstate, String bed) {
        String temp = "1";
        if (StringUtil.isValidStr(strRoomstate)) {
            String[] str = strRoomstate.split("/");
            for (int i = 0; i < str.length; i++) {
                String[] testStr2 = str[i].split(":");
                if (testStr2[0].equals(bed)) {
                    temp = testStr2[1];
                    break;
                }
            }
        }
        return temp;
    }

    public void updateRoomFulLog(HtlRoomstateCc htlRoomstateCc) {
        Session session = orOrderDao.getCurrentSession();
        new DateUtil();
        String hqlupdate = "update  HtlRoomstateCc htlRoomstateCc set "
            + " htlRoomstateCc.reviewid = '" + htlRoomstateCc.getReviewid() + "'"
            + ",htlRoomstateCc.reviewname = '" + htlRoomstateCc.getReviewname() + "'"
            + ",htlRoomstateCc.reviewremark = '" + htlRoomstateCc.getReviewremark() + "'"
            + ",htlRoomstateCc.reviewstate = '1'" + ",htlRoomstateCc.reviewdept = '本部'"
            + ",htlRoomstateCc.processdate = sysdate" +
            // ",htlRoomstateCc.processDate = " + dateUtil.datetimeToString(new Date()) +
            " where htlRoomstateCc.roomstateccid = " + htlRoomstateCc.getRoomstateccid();

        session.createQuery(hqlupdate).executeUpdate();

    }

    public OrOrderDao getOrOrderDao() {
        return orOrderDao;
    }

    public void setOrOrderDao(OrOrderDao orOrderDao) {
        this.orOrderDao = orOrderDao;
    }

    public RoomControlDao getRoomControlDao() {
        return roomControlDao;
    }

    public void setRoomControlDao(RoomControlDao roomControlDao) {
        this.roomControlDao = roomControlDao;
    }
}
