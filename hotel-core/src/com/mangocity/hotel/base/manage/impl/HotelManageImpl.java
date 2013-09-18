package com.mangocity.hotel.base.manage.impl;


import java.sql.CallableStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mangocity.hdl.dao.IExDao;
import com.mangocity.hotel.base.dao.HotelPictureDao;
import com.mangocity.hotel.base.dao.IQuotaForCCDao;
import com.mangocity.hotel.base.dao.ISaleDao;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.IncreaseBean;
import com.mangocity.hotel.base.manage.assistant.CloseRoomReasonBean;
import com.mangocity.hotel.base.manage.assistant.HotelAddressInfo;
import com.mangocity.hotel.base.manage.assistant.HotelComparator;
import com.mangocity.hotel.base.manage.assistant.RoomAndBookItemBean;
import com.mangocity.hotel.base.manage.assistant.RoomComparator;
import com.mangocity.hotel.base.manage.assistant.RoomStateDisplayBean;
import com.mangocity.hotel.base.persistence.ExMapping;
import com.mangocity.hotel.base.persistence.HtlCoverPicture;
import com.mangocity.hotel.base.persistence.HtlChannelMapInfo;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlCreditAssure;
import com.mangocity.hotel.base.persistence.HtlCtct;
import com.mangocity.hotel.base.persistence.HtlCtlDsply;
import com.mangocity.hotel.base.persistence.HtlEbooking;
import com.mangocity.hotel.base.persistence.HtlEbookingFunctionMaster;
import com.mangocity.hotel.base.persistence.HtlEbookingPersonFunction;
import com.mangocity.hotel.base.persistence.HtlEbookingPersonnelInfo;
import com.mangocity.hotel.base.persistence.HtlExchange;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlHotelBase;
import com.mangocity.hotel.base.persistence.HtlHotelExt;
import com.mangocity.hotel.base.persistence.HtlHotelScore;
import com.mangocity.hotel.base.persistence.HtlIncreasePrice;
import com.mangocity.hotel.base.persistence.HtlMainCommend;
import com.mangocity.hotel.base.persistence.HtlPicture;
import com.mangocity.hotel.base.persistence.HtlPictureInfo;
import com.mangocity.hotel.base.persistence.HtlPictureUrl;
import com.mangocity.hotel.base.persistence.HtlPreconcertItem;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlQuotaCutoffDayNew;
import com.mangocity.hotel.base.persistence.HtlQuotaNew;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.base.persistence.HtlRoomBase;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.HtlTempQuota;
import com.mangocity.hotel.base.persistence.HtlUsersComment;
import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.persistence.SellSeason;
import com.mangocity.hotel.base.service.assistant.HotelQueryCondition;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.util.DateUtil;
import com.mangocity.util.ReflectObject;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.HotelBaseConstantBean;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.hotel.constant.BaseConstant;
import com.mangocity.util.hotel.constant.HotelMappingType;
import com.mangocity.util.log.MyLog;

/**
 * 
 * @author xiaowumi 酒店本部管理酒店的接口
 */

public class HotelManageImpl extends DAOHibernateImpl implements HotelManage {
    // add by shizhongwen 2009-04-20 增加日志信息
	private static final MyLog log = MyLog.getLogger(HotelManageImpl.class);

    private ISaleDao saleDao;

    private IQuotaForCCDao quotaForCCDao;

    private HotelPictureDao hotelPictureDao;
    
    private IExDao exHdlDao;

    /**
     * 直辖市的城市代码(北京，上海，天津，重庆)
     */
    private static final String[] DIRECTLYCITYARR = new String[] { "PEK", "CKG", "SHA", "TSN" };

	private static final String HOTEL_ACTIVE = "1";
	

    /**
     * 新增一个酒店
     * 
     * @param hotel
     *            酒店对象
     * @return hotel_id;
     */
    public Long createHotel(HtlHotel hotel) {
        // String str = "seq_hotel_hotel";

        // hotel.setID(super.getSequenceNextVal(str));
        super.save(hotel);
        return hotel.getID();
    }

    /**
     * 查询酒店
     * 
     * @param htlQryCond
     *            酒店查询条件对象
     * @return Hotel 酒店实体列表
     */
    public List queryHotel(HotelQueryCondition htlQryCond) {
        // TODO List
        return null;
    }

    /**
     * 重新找回酒店信息
     * 
     * @param hotel_id
     *            酒店id;
     * @return 酒店一个实体
     */
    public HtlHotel findHotel(long hotel_id) {        
        return (HtlHotel) super.find(HtlHotel.class, hotel_id);
    }

    public List findHtlEbooking(long hotel_id) {
        String sql = "from HtlEbooking where htlHotel.ID = ?";
        List htlEbooking = super.doquery(sql, hotel_id, false);
        return htlEbooking;
    }

    /**
     * 修改酒店对象
     * 
     * @param htl
     *            酒店实体对象
     * @return 修改记录的结果，1=表示修改成功，0=表示修改失败
     */
    public int modifyHotel(HtlHotel htl) {
        super.update(htl);
        return 0;
    }

    /**
     * 新建一个房型
     * 
     * @param roomType
     *            房型实体对象
     * @return room_type_id; 房型实体的id;
     */
    public Long createRoomType(HtlRoomtype roomType) {
        super.saveOrUpdate(roomType);
        return roomType.getID();
    }

    /**
     * 修改房型
     * 
     * @param roomType
     *            房型实体对象
     * @return 修改记录的结果，1=表示修改成功，0=表示修改失败
     */
    public int modifyRoomType(HtlRoomtype roomType) {
        super.merge(roomType);
        return 0;
    }

    /**
     * 删除房型
     * 
     * @param roomTypeId
     *            房型的实体id;
     * @return 删除记录的结果，1=表示删除成功，0=表示删除失败
     */
    public int deleteRoomType(long roomTypeId) {
        // super.remove(HtlRoomtype.class,roomTypeId);
        HtlRoomtype hr = (HtlRoomtype) super.find(HtlRoomtype.class, roomTypeId);
        hr.setHotelID(null);
        super.update(hr);
        return 0;
    }

    /**
     * 删除房型
     * 
     * @param roomTypeId
     *            房型的实体id;
     */
    public void deleteAllRoomType(long roomTypeId) {
        Date ableSaleDate = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        Date date = DateUtil.getDate(df.format(new Date(ableSaleDate.getTime())));

        String hsql = "delete HtlAssignCustom a " + "where quota_id in "
            + "(select q.ID from HtlQuota q where room_id in " + "(select m.ID from HtlRoom m "
            + "where room_type_id = ? " + "and m.ableSaleDate >= ?))";
        /**
        String hsql1 = "delete HtlCutoffDayQuota a " + "where quota_id in "
            + "(select q.ID from HtlQuota q where room_id in " + "(select m.ID from HtlRoom m "
            + "where room_type_id = ? " + "and m.ableSaleDate >= ?))";
        String hsql2 = "delete HtlQuota q where room_id in " + "(select m.ID from HtlRoom m "
            + "where room_type_id = ? " + "and m.ableSaleDate >= ?))";
         **/
        String hsql1 = "delete HtlQuotaCutoffDayNew a "
        			 + "where a.roomtypeId = ? " + "and a.ableDate >= ?))";
        String hsql2 = "delete HtlQuotaNew q "
        			 + "where q.roomtypeId = ? " + "and q.ableSaleDate >= ?))";
        String hsql3 = "delete HtlPrice pr where room_id in " + "(select m.ID from HtlRoom m "
            + "where room_type_id = ? " + "and m.ableSaleDate >= ?))";
        String hsql4 = "delete HtlTempQuota tq where room_id in " + "(select m.ID from HtlRoom m "
            + "where room_type_id = ? " + "and m.ableSaleDate >= ?))";
        String hsql6 = "delete HtlRoom m " + "where room_type_id = ? " + "and m.ableSaleDate >= ? ";
        Object[] obj = new Object[] { roomTypeId, date };
        super.remove(hsql, obj);
        super.remove(hsql1, obj);
        super.remove(hsql2, obj);
        super.remove(hsql3, obj);
        super.remove(hsql4, obj);
        super.remove(hsql6, obj);
        String hsql5 = "delete HtlRoomtype " + "where room_type_id = ? ";

        String hsql7 = "delete HtlRoomtype m " + "where room_type_id = ? ";
        Object[] obj1 = new Object[] { roomTypeId };
        super.remove(hsql5, obj1);
        super.remove(hsql7, obj1);
    }

    /**
     * 删除价格类型所对应的价格
     * 
     * @param childRoomTypeId
     *            价格类型id
     * 
     */

    public void delHtlPrice(long childRoomTypeId) {
        Date ableSaleDate = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        Date date = DateUtil.getDate(df.format(new Date(ableSaleDate.getTime())));
        String hsql = "delete HtlPrice hp " + "where hp.childRoomTypeId = ? "
            + "and hp.ableSaleDate >= ? ";
        String hsql1 = "delete HtlPriceType h " + "where h.ID = ? ";
        Object[] obj = new Object[] { childRoomTypeId, date };
        Object[] obj1 = new Object[] { childRoomTypeId };
        super.remove(hsql, obj);
        super.remove(hsql1, obj1);
    }

    /**
     * 禁用房型
     * 
     * @param roomTypeId
     *            房型的实体id;
     * @return 禁用房型记录的结果，1=表示禁用房型成功，0=表示禁用房型失败
     */
    public int disableRoomType(long roomTypeId) {
        // TODO List
        return 0;
    }

    /**
     * 启用一个房型
     * 
     * @param roomTypeId房型的实体id
     *            ;
     * @return 启用房型记录的结果，1=表示启用房型成功，0=表示启用房型失败
     */
    public int enableRoomType(long roomTypeId) {
        // TODO List
        return 0;
    }

    /**
     * 禁用一个酒店
     * 
     * @param hotelId
     *            酒店实体id
     * @return 禁用酒店结果 1=表示禁用酒店成功，0=表示禁用酒店失败
     */
    public int disableHotel(long hotelId) {
        // TODO List
        return 0;
    }

    /**
     * 启用一个酒店
     * 
     * @param hotelId
     *            酒店实体id
     * @return 启用酒店结果 1=表示启用酒店成功，0=表示启用酒店失败
     */
    public int enableHotel(long hotelId) {
        // TODO List
        return 0;
    }

    /**
     * 列出一个酒店的所有房型(去除香港的房型) add by shizhongwen 时间:Apr 22, 2009 4:18:11 PM
     * 
     * @param hotelId
     * @return
     */
    public List lstRoomTypeByHotelIdRemoveHK(long hotelId) {
        String hsql = "from HtlRoomtype where ishkroomtype='0' and hotel_id =?";
        List lsRoomType = new ArrayList();
        lsRoomType = super.query(hsql, hotelId);
        return lsRoomType;
    }

    public int delHotelInfo(long hotelId) {
        HtlHotel htlHotel = (HtlHotel) super.find(HtlHotel.class, hotelId);
        htlHotel.setActive("0");
        super.update(htlHotel);
        // 删除合作方的映射信息
        String hsql = "";
        hsql += " delete ExMapping e ";
        hsql += " where hotelid = ? ";
        Object[] obj = new Object[1];
        obj[0] = Long.valueOf(hotelId);
        super.remove(hsql, obj);
        return 0;
    }

    public List qryHtlCtct(long hotelId) {
        String hsql = "from HtlCtct where hotel_id = ?";
        List htCt = new ArrayList();
        htCt = super.query(hsql, hotelId);
        return htCt;
    }

    /**
     * 根据酒店ID查询相应的酒店销售季节信息
     * 
     * @param hotelId
     * @return
     */
    public List qryHtlSellSeason(long hotelId) {
        String hsql = "from SellSeason ss where hotel_id = ? and "
            + "sell_begindate is not null and sell_enddate is not null";
        List sellSeasonList = new ArrayList();
        sellSeasonList = super.query(hsql, hotelId);
        return sellSeasonList;
    }

    public long saveOrUpdateHotel(HtlHotel htlHotel) {
        super.saveOrUpdate(htlHotel);
        return 0;
    }

    public long saveOrUpfateHtlCtct(HtlHotel htlHotel) {
        super.saveOrUpdateHtlCtct(htlHotel);
        return 0;
    }

    public long merge(HtlHotel htlHotel) {
        super.merge(htlHotel);
        return 0;
    }

    /**
     * 保存分数
     * 
     * @param
     * @return
     */
    public long saveOrUpdateScore(HtlHotelScore htlScore) {
        super.saveOrUpdate(htlScore);
        return 0;
    }

    public List qryRoom(long hotelId, Date beginDate, Date endDate, String[] week,
        String[] roomTypes) {
        if (null == roomTypes || 1 > roomTypes.length) {
            return new ArrayList<String>();
        }
        
        List<HtlRoomtype> lstRoomTypes = this.qryRoomTypeBedType(hotelId, roomTypes);
        HashMap<String,HtlRoomtype> mapRoomTypes = new HashMap<String,HtlRoomtype>(lstRoomTypes.size());
        for (HtlRoomtype roomtype:lstRoomTypes) {
            mapRoomTypes.put("" + roomtype.getID(), roomtype);
        }
        //新房控bug修改：查询酒店所有得价格类型信息，用来刷选价格用 add by zhijie.gu 2010-02-26
        List<HtlPriceType> lstPriceTypes = this.qryPriceType(hotelId);
       
        // 这里查询来的记录是按日期排序的，
        List<HtlRoom> lstRooms = saleDao.qryRoomState(hotelId, beginDate, endDate, week, roomTypes);
        HashMap<String,RoomStateDisplayBean> map = new HashMap<String,RoomStateDisplayBean>(lstRooms.size());

        for (int i = 0; i < lstRooms.size(); i++) {
            HtlRoom room = (HtlRoom) lstRooms.get(i);
            if (null != room) {
                RoomStateDisplayBean rsdb;//显示按天调整房态记录
                rsdb = (RoomStateDisplayBean) map.get(DateUtil.formatDateToSQLString(room
                    .getAbleSaleDate()));
                if (null == rsdb) {
                    rsdb = new RoomStateDisplayBean();
                    rsdb.setWeek(room.getWeek());
                    rsdb.setSaleRoomDate(room.getAbleSaleDate());
                }
                setDisplayRoomState(room, rsdb);
                map.put(DateUtil.formatDateToSQLString(room.getAbleSaleDate()), rsdb);
            }
        }

        List lstDisplayRoomStates = new ArrayList();
        // 循环日期
        Date bizDate = beginDate;
        for (int j = 0; j <= DateUtil.getDay(beginDate, endDate); j++) {
            RoomStateDisplayBean rsd = (RoomStateDisplayBean) map.get(DateUtil
                .formatDateToSQLString(bizDate));
            if (null != rsd) {
                for (String roomTypeId:roomTypes) {
                    RoomAndBookItemBean rabib = (RoomAndBookItemBean) rsd.getMapRoomAndBook().get(
                        roomTypeId);
                    if (null == rabib) {
                        rabib = new RoomAndBookItemBean();
                    }
                    rabib.setRoomTypeId(new Long(roomTypeId));
                    // 当房型的所有价格类型都为关房时，把标志位置为true. add by zhineng.zhuang 2008-09-27
                    HtlRoom htlRoom = new HtlRoom();
                    htlRoom = rabib.getRoom();
                    String isAllClose = "0";
                    if (null != htlRoom) {
                        List<HtlPrice> lstPrice = new ArrayList<HtlPrice>(htlRoom.getLstPrices().size());
                        lstPrice = htlRoom.getLstPrices();
                        //对价格进行降序排序
                        this.sortLstPrice(lstPrice);
                        HtlPrice htlPrice = new HtlPrice();
                        int closeNum = 0;
                        boolean tf = true;
                        boolean tf1 = true;
                        for (int i = 0; i < lstPrice.size(); i++) {
                            htlPrice = (HtlPrice) lstPrice.get(i);
                            // 关房原因为停止合作8/策略性关房CC不可订6/停业2/装修4/价格为0时
                            //modify by zhijie.gu 2010-01-13 不管关房原因是哪个，都允许修改房态，只有价格为0是不允许
                            if (null != htlPrice) {
                                String reason = htlPrice.getReason();
                                if (0 == Double.compare(0.0,htlPrice.getSalePrice())){
                                    closeNum++;
                                }
                            }
                            
                            //新房控bug修改，价格取的优先级“标准“价格类型面付，没有”标准“，取价格>1里面最低价格 add by zhijie.gu 2010-02-26 begin
                            for (HtlPriceType priceType:lstPriceTypes){
                            	if(priceType.getID().equals(htlPrice.getChildRoomTypeId())){
                            		if("标准".equals(priceType.getPriceType()) && "pay".equals(htlPrice.getPayMethod())){
                            			//如果售卖日期相同则获取该售卖日期的售卖价格
                                        if(DateUtil.dateToString(rsd.getSaleRoomDate()).equals(DateUtil.dateToString(htlPrice.getAbleSaleDate())) && htlPrice.getSalePrice() >1){
                                        	rabib.setSalePrice(htlPrice.getSalePrice());
                                        	tf = false;
                                        }
                            			
                            		}else if (tf && !"标准".equals(priceType.getPriceType()) && "pay".equals(htlPrice.getPayMethod())){
                            			//如果售卖日期相同则获取该售卖日期的售卖价格
                                        if(DateUtil.dateToString(rsd.getSaleRoomDate()).equals(DateUtil.dateToString(htlPrice.getAbleSaleDate())) && htlPrice.getSalePrice() >1){
                                        	rabib.setSalePrice(htlPrice.getSalePrice());
                                        	tf1 = false;
                                        }
                            			
                            		}else if(tf && tf1){//只有预付的时候在这里赋值
                            			//如果售卖日期相同则获取该售卖日期的售卖价格
                                        if(DateUtil.dateToString(rsd.getSaleRoomDate()).equals(DateUtil.dateToString(htlPrice.getAbleSaleDate())) && htlPrice.getSalePrice() >1){
                                        	rabib.setSalePrice(htlPrice.getSalePrice());
                                        }
                            		}
                            		break;
                            		
                            	}
                            	
                            }
                          //新房控bug修改，价格取的优先级“标准“价格类型面付，没有”标准“，取价格>1里面最低价格 add by zhijie.gu 2010-02-26 end
                            
                        }
                        // modify by zhijie.gu 只有没录价格时
                        if (lstPrice.size() == closeNum) {
                            isAllClose = "1";
                        }
                    }
                    rabib.setIsAllClose(isAllClose);
                    
                    
                    

                    // 为房态查询结果加关房原因，精确到子房型。 modify by zhineng.zhuang
                    List queryCloseRoom = new ArrayList();
                    queryCloseRoom = hotelPictureDao.queryCloseRoom(roomTypeId, beginDate, endDate);
                    List<CloseRoomReasonBean> crrbList = new ArrayList<CloseRoomReasonBean>();
                    for (int i = 0; i < queryCloseRoom.size(); i++) {
                        CloseRoomReasonBean closeRoomReasonBean = new CloseRoomReasonBean();
                        closeRoomReasonBean = (CloseRoomReasonBean) queryCloseRoom.get(i);
                        if (0 == DateUtil.compare(closeRoomReasonBean.getSaleDate(), bizDate)) {
                            crrbList.add(closeRoomReasonBean);
                        }
                    }
                    rabib.setCloseRoomReasonBean(crrbList);

                    HtlRoomtype roomtype = (HtlRoomtype) mapRoomTypes.get(roomTypeId);
                    if (null != roomtype) {
                        rabib.setBedType(roomtype.getBedType());
                        if (null == rabib.getTempQuota()) {
                            List lstTempQuota = new ArrayList();
                            rabib.setTempQuota(lstTempQuota);
                        }
                        if (StringUtil.isValidStr(roomtype.getBedType())) {
                            String[] beds = roomtype.getBedType().split(",");

                            for (String bed : beds) {
                                boolean found = false;
                                for (int iTemp = 0; iTemp < rabib.getTempQuota().size(); iTemp++) {
                                    HtlTempQuota tq = (HtlTempQuota) rabib.getTempQuota()
                                        .get(iTemp);
                                    if (tq.getBedId().equals(bed)) {
                                        tq.setBedStatus(getBedState(rabib, bed, tq));
                                        // //这里会重新去新表中读取临时配额,以及预警数，预警状态，可用配额总数
                                        setTempQuotaNew(htlRoom.getHotelId(),htlRoom.getRoomTypeId(),htlRoom.getAbleSaleDate(),rabib,bed);
                                        found = true;
                                        break;
                                    }
                                }
                                //此时临时配额表没有记录。临时配额数，可用配额总数，配额预警数都得从新表取数据,add by shengwei.zuo 
                                if (!found) {
                                	
                                	HtlTempQuota tq1 = new HtlTempQuota();
                                	
                                	List nulTempList = setNullTempQuotaForQuotaNew(hotelId,Long.parseLong(roomTypeId),bizDate,bed);
                                    if(!nulTempList.isEmpty()){
                                    	
                                    	tq1 = (HtlTempQuota)nulTempList.get(0);
                                    	
                                    }else{
                                    	
                                    	tq1.setQuotaQty(0);
                                    }
                                   
                                    tq1.setBedId(bed);
                                    String bedState = getBedState(rabib, bed, tq1);
                                    tq1.setBedStatus(bedState);
                                    tq1.setTempQuotaMode("A");
                                    rabib.getTempQuota().add(tq1);
                                } 
                               
                                
                            }  
                        }  
                    }
                    rsd.getLstRoomAndBookItems().add(rabib);
                }
                lstDisplayRoomStates.add(rsd);
            }
            bizDate = DateUtil.getDate(bizDate, 1);
        }
        return lstDisplayRoomStates;
    }

    private String getBedState(RoomAndBookItemBean rabib, String bed, HtlTempQuota tq1) {
        String bedState = "";
        if (null != rabib.getRoom()) {
            tq1.setRoomId(rabib.getRoom().getID());
            // 因为批次更新房态的时候没有写临时配额表，所以，要从房间表中拿到房态，更新临时配额的床型的房态。
            String bState = rabib.getRoom().getRoomState();// 1:n/:u
            if (null != bState) {
                // 先拿床型
                int bt = bState.indexOf(bed + ":");
                if (-1 != bt) {
                    bedState = bState.substring(bt + 2, bt + 3);
                    if (bedState.equalsIgnoreCase("n") || bedState.equalsIgnoreCase("u")
                        || bedState.equalsIgnoreCase("l")) {
                        bedState = null;
                    }
                }
            }
        }
        return bedState;
    }
    
    /**
     * 临时配额查询时,从配额新表中去查询记录,填充到老表去
     * add by haibo.li 配额改造
     * @param hotelId
     * @param roomTypeId
     * @param saleDate
     * @param raid
     */
    private void setTempQuotaNew(long hotelId,long roomTypeId,Date saleDate ,RoomAndBookItemBean raid,String bedId){
    	try{
    		//房控优化，增加读取普通配额的数据。
    		List quotaList = setDisplayTempQuota(hotelId,roomTypeId,saleDate,HotelBaseConstantBean.TEMPQUOTA,bedId);
        	
    		//拿出普通配额的cut组装成字符串
    		String commQuotaCot = composeStr(quotaList);
    		if(commQuotaCot != "")
    			log.info("=======commQuotaCot"+commQuotaCot);
    		/** 房控合约配额显示BUG修复 add by xiaowei.wang begin*/
    		//房控优化，拆分普通配额的数据。
    		//splitQuotaList(quotaList);
    		removeTheSameObject(quotaList);
    		/** 房控合约配额显示BUG修复 add by xiaowei.wang end*/
    		List lstQuota = raid.getTempQuota();
        	
        	//判断是否有记录
        	if(quotaList!=null&&!quotaList.isEmpty()){
        		
        		List<HtlQuotaCutoffDayNew>  lstQuoCuffDayNew = new  ArrayList<HtlQuotaCutoffDayNew>();
        		
        		//总的可用配额总数,如果共享方式有多种，就进行叠加
        		Integer canUserQuotaSum = 0 ;
        		Long quotaSum=0L;
        	    //房态中，显示配额预警状态，预警数，可用数 hotel2.9.3 add by shengwei.zuo 2009-10-16  begin
        	 	for (Iterator iterator = quotaList.iterator(); iterator.hasNext();) {
        	 		
        	 		List myQutaAndNewList = (List) iterator.next();
        	 		
        	 		HtlQuotaCutoffDayNew quotaNew =(HtlQuotaCutoffDayNew) myQutaAndNewList.get(0);
        	 		
        	 		//配额预警数
        	 		quotaNew.setForewarnQuotaNum((Long)myQutaAndNewList.get(1));
        	 		
        	 		//预警状态
        	 		quotaNew.setForewarnFlag((Long)myQutaAndNewList.get(2));
        	 		
        	 		//可用配额总数
        	 		quotaNew.setCanUserQuotaNum(((Long)myQutaAndNewList.get(3)).intValue());
        	 		//临时配额可用数
        	 		quotaNew.setCasualQuotaAbleNum((Long)myQutaAndNewList.get(4));
        	 		quotaNew.setQuotaPattern((String)myQutaAndNewList.get(5));
        	 		quotaNew.setBuyQuotaAbleNum((Long)myQutaAndNewList.get(6));
        	 		//临时配额已用数
        	 		quotaNew.setCasualQuotaUsedNum((Long)myQutaAndNewList.get(7));
        	 		//合约配额已用数
        	 		quotaNew.setQuotaUsedNumPara((Long)myQutaAndNewList.get(8));
        	 	
        	 		//总的可用配额总数,如果共享方式有多种，就进行叠加
        	 		canUserQuotaSum+=quotaNew.getCanUserQuotaNum();
        	 		quotaSum+=quotaNew.getBuyQuotaAbleNum();
        	 		lstQuoCuffDayNew.add(quotaNew);
        	 		
        	 	}
        	 	
        	 	//辅助的配额明细类  add by shengwei.zuo 2009-11-09
        	 	HtlQuotaCutoffDayNew   quotaCutoffDayNewEnty =  new HtlQuotaCutoffDayNew();
        	 	
        	 	if(lstQuoCuffDayNew!=null&&!lstQuoCuffDayNew.isEmpty()){
        	 		
        	 		quotaCutoffDayNewEnty = lstQuoCuffDayNew.get(0);
        	 		
        	 		for(int j=0;j<lstQuota.size();j++){
        				HtlTempQuota htq =(HtlTempQuota) lstQuota.get(j);
        				//直接把临时配额和cutofftime赋值到
            			if(htq.getBedId().equals(bedId)){
            				
            				//配额数
            				htq.setQuotaQty(quotaCutoffDayNewEnty.getQuotaNum().intValue());
            				
            				//cutofftime
                			htq.setCutofftime(quotaCutoffDayNewEnty.getCutofftime());
                			
                			//cutoffday add by shaojun.yang 2010-01-07
                			htq.setCutoffday(quotaCutoffDayNewEnty.getCutoffday());
                			//临时配额可用数 add by shaojun.yang 2010-01-07
                			htq.setAbleQuotaQty(null==quotaCutoffDayNewEnty.getCasualQuotaAbleNum()?0:quotaCutoffDayNewEnty.getCasualQuotaAbleNum().intValue());
                			
                			//对应的配额明细ID
                			htq.setQuotaCutoffDayNewId(quotaCutoffDayNewEnty.getID());
                			
                			//房态中，显示配额预警状态，预警数，可用数 hotel2.9.3 add by shengwei.zuo 2009-10-16  begin
                			
                			//预警数
                			htq.setForewarnQuotaNumRoSta(quotaCutoffDayNewEnty.getForewarnQuotaNum());
                			
                			//预警标识
                			htq.setForewarnFlagRoSta(quotaCutoffDayNewEnty.getForewarnFlag());
                			
                			//总的可用配额总数,如果共享方式有多种，就进行叠加
                			htq.setQuotaAvailableSumRoSta(canUserQuotaSum.longValue());
                			
                			//对应的配额总表的ID
                			htq.setQuotaNewId(quotaCutoffDayNewEnty.getQuotaId());
                			htq.setQuotaPattern(quotaCutoffDayNewEnty.getQuotaPattern());
                			htq.setQuotaSumRosta(quotaSum);
                			htq.setQuotaUsedSumRosta(quotaCutoffDayNewEnty.getQuotaUsedNumPara());
                			htq.setUsedQuotaQty(quotaCutoffDayNewEnty.getCasualQuotaUsedNum());
                			htq.setComQuotaCot(commQuotaCot);
                			 //房态中，显示配额预警状态，预警数，可用数 hotel2.9.3 add by shengwei.zuo 2009-10-16  end
                			
            			}
        	 		}	
        	 		
        	 	}
        	 	
        	
        		
        	 	
        	}else{//当为空时，就获取配额总表的数据填充到相应的实体类中。	
        		
        		List quotaNewList = setDisplayTempQuotaNew(hotelId,roomTypeId,saleDate,HotelBaseConstantBean.TEMPQUOTA,bedId);
        		
        		List<HtlQuotaNew>  lstQuotaNewEnty = new ArrayList<HtlQuotaNew>();
        		
        		//总的可用配额总数,如果共享方式有多种，就进行叠加
        		Integer sumCanUserQuota = 0;
        		Long quotaSum=0L;
        		for (Iterator iteratorNew = quotaNewList.iterator(); iteratorNew.hasNext();) {
        			
        			List myQuotaNewLst = (List) iteratorNew.next();
        			
        			HtlQuotaNew quotaNew = new HtlQuotaNew() ;
        			
        			//配额总表Id
        			quotaNew.setID((Long)myQuotaNewLst.get(0));
        			
        	 		//临时配额数
        			quotaNew.setCasualQuotaSum((Long)myQuotaNewLst.get(1));
        	 		
        	 		//配额预警数
        	 		quotaNew.setForewarnQuotaNum((Long)myQuotaNewLst.get(2));
        	 			
        	 		//预警标识
        	 		quotaNew.setForewarnFlag((Long)myQuotaNewLst.get(3));
        	 		
        	 		//可用配额总数
        	 		quotaNew.setCanUserQuotaNum(((Long)myQuotaNewLst.get(4)).intValue());
        	 		//临时配额可用数
        	 		quotaNew.setCasualQuotaAbleNum((Long)myQuotaNewLst.get(5));
        	 		//进店模式
        	 		quotaNew.setQuotaPattern((String)myQuotaNewLst.get(6));
        	 		quotaNew.setBuyQuotaAbleNum((Long)myQuotaNewLst.get(7));
        	 		//临时配额已用数
        	 		quotaNew.setCasualQuotaUsedNum((Long)myQuotaNewLst.get(8));
        	 		//合约配额已用数
        	 		quotaNew.setBuyQuotaUsedNum((Long)myQuotaNewLst.get(9));
        	 		//总的可用配额总数,如果共享方式有多种，就进行叠加
        	 		sumCanUserQuota+=quotaNew.getCanUserQuotaNum();
        	 		quotaSum+=quotaNew.getBuyQuotaAbleNum();
        	 		lstQuotaNewEnty.add(quotaNew);
        	
        		}	
        		
        		//配额总表的辅助类 
        		HtlQuotaNew  quotaNewEnty =  new HtlQuotaNew();
        		
        		if(lstQuotaNewEnty!=null&&!lstQuotaNewEnty.isEmpty()){
        			
        			quotaNewEnty = lstQuotaNewEnty.get(0);
        			
        			for(int j=0;j<lstQuota.size();j++){
        				HtlTempQuota htq =(HtlTempQuota) lstQuota.get(j);
        				//直接把临时配额和cutofftime赋值到
            			if(htq.getBedId().equals(bedId)){
                			
                			//房态中，显示配额预警状态，预警数，可用数 hotel2.9.3 add by shengwei.zuo 2009-10-16  begin
            				
            				//临时配额数
                			htq.setQuotaQty(null==quotaNewEnty.getCasualQuotaSum()?0:quotaNewEnty.getCasualQuotaSum().intValue());
                			//临时配额可用数 add by shaojun.yang 2010-01-07
                			htq.setAbleQuotaQty(null==quotaNewEnty.getCasualQuotaAbleNum()?0:quotaNewEnty.getCasualQuotaAbleNum().intValue());
            				
                			//预警数
                			htq.setForewarnQuotaNumRoSta(quotaNewEnty.getForewarnQuotaNum());
                			
                			//预警标识
                			htq.setForewarnFlagRoSta(quotaNewEnty.getForewarnFlag());
                			
                			//总的可用配额总数,如果共享方式有多种，就进行叠加
                			htq.setQuotaAvailableSumRoSta(null==sumCanUserQuota?0:sumCanUserQuota.longValue());
                			
                			//对应的配额总表的ID
                			htq.setQuotaNewId(quotaNewEnty.getID());
                			//进店模式
                			htq.setQuotaPattern(quotaNewEnty.getQuotaPattern());
                			htq.setQuotaSumRosta(quotaSum);
                			htq.setUsedQuotaQty(quotaNewEnty.getCasualQuotaUsedNum());
                			htq.setQuotaUsedSumRosta(quotaNewEnty.getBuyQuotaUsedNum());
                			htq.setComQuotaCot(commQuotaCot);
                			 //房态中，显示配额预警状态，预警数，可用数 hotel2.9.3 add by shengwei.zuo 2009-10-16  end
                			
            			}
        			}	
        			
        		}
        		
        	}
    	}catch(Exception e){
    		log.error("临时配额查询出现错误",e);
    	}
    	

    }
    
    
    
    

    private void setDisplayRoomState(HtlRoom room, RoomStateDisplayBean rsdb) {
        RoomAndBookItemBean rabib = new RoomAndBookItemBean();
        // 设定显示的房间
        rabib.setRoom(room);
        //设置临时配额
        rabib.setTempQuota(saleDao.getTempQuotaByRoomId(room.getID()));
        
        rsdb.getMapRoomAndBook().put("" + room.getRoomTypeId(), rabib);

        // 设定显示的配额,需要把各种共享模式的相加
        // int availQty = 0,usedQty=0;
//        for (int k = 0; k < room.getLstQuotas().size(); k++) {
//            HtlQuota quota = (HtlQuota) room.getLstQuotas().get(k);
//            // 首先寻找普通配额，如果找不到，就取最后个配额用来显示
//            rabib.setQuota(quota);
//            rabib.setTotalAvail(rabib.getTotalAvail() + quota.getAvailQty());
//            rabib.setTotalUsed(rabib.getTotalUsed() + quota.getUsedQty());
//            if (null != quota.getQuotaType()
//                && quota.getQuotaType().equals(BaseConstant.GENERAL_QUOTATYPE)) {
//                rabib.setQuota(quota);
//                // break;
//            }
//        }
        // if (rabib.getQuota()!=null){
        // rabib.getQuota().setAvailQty(availQty);
        // rabib.getQuota().setUsedQty(usedQty);
        // }
        // 设定显示的预订条款
        /*
         * HtlCreditAssure bookItemCreditAssure= saleDao.qryCreditAssure();
         * babib.setBookItemCreditAssure(bookItemCreditAssure);
         */
    }
    
    
    private List setDisplayTempQuota( long hotelId, long roomtypes,Date bizDate,String quotaType,String bedId){
    	return saleDao.queryTempQuota(hotelId,roomtypes,bizDate,quotaType,"CC",bedId);
 
    }
    
    //配额该造，根据相关查询条件，查询配额总表的相关信息 add by shengwei.zuo 2009-10-19
    private List setDisplayTempQuotaNew( long hotelId, long roomtypes,Date bizDate,String quotaType,String bedId){
    	return saleDao.queryTempQuotaNew(hotelId,roomtypes,bizDate,quotaType,"CC",bedId);
 
    }

    public ISaleDao getSaleDao() {
        return saleDao;
    }

    public void setSaleDao(ISaleDao saleDao) {
        this.saleDao = saleDao;
    }

    /*
     * 酒店照片上传
     * 
     * @see com.mangocity.hotel.base.manage.HotelManage#
     * createHtlPicture(com.mangocity.hotel.base.persistence .HtlPicture)
     */
    public Long createHtlPicture(HtlPicture htlPicture) {
        super.save(htlPicture);
        return htlPicture.getID();

    }

    /*
     * 根据酒店ID找酒店照片
     */
    public List findHtlPicture(long hotel_id) {
        String hsql = "from HtlPicture where hotelId = ? order by picture_type";
        List lsHotelPicture = new ArrayList();

        lsHotelPicture = super.query(hsql, hotel_id);
        if (null != lsHotelPicture && 0 < lsHotelPicture.size()) {
            return lsHotelPicture;
        }
        return null;
    }

    /*
     * 酒店照片更新
     * 
     * @see com.mangocity.hotel.base.manage.HotelManage#
     * createHtlPicture(com.mangocity.hotel.base.persistence .HtlPicture)
     */
    public Long updateHtlPicture(HtlPicture htlPicture) {
        super.update(htlPicture);
        return htlPicture.getID();

    }

    public List qryLisHotelToCC(String sql, int startIndex, int maxResults, Object[] obj) {

        return super.doquery(sql, obj, startIndex, maxResults, false);
    }

    public List qryLisRoomTypesToCC(String sql, Object[] obj) {

        return super.doquery(sql, obj, false);
    }

    /**
     * 查询房型对象列表
     * 
     * @param hotelId
     *            酒店id
     * @param roomTypes
     *            房型Id集合
     * @return
     */
    public List<HtlRoomtype> qryRoomTypeBedType(long hotelId, String[] roomTypes) {
        List<HtlRoomtype> lstResult = new ArrayList<HtlRoomtype>();
        List<HtlRoomtype> lstRoomType = super.queryByNamedQuery("lstHotelRoomType", new Object[] { hotelId });
        if (null == roomTypes) {
            return lstRoomType;
        }
        for (HtlRoomtype roomType:lstRoomType) {
            for (String id:roomTypes) {
                Long ID = Long.valueOf(id);
                if (ID.longValue() == roomType.getID().longValue()) {
                    lstResult.add(roomType);
                    break;
                }
            }
        }
        return lstResult;
    }

    /*
     * add by zhineng.zhuang hotel2.6 2009-02-16 CC查询酒店预订条款信息
     * 
     * @see com.mangocity.hotel.base.manage.HotelManage#queryReservationForCC(long, java.util.Date,
     * java.util.Date, java.lang.String)
     */
    public List<HtlPreconcertItem> queryReservationForCC(long hotelId, Date beginDate,
        Date endDate, String roomType) {
        String hsql = "FROM HtlPreconcertItem where hotelId =? and "
            + "priceTypeId=? and validDate between ? and ?  "
            + "and active = '1' order by validDate ";
        long roomTypeId = Long.valueOf(roomType);
        Object[] obj = new Object[] { hotelId, roomTypeId, beginDate,
            DateUtil.getDate(endDate, -1) };
        List<HtlPreconcertItem> query = super.query(hsql, obj);
        return query;
    }

    public List<HtlCreditAssure> qryCreditAssureForCC(long hotelId, Date beginDate, Date endDate,
        String roomType) {
        String hsql = "FROM HtlCreditAssure WHERE hotel_id = ? and room_type like '%" + roomType
            + "%' and ((begin_date <= ? and end_date >= ?)"
            + "or (begin_date > ? and end_date < ?) or (begin_date < ? "
            + "and end_date >= ?)) order by begin_date";
        Object[] obj = new Object[] { hotelId, beginDate, beginDate, beginDate, endDate, endDate,
            endDate };

        List<HtlCreditAssure> query = super.query(hsql, obj);
        return query;
    }

    public String qryCreditAssure(long hotelId, Date beginDate, String roomType) {
        String strCreditAssure = "";
        /*
         * String hsql = "FROM HtlCreditAssure WHERE hotelId = ? and instr('''' ||
         * replace(room_type,',',''''||','||'''') || '''','"+roomType+"')>0 and ((beginDate <= ? and
         * endDate >= ?)" + "or (beginDate <= ? and endDate <= ?) or (beginDate >= ? and endDate >=
         * ?) or (beginDate >= ? and endDate <= ?)) and rownum <=1 order by modifyTime desc ";
         * Object[] obj = new
         * Object[]{hotelId,roomType,beginDate,endDate,beginDate,endDate,beginDate
         * ,endDate,beginDate,endDate};
         * 
         * List<HtlCreditAssure> query = super.query(hsql, obj);
         */
        Map params = new HashMap();
        params.put("hotelId", hotelId);
        params.put("beginDate", DateUtil.dateToString(beginDate));
        params.put("roomType", "%," + roomType + ",%");

        List query = quotaForCCDao.queryCreditAssure("queryCreditAssure", params);
        if (null != query && 0 < query.size()) {

            HtlCreditAssure hca = (HtlCreditAssure) query.get(0);
            // ----------------1-------------
            // 天数
            if (null == hca.getAheadDays() && null != hca.getAheadTimer()
                && !("".equals(hca.getAheadTimer()))) {
                strCreditAssure = "提前";
                strCreditAssure += "0天";
                strCreditAssure += hca.getAheadTimer() + "时间";
            }

            if (null != hca.getAheadDays() && null != hca.getAheadTimer()
                && !("".equals(hca.getAheadTimer()))) {
                strCreditAssure = "提前";
                strCreditAssure += hca.getAheadDays() + "天";
                strCreditAssure += hca.getAheadTimer() + "时间";
            }
            if (null != hca.getAheadDays()
                && (null == hca.getAheadTimer() || "".equals(hca.getAheadTimer()))) {
                strCreditAssure = "提前";
                strCreditAssure += hca.getAheadDays() + "天";
            }

            // ----------------2------------------------
            Date date = new Date();
            // 日期
            if (null == hca.getMustBeforeDate() && null != hca.getMustBeforeTime()
                && !("".equals(hca.getMustBeforeTime()))) {
                if (null != strCreditAssure && !("".equals(strCreditAssure))) {
                    strCreditAssure += "，或者";
                }
                strCreditAssure += "必须在" + DateUtil.dateToString(date) + "日期";
                strCreditAssure += hca.getMustBeforeTime() + "时间之前预订";
            }

            if (null != hca.getMustBeforeDate() && null != hca.getMustBeforeTime()
                && !("".equals(hca.getMustBeforeTime()))) {
                if (null != strCreditAssure && !("".equals(strCreditAssure))) {
                    strCreditAssure += "，或者";
                }
                strCreditAssure += "必须在" + DateUtil.dateToString(hca.getMustBeforeDate()) + "日期";
                strCreditAssure += hca.getMustBeforeTime() + "时间之前预订";
            }
            if (null != hca.getMustBeforeDate()
                && (null == hca.getMustBeforeTime() || "".equals(hca.getMustBeforeTime()))) {
                if (null != strCreditAssure && !("".equals(strCreditAssure))) {
                    strCreditAssure += "，或者";
                }
                strCreditAssure += "必须在" + DateUtil.dateToString(hca.getMustBeforeDate())
                    + "日期之前预定";
            }
            // ------------3-----------------------------

            if (null != hca.getContinueNight() && !("".equals(hca.getContinueNight()))) {
                if (null != strCreditAssure && !("".equals(strCreditAssure))) {
                    strCreditAssure += "，或者";
                }
                strCreditAssure += "连住" + hca.getContinueNight() + "晚.</br>";
            }
            // -----------4---------------------------------
            if (null != hca.getMustDate() && !("".equals(hca.getMustDate()))) {
                strCreditAssure += "连住日期:";
                strCreditAssure += hca.getMustDate();
            }
            // ------------5----------------------------------
            /*
             * if(hca.getRemark() != null && hca.getRemark() != "") { strCreditAssure =
             * strCreditAssure + "<br>预定与担保注意事项:"; strCreditAssure = strCreditAssure +
             * hca.getRemark(); }
             */
        }
        return strCreditAssure;

    }

    public HtlContract findContract(long contractId) {
        return (HtlContract) super.find(HtlContract.class, contractId);
    }

    public int updateHotelNotes(final long hotelId, final String notes) {
        HibernateCallback cb = new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String Hql = "update HtlHotel hotel set hotel.selfNotes = ? where hotel.ID=?  ";
                Query query = session.createQuery(Hql);
                query.setString(0, notes);
                query.setLong(1, hotelId);
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

    /**
     * 更新酒店基本信息表的外网是否显示基本信息字段
     * 
     * @param hotelId
     * @param webShowBase
     * @return
     */
    public int updateHotelWebShowBase(final long hotelId, final String webShowBase) {
        HibernateCallback cb = new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String Hql = "update HtlHotel hotel set hotel.webShowBaseInfo = ? "
                    + "where hotel.ID=?  ";
                Query query = session.createQuery(Hql);
                query.setString(0, webShowBase);
                query.setLong(1, hotelId);
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

    // 增加3D图片信息
    public boolean addHotelPic(HtlPictureInfo hotelPicInfo) {
        boolean flag = false;
        Object o = hotelPictureDao.addHotelPic("insertPic", hotelPicInfo);
        if (null != o) {
            flag = true;
        }
        return flag;
    }

    // 删除3D图片信息
    public boolean deleteHotelPic(HtlPictureInfo hotelPicInfo) {
        boolean flag = false;
        int o = hotelPictureDao.deleteHotelPic("deletePic", hotelPicInfo);
        if (0 < o) {
            flag = true;
        }
        return flag;
    }

    // 修改3D图片信息
    public boolean modifyHotelPic(HtlPictureInfo hotelPicInfo) {
        boolean flag = false;
        int o = hotelPictureDao.modifyHotelPic("updatePic", hotelPicInfo);
        if (0 < o) {
            flag = true;
        }
        return flag;
    }

    // 查询3D图片信息
    public HtlPictureInfo queryHotelPicById(HtlPictureInfo hotelPicInfo) {
        HtlPictureInfo o = hotelPictureDao.queryHotelPicById("selectPicById", hotelPicInfo);
        if (null != o) {
            return o;
        } else {
            return null;
        }
    }

    // 查询3D图片信息列表
    public List queryHotelPicList(HtlPictureInfo hotelPicInfo) {
        List list = hotelPictureDao.queryHotelPicList("selectPic", hotelPicInfo);
        if (null != list && 0 < list.size()) {
            return list;
        } else {
            return new ArrayList();
        }
    }
    public HotelPictureDao getHotelPictureDao() {
        return hotelPictureDao;
    }

    public void setHotelPictureDao(HotelPictureDao hotelPictureDao) {
        this.hotelPictureDao = hotelPictureDao;
    }

    public List<HtlUsersComment> queryHtlUsersComment(long hotelId, 
        int startIndex, int maxResults) {
        // 根据V2.2需求，未通过审核的评论也显示在网页上 modify by CMB
        String hsql = "FROM HtlUsersComment WHERE hotel_id = ? and comment_status in('1','2')";
        return super.doquery(hsql, new Object[] { hotelId }, startIndex, maxResults, false);

    }

    public List<HtlUsersComment> queryHtlUsersAllComment(long hotelId) {
        // 查全部的评论 ADD BY ZHINENG.ZHUANG
        // 生产BUG331网站酒店用户评论排序不正确 modify by chenjiajie 2009-04-10
        String hsql = "FROM HtlUsersComment WHERE hotel_id = ? "
            + "and comment_status in('1','2') order by createTime desc";
        return super.doquery(hsql, new Object[] { hotelId }, false);

    }

    public int queryHtlUsersCommentTotal(long hotelId) {
        // 根据V2.2需求，未通过审核的评论也显示在网页上 modify by CMB
        String hsql = "select count(*) as countRow FROM htl_uses_comment WHERE hotel_id = '"
            + hotelId + "' and comment_status in('1','2')";
        return super.totalNum(hsql);
    }

    public void addHtlUsersComment(HtlUsersComment htlUsersComment) {

        super.save(htlUsersComment);
    }

    public String findCommendTypeByHotelId(long hotelId) {
        String hsql = "from HtlMainCommend commend where commend.hotelID =?";
        List commends = new ArrayList();
        commends = super.query(hsql, hotelId);
        if (0 != commends.size()) {
            return ((HtlMainCommend) commends.get(0)).getCommendType();
        }
        return "";
    }

    /**
     * 根据酒店ID查询相应设定的假期
     * 
     * @param hotelId
     * @return
     */
    public List<SellSeason> qryHoliday(Long hotelId) {
        String hsql = "FROM SellSeason s WHERE s.htlHotel.ID = ?";
        Object[] obj = new Object[] { hotelId };

        List<SellSeason> query = super.query(hsql, obj);
        return query;
    }

    public HtlCreditAssure qryCreditAssureForTMC(long hotelId, Date queryDate, String roomType) {
        /*
         * roomType = "," + roomType + ","; String hsql =
         * "FROM HtlCreditAssure h WHERE hotel_id = ? and ','||room_type||',' like '%" + roomType +
         * "%' and (begin_date <= ? and end_date >= ?)"; String temSql = " and h.createTime
         * in(select max(CREATETIME) from HtlCreditAssure where hotel_id = ? and ','||room_type||','
         * like '%" + roomType + "%' and (begin_date <= ? and end_date >= ?)" + ")"; hsql = hsql +
         * temSql; Object[] obj = new Object[] { hotelId, queryDate, queryDate, hotelId, queryDate,
         * queryDate };
         */
        roomType = "," + roomType + ",";
        String hsql = "FROM HtlCreditAssure h WHERE hotel_id = ? and ','||room_type||',' like '%"
            + roomType + "%' and (begin_date <= ? and end_date >= ?) order by createtime desc";
        Object[] obj = new Object[] { hotelId, queryDate, queryDate };

        List kk = super.query(hsql, obj);
        HtlCreditAssure htlCreditAssure = new HtlCreditAssure();
        if (0 < kk.size()) {
            htlCreditAssure = (HtlCreditAssure) kk.get(0);
        }

        return htlCreditAssure;

    }

    /**
     * 重新找回分数信息
     * 
     * @param score_id
     *            分数id;
     * @return 分数一个实体
     */
    public HtlHotelScore findScore(long score_id) {
        String hsql = "from HtlHotelScore where score_id = ?";
        List lsScore = new ArrayList();

        lsScore = super.query(hsql, score_id);
        if (null != lsScore && 0 < lsScore.size()) {
            return (HtlHotelScore) lsScore.get(0);
        }
        return null;
    }

    /**
     * 找前一天到现在的所有房态更改记录
     * 
     * @param hotelId
     * @return
     */
    public List findRoomStatusDateProcess(long hotelId) {
        List dateRoomProcess = new ArrayList();
        dateRoomProcess = super.queryByNamedQuery("queryRoomStatus", new Object[] { hotelId,
            hotelId });
        return dateRoomProcess;
    }

    /**
     * 取汇率,返回所有兑换人民币的MAP
     * 
     * @return rateExchange
     */
    public Map getExchangeRateMap() {
        Map<String, String> rateExchange = new HashMap<String, String>();
        String hsql = "from HtlExchange he where he.tocurrency ='RMB'";
        List EchangeList = new ArrayList();
        EchangeList = super.doquery(hsql, false);
        if (!EchangeList.isEmpty()) {
            for (int i = 0; i < EchangeList.size(); i++) {
                HtlExchange htlExchange = new HtlExchange();
                htlExchange = (HtlExchange) EchangeList.get(i);
                if (null != htlExchange.getID()) {
                    rateExchange.put(htlExchange.getCurrency(), htlExchange.getRate());
                }
            }
        }
        rateExchange.put("RMB", "1");
        return rateExchange;
    }
    
    public IQuotaForCCDao getQuotaForCCDao() {
        return quotaForCCDao;
    }

    public void setQuotaForCCDao(IQuotaForCCDao quotaForCCDao) {
        this.quotaForCCDao = quotaForCCDao;
    }

    public List findRoomTypePriceTypeLis(long hotelID) {
        List dataLis = new ArrayList();
        String sqlStr = "select new list((r.roomName || p.priceType) as roomChildRoomName ,"
            + "(r.ID || '&&' || price_type_id) as roomChildRoomID) "
            + "from HtlRoomtype as r ,HtlPriceType as p " + "where r.ID = p.roomType.ID "
            + "and r.hotelID = ? " + "order by r.ID";
        dataLis = super.query(sqlStr, hotelID);
        return dataLis;
    }

    public List findRoomTypeLis(long hotelID) {
        List dataLis = new ArrayList();
        String sqlStr = "select new list((r.roomName) as roomChildRoomName ,"
            + "(r.ID) as roomChildRoomID) " + "from HtlRoomtype r " + "where r.hotelID = ? "
            + "order by r.ID";
        dataLis = super.query(sqlStr, hotelID);
        return dataLis;
    }

    public void updateRoomState(final long roomTypeId, String bedType) {
        List<HtlRoom> oldRooms = new ArrayList<HtlRoom>();
        String hsql = " from HtlRoom room where room.roomTypeId = ? and "
            + "room.ableSaleDate>=(sysdate-1)";
        oldRooms = super.query(hsql, roomTypeId);
        if (0 == oldRooms.size()) {
            return;
        }
        /** 生产bug,房型中对床型的更改的状态的更新 by chenjuesu 2009-8-11 Begin **/
        for (HtlRoom oldRoom : oldRooms) {
            String oldState = oldRoom.getRoomState();
            if (null == oldState) {
                oldState = "";
            }
            String[] bedtypes = bedType.trim().split(",");
            StringBuffer buffer = new StringBuffer();
            for (String bedTypeTemp : bedtypes) {
                int index = oldState.indexOf(bedTypeTemp + ":");
                if (-1 == index) {
                    buffer = buffer.append(bedTypeTemp + ":1/");
                    continue;
                }
                buffer = buffer.append(oldState.substring(index, index + 3) + "/");
            }
            final String result = (buffer.deleteCharAt(buffer.length() - 1)).toString();
            if (result.equals(oldState)) {
                return;
            }
            final long oldRoomId = oldRoom.getID();
            HibernateCallback cb = new HibernateCallback() {

                public Object doInHibernate(Session session) throws HibernateException,
                    SQLException {
                    String Hql = " update HtlRoom r set r.roomState = ? where r.ID = ?";
                    Query query = session.createQuery(Hql);
                    query.setString(0, result);
                    query.setLong(1, oldRoomId);
                    query.executeUpdate();
                    return 0;
                }

            };
            getHibernateTemplate().execute(cb);
        }
        /** 生产bug,房型中对床型的更改的状态的更新 by chenjuesu 2009-8-11 end **/
    }

    public int countCommentMember4Hotel(long memberId, long hotelId) {
        // htl_user_comment表：发表时间在一年以内的评论数 add by CMB
        String hsql = "select count(*) as countRow FROM htl_uses_comment WHERE hotel_id = '"
            + hotelId + "' and member_id = '" + memberId + "' and modify_time >= to_date('"
            + getLastYearDate() + "','YYYY-MM-DD')";
        return super.totalNum(hsql);
    }

    public int countOrderMember4Hotel(long memberId, long hotelId) {
        // or_order表：入住时间在一年以内，订单状态为已入住，提前退房或正常退房 add by CMB
        String hsql = "select count(*) as countRow FROM or_order WHERE hotelid = '" + hotelId
            + "' and memberid = '" + memberId
            + "' and orderstate in(4,5,6) and checkindate >= to_date('" + getLastYearDate()
            + "','YYYY-MM-DD')";
        return super.totalNum(hsql);
    }

    public List queryCloseRoom(long hotelID) {
        // TODO Auto-generated method stub
        // HtlOpenCloseRoom
        // 生产bug329 无法查看关房结束日期>=当天的记录 修改为trunc(sysdate) by chenjiajie 2009-04-08
        String hSql = "from HtlOpenCloseRoom where hotelId = ? and "
            + "opCloseSign = 'G' and endDate >= trunc(sysdate) " + "order by beginDate ";
        new ArrayList();
        return super.query(hSql, hotelID);
    }

    // 得到当前时间前一年的日期 add by CMB
    private String getLastYearDate() {
        // 将当前时间的年份减1,月份和天不变 add by CMB
        return DateUtil.dateToString(DateUtil.newDate(DateUtil.getMonthOfSysTime(), DateUtil
            .getDayOfSysTime(), DateUtil.getYearOfSysTime() - 1));
    }

    public long saveOrUpdateCtct(HtlCtct htlCtct) {
        super.saveOrUpdate(htlCtct);
        return 0;
    }

    public void saveOrUpdateExt(HtlHotelExt he) {
        super.saveOrUpdate(he);
    }

    public void saveHtlHotelExt(HtlHotelExt he) {
        super.save(he);
    }

    /**
     * 保存酒店ebooking设置
     */
    public void saveHtlEbooking(HtlEbooking ek) {
        super.merge(ek);
    }

    // 查询一个酒店3D图片的数量
    public int getPictureCountByHotelId(long hotelId) {
        return totalNum(" select count(*) as countRow from Htl_HOTEL_PICINFO  where HOTELID="
            + hotelId);
    }

    public List queryHtlCtct(Long id) {
        String sql = "from HtlCtct where htlHotel.HtlHotel.ID = ?";
        return super.query(sql, id);
    }

    /** Add by chenjiajie 2008-11-28 V2.5 begin **/
    /**
     * 更新映射酒店
     * 
     * @param exMapping
     * @return
     * @author chenjiajie 2008-11-24 Version 2.5
     */
    public Long saveOrUpdateMapping(ExMapping exMapping) {
        super.saveOrUpdate(exMapping);
        return exMapping.getExMappingId();
    }

    public List<ExMapping> findHotelMapping(long hotelId, long mappingType) {
        List dataLis = new ArrayList();
        String hqlStr = null;
        if (mappingType == HotelMappingType.HOTEL_TYPE) {
            hqlStr = "select em from ExMapping em,HtlHotel hh "
                + "where em.hotelid=hh.ID and em.hotelid = ? and em.type = ? and hh.ID=?";
            dataLis = super.query(hqlStr, new Object[] { hotelId, mappingType, hotelId });
        } else if (mappingType == HotelMappingType.ROOM_TYPE) {
            hqlStr = "select em from ExMapping em,HtlRoomtype hh "
                + "where em.roomtypecode=hh.ID and em.hotelid = ? and em.type = ? and hh.hotelID=?";
            dataLis = super.query(hqlStr, new Object[] { hotelId, mappingType, hotelId });
        } else if (mappingType == HotelMappingType.PRICE_TYPE) {
            hqlStr = "select em from ExMapping em,HtlPriceType hh "
                + "where em.pricetypecode=hh.ID and em.hotelid = ? and em.type = ?";
            dataLis = super.query(hqlStr, new Object[] { hotelId, mappingType });
        } else {
            hqlStr = "from ExMapping em where em.hotelid = ? and em.type = ?";
            dataLis = super.query(hqlStr, new Object[] { hotelId, mappingType });
        }
        return dataLis;
    }

    public List<ExMapping> findHotelMapping(long hotelId, long mappingType, long channelType) {
        List dataLis = new ArrayList();
        String hqlStr = null;
        // super.releaseSession(super.getSession());
        if (mappingType == HotelMappingType.HOTEL_TYPE) {
            hqlStr = "select em from ExMapping em,HtlHotel hh "
                + "where em.hotelid=hh.ID and em.hotelid = ? and em.type = ? "
                + "and hh.ID=? and em.channeltype = ?";
            dataLis = super.query(hqlStr,
                new Object[] { hotelId, mappingType, hotelId, channelType });
        } else if (mappingType == HotelMappingType.ROOM_TYPE) {
            hqlStr = "select em from ExMapping em,HtlRoomtype hh "
                + "where em.roomTypeId=hh.ID and em.hotelid = ? and em.type = ?"
                + " and hh.hotelID=? and em.channeltype = ? " + "order by em.roomTypeId";
            dataLis = super.query(hqlStr,
                new Object[] { hotelId, mappingType, hotelId, channelType });
        } else if (mappingType == HotelMappingType.PRICE_TYPE) {
            hqlStr = "select em from ExMapping em,HtlPriceType hh "
                + "where em.priceTypeId=hh.ID and em.hotelid = ? and "
                + "em.type = ? and em.channeltype = ? " + "order by em.roomTypeId,em.priceTypeId";
            dataLis = super.query(hqlStr, new Object[] { hotelId, mappingType, channelType });
        } else {
            hqlStr = "from ExMapping em where em.hotelid = ? and em.type = ? "
                + "and em.channeltype = ?";
            dataLis = super.query(hqlStr, new Object[] { hotelId, mappingType, channelType });
        }
        return dataLis;
    }

    public List findHotelMapping(long hotelId) {
        List dataLis = new ArrayList();
        String hqlStr = "from ExMapping em where em.hotelid = ?";
        dataLis = super.query(hqlStr, hotelId);
        return dataLis;
    }

    /**
     * 根据香港酒店Id找出相应的映射 add by shizhongwen 时间:Apr 22, 2009 5:32:34 PM
     * 
     * @param hkhotelId
     * @return
     */
    public List<ExMapping> findHotelMappingbyHKHotelId(String hkhotelId) {
        List dataLis = new ArrayList();
        String hqlStr = "from ExMapping em where em.hotelcodeforchannel = ?";
        dataLis = super.query(hqlStr, hkhotelId);
        return dataLis;
    }

    /**
     * 此香港酒店hkhotelId 是否已映射 add by shizhongwen 时间:Apr 22, 2009 5:36:03 PM
     * 
     * @param hkhotelId
     * @return
     */
    public boolean isHKHotelMapping(String hkhotelId) {
        List hkHotelMappinglist = this.findHotelMappingbyHKHotelId(hkhotelId);
        return null == hkHotelMappinglist || 0 >= hkHotelMappinglist.size();

    }

    public List<ExMapping> findHotelMapping(long hotelId, long[] type) {
        List dataLis = new ArrayList();
        String typeStr = new String();
        for (int i = 0; i < type.length; i++) {
            typeStr += String.valueOf(type[i]);
            if (i < type.length - 1) {
                typeStr += ",";
            }
        }
        String hqlStr = "from ExMapping em where em.hotelid = ? and em.type in (" + typeStr + ")";
        dataLis = super.query(hqlStr, hotelId);
        return dataLis;
    }

    public List findPriceTypeLis(long hotelId) {
        List dataLis = new ArrayList();
        String sqlStr = "select new list((p.priceType) as roomChildRoomName ,"
            + "(p.ID) as roomChildRoomID ," + "(r.roomName) as roomParentName ,"
            + "(r.ID) as roomParentId) " + "from HtlRoomtype as r ,HtlPriceType as p "
            + "where r.ID = p.roomType.ID " + "and r.hotelID = ? " + "order by r.ID";
        dataLis = super.query(sqlStr, hotelId);
        return dataLis;
    }

    /** Add by chenjiajie 2008-11-28 V2.5 end **/

    /**
     * 根据酒店ID取酒店扩展表信息 add by shizhongwen 时间:Feb 5, 2009 16:15:02
     * 
     * @param hotelId
     * @return
     */
    public List<HtlHotelExt> findHotelExt(Long hotelId) {
        List dataLis = new ArrayList();
        String sqlStr = "select hotelext from HtlHotelExt hotelext where hotelext.htlHotel.ID=?";
        dataLis = super.query(sqlStr, hotelId);
        return dataLis;
    }

    public void deleteAllMappingRoomType(long roomTypeId) {
        // TODO Auto-generated method stub
        String hsql = "";
        hsql += " delete ExMapping e ";
        hsql += " where roomTypeId = ? ";
        Object[] obj = new Object[1];
        obj[0] = String.valueOf(roomTypeId);
        super.remove(hsql, obj);
    }

    /**
     * 根据酒店id删除此酒店与香港的所有映射 add by shizhongwen 时间:Apr 21, 2009 4:49:35 PM
     * 
     * @param hotelid
     * @throws Exception
     */
    public void deleteALLHotelMapping(long hotelid) throws Exception {
        try {
            String hql = "delete ExMapping e where e.hotelid=? and e.channeltype=?";
            Object[] obj = new Object[2];
            obj[0] = Long.valueOf(hotelid);
            obj[1] = Long.valueOf(8);
            super.remove(hql, obj);
        } catch (Exception e) {
            log.error("根据酒店id删除此酒店与香港的所有映射",e);
            throw new Exception("根据酒店id删除此酒店与香港的所有映射");
        }
    }

    public void updateAllMappingGroupCode(final Long hotelId, final Long groupCode,
        final Long channel) {
        // TODO Auto-generated method stub
        HibernateCallback cb = new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String Hql = " update ExMapping m set m.groupcode = ? "
                    + "where m.hotelid = ? and m.channeltype =?";
                Query query = session.createQuery(Hql);
                query.setLong(0, hotelId);
                query.setLong(1, groupCode);
                query.setLong(2, channel);
                query.executeUpdate();
                return 0;
            }
        };

        getHibernateTemplate().execute(cb);
    }

    /**
     * 根据酒店id，房型id，价格id，支付方式，日期查询价格列表 * add by shizhongwen 时间:Mar 8, 2009 4:52:27 PM
     * 
     * @param hotelId
     * @param roomTypeId
     * @param childRoomTypeId
     * @param payMethod
     * @param checkBeginDate
     * @param checkEndDate
     * @return
     */
    public List<HtlPrice> queryHtlPrice(long hotelId, long roomTypeId, long childRoomTypeId,
        String payMethod, String checkBeginDate, String checkEndDate) {
        // TODO Auto-generated method stub
        String hsql = "";
        hsql += "from HtlPrice ";
        hsql += " where roomTypeId = ?";
        hsql += " and  childRoomTypeId= ?";
        hsql += " and  payMethod like ? ";
        hsql += " and  hotelId = ?";
        hsql += " and  ableSaleDate >= ?";
        hsql += " and  ableSaleDate <= ?";
        hsql += " order by ableSaleDate ";
        Object[] params = new Object[6];
        params[0] = roomTypeId;
        params[1] = childRoomTypeId;
        params[2] = payMethod;
        params[3] = hotelId;
        params[4] = DateUtil.getDate(checkBeginDate);
        params[5] = DateUtil.getDate(checkEndDate);
        return super.query(hsql, params);
    }

    /**
     * 根据酒店ID取出所有的房型价格类型用来标注显示与不显示
     * 
     * @param hotelId
     * @return
     */
    public List<HtlCtlDsply> queryHtlCtlDsply(long hotelId) {
        // 调用存储过程，完成所有的显示控制房型的记录写入
        this.InsertHtlCtlDsply(hotelId);

        // 根据芒果网的酒店id查询控制平台显示的数据
        String hsql = "select c.ID,c.hotelID,a.chnName,c.roomTypeId,"
            + "b.roomName,c.priceTypeId,D.priceType,"
            + " c.payMethod,c.CC,c.TP,c.TMC,c.WEB,c.AGENT,c.FLD1,c.FLD2,c.FLD3,c.FLD4 "
            + " from HtlCtlDsply c,HtlHotel a,HtlRoomtype b ,HtlPriceType D"
            + " where a.ID=c.hotelID  and a.ID=b.hotelID "
            + " and b.ishkroomtype = 1  "
            + " and b.ID=c.roomTypeId and D.ID=c.priceTypeId " + " and  c.hotelID=?";
        Object[] obj = new Object[] { hotelId };

        List<HtlCtlDsply> lstCtlDsply = new ArrayList<HtlCtlDsply>();
        List<Object> objectlist = super.query(hsql, obj);
        HtlCtlDsply htl = null;
        int objectlistsize = objectlist.size();
        for (int i = 0; i < objectlistsize; i++) {
            Object[] htlobjects = (Object[]) objectlist.get(i);
            int htlobjectsize = htlobjects.length;
            htl = new HtlCtlDsply();
            for (int j = 0; j < htlobjectsize; j++) {
                switch (j) {
                case 0:
                    htl.setID((Long) htlobjects[j]);
                    break;
                case 1:
                    htl.setHotelID((Long) htlobjects[j]);
                    break;
                case 2:
                    htl.setHotelName((String) htlobjects[j]);
                    break;
                case 3:
                    htl.setRoomTypeId((Long) htlobjects[j]);
                    break;
                case 4:
                    htl.setRoomTypeName((String) htlobjects[j]);
                    break;
                case 5:
                    htl.setPriceTypeId((Long) htlobjects[j]);
                    break;
                case 6:
                    htl.setPriceTypeName((String) htlobjects[j]);
                    break;
                case 7:
                    htl.setPayMethod((String) htlobjects[j]);
                    break;
                case 8:
                    htl.setCC((Long) htlobjects[j]);
                    break;
                case 9:
                    htl.setTP((Long) htlobjects[j]);
                    break;
                case 10:
                    htl.setTMC((Long) htlobjects[j]);
                    break;
                case 11:
                    htl.setWEB((Long) htlobjects[j]);
                    break;
                case 12:
                    htl.setAGENT((Long) htlobjects[j]);
                    break;
                case 13:
                    htl.setFLD1((Long) htlobjects[j]);
                    break;
                case 14:
                    htl.setFLD2((Long) htlobjects[j]);
                    break;
                case 15:
                    htl.setFLD3((Long) htlobjects[j]);
                    break;
                case 16:
                    htl.setFLD4((Long) htlobjects[j]);
                    break;
                }
            }
            try {
                ReflectObject.copy(htl);
            } catch (Exception e) {
                log.error(e);
            }
            lstCtlDsply.add(htl);
        }
        return lstCtlDsply;
    }

    public HtlRoomtype findHtlRoomtype(long roomTypeId) {
        HtlRoomtype hr = (HtlRoomtype) super.find(HtlRoomtype.class, roomTypeId);
        return hr;
    }

    /**
     * 根据子房型Id查子房型信息 add by shizhongwen 时间:Apr 18, 2009 4:42:48 PM
     * 
     * @param roomChildRoomId
     * @return
     */
    public HtlPriceType findHtlPriceType(Long roomChildRoomId) {
        HtlPriceType htlprice = (HtlPriceType) super.find(HtlPriceType.class, roomChildRoomId);
        return htlprice;
    }

    public List<HtlIncreasePrice> queryHtlIncreasePrice() {
        List lstResult = new ArrayList();
        String query = "from HtlIncreasePrice order by channelType, hotelId, greaterThan";
        lstResult = super.doquery(query, false);
        return lstResult;
    }

    public boolean saveOrUpdateHtlIncreasePrice(List<HtlIncreasePrice> increaseList) {
        try {
            List<HtlIncreasePrice> lstResult = queryHtlIncreasePrice();
            List<HtlIncreasePrice> removeList = new ArrayList<HtlIncreasePrice>();
            if (null != lstResult) {
                for (int i = 0; i < lstResult.size(); i++) {
                    HtlIncreasePrice compare = lstResult.get(i);
                    boolean bRemove = true;
                    for (HtlIncreasePrice item : increaseList) {
                        if (null != item.getID()) {
                            if (item.getID().equals(compare.getID())) {
                                bRemove = false;
                                break;
                            }
                        }
                    }
                    if (bRemove) {
                        removeList.add(compare);
                    }
                }
                for (HtlIncreasePrice toRemove : removeList) {
                    super.remove(toRemove);
                }
            }
            for (HtlIncreasePrice toSave : increaseList) {
                super.merge(toSave);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return false;
        }
    }

    public Map getIncreasePriceMap() {
        // 获取加幅记录
        List<HtlIncreasePrice> lstResult = queryHtlIncreasePrice();
        Map increaseMap = new HashMap();
        // 德比
        List<HtlIncreasePrice> debyList = new ArrayList<HtlIncreasePrice>();
        // 畅联
        List<HtlIncreasePrice> colList = new ArrayList<HtlIncreasePrice>();
        // 德尔
        List<HtlIncreasePrice> deerList = new ArrayList<HtlIncreasePrice>();
        // 中科
        List<HtlIncreasePrice> ctsList = new ArrayList<HtlIncreasePrice>();
        // 希尔顿
        List<HtlIncreasePrice> sheltonList = new ArrayList<HtlIncreasePrice>();
        List<HtlIncreasePrice> traList = new ArrayList<HtlIncreasePrice>();
        for (HtlIncreasePrice item : lstResult) {
            if (item.getChannelType().equals("0")) {
                traList.add(item);
                continue;
            } else if (item.getChannelType().equals("1")) {
                debyList.add(item);
                continue;
            } else if (item.getChannelType().equals("2")) {
                colList.add(item);
                continue;
            } else if (item.getChannelType().equals("3")) {
                deerList.add(item);
                continue;
            } else if (item.getChannelType().equals("4")) {
                sheltonList.add(item);
                continue;
            } else if (item.getChannelType().equals("8")) {
                ctsList.add(item);
                continue;
            }
        }
        increaseMap.put(IncreaseBean.DEBY, debyList);
        increaseMap.put(IncreaseBean.COL, colList);
        increaseMap.put(IncreaseBean.DEER, deerList);
        increaseMap.put(IncreaseBean.SHELTON, sheltonList);
        // increaseMap.put(IncreaseBean.CTS, ctsList);        
        increaseMap.put(IncreaseBean.TRA, traList);
        
        // 中旅加幅配置 -- add by chenkeming@2010.01.05
        List<HtlIncreasePrice> tmpList = null;
        Map<Long, List<HtlIncreasePrice>> tmpMap = new HashMap<Long, List<HtlIncreasePrice>>();
        for(HtlIncreasePrice increase : ctsList) {
        	Long hotelId = increase.getHotelId();
        	tmpList = tmpMap.get(hotelId);
        	if(null == tmpList) {
        		tmpList = new ArrayList<HtlIncreasePrice>();
        		tmpList.add(increase);
        		tmpMap.put(hotelId, tmpList);
        	} else {
        		tmpList.add(increase);
        	}
        }
        IncreaseBean.increaseCtsMap = tmpMap;
        
        return increaseMap;
    }

    /**
     * 调用存储过程，完成所有的显示控制房型的记录写入 add by shizhongwen 时间:May 6, 2009 2:49:53 PM
     * 
     * @param hotelId
     */
    public void InsertHtlCtlDsply(long hotelId) {
        CallableStatement cstmt = null;
        // 调用存储过程，完成所有的显示控制房型的记录写入
        try {
            log.info("调用存储过程，完成所有的显示控制房型的记录写入");
            String procedureName = "{call prc_Ctl_dsply(?)}";
            cstmt = super.getCurrentSession().connection().prepareCall(procedureName);
            cstmt.setLong(1, hotelId);
            cstmt.execute();
        } catch (Exception e) {
            log.error(" call prc_ctl_dsply find a error :" + e);
        } finally {
            if (null != cstmt) {
                try {
                    cstmt.close();
                } catch (SQLException e) {
                	log.error(e.getMessage(),e);
                }
            }
        }
    }

    public List<HtlHotel> queryHtlHotelList(String gisids) {
        // TODO Auto-generated method stub
        String hsql = "";
        hsql += " from HtlHotel ";
        hsql += " where gisid in ";
        hsql += " (";
        hsql += gisids + ")";
        return super.query(hsql, null);
    }

    /**
     * 根据酒店ID和渠道取出该酒店的加幅记录 add by chenjiajie 2009-08-03
     * 
     * @param hotelId
     * @param channelType
     * @return
     */
    public List<HtlIncreasePrice> queryHtlIncreasePrice(Long hotelId, String channelType) {
        List lstResult = new ArrayList();
        String hql = "from HtlIncreasePrice where hotelId = ? "
            + "and channelType = ? order by greaterThan";
        lstResult = super.doquery(hql, new Object[] { hotelId, channelType }, false);
        return lstResult;
    }

    /**
     * 保存更新某酒店的加幅记录 add by chenjiajie 2009-08-03
     * 
     * @param increaseList
     * @param hotelId
     * @param channelType
     * @return
     */
    public boolean saveOrUpdateHtlIncreasePrice(List<HtlIncreasePrice> increaseList, Long hotelId,
        String channelType) {
        try {
            List<HtlIncreasePrice> lstResult = queryHtlIncreasePrice(hotelId, channelType);
            List<HtlIncreasePrice> removeList = new ArrayList<HtlIncreasePrice>();
            if (null != lstResult) {
                for (int i = 0; i < lstResult.size(); i++) {
                    HtlIncreasePrice compare = lstResult.get(i);
                    boolean bRemove = true;
                    for (HtlIncreasePrice item : increaseList) {
                        if (null != item.getID()) {
                            if (item.getID().equals(compare.getID())) {
                                bRemove = false;
                                break;
                            }
                        }
                    }
                    if (bRemove) {
                        removeList.add(compare);
                    }
                }
                for (HtlIncreasePrice toRemove : removeList) {
                    super.remove(toRemove);
                }
            }
            for (HtlIncreasePrice toSave : increaseList) {
                super.merge(toSave);
            }
            return true;
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
            return false;
        }
    }

    /**
     * 删除酒店的加幅记录 add by chenjiajie 2009-08-03
     * 
     * @param hotelIdStr
     */
    public void deleteHtlIncreasePriceBatch(String hotelIdStr) {
        String hql = "delete from HtlIncreasePrice where hotelId in (" + hotelIdStr + ")";
        super.doUpdateBatch(hql, new Object[] {});
    }

    /**
     * 批量查询酒店的基本信息 add by chenjiajie 2009-08-03
     * 
     * @param hotelIds
     *            酒店id 用,分隔
     * @return
     */
    public List queryHotelInfoBatch(String hotelIds) {
        String hql = " from HtlHotel where ID in (" + hotelIds + ")";
        return super.doquery(hql, false);
    }

    /**
     * 拼装酒店地址 hotel2.9.2 add by chenjiajie 2009-08-18
     * 
     * @param hotelObj
     * @return
     */
    public String joinHotelAddress(HotelAddressInfo hotelAddressInfo) {
    	
        String address = "";
        // 省份
        String state = "";
        // 城市
        String city = "";
        // 城区
        String zone = "";
        // 酒店原有的地址
        String chnAddress = "";
        // 网站组装
      
        // refactor
        // 省份
        state = InitServlet.localProvinceObj.get(hotelAddressInfo.getState());
        // 城市
        city = InitServlet.localCityObj.get(hotelAddressInfo.getCity());
        // 城区
        zone = InitServlet.citySozeObj.get(hotelAddressInfo.getZone());
        // 酒店原有的地址
        chnAddress = hotelAddressInfo.getChnAddress();
        


        // 杜绝城市的null值
        if (StringUtil.isValidStr(city)) {
            city = city.replaceAll("[A-Z]", "");
        } else {
            city = "";
        }

        // 杜绝省份的null值
        if (StringUtil.isValidStr(state)) {
            state = state.replaceAll("[A-Z]", "");
            // 如果城市的中文出现在省份中，则清空省份
            if (StringUtil.isValidStr(city) && 0 <= state.indexOf(city)) {
                state = "";
            }
        } else {
            state = "";
        }

        // 杜绝城区的null值
        if (StringUtil.isValidStr(zone)) {
            zone = zone.replaceAll("[A-Z]", "");
            if (StringUtil.isValidStr(city) && 0 <= zone.indexOf(city)) {
                zone = "";
            }
        } else {
            zone = "";
        }
        address = state + city + zone + chnAddress;
        return address;
    }
    
    /**
     * 根据相应条件，查询对应的有配额预警的房态和临时配额信息
     * 
     * add by shengwei.zuo  2009-10-21
     * @param hotelId
     * @param beginDate
     * @param endDate
     * @param week
     * @param roomTypes
     * @param queryCCSetRoomState 是否查询CC设置房态还是配额预警
     * @return
     */
    
    public List qryRoomForQuota(long hotelId, String [] week,List roomTypes,List hQuoDateList,Object queryCCSetRoomState) {
    	
            HashMap mapRoomTypes = new HashMap();
            
            int  roomTypeSize = roomTypes.size();
            
            String roomTypesArry [] =  new  String[roomTypeSize]; 
            
            for (int i = 0; i < roomTypes.size(); i++) {
                HtlRoomtype roomtype = (HtlRoomtype) roomTypes.get(i);
                roomTypesArry[i] = roomtype.getID().toString();
                mapRoomTypes.put("" + roomtype.getID(), roomtype);
            }
            
            Date beginDate = new Date();
            
            Date endDate = new Date();
            
            //有配额预警的开始日期和结束日期
         	if(!hQuoDateList.isEmpty()) {
    	 		
    	 		beginDate = DateUtil.getDate(String.valueOf( hQuoDateList.get(0)));
    	 		
    	 		endDate = DateUtil.getDate(String.valueOf(hQuoDateList.get(hQuoDateList.size()-1)));
    	 		
         	}	
            
            // 这里查询来的记录是按日期排序的，
            List lstRooms = saleDao.qryRoomState(hotelId, beginDate, endDate, week, roomTypesArry);
            HashMap map = new HashMap();
            String theDateStr = null;
            for (int i = 0; i < lstRooms.size(); i++) {
                HtlRoom room = (HtlRoom) lstRooms.get(i);
                theDateStr = DateUtil.formatDateToSQLString(room.getAbleSaleDate());
                if (null != room) {
                    RoomStateDisplayBean rsdb;
                    rsdb = (RoomStateDisplayBean) map.get(theDateStr);
                    if (null == rsdb) {
                        rsdb = new RoomStateDisplayBean();
                        rsdb.setWeek(room.getWeek());
                        rsdb.setSaleRoomDate(room.getAbleSaleDate());
                    }

                    setDisplayRoomState(room, rsdb);
                    map.put(theDateStr, rsdb);
                    
                }
            }

            
            List lstDisplayRoomStates = new ArrayList();
            // 循环日期
            Date bizDate = beginDate;
            for (int j = 0; j < hQuoDateList.size(); j++) {
            	
            	bizDate = DateUtil.getDate(String.valueOf(hQuoDateList.get(j)));
            	
                RoomStateDisplayBean rsd = (RoomStateDisplayBean) map.get(DateUtil
                    .formatDateToSQLString(bizDate));
                if (null != rsd) {
                    for (int m = 0; m < roomTypesArry.length; m++) {
                        String roomTypeId = roomTypesArry[m];
                        RoomAndBookItemBean rabib = (RoomAndBookItemBean) rsd.getMapRoomAndBook().get(
                            roomTypeId);
                        if (null == rabib) {
                            rabib = new RoomAndBookItemBean();
                        }
                        
                        Long roomTypeIdLong  =  Long.parseLong(roomTypeId);
                        
                        //判断房型下面的床型是否有预警，有的话就显示，没有就不显示 add by shengwei.zuo 2009-10-22
                        
                        rabib.setRoomTypeId(roomTypeIdLong);
                        // 当房型的所有价格类型都为关房时，把标志位置为true. add by zhineng.zhuang 2008-09-27
                        HtlRoom htlRoom = new HtlRoom();
                        htlRoom = rabib.getRoom();
                        String isAllClose = "0";
                        if (null != htlRoom) {
                            List lstPrice = new ArrayList();
                            lstPrice = htlRoom.getLstPrices();
                            HtlPrice htlPrice = new HtlPrice();
                            int closeNum = 0;
                            for (int i = 0; i < lstPrice.size(); i++) {
                                htlPrice = (HtlPrice) lstPrice.get(i);
                                // 关房原因为停止合作8/策略性关房CC不可订6/停业2/装修4/价格为0时
                                //modify by zhijie.gu 2010-01-13 不管关房原因是哪个，都允许修改房态，只有价格为0是不允许
                                if (null != htlPrice) {
                                    String reason = htlPrice.getReason();
                                    if (0 == Double.compare(0.0,htlPrice.getSalePrice())) {
                                        closeNum++;
                                    }
                                }
                            }
                            // 当房型的价格类型全部关房，或者没录价格时
                            if (lstPrice.size() == closeNum) {
                                isAllClose = "1";
                            }
                        }
                        rabib.setIsAllClose(isAllClose);

                        // 为房态查询结果加关房原因，精确到子房型。 modify by zhineng.zhuang
                        List queryCloseRoom = new ArrayList();
                        queryCloseRoom = hotelPictureDao.queryCloseRoom(roomTypeId, beginDate, endDate);
                        List<CloseRoomReasonBean> crrbList = new ArrayList<CloseRoomReasonBean>();
                        for (int i = 0; i < queryCloseRoom.size(); i++) {
                            CloseRoomReasonBean closeRoomReasonBean = new CloseRoomReasonBean();
                            closeRoomReasonBean = (CloseRoomReasonBean) queryCloseRoom.get(i);
                            if (0 == DateUtil.compare(closeRoomReasonBean.getSaleDate(), bizDate)) {
                                crrbList.add(closeRoomReasonBean);
                            }
                        }
                        rabib.setCloseRoomReasonBean(crrbList);

                        HtlRoomtype roomtype = (HtlRoomtype) mapRoomTypes.get(roomTypeId);
                        
                        if (null != roomtype) {
                        	
                            rabib.setBedType(roomtype.getBedType());
                            
                            if (null == rabib.getTempQuota()) {
                                List lstTempQuota = new ArrayList();
                                rabib.setTempQuota(lstTempQuota);
                            }
                            if (StringUtil.isValidStr(roomtype.getBedType())) {
                                String[] beds = roomtype.getBedType().split(",");

                                for (int ibed = 0; ibed < beds.length; ibed++) {
                                    String bed = beds[ibed];
                                    
                                    boolean found = false;
                                    for (int iTemp = 0; iTemp < rabib.getTempQuota().size(); iTemp++) {
                                        HtlTempQuota tq = (HtlTempQuota) rabib.getTempQuota()
                                            .get(iTemp);
                                        if (tq.getBedId().equals(bed)) {
                                            tq.setBedStatus(getBedState(rabib, bed, tq));
                                            //这里会重新去新表中读取临时配额,以及预警数，预警状态，可用配额总数
                                            setTempQuotaNew(htlRoom.getHotelId(),htlRoom.getRoomTypeId(),htlRoom.getAbleSaleDate(),rabib,bed);
                                            found = true;
                                            break;
                                        }
                                    }
                                    
                                    if (!found) {
                                    	
                                    	//此时临时配额表没有记录。临时配额数，可用配额总数，配额预警数都得从新表取数据,add by shengwei.zuo 
                                    	
                                        HtlTempQuota tq1 = new HtlTempQuota();
                                        
                                    	List nulTempList = setNullTempQuotaForQuotaNew(hotelId,Long.parseLong(roomTypeId),bizDate,bed);
                                        if(!nulTempList.isEmpty()){
                                        	
                                        	tq1 = (HtlTempQuota)nulTempList.get(0);
                                        	
                                        }else{
                                        	
                                        	tq1.setQuotaQty(0);
                                        }
                                        
                                        tq1.setBedId(bed);
                                        String bedState = getBedState(rabib, bed, tq1);
                                        tq1.setBedStatus(bedState);
                                        tq1.setTempQuotaMode("A");
                                        
                                        rabib.getTempQuota().add(tq1);
                                    } 
                                    
                                }  
                            }  
                        
                        }
                        
                        List lstTempQuota = rabib.getTempQuota();
                        
                        List lstTempQuotaNew = new ArrayList();
                        
                        for(int t=0;t<lstTempQuota.size();t++){
                        	 
                        	HtlTempQuota  HtlTempQuota = (HtlTempQuota)lstTempQuota.get(t);
                        	
                        	 //校验该房型下的某一床型在某一天 是否 配额预警的 add shengwei.zuo 2009-10-22
                        	if(null == queryCCSetRoomState){
	                            List ppf = isShowBedType(hotelId,roomTypeIdLong,Long.parseLong(HtlTempQuota.getBedId()),bizDate);
	                            if(ppf!=null&&ppf.size()>0){
	                            	lstTempQuotaNew.add(HtlTempQuota);
	                            }
                        	}else{
                        		//如果是查询CC设置房态，则不用过滤配额预警的
                        		lstTempQuotaNew.add(HtlTempQuota);
                        	}
                        }
                        
                        rabib.setTempQuota(lstTempQuotaNew);
                        
                        rsd.getLstRoomAndBookItems().add(rabib);
                        
                    }
                    lstDisplayRoomStates.add(rsd);
                }
                
            }
            return lstDisplayRoomStates;
        }

    
    /**
     * //校验该房型下的某一床型在某一天 是否 配额预警的 add shengwei.zuo 2009-10-22
     * @param hotelId
     * @param roomtypeId
     * @param bedId
     * @param ableSaleDate
     * @return
     */
    public List isShowBedType(Long hotelId,Long roomtypeId,Long bedId,Date ableSaleDate){
    	
    	 String dateStr = DateUtil.dateToString(ableSaleDate);
    	
      	 List  dataList = new ArrayList();
         
    	 String sqlStr = " select distinct quotanew.HTL_QUOTA_NEW_ID \n"+
					     " from HTL_QUOTA_NEW quotanew \n"+
					     " where quotanew.HOTEL_ID = "+hotelId+" \n"+
					     "   and quotanew.ROOMTYPE_ID = "+roomtypeId+"\n"+
					     "   and quotanew.BED_ID = "+bedId+" \n"+
					     "   and quotanew.ABLE_SALE_DATE = to_date('"+dateStr+"', 'yyyy-MM-dd')\n"+
					     "   and quotanew.FOREWARN_FLAG = 1";
         
    	 dataList = super.getSession().createSQLQuery(sqlStr).list();
    	 
         if (null == dataList || 0 == dataList.size()) {
             return null;
         }
         
         return dataList;
    	
    }
    
    /**
     * 临时配额表中没有相关记录，通过查询配额新表，插入相关记录
     * add by shengwei.zuo 2009-10-24 配额改造
     * @param hotelId
     * @param roomTypeId
     * @param saleDate
     * @param raid
     */
    private List setNullTempQuotaForQuotaNew(long hotelId,long roomTypeId,Date saleDate ,String bedTypeId){
    	
    	List nullTempQuotaList = new ArrayList();
    	
    	try{
    		List quotaList = setDisplayTempQuota(hotelId,roomTypeId,saleDate,HotelBaseConstantBean.TEMPQUOTA,bedTypeId);
    		
    		
    		String commQuotaCot = composeStr(quotaList);
    		if(commQuotaCot != "")
    			log.info("=======commQuotaCot"+commQuotaCot);
    		
    		/** 房控合约配额显示BUG修复 add by xiaowei.wang begin*/
    		//房控优化，拆分普通配额的数据。
    		//splitQuotaList(quotaList);
    		removeTheSameObject(quotaList);
    		/** 房控合约配额显示BUG修复 add by xiaowei.wang end*/
    		//判断是否有记录
        	if(!quotaList.isEmpty()){
        	
        	    //房态中，显示配额预警状态，预警数，可用数 hotel2.9.3 add by shengwei.zuo 2009-10-16  begin
        		
        		List<HtlQuotaCutoffDayNew>  lstQuoCuffDayNew = new  ArrayList<HtlQuotaCutoffDayNew>();
        		
        		//总的可用配额总数,如果共享方式有多种，就进行叠加
        		Integer canUserQuotaSum = 0 ;
        		Long qutoaSum=0L;
        	    //房态中，显示配额预警状态，预警数，可用数 hotel2.9.3 add by shengwei.zuo 2009-10-16  begin
        	 	for (Iterator iterator = quotaList.iterator(); iterator.hasNext();) {
        	 		
        	 		List myQutaAndNewList = (List) iterator.next();
        	 		
        	 		HtlQuotaCutoffDayNew quotaNew =(HtlQuotaCutoffDayNew) myQutaAndNewList.get(0);
        	 		
        	 		//配额预警数
        	 		quotaNew.setForewarnQuotaNum((Long)myQutaAndNewList.get(1));
        	 		
        	 		//预警状态
        	 		quotaNew.setForewarnFlag((Long)myQutaAndNewList.get(2));
        	 		
        	 		//可用配额总数
        	 		quotaNew.setCanUserQuotaNum(((Long)myQutaAndNewList.get(3)).intValue());
        	 		//可用临时配额
        	 		quotaNew.setCasualQuotaAbleNum((Long)myQutaAndNewList.get(4));
        	 		//进店模式
        	 		quotaNew.setQuotaPattern((String)myQutaAndNewList.get(5));
        	 		quotaNew.setBuyQuotaAbleNum((Long)myQutaAndNewList.get(6));
        	 		//临时配额已用数
        	 		quotaNew.setCasualQuotaUsedNum((Long)myQutaAndNewList.get(7));
        	 		//合约配额已用数
        	 		quotaNew.setQuotaUsedNumPara((Long)myQutaAndNewList.get(8));
        	 		//总的可用配额总数,如果共享方式有多种，就进行叠加
        	 		canUserQuotaSum+=quotaNew.getCanUserQuotaNum();
        	 		qutoaSum+=quotaNew.getBuyQuotaAbleNum();
        	 		lstQuoCuffDayNew.add(quotaNew);
        	 		
        	 	}
        	 	
        	 	//辅助的配额明细类  add by shengwei.zuo 2009-11-09
        	 	HtlQuotaCutoffDayNew   quotaCutoffDayNewEnty =  new HtlQuotaCutoffDayNew();
        	 	
        	 	if(lstQuoCuffDayNew!=null&&!lstQuoCuffDayNew.isEmpty()){
        	 		
        	 		quotaCutoffDayNewEnty = lstQuoCuffDayNew.get(0);
        	 		
        	 		HtlTempQuota tempQuotaObj = new HtlTempQuota();
        	 		
        			//直接把临时配额和cutofftime赋值到
        	 		tempQuotaObj.setQuotaQty(null==quotaCutoffDayNewEnty.getQuotaNum()?0:quotaCutoffDayNewEnty.getQuotaNum().intValue());
        	 		tempQuotaObj.setCutofftime(quotaCutoffDayNewEnty.getCutofftime());
        	 		//增加临时配额可用数与cutoffday
        	 		tempQuotaObj.setCutoffday(quotaCutoffDayNewEnty.getCutoffday());
        	 		tempQuotaObj.setAbleQuotaQty(null==quotaCutoffDayNewEnty.getCasualQuotaAbleNum()?0:quotaCutoffDayNewEnty.getCasualQuotaAbleNum().intValue());
        	 		tempQuotaObj.setQuotaCutoffDayNewId(quotaCutoffDayNewEnty.getID());
                			
                	//房态中，显示配额预警状态，预警数，可用数 hotel2.9.3 add by shengwei.zuo 2009-10-16  begin
                			
                	//预警数
        	 		tempQuotaObj.setForewarnQuotaNumRoSta(quotaCutoffDayNewEnty.getForewarnQuotaNum());
                			
                	//预警标识
        	 		tempQuotaObj.setForewarnFlagRoSta(quotaCutoffDayNewEnty.getForewarnFlag());
                			
                	//可用配额总数	
        	 		tempQuotaObj.setQuotaAvailableSumRoSta(canUserQuotaSum.longValue());
                			
                	//对应的配额总表的ID
        	 		tempQuotaObj.setQuotaNewId(quotaCutoffDayNewEnty.getQuotaId());
        	 		tempQuotaObj.setQuotaPattern(quotaCutoffDayNewEnty.getQuotaPattern());
        	 		tempQuotaObj.setQuotaSumRosta(qutoaSum);
        	 		tempQuotaObj.setUsedQuotaQty(quotaCutoffDayNewEnty.getCasualQuotaUsedNum());
        	 		tempQuotaObj.setQuotaUsedSumRosta(quotaCutoffDayNewEnty.getQuotaUsedNumPara());
        	 		tempQuotaObj.setComQuotaCot(commQuotaCot);
                			
        	 		//房态中，显示配额预警状态，预警数，可用数 hotel2.9.3 add by shengwei.zuo 2009-10-16  end
                	
        	 		nullTempQuotaList.add(tempQuotaObj);

        	 		
        	 	}
    	 	
        	 	
        	}else{//当为空时，就获取配额总表的数据填充到相应的实体类中。	
        		
        		List quotaNewList = setDisplayTempQuotaNew(hotelId,roomTypeId,saleDate,HotelBaseConstantBean.TEMPQUOTA,bedTypeId);
        		
        		List<HtlQuotaNew>  lstQuotaNewEnty = new ArrayList<HtlQuotaNew>();
        		
        		//总的可用配额总数,如果共享方式有多种，就进行叠加
        		Integer sumCanUserQuota = 0;
        		Long qutoaSum=0L;
        		for (Iterator iteratorNew = quotaNewList.iterator(); iteratorNew.hasNext();) {
        			
        			List myQuotaNewLst = (List) iteratorNew.next();
        			
        			HtlQuotaNew quotaNew = new HtlQuotaNew() ;
        			
        			//配额总表Id
        			quotaNew.setID((Long)myQuotaNewLst.get(0));
        			
        	 		//临时配额数
        			quotaNew.setCasualQuotaSum((Long)myQuotaNewLst.get(1));
        	 		
        	 		//配额预警数
        	 		quotaNew.setForewarnQuotaNum((Long)myQuotaNewLst.get(2));
        	 			
        	 		//预警标识
        	 		quotaNew.setForewarnFlag((Long)myQuotaNewLst.get(3));
        	 		
        	 		//可用配额总数
        	 		quotaNew.setCanUserQuotaNum(((Long)myQuotaNewLst.get(4)).intValue());
        	 		//可用临时配额数 shaojun.yang
        	 		quotaNew.setCasualQuotaAbleNum((Long)myQuotaNewLst.get(5));
        	 		quotaNew.setQuotaPattern((String)myQuotaNewLst.get(6));
        	 		quotaNew.setBuyQuotaAbleNum((Long)myQuotaNewLst.get(7));
        	 		//临时配额已用数
        	 		quotaNew.setCasualQuotaUsedNum((Long)myQuotaNewLst.get(8));
        	 		//合约配额已用数
        	 		quotaNew.setBuyQuotaUsedNum((Long)myQuotaNewLst.get(9));
        	 		//总的可用配额总数,如果共享方式有多种，就进行叠加
        	 		sumCanUserQuota+=quotaNew.getCanUserQuotaNum();
        	 		qutoaSum+=quotaNew.getBuyQuotaAbleNum();
        	 		lstQuotaNewEnty.add(quotaNew);
        	
        		}	
        		
        		//配额总表的辅助类 
        		HtlQuotaNew  quotaNewEnty =  new HtlQuotaNew();
        		
        		if(lstQuotaNewEnty!=null&&!lstQuotaNewEnty.isEmpty()){
        			
        			quotaNewEnty = lstQuotaNewEnty.get(0);
        			

            		//房态中，显示配额预警状态，预警数，可用数 hotel2.9.3 add by shengwei.zuo 2009-10-16  begin
        	 		
        	 		HtlTempQuota tempQuotaObj = new HtlTempQuota();
        	 		
        	 		//临时配额数
        	 		tempQuotaObj.setQuotaQty(quotaNewEnty.getCasualQuotaSum().intValue());
        	 		//临时配额可用数 add by shaojun.yang 2010-01-07
        	 		tempQuotaObj.setAbleQuotaQty(null==quotaNewEnty.getCasualQuotaAbleNum()?0:quotaNewEnty.getCasualQuotaAbleNum().intValue());
        	 		
                	//预警数
        	 		tempQuotaObj.setForewarnQuotaNumRoSta(quotaNewEnty.getForewarnQuotaNum());
                			
                	//预警标识
                	tempQuotaObj.setForewarnFlagRoSta(quotaNewEnty.getForewarnFlag());
                			
                	//可用配额总数	
                	tempQuotaObj.setQuotaAvailableSumRoSta(null==sumCanUserQuota?0:sumCanUserQuota.longValue());
                			
                	//对应的配额总表的ID
                	tempQuotaObj.setQuotaNewId(quotaNewEnty.getID());
                	tempQuotaObj.setQuotaPattern(quotaNewEnty.getQuotaPattern());
                	tempQuotaObj.setQuotaSumRosta(qutoaSum);
                	tempQuotaObj.setUsedQuotaQty(quotaNewEnty.getCasualQuotaUsedNum());
                	tempQuotaObj.setQuotaUsedSumRosta(quotaNewEnty.getBuyQuotaUsedNum());
                	tempQuotaObj.setComQuotaCot(commQuotaCot);
                	//房态中，显示配额预警状态，预警数，可用数 hotel2.9.3 add by shengwei.zuo 2009-10-16  end
                			
                	nullTempQuotaList.add(tempQuotaObj);	
        			
        		}
        		
        		
        	}
    	}catch(Exception e){
    		log.error("临时配额查询出现错误",e);
    	}
    	
    	
    	return nullTempQuotaList;
    	
    }
    /**
     * 为此酒店设置ebooking超级用户
     * @param htlHotel
     * @param name
     * @param pass
     */
	public void setEbookingSupderUser(HtlHotel htlHotel, String operId,String operName,String name, String pass) {
		// TODO Auto-generated method stub
		HtlEbookingPersonnelInfo personnelInfoBean = new HtlEbookingPersonnelInfo();
		/* 系统时间 */
		String dateId = DateUtil.toStringByFormat(new Date(),"yyyyMMddHHmmss");
		personnelInfoBean.setPersonnelid(Long.valueOf(dateId));
		personnelInfoBean.setLoginname(name);
		personnelInfoBean.setLoginpass(pass);
		personnelInfoBean.setName(name);
		personnelInfoBean.setDeptname("无");
		personnelInfoBean.setPersonnelvalidity(1L);
		personnelInfoBean.setHotelname(htlHotel.getChnName());
		personnelInfoBean.setHotelid(String.valueOf(htlHotel.getID()));
		personnelInfoBean.setState(Long.valueOf("1"));//用户状态
		personnelInfoBean.setSuperper(Long.valueOf("1"));//超级管理员
		personnelInfoBean.setOperationer(operName);//操作员
		personnelInfoBean.setOperationerid(operId);//操作ID
		personnelInfoBean.setLoginamo(0l);//登录数
		personnelInfoBean.setOperationdate(new Date());//操作时间	
		//保存人员对象
		super.save(personnelInfoBean);
		//设置其权限
		HtlEbookingPersonFunction pf = null;
		List<HtlEbookingFunctionMaster> mastList = getEBookingFunctionMaster();
		for(int i=0;i<mastList.size();i++){
			HtlEbookingFunctionMaster ebfm = mastList.get(i);
			pf= new HtlEbookingPersonFunction();
			pf.setFunmasid(ebfm.getFunmasID());//功能ID
			pf.setPerfunid(Long.valueOf(dateId)+i);//ID
			pf.setPersonnelid(Long.valueOf(dateId));//用户表ID
			super.save(pf);
		}
	}
	/**
	 * 
	 * @return
	 */
	public List<HtlEbookingFunctionMaster> getEBookingFunctionMaster(){
		String hsql ="from HtlEbookingFunctionMaster";
		return super.query(hsql);
	}
	
	public List getEBookingUsersByHotelId(String hotelId){
		String hql = "from HtlEbookingPersonnelInfo where hotelid = ? order by state desc";
		return super.doquery(hql, hotelId, false);
	}
	public boolean deleteEBookingUserbyId(Long uid){
		super.remove(HtlEbookingPersonnelInfo.class, uid);
		return true;
	}
	/**
     * 查询用户权限
     * @param hotelId
     * @param loginName
     * @return
     */
	public List<HtlEbookingFunctionMaster> queryEbookingMaster(String hotelId,
			String loginName) {
		
		String hql = "from HtlEbookingPersonnelInfo p , HtlEbookingFunctionMaster fm ,"
            +" HtlEbookingPersonFunction pf where p.personnelid = pf.personnelid "
            +" and pf.funmasid = fm.funmasID and p.loginname=?  and  p.hotelid = ? ";
		
		List list = super.query(hql, new Object[]{loginName,hotelId});
		
		return list;
	}
    
	/**
	 * 根据渠道查询所有的酒店或房型
	 * add by yong.zeng 2009-12-24
	 * @param type 1/2/3 酒店/房型/价格类型
	 * @param channelType 渠道编码
	 * @return
	 */
    public List getHotelMapping(long type,long channelType) {
    	List dataLis = null;
    	StringBuilder hqlStrBuilder  = new StringBuilder();
	    	 if (type == HotelMappingType.HOTEL_TYPE) {
	    		 hqlStrBuilder.append("select em from ExMapping em, HtlHotel hh");
	    		 hqlStrBuilder.append("			 where hh.ID = em.hotelid ");
	    		 hqlStrBuilder.append("			 and   hh.active =1 ");
	    		 hqlStrBuilder.append("			 and   em.isActive =1 ");
	    		 hqlStrBuilder.append(" 		 and   em.type = ? ");
	    		 hqlStrBuilder.append(" 		 and   em.channeltype = ? ");
	             dataLis = super.query(hqlStrBuilder.toString(),new Object[] {type,  channelType });
	         } else if (type == HotelMappingType.ROOM_TYPE) {
	        	 hqlStrBuilder.append("select em from ExMapping em, HtlRoomtype hr");
	        	 hqlStrBuilder.append(" where exists (select 1 from ExMapping m ");
	        	 hqlStrBuilder.append("					 where m.hotelid =em.hotelid ");
	        	 hqlStrBuilder.append("					 and m.channeltype = ? ");
	        	 hqlStrBuilder.append("					 and m.type =1 ");
	        	 hqlStrBuilder.append("					 and m.isActive =1)");
	        	 hqlStrBuilder.append("	and hr.ID =em.roomTypeId ");
	        	 hqlStrBuilder.append("	and hr.active =1 ");
	        	 hqlStrBuilder.append("	and em.isActive =1 ");
	        	 hqlStrBuilder.append("	and em.type = ? ");
	        	 hqlStrBuilder.append("	and em.channeltype = ? ");
	        	 hqlStrBuilder.append("	order by em.roomTypeId");
	             dataLis = super.query(hqlStrBuilder.toString(),new Object[] {channelType,type,channelType });
	         }

    	return dataLis;
    }
    
    /**
     * 将酒店列表和房型列表进行整合成基本酒店列表
     * add by yong.zeng 2009-12-24
     * @param hotelList
     * @param roomList
     * @return
     */
    public List<HtlHotelBase> getBaseHotelListForExMapping(List<ExMapping> hotelList,List<ExMapping> roomList){
    	List<HtlHotelBase> baseList = new ArrayList<HtlHotelBase>();
    	
    	if(null!=hotelList && hotelList.size()>0){
    		for(ExMapping hotel:hotelList){
    			HtlHotelBase hotelBase = new HtlHotelBase();
    			hotelBase.setHotelId(String.valueOf(hotel.getHotelid()));//酒店ID
    			hotelBase.setHotelCode(hotel.getHotelcode());//酒店编码
    			hotelBase.setMangoChannelBoth("M");
    			hotelBase.setHotelcodeforchannel(hotel.getHotelcodeforchannel());//渠道酒店编码
    			hotelBase.setHotelName(hotel.getHotelname());//酒店名称
    			List<ExMapping> mappingLst = getBaseRoomListForExMapping(hotel.getHotelid(),roomList);
    			for(ExMapping mapping:mappingLst){
    				HtlRoomBase roomBase = new HtlRoomBase();
    				roomBase.setRoomTypeId(mapping.getRoomTypeId());//房型ID
    				roomBase.setRoomtypecode(mapping.getRoomTypeId());//房型编码
    				roomBase.setRoomtypename(mapping.getRoomtypenameForMango());//房型名称
    				roomBase.setMangoChannelBoth("M");
    				roomBase.setRoomtypecodeforchannel(mapping.getRoomtypecodeforchannel());//渠道房型编码
    				//roomBase.setRoomtypenameforchannel(roomBase.getRoomtypenameforchannel());//渠道房型名称
    				hotelBase.getRoomBaseLst().add(roomBase);
    			}
    			baseList.add(hotelBase);
    		}
    	}
    	return baseList;
    }
    
    /**
     * 根据hotelid从房型列表获取该酒店的所有房型
     * add by yong.zeng 2009-12-24
     * @param hotelID
     * @param roomList
     * @return
     */
    public List<ExMapping> getBaseRoomListForExMapping(Long hotelID,List<ExMapping> roomList){
    	List<ExMapping> exMappingLst = new ArrayList<ExMapping>();
    	if(null!=hotelID && null!=roomList && roomList.size()>0){
    		for(ExMapping room:roomList){
    			if(room.getHotelid().longValue()==hotelID.longValue()){//指定酒店下的房型
    				exMappingLst.add(room);
    			}
    		}
    	}
    	return exMappingLst;
    }
    
    /**
     * 整合酒店所有基本房型信息
     * @param hotelList  Mango的酒店列表
     * @param hotelList_C 渠道返回的酒店列表
     */
    public List<HtlHotelBase> mergerHotelRoomMC(List<HtlHotelBase> hotelList){
    	
    	//对hotelList进行遍历，将重复的hotelcode进行合并，不重复的置标记M或C
    	List<String> hotelCodeLst = new ArrayList();
    	Map<String,HtlHotelBase> htlBaseMap = new HashMap<String,HtlHotelBase>();
    	for(HtlHotelBase hotel:hotelList){
    		HtlHotelBase hotelOld = htlBaseMap.get(hotel.getHotelcodeforchannel());//Map里的HtlHotelBase
    		if(null==hotelOld)
    		{
    			htlBaseMap.put(hotel.getHotelcodeforchannel(), hotel);
    			
    		}else{
    			if("C".equals(hotel.getMangoChannelBoth())){//供应商
    				hotelOld.setHotelcodeforchannel(hotel.getHotelcodeforchannel());
    				hotelOld.setHotelnameforchannel(hotel.getHotelnameforchannel());
    			}else if("M".equals(hotel.getMangoChannelBoth())){
    				hotelOld.setHotelId(hotel.getHotelId());
    				hotelOld.setHotelCode(hotel.getHotelCode());
    				hotelOld.setHotelName(hotel.getHotelName());
    			}
    			hotelOld.setMangoChannelBoth("MC");
    			
    			
    			//处理房型 start
    			List<HtlRoomBase> roomListTemp = new ArrayList<HtlRoomBase>();
    			roomListTemp.addAll(hotelOld.getRoomBaseLst());
    			roomListTemp.addAll(hotel.getRoomBaseLst());
    			
    			Map<String,HtlRoomBase> roomBaseMapTemp = new HashMap<String,HtlRoomBase>(); 
    			for(HtlRoomBase room:roomListTemp){
    				HtlRoomBase roomTemp = roomBaseMapTemp.get(room.getRoomtypecodeforchannel());
    				if(null==roomTemp){
    					roomBaseMapTemp.put(room.getRoomtypecodeforchannel(), room);
    					
    				}else{
    					if("C".equals(room.getMangoChannelBoth())){//供应商
    						roomTemp.setRoomtypecodeforchannel(room.getRoomtypecodeforchannel());
    						roomTemp.setRoomtypenameforchannel(room.getRoomtypenameforchannel());
    					}else if("M".equals(room.getMangoChannelBoth())){//芒果网
    						roomTemp.setRoomTypeId(room.getRoomTypeId());
    						roomTemp.setRoomtypecode(room.getRoomtypecode());
    						roomTemp.setRoomtypename(room.getRoomtypename());
    					}
    					roomTemp.setMangoChannelBoth("MC");
    				}
    			}// end for
    			List<HtlRoomBase> newRoomList = new ArrayList<HtlRoomBase>();
    			Iterator keyIter = roomBaseMapTemp.values().iterator();
    			while(keyIter.hasNext()){
    				HtlRoomBase roomtmp = (HtlRoomBase)keyIter.next();
    				newRoomList.add(roomtmp);
    			}
    			hotelOld.setRoomBaseLst(newRoomList);
    		//处理房型 end

    		}
    	}// end for
    	List<HtlHotelBase> htlLst= new ArrayList<HtlHotelBase>();
    	
    	Iterator htlKeyIter = htlBaseMap.values().iterator();
    	while(htlKeyIter.hasNext()){
    		HtlHotelBase htlBase = (HtlHotelBase)htlKeyIter.next();


    		if(null==htlBase.getHotelnameforchannel()||"".equals(htlBase.getHotelnameforchannel())){
    			htlBase.setHotelcodeforchannel("");
    		}
    		
    		//对房型进行排序
    		RoomComparator compRoom = new RoomComparator();  
  		  Collections.sort(htlBase.getRoomBaseLst(),compRoom);
    		/*if(htlBase.getMangoChannelBoth().equals("C")){
    		
    		Iterator<HtlRoomBase> roomLst = htlBase.getRoomBaseLst().iterator();
    		while(roomLst.hasNext()){
    			HtlRoomBase temp = roomLst.next();
    			log.info(temp.getRoomtypecodeforchannel()+":"+temp.getRoomtypenameforchannel()+"         ");
    		}
    		}*/
    		htlLst.add(htlBase);
    	}
    	//对酒店进行排序
		HotelComparator compHotel = new HotelComparator();  
		  Collections.sort(htlLst,compHotel);
    	return htlLst;
    }
    
    /**
     * 查询房型对应价格类型对象
     * 
     * @param hotelId
     *            酒店id
     * @param roomTypes
     *            房型Id集合
     * @return
     */
    public List<HtlPriceType> qryPriceType(long  hotelId) { 
    	
    	List<HtlPriceType> priceTypesLis = new ArrayList<HtlPriceType>();
    	priceTypesLis = super.queryByNamedQuery("queryPriceTypeByRoomType", new Object[] { hotelId });
    	
    	return priceTypesLis;
    }
    
    public List sortLstPrice(List priceLis) { 
    	HtlPrice temp = new HtlPrice() ;
    	for(int i=0;i<priceLis.size();i++){
    		for(int j=0;j<priceLis.size()-1-i;j++){
    			HtlPrice priceTemp1 = (HtlPrice)priceLis.get(j);
    			HtlPrice priceTemp2 = (HtlPrice)priceLis.get(j+1);
    			if(priceTemp1.getSalePrice() < priceTemp2.getSalePrice()){
    				temp = priceTemp1;
    				priceLis.set(j, priceTemp2);
    				priceLis.set(j+1,temp);
    				
    			}
    		}
    	}
    	
    	return  priceLis;
    }
    
    
    /**
     * 根据渠道，酒店ID，房型ID， 查询相应的Exampping 
     * @param channelType
     * @param hotelid
     * @param roomtypeid  
     * @param type  
     * @return
     */
    public List<ExMapping> getHotelRoomMapping(long channelType,long hotelid,String roomtypeid,long type) {
    	List<ExMapping> dataLis = new ArrayList<ExMapping>();
		String hqlStr = "";
		hqlStr = "from ExMapping  where channeltype=? and hotelid=? and roomTypeId=? and type=?";
		dataLis = super.query(hqlStr, new Object[] { channelType, hotelid,
				roomtypeid ,type});

		return dataLis;
    }
    
    private List splitQuotaList(List tempQuotaList){
    	for(Iterator iter = tempQuotaList.iterator();iter.hasNext();) {
    			List myQuotaList = (List)iter.next();
    			HtlQuotaCutoffDayNew quotaNew =(HtlQuotaCutoffDayNew) myQuotaList.get(0);
    			if(quotaNew.getQuotaType().equals(HotelBaseConstantBean.GENERALQUOTA)){
    				iter.remove();
    		}
    	}
    	return tempQuotaList;
    }
    
    private List removeTheSameObject(List tempQuotaList){
    		HashMap map = new HashMap(tempQuotaList.size());   
	    	for(Iterator iter = tempQuotaList.iterator();iter.hasNext();) {
				List myQuotaList = (List)iter.next();
				HtlQuotaCutoffDayNew quotaNew =(HtlQuotaCutoffDayNew) myQuotaList.get(0);
				HtlQuotaCutoffDayNew quotaNewTemp = (HtlQuotaCutoffDayNew) map   
                .get(quotaNew.getQuotaId());
				 if (quotaNewTemp == null) {   
	                    map.put(quotaNew.getQuotaId(), quotaNew);   
	                } else {   
	                	iter.remove();   
	                }   
	    	}
	    	return tempQuotaList;
    }
    
    private String composeStr(List tempQuotaList ){
    	String comQuotaCot = "";
    	for(int i =0;i<tempQuotaList.size();i++){
    		List myQuotaList = (List)tempQuotaList.get(i);
    		HtlQuotaCutoffDayNew quotaNew =(HtlQuotaCutoffDayNew) myQuotaList.get(0);
    		
    		if(quotaNew.getQuotaType().equals(HotelBaseConstantBean.GENERALQUOTA)){
    			if("" == comQuotaCot){
    				comQuotaCot =comQuotaCot+quotaNew.getCutofftime();
    			}else{
    				comQuotaCot =comQuotaCot+"<br>COT:"+quotaNew.getCutofftime();
    			}
    			
    		}
    	}
    	
    	return comQuotaCot;
    	
    }
    
    public List<HtlChannelMapInfo> queryHtlChannelMapInfoList(Long roomMappingType , int channelType){

    	return exHdlDao.queryHtlChannelMapInfoList(roomMappingType,channelType);
    }
    
    public List<HtlHotelBase> addHtlChannelMapInfoListToHotelBaseLst(List<HtlHotelBase> hotelBaseLst,List<HtlChannelMapInfo> htlChannelMapInfoList){
    	
    	Map<String,List<HtlChannelMapInfo>> htlChannelHotelMap = new HashMap();
    	//以酒店id的 mapping为key，组装map
    	for(HtlChannelMapInfo htlChannelMapInfo : htlChannelMapInfoList){
    		htlChannelHotelMap.put(htlChannelMapInfo.getMapHotelId(), new ArrayList<HtlChannelMapInfo>());
    	}
    	//把HtlChannelMapInfo对象组装到对应key里面的list中
    	for(HtlChannelMapInfo htlChannelMapInfo : htlChannelMapInfoList){
    		List<HtlChannelMapInfo> htlChannelMapList = htlChannelHotelMap.get(htlChannelMapInfo.getMapHotelId());
    		htlChannelMapList.add(htlChannelMapInfo);
    		htlChannelHotelMap.put(htlChannelMapInfo.getMapHotelId(), htlChannelMapList);
    	}
    	
    	//把数据组装成HtlHotelBase结构，并增加到hotelBaseLst中
    	HtlHotelBase htlHotelBase = null;
    	for(Map.Entry<String, List<HtlChannelMapInfo>> htlChannelHotelMapItems : htlChannelHotelMap.entrySet()){
    		List<HtlChannelMapInfo> htlChannelMapList = htlChannelHotelMapItems.getValue();
    		htlHotelBase =new HtlHotelBase();
    		if(!htlChannelMapList.isEmpty()){
    			htlHotelBase.setHotelcodeforchannel(htlChannelMapList.get(0).getMapHotelId());
    			htlHotelBase.setHotelnameforchannel(htlChannelMapList.get(0).getMapHotelName());
    			htlHotelBase.setMangoChannelBoth("C");
    			
    			List<HtlRoomBase> htlRoomBaseLis = new ArrayList();
    			HtlRoomBase htlRoomBase = null;
    			for(HtlChannelMapInfo htlChannelMap : htlChannelMapList){
    				htlRoomBase =new HtlRoomBase();
    				htlRoomBase.setMangoChannelBoth("C");
    				htlRoomBase.setRoomtypecodeforchannel(htlChannelMap.getMapRoomTypeId());
    				htlRoomBase.setRoomtypenameforchannel(htlChannelMap.getMapRoomTypeName());
    				htlRoomBaseLis.add(htlRoomBase);
    			}
    			htlHotelBase.setRoomBaseLst(htlRoomBaseLis);
    			hotelBaseLst.add(htlHotelBase);
    		}
    	}
    	return hotelBaseLst;
    }
	public void setExHdlDao(IExDao exHdlDao) {
		this.exHdlDao = exHdlDao;
	}

	public List<HtlHotel> findActiveHotel() {
		//ID=30000315 and 
		String hql="select new HtlHotel(ID,chnName,city,longitude,latitude) from HtlHotel where  active=? ";
		//return super.doquery(hql, HOTEL_ACTIVE, 0, 1000, false);
		return super.query(hql, HOTEL_ACTIVE);
	}

	public List<HtlHotel> findActiveHotelCityCode(String cityCode) {
		String hql="select new HtlHotel(ID,chnName,city,longitude,latitude) from HtlHotel where  active=? and city=?";
		return super.query(hql, new Object[] {HOTEL_ACTIVE,cityCode});
	}
	
	//查询无图酒店
    public List<HtlHotel> queryNoPicHotels(){
         return hotelPictureDao.queryNoPicHotels("selectNoPicHotels", null);
    }
    //根据酒店ID查询相应相册封面图片信息
    public List<HtlCoverPicture> queryCoverPictures(HtlHotel htlHotel){
    	List<HtlCoverPicture> htlCoverPictureList = hotelPictureDao.queryCoverPictures("selectCoverPicture", htlHotel);	
    	List<HtlCoverPicture> roomTypeCoverList = hotelPictureDao.queryCoverPictures("selectRoomTypeCoverPicture", htlHotel);	
        if(null != roomTypeCoverList && 0 !=roomTypeCoverList.size()){
        	htlCoverPictureList.add(roomTypeCoverList.get(0));
        }
		return htlCoverPictureList;
    }
    public List<HtlPictureUrl> queryPictureUrls(HtlCoverPicture htlCoverPicture){
		return hotelPictureDao.queryPictureUrl("selectPicUrl", htlCoverPicture);  
    	
    }
    public List<OrParam> queryCommonMemberCd(){
		return hotelPictureDao.queryCommonMemberCd("selectCommonMemberCd", null);
    	
    }
    /**
     * 删除艺龙酒店映射，转成自签酒店，修改htl_hotel_ext的channel
     * @param hotelId
     */
	public void elongToTradition(long hotelId) {
		super.remove("delete ExMapping m where m.hotelid=? and m.channeltype=9", new Object[]{hotelId});
		super.doUpdateBatch("update HtlHotelExt t set t.cooperateChannel='0' where t.htlHotel.ID=?", new Object[]{hotelId});
	}
	/**
	 * 删此酒店所有价格、房态、配额；价格类型；房型；
	 */
	public void traditionToElong(long hotelId,String hotelcodeforchannel,String hotelName) {
		super.remove("delete HtlPrice m where m.hotelId=?", new Object[]{hotelId});
		super.remove("delete HtlRoom m where m.hotelId=?", new Object[]{hotelId});
		super.remove("delete HtlQuotaNew m where m.hotelId=?", new Object[]{hotelId});
		super.doSqlUpdate("delete from htl_price_type t where t.room_type_id in(select r.room_type_id from htl_roomtype r where r.hotel_id="+hotelId+")");
		super.remove("delete HtlRoomType m where m.hotelID=?", new Object[]{hotelId});
		ExMapping exm = new ExMapping();
		exm.setChanneltype(9L);
		exm.setHotelid(hotelId);
		exm.setHotelcodeforchannel(hotelcodeforchannel);
		exm.setHotelname(hotelName);
		exm.setType(1L);
		exm.setCode(""+hotelId);
		exm.setCodeforchannel(hotelcodeforchannel);
		exm.setModiby("HBIZ");
		exm.setModifiername("HBIZ");
		exm.setModitime(new Date());
		exm.setIsActive("1");
		super.save(exm);
		super.doUpdateBatch("update HtlHotelExt t set t.cooperateChannel='9' where t.htlHotel.ID=?", new Object[]{hotelId});
	}
}