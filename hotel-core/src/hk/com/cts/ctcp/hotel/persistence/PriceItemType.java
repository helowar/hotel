package hk.com.cts.ctcp.hotel.persistence;

import java.util.Date;

/**
 * 用于更新价格表的对象类
 * 
 * @author:shizhongwen 创建日期:Mar 17, 2009,6:08:27 PM 描述：
 */
public class PriceItemType {

    private Date night;

    private float basePrice;

    private float salePrice;

    public PriceItemType() {
    }

    public PriceItemType(Date night, float basePrice, float salePrice) {
        this.night = night;
        this.basePrice = basePrice;
        this.salePrice = salePrice;
    }

    public float getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(float basePrice) {
        this.basePrice = basePrice;
    }

    public float getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(float salePrice) {
        this.salePrice = salePrice;
    }

    public Date getNight() {
        return night;
    }

    public void setNight(Date night) {
        this.night = night;
    }

}
