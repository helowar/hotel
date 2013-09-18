package com.mangocity.tmc.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.persistence.OrPriceDetail;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.util.DateUtil;
import com.mangocity.util.log.MyLog;

/**
 * Tmc Order辅助类 
 * @author chenkeming
 *
 */
public class TmcOrderUtil {
    
	private static final MyLog log = MyLog.getLogger(TmcOrderUtil.class);
    
    /**
     * Normand 将查询预定的酒店价格列表赋值到OrOrder中
     * add by shizhongwen
     * 时间:Sep 8, 2009  3:56:35 PM
     * @param priceLis
     * @return
     */
    public static void getOrOrderbyHotelForWebSaleItems(List<QueryHotelForWebSaleItems> priceLis,OrOrder order) {
        if(null==order){
            return;
        }        
        List<OrPriceDetail> priceList = new ArrayList<OrPriceDetail>();
        for(QueryHotelForWebSaleItems saleitem:priceLis){
            if(null !=saleitem){
                OrPriceDetail priceDetail=new OrPriceDetail();
                priceDetail.setNight(saleitem.getFellowDate());//日期
                priceDetail.setSalePrice(saleitem.getSalePrice());//销售价
                priceDetail.setTmcPrice(saleitem.getTmcPirce());//商旅价
                priceDetail.setBasePrice(saleitem.getBasePrice());//底价
                priceList.add(priceDetail);
            }
        }
        order.setPriceList(priceList);
    }
    
    /**
     * 设置TMC价格到orderitem
     * @param order
     * @return
     */
    public static void setTMCPriceToOrderitem(OrOrder order) {
        
        List<OrOrderItem> orderitemLst = order.getOrderItems();//订单明细
        
        List<OrPriceDetail> priceDetailLst = order.getPriceList();//价格明细
                
        if(orderitemLst!=null && priceDetailLst!=null){
            Iterator<OrPriceDetail> iter = priceDetailLst.iterator();
            OrPriceDetail pricedetail = null;
             while(iter.hasNext()){//遍历pricelist
                 pricedetail = iter.next();
                 if(DateUtil.between(pricedetail.getNight(), order.getCheckinDate(), order.getCheckoutDate())){//在入住期间内
                    Iterator<OrOrderItem> oiLst = orderitemLst.iterator();
                    while(oiLst.hasNext()){//遍历orderitem
                        OrOrderItem orderitem = oiLst.next();
                        if(DateUtil.getDay(pricedetail.getNight(), orderitem.getNight())==0){//日期相同
                            orderitem.setTmcPrice(pricedetail.getTmcPrice());
                        }
                    }//end in while
                 }
             }//end out while
        }        
    }    
}
