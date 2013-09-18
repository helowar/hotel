/**
 * 
 *  配额明细表
 */

package com.mangocity.hotel.order.persistence;

import com.mangocity.util.Entity;


/**
 */
public class QuotaDetail implements Entity {

    // ID <pk>
    private Long ID;

    // 订单ID <fk> 和Order关联
    private Order order;

    // 房间ID
    private Long productId;

    // 配额ID
    private Long quotaId;

    // 配额类型
    private String quotaType;

    // 配额数量
    private int quantity;

    // 配额批次
    private Long batchId;

    // cut-off-day
    private int cutoff;

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public int getCutoff() {
        return cutoff;
    }

    public void setCutoff(int cutoff) {
        this.cutoff = cutoff;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long quotaDetailId) {
        this.ID = quotaDetailId;
    }

    public Long getQuotaId() {
        return quotaId;
    }

    public void setQuotaId(Long quotaId) {
        this.quotaId = quotaId;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


}
