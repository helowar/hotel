/**
 *  Modified by chenkeming@2007.05.08
 *  
 *  日审记录明细表
 */

package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 */
public class AuditItem implements Entity {

    // 日审明细ID
    private Long ID;

    // 日审ID <fk> 和Audit关联
    private Audit audit;    

    // 房间号
    private String roomNo;

    // 入住人名称
    private String fellowName;

    // 房间日期
    private Date fellowDate;

    // 是否入住
    private String normalCheck;

    // 特别说明
    private String especiallyShow;
    
    /**
	 * 房间序号
	 */
    private int roomIndex;

    public int getRoomIndex() {
        return roomIndex;
    }

    public void setRoomIndex(int roomIndex) {
        this.roomIndex = roomIndex;
    }

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long detailId) {
        this.ID = detailId;
    }

    public String getEspeciallyShow() {
        return especiallyShow;
    }

    public void setEspeciallyShow(String especiallyShow) {
        this.especiallyShow = especiallyShow;
    }

    public Date getFellowDate() {
        return fellowDate;
    }

    public void setFellowDate(Date fellowDate) {
        this.fellowDate = fellowDate;
    }

    public String getFellowName() {
        return fellowName;
    }

    public void setFellowName(String fellowName) {
        this.fellowName = fellowName;
    }

    public String getNormalCheck() {
        return normalCheck;
    }

    public void setNormalCheck(String normalCheck) {
        this.normalCheck = normalCheck;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

}
