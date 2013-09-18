package hk.com.cts.ctcp.hotel.service;

import hk.com.cts.ctcp.hotel.constant.TxnStatusType;
import hk.com.cts.ctcp.hotel.webservice.enquiryservice.CustInfoData;
import hk.com.cts.ctcp.hotel.webservice.enquiryservice.TxnDtlData;
import hk.com.cts.ctcp.hotel.webservice.enquiryservice.TxnStatusData;
import hk.com.cts.ctcp.hotel.webservice.response.HKRoomAmtResponse;
import hk.com.cts.ctcp.hotel.webservice.response.HKRoomQtyResponse;
import hk.com.cts.ctcp.hotel.webservice.saleservice.BasicData;
import hk.com.cts.ctcp.hotel.webservice.saleservice.BeginData;
import hk.com.cts.ctcp.hotel.webservice.saleservice.CalAmtData;
import hk.com.cts.ctcp.hotel.webservice.saleservice.CustInfo;

import java.util.Date;
import java.util.List;

/**
 * 提供HOP,HBIZ的应用接口 (连接港中旅)
 * 
 * @author shizhongwen 日期:Mar 9, 2009 时间:5:18:42 PM
 */
public interface HKService {

    /**
     * 根据酒店id、入住日期、离店日来查询酒店所有房型的数量列表 * author:shizhongwen 日期:Mar 10, 2009 时间:5:48:05 PM
     * 
     * @param sHotelCode
     *            酒店
     * @param sDateFm
     *            入住日期
     * @param sDateTo
     *            离店日期
     * @param roomTypes
     *            房型
     * @return List<ClassQtyData>
     */
    public List<HKRoomQtyResponse> enqRoomQty(long hotelId, Date dateFm, Date dateTo,
    		String roomTypes);

    /**
     * 网站查配额
     * @param hotelId
     * @param dateFm
     * @param dateTo
     * @param roomTypeId
     * @return
     */
    public List<Date> enqRoomQtyForWeb(long hotelId, Date dateFm, Date dateTo,
    		long roomTypeId);
    
    /**
     * 根据酒店ID、入住日期、离店日期来查询所有房型价格的列表 author:shizhongwen 日期:Mar 9, 2009 时间:6:04:12 PM
     * 
     * @param hotelId
     *            酒店ID
     * @param dateFm
     *            入住日期
     * @param dateTo
     *            离店日期
     * @return List<ClassNationAmtData>
     */
    public List<HKRoomAmtResponse> enqRoomNationAmt(long hotelId, Date dateFm, Date dateTo, 
    		String roomTypes, String childRoomTypes);

    /**
     * 根据酒店ID、入住日期、离店日期、房型编码来查询房型价格列表 author:shizhongwen 日期:Mar 9, 2009 时间:6:11:06 PM
     * 
     * @param hotelId
     *            酒店ID
     * @param dateFm
     *            入住日期
     * @param dateTo
     *            离店日期
     * @param roomTypeId
     *            房型编码
     * @return List<NationAmtData>
     */
    public List<HKRoomAmtResponse> enqNationAmt(long hotelId, Date dateFm, Date dateTo,
        long roomTypeId);

    /**
     * 根据酒店ID、入住日期、离店日期、房型编码、子房型编码来查询房型价格列表 add by shizhongwen 时间:Mar 23, 2009 3:47:53 PM
     * 
     * @param hotelId
     * @param dateFm
     * @param dateTo
     * @param roomTypeId
     * @param childRoomTypeId
     * @return
     */
    public List<HKRoomAmtResponse> enqHKAmtByNation(long hotelId, Date dateFm, Date dateTo,
        long roomTypeId, String childRoomTypeId);

    /**
     * 交易编号，查询当前或完成交易的客户信息 author:shizhongwen 日期:Mar 9, 2009 时间:6:15:11 PM
     * 
     * @param sTxnNo
     *            交易编号
     * @return CustInfoData
     */
    public List<CustInfoData> enqCustInfo(String sTxnNo);

    /**
     * 根据交易编号来查询交易（所有交易类型）的状态 author:shizhongwen 日期:Mar 9, 2009 时间:6:18:46 PM
     * 
     * @param sTxnNo
     *            交易编号
     * @return
     * @see TxnStatusType
     */
    public TxnStatusData enqTxnStatus(String sTxnNo);

    /**
     * 根据交易编号来查询目前或完成交易的清单的内容 author:shizhongwen 日期:Mar 9, 2009 时间:6:19:33 PM
     * 
     * @param sTxnNo
     * @return
     */
    public List<TxnDtlData> enqTxnDtl(String sTxnNo);

    /**
     * 开始交易(交易之前都要调用这个操作)获得交易编号 author:shizhongwen 日期:Mar 9, 2009 时间:6:42:59 PM
     * 
     * @return
     */
    public BeginData saleBegin();

    /**
     * 根据交易编号、酒店ID、日期、房型编码、价格编码、数量来预订房间（HoldRoom） add by shizhongwen 时间:Mar 12, 2009 5:31:32 PM
     * 
     * @param sTxnNo
     *            交易编号
     * @param hotelId
     *            酒店ID
     * @param dateFm
     *            入住日期
     * @param dateTo
     *            离店日期
     * @param roomTypeId
     *            芒果房型
     * @param childRoomTypeId
     *            芒果子房型
     * @param nQty
     *            入住间数
     * @return
     */
    public BasicData holdRoom(String sTxnNo, long hotelId, Date dateFm, Date dateTo,
        long roomTypeId, long childRoomTypeId, int nQty);

    /**
     * 根据密钥、交易编号来查询当前交易的净额 author:shizhongwen 日期:Mar 10, 2009 时间:4:47:46 PM
     * 
     * @param sTxnNo
     *            交易编号
     * @return
     */
    public CalAmtData saleCalAmt(String sTxnNo);

    /**
     * 填写入住客户的信息。 author:shizhongwen 日期:Mar 10, 2009 时间:4:49:05 PM
     * 
     * @param sTxnNo
     *            交易编号
     * @param aInfo
     * @param sRmk
     *            备注
     * @return
     */
    public BasicData saleAddCustInfo(String sTxnNo, List<CustInfo> aInfo, String sRmk);

    /**
     * 承诺（确认）买卖交易 author:shizhongwen 日期:Mar 10, 2009 时间:4:52:12 PM
     * 
     * @param sTxnNo
     *            交易编号
     * @param nTotItem
     *            房间数?
     * @return
     */
    public BasicData saleCommit(String sTxnNo, int nTotItem);

    /**
     * 回滚,取消订单 author:shizhongwen 日期:Mar 10, 2009 时间:4:54:54 PM
     * 
     * @param sTxnNo
     *            交易编号
     * @return
     */
    public BasicData saleRollback(String sTxnNo);

}
