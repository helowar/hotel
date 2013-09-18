package com.mangocity.hotel.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mangocity.hdl.hotel.dto.AddExRoomOrderRequest;
import com.mangocity.hdl.hotel.dto.MGExOrder;
import com.mangocity.hdl.hotel.dto.MGExOrderItem;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.persistence.OrRemark;
import com.mangocity.hotel.order.service.CheckOrderService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.hotel.constant.PayMethod;

/**
 * CheckOrder 业务逻辑处理
 * 
 * @author:shizhongwen 创建日期:Feb 6, 2009,7:53:17 PM 描述：
 */
public class CheckOrderServiceImpl implements CheckOrderService {

//    /**
//     * HDL webservice服务
//     */
//    private IHDLService hdlService;

    // 销售价格
    private Map datemap_sale = new HashMap();

    // 底价
    private Map datemap_base = new HashMap();

    /**
     * 功能:将order和 params的数据封装到 AddExRoomOrderRequest 中 add by shizhongwen 时间:Feb 5, 2009 11:08:14 AM
     * 
     * @param addExRoomOrderRequest
     * @param order
     *            订单
     * @param params
     *            页面传过来的参数集
     * @param sum
     *            AddExRoomOrderRequest
     * @return
     */
    public AddExRoomOrderRequest encapsulationData(AddExRoomOrderRequest addExRoomOrderRequest,
        OrOrder order, Map params) {
        // 页面传过来的总价
        String sum = null;
        // AddExRoomOrderRequest add by shizhongwen 2009-2-1 新增订单的请求类
        addExRoomOrderRequest.setChannelType(order.getChannel());
        addExRoomOrderRequest.setHotelId(order.getHotelId());
        addExRoomOrderRequest.setChainCode(null);

        // AddExRoomOrderRequest add by shizhongwen 2009-2-1 类中的属性MGExOrder ,取得订单的信息给MGExOrder赋值
        sum = String.valueOf(order.getSumRmb());
        MGExOrder exRoomOrder = new MGExOrder();
        exRoomOrder.setCheckindate(DateUtil.toStringByFormat(order.getCheckinDate(), "yyyy-MM-dd"));
        exRoomOrder.setCheckoutdate(DateUtil
            .toStringByFormat(order.getCheckoutDate(), "yyyy-MM-dd"));
        exRoomOrder.setRoomquantity(order.getRoomQuantity());
        exRoomOrder.setPaymethod(order.getPayMethod());
        exRoomOrder.setCurrency(order.getPaymentCurrency());
        exRoomOrder.setExchangerate(Double.valueOf(order.getRateId()).floatValue());
        exRoomOrder.setArrivaltime(order.getArrivalTime()); // 最早到店时间
        exRoomOrder.setLatestarrivaltime(order.getLatestArrivalTime()); // 最晚到店时间
        exRoomOrder.setNosmoking(null);

        // 备注为空的处理 add by chenkeming@2009-04-02
        OrRemark remark = order.getRemark();
        if (null == remark) {
            remark = new OrRemark();
            order.setRemark(remark);
        }
        exRoomOrder.setHotelnotes(order.getRemark().getHotelRemark()); // 给酒店的备注
        exRoomOrder.setSpecialrequest(order.getSpecialRequest()); // 客人特殊要求
        exRoomOrder.setLinkman(order.getLinkMan()); // 联系人
        exRoomOrder.setPricetypecode(order.getChildRoomTypeId().intValue()); // 子房型
        exRoomOrder.setTitle(order.getTitle());// 联系人称呼--目前用作表示联系人性别<br> 'M' : 男<br> , 'F' : 女
        exRoomOrder.setMobile(order.getMobile()); // 手机
        exRoomOrder.setTelephone(order.getTelephone()); // 电话号码
        exRoomOrder.setCustomerfax(order.getCustomerFax()); // 传真
        exRoomOrder.setEmail(order.getEmail());
        exRoomOrder.setArrivaltraffic(order.getArrivalTraffic());// 去酒店坐什么交通工具（如飞机）
        exRoomOrder.setFlight(order.getFlight()); // 车次（航班）
        exRoomOrder.setFellownames(order.getFellowNames());// 中英文，包括所有入住人。便于查询
        exRoomOrder.setAdultnum(order.getFellowList().size()); // 和OrFellowInfo关联
        exRoomOrder.setPaysatus(String.valueOf(order.getPayState())); // 付款状态
        exRoomOrder.setOrderstate(String.valueOf(order.getOrderState()));
        // 订单状态（前台订单未提交，已提交订单，已提交中台，中台处理完毕，//
        //已开始日审，日审完毕，已日结
        exRoomOrder.setHotelid(order.getHotelId());
        exRoomOrder.setHotelname(order.getHotelName());
        exRoomOrder.setRoomtypeid(order.getRoomTypeId().intValue());
        exRoomOrder.setRoomtypecode(order.getRoomTypeId().intValue());
        exRoomOrder.setRoomtypename(order.getRoomTypeName());// 房型名称
        exRoomOrder.setBedtype(order.getBedType());
        exRoomOrder.setIsguarantee(String.valueOf((order.getIsCreditAssured())));// 是否需要信用卡担保
        exRoomOrder.setGuaranteetype("credit");
        exRoomOrder.setCreditcardtype("credit card type");
        exRoomOrder.setCreditcardname(null);
        exRoomOrder.setCreditcardno(null);
        exRoomOrder.setCreditcardexpires(null);
        exRoomOrder.setOrderid(order.getID());
        exRoomOrder.setOrdercd(order.getOrderCD());
        exRoomOrder.setOrdercdhotel(order.getOrderCDHotel());// 订单CD -- 给酒店发传真用

        if (null == order.getCreateDate()) {
            exRoomOrder.setCreatedate(DateUtil.toStringByFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        } else {
            exRoomOrder.setCreatedate(DateUtil.toStringByFormat(order.getCreateDate(),
                "yyyy-MM-dd HH:mm:ss"));
        }

        float totalamount = (float) 0.0;
        List<OrOrderItem> orderItemList = order.getOrderItems();
        for (Iterator itr = orderItemList.iterator(); itr.hasNext();) {
            OrOrderItem orderItem = (OrOrderItem) itr.next();
            MGExOrderItem mgExOrderItem = new MGExOrderItem();
            mgExOrderItem.setNumber(orderItem.getRoomIndex());
            mgExOrderItem.setCheckindate(DateUtil.toStringByFormat(orderItem.getNight(),
                "yyyy-MM-dd"));
            mgExOrderItem.setCheckoutdate(DateUtil.toStringByFormat(orderItem.getNight(),
                "yyyy-MM-dd"));
            mgExOrderItem.setGuests(order.getFellowNames());
            if (null != orderItem.getRoomNo()) {
                mgExOrderItem.setRoomno(Integer.valueOf(orderItem.getRoomNo()));
            }
            mgExOrderItem.setBaseprice(Double.valueOf(orderItem.getBasePrice()).floatValue());
            mgExOrderItem.setSaleprice(Double.valueOf(orderItem.getSalePrice()).floatValue());
            // add by shizhongwen 2009-2-3如果订单为预付
            if (order.getPayMethod().equals(PayMethod.PRE_PAY)) {
                totalamount += Double.valueOf(orderItem.getBasePrice()).floatValue();
            } else { // 订单为面付
                totalamount += Double.valueOf(orderItem.getSalePrice()).floatValue();
            }
            mgExOrderItem.setBaserate((float) 1.0);
            // mgExOrderItem.setTotalcharges((float)orderItem.getServiceFee());
            // mgExOrderItem.setSpecialnote(orderItem.getSpecialNote());
            mgExOrderItem.setCreatetime(exRoomOrder.getCreatedate());
            mgExOrderItem.setQuantity(1);
            mgExOrderItem.setSum(Float.parseFloat(sum));
            mgExOrderItem.setNoteresult(orderItem.getNoteResult());
            mgExOrderItem.setOrderid(order.getID());
            mgExOrderItem.setOrderstate(String.valueOf(order.getOrderState()));
            mgExOrderItem.setHotelconfirm(null);
            mgExOrderItem.setHotelconfirmid(null);
            exRoomOrder.getExOrderItems().add(mgExOrderItem);
        }

        // 赋值总的订单金额
        exRoomOrder.setTotalamount(totalamount);
        exRoomOrder.setLocaltotalamout(totalamount * Double.valueOf(
            order.getRateId()).floatValue());

        addExRoomOrderRequest.setRoomOrder(exRoomOrder);
        return addExRoomOrderRequest;
    }

    /**
     * 封装订单信息(将datemap_sale,datemap_base,sum中的数据封装到OrOrder) add by shizhongwen 时间:Feb 4, 2009
     * 6:15:02 PM
     * 
     * @param order
     * @param datemap_sale
     * @param datemap_base
     * @return
     */
    public OrOrder getNewOrOrder(OrOrder order, Map datemap_sale, Map datemap_base, String sum) {
        List<OrOrderItem> orderItemList = order.getOrderItems();
        for (Iterator itr = orderItemList.iterator(); itr.hasNext();) {

            OrOrderItem orderItem = (OrOrderItem) itr.next();
            MGExOrderItem mgExOrderItem = new MGExOrderItem();
            mgExOrderItem.setNumber(orderItem.getRoomIndex());
            mgExOrderItem.setCheckindate(DateUtil.toStringByFormat(orderItem.getNight(),
                "yyyy-MM-dd"));
            mgExOrderItem.setCheckoutdate(DateUtil.toStringByFormat(orderItem.getNight(),
                "yyyy-MM-dd"));
            Float baseprice = (Float) datemap_base.get(mgExOrderItem.getCheckindate());
            mgExOrderItem.setBaseprice(baseprice);
            // add by shizhongwen 2009-2-3如果订单为预付
            if (order.getPayMethod().equals(PayMethod.PRE_PAY)) {
                // 将所获得的新销售价赋值到订单中
                orderItem.setSalePrice(Double.parseDouble(datemap_sale.get(
                    mgExOrderItem.getCheckindate()).toString()));

            } else { // 订单为面付
                datemap_sale.get(mgExOrderItem.getCheckindate());
                // 将所获得的新销售价赋值到订单中
                orderItem.setSalePrice(((Float) datemap_sale.get(mgExOrderItem.getCheckindate()))
                    .doubleValue());
            }
            orderItem.setBasePrice(baseprice.doubleValue());
        }
        order.setSum(Double.parseDouble(sum));
        order.setSumRmb(Double.parseDouble(sum) * order.getRateId());
        return order;
    }

    /**
     * 取得两日期之间的天数,并将该天与销售价格绑定 add by shizhongwen 时间:Feb 3, 2009 10:47:07 AM
     * 
     * @param checkInDate
     *            起始日期
     * @param checkOutDate
     *            终止日期
     * @param params
     *            页面所传过来的参数(Map)
     * @param pricetype
     *            价格类型:(SalePrice,BasePrice)
     * @return
     * @throws NumberFormatException
     *             如 map中存放 key '2009-02-3' value='480' 时间与当天的价格金额一一对应
     */
    public Map dateChange(Date checkInDate, Date checkOutDate, Map params, String pricetype)
        throws NumberFormatException {
        List<String> dateStringlist = getlistdate(checkInDate, checkOutDate);
        int difdays = DateUtil.getDay(checkInDate, checkOutDate);
        // 日期和当天的销售价格绑定 存放datemap
        Map datemap = new HashMap();
        for (int i = 0; i < difdays; i++) {
            if (pricetype.equals("SalePrice")) {
                if (null == params.get("hSalePrice" + i)) {
                    datemap.put(dateStringlist.get(i), (float) 0.0);
                } else {
                    datemap.put(dateStringlist.get(i), Float.parseFloat((String) params
                        .get("hSalePrice" + i)));
                }
            } else {
                if (null == params.get("hBasePrice" + i)) {
                    datemap.put(dateStringlist.get(i), (float) 0.0);
                } else {
                    datemap.put(dateStringlist.get(i), Float.parseFloat((String) params
                        .get("hBasePrice" + i)));
                }
            }

        }
        return datemap;
    }

    /**
     * 将两日期之间天数转为String 存放list中 add by shizhongwen 时间:Feb 3, 2009 10:57:25 AM
     * 
     * @param checkInDate
     *            入住日期
     * @param checkOutDate
     *            离店日期
     * @return 为日期list 如:list存放 ("2009-02-03","2009-02-04","2009-02-05")
     */
    private List<String> getlistdate(Date checkInDate, Date checkOutDate) {
        // 取得两日期之间的date
        List datelist = DateUtil.getDates(checkInDate, checkOutDate);

        // 将两日期之间的date转换为 String 存放在dateStringlist中
        List<String> dateStringlist = new ArrayList<String>();
        String datestring = "";
        for (Object object : datelist) {
            datestring = DateUtil.toStringByFormat((Date) object, "yyyy-MM-dd");
            dateStringlist.add(datestring);
        }
        return dateStringlist;
    }

    public void setDatemap_sale(Map datemap_sale) {
        this.datemap_sale = datemap_sale;
    }

    public Map getDatemap_sale() {
        return datemap_sale;
    }

    public void setDatemap_base(Map datemap_base) {
        this.datemap_base = datemap_base;
    }

    public Map getDatemap_base() {
        return datemap_base;
    }

}
