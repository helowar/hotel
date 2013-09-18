/**
 * DaAuditingWorkload entity.
 * 审核(回访)工作量总表
 * @author MyEclipse Persistence Tools
 */

package com.mangocity.hotel.order.persistence;



import java.io.Serializable;

import com.mangocity.util.Entity;

/**
 */
public class DaAuditingWorkload implements Entity, Serializable {

    // Fields

    private Long ID;
    /**
	 * 酒店量会员量
	 */
    private Long hotelmemberamount;
    /**
	 * 订单量
	 */
    private Long orderamount;
    /**
	 * 房间量
	 */
    private Long roomamount;
    /**
	 * 类型:回访/审核
	 */
    private Long type;
    /**
	 * 内容类型,三种:1:今天需审核/2:历史未审核/3:今天已完成
	 */
    private Long contenttype;

    // Constructors

    /** default constructor */
    public DaAuditingWorkload() {
    }

    /** minimal constructor */
    public DaAuditingWorkload(Long auditingWorkloadId) {
        this.ID = auditingWorkloadId;
    }

    /** full constructor */
    public DaAuditingWorkload(Long auditingWorkloadId, Long hotelmemberamount,
            Long orderamount, Long roomamount, Long type, Long contenttype) {
        this.ID = auditingWorkloadId;
        this.hotelmemberamount = hotelmemberamount;
        this.orderamount = orderamount;
        this.roomamount = roomamount;
        this.type = type;
        this.contenttype = contenttype;
    }

    // Property accessors

    public Long getHotelmemberamount() {
        return this.hotelmemberamount;
    }

    public void setHotelmemberamount(Long hotelmemberamount) {
        this.hotelmemberamount = hotelmemberamount;
    }

    public Long getOrderamount() {
        return this.orderamount;
    }

    public void setOrderamount(Long orderamount) {
        this.orderamount = orderamount;
    }

    public Long getRoomamount() {
        return this.roomamount;
    }

    public void setRoomamount(Long roomamount) {
        this.roomamount = roomamount;
    }

    public Long getType() {
        return this.type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getContenttype() {
        return this.contenttype;
    }

    public void setContenttype(Long contenttype) {
        this.contenttype = contenttype;
    }

    public Long getID() {
        // TODO Auto-generated method stub
        return this.ID;
    }

    public void setID(Long id) {
        ID = id;
    }

}