package com.mangocity.hweb.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.mangocity.hotel.order.constant.ConfirmType;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotelweb.persistence.HotelOrderFromBean;
import com.mangocity.hweb.persistence.AdditionalServeItem;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.MyBeanUtil;

/**
 */
public class FormartHwebUtil {

    /**
     * 显示格式为：1/2-3/2：￥XX元/晚；4/2、6/2：￥XX元/晚；5/2：￥XX元/晚 * modified by wuyun 2009-06-10 v2.6
     */
    public static List setPriceTemplistUtil(List priceLis, Date checkinDate, Date checkoutDate) {
        List priceTemplist = new ArrayList();
        // m:入住日期标识、n:离店日期前一天的标识
        int m = 0, n = 0;
        if (null != priceLis) {
            int k = 0;
            for (; k < priceLis.size(); k++) {
                QueryHotelForWebSaleItems it = (QueryHotelForWebSaleItems) priceLis.get(k);
                if (0 == it.getFellowDate().compareTo(checkinDate)) {
                    m = k;
                    // 价格集合中可能没有离店日期的价格，如果有，则设置离店日期前一天的标识
                } else if (0 == it.getFellowDate().compareTo(checkoutDate)) {
                    n = k - 1;
                    break;
                }
            }
            // n为0表明没有离店日期的价格,或者只预订了1天且入住日期明细的index为0
            if (0 == n) {
                n = k - 1;
            }

            String dateString = new String();
            Set priceSet = new HashSet();
            List alist = new ArrayList();
            if (m == n) {// 如果只入住1天
                QueryHotelForWebSaleItems item = (QueryHotelForWebSaleItems) priceLis.get(n);
                dateString = DateUtil.formatDateToMD(item.getFellowDate());
                AdditionalServeItem asi = new AdditionalServeItem();
                asi.setAmount(item.getSalePrice());
                asi.setValidDate(dateString);
                priceSet.add(Double.valueOf(asi.getAmount()));
                alist.add(asi);
            } else {
                for (int i = m; i <= n; i++) {
                    // 当前价格明细
                    QueryHotelForWebSaleItems item0 = (QueryHotelForWebSaleItems) priceLis.get(i);
                    String currentDateStr = new String();
                    if (i != n) {
                        for (int j = i + 1; j <= n; j++) {
                            // 下一个价格明细
                            QueryHotelForWebSaleItems item1 = (QueryHotelForWebSaleItems) priceLis
                                .get(j);
                            // 如果当前明细与下一个明细的价格不相等
                            if (0 != Double.compare(item0.getSalePrice(), item1.getSalePrice())) {
                                if (j == i + 1) {// 如果两个明细相邻
                                    // 因为价格不等，且又是相邻，则当前价格明细为独立一条数据
                                    currentDateStr = DateUtil.formatDateToMD(item0.getFellowDate());
                                } else {// 如果不相邻
                                    // 因为价格不等，且不相邻，则为一个区间
                                    currentDateStr = DateUtil.formatDateToMD(item0.getFellowDate())
                                        + "-"
                                        + DateUtil
                                            .formatDateToMD(((QueryHotelForWebSaleItems) priceLis
                                                .get(j - 1)).getFellowDate());
                                }
                                AdditionalServeItem asi = new AdditionalServeItem();
                                asi.setAmount(item0.getSalePrice());
                                asi.setValidDate(currentDateStr);
                                priceSet.add(Double.valueOf(asi.getAmount()));
                                alist.add(asi);
                                i = j - 1;// parasoft-suppress PB.FLVA "业务需要"
                                break;
                            } else if (j == n) {// 如果当前明细与下一个明细的价格相等且已经到了最后一条明细
                                currentDateStr = DateUtil.formatDateToMD(item0.getFellowDate())
                                    + "-"
                                    + DateUtil.formatDateToMD(((QueryHotelForWebSaleItems) priceLis
                                        .get(j)).getFellowDate());
                                AdditionalServeItem asi = new AdditionalServeItem();
                                asi.setAmount(item0.getSalePrice());
                                asi.setValidDate(currentDateStr);
                                priceSet.add(Double.valueOf(asi.getAmount()));
                                alist.add(asi);
                                i = j;// parasoft-suppress PB.FLVA "业务需要"
                                break;
                            }
                        }
                    } else {// 如果为最后一天，则为单独一条
                        currentDateStr = DateUtil.formatDateToMD(item0.getFellowDate());
                        AdditionalServeItem asi = new AdditionalServeItem();
                        asi.setAmount(item0.getSalePrice());
                        asi.setValidDate(currentDateStr);
                        priceSet.add(Double.valueOf(asi.getAmount()));
                        alist.add(asi);
                    }
                }
            }

            for (Iterator it = priceSet.iterator(); it.hasNext();) {
                Double d = (Double) it.next();
                StringBuffer buffer = new StringBuffer();
                for (int p = 0; p < alist.size(); p++) {
                    AdditionalServeItem item = (AdditionalServeItem) alist.get(p);
                    if (0 == Double.compare(item.getAmount(), d.doubleValue())) {
                        buffer.append(item.getValidDate() + "、");
                    }
                }
                AdditionalServeItem newitem = new AdditionalServeItem();
                newitem.setAmount(d.doubleValue());
                newitem.setValidDate(buffer.toString().substring(0,
                    buffer.toString().lastIndexOf("、")));
                priceTemplist.add(newitem);
            }
        }
        return priceTemplist;
    }

    /**
     * 装配订单基本信息
     */
    public static HotelOrderFromBean pupulateFromBean(OrOrder order) {
        HotelOrderFromBean hotelOrderFromBean = new HotelOrderFromBean();
        // 复制
        MyBeanUtil.copyProperties(hotelOrderFromBean, order);
        // 币种
        hotelOrderFromBean.setCurrency(order.getPaymentCurrency());
        // 入住人
        hotelOrderFromBean.setFellowList(order.getFellowList());
        // 订房人民币总价格
        hotelOrderFromBean.setPriceNum(order.getSumRmb());
        // 是香港中科
        hotelOrderFromBean.setFlagCtsHK(true);
        // 是香港中科预付
        hotelOrderFromBean.setRoomChannel(8);
        // 晚数
        int days = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
        hotelOrderFromBean.setDays(days);
        // 确认方式
        if (order.getConfirmType() == ConfirmType.FAX) {// 传真
            hotelOrderFromBean.setConfirmtype("1");
        } else if (order.getConfirmType() == ConfirmType.EMAIL) {// 电子邮件确认
            hotelOrderFromBean.setConfirmtype("3");
        } else if (order.getConfirmType() == ConfirmType.SMS) {// 短信确认
            hotelOrderFromBean.setConfirmtype("4");
        } else if (order.getConfirmType() == ConfirmType.PHONE) {// 电话确认
            hotelOrderFromBean.setConfirmtype("2");
        }
        // 城市
        hotelOrderFromBean.setCityId(order.getCity());

        return hotelOrderFromBean;
    }

}
