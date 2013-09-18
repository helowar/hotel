package com.mangocity.hotel.order.service.assistant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 用于auditDetail.jsp的显示
 * 
 * @author chenkeming
 * 
 */
public class AuditDetailInfo implements Serializable {

    /**
     * 房间号
     */
    private String roomNo = "";

    /**
     * AuditDetailRowInfo的列表
     */
    private List rows = new ArrayList();

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

}
