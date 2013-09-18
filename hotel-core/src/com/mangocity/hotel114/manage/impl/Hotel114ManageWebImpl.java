package com.mangocity.hotel114.manage.impl;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;

import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel114.manage.Hotel114ManageWeb;
import com.mangocity.hotelweb.persistence.HotelInfoForWeb;
import com.mangocity.hotelweb.persistence.HotelInfoForWebBean;
import com.mangocity.hotelweb.persistence.HotelPageForWebBean;
import com.mangocity.hotelweb.persistence.QueryHotelFactorageResult;
import com.mangocity.hotelweb.persistence.QueryHotelForWebBean;
import com.mangocity.hotelweb.persistence.QueryHotelForWebResult;
import com.mangocity.hotelweb.persistence.QueryHotelForWebRoomType;
import com.mangocity.hotelweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.hotelweb.persistence.QueryHotelForWebServiceIntroduction;
import com.mangocity.hotelweb.persistence.QueryPictureForWebServiceIntroduction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.log.MyLog;

/**
 */
public class Hotel114ManageWebImpl extends DAOHibernateImpl implements Hotel114ManageWeb {
	private static final MyLog log = MyLog.getLogger(Hotel114ManageWebImpl.class);
    private DAOIbatisImpl queryDao;

    private ContractManage contractManage;

    private HotelManage hotelManage;

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    /**
     * 网站查询酒店的修改实现
     * 
     * @param queryBean
     *            查询条件
     * @return 查询结果
     * @throws SQLException 
     */
    public HotelPageForWebBean queryHotelsForWeb(QueryHotelForWebBean queryBean)
    throws SQLException {
        HotelPageForWebBean hotelPageForWebBean = new HotelPageForWebBean();
        List<QueryHotelForWebResult> list = new ArrayList<QueryHotelForWebResult>();

        int pageNo = 1;
        // 当指定跳到哪一页时
        if (0 != queryBean.getPageIndex()) {
            pageNo = queryBean.getPageIndex();
        }

        // 计算周天数
        List weekNum = getDateStrList(queryBean.getInDate(), queryBean.getOutDate());
        // 计算周需要显示的行数
        int difdays = DateUtil.getDay(queryBean.getInDate(), queryBean.getOutDate());
        int weekTotal = (difdays - 1) / 7 + 1;

        // 计算显示的页面显示的宽度
        int colCount = (1 < weekTotal ? 8 : difdays) + 6;

        // 每页显示数
        int pageSize = queryBean.getPageSize();

        List<QueryHotelForWebResult> results = null;
        int totalSize = 0; // 查询结果后返回总记录数
        CallableStatement cstmt = null;
        ResultSet rs = null;
        try {
            cstmt = setQueryHotelParas(queryBean);
            cstmt.execute();
            totalSize = Long.valueOf(((BigDecimal) cstmt.getObject(17)).longValue()).intValue();
            // String sql = cstmt.getString(18);
            rs = (ResultSet) cstmt.getObject(19);
            results = getSearchResult(rs);
            cstmt.close();
        } catch (SQLException e) {
            log.error("Query hotel info error! the cause is:" + e);
            cstmt.close();
        } finally {
            if(null!=rs){
                rs.close();
            }
            if(null!=cstmt){
                cstmt.close();
            }
        }

        if (null != results) {
            // 计算总页数
            int totalIndex = 0;
            if (0 != totalSize % pageSize) {
                totalIndex = totalSize / pageSize + 1;
            } else {
                totalIndex = totalSize / pageSize;
            }
            hotelPageForWebBean.setTotalIndex(totalIndex);
            hotelPageForWebBean.setPageSize(pageSize);
            hotelPageForWebBean.setPageIndex(pageNo);

            List<QueryHotelForWebRoomType> roomTypeLis = null;
            List<QueryHotelForWebRoomType> roomTypes = null;
            List<QueryHotelForWebSaleItems> faceLis = null;
            List<QueryHotelForWebSaleItems> faceItemsLis = null;
            QueryHotelForWebResult queryHotelForWebResult = null;
            Date dt = null;
            int priceNum = 0;

            int yud = -1;
            int mf = -1;
            int roomStatus = 0;
            List<QueryHotelForWebResult> tempResultList = null;
            String rateStr = "";
            long hotelId = 0;
            long roomTypeId = 0;
            long tempHotelId = 0;
            if (0 != results.size() && null != results.get(0)) {
                tempHotelId = results.get(0).getHotelId();
            }
            boolean readFlag = false;
            for (QueryHotelForWebResult info : results) {
                if (info.getHotelId() == tempHotelId && readFlag) // 跳过同一酒店的记录
                {
                    tempHotelId = info.getHotelId();
                    continue;
                }
                readFlag = true;
                tempHotelId = info.getHotelId();
                if (null != tempResultList) // 清除前一个酒店数据
                {
                    tempResultList.clear();
                    tempResultList = null;
                }
                tempResultList = pickupUniqueHotel(info.getHotelId(), results); // 得到当前酒店ID对应的酒店数据
                hotelId = info.getHotelId();

                // 每个酒店信息
                queryHotelForWebResult = new QueryHotelForWebResult();
                queryHotelForWebResult.setHotelId(hotelId);
                queryHotelForWebResult.setCommendType(info.getCommendType());
                // 读取该酒店的币种兑换率,包括人民币，人民币对人民币汇率为1
                // if (!CurrencyBean.RMB.equals(info.getCurrency())){
                rateStr = getCurrenyRate(info.getCurrency());
                queryHotelForWebResult.setRateStr(rateStr);
                // hotelPageForWebBean.setRateStr(rateStr);
                // }
                // 每个酒店下的房型信息
                roomTypeLis = getRoomTypeInfoList(hotelId, tempResultList);

                // 房型超过2个页面需要显示房型全显示下拉按钮
                if (2 < roomTypeLis.size()) {
                    queryHotelForWebResult.setFx(roomTypeLis.size());
                }
                roomTypes = new ArrayList<QueryHotelForWebRoomType>();
                queryHotelForWebResult.setPriceId(priceNum);
                for (QueryHotelForWebRoomType queryHotelForWebRoomType : roomTypeLis) {

                    // 酒店结果中每个房型有几种付款方式
                    double price = 0;
                    roomTypeId = Long.parseLong(queryHotelForWebRoomType.getRoomTypeId());
                    price = 0;

                    // 面付方式价格明细查询
                    faceItemsLis = getSaleItemList("pay", hotelId, roomTypeId,
                        queryHotelForWebRoomType.getChildRoomTypeId(), tempResultList);
                    // 给面付list补空，方便页面显示
                    faceLis = new ArrayList<QueryHotelForWebSaleItems>();

                    // 存放面付的每天日期和价格集合StringBuilder
                    StringBuilder faceDatePriceStr = new StringBuilder();

                    mf = -1;
                    if (0 < faceItemsLis.size()) {
                        for (int k = 0; k < difdays; k++) {
                            dt = DateUtil.getDate(queryBean.getInDate(), k);
                            if (queryBean.getOutDate().after(dt)) {
                                int m = 0;
                                for (QueryHotelForWebSaleItems 
                                    queryHotelForWebSaleItems : faceItemsLis) {
                                    roomStatus = getRoomState(queryHotelForWebSaleItems
                                        .getRoomState());
                                    if (dt.equals(queryHotelForWebSaleItems.getFellowDate())) {

                                        queryHotelForWebSaleItems.setPriceId(++priceNum);
                                        // 计算币种转换后显示的价格

                                        BigDecimal b1 = new BigDecimal(Double
                                            .toString(queryHotelForWebSaleItems.getSalePrice()));

                                        BigDecimal b2 = new BigDecimal(rateStr);

                                        queryHotelForWebSaleItems.setRmbPrice(b1.multiply(b2)
                                            .doubleValue());

                                        faceLis.add(queryHotelForWebSaleItems);
                                        price += queryHotelForWebSaleItems.getSalePrice();
                                        m++;
                                        // 增加存放面付的每天日期和价格
                                        faceDatePriceStr.append(DateUtil
                                            .getWeekOfDate(queryHotelForWebSaleItems
                                                .getFellowDate()));
                                        faceDatePriceStr.append(":");
                                        faceDatePriceStr
                                            .append(DateUtil.dateToString(queryHotelForWebSaleItems
                                                .getFellowDate()));
                                        faceDatePriceStr.append(":");
                                        faceDatePriceStr.append(queryHotelForWebSaleItems
                                            .getSalePrice());
                                        faceDatePriceStr.append("/");

                                    }
                                    if (roomStatus > mf) {
                                        mf = roomStatus;
                                    }
                                }
                                if (0 == m) {
                                    faceLis.add(new QueryHotelForWebSaleItems());
                                }
                            }
                        }
                    } else {
                        for (int days = 1; days <= difdays % 7; days++) // 价格没有则补空
                        {
                            faceLis.add(new QueryHotelForWebSaleItems());
                        }
                    }
                    queryHotelForWebRoomType
                        .setFaceDatePriceStr(faceDatePriceStr.toString().trim());
                    // 该房型价格类型的面付价格
                    queryHotelForWebRoomType.setPayPrice(price);
                    price = 0;
                    if (7 < difdays) {
                        for (int k = 0; k < 7 - difdays % 7; k++) {
                            faceLis.add(new QueryHotelForWebSaleItems());
                        }
                    }
                    queryHotelForWebRoomType.setFk(0);
                    queryHotelForWebRoomType.setYud(yud);
                    queryHotelForWebRoomType.setMf(mf);
                    queryHotelForWebRoomType.setColCount(colCount);
                    queryHotelForWebRoomType.setWeekTotal(weekTotal);
                    queryHotelForWebRoomType.setWeekNum(weekNum);
                    queryHotelForWebRoomType.setRowNum(weekTotal * 2);
                    queryHotelForWebRoomType.setCurrency(info.getCurrency());
                    queryHotelForWebRoomType.setItemsList(faceLis);
                    queryHotelForWebRoomType.setItemsPrice(queryHotelForWebRoomType.getPayPrice());
                    queryHotelForWebRoomType.setPayMethod("pay");
                    queryHotelForWebRoomType.setPayStr("面付");
                    queryHotelForWebRoomType.setQuotaType(queryHotelForWebRoomType.getQuotaType());
                    roomTypes.add(queryHotelForWebRoomType);
                }
                queryHotelForWebResult.setHotelStar(info.getHotelStar());
                queryHotelForWebResult.setColCount(colCount);
                queryHotelForWebResult.setWeekTotal(weekTotal);
                queryHotelForWebResult.setWeekNum(weekNum);
                queryHotelForWebResult.setRoomTypes(roomTypes);
                queryHotelForWebResult.setHotelChnName(info.getHotelChnName());
                queryHotelForWebResult.setChnAddress(info.getChnAddress());
                queryHotelForWebResult.setHotelIntroduce(info.getHotelIntroduce());
                // 得到酒店图片
                queryHotelForWebResult.setOutPictureName(info.getOutPictureName());
                queryHotelForWebResult.setHallPictureName(info.getHallPictureName());
                queryHotelForWebResult.setRoomPictureName(info.getRoomPictureName());
                queryHotelForWebResult.setHotelLogo(info.getHotelLogo());

                // 获取3D图片数量
                queryHotelForWebResult.setSandNum(info.getSandNum());

                /*
                 * if (!CurrencyBean.RMB.equals(info.getCurrency())){ rateStr =
                 * getCurrenyRate(info.getCurrency()); queryHotelForWebResult.setRateStr(rateStr);
                 * //hotelPageForWebBean.setRateStr(rateStr); }
                 */
                queryHotelForWebResult.setCurrency(info.getCurrency());
                // hotelPageForWebBean.setRateStr(rateStr);
                queryHotelForWebResult.setEndpriceId(priceNum);
                priceNum++;
                list.add(queryHotelForWebResult);
            }
            roomTypeLis = null;
            roomTypes = null;
            faceLis = null;
            faceItemsLis = null;
            queryHotelForWebResult = null;
            dt = null;
            tempResultList = null;
        }
        // 强行设置网站币种汇率，为港币汇率（目前只考虑港币情况）
        hotelPageForWebBean.setRateStr(getCurrenyRate(CurrencyBean.HKD));

        hotelPageForWebBean.setList(list);
        results = null;
        list = null;
        return hotelPageForWebBean;
    }

    /**
     * 挑出同一酒店数据
     * 
     * @param hotelId
     * @param list
     * @return
     */
    private List<QueryHotelForWebResult> pickupUniqueHotel(long hotelId,
        List<QueryHotelForWebResult> list) {
        List<QueryHotelForWebResult> resultList = new ArrayList<QueryHotelForWebResult>();
        for (QueryHotelForWebResult info : list) {
            if (info.getHotelId() == hotelId) {
                resultList.add(info);
            }
        }
        return resultList;
    }

    /**
     * 得到房型信息
     * 
     * @param hotelId
     * @param roomTypeId
     * @param hotelResultInfo
     * @return
     */
    private List<QueryHotelForWebRoomType> getRoomTypeInfoList(long hotelId,
        List<QueryHotelForWebResult> resultInfo) {
        List<QueryHotelForWebRoomType> roomTypeList = new ArrayList<QueryHotelForWebRoomType>();
        QueryHotelForWebRoomType roomType = null;
        String tmpChildRoomTypeId = "";
        String tmpRoomTypeId = "";
        for (QueryHotelForWebResult info : resultInfo) {
            if (!tmpChildRoomTypeId.equals("")
                && tmpChildRoomTypeId.equals(info.getChildRoomTypeId())
                && tmpRoomTypeId.equals(String.valueOf(info.getRoom_type_id()))) {
                continue;
            }
            if (hotelId == info.getHotelId() && info.getPayMethod().equals("pay")) {
                roomType = new QueryHotelForWebRoomType();
                roomType.setCurrency(info.getCurrency());
                roomType.setPayMethod(info.getPayMethod());
                roomType.setChildRoomTypeId(info.getChildRoomTypeId());
                roomType.setChildRoomTypeName(info.getPriceType());
                roomType.setRoomTypeName(info.getRoomName());
                roomType.setRoomTypeId(String.valueOf(info.getRoom_type_id()));
                roomType.setRoomPrice(info.getSalesRoomPrice());
                roomType.setQuotaType(info.getQuotaType());
                roomType.setBreakfastType(info.getBreakfastType());
                roomType.setBreakfastNum(info.getBreakfastNum());
                roomTypeList.add(roomType);
                tmpChildRoomTypeId = info.getChildRoomTypeId();
                tmpRoomTypeId = roomType.getRoomTypeId();
            }
        }
        return roomTypeList;
    }

    /**
     * 得到房态和价格
     * 
     * @param hotelId
     * @param roomTypeId
     * @param childRoomTypeId
     * @param list
     * @return
     */
    private List<QueryHotelForWebSaleItems> getSaleItemList(String payMethod, long hotelId,
        long roomTypeId, String childRoomTypeId, List<QueryHotelForWebResult> list) {
        List<QueryHotelForWebSaleItems> saleItems = new ArrayList<QueryHotelForWebSaleItems>();
        // QueryHotelForWebSaleItems saleItem = null;
        for (QueryHotelForWebResult info : list) {
            if (hotelId == info.getHotelId() && info.getPayMethod().equals(payMethod)
                && childRoomTypeId.equals(info.getChildRoomTypeId())
                && roomTypeId == info.getRoom_type_id()) {
                QueryHotelForWebSaleItems saleItem = new QueryHotelForWebSaleItems();
                saleItem.setRoomState(info.getRoom_state());
                saleItem.setFellowDate(DateUtil.toDateByFormat(info.getAble_sale_date(),
                    "yyyy-MM-dd"));
                saleItem.setSalePrice(info.getSalesPrice());
                saleItems.add(saleItem);
            }
        }
        return saleItems;
    }

    /**
     * 解析房态
     * 
     * @param roomState
     * @return
     */
    private int getRoomState(String roomState) {
        if (null == roomState || 0 == roomState.length()) {
            return 0;
        }
        // 解析房态1:0/2:-1/3:4
        String[] roomArray = roomState.split("/");
        int roomStatus = 0;
        int temp = 0;
        for (int i = 0; i < roomArray.length; i++) {
            String status = roomArray[i].substring(roomArray[i].indexOf(":") + 1);
            try {
                roomStatus = Integer.parseInt(status);
            } catch (NumberFormatException ne) {
                return temp;
            }
            if (roomStatus > temp) {
                temp = roomStatus;
            }
        }
        return temp;
    }

    /**
     * 获取汇率
     * 
     * @param curreny
     * @return
     */
    private String getCurrenyRate(String curreny) {
        String rate = "1";
        Map ma = hotelManage.getExchangeRateMap();
        if (null == ma) {
            return rate;
        }
        rate = String.valueOf(ma.get(curreny));
        return rate;
    }

    /**
     * 设置查询存储过程参数
     * 
     * @param condition
     * @return
     * @throws SQLException
     */
    private CallableStatement setQueryHotelParas(QueryHotelForWebBean condition)
        throws SQLException {
        CallableStatement cstmt = null;
        try {
            String procedureName = "{call PKGHOTELQUERY_3_0.getHotelWebList(?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?)}";
            cstmt = super.getCurrentSession().connection().prepareCall(
                procedureName);
            cstmt.setString(1, "01"); // sales_channel is 114
            cstmt.setString(2, condition.getCityId()); // p_cityid VARCHAR2,
            // --城市id
            cstmt.setString(3, condition.getCityName()); // p_cityname
            // VARCHAR2, --城市名称
            cstmt.setString(4, condition.getDistrict()); // p_district
            // VARCHAR2, --城区
            cstmt.setString(5, condition.getBizZone()); // p_bizzone VARCHAR2,
            // --商业区
            cstmt.setLong(6, condition.getHotelId()); // p_hotelID

            cstmt.setString(7, condition.getHotelName()); // p_hotelchnname
            // --酒店中文地址
            String star = condition.getHotelStar();
            if (null != star && 0 < star.length()
                && star.substring(star.length() - 1, star.length()).equals(",")) {
                condition.setHotelStar(star.substring(0, star.length() - 1));
            }
            cstmt.setString(8, condition.getHotelStar()); // p_hotelstar
            // VARCHAR2, --酒店星级
            cstmt.setString(9, DateUtil.dateToString(condition.getInDate())); // p_begindate
            // VARCHAR2
            // --开始日期(YYYY-MM-DD)
            cstmt.setString(10, DateUtil.dateToString(condition.getOutDate()));// p_enddate

            cstmt.setString(11, condition.getMinPrice()); // p_minprice
            // NUMERIC,
            cstmt.setString(12, condition.getMaxPrice()); // p_maxprice

            cstmt.setInt(13, condition.getPageSize()); // p_pagesize NUMERIC,
            // --每页记录数, 默认为10
            cstmt.setInt(14, condition.getPageIndex()); // p_pageno NUMERIC,
            // --当前页, 默认为1
            cstmt.setInt(15, condition.getQrymethod()); // p_sortway NUMERIC,
            // --排序方式 1:芒果网推荐 2:价格

            cstmt.registerOutParameter(16, OracleTypes.NUMERIC); // return_pagecount
            // OUT
            // NUMERIC,
            cstmt.registerOutParameter(17, OracleTypes.NUMERIC); // return_recordcount
            // out
            // numeric,
            cstmt.registerOutParameter(18, OracleTypes.VARCHAR); // return_sql
            // OUT
            // VARCHAR2
            cstmt.registerOutParameter(19, OracleTypes.CURSOR); // return_list
            // out
            // outHotelList
        } catch (SQLException sqle) {
            log.error("Get the database connection error,error=" + sqle);
            throw sqle;
        }
        return cstmt;
    }

    /**
     * @param rs
     * @return
     * @throws SQLException
     */
    private List<QueryHotelForWebResult> getSearchResult(ResultSet rs) throws SQLException {
        List<QueryHotelForWebResult> list = new ArrayList<QueryHotelForWebResult>();
        QueryHotelForWebResult info = null;
        try {
            while (rs.next()) {
                info = new QueryHotelForWebResult();
                // info.setLowestPrice(rs.getFloat("LowestPrice"));
                info.setHotelId(rs.getLong("HotelId"));
                info.setHotelChnName(rs.getString("HotelChnName"));
                info.setHotelEngName(rs.getString("HotelEngName"));
                info.setHotelStar(rs.getString("HotelStar"));
                info.setHotelType(rs.getString("HotelType"));
                info.setChnAddress(rs.getString("ChnAddress"));
                info.setHotelIntroduce(rs.getString("HotelChnIntroduce"));
                // info.setChnIntroduce(rs.getString("HotelChnIntroduce"));
                info.setCommendType(rs.getString("Hotel_comm_type"));
                info.setClueInfo(rs.getString("ClueInfo"));
                info.setBizZone(rs.getString("BIZZONE"));
                info.setCity(rs.getString("City"));
                // --价格类型
                info.setChildRoomTypeId(rs.getString("ChildRoomTypeId"));
                // info.setComm_level(rs.getInt("Comm_level"));
                info.setRoom_type_id(rs.getLong("Room_type_id"));
                info.setRoomName(rs.getString("Room_name"));
                info.setSalesPrice(rs.getDouble("SalesPrice"));
                info.setPriceType(rs.getString("Price_type"));
                // info.setClose_flag(rs.getString("close_flag"));
                // info.setReason(rs.getString("Reason"));
                //
                // // --配额类型及支付方式
                info.setQuotaType(rs.getString("Quota_Type"));
                info.setPayMethod(rs.getString("Pay_Method"));
                //
                // // --房间配额 价格
                // info.setAvail_qty(rs.getInt("Avail_qty"));
                info.setAble_sale_date(rs.getString("Able_sale_date"));
                info.setCurrency(rs.getString("Currency"));
                info.setBreakfastType(rs.getInt("Inc_breakfast_type"));
                info.setBreakfastNum(rs.getInt("Inc_breakfast_number"));
                info.setRoom_state(rs.getString("Room_state"));
                // info.setQuota_batch_id(rs.getLong("Quota_batch_id"));
                // info.setQuota_pattern(rs.getString("Quota_pattern"));
                info.setSalesRoomPrice(rs.getDouble("Salesroom_price"));
                // info.setHotel_currency(rs.getString("Hotel_currency"));
                // info.setHotel_balanceMethod(rs.getString("Hotel_balanceMethod"));
                // info.setPictureType(rs.getString("PictureType"));
                info.setOutPictureName(rs.getString("OutPictureName"));
                info.setHallPictureName(rs.getString("RoomPictureName"));
                info.setPictureName(rs.getString("HallPictureName"));
                info.setSandNum(rs.getInt("NumSand"));
                info.setHotelLogo(rs.getString("hotelLogo"));
                list.add(info);
            }
        } catch (SQLException e) {
            log.error("Get the database data error,error=" + e);
            throw e;
        }
        return list;
    }

    /**
     * 对于日期进行格式描述
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    @SuppressWarnings("deprecation")
    protected List<String> getDateStrList(Date startDate, Date endDate) {

        int difdays = DateUtil.getDay(startDate, endDate);

        List<String> dateStrList = new ArrayList<String>();

        difdays = 7 <= difdays ? 7 : difdays;
        for (int i = 0; i < difdays; i++) {
            Date reservationDate = DateUtil.getDate(startDate, i);
            int week = reservationDate.getDay();
            String dateStr = "";
            switch (week) {
            case 1:
                dateStr = "周一";
                break;
            case 2:
                dateStr = "周二";
                break;
            case 3:
                dateStr = "周三";
                break;
            case 4:
                dateStr = "周四";
                break;
            case 5:
                dateStr = "周五";
                break;
            case 6:
                dateStr = "周六";
                break;
            case 0:
                dateStr = "周日";
                break;

            }

            dateStrList.add(dateStr);
        }

        return dateStrList;
    }

    public List<QueryHotelForWebSaleItems> queryPriceForWeb(long childRoomTypeId, Date beginDate,
        Date endDate, double minPrice, double maxPrice, String payMethod, String quotaType) {
        List<QueryHotelForWebSaleItems> lis = new ArrayList<QueryHotelForWebSaleItems>();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("beginDate", beginDate);
        params.put("endDate", endDate);
        params.put("minPrice", minPrice);
        params.put("maxPrice", maxPrice);
        params.put("childRoomTypeId", childRoomTypeId);
        params.put("payMethod", payMethod);
        params.put("quotaType", quotaType);
        List resultLis = queryDao.queryForList("qryWebSaleItems", params);
        int beginWeekNum = DateUtil.getWeekOfDate(beginDate);
        int endWeekNum = DateUtil.getWeekOfDate(endDate);
        if (0 < 7 - beginWeekNum) {
            for (int i = 0; i < beginWeekNum - 1; i++) {
                QueryHotelForWebSaleItems queryHotelForWebSaleItems = 
                    new QueryHotelForWebSaleItems();
                lis.add(queryHotelForWebSaleItems);
            }
        }
        for (int i = 0; i < resultLis.size(); i++) {
            QueryHotelForWebSaleItems queryHotelForWebSaleItems =
                (QueryHotelForWebSaleItems) resultLis
                .get(i);
            lis.add(queryHotelForWebSaleItems);
        }
        if (0 < 8 - endWeekNum) {
            for (int i = 0; i < 8 - endWeekNum; i++) {
                QueryHotelForWebSaleItems queryHotelForWebSaleItems =
                    new QueryHotelForWebSaleItems();
                lis.add(queryHotelForWebSaleItems);
            }
        }

        return lis;
    }

    public QueryHotelForWebServiceIntroduction queryServiceIntroductionForWeb(long hotelId) {
        QueryHotelForWebServiceIntroduction sIn = new QueryHotelForWebServiceIntroduction();
        Map<String, Object> params = new HashMap<String, Object>();
        sIn = (QueryHotelForWebServiceIntroduction) queryDao.queryForObject(
            "qryWebServiceIntroduction", hotelId);
        List pictureInfos = new ArrayList();
        params.put("hotelId", hotelId);
        pictureInfos = queryDao.queryForList("qryPictureWebServiceIntroduction", params);
        if (null != pictureInfos && null != sIn) {
            sIn.setPictureInfos(pictureInfos);
        }
        return sIn;
    }

    public DAOIbatisImpl getQueryDao() {
        return queryDao;
    }

    public void setQueryDao(DAOIbatisImpl queryDao) {
        this.queryDao = queryDao;
    }

    /*
     * public HotelInfoForWebBean queryHotelInfoBeanForWeb(long hotelId, Date beginDate, Date
     * endDate) {
     * 
     * HotelInfoForWebBean hotelInfoForWebBean = new HotelInfoForWebBean(); //计算周天数 List weekNum =
     * getDateStrList(beginDate,endDate);
     * 
     * //计算周需要显示的行数 int difdays = DateUtil.getDay(beginDate,endDate); int weekTotal = (difdays - 1)
     * / 7 + 1;
     * 
     * //计算显示的页面显示的宽度 int colCount = (weekTotal > 1 ?8: difdays)+4; int avgWidth =
     * (int)100/colCount; String avgWidthStr = " width = "+avgWidth+"%";
     * 
     * //获取价格类型 Map<String,Object> roomTypeMap = new HashMap<String,Object>();
     * 
     * roomTypeMap.put("hotelId", hotelId); roomTypeMap.put("beginDate", beginDate);
     * roomTypeMap.put("endDate", endDate);
     * 
     * List roomTypeLis = queryDao.queryForList("qryWebRoomType", roomTypeMap);
     * List<QueryHotelForWebRoomType> roomTypes = new ArrayList<QueryHotelForWebRoomType>(); double
     * price =0; for(int i=0; i<roomTypeLis.size();i++){ QueryHotelForWebRoomType
     * queryHotelForWebRoomType = (QueryHotelForWebRoomType)roomTypeLis.get(i);
     * 
     * //面付方式价格明细查询 roomTypeMap.put("payMethod", "pay"); roomTypeMap.put("childRoomTypeId",
     * queryHotelForWebRoomType.getChildRoomTypeId()); roomTypeMap.put("quotaType",
     * queryHotelForWebRoomType.getQuotaType()); List faceItemsLis =
     * queryDao.queryForList("qryWebSaleItems", roomTypeMap); //给面付list补空，方便页面显示
     * List<QueryHotelForWebSaleItems> faceLis = new ArrayList<QueryHotelForWebSaleItems>();
     * 
     * int mf=-1; //int fafk=0; if (faceItemsLis.size()>0){ //fafk=2; for (int k=0; k<difdays; k++){
     * Date dt = DateUtil.getDate(beginDate, k); if (endDate.after(dt)){ int m=0; for(int
     * z=0;z<faceItemsLis.size();z++){ QueryHotelForWebSaleItems queryHotelForWebSaleItems =
     * (QueryHotelForWebSaleItems)faceItemsLis.get(z);
     * queryHotelForWebRoomType.setRoomPrice(queryHotelForWebSaleItems.getSalesRoomPrice()); if
     * (dt.equals(queryHotelForWebSaleItems.getFellowDate())){
     * faceLis.add(queryHotelForWebSaleItems); price += queryHotelForWebSaleItems.getSalePrice();
     * m++; } if(queryHotelForWebSaleItems.getRoomState()!=null &&
     * Integer.valueOf(queryHotelForWebSaleItems.getRoomState()).intValue()>mf){ mf =
     * Integer.valueOf(queryHotelForWebSaleItems.getRoomState()).intValue(); } } if(m==0){
     * QueryHotelForWebSaleItems queryHotelForWebSaleItems = new QueryHotelForWebSaleItems();
     * faceLis.add(queryHotelForWebSaleItems); } } } } //该房型价格类型的面付价格
     * queryHotelForWebRoomType.setPayPrice(price); price = 0; if(difdays>7){ for(int
     * k=0;k<7-difdays%7;k++){ QueryHotelForWebSaleItems queryHotelForWebSaleItems = new
     * QueryHotelForWebSaleItems(); if (faceLis.size()>0) faceLis.add(queryHotelForWebSaleItems); }
     * } queryHotelForWebRoomType.setYud(-1); queryHotelForWebRoomType.setMf(mf);
     * queryHotelForWebRoomType.setColCount(colCount);
     * queryHotelForWebRoomType.setWeekTotal(weekTotal);
     * queryHotelForWebRoomType.setAvgWidthStr(avgWidthStr);
     * queryHotelForWebRoomType.setWeekNum(weekNum);
     * queryHotelForWebRoomType.setRowNum(weekTotal*2);
     * queryHotelForWebRoomType.setFaceItems(faceLis);
     * 
     * queryHotelForWebRoomType.setWeekNum(weekNum);
     * queryHotelForWebRoomType.setWeekTotal(weekTotal); roomTypes.add(queryHotelForWebRoomType);
     * 
     * } hotelInfoForWebBean.setPriceType(roomTypes); //查询酒店信息 HotelInfoForWebBean bean =
     * (HotelInfoForWebBean)queryDao.queryForObject("qryHotelInfoForWeb", roomTypeMap);
     * hotelInfoForWebBean.setHotelId(bean.getHotelId());
     * hotelInfoForWebBean.setCheckInTime(bean.getCheckInTime());
     * hotelInfoForWebBean.setCheckOutTime(bean.getCheckOutTime());
     * hotelInfoForWebBean.setChnAddress(bean.getChnAddress());
     * hotelInfoForWebBean.setChnName(bean.getChnName());
     * hotelInfoForWebBean.setCreditCard(bean.getCreditCard());
     * hotelInfoForWebBean.setHotelIntroduce(bean.getHotelIntroduce());
     * hotelInfoForWebBean.setHotelStar(bean.getHotelStar());
     * hotelInfoForWebBean.setPictureName(bean.getPictureName());
     * hotelInfoForWebBean.setTelephone(bean.getTelephone());
     * 
     * return hotelInfoForWebBean; }
     */

    public HotelInfoForWebBean queryHotelInfoBeanForWeb(
        QueryHotelForWebBean queryBean) throws SQLException {
        HotelInfoForWebBean hotelInfoForWebBean = new HotelInfoForWebBean();
        List<QueryHotelForWebResult> list = new ArrayList<QueryHotelForWebResult>();

        // 计算周天数
        List weekNum = getDateStrList(queryBean.getInDate(), queryBean.getOutDate());
        // 计算周需要显示的行数
        int difdays = DateUtil.getDay(queryBean.getInDate(), queryBean.getOutDate());
        int weekTotal = (difdays - 1) / 7 + 1;

        List<QueryHotelForWebResult> results = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        try {
            cstmt = setQueryHotelParas(queryBean);
            cstmt.execute();
            // String sql = cstmt.getString(17);
            rs = (ResultSet) cstmt.getObject(19);
            results = getSearchResult(rs);
            cstmt.close();
        } catch (SQLException e) {
            log.error("Query hotel info error! the cause is:" + e);
            cstmt.close();
        } finally {
            if(null != rs){
                rs.close();
            }
            if(null!=cstmt){
                cstmt.close();
            }
        }

        List<QueryHotelForWebRoomType> roomTypes = null;
        if (null != results) {
            List<QueryHotelForWebRoomType> roomTypeLis = null;
            List<QueryHotelForWebSaleItems> saleItemsLis = null;
            List<QueryHotelForWebSaleItems> saleLis = null;
            List<QueryHotelForWebSaleItems> faceLis = null;
            List<QueryHotelForWebSaleItems> faceItemsLis = null;
            QueryHotelForWebResult queryHotelForWebResult = null;
            Date dt = null;
            int priceNum = 0;

            int yud = -1;
            int safk = 0;
            int mf = -1;
            int fafk = 0;

            int roomStatus = 0;
            List<QueryHotelForWebResult> tempResultList = null;
            long hotelId = 0;
            long roomTypeId = 0;
            long tempHotelId = 0;
            if (0 != results.size() && null != results.get(0)) {
                tempHotelId = results.get(0).getHotelId();
            }
            boolean readFlag = false;
            for (QueryHotelForWebResult info : results) {
                if (info.getHotelId() == tempHotelId && readFlag) // 跳过同一酒店的记录
                {
                    tempHotelId = info.getHotelId();
                    continue;
                }
                readFlag = true;
                tempHotelId = info.getHotelId();
                if (null != tempResultList) // 清除前一个酒店数据
                {
                    tempResultList.clear();
                    tempResultList = null;
                }
                tempResultList = pickupUniqueHotel(info.getHotelId(), results); // 得到当前酒店ID对应的酒店数据
                hotelId = info.getHotelId();

                // 每个酒店信息
                queryHotelForWebResult = new QueryHotelForWebResult();
                queryHotelForWebResult.setHotelId(hotelId);
                queryHotelForWebResult.setCommendType(info.getCommendType());

                // 每个酒店下的房型信息
                roomTypeLis = getRoomTypeInfoList(hotelId, tempResultList);

                // 房型超过2个页面需要显示房型全显示下拉按钮
                if (2 < roomTypeLis.size()) {
                    queryHotelForWebResult.setFx(roomTypeLis.size());
                }
                roomTypes = new ArrayList<QueryHotelForWebRoomType>();
                queryHotelForWebResult.setPriceId(priceNum);
                for (QueryHotelForWebRoomType queryHotelForWebRoomType : roomTypeLis) {

                    // 存放面付的每天日期和价格集合StringBuilder
                    StringBuilder faceDatePriceStr = new StringBuilder();

                    // 酒店结果中每个房型有几种付款方式
                    int fk = 0;
                    double price = 0;
                    roomTypeId = Long.parseLong(queryHotelForWebRoomType.getRoomTypeId());
                    // 预付方式价格明细查询
                    saleItemsLis = getSaleItemList("pre_pay", hotelId, roomTypeId,
                        queryHotelForWebRoomType.getChildRoomTypeId(), tempResultList);

                    // 给预付list补空，方便页面显示
                    saleLis = new ArrayList<QueryHotelForWebSaleItems>();
                    yud = -1;
                    safk = 0;
                    if (0 < saleItemsLis.size()) {
                        safk = 1;
                        for (int k = 0; k < difdays; k++) {
                            dt = DateUtil.getDate(queryBean.getInDate(), k);
                            if (queryBean.getOutDate().after(dt)) {
                                int m = 0;
                                for (QueryHotelForWebSaleItems
                                    queryHotelForWebSaleItems : saleItemsLis) {
                                    roomStatus = getRoomState(queryHotelForWebSaleItems
                                        .getRoomState());

                                    if (dt.equals(queryHotelForWebSaleItems.getFellowDate())) {
                                        queryHotelForWebSaleItems.setPriceId(++priceNum);
                                        saleLis.add(queryHotelForWebSaleItems);
                                        price += queryHotelForWebSaleItems.getSalePrice();
                                        m++;
                                    }
                                    if (roomStatus > yud) {
                                        yud = roomStatus;
                                    }
                                }
                                if (0 == m) {
                                    saleLis.add(new QueryHotelForWebSaleItems());
                                    queryHotelForWebRoomType.setNoOrder(true);
                                }
                            }
                        }
                    }

                    // 该房型价格类型的预付价格
                    queryHotelForWebRoomType.setPrepayPrice(price);
                    // 门市价
                    // queryHotelForWebRoomType.setRoomPrice(info.getSalesRoomPrice());
                    price = 0;

                    // 面付方式价格明细查询
                    faceItemsLis = getSaleItemList("pay", hotelId, roomTypeId,
                        queryHotelForWebRoomType.getChildRoomTypeId(), tempResultList);
                    // 给面付list补空，方便页面显示
                    faceLis = new ArrayList<QueryHotelForWebSaleItems>();

                    mf = -1;
                    fafk = 0;
                    if (0 < faceItemsLis.size()) {
                        fafk = 2;
                        for (int k = 0; k < difdays; k++) {
                            dt = DateUtil.getDate(queryBean.getInDate(), k);
                            if (queryBean.getOutDate().after(dt)) {
                                int m = 0;
                                for (QueryHotelForWebSaleItems 
                                    queryHotelForWebSaleItems : faceItemsLis) {
                                    roomStatus = getRoomState(queryHotelForWebSaleItems
                                        .getRoomState());
                                    if (dt.equals(queryHotelForWebSaleItems.getFellowDate())) {
                                        queryHotelForWebSaleItems.setPriceId(++priceNum);
                                        faceLis.add(queryHotelForWebSaleItems);
                                        price += queryHotelForWebSaleItems.getSalePrice();
                                        m++;

                                        // 增加存放面付的每天日期和价格
                                        faceDatePriceStr.append(DateUtil
                                            .getWeekOfDate(queryHotelForWebSaleItems
                                                .getFellowDate()));
                                        faceDatePriceStr.append(":");
                                        faceDatePriceStr
                                            .append(DateUtil.dateToString(queryHotelForWebSaleItems
                                                .getFellowDate()));
                                        faceDatePriceStr.append(":");
                                        faceDatePriceStr.append(queryHotelForWebSaleItems
                                            .getSalePrice());
                                        faceDatePriceStr.append("/");
                                    }
                                    if (roomStatus > mf) {
                                        mf = roomStatus;
                                    }
                                }
                                if (0 == m) {
                                    faceLis.add(new QueryHotelForWebSaleItems());
                                    queryHotelForWebRoomType.setNoOrder(true);
                                }
                            }
                        }
                    }
                    queryHotelForWebRoomType
                        .setFaceDatePriceStr(faceDatePriceStr.toString().trim());
                    // 该房型价格类型的面付价格
                    queryHotelForWebRoomType.setPayPrice(price);
                    price = 0;
                    if (7 < difdays) {
                        for (int k = 0; k < 7 - difdays % 7; k++) {
                            saleLis.add(new QueryHotelForWebSaleItems());
                            faceLis.add(new QueryHotelForWebSaleItems());
                        }
                    }
                    fk = fafk + safk;
                    queryHotelForWebRoomType.setFk(fk);
                    queryHotelForWebRoomType.setYud(yud);
                    queryHotelForWebRoomType.setMf(mf);
                    queryHotelForWebRoomType.setWeekTotal(weekTotal);
                    queryHotelForWebRoomType.setWeekNum(weekNum);
                    queryHotelForWebRoomType.setRowNum(weekTotal * 2);
                    queryHotelForWebRoomType.setCurrency(info.getCurrency());
                    queryHotelForWebRoomType.setSaleItems(saleLis);
                    queryHotelForWebRoomType.setFaceItems(faceLis);
                    roomTypes.add(queryHotelForWebRoomType);
                }

                list.add(queryHotelForWebResult);
            }
        }
        hotelInfoForWebBean.setPriceType(roomTypes);
        // 获取价格类型
        Map<String, Object> roomTypeMap = new HashMap<String, Object>();

        roomTypeMap.put("hotelId", queryBean.getHotelId());
        roomTypeMap.put("beginDate", queryBean.getInDate());
        roomTypeMap.put("endDate", queryBean.getOutDate());
        // 查询酒店信息
        HotelInfoForWebBean bean = (HotelInfoForWebBean) queryDao.queryForObject(
            "qryHotelInfoForWeb", roomTypeMap);
        if (null != bean) {
            hotelInfoForWebBean.setCheckInTime(bean.getCheckInTime());
            hotelInfoForWebBean.setCheckOutTime(bean.getCheckOutTime());
            hotelInfoForWebBean.setChnAddress(bean.getChnAddress());
            hotelInfoForWebBean.setChnName(bean.getChnName());
            hotelInfoForWebBean.setCreditCard(bean.getCreditCard());
            hotelInfoForWebBean.setHotelIntroduce(bean.getHotelIntroduce());
            hotelInfoForWebBean.setHotelStar(bean.getHotelStar());
            hotelInfoForWebBean.setPictureName(bean.getPictureName());
            hotelInfoForWebBean.setTelephone(bean.getTelephone());
        }
        results = null;
        list = null;
        return hotelInfoForWebBean;
    }

    public HotelInfoForWeb queryHotelInfoForWeb(long hotelId) {
        HotelInfoForWeb hotelInfo = new HotelInfoForWeb();
        Map<String, Object> params = new HashMap<String, Object>();
        List pictureInfos = new ArrayList();
        params.put("hotelId", hotelId);
        // 得到酒店基本信息
        HotelInfoForWebBean bean = (HotelInfoForWebBean) queryDao.queryForObject(
            "qryHotelInfoForWeb", params);
        // 得到酒店图片
        pictureInfos = queryDao.queryForList("qryPictureWebServiceIntroduction", params);
        // 组装
        hotelInfo.setHotelId(hotelId);
        hotelInfo.setHotelName(bean.getChnName());
        hotelInfo.setCommendType(bean.getCommendType());
        hotelInfo.setStarType(bean.getHotelStar());
        hotelInfo.setHotelStar(bean.getHotelStar());
        hotelInfo.setChnAddress(bean.getChnAddress());
        hotelInfo.setHotelIntroduce(bean.getHotelIntroduce());
        // 图片
        for (int i = 0; i < pictureInfos.size(); i++) {
            QueryPictureForWebServiceIntroduction picture = 
                (QueryPictureForWebServiceIntroduction) pictureInfos
                .get(i);
            if (picture.getPictureType().equals("0")) {
                hotelInfo.setOutPictureName(picture.getPictureName());
            } else if (picture.getPictureType().equals("1")) {
                hotelInfo.setHallPictureName(picture.getPictureName());
            } else {
                hotelInfo.setRoomPictureName(picture.getPictureName());
            }
        }
        return hotelInfo;
    }

    // add by lidajun 2008-03-26 for 增加渠道供应商 begin
    public QueryHotelFactorageResult queryHotelFactorageForWeb(long memberId) {
        QueryHotelFactorageResult facrotageInfo = new QueryHotelFactorageResult();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("memberId", memberId);
        // 得到渠道供应商信息
        facrotageInfo = (QueryHotelFactorageResult) queryDao.queryForObject(
            "qryHotelFactorageResult", params);
        return facrotageInfo;
    }

    // add by lidajun 2008-03-26 for 增加渠道供应商 end

    public ContractManage getContractManage() {
        return contractManage;
    }

    public void setContractManage(ContractManage contractManage) {
        this.contractManage = contractManage;
    }

}
